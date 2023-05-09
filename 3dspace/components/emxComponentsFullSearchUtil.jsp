<%--
  FullSearchUtil.jsp
  Copyright (c) 1993-2020 Dassault Systemes.
  All Rights Reserved.
  This program contains proprietary and trade secret information of
  Dassault Systemes.
  Copyright notice is precautionary only and does not evidence any actual
  or intended publication of such program
--%>

<%-- Common Includes --%>
<%@include file="../common/emxNavigatorTopErrorInclude.inc"%>
<%@include file="../common/emxCompCommonUtilAppInclude.inc"%>
<%@include file="../emxUICommonAppInclude.inc"%>

<%@page import="com.matrixone.apps.domain.DomainConstants"%>
<%@page import="com.matrixone.apps.domain.DomainObject"%>
<%@page import="com.matrixone.apps.domain.DomainRelationship"%>
<%@page import="com.matrixone.apps.domain.util.i18nNow"%>
<%@page import="com.matrixone.apps.domain.util.PersonUtil"%>
<%@page import="matrix.db.Context"%> 
<%@include file = "../common/enoviaCSRFTokenValidation.inc"%>
<script language="JavaScript" src="../common/scripts/emxUICore.js" type="text/javascript"></script>
<%
	boolean bIsError = false;
    String typeAhead = emxGetParameter(request, "typeAhead");
    String frameName = emxGetParameter(request, "frameName");
    String sAttrRoutePreserveTaskOwner=PropertyUtil.getSchemaProperty(context, "attribute_PreserveTaskOwner");
    String sAttrRouteChooseUsersFromUG = PropertyUtil.getSchemaProperty(context,"attribute_ChooseUsersFromUserGroup");
    String sAttrRouteAutoStopOnRejection=PropertyUtil.getSchemaProperty(context, "attribute_AutoStopOnRejection");
    String sAttrRouteBasePurpose = Framework.getPropertyValue( session, "attribute_RouteBasePurpose" );
    String SELECT_ROUTE_PRESERVE_TASK_OWNER = DomainObject.getAttributeSelect(sAttrRoutePreserveTaskOwner);
    String SELECT_ROUTE_CHOOSE_USERS_FROM_UG = DomainObject.getAttributeSelect(sAttrRouteChooseUsersFromUG);
    String SELECT_ROUTE_AUTO_STOP_ON_REJECTION = DomainObject.getAttributeSelect(sAttrRouteAutoStopOnRejection);
    String SELECT_ROUTE_BASE_PURPOSE = DomainObject.getAttributeSelect(sAttrRouteBasePurpose); 
 	SelectList selectStmts = new SelectList(5);
	selectStmts.addElement(DomainObject.SELECT_NAME);
	selectStmts.addElement(SELECT_ROUTE_BASE_PURPOSE);
	selectStmts.addElement(DomainObject.SELECT_DESCRIPTION);
	selectStmts.addElement(SELECT_ROUTE_AUTO_STOP_ON_REJECTION);
	selectStmts.addElement(SELECT_ROUTE_PRESERVE_TASK_OWNER);
	selectStmts.addElement(SELECT_ROUTE_CHOOSE_USERS_FROM_UG);

    String fromPage  = emxGetParameter(request, "fromPage");

	  if(com.matrixone.apps.framework.ui.UIUtil.isNullOrEmpty(fromPage))
	  {
	      fromPage="";
	  }
	if(fromPage.equals("routeTemplate")){
		String strRowId[] = request.getParameterValues("emxTableRowId");
    	strRowId  = com.matrixone.apps.common.util.ComponentsUIUtil.getSplitTableRowIds(strRowId);
    	String selObjId = strRowId[0];
    	DomainObject selObject = new DomainObject(selObjId);
    	String selObjName = selObject.getInfo(context, DomainConstants.SELECT_NAME);
    	%>
    	<script language="javascript">
    	debugger;
         var form = getTopWindow().getWindowOpener().document.forms[0];
         if (form) {
             if (form.subRouteTemplateName) {
                 form.subRouteTemplateName.value="<%=XSSUtil.encodeForJavaScript(context, selObjName)%>";
             }
             if (form.subRouteTemplateId) {
                 form.subRouteTemplateId.value="<%=XSSUtil.encodeForJavaScript(context, selObjId)%>";
             }
             if (form.routeTemplateCompletionAction) {
            	 form.routeTemplateCompletionAction.parentElement.parentElement.style.display="table-row";
             }
          }
         getTopWindow().close();
         </script>
         <%
       }else if(fromPage.equals("editrouteTemplate")){
		String strRowId[] = request.getParameterValues("emxTableRowId");
    	strRowId  = com.matrixone.apps.common.util.ComponentsUIUtil.getSplitTableRowIds(strRowId);
    	String selObjId = strRowId[0];
    	DomainObject selObject = new DomainObject(selObjId);
    	String selObjName = selObject.getInfo(context, DomainConstants.SELECT_NAME);
    	String fieldNameActual  = emxGetParameter(request, "fieldNameActual");
    	String fieldNameDisplay  = emxGetParameter(request, "fieldNameDisplay");
    	%>
    	<script language="javascript">
    	debugger;
    	var tmpFieldNameActual = "<%=XSSUtil.encodeForJavaScript(context, fieldNameActual)%>";
    	var tmpFieldNameDisplay = "<%=XSSUtil.encodeForJavaScript(context, fieldNameDisplay)%>";
    	var tmpFieldNameOID = tmpFieldNameActual + "OID";
        var targetWindow = getTopWindow().getWindowOpener();
        if(!targetWindow){
   		 targetWindow = getTopWindow().getWindowOpener();
   	 }
        var vfieldNameActual = targetWindow.document.getElementById(tmpFieldNameActual) ? targetWindow.document.getElementById(tmpFieldNameActual) : targetWindow.parent.document.getElementById(tmpFieldNameActual);
        var vfieldNameDisplay = targetWindow.document.getElementById(tmpFieldNameDisplay) ? targetWindow.document.getElementById(tmpFieldNameDisplay) : targetWindow.parent.document.getElementById(tmpFieldNameDisplay);
        var vfieldNameOID = targetWindow.document.getElementById(tmpFieldNameOID) ? targetWindow.document.getElementById(tmpFieldNameOID) : targetWindow.parent.document.getElementById(tmpFieldNameOID);
        if (vfieldNameActual==null && vfieldNameDisplay==null) {
        	vfieldNameActual=targetWindow.document.forms[0][tmpFieldNameActual];
        	vfieldNameDisplay=targetWindow.document.forms[0][tmpFieldNameDisplay];
        	vfieldNameOID=targetWindow.document.forms[0][tmpFieldNameOID];
        }
        vfieldNameDisplay.value ="<%=selObjName%>" ;
        vfieldNameActual.value ="<%=selObjName%>" ;
        vfieldNameOID.value ="<xss:encodeForJavaScript><%=selObjId%></xss:encodeForJavaScript>" ;
        targetWindow.document.getElementById("RouteTemplateCompletionActionId").disabled = false;
        targetWindow.document.getElementById("RouteTemplateCompletionActionId")[0].selected = true;
        targetWindow.document.getElementById("RouteTemplateCompletionActionId")[1].selected = false;
        if(getTopWindow().location.href.indexOf("emxNavigator.jsp")==-1){
        	getTopWindow().close();
        }
         </script>
         <%
       }else if(fromPage.equals("routeWizard"))
    {
    	String autoStopOnRejection = "";
    	String strRowId[] = request.getParameterValues("emxTableRowId");
    	strRowId  = com.matrixone.apps.common.util.ComponentsUIUtil.getSplitTableRowIds(strRowId);
    	String strSelectScope = EnoviaResourceBundle.getProperty(context, "emxComponentsStringResource", context.getLocale(), "emxComponents.CreateRoute.SelectScope");
		String selObjId = strRowId[0];
    	DomainObject selObject = new DomainObject(selObjId);
    	Map resultMap = selObject.getInfo(context, selectStmts);
    	String selObjName = (String)resultMap.get(DomainConstants.SELECT_NAME);
    	String description = (String)resultMap.get(DomainConstants.SELECT_DESCRIPTION);
    	autoStopOnRejection = (String)resultMap.get(SELECT_ROUTE_AUTO_STOP_ON_REJECTION);
    	String routeBasePurpose =  (String)resultMap.get(SELECT_ROUTE_BASE_PURPOSE);
    	String routePreserveTaskOwner = (String) resultMap.get(SELECT_ROUTE_PRESERVE_TASK_OWNER);
  		routePreserveTaskOwner = routePreserveTaskOwner.toLowerCase();
  		String routeChooseFromUserGroup = (String) resultMap.get(SELECT_ROUTE_CHOOSE_USERS_FROM_UG);
  		routeChooseFromUserGroup = routeChooseFromUserGroup.toLowerCase();
    	HashMap routeTemplateScopeMap = (HashMap) JPO.invoke(context, "emxRouteTemplate", null, "getRouteTemplateScopeInfo", JPO.packArgs(selObjId), HashMap.class);
    	String scopeType = (String) routeTemplateScopeMap.get("scopeType");
    	String scopeName = "";
    	String scopeID = "";
    	scopeName = (String) routeTemplateScopeMap.get("scopeName");
    	if(routeTemplateScopeMap.containsKey("scopeID")){
    		scopeID = (String) routeTemplateScopeMap.get("scopeID");
    	}

        %>

    <script language="javascript">
        var form = getTopWindow().getWindowOpener().document.forms[0];
        if (form) {
            if (form.templateName) {
                form.templateName.value="<%=XSSUtil.encodeForJavaScript(context, selObjName)%>";
            }
            if (form.template) {
                form.template.value="<%=XSSUtil.encodeForJavaScript(context, selObjName)%>";
            }
            if (form.templateId) {
                form.templateId.value="<%=XSSUtil.encodeForJavaScript(context, selObjId)%>";
            }
            if(form.txtdescription)
            {
            	form.txtdescription.value="<%=XSSUtil.encodeForJavaScript(context, description)%>";
            }
            if(form.routeBasePurpose)
            {
                form.routeBasePurpose.value="<%=XSSUtil.encodeForJavaScript(context, routeBasePurpose)%>";
            }
            if(form.routeAutoStop){
            	 form.routeAutoStop.value="<%=XSSUtil.encodeForJavaScript(context, autoStopOnRejection)%>";
            }
            if(form.checkPreserveOwner){
                if(!<%=routePreserveTaskOwner%>){
                	form.checkPreserveOwner[0].checked=true;
                }else{
                	form.checkPreserveOwner[1].checked=true;
                }           	 
           }
            if(form.ChooseUsersFromUG){
                if(!<%=routeChooseFromUserGroup%>){
                	form.ChooseUsersFromUG[0].checked=true;
                }else{
                	form.ChooseUsersFromUG[1].checked=true;
                }           	 
           }
            var checkPreserveOwnerTD = getTopWindow().getWindowOpener().document.getElementById("checkPreserveOwnerLabel");
			form.checkPreserveOwner[1].disabled=false;
            if(<%=routeChooseFromUserGroup%>){
            	form.checkPreserveOwner[0].checked=true;
            	form.checkPreserveOwner[1].disabled=true;
            }else{
            	form.checkPreserveOwner[1].disabled=false;
            }
            var routeTemplateScopes = getTopWindow().getWindowOpener().document.getElementsByName("selscope");
            var scopeName = "<%=XSSUtil.encodeForJavaScript(context, scopeName)%>";
            var scopeID = "<%=XSSUtil.encodeForJavaScript(context, scopeID)%>";
            if(routeTemplateScopes){
            	if(form.workspaceFolder && form.workspaceFolder.type != "hidden"){
            		form.workspaceFolder.value = "<%=XSSUtil.encodeForJavaScript(context, strSelectScope)%>";
            		if(form.workspaceFolderId){
            			form.workspaceFolderId.value = "";
            		}
            	}
        		if(form.btnScope){
        			form.btnScope.disabled = false;
     		    }
            	if(scopeName == "All"){
            		routeTemplateScopes[0].checked = true;
            		form.routeTemplateScope.value = "All";
            	}else if(scopeName == "Organization"){
            		routeTemplateScopes[1].checked = true;
            		form.routeTemplateScope.value = "Organization";
            	}else{
            		routeTemplateScopes[2].checked = true;
            		if(form.workspaceFolder && form.workspaceFolder.type != "hidden"){
            			if(form.workspaceFolder.type.indexOf("select") > -1){
            				var options = form.workspaceFolder.options;
            				var exists = false;
            				for (var i = 0; i < options.length; i++){
            				    if (options[i].value == scopeID){
            				      exists = true;
            				      break;
            				    }
            				}
            				if(!exists){
            					form.workspaceFolder.options[form.workspaceFolder.options.length] = new Option(scopeName, scopeID);
            					form.workspaceFolder.options[form.workspaceFolder.options.length-1].selected = true;
            				}
            			}
            			else{
            			form.workspaceFolder.value = scopeName;
            			}
            			if(form.workspaceFolderId){
            				form.workspaceFolderId.value = scopeID;
            			}
            			if(form.routeTemplateScope){
            				form.routeTemplateScope.value = scopeName;
            			}
            		}
            	}
            	if(form.btnScope){
     			   form.btnScope.disabled = true;
     		   }
            }

        }
        getTopWindow().close();
        </script>
   <%
    }
    /* mod ends vh5*/

    else{
	try
	{
		String strMode = emxGetParameter(request,"mode");
		String strObjId = emxGetParameter(request, "objectId");
		String strRelName = request.getParameter("relName");
		String strDirection = request.getParameter("direction");
		String strRowId[] = request.getParameterValues("emxTableRowId");

		// Convert any internal relationship name to its display format:
		if (strRelName != null)
		{
			strRelName = PropertyUtil.getSchemaProperty(context, strRelName);
		}

		if (strRowId == null)
		{   
			%>
				<script language="javascript" type="text/javaScript">
					alert("<emxUtil:i18n localize='i18nId'>emxFramework.IconMail.FindResults.AlertMsg1</emxUtil:i18n>");
				</script>
			<%
		}
		else
		{
			if (strMode.equalsIgnoreCase("Connect"))
			{
				String [] splittedStrRowId = com.matrixone.apps.common.util.ComponentsUIUtil.getSplitTableRowIds(strRowId);
				boolean preserve = false;  // to update the modified date on the root obj
				for (int i = 0; i < splittedStrRowId.length; i++)
				{
					String selObjId = splittedStrRowId[i];

					if ("to".equalsIgnoreCase(strDirection))
					{
						// Create the named relationship to the selected obj from the root obj:
						DomainRelationship.connect(context, selObjId, strRelName, strObjId, preserve);
					}
					else
					{
						// Create the named relationship to the root obj from the selected obj:
						DomainRelationship.connect(context, strObjId, strRelName, selObjId, preserve);
					}
				}
				%>
					<script language="javascript" type="text/javaScript">
						window.parent.getTopWindow().getWindowOpener().location.href = window.parent.getTopWindow().getWindowOpener().location.href;
						getTopWindow().closeWindow();
					</script>
				<%
			}
			
			// Start:OEP:V6R2010:BUG 372490
			// Handled the Chooser condition for RangeHref passing from WebForm for Owner Field.
			String strSearchMode = emxGetParameter(request, "chooserType");
            String fieldNameActual = emxGetParameter(request, "fieldNameActual");
            String fieldNameDisplay = emxGetParameter(request, "fieldNameDisplay");
            
			if (strMode.equalsIgnoreCase("Chooser"))
            {
	            // When choosing a person, use the name/fullname instead...
			        if (strSearchMode.equals("PersonChooser"))
			        {
		            String fieldNameOID = emxGetParameter(request, "fieldNameOID");
		
		            // For most webform choosers, default to the object id/name...
		            String selObjId = strRowId[0].split("[|]")[1];
		            String strObjID = selObjId;
		            DomainObject selObject = new DomainObject(selObjId);
		            String selObjName = selObject.getInfo(context, DomainConstants.SELECT_NAME);
		
		            selObjId = selObjName;
		            selObjName = PersonUtil.getFullName(context, selObjName);
		            
				    %>
				    <script language="javascript" type="text/javaScript">
				    var typeAhead = "<%=XSSUtil.encodeForJavaScript(context, typeAhead)%>";
					var targetWindow = null;
					if(typeAhead == "true")	{
						var frameName = "<%=XSSUtil.encodeForJavaScript(context, frameName)%>";
						if(frameName == null || frameName == "null" || frameName == "") {
							targetWindow = window.parent;
						} else {
							targetWindow = getTopWindow().findFrame(window.parent, frameName);
						}
					} else	{
							targetWindow = getTopWindow().getWindowOpener();
					}
					var tmpFieldNameOID = "<%=XSSUtil.encodeForJavaScript(context, fieldNameActual)%>" + "OID";				    
				   var vfieldNameActual = targetWindow.document.getElementsByName("<%=XSSUtil.encodeForJavaScript(context, fieldNameActual)%>");
				   var vfieldNameDisplay = targetWindow.document.getElementsByName("<%=XSSUtil.encodeForJavaScript(context, fieldNameDisplay)%>");
				   var vfieldNameOID = targetWindow.document.getElementsByName(tmpFieldNameOID);
				   
                   vfieldNameActual = vfieldNameActual == null ? targetWindow.document.forms[0]["<%=XSSUtil.encodeForJavaScript(context, fieldNameActual)%>"] : vfieldNameActual;
                   vfieldNameDisplay = vfieldNameDisplay == null ? targetWindow.document.forms[0]["<%=XSSUtil.encodeForJavaScript(context, fieldNameDisplay)%>"] : vfieldNameDisplay;
                   vfieldNameOID = vfieldNameOID == null ? targetWindow.document.forms[0][tmpFieldNameOID] : vfieldNameOID;
				   
				   
				   vfieldNameActual.value ="<%=XSSUtil.encodeForJavaScript(context, selObjId)%>" ;
				   vfieldNameDisplay.value ="<%=XSSUtil.encodeForJavaScript(context, selObjName)%>" ;
				   vfieldNameOID.value ="<%=XSSUtil.encodeForJavaScript(context, strObjID)%>" ;
				   if(typeAhead != "true")
				  	 getTopWindow().closeWindow();   
				   </script>
				   <%
		           }
            // if the chooser is in the Form
		            else if (strSearchMode.equals("CustomChooser"))
		            {
		                String fieldNameOID = emxGetParameter(request, "fieldNameOID");
		                String routeBasePurpose = "";
		                String routePreserveTaskOwner = "";
		                String routeChooseUsersFromUG = "";
						String templateName = "";
						String description = "";
						String autoStopOnRejection = "";
		                StringTokenizer strTokenizer = new StringTokenizer(strRowId[0] , "|");
		                String strObjectId = strTokenizer.nextToken() ;
		
		                DomainObject objContext = new DomainObject(strObjectId);
		            	if(!strObjectId.equals(null) || !"".equals(strObjectId))
		          	{
		          		           		  
		
		
		          		DomainObject routeTemplateObj = new DomainObject(strObjectId);
		          		Map resultMap = routeTemplateObj.getInfo(context, selectStmts);
		          		templateName = (String) resultMap.get(routeTemplateObj.SELECT_NAME);
		          		routeBasePurpose = (String) resultMap.get(SELECT_ROUTE_BASE_PURPOSE);
		          		routePreserveTaskOwner = (String) resultMap.get(SELECT_ROUTE_PRESERVE_TASK_OWNER);
		          		routeChooseUsersFromUG = (String) resultMap.get(SELECT_ROUTE_CHOOSE_USERS_FROM_UG); //TODO check this
		          		routeChooseUsersFromUG = routeChooseUsersFromUG.toLowerCase();
		          		description = (String)resultMap.get(DomainObject.SELECT_DESCRIPTION);
		          		//description = description.replace("\r","\\r").replace("\n","\\n");
		          		autoStopOnRejection = (String)resultMap.get(SELECT_ROUTE_AUTO_STOP_ON_REJECTION);
		          	}

		                %>
		                <script language="javascript" type="text/javaScript">
						    var typeAhead = "<%=XSSUtil.encodeForJavaScript(context, typeAhead)%>";
							var targetWindow = null;
							if(typeAhead == "true")	{
								var frameName = "<%=XSSUtil.encodeForJavaScript(context, frameName)%>";
								if(frameName == null || frameName == "null" || frameName == "") {
									targetWindow = window.parent;
								} else {
									targetWindow = getTopWindow().findFrame(window.parent, frameName);
								}
							} else	{
									targetWindow = getTopWindow().getWindowOpener();
							}	
							var tmpFieldNameOID = "<%=XSSUtil.encodeForJavaScript(context, fieldNameActual)%>" + "OID";				    
		                    var vfieldNameActual = targetWindow.document.getElementById("<%=XSSUtil.encodeForJavaScript(context, fieldNameActual)%>");
		                    var vfieldNameDisplay = targetWindow.document.getElementById("<%=XSSUtil.encodeForJavaScript(context, fieldNameDisplay)%>");
		                    var vfieldNameOID = targetWindow.document.getElementById(tmpFieldNameOID);
		                    var vfieldDesc = targetWindow.document.getElementsByName("Description")[0];
		                    var vrouteBasePurpose = targetWindow.document.getElementById("RouteBasePurposeId");
		                    var vrouteAutoStopOnRejection = targetWindow.document.getElementById("AutoStopOnRejectionId");
		                    var vfieldPreserveTaskOwner = targetWindow.document.getElementById("calc_PreserveTaskOwner");
		 				   
		                    //var vroutePreserveTaskOwner = targetWindow.document.getElementById("PreserveTaskOwner");
		                    
		                    vfieldNameActual = vfieldNameActual == null ? targetWindow.document.forms[0]["<%=XSSUtil.encodeForJavaScript(context, fieldNameActual)%>"] : vfieldNameActual;
		                    vfieldNameDisplay = vfieldNameDisplay == null ? targetWindow.document.forms[0]["<%=XSSUtil.encodeForJavaScript(context, fieldNameDisplay)%>"] : vfieldNameDisplay;
		                    vfieldNameOID = vfieldNameOID == null ? targetWindow.document.forms[0][tmpFieldNameOID] : vfieldNameOID;
		                    vfieldDesc = vfieldDesc == null ? targetWindow.document.forms[0]["Description"] : vfieldDesc;
		                    vrouteBasePurpose = vrouteBasePurpose == null ? targetWindow.document.forms[0]["RouteBasePurpose"] : vrouteBasePurpose;
							vrouteAutoStopOnRejection = vrouteAutoStopOnRejection == null ? targetWindow.document.forms[0]["AutoStopOnRejectionId"] : vrouteAutoStopOnRejection;
							//vroutePreserveTaskOwner = vroutePreserveTaskOwner == null ? targetWindow.document.forms[0]["PreserveTaskOwner"] : vroutePreserveTaskOwner;
		                    
		                    
		                    vfieldNameActual.value ="<%=XSSUtil.encodeForJavaScript(context, strObjectId)%>" ;
		                    vfieldNameDisplay.value ="<%=XSSUtil.encodeForJavaScript(context, templateName)%>" ;
		                    vfieldNameOID.value ="<%=XSSUtil.encodeForJavaScript(context, strObjectId)%>" ;
		                    vfieldDesc.value ="<%=XSSUtil.encodeForJavaScript(context, description)%>" ;
		                    vrouteBasePurpose.value ="<%=XSSUtil.encodeForJavaScript(context, routeBasePurpose)%>" ;
<%-- 		                    vroutePreserveTaskOwner.value ="<%=XSSUtil.encodeForJavaScript(context, routePreserveTaskOwner)%>" ; --%>
							targetWindow.document.querySelectorAll("#PreserveTaskOwner")[1].disabled=false;
							if(!<%=routeChooseUsersFromUG%>) {
		                    if(!<%="true".equalsIgnoreCase(routePreserveTaskOwner)%>){
		                    	targetWindow.document.querySelectorAll("#PreserveTaskOwner")[0].checked=true;
			                }else{
			                	targetWindow.document.querySelectorAll("#PreserveTaskOwner")[1].checked=true;
				            }
			                    vfieldPreserveTaskOwner.style.pointerEvents = "";
		            		}else {
		            			targetWindow.document.querySelectorAll("#PreserveTaskOwner")[0].checked=true; //select "Retain Person assignment if choose users from UG is true
		            			targetWindow.document.querySelectorAll("#PreserveTaskOwner")[1].disabled=true;
		            		}
		                    vrouteAutoStopOnRejection.value = "<%=XSSUtil.encodeForJavaScript(context, autoStopOnRejection)%>" ;
		                    if(typeAhead != "true")
			                    getTopWindow().closeWindow();
		                  </script>
		             <%
		            }
		}
		// END:OEP:V6R2010:BUG 372490
		}
	}
	catch (Exception e)
	{
		bIsError = true;
		if(e.getMessage().contains("connect business object fail")){
			String strAccessError   = EnoviaResourceBundle.getProperty(context, "emxComponentsStringResource", context.getLocale() , "emxComponents.Issue.CheckAccess");
			session.putValue("error.message", "" + strAccessError);
		}else {
		session.putValue("error.message", "" + e);
		}
		//emxNavErrorObject.addMessage(e.toString().trim());
	}// End of main Try-catck block
  }//end of main else
%>

<%@include file="../common/emxNavigatorBottomErrorInclude.inc"%>
