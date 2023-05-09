<%-----------------------------------------------------------------------------
* FILE    : gscAPPIndentedTableFilterProcess.jsp
* DESC    : Search Process JSP
* VER.    : 1.0
* AUTHOR  : GeonHwan,Bae
* PROJECT : HiMSEN Engine Project
*
* Copyright 2018 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                         Revision history
* -----------------------------------------------------------------
* Since          Author         Description
* ------------   ------------   -----------------------------------
* 2020-05-22     GeonHwan,Bae   최초 생성
------------------------------------------------------------------------------%>
<%@page import="java.net.URLEncoder"%>
<%@include file = "./emxNavigatorTopErrorInclude.inc"%>
<%@ include file = "../emxUICommonHeaderBeginInclude.inc"%>
<%@include file = "./emxUIConstantsInclude.inc"%>
<%@page import="com.matrixone.apps.framework.ui.UIUtil" %>
<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@include file = "./emxNavigatorInclude.inc"%>
<%@include file = "../emxContentTypeInclude.inc"%>
<%@page import="com.matrixone.apps.framework.ui.UINavigatorUtil"%>
<%@page import="com.matrixone.apps.domain.util.PersonUtil"%>

<jsp:useBean id="indentedTableBean" class="com.matrixone.apps.framework.ui.UITableIndented" scope="session"/>
<%!
private void constructWherePatternBasic(StringBuffer sbWhere, String sSelect, String sOperator, String sValue,  boolean isLikeSearch){
    if(StringUtils.isNotEmpty(sValue)){
        if(sbWhere == null) sbWhere = new StringBuffer();
        if(sbWhere.length() > 0){
            sbWhere.append( " && " );
        }
        if(isLikeSearch){
        	sbWhere.append(sSelect).append(" ").append(sOperator).append(" '").append(sValue).append("*'");
        }else{
        	sbWhere.append(sSelect).append(" ").append(sOperator).append(" '").append(sValue).append("'");
        }
    }
} 
%>
<%
//String program =emxGetParameter(request,"program");
//String stTimeStamp = emxGetParameter(request, "timeStamp");
Locale locale = context.getLocale();
TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
double clientTZOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();
    try{
        String filterTimeStamp = emxGetParameter(request,"filterTimeStamp");
        HashMap requestMap = UINavigatorUtil.getRequestParameterMap(pageContext);
        //System.out.println("requestMap : " + requestMap);
        Vector userRoleList    = PersonUtil.getAssignments(context);

        //requestMap.put("parameterMap", requestMap);
        indentedTableBean.init(context, pageContext, requestMap, filterTimeStamp, userRoleList);
        
    }catch(Exception e){
        e.printStackTrace();
    }
%>
<script language="JavaScript" src="./scripts/emxUICore.js"></script>
<script language="JavaScript" src="./scripts/emxUIConstants.js"></script>

<script language="Javascript">
var tableFrame = findFrame(parent, "tableFrame");

if(tableFrame.editableTable != null) {
    tableFrame.editableTable.loadData();
    //tableFrame.RefreshTableHeaders();
    try {
        tableFrame.rebuildView();
    } catch(e) {
    }
}
//parent.searchProcessOff();
getTopWindow().hideMask();
</script>
