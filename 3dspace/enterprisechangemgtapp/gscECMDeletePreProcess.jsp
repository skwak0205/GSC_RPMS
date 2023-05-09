<%-----------------------------------------------------------------------------
* FILE    : gscECMDeletePreProcess.jsp
* DESC    : Delete Pre Process
* VER.    : 1.0
* AUTHOR  : HyungJin.Ju
* PROJECT : GSCaltex Project
*
* Copyright 2018 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                           Revision history
* -----------------------------------------------------------------
*
* Since          Author         Description
* -----------   ------------   -----------------------------------
* 2023-01-10     HyungJin.Ju   최초 생성
------------------------------------------------------------------------------%>
<%@page import="com.gsc.apps.common.util.gscStringUtil"%>
<%@page import="com.gsc.apps.common.constants.gscCustomConstants"%>
<%@page import="com.gsc.apps.ecm.util.gscECMUtil"%>
<%@ page import="com.matrixone.apps.framework.ui.UIUtil" %>
<%@include file ="../components/emxComponentsDesignTopInclude.inc"%>
<%@include file="../common/emxNavigatorTopErrorInclude.inc"%>
<script language="javascript" src="../common/scripts/emxUICore.js"></script>
<script language="javascript">
    getTopWindow().showMask();
</script>
<%
    String href = "../common/emxTable.jsp?program=emxAEFUtil:getErrorObjectIds&table=AEFGenericDeleteErrorReport&sortColumnName=Type&HelpMarker=emxhelp_deleteerror&header=emxFramework.GenericDelete.ErrorReportHeader&customize=false&Export=true&PrinterFriendly=true&showClipboard=false&multiColumnSort=false&CancelButton=true&CancelLabel=emxFramework.Common.Close&pagination=0&autoFilter=false&TransactionType=update";
    String objectId = (String) emxGetParameter(request, "objectId");
    String strAccessProgram = (String) emxGetParameter(request, "AccessProgram");
    String[] selObjectIds = (String[])emxGetParameterValues(request, "emxTableRowId");
    String strDeleteJPO   = gscStringUtil.NVL(emxGetParameter(request, "deleteJPO"));
    String strTargetFrame   = gscStringUtil.NVL(emxGetParameter(request, "TargetFrame"));
    String strisTreeObject   = gscStringUtil.NVL(emxGetParameter(request, "isTreeObject"));
    String strparentOId   = gscStringUtil.NVL(emxGetParameter(request, "parentOID"));
    String refreshTarget = gscStringUtil.NVL(emxGetParameter(request, "refreshTarget"));//Refresh Target Frame 추가 by MinSung,Kim 2021/01/07

    String uiType = (String)emxGetParameter(request, "uiType");
    StringBuffer sbfObjectDisplay = new StringBuffer();

    String strDeleteConfirmMessage  = EnoviaResourceBundle.getProperty(context, "emxFrameworkStringResource", context.getLocale(), "emxFramework.JavaScript.Confirm.ConfirmDeleteMessage");
    String strNoAccessDeleteMessage  = EnoviaResourceBundle.getProperty(context, "emxEnterpriseChangeMgtStringResource", context.getLocale(), "EnterpriseChangeMgt.Common.AlertMsg.NoAccessDeleteObject");
    String strCommonNoDeleteMessage  = EnoviaResourceBundle.getProperty(context, "emxEnterpriseChangeMgtStringResource", context.getLocale(), "EnterpriseChangeMgt.AlertMsg.NotDelete");
    boolean bleDelete = false;
    StringBuilder sbuErrorXml = new StringBuilder();
    sbuErrorXml.append("<mxRoot>");

    StringList slErrorId = new StringList();
    int intIndex = -1;
    if (selObjectIds != null) {
        int intSize = selObjectIds.length;

        StringList sl = FrameworkUtil.splitString(selObjectIds[0], "|");
        String strDisplayFormat = gscCustomConstants.SELECT_NAME;

        if (sl.size() > 0 ){
            int intTableValues = sl.size();

            // emxTabeRowId의 Object Id 찾는 부분
            for(int i=0; i <intTableValues; i++) {
                String strValue = gscStringUtil.NVL(sl.get(i));
                if (!strValue.equals("") && strValue.indexOf(".") > -1) {
                    try {
                        DomainObject object = DomainObject.newInstance(context, strValue);
                        if (object.exists(context)) {
                            String strType = object.getInfo(context, gscCustomConstants.SELECT_TYPE);
                            intIndex = i;
                            try {
                                strDisplayFormat = EnoviaResourceBundle.getProperty(context, "emxFramework.Delete.DisplayFormat." + strType);
                            } catch(Exception e) {
                                strDisplayFormat = "#{" + gscCustomConstants.SELECT_NAME + "}";
                            } finally {
                                break;
                            }
                        }
                    } catch(Exception ex) {
                        System.out.println(ex.toString());
                    }
                }
            }
        }

        String[] strAryObjectId = new String[intSize];

        for(int i=0; i < intSize; i++){
            StringList slObject = FrameworkUtil.splitString(selObjectIds[i], "|");
            String strObjectId = (String)slObject.get(intIndex);
            strAryObjectId[i] = strObjectId;
        }

        if(strAccessProgram != null && strAccessProgram.contains(":")) {
            HashMap prmMap = new HashMap();
            prmMap.put("strAryObjectId", strAryObjectId);
            prmMap.put("strDisplayFormat", strDisplayFormat);
            prmMap.put("objectId", objectId);

            try {
                String prog[] = strAccessProgram.split("[:]");
                Map mpErrorObjects = (HashMap)JPO.invoke(context, prog[0], null, prog[1], JPO.packArgs(prmMap), HashMap.class);

                bleDelete = true;
                if (mpErrorObjects != null) {
                    StringList slDeleteOids    = (StringList)mpErrorObjects.get("delete_objects");
                    StringList slDeleteMessage = (StringList)mpErrorObjects.get("delete_message");
                    if (slDeleteOids.size() > 0) {
                        for(int i=0; i< slDeleteOids.size();++i) {
                            String strTempNotDeleteMessage = "<error id='" + slDeleteOids.get(i) + "'><![CDATA[" + slDeleteMessage.get(i) + "]]></error>";
                            sbuErrorXml.append(strTempNotDeleteMessage);
                        }

                        bleDelete=false;
                    }

                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            StringList slSelectList = new StringList();
            slSelectList.add(gscCustomConstants.SELECT_ID);
            slSelectList.add(gscCustomConstants.SELECT_TYPE);
            slSelectList.add(gscCustomConstants.SELECT_CURRENT);
            slSelectList.add("current.access[delete]");
            slSelectList.addAll(gscECMUtil.getDisplayFormatSelectList(strDisplayFormat));

            MapList mlDeleteObjectList = DomainObject.getInfo(context, strAryObjectId, slSelectList);

            bleDelete = true;
            StringList slDeleteCurrent = new StringList();

            // 권한 체크하는 부분.
            for(int idx=0; idx < intSize; idx++) {
                Map mpDetailObject = (Map) mlDeleteObjectList.get(idx);
                boolean bleObjectDelete = true;
                String strDeleteAccess  = gscStringUtil.NVL(mpDetailObject.get("current.access[delete]"));
                String strObjectId      = gscStringUtil.NVL(mpDetailObject.get(gscCustomConstants.SELECT_ID));
                String strObjectType    = gscStringUtil.NVL(mpDetailObject.get(gscCustomConstants.SELECT_TYPE));
                String strObjectCurrent = gscStringUtil.NVL(mpDetailObject.get(gscCustomConstants.SELECT_CURRENT));
                String strDeleteState = "";
                try {
                    strDeleteState = EnoviaResourceBundle.getProperty(context, "emxFramework.DeleteState." + strObjectType);

                    if (!"".equals(strDeleteState)) {
                        slDeleteCurrent = new StringList();
                        slDeleteCurrent = FrameworkUtil.split(strDeleteState, ",");
                    }
                } catch(Exception ex) {
                    strDeleteState = "";
                }

                if (strDeleteAccess.equalsIgnoreCase("false")) {
                    bleDelete = false;
                    bleObjectDelete= false;
                } else {
                    if (slDeleteCurrent!= null && slDeleteCurrent.size() > 0) {
                        if (!slDeleteCurrent.contains(strObjectCurrent)) {
                            bleDelete = false;
                            bleObjectDelete = false;
                        }
                    }
                }

                if (!bleObjectDelete) {
                    String strTempNotDeleteMessage = "<error id='" + strObjectId + "'><![CDATA[" + strCommonNoDeleteMessage + "]]></error>";
                    sbuErrorXml.append(strTempNotDeleteMessage);
                }
            }

        }
    }

    if (bleDelete) {
%>
<script language="javascript">
    var bleCheck = false;
    <%
            if (sbfObjectDisplay.length() > 0) {
                String strAlertMessage = strNoAccessDeleteMessage.replace("@1", sbfObjectDisplay.toString());
    %>
    parent.turnOffProgress();
    //alert("<%= strAlertMessage %>");
    <%
            } else {
    %>
    bleCheck = confirm("<%= strDeleteConfirmMessage%>");
    <%
            }
    %>
    if (bleCheck) {
        //[S] Modify 2020-06-04 MinSung,Kim, Delete JPO Parameter 추가
        //[S] Modify 2020-06-18 HanSu,Kim, IsTreeObject Parameter 추가
        <%
            StringBuffer urlBuffer = new StringBuffer("");
            urlBuffer.append("../enterprisechangemgtapp/gscECMDeleteProcess.jsp?objectIndex=").append( intIndex );
            urlBuffer.append("&deleteJPO=").append( strDeleteJPO );
            if(UIUtil.isNotNullAndNotEmpty(strisTreeObject))
                urlBuffer.append("&isTreeObject=").append( strisTreeObject );
            if(UIUtil.isNotNullAndNotEmpty(strparentOId))
                urlBuffer.append("&parentOId=").append( strparentOId );
            if(UIUtil.isNotNullAndNotEmpty(refreshTarget))
                urlBuffer.append("&refreshTarget=").append( refreshTarget );

        %>
        var url="<%=urlBuffer.toString()%>";
        //[E] Modify 2020-06-18 HanSu,Kim, IsTreeObject Parameter 추가
        //[S] Modify 2020-06-04 MinSung,Kim, Delete JPO Parameter 추가

        <%
        //[S] Modify 2020-06-04 BongJun,Park TargetFrame 추가
        if(UIUtil.isNotNullAndNotEmpty(strTargetFrame)){
        %>
        <%=strTargetFrame%>.performDataAction(url, "multi");
        <%
        //[E] Modify 2020-06-04 BongJun,Park TagetFrame 추가
        } else {
        %>
        var objFrame = getTopWindow().findFrame(getTopWindow(), "listHidden");
        objFrame.parent.performDataAction(url, "multi");
        <%
        }
        %>

    } else {
        getTopWindow().hideMask();
    }
</script>
<%
} else {
    sbuErrorXml.append("</mxRoot>");
    String strSearchName = "emx" + System.currentTimeMillis();

    UISearch.saveSearch(context, strSearchName, FrameworkUtil.encodeURL(sbuErrorXml.toString(), "UTF-8"));

    href += "&errorxml="+strSearchName;

%>
<script language="javascript">
    getTopWindow().showModalDialog("<%=href%>", 600, 500, "true");
    getTopWindow().hideMask();
    setTimeout("registerEvent()",150);
</script>
<%
    }
%>
<script type="text/javascript" language="javascript">


    function registerEvent()
    {
        var contentWindow = getTopWindow().modalDialog.contentWindow;

        //if(isIE && isMaxIE10 && contentWindow)
        if(isIE && !isIE11 && contentWindow)
            contentWindow.attachEvent("onunload" , undefined);
        else if(contentWindow)
            contentWindow.addEventListener("unload" , undefined, false);
    }


</script>
