package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.*;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.MqlUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.*;
import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.StringUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.mail.MailNotification;
import com.dassault_systemes.enovia.tskv2.ProjectSequence;
import com.matrixone.apps.common.Task;
import com.matrixone.apps.common.*;
import com.matrixone.apps.domain.DomainAccess;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.program.*;
import com.matrixone.apps.program.fiscal.Helper;
import matrix.db.BusinessObjectWithSelect;
import matrix.db.BusinessObjectWithSelectList;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.MatrixException;
import matrix.util.StringList;

import javax.json.JsonArray;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.matrixone.apps.program.ProjectSpace.SELECT_SOURCE_ID;

public class gscProjectService extends ProjectManagement implements ServiceConstants {
    public static final String PARAMETER_PROGRAM_ID = "programId";
    public static final String PARAMETER_STATE = "state";
    public static final String PARAMETER_OWNER = "owned";
    public static final String PARAMETER_ASSIGNED = "assigned";
    public static final String PARAMETER_STRUCTURED = "structured";
    public static final String SELECT_PHYSICAL_ID = "physicalid";
    public static final String PARAMETER_EXCLUDE_SUBTYPES = "excludeSubtypes";
    public static final String PARAMETER_COMPLETED = "completed";
    private static final String POLICY_PROXYITEM = "ProxyItem";
    private static final String SELECT_PROXY_TENANT = "attribute[Proxy_Tenant].value";
    private static final String SELECT_PROXY_SERVICE = "attribute[Proxy_Service].value";
    private static final String SELECT_PROXY_MODIFIED = "attribute[Proxy_Modified].value";
    private static final String SELECT_PROXY_STATE = "attribute[Proxy_State].value";
    private static final String SELECT_PROXY_ID = "attribute[Proxy_Id].value";
    private static final String SELECT_PROXY_TITLE = "attribute[Proxy_Title].value";
    private static final String SELECT_PROXY_URL = "attribute[Proxy_URL].value";
    private static final String COPY_FROM_FILE = "CopyFromFile";
    private static final String COPY_FROM_TEMPLATE = "CopyFromTemplate";
    private static final String SELECT_PHYSICALID = "physicalid";
    private static final String SELECT_PROXYITEM = "interface[ProxyItem]";
    private static final String SELECT_XPLANID = "3DPlanId";
    private static final String SERVICE_XPLAN = "3DPlan";
    private static final String SERVICE_LABEL = "serviceId";
    private static final String ATTRIBUTE_PROXY_TENANT = "ProxyItem.Proxy_Tenant";
    private static final String ATTRIBUTE_PROXY_SERVICE = "ProxyItem.Proxy_Service";
    private static final String ATTRIBUTE_PROXY_MODIFIED = "ProxyItem.Proxy_Modified";
    private static final String ATTRIBUTE_PROXY_STATE = "ProxyItem.Proxy_State";
    private static final String ATTRIBUTE_PROXY_ID = "ProxyItem.Proxy_Id";
    private static final String ATTRIBUTE_PROXY_TITLE = "ProxyItem.Proxy_Title";
    private static final String ATTRIBUTE_PROXY_URL = "ProxyItem.Proxy_URL";
    private static final String SELECT_TASK_TRANSFER;
    private static final String SELECT_QUESTION_ID;
    private static final String CACHE_WORK_CALENDAR_ASSIGNEE = "WorkCalendar_Assignee_Cache";
    private static final String CACHE_WORK_CALENDAR_LOCATION = "WorkCalendar_Location_Cache";
    private static final String SELECT_PARENT_PROJECT_DEFAULT_CALENDAR_ID;
    private static final String SELECT_PARENT_PROJECT_DEFAULT_CALENDAR_DATE;
    private static final String SELECT_PROJECT_DEFAULT_CALENDAR_ID;
    private static final String SELECT_PROJECT_DEFAULT_CALENDAR_DATE;
    private static final String SELECT_WORK_START_TIME = "attribute[Work Start Time].value";
    private static final String SELECT_START_MINUTE = "Start Minute";
    private static final String SELECT_WORK_FINISH_TIME = "attribute[Work Finish Time].value";
    private static final String SELECT_FINISH_MINUTE = "Finish Minute";
    private static final String SELECT_WORKING_TIME_PER_DAY = "attribute[Working Time Per Day].value";
    private static final String CREATE_PROJECT_SUBJECT_SUCUSSESS = "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CreateProjectSubject";
    private static final String CREATE_PROJECT_SUBJECT_FAILED = "emxProgramCentral.Import.BackgoundProcessFailedNotifyMessage.CreateProjectSubject";
    private static final String COPY_SCHEDULE_SUBJECT_SUCUSSESS = "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CopyScheduleSubject";
    private static final String COPY_SCHEDULE_SUBJECT_FAILED = "emxProgramCentral.Import.BackgoundProcessFailedNotifyMessage.CopyScheduleSubject";
    private static final String CREATE_PROJECT_BODY_SUCUSSESS = "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CreateProjectBody";
    private static final String CREATE_PROJECT_BODY_FAILED = "emxProgramCentral.Import.BackgoundProcessFailedNotifyMessage.CreateProjectBody";
    private static final String COPY_SCHEDULE_BODY_SUCUSSESS = "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CopyScheduleBody";
    private static final String COPY_SCHEDULE_BODY_FAILED = "emxProgramCentral.Import.BackgoundProcessFailedNotifyMessage.CopyScheduleBody";

    public static Datacollection getProjects(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
        //ServiceUtil.checkLicenseProject(context, httpRequest);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> parametersSelects = serviceParameters.getSelects();
        ArgMap serviceArgs = serviceParameters.getServiceArgs();
        String state = (String)serviceArgs.get("state");
        String owned = (String)serviceArgs.get("owned");
        String structured = (String)serviceArgs.get("structured");
        String programId = (String)serviceArgs.get("programId");
        String excludeSubtypes = (String)serviceArgs.get("excludeSubtypes");
        String completed = (String)serviceArgs.get("completed");
        String polydata = (String)serviceArgs.get("polydata");
        Datacollection datacollection1 = datacollection;

        // 230425 HJ 조회조건 추가
        String businessUnitId = (String)serviceArgs.get("businessUnitId");

        boolean isOwned = false;
        boolean isStructured = false;
        if (owned != null && !owned.isEmpty()) {
            isOwned = Boolean.parseBoolean(owned);
        }

        if (structured != null && !structured.isEmpty()) {
            isStructured = Boolean.parseBoolean(structured);
        }

        String mode;
        if (serviceParameters.getServiceArgs().containsKey("mode")) {
            mode = (String)serviceParameters.getServiceArgs().get("mode");
            if ("getUpdatedData".equalsIgnoreCase(mode)) {
                String title = (String)serviceParameters.getServiceArgs().get("title");
                List<Dataobject> dataobjectList = datacollection.getDataobjects();
                Dataobject dataobject = dataobjectList.get(0);
                String dataobjectId = dataobject.getId();
                Map getupdatedData = com.dassault_systemes.enovia.dpm.ProjectSpace.getupdatedData(context, dataobjectId, title);
                DataelementMap dataelementMap = dataobject.getDataelements();
                if (dataelementMap == null) {
                    dataelementMap = new DataelementMap();
                }

                if (getupdatedData.containsKey("physicalId")) {
                    dataelementMap.put("physicalId", (String)getupdatedData.get("physicalId"));
                } else if (getupdatedData.containsKey("Error")) {
                    dataelementMap.put("Error", getupdatedData.get("Error"));
                }

                dataobject.setDataelements(dataelementMap);
                return datacollection;
            }
        } else if (serviceParameters.getServiceArgs().containsKey("query")) {
            mode = (String)serviceParameters.getServiceArgs().get("query");
            Datacollection var23 = loadResources(context, mode);
            RelateddataMap var24 = new RelateddataMap();
            var24.put("Resources", (ArrayList<Dataobject>)var23.getDataobjects());
            List<Dataobject> var25 = datacollection.getDataobjects();
            Dataobject var26 = var25.get(0);
            var26.setRelateddata(var24);
        } else if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            datacollection1 = gscProjectSpace.getUserProjects(context, datacollection, false, isStructured, parametersSelects, (String)null);
        } else if (programId != null && !"".equals(programId)) {
            datacollection1 = gscProjectSpace.getProgramProjects(context, programId, parametersSelects);
        } else {
            datacollection1 = gscProjectSpace.getUserProjects(context, (Datacollection)null, isOwned, isStructured, parametersSelects, state, excludeSubtypes, completed, businessUnitId);
        }

