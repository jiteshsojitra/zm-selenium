/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013 Zimbra Software, LLC.
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
package com.zimbra.qa.selenium.framework.util.performance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.OperatingSystem;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;


/**


mysql> create table apps ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 name VARCHAR(256) 
 );
mysql> insert into apps (name) VALUES ('AJAX');
mysql> insert into apps (name) VALUES ('HTML');
mysql> insert into apps (name) VALUES ('MOBILE');
mysql> insert into apps (name) VALUES ('ADMIN');
mysql> insert into apps (name) VALUES ('DESKTOP');
mysql> insert into apps (name) VALUES ('OCTOPUS');

mysql> create table actions ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 name VARCHAR(256) 
 );
mysql> insert into actions (name) VALUES ('ZmMailApp');

mysql> create table builds ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 build VARCHAR(256) 
 );

mysql> create table milestones ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 milestone VARCHAR(256) 
 );
mysql> insert into milestones (milestone) VALUES ('GunsNRoses');
mysql> insert into milestones (milestone) VALUES ('Helix');
mysql> insert into milestones (milestone) VALUES ('IronMaiden');
mysql> insert into milestones (milestone) VALUES ('JudasPriest');


mysql> create table browsers ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 name VARCHAR(512) 
 );

mysql> create table clients ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 name VARCHAR(256) 
 );

mysql> create table messages ( 
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 name VARCHAR(1024) 
 );

mysql> create table perf (
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 created TIMESTAMP(8),
 name VARCHAR(35),
 appid INT,
 buildid INT,
 browserid INT,
 clientid INT,
 milestoneid INT,
 start BIGINT,
 launched BIGINT,
 loaded BIGINT,
 delta BIGINT,
 delta_internal BIGINT,
 messageid INT
 );

 **/


public class PerfDatabase {
	protected static Logger logger = LogManager.getLogger(PerfDatabase.class);

	/**
	 * Insert a PerfData values into the database
	 * @param data
	 * @throws HarnessException
	 */
	public static void record(PerfData data) throws HarnessException {

		// Make sure the action exists
		PerfDatabase.getInstance().getActionKey(data.Key.toString());

		// A mapping of column names to values
		HashMap<String, String> table = new HashMap<String, String>();

		table.put("name", "'" + data.Key.toString() +"'");		// VARCHAR ... enclose in single quotes
		table.put("appid", "" + PerfDatabase.getInstance().getAppType());
		table.put("buildid", "" + PerfDatabase.getInstance().getBuildID());
		table.put("milestoneid", "" + PerfDatabase.getInstance().getMilestoneID());
		table.put("browserid", "" + PerfDatabase.getInstance().getBrowserID());
		table.put("clientid", "" + PerfDatabase.getInstance().getClientID());
		table.put("messageid", "" + PerfDatabase.getInstance().getMessageKey(data.Message));

		table.put("start", "" + data.StartStamp);
		table.put("launched", "" + data.LaunchStamp);
		table.put("loaded", "" + data.FinishStamp);

		table.put("delta", "" + "" + (Long.parseLong(data.FinishStamp) - data.StartStamp));

		String delta_internal = "0";
		if ( data.LaunchStamp != null && !data.LaunchStamp.trim().equals("") ) {
			delta_internal = "" + (Long.parseLong(data.FinishStamp) - Long.parseLong(data.LaunchStamp));
		}
		table.put("delta_internal", delta_internal);

		// Insert the map into the database
		PerfDatabase.getInstance().insertPerf(table);

	}

	protected void insertPerf(HashMap<String, String> data) throws HarnessException {

		// Convert the keys, values into comma separated strings
		String columns = Arrays.asList(data.keySet().toArray()).toString().replace("[", "").replace("]", "");
		String values = Arrays.asList(data.values().toArray()).toString().replace("[", "").replace("]", "");

		String command = String.format("INSERT INTO perf (%s) VALUES (%s)", columns, values);
		logger.info("Statement: "+ command);

		try {

			Statement statement = DatabaseConnection.getInstance().createStatement();
			int ret = statement.executeUpdate(command);
			logger.info("Statement: ret="+ ret);


		} catch (SQLException e) {
			throw new HarnessException(command, e);
		}


	}

		
	/**
	 * Get the ID corresponding to the app from the perf DB
	 * @return
	 * @throws HarnessException
	 */
	protected int getAppType() throws HarnessException {
		getAppTypeTable();

		String type = ZimbraSeleniumProperties.getAppType().toString().toLowerCase();
		if (!appTable.containsKey(type))
			throw new HarnessException("Unknown app type: "+ type +".  Maybe INSERT INTO apps ('name') VALUES ('"+ type +"') ?");

		return (appTable.get(type));
	}

