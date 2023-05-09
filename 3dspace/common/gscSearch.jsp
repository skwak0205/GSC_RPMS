<%-----------------------------------------------------------------------------
* FILE    : gscSearch.jsp
* DESC    : Search Util
* VER.    : 1.0
* AUTHOR  : HyungJin,Ju
* PROJECT : GSCaltex Project
*
* Copyright 2020 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                         Revision history
* -----------------------------------------------------------------
* Since          Author         Description
* ------------   ------------   -----------------------------------
* 2023-01-10     HyungJin,Ju   최초 생성
------------------------------------------------------------------------------%>
<%--  AEFSearchUtil.jsp

   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of MatrixOne,Inc.
   Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

   static const char RCSID[] = $Id: AEFSearchUtil.jsp.rca 1.1 Wed Jan 14 05:57:06 2009 ds-smourougayan Experimental przemek $
--%>

<%@page import="com.gsc.apps.common.constants.gscCustomConstants"%>
<%@page import="com.gsc.apps.common.util.gscOrgUtil"%>
<%@page import="com.gsc.apps.common.util.gscStringUtil" %>
<%@page import="com.matrixone.apps.framework.ui.UIUtil"%>
<%@page import="matrix.util.StringList"%>

<%@include file = "emxNavigatorInclude.inc"%>
<%
response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
response.setHeader("Pragma", "no-cache"); //HTTP 1.0
String fieldNameActual = emxGetParameter(request, "fieldNameActual");
String uiType = emxGetParameter(request, "uiType");
String typeAhead = emxGetParameter(request, "typeAhead");
String frameName = emxGetParameter(request, "frameName");
String sFromMode = emxGetParameter(request, "fromMode");
String frameNameForField = emxGetParameter(request, "frameNameForField");
String fieldNameDisplay = emxGetParameter(request, "fieldNameDisplay");
String sMode = emxGetParameter(request, "Mode");

