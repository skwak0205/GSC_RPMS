<%--  emxDomainAccessProcess.jsp   - The Collection Memeber delete object processing page
   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
--%>

<%@include file = "emxNavigatorInclude.inc"%>
<%@include file = "emxNavigatorTopErrorInclude.inc"%>
<%@include file = "enoviaCSRFTokenValidation.inc"%>
<%@page import="com.matrixone.apps.domain.util.FrameworkUtil"%>
<%
    PropertyUtil.setGlobalRPEValue(context, DomainAccess.RPE_MEMBER_ADDED_REMOVED, "true");    
    String objectId = emxGetParameter(request, "objectId");
    String access = emxGetParameter(request, "access");
	String cmdEventToHandle = emxGetParameter(request, "cmdName");
	String jpoName = emxGetParameter(request,"jpoName");
	String methodName = emxGetParameter(request,"methodName");
    String[] ids = emxGetParameterValues(request, "emxTableRowId");
    if(UIUtil.isNullOrEmpty(jpoName) && UIUtil.isNullOrEmpty(methodName)){
    	jpoName = "emxDomainAccess";
    	methodName = "deleteAccess";
    }
    
        
    //System.out.println("DEFAULT PROJECT = " +DomainAccess.getDefaultProject(context));
    try{
    	
    	Locale locale = new Locale((String)request.getHeader("Accept-Language"));
        
        if("addSecurityContext".equals(cmdEventToHandle) && null == ids)
        		{
        	String strRetMsg = EnoviaResourceBundle.getProperty(context, "emxFrameworkStringResource", locale, "emxFramework.Common.PleaseSelectitem");
        	%>
        	<script type="text/javascript">
        	      alert ("<%=XSSUtil.encodeForJavaScript(context, strRetMsg)%>");
        	      if(parent.SUBMIT_URL_PREV_REQ_IN_PROCESS)
        	      {
    				  parent.setSubmitURLRequestCompleted();
    			  }
        	</script>
        	<% 
        	return;
        		}
    	
    if(cmdEventToHandle.equals("deleteAccess")) {						
	    Map paramMapForJPO = new HashMap();		
		paramMapForJPO.put("busObjId", objectId);			
		paramMapForJPO.put("emxTableRowIds" ,ids);		
		String[] args = JPO.packArgs(paramMapForJPO);
		FrameworkUtil.validateMethodBeforeInvoke(context, jpoName, methodName, "Program");
		JPO.invoke(context, jpoName, null, methodName, args);
    } else {       
	    String owner = "";
	    String ownerAccess = "";
	    String defaultAccess = "";
	    String OID = "";	    
		for(int i =0; i<ids.length;i++)
	    {
	    
	    	StringList idList = com.matrixone.apps.domain.util.StringUtil.split(ids[i], "|");
	    	if( idList.size() >= 1 )
	    	{
	      		String busId = (idList.size() >2) ? (String)idList.get(1) : objectId;
	      		if(!busId.equals(OID))
	      		{
	      			OID = busId;
	      			owner =  new DomainObject(busId).getInfo(context, DomainObject.SELECT_OWNER);
	      			StringList accessNames = DomainAccess.getLogicalNames(context, busId);
	      			//System.out.println("ACCESSNAME ===== "+ accessNames);
	      			defaultAccess = (String)accessNames.get(0);
	      			ownerAccess = (String)accessNames.get(accessNames.size()-1);      			
	      		}
	 			if (cmdEventToHandle.equals("addPerson"))
				{
					String personId = (String)idList.get(0);
					String personName = new DomainObject(personId).getInfo(context, DomainObject.SELECT_NAME);
					if(personName.equals(owner))
					{
						DomainAccess.createObjectOwnership(context, busId, personId, ownerAccess, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
					} else {
						DomainAccess.createObjectOwnership(context, busId, personId, defaultAccess, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
					}
				} else if (cmdEventToHandle.equals("addProject"))
				{
					String projectId = (String)idList.get(0);
					String projectName = new DomainObject(projectId).getInfo(context, DomainObject.SELECT_NAME);
					//System.out.println("Inside DomainAccess Process" +projectName);
					DomainAccess.createObjectOwnership(context, busId, null, projectName, defaultAccess, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
				} else if (cmdEventToHandle.equals("addOrg"))
				{
					String orgId = (String)idList.get(0);
					String orgName = new DomainObject(orgId).getInfo(context, DomainObject.SELECT_NAME);
					//System.out.println("Inside DomainAccess Process" +orgName);
					DomainAccess.createObjectOwnership(context, busId, orgName, null, defaultAccess, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
				} else if (cmdEventToHandle.equals("addSecurityContext"))
				{
					String secContextId = (String)idList.get(0);
					String orgName = new DomainObject(secContextId).getInfo(context, DomainConstants.SELECT_RELATIONSHIP_SECURITY_CONTEXT_ORGANIZATION_TO_NAME);
					String projectName = new DomainObject(secContextId).getInfo(context, DomainConstants.SELECT_RELATIONSHIP_SECURITY_CONTEXT_PROJECT_TO_NAME);
					//System.out.println("Inside DomainAccess Process" +orgName);
					//System.out.println("Inside DomainAccess Process" +projectName);
					DomainAccess.createObjectOwnership(context, busId, orgName, projectName, defaultAccess, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
				} else if(cmdEventToHandle.equals("addUserGroups")) {
					String userGroupId = (String)idList.get(0);
					String userGroupName = new DomainObject(userGroupId).getInfo(context, DomainObject.SELECT_NAME);
					DomainAccess.createObjectOwnershipForUserGroups(context, busId, userGroupName, defaultAccess, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
				}  
	      	}
	    }	
    }
    }catch(Exception e){
    	session.setAttribute("error.message", e.getMessage());
    }
%>
<%@include file = "emxNavigatorBottomErrorInclude.inc"%>

<script>
	var pageToRefresh = getTopWindow().getWindowOpener();
	var cmdEvent = "<%=XSSUtil.encodeForJavaScript(context,(String) cmdEventToHandle)%>";
	if (pageToRefresh) {
		if ("deleteAccess" == cmdEvent){
			var detailsFrame = getTopWindow().findFrame(getTopWindow(),"detailsDisplay");
			if(detailsFrame){
 				detailsFrame.location.reload();
 			}else{
 				getTopWindow().location.href=getTopWindow().location.href.replace("persist=true","");
 			}
		} else {
		var refreshURL = getTopWindow().getWindowOpener().location.href;
		refreshURL = refreshURL.replace("persist=true","");
		getTopWindow().getWindowOpener().location.href = refreshURL;
		getTopWindow().closeWindow();
		
		}
	}
	else
	{
		getTopWindow().refreshTablePage();
	}
</script>
