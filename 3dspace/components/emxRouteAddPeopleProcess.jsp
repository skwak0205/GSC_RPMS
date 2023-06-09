<%--  emxRouteAddPeopleProcess.jsp   -  Creating the WorkSpace Object
   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of MatrixOne,
   Inc.  Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

   static const char RCSID[] = $Id: emxRouteAddPeopleProcess.jsp.rca 1.17 Wed Oct 22 16:17:41 2008 przemek Experimental przemek $
--%>

<%@page import="com.matrixone.apps.domain.DomainConstants"%>
<%@include file = "../emxUICommonAppInclude.inc"%>
<%@ include file = "emxRouteInclude.inc" %>
<%@include file = "../emxTagLibInclude.inc"%>
<%@include file = "../common/enoviaCSRFTokenValidation.inc"%>
<script language="javascript" src="../common/scripts/emxJSValidationUtil.js"></script>
<script src="../common/scripts/emxUICore.js" type="text/javascript"></script>
<script language="JavaScript" src="emxRouteSimpleFunctions.js" type="text/javascript"></script>
<%@ page import = "matrix.db.*, com.matrixone.apps.common.util.ComponentsUIUtil,com.matrixone.apps.domain.util.i18nNow" %>

<jsp:useBean id="formBean" scope="session" class="com.matrixone.apps.common.util.FormBean"/>

