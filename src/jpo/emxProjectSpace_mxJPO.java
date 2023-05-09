//
// $Id: emxProjectSpace.java.rca 1.6 Wed Oct 22 16:21:26 2008 przemek Experimental przemek $ 
//
// emxProjectSpace.java
//
// Copyright (c) 2002-2020 Dassault Systemes.
// All Rights Reserved
// This program contains proprietary and trade secret information of
// MatrixOne, Inc.  Copyright notice is precautionary only and does
// not evidence any actual or intended publication of such program.
//

import com.ds.utilities.Exception.RestServiceException;
import com.ds.utilities.widgets.Tools;
import com.gsc.apps.program.gscProjectSpace;
import com.matrixone.apps.common.ContentReplicateOptions;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UINavigatorUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import com.matrixone.apps.program.ProjectSpace;
import com.matrixone.apps.program.Task;
import matrix.db.*;
import matrix.util.StringList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The <code>emxProjectSpace</code> class represents the Project Space JPO
 * functionality for the AEF type.
 *
 * @version AEF 10.0.SP4 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxProjectSpace_mxJPO extends emxProjectSpaceBase_mxJPO {

    /**
     * @param context the eMatrix <code>Context</code> object
     * @param args    holds no arguments
     * @throws Exception if the operation fails
     * @grade 0
     * @since AEF 10.0.SP4
     */
    public emxProjectSpace_mxJPO(Context context, String[] args) throws Exception {
        super(context, args);
    }

    /**
     * Constructs a new emxProjectSpace JPO object.
     *
     * @throws Exception if the operation fails
     * @since AEF 10.0.SP4
     */
    public emxProjectSpace_mxJPO(String id) throws Exception {
        // Call the super constructor
        super(id);
    }

    public String find(Context context, String[] args1) {
        String typeObj = "Project Space";
        String selects = "attribute[Title]";
        String whereExpr = "";
        String findProgram = "";
        String findFunction = "";
        String findParams = "";
        JsonObjectBuilder output = Json.createObjectBuilder();

        try {
            output.add("msg", "KO");

            boolean isSCMandatory = false;

            ContextUtil.startTransaction(context, false);
            StringList slObj = new StringList();
            slObj.addElement("id");
            slObj.addElement("physicalid");
            slObj.addElement("type");
            slObj.addElement("current");
            slObj.addElement("policy");

            ArrayList<String> jpoSelects = new ArrayList<String>();
            if (null != selects && !selects.isEmpty()) {
                String[] arrSelects = selects.split(",");
                for (int i = 0; i < arrSelects.length; i++) {
                    String select = arrSelects[i];
                    if (select.startsWith("JPO-")) {
                        jpoSelects.add(select);
                    } else {
                        slObj.add(select);
                    }
                }
            }

            if (null == whereExpr || whereExpr.isEmpty()) {
                whereExpr = "";
            }

            MapList mlObjs = null;

            if (null != findProgram && null != findFunction && !findProgram.isEmpty() && !findFunction.isEmpty()) {
                HashMap<String, String> mapArgs = new HashMap<String, String>();

                if (!findParams.isEmpty()) {
                    String[] pairsParams = findParams.split("&");
                    for (int i = 0; i < pairsParams.length; i++) {
                        String[] pair = pairsParams[i].split("=");
                        if (pair.length >= 2) {
                            mapArgs.put(pair[0], pair[1]);
                        }
                    }
                }

                String[] args = JPO.packArgs(mapArgs);
                mlObjs = (MapList) JPO.invoke(context, findProgram, args, findFunction, args, MapList.class);

                MapList mlRes = new MapList();

                for (Object obj : mlObjs) {
                    Map<String, Object> mapObj = (Map<String, Object>) obj;
                    String oidTest = (String) mapObj.get("id");
                    // Make sure that there is an object id, filter objects for some expand program
                    // where last or first map is not an object
                    if (null != oidTest && !oidTest.isEmpty()) {
                        DomainObject dom = new DomainObject(oidTest);

                        @SuppressWarnings("rawtypes") Map mapRes = dom.getInfo(context, slObj);

                        mlRes.add(mapRes);
                    }
                }

                mlObjs = mlRes;
            } else {
                mlObjs = DomainObject.findObjects(context, typeObj, "*", whereExpr, slObj);
            }

            Map<String, Vector<String>> mapJPORes = new HashMap<String, Vector<String>>();
            for (String selectJPO : jpoSelects) {
                String[] selElems = selectJPO.split("-");
                if (selElems.length >= 3) {
                    String prog = selElems[1];
                    String funct = selElems[2];

                    HashMap<String, Object> mapArgs = new HashMap<String, Object>();
                    mapArgs.put("objectList", mlObjs);

                    String[] args = JPO.packArgs(mapArgs);

                    Vector<String> vRes = (Vector<String>) JPO.invoke(context, prog, args, funct, args, Vector.class);
                    mapJPORes.put(selectJPO, vRes);
                }
            }

            JsonArrayBuilder outArr = Json.createArrayBuilder();

            for (int i = 0; i < mlObjs.size(); i++) {
                Map<String, Object> mapObj = (Map<String, Object>) mlObjs.get(i);

                // Add Specific JPO selects
                for (String selectJPO : jpoSelects) {
                    String val = mapJPORes.get(selectJPO).get(i);
                    mapObj.put(selectJPO, val);
                }

                // Add NLS
                for (Object keySelect : slObj) {
                    try {
                        String strKey = (String) keySelect;
                        String value = (String) mapObj.get(strKey);
                        if (null != value) {
                            String nlsType = "";
                            String nlsInfo = "";
                            if (strKey.equals("type")) {
                                nlsType = "Type";
                                nlsInfo = value.trim().replaceAll(" ", "_");
                            } else if (strKey.equals("current")) {
                                nlsType = "State";

                                String strPolicy = (String) mapObj.get("policy");
                                strPolicy = strPolicy.replaceAll(" ", "_");

                                nlsInfo = strPolicy + "." + value.replaceAll(" ", "_");
                            }

                            if (!nlsType.isEmpty()) {
                                String strValueNLS = EnoviaResourceBundle.getFrameworkStringResourceProperty(context, "emxFramework." + nlsType + "." + nlsInfo, context.getLocale());

                                if (null != strValueNLS && !strValueNLS.equals("") && !strValueNLS.startsWith("emxFramework.")) {
                                    mapObj.put("nls!" + strKey, strValueNLS);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        // Silent catch
                    }
                }

                JsonObjectBuilder jsonObj = Json.createObjectBuilder();
                Tools.map2JsonBuilder(jsonObj, mapObj);

                String type = (String) mapObj.get("type");
                if (null != type && !type.isEmpty()) {
                    String icon = UINavigatorUtil.getTypeIconProperty(context, type);
                    if (icon != null && !icon.isEmpty()) {
                        jsonObj.add("iconType", "/common/images/" + icon);
                    }
                }
                outArr.add(jsonObj);
            }

            output.add("msg", "OK");
            output.add("data", outArr);

            ContextUtil.commitTransaction(context);

        } catch (Exception e) {
            try {
                if (ContextUtil.isTransactionActive(context)) {
                    ContextUtil.abortTransaction(context);
                }
            } catch (FrameworkException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        String outStr = output.build().toString();
        System.out.println(outStr);
        return outStr;
    }

    public void cloneStructureFromTemplate(Context context, String[] args) throws Exception {
        // String projectId = "15741.36097.11246.18442";//args[0];
        // String templateId = "15741.36097.61824.17715";//args[1];
        try{
            Map<String,String> jpoArgumentMap  = (Map<String,String>) JPO.unpackArgs(args);
            String projectId			= jpoArgumentMap.get("ProjectId");
            String templateId			= jpoArgumentMap.get("TemplateId");

            gscProjectSpace newProjectSpace = new gscProjectSpace(projectId);
            gscProjectSpace templateProjectSpace = new gscProjectSpace(templateId);
            this.setId(templateId);

            Map questionResponseMap = null;
            String copyConstraint = "true";
            boolean copyRoadMap = true;

            String var104 = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_DURATION + "].inputunit";
            StringList templBusSelect = new StringList(28);
            StringList templRelSelect = new StringList(13);
            templBusSelect.add("id");
            templBusSelect.add("physicalid");
            templBusSelect.add("type");
            templBusSelect.add("name");
            templBusSelect.add("policy");
            templBusSelect.add("level");
            templBusSelect.add("description");
            templBusSelect.add(var104);
            templBusSelect.add("attribute[Deliverables Inheritance]");
            templBusSelect.add(SELECT_DELIVERABLE_IDS);
            templBusSelect.add(SELECT_DELIVERABLE_HAS_FILE);
            templBusSelect.add(DELIVRERABLE_SELECT_IS_VERSION_OBJECT);
            templBusSelect.add(SELECT_REFERENCE_DOC_IDS);
            templBusSelect.add(SELECT_REFERENCE_DOC_HAS_FILE);
            templBusSelect.add(REFERENCE_DOC_SELECT_IS_VERSION_OBJECT);
            templBusSelect.add(SELECT_SOURCE_ID);
            templBusSelect.add("from[" + DomainConstants.RELATIONSHIP_LINK_URL + "].to.id");
            templBusSelect.add("attribute[" + ATTRIBUTE_TASK_ESTIMATED_DURATION + "]");
            templBusSelect.add(ProgramCentralConstants.SELECT_PROJECT_ACCESS_KEY_PHYSICAL_ID_FOR_PREDECESSOR);
            templBusSelect.add(ProgramCentralConstants.SELECT_PREDECESSOR_PHYSICAL_IDS);
            templBusSelect.add(ProgramCentralConstants.SELECT_DEPENDENCY_TYPE);
            templBusSelect.add(ProgramCentralConstants.SELECT_PREDECESSOR_LAG_TIME_INPUT);
            templBusSelect.add(ProgramCentralConstants.SELECT_PREDECESSOR_LAG_TIME_UNITS);
            templBusSelect.add(ProgramCentralConstants.SELECT_PARENT_TASK_PHYSICALID);
            templBusSelect.add(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_TASK);
            templBusSelect.add("from[Referenced Simulations].to.id");
            templBusSelect.add("to[" + DomainConstants.RELATIONSHIP_CLASSIFIED_ITEM + "].from.id");
            templBusSelect.add(ProgramCentralConstants.SELECT_CALENDAR_ID);
            if (questionResponseMap != null) {
                templBusSelect.add(SELECT_TASK_TRANSFER);
                templBusSelect.add(SELECT_QUESTION_ID);
            }

            templRelSelect.add(SELECT_DELIVERABLE_HAS_FILE);
            templRelSelect.add(DELIVRERABLE_SELECT_IS_VERSION_OBJECT);
            templRelSelect.add("from[" + DomainConstants.RELATIONSHIP_LINK_URL + "].to.id");
            templRelSelect.add(ProgramCentralConstants.SELECT_PROJECT_ACCESS_KEY_PHYSICAL_ID_FOR_PREDECESSOR);
            templRelSelect.add(ProgramCentralConstants.SELECT_PREDECESSOR_PHYSICAL_IDS);
            templRelSelect.add(ProgramCentralConstants.SELECT_DEPENDENCY_TYPE);
            templRelSelect.add(ProgramCentralConstants.SELECT_PREDECESSOR_LAG_TIME_INPUT);
            templRelSelect.add(ProgramCentralConstants.SELECT_PREDECESSOR_LAG_TIME_UNITS);
            templRelSelect.add(SELECT_REFERENCE_DOC_IDS);
            templRelSelect.add(SELECT_REFERENCE_DOC_HAS_FILE);
            templRelSelect.add(REFERENCE_DOC_SELECT_IS_VERSION_OBJECT);
            templRelSelect.add(SELECT_DELIVERABLE_HAS_FILE);
            templRelSelect.add(SELECT_DELIVERABLE_IDS);
            templRelSelect.add("from[Referenced Simulations].to.id");
            if ("true".equalsIgnoreCase(copyConstraint)) {
                templBusSelect.add("attribute[" + ATTRIBUTE_TASK_CONSTRAINT_TYPE + "]");
                templBusSelect.add(DomainConstants.ATTRIBUTE_TASK_CONSTRAINT_DATE);
            }

            MapList newList = new MapList();
            new MapList();
            boolean var51 = false;

            MapList taskList;
            try {
                ContextUtil.pushContext(context, PropertyUtil.getSchemaProperty(context, "person_UserAgent"), "", "");
                var51 = true;
                if (copyRoadMap) {
                    taskList = getTemplateTasks(context, this, 0, templBusSelect, (StringList) null, false, false, true, templRelSelect);
                } else {
                    taskList = getTemplateTasks(context, this, 0, templBusSelect, (StringList) null, false, false, false, templRelSelect);
                }
            } finally {
                if (var51) {
                    ContextUtil.popContext(context);
                }

            }

            String tId;
            for (int j = 0; j < taskList.size(); ++j) {
                Map taskMap = (Map) taskList.get(j);
                String taskTransfer = (String) taskMap.get(SELECT_TASK_TRANSFER);
                String questionId = (String) taskMap.get(SELECT_QUESTION_ID);
                StringList taskTransferIds = FrameworkUtil.split(taskTransfer, "\u0007");
                StringList questionIds = FrameworkUtil.split(questionId, "\u0007");

                for (int k = 0; k < taskTransferIds.size() && k < questionIds.size(); ++k) {
                    String qId = (String) questionIds.get(k);
                    tId = (String) taskTransferIds.get(k);
                    if (ProgramCentralUtil.isNotNullString((String) questionResponseMap.get(qId)) && (String) questionResponseMap.get(qId) != "" && ((String) questionResponseMap.get(qId)).equalsIgnoreCase(tId)) {
                        taskMap.put(SELECT_TASK_TRANSFER, tId);
                        taskMap.put(SELECT_QUESTION_ID, qId);
                        break;
                    }
                }

                newList.add(taskMap);
            }

            if (this.isKindOf(context, DomainConstants.TYPE_PROJECT_TEMPLATE) && newProjectSpace.isKindOf(context, DomainConstants.TYPE_PROJECT_SPACE)) {

                HashMap selOptions = new HashMap();
                selOptions.put("copyTaskConstraint", copyConstraint);
                HashMap infoMap = new HashMap();
                infoMap.put("projectTemplateId", templateId);
                infoMap.put("projectId", projectId);
                infoMap.put("TaskMapList", newList);
                infoMap.put("answerList", questionResponseMap);
                infoMap.put("defaultTaskConstraintType", "As Soon As Possible");
                infoMap.put("selectedOption", selOptions);
                String[] packArgs = JPO.packArgs(infoMap);
                createProjectFromProjectTemplate(context, packArgs);

                //JPO.invoke(context, "emxProjectSpace", (String[]) null, "createProjectFromProjectTemplate", packArgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Create new blank project space object.
     *
     * @param context - The eMatrix <code>Context</code> object.
     * @param args    holds information about object.
     * @return newly created obejct id;
     * @throws Exception if operation fails.
     */
    @com.matrixone.apps.framework.ui.CreateProcessCallable
    public Map createNewProject(Context context, String[] args) throws Exception {
        ProjectSpace project = (ProjectSpace) DomainObject.newInstance(context, ProgramCentralConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);

        ProjectSpace newProject = (ProjectSpace) DomainObject.newInstance(context, ProgramCentralConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);

        Map<String, String> returnMap = new HashMap();
        try {
            ContextUtil.startTransaction(context, true);

            String SCHEDULE_FROM = PropertyUtil.getSchemaProperty(context, "attribute_ScheduleFrom");
            Map<String, String> attributeMap = new HashMap();
            Map<String, String> basicProjectInfo = new HashMap();
            Map<String, String> relatedProjectInfo = new HashMap();

            Map programMap = (HashMap) JPO.unpackArgs(args);
            String objectId = (String) programMap.get("objectId");
            String createProject = (String) programMap.get("createProject");
            String projectName = (String) programMap.get("Name");
            String projectAutoName = (String) programMap.get("autoNameCheck");
            String projectDescrption = (String) programMap.get("Description");
            String businessUnitId = (String) programMap.get("BusinessUnitOID");
            String programId = (String) programMap.get("ProgramOID");
            String businessGoalId = (String) programMap.get("BusinessGoalOID");
            String baseCurrency = (String) programMap.get("BaseCurrency");
            String projectVault = project.DEFAULT_VAULTS;
            String projectVisibility = (String) programMap.get("ProjectVisibility");
            String projectPolicy = (String) programMap.get("Policy");
            String projectScheduleFrom = (String) programMap.get("ScheduleFrom");
            String projectDate = (String) programMap.get("ProjectDate");
            String defaultConstraintType = (String) programMap.get("DefaultConstraintType");
            String projectSpaceType = (String) programMap.get("TypeActual");
            String selectedTemplateId = (String) programMap.get("SeachProjectOID");
            String connectRelatedProjects = (String) programMap.get("connectRelatedProject");
            String copyFinancialData = (String) programMap.get("financialData");
            String copyFolderData = (String) programMap.get("folders");
            String sKeepSourceConstraints = (String) programMap.get("keepSourceConstraints");
            String sKeepSourceColors = (String) programMap.get("keepSourceColors");

            String refernceDocument = (String) programMap.get("ReferenceDocument");
            String deliverabletId = (String) programMap.get("DeliverableOID");
            String calendarId = (String) programMap.get("CalendarOID");
			
			// HJ
            String gscProjectType = (String) programMap.get("gscProjectType");
            String gscBusinessArea = (String) programMap.get("gscBusinessArea");

            StringList calendarIds = FrameworkUtil.split(calendarId, "|");

            Locale locale = (Locale) programMap.get("localeObj");
            if (locale == null) {
                locale = context.getLocale();
            }

            String strTimeZone = (String) programMap.get("timeZone");
            double dClientTimeZoneOffset = (new Double(strTimeZone)).doubleValue();
            //  IR-528127-3DEXPERIENCER2018x
            boolean isECHInstalled = FrameworkUtil.isSuiteRegistered(context, "appVersionEnterpriseChange", false, null, null);
            if (isECHInstalled) {
                if (mxType.isOfParentType(context, projectSpaceType, DomainObject.TYPE_CHANGE_PROJECT))
                    programId = (String) programMap.get("ECHMandProgramOID");
            }
            //end  IR-528127-3DEXPERIENCER2018x

            if (ProgramCentralUtil.isNotNullString(projectDate)) {
  				/*projectDate = projectDate.trim();
  				projectDate = eMatrixDateFormat.getFormattedInputDate(context,projectDate,dClientTimeZoneOffset,locale);*/
                TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
                double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
                dClientTimeZoneOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();
                projectDate = projectDate.trim();
                int iDateFormat = eMatrixDateFormat.getEMatrixDisplayDateFormat();
                String strInputTime = eMatrixDateFormat.adjustTimeStringForInputFormat("");
                projectDate = eMatrixDateFormat.getFormattedInputDateTime(projectDate, strInputTime, iDateFormat, dClientTimeZoneOffset, locale);
            }

            //For program,Businessgoal and related project
            if (ProgramCentralUtil.isNotNullString(objectId)) {
                StringList selectable = new StringList();
                selectable.addElement(ProgramCentralConstants.SELECT_IS_PROJECT_SPACE);
                selectable.addElement(ProgramCentralConstants.SELECT_IS_PROGRAM);
                selectable.addElement(ProgramCentralConstants.SELECT_IS_BUSINESS_GOAL);

                DomainObject parentObject = DomainObject.newInstance(context, objectId);
                Map<String, String> parentObjectInfo = parentObject.getInfo(context, selectable);
                String isProjectSpace = parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_PROJECT_SPACE);
                String isProgram = parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_PROGRAM);
                String isBusinessGoal = parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_BUSINESS_GOAL);

                if ("true".equalsIgnoreCase(isProgram)) {
                    programId = objectId;
                } else if ("true".equalsIgnoreCase(isBusinessGoal)) {
                    businessGoalId = objectId;
                } else if ("true".equalsIgnoreCase(isProjectSpace)) {
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

            if ("Clone".equalsIgnoreCase(createProject) || "Template".equalsIgnoreCase(createProject)) {

                SimpleDateFormat dateFormat = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
                Calendar constraintDate = Calendar.getInstance();
                Date newDate = null;
                newDate = dateFormat.parse(projectDate);
                constraintDate.setTime(newDate);
                if (defaultConstraintType.equalsIgnoreCase(ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ASAP)) {
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
            // if (ProgramCentralUtil.isNullString(projectName) && projectAutoName.equalsIgnoreCase("true")) {
            //  String symbolicTypeName = PropertyUtil.getAliasForAdmin(context, "Type", projectSpaceType, true);
            //  String symbolicPolicyName = PropertyUtil.getAliasForAdmin(context, "Policy", projectPolicy, true);
            //  projectName = FrameworkUtil.autoName(context, symbolicTypeName, null, symbolicPolicyName, null, null, true, true);
            // }

            // 자동 입력 선택 시
            // if (ProgramCentralUtil.isNullString(projectName) && projectAutoName.equalsIgnoreCase("true")) {
                // // 230316 JHJ - Project Name 자동 입력
                // projectName = getNameCode(context);
            // }

            // 230316 JHJ - Project Name 자동 입력
            projectName = getNameCode(context, gscProjectType, gscBusinessArea);

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

            boolean isCopyFolderData = true;
            boolean isCopyFinancialData = true;
            boolean keepSourceConstraints = true;
            boolean keepSourceColors = true;

            if (ProgramCentralUtil.isNullString(copyFolderData) || "false".equalsIgnoreCase(copyFolderData)) {
                isCopyFolderData = false;
            }

            if (ProgramCentralUtil.isNullString(copyFinancialData) || "false".equalsIgnoreCase(copyFinancialData)) {
                isCopyFinancialData = false;
            }

            if (ProgramCentralUtil.isNullString(sKeepSourceConstraints) || "false".equalsIgnoreCase(sKeepSourceConstraints)) {
                keepSourceConstraints = false;
            }
            //Can Comment iF as both statement in If and Else are same
            if (ProgramCentralUtil.isNullString(sKeepSourceColors) || "false".equalsIgnoreCase(sKeepSourceColors)) {
                PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
            } else {
                PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
            }


            ContentReplicateOptions selectedOptionForReferenceDocument = ContentReplicateOptions.COPY;
            if (ProgramCentralUtil.isNullString(refernceDocument) || "Reference".equalsIgnoreCase(refernceDocument)) {
                selectedOptionForReferenceDocument = ContentReplicateOptions.CONNECT_EXISTING;
            }

            //Create new project object.
            if ("Blank".equalsIgnoreCase(createProject) || "Import".equalsIgnoreCase(createProject)) {

                newProject = project.createBlankProject(context, basicProjectInfo, attributeMap, relatedProjectInfo);

            } else if ("Clone".equalsIgnoreCase(createProject)) {
                boolean isConnectRelatedProject = false;
                if (ProgramCentralUtil.isNotNullString(connectRelatedProjects) && connectRelatedProjects.equalsIgnoreCase("True")) {
                    isConnectRelatedProject = true;
                }

                //create new project from existing object.
                newProject = project.clone(context, selectedTemplateId, basicProjectInfo, relatedProjectInfo, attributeMap, isConnectRelatedProject, isCopyFolderData, isCopyFinancialData, keepSourceConstraints);

            } else if ("Template".equalsIgnoreCase(createProject)) {
                boolean isTemplateTaskAutoName = false;
                String questionResponseValue = (String) CacheUtil.getCacheObject(context, "QuestionsResponse");
                String resourceTemplateId = (String) programMap.get("ResourceTemplate");

                Map<String, String> questionResponseMap = new HashMap<String, String>();
                if (ProgramCentralUtil.isNotNullString(questionResponseValue)) {
                    StringList questionResponseValueList = FrameworkUtil.split(questionResponseValue, "|");
                    for (int i = 0; i < questionResponseValueList.size(); i++) {
                        String questionRValue = (String) questionResponseValueList.get(i);
                        StringList questionActualRList = FrameworkUtil.split(questionRValue, "=");
                        questionResponseMap.put((String) questionActualRList.get(0), (String) questionActualRList.get(1));
                    }
                }


                //update related info
                relatedProjectInfo.put("resourceTemplateId", resourceTemplateId);

                newProject = project.cloneTemplateToCreateProject(context, selectedTemplateId, basicProjectInfo, relatedProjectInfo, attributeMap, questionResponseMap, isTemplateTaskAutoName, isCopyFolderData, isCopyFinancialData, keepSourceConstraints, selectedOptionForReferenceDocument, calendarIds);
            }
            //Get new project ID
            String newProjectId = newProject.getObjectId();

            returnMap.put("id", newProjectId);
            if (!"Template".equalsIgnoreCase(createProject)) {
                // If only one calendar is selected then that will be connected as Default Calendar
                if (calendarIds.size() == 1) {
                    String defaultCalendarId = calendarIds.get(0) + "|DefaultCalendar";
                    calendarIds.remove(0);
                    calendarIds.add(defaultCalendarId);
                }
                newProject.addCalendars(context, calendarIds);

                ContextUtil.commitTransaction(context);
                //required for Calendars
                Task rollup = new Task(newProjectId);
                rollup.rollupAndSave(context);
            }
        } catch (Exception ex) {
            ContextUtil.abortTransaction(context);
            ex.printStackTrace();
            if (ex.getMessage().contains("No create access")) {
                throw new Exception(EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, "emxProgramCentral.Project.NoCreateAccess", context.getSession().getLanguage()));
            } else {
                throw ex;
            }
        } finally {
            PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
        }

        return returnMap;
    }

    public void createNewProjectFromWidgetTest(Context context, String[] args) {

        /*
        BaseCurrency:Won
        BusinessGoal:2023-TEST NAME-12345
        BusinessGoalDisplay:2023-TEST NAME-12345
        BusinessGoalOID:15741.36097.14340.10114
        BusinessUnit:BU-0000010
        BusinessUnitDisplay:에너지기술개발팀
        BusinessUnitOID:15741.36097.34677.20121
        Calendar:GS Caltex 업무달력
        CalendarDisplay:GS Caltex 업무달력
        CalendarOID:15741.36097.34677.20771
        DefaultConstraintType:As Soon As Possible
        ENO_CSRF_TOKEN:48T9-LJF6-KSPC-QWFR-0UHY-ADFD-1ZTS-LP0A
        HelpMarker:emxhelpprojectdetailsspecify
        Name:사본3 미래준비 과제 템플릿 001
        Policy:Project Space
        Program:
        ProgramDisplay:
        ProgramOID:
        Project:RPMS
        ProjectDate:2023. 1. 18.
        ProjectDateAmPm:
        ProjectDate_msvalue:
        ReferenceDocument:Copy
        ReloadOpener:true
        ResourceTemplate:
        ScheduleFrom:Project Start Date
        SeachProject:미래준비 과제 템플릿 001
        SeachProjectDisplay:미래준비 과제 템플릿 001
        SeachProjectOID:15741.36097.61824.17715
        SelectType:single
        StringResourceFileId:emxProgramCentralStringResource
        SuiteDirectory:programcentral
        TypeActual:Project Space
        TypeActualDisplay:프로젝트 공간
        TypeActualOID:
        User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36
        autoNameChecked:false
        charSet:UTF-8
        createJPO:emxProjectSpace:createNewProject
        createProject:Template
        csrfTokenName:ENO_CSRF_TOKEN
        description:템플릿에서 프로젝트:미래준비 과제 템플릿 001
        emxSuiteDirectory:programcentral
        emxTableRowIds:
        financialData:true
        findMxLink:false
        folders:true
        form:PMCCreateProjectFromTemplateForm
        header:emxProgramCentral.Common.Project.TemplateHeader
        keepSourceColors:True
        keepSourceConstraints:true
        languageStr:ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7
        localeObj:ko
        mode:create
        mode:create
        nameField:both
        openerFrame:content
        policy:policy_ProjectSpace
        postProcessJPO:emxProjectSpace:createAndConnectProject
        postProcessURL:../programcentral/emxProgramCentralUtil.jsp?mode=launchProject
        preProcessJavaScript:disableRTF
        questions:
        questionsDisplay:응답할 질문 없음...
        questionsOID:
        showPageURLIcon:false
        slideinType:wider
        spellCheckerLang:en_US
        spellCheckerURL:
        submitAction:nothing
        submitMethod:GET
        suiteKey:ProgramCentral
        targetLocation:slidein
        timeStamp:1673939622697
        timeZone:-9
        toolbar:
        type:type_ProjectSpace
        typeChooser:true
        uiType:form
         */
        HashMap attMap = new HashMap();
        attMap.put("BaseCurrenct", "Won");
        attMap.put("crateProject", "Template");
        attMap.put("BusinessGoalOID", "15741.36097.14340.10114");
        attMap.put("BusinessUnitOID", "15741.36097.34677.20121");
        attMap.put("CalendarOID", "15741.36097.34677.20771");
        attMap.put("DefaultConstraintType", "As Soon As Possible");
        attMap.put("Name", "사본3 미래준비 과제 템플릿 001");
        attMap.put("Policy", "Project Space");
        attMap.put("ProgramOID", "");
        attMap.put("ProjectDate", "2023. 1. 18.");
        attMap.put("SeachProjectOID", "15741.36097.61824.17715");
        attMap.put("TypeActual", "Project Space");
        attMap.put("createProject", "Template");
        attMap.put("charSet", "UTF-8");
        attMap.put("ScheduleFrom", "Project Start Date");
        attMap.put("autoNameChecked", "false");
        attMap.put("type", "type_ProjectSpace");
        attMap.put("timeZone", "-9");
        attMap.put("localeObj", "ko");
        attMap.put("keepSourceColors", "true");
        attMap.put("folders", "true");
        attMap.put("financialData", "true");
        String[] newArgs = JPO.packArgs(attMap);
        try {
            DebugUtil.setDebug(true);
            createNewProjectFromWidget(context, newArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create new blank project space object.
     *
     * @param context - The eMatrix <code>Context</code> object.
     * @param args    holds information about object.
     * @return newly created obejct id;
     * @throws Exception if operation fails.
     */
    @com.matrixone.apps.framework.ui.CreateProcessCallable
    public Map createNewProjectFromWidget(Context context, String[] args) throws Exception {
        gscProjectSpace project = (gscProjectSpace) DomainObject.newInstance(context, ProgramCentralConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);

        gscProjectSpace newProject = (gscProjectSpace) DomainObject.newInstance(context, ProgramCentralConstants.TYPE_PROJECT_SPACE, DomainConstants.PROGRAM);

        Map<String, String> returnMap = new HashMap();
        try {
            ContextUtil.startTransaction(context, true);

            String SCHEDULE_FROM = PropertyUtil.getSchemaProperty(context, "attribute_ScheduleFrom");
            Map<String, String> attributeMap = new HashMap();
            Map<String, String> basicProjectInfo = new HashMap();
            Map<String, String> relatedProjectInfo = new HashMap();

            Map programMap = (HashMap) JPO.unpackArgs(args);
            String objectId = (String) programMap.get("objectId");
            String createProject = (String) programMap.get("createProject");
            String projectName = (String) programMap.get("Name");
            String projectAutoName = (String) programMap.get("autoNameCheck");
            String projectDescrption = (String) programMap.get("Description");
            String businessUnitId = (String) programMap.get("BusinessUnitOID");
            String programId = (String) programMap.get("ProgramOID");
            String businessGoalId = (String) programMap.get("BusinessGoalOID");
            String baseCurrency = (String) programMap.get("BaseCurrency");
            String projectVault = project.DEFAULT_VAULTS;
            String projectVisibility = (String) programMap.get("ProjectVisibility");
            String projectPolicy = (String) programMap.get("Policy");
            String projectScheduleFrom = (String) programMap.get("ScheduleFrom");
            String projectDate = (String) programMap.get("ProjectDate");
            String defaultConstraintType = (String) programMap.get("DefaultConstraintType");
            String projectSpaceType = (String) programMap.get("TypeActual");
            String selectedTemplateId = (String) programMap.get("SeachProjectOID");
            String connectRelatedProjects = (String) programMap.get("connectRelatedProject");
            String copyFinancialData = (String) programMap.get("financialData");
            String copyFolderData = (String) programMap.get("folders");
            String sKeepSourceConstraints = (String) programMap.get("keepSourceConstraints");
            String sKeepSourceColors = (String) programMap.get("keepSourceColors");

            String refernceDocument = (String) programMap.get("ReferenceDocument");
            String deliverabletId = (String) programMap.get("DeliverableOID");
            String calendarId = (String) programMap.get("CalendarOID");
            System.out.println("calendarId : " + calendarId);
            StringList calendarIds = FrameworkUtil.split(calendarId, "|");
			
			// HJ
            String gscProjectType = (String) programMap.get("gscProjectType");
            String gscBusinessArea = (String) programMap.get("gscBusinessArea");

            System.out.println("selectedTemplateId : " + selectedTemplateId);
            Locale locale = new Locale((String) programMap.get("localeObj"));
            if (locale == null) {
                locale = context.getLocale();
            }

            String strTimeZone = (String) programMap.get("timeZone");
            double dClientTimeZoneOffset = (new Double(strTimeZone)).doubleValue();
            //  IR-528127-3DEXPERIENCER2018x
            boolean isECHInstalled = FrameworkUtil.isSuiteRegistered(context, "appVersionEnterpriseChange", false, null, null);
            if (isECHInstalled) {
                if (mxType.isOfParentType(context, projectSpaceType, DomainObject.TYPE_CHANGE_PROJECT))
                    programId = (String) programMap.get("ECHMandProgramOID");
            }
            //end  IR-528127-3DEXPERIENCER2018x

            if (ProgramCentralUtil.isNotNullString(projectDate)) {
  				/*projectDate = projectDate.trim();
  				projectDate = eMatrixDateFormat.getFormattedInputDate(context,projectDate,dClientTimeZoneOffset,locale);*/
                TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
                double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
                dClientTimeZoneOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();
                projectDate = projectDate.trim();
                int iDateFormat = eMatrixDateFormat.getEMatrixDisplayDateFormat();
                String strInputTime = eMatrixDateFormat.adjustTimeStringForInputFormat("");
                projectDate = eMatrixDateFormat.getFormattedInputDateTime(projectDate, strInputTime, iDateFormat, dClientTimeZoneOffset, locale);
            }

            //For program,Businessgoal and related project
            if (ProgramCentralUtil.isNotNullString(objectId)) {
                StringList selectable = new StringList();
                selectable.addElement(ProgramCentralConstants.SELECT_IS_PROJECT_SPACE);
                selectable.addElement(ProgramCentralConstants.SELECT_IS_PROGRAM);
                selectable.addElement(ProgramCentralConstants.SELECT_IS_BUSINESS_GOAL);

                DomainObject parentObject = DomainObject.newInstance(context, objectId);
                Map<String, String> parentObjectInfo = parentObject.getInfo(context, selectable);
                String isProjectSpace = parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_PROJECT_SPACE);
                String isProgram = parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_PROGRAM);
                String isBusinessGoal = parentObjectInfo.get(ProgramCentralConstants.SELECT_IS_BUSINESS_GOAL);

                if ("true".equalsIgnoreCase(isProgram)) {
                    programId = objectId;
                } else if ("true".equalsIgnoreCase(isBusinessGoal)) {
                    businessGoalId = objectId;
                } else if ("true".equalsIgnoreCase(isProjectSpace)) {
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

            if ("Clone".equalsIgnoreCase(createProject) || "Template".equalsIgnoreCase(createProject)) {

                SimpleDateFormat dateFormat = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
                Calendar constraintDate = Calendar.getInstance();
                Date newDate = null;
                newDate = dateFormat.parse(projectDate);
                constraintDate.setTime(newDate);
                if (defaultConstraintType.equalsIgnoreCase(ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ASAP)) {
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
            if (ProgramCentralUtil.isNullString(projectName) && projectAutoName.equalsIgnoreCase("true")) {
                String symbolicTypeName = PropertyUtil.getAliasForAdmin(context, "Type", projectSpaceType, true);
                String symbolicPolicyName = PropertyUtil.getAliasForAdmin(context, "Policy", projectPolicy, true);

                projectName = FrameworkUtil.autoName(context, symbolicTypeName, null, symbolicPolicyName, null, null, true, true);
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

            boolean isCopyFolderData = true;
            boolean isCopyFinancialData = true;
            boolean keepSourceConstraints = true;
            boolean keepSourceColors = true;

            if (ProgramCentralUtil.isNullString(copyFolderData) || "false".equalsIgnoreCase(copyFolderData)) {
                isCopyFolderData = false;
            }

            if (ProgramCentralUtil.isNullString(copyFinancialData) || "false".equalsIgnoreCase(copyFinancialData)) {
                isCopyFinancialData = false;
            }

            if (ProgramCentralUtil.isNullString(sKeepSourceConstraints) || "false".equalsIgnoreCase(sKeepSourceConstraints)) {
                keepSourceConstraints = false;
            }
            //Can Comment iF as both statement in If and Else are same
            if (ProgramCentralUtil.isNullString(sKeepSourceColors) || "false".equalsIgnoreCase(sKeepSourceColors)) {
                PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
            } else {
                PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
            }


            ContentReplicateOptions selectedOptionForReferenceDocument = ContentReplicateOptions.COPY;
            if (ProgramCentralUtil.isNullString(refernceDocument) || "Reference".equalsIgnoreCase(refernceDocument)) {
                selectedOptionForReferenceDocument = ContentReplicateOptions.CONNECT_EXISTING;
            }

            //Create new project object.
            if ("Blank".equalsIgnoreCase(createProject) || "Import".equalsIgnoreCase(createProject)) {

                newProject = project.createBlankProject(context, basicProjectInfo, attributeMap, relatedProjectInfo);

            } else if ("Clone".equalsIgnoreCase(createProject)) {
                boolean isConnectRelatedProject = false;
                if (ProgramCentralUtil.isNotNullString(connectRelatedProjects) && connectRelatedProjects.equalsIgnoreCase("True")) {
                    isConnectRelatedProject = true;
                }

                //create new project from existing object.
                newProject = project.clone(context, selectedTemplateId, basicProjectInfo, relatedProjectInfo, attributeMap, isConnectRelatedProject, isCopyFolderData, isCopyFinancialData, keepSourceConstraints);

            } else if ("Template".equalsIgnoreCase(createProject)) {
                boolean isTemplateTaskAutoName = false;
                String questionResponseValue = (String) CacheUtil.getCacheObject(context, "QuestionsResponse");
                String resourceTemplateId = (String) programMap.get("ResourceTemplate");

                Map<String, String> questionResponseMap = new HashMap<String, String>();
                if (ProgramCentralUtil.isNotNullString(questionResponseValue)) {
                    StringList questionResponseValueList = FrameworkUtil.split(questionResponseValue, "|");
                    for (int i = 0; i < questionResponseValueList.size(); i++) {
                        String questionRValue = (String) questionResponseValueList.get(i);
                        StringList questionActualRList = FrameworkUtil.split(questionRValue, "=");
                        questionResponseMap.put((String) questionActualRList.get(0), (String) questionActualRList.get(1));
                    }
                }


                //update related info
                relatedProjectInfo.put("resourceTemplateId", resourceTemplateId);

                newProject = project.cloneTemplateToCreateProject(context, selectedTemplateId, basicProjectInfo, relatedProjectInfo, attributeMap, questionResponseMap, isTemplateTaskAutoName, isCopyFolderData, isCopyFinancialData, keepSourceConstraints, selectedOptionForReferenceDocument, calendarIds);
            }
            //Get new project ID
            String newProjectId = newProject.getObjectId();

            returnMap.put("id", newProjectId);
            if (!"Template".equalsIgnoreCase(createProject)) {
                // If only one calendar is selected then that will be connected as Default Calendar
                if (calendarIds.size() == 1) {
                    String defaultCalendarId = calendarIds.get(0) + "|DefaultCalendar";
                    calendarIds.remove(0);
                    calendarIds.add(defaultCalendarId);
                }
                newProject.addCalendars(context, calendarIds);

                ContextUtil.commitTransaction(context);
                //required for Calendars
                Task rollup = new Task(newProjectId);
                rollup.rollupAndSave(context);
            }
        } catch (Exception ex) {
            ContextUtil.abortTransaction(context);
            ex.printStackTrace();
            if (ex.getMessage().contains("No create access")) {
                throw new Exception(EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL, "emxProgramCentral.Project.NoCreateAccess", context.getSession().getLanguage()));
            } else {
                throw ex;
            }
        } finally {
            PropertyUtil.setGlobalRPEValue(context, "CopyColorAttribute", "true");
        }

        return returnMap;
    }
	
	private static String getNameCode(Context context, String gscProjectType, String gscBusinessArea) throws Exception {
        // 230411 Customer Project Name(Code) Setting
        // <SysYear>-<ProjectType>-<BusinessArea>-<AutoNum>

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat yearformat= new SimpleDateFormat("yyyy");
        String SysYear = yearformat.format(date);
        String strProjectyear = SysYear.substring(SysYear.length() - 2); // get SysDate last 2

        String NameCode4ProjectType = "";

        if(ProgramCentralUtil.isNullString(gscProjectType)){
            throw new Exception("gscProjectType 값이 비어있습니다.");
        }else{
            if(gscProjectType.equals("PT01")){
                NameCode4ProjectType = "F";
            }else if(gscProjectType.equals("PT02")) {
                NameCode4ProjectType = "E";
            }else if(gscProjectType.equals("PT03")) {
                NameCode4ProjectType = "R";
            }else if(gscProjectType.equals("PT04")) {
                NameCode4ProjectType = "P";
            }else{
                throw new Exception("존재하지 않는 gscProjectType 값입니다.");
            }
        }

        String NameCode4BusinessArea = "";

        if(ProgramCentralUtil.isNullString(gscBusinessArea)){
            throw new Exception("gscBusinessArea 값이 비어있습니다.");
        }else{
            if(gscBusinessArea.equals("BA01")){
                NameCode4BusinessArea = "HC";
            }else if(gscBusinessArea.equals("BA02")) {
                NameCode4BusinessArea = "WB";
            }else if(gscBusinessArea.equals("BA03")) {
                NameCode4BusinessArea = "PR";
            }else if(gscBusinessArea.equals("BA04")) {
                NameCode4BusinessArea = "PM";
            }else if(gscBusinessArea.equals("BA05")) {
                NameCode4BusinessArea = "CH";
            }else if(gscBusinessArea.equals("BA06")) {
                NameCode4BusinessArea = "RT";
            }else if(gscBusinessArea.equals("BA07")) {
                NameCode4BusinessArea = "LB";
            }else if(gscBusinessArea.equals("BA08")) {
                NameCode4BusinessArea = "ET";
            }else{
                throw new Exception("존재하지 않는 gscBusinessArea 값입니다.");
            }
        }

        // <SysYear>-<ProjectType>-<BusinessArea> 설정
        String strProjectFirstName = strProjectyear + '-' + NameCode4ProjectType + '-' + NameCode4BusinessArea;

        int maxAutoNum = 0;

        String mqlCommand = "temp query bus $1 $2 $3 select name dump $4";
        String mqlquery = com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, mqlCommand, "Project Space", strProjectFirstName+"-*", "*", "|");

        if(mqlquery != null || !mqlquery.isEmpty()){
            StringList listProjectSpace = FrameworkUtil.split(mqlquery,"\n");

            for(int i = 0; i < listProjectSpace.size(); i++){
                String strProjectSpace = listProjectSpace.get(i);
                StringList listProjectName = FrameworkUtil.split(strProjectSpace,"|");
                if(listProjectName.size() <= 3){
                    throw new Exception("Project name does not exist.");
                }else{
                    String ProjectName = listProjectName.get(3);
                    String existAutoNum = ProjectName.substring(ProjectName.length()-3); // get ProjectName last 3 >> AutoNum
                    int intexistAutoNum = Integer.parseInt(existAutoNum);

                    if(intexistAutoNum > maxAutoNum){
                        maxAutoNum = intexistAutoNum;
                    }
                }
            }
        }

        // AutoNum 설정 - 존재하는 AutoNum 최고 값 + 1
        String AutoNum = Integer.toString(maxAutoNum+1);

        // AutoNum 길이 최대 3 - ex) 001, 010, 100
        if(AutoNum.length() == 1){
            AutoNum = "00" + AutoNum;
        }else if(AutoNum.length() == 2){
            AutoNum = "0" + AutoNum;
        }

        // return Project name 선언
        String name = strProjectFirstName + "-" + AutoNum;

        if (name != null) {
            name = name.trim();
        }

        if ((name == null || !name.isEmpty())) {
            return name;
        } else {
            String var4 = com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil.getTranslatedValue(context, "Foundation", "emxFoundation.Widget.Error.EmptyNameNotAllowed", context.getLocale());
            throw new Exception(var4);
        }
    }
	
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Locale locale = new Locale("en-US");
        Context context = new matrix.db.Context("http://rpmsdev.gscaltex.co.kr:8070/internal");
        context.setUser("admin_platform");
        context.setPassword("Qwer1234");
        context.setLocale(locale);
        context.setRole("ctx::VPLMProjectLeader.Company Name.RPMS");
        context.connect();
        emxProjectSpace_mxJPO jpo = new emxProjectSpace_mxJPO(context, args);

        HashMap<String, String> hashMapValue = new HashMap<>();
        String busProjectId = "499948B20000427063D0E9420000061C";
        String initiatedTemplateProjectId = "29614DCF0000270C63B4E9CB000003E6";
        hashMapValue.put("ProjectId", busProjectId);
        hashMapValue.put("TemplateId", initiatedTemplateProjectId);
        String[] JPOArgs = JPO.packArgs(hashMapValue);

        jpo.cloneStructureFromTemplate(context, JPOArgs);
    }
}
