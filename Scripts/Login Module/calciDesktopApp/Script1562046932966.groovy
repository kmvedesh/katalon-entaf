import org.openqa.selenium.By
import org.openqa.selenium.winium.DesktopOptions
import org.openqa.selenium.winium.WiniumDriver

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.utils.reporting as reporting

import internal.GlobalVariable as GlobalVariable

try {
	

	WiniumDriver driver = null
	
	String winiumDriverPath = "D:\\Winium.Desktop.Driver\\Winium.Desktop.Driver.exe";
	
//	String appPath = "C:\\Program Files (x86)\\UiPath\\Studio\\UiRobot.exe"
	
	String appPath = 'C:/windows/system32/calc.exe'

// 	To stop winium desktop driver before start another session
	
	Process process = Runtime.getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe");
	process.waitFor();
	process.destroy();
	
// 	To start winium desktop driver session
	Runtime rt = Runtime.getRuntime();
	Process pr = rt.exec(winiumDriverPath);

	DesktopOptions options = new DesktopOptions(); // Initiate Winium Desktop Options
	options.setApplicationPath(appPath); // Set UiRobot application path
	options.setDebugConnectToRunningApp(false)
	options.setLaunchDelay(2)
		
	Thread.sleep(5000);
	driver = new WiniumDriver(new URL('http://localhost:9999'), options); // Start a winium driver
	
	Thread.sleep(10000);

//	Once you get the driver you can start your automation scripts like selenium scripting,
	
	
	driver.findElement(By.name('Seven')).click()

	driver.findElement(By.name('Plus')).click()

	driver.findElement(By.name('Eight')).click()

	driver.findElement(By.name('Equals')).click()
	
	Thread.sleep(5000);
	
	driver.close()
	
	CustomKeywords.'com.utils.reporting.writeIntoFile'(GlobalVariable.testName, 'stepSummary', 'stepDecription', GlobalVariable.browserName, 'ActualResult',
		reporting.pass, '', CustomKeywords.'com.utils.reporting.getCurrentDateTime'())
}
catch (Exception e) {
	CustomKeywords.'com.utils.reporting.writeIntoFile'(GlobalVariable.testName, 'stepSummary', 'stepDecription', GlobalVariable.browserName, 'ActualResult',
		reporting.fail, e.getMessage(), CustomKeywords.'com.utils.reporting.getCurrentDateTime'())
}