<%
  //added fro 307166
  String jsTreeID           = emxGetParameter(request,"jsTreeID");
  //till here 307166

  String keyValue=emxGetParameter(request,"keyValue");
  String memberType     = emxGetParameter(request, "memberType");
  // added for the bug 297703
  boolean flag = false;
  //till here 297703
  String fromPage     = emxGetParameter(request, "fromPage");
  fromPage = fromPage == null ? "" : fromPage;
  String routeMemberString="";
  String routeMemberType = "";
  String sUser = context.getUser();
  boolean isRevised = false;
  //Added for Bug IR-009799
  String memberIDs     = emxGetParameter(request, "memberIds");
  String[] memberRowIDs     = ComponentsUIUtil.getSplitTableRowIds(emxGetParameterValues(request, "emxTableRowId"));
  String[] memberID = (memberIDs != null) ? ((String[])(FrameworkUtil.split(memberIDs, ";")).toArray(new String []{})) : memberRowIDs;
  boolean showSelectItemAlert = false;
  String strpopUpURL = null;
  String memberNames          = "";
  if(memberID == null || memberID.length == 0) {
      showSelectItemAlert = true;
  } else {
      String sAttrProjectAccess   =  PropertyUtil.getSchemaProperty(context, "attribute_ProjectAccess");
      String sProjectId           = "";
      String typeProject          = PropertyUtil.getSchemaProperty(context, "type_Project");
      String attrTaskEditSetting  = PropertyUtil.getSchemaProperty(context, "attribute_TaskEditSetting");
      String attrTemplateTask               = PropertyUtil.getSchemaProperty(context, "attribute_TemplateTask");
      int i = 0;
      int j = 0;
      boolean allowExtend         = true;

      StringList roleList  = new StringList();
      StringList groupList  = new StringList();
      StringList userGroupList  = new StringList();

      String sRouteId  = "";
      if(keyValue == null || keyValue.equals("")) {
    	  sRouteId = emxGetParameter(request, "routeId");   
    	  sRouteId = sRouteId != null ? sRouteId : emxGetParameter(request, "objectId");
      }

      //if page is called from Access node of Route or Route Template
      if(sRouteId != null && (!sRouteId.equals(""))) {
          BusinessObject doObj1 = new BusinessObject(sRouteId);
          doObj1.open(context);

          if(doObj1.getTypeName().equals(DomainObject.TYPE_ROUTE_TEMPLATE)) {
          		// Set the domain object as RouteTemplate to connect persons.
               RouteTemplate routeObject = (RouteTemplate)DomainObject.newInstance(context, sRouteId);
               String currentState = (String)routeObject.getInfo(context,routeObject.SELECT_CURRENT);
               if(currentState.equals(DomainObject.STATE_ROUTE_TEMPLATE_ACTIVE)) {
                    isRevised = true;
                    String revisedTemplateId = routeObject.revise(context);
                    routeObject.setId(revisedTemplateId);
                    sRouteId = revisedTemplateId;
                }
                try{
                	Route.addMembersToRoute(context, session, request,memberType,null, routeObject,doObj1.getTypeName(),memberID,sRouteId);
             	} catch(Exception ex) { //added for 297703
    				// Modified the alert Message to display properly for bug 363610
    %>
    	            <script language="javascript">
    	            	alert("<%=ex.getMessage()%>");
    	            </script>
    <%
                	//end for 297703    
               }            
    	   } else {
            	// Set the domain object as Route to connect persons.
             	Route routeObject = (Route)DomainObject.newInstance(context, sRouteId);
                try {
                	Route.addMembersToRoute(context,session, request,memberType, routeObject,null,doObj1.getTypeName(),memberID,sRouteId);
                } catch(Exception ex) { //added for 297703
    				// Modified the alert Message to display properly for bug 363610
    %>
                    <script language="javascript">
                    	alert("<%=ex.getMessage()%>");
                    </script>
    <%
    				//end for 297703
               }
            }
            doObj1.close(context);
    	} else { //if page is called from Route or Route Template wizard
      		DomainObject personObject= null;
      		int routeNodeIds           = 0;
      		int tempRouteNodeIds       = 0;
    		// added on 23 jan for bug 281010
            String templateId  =  (String) formBean.getElementValue("templateId");
            DomainObject routeTemplateObj  = null;
            String strTaskEditSetting   = "";

            if(templateId != null && !"".equals(templateId) && !templateId.equals("null")) {
            	routeTemplateObj = DomainObject.newInstance(context ,templateId);
                strTaskEditSetting = routeTemplateObj.getAttributeValue(context,attrTaskEditSetting);
    		}
            if(strTaskEditSetting != null && strTaskEditSetting.trim().equals("Maintain Exact Task List")){
            	allowExtend = false;
    		}
    		//till here
      		MapList memberMapList = new MapList();
      		try {
         		memberMapList = (MapList)formBean.getElementValue("routeMemberMapList");
       		} catch(Exception ect){}
      		if(memberMapList == null || memberMapList.equals("null") || memberMapList.equals("")) {    
      			memberMapList = new MapList();  
      		}
      		MapList taskMapList = new MapList();
      		try {
         		taskMapList = (MapList)formBean.getElementValue("taskMapList");
      		} catch(Exception tml){}
      		if(taskMapList == null) {
      			taskMapList = new MapList();
      		}
    		// added for the bug 337561
    		routeNodeIds=taskMapList.size();
    		//end 337561
      			StringList existingList = new StringList();
      			if(memberMapList!=null &&  memberMapList.size() > 0) {
        			Iterator memberMapListItr = memberMapList.iterator();
        			Map memIdmap = null;
        			while(memberMapListItr.hasNext()) {
                  		memIdmap = (Map)memberMapListItr.next();
                  		if("Role".equalsIgnoreCase((String)memIdmap.get(DomainConstants.SELECT_ID)) ||
                  		      "Group".equalsIgnoreCase((String)memIdmap.get(DomainConstants.SELECT_ID))){
                  		  existingList.addElement((String)memIdmap.get(DomainConstants.SELECT_NAME));
                  		}else{
            			existingList.addElement((String)memIdmap.get(DomainConstants.SELECT_ID));
                  		}
        			}
      			}
      		if( memberID != null && (DomainConstants.TYPE_PERSON.equalsIgnoreCase(memberType)) ) {
        		for(int count =0; count < memberID.length; count++ ) {
            		String personTypeId     = "";
              		personTypeId = memberID[count];
          			personObject = DomainObject.newInstance(context , personTypeId);
          			String sCreateRoute = PropertyUtil.getSchemaProperty(context, "attribute_CreateRoute");
          			StringList perSelects = new StringList();
          			perSelects.add("to["+personObject.RELATIONSHIP_EMPLOYEE+"].from.name");
          			perSelects.add("to["+personObject.RELATIONSHIP_EMPLOYEE+"].from.attribute[Title]");
          			perSelects.add("attribute["+personObject.ATTRIBUTE_FIRST_NAME +"]");
          			perSelects.add("attribute["+personObject.ATTRIBUTE_LAST_NAME +"]");
          			
          			Map perMap = personObject.getInfo(context, perSelects);
          			
          			String sOrgName = (String)perMap.get("to["+personObject.RELATIONSHIP_EMPLOYEE+"].from.name");
          			String sOrgTitle = (String)perMap.get("to["+personObject.RELATIONSHIP_EMPLOYEE+"].from.attribute[Title]");
          			String sFirstName = (String)perMap.get("attribute["+personObject.ATTRIBUTE_FIRST_NAME +"]");
          			String sLastName = (String)perMap.get("attribute["+personObject.ATTRIBUTE_LAST_NAME +"]");
          			String userName = personObject.getName();

          			HashMap tempMap = new HashMap();
          			tempMap.put(personObject.SELECT_ID,personTypeId);
          			tempMap.put(personObject.SELECT_NAME, userName);
          			tempMap.put("LastFirstName",sLastName+", "+sFirstName);
          			tempMap.put(personObject.SELECT_TYPE,personObject.getType(context));
          			tempMap.put("projectLead","");
          			tempMap.put("createRoute",sCreateRoute);
          			sOrgName = (sOrgName == null || "null".equals(sOrgName)) ? "" : sOrgName;
          			tempMap.put("OrganizationName",sOrgName);
          			tempMap.put("OrganizationTitle",sOrgTitle);
          			if(userName.equals(sUser)){
            			tempMap.put("access","Add Remove");
            		}else{
                    	tempMap.put("access","Read");
            		}
          			          			
          			boolean updatedMapList = false;
          			if(!existingList.contains(personTypeId)){
              			memberMapList.add((Map)tempMap);

          			Iterator mapItr = taskMapList.iterator();
          			while(mapItr.hasNext()){
            			HashMap taskMap = (HashMap)mapItr.next();
            			if("none".equals((String)taskMap.get("PersonId"))){
              				taskMap.put("PersonId",personTypeId);
              				taskMap.put("PersonName",sLastName+", "+sFirstName);
              				taskMap.put(DomainConstants.SELECT_NAME,userName);
              				taskMap.put("memberType",memberType);
              				updatedMapList = true;
              				i = i + 1;
              				break;
            			}
          			}

          			//For the 4th Step
          			if(!updatedMapList && allowExtend){
            			HashMap tempHashMap = new HashMap();
            			tempHashMap.put("PersonId",personTypeId);
            			tempHashMap.put(personObject.SELECT_NAME, userName);
            			tempHashMap.put("PersonName",sLastName+", "+sFirstName);
            			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_SEQUENCE,"");
            			tempHashMap.put(personObject.ATTRIBUTE_ALLOW_DELEGATION,"");
            			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_ACTION ,"");
            			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_INSTRUCTIONS ,"");
            			tempHashMap.put(personObject.ATTRIBUTE_TITLE ,"");
            			tempHashMap.put(personObject.ATTRIBUTE_SCHEDULED_COMPLETION_DATE,"");
    					//replaced ++routeNodeIds with routeNodeIds 337561
                  		tempHashMap.put(personObject.RELATIONSHIP_ROUTE_NODE,String.valueOf(routeNodeIds));
                  		tempHashMap.put("memberType",memberType);
                  		taskMapList.add((Map)tempHashMap);
    					//uncommented for 337561
              			routeNodeIds++;
          			}
          			else {
            			if((!updatedMapList) && (!allowExtend)){
              				j = j + 1;
              				memberNames = memberNames + sFirstName + " " + sLastName + ", " ;
            			}
          			}
        		}
      		}
    		}
     		if(memberType.equalsIgnoreCase("Role")) {
         		String strRoles = emxGetParameter(request, "roleList");
         		strRoles = (strRoles != null) ? strRoles : ComponentsUIUtil.arrayToString(memberRowIDs, ";");
         		if(strRoles != null) {
         		    roleList = FrameworkUtil.split(strRoles,";");
         		}
         		boolean addRoles = false;
         		MapList roleMapList = new MapList();
         		try {
                	roleMapList =(MapList)formBean.getElementValue("routeRoleMapList");
              	} catch(Exception rml){}

          		if(roleMapList == null) {
          		    roleMapList = new MapList();
          		}
          		MapList tempRoleMapList = new MapList();
          		Iterator roleListItr = roleList.iterator();
          		while(roleListItr.hasNext()){
          		    String roleName = roleListItr.next().toString();
          		    if(existingList.contains(roleName)){
          		      roleListItr.remove();
          		    }
          		}
          		if(roleList.size() > 0) {
             		tempRoleMapList = getRolesorGroups(roleList, memberType,taskMapList );
             		memberMapList.addAll(tempRoleMapList);
             		roleMapList.addAll(tempRoleMapList);
           		}
           		formBean.setElementValue("routeRoleMapList",roleMapList);//23 dec
     		}
    		if("UserGroup".equalsIgnoreCase(memberType)) {
    			String strUserGroups = emxGetParameter(request, "userGroupList");
    			strUserGroups = (strUserGroups != null) ? strUserGroups : ComponentsUIUtil.arrayToString(memberRowIDs, ";");
         		if(strUserGroups != null) {
         			userGroupList = FrameworkUtil.split(strUserGroups,";");
         		}
         		boolean addUserGroup = false;
         		MapList userGroupMapList = new MapList();
         		try {
         			userGroupMapList =(MapList)formBean.getElementValue("userGroupMapList");
              	} catch(Exception rml){}

          		if(userGroupMapList == null) {
          			userGroupMapList = new MapList();
          		}
          		MapList tempUserGroupMapList = new MapList();
          		Iterator userGroupListItr = userGroupList.iterator();
          		while(userGroupListItr.hasNext()){
          		    String userGroupName = userGroupListItr.next().toString();
          		    if(existingList.contains(userGroupName)){
          		    	userGroupListItr.remove();
          		    }
          		}
          		for(int count =0; count < userGroupList.size(); count++ ) {
                		String userGroupTypeId     = "";
                		userGroupTypeId = userGroupList.get(count);
              			DomainObject userGroupObject = new DomainObject(userGroupTypeId);
              			String sCreateRoute = PropertyUtil.getSchemaProperty(context, "attribute_CreateRoute");
              			StringList selects = new StringList(2);
              			selects.add(DomainConstants.SELECT_NAME);
              			selects.add(DomainConstants.SELECT_ATTRIBUTE_TITLE);
              			Map details = userGroupObject.getInfo(context,selects);
              			
              			String objectName = (String)details.get(DomainConstants.SELECT_NAME);
              			HashMap tempMap = new HashMap();
              			tempMap.put(personObject.SELECT_ID,userGroupTypeId);
              			tempMap.put(personObject.SELECT_NAME, objectName);
              			tempMap.put(personObject.SELECT_TYPE,"User Group");
              			tempMap.put("projectLead","");
              			tempMap.put("createRoute",sCreateRoute);
              			tempMap.put("OrganizationName","");
              			tempMap.put("OrganizationTitle","");
  						tempMap.put("access","Read");	   
  						tempMap.put("LastFirstName",  (String)details.get(DomainConstants.SELECT_ATTRIBUTE_TITLE));
              			boolean updatedMapList = false;
                  		memberMapList.add((Map)tempMap);
                  		userGroupMapList.add((Map)tempMap);
              			Iterator mapItr = taskMapList.iterator();
              			while(mapItr.hasNext()){
                			HashMap taskMap = (HashMap)mapItr.next();
                			if("none".equals((String)taskMap.get("PersonId"))){
                  				taskMap.put("PersonId",userGroupTypeId);
                  				taskMap.put("PersonName",objectName);
                  				taskMap.put(DomainConstants.SELECT_NAME,objectName);
                  				taskMap.put("memberType",memberType);
                  				updatedMapList = true;
                  				i = i + 1;
                  				break;
                			}
              			}

              			//For the 4th Step
              			if(!updatedMapList && allowExtend){
                			HashMap tempHashMap = new HashMap();
                			tempHashMap.put("PersonId",userGroupTypeId);
                			tempHashMap.put("PersonName",objectName);
                			tempHashMap.put(personObject.SELECT_NAME, objectName);
                			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_SEQUENCE,"");
                			tempHashMap.put(personObject.ATTRIBUTE_ALLOW_DELEGATION,"");
                			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_ACTION ,"");
                			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_INSTRUCTIONS ,"");
                			tempHashMap.put(personObject.ATTRIBUTE_TITLE ,"");
                			tempHashMap.put(personObject.ATTRIBUTE_SCHEDULED_COMPLETION_DATE,"");
                      		tempHashMap.put(personObject.RELATIONSHIP_ROUTE_NODE,String.valueOf(routeNodeIds));
                      		tempHashMap.put("memberType",memberType);
                      		taskMapList.add((Map)tempHashMap);
                  			routeNodeIds++;
              			}
            		}        			
           		formBean.setElementValue("routeUserGroupMapList",userGroupMapList);//23 dec
    		}
    		if(memberType.equalsIgnoreCase("Group")) {
          		String strGroups = emxGetParameter(request, "groupList");
        		MapList groupMapList = new MapList();
        		try {
            	groupMapList =(MapList)formBean.getElementValue("routeGroupMapList");
          	} catch(Exception cet){  }
           		groupMapList = addGroupsToRouteWizard(request, strGroups, existingList, taskMapList, memberMapList, groupMapList);
          		formBean.setElementValue("routeGroupMapList",groupMapList);
    		}
    		if("MemberList".equalsIgnoreCase(memberType)) {
        		String personTypeId     = "";
        		StringList strGroupList = new StringList();
        		String memberListID   = emxGetParameter(request,"memberListID");
        		memberListID = (memberListID != null) ? memberListID : ComponentsUIUtil.arrayToString(memberRowIDs, ",");
        		StringList memberListIds = FrameworkUtil.split(memberListID,",");
        		String RELATIONSHIP_LIST_MEMBER = PropertyUtil.getSchemaProperty(context,"relationship_ListMember");
        		//add the properties for output.
        		String strworkPhoneNumber = "";
      			String workPhoneNumber = "";
      			workPhoneNumber = PropertyUtil.getSchemaProperty(context, "attribute_WorkPhoneNumber");
      			strworkPhoneNumber = "attribute["+workPhoneNumber+"]";
      			StringList relSelects = new StringList();
      			short level = 1;

      			StringList objSelects = new StringList();
        		objSelects.add(DomainConstants.SELECT_ID);
        		objSelects.add(DomainConstants.SELECT_NAME);
        		objSelects.add(DomainConstants.SELECT_TYPE);
        		objSelects.add(strworkPhoneNumber);
        		objSelects.add(DomainConstants.SELECT_CURRENT);

        		//similarly create a new string list for holding relationship selectables
        		 Iterator memberListIdsItr = memberListIds.iterator();
                 while(memberListIdsItr.hasNext()){
                     String memberId = memberListIdsItr.next().toString();
        		DomainObject doObj = new DomainObject(memberId);
        		MapList idList = doObj.getRelatedObjects(context, RELATIONSHIP_LIST_MEMBER,DomainConstants.TYPE_PERSON+","+DomainConstants.TYPE_BUSINESS_GROUP, objSelects, relSelects, false, true, level, "", "");

        		MapList tempMembersList = null;
      			Map idmap = null;
      			HashMap tempidMap = null;
        		if(idList != null) {
          			Iterator idListItr = idList.iterator();
          			MapList idMapList = new MapList();
            		tempMembersList = new MapList();

          			while(idListItr.hasNext()) {
                  		idmap = (Map)idListItr.next();
                        String listMemberType = (String)idmap.get(DomainConstants.SELECT_TYPE);
                        if(DomainConstants.TYPE_PERSON.equalsIgnoreCase(listMemberType)){
	                  		tempidMap = new HashMap();
	                  		personTypeId = (String)idmap.get(DomainConstants.SELECT_ID);
	            			if(!existingList.contains(personTypeId)) {
	                  			personObject =   DomainObject.newInstance(context , personTypeId);
	                  			String sCreateRoute         = PropertyUtil.getSchemaProperty(context, "attribute_CreateRoute");
	                  			StringList perSelects = new StringList();
	                  			perSelects.add("to["+personObject.RELATIONSHIP_EMPLOYEE+"].from.name");
	                  			perSelects.add("to["+personObject.RELATIONSHIP_EMPLOYEE+"].from.attribute[Title]");
	                  			perSelects.add("attribute["+personObject.ATTRIBUTE_FIRST_NAME +"]");
	                  			perSelects.add("attribute["+personObject.ATTRIBUTE_LAST_NAME +"]");
	                  			perSelects.add(DomainConstants.SELECT_CURRENT);
	                  			
	                  			Map perMap = personObject.getInfo(context, perSelects);
	                  			String sOrgName             = (String)perMap.get("to["+personObject.RELATIONSHIP_EMPLOYEE+"].from.name");
	                  			String sOrgTitle             = (String)perMap.get("to["+personObject.RELATIONSHIP_EMPLOYEE+"].from.attribute[Title]");
	                  			String sFirstName           = (String)perMap.get("attribute["+personObject.ATTRIBUTE_FIRST_NAME +"]");
	                  			String sLastName            = (String)perMap.get("attribute["+personObject.ATTRIBUTE_LAST_NAME +"]");
	                  			String userName             = personObject.getName();
	                  			// added for the bug 297703 to check for the status of a person
	                  			String sPersonState         =(String)perMap.get(DomainConstants.SELECT_CURRENT);
	
	                  			if(sPersonState.equalsIgnoreCase("Inactive")) {
	                       			flag=true;
	                  			}                         
	                 		 	else {
	    			            	HashMap tempMap = new HashMap();
	    			              	tempMap.put(personObject.SELECT_ID,personTypeId);
	    			              	tempMap.put(personObject.SELECT_NAME, userName);
	    			              	tempMap.put("LastFirstName",sLastName+", "+sFirstName);
	    			              	tempMap.put(personObject.SELECT_TYPE,personObject.getType(context));
	    			              	tempMap.put("projectLead","");
	    			              	tempMap.put("createRoute",sCreateRoute);
	    			              	sOrgName = (sOrgName == null || "null".equals(sOrgName)) ? "" : sOrgName;
	    			              	tempMap.put("OrganizationName",sOrgName);
	    			              	tempMap.put("OrganizationTitle",sOrgTitle);
	                            	if(userName.equals(sUser)){
	                                    tempMap.put("access","Add Remove");
	                            	} else{
	                                    tempMap.put("access","Read");
	                            	}
	                  				memberMapList.add((Map)tempMap);
	                  			}
	                  			//End of Bug 297703*/
	
	    			            boolean updatedMapList = false;
	    			            Iterator mapItr = taskMapList.iterator();
	                  			while(mapItr.hasNext()){
	                    			HashMap taskMap = (HashMap)mapItr.next();
	                    			if("none".equals((String)taskMap.get("PersonId"))){
	                          			taskMap.put("PersonId",personTypeId);
	                      				taskMap.put("PersonName",sLastName+", "+sFirstName);
	                          			taskMap.put(DomainConstants.SELECT_NAME,userName);
	                      				updatedMapList = true;
	                      				i = i + 1;
	                      				break;
	                    			}
	                  			}
	
	                  			//For the 4th Step
	                  			if(!updatedMapList && allowExtend) {
	                    			HashMap tempHashMap = new HashMap();
	                    			tempHashMap.put("PersonId",personTypeId);
	                    			tempHashMap.put(personObject.SELECT_NAME, userName);
	                    			tempHashMap.put("PersonName",sLastName+", "+sFirstName);
	                    			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_SEQUENCE,"");
	                    			tempHashMap.put(personObject.ATTRIBUTE_ALLOW_DELEGATION,"");
	                    			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_ACTION ,"");
	                    			tempHashMap.put(personObject.ATTRIBUTE_ROUTE_INSTRUCTIONS ,"");
	                    			tempHashMap.put(personObject.ATTRIBUTE_TITLE ,"");
	                    			tempHashMap.put(personObject.ATTRIBUTE_SCHEDULED_COMPLETION_DATE,"");
	                    			tempHashMap.put(personObject.RELATIONSHIP_ROUTE_NODE,String.valueOf(routeNodeIds));
	                    			tempHashMap.put("memberType",memberType);
	                        		taskMapList.add((Map)tempHashMap);
	                    			routeNodeIds++;
	                  			}
	                  			else {
	                    			if((!updatedMapList) && (!allowExtend)){
	                      				j = j + 1;
	                      				memberNames = memberNames + sFirstName + " " + sLastName + ", " ;
	                    			}
	                  			}
	                		}
        			}else{
        			    if(!strGroupList.contains((String)idmap.get(DomainConstants.SELECT_NAME))){
        			        strGroupList.add((String)idmap.get(DomainConstants.SELECT_NAME)); 
        			    }
        			}
          			}
          			}
        		}	
                 if(strGroupList.size() >  0){
                     MapList groupMapList = new MapList();
             		try {
                 	groupMapList =(MapList)formBean.getElementValue("routeGroupMapList");
               	} catch(Exception cet){  }
     			    groupMapList = addGroupsToRouteWizard(request, FrameworkUtil.join(strGroupList, ";"), existingList, taskMapList, memberMapList, groupMapList);
               		formBean.setElementValue("routeGroupMapList",groupMapList);   
                 }
    		}
    		
    	  	//to put the MapList reqd for the 4th step of Route Wizard in the session
     		if(keyValue!=null && !keyValue.equals("null") && !"null".equals(keyValue) && !keyValue.equals("") ) {
         		/*Simple Route Creation*/
           		if("QuickRoute".equals(fromPage)) {
                    //Take  the details from the above  constructed MAP
                    
                    StringBuffer routeMemberBuffer=new StringBuffer();
                    String browserLang = request.getHeader("Accept-Language");
                    try {
                        if("Role".equals(memberType)) {
                            routeMemberType = EnoviaResourceBundle.getProperty(context,"emxComponentsStringResource",context.getLocale(), "emxComponents.Common.Role"); 
                        	for(Iterator mapItr=memberMapList.iterator();mapItr.hasNext();) {
    	                        HashMap memMap=(HashMap)mapItr.next();
    	                        String role = (String)memMap.get("LastFirstName");
    	                        routeMemberBuffer.append(i18nNow.getAdminI18NString("Role", role, browserLang));
    	                        routeMemberBuffer.append("|");
    	                        routeMemberBuffer.append(role);
    	                        routeMemberBuffer.append(";");
    	                    }
                        } else if("Group".equals(memberType)) {
                            routeMemberType = EnoviaResourceBundle.getProperty(context,"emxComponentsStringResource",context.getLocale(), "emxComponents.Common.Group");
                        	for(Iterator mapItr=memberMapList.iterator();mapItr.hasNext();) {
    	                        HashMap memMap=(HashMap)mapItr.next();
    	                        String group = (String)memMap.get("LastFirstName");
    	                        routeMemberBuffer.append(i18nNow.getAdminI18NString("Group", group, browserLang));
    	                        routeMemberBuffer.append("|");
    	                        routeMemberBuffer.append(group);
    	                        routeMemberBuffer.append(";");
    	                    }
                        }else if("UserGroup".equals(memberType)){
                        	routeMemberType = EnoviaResourceBundle.getProperty(context,"emxFrameworkStringResource",context.getLocale(), "emxFramework.Type.Group");
                        	for(Iterator mapItr=memberMapList.iterator();mapItr.hasNext();) {
    	                        HashMap memMap=(HashMap)mapItr.next();
    	                        String userGroup = (String)memMap.get("LastFirstName");
    	                        routeMemberBuffer.append(userGroup);
    	                        routeMemberBuffer.append("|");
    	                        routeMemberBuffer.append((String)memMap.get("id"));
    	                        routeMemberBuffer.append(";");
    	                    }
                        } else {
                            routeMemberType = "PERSON";
                        	for(Iterator mapItr=memberMapList.iterator();mapItr.hasNext();) {
    	                        HashMap memMap=(HashMap)mapItr.next();
    	                        routeMemberBuffer.append((String)memMap.get("LastFirstName"));
    	                        routeMemberBuffer.append("|");
    	                        routeMemberBuffer.append((String)memMap.get("id"));
    	                        routeMemberBuffer.append(";");
    	                    }
                        }
                    } catch(Exception e){}
                    routeMemberString=routeMemberBuffer.toString();
           		} //Simple Route Creation End
            	else {
                	formBean.setElementValue("taskMapList",taskMapList);
                  	formBean.setElementValue("routeMemberMapList",memberMapList);
                  	formBean.setFormValues(session);
            	}
     		}
    	}
    	strpopUpURL = UINavigatorUtil.getCommonDirectory(context) + "/emxTree.jsp?objectId=" + XSSUtil.encodeForURL(context, sRouteId) + "&emxSuiteDirectory="+XSSUtil.encodeForURL(context, appDirectory);
    }