	protected static HashMap<String, Integer> appTable = null;
	protected void getAppTypeTable() throws HarnessException {
		if ( appTable == null ) {
			appTable = new HashMap<String, Integer>();

			String query = "SELECT id, name FROM apps";

			try {

				Statement statement = DatabaseConnection.getInstance().createStatement();
				ResultSet rs = statement.executeQuery(query);
				while (rs.next()) {

					Integer id = rs.getInt("id");
					String name = rs.getString("name").toLowerCase();

					logger.info("getAppTypeTable(): id="+ id +" name="+ name);

					appTable.put(name, id);

				}

			} catch (SQLException e) {
				throw new HarnessException(query, e);
			}


		}
	}

	/**
	 * Get the ID corresponding to the Build from the perf DB
	 * @return
	 * @throws HarnessException
	 */
	protected int getBuildID() throws HarnessException {
		getBuildTable();

		String build = getVersionString();
		if (!buildTable.containsKey(build)) {
			insertBuild(build);
		}

		return (buildTable.get(build));
	}

	protected static HashMap<String, Integer> buildTable = null;
	protected void getBuildTable() throws HarnessException {
		if ( buildTable == null ) {
			buildTable = new HashMap<String, Integer>();

			String query = "SELECT id, build FROM builds";

			try {

				Statement statement = DatabaseConnection.getInstance().createStatement();
				ResultSet rs = statement.executeQuery(query);
				while (rs.next()) {

					Integer id = rs.getInt("id");
					String build = rs.getString("build");

					logger.info("getBuildTable(): id="+ id +" build="+ build);

					buildTable.put(build, id);

				}

			} catch (SQLException e) {
				throw new HarnessException(query, e);
			}


		}
	}
	protected void insertBuild(String build) throws HarnessException {

		if ( buildTable.containsKey(build) )
			throw new HarnessException("buildTable already contains "+ build);

		String command = String.format("INSERT INTO builds (build) VALUES ('%s')", build);
		logger.info("Statement: "+ command);

		try {

			Statement statement = DatabaseConnection.getInstance().createStatement();
			int ret = statement.executeUpdate(command);
			logger.info("Statement: ret="+ ret);


		} catch (SQLException e) {
			throw new HarnessException(command, e);
		}

		// Reset the action table to pick up the new ID
		buildTable = null;
		getBuildTable();

	}


	/**
	 * Get the ID corresponding to the browser from the perf DB
	 * @return
	 * @throws HarnessException
	 */
	protected int getBrowserID() throws HarnessException {
		getBrowserTable();
		
		String browser = ZimbraSeleniumProperties.getCalculatedBrowser();

		if (!browserTable.containsKey(browser)) {
			insertBrowser(browser);
		}

		return (browserTable.get(browser));
	}

	protected static HashMap<String, Integer> browserTable = null;
	protected void getBrowserTable() throws HarnessException {
		if ( browserTable == null ) {
			browserTable = new HashMap<String, Integer>();

			String query = "SELECT id, name FROM browsers";

			try {

				Statement statement = DatabaseConnection.getInstance().createStatement();
				ResultSet rs = statement.executeQuery(query);
				while (rs.next()) {

					Integer id = rs.getInt("id");
					String name = rs.getString("name");

					logger.info("getBrowserTable(): id="+ id +" name="+ name);

					browserTable.put(name, id);

				}

			} catch (SQLException e) {
				throw new HarnessException(query, e);
			}


		}
	}
	protected void insertBrowser(String browser) throws HarnessException {

		if ( browserTable.containsKey(browser) )
			throw new HarnessException("browserTable already contains "+ browser);

		String command = String.format("INSERT INTO browsers (name) VALUES ('%s')", browser);
		logger.info("Statement: "+ command);

		try {

			Statement statement = DatabaseConnection.getInstance().createStatement();
			int ret = statement.executeUpdate(command);
			logger.info("Statement: ret="+ ret);


		} catch (SQLException e) {
			throw new HarnessException(command, e);
		}

		// Reset the action table to pick up the new ID
		browserTable = null;
		getBrowserTable();

	}

	/**
	 * Get the ID corresponding to the client OS from the perf DB
	 * @return
	 * @throws HarnessException
	 */
	protected int getClientID() throws HarnessException {
		getClientTable();
		
		String os = OperatingSystem.getOSType().toString();

		if (!clientTable.containsKey(os)) {
			insertClient(os);
		}

		return (clientTable.get(os));
	}

