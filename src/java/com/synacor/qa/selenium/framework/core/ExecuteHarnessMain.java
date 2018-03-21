package com.synacor.qa.selenium.framework.core;

import java.awt.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.*;
import java.util.regex.*;
import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import org.testng.xml.*;
import com.synacor.qa.selenium.framework.ui.AbsSeleniumObject;
import com.synacor.qa.selenium.framework.ui.AbsTab;
import com.synacor.qa.selenium.framework.util.*;
import com.synacor.qa.selenium.framework.util.ConfigProperties.AppType;
import com.synacor.qa.selenium.framework.util.staf.*;
import com.synacor.qa.selenium.staf.StafIntegration;

public class ExecuteHarnessMain {

	private static Logger logger = LogManager.getLogger(ExecuteHarnessMain.class);
	public static final String TraceLoggerName = "testcase.trace";
	public static Logger tracer = LogManager.getLogger(TraceLoggerName);
	private static HashMap<String, String> configMap = new HashMap<String, String>();

	public static int testsTotal = 0;
	public static int testsPassed = 0;
	public static int testsFailed = 0;
	public static int testsSkipped = 0;
	public static int testsRetried = 0;
	public static int testsCount = 0;
	public static StringBuilder testsCountSummary = new StringBuilder();

	public static int currentRunningTest = 1;
	public static int retryLimit = 2;
	public static Boolean isTestRetried = false;

	public static Date testStartTime;
	public static Date testEndTime;
	public static int testTotalSeconds;
	public static String testTotalMinutes;
	protected AbsTab startingPage = null;

	public static String startAttVersion;

	public ExecuteHarnessMain() {
	}

	public int verbosity = 10;
	public static boolean DO_TEST_CASE_SUM = false;
	public static String TEST_TOKEN = ".tests.";
	public String jarfilename;
	public static String classfilter = null;
	public String excludefilter = null;
	public static ArrayList<String> groups = new ArrayList<String>(Arrays.asList("always", "sanity"));
	public ArrayList<String> excludeGroups = new ArrayList<String>(Arrays.asList("skip"));
	public static HashSet<String> retriedTests = new HashSet<String>();

	private static final String OpenQABasePackage = "org.openqa";
	public static final String SeleniumBasePackage = "com.synacor.qa.selenium";
	public static String testoutputfoldername = null;
	public static File fTestOutputDirectory;
	public static ResultListener currentResultListener = null;

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hhmmss");
	public static String currentDateTime = simpleDateFormat.format(new Date());

	public void setTestOutputFolderName(String path) {

		System.setProperty("outputDirectory", path);

		// Append the app and browser
		path += "/" + ConfigProperties.getAppType() + "/" + ConfigProperties.getStringProperty("browser");

		// Make sure the path exists
		File output = new File(path);
		if (!output.exists())
			output.mkdirs();

		// Set the property to the absolute path
		try {
			testoutputfoldername = output.getCanonicalPath();
		} catch (IOException e) {
			logger.warn("Unable to get canonical path of the test output folder (" + e.getMessage()
					+ "). Using absolute path.");
			testoutputfoldername = output.getAbsolutePath();
		}

		// Make sure any other dependent folders exist
		File debug = new File(testoutputfoldername + "/debug");
		if (!debug.exists())
			debug.mkdirs();

		File testng = new File(testoutputfoldername + "/TestNG");
		if (!testng.exists())
			testng.mkdirs();
	}

	public String workingfoldername = ".";
	protected List<String> classes = null;

	private static List<String> getClassesFromJar(File jarfile, Pattern pattern, String excludeStr)
			throws FileNotFoundException, IOException, HarnessException {

		logger.debug("getClassesFromJar " + jarfile.getAbsolutePath());

		List<String> classes = new ArrayList<String>();

		JarInputStream jarFile = null;
		try {

			jarFile = new JarInputStream(new FileInputStream(jarfile));

			while (true) {
				JarEntry jarEntry = jarFile.getNextJarEntry();

				if (jarEntry == null)
					break; // All Done!

				if (!jarEntry.getName().endsWith(".class"))
					continue; // Only process classes

				if (jarEntry.getName().contains("CommonTest.class"))
					continue; // Skip CommonTest, since those aren't tests

				String name = jarEntry.getName().replace('/', '.').replaceAll(".class$", "");
				logger.debug("Class: " + name);

				if (pattern != null) {

					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {

						// Class name matched the filter. add it.
						if (!isExcluded(name, excludeStr)) {
							classes.add(name);
						}
					}

				} else {

					// No filter. add all.
					if (!isExcluded(name, excludeStr)) {
						classes.add(name);
					}
				}
			}

		} finally {
			if (jarFile != null) {
				jarFile.close();
				jarFile = null;
			}
		}

		if (classes.size() < 1) {
			throw new HarnessException("no classes matched pattern filter " + pattern.pattern());
		}

		return (classes);
	}

