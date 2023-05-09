<%-- emxRouteTemplateTaskAssignSelectFS.jsp --
  Copyright (c) 1992-2020 Dassault Systemes.
  All Rights Reserved.
  This program contains proprietary and trade secret information of MatrixOne, Inc.
  Copyright notice is precautionary only and does not evidence any actual or intended publication of such program

  static const char RCSID[] = $Id: emxRouteTemplateTaskAssignSelectFS.jsp.rca 1.7 Wed Oct 22 16:17:46 2008 przemek Experimental przemek $
--%>

<%@ include file="../emxUIFramesetUtil.inc"%>
<%@include file  = "emxRouteInclude.inc"%>
<jsp:useBean id="emxRouteTemplateTaskAssignSelectedFS" class="com.matrixone.apps.framework.ui.UITable" scope="session" />

<%

  framesetObject fs = new framesetObject();
  String Directory = appDirectory;
  fs.setDirectory(Directory);

  String tableBeanName = "emxRouteTemplateTaskAssignSelectedFS";
  String initSource = emxGetParameter(request,"initSource");
  if (initSource == null){
    initSource = "";
  }
  //String jsTreeID = emxGetParameter(request,"jsTreeID");
  //String suiteKey = emxGetParameter(request,"suiteKey");
  String routeId = emxGetParameter(request,"routeId");
  String routeNodeId = emxGetParameter(request,"routeNodeId");

  String slctdd   = emxGetParameter(request,"slctdd");
  String slctmm   = emxGetParameter(request,"slctmm");
  String slctyy   = emxGetParameter(request,"slctyy");

  // Specify URL to come in middle of frameset
  String contentURL = "emxRouteTemplateTaskAssignSelectDialog.jsp";

  // add these parameters to each content URL, and any others the App needs
  contentURL += "?initSource=" + initSource + "&routeId=" + routeId+"&routeNodeId=" + routeNodeId+"&beanName=" + tableBeanName+"&slctdd="+slctdd+"&slctmm="+slctmm+"&slctyy="+slctyy;
  String PageHeading = "emxComponents.Common.AssignTasks";
  String HelpMarker  = ""; // need to give help marker

  fs.setBeanName(tableBeanName);

  //(String pageHeading,String helpMarker, String middleFrameURL, boolean UsePrinterFriendly, boolean IsDialogPage, boolean ShowPagination, boolean ShowConversion)
  fs.initFrameset(PageHeading,HelpMarker,contentURL,false,true,true,false);
  fs.setStringResourceFile("emxComponentsStringResource");

  //(String displayString,String href,String roleList, boolean popup, boolean isJavascript,String iconImage, int WindowSize (1 small - 5 large))
  fs.createCommonLink("emxComponents.Button.Done",
                        "submitform()",
                        "role_GlobalUser",
                        false,
                        true,
                        "common/images/buttonDialogDone.gif",
                        false,
                        5);

    fs.createCommonLink("emxComponents.Button.Cancel",
                        "closeWindow()",
                        "role_GlobalUser",
                        false,
                        true,
                        "common/images/buttonDialogCancel.gif",
                        false,
                        5);
  fs.removeDialogWarning();
  fs.writePage(out);

%>