	protected static HashMap<String, Integer> clientTable = null;
	protected void getClientTable() throws HarnessException {
		if ( clientTable == null ) {
			clientTable = new HashMap<String, Integer>();

			String query = "SELECT id, name FROM clients";

			try {

				Statement statement = DatabaseConnection.getInstance().createStatement();
				ResultSet rs = statement.executeQuery(query);
				while (rs.next()) {

					Integer id = rs.getInt("id");
					String name = rs.getString("name");

					logger.info("getClientTable(): id="+ id +" name="+ name);

					clientTable.put(name, id);

				}

			} catch (SQLException e) {
				throw new HarnessException(query, e);
			}


		}
	}
	protected void insertClient(String os) throws HarnessException {

		if ( clientTable.containsKey(os) )
			throw new HarnessException("clientTable already contains "+ os);

		String command = String.format("INSERT INTO clients (name) VALUES ('%s')", os);
		logger.info("Statement: "+ command);

		try {

			Statement statement = DatabaseConnection.getInstance().createStatement();
			int ret = statement.executeUpdate(command);
			logger.info("Statement: ret="+ ret);


		} catch (SQLException e) {
			throw new HarnessException(command, e);
		}

		// Reset the action table to pick up the new ID
		clientTable = null;
		getClientTable();

	}

	/**
	 * Get the ID corresponding to the key from the perf DB
	 * @return
	 * @throws HarnessException
	 */
	protected int getActionKey(String action) throws HarnessException {
		getActionTable();

		if (!actionTable.containsKey(action)) {
			insertAction(action);
		}

		return (actionTable.get(action));
	}

	protected static HashMap<String, Integer> actionTable = null;
	protected void getActionTable() throws HarnessException {
		if ( actionTable == null ) {
			actionTable = new HashMap<String, Integer>();

			String query = "SELECT id, name FROM actions";

			try {


				Statement statement = DatabaseConnection.getInstance().createStatement();
				ResultSet rs = statement.executeQuery(query);
				while (rs.next()) {

					Integer id = rs.getInt("id");
					String name = rs.getString("name");

					logger.info("getActionTable(): id="+ id +" name="+ name);

					actionTable.put(name, id);

				}

			} catch (SQLException e) {
				throw new HarnessException(query, e);
			}


		}
	}
	protected void insertAction(String action) throws HarnessException {

		if ( actionTable.containsKey(action) )
			throw new HarnessException("actionTable already contains "+ action);

		String command = String.format("INSERT INTO actions (name) VALUES ('%s')", action);
		logger.info("Statement: "+ command);

		try {

			Statement statement = DatabaseConnection.getInstance().createStatement();
			int ret = statement.executeUpdate(command);
			logger.info("Statement: ret="+ ret);


		} catch (SQLException e) {
			throw new HarnessException(command, e);
		}

		// Reset the action table to pick up the new ID
		actionTable = null;
		getActionTable();

	}

	/**
	 * Get the Milestone (string) corresponding to the version number from the perf DB
	 * @return
	 * @throws HarnessException
	 */
	protected int getMilestoneID() throws HarnessException {
		getMilestoneTable();

		if ( getVersionString().startsWith("6") ) {
			return (milestoneTable.get("GunsNRoses"));
		}
		if ( getVersionString().startsWith("7") ) {
			return (milestoneTable.get("Helix"));
		}
		if ( getVersionString().startsWith("8") ) {
			return (milestoneTable.get("IronMaiden"));
		}
		if ( getVersionString().startsWith("9") ) {
			return (milestoneTable.get("JudasPriest"));
		}

		throw new HarnessException("Unable to determine Milestone from version string: "+ getVersionString());

	}
	
	protected static HashMap<String, Integer> milestoneTable = null;
	protected void getMilestoneTable() throws HarnessException {
		if ( milestoneTable == null ) {
			milestoneTable = new HashMap<String, Integer>();

			String query = "SELECT id, milestone FROM milestones";

			try {

				Statement statement = DatabaseConnection.getInstance().createStatement();
				ResultSet rs = statement.executeQuery(query);
				while (rs.next()) {

					Integer id = rs.getInt("id");
					String milestone = rs.getString("milestone");

					logger.info("getActionTable(): id="+ id +" milestone="+ milestone);

					milestoneTable.put(milestone, id);

				}

			} catch (SQLException e) {
				throw new HarnessException(query, e);
			}


		}
	}


