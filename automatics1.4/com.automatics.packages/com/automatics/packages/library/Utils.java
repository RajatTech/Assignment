package com.automatics.packages.library;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.testng.ITestContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings({"rawtypes", "deprecation"})
public class Utils {
	
	public Properties prop=new Properties();
	public InputStream input=null;
	public static HashMap<String, String> hConfigSetings = new HashMap<String, String>();
	public static HashMap<String, String> hEnvParams = new HashMap<String, String>();
	public static HashMap<String, String> hSystemSettings = new HashMap<String, String>();
	public static HashMap<String, String> hRemoteIps = new HashMap<>();
	
	public static ConcurrentMap<String,Integer> ports_map=new ConcurrentHashMap<String,Integer>();
	
	public static Hashtable<String, Hashtable<String, String>> h2TestName_ScriptParams = new Hashtable<String, Hashtable<String, String>>();
	public static Hashtable<String, Hashtable<String, String>> h2TestName_TestParams = new Hashtable<String, Hashtable<String, String>>();
	
	// new object storing and instance storing Hashtable
	
	public static Hashtable<String, Hashtable<String,Object>> h2TestName_ObjectDrivers=new Hashtable<>();
	private static Hashtable<String, String> hTestName_CurrentObjectInstance = new Hashtable<String, String>();
	
	//new preferences hashmaps for each execution type
	
	public static void ReadParameters(ITestContext context) throws Exception {
		
		try {
			
			getSystemSettings();
			//getConfigSettings();
			getEnvParams(context);
		} catch (Exception e) {
			System.out.println("Error: Could Not Read BeforeSuite Parameters" + e.getMessage());
        	throw new Exception (e);
		}
	}
	
	
	//getter and putter for new object hashmap for each testcase
	public static void putObjectDriver(String sTestName, Object driver, String currentDriverInstance) {
		
		Hashtable <String, Object > hdriver = new Hashtable<>();
		hdriver.put(currentDriverInstance, driver);
		if (h2TestName_ObjectDrivers.containsKey(sTestName))
			h2TestName_ObjectDrivers.get(sTestName).put(currentDriverInstance, driver);
		else 
			h2TestName_ObjectDrivers.put(sTestName, hdriver);
	}
	
	
	public static Object getObjectDriver(String sTestName) {
		
		try {
			
			if (hTestName_CurrentObjectInstance.containsKey(sTestName))
				return h2TestName_ObjectDrivers.get(sTestName).get(hTestName_CurrentObjectInstance.get(sTestName));
			
			else {
				if(h2TestName_ObjectDrivers.get(sTestName)!=null) {
					Object myKey = h2TestName_ObjectDrivers.get(sTestName).keySet().toArray()[0];
					return h2TestName_ObjectDrivers.get(sTestName).get(myKey);
				}
				else
					return null;
			}
				
		} catch (Exception e) {
			throw e;
		}
}
	
	public static Object Set_CurrentObjectReference(String sTestName, String sBrowserReference) throws HeadlessException, IOException, AWTException, InterruptedException  {
		
		String sDesc = Logs.log(sTestName);
		
		try { 
		
			hTestName_CurrentObjectInstance.put(sTestName, sBrowserReference);
			
			Reporter.print(sTestName, sDesc  + " to " + sBrowserReference + " -- Done");
	
		} catch (Exception ex) {
			Reporter.printError(sTestName, ex, sDesc, "");
		}
		return h2TestName_ObjectDrivers.get(sTestName).get(sBrowserReference);
	}
	
	public static void set_CurrentPlatform(String sTestName,String sExeType,String sExe_platfrom) throws HeadlessException, IOException, AWTException {
		
		sExeType=Helper.validateUserInput(sTestName, sExeType);
		sExe_platfrom=Helper.validateUserInput(sTestName, sExe_platfrom);
		h2TestName_TestParams.get(sTestName).put("exeType", sExeType);
		h2TestName_TestParams.get(sTestName).put("exePlatform", sExe_platfrom);
		
	}
	