%>
<%!
//added on 23 dec
     public MapList addGroupsToRouteWizard(HttpServletRequest request, String strGroups, StringList existingMemberList, MapList taskMapList,MapList memberMapList, MapList groupMapList) throws MatrixException
  {
    String[] memberRowIDs     = com.matrixone.apps.common.util.ComponentsUIUtil.getSplitTableRowIds(emxGetParameterValues(request, "emxTableRowId"));
    StringList groupList  = new StringList();
		strGroups = (strGroups != null) ? strGroups : ComponentsUIUtil.arrayToString(memberRowIDs, ";");
    if(strGroups != null) {
        groupList = FrameworkUtil.split(strGroups,";");
    }
		if(groupMapList == null) {
		    groupMapList = new MapList();
		}
		MapList tempGroupMapList = new MapList();
  		Iterator groupListItr = groupList.iterator();
  		while(groupListItr.hasNext()){
  		    String groupName = groupListItr.next().toString();
  		    if(existingMemberList.contains(groupName)){
  		      groupListItr.remove();
  		    }
  		}
		if(groupList.size() > 0) {
 		tempGroupMapList = getRolesorGroups(groupList, "Group",taskMapList );
 		memberMapList.addAll(tempGroupMapList);
 		groupMapList.addAll(tempGroupMapList);
	}
		return groupMapList;
  }
