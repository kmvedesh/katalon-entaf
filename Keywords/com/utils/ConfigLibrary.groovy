package com.utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration

import internal.GlobalVariable

public class ConfigLibrary {

	public static final String projectName = "Cora Command ";

	public final String detailResultPath =  GlobalVariable.projectDirectory + "\\Results\\DetailedResults";
	public final String summaryResultPath = GlobalVariable.projectDirectory + "\\Results";

	public String projScreenshotsDirectory = GlobalVariable.projectDirectory + "\\Results\\Screenshots"

	public String projectImgDirectory = GlobalVariable.projectDirectory + "\\Images"

	public String previousReportsDirectory = GlobalVariable.projectDirectory + "\\Previous Test Results"

	public static int failCount = 0;
	public static int passCount = 0;
}
