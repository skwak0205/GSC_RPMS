<%-- emxProgramCentralCalendarCreateDialog.jsp

  Displays a window for creating a Calendar event.

  Copyright (c) 1992-2020 Dassault Systemes.

  All Rights Reserved.
  This program contains proprietary and trade secret information
  of MatrixOne, Inc.  Copyright notice is precautionary only and
  does not evidence any actual or intended publication of such program


--%>

<%@page import="com.matrixone.apps.common.util.ComponentsUtil"%>
<%@page import="com.matrixone.apps.domain.DomainConstants"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.matrixone.apps.program.ProgramCentralConstants"%>
<%@page import="com.matrixone.apps.program.ProgramCentralUtil"%>
<%@page import="com.matrixone.apps.common.WorkCalendar"%>
<%@page import="java.util.List"%>

<%@ include file = "../emxTagLibInclude.inc"%>
<%@include file = "../components/emxComponentsCommonInclude.inc" %>
<%@ include file = "../emxUICommonAppInclude.inc" %>
<%@ include file = "../emxJSValidation.inc" %>
<%@ include file = "../emxUICommonHeaderBeginInclude.inc" %>
<%@include file = "../common/enoviaCSRFTokenInjection.inc"%>	  

<jsp:useBean id="calendar" scope="page" class="com.matrixone.apps.common.WorkCalendar"/>
<head>
  <%@include file = "../common/emxUIConstantsInclude.inc"%>
  <script language="javascript" type="text/javascript" src="../common/scripts/emxUICalendar.js"></script>
</head>

