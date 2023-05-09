 <%--  emxTeamTaskCompleteProcess.jsp   -  Promoting Task to Complete State
    Copyright (c) 1992-2020 Dassault Systemes.
    All Rights Reserved.
    This program contains proprietary and trade secret information of MatrixOne,
    Inc.  Copyright notice is precautionary only
    and does not evidence any actual or intended publication of such program

    static const char RCSID[] = $Id: emxTaskCompleteProcess.jsp.rca 1.37 Wed Oct 22 16:18:26 2008 przemek Experimental przemek przemek $
 --%>
<%@page import="com.matrixone.apps.domain.DomainConstants"%>
<%@page import="com.matrixone.apps.framework.ui.UIUtil"%>
<%@page import = "matrix.db.ClientTaskList"%>
<%@page import = "matrix.db.ClientTaskItr"%>
<%@include file  = "../emxUICommonAppInclude.inc"%>
<%@include file  = "emxRouteInclude.inc"%>
<%@include file = "../common/enoviaCSRFTokenValidation.inc"%>
<%@include file="../common/emxNavigatorTopErrorInclude.inc"%>
<%@page import="javax.json.JsonArray"%>
<%@page import="javax.json.Json"%>
<%@page import="javax.json.JsonObject"%>
<%@page import="javax.json.JsonArrayBuilder"%>
<%@page import="javax.json.JsonObjectBuilder"%>
<%@page import="java.lang.reflect.Method"%>
<%

	String i18NReadAndUnderstand =EnoviaResourceBundle.getProperty(context, "emxFrameworkStringResource", context.getLocale(),
		"emxFramework.UserAuthentication.ReadAndUnderstand");
    String strCommentsAttr       = PropertyUtil.getSchemaProperty(context, "attribute_Comments" );
    String strApprovalStatusAttr = PropertyUtil.getSchemaProperty(context, "attribute_ApprovalStatus" );
	String fromFDA=emxGetParameter(request, "fromFDA");
    String taskId       = emxGetParameter(request, "taskId");
    String targetLocation = emxGetParameter(request, "targetLocation");
    String routeId      = emxGetParameter(request, "routeId");
	String approvalStatus      = emxGetParameter(request, "approvalStatus");
    String comments = emxGetParameter(request, "Comments");
    String isCommentRequired = EnoviaResourceBundle.getProperty(context,"emxComponents.Routes.EnforceAssigneeApprovalComments");
	String eSignRecordId=emxGetParameter(request,"eSignRecordId");
	if("false".equals(isCommentRequired))
	{
	  comments=emxGetParameter(request, "Comments1");
	}
	
    boolean getCommentsFromTaskId = "true".equals(emxGetParameter(request, "getCommentsFromTaskId"));
	
	   String languageStr = emxGetParameter(request,"languageStr");
       String timeZone = emxGetParameter(request,"timeZone");
       
       String reviewerComments    = emxGetParameter(request,"ReviewerComments");
       String taskScheduledDateReq    = emxGetParameter(request,"DueDate");
       String assigneeDueTime    = emxGetParameter(request,"routeTime");
	   
	//added for ESign Authentication.
   String requiresESign = emxGetParameter(request,"requiresESign");
	

	// Added 359515
	String strisEncoded = emxGetParameter(request,"isEncoded");
	if(strisEncoded != null && strisEncoded.equals("true") && comments != null && !"".equals(comments))
	{
	String strCharSet = Framework.getCharacterEncoding(request);
	comments = FrameworkUtil.decodeURL(comments,strCharSet);
	}
	// Ended
	String taskStatus   = emxGetParameter(request, "ApprovalStatus");
    if(UIUtil.isNullOrEmpty(taskStatus)) taskStatus   = emxGetParameter(request, "approvalStatus");


    Route route = (Route)DomainObject.newInstance(context,  routeId);
    com.matrixone.apps.common.Person person = new com.matrixone.apps.common.Person();
    com.matrixone.apps.domain.DomainObject task = new com.matrixone.apps.domain.DomainObject();
    task.setId(taskId);

    if(getCommentsFromTaskId && (comments == null || "".equals(comments))) {
        comments = task.getInfo(context, "attribute[" + DomainConstants.ATTRIBUTE_COMMENTS + "]");
    }


    String attributeBracket = "attribute[";
    String closeBracket = "]";
    DomainRelationship domainRel            = null;
    String attrReviewCommentsNeeded         = PropertyUtil.getSchemaProperty(context, "attribute_ReviewCommentsNeeded");
    String sAttParallelNodeProcessonRule    = PropertyUtil.getSchemaProperty(context, "attribute_ParallelNodeProcessionRule");
    String attrTaskCommentsNeeded   		= PropertyUtil.getSchemaProperty(context, "attribute_TaskCommentsNeeded");
    String strRouteActionAttr        		= PropertyUtil.getSchemaProperty(context, "attribute_RouteAction" );
    String SELECT_ROUTE_ACTION        		= "attribute[" + strRouteActionAttr + "]";

    double clientTZOffset                   = (new Double((String)session.getValue("timeZone"))).doubleValue();
    boolean bTaskCommentGiven               = false;
    boolean returnBack                      = false;
    boolean isProjectSpace                  = false;
    String OFFSET_FROM_TASK_CREATE_DATE     = "Task Create Date";
    String routeTime                        = "";
    String taskCommNeeded     = "";
    String strWorkspaceId   = "";
    String taskScheduledDate  = "";

    StringBuffer sAttrComments =new StringBuffer(attributeBracket);
    sAttrComments.append(DomainConstants.ATTRIBUTE_COMMENTS);
    sAttrComments.append(closeBracket);
    StringBuffer sAttrReviewTask =new StringBuffer(attributeBracket);
    sAttrReviewTask.append(DomainConstants.ATTRIBUTE_REVIEW_TASK);
    sAttrReviewTask.append(closeBracket);
    StringBuffer sAttrRouteNodeId =new StringBuffer(attributeBracket);
    sAttrRouteNodeId.append(DomainConstants.ATTRIBUTE_ROUTE_NODE_ID);
    sAttrRouteNodeId.append(closeBracket);
    StringBuffer sAttrApprovalStatus =new StringBuffer(attributeBracket);
    sAttrApprovalStatus.append(DomainObject.ATTRIBUTE_APPROVAL_STATUS);
    sAttrApprovalStatus.append(closeBracket);
    StringBuffer sAttrScheduledCompletionDate =new StringBuffer(attributeBracket);
    sAttrScheduledCompletionDate.append(DomainObject.ATTRIBUTE_SCHEDULED_COMPLETION_DATE );
    sAttrScheduledCompletionDate.append(closeBracket);

    StringList selectStmt = new StringList();
    selectStmt.addElement(sAttrComments.toString());
    selectStmt.addElement(sAttrReviewTask.toString());
    selectStmt.addElement(sAttrRouteNodeId.toString());
    selectStmt.addElement(sAttrApprovalStatus.toString());
    selectStmt.addElement(sAttrScheduledCompletionDate.toString());
    selectStmt.addElement("from["+DomainObject.RELATIONSHIP_ROUTE_TASK+"].to.to["+DomainObject.RELATIONSHIP_ROUTE_SCOPE+"].from.id");
    selectStmt.addElement(SELECT_ROUTE_ACTION);
	selectStmt.addElement(DomainConstants.SELECT_PHYSICAL_ID);
	selectStmt.addElement(DomainConstants.SELECT_REVISION);
    person=Person.getPerson(context);
    String personId = (String)person.getInfo(context,person.SELECT_ID);
    String personName = (String)person.getInfo(context,person.SELECT_NAME);

    String sRouteAction ="";
    String selRouteAction = "attribute["+task.ATTRIBUTE_ROUTE_ACTION+"]";
    boolean blnRouteActionApprove=true;


    AttributeList attrList                  = new AttributeList();
    Map taskInfoMap           = task.getInfo(context, selectStmt);

    String taskComments       = (String)taskInfoMap.get(sAttrComments.toString());
    taskCommNeeded            = task.getAttributeValue(context , attrTaskCommentsNeeded);
    strWorkspaceId                     = (String)taskInfoMap.get("from["+DomainObject.RELATIONSHIP_ROUTE_TASK+"].to.to["+DomainObject.RELATIONSHIP_ROUTE_SCOPE+"].from.id");
    taskScheduledDate                = (String)taskInfoMap.get(sAttrScheduledCompletionDate.toString());
    String taskAction         = (String)taskInfoMap.get(SELECT_ROUTE_ACTION);
    //Check the property where the comments are needed for an approval task.
    String showCommentsForTaskApproval    = EnoviaResourceBundle.getProperty(context,"emxComponents.Routes.ShowCommentsForTaskApproval");
    String ignoreCommentsForTaskRejection = EnoviaResourceBundle.getProperty(context,"emxComponentsRoutes.InboxTask.IgnoreComments");

    if("Approve".equals(taskStatus) && showCommentsForTaskApproval!=null && "false".equals(showCommentsForTaskApproval)){
      	taskCommNeeded = "No";
    }else if("Reject".equals(taskStatus) && ignoreCommentsForTaskRejection!=null && "true".equalsIgnoreCase(ignoreCommentsForTaskRejection)){
      taskCommNeeded = "No";
    }

    if("Yes".equals(taskCommNeeded))
    {
    	if(("Comment".equals(taskAction) && comments !=null && !"".equals(comments.trim())) || 
       	     ("Comment".equals(taskAction) && "false".equals(isCommentRequired)) ||
       	     ("Approve".equals(taskAction) && "false".equals(isCommentRequired)) ||
       	     ("Approve".equals(taskAction) &&  comments !=null && !"".equals(comments.trim()))){
//Bug No :364624 Dt:02-Feb-2009
        //   if(!trimStr(taskComments).equals(trimStr(comments))){
                bTaskCommentGiven =true;
         //   }
//Bug No :364624 Dt:02-Feb-2009
       }else{
         bTaskCommentGiven =false;
         comments="";
       }
       if(!bTaskCommentGiven && !taskAction.equals("Notify Only")){
         returnBack = true;
       }
    }else{
        if(comments !=null && (!"".equals(comments.trim()))){
           String oldComment=task.getInfo(context,"attribute["+DomainConstants.ATTRIBUTE_COMMENTS+"]");
           if(!oldComment.equals(comments)){
              bTaskCommentGiven =true;
           }
        }else{
            bTaskCommentGiven =false;
            comments="";
        }
    }

    if(!returnBack)
    {
    	String treeMenu = "";
    	try{
			ContextUtil.startTransaction(context, true);
            String strDateTime              = "";
			if (!UIUtil.isNullOrEmpty(taskScheduledDateReq)) {
				taskScheduledDate     =  eMatrixDateFormat.getFormattedInputDateTime(context,taskScheduledDateReq,assigneeDueTime,clientTZOffset, request.getLocale());	

			}
			
            if(taskScheduledDate != null && !"null".equals(taskScheduledDate) && !"".equals(taskScheduledDate)){
                    strDateTime                     = taskScheduledDate;//eMatrixDateFormat.getFormattedInputDateTime(context,taskScheduledDate,routeTime,clientTZOffset,request.getLocale());
            }
            String routeNodeId        = (String)taskInfoMap.get(sAttrRouteNodeId.toString());
            //Get the correct relId for the RouteNodeRel given the attr routeNodeId from the InboxTask.
            routeNodeId = route.getRouteNodeRelId(context, routeNodeId);
            String reviewTask         = (String)taskInfoMap.get(sAttrReviewTask.toString());
			
			task.setAttributeValue(context,strCommentsAttr, comments);
            if(bTaskCommentGiven){
                    task.setAttributeValue(context,attrTaskCommentsNeeded, "No");
            } else {
                    task.setAttributeValue(context,attrTaskCommentsNeeded, "Yes");
            }

            // Setting the attributes to the task object
            if (!"".equals(taskStatus)){
                    attrList.addElement(new Attribute(new AttributeType(strApprovalStatusAttr), taskStatus));
            }
            if(!"".equals(strDateTime)){
                    attrList.addElement(new Attribute(new AttributeType(DomainConstants.ATTRIBUTE_SCHEDULED_COMPLETION_DATE), strDateTime));
            }
            attrList.addElement(new Attribute(new AttributeType(DomainConstants.ATTRIBUTE_COMMENTS), comments));
			task.setAttributes(context,attrList);
            treeMenu = JSPUtil.getApplicationProperty(context,application,"eServiceComponents.treeMenu.InboxTask");

            if(  treeMenu  != null && !"null".equals( treeMenu  ) && !"".equals( treeMenu )) {
                    MailUtil.setTreeMenuName(context, treeMenu );
            }

            // promote task object
            if ("Yes".equalsIgnoreCase(reviewTask)){
                    AttributeList attrList1 = new AttributeList();
                    attrList1.addElement(new Attribute(new AttributeType(attrReviewCommentsNeeded), "Yes"));
                    task.setAttributes(context,attrList1);
                    task.promote(context);
                    try{
                            domainRel             = DomainRelationship.newInstance(context ,routeNodeId);
                            Map attrMap           = new Hashtable();
                            attrMap.put(attrReviewCommentsNeeded,"Yes");
                            Route.modifyRouteNodeAttributes(context, routeNodeId, attrMap);
							if(UIUtil.isNotNullAndNotEmpty(requiresESign) && requiresESign.equals("True")){   //added to update history if ESign Authentication is enabled.
								MqlUtil.mqlCommand(context, "Modify bus $1 add history $2 comment $3",false, taskId,taskStatus,i18NReadAndUnderstand);
								//ESign Record update for review task
								String eSignReviewTaskId =(String)taskInfoMap.get(DomainConstants.SELECT_PHYSICAL_ID);
								String taskLatestRevision=(String)taskInfoMap.get(DomainConstants.SELECT_REVISION);
								updateESignRecordData(context,eSignRecordId,"Review",eSignReviewTaskId,taskLatestRevision);
							}
							
							
                    }catch(Exception ex){
                            session.putValue("error.message",ex);
                    }

            }else{
                // do all operations for delta due-date offsets in this loop
                try {

                        StringBuffer sAttrDueDateOffset =new StringBuffer(attributeBracket);
                        sAttrDueDateOffset.append(DomainConstants.ATTRIBUTE_DUEDATE_OFFSET);
                        sAttrDueDateOffset.append(closeBracket);
                        StringBuffer sAttrDueDateOffsetFrom =new StringBuffer(attributeBracket);
                        sAttrDueDateOffsetFrom.append(DomainConstants.ATTRIBUTE_DATE_OFFSET_FROM);
                        sAttrDueDateOffsetFrom.append(closeBracket);
                        StringBuffer sAttrSequence =new StringBuffer(attributeBracket);
                        sAttrSequence.append(DomainConstants.ATTRIBUTE_ROUTE_SEQUENCE);
                        sAttrSequence.append(closeBracket);
                        String selState               = DomainObject.SELECT_CURRENT;  // get state

                        StringList relSelects            = new StringList();

                        StringBuffer sWhereExp           = new StringBuffer();
                        int nextTaskSeq                  = 0;
                        int currTaskSeq                  = 0;
                        domainRel                     = DomainRelationship.newInstance(context ,routeNodeId);
                        String currTaskSeqStr         = domainRel.getAttributeValue(context, DomainObject.ATTRIBUTE_ROUTE_SEQUENCE);
                        String parallelType                  = domainRel.getAttributeValue(context, sAttParallelNodeProcessonRule);

                        // get current / next task sequence
                        if(currTaskSeqStr != null && !"".equals(currTaskSeqStr) && !"null".equals(currTaskSeqStr)){
                                currTaskSeq = Integer.parseInt(currTaskSeqStr);
                                nextTaskSeq = currTaskSeq+1;
                        }
                        relSelects.addElement(sAttrDueDateOffset.toString());
                        relSelects.addElement(sAttrDueDateOffsetFrom.toString());
                        relSelects.addElement(DomainObject.SELECT_RELATIONSHIP_ID);
                        sWhereExp.append("(");
                        sWhereExp.append(selState);
                        sWhereExp.append(" != \"");
                        sWhereExp.append(DomainObject.STATE_INBOX_TASK_COMPLETE);
                        sWhereExp.append("\")");
                        sWhereExp.append(" && (");
                        sWhereExp.append(sAttrSequence.toString());
                        sWhereExp.append(" == \"");
                        sWhereExp.append(String.valueOf(currTaskSeq));
                        sWhereExp.append("\")");
                        // finds all same order tasks which are not yet complete; if any such exists, next offset task due-date is not set till all complete
                        boolean shouldSetNextTaskDueDate = shouldSetNextTaskDueDate(context, route,currTaskSeq+"");
                        boolean completeAny              = true;
                        if("All".equals(parallelType)){
                                completeAny = false;
                        }
                        // can proceed to set offset due-date for next task
                        // only if Parallel node rule is 'Any' or corresponding flag got is true
                        if(completeAny || shouldSetNextTaskDueDate){
                                // where clause filters to next order tasks offset from this task complete
                                sWhereExp = new StringBuffer();
                                sWhereExp.append("(");
                                sWhereExp.append(sAttrDueDateOffset.toString());
                                sWhereExp.append(" !~~ \"\")");
                                sWhereExp.append(" && (");
                                sWhereExp.append(sAttrDueDateOffsetFrom.toString());
                                sWhereExp.append(" ~~ \"");
                                sWhereExp.append(OFFSET_FROM_TASK_CREATE_DATE);
                                sWhereExp.append("\")");
                                sWhereExp.append(" && (");
                                sWhereExp.append(sAttrSequence.toString());
                                sWhereExp.append(" == \"");
                                sWhereExp.append(String.valueOf(nextTaskSeq));
                                sWhereExp.append("\")");
                                // get all next order tasks with offset from this task promotion / next task creation
                                MapList nextOrderOffsetList = getNextOrderOffsetTasks(context, route, relSelects, sWhereExp.toString());

                                // set Scheduled Due Date attribute for all delta offset Route Nodes
                                Route.setDueDatesFromOffset(context, session, nextOrderOffsetList);
                        }
                }catch(Exception e) {
                        session.putValue("error.message",e);

                }

                MapList subRouteList=route.getAllSubRoutes(context);
                
                try {
                task.promote(context);
                {
                	if(UIUtil.isNotNullAndNotEmpty(requiresESign) && requiresESign.equals("True")) {
                	String strRouteTaskUser = task.getAttributeValue(context,DomainConstants.ATTRIBUTE_ROUTE_TASK_USER);
                	String isResponsibleRoleEnabled = DomainConstants.EMPTY_STRING;
	                	try {
                		isResponsibleRoleEnabled = EnoviaResourceBundle.getProperty(context,"emxFramework.Routes.ResponsibleRoleForSignatureMeaning.Preserve");
	                	} catch(Exception e) {
                		isResponsibleRoleEnabled = "false";
                	}
	                	if(UIUtil.isNotNullAndNotEmpty(isResponsibleRoleEnabled) && isResponsibleRoleEnabled.equalsIgnoreCase("true") && UIUtil.isNotNullAndNotEmpty(strRouteTaskUser) && strRouteTaskUser.startsWith("role_")) {
                		i18NReadAndUnderstand = MessageUtil.getMessage(context, null, "emxFramework.UserAuthentication.ReadAndUnderstandRole", new String[] {
                				  PropertyUtil.getSchemaProperty(context, strRouteTaskUser)}, null, context.getLocale(),
                				  "emxFrameworkStringResource");
	                		if (taskStatus.equalsIgnoreCase("Approve")){
	                			MqlUtil.mqlCommand(context, "Modify bus $1 add history $2 comment $3",false, taskId,"approve",i18NReadAndUnderstand);
	                		} else {
                				MqlUtil.mqlCommand(context, "Modify bus $1 add history $2 comment $3",false, taskId,taskStatus,i18NReadAndUnderstand);
                			}
	                	} else {
	                		if (taskStatus.equalsIgnoreCase("Approve")) {
	                			MqlUtil.mqlCommand(context, "Modify bus $1 add history $2 comment $3",false, taskId,"approve",i18NReadAndUnderstand);
	                		} else {
               					MqlUtil.mqlCommand(context, "Modify bus $1 add history $2 comment $3",false, taskId,taskStatus,i18NReadAndUnderstand);
                			}
                		}
                	}
                }

				
             	String taskState = task.getInfo(context, DomainConstants.SELECT_CURRENT);
				if(UIUtil.isNotNullAndNotEmpty(requiresESign) && "True".equals(requiresESign) && taskState.equalsIgnoreCase(DomainObject.STATE_INBOX_TASK_COMPLETE) && UIUtil.isNotNullAndNotEmpty(eSignRecordId)){
					//Call API to update the esign
					String eSignTaskId =(String)taskInfoMap.get(DomainConstants.SELECT_PHYSICAL_ID);
					String taskLatestRevision=(String)taskInfoMap.get(DomainConstants.SELECT_REVISION);
					updateESignRecordData(context,eSignRecordId,"Complete",eSignTaskId,taskLatestRevision);

				}
				// Added to notify to the subscribed person, if Task completion event is subscribed -start
             	if(Route.STATE_ROUTE_COMPLETE.equals(taskState)) {
	        		try {
		              	SubscriptionManager subscriptionMgr = route.getSubscriptionManager();
		              	subscriptionMgr.publishEvent(context, route.EVENT_TASK_COMPLETED, task.getId(context));
		             } catch(Exception e) {
		             	System.out.println(e.getMessage());
		             }
             	}
	          	// Added to notify to the subscribed person, if Task completion event is subscribed -end


                String sSubject =i18nNow.getI18nString("emxComponents.common.TaskDeletionNotice", "emxComponentsStringResource" ,sLanguage);
                String sMessage1=i18nNow.getI18nString("emxComponents.common.TaskDeletionMessage3", "emxComponentsStringResource" ,sLanguage);
                String sMessage2=i18nNow.getI18nString("emxComponents.common.TaskDeletionMessage2", "emxComponentsStringResource" ,sLanguage);
                String sMessage=sMessage1+" "+task.getName()+" "+sMessage2;
                route.deleteOrphanSubRoutes(context,subRouteList,sMessage,sSubject);
                // set Inbox Task title for auto-namer tasks
                try{
                        ContextUtil.pushContext(context);
                        InboxTask.setTaskTitle(context, routeId);
                        ContextUtil.popContext(context);
                    }catch(Exception ex){
                        session.putValue("error.message",ex);
                    }
                // publish subscription events
                if (!"".equals(routeId)) {

                        treeMenu = JSPUtil.getApplicationProperty(context,application,"eServiceComponents.treeMenu.Route");
                        if(  treeMenu  != null && !"null".equals( treeMenu  ) && !"".equals( treeMenu )) {
                                MailUtil.setTreeMenuName(context, treeMenu );
                        }
                        StringList stringlist = new StringList(10);
                        stringlist.clear();
                        stringlist.add("id");
                        stringlist.add("type");
                        stringlist.add("grantor");
                        stringlist.add("grantee");
                        stringlist.add("granteeaccess");

                        DomainObject domainobject1 = new DomainObject();
                        domainobject1.setId(routeId);

                        MapList maplist = domainobject1.getRelatedObjects(context, DomainConstants.RELATIONSHIP_OBJECT_ROUTE, "*", stringlist, DomainConstants.EMPTY_STRINGLIST, true, false, (short)1, DomainConstants.EMPTY_STRING, DomainConstants.EMPTY_STRING);
                        Map map1 = domainobject1.getInfo(context, stringlist);
                        maplist.add(0, map1);
                        Iterator iterator = maplist.iterator();
                        while(iterator.hasNext()){
                                Map map2 = (Map)iterator.next();
                                Object obj1 = null;
                                Object obj2 = null;
                                Object obj3 = null;
                                Object obj4 = map2.get("grantor");
                                if((obj4 instanceof String) || obj4 == null){
                                        if(obj4 == null || "".equals(obj4))
                                                continue;
                                        obj1 = new ArrayList(1);
                                        obj2 = new ArrayList(1);
                                        obj3 = new ArrayList(1);
                                        ((java.util.List) (obj1)).add(obj4);
                                        ((java.util.List) (obj2)).add(map2.get("grantee"));
                                        ((java.util.List) (obj3)).add(map2.get("granteeaccess"));
                                }else{
                                        obj1 = (java.util.List)map2.get("grantor");
                                        obj2 = (java.util.List)map2.get("grantee");
                                        obj3 = (java.util.List)map2.get("granteeaccess");
                                }

                                String objId = (String)map2.get("id");
                                BusinessObject doc=new BusinessObject(objId);
                                doc.open(context);
                                if (doc.getTypeName().equals("Document")){

                                        for(int i = 0; i < ((java.util.List) (obj1)).size(); i++) {
                                                String strGrantee = (String)((java.util.List) (obj2)).get(i);

                                                if(strGrantee.equals(personName)) {
                                                        String strGrantor = (String)((java.util.List) (obj1)).get(i);

                                                        if(strGrantor.equals(DomainConstants.PERSON_ROUTE_DELEGATION_GRANTOR)) {
                                                        try {

                                                                ContextUtil.pushContext(context, DomainConstants.PERSON_ROUTE_DELEGATION_GRANTOR, null, context.getVault().getName());
                                                                String MQLstmt = "modify bus " + objId + " revoke grantor '" + strGrantor + "' grantee '" + personName + "';";
                                                                String MQLret = MqlUtil.mqlCommand(context, MQLstmt, false);

                                                                } catch(Exception exception4){
                                                                        throw new FrameworkException(exception4);
                                                                }
                                                                finally{
                                                                        ContextUtil.popContext(context);
                                                                }
                                                        }
                                                }
                                        }
                                }
                                doc.close(context);
                        }
                }
                } catch(Exception ex)
                {
                   
                   boolean clientTaskMessagesExists = false;			
                	clientTaskMessagesExists = MqlNoticeUtil.checkIfClientTaskMessageExists(context);
                	if (clientTaskMessagesExists) {
					//  already handled by check trigger.
					}else if( ( ex.toString()!=null ) && (ex.toString().trim()).length()>0 ){
                        emxNavErrorObject.addMessage(ex.toString().trim());
                }
        }

      } ContextUtil.commitTransaction(context); 
    	}catch(Exception e){
   			 	String strLanguage = context.getSession().getLanguage();
         		Locale strLocale = context.getLocale();
			 	String strPromoteConnectedError =EnoviaResourceBundle.getProperty(context,
                                        "emxComponentsStringResource",
                                        strLocale,
                                        "emxComponents.Common.PromoteConnectObjectFailed");
				ClientTaskList listNotices 	= context.getClientTasks();	
				ClientTaskItr itrNotices 	= new ClientTaskItr(listNotices);
    			String message = "";
    			boolean isPromoteConnectObject = false;
    			while (itrNotices.next()) {
    				ClientTask clientTaskMessage =  itrNotices.obj();
    				String emxMessage = (String)clientTaskMessage.getTaskData();
    				 if(emxMessage.contains(DomainConstants.WARNING_1501905)){
    	                	continue;
    	                }
    				if(UIUtil.isNotNullAndNotEmpty(emxMessage) && strPromoteConnectedError.equalsIgnoreCase(emxMessage.trim())){
    					isPromoteConnectObject = true;
    					message =emxMessage +":" + message;
    				}else {
    					message = message + emxMessage;  					
    				}
    			}
    			if(isPromoteConnectObject){
    				context.clearClientTasks();
    				MqlUtil.mqlCommand(context, "notice $1",message);
    			}
    		ContextUtil.abortTransaction(context); 
    	}
        //Publish Route Completed Event when Route goes to Complete State
        String routeState = route.getInfo(context,DomainObject.SELECT_CURRENT);

        if(routeState != null && "Complete".equals(routeState)){
                treeMenu = JSPUtil.getApplicationProperty(context,application,"eServiceComponents.treeMenu.Route");
                boolean validTreeMenu=false;
                if(  treeMenu  != null && !"null".equals( treeMenu  ) && !"".equals( treeMenu )){
                        validTreeMenu=true;
                }
                // PMC Bug 313582:java.lang.ClassCastException - While completing the Route task
                // Added to get the "type" of the "from" end object(Workspace Vault) connected through "Route Scope" relationship as "Super User" since the context user does not have access
                ContextUtil.pushContext(context);
                String sScopeObjType = (String)route.getInfo(context,"to["+route.RELATIONSHIP_ROUTE_SCOPE+"].from.type");
                String sWorkspaceId = "";
                // PMC Bug 313582 Ends here
                ContextUtil.popContext(context);

                //To get the Workspace object to publish the corresponding subscribed events
                if(DomainConstants.TYPE_PROJECT_VAULT.equals(sScopeObjType)) {
                         ContextUtil.pushContext(context);
                        String sWorkspaceVaultId = route.getInfo(context,"to["+route.RELATIONSHIP_ROUTE_SCOPE+"].from.id");
                        WorkspaceVault workspaceVault = (WorkspaceVault)DomainObject.newInstance(context ,sWorkspaceVaultId);
                        if(validTreeMenu) {
                                MailUtil.setTreeMenuName(context, treeMenu );
                        }

                        sWorkspaceId = UserTask.getProjectId(context, sWorkspaceVaultId);
                        if (sWorkspaceId!=null)
                        {
                                DomainObject domainObject = DomainObject.newInstance(context);
                                domainObject.setId(sWorkspaceId);
                                String sType=domainObject.getInfo(context,domainObject.SELECT_TYPE);
                                if(sType.equals(DomainObject.TYPE_PROJECT_SPACE) || mxType.isOfParentType(context,sType,DomainConstants.TYPE_PROJECT_SPACE)) //Modified for Sub type
                                        isProjectSpace=true;
                        }

                        ContextUtil.popContext(context);
                }else{
                        ContextUtil.pushContext(context);
                        sWorkspaceId  = route.getInfo(context,"to["+route.RELATIONSHIP_ROUTE_SCOPE+"].from.id");
                        ContextUtil.popContext(context);
                }
                Workspace workspace =null;
                if(( UIUtil.isNotNullAndNotEmpty(sWorkspaceId)) && !(isProjectSpace)) {
                        ContextUtil.pushContext(context);
                        workspace = (Workspace)DomainObject.newInstance(context ,sWorkspaceId );
                        if(validTreeMenu){
                                MailUtil.setTreeMenuName(context, treeMenu);
                        }
                        ContextUtil.popContext(context);
                }
                ContextUtil.pushContext(context);

                //To get list of documents attached to Route and publish Route Completed Event for the document
                StringList listContentIds = route.getInfoList(context,"to["+route.RELATIONSHIP_OBJECT_ROUTE+"].from.id");
                ContextUtil.popContext(context);

                Document document = (Document)DomainObject.newInstance(context,DomainObject.TYPE_DOCUMENT);
                if(listContentIds == null){
                        listContentIds = new StringList();
                }

                Iterator contentItr = listContentIds.iterator();

                while(contentItr.hasNext()){
                        String sDocId = (String)contentItr.next();

                        document.setId(sDocId);

                        if(validTreeMenu) {

                                MailUtil.setTreeMenuName(context, treeMenu );

                        }

                        ContextUtil.pushContext(context);
                        ContextUtil.popContext(context);


                }

                if(validTreeMenu) {
                        MailUtil.setTreeMenuName(context, treeMenu );
                }
        }

}