	@SuppressWarnings("unused")
	public static Object RunScript(String sTestCaseName,ITestContext testContext) throws Exception   {
		
		String sDesc = ""; 
		
		try { 	
			sTestCaseName = Utils.Helper.validateUserInput(testContext.getName(), sTestCaseName);
			sDesc = Logs.log(testContext.getName())+" :: Running  script: ("+sTestCaseName+")";
			
			if (!sTestCaseName.equalsIgnoreCase("before"+testContext.getSuite().getName()))
				org.testng.Reporter.log(testContext.getName()+"  ::  "+sDesc + " :: Script  : "+ sTestCaseName + " Called");
				//Reporter.print(testContext.getName(), sDesc + " :: Script  : "+ sTestCaseName + " Called"); 
			Class<?> c = Class.forName("com.automatics.packages.testScripts."+sTestCaseName);
			Class<?>[] paramTypes = {ITestContext.class};
				
			Method method = c.getDeclaredMethod("test", paramTypes);
				
			Object ret = method.invoke(c.newInstance(), testContext);
			org.testng.Reporter.log("------------");
			org.testng.Reporter.log(testContext.getName());
			org.testng.Reporter.log(sDesc + " :: Script executed");
			org.testng.Reporter.log("------------");
			
					
			
		}	catch (Exception e) {
		
			if (Utils.handleIntermediateIssue()) { RunScript(sTestCaseName, testContext); }
			if (!sTestCaseName.equalsIgnoreCase("before"+testContext.getSuite().getName()))
				org.testng.Reporter.log(testContext.getName()+"---"+e+"---"+sDesc);
			else
				throw e;
		}
		return Utils.getObjectDriver(testContext.getName());
	}
	
	
	@SuppressWarnings("unchecked")
	private static void getConfigSettings() throws Exception {
		
        
        try {
        	Properties properties = new Properties();
        	if (System.getProperty("jJobName")!=null) 
        		properties.load(new FileReader(new File(new File(System.getProperty("user.dir")) ,"jenkins_config.ini")));
        	else
        		properties.load(new FileReader(new File(new File(System.getProperty("user.dir")),"conf.ini")));
            	
        		hConfigSetings = new HashMap<String, String>((Map) properties);
    	
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        	throw new Exception (e);
        }
	}
	
	private static void getSystemSettings() throws Exception {
		
		File file = new File(System.getProperty("user.dir"));
		hSystemSettings.put("userDir", System.getProperty("user.dir"));
		hSystemSettings.put("packageFolder", file +"\\external\\tools\\");
		hSystemSettings.put("reportFolder", new File(file.getParent()) + "/reports");
		System.out.println("reportFolder : "+hSystemSettings.get("reportFolder"));
	
	}
	
	private static void getEnvParams(ITestContext context) throws Exception {  
        try {
                          
             Utils u = new Utils();
             u.readEnvParams();
             Utils.hEnvParams.putIfAbsent("DEBUG" , "0");
             Utils.hEnvParams.putIfAbsent("OBJ_TIMEOUT" , "100");
             
               } catch (Exception e) {
             System.out.println(e.getMessage());
             throw new Exception (e);
        }
       }      

       private void readEnvParams()
       {
              input=getClass().getClassLoader().getResourceAsStream("config.properties");

              try {
                     prop.load(input);
                     
                     for (String key : prop.stringPropertyNames()) {
                         String value = prop.getProperty(key);
                         hEnvParams.put(key, value);
                     }
                     
              } catch (IOException e) {
                     e.printStackTrace();
              }

       }


		

