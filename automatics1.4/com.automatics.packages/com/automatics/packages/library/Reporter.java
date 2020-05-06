package com.automatics.packages.library;

import io.appium.java_client.AppiumDriver;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;

import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.itextpdf.text.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;


public class Reporter {
	
	static String sExecutionLogFldr, sExecutionId="";
	static ConcurrentHashMap<String,String> hException_msgs=new ConcurrentHashMap<String,String>();
	static ConcurrentHashMap<String,String> htestrunName=new ConcurrentHashMap<String,String>();
	
	public static void startSuite(ITestContext context) throws Exception {	
		
		try {    
			String dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			TreeMap<String, String> hlocalSuite = new TreeMap<>();
			String Suite_Name = context.getSuite().getName();
			sExecutionLogFldr = Utils.hSystemSettings.get("reportFolder") + "/" + Suite_Name + "_" + dtFormat.replace(":","-"); 
			new File(sExecutionLogFldr).mkdirs();
			
			helper.init_exceptions();
			System.out.println(Suite_Name +" Suite Execeution Started");
			
			dtFormat = dtFormat.replace(" ","_");
			
			if (sExecutionId.isEmpty())
				sExecutionId = dtFormat;
			
			hlocalSuite.put("executionId", sExecutionId);
			hlocalSuite.put("suiteName", Suite_Name );
			
			hlocalSuite.put("testTotal",String.valueOf(context.getCurrentXmlTest().getSuite().getTests().size()));
		} catch (Exception e) {
			Logs.log("PreRequisite","Could Not start Suite Reporting " + e.getMessage());
        	e.printStackTrace(); 
		}
	}
	
	public static void addTestRun(ITestContext testContext, String msg) throws JSONException, ParseException, FileNotFoundException, DocumentException  {
		
		String sTest_FullName = testContext.getName();
		try {
			TreeMap<String, String> hTest_Report = new TreeMap<>();
			String Suite_Name = testContext.getSuite().getName();
			
			String testRunApi=Utils.hConfigSetings.get("API_URL")+"/addTestRun";
			hTest_Report.put("executionId", sExecutionId);
			hTest_Report.put("tcName", sTest_FullName.substring(0, sTest_FullName.lastIndexOf("_")));
			hTest_Report.put("testCount", sTest_FullName.substring(sTest_FullName.lastIndexOf("_") + 1));
			hTest_Report.put("testrunExeType", Utils.h2TestName_TestParams.get(sTest_FullName).get("exeType"));
			hTest_Report.put("testrunExePlatform", Utils.h2TestName_TestParams.get(sTest_FullName).get("exePlatform"));
			hTest_Report.put("testrunParent","");
			hTest_Report.put("projectName",Utils.hConfigSetings.get("PROJECT_NAME"));			
			hTest_Report.put("suiteName",Suite_Name);
			
				
		} catch (Exception ex) {
			
			Logs.log(sTest_FullName,"Could Not start TestCase Reporting " + ex.getMessage());
        	ex.printStackTrace();
	    }
	}
	
	public static void print(String sTest_FullName, String sHtmlMsg) throws HeadlessException, IOException, AWTException {
	
		print(sTest_FullName, sHtmlMsg, "");
	}
	
	public static void print(String sTest_FullName, String sHtmlMsg, String ScreenShot) throws HeadlessException, IOException, AWTException {
		
		try {
			
			Logs.log(sTest_FullName, sHtmlMsg);
		} catch (Exception ex) {
	    
			Logs.log(sTest_FullName,"Could Not Add TestCase Step to DB " + ex.getMessage());
	        ex.printStackTrace();
	    }	
	}

	
	public static void print(String sTest_FullName, String sUsrVal, String sDesc, String sExpVal, String sActVal, boolean bStatus) throws HeadlessException, IOException, AWTException {
		print(sTest_FullName, sUsrVal, sDesc, sExpVal, sActVal, bStatus, "");
	}
	public static void print(String sTest_FullName, String sUsrVal, String sDesc, String sExpVal, String sActVal, boolean bStatus, String ScreenShot) throws HeadlessException, IOException, AWTException {

		boolean bExitOnFail = false; String sHtmlMsg = null, computedStatus = "Fail";
		bExitOnFail = sUsrVal.contains("*EXIT_ON_FAIL*");
		try {		
			sHtmlMsg = sDesc;
			if (sUsrVal.contains("*NOT*")) { 
				bStatus = (!bStatus); 
				sHtmlMsg += " - NEGATIVE (NOT)"; 
			}
			
			if (bStatus)
				computedStatus = "Pass";
					
			Logs.log(sTest_FullName, sHtmlMsg + " -- "+ String.valueOf(bStatus) + " -- [Expected - " + 
															sExpVal + ", Actual - " + sActVal + "]");			
	    
		} catch (Exception ex) {
	    	
			Logs.log(sTest_FullName,"Could Not Add TestCase Step to DB " + ex.getMessage());
	        ex.printStackTrace();
	    }	
		
		if ((bExitOnFail) && (!bStatus)) {Assert.fail(sHtmlMsg);}
	}

