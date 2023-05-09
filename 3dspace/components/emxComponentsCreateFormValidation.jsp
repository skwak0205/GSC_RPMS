<%--  emxComponentsCreateFormValidation.jsp

   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of MatrixOne,Inc.
   Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program
   
   static const char RCSID[] = $Id: emxComponentsCreateFormValidation.jsp.rca 1.3.3.2.2.2 Fri Dec 19 10:32:15 2008 ds-lmanukonda Experimental $

 --%>
<%@include file = "../emxUICommonAppNoDocTypeInclude.inc"%>
 <%@ page import = "com.matrixone.apps.framework.ui.UINavigatorUtil" %> 
 <%@ page import = "com.matrixone.apps.domain.util.FrameworkProperties" %> 
<%@ page import = "com.matrixone.apps.domain.*,matrix.util.*,com.matrixone.apps.common.util.*,com.matrixone.apps.common.*" %>

<%!
	private String getI18NString(String key, String language) throws Exception {
    	return i18nNow.getI18nString(key, "emxComponentsStringResource", language);
	}
	private String getI18NString(Context context, String key, String bundle, String language) {
		return EnoviaResourceBundle.getProperty(context, bundle, new Locale(language), key);
	}

%>
 <%
   out.clear();
   com.matrixone.apps.domain.util.i18nNow i18nUtil = new com.matrixone.apps.domain.util.i18nNow();
   String ResFormFileId = "emxComponentsStringResource";
   String languageStr = request.getHeader("Accept-Language");
   String meetingDurationAlert1 = EnoviaResourceBundle.getProperty(context,ResFormFileId,context.getLocale(), "emxComponents.Alert.MreThan500"); 
   String meetingDurationAlert2 =EnoviaResourceBundle.getProperty(context,ResFormFileId,context.getLocale(), "emxComponents.Alert.LessThan1");  
   String meetingDateAlert = EnoviaResourceBundle.getProperty(context,ResFormFileId,context.getLocale(), "emxComponents.Alert.MeetingDate");
   String meetingTimeAlert = EnoviaResourceBundle.getProperty(context,ResFormFileId,context.getLocale(), "emxComponents.Alert.MeetingTime");
   String agendaSequenceMaxLimit = EnoviaResourceBundle.getProperty(context,"emxComponents.SequenceMaxLimit");
   String meetingDurationAlert =EnoviaResourceBundle.getProperty(context,ResFormFileId,context.getLocale(), "emxComponents.Alert.MeetingDuration");
   String nonNumericValueAlert = EnoviaResourceBundle.getProperty(context,ResFormFileId,context.getLocale(), "emxComponents.Alert.NonNumericValue");
   
   String isUniqueCageCode = EnoviaResourceBundle.getProperty(context,"emxComponents.cageCode.Uniqueness");
   isUniqueCageCode = (isUniqueCageCode!=null && isUniqueCageCode.trim().equalsIgnoreCase("true")) ? "true" : "false";
   int allowedDunsDigit = Integer.parseInt(EnoviaResourceBundle.getProperty(context,"emxComponents.allowedDUNSNumberDigitDisplay"));
   String attrDUNS      = PropertyUtil.getSchemaProperty(context, "attribute_DUNSNumber");
   String attrWebSite   = PropertyUtil.getSchemaProperty(context, "attribute_WebSite");
   
   DomainObject personObject=PersonUtil.getPersonObject(context);
   StringList obSelects = new StringList();
   obSelects.add(DomainConstants.SELECT_NAME);
   obSelects.add(DomainConstants.SELECT_ID);
   obSelects.add(DomainConstants.SELECT_ATTRIBUTE_TITLE);
   Map companyList = personObject.getRelatedObject(context,DomainConstants.RELATIONSHIP_EMPLOYEE,false,obSelects,null);
   String organizationName=(String)companyList.get(DomainConstants.SELECT_NAME);
   String organizationId=(String)companyList.get(DomainConstants.SELECT_ID);
   String organizationTitle=(String)companyList.get(DomainConstants.SELECT_ATTRIBUTE_TITLE);
   StringList companyInfo = Issue.getUserCompanyIdName(context);
	String strCompanyId = (String) companyInfo.elementAt(0);
	String strCompanyName = (String) companyInfo.elementAt(1);
	String strCompanyTitle = (String) companyInfo.elementAt(2);
 %>
 
 //Start Changes for Retention Schedule
    var msg3 = "<%= EnoviaResourceBundle.getProperty(context,ResFormFileId,context.getLocale(), "emxComponents.Alert.checkDateMsg")%>"; 
    var msg4 = "<%= EnoviaResourceBundle.getProperty(context,ResFormFileId,context.getLocale(), "emxComponents.Alert.isInvalid")%>";
   var meetingDuration1 = "<%=XSSUtil.encodeForJavaScript(context, meetingDurationAlert1)%>";
   var meetingDuration2 = "<%=XSSUtil.encodeForJavaScript(context, meetingDurationAlert2)%>";
   var meetingDateAlert = "<%=XSSUtil.encodeForJavaScript(context, meetingDateAlert)%>";
   var meetingTimeAlert = "<%=XSSUtil.encodeForJavaScript(context, meetingTimeAlert)%>";
   var meetingDurationAlert = "<%=XSSUtil.encodeForJavaScript(context, meetingDurationAlert)%>";
    var nonNumericValueAlert = "<%=XSSUtil.encodeForJavaScript(context, nonNumericValueAlert)%>";
	var chooseFromUG = false;
	var isFirstTime = true;
	var originalChooseUgValue = "";
