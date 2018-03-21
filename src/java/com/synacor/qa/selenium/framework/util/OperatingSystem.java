package com.synacor.qa.selenium.framework.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class OperatingSystem {
	private static Logger logger = LogManager.getLogger(OperatingSystem.class);

	public static boolean isWindows() {
		return (OperatingSystem.getSingleton().os.startsWith("windows"));
	}
	
	public static boolean isWindows10() {
		return (OperatingSystem.getSingleton().os.startsWith("windows 10"));
	}

	public static boolean isLinux() {
		return (OperatingSystem.getSingleton().os.startsWith("linux"));
	}

	public static boolean isMac() {
		return (OperatingSystem.getSingleton().os.startsWith("mac"));
	}

	public enum OsType {
		WINDOWS, WINDOWS10, LINUX, MAC
	}

	public enum OsArch {
		X86, X64
	}

	public static OsArch getOsArch() {
		String osArch = System.getProperty("os.arch").toLowerCase();
		logger.info("os.arch is: " + osArch);
		if (osArch.equals("x86") || osArch.equals("i386")) {
			return OsArch.X86;
		} else {
			return OsArch.X64;
		}
	}

	public static OsType getOSType() {
		logger.info("os.name is: " + getSingleton().os);
		OsType osType = null;
		if (isWindows()) {
			osType = OsType.WINDOWS;
		} else if (isWindows10()) {
			osType = OsType.WINDOWS10;
		} else if (isMac()) {
			osType = OsType.MAC;
		} else if (isLinux()) {
			osType = OsType.LINUX;
		}
		return osType;
	}

	private String os = null;

	private volatile static OperatingSystem singleton;

	private OperatingSystem() {
		os = System.getProperty("os.name").toLowerCase();
		logger.info("Operating System: " + os);
	}

	private static OperatingSystem getSingleton() {
		if (singleton == null) {
			synchronized (OperatingSystem.class) {
				if (singleton == null) {
					singleton = new OperatingSystem();
				}
			}
		}
		return singleton;
	}
}