	private static void loadJSONParams(ITestContext context) throws Exception {
		// TODO Auto-generated method stub
		try {
			String suiteName = context.getSuite().getName();
			File json = new File(hSystemSettings.get("userDir") + "/com.automatics.packages/com/automatics/packages/json/" + suiteName + ".json");
			if (json.exists()) {
				hEnvParams.put("CONFIG_SSH_EXCEL",json.getAbsolutePath());
			}
		} catch (Exception e) {
			throw e;
		}
	}


	
	public static void getTestParams(final ITestContext testContext) {
		
  		Hashtable<String, String> hParams = new Hashtable<String, String>(testContext.getCurrentXmlTest().getAllParameters());
		h2TestName_TestParams.put(testContext.getName(), hParams);
		read_port(testContext);
	}
	
	public synchronized static void read_port(ITestContext testContext) {	
		
		String sTestName=testContext.getName();
		if((h2TestName_TestParams.get(sTestName).get("exeType")).toUpperCase().contains("ANDROID")) {
			
			if(ports_map.isEmpty())
				ports_map.put(sTestName, 4000);
			else
				ports_map.put(sTestName,4000+ports_map.size());
		}
	}
	
	public static boolean handleIntermediateIssue() {
		return false;
	}
	
	public static void setScriptParams(String sTestName, String sKey, String sItemVal) {
		if( sItemVal==null)
			sItemVal = "";
		if (h2TestName_ScriptParams.containsKey(sTestName))
			
			h2TestName_ScriptParams.get(sTestName).put(sKey, sItemVal);
		else {
			Hashtable<String, String> localScriptParams= new Hashtable<String,String>();
			localScriptParams.put(sKey, sItemVal);
			h2TestName_ScriptParams.put(sTestName, localScriptParams);
		
			}
		}
	

	public static void preScript(ITestContext testContext) throws FileNotFoundException {
		
		try {
			Utils.RunScript("before "+testContext.getSuite().getName(), testContext);
			
		} catch (Exception e) {
			Logs.log("PreRequisite", "Info: No Pre-Suite Operations");
		}
	}
	@SuppressWarnings({ "unused", "unchecked" })
	public static void scheduled_Task_settings(ITestContext context) throws Exception {

		String sConfigName="",timestamp,temp,sDirPath,check_dir=null,current_exec=null,command=null;
		DateFormat tosystemFormat = DateFormat.getInstance(); 

		Properties properties = new Properties();
		HashMap<String ,String> local_conf=new HashMap<String,String>();
		ArrayList<String> DelDirs=new ArrayList<String>();
		
		try {
			timestamp = context.getCurrentXmlTest().getSuite().getFileName();
	
			temp = timestamp.substring(0,timestamp.lastIndexOf("\\"));
	
			sConfigName = (temp.substring(0,temp.lastIndexOf("\\")));
			sConfigName = (sConfigName.substring(sConfigName.lastIndexOf("\\"))).substring(1);
			
			if (sConfigName.equals("temp")) {
				
				sConfigName = (temp.substring(temp.lastIndexOf("\\"))).substring(1);
				sDirPath = (temp.substring(0,temp.lastIndexOf("\\")));
				File file = new File(sDirPath);
				String[] names = file.list();
		
				for(String name : names)
				{
				    if (new File(sDirPath+"\\" + name).isDirectory())
				    {
				        DelDirs.add(name);
				    }
				}
				current_exec = sConfigName.split("_")[0];
				current_exec = current_exec.replaceAll("-", "/");
				Date Curr_date = tosystemFormat.parse(current_exec+" 00:00 AM");
				for (String dir_name : DelDirs) {	
					if ((dir_name.charAt(1) =='-'||dir_name.charAt(2)=='-')&&(sConfigName.charAt(1)=='-'||sConfigName.charAt(2)=='-')) {
						
						check_dir = dir_name.split("_")[0];
						
						check_dir = check_dir.replaceAll("-", "/");
						Date check_date = tosystemFormat.parse(check_dir+" 00:00 AM");
						  
						if (check_date.before(Curr_date))
						{
							Helper.delete_dir_files(sDirPath+"\\"+dir_name);
							command="schtasks.exe /delete /TN \""+dir_name+"_Automatics_Task\" /F";
							Process process = Runtime.getRuntime().exec(command);
						}
					}
				}
		
				properties.load(new FileReader(new File(temp+"\\", sConfigName+".ini")));
				local_conf = new HashMap<String, String>((Map) properties);
				hConfigSetings.put("REPORTTYPE", local_conf.get("REPORTTYPE"));
				hConfigSetings.put("DBREPORT", local_conf.get("DBREPORT"));
				hConfigSetings.put("RERUN", local_conf.get("RERUN"));

			}
		} catch (Exception e) {
			System.out.println("Error: Error executing scheduled task" + e.getMessage());
        	throw new Exception (e);
		}
	}
	public static void close_App(ITestContext testContext) throws IOException {
		
		String sTestName=testContext.getName();
		String line="", temp = "", kill_cmd;
		
	       
		try {
			
			if ((Utils.h2TestName_TestParams.get(sTestName).get("exeType")).toUpperCase().contains("ANDROID") && Utils.hEnvParams.get("Execution_On").equalsIgnoreCase("PHYSICAL")) {
			
				int portNo = Utils.ports_map.get(sTestName);
				String cmds[] = {"cmd.exe", "/c", "netstat -a -n -o | findstr :"+portNo};
				Process p = Runtime.getRuntime().exec(cmds);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				kill_cmd = "cmd.exe /c Taskkill /PID "+line.substring(line.lastIndexOf(" ")+1) +" /F";
			    while ((line = in.readLine()) != null) {
			        temp=line.substring(0, line.length()/2);
			        temp=temp.substring(0, temp.lastIndexOf(" "));
			        if (temp.contains(portNo+"")) {
			        	Runtime.getRuntime().exec(kill_cmd);
			        }
			    }
			}
		} catch (Exception e)
		{
			System.out.println("Error: Closing Appium Session" + e.getMessage());
		}
	}
	
