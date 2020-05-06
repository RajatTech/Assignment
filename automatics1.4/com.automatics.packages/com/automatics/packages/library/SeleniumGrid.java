package com.automatics.packages.library;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class SeleniumGrid {
	
	private static String sSeleniumJar = "selenium-server-standalone-3.8.1.jar";
	private static String sPackageFldr;
	
	
	public static void start() throws Exception {
	
		try {
			sPackageFldr = Utils.hSystemSettings.get("packageFolder");
			gridStartHub();
			gridStartFirefox();
			gridStartChrome();
			gridStartIE();
		} catch (Exception e) {
			Logs.log("PreRequisite", "Could Not Start Grid" + e.getMessage());
		}
	}
	
	private static void gridStartHub() throws IOException, InterruptedException {
		
		Logs.log("PreRequisite"); Logs.log("PreRequisite", "-----Selenium Grid Hub Response----");

		if (getHTTPResponseStatusCode("http://localhost:4444/wd/status") == 404 ) {
			List<String> cmd= Arrays.asList("cmd.exe", "/k", "cd \""  + sPackageFldr + "\" && " + sPackageFldr.split("\\\\")[0] + " && "
					+ "javaw -jar " + sSeleniumJar + " -port 4444 -role hub");
			Utils.Helper.executeRunCommand(cmd, true);
			Thread.sleep(5000);
			
			if (getHTTPResponseStatusCode("http://localhost:4444/wd/status") == 404 ) 
				Logs.log("PreRequisite", "Selenium Grid not  Started");
			else
				Logs.log("PreRequisite", "Selenium Grid Started");
		} else
			Logs.log("PreRequisite", "Found Already Started - http://localhost:4444/wd/status");
	}
	
	private static void gridStartFirefox() throws Exception {
		
		Logs.log("PreRequisite"); Logs.log("PreRequisite", "-----Firefox Grid Response----");

		if (getHTTPResponseStatusCode("http://localhost:5555/wd/hub/status") == 404 ) {
			List<String> cmd= Arrays.asList("cmd.exe", "/k","cd \"" + sPackageFldr + "\" && " + sPackageFldr.split("\\\\")[0] + " && "
					+ "javaw -jar " + sSeleniumJar 
					+ " -role node -hub http://localhost:4444/grid/register -browser browserName=firefox,maxInstances=5 -port 5555");
			Utils.Helper.executeRunCommand(cmd, true);
			
			Thread.sleep(5000);
			
			if (getHTTPResponseStatusCode("http://localhost:5555/wd/hub/status") == 404 ) 
					Logs.log("PreRequisite", "Firefox Grid not Started");
			else
				Logs.log("PreRequisite", "Firefox Grid Started");
			
		} else
			Logs.log("PreRequisite", "Found Already Started - http://localhost:5555/wd/hub/status");
	}
	
	private static void gridStartChrome() throws Exception {
		
		Logs.log("PreRequisite"); Logs.log("PreRequisite", "-----Chrome Grid Response----");
		
		if (getHTTPResponseStatusCode("http://localhost:5559/wd/hub/status") == 404 ) {
			List<String> cmd= Arrays.asList("cmd.exe", "/k","cd \"" + sPackageFldr + "\" && " + sPackageFldr.split("\\\\")[0] + " && "
					+ "javaw -Dwebdriver.chrome.driver=chromedriver.exe -jar  " + sSeleniumJar 
					+ " -role webdriver -hub http://localhost:4444/grid/register -browser browserName=chrome,platform=WINDOWS,maxInstances=5 -port 5559");
			Utils.Helper.executeRunCommand(cmd, true);
			Thread.sleep(5000);
			
			if (getHTTPResponseStatusCode("http://localhost:5559/wd/hub/status") == 404 ) 
					Logs.log("PreRequisite", "Chrome Grid not  Started");
			else
				Logs.log("PreRequisite", "Chrome Grid Started");
		} else
			Logs.log("PreRequisite", "Found Already Started - http://localhost:5559/wd/hub/status");
	}
	
	private static void gridStartIE() throws Exception {
		
		Logs.log("PreRequisite"); Logs.log("PreRequisite", "-----IE Grid Response----");
		
		if (getHTTPResponseStatusCode("http://localhost:5558/wd/hub/status") == 404 ) {
			
			if (isWindow64()) {
				List<String> cmd= Arrays.asList("cmd.exe", "/k","cd \"" + sPackageFldr + "\" && " + sPackageFldr.split("\\\\")[0] + " && "
						+ "javaw -Dwebdriver.ie.driver=IEDriverServer_64.exe -jar " + sSeleniumJar
						+ " -role webdriver -hub http://localhost:4444/grid/register "
						+ "-browser browserName=\"internet explorer\",platform=WINDOWS,maxInstances=5 -port 5558");
				Utils.Helper.executeRunCommand(cmd,true);
			} else {
				List<String> cmd= Arrays.asList("cmd.exe", "/k" ,"cd \"" + sPackageFldr + "\" && " + sPackageFldr.split("\\\\")[0] + " && "
					+ "javaw -Dwebdriver.ie.driver=IEDriverServer_32.exe -jar " + sSeleniumJar
					+ " -role webdriver -hub http://localhost:4444/grid/register "
					+ "-browser browserName=\"internet explorer\",platform=WINDOWS,maxInstances=5 -port 5558");
				Utils.Helper.executeRunCommand(cmd, true);
			}
			Thread.sleep(5000);
			
			if (getHTTPResponseStatusCode("http://localhost:5558/wd/hub/status") == 404 ) 
					Logs.log("PreRequisite", "IE Grid not  Started");
			else
				Logs.log("PreRequisite", "IE Grid Started");
		} else
			Logs.log("PreRequisite", "Found Already Started - http://localhost:5558/wd/hub/status");
	}
	
	private static int getHTTPResponseStatusCode(String u) throws IOException {
		
		try {
			URL url = new URL(u);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			return http.getResponseCode();
		} catch (Exception e) {
			return 404;
		}
    }
	
	public static boolean isWindow64() {
		boolean is64bit = false;
		if (System.getProperty("os.name").contains("Windows")) {
		    is64bit = (System.getenv("ProgramFiles(x86)") != null);
		} else {
		    is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
		}
		return is64bit;
	}
}
