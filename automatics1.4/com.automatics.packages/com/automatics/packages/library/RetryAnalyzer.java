package com.automatics.packages.library;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
	
	int counter = 1;
	int retryLimit = 0;
	
 
	@Override
	public boolean retry(ITestResult result) {
		try {
			
	
			if (Utils.hConfigSetings.containsKey("RERUN"))
				retryLimit = Integer.parseInt(Utils.hConfigSetings.get("RERUN"));
			if (counter < retryLimit) {
				counter++;
				return true;
			}
		} catch (Exception e)	{
			System.out.println("Problems Retrying executing your script : " +  e.getMessage());
		}
			return false;
	}
}