	static class Helper {
		
		private static String takeScreenshot(WebDriver lDriver) throws IOException, HeadlessException, AWTException  { 
			try {
				lDriver = new Augmenter().augment(lDriver);
				String screenshotBase64 = ((TakesScreenshot)lDriver).getScreenshotAs(OutputType.BASE64);
				return screenshotBase64;
				
			} catch (Exception e) {
				throw e;	
			}
			
		}
		
		private static void delete_dir_files(String path) throws Exception
		{
			try {
				File file2 = new File(path);
				String[]entries = file2.list();
				
				for(String s: entries) {
					
					if (new File(path+"\\" + s).isDirectory())
				    {
						delete_dir_files(path+"\\" + s);
				    }
				    File currentFile = new File(file2.getPath(),s);
				    currentFile.delete();
				  }
				file2.delete();
		
			} catch(Exception e) { 
				System.out.println("Error deleting old scheduled task" + e.getMessage());
				throw new Exception (e);
			}
		}
		public static HashMap<String, String>  parseConfigJson(String json, String getType) throws MalformedURLException, JSONException {
		
			
			HashMap<String, String> hConfig = new HashMap<String, String>();
			
			try {
				Type listType = new TypeToken<List<HashMap<String, String>>>(){}.getType();
				List<HashMap<String, String>> listParams = 
						new Gson().fromJson(new JSONObject(json).getJSONArray(getType).toString(), listType);
				for (HashMap<String, String> entry : listParams) {
					String param = "", value = "";
					for (String key : entry.keySet()) {
					    if (key.equalsIgnoreCase("ip")||key.equalsIgnoreCase("name"))
					    	param = entry.get(key);
					    else
					    	value = entry.get(key);
					}
					hConfig.put(param, value);
				}
			} catch (Exception e) {
				throw e;
			}
			 	return hConfig;
		}
		
		
		
