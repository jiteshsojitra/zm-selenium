package com.synacor.qa.selenium.framework.ui;

import org.apache.log4j.*;
import com.synacor.qa.selenium.framework.util.*;

public abstract class AbsTab extends AbsPage {

	protected static Logger logger = LogManager.getLogger(AbsTab.class);

	public AbsTab(AbsApplication application) {
		super(application);
	}

	public abstract void zNavigateTo() throws HarnessException;

	public abstract boolean zIsActive() throws HarnessException;

	public abstract AbsPage zListItem(Action action, String item) throws HarnessException;

	public abstract AbsPage zListItem(Action action, Button option, String item) throws HarnessException;

	public abstract AbsPage zListItem(Action action, Button option, Button subOption, String item)
			throws HarnessException;

	public abstract AbsPage zToolbarPressButton(Button button) throws HarnessException;

	public abstract AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException;

	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		AbsPage page = null;
		zKeyboardTypeString(shortcut.getKeys());
		return (page);
	}

	public AbsPage zKeyboardKeyEvent(int keyEvent) throws HarnessException {
		this.zWaitForBusyOverlay();
		AbsPage page = null;
		this.zKeyboard.zTypeKeyEvent(keyEvent);
		this.zWaitForBusyOverlay();
		return (page);
	}

	public AbsPage zKeyboardTypeString(String keys) throws HarnessException {
		AbsPage page = null;
		this.zKeyboard.zTypeCharacters(keys);
		return (page);
	}
}