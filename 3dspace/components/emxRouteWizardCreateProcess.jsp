<%--  emxRouteWizardCreateProcess.jsp  --  Editing Route object
  Copyright (c) 1992-2020 Dassault Systemes.
  All Rights Reserved.
  This program contains proprietary and trade secret information of MatrixOne,Inc.
  Copyright notice is precautionary only and does not evidence any actual or intended publication of such program

  static const char RCSID[] = $Id: emxRouteWizardCreateProcess.jsp.rca 1.18 Wed Oct 22 16:18:54 2008 przemek Experimental przemek $
 --%>
<%@page import="com.matrixone.apps.domain.DomainConstants"%>
<%@ include file = "../emxUICommonAppInclude.inc" %>
<%@ include file = "emxRouteInclude.inc" %>
<%@include file = "../common/enoviaCSRFTokenValidation.inc"%>
<jsp:useBean id="formBean" scope="session" class="com.matrixone.apps.common.util.FormBean"/>

<%
     String keyValue=emxGetParameter(request,"keyValue");
	 String showmenuinToolbar = emxGetParameter(request,"showmenuinToolbar");

     if(keyValue == null){
       keyValue = formBean.newFormKey(session);
     }
     formBean.processForm(session,request,"keyValue");

     com.matrixone.apps.common.Person person = (com.matrixone.apps.common.Person)DomainObject.newInstance(context,DomainConstants.TYPE_PERSON);
     Route route = (Route)DomainObject.newInstance(context,DomainConstants.TYPE_ROUTE);
     String stateSelect[]    = emxGetParameterValues(request, "stateSelect");
     if(!"null".equals(stateSelect) && !(stateSelect == null)){
        HashMap hashStateMap = new HashMap();
        for(int i = 0 ; i < stateSelect.length; i++){
           StringTokenizer sTok =  new StringTokenizer(stateSelect[i], "#");
           while(sTok.hasMoreTokens()){
            String obId = sTok.nextToken();
            if(obId != null && !"".equals(obId) && !"null".equals(obId)){
                 hashStateMap.put(obId , sTok.nextToken());
            }
           }
        }
       formBean.setElementValue("hashStateMap",hashStateMap);
     }

     //start of code added for the bug 313531
	 String contentSelect[]    = emxGetParameterValues(request, "chkItem1");
	 ArrayList contentSelectArray = new ArrayList();
     if(!"null".equals(contentSelect) && !(contentSelect == null)){

        for(int i = 0 ; i < contentSelect.length; i++){
			contentSelectArray.add(contentSelect[i]);
        }
       
     }
	 formBean.setElementValue("contentSelectArray",contentSelectArray);
	 //end of code added for the bug 313531

     String relatedObjectId          =  (String) formBean.getElementValue("objectId");
     String parentId                 =  (String) formBean.getElementValue("parentId");
     String scopeId                  =  (String) formBean.getElementValue("scopeId");
     String templateId               =  (String) formBean.getElementValue("templateId");
     String templateName             =  (String) formBean.getElementValue("template");
     String routeId                  =  (String) formBean.getElementValue("routeId");
     String routeAutoName            =  (String) formBean.getElementValue("routeAutoName");
     String routeName                =  (String) formBean.getElementValue("routeName");
     /*Modified for XSS, will be reverted back when issue in FormBean is fixed*/
     String routeDescription         =  (String) emxGetParameter(request, "txtdescription");//formBean.getElementValue("txtdescription");
     String selectedAction           =  (String) formBean.getElementValue("selectedAction");
     String portalMode               =  (String) formBean.getElementValue("portalMode");
     String routeBasePurpose         =  (String) formBean.getElementValue("routeBasePurpose");
     String strAutoStopOnRejection   =  (String) formBean.getElementValue("routeAutoStop");
     String selscope                 =  (String) formBean.getElementValue("selscope");
     String routeCompletionAction    =  (String) formBean.getElementValue("routeCompletionAction");
     String supplierOrgId            =  (String) formBean.getElementValue("supplierOrgId");
     String suiteKey                 =  (String) formBean.getElementValue("suiteKey");
     String documentID               =  (String) formBean.getElementValue("documentID");
     String sContentId               =  (String) formBean.getElementValue("contentId");
     String chkRouteMembers          =  (String) formBean.getElementValue("chkRouteMembers");


      String sAttrRestartUponTaskRejection = PropertyUtil.getSchemaProperty(context, "attribute_RestartUponTaskRejection" );
      String sAttrRestrictMembers          = PropertyUtil.getSchemaProperty(context, "attribute_RestrictMembers" );
      String sAttrRouteBasePurpose         = PropertyUtil.getSchemaProperty(context, "attribute_RouteBasePurpose" );
      String sAttrRouteCompletionAction    = PropertyUtil.getSchemaProperty(context, "attribute_RouteCompletionAction" );
      String attrOriginator                = PropertyUtil.getSchemaProperty(context, "attribute_Originator");
      String typeRoute                     = PropertyUtil.getSchemaProperty(context, "type_Route");
      String policyRoute                   = PropertyUtil.getSchemaProperty(context, "policy_Route");
      String personStr                     = PropertyUtil.getSchemaProperty(context, "type_Person");
      String relRouteNode                  = PropertyUtil.getSchemaProperty(context, "relationship_RouteNode");
      String relProjectRoute               = PropertyUtil.getSchemaProperty(context, "relationship_ProjectRoute");
      String relObjectRoute                = PropertyUtil.getSchemaProperty(context, "relationship_ObjectRoute");
      String relRouteScope                 = PropertyUtil.getSchemaProperty(context, "relationship_RouteScope");
      String sAttrAutoStopOnRejection      = PropertyUtil.getSchemaProperty(context, "attribute_AutoStopOnRejection" );

      boolean bExecute                     =  false;
      String routeStart                    = emxGetParameter(request,"routeStart");
      String visblToParent                 = emxGetParameter(request,"visblToParent");
      String workspaceFolderId             = emxGetParameter(request,"workspaceFolderId");
      String workspaceFolder               = emxGetParameter(request,"workspaceFolder");
      routeAutoName                        = emxGetParameter(request,"routeAutoName");
      String checkPreserveOwner                   = emxGetParameter(request,"checkPreserveOwner");
      if(UIUtil.isNullOrEmpty(checkPreserveOwner)){
    	  checkPreserveOwner = "false";
      }
      Hashtable hashRouteWizFirst          = (Hashtable)formBean.getElementValue("hashRouteWizFirst");
      
 
      String prevSelectedScope = (String) hashRouteWizFirst.get("prevSelectedScope");
      String prevRouteTemplateId = (String) hashRouteWizFirst.get("prevRouteTemplateId");
      String prevSelectedScopeId = (String) hashRouteWizFirst.get("prevSelectedScopeId");
      String acceptLanguage = request.getHeader("Accept-Language");
      boolean emptyList = false;
      boolean isScopeWorkspace = false;
      BusinessObject wsObj = null;
      StringList workspaceGrantees = new StringList();
      
      MapList prevRouteMembersMapList = (MapList) formBean.getElementValue("routeMemberMapList");
      MapList prevRouteRoleMapList = (MapList) formBean.getElementValue("routeRoleMapList");
      MapList prevRouteGroupMapList = (MapList) formBean.getElementValue("routeGroupMapList");
      MapList prevTaskMapList = (MapList) formBean.getElementValue("taskMapList");
      hashRouteWizFirst.put("checkPreserveOwner",checkPreserveOwner);
      if(routeName!= null && !routeName.equals("null")){
          hashRouteWizFirst.put("routeName",routeName);
          hashRouteWizFirst.put("routeAutoName","");
      }

      if(routeAutoName!= null && !routeAutoName.equals("null")){
          hashRouteWizFirst.put("routeAutoName",routeAutoName);
          hashRouteWizFirst.put("routeName","");
      }

      if(templateId!= null && !templateId.equals("null")){
           hashRouteWizFirst.put("templateId",templateId);
           hashRouteWizFirst.put("prevRouteTemplateId",templateId);
      }
      if(workspaceFolderId != null && !workspaceFolderId.equals("null")){
    	  hashRouteWizFirst.put("prevSelectedScopeId",workspaceFolderId);
      }

      if(templateName!= null && !templateName.equals("null")){
           hashRouteWizFirst.put("templateName",templateName);
      }
      if(routeBasePurpose!= null && !routeBasePurpose.equals("null")){
         hashRouteWizFirst.put("routeBasePurpose",routeBasePurpose);
      }

      if(routeCompletionAction!= null && !routeCompletionAction.equals("null")){
           hashRouteWizFirst.put("routeCompletionAction",routeCompletionAction);
      }
      if(selscope!= null && !selscope.equals("null")){
         hashRouteWizFirst.put("selscope",selscope);
         if(prevSelectedScope!=null && !prevSelectedScope.equals(selscope)){
        	 hashRouteWizFirst.put("prevSelectedScope", selscope);
        	 emptyList = true;
         }else if(selscope.equals("ScopeName")){
        	 hashRouteWizFirst.put("prevSelectedScope", selscope);
       		 isScopeWorkspace = true;
       		 wsObj = new BusinessObject(workspaceFolderId);
       		 workspaceGrantees = wsObj.getGrantees(context);
       		 if(prevSelectedScopeId!=null && !prevSelectedScopeId.equals(workspaceFolderId)){
       			 hashRouteWizFirst.put("prevSelectedScopeId",workspaceFolderId);
                 emptyList = true; 
       		 }else{
       			emptyList = false;
       		 }
	     }
         if(selscope.equals("ScopeName")){
             hashRouteWizFirst.put("selscopeId",workspaceFolderId);
             hashRouteWizFirst.put("selscopeName",workspaceFolder);
         }
      }
      if(UIUtil.isNotNullAndNotEmpty(templateId)  && !templateId.equals(prevRouteTemplateId)){
     	 emptyList = true;
      }

      if(parentId!= null && !parentId.equals("null")){
              hashRouteWizFirst.put("objectId",parentId);
      }

      if(routeStart == null || routeStart.equals("null")){
            routeStart = "";
      }
      if(visblToParent == null || visblToParent.equals("null")){
            visblToParent = "";
      }
      if(strAutoStopOnRejection!= null && !strAutoStopOnRejection.equals("null")){
         hashRouteWizFirst.put("routeAutoStop",strAutoStopOnRejection);
      }

      hashRouteWizFirst.put("routeStart",routeStart);
      hashRouteWizFirst.put("visblToParent",visblToParent);
      hashRouteWizFirst.put("routeDescription",routeDescription);
      String sPassedType  =  "";
      // the route summary page from workspace / workspace vault so the objectid is set for both the types.
      String sProjectId   =  relatedObjectId;
      String strProjectId = "";
      MapList isExists = new MapList();
	  // to check if Route with same name exists
      if (routeName != null && !routeName.equals("null") && !"".equals(routeName) && (!"checked".equals(routeAutoName))){
          isExists = DomainObject.findObjects(context,Route.TYPE_ROUTE,routeName,null,null,null,null,false,null);
      }

      if (isExists.size() > 0) {
        com.matrixone.apps.domain.util.i18nNow loc = new com.matrixone.apps.domain.util.i18nNow();
        String text = loc.GetString("emxComponentsStringResource", acceptLanguage, "emxComponents.CreateFolder.AlreadyExists");
        session.putValue("error.message"," " + text);
        bExecute = true;
        hashRouteWizFirst = (Hashtable)formBean.getElementValue("hashRouteWizFirst");
        hashRouteWizFirst.put("routeName","");
        if(!(prevRouteMembersMapList != null && prevRouteMembersMapList.size() > 0)){
        hashRouteWizFirst.put("prevRouteTemplateId","");
        }
        
        formBean.setElementValue("hashRouteWizFirst",hashRouteWizFirst);
        formBean.setFormValues(session);
        formBean.removeElement("routeName");
      }
      MapList memberMapList = new MapList();
      MapList roleMapList = new MapList();
      MapList userGroupMapList = new MapList();
      MapList groupMapList = new MapList();
      MapList taskMapList = new MapList();
      HashMap actionRequiredMap=new HashMap();
      String ATTRIBUTE_UserGroupAction = PropertyUtil.getSchemaProperty(context, "attribute_UserGroupAction");
      String ATTRIBUTE_UserGroupLevelInfo = PropertyUtil.getSchemaProperty(context, "attribute_UserGroupLevelInfo");
      if(bExecute == false){ //If route object doesn't exists
         // To Add route template members to route via Route Node..
         //the below condition is duplicate check added just to overcome null pointer exception extra
      if(templateId != null && !"null".equals(templateId) && !"".equals(templateId)) {
	     //if template id is changed, then get route template memebers, else retain the previous list
	     if(UIUtil.isNotNullAndNotEmpty(templateId) && !templateId.equals(prevRouteTemplateId)){
	    	 hashRouteWizFirst.put("prevRouteTemplateId",templateId);
	         DomainObject routeTemplateObj   = DomainObject.newInstance(context,templateId);
	         if(memberMapList == null || ( memberMapList != null && memberMapList.size()== 0)) {
			    MapList domainAccessSummaryList = Route.getOwnershipAccessOnRoute(context, templateId);
	            String selOrg = "to["+routeTemplateObj.RELATIONSHIP_EMPLOYEE+"].from.name";
	            String selOrgTitle = "to["+routeTemplateObj.RELATIONSHIP_EMPLOYEE+"].from.attribute[Title]";
	            StringList selectPersonStmts    = new StringList();
	            selectPersonStmts.add(routeTemplateObj.SELECT_ID);
	            selectPersonStmts.add(routeTemplateObj.SELECT_TYPE);
	            selectPersonStmts.add(routeTemplateObj.SELECT_NAME);
	            selectPersonStmts.add("attribute["+routeTemplateObj.ATTRIBUTE_FIRST_NAME+"]");
	            selectPersonStmts.add("attribute["+routeTemplateObj.ATTRIBUTE_LAST_NAME+"]");
	            selectPersonStmts.add(selOrg);
	            selectPersonStmts.add(selOrgTitle);
	            //relationShip stringList
	            StringList sRelationStmts    = new StringList();
	            sRelationStmts.add("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE+"]");
	            sRelationStmts.add("attribute["+routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION+"]");
	            sRelationStmts.add("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_ACTION+"]");
	            sRelationStmts.add("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS+"]");
	            sRelationStmts.add("attribute["+routeTemplateObj.ATTRIBUTE_TITLE+"]");
	            sRelationStmts.add("attribute["+DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE+"]");
	            sRelationStmts.add("attribute["+DomainObject.ATTRIBUTE_DUEDATE_OFFSET+"]");
	            sRelationStmts.add("attribute["+DomainObject.ATTRIBUTE_DATE_OFFSET_FROM+"]");
	            sRelationStmts.add("attribute["+DomainObject.ATTRIBUTE_ROUTE_TASK_USER+"]");
	            sRelationStmts.add("attribute["+DomainObject.ATTRIBUTE_ALLOW_DELEGATION +"]");
	            sRelationStmts.add("attribute["+DomainObject.ATTRIBUTE_REVIEW_TASK+"]");
	            sRelationStmts.add("attribute[Choose Users From User Group]");
	            sRelationStmts.add("attribute["+ATTRIBUTE_UserGroupAction+"]");
	            sRelationStmts.add("attribute["+ATTRIBUTE_UserGroupLevelInfo+"]");
	            //Added for Bug 309518 - Begin
	            sRelationStmts.add("attribute[Parallel Node Procession Rule]");
	            //Added for Bug 309518 - End
          		 String proxyGoupType = PropertyUtil.getSchemaProperty(context,"type_GroupProxy");
          		String strKindOfProxyGroup = "type.kindof["+ proxyGoupType +"]";
	            Pattern relPattern = new Pattern(DomainObject.RELATIONSHIP_ROUTE_NODE);
	            Pattern typePattern = new Pattern(DomainObject.TYPE_PERSON);
	            typePattern.addPattern(DomainObject.TYPE_ROUTE_TASK_USER);
	            typePattern.addPattern(proxyGoupType);
	            selectPersonStmts.add(strKindOfProxyGroup);
	            //Added for Bug 309518 - End
	            MapList mapList = routeTemplateObj.getRelatedObjects(context,
	                                                        relPattern.getPattern(),  //String relPattern
	                                                        typePattern.getPattern(), //String typePattern
	                                                         selectPersonStmts,
	                                                         sRelationStmts,
	                                                         false,
	                                                         true,
	                                                         (short)1,
	                                                         "",
	                                                         "",
	                                                         null,
	                                                         null,
	                                                         null);
	            if(mapList != null && mapList.size()>0) {
	              Iterator mapListItr = mapList.iterator();
	              int routeNodeIds = 0;
	              StringList personList = new StringList();
	              StringList roleList = new StringList();
	              StringList groupList = new StringList();
	              StringList userGroupList = new StringList();
	              DomainObject personObj = DomainObject.newInstance(context);
	              BusinessObject memberObject = null;
	              while(mapListItr.hasNext()) {
	                Map tempMap = (Map)mapListItr.next();
	                String sPersonId = (String)tempMap.get(routeTemplateObj.SELECT_ID);
	                String sPersonType = (String)tempMap.get(routeTemplateObj.SELECT_TYPE);
	                String isKindOfProxy = (String)tempMap.get(strKindOfProxyGroup);
	                String sFirstName = "";
	                String sLastName  = "";
	                String organizationName = "";
	                String organizationTitle = "";
	                if(sPersonType.equals(DomainConstants.TYPE_PERSON) || "true".equalsIgnoreCase(isKindOfProxy))
	                {
	                  personObj.setId(sPersonId);
	                  if(isScopeWorkspace && !workspaceGrantees.contains(tempMap.get(DomainConstants.SELECT_NAME))){
	                		  continue;
	                  }
	                  personObj.open(context);
	                  String personName = personObj.getName(context);
	                  //This condition is added by daks
	                  if(!strProjectId.equals("null")&& !strProjectId.equals("")){
	                    memberObject = JSPUtil.getProjectMember(context,  session , strProjectId, personObj);
	                    if(memberObject != null) {
	                      sFirstName = (String)tempMap.get("attribute["+DomainConstants.ATTRIBUTE_FIRST_NAME+"]");
	                      sLastName  = (String)tempMap.get("attribute["+DomainConstants.ATTRIBUTE_LAST_NAME+"]");
	                      if(!personList.contains(sPersonId)) {
	                        String sProjectLead = JSPUtil.getAttribute(context, session,memberObject,DomainObject.ATTRIBUTE_PROJECT_ACCESS);
	                        String sCreateRoute = JSPUtil.getAttribute(context, session,memberObject,PropertyUtil.getSchemaProperty(context, "attribute_CreateRoute"));
	                        HashMap tempHashMap = new HashMap();
	                        tempHashMap.put(DomainObject.SELECT_ID,sPersonId);
	                        if("true".equalsIgnoreCase(isKindOfProxy)){
	                        	tempHashMap.put("LastFirstName",(String)personObj.getInfo(context,DomainConstants.SELECT_ATTRIBUTE_TITLE));
	                        	tempHashMap.put("projectLead","");
	                        	tempHashMap.put("createRoute","");
	                        	tempHashMap.put("OrganizationName", "");
	                        	tempHashMap.put("OrganizationTitle", "");
	                        }else {
		                        tempHashMap.put("LastFirstName",sLastName+", "+sFirstName);
		                        tempHashMap.put("projectLead",sProjectLead);
		                        tempHashMap.put("createRoute",sCreateRoute);
		                        tempHashMap.put("OrganizationName", organizationName);
		                        tempHashMap.put("OrganizationTitle", organizationTitle);
	                        }
	                        tempHashMap.put(DomainConstants.SELECT_NAME,personName);
	                        tempHashMap.put(DomainObject.SELECT_TYPE,sPersonType);       
							tempHashMap.put("access",getAccessForPerson(domainAccessSummaryList, personName));                      	                                       
	                        tempHashMap.put("fromRouteTemplate","Yes");
	                        memberMapList.add((Map)tempHashMap);
	                      }
	                      //For the 4the Step
	                      HashMap taskHashMap = new HashMap();
	                      taskHashMap.put("PersonId",sPersonId);
	                      if("true".equalsIgnoreCase(isKindOfProxy)){
	                      	taskHashMap.put("PersonName",personName);
	                      }else {
	                      taskHashMap.put("PersonName",sLastName+", "+sFirstName);
	                      }
	                      taskHashMap.put(DomainConstants.SELECT_NAME,personName);
	                      taskHashMap.put("templateFlag","Yes");
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_ACTION ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_ACTION+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_TITLE ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_TITLE+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_SCHEDULED_COMPLETION_DATE,"");
	                      taskHashMap.put(DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE+"]"));
	                      taskHashMap.put(DomainObject.ATTRIBUTE_DUEDATE_OFFSET,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_DUEDATE_OFFSET+"]"));
	                      taskHashMap.put(DomainObject.ATTRIBUTE_DATE_OFFSET_FROM,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_DATE_OFFSET_FROM+"]"));
	                      taskHashMap.put(routeTemplateObj.RELATIONSHIP_ROUTE_NODE,String.valueOf(routeNodeIds));
	            taskHashMap.put(DomainObject.ATTRIBUTE_REVIEW_TASK,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_REVIEW_TASK+"]"));
	              taskHashMap.put(DomainObject.ATTRIBUTE_ALLOW_DELEGATION,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_ALLOW_DELEGATION+"]"));
	              		  taskHashMap.put(ATTRIBUTE_UserGroupAction,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupAction+"]"));
	              		  taskHashMap.put(ATTRIBUTE_UserGroupLevelInfo,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupLevelInfo+"]"));
	
	                      taskMapList.add((Map)taskHashMap);
	                      routeNodeIds++;
	                      personList.add(sPersonId);
	                  }
	                  } else { //If no Project Id exists, Means Route is created independently
	                    sFirstName = (String)tempMap.get("attribute["+DomainConstants.ATTRIBUTE_FIRST_NAME+"]");
	                    sLastName  = (String)tempMap.get("attribute["+DomainConstants.ATTRIBUTE_LAST_NAME+"]");
	                    if("true".equalsIgnoreCase(isKindOfProxy) && !userGroupList.contains(sPersonId)){
	                    	HashMap tempHashMap = new HashMap();
                            tempHashMap.put(DomainObject.SELECT_ID,sPersonId);
                            tempHashMap.put("LastFirstName",(String)personObj.getInfo(context,DomainConstants.SELECT_ATTRIBUTE_TITLE));
                            tempHashMap.put(DomainConstants.SELECT_NAME , personName);
                            tempHashMap.put(DomainObject.SELECT_TYPE,"User Group");
                            tempHashMap.put("OrganizationName","");
                            tempHashMap.put("OrganizationTitle","");
                            tempHashMap.put("access","Read");
                            tempHashMap.put("fromRouteTemplate","Yes");
                            memberMapList.add((Map)tempHashMap);
                            userGroupMapList.add((Map)tempHashMap);
                            userGroupList.add(sPersonId);
                            
	                    }else if(sPersonType.equals(DomainConstants.TYPE_PERSON) && !personList.contains(sPersonId)) {
	                      HashMap tempHashMap = new HashMap();
	                      tempHashMap.put(DomainObject.SELECT_ID,sPersonId);
	                      tempHashMap.put("LastFirstName",sLastName+", "+sFirstName);
	                      tempHashMap.put(DomainConstants.SELECT_NAME,personName);
	                      tempHashMap.put(DomainObject.SELECT_TYPE,sPersonType);
	                      organizationName = (String)tempMap.get(selOrg);
	                      organizationTitle = (String)tempMap.get(selOrgTitle);
	                      organizationName = (organizationName == null || "null".equals(organizationName)) ? "" : organizationName;
	                      organizationTitle = (organizationTitle == null || "null".equals(organizationTitle)) ? "" : organizationTitle;
	                      tempHashMap.put("OrganizationName", organizationName);
	                      tempHashMap.put("OrganizationTitle", organizationTitle);
	                      // Added for bug 376886
	                      try
	                      {
	                      Access access = routeTemplateObj.getAccessForGranteeGrantor(context,personName, AccessUtil.ROUTE_ACCESS_GRANTOR);
	                      if (AccessUtil.hasAddRemoveAccess(access))
	                          tempHashMap.put("access","Add Remove");
	                      else if(AccessUtil.hasRemoveAccess(access))
	                          tempHashMap.put("access","Remove");
	                      else if(AccessUtil.hasAddAccess(access))
	                          tempHashMap.put("access","Add");
	                      else if(AccessUtil.hasReadWriteAccess(access))
	                          tempHashMap.put("access", "Read Write");
	                      else
	                          tempHashMap.put("access","Read");
	                      }
	                      catch(MatrixException e)
	                      {
	                          tempHashMap.put("access",getAccessForPerson(domainAccessSummaryList, personName));
	                      }
	                      // Ended                      
	                      
	                      tempHashMap.put("fromRouteTemplate","Yes");
	                      tempHashMap.put(ATTRIBUTE_UserGroupAction,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupAction+"]"));
	                      tempHashMap.put(ATTRIBUTE_UserGroupLevelInfo,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupLevelInfo+"]"));
	                      memberMapList.add((Map)tempHashMap);
	                      personList.add(sPersonId);
	                    }
	                    //For the 4the Step
	                    HashMap taskHashMap = new HashMap();
	                    taskHashMap.put("PersonId",sPersonId);
	                    if("true".equalsIgnoreCase(isKindOfProxy)){
	                    	taskHashMap.put("PersonName",personName);
	                    }else {
	                    taskHashMap.put("PersonName",sLastName+", "+sFirstName);
	                    }
	                    taskHashMap.put(DomainConstants.SELECT_NAME,personName);
	                    taskHashMap.put("templateFlag","Yes");
	                    taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE+"]"));
	                    taskHashMap.put(routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION+"]"));
	                    taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_ACTION ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_ACTION+"]"));
	                    taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS+"]"));
	                    taskHashMap.put(routeTemplateObj.ATTRIBUTE_TITLE ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_TITLE+"]"));
	                    taskHashMap.put(routeTemplateObj.ATTRIBUTE_SCHEDULED_COMPLETION_DATE,"");
	                    taskHashMap.put(DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE+"]"));
	                    taskHashMap.put(DomainObject.ATTRIBUTE_DUEDATE_OFFSET,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_DUEDATE_OFFSET+"]"));
	                    taskHashMap.put(DomainObject.ATTRIBUTE_DATE_OFFSET_FROM,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_DATE_OFFSET_FROM+"]"));
	          			taskHashMap.put(DomainObject.ATTRIBUTE_REVIEW_TASK,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_REVIEW_TASK+"]"));
	            		taskHashMap.put(DomainObject.ATTRIBUTE_ALLOW_DELEGATION,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_ALLOW_DELEGATION+"]"));
	            		taskHashMap.put("Choose Users From User Group",(String)tempMap.get("attribute[Choose Users From User Group]"));
	              		taskHashMap.put(ATTRIBUTE_UserGroupAction,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupAction+"]"));
	              		taskHashMap.put(ATTRIBUTE_UserGroupLevelInfo,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupLevelInfo+"]"));
	                    taskHashMap.put(routeTemplateObj.RELATIONSHIP_ROUTE_NODE,String.valueOf(routeNodeIds));
	                    taskMapList.add((Map)taskHashMap);
	                    routeNodeIds++;
	                 
	                 }//if no project
	
	              }  else {
	            	  String rTaskUser  = (String)tempMap.get("attribute["+DomainConstants.ATTRIBUTE_ROUTE_TASK_USER+"]");
	                  if(UIUtil.isNotNullAndNotEmpty(rTaskUser)){
	                	  String isRoleGroup = "";
	                	  String sRoleName = "";
	                	  if(rTaskUser.indexOf("_") > -1 ){
	                       isRoleGroup = rTaskUser.substring(0,rTaskUser.indexOf("_"));
	                       sRoleName = PropertyUtil.getSchemaProperty(context,rTaskUser);
	                	  }
	                      //modified for 311950
	                      // String sRoleName = PropertyUtil.getSchemaProperty(context,rTaskUser);
	                      
	                      if(isScopeWorkspace && !workspaceGrantees.contains(sRoleName)){
		                      continue;
		                  }
	                      // till here
	                      HashMap taskHashMap = new HashMap();
	                      if(isRoleGroup.equals("role")){
	                          if(!roleList.contains(sRoleName)){
	                            HashMap tempHashMap = new HashMap();
	                            tempHashMap.put(DomainObject.SELECT_ID,"Role");
	                            tempHashMap.put("LastFirstName",sRoleName);
	                            tempHashMap.put(DomainConstants.SELECT_NAME , sRoleName);
	                            tempHashMap.put(DomainObject.SELECT_TYPE,"Role");
	                            tempHashMap.put("OrganizationName","");
	                            tempHashMap.put("OrganizationTitle","");
	                            tempHashMap.put("access","Read");
	                            tempHashMap.put("fromRouteTemplate","Yes");
	                            memberMapList.add((Map)tempHashMap);
	                            roleMapList.add((Map)tempHashMap);
	                            roleList.add(sRoleName);
	                           }
	                          taskHashMap.put("PersonId","Role");
	                     }else if(isRoleGroup.equals("group")){
	                          if(!groupList.contains(sRoleName)){
	                            HashMap tempHashMap = new HashMap();
	                            tempHashMap.put(DomainObject.SELECT_ID,"Group");
	                            tempHashMap.put("LastFirstName",sRoleName);
	                            tempHashMap.put(DomainConstants.SELECT_NAME , sRoleName);
	                            tempHashMap.put(DomainObject.SELECT_TYPE,"Group");
	                            tempHashMap.put("OrganizationName","");
	                            tempHashMap.put("OrganizationTitle","");
	                            tempHashMap.put("access","Read");
	                            tempHashMap.put("fromRouteTemplate","Yes");
	                            memberMapList.add((Map)tempHashMap);
	                            groupMapList.add((Map)tempHashMap);
	                            groupList.add(sRoleName);
	                            }
	                          taskHashMap.put("PersonId","Group");
	                     }
	
	                      taskHashMap.put("PersonName",sRoleName);
	                      taskHashMap.put(DomainConstants.SELECT_NAME,sRoleName);
	                      taskHashMap.put("templateFlag","Yes");
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_ACTION ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_ACTION+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_TITLE ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_TITLE+"]"));
	                      taskHashMap.put(routeTemplateObj.ATTRIBUTE_SCHEDULED_COMPLETION_DATE,"");
	                      taskHashMap.put(DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE+"]"));
	                      taskHashMap.put(DomainObject.ATTRIBUTE_DUEDATE_OFFSET,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_DUEDATE_OFFSET+"]"));
	                      taskHashMap.put(DomainObject.ATTRIBUTE_DATE_OFFSET_FROM,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_DATE_OFFSET_FROM+"]"));
	           			  taskHashMap.put(DomainObject.ATTRIBUTE_REVIEW_TASK,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_REVIEW_TASK+"]"));
	                      taskHashMap.put(DomainObject.ATTRIBUTE_ALLOW_DELEGATION,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_ALLOW_DELEGATION+"]"));
	              		  taskHashMap.put(ATTRIBUTE_UserGroupAction,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupAction+"]"));
	              		  taskHashMap.put(ATTRIBUTE_UserGroupLevelInfo,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupLevelInfo+"]"));
	                      taskHashMap.put(routeTemplateObj.RELATIONSHIP_ROUTE_NODE,String.valueOf(routeNodeIds));
	                      taskMapList.add((Map)taskHashMap);
	                      routeNodeIds++;
	                      personList.add(sPersonId);
	
	                }else{
	
	                        HashMap taskHashMap = new HashMap();
	                        taskHashMap.put("PersonId","none");
	                        taskHashMap.put("PersonName","none");
	                        taskHashMap.put(DomainConstants.SELECT_NAME,"");
	                        taskHashMap.put("templateFlag","Yes");
	                        taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE+"]"));
	                        taskHashMap.put(routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ALLOW_DELEGATION+"]"));
	                        taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_ACTION ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_ACTION+"]"));
	                        taskHashMap.put(routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_INSTRUCTIONS+"]"));
	                        taskHashMap.put(routeTemplateObj.ATTRIBUTE_TITLE ,(String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_TITLE+"]"));
	                        taskHashMap.put(routeTemplateObj.ATTRIBUTE_SCHEDULED_COMPLETION_DATE,"");
	                        taskHashMap.put(DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_ASSIGNEE_SET_DUEDATE+"]"));
	                        taskHashMap.put(DomainObject.ATTRIBUTE_DUEDATE_OFFSET,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_DUEDATE_OFFSET+"]"));
	                        taskHashMap.put(DomainObject.ATTRIBUTE_DATE_OFFSET_FROM,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_DATE_OFFSET_FROM+"]"));
	           				taskHashMap.put(DomainObject.ATTRIBUTE_REVIEW_TASK,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_REVIEW_TASK+"]"));
	               			taskHashMap.put(DomainObject.ATTRIBUTE_ALLOW_DELEGATION,(String)tempMap.get("attribute["+DomainObject.ATTRIBUTE_ALLOW_DELEGATION+"]"));
            		 		taskHashMap.put(ATTRIBUTE_UserGroupAction,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupAction+"]"));
            				taskHashMap.put(ATTRIBUTE_UserGroupLevelInfo,(String)tempMap.get("attribute["+ATTRIBUTE_UserGroupLevelInfo+"]"));
	                        taskHashMap.put(routeTemplateObj.RELATIONSHIP_ROUTE_NODE,String.valueOf(routeNodeIds));
	                        taskMapList.add((Map)taskHashMap);
	                        routeNodeIds++;
	                        personList.add(sPersonId);
	                }
	              }
	
	                   personObj.close(context);
	                   //Added for Bug 309518 - Begin
	                   actionRequiredMap.put((String)tempMap.get("attribute["+routeTemplateObj.ATTRIBUTE_ROUTE_SEQUENCE+"]"),(String)tempMap.get("attribute[Parallel Node Procession Rule]") );
	                   //Added for Bug 309518 - End
	            }
	          }
	        formBean.setElementValue("routeMemberMapList",memberMapList);
	        formBean.setElementValue("routeRoleMapList",roleMapList);
	        formBean.setElementValue("routeGroupMapList",groupMapList);
	        formBean.setElementValue("taskMapList",taskMapList);