function checkBadNameChars(fieldName) {
    if(!fieldName)
        fieldName=this;
    var badChars = "";
	 var name;  	  
      if(fieldName.title!="undefined" && fieldName.title!="" && fieldName.title!="null"){
    	  	name = fieldName.title;
    	  }
      else {
    	  	name = fieldName.name;
    	  }
    badNameChars=checkForNameBadChars(fieldName, true);
    if ((badNameChars).length != 0) {
    	//XSSOK
    	var nameAllBadCharName = getAllNameBadChars(fieldName);
    	//XSSOK
		msg = "<%=getI18NString(context,"emxComponents.Alert.InvalidChars", ResFormFileId, languageStr)%>";
        msg += badNameChars;
        //XSSOK
		msg += "<%=getI18NString(context,"emxComponents.Common.AlertInvalidInput",ResFormFileId, languageStr)%>";
        msg += nameAllBadCharName;
        //XSSOK
		msg += "<%=getI18NString(context,"emxComponents.Alert.RemoveInvalidChars",ResFormFileId, languageStr)%> ";
        msg += name;
        //XSSOK
		msg += "  <%=getI18NString(context, "emxComponents.Alert.Field", ResFormFileId,languageStr)%> ";
        fieldName.focus();
        alert(msg);
          return false;
     }
  return true;
  } //End Function 

function checkNonProductionDays()
{
   var isStringValid = false;
   var arrobj= null;
   var strNonPaymentDays = this.value;
   var arrNonPaymentDays= new Array(strNonPaymentDays.length);

  //Date Format to be enetered should be of Format dd/mm/yyyy,dd/mm/yyyy    
   arrNonPaymentDays=strNonPaymentDays.split(",");
      for(var i = 0;i<arrNonPaymentDays.length;i++)
      {
          arrobj = arrNonPaymentDays[i];
          /*isDate() does the validation on the Value enetered in the Non Producation Days Text Area.The String can only have comma      seperated Date values.*/
          var isValidDate = isValidDateFormat(arrobj);

        if(isValidDate)
         {
            isStringValid = true;
         }
    else
     {
            isStringValid = false;
            break;
     }
    }

    if(isStringValid)
    {
        return true;
    }
    else
    {
    alert(msg3+" "+arrobj+ " "+msg4);
    }
}//End Function 

//validating date in mm/dd/yyyy format
function isValidDateFormat(DateToCheck)
{
  if(DateToCheck==""){return true;}
      if(DateToCheck.indexOf("/") < 0){
       return false;
    }

  var m_arrDate = DateToCheck.split("/");
      if(m_arrDate.length != 3){
       return false;
    }
    
    var m_MONTH = m_arrDate[0];
    var m_DAY   = m_arrDate[1];
    var m_YEAR  = m_arrDate[2]; 
    
   if(m_DAY.length > 2 || m_MONTH.length > 2 || m_YEAR.length != 4)
    {
        return false;
    }
    var m_strDate = m_MONTH + "/" + m_DAY + "/" + m_YEAR;
    var testDate=new Date(m_strDate);

    if(testDate.getMonth()+1==m_MONTH)
    {
      return true;
    }
    else
    {
        return false;
    }
}

