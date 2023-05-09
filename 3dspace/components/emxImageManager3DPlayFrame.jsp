
<%-- emxLaunch3DPlay.jsp
   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
--%>

<%@include file="../common/emxNavigatorNoDocTypeInclude.inc"%>
<%@include file="../common/emxNavigatorTopErrorInclude.inc"%>


<emxUtil:localize id="i18nId" bundle="emxComponentsStringResource"
	locale='<%= XSSUtil.encodeForHTML(context, request.getHeader("Accept-Language")) %>' />


<emxUtil:localize id="i18nId" bundle="emxComponentsStringResource" locale='<%= XSSUtil.encodeForHTMLAttribute(context, request.getHeader("Accept-Language")) %>' />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%
String isUrl = (String)emxGetParameter(request, "isUrl");


String physicalId     = (String)emxGetParameter(request, "physicalid");
String dType    = (String)emxGetParameter(request, "dType");
String serverUrl = (String)emxGetParameter(request, "serverUrl");
String filename = (String)emxGetParameter(request, "filename");


%>


<title>Insert title here</title>

<script type="text/javascript" src="../webapps/AmdLoader/AmdLoader.js"></script>
<script src="../webapps/c/UWA/js/UWA_W3C_Alone.js"></script>
<script type="text/javascript" src="../common/scripts/jquery-latest.js"></script>
<script type="text/javascript" src="../common/scripts/emxUICore.js"></script>

<script>
var plaformServiceData = "";
var platformId = "";
if(getTopWindow() && getTopWindow().getWindowOpener() && getTopWindow().getWindowOpener().getTopWindow()){
	plaformServiceData = getTopWindow().getWindowOpener().getTopWindow().x3DPlatformServices;
	platformId = getTopWindow().getWindowOpener().getTopWindow().x3DPlatformServices[0].platformId;
}else if(getTopWindow()){
	plaformServiceData = getTopWindow().x3DPlatformServices;
	platformId = getTopWindow().x3DPlatformServices[0].platformId;
}

var myAppsURL = "<%=XSSUtil.encodeForJavaScript(context, FrameworkUtil.getMyAppsURL(context, request, response))%>"+'/resources/AppsMngt';
var clntlang = "<%=XSSUtil.encodeForJavaScript(context, (String)request.getHeader("Accept-Language"))%>";
var curUserId = "<%=XSSUtil.encodeForJavaScript(context,context.getUser())%>";

if(clntlang){
	clntlang = clntlang.substr(0,2);
}
getTopWindow()["COMPASS_CONFIG"] ={};
getTopWindow()["COMPASS_CONFIG"].userId  = curUserId;
getTopWindow()["COMPASS_CONFIG"].lang = clntlang;
getTopWindow()["COMPASS_CONFIG"].myAppsBaseUrl= myAppsURL;

	require([
	             'DS/3DPlayHelper/3DPlayHelper'
	         ], function (Player3DPlayWeb) {
		
		if(<%=XSSUtil.encodeForJavaScript(context,isUrl)%>===false) {
	        	p= Player3DPlayWeb({container:'divImage',
	        		input:{		        			
	        			asset: {
	        					provider: 'EV6',
	        					physicalid: '<%=XSSUtil.encodeForJavaScript(context,physicalId)%>',
	        					dtype:'<%=XSSUtil.encodeForJavaScript(context,dType)%>',
	        					serverurl:'<%=XSSUtil.encodeForJavaScript(context,serverUrl)%>',
								tenant: platformId
	        					}
	        			},
	        			options : {
	        				loading: 'autoplay',
							platformServices :  plaformServiceData
	        				}
	        			});
	        	
	         } else {
	        	 Player3DShare({container:'divImage',
	        	    	input: {
	        	    		asset: {
	        	    			provider: 'FILE',
	        	    			filename: imageURL
	        	    			}
	        	    }, 
	        	    options : {loading: 'autoplay'}});

	        		
	        	}
	        	
	        	
	         });





</script>


</head>
<body>

<div id="divImage">
</div>

</body>
</html>
