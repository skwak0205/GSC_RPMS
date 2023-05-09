/*
 *  emxInboxTaskNotificationBase.java
 *
 * Copyright (c) 1992-2020 Dassault Systemes.
 *
 * All Rights Reserved.
 * This program contains proprietary and trade secret information of
 * MatrixOne, Inc.  Copyright notice is precautionary only and does
 * not evidence any actual or intended publication of such program.
 *
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Collection;
import java.util.HashSet;
import matrix.db.Context;
import matrix.db.Group;
import matrix.db.JPO;
import matrix.db.Role;
import matrix.db.UserItr;
import matrix.util.MatrixException;
import matrix.util.StringList;

import com.matrixone.apps.common.InboxTask;
import com.matrixone.apps.common.RouteTaskNotification;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MessageUtil;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.PropertyUtil;
import com.matrixone.apps.domain.util.RoleUtil;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;


public class emxInboxTaskNotificationBase_mxJPO extends emxSpool_mxJPO
{
    /**
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since AEF Rossini
     * @grade 0
     */
    public emxInboxTaskNotificationBase_mxJPO (Context context, String[] args)
        throws Exception
    {
    	super(context, args);
    }

    /**
     * This method is executed if a specific method is not specified.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @returns nothing
     * @throws Exception if the operation fails
     * @since AEF Rossini
     * @grade 0
     */
    public int mxMain(Context context, String[] args)
        throws Exception
    {
        if (!context.isConnected())
            throw new Exception(ComponentsUtil.i18nStringNow("emxComponents.Generic.NotSupportedOnDesktopClient", context.getLocale().getLanguage()));
        return 0;
    }

    /**
     * Get the subject for the notification.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args[0] contains map of values for building the notification subject
     * @returns the Subject string
     * @throws Exception if the operation fails
     * @since V6R2012x
     */
   public String getSubject(Context context, String[] args) throws Exception {

	   Map info = (Map) JPO.unpackArgs(args);
       Map payload = (Map) info.get("payload");
       String subject = (String) payload.get("subject");
       String[] subjectKeys = (String[]) payload.get("subjectKeys");
       String[] subjectValues = (String[]) payload.get("subjectValues");
       String companyName = null;
       String basePropName = (String) info.get("bundleName");
       Locale locale = (Locale) info.get("locale");

       subject = MessageUtil.getMessage(context, null, subject, subjectKeys, subjectValues, companyName, locale, basePropName);
       
       return (subject);
   }
   
   /**
    * Get the html message for the notification.
    * Called to create email.
    *
    * @param context the eMatrix <code>Context</code> object
    * @param args[0] contains map of values for building the notification html
    * @returns the html message string
    * @throws Exception if the operation fails
    * @since V6R2012x
    */
   public String getMessageHTML(Context context, String[] args) throws Exception {

	   // get the message text
	   String message = getMessage(context, args);
	   
	   // add in some white space
	   if (message.length() > 0) {
		   message += "<br><br>";
	   }
	   
	   // build the table part of the message
	   Map info = (Map)JPO.unpackArgs(args);
	   Map payload = (Map) info.get("payload");
       String tableHeader = (String) payload.get("tableHeader");
       String tableRow = (String) payload.get("tableRow");
       String[] tableRowKeys = (String[]) payload.get("tableRowKeys");
       String[] tableRowValues = (String[]) payload.get("tableRowValues");
	   String clickMessage = (String) payload.get("click");
	   String[] clickKeys = (String[]) payload.get("clickKeys");
	   String[] clickValues = (String[]) payload.get("clickValues");
	   
	   String companyName = null;
	   String basePropName = (String) info.get("bundleName");
	   Locale locale = (Locale) info.get("locale");
	   for(int i=0;i<tableRowValues.length;i++){
		  if(tableRowValues[i].contains("\n")){
			  tableRowValues[i] = tableRowValues[i].replace("\n","</br>");
		  }
	   }
	   message += "<html><body>";
	   message += "<style>body, th, td {font-family:Verdana;font-size:11px;text-align:left;padding:5px;}</style>";
	   
	   if (clickMessage != null && clickMessage.length() > 0) {
		   clickMessage = MessageUtil.getMessage(context, null, clickMessage, clickKeys, clickValues, companyName, locale, basePropName);
	   }
       
	   if (clickMessage != null && clickMessage.length() > 0) {
    	   message += clickMessage;
    	   message += "<br><br>";
       }
       
	   tableHeader = MessageUtil.getMessage(context, tableHeader, companyName, locale, basePropName);
       if (tableHeader != null && tableHeader.length() > 0) {
    	   message += "<table border = 1><thead>" + tableHeader + "</thead><tbody><tr>";
    	   message += MessageUtil.getMessage(context, null, tableRow, tableRowKeys, tableRowValues, companyName, locale, basePropName);
    	   message += "</tr></tbody></table>";
       }
       
       message += "</body></html>";
       
       return (message);
   	}

   /**
    * Get the text message for the notification.
    * Called to generate icon mail.
    * 
    * @param context the eMatrix <code>Context</code> object
    * @param args contains map of values for building the notification text
    * @returns the text message string
    * @throws Exception if the operation fails
    * @since V6R2012x
    */
   public String getMessageText(Context context, String[] args) throws Exception {

	   Map info = (Map) JPO.unpackArgs(args);
       String basePropName = (String) info.get("bundleName");
       String baseURL = (String) info.get("baseURL");
       Locale locale = (Locale) info.get("locale");
       String objectId = (String) info.get("id");
       
       // get the message text
       String message = getMessage(context, args);
       // add in links for route, task and content
       message += emxMailUtil_mxJPO.getInboxTaskMailMessage(context, objectId, locale, basePropName, baseURL, null);
       return (message);
   }

   /**
    * Get the message for the notification.
    * 
    * @param context the eMatrix <code>Context</code> object
    * @param args contains map of values for building the notification text
    * @returns the text message string
    * @throws Exception if the operation fails
    * @since V6R2012x
    */
   private String getMessage(Context context, String[] args) throws Exception {

	   Map info = (Map) JPO.unpackArgs(args);
       Map payload = (Map) info.get("payload");
       String message = (String) payload.get("message");
       String[] messageKeys = (String[]) payload.get("messageKeys");
       String[] messageValues = (String[]) payload.get("messageValues");
       String companyName = null;
       String basePropName = (String) info.get("bundleName");
       Locale locale = (Locale) info.get("locale");
       
       message = MessageUtil.getMessage(context, null, message, messageKeys, messageValues, companyName, locale, basePropName);
       return (message);
   }
   
   	/**
	 * This method is used to get the name of the route task owner.
	 * 
	 * @param context the eMatrix <code>Context</code> object
     * @param args contains map of values for the route task
	 * @return StringList containing the name of the route task owner.
	 * @throws Exception if the operation fails
	 * @since V6R2012x
	 */  
	public StringList getRouteTaskOwner(Context context, String[] args) throws MatrixException {
		
		StringList routeTaskOwnerList = new StringList();
		final String ROLE_USER_GROUP = PropertyUtil.getSchemaProperty(context,"role_USERGROUPOWNER");
		String routeTaskOwner = null;
		try {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			String objectId = (String) programMap.get(SELECT_ID);
			DomainObject routeTask = DomainObject.newInstance(context, objectId);
			routeTaskOwner = routeTask.getInfo(context, SELECT_OWNER);
			if(routeTaskOwner.equalsIgnoreCase(ROLE_USER_GROUP))
			{
				String  sAttRouteTaskUser   = PropertyUtil.getSchemaProperty(context ,"attribute_RouteTaskUser");
			   routeTaskOwner = routeTask.getInfo(context, "from[" + DomainConstants.RELATIONSHIP_PROJECT_TASK+ "].to.name");
			   routeTaskOwnerList = getGroupOrRoleMembers(context, "User Group",routeTaskOwner);
			}else {
			String type = MqlUtil.mqlCommand(context, "print user $1 select $2 dump", routeTaskOwner, "type");
			// if owner is a group or role then get the members
			if ("group".equals(type) || "role".equals(type)) {
				routeTaskOwnerList = getGroupOrRoleMembers(context, type, routeTaskOwner);
			}
			else {
				if (routeTaskOwner != null) {
					routeTaskOwnerList.add(routeTaskOwner);
				}
			}
			}
		} catch(Exception e) {
			throw new MatrixException(e.getMessage());
		}
		
		return (routeTaskOwnerList);
	}

	/**
	 * This method is used to get the name of the route owner.
	 * 
	 * @param context the eMatrix <code>Context</code> object
     * @param args contains map of values for the route task
	 * @return StringList containing the name of the route owner.
	 * @throws Exception if the operation fails
	 * @since V6R2012x
	 */  
	public StringList getRouteOwner(Context context, String[] args) throws MatrixException {
		
		StringList routeOwnerList = new StringList();
		String routeOwner = null;
		try {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			String objectId = (String) programMap.get(SELECT_ID);
			DomainObject routeTask = DomainObject.newInstance(context, objectId);
			routeOwner = routeTask.getInfo(context, "from["+RELATIONSHIP_ROUTE_TASK+"].to.owner");
			
		} catch(Exception e) {
			throw new MatrixException(e.getMessage());
		}
		
		if (routeOwner != null) {
			routeOwnerList.add(routeOwner);
		}
		
		return (routeOwnerList);
	}