// 	        Added for Bug 309518 - Begin
	        formBean.setElementValue("actionRequiredMap",actionRequiredMap);
	        //Added for Bug 309518 - End
	        }
	
	    }else{
	    	 //retain previous list, if the route template is not changed between navigations
		     if(prevRouteMembersMapList != null && prevRouteMembersMapList.size() > 0){
		    	 emptyList = false;
		     }
	    }
	
		 
 	 }
      if(prevRouteMembersMapList != null && prevRouteMembersMapList.size() > 0 && !emptyList){
   	      formBean.setElementValue("routeMemberMapList", prevRouteMembersMapList);
   	      formBean.setElementValue("routeRoleMapList",prevRouteRoleMapList);
 	      formBean.setElementValue("routeGroupMapList",prevRouteGroupMapList);
 	      formBean.setElementValue("taskMapList",prevTaskMapList);
      }else{
    	  formBean.setElementValue("routeMemberMapList",memberMapList);
          formBean.setElementValue("routeRoleMapList",roleMapList);
          formBean.setElementValue("routeGroupMapList",groupMapList);
          formBean.setElementValue("taskMapList",taskMapList);
          //Added for Bug 309518 - Begin
          formBean.setElementValue("actionRequiredMap",actionRequiredMap);
      }
      formBean.setElementValue("hashRouteWizFirst",hashRouteWizFirst);
	  formBean.setFormValues(session);
 }//ROUTE OBJECT doesn't EXISTS
 



