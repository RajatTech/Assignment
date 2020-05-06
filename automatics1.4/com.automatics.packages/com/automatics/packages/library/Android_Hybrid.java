package com.automatics.packages.library;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;

@SuppressWarnings({"rawtypes"})
public class Android_Hybrid 
{
	// Methods Exposed To User
	

	
	/**
	 * @param sTestName
	 * @param sBrowserPref
	 * @return
	 * @throws Exception
	 */
	public static AppiumDriver launchApp(String sTestName, String sAppReference) throws Exception { // TODO - Expose Test Params
		AppiumDriver driver = null;
		String sDesc = Logs.log(sTestName);
		
		try
		{
			String sExe_Server=Utils.hEnvParams.get("Execution_On");
			switch(sExe_Server.toUpperCase()) {
			
			case "MOBILE_LABS" 	:	driver= Helper.launchNativeMobileLabs(sTestName);;								
									break;
			
			case "PERFECTO" 	:	driver=Helper.launchNativePerfecto(sTestName);								
									break;
			
			case "PHYSICAL" : 		driver=Helper.launchNativePhysical(sTestName);
									break;
			
			default : throw new Exception(sTestName + " Error: Execution Server Entered is Not Correct,Choose any one of MOBILE_LABS, PERFECTO or PHYSICAL");
					
				
			}
			
			driver.manage().timeouts().implicitlyWait(Long.valueOf(Utils.hEnvParams.get("OBJ_TIMEOUT")), TimeUnit.SECONDS);
			
			sAppReference = (sAppReference.equals("")) ? "Default" : sAppReference;
			Utils.putObjectDriver(sTestName, driver, sAppReference);
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("App launched :: ");
			org.testng.Reporter.log("                     ");
		}
		catch (Exception ex) {	
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+ex);
			org.testng.Reporter.log("                     ");
		}
		return driver;
	
	}
	/*
	 * To type the text provided by user in the object
	 */	
	
	public static void native_Type(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
	
		String sDesc = "" ; boolean flag = true; 
			
		try { 
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sDesc = Logs.log(sTestName) + " in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " ) , Value = " + sVal ;
			
			try {
				oEle.clear(); Thread.sleep(500L); 
			}
			catch (Exception e) {
				flag = false;
				oEle.sendKeys(sVal);
				native_HideKeyboard(sTestName);
				
			}
			if(flag)
			oEle.sendKeys(sVal);
			native_HideKeyboard(sTestName);
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc + " :: Performed");
			org.testng.Reporter.log("                     ");
			
		}  catch(Exception e) {
	    	if (Utils.handleIntermediateIssue()) { native_Type(sTestName, oEle, sObjStr, sVal); }
	       
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	
	/*
	 * Scrolls Down to half the height of the screen	
	 */
	
	public static void native_ScrollDown(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
		String sDesc = Logs.log(sTestName)+": Scroll to object : " +sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
						
		try{
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);	
			
			Dimension  size = lDriver.manage().window().getSize(); 
			int startx = (int) (size.width * 0.70); 
			int starty = size.height / 2; 
			int endy = (int) (size.height * 0.20);
			//lDriver.swipe(startx, starty, startx, endy, 3000); 
			new TouchAction(lDriver).press(startx, starty).waitAction(Duration.ofMillis(3000)).moveTo(startx,endy ).release().perform();
			while(!Helper.waitForElement(lDriver, oEle, 2)) {
				//lDriver.swipe(startx, starty, startx, endy, 3000); 
				new TouchAction(lDriver).press(startx, starty).waitAction(Duration.ofMillis(3000)).moveTo(startx,endy ).release().perform();
			}
			
		} catch(Exception e) {
	
			if (Utils.handleIntermediateIssue()) { native_ScrollDown(sTestName,oEle,sObjStr); }
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("                     ");
		}
	}
	/*
	 * Perform the Scroll down function the number of time provided by user 
	 */
	
	public static void native_Scroll_Ntimes(String sTestName, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException, InvalidInputException {
		
		
		String sDesc = Logs.log(sTestName)+": Scroll : " +sVal + " times" ;
		
		sVal = Utils.Helper.validateUserInput(sTestName, sVal);
		int sValue= Integer.parseInt(sVal);
		
		try{
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			Dimension dimensions = lDriver.manage().window().getSize();
			for(int i=0;i<sValue;i++) {
		
				Double screenHeightStart = dimensions.getHeight() * 0.5;
				Double screenHeightEnd = dimensions.getHeight() * 0.05;
				
				//lDriver.swipe(0, screenHeightStart.intValue(), 0, screenHeightEnd.intValue(), 2000);
				new TouchAction(lDriver).press(0, screenHeightStart.intValue()).waitAction(Duration.ofMillis(2000)).moveTo(0, screenHeightEnd.intValue()).release().perform();
			}
		   	
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc + " :: Performed");
			org.testng.Reporter.log("Parameter :: "+sVal);
			org.testng.Reporter.log("                     ");
	
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { native_Scroll_Ntimes(sTestName, sVal);; }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("                     ");
		}
	}
	/*
	 * Returns True if keyboard is displayed on screen.
	 */
	
	public static boolean native_IsKeyboardPresent(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
	
		String checkKeyboardCommand = "adb shell dumpsys input_method";
	    String sDesc = Logs.log(sTestName) + " :: Verifying Keyboard is Displayed or Not" ;
	    boolean presentFlg = false;
	    
	    try {
	        Process process = Runtime.getRuntime().exec(checkKeyboardCommand);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        
	        int read; char[] buffer = new char[4096]; StringBuffer output = new StringBuffer();
	        while ((read = reader.read(buffer)) > 0) {
	            output.append(buffer, 0, read);
	        }
	        reader.close(); process.waitFor();
	        if (output.toString().contains("mInputShown=true"))
	        presentFlg = true;
	       
	        org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Expected Value :: True");
			org.testng.Reporter.log("Actual Value :: "+String.valueOf(presentFlg));
			org.testng.Reporter.log("                     ");
	            
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { native_IsKeyboardPresent(sTestName); }
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("                     ");
		}
	    return presentFlg; 
	}
	
	/*
	 * Hides Keyboard if it is displayed on screen.
	 */
	public static boolean native_HideKeyboard(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
	       
		
	    String sDesc = Logs.log(sTestName) + " :: Hide Keyboard in Native App" ;
	    boolean presentFlg = false;
	    
	    try {
	               
	        presentFlg = Helper.IsKeyboardPresent();
	        if (presentFlg) {
	        	AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
	        	String drivercontext=lDriver.getContext();
	        	lDriver.context("NATIVE_APP");
				lDriver.hideKeyboard();
				lDriver.context(drivercontext);
	        	
	        }
	        	
	      
	        org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+":: Performed");
			org.testng.Reporter.log("                     ");    
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { native_HideKeyboard(sTestName); }			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("                     ");
		}
	    return presentFlg; 
	}
	
	/*
	* Switches Context in the Native app from NATIVE to WEBVIEW or vice versa
	* 
	*/
	
	@SuppressWarnings("unchecked")
	public static void switchContext(String sTestName, String Text) throws HeadlessException, IOException, AWTException, InterruptedException {
		 
		String sDesc = Logs.log(sTestName) + " :: Switch to Context : " + Text ;
	
		try {
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			Set<String> contexts = lDriver.getContextHandles(); 
			for(String s:contexts) { 
				if(s.contains(Text)) 
					lDriver.context(s); 
			} 
		
		} catch(Exception e) {
			if (Utils.handleIntermediateIssue()) { switchContext(sTestName, Text); }			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("                     ");
		}
	}