	protected String versionString = null;
	protected String getVersionString() throws HarnessException {
		if ( versionString == null ) {
			ZimbraAdminAccount.GlobalAdmin().soapSend("<GetVersionInfoRequest xmlns='urn:zimbraAdmin'/>");
			Element GetVersionInfo = ZimbraAdminAccount.GlobalAdmin().soapSelectNode("//admin:GetVersionInfoResponse//admin:info", 1);
			versionString = String.format("%s.%s.%s",
					GetVersionInfo.getAttribute("majorversion", "X"),
					GetVersionInfo.getAttribute("minorversion", "X"),
					GetVersionInfo.getAttribute("microversion", "X"));
		}
		return (versionString);
	}

	/**
	 * Get the ID corresponding to the key from the perf DB
	 * @return
	 * @throws HarnessException
	 */
	protected int getMessageKey(String description) throws HarnessException {
		getMessageTable();

		if (!messageTable.containsKey(description)) {
			insertMessage(description);
		}

		return (messageTable.get(description));
	}

	protected static HashMap<String, Integer> messageTable = null;
	protected void getMessageTable() throws HarnessException {
		if ( messageTable == null ) {
			messageTable = new HashMap<String, Integer>();

			String query = "SELECT id, name FROM messages";

			try {

				Statement statement = DatabaseConnection.getInstance().createStatement();
				ResultSet rs = statement.executeQuery(query);
				while (rs.next()) {

					Integer id = rs.getInt("id");
					String name = rs.getString("name");

					logger.info("getMessageTable(): id="+ id +" name="+ name);

					messageTable.put(name, id);

				}

			} catch (SQLException e) {
				throw new HarnessException(query, e);
			}


		}
	}
	protected void insertMessage(String description) throws HarnessException {

		if ( messageTable.containsKey(description) )
			throw new HarnessException("messageTable already contains "+ description);

		String command = String.format("INSERT INTO messages (name) VALUES ('%s')", description);
		logger.info("Statement: "+ command);

		try {

			Statement statement = DatabaseConnection.getInstance().createStatement();
			int ret = statement.executeUpdate(command);
			logger.info("Statement: ret="+ ret);


		} catch (SQLException e) {
			throw new HarnessException(command, e);
		}

		// Reset the description table to pick up the new ID
		messageTable = null;
		getMessageTable();

	}
	
	public static PerfDatabase getInstance() {
		if (Instance == null) {
			synchronized(PerfDatabase.class) {
				if ( Instance == null) {
					Instance = new PerfDatabase();
				}
			}
		}
		return (Instance);
	}


	protected volatile static PerfDatabase Instance;

	protected PerfDatabase() {	
		logger.info("New "+ this.getClass().getCanonicalName());			
	}

	protected static class DatabaseConnection {
		protected static Logger logger = LogManager.getLogger(DatabaseConnection.class);


		public static final String dbDefaultURL = "jdbc:mysql://10.20.140.198/perf";
		public static final String dbDefaultUsername = "perf";
		public static final String dbDefaultUserpass = "perf";


		public Connection getConnection() throws SQLException {

			if ( conn == null ) {

				conn = DriverManager.getConnection(dbURL, dbUsername, dbUserpass);
				logger.info("Database connection established: "+ dbURL);

			}

			return (conn);
		}

		public Statement createStatement() throws SQLException {

			return (getConnection().createStatement());

		}

		public static DatabaseConnection getInstance() throws HarnessException {
			if (Instance == null) {
				synchronized(DatabaseConnection.class) {
					if ( Instance == null) {
						Instance = new DatabaseConnection();
					}
				}
			}
			return (Instance);
		}


		protected volatile static DatabaseConnection Instance;
		protected Connection conn = null;
		protected String dbURL = null;
		protected String dbUsername = null;
		protected String dbUserpass = null;

		protected DatabaseConnection() throws HarnessException {	
			logger.info("New "+ this.getClass().getCanonicalName());			

			try {

				Class.forName("com.mysql.jdbc.Driver");
				
				dbURL = ZimbraSeleniumProperties.getStringProperty("performance.metrics.db.url", dbDefaultURL);
				dbUsername = ZimbraSeleniumProperties.getStringProperty("performance.metrics.db.username", dbDefaultUsername);
				dbUserpass = ZimbraSeleniumProperties.getStringProperty("performance.metrics.db.userpass", dbDefaultUserpass);

			} catch (ClassNotFoundException e) {
				throw new HarnessException(e);
			}


		}


	}


}