	private static boolean isExcluded(String name, String excludeStr) {
		boolean result = false;

		if (excludeStr == null) {
			return result;
		}

		if (excludeStr.indexOf(";") == -1) {
			result = (name.indexOf(excludeStr) != -1);
		} else {
			String[] splitStr = excludeStr.split(";");
			for (int j = 0; j < splitStr.length; j++) {
				if (result = (name.indexOf(splitStr[j]) != -1)) {
					break;
				}
			}
		}
		return result;
	}

	private static String getTestName(String classname) throws HarnessException {
		String token = TEST_TOKEN;

		int indexOfTests = classname.indexOf(token);
		if (indexOfTests < 0)
			throw new HarnessException("class names must contain " + token + " (" + classname + ")");

		int indexOfDot = classname.indexOf('.', indexOfTests + token.length());
		if (indexOfDot < 0)
			throw new HarnessException("class name doesn't contain ending dot (" + classname + ")");

		String testname = classname.substring(indexOfTests + token.length(), indexOfDot);
		logger.debug("testname: " + testname);

		return (testname);
	}

	protected List<String> getXmlTestNames() throws HarnessException {

		List<String> testnames = new ArrayList<String>();
		for (String c : classes) {
			String testname = getTestName(c);
			if (!testnames.contains(testname)) {
				logger.debug("Add new testname " + testname);
				testnames.add(testname);
			}
		}
		return (testnames);
	}

	protected List<XmlSuite> getXmlSuiteList() throws HarnessException {

		// Add network or foss based on the server version
		if (ConfigProperties.startAttGetVersionString().toLowerCase().contains("network")) {
			excludeGroups.add("foss");
		} else {
			excludeGroups.add("network");
		}

		if (OperatingSystem.isWindows() == false || ConfigProperties.getStringProperty("browser").contains("edge")) {
			excludeGroups.add("upload");
		}

		// Only one suite per run (subject to change)
		XmlSuite suite = new XmlSuite();
		suite.setName(ConfigProperties.getAppType().toString());
		suite.setVerbose(verbosity);
		suite.setThreadCount(4);
		suite.setParallel(XmlSuite.PARALLEL_NONE);

		// Add all the names per the list of classes
		for (String testname : getXmlTestNames()) {
			XmlTest test = new XmlTest(suite);
			test.setName(testname);
			test.setIncludedGroups(groups);
			test.setExcludedGroups(excludeGroups);
		}

		// Add all the classes per the appropriate test name
		for (String c : classes) {
			String testname = getTestName(c);
			for (XmlTest test : suite.getTests()) {
				if (test.getName().equals(testname)) {

					XmlClass x = new XmlClass(c);
					test.getXmlClasses().add(x);

					break;
				}
			}
		}
		return (Arrays.asList(suite));
	}

	public String execute() throws HarnessException, FileNotFoundException, IOException {
		logger.info("Execute ...");

		Date start = new Date();
		Date finish;
		
		// Harness log
		StafIntegration.sHarnessLogFileFolderPath = testoutputfoldername + "/debug/projects";
		StafIntegration.sHarnessLogFilePath = StafIntegration.sHarnessLogFileFolderPath + "/" + StafIntegration.sHarnessLogFileName;
		StafIntegration.pHarnessLogFilePath = Paths.get(StafIntegration.sHarnessLogFileFolderPath, StafIntegration.sHarnessLogFileName);
		StafIntegration.fHarnessLogFile = new File(StafIntegration.sHarnessLogFilePath);
		StafIntegration.fHarnessLogFileFolder = new File(StafIntegration.sHarnessLogFileFolderPath);

		// Create harness log folder and file
		StafIntegration.fHarnessLogFileFolder.mkdirs();
		if (!StafIntegration.fHarnessLogFile.exists()) {
			StafIntegration.fHarnessLogFile.createNewFile();
		}

		StringBuilder result = new StringBuilder();

		try {
			String response;
			response = executeSelenium();
			result.append(response).append('\n');

		} finally {
			finish = new Date();
		}

		// Calculate how long the tests execution took
		long duration = finish.getTime() - start.getTime();

		long days = TimeUnit.MILLISECONDS.toDays(duration);
		duration -= TimeUnit.DAYS.toMillis(days);

		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		duration -= TimeUnit.HOURS.toMillis(hours);

		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		duration -= TimeUnit.MINUTES.toMillis(minutes);

		if (days >= 1) {
			result.append("Duration: ").append(days).append(" day(s) ").append(hours).append(" hour(s)\n");
		} else if (hours >= 1) {
			result.append("Duration: ").append(hours).append(" hour(s) ").append(minutes).append(" minute(s)\n");
		} else {
			result.append("Duration: ").append(minutes).append(" minutes\n");
		}
		result.append("Browser: ").append(ConfigProperties.getStringProperty("browser"));

		return (result.toString());
	}

