package com.utils
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.NameFileComparator
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

class reporting {

	String htmlheader;
	String bodyHeader ;
	String htmlfooter;
	static BufferedWriter fileObj;
	FileWriter fileHTML;

	public static int stepNo = 1;

	public static String pass = "&#10004";

	public static String fail = "&#10008";

	public static int failedTests = 0;

	public static int passedTests = 0;

	public static boolean testStatus = true;

	private static int imagesCount = 0;

	static String NEWLINE = '\n';

	private static final long MAX_BUFFER_SIZE = 1024;

	private static StringBuilder resultBuffer = new StringBuilder();

	public static List<String[]> list = new ArrayList<String[]>();

	ConfigLibrary configLib = new ConfigLibrary()

	@Keyword
	def createFile(String testCaseName) throws Exception {

		File detailResultDir = new File(configLib.detailResultPath);

		if(!detailResultDir.isDirectory()){
			detailResultDir.mkdirs();
		}

		String path = detailResultDir.toString() + "/" + testCaseName + ".html";

		File testCaseFile = new File(path);

		if(testCaseFile.exists())
			testCaseFile.delete();

		testCaseFile.createNewFile();

		FileWriter fileHTML = new FileWriter(testCaseFile, true);

		fileObj = new BufferedWriter(fileHTML);

		htmlheader = "<html><head>";
		htmlheader += "<title>Test Execution Report</title>";
		htmlheader += "</head><body>";
		htmlheader += "<style>div.header-fixed {top: 5px;background-color:white;position: absolute;right: 8px;position: fixed;}";
		htmlheader += "table.header {top: 0px;position: fixed;background-color:white;}";
		htmlheader += "font.style1 {font-family: 'calibri';font-size: 1em;text-align: justify;}</style>";

		fileObj.write(htmlheader);

		String table = "<table class=\"header\" width = 92.2% align = left border='1' bordercolordark='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0' bordercolor='#C0C0C0'>";
		bodyHeader = table;
		bodyHeader = bodyHeader + "<thead><tr><th colspan='5' align = center style=\"background-color:#168DDB;\"><font color = #ffffff face='calibri' size='5'> Test Script Name: " + testCaseName + "</font></th></tr>";
		bodyHeader = bodyHeader + "<tr bgcolor = #168DDB><th width = 30% ><font color = #ffffff size='4' face='calibri'>Step Description</font></th>";
		bodyHeader = bodyHeader + "<th width = 10%><font color = #ffffff size='4' face='calibri'>Browser</font></th>";
		bodyHeader = bodyHeader + "<th width = 30%><font color = #ffffff size='4' face='calibri'>Actual Result</font></th>";
		bodyHeader = bodyHeader + "<th width = 10%><font color = #ffffff size='4' face='calibri'>Status</font></th>";
		bodyHeader = bodyHeader + "<th width = 20%><font color = #ffffff size='4' face='calibri'>Remarks</font></th></tr></thead>";

		fileObj.write(bodyHeader);
	}

	@Keyword
	def writeIntoFile(String testCaseName, String stepSummary, String stepDecription, String browserName, String ActualResult, String stepStatus, String string, String timeStamp) {

		String htmlBody = "<tbody><tr><td border = none colspan = 5><font color = #168DDB class=\"style1\"><b>" + "Step " + (stepNo++) + ": " +  stepSummary + "</b></font></td></tr>";
		htmlBody = htmlBody + "<tr><td width = 30% border = none><font class=\"style1\"> " + stepDecription + "</font></td>";
		htmlBody = htmlBody + "<td align=center width = 10% border = none><font class=\"style1\"> " + browserName + "</font></td>";
		htmlBody = htmlBody + "<td align=center width=30% Border = 0><font class=\"style1\"> " + ActualResult + "</font></td>";
		if(stepStatus.equals(fail)){
			testStatus = false;
			captureScreenshot(testCaseName + "_" + timeStamp);
			String imgLink = "<a style ='text-decoration: none; color: #C0292A; text-align: center;' href=\""+configLib.projScreenshotsDirectory+"\\" + testCaseName + "_" + timeStamp + ".jpg\"><div width=100%>";
			htmlBody = htmlBody + "<td align = center width = 10%><font color = #C0292A class=\"style1\">" + imgLink + stepStatus + "</div></a></font></td>";
			htmlBody = htmlBody + "<td align = center width = 20%><font class=\"style1\"> " + string + "</font></td></tr>";
		}
		else{

			htmlBody = htmlBody + "<td align = center width = 10%><font color = #3FCE30 class=\"style1\">" + stepStatus + "</font></td>";
			htmlBody = htmlBody + "<td align = center width = 20%><font class=\"style1\"> " + string + "</font></td></tr>";
		}

		fileObj.write(htmlBody);
	}