String emxTableRowId[] = emxGetParameterValues(request, "emxTableRowId");
String create = gscStringUtil.NVL(request.getParameter("create")); // 공사 OOL 생성 체크 by JeongHun,Ha 2020/07/09
String sFieldHiddenName = "";
String additionalFieldName = "";
String secondAdditionalFieldName = "";
String sEngineType = "";
String sFunctionalDept = "";
String sFunctionalDeptDisplayName = "";
String sModelName = "";
String sFieldCustomName = "";
StringBuffer actualValue = new StringBuffer();
StringBuffer displayValue = new StringBuffer();
StringBuffer OIDValue = new StringBuffer();
for(int i=0;i<emxTableRowId.length;i++) {
    //[S] ObjectId 가져오는 방식 변경 by MinSung,Kim 2020/08/18
    //StringTokenizer strTokenizer = new StringTokenizer(emxTableRowId[i] , "|");
    //String strObjectId = strTokenizer.nextToken() ;
    String strObjectId = "";
    StringList list = FrameworkUtil.splitString(emxTableRowId[i], "|");
    if(list.size() > 1 && gscStringUtil.isNotEmpty(list.get(1))) {
        strObjectId = list.get(1);
    }
    //[E] ObjectId 가져오는 방식 변경 by MinSung,Kim 2020/08/18

    DomainObject objContext = new DomainObject(strObjectId);
    StringList strList = new StringList();
    strList.addElement(DomainConstants.SELECT_NAME);
    strList.addElement(DomainConstants.SELECT_TYPE);
    strList.addElement("to[gscEngineGroupToModel].from.name");
    strList.addElement("attribute[Display Name]");
    strList.addElement("attribute[gscFunctionalDept]");
    // [S] 공사에서 OOL 생성시 S&O 검색 적용 by MinSung,Kim 2020/08/18
    if("ool".equals(create)) {
        strList.addElement(DomainConstants.SELECT_REVISION);
    }
    // [E] 공사에서 OOL 생성시 S&O 검색 적용 by MinSung,Kim 2020/08/18

    Map resultList = objContext.getInfo(context, strList);
    String strContextObjectName = (String)resultList.get("name");
    String strContextObjectType = (String)resultList.get("type");
    String strTypeSymbolicName  = UICache.getSymbolicName(context, strContextObjectType, "type");
    OIDValue.append(strObjectId);
    actualValue.append(strContextObjectName);
    //System.out.println("sFromMode>>>>>>>>>> "+sFromMode);
    //System.out.println("resultList>>>>>>>>>> "+resultList);
    if(!UIUtil.isNullOrEmpty(strTypeSymbolicName) && "type_Person".equals(strTypeSymbolicName)) {
        displayValue.append(gscOrgUtil.getUserDisplayName(context, strContextObjectName));
    // [S] 공사에서 OOL 생성시 S&O 검색 적용 by JeongHun,Ha 2020/10/14
    } else if(gscCustomConstants.TYPE_PRODUCTS.equals(strContextObjectType) && "ool".equals(create)) {
        String strRev = gscStringUtil.NVL(resultList.get(DomainConstants.SELECT_REVISION));
        displayValue.append(strContextObjectName + " " + strRev);
    // [E] 공사에서 OOL 생성시 S&O 검색 적용 by JeongHun,Ha 2020/07/09
   
    // [S] POR 발생을 위한  Engine Model 검색 2020/10/14    
    }else if(!UIUtil.isNullOrEmpty(fieldNameActual) && "modelName".equals(fieldNameActual)){
    	sModelName = gscStringUtil.NVL(resultList.get("to[gscEngineGroupToModel].from.name"));
    	actualValue = new StringBuffer();
    	actualValue.append(sModelName);
    	displayValue.append(strContextObjectName);
    	if(!UIUtil.isNullOrEmpty(sFromMode) && "CreatePOR".equals(sFromMode)){
    		sEngineType = gscStringUtil.NVL(resultList.get("attribute[Display Name]"));
    		sFieldHiddenName = emxGetParameter(request, "fieldHiddenName");
    		additionalFieldName = emxGetParameter(request, "additionalFieldName");
    		sFieldCustomName = emxGetParameter(request, "fieldCustomName");
    	} else if(!UIUtil.isNullOrEmpty(sFromMode) && "SearchPOR".equals(sFromMode)){
    		sFieldHiddenName = emxGetParameter(request, "fieldHiddenName");
    		//System.out.println("sFieldHiddenName>>>>>>>>>> "+sFieldHiddenName);
        	sEngineType = gscStringUtil.NVL(resultList.get("attribute[Display Name]"));
    	}
    }else if(!UIUtil.isNullOrEmpty(fieldNameActual) && "pNewEngineType".equals(fieldNameActual)){
    	sEngineType = gscStringUtil.NVL(resultList.get("attribute[Display Name]"));
    	
		sModelName = gscStringUtil.NVL(resultList.get("to[gscEngineGroupToModel].from.name"));
		sFieldHiddenName = emxGetParameter(request, "fieldHiddenName");
		displayValue.append(strContextObjectName);
    // [S] POR 발생을 위한  PFG 검색 2020/10/15    	
    
    }else if(!UIUtil.isNullOrEmpty(fieldNameActual) && "pMSNo".equals(fieldNameActual)){
    	sFieldHiddenName = emxGetParameter(request, "fieldHiddenName");
    	displayValue.append(strContextObjectName);
    	if(!UIUtil.isNullOrEmpty(sFromMode) && "CreatePOR".equals(sFromMode)){
    		sFunctionalDept = gscStringUtil.NVL(resultList.get("attribute[gscFunctionalDept]"));
    		String strMql = "print bus $1 $2 $3 select $4 dump";
            sFunctionalDeptDisplayName = MqlUtil.mqlCommand(context, strMql, "gscCodeMaster",sFunctionalDept,"gscFunctionalDept", "attribute[Title]");
    		additionalFieldName = emxGetParameter(request, "additionalFieldName");
    		secondAdditionalFieldName = emxGetParameter(request, "secondAdditionalFieldName");
    	}else if(!UIUtil.isNullOrEmpty(sFromMode) && "CopyPOR".equals(sFromMode)){
    		//additionalFieldName = emxGetParameter(request, "additionalFieldName");
    		//if(additionalFieldName!=null && "pStandardMSNo".equals(additionalFieldName)){
    			
    		//}else{
	    		additionalFieldName = emxGetParameter(request, "additionalFieldName");
	    		sEngineType = gscStringUtil.NVL(resultList.get("attribute[Display Name]"));
	    		sModelName = gscStringUtil.NVL(resultList.get("to[gscEngineGroupToModel].from.name"));
    		//}
    	}
    } else {
        displayValue.append(strContextObjectName);
    }
    
    //System.out.println("sEngineType>>>>>>>>>> "+sEngineType);
    if(i!=emxTableRowId.length-1){
        actualValue.append("|");
        displayValue.append("|");
        OIDValue.append("|");
    }                           
}                       
%>

