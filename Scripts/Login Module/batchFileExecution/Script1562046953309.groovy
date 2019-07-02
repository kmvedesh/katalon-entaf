	import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.utils.reporting as reporting

import internal.GlobalVariable as GlobalVariable

try {
	
	WebUI.delay(5)
	
	String path="cmd /c start C:\\Users\\703177749\\Desktop\\test.bat";
	Runtime rn=Runtime.getRuntime();
	Process pr=rn.exec(path);
	
	CustomKeywords.'com.utils.reporting.writeIntoFile'(GlobalVariable.testName, 'stepSummary', 'stepDecription', GlobalVariable.browserName, 'ActualResult',
		reporting.pass, '', CustomKeywords.'com.utils.reporting.getCurrentDateTime'())
}

catch (Exception e) {
	CustomKeywords.'com.utils.reporting.writeIntoFile'(GlobalVariable.testName, 'stepSummary', 'stepDecription', GlobalVariable.browserName, 'ActualResult',
		reporting.fail, e.getMessage(), CustomKeywords.'com.utils.reporting.getCurrentDateTime'())
}