package com.synacor.qa.selenium.framework.core;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ClientSessionFactory {

	private static Logger logger = LogManager.getLogger(ClientSessionFactory.class);

	public static ClientSession session() {
		long threadID = Thread.currentThread().getId();
		ClientSession session = getSingleton().getClientSession(Long.toString(threadID));
		logger.debug("Factory found session: " + session.toString());
		return (session);
	}

	private volatile static ClientSessionFactory singleton;

	private static ClientSessionFactory getSingleton() {
		if (singleton == null) {
			synchronized (ClientSessionFactory.class) {
				if (singleton == null)
					singleton = new ClientSessionFactory();
			}
		}
		return (singleton);
	}

	private Map<String, ClientSession> SessionMap = null;

	private ClientSessionFactory() {
		logger.info("New ClientSessionFactory");
		SessionMap = new HashMap<String, ClientSession>();
	}

	private ClientSession getClientSession(String threadID) {
		if (SessionMap.containsKey(threadID)) {
			return (SessionMap.get(threadID));
		}
		ClientSession session = new ClientSession();
		SessionMap.put(threadID, session);
		return (session);
	}
}
