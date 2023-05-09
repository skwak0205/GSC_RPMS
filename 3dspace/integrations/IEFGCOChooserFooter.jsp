﻿<%--  IEFGCOChooserFooter.jsp

   Copyright (c) 2016-2020 Dassault Systemes. All rights reserved.
   This program contains proprietary and trade secret information of
   Dassault Systemes and its subsidiaries. Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

--%>
<%@ include file ="MCADTopInclude.inc" %>

<%
	MCADIntegrationSessionData integSessionData = (MCADIntegrationSessionData) session.getAttribute("MCADIntegrationSessionDataObject");
%>

<html>
<head>
<link rel="stylesheet" href="../common/styles/emxUIDefault.css" type="text/css">
<link rel="stylesheet" href="../common/styles/emxUIList.css" type="text/css">
<script language="JavaScript" src="../common/scripts/emxUIConstants.js" type="text/javascript"></script>
<script language="javascript">
	function doDone()
	{
      var framecontentFrame =  findFrame(top,"contentFrame");
	  framecontentFrame.formSubmit();
	}

	function doCancel()
	{
	  top.close();
	}
</script>
</head>

<body>
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr><td>&nbsp</td></tr>
		<tr>
			<td align="right">
				<table border="0">
					<tr>
					        <!--XSSOK-->
						<td align="right"><a href="javascript:doDone()"><img src="../common/images/buttonDialogDone.gif" border="0"><%=integSessionData.getStringResource("mcadIntegration.Server.ButtonName.Submit")%></a></td>
						<!--XSSOK-->
						<td align="right"><a href="javascript:doCancel()"><img src="../common/images/buttonDialogCancel.gif" border="0"><%=integSessionData.getStringResource("mcadIntegration.Server.ButtonName.Cancel")%></a></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