function isDate(DateToCheck)
{

    if(DateToCheck==""){return true;}

    var m_strDate = FormatDate(DateToCheck);

    if(m_strDate==""){

    return false;

    }

    var m_arrDate = m_strDate.split("/");
    var m_DAY = m_arrDate[0];
    var m_MONTH = m_arrDate[1];
    var m_YEAR = m_arrDate[2];

    if(m_YEAR.length > 4)
    {
        return false;
    }

    m_strDate = m_MONTH + "/" + m_DAY + "/" + m_YEAR;

    var testDate=new Date(m_strDate);

    if(testDate.getMonth()+1==m_MONTH)
    {
      return true;
    }
    else
    {
        return false;
    }

}//end function


function FormatDate(DateToFormat,FormatAs){

    if(DateToFormat==""){return"";}

    if(!FormatAs){FormatAs="dd/mm/yyyy";}

     

    var strReturnDate;

    FormatAs = FormatAs.toLowerCase();

    DateToFormat = DateToFormat.toLowerCase();

    var arrDate

    var arrMonths = new Array("January","February","March","April","May","June","July","August","September","October","November","December");

    var strMONTH;

    var Separator;

    while(DateToFormat.indexOf("st")>-1){

    DateToFormat = DateToFormat.replace("st","");

    }

     

    while(DateToFormat.indexOf("nd")>-1){

    DateToFormat = DateToFormat.replace("nd","");

    }

    while(DateToFormat.indexOf("rd")>-1){

    DateToFormat = DateToFormat.replace("rd","");

    }

    while(DateToFormat.indexOf("th")>-1){

    DateToFormat = DateToFormat.replace("th","");

    }
     

    if(DateToFormat.indexOf(".")>-1){

    Separator = ".";

    }

     

    if(DateToFormat.indexOf("-")>-1){
       Separator = "-";
    }
    if(DateToFormat.indexOf("/")>-1){

    Separator = "/";

    }

    if(DateToFormat.indexOf(" ")>-1){

    Separator = " ";

    }

    arrDate = DateToFormat.split(Separator);

    DateToFormat = "";

                for(var iSD = 0;iSD < arrDate.length;iSD++){

                            if(arrDate[iSD]!=""){

                            DateToFormat += arrDate[iSD] + Separator;

                            }

                }

    DateToFormat = DateToFormat.substring(0,DateToFormat.length-1);

    arrDate = DateToFormat.split(Separator);

     

    if(arrDate.length < 3){

    return "";

    }

    var DAY = arrDate[0];

    var MONTH = arrDate[1];

    var YEAR = arrDate[2];

    if(parseFloat(arrDate[1]) > 12){

    DAY = arrDate[1];

    MONTH = arrDate[0];

    }

    if(parseFloat(DAY) && DAY.toString().length==4){

    YEAR = arrDate[0];

    DAY = arrDate[2];

    MONTH = arrDate[1];

    }
    for(var iSD = 0;iSD < arrMonths.length;iSD++){

    var ShortMonth = arrMonths[iSD].substring(0,3).toLowerCase();

    var MonthPosition = DateToFormat.indexOf(ShortMonth);

                if(MonthPosition > -1){

                MONTH = iSD + 1;

                            if(MonthPosition == 0){

                            DAY = arrDate[1];

                            YEAR = arrDate[2];

                            }

                break;

                }

    }

    var strTemp = YEAR.toString();

    if(strTemp.length==2){

     

                if(parseFloat(YEAR)>40){

                YEAR = "19" + YEAR;

                }

                else{

                YEAR = "20" + YEAR;

                }

     

    }

                if(parseInt(MONTH)< 10 && MONTH.toString().length < 2){

                MONTH = "0" + MONTH;

                }

                if(parseInt(DAY)< 10 && DAY.toString().length < 2)
                                {
                DAY = "0" + DAY;
                }

                switch (FormatAs){

                case "dd/mm/yyyy":
                return DAY + "/" + MONTH + "/" + YEAR;

                case "mm/dd/yyyy":
                return MONTH + "/" + DAY + "/" + YEAR;
                
                case "dd/mmm/yyyy":
                return DAY + " " + arrMonths[MONTH -1].substring(0,3) + " " + YEAR;
                
                                case "mmm/dd/yyyy":
                return arrMonths[MONTH -1].substring(0,3) + " " + DAY + " " + YEAR;
                
                                case "dd/mmmm/yyyy":
                return DAY + " " + arrMonths[MONTH -1] + " " + YEAR;   
                
                                case "mmmm/dd/yyyy":
                return arrMonths[MONTH -1] + " " + DAY + " " + YEAR;

                }

    return DAY + "/" + strMONTH + "/" + YEAR;


} //End Function
// Method to check for the Duration of Meeting is less than 500 Min.
function checkMeetingDuration()
  {
 // alert("Inside check Meeting Duration!!!");
	  if(this.value > 500)
	  { 
	  	alert(meetingDuration1);
	 	// this.value.focus();
	  }else if(this.value < 1){
	  	alert(meetingDuration2);
	 	// this.value.focus();
	 		
	  }
	  return ((this.value < 500) && (this.value > 1));
  }
  function pastDateCheck()
  {
  	var strMeeingDate = document.forms[0].MeetingDate_msvalue.value;
  	var strMeetingTime = document.forms[0].StartTime.value;
  	if (trimWhitespace(strMeeingDate) == '')
  		return true; // AEF will take care of mandatory field validation
  	
  	var fieldMeeingDate = new Date(new Date(parseInt(strMeeingDate)).toDateString() + " " + strMeetingTime);
    var fieldDay = fieldMeeingDate.getDate();

    var currentDate = new Date();
    var currentDay = currentDate.getDate();
    
    var fieldDateMod = Date.parse(fieldMeeingDate); 
    var currentDateMod = Date.parse(currentDate);
    
    if (fieldDateMod < currentDateMod) {
    	if(fieldDay == currentDay){
    		alert(meetingTimeAlert);
    	}else {
    		alert(meetingDateAlert);
    	}
    	return false;
    }
    
    return true;
  }

