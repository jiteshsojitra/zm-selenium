package com.zimbra.qa.selenium.projects.universal.tests.mail.newwindow.mail;


/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DialogAddAttendees;
import com.zimbra.qa.selenium.projects.universal.ui.mail.SeparateWindowDisplayMail;


public class CreateAppointment extends PrefGroupMailByMessageTest {

	public int delaySeconds = 10;

	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefMarkMsgRead", "" + delaySeconds);


	}

	@Test( description = "Create Appointment from new window by action menu -> Create Appointment",
			groups = { "functional", "L2" })
	public void CreateAppointment_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ConfigProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");


		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(windowTitle);
			window.zWaitForActive();

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			window.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_CREATE_APPOINTMENT);
			SleepUtil.sleepMedium();
			app.zPageMail.zClickAt(DialogAddAttendees.Locators.zYesButton,"");
			SleepUtil.sleepLong();
			app.zPageMail.zClickAt("css=div[id^='ztb__APPT-'] td[id$='_SEND_INVITE_title']", "");
			SleepUtil.sleepMedium();

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
		
		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment'>"
						+		"<query>subject:("+ subject +")</query>"
						+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");


	}



}