	private static String getJSONFromAPI()
		{
			try
			{
				String url = "";
			
				if (System.getProperty("jJobName")!=null) {
					String basicInfo = callAPI( hConfigSetings.get("SERVER_URL") + "/api/basicInfo");
					String projectInfo = callAPI(hConfigSetings.get("SERVER_URL") + "/api/projectInfo/" + hConfigSetings.get("PROJECT_NAME"));
					JSONObject basicJSONInfo = new JSONObject(basicInfo);
					JSONObject prjJSONInfo = new JSONObject(projectInfo);
					hConfigSetings.put("API_URL", prjJSONInfo.get("apiURL").toString());
					hConfigSetings.put("DASHBOARD_URL", basicJSONInfo.get("dashboardURL").toString());
					hConfigSetings.put("USER_ID", "jenkinsjob (jenkins job)");
					url =  hConfigSetings.get("API_URL") + "/configurations/" + "jenkinsjob (jenkins job)";
				}
				else
					url = hConfigSetings.get("API_URL") + "/configurations/" +  Utils.hConfigSetings.get("USER_ID");
				url = url.replace(" ", "%20");
				return callAPI(url);
			}
			catch(Exception e)
			{
				return "false";
			}
			
		}
		private static String callAPI(String url) throws Exception{
			try {
				@SuppressWarnings("resource")
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				
				HttpResponse response = httpClient.execute(httpGet);
				
				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
				String jsonStr = "", temp;
				while((temp = br.readLine())!=null){jsonStr = temp;}
				return jsonStr;
			} catch (Exception e) {
				throw e;
			}
		}
		public static String getBy(String sObjStr) throws ClassNotFoundException {
			
			String fieldName, className ; 
			
			try {
				
				fieldName = sObjStr.split("\\.")[1] ;
				className = Utils.hEnvParams.get("App")+"." + sObjStr.split("\\.")[0] ;
				Class classTemp = Class.forName(className);
				
				return new Annotations(classTemp.getDeclaredField(fieldName)).buildBy().toString();
				
			} catch (NoSuchFieldException e) { return null; }
		}
		
		public static String executeRunCommand(List<String> sCmds, boolean keepSessionOpen) throws IOException, InterruptedException {
			
			String sTotalResp = "", sLineResp = null;
				
			try {
				
				ProcessBuilder builder = new ProcessBuilder(sCmds);
				builder.redirectErrorStream(true);
				Process p = builder.start(); 
				
				if (keepSessionOpen) {
				
					/*BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					Thread.sleep(500);
					while (br.ready()) {
						sLineResp = br.readLine(); sTotalResp += "\n"+ sLineResp;
						Thread.sleep(500);
					}
					br.close();*/
					
				} else {
					
					Thread.sleep(5000); p.destroyForcibly();
					BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while ((sLineResp = br.readLine()) != null) {
						sTotalResp += sLineResp;
					}
					br.close();
				}
			} catch (Exception e) {
				sTotalResp = "ERROR:-" + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e);
			}
			return sTotalResp;
		}
		
	  	
	 	
		public static String validateUserInput(String sTestName, String sInput) throws HeadlessException, IOException, AWTException {
			String userInput = sInput;
			String test_input=sInput;int cnt=0;
			try {
				if (sInput.contains("CONFIG{")||sInput.contains("TEST{")||sInput.contains("LOCAL{")) {		
					
					cnt=validate_count(test_input,"CONFIG{");
					if (cnt>0) {
						String patternS="(CONFIG\\{.*?\\})";
						Pattern pattern=Pattern.compile(patternS);
						Matcher matcher = pattern.matcher(sInput);
						while (matcher.find()) 
						    userInput=userInput.replace(matcher.group(1),replaceUserInput(sTestName,matcher.group(1)));
							
					}
					cnt=validate_count(test_input,"LOCAL{");
					if (cnt>0)	{
						String patternS="(LOCAL\\{.*?\\})";
						Pattern pattern=Pattern.compile(patternS);
						Matcher matcher = pattern.matcher(sInput);
							
							while(matcher.find())
					            userInput=userInput.replace(matcher.group(1),replaceUserInput(sTestName,matcher.group(1)));
					     		
					}
					cnt=validate_count(test_input,"TEST{");
					if (cnt>0)  {
						String patternS="(TEST\\{.*?\\})";
						Pattern pattern=Pattern.compile(patternS);
						Matcher matcher = pattern.matcher(sInput);
					
							while(matcher.find())
					            userInput=userInput.replace(matcher.group(1),replaceUserInput(sTestName,matcher.group(1)));
					}
				}
	        }
			catch (Exception e) {
				Reporter.printError(sTestName, e, "");
			}
	
			return(userInput);
		}
		
