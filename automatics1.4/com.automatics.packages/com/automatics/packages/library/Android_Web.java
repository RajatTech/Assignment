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

@SuppressWarnings({"all"})
public class Android_Web 
{
	public static Hashtable<String, Hashtable<String, String>> h2AndroidPrefs = new Hashtable<String, Hashtable<String, String>>();	

	// Methods Exposed To User
	

	
	/**
	 * @param sTestName
	 * @param sBrowserPref
	 * @return
	 * @throws Exception
	 */
	public static Object awLaunchWeb(String sTestName, String sUrl, String sBrowserReference) throws Exception { 
		WebDriver driver = null;
		String sDesc = Logs.log(sTestName);
		
		try
		{	
				sUrl = Utils.Helper.validateUserInput(sTestName, sUrl);
				driver = Helper.awLaunchWeb(sTestName,sUrl,sBrowserReference);
				org.testng.Reporter.log("----------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("Url launched :: "+sUrl);
				org.testng.Reporter.log("                     ");

		}
		catch (Exception ex) {
			
			org.testng.Reporter.log(sTestName+" --- "+ex + " -- "  + sDesc);
		}
		
		return driver;
	
	}
	//While running multiple browser in one test script user can switch between browsers by using this method
	
	public static Object awSet_CurrentReference(String sTestName, String sBrowserReference) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
	return Utils.Set_CurrentObjectReference(sTestName, sBrowserReference);
	}
	//Sets the android browser app details provided by user
	
	public static void awSet_AndroidPreference(String sTestName, String sPreferences, String sValue) throws HeadlessException, IOException, AWTException, InterruptedException { // TODO - Expose List of Prefs
		Hashtable <String, String > hPref = new Hashtable<>();
		hPref.put(sPreferences, sValue);
		
		String sDesc = Logs.log(sTestName);
	
		try { 
			sPreferences=Utils.Helper.validateUserInput(sTestName, sPreferences);
			sValue=Utils.Helper.validateUserInput(sTestName, sValue);
			if (h2AndroidPrefs.containsKey(sTestName))
			{
				h2AndroidPrefs.get(sTestName).put(sPreferences, sValue);
				
			}
			else {
				h2AndroidPrefs.put(sTestName,hPref);
				
			}
			org.testng.Reporter.log("------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Preference :: "+sPreferences+"   preferenceValue  ::"+sValue );
			org.testng.Reporter.log("                     ");

					
		} catch (Exception ex) {
			
			org.testng.Reporter.log(sTestName+" --- "+ex + " -- "  + sDesc);
		}
	}
	
