<%--  emxRouteCreateSimpleDialogFS.jsp   -   Create Frameset for Simple Route
   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of
   MatrixOne, Inc.
   Copyright notice is precautionary only and does not evidence any actual
   or intended publication of such program

   static const char RCSID[] = $Id: emxRouteCreateSimpleDialogFS.jsp.rca 1.2.2.6 Wed Oct 22 16:17:53 2008 przemek Experimental przemek $
--%>

<%@include file = "../emxUIFramesetUtil.inc"%>
<%@include file = "emxRouteInclude.inc"%>
<%@include file = "emxComponentsNoCache.inc"%>
<jsp:useBean id="formBean" scope="session" class="com.matrixone.apps.common.util.FormBean"/>
<%

     String keyValue=emxGetParameter(request,"keyValue");
     String scopeId="All";
    if(keyValue == null){
       keyValue = formBean.newFormKey(session);
    }
     String firstTime=emxGetParameter(request,"init1");
     /*For the First time clear the Form Bean*/
     if(firstTime!=null && firstTime.equals("true"))
     {
            formBean.clear();
     }

    formBean.processForm(session,request,"keyValue");
    String routeInstructions="";
    String routeInitiateManner="";
    String allowDelegation="";
    String routeAction="";
    String routeDueDate="";
    String routeMemberString="";
    String routeDueDateMSValue = "";
    String routePreserveTaskOwner = "";
    String statesWithIds = UIUtil.isNullOrEmpty(emxGetParameter(request, "statesWithIds"))?"":(String) emxGetParameter(request, "statesWithIds");
     if(firstTime==null)
    { 
    	routeDueDateMSValue = UIUtil.isNullOrEmpty(emxGetParameter(request,"routeDueDateMSValue")) ? "0" : emxGetParameter(request,"routeDueDateMSValue");
    	routeInstructions = UIUtil.isNullOrEmpty(emxGetParameter(request,"routeInstructions")) ? "" : emxGetParameter(request,"routeInstructions");
    	routeInitiateManner= UIUtil.isNullOrEmpty(emxGetParameter(request,"routeInitiateManner"))?"":emxGetParameter(request,"routeInitiateManner");
    	allowDelegation= UIUtil.isNullOrEmpty(emxGetParameter(request,"allowDelegation"))?"":emxGetParameter(request,"allowDelegation");
    	routeAction= UIUtil.isNullOrEmpty(emxGetParameter(request,"routeAction"))?"":emxGetParameter(request,"routeAction");
    	routeDueDate= UIUtil.isNullOrEmpty(emxGetParameter(request,"routeDueDate"))?"":emxGetParameter(request,"routeDueDate");
    	routeMemberString= UIUtil.isNullOrEmpty(emxGetParameter(request,"routeMemberString"))?"":emxGetParameter(request,"routeMemberString");
    	routePreserveTaskOwner = UIUtil.isNullOrEmpty(emxGetParameter(request,"routePreserveTaskOwner"))?"":emxGetParameter(request,"routePreserveTaskOwner");
    }
     if(UIUtil.isNullOrEmpty(routePreserveTaskOwner)){
    	 routePreserveTaskOwner = "False";
    }
     if(UIUtil.isNotNullAndNotEmpty(routeInstructions)){
    	 routeInstructions= FrameworkUtil.findAndReplace(routeInstructions, "E:N:T:E:R", "\n");
    	}
    String searchDocId=(String) formBean.getElementValue("ContentID");
    String  objectId      =  (String) formBean.getElementValue("objectId");
	if(UIUtil.isNotNullAndNotEmpty(objectId))
	{
    objectId = FrameworkUtil.getOIDfromPID(context, objectId);
	}
    String tableBeanName = "emxRouteCreateSimpleDialogFS";
    framesetObject fs    = new framesetObject();
    String initSource = emxGetParameter(request,"initSource");
    if (initSource == null)
    {
       initSource = "";
    }
    String jsTreeID   =  emxGetParameter(request, "jsTreeID");
    String suiteKey   =  emxGetParameter(request, "suiteKey");
    String portalMode =  emxGetParameter(request, "portalMode");
    String supplierOrgId = emxGetParameter(request,"supplierOrgId");
    fs.setDirectory(appDirectory);

  // ----------------- Do Not Edit Above ------------------------------

    boolean bTeam = FrameworkUtil.isSuiteRegistered(context,
                                                  "featureVersionTeamCentral",
                                                  false,
                                                  null,
                                                  null);
    boolean bProgram = FrameworkUtil.isSuiteRegistered(context,
                                                     "appVersionProgramCentral",
                                                     false,
                                                     null,
                                                     null);

