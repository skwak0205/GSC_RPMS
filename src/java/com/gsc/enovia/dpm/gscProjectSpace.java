package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUserException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.MqlUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.LicenseUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.StringUtil;
import com.matrixone.apps.common.Task;
import com.matrixone.apps.common.TaskDateRollup;
import com.matrixone.apps.common.VaultHolder;
import com.matrixone.apps.common.WorkCalendar;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class gscProjectSpace extends com.matrixone.apps.program.ProjectSpace implements VaultHolder {
    private static final long serialVersionUID = 1L;
    protected static final BigInteger MAX_OBJECTS = new BigInteger("500");
    protected static final String TYPE_PERSON = "Person";
    protected static final String TYPE_PROJECT_SPACE = "Project Space";
    protected static final String TYPE_MILESTONE = "Milestone";
    protected static final String TYPE_GATE = "Gate";
    protected static final String TYPE_PROJECT_ACCESS_LIST = "Project Access List";
    protected static final String RELATIONSHIP_PROGRAM_PROJECT = "Program Project";
    protected static final String STATE_WORKSPACE_VAULT_EXISTS = "Exists";
    protected static final String STATE_PROJECT_REVIEW_PRELIMINARY = "Preliminary";
    protected static final String STATE_PROJECT_REVIEW_INREVIEW = "In Review";
    protected static final String STATE_PROJECT_REVIEW_COMPLETE = "Complete";
    protected static final String STATE_MILESTONE_PRELIMINARY = "Create";
    protected static final String STATE_MILESTONE_INREVIEW = "Review";
    protected static final String STATE_MILESTONE_COMPLETE = "Complete";
    protected static final String STATE_PROJECT_SPACE_CREATE = "Create";
    protected static final String STATE_PROJECT_SPACE_ASSIGN = "Assign";
    protected static final String STATE_PROJECT_SPACE_ACTIVE = "Active";
    protected static final String STATE_PROJECT_SPACE_REVIEW = "Review";
    protected static final String STATE_PROJECT_SPACE_COMPLETE = "Complete";
    protected static final String STATE_PROJECT_SPACE_ARCHIVE = "Archive";
    protected static final String STATE_RISK_CREATE = "Create";
    protected static final String STATE_RISK_ASSIGN = "Assign";
    protected static final String STATE_RISK_ACTIVE = "Active";
    protected static final String STATE_RISK_REVIEW = "Review";
    protected static final String STATE_RISK_COMPLETE = "Complete";
    protected static final String STATE_ISSUE_CREATE = "Create";
    protected static final String STATE_ISSUE_ASSIGN = "Assign";
    protected static final String STATE_ISSUE_ACTIVE = "Active";
    protected static final String STATE_ISSUE_REVIEW = "Review";
    protected static final String STATE_ISSUE_CLOSED = "Closed";
    protected static final String ATTRIBUTE_PROJECT_SCHEDULE_FROM = "Schedule From";
    protected static final String ATTRIBUTE_PROJECT_SCHEDULE_FROM_RANGE_PSD = "Project Start Date";
    protected static final String ATTRIBUTE_PROJECT_SCHEDULE_FROM_RANGE_PFD = "Project Finish Date";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ASAP = "As Soon As Possible";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ALAP = "As Late As Possible";
    public static final Select SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE;
    public static final Select SELECT_TASK_ESTIMATED_DURATION_INPUT_UNIT;
    protected static final String ATTRIBUTE_START_DATE = "estimatedStartDate";
    protected static final String ATTRIBUTE_END_DATE = "dueDate";
    protected static final String ATTRIBUTE_ACTUAL_START_DATE = "actualStartDate";
    protected static final String ATTRIBUTE_ACTUAL_END_DATE = "actualFinishDate";

    static StringList _getIssueStates() {
        StringList var0 = new StringList(5);
        var0.add("Create");
        var0.add("Assign");
        var0.add("Active");
        var0.add("Review");
        var0.add("Closed");
        return var0;
    }

    static StringList _getRiskStates() {
        StringList var0 = new StringList(5);
        var0.add("Create");
        var0.add("Assign");
        var0.add("Active");
        var0.add("Review");
        var0.add("Complete");
        return var0;
    }

    static StringList _getProjectStates() {
        StringList var0 = new StringList(6);
        var0.add("Create");
        var0.add("Assign");
        var0.add("Active");
        var0.add("Review");
        var0.add("Complete");
        var0.add("Archive");
        return var0;
    }

    static StringList _getMilestoneStates() {
        StringList var0 = new StringList(6);
        var0.add("Create");
        var0.add("Review");
        var0.add("Complete");
        return var0;
    }

    public gscProjectSpace() {
    }

    public gscProjectSpace(String var1) throws Exception {
        super(var1);
    }

    public gscProjectSpace(BusinessObject var1) throws Exception {
        super(var1);
    }

    public static String getStateWhereClause(Context var0, String var1, String var2, StringList var3) {
        String var4 = new String();
        StringBuffer var5 = new StringBuffer(100);
        Object var6 = new ArrayList();
        if ("all".equalsIgnoreCase(var2)) {
            var4 = var5.toString();
        } else {
            try {
                var6 = StringUtil.splitString(var2, ",");
            } catch (Exception var9) {
                var9.printStackTrace();
            }

            if (!((List)var6).isEmpty()) {
                var5.append("(");
                Iterator var7 = ((List)var6).iterator();

                while(var7.hasNext()) {
                    String var8 = (String)var7.next();
                    var5.append("current == \"");
                    var5.append(var8);
                    var5.append("\"");
                    if (var7.hasNext()) {
                        var5.append(" || ");
                    }
                }

                var5.append(")");
            }

            if ("()".equalsIgnoreCase(var4.toString())) {
                var5.insert(0, var1);
            } else if (!var1.isEmpty()) {
                var5.insert(0, " && ");
                var5.insert(0, var1);
            }

            var4 = var5.toString();
        }

        return var4;
    }

    protected static String checkProjectAccess(Context var0, String var1) throws FoundationException {
        String var2 = "read";
        String var3 = "";

        try {
            MqlUtil.mqlCommand(var0, "print bus $1 select $2 dump", new String[]{var1, "type"});
        } catch (FoundationException var5) {
            if (var5.getMessage().contains("BusinessObject")) {
                throw new FoundationException("DPM110: You do not have access to this Business Object");
            }
        }

        return var2;
    }

    public static Dataobject getProjectInfo(Context var0, String var1, List var2) throws FoundationException {
        Dataobject var3 = new Dataobject();
        var3.setId(var1);
        Datacollection var4 = new Datacollection();
        var4.getDataobjects().add(var3);
        getProjectInfo(var0, var4, var2);
        return (Dataobject)var4.getDataobjects().get(0);
    }

    public static void getProjectInfo(Context var0, Datacollection var1, List var2) throws FoundationException {
        ServiceUtil.checkLicenseProject(var0, (HttpServletRequest)null);
        if (var2 != null && !var2.isEmpty()) {
            if (var1.getDataobjects().size() > 0) {
                ObjectUtil.print(var0, var1, (PrintData)null, var2, true);
            }

        } else {
            throw new FoundationException("You must provide some selectables to ProjectSpace.getProjectInfo");
        }
    }

    private static String getCompleteFilterClause(Context var0, String var1, String var2) {
        String var3 = "";
        String var4 = "";
        var2 = var2 == null ? "" : var2.replaceAll("current != Complete AND", "");
        if (var1 != null && !var1.equalsIgnoreCase("all")) {
            if (!var1.equalsIgnoreCase("none")) {
                int var5 = Integer.valueOf(var1);
                LocalDate var6 = LocalDate.now();
                LocalDate var7 = var6.plusDays((long)(-var5));
                ZoneId var8 = ZoneId.systemDefault();
                Date var9 = Date.from(var7.atStartOfDay(var8).toInstant());
                var3 = FormatUtil.formatMxDate(var0, var9, (SimpleDateFormat)null);
            }

            if (var3 != null) {
                if (!var3.isEmpty()) {
                    var4 = "(attribute[Task Actual Finish Date]=='' || attribute[Task Actual Finish Date]>='" + var3 + "' )";
                } else {
                    var4 = String.format("current != %s ", "Complete");
                }

                if (var2 != null && !var2.isEmpty()) {
                    var2 = var2 + " AND " + var4;
                } else {
                    var2 = var4;
                }
            }
        }

        return var2;
    }

    protected static Datacollection getUserProjects(Context var0, Datacollection datacollection, boolean var2, boolean isStructured, List selects, String var5) throws FoundationException {
        return getUserProjects(var0, datacollection, var2, isStructured, selects, var5, "");
    }

    protected static Datacollection getUserProjects(Context context, Datacollection datacollection, boolean var2, boolean isStructrued, List selects, String state, String var6) throws FoundationException {
        return getUserProjects(context, datacollection, var2, isStructrued, selects, state, "", (String)null, (String) null);
    }

    protected static Datacollection getUserProjects(Context context, Datacollection datacollection, boolean isOwned, boolean isStructured, List list, String state, String excludeTypes, String completed, String businessUnitId) throws FoundationException {
        boolean hasData = false;
        String typeProjectSpace = PropertyUtil.getSchemaName(context, "type_ProjectSpace");
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            hasData = true;
        }

        if (!hasData || list != null && !list.isEmpty()) {
            Datacollection datacollection1 = new Datacollection();
            List dataObjList;
            Dataobject dataobject;
            ExpandData expandData;
            if (hasData) {
                try {
                    getProjectInfo(context, datacollection, list);
                    dataObjList = datacollection.getDataobjects();
                    Iterator objItr = dataObjList.iterator();

                    while(objItr.hasNext()) {
                        dataobject = (Dataobject)objItr.next();
                        if (dataobject.getId().isEmpty()) {
                            throw new FoundationException("DPM100: Invalid Project Space ID. Business Object Does Not Exist");
                        }

                        datacollection1.getDataobjects().add(dataobject);
                    }
                } catch (Exception ex) {
                    if (ex.getMessage().contains("Business Object Does Not Exist")) {
                        throw new FoundationException("DPM100: Invalid Project Space ID. Business Object Does Not Exist");
                    }

                    throw ex;
                }
            } else {
                String objectWhere = "";
                if (state != null && !state.isEmpty()) {
                    objectWhere = getStateWhereClause(context, "", state, _getProjectStates());
                } else {
                    objectWhere = String.format("current != %s","Archive");
                }
                if(!"".equals(objectWhere)) objectWhere += " AND ";
                objectWhere = objectWhere + " type != 'Project Baseline' AND type != 'Project Snapshot'";
                if (excludeTypes != null && !excludeTypes.isEmpty()) {
                    List var23 = StringUtil.splitString(excludeTypes, ",");
                    String var14;
                    if (!var23.isEmpty()) {
                        for(Iterator var25 = var23.iterator(); var25.hasNext(); objectWhere = objectWhere + " AND type != '" + var14 + "'") {
                            var14 = (String)var25.next();
                        }
                    }
                }

                //if (isOwned) {
                //    objectWhere = String.format("%s AND owner == %s", objectWhere, context.getUser());
                //}

                // 230425 businessUnitId 조건 추가
                if (businessUnitId != null && !businessUnitId.isEmpty()) {
                    if(objectWhere != "") {
                        objectWhere = String.format("%s AND (to[Business Unit Project].from.physicalid == \"%s\")", objectWhere, businessUnitId);
                    }else{
                        objectWhere = String.format("%s (to[Business Unit Project].from.physicalid == \"%s\")", objectWhere, businessUnitId);
                    }
                }

                if (completed != null && !completed.isEmpty()) {
                    objectWhere = getCompleteFilterClause(context, completed, objectWhere);
                }
                QueryData queryData = new QueryData();
                queryData.setTypePattern(typeProjectSpace);
                System.out.println("whereClause = " + objectWhere);
                queryData.setWhereExpression(objectWhere);
                queryData.setLimit(gscProjectSpace.MAX_OBJECTS);
                datacollection1 = ObjectUtil.query(context, queryData, list);
				if (isOwned) {
					expandData = new ExpandData();
					expandData.setRelationshipPattern(RELATIONSHIP_MEMBER);
					expandData.setLimit(Short.valueOf((short)0));
					expandData.setRecurseToLevel(BigDecimal.valueOf(1L));
					expandData.setGetFrom(false);
					expandData.setGetTo(true);
					expandData.setTypePattern("Project Space");
					expandData.setObjectWhere(objectWhere);
					dataobject = new Dataobject();
					System.out.println("ObjectWhere : " + objectWhere);
					try {
						DomainObject personObject = PersonUtil.getPersonObject(context);
						String personObj = personObject.getInfo(context, "id");
						dataobject.setId(personObj);
					} catch (Exception var20) {
						var20.printStackTrace();
						throw new FoundationException(var20.getMessage());
					}
	
					datacollection1 = ObjectUtil.expand(context, dataobject, expandData, list);
				}
            }

            if (isStructured && datacollection1 != null && !datacollection1.getDataobjects().isEmpty()) {
                dataObjList = datacollection1.getDataobjects();
                expandData = new ExpandData();
                String var26 = "(type == \"" + typeProjectSpace + "\")";
                expandData.setGetFrom(true);
                expandData.setGetTo(false);
                expandData.setLimit(Short.valueOf((short)0));
                expandData.setObjectWhere(var26);
                expandData.setPreserveLevel(true);
                expandData.setPreventDuplicates(true);
                expandData.setRecurseToLevel(new BigDecimal(10));
                expandData.setRecurseLevels("10");
                expandData.setRelationshipPattern(PropertyUtil.getSchemaName(context, "relationship_Subtask"));
                Datacollection var28 = new Datacollection();
                Iterator var29 = dataObjList.iterator();

                Dataobject var16;
                String var17;
                while(var29.hasNext()) {
                    var16 = (Dataobject)var29.next();
                    var17 = var16.getId();
                    expandData.setIdField(var17);
                    Datacollection var18 = ObjectUtil.expand(context, var16, expandData, list);
                    if (!var18.getDataobjects().isEmpty()) {
                        var16.getChildren().addAll(var18.getDataobjects());
                        var28.getDataobjects().addAll(var18.getDataobjects());
                    }
                }

                var29 = var28.getDataobjects().iterator();

                while(true) {
                    while(var29.hasNext()) {
                        var16 = (Dataobject)var29.next();
                        var17 = var16.getId();
                        Iterator var30 = dataObjList.iterator();

                        while(var30.hasNext()) {
                            Dataobject var19 = (Dataobject)var30.next();
                            if (var17.equalsIgnoreCase(var19.getId())) {
                                dataObjList.remove(var19);
                                break;
                            }
                        }
                    }

                    return datacollection1;
                }
            } else {
                return datacollection1;
            }
        } else {
            throw new FoundationException("You must provide some selectables to ProjectSpace.getUserProjects");
        }
    }
    protected static Datacollection getPolyDataProjects(Context context,List list,String state) throws FoundationException {
        boolean hasData = false;
        String typeProjectSpace = PropertyUtil.getSchemaName(context, "type_ProjectSpace");


        if (!hasData || list != null && !list.isEmpty()) {
            Datacollection datacollection1 = new Datacollection();
            List dataObjList;
            Dataobject dataobject;
            ExpandData expandData;
            if (hasData) {

            } else {
                String objectWhere = "";
                if (state != null && !state.isEmpty()) {
                    objectWhere = getStateWhereClause(context, "", state, _getProjectStates());
                } else {
                    objectWhere = String.format("current != %s AND current != %s", "Complete", "Archive");
                }

                objectWhere = objectWhere + " AND type != 'Project Baseline' AND type != 'Project Snapshot'";

                expandData = new ExpandData();
                expandData.setRelationshipPattern(RELATIONSHIP_MEMBER);
                expandData.setLimit(Short.valueOf((short)0));
                expandData.setRecurseToLevel(BigDecimal.valueOf(1L));
                expandData.setGetFrom(false);
                expandData.setGetTo(true);
                expandData.setTypePattern("Project Space");
                expandData.setObjectWhere(objectWhere);
                dataobject = new Dataobject();

                try {
                    DomainObject personObject = PersonUtil.getPersonObject(context);
                    String personObj = personObject.getInfo(context, "id");
                    dataobject.setId(personObj);
                } catch (Exception var20) {
                    var20.printStackTrace();
                    throw new FoundationException(var20.getMessage());
                }

                datacollection1 = ObjectUtil.expand(context, dataobject, expandData, list);
            }
             return datacollection1;
        } else {
            throw new FoundationException("You must provide some selectables to ProjectSpace.getUserProjects");
        }
    }
    public static Datacollection getProgramProjects(Context var0, String var1, List var2) throws FoundationException {
        ServiceUtil.checkLicenseDPM(var0);
        Dataobject var3 = new Dataobject();
        var3.setId(var1);
        ExpandData var4 = new ExpandData();
        var4.setRelationshipPattern("Program Project");
        var4.setLimit(Short.valueOf((short)0));
        var4.setRecurseToLevel(BigDecimal.valueOf(1L));
        var4.setGetFrom(true);
        return ObjectUtil.expand(var0, var3, var4, var2);
    }

    public static String createProjectSpace(Context var0, String var1, String var2, Map var3) throws FoundationException {
        return createProjectSpace(var0, var1, var2, var3, (String)null);
    }
    protected static void checkLicenseProjectLead(Context var0, HttpServletRequest var1) throws FoundationException {
        try {
            LicenseUtil.checkLicenseReserved(var0, "ENO_PRF_TP", var1);
        } catch (Exception var4) {
            String var3 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", "emxProjectService.Error.NoPRFlicense", var0.getLocale());
            throw new FoundationUserException(var3);
        }
    }
    public static String createProjectSpace(Context context, String type, String title, Map attrMap, String policy) throws FoundationException {
        try {
            checkLicenseProjectLead(context, (HttpServletRequest)null);
            ObjectEditUtil.checkNameRules(context, title);
            String typeName = type != null ? type : "Project Space";
            String policyName = policy != null && !policy.isEmpty() ? policy : com.matrixone.apps.program.ProjectSpace.getDefaultPolicy(context);
            com.matrixone.apps.program.ProjectSpace projectSpace = new com.matrixone.apps.program.ProjectSpace();
            projectSpace.create(context, typeName, title, policyName, (String)null);
            String projectSpaceId = projectSpace.getId(context);
            ObjectEditUtil.modify(context, projectSpaceId, attrMap, false);
            return projectSpaceId;
        } catch (Exception var9) {
            throw new FoundationException(var9);
        }
    }

    public static void modifyProjectSpace(Context context, String objectId, Map map) throws FoundationException {
        if (map != null && !map.isEmpty()) {
            ServiceUtil.checkLicenseDPM(context);
            ObjectEditUtil.modify(context, objectId, map, false);
        }

    }

    public static void deleteProjectSpace(Context context, String var1) throws FoundationException {
        ServiceUtil.checkLicenseDPM(context);
        ObjectEditUtil.delete(context, var1);
    }

    private static Datacollections getCommonExpand(Context var0, String var1, String var2, String var3, Datacollection var4, List var5, boolean var6, boolean var7, boolean var8, boolean var9, String var10, String var11) throws FoundationException {
        if (var4 != null && !var4.getDataobjects().isEmpty()) {
            if (var5 != null && !var5.isEmpty()) {
                String var12 = "";
                String var13 = "type==\"" + var1 + "\"";
                if (var10 != null && !var10.isEmpty()) {
                    var12 = getStateWhereClause(var0, var13, var10, _getRiskStates());
                } else {
                    var12 = var13 + " && " + var11;
                }

                Datacollections var14 = new Datacollections();

                try {
                    if (var3 != null && !var3.isEmpty()) {
                        Dataobject var18 = ObjectUtil.print(var0, var3, (PrintData)null, var5);
                        Datacollection var16 = new Datacollection();
                        var16.getDataobjects().add(var18);
                        var14.getDatacollections().add(var16);
                    } else {
                        ExpandData var15 = new ExpandData();
                        var15.setGetTo(var6);
                        var15.setGetFrom(var7);
                        var15.setRelationshipPattern(var2);
                        var15.setObjectWhere(var12);
                        var14 = ObjectUtil.expand(var0, var4, var15, var5);
                    }

                    return var14;
                } catch (Exception var17) {
                    var17.printStackTrace();
                    throw new FoundationException(var17.getMessage());
                }
            } else {
                throw new FoundationException("You must provide some selectables to ProjectSpace.getCommon");
            }
        } else {
            throw new FoundationException("No parent objects in ProjectSpace.getCommon");
        }
    }

    protected static Datacollections getProjectRisks(Context var0, Datacollection var1, String var2, boolean var3, boolean var4, List var5, String var6) throws FoundationException {
        String var7 = String.format("current != %s", "Complete");
        Datacollections var8 = getCommonExpand(var0, PropertyUtil.getSchemaName(var0, "type_Risk"), PropertyUtil.getSchemaName(var0, "relationship_Risk"), var2, var1, var5, false, true, var3, var4, var6, var7);
        return var8;
    }

    protected static Datacollections getProjectOpportunities(Context var0, Datacollection var1, String var2, boolean var3, boolean var4, List var5, String var6) throws FoundationException {
        String var7 = String.format("current != %s", "Complete");
        Datacollections var8 = getCommonExpand(var0, PropertyUtil.getSchemaName(var0, "type_Opportunity"), PropertyUtil.getSchemaName(var0, "relationship_Risk"), var2, var1, var5, false, true, var3, var4, var6, var7);
        return var8;
    }

    protected static Datacollections getProjectObjectsFromPAL(Context var0, String var1, Datacollection var2, String var3, boolean var4, boolean var5, List var6, String var7) throws FoundationException {
        String var8 = String.format("current != %s", "Complete");
        String var9 = PropertyUtil.getSchemaName(var0, "type_ProjectAccessList");
        String var10 = PropertyUtil.getSchemaName(var0, "relationship_ProjectAccessList");
        String var11 = "type==\"" + var9 + "\"";
        Datacollections var12 = new Datacollections();

        try {
            ExpandData var13 = new ExpandData();
            var13.setGetTo(true);
            var13.setGetFrom(false);
            var13.setRelationshipPattern(var10);
            var13.setObjectWhere(var11);
            Iterator var14 = var2.getDataobjects().iterator();

            while(var14.hasNext()) {
                Dataobject var15 = (Dataobject)var14.next();
                Datacollection var16 = ObjectUtil.expand(var0, var15, var13, var6);
                Datacollections var17 = getCommonExpand(var0, var1, PropertyUtil.getSchemaName(var0, "relationship_ProjectAccessKey"), var3, var16, var6, false, true, var4, var5, var7, var8);
                var12.getDatacollections().add(var17.getDatacollections().get(0));
            }

            return var12;
        } catch (Exception var18) {
            var18.printStackTrace();
            throw new FoundationException(var18.getMessage());
        }
    }

    protected static Datacollections getProjectIssues(Context var0, Datacollection var1, String var2, boolean var3, boolean var4, List var5, String var6) throws FoundationException {
        String var7 = String.format("current != %s", "Closed");
        Datacollections var8 = getCommonExpand(var0, PropertyUtil.getSchemaName(var0, "type_Issue"), PropertyUtil.getSchemaName(var0, "relationship_Issue"), var2, var1, var5, true, false, var3, var4, var6, var7);
        return var8;
    }

    protected static Datacollection getSubProjects(Context var0, Dataobject var1) throws FoundationException {
        ExpandData var2 = new ExpandData();
        var2.setRelationshipPattern(PropertyUtil.getSchemaName(var0, "relationship_Subtask"));
        var2.setTypePattern("Project Space,Project Concept");
        var2.setGetFrom(true);
        var2.setRecurseToLevel(BigDecimal.valueOf(-2L));
        ArrayList var3 = new ArrayList(1);
        var3.add(ServiceConstants.SELECTABLE_OBJECTID);
        Datacollection var4 = ObjectUtil.expand(var0, var1, var2, var3);
        return var4;
    }

    protected static String addProjectMember(Context context, String parentId, String dataObjectId, Map map) throws FoundationException {
        String var4 = ObjectEditUtil.connect(context, parentId, RELATIONSHIP_MEMBER, dataObjectId, map, false);
        return var4;
    }
    protected static String addRelatedProjects(Context context, String parentId, String dataObjectId, Map map) throws FoundationException {
        String var4 = ObjectEditUtil.connect(context, parentId, "Related Projects", dataObjectId, map, false);
        return var4;
    }
    public static Dataobject addProjectMemberByName(Context context, String parentId, String var2, Map map) throws FoundationException {
        ArrayList var4 = new ArrayList(1);
        var4.add(ServiceConstants.SELECTABLE_OBJECTID);
        Datacollection var5 = getPersonInfo(context, var2, var4, 1);
        if (var5.getDataobjects().isEmpty()) {
            String var9 = PropertyUtil.getTranslatedValue(context, "CollaborativeTasks", "emxCollaborativeTasks.Error.AssigneeInvalid", context.getLocale());
            var9 = var9.replace("%ASSIGNEE%", var2);
            throw new FoundationUserException(var9);
        } else {
            Dataobject dataobject = (Dataobject)var5.getDataobjects().get(0);
            String dataObjectId = dataobject.getId();
            String relId = addProjectMember(context, parentId, dataObjectId, map);
            dataobject.setRelId(relId);
            return dataobject;
        }
    }

    protected static void modifyProjectMember(Context var0, String var1, String var2, Map var3) throws FoundationException {
        ObjectEditUtil.modifyConnection(var0, var1, RELATIONSHIP_MEMBER, var2, var3, false);
    }

    protected static void removeProjectMember(Context var0, String var1, String var2) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var1, RELATIONSHIP_MEMBER, var2);
    }
    protected static void removeRelatedProjects(Context context, String parentId, String projectId) throws FoundationException {
        ObjectEditUtil.disconnect(context, parentId, "Related Projects", projectId);
    }
    protected static Datacollection getPersonInfo(Context var0, String var1, List var2, int var3) throws FoundationException {
        QueryData var4 = new QueryData();
        var4.setTypePattern("Person");
        var4.setNamePattern(var1);
        var4.setLimit(BigInteger.valueOf((long)new Integer(var3)));
        Datacollection var5 = ObjectUtil.query(var0, var4, var2);
        return var5;
    }

    public static List performRollup(Context var0, Dataobject var1, Boolean var2) throws FoundationException {
        if (var2 != null && var2) {
            ContextUtil.setTtriggersMode(var0, false);
        }

        ContextUtil.startTransaction(var0, true);

        try {
            String var3 = var1.getId();
            Task var4 = new Task(var3);
            String var5 = var4.getInfo(var0, "id");
            StringList var6 = new StringList();
            Datacollection var7 = getSubProjects(var0, var1);
            Iterator var8 = var7.getDataobjects().iterator();

            while(var8.hasNext()) {
                Dataobject var9 = (Dataobject)var8.next();
                var6.add(var9.getId());
            }

            var6.add(var5);
            Map var16 = TaskDateRollup.rolloutProject(var0, var6, false);
            List var17 = (List)var16.get("impactedObjectIds");
            ContextUtil.commitTransaction(var0);
            List var10 = var17;
            return var10;
        } catch (Exception var14) {
            ContextUtil.abortTransaction(var0);
            throw new FoundationException(var14);
        } finally {
            if (var2 != null && var2) {
                ContextUtil.setTtriggersMode(var0, true);
            }

        }
    }

    public static Map getupdatedData(Context var0, String var1, String var2) throws FoundationException {
        HashMap var3 = new HashMap();
        String var4 = "";
        boolean var5 = false;

        try {
            DomainObject var17 = new DomainObject(var1);
            String var18 = var17.getInfo(var0, "physicalid");
            var3.put("physicalId", var18);
            return var3;
        } catch (Exception var16) {
            var5 = true;
            if (var5) {
                String var6 = "";
                boolean var7 = false;

                try {
                    String var8 = "name=='" + var2 + "'";
                    String var9 = "temp query bus $1 $2 $3 where $4 select $5 dump $6";
                    String var10 = MqlUtil.mqlCommand(var0, var9, new String[]{"Project Space", "*", "*", var8, "physicalid", "|"});
                    StringList var11 = FrameworkUtil.split(var10, "|");
                    String var12 = "emxProgramCentral.Common.NoProjectFoundWithTitle";
                    if (var11.size() == 0) {
                        var7 = true;
                    } else if (var11.size() > 4) {
                        var12 = "emxProgramCentral.Common.MoreThanOneProjectExistWithTitle";
                        var7 = true;
                    } else if (var11.size() == 4) {
                        String var13 = (String)var11.get(3);
                        if (UIUtil.isNotNullAndNotEmpty(var13) && !var13.equalsIgnoreCase(var1)) {
                            var3.put("physicalId", var13);
                        }
                    }

                    if (var7) {
                        try {
                            var6 = EnoviaResourceBundle.getProperty(var0, "ProgramCentral", var12, var0.getLocale().getLanguage());
                            var6 = FrameworkUtil.findAndReplace(var6, "{0}", var2);
                            var3.put("Error", var6);
                        } catch (Exception var14) {
                            var6 = "";
                            var3.put("Error", var6);
                            var14.printStackTrace();
                        }
                    }
                } catch (FoundationException var15) {
                    var3.put("Error", "Error");
                }
            }

            if (ContextUtil.isTransactionActive(var0)) {
                ContextUtil.abortTransaction(var0);
                ContextUtil.startTransaction(var0, true);
            }

            return var3;
        }
    }

    private static String formatDate(Context var0, String var1) throws Exception {
        if (ProgramCentralUtil.isNullString(var1)) {
            return var1;
        } else {
            Date var2 = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(var1);
            SimpleDateFormat var3 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            String var4 = var3.format(var2);
            return var4;
        }
    }

    public static String computePredictiveActualFinishDate(Context var0, Dataobject var1) throws Exception {
        return computePredictiveActualFinishDate(var0, var1, false);
    }

    public static String computePredictiveActualFinishDate(Context context, Dataobject dataobject, boolean var2) throws Exception {
        SimpleDateFormat var3 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
        Date var4 = ProgramCentralUtil.removeTimeFromDate(context, new Date());
        String var5 = dataobject.getId();
        boolean var6 = false;
        String var7 = "";
        String var8 = "";
        Object var9 = null;
        Object var10 = null;
        String estimatedDuration = DataelementMapAdapter.getDataelementValue(dataobject, "estimatedDuration");
        String percentComplete = DataelementMapAdapter.getDataelementValue(dataobject, "percentComplete");
        String estimatedStartDate = formatDate(context, DataelementMapAdapter.getDataelementValue(dataobject, "estimatedStartDate"));
        String dueDate = formatDate(context, DataelementMapAdapter.getDataelementValue(dataobject, "dueDate"));
        String actualStartDate = formatDate(context, DataelementMapAdapter.getDataelementValue(dataobject, "actualStartDate"));
        String actualFinishDate = formatDate(context, DataelementMapAdapter.getDataelementValue(dataobject, "actualFinishDate"));
        String actualDuration = DataelementMapAdapter.getDataelementValue(dataobject, "actualDuration");
        String policy = DataelementMapAdapter.getDataelementValue(dataobject, "policy");
        String percentComplete1 = DataelementMapAdapter.getDataelementValue(dataobject, "percentComplete");
        String calendarId = DataelementMapAdapter.getDataelementValue(dataobject, "calendarId");
        String calendarDate = DataelementMapAdapter.getDataelementValue(dataobject, "calendarDate");
        if (ProgramCentralConstants.POLICY_PROJECT_REVIEW.equalsIgnoreCase(policy) && ProgramCentralUtil.isNotNullString(actualFinishDate)) {
            actualStartDate = actualFinishDate;
        }

        if (ProgramCentralUtil.isNotNullString(actualStartDate)) {
            var6 = true;
        }

        Date var35;
        if ("100.0".equalsIgnoreCase(percentComplete1) && ProgramCentralUtil.isNotNullString(actualStartDate)) {
            var35 = null;
            Date var36;
            double var37;
            if (ProgramCentralUtil.isNullString(actualFinishDate)) {
                var36 = eMatrixDateFormat.getJavaDate(actualStartDate);
                var37 = com.matrixone.apps.program.Task.parseToDouble(estimatedDuration);
                if (var4.compareTo(ProgramCentralUtil.removeTimeFromDate(context, var36)) < 0) {
                    var35 = computeFinishDate(context, var5, var36, var37, calendarId, calendarDate);
                } else {
                    var35 = setTime(context, var5, var4, calendarId, calendarDate, true);
                }

                actualFinishDate = var3.format(var35);
            }

            if (ProgramCentralUtil.isNullString(actualDuration) || "0.0".equalsIgnoreCase(actualDuration)) {
                var36 = eMatrixDateFormat.getJavaDate(actualStartDate);
                var35 = eMatrixDateFormat.getJavaDate(actualFinishDate);
                var37 = computeDuration(context, var5, var36, var35, calendarId, calendarDate);
                actualDuration = Double.toString(var37);
            }

            var8 = actualFinishDate;
        } else if (!"0.0".equalsIgnoreCase(percentComplete1) && !ProgramCentralUtil.isNullString(actualStartDate)) {
            if (ProgramCentralUtil.isNullString(actualStartDate)) {
                actualStartDate = estimatedStartDate;
            }

            var35 = eMatrixDateFormat.getJavaDate(actualStartDate);
            double var23 = com.matrixone.apps.program.Task.parseToDouble(estimatedDuration);
            double var38 = com.matrixone.apps.program.Task.parseToDouble(percentComplete1);
            Date var27 = null;
            double var28;
            if (var4.compareTo(ProgramCentralUtil.removeTimeFromDate(context, var35)) < 0) {
                var27 = computeFinishDate(context, var5, var35, var23, calendarId, calendarDate);
                var28 = var23 * var38 / 100.0;
                var8 = var3.format(var27);
            } else {
                var28 = 0.0;
                double var30 = 0.0;
                Date var32;
                if (var2) {
                    var28 = var23 / 100.0 * var38;
                    var30 = var23 - var28;
                } else {
                    var32 = setTime(context, var5, var4, calendarId, calendarDate, true);
                    var28 = computeDuration(context, var5, var35, var32, calendarId, calendarDate);
                    --var28;
                    double var33 = var28 * (100.0 / var38);
                    var30 = var33 - var28;
                }

                if (var30 <= 0.0) {
                    var30 = 1.0;
                }

                var32 = setTime(context, var5, var4, calendarId, calendarDate, false);
                var27 = computeFinishDate(context, var5, var32, var30, calendarId, calendarDate);
                var8 = var3.format(var27);
            }
        } else {
            if (ProgramCentralUtil.isNotNullString(actualStartDate)) {
                double var22 = com.matrixone.apps.program.Task.parseToDouble(estimatedDuration);
                Date var24 = eMatrixDateFormat.getJavaDate(actualStartDate);
                if (var2 && ProgramCentralUtil.removeTimeFromDate(context, var24).compareTo(var4) <= 0) {
                    var24 = var4;
                }

                boolean var25 = "Project Review".equalsIgnoreCase(policy);
                var24 = setTime(context, var5, var24, calendarId, calendarDate, var25);
                Date var26 = computeFinishDate(context, var5, var24, var22, calendarId, calendarDate);
                actualFinishDate = var3.format(var26);
            } else {
                actualFinishDate = dueDate;
            }

            var8 = actualFinishDate;
        }

        if (ProgramCentralConstants.POLICY_PROJECT_REVIEW.equalsIgnoreCase(policy)) {
            ;
        }

        var8 = getDateString(context, var8);
        return var8;
    }

    private static String getDateString(Context var0, String var1) throws Exception {
        String var2 = "";
        if (UIUtil.isNotNullAndNotEmpty(var1)) {
            SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
            Date var4 = eMatrixDateFormat.getJavaDate(var1, var0.getLocale());
            Calendar var5 = Calendar.getInstance(var0.getLocale());
            var5.setTime(var4);
            var4 = var5.getTime();
            var2 = var3.format(var4);
        }

        return var2;
    }

    private static Date setTime(Context var0, String var1, Date var2, String var3, String var4, boolean var5) throws FrameworkException {
        Calendar var6 = Calendar.getInstance();
        var6.setTime(var2);
        WorkCalendar var7 = null;
        if (ProgramCentralUtil.isNotNullString(var3)) {
            var7 = WorkCalendar.getCalendarObject(var0, var3, var4);
        } else {
            com.matrixone.apps.program.Task var8 = new com.matrixone.apps.program.Task();
            var8.setId(var1);
            var7 = var8.getSchedulingCalendar(var0);
        }

        if (var7 == null) {
            var7 = WorkCalendar.getDefaultCalendar();
        }

        Map var10 = var7.getWorkingDayDetailMap(var0, var6);
        int var9 = (Integer)var10.get("attribute[Work Start Time].value");
        if (var5) {
            var9 = (Integer)var10.get("attribute[Work Finish Time].value");
        }

        var6.set(11, var9);
        return var6.getTime();
    }

    private static double computeDuration(Context var0, String var1, Date var2, Date var3, String var4, String var5) throws FrameworkException {
        double var6 = 1.0;
        if (var2.compareTo(var3) < 0) {
            WorkCalendar var8 = null;
            if (ProgramCentralUtil.isNotNullString(var4)) {
                var8 = WorkCalendar.getCalendarObject(var0, var4, var5);
            } else {
                com.matrixone.apps.program.Task var9 = new com.matrixone.apps.program.Task();
                var9.setId(var1);
                var8 = var9.getSchedulingCalendar(var0);
            }

            if (var8 == null) {
                var8 = WorkCalendar.getDefaultCalendar();
            }

            var6 = var8.computeNewDuration(var0, var2, var3);
        }

        return var6;
    }

    private static Date computeFinishDate(Context var0, String var1, Date var2, double var3, String var5, String var6) throws FrameworkException {
        WorkCalendar var7 = null;
        Calendar var9 = Calendar.getInstance();
        var9.setTime(var2);
        if (ProgramCentralUtil.isNotNullString(var5)) {
            var7 = WorkCalendar.getCalendarObject(var0, var5, var6);
        } else {
            com.matrixone.apps.program.Task var10 = new com.matrixone.apps.program.Task();
            var10.setId(var1);
            var7 = var10.getSchedulingCalendar(var0);
        }

        if (var7 == null) {
            var7 = WorkCalendar.getDefaultCalendar();
        }

        Map var12 = var7.getWorkingDayDetailMap(var0, var9);
        int var11 = (Integer)var12.get("attribute[Working Time Per Day].value");
        Date var8 = var7.calculateFinishDate(var0, var2, "Task Finish Date", (double)(var11 / 60) * var3);
        return var8;
    }

    public static String getTaskDeviationValue(Context var0, Dataobject var1) throws Exception {
        new HashMap();
        String var3 = var0.getSession().getLanguage();
        String var4 = ProgramCentralUtil.getPMCI18nString(var0, "emxProgramCentral.DurationUnits.Days", var3);
        var4 = " " + var4;
        WorkCalendar var5 = null;
        String var6 = var1.getId();
        float var7 = (float)com.matrixone.apps.program.Task.parseToDouble(DataelementMapAdapter.getDataelementValue(var1, "estimatedDurationInputValue"));
        String var8 = DataelementMapAdapter.getDataelementValue(var1, "percentComplete");
        String var9 = formatDate(var0, DataelementMapAdapter.getDataelementValue(var1, "estimatedStartDate"));
        Date var10 = eMatrixDateFormat.getJavaDate(var9);
        float var11 = (float)com.matrixone.apps.program.Task.parseToDouble(DataelementMapAdapter.getDataelementValue(var1, "percentComplete"));
        String var12 = DataelementMapAdapter.getDataelementValue(var1, "calendarId");
        String var13 = DataelementMapAdapter.getDataelementValue(var1, "calendarDate");
        if (ProgramCentralUtil.isNotNullString(var12)) {
            var5 = WorkCalendar.getCalendarObject(var0, var12, var13);
        }

        Calendar var14 = Calendar.getInstance();
        Date var15 = var14.getTime();
        String var16 = "0";
        Date var17 = ProgramCentralUtil.removeTimeFromDate(var0, var10);
        Date var18 = ProgramCentralUtil.removeTimeFromDate(var0, var15);
        int var19 = var17.compareTo(var18);
        if (var11 == 100.0F || var19 == 0 || var19 > 0 && var11 == 0.0F) {
            var16 = "0";
        } else if (var17.after(var18) && var11 > 0.0F) {
            var16 = getForwardDeviation(var0, var17, var7, var11, var5);
        } else {
            var16 = getBackwardDeviation(var0, var17, var7, var11, var5);
        }

        var16 = var16 + var4;
        return var16;
    }

    protected static String getForwardDeviation(Context var0, Date var1, float var2, float var3, WorkCalendar var4) throws FrameworkException {
        long var5 = 0L;
        long var7 = 0L;
        Date var9 = null;
        Calendar var10 = Calendar.getInstance();
        var10.add(7, 1);
        Date var11 = ProgramCentralUtil.removeTimeFromDate(var0, var10.getTime());
        float var12 = var3 * var2 / 100.0F;
        double var13 = (double)var12 - Math.floor((double)var12);
        if (var13 >= 0.5) {
            ++var12;
        }

        var5 = (long)var12;
        if (var4 != null) {
            var9 = var4.computeFinishDate(var0, var1, var5);
        } else {
            if (var5 > 0L) {
                var7 = var5 * 24L * 60L * 60L * 1000L;
            }

            var9 = DateUtil.computeFinishDate(var1, var7);
        }

        var5 = DateUtil.computeDuration(var11, var9);
        return "" + var5;
    }

    protected static String getBackwardDeviation(Context var0, Date var1, float var2, float var3, WorkCalendar var4) throws FrameworkException {
        Date var5 = Calendar.getInstance().getTime();
        Date var6 = ProgramCentralUtil.removeTimeFromDate(var0, var5);
        long var7 = 0L;
        long var9 = 0L;
        Date var11 = null;
        Calendar var12 = Calendar.getInstance();
        Calendar var13 = Calendar.getInstance();
        var12.add(7, -1);
        Date var14 = ProgramCentralUtil.removeTimeFromDate(var0, var12.getTime());
        if (var3 != 0.0F && var2 != 0.0F) {
            float var15 = var3 * var2 / 100.0F;
            double var16 = (double)var15 - Math.floor((double)var15);
            if (var16 >= 0.5) {
                ++var15;
            }

            var7 = (long)var15;
            if (var4 != null) {
                var11 = var4.computeFinishDate(var0, var1, var7);
                var13.setTime(var11);
                var7 = var4.computeDuration(var0, var13.getTime(), var14);
            } else {
                if (var7 > 0L) {
                    ++var7;
                    var9 = var7 * 24L * 60L * 60L * 1000L;
                }

                var11 = DateUtil.computeFinishDate(var1, var9);
                var13.setTime(var11);
                var7 = DateUtil.computeDuration(var13.getTime(), var14);
            }

            Date var18 = ProgramCentralUtil.removeTimeFromDate(var0, var13.getTime());
            if (var18.compareTo(var6) >= 0) {
                return "0";
            }
        } else if (var4 != null) {
            var7 = var4.computeDuration(var0, var1, var14);
        } else {
            var7 = DateUtil.computeDuration(var1, var14);
        }

        return "-" + var7;
    }

    public static Map getProjectFloatFromPAL(Context var0, String var1) {
        new HashMap();
        Map var2 = com.matrixone.apps.program.ProjectSpace.getProjectFloatFromPAL(var0, var1);
        return var2;
    }

    public static Map getTaskCalendar(Context var0, Datacollection var1) {
        HashMap var2 = new HashMap();

        try {
            StringBuffer var3 = new StringBuffer();
            var3.append("to[");
            var3.append(ProgramCentralConstants.RELATIONSHIP_ASSIGNED_TASKS);
            var3.append("].from.from[");
            var3.append(ProgramCentralConstants.RELATIONSHIP_WORKPLACE);
            var3.append("].to.from[");
            var3.append(ProgramCentralConstants.RELATIONSHIP_CALENDAR);
            var3.append("].to.physicalid");
            Select var4 = new Select("parentProjectDefaultCalId", "to[" + ProgramCentralConstants.RELATIONSHIP_PROJECT_ACCESS_KEY + "].from.from[" + ProgramCentralConstants.RELATIONSHIP_PROJECT_ACCESS_LIST + "].to.from[" + ProgramCentralConstants.RELATIONSHIP_DEFAULT_CALENDAR + "].to.physicalid", (ExpressionType)null, (Format)null, false);
            Select var5 = new Select("projectDefaultCalId", "from[" + ProgramCentralConstants.RELATIONSHIP_DEFAULT_CALENDAR + "].to.physicalid", (ExpressionType)null, (Format)null, false);
            Select var6 = new Select("isProjectSpace", ProgramCentralConstants.SELECT_IS_PROJECT_SPACE, (ExpressionType)null, (Format)null, false);
            Select var7 = new Select("selectCalendar", "from[" + ProgramCentralConstants.RELATIONSHIP_CALENDAR + "].to.physicalid", (ExpressionType)null, (Format)null, false);
            Select var8 = new Select("assigneeLocationCalId", var3.toString(), (ExpressionType)null, (Format)null, false);
            Select var9 = new Select("assignees", "to[" + ProgramCentralConstants.RELATIONSHIP_ASSIGNED_TASKS + "].from.physicalid", (ExpressionType)null, (Format)null, false);
            ArrayList var10 = new ArrayList(6);
            var10.add(var4);
            var10.add(var5);
            var10.add(var6);
            var10.add(var7);
            var10.add(var8);
            var10.add(var9);
            Datacollection var11 = ObjectUtil.print(var0, var1, (PrintData)null, var10, false);

            Dataobject var13;
            String var14;
            for(Iterator var12 = var11.getDataobjects().iterator(); var12.hasNext(); var2.put(var13.getId(), var14)) {
                var13 = (Dataobject)var12.next();
                var14 = "";
                String var15 = DataelementMapAdapter.getDataelementValue(var13, "isProjectSpace");
                if ("TRUE".equalsIgnoreCase(var15)) {
                    var14 = DataelementMapAdapter.getDataelementValue(var13, "projectDefaultCalId");
                } else {
                    var14 = DataelementMapAdapter.getDataelementValue(var13, "selectCalendar");
                    String var16 = DataelementMapAdapter.getDataelementValue(var13, "assignees");
                    StringList var17 = new StringList();
                    if (ProgramCentralUtil.isNotNullString(var16)) {
                        var17 = FrameworkUtil.split(var16, "\u0007");
                    }

                    if (ProgramCentralUtil.isNullString(var14) && var17.size() == 1) {
                        var14 = DataelementMapAdapter.getDataelementValue(var13, "assigneeLocationCalId");
                    }

                    if (ProgramCentralUtil.isNullString(var14)) {
                        var14 = DataelementMapAdapter.getDataelementValue(var13, "parentProjectDefaultCalId");
                    }
                }
            }
        } catch (Exception var18) {
            var18.printStackTrace();
        }

        return var2;
    }
    public static Datacollection getUserResourceRequests(Context context, String projectId, List selects) throws FoundationException {
        ServiceUtil.checkLicenseDPM(context);
        Dataobject dataobject = new Dataobject();
        dataobject.setId(projectId);
        ExpandData expandData = new ExpandData();
        expandData.setRelationshipPattern("Resource Plan");
        expandData.setLimit(Short.valueOf((short)0));
        expandData.setRecurseToLevel(BigDecimal.valueOf(1L));
        expandData.setGetFrom(true);
        return ObjectUtil.expand(context, dataobject, expandData, selects);
    }
    public static Datacollection getUserBusinessGoals(Context context, String projectId, List selects) throws FoundationException {
        ServiceUtil.checkLicenseDPM(context);
        Dataobject dataobject = new Dataobject();
        dataobject.setId(projectId);
        ExpandData expandData = new ExpandData();
        expandData.setRelationshipPattern("Business Goal Project Space");
        expandData.setLimit(Short.valueOf((short)0));
        expandData.setRecurseToLevel(BigDecimal.valueOf(1L));
        expandData.setGetTo(true);
        return ObjectUtil.expand(context, dataobject, expandData, selects);
    }
    public static Datacollection getUserPatents(Context context, String projectId, List selects) throws FoundationException {
        ServiceUtil.checkLicenseDPM(context);
        Dataobject dataobject = new Dataobject();
        dataobject.setId(projectId);
        ExpandData expandData = new ExpandData();
        expandData.setRelationshipPattern("gscProjectPatent");
        expandData.setLimit(Short.valueOf((short)0));
        expandData.setRecurseToLevel(BigDecimal.valueOf(1L));
        expandData.setGetTo(true);
        return ObjectUtil.expand(context, dataobject, expandData, selects);
    }
    static {
        SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE = new Select("durationInputValue", DomainObject.getAttributeSelect(ATTRIBUTE_TASK_ESTIMATED_DURATION) + ".inputvalue", (ExpressionType)null, (Format)null, false);
        SELECT_TASK_ESTIMATED_DURATION_INPUT_UNIT = new Select("durationInputUnit", DomainObject.getAttributeSelect(ATTRIBUTE_TASK_ESTIMATED_DURATION) + ".inputunit", (ExpressionType)null, (Format)null, false);
    }
}
