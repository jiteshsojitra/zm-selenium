package com.synacor.qa.selenium.projects.startatt.pages;

import com.synacor.qa.selenium.framework.ui.AbsApplication;
import com.synacor.qa.selenium.framework.util.HarnessException;

public class StartAttPages extends AbsApplication {

	public PageMain zPageMain = null;
	
	public StartAttPages() {
		super();

		logger.info("new " + StartAttPages.class.getCanonicalName());

		// Main/login page
		zPageMain = new PageMain(this);
		pages.put(zPageMain.myPageName(), zPageMain);
	}

	@Override
	public String myApplicationName() {
		return null;
	}

	@Override
	public boolean zIsLoaded() throws HarnessException {
		return false;
	}
}