	protected String executeSelenium() throws HarnessException, FileNotFoundException, IOException {
		try {
			SeleniumService.getInstance().startSeleniumExecution();
			return (executeTests());

		} finally {
			SeleniumService.getInstance().stopSeleniumExecution();
		}
	}

	protected void getRemoteFile(String remotehost, String fromfile, String tofile) throws HarnessException {
		logger.info("getRemoteFile(" + remotehost + ", " + fromfile + ", " + tofile + ")");

		// Create the folder, if it doesn't exist
		File file = new File(tofile);
		file.getParentFile().mkdirs();

		String localhost = getLocalMachineName();

		StafServiceFS staf = new StafServiceFS(remotehost);
		staf.execute("COPY FILE " + fromfile + " TOFILE " + tofile + " TOMACHINE " + localhost);
	}

	protected static String getLocalMachineName() throws HarnessException {
		logger.info("getLocalMachineName()");

		StafServiceVAR staf = new StafServiceVAR();
		staf.execute();
		return (staf.getStafResponse());
	}

	protected String executeTests() throws FileNotFoundException, IOException, HarnessException {

		logger.info("Execute tests ...");

		try {
			// Build the class list
			classes = getClassesFromJar(new File(jarfilename),
					(classfilter == null ? null : Pattern.compile(classfilter)), excludefilter);

			// Build the list of XmlSuites
			List<XmlSuite> suites = getXmlSuiteList();

			// Create the TestNG test runner
			TestNG testNG = new TestNG();

			for (String st : configMap.keySet()) {
				ConfigProperties.setStringProperty(st, configMap.get(st));
			}

			// Keep checking for server down
			while (ConfigProperties.startAttGetVersionString().indexOf("unknown") != -1) {
				SleepUtil.sleep(100000);
			}

			// Configure the runner
			testNG.setXmlSuites(suites);
			testNG.addListener(new MethodListener(ExecuteHarnessMain.testoutputfoldername));
			testNG.addListener(new ErrorDialogListener());
			testNG.addListener(currentResultListener = new ResultListener(ExecuteHarnessMain.testoutputfoldername));
			testNG.addListener(new AnnotationTransformer());
			testNG.addListener(new TestListener());

			try {
				testNG.setOutputDirectory(ExecuteHarnessMain.testoutputfoldername + "/TestNG");
			} catch (Exception e) {
				throw new HarnessException(e);
			}

			// Run!
			testNG.run();

			// Finish inProgress - overwrite inProgress/index.html
			TestStatusReporter.copyFile(testoutputfoldername + "/TestNG/emailable-report.html",
					testoutputfoldername + "/TestNG/index.html");

			logger.info("Execute tests ... completed");

			SleepMetrics.report();

			return (currentResultListener == null ? "Done" : currentResultListener.getResults());

		} finally {

			testsTotal = 0;
			testsPassed = 0;
			testsFailed = 0;
			testsSkipped = 0;
			testsRetried = 0;

			currentResultListener = null;
		}
	}