/**
 * This method is used to get the task assignee's names and Route Owner.
 * 
 * @param context the eMatrix <code>Context</code> object
 * @param args contains map of values for the route task
 * @return StringList containing the name of the tasks assignees and route owner.
 * @throws Exception if the operation fails
 * @since V6R2019x
 */  
public StringList getRejectionMailList(Context context, String[] args) throws MatrixException {
	
	StringList routeTaskAssigneeList = new StringList();
	final String ROLE_USER_GROUP = PropertyUtil.getSchemaProperty(context,"role_USERGROUPOWNER");
	final String TASK_ASSIGNEE_SELECT = "from["+DomainConstants.RELATIONSHIP_ROUTE_TASK+"].to.to["+DomainConstants.RELATIONSHIP_ROUTE_TASK+"].from.owner";
	final String ROUTE_OWNER = "from["+DomainConstants.RELATIONSHIP_ROUTE_TASK+"].to.owner";
	String taskOwner;
	String routeOwner;
	try {
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		String objectId = (String) programMap.get(SELECT_ID);
		DomainObject routeTask = DomainObject.newInstance(context, objectId);
		StringList selects = new StringList();
		selects.add(DomainConstants.SELECT_OWNER);
		selects.add(TASK_ASSIGNEE_SELECT);
		selects.add(ROUTE_OWNER);
		java.util.Collection<String> multiValueList = new HashSet<String>(2);
		multiValueList.add(TASK_ASSIGNEE_SELECT);
		Map resultMap = routeTask.getInfo(context, selects,multiValueList);
		Object assigneeList = (Object)resultMap.get(TASK_ASSIGNEE_SELECT);
		routeOwner = (String)resultMap.get(ROUTE_OWNER);
		taskOwner = (String)resultMap.get(DomainConstants.SELECT_OWNER);
		if(assigneeList instanceof StringList){
			StringList tempOwnerList = (StringList)assigneeList;
			for(String temptaskOwner : tempOwnerList){
				if(!temptaskOwner.equalsIgnoreCase(ROLE_USER_GROUP) && !routeTaskAssigneeList.contains(temptaskOwner)){
				{
					routeTaskAssigneeList.add(temptaskOwner);
				}
			}
		}
		}
	} catch(Exception e) {
		throw new MatrixException(e.getMessage());
	}	
	if(routeTaskAssigneeList.contains(routeOwner)){
		routeTaskAssigneeList.remove(routeOwner);
	}
	if(routeTaskAssigneeList.contains(taskOwner)){
		routeTaskAssigneeList.remove(taskOwner);
	}
	return (routeTaskAssigneeList);
}
	/**
	 * This method is used to get the notification agent.
	 * 
	 * @param context the eMatrix <code>Context</code> object
     * @param args contains map of values for the route task
	 * @return String containing the name of the context user.
	 * @throws Exception if the operation fails
	 * @since V6R2012x
	 */  
	public String getNotificationAgent(Context context, String[] args) throws MatrixException {
		
		String agent = null;
		
		try {
			agent = emxMailUtil_mxJPO.getAgentName(context, new String[] {});
		}
		catch (Exception e) {
			throw (new MatrixException(e));
		}
		
		if (agent == null || agent.length() == 0) {
			agent = context.getUser();
		}
			
		return (agent);
	}

	/**
	 * This method consolidates the given Notification Requests assuming these are similar notifications.
	 * For now it merges the Attachments.
	 *
	 * @param mapSrcNotificationRequest Map of notification request which is to be merged
	 * @param mapDestNotificationRequest Map of notification request in which to be merged.
	 * This will be modified as a result.
	 * @return void
	 * @since R212
	 */
	protected void mergeSimilarNotification(Map mapSrcNotificationRequest, Map mapDestNotificationRequest, 
											String[] fieldKeys, String[] fieldSeparators)
	{
		if (mapDestNotificationRequest != null && mapSrcNotificationRequest != null) {

			for (int i = 0, j = 0; i < fieldKeys.length && j < fieldSeparators.length; i++, j++) {
				
				String strDest = (String) mapDestNotificationRequest.get(fieldKeys[i]);
				String strSrc = (String) mapSrcNotificationRequest.get(fieldKeys[i]);
				
				if (strDest == null) {
					strDest = "";
				}
				if (strSrc == null) {
					strSrc = "";
				}
				
				if (strSrc.length() != 0) {
					// if this is the body html then do something different
					if (fieldKeys[i].equals(SELECT_ATTRIBUTE_BODY_HTML)) {
						// use only the html portion of the destination string
						strDest = lastSubString(strDest, "<html>", "</html>");
						
						// compare table headers and use the longer
						String strSrcHead = lastSubString(strSrc, "<thead>", "</thead>");
						String strDestHead = lastSubString(strDest, "<thead>", "</thead>");
						if (strSrcHead.length() > strDestHead.length()) {
							int insertPos = strDest.indexOf("<thead>");
							strDest = strDest.substring(0, insertPos) + strSrcHead + strDest.substring(insertPos + strDestHead.length());
						}
						
						// find the last </tr> in the destination string
						int indexDest = strDest.lastIndexOf("</tr>");
						if (indexDest != -1) {
							indexDest += "</tr>".length(); 
						
							// find the table row in the source message
							String tableRow = lastSubString(strSrc, "<tr>", "</tr>");
							if (tableRow != null) {
								// insert into the destination table
								strDest = strDest.substring(0, indexDest) + tableRow + strDest.substring(indexDest);
							}
						}
					}
					else {
						// if not the first message then append the separator
						if (strDest.length() != 0) {
							strDest += fieldSeparators[j];
						}
						strDest += strSrc;
					}
					
					mapDestNotificationRequest.put(fieldKeys[i], strDest);
				}
			}
		}
	}

   	/**
	 * Retrieves the last substring of the source wrapped by the 
	 * begin and end strings.  The begin and end strings are 
	 * included in the result.  Used to extract the last table row
	 * from a notification so that it can be merged into another 
	 * notification table.
	 * 
	 * @param source the source string
     * @param begin the beginning of the string to extract
     * @param end the end of the string to extract 
	 * @return String the resulting string, null if unable to find begin or end
	 * @throws Exception if the operation fails
	 * @since V6R2012x
	 */  
	private String lastSubString(String source, String begin, String end) {
		String result = null;
		
		if (source != null && source.length() > 0 && begin != null && begin.length() > 0 && end != null && end.length() > 0) {
			int beginIndex = source.lastIndexOf(begin);
			if (beginIndex != -1) {
				int endIndex = source.lastIndexOf(end);
				if (endIndex != -1) {
					endIndex += end.length();
					result = source.substring(beginIndex, endIndex);
				}
			}
		}
		
		return (result);
	}

   	/**
	 * Returns the list of members for the given group or role.
	 * 
	 * @param context the eMatrix <code>Context</code> object
     * @param groupOrRole contains either "group" or "role"
     * @param groupOrRoleName name of the group or role
	 * @return StringList the list of members of the group or role
	 * @throws Exception if the operation fails
	 * @since V6R2012x
	 */  
	private StringList getGroupOrRoleMembers(Context context, String groupOrRole, String groupOrRoleName)
		throws Exception {
        
		StringList members = new StringList();
		String[] assigned;
		if ("role".equalsIgnoreCase(groupOrRole)) {
			Role role = new Role(groupOrRoleName);
			role.open(context);
			UserItr itr = new UserItr(role.getAssignments(context));
			while (itr.next()) {
				if (itr.obj() instanceof matrix.db.Person) {
					members.add(itr.obj().getName());
				}
			}
			//if members is empty, there might be a case when it is OCDX role and its children role can be 
			// assigned directly.
			if(members.isEmpty()){
				StringList roles = new StringList();
            	roles.add(groupOrRoleName);
            	String allChildrenRoles = RoleUtil.getVPLMChildrenRoleList(context, roles);
            	assigned = allChildrenRoles.split(",");
            	for(int i=0;i<assigned.length;i++){            		
            		role = new Role(assigned[i]);
            		role.open(context);
            		itr = new UserItr(role.getAssignments(context));
                    while(itr.next())
                    {
                    	if (itr.obj() instanceof matrix.db.Person)
                    	{
                    		members.add(itr.obj().getName());
                    	}
                    }
            	}
			}
			role.close(context);
		}  
		else if ("group".equalsIgnoreCase(groupOrRole)){ 
			Group group = new Group(groupOrRoleName);
			group.open(context);
			UserItr itr = new UserItr(group.getAssignments(context));
			while (itr.next()) {
				if (itr.obj() instanceof matrix.db.Person) {
					members.add(itr.obj().getName());
				}
			}
			group.close(context);
		}else if("User Group".equalsIgnoreCase(groupOrRole)){
			MapList mlMembers = FrameworkUtil.getProjectGroupAssignees(context, groupOrRoleName);
			Map mapTaskInfo ;
			for (Iterator itrTasks = mlMembers.iterator(); itrTasks.hasNext();) {
				mapTaskInfo = (Map)itrTasks.next();
				members.add((String)mapTaskInfo.get(DomainConstants.SELECT_NAME));
			}
		}

		return (members);
	}
	
	/**
	 * This method is used to get 'to list' for Route Task Notification.
	 * 
	 * @param context the eMatrix <code>Context</code> object
     * @param args contains map of values for the route task, mainly needs toList sent in payload
	 * @return StringList containing the names of toList persons.
	 * @throws Exception if the operation fails
	 * @mergedFrom V6R2017xHF10 to V6R2019xGA
	 */  
	public StringList getToListFromPayload(Context context, String[] args) throws Exception {
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap payload = (HashMap) programMap.get("payload");
		
		return (new StringList((String[]) payload.get("toList")));
	}
	
	/**
	 * This method is used to get 'cc list' for Route Task Notification.
	 * 
	 * @param context the eMatrix <code>Context</code> object
     * @param args contains map of values for the route task, mainly needs toList sent in payload
	 * @return StringList containing the names of ccList persons.
	 * @throws Exception if the operation fails
	 * @mergedFrom V6R2017xHF10 to V6R2019xGA
	 */  
	public StringList getCcListFromPayload(Context context, String[] args) throws Exception {
		StringList retList = new StringList();
		
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap payload = (HashMap) programMap.get("payload");
		String[] ccList = (String[]) payload.get("ccList");
		if(ccList != null && ccList.length > 0) {
			retList = new StringList(ccList);
		}
		
		return retList;
	}

	// This method is used to send notification to Route owner to review, when an assignee approves a Task
	// Merged from V6R2017xHF10 to V6R2019xGA
	public static int sendReviewInitiatedNotification(Context context, String[] args) throws Exception {
		if (args == null || args.length < 1) {
			throw (new IllegalArgumentException());
		}
		
		String SELECT_REVIEW_TASK = "attribute[" + PropertyUtil.getSchemaProperty(context, "attribute_ReviewTask") + "]";
		String SELECT_SCHEDULED_COMPLETION_DATE = "attribute[" + PropertyUtil.getSchemaProperty(context, "attribute_ScheduledCompletionDate") + "]";
		String SELECT_COMMENTS = "attribute[" + PropertyUtil.getSchemaProperty(context, "attribute_Comments") +"]";
		String typeInboxTask = EnoviaResourceBundle.getProperty(context,"emxFrameworkStringResource",context.getLocale(),"emxFramework.Type.Inbox_Task");
		String  sAttRouteAction                = PropertyUtil.getSchemaProperty("attribute_RouteAction");
		String  sAttApprovalStatus             = PropertyUtil.getSchemaProperty("attribute_ApprovalStatus");
		
		String objectId = args[0];
		String expression = SELECT_REVIEW_TASK + "==Yes";
		String filterResult = MqlUtil.mqlCommand(context, "evaluate expression $1 on bus $2", expression, objectId);
		if (filterResult.startsWith("FALSE")) {
			return 0;
		}
		
		DomainObject task = new DomainObject(objectId);
		String selRouteId = "from[" + RELATIONSHIP_ROUTE_TASK + "].to.id";
		String selRouteType = "from[" + RELATIONSHIP_ROUTE_TASK + "].to.type";
		String selRouteName = "from[" + RELATIONSHIP_ROUTE_TASK + "].to.name";
		String selRouteTitle = "from[" + RELATIONSHIP_ROUTE_TASK + "].to.attribute[Title]";
		String selRouteOwner = "from[" + RELATIONSHIP_ROUTE_TASK + "].to.owner";
		StringList busSel = new StringList();
		busSel.add(SELECT_TYPE);
		busSel.add(SELECT_OWNER);
		busSel.add(selRouteId);
		busSel.add(selRouteType);
		busSel.add(selRouteName);
		busSel.add(selRouteTitle);
		busSel.add(selRouteOwner);
		busSel.add(SELECT_NAME);
		busSel.add(InboxTask.SELECT_TITLE);
		busSel.add(SELECT_SCHEDULED_COMPLETION_DATE);
		busSel.add(SELECT_COMMENTS);
		busSel.add("attribute["+sAttRouteAction+"]");
		busSel.add("attribute["+sAttApprovalStatus+"]");
		busSel.add(DomainConstants.SELECT_PHYSICAL_ID);
		Map<String, String> taskInfo = task.getInfo(context, busSel);
		
		String strRouteId = taskInfo.get(selRouteId);
		String routePhysicalId = FrameworkUtil.getPIDfromOID(context, strRouteId);
		DomainObject route = new DomainObject(strRouteId);
		
		busSel.clear();
		busSel.add(SELECT_ID);
		busSel.add(SELECT_NAME);
		busSel.add(SELECT_DESCRIPTION);
		busSel.add(SELECT_TYPE);
		busSel.add(SELECT_ATTRIBUTE_TITLE);
		MapList routeContents = route.getRelatedObjects(context,                    // matrix context
                									RELATIONSHIP_OBJECT_ROUTE,  // relationship pattern
                									"*",                        // object pattern
                									busSel,						// object selects
                									EMPTY_STRINGLIST,           // relationship selects
                									true,                       // to direction
                									false,                      // from direction
                									(short) 1,                  // recursion level
                									EMPTY_STRING,               // object where clause
                									EMPTY_STRING,				// relationship where clause
                									(int) 0);					// limit of objects to be returned
		routeContents.addSortKey(DomainConstants.SELECT_NAME, DomainConstants.SortDirection.ASCENDING.toString(), String.class.getSimpleName());
		routeContents.sort();
		
		// form Content with Description data for mail notifications - start
        boolean bShowRouteContent = (routeContents.size() > 0);
        StringBuffer sRouteContent = new StringBuffer();
        StringBuffer routeContent3D = new StringBuffer();
        StringBuffer routeContent3DIds = new StringBuffer();
        StringBuffer routeContent3DTypes = new StringBuffer();
        StringBuffer routeContent3DTitles = new StringBuffer();
        StringBuffer sRouteContentDescription = new StringBuffer();
        for(int i = 0; i < routeContents.size(); i++) {
        	// Map of the Objects
        	Map<String, String> routeContent = (Map<String, String>) routeContents.get(i);

        	// build content links for notification table
			if (i > 0 && i< routeContents.size()) {
        		sRouteContent.append("<br/><br/>");
        		sRouteContentDescription.append("<br/><br/>");
				routeContent3D.append(", ");
				routeContent3DIds.append(", ");
				routeContent3DTypes.append(", ");
				routeContent3DTitles.append(", ");
        	}
        	routeContent3D.append((String) routeContent.get(SELECT_NAME));
        	routeContent3DIds.append((String) routeContent.get(SELECT_ID));
        	routeContent3DTypes.append((String) routeContent.get(SELECT_TYPE));
        	routeContent3DTitles.append((String) routeContent.get(SELECT_ATTRIBUTE_TITLE));
        	sRouteContent.append(emxNotificationUtil_mxJPO.getObjectLinkHTML(context, routeContent.get(SELECT_NAME), routeContent.get(SELECT_ID)));

        	sRouteContentDescription.append(FrameworkUtil.stripString((String) routeContent.get(DomainConstants.SELECT_DESCRIPTION), InboxTask.MAX_LEN_OF_CNT_DESC_FOR_ROUTE_NOTIFICATIONS, FrameworkUtil.StripStringType.WORD_STRIP));
        }
        // form Content with Description data for mail notifications - end
		String mailEnabled = EnoviaResourceBundle.getProperty(context,"emxFramework.Routes.EnableMails");
		
        Map payload = new HashMap();
        payload.put("subject", "emxFramework.Notification.NotifyRouteOwner.SubjectReviewInitiated");
        payload.put("message", "emxFramework.Notification.NotifyRouteOwner.BodyReviewInitiated");
        String[] messageKeys = {"type", "name", "RType", "RName"};
        String[] messageValues = {typeInboxTask, taskInfo.get(SELECT_NAME), taskInfo.get(selRouteType), taskInfo.get(selRouteName)};
        payload.put("messageKeys", messageKeys);
        payload.put("messageValues", messageValues);
        
        String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, taskInfo.get(SELECT_NAME), taskInfo.get(SELECT_ID));
        String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, taskInfo.get(selRouteName), taskInfo.get(selRouteId));
        
        if(bShowRouteContent) {
        	payload.put("tableHeader", "emxFramework.Notification.NotifyRouteOwner.ReviewInitiated.TableWithContentHeader");
        	payload.put("tableRow", "emxFramework.Notification.NotifyRouteOwner.ReviewInitiated.TableWithContentData");
        	
        	String[] tableRowKeys = new String[]{"TaskType", "RouteName", "TaskName", "TaskOwner", "DueDate", "Comments", "Content", "ContentDescription"};
        	payload.put("tableRowKeys", tableRowKeys);
        	String[] tableRowValues = new String[]{typeInboxTask, sRouteLink, sInboxTaskLink, taskInfo.get(SELECT_OWNER), taskInfo.get(SELECT_SCHEDULED_COMPLETION_DATE), taskInfo.get(SELECT_COMMENTS), sRouteContent.toString(), sRouteContentDescription.toString()};
        	payload.put("tableRowValues", tableRowValues);
        } else {
        	payload.put("tableHeader", "emxFramework.Notification.NotifyRouteOwner.ReviewInitiated.TableHeader");
            payload.put("tableRow", "emxFramework.Notification.NotifyRouteOwner.ReviewInitiated.TableData");
            
            String[] tableRowKeys = new String[]{"TaskType", "RouteName", "TaskName", "TaskOwner", "DueDate", "Comments"};
        	payload.put("tableRowKeys", tableRowKeys);
        	String[] tableRowValues = new String[]{typeInboxTask, sRouteLink, sInboxTaskLink, taskInfo.get(SELECT_OWNER), taskInfo.get(SELECT_SCHEDULED_COMPLETION_DATE), taskInfo.get(SELECT_COMMENTS)};
        	payload.put("tableRowValues", tableRowValues);
        }

        payload.put("treeMenu", new String[1]);
        payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
        String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, new String[1]);
        
        String[] clickKeys = {"url", "tenantinfo"};
        String[] clickValues = {sBaseURL, ""};
        String tenantId = context.getTenant();
        if(UIUtil.isNotNullAndNotEmpty(tenantId)){
        	clickValues[1] = "&tenant="+tenantId;
        }
        payload.put("clickKeys", clickKeys);
        payload.put("clickValues", clickValues);
        String[] toList = {taskInfo.get(selRouteOwner)};
        payload.put("toList", toList);

        emxMailUtil_mxJPO.setTreeMenuName(context, new String[1]);
        String oldAgentName = emxMailUtil_mxJPO.getAgentName(context, new String[1]);
        emxMailUtil_mxJPO.setAgentName(context, new String[] {taskInfo.get(SELECT_OWNER)});
			if("true".equalsIgnoreCase(mailEnabled)){
				emxNotificationUtil_mxJPO.objectNotification(context, objectId, "APPRouteTaskDelegatedEvent", payload,"both"); // using APPRouteTaskDelegatedEvent notification object, as we want to mention toList and ccList in payload explicitly
			}else {
				emxNotificationUtil_mxJPO.objectNotification(context, objectId, "APPRouteTaskDelegatedEvent", payload,"iconmail");
			}
        emxMailUtil_mxJPO.setAgentName(context, new String[] {oldAgentName});
        
		String taskTitle = (String)taskInfo.get(InboxTask.SELECT_TITLE);
		if(UIUtil.isNullOrEmpty(taskTitle)){
			taskTitle = (String)taskInfo.get(InboxTask.SELECT_NAME);
		}
		Map<String, String> taskAndRouteInfo = new HashMap<>();
		taskAndRouteInfo.put(RouteTaskNotification.ROUTE_ID, routePhysicalId);
		taskAndRouteInfo.put(RouteTaskNotification.ROUTE_NAME, (String)taskInfo.get(selRouteTitle));
		taskAndRouteInfo.put(RouteTaskNotification.ROUTE_OWNER, (String)taskInfo.get(selRouteOwner));
		taskAndRouteInfo.put(RouteTaskNotification.TASK_ID, (String)taskInfo.get(DomainConstants.SELECT_PHYSICAL_ID));
		taskAndRouteInfo.put(RouteTaskNotification.TASK_NAME, taskTitle);
		taskAndRouteInfo.put(RouteTaskNotification.TASK_ASSIGNEE, (String)taskInfo.get(SELECT_OWNER));
		taskAndRouteInfo.put(RouteTaskNotification.APPROVE_COMMENTS, (String)taskInfo.get(SELECT_COMMENTS));
		taskAndRouteInfo.put(RouteTaskNotification.TASK_ACTION, (String)taskInfo.get("attribute["+sAttRouteAction+"]"));
		taskAndRouteInfo.put(RouteTaskNotification.APPROVE_COMMENTS, (String)taskInfo.get(SELECT_COMMENTS));
		taskAndRouteInfo.put(RouteTaskNotification.ROUTE_CONTENTS, routeContent3D.toString());
		taskAndRouteInfo.put(RouteTaskNotification.ROUTE_CONTENT_IDS, routeContent3DIds.toString());
		taskAndRouteInfo.put(RouteTaskNotification.ROUTE_CONTENT_TITLES, routeContent3DTitles.toString());
		taskAndRouteInfo.put(RouteTaskNotification.ROUTE_CONTENT_TYPES, routeContent3DTypes.toString());
		taskAndRouteInfo.put(RouteTaskNotification.TASK_ATTACHMENTS, InboxTask.getTaskAttachment(context, (String)taskInfo.get(DomainConstants.SELECT_PHYSICAL_ID)));
		RouteTaskNotification notifObj = RouteTaskNotification.getInstance(context, routeContent3DTypes.toString());
		notifObj.sendTaskReviewInitiated3DNotification(context, taskAndRouteInfo);

		return 0;
	}
	
}