function validateMeetingDuration()
{
	var strMeetingDuration = document.forms[0].Duration.value;	
	var returnFlag = true;
		
    if(trimWhitespace(strMeetingDuration) == '')		
		returnFlag = true; // AEF will take care of mandatory field validation  
	if(isNaN(strMeetingDuration))
	{
		alert(nonNumericValueAlert +" <%=XSSUtil.encodeForJavaScript(context, getI18NString("emxComponents.Common.Duration", languageStr))%>" );		
		returnFlag = false;
	}else if(strMeetingDuration > 0 && strMeetingDuration <= 500)
	{
		returnFlag = true;		
	}
	else
	{
		alert(meetingDurationAlert);
		returnFlag = false;	
	}	
return returnFlag;
}

function showRouteCreateWSChooser() {
    flag = false;
    for(var i=0; i < document.forms[0].selscope.length; i++) {
      if (document.forms[0].selscope[i].checked && document.forms[0].selscope[i].value == "ScopeName") {
        flag = true;
      }
    }
    if(flag) {
      var folderChooserURL = "../common/emxIndentedTable.jsp?expandProgram=emxWorkspace:getWorkspaceFoldersForSelection&table=TMCSelectFolder&program=emxWorkspace:getDisabledWorkspaces&header=emxComponents.CreateRoute.SelectScope&submitURL=../components/emxCommonSelectWorkspaceFolderProcess.jsp&type=Route&&suiteKey=Components&submitLabel=emxFramework.Common.Done&cancelLabel=emxFramework.Common.Cancel&displayView=details&customize=false";
      showModalDialog(folderChooserURL, "100", "100", false, "Medium");
    } else {
       alert("<%=XSSUtil.encodeForJavaScript(context, getI18NString("emxComponents.CreateRoute.WorkspaceAvailability", languageStr))%>");
    }
  }
  
//Start
//Checking for Bad characters in the Text Area field
function checkBadChars(fieldName)
{
    if(!fieldName)
    fieldName=this;
    var badChars = "";
    badChars=checkForBadChars(fieldName);
	var name;  	  
      if(fieldName.title!="undefined" && fieldName.title!="" && fieldName.title!="null"){
    	  	name = fieldName.title;
    	  }
      else {
    	  	name = fieldName.name;
    	  }
    if ((badChars).length != 0)
    {
        msg = "<%=getI18NString(context,"emxComponents.Common.AlertInValidChars",ResFormFileId, languageStr)%>";
        msg += badChars;
        msg += "<%=getI18NString(context,"emxComponents.Common.AlertRemoveInValidChars",ResFormFileId, languageStr)%>";
        msg += name;
        msg += "<%=getI18NString(context,"emxComponents.Alert.Field", ResFormFileId,languageStr)%> ";
        fieldName.focus();
        alert(msg);
        return false;
    }
    return true;
}