	@Keyword
	def closeFile() throws Exception {
		htmlfooter = "</tbody></table></body></html>";
		fileObj.write(htmlfooter);
		fileObj.close();
	}

	@Keyword
	def writingSummaryReport() {

		if (resultBuffer.length() > 0) {
			System.out.println("Into Writing");

			writeResultBufferToFile();
			getImagesReport();
			getDashBoardReport();
		}
	}

	/*
	 * Get Dash-board html.
	 */
	def getDashBoardReport() {
		try {
			File dashboardDir = new File(configLib.summaryResultPath);
			int total = ConfigLibrary.passCount+ConfigLibrary.failCount;
			int passPercentage = ((ConfigLibrary.passCount)*100)/total;
			int failPercentage = ((ConfigLibrary.failCount)*100)/total;

			if(!dashboardDir.isDirectory()){
				dashboardDir.mkdirs();
			}

			String path = dashboardDir.toString() + "/" + "DashBoard.html";

			String dashBoard = "<!DOCTYPE html><html><script>";
			dashBoard = dashBoard + "function getsumheight(){";
			dashBoard = dashBoard + "document.getElementById('rowheight3').height=screen.height-((screen.height*18)/100)-document.getElementById('rowheight1').clientHeight-document.getElementById('rowheight2').clientHeight-document.getElementById('rowheight4').clientHeight;";
			dashBoard = dashBoard + "document.getElementById('tableWidth').width = screen.width;}";
			dashBoard = dashBoard + "</script><body onload=\"getsumheight()\"><style>";
			dashBoard = dashBoard + ".row { vertical-align: top; height:auto !important; }";
			dashBoard = dashBoard + ".list {display:none; }";
			dashBoard = dashBoard + ".show {display: none; }";
			dashBoard = dashBoard + ".hide:target + .show {display: inline; }";
			dashBoard = dashBoard + ".hide:target {display: none; }";
			dashBoard = dashBoard + ".hide:target ~ .list {display:inline; }";
			dashBoard = dashBoard + "@media print { .hide, .show { display: none; } }";
			dashBoard = dashBoard + ".btn {border-radius: 6px;padding:0.1em;border:2px;background:#168DDB;}";
			dashBoard = dashBoard + ".badge {display: inline-block;min-width: 10px;padding: 5px 7px;font-size: 12px;font-weight: bold;line-height: 1;";
			dashBoard = dashBoard + "color: #168DDB;text-align: center;white-space: nowrap;vertical-align: middle;background-color: #ffffff;border-radius: 10px;}";
			dashBoard = dashBoard + "a.animate {color:white;text-decoration: none;}";
			dashBoard = dashBoard + "div.space {margin-bottom: 6px;list-style: none;}";
			dashBoard = dashBoard + ".center {margin: auto;width: 50%;}";
			dashBoard = dashBoard + "font.style1 {font-family: 'calibri';font-size: 1em;}";
			dashBoard = dashBoard + "footer {bottom: 0;left: 0;position: fixed;right: 0;text-align: center;background-color: #ffffff;}";
			dashBoard = dashBoard + "div.home {text-align: center;top: 1px;color: #ffffff;right: 20px;position: fixed;min-width: 40px;min-height: 30px;border-radius: 2px;padding:0em;background:#fffff0;}";
			dashBoard = dashBoard + "li {margin: 0.5em 0;}";
			dashBoard = dashBoard + "b.space {line-height: 180%;}";
			dashBoard = dashBoard + "</style><table id='tableWidth'><tr>";
			dashBoard = dashBoard + "<td id=\"rowheight1\" align=center width=17%><a href=\"https://www.genpact.com/\"><img width=90% height=50% src='"+configLib.projectImgDirectory + "\\Genpact.png'/></a></td>";
			dashBoard = dashBoard + "<td width=83%><div class=\"center\" ><font size=5 face='calibri'><b>" + ConfigLibrary.projectName + " Testing Dashboard" + "</b></font></div>";
			dashBoard = dashBoard + "<div style=\"float:right;\"><font size=3  face='calibri'>" + "Executed on:" + GlobalVariable.startTime + "</font></div></td></tr>";
			dashBoard = dashBoard + "<tr><td rowspan=\"4\" bgcolor=\"#FFFFFF\" valign=top width=17%><div style=\"margin-top: 12px;\"></div>";
			dashBoard = dashBoard + "<a class=\"animate\" href=\""+configLib.projScreenshotsDirectory+"\\images.html\" target=\"frame1\"><div class=\"space btn\"><table width=100%><tr><td align='left'><font class=\"style1\" style=\"padding-left: 10px;\">Images</font></td><td align='right'><span class=\"badge\">" + imagesCount + "</span></td></tr></table></div></a>";
			dashBoard = dashBoard + "</td><td id=\"rowheight2\" width=80%>";
			dashBoard = dashBoard + "<table align=center width = 91% cellspacing=10><tr><td align='center' width=24%>";
			dashBoard = dashBoard + "<div align = 'center' style=\"border:2px; background:#649421; padding:0.5em;\"><font class=\"style1\"><b class=\"space\">Execution Start Time</b><br/>" + GlobalVariable.startTime + "</font></div></td>";
			dashBoard = dashBoard + "<td align='center' width=24%><div align = 'center' style=\"border:2px; background:#6AC66A; padding:0.5em;\"><font class=\"style1\"><b class=\"space\">Execution End Time</b><br/>" + GlobalVariable.endTime + "</font></div></td>";
			dashBoard = dashBoard + "<td align='center' width=24%><div align = 'center' style=\"border:2px; background:#2EB1CE; padding:0.5em;\"><font class=\"style1\"><b class=\"space\">Total Time</b><br/>" + getExecutionTime(GlobalVariable.startTime, GlobalVariable.endTime) +"</font></div></td>";
			dashBoard = dashBoard + "<td align='center' width=28% rowspan=\"2\"><canvas id=\"myCanv\" width = 300% height = 140%></canvas>";
			dashBoard = dashBoard + "<script>var data=[{name: \"Pass: " + passPercentage + "%\", grade:" + ConfigLibrary.passCount + ", c:\"#649421\"} ,{name: \"Fail: " + failPercentage + "%\", grade:" + ConfigLibrary.failCount + ", c:\"#C0292A\"}];";
			dashBoard = dashBoard + "function getSum() {var mySum = 0;for (var i = 0; i < data.length; i++) {mySum += data[i].grade ;}return mySum;}";
			dashBoard = dashBoard + "function plotPie() {var x=[(myCanv.width)]-[((myCanv.width)*42)/100], y=[(myCanv.height)/2], y0=10, x0=[(myCanv.width)]-[((myCanv.width)*70)/100], r=y, angle1=0;";
			dashBoard = dashBoard + "canv = document.getElementById(\"myCanv\");ctx = canv.getContext(\"2d\");var mySum = getSum();for (var i = 0; i < data.length; i++) {ctx.strokeStyle = data[i].c ;";
			dashBoard = dashBoard + "ctx.fillStyle = data[i].c ;angle2 = (Math.PI * 2 * data[i].grade) / mySum ;ctx.beginPath();ctx.moveTo(x0,y) ;ctx.arc(x0,y,r, angle1, angle1 + angle2-0.00, false) ;";
			dashBoard = dashBoard + "ctx.fill ();angle1 += angle2 ;ctx.font=\"12px Arial\";ctx.fillRect(x,y0,5,5);ctx.fillText(data[i].name,x+10,y0+8);y0+=18;}}plotPie() ;";
			dashBoard = dashBoard + "</script></td></tr>";
			dashBoard = dashBoard + "<tr><td align='center' width=24%><div align = 'center' style=\"border:2px; background:#8FD32D; padding:0.5em;\"><font class=\"style1\"><b class=\"space\">Testscripts Executed</b><br/>" + total + "</font></div></td>";
			dashBoard = dashBoard + "<td align='center' width=24%><div align = 'center' style=\"border:2px; background:#649421; padding:0.5em;\"><font class=\"style1\"><b class=\"space\">Test Pass</b><br/>" + ConfigLibrary.passCount + "</font></div></td>";
			dashBoard = dashBoard + "<td align='center' width=24%><div align = 'center' style=\"border:2px; background:#C0292A; padding:0.5em;\"><font class=\"style1\"><b class=\"space\">Test Fail</b><br/>" + ConfigLibrary.failCount + "</font></div></td></tr></table>";
			dashBoard = dashBoard + "</td></tr>";
			dashBoard = dashBoard + "<tr><td id=\"rowheight3\" onload=\"value()\" align=left><iframe id=\"frame1\" name=\"frame1\" scrolling=\"yes\" style=\"border:none; margin-top:15px; margin-left:20px;\" height=100% width=95% src='" + configLib.summaryResultPath + "\\Summary.html'\"></iframe></td></tr></table>";
			dashBoard = dashBoard + "<footer id=\"rowheight4\"><font color = #1E90FF face='calibri' size= '2'>Copyright � 2019 <a href=\"https://www.genpact.com/cora/commandcenter/\">"+ConfigLibrary.projectName+"</a>. All Rights Reserved.</font></footer>";
			dashBoard = dashBoard + "<a class=\"animate\" href=\"" + configLib.summaryResultPath + "\\DashBoard.html\"><div left' class=\"home\"><img width=28px height=25px alt=\"Home\" src='"+configLib.projectImgDirectory + "\\home.png'/></div></a></body></html>";

			File f = new File(path);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(dashBoard);
			bw.close();
			System.out.println("Dashboard report created");
		} catch(Exception e) {
			e.getMessage();
		}
	}

