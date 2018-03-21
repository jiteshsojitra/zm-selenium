package com.synacor.qa.selenium.framework.util;

import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SleepUtil {
	
	private static Logger logger = LogManager.getLogger(SleepUtil.class);
	public static int SleepGranularity = 1000;

	public static void sleep(long millis) {

		Date start = new Date();

		try {

			long target = millis;
			long total = 0;

			try {

				while (millis > 0) {

					if ( millis >= SleepGranularity) {
						logger.info("Sleep: "+ SleepGranularity +" milliseconds ... ("+ total +"/"+ target +")");
						Thread.sleep(SleepGranularity);
						total += SleepGranularity;
					} else {
						logger.info("Sleep: "+ millis +" milliseconds ... ("+ millis +"/"+ target +")");
						Thread.sleep(millis);
						total += millis;
					}

					millis -= SleepGranularity;
				}

			} catch (InterruptedException e) {
				logger.warn("Sleep was interuppted", e);
			}

		} finally {
			SleepMetrics.RecordSleep((new Throwable()).getStackTrace(), millis, start, new Date());
		}
	}

	// Sleep for half sec
	public static void sleepVerySmall() {
		sleep(ConfigProperties.getIntProperty("very_small_wait", 500));
	}

	// Sleep for 1sec
	public static void sleepSmall() {
		sleep(ConfigProperties.getIntProperty("small_wait", 1000));
	}

	// Sleep for 2sec
	public static void sleepMedium() {
		sleep(ConfigProperties.getIntProperty("medium_wait", 2000));
	}

	// Sleep for 4sec
	public static void sleepLong() {
		sleep(ConfigProperties.getIntProperty("long_wait", 4000));
	}
	
	// Sleep for 6sec
	public static void sleepLongMedium() {
		sleep(ConfigProperties.getIntProperty("long_medium_wait", 6000));
	}

	// Sleep for 10sec
	public static void sleepVeryLong() {
		sleep(ConfigProperties.getIntProperty("very_long_wait", 10000));
	}
	
	// Sleep for 15sec
	public static void sleepVeryVeryLong() {
		sleep(ConfigProperties.getIntProperty("very_long_wait", 15000));
	}
}