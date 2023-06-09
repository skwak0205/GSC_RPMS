<%-- EffectivityIntermediate.jsp --

    Copyright (c) 1992-2015 Enovia Dassault Systemes.All Rights Reserved.
    This program contains proprietary and trade secret information of MatrixOne,Inc.
    Copyright notice is precautionary only and does not evidence any actual
    or intended publication of such program

    static const char RCSID[] =$Id: EffectivityIntermediate.jsp.rca 1.6.3.2 Wed Oct 22 15:52:02 2008 przemek Experimental przemek przemek $
--%>
<%-- Common Includes --%>

<%@page import = "com.matrixone.apps.domain.util.i18nNow" %>

<%@include file = "../emxI18NMethods.inc"%>
<%@include file = "../emxUICommonAppInclude.inc"%>
<%@include file = "../common/emxUIConstantsInclude.inc"%>

<%@page import = "matrix.util.StringList"%>
<%@page import = "com.matrixone.apps.domain.util.MapList"%>
<%@page import="com.matrixone.apps.domain.DomainRelationship"%>
<%@page import = "com.matrixone.apps.effectivity.EffectivityFramework"%>
<%@page import="com.matrixone.apps.domain.util.XSSUtil"%>
<%@page import="com.matrixone.apps.framework.ui.UIUtil" %>


<%
String acceptLanguage = request.getHeader("Accept-Language");
//Code to be inserted for the bundle to be read from prop file.
String bundle = "EffectivityStringResource";
%>
<%-- XSSOK --%>

<framework:localize id="i18nId" bundle="EffectivityStringResource" locale='<%=XSSUtil.encodeForJavaScript(context,acceptLanguage)%>'/>

<%
  out.clear();
  StringBuffer sb = new StringBuffer();
  boolean bIsError = false;
  try
  {
	  String effColumnType= emxGetParameter(request,"effColumnType");	//This parameter will be Helpful for identifying the Decoupling Effectivity
      String formName = emxGetParameter(request,"formName");
      String fieldNameActual = emxGetParameter(request,"fieldNameActual");
      String fieldNameDisplay = emxGetParameter(request,"fieldNameDisplay");
      String objectId = emxGetParameter(request,"objectId");
      String relId = emxGetParameter(request,"relId");
      String rootObjectId = emxGetParameter(request,"rootObjectId");
     
      if (rootObjectId == null)
      {
          rootObjectId = "";
      }
      String suiteKey = emxGetParameter(request,"suiteKey");
      if(!rootObjectId.equalsIgnoreCase(objectId))
      {   
          //Context Id
          String effRel = "";
          if(relId != null && !"null".equalsIgnoreCase(relId) && relId.length() > 0){
              StringList relSelects = new StringList(4);  
              relSelects.add(DomainRelationship.SELECT_FROM_ID);
              relSelects.add(DomainRelationship.SELECT_NAME);
              String KIND_OF_GBOM = "type.kindof["+ PropertyUtil.getSchemaProperty("relationship_GBOM") + "]";
              String KIND_OF_CustomGBOM = "type.kindof["+ PropertyUtil.getSchemaProperty("relationship_CustomGBOM") +"]";
              relSelects.add(KIND_OF_GBOM);
              relSelects.add(KIND_OF_CustomGBOM);
              MapList rootIdMapList = DomainRelationship.getInfo(context, new String[]{relId}, relSelects);
              
              if(((String)((Map)rootIdMapList.get(0)).get(KIND_OF_GBOM)).equalsIgnoreCase("TRUE") 
                    || ((String)((Map)rootIdMapList.get(0)).get(KIND_OF_CustomGBOM)).equalsIgnoreCase("TRUE"))
              {
                    objectId = rootObjectId;
              }
              
              //get immediate parentId and set it as object's rootObjectId            
              if(rootIdMapList != null && rootIdMapList.size() > 0){
                  rootObjectId = (String)((Map)rootIdMapList.get(0)).get(DomainRelationship.SELECT_FROM_ID);
                  effRel = (String)((Map)rootIdMapList.get(0)).get(DomainRelationship.SELECT_NAME);
              }
          }       
          
          sb.append("EffectivityDefinitionDialog.jsp?modetype=edit&invockedFrom=fromTable");
          sb.append("&formName="+XSSUtil.encodeForURL(context,formName));
          sb.append("&fieldNameActual="+XSSUtil.encodeForURL(context,fieldNameActual));
          sb.append("&fieldNameDisplay="+XSSUtil.encodeForURL(context,fieldNameDisplay));
          sb.append("&effColumnType="+XSSUtil.encodeForURL(context,effColumnType));
          sb.append("&objectId="+XSSUtil.encodeForURL(context,objectId));
          sb.append("&relId="+relId);
          if(effRel.length() > 0){
              sb.append("&effectivityRelationship="+PropertyUtil.getAliasForAdmin(context, "relationship", effRel, true));
              sb.append("&relName="+effRel);
          }
          if (rootObjectId.equals(""))
          {
              rootObjectId = objectId;
          }
          sb.append("&rootObjectId="+rootObjectId);
          sb.append("&suiteKey="+XSSUtil.encodeForURL(context,suiteKey));
      }
      else
      {
          %>
          <script>
         rootNode =true;
          </script>
          <%
      }
  }
  catch(Exception e)
  {
    bIsError=true;
    session.putValue("error.message", e.getMessage());
  }// End of main Try-catck block
