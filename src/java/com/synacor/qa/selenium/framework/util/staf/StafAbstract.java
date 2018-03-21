package com.synacor.qa.selenium.framework.util.staf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFMarshallingContext;
import com.ibm.staf.STAFResult;
import com.synacor.qa.selenium.framework.core.ExecuteHarnessMain;
import com.synacor.qa.selenium.framework.util.*;
import com.synacor.qa.selenium.staf.StafIntegration;

public class StafAbstract {
	protected static Logger logger = LogManager.getLogger(StafAbstract.class);

	// STAF command settings
	protected String StafServer = null;
	protected String StafService = null;
	protected String StafParms = null;

	// STAF response
	protected STAFResult StafResult = null;
	protected String StafResponse = null;

	public StafAbstract() {
		logger.info("new "+ StafAbstract.class.getCanonicalName());

		StafServer = ConfigProperties.getStringProperty("server.host");
		StafService = "PING";
		StafParms = "PING";

		// STAF log
		StafIntegration.sSTAFLogFileFolderPath = ExecuteHarnessMain.testoutputfoldername + "/debug/projects";
		StafIntegration.sSTAFLogFilePath = StafIntegration.sSTAFLogFileFolderPath + "\\" + StafIntegration.sSTAFLogFileName;
		StafIntegration.pSTAFLogFilePath = Paths.get(StafIntegration.sSTAFLogFileFolderPath, StafIntegration.sSTAFLogFileName);
		StafIntegration.fSTAFLogFile = new File(StafIntegration.sSTAFLogFilePath);
		StafIntegration.fSTAFLogFileFolder = new File(StafIntegration.sSTAFLogFileFolderPath);
		if (!StafIntegration.fSTAFLogFileFolder.exists()) {
			StafIntegration.fSTAFLogFileFolder.mkdirs();
		}
		try {
			if (!StafIntegration.fSTAFLogFile.exists()) {
				StafIntegration.fSTAFLogFile.createNewFile();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public STAFResult getSTAFResult() {
		return (StafResult);
	}

	public String getSTAFResponse() {
		return (StafResponse);
	}

	/**
	 * Get the STAF command being used
	 */
	public String getStafCommand() {
		StringBuilder sb = new StringBuilder();
		sb.append("STAF ");
		sb.append(StafServer + " ");
		sb.append(StafService + " ");
		sb.append(StafParms + " ");
		return (sb.toString());
	}

	/**
	 * Get the STAF command being used
	 */
	public String getStafCommand(String Server) {
		StringBuilder sb = new StringBuilder();
		sb.append("STAF ");
		sb.append(Server + " ");
		sb.append(StafService + " ");
		sb.append(StafParms + " ");
		return (sb.toString());
	}

	/**
	 * After using execute(), get the STAF response
	 */
	public String getStafResponse() {
		return (StafResponse);
	}

	/**
	 * Execute the STAF command
	 */
	public boolean execute() throws HarnessException {

		STAFHandle handle = null;

		try {
			handle = new STAFHandle(StafAbstract.class.getName());

	        try {
	        	return runSTAFCommand(StafServer, handle);
	        	
			} finally {
				if (handle != null )
					handle.unRegister();
			}

		} catch (STAFException e) {
        	throw new HarnessException("Error registering or unregistering with STAF, RC: " + e.rc, e);
		}
	}

	/**
	 * Execute the STAF command and add output into harness log file
	 */
	private boolean runSTAFCommand(String StafServer, STAFHandle handle) throws HarnessException {

		logger.info("STAF Command: " + getStafCommand(StafServer));

		try {
			Files.write(StafIntegration.pSTAFLogFilePath, Arrays.asList("STAF Command: " + getStafCommand(StafServer) + "\n"),
					Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}

        StafResult = handle.submit2(StafServer, StafService, StafParms);

        if (StafResult == null)
        	throw new HarnessException("StafResult was null");

    	logger.info("STAF Response Code: "+ StafResult.rc);

    	if (StafResult.rc == STAFResult.AccessDenied) {
    		logger.error("On the server, use: staf local trust set machine *.eng.synacor.com level 5");
    	}

    	if (StafResult.rc != STAFResult.Ok) {
    		throw new HarnessException("Invalid STAF response code ("+ StafResult.rc +"): "+ StafResult.result);
    	}

        if (StafResult.result != null && !StafResult.result.trim().equals("")) {
        	logger.debug(StafResult.result);

        	if ( STAFMarshallingContext.isMarshalledData(StafResult.result) ) {
        		STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(StafResult.result);

        		// Get the entire response
        		StafResponse = STAFMarshallingContext.formatObject(mc);

        	} else {
        		StafResponse = StafResult.result;
        	}
        }

        try {
        	if (StafResult.rc == STAFResult.Ok) {
        		logger.info("STAF Response (success): " + StafResponse);
				Files.write(StafIntegration.pSTAFLogFilePath, Arrays.asList("STAF Response (success): " + StafResponse + "\n"),
						Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        	} else {
        		logger.info("STAF Response (failed): " + StafResponse);
				Files.write(StafIntegration.pSTAFLogFilePath, Arrays.asList("STAF Response: (failed): " + StafResponse + "\n"),
						Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}

        return (StafResult.rc == STAFResult.Ok);

	}
}
