package com.synacor.qa.selenium.framework.util.staf;

import com.synacor.qa.selenium.framework.util.HarnessException;

public class StafServiceVAR extends StafAbstract {

	public StafServiceVAR() {
		logger.info("new "+ StafServiceVAR.class.getCanonicalName());

		this.StafServer = "local";
		this.StafService = "VAR";
		this.StafParms = "GET SYSTEM VAR STAF/Config/Machine";
	}

	public boolean execute(String command) throws HarnessException {
		StafParms = command;
		return (super.execute());
	}
}