        return datacollection1;
    }

    public static Datacollections getProjectMilestones(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
        //ServiceUtil.checkLicenseProject(context, httpRequest);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> selects = serviceParameters.getSelects();
        String filterId = serviceParameters.getFilters().getId();
        ArgMap serviceArgs = serviceParameters.getServiceArgs();
        String state = (String)serviceArgs.get("state");
        String owned = (String)serviceArgs.get("owned");
        boolean isOwned = false;
        boolean var11 = false;
        if (owned != null && !owned.isEmpty()) {
            isOwned = Boolean.parseBoolean(owned);
        }

        Datacollections projectMilestones = ProjectMilestone.getProjectMilestones(context, datacollection, filterId, isOwned, selects, state);
        return projectMilestones;
    }

    public static Dataobject updateProjectMilestones(Context var0, String[] var1) throws Exception {
        return ProjectMilestone.updateProjectMilestones(var0, var1);
    }

    protected static void addProxyInterface(Context var0, String var1) throws FoundationException {
        MqlUtil.mqlCommand(var0, "modify bus $1 add $2 $3", new String[]{var1, "interface", "ProxyItem"});
    }

    protected static void removeProxyInterface(Context var0, String var1) throws FoundationException {
        MqlUtil.mqlCommand(var0, "modify bus $1 remove $2 $3", new String[]{var1, "interface", "ProxyItem"});
    }

    private static String getBusinessUnitId(Context context, Dataobject dataobject, String nameCode) throws FoundationUserException {
        // String nameCode = DataelementMapAdapter.getDataelementValue(dataobject, "businessUnitProject");
        if (nameCode != null) {
            nameCode = nameCode.trim();
        }

        UpdateActions updateAction = dataobject.getUpdateAction();
        if ((nameCode == null || !nameCode.isEmpty()) && (nameCode != null || !UpdateActions.CREATE.equals(updateAction))) {
            return nameCode;
        } else {
            String var4 = PropertyUtil.getTranslatedValue(context, "Foundation", "emxFoundation.Widget.Error.EmptyBusinessUnitProjectNotAllowed", context.getLocale());
            throw new FoundationUserException(var4);
        }
    }

    private static String getNameCode(Context context, Dataobject dataobject, String gscProjectType, String gscBusinessArea) throws Exception {
        // 230411 고객사 요구사항 Project Name(Code) 설정
        // <SysYear>-<ProjectType>-<BusinessArea>-<AutoNum> 설정을 위한 함수

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat yearformat= new SimpleDateFormat("yyyy");
        String SysYear = yearformat.format(date);
        String strProjectyear = SysYear.substring(SysYear.length() - 2); // get SysDate last 2

        // gscProjectType 값을 통해 Name Code 설정 - 전처리
        String NameCode4ProjectType = "";

        if(ProgramCentralUtil.isNullString(gscProjectType)){
            throw new FoundationUserException("gscProjectType 값이 비어있습니다.");
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
                throw new FoundationUserException("존재하지 않는 gscProjectType 값입니다.");
            }
        }

        // gscBusinessArea 값을 통해 Name Code 설정 - 전처리
        String NameCode4BusinessArea = "";

        if(ProgramCentralUtil.isNullString(gscBusinessArea)){
            throw new FoundationUserException("gscBusinessArea 값이 비어있습니다.");
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
                throw new FoundationUserException("존재하지 않는 gscBusinessArea 값입니다.");
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
                    throw new FoundationUserException("Project name does not exist.");
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

        UpdateActions updateAction = dataobject.getUpdateAction();
        if ((name == null || !name.isEmpty()) && (name != null || !UpdateActions.CREATE.equals(updateAction))) {
            return name;
        } else {
            String var4 = PropertyUtil.getTranslatedValue(context, "Foundation", "emxFoundation.Widget.Error.EmptyNameNotAllowed", context.getLocale());
            throw new FoundationUserException(var4);
        }
    }

    public static void cloneStructureFromTemplate(Context context, String[] args) throws Exception {
        try{
            // String projectId = "15741.36097.11246.18442";//args[0];
            // String templateId = "15741.36097.61824.17715";//args[1];

            Map<String,String> jpoArgumentMap  = (Map<String,String>) JPO.unpackArgs(args);
            String projectId = jpoArgumentMap.get("ProjectId");
            String templateId = jpoArgumentMap.get("TemplateId");

            com.gsc.apps.program.gscProjectSpace newProjectSpace = new com.gsc.apps.program.gscProjectSpace(projectId);
            com.gsc.apps.program.gscProjectSpace templateProjectSpace = new com.gsc.apps.program.gscProjectSpace(templateId);

            ProjectTemplate template = new ProjectTemplate(templateId);

            // this.setId(templateId);

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
                com.matrixone.apps.domain.util.ContextUtil.pushContext(context, com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(context, "person_UserAgent"), "", "");
                var51 = true;
                if (copyRoadMap) {
                    taskList = ProjectSpace.getTemplateTasks(context, template, 0, templBusSelect, (StringList) null, false, false, true, templRelSelect);
                } else {
                    taskList = ProjectSpace.getTemplateTasks(context, template, 0, templBusSelect, (StringList) null, false, false, false, templRelSelect);
                }
            } finally {
                if (var51) {
                    com.matrixone.apps.domain.util.ContextUtil.popContext(context);
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

            if (template.isKindOf(context, DomainConstants.TYPE_PROJECT_TEMPLATE) && newProjectSpace.isKindOf(context, DomainConstants.TYPE_PROJECT_SPACE)) {

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

                // JPO.invoke(context, "emxProjectSpace", (String[]) null, "createProjectFromProjectTemplate", packArgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void createProjectFromProjectTemplate(Context context, String[] args) throws Exception {
        try {
            Map programMap = (Map) JPO.unpackArgs(args);
            String projectId = (String) programMap.get("projectId");
            String templateId = (String) programMap.get("projectTemplateId");
            MapList taskMapList = (MapList) programMap.get("TaskMapList");
            Map answerList = (Map) programMap.get("answerList");
            String defaultTaskConstraintType = (String) programMap.get("defaultTaskConstraintType");
            HashMap selectedOptionMap = (HashMap) programMap.get("selectedOption");
            gscProjectService.completePTCloningProcess(context, projectId, taskMapList, answerList, defaultTaskConstraintType, selectedOptionMap, templateId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Dataobject updateProjectSpace(Context context, String[] var1) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject)datacollection.getDataobjects().get(0);
        UpdateActions updateAction = dataobject.getUpdateAction();
        dataobject = updateProjectSpace(context, serviceParameters, dataobject, updateAction);
        return dataobject;
    }

    public static Dataobject updateProjectSpace(Context context, ServiceParameters serviceParameters, Dataobject dataobject, UpdateActions updateActions) throws Exception {
        if (!UpdateActions.CREATE.equals(updateActions) && !UpdateActions.MODIFY.equals(updateActions)) {
            if (UpdateActions.DELETE.equals(updateActions)) {
                com.matrixone.apps.program.ProjectSpace projectSpace = new com.matrixone.apps.program.ProjectSpace(dataobject.getId());
                projectSpace.deleteObject(context);
                dataobject = null;
            }
        } else {
            // String name = getProjectCode(context, dataobject);
            String name = null;
            HashMap<String, String> hashMap = new HashMap<>(5);
            ServiceDataFunctions.fillUpdates(context, dataobject, serviceParameters.getAutosaveFields(), hashMap);
            handleDuration(context, dataobject, hashMap, updateActions);
            handleFormatPattern(context, dataobject, hashMap, updateActions);
            String constraintType;
            if (!hashMap.containsKey("scheduleFrom")) {
                constraintType = DataelementMapAdapter.getDataelementValue(dataobject, "constraintType");
                if ("As Soon As Possible".equals(constraintType)) {
                    hashMap.put("Schedule From", "Project Start Date");
                } else if ("As Late As Possible".equals(constraintType)) {
                    hashMap.put("Schedule From", "Project Finish Date");
                }
            }

            String policy;
            String projectStartDate;
            String busProjectId;
            String interfaces;
            String businessUnitId = DataelementMapAdapter.getDataelementValue(dataobject, "businessUnitProject");
            String parentProject = DataelementMapAdapter.getDataelementValue(dataobject, "parentProject");

            if(businessUnitId != null){
                businessUnitId = getBusinessUnitId(context, dataobject, businessUnitId);
            }
            // String businessUnitId = getBusinessUnitId(context, dataobject);
            String initiatedTemplateProjectId = "";

            if(DataelementMapAdapter.getDataelementValue(dataobject, "initiatedTemplateProject") != null) {
                initiatedTemplateProjectId = DataelementMapAdapter.getDataelementValue(dataobject, "initiatedTemplateProject");
            }


            if (UpdateActions.CREATE.equals(updateActions)) {
                if (!hashMap.containsKey(ProgramCentralConstants.ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE)) {
                    if ("Project Finish Date".equals(hashMap.get("Schedule From"))) {
                        hashMap.put(ProgramCentralConstants.ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE, "As Late As Possible");
                    } else {
                        hashMap.put(ProgramCentralConstants.ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE, "As Soon As Possible");
                    }
                }
                String currency = "";
                if (!hashMap.containsKey("currency") && !hashMap.containsKey("Currency")) {
                    currency = PersonUtil.getCurrency(context);
                    if (currency != null && !currency.isEmpty()) {
                        hashMap.put(ProgramCentralConstants.ATTRIBUTE_CURRENCY, currency);
                    }
                }

                String type = DataelementMapAdapter.getDataelementValue(dataobject, "projectType");
                policy = DataelementMapAdapter.getDataelementValue(dataobject, "policy");
                projectStartDate = DataelementMapAdapter.getDataelementValue(dataobject, "projectStartDate");
                if (UIUtil.isNotNullAndNotEmpty(projectStartDate)) {
                    hashMap.put(com.dassault_systemes.enovia.dpm.ProjectSpace.ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE, hashMap.get(com.dassault_systemes.enovia.dpm.ProjectSpace.ATTRIBUTE_TASK_ESTIMATED_START_DATE));
                }

                hashMap.put(ATTRIBUTE_TASK_ESTIMATED_DURATION, "0.0");
                type = type != null && !type.isEmpty() ? type : dataobject.getType();
                ArgMap serviceArgs = serviceParameters.getServiceArgs();

                String triggerStatus = (String)serviceArgs.get("TriggerStatus");
                if (triggerStatus != null && triggerStatus.equalsIgnoreCase("false")) {
                    Map<String, StringList> interfacesMap = ExtensionUtil.getAutomaticInterfacesByType(context, ProgramCentralConstants.TYPE_PROJECT_MANAGEMENT);
                    StringList projectSpaceList = interfacesMap != null ? interfacesMap.get(ProgramCentralConstants.TYPE_PROJECT_SPACE) : null;
                    if (projectSpaceList != null) {
                        interfaces = projectSpaceList.join(",");
                        hashMap.put("interface", interfaces);
                    }
                }

                // Name Rule Check <YEAR2>-<PROJECT TYPE>-<BUSINESS AREA>-<Auto NUM>
                String gscProjectType = DataelementMapAdapter.getDataelementValue(dataobject, "gscProjectType");
                String gscBusinessArea = DataelementMapAdapter.getDataelementValue(dataobject, "gscBusinessArea");
                name = getNameCode(context, dataobject, gscProjectType, gscBusinessArea);

                busProjectId = gscProjectSpace.createProjectSpace(context, type, name, hashMap, policy);

                // UpdateActions 이 CREATE 일 경우
                // RelationShip 처리 - 복제 전 처리
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Business Unit Project", businessUnitId, false, false);
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Subtask", parentProject, false, true);
                if(com.gsc.util.ServiceUtil.chkNotNull(initiatedTemplateProjectId)){
                    com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Initiated Template Project", initiatedTemplateProjectId, true, true);
                }

                // Share with User Groups 처리
                String busId = busProjectId; // ProjectSpace Id;
                StringList accessNames = DomainAccess.getLogicalNames(context, busId);
                String defaultAccess = (String)accessNames.get(0);
                // ID 입력하여, Group Name 조회 생략 가능
                // String userGroupId = "299169C0000053C863E9F3C500000090"; // Group Id; businessobject Group RnD_Planning - physicalid = 299169C0000053C863E9F3C500000090
                // String userGroupName = new DomainObject(userGroupId).getInfo(context, DomainObject.SELECT_NAME);
                // 우선 Policy 로 처리하는 방안으로, Policy 처리 안될 경우, 커맨드 입력
                // DomainAccess.createObjectOwnershipForUserGroups(context, busId, "RnD_Planning", defaultAccess, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP);

                dataobject.setId(busProjectId);
            } else {

                ServiceUtil.addToMap(hashMap, "name", name);
                busProjectId = dataobject.getId();

                if (hashMap.containsKey("ganttConfig")) {
                    ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var17 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(2);
                    var17.add(new Select("GanttConfig", ProgramCentralConstants.SELECT_GANTT_CONFIG_FROM_PROJECT, ExpressionType.BUS, (Format)null, false));
                    var17.add(new Select("palId", ProgramCentralConstants.SELECT_PAL_OBJECTID_FROM_PROJECT, ExpressionType.BUS, (Format)null, false));
                    ObjectUtil.print(context, dataobject, (PrintData)null, var17, true);
                    policy = DataelementMapAdapter.getDataelementValue(dataobject, "GanttConfig");
                    projectStartDate = DataelementMapAdapter.getDataelementValue(dataobject, "palId");
                    String var9 = "";
                    Map map = new HashMap();
                    if (policy != null && !policy.isEmpty()) {
                        var9 = unzip(policy);
                        var9 = var9.replace("{", "");
                        var9 = var9.replace("}", "");
                        map = (Map)Arrays.stream(var9.split(",")).map((var0x) -> {
                            return var0x.split(":");
                        }).collect(Collectors.toMap((var0x) -> {
                            return var0x[0].trim();
                        }, (var0x) -> {
                            return var0x[1];
                        }));
                    }

                    busProjectId = (hashMap.get("ganttConfig")).toString();
                    busProjectId = busProjectId.replace("{", "");
                    busProjectId = busProjectId.replace("}", "");
                    Map var12 = (Map)Arrays.stream(busProjectId.split(",")).map((var0x) -> {
                        return var0x.split(":");
                    }).collect(Collectors.toMap((var0x) -> {
                        return var0x[0].trim();
                    }, (var0x) -> {
                        return var0x[1];
                    }));
                    ((Map)map).putAll(var12);
                    interfaces = "{" + (String)(map).entrySet().stream().map((var0x) -> {
                        return (String)((Map.Entry)var0x).getKey() + ":" + ((Map.Entry)var0x).getValue();
                    }).collect(Collectors.joining(", ")) + "}";
                    String var14 = zip(interfaces);
                    MqlUtil.mqlCommand(context, "modify bus $1 $2 $3", new String[]{projectStartDate, "GanttConfig", var14});
                    hashMap.remove("ganttConfig");
                }

                // UpdateActions 이 MODIFY 일 경우
                // RelationShip 처리 - 복제 전 처리
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Business Unit Project", businessUnitId, false, false);
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Subtask", parentProject, false, true);

                com.dassault_systemes.enovia.dpm.ProjectSpace.modifyProjectSpace(context, dataobject.getId(), hashMap);
            }

            dataobject = getObject(context, serviceParameters, dataobject.getId(), (String)null, (String)null);
            Dataobject var18 = new Dataobject();
            var18.setUpdateAction(updateActions);
            RelateddataMapAdapter.addRelatedData(dataobject, "performRollup", var18);

            if ((UpdateActions.CREATE.equals(updateActions)) && com.gsc.util.ServiceUtil.chkNotNull(initiatedTemplateProjectId)) {
                HashMap<String, String> hashMapValue = new HashMap<>();
                hashMapValue.put("ProjectId", busProjectId);
                hashMapValue.put("TemplateId", initiatedTemplateProjectId);
                String[] JPOArgs = JPO.packArgs(hashMapValue);

                // JPO.invoke(context,"emxProjectSpace",null,"cloneStructureFromTemplate",JPOArgs,null);
                cloneStructureFromTemplate(context, JPOArgs);

                // RelationShip 후처리
                // com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Business Unit Project", businessUnitId, false, false);
                // com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Subtask", parentProject, false, true);
                //if(com.gsc.util.ServiceUtil.chkNotNull(initiatedTemplateProjectId)){
                //    com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Initiated Template Project", initiatedTemplateProjectId, true, true);
                //}
            }else if((UpdateActions.MODIFY.equals(updateActions)) && com.gsc.util.ServiceUtil.chkNotNull(initiatedTemplateProjectId)) {
                // UpdateActions 이 MODIFY 이고, initiatedTemplateProjectId 가 존재할 경우
                // gscCreateTemplate 함수 실행(task 미존재 및 기존 Initiated Template Project relation 이 없을 경우, 템플릿으로 부터 생성 기능 실행)
                gscCreateTemplate(context, updateActions, busProjectId,initiatedTemplateProjectId, businessUnitId, parentProject);
            }
        }

        return dataobject;
    }

    public static void gscCreateTemplate(Context context, UpdateActions updateActions, String busProjectId, String initiatedTemplateProjectId, String businessUnitId, String parentProject) throws Exception {
        try {
            boolean chkCreateFromTemplate = false;
            String mqlBasicRelId = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump;",busProjectId,"relationship[Initiated Template Project].to.physicalid");
            chkCreateFromTemplate = (!mqlBasicRelId.equals(initiatedTemplateProjectId))? true : false; // 입력 initiatedTemplateProjectId 와 기존 rel id 가 다를 경우, true

            // 입력 initiatedTemplateProjectId 와 기존 rel id 가 다를 경우 실행됨, 같은 경우 무시
            if(chkCreateFromTemplate) {
                boolean chkTaskExist = false;
                String mqlResult = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump $3;",busProjectId,"relationship[Subtask].to","|");
                chkTaskExist = (ProgramCentralUtil.isNullString(mqlResult))? false : true;

                // 기존 Task 가 존재하지 않을 경우, 템플릿으로 부터 생성 기능 실행, 존재할 경우, exception
                if(!chkTaskExist){
                    HashMap<String, String> hashMapValue = new HashMap<>();
                    hashMapValue.put("ProjectId", busProjectId);
                    hashMapValue.put("TemplateId", initiatedTemplateProjectId);
                    String[] JPOArgs = JPO.packArgs(hashMapValue);

                    // RelationShip 처리 - 복제 전 처리
                    // Initiated Template Project Relation 을 cloneStructureFromTemplate 에서 사용
                    com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Business Unit Project", businessUnitId, false, false);
                    com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Subtask", parentProject, false, true);
                    com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Initiated Template Project", initiatedTemplateProjectId, true, true);
                    
                    // JPO.invoke(context,"emxProjectSpace",null,"cloneStructureFromTemplate",JPOArgs,null);
                    cloneStructureFromTemplate(context, JPOArgs);

                    
                }else{
                    throw new Exception("기존 Task가 존재 합니다. 템플릿으로 부터 생성하기 위해서는 기존 Task가 존재하지 않아야 합니다. 기존 Task 전부 삭제 후, 다시 시도해주세요.");
                }
            }else{
                // RelationShip 처리 - 복제 후 처리
                // com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Business Unit Project", businessUnitId, false, false);
                // com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectId, "Subtask", parentProject, false, true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    private static void handleDuration(Context var0, Dataobject var1, Map<String, String> var2, UpdateActions var3) throws FoundationException {
        String var4 = DataelementMapAdapter.getDataelementValue(var1, "estimatedDurationInputValue");
        String var5 = DataelementMapAdapter.getDataelementValue(var1, "estimatedDurationInputUnit");
        if (var4 != null && !var4.isEmpty() || var5 != null && !var5.isEmpty()) {
            String var6 = var2.get(ATTRIBUTE_TASK_ESTIMATED_DURATION);
            if (UpdateActions.MODIFY.equals(var3)) {
                ArrayList<Select> var7 = new ArrayList<Select>(2);
                if (var6 == null || var6.isEmpty()) {
                    var7.add(com.dassault_systemes.enovia.dpm.ProjectSpace.SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE);
                }

                if (var5 == null || var5.isEmpty()) {
                    var7.add(com.dassault_systemes.enovia.dpm.ProjectSpace.SELECT_TASK_ESTIMATED_DURATION_INPUT_UNIT);
                }

                if (!var7.isEmpty()) {
                    Dataobject var8 = com.gsc.enovia.dpm.gscProjectSpace.getProjectInfo(var0, (String)var1.getId(), var7);
                    String var9 = DataelementMapAdapter.getDataelementValue(var8, com.dassault_systemes.enovia.dpm.ProjectSpace.SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE.getName());
                    String var10 = DataelementMapAdapter.getDataelementValue(var8, com.dassault_systemes.enovia.dpm.ProjectSpace.SELECT_TASK_ESTIMATED_DURATION_INPUT_UNIT.getName());
                    var6 = var9 != null ? var9 : var6;
                    var5 = var10 != null ? var10 : var5;
                }
            }

            if (var6 != null && !var6.isEmpty() && var5 != null && !var5.isEmpty()) {
                var6 = var6 + " " + var5;
                var2.put(ATTRIBUTE_TASK_ESTIMATED_DURATION, var6);
            }
        }

    }

    public static Dataobject performRollup(Context var0, String[] var1) throws Exception {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        Dataobject var4 = ((Dataobject)var3.getDataobjects().get(0)).getParent();
        Dataobject var5 = new Dataobject();
        var5.setId(var4.getId());
        String var6 = (String)var2.getServiceArgs().get("rollup");
        String var7 = (String)var2.getServiceArgs().get("disableTriggers");
        if ("live".equalsIgnoreCase(var6)) {
            List var8 = com.dassault_systemes.enovia.dpm.ProjectSpace.performRollup(var0, var4, "true".equalsIgnoreCase(var7));
            Datacollection var9 = new Datacollection();
            var2.setDatacollection(var9);
            Iterator var10 = var8.iterator();

            while(var10.hasNext()) {
                String var11 = (String)var10.next();
                Dataobject var12 = new Dataobject();
                var12.setId(var11);
                var9.getDataobjects().add(var12);
            }

            var2.getServiceArgs().put("$include", null);
            ServiceSave.getUpdatedObjects(var0, "tasks", var2, (String)var2.getServiceArgs().get("$fields"));
            var9 = var2.getDatacollection();
            var5.getChildren().addAll(var9.getDataobjects());
        } else if (!"none".equalsIgnoreCase(var6)) {
            com.dassault_systemes.enovia.dpm.ProjectSpace var13 = new com.dassault_systemes.enovia.dpm.ProjectSpace();
            Object[] var14 = new Object[]{var0, var4, false};
            ContextUtil.submitPostTransactionJob(var0, var13, "performRollup", var14);
        }

        return var5;
    }

    public static Dataobject updateMembers(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject)datacollection.getDataobjects().get(0);
        String dataobjectId = dataobject.getId();
        Dataobject dataobjectParent = dataobject.getParent();
        String parentId = dataobjectParent.getId();
        UpdateActions var8 = dataobject.getUpdateAction();
        if (!UpdateActions.CONNECT.equals(var8) && !UpdateActions.CREATE.equals(var8) && !UpdateActions.MODIFY.equals(var8)) {
            if (UpdateActions.DISCONNECT.equals(var8) || UpdateActions.DELETE.equals(var8)) {
                gscProjectSpace.removeProjectMember(context, parentId, dataobjectId);
                dataobject = null;
            }
        } else {
            String projectAccess = "Project Access";
            String projectAccessValue = RelelementMapAdapter.getRelelementValue(dataobject, "projectAccess");
            String gscRatio = "gscRatio";
            String gscRatioValue = RelelementMapAdapter.getRelelementValue(dataobject, "gscRatio");
            HashMap hashMap = new HashMap();
            if(projectAccessValue != null)
                ServiceUtil.addToMap(hashMap, projectAccess, projectAccessValue);
            if(gscRatioValue != null)
                ServiceUtil.addToMap(hashMap, gscRatio, gscRatioValue);
            if (!UpdateActions.CONNECT.equals(var8) && !UpdateActions.CREATE.equals(var8)) {

                gscProjectSpace.modifyProjectMember(context, parentId, dataobjectId, hashMap);
            } else {
                if (projectAccessValue == null) {
                    projectAccessValue = "Project Member";
                }

                ServiceUtil.addToMap(hashMap, projectAccess, projectAccessValue);
                if (dataobjectId == null) {
                    String objName = DataelementMapAdapter.getDataelementValue(dataobject, "name");
                    Dataobject dataobject1 = com.dassault_systemes.enovia.dpm.ProjectSpace.addProjectMemberByName(context, parentId, objName, hashMap);
                    dataobjectId = dataobject1.getId();
                } else {
                    gscProjectSpace.addProjectMember(context, parentId, dataobjectId, hashMap);
                }
            }

            dataobject = getObject(context, serviceParameters, dataobjectId, (String)null, (String)null);
        }

        return dataobject;
    }
    public static Dataobject updateRelatedProjects(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject)datacollection.getDataobjects().get(0);
        String dataobjectId = dataobject.getId();
        Dataobject dataobjectParent = dataobject.getParent();
        String parentId = dataobjectParent.getId();
        UpdateActions updateAction = dataobject.getUpdateAction();
        if (!UpdateActions.CONNECT.equals(updateAction) && !UpdateActions.CREATE.equals(updateAction) && !UpdateActions.MODIFY.equals(updateAction)) {
            if (UpdateActions.DISCONNECT.equals(updateAction) || UpdateActions.DELETE.equals(updateAction)) {
                gscProjectSpace.removeRelatedProjects(context, parentId, dataobjectId);
                dataobject = null;
            }
        } else {

            HashMap hashMap = new HashMap(1);
            String gscGateResult = "gscGateResult";
            String gscGateResultValue = RelelementMapAdapter.getRelelementValue(dataobject, "gscGateResult");
            if(gscGateResultValue != null)
                ServiceUtil.addToMap(hashMap, gscGateResult, gscGateResultValue);
            gscProjectSpace.addRelatedProjects(context, parentId, dataobjectId, hashMap);
            dataobject = getObject(context, serviceParameters, dataobjectId, (String)null, (String)null);
        }

        return dataobject;
    }

    private static String getTitle(Context context, Dataobject dataobject) throws FoundationUserException {
        String title = DataelementMapAdapter.getDataelementValue(dataobject, "title");
        if (title != null) {
            title = title.trim();
        }

        UpdateActions updateAction = dataobject.getUpdateAction();
        if ((title == null || !title.isEmpty()) && (title != null || !UpdateActions.CREATE.equals(updateAction))) {
            return title;
        } else {
            String var4 = PropertyUtil.getTranslatedValue(context, "Foundation", "emxFoundation.Widget.Error.EmptyTitleNotAllowed", context.getLocale());
            throw new FoundationUserException(var4);
        }
    }
    private static String getProjectCode(Context context, Dataobject dataobject) throws FoundationUserException {
        String name = DataelementMapAdapter.getDataelementValue(dataobject, "gscProjectCode");
        if (name != null) {
            name = name.trim();
        }

        UpdateActions updateAction = dataobject.getUpdateAction();
        if ((name == null || !name.isEmpty()) && (name != null || !UpdateActions.CREATE.equals(updateAction))) {
            return name;
        } else {
            String var4 = PropertyUtil.getTranslatedValue(context, "Foundation", "emxFoundation.Widget.Error.EmptyProjectCodeNotAllowed", context.getLocale());
            throw new FoundationUserException(var4);
        }
    }
    public static Datacollection getProgramProjects(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        String programId = ((Dataobject)serviceParameters.getDatacollection().getDataobjects().get(0)).getId();
        ArgMap serviceArgs = serviceParameters.getServiceArgs();
        serviceArgs.put("programId", programId);
        String serviceName = serviceParameters.getServiceName();
        serviceName = "dpm.gscprojects";
        serviceParameters.setServiceName(serviceName);
        Servicedata var6 = ServiceBase.getData(context, serviceParameters);
        Datacollection datacollection = new Datacollection();
        datacollection.getDataobjects().addAll(var6.getData());
        return datacollection;
    }

    protected static Dataobject getObject(Context var0, ServiceParameters var1, String var2, String var3, String var4) throws FoundationException {
        String var5 = var1.getServiceName();
        var1.setServiceName(var5 + "/" + var2);
        if (var3 != null) {
            var1.getServiceArgs().put("$include", var3);
        }

        if (var4 != null) {
            var1.getServiceArgs().put("$fields", var4);
        }

        Servicedata var6 = ServiceBase.getData(var0, var1);
        Dataobject var7 = null;
        if (var6.getData().size() > 0) {
            var7 = (Dataobject)var6.getData().get(0);
        } else {
            ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var8 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(6);
            var8.add(new Select("physicalid", "physicalid", ExpressionType.BUS, (Format)null, false));
            var8.add(new Select("type", "type", ExpressionType.BUS, (Format)null, false));
            var8.add(new Select("name", "name", ExpressionType.BUS, (Format)null, false));
            var8.add(new Select("revision", "revision", ExpressionType.BUS, (Format)null, false));
            var8.add(new Select("originated", "originated", ExpressionType.BUS, (Format)null, false));
            var8.add(new Select("description", "description", ExpressionType.BUS, (Format)null, false));
            if (var7 == null && var2 != null && !var2.isEmpty()) {
                var7 = ObjectUtil.print(var0, var2, (PrintData)null, var8);
            }
        }

        return var7;
    }

    public static void getPredicitveActualFinishDate(Context var0, String[] var1) throws FoundationException {
        try {
            boolean var2 = false;
            ServiceParameters var3 = (ServiceParameters)JPOUtil.unpackArgs(var1);
            String var4 = (String)var3.getJpoArgs().get("FieldName");
            String var5 = (String)var3.getJpoArgs().get("targetFieldName");
            if (var5 == null) {
                var5 = var4;
            }

            ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var6 = new ArrayList<>();
            var6.add(new Select("calendarId", ProgramCentralConstants.SELECT_CALENDAR_ID, ExpressionType.BUS, (Format)null, false));
            var6.add(new Select("calendarDate", ProgramCentralConstants.SELECT_CALENDAR_DATE, ExpressionType.BUS, (Format)null, false));
            ObjectUtil.print(var0, var3.getDatacollection(), new PrintData(), var6, true);
            List<Dataobject> var7 = var3.getDatacollection().getDataobjects();
            if (var3.getServiceArgs().containsKey("isCustomizedEnvironment")) {
                var2 = Boolean.parseBoolean((String)var3.getServiceArgs().get("isCustomizedEnvironment"));
            }

            Iterator<Dataobject> var8 = var7.iterator();

            while(var8.hasNext()) {
                Dataobject var9 = var8.next();
                String var10 = com.dassault_systemes.enovia.dpm.ProjectSpace.computePredictiveActualFinishDate(var0, var9, var2);
                DataelementMapAdapter.setDataelementValue(var9, var5, var10);
            }

        } catch (Exception var11) {
            throw new FoundationException(var11);
        }
    }

    public static void getDeviationValue(Context var0, String[] var1) throws FoundationException {
        try {
            ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
            String var3 = (String)var2.getJpoArgs().get("FieldName");
            String var4 = (String)var2.getJpoArgs().get("targetFieldName");
            if (var4 == null) {
                var4 = var3;
            }

            ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var5 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>();
            var5.add(new Select("calendarId", ProgramCentralConstants.SELECT_CALENDAR_ID, ExpressionType.BUS, (Format)null, false));
            var5.add(new Select("calendarDate", ProgramCentralConstants.SELECT_CALENDAR_DATE, ExpressionType.BUS, (Format)null, false));
            ObjectUtil.print(var0, var2.getDatacollection(), new PrintData(), var5, true);
            List<Dataobject> var6 = var2.getDatacollection().getDataobjects();
            Iterator<Dataobject> var7 = var6.iterator();

            while(var7.hasNext()) {
                Dataobject var8 = var7.next();
                String var9 = com.dassault_systemes.enovia.dpm.ProjectSpace.getTaskDeviationValue(var0, var8);
                DataelementMapAdapter.setDataelementValue(var8, var4, var9);
            }

        } catch (Exception var10) {
            throw new FoundationException(var10);
        }
    }

    public static void getStatus(Context context, String[] args) throws FoundationException {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            int SlipThresholdYellowRed = Integer.parseInt(EnoviaResourceBundle.getProperty(context, "eServiceApplicationProgramCentral.SlipThresholdYellowRed"));
            ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
            String fieldName = (String)serviceParameters.getJpoArgs().get("FieldName");
            String targetFieldName = (String)serviceParameters.getJpoArgs().get("targetFieldName");
            if (targetFieldName == null) {
                targetFieldName = fieldName;
            }

            String var7 = "TRUE".equalsIgnoreCase((String)serviceParameters.getJpoArgs().get("useTransientFields")) ? "transient." : "";
            List<Dataobject> dataobjects = serviceParameters.getDatacollection().getDataobjects();
            Iterator<Dataobject> dataobjectIterator = dataobjects.iterator();

            while(true) {
                while(dataobjectIterator.hasNext()) {
                    Dataobject dataobject = dataobjectIterator.next();
                    String percentComplete = DataelementMapAdapter.getDataelementValue(dataobject, var7 + "percentComplete");
                    String dueDate = DataelementMapAdapter.getDataelementValue(dataobject, var7 + "dueDate");
                    String actualStartDate = DataelementMapAdapter.getDataelementValue(dataobject, var7 + "actualStartDate");
                    String actualFinishDate = DataelementMapAdapter.getDataelementValue(dataobject, var7 + "actualFinishDate");
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    today = Helper.cleanTime(today);
                    if (null != dueDate && !"".equals(dueDate)) {
                        //new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
                        // simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                        // simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                        Date dueDateDate = simpleDateFormat.parse(dueDate);
                        if ("100.0".equals(percentComplete)) {
                            Date aDate;
                            if (UIUtil.isNotNullAndNotEmpty(actualFinishDate)) {
                                aDate = simpleDateFormat.parse(actualFinishDate);
                                if (aDate.before(dueDateDate)) {
                                    DataelementMapAdapter.setDataelementValue(dataobject, targetFieldName, "completedOnTime");
                                } else {
                                    DataelementMapAdapter.setDataelementValue(dataobject, targetFieldName, "completedLate");
                                }
                            } else if (UIUtil.isNotNullAndNotEmpty(actualStartDate)) {
                                aDate = simpleDateFormat.parse(actualStartDate);
                                if (aDate.before(dueDateDate)) {
                                    DataelementMapAdapter.setDataelementValue(dataobject, targetFieldName, "completedOnTime");
                                } else {
                                    DataelementMapAdapter.setDataelementValue(dataobject, targetFieldName, "completedLate");
                                }
                            }
                        } else if (today.after(dueDateDate)) {
                            DataelementMapAdapter.setDataelementValue(dataobject, targetFieldName, "late");
                        } else if (DateUtil.computeDuration(today, dueDateDate) <= (long)SlipThresholdYellowRed) {
                            DataelementMapAdapter.setDataelementValue(dataobject, targetFieldName, "behindSchedule");
                        } else {
                            DataelementMapAdapter.setDataelementValue(dataobject, targetFieldName, "");
                        }
                    } else {
                        DataelementMapAdapter.setDataelementValue(dataobject, targetFieldName, "");
                    }
                }

                return;
            }
        } catch (ParseException | NumberFormatException | FrameworkException | FoundationException var20) {
            throw new FoundationException(var20);
        }
    }

    public static void getResourceImage(Context var0, String[] var1) throws FoundationException {
        try {
            ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
            String var3 = (String)var2.getJpoArgs().get("FieldName");
            String var4 = (String)var2.getJpoArgs().get("targetFieldName");
            if (var4 == null) {
                var4 = var3;
            }

            List<Dataobject> var5 = var2.getDatacollection().getDataobjects();
            Iterator<Dataobject> var6 = var5.iterator();

            while(var6.hasNext()) {
                Dataobject var7 = var6.next();
                String var8 = DataelementMapAdapter.getDataelementValue(var7, var3);
                String var9 = getSwymURL(var0, var8);
                DataelementMapAdapter.setDataelementValue(var7, var4, var9);
            }

        } catch (Exception var10) {
            var10.printStackTrace();
            throw new FoundationException(var10);
        }
    }

    public static Datacollections getPersonAssignments(Context var0, String[] var1) throws Exception {
        System.out.println("==============================getAssignment:START==============");
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        HttpServletRequest var3 = var2.getHttpRequest();
        ServiceUtil.checkLicenseProject(var0, var3);
        Datacollection var4 = var2.getDatacollection();
        List<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var5 = var2.getSelects();
        ExpandData var6 = new ExpandData();
        ArgMap var7 = var2.getServiceArgs();
        var6.setGetTo(false);
        var6.setGetFrom(true);
        var6.setTypePattern(ProgramCentralConstants.TYPE_PROJECT_MANAGEMENT);
        StringBuffer var8 = new StringBuffer();
        var8.append("current");
        var8.append("!='");
        var8.append(ProgramCentralConstants.STATE_PROJECT_TASK_COMPLETE);
        var8.append("'");
        var8.append(" && ");
        var8.append(ProgramCentralConstants.SELECT_PROJECT_TYPE);
        var8.append("!='");
        var8.append(ProgramCentralConstants.TYPE_EXPERIMENT);
        var8.append("'");
        var8.append(" && ");
        var8.append(ProgramCentralConstants.SELECT_PROJECT_STATE);
        var8.append("!='");
        var8.append(ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD);
        var8.append("'");
        var8.append(" && ");
        var8.append(ProgramCentralConstants.SELECT_PROJECT_STATE);
        var8.append("!='");
        var8.append(ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL);
        var8.append("'");
        var6.setObjectWhere(var8.toString());
        Datacollections var9 = ObjectUtil.expand(var0, var4, var6, var5);
        Map<String, StringList> var10 = ProgramCentralUtil.getDerivativeType(var0, new String[]{ProgramCentralConstants.TYPE_PROJECT_MANAGEMENT});
        StringList var11 = var10.get(ProgramCentralConstants.TYPE_PROJECT_MANAGEMENT);
        Dataobject var12 = new Dataobject();
        var12.setId("typeIcon");

        int var13;
        for(var13 = 0; var13 < var11.size(); ++var13) {
            String var14 = (String)var11.get(var13);
            String var15 = var2.getHttpBaseUrl();

            try {
                String var16 = var15 + "common/images/" + EnoviaResourceBundle.getProperty(var0, "emxFramework.smallIcon.type_" + var14);
                DataelementMapAdapter.setDataelementValue(var12, var14, var16);
            } catch (FrameworkException var17) {
            }
        }

        DataelementMapAdapter.setDataelementValue(var12, "contextUser", var0.getUser());

        for(var13 = 0; var13 < var9.getDatacollections().size(); ++var13) {
            Datacollection var18 = (Datacollection)var9.getDatacollections().get(var13);
            var18.getDataobjects().add(0, var12);
        }

        System.out.println("==============================getAssignment:END==============");
        return var9;
    }

    public static Datacollections getProjectIssues(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        HttpServletRequest var3 = var2.getHttpRequest();
        ServiceUtil.checkLicenseProject(var0, var3);
        Datacollection var4 = var2.getDatacollection();
        List<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var5 = var2.getSelects();
        String var6 = var2.getFilters().getId();
        ArgMap var7 = var2.getServiceArgs();
        String var8 = (String)var7.get("state");
        String var9 = (String)var7.get("owned");
        String var10 = (String)var7.get("assigned");
        boolean var11 = false;
        boolean var12 = false;
        if (var9 != null && !var9.isEmpty()) {
            var11 = Boolean.parseBoolean(var9);
        }

        if (var10 != null && !var10.isEmpty()) {
            var12 = Boolean.parseBoolean(var10);
        }

        Datacollections var13 = gscProjectSpace.getProjectIssues(var0, var4, var6, var11, var12, var5, var8);
        return var13;
    }

    private static Datacollections getProjectRiskManagemnt(Context var0, gscProjectService.Source var1, String[] var2) throws FoundationException {
        ServiceParameters var3 = (ServiceParameters)JPOUtil.unpackArgs(var2);
        HttpServletRequest var4 = var3.getHttpRequest();
        ServiceUtil.checkLicenseProject(var0, var4);
        Datacollection var5 = var3.getDatacollection();
        List<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var6 = var3.getSelects();
        String var7 = var3.getFilters().getId();
        ArgMap var8 = var3.getServiceArgs();
        String var9 = (String)var8.get("state");
        String var10 = (String)var8.get("owned");
        String var11 = (String)var8.get("assigned");
        boolean var12 = false;
        boolean var13 = false;
        if (var10 != null && !var10.isEmpty()) {
            var12 = Boolean.parseBoolean(var10);
        }

        if (var11 != null && !var11.isEmpty()) {
            var13 = Boolean.parseBoolean(var11);
        }

        Datacollections var14 = null;
        switch (var1) {
            case OPP_PRJ:
                var14 = gscProjectSpace.getProjectOpportunities(var0, var5, var7, var12, var13, var6, var9);
                break;
            case RISK_PRJ:
                var14 = gscProjectSpace.getProjectRisks(var0, var5, var7, var12, var13, var6, var9);
                break;
            case OPP_PAL:
                var14 = gscProjectSpace.getProjectObjectsFromPAL(var0, PropertyUtil.getSchemaName(var0, "type_Opportunity"), var5, var7, var12, var13, var6, var9);
                break;
            case RISK_PAL:
                var14 = gscProjectSpace.getProjectObjectsFromPAL(var0, PropertyUtil.getSchemaName(var0, "type_Risk"), var5, var7, var12, var13, var6, var9);
        }

        return var14;
    }

    public static Datacollections getProjectOpportunities(Context var0, String[] var1) throws FoundationException {
        Datacollections var2 = getProjectRiskManagemnt(var0, gscProjectService.Source.OPP_PRJ, var1);
        return var2;
    }

    public static Datacollections getProjectOpportunitiesFromPAL(Context var0, String[] var1) throws FoundationException {
        Datacollections var2 = getProjectRiskManagemnt(var0, gscProjectService.Source.OPP_PAL, var1);
        return var2;
    }

    public static Datacollections getProjectRisks(Context var0, String[] var1) throws FoundationException {
        Datacollections var2 = getProjectRiskManagemnt(var0, gscProjectService.Source.RISK_PRJ, var1);
        return var2;
    }

    private static StringList tokenizeResourceName(String var0) {
        new StringList();
        var0 = var0.trim();
        StringList var1 = FrameworkUtil.split(var0, " ");
        return var1;
    }

    private static String getSwymURL(Context var0, String var1) throws FrameworkException {
        String var2 = "";
        String var3 = var0.getTenant().toLowerCase();
        if (var1 != null && !var1.isEmpty()) {
            String var4 = "";
            String var5 = com.matrixone.apps.domain.util.PropertyUtil.getEnvironmentProperty(var0, "SWYM_URL");
            if (ProgramCentralUtil.isNotNullString(var5)) {
                var4 = var3 + var5;
                if (!var4.startsWith("https://")) {
                    var4 = "https://" + var4;
                }

                if (!var4.endsWith("/")) {
                    var4 = var4 + "/";
                }

                String var6 = "/api/user/getpicture/login/";
                String var7 = "/format/mini";
                var2 = var4 + var6 + var1 + var7;
            } else {
                var2 = "../../common/images/iconSmallPerson.png";
            }
        }

        return var2;
    }

    private static Datacollection loadResources(Context var0, String var1) {
        System.out.println("=============Load Resource : START===============");
        Datacollection var2 = new Datacollection();

        try {
            String var3 = "";
            String var4 = "";
            String var5 = "";
            String var6 = "";
            StringList var7 = new StringList();
            var7.add("physicalid");
            var7.add("name");
            var7.add(ProgramCentralConstants.SELECT_ATTRIBUTE_FIRSTNAME);
            var7.add(ProgramCentralConstants.SELECT_ATTRIBUTE_LASTNAME);
            StringBuffer var8 = new StringBuffer();
            StringList var9 = tokenizeResourceName(var1);
            int var10 = var9.size();
            if (var10 == 1) {
                var3 = var4 = var6 = (String)var9.get(0);
                var8.append("(");
                var8.append("name");
                var8.append(" ~~ '");
                var8.append(var3);
                var8.append("*'");
                var8.append(" || ");
                var8.append(ProgramCentralConstants.SELECT_ATTRIBUTE_FIRSTNAME);
                var8.append(" ~~ '");
                var8.append(var4);
                var8.append("*'");
                var8.append(" || ");
                var8.append(ProgramCentralConstants.SELECT_ATTRIBUTE_LASTNAME);
                var8.append(" ~~ '");
                var8.append(var6);
                var8.append("*'");
                var8.append(") && ");
                var8.append("current");
                var8.append(" !~~ '");
                var8.append("Inactive'");
            } else if (var10 == 2) {
                var4 = (String)var9.get(0);
                var6 = (String)var9.get(1);
                var8.append("(");
                var8.append(ProgramCentralConstants.SELECT_ATTRIBUTE_FIRSTNAME);
                var8.append(" ~~ '");
                var8.append(var4);
                var8.append("'");
                var8.append(" && ");
                var8.append(ProgramCentralConstants.SELECT_ATTRIBUTE_LASTNAME);
                var8.append(" ~~ '");
                var8.append(var6);
                var8.append("*'");
                var8.append(") && ");
                var8.append("current");
                var8.append(" !~~ '");
                var8.append("Inactive'");
            }

            MapList var11 = DomainObject.findObjects(var0, ProgramCentralConstants.TYPE_PERSON, (String)null, "-", (String)null, "*", var8.toString(), (String)null, true, var7, (short)0, (String)null, (String)null, new StringList());
            Iterator var12 = var11.iterator();

            while(var12.hasNext()) {
                Map var13 = (Map)var12.next();
                String var14 = (String)var13.get(DomainConstants.SELECT_ATTRIBUTE_FIRSTNAME);
                String var15 = (String)var13.get(DomainConstants.SELECT_ATTRIBUTE_LASTNAME);
                String var16 = (String)var13.get("physicalid");
                String var17 = (String)var13.get("name");
                Dataobject var18 = new Dataobject();
                var18.setId(var16);
                DataelementMapAdapter.setDataelementValue(var18, "Id", var16);
                DataelementMapAdapter.setDataelementValue(var18, "Name", var14 + ' ' + var15);
                DataelementMapAdapter.setDataelementValue(var18, "Username", var17);
                DataelementMapAdapter.setDataelementValue(var18, "icon", getSwymURL(var0, var17));
                var2.getDataobjects().add(var18);
            }

            System.out.println("=============Load Resource : END===============");
            System.out.println("Added res::" + var2.getDataobjects().size());
            return var2;
        } catch (FrameworkException var19) {
            var19.printStackTrace();
            return var2;
        }
    }

    public static void getFullName(Context var0, String[] var1) throws FoundationException {
        try {
            ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
            String var3 = (String)var2.getJpoArgs().get("FieldName");
            String var4 = (String)var2.getJpoArgs().get("targetFieldName");
            if (var4 == null) {
                var4 = var3;
            }

            List<Dataobject> var5 = var2.getDatacollection().getDataobjects();
            Iterator<Dataobject> var6 = var5.iterator();

            while(var6.hasNext()) {
                Dataobject var7 = var6.next();
                String var8 = DataelementMapAdapter.getDataelementValue(var7, var3);
                if (var8 != null && !var8.isEmpty()) {
                    var8 = PersonUtil.getFullName(var0, var8);
                    DataelementMapAdapter.setDataelementValue(var7, var4, var8);
                }
            }

        } catch (FrameworkException var9) {
            throw new FoundationException(var9);
        }
    }

    public static Datacollections getProjectRisksFromPAL(Context var0, String[] var1) throws FoundationException {
        Datacollections var2 = getProjectRiskManagemnt(var0, gscProjectService.Source.RISK_PAL, var1);
        return var2;
    }

    public static void convertScheduleFromToConstraint(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        String var3 = (String)var2.getJpoArgs().get("FieldName");
        String var4 = (String)var2.getJpoArgs().get("targetFieldName");
        if (var4 == null) {
            var4 = var3;
        }

        List<Dataobject> var5 = var2.getDatacollection().getDataobjects();
        Iterator<Dataobject> var6 = var5.iterator();

        while(var6.hasNext()) {
            Dataobject var7 = var6.next();
            String var8 = DataelementMapAdapter.getDataelementValue(var7, var3);
            if (var8 != null && !var8.isEmpty()) {
                if ("Project Finish Date".equals(var8)) {
                    DataelementMapAdapter.setDataelementValue(var7, var4, "As Late As Possible");
                } else {
                    DataelementMapAdapter.setDataelementValue(var7, var4, "As Soon As Possible");
                }
            }
        }

    }

    public static void getGanttColumns(Context var0, String[] var1) throws FoundationException {
        try {
            ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
            Datacollection var3 = var2.getDatacollection();
            String var4 = (String)var2.getJpoArgs().get("FieldName");
            String var5 = (String)var2.getJpoArgs().get("targetFieldName");
            if (var5 == null) {
                var5 = var4;
            }

            List<Dataobject> var6 = var2.getDatacollection().getDataobjects();
            Iterator<Dataobject> var7 = var6.iterator();

            while(var7.hasNext()) {
                Dataobject var8 = var7.next();
                String var9 = "";
                String var10 = "";
                String var11 = "";
                if (var2.getServiceArgs().containsKey("viewId")) {
                    var9 = (String)var2.getServiceArgs().get("viewId");
                }

                if (var2.getServiceArgs().containsKey("referenceId")) {
                    var10 = (String)var2.getServiceArgs().get("referenceId");
                }

                if (var2.getServiceArgs().containsKey("clientTimezone")) {
                    var11 = (String)var2.getServiceArgs().get("clientTimezone");
                }

                HashMap<String, String> var12 = new HashMap<>();
                Dataobject var13 = (Dataobject)var3.getDataobjects().get(0);
                var12.put("objectId", var13.getId());
                var12.put("viewId", var9);
                var12.put("referenceId", var10);
                var12.put("clientTimezone", var11);
                String[] var14 = JPO.packArgs(var12);
                JsonArray var15 = (JsonArray)JPO.invoke(var0, "emxGantt", (String[])null, "getColumns", var14, JsonArray.class);
                DataelementMapAdapter.setDataelementValue(var8, var5, var15.toString());
            }

        } catch (Exception var16) {
            var16.printStackTrace();
            throw new FoundationException(var16);
        }
    }

    public static void getSequenceOrder(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        String var3 = (String)var2.getJpoArgs().get("FieldName");
        String var4 = (String)var2.getJpoArgs().get("palIdField");
        String var5 = (String)var2.getJpoArgs().get("taskWBS");
        String var6 = (String)var2.getJpoArgs().get("isDependencyObject");
        HashMap var7 = new HashMap();
        HashMap<String, Dataobject> var8 = new HashMap<>();
        List var9 = var2.getDatacollection().getDataobjects();
        Iterator var10 = var9.iterator();

        while(true) {
            String var13;
            while(var10.hasNext()) {
                Dataobject var11 = (Dataobject)var10.next();
                String var12 = DataelementMapAdapter.getDataelementValue(var11, var4);
                if (var12 != null && !var12.isEmpty()) {
                    var13 = DataelementMapAdapter.getDataelementValue(var11, "taskProjectId");
                    var7.put(var12, var13);
                } else {
                    var13 = var11.getRelId();
                    if (var13 != null || !var13.isEmpty()) {
                        var12 = MqlUtil.mqlCommand(var0, "print connection $1 select $2 $3 $4 dump $5", new String[]{var13, "from.to[Project Access List].from.id", "from.to[Project Access Key].from.id", "from.physicalid", "|"});
                        String[] var14 = var12 != null ? var12.split("\\|") : null;
                        if (var14 != null && var14.length >= 2 && !var7.containsKey(var14[0])) {
                            var7.put(var14[0], var14[1]);
                        }
                    }
                }
            }

            Object var18 = (Map)ContextUtil.getAttribute(var0, "keyPalCacheMap");
            Set var19;
            Iterator var20;
            if (var18 == null) {
                var18 = new HashMap();
                ContextUtil.setAttribute(var0, "keyPalCacheMap", var18);
            } else {
                var19 = ((Map)var18).keySet();
                var20 = var19.iterator();

                while(var20.hasNext()) {
                    var13 = (String)var20.next();
                    var8.putAll((Map<String, Dataobject>)((Map)var18).get(var13));
                }
            }

            var19 = var7.keySet();
            var20 = var19.iterator();

            String var22;
            while(var20.hasNext()) {
                var13 = (String)var20.next();

                try {
                    if (!((Map)var18).containsKey(var13)) {
                        var22 = (String)var7.get(var13);
                        ProjectSequence var15 = new ProjectSequence(var0, var13);
                        Map<String, Dataobject> var16 = var15.getSequenceData(var0);
                        if (ProgramCentralUtil.isNotNullString(var22)) {
                            var16.remove(var22);
                        }

                        ((Map)var18).put(var13, var8);
                        var8.putAll(var16);
                    } else {
                        var8.putAll((Map<String, Dataobject>)((Map)var18).get(var13));
                    }
                } catch (Exception var17) {
                    var17.printStackTrace();
                    throw new FoundationException(var17);
                }
            }

            var20 = var9.iterator();

            while(var20.hasNext()) {
                Dataobject var21 = (Dataobject)var20.next();
                var22 = var21.getId();
                if ("true".equalsIgnoreCase(var6)) {
                    var22 = RelelementMapAdapter.getRelelementValue(var21, "From");
                }

                Dataobject var23 = var8.get(var22);
                if (var23 != null) {
                    RelelementMapAdapter.setRelelementValue(var21, var3, (String)var23.getDataelements().get("seqId"));
                    RelelementMapAdapter.setRelelementValue(var21, var5, (String)var23.getDataelements().get("wbsId"));
                }
            }

            return;
        }
    }

    public static void getFloatValues(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        String var3 = (String)var2.getJpoArgs().get("FieldName");
        String var4 = (String)var2.getJpoArgs().get("palIdField");
        String var5 = (String)var2.getJpoArgs().get("isTotalFloat");
        String var6 = (String)var2.getJpoArgs().get("isOverallCrit");
        HashMap var7 = new HashMap();
        HashMap var8 = new HashMap();
        List var9 = var2.getDatacollection().getDataobjects();
        Iterator var10 = var9.iterator();

        String var13;
        while(var10.hasNext()) {
            Dataobject var11 = (Dataobject)var10.next();
            String var12 = DataelementMapAdapter.getDataelementValue(var11, var4);
            if (var12 != null && !var12.isEmpty()) {
                var13 = DataelementMapAdapter.getDataelementValue(var11, "taskProjectId");
                var7.put(var12, var13);
            }
        }

        Object var19 = (Map)ContextUtil.getAttribute(var0, "keyPalFloatCacheMap");
        Set var20;
        Iterator var21;
        if (var19 == null) {
            var19 = new HashMap();
            ContextUtil.setAttribute(var0, "keyPalFloatCacheMap", var19);
        } else {
            var20 = ((Map)var19).keySet();
            var21 = var20.iterator();

            while(var21.hasNext()) {
                var13 = (String)var21.next();
                var8.putAll((Map)((Map)var19).get(var13));
            }
        }

        var20 = var7.keySet();
        var21 = var20.iterator();

        String var14;
        while(var21.hasNext()) {
            var13 = (String)var21.next();

            try {
                if (!((Map)var19).containsKey(var13)) {
                    var14 = (String)var7.get(var13);
                    Map var15 = com.dassault_systemes.enovia.dpm.ProjectSpace.getProjectFloatFromPAL(var0, var13);
                    if (ProgramCentralUtil.isNotNullString(var14)) {
                        var15.remove(var14);
                    }

                    ((Map)var19).put(var13, var8);
                    var8.putAll(var15);
                } else {
                    var8.putAll((Map)((Map)var19).get(var13));
                }
            } catch (Exception var18) {
                var18.printStackTrace();
                throw new FoundationException(var18);
            }
        }

        var21 = var9.iterator();

        while(var21.hasNext()) {
            Dataobject var22 = (Dataobject)var21.next();
            var14 = var22.getId();
            StringBuilder var23 = new StringBuilder();
            Object var16 = var8.get(var14);
            if (var16 != null) {
                StringList var17 = FrameworkUtil.split((String)var16, "|");
                if ("True".equalsIgnoreCase(var5)) {
                    var23.append((String)var17.get(0));
                } else if ("True".equalsIgnoreCase(var6)) {
                    var23.append((String)var17.get(2));
                } else {
                    var23.append((String)var17.get(1));
                }
            }

            if (var16 != null) {
                DataelementMapAdapter.setDataelementValue(var22, var3, var23.toString());
            } else {
                DataelementMapAdapter.setDataelementValue(var22, var3, "0.0");
            }
        }

    }

    public static Datacollections getProjectFloatValues(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        HttpServletRequest var3 = var2.getHttpRequest();
        ServiceUtil.checkLicenseProject(var0, var3);
        String var4 = (String)var2.getJpoArgs().get("palIdField");
        Datacollections var5 = new Datacollections();
        Datacollection var6 = var2.getDatacollection();
        ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var7 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(2);
        var7.add(new Select(var4, "to[Project Access List].from.id", ExpressionType.BUS, (Format)null, false));
        var7.add(new Select("objId", "id", ExpressionType.BUS, (Format)null, false));
        Dataobject var8 = (Dataobject)var6.getDataobjects().get(0);
        ObjectUtil.print(var0, var8, (PrintData)null, var7, true);
        String var9 = DataelementMapAdapter.getDataelementValue(var8, "objId");

        try {
            TaskDateRollup.rolloutProject(var0, new StringList(var9), true, true);
        } catch (FrameworkException var28) {
            var28.printStackTrace();
        }

        String var10 = "";
        String var11 = "";
        if (var2.getServiceArgs().containsKey("clientTimezone")) {
            var10 = (String)var2.getServiceArgs().get("clientTimezone");
        }

        String var12 = DataelementMapAdapter.getDataelementValue(var8, var4);
        Datacollection var13 = new Datacollection();
        Map var14 = com.dassault_systemes.enovia.dpm.ProjectSpace.getProjectFloatFromPAL(var0, var12);
        Iterator var15 = var14.entrySet().iterator();

        while(var15.hasNext()) {
            Map.Entry var16 = (Map.Entry)var15.next();
            String var17 = (String)var16.getKey();
            Object var18 = var16.getValue();
            Dataobject var19 = new Dataobject();
            var19.setId(var17);
            if ("Last Modified".equalsIgnoreCase(var17)) {
                String var20 = var0.getLocale().getLanguage();

                try {
                    String var21 = EnoviaResourceBundle.getProperty(var0, "ProgramCentral", "emxProgramCentral.Schedule.Float", var20);
                    String var22 = EnoviaResourceBundle.getProperty(var0, "ProgramCentral", "emxProgramCentral.Schedule.Float.Updated", var20);
                    if (ProgramCentralUtil.isNotNullString(var10)) {
                        double var23 = Task.parseToDouble(var10);
                        int var25 = eMatrixDateFormat.getEMatrixDisplayDateFormat();
                        Locale var26 = var0.getLocale();
                        var11 = eMatrixDateFormat.getFormattedDisplayDateTime(var16.getValue().toString(), true, var25, var23, var26);
                    }

                    String var31 = var21 + " ( " + var22 + ":" + var11 + " )";
                    DataelementMapAdapter.setDataelementValue(var19, "lastModified", var31);
                    var13.getDataobjects().add(var19);
                } catch (MatrixException var27) {
                    var27.printStackTrace();
                }
            } else {
                StringBuilder var32 = new StringBuilder();
                StringBuilder var29 = new StringBuilder();
                if (var18 != null) {
                    StringList var30 = FrameworkUtil.split((String)var18, "|");
                    var32.append((String)var30.get(0));
                    var29.append((String)var30.get(1));
                } else {
                    var32.append("0.0");
                    var29.append("0.0");
                }

                DataelementMapAdapter.setDataelementValue(var19, "totalFloat", var32.toString());
                DataelementMapAdapter.setDataelementValue(var19, "freeFloat", var29.toString());
                var13.getDataobjects().add(var19);
            }
        }

        var5.getDatacollections().add(var13);
        return var5;
    }

    public static Datacollections getProjectForecastValues(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        HttpServletRequest var3 = var2.getHttpRequest();
        Datacollection var4 = new Datacollection();
        ServiceUtil.checkLicenseProject(var0, var3);
        String var5 = (String)var2.getJpoArgs().get("palIdField");
        Datacollections var6 = new Datacollections();
        String var7 = "";
        String var8 = "";
        if (var2.getServiceArgs().containsKey("clientTimezone")) {
            var7 = (String)var2.getServiceArgs().get("clientTimezone");
        }

        Datacollection var9 = var2.getDatacollection();
        ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var10 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(2);
        var10.add(new Select(var5, "to[Project Access List].from.id", ExpressionType.BUS, (Format)null, false));
        var10.add(new Select("objId", "id", ExpressionType.BUS, (Format)null, false));
        var10.add(new Select("physicalid", "physicalid", ExpressionType.BUS, (Format)null, false));
        Dataobject var11 = (Dataobject)var9.getDataobjects().get(0);
        ObjectUtil.print(var0, var11, (PrintData)null, var10, true);
        String var12 = DataelementMapAdapter.getDataelementValue(var11, "objId");
        String var13 = DataelementMapAdapter.getDataelementValue(var11, "physicalid");
        Object var14 = new HashMap();

        try {
            var14 = TaskDateRollup.rolloutProject(var0, new StringList(var12), true, false, true);
        } catch (FrameworkException var36) {
            var36.printStackTrace();
        }

        if (!((Map)var14).isEmpty()) {
            HashMap var15 = (HashMap)((Map)var14).get("common.Task_updatedDates");
            Iterator var16 = var15.entrySet().iterator();

            Dataobject var20;
            String var21;
            String var24;
            while(var16.hasNext()) {
                Map.Entry var17 = (Map.Entry)var16.next();
                String var18 = (String)var17.getKey();
                HashMap var19 = (HashMap)var17.getValue();
                var20 = new Dataobject();
                var21 = var19.get("physicalid").toString();
                var20.setId(var21);
                Date var22 = (Date)var19.get("attribute[Forecast Start Date]");
                Date var23 = (Date)var19.get("attribute[Forecast Finish Date]");
                var24 = "";
                if (var22 != null && var23 != null) {
                    var24 = var19.get("attribute[Forecast Duration]").toString();
                }

                Date var25 = (Date)var19.get("attribute[Task Estimated Start Date]");
                Date var26 = (Date)var19.get("attribute[Task Estimated Finish Date]");
                String var27 = var19.get("attribute[Task Estimated Duration]").toString();
                String var28 = " " + var19.get("attribute[Task Estimated Duration].inputunit").toString();
                Calendar var29 = Calendar.getInstance(var0.getLocale());
                SimpleDateFormat var30 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
                String var31 = "";
                String var32 = "";
                if (var22 != null && var23 != null) {
                    var29.setTime(var22);
                    var22 = var29.getTime();
                    var31 = var30.format(var22);
                    var29.setTime(var23);
                    var23 = var29.getTime();
                    var32 = var30.format(var23);
                }

                var29.setTime(var25);
                var25 = var29.getTime();
                String var33 = var30.format(var25);
                var29.setTime(var26);
                var26 = var29.getTime();
                String var34 = var30.format(var26);
                if (var22 != null && var23 != null) {
                    DataelementMapAdapter.setDataelementValue(var20, "forecastStartDate", var31);
                    DataelementMapAdapter.setDataelementValue(var20, "forecastFinishDate", var32);
                    DataelementMapAdapter.setDataelementValue(var20, "forecastDuration", var24);
                }

                DataelementMapAdapter.setDataelementValue(var20, "EstStartDate", var33);
                DataelementMapAdapter.setDataelementValue(var20, "EstFinishDate", var34);
                DataelementMapAdapter.setDataelementValue(var20, "EstDuration", var27 + var28);
                var4.getDataobjects().add(var20);
            }

            ArrayList<Select> var37 = new ArrayList<>(1);
            var37.add(new Select(ProgramCentralConstants.SELECT_ATTRIBUTE_FORECAST_CALCULATED_ON, ProgramCentralConstants.SELECT_ATTRIBUTE_FORECAST_CALCULATED_ON, ExpressionType.BUS, (Format)null, false));
            Dataobject var38 = com.gsc.enovia.dpm.gscProjectSpace.getProjectInfo(var0, (String)var13, var37);
            String var39 = DataelementMapAdapter.getDataelementValue(var38, ProgramCentralConstants.SELECT_ATTRIBUTE_FORECAST_CALCULATED_ON);
            var20 = new Dataobject();
            var20.setId("Last Modified");
            var21 = var0.getLocale().getLanguage();

            try {
                String var40 = EnoviaResourceBundle.getProperty(var0, "ProgramCentral", "emxProgramCentral.Common.Forecast", var21);
                String var41 = EnoviaResourceBundle.getProperty(var0, "ProgramCentral", "emxProgramCentral.Schedule.Float.Updated", var21);
                if (ProgramCentralUtil.isNotNullString(var7)) {
                    double var42 = Task.parseToDouble(var7);
                    int var43 = eMatrixDateFormat.getEMatrixDisplayDateFormat();
                    Locale var44 = var0.getLocale();
                    var8 = eMatrixDateFormat.getFormattedDisplayDateTime(var39, true, var43, var42, var44);
                }

                var24 = var40 + " ( " + var41 + ":" + var8 + " )";
                DataelementMapAdapter.setDataelementValue(var20, "forecastUpdatedOn", var24);
            } catch (MatrixException var35) {
                var35.printStackTrace();
            }

            var4.getDataobjects().add(var20);
        }

        var6.getDatacollections().add(var4);
        return var6;
    }

    public static Datacollections getDerivativeTypes(Context var0, String[] var1) throws FoundationException {
        Datacollections var2 = new Datacollections();
        ServiceParameters var3 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        HttpServletRequest var4 = var3.getHttpRequest();
        ServiceUtil.checkLicenseProject(var0, var4);
        Datacollection var5 = var3.getDatacollection();
        Dataobject var6 = (Dataobject)var5.getDataobjects().get(0);
        ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var7 = new ArrayList<>(5);
        var7.add(new Select("objType", "type", ExpressionType.BUS, (Format)null, false));
        var7.add(new Select("kindOfProjectSpace", "type.kindof[Project Space]", ExpressionType.BUS, (Format)null, false));
        var7.add(new Select("kindOfExperiment", "type.kindof[Experiment]", ExpressionType.BUS, (Format)null, false));
        var7.add(new Select("kindOfProjectBaseline", "type.kindof[Project Baseline]", ExpressionType.BUS, (Format)null, false));
        var7.add(new Select("kindOfProjectConcept", "type.kindof[Project Concept]", ExpressionType.BUS, (Format)null, false));
        var7.add(new Select("kindOfProjectTemplate", "type.kindof[Project Template]", ExpressionType.BUS, (Format)null, false));
        ObjectUtil.print(var0, var6, (PrintData)null, var7, true);
        String var8 = DataelementMapAdapter.getDataelementValue(var6, "objType");
        String var9 = DataelementMapAdapter.getDataelementValue(var6, "kindOfProjectSpace");
        String var10 = DataelementMapAdapter.getDataelementValue(var6, "kindOfExperiment");
        String var11 = DataelementMapAdapter.getDataelementValue(var6, "kindOfProjectBaseline");
        String var12 = DataelementMapAdapter.getDataelementValue(var6, "kindOfProjectConcept");
        String var13 = DataelementMapAdapter.getDataelementValue(var6, "kindOfProjectTemplate");
        Dataobject var14 = new Dataobject();
        var8 = var8.replaceAll(" ", "");
        if ("True".equalsIgnoreCase(var10)) {
            DataelementMapAdapter.setDataelementValue(var14, var8, "Experiment");
        } else if ("True".equalsIgnoreCase(var11)) {
            DataelementMapAdapter.setDataelementValue(var14, var8, "ProjectBaseline");
        } else if ("True".equalsIgnoreCase(var9)) {
            DataelementMapAdapter.setDataelementValue(var14, var8, "ProjectSpace");
            DataelementMapAdapter.setDataelementValue(var14, "ProjectConcept", "ProjectConcept");
        } else if ("True".equalsIgnoreCase(var12)) {
            DataelementMapAdapter.setDataelementValue(var14, var8, "ProjectConcept");
            DataelementMapAdapter.setDataelementValue(var14, "ProjectSpace", "ProjectSpace");
        } else if ("True".equalsIgnoreCase(var13)) {
            DataelementMapAdapter.setDataelementValue(var14, var8, "ProjectTemplate");
        }

        List<String> var15 = ObjectUtil.getTypeDerivatives(var0, ProgramCentralConstants.TYPE_GATE, true);
        List<String> var16 = ObjectUtil.getTypeDerivatives(var0, ProgramCentralConstants.TYPE_MILESTONE, true);
        List<String> var17 = ObjectUtil.getTypeDerivatives(var0, ProgramCentralConstants.TYPE_PHASE, true);
        List<String> var18 = ObjectUtil.getTypeDerivatives(var0, ProgramCentralConstants.TYPE_TASK, true);
        ArrayList<List<String>> var19 = new ArrayList<List<String>>();
        var19.add(var15);
        var19.add(var16);
        var19.add(var17);
        var19.add(var18);

        for(int var20 = 0; var20 < var19.size(); ++var20) {
            List<String> var21 = var19.get(var20);

            for(int var22 = 0; var22 < var21.size(); ++var22) {
                String var23 = var21.get(var22);
                var23 = var23.replaceAll(" ", "");
                if (var21.contains("Gate")) {
                    DataelementMapAdapter.setDataelementValue(var14, var23, "Gate");
                } else if (var21.contains("Milestone")) {
                    DataelementMapAdapter.setDataelementValue(var14, var23, "Milestone");
                } else if (var21.contains("Phase")) {
                    DataelementMapAdapter.setDataelementValue(var14, var23, "Phase");
                } else {
                    DataelementMapAdapter.setDataelementValue(var14, var23, "Task");
                }
            }
        }

        Datacollection var24 = new Datacollection();
        var24.getDataobjects().add(var14);
        var2.getDatacollections().add(var24);
        return var2;
    }

    public static Datacollections getTypeAndPolicy(Context var0, String[] var1) throws Exception {
        Datacollections var2 = new Datacollections();
        ServiceParameters var3 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        HttpServletRequest var4 = var3.getHttpRequest();
        Datacollection var5 = var3.getDatacollection();
        Dataobject var6 = (Dataobject)var5.getDataobjects().get(0);
        String var7 = var6.getId();
        ServiceUtil.checkLicenseProject(var0, var4);
        Dataobject var8 = new Dataobject();
        StringList var9 = ProgramCentralUtil.getTaskSubTypesList(var0);
        List<String> var10 = ObjectUtil.getTypeDerivatives(var0, ProgramCentralConstants.TYPE_GATE, true);
        List<String> var11 = ObjectUtil.getTypeDerivatives(var0, ProgramCentralConstants.TYPE_MILESTONE, true);
        StringList var12 = new StringList();
        var12.addAll(var10);
        var12.addAll(var11);
        String var13 = var0.getLocale().getLanguage();
        String var14 = "";

        for(int var15 = 0; var15 < var9.size(); ++var15) {
            StringList var16 = new StringList();
            StringList var17 = new StringList();
            String var18 = (String)var9.get(var15);
            var14 = com.dassault_systemes.enovia.tskv2.Task.getDefaultPolicy(var0, var18);
            String var19 = EnoviaResourceBundle.getAdminI18NString(var0, "Policy", var14, var13);
            List<String> var20 = ProgramCentralUtil.getPolicies(var0, var18, false);
            int var21 = 0;

            for(int var22 = var20.size(); var21 < var22; ++var21) {
                String var23 = var20.get(var21);
                if (!ProgramCentralConstants.POLICY_PROJECT_TASK.equalsIgnoreCase(var23) || !var10.contains(var18) && !var11.contains(var18)) {
                    String var24 = EnoviaResourceBundle.getAdminI18NString(var0, "Policy", var23, var13);
                    var16.add(var23);
                    var17.add(var24);
                }
            }

            if (var16.contains(var14)) {
                var16.remove(var14);
                var17.remove(var19);
                var16.add(0, var14);
                var17.add(0, var19);
            }

            DataelementMapAdapter.setDataelementValue(var8, var18, var16.toString().concat("#").concat(var17.toString()));
        }

        DataelementMapAdapter.setDataelementValue(var8, "AllTypes", var9.toString());
        DataelementMapAdapter.setDataelementValue(var8, "gateMilestoneTypes", var12.toString());
        String var25 = EnoviaResourceBundle.getProperty(var0, "emxFramework.Javascript.NameBadChars");
        DataelementMapAdapter.setDataelementValue(var8, "InvalidChars", var25);
        String var26 = MqlUtil.mqlCommand(var0, "print bus $1 select $2 dump", new String[]{var7, "attribute[Schedule].value"});
        DataelementMapAdapter.setDataelementValue(var8, "schedule", var26);
        Datacollection var27 = new Datacollection();
        var27.getDataobjects().add(var8);
        var2.getDatacollections().add(var27);
        return var2;
    }

    private static void handleFormatPattern(Context var0, Dataobject var1, Map<String, String> var2, UpdateActions var3) throws FoundationException {
        String var4 = DataelementMapAdapter.getDataelementValue(var1, "pattern");
        String var5 = "";
        if (var4 != null && !var4.isEmpty()) {
            String var6 = var1.getId();
            String var7 = var2.get("FormatPattern");
            if (UpdateActions.MODIFY.equals(var3)) {
                ArrayList<Select> var8 = new ArrayList<Select>(1);
                if (var7 != null || !var7.isEmpty()) {
                    var8.add(new Select("hasInterface", "interface[DPMGanttChart]", ExpressionType.BUS, (Format)null, false));
                }

                Dataobject var9 = com.gsc.enovia.dpm.gscProjectSpace.getProjectInfo(var0, (String)var6, var8);
                var5 = DataelementMapAdapter.getDataelementValue(var9, "hasInterface");
            }

            if ("false".equalsIgnoreCase(var5)) {
                MqlUtil.mqlCommand(var0, "modify bus $1 add $2 $3", new String[]{var6, "interface", "DPMGanttChart"});
                var2.put("FormatPattern", var7);
            }
        }

    }

    public static void createProjectInBackground(Context var0, String[] var1) throws Exception {
        gscProjectService var2 = new gscProjectService();
        ServiceParameters var3 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Object[] var4 = new Object[]{var0, var3};
        ContextUtil.submitPostTransactionJob(var0, var2, "createProjectInBackgroundProcess", var4);
        ContextUtil.processPostTransactionJob(var0, true);
    }

    public static void createProjectInBackgroundProcess(Context var0, ServiceParameters var1) throws Exception {
        String var2 = var0.getUser();
        boolean var3 = false;
        String var4 = "";
        boolean var15 = false;

        try {
            var15 = true;
            List<Dataobject> var5 = var1.getDatacollection().getDataobjects();
            var1.setHttpRequest((HttpServletRequest)null);
            Dataobject var6 = var5.get(0);
            var1.setServiceName("dpm.gscprojects");
            ArgMap var7 = var1.getServiceArgs();
            var7.put("rollup", "live");
            Servicedata var8 = new Servicedata();
            var8.getData().add(var6);
            Servicedata var9 = ServiceBase.saveData(var0, var8, var1);
            Dataobject var10 = (Dataobject)var9.getData().get(0);
            var4 = var10.getId();
            var3 = true;
            var15 = false;
        } catch (Exception var16) {
            throw new FoundationException(var16);
        } finally {
            if (var15) {
                if (var3) {
                    StringList var12 = new StringList();
                    var12.add(var2);
                    createProjectInBackgroundProcessNotification(var0, var4, var12, var3);
                }

            }
        }

        if (var3) {
            StringList var18 = new StringList();
            var18.add(var2);
            createProjectInBackgroundProcessNotification(var0, var4, var18, var3);
        }

    }

    private static void createProjectInBackgroundProcessNotification(Context var0, String var1, StringList var2, boolean var3) throws FoundationException {
        try {
            String var4 = MqlUtil.mqlCommand(var0, "print bus $1 select $2 dump", new String[]{var1, "name"});
            String var5 = NotificationUtil.appendBaseUrl(var0, new StringList(var1), "");
            var5 = var5.replace("\n", "");
            String var6 = "<a href=" + XSSUtil.encodeForHTML(var0, var5) + ">" + var4 + "</a>";
            String var7 = "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CreateProjectSubject";
            String var8 = "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CreateProjectBody";
            Iterator<String> var9 = var2.iterator();

            while(var9.hasNext()) {
                String var10 = var9.next();
                String var11 = com.matrixone.apps.domain.util.PropertyUtil.getAdminProperty(var0, "person", var10, "IconMailLanguagePreference");
                Locale var12 = new Locale(var11);
                String var13 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", var7, var12).replaceAll("%NAME%", var4);
                String var14 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", var8, var12);
                String var15 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.ProjectName", var12).replaceAll("%NAME%", "<strong>" + var6 + "</strong>");
                var14 = var14 + "<br><br>" + var15;
                var14 = var14 + "<br><br> ";
                MailNotification var16 = new MailNotification(var0, var10, var13, var14);
                var16.sendMail();
            }

        } catch (Exception var17) {
            throw new FoundationException(var17);
        }
    }

    public static void completeImportBackgroundProcess(Context context, String newlyProjectCreatedId, MapList importObjList, HashMap parameterMap) throws FoundationException {
        gscProjectService var4 = new gscProjectService();
        Object[] args = new Object[]{context, newlyProjectCreatedId, importObjList, parameterMap};
        ContextUtil.submitPostTransactionJob(context, var4, "completeImportProcess", args);
        ContextUtil.processPostTransactionJob(context, true);
    }

    public static void completeImportProcess(Context context, String var1, MapList var2, HashMap var3) throws FoundationException {
        String datesAsEntered = (String)var3.get("UseStartAndEndDatesAsEntered");
        String scheduleFrom = (String)var3.get("ScheduleFrom");
        String calendarOID = (String)var3.get("CalendarOID");
        String mode = (String)var3.get("mode");
        String isCreateProjectOrCopyScheduleFromFile = (String)var3.get("isCreateProjectOrCopyScheduleFromFile");
        boolean isBackgroundProcess = "TRUE".equalsIgnoreCase((String)var3.get("isBackgroundProcess"));
        String loggedInUser = (String)var3.get("loggedInUser");
        boolean var11 = false;
        HashMap projectWBSIdToLevelMap = (HashMap)var3.get("projectWBSIdToLevelMap");
        boolean var66 = false;

        try {
            var66 = true;
            WorkCalendar defaultWorkCalendar = null;
            WorkCalendar workCalendar = null;
            if ("true".equalsIgnoreCase(datesAsEntered)) {
                if (!"importProjectSchedule".equalsIgnoreCase(mode) && !"copyScheduleFromFile".equalsIgnoreCase(isCreateProjectOrCopyScheduleFromFile)) {
                    if (ProgramCentralUtil.isNotNullString(calendarOID)) {
                        DomainObject domainObject = new DomainObject(calendarOID);
                        String var75 = domainObject.getInfo(context, "modified");
                        workCalendar = WorkCalendar.getCalendarObject(context, calendarOID, var75);
                    }
                } else {
                    StringList var15 = new StringList(5);
                    var15.addElement(SELECT_PROJECT_DEFAULT_CALENDAR_ID);
                    var15.addElement(SELECT_PROJECT_DEFAULT_CALENDAR_DATE);
                    var15.addElement(ProgramCentralConstants.SELECT_ATTRIBUTE_SCHEDULED_FROM);
                    var15.addElement(SELECT_PARENT_PROJECT_DEFAULT_CALENDAR_ID);
                    var15.addElement(SELECT_PARENT_PROJECT_DEFAULT_CALENDAR_DATE);
                    var15.addElement(ProgramCentralConstants.SELECT_PROJECT_SCHEDULE_FROM_TASK);
                    ArrayList<String> var16 = new ArrayList<String>();
                    var16.add(var1);
                    if (UIUtil.isNotNullAndNotEmpty(calendarOID)) {
                        var16.add(calendarOID);
                        var15.addElement("modified");
                    }

                    BusinessObjectWithSelectList objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, (String[])var16.toArray(new String[var16.size()]), var15, true);
                    BusinessObjectWithSelect selectListElement = (BusinessObjectWithSelect)objectWithSelectList.getElement(0);
                    scheduleFrom = selectListElement.getSelectData(ProgramCentralConstants.SELECT_ATTRIBUTE_SCHEDULED_FROM);
                    if (UIUtil.isNullOrEmpty(scheduleFrom)) {
                        scheduleFrom = selectListElement.getSelectData(ProgramCentralConstants.SELECT_PROJECT_SCHEDULE_FROM_TASK);
                    }

                    if (UIUtil.isNullOrEmpty(scheduleFrom)) {
                        scheduleFrom = "Project Start Date";
                    }

                    String defaultCalendarId = selectListElement.getSelectData(SELECT_PROJECT_DEFAULT_CALENDAR_ID);
                    String defaultCalendarDate = selectListElement.getSelectData(SELECT_PROJECT_DEFAULT_CALENDAR_DATE);
                    if (ProgramCentralUtil.isNullString(defaultCalendarId)) {
                        defaultCalendarId = selectListElement.getSelectData(SELECT_PARENT_PROJECT_DEFAULT_CALENDAR_ID);
                        defaultCalendarDate = selectListElement.getSelectData(SELECT_PARENT_PROJECT_DEFAULT_CALENDAR_DATE);
                    }

                    if (UIUtil.isNotNullAndNotEmpty(defaultCalendarId)) {
                        workCalendar = WorkCalendar.getCalendarObject(context, defaultCalendarId, defaultCalendarDate);
                    }
                }

                if (null == workCalendar) {
                    defaultWorkCalendar = WorkCalendar.getDefaultCalendar();
                }
            }

            int var74 = eMatrixDateFormat.getEMatrixDisplayDateFormat();
            TimeZone timeZone = TimeZone.getTimeZone(context.getSession().getTimezone());
            double rawOffset = -1.0 * (double)timeZone.getRawOffset();
            double offset = new Double(rawOffset / 3600000.0);
            DateFormat dateFormat = DateFormat.getDateTimeInstance(var74, var74, Locale.US);
            Calendar calendar = Calendar.getInstance();
            String var23 = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump", new String[]{var1, "physicalid"});
            Dataobject dataobject1 = new Dataobject();
            dataobject1.setId(var23);
            dataobject1.setUpdateAction(UpdateActions.MODIFY);
            Datacollection datacollection = new Datacollection();
            RelateddataMapAdapter.addRelatedData(dataobject1, "tasks", datacollection);
            HashMap<String, Dataobject> var26 = new HashMap<String, Dataobject>();
            StringList stringList = new StringList();
            StringList stringList1 = new StringList();

            try {
                Map<String, StringList> derivativeType = ProgramCentralUtil.getDerivativeType(context, new String[]{ProgramCentralConstants.TYPE_GATE, ProgramCentralConstants.TYPE_MILESTONE});
                stringList1.addAll(derivativeType.get(ProgramCentralConstants.TYPE_GATE));
                stringList1.addAll(derivativeType.get(ProgramCentralConstants.TYPE_MILESTONE));
            } catch (Exception var68) {
                stringList1.add(ProgramCentralConstants.TYPE_GATE);
                stringList1.add(ProgramCentralConstants.TYPE_MILESTONE);
            }

            StringList var79 = new StringList();

            try {
                Map<String, StringList> var30 = ProgramCentralUtil.getDerivativeType(context, new String[]{ProgramCentralConstants.TYPE_PROJECT_SPACE});
                var79.addAll(var30.get(ProgramCentralConstants.TYPE_PROJECT_SPACE));
            } catch (Exception var67) {
                var79.add(ProgramCentralConstants.TYPE_PROJECT_SPACE);
            }

            HashMap<String, Boolean> var80 = new HashMap<String, Boolean>();
            int var31 = var2.size();
            int var32 = 0;
            Iterator var33 = var2.iterator();

            while(true) {
                while(var33.hasNext()) {
                    Map var34 = (Map)var33.next();
                    String type = (String)var34.remove("type");
                    String name = (String)var34.remove("name");
                    String level = (String)var34.remove("Level");
                    if ("0".equals(level)) {
                        --var31;
                    } else {
                        ++var32;
                        boolean var38 = var79.contains(type);
                        String var39 = ((String)var34.remove(ATTRIBUTE_TASK_ESTIMATED_DURATION)).toLowerCase();
                        var39 = var39.replace("days", "d");
                        var39 = var39.replace("hours", "h");
                        boolean var40 = stringList1.contains(type);
                        double var41 = Task.parseToDouble(var39.replaceAll("days|hours|d|h", "").trim());
                        if (var40 && var41 != 0.0) {
                            var39 = "0.0 d";
                        } else if (!var40 && var41 <= 0.0) {
                            var39 = "1.0 d";
                        }

                        String assignees = (String)var34.remove("Assignees");
                        String dependencies = (String)var34.remove("Dependencies");
                        String taskConstraintType = (String)var34.remove(DomainConstants.ATTRIBUTE_TASK_CONSTRAINT_TYPE);
                        String taskConstraintDate = (String)var34.remove(DomainConstants.ATTRIBUTE_TASK_CONSTRAINT_DATE);
                        Dataobject dataobject = new Dataobject();
                        dataobject.setId(String.valueOf(level));
                        dataobject.setUpdateAction(UpdateActions.CREATE);
                        dataobject.setType(type);
//                  DataelementMapAdapter.setDataelementValue(dataobject, "title", name);
                        HashMap hashMap = new HashMap();
                        if ("true".equalsIgnoreCase(datesAsEntered)) {
                            hashMap.put("EstimatedStartDate", var34.remove("Task Estimated Start Date"));
                            hashMap.put("EstimatedFinishDate", var34.remove("Task Estimated Finish Date"));
                            hashMap.put("ProjectScheduleFrom", scheduleFrom);
                            hashMap.put("TaskType", type);
                            hashMap.put("WorkCalendar", workCalendar);
                            if (null == workCalendar) {
                                hashMap.put("DefaultWorkCalendar", defaultWorkCalendar);
                            }

                            if (!var38) {
                                hashMap.put("TaskAssignees", assignees);
                            }

                            Map<String, String> var49 = getUpdatedAttributesForSelectedDates(context, hashMap);
                            var39 = var49.get("Duration");
                            taskConstraintType = var49.get("TaskConstraintType");
                            taskConstraintDate = var49.get("TaskConstraintDate");
                        }

                        DataelementMapAdapter.setDataelementValue(dataobject, "estimatedDuration", var39);
                        if (taskConstraintType != null && !taskConstraintType.isEmpty()) {
                            DataelementMapAdapter.setDataelementValue(dataobject, "constraintType", taskConstraintType);
                        }

                        if (taskConstraintDate != null && !taskConstraintDate.isEmpty()) {
                            try {
                                taskConstraintDate = eMatrixDateFormat.getFormattedDisplayDateTime(context, taskConstraintDate, true, var74, offset, Locale.US);
                                Date var84 = dateFormat.parse(taskConstraintDate);
                                calendar.clear();
                                calendar.setTime(var84);
                                if (!"Start No Later Than".equalsIgnoreCase(taskConstraintType) && !"Start No Earlier Than".equalsIgnoreCase(taskConstraintType) && !"Must Start On".equalsIgnoreCase(taskConstraintType)) {
                                    if ("Must Finish On".equalsIgnoreCase(taskConstraintType) || "Finish No Later Than".equalsIgnoreCase(taskConstraintType) || "Finish No Earlier Than".equalsIgnoreCase(taskConstraintType)) {
                                        calendar.set(11, 17);
                                    }
                                } else {
                                    calendar.set(11, 8);
                                }

                                calendar.set(12, 0);
                                calendar.set(13, 0);
                                String var50 = FormatUtil.getFormattedISODate(context, calendar.getTime(), (SimpleDateFormat)null);
                                DataelementMapAdapter.setDataelementValue(dataobject, "constraintDate", var50);
                            } catch (Exception var69) {
                                System.out.println(var69.getMessage());
                            }
                        }

                        var34.remove("Task Estimated Start Date");
                        var34.remove("Task Estimated Finish Date");
                        String var85 = (String)var34.get("Deliverables Inheritance");
                        if (ProgramCentralUtil.isNotNullString(var85)) {
                            DataelementMapAdapter.setDataelementValue(dataobject, "deliverablesInheritance", var85);
                        }

                        var34.remove("Deliverables Inheritance");
                        Iterator var86 = var34.keySet().iterator();

                        String var52;
                        while(var86.hasNext()) {
                            String var51 = (String)var86.next();
                            var52 = (String)var34.get(var51);
                            int var53 = var51.indexOf(".");
                            if (var53 != -1) {
                                var51 = var51.substring(var53 + 1);
                            }

                            if (!var80.containsKey(var51)) {
                                var80.put(var51, false);
                                String var54 = MqlUtil.mqlCommand(context, "list attribute $1 select $2 dump", new String[]{var51, "valuetype"});
                                if ("multival".equalsIgnoreCase(var54)) {
                                    var80.put(var51, true);
                                }
                            }

                            if (var80.get(var51)) {
                                List<String> var91 = StringUtil.splitString(var52, ",");
                                DataelementMapAdapter.setDataelementValues(dataobject, var51, var91);
                            } else {
                                DataelementMapAdapter.setDataelementValue(dataobject, var51, var52);
                            }
                        }

                        Dataobject var87 = null;
                        int var88 = level.lastIndexOf(".");
                        if (var88 != -1) {
                            var52 = level.substring(0, var88);
                            var87 = var26.get(var52);
                        }

                        if (var87 == null) {
                            datacollection.getDataobjects().add(dataobject);
                        } else {
                            var87.getChildren().add(dataobject);
                        }

                        stringList.clear();
                        if (!var38 && assignees != null && !assignees.isEmpty()) {
                            Datacollection var89 = new Datacollection();
                            RelateddataMapAdapter.addRelatedData(dataobject, "assignees", var89);
                            StringList var90 = FrameworkUtil.split(assignees, "|");
                            Iterator<String> var92 = var90.iterator();

                            while(var92.hasNext()) {
                                String var55 = var92.next();
                                StringList var56 = FrameworkUtil.split(var55, ":");
                                String var57 = (String)var56.get(1);
                                if (!stringList.contains(var57)) {
                                    stringList.addElement(var57);
                                    Dataobject var58 = new Dataobject();
                                    var58.setUpdateAction(UpdateActions.CREATE);
                                    var58.setId(var57);
                                    var89.getDataobjects().add(var58);
                                }
                            }
                        }

                        if (dependencies != null && !dependencies.isEmpty()) {
                            addDependencies(dependencies, dataobject, var31, projectWBSIdToLevelMap, "CopyFromFile");
                        }

                        var34.remove("ParentProjectLevel");
                        var34.remove("WBSId");
                        var26.put(level, dataobject);
                    }
                }

                Servicedata var81 = new Servicedata();
                var81.getData().add(dataobject1);
                ServiceParameters var82 = new ServiceParameters();
                var82.setServiceName("dpm.gscprojects");
                ArgMap var83 = new ArgMap();
                var82.setServiceArgs(var83);
                var83.put("$include", "none");
                var83.put("$fields", "none");
                var83.put("rollup", "live");
                var83.put("disableTriggers", "true");
                ServiceBase.saveData(context, var81, var82, 0L);
                var11 = true;
                var66 = false;
                break;
            }
        } catch (Exception var70) {
            throw new FoundationException(var70);
        } finally {
            if (var66) {
                if (isBackgroundProcess) {
                    StringList var60 = new StringList();
                    var60.add(loggedInUser);
                    importBackgroundProcessNotification(context, var1, var60, isCreateProjectOrCopyScheduleFromFile, var11);
                }

            }
        }

        if (isBackgroundProcess) {
            StringList var72 = new StringList();
            var72.add(loggedInUser);
            importBackgroundProcessNotification(context, var1, var72, isCreateProjectOrCopyScheduleFromFile, var11);
        }

    }

    private static void importBackgroundProcessNotification(Context var0, String var1, StringList var2, String var3, boolean var4) throws FoundationException {
        try {
            String var5 = MqlUtil.mqlCommand(var0, "print bus $1 select $2 dump", new String[]{var1, "name"});
            String var6 = NotificationUtil.appendBaseUrl(var0, new StringList(var1), "");
            var6 = var6.replace("\n", "");
            String var7 = "<a href=" + XSSUtil.encodeForHTML(var0, var6) + ">" + var5 + "</a>";
            String var8 = "createNewProjectFromFile".equalsIgnoreCase(var3) ? "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CreateProjectSubject" : "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CopyScheduleSubject";
            String var9 = "createNewProjectFromFile".equalsIgnoreCase(var3) ? "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CreateProjectBody" : "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.CopyScheduleBody";
            if (!var4) {
                var8 = "createNewProjectFromFile".equalsIgnoreCase(var3) ? "emxProgramCentral.Import.BackgoundProcessFailedNotifyMessage.CreateProjectSubject" : "emxProgramCentral.Import.BackgoundProcessFailedNotifyMessage.CopyScheduleSubject";
                var9 = "createNewProjectFromFile".equalsIgnoreCase(var3) ? "emxProgramCentral.Import.BackgoundProcessFailedNotifyMessage.CreateProjectBody" : "emxProgramCentral.Import.BackgoundProcessFailedNotifyMessage.CopyScheduleBody";
            }

            Iterator<String> var10 = var2.iterator();

            while(var10.hasNext()) {
                String var11 = var10.next();
                String var12 = com.matrixone.apps.domain.util.PropertyUtil.getAdminProperty(var0, "person", var11, "IconMailLanguagePreference");
                Locale var13 = new Locale(var12);
                String var14 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", var8, var13).replaceAll("%NAME%", var5);
                String var15 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", var9, var13);
                String var16 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", "emxProgramCentral.Import.BackgoundProcessCompleteNotifyMessage.ProjectName", var13).replaceAll("%NAME%", "<strong>" + var7 + "</strong>");
                var15 = var15 + "<br><br>" + var16;
                var15 = var15 + "<br><br> ";
                MailNotification var17 = new MailNotification(var0, var11, var14, var15);
                var17.sendMail();
            }

        } catch (Exception var18) {
            throw new FoundationException(var18);
        }
    }

    private static void addDependencies(String var0, Dataobject var1, int var2, Map var3, String var4) {
        Datacollection var5 = new Datacollection();
        RelateddataMapAdapter.addRelatedData(var1, "predecessors", var5);
        StringList var6 = FrameworkUtil.split(var0, ",");
        String var7 = (String)var1.getDataelements().get("ParentProjectLevel");
        if (ProgramCentralUtil.isNullString(var7)) {
            var7 = "0";
        }

        HashMap var8 = var3 != null && var3.get(var7) != null ? (HashMap)var3.get(var7) : new HashMap();
        var6.forEach((var3x) -> {
            StringList var4x = FrameworkUtil.split(var3x, ":");
            String var5x = (String)var4x.get(0);
            var5x = (String)var8.get(var5x);
            boolean var16 = "CopyFromFile".equalsIgnoreCase(var4) && ProgramCentralUtil.isNotNullString(var5x);
            boolean var17 = "CopyFromTemplate".equalsIgnoreCase(var4);
            if (var17 || var16) {
                String var8x = var4x.size() > 1 ? ((String)var4x.get(1)).trim() : "FS";
                String var9 = var8x.indexOf("+") != -1 ? "+" : "-";
                StringList var10 = FrameworkUtil.split(var8x, var9);
                String var11 = ((String)var10.get(0)).trim();
                String var12 = var10.size() > 1 ? ((String)var10.get(1)).trim() : null;
                Dataobject var13 = new Dataobject();
                var5.getDataobjects().add(var13);
                var13.setUpdateAction(UpdateActions.CREATE);
                var13.setId(var5x);
                RelelementMapAdapter.setRelelementValue(var13, "dependencyType", var11);
                if (var12 != null) {
                    RelelementMapAdapter.setRelelementValue(var13, "lagTime", var9 + var12);
                }
            }

        });
    }

    private static String zip(String var0) {
        try {
            ByteArrayOutputStream var1 = new ByteArrayOutputStream();
            Throwable var2 = null;

            String var35;
            try {
                GZIPOutputStream var3 = new GZIPOutputStream(var1);
                Throwable var4 = null;

                try {
                    var3.write(var0.getBytes(StandardCharsets.UTF_8));
                } catch (Throwable var29) {
                    var4 = var29;
                    throw var29;
                } finally {
                    if (var3 != null) {
                        if (var4 != null) {
                            try {
                                var3.close();
                            } catch (Throwable var28) {
                                var4.addSuppressed(var28);
                            }
                        } else {
                            var3.close();
                        }
                    }

                }

                Base64.Encoder var34 = Base64.getEncoder();
                var35 = var34.encodeToString(var1.toByteArray());
            } catch (Throwable var31) {
                var2 = var31;
                throw var31;
            } finally {
                if (var1 != null) {
                    if (var2 != null) {
                        try {
                            var1.close();
                        } catch (Throwable var27) {
                            var2.addSuppressed(var27);
                        }
                    } else {
                        var1.close();
                    }
                }

            }

            return var35;
        } catch (IOException var33) {
            throw new RuntimeException("Failed to zip content", var33);
        }
    }

    private static String unzip(String var0) {
        Base64.Decoder var1 = Base64.getDecoder();
        byte[] var2 = var1.decode(var0);
        if (!isCompressed(var2)) {
            return var0;
        } else {
            try {
                ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
                Throwable var4 = null;

                try {
                    GZIPInputStream var5 = new GZIPInputStream(var3);
                    Throwable var6 = null;

                    try {
                        InputStreamReader var7 = new InputStreamReader(var5, StandardCharsets.UTF_8);
                        Throwable var8 = null;

                        try {
                            BufferedReader var9 = new BufferedReader(var7);
                            Throwable var10 = null;

                            try {
                                StringBuilder var11 = new StringBuilder();

                                String var12;
                                while((var12 = var9.readLine()) != null) {
                                    var11.append(var12);
                                }

                                String var13 = var11.toString();
                                return var13;
                            } catch (Throwable var89) {
                                var10 = var89;
                                throw var89;
                            } finally {
                                if (var9 != null) {
                                    if (var10 != null) {
                                        try {
                                            var9.close();
                                        } catch (Throwable var88) {
                                            var10.addSuppressed(var88);
                                        }
                                    } else {
                                        var9.close();
                                    }
                                }

                            }
                        } catch (Throwable var91) {
                            var8 = var91;
                            throw var91;
                        } finally {
                            if (var7 != null) {
                                if (var8 != null) {
                                    try {
                                        var7.close();
                                    } catch (Throwable var87) {
                                        var8.addSuppressed(var87);
                                    }
                                } else {
                                    var7.close();
                                }
                            }

                        }
                    } catch (Throwable var93) {
                        var6 = var93;
                        throw var93;
                    } finally {
                        if (var5 != null) {
                            if (var6 != null) {
                                try {
                                    var5.close();
                                } catch (Throwable var86) {
                                    var6.addSuppressed(var86);
                                }
                            } else {
                                var5.close();
                            }
                        }

                    }
                } catch (Throwable var95) {
                    var4 = var95;
                    throw var95;
                } finally {
                    if (var3 != null) {
                        if (var4 != null) {
                            try {
                                var3.close();
                            } catch (Throwable var85) {
                                var4.addSuppressed(var85);
                            }
                        } else {
                            var3.close();
                        }
                    }

                }
            } catch (IOException var97) {
                throw new RuntimeException("Failed to unzip content", var97);
            }
        }
    }

    private static boolean isCompressed(byte[] var0) {
        return var0[0] == 31 && var0[1] == -117;
    }

    public static void getGanttConfig(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        List<Dataobject> var3 = var2.getDatacollection().getDataobjects();
        Iterator<Dataobject> var4 = var3.iterator();

        while(var4.hasNext()) {
            Dataobject var5 = var4.next();
            ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var6 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(2);
            var6.add(new Select("GanttConfig", ProgramCentralConstants.SELECT_GANTT_CONFIG_FROM_PROJECT, ExpressionType.BUS, (Format)null, false));
            ObjectUtil.print(var0, var5, (PrintData)null, var6, true);
            String var7 = DataelementMapAdapter.getDataelementValue(var5, "GanttConfig");
            if (var7 != null && !var7.isEmpty()) {
                String var8 = unzip(var7);
                if (var5.getDataelements() != null) {
                    var5.getDataelements().replace("ganttConfig", var8);
                }
            }
        }

    }

    public static Map<String, String> getUpdatedAttributesForSelectedDates(Context var0, Map var1) throws Exception {
        HashMap<String, String> var2 = new HashMap<>();

        try {
            String var3 = (String)var1.get("EstimatedStartDate");
            String var4 = (String)var1.get("EstimatedFinishDate");
            WorkCalendar var5 = (WorkCalendar)var1.get("WorkCalendar");
            String var6 = (String)var1.get("TaskAssignees");
            String var7 = (String)var1.get("ProjectScheduleFrom");
            if (UIUtil.isNullOrEmpty(var7)) {
                var7 = "Project Start Date";
            }

            String var8 = "";
            String var9 = "";
            String var10 = "";
            String var11 = "";
            boolean var12 = true;
            boolean var13 = true;
            if (null == var5) {
                if (UIUtil.isNotNullAndNotEmpty(var6)) {
                    StringList var14 = FrameworkUtil.split(var6, "|");
                    if (1 == var14.size()) {
                        String var15 = (String)FrameworkUtil.split(var6, ":").get(1);
                        Map var16 = CacheUtil.getCacheMap(var0, "WorkCalendar_Assignee_Cache");
                        var5 = (WorkCalendar)var16.get(var15);
                        CacheUtil.setCacheMap(var0, "WorkCalendar_Assignee_Cache", var16);
                        if (null == var5) {
                            Person var17 = new Person(var15);
                            String var18 = var17.getInfo(var0, Person.SELECT_LOCATION_ID);
                            if (UIUtil.isNotNullAndNotEmpty(var18)) {
                                Map var19 = CacheUtil.getCacheMap(var0, "WorkCalendar_Location_Cache");
                                var5 = (WorkCalendar)var19.get(var18);
                                if (null == var5) {
                                    var5 = WorkCalendar.getCalendar(var0, var18);
                                    var19.put(var18, var5);
                                    CacheUtil.setCacheMap(var0, "WorkCalendar_Location_Cache", var19);
                                    var16.put(var15, var5);
                                    CacheUtil.setCacheMap(var0, "WorkCalendar_Assignee_Cache", var16);
                                }
                            }
                        }
                    }
                }

                if (null == var5) {
                    var5 = (WorkCalendar)var1.get("DefaultWorkCalendar");
                    var13 = false;
                }
            }

            Calendar var35 = Calendar.getInstance();
            Calendar var36 = Calendar.getInstance();
            Calendar var37 = Calendar.getInstance();
            Date var38 = null;
            Date var39 = null;
            int var40 = 8;
            int var20 = 0;
            int var21 = 17;
            int var22 = 0;
            Map var23 = null;
            new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            TimeZone var25 = TimeZone.getTimeZone(var0.getSession().getTimezone());
            double var26 = -1.0 * (double)var25.getRawOffset();
            double var28 = new Double(var26 / 3600000.0);
            int var30 = eMatrixDateFormat.getEMatrixDisplayDateFormat();
            Locale var31 = var0.getLocale();
            DateFormat var32 = DateFormat.getDateTimeInstance(var30, var30, var31);
            if (UIUtil.isNotNullAndNotEmpty(var3) && UIUtil.isNotNullAndNotEmpty(var4)) {
                int var34;
                if (UIUtil.isNotNullAndNotEmpty(var3)) {
                    var3 = eMatrixDateFormat.getFormattedDisplayDateTime(var0, var3, true, var30, var28, var31);
                    var38 = var32.parse(var3);
                    var37.setTime(var38);
                    if (var13) {
                        var34 = Integer.parseInt(var5.getInfo(var0, "attribute[Working Time Per Day].value"));
                        var23 = var5.getWorkingDayDetailMap(var0, var37);
                        if (var23 != null && !var23.isEmpty()) {
                            var40 = (Integer)var23.get("attribute[Work Start Time].value");
                            var20 = (Integer)var23.get("Start Minute");
                        }
                    }

                    var37.set(11, var40);
                    var37.set(12, var20);
                    var38 = var37.getTime();
                }

                if (UIUtil.isNotNullAndNotEmpty(var4)) {
                    var4 = eMatrixDateFormat.getFormattedDisplayDateTime(var0, var4, true, var30, var28, var31);
                    var39 = var32.parse(var4);
                    var36.setTime(var39);
                    if (var13) {
                        var34 = Integer.parseInt(var5.getInfo(var0, "attribute[Working Time Per Day].value"));
                        var23 = var5.getWorkingDayDetailMap(var0, var36);
                        if (var23 != null && !var23.isEmpty()) {
                            var21 = (Integer)var23.get("attribute[Work Finish Time].value");
                            var22 = (Integer)var23.get("Finish Minute");
                        }
                    }

                    var36.set(11, var21);
                    var36.set(12, var22);
                    var39 = var36.getTime();
                }

                var11 = var4;
                var10 = "Finish No Earlier Than";
                if ("Project Finish Date".equalsIgnoreCase(var7)) {
                    var10 = "Finish No Later Than";
                }

                if (var39.before(var38)) {
                    var8 = "1.0 d";
                } else {
                    if (var13) {
                        var8 = Double.toString(var5.computeNewDuration(var0, var38, var39));
                    } else {
                        var8 = Double.toString((double)var5.computeDuration(var0, var38, var39));
                    }

                    if ("0.0".equalsIgnoreCase(var8)) {
                        var8 = "1.0";
                    }

                    var8 = var8.concat(" d");
                }
            }

            var2.put("Duration", var8);
            var2.put("TaskConstraintType", var10);
            var2.put("TaskConstraintDate", var11);
            return var2;
        } catch (Exception var33) {
            var33.printStackTrace();
            throw var33;
        }
    }

    public static void completePTCloningProcess(Context context, String projectId, MapList taskMapList, Map answerList, String defaultTaskConstraintType, HashMap selectedOptionMap, String templateId) throws FoundationException {
        try {
            HashSet<String> taskSet = new HashSet<String>();
            HashMap<String, String> taskWBSMap = new HashMap<String, String>();
            new HashMap();
            new HashSet();
            HashSet<Integer> var10 = new HashSet<>();
            boolean var11 = selectedOptionMap != null && selectedOptionMap.containsKey("copyTaskConstraint") ? "TRUE".equalsIgnoreCase((String)selectedOptionMap.get("copyTaskConstraint")) : true;
            String mqlprojectinfo = MqlUtil.mqlCommand(context, "print bus $1 select $2 $3 $4 dump $5", new String[]{projectId, "physicalid", "type", "from[Initiated Template Project].to.physicalid", "|"});
            StringList listmqlprojectinfo = FrameworkUtil.split(mqlprojectinfo, "|");
            String projectPhysicalId = (String)listmqlprojectinfo.get(0);
            String projectType = (String)listmqlprojectinfo.get(1);
            // String templateId = (String)listmqlprojectinfo.get(2);
            new HashMap();
            new HashMap();
            Map<String, Dataobject> var19 = null;
            String relProjectAccessKey = "";
            boolean var21 = false;
            String var22 = "";
            taskMapList.parallelStream().forEach((taskMap) -> {
                String attrTaskWBSvalue = (String)((Map)taskMap).get("attribute[Task WBS].value");
                String taskpysicalId = (String)((Map)taskMap).get("physicalid");
                boolean var6x = checkAnswer((Map)taskMap, answerList);
                if (!var6x) {
                    taskSet.add(taskpysicalId);
                    taskWBSMap.put(taskpysicalId, attrTaskWBSvalue);
                }

            });
            Collection<String> var23 = taskWBSMap.values();
            Iterator taskIter = taskMapList.iterator();
            boolean var25 = false;

            while(true) {
                String var28;
                String var29;
                String var40;
                do {
                    if (!taskIter.hasNext()) {
                        ServiceParameters serviceParameters = new ServiceParameters();
                        serviceParameters.setServiceName("dpm.gscprojects/" + templateId);
                        ArgMap argMap = new ArgMap();
                        serviceParameters.setServiceArgs(argMap);
                        argMap.put("$include", "none,tasks,predecessors");
                        StringBuffer buf = new StringBuffer();
                        buf.append("all");
                        buf.append(",tasks.deliverablesInheritance");
                        buf.append(",!tasks.percentComplete");
                        buf.append(",!tasks.estimatedStartDate");
                        buf.append(",!tasks.dueDate");
                        buf.append(",!tasks.actualStartDate");
                        buf.append(",!tasks.actualFinishDate");
                        buf.append(",!tasks.actualDuration");
                        buf.append(",!tasks.forecastStartDate");
                        buf.append(",!tasks.forecastFinishDate");
                        buf.append(",!tasks.forecastDuration");
                        buf.append(",!tasks.freeFloat");
                        buf.append(",!tasks.totalFloat");
                        buf.append(",!tasks.isOverallCritical");
                        buf.append(",!tasks.Deviation");
                        buf.append(",!tasks.status");
                        buf.append(",!tasks.predictiveActualFinishDate");
                        buf.append(",!tasks.constraintDate");
                        buf.append(",!tasks.pattern");
                        buf.append(",!tasks.color");
                        argMap.put("$fields", buf.toString());
                        argMap.put("level", "0");
                        Servicedata servicedata = ServiceBase.getData(context, serviceParameters);
                        Dataobject templateDataObj = servicedata.getData().isEmpty() ? null : (Dataobject)servicedata.getData().get(0);
                        templateDataObj.setUpdateAction(UpdateActions.MODIFY);
                        templateDataObj.setId(projectPhysicalId);
                        templateDataObj.setType(projectType);
                        templateDataObj.getDataelements().clear();
                        RelateddataMap relateddata = templateDataObj.getRelateddata();
                        Object templateTaskList = new ArrayList();
                        if (relateddata != null && relateddata.containsKey("tasks")) {
                            templateTaskList = (List<Dataobject>)relateddata.get("tasks");
                        }

                        if (var11) {
                            defaultTaskConstraintType = null;
                        }

                        setDefaultUpdateActionAndConstarintType(UpdateActions.CREATE, (List<Dataobject>) templateTaskList, defaultTaskConstraintType, taskSet);
                        Servicedata servicedata1 = new Servicedata();
                        servicedata1.getData().add(templateDataObj);
                        ServiceParameters serviceParameters1 = new ServiceParameters();
                        serviceParameters1.setServiceName("dpm.gscprojects");
                        ArgMap argMap1 = new ArgMap();
                        serviceParameters1.setServiceArgs(argMap1);
                        argMap1.put("$include", "none");
                        argMap1.put("$fields", "none");
                        argMap1.put("rollup", "live");
                        argMap1.put("disableTriggers", "true");
                        ServiceBase.saveData(context, servicedata1, serviceParameters1, 0L);
                        return;
                    }

                    Map taskiterNext = (Map)taskIter.next();
                    if (relProjectAccessKey.isEmpty()) {
                        relProjectAccessKey = (String)taskiterNext.get(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_TASK);
                        ProjectSequence var27 = new ProjectSequence(context, relProjectAccessKey);
                        var19 = var27.getSequenceData(context);
                    }

                    if (!var25) {
                        for(Iterator<String> var39 = taskSet.iterator(); var39.hasNext(); var25 = true) {
                            var28 = var39.next();
                            var29 = (String)(var19.get(var28)).getDataelements().get("seqId");
                            var10.add(Integer.parseInt(var29));
                        }
                    }

                    var40 = (String)taskiterNext.get("physicalid");
                    var28 = (String)taskiterNext.get(ProgramCentralConstants.SELECT_PARENT_TASK_PHYSICALID);
                } while(!taskSet.contains(var40) && !taskSet.contains(var28));

                taskSet.add(var40);
                taskIter.remove();
                var29 = (String)(var19.get(var40)).getDataelements().get("seqId");
                var10.add(Integer.parseInt(var29));
            }
        } catch (Exception var37) {
            var37.printStackTrace();

            throw var37;
        }
    }

    private static void setDefaultUpdateActionAndConstarintType(UpdateActions var0, List<Dataobject> var1, String var2, HashSet<String> var3) {
        int var4 = 0;

        for(int var5 = var1.size(); var4 < var5; ++var4) {
            Dataobject var6 = var1.get(var4);
            int var10;
            if (!var3.contains(var6.getId())) {
                var6.setUpdateAction(UpdateActions.CREATE);
                if (var2 != null && !var2.isEmpty()) {
                    DataelementMapAdapter.setDataelementValue(var6, "constraintType", var2);
                }

                List<Dataobject> var13 = var6.getChildren();
                if (var13 != null && var13.size() > 0) {
                    setDefaultUpdateActionAndConstarintType(UpdateActions.CREATE, var13, var2, var3);
                }

                RelateddataMap var14 = var6.getRelateddata();
                Object var15 = new ArrayList();
                if (var14 != null && var14.containsKey("predecessors")) {
                    var15 = (List<Dataobject>)var14.get("predecessors");
                }

                var10 = 0;

                for(int var16 = ((List)var15).size(); var10 < var16; ++var10) {
                    Dataobject var12 = (Dataobject)((List)var15).get(var10);
                    if (!var3.contains(var12.getId())) {
                        var12.setUpdateAction(UpdateActions.CREATE);
                    }
                }
            } else {
                var6.setUpdateAction(UpdateActions.NONE);
                RelateddataMap var7 = var6.getRelateddata();
                Object var8 = new ArrayList();
                if (var7 != null && var7.containsKey("predecessors")) {
                    var8 = (List<Dataobject>)var7.get("predecessors");
                }

                int var9 = 0;

                for(var10 = ((List)var8).size(); var9 < var10; ++var9) {
                    Dataobject var11 = (Dataobject)((List)var8).get(var9);
                    var11.setUpdateAction(UpdateActions.NONE);
                }
            }
        }

    }

    protected static boolean checkAnswer(Map var0, Map var1) {
        if (var1 == null) {
            return true;
        } else {
            String var2 = (String)var0.get(SELECT_QUESTION_ID);
            String var3 = (String)var0.get(SELECT_TASK_TRANSFER);
            boolean var4 = true;
            String var5 = (String)var1.get(var2);
            if (var3 != null && !var3.equalsIgnoreCase(var5)) {
                var4 = false;
            }

            return var4;
        }
    }

    public void getDpmTaskCalendar(Context var1, String[] var2) throws FoundationException {
        ServiceParameters var3 = (ServiceParameters)JPOUtil.unpackArgs(var2);
        Datacollection var4 = var3.getDatacollection();
        Map var5 = com.dassault_systemes.enovia.dpm.ProjectSpace.getTaskCalendar(var1, var4);
        List<Dataobject> var6 = var4.getDataobjects();
        Iterator<Dataobject> var7 = var6.iterator();

        while(var7.hasNext()) {
            Dataobject var8 = var7.next();
            String var9 = var8.getId();
            String var10 = (String)var5.get(var9);
            DataelementMapAdapter.setDataelementValue(var8, "CalendarId", var10);
        }

    }

    public static Datacollections promoteSuccessor(Context context, String[] inputStrings) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(inputStrings);
        HttpServletRequest httpServletRequest = serviceParameters.getHttpRequest();
        ServiceUtil.checkLicenseProject(context, httpServletRequest);
        String palIdField = (String)serviceParameters.getJpoArgs().get("palIdField");
        new Datacollections();
        Datacollection datacollection = serviceParameters.getDatacollection();
        ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> selectableArrayList = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(2);
        selectableArrayList.add(new Select(palIdField, "to[Project Access List].from.id", ExpressionType.BUS, (Format)null, false));
        selectableArrayList.add(new Select("objId", "id", ExpressionType.BUS, (Format)null, false));
        Dataobject dataobject = (Dataobject)datacollection.getDataobjects().get(0);
        ObjectUtil.print(context, dataobject, (PrintData)null, selectableArrayList, true);
        String objId = DataelementMapAdapter.getDataelementValue(dataobject, "objId");
        Datacollections datacollections = com.dassault_systemes.enovia.dpm.ProjectSpace.promoteSuccessor(context, objId);
        return datacollections;
    }

    public static void checkIfPromoteReadyTaskCommandVisible(Context var0, String[] var1) throws Exception {
        Boolean var2 = true;
        ServiceParameters var3 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        HttpServletRequest var4 = var3.getHttpRequest();
        ServiceUtil.checkLicenseProject(var0, var4);
        List<Dataobject> var5 = var3.getDatacollection().getDataobjects();
        Iterator<Dataobject> var6 = var5.iterator();

        while(var6.hasNext()) {
            Dataobject var7 = var6.next();
            var2 = gscProjectSpace.checkIfAutoPromoteDependentTaskEnabled(var0);
            DataelementMapAdapter.setDataelementValue(var7, "checkIfPromoteReadyTaskCommandVisible", var2.toString());
        }

    }

    public static Range getTypes(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("type");
        String var5 = (String)var3.get("excludeTypeName");
        StringList var6 = FrameworkUtil.split(var5, ",");
        Range var7 = ServiceUtil.getTypes(var0, var4, var6);
        return var7;
    }

    public static Range getAllTypePolicy(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("type");
        String var5 = (String)var3.get("excludePolicyName");
        String var6 = (String)var3.get("excludeTypeName");
        StringList var7 = FrameworkUtil.split(var6, ",");
        StringList var8 = FrameworkUtil.split(var5, ",");
        String var9 = com.dassault_systemes.enovia.tskv2.Task.getDefaultPolicy(var0, var4);
        Range var10 = ServiceUtil.getAllTypePolicy(var0, var4, var9, var7, var8);
        return var10;
    }

    public static Range getCurrencyValues(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("attributeName");
        String var5 = "";

        try {
            String var6 = PersonUtil.getCurrency(var0);
            if (var6 != null && !var6.isEmpty()) {
                var5 = var6;
            }
        } catch (FrameworkException var7) {
            var7.printStackTrace();
        }

        Range var8 = ServiceUtil.getAttributeRange(var0, var4, true, var5);
        return var8;
    }

    public static Range getRangeTypeDerivatives(Context var0, String[] var1) throws FoundationException, FrameworkException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("relKey");
        String var5 = (String)var3.get("searchtypes");
        String var6 = "6W_TYPE_DERIVATIVES:" + var4;
        String var7 = null;
        Class<gscProjectService> var8 = gscProjectService.class;
        synchronized(gscProjectService.class) {
            var7 = (String)CacheUtil.getCacheObject(var0, var6);
            if (var7 == null) {
                List<String> var9 = ObjectUtil.getTypeDerivatives(var0, var5);
                var7 = StringUtil.join(var9, ",");
                CacheUtil.setCacheObject(var0, var6, var7);
            }
        }

        Range var12 = new Range();
        Range.Item var13 = new Range.Item();
        var13.setDisplay(var7);
        var13.setValue("context");
        var12.getItem().add(var13);
        return var12;
    }

    static {
        SELECT_TASK_TRANSFER = "to[" + DomainConstants.RELATIONSHIP_QUESTION + "].attribute[" + DomainConstants.ATTRIBUTE_TASK_TRANSFER + "]";
        SELECT_QUESTION_ID = "to[" + DomainConstants.RELATIONSHIP_QUESTION + "].from.id";
        SELECT_PARENT_PROJECT_DEFAULT_CALENDAR_ID = "to[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_KEY + "].from.from[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_LIST + "].to.from[" + ProgramCentralConstants.RELATIONSHIP_DEFAULT_CALENDAR + "].to.id";
        SELECT_PARENT_PROJECT_DEFAULT_CALENDAR_DATE = "to[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_KEY + "].from.from[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_LIST + "].to.from[" + ProgramCentralConstants.RELATIONSHIP_DEFAULT_CALENDAR + "].to.modified";
        SELECT_PROJECT_DEFAULT_CALENDAR_ID = "from[" + ProgramCentralConstants.RELATIONSHIP_DEFAULT_CALENDAR + "].to.id";
        SELECT_PROJECT_DEFAULT_CALENDAR_DATE = "from[" + ProgramCentralConstants.RELATIONSHIP_DEFAULT_CALENDAR + "].to.modified";
    }

    private static enum Source {
        OPP_PRJ,
        OPP_PAL,
        RISK_PRJ,
        RISK_PAL;
    }
	
	public static void getPatentCount(Context context, String[] args) throws Exception {

        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
//        com.gsc.enovia.dpm.ServiceUtil.checkLicenseProject(context, httpRequest);
        List<Dataobject> dataobjectList = serviceParameters.getDatacollection().getDataobjects();
        Iterator<Dataobject> iterator = dataobjectList.iterator();

        while(iterator.hasNext()) {
            Dataobject dataobject = iterator.next();
            String dataobjectId = dataobject.getId();
            String gscPatentCount = com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("eval expr 'count TRUE' on query connection type 'gscProjectPatent' where 'from.id==%s'",dataobjectId));

            // 각각의 field 에 값 입력
            DataelementMapAdapter.setDataelementValue(dataobject, "gscPatentCount", gscPatentCount);
        }
    }
}