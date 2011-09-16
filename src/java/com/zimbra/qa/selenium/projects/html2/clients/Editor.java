/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.html2.clients;

import com.zimbra.qa.selenium.framework.util.HarnessException;



public class Editor extends ZFieldObject {
	public Editor() {
		super("editorCore_html", "EmailBodyField");
	}
	public void zType(String data)  throws HarnessException  {
		if (data != "")
			ZObjectCore("", "type", true, data);
	}

}