/*	public static void ahNative_SwipeInDropdown(String sTestName,WebElement oEle, String sObjStr,  String Text) throws HeadlessException, IOException, AWTException, InterruptedException { 
		 
		String sDesc =  Logs.log(sTestName);
	
		try { 
	
			Text = Utils.Helper.validateUserInput(sTestName, Text); 
			sDesc=Logs.log(sTestName); 
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName); 
	
			Dimension  size = lDriver.manage().window().getSize(); 
			int startx = (int) (size.width * 0.70); 
			int starty = size.height / 2; 
			int endy = (int) (size.height * 0.20); 
	
	
			while (!ahVerify_ElementPresent(sTestName, oEle, sObjStr, "")) 
			{ 
			  if (Text.contains("VERTICALT2B")){ 
			  //Swipe from Top to Bottom. 
				  lDriver.swipe(startx, endy, startx, starty, 3000); 
				  Thread.sleep(2000); 
			  } 
			  else if (Text.contains("VERTICALB2T")) { 
				  //Swipe from Bottom to Top. 
				  lDriver.swipe(startx, starty, startx, endy, 3000); 
				  Thread.sleep(2000); 
			  } 
			
			  Reporter.print(sTestName, sDesc+" :: Performed"); 
			} 
		} catch(Exception e) { 
	
			if (Utils.handleIntermediateIssue()) { ahNative_SwipeInDropdown(sTestName,oEle,sObjStr, Text); } 
			Reporter.printError(sTestName, e, sDesc); 
		} 
	}*/
	/**
	 * @param sTestName
	 * @param sBrowserPref
	 * @return
	 * @throws AWTException 
	 * @throws HeadlessException 
	 * @throws Exception
	 */
	
	/*
	 * 	Closes the NativeApp
	*/	

	public static void native_closeApp(String sTestName) throws IOException, InterruptedException, HeadlessException, AWTException {
		try {
				((AppiumDriver)Utils.getObjectDriver(sTestName)).quit();
				Thread.sleep(1900);			
				org.testng.Reporter.log("----------------------------------------------");
				org.testng.Reporter.log("CLOSE PERFORMED");
				org.testng.Reporter.log("                     ");
		} catch(Exception e)
		{
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: PROBLEM IN CLOSING");
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("                     ");
		}
	}
	
	public static Object ahSet_CurrentReference(String sTestName, String sBrowserReference) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		return Utils.Set_CurrentObjectReference(sTestName, sBrowserReference);
	}
	public static void set_AndroidPreference(String sTestName, String sPreferences, String sValue) throws HeadlessException, IOException, AWTException, InterruptedException { // TODO - Expose List of Prefs
		Hashtable <String, String > hPref = new Hashtable<>();
		hPref.put(sPreferences, sValue);
		
		String sDesc = Logs.log(sTestName);
	
		try { 
			sPreferences=Utils.Helper.validateUserInput(sTestName, sPreferences);
			sValue=Utils.Helper.validateUserInput(sTestName, sValue);
			if (Android_Web.h2AndroidPrefs.containsKey(sTestName))
				Android_Web.h2AndroidPrefs.get(sTestName).put(sPreferences, sValue);
			else {
				Android_Web.h2AndroidPrefs.put(sTestName,hPref);
			}
		
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log ("\nAndroid Preference :" + sPreferences + " Set to : " + sValue + " -- Done");
			org.testng.Reporter.log("        ");
		
		} catch (Exception ex) {	
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+ex);
			org.testng.Reporter.log("                     ");
			
		}
	}
	
	/*
	 * Verifies the text of the alert box present.
	 * Usage : To check whether expected alert has popped up or not.
	 * 
	 * User has to provide the text , the same is then checked whether it is equal to the text of the alert.
	 *	Note:
	 *		• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
	 *		• For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
	 *		•  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously.
     *
	 * Parameter 1 - test case name
	 * Parameter 2 - expected text in alert box
	 * */	
	
	public static boolean verify_Alert(String sTestName, String sExpAlertTxt) throws HeadlessException, IOException, AWTException, InterruptedException  {

		String sDesc, sActVal, sExpVal; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		
		try { 	
			sExpAlertTxt = Utils.Helper.validateUserInput(sTestName, sExpAlertTxt);
			sExpVal = Reporter.filterUserInput(sExpAlertTxt);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);

			sActVal = lDriver.switchTo().alert().getText();
			lDriver.switchTo().alert().accept();			
			
			bStatus = sExpVal.equals(sActVal);
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("expected Value :: "+sExpVal);
			org.testng.Reporter.log("Actual Value :: "+sActVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);		
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Alert(sTestName, sExpAlertTxt); }
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Used to set current execution platform e.g Execution type :: WEB and Execution platform :: chrome
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : execution type(Web/Mobile)
	 * Parameter 3 : Execution platform (Browser name)
	 * */	
	
	public static void set_CurrentPlatform(String sTestName,String sExeType,String sExe_platfrom) throws HeadlessException, IOException, AWTException {
		try{
			Utils.set_CurrentPlatform(sTestName, sExeType, sExe_platfrom);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@SuppressWarnings("unused")
	public static boolean verify_AlertPresent(String sTestName, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException {
		
		String sDesc, sExpVal ; boolean bStatus = false;
		sDesc = Logs.log(sTestName);	
		try { 			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sExpVal = Reporter.filterUserInput(sVal);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);

			WebDriverWait wait = new WebDriverWait(lDriver, 20);
			
			try {
				if (wait.until(ExpectedConditions.alertIsPresent()) != null) { bStatus = true; }
			}
			catch (Exception e) {}
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("prameter Value :: "+sVal);
			org.testng.Reporter.log("expected Value :: "+sExpVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_AlertPresent(sTestName,sVal); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	
	/*
	 * to clear web element
	 **/	
	public static void clear(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		 
		String sDesc = "" ; 
			
		try { 
			
			sDesc = Logs.log(sTestName) + " Clear in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr)  + " ) " ;
			//Helper.checkReady(sTestName, oEle);
			oEle.clear(); Thread.sleep(500L); 			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+" :: Performed");
			org.testng.Reporter.log("CLOSE PERFORMED");
			org.testng.Reporter.log("                     ");
		    }
		catch(Exception e) {
			
	    		if (Utils.handleIntermediateIssue()) { clear(sTestName, oEle, sObjStr); }	    		
	    		org.testng.Reporter.log("----------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("Exception :: "+e);
				screenShot(sTestName);
				org.testng.Reporter.log("     ");
				Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
				Assert.fail();
		}
	}
	
	/*
	 * Verifies the value of the attribute in given object.
	 * Usage : To verify values of attributes like class,name,id,style,width etc.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath from object map
	 * Parameter 3 : xPath from object map in string format
	 * Parameter 4 : provide the attribute name  
	 * Parameter 5 : expected value  e.g. ""width"" in parameter 4 and its value ""500px"" in parameter 5.
	 * 
	 *	Note:
	 *			• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
	 *			• For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
	 *			•  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously."
     *
	 * */
	
	public static boolean verify_Attribute(String sTestName, WebElement oEle, String sObjStr, String sAttribName,String sExpVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc, sActVal, sVal=sAttribName; boolean bStatus = false;	
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		try { 	
			sAttribName = Utils.Helper.validateUserInput(sTestName, sAttribName);
			sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
			sAttribName = Reporter.filterUserInput(sAttribName);			
			//Helper.checkReady(sTestName, oEle);
			sActVal= oEle.getAttribute(sAttribName);
			
			bStatus = sExpVal.equals(sActVal);			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+  " :: Validate the atttibute - ");
			org.testng.Reporter.log("prameter Value :: "+sVal);
			org.testng.Reporter.log("expected Value :: "+sExpVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Attribute(sTestName, oEle, sObjStr,sVal, sExpVal); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Verifies whether the object is selected or not.
	 * Usage : Check whether checkbox/radiobutton is selected or not.
	 * 
	 * 		Note:
	 *			• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
	 *			• For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
	 *			•  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath from object map
	 * Parameter 3 : xPath from object map in string format
	 * Parameter 4 : value of check box
	 * */
	public static boolean verify_Checked(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; boolean bStatus = false;
	
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		
		try { 	
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			Reporter.filterUserInput(sVal);
			//Helper.checkReady(sTestName, oEle);
			
			if (oEle.isSelected()) {
				bStatus = true;
			}
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("prameter Value :: "+sVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Checked(sTestName, oEle, sObjStr, sVal); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Verifies the value of the cookie.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : Provide the cookie name 
	 * Parameter 3 : Expected value 
	 * e.g. "domain_name" in Parameter 2 and its value "http://www.google.com" in Parameter 3.
	 *  	Note:
	 *			• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
	 *			• For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
	 *			•  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously."
     *
	 * */
	
	public static boolean verify_Cookies(String sTestName,String sCookieName, String sExpVal) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc, sActVal,sVal=sCookieName; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName);
		
	    
		try { 
			sCookieName = Utils.Helper.validateUserInput(sTestName, sCookieName);
			sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
			sCookieName = Reporter.filterUserInput(sCookieName);
		    
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			sActVal = lDriver.manage().getCookieNamed(sCookieName).getValue();
			bStatus = sExpVal.equals(sActVal);		
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("prameter Value :: "+sVal);
			org.testng.Reporter.log("expected Value :: "+sExpVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Cookies(sTestName, sVal,sExpVal); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Verifies whether object is Enabled or not. 
	 *  
	 * Parameter 1 : Test case name
	 * Parameter 2 : xPath from object map
	 * Parameter 3 : xPath from object map in string format
	 * Parameter 4 : expected value to be checked [optional]
	 * 
	 * Note:
	 *			• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
	 *			• For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
	 *			•  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously.
	 * */	

	public static boolean verify_Editable(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; boolean bStatus = false; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
				
		try { 	
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			Reporter.filterUserInput(sVal);
			//Helper.checkReady(sTestName, oEle);
			
			if (oEle.isEnabled()) { 
				bStatus = true;
			}
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("prameter Value :: "+sVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Editable(sTestName, oEle, sObjStr, sVal); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * It Works for following 3 scenarios: 
     *			• To verify the Height of the object.
	 *			• To verify the width of the object.
	 *			• To verify both height and width of the object.
	 * 
	 * Parameter 1 : test case name.
	 * Parameter 2 : xPath from object map.
	 * Parameter 3 : xPath from object map in string format.
	 * Parameter 4 : provide the attribute name
	 * Parameter 5 : expected value 
	 * e.g. "height" in Parameter 4 and its value "200px" in Parameter 5.
	 * 	    "width" in Parameter 4 and its value "100px" in Parameter 5.
	 *		for both, ""Both"" in Parameter 4 and value in Variable name like ""200|500"".
	 *
	 *	Note:
	 * 		• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
	 *		• For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
	 *		• Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously."
     *
	 * */	

	public static boolean verify_Object_Dimension(String sTestName, WebElement oEle, String sObjStr, String sDimenType,String sDimenVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc, sActVal = null,sVal=sDimenType; boolean bStatus = false;
			
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
	    
		try { 
			sDimenType = Utils.Helper.validateUserInput(sTestName, sDimenType);
			sDimenVal = Utils.Helper.validateUserInput(sTestName, sDimenVal);
			sDimenType = Reporter.filterUserInput(sDimenType);
			
		    //Helper.checkReady(sTestName, oEle);
						
		    if (sDimenType.equalsIgnoreCase("Height")) {
		    	sActVal = String.valueOf(oEle.getSize().getHeight());
		    	
		    } else if (sDimenType.equalsIgnoreCase("Width")) {
		    	sActVal = String.valueOf(oEle.getSize().getWidth());
		    	
		    } else if (sDimenType.equalsIgnoreCase("Both")) {
		    	
		    	sActVal = String.valueOf(oEle.getSize().getHeight()) + "|" + String.valueOf(oEle.getSize().getWidth());
		    }
		    
		    bStatus = sDimenVal.equals(sActVal);
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc + " :: DimenType - " + sDimenType+", DimenVal :: "+sDimenVal);
			org.testng.Reporter.log("Actual Value :: "+sActVal);			
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) {  verify_Object_Dimension(sTestName, oEle, sObjStr, sVal,sDimenVal); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Verifies whether Element Is Displayed on page or not.
	 * To Check element presence before performing any operation on it.
	 * 
	 * Argument is optional. User can provide argument for negation and Exit_on_Fail.
	 *	Note:
	 *		• If user want to stop the test execution against a validation failure, write *EXIT_ON_FAIL* as argument.
	 *		• For negative validation, write *NOT* as argument. E.g. *NOT*
	 *		• Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously.
	 *
	 * Parameter 1 : test case name [mandatory]
	 * Parameter 2 : xpath from object map [mandatory]
	 * Parameter 3 : xpath from object map in string format [mandatory]
	 * Parameter 4 : Element to be verified [optional]
	 * */
	
	public static boolean verify_ElementPresent(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; boolean bStatus = false; 
			
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
			
		try { 	
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			Reporter.filterUserInput(sVal);
			//Helper.checkReady(sTestName, oEle);
			AppiumDriver driver = (AppiumDriver)Utils.getObjectDriver(sTestName);
			try {
				
				bStatus = Helper.waitForElement(driver,oEle, Integer.parseInt(Utils.hEnvParams.get("OBJ_TIMEOUT")));
			
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("prameter Value :: "+sVal);
				org.testng.Reporter.log("Step Status ::"+bStatus);
				screenShot(sTestName);
				org.testng.Reporter.log("        ");
				
			}
			catch (NoSuchElementException e) {
		
				org.testng.Reporter.log("----------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("Exception :: "+e);
				org.testng.Reporter.log("Step Status ::"+bStatus);
				screenShot(sTestName);
				org.testng.Reporter.log("     ");
				Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
				Assert.fail();
			}
			
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_ElementPresent(sTestName, oEle, sObjStr, sVal); }
		
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	
	/*
	 * Verifies the text given as argument matches the actual text of the object.
	 * 
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xpath from object map
	 * Parameter 3 : xpath from object map in string format
	 * Parameter 4 : expected text which needs to be verified
	 * 
	 * Note:
	 *		• If user want to stop the test execution against a validation failure, write *EXIT_ON_FAIL* as argument.
	 *		• For negative validation, write *NOT* as argument. E.g. *NOT*
	 *		• Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously.
	 * 
	 * */
	public static boolean verify_Text(String sTestName, WebElement oEle, String sObjStr, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
				
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
	
		try { 
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sExpVal = Reporter.filterUserInput(sExpText);
			//Helper.checkReady(sTestName, oEle);
			
			sActVal = oEle.getText();
			bStatus = sExpVal.equalsIgnoreCase(sActVal);
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Actual Value :: "+sActVal);
			org.testng.Reporter.log("expected Value :: "+sExpVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Text(sTestName, oEle, sObjStr, sExpText); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
		
	}
	/*
	 * Checks if two values provided are equal or not. It is Case-Sensitive.
	 * Usage : User can match a stored value in local parameters to another value.
	 * 
	 * Parameter 1 : test case name
	 * parameter 2: value 1                                  
	 * Parameter 3 : value 2                                                   
	 *		e.g. ""Local{UserName}"" in parameter 2 and ""User1"" in parameter 3 .
	 *
	 *	Note:
	 *		• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
     *      • For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
     *       •  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously."
	 * 
	 * */	

	public static boolean equals(String sTestName, String sExpVal,String sActVal) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = "",sVal=sExpVal; boolean bStatus = false;
	
		try { 
	    	
			sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
			sActVal = Utils.Helper.validateUserInput(sTestName, sActVal);
			sDesc = Logs.log(sTestName) + " :: Values - " + sExpVal +" And "+ sActVal;
			sExpVal = Reporter.filterUserInput(sExpVal);
		    
		    bStatus = sExpVal.equals(sActVal);
			
		    org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Actual Value :: "+sActVal);
			org.testng.Reporter.log("expected Value :: "+sExpVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { equals(sTestName, sVal,sActVal); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	
	/*
	 * Verifies whether the value given as argument is present in HTML code of the page or not.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : Expected value which need to be verified in HTML source
	 * */
	public static boolean verifyIn_HTMLSource(String sTestName, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc, sExpVal; boolean bStatus = false;
		sDesc = Logs.log(sTestName) + " :: Values - " + sExpText;
		sExpVal = Reporter.filterUserInput(sExpText);
		
		try { 	
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			
			bStatus = lDriver.getPageSource().contains(sExpVal);
			//Reporter.print(sTestName, sExpText, sDesc, "NA", "NA", bStatus, Helper.takeScreenshot((AppiumDriver)Utils.getObjectDriver(sTestName)));
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			
			org.testng.Reporter.log("expected Value :: "+sExpVal);
			//org.testng.Reporter.log("expected Text :: "+ sExpText);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verifyIn_HTMLSource(sTestName, sExpText); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			
		}
		return bStatus;
	}
	/*
	 * Verifies the current URL of the Browser contains expected URL given as argument.
	 * Usage : User can assure whether he is on the right page as expected.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : User has to provide the URL , the same is then checked whether it is contained in the URL of the current page
	 * 
	 * Note:
	 *		• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
     *      • For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
     *       •  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously."
	 * */	
	
	public static boolean verifyIn_URL(String sTestName, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		try { 	
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sExpVal = Reporter.filterUserInput(sExpText);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			
			sActVal = lDriver.getCurrentUrl();
			bStatus = sActVal.contains(sExpVal);
			//Reporter.print(sTestName, sExpText, sDesc, sExpVal, sActVal, bStatus, Helper.takeScreenshot(lDriver));
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Actual Value :: "+sActVal);
			org.testng.Reporter.log("expected Value :: "+sExpVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			//org.testng.Reporter.log("expected Text :: "+sExpText);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verifyIn_URL(sTestName, sExpText); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Verifies whether the options given as parameter are present in SelectBox or not. 
	 * Usage : To Verify option presence in SelectBox before selecting it.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath from object map
	 * Parameter 3 : xPath from object map in string format
	 * Parameter 4 : User has to provide the options separated by a "|", same are then checked whether they are present in SelectBox or not. 
	 * 				 e.g.Option1|Option2|........|OptionN
	 * Note:
	 *		• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
     *      • For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
     *      •  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously.
     *      
	 * */
	public static boolean verify_SelectOptions(String sTestName, WebElement oEle, String sObjStr, String sExpOptions) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		
		try { 	
			
			sExpOptions = Utils.Helper.validateUserInput(sTestName, sExpOptions);
			sExpVal = Reporter.filterUserInput(sExpOptions);
			//Helper.checkReady(sTestName, oEle);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				
				Select selList = new Select(oEle); List<WebElement> oSize = selList.getOptions();
				for(int i =0; i<oSize.size(); i++) { 
					sActVal = sActVal + "|" + selList.getOptions().get(i).getText(); 
				}		
				
				for (String sExpOption: sExpVal.split("\\|")) {
					
					if (!sActVal.contains(sExpOption))  {
						bStatus = false; break;
					} else 
						bStatus = true;
				}
								
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("Actual Value :: "+sActVal);
				org.testng.Reporter.log("expected Value :: "+sExpVal);
				org.testng.Reporter.log("Step Status ::"+bStatus);
				screenShot(sTestName);
				org.testng.Reporter.log("        ");
			}
			else
				Reporter.print(sTestName, sDesc + "  :: Error : This Method Works only for Html : 'Select' tag");

		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_SelectOptions(sTestName, oEle, sObjStr, sExpOptions); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Verifies whether the options provided in parameters are selected in the selectBox.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath from object map
	 * Parameter 3 : xPath from object map in string format
	 * Parameter 4 : User has to provide options to be checked separated by a pipe "|".  e.g.  "Option1|Option2"
	 *  
	 * */
	
	public static boolean verify_SelectedOptions(String sTestName, WebElement oEle, String sObjStr, String sExpOptions) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try {
			
			sExpOptions = Utils.Helper.validateUserInput(sTestName, sExpOptions);
			sExpVal = Reporter.filterUserInput(sExpOptions);
			//Helper.checkReady(sTestName, oEle);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				
				Select selList = new Select(oEle); List<WebElement> oSize = selList.getAllSelectedOptions();
				for(int i =0; i<oSize.size(); i++) { 
					sActVal = sActVal + "|" + selList.getAllSelectedOptions().get(i).getText(); 
				}		
				
				for (String sExpOption: sExpVal.split("\\|")) {
					
					if (!sActVal.contains(sExpOption))  {
						bStatus = false; break;
					} else 
						bStatus = true;
				}
				
				
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("Actual Value :: "+sActVal);
				org.testng.Reporter.log("expected Value :: "+sExpVal);
				org.testng.Reporter.log("Step Status ::"+bStatus);
				//org.testng.Reporter.log("exoptions ::"+sExpOptions);
				screenShot(sTestName);
				org.testng.Reporter.log("        ");
			}
			else
				Reporter.print(sTestName, sDesc + "  :: Error : This Method Works only for Html : 'Select' tag");
			    org.testng.Reporter.log("--------------------------------------------------");
			    org.testng.Reporter.log("STEP :: "+sDesc);
			    org.testng.Reporter.log("Step Status ::"+bStatus);
			    screenShot(sTestName);
			    org.testng.Reporter.log("        ");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_SelectedOptions(sTestName, oEle, sObjStr, sExpOptions); }
		
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}

	/*
	 * Verifies that text given as argument is present anywhere on Page.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : expected text to check
	 * */
	@SuppressWarnings("unchecked")
	public static boolean verify_TextPresent(String sTestName, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = "", sExpVal; boolean bStatus = false;
		
		try { 
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sDesc = Logs.log(sTestName) + " :: Validating text '" + sExpText + "'";
			sExpVal = Reporter.filterUserInput(sExpText);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			
			List<WebElement> oList = lDriver.findElements(By.xpath("//*[contains(text(),'" + sExpVal + "')]"));
			
			if (oList.size() > 0) bStatus = true;
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("expected Text :: "+sExpText);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_TextPresent(sTestName, sExpText); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Verifies the current title of the Browser contains the expected title given as argument.
	 * Usage : User can assure whether he is on the right page as expected.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : expected title value
	 * */
	public static boolean verifyIn_Title(String sTestName, String sExpTitle) throws HeadlessException, IOException, AWTException, InterruptedException , InterruptedException {
		
		String sDesc = "", sExpVal; boolean bStatus = false;
		
		try { 	
			
			sExpTitle = Utils.Helper.validateUserInput(sTestName, sExpTitle);
			sDesc = Logs.log(sTestName) + " :: Validating Title '" + sExpTitle + "'";
			sExpVal = Reporter.filterUserInput(sExpTitle);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			
			if (lDriver.getTitle().contains(sExpVal))
				bStatus = true;
			
			//Reporter.print(sTestName, sExpTitle, sDesc, sExpVal, lDriver.getTitle(), bStatus, Helper.takeScreenshot(lDriver));
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("expected Title :: "+sExpTitle);
			org.testng.Reporter.log("expected value :: "+sExpVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verifyIn_Title(sTestName, sExpTitle); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	/*
	 * Verifies whether object is displayed or not.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath from object map
	 * Parameter 3 : xPath from object map in string format
	 * Parameter 4 : value which needs to be checked in object [optional]
	 * */
	public static boolean verify_Visible(String sTestName, WebElement oEle, String sObjStr, String sUserVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 	
			
			sUserVal = Utils.Helper.validateUserInput(sTestName, sUserVal);
			Reporter.filterUserInput(sUserVal);
			//Helper.checkReady(sTestName, oEle);
			
			if (oEle.isDisplayed())
				bStatus = true;
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+ " :: Validating visible '" + sUserVal + "'");
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Visible(sTestName, oEle, sObjStr, sUserVal); }
		
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	
	/*
	 * To select/Deselect a checkBox/radiobutton.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath from object map
	 * Parameter 3 : xPath from object map in string format
	 * Parameter 4 : User has to provide "true" for Selecting and "false" for Deselecting.
	 * */
	public static void check_Uncheck(String sTestName, WebElement oEle, String sObjStr, String sUserVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; 
			
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			sUserVal = Utils.Helper.validateUserInput(sTestName, sUserVal);	
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);

			if (sUserVal.equalsIgnoreCase("true")) {
				
				if (!oEle.isSelected()) {
					Helper.forceClick(lDriver, oEle);
				}
			} else if (sUserVal.equalsIgnoreCase("false")) {
				
				if (oEle.isSelected()) {
					Helper.forceClick(lDriver, oEle);
				}
			}
				
		
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { check_Uncheck(sTestName, oEle, sObjStr, sUserVal); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * This method is used to perform click operation on web page at given location
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath from object map
	 * Parameter 3 : xPath from object map in string format
	 * 
	 * */
	public static void click(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc = "";
				
		sDesc = Logs.log(sTestName) + " on Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		
		try { 	
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			try {
				
				Helper.forceClick(lDriver, oEle);
			}
			catch (Exception e) {
				oEle.click();
			}
		
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { click(sTestName, oEle, sObjStr); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Prints the message in logs.
	 * 
	 * Parameter1 : test case name
	 * Parameter 2 : User has to provide the Text to be printed.
	 * 
	 * */
	public static void print(String sTestName, String sTxt) throws HeadlessException, IOException, AWTException, InterruptedException, InvalidInputException  {

		String sDesc = Logs.log(sTestName);
		sTxt = Utils.Helper.validateUserInput(sTestName, sTxt);		
		org.testng.Reporter.log("--------------------------------------------------");
		org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");	
		org.testng.Reporter.log("        ");
	}
	/*
	 * This method is used to navigate back to previous page 
	 * */
	public static void goBack(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; 
		sDesc = Logs.log(sTestName);
		
		try { 	
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			lDriver.navigate().back();

			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { goBack(sTestName); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Opens the URL provided as argument.
	 *
	 * Parameter 1 : test case name
	 * parameter 2 : "User has to provide the URL to be opened. e.g. "http://www.google.co.in/"
	 * 
	 * */
	public static void openURL(String sTestName, String sURL) throws HeadlessException, IOException, AWTException, InterruptedException  {
		

		String sDesc = ""; 
		
		try {
		
			sURL = Utils.Helper.validateUserInput(sTestName, sURL);
		
			sDesc = Logs.log(sTestName) + ":  " + sURL;
		
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			lDriver.get(sURL);
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		} catch ( Exception e) {
		
			if (Utils.handleIntermediateIssue()) { openURL(sTestName, sURL); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		 
	}
	/*
	 * This method is used to refresh browser.
	 * */
	public static void refresh(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; 
		sDesc = Logs.log(sTestName);
		
		try { 	
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			lDriver.navigate().refresh();

			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { refresh(sTestName); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Select multiple options in SelectBox.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xpath from object map
	 * Parameter 3 : xPath from object map in string format
	 * Parameter 4 : User has to provide the options to be selected seperated by a pipe "|".
	 * 
	 * */
	public static void multiSelect(String sTestName, WebElement oEle, String sObjStr, String sOptions) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try {
			
			sOptions = Utils.Helper.validateUserInput(sTestName, sOptions);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				Select oSelect = new Select(oEle);
				
				for (String sValToBeSelected : sOptions.split("\\|")) {
					oSelect.selectByVisibleText(sValToBeSelected);
					oEle.sendKeys(Keys.CONTROL);
				}
				
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
				org.testng.Reporter.log("        ");
			}
			else
				
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("Step ::"+sDesc + " :: Error : This Method Works only for Html : 'Select' tag");
				org.testng.Reporter.log("        ");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { multiSelect(sTestName, oEle, sObjStr, sOptions); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Select option from SelectBox.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xpath from object map
	 * Parameter 3 : xpath from object map in string format
	 * Parameter 4 : User has to provide the text of the option to be selected.
	 * 
	 * */
	public static void select(String sTestName, WebElement oEle, String sObjStr, String sOption) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 
			sOption = Utils.Helper.validateUserInput(sTestName, sOption);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				Select oSelect = new Select(oEle);
		        oSelect.selectByVisibleText(sOption);      
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
				org.testng.Reporter.log("        ");
			}
			else
				
				org.testng.Reporter.log("--------------------------------------------------");
			    org.testng.Reporter.log("Step ::"+sDesc + " :: Error : This Method Works only for Html : 'Select' tag");
			    org.testng.Reporter.log("        ");
	
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { select(sTestName, oEle, sObjStr, sOption); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Verifies whether frame given as parameter is present in the page or not .

	 * Parameter 1 : test case name
	 * Parameter 2 : User can provide any attribute's value that is unique to that frame(id,class,name etc). e.g. "frame1"
	 * 
	 * */
	public static void verify_Frame(String sTestName, String sFrameInfo) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc, sExpVal; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		try { 	
			
			sFrameInfo = Utils.Helper.validateUserInput(sTestName, sFrameInfo);
			sExpVal  = Reporter.filterUserInput(sFrameInfo);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			@SuppressWarnings("unchecked")
			List<WebElement> oFrameList = lDriver.findElements(By.xpath("//iframe|//frame"));
			for (WebElement oFrame : oFrameList)  {
				if (oFrame.isDisplayed()) {
					
						JavascriptExecutor executor = (JavascriptExecutor) lDriver;
						String sOutAttribs =  executor.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index)" +
								"{ items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", oFrame).toString();
						if (sOutAttribs.contains(sExpVal)) {
							 bStatus=true; break;
						}
				}
			}
			
		
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);	
			org.testng.Reporter.log("expected Value :: "+ sFrameInfo);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
					
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Frame(sTestName, sFrameInfo); }
		
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	
	/*
	 * Switches the handle to Frame/Iframe provided as argument. It switches to first frame displayed if Parameter is left empty.
	 * Usage : Switching to frame/iframe is necessary before performing any operation inside that frame.
	 * 
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : User can provide any attribute's value that is unique to that frame(id,class,name etc). e.g. ""frame1"
	 * 
	 * */
	public static void switch_To_Frame(String sTestName, String sFrameInfo) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		try { 	
			
			sFrameInfo = Utils.Helper.validateUserInput(sTestName, sFrameInfo);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			@SuppressWarnings("unchecked")
			List<WebElement> oFrameList = lDriver.findElements(By.xpath("//iframe|//frame"));
			for (WebElement oFrame : oFrameList)  {
				if (oFrame.isDisplayed()) {
					
					if (!sFrameInfo.isEmpty()) { 
						JavascriptExecutor executor = (JavascriptExecutor) lDriver;
						String sOutAttribs =  executor.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index)" +
								"{ items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", oFrame).toString();
						if (sOutAttribs.contains(sFrameInfo)) {
							lDriver.switchTo().frame(oFrame); bStatus=true; break;
						}
					} else {
						lDriver.switchTo().frame(oFrame); bStatus=true; break;
					}
				}
			}
			
			if (!bStatus) {
				
			    org.testng.Reporter.log("--------------------------------------------------");
			    org.testng.Reporter.log("Step ::"+sDesc + "  :: Frame '" + sFrameInfo + "' not found");
			    org.testng.Reporter.log("        ");
			}
			else {
			
		        org.testng.Reporter.log("--------------------------------------------------");
		        org.testng.Reporter.log("Step ::"+sDesc + "  :: Performed - '" + sFrameInfo + "'");
		        org.testng.Reporter.log("        ");
			}
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { switch_To_Frame(sTestName, sFrameInfo); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Switches the handle back to default content.
	 * Usage : To switch back to default content when user has switched to a iframe/frame.
	 * 
	 * */
	public static void switch_To_Default(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; 
		sDesc = Logs.log(sTestName);
		
		try { 	
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);

			lDriver.switchTo().defaultContent();
		
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { switch_To_Default(sTestName); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}

	public static void switch_To_Tab(String sTestName, String sWindowInfo) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; 
		sDesc = Logs.log(sTestName);
		
		try { 	
			sWindowInfo = Utils.Helper.validateUserInput(sTestName, sWindowInfo);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			if (sWindowInfo.isEmpty()) {
				
				Utils.setScriptParams(sTestName, "Parent_Brwsr", lDriver.getWindowHandle());
				for (String sWinHandle : lDriver.getWindowHandles())  {
				    lDriver.switchTo().window(sWinHandle);
				  }
				Utils.setScriptParams(sTestName, "Child_Brwsr", lDriver.getWindowHandle());

			} else if (sWindowInfo.equalsIgnoreCase("Parent")) {
			
				if (!Utils.h2TestName_ScriptParams.get(sTestName).get("Parent_Brwsr").isEmpty()) {   
					lDriver.switchTo().window(Utils.h2TestName_ScriptParams.get(sTestName).get("Parent_Brwsr"));
				} else {	
					for (String sWinHandle : lDriver.getWindowHandles())  {
					    lDriver.switchTo().window(sWinHandle);
					}
					Utils.setScriptParams(sTestName, "Parent_Brwsr", lDriver.getWindowHandle());
				}
				
			} else if (sWindowInfo.equalsIgnoreCase("Child")) {
				if (!Utils.h2TestName_ScriptParams.get(sTestName).get("Child_Brwsr").isEmpty()) {   
					lDriver.switchTo().window(Utils.h2TestName_ScriptParams.get(sTestName).get("Child_Brwsr"));
				} else {	
					for (String sWinHandle : lDriver.getWindowHandles())  {
					    lDriver.switchTo().window(sWinHandle);
					}
					Utils.setScriptParams(sTestName, "Child_Brwsr", lDriver.getWindowHandle());
				}
			}
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed - '" + sWindowInfo + "'");
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { switch_To_Tab(sTestName, sWindowInfo); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Finds the value of the attribute in given object and stores in the Variable Name provided in argument.
	 * Usage : To store values of attributes like class,name,id,style,width etc.
	 * 
	 * Parameter 1 : Test case name.
	 * Parameter 2 : xPath from object map.
	 * Parameter 3 : xPath from object map in string format.
	 * Parameter 4 : Attribute value which needs to be stored.
	 * Parameter 5 : Variable value in which the value needs to be stored.
	 *   e.g. "id" in Argument 1 and variabe "temp" in Argument 2.
	 *
	 * */
	

	public static void store_Attribute(String sTestName, WebElement oEle, String sObjStr, String sComputedVal,String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc = "", sActVal; 
		
		
		try { 	
		
			sComputedVal = Utils.Helper.validateUserInput(sTestName, sComputedVal);
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
		
			sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
			
			if (sVarName.equals("")) {
				sVarName = "Temp";
			} 
			sActVal = oEle.getAttribute(sComputedVal);
			Utils.setScriptParams(sTestName, sVarName, sActVal);
				
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Store (Attribute-" + sComputedVal + ", Value-" + sActVal + ") in Local Variable '" + sVarName + "'");
			org.testng.Reporter.log("        ");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { store_Attribute(sTestName, oEle, sObjStr,sComputedVal,sVarName ); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Stores the current URL in Variable Name provided as argument.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : "User has to provide the Variable Name to store the current URL. e.g. "Temp"
	 * 
	 * */
public static void store_URL(String sTestName, String sUserVal) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = "", sActVal, sVarName; 
		
		
		try { 
			sUserVal = Utils.Helper.validateUserInput(sTestName, sUserVal);
			
			sDesc = Logs.log(sTestName);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			
			sVarName = sUserVal;
			if (sUserVal.equals("")) {
				sVarName = "Temp";	
			}
			
			sActVal = lDriver.getCurrentUrl();
			Utils.setScriptParams(sTestName, sVarName, sActVal);
				
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Store (Current URL -" + sActVal + ") in Local Variable '" + sVarName + "'");
			org.testng.Reporter.log("        ");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { store_URL(sTestName, sUserVal); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	
	}
/*
 * Stores all options of SelectBox in Variable Name provided as argument.
 * 
 * Parameter 1 : test case name
 * Parameter 2 : xPath from object map
 * Parameter 3 : xPath from object map in string format
 * Parameter 4 : "User has to provide variable name to store options of the SelectBox.e.g.Temp"
 * 
 * */
	public static void store_SelectOptions(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 	
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				
				Select selList = new Select(oEle); List<WebElement> oSize = selList.getOptions();
				for(int i =0; i<oSize.size(); i++) { 
					sActVal = sActVal + "|" + selList.getOptions().get(i).getText(); 
				}		
				
				if (sVarName.equals("")) {
					sVarName = "Temp";	
				}
				
				sActVal = sActVal.substring(2);
				Utils.setScriptParams(sTestName, sVarName, sActVal);
					
				
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("Step ::"+sDesc + " :: Store options '" + sActVal + "' of "+ sObjStr + " in Local Variable '" + sVarName + "'");
				org.testng.Reporter.log("        ");
			}
			else
			
				org.testng.Reporter.log("--------------------------------------------------");
			    org.testng.Reporter.log("Step ::"+sDesc + " :: Error : This Method Works only for Html : 'Select' tag");
			    org.testng.Reporter.log("        ");

		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { store_SelectOptions(sTestName, oEle, sObjStr, sVarName); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Stores all Selected options of SelectBox in Variable Name provided as argument.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : "User has to provide variable name to store selected options of the SelectBox. e.g. "Temp"
	 * 
	 * */
	public static void store_SelectedValue(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 	
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				
				Select selList = new Select(oEle); List<WebElement> oSize = selList.getAllSelectedOptions();
				for(int i =0; i<oSize.size(); i++) { 
					sActVal = sActVal + "|" + selList.getAllSelectedOptions().get(i).getText(); 
				}		
				
				if (sVarName.equals("")) {
					sVarName = "Temp";	
				}
				
				sActVal = sActVal.substring(2);
				Utils.setScriptParams(sTestName, sVarName, sActVal);
					
				
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("Step ::"+sDesc + " :: Store selected options '" + sActVal + "' of "+ sObjStr + " in Local Variable '" + sVarName + "'");
				org.testng.Reporter.log("        ");
			}
			else
				
			    org.testng.Reporter.log("--------------------------------------------------");
			    org.testng.Reporter.log("Step ::"+sDesc + " :: Error : This Method Works only for Html : 'Select' tag");
			    org.testng.Reporter.log("        ");
	
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { store_SelectedValue(sTestName, oEle, sObjStr, sVarName); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	
	/*
	 * Stores the visible text of the object in Variable Name provided as argument.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : Stores the visible text of the object in Variable Name provided as parameter.
	 * 
	 * */
	public static void store_Text(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			sActVal = oEle.getText(); 
			
			if (sVarName.equals("")) {
				sVarName = "Temp";	
			}
			
			Utils.setScriptParams(sTestName, sVarName, sActVal);
				
		
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Store '" + sActVal + "' of "+ sObjStr + " in Local Variable '" + sVarName + "'");
			org.testng.Reporter.log("        ");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { store_Text(sTestName, oEle, sObjStr, sVarName); }
		
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Stores the visible text of the object in Variable Name provided as argument.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : Stores the visible text of the object in Variable Name provided as parameter.
	 * */
	public static void store_Value(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null; 
		
		sDesc = Logs.log(sTestName) + " of Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		//Helper.checkReady(sTestName, oEle);
		
		try { 	
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			sActVal = oEle.getText(); 
			
			if (sVarName.equals("")) {
				sVarName = "Temp";	
			}
			
			Utils.setScriptParams(sTestName, sVarName, sActVal);
				
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Store '" + sActVal + sActVal + " in Local Variable '" + sVarName + "'");
			org.testng.Reporter.log("        ");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { store_Value(sTestName, oEle, sObjStr, sVarName); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	
	@SuppressWarnings("unused")
	public static void type(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc = "" ; boolean flag = true; 
			
		try { 
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sDesc = Logs.log(sTestName) + " in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " ) , Value = " + sVal ;
			//Helper.checkReady(sTestName, oEle);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			
			try {
				oEle.clear(); Thread.sleep(500L); 
			}
			catch (Exception e) {
				flag = false;
				oEle.sendKeys(sVal);
				if(Utils.hEnvParams.get("Execution_On").equalsIgnoreCase("Physical"))
					Android_Hybrid.native_HideKeyboard(sTestName);
				
			}
			if(flag)
			oEle.sendKeys(sVal);
			if(Utils.hEnvParams.get("Execution_On").equalsIgnoreCase("Physical"))
				Android_Hybrid.native_HideKeyboard(sTestName);
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { type(sTestName, oEle, sObjStr, sVal); }
	    	
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	@SuppressWarnings("unused")
	public static void type_Advanced(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc = "" ; boolean flag = true; 
			
		try { 
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sDesc = Logs.log(sTestName) + " in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " ) , Value = " + sVal ;
			//Helper.checkReady(sTestName, oEle);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);	
			Helper.CheckPopUp_Object(sTestName, oEle, sObjStr);
				if (Helper.CheckExist(lDriver,oEle).contains("True")) {
					if (!oEle.getAttribute("value").equals("")){
						oEle.clear();Thread.sleep(2000L);
					} 
					oEle.sendKeys(sVal); 
				}
				switch_To_Default(sTestName);	
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { type_Advanced(sTestName, oEle, sObjStr, sVal); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * 	It deletes all cookies of the browser.
	 */

	public static void deleteCookies(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException {
		
		String sDesc; 
		sDesc = Logs.log(sTestName)+" :: All Cookies Deleted";
		
		try { 	

			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
	
			lDriver.manage().deleteAllCookies();
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("        ");

		}  catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { deleteCookies(sTestName); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}

	/*
	 * Deletes  the cache of the browser.
	 * */
	public static void deleteCache(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException {
		
		String sDesc, sBrowser, sTitle = "", vbPath; 
		sDesc = Logs.log(sTestName) + " :: Cache Deleted";
		
		try { 	
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			sBrowser = Utils.h2TestName_TestParams.get(sTestName).get("exePlatform"); 
			if(sBrowser.equalsIgnoreCase("Firefox"))
				sTitle = lDriver.getTitle() +" - Mozilla Firefox";
			else if(sBrowser.equalsIgnoreCase("IE"))
				sTitle = lDriver.getTitle() +" - Internet Explorer";
			
				
			vbPath = "\""+System.getProperty("user.dir").replace("\\", "/") +"/Packages/delcache.vbs\"";
		
			Runtime.getRuntime().exec( "cscript " +vbPath+ " \""+sTitle+ "\"");
	
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) {deleteCache(sTestName); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	
	/*
	 * Performs Double Click on the object given.
	 * 
	 * Parameter 1 : test case name
	 * parameter 2 : xPath
	 * Parameter 3 : xPath in string format
	 * 
	 * */	
	public static void doubleClick(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException   {
		
		String sDesc; 
		
		sDesc = Logs.log(sTestName)+ " :: Double Click on Element: " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			Actions action = new Actions(lDriver);
			action.moveToElement(oEle).doubleClick().build().perform();
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("        ");
			
		}   catch (Exception e) {
		
			if (Utils.handleIntermediateIssue()) { doubleClick(sTestName,oEle,sObjStr); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	
	public static Object ahRunScript(String sTestCaseName,ITestContext testContext) throws Exception   {
		
		return Utils.RunScript(sTestCaseName, testContext);
	}
	
	public static Object ahRunScript(String sTestName,String sTestCaseName,ITestContext testContext) throws Exception   {
		
		return ahRunScript( sTestCaseName, testContext);
	}
	
	public static void wait(String sTestName, String secs) throws NumberFormatException, InterruptedException, HeadlessException, IOException, AWTException, InterruptedException, InvalidInputException  {
		
		secs = Utils.Helper.validateUserInput(sTestName, secs);
		String sDesc = Logs.log(sTestName)+" :: Waiting for: ("+secs+")seconds";
		Thread.sleep(Long.valueOf(secs)*1000);
		
		org.testng.Reporter.log("--------------------------------------------------");
		org.testng.Reporter.log("Step ::"+sDesc);
		org.testng.Reporter.log("        ");
	}
	
	public static void waitForElementPresent(String sTestName, WebElement oEle,  String sObjStr, String WaitTimeSec) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc = "" ;
		
		sDesc =  Logs.log(sTestName)+" :: Waiting for Element : " +sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
				
		try {
			
			WaitTimeSec = Utils.Helper.validateUserInput(sTestName, WaitTimeSec);
			long sec = Long.parseLong(WaitTimeSec);
			//Helper.checkReady(sTestName, oEle);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			WebDriverWait wait = new WebDriverWait(lDriver, sec);
			wait.until(ExpectedConditions.visibilityOf(oEle));
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc+ " to be Present: ("+WaitTimeSec+")seconds");
			org.testng.Reporter.log("        ");
		
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { waitForElementPresent(sTestName,oEle,sObjStr, WaitTimeSec); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}

	public static boolean verify_SubStringInText(String sTestName, WebElement oEle, String sObjStr, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName)+ " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try {
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sExpVal = Reporter.filterUserInput(sExpText);	
			//Helper.checkReady(sTestName, oEle);
			
			sActVal = oEle.getText();
			sActVal = sActVal.replace("\n", " ");
			bStatus = (sActVal.contains(sExpVal));	
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+ " :: Check the contents '" + sExpText + "'");
			org.testng.Reporter.log("Expected Title :: "+sExpVal);
			org.testng.Reporter.log("Actual Title :: "+sActVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			org.testng.Reporter.log("expected text ::"+ sExpText);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
		
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { verify_SubStringInText(sTestName,oEle,sObjStr, sExpText); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	
	public static void acceptAlert(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = Logs.log(sTestName)+" :: Clicking on 'OK' of Popup";
		
		try {
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			Alert alert = lDriver.switchTo().alert();	
			alert.accept();
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("        ");
		   
		}   catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { acceptAlert(sTestName); }
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Accepts the alert.
	 * Usage : Clicks on CANCEL of Alert.
	 * 
	 * Parameter 1 : test case name
	 * 
	 * */
	public static void cancelAlert(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = Logs.log(sTestName)+" :: Clicking on 'CANCEL' of Popup";
		
		try {
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			Alert alert = lDriver.switchTo().alert();							
		    alert.dismiss();
		    
		  
		    org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("        ");
				   
		}   catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { cancelAlert(sTestName); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Usage : Navigates one page Forward.
	 * Argument Required :: Test case name[Mandatory].
	 * */
	public static void goForward(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = Logs.log(sTestName)+" :: Moving Forward One Page";
		
		try{
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
		    lDriver.navigate().forward();
		    
		   
		    org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("        ");
	
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { goForward(sTestName); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Verifies the value of the attribute "title" of  the given object.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * Parameter 3 : xPath string
	 * Parameter 4 : User has to provide the text , the same is then checked whether it is equal to the value of "title" attribute of  the object.
	 * 
	 * */	
	
	public static boolean verify_ToolTip(String sTestName, WebElement oEle, String sObjStr, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName)+ " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try {
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sExpVal = Reporter.filterUserInput(sExpText);
			//Helper.checkReady(sTestName, oEle);			
			sActVal = oEle.getAttribute("title").replace("\n", " "); 
			bStatus = sExpVal.equals(sActVal);
		
		   
		    org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+ " :: Verifying ToolTip Text:'");
			org.testng.Reporter.log("expected Value :: "+ sExpVal);
			org.testng.Reporter.log("Actual Value :: "+ sActVal);
			org.testng.Reporter.log("Step Status ::"+bStatus);
			org.testng.Reporter.log("Expected text ::"+sExpText);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { verify_ToolTip(sTestName, oEle, sObjStr, sExpText); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	    return bStatus;
	}
	/*
	 * Verifies the value of the attribute in CSS of the object given.
	 * 
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * Parameter 3 : xPath string
	 * Parameter 4 : attribute value
	 * Parameter 5 : expected value
	 * 		e.g "background-color" in parameter 4 and its value "#f0f2f5"  in parameter 5.
	 * 
	 * */	
	
	public static boolean verifyIn_CSSValue(String sTestName, WebElement oEle, String sObjStr, String sAttribute,String sExpVal)throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
		String sDesc, sActVal = null, sVal=sAttribute; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName)+ " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
			
		try {
			sAttribute = Utils.Helper.validateUserInput(sTestName, sAttribute);
			sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
			
			sAttribute = Reporter.filterUserInput(sAttribute);
			//Helper.checkReady(sTestName, oEle);
			
			sActVal = oEle.getCssValue(sAttribute);
			bStatus = sActVal.equals(sExpVal);
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+ " :: Verifying Value in CSS:'" + sVal + "'");
			org.testng.Reporter.log("expected Value :: "+ sExpVal);
			org.testng.Reporter.log("Actual Value :: "+ sActVal);			
			org.testng.Reporter.log("Step Status ::"+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { verifyIn_CSSValue(sTestName, oEle, sObjStr,sVal,sExpVal); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	    return bStatus;
	}	
	/*
	 * Finds the value of the attribute in CSS of given object and stores in the Variable Name provided in argument.
	 * Usage : To store values of attributes like position,padding,background-color etc. 
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * Parameter 3 : xPath string
	 * Parameter 4 : attribute value
	 * Parameter 5 : variable value
	 * 		e.g. "position" in paramter 4 and variabe "temp" in parameter 5.
	 * 
	 * */
	public static void store_CSSValue(String sTestName, WebElement oEle, String sObjStr,String sComputedVal,String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 
		
		String sDesc, sActVal; 
		sDesc = Logs.log(sTestName) + "GetCSS : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
			
		try {
			
			sComputedVal = Utils.Helper.validateUserInput(sTestName, sComputedVal);
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			if (sVarName.equals("")) {
				sVarName="Temp";
			} 
			sActVal = oEle.getCssValue(sComputedVal);
			Utils.setScriptParams(sTestName, sVarName, sActVal);
			
			//Reporter.print(sTestName,sDesc + ", Type:("+sComputedVal+"), Got Value:("+sActVal+")");
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("Type :: "+ sComputedVal);
			org.testng.Reporter.log("Actual Value :: "+ sActVal);
			org.testng.Reporter.log("        ");
		
		}	catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { store_CSSValue(sTestName,oEle,sObjStr,sComputedVal,sVarName); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
    }
	/*
	 * Stores Current Title in Variable Name provided as argument.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * Parameter 3 : xPath string
	 * Parameter 4 : attribute value
	 * Parameter 5 : User has to provide the Variable Name to store the the title of the current page. e.g. "Temp"
	 * 
	 * */
	public static void store_Title(String sTestName, String sUserVal) throws HeadlessException, IOException, AWTException, InterruptedException  { 
		
		String sDesc, sActVal, sVarName; 
		sDesc = Logs.log(sTestName);
		
		try {	
			sUserVal = Utils.Helper.validateUserInput(sTestName, sUserVal);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);	
			sVarName = (sUserVal.equals("")) ? "Temp" : sUserVal;

			sActVal =lDriver.getTitle();
			Utils.setScriptParams(sTestName, sVarName, sActVal);
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Store (Current Title -" + sActVal + ") in Local Variable '" + sVarName + "'");
			org.testng.Reporter.log("        ");
				
		} 	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { store_Title(sTestName,sUserVal); }
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}				
    }
	/*
	 * Clicks on the Text provided as Argument.
	 * Usage : Clicking on an object by providing its visible Text.
	 * 
	 * Parameter 1 : test case name [mandatory]
	 * parameter 2 : Provide the text on which you want to click.[mandatory]
	 * */	

	public static void click_OnText(String sTestName, String sText) throws HeadlessException, IOException, AWTException, InterruptedException 	{
		
		String sDesc = Logs.log(sTestName)+" :: Click on Text: "+ sText;
		boolean foundFlg = false;
		
		try {
			
			sText = Utils.Helper.validateUserInput(sTestName, sText);
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);	
			@SuppressWarnings("unchecked")
			List<WebElement> childEle = lDriver.findElements(By.tagName("*"));
			for(WebElement oEle : childEle){	
				if(oEle.isDisplayed()) {
					if (oEle.getAttribute("innerHTML").contains(sText)) {
						oEle.click(); foundFlg = true; break;
				    }
			    }
			}
			sDesc=Logs.log(sTestName)+" :: Click on Text: "+ sText + ", found " + String.valueOf(foundFlg);
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("        ");
			
		} catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { click_OnText(sTestName, sText); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * To send "enter" to an object.
	 * 
	 * Parameter 1 : test case name 
	 * */
	
	public static void enter(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException {
		String sDesc = "" ;  
		
		try {
			
			sDesc = Logs.log(sTestName) + " in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
			
			oEle.sendKeys(Keys.ENTER);
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
		
		} catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { enter(sTestName, oEle, sObjStr); }
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Stores font color of the object in Variable Name given as argument
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * Parameter 3 : xPath string
	 * Parameter 4 : User has to provide the variable name in which font color of the object will be stored.
	 * 
	 * */
	public static void store_FontColor(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal; 
		
		sDesc = Logs.log(sTestName)+" :: Reterving Font color of Obj : "+sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 
			
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			sActVal = oEle.getCssValue("color");
			String[] numbers = sActVal.replace("rgba(", "").replace(")", "").split(",");
			int iR = Integer.parseInt(numbers[0].trim()); int iG = Integer.parseInt(numbers[1].trim()); int iB = Integer.parseInt(numbers[2].trim());
			sActVal = String.format("#%02x%02x%02x", iR, iG, iB).toUpperCase();
			
			sVarName = (sVarName.equals("")) ? "Temp" : sVarName;

			Utils.setScriptParams(sTestName, sVarName, sActVal);
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Store '" + sActVal + "' of "+ sObjStr + " in Local Variable '" + sVarName + "'");
			org.testng.Reporter.log("        ");
		
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { store_FontColor(sTestName,oEle,sObjStr,sVarName); }
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Verifies the text given as argument is present in text of object(id=errorDiv)
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : User has to provide the text , the same is then checked whether it is present in object(id=errorDiv) or not.
	 * 
	 * */	
	
	public static void verifyIn_Error(String sTestName, String sExpText) throws Exception {	
		
		String sDesc = "";
		
		try {
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sDesc = Logs.log(sTestName) + " :: Validating error message( " + sExpText + " )";
			
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
		    WebElement oEle = (WebElement) lDriver.findElement(By.xpath("//*[@id='errorDiv']"));
		    verify_SubStringInText(sTestName, oEle, "errObj", sExpText);
			    
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc);
			org.testng.Reporter.log("        ");
		 
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { verifyIn_Error(sTestName,sExpText); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	
	/*
	 * Opens link in new window
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * parameter 3 : xpath in string format
	 * 
	 * */
	
	
	public static void openIn_NewTab(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 
		
		String sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try{
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			Utils.setScriptParams(sTestName, "Parent_Brwsr", lDriver.getWindowHandle());

		    Actions newTab = new Actions(lDriver);
		    newTab.moveToElement(oEle); 
		    newTab.perform();
		    newTab.keyDown(Keys.CONTROL).click(oEle).build().perform();
			
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		    for(String winHandle : lDriver.getWindowHandles()){
			    lDriver.switchTo().window(winHandle);			    
			}				
			Utils.setScriptParams(sTestName, "Child_Brwsr", lDriver.getWindowHandle());
			
		}	catch(Exception e) {
	
			if (Utils.handleIntermediateIssue()) { openIn_NewTab(sTestName, oEle, sObjStr); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Focuses on object
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * parameter 3 : xpath in string format
	 * 
	 * */	
	
	public static void focus(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 
		
		String sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try{
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);	
			if ("input".equals(oEle.getTagName())) {
				oEle.sendKeys("");
			} else {
				new Actions(lDriver).moveToElement(oEle).perform();
			}
			
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Focussed on Obj : " + sObjStr + ")");
			org.testng.Reporter.log("        ");
		 
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { focus(sTestName,oEle,sObjStr); }
		
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	}
	/*
	 * Checks if the value is empty or not.
	 * Can be used where user want to validate the value of a stored variable during execution.
	 * User can provide the variable name (i.e. Local/Test), the same can be verified whether it is empty or not. e.g.  Local{UserName}
	 *	Note:
	 *		• If user want to stop the test execution against a validation failure, append *EXIT_ON_FAIL* at end of argument.
	 *		• For negative validation, append *NOT* at end of argument. E.g. <Arg_By_User>*NOT*
	 *		•  Append *EXIT_ON_FAIL**NOT* for both cases to work simultaneously.
     *
     * Parameter 1 : Test case name[Mandatory]
     * Parameter 2 : value to check[Mandatory]
	 * */
	
	public static boolean is_Empty(String sTestName, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = "", sExpVal; boolean bStatus = false;
	
	    try { 	
	    	sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sDesc = Logs.log(sTestName) + " :: Value - " + sVal;
			sExpVal = Reporter.filterUserInput(sVal);
			
		    bStatus = sExpVal.isEmpty();
			Reporter.print(sTestName, sVal, sDesc, "NA","NA", bStatus);
//			org.testng.Reporter.log("--------------------------------------------------");
//			org.testng.Reporter.log("Step ::"+sDesc);
//			org.testng.Reporter.log("        ");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { is_Empty(sTestName, sVal); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
		return bStatus;
	}
	
	/*
	 * Hover the object
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * parameter 3 : xpath in string format
	 * 
	 * */
	public static void mouseOver(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 
		
		String sDesc ; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try {
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
			Actions newTab = new Actions(lDriver);
			newTab.moveToElement(oEle); 
			newTab.perform();
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
			org.testng.Reporter.log("        ");
			
		
		} catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { mouseOver(sTestName, oEle, sObjStr);; }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	 }
	/*
	 * Clicks on the Object.

	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * parameter 3 : xpath in string format
	 * 
	 * */
	public static void mouseClick(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 

		String sDesc = "";
		
				
		sDesc = Logs.log(sTestName) + " on Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		try {
			AppiumDriver lDriver =(AppiumDriver)Utils.getObjectDriver(sTestName);
		
		
				Actions newTab = new Actions(lDriver);
				newTab.moveToElement(oEle); 
				newTab.perform();
				newTab.click(oEle).build().perform();
		
			    
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("Step ::"+sDesc + " :: Performed");
				org.testng.Reporter.log("        ");	
		}
		catch (Exception e) {
			oEle.click();
		}
		
	 }
	// takes screenshot 
	public static void screen_Capture(String sTestName , String sUsrVal) throws HeadlessException, IOException, AWTException, InterruptedException  { 
		
		String sDesc = "" ; 
		
		try {
			sUsrVal = Utils.Helper.validateUserInput(sTestName, sUsrVal);
			sDesc = Logs.log(sTestName) + " - " + sUsrVal ;
			
			org.testng.Reporter.log("--------------------------------------------------");
		    org.testng.Reporter.log("STEP :: "+sDesc+ " :: Verifying ToolTip Text:'");
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
		
		} catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { screen_Capture(sTestName, sUsrVal); }
			
			org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}
	 }
	/*
	 * To Store the cell data of specified row and column in given variable name.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * parameter 3 : xPath in string format
	 * Parameter 4 : Provide row number and column number separated by Double colon "::" 
	 * Parameter 5 : Variable name 
	 *       e.g. "1::2" in parameter 4 and  "user1" in parameter 5.
	 *       
	 *       Both RowNumber and Column Number can be set "ALL" to store all Rows and All Columns values. 
	 *       	e.g.  "1::ALL"  in parameter 4  Stores all columns values of first row and the variable "temp1" is stored in the parameter 5.
	 * 
	 * */
	
	public static void get_Cell_Data(String sTestName, WebElement oEle, String sObjStr, String sRowCol,String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
		String sDesc = "", sActVal ="", sColumn = "", sRow="" , flagTh = "false",sVal=sRowCol;  
		int sColumnNmbr, sRowNmbr,i=1;
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			
			sRowCol = Utils.Helper.validateUserInput(sTestName, sRowCol);
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			sRowCol = Reporter.filterUserInput(sRowCol);
			if (sRowCol.contains("::")) {
				sColumn = sRowCol.split("::")[1];
				sRow = sRowCol.split("::")[0];
			}
			else {
				//Reporter.print(sTestName, sVal + "*EXIT_ON_FAIL*", sDesc +"  : Please provide input correctly", "True", "False", false);
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc+" : Please provide input correctly");
				org.testng.Reporter.log("Actual Value :: "+ sActVal);
				org.testng.Reporter.log("Step Status :: false");
				screenShot(sTestName);
				org.testng.Reporter.log("        ");
			}
			if(sVarName.equals(""))
			{
				sVarName="Temp";
			}
			if(sColumn.equalsIgnoreCase("ALL")) 
				sColumnNmbr = -1;
			else
				sColumnNmbr = Integer.parseInt(sColumn);
			if(sRow.equalsIgnoreCase("ALL")) {
				sRowNmbr = -1 ;
			} else 
				sRowNmbr = Integer.parseInt(sRow);
				
			List<WebElement> rows = oEle.findElements(By.tagName("tr"));
			
			for(WebElement row : rows) {
				List<WebElement> cols = null;
				String htmlSource = row.getAttribute("innerHTML");
				if (htmlSource.contains("<th") ) {
					if (flagTh.equalsIgnoreCase("true")) {
						continue;
					}
					else {
						cols = row.findElements(By.tagName("th"));
						flagTh = "true";
					}
					
				}
				else {
					cols = row.findElements(By.tagName("td"));
				}
				if (sColumnNmbr==-1 && sRowNmbr==-1) { 			//All rows All Columns
						sActVal = sActVal + "\nRow No." + i + ":";
							for(int j=0;j<cols.size();j++)
								sActVal = sActVal + cols.get(j).getText() +"|";  	
					} 
				else {
						if(sColumnNmbr==-1 && sRowNmbr!=-1) {		//Row Number but All Columns
							if(i==sRowNmbr) {
								sActVal = sActVal + "Row No." + i + ":";
								for(int j=0;j<cols.size();j++)
									sActVal = sActVal + cols.get(j).getText() +"|";  
								break;
							}
						}
						else {
							if(sRowNmbr==-1 && sColumnNmbr!=-1) 	//All rows But Column Number
								sActVal = sActVal + cols.get(sColumnNmbr-1).getText() +"|";
							else if(i==sRowNmbr) {					//Row Number Column Number
								sActVal = cols.get(sColumnNmbr-1).getText();
								break;
							}
						}
				}
					i++;
			}
			
			Utils.setScriptParams(sTestName, sVarName, sActVal);
			Reporter.print(sTestName, sDesc + " :: Store Cell Data of Row: '" + sRow + "' and Column: '" + sColumn + "' in Local Variable '" + sVarName + "', Data is: '" + sActVal + "'");
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+" :: Store Cell Data of Row: '" + sRow + "' and Column: '" + sColumn + "' in Local Variable '" + sVarName +"'");
			org.testng.Reporter.log("Actual Value :: "+ sActVal);
			org.testng.Reporter.log("        ");
			
		} catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) {  get_Cell_Data(sTestName, oEle, sObjStr, sVal,sVarName);}
	    	
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		
		}
	}
	/*
	 * To Store the cell data of specified column where other column's value matches with the expected value.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * parameter 3 : xPath in string format
	 * Parameter 4 : Provide Column number to be compared  and its expected value seperated by "=" , then required Column number 
	 * Parameter 5 : Variable name 
 	 *			e.g. "2=user1" in Parameter 4 and "3=user2" in Parameter 5.
	 * 
	 * */
	public static void get_RowWith_CellData(String sTestName, WebElement oEle, String sObjStr,String sCol,String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
		String sDesc, sActVal ="", sUsrVal = "", sResult = "", flagTh = "false",sVal=sCol; int sRequiredColumnNmbr = 0, sColumnNmbr = 0;
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try {
			sCol = Utils.Helper.validateUserInput(sTestName, sCol);
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			sCol = Reporter.filterUserInput(sCol);
			
			if (sCol.contains("=")) {
				sColumnNmbr = Integer.parseInt(sCol.split("=")[0]);
				sUsrVal = sCol.split("=")[1];
				sRequiredColumnNmbr = Integer.parseInt(sVarName.split("=")[0]);
				sVarName=sVarName.split("=")[1];
			}	
			else {
				//Reporter.print(sTestName, sVal + "*EXIT_ON_FAIL*", sDesc +"Plese provide input correctly", "True", "False", false);
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc+"Plese provide input correctly");
				org.testng.Reporter.log("Actual Value :: "+ sActVal);
				org.testng.Reporter.log("Step Status :: false");
				org.testng.Reporter.log("        ");
			}
				List<WebElement> rows = oEle.findElements(By.tagName("tr"));
				
				for(WebElement row : rows) {
					List<WebElement> cols = null;
					String htmlSource = row.getAttribute("innerHTML");
					if (htmlSource.contains("<th") ) {
						if (flagTh.equalsIgnoreCase("true")) {
							continue;
						}
						else {
							cols = row.findElements(By.tagName("th"));
							flagTh = "true";
						}
						
					}
					else {
						cols = row.findElements(By.tagName("td"));
					}
						sActVal = cols.get(sColumnNmbr-1).getText();
						if(sActVal.equalsIgnoreCase(sUsrVal)) {
							sResult = cols.get(sRequiredColumnNmbr-1).getText();
							break;
						}
					
					}
				
				Utils.setScriptParams(sTestName, sVarName, sResult);
				
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("Step ::"+sDesc + " :: Store Value of Column: '"+ sRequiredColumnNmbr + "' where Column '" + sColumnNmbr + "' has Value: '" + sUsrVal + "' in Local Variable '" + sVarName + "' Data is: '" + sResult + "'");
				org.testng.Reporter.log("        ");
				
			} catch(Exception e) {
				
			    	if (Utils.handleIntermediateIssue()) { get_RowWith_CellData(sTestName,  oEle, sObjStr, sVal,sVarName);}
			    	
			    	org.testng.Reporter.log("----------------------------------------------");
					org.testng.Reporter.log("STEP :: "+sDesc);
					org.testng.Reporter.log("Exception :: "+e);
					screenShot(sTestName);
					org.testng.Reporter.log("     ");
					Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
					Assert.fail();
					
			}
	
	}
	/*
	 * Verifies the cell data of specified row and column.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * parameter 3 : xPath in string format
	 * Parameter 4 : provide row number and column number separated by Double colon "::" 
	 * Parameter 5 : Variable name 
 	 *			 e.g. "1::2" in Parameter 4 and "user1" in Parameter 5 which Verifies cell data of second Column of first row matches with "user1" or not.
 	 *
 	 *		Both RowNumber and Column Number can be set "ALL" to store all Rows and All Columns values.    
 	 *			 e.g.  "1::ALL"  in Parameter 4  Stores all columns values of first row and the variable "temp1" is stored in the Parameter 5.
	 * 
	 * */
	
	
	public static void verify_Cell_Data(String sTestName, WebElement oEle, String sObjStr,String sRowCol,String sExpVal) throws HeadlessException, IOException, AWTException, ClassNotFoundException, InterruptedException {
		
		String sDesc = null,sVal=sRowCol, sActVal ="", sColumn = null, sRow="", flagTh = "false"; boolean bStatus = false; int sColumnNmbr = 0, sRowNmbr,i=1;
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			 
			sRowCol = Utils.Helper.validateUserInput(sTestName, sRowCol);
			sExpVal=Utils.Helper.validateUserInput(sTestName, sExpVal);
			sRowCol = Reporter.filterUserInput(sRowCol);
			
			//Helper.checkReady(sTestName, oEle);
			sRow = sExpVal.split("::")[0];
			if (sRowCol.contains("::")) {
				sColumn = sRowCol.split("::")[1];
				sRow = sRowCol.split("::")[0];
			}
			else {
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc+" : Please provide input correctly");
				org.testng.Reporter.log("Actual Value :: "+ sActVal);
				org.testng.Reporter.log("Step Status :: false");
				org.testng.Reporter.log("        ");
			}
			
			if(sColumn.equalsIgnoreCase("ALL")) 
				sColumnNmbr = -1;
			else
				sColumnNmbr = Integer.parseInt(sColumn);
			if(sRow.equalsIgnoreCase("ALL")) {
				sRowNmbr = -1 ;
			} else 
				sRowNmbr = Integer.parseInt(sRow);
			
			List<WebElement> rows = oEle.findElements(By.tagName("tr"));
			for(WebElement row : rows) {
				List<WebElement> cols = null;
				String htmlSource = row.getAttribute("innerHTML");
				if (htmlSource.contains("<th") ) {
					if (flagTh.equalsIgnoreCase("true")) {
						continue;
					}
					else {
						cols = row.findElements(By.tagName("th"));
						flagTh = "true";
					}
					
				}
				else {
					cols = row.findElements(By.tagName("td"));
				}
					if (sColumnNmbr==-1 && sRowNmbr==-1) {
						for(int j=0;j<cols.size();j++) {
							sActVal =  sActVal +cols.get(j).getText() + "|";
							if(sActVal.contains(sExpVal)) {
								bStatus = true;
								break;
							}
						}
					} 
					else {
						if(sColumnNmbr==-1 && sRowNmbr!=-1) {
							if(i==sRowNmbr) {
								for(int j=0;j<cols.size();j++) {
									sActVal =  sActVal +cols.get(j).getText()+ "|";
									if(sActVal.contains(sExpVal)) {
										bStatus = true;
										break;
									}
								}
							}
						}
						else {
							if(sRowNmbr==-1 && sColumnNmbr!=-1) { 
								sActVal =  sActVal + cols.get(sColumnNmbr-1).getText()+ "|";
								if(sActVal.contains(sExpVal)) {
									bStatus = true;
									break;
								}
							}
							else if(i==sRowNmbr) {
								sActVal = cols.get(sColumnNmbr-1).getText();
								if(sActVal.equalsIgnoreCase(sExpVal)) {
									bStatus = true;
									break;
								}
							}
						}
					}
					i++;
				
			}
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+" : Please provide input correctly");
			org.testng.Reporter.log("Expected Value :: "+ sExpVal);
			org.testng.Reporter.log("Actual Value :: "+ sActVal);
			org.testng.Reporter.log("Step Status :: "+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		} catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_Cell_Data(sTestName,oEle,sObjStr, sVal,sExpVal); }
			
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			
		}
	}
	/*
	 * To verify the cell data of specified column where other column's value matches with the expected value.
	 * 
	 * Parameter 1 : test case name
	 * Parameter 2 : xPath
	 * parameter 3 : xPath in string format
	 * Parameter 4 : provide Column number to be compared  and its expected value separated by "=" , then required Column number
	 * Parameter 5 : expected value to be verified
 	 *			 e.g. "3=London" in Parameter 4 and "4=country" in Parameter 5.
	 * 
	 * */	
	
	public static void verify_RowWith_CellData(String sTestName, WebElement oEle,  String sObjStr, String sCol,String sExpVal) throws HeadlessException, IOException, AWTException, ClassNotFoundException, InterruptedException {
		
		String sDesc = "", sActVal ="",sVal=sCol, sResult = "", sColumnVal = "", flagTh = "false"; boolean bStatus = false; int sRequiredColumnNmbr = 0, sColumnNmbr = 0;
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
	
	try {
		
		sCol = Utils.Helper.validateUserInput(sTestName, sCol);
		sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
		sCol = Reporter.filterUserInput(sCol);
		
		if (sCol.contains("=")) {
			sColumnNmbr = Integer.parseInt(sCol.split("=")[0]);
			sColumnVal = sCol.split("=")[1];
			sRequiredColumnNmbr = Integer.parseInt(sExpVal.split("=")[0]);
			sExpVal = sExpVal.split("=")[1];
		}		
		else {
			Reporter.print(sTestName, sVal + "*EXIT_ON_FAIL*", sDesc +"Plese provide input correctly", "True", "False", false);
		}
		
			List<WebElement> rows = oEle.findElements(By.tagName("tr"));
			
			for(WebElement row : rows) {
				List<WebElement> cols = null;
				String htmlSource = row.getAttribute("innerHTML");
				if (htmlSource.contains("<th") ) {
					if (flagTh.equalsIgnoreCase("true")) {
						continue;
					}
					else {
					cols = row.findElements(By.tagName("th"));
					flagTh = "true";
					}
					
				}
				else {
					cols = row.findElements(By.tagName("td"));
				}
					sActVal = cols.get(sColumnNmbr-1).getText();
					if(sActVal.equalsIgnoreCase(sColumnVal)) {
						
						sResult = cols.get(sRequiredColumnNmbr-1).getText();
						if(sResult.equalsIgnoreCase(sExpVal))
							bStatus = true;
							break;
					}
			}
			
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+" : Please provide input correctly");
			org.testng.Reporter.log("Expected Value :: "+ sExpVal);
			org.testng.Reporter.log("Actual Value :: "+ sActVal);
			org.testng.Reporter.log("Step Status :: "+bStatus);
			screenShot(sTestName);
			org.testng.Reporter.log("        ");
			
		} catch(Exception e) {
			
		    if (Utils.handleIntermediateIssue()) { verify_RowWith_CellData(sTestName, oEle, sObjStr, sVal,sExpVal);}
		
		    org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
		}	
	
	}	
	
	
	

	public static void closeApp(String sTestName) throws IOException, InterruptedException, HeadlessException, AWTException {
		String sDesc = Logs.log(sTestName);
		
		try {
  				((AppiumDriver)Utils.getObjectDriver(sTestName)).close();
  				Reporter.print(sTestName,sDesc + " :: CLOSE PERFORMED");
  		
  		} catch(Exception e)
		{
  			if (Utils.handleIntermediateIssue()) { closeApp(sTestName);}
			
		
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+" :: PROBLEM IN CLOSING");
			org.testng.Reporter.log("        ");
		}
  	}
	/*
	 * User can type into an already typed input box without erasing the previous typed data if needed 
	 * 
	 * Parameter 1 : test case name
	 * parameter 2 : xPaath
	 * Parameter 3 : xPath in string format
	 * Parameter 4 : User has to provide the text that needs to be sent to the Web Element
	 * 
	 * */	
	
	public static void type_WithoutClear(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {

		String sDesc = Logs.log(sTestName);
		try {

			sVal =  Utils.Helper.validateUserInput(sTestName, sVal);
					oEle.sendKeys(sVal);
		
			org.testng.Reporter.log("--------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc+" :: Performed");
			org.testng.Reporter.log("        ");

		} 
		catch(Exception e) {

		if (Utils.handleIntermediateIssue()) { 
			type_WithoutClear(sTestName, oEle, sObjStr, sVal); }
		Reporter.printError(sTestName, e, sDesc);
		}
	}
	
	public static void select_By_Index(String sTestName, WebElement oEle, String sObjStr, String sOption) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 
			sOption = Utils.Helper.validateUserInput(sTestName, sOption);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				Select oSelect = new Select(oEle);
		        oSelect.selectByIndex(Integer.parseInt(sOption)-1);            
	
				
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc+" :: Performed");
				org.testng.Reporter.log("        ");
			}
			else
				Reporter.print(sTestName, sDesc + " :: Error : This Method Works only for Html : 'Select' tag");
	
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { select_By_Index(sTestName, oEle, sObjStr, sOption); }
			Reporter.printError(sTestName, e, sDesc);
		}
	}
	// takes screenshot
	public static void screenShot(String sTestName) {
		WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
        File srcFile=((TakesScreenshot)lDriver).getScreenshotAs(OutputType.FILE);
        String timestamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        File destFile= new File(System.getProperty("user.dir") + "/com.automatics.packages/com/automatics/packages/screenshots/"+sTestName+"_"+timestamp+".png");
        try {
               FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
               e.printStackTrace();
        }
        org.testng.Reporter.log("SCREENSHOT :: <a href='"+destFile+"'> <img_src='"+destFile+"' height='100', width='100' />image_link</a>");
	}
	
	
	private static class Helper {
		
		
		static boolean IsKeyboardPresent () throws Exception {
			String checkKeyboardCommand = "adb shell dumpsys input_method";
	        boolean presentFlg = false;
	        
	        try {
	            Process process = Runtime.getRuntime().exec(checkKeyboardCommand);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            
	            int read; char[] buffer = new char[4096]; StringBuffer output = new StringBuffer();
	            while ((read = reader.read(buffer)) > 0) {
	                output.append(buffer, 0, read);
	            }
	            reader.close(); process.waitFor();
	            if (output.toString().contains("mInputShown=true"))
	            presentFlg = true;
	                
			}	catch(Exception e) {
					throw e;
				
			}
	        return presentFlg; 
		}
		
		private static AppiumDriver launchNativePerfecto(String sTestName) throws Exception {
			  
			
			AppiumDriver driver = null;
			try { 
				
				DesiredCapabilities oCap = new DesiredCapabilities();
				oCap=set_NativeCapabilities(sTestName);
				oCap.setCapability("deviceConnectUserName", Utils.hEnvParams.get("devConnUserID"));
				oCap.setCapability("deviceConnectApiKey", Utils.hEnvParams.get("devConnAPIKey"));
				oCap.setCapability("user", Utils.hEnvParams.get("Perfecto_User"));
				oCap.setCapability("password", Utils.hEnvParams.get("Perfecto_Password"));
				URL sURL = new URL(Utils.hEnvParams.get("Cloud_IP")+"/perfectomobile/wd/hub");
				driver = new AndroidDriver(sURL, oCap); 
				
			}catch(Exception e)	{
					
				throw e;
			}
				return driver;
		}
		
	
		 private static AppiumDriver launchNativeMobileLabs(String sTestName) throws Exception {
			 AppiumDriver driver = null;
				try { 
				   					
					DesiredCapabilities oCap = new DesiredCapabilities();
					oCap=set_NativeCapabilities(sTestName);
					oCap.setCapability("deviceConnectUserName", Utils.hEnvParams.get("Dev_ConnUserID"));
					oCap.setCapability("deviceConnectApiKey", Utils.hEnvParams.get("Dev_ConnAPIKey"));
					
					URL sURL = new URL("http://"+Utils.hEnvParams.get("Cloud_IP")+"/Appium");
					driver = new AndroidDriver(sURL, oCap); 
						
					
			} catch(Exception e) {
				throw e;
			}
			return driver;
		}

		static boolean waitForElement(Object driver, WebElement oEle,int Seconds) {
				
			try {
				AppiumDriver lDriver=(AppiumDriver)driver;
				WebDriverWait wait = new WebDriverWait(lDriver, Seconds);
				wait.until(ExpectedConditions.visibilityOf(oEle));
				return true;	
				
			} catch(Exception e) {
				return false;
			}
		}

		
		 private static AppiumDriver launchNativePhysical(String sTestName) throws Exception {

				AppiumDriver driver = null;
				try { 
				   
					String sPackageFldr = Utils.hSystemSettings.get("packageFolder"); 	
					DesiredCapabilities oCap = new DesiredCapabilities();
					oCap=set_NativeCapabilities(sTestName);
					oCap.setCapability("chromedriverExecutable", sPackageFldr+"chromedriver.exe");
					
					startAppium(sTestName);
					URL sURLNative = new URL("http://localhost:"+ Utils.ports_map.get(sTestName) +"/wd/hub");
					driver = new AndroidDriver(sURLNative, oCap); 	
					
				}
				catch(Exception e) {
					throw e;
				}
			return driver;
		
		}
		
	  	public static void startAppium(String sTestName) throws IOException, InterruptedException {
	  		String sStartAppium="\""+Utils.hEnvParams.get("NodeExe_Path")+"\" \""+Utils.hEnvParams.get("AppiumJS_Path")+"\" -p "+Utils.ports_map.get(sTestName)+" -bp "+(Utils.ports_map.get(sTestName)+1000)+" -U "+Utils.h2TestName_TestParams.get(sTestName).get("exePlatform")+" --chromedriver-port "+(Utils.ports_map.get(sTestName)+2000);
			List<String> command=Arrays.asList("cmd.exe", "/K","start","cmd","/K",sStartAppium);
			Utils.Helper.executeRunCommand(command, true);
			Thread.sleep(2*10000);
	  	}
		private static DesiredCapabilities set_NativeCapabilities(String sTestName) throws Exception {
			
			DesiredCapabilities oCap = new DesiredCapabilities();
			oCap.setCapability("deviceName", "Android"); 
			oCap.setCapability("platformName", "Android");
			oCap.setCapability("udid", Utils.h2TestName_TestParams.get(sTestName).get("exePlatform"));
			oCap.setCapability("deviceConnectIgnoreSession", true);
		
			oCap.setCapability("newCommandTimeout", Utils.hEnvParams.get("OBJ_TIMEOUT"));
	
			if (Android_Web.h2AndroidPrefs.containsKey(sTestName))
			{	
											
				//if (Android.h2AndroidPrefs.get(sTestName).containsKey("App_WaitActivity") && Android.h2AndroidPrefs.get(sTestName).containsKey("App_Package") && Android.h2AndroidPrefs.get(sTestName).containsKey("App_Activity"))	{
				if (Android_Web.h2AndroidPrefs.get(sTestName).containsKey("App_Package") && Android_Web.h2AndroidPrefs.get(sTestName).containsKey("App_Activity"))	{	
					//oCap.setCapability("appWaitActivity", Android.h2AndroidPrefs.get(sTestName).get("App_WaitActivity").replaceAll("|", ","));
					oCap.setCapability("appPackage", Android_Web.h2AndroidPrefs.get(sTestName).get("App_Package")); 
					oCap.setCapability("appActivity", Android_Web.h2AndroidPrefs.get(sTestName).get("App_Activity")); 
					}
				else {
					throw new Exception(sTestName+"Error : App Details are not complete");
				}
			}
			else {
				throw new Exception(sTestName+"Error : You Need to fill App Details :  App_Package , App_Activity ");
			}
			return oCap;
		}
	 
	
		
		
	static String CheckExist(Object lDriver, WebElement objDesc) throws Exception {
		String sRes="False";
		AppiumDriver driver=(AppiumDriver)lDriver;
		try{
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", objDesc);
				if (objDesc.getTagName() != null)
					sRes = "True";
			} catch (Exception ex) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", objDesc);
				if (objDesc.getTagName() != null)
					sRes = "True";
			}
		}catch(Exception e) {
			throw e;
		}
		return sRes;
	}
	
	@SuppressWarnings("unchecked")
	static void CheckPopUp_Object(String sTestName,WebElement oEle,String sObjStr) throws Exception
	{    
		AppiumDriver lDriver= (AppiumDriver)Utils.getObjectDriver(sTestName);
		try
		{
			if (lDriver.findElement(By.tagName("iframe")).getTagName()!=null )
		    { 
				List<WebElement> iframes=lDriver.findElements(By.tagName("iframe"));
				for(int i=0;i<iframes.size();i++)
				{
						if (iframes.get(i).isDisplayed()) {
							String FrameName = iframes.get(i).getAttribute("name");
							if(iframes.get(i).getAttribute("name").equals("")){
								FrameName = iframes.get(i).getAttribute("id");
								if(iframes.get(i).getAttribute("id").equals("")){
									FrameName = iframes.get(i).getAttribute("class");}
								
								}
							lDriver.switchTo().frame(FrameName);
							if (Helper.CheckExist(lDriver,oEle).contains("True")) {break;}	
							lDriver.switchTo().defaultContent();			
						}		
				}			
		    }
		} catch(Exception e) {
			throw e;
		}
	}
	

	@SuppressWarnings("unused")
	static void forceEnter(AppiumDriver driver, WebElement oEle, String val) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].value="+val+";", oEle);
	}
	
	static void forceClick(AppiumDriver driver, WebElement oEle) {
		
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", oEle);
	}
	
	@SuppressWarnings("unused")
	static boolean isDisplayed(WebElement oEle) {
		return oEle.isDisplayed();
	}
	
	 static void checkReady( String sTestName, WebElement oEle) throws InterruptedException, HeadlessException, IOException, AWTException	{
		
			Thread.sleep(1000);
		
			AppiumDriver lDriver = (AppiumDriver)Utils.getObjectDriver(sTestName);
			WebDriverWait wait = new WebDriverWait(lDriver, Long.parseLong(Utils.hEnvParams.get("OBJ_TIMEOUT")));
			wait.until(ExpectedConditions.elementToBeClickable(oEle));
	}
		
	
}



		
}
	