	public static void awset_CurrentPlatform(String sTestName,String sExeType,String sExe_platfrom) throws HeadlessException, IOException, AWTException {
		try{
			Utils.set_CurrentPlatform(sTestName, sExeType, sExe_platfrom);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	//Verifies the text of the alert box present
	public static boolean awVerify_Alert(String sTestName, String sExpAlertTxt) throws HeadlessException, IOException, AWTException, InterruptedException  {

		String sDesc, sActVal, sExpVal; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		
		try { 	
			sExpAlertTxt = Utils.Helper.validateUserInput(sTestName, sExpAlertTxt);
			sExpVal = Reporter.filterUserInput(sExpAlertTxt);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);

			sActVal = lDriver.switchTo().alert().getText();
			lDriver.switchTo().alert().accept();			
			
			bStatus = sExpVal.equals(sActVal);
			if(bStatus == true) {
				org.testng.Reporter.log("------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("Expected Alert :: "+sExpAlertTxt);
				org.testng.Reporter.log("Expected Value :: "+sExpVal);
				org.testng.Reporter.log("Actual Value :: "+sActVal);
				screenShot(sTestName);
				org.testng.Reporter.log("                     ");
			}
			else 
			{
				org.testng.Reporter.log("-------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("Expected Alert :: "+sExpAlertTxt);
				org.testng.Reporter.log("Expected Value :: "+sExpVal);
				org.testng.Reporter.log("Actual Value :: "+sActVal);
				screenShot(sTestName);
				org.testng.Reporter.log("                                ");
				Assert.assertFalse(bStatus, "FAILED AT STEP :: "+sDesc);
				Assert.fail();
			}	
		} 
		catch(Exception e)
		{
			
	    	if (Utils.handleIntermediateIssue())
	    	{ 
	    		awVerify_Alert(sTestName, sExpAlertTxt); 
	    	}
			Assert.assertFalse(bStatus, "EXCEPTION AT STEP :: "+sDesc);
	    	}
		return bStatus;
		}
		
	//Verifies whether alert is present or not. 
	@SuppressWarnings("unused")
	public static boolean awVerify_AlertPresent(String sTestName, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException {
		
		String sDesc, sExpVal ; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		
		try { 
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sExpVal = Reporter.filterUserInput(sVal);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);

			WebDriverWait wait = new WebDriverWait(lDriver, 20);
			
			try {
				if (wait.until(ExpectedConditions.alertIsPresent()) != null)
				{ 
					bStatus = true;
					}
				if(bStatus)
				{
				org.testng.Reporter.log("----------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc+":: Alert present");
				screenShot(sTestName);
				org.testng.Reporter.log("                      ");
				}
		    	
			}
			catch (Exception e) {}
					
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_AlertPresent(sTestName,sVal); }
	    	org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("                      ");
			Assert.assertFalse(bStatus, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();		}
		return bStatus;
	}
	
	public static void awClear(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		 
		String sDesc = "" ; 
			
		try { 
			
			sDesc = Logs.log(sTestName) + " Clear in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr)  + " ) " ;
			Helper.checkReady(sTestName, oEle);
			oEle.clear(); Thread.sleep(500L); 
			org.testng.Reporter.log(sTestName+"  ::  " +sDesc + " :: Performed");
		    }
		catch(Exception e) {
			
	    		if (Utils.handleIntermediateIssue()) { awClear(sTestName, oEle, sObjStr); }
	    		org.testng.Reporter.log("-----------------------------------------------");
				org.testng.Reporter.log("STEP :: "+sDesc);
				org.testng.Reporter.log("Exception :: "+e);
				screenShot(sTestName);
				org.testng.Reporter.log("     ");
				Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
				Assert.fail();
				}
	}
	
	//Verifies the value of the attribute in given object.
	
	public static boolean awVerify_Attribute(String sTestName, WebElement oEle, String sObjStr, String sAttribName,String sExpVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc, sActVal, sVal=sAttribName; boolean bStatus = false;
	
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		
		try { 	
			sAttribName = Utils.Helper.validateUserInput(sTestName, sAttribName);
			sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
			sAttribName = Reporter.filterUserInput(sAttribName);
			
			Helper.checkReady(sTestName, oEle);
			

			sActVal= oEle.getAttribute(sAttribName);
			
			bStatus = sExpVal.equals(sActVal);
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log(sAttribName + "---"+"ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Attribute present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log(sAttribName + "---"+"ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_Attribute(sTestName, oEle, sObjStr,sVal, sExpVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
					}
		return bStatus;
	}
	
	//	Verifies whether the object is selected or not.
	
	public static boolean awVerify_Checked(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; boolean bStatus = false;
	
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		
		try { 	
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			Reporter.filterUserInput(sVal);
			Helper.checkReady(sTestName, oEle);
			
			if (oEle.isSelected()) {
				bStatus = true;
			}
			if(bStatus)
			{
			org.testng.Reporter.log("STEP :: "+sDesc+":: Element selected");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("STEP :: "+sDesc+":: Element not selected");
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}
					
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_Checked(sTestName, oEle, sObjStr, sVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
					}
		return bStatus;
	}
	//Verifies the value of the cookie.
	public static boolean awVerify_Cookies(String sTestName,String sCookieName, String sExpVal) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc, sActVal,sVal=sCookieName; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName);
		
	    
		try { 
			sCookieName = Utils.Helper.validateUserInput(sTestName, sCookieName);
			sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
			sCookieName = Reporter.filterUserInput(sCookieName);
		    
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			sActVal = lDriver.manage().getCookieNamed(sCookieName).getValue();
		
			bStatus = sExpVal.equals(sActVal);
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log(sCookieName + "---"+"ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Attribute present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log(sCookieName + "---"+"ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}			
						
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_Cookies(sTestName, sVal,sExpVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
					}
		return bStatus;
	}
	//Verifies whether object is Enabled or not. 
	public static boolean awVerify_Editable(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; boolean bStatus = false; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
				
		try { 	
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			Reporter.filterUserInput(sVal);
			Helper.checkReady(sTestName, oEle);
			
			if (oEle.isEnabled()) { 
				bStatus = true;
			}
			if(bStatus)
			{
			org.testng.Reporter.log("STEP :: "+sDesc+":: Element editable");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("STEP :: "+sDesc+":: Element not editable");
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_Editable(sTestName, oEle, sObjStr, sVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
					}
		return bStatus;
	}
	/* It Works for following 3 scenarios: 

		• To verify the Height of the object.

		• To verify the width of the object.

		• To verify both height and width of the object.
	*/
	public static boolean awVerify_Object_Dimension(String sTestName, WebElement oEle, String sObjStr, String sDimenType,String sDimenVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc, sActVal = null,sVal=sDimenType; boolean bStatus = false;
			
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
	    
		try { 
			sDimenType = Utils.Helper.validateUserInput(sTestName, sDimenType);
			sDimenVal = Utils.Helper.validateUserInput(sTestName, sDimenVal);
			sDimenType = Reporter.filterUserInput(sDimenType);
			
		    Helper.checkReady(sTestName, oEle);
						
		    if (sDimenType.equalsIgnoreCase("Height")) {
		    	sActVal = String.valueOf(oEle.getSize().getHeight());
		    	
		    } else if (sDimenType.equalsIgnoreCase("Width")) {
		    	sActVal = String.valueOf(oEle.getSize().getWidth());
		    	
		    } else if (sDimenType.equalsIgnoreCase("Both")) {
		    	
		    	sActVal = String.valueOf(oEle.getSize().getHeight()) + "|" + String.valueOf(oEle.getSize().getWidth());
		    }
		    
		    bStatus = sDimenVal.equals(sActVal);
		    
		    org.testng.Reporter.log("DimensionType  ::"+sDimenType+"  DimesnionValue  ::"+sDimenVal+"    AutualValue ::"+sActVal);
             Assert.assertEquals(bStatus, true);
		    
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) {  awVerify_Object_Dimension(sTestName, oEle, sObjStr, sVal,sDimenVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
					}
		return bStatus;
	}
	
	//Verifies whether Element Is Displayed on page or not.
	
	public static boolean awVerify_ElementPresent(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; boolean bStatus = false; 
			
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
			
		try { 	
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			Reporter.filterUserInput(sVal);
			
			WebDriver driver = (WebDriver)Utils.getObjectDriver(sTestName);
			try {
				
				bStatus = Helper.waitForElement(driver,oEle, Integer.parseInt(Utils.hEnvParams.get("OBJ_TIMEOUT")));
				if(bStatus)
				{
				org.testng.Reporter.log("STEP :: "+sDesc+":: Element present");
				screenShot(sTestName);
				Assert.assertEquals(true, bStatus);
				}
				else
				{
					org.testng.Reporter.log("STEP :: "+sDesc+":: Element not present with given identifier");
				    Assert.assertEquals(true, bStatus);
				    screenShot(sTestName);
				}	
			
			}
			catch (NoSuchElementException e) {
				org.testng.Reporter.log("STEP :: "+sDesc+":: Element not present with given identifier  ::" +e);
				}
			
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_ElementPresent(sTestName, oEle, sObjStr, sVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
		return bStatus;
	}
	//Verifies the text given as argument matches the actual text of the object.
	public static boolean awVerify_Text(String sTestName, WebElement oEle, String sObjStr, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
				
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
	
		try { 
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sExpVal = Reporter.filterUserInput(sExpText);
			Helper.checkReady(sTestName, oEle);
			
			sActVal = oEle.getText();
			bStatus = sExpVal.equalsIgnoreCase(sActVal);
			
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("ExpectedText ::"+sExpVal + "--ActualValue  ::"+sActVal);
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("ExpectedText ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}			
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_Text(sTestName, oEle, sObjStr, sExpText); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
		return bStatus;
		
	}
	//Checks if two values provided are equal or not. It is Case-Sensitive.
	public static boolean awEquals(String sTestName, String sExpVal,String sActVal) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = "",sVal=sExpVal; boolean bStatus = false;
	
		try { 
	    	
			sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
			sActVal = Utils.Helper.validateUserInput(sTestName, sActVal);
			sDesc = Logs.log(sTestName) + " :: Values - " + sExpVal +" And "+ sActVal;
			sExpVal = Reporter.filterUserInput(sExpVal);
		    
		    bStatus = sExpVal.equals(sActVal);
		    if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}		
						
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awEquals(sTestName, sVal,sActVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}		return bStatus;
	}
	//Verifies whther the value given as argument is present in html code of the page or not.
	public static boolean awVerifyIn_HTMLSource(String sTestName, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc, sExpVal; boolean bStatus = false;
		sDesc = Logs.log(sTestName) + " :: Values - " + sExpText;
		sExpVal = Reporter.filterUserInput(sExpText);
		
		try { 	
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			
			bStatus = lDriver.getPageSource().contains(sExpVal);
			
			org.testng.Reporter.log("Expected Value ::"+sExpVal+"   Actual Value ::"+lDriver.getPageSource());
						
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerifyIn_HTMLSource(sTestName, sExpText); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
		return bStatus;
	}
	//Verifies the current URL of the Browser contains expected URL given as argument.
	public static boolean awVerifyIn_URL(String sTestName, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		try { 	
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sExpVal = Reporter.filterUserInput(sExpText);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			
			sActVal = lDriver.getCurrentUrl();
			bStatus = sActVal.contains(sExpVal);
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("ExpectedUrl ::"+sExpVal + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Attribute present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("ExpectedUrl ::"+sExpVal + "--ActualValue  ::"+sActVal);
				Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}					
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerifyIn_URL(sTestName, sExpText); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			
		}
		return bStatus;
	}
	//Verifies whether the options given as argument are present in SelectBox or not. 
	public static boolean awVerify_SelectOptions(String sTestName, WebElement oEle, String sObjStr, String sExpOptions) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		
		try { 	
			
			sExpOptions = Utils.Helper.validateUserInput(sTestName, sExpOptions);
			sExpVal = Reporter.filterUserInput(sExpOptions);
			Helper.checkReady(sTestName, oEle);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				
				Select selList = new Select(oEle);
				List<WebElement> oSize = selList.getOptions();
				for(int i =0; i<oSize.size(); i++) { 
					sActVal = sActVal + "|" + selList.getOptions().get(i).getText(); 
				}		
				
				for (String sExpOption: sExpVal.split("\\|")) {
					
					if (!sActVal.contains(sExpOption))  {
						bStatus = false; break;
					} else 
						bStatus = true;
				}
				
							}
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Attribute present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}		
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_SelectOptions(sTestName, oEle, sObjStr, sExpOptions); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
		return bStatus;
	}
	//Verifies whther the options provided in argument are selected in the SelectBox.
	public static boolean awVerify_SelectedOptions(String sTestName, WebElement oEle, String sObjStr, String sExpOptions) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try {
			
			sExpOptions = Utils.Helper.validateUserInput(sTestName, sExpOptions);
			sExpVal = Reporter.filterUserInput(sExpOptions);
			Helper.checkReady(sTestName, oEle);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				
				Select selList = new Select(oEle); 
				List<WebElement> oSize = selList.getAllSelectedOptions();
				for(int i =0; i<oSize.size(); i++) { 
					sActVal = sActVal + "|" + selList.getAllSelectedOptions().get(i).getText(); 
				}		
				
				for (String sExpOption: sExpVal.split("\\|")) {
					
					if (!sActVal.contains(sExpOption)) 
					{
						bStatus = false; break;
					} 
					else 
						bStatus = true;
				}
				
			}
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Attribute present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}		
		}

          catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_SelectedOptions(sTestName, oEle, sObjStr, sExpOptions); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
		return bStatus;
	}
	//Verifies that text given as argument is present anywhere on Page.
	public static boolean awVerify_TextPresent(String sTestName, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = "", sExpVal; boolean bStatus = false;
		
		try { 
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sDesc = Logs.log(sTestName) + " :: Validating text '" + sExpText + "'";
			sExpVal = Reporter.filterUserInput(sExpText);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			
			List<WebElement> oList = lDriver.findElements(By.xpath("//*[contains(text(),'" + sExpVal + "')]"));
			
			if (oList.size() > 0) bStatus = true;
			
			//Reporter.print(sTestName, sExpText, sDesc, "NA", "NA", bStatus, Helper.takeScreenshot(lDriver));
			if(bStatus)
			{
				org.testng.Reporter.log("Element present with given text");
				Assert.assertEquals(true, bStatus);
			}
			else
			{
				Assert.assertEquals(true, bStatus);
				org.testng.Reporter.log("Element dosen't exist with given text");
			}
		}  
		catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_TextPresent(sTestName, sExpText); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
					}
		return bStatus;
	}
	
	//Verifies the current title of the Browser contains the expected title given as argument.

	public static boolean awVerifyIn_Title(String sTestName, String sExpTitle) throws HeadlessException, IOException, AWTException, InterruptedException , InterruptedException {
		
		String sDesc = "", sExpVal; boolean bStatus = false;
		
		try { 	
			
			sExpTitle = Utils.Helper.validateUserInput(sTestName, sExpTitle);
			sDesc = Logs.log(sTestName) + " :: Validating Title '" + sExpTitle + "'";
			sExpVal = Reporter.filterUserInput(sExpTitle);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			
			if (lDriver.getTitle().contains(sExpVal))
		
				bStatus = true;
			
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("ExpectedTitle ::"+sExpVal + "--ActualValue  ::"+lDriver.getTitle());
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("ExpectedTitle ::"+sExpVal + "--ActualValue  ::"+lDriver.getTitle());
				 Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}				
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerifyIn_Title(sTestName, sExpTitle); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
		return bStatus;
	}
	
	//Verifies whether object is displayed or not
	public static boolean awVerify_Visible(String sTestName, WebElement oEle, String sObjStr, String sUserVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 	
			
			sUserVal = Utils.Helper.validateUserInput(sTestName, sUserVal);
			Reporter.filterUserInput(sUserVal);
			Helper.checkReady(sTestName, oEle);
			
			if (oEle.isDisplayed())
				bStatus = true;
			
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("Element displayed");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("Element is not displayed");
				Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}					
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_Visible(sTestName, oEle, sObjStr, sUserVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
		return bStatus;
	}
	//To select/Deselect a checkBox/radiobutton.
	public static void awCheck_Uncheck(String sTestName, WebElement oEle, String sObjStr, String sUserVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; 
			
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			sUserVal = Utils.Helper.validateUserInput(sTestName, sUserVal);	
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);

			if (sUserVal.equalsIgnoreCase("true")) {
				
				if (!oEle.isSelected()) {
					Helper.forceClick(lDriver, oEle);
				}
			} else if (sUserVal.equalsIgnoreCase("false")) {
				
				if (oEle.isSelected()) {
					Helper.forceClick(lDriver, oEle);
				}
			}
				
					
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awCheck_Uncheck(sTestName, oEle, sObjStr, sUserVal); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
	}
	//Click on given Object.
	public static void awClick(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc = "";
				
		sDesc = Logs.log(sTestName) + " on Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		
		try { 	
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			try {
				
				Helper.forceClick(lDriver, oEle);
			}
			catch (Exception e) {
				oEle.click();
			}
			
			org.testng.Reporter.log(sTestName+"---"+ sDesc + " :: Performed");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awClick(sTestName, oEle, sObjStr); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
	}
	//Prints the message in logs and HTML report.
	public static void awPrint(String sTestName, String sTxt) throws HeadlessException, IOException, AWTException, InterruptedException, InvalidInputException  {

		String sDesc = Logs.log(sTestName);
		sTxt = Utils.Helper.validateUserInput(sTestName, sTxt);	
		org.testng.Reporter.log(sTestName+" ::"+ sDesc + " :: '" + sTxt + "'");
	}
	//Navigates one page Back.
	public static void awGoBack(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; 
		sDesc = Logs.log(sTestName);
		
		try { 	
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			lDriver.navigate().back();

			org.testng.Reporter.log(sTestName+"---"+ sDesc + " :: Performed");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awGoBack(sTestName); }
	    	org.testng.Reporter.log("-----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+sDesc);
			org.testng.Reporter.log("Exception :: "+e);
			screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+sDesc);
			Assert.fail();
			}
	}
	//Opens the URL provided as argument.
	public static void awOpenURL(String sTestName, String sURL) throws HeadlessException, IOException, AWTException, InterruptedException  {
		

		String sDesc = ""; 
		
		try {
		
			sURL = Utils.Helper.validateUserInput(sTestName, sURL);
		
			sDesc = Logs.log(sTestName) + ":  " + sURL;
		
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			lDriver.get(sURL);
			org.testng.Reporter.log(sTestName+"---"+ sDesc + " :: Performed");
			
		} catch ( Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awOpenURL(sTestName, sURL); }
			org.testng.Reporter.log(sTestName+":"+e+" :: " +sDesc );
		}
		 
	}
	//To refresh the browser.
	public static void awRefresh(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; 
		sDesc = Logs.log(sTestName);
		
		try { 	
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			lDriver.navigate().refresh();

			org.testng.Reporter.log(sTestName+"---"+ sDesc + " :: Performed");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awRefresh(sTestName); }
	    	org.testng.Reporter.log(sTestName+":"+e+" :: " +sDesc );
		}
	}
	//Select multiple options in SelectBox.
	public static void awMultiSelect(String sTestName, WebElement oEle, String sObjStr, String sOptions) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

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
				org.testng.Reporter.log(sTestName+"---"+ sDesc + " :: Performed");
			}
			else
				org.testng.Reporter.log(sTestName+"---"+ sDesc + " :: Error : This Method Works only for Html : 'Select' tag");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awMultiSelect(sTestName, oEle, sObjStr, sOptions); }
					}
	}
	
	//Select option from SelectBox.
	public static void awSelect(String sTestName, WebElement oEle, String sObjStr, String sOption) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 
			sOption = Utils.Helper.validateUserInput(sTestName, sOption);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				Select oSelect = new Select(oEle);
		        oSelect.selectByVisibleText(sOption);            
	
				org.testng.Reporter.log(sTestName+"---"+ sDesc + " :: Performed");
			}
			else
				org.testng.Reporter.log(sTestName+"---"+ sDesc + " :: Error : This Method Works only for Html : 'Select' tag");
	
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awSelect(sTestName, oEle, sObjStr, sOption); }
	    	org.testng.Reporter.log(sTestName+"---"+ e);

		}
	}
	//Verifies whether frame given as argument is present in the page or not 
	public static void awVerify_Frame(String sTestName, String sFrameInfo) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc, sExpVal; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		try { 	
			
			sFrameInfo = Utils.Helper.validateUserInput(sTestName, sFrameInfo);
			sExpVal  = Reporter.filterUserInput(sFrameInfo);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
		
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
			
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log(  sFrameInfo+"frame is present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log(sFrameInfo+"frame is not present");
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}							
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_Frame(sTestName, sFrameInfo); }
	    	org.testng.Reporter.log(sFrameInfo+ "::"+ e+ "  ::frame is not present");
			
		}
	}
	 /* Switches the handle to Frame/Iframe provided as argument.
       It switches to first frame displayed if argument is left empty.	
	*/
	public static void awSwitch_To_Frame(String sTestName, String sFrameInfo) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; boolean bStatus = false;
		sDesc = Logs.log(sTestName);
		
		try { 	
			
			sFrameInfo = Utils.Helper.validateUserInput(sTestName, sFrameInfo);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			
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
			
			if (!bStatus)
				org.testng.Reporter.log(sTestName+"--"+ sDesc + "  :: Frame '" + sFrameInfo + "' not found");
			else
				org.testng.Reporter.log(sTestName+"--"+ sDesc + "  :: Performed - '" + sFrameInfo + "'");
					
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awSwitch_To_Frame(sTestName, sFrameInfo); }
	    	org.testng.Reporter.log(sTestName+"---"+ e+"----" +sDesc+ Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Switches the handle back to default content.
	public static void awSwitch_To_Default(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; 
		sDesc = Logs.log(sTestName);
		
		try { 	
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);

			lDriver.switchTo().defaultContent();
			org.testng.Reporter.log(sTestName+"----"+ sDesc + "  :: Performed");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awSwitch_To_Default(sTestName); }
	    	org.testng.Reporter.log(sTestName+"----"+e+"  ::"+sDesc+ Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	/*
	It Works for following 3 scenarios: 
		• If User Does not give any Window information ,current window is stored as ParentBrowser and driver is switched to other window available 
		  and stores it as ChildBrowser
	
        • If User gives "Parent" as argument , driver is switched to ParentBrowser if it was stored earlier, 
	    otherwise it switches to other window available and stores it as ParentBrowser.
	    • If User gives "Child" as argument , driver is switched to ChildBrowser if it was stored earlier, 
	    otherwise it switches to other window available and stores it as ChildBrowser. 
	*/
	public static void awSwitch_To_Tab(String sTestName, String sWindowInfo) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc; 
		sDesc = Logs.log(sTestName);
		
		try { 	
			sWindowInfo = Utils.Helper.validateUserInput(sTestName, sWindowInfo);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
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
			
			org.testng.Reporter.log(sTestName +"----------" +sDesc + "  :: Performed - '" + sWindowInfo + "'");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awSwitch_To_Tab(sTestName, sWindowInfo); }
	    	org.testng.Reporter.log(sTestName+"------"+ e+"===="+ sDesc+ Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Finds the value of the attribute in given object and stores in the Variable Name provided in argument.
	public static void awStore_Attribute(String sTestName, WebElement oEle, String sObjStr, String sComputedVal,String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
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
				
			org.testng.Reporter.log(sTestName+"=======" +sDesc + " :: Store (Attribute-" + sComputedVal + ", Value-" + sActVal + ") in Local Variable '" + sVarName + "'");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awStore_Attribute(sTestName, oEle, sObjStr,sComputedVal,sVarName ); }
	    	org.testng.Reporter.log(sTestName+"========"+e+"======="+ sDesc+  Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	
	//Stores the current URL in Variable Name provided as argument.

public static void awStore_URL(String sTestName, String sUserVal) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = "", sActVal, sVarName; 
		
		
		try { 
			sUserVal = Utils.Helper.validateUserInput(sTestName, sUserVal);
			
			sDesc = Logs.log(sTestName);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			
			sVarName = sUserVal;
			if (sUserVal.equals("")) {
				sVarName = "Temp";	
			}
			
			sActVal = lDriver.getCurrentUrl();
			Utils.setScriptParams(sTestName, sVarName, sActVal);
				
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + "  :: Store (Current URL -" + sActVal + ") in Local Variable '" + sVarName + "'");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awStore_URL(sTestName, sUserVal); }
	    	org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	
	}
    //Stores all options of SelectBox in Variable Name provided as argument
	public static void awStore_SelectOptions(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
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
					
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"====="+ " :: Store options '" + sActVal + "' of "+ sObjStr + " in Local Variable '" + sVarName + "'");
			}
			else
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"====="+ "  :: Error : This Method Works only for Html : 'Select' tag");

		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awStore_SelectOptions(sTestName, oEle, sObjStr, sVarName); }
	    	org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+ Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Stores all Selected options of SelectBox in Variable Name provided as argument.
	public static void awStore_SelectedValue(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 	
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				
				Select selList = new Select(oEle); 
				List<WebElement> oSize = selList.getAllSelectedOptions();
				for(int i =0; i<oSize.size(); i++) { 
					sActVal = sActVal + "|" + selList.getAllSelectedOptions().get(i).getText(); 
				}		
				
				if (sVarName.equals("")) {
					sVarName = "Temp";	
				}
				
				sActVal = sActVal.substring(2);
				Utils.setScriptParams(sTestName, sVarName, sActVal);
					
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Store selected options '" + sActVal + "' of "+ sObjStr + " in Local Variable '" + sVarName + "'");
			}
			else
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + "  :: Error : This Method Works only for Html : 'Select' tag");
	
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awStore_SelectedValue(sTestName, oEle, sObjStr, sVarName); }
	    	org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+ Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Stores the vissible text of the object in Variable Name provided as argument.
	public static void awStore_Text(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			sActVal = oEle.getText(); 
			
			if (sVarName.equals("")) {
				sVarName = "Temp";	
			}
			
			Utils.setScriptParams(sTestName, sVarName, sActVal);
				
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Store '" + sActVal + "' of "+ sObjStr + " in Local Variable '" + sVarName + "'");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awStore_Text(sTestName, oEle, sObjStr, sVarName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Stores the vissible text of the object in Variable Name provided as argument.
	public static void awStore_Value(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null; 
		
		sDesc = Logs.log(sTestName) + " of Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		Helper.checkReady(sTestName, oEle);
		
		try { 	
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			sActVal = oEle.getText(); 
			
			if (sVarName.equals("")) {
				sVarName = "Temp";	
			}
			
			Utils.setScriptParams(sTestName, sVarName, sActVal);
				
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Store '" + sActVal +  " in Local Variable '" + sVarName + "'");
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awStore_Value(sTestName, oEle, sObjStr, sVarName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Types the given String into an object. It Clears the object before typing.
	@SuppressWarnings("unused")
	public static void awType(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc = "" ; boolean flag = true; 
			
		try { 
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sDesc = Logs.log(sTestName) + " in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " ) , Value = " + sVal ;
			//Helper.checkReady(sTestName, oEle);
			
			//WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			
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
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Performed");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awType(sTestName, oEle, sObjStr, sVal); }
	    	org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	/*
	Checks if a PopUp is there, finds the object in that Pop UP and Types the given String into it. 
	If PopUp is not there Finds the object on page and types into it.
	*/
	@SuppressWarnings("unused")
	public static void awType_Advanced(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {

		String sDesc = "" ; boolean flag = true; 
			
		try { 
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sDesc = Logs.log(sTestName) + " in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " ) , Value = " + sVal ;
			Helper.checkReady(sTestName, oEle);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);	
			Helper.CheckPopUp_Object(sTestName, oEle, sObjStr);
				if (Helper.CheckExist(lDriver,oEle).contains("True")) {
					if (!oEle.getAttribute("value").equals("")){
						oEle.clear();Thread.sleep(2000L);
					} 
					oEle.sendKeys(sVal); 
					
				}
				awSwitch_To_Default(sTestName);
			
			
			
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Performed");
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awType_Advanced(sTestName, oEle, sObjStr, sVal); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//It deletes all cookies of the browser.
	public static void awDeleteCookies(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException {
		
		String sDesc; 
		sDesc = Logs.log(sTestName)+" :: All Cookies Deleted";
		
		try { 	

			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
	
			lDriver.manage().deleteAllCookies();
			
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====");

		}  catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { awDeleteCookies(sTestName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Deletes  the cache of the browser.
	public static void awDeleteCache(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException {
		
		String sDesc, sBrowser, sTitle = "", vbPath; 
		sDesc = Logs.log(sTestName) + " :: Cache Deleted";
		
		try { 	
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			sBrowser = Utils.h2TestName_TestParams.get(sTestName).get("exePlatform"); 
			if(sBrowser.equalsIgnoreCase("Firefox"))
				sTitle = lDriver.getTitle() +" - Mozilla Firefox";
			else if(sBrowser.equalsIgnoreCase("IE"))
				sTitle = lDriver.getTitle() +" - Internet Explorer";
			
				
			vbPath = "\""+System.getProperty("user.dir").replace("\\", "/") +"/Packages/delcache.vbs\"";
		
			Runtime.getRuntime().exec( "cscript " +vbPath+ " \""+sTitle+ "\"");
	
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====");
			
		}  catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) {awDeleteCache(sTestName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Performs Double Click on the object given.
	public static void awDoubleClick(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException   {
		
		String sDesc; 
		
		sDesc = Logs.log(sTestName)+ " :: Double Click on Element: " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			Actions action = new Actions(lDriver);
			action.moveToElement(oEle).doubleClick().build().perform();
		
			
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====");
			
		}   catch (Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awDoubleClick(sTestName,oEle,sObjStr); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//To run another TestScript inside a TestCase.
	private static Object awRunScript(String sTestCaseName,ITestContext testContext) throws Exception   {
		
		return Utils.RunScript(sTestCaseName, testContext);
	}
	public static Object awRunScript(String sTestName,String sTestCaseName,ITestContext testContext) throws Exception   {
		
		return awRunScript( sTestCaseName, testContext);
	}
	
	//Delays execution for specified seconds
	public static void awWait(String sTestName, String secs) throws NumberFormatException, InterruptedException, HeadlessException, IOException, AWTException, InterruptedException, InvalidInputException  {
		
		secs = Utils.Helper.validateUserInput(sTestName, secs);
		String sDesc = Logs.log(sTestName)+" :: Waiting for: ("+secs+")seconds";
		Thread.sleep(Long.valueOf(secs)*1000);
		org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====");
	}
	//Waits for element to be present for specified seconds.
	public static void awWaitForElementPresent(String sTestName, WebElement oEle,  String sObjStr, String WaitTimeSec) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc = "" ;
		
		sDesc =  Logs.log(sTestName)+" :: Waiting for Element : " +sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
				
		try {
			
			WaitTimeSec = Utils.Helper.validateUserInput(sTestName, WaitTimeSec);
			long sec = Long.parseLong(WaitTimeSec);
			Helper.checkReady(sTestName, oEle);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			WebDriverWait wait = new WebDriverWait(lDriver, sec);
			wait.until(ExpectedConditions.visibilityOf(oEle));
			
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"====="+ " to be Present: ("+WaitTimeSec+")seconds");
		
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awWaitForElementPresent(sTestName,oEle,sObjStr, WaitTimeSec); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Verifies the text given as argument is present in the actual text of the object.
	public static boolean awVerify_SubStringInText(String sTestName, WebElement oEle, String sObjStr, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName)+ " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try {
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sExpVal = Reporter.filterUserInput(sExpText);	
			Helper.checkReady(sTestName, oEle);
			
			sActVal = oEle.getText();
			sActVal = sActVal.replace("\n", " ");
			bStatus = (sActVal.contains(sExpVal));	
			
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Substring present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}
		}	
		catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awVerify_SubStringInText(sTestName,oEle,sObjStr, sExpText); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
		return bStatus;
	}
	//Accepts the alert.
	public static void awAcceptAlert(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = Logs.log(sTestName)+" :: Clicking on 'OK' of Popup";
		
		try {
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			Alert alert = lDriver.switchTo().alert();	
			alert.accept();
			
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====");
		   
		}   catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awAcceptAlert(sTestName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Cancels the alert.
	public static void awCancelAlert(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = Logs.log(sTestName)+" :: Clicking on 'CANCEL' of Popup";
		
		try {
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			Alert alert = lDriver.switchTo().alert();							
		    alert.dismiss();
		    
		    org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====");
				   
		}   catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awCancelAlert(sTestName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	
	//Navigates one page Forward.
	
	public static void awGoForward(String sTestName) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = Logs.log(sTestName)+" :: Moving Forward One Page";
		
		try{
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
		    lDriver.navigate().forward();
		    
		    org.testng.Reporter.log(sTestName+"==========="+sDesc);
	
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awGoForward(sTestName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	
	public static boolean awVerify_ToolTip(String sTestName, WebElement oEle, String sObjStr, String sExpText) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal = null, sExpVal; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName)+ " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try {
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sExpVal = Reporter.filterUserInput(sExpText);
			Helper.checkReady(sTestName, oEle);
			
			sActVal = oEle.getAttribute("title").replace("\n", " "); 
			bStatus = sExpVal.equals(sActVal);
		
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Tool tip present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log("ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}			
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awVerify_ToolTip(sTestName, oEle, sObjStr, sExpText); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	    return bStatus;
	}
	
	//Verifies the value of the attribute in CSS of the object given.
	
	public static boolean awVerifyIn_CSSValue(String sTestName, WebElement oEle, String sObjStr, String sAttribute,String sExpVal)throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
		String sDesc, sActVal = null, sVal=sAttribute; boolean bStatus = false;
		
		sDesc = Logs.log(sTestName)+ " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
			
		try {
			sAttribute = Utils.Helper.validateUserInput(sTestName, sAttribute);
			sExpVal = Utils.Helper.validateUserInput(sTestName, sExpVal);
			
			sAttribute = Reporter.filterUserInput(sAttribute);
			Helper.checkReady(sTestName, oEle);
			
			sActVal = oEle.getCssValue(sAttribute);
			bStatus = sActVal.equals(sExpVal);
			
			if(bStatus)
			{
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log(sAttribute + "---"+"ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Attribute present");
			screenShot(sTestName);
			Assert.assertEquals(true, bStatus);
			}
			else
			{
				org.testng.Reporter.log(sAttribute + "---"+"ExpectedValue ::"+sExpVal + "--ActualValue  ::"+sActVal);
			    Assert.assertEquals(true, bStatus);
			    screenShot(sTestName);
			}
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awVerifyIn_CSSValue(sTestName, oEle, sObjStr,sVal,sExpVal); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	    return bStatus;
	}	
	
	//Finds the value of the attribute in CSS of given object and stores in the Variable Name provided in argument.
	public static void awStore_CSSValue(String sTestName, WebElement oEle, String sObjStr,String sComputedVal,String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 
		
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
			
			org.testng.Reporter.log("----------------------------------------------------");
			org.testng.Reporter.log("cssAttribute"+"---"+sComputedVal + "---"+"Variable ::"+sVarName + "--ActualValue  ::"+sActVal);
			org.testng.Reporter.log("STEP :: "+sDesc+":: Attribute present");
			screenShot(sTestName);
			
		}	catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { awStore_CSSValue(sTestName,oEle,sObjStr,sComputedVal,sVarName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
    }
	//Stores Current Title in Variable Name provided as argument.
	public static void awStore_Title(String sTestName, String sUserVal) throws HeadlessException, IOException, AWTException, InterruptedException  { 
		
		String sDesc, sActVal, sVarName; 
		sDesc = Logs.log(sTestName);
		
		try {	
			sUserVal = Utils.Helper.validateUserInput(sTestName, sUserVal);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);	
			sVarName = (sUserVal.equals("")) ? "Temp" : sUserVal;

			sActVal =lDriver.getTitle();
			Utils.setScriptParams(sTestName, sVarName, sActVal);
			
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Store (Current Title -" + sActVal + ") in Local Variable '" + sVarName + "'");
				
		} 	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awStore_Title(sTestName,sUserVal); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}				
    }
	//Clicks on the Text provied as Argument.
	public static void awClick_OnText(String sTestName, String sText) throws HeadlessException, IOException, AWTException, InterruptedException 	{
		
		String sDesc = Logs.log(sTestName)+" :: Click on Text: "+ sText;
		boolean foundFlg = false;
		
		try {
			
			sText = Utils.Helper.validateUserInput(sTestName, sText);
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);	
			
			List<WebElement> childEle = lDriver.findElements(By.tagName("*"));
			for(WebElement oEle : childEle){	
				if(oEle.isDisplayed()) {
					if (oEle.getAttribute("innerHTML").contains(sText)) {
						oEle.click(); foundFlg = true; break;
				    }
			    }
			}
			sDesc=Logs.log(sTestName)+" :: Click on Text: "+ sText + ", found " + String.valueOf(foundFlg);
			
		} catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awClick_OnText(sTestName, sText); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	
	//To send "enter" to an object.	
	public static void awEnter(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException {
		String sDesc = "" ;  
		
		try {
			
			sDesc = Logs.log(sTestName) + " in Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
			
			oEle.sendKeys(Keys.ENTER);
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Performed");
		
		} catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { awEnter(sTestName, oEle, sObjStr); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	
	//Stores font color of the object in Variable Name given as argument	
	public static void awStore_FontColor(String sTestName, WebElement oEle, String sObjStr, String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc, sActVal; 
		
		sDesc = Logs.log(sTestName)+" :: Reterving Font color of Obj : "+sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 
			
			sVarName = Utils.Helper.validateUserInput(sTestName, sVarName);
			
			sActVal = oEle.getCssValue("color");
			String[] numbers = sActVal.replace("rgba(", "").replace(")", "").split(",");
			int iR = Integer.parseInt(numbers[0].trim()); 
			int iG = Integer.parseInt(numbers[1].trim()); 
			int iB = Integer.parseInt(numbers[2].trim());
			sActVal = String.format("#%02x%02x%02x", iR, iG, iB).toUpperCase();
			
			sVarName = (sVarName.equals("")) ? "Temp" : sVarName;

			Utils.setScriptParams(sTestName, sVarName, sActVal);
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Store '" + sActVal + "' of "+ sObjStr + " in Local Variable '" + sVarName + "'");
		
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awStore_FontColor(sTestName,oEle,sObjStr,sVarName); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Verifies the text given as argument is present in text of object(id=errorDiv)
	public static void awVerifyIn_Error(String sTestName, String sExpText) throws Exception {	
		
		String sDesc = "";
		
		try {
			
			sExpText = Utils.Helper.validateUserInput(sTestName, sExpText);
			sDesc = Logs.log(sTestName) + " :: Validating error message( " + sExpText + " )";
			
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
		    WebElement oEle = (WebElement) lDriver.findElement(By.xpath("//*[@id='errorDiv']"));
		    awVerify_SubStringInText(sTestName, oEle, "errObj", sExpText);
			    
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====");		     
		 
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awVerifyIn_Error(sTestName,sExpText); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Opens the link in New Tab
	public static void awOpenIn_NewTab(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 
		
		String sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try{
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			Utils.setScriptParams(sTestName, "Parent_Brwsr", lDriver.getWindowHandle());

		    Actions newTab = new Actions(lDriver);
		    newTab.moveToElement(oEle); 
		    newTab.perform();
		    newTab.keyDown(Keys.CONTROL).click(oEle).build().perform();
			
			
		    org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Performed");
			
		    for(String winHandle : lDriver.getWindowHandles()){
			    lDriver.switchTo().window(winHandle);			    
			}				
			Utils.setScriptParams(sTestName, "Child_Brwsr", lDriver.getWindowHandle());
			
		}	catch(Exception e) {
	
			if (Utils.handleIntermediateIssue()) { awOpenIn_NewTab(sTestName, oEle, sObjStr); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Focuses on the object.
	public static void awFocus(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 
		
		String sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try{
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);	
			if ("input".equals(oEle.getTagName())) {
				oEle.sendKeys("");
			} else {
				new Actions(lDriver).moveToElement(oEle).perform();
			}
			
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Focussed on Obj : " + sObjStr + ")");
		 
		}	catch(Exception e) {
		
			if (Utils.handleIntermediateIssue()) { awFocus(sTestName,oEle,sObjStr); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	}
	//Checks if the value is empty or not.
	public static boolean awIs_Empty(String sTestName, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = "", sExpVal; boolean bStatus = false;
	
	    try { 	
	    	sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			sDesc = Logs.log(sTestName) + " :: Value - " + sVal;
			sExpVal = Reporter.filterUserInput(sVal);
			
		    bStatus = sExpVal.isEmpty();
		    org.testng.Reporter.log(sTestName+"========="+ sDesc+"====="+sExpVal);
			Assert.assertEquals(bStatus, true);
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awIs_Empty(sTestName, sVal); }
	    	org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"=====");	
	    	}
		return bStatus;
	}
	//Hover over the object
	public static void awMouseOver(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 
		
		String sDesc ; 
		
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try {
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
			Actions newTab = new Actions(lDriver);
			newTab.moveToElement(oEle); 
			newTab.perform();
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Performed");
		
		} catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { awMouseOver(sTestName, oEle, sObjStr);; }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	 }
	//Clicks on the Object.
	public static void awMouseClick(String sTestName, WebElement oEle, String sObjStr) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  { 

		String sDesc = "";
		
				
		sDesc = Logs.log(sTestName) + " on Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		try {
			WebDriver lDriver =(WebDriver)Utils.getObjectDriver(sTestName);
		
		
				Actions newTab = new Actions(lDriver);
				newTab.moveToElement(oEle); 
				newTab.perform();
				newTab.click(oEle).build().perform();
		
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Performed");
		}
		catch (Exception e) {
			oEle.click();
		}
		
	 }
	//Takes ScreenShot of the screen and save it as VariableName given in argument.
	public static void awScreen_Capture(String sTestName , String sUsrVal) throws HeadlessException, IOException, AWTException, InterruptedException  { 
		
		String sDesc = "" ; 
		
		try {
			sUsrVal = Utils.Helper.validateUserInput(sTestName, sUsrVal);
			sDesc = Logs.log(sTestName) + " - " + sUsrVal ;
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		
		} catch(Exception e) {
			
			if (Utils.handleIntermediateIssue()) { awScreen_Capture(sTestName, sUsrVal); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}
	 }
	
	//To Store the cell data of specified row and column in given variable name.
	public static void awGet_Cell_Data(String sTestName, WebElement oEle, String sObjStr, String sRowCol,String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
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
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"====="+ "*EXIT_ON_FAIL*"+"  : Please provide input correctly");
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
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Store Cell Data of Row: '" + sRow + "' and Column: '" + sColumn + "' in Local Variable '" + sVarName + "', Data is: '" + sActVal + "'");
			
		} catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) {  awGet_Cell_Data(sTestName, oEle, sObjStr, sVal,sVarName);}
	    	org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		
		}
	}
	
	//To Store the cell data of specified column where other column's value matches with the expected value.
	
	public static void awGet_RowWith_CellData(String sTestName, WebElement oEle, String sObjStr,String sCol,String sVarName) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {
		
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
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"====="+ "*EXIT_ON_FAIL*"+"  : Please provide input correctly");
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
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Store Value of Column: '"+ sRequiredColumnNmbr + "' where Column '" + sColumnNmbr + "' has Value: '" + sUsrVal + "' in Local Variable '" + sVarName + "' Data is: '" + sResult + "'");
				
			} catch(Exception e) {
				
			    	if (Utils.handleIntermediateIssue()) { awGet_RowWith_CellData(sTestName,  oEle, sObjStr, sVal,sVarName);}
			    	org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
					
			}
	
	}
	
	//Verifies the cell data of specified row and column.
	
	public static void awVerify_Cell_Data(String sTestName, WebElement oEle, String sObjStr,String sRowCol,String sExpVal) throws HeadlessException, IOException, AWTException, ClassNotFoundException, InterruptedException {
		
		String sDesc = null,sVal=sRowCol, sActVal ="", sColumn = null, sRow="", flagTh = "false"; boolean bStatus = false; int sColumnNmbr = 0, sRowNmbr,i=1;
		sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
		
		try { 	
			 
			sRowCol = Utils.Helper.validateUserInput(sTestName, sRowCol);
			sExpVal=Utils.Helper.validateUserInput(sTestName, sExpVal);
			sRowCol = Reporter.filterUserInput(sRowCol);
			
			Helper.checkReady(sTestName, oEle);
			sRow = sExpVal.split("::")[0];
			if (sRowCol.contains("::")) {
				sColumn = sRowCol.split("::")[1];
				sRow = sRowCol.split("::")[0];
			}
			else {
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"====="+ "*EXIT_ON_FAIL*"+"  : Please provide input correctly");
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
			
			org.testng.Reporter.log(sTestName+"===="+ sVal+ "======"+ sDesc+"=========="+ sExpVal+"====="+ sActVal+"======="+ bStatus+"===="+ Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));	
			
		} catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awVerify_Cell_Data(sTestName,oEle,sObjStr, sVal,sExpVal); }
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
			
		}
	}
	//To verify the cell data of specified column where other column's value matches with the expected value.	
	
	public static void awVerify_RowWith_CellData(String sTestName, WebElement oEle,  String sObjStr, String sCol,String sExpVal) throws HeadlessException, IOException, AWTException, ClassNotFoundException, InterruptedException {
		
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
			org.testng.Reporter.log(sTestName+"========="+ sDesc+"====="+ "*EXIT_ON_FAIL*"+"  : Please provide input correctly");
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
			org.testng.Reporter.log(sTestName+"===="+ sVal+ "======"+ sDesc+"=========="+ sExpVal+"====="+ sResult+"======="+ bStatus+"===="+ Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));	

		} catch(Exception e) {
			
		    if (Utils.handleIntermediateIssue()) { awVerify_RowWith_CellData(sTestName, oEle, sObjStr, sVal,sExpVal);}
			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+Helper.takeScreenshot((WebDriver)Utils.getObjectDriver(sTestName)));
		}	
	
	}	
	
	//Closes the Browser/App.
	public static void awcloseApp(String sTestName) throws IOException, InterruptedException, HeadlessException, AWTException {
		String sDesc = Logs.log(sTestName);
		
		try {
  				((WebDriver)Utils.getObjectDriver(sTestName)).close();
  				org.testng.Reporter.log(sTestName+"====="+ sDesc+"====="+ " :: CLOSE PERFORMED");
  		
  		} catch(Exception e)
		{
  			if (Utils.handleIntermediateIssue()) { awcloseApp(sTestName);}
			
  			org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"====="+ " :: PROBLEM IN CLOSING");
		}
  	}
	//User can type into an already typed input box without erasing the previous typed data if needed
	public static void awType_WithoutClear(String sTestName, WebElement oEle, String sObjStr, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException {

		String sDesc = Logs.log(sTestName);
		try {

			sVal =  Utils.Helper.validateUserInput(sTestName, sVal);
					oEle.sendKeys(sVal);

			org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Performed");

		} 
		catch(Exception e) {

		if (Utils.handleIntermediateIssue()) { 
			awType_WithoutClear(sTestName, oEle, sObjStr, sVal); }
		org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"=====");
		}
	}
	//User can select from a select list basis on the order of items present in the list 
	public static void awSelect_By_Index(String sTestName, WebElement oEle, String sObjStr, String sOption) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		String sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )";
		
		try { 
			sOption = Utils.Helper.validateUserInput(sTestName, sOption);
			
			if(oEle.getTagName().toString().equalsIgnoreCase("select")){
				Select oSelect = new Select(oEle);
		        oSelect.selectByIndex(Integer.parseInt(sOption)-1);            
	
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Performed");
			}
			else
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " :: Error : This Method Works only for Html : 'Select' tag");
	
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { awSelect_By_Index(sTestName, oEle, sObjStr, sOption); }
	    	org.testng.Reporter.log(sTestName+"========="+e+"===="+ sDesc+"=====");		}
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
		
		private static WebDriver launchNativePerfecto(String sTestName) throws Exception {
			  
			
			WebDriver driver = null;
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
		
	
		 private static WebDriver launchNativeMobileLabs(String sTestName) throws Exception {
			 WebDriver driver = null;
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
			WebDriver lDriver=(WebDriver)driver;
			try {
				
				lDriver.manage().timeouts().implicitlyWait(Long.valueOf(0), TimeUnit.SECONDS); 
				WebDriverWait wait = new WebDriverWait(lDriver, Seconds); 
				wait.until(ExpectedConditions.visibilityOf(oEle)); 
				lDriver.manage().timeouts().implicitlyWait(Long.valueOf(Utils.hEnvParams.get("OBJ_TIMEOUT")), TimeUnit.SECONDS); 
				return true; 
	 
			} catch(Exception e) { 
				lDriver.manage().timeouts().implicitlyWait(Long.valueOf(Utils.hEnvParams.get("OBJ_TIMEOUT")), TimeUnit.SECONDS); 
				return false; 
			} 
		}
		
		
		 private static WebDriver launchNativePhysical(String sTestName) throws Exception {

				WebDriver driver = null;
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
	
			if (h2AndroidPrefs.containsKey(sTestName))
			{	
											
				if (h2AndroidPrefs.get(sTestName).containsKey("App_WaitActivity") && h2AndroidPrefs.get(sTestName).containsKey("App_Package") && h2AndroidPrefs.get(sTestName).containsKey("App_Activity"))	{
					oCap.setCapability("appWaitActivity", h2AndroidPrefs.get(sTestName).get("App_WaitActivity").replaceAll("|", ","));
					oCap.setCapability("appPackage", h2AndroidPrefs.get(sTestName).get("App_Package")); 
					oCap.setCapability("appActivity", h2AndroidPrefs.get(sTestName).get("App_Activity")); 
					}
				else {
					throw new Exception(sTestName+"Error : App Details are not complete");
				}
			}
			else {
				throw new Exception(sTestName+"Error : You Need to fill App Details : App_WaitActivity , App_Package , App_Activity ");
			}
			return oCap;
		}
	 
	//	Launches the web browser(Chrome if prefrences not set) and runs the URL provided by User.
		private static WebDriver awLaunchWeb(String sTestName, String sUrl, String sBrowserReference) throws Exception { 
			WebDriver driver = null;
			String sDesc = Logs.log(sTestName);
			String sExe_Server = Utils.hEnvParams.get("Execution_On");
			switch(sExe_Server.toUpperCase()) {
			
			case "MOBILE_LABS" 	:	driver= androidLaunchMobileLabs(sTestName);;								
									break;
			
			case "PERFECTO" 	:	driver=androidLaunchPerfecto(sTestName);								
									break;
			
			case "PHYSICAL" : 		driver=androidLaunchPhysical(sTestName);
									break;
			
			default : throw new Exception(sTestName+"Error: Execution Server Entered is Not Correct, Choose any one amongst MOBILE_LABS, PERFECTO or PHYSICAL ");
					  
				
				
			}
			try
			{
				sUrl = Utils.Helper.validateUserInput(sTestName, sUrl);
				driver.manage().timeouts().implicitlyWait(Long.valueOf(Utils.hEnvParams.get("OBJ_TIMEOUT")), TimeUnit.SECONDS);
				driver.manage().timeouts().pageLoadTimeout(Long.valueOf(Utils.hEnvParams.get("OBJ_TIMEOUT")), TimeUnit.SECONDS);
				
				driver.get(sUrl);
				
				sBrowserReference = (sBrowserReference.equals("")) ? "Default" : sBrowserReference;
				Utils.putObjectDriver(sTestName, driver, sBrowserReference);
				org.testng.Reporter.log(sTestName+"========="+ sDesc+"=====" + " -- Execution on  " + sExe_Server +"  URL: "  + sUrl);
			
			}
			catch (Exception ex) {
				
				org.testng.Reporter.log(sTestName+"========="+ex+"===="+ sDesc+"=====");			}
			
			return driver;
		
		}
		private static DesiredCapabilities set_WebCapabilities(String sTestName) {
			
			DesiredCapabilities oCap = new DesiredCapabilities();
			oCap.setCapability("deviceName", "Android"); 
			oCap.setCapability("platformName", "Android");
			oCap.setCapability("udid", Utils.h2TestName_TestParams.get(sTestName).get("exePlatform"));
			oCap.setCapability("deviceConnectIgnoreSession", true);
			
			oCap.setCapability("newCommandTimeout", Utils.hEnvParams.get("OBJ_TIMEOUT"));
	
			if (h2AndroidPrefs.containsKey(sTestName)) {
				
				if (h2AndroidPrefs.get(sTestName).containsKey("Browser_Name") && h2AndroidPrefs.get(sTestName).containsKey("App_Package") && h2AndroidPrefs.get(sTestName).containsKey("App_Activity"))	{
					oCap.setCapability("browserName", h2AndroidPrefs.get(sTestName).get("Browser_Name"));	
					oCap.setCapability("appPackage", h2AndroidPrefs.get(sTestName).get("App_Package")); 
					oCap.setCapability("appActivity", h2AndroidPrefs.get(sTestName).get("App_Activity")); 
				}
				else {
					oCap.setCapability("browserName", "Chrome");
					//oCap.setCapability("appPackage", "com.android.chrome"); 
					//oCap.setCapability("appActivity", "com.google.android.apps.chrome.Main"); 
					}	
			}
			else {
				oCap.setCapability("browserName", "Chrome");
			//	oCap.setCapability("appPackage", "com.android.chrome"); 
			//	oCap.setCapability("appActivity", "com.google.android.apps.chrome.Main");
				}
			return oCap;

		}
		private static WebDriver androidLaunchMobileLabs(String sTestName) throws Exception {
			  
			
			WebDriver driver = null;
			try { 
				
				DesiredCapabilities oCap = new DesiredCapabilities();
				oCap=set_WebCapabilities(sTestName);
				oCap.setCapability("deviceConnectUserName", Utils.hEnvParams.get("Dev_ConnUserID"));
				oCap.setCapability("deviceConnectApiKey", Utils.hEnvParams.get("Dev_ConnAPIKey"));
						
				URL sURL = new URL("http://"+Utils.hEnvParams.get("Cloud_IP")+"/Appium");
				
				driver = new AndroidDriver(sURL, oCap); 
				
			}	
			catch(Exception e)	{
				
				throw e;
			}
			return driver;

		}
		
		
		private static WebDriver androidLaunchPerfecto(String sTestName) throws Exception {
			  
			
			WebDriver driver = null;
			try { 
				
				DesiredCapabilities oCap = new DesiredCapabilities();
				oCap=set_WebCapabilities(sTestName);
				oCap.setCapability("deviceConnectUserName", Utils.hEnvParams.get("devConnUserID"));
				oCap.setCapability("deviceConnectApiKey", Utils.hEnvParams.get("devConnAPIKey"));
				oCap.setCapability("user", Utils.hEnvParams.get("Perfecto_User"));
				oCap.setCapability("password", Utils.hEnvParams.get("Perfecto_Password"));
				URL sURL = new URL(Utils.hEnvParams.get("Cloud_IP")+"/perfectomobile/wd/hub");
				driver = new AndroidDriver(sURL, oCap); 
				
			}
			catch(Exception e)	{
				
				throw e;
			}
			return driver;

		}
	
		private static WebDriver androidLaunchPhysical(String sTestName) throws Exception {
			  
			
			WebDriver driver = null;
			try { 
				String sPackageFldr = Utils.hSystemSettings.get("packageFolder"); 	
				DesiredCapabilities oCap = new DesiredCapabilities();
				oCap=set_WebCapabilities(sTestName);
				oCap.setCapability("chromedriverExecutable", sPackageFldr+"chromedriver.exe");
				
				startAppium(sTestName);
				URL sURLNative = new URL("http://localhost:"+ Utils.ports_map.get(sTestName) +"/wd/hub");
				driver = new AndroidDriver(sURLNative, oCap); 	
				
			}
		
			catch(Exception e)	{
				
				throw e;
			}
			return driver;
		
		}

		private static String takeScreenshot(Object driver) throws IOException, HeadlessException, AWTException  { 
			try {
				WebDriver lDriver=(WebDriver)driver;
				File screenshot = ((TakesScreenshot)lDriver).getScreenshotAs(OutputType.FILE);
				BufferedImage screenshotBase64=ImageIO.read(screenshot);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(screenshotBase64, "png", Base64.getEncoder().wrap(os));
				//Logs.log("ss",os.toString(StandardCharsets.ISO_8859_1.name()));
				return os.toString(StandardCharsets.ISO_8859_1.name());	
			
				
			} catch (Exception e) {
				return "";	
			}
			
		}
	
	static String CheckExist(Object lDriver, WebElement objDesc) throws Exception {
		String sRes="False";
		WebDriver driver=(WebDriver)lDriver;
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
	
	static void CheckPopUp_Object(String sTestName,WebElement oEle,String sObjStr) throws Exception
	{    
		WebDriver lDriver= (WebDriver)Utils.getObjectDriver(sTestName);
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
	static void forceEnter(WebDriver driver, WebElement oEle, String val) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].value="+val+";", oEle);
	}
	
	static void forceClick(WebDriver driver, WebElement oEle) {
		
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", oEle);
	}
	
	@SuppressWarnings("unused")
	static boolean isDisplayed(WebElement oEle) {
		return oEle.isDisplayed();
	}
	
	 static void checkReady( String sTestName, WebElement oEle) throws InterruptedException, HeadlessException, IOException, AWTException	{
		
			Thread.sleep(1000);
		
			WebDriver lDriver = (WebDriver)Utils.getObjectDriver(sTestName);
			WebDriverWait wait = new WebDriverWait(lDriver, Long.parseLong(Utils.hEnvParams.get("OBJ_TIMEOUT")));
			wait.until(ExpectedConditions.elementToBeClickable(oEle));
	}

	}	
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
}
	