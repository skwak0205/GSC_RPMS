
<%--
@quickreview SPI10 20:04:28 : [Modifications for navigating from CATIA to ENOVIA null issue for physicalID].
@quickreview VKY 19:12:17 [XSS: Encode vulnerable data ].
@quickreview X32 19:06:20 : Old Reporter removal Modification
@quickreview E25 19:06:25 :  Fix for IR-696086-3DEXPERIENCER2020x .Green Color for collaboration block case.
@quickreview E25 19:01:07 :  Modifications for short/detailed report option modifications from web side reporter on/off case
@quickreview E25 18:12:11 [Modifications for New message Specifications under variable].
@quickreview E25 17:08:16 [Modifications for integration of new reporter class].
 -->
<%-- @quickreview SBM1 17:09:25 : IR-550969-3DEXPERIENCER2018x Fix to prevent XSS attacks --%>
<%@page import="com.matrixone.apps.domain.util.ContextUtil"%>
<%@include file="../common/emxNavigatorInclude.inc"%>
<%@include file="../common/emxNavigatorTopErrorInclude.inc"%>
<%@page
	import="com.matrixone.vplmintegrationitf.util.VPLMIntegrationConstants"%>
<%@page
	import="com.matrixone.vplmintegrationitf.util.VPLMIntegrationReporter"%>
<%@ page import="com.matrixone.vplmintegrationitf.util.VPLMIntegrationEnvVariable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.*"%>
<%@page import="com.matrixone.apps.domain.DomainConstants"%>
<%@page import="com.matrixone.apps.domain.util.MapList"%>
<%@page import="matrix.util.StringList"%>
<%@page import="com.dassault_systemes.vplmintegration.sdk.enovia.VPLMBusObject"%>

<html>

<head>
<title></title>

<%@page import="com.matrixone.vplmintegration.util.*"%>
<!-- SM7 - IR-96983- Feb 24, 2011 - The link to hide/show the "corresponding modifications in VPM" is NLS enabled now -->
<%
	String targetLocation = emxGetParameter(request, "targetLocation");
	String hidecModinVPM = UINavigatorUtil.getI18nString("emxVPLMSynchro.Msg.Success.HideModinVPM",
			"emxVPLMSynchroStringResource", request.getHeader("Accept-Language"));
	String showModinVPM = UINavigatorUtil.getI18nString("emxVPLMSynchro.Msg.Success.ShowModinVPM",
			"emxVPLMSynchroStringResource", request.getHeader("Accept-Language"));
%>
<script language="javascript" src="../common/scripts/emxUIConstants.js"></script>
<script language="javascript" src="../common/scripts/emxUICore.js"></script>
<script language="javascript" src="../common/scripts/emxUICoreMenu.js"></script>
<script language="javaScript" src="../common/scripts/emxUITableUtil.js"></script>

<script language="javascript">
		    addStyleSheet("emxUIDefault","../common/styles/");
		    addStyleSheet("emxUIToolbar","../common/styles/");
		    addStyleSheet("emxUIList","../common/styles/");
		    addStyleSheet("emxUIProperties","../common/styles/");
    addStyleSheet("emxUITemp","../");
		    addStyleSheet("emxUIForm","../common/styles/");
    </script>