%>
<script language="javascript">

var quickRoute = "QuickRoute"=="<%=XSSUtil.encodeForJavaScript(context, fromPage)%>";
var fromAccessPage = "RouteAccessSummary" == "<%=XSSUtil.encodeForJavaScript(context, fromPage)%>";
var flag = "<%=flag%>"=="true";
var isRevised = "<%=isRevised%>" == "true";

<framework:ifExpr expr="<%=showSelectItemAlert%>">
	alert("<emxUtil:i18nScript localize="i18nId">emxComponents.Search.Error.24</emxUtil:i18nScript>");
</framework:ifExpr>

<framework:ifExpr expr="<%=!showSelectItemAlert%>">
	<framework:ifExpr expr="<%=memberNames.length() != 0%>">
		alert("<emxUtil:i18nScript localize="i18nId">emxComponents.RouteWizard.MaintainExactTaskListAlert</emxUtil:i18nScript>");
	</framework:ifExpr>
	 
	var isPopupWindow = false;
	if(getTopWindow().getWindowOpener() && isMoz && (!getTopWindow().SnN || !getTopWindow().SnN.FROM_SNN)){
		isPopupWindow = true;
	}
	 
	if(quickRoute) {
	    populateQuickRouteMemberList("<%=XSSUtil.encodeForJavaScript(context, routeMemberType)%>", "<%=XSSUtil.encodeForJavaScript(context, routeMemberString)%>");
	} else {
		if(!isRevised) {
			if(flag) {
				alert("<emxUtil:i18nScript localize="i18nId">emxComponents.RouteWizard.InactivePerson</emxUtil:i18nScript>" );
			}
				if(fromAccessPage){
					getTopWindow().getWindowOpener().getTopWindow().refreshSpecificPage("APPTasksGraphical");
					getTopWindow().getWindowOpener().getTopWindow().refreshSpecificPage("APPTask");
			}
					getTopWindow().getWindowOpener().parent.location.href = getTopWindow().getWindowOpener().parent.location.href;		
		} else {
			getTopWindow().getWindowOpener().parent.location.href = "<%=strpopUpURL%>";
		}
	}
		
	window.closeWindow(isPopupWindow);
</framework:ifExpr>
</script>