<script src="scripts/emxUICore.js" type="text/javascript"></script>
<script language="javascript" type="text/javaScript">

var typeAhead = "<%=XSSUtil.encodeForJavaScript(context, typeAhead)%>";
var targetWindow = null;
var uiType = "<%=XSSUtil.encodeForJavaScript(context, uiType)%>";
if(typeAhead == "true") {
    var frameName = "<%=XSSUtil.encodeForJavaScript(context, frameName)%>";
    if(frameName == null || frameName == "null" || frameName == "") {
        targetWindow = window.parent;
    } else {
        targetWindow = getTopWindow().findFrame(window.parent, frameName);
    }
    
    if(!targetWindow){
    	targetWindow = window.parent;
    }
} else {

	var frameName = "<%=XSSUtil.encodeForJavaScript(context, frameNameForField)%>";
	 if(frameName == null || frameName == "null" || frameName == "") {
		targetWindow = getTopWindow().getWindowOpener();
	 } else {
	      targetWindow = getTopWindow().findFrame(getTopWindow().getWindowOpener().getTopWindow(), frameName);
	 }
	 if(!targetWindow){
		 targetWindow = getTopWindow().getWindowOpener();
	 }
}

var tmpFieldNameActual = "<%=XSSUtil.encodeForJavaScript(context, fieldNameActual)%>";
var tmpFieldNameDisplay = "<%=XSSUtil.encodeForJavaScript(context, fieldNameDisplay)%>";
var tmpFieldNameOID = tmpFieldNameActual + "OID";

var tmpFieldHiddenName = "<%=XSSUtil.encodeForJavaScript(context, sFieldHiddenName)%>";
var tmpAdditionalFieldName = "<%=XSSUtil.encodeForJavaScript(context, additionalFieldName)%>";
var tmpSecondAdditionalFieldName = "<%=XSSUtil.encodeForJavaScript(context, secondAdditionalFieldName)%>";
var tmpFieldCustomName = "<%=XSSUtil.encodeForJavaScript(context, sFieldCustomName)%>";

var vfieldNameActual = targetWindow.document.getElementById(tmpFieldNameActual) ? targetWindow.document.getElementById(tmpFieldNameActual) : targetWindow.parent.document.getElementById(tmpFieldNameActual);
var vfieldNameDisplay = targetWindow.document.getElementById(tmpFieldNameDisplay) ? targetWindow.document.getElementById(tmpFieldNameDisplay) : targetWindow.parent.document.getElementById(tmpFieldNameDisplay);
var vfieldNameOID = targetWindow.document.getElementById(tmpFieldNameOID) ? targetWindow.document.getElementById(tmpFieldNameOID) : targetWindow.parent.document.getElementById(tmpFieldNameOID);
var vfieldHiddenName = targetWindow.document.getElementById(tmpFieldHiddenName) ? targetWindow.document.getElementById(tmpFieldHiddenName) : targetWindow.parent.document.getElementById(tmpFieldHiddenName);
var vadditionalFieldName = targetWindow.document.getElementById(tmpAdditionalFieldName) ? targetWindow.document.getElementById(tmpAdditionalFieldName) : targetWindow.parent.document.getElementById(tmpAdditionalFieldName);
var vsecondAdditionalFieldName = targetWindow.document.getElementById(tmpSecondAdditionalFieldName) ? targetWindow.document.getElementById(tmpSecondAdditionalFieldName) : targetWindow.parent.document.getElementById(tmpSecondAdditionalFieldName);
var vFieldCustomName = targetWindow.document.getElementById(tmpFieldCustomName) ? targetWindow.document.getElementById(tmpFieldCustomName) : targetWindow.parent.document.getElementById(tmpFieldCustomName);

