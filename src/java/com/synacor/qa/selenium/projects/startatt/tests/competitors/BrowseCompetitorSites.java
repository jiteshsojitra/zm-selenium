package com.synacor.qa.selenium.projects.startatt.tests.competitors;

import org.testng.annotations.Test;
import com.synacor.qa.selenium.framework.util.ConfigProperties;
import com.synacor.qa.selenium.framework.util.HarnessException;
import com.synacor.qa.selenium.projects.startatt.core.StartAttCore;
import com.synacor.qa.selenium.projects.startatt.tests.competitors.BrowseCompetitorSites;

public class BrowseCompetitorSites extends StartAttCore {
	
	String[] competitorURLs;

	public BrowseCompetitorSites() {
		logger.info("New "+ BrowseCompetitorSites.class.getCanonicalName());
		super.startingPage = app.zPageMain;
	}

	@Test (description = "Browse all competitor sites",
			groups = { "sanity", "L0" })

	public void BrowseCompetitorSites_01() throws HarnessException {

		CompetitorSites();
		for (int i = 0; i < competitorURLs.length; i++) {
			app.zPageMain.sOpen(competitorURLs[i]);
		}
		app.zPageMain.sOpen(ConfigProperties.getStringProperty("server.scheme") + "://"
				+ ConfigProperties.getStringProperty("server.host"));
	}
	
	@Test (description = "Browse all competitor sites",
			groups = { "sanity", "L0" })

	public void BrowseCompetitorSites_02() throws HarnessException {

		CompetitorSites();
		for (int i = 0; i < competitorURLs.length; i++) {
			app.zPageMain.sOpen(competitorURLs[i]);
		}
		app.zPageMain.sClick("css=a[data-track-args-label='Mail']");
		app.zPageMain.sOpen(ConfigProperties.getStringProperty("server.scheme") + "://"
				+ ConfigProperties.getStringProperty("server.host"));
	}
	
	public void CompetitorSites() throws HarnessException {
		competitorURLs = new String[] { 
		};
	}
}