	public static void printError(String sTest_FullName, Exception e, String sDesc) throws HeadlessException, IOException, AWTException {
		printError(sTest_FullName, e, sDesc, "");
	}
	
	public static void printError(String sTest_FullName, Exception e, String sDesc, String Screenshot) throws HeadlessException, IOException, AWTException {

		boolean bExitOnFail = true, bStatus = false; String sHtmlMsg = "" , sLogMsg;
		try {
			
			sLogMsg = sDesc + "\nError: " + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e);
			for (int i = 0; i<3 ;i++)
				sHtmlMsg += sLogMsg.split("\n")[i];	
			sHtmlMsg = sDesc + "\nError: " + sHtmlMsg;
			
			Logs.log(sTest_FullName, sLogMsg + " -- "+ String.valueOf(bStatus) + " -- [Expected - " + "True" + ", Actual - " + "False" + "]");
							
	    } catch (Exception ex) {
	    	
	    	Logs.log(sTest_FullName,"Could Not Add TestCase Step to DB " + ex.getMessage());
	        ex.printStackTrace();
	    }	
		
		if ((bExitOnFail)) {Assert.fail(sHtmlMsg);}
	}
	
	public static String filterUserInput(String sUserVal) {
		return sUserVal.replace("*EXIT_ON_FAIL*", "").replace("*NOT*", "");
	}
	
		
	public static void printBDD ( String sTestName, String sVal, String sDesc) throws HeadlessException, IOException, AWTException {
		sDesc = "^^BLUE" + sVal + "--" + sDesc.split("\\^")[0] + "--" + sDesc.split("\\>>")[1];
		print(sTestName,sDesc);
	}
	
	@SuppressWarnings("rawtypes")
	private static String get_error(String sTestName, String Exception) throws FileNotFoundException {	
		try {
			int cnt=0;	String key =null;
			Iterator iterator = hException_msgs.keySet().iterator();	
		
			while (iterator.hasNext()) {
		      key = (String) iterator.next();
		      if (Exception.contains(key)) {
		    	  Logs.log(sTestName, hException_msgs.get(key));
		    	  cnt++;
		    	  break;
		      }
			}
	
			if (cnt==0)
				return "";
			else
				return hException_msgs.get(key);
		} catch (Exception e) {
			throw e;
		}
	}
	public static void updateSuiteRun(ITestContext testContext) throws Exception{	

		TreeMap<String,Object> hSuiteInfo = new TreeMap<>();
		
		try {
			String apiUrl = Utils.hConfigSetings.get("API_URL") + "/updateSuiteRun";
			hSuiteInfo.put("executionId", sExecutionId);
			hSuiteInfo.put("suiteName", testContext.getSuite().getName());
			hSuiteInfo.put("testrunName", htestrunName.get(testContext.getName()));
			//helper.postJSONToAPI("PreRequisite", apiUrl, new JSONObject(hSuiteInfo));
			Logs.log("PreRequisite","Logs:  " + Reporter.sExecutionLogFldr);
			closeBrowser(testContext.getName());
		} catch (Exception e) {
			
			Logs.log("PreRequisite","Could Not close Suite run " + e.getMessage());
	        e.printStackTrace();
		}
	
	}
	
	private static void closeBrowser(String sTestName){
		try{
			Logs.log(sTestName,"Closing Browser after testcase");
			WebDriver lDriver=(WebDriver) Utils.getObjectDriver(sTestName);
			lDriver.quit();
			Logs.log(sTestName,"Browser Closed");
		} catch (java.lang.NullPointerException ex) {
			try {
			Logs.log(sTestName,"Could Not close Browser");
			}
			catch(Exception e2){
			}
			
			}
		catch(Exception e) {
			try {
				Logs.log(sTestName,"Could Not close Browser");
			}
			catch(Exception e2){
			}
		}
	}
	
	private static class helper{
		
		public static String postJSONToAPI(String sTest_FullName, String apiUrl, JSONObject postObj) throws FileNotFoundException {
			
			String returned_msg="";
			try
			{
				URL post_url = new URL(apiUrl);
				HttpURLConnection con = (HttpURLConnection) post_url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", "Mozilla/5.0");
				con.setRequestProperty("Content-Type","application/json");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(postObj.toString());
				wr.flush();
				wr.close();
				
				BufferedReader br = null;
				if(con.getResponseCode()==200) {
					String temp_str = "", str = "";
					br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					while((temp_str = br.readLine())!=null)
					{
						str = str + temp_str;
					}
					returned_msg = str;
					Logs.log(sTest_FullName,"Report Data posted Successfully");
				}
				else {
					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					String temp_str = "", str = "";
					while((temp_str = br.readLine())!=null)
					{
						str = temp_str;
					}
					returned_msg = str;
					Logs.log(sTest_FullName,"[AutomaticsAPI - postJSONToAPI()] - Exception : " +str);
				}
			}
			catch(Exception e)
			{
				Logs.log(sTest_FullName,"[AutomaticsAPI - postJSONToAPI()] - Exception : " +e.getMessage());
			}
			return returned_msg;
		}	
		
		@SuppressWarnings("rawtypes")
		public static String check_failureType(String sTest_FullName,String sLogMsg) throws FileNotFoundException {
			
			String title = "";
			try {
					WebDriver driver=(WebDriver)Utils.getObjectDriver(sTest_FullName);
					title=driver.getTitle();
			}
			catch(Exception e1) {
				try {
					AppiumDriver aDriver=(AppiumDriver)Utils.getObjectDriver(sTest_FullName);
					title=aDriver.getTitle();
				}
				catch(Exception e) { 
				}
			}
			if (title.equalsIgnoreCase("Unknown Host")) {
				return "Unknown Host URL";
			}
			else if (title.equalsIgnoreCase("Problem Loading Page")) {
				return "Problem Loading Page";
			}
			else if (title.equalsIgnoreCase("Service Unavailable")) {
				return "URL Service Unavailable";
			}			
			else {
				return get_error(sTest_FullName,sLogMsg);	
			}			
		}
			
		
		public static void init_exceptions()
		{
			hException_msgs.put("org.openqa.selenium.TimeoutException", "Page Load Timeout");
			hException_msgs.put("org.openqa.selenium.net.UrlChecker.TimeoutException", "URL Timed Out");
			hException_msgs.put("org.openqa.selenium.StaleElementReferenceException", "Element Left Behind");
			hException_msgs.put("org.openqa.selenium.NoSuchElementException", "Element Not Found");
			hException_msgs.put("com.thoughtworks.selenium.Wait.WaitTimedOutException", "Waiting TimeOut");
			hException_msgs.put("org.openqa.selenium.InvalidElementStateException", "Invalid Element State");
			hException_msgs.put("org.openqa.selenium.ElementNotInteractableException", "Element Not Interactable");
			hException_msgs.put("org.openqa.selenium.ElementNotVisibleException", "Element Not Visible");
			hException_msgs.put("org.openqa.selenium.ElementNotSelectableException", "Element Not Selectable");
			hException_msgs.put("org.openqa.selenium.NoAlertPresentException", "Alert Not Accessible");
			hException_msgs.put("org.openqa.selenium.UnhandledAlertException", "Unhandeled Alert Error");
			hException_msgs.put("org.openqa.selenium.WebDriverException: can't access dead object", "Accessing Dead Object");
			hException_msgs.put("org.openqa.selenium.WebDriverException: Failed to decode response from marionette", "Browser Closed");
			hException_msgs.put("org.openqa.selenium.WebDriverException: Tried to run command without establishing a connection", "Connection Problem");
			hException_msgs.put("org.openqa.selenium.WebDriverException: unknown error: Element is not clickable at point", "Element Over Lapped");
			hException_msgs.put("org.openqa.selenium.WebDriverException: chrome not reachable", "Chrome Driver Mismatch");
			hException_msgs.put("org.openqa.selenium.WebDriverException: unknown error: cannot focus element", "Element Not In Focus");
			hException_msgs.put("org.openqa.selenium.WebDriverException: no such window", "Window Already Closed");
			hException_msgs.put("org.openqa.selenium.WebDriverException: Specified firefox binary location does not exist", "Firefox Path Incorrect");
			hException_msgs.put("org.openqa.selenium.JavascriptException: JavaScript error", "JS Couldn't Run");
			hException_msgs.put("org.openqa.selenium.WebDriverException: Reached error page", "Problem Loading Page");
			hException_msgs.put("com.automatics.packages.library.InvalidInputException: You have Not Provided FIREFOX_PATH in Configuration Parameters","Firefox Path Missing in Configuration parameter");
				
		
		}
	}
}
class Logs {
	
	public static String log(String sTest_FullName) throws FileNotFoundException, InterruptedException {
		
		String sTestLogFile = Reporter.sExecutionLogFldr + "/" + sTest_FullName + ".log";
		String sCalledMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		String msg = "\n" + (new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime())) + "::" + Thread.currentThread().getStackTrace()[2].getClassName() + "." + sCalledMethodName + "\n";
		System.out.println(msg);
		PrintWriter writerOut = new PrintWriter(new FileOutputStream(new File(sTestLogFile), true));
		writerOut.append(msg); writerOut.close();
		return sCalledMethodName;
	}

	public static void log(String sTest_FullName, String msg) throws FileNotFoundException {
		
		String sTestLogFile = Reporter.sExecutionLogFldr + "/" + sTest_FullName + ".log";
		msg = msg.replace("^^BLUE", "");
	
		if (StringUtils.countMatches(msg, "\n")>15) {
			for (int i =0;i <15; i++) {
				System.out.println(msg.split("\n")[i]);
			}
		}
		else
		System.out.println(msg);		
		
		PrintWriter writerOut = new PrintWriter(new FileOutputStream(new File(sTestLogFile), true));
		writerOut.append(msg + "\n"); writerOut.close();
	}
	
	
	
}