	/*
	 * Add contents to buffer for summary report.
	 */
	@Keyword
	def summaryReport(final String modName, final String tcName,final String scriptExecutionTime, final String status) {
		try {
			if (resultBuffer.length() > 0) {
				resultBuffer.append(NEWLINE);
			}
			resultBuffer.append(" "+ modName + " ~ " + tcName + " ~ " + scriptExecutionTime + " ~ " + status + "`");

			if (resultBuffer.length() < MAX_BUFFER_SIZE) {
				return;
			}
		} catch (Exception e) {
		}
	}

	/*
	 * Get summary report
	 */
	def writeResultBufferToFile() {

		try {

			File summaryResultDir = new File(configLib.summaryResultPath);

			if(!summaryResultDir.isDirectory()){
				summaryResultDir.mkdirs();
			}

			String path = summaryResultDir.toString() + "/" + "Summary.html";

			OutputStream htmlfile = new FileOutputStream(new File(path));
			PrintStream printhtml = new PrintStream(htmlfile);

			String htmlheader = "<html><head>";
			htmlheader += "<title>Automation Report</title>";
			htmlheader += "</head><body>";
			String htmlfooter = "</body></html>";
			String bodyHeader = "<style>#header-fixed {position: fixed;top: 0px;background-color:white;}";
			bodyHeader = bodyHeader + "font.style1 {font-family: 'calibri';font-size: 1em;word-spacing: 12px;}</style>";

			bodyHeader = bodyHeader + "<table id=\"header-fixed\" width = 93% border='1' bordercolordark='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0' bordercolor='#C0C0C0'>";
			bodyHeader = bodyHeader + "<thead> <tr bgcolor='#168DDB'> <th width = 25%><font color = #ffffff face='calibri' size= '4.5'>" + "Module Name" + "</font></th>";
			bodyHeader = bodyHeader + "<th width = 25%><font color = #ffffff face='calibri' size= '4.5'>" + "Test Script Name" + "</font></th>";
			bodyHeader = bodyHeader + "<th width = 10%><font color = #ffffff face='calibri' size= '4.5'>" + "Duration" + "</font></th>";
			bodyHeader = bodyHeader + "<th width = 10%><font color = #ffffff face='calibri' size= '4.5'>" + "Status" + "</font></th></tr></thead>";

			bodyHeader = bodyHeader + "<tbody>";
			String[] temp = resultBuffer.toString().split("`");

			for (String s : temp) {

				String[] data = s.trim().split("~");

				bodyHeader = bodyHeader + "<tr>";

				for (int i = 0; i < data.length; i++) {

					if (i == data.length - 1) {
						if(data[i].trim().equals(pass)){
							String fileLink = "<a style ='text-decoration : none ; color: #3FCE30; text-align: center;' href= \""+ configLib.detailResultPath + "\\"  + data[1].trim() + ".html\"  title='View detail result' ><div width=100%>";
							bodyHeader = bodyHeader + "<td width = 10% align = center><font class=\"style1\"> " + fileLink	+ data[i].trim() + "</div></a></font></td>";
						}else{
							String fileLink = "<a style ='text-decoration : none ; color: #C0292A; text-align: center;' href= \""+ configLib.detailResultPath + "\\" + data[1].trim() + ".html\"  title='View detail result' ><div width=100%>";
							bodyHeader = bodyHeader + "<td width = 10% align = center><font class=\"style1\"> " + fileLink	+ data[i].trim() + "</div></a></font></td>";
						}
					}else if(i == data.length-2)
						bodyHeader = bodyHeader + "<td width = 10% align=center><pre><font class=\"style1\"> " + data[i].trim() + "</font></pre></td>";
					else if(i == data.length-3)
						bodyHeader = bodyHeader + "<td width = 25%><pre><font class=\"style1\"> " + data[i].trim() + "</font></pre></td>";
					else
						bodyHeader = bodyHeader + "<td width = 25%><pre><font class=\"style1\"> " + data[i].trim() + "</font></pre></td>";
				}
				bodyHeader = bodyHeader + "</tr>";
			}
			bodyHeader = bodyHeader + "</tbody></table>";

			printhtml.println(htmlheader + bodyHeader + htmlfooter);

			printhtml.close();
			htmlfile.close();
			System.out.println("Summary report created");

			resultBuffer.delete(0, resultBuffer.length());
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/*
	 * Get images report
	 */
	def getImagesReport() {
		try {
			File imageDir = new File(configLib.projScreenshotsDirectory);

			if(!imageDir.isDirectory()){
				imageDir.mkdirs();
			}

			String path = imageDir.toString() + "\\" + "images.html";

			FileUtils.copyFile(new File(configLib.projectImgDirectory + "\\home.png"), new File(configLib.projScreenshotsDirectory +"\\home.png"));
			FileUtils.copyFile(new File(configLib.projectImgDirectory + "\\Genpact.png"), new File(configLib.projScreenshotsDirectory +"\\Genpact.png"));

			String imageReport = "<!DOCTYPE html><html><body>";
			List<String> images = getFileNames(configLib.projScreenshotsDirectory, ".jpg");
			imagesCount = images.size();
			if(imagesCount>0) {

				imageReport = imageReport + "<style>.btn{display:inline-block;padding:6px 12px;font-size:14px;font-weight:400;line-height:309px;text-align:center;white-space:nowrap;vertical-align:middle;";
				imageReport = imageReport + "-ms-touch-action:manipulation;touch-action:manipulation;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;background:#ffffff;";
				imageReport = imageReport + "border:0px solid transparent;border-radius:2px}</style>";
				imageReport = imageReport + "<script type=\"text/javascript\">var i = 0;var image = new Array();";

				for(int i = 0 ; i < imagesCount; i++) {
					imageReport = imageReport + "image["+ i + "] = \"" + images.get(i) + "\";";
				}

				imageReport = imageReport + "var k = image.length-1;";
				imageReport = imageReport + "function increment() {if(i<k) {i++;}else {i=0;}swapImage();}";
				imageReport = imageReport + "function decrement() {if(i>0) {i--;}else {i=k;}swapImage();}";
				imageReport = imageReport + "function swapImage(){var el = document.getElementById(\"mydiv\");el.innerHTML=image[i];var img= document.getElementById(\"slide\");img.src= image[i];";
				imageReport = imageReport + "img.width=\"542\";img.height=\"364\";}";
				imageReport = imageReport + "function addLoadEvent(func) {var oldonload = window.onload;if (typeof window.onload != 'function') {window.onload = func;}else {window.onload = function() {";
				imageReport = imageReport + "if (oldonload) {oldonload();}func();}}}addLoadEvent(function() {swapImage(); });</script>";

				imageReport = imageReport + "<table border=0 width=85% height=100%><tr>";
				imageReport = imageReport + "<td width=25%%><button style=\"float: right;\" class=\"btn\" onclick=\"decrement()\"><font size='4'>&#9668</font></button></td>";
				imageReport = imageReport + "<td width=50% align=\"center\"><a><img name=\"slide\" id=\"slide\" alt =\"Screenshots\" src=\"" + images.get(0) + "\"/></a></td>";
				imageReport = imageReport + "<td width=25%><button style=\"float: left;\" class=\"btn\" onclick=\"increment()\"><font size='4'>&#9658</font></button></td>";
				imageReport = imageReport + "</tr><tr><td colspan='3' align=\"center\" style=\"font:small-caps bold 15px georgia; color:#168DDB;\"> <div id =\"mydiv\"></div></td></tr></table>";
			} else {

				imageReport = imageReport + "<div align=center style=\"background:#168DDB; color:#FFFFFF; padding: 5px 7px; border-radius: 6px;\">Images</div>";
				imageReport = imageReport + "<div width=100% style=\"margin-top:120px;\" align=center valign=center>";
				imageReport = imageReport + "<font style=\"font-family:'calibri';\" size='6'>Hurray..!!!<br/>TestScript(s) got passed.</font></div>";
			}

			imageReport = imageReport + "</body></html>";

			File f = new File(path);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(imageReport);
			bw.close();
			System.out.println("Images report created");
		} catch(Exception e) {
			e.getMessage();
		}
	}


	/*
	 * Get total runs report
	 */
	@Keyword
	def getTotalRunsReport() {
		try {
			File totalRunDir = new File(configLib.detailResultPath);

			if(!totalRunDir.isDirectory()){
				totalRunDir.mkdirs();
			}

			String path = configLib.projScreenshotsDirectory + "\\totalRuns.html";

			String totalRuns = "<!DOCTYPE html><html lang=\"en\"><head></head>";
			totalRuns = totalRuns + "<body bgcolor=\"#FFFFFF\"><Style>font.style1 {font-family: 'calibri';font-size: 1em;}";
			totalRuns = totalRuns + "#verticalText {transform: rotate(270deg);transform-origin: left top 0;margin-top: 250px;margin-left:5px;position: fixed;}";
			totalRuns = totalRuns + "</style><div style=\"margin-top:15px;\"></div>";
			totalRuns = totalRuns + "<div style=\"float: left;\" valign='center' id=\"verticalText\" ><font class=\"style1\"><b>Percentage</b></font></div>";
			totalRuns = totalRuns + "<div align=\"left\"><canvas id=\"myCanv\" width = 1000% height=320%></canvas></div>";
			totalRuns = totalRuns + "<table width='980'><tr>";
			totalRuns = totalRuns + "<td><div style=\"margin-left:70px;\"></div></td>";
			totalRuns = totalRuns + "<td width='15%'><font class=\"style1\">Run 1</font></td>";
			totalRuns = totalRuns + "<td width='15%'><font class=\"style1\">Run 2</font></td>";
			totalRuns = totalRuns + "<td width='15%'><font class=\"style1\">Run 3</font></td>";
			totalRuns = totalRuns + "<td width='15%'><font class=\"style1\">Run 4</font></td>";
			totalRuns = totalRuns + "<td width='15%'><font class=\"style1\">Run 5</font></td>";
			totalRuns = totalRuns + "<td width='15%'><font class=\"style1\">Run 6</font></td>";
			totalRuns = totalRuns + "<td width='15%'><font class=\"style1\">Run 7</font></td></tr>";
			totalRuns = totalRuns + "<tr><td align='center' colspan='8'><font class=\"style1\" ><b>Last 5 runs results</b></font></td></font></tr></table>";
			totalRuns = totalRuns + "<script>var data=[ ";

			for(int i=0;i<7;i++) {
				totalRuns = totalRuns + "{grade1:60, grade2:20, grade3:20}";
				if(i!=6){
					totalRuns = totalRuns + ", ";
				}
			}

			totalRuns = totalRuns + "];var x=5, y=0, i=0, lineWidth=0, length=0, limit=0;var text;";

			totalRuns = totalRuns + "function addText() {ctx.font=\"13px Arial\";ctx.fillStyle = \"#000000\";ctx.fillText(text+\"%\",x,length-3);}";
			totalRuns = totalRuns + "function addBar() {y=canv.height;length = y-(limit*text);ctx.fillRect(x,length,25,y);addText();x+=35;}";
			totalRuns = totalRuns + "function getWidth() {if(lineWidth<x)lineWidth = x-115;}";
			totalRuns = totalRuns + "function plotBar() {canv = document.getElementById(\"myCanv\");ctx = canv.getContext(\"2d\");limit=((canv.height-20)/100);";
			totalRuns = totalRuns + "for (i=0;i<data.length;i++) {x+=32;ctx.beginPath();ctx.moveTo(x,y) ;y=canv.height-20;text = data[i].grade1;ctx.fillStyle = \"#649421\" ;";
			totalRuns = totalRuns + "addBar();text = data[i].grade2;ctx.fillStyle = \"#C0292A\" ;addBar();text = data[i].grade3;ctx.fillStyle = \"#F7D10E\" ;addBar();}";
			totalRuns = totalRuns + "ctx.fillRect(0,canv.height-1,canv.width,0.2);}plotBar() ;</script></body></html>";


			File f = new File(path);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(totalRuns);
			bw.close();
			System.out.println("Images report created");
		} catch(Exception e) {
			e.getMessage();
		}
	}

	/*
	 * Get file names of specific extension.
	 */
	@Keyword
	def List<String> getFileNames(String dirPath, String extension) throws IOException {
		File file = new File(dirPath);
		String[] myFiles;
		List<String> fileNames = new ArrayList<String>();
		if (file.isDirectory()) {
			myFiles = file.list();
			for (int i = 0; i < myFiles.length; i++) {
				if(myFiles[i].toString().endsWith(extension)) {
					fileNames.add(myFiles[i].toString());
				}
			}
		}
		return fileNames;
	}

	/*
	 * Delete files.
	 */
	@Keyword
	def deleteFiles(String dirPath) throws IOException {
		File file = new File(dirPath);
		String[] myFiles;
		if(file.exists()) {
			if (file.isDirectory()) {
				myFiles = file.list();
				for (int i = 0; i < myFiles.length; i++) {
					myFiles[i].toString();
					File myFile = new File(file, myFiles[i]);
					myFile.delete();
				}
				file.delete();
			}
		}
	}

	/*
	 * Delete report folder.
	 */
	@Keyword
	def deleteReportFolder() throws Exception{
		deleteFiles(configLib.detailResultPath);
		deleteFiles(configLib.projScreenshotsDirectory);
		deleteFiles(configLib.summaryResultPath);
	}

	/*
	 * Create pie chart
	 */
	@Keyword
	def String pieChartView() throws Exception {
		String filePath = configLib.detailResultPath +"/PieChart.html";
		int total = ConfigLibrary.passCount+ConfigLibrary.failCount;
		int passPercentage = (ConfigLibrary.passCount*100)/total;
		int failPercentage = (ConfigLibrary.failCount*100)/total;

		String htmlCode = "<!DOCTYPE html><html lang=\"en\"><head></head><body bgcolor=\"#FFFFF0\">"
		+"<canvas id=\"myCanv\" width = 210 height=150></canvas><script>"
		+"var data=[ {name: \"Pass: "+passPercentage+"%\", grade:"+ ConfigLibrary.passCount +", c:\"#649421\"} ,"
		+"{name: \"Fail: "+failPercentage+"%\", grade:"+ ConfigLibrary.failCount +", c:\"#C0292A\"} ];"

		+"function getSum() {"
		+"var mySum = 0;"
		+"for (var i = 0; i < data.length; i++) {mySum += data[i].grade ;}"
		+"return mySum;}"

		+"function plotPie() {"
		+"var x=20, x0=180, y=y0=90, r=90, angle1=0;"
		+"canv = document.getElementById(\"myCanv\");"
		+"ctx = canv.getContext(\"2d\");"
		+"var mySum = getSum();"
		+"for (var i = 0; i < data.length; i++) {"
		+"ctx.strokeStyle = data[i].c ;"
		+"ctx.fillStyle = data[i].c ;"
		+"angle2 = Math.PI * 2 * data[i].grade / mySum ;"
		+"ctx.beginPath();"
		+"ctx.moveTo(x0,y0) ;"
		+"ctx.arc(x0,y0,r, angle1, angle1 + angle2-0.00, false) ;"
		+"ctx.fill ();"
		+"angle1 += angle2 ;"
		+"ctx.font=\"12px Arial\";"
		+"ctx.fillRect(x,y+40,5,5);"
		+"ctx.fillText(data[i].name,x,y+48);"
		+"y += 15;}}"

		+"plotPie() ;"
		+"</script></body></html>";

		File f = new File(filePath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(htmlCode);
		bw.close();
		if(System.getProperty("os.name").replaceAll("[^A-Za-z]+", "").equalsIgnoreCase("Windows")){
			Process p = Runtime.getRuntime().exec("attrib +h " + filePath);
			p.waitFor();
		}

		return htmlCode;
	}

	/*
	 * Method for after method
	 */
	@Keyword
	def afterTestRun() {
		if (testStatus) {
			passedTests++;
		} else {
			failedTests++;
		}
		testStatus = true;
	}

	/*
	 * Method for after class.
	 */
	@Keyword
	def afterScript(String moduleName, String testCaseName) throws Exception {
		if (failedTests > 0) {
			fail(moduleName, testCaseName);
		} else {
			pass(moduleName, testCaseName);
		}
		failedTests = 0;
		passedTests = 0;
	}


	/*
	 * Capture screenshots
	 */
	def captureScreenshot(String ImageName) throws Exception {
		WebUI.takeScreenshot(configLib.projScreenshotsDirectory + "\\" + ImageName + ".jpg")
	}

	/*
	 * Test case pass report
	 */
	def pass(String modName, String testCaseName) throws Exception {
		ConfigLibrary.passCount = ConfigLibrary.passCount + 1;
		summaryReport(modName, testCaseName, getExecutionTime(GlobalVariable.startTime, getCurrentTime()),
				pass);
		closeFile();
	}

	/*
	 * Test case fail report
	 */
	def fail(String modName, String tcName) throws Exception {
		ConfigLibrary.failCount = ConfigLibrary.failCount + 1;
		summaryReport(modName, tcName, getExecutionTime(GlobalVariable.startTime, getCurrentTime()),
				fail);
		closeFile();
	}

	/*
	 * System current date and time
	 */
	@Keyword
	def String getCurrentDateTime() {
		Calendar cd = Calendar.getInstance();
		SimpleDateFormat datefor = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss");
		String date = datefor.format(cd.getTime());
		return date;
	}

	/*
	 * System current date and time
	 */
	def String getCurrentTime() {
		Calendar cd = Calendar.getInstance();
		SimpleDateFormat datefor = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String date = datefor.format(cd.getTime());
		return date;
	}

	/*
	 * Get execution time
	 */
	def String getExecutionTime(String executionStartTime, String executionEndTime) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		SimpleDateFormat timeFor = new SimpleDateFormat("HH:mm:ss");

		Date endTime = sdf.parse(executionEndTime);
		Date startTime = sdf.parse(executionStartTime);
		endTime.setHours(endTime.getHours() - startTime.getHours());
		endTime.setMinutes(endTime.getMinutes() - startTime.getMinutes());
		endTime.setSeconds(endTime.getSeconds() - startTime.getSeconds());

		String date = timeFor.format(endTime);
		return date;
	}


	/*
	 * Back up of Test Results
	 */
	@Keyword
	def reportsBackUp() {
		File file = new File(configLib.previousReportsDirectory)

		if(!file.isDirectory()){
			file.mkdirs();
		}

		String dateTime = getCurrentDateTime()
		File[] file_array = file.listFiles();
		Arrays.sort(file_array, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

		if(file_array.length>=10) {
			FileUtils.deleteDirectory(file_array[0]);
			FileUtils.copyDirectory(new File(configLib.summaryResultPath), new File(configLib.previousReportsDirectory  + "\\Results" + "_" + dateTime));
		} else {
			FileUtils.copyDirectory(new File(configLib.summaryResultPath), new File(configLib.previousReportsDirectory  + "\\Results" + "_" + dateTime));
		}
	}
}
