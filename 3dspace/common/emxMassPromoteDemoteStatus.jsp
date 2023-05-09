<!-- emxMassPromoteDemoteStatus.jsp
  Copyright (c) 1992-2020 Dassault Systemes.
  All Rights Reserved.
  This program contains proprietary and trade secret information of
  Dassault Systemes.
  Copyright notice is precautionary only and does not evidence any actual
  or intended publication of such program
-->
<%@include file = "emxNavigatorInclude.inc"%>
<%@include file = "emxNavigatorTopErrorInclude.inc"%>
<%
String strLanguage = request.getHeader("Accept-Language");
String Message =(String)emxGetParameter(request,"errorObjectName");
String statusMessage = UINavigatorUtil.getI18nString(
        "emxFramework.MassPromoteDemote.StatusMessage",
        "emxFrameworkStringResource", strLanguage);
%>
<!--//XSSOK -->
<b><%=statusMessage%></b>
