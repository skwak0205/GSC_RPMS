package com.gsc.enovia.dpm.projectconcept;

import com.dassault_systemes.enovia.dpm.ProjectService;
import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.*;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Locale;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.RelateddataMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.dassault_systemes.enovia.tskv2.ProjectSequence;
import com.matrixone.apps.common.Organization;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.PersonUtil;
import com.matrixone.apps.domain.util.eMatrixDateFormat;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import com.matrixone.apps.program.ProjectSpace;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.StringList;



public class gscProjectConceptService implements ServiceConstants {
    public static final String PARAMETER_STATE = "state";

    public static Datacollection getProjectConcepts(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
//        ServiceUtil.checkLicenseProject(context, httpRequest);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List selects = serviceParameters.getSelects();
        ArgMap serviceArgs = serviceParameters.getServiceArgs();
        String state = (String)serviceArgs.get("state");
        Datacollection datacollection1;
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            datacollection1 = com.gsc.enovia.dpm.projectconcept.gscProjectConcept.getUserProjectConcepts(context, datacollection, selects, state);
        } else {
            datacollection1 = com.gsc.enovia.dpm.projectconcept.gscProjectConcept.getUserProjectConcepts(context, (Datacollection)null, selects, state);
        }

        return datacollection1;
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

    public static Dataobject updateProjectConcept(Context context, String[] var1) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(var1);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject) datacollection.getDataobjects().get(0);
        UpdateActions updateAction = dataobject.getUpdateAction();
        dataobject = updateProjectConcept(context, serviceParameters, dataobject, updateAction);

        return dataobject;
    }

    public static Dataobject updateProjectConcept(Context context, ServiceParameters serviceParameters, Dataobject dataobject, UpdateActions updateActions) throws Exception {
        if (!UpdateActions.CREATE.equals(updateActions) && !UpdateActions.MODIFY.equals(updateActions)) {
            if (UpdateActions.DELETE.equals(updateActions)) {
                DomainObject domBusGoal = new DomainObject(dataobject.getId());
                domBusGoal.deleteObject(context);
                // com.gsc.apps.program.Financials financialItem = new com.gsc.apps.program.FinancialItem(dataobject.getId());
                // financialItem.deleteObject(context);
                dataobject = null;
            }
        } else {
            HashMap<String, String> attrMap = new HashMap<>(5);
            // dpm-services.xml의 field-selectable autosave 설정이 true 인 항목을 조회함
            ServiceDataFunctions.fillUpdates(context, dataobject, serviceParameters.getAutosaveFields(), attrMap);

            String busProjectConceptId;
            String interfaces;

            // type 및 policy 정의
            String name = getTitle(context,dataobject);
            String type = "Project Concept";
            String policy = "Project Concept";
            
            // Table View 를 위해 gscTitle 에 name 동일하게 적용
            attrMap.put("gscTitle",name);

            // estimatedStartDate 처리
            String estimatedStartDate = DataelementMapAdapter.getDataelementValue(dataobject, "estimatedStartDate");
            if(!ProgramCentralUtil.isNullString(estimatedStartDate)){
                // SimpleDateFormat simpleDateFormat = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
                // String estimatedStartDate_simpleformat = simpleDateFormat.format(estimatedStartDate);
                attrMap.put("Task Estimated Start Date",estimatedStartDate);
            }

            // relation 관련
            String businessUnitId = DataelementMapAdapter.getDataelementValue(dataobject, "businessUnitProject");

            if (UpdateActions.CREATE.equals(updateActions)) {
                type = type != null && !type.isEmpty() ? type : dataobject.getType();
                ArgMap serviceArgs = serviceParameters.getServiceArgs();
                String triggerStatus = (String) serviceArgs.get("TriggerStatus");

                if (triggerStatus != null && triggerStatus.equalsIgnoreCase("false")) {
                    Map<String, StringList> interfacesMap = ExtensionUtil.getAutomaticInterfacesByType(context, ProgramCentralConstants.TYPE_FINANCIAL_ITEM);
                    StringList projectSpaceList = interfacesMap != null ? interfacesMap.get(ProgramCentralConstants.TYPE_FINANCIAL_ITEM) : null;
                    if (projectSpaceList != null) {
                        interfaces = projectSpaceList.join(",");
                        attrMap.put("interface", interfaces);
                    }
                }

                // BUS 생성 부분
                busProjectConceptId = createProjectConcept(context, type, name, attrMap, policy);
                // ProjectConcept(emxProjectConcept-performPostProcessActions)
                busProjectConceptId = createProjectConceptJPO(context, dataobject, busProjectConceptId);

                // RelationShip 처리
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectConceptId, "Business Unit Project", businessUnitId, false, false);

                dataobject.setId(busProjectConceptId);

            } else if (UpdateActions.MODIFY.equals(updateActions)) {
                busProjectConceptId = dataobject.getId();

                // RelationShip 처리
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busProjectConceptId, "Business Unit Project", businessUnitId, false, false);

                // 수정 처리
                addToMap(attrMap, "name", name);
                modifyProjectConcept(context, busProjectConceptId, attrMap);

            } else {
                busProjectConceptId = dataobject.getId();

                addToMap(attrMap, "name", name);
                modifyProjectConcept(context, busProjectConceptId, attrMap);
            }

            dataobject = getObject(context, serviceParameters, dataobject.getId(), (String) null, (String) null);
            Dataobject var18 = new Dataobject();
            var18.setUpdateAction(updateActions);

        }

        return dataobject;
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
            var7 = (Dataobject) var6.getData().get(0);
        } else {
            ArrayList<Selectable> var8 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(6);
            var8.add(new Select("physicalid", "physicalid", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("type", "type", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("name", "name", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("revision", "revision", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("originated", "originated", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("description", "description", ExpressionType.BUS, (Format) null, false));
            if (var7 == null && var2 != null && !var2.isEmpty()) {
                var7 = ObjectUtil.print(var0, var2, (PrintData) null, var8);
            }
        }

        return var7;
    }

    public static String createProjectConcept(Context context, String type, String name, Map attrMap, String policy) throws FoundationException {
        try {
            //checkLicenseProjectLead(context, (HttpServletRequest)null);
            ObjectEditUtil.checkNameRules(context, name);
            String typeName = type != null ? type : "Project Concept";
            String policyName = policy != null && !policy.isEmpty() ? policy : "Project Concept";
            gscProjectConcept resourceRequest = new gscProjectConcept();
            resourceRequest.create(context, typeName, name, policyName, (String) null);
            String resourceRequestId = resourceRequest.getId(context);
            ObjectEditUtil.modify(context, resourceRequestId, attrMap, false);
            return resourceRequestId;
        } catch (Exception var9) {
            throw new FoundationException(var9);
        }
    }

    public static String createProjectConceptJPO(Context context, Dataobject dataobject, String busProjectConceptId) throws FoundationException {
        try {

            com.matrixone.apps.program.ProjectConcept projectConcept =
                    (com.matrixone.apps.program.ProjectConcept) DomainObject.newInstance(context,
                            DomainConstants.TYPE_PROJECT_CONCEPT, DomainConstants.PROGRAM);
            String revision = projectConcept.getUniqueName(DomainConstants.EMPTY_STRING);
            DomainObject pc = DomainObject.newInstance(context, busProjectConceptId);
            System.out.println(pc.getInfo(context, "name"));
            projectConcept.startTransaction(context, true);
            projectConcept.setId(busProjectConceptId);

            DomainObject _accessListObject = DomainObject.newInstance(context);
            // create project access list object and connect to project
            _accessListObject.createAndConnect(context,
                    DomainConstants.TYPE_PROJECT_ACCESS_LIST,
                    projectConcept.getUniqueName("PAL-"),
                    DomainConstants.EMPTY_STRING,
                    DomainConstants.POLICY_PROJECT_ACCESS_LIST,
                    context.getVault().getName(),
                    DomainConstants.RELATIONSHIP_PROJECT_ACCESS_LIST,
                    projectConcept,
                    false);

            //Start - Added for new PAL based sequence operation

            String palPhysicalId = ProgramCentralUtil.getPhysicalId(context, _accessListObject.getObjectId(context));

            ProjectSequence pal = new ProjectSequence(context, palPhysicalId);
            pal.assignSequence(context,
                    null,
                    ProgramCentralUtil.getPhysicalId(context, busProjectConceptId),
                    null,
                    null, true);

            //End

            // get person id
            com.matrixone.apps.common.Person person = com.matrixone.apps.common.Person.getPerson(context);

            // get the company id for this context
            com.matrixone.apps.common.Company company = person.getCompany(context);
            StringList objectSelects = new StringList(2);
            objectSelects.add(DomainConstants.SELECT_TYPE);
            objectSelects.add(ProgramCentralConstants.SELECT_PHYSICALID);
            Map projectConceptInfo = projectConcept.getInfo(context, objectSelects);

            String type = (String) projectConceptInfo.get(DomainConstants.SELECT_TYPE);
            String sourceId = (String) projectConceptInfo.get(ProgramCentralConstants.SELECT_PHYSICALID);

            String sCommandStatement = "";

            if ((DomainConstants.TYPE_PROJECT_TEMPLATE).equals(type)) {
                //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
                sCommandStatement = "connect bus $1 preserve relationship $2 to $3";
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, sCommandStatement, company.getObjectId(), DomainConstants.RELATIONSHIP_COMPANY_PROJECT_TEMPLATES, busProjectConceptId);
                //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End

            } else {
                //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
                sCommandStatement = "connect bus $1 preserve relationship $2 to $3";
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, sCommandStatement, company.getObjectId(), DomainConstants.RELATIONSHIP_COMPANY_PROJECT, busProjectConceptId);
                //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End
            }

            String personId = person.getObjectId();
            // projectConcept.addMember(context, personId);
            //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
            sCommandStatement = "connect bus $1 preserve relationship $2 to $3 $4 $5";
            com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, sCommandStatement, busProjectConceptId, DomainConstants.RELATIONSHIP_MEMBER, personId, DomainConstants.ATTRIBUTE_PROJECT_ACCESS, "Project Owner");
            //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End

            String strOwner = DataelementMapAdapter.getDataelementValue(dataobject, "owner");
            String strProjectDate = DataelementMapAdapter.getDataelementValue(dataobject, "ProjectDate");
            String strScheduleFrom = DataelementMapAdapter.getDataelementValue(dataobject, "Schedule From");
            String strDescription = DataelementMapAdapter.getDataelementValue(dataobject, "ProjectDescription");
            String fromRelatedProjects = DataelementMapAdapter.getDataelementValue(dataobject, "fromRelatedProjects");
            String fromProgram = DataelementMapAdapter.getDataelementValue(dataobject, "fromProgram");
            String calendarId = DataelementMapAdapter.getDataelementValue(dataobject, "calendarId");
            String strTimeZone = "-9";
            StringList calendarIds = FrameworkUtil.split(calendarId, "|");
            double clientTimeZone = Double.parseDouble(strTimeZone);
            HashMap requestMap = new HashMap<>();
            requestMap.put("localeObj","ko_KR");

            if (strOwner != null && !strOwner.equals("") && !strOwner.equals("null")) {
                projectConcept.setOwner(context, strOwner);

            }
            // add the originator as a default "Project Owner" member
            // of this project


            final String ATTRIBUTE_SCHEDULE_FROM = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(context, "attribute_ScheduleFrom");

            if(!ProgramCentralUtil.isNullString(strProjectDate)){
                strProjectDate = eMatrixDateFormat.getFormattedInputDate(context, strProjectDate, clientTimeZone, (Locale) requestMap.get("localeObj"));

                //
                // TaskDateRollUp bean algorithm takes into account the time of the the start and finish dates
                // to account for that login following adjustments is done.
                //

                Date dtProjectDate = com.matrixone.apps.domain.util.eMatrixDateFormat.getJavaDate(strProjectDate);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(dtProjectDate);

                if ("Project Start Date".equals(strScheduleFrom)) {
                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                } else if ("Project Finish Date".equals(strScheduleFrom)) {
                    calendar.set(Calendar.HOUR_OF_DAY, 17);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                }

                dtProjectDate = calendar.getTime();
                //Added:02-June-2010:vf2:R210 PRG:IR-031079
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(com.matrixone.apps.domain.util.eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
                //End:02-June-2010:vf2:R210 PRG:IR-031079
                strProjectDate = simpleDateFormat.format(dtProjectDate);

                HashMap attributes = new HashMap(4);
                attributes.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_START_DATE, strProjectDate);
                attributes.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE, strProjectDate);
                attributes.put(ATTRIBUTE_SCHEDULE_FROM, strScheduleFrom);
                attributes.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_DURATION, "0");
                attributes.put(DomainConstants.ATTRIBUTE_ORIGINATOR, context.getUser());
                attributes.put(ProgramCentralConstants.ATTRIBUTE_SOURCE_ID, sourceId);

                projectConcept.setAttributeValues(context, attributes);
            }

            if (strDescription != null) {
                projectConcept.setDescription(context, strDescription);
            }

            if (fromRelatedProjects != null && fromRelatedProjects.equalsIgnoreCase("true")) {
                String relProjectId = (String) busProjectConceptId;
                String relatedProjectRelationship = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(context, DomainObject.SYMBOLIC_relationship_RelatedProjects);
                projectConcept.setRelatedObject(context, relatedProjectRelationship, false, relProjectId);
            }
            if (fromProgram != null && fromProgram.length() != 0) {

                String programProjectRelationship = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(context, DomainObject.SYMBOLIC_relationship_ProgramProject);
                projectConcept.setRelatedObject(context, programProjectRelationship, false, fromProgram);
            }

            //This is a rework done for storing primary ownership on Project Concept to fix IR-272065V6R2014x.
            StringList pcSelects = new StringList();
            pcSelects.add(ProgramCentralConstants.SELECT_COLLABORATIVE_SPACE);
            pcSelects.add(ProgramCentralConstants.SELECT_ORGANIZATION);
            pcSelects.add(ProjectSpace.SELECT_PROJECT_VISIBILITY);

            Map conceptInfo = projectConcept.getInfo(context, pcSelects);
            String visibility = (String) conceptInfo.get(ProjectSpace.SELECT_PROJECT_VISIBILITY);
            String project = (String) conceptInfo.get(ProgramCentralConstants.SELECT_COLLABORATIVE_SPACE);
            String org = (String) conceptInfo.get(ProgramCentralConstants.SELECT_ORGANIZATION);

            if ("Company".equals(visibility) &&
                    (ProgramCentralUtil.isNullString(project) || (ProgramCentralUtil.isNullString(org)))) {
                String defaultOrg = PersonUtil.getActiveOrganization(context);
                defaultOrg = Organization.getCompanyTitleFromName(context, defaultOrg);  // Added to return Title instead of Name
                String defaultProj = PersonUtil.getActiveProject(context);
                projectConcept.setPrimaryOwnership(context, defaultProj, defaultOrg);
            }

            // If only one calendar is selected then that will be connected as Default Calendar
            if(!ProgramCentralUtil.isNullString(calendarId)){
                if (calendarIds.size() == 1) {
                    String defaultCalendarId = calendarIds.get(0) + "|DefaultCalendar";
                    calendarIds.remove(0);
                    calendarIds.add(defaultCalendarId);
                }
                projectConcept.addCalendars(context, calendarIds);
            }

            ContextUtil.commitTransaction(context);

            return busProjectConceptId;
        } catch (Exception var9) {
            throw new FoundationException(var9);
        }
    }

    public static void modifyProjectConcept(Context context, String objectId, Map map) throws FoundationException {
        if (map != null && !map.isEmpty()) {
//            com.gsc.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
            ObjectEditUtil.modify(context, objectId, map, false);
        }
    }

    static void addToMap(Map var0, String var1, String var2) {
        if (var2 != null) {
            var0.put(var1, var2);
        }
    }

    public static String emxProjectConceptBase_performPostProcessActions(Context context, String[] args) throws Exception {
        // Check license while creating Project Concept, if license check fails here
        // the projects will not be created.
//        ComponentsUtil.checkLicenseReserved(context, ProgramCentralConstants.PGE_PRG_LICENSE_ARRAY);

        HashMap programMap = (HashMap) JPO.unpackArgs(args);
        HashMap requestMap = (HashMap) programMap.get("requestMap");
        HashMap paramMap = (HashMap) programMap.get("paramMap");
        String businessUnitId = (String) requestMap.get("BusinessUnitId");
        String programId = (String) requestMap.get("ProgramId");
        String defaultVault = (String) requestMap.get("defaultVault");
        String strProjName = (String) requestMap.get("Name");
        String fromRelatedProjects = (String) requestMap.get("fromRelatedProjects");
        String strDescription = (String) requestMap.get("ProjectDescription");
        String fromProgram = (String) requestMap.get("fromProgram");
        String strOwner = (String) requestMap.get("Owner");
        String objectId = (String) paramMap.get("objectId");
        String strProjectDate = (String) requestMap.get("ProjectDate");
        String strScheduleFrom = (String) requestMap.get("Schedule From");
        String strTimeZone = (String) requestMap.get("timeZone");
        double clientTimeZone = Double.parseDouble(strTimeZone);
        String calendarId = (String) requestMap.get("CalendarOID");
        StringList calendarIds = FrameworkUtil.split(calendarId, "|");


        com.matrixone.apps.program.Program program =
                (com.matrixone.apps.program.Program) DomainObject.newInstance(context,
                        DomainConstants.TYPE_PROGRAM, DomainConstants.PROGRAM);
        com.matrixone.apps.program.ProjectConcept projectConcept =
                (com.matrixone.apps.program.ProjectConcept) DomainObject.newInstance(context,
                        DomainConstants.TYPE_PROJECT_CONCEPT, DomainConstants.PROGRAM);
        String revision = projectConcept.getUniqueName(DomainConstants.EMPTY_STRING);

        try {
            DomainObject pc = DomainObject.newInstance(context, objectId);
            System.out.println(pc.getInfo(context, "name"));
            projectConcept.startTransaction(context, true);
            projectConcept.setId(objectId);
            //PRG:RG6:R213:Mql Injection:parameterized Mql:17-Oct-2011:start
            String sCommandStatement = "modify bus $1 $2 $3 name $4 revision $5";
            com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, sCommandStatement, DomainConstants.TYPE_PROJECT_CONCEPT, strProjName, "", strProjName, revision);
            //PRG:RG6:R213:Mql Injection:parameterized Mql:17-Oct-2011:End

            if (programId != null && !programId.equals("") && !programId.equals("null")) {
                projectConcept.setProgram(context, programId);
            }

            if (businessUnitId != null && !businessUnitId.equals("") && !businessUnitId.equals("null")) {
                projectConcept.setOrganization(context, businessUnitId);
            }

            DomainObject _accessListObject = DomainObject.newInstance(context);
            // create project access list object and connect to project
            _accessListObject.createAndConnect(context,
                    DomainConstants.TYPE_PROJECT_ACCESS_LIST,
                    projectConcept.getUniqueName("PAL-"),
                    DomainConstants.EMPTY_STRING,
                    DomainConstants.POLICY_PROJECT_ACCESS_LIST,
                    context.getVault().getName(),
                    DomainConstants.RELATIONSHIP_PROJECT_ACCESS_LIST,
                    projectConcept,
                    false);

            //Start - Added for new PAL based sequence operation

            String palPhysicalId = ProgramCentralUtil.getPhysicalId(context, _accessListObject.getObjectId(context));

            ProjectSequence pal = new ProjectSequence(context, palPhysicalId);
            pal.assignSequence(context,
                    null,
                    ProgramCentralUtil.getPhysicalId(context, objectId),
                    null,
                    null, true);


            //End


            // get person id
            com.matrixone.apps.common.Person person = com.matrixone.apps.common.Person.getPerson(context);

            // get the company id for this context
            com.matrixone.apps.common.Company company = person.getCompany(context);
            StringList objectSelects = new StringList(2);
            objectSelects.add(DomainConstants.SELECT_TYPE);
            objectSelects.add(ProgramCentralConstants.SELECT_PHYSICALID);
            Map projectConceptInfo = projectConcept.getInfo(context, objectSelects);

            String type = (String) projectConceptInfo.get(DomainConstants.SELECT_TYPE);
            String sourceId = (String) projectConceptInfo.get(ProgramCentralConstants.SELECT_PHYSICALID);

            if ((DomainConstants.TYPE_PROJECT_TEMPLATE).equals(type)) {
                //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
                sCommandStatement = "connect bus $1 preserve relationship $2 to $3";
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, sCommandStatement, company.getObjectId(), DomainConstants.RELATIONSHIP_COMPANY_PROJECT_TEMPLATES, objectId);
                //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End

            } else {
                //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
                sCommandStatement = "connect bus $1 preserve relationship $2 to $3";
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, sCommandStatement, company.getObjectId(), DomainConstants.RELATIONSHIP_COMPANY_PROJECT, objectId);
                //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End
            }

            String personId = person.getObjectId();
            // projectConcept.addMember(context, personId);
            //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
            sCommandStatement = "connect bus $1 preserve relationship $2 to $3 $4 $5";
            com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, sCommandStatement, objectId, DomainConstants.RELATIONSHIP_MEMBER, personId, DomainConstants.ATTRIBUTE_PROJECT_ACCESS, "Project Owner");
            //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End

            if (strOwner != null && !strOwner.equals("") && !strOwner.equals("null")) {
                projectConcept.setOwner(context, strOwner);

            }
            // add the originator as a default "Project Owner" member
            // of this project


            final String ATTRIBUTE_SCHEDULE_FROM = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(context, "attribute_ScheduleFrom");
            strProjectDate = eMatrixDateFormat.getFormattedInputDate(context, strProjectDate, clientTimeZone, (Locale) requestMap.get("localeObj"));


            //
            // TaskDateRollUp bean algorithm takes into account the time of the the start and finish dates
            // to account for that login following adjustments is done.
            //

            Date dtProjectDate = com.matrixone.apps.domain.util.eMatrixDateFormat.getJavaDate(strProjectDate);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dtProjectDate);

            if ("Project Start Date".equals(strScheduleFrom)) {
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            } else if ("Project Finish Date".equals(strScheduleFrom)) {
                calendar.set(Calendar.HOUR_OF_DAY, 17);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }

            dtProjectDate = calendar.getTime();
            //Added:02-June-2010:vf2:R210 PRG:IR-031079
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(com.matrixone.apps.domain.util.eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            //End:02-June-2010:vf2:R210 PRG:IR-031079
            strProjectDate = simpleDateFormat.format(dtProjectDate);

            HashMap attributes = new HashMap(4);
            attributes.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_START_DATE, strProjectDate);
            attributes.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE, strProjectDate);
            attributes.put(ATTRIBUTE_SCHEDULE_FROM, strScheduleFrom);
            attributes.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_DURATION, "0");
            attributes.put(DomainConstants.ATTRIBUTE_ORIGINATOR, context.getUser());
            attributes.put(ProgramCentralConstants.ATTRIBUTE_SOURCE_ID, sourceId);

            projectConcept.setAttributeValues(context, attributes);

            if (strDescription != null) {
                projectConcept.setDescription(context, strDescription);
            }

            if (fromRelatedProjects != null && fromRelatedProjects.equalsIgnoreCase("true")) {
                String relProjectId = (String) requestMap.get("objectId");
                String relatedProjectRelationship = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(context, DomainObject.SYMBOLIC_relationship_RelatedProjects);
                projectConcept.setRelatedObject(context, relatedProjectRelationship, false, relProjectId);
            }
            if (fromProgram != null && fromProgram.length() != 0) {

                String programProjectRelationship = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(context, DomainObject.SYMBOLIC_relationship_ProgramProject);
                projectConcept.setRelatedObject(context, programProjectRelationship, false, fromProgram);
            }

            //This is a rework done for storing primary ownership on Project Concept to fix IR-272065V6R2014x.
            StringList pcSelects = new StringList();
            pcSelects.add(ProgramCentralConstants.SELECT_COLLABORATIVE_SPACE);
            pcSelects.add(ProgramCentralConstants.SELECT_ORGANIZATION);
            pcSelects.add(com.matrixone.apps.program.ProjectSpace.SELECT_PROJECT_VISIBILITY);

            Map conceptInfo = projectConcept.getInfo(context, pcSelects);
            String visibility = (String) conceptInfo.get(ProjectSpace.SELECT_PROJECT_VISIBILITY);
            String project = (String) conceptInfo.get(ProgramCentralConstants.SELECT_COLLABORATIVE_SPACE);
            String org = (String) conceptInfo.get(ProgramCentralConstants.SELECT_ORGANIZATION);

            if ("Company".equals(visibility) &&
                    (ProgramCentralUtil.isNullString(project) || (ProgramCentralUtil.isNullString(org)))) {
                String defaultOrg = PersonUtil.getActiveOrganization(context);
                defaultOrg = Organization.getCompanyTitleFromName(context, defaultOrg);  // Added to return Title instead of Name
                String defaultProj = PersonUtil.getActiveProject(context);
                projectConcept.setPrimaryOwnership(context, defaultProj, defaultOrg);
            }

            // If only one calendar is selected then that will be connected as Default Calendar
            if (calendarIds.size() == 1) {
                String defaultCalendarId = calendarIds.get(0) + "|DefaultCalendar";
                calendarIds.remove(0);
                calendarIds.add(defaultCalendarId);
            }
            projectConcept.addCalendars(context, calendarIds);

            ContextUtil.commitTransaction(context);

            return objectId;
        } catch (Exception ee) {
            throw new Exception(ee);
        }
    }
}
