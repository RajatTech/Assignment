package com.automatics.packages.library;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.ScreenOrientation;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.ScreenOrientation;


import com.google.common.collect.ImmutableMap;

public class Project_Specific {


	
	public static void Type_in_search(String sTestName)
	{
		WebDriver lDriver = (WebDriver) Utils.getObjectDriver(sTestName);
		AndroidDriver driver=(AndroidDriver)lDriver;
		driver.findElement(By.id("com.amazon.mShop.android.shopping:id/rs_search_src_text")).sendKeys("1 kg almond");
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "search"));
	}

	public static void storetext(String sTestName)
	{
		WebDriver lDriver = (WebDriver) Utils.getObjectDriver(sTestName);
		//WebElement oEle = lDriver.findElement(By.xpath("(//*[contains(text(),'(65 Inches)')])"));
		try {
			WebElement oEle = lDriver.findElement(By.id("(item_title)[3]"));
            if(oEle.isDisplayed())
            {
                            oEle.click();
            }
            }
            
            catch(Exception e) 
            {
                            System.out.println( "is hidden");
            }

//		String tv=oEle.getText();
//		System.out.println(tv);
		
	}
	
	public static void scrollintoview_Almonds_click(String sTestName)
	{
		WebDriver lDriver = (WebDriver) Utils.getObjectDriver(sTestName);
		MobileElement	element = (MobileElement) lDriver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().resourceId(\"com.amazon.mShop.android.shopping:id/rs_search_results_root\")).scrollIntoView("
						+ "new UiSelector().text(\"Nutraj California Almonds 1Kg\"))"));
		
		element.click();
		
		org.testng.Reporter.log("--------------------------------------------------");
		org.testng.Reporter.log("STEP :: "+"Scrolled and clicked");
		
		org.testng.Reporter.log("        ");
		
	}
	
	public static void scrollintoview_Add_To_cart_click(String sTestName)
	{
		WebDriver lDriver = (WebDriver) Utils.getObjectDriver(sTestName);
		MobileElement	element = (MobileElement) lDriver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"Add to Cart\"));"));
		element.click();
		org.testng.Reporter.log("--------------------------------------------------");
		org.testng.Reporter.log("STEP :: "+"Scrolled and clicked");
		
		org.testng.Reporter.log("        ");
		
	}

	public static boolean waitForElement(String sTestName,  WebElement oEle, int Seconds) {
		// TODO Auto-generated method stub

		try {
			WebDriver lDriver = (WebDriver) Utils.getObjectDriver(sTestName);
         
			WebDriverWait wait = new WebDriverWait(lDriver, Seconds);
			wait.until(ExpectedConditions.presenceOfElementLocated((By) oEle));
			return true;

		} catch (Exception e) {
			return false;
		}
	}
	
	//for screen orientation
	
	public static void rotateScreen(ScreenOrientation orientation, String sTestName ) {
		AppiumDriver driver = (AppiumDriver)Utils.getObjectDriver(sTestName);
		((AppiumDriver) driver).rotate(orientation);
		org.testng.Reporter.log("--------------------------------------------------");
		org.testng.Reporter.log("STEP :: "+"screen rotated");
		
		org.testng.Reporter.log("        ");
	}
	
	public static boolean verify_ElementPresent(String sTestName, String sVal) throws HeadlessException, IOException, AWTException, InterruptedException, ClassNotFoundException  {
		
		
		AppiumDriver driver = (AppiumDriver)Utils.getObjectDriver(sTestName);
		
		
		MobileElement	oEle = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().resourceId(\"com.amazon.mShop.android.shopping:id/rs_search_results_root\")).scrollIntoView("
						+ "new UiSelector().text(\"Nutraj California Almonds 1Kg\"))"));
		String sDesc; boolean bStatus = false; 
		//String sObjStr = "Almonds";
			
		//sDesc = Logs.log(sTestName) + " Object : " + sObjStr + " -> ( " + Utils.Helper.getBy(sObjStr) + " )" ;
			
		try { 	
			
			sVal = Utils.Helper.validateUserInput(sTestName, sVal);
			Reporter.filterUserInput(sVal);
			//Helper.checkReady(sTestName, oEle);
			
			try {
				
				bStatus = Helper.waitForElement(driver,oEle, Integer.parseInt(Utils.hEnvParams.get("OBJ_TIMEOUT")));
			
				org.testng.Reporter.log("--------------------------------------------------");
				org.testng.Reporter.log("STEP :: "+"Found element");
				org.testng.Reporter.log("prameter Value :: "+sVal);
				Helper.screenShot(sTestName);
				org.testng.Reporter.log("        ");
				
			}
			catch (NoSuchElementException e) {
		
				org.testng.Reporter.log("----------------------------------------------");
				org.testng.Reporter.log("STEP :: "+"");
				org.testng.Reporter.log("Exception :: "+e);
				org.testng.Reporter.log("Step Status ::"+bStatus);
				Helper.screenShot(sTestName);
				org.testng.Reporter.log("     ");
				Assert.assertFalse(false, "EXCEPTION AT STEP :: "+"element not found");
				Assert.fail();
			}
			
			
		}  catch(Exception e) {
			
	    	if (Utils.handleIntermediateIssue()) { verify_ElementPresent(sTestName, sVal); }
		
	    	org.testng.Reporter.log("----------------------------------------------");
			org.testng.Reporter.log("STEP :: "+"element not found due to intermittent issues");
			org.testng.Reporter.log("Exception :: "+e);
			Helper.screenShot(sTestName);
			org.testng.Reporter.log("     ");
			Assert.assertFalse(false, "EXCEPTION AT STEP :: "+"element not found due to intermittent issues");
			Assert.fail();
		}
		return bStatus;
	}
	

	
}
	

 class Helper {
	static String takeScreenshot(WebDriver lDriver) throws IOException,
			HeadlessException, AWTException {
		try {
			lDriver = new Augmenter().augment(lDriver);
			String screenshotBase64 = ((TakesScreenshot) lDriver)
					.getScreenshotAs(OutputType.BASE64);
			return screenshotBase64;

		} catch (Exception e) {
			throw e;
		}

	}


	
	
	static boolean waitForElement(Object driver, MobileElement oEle,int Seconds) {
		
		try {
			AppiumDriver lDriver=(AppiumDriver)driver;
			WebDriverWait wait = new WebDriverWait(lDriver, Seconds);
			wait.until(ExpectedConditions.visibilityOf(oEle));
		Boolean	isElementPresent = oEle.isDisplayed();
			//wait.until(ExpectedConditions.presenceOfElementLocated(oEle));
			return true;	
			
		} catch(Exception e) {
			return false;
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