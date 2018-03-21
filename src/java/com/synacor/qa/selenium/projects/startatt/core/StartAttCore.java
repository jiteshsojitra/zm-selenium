package com.synacor.qa.selenium.projects.startatt.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.testng.*;
import org.testng.annotations.*;
import org.xml.sax.SAXException;
import com.synacor.qa.selenium.framework.core.*;
import com.synacor.qa.selenium.framework.ui.*;
import com.synacor.qa.selenium.framework.util.*;
import com.synacor.qa.selenium.framework.util.staf.StafServicePROCESS;
import com.synacor.qa.selenium.projects.startatt.pages.StartAttPages;

public class StartAttCore {

	protected StartAttPages app = null;
	protected AbsTab startingPage = null;

	protected Map<String, String> startingAccountPreferences = null;
	protected Map<String, String> startingUserPreferences = null;
	protected Map<String, String> startingUserZimletPreferences = null;

	public static boolean organizerTest = false;
	public static boolean allDayTest = false;

	WebElement we = null;
	protected WebDriver webDriver = ClientSessionFactory.session().webDriver();
	protected static Logger logger = LogManager.getLogger(StartAttCore.class);

	protected StafServicePROCESS staf = new StafServicePROCESS();

	protected StartAttCore() {
		logger.info("New " + StartAttCore.class.getCanonicalName());

		app = new StartAttPages();

		startingPage = app.zPageMain;
		startingAccountPreferences = new HashMap<String, String>();
		startingUserZimletPreferences = new HashMap<String, String>();
	}

	@BeforeSuite(groups = { "always" })
	public void coreBeforeSuite() throws HarnessException, IOException, InterruptedException, SAXException {

		logger.info("BeforeSuite: start");

		try {

			ConfigProperties.setAppType(ConfigProperties.AppType.STARTATT);

			// Dynamic wait for App to be ready
			int maxRetry = 5;
			int retry = 0;
			boolean appIsReady = false;
			while (retry < maxRetry && !appIsReady) {
				try {
					logger.info("Retry #" + retry);
					retry++;
					webDriver.navigate().to(ConfigProperties.getBaseURL());
					appIsReady = true;

				} catch (WebDriverException e) {
					if (retry == maxRetry) {
						logger.error("Unable to open ajax app. Is a valid certificate installed?", e);
						throw e;
					} else {
						logger.info("App is still not ready...", e);
						SleepUtil.sleep(6000);
						continue;
					}
				}
			}
			logger.info("App is ready!");

		} catch (WebDriverException e) {
			logger.error("Unable to open ajax app. Is a valid certificate installed?", e);

		} catch (Exception e) {
			logger.warn(e);
		}

		logger.info("BeforeSuite: finish");
	}

	@BeforeClass(groups = { "always" })
	public void coreBeforeClass() throws HarnessException {
		logger.info("BeforeClass: start");
		logger.info("BeforeClass: finish");
	}

	@BeforeMethod(groups = { "always" })
	public void coreBeforeMethod(Method method, ITestContext testContext) throws HarnessException {

		logger.info("BeforeMethod: start");

		// Get the test name & description
		for (ITestNGMethod testngMethod : testContext.getAllTestMethods()) {
			String methodClass = testngMethod.getRealClass().getSimpleName();
			if (methodClass.equals(method.getDeclaringClass().getSimpleName())
					&& testngMethod.getMethodName().equals(method.getName())) {
				synchronized (StartAttCore.class) {
					logger.info("---------BeforeMethod-----------------------");
					logger.info("Test       : " + methodClass + "." + testngMethod.getMethodName());
					logger.info("Description: " + testngMethod.getDescription());
					logger.info("----------------------------------------");
				}
				break;
			}
		}

		// If a startingPage is defined, then make sure we are on that page
		if (startingPage != null) {
			logger.info("BeforeMethod: startingPage is defined -> " + startingPage.myPageName());

			// If the starting page is not active, navigate to it
			if (!startingPage.zIsActive()) {
				startingPage.zNavigateTo();
			}

			// Confirm that the page is active
			if (!startingPage.zIsActive()) {
				throw new HarnessException("Unable to navigate to " + startingPage.myPageName());
			}

			logger.info("BeforeMethod: startingPage navigation done -> " + startingPage.myPageName());
		}

		logger.info("BeforeMethod: finish");
	}

	@AfterSuite(groups = { "always" })
	public void coreAfterSuite() throws HarnessException, IOException {
		logger.info("AfterSuite: start");
		if (!ConfigProperties.getStringProperty("server.host").contains("start.att")) {
			webDriver.quit();
		}
		logger.info("AfterSuite: finished");
	}

	@AfterClass(groups = { "always" })
	public void coreAfterClass() throws HarnessException {
		logger.info("AfterClass: start");

		logger.info("AfterClass: finish");
	}

	@AfterMethod(groups = { "always" })
	public void coreAfterMethod(Method method, ITestResult testResult) throws HarnessException, IOException {
		logger.info("AfterMethod: start");

		if (!app.zPageMain.zIsActive()) {
			logger.error("Main page were not active. Reload app.", new Exception());
			app.zPageMain.sOpen(ConfigProperties.getBaseURL());
		}

		// Get test PASSED/FAILED status
		if (testResult.getStatus() == ITestResult.FAILURE){
		}

		logger.info("AfterMethod: finish");
	}
}