function ValidateEstimateDates(){
		var StartDate = document.getElementsByName("EstimatedStartDate");
		var EndDate = document.getElementsByName("EstimatedFinishDate");
		var StartDate_msvalue =  document.getElementsByName("EstimatedStartDate_msvalue");
		var EndDate_msvalue =  document.getElementsByName("EstimatedFinishDate_msvalue");
		
		
		if( StartDate[StartDate.length - 1]!="" &&  EndDate[EndDate.length - 1]!="")
		{
       if( StartDate[StartDate.length - 1].value!="" &&  EndDate[EndDate.length - 1].value!="")
			{
			if( StartDate_msvalue[StartDate_msvalue.length - 1].value> EndDate_msvalue[EndDate_msvalue.length - 1].value)
				{
					alert("<%=getI18NString(context,"emxComponents.Common.Issue.Alert.EstimatedStartAndEndDates", ResFormFileId, languageStr)%>");
					return false;
				}
			}
		}
		return true;
	}


function createMemberListOnLoad()
{
if(document.forms[0].Scope[0] != undefined)
document.forms[0].Scope[0].checked=true;
else
document.forms[0].Scope.checked=true;
disableOrganization();
}

function disableOrganization()
{
  for(i=0;i<document.forms[0].elements.length;i++)
  {
    if(document.forms[0].elements[i].name == 'owningBU')
    {
    document.forms[0].elements[i+2].disabled=true;
     document.forms[0].owningBUDisplay.value="";
     document.forms[0].owningBUOID.value="";
    break;
    }
  }
}

function enableOrganization(name,id)
{
  for(i=0;i<document.forms[0].elements.length;i++)
  {
    if(document.forms[0].elements[i].name == 'owningBU')
    {
    document.forms[0].elements[i+2].disabled=false;
    document.forms[0].owningBUDisplay.value=name;
    document.forms[0].owningBUOID.value=id;
    break;
    }
  }
}

function changeOwningOrganizationField()
{
if(document.forms[0].Scope[1].checked == true){
enableOrganization("<%= XSSUtil.encodeForJavaScript(context, organizationTitle) %>","<%= XSSUtil.encodeForJavaScript(context, organizationId) %>")
}
else{
disableOrganization();
}
}

function validateDepartmentName(fieldName) {
  		if(!fieldName) {
			 fieldName=this;
   		}
   		var orgType = "Department";
  		var isNameValid = validateName(fieldName,orgType);
  		if(!isNameValid)
  			return false;

	  	return validateOrgName(trim(fieldName.value));	
}
  
function validatePlantName(fieldName) {
  		if(!fieldName) {
			 fieldName=this;
   		}
   		var orgType = "Plant"; 
  		var isNameValid = validateName(fieldName,orgType);
  		if(!isNameValid)
  			return false;

	  	return validateOrgName(trim(fieldName.value));	
}
  
function validateOrgName(orgName) {
	  	var url = "../components/emxComponentsOrgNameCheck.jsp?orgName=" + encodeURIComponent(orgName);	
	  	var xmlResult = emxUICore.getXMLData(url);
	    try{
	        var root = xmlResult.documentElement;
			var resultNodeVal = emxUICore.getText(emxUICore.selectSingleNode(root, "/mxOrgNameCheck/result"));
			if(resultNodeVal == "success") {
				return true;
			} else {
				var messageNodeVal = emxUICore.getText(emxUICore.selectSingleNode(root, "/mxOrgNameCheck/errorMsg"));
				alert(messageNodeVal);
				return false;
			} 
	    } catch(e){
	    }
	    return true;
}  