<script language="javascript">
			function showVPMDetails(){
				var hidecModinVPMJS = "<%=XSSUtil.encodeForJavaScript(context,hidecModinVPM)%>";
				var showModinVPMJS = "<%=XSSUtil.encodeForJavaScript(context,showModinVPM)%>";
				var VPMDetails = document.getElementById('VPMDetails');
				var VPMDetailsLink = document.getElementById("VPMDetailsLink");
				if(VPMDetails.style.display=="none"){
					VPMDetails.style.display="block";
					<!-- SM7 - IR-96983- Feb 24,2011 - Providing the translated string to display -->
					//VPMDetailsLink.firstChild.data="Hide corresponding modifications in VPM";
					VPMDetailsLink.firstChild.data=hidecModinVPMJS;
				}else if(VPMDetails.style.display=="block"){
					VPMDetails.style.display="none";
					<!-- SM7 - IR-96983- Feb 24,2011 - Providing the translated string to display -->
					//VPMDetailsLink.firstChild.data="See corresponding modifications in VPM";
					VPMDetailsLink.firstChild.data=showModinVPMJS;
			}
        }
		
			function doCancel(targetLocation){
				if(targetLocation=="popup"){
					top.close();
				}else if(targetLocation=="slidein"){
					top.closeSlideInDialog();
		}
		}
		<!-- SM7 - IR-96983- Feb 24, 2011 - Javascript method added to close when done button is clicked -->
		function doneAction()
        {
            if('<%=XSSUtil.encodeForJavaScript(context, targetLocation)%>' == 'slidein')
           	{
				//X32:IR-452500-3DEXPERIENCER2017x :No need to check frame name condition. Close slide in window and refresh Table view.
				getTopWindow().closeSlideInDialog();
				getTopWindow().refreshTablePage();
			}else{
				//Close the popup window
				parent.window.close();
				//Refresh the table view
				parent.window.opener.top.refreshTablePage();
          }
       }
		function doUnload()
		{
		 if (window.event.clientX < 0 && window.event.clientY < 0)
		 {
		   alert("Window is closed.");
		 }
		}
	    </script>


</head>

<%
	String header = "Publish Precise BOM";

	String browser = request.getHeader("USER-AGENT");
	boolean isIE = browser.indexOf("MSIE") > 0;
%>
<!-- SM7 - IR-96983- extra header showing "Publish Precise BOM" is disabled -->

