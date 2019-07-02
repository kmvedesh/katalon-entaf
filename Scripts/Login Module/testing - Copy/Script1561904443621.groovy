import org.openqa.selenium.By
import org.openqa.selenium.winium.DesktopOptions
import org.openqa.selenium.winium.WiniumDriver

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.utils.reporting as reporting

import internal.GlobalVariable as GlobalVariable

try {
	

	WebUI.openBrowser('')

	WebUI.maximizeWindow()
	
	String str = 'C:\\Users\\703177749\\Desktop\\test.bat'
	
//	Runtime.runtime.exec(str)
	
//	Runtime.getRuntime().exec("cmd.exe C:\\Users\\703177749\\Desktop\\test.bat")
	
//	Process run = Runtime.getRuntime().exec("cmd.exe", "/c", "Start", str);
	
//	ProcessBuilder processBuilder = new ProcessBuilder("C:\\Users\\703177749\\Desktop\\test.bat");
	
	 //Process process = Runtime.getRuntime().exec(
	 //            "cmd /c hello.bat", null, new File("C:\\Users\\mkyong\\"));
				 

//		 Process process = processBuilder.start();
	
	
	String path="cmd /c start C:\\Users\\703177749\\Desktop\\test.bat";
	Runtime rn=Runtime.getRuntime();
	Process pr=rn.exec(path);
	
//	Thread.sleep(5000);
	
	CustomKeywords.'com.utils.reporting.writeIntoFile'(GlobalVariable.testName, 'stepSummary', 'stepDecription', GlobalVariable.browserName, 'ActualResult',
		reporting.pass, '', CustomKeywords.'com.utils.reporting.getCurrentDateTime'())
}
catch (Exception e) {
	CustomKeywords.'com.utils.reporting.writeIntoFile'(GlobalVariable.testName, 'stepSummary', 'stepDecription', GlobalVariable.browserName, 'ActualResult',
		reporting.fail, e.getMessage(), CustomKeywords.'com.utils.reporting.getCurrentDateTime'())
}