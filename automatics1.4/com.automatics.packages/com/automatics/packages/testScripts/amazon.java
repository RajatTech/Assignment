// ##### HEADER_START #####
// @tcName -> amazon
// @tcDesc -> Amazon
// @tcType -> Android_Hybrid,Project_Specific
// @tcIdentifier -> magenta_login
// @Iter -> []
// @objectMap -> ["amazon_om"]
// ##### HEADER_END #####

// ##### DECLARATION_START #####
package com.automatics.packages.testScripts;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;

import com.google.gson.JsonObject;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.annotations.*;

import com.itextpdf.text.DocumentException;
import com.automatics.packages.library.*;
import com.automatics.packages.objectMap.*;
// ##### DECLARATION_END #####

// ##### TEST_START #####
public class amazon extends com.automatics.packages.beforeSuite.Suite {
	
	@BeforeMethod()
	public void beforetest(final ITestContext testContext) throws JSONException, org.json.simple.parser.ParseException, FileNotFoundException, DocumentException {
	
		Utils.getTestParams(testContext);
		Reporter.addTestRun(testContext, "Execution - Started");
	}

	@Test(retryAnalyzer=RetryAnalyzer.class)
	public void test(final ITestContext testContext) throws Exception{
		
		String sTestName = testContext.getName();
		
		try {
			Object driver = Utils.getObjectDriver(sTestName);
			
			// ##### OBJECTMAP_START #####

			amazon_om amazon_om = new amazon_om((WebDriver)driver);

			PageFactory.initElements((WebDriver)driver,amazon_om);
			// ##### OBJECTMAP_END #####
			
			// ##### SCRIPT_START #####
			Android_Hybrid.set_AndroidPreference(sTestName, "App_Package", "com.amazon.mShop.android.shopping");
			Android_Hybrid.set_AndroidPreference(sTestName, "App_Activity", "com.amazon.mShop.home.HomeActivity");
			Android_Hybrid.set_AndroidPreference(sTestName, "App_WaitActivity", "com.amazon.mShop.home.HomeActivity");
			Android_Hybrid.wait(sTestName, "10");
			driver=Android_Hybrid.launchApp(sTestName, "");//launch app
			PageFactory.initElements((WebDriver)driver,amazon_om);//get the driver
			Project_Specific.waitForElement(sTestName,amazon_om.Amazon_Login_page__username, 40);//wait for username to be available
			Android_Hybrid.click(sTestName, amazon_om.Amazon_Login_page__username, "amazon_om.Amazon_Login_page__username");
			Project_Specific.waitForElement(sTestName,amazon_om.Amazon_Login_page__Mobile_or_email, 40);
			Android_Hybrid.wait(sTestName, "15");
			Android_Hybrid.type(sTestName, amazon_om.Amazon_Login_page__Mobile_or_email,"amazon_om.Amazon_Login_page__Mobile_or_email" ,"CONFIG{username}");
			Project_Specific.waitForElement(sTestName,amazon_om.Amazon_Login_page__cntinu, 20);
			//Android_Hybrid.Click(sTestName, amazon_om.Amazon_Login_page__reference, "amazon_om.Amazon_Login_page__reference");
			Android_Hybrid.wait(sTestName, "5");
			Android_Hybrid.click(sTestName, amazon_om.Amazon_Login_page__cntinu, "amazon_om.Amazon_Login_page__cntinu");
			Project_Specific.waitForElement(sTestName,amazon_om.Amazon_Login_page__password, 20);
			Android_Hybrid.type(sTestName, amazon_om.Amazon_Login_page__password, "amazon_om.Amazon_Login_page__password" ,"CONFIG{password}");
			Project_Specific.waitForElement(sTestName,amazon_om.Amazon_Login_page__login, 20);
			Android_Hybrid.click(sTestName, amazon_om.Amazon_Login_page__login, "amazon_om.Amazon_Login_page__login");
			Project_Specific.waitForElement(sTestName,amazon_om.Amazon_home_page__Search_bar, 20);
//			Android_Hybrid.Click(sTestName, amazon_om.Amazon_Login_page__cntinu, "amazon_om.Amazon_Login_page__cntinu");
//			Android_Hybrid.Wait(sTestName, "10");
			Android_Hybrid.click(sTestName, amazon_om.Amazon_home_page__Search_bar, "amazon_om.Amazon_home_page__Search_bar");
			Android_Hybrid.wait(sTestName, "5");
			Project_Specific.Type_in_search(sTestName);
			Android_Hybrid.wait(sTestName, "5");
			Project_Specific.rotateScreen(ScreenOrientation.LANDSCAPE, sTestName);
			Android_Hybrid.wait(sTestName, "5");
			Project_Specific.rotateScreen(ScreenOrientation.PORTRAIT, sTestName);
			Project_Specific.scrollintoview_Almonds_click(sTestName);
			Android_Hybrid.wait(sTestName, "5");
			Project_Specific.scrollintoview_Add_To_cart_click(sTestName);
			Project_Specific.waitForElement(sTestName,amazon_om.almonds1kg__Open_cart, 20);
			Android_Hybrid.click(sTestName, amazon_om.almonds1kg__Open_cart, "amazon_om.almonds1kg__Open_cart");
			Android_Hybrid.wait(sTestName, "5");
			Project_Specific.verify_ElementPresent(sTestName, "Almonds");
			//Android_Hybrid.verify_ElementPresent(sTestName, amazon_om.almonds1kg__Almonds, "amazon_om.almonds1kg__Almonds" ,"");
//			Android_Hybrid.click(sTestName, amazon_om.Amazon_65_Tv__n_no_Tv, "amazon_om.Amazon_65_Tv__n_no_Tv");
//			Android_Hybrid.wait(sTestName, "5");
//			Android_Hybrid.native_Type(sTestName, amazon_om.Amazon_home_page__Search_bar, "amazon_om.Amazon_home_page__Search_bar" ,"65 inch TV");
//			Android_Hybrid.wait(sTestName, "20");
//			Android_Hybrid.click(sTestName, amazon_om.Amazon_home_page__Menu, "amazon_om.Amazon_home_page__Menu");
//			Android_Hybrid.wait(sTestName, "10");
			// ##### SCRIPT_END #####
			
	} catch (Exception e){
			 
			Reporter.printError(sTestName, e, "Run Time");
		}		  
	}  
	
	@AfterTest
	public void aftertest(final ITestContext testContext) throws Exception {
		Reporter.updateSuiteRun(testContext);
	}	
}
// ##### TEST_END #####