function validateName(fieldName,orgType)
{
	if(!fieldName) {
        fieldName=this;
    }
    var namebadCharName = checkForNameBadCharsList(fieldName);
	
	if (fieldName.value.length == 0 || fieldName.value == "") {
	  if(orgType == "Department"){
      alert("<%=getI18NString(context,"emxComponents.DepartmentDialog.EnterName",ResFormFileId, languageStr)%>");
      } else if(orgType == "Plant"){
      alert("<%=getI18NString(context,"emxComponents.PlantDialog.EnterName",ResFormFileId, languageStr)%>");
      }
      return false;
    } else if (namebadCharName.length != 0) {
      alert("<%=getI18NString(context,"emxComponents.Common.AlertInValidChars",ResFormFileId, languageStr)%>"+namebadCharName+"<%=getI18NString(context,"emxComponents.Common.AlertRemoveInValidChars",ResFormFileId, languageStr)%>");
      return false;
    }
    return true;
}

//Validates DUNS Number
function validateDunsNumber(fieldName) {
    if(!fieldName) {
        fieldName=this;
    }

    if(fieldName.value < 0) {
        alert("<%= XSSUtil.encodeForJavaScript(context, i18nUtil.getAttributeI18NString(attrDUNS,languageStr)) %> " + "<%=XSSUtil.encodeForJavaScript(context, getI18NString("emxComponents.CompanyDialog.PositiveNumber", languageStr))%>");
        return false;
    }
    if(fieldName.value.length > '<%=allowedDunsDigit%>') {
        alert("<%= XSSUtil.encodeForJavaScript(context, i18nUtil.getAttributeI18NString(attrDUNS,languageStr)) %> " + "<%=XSSUtil.encodeForJavaScript(context, getI18NString("emxComponents.Common.allowedDunsNumber", languageStr))%>"+'<%=allowedDunsDigit%>');
        return false;
    }
    return true;
}

//validates website contains space or not
function isWebsiteContainsSpace(fieldName) {
    if(!fieldName) {
        fieldName=this;
    }

    var result = /\s/.test(fieldName.value);
    if(result) {
        alert("<%=XSSUtil.encodeForJavaScript(context, getI18NString("emxComponents.CompanyDialog.Invalid", languageStr))%>" + " <%= XSSUtil.encodeForJavaScript(context, i18nUtil.getAttributeI18NString(attrWebSite,languageStr))%>.");
        return false;
    }
    return true;
}

//validate the URL
function validateURL(fieldName) {
    if(!fieldName) {
        fieldName=this;
    }

    if (fieldName.value != "" ) {
        if ( fieldName.value.indexOf("http://") < 0 && fieldName.value.indexOf("https://") < 0 ) {
          alert("<%=XSSUtil.encodeForJavaScript(context, getI18NString("emxComponents.CompanyDialog.SiteNameAlert", languageStr))%>");
            return false;
        }
    }
    return true;
}

//Validating Postal Code Field in Location Page
function validateLocationPostalCode()
{	
     if (!(isAlphanumeric( trim(this.value), true))) {
          alert("<%=getI18NString(context,"emxComponents.LocationDialog.PleaseTypePostCode",ResFormFileId, languageStr)%>");
          return false;
    } else if ( this.value < 0 ) {
          alert("<%=getI18NString(context,"emxComponents.LocationDialog.PostalCodePositiveNumber",ResFormFileId, languageStr)%>");
          return false;
    } 
    return true;
}            

function validateDeptCageCode()
{
	var namebadCharName = checkForNameBadCharsList(this);
  	
    if (namebadCharName.length != 0) {
     alert("<%=getI18NString(context,"emxComponents.Common.AlertInValidChars",ResFormFileId, languageStr)%>"+namebadCharName+"<%=getI18NString(context,"emxComponents.Common.AlertRemoveInValidChars",ResFormFileId, languageStr)%>");
      return false;
    } else if( ("true" == "<%=isUniqueCageCode%>") && trim(this.value)=="" ) {
        alert("<%=getI18NString(context,"emxComponents.CompanyDialog.EnterCageCode",ResFormFileId,languageStr)%>");
        return false;
    }
    return true;
} 

function validateDeptId()
{

	var namebadCharName = checkForNameBadCharsList(this);
	
	if (this.value.length == 0 || this.value == "") {
      alert("<%=getI18NString(context,"emxComponents.DepartmentDialog.EnterDepartmentId",ResFormFileId, languageStr)%>"); 
      return false;
    } else if (namebadCharName.length != 0) {
      alert("<%=getI18NString(context,"emxComponents.Common.AlertInValidChars",ResFormFileId, languageStr)%>"+namebadCharName+"<%=getI18NString(context,"emxComponents.Common.AlertRemoveInValidChars",ResFormFileId, languageStr)%>");
      return false;
    }
    return true;
}

