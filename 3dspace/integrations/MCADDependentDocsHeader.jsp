﻿<%--  MCADDependentDocsHeader.jsp

   Copyright (c) 2016-2020 Dassault Systemes. All rights reserved.
   This program contains proprietary and trade secret information of
   Dassault Systemes and its subsidiaries. Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

--%>
<%@page import = "matrix.db.*, matrix.util.*, com.matrixone.MCADIntegration.utils.*, com.matrixone.MCADIntegration.server.beans.*" %>
<%@ include file ="MCADTopInclude.inc" %>
<script language="javascript" type="text/javascript" src="./scripts/IEFHelpInclude.js"></script>

<%
	MCADIntegrationSessionData integSessionData = (MCADIntegrationSessionData) session.getAttribute("MCADIntegrationSessionDataObject");
%>

<html>
<head>
	<link rel="stylesheet" href="../common/styles/emxUIDefault.css" type="text/css">
	<link rel="stylesheet" href="../common/styles/emxUIList.css" type="text/css">
	<link rel="stylesheet" href="../common/styles/emxUIDialog.css" type="text/css">
</head>

<body>
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr><td><img src="images/utilSpace.gif" width="1" height="10"></td></tr>
		<tr><td class="pageBorder"><img src="images/utilSpace.gif" width="1" height="1"></td></tr>
	</table>

	<table border="0" width="100%" cellpadding="2">
		<tr>
		    <!--XSSOK-->
			<td class="pageHeader"><%=integSessionData.getStringResource("mcadIntegration.Server.Title.DependentDocuments")%></td>
			<td align="right" >
				<a href='javascript:openIEFHelp("emxhelpdscderivedoutput")'><img src="./images/buttonContextHelp.gif" width="16" height="16" border="0" ></a>
			</td>
		</tr>
	</table>

	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr><td class="pageBorder"><img src="images/utilSpace.gif" width="1" height="1"></td></tr>
	</table>
</body>
</html>
