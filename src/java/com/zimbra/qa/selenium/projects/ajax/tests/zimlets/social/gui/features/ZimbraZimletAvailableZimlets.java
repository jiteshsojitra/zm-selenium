package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.social.gui.features;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;


public class ZimbraZimletAvailableZimlets extends AjaxCommonTest {

	
	@SuppressWarnings("serial")
	public ZimbraZimletAvailableZimlets() {
		logger.info("New "+ ZimbraZimletAvailableZimlets.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageSocial;

		// Make sure we are using an account with message view
		super.startingAccountPreferences = new HashMap<String, String>() {{
				    				    
					// Only mail is enabled
				    put("zimbraFeatureMailEnabled", "FALSE");
				    put("zimbraFeatureContactsEnabled", "FALSE");
				    put("zimbraFeatureCalendarEnabled", "FALSE");
				    put("zimbraFeatureTasksEnabled", "FALSE");
				    put("zimbraFeatureBriefcasesEnabled", "FALSE");
				    put("zimbraFeatureOptionsEnabled", "FALSE");
				    
				    put("zimbraZimletAvailableZimlets", "+com_zimbra_social");

				}};


	}
	
	@Test(	description = "Load the client with just Social enabled",
			groups = { "functional" })
	public void ZimbraZimletAvailableZimlets_01() throws HarnessException {
		
		ZAssert.assertTrue(app.zPageSocial.zIsActive(), "Verify the social page is active");

		
	}


}
