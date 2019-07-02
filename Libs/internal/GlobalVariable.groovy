package internal

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.main.TestCaseMain


/**
 * This class is generated automatically by Katalon Studio and should not be modified or deleted.
 */
public class GlobalVariable {
     
    /**
     * <p></p>
     */
    public static Object Max_Delay
     
    /**
     * <p></p>
     */
    public static Object Min_Delay
     
    /**
     * <p></p>
     */
    public static Object testName
     
    /**
     * <p></p>
     */
    public static Object testStatus
     
    /**
     * <p></p>
     */
    public static Object startTime
     
    /**
     * <p></p>
     */
    public static Object endTime
     
    /**
     * <p></p>
     */
    public static Object URL
     
    /**
     * <p></p>
     */
    public static Object moduleName
     
    /**
     * <p></p>
     */
    public static Object browserName
     
    /**
     * <p></p>
     */
    public static Object projectDirectory
     

    static {
        try {
            def selectedVariables = TestCaseMain.getGlobalVariables("default")
			selectedVariables += TestCaseMain.getGlobalVariables(RunConfiguration.getExecutionProfile())
            selectedVariables += RunConfiguration.getOverridingParameters()
    
            Max_Delay = selectedVariables['Max_Delay']
            Min_Delay = selectedVariables['Min_Delay']
            testName = selectedVariables['testName']
            testStatus = selectedVariables['testStatus']
            startTime = selectedVariables['startTime']
            endTime = selectedVariables['endTime']
            URL = selectedVariables['URL']
            moduleName = selectedVariables['moduleName']
            browserName = selectedVariables['browserName']
            projectDirectory = selectedVariables['projectDirectory']
            
        } catch (Exception e) {
            TestCaseMain.logGlobalVariableError(e)
        }
    }
}