%>

<%--
    //set the RPE Variable
    String treeMenu = JSPUtil.getApplicationProperty(context,application,"eServiceComponents.treeMenu.InboxTask");

    if(treeMenu != null && !"".equals(treeMenu)){
      MQLCommand mql = new MQLCommand();
      mql.open(context);
      String mqlCommand = "set env global MX_TREE_MENU " + treeMenu;
      mql.executeCommand(context, mqlCommand);
      mql.close(context);
    }
--%>

<html>
<%@include file = "../common/emxNavigatorBottomErrorInclude.inc"%>
<body>
   <form name="newForm">
       <input type="hidden" name="objectId" value="<xss:encodeForHTMLAttribute><%=taskId%></xss:encodeForHTMLAttribute>" />
   </form>

   <!-- To use the findFrame function -->
   <script language="JavaScript" type="text/javascript" src="../common/scripts/emxUIConstants.js"></script>
   <script language="javascript" src="../common/scripts/emxUICore.js"></script>
   <script language="javascript">
<%
      String flag = emxGetParameter(request,"flag");

      if(flag != null && "fda".equalsIgnoreCase(flag))
      {
       if(returnBack){
%>

        alert("<emxUtil:i18nScript localize="i18nId">emxComponents.RejectComments.Comments</emxUtil:i18nScript>");
        getTopWindow().closeWindow();
<%
      }
      else
      {
%>
        parent.window.getWindowOpener().parent.location.reload();
        getTopWindow().closeWindow();
<%   }
      }
      else
      {
%>
<%
  if(returnBack){
%>

     alert("<emxUtil:i18nScript localize="i18nId">emxComponents.RejectComments.Comments</emxUtil:i18nScript>");
     parent.location.href = parent.location.href;
<%
  }else{
%>
if(getTopWindow().getWindowOpener() || (getTopWindow() && <%="true".equals(fromFDA)%> ) )
{
	if(getTopWindow() && getTopWindow().getWindowOpener() == null && <%="true".equals(fromFDA)%> ){
		var pageContentFrame = openerFindFrame(getTopWindow(),"pagecontent");
       if(pageContentFrame){
       		pageContentFrame.document.location.href = pageContentFrame.document.location.href;
        }
        var taskSignatureTableFrame = openerFindFrame(getTopWindow(),"AEFLifecycleTaskSignatures");
        if(taskSignatureTableFrame){
            taskSignatureTableFrame.document.location.href = taskSignatureTableFrame.document.location.href;
        }
        var approvalsTableFrame = openerFindFrame(getTopWindow(),"AEFLifecycleApprovals");
        if(approvalsTableFrame){
            approvalsTableFrame.document.location.href = approvalsTableFrame.document.location.href;
                }
				
		var portalFrame =  findFrame(getTopWindow(), "portalDisplay");
		if(portalFrame != null && portalFrame != "undefined"){
			portalFrame.document.location.href = portalFrame.document.location.href;
		}
				
        var contentFrame = openerFindFrame(getTopWindow(), "detailsDisplay");
        if(contentFrame != null && contentFrame != "undefined"){
            contentFrame.document.location.href = contentFrame.document.location.href;
        }else{
        	contentFrame = openerFindFrame(getTopWindow(), "content");
        	if(contentFrame != null && contentFrame != "undefined"){
        		contentFrame.document.location.href = contentFrame.document.location.href;
        	}
        }
		
		 if(getTopWindow().RefreshHeader){
			getTopWindow().deletePageCache();
			getTopWindow().RefreshHeader();
		}
	}
	else {
	var frameName = (getTopWindow().getWindowOpener() && getTopWindow().getWindowOpener().getTopWindow().getWindowOpener())? getTopWindow().getWindowOpener().getTopWindow().getWindowOpener():getTopWindow().getWindowOpener().getTopWindow();
    var frame = findFrame(frameName, "portalDisplay");
    if(getTopWindow().getWindowOpener() && !frame) {
    	var pageContentFrame = openerFindFrame(getTopWindow(),"pagecontent");
        if(pageContentFrame){
        	pageContentFrame.document.location.href = pageContentFrame.document.location.href;
        }
        var taskSignatureTableFrame = openerFindFrame(getTopWindow(),"AEFLifecycleTaskSignatures");
        if(taskSignatureTableFrame){
        	taskSignatureTableFrame.document.location.href = taskSignatureTableFrame.document.location.href;
        }
        var approvalsTableFrame = openerFindFrame(getTopWindow(),"AEFLifecycleApprovals");
        if(approvalsTableFrame){
        	approvalsTableFrame.document.location.href = approvalsTableFrame.document.location.href;
        }
        var contentFrame = openerFindFrame(getTopWindow(), "detailsDisplay");
	    if(contentFrame != null && contentFrame != "undefined"){
	    	contentFrame.document.location.href = contentFrame.document.location.href;
        }else{
        	contentFrame = openerFindFrame(getTopWindow(), "content");
        	if(contentFrame != null && contentFrame != "undefined"){
        		contentFrame.document.location.href = contentFrame.document.location.href;
        	}
	    }
		
    }
    if(frame){
		if(<%="slidein".equals(targetLocation)%>){
			var APPRouteDetailsFrame = findFrame(getTopWindow().opener.getTopWindow(), "APPRouteDetails");
			if(APPRouteDetailsFrame){
				APPRouteDetailsFrame.location.href = APPRouteDetailsFrame.location.href;
			}
	    }

       var pageContentFrame = openerFindFrame(getTopWindow(),"pagecontent");
       if(pageContentFrame){
       		pageContentFrame.document.location.href = pageContentFrame.document.location.href;
        }
        var taskSignatureTableFrame = openerFindFrame(getTopWindow(),"AEFLifecycleTaskSignatures");
        if(taskSignatureTableFrame){
            taskSignatureTableFrame.document.location.href = taskSignatureTableFrame.document.location.href;
        }
        var approvalsTableFrame = openerFindFrame(getTopWindow(),"AEFLifecycleApprovals");
        if(approvalsTableFrame){
            approvalsTableFrame.document.location.href = approvalsTableFrame.document.location.href;
                }
        var contentFrame = openerFindFrame(getTopWindow(), "detailsDisplay");
        if(contentFrame != null && contentFrame != "undefined"){
            contentFrame.document.location.href = contentFrame.document.location.href;
        }else{
        	contentFrame = openerFindFrame(getTopWindow(), "content");
        	if(contentFrame != null && contentFrame != "undefined"){
        		contentFrame.document.location.href = contentFrame.document.location.href;
        	}
        }
     }
	 
	
    if(getTopWindow().RefreshHeader){
    	getTopWindow().deletePageCache();
		getTopWindow().RefreshHeader();
    }
	if(getTopWindow().opener.getTopWindow().RefreshHeader){
		getTopWindow().opener.getTopWindow().deletePageCache();
		getTopWindow().opener.getTopWindow().RefreshHeader();
	}

	if(getTopWindow().opener.getTopWindow().opener && getTopWindow().opener.getTopWindow().opener.getTopWindow().RefreshHeader){
		getTopWindow().opener.getTopWindow().opener.getTopWindow().deletePageCache();
		getTopWindow().opener.getTopWindow().opener.getTopWindow().RefreshHeader();
    }

    if(<%=!"slidein".equals(targetLocation)%> && <%=!"true".equals(fromFDA)%> ){ //XSSOK  
     getTopWindow().closeWindow();
    }
	}
  }else{
	    if(<%=!"slidein".equals(targetLocation)%>){ //XSSOK
			
			parent.location.href = parent.location.href;
      		if(getTopWindow().RefreshHeader){
      	    	getTopWindow().deletePageCache();
      			getTopWindow().RefreshHeader();
      	    }
			
	    }
}
<%
  }
%>
<%
      }