function validateDeptDesc()
{
	var namebadCharName = checkForBadChars(this);
    if (namebadCharName.length != 0){
      alert("<%=getI18NString(context,"emxComponents.Common.AlertInValidChars",ResFormFileId, languageStr)%>"+namebadCharName+"<%=getI18NString(context,"emxComponents.Common.AlertRemoveInValidChars",ResFormFileId, languageStr)%>");
      return false;
    } else {
    	return true;
    }
    return true;    
}

function validateDeptWebSite()
{
    
    if (validateURL(this) && isWebsiteContainsSpace(this)) {
    	return true;
    } else {
    	return false;
    }
    return false;    
}

function clearDepartmentFormFields() {
	setFieldValue("Department ID", "");
	setFieldValue("Phone Number", "");
	setFieldValue("Fax Number", "");
}

function setFieldValue(fieldName, newValue) {
  	var fieldObj = getTopWindow().document.getElementsByName(fieldName)[0];
  	fieldObj = fieldObj == null ? document.forms[0][fieldName] : fieldObj;
	if(fieldObj ){
  		fieldObj.value = newValue;
	}
}

//[Added::Feb 23, 2011:S4E:R211:IR-088930V6R2012::Start] 
    function validateStandardCost() {
        var standardCostValue = document.forms[0].StandardCost.value;
        if (isNaN(standardCostValue))
         {
            alert("<%=getI18NString(context,"emxComponents.StandardCost.EnterNumericValue",ResFormFileId,languageStr)%>"); 
           return false;
         } 
         else if (parseInt(standardCostValue,10) < 0 )
         {
            alert("<%=getI18NString(context,"emxComponents.StandardCost.EnterPositiveValue",ResFormFileId,languageStr)%>");            
           return false;
         }
         else{
             return true;    
         }
        
    }
//[Added::Feb 23, 2011:S4E:R211:IR-088930V6R2012::End]    

function saveASTemplateAvailabilityChanged() {
	var Availability = document.forms[0].Availability;
	var btnWorkspaceAvailable = document.forms[0].btnWorkspaceAvailable;
	
	var workspaceAvailableDisabled = Availability[Availability.length - 1].checked == false;
	if(btnWorkspaceAvailable) {
		btnWorkspaceAvailable.disabled = workspaceAvailableDisabled;
	} else {
  		for(i = 0; i < document.forms[0].elements.length; i++) {
	    	if(document.forms[0].elements[i].name == 'WorkspaceAvailable') {
    			document.forms[0].elements[i+2].disabled = workspaceAvailableDisabled;
		    	break;
    		}
	  	}
	}
	if(!workspaceAvailableDisabled) {
		document.forms[0].WorkspaceAvailableDisplay.value = "<%=XSSUtil.encodeForJavaScript(context, getI18NString("emxComponents.CreateRoute.SelectScope", languageStr))%>";
	} else {
		basicClear("WorkspaceAvailable");
	}
}

function routeSaveTemplateFormLoad() {
	document.getElementById("Availability").checked = true;
	saveASTemplateAvailabilityChanged();
	
	document.getElementById("SaveTaskAssignees").checked = true;
	var Name = document.getElementById("Name");
	if(Name.value == ""){
			document.getElementById("SaveOptions").checked = 'checked';
			}
	else{
		document.getElementsByName("SaveOptions")[1].checked = 'checked';
		toggleTemplateNameEdit();
		saveASTemplateChooseUsersFromUserGroupChange();
		}
}


