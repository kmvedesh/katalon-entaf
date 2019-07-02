
/**
 * This class is generated automatically by Katalon Studio and should not be modified or deleted.
 */

import java.lang.String


def static "com.utils.reporting.createFile"(
    	String testCaseName	) {
    (new com.utils.reporting()).createFile(
        	testCaseName)
}

def static "com.utils.reporting.writeIntoFile"(
    	String testCaseName	
     , 	String stepSummary	
     , 	String stepDecription	
     , 	String browserName	
     , 	String ActualResult	
     , 	String stepStatus	
     , 	String string	
     , 	String timeStamp	) {
    (new com.utils.reporting()).writeIntoFile(
        	testCaseName
         , 	stepSummary
         , 	stepDecription
         , 	browserName
         , 	ActualResult
         , 	stepStatus
         , 	string
         , 	timeStamp)
}

def static "com.utils.reporting.closeFile"() {
    (new com.utils.reporting()).closeFile()
}

def static "com.utils.reporting.writingSummaryReport"() {
    (new com.utils.reporting()).writingSummaryReport()
}

def static "com.utils.reporting.summaryReport"(
    	String modName	
     , 	String tcName	
     , 	String scriptExecutionTime	
     , 	String status	) {
    (new com.utils.reporting()).summaryReport(
        	modName
         , 	tcName
         , 	scriptExecutionTime
         , 	status)
}

def static "com.utils.reporting.getTotalRunsReport"() {
    (new com.utils.reporting()).getTotalRunsReport()
}

def static "com.utils.reporting.getFileNames"(
    	String dirPath	
     , 	String extension	) {
    (new com.utils.reporting()).getFileNames(
        	dirPath
         , 	extension)
}

def static "com.utils.reporting.deleteFiles"(
    	String dirPath	) {
    (new com.utils.reporting()).deleteFiles(
        	dirPath)
}

def static "com.utils.reporting.deleteReportFolder"() {
    (new com.utils.reporting()).deleteReportFolder()
}

def static "com.utils.reporting.pieChartView"() {
    (new com.utils.reporting()).pieChartView()
}

def static "com.utils.reporting.afterTestRun"() {
    (new com.utils.reporting()).afterTestRun()
}

def static "com.utils.reporting.afterScript"(
    	String moduleName	
     , 	String testCaseName	) {
    (new com.utils.reporting()).afterScript(
        	moduleName
         , 	testCaseName)
}

def static "com.utils.reporting.getCurrentDateTime"() {
    (new com.utils.reporting()).getCurrentDateTime()
}

def static "com.utils.reporting.reportsBackUp"() {
    (new com.utils.reporting()).reportsBackUp()
}

def static "fluent_Wait.waitForElement.fluentWait"(
    	String xpath	) {
    (new fluent_Wait.waitForElement()).fluentWait(
        	xpath)
}