<%
	String defaultLanguage = Locale.ENGLISH.getLanguage();
	String displayLanguage = context.getLocale().getLanguage();
	String calendarId = emxGetParameter(request, "objectId");
	String isExceptionConflict = emxGetParameter(request, "isExceptionConflict");
	String tempWorkingTimes = emxGetParameter(request, "tempWorkingTimes");
	
	StringList days = calendar.getShortDaysOfWeek(context, Locale.ENGLISH.getLanguage());
	StringList daysGlobalized = calendar.getShortDaysOfWeek(context, context.getLocale().getLanguage());	
	StringList slDaysInMonth = new StringList();
	for(int dayNumber = 1; dayNumber<= 31; dayNumber++){
		slDaysInMonth.add(dayNumber + DomainConstants.EMPTY_STRING);
	}  

	StringList slWeekDayOccurencesDisplay = calendar.getWeekOfMonthAttributeRange(context, displayLanguage);  
    StringList slWeekDayOccurencesStorage = calendar.getWeekOfMonthAttributeRange(context, defaultLanguage); 
    
    StringList slWeekDaysDisplay =  calendar.getDayOfWeekAttributeRange(context, displayLanguage);
    StringList slWeekDaysStorage =  calendar.getDayOfWeekAttributeRange(context, defaultLanguage);
    
    StringList slMonthsDisplay =  calendar.getMonthOfYearAttributeRange(context, displayLanguage); 
    StringList slMonthsStorage = calendar.getMonthOfYearAttributeRange(context, defaultLanguage);
    
    StringList slWorkTimeBasicFormatRange = calendar.getWorkingTimeIntervals(WorkCalendar.TimeFormat.BASIC); 
	StringList slWorkTimeMilitaryFormatRange = calendar.getWorkingTimeIntervals(WorkCalendar.TimeFormat.MILITARY);
    
    String mode = emxGetParameter(request,"mode");
    String relId = emxGetParameter(request,"relId");
    
    String sStartTime = ""; 
	String sFinishTime = ""; 
	    
    String recesTimingsStr = "";
    MapList recessStartEndMapList = new MapList();
    int recessCount = 0;
    
    if(ProgramCentralUtil.isNotNullString(relId) || "True".equalsIgnoreCase(isExceptionConflict)){
    	String sWorkingTimes = "";
    	
		if("True".equalsIgnoreCase(isExceptionConflict)){
			sWorkingTimes = tempWorkingTimes;
		}
		else{
		String mqlCmd = "print connection $1 select $2 dump $3";
    		sWorkingTimes = MqlUtil.mqlCommand(context, true, true, mqlCmd, true, relId,
		WorkCalendar.SELECT_WORKING_TIMES, "|");
		}
		
		StringList workingHoursStartEndTime = new StringList();
		StringList workingHours = FrameworkUtil.split(sWorkingTimes, ",");
		for (int i = 0, size = workingHours.size(); i < size; i++) {
			StringList startFinishTime = FrameworkUtil.split(workingHours.get(i), "-");
			workingHoursStartEndTime.addAll(startFinishTime);
			
			if(i==0){
				sStartTime = startFinishTime.get(0);
			}
			if(i==size-1){
				sFinishTime = startFinishTime.get(1);
			}
			
		}

		int rstartTime = 0;
		int rfinishTime = 0;
		int workingHoursStartEndTimeSize = workingHoursStartEndTime.size();
		StringBuffer recessStartFinishStr = new StringBuffer();

		for (int i = 1; (i + 1) < workingHoursStartEndTimeSize; i++, i++) {
			Map recessMap = new HashMap<String, String>();
			rstartTime = Integer.parseInt(workingHoursStartEndTime.get(i));
			rfinishTime = Integer.parseInt(workingHoursStartEndTime.get(i + 1));
			recessMap.put(WorkCalendar.RECESS_START_TIME, workingHoursStartEndTime.get(i));
			recessMap.put(WorkCalendar.RECESS_FINISH_TIME, workingHoursStartEndTime.get(i + 1));

			int start = slWorkTimeMilitaryFormatRange.indexOf((String) workingHoursStartEndTime.get(i));
			int finish = slWorkTimeMilitaryFormatRange.indexOf((String) workingHoursStartEndTime.get(i + 1));

			recessStartFinishStr.append(start + "-" + finish + ",");
			recessCount++;
		}
		if(recessStartFinishStr.length() > 0){
			recessStartFinishStr.deleteCharAt(recessStartFinishStr.length() - 1);
		}
		recesTimingsStr = recessStartFinishStr.toString();

	}else{
		recessCount = 1;
	}

    //Attributes
    String startDate = emxGetParameter(request,"Start Date");
    String endDate = emxGetParameter(request,"End Date");
    String exceptionType = emxGetParameter(request,"Calendar Exception Type");
    String title = emxGetParameter(request,"Title");
    String description = emxGetParameter(request,"description");
    String frequency = emxGetParameter(request,"Frequency");
    String sDaysOfWeek = emxGetParameter(request,"Days Of Week");
    String sDayOfMonth = emxGetParameter(request,"Day Of Month");
    String sDayOfWeek = emxGetParameter(request,"Day Of Week");
    String sWeekOfMonth = emxGetParameter(request,"Week Of Month");
    String sMonthOfYear = emxGetParameter(request,"Month Of Year");


	int iStartTime = slWorkTimeMilitaryFormatRange.indexOf("0800");
   	int iFinishTime = slWorkTimeMilitaryFormatRange.indexOf("1700");
   	int iLunchStartTime = slWorkTimeMilitaryFormatRange.indexOf("1200");
   	int iLunchFinishTime = slWorkTimeMilitaryFormatRange.indexOf("1300");

    if(ProgramCentralUtil.isNullString(title)){
    	title = ProgramCentralConstants.EMPTY_STRING;
    }
    if(ProgramCentralUtil.isNullString(description)){
    	description = ProgramCentralConstants.EMPTY_STRING;
    }
    if(ProgramCentralUtil.isNullString(sDayOfMonth) || "0".equals(sDayOfMonth)){
    	sDayOfMonth = "1";
    }
    if(ProgramCentralUtil.isNotNullString(sDayOfWeek)){
    	int dayOfWeekIndex = slWeekDaysStorage.indexOf(sDayOfWeek);
    	sDayOfWeek = (String)slWeekDaysDisplay.get(dayOfWeekIndex);
    }
    if(ProgramCentralUtil.isNotNullString(sWeekOfMonth)){
    	int weekOfMonthIndex = slWeekDayOccurencesStorage.indexOf(sWeekOfMonth);
    	sWeekOfMonth = (String)slWeekDayOccurencesDisplay.get(weekOfMonthIndex);
    }
    if(ProgramCentralUtil.isNotNullString(sMonthOfYear)){
    	int monthOfYearIndex = slMonthsStorage.indexOf(sMonthOfYear);
    	sMonthOfYear = (String)slMonthsDisplay.get(monthOfYearIndex);
    }
    if(ProgramCentralUtil.isNotNullString(sStartTime)){
		sStartTime = String.format("%04d", Integer.parseInt(sStartTime));
    	iStartTime = slWorkTimeMilitaryFormatRange.indexOf(sStartTime);
    }
    if(ProgramCentralUtil.isNotNullString(sFinishTime)){
    	sFinishTime = String.format("%04d", Integer.parseInt(sFinishTime));
    	iFinishTime = slWorkTimeMilitaryFormatRange.indexOf(sFinishTime);
    }
	
    
    StringList slWeeklyFrequencyPattern = new StringList();
    boolean isMonthlyReccurenceByDay = false;
    boolean isYearlyReccurenceByDay = false;
    boolean isfrqueuncyPatterncValid = false;
    if("Weekly".equalsIgnoreCase(frequency) ){
    	slWeeklyFrequencyPattern = FrameworkUtil.splitString(sDaysOfWeek, ",");
    }else if("Monthly".equalsIgnoreCase(frequency) && ProgramCentralUtil.isNotNullString(sWeekOfMonth)){
        	isMonthlyReccurenceByDay = true;
    }else if("Yearly".equalsIgnoreCase(frequency) && ProgramCentralUtil.isNotNullString(sWeekOfMonth)){
        	isYearlyReccurenceByDay = true;
    }else{
    	slWeeklyFrequencyPattern.add("1");
    }	
    
    //Date Fields
	String timezone = (String)session.getValue("timeZone");
	double dbTimeZone = Double.parseDouble(timezone);
	Locale locale = context.getLocale();
	SimpleDateFormat sdf = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
	SimpleDateFormat dayFormatter = new SimpleDateFormat("EEE");

	Calendar todayCalender = Calendar.getInstance(Locale.US);
	todayCalender.set(Calendar.HOUR, 0);
	todayCalender.set(Calendar.MINUTE, 0);
	todayCalender.set(Calendar.SECOND, 0);
	todayCalender.set(Calendar.MILLISECOND, 0);
	todayCalender.set(Calendar.AM_PM, Calendar.AM);
	
	String currenDayName = dayFormatter.format(todayCalender.getTime());
	String sToday = sdf.format(todayCalender.getTime());
	sToday = eMatrixDateFormat.getFormattedDisplayDate(sToday, dbTimeZone, locale);
	
    if (UIUtil.isNullOrEmpty(startDate)){
        startDate = sToday;
    }else{
    	startDate = eMatrixDateFormat.getFormattedDisplayDate(startDate, dbTimeZone, locale);
    }
    if (UIUtil.isNullOrEmpty(endDate)){
        endDate = "";
    }else{
    	endDate = eMatrixDateFormat.getFormattedDisplayDate(endDate, dbTimeZone, locale);
    }
	String lblName = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.Common.Name",
	displayLanguage);
    String lblExceptionType = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.ExceptionType", displayLanguage);
    String lblNonWorking = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.Attribute.CalendarExceptionType.Range.NonWorking", displayLanguage);
    String lblWorking = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.Attribute.CalendarExceptionType.Range.Working", displayLanguage);
    
    String lblWorkHours = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.WorkHours", displayLanguage);
    String lblLunchHours = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.LunchHours", displayLanguage);
    String lblEffectiveFrom = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.Attribute.StartDate", displayLanguage);
    String lblEffectiveTo = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.Attribute.FinishDate", displayLanguage);
    String lblFrequency = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.Frequency", displayLanguage);
    String lblWeeklyPattern = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.Frequency.WeeklyPattern", displayLanguage);
    String lblMonthlyPattern = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.Frequency.MonthlyPattern", displayLanguage);
    String lblYearlyPattern = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.Frequency.YearlyPattern", displayLanguage);
    String lblRequiredText = ComponentsUtil.i18nStringNow("emxComponents.Commom.RequiredText", displayLanguage);
    String lblDay = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.FrequencyPattern.Label.Day", displayLanguage);
    String lblOf = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.FrequencyPattern.Label.Of", displayLanguage);
    String lblOfEveryMonth = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.FrequencyPattern.Label.OfEveryMonth", displayLanguage);
    String lblDeleteRecess = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.DeleteRecess", displayLanguage);
    String lblAddRecess = ProgramCentralUtil.getPMCI18nString(context, "emxProgramCentral.WorkCalendar.AddRecess", displayLanguage);
    String errInvalidChars = EnoviaResourceBundle.getProperty(context, "Framework", "emxFramework.Common.InvalidName", displayLanguage);
    String errEnterATitle = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Common.EnterATitle", displayLanguage);
    String errInvalidDateRange = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Calendar.InvalidDateRange", displayLanguage);
