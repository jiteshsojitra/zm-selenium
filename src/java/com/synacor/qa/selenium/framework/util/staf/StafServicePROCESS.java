package com.synacor.qa.selenium.framework.util.staf;

import com.synacor.qa.selenium.framework.util.HarnessException;

public class StafServicePROCESS extends StafAbstract {

	public static final int StafTimeoutMillisDefault = 100000;
	private int StafTimeoutMillis = StafTimeoutMillisDefault;

	public StafServicePROCESS() {
		logger.info("new "+ StafServicePROCESS.class.getCanonicalName());

		StafService = "PROCESS";
		StafParms = "START SHELL COMMAND \"ls\" RETURNSTDOUT RETURNSTDERR WAIT "+ StafTimeoutMillis;

	}

	public void setTimeout(int timeout) {
		StafTimeoutMillis = timeout;
	}

	public int getTimeout() {
		return (StafTimeoutMillis);
	}

	public int resetTimeout() {
		StafTimeoutMillis = StafTimeoutMillisDefault;
		return (StafTimeoutMillis);
	}

	public boolean execute(String command) throws HarnessException {
		StafParms = String.format("START SHELL COMMAND \"%s\" RETURNSTDOUT RETURNSTDERR WAIT %d", command, StafTimeoutMillis);
		return (super.execute());
	}
}