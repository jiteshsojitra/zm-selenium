package com.synacor.qa.selenium.framework.util;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.synacor.qa.selenium.framework.core.ExecuteHarnessMain;

public class ConfigProperties {
	private static final Logger logger = LogManager.getLogger(ConfigProperties.class);

	public static final String PropSynacorVersion = "1.0";
	private static InetAddress localMachine;
	private static ConfigProperties instance = null;
	private File BaseDirectory = null;
	private File PropertiesConfigurationFilename = null;
	private PropertiesConfiguration configProp;

	public static void setStringProperty(String key,String value) {
		ConfigProperties.getInstance().getConfigProp().setProperty(key, value);
	}

	public static String getStringProperty(String key, String defaultValue) {
		return (ConfigProperties.getInstance().getConfigProp().getString(key, defaultValue));
	}

	public static String getStringProperty(String key) {
		return (getStringProperty(key, null));
	}

	public static int getIntProperty(String key) {
		return (getIntProperty(key, 0));
	}

	public static int getIntProperty(String key, int defaultValue) {
		String value = ConfigProperties.getInstance().getConfigProp().getString(key, null);
		if ( value == null )
			return (defaultValue);
		return (Integer.parseInt(value));
	}

	private static int counter = 0;
	public static String getUniqueString() {
		return ("" + System.currentTimeMillis() + (++counter));
	}

	public static ResourceBundle getResourceBundleProperty(String key) {
		return ((ResourceBundle) ConfigProperties.getInstance().getConfigProp().getProperty(key));
	}

	public static PropertiesConfiguration getConfigProperties() {
		return ConfigProperties.getInstance().getConfigProp();
	}

	public static PropertiesConfiguration setConfigProperties(String filename) {
		logger.info("setConfigProperties using: "+ filename);
		ConfigProperties.getInstance().PropertiesConfigurationFilename = new File(filename);
		ConfigProperties.getInstance().init();
		return (ConfigProperties.getInstance().getConfigProp());
	}

	public static String getBaseDirectory() {
		if (ConfigProperties.getInstance().BaseDirectory == null)
			return (".");
		return (ConfigProperties.getInstance().BaseDirectory.getAbsolutePath());
	}

	public static File setBaseDirectory(String directory) {
		logger.info("setWorkingDirectory using: "+ directory);
		ConfigProperties.getInstance().BaseDirectory = new File(directory);
		return (ConfigProperties.getInstance().BaseDirectory);
	}

	private PropertiesConfiguration getConfigProp() {
		return configProp;
	}

	private static ConfigProperties getInstance() {
		if ( instance == null ) {
			synchronized(ConfigProperties.class) {
				if ( instance == null ) {
					instance = new ConfigProperties();
					instance.init();
				}
			}
		}
		return instance;
	}

	private ConfigProperties() {
		logger.debug("new ConfigProperties");
	}

	private void init() {

		// Load the config.properties values
		if ( PropertiesConfigurationFilename == null ) {
			logger.info("config.properties is default");
			configProp = createDefaultProperties();
		} else {
			try {
				logger.info("config.properties is "+ PropertiesConfigurationFilename.getAbsolutePath());
				configProp = new PropertiesConfiguration();
				configProp.load(PropertiesConfigurationFilename);
			} catch (ConfigurationException e) {
				logger.error("Unable to open config file: " + PropertiesConfigurationFilename.getAbsolutePath(), e);
				logger.info("config.properties is default");
				configProp = createDefaultProperties();
			}
		}
	}

	private PropertiesConfiguration createDefaultProperties() {
		PropertiesConfiguration defaultProp = new PropertiesConfiguration();

		defaultProp.setProperty("browser", "FF54");
		defaultProp.setProperty("runMode", "DEBUG");
		defaultProp.setProperty("product", "startatt");
		defaultProp.setProperty("locale", "en_US");
		defaultProp.setProperty("intl", "us");
		defaultProp.setProperty("serverport", "4444");
		defaultProp.setProperty("mode", "https");
		defaultProp.setProperty("server", "start.att.net");
		defaultProp.setProperty("testOutputDirectory", "test-output");
		defaultProp.setProperty("very_small_wait", "1000");
		defaultProp.setProperty("small_wait", "1000");
		defaultProp.setProperty("medium_wait", "2000");
		defaultProp.setProperty("long_wait", "4000");
		defaultProp.setProperty("long_medium_wait", "6000");
		defaultProp.setProperty("very_long_wait", "10000");

		return defaultProp;
	}

	public enum AppType {
		STARTATT
	}

	private static AppType appType = AppType.STARTATT;
	public static void setAppType(AppType type) {
		appType = type;
	}
	public static AppType getAppType() {
		return (appType);
	}

	public static String getLocalHost() {
		try {
			localMachine = InetAddress.getLocalHost();
			return localMachine.getHostName();
		} catch (Exception e) {
			logger.info(e.fillInStackTrace());
			return "127.0.0.1";
		}
	}

	private static final String CalculatedBrowser = "CalculatedBrowser";

	public static String getCalculatedBrowser() {
		String browser = getStringProperty(CalculatedBrowser);

		if ( browser != null ) {
			return (browser);
		}

		browser = ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".browser",
				ConfigProperties.getStringProperty("browser"));

		if (browser.charAt(0) == '*') {
			browser = browser.substring(1);
			if ((browser.indexOf(" ")) > 0) {
				String str = browser.split(" ")[0];
				int i;
				if ((i = browser.lastIndexOf("\\")) > 0) {
					str += "_" + browser.substring(i+1);
				}
				browser = str;
			}
		}

		ConfigProperties.setStringProperty(CalculatedBrowser, browser);
		return (browser);
	}
	
	public static String getLogoutURL() {
		return null;
	}

	public static String getBaseURL() {
		return ConfigProperties.getStringProperty("server.scheme") + "://" + ConfigProperties.getStringProperty("server.host");
	}

	public static String startAttGetVersionString() throws HarnessException {
		
		logger.info("Get project version...");
		
		String buildType = "startatt";
		Date date = new Date();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("hh.mm");

		ExecuteHarnessMain.startAttVersion = buildType + "-" + dateTimeFormat.format(date);
		return ExecuteHarnessMain.startAttVersion;
	}

	public static void main(String[] args) {
		System.setProperty("log4j.configuration", "file:///C:/log4j.properties");
		System.out.println(System.getProperty("log4j.configuration"));
		System.out.println(System.getProperty("user.dir"));
		String br = (String) ConfigProperties.getInstance().getConfigProp().getProperty("browser");
		System.out.println(br);
		logger.debug(br);
		ResourceBundle zmMsg = (ResourceBundle) ConfigProperties.getInstance().getConfigProp().getProperty("zmMsg");
		System.out.println(zmMsg.getLocale());
		logger.debug(zmMsg.getLocale());
	}
}