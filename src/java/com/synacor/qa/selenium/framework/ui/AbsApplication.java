package com.synacor.qa.selenium.framework.ui;

import java.util.*;
import org.apache.log4j.*;
import com.synacor.qa.selenium.framework.util.*;

public abstract class AbsApplication {
	
	protected static Logger logger = LogManager.getLogger(AbsApplication.class);
	
	protected Map<String, AbsTab> pages = new HashMap<String, AbsTab>();

	protected AbsApplication() {
		logger.info("new " + AbsApplication.class.getCanonicalName());
	}
	
	public abstract String myApplicationName();

	public abstract boolean zIsLoaded() throws HarnessException;

	public List<AbsTab> zGetActivePages() throws HarnessException {
		List<AbsTab> actives = new ArrayList<AbsTab>();
		for (AbsTab p : pages.values()) {
			if ( p.zIsActive() ) {
				actives.add(p);
			}
		}
		return (actives);
	}
}