%>
<!--  IR# IR-551582-3DEXPERIENCER2018x fixing for context effectivity Dialog Title and also Line# 123 for title <title> And Line#23 "@page 			  import="com.matrixone.apps.framework.ui.UIUtil" used for title function-->
<%
	String objectId = emxGetParameter(request, "objectId");
	String pageHeader= "";
	if(objectId!=null && !"".equalsIgnoreCase(objectId)) {
		pageHeader=UIUtil.getWindowTitleName(context,null,objectId,pageHeader);
	}
%>			<!-- IR# IR-551582-3DEXPERIENCER2018x -->


<html>
<head>
<title><%=XSSUtil.encodeForHTML(context, pageHeader)%> | <%=i18nStringNowUtil("Effectivity.Form.Label.Effectivity", bundle,acceptLanguage)%></title>
<link rel="stylesheet" type="text/css" media="screen" href="../common/styles/emxUIStructureBrowser.css" />
<script language="JavaScript" type="text/javascript" src="../common/emxUIConstantsJavaScriptInclude.jsp"></script>
<script language="javascript" src="../common/scripts/emxUIConstants.js"></script>
<script language="javascript" src="../common/scripts/jquery-latest.js"></script>
<script language="javascript" src="../common/scripts/emxUICore.js"></script>
 <script>

var rootNode;
 function generateURL()
 {
     if(rootNode)
     {
         getTopWindow().window.alert("<%=i18nStringNowUtil("Effectivity.Root.StructureEffectivity", bundle,acceptLanguage)%>");
         getTopWindow().window.closeWindow();
     }
     else
     {   
          var  postProcessCFFURL="";
          var  showContext= "";
          var  includeContextProgram= "";
          var  showOr = "";
          var  showAnd =""; 
          var  expandProgram = "";
          var  effectivityRelationship = "";
          var  relationship = "";
          var opener = parent.window.getWindowOpener().oXML;
          postProcessCFFURL= parent.window.getWindowOpener().getRequestSetting("postProcessCFFURL");
          showContext= parent.window.getWindowOpener().getRequestSetting("showContext");
          //includeContextProgram= parent.window.getWindowOpener().getRequestSetting("includeContextProgram");
          var tempIncCtxProg = emxUICore.selectSingleNode(opener,"/mxRoot/requestMap/setting[@name = 'includeContextProgram']") // Fix for IR-532195-3DEXPERIENCER2019x, issue while parsing the URL Parameters
          if(tempIncCtxProg != "undefined" && tempIncCtxProg != null && tempIncCtxProg != "")
          {
        	  includeContextProgram = emxUICore.getText(tempIncCtxProg);
          }   
          showOr= parent.window.getWindowOpener().getRequestSetting("showOr");
          showAnd= parent.window.getWindowOpener().getRequestSetting("showAnd");
          expandProgram= parent.window.getWindowOpener().getRequestSetting("expandProgram");
          //effectivityRelationship= parent.window.getWindowOpener().getRequestSetting("effectivityRelationship");
          var tempEffRel = emxUICore.selectSingleNode(opener,"/mxRoot/requestMap/setting[@name = 'effectivityRelationship']")// Fix for IR-531269-3DEXPERIENCER2019x, issue while parsing the URL Parameters
          if(tempEffRel != "undefined" && tempEffRel != null && tempEffRel != "")
          {
        	  effectivityRelationship = emxUICore.getText(tempEffRel);
          }        
          relationship= parent.window.getWindowOpener().getRequestSetting("relationship");
          var indentedTableParams = "";
          if(postProcessCFFURL != "undefined" && postProcessCFFURL != null && postProcessCFFURL != "")
          {
              indentedTableParams += "&postProcessCFFURL="+postProcessCFFURL;
          }
          if(showContext != "undefined" && showContext != null && showContext != "")
          {
              indentedTableParams += "&showContext="+showContext;
          }
          if(showOr != "undefined" && showOr != null && showOr != "")
          {
              indentedTableParams += "&showOr="+showOr;
          }
          if(showAnd != "undefined" && showAnd != null && showAnd != "")
          {
              indentedTableParams += "&showAnd="+showAnd;
          }
          if(expandProgram != "undefined" && expandProgram != null && expandProgram != "")
          {
              indentedTableParams += "&expandProgram="+expandProgram;
          }
          if(effectivityRelationship != "undefined" && effectivityRelationship != null && effectivityRelationship != "")
          {
              indentedTableParams += "&effectivityRelationship="+effectivityRelationship;
          }
          if(relationship != "undefined" && relationship != null && relationship != "")
          {
              indentedTableParams += "&relationship="+relationship;
          }
          if(includeContextProgram != "undefined" && includeContextProgram != null && includeContextProgram != "")
          {
              indentedTableParams += "&includeContextProgram="+includeContextProgram;
          }
         
          var url = '<%=XSSUtil.encodeURLwithParsing(context,sb.toString())%>';
          url += indentedTableParams;
          var frame = document.getElementById("EffectivityDD");
          //alert("url: " + url);
          frame.setAttribute("src",url);
     }
 }
          
</script>
</head>
<body onLoad = "generateURL();">
<iframe id="EffectivityDD"  style="overflow:hidden;width:100%;height:100%;border:0"></iframe>
</body>
</html>  
