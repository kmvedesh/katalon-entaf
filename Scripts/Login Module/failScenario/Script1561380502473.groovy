import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.utils.reporting as reporting

import internal.GlobalVariable as GlobalVariable


WebUI.openBrowser('')

WebUI.navigateToUrl(GlobalVariable.URL)

WebUI.waitForPageLoad(GlobalVariable.Max_Delay)

WebUI.maximizeWindow()

try {
    

	WebUI.verifyElementVisible(findTestObject('Page_Login/div_genpact-logo'))
	
	WebUI.verifyElementVisible(findTestObject('Page_Login/edittext_Username'))
	
	WebUI.verifyElementVisible(findTestObject('Page_Login/edittext_Password'))
	
	WebUI.verifyElementClickable(findTestObject('Page_Login/button_SIGN IN'))
	
	WebUI.verifyElementPresent(findTestObject('Page_Login/a_Dashboard'), 10)
	
    CustomKeywords.'com.utils.reporting.writeIntoFile'(GlobalVariable.testName, 'stepSummary', 'stepDecription', GlobalVariable.browserName, 'ActualResult', 
        reporting.pass, '', CustomKeywords.'com.utils.reporting.getCurrentDateTime'())
}
catch (Exception e) {
    CustomKeywords.'com.utils.reporting.writeIntoFile'(GlobalVariable.testName, 'stepSummary', 'stepDecription', GlobalVariable.browserName, 'ActualResult', 
        reporting.fail, e.getMessage(), CustomKeywords.'com.utils.reporting.getCurrentDateTime'())
} 