<body class="slide-in-panel" onload="turnOffProgress();">
	<!--
		<div id="pageHeadDiv">
			<form name="formHeaderForm">
				<table>
					<tr>
						<td class="page-title">
							<h2><%=header%></h2>
						</td>
						<td class="functions">
							<table>
								<tr>
									<td class="progress-indicator"><div id="imgProgressDiv"></div></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<div class="toolbar-container" id="divToolbarContainer">
					<div id="divToolbar" class="toolbar-frame"></div>
				</div>
			</form>     		
		</div>
    
		<div id="divPageBody" <%if (isIE) {%> style="top:85px;" <%}%>>
		-->
	<%
		try {

			String objectProcessed = UINavigatorUtil.getI18nString(
					"emxVPLMSynchro.Notify.Success.ObjProcessed",
					"emxVPLMSynchroStringResource",
					request.getHeader("Accept-Language"));
			String objectCreated = UINavigatorUtil.getI18nString(
					"emxVPLMSynchro.Notify.Success.ObjCreated",
					"emxVPLMSynchroStringResource",
					request.getHeader("Accept-Language"));
			String objectUpdated = UINavigatorUtil.getI18nString(
					"emxVPLMSynchro.Notify.Success.ObjUpdated",
					"emxVPLMSynchroStringResource",
					request.getHeader("Accept-Language"));
			String objectDeleted = UINavigatorUtil.getI18nString(
					"emxVPLMSynchro.Notify.Success.ObjDeleted",
					"emxVPLMSynchroStringResource",
					request.getHeader("Accept-Language"));

			String msgString = null;
			String collaborationStatus = null;
			String languageStr = request.getHeader("Accept-Language");
			// Get the object ID
						
			String objID = emxGetParameter(request, "objectId");
			if(!objID .contains("."))
			{
			VPLMBusObject busObj = new VPLMBusObject(context, objID);
			objID = busObj.getIdForADK();
			} 
			
			String detailedReport = emxGetParameter(request, VPLMIntegrationReporter.DETAILED_REPORT);
			boolean detailedReportflag= detailedReportflag=Boolean.parseBoolean(detailedReport);
			//Create the arguments
			Hashtable argTable = new Hashtable();
			argTable.put("ROOTID", objID);
			

			//Pass all the arguments in the URL
			Map params = request.getParameterMap();
			java.util.Set keys = params.keySet();
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value[] = (String[]) params.get(key);
				if (value != null && value[0].toString().length() > 0) {
					argTable.put(key, value[0].toString());
				}
			}

			String[] args = JPO.packArgs(argTable);

			if (Boolean.valueOf(com.matrixone.jsystem.util.Sys.getEnvEx("PUEECOSync"))) {

				//SBM1: IR-367074-V6R2013x:  Block the synchronization and add a warning message if a context item is missing 
				// This code is added here instead of java code, to block only manual synchronization
				VPLMBusObject chgObj = new VPLMBusObject(context, (String) argTable.get("ROOTID"));
				if (chgObj.matchWithTypes(PropertyUtil.getSchemaProperty("type_PUEECO"))) {
					MapList contextItemList = chgObj.getHandlerForADK().getRelatedObjects(
									context,
									PropertyUtil.getSchemaProperty(context,"relationship_ContextItem"),// relationship pattern
									DomainConstants.TYPE_PART, // object pattern
									new StringList(DomainConstants.SELECT_ID), // object selects
									new StringList(DomainConstants.SELECT_RELATIONSHIP_ID), // relationship selects
									false, // to direction
									true, // from direction
									(short) 1, // recursion level
									null, // object where clause
									null, // relationship where clause
									null, null, null);

					if (contextItemList == null	|| contextItemList.isEmpty()) {
						String st_msg = UINavigatorUtil.getI18nString("emxVPLMSynchro.Warning.ContextItemMissing","emxVPLMSynchroStringResource", request.getHeader("Accept-Language"));
						throw new Exception(st_msg);
					}
				}
			}
			// Invoke the JPO
			Map matrixObjIDvplmObjIDMap = (Map) JPO.invoke(context,"VPLMIntegSyncWithVPLM", null, "execute", args,	Map.class);

			if (matrixObjIDvplmObjIDMap != null
					&& matrixObjIDvplmObjIDMap.size() > 0) {
			
				
				List msgVector = null;
				int msgVectorSize = 0;
				Map<String, List<String>> syncReport=null;
			  	 //detailedMessageMap
				syncReport=(Map<String, List<String>>)matrixObjIDvplmObjIDMap.get(objID);
				ArrayList<String> errorMessage = (ArrayList) syncReport.get(VPLMIntegrationReporter.ERROR_MESSAGES);
				msgVector =(ArrayList) syncReport.get(VPLMIntegrationReporter.DETAILED_REPORT);
				ArrayList<String> shortReport =(ArrayList) syncReport.get(VPLMIntegrationReporter.SHORT_REPORT);
				ArrayList<String> collabStatus =(ArrayList) syncReport.get(VPLMIntegrationReporter.Collab_Status);
			if (errorMessage != null && !errorMessage.isEmpty()) {
				String throwerrorMessage="";	
				for(String error:errorMessage)
    			{
					throwerrorMessage=throwerrorMessage.concat(error);
    			}
				Exception exc = null;
				 if (errorMessage instanceof ArrayList) {
						exc = new Exception(throwerrorMessage);
					} else {
						exc = new Exception(
								"unknown error type (neither exception nor message available)");
					}

					throw exc;
				}
				 msgString=collabStatus.get(1);
                 collaborationStatus = collabStatus.get(0);


					 
			/*    if (errorMessage.isEmpty() && msgVector.isEmpty())
				 {
					 msgString=VPLMIntegrationReporter.getMessage(context,VPLMIntegrationConstants.NLS_RESOURCE_FILE_ITG,"emxVPLMSynchro.Success.SynchroAlreadyDone");
				 }
				 else
					 {
						 if(errorMessage.isEmpty())
						 msgString= VPLMIntegrationReporter.getMessage(context,VPLMIntegrationConstants.NLS_RESOURCE_FILE_ITG,"emxVPLMSynchro.Success.VPLMSyncSuccess");
						 						 
					 }
			        msgVectorSize = msgVector.size();    */
	%>
	<table>
		<tr>
			<td class="heading1">
				<%
					if (collaborationStatus.equals(VPLMIntegrationReporter._block)) {
				%>
				<!-- SM7 - IR-96983- Feb 24, 2011 - The synchronisation report is NLS enabled now -->
				<p style="text-align: center; color: orange; font-size: 10pt;">
					<%
						} else {
					%>
				
				<p style="text-align: center; color: green; font-size: 10pt;">
					<%
						}
					%><%=XSSUtil.encodeForHTML(context,UINavigatorUtil.getI18nString(msgString,
							"emxVPLMSynchroStringResource",
							request.getHeader("Accept-Language")))%></p>
			</td>
		</tr>

		<tr>
			<td class="inputField">
				<%
					//LUS - BOM_Sync_Reporting Enhancements : Start 12/2/2008
	List<String> shortmsgList= (ArrayList) syncReport.get(VPLMIntegrationReporter.SHORT_REPORT);
	if( (null != shortmsgList && !shortmsgList.isEmpty()))
	{
	if(shortmsgList.size() > 0)
	{
		for(int i=0;i<shortmsgList.size();i++){
			
			%><p><%=shortmsgList.get(i)%></p><%
		} 
	}
	}
%>
	</td>
		</tr>
	</table>
<%if(detailedReportflag) {%>
	<table>
		<tr>
			<td class="heading1" style="text-align: center;">
				<!-- SM7 - IR-96983-  The link to hide/show the "corresponding modifications in VPM" is NLS enabled now -->
				<a id="VPMDetailsLink" href="javascript:showVPMDetails()"> <%=XSSUtil.encodeForHTML(context, showModinVPM)%></p></a>
				<!--<a id="VPMDetailsLink"  href="javascript:showVPMDetails()"> See corresponding modifications in VPM</p></a>  -->
			</td>
		</tr>
		<tr>
			<td class="inputField" id="VPMDetails" style="display: none;">
				<%
					if (msgVector != null) {
								for (int i = 0; i < msgVector.size(); i++) {
									String msg = (String) msgVector.get(i);
				%>
				<p><%=msg%></p> <%
 	}
 			}
 %>
			</td>
		</tr>
	</table>
	<%} %>
	<%
		}
		} catch (Exception exception) {
			String msgSyncFailed = UINavigatorUtil.getI18nString(
					"emxVPLMSynchroCfg.Info.PublicationFailed",
					"emxVPLMSynchroCfgStringResource",
					request.getHeader("Accept-Language"));
			String msgString = ("" + exception.getMessage()).replaceAll(
					"'", "");

			java.util.List list = new ArrayList();
			StringTokenizer tok = new StringTokenizer(msgString, "|");
			int counter = 0;
			while (tok.hasMoreElements()) {
				String line = (String) tok.nextElement();
				list.add(line.substring(line.lastIndexOf('|') + 1));
			}
	%>
	<table>
		<tr>
			<td class="heading1">
				<p style="text-align: center; color: red; font-size: 10pt;"><%=XSSUtil.encodeForHTML(context,msgSyncFailed)%></p>
			</td>
		</tr>
		<tr>
			<td class="inputField">
				<table>
					<tr>
						<th><%=XSSUtil.encodeForHTML(context,UINavigatorUtil.getI18nString(
						"emxVPLMSynchro.Failed.Name",
						"emxVPLMSynchroStringResource",
						request.getHeader("Accept-Language")))%></th>
						<th><%=XSSUtil.encodeForHTML(context,UINavigatorUtil.getI18nString(
						"emxVPLMSynchro.Failed.Message",
						"emxVPLMSynchroStringResource",
						request.getHeader("Accept-Language")))%></th>
					</tr>
					<%
						if (list.size() == 3) {
					%>
					<tr class="even">
						<td><a
							href="JavaScript:onLink('emxTree.jsp?objectId=<%=(String) list.get(0)%>')"><%=(String) list.get(1)%></a></td>
						<td class="field"><%=(String) list.get(2)%></td>
					</tr>
					<%
						} else {
					%>
					<tr class="even">
						<td class="field">&nbsp;</td>
						<td class="field"><%=msgString%></td>
					</tr>
					<%
						}
					%>
				</table>
			</td>
		</tr>
	</table>
	<%
		}
	%>
	</div>

	<div id="divPageFoot">
		<table>
			<tr>
				<td class="functions"></td>
				<td class="buttons">
					<table>
						<tr>
							<!-- SM7 - IR-96983-  The extra cancel button in the page is hidden-->
							<!--  <td><a n0ame="cancelButton" id="cancelButtonId" href="javascript:doCancel('<%=targetLocation%>')"><img src="images/buttonDialogCancel.gif" border="0" alt="<emxUtil:i18n localize="i18nId">emxFramework.Common.Close</emxUtil:i18n>"></a></td> -->
								<!--<td><a name="cancelLabel" id="cancelLabelId" href="javascript:doCancel('<%=targetLocation%>')" class="button"><emxUtil:i18n localize="i18nId">emxFramework.Common.Close</emxUtil:i18n></a></td> -->
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>

</body>

</html>
