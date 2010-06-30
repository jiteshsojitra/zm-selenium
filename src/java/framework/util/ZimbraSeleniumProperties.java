//helper class for retrieving properties
package framework.util;

import java.io.File;
import java.lang.reflect.Field;

import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ZimbraSeleniumProperties {
	private static ZimbraSeleniumProperties instance = new ZimbraSeleniumProperties();
	private final String configPropName = "config.properties";
	private PropertiesConfiguration configProp;
	private File dir;
	private String curDir = ".";
	
	public static ZimbraSeleniumProperties getInstance() {
		return instance;
	}

	private ZimbraSeleniumProperties() {
		init();
	}

	public PropertiesConfiguration getConfigProperties() {
		return configProp;
	}

	private void init() {
		try {
			StackTraceElement[] stearr = new Throwable().getStackTrace();
			//assigning default value 
			String clname = "projects.html.bin.ExecuteTests";
			if (stearr != null) {
				for(StackTraceElement ste : stearr){
					clname = ste.getClassName();
					if(clname.contains("ExecuteTests"))
						break;
				}				
				Field field = Class.forName(clname).getDeclaredField("WorkingDirectory");
				curDir = (String) field.get("WorkingDirectory");
			}

			dir = new File(curDir);

			File file = new File(dir.getCanonicalPath() + File.separator
					+ "conf" + File.separator + configPropName);

			if (!file.exists()) {
				File[] files = FileFinder.listFilesAsArray(dir, configPropName,
						true);
				if (files != null && files.length > 0) {
					file = files[0];
				}
				if (!file.exists() || !file.getName().contains(configPropName)) {
					ZimbraSeleniumLogger.mLog.error(configPropName
							+ " does not exist!");
					configProp = createDefaultProperties();
				}
			}

			if (null == configProp)
				configProp = new PropertiesConfiguration(file);

		} catch (Exception ex) {
			ZimbraSeleniumLogger.mLog.error("Exception : " + ex);
		}

		String locale = configProp.getString("locale");

		configProp.setProperty("zmMsg", ResourceBundle.getBundle(
				"framework.locale.ZmMsg", new Locale(locale)));

		configProp.setProperty("zhMsg", ResourceBundle.getBundle(
				"framework.locale.ZhMsg", new Locale(locale)));

		configProp.setProperty("ajxMsg", ResourceBundle.getBundle(
				"framework.locale.AjxMsg", new Locale(locale)));

		configProp.setProperty("i18Msg", ResourceBundle.getBundle(
				"framework.locale.I18nMsg", new Locale(locale)));

		configProp.setProperty("zsMsg", ResourceBundle.getBundle(
				"framework.locale.ZsMsg", new Locale(locale)));

	}

	private PropertiesConfiguration createDefaultProperties() {
		PropertiesConfiguration defaultProp = new PropertiesConfiguration();

		defaultProp.setProperty("browser", "FF3");

		defaultProp.setProperty("runMode", "DEBUG");

		defaultProp.setProperty("product", "zcs");

		defaultProp.setProperty("locale", "en_US");

		defaultProp.setProperty("intl", "us");

		defaultProp.setProperty("testdomain", "testdomain.com");

		defaultProp.setProperty("multiWindow", "true");

		defaultProp.setProperty("objectDataFile",
				"projects/zcs/data/objectdata.xml");

		defaultProp.setProperty("testDataFile",
				"projects/zcs/data/testdata.xml");

		defaultProp.setProperty("serverMachineName", "localhost");

		defaultProp.setProperty("serverport", "4444");

		defaultProp.setProperty("mode", "http");

		defaultProp.setProperty("server", "qa60.lab.zimbra.com");

		defaultProp.setProperty("ProvServer", "qa60.lab.zimbra.com");

		defaultProp.setProperty("ZimbraLogRoot", "test-output");

		defaultProp.setProperty("adminName", "admin");

		defaultProp.setProperty("adminPwd", "test123");

		defaultProp.setProperty("small_wait", "1000");

		defaultProp.setProperty("medium_wait", "2000");

		defaultProp.setProperty("long_wait", "4000");

		defaultProp.setProperty("very_long_wait", "10000");

		defaultProp.setProperty("zmMsg", ResourceBundle.getBundle(
				"framework.locale.ZhMsg", new Locale("en_US")));

		defaultProp.setProperty("zhMsg", ResourceBundle.getBundle(
				"framework.locale.ZhMsg", new Locale("en_US")));

		defaultProp.setProperty("ajxMsg", ResourceBundle.getBundle(
				"framework.locale.AjxMsg", new Locale("en_US")));

		defaultProp.setProperty("i18Msg", ResourceBundle.getBundle(
				"framework.locale.I18nMsg", new Locale("en_US")));

		defaultProp.setProperty("zsMsg", ResourceBundle.getBundle(
				"framework.locale.ZsMsg", new Locale("en_US")));

		return defaultProp;
	}

	private static class CurClassGetter extends SecurityManager {
		private Class<?> getCurrentClass() {
			return getClassContext()[1];
		}
	}

	// for unit test need to change access to public
	private static void main(String[] args) {
		ZimbraSeleniumLogger.setmLog(new CurClassGetter().getCurrentClass());

		String br = (String) ZimbraSeleniumProperties.getInstance()
				.getConfigProperties().getProperty("browser");
		System.out.println(br);
		ZimbraSeleniumLogger.mLog.debug(br);

		ResourceBundle zmMsg = (ResourceBundle) ZimbraSeleniumProperties
				.getInstance().getConfigProperties().getProperty("zmMsg");
		System.out.println(zmMsg.getLocale());
		ZimbraSeleniumLogger.mLog.debug(zmMsg.getLocale());
	}
}