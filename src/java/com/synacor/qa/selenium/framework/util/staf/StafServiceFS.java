package com.synacor.qa.selenium.framework.util.staf;

import com.synacor.qa.selenium.framework.util.HarnessException;

public class StafServiceFS extends StafAbstract {

	public StafServiceFS() {
		logger.info("new "+ StafServiceFS.class.getCanonicalName());

		StafService = "FS";
		StafParms = "QUERY ENTRY /tmp";

	}

	public StafServiceFS(String host) {
		logger.info("new "+ StafServiceFS.class.getCanonicalName());

		StafService = "FS";
		StafServer = host;
		StafParms = "QUERY ENTRY /tmp";

	}

	public boolean execute(String command) throws HarnessException {
		StafParms = command;
		return (super.execute());
	}
}