if (vfieldNameActual==null && vfieldNameDisplay==null) {
	vfieldNameActual=targetWindow.document.forms[0][tmpFieldNameActual];
	vfieldNameDisplay=targetWindow.document.forms[0][tmpFieldNameDisplay];
	vfieldNameOID=targetWindow.document.forms[0][tmpFieldNameOID];
	vfieldHiddenName=targetWindow.document.forms[0][tmpFieldHiddenName];
	vadditionalFieldName=targetWindow.document.forms[0][tmpAdditionalFieldName];
	vsecondAdditionalFieldName=targetWindow.document.forms[0][tmpSecondAdditionalFieldName];
	vFieldCustomName=targetWindow.document.forms[0][tmpFieldCustomName];
}

if (vfieldNameActual==null && vfieldNameDisplay==null) {
	if(targetWindow.parent.document.forms[0]){
		vfieldNameActual=targetWindow.parent.document.forms[0][tmpFieldNameActual] ? targetWindow.parent.document.forms[0][tmpFieldNameActual] : targetWindow.document.forms[0][tmpFieldNameActual];
		vfieldNameDisplay=targetWindow.parent.document.forms[0][tmpFieldNameDisplay] ? targetWindow.parent.document.forms[0][tmpFieldNameDisplay] : targetWindow.document.forms[0][tmpFieldNameDisplay];
		vfieldNameOID=targetWindow.parent.document.forms[0][tmpFieldNameOID] ? targetWindow.parent.document.forms[0][tmpFieldNameOID] : targetWindow.document.forms[0][tmpFieldNameOID];
		vfieldHiddenName=targetWindow.parent.document.forms[0][tmpFieldHiddenName] ? targetWindow.parent.document.forms[0][tmpFieldHiddenName] : targetWindow.document.forms[0][tmpFieldHiddenName];
		vadditionalFieldName=targetWindow.parent.document.forms[0][tmpAdditionalFieldName] ? targetWindow.parent.document.forms[0][tmpAdditionalFieldName] : targetWindow.document.forms[0][tmpAdditionalFieldName];
		vsecondAdditionalFieldName=targetWindow.parent.document.forms[0][tmpSecondAdditionalFieldName] ? targetWindow.parent.document.forms[0][tmpSecondAdditionalFieldName] : targetWindow.document.forms[0][tmpSecondAdditionalFieldName];
		vFieldCustomName=targetWindow.parent.document.forms[0][tmpFieldCustomName] ? targetWindow.parent.document.forms[0][tmpFieldCustomName] : targetWindow.document.forms[0][tmpFieldCustomName];
	}
}

if (vfieldNameActual==null && vfieldNameDisplay==null) {
	vfieldNameActual=targetWindow.document.getElementsByName(tmpFieldNameActual)[0] ? targetWindow.document.getElementsByName(tmpFieldNameActual)[0] : targetWindow.parent.document.getElementsByName(tmpFieldNameActual)[0];
	vfieldNameDisplay=targetWindow.document.getElementsByName(tmpFieldNameDisplay)[0] ? targetWindow.document.getElementsByName(tmpFieldNameDisplay)[0] : targetWindow.parent.document.getElementsByName(tmpFieldNameDisplay)[0];
	vfieldNameOID=targetWindow.document.getElementsByName(tmpFieldNameOID)[0] ? targetWindow.document.getElementsByName(tmpFieldNameOID)[0] : targetWindow.parent.document.getElementsByName(tmpFieldNameOID)[0];
	vfieldHiddenName=targetWindow.document.getElementsByName(tmpFieldHiddenName)[0] ? targetWindow.document.getElementsByName(tmpFieldHiddenName)[0] : targetWindow.parent.document.getElementsByName(tmpFieldHiddenName)[0];
	vadditionalFieldName=targetWindow.document.getElementsByName(tmpAdditionalFieldName)[0] ? targetWindow.document.getElementsByName(tmpAdditionalFieldName)[0] : targetWindow.parent.document.getElementsByName(tmpAdditionalFieldName)[0];
	vsecondAdditionalFieldName=targetWindow.document.getElementsByName(tmpSecondAdditionalFieldName)[0] ? targetWindow.document.getElementsByName(tmpSecondAdditionalFieldName)[0] : targetWindow.parent.document.getElementsByName(tmpSecondAdditionalFieldName)[0];
	vFieldCustomName=targetWindow.document.getElementsByName(tmpFieldCustomName)[0] ? targetWindow.document.getElementsByName(tmpFieldCustomName)[0] : targetWindow.parent.document.getElementsByName(tmpFieldCustomName)[0];
}