%>

<%@page import="com.matrixone.apps.domain.util.EnoviaResourceBundle"%><html>
<html>
<link rel="stylesheet" href="../plugins/libs/jqueryui/1.10.3/css/jquery-ui.css">
<link rel="stylesheet" href="../programcentral/styles/WorkCalendar.css">
<script src="../common/scripts/jquery-1.9.1.js"></script>
<script src="../plugins/libs/jqueryui/1.10.3/js/jquery-ui.js"></script>
<script src="../programcentral/script/WorkCalendar.js"></script>

  <body class="white" onload="document.EventCreate.Title.focus()">
    <form name="EventCreate" method="post" onSubmit="return false;">
      <input type="hidden" name="parentOID" value="<%=XSSUtil.encodeForHTMLAttribute(context, calendarId)%>" />
      <input type="hidden" name="mode" value="<%=XSSUtil.encodeForHTMLAttribute(context, mode)%>" />      
      <input type="hidden" name="relId" value="<%=XSSUtil.encodeForHTMLAttribute(context, relId)%>" />      
      <table border="0" width="100%" height="100%">
        <tr>
          <td>
            <%-- Display the input fields. --%>
            <table border="0" width="100%" id="table_createEvent">
              <tr>
					<td></td>
					<td class="requiredNotice"><%=XSSUtil.encodeForHTMLAttribute(context, lblRequiredText)%></td>
					<td></td>
     	      </tr>
     		  <tr>
                <td nowrap="nowrap" class="labelRequired"><%=XSSUtil.encodeForHTMLAttribute(context, lblName)%></td>
                <td nowrap="nowrap" class="field"><input type="text" name="Title" size="30" value="<%=XSSUtil.encodeForHTMLAttribute(context, title)%>" onkeydown="return (event.keyCode!=13);" /></td>
				<td class="field" style="width:50px;">&nbsp;</td>
              </tr>
              <tr>
                <td class="label" nowrap="nowrap"><%=XSSUtil.encodeForHTMLAttribute(context, lblExceptionType)%></td>
				<td class="field">
					<div id="exceptionTypes">
					<%if("Working".equals(exceptionType)){%>
						<input type="radio" id="exceptionTypeNonWorking" value="Non Working" name="exceptionTypeRadio"><label class="label" for="exceptionTypeNonWorking"><%=XSSUtil.encodeForHTMLAttribute(context, lblNonWorking)%></label>
						<input type="radio" id="exceptionTypeWorking" value="Working" name="exceptionTypeRadio" checked="checked"><label class="label" for="exceptionTypeWorking"><%=XSSUtil.encodeForHTMLAttribute(context, lblWorking)%></label>
					<%}else{%>
						<input type="radio" id="exceptionTypeNonWorking" value="Non Working" name="exceptionTypeRadio" checked="checked"><label class="label" for="exceptionTypeNonWorking"><%=XSSUtil.encodeForHTMLAttribute(context, lblNonWorking)%></label>
						<input type="radio" id="exceptionTypeWorking" value="Working" name="exceptionTypeRadio"><label class="label" for="exceptionTypeWorking"><%=XSSUtil.encodeForHTMLAttribute(context, lblWorking)%></label>
					<%}%>					
	     	         </div>
	    		</td>
				<td class="field"></td>
              </tr>
              <tr class="trHiddenWorkingHours" >
                <td class="label" ><%=XSSUtil.encodeForHTMLAttribute(context, lblWorkHours)%></td>
                <td class="field">
                	<hr class="slider-space"/>
                	<div id="divWorkHours" name="divWorkHours"></div>
                </td>
				<td class="field"></td>
              </tr>
                            
              <tr class="trHiddenWorkingHours" >
                <td class="label" ><%=XSSUtil.encodeForHTMLAttribute(context, lblLunchHours)%></td>
                <td class="field">
                </td>
				<td class="field">
                	<button id="add_Recess" onclick="add(this,'<%=XSSUtil.encodeForHTMLAttribute(context, lblLunchHours)%>','createEvent','table_createEvent',null,null)"><img src="../common/images/I_WorkCalendarAddRecess_32.png" alt="<%=XSSUtil.encodeForHTMLAttribute(context, lblAddRecess)%>" title="<%=XSSUtil.encodeForHTMLAttribute(context, lblAddRecess)%>" width="20" height="20"/></button>
                	</td>
              </tr>
                            
              <%
              for(int i=0; i<recessCount;i++ ){
            	  String divLunchHourId = "divLunchHours"+ "_" +i;
            	  
              %>
            	  
            	  <tr class="trHiddenWorkingHours" >
                	<td class="label" ></td>
                	<td class="field">
                		<br/>
                		<div id="<%=divLunchHourId%>" name="<%=divLunchHourId%>" value="<%=relId%>"></div>
                	</td>
					<td class="field"><br/><button onclick="deleteRecess(this,true,'table_createEvent',calendarId,relId)"><img src="../common/images/iconActionRemove.png" alt="<%=XSSUtil.encodeForHTMLAttribute(context, lblDeleteRecess)%>"  title="<%=XSSUtil.encodeForHTMLAttribute(context, lblDeleteRecess)%>" width="20" height="20"/></button></td>
              	  </tr>
            	  
              <%
              	}
              
              	if(recessCount == 0){
              		String divLunchHourId = "divLunchHours"+ "_0";
              	  
                    %>
                  	  
                  	  <tr class="trHiddenWorkingHours" >
                      	<td class="label" ></td>
                      	<td class="field">
                      		<br/>
                      		<div id="<%=divLunchHourId%>" name="<%=divLunchHourId%>" value="<%=relId%>"></div>
                      	</td>
      					<td class="field"><br/><button onclick="deleteRecess(this,true,'table_createEvent',calendarId,relId)"><img src="../common/images/iconActionRemove.png" alt="<%=XSSUtil.encodeForHTMLAttribute(context, lblDeleteRecess)%>"  title="<%=XSSUtil.encodeForHTMLAttribute(context, lblDeleteRecess)%>" width="20" height="20"/></button></td>
              </tr>
              
                    <%
              	}
              %>
              
                <tr>
                <td nowrap="nowrap" class="labelRequired"><%=XSSUtil.encodeForHTMLAttribute(context, lblEffectiveFrom)%></td>
                <td nowrap="nowrap" class="field">
                	<input type="text" id="startDate" name="StartDate" size="20" readonly value="<%=startDate%>" />
                    <a  href="javascript:showCalendar('EventCreate', 'StartDate', '')"><img src="../common/images/iconSmallCalendar.gif" border="0" valign="absmiddle" /></a>
                    <input type="hidden" name="StartDate_msvalue" value="" />
                </td>
     			<td class="field"></td>
              </tr>
              <tr>
                <td class="label" nowrap="nowrap"><%=XSSUtil.encodeForHTMLAttribute(context, lblEffectiveTo)%></td>
                <td nowrap="nowrap" class="field">
                	<input type="text" id="endDate" name="EndDate" size="20" readonly value="<%=endDate%>" />
                    <a href="javascript:showCalendar('EventCreate','EndDate', '')"><img src="../common/images/iconSmallCalendar.gif" border="0" valign="absmiddle" name="img2" /></a>
                </td>
				<td class="field"></td>
              </tr>
              
              <tr>
                <td class="label" nowrap="nowrap"><%=XSSUtil.encodeForHTMLAttribute(context, lblFrequency)%></td>
				<td class="field">
					<div id="recurrencePattern">
				    <%
					StringList slFrequencyRange = calendar.getFrequencyAttributeRange(context);
					StringList slFrequencyDisplayRange = calendar.getFrequencyAttributeDisplayRange(context, slFrequencyRange);
						for(int index = 0; index < slFrequencyRange.size(); index++){
							String recurrenceType = (String)slFrequencyRange.get(index);
							String recurrenceTypeId = "recurrenceType" + recurrenceType;
							String displayRecurrenceType = (String)slFrequencyDisplayRange.get(index);
				    %>
						<input type="radio" id="<%=XSSUtil.encodeForHTMLAttribute(context, recurrenceTypeId)%>" value="<%=XSSUtil.encodeForHTMLAttribute(context, recurrenceType)%>" name="recurrenceTypeRadio"><label class="label" for="<%=XSSUtil.encodeForHTMLAttribute(context, recurrenceTypeId)%>"><%=XSSUtil.encodeForHTMLAttribute(context, displayRecurrenceType)%></label>
					<%	}  %>	
						<script>
							var frequency = '<%=XSSUtil.encodeForJavaScript(context, frequency)%>';
							if("Weekly" === frequency){
								$("#recurrenceTypeWeekly").attr('checked', 'checked');
							}else if("Monthly" === frequency){
								$("#recurrenceTypeMonthly").attr('checked', 'checked');								
							}else if("Daily" === frequency){
								$("#recurrenceTypeDaily").attr('checked', 'checked');
							}else {
								$("#recurrenceTypeYearly").attr('checked', 'checked');
							}
						</script>
	     	         </div>
	    		</td>
				<td class="field"></td>
              </tr>
              <tr class="trHiddenRecurrencePatternWeekly">
                <td class="label" nowrap="nowrap"><%=XSSUtil.encodeForHTMLAttribute(context, lblWeeklyPattern)%></td>
				<td class="field">
              		<div id="weekCheckboxGroup">
					<%for(int index=0; index<days.size(); index++){
						String day = (String)days.get(index);
						String dayGlobalized = (String)daysGlobalized.get(index);
						String checkBoxId = "cb" + day;
						if(slWeeklyFrequencyPattern.contains(day)){%>
							<input type="checkbox" id="<%=XSSUtil.encodeForHTMLAttribute(context, checkBoxId)%>" name="<%=XSSUtil.encodeForHTMLAttribute(context, day)%>" checked="checked"><label class="label" for="<%=XSSUtil.encodeForHTMLAttribute(context, checkBoxId)%>"><%=XSSUtil.encodeForHTMLAttribute(context, dayGlobalized)%></label>
						<%}else{ %>
							<input type="checkbox" id="<%=XSSUtil.encodeForHTMLAttribute(context, checkBoxId)%>" name="<%=XSSUtil.encodeForHTMLAttribute(context, day)%>"><label class="label" for="<%=XSSUtil.encodeForHTMLAttribute(context, checkBoxId)%>"><%=XSSUtil.encodeForHTMLAttribute(context, dayGlobalized)%></label>
						<%	} 
						} %>
					</div>
	    		</td>
				<td class="field"></td>
              </tr>
              <tr class="trHiddenRecurrencePatternMonthly">
                <td class="label" nowrap="nowrap"><%=XSSUtil.encodeForHTMLAttribute(context, lblMonthlyPattern)%></td>
				<td class="field">
					<input type="radio" name="monthlyRecurringPattern" value="monthlyRecurringDate" id="radioMonthlyRecurringDate">
						<label>Day</label>
						<select id="monthlyRecurringDate">
				            <framework:optionList optionList="<%=slDaysInMonth%>" valueList="<%=slDaysInMonth%>" selected="<%=XSSUtil.encodeForHTMLAttribute(context, sWeekOfMonth)%>"/>
						</select>
              			<label><%=XSSUtil.encodeForHTMLAttribute(context, lblOfEveryMonth)%></label>
              		<br/>
              		<br/>
              		<input type="radio" name="monthlyRecurringPattern" value="monthlyRecurringDay" id="radioMonthlyRecurringDay" />
              			<select id="monthlyRecurringWeekOfMonth">
				            <framework:optionList optionList="<%=slWeekDayOccurencesDisplay%>" valueList="<%=slWeekDayOccurencesStorage%>" selected="<%=sWeekOfMonth%>"/>
						</select>
						<select id="monthlyRecurringDayOfWeek">
				            <framework:optionList optionList="<%=slWeekDaysDisplay%>" valueList="<%=slWeekDaysStorage%>" selected="<%=XSSUtil.encodeForHTMLAttribute(context, sDayOfWeek)%>"/>
						</select>
              			<label><%=XSSUtil.encodeForHTMLAttribute(context, lblOfEveryMonth)%></label>              			
              			<framework:ifExpr expr='<%= isMonthlyReccurenceByDay %>'>
							<script>
								var weekOfMonth = '<%=XSSUtil.encodeForHTMLAttribute(context, sWeekOfMonth)%>';
								var dayOfWeek = '<%=XSSUtil.encodeForHTMLAttribute(context, sDayOfWeek)%>';
								$("#radioMonthlyRecurringDay").attr('checked', 'checked');
								$("#monthlyRecurringWeekOfMonth").val(weekOfMonth);
								$("#monthlyRecurringDayOfWeek").val(dayOfWeek);
							</script>
              			</framework:ifExpr>
              			<framework:ifExpr expr='<%= !isMonthlyReccurenceByDay %>'>
							<script>
								var date = '<%=XSSUtil.encodeForHTMLAttribute(context, sDayOfMonth)%>';
								$("#radioMonthlyRecurringDate").attr('checked', 'checked');
								$("#monthlyRecurringDate").val(date);
							</script>
              			</framework:ifExpr>
        		</td>
				<td class="field"></td>
              </tr>
              <tr class="trHiddenRecurrencePatternYearly">
                <td class="label" nowrap="nowrap"><%=XSSUtil.encodeForHTMLAttribute(context, lblYearlyPattern)%></td>
				<td class="field">
					<input type="radio" name="yearlyRecurringPattern" value="yearlyRecurringDate" id="radioYearlyRecurringDate">
						<label><%=XSSUtil.encodeForHTMLAttribute(context, lblDay)%></label>
						<select id="yearlyRecurringDate" onchange="populateMonths(this)">
				            <framework:optionList optionList="<%=slDaysInMonth%>" valueList="<%=slDaysInMonth%>" selected="<%=XSSUtil.encodeForHTMLAttribute(context, sWeekOfMonth)%>"/>
						</select>
              			<label><%=XSSUtil.encodeForHTMLAttribute(context, lblOf)%>&nbsp;</label>
              			<select id="yearlyrecurringMonthOfYear1">
				            <framework:optionList optionList="<%=slMonthsDisplay%>" valueList="<%=slMonthsStorage%>" selected="<%=sMonthOfYear%>"/>
						</select>
              		<br/>
              		<br/>
              		<input type="radio" name="yearlyRecurringPattern" value="yearlyRecurringDay" id="radioYearlyRecurringDay">
              			<select id="yearlyRecurringWeekOfMonth">
				            <framework:optionList optionList="<%=slWeekDayOccurencesDisplay%>" valueList="<%=slWeekDayOccurencesStorage%>" selected="<%=sWeekOfMonth%>"/>
						</select>
						<select id="yearlyRecurringDayOfWeek">
				            <framework:optionList optionList="<%=slWeekDaysDisplay%>" valueList="<%=slWeekDaysStorage%>" selected="<%=XSSUtil.encodeForHTMLAttribute(context, sDayOfWeek)%>"/>
						</select>
              			<label><%=XSSUtil.encodeForHTMLAttribute(context, lblOf)%>&nbsp;</label>              			
						<select id="yearlyrecurringMonthOfYear2">
				            <framework:optionList optionList="<%=slMonthsDisplay%>" valueList="<%=slMonthsStorage%>" selected="<%=sMonthOfYear%>" />
						</select>
              			<framework:ifExpr expr='<%= isYearlyReccurenceByDay %>'>
							<script>
								var weekOfMonth = '<%=XSSUtil.encodeForHTMLAttribute(context, sWeekOfMonth)%>';
								var dayOfWeek = '<%=XSSUtil.encodeForHTMLAttribute(context, sDayOfWeek)%>';
								var monthOfYear = '<%=XSSUtil.encodeForHTMLAttribute(context, sMonthOfYear)%>';
								$("#radioYearlyRecurringDay").attr('checked', 'checked');
								$("#yearlyRecurringWeekOfMonth").val(weekOfMonth);
								$("#yearlyRecurringDayOfWeek").val(dayOfWeek);
								
							</script>
              			</framework:ifExpr>
              			<framework:ifExpr expr='<%= !isYearlyReccurenceByDay %>'>
							<script>
								var date = '<%=XSSUtil.encodeForHTMLAttribute(context, sDayOfMonth)%>';
								var monthOfYear = '<%=XSSUtil.encodeForHTMLAttribute(context, sMonthOfYear)%>';
								$("#radioYearlyRecurringDate").attr('checked', 'checked');
								$("#yearlyRecurringDate").val(date);
							
							</script>
              			</framework:ifExpr>
	    		</td>
				<td class="field"><button id=triggerSubmitEvent style="display: none;"></button></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </form>
  </body>

  <script language="javascript" type="text/javaScript">
  	var calendarId = "<%=XSSUtil.encodeForJavaScript(context, calendarId)%>";
	var relId = "<%=XSSUtil.encodeForJavaScript(context, relId)%>";
  $(function(){
		//Draw Form		
		$( "#exceptionTypes" ).buttonset();
		$( "#recurrencePattern" ).buttonset();
		$( "#weekCheckboxGroup" ).buttonset();
		//Load Time sliders
		var startTime = <%=iStartTime%>;
		var finishTime = <%=iFinishTime%>;
		var recessStartTime = <%=iLunchStartTime%>;
		var recessFinishTime = <%=iLunchFinishTime%>;
		var recessTimesCount = <%=recessCount%>;
		var relId = '<%=relId%>';
		var criteria = '*[id^="divLunchHours"]';
		var divLunchHoursElements = document.querySelectorAll(criteria);
		var recessStartEndTimes= '<%=recesTimingsStr%>';
		
		var length = divLunchHoursElements.length;

		var exceptionType = '<%=XSSUtil.encodeForJavaScript(context, exceptionType)%>';
		
		var recessArr = recessStartEndTimes.split(",");
		var lunchHourId = "divLunchHours_0";
		if(recessTimesCount > 1){
			//modifying existing event
			for(var i=0;i<length;i++){
				lunchHourId = divLunchHoursElements[i].id;
				var recessTimeStr = recessArr[i];
				var recessTimeArr = recessTimeStr.split("-");

				options = {"mode": "Lunch Hours", "values": [recessTimeArr[0], recessTimeArr[1]], "disabled": true};
				loadTimeSlider(calendarId, relId, lunchHourId, "divWorkHours",null, options, "table_createEvent");
		}
		}else if(recessTimesCount == 1){
			//event create dialog
			var recessTimeStr = recessArr[0];
			if(recessTimeStr != "" ){
				var recessTimeArr = recessTimeStr.split("-");
				recessStartTime = recessTimeArr[0];
				recessFinishTime = recessTimeArr[1];
			}
			options = {"mode": "Lunch Hours", "values": [recessStartTime, recessFinishTime], "disabled": true};
			loadTimeSlider(calendarId, relId, "divLunchHours_0", "divWorkHours", null, options ,"table_createEvent");
		}else{
			//modifying existing event with no recess
			var midPoint = (startTime + finishTime)/2 ;
			var noRecessStartEndTime = Math.floor(midPoint) -1;
			var nostartTime = Math.floor(midPoint) -1;
	    	var nofinishTime = Math.floor(midPoint) +1;
			
	    	options = {"mode": "Lunch Hours", "values": [noRecessStartEndTime, noRecessStartEndTime], "disabled": true};
			loadTimeSlider(calendarId, relId, "divLunchHours_0", "divWorkHours", null, options ,"table_createEvent");
			  
		}
		
		var options = {"mode": "Work Hours", "values": [startTime, finishTime], "disabled": true};
		loadTimeSlider(calendarId, relId, "divWorkHours", lunchHourId, null, options,"table_createEvent");
		

		//Populate Form 
		var frequency = '<%=XSSUtil.encodeForJavaScript(context, frequency)%>';
		
		if("Working" === exceptionType){
			toggleTimeSlider();
		}	
		if(frequency != undefined && frequency != null &&  frequency != "null" ){
			toggleEventFrequency(frequency);
		}else{
			toggleEventFrequency("Yearly");
		}
		
		
	});

	var toggleTimeSlider  = function(){
		var isWorkingHoursDisabled = $("#divWorkHours").slider("option", "disabled");
		var isLunchHoursDisabled = 	$("#divLunchHours").slider("option", "disabled"); 
		var criteria = '*[id^="divLunchHours"]';
		var divLunchHoursElements = document.querySelectorAll(criteria);
		if (isWorkingHoursDisabled){
			//Enable Time slider
			$("#divWorkHours").slider("option", "disabled", false);
			$("#divLunchHours").slider("option", "disabled", false);
			
			
			for(var i =0; i< divLunchHoursElements.length; i++){
				var recessId = "#"+divLunchHoursElements[i].id;
				$(recessId).slider("option", "disabled", false);
			}
			
			//Enable Row in which time slider is present.
			$(".trHiddenWorkingHours").show();
		}else{
			//Disable Time slider 
			$("#divWorkHours").slider("option", "disabled", true);
			$("#divLunchHours").slider("option", "disabled", true);
			
			for(var i =0; i< divLunchHoursElements.length; i++){
				var recessId = "#"+divLunchHoursElements[i].id;
				$(recessId).slider("option", "disabled", true);
			}
			
			//Disable Row in which time slider is present.
			$(".trHiddenWorkingHours").hide();
		}
	};
	
	var toggleEventFrequency  = function(frequency){
		$(".trHiddenRecurrencePatternWeekly").hide();
		$(".trHiddenRecurrencePatternMonthly").hide();
		$(".trHiddenRecurrencePatternYearly").hide();
		$(".trHiddenRecurrencePatternDaily").hide();		
		var style = ".trHiddenRecurrencePattern" + frequency;
		$(style).show();
	};

	//On change Calendar Exception Type 
	$( "#exceptionTypeNonWorking" ).change(function(){
		toggleTimeSlider();
	});		
	$( "#exceptionTypeWorking" ).change(function(){
		toggleTimeSlider();
	});

	//On change Recurrence type 
	$( "#recurrenceTypeWeekly" ).change(function(){
		toggleEventFrequency("Weekly");
	});
	$( "#recurrenceTypeMonthly" ).change(function(){
		toggleEventFrequency("Monthly");
	});
	$( "#recurrenceTypeYearly" ).change(function(){
		toggleEventFrequency("Yearly");
	});
	$( "#recurrenceTypeDaily" ).change(function(){
		toggleEventFrequency("Daily");
	});
	
    f = document.EventCreate;
    var bool = false;

    function closeWindow() {
    	parent.window.closeWindow();
    }
    
    function submit() {
    	
    	var mode = '<%=XSSUtil.encodeForJavaScript(context, mode)%>';
    	var exceptionType = '<%=XSSUtil.encodeForJavaScript(context, exceptionType)%>';
    	var isWorkingException = $( "#exceptionTypeWorking")[0].checked;
    	var noOverllaping = true;
    	var workingTimes ;
    	if(isWorkingException){
    		noOverllaping = validateRecessTimes("table_createEvent");
    		workingTimes = getSortedWorkingHours("table_createEvent");
    	}else{
    		workingTimes = getSortedWorkingHoursForNonWorkingDay("table_createEvent", mode);
    	}
    	  	
    	
 		if((noOverllaping && isWorkingException) || ! isWorkingException ){
		var workingHours = getSelectedWorkingHours();  
		//var lunchHours = getSelectedLunchHours();
		var lunchHours = null;
		//var workingTimes = getSortedWorkingHours("table_createEvent");
		var monthlyRecurringDate = getMonthlyRecurringDate();
		var monthlyRecurringDayOfWeek = getMonthlyRecurrringDayOfWeek();
		var monthlyRecurringWeekOfMonth = getMonthlyRecurrringWeekOfMonth();
		
		var yearlyRecurringDayOfWeek = getYearlyRecurrringDayOfWeek();
		var yearlyRecurringDate = getYearlyRecurringDate();
		var yearlyRecurringWeekOfMonth = getYearlyRecurrringWeekOfMonth();
		var yearlyRecurringMonthOfYear = getYearlyRecurrringMonthOfYear();
     if (!bool) {
      if (validateForm()) {
         bool = true;
         //f.action="WorkCalendarEventCreateProcess.jsp?workingHours=" + workingHours  + "&monthlyRecurringDate=" + monthlyRecurringDate + "&monthlyRecurringDayOfWeek=" + monthlyRecurringDayOfWeek + "&monthlyRecurringWeekOfMonth=" + monthlyRecurringWeekOfMonth + "&yearlyRecurringDate=" + yearlyRecurringDate + "&yearlyRecurringDayOfWeek=" + yearlyRecurringDayOfWeek + "&yearlyRecurringWeekOfMonth=" + yearlyRecurringWeekOfMonth + "&yearlyRecurringMonthOfYear=" + yearlyRecurringMonthOfYear + "&workingTimes="+workingTimes;
         var actionUrl = "WorkCalendarEventCreateProcess.jsp?monthlyRecurringDate=" + monthlyRecurringDate + "&monthlyRecurringDayOfWeek=" + monthlyRecurringDayOfWeek + "&monthlyRecurringWeekOfMonth=" + monthlyRecurringWeekOfMonth + "&yearlyRecurringDate=" + yearlyRecurringDate + "&yearlyRecurringDayOfWeek=" + yearlyRecurringDayOfWeek + "&yearlyRecurringWeekOfMonth=" + yearlyRecurringWeekOfMonth + "&yearlyRecurringMonthOfYear=" + yearlyRecurringMonthOfYear;
         if(!(! isWorkingException && "edit" === mode)){
        	 actionUrl +=  "&workingTimes="+workingTimes;
         }
         
         f.action= actionUrl;
         f.submit();
         if(Browser.FIREFOX){
	    		$("#triggerSubmitEvent").click();
	     }
		 
      }
     }
    }
    }

    function validateForm() {
      var badChars = checkForNameBadCharsList(f.Title);
      if (badChars != "") {
        alert('<%=errInvalidChars%>');
        f.Title.focus();
        return false;
      }

      f.Title.value = trimWhitespace(f.Title.value);
      if (f.Title.value == ''){
        alert('<%=errEnterATitle%>');
        f.Title.focus();
        return false;
      }
        var start = new Date(f.StartDate.value);
        var end = new Date(f.EndDate.value);
        if (start.getTime() > end.getTime()) {
    	  alert('<%=errInvalidDateRange%>');
          f.StartDate.focus();
          return false;
        }
      return true;
    }
  </script>

</html>

<%@include file = "../emxUICommonEndOfPageInclude.inc"%>