%>
  </script>
</body>
</html>
  <%!
// method gets all tasks of next order whose due-dates are offset from Task Complete Event
// returns true only if all same sequence tasks with 'All' ParallelNode processing rule are completed
public boolean shouldSetNextTaskDueDate(Context context, Route route, String currTaskSeq) throws Exception{

boolean retVal =false;
StringList objSelect=new StringList();
objSelect.add(DomainObject.SELECT_ID);
objSelect.add("attribute["+DomainObject.ATTRIBUTE_ROUTE_NODE_ID+"]");
objSelect.add(DomainObject.SELECT_CURRENT);
// get all Inbox Tasks
MapList taskMapList = route.getRelatedObjects(context,
                               Route.RELATIONSHIP_ROUTE_TASK, //String relPattern
                               Route.TYPE_INBOX_TASK,         //String typePattern
                               objSelect,                     //StringList objectSelects,
                               null,                          //StringList relationshipSelects,
                               true,                          //boolean getTo,
                               false,                         //boolean getFrom,
                               (short)1,                      //short recurseToLevel,
                               "",                           //String objectWhere,
                               "",                           //String relationshipWhere,
                               null,                        //Pattern includeType,
                               null,                        //Pattern includeRelationship,
                               null);                       //Map includeMap

// for getting all Same sequence tasks which are incomplete
 MapList incompleteSameSeqMapList=new MapList();
 Iterator it=taskMapList.iterator();
 String state = "";
 String relId = "";
 String currTaskSeqStr = "";
 DomainRelationship domRel = null;
 Hashtable hashTable = new Hashtable();
 while(it.hasNext()){
  hashTable=(Hashtable)it.next();
  state=(String)hashTable.get(DomainObject.SELECT_CURRENT);
  relId=(String)hashTable.get("attribute["+DomainObject.ATTRIBUTE_ROUTE_NODE_ID+"]");

        //Get the correct relId for the RouteNodeRel given the attr routeNodeId from the InboxTask.
        relId = route.getRouteNodeRelId(context, relId);

if(relId!=""){
  domRel=DomainRelationship.newInstance(context,relId);
  currTaskSeqStr    = domRel.getAttributeValue(context, DomainObject.ATTRIBUTE_ROUTE_SEQUENCE);
  if(currTaskSeq.equals(currTaskSeqStr) && !state.equalsIgnoreCase(DomainObject.STATE_INBOX_TASK_COMPLETE)){
    incompleteSameSeqMapList.add(hashTable);
  }
 }
 }

 if(incompleteSameSeqMapList.size()==1 ){
  retVal=true;
 }
 return retVal;
}
public static void updateESignRecordData(Context context,String eSignRecordId,String completeReviewTask,String eSignTaskId,String taskLatestRevision){
	try{
		JsonObjectBuilder jsonMaturityObj = BPSJsonObjectBuilder.createJsonObjectBuilder(Json.createObjectBuilder());
		JsonObjectBuilder jsonObjRevision = BPSJsonObjectBuilder.createJsonObjectBuilder(Json.createObjectBuilder());
		JsonObjectBuilder jsonObj = BPSJsonObjectBuilder.createJsonObjectBuilder(Json.createObjectBuilder());
		jsonObj.add("before","To Do");
		jsonObj.add("after",completeReviewTask);
		jsonObjRevision.add("ObjectRevision",taskLatestRevision);
		jsonObjRevision.build();
		jsonMaturityObj.add("MaturityChange",jsonObj.build());
		jsonMaturityObj.build();
		JsonArrayBuilder jsonArry = Json.createArrayBuilder();
		jsonArry.add(jsonObjRevision);
		jsonArry.add(jsonMaturityObj);
		JsonObjectBuilder actionTakenObj = BPSJsonObjectBuilder.createJsonObjectBuilder(Json.createObjectBuilder());
		actionTakenObj.add("actionType", jsonArry.build());
		String actionTakenString=actionTakenObj.build().toString().replaceAll("\\\\", "");
		final Map<String, String> updates = new HashMap<String, String>(); 
		updates.put("ESignObjectRef",eSignTaskId);
		updates.put("ESignObjectServiceID", "3DSpace"); 
		updates.put("ESignObjectURI", "/v1/resources/task/");
		updates.put("ESignActionTaken", actionTakenString);  
		updates.put("eSignRecordId", eSignRecordId);
		Class objectTypeArray[] = new Class[2];
		objectTypeArray[0] =  context.getClass();
		objectTypeArray[1] = Map.class;
		Class<?> c = Class.forName("com.dassault_systemes.enovia.esign.ESignRecordUtil");
		Object eSignRecordUtil = c.newInstance();
		Method updateESignRecord = c.getMethod("UpdateESignRecord", objectTypeArray);
		updateESignRecord.invoke(eSignRecordUtil, context,updates);
	}catch(Exception e){
		System.out.println("Exception in updating eSign"+e.getMessage());
	}
}
%>

<%!
// method to get all tasks of next order whose due-dates are offset from Task Complete Event
public MapList getNextOrderOffsetTasks(Context context, DomainObject route, StringList relSelects, String sWhereExp) throws Exception{

MapList taskMapList = route.getRelatedObjects(context,
                               Route.RELATIONSHIP_ROUTE_NODE, //String relPattern
                                "*",                          //String typePattern
                               null,                         //StringList objectSelects,
                               relSelects,                     //StringList relationshipSelects,
                               false,                     //boolean getTo,
                               true,                     //boolean getFrom,
                               (short)1,                 //short recurseToLevel,
                               "",                       //String objectWhere,
                               sWhereExp,           //String relationshipWhere,
                               null,                     //Pattern includeType,
                               null,                     //Pattern includeRelationship,
                               null);                    //Map includeMap


return taskMapList;

}
%>

<%!
/*
  String from browser contains \n as well as \r this function removes \r
  from string for string comparison
*/
public String trimStr(String val)
{
   StringBuffer retBuffer=new StringBuffer();
   for(int i=0;i<val.length();i++)
   {
    if(val.charAt(i)!='\r'){
        retBuffer.append(val.charAt(i));
      }
   }
   return retBuffer.toString();
}
%>