try
{
  //added for the bug 316267
  Person personObj=Person.getPerson(context);
  boolean boolHostCompanyEmployee=false;
  if((Company.getHostCompany(context)).equals(personObj.getCompanyId(context)))
      boolHostCompanyEmployee=true;
  //till here

    if(searchDocId==null || searchDocId.equals("null"))
    {
        searchDocId="";
    }
  //added for the bug 316267
  if(objectId!=null)
  {
  //till here
    DomainObject relatedObject=new DomainObject(objectId);

    String curType=relatedObject.getInfo(context,DomainConstants.SELECT_TYPE);

    if( (!curType.equals(DomainConstants.TYPE_PROJECT)) &&
                  (!curType.equals(DomainObject.TYPE_PROJECT_VAULT)) &&
                             (!curType.equals(DomainObject.TYPE_INBOX_TASK)))
    {
            searchDocId+=objectId+"~";
    }
     if( (curType.equals(DomainObject.TYPE_WORKSPACE)) || (curType.equals(DomainObject.TYPE_PROJECT_SPACE)) || (curType!=null && !"null".equals(curType) && !"".equals(curType) && mxType.isOfParentType(context,curType,DomainConstants.TYPE_PROJECT_SPACE)) || (curType.equals(DomainObject.TYPE_WORKSPACE_VAULT)))//Modified to handle Sub Type
    {
            scopeId=objectId;
    }
  //added for the bug 316267
  else if(!boolHostCompanyEmployee)
  {
    scopeId="All";
  }
  }
  else if(!boolHostCompanyEmployee)
  {
    scopeId="All";
  }

  //Till here added for the bug 316267
}
catch(Exception e)
{
}

  // Specify URL to come in middle of frameset
  StringBuffer contentURL = new StringBuffer(175);
  contentURL.append("emxRouteCreateSimpleDialog.jsp");

  // add these parameters to each content URL, and any others the App needs
  contentURL.append("?suiteKey=");
  contentURL.append(suiteKey);
  contentURL.append("&initSource=");
  contentURL.append(initSource);
  contentURL.append("&jsTreeID=");
  contentURL.append(jsTreeID);
  contentURL.append("&beanName=");
  contentURL.append(tableBeanName);
  contentURL.append("&statesWithIds=");
  contentURL.append(statesWithIds);
  contentURL.append("&searchDocId=");
  contentURL.append(searchDocId);
  contentURL.append("&objectId=");
  contentURL.append(objectId);
  contentURL.append("&keyValue=");
  contentURL.append(keyValue);
  contentURL.append("&scopeId=");
  contentURL.append(scopeId);
  contentURL.append("&supplierOrgId=");
  contentURL.append(supplierOrgId);
  contentURL.append("&routeInstructions=");
  contentURL.append(routeInstructions);
  contentURL.append("&routeInitiateManner=");
  contentURL.append(routeInitiateManner);
  contentURL.append("&allowDelegation=");
  contentURL.append(allowDelegation);
  contentURL.append("&routeAction=");
  contentURL.append(routeAction);
  contentURL.append("&routeDueDate=");
  contentURL.append(routeDueDate);
  contentURL.append("&routeMemberString=");
  contentURL.append(routeMemberString);
  contentURL.append("&routeDueDateMSValue=");
  contentURL.append(routeDueDateMSValue);
  contentURL.append("&routePreserveTaskOwner=");
  contentURL.append(routePreserveTaskOwner);
  fs.setBeanName(tableBeanName);
  fs.setStringResourceFile("emxComponentsStringResource");

  // Page Heading - Internationalized
  String PageHeading = "emxComponents.CreateSimpleRouteDialog.CreateSimple";

  // Marker to pass into Help Pages
  // icon launches new window with help frameset inside

  String HelpMarker = "emxhelpcreatesimpleroute";

  fs.initFrameset(PageHeading,
                  HelpMarker,
                  contentURL.toString(),
                  false,
                  true,
                  false,
                  false);

  fs.createCommonLink("emxComponents.Common.AddContent",
                      "AddContent()",
                      "role_GlobalUser",
                      false,
                      true,
                      "../common/images/iconActionAdd32.png",
                      true,3);


  fs.createCommonLink("emxComponents.Button.RemoveSelected",
                      "removeSelectedContent()",
                      "role_GlobalUser",
                      false,
                      true,
                      "../common/images/iconActionRemove.png",
                      true,3);


   // show Upload only if TeamCentral or ProgramCentral is installed


   fs.createFooterLink("emxComponents.Button.Done",
                       "submitForm()",
                       "role_GlobalUser",
                       false,
                       true,
                       "common/images/buttonDialogDone.gif",
                       3);

   fs.createFooterLink("emxComponents.Button.Cancel",
                       "closeWindow()",
                       "role_GlobalUser",
                       false,
                       true,
                       "common/images/buttonDialogCancel.gif",
                       3);

  // ----------------- Do Not Edit Below ------------------------------

   fs.writePage(out);

%>