/*
   FIX IR-088125V6R2012 
   In IE8, for some use-cases, getElementsByName doesn't work when 
   accessing URL with its full name. Below code address the issue.
 */
if (vfieldNameActual==null && vfieldNameDisplay==null) {
     var elem = targetWindow.document.getElementsByTagName("input");
     var att;
     var iarr;
     for(i = 0,iarr = 0; i < elem.length; i++) {
        att = elem[i].getAttribute("name");
        if(tmpFieldNameDisplay == att) {
            vfieldNameDisplay = elem[i];
            iarr++;
        }
        if(tmpFieldNameActual == att) {
            vfieldNameActual = elem[i];
            iarr++;
        }
        if(iarr == 2) {
            break;
        }
    }
}
/* FIX - END */

vfieldNameDisplay.value ="<%=displayValue%>" ;
vfieldNameActual.value ="<%=actualValue%>" ;
//[S] POR 발생을 위한  PFG 검색 2020/10/15    
var vFromType = "<%=sFromMode%>";
if(tmpFieldNameActual!=null && tmpFieldNameActual == 'pMSNo'){
	var vModelCode = vfieldHiddenName.value;
	if(vFromType!=null && vFromType == 'CopyPOR'){
		var additionalValue = vadditionalFieldName.value;
		vfieldNameActual.value = "<%=sModelName%>"+additionalValue;
	}else{
		vfieldNameActual.value =vModelCode+"<%=actualValue%>" ;
	}
}
var vEngineType = "<%=sEngineType%>";
if(vEngineType !=null && vEngineType != ''){
	if(tmpFieldHiddenName!=null && tmpFieldHiddenName=='modelNewName'){
		vfieldNameActual.value = vEngineType;
		vfieldHiddenName.value = "<%=sModelName%>";
	}else if(vFromType!=null && vFromType == 'SearchPOR'){
		vfieldHiddenName.value = vEngineType;
	}else{
		var vMSNo = vadditionalFieldName.value;
		vfieldHiddenName.value = "<%=sEngineType%>";
		if(vFromType!=null && vFromType == 'CreatePOR'){
			vFieldCustomName.value = "<%=sModelName%>"+vMSNo;
		}
	}
}

var vFunctionalDept = "<%=sFunctionalDept%>";
var vFunctionalDeptDisplay = "<%=sFunctionalDeptDisplayName%>";
if(vFunctionalDept !=null && vFunctionalDept != ''){
	vsecondAdditionalFieldName.value = vFunctionalDeptDisplay;
	vadditionalFieldName.value = vFunctionalDept;
}


//[E] POR 발생을 위한  PFG 검색 2020/10/15    

if(vfieldNameOID != null) {
vfieldNameOID.value ="<xss:encodeForJavaScript><%=OIDValue%></xss:encodeForJavaScript>" ;
} else if(uiType === "createForm" || uiType === "form") {
vfieldNameActual.value ="<xss:encodeForJavaScript><%=OIDValue%></xss:encodeForJavaScript>" ;
}

/*Below function should be invoked to update the filter values */
if(targetWindow.updateFilters) {
targetWindow.updateFilters("<xss:encodeForJavaScript><%=fieldNameDisplay%></xss:encodeForJavaScript>","<xss:encodeForJavaScript><%=displayValue%></xss:encodeForJavaScript>",true);
}

<%
    if ("unitRefresh".equals(sMode)) {
%>
targetWindow.search();
<%
    }
%>

if(typeAhead != "true") {
getTopWindow().closeWindow();
}
</script>