%>

<%!
	public static String getAccessForPerson ( MapList domainAccessSummaryList, String personName)
	{
		String sAccess = "Read";
		try
		{
			for(int i = 0 ; i < domainAccessSummaryList.size(); i++){
				   HashMap m = (HashMap)domainAccessSummaryList.get(i);
				   if((personName+"_PRJ").equals(((String)m.get("name")))){
					   sAccess = (String)m.get("access");
		    	   }
		    }
			if("Full".equalsIgnoreCase(sAccess)){
			  sAccess = "Add Remove";
			}
		}
		catch(Exception e)
		{
			
		}
		return sAccess;
	}
%>

<html>
<body>
<form name="newForm" target="_parent">
  <input type="hidden" name="objectId"     value="<xss:encodeForHTMLAttribute><%=relatedObjectId%></xss:encodeForHTMLAttribute>"/>
  <input type="hidden" name="routeId"      value="<xss:encodeForHTMLAttribute><%=routeId%></xss:encodeForHTMLAttribute>"/>
  <input type="hidden" name="templateId"   value="<xss:encodeForHTMLAttribute><%=templateId%></xss:encodeForHTMLAttribute>"/>
  <input type="hidden" name="checkPreserveOwner"   value="<xss:encodeForHTMLAttribute><%=checkPreserveOwner%></xss:encodeForHTMLAttribute>"/>
  <input type="hidden" name="scopeId"      value="<xss:encodeForHTMLAttribute><%=scopeId%></xss:encodeForHTMLAttribute>"/>
  <input type="hidden" name="templateName" value="<xss:encodeForHTMLAttribute><%=templateName%></xss:encodeForHTMLAttribute>"/>
  <input type="hidden" name="selectedAction" value="<xss:encodeForHTMLAttribute><%=selectedAction%></xss:encodeForHTMLAttribute>"/>
  <input type="hidden" name="supplierOrgId"  value="<xss:encodeForHTMLAttribute><%=supplierOrgId%></xss:encodeForHTMLAttribute>"/>
  <input type="hidden" name="suiteKey" value="<xss:encodeForHTMLAttribute><%=suiteKey%></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="portalMode" value="<xss:encodeForHTMLAttribute><%=portalMode%></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="keyValue" value="<xss:encodeForHTMLAttribute><%=keyValue%></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="showmenuinToolbar" value="<xss:encodeForHTMLAttribute><%=showmenuinToolbar%></xss:encodeForHTMLAttribute>" />
</form>
<script language="javascript">
<%
    if(!bExecute) {

%>
      document.newForm.action = "emxRouteWizardAccessMembersFS.jsp?keyValue=<%=XSSUtil.encodeForURL(context, keyValue)%>";
<%
    }  else {

%>
       document.newForm.action = "emxRouteWizardCreateDialogFS.jsp?keyValue=<%=XSSUtil.encodeForURL(context, keyValue)%>";
<%
    }
%>
  document.newForm.submit();
</script>
</body>
</html>

