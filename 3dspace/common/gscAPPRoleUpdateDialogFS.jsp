<%-----------------------------------------------------------------------------
* FILE    : gscAPPRoleUpdateDialogFS.jsp
* DESC    : User Role Update Dialog FS Page
* VER.    : 1.0
* AUTHOR  : SeungYong,Lee
* PROJECT : HiMSEM Engine Project
*
* Copyright 2018 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                           Revision history
* -----------------------------------------------------------------
*
* Since          Author         Description
* -----------   ------------   -----------------------------------
* 2020-09-04     SeungYong,Lee   최초 생성
------------------------------------------------------------------------------%>
<%@include file="../emxUIFramesetUtil.inc"%>
<%@include file="emxCompCommonUtilAppInclude.inc"%>
<%
    framesetObject fs = new framesetObject();

    fs.setDirectory(appDirectory);
    fs.setSubmitMethod(request.getMethod());
    String initSource = emxGetParameter(request,"initSource");
    if (initSource == null){
        initSource = "";
    }

    String jsTreeID = emxGetParameter(request,"jsTreeID");
    String suiteKey = emxGetParameter(request,"suiteKey");
    String objectId = emxGetParameter(request,"emxTableRowId");

    // ----------------- Do Not Edit Above ------------------------------

    // To get the selected collection name from Delete Check page
    String strCollections = "";
    strCollections = emxGetParameter(request,"strCollections");

    // Specify URL to come in middle of frameset
    StringBuffer contentURL = new StringBuffer("gscAPPRoleUpdateDialog.jsp");

    // add these parameters to each content URL, and any others the App needs
    contentURL.append("?suiteKey=");
    contentURL.append(suiteKey);
    contentURL.append("&initSource=");
    contentURL.append(initSource);
    contentURL.append("&jsTreeID=");
    contentURL.append(jsTreeID);
    contentURL.append("&objectId=");
    contentURL.append(objectId);
    contentURL.append("&selectedCollections=");
    contentURL.append(strCollections);
    
    String finalURL=contentURL.toString();

    // Page Heading - Internationalized
    String PageHeading = "emxComponents.Common.Update";
    String HelpMarker = "emxhelpcollectionsdelete";

    //(String pageHeading,String helpMarker, String middleFrameURL, boolean UsePrinterFriendly, boolean IsDialogPage, boolean ShowPagination, boolean ShowConversion)
    fs.initFrameset(PageHeading,
                   HelpMarker,
                   finalURL,
                   false,
                   true,
                   false,
                   false);

    fs.removeDialogWarning();
    fs.setStringResourceFile("emxComponentsStringResource");

    // Set page title - internationalized
    String pageTitle = i18nNow.getI18nString("emxComponents.Common.Command.RoleUpdate", "emxComponentsStringResource",
                                       request.getHeader("Accept-Language"));
    fs.setPageTitle(pageTitle);

    String roleList = "role_GlobalUser";

    fs.createFooterLink("emxComponents.Common.Done",
                      "window.frames['iDataFrame'].sendToUpdatePage()",
                      roleList,
                      false,
                      true,
                      "common/images/buttonDialogDone.gif",
                      0);


    fs.createFooterLink("emxCommonButton.Cancel",
                      "parent.window.closeWindow()",
                      roleList,
                      false,
                      true,
                      "common/images/buttonDialogCancel.gif",
                      0);


    // ----------------- Do Not Edit Below ------------------------------

    fs.writePage(out);

%>