	public String sumTestCounts() throws FileNotFoundException, IOException, HarnessException {

		logger.debug("sumTestCounts");

		StringBuilder stringBuilder = new StringBuilder();
		int sum = 0;

		List<String> classes = ExecuteHarnessMain.getClassesFromJar(new File(jarfilename),
				(classfilter == null ? null : Pattern.compile(classfilter)), excludefilter);

		for (String s : classes) {

			try {

				Class<?> currentClass = Class.forName(s);
				logger.debug("sumTestCounts: checking class: " + currentClass.getCanonicalName());

				for (Method method : Arrays.asList(currentClass.getDeclaredMethods())) {

					logger.debug("sumTestCounts: checking method: " + method.getName());

					for (Annotation annotation : Arrays.asList(method.getAnnotations())) {

						logger.debug("sumTestCounts: checking annotation: " + annotation.toString());

						if (annotation instanceof org.testng.annotations.Test) {

							org.testng.annotations.Test testAnnotation = (org.testng.annotations.Test) annotation;

							// Check the groups to make sure they match
							for (String group : Arrays.asList(testAnnotation.groups())) {

								if (ExecuteHarnessMain.groups.contains(group)) {

									logger.debug("sumTestCounts: matched: " + group);

									stringBuilder.append(++sum).append(": ").append(testAnnotation.description()).append('\n');
									continue; // for (Annotation a ...

								}
							}

						}

					}

				}

			} catch (ClassNotFoundException e) {
				logger.warn("sumTestCounts: Unable to find class", e);
			}
		}

		logger.debug("sumTestCounts: found: " + sum);

		stringBuilder.append("Number of matching test cases: " + sum);
		return (stringBuilder.toString());
	}

	protected static class ErrorDialogListener extends AbsSeleniumObject implements IInvokedMethodListener {

		@Override
		public void afterInvocation(IInvokedMethod method, ITestResult result) {

			if (method.isTestMethod()) {

				logger.info("ErrorDialogListener:afterInvocation ...");

				String locator = "css=div#ErrorDialog";

				try {

					boolean present = sIsElementPresent(locator);
					if (present) {

						logger.info("ErrorDialogListener:afterInvocation ... present=" + present);

						Number left = sGetElementPositionLeft(locator);
						if (left.intValue() > 0) {

							logger.info("ErrorDialogListener:afterInvocation ... left=" + left);

							Number top = sGetElementPositionTop(locator);

							if (top.intValue() > 0) {

								logger.info("ErrorDialogListener:afterInvocation ... top=" + top);

								// Log the error
								logger.error(new HarnessException("ExecuteHarnessMain: Error dialog is visible"));

								// Take screenshot
								getScreenCapture(result);

								// Set the test as failed
								result.setStatus(ITestResult.FAILURE);
							}
						}
					}

				} catch (Exception ex) {
					logger.error(new HarnessException("ErrorDialogListener:afterInvocation ", ex), ex);
				}

				logger.info("ErrorDialogListener:afterInvocation ... done");
			}

		}

		public static void getScreenCapture(ITestResult result) {

			String coreFolderPath, screenShotFilePath, testcase;
			testcase = result.getName().toString();

			coreFolderPath = testoutputfoldername + "/debug/projects/"
					+ ConfigProperties.getAppType().toString().toLowerCase() + "/core/";
			screenShotFilePath = coreFolderPath + testcase + ".png";

			// Make sure required folders exist
			File coreFolder = new File(testoutputfoldername + "/debug/projects/"
					+ ConfigProperties.getAppType().toString().toLowerCase() + "/core/");
			if (!coreFolder.exists())
				coreFolder.mkdirs();

			logger.info("Creating screenshot: " + screenShotFilePath);
			try {
				File scrFile = ((TakesScreenshot) ClientSessionFactory.session().webDriver())
						.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(screenShotFilePath));

			} catch (HeadlessException e) {
				logger.error("Unable to create screenshot", e);
			} catch (IOException e) {
				logger.error("IE exception when creating image file at " + screenShotFilePath, e);
			} catch (WebDriverException e) {
				logger.error("Webdriver exception when creating image file at " + screenShotFilePath, e);
			}

		}

