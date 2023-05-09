/*
**   emxCommonInitiateRouteBase.java
**
**   Copyright (c) 1992-2020 Dassault Systemes.
**   All Rights Reserved.
**   This program contains proprietary and trade secret information of MatrixOne,
**   Inc.  Copyright notice is precautionary only
**   and does not evidence any actual or intended publication of such program
**
*/
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import matrix.db.Context;
import matrix.db.Group;
import matrix.db.JPO;
import matrix.db.MQLCommand;
import matrix.db.UserItr;
import matrix.util.SelectList;
import matrix.util.StringList;

import com.matrixone.apps.common.InboxTask;
import com.matrixone.apps.common.Organization;
import com.matrixone.apps.common.Route;
import com.matrixone.apps.common.RouteTaskNotification;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainAccess;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.PersonUtil;
import com.matrixone.apps.domain.util.PropertyUtil;
import com.matrixone.apps.domain.util.eMatrixDateFormat;
import com.matrixone.apps.domain.util.i18nNow;
import com.matrixone.apps.domain.util.MessageUtil;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.enovia.bps.notifications.NotificationUtil;



/**
 * The <code>emxCommonInitiateRouteBase</code> class contains
 *
 * @version AEF  10 Minor 1 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxCommonInitiateRouteBase_mxJPO
{
    public static final String  sRelRouteNode                =  DomainConstants.RELATIONSHIP_ROUTE_NODE;
    public static final String  sRelRouteTask                =  PropertyUtil.getSchemaProperty("relationship_RouteTask");
    public static final String  sRelProjectTask              =  PropertyUtil.getSchemaProperty("relationship_ProjectTask");
    public static final String  sRelObjectRoute              =  PropertyUtil.getSchemaProperty("relationship_ObjectRoute");
    public static final String  sAttRouteSequence            =  PropertyUtil.getSchemaProperty("attribute_RouteSequence");
    public static final String  sAttRouteAction              =  PropertyUtil.getSchemaProperty("attribute_RouteAction");
    public static final String  sAttRouteInstructions        =  PropertyUtil.getSchemaProperty("attribute_RouteInstructions");
    public static final String  sAttApprovalStatus           =  PropertyUtil.getSchemaProperty("attribute_ApprovalStatus");
    public static final String  sAttScheduledCompletionDate  =  PropertyUtil.getSchemaProperty("attribute_ScheduledCompletionDate");
    public static final String  sAttApproversResponsibility  =  PropertyUtil.getSchemaProperty("attribute_ApproversResponsibility");
    public static final String  sAttRouteNodeID              =  PropertyUtil.getSchemaProperty("attribute_RouteNodeID");
    public static final String  sAttCurrentRouteNode         =  PropertyUtil.getSchemaProperty("attribute_CurrentRouteNode");
    public static final String  sAttRouteStatus              =  PropertyUtil.getSchemaProperty("attribute_RouteStatus");
    public static final String  sAttTitle                    =  PropertyUtil.getSchemaProperty("attribute_Title");
    public static final String  sAttAllowDelegation          =  PropertyUtil.getSchemaProperty("attribute_AllowDelegation");
    public static final String  sAttReviewTask               =  PropertyUtil.getSchemaProperty("attribute_ReviewTask");
    public static final String  sAttAssigneeDueDateOpt       =  PropertyUtil.getSchemaProperty("attribute_AssigneeSetDueDate");
    public static final String  sAttDueDateOffset            =  PropertyUtil.getSchemaProperty("attribute_DueDateOffset");
    public static final String  sAttDueDateOffsetFrom        =  PropertyUtil.getSchemaProperty("attribute_DateOffsetFrom");
    public static final String  sAttTemplateTask             =  PropertyUtil.getSchemaProperty("attribute_TemplateTask");
    public static final String  sAttFirstName                =  PropertyUtil.getSchemaProperty("attribute_FirstName");
    public static final String  sAttLastName                 =  PropertyUtil.getSchemaProperty("attribute_LastName");
    public static final String  sAttAbsenceStartDate         =  PropertyUtil.getSchemaProperty("attribute_AbsenceStartDate");
    public static final String  sAttAbsenceEndDate           =  PropertyUtil.getSchemaProperty("attribute_AbsenceEndDate");
    public static final String  sAttAbsenceDelegate          =  PropertyUtil.getSchemaProperty("attribute_AbsenceDelegate");
    public static final String  sAttRouteTaskUser            =  PropertyUtil.getSchemaProperty("attribute_RouteTaskUser");
    public static final String  sAttRouteTaskUserCompany     =  PropertyUtil.getSchemaProperty("attribute_RouteTaskUserCompany");
    public static final String  sTypePerson                  =  PropertyUtil.getSchemaProperty("type_Person");
    public static final String sAttrRouteOwnerUGChoice = PropertyUtil.getSchemaProperty("attribute_RouteOwnerUGChoice");
    public static final String sAttrRouteOwnerTask = PropertyUtil.getSchemaProperty("attribute_RouteOwnerTask"); 
    public static final String  sTypeRouteTaskUser           =  PropertyUtil.getSchemaProperty("type_RouteTaskUser");
    public static final String  sWorkspaceAccessGrantor      =  PropertyUtil.getSchemaProperty("person_WorkspaceAccessGrantor");
    public static final String  sRouteAccessGrantor          =  PropertyUtil.getSchemaProperty("person_RouteAccessGrantor");
    public static final String  sRouteDelegationGrantor      =  PropertyUtil.getSchemaProperty("person_RouteDelegationGrantor");
	public static final String 	sRoutePreserveTaskOwner      =  PropertyUtil.getSchemaProperty("attribute_PreserveTaskOwner");
	/* Added for Choose users from User Group assignee implementation */
	 public static final String  sAttUsreGroupAction        =  PropertyUtil.getSchemaProperty("attribute_UserGroupAction");
	 public static final String  sAttUserGroupInfo        =  PropertyUtil.getSchemaProperty("attribute_UserGroupLevelInfo");
	 public static final String  sAttChooseUsersFromUG        =  PropertyUtil.getSchemaProperty("attribute_ChooseUsersFromUserGroup");
	 //done
    public static final String  typeTask                     = "type_InboxTask";
    public static final String  policy                       = PropertyUtil.getSchemaProperty("policy_InboxTask");

    //adeed for 307078
    public static final String  sSuperUser      =  PropertyUtil.getSchemaProperty("person_UserAgent");
    //till here

    // added for IR - 043921V6R2011
    public static final String  SELECT_OWNING_ORG_ID          =  "from[" + DomainConstants.RELATIONSHIP_INITIATING_ROUTE_TEMPLATE 
                                                     + "].to.to[" + DomainConstants.RELATIONSHIP_OWNING_ORGANIZATION + "].from.id";


    /**
     * Constructor.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since AEF 10 minor 1
     * @grade 0
     */
    public emxCommonInitiateRouteBase_mxJPO (Context context, String[] args)
        throws Exception
    {
    }

    /**
     * This method is executed if a specific method is not specified.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @returns nothing
     * @throws Exception if the operation fails
     * @since AEF 10 minor 1
     */
    public int mxMain(Context context, String[] args)
        throws Exception
    {
        if (true)
        {
            throw new Exception(ComponentsUtil.i18nStringNow("emxComponents.CommonInitiateRoute.SpecifyMethodOnInitiateRouteInvocation", context.getLocale().getLanguage()));
        }
        return 0;
    }

    /**
     * eServicecommonInitiateRoute method to remove the proicess from the tcl
     *
     * The method basically performs following high level operations.
     * It checks on each route level if the task is required to be activated.
     * If the task is to be activated then it is decided if the new task object is to be created or the old incomplete task s are to be reused.
     * If for a particular level there exists a completed task object, then it is revised. This will preserve the approval history.
     * Then it will take care of absense delegation mechanism if the assignee is absent and absense delegation is configured for him.
     * The task is then assigned to the assignee and appropriate notifications are sent across.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     * @returns booelan
     * @throws Exception if the operation fails
     * @since AEF 10 minor 1
     */
     public static int InitiateRoute (Context context,String args[])
     {
        String user=context.getUser();
		final String INITIAL_USER_GROUP_REL  = PropertyUtil.getSchemaProperty(context, "relationship_CandidateAssignee");
		final String SELECT_INITIAL_USER_GROUP_ID = "from[" + INITIAL_USER_GROUP_REL+"].to.id";
		final String PROXY_GROUP_TYPE = PropertyUtil.getSchemaProperty(context,"type_GroupProxy");
		final String GROUP_TYPE = PropertyUtil.getSchemaProperty(context,"type_Group");
		final String ROLE_USER_GROUP = PropertyUtil.getSchemaProperty(context,"role_USERGROUPOWNER");
        //varibales Declaration
        String sRouteNodeId             = "";
        String sRouteNodeObjectId       = "";
        String sRouteAction             = "";
        String sScheduledCompletionDate = "";
        String sApproversResponsibility = "";
        String sTitle                   = "";
        String sAllowDelegation         = "";
        String sReviewTask              = "";
        String sAssigneeDueDateOpt      = "";
        String sDueDateOffString        = "";
        String sRouteOwnerTask          = "FALSE";
        String sRouteOwnerUserGroup     = "";
		String sRoutePreserveOwner      = "";
        String sDueDateOffsetFrom       = "";
        String sTemplateTask            = "";
        String sRouteTaskUser           = "";
        String sRouteTaskUserCompany    = "";
        String sRouteNodeIdAttr         = "";
        String sPersonId                = "";
        String sPersonName              = "";
        String sPersonVault             = "";
        String sAbsenceDelegate         = "";
        String sPersonFirstName         = "";
        String sPersonLastName          = "";
        /* Added for Choose users from User Group assignee implementation */
        String sUserGroupAction = "";
        String sUserGroupInfo = "";
        String sChooseUsersFromUG = "";
        //End
        String sRouteInstructions = "";
        String sRPEType = "";
        String sRPEName  = "";
        String sRPERev  = "";
		String strTaskAssignTo = "";
        String strMQL = "";
        String sDelegatorName = "";
        String sDelegatorId   = "";
        String sDelegatorFullName ="";
        String sGroupOrRole = "";
        String sGroupOrRoleTitle = "";
        String sGRName ="";
        String sRPEOwner = "";
        String sRouteTaskUserValue ="";
        String sObjectId= "";
        String lGrantor ="";
        String lGrantee = "";
        String lGranteeAccess ="";
        String sInboxTaskId   ="";
		String sInboxTaskPhysicalId   ="";
        String sInboxTaskType ="";
        String sInboxTaskName  ="";
		String sInboxTaskTitle = "";
        String sInboxTaskRev  ="";
        String sRouteObjects= "";
        String sRouteContent= "";
        String sRouteContentDescription= "";
        int bPersonFound=0;
        String Message = "";
        int nCurrentRouteSequenceStarted = 0;
        String sRouteId = null;
		String sRoutePhysicalId = null;
		//Added for the Bug No: 339357 3 11/20/2007 Begin 
		String strLanguage = "";
		String aliasRoleName= "";
		//Added for the Bug No: 339357 3 11/20/2007 End 

        // Added for IR-043921V6R2011
        String sRoleOrGroupToList = null;

// Start: Resume Process Implementation
        final String POLICY_INBOX_TASK_STATE_COMPLETE = PropertyUtil.getSchemaProperty(context, "Policy", DomainObject.POLICY_INBOX_TASK, "state_Complete");
        boolean isTaskConnectedToRoute = false;
        Map mapTaskInfo = null;
        Map mapAttributesToReset = null;
        String strOldTaskId = null;
        String strOldTaskState = null;
        String strRoleOrGroup = null;
        DomainObject dmoOldTask = null;
        DomainObject dmoNewTask = null;

        StringList slTaskSelect = new StringList(DomainObject.SELECT_ID);
        slTaskSelect.add(DomainObject.SELECT_TYPE);
        slTaskSelect.add(DomainObject.SELECT_NAME);
        slTaskSelect.add(DomainObject.SELECT_REVISION);
        slTaskSelect.add(DomainObject.SELECT_CURRENT);
		slTaskSelect.add(InboxTask.SELECT_TITLE);
        slTaskSelect.add("from[" + DomainObject.RELATIONSHIP_PROJECT_TASK + "].to.id");
		slTaskSelect.add(SELECT_INITIAL_USER_GROUP_ID);
		slTaskSelect.add(DomainConstants.SELECT_PHYSICAL_ID);
//End: Resume Process Implementation
		String mailEnabled = null;
		String notifyType = "iconmail";
        
        try {
            ContextUtil.startTransaction(context, true);
            // getting the type from arguments[0]
            String sType = args[0];
            // getting the Name from arguments[1]
            String sName = args[1];
            // getting the Revision from arguments[2]
            String sRev = args[2];
			mailEnabled = EnoviaResourceBundle.getProperty(context,"emxFramework.Routes.EnableMails");
			if("true".equalsIgnoreCase(mailEnabled)){
				notifyType = "both";
			}
            // The Task Sequence to Execute
            int sRouteSequenceValue = Integer.parseInt(args[3]);
            int bErrorOnNoConnection = Integer.parseInt(args[4]);

            // declaring String array because emxMailUtil JPO needs treeMenu as
            // a String array
            String sTreeMenu[] = new String[1];
            // Building the Task Object
            DomainObject inboxTask = new DomainObject();

            // Building the Object of the Mail Util to be used Later
            emxMailUtil_mxJPO mailUtil = new emxMailUtil_mxJPO(context, null);
            // Building the Object of the emxcommonPushPopShadowAgent to be used
            // Later
            emxcommonPushPopShadowAgent_mxJPO PushPopShadowAgent = new emxcommonPushPopShadowAgent_mxJPO(
                    context, null);
            // to get the Route Id and the Route Owner

			String mqlret = MqlUtil.mqlCommand(context, "print bus $1 $2 $3 select $4 $5 $6 $7 $8 $9 dump $10", sType, sName, sRev, "id", "owner",DomainConstants.SELECT_PHYSICAL_ID,DomainConstants.SELECT_ORGANIZATION, DomainConstants.SELECT_PROJECT,DomainConstants.SELECT_ATTRIBUTE_TITLE, "|");
            // getting the Route Id from the output
			StringList values = FrameworkUtil.split(mqlret, "|");
			sRouteId = values.get(0);
            // getting the Route Owner
			String sRouteOwner = values.get(1);
			sRoutePhysicalId = values.get(2);
			String routeOrg = values.get(3);
			String routeProject = values.get(4);
			String routeTitle = values.get(5);
            // building the Route Object for getting the Related Objects using
            // getRelatedObjects method
            DomainObject RouteObject = new DomainObject(sRouteId);
            // objects Selects
            StringList objectSelects = new StringList();
            objectSelects.addElement(DomainConstants.SELECT_ID);
            objectSelects.addElement(DomainConstants.SELECT_TYPE);
            objectSelects.addElement(DomainConstants.SELECT_NAME);
            objectSelects.addElement(DomainConstants.SELECT_REVISION);
            objectSelects.addElement(DomainConstants.SELECT_DESCRIPTION);
            objectSelects.addElement(DomainConstants.SELECT_ATTRIBUTE_TITLE);
			objectSelects.addElement("attribute["+ sRoutePreserveTaskOwner + "]");
            // relationship Selects
            StringList relationshipSelects = new StringList();
            // String Buffer to append the type name Revision of the Objects
            // which are connected to Route
            // by Object Route Relationship

            StringBuffer sBufRouteObjects = new StringBuffer(64);
            // getting Objects which are Connected with object Route
            // Relationship
            MapList sRouteObjectsMaplist = RouteObject.getRelatedObjects(context,
                                                                    sRelObjectRoute,
                                                                    "*",
                                                                    objectSelects,
                                                                    relationshipSelects,
                                                                    true,
                                                                    false,
                                                                    (short)1,
                                                                    "",
                                                                    "");
            
            sRouteObjectsMaplist.addSortKey(DomainConstants.SELECT_NAME, DomainConstants.SortDirection.ASCENDING.toString(), String.class.getSimpleName());
            sRouteObjectsMaplist.sort();
			StringBuffer routeContentFor3D = new StringBuffer(32);
			StringBuffer routeContentIdsFor3D = new StringBuffer(32);
			StringBuffer routeContentTypesFor3D = new StringBuffer(32);
			StringBuffer routeContentTitleFor3D = new StringBuffer(32);
			String oldTaskAssignee = "";
            for(int i=0; i< sRouteObjectsMaplist.size(); i++) {
                // Map of the Objects
                Map mapRouteObjects = (Map) sRouteObjectsMaplist.get(i);

                // appending the Type name and revision to the String buffer
                sBufRouteObjects.append(mapRouteObjects.get(DomainConstants.SELECT_TYPE));
                sBufRouteObjects.append(" ");
                sBufRouteObjects.append(mapRouteObjects.get(DomainConstants.SELECT_NAME));

				if(i >= 1 && i<=sRouteObjectsMaplist.size()-1){
					routeContentFor3D.append(", ");
					routeContentIdsFor3D.append(",");
					routeContentTypesFor3D.append(",");
					routeContentTitleFor3D.append(",");
				}
				routeContentFor3D.append((String) mapRouteObjects.get(DomainConstants.SELECT_NAME));
				routeContentIdsFor3D.append((String) mapRouteObjects.get(DomainConstants.SELECT_ID));
				routeContentTypesFor3D.append((String) mapRouteObjects.get(DomainConstants.SELECT_TYPE));
				routeContentTitleFor3D.append((String) mapRouteObjects.get(DomainConstants.SELECT_ATTRIBUTE_TITLE));
                //Bug fix: 292537
                sBufRouteObjects.append(" ");
                sBufRouteObjects.append(mapRouteObjects.get(DomainConstants.SELECT_REVISION));
                sBufRouteObjects.append("\n");
            
            	// build content links for notification table
                if (i > 0) {
                	sRouteContent += "<BR>";
                	sRouteContentDescription += "<BR>";
                }

                sRouteContent += emxNotificationUtil_mxJPO.getObjectLinkHTML(context, 
                									(String) mapRouteObjects.get(DomainConstants.SELECT_NAME),
                									(String) mapRouteObjects.get(DomainConstants.SELECT_ID));
            
            	sRouteContentDescription += (String) mapRouteObjects.get(DomainConstants.SELECT_DESCRIPTION);
            }

            Map routeMap = RouteObject.getInfo(context, objectSelects);
			sRoutePreserveOwner = (String) routeMap.get("attribute["+ sRoutePreserveTaskOwner + "]");
            sRouteObjectsMaplist.add(routeMap);
            // Converting the StringBuffer content to String
            sRouteObjects = sBufRouteObjects.toString();
            // object Selects
            SelectList routeNodeObjectSelects = new SelectList();
            routeNodeObjectSelects.addElement(DomainConstants.SELECT_ID);
            routeNodeObjectSelects.addElement(DomainConstants.SELECT_NAME);
			routeNodeObjectSelects.addElement(DomainConstants.SELECT_TYPE);
			routeNodeObjectSelects.addElement(DomainConstants.SELECT_CURRENT);
            routeNodeObjectSelects.addElement(DomainConstants.SELECT_VAULT);
            routeNodeObjectSelects.addElement("attribute["
                    + sAttAbsenceDelegate + "]");
            routeNodeObjectSelects.addElement("attribute[" + sAttFirstName
                    + "]");
            routeNodeObjectSelects
                    .addElement("attribute[" + sAttLastName + "]");
            // relationship selects
            SelectList routeNodeRelationshipSelects = new SelectList();
            routeNodeRelationshipSelects
			.addElement(DomainConstants.SELECT_PHYSICAL_ID);
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttRouteSequence + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttRouteAction + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttApprovalStatus + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttScheduledCompletionDate + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttApproversResponsibility + "]");
            routeNodeRelationshipSelects.addElement("attribute[" + sAttTitle
                    + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttAllowDelegation + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttReviewTask + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttAssigneeDueDateOpt + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttDueDateOffset + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttDueDateOffsetFrom + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttTemplateTask + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttRouteTaskUser + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttRouteTaskUserCompany + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttRouteNodeID + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttRouteInstructions + "]");
            /* Added for Choose users from User Group assignee implementation */
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttUsreGroupAction + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttUserGroupInfo + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttChooseUsersFromUG + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttrRouteOwnerTask + "]");
            routeNodeRelationshipSelects.addElement("attribute["
                    + sAttrRouteOwnerUGChoice + "]");
            //done

            MapList routedNodeObjects = RouteObject.getRelatedObjects(context,
                    sRelRouteNode, "*", routeNodeObjectSelects,
                    routeNodeRelationshipSelects, false, true, (short) 1, "",
                    "");
            // Sorting the Maplist Based on the Route Sequence.
            routedNodeObjects.sort("attribute[" + sAttRouteSequence + "]",
                    "ascending", "String");
            // routedNodeObjects.add(RoutedItems);
            // checking whether there are any objects Connected to Route using
            // Route Node Relationship

            if (routedNodeObjects.size() == 0) {
                String[] mailArguments = new String[10];
                mailArguments[0] = "emxFramework.ProgramObject.eServicecommonInitiateRoute.NoObjectError";
                mailArguments[1] = "4";
                mailArguments[2] = "Person";
                mailArguments[3] = sTypePerson;
                mailArguments[4] = "Type";
                mailArguments[5] = sType;
                mailArguments[6] = "Name";
                mailArguments[7] = sName;
                mailArguments[8] = "Rev";
                mailArguments[9] = sRev;
                Message = mailUtil.getMessage(context, mailArguments);
                if (Message.length() > 0) {
                    MqlUtil.mqlCommand(context, "notice $1", Message);
                    return 0;
                }
                // throw new MatrixException(exceptionMessage);
                return 0;
            }

            //to prevent app server crash due to stack error caused by recursive triggers when route contains successive levels of notification-only tasks,
            //this logic will re-sequence these route nodes to occur at the current level.
            int levelsToDecrement = -1;
            int atLevel = sRouteSequenceValue;
            boolean bAllNotificaionTasks = checkIfAllNotificationTasks(routedNodeObjects, atLevel);
            if (bAllNotificaionTasks)
            {
                while (bAllNotificaionTasks)
                {
                    //check next level to see if all tasks are notification tasks only
                    levelsToDecrement++;
                    atLevel++;
                    bAllNotificaionTasks = checkIfAllNotificationTasks(routedNodeObjects, atLevel);                    
                }
            }
            if (levelsToDecrement > 0)
            {
                for (int i = 1; i <= levelsToDecrement; i++) 
                {
                    //adjust route nodes sequence level to be the current level
                    atLevel = sRouteSequenceValue + i;
                    adjustSequenceLevel(context, routedNodeObjects, atLevel, -i);
                }            
                //adjust the rest of the tasks so that seq level is sequential.
                boolean bUpdatedNodes = true;
                int adjustment = atLevel - sRouteSequenceValue;
                while (bUpdatedNodes)
                {
                    atLevel++;
                    bUpdatedNodes = adjustSequenceLevel(context, routedNodeObjects, atLevel, -adjustment);
                }                                
            }            
            
            /*
             * Check if Person or Group/Role with required sequence order is
             * present If not then increament sequence order till Person or
             * Group/Role found If no person or group/role found then error out.
             */
            com.matrixone.apps.common.Person person = (com.matrixone.apps.common.Person) DomainObject
                    .newInstance(context, DomainConstants.TYPE_PERSON);

            int bGreaterSeqNoFound = 1;
            int nRoutedNodeObjectsSize = routedNodeObjects.size();
         // to check if there is any inactive user
 			boolean abortInitiateTask = checkInactiveAssignee(context, routedNodeObjects, RouteObject, sType, sName, sRev, mailUtil, user, sRouteSequenceValue,sRoutePhysicalId,routeTitle);
 			if(abortInitiateTask)
 						{
	 				String sMsg = EnoviaResourceBundle.getProperty(context,"emxFrameworkStringResource", new Locale(context.getSession().getLanguage()),"emxFramework.ProgramObject.eServicecommonRoute.StartRestartRouteInactivePerson");
					if(!"Not Started".equals(RouteObject.getAttributeValue(context, sAttRouteStatus))){
						com.matrixone.apps.common.Route.stopRoute(context, sRouteId);
						sMsg = EnoviaResourceBundle.getProperty(context,"emxFrameworkStringResource", new Locale(context.getSession().getLanguage()),"emxFramework.ProgramObject.eServicecommonRoute.GenericMessageInactivePerson");
					}
					sRouteSequenceValue--;
					RouteObject.setAttributeValue(context,sAttCurrentRouteNode,""+sRouteSequenceValue);
					MqlUtil.mqlCommand(context, "notice $1", sMsg);
					return 1;
 						}
 			//-----------------------
            while (bGreaterSeqNoFound == 1) {
                bGreaterSeqNoFound = 0;
                for (int i = 0; i < nRoutedNodeObjectsSize; i++) {
                    Map objectMap = (Map) routedNodeObjects.get(i);
                    // getting the Sequence no of the Task
                    String sRouteSequence = (String) objectMap.get("attribute["
                            + sAttRouteSequence + "]");
                    int routeSequence = Integer.parseInt(sRouteSequence);
                    if (routeSequence > sRouteSequenceValue) {
                        bGreaterSeqNoFound = 1;
                    }
                    // Checking whether the passed sequence and the object
                    // sequence are same
                    // if same only do the below operation
                    if (routeSequence == sRouteSequenceValue) {
                        bPersonFound = 1;
								String newRelId = "";
                        // getting all the attribute Values from the Map
						sRouteNodeId = (String) objectMap.get(DomainConstants.SELECT_PHYSICAL_ID);
                        sRouteNodeObjectId = (String) objectMap.get("id");
                        sRouteAction = (String) objectMap.get("attribute["
                                + sAttRouteAction + "]");
                        sScheduledCompletionDate = (String) objectMap
                                .get("attribute[" + sAttScheduledCompletionDate
                                        + "]");
                        sApproversResponsibility = (String) objectMap
                                .get("attribute[" + sAttApproversResponsibility
                                        + "]");
                        sTitle = (String) objectMap.get("attribute["
                                + sAttTitle + "]");
                        sAllowDelegation = (String) objectMap.get("attribute["
                                + sAttAllowDelegation + "]");
                        sReviewTask = (String) objectMap.get("attribute["
                                + sAttReviewTask + "]");
                        sAssigneeDueDateOpt = (String) objectMap
                                .get("attribute[" + sAttAssigneeDueDateOpt
                                        + "]");
                        sDueDateOffString = (String) objectMap.get("attribute["
                                + sAttDueDateOffset + "]");
                        sDueDateOffsetFrom = (String) objectMap
                                .get("attribute[" + sAttDueDateOffsetFrom + "]");
                        sTemplateTask = (String) objectMap.get("attribute["
                                + sAttTemplateTask + "]");
                        sRouteTaskUser = (String) objectMap.get("attribute["
                                + sAttRouteTaskUser + "]");
                        sRouteTaskUserCompany = (String) objectMap
                                .get("attribute[" + sAttRouteTaskUserCompany
                                        + "]");
                        sRouteNodeIdAttr = (String) objectMap.get("attribute["
                                + sAttRouteNodeID + "]");
                        sPersonId = (String) objectMap
                                .get(DomainConstants.SELECT_ID);
                        sPersonName = (String) objectMap
                                .get(DomainConstants.SELECT_NAME);
                        sPersonVault = (String) objectMap
                                .get(DomainConstants.SELECT_VAULT);
                        sAbsenceDelegate = (String) objectMap.get("attribute["
                                + sAttAbsenceDelegate + "]");
                        sPersonFirstName = (String) objectMap.get("attribute["
                                + sAttFirstName + "]");
                        sPersonLastName = (String) objectMap.get("attribute["
                                + sAttLastName + "]");
                        sRouteInstructions = (String) objectMap
                                .get("attribute[" + sAttRouteInstructions + "]");
                        /* Added for Choose users from User Group assignee implementation */
                        sUserGroupAction = (String) objectMap
                                .get("attribute[" + sAttUsreGroupAction + "]");
                        sUserGroupInfo = (String) objectMap
                                .get("attribute[" + sAttUserGroupInfo + "]");
                        sChooseUsersFromUG = (String) objectMap
                                .get("attribute[" + sAttChooseUsersFromUG + "]");
                        sRouteOwnerTask = (String) objectMap.get("attribute["
                                + sAttrRouteOwnerTask + "]");
                        sRouteOwnerUserGroup = (String) objectMap.get("attribute["
                                + sAttrRouteOwnerUGChoice + "]");
                       
                        
						//done
						sDelegatorId = "";//initializing to empty .previous value to clear
                        String sRouteInstructionsStripped = FrameworkUtil.stripString(sRouteInstructions, InboxTask.MAX_LEN_OF_CNT_DESC_FOR_ROUTE_NOTIFICATIONS, FrameworkUtil.StripStringType.WORD_STRIP);

                        person.setId(sPersonId);
								String strKindOfProxyGroup = "type.kindof["+ PropertyUtil.getSchemaProperty(context, "type_GroupProxy") +"]";
								//Map details = person.getInfo(context,selects);
								String currentState =(String) objectMap.get(DomainConstants.SELECT_CURRENT);
								String objectType  = (String)objectMap.get(DomainConstants.SELECT_TYPE);
								boolean isKindOfProxyGroup = PROXY_GROUP_TYPE.equals(objectType) || GROUP_TYPE.equals(objectType);
								//TODO need to remove once check trigger is implemented.
								//if assignee is User Group and the choose choosers from user group is true, then block start route
								if(isKindOfProxyGroup && "True".equals(sChooseUsersFromUG)){
									throw new Exception("Please choose users from User Group assignee before starting the route");
								}
								boolean isUGAlreadyConnectedToTask = false;;
						// Get multiline attribute Route Instuction from
                        // connection id
                        sRPEType = MqlUtil.mqlCommand(context, "get env $1", "TYPE");
                        sRPEName = MqlUtil.mqlCommand(context, "get env $1", "NAME");
						String sRPETITLE = MqlUtil.mqlCommand(context, "get env $1", "TITLE");
						if(UIUtil.isNullOrEmpty(sRPETITLE)){
							sRPETITLE = sRPEName;
						}
                        sRPERev = MqlUtil.mqlCommand(context,"get env $1", "REVISION");
                        String Vault = MqlUtil.mqlCommand(context, "get env $1", "VAULT");
                        MqlUtil.mqlCommand(context, "unset env $1 $2", "global", "MX_TREE_MENU");
                        // Start: Resume Process implementation
                        //because the route node id is objectid in the previous versions . IR-634496-3DEXPERIENCER2019x
                        String objectWhereClause = "attribute[" + sAttRouteNodeID + "]=='"+ sRouteNodeId + "' || attribute[" + sAttRouteNodeID + "]=='"+ sRouteNodeObjectId + "'";
                        MapList mlOldTasks = RouteObject.getRelatedObjects(context,
                                sRelRouteTask, DomainObject.TYPE_INBOX_TASK,
                                slTaskSelect, null, true, false, (short) 1,
                                objectWhereClause, "");
                        //
                        // If old task object is present then we have to reuse
                        // it (if not completed already) or revise it (if
                        // complete already)
                        // If no old task is present then we will just create
                        // new inbox task object
                        //
                        if (mlOldTasks.size() > 0) {
                            mapTaskInfo = (Map) mlOldTasks.get(0); // Assuming
                            // that each Route Node relationship gives rise to
                            // exactly one task which is connected to route object

                            // If there are tasks which are valid ones
                            // (connected to route and person object), we will
                            // not touch them
									isUGAlreadyConnectedToTask = true;
                            if (mapTaskInfo.get("from["
                                    + DomainObject.RELATIONSHIP_PROJECT_TASK
                                    + "].to.id") != null) {
                                continue;
                            }

									strOldTaskId = (String) mapTaskInfo.get(DomainObject.SELECT_ID);
									strOldTaskState = (String) mapTaskInfo.get(DomainObject.SELECT_CURRENT);
									strTaskAssignTo = (String) mapTaskInfo.get(SELECT_INITIAL_USER_GROUP_ID);
                            if (POLICY_INBOX_TASK_STATE_COMPLETE
                                    .equals(strOldTaskState)) {
                                // Revise this task, so that this revisions will
                                // be floated and new revision will be connected
                                // to the task
                                dmoOldTask = new DomainObject(strOldTaskId);
                                dmoNewTask = new DomainObject(dmoOldTask
                                        .reviseObject(context, true));
                                try {
                                	ContextUtil.pushContext(context);
									Map taskInfo = dmoNewTask.getInfo(context,slTaskSelect);
									sInboxTaskId = (String) taskInfo.get(DomainObject.SELECT_ID);
									sInboxTaskPhysicalId = (String) taskInfo.get(DomainConstants.SELECT_PHYSICAL_ID);
                                    DomainAccess.createObjectOwnership(context,sInboxTaskId,sRouteId,DomainAccess.COMMENT_MULTIPLE_OWNERSHIP); // updating Route Id on Inbox Task
                                    
                                    //Adding check for inactive user present in complete tasks. 
                                    //Restart is allowed for Complete task with inactive user and below code will notify Route owner that inactive user is present.
                                    String currentTaskAssigneeState = (String) objectMap.get(DomainConstants.SELECT_CURRENT);
                                    if("Inactive".equalsIgnoreCase(currentTaskAssigneeState)) {
                                    	notifyForInactiveAssignee(context, sRouteOwner, sType, sName, sRev, mailUtil, user, sRoutePhysicalId, sPersonName,routeTitle);
                                    }
                                } catch (Exception ex) {
                                	ex.printStackTrace();
                                } finally {
                                	ContextUtil.popContext(context);
                                }
                                // If the task was created for the role or group
                                // then after revision to new task we shall
                                // set the role/group as the owner of the task,
                                // so that when an user accepts the task, the
                                // ownership
                                // changes to himself
                                if (sRouteTaskUser != null
                                        && !"".equals(sRouteTaskUser)
                                        && (sRouteTaskUser.startsWith("role_") || sRouteTaskUser
                                                .startsWith("group_"))) {
                                    strRoleOrGroup = PropertyUtil
                                            .getSchemaProperty(context,
                                                    sRouteTaskUser);
                                    dmoNewTask
                                            .setOwner(context, strRoleOrGroup);
                                }
                            } else {
                                dmoNewTask = new DomainObject(strOldTaskId);
                            }
									//code to preserve the User Group

									if ("true".equalsIgnoreCase(sRoutePreserveOwner) && UIUtil.isNotNullAndNotEmpty(strTaskAssignTo)) {
										    isKindOfProxyGroup   = true;
										    dmoNewTask.setOwner(context, ROLE_USER_GROUP);
											sPersonId = strTaskAssignTo;
											sPersonName = (String) new DomainObject(strTaskAssignTo).getInfo(context, DomainObject.SELECT_NAME);
											person.setId(sPersonId);
											Route routeObject = (Route)DomainObject.newInstance(context,sRouteId);
											String connectionId = routeObject.getRouteNodeRelId(context,sRouteNodeId );
											Map relAttrs = DomainRelationship.getAttributeMap(context, connectionId);
											DomainRelationship.disconnect(context, connectionId);
											DomainRelationship rel = DomainRelationship.connect(context, RouteObject, DomainConstants.RELATIONSHIP_ROUTE_NODE, person);
											rel.setAttributeValues(context, relAttrs);
											newRelId = rel.getName();
											sRouteNodeId = newRelId;
								newRelId = MqlUtil.mqlCommand(context, "print connection $1 select $2 dump", newRelId, DomainConstants.SELECT_PHYSICAL_ID);
											rel.setAttributeValue(context,DomainConstants.ATTRIBUTE_ROUTE_NODE_ID,newRelId);
											sRouteNodeIdAttr = newRelId;
										}
                            // Reset some attributes now Approval Status,
                            // Comments, Due Date and Completion Date
                            mapAttributesToReset = new HashMap();
                            mapAttributesToReset.put(
                                    DomainObject.ATTRIBUTE_APPROVAL_STATUS,
                                    "None");
                            mapAttributesToReset
                                    .put(
                                            DomainObject.ATTRIBUTE_SCHEDULED_COMPLETION_DATE,
                                            "");
                            mapAttributesToReset
                                    .put(
                                            DomainObject.ATTRIBUTE_ACTUAL_COMPLETION_DATE,
                                            "");
                            mapAttributesToReset.put(
                                    DomainObject.ATTRIBUTE_COMMENTS, "");

                            dmoNewTask.setAttributeValues(context,
                                    mapAttributesToReset);
									//  slTaskSelect.add(SELECT_TASK_ASSIGNEE_CONNECTION);
							mapTaskInfo = dmoNewTask.getInfo(context,slTaskSelect);
							sInboxTaskId = (String) mapTaskInfo.get(DomainObject.SELECT_ID);
							sInboxTaskPhysicalId =(String) mapTaskInfo.get(DomainConstants.SELECT_PHYSICAL_ID);
							sInboxTaskType = (String) mapTaskInfo.get(DomainObject.SELECT_TYPE);
							sInboxTaskName = (String) mapTaskInfo.get(DomainObject.SELECT_NAME);
							sInboxTaskTitle =(String) mapTaskInfo.get(InboxTask.SELECT_TITLE);
							if(UIUtil.isNullOrEmpty(sInboxTaskTitle)){
								sInboxTaskTitle = sInboxTaskName;
							}
                            sInboxTaskRev = (String) mapTaskInfo
                                    .get(DomainObject.SELECT_REVISION);
									/* String projectTaskRel = (String) mapTaskInfo.get(SELECT_TASK_ASSIGNEE_CONNECTION);
                            DomainRelationship.setToObject(context, projectTaskRel, person);*/
                            isTaskConnectedToRoute = true;
                        } else {
                            // Create a Inbox Task
                            String strInboxRes = MqlUtil.mqlCommand(context, "execute program $1 $2 $3 $4 $5 $6", true, "eServicecommonNumberGenerator.tcl", typeTask, "", "", "Null", Vault);

                            StringList slTokens = FrameworkUtil.split(strInboxRes, "|");
                            String sInboxErrorCode = ((String) slTokens.get(0)).trim();

                            // getting the Task id,type,name,Rev from tokenizing
                            // the output of the mql.
                            sInboxTaskId = ((String) slTokens.get(1)).trim();
                            sInboxTaskType = ((String) slTokens.get(2)).trim();
                            sInboxTaskName = ((String) slTokens.get(3)).trim();
                            sInboxTaskRev = ((String) slTokens.get(4)).trim();
							sInboxTaskPhysicalId = FrameworkUtil.getPIDfromOID(context, sInboxTaskId);
                            isTaskConnectedToRoute = false;
                            
                            if(UIUtil.isNotNullAndNotEmpty(routeOrg) && UIUtil.isNotNullAndNotEmpty(routeProject)) {
                            	try {
                            		MqlUtil.mqlCommand(context, "trigger off", true);
                            		String command = "mod bus $1 organization $2 project $3";
                            		MqlUtil.mqlCommand(context, command, sInboxTaskId, routeOrg, routeProject);
                            	}finally {
                            		MqlUtil.mqlCommand(context, "trigger on", true);
                            	}
                        	}
                            
                            
                        }
								StringList accessNames = DomainAccess.getLogicalNames(context, sInboxTaskId);
								String defaultAccess = (String)accessNames.get(0);
								if(isKindOfProxyGroup){
									DomainAccess.createObjectOwnershipForUserGroups(context, sInboxTaskId, sPersonName,defaultAccess ,DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
								}
								DomainAccess.createObjectOwnership(context,sInboxTaskId,sRouteId,DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);// updating Route Id on Inbox Task
								defaultAccess = (String)accessNames.get(accessNames.size()-1);
								//Commented below line of code. Ownership will be added to the Route for the owner
								//DomainAccess.createObjectOwnership(context,sInboxTaskId,null,sRouteOwner+"_PRJ",defaultAccess,DomainAccess.COMMENT_MULTIPLE_OWNERSHIP,false);
								
						String selectProp = "emxFramework.Type."+ sInboxTaskType.replace(' ', '_');
						//sInboxTaskType = EnoviaResourceBundle.getFrameworkStringResourceProperty(context, selectProp, new Locale(context.getSession().getLanguage()));
						
                        // End: Resume Process implementation
                        // setting the id of the task.
                        inboxTask.setId(sInboxTaskId);
                        
//                      # 370487 set the originator value of task 
                        inboxTask.setAttributeValue(context, DomainObject.ATTRIBUTE_ORIGINATOR, sRouteOwner); 
                        
                        /*
                         * Copy 'Route Action' 'Route Instructions' 'Approval
                         * Status' 'Scheduled Completion Date' 'Route Task User'
                         * 'Route Task User Company' 'Review Task' 'Assignee Set
                         * Due Date' 'Due Date Offset' 'Date Offset From'
                         * 'Approvers Responsibility' attributes from
                         * relationship Route Node to Inbox Tasxk Set 'Route
                         * Node Id' attribute of Inbox Task to 'Route Node'
                         * Relationship ID
                         */
                        HashMap attributesMap = new HashMap();
                        attributesMap.put(sAttRouteAction, sRouteAction);
                        attributesMap.put(sAttRouteInstructions,
                                sRouteInstructions);
                        attributesMap.put(sAttApprovalStatus, "None");
                        if(sScheduledCompletionDate.equals("")&&!sDueDateOffString.equals(""))
                        {
							String duedateOffset;
                        	GregorianCalendar cal            = new GregorianCalendar();
                    		GregorianCalendar offSetCal      = new GregorianCalendar();
                    		SimpleDateFormat formatterTest   = new SimpleDateFormat (eMatrixDateFormat.getInputDateFormat(),Locale.US);
                    		offSetCal      = (GregorianCalendar)cal.clone();
                    		duedateOffset  = sDueDateOffString;
                    		offSetCal.add(Calendar.DATE, Integer.parseInt(duedateOffset));
                    		sScheduledCompletionDate   = formatterTest.format(offSetCal.getTime());
                        }
                        attributesMap.put(sAttScheduledCompletionDate,
                                sScheduledCompletionDate);
                        attributesMap.put(sAttApproversResponsibility,
                                sApproversResponsibility);
                        attributesMap.put(sAttRouteNodeID, sRouteNodeIdAttr);
						if (UIUtil.isNullOrEmpty(sTitle)) {
                            sTitle = inboxTask.getInfo(context, DomainObject.SELECT_NAME);
                            MqlUtil.mqlCommand(context, "modify connection $1 Title $2", sRouteNodeId, sTitle);
                        }
						sInboxTaskTitle = sTitle;
                        attributesMap.put(sAttTitle, sTitle);
                        attributesMap
                                .put(sAttAllowDelegation, sAllowDelegation);
                        attributesMap.put(sAttReviewTask, sReviewTask);
                        attributesMap.put(sAttAssigneeDueDateOpt,
                                sAssigneeDueDateOpt);
                        attributesMap.put(sAttDueDateOffset, sDueDateOffString);
                        attributesMap.put(sAttDueDateOffsetFrom,
                                sDueDateOffsetFrom);
                        attributesMap.put(sAttTemplateTask, sTemplateTask);
                        attributesMap.put(sAttRouteTaskUser, sRouteTaskUser);
                        attributesMap.put(sAttRouteTaskUserCompany,
                                sRouteTaskUserCompany);
                        /*For choose users from UG implementation */
                        attributesMap.put(sAttUserGroupInfo, sUserGroupInfo);
                        attributesMap.put(sAttUsreGroupAction, sUserGroupAction);
                        
                        if("TRUE".equalsIgnoreCase(sRouteOwnerTask)) {
                        	attributesMap.put(sAttrRouteOwnerTask, sRouteOwnerTask);
                        	if(UIUtil.isNotNullAndNotEmpty(sRouteOwnerUserGroup)) {
                        		attributesMap.put(sAttrRouteOwnerUGChoice, sRouteOwnerUserGroup);  
                        	}                            
                        }
                                              
                        
                        //added
                        // updating the attibutes
                        inboxTask.setAttributeValues(context, attributesMap);
								if(isKindOfProxyGroup && !isUGAlreadyConnectedToTask){
									DomainRelationship.connect(context, inboxTask,INITIAL_USER_GROUP_REL, person);
								}
                        // connecting the inbox task object to Route
                        if (!isTaskConnectedToRoute) {
                            DomainRelationship.connect(context, inboxTask,
                                    sRelRouteTask, RouteObject);
                        }
                        
                        // Set the Inbox Task due date
                        Date date = new Date();
                        String strDate = null;
                        if(routeSequence>1) // First Order task will have the Route Start date+1 as the Scheduled completion date
                        					// Second, Third & so on will have the last task of previous order Compeltion Date+1
                        {
                        	//DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa");
                        	DateFormat dateFormat = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat());
                        	strDate = dateFormat.format(date);
                        }
                        
                        if(sAssigneeDueDateOpt.equalsIgnoreCase("Yes"))
                        {
							sScheduledCompletionDate = com.matrixone.apps.common.Route.setAssigneeDueDate(context, RouteObject, inboxTask, strDate);
                        }
						
                        // Before connecting the task to the person, check to
                        // see if
                        // delegation is required:
                        // checking if the absenceDelegate attribute value not
                        // equals "" and
                        // allow delegation equals to true. if both conditiond
                        // are met do the belw Operation
                        if (!"".equals(sAbsenceDelegate)
                                && "TRUE".equals(sAllowDelegation)) {
                            // Rather than dealing with date/time formats, get
                            // the current
                            // time from the server. The RPE timestamp available
                            // in triggers
                            // is not necessarily the same format of the server.
							oldTaskAssignee = sPersonName;
                            /*
                             * Commented for bug 285757 SelectList
                             * taskSelectList = new SelectList(1);
                             * taskSelectList.add("originated"); Map taskMap =
                             * inboxTask.getInfo(context,taskSelectList); String
                             * sCurrentTime = (String)taskMap.get("originated");
                             */
                            SelectList objectSelects1 = new SelectList();
                            objectSelects1.addElement(DomainConstants.SELECT_ID);
                            objectSelects1.addElement("attribute["
                                    + sAttAbsenceDelegate + "]");
                            // find the person object // modified for bug 285757
                            
                            // For bug 343655 - If the due date is not specified then current date is taken to do following comparison
                            // We cannot take task's originated date, because the task can be an old task 
                            // which is being reused (Resume Process implementation)
                            String strTentativeCompletionDate = inboxTask.getAttributeValue(context, DomainConstants.ATTRIBUTE_SCHEDULED_COMPLETION_DATE);
							SimpleDateFormat dateFormat = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
                            if (strTentativeCompletionDate == null || "".equals(strTentativeCompletionDate)) {
                                
                                strTentativeCompletionDate = dateFormat.format(new Date());
                            }
                            String todayDate = dateFormat.format(new Date());
                            ArrayList alDelegated= new ArrayList();
                           
                            MapList absenceMap = DomainObject.findObjects(
                                    context, sTypePerson, sPersonName, "*",
                                    "*", sPersonVault, "attribute["
                                            + sAttAbsenceStartDate + "] <='"
                                            + strTentativeCompletionDate
                                            + "' && attribute["
                                            + sAttAbsenceEndDate + "] >='"
                                            + strTentativeCompletionDate 
                                            + "' && attribute["
                                            + sAttAbsenceStartDate + "] <='"
                                            + todayDate + "'",
                                    false, objectSelects1);
                            if (absenceMap.size() != 0) {
                            	alDelegated.add(sPersonName);                                
                            	String tempsAbsenceDelegate = sAbsenceDelegate;
                            	while (!"".equals(tempsAbsenceDelegate)) {
	                            	if(alDelegated.contains(sAbsenceDelegate)){
                            			String sMsg = EnoviaResourceBundle.getProperty(context,"emxComponentsStringResource", new Locale(context.getSession().getLanguage()),"emxComponents.Tasks.DelegationFailed");
    	                            	emxContextUtil_mxJPO.mqlNotice(context,sMsg);
    	                            	return 1;	                                			
	                        		}
	                            	MapList absenceMap2 = DomainObject.findObjects(
                                    context, sTypePerson, sAbsenceDelegate, "*",
                                    "*","*", "attribute["
                                            + sAttAbsenceStartDate + "] <='"
                                            + strTentativeCompletionDate
                                            + "' && attribute["
                                            + sAttAbsenceEndDate + "] >='"
                                            + strTentativeCompletionDate 
                                            + "' && attribute["
                                            + sAttAbsenceStartDate + "] <='"
                                            + todayDate + "'",
                                    false, objectSelects1);
	                            	if(absenceMap2.size() != 0) {
	                                	alDelegated.add(sAbsenceDelegate);
                            	 	Map personMap = (Map) absenceMap2.get(0);
                            	 	sAbsenceDelegate = (String) personMap.get("attribute["
                                        + sAttAbsenceDelegate + "]");
                            	 	tempsAbsenceDelegate = sAbsenceDelegate;
	                            	} 
	                            	else {
                            	    tempsAbsenceDelegate = "";
                                    }
                            	}
                            // if the person has absence set start and end date
                            // set the map size will be greater than 0
                            //if (absenceMap.size() != 0) {
                                /*
                                 * The person is absent; delegate the person's
                                 * task Just re-set sPersonId & sPersonName to
                                 * the new person Look up this new person
                                 */

                                StringList objectSelect = new StringList();
                                objectSelect
                                        .addElement(DomainConstants.SELECT_ID);
                                objectSelect.addElement("attribute["
                                        + sAttFirstName + "]");
                                objectSelect.addElement("attribute["
                                        + sAttLastName + "]");
                                // find the details of the Delegate
                                MapList personMapList = DomainObject
                                        .findObjects(context, sTypePerson,
                                                sAbsenceDelegate, "*", "*",
                                                "*", "", false, objectSelect);

                                sDelegatorName = sAbsenceDelegate;
                                Map personMap = (Map) personMapList.get(0);
                                sDelegatorId = (String) personMap
                                        .get(DomainConstants.SELECT_ID);
                                sDelegatorFullName = (String) personMap
                                        .get("attribute[" + sAttFirstName + "]")
                                        + (String) personMap.get("attribute["
                                                + sAttLastName + "]");
                                // Change route node relationship to reflect new
                                // delegator

                                MQLCommand updateCommand = new MQLCommand();
                                updateCommand.open(context);
                                strMQL = "modify connection $1 to $2";
                                updateCommand.executeCommand(context, strMQL, sRouteNodeId, sDelegatorId);
                                String errUpdate = updateCommand.getError();
                                String strResUpdate = updateCommand.getResult().trim();

                                updateCommand.close(context);
                                if (errUpdate.length() != 0) {
                                    // throw new MatrixException("Error
                                    // connecting Delegator ");
                                    return 1;
                                }
                                /*
                                 * Grant access to the route and route objects
                                 * to new person based on access to original
                                 * assignee. Do this by looking up grants from
                                 * 'Route Access Grantor' and granting to
                                 * new user using 'Route Delegation Grantor'.
                                 */
                                MapList lGrants = new MapList();
                                // modified for 307078
                                // if (fGranteeLookup == 0) {
                                // fGranteeLookup = 1;
                                // till here

                                for (int i1 = 0; i1 < sRouteObjectsMaplist
                                        .size(); i1++) {
                                    Map mapRouteObjects = (Map) sRouteObjectsMaplist
                                            .get(i1);
                                    /*if(!DomainConstants.TYPE_ROUTE.equals((String)mapRouteObjects.get("type"))){
										continue;
									}*/
                                    sObjectId = (String) mapRouteObjects
                                            .get(DomainConstants.SELECT_ID);

                                    lGrantor = MqlUtil.mqlCommand(context,"print bus $1 select $2 $3 dump $4", sObjectId, "grantee", "grantor", "|" );
                                    if (lGrantor.indexOf("|") != -1) {
                                        //lGrantor = lGrantor.substring(0,
                                               // lGrantor.indexOf("|"));
                                        
                                        StringTokenizer stk1 = new StringTokenizer(
                                                lGrantor, "|");
                                        
                                        int k = 0, nGrantors = stk1.countTokens() / 2, pos = 0;
                                        String tmpStr = "";
                                        boolean found = false;
                                        while (stk1.hasMoreTokens()) {
                                            tmpStr = (String) stk1.nextToken();
                                            if (tmpStr.equals(sPersonName)) {
                                                lGrantor = tmpStr;
                                                found = true;
                                                pos = k;
                                            }
                                            if (found && k == (nGrantors + pos)) {
                                                lGrantor = tmpStr;
                                            }
                                            k++;

                                        }
                                        
                                    }
                                    if ("".equals(lGrantor)) {
                                        continue;
                                    }
                                    String output = MqlUtil.mqlCommand(context,"print bus $1 select $2 $3 dump $4", sObjectId, "grantee", "granteeaccess", "|" );
                                                    
                                    StringTokenizer stk = new StringTokenizer(output, "|");

                                    int k = 0, nGrantees = stk.countTokens() / 2, pos = 0;
                                    String tmpStr = "";
                                    boolean found = false;
                                    while (stk.hasMoreTokens()) {
                                        tmpStr = (String) stk.nextToken();
                                        if (tmpStr.equals(sPersonName)) {
                                            lGrantee = tmpStr;
                                            found = true;
                                            pos = k;
                                        }
                                        if (found && k == (nGrantees + pos)) {
                                            lGranteeAccess = tmpStr;
                                        }
                                        k++;

                                    }
                                    HashMap grantMap = new HashMap();
                                    grantMap.put("sObjectId", sObjectId);
                                    grantMap.put("lGrantor", lGrantor);
                                    grantMap.put("lGrantee", lGrantee);
                                    grantMap.put("lGranteeAccess",
                                            lGranteeAccess);

                                    lGrants.add(grantMap);
                                }// eof for loop
                                // modified for 307078
                                // }//. eof if loop
                                // till here
                                for (int i2 = 0; i2 < lGrants.size(); i2++) {
                                    HashMap grantMap = (HashMap) lGrants.get(i2);
                                    sObjectId = (String) grantMap.get("sObjectId");

                                    lGrantor = (String) grantMap.get("lGrantor");

                                    lGrantee = (String) grantMap.get("lGrantee");

                                    lGranteeAccess = (String) grantMap.get("lGranteeAccess");
                                    // modified for 307078
                                    if ((lGrantor
                                            .equals(sRouteAccessGrantor) || lGrantor
                                            .equals(sSuperUser))
                                            && lGrantee.equals(sPersonName)) {
                                        // till here
										 ContextUtil.pushContext(context, sRouteDelegationGrantor, null, null);
                                         String mqlCommand =  "modify bus $1 grant $2 access";
                                         StringList mqlParam = new StringList();
                                         mqlParam.add(sObjectId);          
                                         mqlParam.add(sDelegatorName);
                                         String[] lGranteeAccesslist = lGranteeAccess.split(",");       
                                         int index=0;
                                                for (int i1 = 0; i1 < lGranteeAccesslist.length; i1++) {
                                                       index = i1+3;
                                                       mqlCommand = mqlCommand +" $"+index;
                                                       if(i1!=lGranteeAccesslist.length-1){
                                                              mqlCommand+=",";
                                                       }
                                                       mqlParam.add(lGranteeAccesslist[i1]);
                                                       }
                                         MqlUtil.mqlCommand(context, mqlCommand,mqlParam);
                                  ContextUtil.popContext(context);

                                    }
                                }
                                // # Override old assignee id and name to new
                                // delegator
                                sPersonId = sDelegatorId;
                                sPersonName = sDelegatorName;

                            }// eof absence Map .size(0
                        }
                        // Connect Inbox Task via Project Task relationship to
                        // the Person or RTU the Route is attached to
                        DomainObject personObject = new DomainObject(sPersonId);
                        personObject.open(context);
                        personObject.close(context);
                        DomainRelationship.connect(context, inboxTask,
                                sRelProjectTask, personObject);
                        String grantAccessForNewUser = Route.getAccessbits(context,sRouteId,person.getName());
                        String defaultAccessOnRoute = null;
       				 	if(UIUtil.isNotNullAndNotEmpty(grantAccessForNewUser)){
       				 		defaultAccessOnRoute = DomainAccess.getLogicalName(context, sRouteId,grantAccessForNewUser); 
       					}
       				 	if( !isKindOfProxyGroup && DomainConstants.TYPE_PERSON.equalsIgnoreCase(objectType)){
       				 		if(UIUtil.isNotNullAndNotEmpty(grantAccessForNewUser) && UIUtil.isNotNullAndNotEmpty(defaultAccessOnRoute)){
       				 			DomainAccess.createObjectOwnership(context, sRouteId, sPersonId, defaultAccessOnRoute, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
       				 		}
       				 	}
       				 	if(UIUtil.isNotNullAndNotEmpty(sDelegatorName)) {
							for(int k=0; k< sRouteObjectsMaplist.size(); k++) {								  
								Map mapRouteObjects = (Map) sRouteObjectsMaplist.get(k);
								String fromType = (String)mapRouteObjects.get(DomainConstants.SELECT_TYPE);
								if(!(DomainObject.TYPE_WORKSPACE.equals(fromType) || DomainObject.TYPE_WORKSPACE_VAULT.equals(fromType) || DomainObject.TYPE_PROJECT_SPACE .equals(fromType) || DomainObject.TYPE_PROJECT_CONCEPT.equals(fromType) || DomainObject.TYPE_ROUTE.equals(fromType))) {									
									DomainAccess.createObjectOwnership(context, (String)mapRouteObjects.get(DomainConstants.SELECT_ID) , null, sDelegatorName+"_PRJ", defaultAccessOnRoute, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP_GRANTED +" " +sName, false);
								}									
							}
						}
                        // # set person variable to group/role if applicable

                        if (!sRouteTaskUser.equals("")) {
                            sGroupOrRole = sRouteTaskUser.substring(0,
                                    (sRouteTaskUser.indexOf("_")));
                            sRouteTaskUserValue = PropertyUtil
                                    .getSchemaProperty(context, sRouteTaskUser);
                            StringList slPersonInfoSelect=new StringList();
                            slPersonInfoSelect.add(DomainObject.SELECT_TYPE);
                            slPersonInfoSelect.add(DomainObject.SELECT_NAME);
									slPersonInfoSelect.add(strKindOfProxyGroup);
                            Map mpPersonInfo=personObject.getInfo(context, slPersonInfoSelect);
                            String strPersonType=(String)mpPersonInfo.get(DomainObject.SELECT_TYPE);
									isKindOfProxyGroup =Boolean.parseBoolean((String)mpPersonInfo.get(strKindOfProxyGroup));
									if(UIUtil.isNotNullAndNotEmpty(strPersonType)&& (DomainConstants.TYPE_PERSON.equals(strPersonType) || isKindOfProxyGroup)){
                            	sPersonName =(String)mpPersonInfo.get(DomainObject.SELECT_NAME);
                             }else{
                            sPersonName = sRouteTaskUserValue;
                            }
                            String sFirstChar = (sGroupOrRole.substring(0, 1))
                                    .toUpperCase();
                            sGroupOrRoleTitle = sFirstChar
                                    + sGroupOrRole.substring(1, sGroupOrRole
                                            .length());

                            // IR-043921V6R2011 - Changes START
                            String sOwnOrgId = RouteObject.getInfo( context, SELECT_OWNING_ORG_ID );
                            StringList busSelects = new StringList(2);
                            busSelects.addElement( DomainConstants.SELECT_ID );
                            busSelects.addElement( DomainConstants.SELECT_NAME );
                            Organization org = null;
                            MapList mlMembers = new MapList();
                            if( sOwnOrgId !=null && !"null".equals( sOwnOrgId ) && !"".equals( sOwnOrgId )) {
                                org = (Organization) DomainObject.newInstance(context, sOwnOrgId);
                            }
                            // IR-043921V6R2011 - Changes END
						    //Commented for the Bug No: 339357 3 11/20/2007 Begin 
                            //sGRName = sGroupOrRoleTitle + " - " + sPersonName;
							 //Commented for the Bug No: 339357 3 11/20/2007 End
							//Added for the Bug No: 339357 3 11/20/2007 Begin 
                            strLanguage = context.getSession().getLanguage();
                            if("Role".equals(sGroupOrRoleTitle)) {
                                aliasRoleName= i18nNow.getAdminI18NString("Role",sPersonName,strLanguage);
                                // IR-043921V6R2011 - Changes START
                                if( org != null ) {
                                    mlMembers = org.getMemberPersons(context, busSelects, null, sRouteTaskUserValue );
                                }
                                // IR-043921V6R2011 - Changes END
                            } else { 
                                aliasRoleName= i18nNow.getAdminI18NString("Group",sPersonName,strLanguage);
                                // IR-043921V6R2011 - Changes START
                                if( org != null ) {
                                    MapList mlAll = org.getMemberPersons(context, busSelects, null, null );

                                    StringList slUsers = new StringList( 50 );
                                    Group matrixGroup = new Group( sRouteTaskUserValue );
                                    matrixGroup.open(context);
                                    UserItr itr = new UserItr(matrixGroup.getAssignments(context));
                                    while(itr.next()) {
                                        if (itr.obj() instanceof matrix.db.Person) {
                                            slUsers.add( itr.obj().getName());
                                        }
                                    }
                                    matrixGroup.close(context);
                                    for( Iterator mlItr = mlAll.iterator(); mlItr.hasNext(); ) {
                                        Map mPerson = (Map) mlItr.next();
                                        if( slUsers.contains((String) mPerson.get( DomainConstants.SELECT_NAME )) ) {
                                            mlMembers.add( mPerson );
                                        }
                                    }
                                }
                                // IR-043921V6R2011 - Changes END
                           }
                           sGRName =sGroupOrRoleTitle +" - "+aliasRoleName;
                           //Added for the Bug No: 339357 3 11/20/2007 End 

                           // IR-043921V6R2011 - Changes START
                           if( mlMembers.size() > 0 ) {
                               Map mTemp = null;
                               StringBuffer sbToList = new StringBuffer( 100 );
                               for( Iterator itr = mlMembers.iterator(); itr.hasNext(); ) {
                                   mTemp = (Map) itr.next();
                                   sbToList.append( (String) mTemp.get( DomainConstants.SELECT_NAME ) );
                                   sbToList.append( ',' );
                               }
                               // Remove the last comma and assign the resulting string to 'sRoleOrGroupToList'
                               // which is essentially the 'ToList' of the email/iconmail.
                               sRoleOrGroupToList = sbToList.substring( 0, sbToList.length()-1 );
                           } else {
                               sRoleOrGroupToList = sPersonName;
                           }
                           // IR-043921V6R2011 - Changes END
                        }
								if(isKindOfProxyGroup){
									sRoleOrGroupToList = FrameworkUtil.getPersonsMailListFromUserGroup(context, sPersonId);
									if(UIUtil.isNullOrEmpty(sRoleOrGroupToList)) {
										sRoleOrGroupToList = sPersonName;
									}
								}
                        String strTranslatedInboxTaskType = EnoviaResourceBundle.getFrameworkStringResourceProperty(context, "emxFramework.Type." + sInboxTaskType.replace(" ", "_"), new Locale(context.getSession().getLanguage()));
						String strTranslatedRPEType = EnoviaResourceBundle.getFrameworkStringResourceProperty(context, "emxFramework.Type." + sRPEType.replace(" ", "_"), new Locale(context.getSession().getLanguage()));
						String strTranslatedSType = EnoviaResourceBundle.getFrameworkStringResourceProperty(context, "emxFramework.Type." + sType.replace(" ", "_"), new Locale(context.getSession().getLanguage()));
						String strTranslatedRouteAction = EnoviaResourceBundle.getFrameworkStringResourceProperty(context, "emxFramework.Range.Route_Action." + sRouteAction.replace(" ", "_"), new Locale(context.getSession().getLanguage()));

                        // Updating the Owner
                        try {
                        	MqlUtil.mqlCommand(context, "set env $1  $2", "NEED_TO_UPDATE_OWNERSHIP", "false");
                        	ContextUtil.pushContext(context, null, null, null);
									if(isKindOfProxyGroup){
										inboxTask.setOwner(context, ROLE_USER_GROUP);
										accessNames = DomainAccess.getLogicalNames(context, sInboxTaskId);
										defaultAccess = (String)accessNames.get(0);
										DomainAccess.createObjectOwnershipForUserGroups(context, sInboxTaskId, sPersonName,defaultAccess , DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);
										sGroupOrRoleTitle = EnoviaResourceBundle.getProperty(context,"emxFrameworkStringResource",context.getLocale(), "emxFramework.Type.Group");
										String tempValues = MqlUtil.mqlCommand(context,"temp query bus $1 $2 $3 select $4 dump $5", PropertyUtil.getSchemaProperty(context, "type_GroupProxy"), sPersonName, "-", DomainConstants.SELECT_ATTRIBUTE_TITLE , "~");
										sPersonName = tempValues.split("~")[3];
										aliasRoleName = sPersonName ;
										sGRName =sGroupOrRoleTitle +" - "+ sPersonName;
									}else {
                        	inboxTask.setOwner(context, sPersonName);
                        }
								}
                        finally {
                            //Command to set "trigger on"
                             ContextUtil.popContext(context);
                             MqlUtil.mqlCommand(context, "unset env $1", "NEED_TO_UPDATE_OWNERSHIP");
                        }
                        String sRouteOwnerFullName =PersonUtil.getFullName(context, sRouteOwner);
                        Map<String,String> notifyDetails = new HashMap<String,String>();
                        notifyDetails.put(RouteTaskNotification.ROUTE_NAME, routeTitle);
                        notifyDetails.put(RouteTaskNotification.ROUTE_OWNER, sRouteOwner);
                        notifyDetails.put(RouteTaskNotification.ROUTE_ID, sRoutePhysicalId);
                        notifyDetails.put(RouteTaskNotification.TASK_ID, sInboxTaskPhysicalId);
                        notifyDetails.put(RouteTaskNotification.TASK_ACTION, sRouteAction);
                        notifyDetails.put(RouteTaskNotification.TASK_NAME, sInboxTaskTitle);
                        notifyDetails.put(RouteTaskNotification.TASK_INSTRUCTION,  sRouteInstructions);
                        notifyDetails.put(RouteTaskNotification.ROUTE_CONTENTS,  routeContentFor3D.toString());
                        notifyDetails.put(RouteTaskNotification.ROUTE_CONTENT_TITLES,  routeContentTitleFor3D.toString());
                        notifyDetails.put(RouteTaskNotification.ROUTE_CONTENT_IDS,  routeContentIdsFor3D.toString());
                        notifyDetails.put(RouteTaskNotification.ROUTE_CONTENT_TYPES,  routeContentTypesFor3D.toString());
                        notifyDetails.put(RouteTaskNotification.SENDER_USER,  context.getUser());
                        notifyDetails.put(RouteTaskNotification.DUE_DATE,sScheduledCompletionDate);
                        notifyDetails.put(RouteTaskNotification.ROUTE_OWNER_TASK,sRouteOwnerTask);
                        if(UIUtil.isNotNullAndNotEmpty(sRouteOwnerUserGroup)) {
                        	String groupTitle = MqlUtil.mqlCommand(context,"print bus $1 select $2 dump",sRouteOwnerUserGroup, DomainConstants.SELECT_ATTRIBUTE_TITLE);
                        	 notifyDetails.put(RouteTaskNotification.Route_Owner_UG_Choice,groupTitle);
                        }
                        RouteTaskNotification notifyObj = RouteTaskNotification.getInstance(context, notifyDetails.get("ROUTE_CONTENT_TYPES"));
                        // Send Icon mail to the Group or Role if applicable.
                        if ((sRPEType.equals(sInboxTaskType))
                                && !sRouteTaskUser.equals("")) {

                            String mqlReturn = MqlUtil.mqlCommand(context,"expand bus $1 $2 $3 from relationship $4 dump $5",sRPEType,sRPEName,sRPERev,sRelProjectTask,"|");
                            StringTokenizer ownerString = new StringTokenizer(mqlReturn, "|");
                            ownerString.nextToken();
                            ownerString.nextToken();
                            ownerString.nextToken();
                            ownerString.nextToken();
                            sRPEOwner = (String) ownerString.nextToken();
									sendMailNotificationIfTask(context, user, sPersonName, sRPEType, sRPEName,sRPETITLE, sRPERev,
											sGroupOrRoleTitle, sGRName, sRPEOwner, sInboxTaskId,strTranslatedInboxTaskType, sInboxTaskName,
											sInboxTaskRev, sRouteObjects, sRoleOrGroupToList, sType, sName, sRev, sTreeMenu,
									mailUtil, sRouteOwner,sRouteOwnerFullName,sRouteId,sRouteContent,notifyDetails,notifyObj);
                        } else if (!(sRPEType.equals(sInboxTaskType))
                                && !sRouteTaskUser.equals("")) {
                        	String tenantId = context.getTenant();
									sendMailNotificationIfRoute(context, user, sScheduledCompletionDate, sRouteInstructions,
									sGroupOrRoleTitle, sGRName, sInboxTaskId,sInboxTaskName, sInboxTaskRev,
									sRouteObjects, sRouteContent, sRouteContentDescription, sRouteId,aliasRoleName,
											sName, sRev, sTreeMenu, mailUtil, sRouteOwner,sRouteOwnerFullName, strTranslatedInboxTaskType,
									strTranslatedSType, strTranslatedRouteAction, tenantId,notifyDetails,notifyObj);
                        }

                        // Send Icon mail to the Person if applicable.

                        if (sRPEType.equals(sInboxTaskType)
                                && sRouteTaskUser.equals("")) {

                            String mqlReturn = MqlUtil.mqlCommand(context,"expand bus $1 $2 $3 from relationship $4 dump $5",sRPEType,sRPEName,sRPERev,sRelProjectTask,"|");
                            StringTokenizer ownerString = new StringTokenizer(
                                    mqlReturn, "|");
                            ownerString.nextToken();
                            ownerString.nextToken();
                            ownerString.nextToken();
                            ownerString.nextToken();
                            sRPEOwner = (String) ownerString.nextToken();

                            String tenantId = context.getTenant();
									if(isKindOfProxyGroup){
										sendMailNotificationIfTask(context, user, sPersonName, sRPEType, sRPEName,sRPETITLE, sRPERev,
												sGroupOrRoleTitle, sGRName, sRPEOwner, sInboxTaskId, strTranslatedInboxTaskType, sInboxTaskName,
												sInboxTaskRev, sRouteObjects, sRoleOrGroupToList, sType, sName, sRev, sTreeMenu,
										mailUtil, sRouteOwner,sRouteOwnerFullName,sRouteId,sRouteContent,notifyDetails,notifyObj);
									}else {
                            if (sRouteObjects.equals("")) {

                                if (sDelegatorId.equals("")) {
										String strInboxDueDate  = inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");
										
                                	Map payload = new HashMap();
                                    payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice");
                                    payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.MessageNotice");
                                    String[] messageKeys = {"IBType", "IBName", "RPEType", "RPEName", "RPEOwner", "Type", "Name", "ROwner"};
                                    String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, strTranslatedRPEType, sRPEName, sRPEOwner, strTranslatedSType, sName, sRouteOwnerFullName};
                                    payload.put("messageKeys", messageKeys);
                                    payload.put("messageValues", messageValues);
                                    payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableHeader");
                                    payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableData");
                                    String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions"};
                                    String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
                                    String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
                                    String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, strInboxDueDate, sRouteInstructions};
                                    payload.put("tableRowKeys", tableKeys);
                                    payload.put("tableRowValues", tableValues);
                                    payload.put("treeMenu", sTreeMenu);
                                    payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
                                    String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
                                    
                                    String[] clickKeys = {"url", "tenantinfo"};
                                    String[] clickValues = {sBaseURL, ""};
                                    if(UIUtil.isNotNullAndNotEmpty(tenantId)){
                                    	clickValues[1] = "&tenant="+tenantId;
                                    }
                                    payload.put("clickKeys", clickKeys);
                                    payload.put("clickValues", clickValues);

                                    mailUtil.setTreeMenuName(context, sTreeMenu);
                                    String oldAgentName = mailUtil.getAgentName(context, new String[] {});
                                    mailUtil.setAgentName(context, new String[] { sRouteOwner });
											emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,notifyType);
                                    mailUtil.setAgentName(context, new String[] { oldAgentName });
										HashMap hmParam		= new HashMap();
										hmParam.put("id", sInboxTaskId);
										String[] args1 = (String[])JPO.packArgs(hmParam);
										StringList tolist =  (StringList) JPO.invoke(context, "emxInboxTaskNotificationBase", null, "getRouteTaskOwner", args1, StringList.class);
										List<String> toList = new ArrayList<String>();
										for(String userName:tolist) {
											toList.add(userName);
											notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, userName);
										}        							
										notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
										notifyDetails.put(RouteTaskNotification.DUE_DATE, strInboxDueDate);
										notifyDetails.put(RouteTaskNotification.COMPLETED_TASK_NAME, sRPETITLE);
										notifyObj.sendNewTaskAssignment3DNotificationUponTaskCompletion(context, notifyDetails, toList);		
                                } else {
										String strInboxDueDate  = inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");

									Map payload = new HashMap();
									payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice");
                                    payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.DelegatorMessageNotice");
                                    String[] messageKeys = {"IBType", "IBName", "RPEType", "RPEName", "RPEOwner", "Type", "Name", "ROwner", "Delegator"};
                                    String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, strTranslatedRPEType, sRPEName, sRPEOwner, strTranslatedSType, sName, sRouteOwnerFullName, sPersonFirstName + " " + sPersonLastName};
                                    payload.put("messageKeys", messageKeys);
                                    payload.put("messageValues", messageValues);
                                    payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableHeader");
                                    payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableData");
                                    String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions"};
                                    String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
                                    String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);

                                    String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, strInboxDueDate, sRouteInstructions};
                                    payload.put("tableRowKeys", tableKeys);
                                    payload.put("tableRowValues", tableValues);
                                    payload.put("treeMenu", sTreeMenu);
                                    payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
                                    String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
                                    String[] clickKeys = {"url"};
                                    String[] clickValues = {sBaseURL};
                                    payload.put("clickKeys", clickKeys);
                                    payload.put("clickValues", clickValues);
                                    String[] toList = {sPersonName};
                                    payload.put("toList", toList);
									
									mailUtil.setTreeMenuName(context, sTreeMenu);
                                    String oldAgentName = mailUtil.getAgentName(context, new String[] {});
                                    mailUtil.setAgentName(context, new String[] {user});
												emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPRouteTaskDelegatedEvent", payload,notifyType);
                                    mailUtil.setAgentName(context, new String[] {oldAgentName});
										
										List<String> toOldPerson = new ArrayList<>();
										toOldPerson.add(oldTaskAssignee);
										
										notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, sPersonName); 
										notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
										notifyDetails.put(RouteTaskNotification.DUE_DATE, strInboxDueDate);
										notifyDetails.put(RouteTaskNotification.IS_DELEGATED, "Yes");
										notifyDetails.put(RouteTaskNotification.COMPLETED_TASK_NAME, sRPETITLE);
										
										notifyObj.sendNewTaskAssignment3DNotificationUponTaskCompletion(context, notifyDetails,toOldPerson);
                                }
                            } // eof if {sRouteObjects == ""}
                            else {

                                if (sDelegatorId.equals("")) {
										String strInboxDueDate  = inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");

                                	Map payload = new HashMap();
                                	payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice");
                                    payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.MessageNotice");
                                    String[] messageKeys = {"IBType", "IBName", "RPEType", "RPEName", "RPEOwner", "Type", "Name", "ROwner"};
                                    String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, strTranslatedRPEType, sRPEName, sRPEOwner, strTranslatedSType, sName, sRouteOwnerFullName};
                                    payload.put("messageKeys", messageKeys);
                                    payload.put("messageValues", messageValues);
                                    payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableWithContentHeader");
                                    payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableWithContentData");
                                    String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions", "Content", "ContentDescription"};
                                    String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
                                    String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
                                    String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, strInboxDueDate, sRouteInstructionsStripped, sRouteContent.toString(), sRouteContentDescription.toString()};
                                    
                                    payload.put("tableRowKeys", tableKeys);
                                    payload.put("tableRowValues", tableValues);
                                    payload.put("treeMenu", sTreeMenu);
                                    payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
                                    String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
                                    String[] clickKeys = {"url", "tenantinfo"};
                                    String[] clickValues = {sBaseURL, ""};
                                    if(UIUtil.isNotNullAndNotEmpty(tenantId)){
                                    	clickValues[1] = "&tenant="+tenantId;
                                    }
                                    payload.put("clickKeys", clickKeys);
                                    payload.put("clickValues", clickValues);

                                    mailUtil.setTreeMenuName(context, sTreeMenu);
                                    String oldAgentName = mailUtil.getAgentName(context, new String[] {});
                                    String contextUser = PropertyUtil.getGlobalRPEValue(context,ContextUtil.MX_LOGGED_IN_USER_NAME);
                                    mailUtil.setAgentName(context, new String[] { contextUser });
											emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,notifyType);
                                    mailUtil.setAgentName(context, new String[] { oldAgentName });
										HashMap hmParam		= new HashMap();
										hmParam.put("id", sInboxTaskId);
										String[] args1 = (String[])JPO.packArgs(hmParam);
										StringList tolist =  (StringList) JPO.invoke(context, "emxInboxTaskNotificationBase", null, "getRouteTaskOwner", args1, StringList.class);

										notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, sPersonName); 
										notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
										notifyDetails.put(RouteTaskNotification.DUE_DATE, strInboxDueDate);
										notifyDetails.put(RouteTaskNotification.COMPLETED_TASK_NAME, sRPETITLE);
										notifyObj.sendNewTaskAssignment3DNotificationUponTaskCompletion(context, notifyDetails, tolist);
                                } else {
										String strInboxDueDate  = inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");

                                	Map payload = new HashMap();
                                    payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice");
                                    payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.DelegatorMessageNotice");
                                    String[] messageKeys = {"IBType", "IBName", "RPEType", "RPEName", "RPEOwner", "Type", "Name", "ROwner", "Delegator"};
                                    String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, strTranslatedRPEType, sRPEName, sRPEOwner, strTranslatedSType, sName, sRouteOwnerFullName, sPersonFirstName + " " + sPersonLastName};
                                    payload.put("messageKeys", messageKeys);
                                    payload.put("messageValues", messageValues);
                                    payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableWithContentHeader");
                                    payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableWithContentData");
                                    String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions", "Content", "ContentDescription"};
                                    String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
                                    String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);

                                    String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, strInboxDueDate, sRouteInstructionsStripped, sRouteContent.toString(), sRouteContentDescription.toString()};
                                    payload.put("tableRowKeys", tableKeys);
                                    payload.put("tableRowValues", tableValues);
                                    payload.put("treeMenu", sTreeMenu);
                                    payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
                                    String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
                                    String[] clickKeys = {"url"};
                                    String[] clickValues = {sBaseURL};
                                    payload.put("clickKeys", clickKeys);
                                    payload.put("clickValues", clickValues);
                                    String[] toList = {sPersonName};
                                    payload.put("toList", toList);

                                    mailUtil.setTreeMenuName(context, sTreeMenu);
                                    String oldAgentName = mailUtil.getAgentName(context, new String[] {});
                                    mailUtil.setAgentName(context, new String[] {user});
											emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPRouteTaskDelegatedEvent", payload,notifyType);
                                    mailUtil.setAgentName(context, new String[] {oldAgentName});
										
											List<String> toOldPerson = new ArrayList<String>();
											toOldPerson.add(oldTaskAssignee);
										List<String> toListPerson = new ArrayList<String>();
										toListPerson.add(sPersonName);
										
										notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, sPersonName); 
										notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
										notifyDetails.put(RouteTaskNotification.DUE_DATE, strInboxDueDate);
										notifyDetails.put(RouteTaskNotification.COMPLETED_TASK_NAME, sRPETITLE);
										notifyDetails.put(RouteTaskNotification.DELEGATOR, oldTaskAssignee);
										notifyDetails.put(RouteTaskNotification.IS_DELEGATED, "Yes");
										notifyObj.sendNewTaskAssignment3DNotificationUponTaskCompletion(context, notifyDetails, toOldPerson);
                                }
                            }
                        }} else if (!sRPEType.equals(sInboxTaskType)
                                && sRouteTaskUser.equals("")) {
                        	String tenantId = context.getTenant();
										if(isKindOfProxyGroup){
											sendMailNotificationIfRoute(context, user, sScheduledCompletionDate, sRouteInstructions,
											sGroupOrRoleTitle, sGRName, sInboxTaskId,sInboxTaskName, sInboxTaskRev,
											sRouteObjects, sRouteContent, sRouteContentDescription,sRouteId,aliasRoleName,
													sName, sRev, sTreeMenu, mailUtil, sRouteOwner,sRouteOwnerFullName, strTranslatedInboxTaskType,
											strTranslatedSType, strTranslatedRouteAction, tenantId,notifyDetails,notifyObj);
										}else {
                            if (sRouteObjects.equals("")) {

                                if (sDelegatorId.equals("")) {
											String dueDate =inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");

                                	Map payload = new HashMap();
                                    payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice2");
                                    payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.FirstMessage");
                                    String[] messageKeys = {"IBType", "IBName", "Type", "Name", "ROwner"};
                                    String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, strTranslatedSType, sName, sRouteOwnerFullName};
                                    payload.put("messageKeys", messageKeys);
                                    payload.put("messageValues", messageValues);
                                    payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableHeader");
                                    payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableData");
                                    String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions"};
                                    String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
                                    String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
                                    String strInboxDueDate  = inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");
                                    String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, strInboxDueDate, sRouteInstructions};
                                    payload.put("tableRowKeys", tableKeys);
                                    payload.put("tableRowValues", tableValues);
                                    payload.put("treeMenu", sTreeMenu);
                                    payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
                                    String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
                                    String[] clickKeys = {"url", "tenantinfo"};
                                    String[] clickValues = {sBaseURL, ""};
                                    if(UIUtil.isNotNullAndNotEmpty(tenantId)){
                                    	clickValues[1] = "&tenant="+tenantId;
                                    }
                                    payload.put("clickKeys", clickKeys);
                                    payload.put("clickValues", clickValues);

                                    mailUtil.setTreeMenuName(context, sTreeMenu);
                                    String oldAgentName = mailUtil.getAgentName(context, new String[] {});
                                    mailUtil.setAgentName(context, new String[] { user });
												emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,notifyType);
                                    mailUtil.setAgentName(context, new String[] { oldAgentName });
											HashMap hmParam		= new HashMap();
											hmParam.put("id", sInboxTaskId);
											String[] args1 = (String[])JPO.packArgs(hmParam);
											StringList tolist =  (StringList) JPO.invoke(context, "emxInboxTaskNotificationBase", null, "getRouteTaskOwner", args1, StringList.class);
											List<String> toList = new ArrayList<String>();
											for(String userName:tolist) {
												toList.add(userName);
												notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, userName); 
											}
											notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
											notifyDetails.put(RouteTaskNotification.DUE_DATE, strInboxDueDate);
											notifyDetails.put(RouteTaskNotification.SENDER_USER, user);
											notifyObj.sendNewTaskAssignment3DNotification(context, notifyDetails, toList);
										
                                } else {

                                	Map payload = new HashMap();
                                    payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice2");
                                    payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.FirstDelegatorMessage");
                                    String[] messageKeys = {"IBType", "IBName", "Type", "Name", "ROwner", "Delegator"};
                                    String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, strTranslatedSType, sName, sRouteOwnerFullName, sPersonFirstName + " " + sPersonLastName};
                                    payload.put("messageKeys", messageKeys);
                                    payload.put("messageValues", messageValues);
                                    payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableHeader");
                                    payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableData");
                                    String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions"};
                                    String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
                                    String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
                                    String strInboxDueDate  = inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");
                                    String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, strInboxDueDate, sRouteInstructions};
                                    payload.put("tableRowKeys", tableKeys);
                                    payload.put("tableRowValues", tableValues);
                                    payload.put("treeMenu", sTreeMenu);
                                    payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
                                    String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
                                    String[] clickKeys = {"url", "tenantinfo"};
                                    String[] clickValues = {sBaseURL, ""};
                                    if(UIUtil.isNotNullAndNotEmpty(tenantId)){
                                    	clickValues[1] = "&tenant="+tenantId;
                                    }
                                    payload.put("clickKeys", clickKeys);
                                    payload.put("clickValues", clickValues);

                                    mailUtil.setTreeMenuName(context, sTreeMenu);
                                    String oldAgentName = mailUtil.getAgentName(context, new String[] {});
                                    mailUtil.setAgentName(context, new String[] { user });
												emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,notifyType);
                                    mailUtil.setAgentName(context, new String[] { oldAgentName });
						
											HashMap hmParam		= new HashMap();
											hmParam.put("id", sInboxTaskId);
											String[] args1 = (String[])JPO.packArgs(hmParam);
											StringList tolist =  (StringList) JPO.invoke(context, "emxInboxTaskNotificationBase", null, "getRouteTaskOwner", args1, StringList.class);
											for(String userName:tolist) {
												notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, userName);
											}
											List<String> toOldList = new ArrayList<String>();
											toOldList.add(oldTaskAssignee);
											notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
											notifyDetails.put(RouteTaskNotification.DELEGATOR, oldTaskAssignee);
											notifyDetails.put(RouteTaskNotification.IS_DELEGATED, "Yes");
											notifyDetails.put(RouteTaskNotification.DUE_DATE, sScheduledCompletionDate);
											notifyDetails.put(RouteTaskNotification.SENDER_USER, user);
											notifyObj.sendNewTaskAssignment3DNotification(context, notifyDetails, toOldList);
                                }
                            } else {

                                if (sDelegatorId.equals("")) {

                                	Map payload = new HashMap();
                                    payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice2");
                                    payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.FirstMessage");
                                    String[] messageKeys = {"IBType", "IBName", "Type", "Name", "ROwner"};
                                    String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, strTranslatedSType, sName, sRouteOwnerFullName};
                                    payload.put("messageKeys", messageKeys);
                                    payload.put("messageValues", messageValues);
                                    payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableWithContentHeader");
                                    payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableWithContentData");
                                    String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions", "Content", "ContentDescription"};
                                    String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
                                    String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
                                    String strInboxDueDate  = inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");
                                    String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, strInboxDueDate, sRouteInstructionsStripped, sRouteContent.toString(), sRouteContentDescription.toString()};
                                    
                                    payload.put("tableRowKeys", tableKeys);
                                    payload.put("tableRowValues", tableValues);
                                    payload.put("treeMenu", sTreeMenu);
                                    payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
                                    String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
                                    String[] clickKeys = {"url", "tenantinfo"};
                                    String[] clickValues = {sBaseURL, ""};
                                    if(UIUtil.isNotNullAndNotEmpty(tenantId)){
                                    	clickValues[1] = "&tenant="+tenantId;
                                    }
                                    payload.put("clickKeys", clickKeys);
                                    payload.put("clickValues", clickValues);

                                    mailUtil.setTreeMenuName(context, sTreeMenu);
                                    String oldAgentName = mailUtil.getAgentName(context, new String[] {});
                                    mailUtil.setAgentName(context, new String[] { user });
												emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,notifyType);
                                    mailUtil.setAgentName(context, new String[] { oldAgentName });


											HashMap hmParam		= new HashMap();
											hmParam.put("id", sInboxTaskId);
											String[] args1 = (String[])JPO.packArgs(hmParam);
											StringList tolist =  (StringList) JPO.invoke(context, "emxInboxTaskNotificationBase", null, "getRouteTaskOwner", args1, StringList.class);
											List<String> toList = new ArrayList<String>();
											for(String userName:tolist) {
												toList.add(userName);
												notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, userName);
											}
											notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
											notifyDetails.put(RouteTaskNotification.DUE_DATE, sScheduledCompletionDate);
											notifyDetails.put(RouteTaskNotification.SENDER_USER, user);
											notifyObj.sendNewTaskAssignment3DNotification(context, notifyDetails, toList);
													
                                } else {

                                	Map payload = new HashMap();
                                    payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice2");
                                    payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.FirstDelegatorMessage");
                                    String[] messageKeys = {"IBType", "IBName", "Type", "Name", "ROwner", "Delegator"};
                                    String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, strTranslatedSType, sName, sRouteOwnerFullName, sPersonFirstName + " " + sPersonLastName};
                                    payload.put("messageKeys", messageKeys);
                                    payload.put("messageValues", messageValues);
                                    payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableWithContentHeader");
                                    payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableWithContentData");
                                    String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions", "Content", "ContentDescription"};
                                    String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
                                    String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
									String strInboxDueDate  = inboxTask.getInfo(context,"attribute[" + sAttScheduledCompletionDate+ "]");
                                    String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, strInboxDueDate, sRouteInstructionsStripped, sRouteContent.toString(), sRouteContentDescription.toString()};
                                    payload.put("tableRowKeys", tableKeys);
                                    payload.put("tableRowValues", tableValues);
                                    payload.put("treeMenu", sTreeMenu);
                                    payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
                                    String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
                                    String[] clickKeys = {"url", "tenantinfo"};
                                    String[] clickValues = {sBaseURL, ""};
                                    if(UIUtil.isNotNullAndNotEmpty(tenantId)){
                                    	clickValues[1] = "&tenant="+tenantId;
                                    }
                                    payload.put("clickKeys", clickKeys);
                                    payload.put("clickValues", clickValues);

                                    mailUtil.setTreeMenuName(context, sTreeMenu);
                                    String oldAgentName = mailUtil.getAgentName(context, new String[] {});
                                    mailUtil.setAgentName(context, new String[] { user });
												emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,notifyType);
                                    mailUtil.setAgentName(context, new String[] { oldAgentName });
											HashMap hmParam		= new HashMap();
											hmParam.put("id", sInboxTaskId);
											String[] args1 = (String[])JPO.packArgs(hmParam);
											StringList tolist =  (StringList) JPO.invoke(context, "emxInboxTaskNotificationBase", null, "getRouteTaskOwner", args1, StringList.class);
											List<String> toList = new ArrayList<String>();
											for(String userName:tolist) {
												toList.add(userName);
												notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, userName);
											}
											List<String> toOldList = new ArrayList<String>();
											toOldList.add(oldTaskAssignee);
											notifyDetails.put(RouteTaskNotification.DUE_DATE, sScheduledCompletionDate);
											notifyDetails.put(RouteTaskNotification.SENDER_USER, user);
											notifyDetails.put(RouteTaskNotification.DELEGATOR, oldTaskAssignee);
											notifyDetails.put(RouteTaskNotification.IS_DELEGATED, "Yes");
											notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
											notifyObj.sendNewTaskAssignment3DNotification(context, notifyDetails,toOldList);
											
                                }
                            }
										}
                        }//
                        // PushPopShadowAgent.popContext(context,sRouteDelegationGrantor);//popShadowAgent
                        // Inform route owner that task was delegated to someone
                        // else
                        if (!sDelegatorId.equals("")) {
							
                        	String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
							String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
							
							String taskNameLink=sInboxTaskName;
                            String routeNameLink=sName;
                            if (sInboxTaskLink.contains("<a href")) {
                            	 String[] inboxUrl=sInboxTaskLink.split("'");
                            	 taskNameLink=inboxUrl[1];
                            }

                            if (sRouteLink.contains("<a href")) {
                            	 String[] sRouteUrl=sRouteLink.split("'");
                            	 routeNameLink=sRouteUrl[1];
                            }

                            String subjectKey = "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice";
							String messageKey = "emxFramework.ProgramObject.eServicecommonInitiateRoute.MessageDelegator";
							String[] messageTextKeys = {"IBName","Name","Delegator","FullName"};
							String[] messageTextValues = {sInboxTaskName, sName, sDelegatorFullName,  sPersonFirstName + " " + sPersonLastName};
							String[] messageTextValues1 = {taskNameLink, routeNameLink, sDelegatorFullName,  sPersonFirstName + " " + sPersonLastName};
							String basePropName = MessageUtil.getBundleName(context);

							String subject = MessageUtil.getMessage(context, null, subjectKey, null, null, null, context.getLocale(), basePropName);
							String messageText = MessageUtil.getMessage(context, null, messageKey, messageTextKeys, messageTextValues, null, context.getLocale(), basePropName);
							String messageText1 = MessageUtil.getMessage(context, null, messageKey, messageTextKeys, messageTextValues1, null, context.getLocale(), basePropName);

							StringList toList = new StringList();
							toList.addElement(sPersonName);

							String oldAgentName = mailUtil.getAgentName(context, new String[] {});

							mailUtil.setAgentName(context, new String[] { user });
								emxNotificationUtil_mxJPO.sendJavaMail(context, toList, null, null, subject, messageText1, null, null, toList, null, notifyType);
							mailUtil.setAgentName(context, new String[] { oldAgentName });
							notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, sPersonName);
							notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "Person");
							notifyDetails.put(RouteTaskNotification.DELEGATOR, oldTaskAssignee);
							notifyDetails.put(RouteTaskNotification.SENDER_USER, user);
							notifyObj.sendTaskDelegated3DNotificationToRouteOwner(context, notifyDetails);
                        }
                    }
                    // Set Route Status attribute on Route to equal Started
                    // Set Current Route Node attribute on Route to equal 1
                    PushPopShadowAgent.pushContext(context, null);
                    RouteObject.setAttributeValue(context, "" + sAttCurrentRouteNode,
                            "" + sRouteSequenceValue);
                    //RouteObject.setAttributeValue(context, sAttRouteStatus, "Started");
                    PushPopShadowAgent.popContext(context, null);
                    
                    nCurrentRouteSequenceStarted = sRouteSequenceValue;
                }// eof for loop
                if (bPersonFound == 1) {
                    break;
                }
                if (bGreaterSeqNoFound == 1) {
                    sRouteSequenceValue++;
                    // return 1;
                }
            }// eof while loop
            RouteObject.setAttributeValue(context, sAttRouteStatus, "Started");
            // Error if no Person object found
            if (bPersonFound == 0 && bErrorOnNoConnection == 1) {

                if (sRouteTaskUser.length()==0) {

                    String[] mailArguments = new String[11];
                    mailArguments[0] = "emxFramework.ProgramObject.eServicecommonInitiateRoute.NoObjectError";
                    mailArguments[1] = "4";
                    mailArguments[2] = "Person";
                    mailArguments[3] = sTypePerson;
                    mailArguments[4] = "Type";
                    mailArguments[5] = sType;
                    mailArguments[6] = "Name";
                    mailArguments[7] = sName;
                    mailArguments[8] = "Rev";
                    mailArguments[9] = sRev;
                    mailArguments[10] = "FullName";
                    mailUtil.setTreeMenuName(context, sTreeMenu);
                    // Calling method to send Mail
                    Message = mailUtil.getMessage(context, mailArguments);
                } else {

                    String[] mailArguments = new String[10];
                    mailArguments[0] = "emxFramework.ProgramObject.eServicecommonInitiateRoute.NoObjectError";
                    mailArguments[1] = "4";
                    mailArguments[2] = "Person";
                    mailArguments[3] = sTypeRouteTaskUser;
                    mailArguments[4] = "Type";
                    mailArguments[5] = sType;
                    mailArguments[6] = "Name";
                    mailArguments[7] = sName;
                    mailArguments[8] = "Rev";
                    mailArguments[9] = sRev;
                    mailUtil.setTreeMenuName(context, sTreeMenu);
                    // Calling method to send Mail
                    Message = mailUtil.getMessage(context, mailArguments);
                }// eof else
                if (Message.length() > 0) {
                    MqlUtil.mqlCommand(context, "notice $1", Message);
                    return 1;
                }
            }// eof if (bPersonFound == 0 && bErrorOnNoConnection == 1)
            //
            // Following code will take care to auto complete the Notify Only tasks.
            // This code is required to be after commiting the transaction because, on completing the
            // Notify Only task, the completion will again lead to execute of this method, which should get
            // Complete state for this Notify Only task
            //
            int nCurrentSequence = 0;
            String strCurrentRouteAction = "";
            String strCurrentRouteNodeId = "";
            String strTaskIdToComplete = "";
            String strReviewTask = "";
            DomainObject dmoTask = null;
            MapList mlTasks = null;
            emxContextUtil_mxJPO objContextUtil = new emxContextUtil_mxJPO(context, new String[0]);
            
            // Get all the tasks of the route
            slTaskSelect = new StringList(DomainObject.SELECT_ID);
            slTaskSelect.add(DomainObject.SELECT_CURRENT);
            slTaskSelect.add("attribute[" + sAttRouteNodeID + "]");
            slTaskSelect.add("attribute[" + sAttReviewTask + "]");
					slTaskSelect.add(SELECT_INITIAL_USER_GROUP_ID);

            // If the route is started then only think of auto completing Notify Only tasks
            if ("Started".equals(RouteObject.getAttributeValue(context, sAttRouteStatus)) && nCurrentRouteSequenceStarted != 0) {
                
                for (Iterator itrRouteNodes = routedNodeObjects.iterator(); itrRouteNodes.hasNext();) {
                    Map mapRouteNodeInfo = (Map)itrRouteNodes.next();
                    
                    // Get only route node info maps on current level
                    nCurrentSequence = Integer.parseInt((String)mapRouteNodeInfo.get("attribute[" + sAttRouteSequence + "]"));
                    if (nCurrentSequence != nCurrentRouteSequenceStarted) {
                        continue;
                    }
                    
                    // Get the notify only task
                    strCurrentRouteAction = (String)mapRouteNodeInfo.get("attribute[" + sAttRouteAction + "]");
                    if (!"Notify Only".equals(strCurrentRouteAction)) {
                        continue;
                    }
                    
                    // Get the task corresponding to this route node relationship.
                    strCurrentRouteNodeId = (String)mapRouteNodeInfo.get("attribute["+ sAttRouteNodeID + "]");
                    
                    mlTasks = RouteObject.getRelatedObjects(context, 
                                                        sRelRouteTask, 
                                                        DomainObject.TYPE_INBOX_TASK,
                                                        slTaskSelect, 
                                                        null, 
                                                        true, 
                                                        false, 
                                                        (short) 1,
                                                        //"attribute[" + sAttRouteNodeID + "]=='" + sRouteNodeId + "' && current!='" + POLICY_INBOX_TASK_STATE_COMPLETE + "'", 
                                                        "attribute[" + sAttRouteNodeID + "]=='" + strCurrentRouteNodeId + "'", // The above expression is not working!
                                                        "");
                    
                    if (mlTasks == null || mlTasks.size() <= 0) {
                        continue;
                    }
                    
                    // Complete all such tasks
                    for (Iterator itrTasks = mlTasks.iterator(); itrTasks.hasNext();) {
                        mapTaskInfo = (Map)itrTasks.next();
                        
                        if (!POLICY_INBOX_TASK_STATE_COMPLETE.equals((String)mapTaskInfo.get(DomainObject.SELECT_CURRENT))) {
                            
                            strTaskIdToComplete = (String)mapTaskInfo.get(DomainObject.SELECT_ID);
                            strReviewTask = (String)mapTaskInfo.get("attribute[" + sAttReviewTask + "]");
                            
                            dmoTask = new DomainObject(strTaskIdToComplete);
                            
                            //
                            // If this is not Review Task, then promote action trigger on Assigned state will promote it to Complete
                            // If it is Review Task then it is to be completed through Review state. A Check trigger on
                            // Review Promote event, throws error if promotion is not being carried by Route Owner.
                            // Due to this, the Review Task is made momentary No.
                            //
                            if ("No".equals(strReviewTask)) {
                                dmoTask.promote(context);
                            }
                            else {
                            	try{
                            		 MqlUtil.mqlCommand(context,"history off;", true);
                                     dmoTask.setAttributeValue(context, sAttReviewTask, "No");
                            	}finally{
                            		MqlUtil.mqlCommand(context,"history on;", true);
                            	}
                                dmoTask.promote(context);
                                try{
                                     ContextUtil.pushContext(context);
                                     MqlUtil.mqlCommand(context,"history off;", true);
                                     dmoTask.setAttributeValue(context, sAttReviewTask, "Yes");
                            	}finally{
                            	    MqlUtil.mqlCommand(context,"history on;", true);
                            		ContextUtil.popContext(context);
                            	}
                                
                            }
                            
                        }//if not completed tasks
                    }//for tasks
                }// for route nodes
            }//If Started route
            ContextUtil.commitTransaction(context);
            if (bErrorOnNoConnection == 0) {
                return bPersonFound;
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(" Exception in emxCommonInitiateRouteBase.InitiateRoute() : " + e);
            ContextUtil.abortTransaction(context);
            return 1;
        }
    }
     
     public static int checkInactiveAssigneeResumeRoute(Context context, String args[]) {
   		try {
   			String user=context.getUser();
   			String routeId = args[0];
           	DomainObject Route = new DomainObject(routeId);
           	StringList objectSelects = new StringList();
   			objectSelects.add(DomainConstants.SELECT_OWNER);
   			objectSelects.add(DomainConstants.SELECT_TYPE);
   			objectSelects.add(DomainConstants.SELECT_REVISION);
   			objectSelects.add(DomainConstants.SELECT_NAME);
   			objectSelects.add(DomainConstants.SELECT_ATTRIBUTE_TITLE);
   			objectSelects.add("attribute[" +sAttCurrentRouteNode+"]");
   			objectSelects.add(DomainConstants.SELECT_PHYSICAL_ID);
   			Map routedetails = Route.getInfo(context, objectSelects);
   			String sOwner = (String) routedetails.get(DomainConstants.SELECT_OWNER);
   			String sRouteType  = (String) routedetails.get(DomainConstants.SELECT_TYPE);
   			String sRouteName  = (String) routedetails.get(DomainConstants.SELECT_NAME);
   			String sRouteTitle  = (String) routedetails.get(DomainConstants.SELECT_ATTRIBUTE_TITLE);
   			String sRouteRev  = (String) routedetails.get(DomainConstants.SELECT_REVISION);
   			int sCurrentRouteNode  = Integer.parseInt((String) routedetails.get("attribute[" +sAttCurrentRouteNode+"]"));
   			String sRoutePhysicalId  = (String) routedetails.get(DomainConstants.SELECT_PHYSICAL_ID);
   			
   			String sType = sRouteType;
   			String sName = sRouteName;
   			String sRev = sRouteRev;
   			int sRouteSequenceValue = sCurrentRouteNode;
   			
   			// object Selects
               SelectList routeNodeObjectSelects = new SelectList();
               routeNodeObjectSelects.addElement(DomainConstants.SELECT_ID);
               routeNodeObjectSelects.addElement(DomainConstants.SELECT_NAME);
               routeNodeObjectSelects.addElement(DomainConstants.SELECT_TYPE);
               routeNodeObjectSelects.addElement(DomainConstants.SELECT_CURRENT);
               // relationship selects
               SelectList routeNodeRelationshipSelects = new SelectList();
               routeNodeRelationshipSelects
                       .addElement(DomainConstants.SELECT_PHYSICAL_ID);
               routeNodeRelationshipSelects.addElement("attribute["
                       + sAttRouteSequence + "]");
               
               MapList routedNodeObjects = Route.getRelatedObjects(context,
                       sRelRouteNode, "*", routeNodeObjectSelects,
                       routeNodeRelationshipSelects, false, true, (short) 1, "",
                       "");
               // Sorting the Maplist Based on the Route Sequence.
               routedNodeObjects.sort("attribute[" + sAttRouteSequence + "]",
                       "ascending", "String");
               
            // Building the Object of the Mail Util to be used Later
               emxMailUtil_mxJPO mailUtil = new emxMailUtil_mxJPO(context, null);
               
               boolean abortInitiateTask = checkInactiveAssignee(context, routedNodeObjects, Route, sType, sName, sRev, mailUtil, user, sRouteSequenceValue, sRoutePhysicalId,sRouteTitle);
               
               if(abortInitiateTask)
   			{
   				com.matrixone.apps.common.Route.stopRoute(context, routeId);
   				String sMsg = EnoviaResourceBundle.getProperty(context,"emxFrameworkStringResource", new Locale(context.getSession().getLanguage()),"emxFramework.ProgramObject.eServicecommonRoute.GenericMessageInactivePerson");
   				MqlUtil.mqlCommand(context, "notice $1", sMsg);
                   return 1;
   			}
   			
   			return 0;

   		} catch (Exception e) {
   			ContextUtil.abortTransaction(context);
   			return 1;
   		}
   	}

      private static boolean checkInactiveAssignee(Context context, MapList routedNodeObjects, DomainObject Route, String sType, String sName, String sRev, emxMailUtil_mxJPO mailUtil, String user, int sRouteSequenceValue, String sRoutePhysicalId, String routeTitle) throws Exception 
      {
  		boolean abortInitiateTask = false;
      	int nRoutedNodeObjectsSize = routedNodeObjects.size();
      	final String PROXY_GROUP_TYPE = PropertyUtil.getSchemaProperty(context,"type_GroupProxy");
 		final String GROUP_TYPE = PropertyUtil.getSchemaProperty(context,"type_Group");
 		String mailEnabled = EnoviaResourceBundle.getProperty(context,"emxFramework.Routes.EnableMails");
      	
 		//Get all Route tasks & current states
 		SelectList objectSelects = new SelectList();
        objectSelects.add("to[Route Task].from.attribute[Route Node ID].value");
        objectSelects.add("to[Route Task].from.current");
        java.util.List multivalueList = new java.util.ArrayList();
        multivalueList.add("to[Route Task].from.attribute[Route Node ID].value");
        multivalueList.add("to[Route Task].from.current");
        Map objectInfo = Route.getInfo(context, objectSelects, multivalueList);
        
        //Create Two Arrays. One Contain Route Node Ids and another one contains corresponding status of those Route Node
        StringList routeNodeIds = (StringList) objectInfo.get("to[Route Task].from.attribute[Route Node ID].value");
        StringList statusList = (StringList) objectInfo.get("to[Route Task].from.current");
             
      	for (int i = 0; i < nRoutedNodeObjectsSize; i++) {
              Map objectMapInactive = (Map) routedNodeObjects.get(i);
              String currentState =(String) objectMapInactive.get(DomainConstants.SELECT_CURRENT);
  				String sPersonNameInactive = (String) objectMapInactive.get(DomainConstants.SELECT_NAME);
  				String objectType  = (String)objectMapInactive.get(DomainConstants.SELECT_TYPE);
 				boolean isKindOfProxyGroup = PROXY_GROUP_TYPE.equals(objectType) || GROUP_TYPE.equals(objectType);
 				boolean isUGAlreadyConnectedToTask = false;
  				
 				String routeNodeId = (String)objectMapInactive.get(DomainConstants.SELECT_PHYSICAL_ID);
 				
  				boolean isTaskCompleted = false;
  				if(routeNodeIds != null && statusList != null ){
  					int index = routeNodeIds.indexOf(routeNodeId);
  					if(index != -1 && "Complete".equals(statusList.get(index))) {
  						isTaskCompleted = true;  						
  	  				}
  				}
  				
  				if ("Inactive".equals(currentState) && !isKindOfProxyGroup && !isTaskCompleted) {

                   // Send Iconmail to Route owner about the task issue
                      String sOwner = Route.getInfo(context, "owner");
                      // Added for Bugs 328159 Begin
                      String oldAgentName = mailUtil.getAgentName(
                              context, new String[] {});
                      // Added for Bugs 328159 End
                      mailUtil.setAgentName(context,
                              new String[] { user });
							StringList toList = new StringList();
							toList.add(sOwner);
							String[] messageKeys = {"RPerson","RType","RName","RRev"};
							String[] messageValues = {sPersonNameInactive,sType,sName,sRev};
							mailUtil.sendNotification(context, toList,null, null,"emxFramework.ProgramObject.eServicecommonRoute.SubjectInactivePerson",null,null, "emxFramework.ProgramObject.eServicecommonRoute.MessageInactivePerson",messageKeys, messageValues,null,null, "true".equalsIgnoreCase(mailEnabled));
                      // Added for Bugs 328159 Begin
                      mailUtil.setAgentName(context,
                              new String[] { oldAgentName });
					  Map<String, String> details = new HashMap<>();
                      details.put(RouteTaskNotification.ROUTE_OWNER, sOwner);
                      details.put(RouteTaskNotification.ROUTE_ID, sRoutePhysicalId);
                      details.put(RouteTaskNotification.ROUTE_NAME, routeTitle);
                      details.put(RouteTaskNotification.SENDER_USER, user);
                      RouteTaskNotification notifyObj = RouteTaskNotification.getInstance(context, "");
                      notifyObj.sendInactivePerson3DNotification(context, details, sPersonNameInactive);
						
                      // Added for Bugs 328159 End
                      // Commented for IR-013125V6R2011
                      	abortInitiateTask = true;
                      
                      
  						
                  
  				}
              
  		}
      	
      	return abortInitiateTask;
      }

	private static void sendMailNotificationIfRoute(Context context, String user, String sScheduledCompletionDate,
			String sRouteInstructions, String sGroupOrRoleTitle, String sGRName, String sInboxTaskId,
			String sInboxTaskName, String sInboxTaskRev, String sRouteObjects, String sRouteContent,
			String sRouteContentDescription, String sRouteId,String aliasRoleName, String sName, String sRev,
			String[] sTreeMenu, emxMailUtil_mxJPO mailUtil, String sRouteOwner,String sRouteOwnerFullName, String strTranslatedInboxTaskType,
			String strTranslatedSType, String strTranslatedRouteAction, String tenantId,Map<String,String> notifyDetails,RouteTaskNotification notifyObj) throws Exception {
		String mailEnabled = EnoviaResourceBundle.getProperty(context,"emxFramework.Routes.EnableMails");
		if (sRouteObjects.equals("")) {

			Map payload = new HashMap();
			payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice3");
			String[] subjectKeys = {"Name", "GroupOrRole"};
			String[] subjectValues = {aliasRoleName,sGroupOrRoleTitle};
			payload.put("subjectKeys", subjectKeys);
			payload.put("subjectValues", subjectValues);
			payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.FirstMessage2");
			String[] messageKeys = {"IBType", "IBName", "IBRev", "GRName", "Type", "Name", "Rev", "ROwner"};
			String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, sInboxTaskRev, sGRName, strTranslatedSType, sName, sRev, sRouteOwnerFullName};
			payload.put("messageKeys", messageKeys);
			payload.put("messageValues", messageValues);
			payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableHeader");
			payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableData");
			String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions"};
			String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
			String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
			String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, sScheduledCompletionDate, sRouteInstructions};
			payload.put("tableRowKeys", tableKeys);
			payload.put("tableRowValues", tableValues);
			payload.put("treeMenu", sTreeMenu);
			payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
			String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);

			String[] clickKeys = {"url", "tenantinfo"};
			String[] clickValues = {sBaseURL, ""};
			if(UIUtil.isNotNullAndNotEmpty(tenantId)){
				clickValues[1] = "&tenant="+tenantId;
			}
			payload.put("clickKeys", clickKeys);
			payload.put("clickValues", clickValues);

			mailUtil.setTreeMenuName(context, sTreeMenu);
			String oldAgentName = mailUtil.getAgentName(context, new String[] {});
			mailUtil.setAgentName(context, new String[] { sRouteOwner });
			if("true".equalsIgnoreCase(mailEnabled)){
				emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,"both");
			}else {
				emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,"iconmail");
			}
			mailUtil.setAgentName(context, new String[] { oldAgentName });
		} else {

			Map payload = new HashMap();
			payload.put("subject", "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice3");
			String[] subjectKeys = {"Name", "GroupOrRole"};
			String[] subjectValues = {aliasRoleName, sGroupOrRoleTitle};
			payload.put("subjectKeys", subjectKeys);
			payload.put("subjectValues", subjectValues);
			payload.put("message", "emxFramework.ProgramObject.eServicecommonInitiateRoute.FirstMessageWithObjects2");
			String[] messageKeys = {"IBType", "IBName", "IBRev", "GRName", "Type", "Name", "Rev", "ROwner", "RObjects"};
			String[] messageValues = {strTranslatedInboxTaskType, sInboxTaskName, sInboxTaskRev, sGRName, strTranslatedSType, sName, sRev, sRouteOwnerFullName, sRouteObjects};
			payload.put("messageKeys", messageKeys);
			payload.put("messageValues", messageValues);
			payload.put("tableHeader", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableHeader");
			payload.put("tableRow", "emxFramework.ProgramObject.eServicecommonInitiateRoute.TableData");
			String[] tableKeys = {"TaskType", "RouteName", "TaskName", "DueDate", "Instructions", "Content", "ContentDescription"};
			String sInboxTaskLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sInboxTaskName, sInboxTaskId);
			String sRouteLink = emxNotificationUtil_mxJPO.getObjectLinkHTML(context, sName, sRouteId);
			String[] tableValues = {strTranslatedRouteAction, sRouteLink, sInboxTaskLink, sScheduledCompletionDate, sRouteInstructions, sRouteContent, sRouteContentDescription};
			payload.put("tableRowKeys", tableKeys);
			payload.put("tableRowValues", tableValues);
			payload.put("treeMenu", sTreeMenu);
			payload.put("click", "emxFramework.ProgramObject.eServicecommonInitiateRoute.ClickMyTasks");
			String sBaseURL = emxMailUtil_mxJPO.getBaseURL(context, null);
			String[] clickKeys = {"url", "tenantinfo"};
			String[] clickValues = {sBaseURL, ""};
			if(UIUtil.isNotNullAndNotEmpty(tenantId)){
				clickValues[1] = "&tenant="+tenantId;
			}
			payload.put("clickKeys", clickKeys);
			payload.put("clickValues", clickValues);

			mailUtil.setTreeMenuName(context, sTreeMenu);
			String oldAgentName = mailUtil.getAgentName(context, new String[] {});
			mailUtil.setAgentName(context, new String[] { user });
			
			if("true".equalsIgnoreCase(mailEnabled)){
				emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,"both");
			}else {
				emxNotificationUtil_mxJPO.objectNotification(context, sInboxTaskId, "APPObjectRouteTaskAssignedEvent", payload,"iconmail");
			}
			mailUtil.setAgentName(context, new String[] { oldAgentName });
		}
		HashMap hmParam		= new HashMap();
		hmParam.put("id", sInboxTaskId);
		String[] args1 = (String[])JPO.packArgs(hmParam);
		StringList tolist =  (StringList) JPO.invoke(context, "emxInboxTaskNotificationBase", null, "getRouteTaskOwner", args1, StringList.class);
		
		notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, aliasRoleName);
		notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "usergroup");
		notifyDetails.put(RouteTaskNotification.SENDER_USER, user);
		notifyObj.sendNewTaskAssignment3DNotification(context, notifyDetails, tolist);
	}

	private static void sendMailNotificationIfTask(Context context, String user, String sPersonName, String sRPEType,
			String sRPEName,String sRPETITLE, String sRPERev, String sGroupOrRoleTitle, String sGRName, String sRPEOwner,
			String sInboxTaskId, String sInboxTaskType, String sInboxTaskName, String sInboxTaskRev,
			String sRouteObjects, String sRoleOrGroupToList, String sType, String sName, String sRev,
			String[] sTreeMenu, emxMailUtil_mxJPO mailUtil, String sRouteOwner,String sRouteOwnerFullName,String sRouteId,String sRouteContent,Map<String,String> notifyDetails, RouteTaskNotification notifyObj) throws Exception {
		String mailEnabled = EnoviaResourceBundle.getProperty(context,"emxFramework.Routes.EnableMails");
		if (sRouteObjects.equals("")) {

			// Construct String array to send mail
			// notification
			String[] mailArguments = new String[35];
			// IR-043921V6R2011 - Changes START
			//mailArguments[0] = sPersonName;
			mailArguments[0] = sRoleOrGroupToList;
			// IR-043921V6R2011 - Changes END
			mailArguments[1] = "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice3";
			mailArguments[2] = "2";
			mailArguments[3] = "Name";
			mailArguments[4] = sPersonName;
			mailArguments[5] = "GroupOrRole";
			mailArguments[6] = sGroupOrRoleTitle;
			mailArguments[7] = "emxFramework.ProgramObject.eServicecommonInitiateRoute.MessageNotice2";
			mailArguments[8] = "12";
			mailArguments[9] = "IBType";
			mailArguments[10] = sInboxTaskType;
			mailArguments[11] = "IBName";
			mailArguments[12] = sInboxTaskName;
			mailArguments[13] = "IBRev";
			mailArguments[14] = sInboxTaskRev;
			mailArguments[15] = "GRName";
			mailArguments[16] = sGRName;
			mailArguments[17] = "RPEType";
			mailArguments[18] = sRPEType;
			mailArguments[19] = "RPEName";
			mailArguments[20] = sRPEName;
			mailArguments[21] = "RPERev";
			mailArguments[22] = sRPERev;
			mailArguments[23] = "RPEOwner";
			mailArguments[24] = sRPEOwner;
			mailArguments[25] = "Type";
			mailArguments[26] = sType;
			mailArguments[27] = "Name";
			mailArguments[28] = sName;
			mailArguments[29] = "Rev";
			mailArguments[30] = sRev;
			mailArguments[31] = "ROwner";
			mailArguments[32] = sRouteOwnerFullName;
			mailArguments[33] = sInboxTaskId;
			mailArguments[34] = "";
			// To set Tree Menu
			mailUtil.setTreeMenuName(context, sTreeMenu);
			// Calling method to send Mail
			// Added for Bugs 328159 Begin
			String oldAgentName = mailUtil.getAgentName(
					context, new String[] {});
			// Added for Bugs 328159 End
			mailUtil.setAgentName(context,
					new String[] { sRouteOwner });
			mailUtil.sendNotificationToUser(context,
					mailArguments);
			StringList toList = new StringList();
			for(String toListPerson : sRoleOrGroupToList.split(",")){
				toList.add(toListPerson);
			}
			StringList objectIdList = new StringList();
			objectIdList.add(sInboxTaskId);
			String[] subjectKeys = {"Name","GroupOrRole"};
			String[] subjectValues = {sPersonName,sGroupOrRoleTitle};
			String[] messageKeys = {"IBType","IBName","IBRev","GRName","RPEType","RPEName","RPERev","RPEOwner","Type","Name","Rev","ROwner"};
			String[] messageValues = {sInboxTaskType,sInboxTaskName,sInboxTaskRev,sGRName,sRPEType,sRPEName,sRPERev,sRPEOwner,sType,sName,sRev,sRouteOwnerFullName};
			mailUtil.sendNotification(context,toList,null,null,"emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice3",
                    subjectKeys,
                    subjectValues,
                    "emxFramework.ProgramObject.eServicecommonInitiateRoute.MessageNotice2",
                    messageKeys,
                    messageValues,
                    objectIdList,
                    null,
                    "true".equalsIgnoreCase(mailEnabled));
			// Added for Bugs 328159 Begin
			mailUtil.setAgentName(context,
					new String[] { oldAgentName });
			// Added for Bugs 328159 End
		} else {

			String[] mailArguments = new String[37];
			// IR-043921V6R2011 - Changes START
			//mailArguments[0] = sPersonName;
			mailArguments[0] = sRoleOrGroupToList;
			// IR-043921V6R2011 - Changes END
			mailArguments[1] = "emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice3";
			mailArguments[2] = "2";
			mailArguments[3] = "Name";
			mailArguments[4] = sPersonName;
			mailArguments[5] = "GroupOrRole";
			mailArguments[6] = sGroupOrRoleTitle;
			mailArguments[7] = "emxFramework.ProgramObject.eServicecommonInitiateRoute.MessageNotice2";
			mailArguments[8] = "13";
			mailArguments[9] = "IBType";
			mailArguments[10] = sInboxTaskType;
			mailArguments[11] = "IBName";
			mailArguments[12] = sInboxTaskName;
			mailArguments[13] = "IBRev";
			mailArguments[14] = sInboxTaskRev;
			mailArguments[15] = "GRName";
			mailArguments[16] = sGRName;
			mailArguments[17] = "RPEType";
			mailArguments[18] = sRPEType;
			mailArguments[19] = "RPEName";
			mailArguments[20] = sRPEName;
			mailArguments[21] = "RPERev";
			mailArguments[22] = sRPERev;
			mailArguments[23] = "RPEOwner";
			mailArguments[24] = sRPEOwner;
			mailArguments[25] = "Type";
			mailArguments[26] = sType;
			mailArguments[27] = "Name";
			mailArguments[28] = sName;
			mailArguments[29] = "Rev";
			mailArguments[30] = sRev;
			mailArguments[31] = "ROwner";
			mailArguments[32] = sRouteOwnerFullName;
			mailArguments[33] = "RObjects";
			mailArguments[34] = sRouteObjects;
			mailArguments[35] = sInboxTaskId;
			mailArguments[36] = "";
			// till here
			// To set Tree Menu
			mailUtil.setTreeMenuName(context, sTreeMenu);
			// Calling method to send Mail
			// Added for Bugs 328159 Begin
			String oldAgentName = mailUtil.getAgentName(
					context, new String[] {});
			// Added for Bugs 328159 End
			mailUtil.setAgentName(context,
					new String[] { user });
			mailUtil.sendNotificationToUser(context,
					mailArguments);
			StringList toList = new StringList();
			for(String toListPerson : sRoleOrGroupToList.split(",")){
				toList.add(toListPerson);
			}
			StringList objectIdList = new StringList();
			objectIdList.add(sInboxTaskId);
			String[] subjectKeys = {"Name","GroupOrRole"};
			String[] subjectValues = {sPersonName,sGroupOrRoleTitle};
			String[] messageKeys = {"IBType","IBName","IBRev","GRName","RPEType","RPEName","RPERev","RPEOwner","Type","Name","Rev","ROwner","RObjects"};
			String[] messageValues = {sInboxTaskType,sInboxTaskName,sInboxTaskRev,sGRName,sRPEType,sRPEName,sRPERev,sRPEOwner,sType,sName,sRev,sRouteOwnerFullName,sRouteObjects};
			mailUtil.sendNotification(context,toList,null,null,"emxFramework.ProgramObject.eServicecommonInitiateRoute.SubjectNotice3",
                    subjectKeys,
                    subjectValues,
                    "emxFramework.ProgramObject.eServicecommonInitiateRoute.MessageNotice2",
                    messageKeys,
                    messageValues,
                    objectIdList,
                    null,
                    "true".equalsIgnoreCase(mailEnabled));
			// Added for Bugs 328159 Begin
			mailUtil.setAgentName(context,
					new String[] { oldAgentName });
			// Added for Bugs 328159 End
		}
		List<String> toList = new ArrayList<String>();
		for(String userName:sRoleOrGroupToList.split(",")) {
			toList.add(userName);
		}		
		notifyDetails.put(RouteTaskNotification.TASK_ASSIGNEE, sPersonName);
		notifyDetails.put(RouteTaskNotification.COMPLETED_TASK_NAME, sRPETITLE);
		notifyDetails.put(RouteTaskNotification.ASSIGNEE_TYPE, "usergroup");
		notifyObj.sendNewTaskAssignment3DNotificationUponTaskCompletion(context, notifyDetails, toList);
	}
    static protected boolean checkIfAllNotificationTasks(MapList routedNodeObjects, int atLevel)
    {
        boolean bAllNotificationTasks = false;
        for (int i = 0; i < routedNodeObjects.size(); i++) 
        {
            Map objectMap = (Map) routedNodeObjects.get(i);
            String sRouteSequence = (String) objectMap.get("attribute[" + sAttRouteSequence + "]");
            int routeSequence = Integer.parseInt(sRouteSequence);
            if (routeSequence != atLevel)
            {
                //we only need to check tasks at the given sequence level
                continue;
            }
            String routeAction = (String) objectMap.get("attribute[" + sAttRouteAction + "]");
            if (!"Notify Only".equals(routeAction))
            {
                bAllNotificationTasks = false;
                break;
            }
            else
            {
                bAllNotificationTasks = true;
            }
        }
        return bAllNotificationTasks;    
    }

    static protected boolean adjustSequenceLevel(Context context, MapList routedNodeObjects, int atLevel, int adjustment)
        throws Exception
    {
        boolean bNodesUpdated = false;
        for (int i = 0; i < routedNodeObjects.size(); i++) 
        {
            Map objectMap = (Map) routedNodeObjects.get(i);
            String sRouteSequence = (String) objectMap.get("attribute[" + sAttRouteSequence + "]");
            int routeSequence = Integer.parseInt(sRouteSequence);
            if (routeSequence == atLevel)
            {
				String relId = (String) objectMap.get(DomainConstants.SELECT_PHYSICAL_ID);
                MqlUtil.mqlCommand(context, "modify connection $1 $2 $3", relId,sAttRouteSequence, String.valueOf(atLevel + adjustment));
                objectMap.put("attribute[" + sAttRouteSequence + "]", String.valueOf(atLevel + adjustment));
                bNodesUpdated = true;
            }        
        }
        return bNodesUpdated;
    }
    
    /**
     * This method is executed to send notification in case of inactive user present in complete task while restarting.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @returns nothing
     * @throws Exception if the operation fails
     * @since AEF 10 minor 1
     */
	   private static void notifyForInactiveAssignee(Context context,String sOwner, String sType, String sName, String sRev, emxMailUtil_mxJPO mailUtil, String user, String sRoutePhysicalId, String sPersonNameInactive, String routeTitle) throws Exception{
		   String mailEnabled = EnoviaResourceBundle.getProperty(context,"emxFramework.Routes.EnableMails");    			
		   // Added for Bugs 328159 Begin
		   String oldAgentName = mailUtil.getAgentName(context, new String[] {});
		   // Added for Bugs 328159 End
		   mailUtil.setAgentName(context,new String[] { user });
		   StringList toList = new StringList();
		   toList.add(sOwner);
		   String[] messageKeys = {"RPerson","RType","RName","RRev"};
		   String[] messageValues = {sPersonNameInactive,sType,sName,sRev};
		   mailUtil.sendNotification(context, toList,null, null,"emxFramework.ProgramObject.eServicecommonRoute.SubjectInactivePersonInRestart",null,null, "emxFramework.ProgramObject.eServicecommonRoute.MessageInactivePersonInRestart",messageKeys, messageValues,null,null, "true".equalsIgnoreCase(mailEnabled));
		   // Added for Bugs 328159 Begin
		   mailUtil.setAgentName(context,new String[] { oldAgentName });
		   Map<String, String> details = new HashMap<>();
		   details.put(RouteTaskNotification.ROUTE_OWNER, sOwner);
		   details.put(RouteTaskNotification.ROUTE_ID, sRoutePhysicalId);
		   details.put(RouteTaskNotification.ROUTE_NAME, routeTitle);
		   details.put(RouteTaskNotification.SENDER_USER, user);
		   RouteTaskNotification notifyObj = RouteTaskNotification.getInstance(context, "");
		   notifyObj.sendInactivePerson3DNotificationForCompletedTask(context, details, sPersonNameInactive);		
	   }
}
