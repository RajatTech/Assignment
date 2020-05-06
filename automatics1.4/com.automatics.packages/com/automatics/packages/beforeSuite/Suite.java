package com.automatics.packages.beforeSuite;

import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.automatics.packages.library.*;

public class Suite {
	
	@BeforeSuite
	public void preSuite(ITestContext context) throws Exception {
			Utils.ReadParameters(context);
			Reporter.startSuite(context);
			SeleniumGrid.start();
			Utils.preScript(context);
		
	}
}