		@Override
		public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		}

	}

	protected static class MethodListener implements IInvokedMethodListener {

		private static Logger logger = LogManager.getLogger(MethodListener.class);
		private static final Logger OpenQALogger = LogManager.getLogger(OpenQABasePackage);
		private static final Logger Logger = LogManager.getLogger(SeleniumBasePackage);

		private final Map<String, Appender> appenders = new HashMap<String, Appender>();
		private static final Layout layout = new PatternLayout("%-4r [%t] %-5p %c %x - %m%n");

		public String outputFolder = null;

		protected MethodListener(String folder) {
			outputFolder = (folder == null ? "logs" : folder);
		}

		protected String getKey(Method method) {
			return (method.getDeclaringClass().getCanonicalName());
		}

		protected String getFilename(Method method) {
			String c = method.getDeclaringClass().getCanonicalName().replace(SeleniumBasePackage, "").replace('.', '/');
			String m = method.getName();
			return (String.format("%s/debug/%s/%s.txt", outputFolder, c, m));
		}

		protected String getTestCaseID(Method method) {
			String c = method.getDeclaringClass().getCanonicalName();
			String m = method.getName();
			return (c + "." + m);
		}

		/**
		 * Add a new FileAppender for each class before invocation
		 */
		@Override public void beforeInvocation(IInvokedMethod method, ITestResult result) {

			if (method.isTestMethod()) {

				try {
					String key = getKey(method.getTestMethod().getMethod());
					if (!appenders.containsKey(key)) {
						String filename = getFilename(method.getTestMethod().getMethod());
						Appender a = new FileAppender(layout, filename, false);
						appenders.put(key, a);
						OpenQALogger.addAppender(a);
						Logger.addAppender(a);
					}

					// Log start time
					testStartTime = new Date();

					logger.info("MethodListener: START: " + getTestCaseID(method.getTestMethod().getMethod()));

					// Log the associated bugs
					Bugs b = method.getTestMethod().getMethod().getAnnotation(Bugs.class);
					if (b != null) {
						logger.info("Associated bugs: " + b.ids());
					}

					// Log the test case trace
					tracer.trace("/***");
					tracer.trace("## ID: " + getTestCaseID(method.getTestMethod().getMethod()));
					tracer.trace("# Objective: " + method.getTestMethod().getDescription());
					tracer.trace("# Group(s): " + Arrays.toString(method.getTestMethod().getGroups()));
					tracer.trace("");

				} catch (IOException e) {
					logger.warn("Unable to add test class appender", e);
				}
			}
		}

		/**
		 * Remove any FileAppenders after invocation
		 */
		@Override
		public void afterInvocation(IInvokedMethod method, ITestResult result) {

			String testStatus;
			Scanner scanner = null;

			if (result.isSuccess()) {
				testStatus = "PASSED";
			} else {
				testStatus = "FAILED";
			}

			if (method.isTestMethod()) {

				logger.info("MethodListener: FINISH: " + getTestCaseID(method.getTestMethod().getMethod()));

				tracer.trace("");
				tracer.trace("# Pass: " + result.isSuccess());
				tracer.trace("# End ID: " + getTestCaseID(method.getTestMethod().getMethod()));
				tracer.trace("***/");
				tracer.trace("");
				tracer.trace("");

				Appender a = null;
				String key = getKey(method.getTestMethod().getMethod());
				if (appenders.containsKey(key)) {
					a = appenders.get(key);
					appenders.remove(key);
				}
				if (a != null) {
					OpenQALogger.removeAppender(a);
					Logger.removeAppender(a);
					a.close();
					a = null;
				}

				// Log end time
				testEndTime = new Date();
				testTotalSeconds = (int) ((testEndTime.getTime()-testStartTime.getTime())/1000);
				testTotalMinutes = new DecimalFormat("##.##").format((float) Math.round(testTotalSeconds) / 60);

				StafIntegration.sHarnessLogFileFolderPath = testoutputfoldername + "/debug/projects";
				StafIntegration.sHarnessLogFilePath = StafIntegration.sHarnessLogFileFolderPath + "/" + StafIntegration.sHarnessLogFileName;
				StafIntegration.pHarnessLogFilePath = Paths.get(StafIntegration.sHarnessLogFileFolderPath, StafIntegration.sHarnessLogFileName);
				StafIntegration.fHarnessLogFile = new File(StafIntegration.sHarnessLogFilePath);
				StafIntegration.fHarnessLogFileFolder = new File(StafIntegration.sHarnessLogFileFolderPath);

				// Test summary
				try {
					Boolean testHeaderFound = false;
					String testHeader = "# | Test | Start Time | End Time | Duration";

					try {
						scanner = new Scanner(StafIntegration.fHarnessLogFile);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					while (scanner.hasNextLine()) {
						if (scanner.nextLine().contains(testHeader)) {
							testHeaderFound = true;
							break;
						}
					}

					if (!testHeaderFound) {
						Files.write(StafIntegration.pHarnessLogFilePath,
								Arrays.asList("\n" + testHeader), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				// Test data
				if (testStatus.equals("PASSED") || isTestRetried.equals(true)) {

					Boolean testDataFound = false;
					String testLine = null;

					try {
						scanner = new Scanner(StafIntegration.fHarnessLogFile);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					while (scanner.hasNextLine()) {
						if (scanner.nextLine().contains(method.getTestMethod().toString())) {
							testDataFound = true;
							break;
						}
					}

					if (testDataFound) {

						try {
						    testLine = IOUtils.toString(new FileInputStream(StafIntegration.sHarnessLogFilePath), Charset.forName("UTF-8"));
						} catch (IOException e) {
						    e.printStackTrace();
						}

						testLine = testLine.replace("(FAILED) " + method.getTestMethod().toString(),
								"(" + testStatus + ") " + method.getTestMethod().toString());

						try {
						    IOUtils.write(testLine, new FileOutputStream(StafIntegration.sHarnessLogFilePath), Charset.forName("UTF-8"));
						} catch (IOException e) {
						    e.printStackTrace();
						}

					} else {

						try {
							Files.write(StafIntegration.pHarnessLogFilePath,
									Arrays.asList(currentRunningTest++ + "/" + testsCount + " | (" + testStatus + ") " + method.getTestMethod() + " | " + testStartTime.toString().split(" ")[3] + " | "
											+ testEndTime.toString().split(" ")[3] + " | " + testTotalMinutes), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static class RetryAnalyzer implements IRetryAnalyzer {

		int retryCounter = 0;

		@Override
		public boolean retry(ITestResult result) {

			String testPath = result.getMethod().getMethod().getDeclaringClass().getName() + "." + result.getMethod().getMethod().getName();

			retriedTests.add(testPath);
			if (retryCounter == 1) {
				testsRetried++;
			}

			if (retryCounter < retryLimit) {
				retryCounter++;
				isTestRetried = true;
				return true;
			}

			isTestRetried = false;
			return false;
		}
	}

	 // Dynamically add retry class annotation to each test cases
	@SuppressWarnings("rawtypes")
	protected class AnnotationTransformer implements IAnnotationTransformer {

		@Override
		public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
			annotation.setRetryAnalyzer(ExecuteHarnessMain.RetryAnalyzer.class);
		}
	}

	// Listener is use to avoid conflict of count in the TestNG report
	protected class TestListener implements ITestListener {
	    @Override
		public void onFinish(ITestContext context) {
			Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
			for (ITestResult temp : failedTests) {
				ITestNGMethod method = temp.getMethod();
				if (context.getFailedTests().getResults(method).size() > 1) {
					failedTests.remove(temp);
				} else {
					if (context.getPassedTests().getResults(method).size() > 0) {
						failedTests.remove(temp);
					}
				}
			}
		}

	    public void onTestStart(ITestResult result) {   }

	    public void onTestSuccess(ITestResult result) {   }

	    public void onTestFailure(ITestResult result) {   }

	    public void onTestSkipped(ITestResult result) {   }

	    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {   }

	    public void onStart(ITestContext context) {   }
	}

	public static class ResultListener extends TestListenerAdapter {

		private static List<String> failedTests = new ArrayList<String>();
		private static List<String> skippedTests = new ArrayList<String>();

		protected ResultListener(String folder) {
			ResultListener.setOutputfolder(folder);
		}

		public String getResults() {
			StringBuilder sb = new StringBuilder();

			testsCountSummary.append("Total Tests:   ").append(testsTotal).append('\n');
			testsCountSummary.append("Total Passed:  ").append(testsPassed).append('\n');
			testsCountSummary.append("Total Failed:  ").append(testsFailed).append('\n');
			testsCountSummary.append("Total Skipped: ").append(testsSkipped).append('\n');
			testsCountSummary.append("Total Retried: ").append(retriedTests.size()).append('\n');

			sb.append(testsCountSummary);

			if (!failedTests.isEmpty()) {
				sb.append("\nFailed tests:\n");
				for (String s : failedTests) {
					sb.append(s).append('\n');
				}
			}
			if (!skippedTests.isEmpty()) {
				sb.append("\nSkipped tests:\n");
				for (String s : skippedTests) {
					sb.append(s).append('\n');
				}
			}
			if (!retriedTests.isEmpty()){
				sb.append("\nRetried tests:\n");
				for (String s : retriedTests) {
					sb.append(s).append('\n');
				}
			}
			return (sb.toString());
		}

		private static ITestResult runningTestCase = null;

		private static void setRunningTestCase(ITestResult result) {
			runningTestCase = result;
		}

		private static String outputFolder = null;

		private static void setOutputfolder(String folder) {
			outputFolder = (folder == null ? "logs" : folder);
		}

		private static int screenshotcount = 0;

		public static String getScreenCaptureFilename(Method method) {
			String c = method.getDeclaringClass().getCanonicalName().replace(SeleniumBasePackage, "").replace('.', '/');
			String m = method.getName();
			return (String.format("%s/debug/%s/%sss%d.png", outputFolder, c, m, ++screenshotcount));
		}

		public static void getScreenCapture(ITestResult result) {

			String filename = getScreenCaptureFilename(result.getMethod().getMethod());

			logger.info("Creating screenshot: " + filename);
			try {
				File scrFile = ((TakesScreenshot) ClientSessionFactory.session().webDriver())
						.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(filename));

			} catch (HeadlessException e) {
				logger.error("Unable to create screenshot", e);
			} catch (IOException e) {
				logger.error("IE exception when creating image file at " + filename, e);
			} catch (WebDriverException e) {
				logger.error("Webdriver exception when creating image file at " + filename, e);
			}

		}

		@Override
		public void onFinish(ITestContext context) {
		}

		@Override
		public void onStart(ITestContext context) {

		}

		@Override
		public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
			getScreenCapture(result);
		}

		/**
		 * Add failed tests
		 */
		@Override
		public void onTestFailure(ITestResult result) {
			testsFailed++;
			String fullname = result.getMethod().getMethod().getDeclaringClass().getName() + "."
					+ result.getMethod().getMethod().getName();
			failedTests.add(fullname + " " + Arrays.toString(result.getMethod().getGroups()));
			retriedTests.remove(fullname);
			getScreenCapture(result);
		}

		/**
		 * Add skipped tests
		 */
		@Override
		public void onTestSkipped(ITestResult result) {
			testsSkipped++;
			String fullname = result.getMethod().getMethod().getDeclaringClass().getName() + "."
					+ result.getMethod().getMethod().getName();
			skippedTests.add(fullname + " " + Arrays.toString(result.getMethod().getGroups()));
			getScreenCapture(result);
		}

		/**
		 * Total tests
		 */
		@Override
		public void onTestStart(ITestResult result) {
			setRunningTestCase(result);

			// Maintain retry testcases count
			if (testsTotal == 0) {
				testsTotal++;
			}
			if (testsTotal == testsFailed + testsPassed + testsSkipped) {
				testsTotal++;
			}
		}

		public static void captureScreen() {
			getScreenCapture(runningTestCase);
		}

		@Override
		public void onTestSuccess(ITestResult result) {
			testsPassed++;
		}

		@Override
		public void onConfigurationFailure(ITestResult result) {
			getScreenCapture(result);
		}

		@Override
		public void onConfigurationSkip(ITestResult result) {
		}

		@Override
		public void onConfigurationSuccess(ITestResult result) {
		}

	}

	private boolean parseArgs(String arguments[]) throws ParseException, HarnessException, IOException {

		// Build option list
		Options options = new Options();
		options.addOption(new Option("h", "help", false, "print usage"));
		options.addOption(new Option("l", "log4j", true, "log4j file containing log4j configuration"));
		options.addOption(new Option("j", "jarfile", true, "jarfile containing test cases"));
		options.addOption(new Option("p", "pattern", true, "class filter regex, i.e. projects.ajax.tests."));
		options.addOption(new Option("g", "groups", true,
				"comma separated list of groups to execute (always, sanity, smoke, functional)"));
		options.addOption(new Option("v", "verbose", true, "set suite verbosity (default: " + verbosity + ")"));
		options.addOption(new Option("o", "output", true, "output foldername"));
		options.addOption(new Option("w", "working", true, "current working foldername"));
		options.addOption(new Option("c", "config", true,
				"dynamic setting config properties i.e browser, server, locale... ( -c 'locale=en_US,browser=firefox' "));
		options.addOption(
				new Option("s", "sum", false, "run harness in mode to count the number of matching test cases"));
		options.addOption(new Option("e", "exclude", true, "exclude pattern  "));
		options.addOption(new Option("eg", "exclude_groups", true,
				"comma separated list of groups to exclude when execute (skip)"));

		// Set required options
		options.getOption("j").setRequired(true);

		try {
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, arguments);

			if (cmd.hasOption('h')) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ExecuteTests", options);
				return false;
			}

			if (cmd.hasOption('s')) {
				DO_TEST_CASE_SUM = true;
			}

			if (cmd.hasOption('c')) {

				String[] confArray = cmd.getOptionValues('c');

				for (int i = 0; i < confArray.length; i++) {
					// could have form: 'browser=firefox;locale=en_US'
					String[] confItems = confArray[i].split(",");

					for (int j = 0; j < confItems.length; j++) {
						String[] confItem = confItems[j].split("=");

						// check form config=value and if a valid config name
						if ((confItem.length > 1) && (ConfigProperties.getStringProperty(confItem[0]) != null)) {
							configMap.put(confItem[0], confItem[1]);

						}
					}
				}
			}

			if (cmd.hasOption('p')) {
				String filter = cmd.getOptionValue('p');
				ExecuteHarnessMain.classfilter = filter;

				// Set the app type on the properties
				for (AppType t : AppType.values()) {
					// Look for ".type." (e.g. ".ajax.") in the pattern
					if (ExecuteHarnessMain.classfilter.contains(t.toString().toLowerCase())) {
						ConfigProperties.setAppType(t);
						break;
					}
				}
			}

			if (cmd.hasOption('e')) {
				if (cmd.getOptionValue('e').length() > 0) {
					this.excludefilter = cmd.getOptionValue('e');
				}
			}

			// 'o' check should be after 'p' check to avoid code redundancy
			if (cmd.hasOption('o')) {
				setTestOutputFolderName(cmd.getOptionValue('o'));
			} else {
				setTestOutputFolderName(ConfigProperties.getStringProperty("testOutputDirectory") + "/"
						+ ConfigProperties.startAttGetVersionString());
			}

			// Processing log4j must come first so debugging can happen
			if (cmd.hasOption('l')) {
				PropertyConfigurator.configure(cmd.getOptionValue('l'));
			} else {
				BasicConfigurator.configure();
			}

			if (cmd.hasOption('j')) {
				this.jarfilename = cmd.getOptionValue('j');
			}

			if (cmd.hasOption('g')) {
				// Remove spaces and split on commas
				String[] values = cmd.getOptionValue('g').replaceAll("\\s+", "").split(",");
				ExecuteHarnessMain.groups = new ArrayList<String>(Arrays.asList(values));
			}

			if (cmd.hasOption("eg")) {
				// Remove spaces and split on commas
				String[] values = cmd.getOptionValue("eg").replaceAll("\\s+", "").split(",");
				this.excludeGroups = new ArrayList<String>(Arrays.asList(values));
			}

			if (cmd.hasOption('v')) {
				this.verbosity = Integer.parseInt(cmd.getOptionValue('v'));
			}

			if (cmd.hasOption('w')) {
				workingfoldername = cmd.getOptionValue('w');
			}

		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ExecuteTests", options);
			throw e;
		}

		return (true);
	}

	public static void main(String[] args) throws HarnessException, IOException {

		String sumTestsResult = "No results";
		String executeTestsResult = "No results";

		BasicConfigurator.configure();

		try {

			// Set the working conditions
			ConfigProperties.setBaseDirectory("");
			ConfigProperties.setConfigProperties("conf/config.properties");

			for (AppType appType : AppType.values()) {
				if (args[3].contains(appType.toString().toLowerCase()) ) {
	        		ConfigProperties.setAppType(appType);
	            	break;
	        	}
	        }

			// Create the harness object and execute it
			ExecuteHarnessMain harness = new ExecuteHarnessMain();
			if (harness.parseArgs(args)) {
				if (DO_TEST_CASE_SUM) {
					// Sum
					executeTestsResult = harness.sumTestCounts();
				} else {
					// Sum
					sumTestsResult = harness.sumTestCounts();
					String[] splitSumTestsResult = sumTestsResult.split("Number of matching test cases: ");
					testsCount = Integer.parseInt(splitSumTestsResult[1]);

					// Execute
					executeTestsResult = harness.execute();
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			DO_TEST_CASE_SUM = false;
		}

		System.out.println("\n*****\n" + executeTestsResult);

		try {
			Files.write(StafIntegration.pHarnessLogFilePath, Arrays.asList("\n\n" + testsCountSummary),
					Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}
}