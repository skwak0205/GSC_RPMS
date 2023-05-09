<%@ page import="matrix.db.Context" %>
<%@ page import="com.dassault_systemes.vplmposadministrationui.uiutil.PreferencesUtil" %>
<%@include file = "../common/emxNavigatorInclude.inc"%>
<%@include file = "../emxTagLibInclude.inc"%>
<%@include file = "../common/emxPLMOnlineAdminAttributesCalculation.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
	<!-- ALU4 2020:03:11 TSK5602766 change redirections -->

		<%
        PreferencesUtil prut = new PreferencesUtil();
        if(prut.getUserPreferredUISolution(mainContext).equals("TEAM")) {%>
           <jsp:forward page="emxPortal.jsp">
                  <jsp:param name="portal" value="APPXPProjectAdminPortal"></jsp:param>
               <jsp:param name="toolbar" value="APPXPAdminToolBar"></jsp:param>
               <jsp:param name="header" value="emxPlmOnline.label.ManageProject"></jsp:param>
           </jsp:forward>
        <%}else{%>
           <jsp:forward page="emxPortal.jsp">
               <jsp:param name="portal" value="APPVPLMPersonAdminPortal"></jsp:param>
               <jsp:param name="toolbar" value="APPXPAdminToolBar"></jsp:param>
               <jsp:param name="header" value="emxPlmOnline.label.ManagePerson"></jsp:param>
           </jsp:forward>
        <%}%>
   </body>
</html>