function toggleTemplateNameEdit(){
		var SaveOptions = document.getElementsByName("SaveOptions");
		var templateName = document.getElementsByName("Name");
	    var RouteTemplateDisabled = SaveOptions[SaveOptions.length - 1].checked == true;
	    var PreserveTaskOwner = document.getElementsByName("PreserveTaskOwner");
	    var chooseUserGroupOptions = document.getElementsByName("ChooseUsersFromUserGroup");
	    var chooseUserGroupOptionstrue = chooseUserGroupOptions[chooseUserGroupOptions.length - 1].checked == true;
	    if(originalChooseUgValue === ""){
	    	//This is to store the chooseUG value when the form is loaded for the first time
			originalChooseUgValue = chooseUserGroupOptionstrue;
	    }
			if(RouteTemplateDisabled){
				if(templateName){
					templateName[0].disabled = true;
				}	
				if(originalChooseUgValue !== ""){
					// If "revise template" option is choosed, then we should always load the chooseUG value which is present on the template,
					// hence using the value stored on the first load
					chooseUserGroupOptionstrue = originalChooseUgValue;
				}	
				if(chooseFromUG || chooseUserGroupOptionstrue) {
					PreserveTaskOwner[0].checked=true;
					PreserveTaskOwner[0].value="False";
					PreserveTaskOwner[1].disabled=true;
					chooseUserGroupOptions[0].disabled=true;
					chooseUserGroupOptions[1].checked=true;
				}else {
					PreserveTaskOwner[0].disabled=false;
					PreserveTaskOwner[1].disabled=false;
					chooseUserGroupOptions[1].disabled=true;
					chooseUserGroupOptions[0].disabled=false;
					chooseUserGroupOptions[0].checked=true;
					chooseUserGroupOptions[0].value="False";
				}
			}
			else{
				if(templateName){
					templateName[0].disabled = false;
				}
					PreserveTaskOwner[1].disabled=false;
					chooseUserGroupOptions[0].disabled=false;
					chooseUserGroupOptions[1].disabled=false;
					chooseUserGroupOptions[0].checked=true;
					chooseUserGroupOptions[0].value="False";
					chooseUserGroupOptions[1].checked=false;
					chooseUserGroupOptions[1].value="True";
					chooseUserGroupOptions[1].removeAttribute("checked");
					if(field){					
						field.PreviousValue = "False";
					}
			}
}
function saveASTemplateChooseUsersFromUserGroupChange() {
	var chooseUserGroupOptions = document.getElementsByName("ChooseUsersFromUserGroup");	
	var PreserveTaskOwner = document.getElementsByName("PreserveTaskOwner");
	var SaveOptions = document.getElementsByName("SaveOptions");
	var RouteTemplateDisabled = SaveOptions[SaveOptions.length - 1].checked == true;
	PreserveTaskOwner[0].disabled=false;
	PreserveTaskOwner[1].disabled=false;
	var chooseUserGroupOptionstrue = chooseUserGroupOptions[chooseUserGroupOptions.length - 1].checked == true;
	if(chooseUserGroupOptionstrue) {
		PreserveTaskOwner[0].checked=true;
		PreserveTaskOwner[0].value="False";
		PreserveTaskOwner[1].disabled=true;
		PreserveTaskOwner[1].value="True";
	}else {
		chooseUserGroupOptions[0].checked=true;
		chooseUserGroupOptions[0].value="False";
			}
	if(isFirstTime){
		isFirstTime = false;
		chooseFromUG = chooseUserGroupOptionstrue;
			}
}
		
function clearAddressFieldInLocation()
{
    setFieldValue("Address1", "");
    setFieldValue("Address2", "");
}
function issueCreatePreProcess(){
	//XSSOK
	document.forms['emxCreateForm'].elements["Company"].value		= 	"<%=strCompanyName%>";
	//XSSOK
	document.forms['emxCreateForm'].elements["CompanyOID"].value	= 	"<%=strCompanyId%>";
	//XSSOK
	document.forms['emxCreateForm'].elements["CompanyDisplay"].value= 	"<%=strCompanyTitle%>";	
}

function clearCompanyFormFields(){
    setFieldValue("Organization ID", "");
    setFieldValue("Cage Code1", "");
    setFieldValue("Cage Code2", "");
    setFieldValue("DUNS Number", "");
    setFieldValue("Organization Phone Number", "");
    setFieldValue("Organization Fax Number", "");
    setFieldValue("Address", "");
}

function clearOtherFields(){
	console.log("asdasD");
	var objNameOID = document.getElementsByName("NameOID");
	if(typeof objNameOID != 'undefined' && objNameOID != '' && objNameOID.length > 0 && typeof objNameOID[0] != 'undefined' && objNameOID[0].value != ""){
		emxFormReloadField('Organization');
		emxFormReloadField('Project');
	}
}


