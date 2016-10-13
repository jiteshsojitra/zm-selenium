/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
/**
 *
 */
package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogTag;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class PageMail extends AbsTab {

	WebElement we = null;

	public static class Locators {

		public static final String zMailZimletsPane = "ztih__main_Mail__ZIMLET_textCell";
		public static final String zMailTagsPane = "ztih__main_Mail__TAG_textCell";
		public static final String zInboxFolder = "zti__main_Mail__2_textCell";

		public static final String IsConViewActiveCSS = "css=div[id='zv__CLV-main']";
		public static final String IsMsgViewActiveCSS = "css=div[id='zv__TV-main']";

		public static final String zPrintIconBtnID = "zb__CLV-main__PRINT_left_icon";
		public static final String zTagMenuDropdownBtnID = "zb__CLV-main__TAG_MENU_dropdown";
		public static final String zDetachIconBtnID = "zb__TV-main__DETACH_left_icon";
		public static final String zViewMenuDropdownBtnID = "zb__CLV-main__VIEW_MENU_dropdown";
		public static final String zViewMenuDropdownBtnConvID = "zb__CV-1__VIEW_MENU_dropdown";
		public static final String zByConvViewConvID = "css=div[id^='POPUP_DWT'][style*='display: block;'] div[id^='CLV']";
		public static final String zByMsgViewConvID = "css=div[id^='POPUP_DWT'][style*='display: block;'] div[id^='TV']";
		public static final String zByCONVORDERwConvID = "css=div[id^='POPUP_DWT'][style*='display: block;'] div[id^='CONV_ORDER']";
		public static final String zViewEntireMessageLink = "css=div[id$='msgTruncation'] td [id$='_link']";
		public static final String zCloseIconBtn_messageWindow = "css=td[id=zb__MSG__CLOSE_left_icon]";
		public static final String cssTVRowsLocator = "css=div#zl__TV-main__rows";

		// Accept, Decline & Tentative button, menus and dropdown locators
		public static final String AcceptDropdown = "css=td[id$='__Inv__REPLY_ACCEPT_dropdown']";
		public static final String AcceptNotifyOrganizerMenu = "id=REPLY_ACCEPT_NOTIFY_title";
		public static final String AcceptEditReplyMenu = "id=INVITE_REPLY_ACCEPT_title";
		public static final String AcceptDontNotifyOrganizerMenu = "id=REPLY_ACCEPT_IGNORE_title";

		public static final String TentativeDropdown = "css=td[id$='__Inv__REPLY_TENTATIVE_dropdown']";
		public static final String TentativeNotifyOrganizerMenu = "id=REPLY_TENTATIVE_NOTIFY_title";
		public static final String TentativeEditReplyMenu = "id=INVITE_REPLY_TENTATIVE_title";
		public static final String TentativeDontNotifyOrganizerMenu = "id=REPLY_TENTATIVE_IGNORE_title";

		public static final String DeclineDropdown = "css=td[id$='__Inv__REPLY_DECLINE_dropdown']";
		public static final String DeclineNotifyOrganizerMenu = "id=REPLY_DECLINE_NOTIFY_title";
		public static final String DeclineEditReplyMenu = "id=INVITE_REPLY_DECLINE_title";
		public static final String DeclineDontNotifyOrganizerMenu = "id=REPLY_DECLINE_IGNORE_title";

		public static final String ProposeNewTimeButtonMsgView = "id=zb__TV-main__Inv__PROPOSE_NEW_TIME_title";

		public static final String zMsgViewDisplayImgLink = "css=a#zv__TV__TV-main_MSG_displayImages_dispImgs";
		public static final String zMsgViewDomainLink = "css=a#zv__TV__TV-main_MSG_displayImages_domain";
		public static final String zMsgExternalImage = "css=body.MsgBody img";
		public static final String zMsgViewWarningIcon = "css=div#zv__TV__TV-main_MSG_displayImages.DisplayImages div.ImgWarning";
		public static final String zConViewDisplayImgLink = "css=a[id$='_displayImages_dispImgs']";
		public static final String zConViewDomainLink = "css=a[id$='_displayImages_domain']";
		public static final String zConViewWarningIcon = "css=div[id$='_displayImages']  div[class='ImgWarning']";
		public static final String zReplyToolbarButton = "css=div[id$='__REPLY']";
		public static final String zReplyAllToolbarButton = "css=div[id$='__REPLY_ALL']";
		public static final String zForwardToolbarButton = "css=div[id$='__FORWARD']";
		public static final String zSaveWarningDialog = "css=div[id='YesNoCancel'][style*='display: block;']";
		public static final String CreateNewCalendar = "css=div[id^='POPUP_DWT'] td[id='NEWCAL_title']";

		public static final String zToAddressBubble = "css=tr[id='zv__COMPOSE-1_to_row'] td[id='zv__COMPOSE-1_to_cell'] div div span[class='addrBubble']>span";
		public static final String zCcAddressBubble = "css=tr[id='zv__COMPOSE-1_cc_row'] td[id='zv__COMPOSE-1_cc_cell'] div div span[class='addrBubble']>span";
		public static final String zBccAddressBubble = "css=tr[id='zv__COMPOSE-1_bcc_row'] td[id='zv__COMPOSE-1_bcc_cell'] div div span[class='addrBubble']>span";
		public static final String zDeleteAddressContextMenu = "css=div[id^='POPUP_DWT'] tbody div[id='DELETE'] table tbody tr[id='POPUP_DELETE']";
		public static final String zCopyAddressContextMenu = "css=div[id^='POPUP_DWT'] tbody div[id='COPY'] table tbody tr[id='POPUP_COPY']";
		public static final String zEditAddressContextMenu = "css=div[id^='POPUP_DWT'] tbody div[id='EDIT'] table tbody tr[id='POPUP_EDIT']";
		public static final String zExpandAddressContextMenu = "css=div[id^='POPUP_DWT'] tbody div[id='EXPAND'][class*='ZDisabled'] table tbody tr[id='POPUP_EXPAND']";
		public static final String zContactAddressContextMenu = "css=div[id^='POPUP_DWT'] tbody div[id='CONTACT'] table tbody tr[id='POPUP_CONTACT']";
		public static final String zMovetToToAddressContextMenu = "css=div[id^='POPUP_DWT'] tbody div[id='MOVE_TO_TO'][class*='ZDisabled'] table tbody tr[id='POPUP_MOVE_TO_TO']";
		public static final String zMoveToCcAddressContextMenu = "css=div[id^='POPUP_DWT'] tbody div[id='MOVE_TO_CC'] table tbody tr[id='POPUP_MOVE_TO_CC']";
		public static final String zMoveToBccAddressContextMenu = "css=div[id^='POPUP_DWT'] tbody div[id='MOVE_TO_BCC'] table tbody tr[id='POPUP_MOVE_TO_BCC']";

		// Msg header Address Context menu
		public static final String zCopyMsgHdrContextMenu = "css=div[id^='zcs'][class^='ActionMenu ']  tbody div[id='COPY'] table tbody tr[id='POPUP_COPY']";
		public static final String zFindEmailsMsgHdrContextMenu = "css=div[id^='zcs'][class^='ActionMenu ']  tbody div[id='SEARCH_MENU'] table tbody tr[id='POPUP_SEARCH_MENU']";
		public static final String zNewEmailsMsgHdrContextMenu = "css=div[id^='zcs'][class^='ActionMenu ']  tbody div[id='NEW_MESSAGE'] table tbody tr[id='POPUP_NEW_MESSAGE']";
		public static final String zAddToContactMsgHdrContextMenu = "css=div[id^='zcs'][class^='ActionMenu ']  tbody div[id='CONTACT'] table tbody tr[id='POPUP_CONTACT']";
		public static final String zGoToUrlMsgHdrContextMenu = "css=div[id^='zcs'][class^='ActionMenu ']  tbody div[id='GO_TO_URL'] table tbody tr[id='POPUP_GO_TO_URL']";
		public static final String zCreateFilterMsgHdrContextMenu = "css=div[id^='zcs'][class^='ActionMenu ']  tbody div[id^='ADD_TO_FILTER_RULE'] table tbody tr[id^='POPUP_ADD_TO_FILTER_RULE']";
		public static final String zFromHdrAddressBubble = "css=div[id='zv__TV-main__MSG'] table[id='zv__TV-main__MSG_headerElement'] tr[id^='OBJ_PREFIX_DWT'][id$='_from'] span[class^='addrBubble']>span";
		public static final String zAttachdropdown = "css=div[id='zb__COMPOSE-1___attachments_btn'] table tbody tr td[id='zb__COMPOSE-1___attachments_btn_dropdown']>div";
		public static final String zAddFilterMsgHdrContextMenu = "css=div[id^='POPUP'] td[id$='_title']:contains('New Filter')";

		public static final String zAttachMailMenu = "css=div[class='DwtMenu'] td div[id^='DWT'] td[id$='_title']:contains('Mail')";
		public static final String zAttachContactMenu = "css=div[class='DwtMenu'] td div[id^='DWT'] td[id$='_title']:contains('Contacts')";
		public static final String zAttachBriefcaseMenu = "css=div[class='DwtMenu'] td div[id^='DWT'] td[id$='_title']:contains('Briefcase')";
		public static final String zAttachContactFolder = "css=div[id='zov__AttachContactsTabView'] div[id^='zti__AttachContactsTabView'] td[id^='zti__AttachContactsTabView']:contains('Contacts')";
		public static final String zAttachInboxFolder = "css=div[id='zov__AttachMailTabView'] div[id^='zti__AttachMailTabView'] td[id^='zti__AttachMailTabView']:contains('Inbox')";
		public static final String zAttachBriefcaseFolder = "css=div[id='zov__ZmBriefcaseTabView'] div[id^='zti__ZmBriefcaseTabView'] td[id^='zti__ZmBriefcaseTabView']:contains('Briefcase')";
		public static final String zUntagBubble = "css=div[id='zv__TV-main__MSG'] tr[id='zv__TV__TV-main_MSG_tagRow'] span[class='addrBubble TagBubble'] span[class='ImgBubbleDelete']";

		// Full view pane locators
		public static final String zCloseButtonFullViewPane = "css=div[id^='ztb__MSG'] div[id$='CLOSE'] tr td[id$='CLOSE_title']";
		public static final String zDeleteButtonFullViewPane = "css=div[id^='ztb__MSG'] div[id$='DELETE'] tr td[id$='DELETE_title']";

		// Reading pane separator bar locators
		public static final String zBottomReadingPaneSeparatorBar = "css=div[class='AppSash-vert']";
		public static final String zRightReadingPaneSeparatorBar = "css=div[class='AppSash-horiz']";

		public static class CONTEXT_MENU {
			// TODO: Until https://bugzilla.zimbra.com/show_bug.cgi?id=56273 is
			// fixed, ContextMenuItem will be defined using the text content
			public static String stringToReplace = "<ITEM_NAME>";
		}
	}

	public PageMail(AbsApplication application) {
		super(application);

		logger.info("new " + PageMail.class.getCanonicalName());

	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if (!((AppAjaxClient) MyApplication).zPageMain.zIsActive()) {
			((AppAjaxClient) MyApplication).zPageMain.zNavigateTo();
		}

		String locator;
		boolean loaded, visible;

		// Check MLV first
		locator = "css=div#zb__TV-main__VIEW_MENU";

		loaded = this.sIsElementPresent(locator);
		visible = this.zIsVisiblePerPosition(locator, 4, 74);
		if (loaded && visible)
			return (true);

		// Check CLV next
		locator = "css=div#zb__CLV-main__VIEW_MENU";
		loaded = this.sIsElementPresent(locator);
		visible = this.zIsVisiblePerPosition(locator, 4, 74);
		if (loaded && visible)
			return (true);

		// We made it here, neither were active
		return (false);
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

		((AppAjaxClient) MyApplication).zPageMain.zCheckAppLoaded(Locators.zMailZimletsPane);
	}

	public boolean zVerifyMailExists(String subject) throws HarnessException {

		boolean found = false;

		for (int i = 1; i <= 2; i++) {

			zToolbarPressButton(Button.B_REFRESH);

			List<MailItem> items = zListGetMessages();

			for (MailItem item : items) {
				if (subject.equals(item.getSubject())) {
					found = true;
					break;
				} else {
					logger.info("Mail not displayed in current view");
					if (i == 2) {
						Stafpostqueue sp = new Stafpostqueue();
						sp.waitForPostqueue();
					}
				}
			}

			if (found == true) {
				logger.info("Mail displayed in current view");
				break;
			}
		}

		return found;

	}

	public boolean zVerifyAttachmentExistsInMail(String fileName) throws HarnessException {
		boolean isAttachmentExists = false;
		isAttachmentExists = sIsElementPresent("css=td a[id*='main_MSG_attLinks']:contains('" + fileName + "')");
		return isAttachmentExists;
	}

	@SuppressWarnings("static-access")
	public void zDisplayMailRightClick(String locator, Button button) throws HarnessException {
		try {
			webDriver().switchTo().defaultContent();
			webDriver().switchTo().frame(0);
			we = webDriver().findElement(By.cssSelector(locator));
			final Actions builder = new Actions(webDriver());
			builder.contextClick(we).build().perform();
			webDriver().switchTo().defaultContent();

			String btnLocator = null;
			if (button.equals(button.B_CREATE_NEW_CALENDAR)) {
				btnLocator = Locators.CreateNewCalendar;
			}
			we = webDriver().findElement(By.cssSelector(btnLocator.replace("css=", "")));
			we.click();
			SleepUtil.sleepSmall();

		} finally {
			webDriver().switchTo().defaultContent();
		}
	}
	
	public void zDisplayMailClick(String locator) throws HarnessException {
		try {
			webDriver().switchTo().defaultContent();
			webDriver().switchTo().frame(0);
			we = getElement(locator);
			we.click();
			SleepUtil.sleepSmall();

		} finally {
			webDriver().switchTo().defaultContent();
		}
	}

	public void zDisplayMailHoverOver(String locator) throws HarnessException {
		try {

			webDriver().switchTo().defaultContent();
			webDriver().switchTo().frame(0);

			if (locator.contains("nth-of-type")) {
				we = webDriver().findElement(By.cssSelector(locator));
				final Actions builder = new Actions(webDriver());
				builder.moveToElement(we).build().perform();
				
			} else if (locator.contains("url")) {
				
				this.sClickAt(locator, "");
				SleepUtil.sleepSmall();
				this.sMouseOver(locator);
				
				String mainwindow = webDriver().getWindowHandle();
				try {					
					Set <String> windows = webDriver().getWindowHandles();
				    
					// Close opened URL window
				    for (String handle: windows) {
				    	webDriver().switchTo().window(handle);
				        if (!handle.equals(mainwindow)) {
				        	webDriver().close();
				        }
				    }
				} finally {
					webDriver().switchTo().window(mainwindow);
				}
			    
			} else {
				this.sMouseOver(locator);
			}

		} finally {
			webDriver().switchTo().defaultContent();
		}
	}

	public boolean zVerifyInlineImageAttachmentExistsInComposeWindow() throws HarnessException {

		try {
			webDriver().switchTo().defaultContent();
			webDriver().switchTo().frame("ZmHtmlEditor1_body_ifr");
			we = webDriver().findElement(By.cssSelector("body#tinymce img"));

			if (we.getAttribute("src").contains("/service/home/~/?auth=co")
					&& we.getAttribute("data-mce-src").startsWith("cid:")
					&& we.getAttribute("data-mce-src").endsWith("@zimbra")
					&& we.getAttribute("dfsrc").startsWith("cid:") && we.getAttribute("dfsrc").endsWith("@zimbra")) {
				return true;
			} else {
				return false;
			}

		} finally {
			webDriver().switchTo().defaultContent();
		}
	}
	
	public boolean zVerifyInlineImageAttachmentExistsInComposeWindow(String windowTitle, int iFrame) throws HarnessException {

		try {
			webDriver().switchTo().defaultContent();
			sSelectWindow(windowTitle);
			webDriver().switchTo().frame(iFrame);
			WebElement we = webDriver().findElement(By.cssSelector("html div img"));
			
			if (we.getAttribute("src").contains("/service/home/~/?auth=co")
					&& we.getAttribute("data-mce-src").startsWith("cid:")
					&& we.getAttribute("data-mce-src").endsWith("@zimbra")
					&& we.getAttribute("dfsrc").startsWith("cid:") && we.getAttribute("dfsrc").endsWith("@zimbra")) {
				return true;
			} else {
				return false;
			}

		} finally {
			webDriver().switchTo().defaultContent();
		}
	}

	public boolean zVerifyInlineImageAttachmentExistsInMail() throws HarnessException {

		try {
			webDriver().switchTo().defaultContent();
			webDriver().switchTo().frame(0);
			we = webDriver().findElement(By.cssSelector("html div img"));
			if (we.getAttribute("pnsrc").startsWith("cid")
					&& we.getAttribute("src").contains("/service/home/~/?auth=co")) {
				return true;
			} else {
				return false;
			}

		} finally {
			webDriver().switchTo().defaultContent();
		}
	}

	public boolean zVerifyDisplayMailElement(String locator) throws HarnessException {
		try {
			webDriver().switchTo().defaultContent();
			webDriver().switchTo().frame(0);
			we = webDriver().findElement(By.cssSelector(locator));
			if (we.isDisplayed()) {
				return true;
			} else {
				return false;
			}

		} finally {
			webDriver().switchTo().defaultContent();
		}
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_NEW) {

			locator = "css=div[id$='__NEW_MENU'] td[id$='__NEW_MENU_title']";
			page = new FormMailNew(this.MyApplication);
			
			for (int i=0; i<=2; i++) {
				this.sClick(locator);
				zWaitForBusyOverlay();
				SleepUtil.sleepMedium();
				
				if (!page.zIsActive()) {
					continue;
				} else {
					return page;
				}
			}

		} else if (button == Button.B_NEW_IN_NEW_WINDOW) {

			locator = "css=div[id$='__NEW_MENU'] td[id$='__NEW_MENU_title']";
			page = new SeparateWindowFormMailNew(this.MyApplication);
			this.zClickAt(locator, "0,0");

			return (page);

		} else if (button == Button.B_DETACH_COMPOSE) {

			locator = "css=div[id$='__DETACH_COMPOSE'] td[id$='COMPOSE_left_icon']";
			page = new SeparateWindowFormMailNew(this.MyApplication);
			this.sClickAt(locator, "0,0");
			SleepUtil.sleepVeryLong();

			return (page);

		} else if (button == Button.B_REFRESH || button == Button.B_LOADFEED) {

			return (((AppAjaxClient) this.MyApplication).zPageMain.zToolbarPressButton(Button.B_REFRESH));

		} else if (button == Button.B_DELETE) {

			String id;
			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				id = "zb__TV-main__DELETE_left_icon";
			} else {
				id = "zb__CLV-main__DELETE_left_icon";
			}

			// Check if the button is enabled
			locator = "css=td[id='" + id + "']>div[class*='ZDisabledImage']";
			if (sIsElementPresent(locator)) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled: ZDisabledImage");
			}

			locator = "css=td#" + id;

		} else if (button == Button.B_MOVE) {

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[contains(@id, '__MOVE_left_icon')]/div)@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + attrs);
			}

			locator = "css=td[id$='__MOVE_left_icon']";
			page = new DialogMove(MyApplication, this);

		} else if (button == Button.B_ADD_TO_CALENDAR) {

			locator = "css=a[id$='2_calendar']";
			page = new DialogAddToCalendar(MyApplication, this);

		} else if (button == Button.B_BRIEFCASE) {

			locator = "css=a[id$='2_briefcase']";
			page = new DialogAddToBriefcase(MyApplication, this);

		} else if (button == Button.B_LAUNCH_IN_SEPARATE_WINDOW) {

			boolean isCLV = this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0);

			String pulldownLocator, optionLocator;

			if (isCLV) {
				pulldownLocator = "css=td[id='zb__CLV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__CLV-main']";
			} else {
				pulldownLocator = "css=td[id='zb__TV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__TV-main']";
			}

			optionLocator += " div[id*='DETACH'] td[id$='_title']";
			page = new SeparateWindow(this.MyApplication);

			this.sClickAt(pulldownLocator, "0,0");
			zWaitForBusyOverlay();
			this.sClickAt(optionLocator, "0,0");
			zWaitForBusyOverlay();
			SleepUtil.sleepVeryLong();

			return (page);

		} else if (button == Button.B_PRINT) {

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='" + Locators.zPrintIconBtnID + "']/div)@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + attrs);
			}

			locator = "id='" + Locators.zPrintIconBtnID;
			page = null;
			throw new HarnessException("implement Print dialog");

		} else if (button == Button.B_REPLY) {

			page = new FormMailNew(this.MyApplication);
			locator = Locators.zReplyToolbarButton;

			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Reply icon not present " + button);
			}

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//div[contains(@id,'__REPLY')])@class");
			if (attrs.contains("ZDisabled")) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + attrs);
			}

		} else if (button == Button.B_REPLYALL) {

			page = new FormMailNew(this.MyApplication);
			locator = Locators.zReplyAllToolbarButton;

			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Reply All icon not present " + button);
			}

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//div[contains(@id,'__REPLY_ALL')])@class");
			if (attrs.contains("ZDisabled")) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + attrs);
			}

		} else if (button == Button.B_FORWARD) {

			page = new FormMailNew(this.MyApplication);
			locator = Locators.zForwardToolbarButton;

			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Forward icon not present " + button);
			}

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//div[contains(@id,'__FORWARD')])@class");
			if (attrs.contains("ZDisabled")) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + attrs);
			}

		} else if ((button == Button.B_RESPORTSPAM) || (button == Button.B_RESPORTNOTSPAM)) {

			page = null;
			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				locator = "css=div[id='zb__TV-main__SPAM'] td[id$='_title']";
			} else {
				locator = "css=div[id='zb__CLV-main__SPAM'] td[id$='_title']";
			}

			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Spam icon not present " + button);
			}

		} else if (button == Button.B_TAG) {

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='" + Locators.zTagMenuDropdownBtnID + "']/div)@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + attrs);
			}
			locator = "id='" + Locators.zTagMenuDropdownBtnID + "'";

		} else if (button == Button.B_RFC822_ATTACHMENT_LINK) {

			locator = "css=a[id^='zv__TV__TV-main_MSG']";

			page = new SeparateWindow(this.MyApplication);
			((SeparateWindow) page).zInitializeWindowNames();
			this.sClickAt(locator, "");
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_ARCHIVE) {

			page = null;

			if (this.sIsElementPresent("css=div[id$='zb__TV-main__ARCHIVE'] td[id$='_title']")) {
				locator = "css=div[id$='zb__TV-main__ARCHIVE'] td[id$='_title']";
			} else if (this.sIsElementPresent("css=div[id$='zb__CLV-main__ARCHIVE'] td[id$='_title']")) {
				locator = "css=div[id$='zb__CLV-main__ARCHIVE'] td[id$='_title']";
			} else {
				throw new HarnessException("Archive icon not present " + button);
			}

		} else if (button == Button.B_READMORE) {

			if (!this.sIsElementPresent("css=div[id$='__KEEP_READING']")) {
				throw new HarnessException("Keep Reading button not present " + button);
			}

			if (this.sIsElementPresent("css=div[id$='__KEEP_READING'].ZDisabled")) {
				throw new HarnessException("Keep Reading button is disabled " + button);
			}

			locator = "css=div[id$='__KEEP_READING'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_NEWWINDOW) {

			return (this.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW));

		} else if (button == Button.B_LISTVIEW) {

			// Check if the button is enabled
			String attrs = sGetAttribute("xpath=(//td[@id='" + Locators.zViewMenuDropdownBtnID + "']/div)@class");
			if (attrs.contains("ZDisabledImage")) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + attrs);
			}
			locator = "id='" + Locators.zViewMenuDropdownBtnID + "'";

		} else if (button == Button.B_CLOSE_CONVERSATION) {

			locator = "css=div[id$='__CLOSE'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_SELECT_ALL) {

			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				locator = "css=div#zv__TV-main td[id$='__se'] div[id$='__se']";
			} else {
				locator = "css=div#zv__CLV-main td[id$='__se'] div[id$='__se']";
			}
			page = null;

		} else if (button == Button.B_EML_ATTACHEMENT) {

			locator = "css=td a[id^='zv__TV__TV-main_MSG_attLinks']:contains('8989.eml')";
			page = new SeparateWindowDisplayMail(this.MyApplication);

		} else if (button == Button.B_SHIFT_SELECT_ALL) {

			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				locator = "css=div#zv__TV-main td[id$='__se'] div[id$='__se']";
			} else {
				locator = "css=div#zv__CLV-main td[id$='__se'] div[id$='__se']";
			}
			page = null;

			final org.openqa.selenium.interactions.Actions builder = new org.openqa.selenium.interactions.Actions(webDriver());
			org.openqa.selenium.interactions.Action action = builder.keyDown(Keys.SHIFT).click(getElement(locator)).keyUp(Keys.SHIFT).build();
			action.perform();

			this.zWaitForBusyOverlay();
			return (page);

		} else if ((button == Button.B_MAIL_VIEW_READING_PANE_BOTTOM)
				|| (button == Button.B_MAIL_VIEW_READING_PANE_RIGHT)
				|| (button == Button.B_MAIL_VIEW_READING_PANE_OFF)) {

			locator = "css=td[id$=VIEW_MENU_dropdown]>div[class='ImgSelectPullDownArrow']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();

			// Click on Reading pane
			locator = "css=td[id$=READING_PANE_1_dropdown]>div[class='ImgCascade']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();

			// Select Reading Pane At the Bottom/On the Right/Off
			if (button == Button.B_MAIL_VIEW_READING_PANE_BOTTOM) {
				locator = "css=div#bottom td[id$='_title']";
			} else if (button == Button.B_MAIL_VIEW_READING_PANE_RIGHT) {
				locator = "css=div#right td[id$='_title']";
			} else if (button == Button.B_MAIL_VIEW_READING_PANE_OFF) {
				locator = "css=div#off td[id$='_title']";
			}
			this.zClick(locator);
			this.zWaitForBusyOverlay();

			return (null);

		} else if ((button == Button.B_MAIL_VIEW_BY_CONVERSATION) || (button == Button.B_MAIL_VIEW_BY_MESSAGE)) {

			locator = "css=td[id$=VIEW_MENU_dropdown]>div[class='ImgSelectPullDownArrow']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();

			// Select view (message/conversation)
			if (button == Button.B_MAIL_VIEW_BY_CONVERSATION) {
				locator = "css=div#CLV td[id$='_title']";
			} else if (button == Button.B_MAIL_VIEW_BY_MESSAGE) {
				locator = "css=div#TV td[id$='_title']";
			}
			this.zClick(locator);
			this.zWaitForBusyOverlay();

			return (null);

		} else if ((button == Button.B_MAIL_LIST_GROUPBY_FROM) || (button == Button.B_MAIL_LIST_GROUPBY_DATE)
				|| (button == Button.B_MAIL_LIST_GROUPBY_SIZE)) {

			locator = "css=td#zlha__TV-main__su";
			this.zRightClickAt(locator, "");
			this.zWaitForBusyOverlay();

			// Hover over Group By
			locator = "css=td[id$='_title']:contains('Group By')";
			this.sMouseOver(locator);
			this.zWaitForBusyOverlay();

			// Select From/Date/Size
			if (button == Button.B_MAIL_LIST_GROUPBY_FROM) {
				locator = "css=div#GROUPBY_FROM td[id$='_title']";
			} else if (button == Button.B_MAIL_LIST_GROUPBY_DATE) {
				locator = "css=div#GROUPBY_DATE td[id$='_title']";
			} else if (button == Button.B_MAIL_LIST_GROUPBY_SIZE) {
				locator = "css=div#GROUPBY_SIZE td[id$='_title']";
			}
			this.sClickAt(locator, "");
			this.zWaitForBusyOverlay();

			return (null);

		} else if (button == Button.B_MAIL_LIST_SORTBY_FLAGGED) {

			locator = "css=td[id='zlh__TV-main__fg'] div[class='ImgFlagRed']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();
			return (null);

		} else if (button == Button.B_MAIL_LIST_SORTBY_FROM) {

			locator = "css=td[id='zlh__TV-main__fr'] td[id='zlhl__TV-main__fr']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();
			return (null);

		} else if (button == Button.B_MAIL_LIST_SORTBY_ATTACHMENT) {

			locator = "css=td[id='zlh__TV-main__at'] div[class='ImgAttachment']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();
			return (null);

		} else if (button == Button.B_MAIL_LIST_SORTBY_SUBJECT) {

			locator = "css=td[id='zlh__TV-main__su'] td[id='zlhl__TV-main__su']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();
			return (null);

		} else if (button == Button.B_MAIL_LIST_SORTBY_SIZE) {

			locator = "css=td[id='zlh__TV-main__sz'] td[id='zlhl__TV-main__sz']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();
			return (null);

		} else if (button == Button.B_MAIL_LIST_SORTBY_RECEIVED) {

			locator = "css=td[id='zlh__TV-main__dt'] td[id='zlhl__TV-main__dt']";
			this.zClick(locator);
			this.zWaitForBusyOverlay();
			return (null);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClick(locator);
		zWaitForBusyOverlay();

		if (button == Button.B_EML_ATTACHEMENT) {
			SleepUtil.sleepVeryLong();
		} else {
			SleepUtil.sleepSmall();
		}

		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + ")");
		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		boolean isCLV = this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0);

		if (pulldown == Button.B_TAG) {
			if (option == Button.O_TAG_NEWTAG) {

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id$='__TAG_MENU|MENU'] td[id='mail_newtag_title']";
				page = new DialogTag(this.MyApplication, this);

			} else if (option == Button.O_TAG_REMOVETAG) {

				pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id$='__TAG_MENU|MENU'] td[id='mail_removetag_title']";

				page = null;

			} else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else if (pulldown == Button.B_NEW) {

			pulldownLocator = "css=td[id$='__NEW_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";

			if (option == Button.O_NEW_TAG) {

				optionLocator = "css=td[id$='__NEW_MENU_NEW_TAG_left_icon']>div[class='ImgNewTag']";
				page = new DialogTag(this.MyApplication, this);

			} else if (option == Button.O_NEW_FOLDER) {

				optionLocator = "css=td[id$='__NEW_MENU_NEW_FOLDER_left_icon']>div[class='ImgNewFolder']";
				page = new DialogCreateFolder(this.MyApplication, this);

			}

		} else if (pulldown == Button.B_ATTACH) {

			pulldownLocator = Locators.zAttachdropdown;

			if (option == Button.O_MAILATTACH) {

				optionLocator = Locators.zAttachMailMenu;
				page = null;

			} else if (option == Button.O_CONTACTATTACH) {

				optionLocator = Locators.zAttachContactMenu;
				page = null;

			} else if (option == Button.O_BRIEFCASEATTACH) {

				optionLocator = Locators.zAttachBriefcaseMenu;
				page = null;

			}

		} else if (pulldown == Button.B_ACTIONS) {

			if (isCLV) {
				pulldownLocator = "css=td[id='zb__CLV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__CLV-main']";
			} else {
				pulldownLocator = "css=td[id='zb__TV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__TV-main']";
			}

			if (option == Button.B_PRINT) {

				optionLocator += " div[id^='PRINT'] td[id$='_title']";
				page = new DialogRedirect(this.MyApplication, this);

			} else if ((option == Button.B_RESPORTSPAM) || (option == Button.B_RESPORTNOTSPAM)) {

				optionLocator += " div[id^='SPAM'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_LAUNCH_IN_SEPARATE_WINDOW) {

				optionLocator += " div[id*='DETACH'] td[id$='_title']";
				page = new SeparateWindowDisplayMail(this.MyApplication);

				this.sClickAt(pulldownLocator, "0,0");
				zWaitForBusyOverlay();

				this.sClickAt(optionLocator, "0,0");
				zWaitForBusyOverlay();

				SleepUtil.sleepVeryLong();

				return (page);

			} else if (option == Button.O_MARK_AS_READ) {

				optionLocator += " div[id^='MARK_READ'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_MARK_AS_UNREAD) {

				optionLocator += " div[id^='MARK_UNREAD'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_SHOW_ORIGINAL) {

				optionLocator += " div[id^='SHOW_ORIG'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_SHOW_CONVERSATION) {

				optionLocator = "id=zmi__TV-main__SHOW_CONV_title";
				page = null;

			} else if (option == Button.B_REDIRECT) {

				optionLocator += " div[id*='REDIRECT'] td[id$='_title']";
				page = new DialogRedirect(this.MyApplication, this);

			} else if (option == Button.B_MUTE) {

				optionLocator += " div[id^='MUTE_CONV'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_EDIT_AS_NEW) {

				optionLocator += " div[id*='EDIT_AS_NEW'] td[id$='_title']";
				page = new FormMailNew(this.MyApplication);

			} else if (option == Button.O_NEW_FILTER) {

				optionLocator += " div[id^='ADD_FILTER_RULE'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_NEW_APPOINTMENT) {

				optionLocator += " div[id^='CREATE_APPT'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_NEW_TASK) {

				optionLocator += " div[id^='CREATE_TASK'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_QUICK_COMMANDS_MENU) {

				optionLocator += " div[id^='QUICK_COMMANDS'] td[id$='_title']";
				page = null;

			} else {

				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else if ((pulldown == Button.B_OPTIONS) && (option == Button.O_ADD_SIGNATURE)) {

			pulldownLocator = "css=td[id$='_ADD_SIGNATURE_dropdown']>div[class='ImgSelectPullDownArrow']";
			page = null;

		} else if (pulldown == Button.B_MOVE) {

			if (option == Button.O_NEW_FOLDER) {

				if (this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0)) {
					pulldownLocator = "css=div[id='ztb__CLV-main'] div[id$='__MOVE_MENU'] td[id$='_dropdown']";
					optionLocator = "css=div[id='ZmMoveButton_CLV-main'] div[id$='NEWFOLDER'] td[id$='_title']";
				} else {
					pulldownLocator = "css=div[id='ztb__TV-main'] div[id$='__MOVE_MENU'] td[id$='_dropdown']";
					optionLocator = "css=div[id='ZmMoveButton_TV-main'] div[id$='NEWFOLDER'] td[id$='_title']";
				}
				page = new DialogCreateFolder(this.MyApplication, this);

				if (!this.sIsElementPresent(pulldownLocator)) {
					throw new HarnessException(pulldownLocator + " not present!");
				}

				this.sClickAt(pulldownLocator, "0,0");
				zWaitForBusyOverlay();

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "");
				zWaitForBusyOverlay();
				SleepUtil.sleepMedium();
				page.zWaitForActive();

				return (page);

			} else {
				throw new HarnessException("no logic defined for B_MOVE and " + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		if (pulldownLocator != null) {

			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.sClickAt(pulldownLocator, "0,0");
			zWaitForBusyOverlay();

			if (optionLocator != null) {
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "0,0");
				zWaitForBusyOverlay();
				SleepUtil.sleepSmall();
			}

			if (page != null) {
				page.zWaitForActive();
			}
		}

		return (page);

	}

	public enum PageMailView {
		BY_MESSAGE, BY_CONVERSATION
	}

	/**
	 * Get the Page Property: ListView = By message OR By Conversation
	 *
	 * @return
	 * @throws HarnessException
	 */
	public PageMailView zGetPropMailView() throws HarnessException {
		if (this.zIsVisiblePerPosition(Locators.IsConViewActiveCSS, 0, 0)) {
			return (PageMailView.BY_CONVERSATION);
		} else if (this.zIsVisiblePerPosition(Locators.IsMsgViewActiveCSS, 0, 0)) {
			return (PageMailView.BY_MESSAGE);
		}

		throw new HarnessException("Unable to determine the Page Mail View");
	}

	public MailItem parseMessageRow(String top) throws HarnessException {
		MailItem item = null;

		if (top.contains("CLV")) {
			item = new ConversationItem();

			if (this.sIsElementPresent(top.trim() + "[class*='ZmConvExpanded']"))
				((ConversationItem) item).gIsConvExpanded = true;

		} else if (top.contains("TV")) {
			item = new MailItem();
		} else {
			throw new HarnessException("Unknown message row type " + top);
		}

		String msglocator = top;
		String locator;

		// Is it checked?
		locator = msglocator + " div[class*='ImgCheckboxChecked']";
		item.gIsSelected = this.sIsElementPresent(locator);

		// Is it flagged
		locator = msglocator + " div[class*='ImgFlagRed']";
		item.gIsFlagged = this.sIsElementPresent(locator);

		// Is it high priority?
		item.gPriority = MailItem.Priority.Normal;
		if (this.sIsElementPresent(msglocator + " div[id$='__pr']>div[class*=ImgPriorityHigh_list]"))
			item.gPriority = MailItem.Priority.High;
		if (this.sIsElementPresent(msglocator + " div[id$='__pr']>div[class*=ImgPriorityLow_list]"))
			item.gPriority = MailItem.Priority.Low;

		locator = msglocator + " div[id$='__tg']";

		// Get the From
		locator = msglocator + " [id*='__fr']";
		item.gFrom = this.sGetText(locator).trim();

		// Get the attachment
		locator = msglocator + " div[id$='__at'][class*=ImgBlank_16]";
		if (this.sIsElementPresent(locator)) {
			item.gHasAttachments = false;
		} else {
		}

		// Get the fragment and the subject
		item.gFragment = "";
		item.gSubject = "";
		locator = msglocator + " [id$='__fm']";
		if (this.sIsElementPresent(locator)) {

			item.gFragment = this.sGetText(locator).trim();

			// Get the subject
			locator = msglocator + " [id$='__su']";
			if (this.sIsElementPresent(locator)) {

				String subject = this.sGetText(locator).trim();
				item.gSubject = subject.replace(item.gFragment, "").trim();

			}

		} else {

			// Conversation items's fragment is in the subject field
			locator = msglocator + " [id$='__su']";
			if (this.sIsElementPresent(locator)) {
				item.gFragment = this.sGetText(locator).trim();
				item.gSubject = "";
			}
		}

		// Get the folder
		locator = msglocator + " nobr[id$='__fo']";
		if (this.sIsElementPresent(locator)) {
			item.gFolder = this.sGetText(locator).trim();
		} else {
			item.gFolder = "";
		}

		// Get the size
		locator = msglocator + " nobr[id$='__sz']";
		if (this.sIsElementPresent(locator)) {
			item.gSize = this.sGetText(locator).trim();
		} else {
			item.gSize = "";
		}

		// Get the received date
		locator = msglocator + " [id$='__dt']";
		item.gReceived = this.sGetText(locator).trim();

		return (item);
	}

	public List<MailItem> zListGetMessages() throws HarnessException {

		List<MailItem> items = new ArrayList<MailItem>();

		String listLocator = null;
		String rowLocator = null;
		if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
			listLocator = "css=ul[id='zl__TV-main__rows']";
			rowLocator = "li[id^='zli__TV-main__']";
		} else {
			listLocator = "css=ul[id='zl__CLV-main__rows']";
			rowLocator = "li[id^='zli__CLV-main__']";
		}

		// Make sure the button exists
		if (!this.sIsElementPresent(listLocator))
			throw new HarnessException("Message List View Rows is not present: " + listLocator);

		String tableLocator = listLocator + " " + rowLocator;
		// How many items are in the table?
		int count = this.sGetCssCount(tableLocator);
		logger.debug(myPageName() + " zListGetMessages: number of messages: " + count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			// Add the new item to the list
			MailItem item = parseMessageRow(listLocator + " li:nth-of-type(" + i + ") ");
			items.add(item);
			logger.info(item.prettyPrint());
		}

		// Return the list of items
		return (items);
	}

	@Override
	public AbsPage zListItem(Action action, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + subject + ")");

		tracer.trace(action + " on subject = " + subject);

		if (action == null)
			throw new HarnessException("action cannot be null");

		if (subject == null)
			throw new HarnessException("subject cannot be null");

		AbsPage page = null;
		String listLocator;
		String rowLocator;
		String itemlocator = null;

		// Find the item locator
		//

		if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
			listLocator = "css=ul[id='zl__TV-main__rows']";
			rowLocator = "li[id^='zli__TV-main__']";
		} else {
			listLocator = "css=ul[id='zl__CLV-main__rows']";
			rowLocator = "li[id^='zli__CLV-main__']";
		}

		// view first?
		if (!this.sIsElementPresent(listLocator))
			throw new HarnessException("List View Rows is not present " + listLocator);

		// How many items are in the table?
		int count = this.sGetCssCount(listLocator + " " + rowLocator);
		logger.debug(myPageName() + " zListSelectItem: number of list items: " + count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			itemlocator = listLocator + " li:nth-of-type(" + i + ") ";
			String s = this.sGetText(itemlocator + " [id$='__su']").trim();

			if (s.contains(subject)) {
				break; // found it
			}

			itemlocator = null;
		}

		if (itemlocator == null) {
			throw new HarnessException("Unable to locate item with subject(" + subject + ")");
		}

		if (action == Action.A_LEFTCLICK) {

			// Left-Click on the item
			this.zClickAt(itemlocator, "");

			this.zWaitForBusyOverlay();

			// Return the displayed mail page object
			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				page = new DisplayMail(MyApplication);
			} else {
				page = new DisplayConversation(MyApplication);
			}

		} else if (action == Action.A_DOUBLECLICK) {

			// Double-Click on the item
			this.sDoubleClick(itemlocator);

			this.zWaitForBusyOverlay();

			page = new DisplayMail(MyApplication);

		} else if (action == Action.A_CTRLSELECT) {

			throw new HarnessException("implement me!  action = " + action);

		} else if (action == Action.A_SHIFTSELECT) {

			throw new HarnessException("implement me!  action = " + action);

		} else if (action == Action.A_RIGHTCLICK) {

			// Right-Click on the item
			this.zRightClick(itemlocator);

			// Return the displayed mail page object
			page = new ContextMenu(MyApplication);

		} else if (action == Action.A_MAIL_CHECKBOX) {

			String selectlocator = itemlocator + " div[id$='__se']>div.ImgCheckboxUnchecked";
			if (!this.sIsElementPresent(selectlocator))
				throw new HarnessException("Checkbox locator is not present " + selectlocator);

			String image = this.sGetAttribute(selectlocator + "@class");
			if (image.equals("ImgCheckboxChecked"))
				throw new HarnessException("Trying to check box, but it was already enabled");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else if (action == Action.A_MAIL_UNCHECKBOX) {

			String selectlocator = itemlocator + " div[id$='__se']>div.ImgCheckboxChecked";
			if (!this.sIsElementPresent(selectlocator))
				throw new HarnessException("Checkbox locator is not present " + selectlocator);

			String image = this.sGetAttribute(selectlocator + "@class");
			if (image.equals("ImgCheckboxUnchecked"))
				throw new HarnessException("Trying to uncheck box, but it was already disabled");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else if (action == Action.A_MAIL_EXPANDCONVERSATION) {

			String selectlocator = itemlocator + " div[id$='__ex']";
			if (!this.sIsElementPresent(selectlocator))
				throw new HarnessException("Checkbox locator is not present " + selectlocator);

			String image = this.sGetAttribute(selectlocator + ">div@class");
			if (image.equals("ImgNodeExpanded"))
				throw new HarnessException("Trying to expand, but conversation was alread expanded");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else if (action == Action.A_MAIL_COLLAPSECONVERSATION) {

			String selectlocator = itemlocator + " div[$id$='__ex']";
			if (!this.sIsElementPresent(selectlocator))
				throw new HarnessException("Checkbox locator is not present " + selectlocator);

			String image = this.sGetAttribute(selectlocator + "@class");
			if (image.equals("ImgNodeCollapsed"))
				throw new HarnessException("Trying to collapse, but conversation was alread collapsed");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else if ((action == Action.A_MAIL_FLAG) || (action == Action.A_MAIL_UNFLAG)) {
			// Both FLAG and UNFLAG have the same action and result

			String flaglocator = itemlocator + " div[id$='__fg']";

			// Left-Click on the flag field
			this.zClick(flaglocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else {
			throw new HarnessException("implement me!  action = " + action);
		}

		if (page != null) {
			page.zWaitForActive();
		}

		SleepUtil.sleepMedium();
		
		return (page);

	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		tracer.trace(action + " then " + option + "," + subOption + " on item = " + item);

		throw new HarnessException("implement me!");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String subject) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + option + ", " + subject + ")");

		tracer.trace(action + " then " + option + " on subject = " + subject);

		if (action == null)
			throw new HarnessException("action cannot be null");
		if (option == null)
			throw new HarnessException("button cannot be null");
		if (subject == null || subject.trim().length() == 0)
			throw new HarnessException("subject cannot be null or blank");

		AbsPage page = null;
		String listLocator;
		String rowLocator;
		String itemlocator = null;

		if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
			listLocator = "css=ul[id='zl__TV-main__rows']";
			rowLocator = "li[id^='zli__TV-main__']";
		} else {
			listLocator = "css=ul[id='zl__CLV-main__rows']";
			rowLocator = "li[id^='zli__CLV-main__']";
		}

		// view first?
		if (!this.sIsElementPresent(listLocator))
			throw new HarnessException("List View Rows is not present " + listLocator);

		// How many items are in the table?
		int count = this.sGetCssCount(listLocator + " " + rowLocator);
		logger.debug(myPageName() + " zListSelectItem: number of list items: " + count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			itemlocator = listLocator + " div:nth-of-type(" + i + ") ";
			String s = this.sGetText(itemlocator + " [id$='__su']").trim();

			if (s.contains(subject)) {
				break; // found it
			}

			itemlocator = null;
		}

		if (itemlocator == null) {
			throw new HarnessException("Unable to locate item with subject(" + subject + ")");
		}

		if (action == Action.A_RIGHTCLICK) {

			this.zRightClickAt(itemlocator, "");

			String optionLocator = "css=div[id^='zm__CLV-main__']";
			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				optionLocator = "div[id^='zm__TV-main__']";
			}

			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				optionLocator = "css=div[id^='zm__TV-main__']";
			}

			if (option == Button.O_MARK_AS_READ) {

				optionLocator += " div[id*S='MARK_READ'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_MARK_AS_UNREAD) {

				optionLocator += " div[id*='MARK_UNREAD'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_DELETE) {

				optionLocator += " div[id*='DELETE'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_REPLY) {

				optionLocator += " div[id^='REPLY'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_REPLYALL) {

				optionLocator += " div[id^='REPLY'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_FORWARD) {

				optionLocator += " div[id^='REPLY'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_REDIRECT) {

				optionLocator += " div[id*='REDIRECT'] td[id$='_title']";
				page = new DialogRedirect(this.MyApplication, this);

			} else if (option == Button.B_MUTE) {

				optionLocator += " div[id^='MUTE_CONV'] td[id$='_title']";
				page = null;

			} else if (option == Button.O_EDIT_AS_NEW) {

				optionLocator += " div[id*='EDIT_AS_NEW'] td[id$='_title']";
				page = new FormMailNew(this.MyApplication);


			} else if (option == Button.O_CREATE_APPOINTMENT) {
				optionLocator += " div[id^='zmi__CLV-main__CREATE_APPT'] td[id^='zmi__CLV-main__CREATE_APPT']";
				page = new DialogAddAttendees(this.MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			} else if (option == Button.O_CREATE_TASK) {

				optionLocator += " div[id^='zmi__TV-main__CREATE_TASK'] td[id$='_title']";
				page = null;

			} else {
				throw new HarnessException("implement action:" + action + " option:" + option);
			}

			this.zClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();

		} else {
			throw new HarnessException("implement me!  action = " + action);
		}

		if (page != null) {
			page.zWaitForActive();
		}

		// Default behavior
		return (page);

	}

	@Override
	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		String keyCode;
		if (shortcut == null)
			throw new HarnessException("Shortcut cannot be null");

		tracer.trace("Using the keyboard, press the " + shortcut.getKeys() + " keyboard shortcut");

		AbsPage page = null;

		if ((shortcut == Shortcut.S_NEWITEM) || (shortcut == Shortcut.S_NEWMESSAGE)
				|| (shortcut == Shortcut.S_NEWMESSAGE2)) {

			// "New Message" shortcuts result in a compose form opening
			page = new FormMailNew(this.MyApplication);

		} else if ((shortcut == Shortcut.S_NEWITEM_IN_NEW_WINDOW) || (shortcut == Shortcut.S_NEWMESSAGE_IN_NEW_WINDOW)
				|| (shortcut == Shortcut.S_NEWMESSAGE2_IN_NEW_WINDOW)) {

			// These shortcuts result in a separate window opening
			page = new SeparateWindowFormMailNew(this.MyApplication);

			// Don't fall through. The test case needs to make sure the separate
			// window opens
			zKeyboard.zTypeCharacters(shortcut.getKeys());
			return (page);

		} else if ((shortcut == Shortcut.S_NEWTAG)) {

			// "New Message" shortcuts result in a compose form opening
			// page = new FormMailNew(this.MyApplication);
			page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

		} else if ((shortcut == Shortcut.S_MAIL_TAG)) {

			// "New Message" shortcuts result in a compose form opening
			// page = new FormMailNew(this.MyApplication);
			page = new DialogTagPicker(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

		} else if ((shortcut == Shortcut.S_NEWFOLDER)) {

			// "New Message" shortcuts result in a compose form opening
			// page = new FormMailNew(this.MyApplication);
			page = new DialogCreateFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

		} else if ((shortcut == Shortcut.S_MAIL_HARDELETE)) {

			// Hard Delete shows the Warning Dialog : Are you sure you want to
			// permanently delete it?
			page = new DialogWarning(DialogWarning.DialogWarningID.PermanentlyDeleteTheItem, MyApplication,
					((AppAjaxClient) MyApplication).zPageMail);

		} else if (shortcut == Shortcut.S_ASSISTANT) {

			page = new DialogAssistant(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

		} else if (shortcut == Shortcut.S_ESCAPE) {

			page = new DialogWarning(DialogWarning.DialogWarningID.SaveCurrentMessageAsDraft, this.MyApplication,
					((AppAjaxClient) this.MyApplication).zPageMail);
			WebElement we = getElement("css=div#z_banner");
			we.sendKeys(Keys.ESCAPE);
			this.zWaitForBusyOverlay();

			if (page != null) {
				page.zWaitForActive();
			}

			return page;
		} else if (shortcut == Shortcut.S_PASTE) {
			page = null;
			keyCode = "17,86";
			zKeyDown(keyCode);
			return (page);

		} else if (shortcut == Shortcut.S_COPY) {
			page = null;
			keyCode = "17,67";
			zKeyDown(keyCode);
			return (page);

		}

		else if (shortcut == Shortcut.S_UNTAG) {
			page = null;
			keyCode = "85";
			zKeyDown(keyCode);

			return (page);
		}

		zKeyboard.zTypeCharacters(shortcut.getKeys());

		// If the app is busy, wait for it to become active
		this.zWaitForBusyOverlay();

		// If a page is specified, wait for it to become active
		if (page != null) {
			page.zWaitForActive(); // This method throws a HarnessException if
									// never active
		}
		return (page);
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option, Object dynamic) throws HarnessException {
		logger.info(
				myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + ", " + dynamic + ")");
		tracer.trace("Click pulldown " + pulldown + " then " + option + " then " + dynamic);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");
		if (option == null)
			throw new HarnessException("Option cannot be null!");
		if (dynamic == null)
			throw new HarnessException("dynamic object cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		String dynamicLocator = null;
		AbsPage page = null;

		if ((pulldown == Button.B_OPTIONS) && (option == Button.O_ADD_SIGNATURE)) {

			if (!(dynamic instanceof String))
				throw new HarnessException("dynamic must be a string!  " + dynamic.getClass().getCanonicalName());

			String name = (String) dynamic;
			logger.info("Click on Signature: " + name);

			pulldownLocator = "css=[id^=zb__COMPOSE][id$=__COMPOSE_OPTIONS_dropdown]";
			optionLocator = "css=div[id$='ADD_SIGNATURE'] tr[id='POPUP_zmi__COMPOSE-1_NEW_MESSAGE__ADD_SIGNATURE']> td[id='zmi__COMPOSE-1_NEW_MESSAGE__ADD_SIGNATURE_dropdown']>div[class='ImgCascade']";
			dynamicLocator = "css=td[id$='_title']:contains('" + name + "')";
			page = null;

		} else if ((pulldown == Button.B_OPTIONS) && (option == Button.O_ADD_FWD_SIGNATURE)) {

			if (!(dynamic instanceof String))
				throw new HarnessException("dynamic must be a string!  " + dynamic.getClass().getCanonicalName());

			String name = (String) dynamic;
			logger.info("Click on Signature: " + name);

			pulldownLocator = "css=[id^=zb__COMPOSE][id$=__COMPOSE_OPTIONS_dropdown]";
			optionLocator = "css=div[id$='_FORWARD_ATT'] div[id$='ADD_SIGNATURE'] tr[id='POPUP_zmi__COMPOSE-1_FORWARD_ATT__ADD_SIGNATURE']>td[id$='_dropdown']>div[class='ImgCascade']";
			dynamicLocator = "css=td[id$='_title']:contains('" + name + "')";
			page = null;

		} else if ((pulldown == Button.B_OPTIONS) && (option == Button.O_ADD_Reply_SIGNATURE)
				|| (option == Button.O_ADD_ReplyAll_SIGNATURE)) {

			if (!(dynamic instanceof String))
				throw new HarnessException("dynamic must be a string!  " + dynamic.getClass().getCanonicalName());

			String name = (String) dynamic;
			pulldownLocator = "css=[id^=zb__COMPOSE][id$=__COMPOSE_OPTIONS_dropdown]";
			optionLocator = "css=div[id$='_REPLY'] div[id$='ADD_SIGNATURE'] tr[id='POPUP_zmi__COMPOSE-1_REPLY__ADD_SIGNATURE']>td[id$='_dropdown']>div[class='ImgCascade']";
			dynamicLocator = "css=td[id$='_title']:contains('" + name + "')";
			page = null;

		} else if (pulldown == Button.B_ACTIONS) {

			boolean isCLV = this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0);

			if (isCLV) {
				pulldownLocator = "css=td[id='zb__CLV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__CLV-main']";
			} else {
				pulldownLocator = "css=td[id='zb__TV-main__ACTIONS_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
				optionLocator = "css=div[id='zm__TV-main']";
			}

			if (option == Button.O_QUICK_COMMANDS_MENU) {

				if (!(dynamic instanceof String))
					throw new HarnessException("dynamic must be a string!  " + dynamic.getClass().getCanonicalName());

				String quickcommand = (String) dynamic;
				logger.info("Click on Quick Command: " + quickcommand);

				optionLocator += " div[id^='QUICK_COMMANDS'] td[id$='_title']";
				dynamicLocator = "css=div[id^='quickCommandSubMenu_'] td[id$='_title']:contains('" + quickcommand
						+ "')";
				page = null;

				// Make sure the locator exists
				if (!this.sIsElementPresent(pulldownLocator)) {
					throw new HarnessException(pulldownLocator + " not present!");
				}

				this.zClickAt(pulldownLocator, "");
				zWaitForBusyOverlay();

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(optionLocator + " not present!");
				}

				this.sMouseOver(optionLocator);
				zWaitForBusyOverlay();
				SleepUtil.sleepSmall();

				this.zClickAt(dynamicLocator, "");
				zWaitForBusyOverlay();

				return (page);

			} else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.zClickAt(pulldownLocator, "");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(
							" option " + option + " optionLocator " + optionLocator + " not present!");
				}
				this.sMouseOver(optionLocator);
				// this.zClickAt(optionLocator,"");

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();

			}
			if (dynamicLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(dynamicLocator)) {
					throw new HarnessException(dynamicLocator + " not present!");
				}
				// this.sMouseOver(dynamicLocator);
				this.zClickAt(dynamicLocator, "");
				SleepUtil.sleepMedium();

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
			}

		}

		// Return the specified page, or null if not set
		return (page);

	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Object dynamic) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressPulldown(" + pulldown + ", " + dynamic + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + dynamic);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (dynamic == null)
			throw new HarnessException("Option cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_MOVE) {

			if (!(dynamic instanceof FolderItem))
				throw new HarnessException("if pulldown = " + Button.B_MOVE + ", then dynamic must be FolderItem");

			FolderItem folder = (FolderItem) dynamic;
			
			// Check if we are CLV or MV
			if (this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0)) {
				pulldownLocator = "css=td#zb__CLV-main__MOVE_MENU_dropdown>div";
				optionLocator = "css=td#zti__ZmFolderChooser_MailCLV-main__" + folder.getId() + "_textCell";
			} else {
				pulldownLocator = "css=td#zb__TV-main__MOVE_MENU_dropdown>div";
				optionLocator = "css=td#zti__ZmFolderChooser_MailTV-main__" + folder.getId() + "_textCell";
			}
			page = null;

		} else if (pulldown == Button.B_TAG) {

			if (!(dynamic instanceof TagItem))
				throw new HarnessException("if pulldown = " + Button.B_TAG + ", then dynamic must be TagItem");

			TagItem tag = (TagItem) dynamic;

			pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
			optionLocator = "css=div[id='zb__TV-main__TAG_MENU|MENU'] td[id$='_title']:contains(" + tag.getName() + ")";
			page = null;

		} else {

			throw new HarnessException("no logic defined for pulldown/dynamic " + pulldown + "/" + dynamic);

		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " pulldownLocator " + pulldownLocator + " not present!");
			}

			this.sClickAt(pulldownLocator, "");
			zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(" dynamic " + dynamic + " optionLocator " + optionLocator + " not present!");
				}

				logger.info(this.sIsElementPresent(optionLocator));
				this.sClickAt(optionLocator, "");
				zWaitForBusyOverlay();
				SleepUtil.sleepSmall();
			}

		}

		// Return the specified page, or null if not set
		return (page);

	}

	public AbsTooltip zHoverOver(Button button) throws HarnessException {
		logger.info(myPageName() + " zHoverOverButton(" + button + ")");

		tracer.trace("Hover over " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null");

		String locator = null;

		if (button == Button.B_DELETE) {

			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				locator = "css=td[id='zb__TV-main__DELETE_title']";
			} else {
				locator = "css=td[id='zb__CLV-main__DELETE_title']";
			}

		} else if (button == Button.B_REPLY) {

			if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
				locator = "css=td[id='zb__TV-main__REPLY_title']";
			} else {
				locator = "css=td[id='zb__CLV-main__REPLY_title']";
			}

		} else {
			throw new HarnessException("no logic defined for button: " + button);
		}

		Tooltip tooltip = new Tooltip(MyApplication);
		if (tooltip.zIsActive()) {

			// Mouse over
			this.sMouseOver(locator);
			this.zWaitForActive();

			// Wait for the new text
			SleepUtil.sleep(5000);

			// Make sure the tooltip is active
			tooltip.zWaitForActive();

		} else {

			// Mouse over
			this.sMouseOver(locator);
			this.zWaitForActive();

			// Make sure the tooltip is active
			tooltip.zWaitForActive();

		}

		return (tooltip);
	}

	/**
	 * Check warning icon,Display Image link,Domain link
	 *
	 * @return
	 * @throws HarnessException
	 */
	public boolean zHasWDDLinks() throws HarnessException {

		if (zGetPropMailView() == PageMailView.BY_MESSAGE) {
			List<String> locators = Arrays.asList(Locators.zMsgViewDisplayImgLink, Locators.zMsgViewDomainLink,
					Locators.zMsgViewWarningIcon);
			for (String locator : locators) {
				if (!this.sIsElementPresent(locator))
					return (false);
			}
			return (true);

		} else if (zGetPropMailView() == PageMailView.BY_CONVERSATION) {
			List<String> locators = Arrays.asList(Locators.zConViewDisplayImgLink, Locators.zConViewDomainLink,
					Locators.zConViewWarningIcon);
			for (String locator : locators) {
				if (!this.sIsElementPresent(locator))
					return (false);
			}
			return (true);

		} else {
			throw new HarnessException("no logic defined  ");
		}
	}

	public void zRightClickAddressBubble(Field field) throws HarnessException {
		SleepUtil.sleepMedium();
		logger.info("zRightClickAddressBubble(" + field + ")");

		if (field == Field.To) {
			try {
				this.zRightClick(Locators.zToAddressBubble);
			} catch (Exception ex) {
				throw new HarnessException(ex);
			}

		} else if (field == Field.Cc) {
			try {
				SleepUtil.sleepMedium();
				this.zRightClick(
						"css=tr[id='zv__COMPOSE-1_cc_row'] td[id='zv__COMPOSE-1_cc_cell'] div div span[class^='addrBubble']>span");
				SleepUtil.sleepSmall();
			} catch (Exception ex) {
				throw new HarnessException(ex);
			}

		} else if (field == Field.Bcc) {
			SleepUtil.sleepMedium();
			try {
				this.zRightClick(
						"css=tr[id='zv__COMPOSE-1_bcc_row'] td[id='zv__COMPOSE-1_bcc_cell'] div div span[class^='addrBubble']>span");
				SleepUtil.sleepSmall();
			} catch (Exception ex) {
				throw new HarnessException(ex);
			}

		} else if (field == Field.From) {
			SleepUtil.sleepMedium();
			try {
				this.zRightClick(Locators.zFromHdrAddressBubble);
			} catch (Exception ex) {
				throw new HarnessException(ex);
			}
		}
		SleepUtil.sleepSmall();
	}

	public void zDeleteAddressContextMenu() throws HarnessException {
		this.sFocus(Locators.zDeleteAddressContextMenu);
		this.sClickAt(Locators.zDeleteAddressContextMenu, "");
	}

	public void zCopyAddressContextMenu() throws HarnessException {
		this.sFocus(Locators.zCopyAddressContextMenu);
		this.sClickAt(Locators.zCopyAddressContextMenu, "");
	}

	public void zEditAddressContextMenu() throws HarnessException {
		this.sFocus(Locators.zEditAddressContextMenu);
		this.sClickAt(Locators.zEditAddressContextMenu, "");
	}

	public void zAddToContactAddressContextMenu() throws HarnessException {
		this.sFocus(Locators.zContactAddressContextMenu);
		this.sClickAt(Locators.zContactAddressContextMenu, "");

	}

	public void zMoveToCcAddressContextMenu() throws HarnessException {
		this.sFocus(Locators.zMoveToCcAddressContextMenu);
		this.sClickAt(Locators.zMoveToCcAddressContextMenu, "");
	}

	public void zMoveToBCcAddressContextMenu() throws HarnessException {
		this.sFocus(Locators.zMoveToBccAddressContextMenu);
		this.sClickAt(Locators.zMoveToBccAddressContextMenu, "");
	}

	public void NewEmailMsgHdrContextMenu() throws HarnessException {
		this.sFocus(Locators.zNewEmailsMsgHdrContextMenu);
		this.sClickAt(Locators.zNewEmailsMsgHdrContextMenu, "");

	}

	public void zAddToContactMsgHdrContextMenu() throws HarnessException {
		this.sFocus(Locators.zAddToContactMsgHdrContextMenu);
		this.sClickAt(Locators.zAddToContactMsgHdrContextMenu, "");
	}

	public void zCreateFilterMsgHdrContextMenu() throws HarnessException {
		this.sFocus(Locators.zCreateFilterMsgHdrContextMenu);
		this.sClickAt(Locators.zCreateFilterMsgHdrContextMenu, "");
		this.sClickAt(Locators.zAddFilterMsgHdrContextMenu, "");
	}

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.O_ATTACH_DROPDOWN) {
			if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_dropdown']")) {
				locator = "css=td[id='zb__COMPOSE-2___attachments_btn_dropdown']";
			} else {
				locator = "css=td[id='zb__COMPOSE-1___attachments_btn_dropdown']";
			}

		} else if (button == Button.B_ATTACH) {
			if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_title']")) {
				locator = "css=td[id='zb__COMPOSE-2___attachments_btn_title']";
			} else {
				locator = "css=td[id='zb__COMPOSE-1___attachments_btn_title']";
			}

		} else if (button == Button.B_MY_COMPUTER) {
			locator = "css=div[class='DwtMenu'] td[id$='_title']:contains('My Computer')";

		} else if (button == Button.B_ATTACH_INLINE) {
			locator = "css=div[class='DwtMenu'] td[id$='_title']:contains('Attach Inline')";

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}
		
		zWaitTillElementPresent(locator);
		this.sClick(locator);
		
		return (page);
	}

	public boolean zVerifyAllAddressContextMenu(String app) throws HarnessException {

		List<String> locators = new ArrayList<String>();

		if (app == "calendar") {
			locators = Arrays.asList(Locators.zDeleteAddressContextMenu, Locators.zCopyAddressContextMenu,
					Locators.zEditAddressContextMenu, Locators.zExpandAddressContextMenu,
					Locators.zContactAddressContextMenu);

		} else if (app == "MessageHeader") {
			locators = Arrays.asList(Locators.zCopyMsgHdrContextMenu, Locators.zFindEmailsMsgHdrContextMenu,
					Locators.zNewEmailsMsgHdrContextMenu, Locators.zAddToContactMsgHdrContextMenu,
					Locators.zGoToUrlMsgHdrContextMenu, Locators.zCreateFilterMsgHdrContextMenu);

		} else {

			locators = Arrays.asList(Locators.zDeleteAddressContextMenu, Locators.zCopyAddressContextMenu,
					Locators.zEditAddressContextMenu, Locators.zExpandAddressContextMenu,
					Locators.zContactAddressContextMenu, Locators.zMovetToToAddressContextMenu,
					Locators.zMoveToCcAddressContextMenu, Locators.zMoveToBccAddressContextMenu);
		}

		for (String locator : locators) {
			if (!this.sIsElementPresent(locator))
				return (false);

		}

		return (true);

	}

	public boolean zHasTOCcBccEmpty() throws HarnessException {

		List<String> locators = Arrays.asList(
				"css=td[id='zv__COMPOSE-1_to_cell'] div div[class='addrBubbleHolder-empty']",
				"css=td[id='zv__COMPOSE-1_cc_cell'] div div[class='addrBubbleHolder-empty']",
				"css=td[id='zv__COMPOSE-1_bcc_cell'] div div[class='addrBubbleHolder-empty']");

		for (String locator : locators) {
			if (!this.sIsElementPresent(locator))
				return (false);

		}

		return (true);
	}

	public AbsPage zToolbarPressButtonFullViewPane(Button button) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressButtonFullView (" + button + ")");
		tracer.trace("Click page button " + button);

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_DELETE_FULL_VIEW_PANE) {
			locator = Locators.zDeleteButtonFullViewPane;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Make sure the locator was set
		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator " + locator + " not present!");
		}

		this.sClickAt(locator, "");
		SleepUtil.sleepMedium();

		return (page);

	}

}
