package com.synacor.qa.selenium.projects.startatt.pages;

import com.synacor.qa.selenium.framework.ui.AbsApplication;
import com.synacor.qa.selenium.framework.ui.AbsPage;
import com.synacor.qa.selenium.framework.ui.AbsTab;
import com.synacor.qa.selenium.framework.ui.Action;
import com.synacor.qa.selenium.framework.ui.Button;
import com.synacor.qa.selenium.framework.util.HarnessException;
import com.synacor.qa.selenium.framework.util.SleepUtil;

public class PageMain extends AbsTab {

	public static class Locators {
		public static final String sWeatherPane = "css=div[class='weather']";
	}

	public PageMain(AbsApplication application) {
		super(application);
		logger.info("new " + PageMain.class.getCanonicalName());
	}

	public boolean sWebLoadingCompleted() throws HarnessException {
		for (int i = 0; i <= 15; i++) {
			boolean present = sIsElementPresent(Locators.sWeatherPane);
			if (present == true) {
				SleepUtil.sleepSmall();
				return true;
			} else {
				SleepUtil.sleepMedium();
			}
		}
		return false;
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		boolean present = sIsElementPresent(Locators.sWeatherPane);
		if (!present) {
			logger.info("Weather pane present = " + present);
			return (false);
		}

		boolean loaded = sWebLoadingCompleted();
		if (!loaded) {
			logger.info("sWebLoadingCompleted() = " + loaded);
			return (false);
		}

		logger.info("isActive() = " + true);
		return (true);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {
		if (zIsActive()) {
			logger.info(myPageName() + " is already loaded");
			return;
		}

		if (!((StartAttPages) MyApplication).zPageMain.zIsActive()) {
			((StartAttPages) MyApplication).zPageMain.zNavigateTo();
		}
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		return null;
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		return null;
	}
}