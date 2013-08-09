/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.util.staf;

import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * Use the STAF FS service to execute a command
 * 
 * @author Matt Rhoades
 *
 */
public class StafServiceFS extends StafAbstract {
	
	public StafServiceFS() {
		logger.info("new "+ StafServiceFS.class.getCanonicalName());
		
		StafService = "FS";
		StafParms = "QUERY ENTRY /tmp";
		
	}
	
	/**
	 * Execute the STAF request<p>
	 * For example, if "command = 'QUERY ENTRY /tmp'", then execute 'STAF server FS QUERY ENTRY /tmp'
	 * @return 
	 * @throws HarnessException 
	 */
	public boolean execute(String command) throws HarnessException {
		StafParms = command;
		return (super.execute());
	}
}