		private static String replaceUserInput(String sTestName, String sInput) throws HeadlessException, IOException, AWTException {
			String userInput = sInput; String sParamName;

			try {
				if (sInput.contains("CONFIG{")) {
					sParamName = sInput.substring(sInput.indexOf("{") + 1, sInput.indexOf("}"));
					userInput = Utils.hEnvParams.get(sParamName);
					if (userInput==null)
						throw new InvalidInputException("Error: Null value found for Configuration Parameter '" + sParamName +"'");
	
				} 
				else if (sInput.contains("TEST{")) {
	
					sParamName = sInput.substring(sInput.indexOf("{") + 1, sInput.indexOf("}"));
					if (Utils.h2TestName_TestParams.get(sTestName)!=null) {
						userInput = Utils.h2TestName_TestParams.get(sTestName).get(sParamName);
						if (userInput==null)
							throw new InvalidInputException("Error: Null value found for Test Parameter '" + sParamName +"'");
	
					} 
					else {
						throw new InvalidInputException("Error: No Test parameters Present For Test Case '" + sTestName +"'");
					}
				} 
				else if (sInput.contains("LOCAL{")) {
	
					sParamName = sInput.substring(sInput.indexOf("{") + 1, sInput.indexOf("}"));
					if ((Utils.h2TestName_ScriptParams.get(sTestName))!=null) {
						userInput = Utils.h2TestName_ScriptParams.get(sTestName).get(sParamName);
						if (userInput==null)
							throw new InvalidInputException("Error: Null value found for Local Parameter '" + sParamName +"'");
					}
					else {
						throw new InvalidInputException("Error: No LOCAL parameters Present For Test Case '" + sTestName +"'");
					}
				}

			}
			catch (Exception e) {
				Reporter.printError(sTestName, e, "");
			}

			return(userInput);
		}
		
		private static int validate_count(String test_input,String val)	{	
			int cnt=0;
			int index = test_input.indexOf(val);
			while (index != -1) {
			    cnt++;
			    test_input = test_input.substring(index + 1);
			    index = test_input.indexOf(val);
			}
			return cnt;
		}
	}

	private static String getCellValue(Cell cell) { 
		String ret = ""; 
	    switch (cell.getCellType()) { 
	        case Cell.CELL_TYPE_BOOLEAN: 
	            ret = "" + cell.getBooleanCellValue(); 
	            break; 
	        case Cell.CELL_TYPE_STRING: 
	            ret = cell.getRichStringCellValue().getString(); 
	            break; 
	        case Cell.CELL_TYPE_NUMERIC: 
	            if (DateUtil.isCellDateFormatted(cell)) { 
	                ret = cell.getDateCellValue().toString(); 
	            } else { 
	                ret = "" + (int)cell.getNumericCellValue(); 
	            } 
	            break; 
	        case Cell.CELL_TYPE_BLANK: 
	            ret = ""; 
	            break; 
	        default: 
	            ret = ""; 
	    } 
	    return ret; 
	}

}
@SuppressWarnings("serial")
class InvalidInputException extends Exception{  
	 
	InvalidInputException(String s){  
	super(s);  
 }  
}
