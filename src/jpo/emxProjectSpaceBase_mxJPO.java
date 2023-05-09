// emxProjectSpaceBase.java
//
// Copyright (c) 2002-2020 Dassault Systemes.
// All Rights Reserved
//

//
//Change History:
//Date       Change By  Release   Bug/Functionality        Details
//-----------------------------------------------------------------------------------------------------------------------------
//3-Apr-09   wqy        V6R2010   370792                   Added new MQL statements. Handled Error in Try Catch Block.
//
//

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import matrix.db.AccessConstants;
import matrix.db.AttributeType;
import matrix.db.BusinessObject;
import matrix.db.BusinessObjectWithSelect;
import matrix.db.BusinessObjectWithSelectItr;
import matrix.db.BusinessObjectWithSelectList;
import matrix.db.Context;
import matrix.db.Dimension;
import matrix.db.JPO;
import matrix.db.MQLCommand;
import matrix.db.SetList;
import matrix.db.Unit;
import matrix.db.UnitItr;
import matrix.db.UnitList;
import matrix.util.MatrixException;
import matrix.util.StringList;

import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.tskv2.ProjectSequence;
import com.dassault_systemes.enovia.dpm.ProjectService;
import com.dassault_systemes.enovia.dpm.notification.NotificationBase;
import com.matrixone.apps.common.ContentReplicateOptions;
import com.matrixone.apps.common.DependencyRelationship;
import com.matrixone.apps.common.MemberRelationship;
import com.matrixone.apps.common.Organization;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.common.SubtaskRelationship;
import com.matrixone.apps.common.TaskDateRollup;
import com.matrixone.apps.common.TaskHolder;
import com.matrixone.apps.common.WorkspaceVault;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainAccess;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.FrameworkStringResource;
import com.matrixone.apps.domain.Job;
import com.matrixone.apps.domain.util.CacheUtil;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.DateUtil;
import com.matrixone.apps.domain.util.DebugUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MailUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.PersonUtil;
import com.matrixone.apps.domain.util.PropertyUtil;
import com.matrixone.apps.domain.util.StringUtil;
import com.matrixone.apps.domain.util.XSSUtil;
import com.matrixone.apps.domain.util.eMatrixDateFormat;
import com.matrixone.apps.domain.util.i18nNow;
import com.matrixone.apps.domain.util.mxType;
import com.matrixone.apps.framework.ui.UIComponent;
import com.matrixone.apps.framework.ui.UIMenu;
import com.matrixone.apps.program.CheckList;
import com.matrixone.apps.program.Currency;
import com.matrixone.apps.program.FinancialItem;
import com.matrixone.apps.program.GateReport;
import com.matrixone.apps.program.NotificationUtil;
import com.matrixone.apps.program.Opportunity;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import com.matrixone.apps.program.ProjectSpace;
import com.matrixone.apps.program.ProjectTemplate;
import com.matrixone.apps.program.Question;
import com.matrixone.apps.program.QuestionRelationship;
import com.matrixone.apps.program.ResourcePlanTemplate;
import com.matrixone.apps.program.ResourceRequest;
import com.matrixone.apps.program.Risk;
import com.matrixone.apps.program.RiskManagement;
import com.matrixone.apps.program.RiskRPNRelationship;
import com.matrixone.apps.program.Task;
import com.matrixone.apps.program.mycalendar.MyCalendarUtil;
import com.matrixone.json.JSONObject;
import com.matrixone.json.JSONArray;

/**
 * The <code>emxProjectSpaceBase</code> class represents the Project Space JPO
 * functionality for the AEF type.
 *
 * @version AEF 9.5.1.1 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxProjectSpaceBase_mxJPO extends com.matrixone.apps.program.ProjectSpace
{
    /** Keeps track of confirmed accesses for the context. */
    protected ArrayList passedAccessTypes = new ArrayList();
    // Create an instant of emxUtil JPO
    protected emxProgramCentralUtil_mxJPO emxProgramCentralUtilClass = null;
    private Context context;
    /*Project Lag calendar */
    private final static String ATTRIBUTE_LAG_CALENDAR = PropertyUtil.getSchemaProperty("attribute_LagCalendar");
    private final static String SELECT_ATTRIBUTE_LAG_CALENDAR = "attribute[" + ATTRIBUTE_LAG_CALENDAR + "]";
    /** The project type relative to this object. */
    static protected final String SELECT_PROJECT_ID = "from[" +
        RELATIONSHIP_PROJECT_ACCESS_LIST + "].to." + SELECT_ID;
	static protected final String SELECT_TYPE_FROM_PAL = "from[" +
			RELATIONSHIP_PROJECT_ACCESS_LIST + "].to." + SELECT_TYPE;
    /** The project visibility attribute relative to this object. */
    static protected final String SELECT_PROJECT_VISIBILITY_FROM_PAL = "from[" +
        RELATIONSHIP_PROJECT_ACCESS_LIST + "].to." +getAttributeSelect(ATTRIBUTE_PROJECT_VISIBILITY);
    /** Select "ProjectVisibility". */
    public static final String SELECT_PROJECT_VISIBILITY =
            getAttributeSelect(ATTRIBUTE_PROJECT_VISIBILITY);
    private final static String SELECT_ATTRIBUTE_TASK_ESTIMATED_END_DATE = "attribute[" + DomainConstants.ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE  + "]";
    private static final String SELECT_DELIVERABLE = "from[" + RELATIONSHIP_CONTRIBUTES_TO + "].to.id";
    
    private static final String SELECT_QUE_TASK_TRANSFER ="from[" + RELATIONSHIP_QUESTION + "].attribute[" + QuestionRelationship.ATTRIBUTE_TASK_TRANSFER + "]";
   static protected final String SELECT_TASK_ASSIGNEE = "to["+DomainConstants.RELATIONSHIP_ASSIGNED_TASKS+"].from."+DomainConstants.SELECT_NAME;
	public static final String TYPE_OPPORTUNITY                  = "Opportunity";
	public static final String TYPE_RISK                  = "Risk";
	public static final String TYPE_ISSUE                  = "Issue";
//  public static final String  RELATIONSHIP_RESOLUTION_PROJECT   = "Resolution Project";  //from
    public static final String  RELATIONSHIP_RESOLUTION_PROJECT   = "Contributes To";      //from
	public static final String RELATIONSHIP_RSOLVED_TO   = "Resolved To";


	java.text.SimpleDateFormat sdf =
			new java.text.SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(),
					Locale.US);

    /**
     * Constructs a new emxProjectSpace JPO object.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        0 - String containing the id
     * @throws Exception if the operation fails
     * @since AEF 9.5.1.1
     */
    public emxProjectSpaceBase_mxJPO (Context context, String[] args)
        throws Exception
    {
        // Call the super constructor
        super();
        this.context = context;
        if ((args != null) && (args.length > 0))
        {
            setId(args[0]);
        }
    }

    /**
     * Constructs a new emxProjectSpace JPO object.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param id the business object id
     * @throws Exception if the operation fails
     * @since AEF 9.5.1.3
     */
    public emxProjectSpaceBase_mxJPO (String id) throws Exception
    {
        // Call the super constructor
        super(id);
    }

    /**
     * This method get the access list object for this Project.
     *
     * @param context the eMatrix <code>Context</code> object
     * @return DomainObject access list object
     * @throws Exception if the operation fails
     * @since AEF 9.5.1.3
     */
    protected DomainObject getAccessListObject(Context context)
      throws Exception
    {
        return super.getAccessListObject(context);
    }

    /**
     * This function verifies the user's permission for the given project.
     * This check is made by verifying the user's company matches the
     * program's company.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *      PROJECT_MEMBER to see if the context user is a project member for
     *                  this project, <BR>
     *      PROJECT_ASSESSOR to see if the context user is a project assessor for
     *                  this project
     *      FINANCIAL_REVIEWER to see if the context user is a financial reviewer for
     *                  this project
     * @return boolean true or false
     * @throws Exception if the operation fails
     * @since AEF 9.5.1.0
     */
    public boolean hasAccess(Context context, String[] args)
      throws Exception
    {
        DebugUtil.debug("Start Project - " + new Date().getTime());

        //program[emxProjectSpace PROJECT_LEAD|PROJECT_MEMBER -method hasAccess
        //            -construct ${OBJECTID}] == true
        String accessType = args[0];
        boolean access = false;

        String ignoreAccess = MqlUtil.mqlCommand(context, "get env $1 $2","global","IGNORE_ACCESS");
        if ("TRUE".equals(ignoreAccess))
        {
            access = true;
        }
        else
        {
            // Check if access has already been checked.
            if (passedAccessTypes.indexOf(accessType) != -1)
            {
                access = true;
            }
        }

        if (access == false)
        {
            DomainObject accessListObject = getAccessListObject(context);

            if (accessListObject != null)
            {
                int iAccess;
                if ("PROJECT_MEMBER".equals(accessType))
                {
                    iAccess = AccessConstants.cExecute;
                }
                else if ("PROJECT_ASSESSOR".equals(accessType))
                {
                    iAccess = AccessConstants.cViewForm;
                }
                else if ("FINANCIAL_REVIEWER".equals(accessType))
                {
                    iAccess = AccessConstants.cModifyForm;
                }
                else
                {
                    iAccess = AccessConstants.cModify;
                }
                DebugUtil.debug("Checking access list... " +
                    new Date().getTime());
                if (accessListObject.checkAccess(context, (short) iAccess))
                {
                    passedAccessTypes.add(accessType);
                    access = true;
                }
            }
        }

        DebugUtil.debug(context.getUser() + " : " + new Date().getTime());
        DebugUtil.debug("End Project - ", getId(), " : " + access);

        return access;
    }

    /**
     * This method gets project permission for this user;
     * value returned should be: None, Project Member, Project Assessor,
     * Financial Reviewer, Project Lead, Project Owner.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @return String the user access permissions for this project.
     * @throws FrameworkException if operation fails.
     * @since AEF 9.5.1.3
     */
    public String getAccess(Context context, String[] args)
      throws Exception
    {
        String access = EMPTY_STRING;
        DomainObject accessListObject = getAccessListObject(context);

        if (accessListObject != null)
        {
            if (accessListObject.checkAccess(context,
                      (short) AccessConstants.cOverride))
            {
                access = ProgramCentralConstants.PROJECT_ROLE_PROJECT_LEAD;
            }
            else if (accessListObject.checkAccess(context,
                      (short) AccessConstants.cModify))
            {
                access = ProgramCentralConstants.PROJECT_ROLE_PROJECT_LEAD;
            }
            else if (accessListObject.checkAccess(context,
                      (short) AccessConstants.cViewForm))
            {
                access = ProgramCentralConstants.PROJECT_ROLE_PROJECT_ASSESSOR;
            }
            else if (accessListObject.checkAccess(context,
                      (short) AccessConstants.cModifyForm))
            {
                access = ProgramCentralConstants.PROJECT_ROLE_FINANCIAL_REVIEWER;
            }
            else if (accessListObject.checkAccess(context,
                      (short) AccessConstants.cExecute))
            {
                access = ProgramCentralConstants.PROJECT_ROLE_PROJECT_MEMBER;
            }
            else
            {
                access = "None";
            }
        }
        DebugUtil.debug("Access is " + access);
        return access;
    }

    /**
     * Notify tasks that are near due or late to task assignees/owner
     * based upon the Alert settings in the Project.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws FrameworkException if operation fails
     * @since AEF 9.5.4.1
     * @deprecated R210, use program emxProgramCentralNotificationUtil method notifyTaskAssignees -LateTask //13-Sep-2010:S2E
     */
    public void performTaskEscalation(Context context, String[] args)
      throws Exception
    {/*
        try
        {
            // Define selectables for each ProjectSpace object.
            StringList projectSelects = new StringList(4);
            projectSelects.add(SELECT_ID);
            projectSelects.add(SELECT_ESCALATION_DATE);
            projectSelects.add(SELECT_RECURRENCE_INTERVAL);
            projectSelects.add(SELECT_SEND_REMINDER);

            // Define selectables for each Task object.
            StringList taskSelects = new StringList(7);
            taskSelects.add(SELECT_ID);
            taskSelects.add(SELECT_NAME);
            taskSelects.add(SELECT_OWNER);
            taskSelects.add(SELECT_TASK_ESTIMATED_DURATION);
            taskSelects.add(SELECT_TASK_ESTIMATED_START_DATE);
            taskSelects.add(SELECT_TASK_ESTIMATED_FINISH_DATE);
            taskSelects.add(SELECT_TASK_ACTUAL_FINISH_DATE);

            // Save current time to compare against below.
            Date todayDate = new Date();
            long currentTime = todayDate.getTime();

            //use MatrixDateFormat's pattern
            SimpleDateFormat mxDateFrmt =
                new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(),
                    Locale.US);
            String dateStr = mxDateFrmt.format(todayDate);
            String noNotificationValue = EnoviaResourceBundle.getProperty(context,"emxProgramCentral.ProjectDoNotSendReminder");
            //whereClause for all ProjectSpace objects that are not complete
            //and do not have "Send Reminder" set to "Do not send reminder"
            String whereClause =
                "(attribute[" + ATTRIBUTE_TASK_ACTUAL_FINISH_DATE +
                "]  ==  null) && (attribute[" + ATTRIBUTE_SEND_REMINDER +
                "] != '" + noNotificationValue + "')";

            DebugUtil.debug("Where clause for ProjectSpace project: ",
                whereClause);

            StringList assigneeSels = new StringList(1);
            assigneeSels.add(SELECT_NAME);

            // Find all ProjectSpace items in the database.
            MapList projectspaceObjsList =
                findObjects(context, TYPE_PROJECT_SPACE, null,
                    whereClause, projectSelects);

            DebugUtil.debug("ProjectSpace count:  " +
                projectspaceObjsList.size());

            Iterator itr = projectspaceObjsList.iterator();
            while (itr.hasNext())
            {
                Map projectMap = (Map) itr.next();

                // Get ProjectSpace business object id
                String projectId = (String) projectMap.get(SELECT_ID);

                // define object for general querying and updating
                ProjectSpace object =
                    (ProjectSpace) DomainObject.newInstance(context,
                        DomainConstants.TYPE_PROJECT_SPACE,
                        DomainConstants.PROGRAM);

                //set objectId to this ProjectSpace object
                object.setId(projectId);

                String escalationDateStr =
                    (String) projectMap.get(SELECT_ESCALATION_DATE);
                String recurIntervalStr =
                    (String) projectMap.get(SELECT_RECURRENCE_INTERVAL);
                String sendReminderStr =
                    (String) projectMap.get(SELECT_SEND_REMINDER);

                //per requirements and system's alert settings,  the system
                //should go through this check once a day.
                //determine whether the performTaskEscalation method already got
                //called today. If so, no need to perform it again.
                long escalationTime = 0;
                if ((escalationDateStr != null) &&
                      !"".equals(escalationDateStr))
                {
                    Date escalationDate =
                        eMatrixDateFormat.getJavaDate(escalationDateStr);
                    escalationTime = escalationDate.getTime();
                    if (((currentTime - escalationTime) / 86400000) == 0)
                    {
                        //DebugUtil.debug(
                        //"This performTaskEscalation method already got called today!!!");
                        continue;
                    }
                }

                //get all tasks for this project
                MapList tasksForProject =
                    object.getTasks(context, 0, taskSelects, null, false);

                Iterator itr2 = tasksForProject.iterator();
                while (itr2.hasNext())
                {
                    Map taskMap = (Map) itr2.next();

                    String daysDiff = sendReminderStr;
                    String actualFinishDateStr =
                        (String) taskMap.get(SELECT_TASK_ACTUAL_FINISH_DATE);

                    //Do not go further if the task is complete
                    if ((actualFinishDateStr != null) &&
                          !"".equals(actualFinishDateStr))
                    {
                        //DebugUtil.debug("DO NOT NEED TO SEND NOTIFICATION - TASK IS COMPLETE!!!");
                        continue;
                    }

                    //compute the time to notify
                    Date dueDate =
                        eMatrixDateFormat.getJavaDate((String) taskMap.get(
                                SELECT_TASK_ESTIMATED_FINISH_DATE));
                    long notifyTime = dueDate.getTime();
                    notifyTime += (Integer.parseInt(daysDiff) * 86400000);

                    long timeDiff = currentTime - notifyTime;

                    // Time falls into the warning/late period
                    if (timeDiff >= 0)
                    {
                        long days = timeDiff / 86400000;// convert timeDiff into days

                        // check to see a reminder needs to be sent again
                        if ((days > 0) && ("0".equals(recurIntervalStr)) &&
                              (escalationDateStr != null) &&
                              (!"".equals(escalationDateStr)) &&
                              (escalationTime > notifyTime))
                        {
                            //DebugUtil.debug("Do not need to send notification again!");
                            continue;
                        }
                        else
                        {
                            long modValue = 0;

                            // find days after notifying date
                            if (!"0".equals(recurIntervalStr))
                            {
                                modValue =
                                    days % Long.parseLong(recurIntervalStr);
                            }

                            //no notification sent if days after notifying date
                            //is not divisible by Recurnence Interval value
                            if (modValue != 0)
                            {
                                //DebugUtil.debug("task :  " + (String)taskMap.get(SELECT_NAME) +
                                //                " is not in the notifying period again.");
                                continue;
                            }
                        }
                    }
                    else
                    {
                        //DebugUtil.debug("task: " + (String)taskMap.get(SELECT_NAME) +
                        //                " is not in the notifying period.");
                        continue;
                    }

                    Task taskObj =
                        (Task) DomainObject.newInstance(context,
                            DomainConstants.TYPE_TASK, DomainConstants.PROGRAM);

                    //set id to the task object
                    taskObj.setId((String) taskMap.get(SELECT_ID));

                    StringList mailToList = new StringList();
                    MapList assigneesList =
                        taskObj.getAssignees(context, assigneeSels, null, "");
                    int assigneesCount = assigneesList.size();

                    //build the mailling list of task assginees
                    for (int i = 0; i < assigneesCount; i++)
                    {
                        mailToList.add((String) ((Map) assigneesList.get(i)).get(
                                SELECT_NAME));
                    }

                    //add the task owner to the mailing list if it's not already in there
                    if (!mailToList.contains(
                              (String) taskMap.get(SELECT_OWNER)))
                        mailToList.add((String) taskMap.get(SELECT_OWNER));

                    //save taskname for mailling purpose
                    String objectName = (String) taskMap.get(SELECT_NAME);
                    String subjectProperty = ProgramCentralConstants.EMPTY_STRING;
                    String messageProperty = ProgramCentralConstants.EMPTY_STRING;

                    if (Integer.parseInt(daysDiff) > 0)
                    {
                        // task is passed due
                        subjectProperty =
                            FrameworkStringResource.ProjectSpace_LateEscalationSubject;
                        messageProperty =
                            FrameworkStringResource.ProjectSpace_LateEscalationMessage;
                    }
                    else
                    {
                        // task is not passed due
                        subjectProperty =
                            FrameworkStringResource.ProjectSpace_EscalationSubject;
                        messageProperty =
                            FrameworkStringResource.ProjectSpace_EscalationMessage;
                    }

                    //Task object to refer to in the notification
                    StringList objectIdList = new StringList(1);
                    objectIdList.addElement((String) taskMap.get(SELECT_ID));

                    // Sends mail notification to the taskAssignees/owner.
                    String[] subjectKeys = { "WBSTaskName" };
                    String[] subjectValues = { objectName };
                    String[] messageKeys =
                    {
                        "WBSTaskName", "WBSTaskEstStartDate",
                        "WBSTaskEstFinishDate", "WBSTaskEstDuration"
                    };
                    String[] messageValues =
                    {
                        objectName,
                        (String) taskMap.get(SELECT_TASK_ESTIMATED_START_DATE),
                        (String) taskMap.get(SELECT_TASK_ESTIMATED_FINISH_DATE),
                        (String) taskMap.get(SELECT_TASK_ESTIMATED_DURATION)
                    };

                    try
                    {
                        MailUtil.sendNotification(context, mailToList, null,
                            null, subjectProperty, subjectKeys, subjectValues,
                            messageProperty, messageKeys, messageValues,
                            objectIdList, null);
                    }
                    catch (Exception er)
                    {
                        //there might be the error of invalid user(s) in the mailToList
                        //continuing on checking the remaining task(s)
                    }
                }

                //end task while
                boolean isContextPushed = false;

                //Need to be a super user to set the Escalation Date.
                try
                {
                    ContextUtil.pushContext(context);
                    isContextPushed = true;

                    //set the escalation date on the project
                    object.setAttributeValue(context,
                        ATTRIBUTE_ESCALATION_DATE, dateStr);
                }
                catch (Exception ex)
                {
                    throw (new FrameworkException(ex));
                }
                finally
                {
                    if (isContextPushed)
                    {
                        ContextUtil.popContext(context);
                    }
                }
            }

            //end project while
        }
        catch (Exception e)
        {
            throw new FrameworkException(e);
        }*/
    }

    /**
     * get folder list for the Project Space - to be used for Structured tree.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        0 - HashMap containing the parameters - Object id
     * @return Maplist of Workspace Folder names
     * @throws Exception if the operation fails
     * @since AEF Rossini
     */
    public static MapList getProjectSpaceFolderList(Context context, String[] args) throws Exception
    {
        Map programMap = (Map) JPO.unpackArgs(args);
        Map paramMap = (Map)programMap.get("paramMap");
        String strObjectId = (String) paramMap.get("objectId");

        StringList slBusSelect = new StringList(DomainConstants.SELECT_ID);
        slBusSelect.add(DomainConstants.SELECT_TYPE);
        slBusSelect.add(DomainConstants.SELECT_NAME);

        emxProjectFolder_mxJPO projectFolder = new emxProjectFolder_mxJPO(context, null);
        MapList mlFolders = projectFolder.getProjectFolder(context, strObjectId, slBusSelect, null);

        //
        // Adjust the tree menus to be shown
        //
        final String TREE_MENU_WORKSPACE_VAULT = EnoviaResourceBundle.getProperty(context, "eServiceSuiteProgramCentral.emxTreeAlternateMenuName.type_ProjectVault");
        final String TREE_MENU_CONTROLLED_FOLDER = EnoviaResourceBundle.getProperty(context, "eServiceSuiteProgramCentral.emxTreeAlternateMenuName.type_ControlledFolder");

        //
        // To keep this code extensible, we shall be using the same tree menu as that of Controlled Folder for its children as well
        // For this purpose we shall find Controlled Folder type hierarchy and check if this folder belongs any of these types before
        // it is checked against Workspace Vault.
        // To get all the derived type recursively, we shall use MQL. BusinessType API only gives immediately derived type.
        //
        //String strMQL = "print type \"" + TYPE_CONTROLLED_FOLDER +"\" select derivative dump |";
        String strMQL = "print type $1 select $2 dump $3";
        String strResult = MqlUtil.mqlCommand(context, strMQL,TYPE_CONTROLLED_FOLDER,"derivative","|");
        StringList slControlledFolderTypeHierarchy = FrameworkUtil.split(strResult, "|");

        // Dont forget to add Controlled Folder type itself into this listing
        slControlledFolderTypeHierarchy.add(TYPE_CONTROLLED_FOLDER);

        Map mapFolder = null;
        String strFolderType = "";

        for (Iterator itrFolders = mlFolders.iterator(); itrFolders.hasNext();) {
            mapFolder = (Map) itrFolders.next();

            strFolderType = (String)mapFolder.get(DomainConstants.SELECT_TYPE);

            if (slControlledFolderTypeHierarchy.contains(strFolderType)) {
                mapFolder.put("treeMenu", TREE_MENU_CONTROLLED_FOLDER);
            }
            else {
                mapFolder.put("treeMenu", TREE_MENU_WORKSPACE_VAULT);
            }
        }

        return mlFolders;
        // End:R207:PRG Controlled Folder
    }

    /****************************************************************************************************
     *       Methods for Config Table Conversion Task
     ****************************************************************************************************/
    /**
     * gets the list of All Projects owned by the user
     * Used for PMCProjectSpaceMyDesk table
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @return MapList containing the ids of Project objects
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getAllProjects(Context context, String[] args)
      throws Exception
    {
        return getProjectSummary(context, args, null);
    }

    /**
     * gets the list of active Projects owned by the user
     * Used for PMCProjectSpaceMyDesk table
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @return MapList containing the ids of Project objects
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getActiveProjects(Context context, String[] args)
      throws Exception
    {
        return getProjectSummary(context, args, STATE_PROJECT_ARCHIVE);
    }


    /**
     * gets the list of complete Projects owned by the user
     * Used for PMCProjectSpaceMyDesk table
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @return MapList containing the ids of Project objects
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getCompletedProjects(Context context, String[] args)
      throws Exception
    {
        return getProjectSummary(context, args, STATE_PROJECT_COMPLETE);
    }

    //Added:nr2:11-05-2010:PRG:R210:For Project Hold Cancel Highlight
    /**
     * gets the list of Hold Projects owned by the user
     * Used for PMCProjectSpaceMyDesk table
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @return MapList containing the ids of Project objects
     * @throws Exception if the operation fails
     * @since PMC R210
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getHoldProjects(Context context, String[] args)
      throws Exception
    {
        return getProjectSummary(context, args, ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD);
    }

    /**
     * gets the list of Cancel Projects owned by the user
     * Used for PMCProjectSpaceMyDesk table
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @return MapList containing the ids of Project objects
     * @throws Exception if the operation fails
     * @since PMC R210
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getCancelProjects(Context context, String[] args)
      throws Exception
    {
        return getProjectSummary(context, args, ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL);
    }
  //End:nr2:11-05-2010:PRG:R210:For Project Hold Cancel Highlight

    /**
     * This method is used to gets the list of projects owned by the user.
     * Used for PMCProjectSpaceMyDesk table
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @param busWhere optional business object where clause
     * @return MapList containing the id of projects owned by the user.
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    public MapList getProjectSummary(Context context, String[] args,
        String selectState) throws Exception
    {
        // Check license while listing Project Concepts, Project Space, if license check fails here
        // the projects will not be listed. This is mainly done to avoid Project Concepts from being listed
        // but as this is the common method, the project space objects will also not be listed.
        //
        ComponentsUtil.checkLicenseReserved(context,ProgramCentralConstants.PGE_LICENSE_ARRAY);


        MapList projectList = null;
        com.matrixone.apps.program.Program program =
            (com.matrixone.apps.program.Program) DomainObject.newInstance(context,
                DomainConstants.TYPE_PROGRAM, DomainConstants.PROGRAM);
        com.matrixone.apps.common.Person person =
            com.matrixone.apps.common.Person.getPerson(context);
        com.matrixone.apps.program.ProjectSpace project =
            (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,
                DomainConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);
        try
        {
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            String objectId = (String) programMap.get("objectId");

            // Retrieve the person's project's info
            String busWhere = ProgramCentralConstants.EMPTY_STRING;
            String busId = ProgramCentralConstants.EMPTY_STRING;
            String relWhere = ProgramCentralConstants.EMPTY_STRING;

            String vaultPattern = "";

            String vaultOption = PersonUtil.getSearchDefaultSelection(context);

            vaultPattern = PersonUtil.getSearchVaults(context, false ,vaultOption);

            //use the matchlist keyword to filter by vaults, need this if option is not "All Vaults"
            if (!vaultOption.equals(PersonUtil.SEARCH_ALL_VAULTS) && vaultPattern.length() > 0)
            {
            busWhere = "vault matchlist '" + vaultPattern + "' ','";
            }

            if ((STATE_PROJECT_COMPLETE).equals(selectState))
            {
                if ((busWhere == null) || "".equals(busWhere))
                {
                    busWhere = "current=='" + STATE_PROJECT_COMPLETE + "'";
                }
                else
                {
                    busWhere += (" && current=='" + STATE_PROJECT_COMPLETE + "'");
                }
            }
            else if ((STATE_PROJECT_ARCHIVE).equals(selectState))
            {
                // Active Projects - not in the complete state or in the archive state
                if ((busWhere == null) || "".equals(busWhere))
                {
                    busWhere =
                        "current!=" + STATE_PROJECT_COMPLETE + " && current!=" + STATE_PROJECT_ARCHIVE +
                        " && current!=" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD + " && current!=" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL;
                }
                else
                {
                    busWhere += (" && current!=" + STATE_PROJECT_COMPLETE + " && current!=" + STATE_PROJECT_ARCHIVE
                             + " && current!=" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD + " && current!=" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL);
                }
            }
          //Added:nr2:11-05-2010:PRG:R210:For Project Hold Cancel Highlight
            else if((ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD).equals(selectState)){
                busWhere =
                    "current==" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD;
            }
            else if((ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL).equals(selectState)){
                busWhere =
                    "current==" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL;
                relWhere = MemberRelationship.SELECT_PROJECT_ACCESS+" == '"+ProgramCentralConstants.PROJECT_ROLE_PROJECT_OWNER+"'";
                relWhere += " ||"+MemberRelationship.SELECT_PROJECT_ACCESS+" == '"+ProgramCentralConstants.PROJECT_ACCESS_PROJECT_LEAD+"'";
              }
          //End:nr2:11-05-2010:PRG:R210:For Project Hold Cancel Highlight
            else
            {
                //do nothing
                //Added:nr2:PRG:R210:29-05-2010:For Project Hold and Cancel Highlight
                if ((busWhere == null) || "".equals(busWhere))
                {
                    //busWhere = "current!='" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL + "'";
                    //busWhere += " && current !='" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD + "'";
                }
                else
                {
                    busWhere += " current!='" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL + "'";
                    busWhere += " && current !='" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD + "'";
                }
                //End:nr2:PRG:R210:29-05-2010:For Project Hold and Cancel Highlight

            }
            if (ProgramCentralUtil.isNullString(busWhere)) {
            	busWhere +=" type!=" + ProgramCentralConstants.TYPE_EXPERIMENT+"&&type!='"+ProgramCentralConstants.TYPE_PROJECT_BASELINE+"'"; 
            } else {
            	busWhere +=" && type!=" + ProgramCentralConstants.TYPE_EXPERIMENT+"&&type!='"+ProgramCentralConstants.TYPE_PROJECT_BASELINE+"'";
            }

            StringList busSelects = new StringList(2);
            busSelects.add(project.SELECT_ID);
            busSelects.add(SELECT_ATTRIBUTE_TASK_ESTIMATED_END_DATE);
            busSelects.add(project.SELECT_VAULT);
            busSelects.add(SELECT_NAME);
            if ((objectId != null) && !"".equals(objectId))
            {
                // if busId is passed then page is called from program page
                program.setId(objectId);
                projectList =
                    program.getProjects(context, busSelects, busWhere);
            }
            //ends if
            else
            {
                //busSelects.add(project.SELECT_PROGRAM_NAME);
                projectList =
                    project.getUserProjects(context, person, busSelects, null,
                        busWhere, relWhere);
            }
            //ends else
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            return projectList;
        }
    }


    /**
     * Returns list of Projects excluding the current project.
     * @param context
     * @param args
     * @return
     * @throws MatrixException
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getExternalProjects(Context context, String[] args)throws MatrixException
    {
        try{
            HashMap arguMap = (HashMap)JPO.unpackArgs(args);
            String strProjectId = (String) arguMap.get("projectId");
            MapList projectMapList = new MapList();

            projectMapList = getAllProjects(context, args);

            Iterator itr = projectMapList.iterator();
            while(itr.hasNext())
            {
                Map mpExcludeProject=(Map) itr.next();
                String id =  (String)mpExcludeProject.get(DomainConstants.SELECT_ID);
                String strType =  (String)mpExcludeProject.get(DomainConstants.SELECT_TYPE);

                if(strProjectId.equals(id)){
                    projectMapList.remove(mpExcludeProject);
                    itr = projectMapList.iterator();
                }

                if(DomainConstants.TYPE_PROJECT_CONCEPT.equals(strType)){
                    projectMapList.remove(mpExcludeProject);
                    itr = projectMapList.iterator();
                }
            }
            return projectMapList;
        }catch(Exception e){
            throw new MatrixException(e);
        }
    }

    /**
     * showOwner - displays the owner with lastname,firstname format
     *             also has a link to open a pop up with the owner
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        0 - MapList containing the object Maps
     *        1 - MapList containing the parameters
     * @return Vector containing the owner value as String
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    public StringList showOwner(Context context, String[] args)
      throws Exception
      {
    	StringList owner = new StringList();
    	try
    	{
    		com.matrixone.apps.program.ProjectSpace project =
    				(com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,
    						DomainConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);
    		HashMap projectMap = (HashMap) JPO.unpackArgs(args);
    		MapList objectList = (MapList) projectMap.get("objectList");
    		Map paramList = (Map) projectMap.get("paramList");

    		Map objectMap = null;

    		Iterator objectListIterator = objectList.iterator();
    		String[] objIdArr = new String[objectList.size()];
    		int arrayCount = 0;
    		while (objectListIterator.hasNext())
    		{
    			objectMap = (Map) objectListIterator.next();
    			objIdArr[arrayCount] =
    					(String) objectMap.get(project.SELECT_ID);
    			arrayCount++;
    		}

    		MapList actionList =
    				DomainObject.getInfo(context, objIdArr,
    						new StringList(project.SELECT_OWNER));

    		Iterator actionsListIterator = actionList.iterator();

    		while (actionsListIterator.hasNext())
    		{
    			objectMap = (Map) actionsListIterator.next();
    			String owners = (String) objectMap.get(project.SELECT_OWNER);
    			//String strOwner = com.matrixone.apps.common.Person.getDisplayName(context, owners);
    			owner.add(owners);
    		} //ends while
    	}
    	catch (Exception ex)
    	{
    		throw ex;
    	}
    	finally
    	{
    		return owner;
    	}
      }

    /**
     * This function displays the list of programs
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        0 - MapList containing the object Maps
     *        1 - MapList containing the parameters
     * @return Vector containing the program value as String
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    public Vector getProgram(Context context, String[] args)
      throws Exception
    {
        Vector programs = new Vector();
        try {
        	com.matrixone.apps.program.ProjectSpace project =
                    (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,
                        DomainConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);
        	
            HashMap projectMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) projectMap.get("objectList");
            Map paramList = (Map) projectMap.get("paramList");
            boolean isprinterFriendly=false;
            if(paramList.get("reportFormat") != null)
            {
                isprinterFriendly = true;
            }
            
            Map objectMap = null;

            Iterator objectListIterator = objectList.iterator();
            String[] objIdArr = new String[objectList.size()];
            
            String strOutput = "";
            String strProgramId = "";
            String strProgramName= "";
            
            //Modified to get multiple programs connected to Project Space/Project Concept.
            while (objectListIterator.hasNext())
            {
                objectMap = (Map) objectListIterator.next();
                String strProjectId =(String) objectMap.get(project.SELECT_ID);
                DomainObject projectobj = DomainObject.newInstance(context,strProjectId);
                StringList programIds = projectobj.getInfoList(context,"to["+RELATIONSHIP_PROGRAM_PROJECT+"].from.id");
                if ((programIds == null) || programIds.isEmpty())
                {
                    programs.add(EMPTY_STRING);
                }
                else{
                    StringBuffer output = new StringBuffer();
                    for(int nCount=0;nCount<programIds.size();nCount++)
                    {
                        strProgramId =XSSUtil.encodeForURL(context, (String)programIds.get(nCount));
                        DomainObject programDobj = DomainObject.newInstance(context,strProgramId);
                        strProgramName = programDobj.getInfo(context, SELECT_NAME);
                        if(!isprinterFriendly)
                        {
                        	String url = "../common/emxTree.jsp?objectId="+XSSUtil.encodeForURL(context,strProgramId)+"&amp;relId=null&amp;suiteKey=ProgramCentral";
                        	output.append("<a href='").append(url).append("'>");
                        }
                        output.append("<img src='../common/images/iconSmallProgram.gif' border='0' valign='absmiddle'/>");
                        //Added for special character.
                        output.append(XSSUtil.encodeForXML(context,strProgramName));
                        if(!isprinterFriendly)
                        output.append("</a>,");
                    }
                    strOutput=output.toString();
                    programs.add(strOutput.substring(0, strOutput.length()-1));

                }
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
            throw ex;
        } finally {
            return programs;
        }
    }

    /**
     * This method is used to show the status image.
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        0 - objectList MapList
     * @return Vector containing all the status image value as String.
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    public Vector getStatusIcon(Context context, String[] args)
      throws Exception
    {
        Vector showIcon = new Vector();
        String languageString = context.getSession().getLanguage();
        try
        {
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) programMap.get("objectList");
           
            Map objectMap = null;
            int i = 0;
            Iterator objectListIterator = objectList.iterator();
            String[] objIdArr = new String[objectList.size()];
            while (objectListIterator.hasNext())
            {
                objectMap = (Map) objectListIterator.next();
                objIdArr[i] = (String) objectMap.get(DomainObject.SELECT_ID);
                i++;
            }
String stooltip=ProgramCentralConstants.EMPTY_STRING;
            StringList busSelect = new StringList(6);
            busSelect.add(SELECT_BASELINE_CURRENT_END_DATE);
            busSelect.add(SELECT_TASK_ESTIMATED_FINISH_DATE);
            busSelect.add(SELECT_TASK_ACTUAL_FINISH_DATE);
            busSelect.add(SELECT_CURRENT);
            busSelect.add(SELECT_TYPE);

            MapList actionList =
                DomainObject.getInfo(context, objIdArr, busSelect);

            int actionListSize = 0;
            if (actionList != null)
            {
                actionListSize = actionList.size();
            }

            //set the yellow red threshold from the properties file
            int yellowRedThreshold =
                Integer.parseInt(EnoviaResourceBundle.getProperty(context,"eServiceApplicationProgramCentral.SlipThresholdYellowRed"));
            Date sysDate =new Date();
            String sStatusOnTime = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Common.OnTime",languageString);
            String sStatusLate = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Common.Late",languageString);
            String sStatusBehindSchedule = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Common.Legend.BehindSchedule",languageString);
            for (i = 0; i < actionListSize; i++)
            {
                //determine which gif should be display for status
                String statusGif = "";
                Date baselineCurrentEndDate = null;
                objectMap = (Map) actionList.get(i);

                String baselineCurrentEndDateString =
                    (String) objectMap.get(SELECT_BASELINE_CURRENT_END_DATE);

                Date estFinishDate =
                    sdf.parse((String) objectMap.get(
                            SELECT_TASK_ESTIMATED_FINISH_DATE));

                if (!"".equals(baselineCurrentEndDateString))
                {
                    baselineCurrentEndDate =
                        sdf.parse((String) objectMap.get(
                                SELECT_BASELINE_CURRENT_END_DATE));
                }
                long daysRemaining;
                if (null == baselineCurrentEndDate)
                {
                    daysRemaining =
                        (long) DateUtil.computeDuration(sysDate, estFinishDate);
                }
                else
                {
                    daysRemaining =
                        (long) DateUtil.computeDuration(sysDate,
                            baselineCurrentEndDate);
                }

                // determine which status gif to display
                if (null == baselineCurrentEndDate)
                {
                    if (objectMap.get(SELECT_TYPE).equals(TYPE_PROJECT_CONCEPT))
                    {
                        statusGif = ProgramCentralConstants.EMPTY_STRING;
                    }
                    else
                    {
                        if (objectMap.get(SELECT_CURRENT).equals(STATE_PROJECT_COMPLETE))
                        {
                            statusGif =
                                "<img src=\"images/iconStatusGreen.gif\" border=\"0\" alt=\"";
                            statusGif += (sStatusOnTime + "\"/>");
    						stooltip=sStatusOnTime;
                        }
                        else if (!objectMap.get(SELECT_CURRENT).equals(STATE_PROJECT_COMPLETE) &&
                              sysDate.after(estFinishDate))
                        {
                            statusGif =
                                "<img src=\"images/iconStatusRed.gif\" border=\"0\" alt=\"";
                            statusGif += (sStatusLate + "\"/>");
    						stooltip=sStatusLate;
                        }
                        else if (!objectMap.get(SELECT_CURRENT).equals(STATE_PROJECT_COMPLETE) &&
                              (daysRemaining <= yellowRedThreshold))
                        {
                            statusGif =
                                "<img src=\"images/iconStatusYellow.gif\" border=\"0\" alt=\"";
                            statusGif += (sStatusBehindSchedule + "\"/>");
    						stooltip=sStatusBehindSchedule;
                        }
                        else{
                            statusGif = ProgramCentralConstants.EMPTY_STRING;
                        }
                    }
                }
                else
                {
                    if (objectMap.get(SELECT_TYPE).equals(TYPE_PROJECT_CONCEPT))
                    {
                        statusGif = ProgramCentralConstants.EMPTY_STRING;
                    }
                    else
                    {
                        if (objectMap.get(SELECT_CURRENT).equals(STATE_PROJECT_COMPLETE))
                        {
                            statusGif =
                                "<img src=\"images/iconStatusGreen.gif\" border=\"0\" alt=\"";
                            statusGif += (sStatusOnTime + "\"/>");
    						stooltip=sStatusOnTime;
                        }
                        else if (!objectMap.get(SELECT_CURRENT).equals(STATE_PROJECT_COMPLETE) &&
                              sysDate.after(baselineCurrentEndDate))
                        {
                            statusGif =
                                "<img src=\"images/iconStatusRed.gif\" border=\"0\" alt=\"";
                            statusGif += (sStatusLate + "\"/>");
    						stooltip=sStatusLate;
                        }
                        else if (!objectMap.get(SELECT_CURRENT).equals(STATE_PROJECT_COMPLETE) &&
                              (daysRemaining <= yellowRedThreshold))
                        {
                            statusGif =
                                "<img src=\"images/iconStatusYellow.gif\" border=\"0\" alt=\"";
                            statusGif += (sStatusBehindSchedule + "\"/>");
    						stooltip=sStatusBehindSchedule;
                        }
                        else
                        {
                            statusGif = ProgramCentralConstants.EMPTY_STRING;
                        }
                    }
                }
    			//showIcon.addElement(statusGif);
    			StringBuffer sBuff = new StringBuffer();
    			sBuff.append("<p title=\""+stooltip+"\">");
    			sBuff.append(statusGif);
    			sBuff.append("</p>");
    			showIcon.add(sBuff.toString());
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            return showIcon;
        }
    }
    /**
     * This method is used to get current phase of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     * @return List containing current phase of the related project.
     * @throws Exception if the operation fails
     * @since PMC 11-0
     */
   public java.util.List getCurrentPhase(Context context, String[] args) throws Exception {

        HashMap projectMap = (HashMap) JPO.unpackArgs(args);
        Map paramList = (Map) projectMap.get("paramList");
        String suiteDirectory = (String)paramList.get("SuiteDirectory");
        String strPrinterFriendly = (String)paramList.get("reportFormat");
        boolean isPrinterFriendly = false;
        if(strPrinterFriendly != null){
            isPrinterFriendly = true;
        }

		List currentPhase = new StringList();
        MapList projectDetails = getProjectDetails(context,args);
        Iterator prjIterator = projectDetails.iterator();
        while (prjIterator.hasNext()){
            String displayPhase = "";
            Map projectDetailsMap = (HashMap) prjIterator.next();
            String phase = (String) projectDetailsMap.get("currentPhase");
            String phaseURL = (String) projectDetailsMap.get("currentPhaseURL");
			String sType = (String) projectDetailsMap.get("type");
			String phaseIcon = ProgramCentralConstants.EMPTY_STRING;
			
			
			if(TYPE_PHASE.equalsIgnoreCase(sType)){
				phaseIcon = "iconSmallPhase.png";
			}else if(TYPE_GATE.equalsIgnoreCase(sType)){
				phaseIcon = "iconSmallGate.png";
			}
			
			if(TYPE_PROJECT_SPACE.equalsIgnoreCase(sType)){
				phaseIcon ="iconSmallProject.png";
			}
			if(TYPE_PROJECT_CONCEPT.equalsIgnoreCase(sType)){
				phaseIcon ="iconSmallProjectConcept.gif";
			}
            phaseURL =  XSSUtil.encodeForHTML(context, phaseURL);

            if(!"".equals(phase)){
                StringBuffer sbDisplayPhase = new StringBuffer(100);
                sbDisplayPhase.append("<a title='").append(XSSUtil.encodeForXML(context, phase));
            	sbDisplayPhase.append("' href ='").append(phaseURL);
            	sbDisplayPhase.append("'>");
				sbDisplayPhase.append("<img src=\"../common/images/"+phaseIcon+"\" name=\"imgTask\" border=\"0\"/>");
                sbDisplayPhase.append(XSSUtil.encodeForXML(context,phase));
                sbDisplayPhase.append("</a>");

                if(!isPrinterFriendly) {
                   displayPhase = sbDisplayPhase.toString();
                } else {
                   displayPhase = phase;
                }
            }
            currentPhase.add(displayPhase);
        }
        return currentPhase;
     }

    /**
     * This method is used to get current phase estimated end date of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     * @return Vector containing current phase estimated end date of the related project
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public Vector getCurrentPhaseEstimatedEndDate(Context context, String[] args) throws Exception {

         Vector currentPhaseEstEndDate = new Vector();
         try{
             MapList projectDetails = getProjectDetails(context,args);
             Iterator prjIterator = projectDetails.iterator();
             while (prjIterator.hasNext()){
                Map projectDetailsMap = (HashMap) prjIterator.next();
                String strCurrentPhaseEstimatedEndDate = (String) projectDetailsMap.get("CurrentPhaseEstimatedEndDate");
                currentPhaseEstEndDate.add(strCurrentPhaseEstimatedEndDate);
            }
         }
         catch(Exception e){
            e.printStackTrace();
         }
         return currentPhaseEstEndDate;
   }

    /**
     * This method is used to get current phase actual end date of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     * @return Vector containing current phase actual end date of the related project.
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public Vector getCurrentPhaseActualEndDate(Context context, String[] args) throws Exception {

       Vector currentPhaseActualEndDate = new Vector();
       try{
          MapList projectDetails = getProjectDetails(context,args);
          Iterator prjIterator = projectDetails.iterator();
           while (prjIterator.hasNext()){
                Map projectDetailsMap = (HashMap) prjIterator.next();
                String strCurrentPhaseActualEndDate = (String) projectDetailsMap.get("CurrentPhaseActualEndDate");
                currentPhaseActualEndDate.add(strCurrentPhaseActualEndDate);
            }
       }
       catch(Exception e){
           e.printStackTrace();
       }
       return currentPhaseActualEndDate;
    }

    /**
     * This method is used to get slip days of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     * @return List containing slip days of the related project
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public java.util.List getSlipDays(Context context, String[] args) throws Exception{

         java.util.List slipDays = new StringList();
         try{
             MapList projectDetails = getProjectDetails(context,args);
             Iterator prjIterator = projectDetails.iterator();

             while (prjIterator.hasNext()){
                Map projectDetailsMap = (HashMap) prjIterator.next();
                String strSlipDays = (String) projectDetailsMap.get("slipDays");
                String col = (String) projectDetailsMap.get("col");

                if(!"".equals(strSlipDays)){
                    StringBuffer sbSlipColor = new StringBuffer(100);
                    sbSlipColor.append("<font color='");
                    sbSlipColor.append(col);
                    sbSlipColor.append("'><b>");
                    sbSlipColor.append(XSSUtil.encodeForHTML(context,strSlipDays));
                    sbSlipColor.append("</b></font>");
                    slipDays.add(sbSlipColor.toString());
                }
                else{
                    slipDays.add("");
                }
            }
         }
         catch(Exception e){
            e.printStackTrace();
         }
         return slipDays;
   }

    /**
     * This method is used to get percentage completion of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     * @return List containing precentage completion of the related project
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public java.util.List getPercentDone(Context context, String[] args) throws Exception{

         java.util.List percentDone = new StringList();
         try{
             MapList projectDetails = getProjectDetails(context,args);
             Iterator prjIterator = projectDetails.iterator();

             while (prjIterator.hasNext()){
                Map projectDetailsMap = (HashMap) prjIterator.next();
                String percentComplete = (String) projectDetailsMap.get("percentComplete");
                percentDone.add(percentComplete);
            }
         }
         catch(Exception e){
            e.printStackTrace();
         }
         return percentDone;
   }

    /**
     * This method is used to get risks associated with the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     * @return List containing risks of the related project.
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public java.util.List getRisks(Context context, String[] args) throws Exception {

         java.util.List risks = new StringList();

         try{
            com.matrixone.apps.program.ProjectSpace projectSpace =
            (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);
            com.matrixone.apps.program.Risk risk =
            (com.matrixone.apps.program.Risk) DomainObject.newInstance(context, DomainConstants.TYPE_RISK, DomainConstants.PROGRAM);

            int riskValue1 = 0;
            int riskValue2 = 0;

            int thresholdValues[]    = getThresholdValues(context);
            String colorCodeValues[] = getColorCodeValues(context);

            riskValue1 = thresholdValues[2];
            riskValue2 = thresholdValues[3];

            String riskColor1 = colorCodeValues[3];
            String riskColor2 = colorCodeValues[4];
            String riskColor3 = colorCodeValues[5];

            // to get RPN values
            StringList risk_relSelects =  new StringList(3);
            risk_relSelects.add(com.matrixone.apps.program.RiskRPNRelationship.SELECT_EFFECTIVE_DATE);
            risk_relSelects.add(com.matrixone.apps.program.RiskRPNRelationship.SELECT_RISK_RPN_VALUE);
            risk_relSelects.add(com.matrixone.apps.program.RiskRPNRelationship.SELECT_RISK_IMPACT);

            HashMap projectMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) projectMap.get("objectList");
            Map paramList = (Map) projectMap.get("paramList");
            String suiteDirectory = (String)paramList.get("SuiteDirectory");

            String strPrinterFriendly = (String)paramList.get("reportFormat");
            boolean isPrinterFriendly = false;
            if(strPrinterFriendly != null){
                isPrinterFriendly = true;
            }

            Iterator objectListIterator = objectList.iterator();
            while (objectListIterator.hasNext()){
                String strDisplayRisk = "";
                Map objectMap = (Map) objectListIterator.next();
                String objectId = (String) objectMap.get(projectSpace.SELECT_ID);
                projectSpace.setId(objectId);
                //String riskURL = "../programcentral/emxProgramCentralRiskSummaryFS.jsp?objectId=" + objectId;
                //String riskURL = "../common/emxTable.jsp?program=emxRisk:getActiveRisks,emxRisk:getCompletedRisks,emxRisk:getAllRisks&programLabel=emxProgramCentral.Common.ActiveProjects,emxProgramCentral.Common.CompletedProjects,emxProgramCentral.Common.All&table=PMCRisksSummary&portaltable=PMCPortalRiskSummary&selection=multiple&suiteKey=ProgramCentral&Export=true&sortColumnName=Title&sortDirection=ascending&toolbar=PMCRisksSummaryToolBar&header=emxProgramCentral.ProgramTop.RisksProject&HelpMarker=emxhelprisksummary&objectId=" + objectId;
                String riskURL = UIMenu.getHRef(UIMenu.getCommand(context, "PMCRisk"));
                riskURL = riskURL.replace("${COMMON_DIR}","../common");
                riskURL = riskURL.replace("${SUITE_DIR}","../programcentral");
                riskURL=riskURL+"&objectId=" + XSSUtil.encodeForURL(context,objectId) + "&suiteKey=ProgramCentral";
                riskURL = riskURL.replaceAll("&","&amp;");
                int yellowRisks=0;
                int redRisks=0;
                int greenRisks=0;
                int displayRisks=0;
                double rpnNumber=0;
                String riskCol ="";
                StringList rbusSelects = new StringList(2);
                rbusSelects.add(risk.SELECT_ID);
                rbusSelects.add(risk.SELECT_NAME);
                MapList projectRisks=risk.getRisks(context,projectSpace,rbusSelects,risk_relSelects,null);
                int riskValues[] = new int[3];
                Map currentRisk = null;
                MapList RPNList = null;
                String RPN ="0";

                if (! projectRisks.isEmpty())
                {
                ListIterator listItr2 = projectRisks.listIterator();

                while(listItr2.hasNext())
                {
                  currentRisk=(Map)listItr2.next();
                  risk.setId((String)currentRisk.get(risk.SELECT_ID));
                  RPNList = risk.getRPNs(context, risk_relSelects, null);
                  RPN = "0.0";
                  if(!RPNList.isEmpty())
                  {
                    Map RPNMap = (Map) RPNList.get(0);
                    RPN = (String) RPNMap.get(com.matrixone.apps.program.RiskRPNRelationship.SELECT_RISK_RPN_VALUE);
                  }

                  try
                  {
                    rpnNumber=Task.parseToDouble(RPN);
                  }
                  catch(Exception e)
                  {
                    //do nothing
                  }

                  if ( rpnNumber >= riskValue2 )
                  {
                    redRisks++;
                  }
                  else if ( rpnNumber > riskValue1 && rpnNumber < riskValue2 )
                  {
                    yellowRisks++;
                  }
                  else
                  {
                    greenRisks++;
                  }
                } // end of while loop
                }
                if(redRisks != 0)
                {
                    displayRisks=redRisks;
                    riskCol ="#"+riskColor3;
                }
                else if(yellowRisks != 0)
                {
                    displayRisks=yellowRisks;
                    riskCol = "#"+riskColor2;
                }
                else
                {
                    displayRisks=greenRisks;
                    riskCol = "#"+riskColor1;
                }

                String strRisks = String.valueOf(displayRisks);

                StringBuffer sbDisplayRisk = new StringBuffer(100);
                sbDisplayRisk.append("<a href=\"javascript:showModalDialog('");
                sbDisplayRisk.append(riskURL);
                sbDisplayRisk.append("')\">");
                sbDisplayRisk.append("<font color='");
                sbDisplayRisk.append(riskCol);
                sbDisplayRisk.append("'><b>");
                sbDisplayRisk.append(XSSUtil.encodeForHTML(context,strRisks));
                sbDisplayRisk.append("</b></font></a>");

                if(!isPrinterFriendly) {
                   strDisplayRisk = sbDisplayRisk.toString();
                } else {
                   strDisplayRisk = new StringBuffer(100).append("<font color='").append(riskCol).append("'><b>").append(XSSUtil.encodeForHTML(context,strRisks)).append("</b></font>").toString();
                }
                risks.add(strDisplayRisk);
            }
         }
         catch(Exception e){
            e.printStackTrace();
         }
         return risks;
   }

    /**
     * This method is used to get project assessment of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     * @return List containing project assessment of the related project.
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public java.util.List getProjectAssessment(Context context, String[] args) throws Exception {

         java.util.List projectAssessment = new StringList();

         // Status colors;
         String red    = "Red";
         String green  = "Green";
         String yellow = "Yellow";
         String other  = "---";

         try{
            com.matrixone.apps.program.ProjectSpace projectSpace =
            (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);

            HashMap projectMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) projectMap.get("objectList");
            Map paramList = (Map) projectMap.get("paramList");
            String suiteDirectory = (String)paramList.get("SuiteDirectory");

            String strPrinterFriendly = (String)paramList.get("reportFormat");
            boolean isPrinterFriendly = false;
            if(strPrinterFriendly != null){
                isPrinterFriendly = true;
            }

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            Iterator objectListIterator = objectList.iterator();
            while (objectListIterator.hasNext()){
                String displayAssessment = "";
                Map objectMap = (Map) objectListIterator.next();
                String objectId = (String) objectMap.get(projectSpace.SELECT_ID);
                projectSpace.setId(objectId);

                // assessment selectables

                  StringList AssessmentbusSelects = new StringList(2);
                  AssessmentbusSelects.add(com.matrixone.apps.program.Assessment.SELECT_ORIGINATED);
                  AssessmentbusSelects.add(com.matrixone.apps.program.Assessment.SELECT_ASSESSMENT_STATUS );
                  MapList assessmentList = new MapList();
                  assessmentList=com.matrixone.apps.program.Assessment.getAssessments(context,projectSpace,AssessmentbusSelects,null,null,null);

                  Map LatestAssessment=null;
                  String status = "";
                  String assessmentURL = "";

                  if (! assessmentList.isEmpty())
                  {
                    Iterator assessmentListItr = assessmentList.iterator();
                    String dtstr1="";
                    String dtstr2="";
                    while(assessmentListItr.hasNext())
                    {
                      Map current       = (Map) assessmentListItr.next();
                      if(LatestAssessment==null)
                      {
                        LatestAssessment = current;
                      }
                      else
                      {
                        dtstr1 = (String) LatestAssessment.get(com.matrixone.apps.program.Assessment.SELECT_ORIGINATED);
                        dtstr2 = (String) current.get(com.matrixone.apps.program.Assessment.SELECT_ORIGINATED);
                        java.util.Date max  = sdf.parse(dtstr1);
                        java.util.Date min1 = sdf.parse(dtstr2);
                        if(max.compareTo(min1)<0)
                        {
                          LatestAssessment = current;
                        }
                      }
                    }
                  }

                  if(LatestAssessment!=null)
                  {
                    //assessmentURL = "../programcentral/emxProgramCentralAssessmentSummaryFS.jsp?objectId=" + objectId;
                    //assessmentURL = "../common/emxTable.jsp?program=emxAssessment:getAssessment&table=PMCAssessmentSummary&portaltable=PMCPortalAssessmentSummary&selection=multiple&sortColumnName=Name&sortDirection=ascending&toolbar=PMCAssessSummaryToolBar&header=emxProgramCentral.Common.AssessmentsPageHeading&HelpMarker=emxhelpassessmentsummary&Export=false&suiteKey=ProgramCentral&objectId=" + objectId;
                    //assessmentURL = assessmentURL.replaceAll("&","&amp;");
                    assessmentURL = UIMenu.getHRef(UIMenu.getCommand(context, "PMCAssessment"));
                    assessmentURL = assessmentURL.replace("${COMMON_DIR}","../common");
                    assessmentURL=assessmentURL+"&objectId=" +XSSUtil.encodeForURL(context,(String)objectId) + "&suiteKey=ProgramCentral";
                    assessmentURL = assessmentURL.replaceAll("&","&amp;");
                    status = (String)LatestAssessment.get(com.matrixone.apps.program.Assessment.SELECT_ASSESSMENT_STATUS);
                  }

               StringBuffer sbAssessment = new StringBuffer(100);
               sbAssessment.append("<a href=\"javascript:showModalDialog('");
               sbAssessment.append(assessmentURL);
               sbAssessment.append("')\">");

               if(!"".equals(status)){
                   if(status.equals(red)){
                       if(!isPrinterFriendly){
                           displayAssessment = sbAssessment.append("<img src=\"../common/images/iconStatusRed.gif\" border=\"0\" /></a>").toString();
                       }
                       else{
                           displayAssessment = "<img src=\"../common/images/iconStatusRed.gif\" border=\"0\" />";
                       }
                   }
                   else if(status.equals(green)){
                       if(!isPrinterFriendly){
                           displayAssessment = sbAssessment.append("<img src=\"../common/images/iconStatusGreen.gif\" border=\"0\" /></a>").toString();
                       }
                       else{
                           displayAssessment = "<img src=\"../common/images/iconStatusGreen.gif\" border=\"0\" />";
                       }
                   }
                   else if(status.equals(yellow)){
                       if(!isPrinterFriendly){
                           displayAssessment = sbAssessment.append("<img src=\"../common/images/iconStatusYellow.gif\" border=\"0\" /></a>").toString();
                       }
                       else{
                          displayAssessment = "<img src=\"../common/images/iconStatusYellow.gif\" border=\"0\" />";
                       }
                   }
                   else if(status.equals(yellow)){
                       if(!isPrinterFriendly){
                           displayAssessment = sbAssessment.append("---</a>").toString();
                       }
                       else{
                           displayAssessment = "---";
                       }
                   }
               }
               else if("".equals(status)){
                   displayAssessment = "";
               }
               projectAssessment.add(displayAssessment);
            }
         }
         catch(Exception e){
            e.printStackTrace();
         }
         return projectAssessment;

   }

    /**
     * This method is used to get cost ratio of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     * @return List containing  cost ratio of the related project
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public java.util.List getCostRatio(Context context, String[] args) throws Exception {

        HashMap projectMap = (HashMap) JPO.unpackArgs(args);
        MapList objectList = (MapList) projectMap.get("objectList");
        Map paramList = (Map) projectMap.get("paramList");

        String strPrinterFriendly = (String)paramList.get("reportFormat");
        boolean isPrinterFriendly = false;
        if(strPrinterFriendly != null){
            isPrinterFriendly = true;
        }
        java.util.List costRatioList = new StringList();
        MapList projectDetails = getProjectFinancialDetails(context,args);
        Iterator prjIterator = projectDetails.iterator();

        while (prjIterator.hasNext()){
            String displayCostRatio = "";
            Map projectDetailsMap = (HashMap) prjIterator.next();
            String costURL = (String) projectDetailsMap.get("costURL");
            String costColor = (String) projectDetailsMap.get("costColor");
            String costRatio = (String) projectDetailsMap.get("costRatio");
                StringBuffer sbDisplayCostRatio = new StringBuffer(100);
            if(!ProgramCentralUtil.isNullString(costURL)
                    && !ProgramCentralUtil.isNullString(costRatio)){
                sbDisplayCostRatio.append("<a href=\"javascript:showModalDialog('");
                sbDisplayCostRatio.append(costURL);
                sbDisplayCostRatio.append("')\">");
                sbDisplayCostRatio.append("<font color='");
                sbDisplayCostRatio.append(costColor);
                sbDisplayCostRatio.append("'><b>");
                sbDisplayCostRatio.append(costRatio);
                sbDisplayCostRatio.append("</b></font></a>");
            }
            else
                sbDisplayCostRatio.append("");
                if(!isPrinterFriendly) {
                   displayCostRatio = sbDisplayCostRatio.toString();
                } else {
                   displayCostRatio = costRatio;
                }
            costRatioList.add(displayCostRatio);
        }
        return costRatioList;
     }

    /**
     * This method is used to get total cost of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     * @return List containing total cost of the related project
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public java.util.List getTotalCost(Context context, String[] args) throws Exception{

        HashMap projectMap = (HashMap) JPO.unpackArgs(args);
        MapList objectList = (MapList) projectMap.get("objectList");
        Map paramList = (Map) projectMap.get("paramList");

        String strPrinterFriendly = (String)paramList.get("reportFormat");
        boolean isPrinterFriendly = false;
        if(strPrinterFriendly != null){
            isPrinterFriendly = true;
        }

        java.util.List totalCostList = new StringList();

        MapList projectDetails = getProjectFinancialDetails(context,args);
        Iterator prjIterator = projectDetails.iterator();

        while (prjIterator.hasNext()){
            String displayTotalCost = "";
            Map projectDetailsMap = (HashMap) prjIterator.next();
            String totalCost = (String) projectDetailsMap.get("totalCost");
            String totalCostURL = (String) projectDetailsMap.get("totalCostURL");
            totalCostURL = totalCostURL.replaceAll("&","&amp;");
            if(!"".equals(totalCost)){
                StringBuffer sbDisplayTotalCost = new StringBuffer(100);
                sbDisplayTotalCost.append("<a href=\"javascript:showModalDialog('");
                sbDisplayTotalCost.append(totalCostURL);
                sbDisplayTotalCost.append("')\">");
                sbDisplayTotalCost.append(XSSUtil.encodeForHTML(context,(String)totalCost));
                sbDisplayTotalCost.append("</a>");

                if(!isPrinterFriendly) {
                   displayTotalCost = sbDisplayTotalCost.toString();
                } else {
                   displayTotalCost = XSSUtil.encodeForHTML(context,(String)totalCost);
                }
            }
            totalCostList.add(displayTotalCost);
        }
        return totalCostList;
   }

    /**
     * This method is used to get total benefits of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     * @return List containing total benefit of the related project
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public java.util.List getTotalBenefit(Context context, String[] args) throws Exception{

        HashMap projectMap = (HashMap) JPO.unpackArgs(args);
        MapList objectList = (MapList) projectMap.get("objectList");
        Map paramList = (Map) projectMap.get("paramList");

        String strPrinterFriendly = (String)paramList.get("reportFormat");
        boolean isPrinterFriendly = false;
        if(strPrinterFriendly != null){
            isPrinterFriendly = true;
        }

        java.util.List totalBenefitList = new StringList();

        MapList projectDetails = getProjectFinancialDetails(context,args);
        Iterator prjIterator = projectDetails.iterator();

        while (prjIterator.hasNext()){
            String displayTotalBenefit = "";
            Map projectDetailsMap = (HashMap) prjIterator.next();
            String totalBenefit = (String) projectDetailsMap.get("totalBenefit");
            String totalBenefitURL = (String) projectDetailsMap.get("totalBenefitURL");
            totalBenefitURL = totalBenefitURL.replaceAll("&","&amp;");
            if(!"".equals(totalBenefit)){
                StringBuffer sbDisplayTotalBenefit = new StringBuffer(100);
                sbDisplayTotalBenefit.append("<a href=\"javascript:showModalDialog('");
                sbDisplayTotalBenefit.append(totalBenefitURL);
                sbDisplayTotalBenefit.append("')\">");
                sbDisplayTotalBenefit.append(XSSUtil.encodeForHTML(context,totalBenefit));
                sbDisplayTotalBenefit.append("</a>");

                if(!isPrinterFriendly) {
                   displayTotalBenefit = sbDisplayTotalBenefit.toString();
                } else {
                   displayTotalBenefit = XSSUtil.encodeForHTML(context,totalBenefit);
                }
            }
            totalBenefitList.add(displayTotalBenefit);
        }
        return totalBenefitList;
   }

    /**
     * This method is used to get total benefit of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     * @return List containing total benefit of the related project
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public java.util.List getQuality(Context context, String[] args) throws Exception{

       java.util.List qualityList = new StringList();
       String displayQuality = "";
       try{
            com.matrixone.apps.program.ProjectSpace projectSpace =
            (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);

            HashMap projectMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) projectMap.get("objectList");
            Map paramList = (Map) projectMap.get("paramList");
            String strPrinterFriendly = (String)paramList.get("reportFormat");
            boolean isPrinterFriendly = false;
            if(strPrinterFriendly != null){
                isPrinterFriendly = true;
            }

            Iterator objectListIterator = objectList.iterator();
            while (objectListIterator.hasNext()){
                Map objectMap = (Map) objectListIterator.next();
                String objectId = (String) objectMap.get(projectSpace.SELECT_ID);

                //String qualityURL="../programcentral/emxProgramCentralQualitySummaryFS.jsp?objectId=" + objectId;
                //String qualityURL="../common/emxTable.jsp?program=emxQuality:getAllProjectQuality,emxQuality:getControlledProjectQuality,emxQuality:getActiveProjectQuality&programLabel=emxProgramCentral.Common.All,emxProgramCentral.Common.Controlled,emxProgramCentral.Common.ActiveProjects&table=PMCProjectQuality&toolbar=PMCProjectQualityToolbar&Export=true&sortColumnName=Name&sortDirection=ascending&header=emxProgramCentral.Common.QualityPageHeading&HelpMarker=emxhelpqualitysummary&suiteKey=ProgramCentral&objectId=" + objectId;
                String qualityURL = UIMenu.getHRef(UIMenu.getCommand(context, "PMCQuality"));
                qualityURL = qualityURL.replace("${COMMON_DIR}","../common");
                qualityURL=qualityURL+"&objectId=" + XSSUtil.encodeForURL(context, objectId)+"&suiteKey=ProgramCentral";
                qualityURL = qualityURL.replaceAll("&","&amp;");

                StringBuffer sbDisplayQuality = new StringBuffer(100);
                sbDisplayQuality.append("<a href=\"javascript:showModalDialog('");
                sbDisplayQuality.append(qualityURL);
                sbDisplayQuality.append("')\">");
                sbDisplayQuality.append("<img src=\"../common/images/iconQualityMeasure.gif\" border=\"0\" />");
                sbDisplayQuality.append("</a>");

                if(!isPrinterFriendly) {
                   displayQuality = sbDisplayQuality.toString();
                } else {
                   displayQuality = "<img src=\"../common/images/iconQualityMeasure.gif\" border=\"0\" />";
                }
                qualityList.add(displayQuality);
             }
       }
       catch(Exception e){
           e.printStackTrace();
       }
       return qualityList;
   }

    /**
     * This method is used to get the Color Code Values from the properties file
     * @return String[] containing all the Color Code Values
     * @throws Excepton if operation fails.
     * @deprecated Use getColorCodeValues(Context context) instead.
     */
    public String[] getColorCodeValues() throws Exception{
        return getColorCodeValues(this.context);
    }

    /**
     * This method is used to get the Color Code Values from the properties file
     * @param context the ENOVIA <code>Context</code> object
     * @return String[] containing all the Color Code Values
     * @throws Excepton if operation fails.
     */
    public String[] getColorCodeValues(Context context) throws Exception
    {
        String slipColor1="008000";
        String slipColor2="FFCC00";
        String slipColor3="FF0000";
        String riskColor1="008000";
        String riskColor2="FFCC00";
        String riskColor3="FF0000";
        String costRatioColor1="008000";
        String costRatioColor2="FFCC00";
        String costRatioColor3="FF0000";
        String proName ="emxProgramCentral";
        String ascendText1 ="eServiceApplicationProgramCentral.SlipThreshholdColor1";
        String ascendText2 ="eServiceApplicationProgramCentral.SlipThreshholdColor2";
        String ascendText3 ="eServiceApplicationProgramCentral.SlipThreshholdColor3";
        String ascendText4 ="eServiceApplicationProgramCentral.RiskThreshholdColor1";
        String ascendText5 ="eServiceApplicationProgramCentral.RiskThreshholdColor2";
        String ascendText6 ="eServiceApplicationProgramCentral.RiskThreshholdColor3";
        String ascendText7 ="eServiceApplicationProgramCentral.CostRatioThreshholdColor1";
        String ascendText8 ="eServiceApplicationProgramCentral.CostRatioThreshholdColor2";
        String ascendText9 ="eServiceApplicationProgramCentral.CostRatioThreshholdColor3";

        String s1 = EnoviaResourceBundle.getProperty(context, ascendText1);
        slipColor1=s1.trim();
        String s2 = EnoviaResourceBundle.getProperty(context, ascendText2);
        slipColor2=s2.trim();
        String s3 = EnoviaResourceBundle.getProperty(context, ascendText3);
        slipColor3=s3.trim();
        String s4 = EnoviaResourceBundle.getProperty(context, ascendText4);
        riskColor1=s4.trim();
        String s5 = EnoviaResourceBundle.getProperty(context, ascendText5);
        riskColor2=s5.trim();
        String s6 = EnoviaResourceBundle.getProperty(context, ascendText6);
        riskColor3=s6.trim();
        String s7 = EnoviaResourceBundle.getProperty(context, ascendText7);
        costRatioColor1=s7.trim();
        String s8 = EnoviaResourceBundle.getProperty(context, ascendText8);
        costRatioColor2=s8.trim();
        String s9 = EnoviaResourceBundle.getProperty(context, ascendText9);
        costRatioColor3=s9.trim();
        String values[]={slipColor1,slipColor2,slipColor3,riskColor1,riskColor2,riskColor3,costRatioColor1,costRatioColor2,costRatioColor3};

        return values;
    }

    /**
     * This method is used to get the Threshold Values from the properties file
     * @return int[] containing all the the Threshold Values
     * @throws Exception if the operation fails
     * @deprecated Use getThresholdValues(Context context) instead
     */
    public int[] getThresholdValues() throws Exception{
        return getThresholdValues(this.context);
    }

    /**
     * This method is used to get the Threshold Values from the properties file
     * @param context the ENOVIA <code>Context</code> object.
     * @return int[] containing all the the Threshold Values
     * @throws Exception if the operation fails
     */
    public int[] getThresholdValues(Context context) throws Exception
    {
        int slipValue1=0;
        int slipValue2=0;
        int riskValue1=0;
        int riskValue2=0;
        int costratioValue1=100;
        int costratioValue2=110;
        String proName ="emxProgramCentral";
        String ascendText1 ="eServiceApplicationProgramCentral.SlipThresholdGreenYellow";
        String ascendText2 ="eServiceApplicationProgramCentral.SlipThresholdYellowRed";
        String ascendText3 ="eServiceApplicationProgramCentralRPNThreshold.Yellow";
        String ascendText4 ="eServiceApplicationProgramCentralRPNThreshold.Red";
        String ascendText5 ="eServiceApplicationProgramCentral.CostRatioThresholdGreenYellow";
        String ascendText6 ="eServiceApplicationProgramCentral.CostRatioThresholdYellowRed";

        String s1 = EnoviaResourceBundle.getProperty(context, ascendText1);
        s1=s1.trim();
        slipValue1 = Integer.parseInt(s1);
        String s2 = EnoviaResourceBundle.getProperty(context, ascendText2);
        s2=s2.trim();
        slipValue2 = Integer.parseInt(s2);
        String s5 = EnoviaResourceBundle.getProperty(context, ascendText5);
        if ( s5 != null )
        {
             s5=s5.trim();
             costratioValue1 = Integer.parseInt(s5);
        }
        String s6 = EnoviaResourceBundle.getProperty(context, ascendText6);
        if ( s6 != null )
        {
             s6=s6.trim();
             costratioValue2 = Integer.parseInt(s6);
        }

        String s3 = EnoviaResourceBundle.getProperty(context, ascendText3);
        s3=s3.trim();
        riskValue1 = Integer.parseInt(s3);
        String s4 = EnoviaResourceBundle.getProperty(context, ascendText4);
        s4=s4.trim();
        riskValue2 = Integer.parseInt(s4);

        int values[]={slipValue1,slipValue2,riskValue1,riskValue2,costratioValue1,costratioValue2};
        return values;
    }

    /**
     * Returns a list of financial items(Budget and Benefit) of a project.
     * @param context the enovia <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     *        paramList - Map containing the list of parameters with its corresponding values
     * @return A list of map containing project's financial items details.
     * @throws Exception if the operation fails
     */
    public MapList getProjectFinancialDetails(Context context, String[] args) throws Exception
    {
        int thresholdValues[] = getThresholdValues(context);
        String colorCodeValues[] = getColorCodeValues(context);
        int costratioValue1 = thresholdValues[4];
        int costratioValue2 = thresholdValues[5];
        String costRatioColor1 = colorCodeValues[6];
        String costRatioColor2 = colorCodeValues[7];
        String costRatioColor3 = colorCodeValues[8];
        MapList projectFinancialDetails = new MapList();
        String costURL = "";
        String totalCostURL = "";
        String totalBenefitURL = "";
        try{
            com.matrixone.apps.program.ProjectSpace projectSpace =
            (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);
            //Financial selectables
            StringList FinancialbusSelects = new StringList(7);
            FinancialbusSelects.add(com.matrixone.apps.program.FinancialItem.SELECT_ID);
            FinancialbusSelects.add(com.matrixone.apps.program.FinancialItem.SELECT_NAME);
            FinancialbusSelects.add(com.matrixone.apps.program.FinancialItem.SELECT_PLANNED_COST);
            FinancialbusSelects.add(com.matrixone.apps.program.FinancialItem.SELECT_ACTUAL_COST);
            FinancialbusSelects.add(com.matrixone.apps.program.FinancialItem.SELECT_ESTIMATED_COST);
            FinancialbusSelects.add(com.matrixone.apps.program.FinancialItem.SELECT_PLANNED_BENEFIT);
            FinancialbusSelects.add(com.matrixone.apps.program.FinancialItem.SELECT_ACTUAL_BENEFIT);
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) programMap.get("objectList");
            Map paramList = (Map) programMap.get("paramList");
            String strPrinterFriendly = (String)paramList.get("reportFormat");
            boolean isPrinterFriendly = false;
            if(strPrinterFriendly != null){
                isPrinterFriendly = true;
            }
            Iterator objectListIterator = objectList.iterator();
            while (objectListIterator.hasNext()){
                Map projectMap = new HashMap();
                Map objectMap = (Map) objectListIterator.next();
                String objectId = (String) objectMap.get(projectSpace.SELECT_ID);
                projectSpace.setId(objectId);

                // for getting the values for Totalcost, Cost Ration and Totalbenefit columns
                  MapList financeItemList = null;
                  Map budget              = null;
                  double totalbenift      = 0.0;
                  double totalcost        = 0.0;
                  int costratio           = 0;
                  String costId           = "";
                  DomainObject fItem = null;
                  boolean isBudget = false;
                  String strTotalCost = "";
                  String strCostRatio = "";
                  String strTotalBenefit = "";
                  financeItemList = com.matrixone.apps.program.FinancialItem.getFinancialItems(context,projectSpace,FinancialbusSelects);

                  if (! financeItemList.isEmpty())
                  {
                    Iterator financeItr = financeItemList.iterator();
                    while(financeItr.hasNext())
                    {
                      budget   = (Map) financeItr.next();
                      costId   = (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_ID);
                      fItem = DomainObject.newInstance(context, costId);
                      if(fItem.isKindOf(context, ProgramCentralConstants.TYPE_BUDGET))
                          isBudget = true;
                      else
                          isBudget = false;
                      if(isBudget){
                          //Total Cost calculation
                      if(Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_ACTUAL_COST) ) == 0.0  )
                      {
                        totalcost = totalcost + Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_PLANNED_COST));
                      }
                      else
                      {
                        totalcost = totalcost + Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_ACTUAL_COST) );
                      }

                          //Cost Ratio calculation
                      if(Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_PLANNED_COST)) == 0.0  )
                      {
                        costratio = 0;
                      }
                      else
                      {
                        if(Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_ACTUAL_COST) ) == 0.0  )
                        {
                          costratio = (int) ((Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_ESTIMATED_COST))/
                          Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_PLANNED_COST)))*100);
                        }
                        else
                        {
                          costratio = (int) ((Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_ACTUAL_COST) )/
                          Task.parseToDouble( (String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_PLANNED_COST)))*100);
                        }
                      }
                          strTotalCost = "$"+ totalcost;
                          strCostRatio = costratio + "%";
                          costURL = "../common/emxTree.jsp?objectId="+XSSUtil.encodeForURL(context,costId)+"&amp;AppendParameters=false";
                          totalCostURL = "../common/emxIndentedTable.jsp?table=PMCProjectBudgetPlanTable&toolbar=PMCProjectBudgetToolbar,PMCProjectBudgetFilter&selection=multiple&header=emxProgramCentral.Budget.ProjectBudget.Budget&suiteKey=ProgramCentral&editLink=true&expandProgram=emxProjectBudget:getTableExpandChildBudgetData&HelpMarker=emxhelpprojectbudgetlist&emxExpandFilter=3&objectId="+XSSUtil.encodeForURL(context,objectId);
                        }
                      //For benefit calculation
                      else{
                          if(Task.parseToDouble((String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_ACTUAL_BENEFIT)) == 0.0)
                          {
                            totalbenift = totalbenift + Task.parseToDouble((String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_PLANNED_BENEFIT));
                          }
                          else
                          {
                            totalbenift = totalbenift + Task.parseToDouble((String)budget.get(com.matrixone.apps.program.FinancialItem.SELECT_ACTUAL_BENEFIT));
                          }
                          strTotalBenefit = "$" + totalbenift;
                          totalBenefitURL = "../common/emxIndentedTable.jsp?table=PMCProjectBenefitSummaryTable&toolbar=PMCProjectBenefitOperationsToolBar,PMCProjectBenefitFilter&freezePane=Name&selection=multiple&header=emxProgramCentral.ProjectBenefit.Benefit&suiteKey=ProgramCentral&expandProgram=emxBenefitItem:getTableExpandChildProjectBenefitData&editLink=true&postProcessJPO=emxBenefitItem:postProcessRefresh&HelpMarker=emxhelpfinancialitemsummary&emxExpandFilter=3&objectId=" +XSSUtil.encodeForURL(context, objectId);
                      }
                    }
                    projectMap.put("costRatio", strCostRatio);
                    projectMap.put("totalCost", strTotalCost);
                    projectMap.put("totalBenefit", strTotalBenefit);
                  }
                  else {
                    projectMap.put("costRatio", "");
                    projectMap.put("totalCost", "");
                    projectMap.put("totalBenefit", "");
                  }

                  String costColor = "";
                  if ( costratio > costratioValue2 )
                  {
                    costColor = "#" + costRatioColor3;
                  }
                  else if ( costratio> costratioValue1 && costratio <= costratioValue2 )
                  {
                    costColor = "#" + costRatioColor2;
                  }
                  else
                  {
                    costColor= "#"+costRatioColor1;
                  }

                  projectMap.put("costColor", costColor);
                  projectMap.put("costURL", costURL);
                  //Modified for Bug#339965 on 08/20/2007 - Start
                  //String totalCostURL   = "../common/emxTable.jsp?program=emxFinancialItem:getCostItems&chart=false&calculations=false&objectCompare=false&toolbar=PMCProjectSpaceCostItemToolBar&selection=multiple&table=PMCProjectCostSummary&suiteKey=ProgramCentral&HelpMarker=emxhelpcostitemsummary&header=emxProgramCentral.ProgramTop.CostBudgetCategories&PrinterFriendly=true&objectId=" + costId;
                  //String totalBenefitURL   = "../common/emxTable.jsp?program=emxFinancialItem:getBenefitItems&chart=false&calculations=false&objectCompare=false&toolbar=PMCProjectSpaceBenefitItemToolBar&selection=multiple&table=PMCProjectBenefitSummary&suiteKey=ProgramCentral&HelpMarker=emxhelpbenefititemsummary&header=emxProgramCentral.ProgramTop.BenefitBudgetCategories&PrinterFriendly=true&objectId=" + costId;
                  //Modified for Bug#339965 on 08/20/2007 - End
                  projectMap.put("totalCostURL", totalCostURL);
                  projectMap.put("totalBenefitURL", totalBenefitURL);

                  projectFinancialDetails.add(projectMap);
          }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return projectFinancialDetails;

    }

    /**
     * This method is used to get project details of the related project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectList - MapList containing the objects list
     *        paramList - Map containing the list of parameters with its corresponding values
     * @return MapList containing project details of the related project
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
   public MapList getProjectDetails(Context context, String[] args) throws Exception
   {
	   double totalTasks = 0.0;
	   double completedTasks = 0.0;
	   long slipDay   = 0;
	   long slipDayab = 0;
	   String col     = "";
	   boolean slipFlag=true;
	   int slipValue1 = 0;
	   int slipValue2 = 0;
	   String slipColor1 = ProgramCentralConstants.EMPTY_STRING;
	   String slipColor2 = ProgramCentralConstants.EMPTY_STRING;
	   String slipColor3 = ProgramCentralConstants.EMPTY_STRING;
	   java.util.Date date1 =  new java.util.Date();
	   java.util.Date today = new java.util.Date();
	   today.setHours(0);
	   today.setMinutes(0);
	   today.setSeconds(0);
	   String strCurrentPhase = "";
	   ArrayList phaseList = new ArrayList();
	   ArrayList dateList = new ArrayList();
	   String phaseEstEndDate = ProgramCentralConstants.EMPTY_STRING;
	   String phaseActEndDate = ProgramCentralConstants.EMPTY_STRING;
	   String CurrentPhaseDate="";
	   String percentComplete = "0";
	   java.util.HashMap incompleteTask  = new java.util.HashMap();
	   java.util.HashMap completeTask    = new java.util.HashMap();
	   String phaseId = "";

	   //Read SlipThresholdValues from property file
	   if (slipFlag)
	   {
		   int thresholdValues[]    = {5,10,5,10,5,10};
		   String colorCodeValues[] = {"008000","FFCC00","FF0000","008000","FFCC00","FF0000","008000","FFCC00","FF0000"};
		   try
		   {
			   thresholdValues = getThresholdValues(context);
			   colorCodeValues = getColorCodeValues(context);
		   }
		   catch(Exception e)
		   {
			   //if threshold values are not there in the property file
		   }

		   slipValue1 = thresholdValues[0];
		   slipValue2 = thresholdValues[1];

		   slipColor1 = colorCodeValues[0];
		   slipColor2 = colorCodeValues[1];
		   slipColor3 = colorCodeValues[2];

		   slipFlag=false;
	   }

	   // Maplist to hold the projects' details
	   MapList projectDetails = new MapList();

	   try{
		   ProjectSpace projectSpace = (ProjectSpace) DomainObject.newInstance(context, 
				   DomainConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);
		   //com.matrixone.apps.program.Task task = (com.matrixone.apps.program.Task) DomainObject.newInstance(context,TYPE_TASK,PROGRAM);

		   //Formatting Date to Ematrix Date Format
		   boolean bDisplayTime = PersonUtil.getPreferenceDisplayTimeValue(context);
		   int iDateFormat = PersonUtil.getPreferenceDateFormatValue(context);

		   // get Task information
		   StringList task_busSelects =  new StringList(6);
		   task_busSelects.add(Task.SELECT_ID);
		   task_busSelects.add(Task.SELECT_TYPE);
		   task_busSelects.add(Task.SELECT_NAME);
		   task_busSelects.add(Task.SELECT_TASK_ESTIMATED_START_DATE);
		   task_busSelects.add(Task.SELECT_TASK_ACTUAL_FINISH_DATE);
		   task_busSelects.add(Task.SELECT_TASK_ESTIMATED_FINISH_DATE);
		   task_busSelects.add(Task.SELECT_CURRENT);
		   task_busSelects.add(Task.SELECT_BASELINE_CURRENT_END_DATE);
		   task_busSelects.add(Task.SELECT_PERCENT_COMPLETE);

		   StringList relSelects = new StringList(1);
		   //relSelects.add(SubtaskRelationship.SELECT_TASK_WBS);
		   //relSelects.add(SubtaskRelationship.SELECT_SEQUENCE_ORDER);

		   Map programMap = (Map) JPO.unpackArgs(args);
		   Map paramList = (Map) programMap.get("paramList");
		   String suiteDirectory = (String)paramList.get("SuiteDirectory");
		   String stringClientTimeOffset = (String) paramList.get("timeZone");
		   Locale locale = (Locale) paramList.get("localeObj");
		   double iClientTimeOffset = Task.parseToDouble(stringClientTimeOffset);

		   MapList objectList = (MapList) programMap.get("objectList");
		   int size = objectList.size();
		   String []projectIdArr = new String[size];
		   for(int i=0;i<size;i++){
			   Map projectMap = (Map)objectList.get(i);
			   String id = (String)projectMap.get(SELECT_ID);
			   projectIdArr[i] = id;
		   }
		   
		   MapList projectInfoList = ProgramCentralUtil.getObjectDetails(context, projectIdArr, new StringList(SELECT_CURRENT));
		   Map<String,Object> projectInfoMap = new HashMap();
		   
		   
		   for(int i=0;i<size;i++){
			   Map projectMap = (Map)objectList.get(i);
			   Map projectMap1 = (Map)projectInfoList.get(i);
			   Map projectTempMap = new HashMap();
			   
			   String id = (String)projectMap.get(SELECT_ID);
			   String state = (String)projectMap1.get(SELECT_CURRENT);
			   MapList projectTasks = null;
			   //Current phase should get computed for Active and Review projects.
			   if(ProgramCentralConstants.STATE_PROJECT_SPACE_ACTIVE.equalsIgnoreCase(state)
					   || ProgramCentralConstants.STATE_PROJECT_SPACE_REVIEW.equalsIgnoreCase(state)) {

				   projectSpace.setId(id);
				   projectTasks = Task.getTasks(context, (TaskHolder)projectSpace, 1, task_busSelects, relSelects);
			   } else {
				   projectTasks = new MapList();
			   }
			   projectTempMap.put(SELECT_CURRENT, state);
			   projectTempMap.put("TaskList", projectTasks);
			   
			   projectInfoMap.put(id, projectTempMap);
		   }
		   
		   Iterator objectListIterator = objectList.iterator();
		   StringBuffer sbCurrentPhaseURL = new StringBuffer(64);
		   for(int i=0;i<size;i++){
			   Map projectMap = new HashMap();
			   slipDay         = 0;
			   slipDayab       = 0;
			   col             = "";
			   totalTasks      = 0.0;
			   completedTasks  = 0.0;

			   strCurrentPhase = "";

			   Map objectMap = (Map) objectList.get(i);
			   String objectId = (String) objectMap.get(projectSpace.SELECT_ID);
			   Map projectDetailsMap 	= (Map) projectInfoMap.get(objectId);
			   String current 			= (String) projectDetailsMap.get(SELECT_CURRENT);
			  /* projectSpace.setId(objectId);

			   // Build busSelects and busWhere for retrieving owner information
			   String current = projectSpace.getInfo(context,projectSpace.SELECT_CURRENT);*/
			   //No need to compute current phase for project which are in following states.
			   if(ProgramCentralConstants.STATE_PROJECT_SPACE_CREATE.equals(current)
				   || ProgramCentralConstants.STATE_PROJECT_SPACE_ASSIGN.equals(current)
				   || ProgramCentralConstants.STATE_PROJECT_SPACE_COMPLETE.equals(current) 
					   || ProgramCentralConstants.STATE_PROJECT_SPACE_ARCHIVE.equals(current)){
				   projectMap.put("col", DomainConstants.EMPTY_STRING);
				   projectMap.put("currentPhase", DomainConstants.EMPTY_STRING);
				   projectMap.put("currentPhaseURL", DomainConstants.EMPTY_STRING);
				   projectDetails.add(projectMap);
				   continue;
			   }

			   //MapList projectTasks = projectSpace.getTasks(context,1,task_busSelects, relSelects,false);
			   //commented for 478380
			   //MapList projectTasks = Task.getTasks(context, (TaskHolder)projectSpace, 1, task_busSelects, relSelects);
			   MapList projectTasks = (MapList) projectDetailsMap.get("TaskList");
			   //clear the lists to make sure they are empty
			   incompleteTask.clear();
			   completeTask.clear();
			   if (! projectTasks.isEmpty())
			   {
				   Iterator taskItr = projectTasks.iterator();
				   while(taskItr.hasNext())
				   {
					   Map taskMap = (Map) taskItr.next();
					   String currstate = (String) taskMap.get(Task.SELECT_CURRENT);
					  // task.setId((String) taskMap.get(Task.SELECT_ID));
					   if ("Complete".equals(currstate))
					   {
						   //Complete State Block
						   String finishDate = "";
						   String baselineCurrentFinishDateStr = (String) taskMap.get(Task.SELECT_BASELINE_CURRENT_END_DATE);
						   if (null == baselineCurrentFinishDateStr || "".equals(baselineCurrentFinishDateStr))
						   {
							   finishDate = (String) taskMap.get(Task.SELECT_TASK_ACTUAL_FINISH_DATE);
						   }
						   else
						   {
							   finishDate = baselineCurrentFinishDateStr;
						   }
						   completeTask.put(finishDate + taskMap.get(Task.SELECT_ID),taskMap);
					   }
					   else
					   {
						   // Incomplete State Block
						   String finishDate = "";
						   String baselineCurrentFinishDateStr = (String) taskMap.get(Task.SELECT_BASELINE_CURRENT_END_DATE);
						   if (null == baselineCurrentFinishDateStr || "".equals(baselineCurrentFinishDateStr)){
							   finishDate = (String) taskMap.get(Task.SELECT_TASK_ESTIMATED_FINISH_DATE);
						   }else{
							   finishDate = baselineCurrentFinishDateStr;
						   }

						   incompleteTask.put(finishDate+taskMap.get(Task.SELECT_ID),taskMap);
					   }
				   } // end while
			   } //end if task list is not empty

			   Map minTask      = null;
			 
			   Map disply         = null;
			   String  currentPhaseURL   = ProgramCentralConstants.EMPTY_STRING;
			   String minStr = ProgramCentralConstants.EMPTY_STRING;
				String sType  = EMPTY_STRING;

			   //if there are tasks see which one we are going to set as the current phase
			   if ( !incompleteTask.isEmpty() || !completeTask.isEmpty() )
			   {
				   //all tasks are complete, get the task with the oldest end date
				   if(incompleteTask.isEmpty())
				   {/*
					   // COMPLETE
					   if(! completeTask.isEmpty())
					   {
						   Object keys[] = (completeTask.keySet()).toArray();
						   //put the keys in ascending order
						   java.util.Arrays.sort(keys);
						   minStr = (String)keys[0];
						   Date testDate;
						   int testOrder;
						   Date maxDate;
						   int maxOrder;
						   for (int i =0; i < keys.length; i++){
							   Map mTask = (Map)completeTask.get((String)keys[i]);
							   String strTaskActualFinishDate = (String)mTask.get(task.SELECT_TASK_ACTUAL_FINISH_DATE);
							   if(ProgramCentralUtil.isNotNullString(strTaskActualFinishDate)){
								   testDate = sdf.parse(strTaskActualFinishDate);
								   maxDate = sdf.parse(((String)((Map)completeTask.get(minStr)).get(task.SELECT_TASK_ACTUAL_FINISH_DATE)));
								   maxOrder = Integer.parseInt((String)((Map)completeTask.get(minStr)).get(SubtaskRelationship.SELECT_SEQUENCE_ORDER));
								   //If dates are same, take sequence order, to get the latest completed task.
								   if(testDate.equals(maxDate)){
									   testOrder = Integer.parseInt((String)mTask.get(SubtaskRelationship.SELECT_SEQUENCE_ORDER));
									   if(testOrder > maxOrder){
										   minStr = (String)keys[i];
									   }
								   }
								   else if(testDate.after(maxDate)){
									   minStr = (String)keys[i];
								   }
							   }
						   }//end for keys
					   }//end if completed list is not empty

					   disply = (Map) completeTask.get(minStr);
					   //if the task is complete use the Actual Finish date for slip days calculations
					   String actualFinishDate = (String)disply.get(task.SELECT_TASK_ACTUAL_FINISH_DATE);
					   if(ProgramCentralUtil.isNotNullString(actualFinishDate))
					   {
					   date1  = sdf.parse((String)disply.get(task.SELECT_TASK_ACTUAL_FINISH_DATE));
					   }
					   //if the task is complete use the estimated finish date
					   min    = sdf.parse((String)disply.get(task.SELECT_TASK_ESTIMATED_FINISH_DATE));

				   */}
				   //incomplete task list is not empty get the first task with the lowest estimated end date
				   //Or with min wbs Number comparing texicographically
				   //So 1.1.1 < 1.1.2
				   else
				   {
				       Map map = getCurrentPhase(context, projectTasks,current);
				       if(map!=null && !map.isEmpty())
					   {
						 disply = map;
					         //if the task is incomplete use the Estimated Finish date for slip days calculations
					          date1  = sdf.parse((String)disply.get(Task.SELECT_TASK_ESTIMATED_FINISH_DATE));
					   }/*else{
					   if(! incompleteTask.isEmpty())
					   {
						   Object keys[] = (incompleteTask.keySet()).toArray();
						   //put the keys in ascending order
						   java.util.Arrays.sort(keys);
						   minStr = (String)keys[0];

						   java.util.Date minDate;
						   java.util.Date test;
						   String wbsNumber = DomainConstants.EMPTY_STRING;
						   String minNumber = DomainConstants.EMPTY_STRING;
						   for (int i =0; i <keys.length; i++)
						   {
							   minDate   = sdf.parse(((String)((Map)incompleteTask.get(minStr)).get(task.SELECT_TASK_ESTIMATED_FINISH_DATE)));
							   test      = sdf.parse(((String)((Map)incompleteTask.get((String)keys[i])).get(task.SELECT_TASK_ESTIMATED_FINISH_DATE)));

							   minNumber = ((String)((Map)incompleteTask.get(minStr)).get(com.matrixone.apps.common.SubtaskRelationship.SELECT_TASK_WBS));
							   wbsNumber = ((String)((Map)incompleteTask.get((String)keys[i])).get(com.matrixone.apps.common.SubtaskRelationship.SELECT_TASK_WBS));
							   //Added:10-Feb-09:nzf:R207:PRG Bug:363823
							   boolean flag = false;
							   Task tskForCheking =  new Task();
							   if(tskForCheking.checkTaskSeniority(wbsNumber,minNumber)>0){
								   flag = true;
							   }
							   if(test.before(minDate) || (test.equals(minDate) && flag)){
								   //End:R207:PRG Bug:363823
								   minStr = (String)keys[i];
							   }
						   }
					   }//end if
					   disply = (Map) incompleteTask.get(minStr);
					   //if the task is incomplete use the Estimated Finish date for slip days calculations
					   date1  = sdf.parse((String)disply.get(task.SELECT_TASK_ESTIMATED_FINISH_DATE));
				   }*/
			   }
			   }

			   date1.setHours(0);
			   date1.setMinutes(0);
			   date1.setSeconds(0);

			   if(disply!=null) {
				   // format the date as required
				   phaseEstEndDate = (String)disply.get(Task.SELECT_TASK_ESTIMATED_FINISH_DATE);
				   phaseActEndDate = (String)disply.get(Task.SELECT_TASK_ACTUAL_FINISH_DATE);

				   phaseEstEndDate = eMatrixDateFormat.getFormattedDisplayDateTime(context,phaseEstEndDate, bDisplayTime, iDateFormat, iClientTimeOffset,locale);
				   phaseActEndDate = eMatrixDateFormat.getFormattedDisplayDateTime(context,phaseActEndDate, bDisplayTime, iDateFormat, iClientTimeOffset,locale);

					sType  = (String)disply.get(SELECT_TYPE);
					projectMap.put("type", sType);
				   //Added:22-July-11:MS9:R211:PRG IR-116187V6R2012x
				   String strFinishDate = phaseEstEndDate;
				   if(incompleteTask.isEmpty() && !completeTask.isEmpty())
				   {
					   strFinishDate = phaseActEndDate;
				   }

				   //CurrentPhaseDate= eMatrixDateFormat.getFormattedDisplayDateTime(context,strFinishDate, bDisplayTime, iDateFormat, iClientTimeOffset,locale);
				   CurrentPhaseDate= strFinishDate;
				   //End:22-July-11:MS9:R211:PRG IR-116187V6R2012x

				   if(CurrentPhaseDate != null) {
					   projectMap.put("CurrentPhaseDate", CurrentPhaseDate);
				   }
				   else {
					   projectMap.put("CurrentPhaseDate", "");
				   }


				   projectMap.put("CurrentPhaseEstimatedEndDate", phaseEstEndDate);
				   projectMap.put("CurrentPhaseActualEndDate", phaseActEndDate);

				   percentComplete = (String)disply.get(Task.SELECT_PERCENT_COMPLETE);
				   projectMap.put("percentComplete", percentComplete);

				   //determine slip days and color of slip days
				   if ( date1.after(today) ) {
					   //if the (Estimated/Actual) date is after the current date (min) then
					   //the milestone (task) is currently on time, so show the slip days as
					   //the amount of time until (Estimated/Actual) Finish date ( always green)
					   int dayOfWeek = today.getDay();
					   if (dayOfWeek  == 0 || dayOfWeek == 6) {
						   //don't remove day since start day is not a week day
						   slipDay = Task.computeDuration(today,date1);
					   } else {
						   //week day, so take out the starting day
						   slipDay = Task.computeDuration(today,date1) - 1;
					   }
					   //slip day color should always be green when it is before the (Estimated/Actual) Finish date
					   slipDayab = java.lang.Math.abs(slipDay);
					   col = "#"+slipColor1;
				   } else {
					   //calculate the slip days and change color according to the amount of days
					   //the milestone (task) has slipped
					   slipDay = Task.computeDuration(date1,today) - 1;//take out the starting day
					   //determine color of slip days
					   if ( slipDay >= slipValue2 ) {
						   slipDayab = java.lang.Math.abs(slipDay);
						   col       = "#" + slipColor3;
					   } else if ( slipDay > slipValue1 && slipDay < slipValue2 ) {
						   slipDayab = java.lang.Math.abs(slipDay);
						   col       = "#" + slipColor2;
					   } else {
						   slipDayab = java.lang.Math.abs(slipDay);
						   col = "#"+slipColor1;
					   }//ends else

					   // set the color to green if project is complete
					   if(incompleteTask.isEmpty())
					   {
						   col = "#"+slipColor1;
					   }
				   }//ends else
				   sbCurrentPhaseURL = new StringBuffer(64);
				   sbCurrentPhaseURL.append("../common/emxTree.jsp?objectId=");
				   sbCurrentPhaseURL.append(disply.get(Task.SELECT_ID));
				   //sbCurrentPhaseURL.append("&amp;AppendParameters=false&amp;emxSuiteDirectory=");
				   //sbCurrentPhaseURL.append(suiteDirectory);

				   strCurrentPhase = (String) disply.get(Task.SELECT_NAME);
				   projectMap.put("slipDays", String.valueOf(slipDayab));
			   }
			   else {
				   projectMap.put("CurrentPhaseEstimatedEndDate", "");
				   projectMap.put("CurrentPhaseActualEndDate", "");
					projectMap.put("type", sType);
				   // added for Dashboard table
				   projectMap.put("CurrentPhaseDate", "");
				   // till here
				   projectMap.put("slipDays", "");
			   }

			   projectMap.put("col", col);
			   projectMap.put("currentPhase", strCurrentPhase);
			   projectMap.put("currentPhaseURL", sbCurrentPhaseURL.toString());

			   projectDetails.add(projectMap);
		   }
	   }
	   catch(Exception e){
		   e.printStackTrace();
	   }
	   finally
	   {
		   return projectDetails;
	   }
   }


   /**
     * showOwnerWithHyperLink - displays the owner with lastname,firstname format
     *             also has a link to open a pop up with the owner
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        0 - MapList containing the object Maps
     *        1 - MapList containing the parameters
     * @return Vector containing the owner value as String
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    public java.util.List showOwnerWithHyperLink(Context context, String[] args)
      throws Exception
    {
        java.util.List lstOwner = new StringList();
        try
        {
            com.matrixone.apps.program.ProjectSpace project =
            (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,
                    DomainConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);

            HashMap projectMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) projectMap.get("objectList");
            Map paramList = (Map) projectMap.get("paramList");
            String suiteDirectory = (String)paramList.get("SuiteDirectory");

            String strPrinterFriendly = (String)paramList.get("reportFormat");
            boolean isPrinterFriendly = false;
            if(strPrinterFriendly != null){
                isPrinterFriendly = true;
            }

            Map objectMap = null;

            Iterator objectListIterator = objectList.iterator();
            String[] objIdArr = new String[objectList.size()];
            int arrayCount = 0;
            while (objectListIterator.hasNext())
            {
                objectMap = (Map) objectListIterator.next();
                objIdArr[arrayCount] =
                    (String) objectMap.get(project.SELECT_ID);
                arrayCount++;
            }

            MapList actionList = DomainObject.getInfo(context, objIdArr, new StringList(project.SELECT_OWNER));

            Iterator actionsListIterator = actionList.iterator();

            while (actionsListIterator.hasNext())
            {
                String displayOwner = "";
                objectMap = (Map) actionsListIterator.next();
                String owners = (String) objectMap.get(project.SELECT_OWNER);
                String strOwner = com.matrixone.apps.common.Person.getDisplayName(context, owners);
                //Added for special charater.
                strOwner    =   XSSUtil.encodeForXML(context, strOwner);
                com.matrixone.apps.common.Person tempPerson = com.matrixone.apps.common.Person.getPerson(context,owners);
                String ownerId = tempPerson.getInfo(context, com.matrixone.apps.common.Person.SELECT_ID);

                StringBuffer sbPersonURL = new StringBuffer(100);
                sbPersonURL.append("../common/emxTree.jsp?objectId=");
                sbPersonURL.append(XSSUtil.encodeForURL(context,ownerId));
                sbPersonURL.append("&amp;AppendParameters=false");
                sbPersonURL.append("&amp;emxSuiteDirectory=");
                sbPersonURL.append(suiteDirectory);

                StringBuffer sbDisplayOwner = new StringBuffer(100);
                sbDisplayOwner.append("<a href='").append(sbPersonURL.toString()).append("'>");
                sbDisplayOwner.append(strOwner);
                sbDisplayOwner.append("</a>");

                if(!isPrinterFriendly) {
                   displayOwner = sbDisplayOwner.toString();
                } else {
                   displayOwner = strOwner;
                }
                lstOwner.add(displayOwner);
            } //ends while
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw ex;
        }
        finally
        {
            return lstOwner;
        }
    }

    /**
     * This method is used to check whether a particular command is having access to the logged in user.
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        objectId - String containing object id.
     * @return boolean containing access to the command for the logged in user.
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
    public boolean hasAccessToCommand(Context context, String[] args)
      throws Exception
    {
        HashMap programMap = (HashMap) JPO.unpackArgs(args);
        String objectId = (String) programMap.get("objectId");
        com.matrixone.apps.program.ProjectSpace projectSpace =
          (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,
            DomainConstants.TYPE_PROJECT_SPACE, "PROGRAM");

        boolean access = false;
        try
        {
          if ((objectId != null) && !objectId.equals(""))
          {
            projectSpace.setId(objectId);
           access = projectSpace.checkAccess(context, (short) AccessConstants.cModify);
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }

    return access;
    }


     /**
     * This function displays the list of programs
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        0 - MapList containing the object Maps
     *        1 - MapList containing the parameters
     * @return List containing the program value as String
     * @throws Exception if the operation fails
     * @since PMC 10-7
     */
    public java.util.List getProgramWithHyperLink(Context context, String[] args)
      throws Exception
    {
        java.util.List programs = new StringList();
        try
        {
            com.matrixone.apps.program.ProjectSpace project =
                (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,
                    DomainConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);
            HashMap projectMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) projectMap.get("objectList");
            Map paramList = (Map) projectMap.get("paramList");
            Map objectMap = null;
            Iterator objectListIterator = objectList.iterator();
            String[] objIdArr = new String[objectList.size()];
            int arrayCount = 0;
            String strPrinterFriendly = (String)paramList.get("reportFormat");
            boolean isPrinterFriendly = false;
            if(strPrinterFriendly != null){
                isPrinterFriendly = true;
            }

            while (objectListIterator.hasNext())
            {
                objectMap = (Map) objectListIterator.next();
                objIdArr[arrayCount] = (String) objectMap.get(project.SELECT_ID);
                arrayCount++;
            }
            StringList objectSelects = new StringList();
            objectSelects.add(project.SELECT_PROGRAM_NAME);
            objectSelects.add(project.SELECT_PROGRAM_ID);

            String displayProgram = "";
            String program = "";
            String programId = "";
            MapList actionList = DomainObject.getInfo(context, objIdArr,objectSelects);
            Iterator actionsListIterator = actionList.iterator();
            while (actionsListIterator.hasNext())
            {
                objectMap = (Map) actionsListIterator.next();
                program = (String) objectMap.get(project.SELECT_PROGRAM_NAME);
                programId = (String) objectMap.get(project.SELECT_PROGRAM_ID);
                if(program == null || "null".equals(program)){
                    program = "";
                }
                StringBuffer sbProgramURL = new StringBuffer(100);
                sbProgramURL.append("../common/emxTree.jsp?objectId=");
                sbProgramURL.append(XSSUtil.encodeForURL(context,programId));
                sbProgramURL.append("&amp;AppendParameters=false");

                StringBuffer sbDisplayProgram = new StringBuffer(100);
                sbDisplayProgram.append("<a href='").append(sbProgramURL.toString()).append("'>");
                sbDisplayProgram.append(XSSUtil.encodeForHTML(context,program));
                sbDisplayProgram.append("</a>");

                if(!isPrinterFriendly) {
                    displayProgram = sbDisplayProgram.toString();
                }
                else {
                    displayProgram = XSSUtil.encodeForHTML(context,program);
                }
                programs.add(displayProgram);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw ex;
        }
        finally
        {
            return programs;
        }
    }
    /**
     * This method is used to get Dashboards  of the  project
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     * @return Vector containing current phase estimated end date of the  project
     * @throws Exception if the operation fails
     * @since X+2
     */

@com.matrixone.apps.framework.ui.ProgramCallable
public java.util.List getDashBoards(Context context, String[] args)
      throws Exception
    {
        com.matrixone.apps.program.Program program =
            (com.matrixone.apps.program.Program) DomainObject.newInstance(context,
                DomainConstants.TYPE_PROGRAM, DomainConstants.PROGRAM);
        com.matrixone.apps.common.Person person =
            com.matrixone.apps.common.Person.getPerson(context);
        com.matrixone.apps.program.ProjectSpace projectSpace =
            (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,
                DomainConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);

    String sArchiveStateName = PropertyUtil.getSchemaProperty(context,"policy",projectSpace.POLICY_PROJECT,"state_Archive");
    String selectState="";
    String dashboardName="";
    String busWhere = null;
    String relWhere = null;
    if (selectState == null || "".equals(selectState) || selectState.equalsIgnoreCase("---"))
    {
        selectState = "All";
    }

    // If selectState is All, then display all archived objects except concept
    // projects
    if (selectState.equals("All") || selectState.equals("---") )
    {
        busWhere = "current!='" + sArchiveStateName + "'&&type!='" + projectSpace.TYPE_PROJECT_CONCEPT + "'";
    }
    else
    {
        // Else, only display objects described by selectState filter (do not
        // include concept projects)
        busWhere = "current=='" + selectState + "'&&type!='" + projectSpace.TYPE_PROJECT_CONCEPT + "'" +
               "&&current!='" + sArchiveStateName + "'";
    }

    // get Project information
    StringList busSelects =  new StringList(7);
    busSelects.add(projectSpace.SELECT_ID);
    busSelects.add(projectSpace.SELECT_NAME);
    busSelects.add(projectSpace.SELECT_TYPE);
    busSelects.add(projectSpace.SELECT_BUSINESS_UNIT_NAME);
    busSelects.add(projectSpace.SELECT_CURRENT);
    busSelects.add(projectSpace.SELECT_OWNER);
    busSelects.add(projectSpace.SELECT_PROGRAM_NAME);

    MapList setProjects   = new MapList();
        try
        {
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            String objectId = (String) programMap.get("objectId");
            dashboardName = (String) programMap.get("dashboardName");
            if (objectId != null && !"null".equals(objectId))
            {

                DomainObject dmoProgram = DomainObject.newInstance(context,objectId);
                if (!dmoProgram.isKindOf(context,TYPE_PROGRAM))
                {
                projectSpace.setId(objectId);
                Map theProjectMap = (Map) projectSpace.getInfo(context, busSelects);
                setProjects.add( theProjectMap );
                }
                else
                {
                program.setId(objectId);
                setProjects = program.getProjects( context, busSelects, busWhere );
                }
            }
  // Take the Set name contained in dashboardName and retrieve the contents of
    // the set.
            else
            {
                if (dashboardName != null && !dashboardName.equals(""))
                {
                setProjects = getDashboardList(context, dashboardName, busSelects );
                }
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            return setProjects;
        }

}
 /**
     * This method is used to get all Sets with the given Name
     *
     * @param context
     *            the eMatrix <code>Context</code> object
     * @param dashboardName
     *            Name of the Dashboard Collection
     * @return matrix.db.Set containing Dashboard set
     * @throws Exception
     *             if the operation fails
     * @since PMC 10-7-X2
     */

public matrix.db.Set getSet( Context context, String dashboardName )
  {
    try
    {
      SetList l = matrix.db.Set.getSets( context, true );
      for ( int i = 0; i < l.size(); i++ )
      {
        matrix.db.Set s = ( matrix.db.Set )l.getElement( i );
        if ( s.getName().equals( dashboardName ) )
        {
            return s;
        }
      }
    }
    catch ( MatrixException me )
    {
      me.printStackTrace();
      return null;
    }
    return null;
  }

    /**
     * This method is used to get all Sets with the given Name
     *
     * @param context
     *            the eMatrix <code>Context</code> object
     * @param dashboardName
     *            Name of the Dashboard Collection
     * @param objectSelects
     *
     * @return MapList containing projects under Dashboard
     * @throws Exception
     *             if the operation fails
     * @since PMC X+2
     */
  public MapList getDashboardList( Context context, String dashboardName, StringList objectSelects )
      throws FrameworkException
  {
    MapList maplist = new MapList();
    matrix.db.Set s = getSet( context, ".dashboard-" + dashboardName );
    if ( s != null )
    {
      try
      {
        BusinessObjectWithSelectList bowsl = s.select( context, objectSelects );
        maplist = FrameworkUtil.toMapList(bowsl);
      }
      catch ( MatrixException me )
      {
        me.printStackTrace();
        return maplist;
      }
    }
    return maplist;
  }


//to get Project Metrics ---- Added for Configurable Component
 /**
     * This method is used to get Project Metrics of the project
     *
     * @param context
     *            the eMatrix <code>Context</code> object
     * @param args
     *            holds the following input arguments:
     * @return Vector containing current phase estimated end date of the  project
     * @throws Exception if the operation fails
     * @since PMC X+2
     */
 public StringList getProjectMetrics(Context context, String[] args)
      throws Exception
    {

        StringList result = new StringList();
        try
        {

        HashMap programMap = (HashMap)JPO.unpackArgs(args);
        MapList objList = (MapList)programMap.get("objectList");
        Vector columnVals = new Vector(objList.size());

        Iterator i = objList.iterator();
        while (i.hasNext())
        {
            StringBuffer output = new StringBuffer("");
            Map m = (Map) i.next();
            String objectId = (String)m.get("id");
            String strProjName = (String)m.get("name");
            output.append("<a href=\"JavaScript:showModalDialog('../programcentral/emxProgramCentralDashboardsMetricsCharts.jsp?objectId="+XSSUtil.encodeForURL(context,(String)objectId)+"&objectName=" +XSSUtil.encodeForURL(context, strProjName)+"&treeNodeKey=node.Projects&suiteKey=eServiceSuiteProgramCentral',875,550,true)\"><img src=\"../common/images/iconMetricMeasure.gif\" border=0></a>");
            if(!"".equals(output.toString())) {
                result.add(output.toString());
            }
        }
        }catch(Exception e){ throw (e);}
      return result;

}

    /**
     * This method is used to get current phase estimated end date of the
     * project
     *
     * @param context
     *            the eMatrix <code>Context</code> object
     * @param args
     *            holds the following input arguments:
     * @return Vector containing current phase estimated end date of the project
     * @throws Exception
     *             if the operation fails
     * @since PMC X+2
     */
   public Vector getCurrentPhaseDate(Context context, String[] args) throws Exception {

         Vector currentPhaseEstEndDate = new Vector();
         try{
             MapList projectDetails = getProjectDetails(context,args);
             Iterator prjIterator = projectDetails.iterator();
             while (prjIterator.hasNext()){
                Map projectDetailsMap = (HashMap) prjIterator.next();
                String strCurrentPhaseEstimatedEndDate = XSSUtil.encodeForHTML(context,(String) projectDetailsMap.get("CurrentPhaseDate"));
                if(strCurrentPhaseEstimatedEndDate!=null && strCurrentPhaseEstimatedEndDate.length()!=0)
                {
                currentPhaseEstEndDate.add(strCurrentPhaseEstimatedEndDate);
                }
            }
         }
         catch(Exception e){
            e.printStackTrace();
         }
         return currentPhaseEstEndDate;
   }




//to get Dashboards Project Assessment  ---- Added for Configurable Components
   /**
     * This method is used to get project assessment of the  project
     *
     * @param context
     *            the eMatrix <code>Context</code> object
     * @param args
     *            holds the following input arguments: objectList - MapList
     *            containing the objects list
     * @return List containing project assessment of the related project.
     * @throws Exception if the operation fails
     * @since PMC X+2
     */
   public java.util.List getDashBoardProjectAssessment(Context context, String[] args) throws Exception {

         java.util.List projectAssessment = new StringList();

         // Status colors;
         String red    = "Red";
         String green  = "Green";
         String yellow = "Yellow";
         String other  = "---";

         try{
            com.matrixone.apps.program.ProjectSpace projectSpace =
            (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);

            HashMap projectMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList) projectMap.get("objectList");
            Map paramList = (Map) projectMap.get("paramList");
            String suiteDirectory = (String)paramList.get("SuiteDirectory");

            String strPrinterFriendly = (String)paramList.get("reportFormat");
            boolean isPrinterFriendly = false;
            if(strPrinterFriendly != null){
                isPrinterFriendly = true;
            }

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            Iterator objectListIterator = objectList.iterator();
            while (objectListIterator.hasNext()){
                String displayAssessment = "";
                Map objectMap = (Map) objectListIterator.next();
                String objectId = (String) objectMap.get(projectSpace.SELECT_ID);
                projectSpace.setId(objectId);

                // assessment selectables

                  StringList AssessmentbusSelects = new StringList(2);
                  AssessmentbusSelects.add(com.matrixone.apps.program.Assessment.SELECT_ORIGINATED);
                  AssessmentbusSelects.add(com.matrixone.apps.program.Assessment.SELECT_ASSESSMENT_STATUS );
                  MapList assessmentList = new MapList();
                  assessmentList=com.matrixone.apps.program.Assessment.getAssessments(context,projectSpace,AssessmentbusSelects,null,null,null);
                 Map LatestAssessment=null;
                  String status = "";
                  String assessmentURL = "";

                  if (! assessmentList.isEmpty())
                  {

                   Iterator assessmentListItr = assessmentList.iterator();
                    String dtstr1="";
                    String dtstr2="";
                    while(assessmentListItr.hasNext())
                    {
                      Map current       = (Map) assessmentListItr.next();
                      if(LatestAssessment==null)
                      {
                        LatestAssessment = current;
                      }
                      else
                      {
                        dtstr1 = (String) LatestAssessment.get(com.matrixone.apps.program.Assessment.SELECT_ORIGINATED);
                        dtstr2 = (String) current.get(com.matrixone.apps.program.Assessment.SELECT_ORIGINATED);
                        java.util.Date max  = sdf.parse(dtstr1);
                        java.util.Date min1 = sdf.parse(dtstr2);
                        if(max.compareTo(min1)<0)
                        {
                          LatestAssessment = current;
                        }
                      }
                    }
                  }
                  if(LatestAssessment!=null)
                  {
                    //assessmentURL = "../programcentral/emxProgramCentralAssessmentSummaryFS.jsp?objectId=" + objectId+"&fromDashboard=true";
                    //assessmentURL = "../common/emxTable.jsp?program=emxAssessment:getAssessment&table=PMCAssessmentSummary&portaltable=PMCPortalAssessmentSummary&selection=multiple&sortColumnName=Name&sortDirection=ascending&toolbar=PMCAssessSummaryToolBar&header=emxProgramCentral.Common.AssessmentsPageHeading&HelpMarker=emxhelpassessmentsummary&Export=false&suiteKey=ProgramCentral&objectId=" + objectId+"&fromDashboard=true";
                    assessmentURL = UIMenu.getHRef(UIMenu.getCommand(context, "PMCAssessment"));
                    assessmentURL = assessmentURL.replace("${COMMON_DIR}","../common");
                    assessmentURL=assessmentURL+"&objectId=" + XSSUtil.encodeForURL(context,objectId) + "&fromDashboard=true&suiteKey=ProgramCentral";
                    assessmentURL = assessmentURL.replaceAll("&","&amp;");
                    status = (String)LatestAssessment.get(com.matrixone.apps.program.Assessment.SELECT_ASSESSMENT_STATUS);
                  }
               StringBuffer sbAssessment = new StringBuffer(100);
               sbAssessment.append("<a href=\"javascript:showModalDialog('");
               sbAssessment.append(assessmentURL);
               sbAssessment.append("')\">");

               if(!"".equals(status)){
                   if(status.equals(red)){
                       if(!isPrinterFriendly){
                           displayAssessment = sbAssessment.append("<img src=\"../common/images/iconStatusRed.gif\" border=\"0\" /></a>").toString();
                       }
                       else{
                           displayAssessment = "<img src=\"../common/images/iconStatusRed.gif\" border=\"0\" />";
                       }
                   }
                   else if(status.equals(green)){
                       if(!isPrinterFriendly){
                           displayAssessment = sbAssessment.append("<img src=\"../common/images/iconStatusGreen.gif\" border=\"0\" /></a>").toString();
                       }
                       else{
                           displayAssessment = "<img src=\"../common/images/iconStatusGreen.gif\" border=\"0\" />";
                       }
                   }
                   else if(status.equals(yellow)){
                       if(!isPrinterFriendly){
                           displayAssessment = sbAssessment.append("<img src=\"../common/images/iconStatusYellow.gif\" border=\"0\" /></a>").toString();
                       }
                       else{
                          displayAssessment = "<img src=\"../common/images/iconStatusYellow.gif\" border=\"0\" />";
                       }
                   }
                   else if(status.equals(other)){
                       if(!isPrinterFriendly){
                           displayAssessment = sbAssessment.append("---</a>").toString();
                       }
                       else{
                           displayAssessment = "---";
                       }
                   }
               }
               else if("".equals(status)){
                   displayAssessment = "";
               }
               projectAssessment.add(displayAssessment);
            }
         }
         catch(Exception e){
            e.printStackTrace();
         }
         return projectAssessment;
   }

    /**
     * This method gets all the active Tasks, Risks, and/or Issues
     * assigned to a specific user
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments as it is getting the params list from the Config UI table
     * @return Maplist of items and relationship ids
     * @throws Exception if the operation fails
     * @since Common 10-5-0-0
     */
   @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getPersonAssignments ( Context context , String[] args )
            throws Exception {

        HashMap paramMap = ( HashMap ) JPO.unpackArgs ( args );
        String objectId  = ( String ) paramMap.get ( "objectId" );
        String sPersonFrom  = ( String ) paramMap.get ( "txtPersonFrom" );
        String sPersonTo    = ( String ) paramMap.get ( "txtPersonTo" );
        String selTask  = (String) paramMap.get( "selTask" );
        String selIssue = (String) paramMap.get( "selIssue" );
        String selRisk  = (String) paramMap.get( "selRisk" );

        DomainObject doPerson = DomainObject.newInstance( context, sPersonFrom );
        DomainObject doProject = DomainObject.newInstance( context, objectId );

        // Get symbolic name for state name to check item against to see if it is available to reassign.
        String sStateComplete = PropertyUtil.getSchemaProperty( context, "policy" , PropertyUtil.getSchemaProperty( context, "policy_ProjectTask" ), "state_Complete" );

        StringList busSelects = new StringList( 1 );
        busSelects.add( "id" );

        StringList relSelects = new StringList( 3 );
        relSelects.add( "id[connection]" );
        relSelects.add( "to.id" );
        relSelects.add( "to.current" );

        MapList mlItems = new MapList();

        String relPattern = PropertyUtil.getSchemaProperty( context, "relationship_ProjectAccessKey" );
        String postTypePattern = PropertyUtil.getSchemaProperty( context, "type_Person" );

        MapList mlAssignedItems = new MapList();
        String sPersonId = "";
        HashMap hmTaskInfo = new HashMap();


        // Check to see if we need to retrieve WBS Task and/or Risks
        if ( ( selTask != null && selTask.equals( "true" ) ) || ( selRisk != null && selRisk.equals( "true" ) ) ) {

            // In order to determine related Risk & Task objects, we need to expand
            // from the project through the Project Access List and then to the related
            // risk or task to the person.  Once we get a list of all persons assigned,
            // we need to iterate through and check against the selected person.

            // The primary reason to do it this way is to limit the recursion level to two levels
            // deep instead of having to expand the entire project structure

            // Step 1 - Expand from Project Access List to related tasks and risks to get related persons
            String sPALID = doProject.getInfo ( context, "to[" + PropertyUtil.getSchemaProperty( context, "relationship_ProjectAccessList" ) + "].businessobject.id" );
            DomainObject doProjectAccessList = DomainObject.newInstance( context, sPALID );

            if ( selTask != null && selTask.equals( "true" ) ) relPattern += "," + PropertyUtil.getSchemaProperty( context, "relationship_AssignedTasks" );
            if ( selRisk != null && selRisk.equals( "true" ) ) relPattern += "," + PropertyUtil.getSchemaProperty( context, "relationship_AssignedRisk" );

            mlItems = doProjectAccessList.getRelatedObjects( context, relPattern, "*", true, true, 2, busSelects, relSelects, null, "to.locked==FALSE || to.locker==context.user", null, postTypePattern, null );

            // Step 2 - Iterate through list of persons to check against selected person

            for ( int i=0; i<mlItems.size(); i++ ) {
                Map mItem = (Map) mlItems.get( i );
                sPersonId = (String) mItem.get( "id" );
                if ( sPersonId != null && sPersonId.equals( sPersonFrom ) ) {

                    // Replace list of people ids with task ids
                    // and also add the ids of the person to replace
                    // it with
                    hmTaskInfo = new HashMap();
                    hmTaskInfo.put( "id[connection]", (String) mItem.get( "id[connection]" ) );
                    hmTaskInfo.put( "id", (String) mItem.get( "to.id" ) );

                    // Add custom entry to maplist that will be used to determine whether
                    // or not task can be selected
                    if ( sStateComplete.equals( (String) mItem.get( "to.current" ) ) ) {
                        hmTaskInfo.put( "AVAILABLE_TO_REASSIGN", "false" );
                    } else {
                        hmTaskInfo.put( "AVAILABLE_TO_REASSIGN", "true" );
                    }
                    mlAssignedItems.add( hmTaskInfo );
                }
            }
        }

        // Check to see if we need to retrieve WBS Task and/or Risks
        if ( selIssue != null && selIssue.equals( "true" ) ) {

            // Similar to tasks, we need to recurse down an extra level to the actual person
            // and then "back up" one level to get the task id
            relPattern = PropertyUtil.getSchemaProperty( context, "relationship_Issue" ) + "," + PropertyUtil.getSchemaProperty( context, "relationship_AssignedIssue" );
            mlItems = doProject.getRelatedObjects( context, relPattern, "*", true, true, 2, busSelects, relSelects, null, "to.locked==FALSE || to.locker==context.user", null, postTypePattern, null );

            for ( int i=0; i<mlItems.size(); i++ ) {
                Map mItem = (Map) mlItems.get( i );
                sPersonId = (String) mItem.get( "id" );
                if ( sPersonId != null && sPersonId.equals( sPersonFrom ) ) {

                    // Replace list of people ids with task ids
                    // and also add the ids of the person to replace
                    // it with
                    hmTaskInfo = new HashMap();
                    hmTaskInfo.put( "id[connection]", (String) mItem.get( "id[connection]" ) );
                    hmTaskInfo.put( "id", (String) mItem.get( "to.id" ) );

                    // Add custom entry to maplist that will be used to determine whether
                    // or not task can be selected
                    if ( sStateComplete.equals( (String) mItem.get( "to.current" ) ) ) {
                        hmTaskInfo.put( "AVAILABLE_TO_REASSIGN", "false" );
                    } else {
                        hmTaskInfo.put( "AVAILABLE_TO_REASSIGN", "true" );
                    }
                    mlAssignedItems.add( hmTaskInfo );
                }
            }
        }

        return mlAssignedItems;
    }

    /**
     * This method gets the "AVAILABLE_TO_REASSIGN" entry for each element in the MapList
     * that was generated from the getPersonAssignments() method.  This determines
     * whether to enable the checkbox or not
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @returns Vector of true/false values for each row.
     * @throws Exception if the operation fails
     */

    public Vector getTaskCheckboxes(Context context, String[] args)
        throws Exception {

        HashMap programMap = (HashMap)JPO.unpackArgs(args);
        MapList objList = (MapList) programMap.get( "objectList" );
        HashMap paramMap = (HashMap) programMap.get( "paramList" );

        Vector columnVals = new Vector(objList.size());
        String displayValue;

        Iterator i = objList.iterator();
        while (i.hasNext()) {
            Map m = (Map) i.next();

            // Get the custom added entries that were created in the getPersonAssignments method
            columnVals.addElement( (String)m.get( "AVAILABLE_TO_REASSIGN" ) );
        }

        return columnVals;

    }

    /**
     * Transfer Task Assignment - reassign tasks, issues, and/or risks
     * from one user to another
     *
     * @param context the user context object for the current session
     * @param args contains a MapList of changes with keys of:
     *        - "txtPersonFrom" id of currently assigned person
     *        - "txtPersonTo" id of person to transfer assignment to
     *        - "objectId" id of project
     *        - "selectedIds" string array of selected items to transfer
     *
     * @throws Exception if the operation fails
     * @returns nothing
     * @since AEF 10.5.0.0
     * @grade 0
     */

    public StringList transferTaskAssignments ( Context context , String args[] )
            throws Exception {
        Map map = ( Map ) JPO.unpackArgs ( args );
        String txtPersonFrom = ( String ) map.get ( "txtPersonFrom" );
        String txtPersonTo   = ( String ) map.get ( "txtPersonTo" );
        String objectId      = ( String ) map.get ( "objectId" );
        String[] selectedIds = ( String[] ) map.get ( "selectedIds" );
        String SELECT_CRITICAL_TASK = "attribute[" + PropertyUtil.getSchemaProperty(context, "attribute_CriticalTask") + "]";

        String invokeFrom 	= (String)map.get("invokeFrom"); //Added for OTD
        DomainObject doPersonFrom = DomainObject.newInstance( context, txtPersonFrom );
        String sPersonNameFrom    = doPersonFrom.getInfo( context, "name" );
        DomainObject doPersonTo   = DomainObject.newInstance( context, txtPersonTo );
        String sPersonNameTo      = doPersonTo.getInfo( context, "name" );

        String companyName = null;
        StringList mailToList = new StringList(1);
        DomainObject personObject = DomainObject.newInstance(context, txtPersonTo);
        String personName = (String) personObject.getName(context);
        mailToList.addElement(personName);

        StringList mailCcList = new StringList(1);
        StringList objectIdList = null;
        objectIdList = new StringList(1);
        String taskName = "";
        String taskEstStartDate = "";
        String taskEstFinishDate = "";
        String taskCriticality  = "";
        String sTempMailMessage = "";
        String languageStr = context.getSession().getLanguage();
        String sFinalMailMessage = EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Common.AssignTaskMessageTop", languageStr);
        sFinalMailMessage += "\n";
        String sProjectName = "";
        String sMailMessage = "";
        String mKey[] = {"TaskName", "TaskEstStartDate", "TaskEstFinishDate"};
        java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("MM/dd/yy");

        // Use an MQL session to reassign relationships because it's faster :)
        // Additionally, because we are simply using a modify connection command
        // we do not need to distinguish between tasks, issues, and risks
        MQLCommand _mql = new MQLCommand();
        _mql.open(context);

        int selectedIdsLength = selectedIds.length;
        String[] selectedItemIds = new String[selectedIdsLength];
        for ( int i=0; i < selectedIdsLength; i++ ) {
            selectedItemIds[i] = selectedIds[i].substring( selectedIds[i].indexOf( "|" ) + 1 );
        }


        StringList slErrors = new StringList();

        //String relId = "";
        String objId = "";
        String relName = "";
        String mainObjType = "";

        StringList objectIds = new StringList();
        
        boolean isError = false;

        boolean needTaskNotification = false;

        if ( selectedIds != null && selectedIdsLength >0) {

        	for ( int i=0; i < selectedIdsLength; i++ ) {

                // It is assumed the selected ids were passed in from a configurable
                // table using a format of "OBJECTID | RELID"
        		//relId =  selectedIds[i].substring( 0, selectedIds[i].indexOf( "|" ) );
                objId = selectedIds[i].substring( selectedIds[i].indexOf( "|" ) + 1 );

        		objectIds.add(objId);

        	}

        	int size = objectIds.size();
        	String[] strObjectIds = new String[size];

        	objectIds.toArray(strObjectIds);

                StringList sList = new StringList(3);
                sList.add(DomainConstants.SELECT_NAME);
        	sList.add(DomainConstants.SELECT_ID);
                sList.add(SELECT_TASK_ESTIMATED_START_DATE);
                sList.add(SELECT_TASK_ESTIMATED_FINISH_DATE);
                sList.add(SELECT_CRITICAL_TASK);
                sList.add("to[" + RELATIONSHIP_SUBTASK + "].from.name");
        	sList.add(ProgramCentralConstants.SELECT_KINDOF_TASK);
        	sList.add(ProgramCentralConstants.SELECT_IS_PHASE);
        	sList.add(ProgramCentralConstants.SELECT_IS_GATE);
        	sList.add(ProgramCentralConstants.SELECT_IS_MILESTONE);
        	sList.add(ProgramCentralConstants.SELECT_IS_RISK);
        	sList.add(ProgramCentralConstants.SELECT_IS_ISSUE);
                sList.add(DomainConstants.SELECT_TYPE);


        	MapList objectInfoInfoList = new MapList();
        	BusinessObjectWithSelectList objectWithSelectList = null;
        	if("TestCase".equalsIgnoreCase(invokeFrom)) { ////Added for OTD
        		objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, strObjectIds, sList,true);
		}else {
        		objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, strObjectIds, sList);	
        	}

        	for (BusinessObjectWithSelectItr objectWithSelectItr = new BusinessObjectWithSelectItr(objectWithSelectList); objectWithSelectItr.next();) {
        		BusinessObjectWithSelect objectWithSelect = objectWithSelectItr.obj();

        		Map mapTask = new HashMap();
        		for (Iterator itrSelectables = sList.iterator(); itrSelectables.hasNext();) {
        			String strSelectable = (String)itrSelectables.next();
        			mapTask.put(strSelectable, objectWithSelect.getSelectData(strSelectable));
        		}

        		objectInfoInfoList.add(mapTask);
        	}
        	for ( int i=0, objectInfoInfoListSize = objectInfoInfoList.size(); i < objectInfoInfoListSize; i++ ) {

        		Map taskMap = (Map) objectInfoInfoList.get(i);
                taskName = (String)taskMap.get(DomainConstants.SELECT_NAME);
                sProjectName  = (String)taskMap.get("to[" + RELATIONSHIP_SUBTASK + "].from.name");
                mainObjType = (String)taskMap.get(DomainConstants.SELECT_TYPE);
        		objId = (String)taskMap.get(DomainConstants.SELECT_ID);
        		
        		boolean isTask = "TRUE".equalsIgnoreCase((String)taskMap.get(ProgramCentralConstants.SELECT_KINDOF_TASK)) ||
        				"TRUE".equalsIgnoreCase((String)taskMap.get(ProgramCentralConstants.SELECT_IS_PHASE)) ||
        				"TRUE".equalsIgnoreCase((String)taskMap.get(ProgramCentralConstants.SELECT_IS_GATE)) ||
        				"TRUE".equalsIgnoreCase((String)taskMap.get(ProgramCentralConstants.SELECT_IS_MILESTONE));

                // only do mail for Tasks
        		if(isTask){
        			needTaskNotification = true;
                   Date startD = eMatrixDateFormat.getJavaDate((String)taskMap.get(SELECT_TASK_ESTIMATED_START_DATE));
                   taskEstStartDate = date.format(startD);
                 startD = eMatrixDateFormat.getJavaDate((String)taskMap.get(SELECT_TASK_ESTIMATED_FINISH_DATE));
                   taskEstFinishDate = date.format(startD);

                   taskCriticality  = (String)taskMap.get(SELECT_CRITICAL_TASK);
                   if ("TRUE".equals(taskCriticality))
                   {
                      sMailMessage = "emxProgramCentral.Common.AssignCriticalTaskMessage";
                   }
                   else
                   {
                      sMailMessage = "emxProgramCentral.Common.AssignTaskMessage";
                   }

                   String mValue[] = {taskName, taskEstStartDate, taskEstFinishDate};
                   sTempMailMessage  = emxProgramCentralUtilClass.getMessage(
                                   context, sMailMessage, mKey, mValue, companyName);
                   sFinalMailMessage += "  " + sTempMailMessage + "\n";
                }


                objectIdList.addElement(objId);


                
       		if(isTask){
                    relName = DomainConstants.RELATIONSHIP_ASSIGNED_TASKS;
                }
        		else if("TRUE".equalsIgnoreCase((String)taskMap.get(ProgramCentralConstants.SELECT_IS_RISK))){
                    relName = Risk.RELATIONSHIP_ASSIGNED_RISK;
                }
        		else if("TRUE".equalsIgnoreCase((String)taskMap.get(ProgramCentralConstants.SELECT_IS_ISSUE))){
                    relName = ProgramCentralConstants.RELATIONSHIP_ASSIGNED_ISSUE;
                }
                //Added/Modified:6-Apr-09:wqy:R207:PRG Bug :370792
                try
                {
                	ContextUtil.startTransaction(context,true);
                    DomainObject dObject = new DomainObject(objId);
                    String strSelect = "to["+relName+"]."+DomainConstants.SELECT_FROM_ID+"";
                    StringList slPersonIds = dObject.getInfoList(context, strSelect);
                    //Added to check whether person is already connected, if yes then don't connect and if no then connect
                    if(!slPersonIds.contains(txtPersonTo)) {
                        MqlUtil.mqlCommand(context, "connect businessobject $1 relationship $2 from $3",true,objId,relName,txtPersonTo);
                    }
                    MqlUtil.mqlCommand(context, "disconnect businessobject $1 relationship $2 from $3",true,objId,relName,txtPersonFrom);
                    ContextUtil.commitTransaction(context);
                }
                catch(MatrixException e)
                {e.printStackTrace();
                ContextUtil.abortTransaction(context);
                    isError = true;
                    slErrors.add( mainObjType + " " + taskName );
                }
            }//end of for
            if(isError)
            {
                String strTxtMessage = EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                        "emxProgramCentral.Message.CouldNotReassignTasks", languageStr);

                for ( int i=0; i<slErrors.size(); i++ )
                {
                    strTxtMessage += "\n"+ slErrors.get(i);
                }
                emxContextUtilBase_mxJPO.mqlNotice(context, strTxtMessage);
            }
            //End:R207:PRG Bug :370792
        }

        if(needTaskNotification){
           // Sends mail notification for tasks only
           //get the mail subject
           String mSubjectKey[] = {"ProjectName"};
           String mSubjectValue[] = {sProjectName};
           String sMailSubject = "emxProgramCentral.Common.TransferTaskSubject";
           sMailSubject  = emxProgramCentralUtilClass.getMessage(
                               context, sMailSubject, mSubjectKey, mSubjectValue, companyName);

           //get the mail message
           MailUtil.setAgentName(context, context.getUser());
           MailUtil.sendMessage(context, mailToList, mailCcList, null,
                                sMailSubject, sFinalMailMessage, objectIdList);
        }
        _mql.close(context);

        return slErrors;
    }

    /**
     * To get Resource Requests of this Project Space with asked details

     * context Matrix context object
     * StringList slBusSelects Resource Request Selectables
     * StringList slRelSelects Relationship 'Resource Plan' selectables
     * String strBusWhere Resource Request where Expression
     * String strRelWhere Relationship 'Resource Plan' Expression
     * returns Maplist containing info based on selectables
     * throws Matrix Exception if operation fails
     */
    public MapList getResourceRequests(Context context,String[] args) throws Exception
    {
        Map mapBusInfo =(Map) JPO.unpackArgs(args);

        StringList slRelSelects = new StringList();
        StringList slBusSelects  = new StringList();
        String strBusWhere = null;
        String strRelWhere = null;

        if(mapBusInfo.containsKey("BusSelects"))
        {
            slBusSelects = (StringList) mapBusInfo.get("BusSelects");
        }
        if(mapBusInfo.containsKey("RelSelects"))
        {
            slRelSelects = (StringList) mapBusInfo.get("RelSelects");
        }
        if(mapBusInfo.containsKey("BusWhere"))
        {
            strBusWhere = (String) mapBusInfo.get("BusWhere");
        }
        if(mapBusInfo.containsKey("RelWhere"))
        {
            strRelWhere = (String) mapBusInfo.get("RelWhere");
        }

        MapList mlResourcePlanInfo = this.getRelatedObjects(context,
                                                                                    RELATIONSHIP_RESOURCE_PLAN, //pattern to match relationships
                                                                                    TYPE_RESOURCE_REQUEST, //pattern to match types
                                                                                    slBusSelects, //the eMatrix StringList object that holds the list of select statement pertaining to Business Obejcts.
                                                                                    slRelSelects, //the eMatrix StringList object that holds the list of select statement pertaining to Relationships.
                                                                                    false, //get To relationships
                                                                                    true, //get From relationships
                                                                                    (short)1, //the number of levels to expand, 0 equals expand all.
                                                                                    strBusWhere, //where clause to apply to objects, can be empty ""
                                                                                    strRelWhere,
                                                                                    0); //where clause to apply to relationship, can be empty ""


        return mlResourcePlanInfo;
    }

    //Added:23-Apr-2010:s4e:R210 PRG:2011x
    /**
     *
     * @param context
     *            The matrix context object
     * @param returns
     *            StringList containing Programs  related to context Host Company which are in "Active" state.
     * @param args
     *            The arguments, it contains programMap
     * @throws Exception
     *             if operation fails
     */
    //This method is used for including object id's for autonomy search page for selecting Programs while
    //creating Project Space/Project Concept
    @com.matrixone.apps.framework.ui.IncludeOIDProgramCallable
    public StringList getIncludeOIDforProgramSearch(Context context, String[] args)throws Exception
    {
        StringList slProgramIds = new StringList();
        try
        {
            Map programMap = (Map) JPO.unpackArgs(args);
            String strCompanyId = (String)programMap.get("companyId");
            String strProjectId = (String)programMap.get("projectId");
            String strProgramId = "";
            StringList slConnectedProgramIdList = new StringList();
            if(null != strProjectId && !"".equals(strProjectId) && !"null".equals(strProjectId))
            {
                DomainObject projectDobj = DomainObject.newInstance(context, strProjectId);
                slConnectedProgramIdList = projectDobj.getInfoList(context,"to[" +RELATIONSHIP_PROGRAM_PROJECT+"].from.id");
            }
            DomainObject comapanyDobj = DomainObject.newInstance(context,strCompanyId);
            String strRelationshipPattern = RELATIONSHIP_COMPANY_PROGRAM;
            String strTypePattern = TYPE_PROGRAM;
            StringList slBusSelect = new StringList();
            slBusSelect.add(SELECT_ID);
            short recurseToLevel = 1;
            boolean getFrom = true;
            boolean getTo = false;
            String strBusWhere = "";
            String strRelWhere = "";
            strBusWhere=SELECT_CURRENT+"=="+"Active";
            MapList mlProgramList = comapanyDobj.getRelatedObjects(context,
                    strRelationshipPattern, //pattern to match relationships
                    strTypePattern, //pattern to match types
                    slBusSelect, //the eMatrix StringList object that holds the list of select statement pertaining to Business Objects.
                    null, //the eMatrix StringList object that holds the list of select statement pertaining to Relationships.
                    getTo, //get To relationships
                    getFrom, //get From relationships
                    recurseToLevel, //the number of levels to expand, 0 equals expand all.
                    strBusWhere, //where clause to apply to objects, can be empty ""
                    strRelWhere); //where clause to apply to relationship, can be empty ""

            Map mapProgramIds = null;
            Iterator itrmlProgramList = mlProgramList.iterator();
            while (itrmlProgramList.hasNext())
            {
                mapProgramIds = (Map) itrmlProgramList.next();
                strProgramId = (String)mapProgramIds.get(SELECT_ID);
                if(!slConnectedProgramIdList.contains(strProgramId)){
                	slProgramIds.add((String)mapProgramIds.get(SELECT_ID));
                }

            }

            return slProgramIds;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    //End:23-Apr-2010:s4e:R210 PRG:2011x

    //Added:5-May-2010:s4e:R210 PRG:2011x
    /**
     *
     * @param context
     *            The matrix context object
     * @param returns
     *            StringList containing Person with role ProjectLead and ExternalProjectLead
     * @param args
     *            The arguments, it contains programMap
     * @throws Exception
     *             if operation fails
     */
    //This method is used for including person with role "ProjectLead" and "ExternalProjectLead"
    @com.matrixone.apps.framework.ui.IncludeOIDProgramCallable
    public StringList getIncludeOIDforPersonSearch(Context context, String[] args)throws Exception
    {	
		return new StringList();
		/*
        try
        {
            StringList slPersonProjectLeadRoleNames = new StringList();
            StringList slPersonExternalProjectLeadRoleNames = new StringList();
            StringList slPersonIds = new StringList();
            String strPersonName ="";
            String strPersonId ="";
            String personOrg[] = null;
            int nLength;
            String strPersonProjectLeadRole = PropertyUtil.getSchemaProperty(context,"role_ProjectLead");
            String strPersonExternalProjectLeadRole = PropertyUtil.getSchemaProperty(context,"role_ExternalProjectLead");
            slPersonProjectLeadRoleNames = PersonUtil.getPersonFromRole(context, strPersonProjectLeadRole);
            slPersonExternalProjectLeadRoleNames = PersonUtil.getPersonFromRole(context, strPersonExternalProjectLeadRole);
            for(int nCount=0;nCount<slPersonProjectLeadRoleNames.size();nCount++)
            {
                strPersonName=(String)slPersonProjectLeadRoleNames.get(nCount);
                //This method "PersonUtil.getMemberOrganizations()" is used to check if the person is associated with any organisation/company
                //If the person gets deleted then the object remains as an Admin object which we can see in "Business Modeler",
                //So it will give exception for method PersonUtil.getPersonObjectID()
                personOrg =PersonUtil.getMemberOrganizations(context,strPersonName);
                nLength = personOrg.length;
                if(nLength!=0)
                {
                    strPersonId=PersonUtil.getPersonObjectID(context, strPersonName);
                }
                slPersonIds.add(strPersonId);
            }
            for(int nCount=0;nCount<slPersonExternalProjectLeadRoleNames.size();nCount++)
            {
                strPersonName=(String)slPersonExternalProjectLeadRoleNames.get(nCount);
                personOrg =PersonUtil.getMemberOrganizations(context,strPersonName);
                nLength = personOrg.length;
                if(nLength!=0)
                {
                    strPersonId=PersonUtil.getPersonObjectID(context, strPersonName);
                }
                slPersonIds.add(strPersonId);
            }
            return slPersonIds;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }*/
    }
    //End:5-May-2010:s4e:R210 PRG:2011x
    /**
     * Returns the Policies governing Project Space.
     * This method is added as a Range Function to return Project Policies
     * in Project edit page. It is done to avoid 'Project Space Hold Cancel'
     * policy to be listed in Project Properties page.
     *
     * @param context the user context object for the current session
     * @param args contains the parameter map.
     *
     * @throws Exception if the operation fails
     * @returns nothing
     * @since AEF V6R2011x
     * @grade 0
     */
   //Added:nr2:PRG:R210:03-06-:2010:For Project Gate Highlight
    @com.matrixone.apps.framework.ui.ProgramCallable
    public HashMap getProjectPolicies(Context context,String[] args)
    throws MatrixException{
            HashMap returnMap = new HashMap();
            try{
                    HashMap programMap = (HashMap)JPO.unpackArgs(args);
                    HashMap paramMap = (HashMap) programMap.get("paramMap");

                    String objectId = (String)paramMap.get("objectId");
                    if(null==objectId || "".equals(objectId)){
                        throw new Exception();
                    }
                    String languageStr = context.getSession().getLanguage();
                    String policyName = "";
                    String i18npolicyName = "";
                    StringList fieldRangeValue = new StringList();
                    StringList fieldDisplayRangeValue = new StringList();
                    DomainObject projObj = DomainObject.newInstance(context, objectId);
                    //Current Policy
                    String projectPolicy = projObj.getInfo(context,SELECT_POLICY);

                    MapList policyList = projObj.getPolicies(context);

                    //if current policy is Project Space Hold Cancel, show both policies
                    //else remove this policy from Policy Map

                    if(POLICY_PROJECT_SPACE.equals(projectPolicy)){
                    for(int i=0;((policyList != null && policyList.size() > 0) && i < policyList.size());i++){
                        Map policyMap = (Map) policyList.get(i);
                        policyName = (String) policyMap.get(SELECT_NAME);
                            if(POLICY_PROJECT_SPACE.equals(policyName)){
                            fieldRangeValue.add(policyName);
                            i18npolicyName = i18nNow.getAdminI18NString("Policy", policyName, languageStr);
                            fieldDisplayRangeValue.add(i18npolicyName);
                        }
                    }
                    } //End If
                    else if(ProgramCentralConstants.POLICY_PROJECT_SPACE_HOLD_CANCEL.equals(projectPolicy)){
                        for(int i=0;((policyList != null && policyList.size() > 0) && i < policyList.size());i++){
                            Map policyMap = (Map) policyList.get(i);
                            policyName = (String) policyMap.get(SELECT_NAME);
                            if(ProgramCentralConstants.POLICY_PROJECT_SPACE_HOLD_CANCEL.equals(policyName)){
                                fieldRangeValue.add(policyName);
                                i18npolicyName = i18nNow.getAdminI18NString("Policy", policyName, languageStr);
                                fieldDisplayRangeValue.add(i18npolicyName);
                            }
                        }
                    }// End else if
                    else{
                        //Do Nothing. This condition is added if future policies are introduced
                        //Do the processing based on the requirement
                    }

                    returnMap.put("field_choices",fieldRangeValue);
                    returnMap.put("field_display_choices",fieldDisplayRangeValue);

                    return returnMap;
            }
            catch (Exception fxe){
                    throw new MatrixException(fxe);
            }
    }
//End:nr2:PRG:R210:03-06-:2010:For Project Gate Highlight


    /**
     *  This method returns vector containing name of vaults in which project is present.
     *  @param context the ENOVIA <code>Context</code> object
     * @param returns vector containing name of the vault in which project is present.
     * @param args The arguments, it contains programMap
     * @throws Exception if operation fails
     */
    public Vector getProjectVaults(Context context,String[] args) throws MatrixException {
        Vector vProjectVaults = new Vector();
        try {
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            MapList objectList = (MapList)programMap.get("objectList");
            String[] str = new String[objectList.size()];
            StringList slVault = new StringList(DomainConstants.SELECT_VAULT);
            String strVault = DomainConstants.EMPTY_STRING;
            Map mpProjectSpace;
            Map mpVault;
            for (int i = 0; i < objectList.size(); i++) {
                mpProjectSpace = (Map)objectList.get(i);
                str[i] = (String)mpProjectSpace.get(DomainConstants.SELECT_ID);
            }
            MapList mpList =  DomainObject.getInfo(context, str, slVault);
            for (int i = 0;i< mpList.size();i++)
            {
                mpVault = (Map)mpList.get(i);
                strVault = (String)mpVault.get(DomainConstants.SELECT_VAULT);
                vProjectVaults.add(strVault);
            }
        }catch(Exception e)
        {
            throw new MatrixException(e);
        }
        return vProjectVaults;
    }

    /**
     * Returns a Map of range values of Currency Attribute.
     * @param context the ENOVIA <code>Context</code> object.
     * @return a Map of Currency attribute range values.
     * @throws MatrixException if operation fails.
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public Map getCurrencyAttributeRange(Context context, String args[]) throws MatrixException{
        StringList slCurrencyRange = Currency.getCurrencyAttributeRange(context);
        slCurrencyRange.sort();

    	StringList newCurrencyRangeList = new StringList();

    	String currencyDefault = PersonUtil.getCurrency(context);
    	if(currencyDefault.contains("Entered") || currencyDefault.contains("Unassigned") || "".equals(currencyDefault)){
    		newCurrencyRangeList = slCurrencyRange;
    	} else{
    		slCurrencyRange.remove(currencyDefault);
    		newCurrencyRangeList.add(0, currencyDefault);
    		newCurrencyRangeList.addAll(slCurrencyRange);
    	}

        String strLanguage = context.getSession().getLanguage();
    	StringList slCurrencyRangeTranslated = i18nNow.getAttrRangeI18NStringList(ProgramCentralConstants.ATTRIBUTE_CURRENCY, newCurrencyRangeList, strLanguage);
    	
        Map mCurrencyRange = new HashMap();
    	mCurrencyRange.put("field_choices", newCurrencyRangeList);
        mCurrencyRange.put("field_display_choices", slCurrencyRangeTranslated);


        return mCurrencyRange ;
    }


    /**
     * Updates the base currency of Project and related objects.
     * This method is executed as background activity.
     * @param context the ENOVIA <code>Context</code> object
     * @param args request arguments
     * @throws MatrixException if operation fails.
     */
    public void updateBaseCurrencyInBackground(Context context, String args[]) throws MatrixException{
		/*
            Map paramMap = new HashMap();
        String projectId = args[0];
            String newCurrency = args[1];
        String oldCurrency = args[2];
        String globVar =  ProgramCentralConstants.BJ_UPDATE_BASE_CUREENCY + "|" + ProgramCentralConstants.TYPE_PROJECT_SPACE  + "|" + projectId ;
        try {
            //Return if there is no new currency entered.
            if(ProgramCentralUtil.isNullString(newCurrency))
                return;
            DomainObject project = DomainObject.newInstance(context, projectId);
            String relPattern = ProgramCentralConstants.RELATIONSHIP_PROJECT_FINANCIAL_ITEM;
            String typePattern = ProgramCentralConstants.TYPE_FINANCIAL_ITEM;
            boolean isFCAInstalled = com.matrixone.apps.domain.util.FrameworkUtil.isSuiteRegistered(context,"appVersionForecastAnalysis",false,null,null);
            if(isFCAInstalled){
                relPattern += "," + ProgramCentralConstants.RELATIONSHIP_GOVERNING_PROJECT;
                typePattern += "," + ProgramCentralConstants.TYPE_PRODUCTS;
            }
            StringList slBusSelects = new StringList();
            slBusSelects.add(SELECT_ID);
            StringList slRelSelects = new StringList();
            MapList mlProjectConnectedObjects = project.getRelatedObjects(context,
                    relPattern, //pattern to match relationships
                    typePattern, //pattern to match types
                    slBusSelects, //the eMatrix StringList object that holds the list of select statement pertaining to Business Obejcts.
                    slRelSelects, //the eMatrix StringList object that holds the list of select statement pertaining to Relationships.
                    true, //get To relationships
                    true, //get From relationships
                    (short)1, //the number of levels to expand, 0 equals expand all.
                    "", //where clause to apply to objects, can be empty ""
                    "",
                    0); //where clause to apply to relationship, can be empty ""

            StringList slResReqId = new StringList();
            boolean hasResourceRequest = false;
            ContextUtil.startTransaction(context, true);
            //Update project's base currency
            project.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_CURRENCY, newCurrency);
            for (Iterator iterator = mlProjectConnectedObjects.iterator(); iterator.hasNext();) {
                Map objectMap = (Map) iterator.next();
                String objectId = (String)objectMap.get(SELECT_ID);
                DomainObject object = DomainObject.newInstance(context,objectId);

                //Update base currency in Financial objects of project.
                if(object.isKindOf(context, ProgramCentralConstants.TYPE_FINANCIAL_ITEM)){
                    FinancialItem financialItem = new FinancialItem(objectId);
                    financialItem.updateBaseCurrency(context, oldCurrency, newCurrency);
                }
                //Update base currency in Product Forecast
                else if(object.isKindOf(context, ProgramCentralConstants.TYPE_PRODUCTS) && isFCAInstalled){
                    paramMap.put("oldCurrency", oldCurrency);
                    paramMap.put("newCurrency", newCurrency);
                    paramMap.put("objectId", objectId);
                    JPO.invoke(context, "emxForecastManagement", null, "updateBaseCurrency", JPO.packArgs(paramMap), HashMap.class);
                }
                //Update base currency in Resource Standard cost.
                else if(object.isKindOf(context, ProgramCentralConstants.TYPE_RESOURCE_REQUEST)){
                    slResReqId.add(objectId);
                    hasResourceRequest = true;
            }
            }
            if(hasResourceRequest){
                String[] resReqIds = new String[slResReqId.size()];
                slResReqId.toArray(resReqIds);
                slBusSelects.clear();
                slBusSelects.add(ProgramCentralConstants.SELECT_ID);
                slBusSelects.add(ProgramCentralConstants.SELECT_ATTRIBUTE_STANDARD_COST);
                MapList resReqInfo = DomainObject.getInfo(context, resReqIds, slBusSelects);
                for (Iterator iterator = resReqInfo.iterator(); iterator.hasNext();) {
                    Map resReqMap = (Map) iterator.next();
                    String resReqId = (String)resReqMap.get(ProgramCentralConstants.SELECT_ID);
                    String stdCost = (String)resReqMap.get(ProgramCentralConstants.SELECT_ATTRIBUTE_STANDARD_COST);
                    ResourceRequest request = new ResourceRequest(resReqId);
                    request.updateStandardCostCurrency(context, stdCost, oldCurrency, newCurrency);
                }
            }
            updateBaseCurrencyExtention(context, projectId, oldCurrency, newCurrency);
			
        }catch (Exception e) {
            if(ContextUtil.isTransactionActive(context))
                ContextUtil.abortTransaction(context);
            throw new MatrixException(e);
        }finally{
            if(ContextUtil.isTransactionActive(context)){
                ContextUtil.commitTransaction(context);
                NotificationUtil.sendBaseCurrencyChangesNotification(context, projectId, oldCurrency, newCurrency);
        }
    }*/
    }

    /**
     * An extension point to extend the functionality to update the base currency of the
     * customized financial objects connected to the project.
     * This method is executed as background activity.
     * @param context the ENOVIA <code>Context</code> object
     * @param projectId the project string id
     * @param oldCurrency project's old base currency
     * @param newCurrency project's new base currency
     * @throws MatrixException if operation fails.
     */
    public void updateBaseCurrencyExtention(Context context, String projectId, String oldCurrency, String newCurrency) throws MatrixException{
        //Customer implementaion
    }

    /**
     * Updates the base currency of Project and related objects.
     * @param context the ENOVIA <code>Context</code> object
     * @param args request arguments
     * @throws MatrixException if operation fails.
     */
    public void updateBaseCurrency(Context context, String args[]) throws MatrixException{
		/*
        try {
            Map programMap = (Map)JPO.unpackArgs(args);
            Map paramMap = (HashMap) programMap.get("paramMap");
            String newCurrency = (String) paramMap.get("New Value");
            String oldCurrency = (String) paramMap.get("Old value");
            String projectId = (String)paramMap.get("objectId");

            DomainObject project = DomainObject.newInstance(context,projectId);
            String projectName = project.getInfo(context,SELECT_NAME);
            String title =  ProgramCentralConstants.BJ_UPDATE_BASE_CUREENCY + "|" + ProgramCentralConstants.TYPE_PROJECT_SPACE  + "|" + projectId ;
            String[] params = {projectId, newCurrency, oldCurrency};
            Job job = new Job("emxProjectSpace", "updateBaseCurrencyInBackground", params);
            job.setTitle(title);
            job.createAndSubmit(context);
        }catch(Exception e){
            throw new MatrixException(e);
        }
		*/
    }

    /**
     * Checks if Edit Project Properties Command is available.
     * @param context the ENOVIA <code>Context</code> object.
     * @param args request arguments
     * @return true if Edit Properties Command is available
     * @throws MatrixException if operation fails.
     */
    public boolean hasAccessToEditPropertiesCommand(Context context, String[] args)
    throws MatrixException{
        try{
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            String objectId = (String) programMap.get("objectId");
            ProjectSpace project = (ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE, "PROGRAM");
            project.setId(objectId);
            String SELECT_CURRENT_ACCESS_MODIFY = SELECT_CURRENT + ".access[modify]";
            StringList slBusSelect = new StringList();
            slBusSelect.add(SELECT_CURRENT);
            slBusSelect.add(SELECT_CURRENT_ACCESS_MODIFY);
            Map projectInfo = project.getInfo(context, slBusSelect);
            String current = (String) projectInfo.get(SELECT_CURRENT);
            String currentAccessModify = (String) projectInfo.get(SELECT_CURRENT_ACCESS_MODIFY);
            boolean isBackgroundTaskActive = ProgramCentralUtil.isBackgroundTaskActive(context, objectId, ProgramCentralConstants.BJ_UPDATE_BASE_CUREENCY);
            if("true".equalsIgnoreCase(currentAccessModify) && !STATE_PROJECT_COMPLETE.equals(current) && !isBackgroundTaskActive)
                return true;
            return false;
        }catch(Exception e){
            throw new MatrixException(e);
        }
    }

    /**
     * This method returns true when "PMCProjectSpaceMyDesk" this table is invoked from My Enovia-->Projects otherwise false.
     * @param context the user context object for the current session
     * @param args contains the parameter map.
     * @throws Exception if the operation fails
     */
    public boolean isCalledFromProjects(Context context, String args[]) throws MatrixException
    {
        boolean blAccess = true;
        try {
            HashMap inputMap = (HashMap)JPO.unpackArgs(args);
            String isFromProject = (String)inputMap.get("isFromProject");
            if(null!=isFromProject && "false".equals(isFromProject)) {
                blAccess = false;
            }
        }catch (Exception e) {
            throw new MatrixException(e);
        }
        return blAccess;
    }

    /**
     * This method returns maplist containing list of subprojects from a project for which user is a member of.
     * It displays all subprojects in one level irrespective of subproject's actual level.
     * @param context the ENOVIA <code>Context</code> object
     * @param args The arguments, it contains programMap
     * @param returns maplist containing subprojects list from the project
     * @throws Exception if operation fails
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getSubProjectsList(Context context,String[] args) throws MatrixException
    {
        MapList mlAllList = new MapList();
        MapList mlReturnSubProjectList = new MapList();
        try {
            Map programMap = (Map)JPO.unpackArgs(args);

            String strLevel = (String) programMap.get("level");
            StringTokenizer stLevel = new StringTokenizer(strLevel,",");

            if(stLevel.countTokens() == 2)//IR-196212V6R2014
            {
                String strObjectId = (String) programMap.get("objectId");
                DomainObject dmoObject = DomainObject.newInstance(context, strObjectId);

                StringList slProjects = ProgramCentralUtil.getSubTypesList(context, ProgramCentralConstants.TYPE_PROJECT_SPACE);
                slProjects.addAll(ProgramCentralUtil.getSubTypesList(context, ProgramCentralConstants.TYPE_PROJECT_CONCEPT));

                StringList objectSelects = new StringList();
                objectSelects.add(SELECT_ID);
                objectSelects.add(SELECT_TYPE);

                mlAllList = dmoObject.getRelatedObjects(context,
                        DomainConstants.RELATIONSHIP_SUBTASK,
                        DomainObject.QUERY_WILDCARD,
                        objectSelects, //the eMatrix StringList object that holds the list of select statement pertaining to Business Obejcts.
                        null, //the eMatrix StringList object that holds the list of select statement pertaining to Relationships.
                        false, //get To relationships
                        true, //get From relationships
                        (short)0, //the number of levels to expand, 0 equals expand all.
                        DomainObject.EMPTY_STRING, //where clause to apply to objects, can be empty ""
                        DomainObject.EMPTY_STRING,//where clause to apply to relationship, can be empty ""
                        0);

                for (int i = 0; i < mlAllList.size(); i++) {
                    Map subProjectMap = (Map) mlAllList.get(i);
                    if(null!=subProjectMap.get(SELECT_TYPE) && slProjects.contains(subProjectMap.get(SELECT_TYPE))) {
                            subProjectMap.put("disableSelection", "true");
                            subProjectMap.remove("level");//IR-196186V6R2014
                            mlReturnSubProjectList.add(subProjectMap);
                        }
                    }
                }
            return mlReturnSubProjectList;
        }catch(Exception e)
        {
            throw new MatrixException(e);
        }
    }
    /**
     * This method returns true if there is Secondary Vault otherwise returns false.
     *
     * @param context
     *       context object which is used while fetching data related application.
     * @param args
     *       Holds input argument.
     * @return
     *        true if there is secondary vault
     * @throws MatrixException
     *         Exception can be thrown in case of method fails to execute.
     */
    public boolean hasSecondaryVaultInProject(Context context, String[] args)
    throws MatrixException
    {
        try {
            return ProgramCentralUtil.hasSecondaryVault(context,args);

        } catch (Exception e) {

            throw new MatrixException();
        }
    }

    /**
     * Exclude Related projects which are already connected to Project and which are in Archiev state
     * @param context the eMatrix <code>Context</code> object
     * @param args
     * @return StringList
     * @throws MatrixException
     */

    @com.matrixone.apps.framework.ui.ExcludeOIDProgramCallable
    public StringList excludeRelatedProjectsforAddExisting(Context context, String[] args)throws  MatrixException {
        
    	StringList excludeProjectIdList = new StringList();
    	StringList relatedProjectSelectableList = new StringList(1);
    	relatedProjectSelectableList.add(SELECT_ID);
        
    	final String SELECT_MEMBER_ID = "from["+DomainRelationship.RELATIONSHIP_MEMBER+"].to.id";
    	String typePattern = TYPE_PROJECT_SPACE + "," + TYPE_PROJECT_CONCEPT;
    	StringList busSelects = new StringList();
    	busSelects.add(SELECT_ID);
    	busSelects.add(SELECT_CURRENT);
    	busSelects.add(SELECT_TYPE);
    	busSelects.add(SELECT_MEMBER_ID);
    	busSelects.add(ProgramCentralConstants.SELECT_ATTRIBUTE_PROJECT_VISIBILITY);
    	
        try {

            String contextPersonId = PersonUtil.getPersonObjectID(context);
            Map programMap = (Map)JPO.unpackArgs(args);
            String strMasterProjectID = (String)programMap.get("objectId");
            excludeProjectIdList.addElement(strMasterProjectID);
            DomainObject masterProject = DomainObject.newInstance(context, strMasterProjectID);
            MapList mpProjectList = DomainObject.findObjects(context,typePattern,null,null,busSelects);
    		String CollabSpaceOfLoggedInUser =PersonUtil.getActiveProject(context);
            
            MapList existingRelatedProjectMapList = 
            				masterProject.getRelatedObjects(
											                context,
											                ProgramCentralConstants.RELATIONSHIP_RELATED_PROJECTS,
											                TYPE_PROJECT_MANAGEMENT,
											                relatedProjectSelectableList,
											                null,
											                true,
											                true,
											                (short) 1,
											                EMPTY_STRING,
											                EMPTY_STRING,0);
            
            StringList existingRelatedProjectIdList = new StringList(existingRelatedProjectMapList.size());
            
            for(int i =0; i<existingRelatedProjectMapList.size();i++) {
            	Map<String,String> relatedProjectInfoMap = (Map<String,String>)existingRelatedProjectMapList.get(i);
            	String relatedProjectId = relatedProjectInfoMap.get(SELECT_ID);
            	existingRelatedProjectIdList.add(relatedProjectId);
            }

            for (Iterator<Map<String,String>> iterator = mpProjectList.iterator(); iterator.hasNext();) {
            	
            	Map<String,String>  projectInfoMap = iterator.next();
            	
                String memberIds 		 = projectInfoMap.get(SELECT_MEMBER_ID);
                String strPtojectId 	 = projectInfoMap.get(DomainObject.SELECT_ID);
                String projectType 		 = projectInfoMap.get(DomainObject.SELECT_TYPE);
                String projectState 	 = projectInfoMap.get(DomainObject.SELECT_CURRENT);
                String projectVisibility = projectInfoMap.get(ProgramCentralConstants.SELECT_ATTRIBUTE_PROJECT_VISIBILITY);
    			DomainObject strProjectObject =DomainObject.newInstance(context,strPtojectId);
    			String collabSpaceofStrProject = strProjectObject.getInfo(context, "altowner2");
                StringList projectMemberIdList = 
                				FrameworkUtil.split(memberIds, matrix.db.SelectConstants.cSelectDelimiter);

                if(existingRelatedProjectIdList.contains(strPtojectId)||
                		projectType.equalsIgnoreCase(ProgramCentralConstants.TYPE_EXPERIMENT)|| 
    					ProgramCentralConstants.TYPE_PROJECT_BASELINE.equalsIgnoreCase(projectType)|| 
    					projectState.equalsIgnoreCase(STATE_PROJECT_SPACE_ARCHIVE)||projectState.equalsIgnoreCase(STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL))
    			{
    				excludeProjectIdList.addElement(strPtojectId);
    			}
    			if((!projectMemberIdList.contains(contextPersonId) && !projectVisibility.equals("Company")) && !collabSpaceofStrProject.equals(CollabSpaceOfLoggedInUser))
    			{
                	excludeProjectIdList.addElement(strPtojectId);
                }
            }
        }catch (Exception e) {
            throw new MatrixException(e);
        }
        return excludeProjectIdList;
    }

    /**
     * connect the new member to the Project Space.
     *
     * @param context the eMatrix Context object
     * @param args holds id of Project Member object
     * @return void
     * @throws Exception if the operation fails
     */
    public void connectMember(matrix.db.Context context, String[] args) throws Exception
    {
        try {
            String projectSpaceId = args[0];
            String project = args[1];
            String result = MqlUtil.mqlCommand(context, "list role $1 select $2 $3 dump $4", true, project, "person", "parent", "|");
            StringList resultList = StringUtil.split(result, "|");
            if( resultList.size() == 2 && "User Projects".equals(resultList.get(1)) )
            {
                String personId = PersonUtil.getPersonObjectID(context, (String)resultList.get(0));
                if( personId != null)
                {
                    ProjectSpace projectSpace = new ProjectSpace(projectSpaceId);

                    String relationship_WorkspaceMember = PropertyUtil.getSchemaProperty(context,"relationship_WorkspaceMember");
                    //String select = "print bus "+ projectSpaceId +" select from["+ DomainConstants.RELATIONSHIP_MEMBER +"|to.id==" + personId +"].id dump";
                    String mqlQueryString = "print bus $1 select $2 dump";
                    String relId = MqlUtil.mqlCommand(context, mqlQueryString, true,projectSpaceId,"from["+ DomainConstants.RELATIONSHIP_MEMBER +"|to.id==" + personId +"].id");
                    if( relId == null || "".equals(relId))
                    {
                     DomainObject domPerson = DomainObject.newInstance(context,personId);
                     DomainObject domProject = DomainObject.newInstance(context,projectSpaceId);

                     DomainRelationship domRel = DomainRelationship.connect(context, domProject,  DomainConstants.RELATIONSHIP_MEMBER, domPerson);
                     domRel.setAttributeValue(context,DomainConstants.ATTRIBUTE_PROJECT_ACCESS,"Project Member");
                     //domRel.setAttributeValue(context,DomainConstants.ATTRIBUTE_PROJECT_ROLE,"Project Lead");

                    }
                }
            }
        } catch (Exception e) {
            throw e;
      }
    }


    /**
     * Disconnecting connected objects after Security Contexts are removed from the Project.
     *
     * @param context the eMatrix Context object
     * @param args holds id of Project Member object
     * @return void
     * @throws Exception if the operation fails
     */
    public void disconnectMember(matrix.db.Context context, String[] args) throws Exception
    {
    	boolean isContextPushed = false;
        try
        {
            String ProjectSpaceId = args[0];
            String project = args[1];
            String org = args[2];
            String comment = args[3];
            String personId = "";
            String result = MqlUtil.mqlCommand(context, "list role $1 select $2 $3 dump $4", true, project, "person", "parent", "|");
            StringList resultList = StringUtil.split(result, "|");

    		if( !(resultList.size() == 2 && "User Projects".equals(resultList.get(1)))){
    			DomainObject domainObject = new DomainObject(ProjectSpaceId);
    			StringList selects = new StringList(1);
    			selects.add(DomainConstants.SELECT_ID);
    			selects.add(WorkspaceVault.SELECT_ACCESS_TYPE);
    			ProgramCentralUtil.pushUserContext(context);
    			isContextPushed = true;
    			String relType = DomainConstants.RELATIONSHIP_WORKSPACE_VAULTS + "," + DomainConstants.RELATIONSHIP_SUB_VAULTS;
    			MapList folderList = domainObject.getRelatedObjects(context, relType, "*", selects, null, false, true, (short)0, null, null,0,null,null,null);
    			String accessType = EMPTY_STRING;
    			String oid = EMPTY_STRING;
    			Iterator itr = folderList.iterator();
    			while(itr.hasNext())
                    {
    				Map folderMap = (Map)itr.next();
    				oid = (String)folderMap.get(DomainConstants.SELECT_ID);
    				accessType = (String)folderMap.get(WorkspaceVault.SELECT_ACCESS_TYPE);
    				if("Inherited".equalsIgnoreCase(accessType)){
    				DomainAccess.deleteObjectOwnership(context, oid, org, project, comment);
                }
                }

            }

        } catch (Exception e) {
            throw e;
    	}finally{
    		if(isContextPushed){
    			ProgramCentralUtil.popUserContext(context);
    		}
      }
    }

    /**
     * modify the primary ownership.  This method clears primary ownership of the object
     *      if the project was created with Member visibility.
     *
     * @param context the eMatrix Context object
     * @param args[0] holds id of Project Space
     * @param args[1] holds id of the object we are connecting to the PAL object
     * @return void
     * @throws Exception if the operation fails
     */
    public void modifyPrimaryOwnership(Context context, String[] args) throws Exception
    {
    	String createOperation = PropertyUtil.getGlobalRPEValue(context, "IGNORE_CREATE_TRIGGER");
		if(!"true".equalsIgnoreCase(createOperation)) {
        try
        {
        	String projectId = args[0]; // id of either Project or PAL object
        	String objectId = args[2]; // id of object we are creating in the WBS
        	String projectVisibility = null;
        	String SELECT_PROJECT_TEMPLATE_TYPE_FROM_PAL = "from["+DomainObject.RELATIONSHIP_PROJECT_ACCESS_LIST+"].to.type";

        	DomainObject domainObject = DomainObject.newInstance(context,projectId);

        	boolean isTaskObject = false;
        	Map taskInfo = null;
        	StringList objectSelects = new StringList();
        	objectSelects.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
        	objectSelects.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_CONCEPT);
        	objectSelects.add(SELECT_PROJECT_VISIBILITY);
        	objectSelects.add(SELECT_PROJECT_VISIBILITY_FROM_PAL);
	        	
        	Map mProjectInfo = domainObject.getInfo(context,  objectSelects);

        	String sId 	   = (String)mProjectInfo.get(SELECT_ID);
        	String bisProjectSpaceType = (String)mProjectInfo.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
        	String bisProjectConceptType = (String)mProjectInfo.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_CONCEPT);
        	String sVisibility = (String)mProjectInfo.get(SELECT_PROJECT_VISIBILITY);
        	String sVisibilityFromPal = (String)mProjectInfo.get(SELECT_PROJECT_VISIBILITY_FROM_PAL);
        	if (bisProjectSpaceType.equalsIgnoreCase("true") || bisProjectConceptType.equalsIgnoreCase("true"))
        	{
        		//projectVisibility = domainObject.getInfo(context,SELECT_PROJECT_VISIBILITY);
        		projectVisibility = sVisibility;
        	}
        	else
        	{
        		//projectVisibility = domainObject.getInfo(context,SELECT_PROJECT_VISIBILITY_FROM_PAL);
        		projectVisibility = sVisibilityFromPal;
        		// set to the Task Managment object we are trying to clear
        		domainObject.setId(objectId);
        		isTaskObject = true;
        	}

        	//not applicable for DeliverablesPlanning
        	if (ProgramCentralConstants.EMPTY_STRING.equals(projectVisibility)) {
        		return;
        	}

        	StringList selectables = new StringList();
        	selectables.add(SELECT_ID);
        	selectables.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
        	selectables.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_CONCEPT);
        	if(isTaskObject){
        		taskInfo   = domainObject.getInfo(context,  selectables);
        		sId 	   = (String)taskInfo.get(SELECT_ID);
                bisProjectSpaceType = (String)taskInfo.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
                bisProjectConceptType = (String)taskInfo.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_CONCEPT);
        	}

        	String defaultProj = PersonUtil.getActiveProject(context);

        	//if Project Space belongs to GLOBAL collab space, remove primary ownership
        	if ("GLOBAL".equalsIgnoreCase(defaultProj)){
        		domainObject.removePrimaryOwnership(context);					
        	}
        	String defaultOrg = PersonUtil.getActiveOrganization(context);
        	defaultOrg = Organization.getCompanyTitleFromName(context, defaultOrg);  // Added to return Title instead of Name
        	if("Company".equalsIgnoreCase(projectVisibility) && 
        			("true".equalsIgnoreCase(bisProjectSpaceType) || "true".equalsIgnoreCase(bisProjectConceptType)))
        	{
        		DomainAccess.createObjectOwnership(context, sId, defaultOrg, null, "Project Member", DomainAccess.COMMENT_MULTIPLE_OWNERSHIP, true);
        	}
            if ("Members".equalsIgnoreCase(projectVisibility) && ("true".equalsIgnoreCase(bisProjectSpaceType) || "true".equalsIgnoreCase(bisProjectConceptType))) {
        		DomainAccess.deleteObjectOwnership(context, domainObject.getId(context), defaultOrg, null, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP, true);
        	}

        }catch(Exception ex){
            throw new MatrixException(ex);
        }
    }

        
    }

  	/**
  	 * Updated project information
  	 * @param context - The eMatrix <code>Context</code> object. 
  	 * @param args holds information about object.
  	 * @throws Exception if operation fails.
  	 */
  	@com.matrixone.apps.framework.ui.PostProcessCallable  
  	public void createAndConnectProject(Context context,String[]args)throws Exception
  	{
  		//TODO
  	}

  	/**
  	 * Create new blank project space object.
  	 * @param context - The eMatrix <code>Context</code> object. 
  	 * @param args holds information about object.
  	 * @return newly created obejct id;
  	 * @throws Exception if operation fails.
  	 */
  	@com.matrixone.apps.framework.ui.CreateProcessCallable  
  	public Map createNewProject(Context context,String[]args)throws Exception
  	{
  		ProjectSpace project =(ProjectSpace)DomainObject.newInstance(context,
  				ProgramCentralConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);

  		ProjectSpace newProject =(ProjectSpace)DomainObject.newInstance(context,
  				ProgramCentralConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);

  		Map <String,String>returnMap = new HashMap();
  		try{
  			ContextUtil.startTransaction(context, true);

  			String SCHEDULE_FROM = PropertyUtil.getSchemaProperty(context,"attribute_ScheduleFrom");
  			Map <String,String>attributeMap = new HashMap();
  			Map <String,String>basicProjectInfo = new HashMap();
  			Map <String,String>relatedProjectInfo = new HashMap();

  			Map programMap 					= (HashMap) JPO.unpackArgs(args);
			String objectId 				= (String)programMap.get("objectId");
  			String createProject 			= (String)programMap.get("createProject");
  			String projectName 				= (String)programMap.get("Name");
  			String projectAutoName 			= (String)programMap.get("autoNameCheck");
  			String projectDescrption 		= (String)programMap.get("Description");
  			String businessUnitId 			= (String)programMap.get("BusinessUnitOID");
  			String programId 				= (String)programMap.get("ProgramOID");
  			String businessGoalId 			= (String)programMap.get("BusinessGoalOID");
  			String baseCurrency 			= (String)programMap.get("BaseCurrency");
  			String projectVault 			=  project.DEFAULT_VAULTS;
  			String projectVisibility 		= (String)programMap.get("ProjectVisibility");
  			String projectPolicy 			= (String)programMap.get("Policy");
  			String projectScheduleFrom 		= (String)programMap.get("ScheduleFrom");
  			String projectDate 				= (String)programMap.get("ProjectDate");
  			String defaultConstraintType 	= (String)programMap.get("DefaultConstraintType");
  			String projectSpaceType 		= (String)programMap.get("TypeActual");
  			String selectedProjectId 		= (String)programMap.get("SeachProjectOID");
  			String connectRelatedProjects 	= (String)programMap.get("connectRelatedProject");
  			String copyFinancialData 		= (String)programMap.get("financialData");
  			String copyFolderData 			= (String)programMap.get("folders");
			String sKeepSourceConstraints 	= (String)programMap.get("keepSourceConstraints");
			String sKeepSourceColors 		= (String)programMap.get("keepSourceColors");
			
			String refernceDocument 		= (String)programMap.get("ReferenceDocument");
			String deliverabletId 			= (String)programMap.get("DeliverableOID");
            String calendarId 				= (String)programMap.get("CalendarOID");
            
  			StringList calendarIds = FrameworkUtil.split(calendarId, "|");
  			
  			Locale locale	=	(Locale)programMap.get("localeObj");
            if(locale == null) {
            	locale	=	context.getLocale();
            }
			
  			String strTimeZone 				= (String)programMap.get("timeZone");
			double dClientTimeZoneOffset 	= (new Double(strTimeZone)).doubleValue();
			//  IR-528127-3DEXPERIENCER2018x
			boolean isECHInstalled =  FrameworkUtil.isSuiteRegistered(context,
	  				"appVersionEnterpriseChange",false,null,null);
			if(isECHInstalled){
				if(mxType.isOfParentType(context, projectSpaceType, DomainObject.TYPE_CHANGE_PROJECT))
					programId = (String)programMap.get("ECHMandProgramOID");
			}
			//end  IR-528127-3DEXPERIENCER2018x

  			if(ProgramCentralUtil.isNotNullString(projectDate)){
  				/*projectDate = projectDate.trim();
  				projectDate = eMatrixDateFormat.getFormattedInputDate(context,projectDate,dClientTimeZoneOffset,locale);*/
  				TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
				double dbMilisecondsOffset = (double)(-1)*tz.getRawOffset();
				dClientTimeZoneOffset = (new Double(dbMilisecondsOffset/(1000*60*60))).doubleValue(); 
  				projectDate = projectDate.trim();
  				 int iDateFormat = eMatrixDateFormat.getEMatrixDisplayDateFormat();
  		        String strInputTime = eMatrixDateFormat.adjustTimeStringForInputFormat("");
  				projectDate = eMatrixDateFormat.getFormattedInputDateTime(projectDate, strInputTime, iDateFormat, dClientTimeZoneOffset, locale);
  			}

  			//For program,Businessgoal and related project
  			if(ProgramCentralUtil.isNotNullString(objectId)){
  				StringList selectable = new StringList();
  				selectable.addElement(ProgramCentralConstants.SELECT_IS_PROJECT_SPACE);
  				selectable.addElement(ProgramCentralConstants.SELECT_IS_PROGRAM);
  				selectable.addElement(ProgramCentralConstants.SELECT_IS_BUSINESS_GOAL);
  				
  				DomainObject parentObject = DomainObject.newInstance(context,objectId);
  				Map <String,String>parentObjectInfo = parentObject.getInfo(context, selectable);
  				String isProjectSpace 				= parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_PROJECT_SPACE);
  				String isProgram 					= parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_PROGRAM);
  				String isBusinessGoal 				= parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_BUSINESS_GOAL);
  				
  				if("true".equalsIgnoreCase(isProgram)){
  					programId = objectId;
  				}else if("true".equalsIgnoreCase(isBusinessGoal)){
  					businessGoalId = objectId;
  				}else if("true".equalsIgnoreCase(isProjectSpace)){
  					relatedProjectInfo.put("AddAsChild", "true");
  					relatedProjectInfo.put("RelatedProjectId", objectId);
  				}
  			}

  			//Project space attribute map values
  			attributeMap.put(DomainObject.ATTRIBUTE_TASK_ESTIMATED_START_DATE, projectDate);
  			attributeMap.put(DomainObject.ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE, projectDate);
  			attributeMap.put(DomainObject.ATTRIBUTE_TASK_ESTIMATED_DURATION, "0.0");
  			attributeMap.put(DomainObject.ATTRIBUTE_PROJECT_VISIBILITY, projectVisibility);
  			attributeMap.put(DomainObject.ATTRIBUTE_CURRENCY, baseCurrency);
  			attributeMap.put(SCHEDULE_FROM, projectScheduleFrom);
  			attributeMap.put(DomainObject.ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE, defaultConstraintType);
  			
  			//Baseline attributes should not have any values while project creation.
  			attributeMap.put(DomainObject.ATTRIBUTE_BASELINE_INITIAL_START_DATE, ProgramCentralConstants.EMPTY_STRING);
  			attributeMap.put(DomainObject.ATTRIBUTE_BASELINE_INITIAL_END_DATE, ProgramCentralConstants.EMPTY_STRING);
  			attributeMap.put(DomainObject.ATTRIBUTE_BASELINE_CURRENT_START_DATE, ProgramCentralConstants.EMPTY_STRING);
  			attributeMap.put(DomainObject.ATTRIBUTE_BASELINE_CURRENT_END_DATE, ProgramCentralConstants.EMPTY_STRING);

  			if("Clone".equalsIgnoreCase(createProject) || "Template".equalsIgnoreCase(createProject)){
  				
  				SimpleDateFormat dateFormat = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(),Locale.US);
  				Calendar constraintDate = Calendar.getInstance();
  				Date newDate = null;
  				newDate = dateFormat.parse(projectDate);
  				constraintDate.setTime(newDate);
  				if(defaultConstraintType.equalsIgnoreCase(ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ASAP)){
  					constraintDate.set(Calendar.HOUR_OF_DAY, 8);
  				} else {
  					constraintDate.set(Calendar.HOUR_OF_DAY, 17);
  				}
  				constraintDate.set(Calendar.MINUTE, 0);
  				constraintDate.set(Calendar.SECOND, 0);
  				projectDate = dateFormat.format((constraintDate.getTime()));
  				attributeMap.put(DomainObject.ATTRIBUTE_TASK_CONSTRAINT_DATE, projectDate);
  			}

  			//get auto name 
  			if(ProgramCentralUtil.isNullString(projectName) && projectAutoName.equalsIgnoreCase("true")){
  				String symbolicTypeName = PropertyUtil.getAliasForAdmin(context, "Type", projectSpaceType, true);
  				String symbolicPolicyName = PropertyUtil.getAliasForAdmin(context, "Policy", projectPolicy, true);
  				
  				projectName = FrameworkUtil.autoName(context,
  						symbolicTypeName,
  						null,
  						symbolicPolicyName,
  						null,
  						null,
  						true,
  						true);
  			}

  			//builds basic project info map
  			basicProjectInfo.put("name", projectName);
  			basicProjectInfo.put("type", projectSpaceType);
  			basicProjectInfo.put("policy", projectPolicy);
  			basicProjectInfo.put("vault", projectVault);
  			basicProjectInfo.put("description", projectDescrption);

  			//Builds related project info map
  			relatedProjectInfo.put("programId", programId);
  			relatedProjectInfo.put("businessUnitId", businessUnitId);
  			relatedProjectInfo.put("businessGoalId", businessGoalId);
            relatedProjectInfo.put("deliverableId", deliverabletId);

  			boolean isCopyFolderData	= true;
  			boolean isCopyFinancialData = true;
			boolean keepSourceConstraints = true;
			boolean keepSourceColors = true;

  			if(ProgramCentralUtil.isNullString(copyFolderData) || "false".equalsIgnoreCase(copyFolderData)){
  				isCopyFolderData = false;
  			}

  			if(ProgramCentralUtil.isNullString(copyFinancialData) || "false".equalsIgnoreCase(copyFinancialData)){
  				isCopyFinancialData = false;
  			}

			if(ProgramCentralUtil.isNullString(sKeepSourceConstraints) || "false".equalsIgnoreCase(sKeepSourceConstraints)){
				keepSourceConstraints = false;
			}
			//Can Comment iF as both statement in If and Else are same
			if(ProgramCentralUtil.isNullString(sKeepSourceColors) || "false".equalsIgnoreCase(sKeepSourceColors)){
				PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
			}else{
				PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
			}
			
			
			ContentReplicateOptions selectedOptionForReferenceDocument = ContentReplicateOptions.COPY;
			if(ProgramCentralUtil.isNullString(refernceDocument) || "Reference".equalsIgnoreCase(refernceDocument)){
				selectedOptionForReferenceDocument = ContentReplicateOptions.CONNECT_EXISTING;
			}

  			//Create new project object.
  			if("Blank".equalsIgnoreCase(createProject) || 
  					"Import".equalsIgnoreCase(createProject)){

  				newProject = project.createBlankProject(context, 
  						basicProjectInfo, 
  						attributeMap, 
  						relatedProjectInfo);

  			}else if("Clone".equalsIgnoreCase(createProject)){
  				boolean isConnectRelatedProject = false;
  				if(ProgramCentralUtil.isNotNullString(connectRelatedProjects) &&
  						connectRelatedProjects.equalsIgnoreCase("True")){
  					isConnectRelatedProject = true;
  				}

  				//create new project from existing object.
  				newProject = project.clone(context, 
  						selectedProjectId, 
  						basicProjectInfo,
  						relatedProjectInfo, 
  						attributeMap,
  						isConnectRelatedProject,
  						isCopyFolderData,
  						isCopyFinancialData,
						keepSourceConstraints);

  			}else if("Template".equalsIgnoreCase(createProject)){
  				boolean  isTemplateTaskAutoName = false;
  				String questionResponseValue 	= (String) CacheUtil.getCacheObject(context, "QuestionsResponse");
  				String resourceTemplateId 	    = (String)programMap.get("ResourceTemplate");

  				Map <String,String>questionResponseMap = new HashMap<String,String>();
  				if(ProgramCentralUtil.isNotNullString(questionResponseValue)){
  					StringList questionResponseValueList = FrameworkUtil.split(questionResponseValue, "|");
  					for(int i=0;i<questionResponseValueList.size();i++){
  						String questionRValue = (String)questionResponseValueList.get(i);
  						StringList questionActualRList = FrameworkUtil.split(questionRValue, "=");
  						questionResponseMap.put((String)questionActualRList.get(0), (String)questionActualRList.get(1));
  					}
  				}


  				//update related info
  				relatedProjectInfo.put("resourceTemplateId", resourceTemplateId);

  				newProject = project.cloneTemplateToCreateProject(context, 
  						selectedProjectId, 
  						basicProjectInfo, 
  						relatedProjectInfo, 
  						attributeMap,
  						questionResponseMap, 
  						isTemplateTaskAutoName,
  						isCopyFolderData,
						isCopyFinancialData,
						keepSourceConstraints,
                        selectedOptionForReferenceDocument, calendarIds);
  			}
  			//Get new project ID
  			String newProjectId = newProject.getObjectId();

  			returnMap.put("id", newProjectId);
            if(!"Template".equalsIgnoreCase(createProject)) {
  			// If only one calendar is selected then that will be connected as Default Calendar
  			if(calendarIds.size()==1){
  				String defaultCalendarId = calendarIds.get(0) +"|DefaultCalendar";
  				calendarIds.remove(0);
  				calendarIds.add(defaultCalendarId);
  			}
  			newProject.addCalendars(context, calendarIds);
		
  			ContextUtil.commitTransaction(context);
  			//required for Calendars
  			Task rollup = new Task(newProjectId);
  			rollup.rollupAndSave(context);
			}
  		}catch(Exception ex){
  			ContextUtil.abortTransaction(context);
  			ex.printStackTrace();
  			if(ex.getMessage().contains("No create access")){
  				throw new Exception(EnoviaResourceBundle.getProperty(context,ProgramCentralConstants.PROGRAMCENTRAL, 
  						"emxProgramCentral.Project.NoCreateAccess", context.getSession().getLanguage()));
  			}
  			else{
  				throw  ex;
  			}
  		}finally{
  			PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
  		}

  		return returnMap;
  	}

  	/**
  	 * Get Selected Project policy
  	 * @param context - The eMatrix <code>Context</code> object. 
  	 * @param args holds information about object.
  	 * @return policy list.
  	 * @throws Exception if operation fails.
  	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable
  	public Map getProjectPolicyRange(Context context,String[]args)throws Exception
  	{
  		Map programMap = (HashMap) JPO.unpackArgs(args);
  		Map requestMap = (HashMap)programMap.get("requestMap");

  		String projectSpaceType = (String)requestMap.get("type");
  		projectSpaceType = PropertyUtil.getSchemaProperty(context,projectSpaceType);

  		if(ProgramCentralUtil.isNullString(projectSpaceType)){
  			projectSpaceType = DomainObject.TYPE_PROJECT_SPACE;
  		}

		MapList policyList = mxType.getPolicies(context, projectSpaceType, true);

  		StringList hiddenPolicyList = new StringList();
  		StringList displayPolicyList = new StringList();
  		for(int i=0;i<policyList.size();i++){
			Map policyMap = (Map)policyList.get(i);
			String policy = (String)policyMap.get("name");

			// IR-621192 - NX5
			if( !ProgramCentralConstants.POLICY_PROJECT_SPACE_HOLD_CANCEL.equalsIgnoreCase(policy) &&
				!policy.contains("Bio_policyCockpitProject")){
  				String i18Policy = EnoviaResourceBundle.getAdminI18NString(context, "Policy", policy, context.getSession().getLanguage());
  				hiddenPolicyList.addElement(policy);
  				displayPolicyList.addElement(i18Policy);
  			}
  		}
  		
  		Map policyMap = new HashMap();
  		policyMap.put("field_choices", hiddenPolicyList);
  		policyMap.put("field_display_choices", displayPolicyList);

  		return policyMap;
  	}

  	/**
  	 * Get current date for project date field.
  	 * @param context - The eMatrix <code>Context</code> object. 
  	 * @param args holds object related information.
  	 * @return current date.
  	 * @throws Exception if operation fails.
  	 */
  	public String getCurrentDate(Context context,String[]args)throws Exception
  	{
  		ProjectSpace project = new ProjectSpace();
  		return project.getCurrentDate(context,args);
  	}
  	/**
  	 * Update project date.
  	 * @param context - The eMatrix <code>Context</code> object. 
  	 * @param args holds object information.
  	 * @throws Exception
  	 */
  	public void updateProjectDate(Context context,String[]args)throws Exception
  	{
  		//TODO
  		/**
  		 * please do not remove this method because when we using standard component with prepapulated value, update method required for that field.
  		 */
  	}

  	/**
  	 * Copy option for copy project.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return option value.
  	 * @throws Exception if operation fails.
  	 */
  	public String getCopyOptions(Context context,String[]args)throws Exception
  	{
  		String includeRelatedProject = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
  				"emxProgramCentral.Common.IncludeRelatedProject", context.getSession().getLanguage());
  		
  		String financialData = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
  				"emxProgramCentral.Common.Project.FinancialData", context.getSession().getLanguage());
  		
  		String folders = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
  				"emxProgramCentral.Common.Project.Folders", context.getSession().getLanguage());
  		
			String constraints = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
				"emxProgramCentral.Common.Constraints", context.getSession().getLanguage());
  		
		
  		StringBuilder sb = new StringBuilder();
  		sb.append("<table>");
  		sb.append("<tr>");
  		
		sb.append("<td colspan=\"2\" width=\"135\">");
  		sb.append("<input type=\"checkbox\" name=\"connectRelatedProject\" value=\"True\" />");
  		sb.append("<label for=\"connectRelatedProject\">");
  		sb.append(includeRelatedProject);
  		sb.append("</label>");
  		sb.append("</td>");

  		sb.append("<td colspan=\"2\" width=\"120\">");
  		sb.append("<input type=\"checkbox\" name=\"folders\" value=\"true\" checked=\"true\" />");
  		sb.append("<label for=\"folders\">");
  		sb.append(folders);
  		sb.append("</label>");
  		sb.append("</td>");
  		
  		sb.append("</tr>");
  		sb.append("<tr>");
  		
  		sb.append("<td colspan=\"2\" width=\"95\">");
  		sb.append("<input type=\"checkbox\" name=\"financialData\" value=\"true\" checked=\"true\"/>");
  		sb.append("<label for=\"financialData\">");
  		sb.append(financialData);
  		sb.append("</label>");
  		sb.append("</td>");
		
		sb.append("<td colspan=\"2\" width=\"100\">");
		sb.append("<input type=\"checkbox\" name=\"keepSourceConstraints\" value=\"true\" checked=\"true\" />");
		sb.append("<label for=\"constraints\">");
		sb.append(constraints);
		sb.append("</label>");
		sb.append("</td>");

		sb.append("<td colspan=\"2\" width=\"100\" hidden=\"true\">");
		sb.append("<input type=\"checkbox\" name=\"keepSourceColors\" value=\"true\" checked=\"true\"/>");
		sb.append("<label for=\"colors\">");
		sb.append("Colors");
		sb.append("</label>");
		sb.append("</td>");
		
  		sb.append("</tr>");
  		sb.append("</table>");

  		return sb.toString();
  	}

  	/**
  	 * Copy option for template.
  	 * @param context - The eMatrix <code>Context</code> object. 
  	 * @param args holds object informations.
  	 * @return option value.
  	 * @throws Exception if operation fails.
  	 */
  	public String getCopyOptionsForTemplate(Context context,String[]args)throws Exception
  	{
  		String financialData = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
				"emxFramework.Command.FinancialItem", context.getSession().getLanguage());
  		
  		String folders = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
  				"emxProgramCentral.Common.Project.Folders", context.getSession().getLanguage());
  		
		String constraints = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
				"emxProgramCentral.Common.Constraints", context.getSession().getLanguage());

  		StringBuilder sb = new StringBuilder();
  		sb.append("<table>");
  		sb.append("<tr>");

		sb.append("<td>");
  		sb.append("<input type=\"checkbox\" name=\"financialData\" value=\"true\" checked=\"true\"/>");
  		sb.append("<label for=\"financialData\">");
  		sb.append(financialData);
  		sb.append("</label>");
  		sb.append("</td>");

		sb.append("<td>");
		sb.append("&amp;nbsp;");
		sb.append("</td>");

		sb.append("<td>");
  		sb.append("<input type=\"checkbox\" name=\"folders\" value=\"true\" checked=\"true\" />");
  		sb.append("<label for=\"folders\">");
  		sb.append(folders);
  		sb.append("</label>");
  		sb.append("</td>");

		sb.append("<td>");
		sb.append("&amp;nbsp;");
		sb.append("</td>");

		sb.append("<td>");
		sb.append("<input type=\"checkbox\" name=\"keepSourceConstraints\" value=\"true\" checked=\"true\" />");
		sb.append("<label for=\"folders\">");
		sb.append(constraints);
		sb.append("</label>");
		sb.append("</td>");

		sb.append("<td colspan=\"2\" width=\"100\" hidden=\"true\">");
		sb.append("<input type=\"checkbox\" name=\"keepSourceColors\" value=\"True\" checked=\"true\"/>");
		sb.append("<label for=\"colors\">");
		sb.append("Colors");
		sb.append("</label>");
		sb.append("</td>");

  		sb.append("</tr>");
  		sb.append("</table>");

  		return sb.toString();
  	}

  	/**
  	 * Get import file feild.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about objects.
  	 * @return import field with preview command.
  	 * @throws Exception if operation fails.
  	 */
  	public String getImportField(Context context,String[]args)throws Exception
  	{
		Map programMap 		= (Map) JPO.unpackArgs(args);
		Map requestMap 		= (Map)programMap.get("requestMap");
  		String importMode = (String)requestMap.get("importMode");
		String importSubProject = (String)requestMap.get("importSubProject");
  		if(ProgramCentralUtil.isNullString(importMode)){
  			importMode = ProgramCentralConstants.EMPTY_STRING;
  		}
		if(ProgramCentralUtil.isNotNullString(importSubProject)) {
        	importMode = "ProjectTemplateSchedule";
        }
  		String previewButtonLevel = EnoviaResourceBundle.getProperty(context,ProgramCentralConstants.PROGRAMCENTRAL, 
  				"emxProgramCentral.Common.Project.Preview", context.getSession().getLanguage()); 
  		
  		StringBuilder sb = new StringBuilder();
  		sb.append("<table>");
  		sb.append("<tr>");

  		sb.append("<td >");
  		sb.append("<input type=\"file\" id= \"FileFullPath\" name=\"FilePath\" size=\"18\" onChange=\"javascript=getFileDetails('"+importMode+"')\"/>");
  		sb.append("</td>");

		//Change for IR-663521
		// To put the review button on the new row
		sb.append("</tr>");
		sb.append("<tr style=\"float: left;\" >");
		sb.append("<td class=\"icon-and-text-button\" style=\"width: 100px;\" >");
		sb.append("<input type=\"button\"  id=\"previewFile\" name=\"previewFile\" value=\"" + previewButtonLevel
				+ "\" onClick=\"javascript=getPreview()\" style=\"margin-top: 6px;margin-left: 3px;margin-bottom: 3px; width: 93px; margin-right: 6px; \" />");

		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");

  		return sb.toString();
  	}

  	/**
  	 * Remove all cache object which is used for import preview.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @throws Exception If operation fails.
  	 */
  	private void removeImportCacheSetting(Context context)throws Exception
  	{
  		CacheUtil.removeCacheObject(context, "noMoreExpandNameColumn");
  		CacheUtil.removeCacheObject(context, "noMoreExpandWBSColumn");
  		CacheUtil.removeCacheObject(context, "noMoreExpandTypeColumn");
  		CacheUtil.removeCacheObject(context, "noMoreExpandEstDuration");
  		CacheUtil.removeCacheObject(context, "noMoreExpandEstStartDate");
  		CacheUtil.removeCacheObject(context, "noMoreExpandEstEndDate");
  		CacheUtil.removeCacheObject(context, "noMoreExpandDependencyColumn");
  		CacheUtil.removeCacheObject(context, "noMoreExpandAssigneeColumn");
  		CacheUtil.removeCacheObject(context, "noMoreExpandCTColumn");
  		CacheUtil.removeCacheObject(context, "noMoreExpandCDColumn");
  		CacheUtil.removeCacheObject(context, "noMoreExpandDescriptionColumn");
  	}
  	
  	/**
  	 * Get vertual view of imported file object.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return imported object List.
  	 * @throws Exception If opration fails.
  	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable
  	public MapList getImportObjectList(Context context, String[] args) throws Exception
  	{
  		ProjectSpace project = new ProjectSpace();

  		MapList importObjectList = new MapList();
  		Map programMap = (Map)JPO.unpackArgs(args);
  		Map requestValuesMap = (Map)programMap.get("RequestValuesMap");

  		String isAlreadyExpanded = (String)requestValuesMap.get("isFullyExpanded");
  		if(ProgramCentralUtil.isNullString(isAlreadyExpanded)){
  			removeImportCacheSetting(context);
  			requestValuesMap.put("isFullyExpanded", "true");

  			String fileContent = MyCalendarUtil.getValueFromContext(context,"fileContent");
  			StringList fileContentList = FrameworkUtil.split(fileContent, "\n");

  			for(int i=0,size=fileContentList.size();i<size;i++){
  				String fileLine = (String) fileContentList.get(i);
  				StringList fileContents =  FrameworkUtil.split(fileLine, ";");
  				
  				Map fileMap = new HashMap();
  				for(int j=0,fSize=fileContents.size();j<fSize;j++){
  					String contents = (String)fileContents.get(j);
  					StringList fileKeyValuePair = FrameworkUtil.split(contents, "=");
  					
					if(fileKeyValuePair.size()>=2) {
  					String key = (String)fileKeyValuePair.get(0);
  					String value = (String)fileKeyValuePair.get(1);

  					if("WBS".equalsIgnoreCase(key)||"Level".equalsIgnoreCase(key)){
  						String level = getLevel(context, value);
  						if(level.equalsIgnoreCase("0")){
  							fileMap.put("id[level]", level);
  							fileMap.put("Root Node", "true");
  						}else{
  							fileMap.put("hasChildren","false");
  						}
  						fileMap.put("level", level);
  					}
  					
  					if(value.startsWith("Error:")){
  						fileMap.put("styleRows", "ResourcePlanningRedBackGroundColor");
  					}
  					fileMap.put(key, value);
  				}
				}

  				fileMap.put("hasChildren","false");
  				fileMap.put("RowEditable", "readonly");

  				fileMap.put("expand","true");
  				fileMap.put("display","block");

  				/*if(!project.hasCorrectValue(context, fileMap)){
  					fileMap.put("styleRows", "ResourcePlanningRedBackGroundColor");
  				}*/

  				importObjectList.add(fileMap);
  			}
  		}
  		return importObjectList;

  	}

  	/**
  	 * Get correct WBS level value.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param wbs ia s Task WBS value.
  	 * @return task level.
  	 * @throws Exception if operation fails.
  	 */
  	private String getLevel(Context context,String wbs)throws Exception
  	{
  		String newLevel = "0";
  		if(wbs.equalsIgnoreCase("0")){
  			newLevel = "0";
  		}else {
  			int counter = 1;
  			for( int i=0; i<wbs.length(); i++ ) {
  				if( wbs.charAt(i) == '.' ) {
  					counter++;
  				} 
  			}
  			newLevel = Integer.toString(counter);
  		}
  		return newLevel;
  	}
  	
  	/**
  	 * Get dynamic table for imported object list.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about objects.
  	 * @return dynamic table.
  	 * @throws Exception if operation fails.
  	 */
  	public MapList getProjectImportViewDynamicTable(Context context, String[] args) throws Exception
  	{
  		MapList tableColumnsList = new MapList();

  		String importedFileHeader = EnoviaResourceBundle.getProperty(context,"emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.ColumnName");
  		StringList importedFileHeaderList = FrameworkUtil.split(importedFileHeader, ",");
  		Map <String,Map>headerDetailsMap = new HashMap<String,Map>();

  		int size = importedFileHeaderList.size();

  		//Read setting from properties file.
  		for(int i=0;i<size;i++){
  			String headerName = (String)importedFileHeaderList.get(i);
  			Map headerMap = getImportedFileHeaderSettings(context, headerName);
  			headerDetailsMap.put(headerName, headerMap);
  		}

  		for(int i=0;i<size;i++){
  			String columnName = (String)importedFileHeaderList.get(i);

  			Map headerMap = headerDetailsMap.get(columnName);
  			String headerName = (String)headerMap.get("Header");
			String displayHeaderName = (String)headerMap.get("displayHeader");
  			String columnType = (String)headerMap.get("ColumnType");
  			String attributeName = (String)headerMap.get("AttributeName");
  			String program = (String)headerMap.get("program");
  			String function = (String)headerMap.get("function");
  			String groupHeader = (String)headerMap.get("GroupHeader");

  			Map columnMap = new HashMap(); 
  			Map <String,String>settingMap = new HashMap<String,String>(); 

  			columnMap.put("name", headerName);
			columnMap.put("label",displayHeaderName);

  			settingMap.put("Registered Suite","ProgramCentral");
  			settingMap.put("program",program);
  			settingMap.put("function",function);

  			if(headerName.equalsIgnoreCase("Name")){
  				settingMap.put("Column Type","programHTMLOutput");
  				settingMap.put("Width","180");
  				settingMap.put("Auto Filter","false");
  			}else if(headerName.equalsIgnoreCase("Level")){
  				settingMap.put("Column Type","program");
  				settingMap.put("Width","135");
  				settingMap.put("Sort Program","emxSortHTMLAlphaNumericBase");
  				settingMap.put("Sort Type","other");
  			}else{
  				settingMap.put("Column Type","program");
  				settingMap.put("Width","135");
  			}

  			if(ProgramCentralUtil.isNotNullString(groupHeader)&& !groupHeader.equalsIgnoreCase("NA")){
  				settingMap.put("Group Header",groupHeader);
  			}
  			settingMap.put("Editable","false");
  			if("Level".equalsIgnoreCase(headerName)){
  				settingMap.put("sorttype ","other");
  				settingMap.put("Auto Filter","false");
  			}
  			settingMap.put("Sortable","false");
  			settingMap.put("Export","false");

  			columnMap.put("settings", settingMap);

  			tableColumnsList.add(columnMap);

  		}

        Map columnMap = new HashMap(); 
        Map <String,String>settingMap = new HashMap<String,String>(); 
        columnMap.put("name", "ID");
        columnMap.put("label","emxProgramCentral.Common.ID");

        settingMap.put("Registered Suite","ProgramCentral");
        settingMap.put("program","emxProjectSpace");
        settingMap.put("function","getImportedObjectID");
        settingMap.put("sorttype ","other");
        settingMap.put("Auto Filter","false");
        settingMap.put("Sortable","false");
        settingMap.put("Export","false");
        settingMap.put("Column Type","program");
		settingMap.put("Width","50");
		settingMap.put("Sort Program","emxSortHTMLAlphaNumericBase");
		settingMap.put("Sort Type","other");
		
		   columnMap.put("settings", settingMap);

           tableColumnsList.add(1,columnMap);
        

  		return tableColumnsList;
  	}

  	/**
  	 * Get imported object name.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object name list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectName(Context context,String[]args)throws Exception
  	{
  		StringList nameList = new StringList();
  		String projectSpaceIcon = "images/iconSmallProject.png";
  		String taskIcon = "images/iconSmallTask.png";
  		String gateIcon = "images/iconSmallGate.png";
  		String milestoneIcon = "images/iconSmallMilestone.png";
  
  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);
  			MapList objectList = (MapList)programMap.get("objectList");
  			int objSize = objectList.size();

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize ==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandNameColumn") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){

  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskName = (String)fileMap.get("name");
  					String taskType = (String)fileMap.get("type");
  					if(ProgramCentralUtil.isNotNullString(taskName)){
  							String iconImg = "<img border=\"0\" src=\""+taskIcon+"\"/>";
  						if (ProgramCentralConstants.TYPE_GATE.equalsIgnoreCase(taskType)){
  							iconImg = "<img border=\"0\" src=\""+gateIcon+"\"/>";
  						}else if (ProgramCentralConstants.TYPE_MILESTONE.equalsIgnoreCase(taskType)){
  							iconImg = "<img border=\"0\" src=\""+milestoneIcon+"\"/>";
  						}else if(DomainObject.TYPE_PROJECT_SPACE.equalsIgnoreCase(taskType) || taskType.contains("Project Space")){
  							iconImg = "<img border=\"0\" src=\""+projectSpaceIcon+"\"/>";
  						}
  							taskName = iconImg+" "+ XSSUtil.encodeForXML(context, taskName);
  					}else{
  						taskName = DomainObject.EMPTY_STRING;
  					}
  					nameList.addElement(taskName);
  				}
  				//CacheUtil.setCacheObject(context, "noMoreExpandNameColumn", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return nameList;
  	}

  	/**
  	 * Get imported object WBS.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object WBS list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectWBS(Context context,String[]args)throws Exception
  	{
  		StringList taskWBSList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);
  			MapList objectList = (MapList)programMap.get("objectList");
  			int objSize = objectList.size();
  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandWBSColumn") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){

  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskWBS = (String)fileMap.get("Level");
  					taskWBSList.addElement(taskWBS);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandWBSColumn", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return taskWBSList;
  	}

    public StringList getImportedObjectID(Context context,String[]args)throws Exception
    {
    	StringList taskIDList = new StringList();

    	try{
    		Map programMap = (Map)JPO.unpackArgs(args);
    		MapList objectList = (MapList)programMap.get("objectList");
    		int objSize = objectList.size();
    		for(int i=0;i<objSize;i++) {
    			Map objectInfo = (Map)objectList.get(i);
    			String strLevel = (String)objectInfo.get("level");
    			if("0".equals(strLevel)) {
    				taskIDList.add("0");
    			} else {
    				taskIDList.add((String)objectInfo.get("WBSId"));
    			}
    		}

    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return taskIDList;
    }

  	/**
  	 * Get imported object type.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object type list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectType(Context context,String[]args)throws Exception
  	{
  		StringList importObjectTypeList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);
  			MapList objectList = (MapList)programMap.get("objectList");
  			int objSize = objectList.size();

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandTypeColumn") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){

  				String lang = context.getSession().getLanguage();
  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskType = (String)fileMap.get("type");
					String typeDisplay = EnoviaResourceBundle.getAdminI18NString(context, "Type", taskType, lang);
  					if(taskType.contains("Error:")){
  						typeDisplay = taskType;
  					}
					importObjectTypeList.addElement(typeDisplay);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandTypeColumn", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectTypeList;
  	}
  	
  	/**
  	 * Get imported object Estimated duration.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object estimated duration list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectEstDuration(Context context,String[]args)throws Exception
  	{
  		StringList importObjectDuration = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);
  			MapList objectList = (MapList)programMap.get("objectList");

  			int objSize = objectList.size();
  			String lang = context.getSession().getLanguage();

			String days = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
					"emxProgramCentral.DurationUnits.Days", lang);
			String hours = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
					"emxProgramCentral.DurationUnits.Hours",lang);
			
			

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize ==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandEstDuration") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){

  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskDuration = (String)fileMap.get("Task Estimated Duration");
  					String durationUnit = taskDuration.substring(taskDuration.lastIndexOf(" ")+1);
  					if(super.isCorrectValue(context, taskDuration)){

  						if("d".equalsIgnoreCase(durationUnit)||"days".equalsIgnoreCase(durationUnit)){
							taskDuration = taskDuration.replace(taskDuration.substring(taskDuration.lastIndexOf(" ")+1), days);
  						}else if("h".equalsIgnoreCase(durationUnit)||"hours".equalsIgnoreCase(durationUnit)){
							taskDuration = taskDuration.replace(taskDuration.substring(taskDuration.lastIndexOf(" ")+1), hours);
  						}
  					}

  					importObjectDuration.addElement(taskDuration);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandEstDuration", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectDuration;
  	}

  	/**
  	 * Get imported object Estimated start date.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object Estimated start date list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectEstStartDate(Context context,String[]args)throws Exception
  	{
  		StringList importObjectStartDateList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);
  			Map paramList = (Map)programMap.get("paramList");
  			MapList objectList = (MapList)programMap.get("objectList");

  			int objSize = objectList.size();

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandEstStartDate") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){

  				String timeZone = (String) paramList.get("timeZone");
  				double clientTZOffset = Task.parseToDouble(timeZone);
  				Locale locale = context.getLocale();

  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskStartDate = (String)fileMap.get("Task Estimated Start Date");

  					if(super.isCorrectValue(context, taskStartDate)){
						taskStartDate = eMatrixDateFormat.getFormattedDisplayDate(taskStartDate, clientTZOffset,locale);
  					}

  					importObjectStartDateList.addElement(taskStartDate);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandEstStartDate", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectStartDateList;
  	}
  	
  	/**
  	 * Get imported object Estimated end date.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object Estimated end date list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectEstEndDate(Context context,String[]args)throws Exception
  	{
  		StringList importObjectEndDateList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);
  			Map paramList = (Map)programMap.get("paramList");
  			MapList objectList = (MapList)programMap.get("objectList");

  			int objSize = objectList.size();

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandEstEndDate") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){  		
  				String timeZone = (String) paramList.get("timeZone");
  				double clientTZOffset = Task.parseToDouble(timeZone);
  				Locale locale = context.getLocale();

  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskEndDate = (String)fileMap.get("Task Estimated Finish Date");

  					if(super.isCorrectValue(context, taskEndDate)){
						taskEndDate = eMatrixDateFormat.getFormattedDisplayDate(taskEndDate, clientTZOffset,locale);
  					}

  					importObjectEndDateList.addElement(taskEndDate);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandEstEndDate", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectEndDateList;
  	}
  	
  	/**
  	 * Get imported object Dependency.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object Dependency list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectDependency(Context context,String[]args)throws Exception
  	{
  		StringList importObjectDependencyList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);
  			MapList objectList = (MapList)programMap.get("objectList");

  			int objSize = objectList.size();

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandDependencyColumn") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){  

  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskDependency = (String)fileMap.get("Dependencies");
  					importObjectDependencyList.addElement(taskDependency);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandDependencyColumn", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectDependencyList;
  	}

  	/**
  	 * Get imported object Assignee.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object assignee.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectAssignee(Context context,String[]args)throws Exception
  	{
  		StringList importObjectAssigneeList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);
  			MapList objectList = (MapList)programMap.get("objectList");

  			int objSize = objectList.size();

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize ==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandAssigneeColumn") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){     		
  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskAssignee = (String)fileMap.get("Assignees");

  					if(ProgramCentralUtil.isNotNullString(taskAssignee)){

  						StringList assigneeList = FrameworkUtil.split(taskAssignee, "|");
  						taskAssignee = DomainObject.EMPTY_STRING;

  						for(int j=0,size=assigneeList.size();j<size;j++){
  	  						String assignee = (String)assigneeList.get(j);
  	  						StringList hiddenAssigneeValueList = FrameworkUtil.split(assignee, ":");
  	  						if("Error".equalsIgnoreCase((String)hiddenAssigneeValueList.get(0))){
  	  							taskAssignee = assignee;	
  	  						}else{
  	  							taskAssignee += (String)hiddenAssigneeValueList.get(0)+";";
  	  						}
  	  					}
  						taskAssignee = taskAssignee.substring(0, taskAssignee.length()-1);
  					}

  					importObjectAssigneeList.addElement(taskAssignee);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandAssigneeColumn", "true");
  			}
  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectAssigneeList;
  	}

  	/**
  	 * Get imported object Description.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object Estimated start date list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectDescriptions(Context context,String[]args)throws Exception
  	{
  		StringList importObjectdescriptionList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);

  			MapList objectList = (MapList)programMap.get("objectList");
  			int objSize = objectList.size();

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandDescriptionColumn") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){  
  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskDescription = (String)fileMap.get("description");
  					importObjectdescriptionList.addElement(taskDescription);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandDescriptionColumn", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectdescriptionList;
  	}

  	/**
  	 * Get imported object Constraint Types.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object Constraint Type list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectConstraintTypes(Context context,String[]args)throws Exception
  	{
  		StringList importObjectConstraintTypeList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);

  			MapList objectList = (MapList)programMap.get("objectList");
  			int objSize = objectList.size();

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandCTColumn") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){  
  				String lang = context.getSession().getLanguage();
  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskConstraintType = (String)fileMap.get("Task Constraint Type");
  					
  					if(ProgramCentralUtil.isNotNullString(taskConstraintType)){
						if(!taskConstraintType.contains("Error"))
  						taskConstraintType = EnoviaResourceBundle.getRangeI18NString(context, ATTRIBUTE_TASK_CONSTRAINT_TYPE, taskConstraintType, lang);
  					importObjectConstraintTypeList.addElement(taskConstraintType);
                    }else {
  						importObjectConstraintTypeList.addElement(EMPTY_STRING);
  					}
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandCTColumn", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectConstraintTypeList;
  	}
  	
  	/**
  	 * Get imported object Constraint Date.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return object Constraint Date list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getImportedObjectConstraintDates(Context context,String[]args)throws Exception
  	{
  		StringList importObjectConstraintDateList = new StringList();

  		try{
  			Map programMap = (Map)JPO.unpackArgs(args);

			Map paramList = (Map)programMap.get("paramList");
  			MapList objectList = (MapList)programMap.get("objectList");
  			int objSize = objectList.size();

			String timeZone = (String) paramList.get("timeZone");
			double clientTZOffset = Task.parseToDouble(timeZone);

  			String isRootNode = DomainObject.EMPTY_STRING;
  			if(objSize==1){
  				Map rootNodeMap = (Map)objectList.get(0);
  				isRootNode = (String)rootNodeMap.get("Root Node");
  			}

  			String isStructureExpanded = (String)CacheUtil.getCacheObject(context, "noMoreExpandCDColumn") ;
  			if(ProgramCentralUtil.isNullString(isStructureExpanded)|| "true".equalsIgnoreCase(isRootNode)){  
  				Locale locale = context.getLocale();
  				for(int i=0;i<objSize;i++){
  					Map fileMap = (Map)objectList.get(i);
  					String taskConstraintDate = (String)fileMap.get("Task Constraint Date");

					if(super.isCorrectValue(context, taskConstraintDate)){
						taskConstraintDate = eMatrixDateFormat.getFormattedDisplayDate(taskConstraintDate, clientTZOffset,locale);
					}

  					importObjectConstraintDateList.addElement(taskConstraintDate);
  				}
  				CacheUtil.setCacheObject(context, "noMoreExpandCDColumn", "true");
  			}

  		}catch(Exception ex){
  			ex.printStackTrace();
  		}
  		return importObjectConstraintDateList;
  	}

  	/**
  	 * Get resource plan template object list.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds the information about object.
  	 * @return resource plan template object list.
  	 * @throws Exception if operation fails.
  	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable
  	public Map getResourcePlanTemplateList(Context context,String[]args)throws Exception
  	{
  		StringList rangeToDisplay = new StringList();
  		StringList range = new StringList();
  		
  		rangeToDisplay.addElement(DomainObject.EMPTY_STRING);
  		range.addElement(DomainObject.EMPTY_STRING);

  		Map programMap = JPO.unpackArgs(args);
  		Map fieldValuesMap = (Map) programMap.get("fieldValues");
  		if(fieldValuesMap != null){
  			String templateObjectId = (String) fieldValuesMap.get("SeachProjectOID");

  			ResourcePlanTemplate resourcePlanTemplteObj = new ResourcePlanTemplate();
  			MapList mlResourceRequest = resourcePlanTemplteObj.getResourceRequestMap(context,templateObjectId);

  			for(Iterator itr = mlResourceRequest.iterator();itr.hasNext();){
  				Map mapResourcePlanTemplate = (Map)itr.next();
  				String strResourceRequestTemplateId   = (String)mapResourcePlanTemplate.get(DomainConstants.SELECT_RELATIONSHIP_ID);
  				String strResourceRequestTemplateName = (String)mapResourcePlanTemplate.get(ResourcePlanTemplate.SELECT_RESOURCE_PLAN_TEMPLATE_NAME);

  				rangeToDisplay.addElement(strResourceRequestTemplateName);
  				range.addElement(strResourceRequestTemplateId);
  			}
  		}

  		Map resourceTemplateRangeMap = new HashMap();
  		resourceTemplateRangeMap.put("RangeValues", range);
  		resourceTemplateRangeMap.put("RangeDisplayValues", rangeToDisplay);

  		return resourceTemplateRangeMap ;
  	}

  	
  	/**
  	 * Used as Reload function in project creation form while creating project from a template.
  	 * It will populate values for "Schedule From" field as per it's value in selected template.
  	 * 
  	 * @param context
  	 * @param args
  	 * @return
  	 * @throws Exception
  	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable
  	public Map getScheduleFromList(Context context,String[]args)throws Exception {
  		StringList rangeToDisplay = new StringList();
  		StringList range = new StringList();
  		String language = context.getSession().getLanguage();
  		String scheduleFromStart = ProgramCentralConstants.ATTRIBUTE_SCHEDULED_FROM_RANGE_START; 
  		String scheduleFromFinish = ProgramCentralConstants.ATTRIBUTE_SCHEDULED_FROM_RANGE_FINISH;		
  		
  		Map programMap = JPO.unpackArgs(args);
  		Map fieldValuesMap = (Map) programMap.get("fieldValues");
  		if(fieldValuesMap != null){
  			String templateId = (String) fieldValuesMap.get("SeachProjectOID");
  			DomainObject template = DomainObject.newInstance(context, templateId);
  			String templateScheduleFrom = template.getInfo(context, "attribute[" + ATTRIBUTE_PROJECT_SCHEDULE_FROM + "].value");
  			String secondScheduleFromType = scheduleFromStart.equalsIgnoreCase(templateScheduleFrom)? scheduleFromFinish : scheduleFromStart;
  			range.add(templateScheduleFrom);
  			range.add(secondScheduleFromType);
  			rangeToDisplay.add(EnoviaResourceBundle.getRangeI18NString(context,ATTRIBUTE_PROJECT_SCHEDULE_FROM, templateScheduleFrom, language));
  			rangeToDisplay.add(EnoviaResourceBundle.getRangeI18NString(context,ATTRIBUTE_PROJECT_SCHEDULE_FROM, secondScheduleFromType, language));
  		}
  		
  		Map scheduleFromInfoMap = new HashMap();
  		scheduleFromInfoMap.put("RangeValues", range);
  		scheduleFromInfoMap.put("RangeDisplayValues", rangeToDisplay);
  		return scheduleFromInfoMap;
  	}
  	
  	
  	/**
  	 * Used as Reload function in project creation form while creating project from a template.
  	 * It will populate values for "Default Constraint Type" field as per it's value in selected template.
  	 *  
  	 * @param context
  	 * @param args
  	 * @return
  	 * @throws Exception
  	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable
  	public Map getDefaultConstraintTypeList(Context context,String[]args)throws Exception {
  		StringList rangeToDisplay = new StringList();
  		StringList range = new StringList();
  		String language = context.getSession().getLanguage();
  		
  		Map programMap = JPO.unpackArgs(args);
  		Map fieldValuesMap = (Map) programMap.get("fieldValues");
  		if(fieldValuesMap != null){
  			String templateConstraintType = ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ASAP;
  			String secondConstraintType = ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ALAP;
  			String templateId = (String) fieldValuesMap.get("SeachProjectOID");
  			DomainObject template = DomainObject.newInstance(context, templateId);
  			String scheduleFrom = template.getInfo(context, "attribute[" + ATTRIBUTE_PROJECT_SCHEDULE_FROM + "].value");
  			if(ProgramCentralConstants.ATTRIBUTE_SCHEDULED_FROM_RANGE_FINISH.equalsIgnoreCase(scheduleFrom)){
  				templateConstraintType = ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ALAP;
  				secondConstraintType = ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ASAP;
  			}
  			range.add(templateConstraintType);
  			range.add(secondConstraintType);
  			rangeToDisplay.add(EnoviaResourceBundle.getRangeI18NString(context,ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE, templateConstraintType, language));
  			rangeToDisplay.add(EnoviaResourceBundle.getRangeI18NString(context,ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE, secondConstraintType, language));
  		}
  		
  		Map constraintTypeInfoMap = new HashMap();
  		constraintTypeInfoMap.put("RangeValues", range);
  		constraintTypeInfoMap.put("RangeDisplayValues", rangeToDisplay);
  		return constraintTypeInfoMap;
  	}
  	
  	
  	
  	/**
  	 * Get question response list.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return questionresponse list.
  	 * @throws Exception if operation fails.
  	 */
  	public StringList getQuestionTaskResponseRangeValues(Context context,String[]args)throws Exception
  	{
  		StringList qrList = new StringList();
  		Map programMap = JPO.unpackArgs(args);
  		MapList objectList = (MapList)programMap.get("objectList");
		String responseGivenByUser = (String) CacheUtil.getCacheObject(context, "dpm_questionResponseCachedData");
		JSONObject userResponseJSONData = new JSONObject();
		if(ProgramCentralUtil.isNotNullString(responseGivenByUser)){
			userResponseJSONData = new JSONObject(responseGivenByUser);
		}
  		String clientLanguage = context.getSession().getLanguage();
  		String i18True = EnoviaResourceBundle.getProperty(context,ProgramCentralConstants.PROGRAMCENTRAL, 
  				"emxProgramCentral.QuestionResponseRange.TRUE", clientLanguage);
  		String i18False = EnoviaResourceBundle.getProperty(context,ProgramCentralConstants.PROGRAMCENTRAL, 
  				"emxProgramCentral.QuestionResponseRange.FALSE", clientLanguage);

  		for(int i=0;i<objectList.size();i++){
  			Map questionMap = (Map)objectList.get(i);
  			String questionId = (String)questionMap.get(DomainObject.SELECT_ID);

  			StringBuilder sb = new StringBuilder();
			String selectedOption = "0";
			if(userResponseJSONData.contains(questionId)){
				selectedOption = (String) ((JSONObject)userResponseJSONData.get(questionId)).get("option").toString();
			}
			
			String selectedTrueString = "selected=\"true\"";
					
					sb.append("<select name=\"QR\" id=\""+questionId+"-response\" class=\"temp-select-class\" onChange=\"getSubQuestionsInTemplate()\">");
					String emptySelectedTrue = (selectedOption.equalsIgnoreCase("1") || selectedOption.equalsIgnoreCase("2"))?"":selectedTrueString;
					String falseSelectedTrue = selectedOption.equalsIgnoreCase("1")?selectedTrueString:"";
					String trueSelectedTrue = selectedOption.equalsIgnoreCase("2")?selectedTrueString:"";
					//sb.append("<option value=\"\" "+emptySelectedTrue+" disabled=\"disabled\">");
					sb.append("<option value=\"\" "+emptySelectedTrue+">");
					sb.append("");
					sb.append("</option>");

					sb.append("<option value=\""+questionId+"|FALSE\" "+falseSelectedTrue+" >");
  			sb.append(i18False);
  			sb.append("</option>");

					sb.append("<option value=\""+questionId+"|TRUE\" "+trueSelectedTrue+">");
  			sb.append(i18True);
  			sb.append("</option>");

  			sb.append("</select>");
  			
  			qrList.addElement(sb.toString());
  		}

  		return qrList;
  	}

  	/**
  	 * Get active question object list.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return active question object list.
  	 * @throws Exception if operation fails.
  	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable  
  	public MapList getActiveQuestionList(Context context,String[]args)throws Exception
  	{
  		com.matrixone.apps.program.Question question =
  				(com.matrixone.apps.program.Question) DomainObject.newInstance(context,
  						DomainConstants.TYPE_QUESTION, "PROGRAM");
  		com.matrixone.apps.program.ProjectTemplate projectTemplate =
  				(com.matrixone.apps.program.ProjectTemplate) DomainObject.newInstance(context,
  						DomainConstants.TYPE_PROJECT_TEMPLATE, "PROGRAM");

  		MapList questionMapList = new MapList();

  		String QuestionType = question.TYPE_QUESTION;
  		String sPolicyName = question.getDefaultPolicy(context);
  		String ActiveState = PropertyUtil.getSchemaProperty(context,"policy",sPolicyName,"state_Active");

  		Map programMap = JPO.unpackArgs(args);
  		String selectedProjectId = (String) programMap.get("objectId");
  		StringList SelectedProjectList = FrameworkUtil.split(selectedProjectId, "|");
		String[] SelectedIds = new String[SelectedProjectList.size()];
		SelectedProjectList.toArray(SelectedIds);
		
			StringList objectSelects = new StringList();  
			objectSelects.add(DomainConstants.SELECT_ID);
		objectSelects.add(ProgramCentralConstants.SELECT_IS_PROJECT_TEMPLATE);
			
		MapList objectInfoList = DomainObject.getInfo(context, SelectedIds, objectSelects);
		
		StringList selectedTemplateIdList = new StringList();
		
		StringList busSelects = new StringList(1);
			StringList relSelects = new StringList(1);
	  		busSelects.add(DomainConstants.SELECT_ID);
	  		//busSelects.add("relationship.id");
	  		busSelects.add(question.SELECT_NAME);
	  		busSelects.add(question.SELECT_DESCRIPTION);
	  		busSelects.add(question.SELECT_HAS_ITEMS);
	  		//busSelects.add(SELECT_QUE_TASK_TRANSFER);
	  		relSelects.add("attribute[Task Transfer]");
			
		//Iterate through each selected project/project Template and get only Project template Id's.
		for (int i= 0,size = objectInfoList.size(); i<size; i++ ) {
			Map projectInfo = (Map) objectInfoList.get(i);			
			
			if("TRUE".equalsIgnoreCase((String) projectInfo.get(ProgramCentralConstants.SELECT_IS_PROJECT_TEMPLATE))){
				selectedTemplateIdList.add((String) projectInfo.get(DomainConstants.SELECT_ID));
			}
			else{
				DomainObject tempObjectQue = new DomainObject((String) projectInfo.get("id"));
				if(tempObjectQue.isKindOf(context,DomainConstants.TYPE_QUESTION)){
					
	  		
	  		String busWhere =  question.SELECT_HAS_ITEMS + "== True&&current==" + ActiveState;
	
					Question tempQueObj = new Question((String) projectInfo.get("id"));
					MapList questionList = tempObjectQue.getRelatedObjects(
                               context,
                               RELATIONSHIP_QUESTION,
                               DomainConstants.TYPE_QUESTION,
                               busSelects,
                               //DomainObject.EMPTY_STRINGLIST,
							   relSelects,
                               false,
                               true,
                               (short) 1,
                               null,
                               null);
		
					questionMapList.addAll(questionList);
				}
			}
		}
		
		if(selectedTemplateIdList.size()>0){
	  		
	  		String busWhere =  question.SELECT_HAS_ITEMS + "== True&&current==" + ActiveState;
	
			for(int i=0; i < selectedTemplateIdList.size(); i++){
				String templateId = selectedTemplateIdList.get(i);
		  		projectTemplate.setId(templateId);
		
		  		MapList questionList = question.getQuestions(context, 
		  				projectTemplate, 
		  				busSelects, 
		  				relSelects,
		  				busWhere,
		  				DomainObject.EMPTY_STRING);
		
		  		questionMapList.addAll(questionList);
			}
		}
		
		return questionMapList;
  	}

  	/**
  	 * Get preview button for predict WBS field. 
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return preview button.
  	 * @throws MatrixException if operation fails.
  	 */
  	public String getTemplatePredictWBSField(Context context, String[] args) throws MatrixException
  	{ 
  		String previewButtonLevel = EnoviaResourceBundle.getProperty(context,ProgramCentralConstants.PROGRAMCENTRAL, 
  				"emxProgramCentral.Common.Project.Preview", context.getSession().getLanguage());
  		
  		StringBuilder sb = new StringBuilder();
  		sb.append("<input type=\"button\" id=\"predictWBS\" name=\"predictWBS\" value=\""+previewButtonLevel+"\" onClick=\"javascript=predictTemplateWBS()\"/>");

  		return sb.toString();
  	}
  	
  	/**
  	 * Get template WBS task list based on question response.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return template wbs tasklist.
  	 * @throws Exception if operation fails.
  	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable  
  	public MapList getTemplateWBS(Context context,String[]args)throws Exception
  	{
  		com.matrixone.apps.common.Task task = (com.matrixone.apps.common.Task) 
  				DomainObject.newInstance(context, TYPE_TASK);

  		Map programMap = JPO.unpackArgs(args);
  		String templateId = (String) programMap.get("objectId");
  		task.setId(templateId);

  		StringList busSelects = new StringList(10);
  		busSelects.add(SELECT_ID);
  		busSelects.add(SELECT_TYPE);
  		busSelects.add(SELECT_NAME);
  		busSelects.add(SELECT_POLICY);
  		busSelects.add(SELECT_DESCRIPTION);
  		busSelects.add(SELECT_PREDECESSOR_IDS);
  		busSelects.add(SELECT_PREDECESSOR_TYPES);
  		busSelects.add(SELECT_PREDECESSOR_DURATION_KEYWORD);
  		busSelects.add(SELECT_DELIVERABLE_IDS);
  		busSelects.add(SELECT_DELIVERABLE_HAS_FILE);
  		busSelects.add(SELECT_PREDECESSOR_LAG_TIMES);
  		busSelects.add(DELIVRERABLE_SELECT_IS_VERSION_OBJECT);
  		busSelects.add(SELECT_TASK_TRANSFER);
  		busSelects.add(SELECT_QUESTION_ID);
  		busSelects.add(SELECT_HAS_SUBTASK);

  		MapList mapList = Task.getTasks(context, task, 1, busSelects, null, false, false);
  		mapList.sort(DomainObject.ATTRIBUTE_SEQUENCE_ORDER, ProgramCentralConstants.ASCENDING_SORT, ProgramCentralConstants.SORTTYPE_INTEGER);

  		String questionResponse = (String) CacheUtil.getCacheObject(context, "QuestionsResponse");
  		Map <String,String>questionResponseMap = new HashMap<String,String>();
  		if(ProgramCentralUtil.isNotNullString(questionResponse)){
  			StringList questionResponseValueList = FrameworkUtil.split(questionResponse, "|");
  			for(int i=0;i<questionResponseValueList.size();i++){
  				String questionRValue = (String)questionResponseValueList.get(i);
  				StringList questionActualRList = FrameworkUtil.split(questionRValue, "=");
  				questionResponseMap.put((String)questionActualRList.get(0), (String)questionActualRList.get(1));
  			}
  		}
  		
  		MapList finalTaskList = new MapList();
  		for(int i=0;i<mapList.size();i++){
  			Map taskInfoMap = (Map)mapList.get(i);
  			String questionId = taskInfoMap.get(SELECT_QUESTION_ID) instanceof String ? (String) taskInfoMap.get(SELECT_QUESTION_ID) : "";
  			StringList slquestionId = taskInfoMap.get(SELECT_QUESTION_ID) instanceof StringList ? (StringList) taskInfoMap.get(SELECT_QUESTION_ID) : new StringList();
  			String taskTransfer = taskInfoMap.get(SELECT_TASK_TRANSFER) instanceof String ? (String) taskInfoMap.get(SELECT_TASK_TRANSFER) : "";
  			StringList sltaskTransfer =  taskInfoMap.get(SELECT_TASK_TRANSFER) instanceof StringList ? (StringList) taskInfoMap.get(SELECT_TASK_TRANSFER):new StringList();
  			String hasSubProject = (String) taskInfoMap.get(SELECT_TYPE);
  			String hasSubtask = (String) taskInfoMap.get(SELECT_HAS_SUBTASK);
  			
			if(hasSubProject.equalsIgnoreCase(TYPE_PROJECT_SPACE)){
				continue;
			}
			if("FALSE".equalsIgnoreCase(hasSubtask)){
				taskInfoMap.put("hasChildren", "false");
			}

  			if(ProgramCentralUtil.isNotNullString(taskTransfer)){
  				String questionResponseValue = questionResponseMap.get(questionId);
  				if(taskTransfer.equalsIgnoreCase(questionResponseValue)){
  					finalTaskList.add(taskInfoMap);
  				}
  			}
  			else if(sltaskTransfer.size()>0 && slquestionId.size()>0) {
  				for(int itr=0; itr<sltaskTransfer.size() && itr<slquestionId.size(); itr++) {
  					String questionIdTemp = slquestionId.get(itr);
  					String questionResponseValue = questionResponseMap.get(questionIdTemp);
  					String taskTransferResponse = sltaskTransfer.get(itr);
  	  				if(taskTransferResponse.equalsIgnoreCase(questionResponseValue)){
  	  					if(!finalTaskList.contains(taskInfoMap))
  	  						finalTaskList.add(taskInfoMap);
  	  				}
  				}
  			}
  			else{
  				finalTaskList.add(taskInfoMap);
  			}
  		}

  		return finalTaskList;
  	}
  	
  	/**
  	 * Exclude experiment project from search list.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about objects.
  	 * @return experiment Id list.
  	 * @throws Exception if operation fails.
  	 */
  	@com.matrixone.apps.framework.ui.ExcludeOIDProgramCallable
	public StringList excludeExperimentProject(Context context,String[]args)throws Exception
	{
		StringList slFinalList = new StringList();
		try{
			StringList slSelect = new StringList(2);
			slSelect.addElement(DomainObject.SELECT_ID);
			slSelect.add(ProgramCentralConstants.SELECT_KINDOF_EXPERIMENT_PROJECT);
			slSelect.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_BASELINE);
			
			String typePattern = ProgramCentralConstants.TYPE_EXPERIMENT+","+ProgramCentralConstants.TYPE_PROJECT_BASELINE;			
			MapList mpProjectList = DomainObject.findObjects(context,
					typePattern,
					null,
					null,
					slSelect);                                   

			for (Iterator iterator = mpProjectList.iterator(); iterator.hasNext();) {
				Map projectInfoMap = (Map) iterator.next();
				String objectId = (String)projectInfoMap.get(DomainObject.SELECT_ID);				
				String isExpProject = (String)projectInfoMap.get(ProgramCentralConstants.SELECT_KINDOF_EXPERIMENT_PROJECT);
				String isProjectBaseline = (String)projectInfoMap.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_BASELINE);
				
				if("true".equalsIgnoreCase(isExpProject) || "true".equalsIgnoreCase(isProjectBaseline)){
					slFinalList.addElement(objectId);
				}
			}
			
			return slFinalList;
		}catch(Exception e){
			e.printStackTrace();
			throw new MatrixException(e);
		}
	}

	//PSR: Gantt: Start
	public StringBuffer[] getDataGanttChart(Context context, String[] args) throws Exception {
	 StringBuffer[] aData        = new StringBuffer[8];     
	 /*commented this method as there are no references
		String sColorRed        = "CC092F";
		String sColorGreen      = "6FBC4B";
		String sColorOrange     = "FF8A2E";
		String sColorGray       = "7F7F7F";
		String sColor025        = "d5e8f2";
		String sColor050        = "78befa";
		String sColor075        = "368ec4";
		String sColor099        = "005686";
		String sColor100        = "003c5a";      

		HashMap paramMap            = (HashMap) JPO.unpackArgs(args);
		String sOID                 = (String) paramMap.get("objectId");          
		String sExpandLevels        = (String) paramMap.get("expandLevels");           
		Short levels                = Short.parseShort(sExpandLevels);      
		StringBuffer sbCategories   = new StringBuffer();
		StringBuffer sbTarget       = new StringBuffer();
		StringBuffer sbActual       = new StringBuffer();
		StringBuffer sbGates        = new StringBuffer();        
		StringBuffer sbEvents       = new StringBuffer();        
		StringBuffer sbPopups       = new StringBuffer();        
		StringBuffer sbTaskDetails  = new StringBuffer();        
		StringBuffer sbHeight       = new StringBuffer();

		DomainObject doProject = new DomainObject(sOID);

		StringList busSelects = new StringList();
		StringList relSelects = new StringList();
		busSelects.add("type");
		busSelects.add("id");
		busSelects.add("name");
		busSelects.add("policy");
		busSelects.add("current");
		busSelects.add("owner");
		busSelects.add("attribute[Schedule Duration Units]");
		busSelects.add("attribute[Task Estimated Duration]");
		busSelects.add("attribute[Task Estimated Start Date]");
		busSelects.add("attribute[Task Estimated Finish Date]");
		busSelects.add("attribute[Task Actual Duration]");
		busSelects.add("attribute[Task Actual Start Date]");
		busSelects.add("attribute[Task Actual Finish Date]");
		busSelects.add("attribute[Task Estimated Duration]");
		busSelects.add("attribute[Percent Complete]");
		busSelects.add("from[Task Deliverable].to.name");
		relSelects.add("attribute[Sequence Order]");		

		StringList slSelectsPerson = new StringList();
		slSelectsPerson.add("attribute[First Name]");
		slSelectsPerson.add("attribute[Last Name]");
		slSelectsPerson.add("attribute[Work Phone Number]");

		MapList mlTasks = doProject.getRelatedObjects(context, "Subtask", "Task Management", busSelects, relSelects, false, true, levels, "", "", 10000);
		mlTasks.sort("attribute[Sequence Order]", "ascending", "integer");
		// Append current date
		Calendar cDate = Calendar.getInstance();		
		sbGates.append("{ color: '#333',");
		sbGates.append("width: 3,");
		sbGates.append("value: Date.UTC(").append(cDate.get(Calendar.YEAR)).append(",").append(cDate.get(Calendar.MONTH)).append(",").append(cDate.get(Calendar.DAY_OF_MONTH)).append("),");
		sbGates.append("label: {");
		sbGates.append("text: 'Today',");
		sbGates.append("style: {");
		sbGates.append("color: '#333',");
		sbGates.append("fontWeight: 'normal'");
		sbGates.append("}}}");

		if(mlTasks.size() > 0) {

			for (int i = 0; i < mlTasks.size(); i++) {

				Map mTask 	= (Map)mlTasks.get(i);                
				String sType 	= (String)mTask.get("type");
				String sName 	= (String)mTask.get("name");

				if(sType.equals("Gate")) { 

					String sDate 	= (String)mTask.get("attribute[Task Estimated Finish Date]");
					cDate.setTime(sdf.parse(sDate));
					if(sbGates.length() > 0) { sbGates.append(","); }
					sbGates.append("{ color: '#").append(sColorRed).append("',");
					sbGates.append("width: 3,");
					sbGates.append("value: Date.UTC(").append(cDate.get(Calendar.YEAR)).append(",").append(cDate.get(Calendar.MONTH)).append(",").append(cDate.get(Calendar.DAY_OF_MONTH)).append("),");
					sbGates.append("label: {");
					sbGates.append("text: '").append(sName).append("',");
					sbGates.append("style: {");
					sbGates.append("color: '#").append(sColorRed).append("',");
					sbGates.append("fontWeight: 'normal'");
					sbGates.append("}}}");

					mlTasks.remove(i);

				} else {

					String sOIDTask         = (String)mTask.get("id");
					String sCurrent         = (String)mTask.get("current");
					String sOwner           = (String)mTask.get("owner");
					String sTargetStart     = (String)mTask.get("attribute[Task Estimated Start Date]");
					String sTargetEnd       = (String)mTask.get("attribute[Task Estimated Finish Date]");    
					String sTargetDuration  = (String)mTask.get("attribute[Task Estimated Duration]");
					String sActualStart     = (String)mTask.get("attribute[Task Actual Start Date]");
					String sActualEnd       = (String)mTask.get("attribute[Task Actual Finish Date]");
					String sActualDuration  = (String)mTask.get("attribute[Task Actual Duration]");
					String sUnits           = (String)mTask.get("attribute[Schedule Duration Units]");
					String sPercent         = (String)mTask.get("attribute[Percent Complete]");

					Calendar cTargetStart   = Calendar.getInstance();
					Calendar cTargetEnd     = Calendar.getInstance();
					Calendar cActualStart   = Calendar.getInstance();
					Calendar cActualEnd     = Calendar.getInstance();
					cTargetStart.setTime(sdf.parse(sTargetStart));	
					cTargetEnd.setTime(sdf.parse(sTargetEnd));	



					// List of Categories                    
					if(sbCategories.length() > 0) { sbCategories.append(","); }
					sbCategories.append("'").append(sName).append("'");

					// Target Bars
					String sTargetColor = sColor025;                                       
					//                    double dPercent = Task.parseToDouble(sPercent);
					//                    if(dPercent <= 25.0)        { sTargetColor = sColor025; }
					//                    else if(dPercent <= 50.0)   { sTargetColor = sColor050; }
					//                    else if(dPercent <= 75.0)   { sTargetColor = sColor075; }
					//                    else if(dPercent < 100.0)   { sTargetColor = sColor099; } 

					if(sCurrent.equals("Active"))           { sTargetColor = sColor050; }
					else if(sCurrent.equals("Review"))      { sTargetColor = sColor099; }
					else if(sCurrent.equals("Complete"))    { sTargetColor = sColor100; }

					if(sbTarget.length() > 0) { sbTarget.append(","); }
					sbTarget.append("{");
					sbTarget.append("   id : '").append(sOIDTask).append("',");
					sbTarget.append("   name : '").append(sName).append("',");                    
					sbTarget.append("   type : '").append(sType).append("',");                    
					sbTarget.append("   info : '").append(sCurrent).append(" (").append(sPercent).append("%)',");                    
					sbTarget.append("   color : '#").append(sTargetColor).append("',");
					sbTarget.append("   low:Date.UTC(").append(cTargetStart.get(Calendar.YEAR)).append(",").append(cTargetStart.get(Calendar.MONTH)).append(",").append(cTargetStart.get(Calendar.DAY_OF_MONTH)).append("),");
					sbTarget.append("   high:Date.UTC(").append(cTargetEnd.get(Calendar.YEAR)).append(",").append(cTargetEnd.get(Calendar.MONTH)).append(",").append(cTargetEnd.get(Calendar.DAY_OF_MONTH)).append(")");
					sbTarget.append("}");

					// Actual Bars
					String sActualColor = sColorGray; 
					if(sbActual.length() > 0) { sbActual.append(","); }
					sbActual.append("{");
					sbActual.append("   id : '").append(sOIDTask).append("',");
					sbActual.append("   name : '").append(sName).append("',");
					sbActual.append("   type : '").append(sType).append("',");
					sbActual.append("   info : '").append(sCurrent).append(" (").append(sPercent).append("%)',");
					if(null != sActualStart) {
						if(!"".equals(sActualStart)) {
							cActualStart.setTime(sdf.parse(sActualStart));
							if(sCurrent.equals("Complete")) {
								if(null != sActualEnd) {
									if(!"".equals(sActualEnd)) {
										cActualEnd.setTime(sdf.parse(sActualEnd));
										if(cActualEnd.after(cTargetEnd)) { sActualColor = sColorRed; }
										else { sActualColor = sColorGreen; }
									}
								}
							} else {
								if((null != sActualEnd) && (!"".equals(sActualEnd))) {
									cActualEnd.setTime(sdf.parse(sActualEnd));                                    
								} else {
									cActualEnd.setTime(sdf.parse(sTargetEnd));
									Long lTarget = cTargetStart.getTimeInMillis();
									Long lActual = cActualStart.getTimeInMillis();
									Long lDiff = lActual - lTarget;
									Long lDiffDays = lDiff / (24*60*60*1000);
									Integer iDiff = lDiffDays.intValue();
									cActualEnd.add(java.util.GregorianCalendar.DAY_OF_YEAR, iDiff);
								}
								if(cActualStart.after(cTargetStart)) { sActualColor = sColorOrange; }
								else if(cActualStart.before(cDate)) { sActualColor = sColorOrange; }
								else { sActualColor = sColorGray; }
							}

							sbActual.append("   color : '#").append(sActualColor).append("',");
							sbActual.append("   low:Date.UTC(").append(cActualStart.get(Calendar.YEAR)).append(",").append(cActualStart.get(Calendar.MONTH)).append(",").append(cActualStart.get(Calendar.DAY_OF_MONTH)).append("),");
							sbActual.append("   high:Date.UTC(").append(cActualEnd.get(Calendar.YEAR)).append(",").append(cActualEnd.get(Calendar.MONTH)).append(",").append(cActualEnd.get(Calendar.DAY_OF_MONTH)).append(")");

						}
					}                    
					sbActual.append("}");

					// List of Events and Popups                 
					sbEvents.append("if(this.id == '").append(sOIDTask).append("') { App.popUp").append(i).append(".open(); App.popUp").append(i).append(".positionTo(document.body, 50, 50);}");
					sbPopups.append("App.popUp").append(i).append("= new PopUpWindow('").append(sName).append("', { contentDiv: '").append(sOIDTask).append("', isResizable: true, width: 300 });");


					// Task Details                  
					if(!sUnits.equals("")) { sUnits = " " + sUnits; }
					if(sTargetStart.contains(" ")) { sTargetStart = sTargetStart.substring(0, sTargetStart.indexOf(" ")); }
					if(sTargetEnd.contains(" ")) { sTargetEnd = sTargetEnd.substring(0, sTargetEnd.indexOf(" ")); }                
					if(null == sActualDuration) { sActualDuration = ""; }
					if(null != sActualStart) {if(sActualStart.contains(" ")) { sActualStart = sActualStart.substring(0, sActualStart.indexOf(" ")); }} else { sActualStart = ""; }
					if(null != sActualEnd) {if(sActualEnd.contains(" ")) { sActualEnd = sActualEnd.substring(0, sActualEnd.indexOf(" ")); }} else { sActualEnd = ""; sActualDuration = "";}                    

					String sActual  = sActualStart + " - " + sActualEnd + " (" + sActualDuration + sUnits + ")";
					String sTarget  = sTargetStart + " - " + sTargetEnd + " (" + sTargetDuration + sUnits + ")";                    
					String sMail    = com.matrixone.apps.domain.util.PersonUtil.getEmail(context, sOwner);

					String sOIDPerson = com.matrixone.apps.domain.util.PersonUtil.getPersonObjectID(context, sOwner);
					DomainObject doPerson = new DomainObject(sOIDPerson);

					Map mDataPerson = doPerson.getInfo(context, slSelectsPerson);

					String sPhone       = (String)mDataPerson.get("attribute[Work Phone Number]");    
					String sFirstName   = (String)mDataPerson.get("attribute[First Name]");    
					String sLastName    = (String)mDataPerson.get("attribute[Last Name]");    
					String sPerson      = sFirstName + " " + sLastName;

					String sDeliverables = "1";
					if(mTask.get("from[Task Deliverable].to.name") == null) { sDeliverables = "none"; }
					else  if(mTask.get("from[Task Deliverable].to.name") instanceof StringList) {	
						StringList slDeliverables = (StringList)mTask.get("from[Task Deliverable].to.name");
						sDeliverables = String.valueOf(slDeliverables.size());
					} 

					sDeliverables = "<a href='#' onClick=\"javascript:window.open('../common/emxTree.jsp?DefaultCategory=PMCDeliverableCommandPowerView&objectId=" + sOIDTask + "','', 'height=650,width=950,toolbar=no,directories=no,status=no,menubar=no;return false;')\" >" + sDeliverables + "</a>";


					sbTaskDetails.append("<div id='").append(sOIDTask).append("'>");
					sbTaskDetails.append("<table>");
					sbTaskDetails.append("<tr><td style='text-align:right'><strong>Activity</strong></td><td width='3px'></td><td>").append(sType).append(" <a href='#' onClick=\"javascript:window.open('../common/emxTree.jsp?objectId=").append(sOIDTask).append("','', 'height=650,width=950,toolbar=no,directories=no,status=no,menubar=no;return false;')\">").append(sName).append("</a>").append("</td></tr>");
					sbTaskDetails.append("<tr><td style='text-align:right'><strong>Owner</strong></td><td width='3px'></td><td>").append(sPerson).append("</td></tr>");
					sbTaskDetails.append("<tr><td style='text-align:right'><strong>Mail</strong></td><td width='3px'></td><td><a href='mailto:").append(sMail).append("?subject=(Your task) ").append(sName).append("'>").append(sMail).append("</a>").append("</td></tr>");
					sbTaskDetails.append("<tr><td style='text-align:right'><strong>Phone</strong></td><td width='3px'></td><td>").append(sPhone).append("</td></tr>");
					sbTaskDetails.append("<tr><td style='text-align:right'><strong>Plan</strong></td><td width='3px'></td><td>").append(sTarget).append("</td></tr>");
					sbTaskDetails.append("<tr><td style='text-align:right'><strong>Actual</strong></td><td width='3px'></td><td>").append(sActual).append("</td></tr>");                    
					sbTaskDetails.append("<tr><td style='text-align:right'><strong>Status</strong></td><td width='3px'></td><td>").append(sCurrent).append(" (").append(sPercent).append("%)</td></tr>");
					sbTaskDetails.append("<tr><td style='text-align:right'><strong>Deliverables</strong></td><td width='3px'></td><td>").append(sDeliverables).append("</td></tr>");
					sbTaskDetails.append("</table>");
					sbTaskDetails.append("</div>");

				}			


			}
		}	

		// DeterestimatedDatee Chart Height
		int iHeightGantt = 40;	
		iHeightGantt += mlTasks.size() * 30;
		sbHeight.append(String.valueOf(iHeightGantt));

		aData[0] = sbCategories;
		aData[1] = sbTarget;
		aData[2] = sbActual;
		aData[3] = sbGates;
		aData[4] = sbEvents;
		aData[5] = sbTaskDetails;
		aData[6] = sbPopups;
		aData[7] = sbHeight;
*/
		return aData;

	}      
	
	@com.matrixone.apps.framework.ui.ProgramCallable
	public MapList getPendingTasksOfProject(Context context, String[] args) throws Exception {
MapList mlResult        = new MapList();
/*
		HashMap paramMap    = (HashMap) JPO.unpackArgs(args);
		String sOID         = (String) paramMap.get("objectId");
		String sMode        = (String) paramMap.get("mode");

		return retrieveOpenTasksOfProject(context, args, sMode, sOID);          
		*/
		return mlResult;
	} 

	@com.matrixone.apps.framework.ui.ProgramCallable
	public MapList getPendingCriticalTasksOfProject(Context context, String[] args) throws Exception {
MapList mlResult        = new MapList();
/*
		HashMap paramMap    = (HashMap) JPO.unpackArgs(args);
		String sOID         = (String) paramMap.get("objectId");
		String sMode        = (String) paramMap.get("mode");

		return getOpenCriticalTask(context, args, sMode, sOID);          
		*/
		return mlResult;
	} 
	public MapList retrieveOpenTasksOfProject(Context context, String[] args, String sMode, String sOIDProject) throws Exception {
		MapList mlResult        = new MapList();
		/*
		DomainObject doProject  = new DomainObject(sOIDProject);
		Calendar cal            = Calendar.getInstance(TimeZone.getDefault());
		Calendar cCurrent       = Calendar.getInstance();
		int iYearCurrent        = cCurrent.get(Calendar.YEAR);
		int iMonthCurrent       = cCurrent.get(Calendar.MONTH);
		int iWeekCurrent        = cCurrent.get(Calendar.WEEK_OF_YEAR);        
		long lCurrent           = cCurrent.getTimeInMillis();
		long lDiff              = 2592000000L;   

		if(null == sMode) { sMode = "All"; }        

		StringList busSelects = new StringList();
		StringList relSelects = new StringList();
		busSelects.add("id");
		busSelects.add("type");
		busSelects.add("name");
		busSelects.add("policy");
		busSelects.add("current");
		busSelects.add("owner");
		busSelects.add("attribute[Task Estimated Finish Date]");
		busSelects.add("from[Subtask]");                           

		MapList mlTasks = doProject.getRelatedObjects(context, "Program Project,Subtask", "Project Space,Task Management", busSelects, relSelects, false, true, (short) 0, "", "", 0);

		if (mlTasks.size() > 0) {

			for (int j = 0; j < mlTasks.size(); j++) {

				Map mTask = (Map) mlTasks.get(j);
				String sCurrent     = (String) mTask.get("current");
				String sType        = (String) mTask.get("type");
				String sIsLeaf      = (String) mTask.get("from[Subtask]");
				String sOIDResult   = (String) mTask.get("id");
				String sOwner       = (String) mTask.get("owner");

				if (sType.equals("Task")) {
					if (!sCurrent.equals("Complete")) {
						if (sIsLeaf.equalsIgnoreCase("FALSE")) {

							Map mResult = new HashMap();
							mResult.put("id"        , sOIDResult    );
							mResult.put("owner"     , sOwner        );
							mResult.put("current"   , sCurrent      );

							if (sMode.equals("All")) {
								mlResult.add(mResult);
							} else if (sMode.equals("Overdue")) {
								String sTargetDate = (String) mTask.get("attribute[Task Estimated Finish Date]");
								if (sTargetDate != null && !"".equals(sTargetDate)) {

									Calendar cTarget = Calendar.getInstance();
									cTarget.setTime(sdf.parse(sTargetDate));

									if (cTarget.before(cal)) {
										mlResult.add(mResult);
									}
								}                                
							} else if (sMode.equals("This Week")) {
								String sTargetDate = (String) mTask.get("attribute[Task Estimated Finish Date]");
								if (sTargetDate != null && !"".equals(sTargetDate)) {

									Calendar cTarget = Calendar.getInstance();
									cTarget.setTime(sdf.parse(sTargetDate));

									int iYearTarget = cTarget.get(Calendar.YEAR);
									int iWeekTarget = cTarget.get(Calendar.WEEK_OF_YEAR);

									if (iYearCurrent == iYearTarget) {
										if (iWeekCurrent == iWeekTarget) {
											mlResult.add(mResult);
										}
									}
								}
							} else if (sMode.equals("This Month")) {
								String sTargetDate = (String) mTask.get("attribute[Task Estimated Finish Date]");
								if (sTargetDate != null && !"".equals(sTargetDate)) {

									Calendar cTarget = Calendar.getInstance();
									cTarget.setTime(sdf.parse(sTargetDate));

									int iYearTarget = cTarget.get(Calendar.YEAR);
									int iMonthTarget = cTarget.get(Calendar.MONTH);

									if (iYearCurrent == iYearTarget) {
										if (iMonthCurrent == iMonthTarget) {
											mlResult.add(mResult);
										}
									}
								}
							} else if (sMode.equals("Soon")) {
								String sTargetDate = (String) mTask.get("attribute[Task Estimated Finish Date]");
								if (sTargetDate != null && !"".equals(sTargetDate)) {

									Calendar cTarget = Calendar.getInstance();
									cTarget.setTime(sdf.parse(sTargetDate));
									long lTarget        = cTarget.getTimeInMillis();	

									if ((lTarget - lCurrent) < lDiff) {
										if ((lTarget - lCurrent) > 0) {
											mlResult.add(mResult);
										}	                    
									}	    
								}
							}                             
						}
					}
				}
			}
		}
*/
		return mlResult;
	}

	public MapList getOpenCriticalTask(Context context, String[] args, String sMode, String sOIDProject) throws Exception {
		MapList mlResult        = new MapList();
		/*
		DomainObject doProject  = new DomainObject(sOIDProject);
		Calendar cal            = Calendar.getInstance(TimeZone.getDefault());
		Calendar cCurrent       = Calendar.getInstance();
		int iYearCurrent        = cCurrent.get(Calendar.YEAR);
		int iMonthCurrent       = cCurrent.get(Calendar.MONTH);
		int iWeekCurrent        = cCurrent.get(Calendar.WEEK_OF_YEAR);        
		long lCurrent           = cCurrent.getTimeInMillis();
		long lDiff              = 2592000000L;   

		if(null == sMode) { sMode = "All"; }        

		StringList busSelects = new StringList();
		StringList relSelects = new StringList();
		busSelects.add(ProgramCentralConstants.SELECT_ID);
		busSelects.add(ProgramCentralConstants.SELECT_TYPE);
		busSelects.add(ProgramCentralConstants.SELECT_NAME);
		busSelects.add(ProgramCentralConstants.SELECT_POLICY);
		busSelects.add(ProgramCentralConstants.SELECT_CURRENT);
		busSelects.add(ProgramCentralConstants.SELECT_OWNER);
		busSelects.add(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
		busSelects.add(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ACTUAL_FINISH_DATE);
		busSelects.add(Task.SELECT_CRITICAL_TASK);
		busSelects.add("from[Subtask]");                           

		//MapList mlTasks = doProject.getRelatedObjects(context, "Program Project,Subtask", "Project Space,Task Management", busSelects, relSelects, false, true, (short) 0, "", "", 0);
		MapList mlTasks = doProject.getRelatedObjects(context, ProgramCentralConstants.RELATIONSHIP_SUBTASK, 
				ProgramCentralConstants.TYPE_TASK_MANAGEMENT, busSelects, relSelects, false, true, (short) 1,
				"", null, 0);


		if (mlTasks.size() > 0) {

			for (int j = 0; j < mlTasks.size(); j++) {

				Map mTask = (Map) mlTasks.get(j);
				String sCurrent     	= (String) mTask.get(ProgramCentralConstants.SELECT_CURRENT);
				String sType        	= (String) mTask.get(ProgramCentralConstants.SELECT_TYPE);
				String sIsLeaf      	= (String) mTask.get("from[Subtask]");
				String sOIDResult   	= (String) mTask.get(ProgramCentralConstants.SELECT_ID);
				String sOwner       	= (String) mTask.get(ProgramCentralConstants.SELECT_OWNER);
				String sCriticalTask 	= (String) mTask.get(Task.SELECT_CRITICAL_TASK);
				boolean isTaskCritical = false;
				if("TRUE".equalsIgnoreCase(sCriticalTask)){
					isTaskCritical = true;
				}

				//If task is 1. Critical, 2. Not Completed, 3. A Leaf task, add it in the list
				if (!sCurrent.equals("Complete") && sIsLeaf.equalsIgnoreCase("FALSE") && isTaskCritical) {
						Map mResult = new HashMap();
						mResult.put(ProgramCentralConstants.SELECT_ID, 		sOIDResult    );
						mResult.put(ProgramCentralConstants.SELECT_OWNER, 	sOwner        );
						mResult.put(ProgramCentralConstants.SELECT_CURRENT, sCurrent      );

						if (sMode.equals("All")) {
							mlResult.add(mResult);
						} else if (sMode.equals("Overdue")) {
							String sTargetDate = (String) mTask.get(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
							if (sTargetDate != null && !"".equals(sTargetDate)) {

								Calendar cTarget = Calendar.getInstance();
								cTarget.setTime(sdf.parse(sTargetDate));

								if (cTarget.before(cal)) {
									mlResult.add(mResult);
								}
							}                                
						} else if (sMode.equals("This Week")) {
							String sTargetDate = (String) mTask.get(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
							if (sTargetDate != null && !"".equals(sTargetDate)) {

								Calendar cTarget = Calendar.getInstance();
								cTarget.setTime(sdf.parse(sTargetDate));

								int iYearTarget = cTarget.get(Calendar.YEAR);
								int iWeekTarget = cTarget.get(Calendar.WEEK_OF_YEAR);

								if (iYearCurrent == iYearTarget) {
									if (iWeekCurrent == iWeekTarget) {
										mlResult.add(mResult);
									}
								}
							}
						} else if (sMode.equals("This Month")) {
							String sTargetDate = (String) mTask.get(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
							if (sTargetDate != null && !"".equals(sTargetDate)) {

								Calendar cTarget = Calendar.getInstance();
								cTarget.setTime(sdf.parse(sTargetDate));

								int iYearTarget = cTarget.get(Calendar.YEAR);
								int iMonthTarget = cTarget.get(Calendar.MONTH);

								if (iYearCurrent == iYearTarget) {
									if (iMonthCurrent == iMonthTarget) {
										mlResult.add(mResult);
									}
								}
							}
						} else if (sMode.equals("Soon")) {
							String sTargetDate = (String) mTask.get(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
							if (sTargetDate != null && !"".equals(sTargetDate)) {

								Calendar cTarget = Calendar.getInstance();
								cTarget.setTime(sdf.parse(sTargetDate));
								long lTarget        = cTarget.getTimeInMillis();	

								if ((lTarget - lCurrent) < lDiff) {
									if ((lTarget - lCurrent) > 0) {
										mlResult.add(mResult);
									}	                    
								}	    
							}
						}                             
					}
			}
		}
*/
		return mlResult;
	}

	//PSR: Gantt: End

	/**
  	 * Change Discipline avaliable if ECH is installed.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return boolean value.
  	 * @throws Exception if opeartion fails.
  	 */
  	public boolean isChangeDisciplineFieldEnable(Context contex,String[]args)throws Exception
  	{
		boolean isECHInstalled =  FrameworkUtil.isSuiteRegistered(context,
  				"appVersionEnterpriseChange",false,null,null);
  		boolean isSelectedTypeChangeProjectOrTask = false;
  		boolean isECHFieldVisible = false;
  		boolean fromProgram = isFromProgram(contex, args);
  		if(isECHInstalled){
  			Map programMap = (HashMap) JPO.unpackArgs(args);
  			String projectType = (String)programMap.get("type");
  			
  			if(ProgramCentralUtil.isNotNullString(projectType) && projectType.startsWith("_selectedType")){
				StringList selectedTypeList = FrameworkUtil.split(projectType, ":");
  				selectedTypeList = FrameworkUtil.split((String)selectedTypeList.get(1), ",");
  				String selectedType = (String)selectedTypeList.get(0);
  				
  				if(mxType.isOfParentType(context, selectedType, DomainObject.TYPE_CHANGE_PROJECT) ||
  						mxType.isOfParentType(context, selectedType, DomainObject.TYPE_CHANGE_TASK) ){
  					isSelectedTypeChangeProjectOrTask = true;
  				}
  			}
  			
  			if(isECHInstalled && isSelectedTypeChangeProjectOrTask){
  				isECHFieldVisible = true;
  			}
  		}
  		return (!fromProgram && isECHFieldVisible);
  	}

	/**
  	 * Program field should be Mandatory if ECH installed, hiding the standard Program field in this case.
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return boolean value.
  	 * @throws Exception if opeartion fails.
  	 */
  	public boolean isECHFieldDisable(Context contex,String[]args)throws Exception
  	{
  		boolean isECHFieldDisable = true;
  		isECHFieldDisable = ! isChangeDisciplineFieldEnable(contex, args);
  		
  		return isECHFieldDisable;
  	}
  	
    /**
     * Checks if the object is RACE compliant.
     * @param context the ENOVIA <code>Context</code> object.
     * @param args request arguments
     * @return true if object is RACE compliant
     * @throws MatrixException if operation fails.
     */
    public boolean isRaceCompliant(Context context, String[] args)
    throws MatrixException{
        try{
            Map programMap = (HashMap) JPO.unpackArgs(args);
            String objectId = (String) programMap.get("objectId");
            DomainObject project = DomainObject.newInstance(context, objectId);
            String collabSpace = project.getInfo(context, "altowner2");
            if(ProgramCentralUtil.isNotNullString(collabSpace) && !"GLOBAL".equalsIgnoreCase(collabSpace))
                return true;
            return false;
        }catch(Exception e){
            throw new MatrixException(e);
        }
    }

	/************************Preferences Attribute Functions*******************************/

    @com.matrixone.apps.framework.ui.ProgramCallable
	public Map getDurationUnitAttrRange(Context context, String args[]) throws MatrixException {

        try {
			String language = context.getSession().getLanguage();
			AttributeType attrType = new AttributeType(ATTRIBUTE_TASK_ESTIMATED_DURATION);
			Dimension dimension = attrType.getDimension(context);
			UnitList unitList = new UnitList();
			if (dimension != null) {
				unitList = dimension.getUnits(context);
			}

			//StringList slUnitRange = new StringList();
			StringList slAttributeRange = new StringList();
			StringList slAttributeRangeTranslated = new StringList();
			
			if (!unitList.isEmpty()) {
				for(UnitItr uitr = new UnitItr(unitList); uitr.next();) {
					Unit unit = (Unit) uitr.obj();
					//slUnitRange.add (unit.getLabel());
					slAttributeRange.add (unit.getName());
				}
			}
			for (int i=0;i<slAttributeRange.size();i++){
				String value = (String) slAttributeRange.get(i);
				String strKey = "emxFramework.Range.Dimension." + value;
				String i18nValue = EnoviaResourceBundle.getProperty(context, "Framework", strKey, language);
				slAttributeRangeTranslated.add(i18nValue);
			}
			Map mAttributeRange = new HashMap();
			mAttributeRange.put("field_choices", slAttributeRange);
			mAttributeRange.put("field_display_choices", slAttributeRangeTranslated);
			return mAttributeRange;
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	public String getScheduleDurationUnit(Context context, String args[]) throws MatrixException {
		try {
			Map programMap = (Map) JPO.unpackArgs(args);
			Map paramMap = (Map)programMap.get("paramMap");
			String strId = (String) paramMap.get("objectId");

			String language = context.getSession().getLanguage();
			DomainObject project = DomainObject.newInstance(context, strId);
			String attrVal = project.getInfo(context, ProgramCentralConstants.SELECT_TASK_ESTIMATED_DURATION_INPUTUNIT);
			String sAttributeValue = EnoviaResourceBundle.getProperty(context, "Framework","emxFramework.Range.Dimension." + attrVal, language);
			return sAttributeValue;
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	public void updateScheduleDurationUnit(Context context, String args[]) throws MatrixException {
		try {
			Map programMap = (Map)JPO.unpackArgs(args);
			Map paramMap 		= (Map) programMap.get("paramMap");
			String newUnit = (String) paramMap.get("New Value");
			String projectId = (String)paramMap.get("objectId");
			
			ProjectSpace project = new ProjectSpace(projectId);

			String ATTRIBUTE_TASK_ESTIMATED_DURATION = PropertyUtil.getSchemaProperty(context,"attribute_TaskEstimatedDuration");
			String SELECT_TASK_ESTIMATED_DURATION_BY_UNIT = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_DURATION + "].unitvalue[" + newUnit + "]";
			
			String SELECT_PROJECT_DEFAULT_CALENDAR_WORKTIME = "from[Default Calendar].to.attribute[Working Time Per Day].value";
			String SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE ="attribute[Task Estimated Duration].inputvalue";
			String SELECT_TASK_CALENDAR_WORK_TIME = "from[" + ProgramCentralConstants.RELATIONSHIP_CALENDAR + "].to.attribute[Working Time Per Day].value";
					
			StringList relSelects = new StringList();
			StringList busSelects = new StringList();
			busSelects.add(SELECT_ID);
			busSelects.add(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_DURATION);
			busSelects.add(ProgramCentralConstants.SELECT_TASK_ESTIMATED_DURATION_INPUTUNIT);
			busSelects.add(SELECT_TASK_ESTIMATED_DURATION_BY_UNIT);
			busSelects.add(SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE);
			
			StringList projectSelectable = new StringList();
			projectSelectable.addElement(SELECT_PROJECT_DEFAULT_CALENDAR_WORKTIME);
			projectSelectable.addAll(busSelects);
			
			Map projectInfo = project.getInfo(context, projectSelectable);
			
			StringList taskSelectable = new StringList();
			taskSelectable.add(SELECT_TASK_CALENDAR_WORK_TIME);
			taskSelectable.addAll(busSelects);
			MapList objectList = Task.getTasks(context, 
					project,
					1 , 
					taskSelectable, 
					relSelects);
			objectList.add(projectInfo);
			
			
			DomainObject taskObj = DomainObject.newInstance(context);
			
			for (Object objectMap : objectList) {
				Map<String,Object> taskMap = (Map) objectMap;
				String objId 			= (String) taskMap.get(SELECT_ID);
				String durationUnit 	= (String) taskMap.get(ProgramCentralConstants.SELECT_TASK_ESTIMATED_DURATION_INPUTUNIT);
				
				String workTimePerDay 	= (String) taskMap.get(SELECT_TASK_CALENDAR_WORK_TIME);
				
				workTimePerDay 			= workTimePerDay == null || workTimePerDay.isEmpty()?(String) projectInfo.get(SELECT_PROJECT_DEFAULT_CALENDAR_WORKTIME):workTimePerDay;
				String duration 		= (String) taskMap.get(SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE);
				
				if(!newUnit.equalsIgnoreCase(durationUnit)) {
					double workHoursDay = 0;
					if(workTimePerDay != null && !workTimePerDay.isEmpty()) {
						workHoursDay = Double.valueOf(workTimePerDay);
						workHoursDay = workHoursDay/60;

						if("d".equalsIgnoreCase(newUnit)){
							duration = String.valueOf((Task.parseToDouble(duration)/workHoursDay));
						}else{
							duration = String.valueOf((Task.parseToDouble(duration)*workHoursDay));	
						}
						
					}else {
						duration = (String) taskMap.get(SELECT_TASK_ESTIMATED_DURATION_BY_UNIT);
					}
					
					taskObj.setId(objId);
					taskObj.setAttributeValue(context, 
							ATTRIBUTE_TASK_ESTIMATED_DURATION, 
							duration + ProgramCentralConstants.SPACE + newUnit);
				}
			}
			
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}
	
	public String getSendReminderAttrValue(Context context, String args[]) throws MatrixException {
		try {
			Map programMap = (Map) JPO.unpackArgs(args);
			Map paramMap = (Map)programMap.get("paramMap");
			String strId = (String) paramMap.get("objectId");
			String language = context.getSession().getLanguage();
			
			DomainObject project = DomainObject.newInstance(context, strId);
			String sProjectSendReminderValue = project.getInfo(context,ProgramCentralConstants.SELECT_ATTRIBUTE_SEND_REMINDER);

			String propertyKey = "emxProgramCentral.ProjectSendReminder";
			String propertyVal = EnoviaResourceBundle.getProperty(context, propertyKey);
			String[] aItems = propertyVal.split(ProgramCentralConstants.COMMA);
			String prevItem = EMPTY_STRING;
			for (String thisItem : aItems) {
				thisItem = thisItem.trim();
				if(thisItem.equals(sProjectSendReminderValue)){
					break;
				}
				prevItem = thisItem;
			}
			
			String sAttributeValue = ProgramCentralUtil.getPMCI18nString(context, prevItem, language);
			return sAttributeValue;
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	@com.matrixone.apps.framework.ui.ProgramCallable
	public Map getSendReminderAttrRange(Context context, String args[]) throws MatrixException {
		try {	
			String language = context.getSession().getLanguage();
			String sendReminder = EnoviaResourceBundle.getProperty(context, "emxProgramCentral.ProjectSendReminder");
			StringList sendReminderList = new StringList();
			StringList sendReminderValueList = new StringList();
			String[] sendReminderArray = sendReminder.split(",");
			Map mAttributeRange = new HashMap();
			
			for(int i=0;i<sendReminderArray.length-1;i=i+2) {
				sendReminderList.add(EnoviaResourceBundle.getProperty(context, "ProgramCentral", sendReminderArray[i].trim(), language));
				sendReminderValueList.add(sendReminderArray[i+1].trim());
			}
			
			mAttributeRange.put("field_choices", sendReminderValueList);
			mAttributeRange.put("field_display_choices", sendReminderList);
			return mAttributeRange;
			
		} catch(Exception e) {
			throw new MatrixException(e);
		}
	} 

	public void updateSendReminderAttr(Context context, String args[]) throws MatrixException {
		try {
			Map programMap = (Map)JPO.unpackArgs(args);
			Map paramMap = (HashMap) programMap.get("paramMap");
			String newValue = (String) paramMap.get("New Value");
			String projectId = (String)paramMap.get("objectId");
			DomainObject project = DomainObject.newInstance(context,projectId);
			project.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_SEND_REMINDER, newValue);
			
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	public String getTaskAssignmentReminderDuration(Context context, String args[]) throws MatrixException {
		
		String language = context.getSession().getLanguage();
		String propertyKey = "emxProgramCentral.TaskSendReminder";
		String propertyVal = EnoviaResourceBundle.getProperty(context, propertyKey);
		
		try {
			Map programMap = (Map) JPO.unpackArgs(args);
			Map paramMap = (Map)programMap.get("paramMap");
			String strId = (String) paramMap.get("objectId");

			DomainObject project = DomainObject.newInstance(context, strId);
			String attrVal = project.getInfo(context,ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ASSIGNMENT_REMINDER_DURATION);
			
			String[] aItems = propertyVal.split(ProgramCentralConstants.COMMA);
			String prevItem = EMPTY_STRING;
			
			for (String thisItem : aItems) {
				thisItem = thisItem.trim();
				if(thisItem.equals(attrVal)) {
					break;
				}
				prevItem = thisItem;
			}
			String taskAssignemntReminderDuration = ProgramCentralUtil.getPMCI18nString(context, prevItem, language);
			
			return taskAssignemntReminderDuration;
		} catch(Exception e) {
			throw new MatrixException(e);
		}
	}

	@com.matrixone.apps.framework.ui.ProgramCallable
	public Map getTaskAssignmentReminderDurationAttrRange(Context context, String args[]) throws MatrixException{
		try {	
			String language = context.getSession().getLanguage();
			String resVal = EnoviaResourceBundle.getProperty(context, "emxProgramCentral.TaskSendReminder");
			StringList displayList = new StringList();
			StringList valueList = new StringList();
			Map mAttributeRange = new HashMap();
			String[] taskSendReminderDurationRangearray = resVal.split(",");
			
			for(int i=0;i<taskSendReminderDurationRangearray.length-1;i=i+2) {
				displayList.add(EnoviaResourceBundle.getProperty(context, "ProgramCentral", taskSendReminderDurationRangearray[i].trim(), language));
				valueList.add(taskSendReminderDurationRangearray[i+1].trim());
			}
		
			mAttributeRange.put("field_choices", valueList);
			mAttributeRange.put("field_display_choices", displayList);
			return mAttributeRange;
		}catch(Exception e){
			throw new MatrixException(e);
		}
	} 

	public void updateTaskAssignmentReminderDuration(Context context, String args[]) throws MatrixException{
		try {
			Map programMap = (Map)JPO.unpackArgs(args);
			Map paramMap = (HashMap) programMap.get("paramMap");
			String val = (String) paramMap.get("New Value");
			String projectId = (String)paramMap.get("objectId");
			DomainObject project = DomainObject.newInstance(context,projectId);
			project.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_TASK_ASSIGNMENT_REMINDER_DURATION, val);
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	public String getTaskAssignmentReminderRecurrenceInterval(Context context, String args[]) throws MatrixException{
		try{
			Map programMap = (Map) JPO.unpackArgs(args);
			Map paramMap = (Map)programMap.get("paramMap");
			String strId = (String) paramMap.get("objectId");

			DomainObject project = DomainObject.newInstance(context, strId);
			String attrVal = project.getInfo(context, 
					ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ASSIGNMENT_REMINDER_RECURRENCE_INTERVAL);

			String language = context.getSession().getLanguage();
			String propertyKey = "emxProgramCentral.TaskRecurrenceInterval";
			String propertyVal = EnoviaResourceBundle.getProperty(context, propertyKey); 
			String[] aItems = propertyVal.split(ProgramCentralConstants.COMMA);
			String prevItem = ProgramCentralConstants.EMPTY_STRING;
			for (String thisItem : aItems) {
				thisItem = thisItem.trim();
				if(thisItem.equals(attrVal)){
					break;
				}
				prevItem = thisItem;
			}
			String sAttributeValue = ProgramCentralUtil.getPMCI18nString(context, prevItem, language);
			return sAttributeValue;
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	@com.matrixone.apps.framework.ui.ProgramCallable
	public Map getTaskAssignmentReminderRecurrenceIntervalAttrRange(Context context, String args[]) throws MatrixException{
		try{	
			String language = context.getSession().getLanguage();
			String resVal = EnoviaResourceBundle.getProperty(context, "emxProgramCentral.TaskRecurrenceInterval");
			StringList displayList = new StringList();
			StringList valueList = new StringList();
			String[] taskReminderRecurrenceIntervalRangearray = resVal.split(",");
			
			for(int i=0;i<taskReminderRecurrenceIntervalRangearray.length-1;i=i+2) {
				displayList.add(EnoviaResourceBundle.getProperty(context, "ProgramCentral", taskReminderRecurrenceIntervalRangearray[i].trim(), language));
				valueList.add(taskReminderRecurrenceIntervalRangearray[i+1].trim());
			}

			Map mAttributeRange = new HashMap();
			mAttributeRange.put("field_choices", valueList);
			mAttributeRange.put("field_display_choices", displayList);
			return mAttributeRange;
		}catch(Exception e){
			throw new MatrixException(e);
		}
	} 

	public void updateTaskAssignmentReminderRecurrenceInterval(Context context, String args[]) throws MatrixException{
		try {
			Map programMap = (Map)JPO.unpackArgs(args);
			Map paramMap = (HashMap) programMap.get("paramMap");
			String val = (String) paramMap.get("New Value");
			String projectId = (String)paramMap.get("objectId");
			DomainObject project = DomainObject.newInstance(context,projectId);
			project.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_TASK_ASSIGNMENT_REMINDER_RECURRENCE_INTERVAL, val);
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	public String getProjectRecurrenceInterval(Context context, String args[]) throws MatrixException{
		try{
			Map programMap = (Map) JPO.unpackArgs(args);
			Map paramMap = (Map)programMap.get("paramMap");
			String strId = (String) paramMap.get("objectId");

			DomainObject project = DomainObject.newInstance(context, strId);
			String sProjectRecurrence = project.getInfo(context,
					ProgramCentralConstants.SELECT_ATTRIBUTE_RECURRENCE_INTERVAL);

			String propertyKey = "emxProgramCentral.ProjectRecurrenceInterval";
			String recurInterval = EnoviaResourceBundle.getProperty(context, propertyKey);
			String[] aRecurInterval = recurInterval.split(ProgramCentralConstants.COMMA);
			String prevItem = ProgramCentralConstants.EMPTY_STRING;
			for (String thisItem : aRecurInterval) {
				thisItem = thisItem.trim();
				if(thisItem.equals(sProjectRecurrence)){
					break;
				}
				prevItem = thisItem;
			}

			String language = context.getSession().getLanguage();
			String projectRecurrence = ProgramCentralUtil.getPMCI18nString(context, prevItem, language);
			return projectRecurrence;
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	public void updateRecurrenceInterval(Context context, String args[]) throws MatrixException{
		try {
			Map programMap = (Map)JPO.unpackArgs(args);
			Map paramMap = (HashMap) programMap.get("paramMap");
			String val = (String) paramMap.get("New Value");
			String projectId = (String)paramMap.get("objectId");
			DomainObject project = DomainObject.newInstance(context,projectId);
			project.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_RECURRENCE_INTERVAL, val);
        }catch(Exception e){
            throw new MatrixException(e);
        }
    }

	@com.matrixone.apps.framework.ui.ProgramCallable
	public Map getRecurrenceIntervalAttrRange(Context context, String args[]) throws MatrixException{
		try{	
			String language = context.getSession().getLanguage();
			String propertyKey = "emxProgramCentral.ProjectRecurrenceInterval";
			String recurInterval = EnoviaResourceBundle.getProperty(context, propertyKey);
			StringList recurIntervalList = new StringList();
			StringList recurIntervalValueList = new StringList();
			String[] RecurrenceIntervalRangearray = recurInterval.split(",");
			
			for(int i=0;i<RecurrenceIntervalRangearray.length-1;i=i+2) {
				recurIntervalList.add(EnoviaResourceBundle.getProperty(context, "ProgramCentral", RecurrenceIntervalRangearray[i].trim(), language));
				recurIntervalValueList.add(RecurrenceIntervalRangearray[i+1].trim());
			}

			Map mAttributeRange = new HashMap();
			mAttributeRange.put("field_choices", recurIntervalValueList);
			mAttributeRange.put("field_display_choices", recurIntervalList);
			return mAttributeRange;
		}catch(Exception e){
			throw new MatrixException(e);
		}
	}

	public boolean getResourcePlanPreferenceViewAccess(Context context, String args[]) throws MatrixException{
		boolean editFlag = true;
		try {
			 Map programMap =  JPO.unpackArgs(args);
		     String projectId = (String) programMap.get("objectId");
		     String mode = (String) programMap.get("mode");
		     DomainObject project = DomainObject.newInstance(context,projectId);
		     String SELECT_RESOURCE_REQUEST_ID = "from[" + DomainConstants.RELATIONSHIP_RESOURCE_PLAN + "].to.id";
		     String hasResourceRequests = project.getInfo(context, SELECT_RESOURCE_REQUEST_ID);
		     
		     if("edit".equals(mode) && ProgramCentralUtil.isNullString(hasResourceRequests)) {
		    	 editFlag = false;
		     }
		     return editFlag;
		} catch(Exception e) {
			throw new MatrixException(e);
		}
	}
	public boolean getResourcePlanPreferenceEditAccess(Context context,String[] args)throws Exception { 
		   boolean showEditField = !getResourcePlanPreferenceViewAccess(context, args);
		   return showEditField;
	   }

	 /**
	  * Method to check if DPG product is installed
	  *
	  * @param context
	  * @param args
	  * @return	true - if DPG is installed
	  * 		false - if DPG is not Installed
	  * @exception throws FrameworkException
	  * @since R418
	  */
    public boolean isDPGInstalled(Context context,String args[]) throws Exception
    {
        //return  FrameworkUtil.isSuiteRegistered(context,"appVersionENO6WDeliverablesPlanning",false,null,null);
    	return false;
    }

	
	 /**
	  * Method to check if DPG product is installed and the Project has No Shadow Gates
	  *
	  * @param context
	  * @param args
	  * @return	true - if DPG is installed and there are no Shadow Gates on the Project and we are in the Create or Assign state
	  * 		false - if DPG is not Installed or there are Shadow Gates on the Project
	  * @exception throws FrameworkException
	  * @since R418
	  */
   public boolean isDPGInstalledandNoShadowGates(Context context,String args[]) throws Exception
   {
      return false;
	   /* try{
           boolean access = FrameworkUtil.isSuiteRegistered(context,"appVersionENO6WDeliverablesPlanning",false,null,null);
           
           if (access == true)
           {
        	   String RELATIONSHIP_SHADOW_GATE = PropertyUtil.getSchemaProperty("relationship_ShadowGate");
               HashMap programMap = (HashMap) JPO.unpackArgs(args);
               String objectId = (String) programMap.get("objectId");
               DomainObject project = DomainObject.newInstance(context, objectId);
               String SELECT_IS_GATE_A_SHADOW_GATE = "from["+DomainConstants.RELATIONSHIP_SUBTASK+"].to.from["+RELATIONSHIP_SHADOW_GATE+"].id";
               StringList strShadowGateRelationships = project.getInfoList(context,SELECT_IS_GATE_A_SHADOW_GATE);
               String stateName = project.getInfo(context,DomainConstants.SELECT_CURRENT);
               String sStateCreate = PropertyUtil.getSchemaProperty(context, "Policy", DomainObject.POLICY_PROJECT_SPACE, "state_Create");
               String sStateAssign = PropertyUtil.getSchemaProperty(context, "Policy", DomainObject.POLICY_PROJECT_SPACE, "state_Assign");


               if ((strShadowGateRelationships == null || strShadowGateRelationships.isEmpty()) && (stateName.equals(sStateCreate) || stateName.equals(sStateAssign)))
               {
            	   return true;
               }
               else
               {
            	   return false;   
               }
           }
           return access;
       }catch(Exception e){
           throw new MatrixException(e);
       }*/
   }

	 /**
	  * Method to check if DPG product is installed and the Project has Shadow Gates
	  *
	  * @param context
	  * @param args
	  * @return	true - if DPG is installed and the Project has Shadow Gates
	  * 		false - if DPG is not Installed or there are no Shadow Gates on the Project
	  * @exception throws FrameworkException
	  * @since R418
	  */
 public boolean isDPGInstalledandHasShadowGates(Context context,String args[]) throws Exception
 {
    return false;
	 /* try{
         boolean access = FrameworkUtil.isSuiteRegistered(context,"appVersionENO6WDeliverablesPlanning",false,null,null);
  	   
         if (access == true)
         {
      	     String RELATIONSHIP_SHADOW_GATE = PropertyUtil.getSchemaProperty("relationship_ShadowGate");
             HashMap programMap = (HashMap) JPO.unpackArgs(args);
             String objectId = (String) programMap.get("objectId");
             DomainObject project = DomainObject.newInstance(context, objectId);
             String SELECT_IS_GATE_A_SHADOW_GATE = "from["+DomainConstants.RELATIONSHIP_SUBTASK+"].to.from["+RELATIONSHIP_SHADOW_GATE+"].id";
             StringList strShadowGateRelationships = project.getInfoList(context,SELECT_IS_GATE_A_SHADOW_GATE);

             String stateName = project.getInfo(context,DomainConstants.SELECT_CURRENT);
             String sStateCreate = PropertyUtil.getSchemaProperty(context, "Policy", DomainObject.POLICY_PROJECT_SPACE, "state_Create");
             String sStateAssign = PropertyUtil.getSchemaProperty(context, "Policy", DomainObject.POLICY_PROJECT_SPACE, "state_Assign");


             if ((strShadowGateRelationships == null || strShadowGateRelationships.isEmpty()) && (stateName.equals(sStateCreate) || stateName.equals(sStateAssign)))
             {
          	   return false;
             }
             else
             {
          	   return true;   
             }
         }
         return access;
     }catch(Exception e){
         throw new MatrixException(e);
     }*/
 }
	/**
	 * This method returns a list of the user's projects. This method is called from the copy entire/partial Schedule form.
	 * @param context the ENOVIA <code>Context</code> object.
	 * @param args request arguments
	 * @return a list of the user's projects
	 * @throws FrameworkException
	 */
	@com.matrixone.apps.framework.ui.IncludeOIDProgramCallable
	public StringList getProjects(Context context,String[] args)throws FrameworkException {

		StringList returnList = new StringList();
		try{
			Map programMap = (Map) JPO.unpackArgs(args);
			String selectedTaskId = (String) programMap.get("selectedTaskId");
			Person person = Person.getPerson(context);
			boolean showSubTypes = true;
			StringBuffer busWhere = new StringBuffer();
			busWhere.append("type != "+ProgramCentralConstants.TYPE_EXPERIMENT);

			StringList busSelects = new StringList(SELECT_ID);
			StringList relSelects = new StringList(0);

			String relWhere = ProgramCentralConstants.EMPTY_STRING;

			MapList projectList = ProjectSpace.getProjects(context, person, TYPE_PROJECT_MANAGEMENT, busSelects, relSelects, busWhere.toString(), relWhere, true);

			Iterator projectListIterator = projectList.iterator();
			while(projectListIterator.hasNext()){
				Map projectMap = (Map)projectListIterator.next();
				returnList.add((String)projectMap.get(SELECT_ID));
			}
			//Below code is to include templates in search result.
			String activeTemplate = "current=='" + "Active" + "'";
			MapList tempList = ProjectTemplate.getProjectTemplates(context, busSelects, activeTemplate);
			Iterator templateItr = tempList.iterator();
			while(templateItr.hasNext()){
				Map templateMap = (Map)templateItr.next();
				returnList.add((String)templateMap.get(SELECT_ID));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return returnList;
	}

	@com.matrixone.apps.framework.ui.PostProcessCallable  
	public static void copyEntireScheduleProcess(Context context,String[]args)throws Exception 	{
		try{
		Map programMap 				 = (Map) JPO.unpackArgs(args);
		Map requestMap				 = (Map)programMap.get("requestMap");
		String questionResponseValue = (String) CacheUtil.getCacheObject(context, "QuestionsResponse");
		CacheUtil.removeCacheObject(context, "QuestionsResponse");
	    
		Map <String,String>questionResponseMap = new HashMap<String,String>();

		if(ProgramCentralUtil.isNotNullString(questionResponseValue)){
			questionResponseMap = Question.getQuestionResponce(questionResponseValue);
		}
		Map mapCheckAttributeCopy = new HashMap();
		mapCheckAttributeCopy.put(ATTRIBUTE_ESTIMATED_DURATION_KEYWORD, "true");

		String objectId = (String) requestMap.get("objectId");
		String projectTemplateId = (String) requestMap.get("SeachProjectOID");

		Task srcTask = new Task(projectTemplateId);
		Task targetTask = new Task(objectId);

		Map lookup = new HashMap();
PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
		srcTask.cloneWBS(context, targetTask, null, questionResponseMap, lookup, true, false, mapCheckAttributeCopy);

		}finally{
                        PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");	
		}
		//targetTask.importStartDate(context,new Date(Date.parse(targetTask.getInfo(context,targetTask.SELECT_TASK_ESTIMATED_START_DATE))));
	}

	@com.matrixone.apps.framework.ui.PostProcessCallable  
	public static void copyPartialScheduleProcess(Context context,String[]args)throws Exception {
		try {
			Map programMap 				 = (Map) JPO.unpackArgs(args);

			CacheUtil.removeCacheObject(context, "taskIdList");

			String questionResponseValue = (String) CacheUtil.getCacheObject(context, "QuestionsResponse");
			CacheUtil.removeCacheObject(context, "QuestionsResponse");
			PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
			String targetTaskId = (String) programMap.get("objectId");
			String sourceTasks = (String) programMap.get("SeachProjectOID");
        String UseStartAndEndDatesAsEntered = (String) programMap.get("UseStartAndEndDatesAsEntered");
			StringList sourceTaskList = FrameworkUtil.split(sourceTasks, "|");
			String[] sourceTaskIds = new String[sourceTaskList.size()];
			sourceTaskList.toArray(sourceTaskIds);
        boolean isUseStartAndEndDatesAsEntered=false;
        if("true".equalsIgnoreCase(UseStartAndEndDatesAsEntered)){
        	isUseStartAndEndDatesAsEntered=true;
         }

			MapList objectInfoList = new MapList();
			for(int i=0, j = sourceTaskList.size(); i < j; i++){
				StringList objectSelects = new StringList();  
				objectSelects.add(ProgramCentralConstants.SELECT_ID);
				objectSelects.add(ProgramCentralConstants.SELECT_TYPE);
				objectSelects.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
				objectSelects.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_TEMPLATE);
				objectInfoList = DomainObject.getInfo(context, sourceTaskIds, objectSelects);
			}

			StringList partialTaskList = new StringList();
			//Iterate through each task.
			for (Iterator iterator = objectInfoList.iterator(); iterator.hasNext();) {
				Map taskInfo = (Map) iterator.next();
				String taskId = (String) taskInfo.get(ProgramCentralConstants.SELECT_ID);
				String taskType = (String) taskInfo.get(ProgramCentralConstants.SELECT_TYPE);
				String taskTypeKindOfProjectSpace = (String) taskInfo.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
				String taskTypeKindOfProjectTemplate = (String) taskInfo.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_TEMPLATE);

				if("true".equalsIgnoreCase(taskTypeKindOfProjectSpace) || "true".equalsIgnoreCase(taskTypeKindOfProjectTemplate)){

                 if("true".equalsIgnoreCase(taskTypeKindOfProjectSpace) &&isUseStartAndEndDatesAsEntered){
                 	PropertyUtil.setGlobalRPEValue(context,"UseStartAndEndDatesAsEntered","true");
                 }else{
                	 PropertyUtil.setGlobalRPEValue(context,"UseStartAndEndDatesAsEntered","false");
                 }
					Map<String,String> questionResponseMap = new HashMap<String,String>();

					if(ProgramCentralUtil.isNotNullString(questionResponseValue)){
						questionResponseMap = Question.getQuestionResponce(questionResponseValue);
					}

					Map mapCheckAttributeCopy = new HashMap();
					mapCheckAttributeCopy.put(ATTRIBUTE_ESTIMATED_DURATION_KEYWORD, "true");

					Task srcTask = new Task(taskId);
					Task targetTask = new Task(targetTaskId);

					Map lookup = new HashMap();
					CheckList checklist = new CheckList();
					srcTask.addEventListener(checklist);
					//passing true for copyReferenceDocument and false for copyDeliverables and 
					srcTask.cloneWBS(context, targetTask, null, questionResponseMap, lookup, false, true, false, mapCheckAttributeCopy);
					//srcTask.cloneWBS(context, targetTask, null, questionResponseMap, lookup, true, false, mapCheckAttributeCopy);
					srcTask.removeEventListener(checklist);
				}else{
					partialTaskList.add(taskId);
				}

			}

			//It will only call for Partial Copy - Task Selection
			if(partialTaskList.size() > 0){
				ProjectSpace project =(ProjectSpace) DomainObject.newInstance(context,TYPE_PROJECT_SPACE, PROGRAM);
				project.setId(targetTaskId);
            if(isUseStartAndEndDatesAsEntered){
             	PropertyUtil.setGlobalRPEValue(context,"UseStartAndEndDatesAsEntered","true");
             }
				try{
					String[] partialTaskIds = new String[partialTaskList.size()];
					partialTaskList.toArray(partialTaskIds);
					project.addTask(context, partialTaskIds, null);
					PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
					//Added code to support Auto/Manual rollup feature 
					String projectSchedule = project.getSchedule(context);
					String isKindOfProjectTemplate = EMPTY_STRING;
                    String targetTaskInfo =EMPTY_STRING;
                    String isProjectTemplateTask =EMPTY_STRING;
					
					if(projectSchedule.isEmpty()){
                    String mqlCmd ="print bus $1 select $2 $3 $4 dump $5";
                    targetTaskInfo = MqlUtil.mqlCommand(context, 
                            true,
                            true,
                            mqlCmd, 
                            true,
                            targetTaskId,
                            ProgramCentralConstants.SELECT_KINDOF_PROJECT_TEMPLATE,
                            ProgramCentralConstants.SELECT_PROJECT_SCHEDULE_ATTRIBUTE_FROM_TASK,
                            ProgramCentralConstants.SELECT_IS_PROJECT_TEMPLATE_TASK,
                            "|");
					StringList resultList = StringUtil.split(targetTaskInfo, "|");
                    isKindOfProjectTemplate  =resultList.get(0);
                   if(resultList.size()>1){
                        projectSchedule =resultList.get(1);}
                    if(resultList.size()>2){
                        isProjectTemplateTask =resultList.get(2);}
                    Task projectRollup = new Task(targetTaskId);
					projectRollup.rollupAndSave(context);
                   }
					if(ProgramCentralConstants.PROJECT_SCHEDULE_AUTO.equalsIgnoreCase(projectSchedule) || "TRUE".equalsIgnoreCase(isKindOfProjectTemplate) || "TRUE".equalsIgnoreCase(isProjectTemplateTask)){
						Task projectRollup = new Task(targetTaskId);
						projectRollup.rollupAndSave(context);
					}
				}catch(Exception e){
					throw new Exception(EnoviaResourceBundle.getProperty(context, "ProgramCentral",
							"emxProgramCentral.CopySchedule.CopyPartialSchedule.ErrorMsg", context.getSession().getLanguage()));
                }finally{
                    PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
                    PropertyUtil.setGlobalRPEValue(context,"UseStartAndEndDatesAsEntered","false");
				}
			}
		}finally{
			PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
            PropertyUtil.setGlobalRPEValue(context,"UseStartAndEndDatesAsEntered","false");
		}
	}
		
	/**
     * It returns true when a Project is created within a Program.
     * 
     * @param context the ENOVIA <code>Context</code> object.
     * @param args request arguments
     * @return true if object is of type Program
     * @throws Exception if operation fails. 
     */
	public boolean isFromProgram(Context context, String[] args) throws Exception {
		boolean isFromProgram = false;
		//boolean isECHFieldDisable = isECHFieldDisable(context, args);
		Map programMap = (Map) JPO.unpackArgs(args);
		String objectId = (String) programMap.get("objectId");
		if(ProgramCentralUtil.isNotNullString(objectId)){
			DomainObject domObj = DomainObject.newInstance(context, TYPE_PROGRAM, PROGRAM);
			domObj.setId(objectId);
			if(domObj.isKindOf(context, TYPE_PROGRAM)){
				isFromProgram = true;
			}
		}
		return (isFromProgram);
	}
	
	/**
     * It returns true when a Project is created anywhere outside of a Program.
     * 
     * @param context the ENOVIA <code>Context</code> object.
     * @param args request arguments
     * @return true if object is of type Program
     * @throws Exception if operation fails. 
     */
	public boolean isNotFromProgram(Context context, String[] args) throws Exception {
		boolean isFromProgram = isFromProgram(context, args);
		boolean isECHFieldDisable = isECHFieldDisable(context, args);
		return (!isFromProgram && isECHFieldDisable);
	}
	
	
	
	/**
     * It returns Program name.
     * 
     * @param context the ENOVIA <code>Context</code> object.
     * @param args request arguments
     * @return programName
     * @throws Exception if operation fails. 
     */
	public String getProgramName(Context context, String[] args) throws Exception {
		
		String programName = EMPTY_STRING;
		Map programMap = (Map) JPO.unpackArgs(args);
		Map requestMap = (Map) programMap.get("requestMap");
		String objectId = (String) requestMap.get("objectId");
		if(ProgramCentralUtil.isNotNullString(objectId)){
			MapList objInfoMapList = DomainObject.getInfo(context, new String[]{objectId}, new StringList(SELECT_NAME));
			Map objInfoMap = (Map) objInfoMapList.get(0);
			programName = (String) objInfoMap.get(SELECT_NAME);
		}
		return programName;
	}

 /**
  * This method returns maplist containing list of Gates from the Deliverable Plan connected to the Deliverable of the Project.
  * It displays all the Gates of the Deliverable Plan.
  * @param context the ENOVIA <code>Context</code> object
  * @param args The arguments, it contains programMap
  * @param returns maplist containing Gates from the projects Deliverable, Deliverable Plan
  * @throws Exception if operation fails
  */
 @com.matrixone.apps.framework.ui.ProgramCallable
 public MapList getAllDeliverablePlanGates(Context context,String[] args) throws MatrixException
 {
	return new MapList();
    /* MapList mlAllList = new MapList();
     try {
    	 Map programMap = (Map)JPO.unpackArgs(args);

         String strProjectId = (String)programMap.get("objectId");
         DomainObject dmoObject = DomainObject.newInstance(context, strProjectId);

         String deliverableId = dmoObject.getInfo(context, SELECT_DELIVERABLE);
         MapList mlMasterPlanGates = new MapList();
         
         if (null != deliverableId && !"".equals(deliverableId))
         {
//        	 DeliverablePlanGate deliverablePlanGate = new DeliverablePlanGate();
//        	 mlMasterPlanGates = deliverablePlanGate.getMasterPlanGates(context, deliverableId, null);
         	// use reflection to get method from DPG
            Class reqClass = Class.forName("com.dassault_systemes.enovia.dpg.DeliverablePlanGate");
        	Class[] classArgs = new Class[3];
            classArgs[0] = Context.class;
            classArgs[1] = String.class;
            classArgs[2] = StringList.class;
            Method reqMethod = reqClass.getMethod("getMasterPlanGates", classArgs);
        	Object[] methodArgs = new Object[3];
        	methodArgs[0] = context;
        	methodArgs[1] = deliverableId;
        	methodArgs[2] = null;
        	mlMasterPlanGates = (MapList) reqMethod.invoke(null, methodArgs);
             
             
 			StringList slBusSelect = new StringList(2);
			slBusSelect.add(SELECT_ID);
			StringList slRelSelect = new StringList(EMPTY_STRING);
			String strTypePattern = TYPE_GATE;
			String strRelPattern = RELATIONSHIP_SUBTASK+","+ "Shadow Gate";

			MapList mlAlreadyUsedShadowGates = dmoObject.getRelatedObjects(context,
									  strRelPattern,  // rel pattern
									  strTypePattern, // type pattern
									  slBusSelect,    // bus selects
									  slRelSelect,    // relationship selects
									  true,           // getTo
									  true,           // getFrom
									  (short) 2,      // recurseToLevel
									  null,           // objectWhere
									  null,           // relationshipWhere
									  0);

			// Remove any Gates that have already been added to the Project
			for (Iterator iterator = mlAlreadyUsedShadowGates.iterator(); iterator.hasNext();) {
				Map mShadowGate = (Map) iterator.next();
				String strShadowGateId = (String)mShadowGate.get(SELECT_ID);
				
				for (Iterator iterator2 = mlMasterPlanGates.iterator(); iterator2.hasNext();)
				{
					Map mMasterGate = (Map) iterator2.next();
					String strMasterGateId = (String)mMasterGate.get(SELECT_ID);
					
					if(strMasterGateId.equalsIgnoreCase(strShadowGateId))
					{
						mlMasterPlanGates.remove(mMasterGate);
						break;
					}
				
				}
				
			}
  			
         }

         return mlMasterPlanGates; 
     }catch(Exception e)
     {
         throw new MatrixException(e);
     }*/
 }

	 /**
	  * Method to check if DPG product is installed and we are in Create or Assign State
	  *
	  * @param context
	  * @param args
	  * @return	true - if DPG is installed, project in Create or Assign State, Deliverable connected to Project
	  * 		false - if DPG is not Installed
	  * @exception throws FrameworkException
	  * @since R418
	  */
   public boolean isAddingShadowGateEnabled(Context context,String args[]) throws Exception
   {
      return false;
	   /* try{
           boolean access = FrameworkUtil.isSuiteRegistered(context,"appVersionENO6WDeliverablesPlanning",false,null,null);
           
           if (access == true)
           {
        	   String RELATIONSHIP_SHADOW_GATE = PropertyUtil.getSchemaProperty("relationship_ShadowGate");
               HashMap programMap = (HashMap) JPO.unpackArgs(args);
               String objectId = (String) programMap.get("objectId");
               DomainObject project = DomainObject.newInstance(context, objectId);
               
               StringList sList = new StringList(2);
               sList.add(DomainConstants.SELECT_CURRENT);
               sList.add(SELECT_DELIVERABLE);

               Map taskMap = (Map)project.getInfo(context,sList);
               String stateName = (String)taskMap.get(DomainConstants.SELECT_CURRENT);
               String deliverableId = (String)taskMap.get(SELECT_DELIVERABLE);
               
               String sStateCreate = PropertyUtil.getSchemaProperty(context, "Policy", DomainObject.POLICY_PROJECT_SPACE, "state_Create");
               String sStateAssign = PropertyUtil.getSchemaProperty(context, "Policy", DomainObject.POLICY_PROJECT_SPACE, "state_Assign");


               if ((deliverableId != null && !deliverableId.isEmpty()) && (stateName.equals(sStateCreate) || stateName.equals(sStateAssign)))
               {
            	   return true;
               }
               else
               {
            	   return false;   
               }
           }
           return access;
       }catch(Exception e){
           throw new MatrixException(e);
       }*/
   }
	/**
	 * Get all calendars connected to the project.
	 * @param context the ENOVIA <code>Context</code> object.
	 * @param args request arguments
	 * @return all the calendars connected to a project.
	 * @throws Exception if operation fails.
	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable
  	public MapList getProjectCalendars(Context context, String[] args) throws Exception	{

  		MapList calendarList = new MapList();
  		Map programMap = (Map) JPO.unpackArgs(args);
  		String objectId = (String) programMap.get("objectId");
 		ProjectSpace project = (ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);
 		project.setId(objectId);
  		calendarList = project.getProjectCalendars(context);
  		
  		return calendarList;
  	}
  	/**
  	 * Excludes the calendars which are already connected to Project. It also excludes the "DefaultCalendar" object which exists in DB.
  	 * @param context the ENOVIA <code>Context</code> object.
  	 * @param args request arguments
  	 * @return List of calendarIds, which contains, Ids of calendars which are already connected to a project and a "DefaultCalendar" Id which exists in DB.
  	 * @throws Exception if operation fails.
  	 */
   	 @com.matrixone.apps.framework.ui.ExcludeOIDProgramCallable
   	 public StringList getExcludeOIDForProjectCalendar(Context context, String []args)throws Exception {

   		 StringList excludeCalendarList = new StringList();
   		 MapList calendarList = new MapList();
   		 Map programMap = (Map) JPO.unpackArgs(args);
   		 String objectId = (String) programMap.get("objectId");


   		 MapList mlCalendarList = new MapList();
   		 StringList selectables = new StringList();
   		 selectables.add(DomainConstants.SELECT_ID);
   		 selectables.add(DomainConstants.SELECT_NAME);
   		 
   		 mlCalendarList =DomainObject.findObjects(context,
   				 								  PropertyUtil.getSchemaProperty(context,"type_WorkCalendar"),
   				 								  null,"name == 'DefaultCalendar'",
   				 								  selectables);
   		 //Exclude the DefaultCalendar object
   		 Iterator mlCalendarListIterator = mlCalendarList.iterator();
   		 while(mlCalendarListIterator.hasNext()){
   			 Map defaultCalendarInfo = (Map)mlCalendarListIterator.next();
   			 String defaultCalendarId = (String)defaultCalendarInfo.get(SELECT_ID);
   			 excludeCalendarList.add(defaultCalendarId);
   		 }

   		 ProjectSpace project = (ProjectSpace) DomainObject.newInstance(context, DomainConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);
   		 project.setId(objectId);
   		 calendarList = project.getProjectCalendars(context);

   		 // Exclude the calendars which are already connected to Project
   		 Iterator calendarListIterator = calendarList.iterator();
   		 while(calendarListIterator.hasNext()){
   			 Map calendarInfo = (Map)calendarListIterator.next();
   			 String calendarId = (String)calendarInfo.get(SELECT_ID);
   			 excludeCalendarList.add(calendarId);
   		 }

   		 return excludeCalendarList;
   	 }
   	 
   	/**
   	 * This method returns the StringList containing the value, whether the calendar is connected to project or not.
   	 * If the calendar is already connected to project then it will add "Yes" otherwise "No" in the returning StringList.
   	 * Gets all calendars in the Company. 
   	 * @param context - The ENOVIA <code>Context</code> object.
   	 * @param args
   	 * @throws Exception if operation fails.
   	 */
   	 public StringList getAllCalendars(Context context, String[] args) throws Exception {

   		 Map projectMap = (HashMap) JPO.unpackArgs(args);
   		 MapList objectList = (MapList) projectMap.get("objectList");
   		 Map paramList = (Map) projectMap.get("paramList");
   		 String objectID = (String) paramList.get("parentOID");
   		 StringList returnList = new StringList(objectList.size());

   		 MapList projectCalendarList = new MapList();
   		 StringList projectCalList = null;
   		 StringBuffer slBusSelect = new StringBuffer(ProgramCentralConstants.SELECT_TASK_PROJECT_ID);
   		 String selectable = slBusSelect.toString();	
   		 DomainObject dObj =  DomainObject.newInstance(context,objectID);
   		 String projectId = dObj.getInfo(context, selectable);
   		 if(projectId == null){
   			 projectId = objectID;
   		 }
   		 
   		 ProjectSpace project = (ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE,DomainConstants.PROGRAM);
   		 project.setId(projectId);
   		 projectCalendarList = project.getProjectCalendars(context);

   		 projectCalList = new StringList(projectCalendarList.size());

   		 for (Iterator iterator = projectCalendarList.iterator(); iterator.hasNext();) {
   			 Map temp = (Map)iterator.next();
   			 String id = (String) temp.get(SELECT_ID);
   			 projectCalList.add(id);
   		 }
   		 for (Iterator iterator = objectList.iterator(); iterator.hasNext();) {
   			 Map calendarInfo = (Map) iterator.next();
   			 String calendarId = (String)calendarInfo.get(SELECT_ID);
   			 if(projectCalList.contains(calendarId)){
   				 returnList.add("Yes");
   			 } else{
   				 returnList.add("No");
   			 }
   		 }
   		 return returnList;
   	 }
   	 
   	 public boolean getEnableApprovalViewAccess(Context context, String[] args) throws Exception {
   		 boolean editFlag = true;
   		 Map programMap = (Map) JPO.unpackArgs(args);
   		 String projectId = (String) programMap.get("objectId");
   		 String mode = (String) programMap.get("mode");
   		 String SELECT_ISSUE_NAME = "to[Issue].from.name";	
   		 StringList busSelects = new StringList();
   		 busSelects.add(SELECT_ISSUE_NAME);

   		 ProjectSpace project = new ProjectSpace(projectId);
   		 Map projectMap = project.getInfo(context, busSelects);

   		 boolean isEnableApprovalFreezed = false;
   		 StringList issueNameList = new StringList();
   		 Object issueNames = projectMap.get(SELECT_ISSUE_NAME);
   		 if(issueNames instanceof String){
   			issueNameList.add((String)issueNames);
   		 }else{
   		 issueNameList = (StringList)projectMap.get(SELECT_ISSUE_NAME);
   		 }
   		 if(issueNameList != null){
   			 for(int i=0;i<issueNameList.size();i++){
   				 String issueName = (String)issueNameList.get(i);
   				 if(issueName.startsWith(ProgramCentralConstants.START_WITH_EXP)){
   					 isEnableApprovalFreezed = true;
   					 break;
   				 }
   			 }
   		 }
   		 if("edit".equals(mode) && isEnableApprovalFreezed == false) {
   			 editFlag = false;
   		 }
   		 return editFlag;
   	 }
   	 
   	 public boolean getEnableApprovalEditAccess(Context context, String[] args) throws Exception {

   		 boolean showEditField = !getEnableApprovalViewAccess(context, args);
   		 return showEditField;

   	 }
   	 
   	@com.matrixone.apps.framework.ui.ProgramCallable
   	 public Map getScheduleFromAttributeRange(Context context,String[]args)throws Exception {
   		 StringList rangeToDisplay = new StringList();
   		 StringList range = new StringList();
   		 String language = context.getSession().getLanguage();
   		 String scheduleFromStart = ProgramCentralConstants.ATTRIBUTE_SCHEDULED_FROM_RANGE_START; 
   		 String scheduleFromFinish = ProgramCentralConstants.ATTRIBUTE_SCHEDULED_FROM_RANGE_FINISH;		
   		 String templateScheduleFrom = EMPTY_STRING;
   		 String secondScheduleFromType = EMPTY_STRING;
   		 Map programMap = JPO.unpackArgs(args);
   		 Map requestMap = (Map) programMap.get("requestMap");
   		 String templateId = (String) requestMap.get("objectId");

   		 if(ProgramCentralUtil.isNotNullString(templateId)){
   			 DomainObject template = DomainObject.newInstance(context, templateId);
   			 templateScheduleFrom = template.getInfo(context, "attribute[" + ATTRIBUTE_PROJECT_SCHEDULE_FROM + "].value");
   			 secondScheduleFromType = scheduleFromStart.equalsIgnoreCase(templateScheduleFrom)? scheduleFromFinish : scheduleFromStart;
   		 } else {
   			 templateScheduleFrom = scheduleFromStart;
   			 secondScheduleFromType = scheduleFromStart.equalsIgnoreCase(templateScheduleFrom)? scheduleFromFinish : scheduleFromStart;
   		 }
   		 range.add(templateScheduleFrom);
   		 range.add(secondScheduleFromType);
   		 rangeToDisplay.add(EnoviaResourceBundle.getRangeI18NString(context,ATTRIBUTE_PROJECT_SCHEDULE_FROM, templateScheduleFrom, language));
   		 rangeToDisplay.add(EnoviaResourceBundle.getRangeI18NString(context,ATTRIBUTE_PROJECT_SCHEDULE_FROM, secondScheduleFromType, language));

   		 Map scheduleFromInfoMap = new HashMap();
   		 scheduleFromInfoMap.put("field_choices", range);
   		 scheduleFromInfoMap.put("field_display_choices", rangeToDisplay);
   		 return scheduleFromInfoMap;
   	 }
   	 /**
   	  * This method returns true if logged in user is owner of poject.
   	  * @param context - The ENOVIA <code>Context</code> object.
   	  * @param args
   	  * @return true/false
   	  * @throws Exception
   	  */
   	 public boolean isProjectOwner(Context context, String args[]) throws Exception {

   		 boolean hasAccess = false;
   		 Map inputMap   = (Map) JPO.unpackArgs(args);
   		 String mode = (String) inputMap.get("mode");
   		 if(!mode.equalsIgnoreCase("edit")){
   			 hasAccess = true;
   		 }
   		 String loggedInUser = context.getUser();
   		 String objectID = (String) inputMap.get("objectId");
   		 com.matrixone.apps.program.ProjectSpace projectSpace = (com.matrixone.apps.program.ProjectSpace) DomainObject.newInstance(context, DomainConstants.TYPE_PROJECT_SPACE, "PROGRAM");
   		 projectSpace.setId(objectID);
   		boolean hasChangeOwnerAccess =  projectSpace.checkAccess(context, (short) AccessConstants.cChangeOwner);
   		 if(hasChangeOwnerAccess){
   			 hasAccess = true;
   		 }

   		 return hasAccess;
   	 }
   	 /**
   	  * This method is edit access function on owner column of WBSViewTable. Retrurns true for project only if
   	  * logged in user is owner of project.
   	  * @param context - The ENOVIA <code>Context</code> object.
   	  * @param args
   	  * @return StringList
   	  * @throws MatrixException
   	  */
   	 public StringList isOwnerColumnEditable(Context context, String[] args) throws MatrixException {

   		 StringList slAccessList = new StringList();
   		 try {
   			 Map programMap = (HashMap) JPO.unpackArgs(args);
   			 MapList objectList = (MapList) programMap.get("objectList");
   			 String loggedInUser = context.getUser();
   			 Map objectMap = null;
   			 Iterator objectListIterator = objectList.iterator();
   			 String[] objIdArr = new String[objectList.size()];   			
   			 String id = EMPTY_STRING;
   			 int i = 0;

   			 while (objectListIterator.hasNext()) {

   				 objectMap = (Map) objectListIterator.next();
   				 id = (String) objectMap.get(DomainObject.SELECT_ID);
   				 objIdArr[i] = id;
   				 i++;
   			 }
   			 StringList busSelect = new StringList(1);
   			 busSelect.add(SELECT_OWNER);
   			 busSelect.add(SELECT_TYPE);
   			 busSelect.add(SELECT_CURRENT);

   			 MapList mlTaskDetails = DomainObject.getInfo(context, objIdArr, busSelect);

   			 for (Iterator itrTableRows = mlTaskDetails.iterator(); itrTableRows.hasNext();) {

   				 Map map = (Map)itrTableRows.next();
   				 String owner = (String)map.get(SELECT_OWNER);   				 
   				 String type = (String)map.get(SELECT_TYPE);
   				 String state = (String)map.get(SELECT_CURRENT);
   				 
   				 if(ProgramCentralConstants.STATE_PROJECT_TASK_COMPLETE.equalsIgnoreCase(state))
   				 {
   					slAccessList.add("false");
   					continue;
   				 }   				
   				 
   				 if(TYPE_PROJECT_SPACE.equalsIgnoreCase(type)){
   					 if(loggedInUser.equalsIgnoreCase(owner)){
   						 slAccessList.add("true");
   					 }else{
   						 slAccessList.add("false");
   					 }
   				 }
   				 else{
   					 slAccessList.add("true");
   				 }
   			 }

   			 return slAccessList;

   		 } catch (Exception exp) {
   			 exp.printStackTrace();
   			 throw new MatrixException(exp);
   		 }
   	 }
   	 /**
   	  * This method used to decide weather to show group header fields on ProjectProperties page.
   	  * @param context - The ENOVIA <code>Context</code> object.
   	  * @param args
   	  * @return true/false
   	  * @throws Exception
   	  */
   	 public boolean isGroupHeaderTrue(Context context, String args[]) throws Exception {

   		 boolean modeNotEdit = true;
   		 Map inputMap   = (Map) JPO.unpackArgs(args);
   		 String mode = (String) inputMap.get("mode");
   		 String targetLocation = (String) inputMap.get("targetLocation");
   		 if("slidein".equalsIgnoreCase(targetLocation) && mode.equalsIgnoreCase("edit")){
   			modeNotEdit = false;
   		 }
   		 return modeNotEdit;
   	 }

 
    private String getLastDecision(Context context,String gateId){
        String decisionName = "";
        try{
            GateReport grBean = new GateReport();
            decisionName = grBean.getLastDecision(context,gateId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return decisionName;
    }
    
    private Map getCurrentPhase(Context context, MapList list, String projectState) {
    	
    	Map phaseMap = java.util.Collections.EMPTY_MAP;
    	int listSize = list.size();
    	Date currentDt = new Date();
    	for(int i=0; i<listSize; i++){
    		
            Map tempMap = (Map) list.get(i);
            String type = (String) tempMap.get(DomainConstants.SELECT_TYPE);

            if(type.equals(TYPE_GATE)){

                if (ProgramCentralConstants.STATE_PROJECT_TASK_CREATE.equals(projectState) || ProgramCentralConstants.STATE_PROJECT_TASK_ASSIGN.equals(projectState)){
                    break;
                }else if((ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD.equals(projectState) || ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL.equals(projectState)) ){
                	
                	     String gateId = (String) tempMap.get(DomainConstants.SELECT_ID);
                	     String gateDecision = getLastDecision(context, gateId);
                        if(null != gateDecision && !"".equals(gateDecision) && (ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD.equals(gateDecision) || ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL.equals(gateDecision))){
                        	
                        	phaseMap = tempMap;
                            break;
                        }
                    }else{
                    	
                    //Search for first gate with Est. start date > current date.
                    String attrEstStartDate = "attribute[" + DomainConstants.ATTRIBUTE_TASK_ESTIMATED_START_DATE + "]";
                    String gateEstStartDtStr = (String) tempMap.get(attrEstStartDate);
                    Date gateEstStartDt = eMatrixDateFormat.getJavaDate(gateEstStartDtStr);
                    String gateState = (String) tempMap.get(SELECT_CURRENT);
                    
                    if((gateEstStartDt.after(currentDt)|| areDatesEqual(gateEstStartDt, currentDt)) && !ProgramCentralConstants.STATE_PROJECT_REVIEW_COMPLETE.equals(gateState)){
                    	phaseMap = tempMap;
                        break;
                     }
                 }
            }
            else if(type.equals(TYPE_PHASE)){
                String state = (String) tempMap.get(DomainConstants.SELECT_CURRENT);
                if(ProgramCentralConstants.STATE_PROJECT_TASK_ACTIVE.equals(state) || ProgramCentralConstants.STATE_PROJECT_TASK_REVIEW.equals(state)){
                	phaseMap = tempMap;
                    break;
                }
            }

        } 
    	
    	return phaseMap;
    }
    
    /**
     * It returns true if two dates are equal
     * 
     * @param Date firstDate
     * @param Date secondDate
     * @return true if firstDate and secondDate are equal 
     */
    private boolean areDatesEqual(Date firstDate,Date secondDate){
        boolean returnVal = false;
        try{
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy.MM.dd");
            String firstDateStr =  sfd.format(firstDate);
            String secondDateStr =  sfd.format(secondDate);

            returnVal = firstDateStr.equals(secondDateStr);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return returnVal;
    }

   	 /**
   	  * This method used to get the logged in Users Collaborative Space.
   	  * @param context - The ENOVIA <code>Context</code> object.
   	  * @param args
   	  * @return Collaborative Space name
   	  * @throws Exception
   	  */
	  public String getCollaborativeSpace(Context context, String args[]) throws Exception
	  {
         try {
		     String strLoggedInUsersCS = PersonUtil.getActiveProject(context);
		     return strLoggedInUsersCS;
   		 } catch (Exception exp) {
   			 exp.printStackTrace();
   			 throw new MatrixException(exp);
   		 }

	  }
	  
	  /**
	     * It returns true if ProjectSpace Policy is not Hold and Cancel
	     * 
	     * @param context the ENOVIA <code>Context</code> object.
	     * @param args request arguments
	     * @return true if ProjectSpace Policy is not Hold and Cancel
	     * @throws Exception if operation fails. 
	     */
	    public boolean isProjectNotInHoldOrCancel(Context context, String args[]) throws Exception {
	    
			boolean isNotProjectSpaceHoldCancel = true;
			Map programMap = (Map) JPO.unpackArgs(args);
			String objectId = (String) programMap.get("objectId");
			if(ProgramCentralUtil.isNullString(objectId))
			{
				return isNotProjectSpaceHoldCancel;
			}
			DomainObject dmoObject = DomainObject.newInstance(context,objectId);
			StringList selects = new StringList(2);
			selects.add(DomainConstants.SELECT_POLICY);
			selects.add(ProgramCentralConstants.SELECT_PROJECT_POLICY_FROM_TASK);
			selects.add(ProgramCentralConstants.SELECT_IS_PROJECT_TEMPLATE);
			Map objectInfo = dmoObject.getInfo(context, selects);
			String policyName = (String) objectInfo.get(DomainConstants.SELECT_POLICY);
			String isProjectTemplate= (String) objectInfo.get(ProgramCentralConstants.SELECT_IS_PROJECT_TEMPLATE);
			if("true".equalsIgnoreCase(isProjectTemplate)){
				ProjectTemplate projectTemplate = (ProjectTemplate)DomainObject.newInstance(context, DomainConstants.TYPE_PROJECT_TEMPLATE, DomainObject.PROGRAM);
		 		return projectTemplate.isOwnerOrCoOwner(context, objectId);
			}
			String projectPolicy = (String) objectInfo.get(ProgramCentralConstants.SELECT_PROJECT_POLICY_FROM_TASK);
	        if(ProgramCentralConstants.POLICY_PROJECT_SPACE_HOLD_CANCEL.equalsIgnoreCase(projectPolicy) || ProgramCentralConstants.POLICY_PROJECT_SPACE_HOLD_CANCEL.equalsIgnoreCase(policyName))
	                {
	        	      isNotProjectSpaceHoldCancel=false;
	                }
			return isNotProjectSpaceHoldCancel;
	  	 }
	    
	    @com.matrixone.apps.framework.ui.ProgramCallable
	    public MapList getProjectTemplatesAndProjects(Context context, String args[]) throws Exception {
	    	MapList templateobjectList = new MapList();
	    	MapList objectList = new MapList();
	    	try{
	    	StringList objectSelectables = new StringList(SELECT_ID);
	    	templateobjectList = findObjects(context, TYPE_PROJECT_TEMPLATE, null,SELECT_CURRENT + " == Released", objectSelectables);
	    	
			String whereClause=ProgramCentralConstants.SELECT_KINDOF_EXPERIMENT_PROJECT+"!=TRUE &&"+ProgramCentralConstants.SELECT_KINDOF_PROJECT_BASELINE+"!=TRUE";
		    //	String whereClause=SELECT_CURRENT + " == Released && "+SELECT_TYPE+" == 'Project Template' && "+ProgramCentralConstants.SELECT_KINDOF_EXPERIMENT_PROJECT+"!=TRUE &&" +ProgramCentralConstants.SELECT_KINDOF_PROJECT_BASELINE+"!=TRUE";
		    	String type =  TYPE_PROJECT_SPACE;
			objectList = findObjects(context, type, null,whereClause, objectSelectables);
				
				objectList.addAll(templateobjectList);
	    	}catch(Exception e){
	    		 throw new Exception(e);
	    	}
			return objectList;
	  	 }
	    

      public boolean hasAccessToAddGovernedItemsAction(Context context, String[] args) throws MatrixException {
       		try {
       			HashMap programMap = (HashMap) JPO.unpackArgs(args);
       			String objectId = (String) programMap.get("objectId");
       			ProjectSpace project = new ProjectSpace(objectId);
       			return project.hasAccessToAddGovernedItemsAction(context);
       		} catch (Exception e) {
       			throw new MatrixException(e);
       		}
      }

      public boolean hasAccessToRemoveGovernedItemsAction(Context context, String[] args) throws MatrixException {
       		try {
       			return this.hasAccessToAddGovernedItemsAction(context, args);
       		} catch (Exception e) {
       			throw new MatrixException(e);
       		}
      }

	    /**
		 * get Project Task List for Epxort Project
		 * @param context - The eMatrix <code>Context</code> object. 
		 * @param args holds object related information.
		 * @return current Year.
		 * @throws Exception if operation fails.
		 */
	    public StringList exportProjectTaskList(Context context, String args[]) throws Exception 
	    {
	    	long start = System.currentTimeMillis();
    	Map programMap = (Map) JPO.unpackArgs(args);
		String projectId = (String) programMap.get("objectId");
		String exportFormat = (String) programMap.get("exportFormat");
		String language = (String) programMap.get("language");
        String exportSubProject = (String) programMap.get("exportSubProject");
		
	    	if(ProgramCentralUtil.isNullString(projectId)) {
	    		throw new Exception("Invalid object id....");
	    	}

	    	ProjectSpace project = new ProjectSpace(projectId);
	
	    String seperator = ",";
	    if (!"CSV".equalsIgnoreCase(exportFormat)){
	        seperator = "\t";
	    }
	        final String SELECT_PREDECESSOR_LAG_TIMES_DURATION_UNIT = "from["+RELATIONSHIP_DEPENDENCY+"].attribute["+DependencyRelationship.ATTRIBUTE_LAG_TIME+"].inputunit";
	    	final String HAS_ASSIGNEE = "to[Assigned Tasks]";
	    
	    	StringList busSelects = new StringList(15);
	    	busSelects.add(ProgramCentralConstants.SELECT_PHYSICALID);
	    busSelects.add(SELECT_NAME);
	    busSelects.add(SELECT_TYPE);
	    busSelects.add(SELECT_TASK_ESTIMATED_DURATION);
	    busSelects.add(ProgramCentralConstants.SELECT_TASK_ESTIMATED_DURATION_INPUTUNIT);
	    busSelects.add(SELECT_TASK_ESTIMATED_START_DATE);
	    busSelects.add(SELECT_TASK_ESTIMATED_FINISH_DATE);
            busSelects.add(SELECT_DEFAULT_CONSTRAINT_TYPE);
            busSelects.add(SELECT_TASK_CONSTRAINT_DATE);

            Map projectInfo = project.getInfo(context, busSelects);
            projectInfo.put(SubtaskRelationship.SELECT_TASK_WBS, "0");

            busSelects.add(ProgramCentralConstants.SELECT_PROJECT_ID);
            busSelects.add(HAS_ASSIGNEE);
	    	busSelects.add(ProgramCentralConstants.SELECT_PREDECESSOR_PHYSICAL_IDS);
            busSelects.add(ProgramCentralConstants.SELECT_PREDECESSOR_PROJECT_ID);
	    busSelects.add(SELECT_PREDECESSOR_TYPES);
	    busSelects.add(SELECT_PREDECESSOR_LAG_TIMES);
	    	busSelects.add(SELECT_PREDECESSOR_LAG_TIMES_DURATION_UNIT);

	    busSelects.add(SELECT_TASK_CONSTRAINT_TYPE);
	    
            busSelects.add(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_PROJECT);
            busSelects.add(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_TASK);
        
        
	    	long process = System.currentTimeMillis();
	    	DomainObject palObj = project.getAccessListObject(context, project);
          
	    	String palId 		= palObj.getObjectId(context);

            String typePattern = TYPE_TASK_MANAGEMENT;
            if("True".equalsIgnoreCase(exportSubProject)){
            	typePattern = TYPE_PROJECT_MANAGEMENT;
            }

	    	ContextUtil.pushContext(context);
            MapList objectList = project.getRelatedObjects(
	    			context,                // context
                    Task.RELATIONSHIP_SUBTASK,    // relationshipPattern
                    typePattern,// typePattern
	    			busSelects,            // objectSelects
	    			null,                   // relationshipSelects
	    			false,                  // getTo,
	    			true,                   // getFrom
                    (short) 0,              // recurseToLevel
	    			null,                   // objectWhere
	    			null,                   // relationshipWhere
	    			(short) 0,              // limit
	    			false,                  // check hidden
	    			false,                  // prevent duplicates
	    			(short) 1000,           // page size
	    			null, null, null, null);// includeType, includeRelationship, postPatterns, relKeyPrefix
	    	ContextUtil.popContext(context);

	    	DebugUtil.debug("Total time from DB______"+(System.currentTimeMillis()-process));
	    	process = System.currentTimeMillis();

            StringList PALList = new StringList(palId);
            Map<String, String> projectIdToPALMapping = new HashMap<>();
           
            for(int i=0;i<objectList.size();i++) {
            	Map objectInfo = (Map)objectList.get(i);
            	String strPALId = (String)objectInfo.get(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_PROJECT);
            	String projectPhysicalId       = (String)objectInfo.get(ProgramCentralConstants.SELECT_PHYSICALID);
            	
            	if(ProgramCentralUtil.isNotNullString(strPALId) && !PALList.contains(strPALId)) {
            		projectIdToPALMapping.put(projectPhysicalId,strPALId);
            		PALList.add(strPALId);
            	}
            }
   
            Map<String, Dataobject> entireStructureSequenceData = new HashMap<String, Dataobject>();
            Map<String, String> PALtoWBSMapping = new HashMap<>();
            
            for(int i=0;i<PALList.size();i++) {
            	String strPALId = PALList.get(i);
            	ProjectSequence pal = new ProjectSequence(context, strPALId);
                List<Dataobject> subProjects = pal.getProjects(context);
                
                for(int j=0; j<subProjects.size(); j++) {
                	Dataobject subProjectObject          = subProjects.get(j);
                	String projectPhysicalId = subProjectObject.getId();
                	String projectPALId = projectIdToPALMapping.get(projectPhysicalId);

                    String projectWBS = (String) subProjectObject.getDataelements().get(ProgramCentralConstants.KEY_WBS_ID);
                    
                    if(projectWBS != "0") {
                    	String parentProjectWBS = PALtoWBSMapping.get(strPALId);
                    	if(ProgramCentralUtil.isNotNullString(parentProjectWBS)) {
                    		projectWBS = parentProjectWBS+"."+projectWBS;
                    	}
                    	PALtoWBSMapping.put(projectPALId, projectWBS);
                    }

                }


	    	Map<String, Dataobject> projectSeqData = pal.getSequenceData(context);

            	entireStructureSequenceData.putAll(projectSeqData);
            }

	    	DebugUtil.debug("Total time from PAL______"+(System.currentTimeMillis()-process));
	    	process = System.currentTimeMillis();

	    	objectList.forEach(map->{
	    		Map<String,String> taskMap 	= (Map<String, String>) map;
	    		String taskPhysicalId 		= taskMap.get(ProgramCentralConstants.SELECT_PHYSICALID);
                String taskPALId       = taskMap.get(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_TASK);
                String parentProjectWBS = EMPTY_STRING;
                Dataobject taskObj          = entireStructureSequenceData.get(taskPhysicalId);
                
                //Subproject
                if(ProgramCentralUtil.isNullString(taskPALId)) {
                	taskPALId       = taskMap.get(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_PROJECT);
                	parentProjectWBS = PALtoWBSMapping.get(taskPALId);
                	taskMap.put(SubtaskRelationship.SELECT_TASK_WBS,parentProjectWBS);
                }
                //Task from subproject
                else if(ProgramCentralUtil.isNotNullString(taskPALId) && ProgramCentralUtil.isNotNullString(PALtoWBSMapping.get(taskPALId))) {

                	parentProjectWBS = PALtoWBSMapping.get(taskPALId);
                	taskMap.put(SubtaskRelationship.SELECT_TASK_WBS,parentProjectWBS+"."+(String) taskObj.getDataelements().get(ProgramCentralConstants.KEY_WBS_ID));
                } 
                //Root project tasks
                else {
	    		taskMap.put(SubtaskRelationship.SELECT_TASK_WBS,(String) taskObj.getDataelements().get(ProgramCentralConstants.KEY_WBS_ID));

                }

	    	});

            objectList.add(0, projectInfo);
            objectList.sortStructure(context, SubtaskRelationship.SELECT_TASK_WBS, "ascending", "emxWBSColumnComparator");

	    	DebugUtil.debug("Total time process and sort______"+(System.currentTimeMillis()-process));
	    	process = System.currentTimeMillis();


	    	Map projectMap = (Map) objectList.get(0); //Project info map

	    	StringList taskList = new StringList();

	    //Internationalized Heading
	    StringBuffer headerLine = new StringBuffer();
        

	    String exportFileHeader = EnoviaResourceBundle.getProperty(context,"emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.ColumnName");
	    headerLine.append(exportFileHeader);
	    	if(exportFormat.equalsIgnoreCase("CSV")) {
	    taskList.add(headerLine.toString());
	    	}

	    SimpleDateFormat sdf = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
	    SimpleDateFormat date = new SimpleDateFormat("MM/dd/yy");

	    NumberFormat _df = NumberFormat.getInstance(Locale.US);
		_df.setMaximumFractionDigits(2);
		_df.setGroupingUsed(false);

	    String projectConstraintDate ="";
	    String projectEstimatedDuration = (String)projectMap.get(SELECT_TASK_ESTIMATED_DURATION);
	    String projectEstimatedDurationUnit = (String)projectMap.get(ProgramCentralConstants.SELECT_TASK_ESTIMATED_DURATION_INPUTUNIT);
	    if("h".equalsIgnoreCase(projectEstimatedDurationUnit)&& ProgramCentralUtil.isNotNullString(projectEstimatedDuration)){
			double duration = Task.parseToDouble(projectEstimatedDuration);    				
			duration = duration * 8;
			duration = Task.parseToDouble(_df.format(duration));
			projectEstimatedDuration = String.valueOf(duration);
        }

        String projectEstimatedStartDate = (String) projectMap.get(SELECT_TASK_ESTIMATED_START_DATE);
        if (ProgramCentralUtil.isNotNullString(projectEstimatedStartDate)){
            Date startDate = sdf.parse(projectEstimatedStartDate);
            projectEstimatedStartDate = date.format(startDate);
        }
         String projectEstimatedFinishDate = (String) projectMap.get(SELECT_TASK_ESTIMATED_FINISH_DATE);
        if (ProgramCentralUtil.isNotNullString(projectEstimatedFinishDate)){
            Date finishDate = sdf.parse(projectEstimatedFinishDate);
            projectEstimatedFinishDate = date.format(finishDate);
        }
        
        String projectDate = (String) projectMap.get(SELECT_TASK_CONSTRAINT_DATE);
        if (ProgramCentralUtil.isNotNullString(projectDate)){
            Date project_Date = sdf.parse(projectDate);
            projectConstraintDate = date.format(project_Date);
        }
	    headerLine.replace(0, headerLine.length(), "0"); //WBS
	    headerLine.append(seperator);
	    headerLine.append((String) projectMap.get(SELECT_NAME)); //Name
	    headerLine.append(seperator);

	    String strProjectType = (String) projectMap.get(SELECT_TYPE);
	    headerLine.append(strProjectType); //Type
	    headerLine.append(seperator); //Type
	    headerLine.append(projectEstimatedDuration + " "+ projectEstimatedDurationUnit);
	    headerLine.append(seperator);
	    headerLine.append(projectEstimatedStartDate); // Start Date
	    headerLine.append(seperator);
	    headerLine.append(projectEstimatedFinishDate); // Finish Date
	    headerLine.append(seperator);
	    headerLine.append(seperator); // Dependency and Assignee
	    headerLine.append(seperator);
        headerLine.append((String) projectMap.get(SELECT_DEFAULT_CONSTRAINT_TYPE));
	    headerLine.append(seperator);
	    headerLine.append((String) projectConstraintDate);
	    taskList.add(headerLine.toString());

	    	String assigneeNames = EMPTY_STRING;
	    	for(int in=1,size=objectList.size();in<size;in++) {

	    		Map thisTask 		= (Map) objectList.get(in);
	    		String taskId 		= (String) thisTask.get(ProgramCentralConstants.SELECT_PHYSICALID);
	    		String hasAssignee 	= (String) thisTask.get(HAS_ASSIGNEE);
	    		//if it has assignee
	    		assigneeNames = EMPTY_STRING;
	    		if("true".equalsIgnoreCase(hasAssignee)) {
	    StringList taskSelects = new StringList(2);
	    taskSelects.add(SELECT_ID);
	    taskSelects.add(SELECT_NAME);

	    			Task currentTask 		= new Task(taskId);
	        MapList assigneesList = currentTask.getAssignees(context, taskSelects, null, null);

	    			StringBuilder assigneeSB = new StringBuilder();
	    			assigneeSB.append("\"");

	        Iterator assigneeItr = assigneesList.iterator();
	        while (assigneeItr.hasNext()) {
	            Map thisAssignee = (Map) assigneeItr.next();
	            String assigneeName = PersonUtil.getFullName(context, (String) thisAssignee.get(SELECT_NAME));
	            assigneeSB.append(assigneeName);
	            if (assigneeItr.hasNext()){
	                assigneeSB.append(",");
	            } else {
	                assigneeSB.append("\"");
	            }
	        }
	        if (assigneeSB.toString().equals("\"")){
	            assigneeSB.append("\"");
	        }

	    			assigneeNames = assigneeSB.toString();
	    		}

            String projectObjectId = (String)thisTask.get(ProgramCentralConstants.SELECT_PROJECT_ID);
	        String preString = "";
	    		Object listPreds 				= (Object) thisTask.get(ProgramCentralConstants.SELECT_PREDECESSOR_PHYSICAL_IDS);
	        Object listTypes = (Object) thisTask.get(SELECT_PREDECESSOR_TYPES);
	        Object listLagTimes = (Object) thisTask.get(SELECT_PREDECESSOR_LAG_TIMES);
            Object listLagTimesDurationUnit = (Object) thisTask.get(SELECT_PREDECESSOR_LAG_TIMES_DURATION_UNIT); 
            Object listPredecessorProjectIds = (Object) thisTask.get(ProgramCentralConstants.SELECT_PREDECESSOR_PROJECT_ID);
	    		Object defaultDurationUnit = (Object) ProgramCentralConstants.DEFAULT_DURATION_UNIT; 

	        if (listPreds == null) {
	            preString = "";
	        } else if (listPreds instanceof String){
            	if(ProgramCentralUtil.isNotNullString(projectObjectId) && projectObjectId.equals((String)listPredecessorProjectIds)) {
            		Dataobject predecessor = entireStructureSequenceData.get((String) listPreds);
	    			if (predecessor != null){
	    			String seqNo = (String) predecessor.getDataelements().get(ProgramCentralConstants.KEY_SEQ_ID);
	    			//String preId = (String)thisTask.get((String) listPreds);
	    			preString = seqNo+ ":" + (String) listTypes;

	    			if (ProgramCentralUtil.isNotNullString((String)listLagTimes)
	    					&& ProgramCentralUtil.isNotNullString((String)listLagTimesDurationUnit)){
	    						String lagTimeDays = EMPTY_STRING;
	    						if(!listLagTimesDurationUnit.equals(defaultDurationUnit) && ProgramCentralUtil.isNotNullString((String)listLagTimes)) {
	    							float lagTime = Float.parseFloat((String)listLagTimes)*8;           //lag time in days 
	    							lagTimeDays = String.valueOf(lagTime);
	    						}else {
	    							lagTimeDays= (String)listLagTimes;
	    						}

	              if(((String)listLagTimes).startsWith("-")){
	    							preString += (String) lagTimeDays + " " + listLagTimesDurationUnit;
	              } else {
	    							preString += "+" + (String) lagTimeDays + " " + listLagTimesDurationUnit;
	              }
	            }
	         }
            	}
	        } else if (listPreds instanceof StringList) {
	            StringList sl = (StringList) listPreds;
	            StringList st = (StringList) listTypes;
	            StringList slag = (StringList) listLagTimes;
	    			StringList slagUnit = (StringList) listLagTimesDurationUnit;
            	StringList predecessorProjectIdList = (StringList)listPredecessorProjectIds;

	            boolean foundExternal = false;

	            for (int i =0; i<sl.size(); i++) {
	                if(i!=0){
	                	if(preString.length() > 1)
	                  		preString=preString+",";
	                } else {
	                  preString="\""+preString;
	                }
            		String predecessorProjectId = predecessorProjectIdList.get(i);
            		if(ProgramCentralUtil.isNotNullString(projectObjectId) && projectObjectId.equals(predecessorProjectId)) {

            			Dataobject predecessor = entireStructureSequenceData.get((String) sl.elementAt(i));
	    			if (predecessor != null) {
	    				String seqNo = (String) predecessor.getDataelements().get(ProgramCentralConstants.KEY_SEQ_ID);

	    				//String preId = (String)wbsIndexMap.get((String) sl.elementAt(i));
	    				preString = preString + seqNo + ":" + (String) st.elementAt(i);
	    				if (slag != null && !"".equals(slag)){

	    							String lagTimeDays = EMPTY_STRING;
	    							if(!slagUnit.get(i).equals(defaultDurationUnit) && ProgramCentralUtil.isNotNullString((String)slag.get(i))) {
	    								float lagTime = Float.parseFloat((String)slag.get(i))*8;           //lag time in days 
	    								lagTimeDays = String.valueOf(lagTime);
	    							}else {
	    								lagTimeDays= (String)slag.get(i);
	    							}


	    							if (((String)slag.get(i)).startsWith("-")){
	    								preString += lagTimeDays + " " + slagUnit.get(i);
	                  } else {
	    								preString += "+" + lagTimeDays + " " + slagUnit.get(i); 
	                  }
	                }
	           		}
            		}
	            }//end for loop
	            if(sl.size()>0){
	              preString=preString+"\"";
	            }

	        }

            String taskEstDurationUnit = (String) thisTask.get(ProgramCentralConstants.SELECT_TASK_ESTIMATED_DURATION_INPUTUNIT);
            String taskEstDuration= (String) thisTask.get(SELECT_TASK_ESTIMATED_DURATION);
            if("h".equalsIgnoreCase(taskEstDurationUnit)&& ProgramCentralUtil.isNotNullString(taskEstDuration)){
				double duration = Task.parseToDouble(taskEstDuration);    				
				duration = duration * 8;
				duration = Task.parseToDouble(_df.format(duration));
				taskEstDuration = String.valueOf(duration);
            }
            
            
            String taskEstimatedStartDate = (String) thisTask.get(SELECT_TASK_ESTIMATED_START_DATE);
            if (ProgramCentralUtil.isNotNullString(taskEstimatedStartDate)){
                Date startDate = sdf.parse(taskEstimatedStartDate);
                taskEstimatedStartDate = date.format(startDate);
            }
             String taskEstimatedFinishDate = (String) thisTask.get(SELECT_TASK_ESTIMATED_FINISH_DATE);
            if (ProgramCentralUtil.isNotNullString(taskEstimatedFinishDate)){
                Date finishDate = sdf.parse(taskEstimatedFinishDate);
                taskEstimatedFinishDate = date.format(finishDate);
            }
            
            String taskConstraintDate = (String) thisTask.get(SELECT_TASK_CONSTRAINT_DATE);
            if (ProgramCentralUtil.isNotNullString(taskConstraintDate)){
                Date constraintDateForTask = sdf.parse(taskConstraintDate);
                taskConstraintDate = date.format(constraintDateForTask);
            }

            String taskConstraintType = (String)thisTask.get(SELECT_TASK_CONSTRAINT_TYPE);
            if(ProgramCentralUtil.isNullString(taskConstraintType)) {
            	taskConstraintType = (String)thisTask.get(SELECT_DEFAULT_CONSTRAINT_TYPE);
            }
            		
	        StringBuffer thisLine = new StringBuffer();
	        thisLine.append(thisTask.get(SubtaskRelationship.SELECT_TASK_WBS) + seperator);
	        thisLine.append(thisTask.get(SELECT_NAME) + seperator);
	        thisLine.append(thisTask.get(SELECT_TYPE) + seperator);
	        thisLine.append(taskEstDuration + " " + taskEstDurationUnit+ seperator);
	        thisLine.append(taskEstimatedStartDate + seperator);
	        thisLine.append(taskEstimatedFinishDate + seperator);
	        thisLine.append(preString + seperator);
	    		thisLine.append(assigneeNames + seperator);
                thisLine.append(taskConstraintType + seperator);
	        thisLine.append(taskConstraintDate);
	        taskList.add(thisLine.toString());
	    	}
	    	DebugUtil.debug("Total time for final process ______"+(System.currentTimeMillis()-process));

	    	DebugUtil.debug("Toatl time for project export......"+(System.currentTimeMillis()-start));

	    return taskList;
	}
	/**
	 * Get current Year for Labor Report Options
	 * @param context - The eMatrix <code>Context</code> object. 
	 * @param args holds object related information.
	 * @return current Year.
	 * @throws Exception if operation fails.
	 */
	public String getCurrentYear(Context context,String[]args)throws Exception
	{
		Date currDate = new Date();
		String currYear = Integer.toString(currDate.getYear()+1900);
		return currYear;
	}
	    
	/**
	 * Update Project Schedule attribute on Project Preference page.
	 * @param context -  The ENOVIA <code>Context</code> object.
	 * @param args - Holds information about context object.
	 * @throws Exception - If operation fails.
	 */
	public void updateProjectScheduleAttribute(Context context,String[]args)throws Exception
	{
		Map programMap 			= (Map)JPO.unpackArgs(args);
		Map paramMap 			= (Map)programMap.get("paramMap");
		String newScheduleValue = (String)paramMap.get("New Value");
		String projectId 		= (String)paramMap.get("objectId");

		ProjectSpace project = new ProjectSpace(projectId);
		project.setAttributeValue(context, 
				ProgramCentralConstants.ATTRIBUTE_PROJECT_SCHEDULE, 
				newScheduleValue);

		if(ProgramCentralConstants.PROJECT_SCHEDULE_AUTO.equalsIgnoreCase(newScheduleValue)){
			TaskDateRollup.rolloutProject(
					context,
					new StringList(projectId),
					true);
		}
	}

	/**
	 * Update Project Schedule Based On attribute on Project Preference page.
	 * @param context -  The ENOVIA <code>Context</code> object.
	 * @param args - Holds information about context object.
	 * @throws Exception - If operation fails.
	 */
	public void updateProjectScheduleBasedOn(Context context, String[] args)
			throws Exception
	{
		Map programMap 			= (Map)JPO.unpackArgs(args);
		Map paramMap 			= (Map)programMap.get("paramMap");
		String newScheduleBasedOnValue = (String)paramMap.get("New Value");
		String projectId 		= (String)paramMap.get("objectId");

		ProjectSpace project = new ProjectSpace(projectId);
		project.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_PROJECT_SCHEDULE_BASED_ON, newScheduleBasedOnValue);
		String projectSchedule = project.getInfo(context, ProgramCentralConstants.SELECT_PROJECT_SCHEDULE);

		if(ProgramCentralConstants.PROJECT_SCHEDULE_AUTO.equalsIgnoreCase(projectSchedule)){
			TaskDateRollup.rolloutProject(context,new StringList(projectId),true);
		}
	}

    public void updatePercentCompleteBasedOn(Context context, String[] args) throws Exception
    {
        Map programMap          = (Map)JPO.unpackArgs(args);
        Map paramMap            = (Map)programMap.get("paramMap");
        String newPercentCompleteBasedOnValue = (String)paramMap.get("New Value");
        String projectId        = (String)paramMap.get("objectId");

        ProjectSpace project = new ProjectSpace(projectId);
        try {
            project.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_PERCENT_COMPLETE_BASED_ON, newPercentCompleteBasedOnValue);

        }catch (Exception e) {
            if(ContextUtil.isTransactionActive(context)) {
                ContextUtil.abortTransaction(context);
            }
            throw new MatrixException(e);
        }finally{
            if(ContextUtil.isTransactionActive(context)){
                ContextUtil.commitTransaction(context);
            }
        }
        StringList selectable = new StringList(1);
        selectable.add(ProgramCentralConstants.SELECT_PROJECT_SCHEDULE);
    
        Map<String, String> projectInfo = project.getInfo(context, selectable);
        String projectSchedule = projectInfo.get(ProgramCentralConstants.SELECT_PROJECT_SCHEDULE);

        if(ProgramCentralConstants.PROJECT_SCHEDULE_AUTO.equalsIgnoreCase(projectSchedule)){
            TaskDateRollup.rolloutProject(context,new StringList(projectId),true);
        }
    }
    
   @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getAssignedActiveProjects(Context context, String[] args)
      throws MatrixException
    {
        return getProjectList(context, args, "AssignedActive");
    }
    
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getCredentialedActiveProjects(Context context, String[] args)
      throws MatrixException
    {
        return getProjectList(context, args, "CredentialedActive");
    }
    
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getArchiveCancelCompleteProjects(Context context, String[] args)
      throws MatrixException
    {
        return getProjectList(context, args, "Inactive");
    }
    
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getAllOtherProjects(Context context, String[] args)
      throws MatrixException
    {
        return getProjectList(context, args, "All");
    }
    
    /**
     * This method is used to gets the list of projects based on mode passsed.
     * Used for PMCProjectSpaceMyDesk table
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @param busWhere optional business object where clause
     * @return MapList containing the id of projects owned by the user.
     * @throws Exception if the operation fails
     * @since R418.HF18
     */
    private MapList getProjectList(Context context, String[] args, String Mode) throws MatrixException
    {
    	// Check license while listing Project Concepts, Project Space, if license check fails here
    	// the projects will not be listed. This is mainly done to avoid Project Concepts from being listed
    	// but as this is the common method, the project space objects will also not be listed.
    	//
    	ComponentsUtil.checkLicenseReserved(context,ProgramCentralConstants.PGE_LICENSE_ARRAY);


    	MapList projectList = new MapList();
    	StringList busSelects = new StringList(2);
    	busSelects.add(ProjectSpace.SELECT_ID);
    	busSelects.add(ProjectSpace.SELECT_NAME);
    	busSelects.add(SELECT_ATTRIBUTE_TASK_ESTIMATED_END_DATE);
    	busSelects.add(ProjectSpace.SELECT_VAULT);

    	String busWhere = "";
    	try{
    		if("AssignedActive".equalsIgnoreCase(Mode)){
    			projectList = getActiveProjects(context, args);
    		}else{
	    			if (ProgramCentralUtil.isNullString(busWhere)) {
	                	busWhere +=" type!=" + ProgramCentralConstants.TYPE_EXPERIMENT+ " && type!='" + ProgramCentralConstants.TYPE_PROJECT_BASELINE+"'"; 
	                } else {
	                busWhere +=" && type!=" + ProgramCentralConstants.TYPE_EXPERIMENT+ " && type!='" + ProgramCentralConstants.TYPE_PROJECT_BASELINE+"'";
	                }
    			if("CredentialedActive".equalsIgnoreCase(Mode)){
	    				String notProjectMemberBusWhere = "!from[Member].to.name smatchlist '"+context.getUser()+"' ','";
    				busWhere += ProgramCentralUtil.isNullString(busWhere) ? EMPTY_STRING : " && ";
    				busWhere += "current!=" + STATE_PROJECT_COMPLETE + " && current!=" + STATE_PROJECT_ARCHIVE +
    						" && current!=" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD + 
    						" && current!=" + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL ;
	    				busWhere += "&& "+notProjectMemberBusWhere;
	    				
    			}else if("Inactive".equalsIgnoreCase(Mode)){
    				busWhere += ProgramCentralUtil.isNullString(busWhere) ? EMPTY_STRING : " && ";
    				busWhere += "current==" + STATE_PROJECT_COMPLETE + " || current==" + STATE_PROJECT_ARCHIVE+" || current==" + STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL+" || current==" + STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD;    		   
    			}
	    			
	    			
    			String typePattern = TYPE_PROJECT_SPACE + "," + TYPE_PROJECT_CONCEPT;
	    			projectList = DomainObject.findObjects(context, typePattern, "*", "*", "*",DomainConstants.QUERY_WILDCARD, busWhere, true, busSelects);
    		} 

    		return projectList;		

	    	}
	    	catch (Exception ex) {
	    		throw new MatrixException(ex);
	    	}
	    }
	    
	    public boolean showCopyFromProjectField(Context context, String args[]) throws Exception {
	    	HashMap programMap = (HashMap)JPO.unpackArgs(args);
			
			String  isCopyFrom = (String) programMap.get("copyFrom");
			
			if("Project".equalsIgnoreCase(isCopyFrom)) 
				return true;
			
			return false;
		}
	    
	    public boolean showCopyFromFileField(Context context, String args[]) throws Exception {
	    	HashMap programMap = (HashMap)JPO.unpackArgs(args);
			
			String  isCopyFrom = (String) programMap.get("copyFrom");
			
			if("File".equalsIgnoreCase(isCopyFrom)) 
				return true;
			
			return false;
		}
	    
		@com.matrixone.apps.framework.ui.ProgramCallable
	    public String getReferenceDocumentCopyOption(Context context, String args[]) throws Exception
		{
			StringBuffer strHTMLBuffer = new StringBuffer();

			String strLanguage=context.getSession().getLanguage();
			String docCopy = EnoviaResourceBundle.getProperty(context,ProgramCentralConstants.PROGRAMCENTRAL, "emxProgramCentral.Common.Copy", strLanguage);
			String docConnect = EnoviaResourceBundle.getProperty(context,ProgramCentralConstants.PROGRAMCENTRAL, "emxProgramCentral.ReferenceDocument.Reference", strLanguage);
			
			strHTMLBuffer.append("<input type='radio' name='ReferenceDocument'  value='Copy' checked='true' />");
			strHTMLBuffer.append(docCopy);
			strHTMLBuffer.append("<input type='radio' name='ReferenceDocument'  value='Reference' />");
			strHTMLBuffer.append(docConnect);

			return strHTMLBuffer.toString();
		}
	   
		/**
		 * This method will return true if the selected table is other than mentioned table names.
		 * @param context
		 * @param args
		 * @return true if the selected table is other than mentioned table names.
		 * @throws Exception
		 */
		@com.matrixone.apps.framework.ui.ProgramCallable
		public boolean hasAccessToPMCCalculateFloatAndCriticalPathCommand(Context context,String args[]) throws Exception
		{
           
			boolean hasAccess = true;
			try {
				HashMap inputMap = (HashMap)JPO.unpackArgs(args);
				
				String selectedTable = (String)inputMap.get("selectedTable");
				String selectedProgram = (String)inputMap.get("selectedProgram");
				if(ProgramCentralUtil.isNullString(selectedProgram)){
					selectedProgram = EMPTY_STRING;
				}
				StringList excludeTableList = new StringList();
				excludeTableList.add("PMCWBSAllocationViewTable");
				excludeTableList.add("PMCWBSAssignmentViewTable");
				excludeTableList.add("PMCWBSViewTable");
				excludeTableList.add("PMCProjectTaskEffort");
				excludeTableList.add("PMCWBSForecastViewTable");
				excludeTableList.add("PMCWBSViewTableENXSBGrid");
				excludeTableList.add("PMCWBSAllocationViewTableENXSBGrid");
				excludeTableList.add("PMCWBSAssignmentViewTableENXSBGrid");
				excludeTableList.add("PMCProjectTaskEffortENXSBGrid");
				excludeTableList.add("PMCWBSForecastViewTableENXSBGrid");

				if(ProgramCentralUtil.isNotNullString(selectedTable) && ((excludeTableList.contains(selectedTable)) || selectedTable.contains("~") || selectedProgram.contains("emxTask:getWBSDeletedSubtasks"))){
					hasAccess = false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return hasAccess;
		}
	   

	/**
	 * Returns Maplist containing All Project that can be added as resolving project for risk or opportunity  
	 * @param context
	 * @param args
	 * @return
	 */
	@com.matrixone.apps.framework.ui.ProgramCallable
	public MapList getRIOItems(Context context, String args[]) throws Exception {
		MapList objectList = new MapList();
		try{


			Map programMap = (Map) JPO.unpackArgs(args);
			String objectId=(String) programMap.get("objectId");
			ProjectSpace project = new ProjectSpace();
			project.setId(objectId);

			objectList=project.getRIOItems(context);

		}catch (Exception e) {
			e.printStackTrace();
}
		return objectList;
	}

	/*
	 * Risk, Opportunity and Issue list
	 * */
	@com.matrixone.apps.framework.ui.ProgramCallable
	public MapList getResolvesItemsForAProject(Context context,String args[]) throws Exception
	{final String SELECT_RISK_ACCESS     = "current.access[modify]";
	MapList resolvesItems = new MapList();
	Map programMap = (Map) JPO.unpackArgs(args);
	String parentOId = (String) programMap.get("parentOID");
	String projectId =parentOId;
	if(null==projectId) {
		projectId= (String) programMap.get("objectId");}

	DomainObject objParent = DomainObject.newInstance(context,projectId);
	StringList busSelectList = new StringList(2);
	StringList relSelectList1 = new StringList(2);
	busSelectList.add(SELECT_ID);
	//busSelectList.add(SELECT_CURRENT);
	busSelectList.add(SELECT_TYPE);
	//busSelectList.add(SELECT_NAME);
	//busSelectList.add(SELECT_DESCRIPTION);
	//busSelectList.add(RiskManagement.SELECT_ESTIMATED_START_DATE);
	//busSelectList.add(RiskManagement.SELECT_ESTIMATED_END_DATE);
	//busSelectList.add(RiskManagement.SELECT_ACTUAL_START_DATE);
	//busSelectList.add(RiskManagement.SELECT_ACTUAL_END_DATE);
	busSelectList.add(ProgramCentralConstants.SELECT_IS_RISK);
	busSelectList.add(ProgramCentralConstants.SELECT_IS_ISSUE);
	busSelectList.add(RiskManagement.SELECT_RISK_VISIBILITY);
	busSelectList.add(SELECT_RISK_ACCESS);
	relSelectList1.add(RiskRPNRelationship.SELECT_ID);

	String sRelPattern = RELATIONSHIP_RESOLUTION_PROJECT+","+RELATIONSHIP_RSOLVED_TO; 
	String sTypePattern = TYPE_RISK+","+TYPE_OPPORTUNITY+","+TYPE_ISSUE;

	resolvesItems = objParent.getRelatedObjects(context,sRelPattern,sTypePattern,busSelectList,relSelectList1,true,false,(short) 0,DomainObject.EMPTY_STRING,DomainObject.EMPTY_STRING,0);

	// If a Risk is restricted and User has no modify access, Risk should not be displayed in RIO item
	Iterator<Map> objectlistIterator = resolvesItems.iterator();
	while(objectlistIterator.hasNext()){
		Map objectInfo = objectlistIterator.next();

		String isRiskType = (String) objectInfo.get(ProgramCentralConstants.SELECT_IS_RISK);
		if("TRUE".equalsIgnoreCase(isRiskType)){
			String sVisibility = (String) objectInfo.get(RiskManagement.SELECT_RISK_VISIBILITY);
			String sAccess = (String) objectInfo.get(SELECT_RISK_ACCESS);
			if ("Restricted".equalsIgnoreCase(sVisibility) && ! "TRUE".equalsIgnoreCase(sAccess)){
				objectlistIterator.remove();
			}
		}
	}

	return resolvesItems;
}

    @com.matrixone.apps.framework.ui.ProgramCallable
    public Map getDefaultCategoryList(Context context, String args[]) throws MatrixException{
        try{
            Map programMap      = (Map) JPO.unpackArgs(args);
            Map paramMap        = (Map) programMap.get("paramMap");
            String objectId     = (String) paramMap.get("objectId");

            String lang = context.getSession().getLanguage();
            StringList displayList = new StringList();
            StringList actualList = new StringList();

            HashMap scheduleCmdMap      = UIMenu.getCommand(context, "PMCSchedule");    
            HashMap treeMenuMap         = UIMenu.getMenu(context, "type_ProjectSpace");
            String menuLabel            = UIComponent.getLabel(treeMenuMap);
            String regSuite             = UIComponent.getSetting(treeMenuMap, "Registered Suite");
            if(menuLabel != null && menuLabel.contains("{NAME}")) {
                actualList.add("Default");
                displayList.add(EnoviaResourceBundle.getProperty(context, regSuite,"emxProgramCentral.Common.Default",lang));
            }

            MapList treeMenuChildren    = (MapList) treeMenuMap.get("children");
            for (Object child : treeMenuChildren) {
                Map<String,String> childMap = (Map) child;
                String cmdName = childMap.get("name");
                if("PMCSchedule".equalsIgnoreCase(cmdName)) {
                    actualList.add(cmdName);

                    HashMap cmdMap  = UIMenu.getCommand(context, cmdName);
                    String cmdLabel = UIComponent.getLabel(cmdMap);
                    regSuite = UIComponent.getSetting(cmdMap, "Registered Suite");
                    String displayValue = EnoviaResourceBundle.getProperty(context, regSuite,cmdLabel,lang);
                    displayList.add(displayValue);
                    break;
                }
            }

            Map rangeValueMap = new HashMap();
            rangeValueMap.put("field_choices", actualList);
            rangeValueMap.put("field_display_choices", displayList);

            return rangeValueMap;
        }catch(Exception e){
            throw new MatrixException(e);
        }
    } 
    @com.matrixone.apps.framework.ui.PostProcessCallable
    public String getDefaultCategory(Context context, String args[]) throws Exception {
        Map programMap      = (Map) JPO.unpackArgs(args);
        Map paramMap        = (Map) programMap.get("paramMap");
        String objectId     = (String) paramMap.get("objectId");
        String categoryName = EMPTY_STRING;

        String lang = context.getSession().getLanguage();
        ProjectSpace project = new ProjectSpace(objectId);
        Dataobject preference = project.getUserPreference(context);
        if(preference == null) {
            categoryName = EnoviaResourceBundle.getProperty(context, "ProgramCentral","emxProgramCentral.Common.Default",lang);
        }else {
            categoryName = (String)preference.getDataelements().get("defCat");
            if("Default".equalsIgnoreCase(categoryName)) {
                categoryName = EnoviaResourceBundle.getProperty(context, "ProgramCentral","emxProgramCentral.Common.Default",lang);
            }else {
                HashMap cmdMap  = UIMenu.getCommand(context, categoryName);
                String cmdLabel = UIComponent.getLabel(cmdMap);
                String regSuite = UIComponent.getSetting(cmdMap, "Registered Suite");
                categoryName = EnoviaResourceBundle.getProperty(context, regSuite,cmdLabel,lang);
            }
        }

        return categoryName;
    }

    @com.matrixone.apps.framework.ui.ProgramCallable
    public Map getDefaultTabList(Context context, String args[]) throws Exception{
        Map programMap      = (Map) JPO.unpackArgs(args);
        Map paramMap        = (Map) programMap.get("paramMap");
        String objectId     = (String) paramMap.get("objectId");
        
        StringList displayList = new StringList();
        StringList actualList = new StringList();

        String lang = context.getSession().getLanguage();
        ProjectSpace project = new ProjectSpace(objectId);
        Dataobject preference = project.getUserPreference(context);
        if(preference != null) {
            String categoryName = (String)preference.getDataelements().get("defCat");
            if("PMCSchedule".equalsIgnoreCase(categoryName)) {
                actualList.add("PMCWBS");
                
                if(ProgramCentralUtil.isDataGridEnabled(context)) {
                	actualList.add("PMCWBSDataGridView");
                }
                actualList.add("PMCWBSFlatView");
                actualList.add("PMCGantt");
                
                for (String cmdName : actualList) {
                    HashMap cmdMap  = UIMenu.getCommand(context, cmdName);
                    String cmdLabel = UIComponent.getLabel(cmdMap);
                    String regSuite = UIComponent.getSetting(cmdMap, "Registered Suite");
                    String displayValue = EnoviaResourceBundle.getProperty(context, regSuite,cmdLabel,lang);
                    displayList.add(displayValue);
                }
            }
        }
        Map mAttributeRange = new HashMap();
        mAttributeRange.put("field_choices", actualList);
        mAttributeRange.put("field_display_choices", displayList);

        return mAttributeRange;

    }

    /**
     * Get selected task policy.
     * @param context - The ENOVIA <code>Context</code> object.
     * @param args - The args hold information about object.
     * @return selected policy
     * @throws Exception If operation fails.
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public Map getDefaultTabListForSelectedCategory(Context context, String[] args)throws Exception
    {
        Map programMap          = JPO.unpackArgs(args);
        Map fieldMap            = (Map)programMap.get("fieldValues");
        String categoryName     = (String) fieldMap.get("DefaultCategory");

        StringList fieldRangeValues = new StringList();
        StringList fieldDisplayRangeValues = new StringList();

        String lang = context.getSession().getLanguage();
        if("PMCSchedule".equalsIgnoreCase(categoryName)) {
            fieldRangeValues.add("PMCWBS");
            if(ProgramCentralUtil.isDataGridEnabled(context)) {
            	fieldRangeValues.add("PMCWBSDataGridView");
            }
            fieldRangeValues.add("PMCWBSFlatView");
            fieldRangeValues.add("PMCGantt");
            
            for (String cmdName : fieldRangeValues) {
                HashMap cmdMap  = UIMenu.getCommand(context, cmdName);
                String cmdLabel = UIComponent.getLabel(cmdMap);
                String regSuite = UIComponent.getSetting(cmdMap, "Registered Suite");
                String displayValue = EnoviaResourceBundle.getProperty(context, regSuite,cmdLabel,lang);
                fieldDisplayRangeValues.add(displayValue);
            }
        }else {
            fieldRangeValues.add("Default");
            fieldDisplayRangeValues.add(EnoviaResourceBundle.getProperty(context, "ProgramCentral","emxProgramCentral.Common.Default",lang));
        }
        
        Map tabRangeInfoMap =   new HashMap();
        tabRangeInfoMap.put("RangeValues", fieldRangeValues);
        tabRangeInfoMap.put("RangeDisplayValues", fieldDisplayRangeValues);
        
        return  tabRangeInfoMap;
    }

    @com.matrixone.apps.framework.ui.PostProcessCallable
    public String getDefaultTab(Context context, String args[]) throws Exception {
        Map programMap      = (Map) JPO.unpackArgs(args);
        Map paramMap        = (Map) programMap.get("paramMap");
        String objectId     = (String) paramMap.get("objectId");
        String tabName = EMPTY_STRING;

        String lang = context.getSession().getLanguage();
        ProjectSpace project = new ProjectSpace(objectId);
        Dataobject preference = project.getUserPreference(context);
        if(preference == null) {
            tabName = EnoviaResourceBundle.getProperty(context, "ProgramCentral","emxProgramCentral.Common.Default",lang);
        }else {
            tabName = (String)preference.getDataelements().get("defTab");
            if("Default".equalsIgnoreCase(tabName)) {
                tabName = EnoviaResourceBundle.getProperty(context, "ProgramCentral","emxProgramCentral.Common.Default",lang);
            }else {
                HashMap cmdMap  = UIMenu.getCommand(context, tabName);
                String cmdLabel = UIComponent.getLabel(cmdMap);
                String regSuite = UIComponent.getSetting(cmdMap, "Registered Suite");
                tabName = EnoviaResourceBundle.getProperty(context, regSuite,cmdLabel,lang);
            }
        }

        return tabName;
    }

    @com.matrixone.apps.framework.ui.PostProcessCallable
    public void updateProjectUserPreference(Context context,String[] args)throws Exception
    {
        Map programMap = (Map) JPO.unpackArgs(args);
        Map paramMap    = (Map) programMap.get("paramMap");
		HashMap requestMap = (HashMap) programMap.get("requestMap");
        String projectId = (String)paramMap.get("objectId");
		ProjectSpace project = new ProjectSpace(projectId);

	//updateLagCalendarPreference
	StringList busSelects = new StringList(3);
        busSelects.add(SELECT_ATTRIBUTE_LAG_CALENDAR);
        busSelects.add(ProgramCentralConstants.SELECT_PROJECT_SCHEDULE);
        Map projectInfo = project.getInfo(context,busSelects);
        
        String strLagCalendar = (String)projectInfo.get(SELECT_ATTRIBUTE_LAG_CALENDAR);
        String strSchedule = (String)projectInfo.get(ProgramCentralConstants.SELECT_PROJECT_SCHEDULE);
        String existingProjectLagCalendarVal = (String)requestMap.get("Lag CalendarfieldValue");
        String existingProjectScheduleVal = (String)requestMap.get("ScheduleModefieldValue");
        
        if(((ProgramCentralUtil.isNotNullString(existingProjectLagCalendarVal) && !(existingProjectLagCalendarVal.equalsIgnoreCase(strLagCalendar))) || 
                (ProgramCentralUtil.isNotNullString(existingProjectScheduleVal) && !(existingProjectScheduleVal.equalsIgnoreCase(strSchedule)))) &&  
                		(ProgramCentralConstants.PROJECT_SCHEDULE_AUTO.equalsIgnoreCase(strSchedule))){
                    TaskDateRollup.rolloutProject(
                            context,
                            new StringList(projectId),
                            true);
                }
	//updateProjectUserPreference
        String defaultCategory =  (String)paramMap.get("DefaultCategory");
        String defaultTab       =  (String)paramMap.get("DefaultTab");
        
        if(ProgramCentralUtil.isNotNullString(defaultCategory)
                && ProgramCentralUtil.isNotNullString(defaultTab)) {
            project.setUserPreference(context, defaultCategory, defaultTab);
        }
    
    }
    @com.matrixone.apps.framework.ui.ProgramCallable
    public String getDefaultCategoryName(Context context, String args[]) throws Exception {
        Map programMap      = (Map) JPO.unpackArgs(args);
        Map paramMap        = (Map) programMap.get("paramMap");
        String objectId     = (String) paramMap.get("objectId");
        String categoryName = EMPTY_STRING;

        ProjectSpace project = new ProjectSpace(objectId);
        Dataobject preference = project.getUserPreference(context);
        if(preference != null) {
            categoryName = (String)preference.getDataelements().get("defCat");
            if("Default".equalsIgnoreCase(categoryName))categoryName="";
        }

        return categoryName;
    }
	@com.matrixone.apps.framework.ui.ProgramCallable
	public MapList getRelatedProject(Context context, String[] args) throws Exception {

		Map programMap = (Map) JPO.unpackArgs(args);
		MapList mapBusIds = new MapList();
		String strExpandLevel 	= (String) programMap.get("expandLevel");
		StringList busSelect = new StringList();
		busSelect.add(DomainConstants.SELECT_ID);
		busSelect.add("from["+ProgramCentralConstants.RELATIONSHIP_RELATED_PROJECTS+"].to.name");

		int nExpandLevel =  ProgramCentralUtil.getExpandLevel(strExpandLevel);
		DomainObject dObj = new DomainObject(programMap.get("objectId").toString());

		mapBusIds = dObj.getRelatedObjects(context, ProgramCentralConstants.RELATIONSHIP_RELATED_PROJECTS, DomainConstants.TYPE_PROJECT_SPACE+","+DomainConstants.TYPE_PROJECT_CONCEPT, false, true, nExpandLevel, busSelect, null, null, null, 0, null, null, null); 

		for(int i=0,size=mapBusIds.size();i<size;i++)
		{
			if(!(((Map) mapBusIds.get(i)).containsKey("from["+ProgramCentralConstants.RELATIONSHIP_RELATED_PROJECTS+"].to.name")))
				((Map) mapBusIds.get(i)).put("hasChildren", "false");
		}
		return mapBusIds;
	}
		@com.matrixone.apps.framework.ui.ProgramCallable
		public boolean hasAccessToPMCCalculateForecastCommand(Context context,String args[]) throws Exception
		{
			boolean hasAccess = false;
			try {
				HashMap inputMap = (HashMap)JPO.unpackArgs(args);
				
				String selectedTable = (String)inputMap.get("selectedTable");
				String selectedProgram = (String)inputMap.get("selectedProgram");
				if(ProgramCentralUtil.isNullString(selectedProgram)){
					selectedProgram = EMPTY_STRING;
				}
				StringList includeTableList = new StringList();
				includeTableList.add("PMCWBSForecastViewTable");
				includeTableList.add("PMCWBSForecastViewTableENXSBGrid");

				if(ProgramCentralUtil.isNotNullString(selectedTable) && ((selectedTable.contains("PMCWBSForecastViewTable")) && !selectedProgram.contains("emxTask:getWBSDeletedSubtasks"))){
					hasAccess = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return hasAccess;
		}

    /**
     * Returns if the user has access to convert the root of the folders to a Workspace Root.
     * @param context the ENOVIA <code>Context</code> object.
     * @param args request arguments
     * @return true if Edit Properties Command is available
     * @throws MatrixException if operation fails.
     */
    public boolean hasAccessToConvertProjectBookmarkRoot(Context context, String[] args)
    throws MatrixException{
        try{
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            String objectId = (String) programMap.get("objectId");
            ProjectSpace project = (ProjectSpace) DomainObject.newInstance(context,DomainConstants.TYPE_PROJECT_SPACE, "PROGRAM");
            project.setId(objectId);
            String SELECT_CURRENT_ACCESS_MODIFY = SELECT_CURRENT + ".access[modify]";
            String ATTRIBUTE_BOOKMARK_PHYSICAL_ID = PropertyUtil.getSchemaProperty("attribute_BookmarkPhysicalId");
	        String SELECT_BOOKMARK_PHYSICAL_ID = DomainObject.getAttributeSelect(ATTRIBUTE_BOOKMARK_PHYSICAL_ID);
            StringList slBusSelect = new StringList();
            slBusSelect.add(SELECT_CURRENT);
            slBusSelect.add(SELECT_CURRENT_ACCESS_MODIFY);
            slBusSelect.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
            Map projectInfo = project.getInfo(context, slBusSelect);
            String current = (String) projectInfo.get(SELECT_CURRENT);
            String currentAccessModify = (String) projectInfo.get(SELECT_CURRENT_ACCESS_MODIFY);
            String isKindOfProjectSpace = (String) projectInfo.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);

            if("true".equalsIgnoreCase(currentAccessModify) && !STATE_PROJECT_COMPLETE.equals(current) && ("true".equalsIgnoreCase(isKindOfProjectSpace)))
                return true;
            return false;
        }catch(Exception e){
            throw new MatrixException(e);
        }
    }

/**
	     * Option for import project.
	     * @param context - The eMatrix <code>Context</code> object.
	     * @param args holds information about object.
	     * @return option value.
	     * @throws Exception if operation fails.
	     */
	    public String getImportOption(Context context,String[]args)throws Exception{

	    	Map programMap          = JPO.unpackArgs(args);
	    	Map paramMap        = (Map) programMap.get("paramMap");
	    	Map requestMap        = (Map) programMap.get("requestMap");
	    	String ImportProject =(String) requestMap.get("createProject");
	    	String selectedTaskId   = (String) paramMap.get("objectId");
	    	Task task               = new Task();
	    	boolean isVisibleForTemplate = true;
        boolean isCopyFromFileForm  =false;
	    	if(ProgramCentralUtil.isNotNullString(selectedTaskId)) {
	    		isVisibleForTemplate = task.isVisibleForTemplate(context, selectedTaskId);
          isCopyFromFileForm=showCopyFromFileField(context,JPO.packArgs(requestMap));
	    	}

	        String useStartAndEnddate = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
	                "emxProgramCentral.Import.StartandEndDateImportOption", context.getSession().getLanguage());
	        String backgoundImportOption = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, 
	                "emxProgramCentral.Import.BackgoundImportOption", context.getSession().getLanguage());
	        StringBuilder sb = new StringBuilder();
	        sb.append("<table>");
	        
	        if(isVisibleForTemplate) {
	        sb.append("<tr>");
	        sb.append("<td colspan=\"2\" width=\"300\">");
	        sb.append("<input type=\"checkbox\" name=\"UseStartAndEndDatesAsEntered\" value=\"True\" /> ");
	        //sb.append("<label for=\"Use Start and End Dates as Entered\"> ");
	        sb.append(useStartAndEnddate);
	        sb.append("</td>");
	        sb.append("</tr>");
	        }
	        if(isCopyFromFileForm || "Import".equalsIgnoreCase(ImportProject)){
	        sb.append("<tr>");
	        sb.append("<td colspan=\"2\" width=\"300\">");
	        sb.append("<input type=\"checkbox\" name=\"BackgoundImportOption\" value=\"True\" /> ");
	        sb.append(backgoundImportOption);
	        sb.append("</td>");
	        sb.append("</tr>");
         }
	        sb.append("</table>");

	        return sb.toString();
	    
		}
		 public boolean showImportOptionField(Context context, String[]args)throws Exception{
	    	boolean isVisible=false;
	    	Task task               = new Task();
	    	Map programMap          = JPO.unpackArgs(args);
	    	String selectedTaskId   = (String) programMap.get("objectId");

	    	boolean isCopyFromFileForm = showCopyFromFileField(context, args);
	    	//boolean isVisibleForTemplate = task.isVisibleForTemplate(context, selectedTaskId);
	    	//isVisible = isCopyFromFileForm&&isVisibleForTemplate;
	    	return isCopyFromFileForm;
	    }
     /**
     * This method is for calling ProjectService method for Creating Project Schedule Structure from Project Template
     * @param context
     * @param args
     */ 
         public void createProjectFromProjectTemplate(Context context, String[] args) throws Exception {
    	   try {
    		Map programMap = (Map) JPO.unpackArgs(args);
    		String projectId = (String) programMap.get("projectId");
    		String templateId = (String) programMap.get("projectTemplateId");
    		MapList taskMapList = (MapList) programMap.get("TaskMapList");
    		Map answerList = (Map) programMap.get("answerList");
    		String defaultTaskConstraintType =(String) programMap.get("defaultTaskConstraintType");
    		HashMap selectedOptionMap =(HashMap) programMap.get("selectedOption");
    		ProjectService.completePTCloningProcess(context,projectId,taskMapList,answerList,defaultTaskConstraintType, selectedOptionMap);
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
        }
        public boolean showCopyOptionField(Context context, String[]args)throws Exception{
		    	boolean isVisible=false;
		    	Task task               = new Task();
		    	Map programMap          = JPO.unpackArgs(args);
		    	String selectedTaskId   = (String) programMap.get("objectId");

		    	boolean isCopyFromFileForm = showCopyFromFileField(context, args);
		    	boolean isVisibleForTemplate = task.isVisibleForTemplate(context, selectedTaskId);
		    	isVisible = (!isCopyFromFileForm)&&isVisibleForTemplate;
		    	return isVisible;
}
    /**
     * Only show Project Visibility if CSE, GLOBAL CS.
     * @param context - The eMatrix <code>Context</code> object.
     * @param args holds information about object.
     * @return boolean value.
     * @throws Exception if opeartion fails.
     */
    public boolean isProjectVisibilityFieldEnable(Context contex,String[]args)throws Exception
    {
        String defaultProj = PersonUtil.getActiveProject(context);

        //if Project Space is getting created in GLOBAL collab space thenwe can show Project Visibility
        if ("GLOBAL".equalsIgnoreCase(defaultProj)){
            return (true);
        }
        return (false);
    }
	

	/**
     * This method is used to check whether the logged in user has required role or not.
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments
     * @return boolean containing access to the command for the logged in user.
     * @throws Exception if the operation fails
     */
    public boolean hasAccessToCreateCommand(Context context, String[] args)
    {
    	boolean access = false;

    	try {
    		String contextUserRole = context.getRole();
    		if (contextUserRole != null && !contextUserRole.isEmpty() && (contextUserRole.startsWith("ctx::VPLMProjectLeader.") || contextUserRole.startsWith("ctx::3DSRestrictedLeader.") || contextUserRole.startsWith("ctx::Project Lead."))) {
    			access = true;
    		}
    	} catch (Exception e) {
    		e.printStackTrace(); 
    	}

    	return access;
    }
    
    /**
     * This method returns the name of the object with HTML formating. This method is configured on Project summary and Project Template summary tables
     * @param context the eMatrix <code>Context</code> object
     * @param args input arguments
     * @return HTML formated name
     * @throws Exception if the operation fails
     */
	@com.matrixone.apps.framework.ui.ProgramCallable
    public Vector getNameColumn (Context context, String[] args) throws Exception
    {
		Vector columnValues = new Vector();
		try {
    	 Map programMap      = (Map)JPO.unpackArgs(args);
         MapList objectList  = (MapList) programMap.get("objectList");
         HashMap paramList   = (HashMap) programMap.get("paramList");
		 String invokeFrom 	= (String)programMap.get("invokeFrom"); //Added for OTD
         
         String exportFormat = (String)  paramList.get("exportFormat");
         
         int size = objectList.size();
         
         boolean isPrinterFriendly = false;
         String strPrinterFriendly = (String)paramList.get("reportFormat");
         if ( ProgramCentralUtil.isNotNullString(strPrinterFriendly) ) {
             isPrinterFriendly = true;
         }
         String[] strObjectIds = new String[size];
         for (int i = 0; i < size; i++) {
             Map mapObject = (Map) objectList.get(i);
             String projectId = (String) mapObject.get(DomainObject.SELECT_ID);
             strObjectIds[i] = projectId;
         }
         
         StringList slBusSelect = new StringList(2);
         slBusSelect.add(DomainConstants.SELECT_ID);
         slBusSelect.add(DomainConstants.SELECT_NAME);
         
         BusinessObjectWithSelectList objectWithSelectList = null;
         if("TestCase".equalsIgnoreCase(invokeFrom)) { ////Added for OTD
        	 objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, strObjectIds, slBusSelect,true);
         }else {
			objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, strObjectIds, slBusSelect);
         }
         
         for(int i=0; i<size; i++) {
        	 StringBuilder formattedName = new StringBuilder();
        	 
             BusinessObjectWithSelect objectWithSelect =  objectWithSelectList.get(i);
             String projectId =  objectWithSelect.getSelectData(ProgramCentralConstants.SELECT_ID);
             String projectName          = (String)objectWithSelect.getSelectData(SELECT_NAME);
        	 
        	 if(isPrinterFriendly || "CSV".equalsIgnoreCase(exportFormat) || "HTML".equalsIgnoreCase(exportFormat) || "Text".equalsIgnoreCase(exportFormat) ){
                     formattedName.append(projectName);
                 }else{
                     String url = "../common/emxTree.jsp?objectId="+XSSUtil.encodeForURL(context,projectId)+"&amp;mode=replace&amp;portalMode=true";

                	 formattedName.append("<a href='").append(url).append("'>");
                	 formattedName.append(XSSUtil.encodeForXML(context,projectName));
                	 formattedName.append("</a>");
                 }
        	 columnValues.add(formattedName.toString());
             }
		}catch(Exception e){
				e.printStackTrace();
		}
         return columnValues;
    }
	
	/**
     * This method returns the name of the object with HTML formating. This method is configured on Project Detail Page
     * @param context the eMatrix <code>Context</code> object
     * @param args input arguments
     * @return HTML formated name
     * @throws Exception if the operation fails
     */
	public StringList getRelatedProjectNameColumnData(Context context, String[] args) throws Exception
    {
        long start          = System.currentTimeMillis();
        Map programMap      = (Map)JPO.unpackArgs(args);
        MapList objectList  = (MapList) programMap.get("objectList");
        HashMap paramList   = (HashMap) programMap.get("paramList");
        String exportFormat = (String)  paramList.get("exportFormat");
        String invokeFrom   = (String)  programMap.get("invokeFrom"); //Added for ODT

        boolean isPrinterFriendly = false;
        String strPrinterFriendly = (String)paramList.get("reportFormat");
        if ( strPrinterFriendly != null ) {
            isPrinterFriendly = true;
        }

        int size = objectList.size();
        String[] strObjectIds = new String[size];
        for (int i = 0; i < size; i++) {
            Map mapObject = (Map) objectList.get(i);
            String projectId = (String) mapObject.get(DomainObject.SELECT_ID);
            strObjectIds[i] = projectId;
        }

        StringList slBusSelect = new StringList(13);
        slBusSelect.add(DomainConstants.SELECT_ID);
        slBusSelect.add(DomainConstants.SELECT_NAME);
        Map mapProjectInfo = new HashMap();
        BusinessObjectWithSelectList objectWithSelectList = null;
        if("TestCase".equalsIgnoreCase(invokeFrom)) { //Added for ODT
            objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, strObjectIds, slBusSelect,true);
        }else {
            objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, strObjectIds, slBusSelect);
        }

        StringList columnValues = new StringList(objectList.size());

        Iterator objectIdItr = objectList.iterator();
        for(int i = 0, objectWithSelectListSize =objectWithSelectList.size(); i<objectWithSelectListSize; i++ ) {
	        BusinessObjectWithSelect objectWithSelect =  objectWithSelectList.get(i);
            String projectIds =  objectWithSelect.getSelectData(ProgramCentralConstants.SELECT_ID);
            String strName          = (String)objectWithSelect.getSelectData(SELECT_NAME);
            
            StringBuilder sBuff = new StringBuilder();

            if(isPrinterFriendly || "CSV".equalsIgnoreCase(exportFormat) || "HTML".equalsIgnoreCase(exportFormat) || "Text".equalsIgnoreCase(exportFormat)){
                sBuff.append(strName);        
            }else{
                projectIds = XSSUtil.encodeForURL(context,projectIds);
                strName = XSSUtil.encodeForXML(context, strName);
                
                String url = "../common/emxNavigator.jsp?isPopup=false&amp;objectId="+projectIds;
                
                sBuff.append("<a  href=\""+url);
                sBuff.append("\" target=\"_blank\" title=\""+strName);
                sBuff.append("\">");
                sBuff.append(strName);
                sBuff.append("</a>");
            }

            columnValues.add(sBuff.toString());
        }
        DebugUtil.debug("Total time in getRelatedProjectNameColumnData(programHTMLOutput):"+(System.currentTimeMillis()-start));
        return columnValues;
    }
	
	public void promoteSuccessor3DNotification(Context context, String[] args) throws Exception {			
		 try {	    			
			 	NotificationBase.processNotification(context, args);       			   			   	  			
	    	} catch (Exception e) {
	    		throw new Exception(e);
	    	}
	 }
	 
	 public void send3DNotificationForPotion(Context context, String[] args) throws Exception {
		 try {	  
			 
			 	NotificationBase.processNotification(context, args); 
	    	} catch (Exception e) {
	    		throw new Exception(e);
	    	}
	 }
	 
	 public StringList getCommentInputBox(Context context, String[] args) throws Exception {
				
		StringList qrList = new StringList();
        Map programMap = JPO.unpackArgs(args);
        MapList objectList = (MapList)programMap.get("objectList");
		String responseGivenByUser = (String) CacheUtil.getCacheObject(context, "dpm_questionResponseCachedData");
		JSONObject userResponseJSONData = new JSONObject();
		if(ProgramCentralUtil.isNotNullString(responseGivenByUser)){
			userResponseJSONData = new JSONObject(responseGivenByUser);
		}

        for(int i=0;i<objectList.size();i++){
            Map questionMap = (Map)objectList.get(i);
            String questionId = (String)questionMap.get(DomainObject.SELECT_ID);
			String givenComment = "";
			if(userResponseJSONData.contains(questionId)){
				givenComment = (String) ((JSONObject)userResponseJSONData.get(questionId)).get("comment").toString();
			}
            StringBuilder sb = new StringBuilder();
            sb.append("<input name=\"CommentInput\" id=\""+questionId+"-comment-input\" type=\"text\" class=\"temp-comment-input-class\" />");
            
            qrList.addElement(sb.toString());
        }

        return qrList;
	}
	 
	 /**
	  * Get list of answer given while responding question
	  * @param context - The eMatrix <code>Context</code> object.
	  * @param args holds information about object.
	  * @return response answer object list.
	  * @throws Exception if operation fails.
	  */
	 public StringList getQuestionAnswerFromResponse(Context context, String[] args) throws Exception {	    			
		Map programMap = JPO.unpackArgs(args);
        MapList objectList = (MapList)programMap.get("objectList");
        Map paramList = (Map)programMap.get("paramList");
		String selectedProjectId = (String) paramList.get("parentOID");
		final String temp1 = "relationship[Initiated Template Project].id";
		String relIdTemp = "";
		if(ProgramCentralUtil.isNotNullString(selectedProjectId) && selectedProjectId != EMPTY_STRING){
			relIdTemp = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump $3",selectedProjectId,temp1,"|");
		}
		StringList qrList = new StringList();
		String responseDataJSON = "";
		if(ProgramCentralUtil.isNotNullString(relIdTemp) && relIdTemp != EMPTY_STRING){
			responseDataJSON = (String) DomainRelationship.getAttributeString(context, relIdTemp, "ProjectQuestionResponse");
		}
		if(ProgramCentralUtil.isNotNullString(responseDataJSON) && responseDataJSON != EMPTY_STRING)
		{
		JSONObject jsonData = new JSONObject(responseDataJSON);
		
		if(jsonData.contains("QuestionInfo")){
			JSONArray tempjsonArr = jsonData.getJSONArray("QuestionInfo");
			for(int i=0;i<objectList.size();i++){
				Map questionMap = (Map)objectList.get(i);
				String questionId = (String)questionMap.get(DomainObject.SELECT_ID);
				StringBuilder sb = new StringBuilder();
					for(int jsonArrItr = 0; jsonArrItr<tempjsonArr.length(); jsonArrItr++){
					JSONObject tempJSONObj = tempjsonArr.getJSONObject(jsonArrItr);
					if(questionId.equalsIgnoreCase((String)tempJSONObj.getString("questionId")))
					{
						qrList.addElement((String)tempJSONObj.getString("response"));
						break;
					}
				}
			}
		}
		}
        return qrList;
	 }
	 
	 /**
	  * Get list of comments given while responding a question
	  * @param context - The eMatrix <code>Context</code> object.
	  * @param args holds information about object.
	  * @return response comment object list.
	  * @throws Exception if operation fails.
	  */
	 public StringList getQuestionCommentFromResponse(Context context, String[] args) throws Exception {
				
		Map programMap = JPO.unpackArgs(args);
        MapList objectList = (MapList)programMap.get("objectList");
        Map paramList = (Map)programMap.get("paramList");
		String selectedProjectId = (String) paramList.get("parentOID");
		final String temp1 = "relationship[Initiated Template Project].id";
		String relIdTemp = "";
		if(ProgramCentralUtil.isNotNullString(selectedProjectId) && selectedProjectId != EMPTY_STRING){
			relIdTemp = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump $3",selectedProjectId,temp1,"|");
		}
		StringList qrList = new StringList();
		String responseDataJSON = "";
		if(ProgramCentralUtil.isNotNullString(relIdTemp) && relIdTemp != EMPTY_STRING){
			responseDataJSON = (String) DomainRelationship.getAttributeString(context, relIdTemp, "ProjectQuestionResponse");
		}
		
		if(ProgramCentralUtil.isNotNullString(responseDataJSON) && responseDataJSON != EMPTY_STRING)
		{
		JSONObject jsonData = new JSONObject(responseDataJSON);
		if(jsonData.contains("QuestionInfo")){
			JSONArray tempjsonArr = jsonData.getJSONArray("QuestionInfo");
			
				for(int i=0;i<objectList.size();i++){
					Map questionMap = (Map)objectList.get(i);
					String questionId = (String)questionMap.get(DomainObject.SELECT_ID);
					StringBuilder sb = new StringBuilder();
					for(int jsonArrItr = 0; jsonArrItr<tempjsonArr.length(); jsonArrItr++){
				JSONObject tempJSONObj = tempjsonArr.getJSONObject(jsonArrItr);
					if(questionId.equalsIgnoreCase((String)tempJSONObj.getString("questionId")))
					{
						qrList.addElement((String)tempJSONObj.getString("comment"));
						break;
					}
				}
			}
		}
		}
        return qrList;
	 }
	 
	 /**
	  * Get list of connected tasks due to question response
	  * @param context - The eMatrix <code>Context</code> object.
	  * @param args holds information about object.
	  * @return connected task object list.
	  * @throws Exception if operation fails.
	  */
	 public StringList getQuestionTasksFromResponse(Context context, String[] args) throws Exception {
		Map programMap = JPO.unpackArgs(args);
        MapList objectList = (MapList)programMap.get("objectList");
        Map paramList = (Map)programMap.get("paramList");
		String exportFormat = (String)paramList.get("exportFormat");
		String selectedProjectId = (String) paramList.get("parentOID");
		final String temp1 = "relationship[Initiated Template Project].id";
		String relIdTemp = "";
		Map<String, Map<String,String>> templateTaskInfoMap = new HashMap<String, Map<String,String>>();
		if(ProgramCentralUtil.isNotNullString(selectedProjectId) && selectedProjectId != EMPTY_STRING){
			DomainObject selectedProjectObj = new DomainObject(selectedProjectId);
			StringList questionNameList = new StringList();
         	StringList busSelectList = new StringList(2);
        	busSelectList.add(SELECT_ID);
        	busSelectList.add(SELECT_NAME);
        	busSelectList.add(SELECT_PHYSICALID);
        	busSelectList.add(ProgramCentralConstants.SELECT_ATTRIBUTE_SOURCE_ID);
        	StringList relSelectList = new StringList();
			relSelectList.add(DomainConstants.SELECT_RELATIONSHIP_NAME);
			relSelectList.add(DomainConstants.SELECT_RELATIONSHIP_ID);
        	String busWhere = EMPTY_STRING;
    		String relWhere = EMPTY_STRING; 

            MapList relatedObjectsMapList = selectedProjectObj.getRelatedObjects(context,"*","*",
					busSelectList,relSelectList,false,true,(short)1,
					busWhere,relWhere,0);
			
			 int relatedObjectsMapListSize = relatedObjectsMapList.size();
            for(int i=0; i<relatedObjectsMapListSize; i++){
            	Map relatedObjectsMap = (Map)relatedObjectsMapList.get(i);
				final String RELATIONSHIP_INITIATED_TEMPLATE_PROJECT = "Initiated Template Project";
            	String taskName = (String)relatedObjectsMap.get(SELECT_NAME);
            	String taskId = (String)relatedObjectsMap.get(SELECT_ID);
            	String relationshipId = (String)relatedObjectsMap.get(DomainConstants.SELECT_RELATIONSHIP_ID);
            	String relationshipName = (String)relatedObjectsMap.get(DomainConstants.SELECT_RELATIONSHIP_NAME);
            	String sourceId = (String)relatedObjectsMap.get(ProgramCentralConstants.SELECT_ATTRIBUTE_SOURCE_ID);
            	if(RELATIONSHIP_SUBTASK.equalsIgnoreCase(relationshipName)){
					Map<String, String> taskInfoMap = new HashMap<String, String>();
					taskInfoMap.put("name",taskName);
					taskInfoMap.put("id",taskId);
					templateTaskInfoMap.put(sourceId,taskInfoMap);
				}
				else if(RELATIONSHIP_INITIATED_TEMPLATE_PROJECT.equalsIgnoreCase(relationshipName)){
					relIdTemp = relationshipId;
				}
            }
		}
		
		StringList qrList = new StringList();
		String responseDataJSON = "";
		if(ProgramCentralUtil.isNotNullString(relIdTemp) && relIdTemp != EMPTY_STRING){
			responseDataJSON = (String) DomainRelationship.getAttributeString(context, relIdTemp, "ProjectQuestionResponse");
		}
		
				for(int i=0;i<objectList.size();i++){
					boolean qrListItemAddedFlag = false;
					Map questionMap = (Map)objectList.get(i);
					String qrListString = "";
					String questionId = (String)questionMap.get(DomainObject.SELECT_ID);
					if(questionMap.get("from[Question].attribute[Task Transfer]") instanceof StringList && questionMap.get("from[Question].to.physicalid") instanceof StringList && questionMap.get("from[Question].to.id") instanceof StringList)
					{
						if(ProgramCentralUtil.isNotNullString(responseDataJSON) && responseDataJSON != EMPTY_STRING)
		{
		JSONObject jsonData = new JSONObject(responseDataJSON);
		
		if(jsonData.contains("QuestionInfo")){
			JSONArray tempjsonArr = jsonData.getJSONArray("QuestionInfo");
			for(int jsonArrItr = 0; jsonArrItr<tempjsonArr.length(); jsonArrItr++){
						
				JSONObject tempJSONObj = tempjsonArr.getJSONObject(jsonArrItr);
				String responseForQuestion = (String)tempJSONObj.getString("response");
					StringList sltaskResponse = (StringList)questionMap.get("from[Question].attribute[Task Transfer]");
					StringList sltaskPhysicalIds = (StringList)questionMap.get("from[Question].to.physicalid");
					StringList sltaskIds = (StringList)questionMap.get("from[Question].to.id");
					StringBuilder sb = new StringBuilder();
					if(questionId.equalsIgnoreCase((String)tempJSONObj.getString("questionId")) && sltaskResponse.size()==sltaskPhysicalIds.size())
					{
						String connectedTaskNames = "";
						for(int slItr = 0; slItr<sltaskPhysicalIds.size(); slItr++){
							if(responseForQuestion.equalsIgnoreCase((String)sltaskResponse.get(slItr))){
								String templateTaskPhysicalId = sltaskPhysicalIds.get(slItr);
								if(templateTaskInfoMap.containsKey(templateTaskPhysicalId)){
									Map projectTaskInfo = (Map) templateTaskInfoMap.get(templateTaskPhysicalId);
									String projectTaskName = (String) projectTaskInfo.get("name");
									String projectTaskId = (String) projectTaskInfo.get("id");
									if(!("CSV".equalsIgnoreCase(exportFormat) || "TEXT".equalsIgnoreCase(exportFormat)))
									{
										connectedTaskNames += "<a target='_blank' href='emxNavigator.jsp?objectId="+projectTaskId+"'>"+projectTaskName+"</a>"+" | ";
									}
									else{
										connectedTaskNames += projectTaskName+" , ";
									}
								}
							}
							
						}
						if(connectedTaskNames.length()>3){
							connectedTaskNames = connectedTaskNames.substring(0,connectedTaskNames.length()-3);
						}
						qrListString = connectedTaskNames;
						break;
					}
				}
				
				}
				
			}
		}
		else if(questionMap.get("from[Question].attribute[Task Transfer]") instanceof String && questionMap.get("from[Question].to.physicalid") instanceof String && questionMap.get("from[Question].to.id") instanceof String){
			JSONObject jsonData = new JSONObject(responseDataJSON);
			if(jsonData.contains("QuestionInfo")){
			JSONArray tempjsonArr = jsonData.getJSONArray("QuestionInfo");
			for(int jsonArrItr = 0; jsonArrItr<tempjsonArr.length(); jsonArrItr++){
						
				JSONObject tempJSONObj = tempjsonArr.getJSONObject(jsonArrItr);
				String responseForQuestion = (String)tempJSONObj.getString("response");
					String taskResponse = (String)questionMap.get("from[Question].attribute[Task Transfer]");
					String taskPhysicalIds = (String)questionMap.get("from[Question].to.physicalid");
					String taskIds = (String)questionMap.get("from[Question].to.id");
					StringBuilder sb = new StringBuilder();
					if(questionId.equalsIgnoreCase((String)tempJSONObj.getString("questionId")))
					{
						String connectedTaskNames = "";
							if(responseForQuestion.equalsIgnoreCase((String)taskResponse)){
								if(templateTaskInfoMap.containsKey(taskPhysicalIds)){
									Map projectTaskInfo = (Map) templateTaskInfoMap.get(taskPhysicalIds);
									String projectTaskName = (String) projectTaskInfo.get("name");
									String projectTaskId = (String) projectTaskInfo.get("id");
									if(!("CSV".equalsIgnoreCase(exportFormat) || "TEXT".equalsIgnoreCase(exportFormat)))
									{
										connectedTaskNames += "<a target='_blank' href='emxNavigator.jsp?objectId="+projectTaskId+"'>"+projectTaskName+"</a>"+" | ";
									}
									else{
										connectedTaskNames += projectTaskName+" , ";
									}
								}
							}
							if(connectedTaskNames.length()>3){
								connectedTaskNames = connectedTaskNames.substring(0,connectedTaskNames.length()-3);
							}
						qrListString = connectedTaskNames;
						break;
					}
				}
			}
		}
		qrList.addElement(qrListString);
		}
        return qrList;
		
	 }
	 
	 /**
  	 * Get list of all the responded questions
  	 * @param context - The eMatrix <code>Context</code> object.
  	 * @param args holds information about object.
  	 * @return responded question object list.
  	 * @throws Exception if operation fails.
  	 */
  	@com.matrixone.apps.framework.ui.ProgramCallable  
  	public MapList getRespondedQuestionList(Context context,String[]args)throws Exception
  	{
  		com.matrixone.apps.program.Question question =
  				(com.matrixone.apps.program.Question) DomainObject.newInstance(context,
  						DomainConstants.TYPE_QUESTION, "PROGRAM");
  		com.matrixone.apps.program.ProjectTemplate projectTemplate =
  				(com.matrixone.apps.program.ProjectTemplate) DomainObject.newInstance(context,
  						DomainConstants.TYPE_PROJECT_TEMPLATE, "PROGRAM");

  		MapList questionMapList = new MapList();

  		String QuestionType = question.TYPE_QUESTION;
  		String sPolicyName = question.getDefaultPolicy(context);
  		String ActiveState = PropertyUtil.getSchemaProperty(context,"policy",sPolicyName,"state_Active");

  		Map programMap = JPO.unpackArgs(args);
  		String selectedProjectId = (String) programMap.get("objectId");
		
		DomainObject projectObj = new DomainObject(selectedProjectId);
		
		StringList busSelects1 = new StringList();
		busSelects1.addElement(DomainObject.SELECT_ID);
		
		StringList relSelects1 = new StringList();
		
		//MapList mlTemplateInfo = projectObj.getRelatedObjects(context, "Initiated Template Project", true, busSelects1, relSelects1);
		
		
		final String temp1 = "relationship[Initiated Template Project].to.id";
		final String temp2 = "relationship[Initiated Template Project].id";
				String mqlResponse = MqlUtil.mqlCommand(context, "print bus $1 select $2 $3 dump $4",selectedProjectId,temp1,temp2,"|");
		String templateIdTemp = "";
		String relationshipIdTemp = "";
		if(FrameworkUtil.split(mqlResponse,"|").size()>=2){
			templateIdTemp = (String) FrameworkUtil.split(mqlResponse,"|").get(0);
			relationshipIdTemp = (String) FrameworkUtil.split(mqlResponse,"|").get(1);
		}
		
		String responseDataOnRelationship = "";
		if(relationshipIdTemp != EMPTY_STRING){
			responseDataOnRelationship = (String) DomainRelationship.getAttributeString(context, relationshipIdTemp, "ProjectQuestionResponse");
		}
		if(ProgramCentralUtil.isNotNullString(templateIdTemp) && templateIdTemp != EMPTY_STRING){
		
			StringList busSelects = new StringList(1);
	  		busSelects.add(question.SELECT_ID);
	  		busSelects.add(question.SELECT_DESCRIPTION);
	  		busSelects.add(question.SELECT_HAS_ITEMS);
	  		busSelects.add(SELECT_QUE_TASK_TRANSFER);
			busSelects.add("from[Question].to.id");
			//busSelects.add(ProgramCentralConstants.SELECT_PHYSICALID);
			busSelects.add("from[Question].to.physicalid");
	  		
	  		String busWhere =  question.SELECT_HAS_ITEMS + "== True&&current==" + ActiveState;
	
		  		projectTemplate.setId(templateIdTemp);
				
				MapList questionListTemp = new MapList();
		
		  		MapList questionList = question.getQuestions(context, 
		  				projectTemplate, 
		  				busSelects, 
		  				DomainObject.EMPTY_STRINGLIST, 
		  				busWhere,
		  				DomainObject.EMPTY_STRING);
						
				MapList questionList1 = new MapList();

				for(int mapListItr = 0; mapListItr<questionList.size(); mapListItr++){
					Map m = (Map) questionList.get(mapListItr);
					String questionId = (String) m.get(question.SELECT_ID);
					DomainObject domObj = new DomainObject(questionId);
					MapList questionList2 = domObj.getRelatedObjects(
                               context,
                               RELATIONSHIP_QUESTION,
                               DomainConstants.TYPE_QUESTION,
                               busSelects,
                               DomainObject.EMPTY_STRINGLIST,
							   //relSelects,
                               false,
                               true,
                               (short) 0,
                               null,
                               null);
					questionList1.addAll(questionList2);
				}
				
				questionList.addAll(questionList1);

				for(int mapListItr = 0; mapListItr<questionList.size(); mapListItr++){
					Map m = (Map) questionList.get(mapListItr);
					m.put("disableSelection", "true");
					m.put("RowEditable", "readonly");
					String questionId = (String) m.get("id");
					if(responseDataOnRelationship != EMPTY_STRING && responseDataOnRelationship.contains(questionId))
					{
						questionMapList.add(m);
					}
				}
		//questionMapList.addAll(questionList);
		}
		return questionMapList;
  	}
}


