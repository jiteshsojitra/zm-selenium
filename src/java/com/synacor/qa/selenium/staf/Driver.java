package com.synacor.qa.selenium.staf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.synacor.qa.selenium.framework.core.ExecuteHarnessMain;
import com.synacor.qa.selenium.framework.util.HarnessException;

public class Driver {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws FileNotFoundException, IOException, HarnessException {

        // Create the execution object
        ExecuteHarnessMain harness = new ExecuteHarnessMain();
        
        harness.jarfilename = "jarfile.jar";
        harness.classfilter = "projects.ajax.tests";
        harness.groups = new ArrayList<String>(Arrays.asList("always", "sanity"));
        
        // Execute!
		String response = harness.execute();		
		System.out.println(response);
	}
}