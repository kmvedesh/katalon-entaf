import java.text.DateFormat
import java.text.SimpleDateFormat

import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.webui.driver.DriverFactory
import com.utils.reporting

import internal.GlobalVariable as GlobalVariable

class TestListeners {
	
	String startTime
	String endTime
	
	@BeforeTestCase
	def setUp(TestCaseContext testCaseContext) {
	    GlobalVariable.testName = testCaseContext.getTestCaseId()
		String fullPath = GlobalVariable.testName
		String[] segments = fullPath.split("/");
		GlobalVariable.testName = segments[segments.length-1]
		GlobalVariable.moduleName = segments[segments.length-2]
		
		GlobalVariable.browserName = DriverFactory.getExecutedBrowser().getName()
		String browser = GlobalVariable.browserName
		String[] partition = browser.split("_")
		GlobalVariable.browserName = partition[partition.length-2]
		
		CustomKeywords.'com.utils.reporting.createFile'(GlobalVariable.testName)
	}
	
	@AfterTestCase
	def tearDown(){
		CustomKeywords.'com.utils.reporting.afterTestRun'()
		CustomKeywords.'com.utils.reporting.afterScript'(GlobalVariable.moduleName ,GlobalVariable.testName)
		reporting.stepNo = 1;
	}
	
		
	@BeforeTestSuite
	def BeforeTestSuitReport(){
		GlobalVariable.projectDirectory = RunConfiguration.getProjectDir()
		CustomKeywords.'com.utils.reporting.deleteReportFolder'()
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		Date date = new Date();
		GlobalVariable.startTime= dateFormat.format(date);
	}
	
	@AfterTestSuite
	def AfterTestSuitReport(){
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		Date date = new Date();
		GlobalVariable.endTime= dateFormat.format(date);
		
		CustomKeywords.'com.utils.reporting.writingSummaryReport'()
		CustomKeywords.'com.utils.reporting.reportsBackUp'()
	}

}