//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.apps.program;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceJson;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollections;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.tskv2.ProjectSequence;
import com.dassault_systemes.enovia.workspace.modeler.WorkspaceVault;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.common.*;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.common.util.ListUtil;
import com.matrixone.apps.domain.DomainAccess;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.effectivity.EffectivityFramework;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.program.Task;
import com.matrixone.apps.program.*;
import com.matrixone.apps.program.fiscal.Helper;
import matrix.db.Access;
import matrix.db.*;
import matrix.util.MatrixException;
import matrix.util.Pattern;
import matrix.util.StringList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class gscProjectSpace extends ProjectManagement implements VaultHolder, AssessmentHolder, QualityHolder, RiskHolder, URLHolder, DocumentHolder {
    protected static final String MQL_DUMP_STR = "|||";
    public static final String RANGE_PROJECT_USER = "Project User";
    public static final String RANGE_PROJECT_LEAD = "Project Lead";
    public static final String RANGE_PROJECT_OWNER = "Project Owner";
    public static final String SELECT_ESCALATION_DATE;
    public static final String SELECT_RECURRENCE_INTERVAL;
    public static final String SELECT_SEND_REMINDER;
    public static final String ATTRIBUTE_TASK_RECURRENCE_INTERVAL;
    public static final String ATTRIBUTE_TASK_SEND_REMINDER;
    public static final String SELECT_TASK_RECURRENCE_INTERVAL;
    public static final String SELECT_TASK_SEND_REMINDER;
    public static final String ATTRIBUTE_BOOKMARK_PHYSICAL_ID;
    public static final String INTERFACE_DPMBOOKMARKROOT;
    public static final String SELECT_PROJECT_FUNCTION;
    public static final String SELECT_PROJECT_TYPE;
    public static final String SELECT_PROJECT_VISIBILITY;
    public static final String SELECT_SCOPE;
    public static final String SELECT_BUSINESS_UNIT_ID;
    public static final String SELECT_BUSINESS_UNIT_NAME;
    public static final String SELECT_COMPANY_ID;
    public static final String SELECT_COMPANY_NAME;
    public static final String SELECT_PROGRAM_ID;
    public static final String SELECT_PROGRAM_NAME;
    protected static final String SELECT_MEMBERS_ID;
    protected static final String SELECT_MEMBERS_PROJECT_ACCESS;
    protected static final String SELECT_MEMBERS_PROJECT_ROLE;
    public static final String SELECT_BASELINE_CURRENT_END_DATE;
    public static final String SELECT_BASELINE_CURRENT_START_DATE;
    public static final String SELECT_BASELINE_INITIAL_END_DATE;
    public static final String SELECT_BASELINE_INITIAL_START_DATE;
    public static final String SELECT_BASELINE_ID;
    public static final String SELECT_MEMBERSHIP_ACCESS;
    public static final String SELECT_MEMBERSHIP_ROLE;
    public static final String SELECT_MEMBERSHIP_ID = "to.id[Project Member]";
    public static final String SELECT_MEMBERSHIP_VAULT_ACCESS;
    public static final String SELECT_PROJECT_ACCESS;
    public static final String SELECT_PROJECT_ROLE;
    protected static final String SELECT_PROJECT_ACCESS_LIST_ID;
    protected static final String SELECT_TASK_PROJECT_ACCESS_LIST_ID;
    public static final String ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE;
    public static final String SELECT_DEFAULT_CONSTRAINT_TYPE;
    public static final String DEFAULT_TASK_CONSTRAINT = "As Soon As Possible";
    public static final String RELATIONSHIP_CONTRIBUTES_TO;
    private DomainObject _accessListObject = null;
    protected static final RelationshipType MEMBER_RELATIONSHIP_TYPE;
    public static final String STATE_PROJECT_COMPLETE;
    public static final String STATE_PROJECT_ARCHIVE;
    public static final String SELECT_EFFORT_NOTIFICATION;
    public static final String TYPE_GATE;
    public static final String TYPE_PHASE;
    public static final String TYPE_MILESTONE;
    public static final String POLICY_PROJECT_REVIEW;
    public static final String STATE_PROJECT_REVIEW_CREATE;
    public static final String STATE_PROJECT_REVIEW_REVIEW;
    public static final String STATE_PROJECT_REVIEW_COMPLETE;
    public static final String POLICY_PROJECT_SPACE_HOLD_CANCEL;
    public static final String STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD;
    public static final String STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL;
    public static final String SELECT_PROJECT_TEMPLATE_ID;
    protected static final String ROLE_FINANCIAL_REVIEWER;
    protected static final String ROLE_PROJECT_ASSESSOR;
    protected static final String ROLE_EXTERNAL_PROJECT_LEAD;
    protected static final String ROLE_EXTERNAL_FINANCIAL_REVIEWER;
    protected static final String ROLE_EXTERNAL_PROJECT_ASSESSOR;
    private static final String SELECT_SUCCESSOR_IDS;
    public static final String COMMENT_PRIMARY = "Primary";
    public static final String KEY_OWNERSHIP_ID = "ownershipId";
    public static final String KEY_PERSON_NAME = "personName";
    public static final String KEY_IS_PERSON_OWNERSHIP = "isPersonOwnership";
    public static final String KEY_OWNERSHIP_ORG = "ownershipOrg";
    public static final String KEY_OWNERSHIP_PROJECT = "ownershipProject";
    public static final String KEY_OWNERSHIP_COMMENT = "ownershipComment";
    public static final String KEY_IS_PRIMARY_OWNERSHIP = "isPrimaryOwnership";
    public static final String DEFAULT_VAULTS = "eService Production";
    public static final String SELECT_BUSINESS_GOAL_ID;
    public static final String SELECT_BUSINESS_GOAL_NAME;
    public static final String TYPE_RISK_MANAGEMENT = "Risk Management";
    public static final String RELATIONSHIP_RESOLUTION_PROJECT = "Contributes To";
    public static final String RELATIONSHIP_RESOLVED_TO;
    public static final String RIO_ITEM_ID = "to[Contributes To].from.id";
    public static final String RESOLVED_ISSUE_ID;
    public static final String RESOLVED_BY_ITEM_ID = "from[Contributes To].to.id";
    public static final String RESOLVED_BY_ITEM_ID_FOR_ISSUE;
    public static final String SELECT_SOURCE_ID;
    private StringList WBSLevelList = new StringList();
    private boolean isFileCorrect = true;
    private final String ATTRIBUTE_IS_PROPOSAL_TEMPLATE = "Is Proposal Template";
    private static final String ATTRIBUTE_FORMAT_PATTERN;
    public static final String KEY_LEVEL = "level";
    public static final String SELECT_PROJECT_SCHEDULE;
    public static final String RELATIONSHIP_REFERENCED_SIMULATIONS = "Referenced Simulations";
    public static final String SELECT_SIMULIA_PROCESS_AND_TEMPLATE_IDS = "from[Referenced Simulations].to.id";
    private StringList slDefaultContraintType = new StringList();
    private static boolean hasCircularDependency;
    private static boolean _testCase;

    public void setOdtEnv(boolean var1) {
        _testCase = var1;
    }

    public gscProjectSpace() {
        this.isFileCorrect = true;
    }

    public gscProjectSpace(String var1) throws Exception {
        super(var1);
        this.isFileCorrect = true;
    }

    public gscProjectSpace(BusinessObject businessObject) {
        super(businessObject);
        this.isFileCorrect = true;
    }

    public gscProjectSpace(DecoratedOid decoratedOid) throws Exception {
        super(decoratedOid.getOID());
        this.isFileCorrect = true;
    }

    public void addMember(Context context, String var2) throws FrameworkException {
        String[] var3 = new String[]{var2};
        this.addMembers(context, var3);
    }

    public void addMember(Context context, String[] var2) throws FrameworkException {
        this.addMembers(context, var2);
    }

    public void addMembers(Context var1, String[] var2) throws FrameworkException {
        DomainRelationship.connect(var1, this, MEMBER_RELATIONSHIP_TYPE, true, var2, true);
    }

    protected void addMember(Context context, String var2, String projectAccess) throws FrameworkException {
        HashMap hashMap = new HashMap(1);
        HashMap hashMap1 = new HashMap(1);
        hashMap1.put(ATTRIBUTE_PROJECT_ACCESS, projectAccess);
        hashMap.put(var2, hashMap1);
        this.addNewMembers(context, hashMap);
    }

    protected void addNewMembers(Context var1, Map map) throws FrameworkException {
        int var3 = map.size();
        System.out.println("map : " + map);
        if (var3 > 0) {
            StringBuffer stringBuffer = new StringBuffer(var3 * 50);
            ArrayList arrayList = new ArrayList();
            Set keySet = map.keySet();
            Iterator iterator = keySet.iterator();

            while(iterator.hasNext()) {
                String var8 = (String)iterator.next();
                stringBuffer.append("connect bus $1 preserve relationship $2 to $3");
                arrayList.add(this.getObjectId());
                arrayList.add(RELATIONSHIP_MEMBER);
                arrayList.add(var8);// member id
                Map var9 = (Map)map.get(var8);
                Iterator var10 = var9.keySet().iterator();
                int var11 = 4;

                while(var10.hasNext()) {
                    String var12 = (String)var10.next();
                    String var13 = (String)var9.get(var12);
                    stringBuffer.append(" $" + var11++);
                    stringBuffer.append(" $" + var11++);
                    arrayList.add(var12);
                    arrayList.add(var13);
                }
                System.out.println("arrayList : " + arrayList);
                MqlUtil.mqlCommand(var1, stringBuffer.toString(), arrayList);
                stringBuffer.delete(0, stringBuffer.length());
                arrayList.clear();
            }
        }

    }

    public void addMembers(Context var1, Map var2) throws FrameworkException {
        int var3 = var2.size();
        if (var3 > 0) {
            String[] var4 = new String[var3];
            Set var5 = var2.keySet();
            Iterator var6 = var5.iterator();

            for(int var7 = 0; var6.hasNext(); ++var7) {
                var4[var7] = (String)var6.next();
            }

            Map var8 = DomainRelationship.connect(var1, this, MEMBER_RELATIONSHIP_TYPE, true, var4, true);
            Set var9 = var8.keySet();
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
                String var11 = (String)var10.next();
                Object var12 = var8.get(var11);
                if (var12 instanceof String) {
                    DomainRelationship var13 = new DomainRelationship((String)var12);
                    Map var14 = (Map)var2.get(var11);
                    var13.setAttributeValues(var1, var14);
                }
            }
        }

    }

    public void setGroupRoleMembership(Context var1, Map var2) throws FrameworkException {
        int var3 = var2.size();
        if (var3 > 0) {
            Set var4 = var2.keySet();
            Iterator var5 = var4.iterator();

            try {
                ContextUtil.startTransaction(var1, true);

                while(var5.hasNext()) {
                    String var6 = (String)var5.next();
                    String var7 = (String)var2.get(var6);
                    String[] var8;
                    if (var7.equals("None")) {
                        var8 = new String[]{this.getObjectId(), var6};
                        JPO.invoke(var1, "emxMemberRelationship", (String[])null, "triggerDeleteAction", var8);
                    } else {
                        var8 = new String[]{this.getObjectId(), var6, ATTRIBUTE_PROJECT_ACCESS, null, var7};
                        JPO.invoke(var1, "emxMemberRelationship", (String[])null, "triggerModifyAttributeAction", var8);
                    }
                }

                ContextUtil.commitTransaction(var1);
            } catch (Exception var9) {
                ContextUtil.abortTransaction(var1);
                throw new FrameworkException(var9);
            }
        }

    }

    public void clear() throws FrameworkException {
        super.clear();
        this._accessListObject = null;
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6) throws FrameworkException, Exception {
        return this.clone(var1, var2, var3, var4, var5, var6, false);
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, boolean var7) throws FrameworkException, Exception {
        return var7 ? this.clone(var1, var2, var3, var4, var5, var6, (String)null, var7) : this.clone(var1, var2, var3, var4, var5, var6, (String)null);
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7) throws FrameworkException {
        return this.clone(var1, var2, var3, var4, var5, var6, var7, false);
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8) throws FrameworkException {
        try {
            return this.clone(var1, var2, var3, var4, var5, var6, var7, var8, (Map)null);
        } catch (Exception var10) {
            throw new FrameworkException(var10);
        }
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8, Map var9) throws FrameworkException {
        return this.clone(var1, var2, var3, var4, var5, var6, var7, var8, (Map)null, ProjectCloneOption.CLONE_EVERYTHING);
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8, Map var9, ProjectCloneOption... var10) throws FrameworkException {
        return this.clone(var1, var2, var3, var4, var5, var6, var7, var8, var9, false, var10);
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8, Map var9, boolean var10, ProjectCloneOption... var11) throws FrameworkException {
        return this.clone(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, true, var11);
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8, Map var9, boolean var10, ContentReplicateOptions var11, ProjectCloneOption... var12) throws FrameworkException {
        return this.clone(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, true, var11, var12);
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8, Map var9, boolean var10, boolean var11, ProjectCloneOption... var12) throws FrameworkException {
        return this.clone(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, true, ContentReplicateOptions.COPY, var12);
    }

    public DomainObject clone(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8, Map var9, boolean var10, boolean var11, ContentReplicateOptions var12, ProjectCloneOption... var13) throws FrameworkException {
        DebugUtil.debug("Entering cloneObject.");
        ContextUtil.startTransaction(var1, true);

        try {
            ComponentsUtil.checkLicenseReserved(var1, ProgramCentralConstants.PGE_PRG_LICENSE_ARRAY);
            String var14 = this.getUniqueName("");
            DebugUtil.debug("Duplicating Object Information.");
            StringList var15 = this.getAttributeNames(var1);
            var15.remove(ATTRIBUTE_TASK_ACTUAL_DURATION);
            var15.remove(ATTRIBUTE_TASK_ACTUAL_START_DATE);
            var15.remove(ATTRIBUTE_TASK_ACTUAL_FINISH_DATE);
            var15.remove(ATTRIBUTE_PERCENT_COMPLETE);
            var15.remove(ATTRIBUTE_ORIGINATOR);
            var15.remove(ATTRIBUTE_PROJECT_TYPE);
            var15.remove(ATTRIBUTE_PROJECT_FUNCTION);
            var15.remove("Is Proposal Template");
            var15.remove(ProgramCentralConstants.ATTRIBUTE_FORECAST_DURATION);
            var15.remove(ProgramCentralConstants.ATTRIBUTE_FORECAST_START_DATE);
            var15.remove(ProgramCentralConstants.ATTRIBUTE_FORECAST_FINISH_DATE);
            var15.remove(ProgramCentralConstants.ATTRIBUTE_FORECAST_CALCULATED_ON);
            var15.remove(ProgramCentralConstants.ATTRIBUTE_BOOKMARK_PHYSICAL_ID);
            var15.remove("EnforceProjectName");
            var15.remove("ProjectAutonameSeries");
            var15.add(ProgramCentralConstants.ATTRIBUTE_SOURCE_ID);
            Map var16 = this.getAttributeMap(var1, true);
            HashMap var17 = new HashMap();
            Iterator var18 = var15.iterator();

            while(var18.hasNext()) {
                String var19 = (String)var18.next();
                AttributeType var20 = new AttributeType(var19);
                var20.open(var1);
                String var21;
                if (var20.isMultiVal()) {
                    var21 = "";
                    var21 = PropertyUtil.getSchemaProperty("attribute_" + var19);
                    if (ProgramCentralUtil.isNullString(var21)) {
                        var21 = var19;
                    }

                    StringList var22 = this.getInfoList(var1, "attribute[" + var21 + "]");
                    var17.put(var19, var22);
                } else {
                    var21 = (String)var16.get(var19);
                    if (var21 != null) {
                        var17.put(var19, var21);
                    }
                }
            }

            StringList var35 = new StringList();
            var35.add("physicalid");
            var35.add("id");
            var35.add("description");
            var35.add(SELECT_REFERENCE_DOC_IDS);
            var35.add(SELECT_REFERENCE_DOC_HAS_FILE);
            var35.add(REFERENCE_DOC_SELECT_IS_VERSION_OBJECT);
            var35.add("to[" + DomainConstants.RELATIONSHIP_CLASSIFIED_ITEM + "].from.id");
            StringList var36 = new StringList();
            var36.add(SELECT_REFERENCE_DOC_IDS);
            var36.add(SELECT_REFERENCE_DOC_HAS_FILE);
            var36.add(REFERENCE_DOC_SELECT_IS_VERSION_OBJECT);
            Map var38 = this.getInfo(var1, var35, var36);
            MapList var37 = new MapList();
            var37.add(var38);
            String var23 = (String)var38.get("description");
            Object var24 = var38.get("to[" + DomainConstants.RELATIONSHIP_CLASSIFIED_ITEM + "].from.id");
            boolean var25 = null != var9 && var9.size() > 0;
            BusinessInterfaceList var26 = this.getBusinessInterfaces(var1, true);

            for(int var27 = 0; var27 < var26.size(); ++var27) {
                BusinessInterface var28 = (BusinessInterface)var26.get(var27);
                if (var28.getName().equalsIgnoreCase("DPMForecast")) {
                    var26.remove(var28);
                    break;
                }
            }

            BusinessInterfaceList var39 = new BusinessInterfaceList();
            var39.addAll(var26);
            if (var25) {
                var17.putAll(var9);

                for(int var40 = 0; var40 < var39.size(); ++var40) {
                    BusinessInterface var29 = (BusinessInterface)var39.get(var40);
                    if (!var29.getName().equalsIgnoreCase("DPMGanttChart")) {
                        var26.remove(var29);
                    }
                }
            }

            gscProjectSpace var41 = new gscProjectSpace();
            String var42 = PropertyUtil.getGlobalRPEValue(var1, "CopyColorAttribute");
            if ("false".equalsIgnoreCase(var42)) {
                var17.remove(ATTRIBUTE_COLOR);
                var17.remove(ATTRIBUTE_FORMAT_PATTERN);
            }

            var41.create(var1, var2, var3, var4, var1.getVault().getName(), var17, var23, var10, var26);
            if (var24 != null) {
                this.cloneClassifiedItems(var1, var24, var41.getObjectId(var1));
            }

            DebugUtil.debug("Cloning successful.");
            HashMap var30 = new HashMap();
            var30.put(ATTRIBUTE_ESTIMATED_DURATION_KEYWORD, "true");
            if (this.isKindOf(var1, DomainConstants.TYPE_PROJECT_TEMPLATE) && var41.isKindOf(var1, DomainConstants.TYPE_PROJECT_SPACE)) {
                DebugUtil.debug("Connecting project to initiating template.");
                DomainRelationship.connect(var1, var41.getObjectId(var1), RELATIONSHIP_INITIATED_TEMPLATE_PROJECT, this.getObjectId(var1), true);
                cloneStructure(var1, this, var41, var6, false, var8, var30, false, false, var10, ContentReplicateOptions.COPY, var12, ContentReplicateOptions.COPY, var13);
            } else {
                DomainRelationship.connect(var1, var41.getObjectId(var1), RELATIONSHIP_INITIATED_TEMPLATE_PROJECT, this.getObjectId(var1), true);
                if (var11) {
                    cloneStructure(var1, this, var41, var6, true, var8, var30, false, false, var10, ContentReplicateOptions.IGNORE, var12, ContentReplicateOptions.COPY, var13);
                } else {
                    cloneStructure(var1, this, var41, var6, true, var8, var30, false, false, var10, ContentReplicateOptions.IGNORE, var12, ContentReplicateOptions.COPY, var11, var13);
                }
            }

            this.copyRelatedItemsAndExtendedMemberRoles(var1, var41);
            String var31 = (String)var38.get("id");
            HashMap var32 = new HashMap();
            var32.put(var31, var41);
            boolean var33 = false;
            this.cloneTaskDeliverablesandReferenceDocuments(var1, var37, var32, var33, var12);
            ContextUtil.commitTransaction(var1);
            return var41;
        } catch (Exception var34) {
            ContextUtil.abortTransaction(var1);
            throw new FrameworkException(var34);
        }
    }

    protected void copyRelatedItemsAndExtendedMemberRoles(Context var1, gscProjectSpace var2) throws MatrixException {
        try {
            boolean var3 = FrameworkUtil.isSuiteRegistered(var1, "appVersionLPI", false, (String)null, (String)null);
            String var4 = PropertyUtil.getRPEValue(var1, "fromODT", true);
            if (var3 || !"False".equalsIgnoreCase(var4) && !ProgramCentralUtil.isNullString(var4)) {
                StringList var5 = new StringList(3);
                var5.add("type.kindof[" + TYPE_PROJECT_TEMPLATE + "]");
                var5.add("type.kindof[" + TYPE_PROJECT_SPACE + "]");
                var5.add("type.kindof[" + TYPE_PROJECT_CONCEPT + "]");
                Map var6 = this.getInfo(var1, var5);
                boolean var7 = "true".equalsIgnoreCase((String)var6.get("type.kindof[" + TYPE_PROJECT_TEMPLATE + "]"));
                boolean var8 = "true".equalsIgnoreCase((String)var6.get("type.kindof[" + TYPE_PROJECT_SPACE + "]"));
                boolean var9 = "true".equalsIgnoreCase((String)var6.get("type.kindof[" + TYPE_PROJECT_CONCEPT + "]"));
                if (var7 || var8 || var9) {
                    String var10 = this.getId(var1);
                    String var11 = var2.getId(var1);
                    String[] var12 = new String[]{var10, var11};
                    if ("False".equalsIgnoreCase(var4) || ProgramCentralUtil.isNullString(var4)) {
                        JPO.invoke(var1, "emxRelatedItemsTrigger", (String[])null, "copyRelatedItemsWhenCopyTask", var12);
                        if (!var7) {
                            JPO.invoke(var1, "emxExtendedMembershipTrigger", (String[])null, "copyExtendedMemberOnProjectCopy", var12);
                        }
                    }

                }
            }
        } catch (Exception var13) {
            throw new MatrixException(var13);
        }
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3) throws Exception {
        cloneStructure(var0, var1, var2, var3, true);
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3, boolean var4) throws Exception {
        cloneStructure(var0, var1, var2, var3, var4, false);
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3, boolean var4, boolean var5) throws Exception {
        cloneStructure(var0, var1, var2, var3, var4, var5, (Map)null);
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3, boolean var4, boolean var5, Map var6) throws Exception {
        cloneStructure(var0, var1, var2, var3, var4, var5, var6, false, ContentReplicateOptions.COPY, ContentReplicateOptions.COPY);
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3, boolean var4, boolean var5, Map var6, ProjectCloneOption... var7) throws Exception {
        cloneStructure(var0, var1, var2, var3, var4, var5, var6, false, ContentReplicateOptions.IGNORE, ContentReplicateOptions.COPY, var7);
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3, boolean var4, boolean var5, Map var6, boolean var7, ContentReplicateOptions var8, ContentReplicateOptions var9) throws Exception {
        cloneStructure(var0, var1, var2, var3, var4, var5, var6, false, var8, var9, ProjectCloneOption.CLONE_EVERYTHING);
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3, boolean var4, boolean var5, Map var6, boolean var7, ContentReplicateOptions var8, ContentReplicateOptions var9, ProjectCloneOption... var10) throws Exception {
        cloneStructure(var0, var1, var2, var3, var4, var5, var6, var7, false, false, var8, var9, var10);
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3, boolean var4, boolean var5, Map var6, boolean var7, boolean var8, boolean var9, ContentReplicateOptions var10, ContentReplicateOptions var11, ProjectCloneOption... var12) throws Exception {
        cloneStructure(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, ContentReplicateOptions.IGNORE, var11, true, var12);
    }

    public static void cloneStructure(Context var0, gscProjectSpace var1, gscProjectSpace var2, Map var3, boolean var4, boolean var5, Map var6, boolean var7, boolean var8, boolean var9, ContentReplicateOptions var10, ContentReplicateOptions var11, ContentReplicateOptions var12, ProjectCloneOption... var13) throws Exception {
        cloneStructure(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, true, var13);
    }

    public static void cloneStructure(Context context, gscProjectSpace gscProjectSpace, gscProjectSpace var2, Map var3, boolean var4, boolean var5, Map var6, boolean var7, boolean var8, boolean var9, ContentReplicateOptions var10, ContentReplicateOptions var11, ContentReplicateOptions var12, boolean var13, ProjectCloneOption... var14) throws Exception {
        HashMap var15 = new HashMap();
        ArrayList var16 = new ArrayList();
        if (var14 != null) {
            ProjectCloneOption[] var17 = var14;
            int var18 = var14.length;

            for(int var19 = 0; var19 < var18; ++var19) {
                ProjectCloneOption var20 = var17[var19];
                var16.add(var20);
            }
        }

        if (var16.size() > 0) {
            if (var16.contains(ProjectCloneOption.CLONE_FOLDERS) || var16.contains(ProjectCloneOption.CLONE_EVERYTHING)) {
                WorkspaceVault.cloneStructure(context, gscProjectSpace, var2, var15, var4, var12);
            }

            if (var16.contains(ProjectCloneOption.CLONE_WBS_CONSTRAINTS)) {
                var8 = true;
            }
        }

        Task var48 = new Task(gscProjectSpace);
        CheckList var49 = new CheckList();
        Class var50 = CheckList.class;
        synchronized(CheckList.class) {
            var48.addEventListener(var49);
            if (var13) {
                var48.cloneStructure(context, var2, var3, var15, var5, var6, var7, var8, var9, var10, var11, true);
            } else {
                var48.cloneStructure(context, var2, var3, var15, var5, var6, var7, var8, var9, var10, var11, var13);
            }

            var48.removeEventListener(var49);
        }

//        if (projectSpace instanceof ProjectTemplate && var2.isKindOf(context, DomainConstants.TYPE_PROJECT_TEMPLATE)) {
//            Question.cloneStructure(context, (ProjectTemplate)projectSpace, new ProjectTemplate(var2), var15);
//        }

        StringList var51 = new StringList(1);
        var51.add(SELECT_URL_IDS);
        Map var52 = gscProjectSpace.getInfo(context, var51, var51);
        List var21 = (List)var52.get(SELECT_URL_IDS);
        if (var21 != null) {
            DebugUtil.debug("Cloning bookmarks.");
            int var22 = var21.size();
            String[] var23 = new String[var22];

            for(int var24 = 0; var24 < var22; ++var24) {
                var23[var24] = (String)var21.get(var24);
            }

            URL.copyURLs(context, var23, var2);
        }

        var51.clear();
        var51.add("id");
        StringList var53 = new StringList(3);
        var53.add("id[connection]");
        var53.add(MemberRelationship.SELECT_PROJECT_ACCESS);
        var53.add(MemberRelationship.SELECT_PROJECT_ROLE);
        MapList var54 = var2.getMembers(context, var51, var53, (String)null, (String)null);
        String var55 = "";

        Map var26;
        for(Iterator var25 = var54.iterator(); var25.hasNext(); var55 = (String)var26.get("id[connection]")) {
            var26 = (Map)var25.next();
        }

        StringList var56 = new StringList(2);
        var56.add("type");
        var56.add(ProgramCentralConstants.SELECT_GANTT_CONFIG_FROM_PROJECT);
        StringList var27 = new StringList(2);
        var27.add("type");
        var27.add(SELECT_PROJECT_ACCESS_LIST_ID_FOR_PROJECT);
        Map var28 = gscProjectSpace.getInfo(context, var56);
        String var29 = (String)var28.get("type");
        String var30 = (String)var28.get(ProgramCentralConstants.SELECT_GANTT_CONFIG_FROM_PROJECT);
        Map var31 = var2.getInfo(context, var27);
        String var32 = (String)var31.get("type");
        String var33 = (String)var31.get(ProgramCentralConstants.SELECT_PAL_OBJECTID_FROM_PROJECT);
        DomainObject var34 = DomainObject.newInstance(context, var33);
        var34.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_GANTT_CONFIG, var30);
        boolean var35 = true;
        if (ProgramCentralConstants.TYPE_PROJECT_TEMPLATE.equalsIgnoreCase(var29) && ProgramCentralConstants.TYPE_PROJECT_SPACE.equalsIgnoreCase(var32)) {
            var35 = false;
        }

        if (var35) {
            MapList members = gscProjectSpace.getMembers(context, var51, var53, (String)null, (String)null, true);
            int var37 = members.size();
            if (var37 > 0) {
                String var38 = Person.getPerson(context).getObjectId();
                HashMap var39 = new HashMap(var37);
                HashMap var40 = new HashMap();
                Iterator var41 = members.iterator();

                while(true) {
                    while(var41.hasNext()) {
                        var52 = (Map)var41.next();
                        String var42 = (String)var52.get("id");
                        String var44;
                        if (ProgramCentralUtil.isNotNullString(var42)) {
                            if (!var42.equals(var38)) {
                                HashMap var47 = new HashMap(2);
                                var44 = (String)var52.get(MemberRelationship.SELECT_PROJECT_ACCESS);
                                if ("Project Owner".equals(var44)) {
                                    var44 = "Project Lead";
                                }

                                var47.put(ATTRIBUTE_PROJECT_ACCESS, var44);
                                var47.put(ATTRIBUTE_PROJECT_ROLE, var52.get(MemberRelationship.SELECT_PROJECT_ROLE));
                                var39.put(var42, var47);
                            }
                        } else {
                            String var43 = (String)var52.get("type");
                            if (ProgramCentralUtil.isNotNullString(var43) && ("Role".equalsIgnoreCase(var43) || "Group".equalsIgnoreCase(var43))) {
                                var44 = (String)var52.get("name");
                                String var45 = (String)var52.get(MemberRelationship.SELECT_PROJECT_ACCESS);
                                var40.put(var44, var45);
                            }
                        }
                    }

                    var2.addNewMembers(context, var39);
                    if (null != var40 && var40.size() != 0) {
                        var2.setGroupRoleMembership(context, var40);
                    }
                    break;
                }
            }
        }

        DebugUtil.debug("Executing project cloneStructure - completed.");
    }

    public void confirmDelete(Context var1, boolean var2, BusinessObjectList var3) throws FrameworkException {
        super.confirmDelete(var1, var2, var3);
    }

    public void create(Context var1, String var2, String var3, String var4) throws FrameworkException {
        this.create(var1, TYPE_PROJECT_SPACE, var2, var3, var4);
    }

    public void create(Context var1, String var2, String var3, String var4, String var5) throws FrameworkException {
        this.create(var1, var2, var3, var4, var5, (Map)null, (String)null);
    }

    public void create(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7) throws FrameworkException {
        this.create(var1, var2, var3, var4, var5, var6, var7, false);
    }

    public void create(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8) throws FrameworkException {
        this.create(var1, var2, var3, var4, var5, var6, var7, var8, new BusinessInterfaceList());
    }

    public void create(Context context, String type, String name, String policy, String var5, Map attrMap, String descriptioin, boolean var8, BusinessInterfaceList businessInterfaceList) throws FrameworkException {
        try {
            ComponentsUtil.checkLicenseReserved(context, ProgramCentralConstants.PGE_PRG_LICENSE_ARRAY);
        } catch (MatrixException var21) {
            throw new FrameworkException(var21);
        }

        String revision = this.getUniqueName("");
        this.createObject(context, type, name, revision, policy, context.getVault().getName());
        this.triggerCreateAction(context, (String[])null);
        if (attrMap == null) {
            attrMap = new HashMap(3);
        }

        if (!((Map)attrMap).containsKey(ATTRIBUTE_TASK_ESTIMATED_DURATION)) {
            String var11 = this.getInfo(context, "originated");
            ((Map)attrMap).put(ATTRIBUTE_TASK_ESTIMATED_START_DATE, var11);
            ((Map)attrMap).put(ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE, var11);
            ((Map)attrMap).put(ATTRIBUTE_TASK_ESTIMATED_DURATION, "1.0");
        }

        ((Map)attrMap).put(ATTRIBUTE_ORIGINATOR, context.getUser());
        Set var23 = ((Map)attrMap).keySet();
        AttributeList var12 = new AttributeList();
        Iterator var13 = var23.iterator();

        while(var13.hasNext()) {
            String var14 = (String)var13.next();
            AttributeType var15 = new AttributeType(var14);

            try {
                var15.open(context);
                boolean var16 = var15.isMultiVal();
                if (var16) {
                    StringList var17 = (StringList)((Map)attrMap).get(var14);
                    var12.addElement(new Attribute(var15, var17));
                    var13.remove();
                }
            } catch (MatrixException var20) {
                var20.printStackTrace();
            }
        }

        String str;
        if (!var8) {
            str = this.getInfo(context, "physicalid");
            ((Map)attrMap).put(ProgramCentralConstants.ATTRIBUTE_SOURCE_ID, str);
        }

        if (businessInterfaceList != null) {
            try {
                BusinessInterfaceList businessInterfaces = this.getBusinessInterfaces(context, true);
                int i = 0;

                for(int j = businessInterfaceList.size(); i < j; ++i) {
                    BusinessInterface businessInterface = (BusinessInterface)businessInterfaceList.get(i);
                    if (!businessInterfaces.contains(businessInterface)) {
                        try {
                            this.addBusinessInterface(context, businessInterface);
                        } catch (Exception var19) {
                            System.out.println(businessInterface.getName() + " is not applicable to the type " + type);
                        }
                    }
                }
            } catch (MatrixException var22) {
                var22.printStackTrace();
            }
        }

        str = PropertyUtil.getGlobalRPEValue(context, "CopyColorAttribute");
        if ("false".equalsIgnoreCase(str)) {
            ((Map)attrMap).remove(ATTRIBUTE_COLOR);
            ((Map)attrMap).remove(ATTRIBUTE_FORMAT_PATTERN);
        }

        this.setAttributeValues(context, (Map)attrMap);
        if (var12 != null && !var12.isEmpty()) {
            try {
                this.setAttributeValues(context, var12);
            } catch (MatrixException var18) {
                var18.printStackTrace();
            }
        }

        if (descriptioin != null) {
            this.setDescription(context, descriptioin);
        }

    }

    public String getAccess(Context context) throws MatrixException {
        String var2 = null;
        String[] var3 = new String[]{this.getObjectId()};

        try {
            var2 = (String)JPO.invoke(context, "emxProjectSpace", var3, "getAccess", (String[])null, "".getClass());
        } catch (MatrixException var5) {
        }

        return var2;
    }

    protected DomainObject getAccessListObject(Context context) throws Exception {
        if (this._accessListObject == null) {
            String id = this.getId();
            String mql = "print bus $1 select $2 dump";
            String type = MqlUtil.mqlCommand(context, mql, new String[]{id, "type"});
            boolean isParentOfTask = mxType.isOfParentType(context, type, DomainConstants.TYPE_TASK_MANAGEMENT);
            String accessListId = "";
            if (!isParentOfTask) {
                accessListId = this.getInfo(context, SELECT_PROJECT_ACCESS_LIST_ID);
            } else {
                accessListId = this.getInfo(context, SELECT_TASK_PROJECT_ACCESS_LIST_ID);
            }

            if (accessListId != null && !"".equals(accessListId)) {
                this._accessListObject = DomainObject.newInstance(context, accessListId);
            }
        }

        return this._accessListObject;
    }

    public Map getCompany(Context context, StringList list) throws FrameworkException {
        return this.getRelatedObject(context, RELATIONSHIP_COMPANY_PROJECT, false, list, (StringList)null);
    }

    public static String getDefaultPolicy(Context context) {
        return POLICY_PROJECT_SPACE;
    }

    public MapList getMembers(Context context, StringList var2, StringList var3, String var4, String var5) throws FrameworkException {
        return this.getMembers(context, var2, var3, var4, var5, false);
    }

    protected Map getSCProjectMembers(Context var1, String var2, Map var3) throws FrameworkException {
        if (var3 == null) {
            var3 = new HashMap(10);
        }

        String var4 = "print role $1 select $2 $3 dump $4";
        String var5 = MqlUtil.mqlCommand(var1, var4, new String[]{var2, "child", "child.isaproject", "|"});
        StringList var6 = FrameworkUtil.split(var5, "|");
        int var7 = var6.size();

        for(int var8 = 0; var8 < var7 / 2; ++var8) {
            String var9 = (String)var6.get(var8);
            String var10 = (String)var6.get(var8 + var7 / 2);
            if ("TRUE".equalsIgnoreCase(var10)) {
                this.getSCProjectMembers(var1, var9, (Map)var3);
            } else {
                var4 = "print role $1 select $2 dump $3";
                String var11 = MqlUtil.mqlCommand(var1, var4, new String[]{var9, "role.ancestor", "|"});
                StringList var12 = FrameworkUtil.split(var11, "|");
                var4 = "print role $1 select $2 dump $3";
                String var13 = MqlUtil.mqlCommand(var1, var4, new String[]{var9, "person", "|"});
                StringList var14 = FrameworkUtil.split(var13, "|");

                for(int var15 = 0; var15 < var14.size(); ++var15) {
                    String var16 = (String)var14.get(var15);
                    StringList var17 = (StringList)((Map)var3).get(var16);
                    if (var17 == null) {
                        var17 = var12;
                    } else {
                        var17.addAll(var12);
                    }

                    ((Map)var3).put(var16, var17);
                }
            }
        }

        return (Map)var3;
    }

    protected Map getSCProjectMembers(Context var1, String var2, StringList var3) throws FrameworkException {
        Map var4 = this.getSCProjectMembers(var1, var2, (Map)null);
        Set var5 = var4.keySet();
        Map var6 = Person.getPersonsFromNames(var1, var5, var3);
        var5 = var6.keySet();

        Map var10;
        String var11;
        for(Iterator var7 = var5.iterator(); var7.hasNext(); var10.put(MemberRelationship.SELECT_PROJECT_ACCESS, var11)) {
            String var8 = (String)var7.next();
            StringList var9 = (StringList)var4.get(var8);
            var10 = (Map)var6.get(var8);
            var10.put(MemberRelationship.SELECT_PROJECT_ROLE, var9);
            var11 = null;
            if (!var9.contains(ROLE_PROJECT_LEAD) && !var9.contains(ROLE_EXTERNAL_PROJECT_LEAD)) {
                if (!var9.contains(ROLE_FINANCIAL_REVIEWER) && !var9.contains(ROLE_EXTERNAL_FINANCIAL_REVIEWER)) {
                    if (!var9.contains(ROLE_PROJECT_ASSESSOR) && !var9.contains(ROLE_EXTERNAL_PROJECT_ASSESSOR)) {
                        var11 = "Project Member";
                    } else {
                        var11 = "Project Assessor";
                    }
                } else {
                    var11 = "Financial Reviewer";
                }
            } else {
                var11 = "Project Lead";
            }
        }

        return var6;
    }

    public MapList getMembers(Context context, StringList objSelects, StringList relSelects, String var4, String var5, boolean var6) throws FrameworkException {
        MapList relatedObjects = this.getRelatedObjects(context, RELATIONSHIP_MEMBER, "*", objSelects, relSelects, false, true, (short)1, var4, var5);
        return relatedObjects;
    }

    public MapList getSCAccessList(Context var1, StringList var2) throws FrameworkException {
        MapList var3 = null;
        String var4 = this.getInfo(var1, "altowner2");
        if (!"".equals(var4)) {
            Map var5 = this.getSCProjectMembers(var1, var4, var2);
            var3 = new MapList(var5.size());
            Set var6 = var5.keySet();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
                String var8 = (String)var7.next();
                Map var9 = (Map)var5.get(var8);
                var9.put(MemberRelationship.SELECT_PROJECT_ROLE, "");
                var3.add(var9);
            }
        } else {
            var3 = new MapList();
        }

        return var3;
    }

    public Map getBusinessUnit(Context var1, StringList var2) throws FrameworkException {
        return this.getRelatedObject(var1, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, var2, (StringList)null);
    }

    /** @deprecated */
    public Map getOrganization(Context var1, StringList var2) throws FrameworkException {
        return this.getBusinessUnit(var1, var2);
    }

    public static MapList getProjects(Context var0, Person var1, StringList var2, StringList var3, String var4, String var5) throws FrameworkException {
        return getProjects(var0, var1, TYPE_PROJECT_MANAGEMENT, var2, var3, var4, var5, true);
    }

    public static MapList getProjects(Context var0, Person var1, String var2, StringList var3, StringList var4, String var5, String var6, boolean var7) throws FrameworkException {
        return getProjects(var0, var1, TYPE_PROJECT_MANAGEMENT, var3, var4, var5, var6, true, false);
    }

    public static MapList getProjects(Context var0, Person var1, String var2, StringList var3, StringList var4, String var5, String var6, boolean var7, boolean var8) throws FrameworkException {
        if (var3 == null) {
            var3 = new StringList();
        }

        if (!var3.contains("type")) {
            var3.add("type");
        }

        if (var2 != null && !"".equals(var2.trim())) {
            var2 = var2.trim();
            new MapList();
            MapList var9;
            if (var8) {
                var9 = DomainObject.findObjects(var0, var2, "*", "*", "*", "*", var5, true, var3);
            } else {
                var9 = var1.getRelatedObjects(var0, RELATIONSHIP_MEMBER, var2, var3, (StringList)null, true, false, (short)1, var5, var6);
            }

            if (!var7) {
                String var10 = "print type $1 select $2 dump $3";
                String var11 = MqlUtil.mqlCommand(var0, var10, new String[]{var2, "derivative", "|"});
                StringList var12 = FrameworkUtil.split(var11, "|");
                MapList var13 = new MapList();
                Map var14 = null;
                String var15 = null;
                Iterator var16 = var9.iterator();

                while(var16.hasNext()) {
                    var14 = (Map)var16.next();
                    var15 = (String)var14.get("type");
                    if (!var12.contains(var15)) {
                        var13.add(var14);
                    }
                }

                var9 = var13;
            }

            return var9;
        } else {
            throw new FrameworkException("Invalid project type '" + var2 + "'");
        }
    }

    protected static MapList getSCUserProjects(Context var0, Person var1, StringList var2, String var3, boolean var4) throws FrameworkException {
        String var5 = var1.getName(var0);
        String var6 = "print person $1 select $2 dump";
        String var7 = MqlUtil.mqlCommand(var0, var6, new String[]{var5, "assignment.project"});
        var6 = "list role $1 select $2 dump $3";
        String var8 = MqlUtil.mqlCommand(var0, var6, new String[]{var7, "ancestor", "|||"});
        var8 = FrameworkUtil.findAndReplace(var8, "\n", "|||");
        if (ProgramCentralUtil.isNullString(var3)) {
            var3 = "altowner2 != '' && altowner2 matchlist '" + var8 + "' '" + "|||" + "' ";
        } else {
            var3 = "altowner2 != '' && altowner2 matchlist '" + var8 + "' '" + "|||" + "' && (" + var3 + ")";
        }

        String var9 = TYPE_PROJECT_SPACE + "," + TYPE_PROJECT_CONCEPT;
        String var10 = "*";
        return DomainObject.findObjects(var0, var9, "*", "*", "*", (String)null, var3, var4, var2);
    }

    /** @deprecated */
    public static MapList getUserProjects(Context var0, Person var1, StringList var2, StringList var3, String var4, String var5) throws FrameworkException {
        Pattern var6 = new Pattern(TYPE_PROJECT_SPACE);
        var6.addPattern(TYPE_PROJECT_CONCEPT);
        boolean var7 = true;
        return getProjects(var0, var1, var6.getPattern(), var2, var3, var4, var5, var7, false);
    }

    public static MapList getTypeAttributes(Context var0) throws FrameworkException {
        return getTypeAttributes(var0, TYPE_PROJECT_SPACE);
    }

    public static boolean isSCEnabled(Context var0) throws FrameworkException {
        return true;
    }

    public void removeMembers(Context var1, String[] var2) throws FrameworkException {
        DomainRelationship.disconnect(var1, var2);
    }

    protected DomainObject getBaselineObject(Context var1, String var2) throws Exception {
        DomainObject var3 = DomainObject.newInstance(var1, var2);
        return var3;
    }

    public void setBaseLine(Context var1, String var2) throws FrameworkException {
        try {
            ContextUtil.startTransaction(var1, true);
            new MapList();
            com.matrixone.apps.common.Task var4 = (com.matrixone.apps.common.Task)newInstance(var1, TYPE_TASK);
            Task var5 = (Task)newInstance(var1, TYPE_TASK, "Program");
            StringList var6 = new StringList(10);
            var6.add("id");
            var6.add(SELECT_TASK_ESTIMATED_START_DATE);
            var6.add(SELECT_TASK_ESTIMATED_FINISH_DATE);
            var6.add(SELECT_BASELINE_INITIAL_START_DATE);
            var6.add(SELECT_BASELINE_INITIAL_END_DATE);
            var6.add(SELECT_BASELINE_CURRENT_START_DATE);
            var6.add(SELECT_BASELINE_CURRENT_END_DATE);
            MapList var3 = com.matrixone.apps.common.Task.getTasks(var1, this, 0, var6, (StringList)null, false, false);
            ListIterator var7 = var3.listIterator();

            String var10;
            String var11;
            String var12;
            while(var7.hasNext()) {
                Map var8 = (Map)var7.next();
                new HashMap();
                var10 = (String)var8.get("id");
                var5.setId(var10);
                var11 = (String)var8.get(Task.SELECT_TASK_ESTIMATED_START_DATE);
                var12 = (String)var8.get(Task.SELECT_TASK_ESTIMATED_FINISH_DATE);
                String var13 = (String)var8.get(Task.SELECT_BASELINE_INITIAL_START_DATE);
                String var14 = (String)var8.get(Task.SELECT_BASELINE_INITIAL_END_DATE);
                this.setBaseLineDates(var1, var5, var11, var13, ATTRIBUTE_BASELINE_CURRENT_START_DATE, ATTRIBUTE_BASELINE_INITIAL_START_DATE);
                this.setBaseLineDates(var1, var5, var12, var14, ATTRIBUTE_BASELINE_CURRENT_END_DATE, ATTRIBUTE_BASELINE_INITIAL_END_DATE);
            }

            new HashMap();
            String var9 = this.getInfo(var1, SELECT_TASK_ESTIMATED_START_DATE);
            var10 = this.getInfo(var1, SELECT_TASK_ESTIMATED_FINISH_DATE);
            var11 = this.getInfo(var1, SELECT_BASELINE_INITIAL_START_DATE);
            var12 = this.getInfo(var1, SELECT_BASELINE_INITIAL_END_DATE);
            this.setBaseLineDates(var1, this, var9, var11, ATTRIBUTE_BASELINE_CURRENT_START_DATE, ATTRIBUTE_BASELINE_INITIAL_START_DATE);
            this.setBaseLineDates(var1, this, var10, var12, ATTRIBUTE_BASELINE_CURRENT_END_DATE, ATTRIBUTE_BASELINE_INITIAL_END_DATE);
            this.setBaseLineLog(var1, var2);
            ContextUtil.commitTransaction(var1);
        } catch (Exception var15) {
            ContextUtil.abortTransaction(var1);
            throw new FrameworkException(var15);
        }
    }

    private void setBaseLineDates(Context var1, DomainObject var2, String var3, String var4, String var5, String var6) throws Exception {
        try {
            HashMap var7 = new HashMap();
            if (null != var4 && !"".equals(var4)) {
                var7.put(var5, var3);
            } else {
                var7.put(var6, var3);
                var7.put(var5, var3);
            }

            var2.setAttributeValues(var1, var7);
        } catch (Exception var8) {
            throw new FrameworkException(var8);
        }
    }

    private void setBaseLineLog(Context var1, String var2) throws FrameworkException {
        try {
            DomainRelationship var3 = new DomainRelationship();
            StringList var4 = new StringList(2);
            var4.add("id[connection]");
            Map var5 = this.getRelatedObject(var1, RELATIONSHIP_BASELINE_LOG, true, var4, (StringList)null);
            gscProjectSpace var6 = new gscProjectSpace();
            var6.setId(this.getId());
            if (null == var5) {
                var3 = this.createAndConnect(var1, TYPE_BASELINE_LOG, (String)null, this.getUniqueName(""), POLICY_BASELINE_LOG, (String)null, RELATIONSHIP_BASELINE_LOG, var6, true);
            } else {
                String var7 = this.getInfo(var1, SELECT_BASELINE_ID);
                var3 = DomainRelationship.connect(var1, var6, RELATIONSHIP_BASELINE_LOG, this.getBaselineObject(var1, var7));
            }

            Person var10 = Person.getPerson(var1);
            String var8 = var10.getInfo(var1, Person.SELECT_FIRST_NAME);
            var8 = var8 + " ";
            var8 = var8 + var10.getInfo(var1, Person.SELECT_LAST_NAME);
            var3.setAttributeValue(var1, ATTRIBUTE_ORIGINATOR, var8);
            var3.setAttributeValue(var1, ATTRIBUTE_COMMENTS, var2);
        } catch (Exception var9) {
            throw new FrameworkException(var9);
        }
    }

    public MapList getBaselineLog(Context var1, StringList var2) throws FrameworkException {
        return this.getRelatedObjects(var1, RELATIONSHIP_BASELINE_LOG, "*", (StringList)null, var2, false, true, (short)1, "", "");
    }

    public void setBusinessUnit(Context var1, String var2) throws FrameworkException {
        this.setRelatedObject(var1, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, var2);
    }

    /** @deprecated */
    public void setOrganization(Context var1, String var2) throws FrameworkException {
        this.setBusinessUnit(var1, var2);
    }

    public void setOwner(Context context, String userId) throws FrameworkException {
        try {
            ContextUtil.startTransaction(context, true);
            String owner = this.getInfo(context, "owner");
            if (!owner.equals(userId)) {
                String ownerObjectId = Person.getPerson(context, owner).getObjectId();
                String userObjectId = Person.getPerson(context, userId).getObjectId();
                String var6 = "id == " + ownerObjectId + " || id == " + userObjectId;
                StringList objSelect = new StringList(2);
                objSelect.add("id");
                objSelect.add("name");
                StringList relSelect = new StringList(1);
                relSelect.add("id[connection]");
                MapList members = this.getMembers(context, objSelect, relSelect, var6, (String)null);
                boolean var10 = false;
                Iterator iterator = members.iterator();

                label34:
                while(true) {
                    Map map;
                    String id;
                    do {
                        if (!iterator.hasNext()) {
                            if (!var10) {
                                this.addMember(context, userObjectId, "Project Owner");
                            }

                            super.setOwner(context, userId);
                            this.updatePALOwnership(context, owner, userId);
                            this.changeRelatedTypeOwnership(context, owner, userId);
                            this.sendOwnershipChangeNotification(context, owner, userId);
                            break label34;
                        }

                        map = (Map)iterator.next();
                        id = (String)map.get("id");
                    } while(!id.equals(userObjectId) && !id.equals(ownerObjectId));

                    String relId = (String)map.get("id[connection]");
                    MemberRelationship memberRelationship = new MemberRelationship(relId);
                    if (id.equals(userObjectId)) {
                        var10 = true;
                        memberRelationship.setAttributeValue(context, ATTRIBUTE_PROJECT_ACCESS, "Project Owner");
                    } else {
                        memberRelationship.setAttributeValue(context, ATTRIBUTE_PROJECT_ACCESS, "Project Lead");
                    }
                }
            }

            ContextUtil.commitTransaction(context);
        } catch (Exception var16) {
            ContextUtil.abortTransaction(context);
            throw new FrameworkException(var16);
        }
    }

    protected void changeRelatedTypeOwnership(Context context, String owner, String userId) throws MatrixException {
        try {
            ContextUtil.pushContext(context, PropertyUtil.getSchemaProperty(context, "person_UserAgent"), "", "");
            String var4 = "from[" + ResourcePlanTemplate.RELATIONSHIP_RESOURCE_PLAN_TEMPLATE + "].frommid[" + ResourcePlanTemplate.RELATIONSHIP_RESOURCE_REQUEST_PLAN_TEMPLATE + "]";
            String var5 = var4 + ".to.id";
            String var6 = var4 + ".to.type";
            String var7 = var4 + ".to.owner";
            String allLevelTypes = FrameworkProperties.getProperty(context, "emxProgramCentral.ChangeProjectOwnerShip.AllLevelTypes");
            String firstLevelTypes = FrameworkProperties.getProperty(context, "emxProgramCentral.ChangeProjectOwnerShip.FirstLevelTypes");
            String allLevelRel = FrameworkProperties.getProperty(context, "emxProgramCentral.ChangeProjectOwnerShip.AllLevelRel");
            String firstLevelRel = FrameworkProperties.getProperty(context, "emxProgramCentral.ChangeProjectOwnerShip.FirstLevelRel");
            String[] allLevelArry = allLevelTypes.split(",");
            String var13 = PropertyUtil.getSchemaProperty(context, allLevelArry[0]);
            int var14 = 1;

            for(int i = allLevelArry.length; var14 < i; ++var14) {
                String var16 = allLevelArry[var14];
                var16 = PropertyUtil.getSchemaProperty(context, var16);
                var13 = var13 + "," + var16;
            }

            String[] var51 = firstLevelTypes.split(",");
            String var52 = PropertyUtil.getSchemaProperty(context, var51[0]);
            int var53 = 1;

            for(int var17 = var51.length; var53 < var17; ++var53) {
                String var18 = var51[var53];
                var18 = PropertyUtil.getSchemaProperty(context, var18);
                var52 = var52 + "," + var18;
            }

            String[] var54 = allLevelRel.split(",");
            String var55 = PropertyUtil.getSchemaProperty(context, var54[0]);
            int var56 = 1;

            String projectId;
            for(int var19 = var54.length; var56 < var19; ++var56) {
                projectId = var54[var56];
                projectId = PropertyUtil.getSchemaProperty(context, projectId);
                var55 = var55 + "," + projectId;
            }

            String[] var57 = firstLevelRel.split(",");
            String var58 = PropertyUtil.getSchemaProperty(context, var57[0]);
            int var59 = 1;

            String accessListiD;
            for(int var21 = var57.length; var59 < var21; ++var59) {
                accessListiD = var57[var59];
                accessListiD = PropertyUtil.getSchemaProperty(context, accessListiD);
                var58 = var58 + "," + accessListiD;
            }

            projectId = this.getId(context);
            DomainObject projectObj = DomainObject.newInstance(context, projectId);
            accessListiD = projectObj.getInfo(context, SELECT_PROJECT_ACCESS_LIST_ID);
            StringList var23 = new StringList();
            StringList var24 = new StringList();
            StringList objSelects = new StringList();
            objSelects.add("id");
            objSelects.add("type");
            objSelects.add("owner");
            objSelects.add(SELECT_TASK_PROJECT_ACCESS_LIST_ID);
            boolean var26 = false;
            boolean var27 = true;
            boolean var28 = true;
            String var29 = "";
            String var30 = "";
            String var31 = "";
            MapList var32 = projectObj.getRelatedObjects(context, var55, var13, objSelects, (StringList)null, var28, var27, (short)0, var30, var31, (short)0, true, true, (short)1000, (Pattern)null, (Pattern)null, (Map)null, "");
            MapList var33 = projectObj.getRelatedObjects(context, var58, var52, objSelects, (StringList)null, var28, var27, (short)1, var30, var31, (short)0, true, true, (short)1000, (Pattern)null, (Pattern)null, (Map)null, "");
            StringList var34 = new StringList(3);
            var34.add(var5);
            var34.add(var6);
            var34.add(var7);
            MapList var35 = DomainObject.getInfo(context, new String[]{projectId}, var34);
            MapList var36 = new MapList();
            var36.addAll(var32);
            var36.addAll(var33);
            var36.addAll(var35);
            Map var37 = null;
            Iterator var38 = var36.iterator();

            String var41;
            while(var38.hasNext()) {
                var37 = (Map)var38.next();
                var29 = (String)var37.get("id");
                String var39 = (String)var37.get("type");
                String var40 = (String)var37.get("owner");
                var41 = (String)var37.get(SELECT_TASK_PROJECT_ACCESS_LIST_ID);
                if (ProgramCentralUtil.isNotNullString(var29) && !var23.contains(var29) && owner.equals(var40) && (ProgramCentralUtil.isNullString(var41) || accessListiD.equals(var41))) {
                    var23.add(var29);
                    var24.add(var39);
                } else if (ProgramCentralUtil.isNullString(var29) && ProgramCentralUtil.isNullString(var40)) {
                    StringList var42 = ProgramCentralUtil.getAsStringList(var37.get(var5));
                    StringList var43 = ProgramCentralUtil.getAsStringList(var37.get(var6));
                    StringList var44 = ProgramCentralUtil.getAsStringList(var37.get(var7));
                    int var45 = var44 == null ? 0 : var44.size();
                    if (var45 > 0 && owner.equals(var44.get(0))) {
                        var23.addAll(var42);
                        var24.addAll(var43);
                    }
                }
            }

            if (!var23.isEmpty()) {
                for(int var61 = 0; var61 < var23.size(); ++var61) {
                    DomainObject var62 = DomainObject.newInstance(context, ((String)var23.get(var61)).toString());
                    var62.setOwner(context, userId);
                    if (ProgramCentralConstants.TYPE_MEETING.equalsIgnoreCase((String)var24.get(var61))) {
                        RelationshipType var63 = new RelationshipType(RELATIONSHIP_ASSIGNED_MEETINGS);
                        var62.addFromObject(context, var63, PersonUtil.getPersonObjectID(context, userId));
                    } else if (ProgramCentralConstants.TYPE_BUDGET.equalsIgnoreCase((String)var24.get(var61))) {
                        var41 = PersonUtil.getPersonObjectID(context, userId);
                        DomainAccess.createObjectOwnership(context, ((String)var23.get(var61)).toString(), var41, "Project Lead", "Multiple Ownership For Object");
                        DomainAccess.deleteObjectOwnership(context, ((String)var23.get(var61)).toString(), (String)null, owner + "_PRJ", "Multiple Ownership For Object");
                    }
                }
            }
        } catch (Exception var49) {
            throw new MatrixException(var49);
        } finally {
            ContextUtil.popContext(context);
        }

    }

    private void sendOwnershipChangeNotification(Context context, String ownerId, String userId) throws FrameworkException {
        if (!userId.equals(ownerId)) {
            StringList var4 = new StringList();
            var4.addElement(userId);
            String var5 = this.getInfo(context, "name");
            String var6 = "emxProgramCentral.Common.ProjectOwnershipChange.Subject";
            String[] var7 = new String[0];
            String[] var8 = new String[0];
            String var9 = "emxProgramCentral.Common.ProjectOwnershipChange.Message";
            String[] var10 = new String[]{"projectName"};
            String[] var11 = new String[]{var5};
            gscProjectConcept.sendNotification(context, this.getId(), var4, var6, var7, var8, var9, var10, var11);
        }

    }

    private void updatePALOwnership(Context context, String ownerId, String userId) throws Exception {
        DomainObject accessListObject = this.getAccessListObject(context);
        if (!userId.equals(ownerId)) {
            try {
                ContextUtil.pushContext(context);
                accessListObject.setOwner(context, userId);
            } finally {
                ContextUtil.popContext(context);
            }

            BusinessObjectList businessObjects = new BusinessObjectList(1);
            businessObjects.add(accessListObject);
            StringList var6 = new StringList(1);
            var6.add(ownerId);

            try {
                ContextUtil.pushContext(context);
                BusinessObject.revokeAccessRights(context, businessObjects, var6);
                Access access = new Access();
                access.setModifyAccess(true);
                access.setExecuteAccess(true);
                access.setUser(ownerId);
                BusinessObject.grantAccessRights(context, businessObjects, access);
            } finally {
                ContextUtil.popContext(context);
            }
        }

    }

    public void setProgram(Context context, String programIds) throws FrameworkException {
        new StringList();
        StringList newProgramIds = new StringList();
        StringList programIdList = new StringList();
        new StringList();
        StringList newProgramRelList = new StringList();
        String pId = "";
        String projectId = "";
        projectId = this.getId();
        DomainObject projectObj = DomainObject.newInstance(context, projectId);
        boolean suiteRegistered = FrameworkUtil.isSuiteRegistered(context, "appVersionEnterpriseChange", false, (String)null, (String)null);
        if (!projectObj.isKindOf(context, ProgramCentralConstants.TYPE_CHANGE_PROJECT)) {
            programIdList = projectObj.getInfoList(context, "to[" + RELATIONSHIP_PROGRAM_PROJECT + "].from.id");
        }

        StringList programRelIdList = projectObj.getInfoList(context, "to[" + RELATIONSHIP_PROGRAM_PROJECT + "].id");
        String[] var12 = null;
        String[] var13 = null;
        StringList pList = FrameworkUtil.split(programIds, ",");
        if (!pList.isEmpty()) {
            int i;
            for(i = 0; i < pList.size(); ++i) {
                pId = (String)pList.get(i);
                if (!programIdList.contains(pId)) {
                    newProgramIds.add(pId);
                }
            }

            for(i = 0; i < programIdList.size(); ++i) {
                pId = (String)programIdList.get(i);
                int var15 = programIdList.indexOf(pId);
                if (!pList.contains(pId)) {
                    newProgramRelList.add((String)programRelIdList.get(var15));
                }
            }

            if (!newProgramRelList.isEmpty()) {
                var13 = newProgramRelList.toString().substring(1, newProgramRelList.toString().length() - 1).split(",");
                DomainRelationship.disconnect(context, var13);
            }

            if (!newProgramIds.isEmpty()) {
                var12 = newProgramIds.toString().substring(1, newProgramIds.toString().length() - 1).split(",");
                DomainRelationship.connect(context, projectObj, RELATIONSHIP_PROGRAM_PROJECT, false, var12);
            }

            if (suiteRegistered && projectObj.isKindOf(context, DomainConstants.TYPE_CHANGE_PROJECT) && !programRelIdList.isEmpty()) {
                var13 = programRelIdList.toString().substring(1, programRelIdList.toString().length() - 1).split(",");
                DomainRelationship.disconnect(context, var13);
            }
        } else if (!programRelIdList.isEmpty()) {
            var13 = programRelIdList.toString().substring(1, programRelIdList.toString().length() - 1).split(",");
            DomainRelationship.disconnect(context, var13);
        }

    }

    protected void triggerCreateAction(Context var1, String[] var2) throws FrameworkException {
        this._accessListObject = DomainObject.newInstance(var1);
        this._accessListObject.createAndConnect(var1, TYPE_PROJECT_ACCESS_LIST, this.getUniqueName("PAL-"), "", POLICY_PROJECT_ACCESS_LIST, var1.getVault().getName(), RELATIONSHIP_PROJECT_ACCESS_LIST, this, false);

        try {
            this.getObjectId(var1);
            String var4 = ProgramCentralUtil.getPhysicalId(var1, this._accessListObject.getObjectId(var1));
            ProjectSequence var5 = new ProjectSequence(var1, var4);
            var5.assignSequence(var1, (String)null, ProgramCentralUtil.getPhysicalId(var1, this.getObjectId(var1)), (String)null, (String)null, true);
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new FrameworkException(var7);
        }

        DebugUtil.debug("Looking up user id...");
        Person var3 = Person.getPerson(var1);
        DebugUtil.debug("Looking up company id...");
        Company var8 = var3.getCompany(var1);
        DebugUtil.debug("Setting Company/Project relationship");
        String var9;
        if (this.isKindOf(var1, TYPE_PROJECT_TEMPLATE)) {
            var9 = "connect bus $1 preserve relationship $2 to $3";
            MqlUtil.mqlCommand(var1, var9, new String[]{var8.getObjectId(), RELATIONSHIP_COMPANY_PROJECT_TEMPLATES, this.getObjectId()});
        } else {
            var9 = "connect bus $1 preserve relationship $2 to $3";
            MqlUtil.mqlCommand(var1, var9, new String[]{var8.getObjectId(), RELATIONSHIP_COMPANY_PROJECT, this.getObjectId()});
            String var6 = var3.getObjectId();
            DebugUtil.debug("Adding project owner link...");
            this.addMember(var1, var6, "Project Owner");
        }

    }

    public MapList getMembers(Context var1, StringList var2, StringList var3, String var4, String var5, boolean var6, boolean var7) throws FrameworkException {
        MapList var8;
        if (var7) {
            try {
                ContextUtil.pushContext(var1);
                var8 = this.getMembers(var1, var2, var3, var4, var5, var6);
            } catch (Exception var13) {
                throw new FrameworkException(var13);
            } finally {
                ContextUtil.popContext(var1);
            }
        } else {
            var8 = this.getMembers(var1, var2, var3, var4, var5, var6);
        }

        return var8;
    }

    public String getProjectInfo(Context var1, String var2) throws FrameworkException {
        String var3 = "";

        try {
            ContextUtil.pushContext(var1);
            var3 = this.getInfo(var1, var2);
        } catch (Exception var8) {
            throw new FrameworkException(var8);
        } finally {
            ContextUtil.popContext(var1);
        }

        return var3;
    }

    public static StringList getStates(Context var0, String var1) throws FrameworkException {
        StringList var2 = new StringList();
        String var3 = "print policy $1 select $2 dump";
        String var4 = MqlUtil.mqlCommand(var0, true, true, var3, true, new String[]{var1, "state"});
        String[] var5 = var4.split("\\,");
        int var6 = 0;

        for(int var7 = var5.length; var6 < var7; ++var6) {
            var2.add(var5[var6]);
        }

        return var2;
    }

    public DomainRelationship setRelatedObject(Context var1, String var2, boolean var3, String var4) throws FrameworkException {
        try {
            DomainRelationship var5 = null;
            StringList var6 = new StringList(1);
            var6.add("id");
            StringList var7 = new StringList(1);
            var7.add("id[connection]");
            Map var8 = this.getRelatedObject(var1, var2, var3, var6, var7);
            if (var8 != null) {
                String var9 = (String)var8.get("id[connection]");
                if (var4 == null) {
                    DomainRelationship.disconnect(var1, var9);
                } else {
                    String var10 = (String)var8.get("id");
                    if (!var4.equals(var10)) {
                        DomainRelationship.disconnect(var1, var9);
                        var5 = DomainRelationship.connect(var1, DomainObject.newInstance(var1, var4), var2, this);
                    } else {
                        var5 = new DomainRelationship(var9);
                    }
                }
            } else if (var4 != null) {
                var5 = new DomainRelationship(this.connect(var1, new RelationshipType(var2), var3, DomainObject.newInstance(var1, var4)));
            }

            return var5;
        } catch (Exception var11) {
            throw new FrameworkException(var11);
        }
    }

    public void addTask(Context var1, String[] var2, Map var3) throws Exception {
        String var4 = ProgramCentralConstants.SELECT_PROJECT_OBJECT + ".type.kindof[" + DomainObject.TYPE_PROJECT_SPACE + "]";
        boolean var5 = true;
        boolean var6 = true;
        String var7 = PropertyUtil.getGlobalRPEValue(var1, "UseStartAndEndDatesAsEntered");
        StringList var8 = new StringList();
        var8.add("id");
        var8.add(var4);
        MapList var9 = DomainObject.getInfo(var1, var2, var8);
        Iterator var10 = var9.iterator();

        while(var10.hasNext()) {
            Map var11 = (Map)var10.next();
            String var12 = (String)var11.get("id");
            String var13 = (String)var11.get(var4);
            if ("true".equalsIgnoreCase(var13) && "true".equalsIgnoreCase(var7)) {
                PropertyUtil.setGlobalRPEValue(var1, "UseStartAndEndDatesAsEntered", "true");
            } else {
                PropertyUtil.setGlobalRPEValue(var1, "UseStartAndEndDatesAsEntered", "false");
            }

            Task var14 = new Task(var12);
            CheckList var15 = new CheckList();
            var14.addEventListener(var15);
            var14.cloneTaskWithStructure(var1, this, var3, (Map)null, var5, false, var6);
            var14.removeEventListener(var15);
        }

    }

    /** @deprecated */
    public void addTasks(Context var1, String var2, Map var3) throws Exception {
        com.matrixone.apps.common.Task var4 = (com.matrixone.apps.common.Task)DomainObject.newInstance(var1, TYPE_TASK);
        var4.setId(var2);
        Task var5 = new Task(var4);
        var5.cloneStructure(var1, this, var3, (Map)null, true);
    }

    public static MapList getTypeAttributes(Context var0, String var1) throws FrameworkException {
        return DomainObject.getTypeAttributes(var0, var1);
    }

    public static BusinessTypeList getAllSubTypes(Context var0, String var1, BusinessTypeList var2) throws MatrixException {
        if (var2 == null) {
            var2 = new BusinessTypeList();
        }

        BusinessType var3 = new BusinessType(var1, var0.getVault());
        var3.open(var0);
        BusinessTypeList var4 = var3.getChildren(var0);

        BusinessType var6;
        for(Iterator var5 = var4.iterator(); var5.hasNext(); var2 = getAllSubTypes(var0, var6.getName(), var2)) {
            var6 = (BusinessType)var5.next();
            var2.addElement(var6);
        }

        return var2;
    }

    public void setType(Context var1, String var2, String var3, String var4) throws Exception {
        ContextUtil.pushContext(var1);
        String var5 = "modify bus $1 type $2 policy $3";
        MqlUtil.mqlCommand(var1, var5, new String[]{var3, var2, var4});
        ContextUtil.popContext(var1);
    }

    public List getCommonElements(MapList var1, MapList var2) {
        return ListUtil.getCommonElements(var1, var2);
    }

    public String getTypeInclusionList(Context var1, String var2) throws Exception {
        ArrayList var3 = new ArrayList();
        StringBuffer var4 = new StringBuffer(64);
        RelationshipType var5 = new RelationshipType(var2);
        BusinessTypeList var6 = var5.getToTypes(var1, false);
        BusinessTypeItr var7 = new BusinessTypeItr(var6);
        String var8 = "";

        while(true) {
            while(var7.next()) {
                BusinessType var9 = (BusinessType)var7.obj();
                var8 = FrameworkUtil.getBaseType(var1, var9.toString(), var1.getVault());
                if (!"".equals(var8) && !"null".equals(var8) && var8 != null && !var3.contains(var8)) {
                    var3.add(var8);
                } else if (("".equals(var8) || "null".equals(var8) || var8 == null) && !var3.contains(var9.toString())) {
                    var3.add(var9.toString());
                }
            }

            for(int var10 = 0; var10 < var3.size(); ++var10) {
                var4.append((String)var3.get(var10));
                var4.append(",");
            }

            return var4.toString();
        }
    }

    public List getCommonProjects(Context var1, String var2, StringList var3, MapList var4) throws Exception {
        new ArrayList();
        ArrayList var6 = new ArrayList();
        String var7 = PropertyUtil.getSchemaProperty(var1, "relationship_RelatedProjects");
        gscProjectSpace var8 = new gscProjectSpace();
        Pattern var9 = new Pattern(DomainConstants.TYPE_PROJECT_SPACE);
        var9.addPattern(DomainConstants.TYPE_PROJECT_CONCEPT);
        StringList var10 = FrameworkUtil.split(var2, "|");
        int var11 = var10.size();

        for(int var12 = 0; var12 < var11; ++var12) {
            String var13 = (String)var10.get(var12);
            var8.setId(var13);
            MapList var14 = var8.getRelatedObjects(var1, var7, var9.getPattern(), var3, (StringList)null, false, true, (short)1, (String)null, (String)null);
            List var5 = this.getCommonElements(var4, var14);
            int var15 = var5.size();

            for(int var16 = 0; var16 < var15; ++var16) {
                var6.add((String)var5.get(var16));
            }
        }

        return var6;
    }

    public boolean compareToMap(MapList var1, String var2) {
        return ListUtil.compareToMap(var1, var2);
    }

    /** @deprecated */
    public void setDeliverableDSFA(Context var1, String var2, String var3, String var4) throws FrameworkException {
    }

    public MapList getResourceRequests(Context var1, StringList var2, StringList var3, String var4, String var5) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                HashMap var6 = new HashMap();
                if (var2 != null) {
                    var6.put("BusSelects", var2);
                }

                if (var3 != null) {
                    var6.put("RelSelects", var3);
                }

                if (var4 != null) {
                    var6.put("BusWhere", var4);
                }

                if (var5 != null) {
                    var6.put("RelWhere", var5);
                }

                String[] var7 = new String[]{this.getId()};
                String[] var8 = JPO.packArgs(var6);
                return (MapList)JPO.invoke(var1, "emxProjectSpace", var7, "getResourceRequests", var8, MapList.class);
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            throw new MatrixException(var9);
        }
    }

    public MapList getProjectTaskAssignees(Context var1, String var2) throws MatrixException {
        MapList var3 = new MapList();

        try {
            String var4 = "print bus $1 select $2 dump $3;";
            String var5 = MqlUtil.mqlCommand(var1, var4, new String[]{var2, "to[" + ProgramCentralConstants.RELATIONSHIP_PROJECT_ACCESS_LIST + "].from.id", "|"});
            DomainObject var6 = DomainObject.newInstance(var1, var5);
            StringList var7 = new StringList(2);
            var7.add("id");
            var7.add("name");
            var7.add(Person.SELECT_FIRST_NAME);
            var7.add(Person.SELECT_LAST_NAME);
            var7.add("current");
            StringList var8 = new StringList("");
            String var9 = TYPE_TASK_MANAGEMENT + "," + TYPE_PERSON;
            String var10 = RELATIONSHIP_PROJECT_ACCESS_KEY + "," + RELATIONSHIP_ASSIGNED_TASKS;
            MapList var11 = var6.getRelatedObjects(var1, var10, var9, var7, var8, true, true, (short)2, (String)null, (String)null, 0);
            StringList var12 = new StringList();
            Iterator var13 = var11.iterator();

            while(var13.hasNext()) {
                Map var14 = (Map)var13.next();
                String var15 = (String)var14.get("level");
                String var16 = (String)var14.get("id");
                if ("2".equals(var15) && !var12.contains(var16)) {
                    var12.add(var16);
                    var3.add(var14);
                }
            }

            return var3;
        } catch (FrameworkException var17) {
            var17.printStackTrace();
            throw new MatrixException();
        }
    }

    protected MapList getApprovedEffortsForProject(Context var1, String var2) throws MatrixException {
        MapList var3 = new MapList();

        try {
            assert null != var2;

            DomainObject var4 = DomainObject.newInstance(var1, var2);
            StringList var5 = new StringList(3);
            var5.add("id");
            var5.add("name");
            var5.add("current");
            StringList var6 = new StringList(1);
            String var7 = TYPE_EFFORT;
            String var8 = RELATIONSHIP_EFFORTS;
            String var9 = "current ~~ " + ProgramCentralConstants.STATE_EFFORT_APPROVED + " || current ~~ " + ProgramCentralConstants.STATE_EFFORT_SUBMIT;
            MapList var10 = var4.getRelatedObjects(var1, var8, var7, var5, var6, true, true, (short)1, var9, (String)null, 0);
            var3.addAll(var10);
            return var3;
        } catch (FrameworkException var11) {
            throw new MatrixException(var11);
        }
    }

    public boolean isApprovedEffortExistsForProject(Context var1, String var2) throws MatrixException {
        boolean var3 = false;
        MapList var4 = this.getApprovedEffortsForProject(var1, var2);

        assert null != var4;

        if (var4.size() > 0) {
            var3 = true;
        }

        return var3;
    }

    public boolean hasPhase(Context var1) throws MatrixException {
        StringList var2 = null;
        MapList var3 = null;

        try {
            if (!this.isKindOf(var1, ProgramCentralConstants.TYPE_PROJECT_SPACE) && !this.isKindOf(var1, ProgramCentralConstants.TYPE_PROJECT_TEMPLATE) && !this.isKindOf(var1, ProgramCentralConstants.TYPE_PROJECT_CONCEPT)) {
                throw new MatrixException("Invalid Project Management object");
            } else {
                var2 = new StringList();
                var2.add("id");
                var3 = this.getRelatedObjects(var1, RELATIONSHIP_SUBTASK, TYPE_PHASE, var2, (StringList)null, false, true, (short)1, (String)null, "");
                return var3.size() != 0;
            }
        } catch (FrameworkException var5) {
            throw new MatrixException(var5);
        }
    }

    public MapList getAllWBSTasks(Context var1, StringList var2, String var3) throws MatrixException {
        String var4 = PropertyUtil.getSchemaProperty(var1, "relationship_ProjectAccessList");
        String var5 = PropertyUtil.getSchemaProperty(var1, "relationship_ProjectAccessKey");
        String var6 = PropertyUtil.getSchemaProperty(var1, "type_TaskManagement");
        String var7 = "to[" + var4 + "].from.id";

        try {
            String var9 = this.getInfo(var1, var7);
            DomainObject var10 = DomainObject.newInstance(var1, var9);
            return var10.getRelatedObjects(var1, var5, var6, var2, (StringList)null, false, true, (short)1, var3, (String)null, 0);
        } catch (FrameworkException var11) {
            throw new MatrixException(var11);
        }
    }

    String getBaseCurrency(Context var1) throws MatrixException {
        String var2 = this.getInfo(var1, ProgramCentralConstants.SELECT_ATTRIBUTE_CURRENCY);
        return var2;
    }

    private String getAutoName(Context var1, String var2) throws FrameworkException {
        String var3 = PropertyUtil.getAliasForAdmin(var1, "type", ProgramCentralConstants.TYPE_PROJECT_SNAPSHOT, true);
        String var4 = PropertyUtil.getAliasForAdmin(var1, "policy", ProgramCentralConstants.POLICY_PROJECT_SPACE, true);
        String var5 = FrameworkUtil.autoName(var1, var3, (String)null, var4, (String)null, (String)null, true, true);
        if (ProgramCentralConstants.TYPE_EXPERIMENT.equalsIgnoreCase(var2)) {
            var5 = var5.replace("P", "Exp");
        } else if (ProgramCentralConstants.TYPE_PROJECT_SNAPSHOT.equalsIgnoreCase(var2)) {
            var5 = var5.replace("P", "Snp");
        }

        return var5;
    }

    private static void updateNewTask(Context var0, MapList var1) throws MatrixException {
        var1.sort("level", "descending", "integer");
        Iterator var4 = var1.iterator();

        while(true) {
            while(true) {
                String var6;
                String var7;
                String var8;
                String var9;
                String var10;
                do {
                    if (!var4.hasNext()) {
                        return;
                    }

                    Map var5 = (Map)var4.next();
                    var6 = (String)var5.get("newTaskId");
                    var7 = (String)var5.get("state");
                    var8 = (String)var5.get("percentValue");
                    var9 = (String)var5.get("type");
                    var10 = (String)var5.get("policy");
                } while(!ProgramCentralUtil.isNotNullString(var6));

                DomainObject var11 = DomainObject.newInstance(var0, var6);
                var11.setAttributeValue(var0, ProgramCentralConstants.ATTRIBUTE_PERCENT_COMPLETE, var8);
                if (var9.equalsIgnoreCase(ProgramCentralConstants.TYPE_GATE) && var7.equalsIgnoreCase(ProgramCentralConstants.STATE_PROJECT_TASK_COMPLETE) && ProgramCentralConstants.POLICY_PROJECT_REVIEW.equals(var10)) {
                    HashMap var12 = new HashMap();
                    var12.put("objectId", var6);
                    var12.put("switch", "Approve");
                    HashMap var13 = new HashMap();
                    var13.put("requestMap", var12);
                    HashMap var14 = new HashMap();
                    var14.put("Description", "Approved Or ConditionallyApproved");
                    HashMap var15 = new HashMap();
                    var15.put("formMap", var13);
                    var15.put("paramMap", var14);

                    String[] var16;
                    try {
                        var16 = JPO.packArgs(var15);
                    } catch (Exception var18) {
                        throw new MatrixException(var18);
                    }

                    JPO.invoke(var0, "emxProjectHoldAndCancel", (String[])null, "projectPolicyChangeSequence", var16, (Class)null);
                } else {
                    var11.setState(var0, var7);
                }
            }
        }
    }

    private static void cloneDependency(Context var0, MapList var1, Map var2) throws MatrixException {
        Iterator var3 = var1.iterator();

        while(true) {
            Map var4;
            Object var5;
            DomainObject var7;
            do {
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    var4 = (Map)var3.next();
                    var5 = var4.get(SELECT_PREDECESSOR_IDS);
                } while(var5 == null);

                String var6 = (String)var4.get("id");
                var7 = (DomainObject)var2.get(var6);
            } while(var7 == null);

            Object var8 = var4.get(SELECT_PREDECESSOR_TYPES);
            Object var9 = var4.get(SELECT_PREDECESSOR_LAG_TIMES);
            Object var10 = null;
            Object var11 = null;
            Object var12 = null;
            if (var5 instanceof String) {
                var10 = new ArrayList(1);
                ((List)var10).add(var5);
                var11 = new ArrayList(1);
                ((List)var11).add(var8);
                var12 = new ArrayList(1);
                ((List)var12).add(var9);
            } else {
                var10 = (List)var5;
                var11 = (List)var8;
                var12 = (List)var9;
            }

            for(int var13 = 0; var13 < ((List)var10).size(); ++var13) {
                String var14 = (String)((List)var10).get(var13);
                String var15 = (String)((List)var11).get(var13);
                String var16 = (String)((List)var12).get(var13);
                DomainObject var17 = (DomainObject)var2.get(var14);
                if (var17 != null) {
                    String var18 = "connect bus $1 preserve relationship $2 to $3 $4 $5 $6 $7";
                    MqlUtil.mqlCommand(var0, var18, new String[]{var7.getObjectId(), RELATIONSHIP_DEPENDENCY, var17.getObjectId(), ATTRIBUTE_DEPENDENCY_TYPE, var15, DependencyRelationship.ATTRIBUTE_LAG_TIME, var16});
                }
            }
        }
    }

    private void createAndConnectTask(Context var1, String var2, String var3, String var4, DomainObject var5, String var6, Map var7, Map var8, String var9, String var10, ProjectSequence var11) throws MatrixException {
        try {
            DomainRelationship var12 = this.createAndConnect(var1, var2, var3, this.getUniqueName(""), var4, var1.getVault().getName(), ProgramCentralConstants.RELATIONSHIP_SUBTASK, var5, true);
            DomainObject var13 = this.getPALObject(var1, var5);
            String var14 = this.getId(var1);
            if (var13 != null) {
                DomainRelationship.connect(var1, var13, RELATIONSHIP_PROJECT_ACCESS_KEY, this);
                String var15;
                if (var11 == null) {
                    var15 = ProgramCentralUtil.getPhysicalId(var1, var13.getObjectId(var1));
                    var11 = new ProjectSequence(var1, var15);
                }

                var15 = ProgramCentralUtil.getPhysicalId(var1, var14);
                var11.assignSequence(var1, var6, var15, (String)null, (String)null, false);
            }

            if (var8 != null) {
                var12.setAttributeValues(var1, var8);
            }

            if (var7 != null) {
                this.setAttributeValues(var1, var7);
            }

            if (var9 != null) {
                this.setDescription(var1, var9);
            }

        } catch (FoundationException var16) {
            var16.printStackTrace();
            throw new MatrixException(var16);
        }
    }

    private DomainObject getPALObject(Context var1, DomainObject var2) throws MatrixException {
        DomainObject var3 = null;
        String var4 = "";
        StringList var5 = new StringList();
        var5.add(SELECT_PROJECT_ACCESS_LIST_ID_FOR_TASK);
        var5.add(SELECT_PROJECT_ACCESS_LIST_ID_FOR_PROJECT);
        Map var6 = var2.getInfo(var1, var5);
        var4 = (String)var6.get(SELECT_PROJECT_ACCESS_LIST_ID_FOR_TASK);
        if (var4 == null || "".equals(var4) || "null".equals(var4)) {
            var4 = (String)var6.get(SELECT_PROJECT_ACCESS_LIST_ID_FOR_PROJECT);
        }

        if (var4 != null && !"".equals(var4) && !"null".equals(var4)) {
            var3 = DomainObject.newInstance(var1, var4);
        }

        return var3;
    }

    private static String updateOwner(Context var0, String var1, String var2) throws MatrixException {
        DomainObject var3 = DomainObject.newInstance(var0, var1);
        String var4 = var3.getOwner(var0).getName();
        var3.setOwner(var0, var2);
        return var4;
    }

    private static MapList getSubTaskList(Context var0, String var1) throws MatrixException {
        String var2 = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_DURATION + "].inputunit";
        String var3 = "to[" + ProgramCentralConstants.RELATIONSHIP_EXPERIMENT + "].from.id";
        String var4 = "to[" + RELATIONSHIP_DEPENDENCY + "].from.id";
        com.matrixone.apps.common.Task var5 = (com.matrixone.apps.common.Task)newInstance(var0, TYPE_TASK);
        var5.setId(var1);
        StringList var6 = new StringList();
        var6.add("id");
        var6.add("type");
        var6.add("name");
        var6.add("policy");
        var6.add("description");
        var6.add("vault");
        var6.add("current");
        var6.add("owner");
        var6.add(SELECT_PREDECESSOR_IDS);
        var6.add(var4);
        var6.add(SELECT_PREDECESSOR_TYPES);
        var6.add(SELECT_PREDECESSOR_DURATION_KEYWORD);
        var6.add(SELECT_PREDECESSOR_LAG_TIMES);
        var6.add(var2);
        if (var5.getType(var0).equals(ProgramCentralConstants.TYPE_EXPERIMENT)) {
            var6.add(var3);
        }

        StringList var7 = new StringList();
        var7.add(SubtaskRelationship.SELECT_TASK_WBS);
        var7.add(SubtaskRelationship.SELECT_SEQUENCE_ORDER);
        MapList var8 = com.matrixone.apps.common.Task.getTasks(var0, var5, 0, var6, var7);
        return var8;
    }

    protected void connectWithMasterProject(Context var1, String var2, String var3, String var4) throws Exception {
        DomainRelationship.connect(var1, var2, var4, var3, true);
    }

    public gscProjectSnapshot createSnapshot(Context var1) throws MatrixException {
        String var2 = this.getId(var1);

        assert var2 != null;

        return this.createSnapshot(var1, true);
    }

    public gscProjectSnapshot createSnapshot(Context var1, boolean var2) throws MatrixException {
        String var3 = this.getId(var1);

        assert var3 != null;

        new gscProjectSnapshot(this);

        try {
            this.getMasterProjectInfo(var1);
            ContextUtil.startTransaction(var1, true);
            gscProjectSnapshot var4 = (gscProjectSnapshot)this.createProjectSubType(var1, ProgramCentralConstants.TYPE_PROJECT_SNAPSHOT, var2);
            String var6 = var4.getId(var1);
            var4.setMasterProject(var1, this);
            this.cloneWBSStructureToChildProject(var1, var6);
            com.matrixone.apps.common.Task var7 = new com.matrixone.apps.common.Task(var6);
            var7.rollupAndSave(var1);
            connectProjects(var1, this, var4, "Related Projects", (Map)null);
            var4.setEffectivity(var1);
            ContextUtil.commitTransaction(var1);
            return var4;
        } catch (Exception var8) {
            ContextUtil.abortTransaction(var1);
            throw new MatrixException(var8);
        }
    }

    protected Map getMasterProjectInfo(Context var1) throws MatrixException {
        assert this.getId(var1) != null;

        StringList var2 = new StringList();
        var2.add("type");
        var2.add("name");
        var2.add("policy");
        var2.add("vault");
        var2.add("current");
        var2.add("description");
        var2.add("owner");
        Map var3 = this.getInfo(var1, var2);
        return var3 == null ? Collections.EMPTY_MAP : var3;
    }

    private gscProjectSpace createProjectSubType(Context var1, String var2, boolean var3) throws MatrixException {
        this.getId(var1);
        Map var5 = this.getMasterProjectInfo(var1);
        gscProjectSpace var6 = (gscProjectSpace)DomainObject.newInstance(var1, ProgramCentralConstants.TYPE_PROJECT_SPACE, "Program");
        gscProjectSnapshot var7 = null;
        String var8 = (String)var5.get("name");
        String var9 = (String)var5.get("type");
        String var10 = (String)var5.get("policy");
        String var11 = (String)var5.get("vault");
        String var12 = (String)var5.get("description");
        if (var10.equalsIgnoreCase(ProgramCentralConstants.POLICY_PROJECT_SPACE_HOLD_CANCEL)) {
            var10 = ProgramCentralConstants.POLICY_PROJECT_SPACE;
        }

        var6.create(var1, var2, var8, var10, var1.getVault().getName(), (Map)null, var12);
        if (var3) {
            var8 = this.getAutoName(var1, var2);
        }

        var6.change(var1, var2, var8, var6.getRevision(), var11, var10);
        String var13 = var6.getObjectId();
        var7 = (gscProjectSnapshot)DomainObject.newInstance(var1, var2, "Program");
        var7.setId(var13);
        this.addMembers(var1, var13);
        this.setProjectAttributesOnChild(var1, var13);
        return var7;
    }

    private void setProjectAttributesOnChild(Context var1, String var2) throws MatrixException {
        DomainObject var3 = DomainObject.newInstance(var1);
        var3.setId(var2);
        Map var4 = this.getAttributeMap(var1, true);
        Map var5 = this.getMasterProjectInfo(var1);
        String var6 = (String)var5.get("current");
        String var7 = (String)var5.get("owner");
        String var8 = updateOwner(var1, var2, var7);
        var4.put(DomainObject.ATTRIBUTE_ORIGINATOR, var8);
        var3.setAttributeValues(var1, var4);
        var3.setState(var1, var6);
    }

    protected void cloneWBSStructureToChildProject(Context var1, String var2) throws MatrixException {
        String var3 = this.getId(var1);

        assert var3 != null;

        DomainObject var7 = DomainObject.newInstance(var1, var2);
        DomainObject var8 = DomainObject.newInstance(var1);
        MapList var9 = getSubTaskList(var1, var3);
        StringList var10 = new StringList(2);
        var10.add("physicalid");
        var10.add(SELECT_PROJECT_ACCESS_LIST_ID);
        BusinessObjectWithSelect var11 = var7.select(var1, var10);
        String var12 = var11.getSelectData("physicalid");
        String var13 = var11.getSelectData(SELECT_PROJECT_ACCESS_LIST_ID);
        HashMap var14 = new HashMap(2);
        HashMap var15 = new HashMap();
        HashMap var16 = new HashMap(1);
        var16.put("0", var7);
        if (var9 != null && !var9.isEmpty()) {
            StringList var17 = new StringList();
            MapList var18 = new MapList();
            StringList var19 = new StringList();
            var19.addElement(ProgramCentralConstants.SELECT_IS_KINDOF_PROJECT_SNAPSHOT);
            var19.addElement(ProgramCentralConstants.SELECT_KINDOF_EXPERIMENT_PROJECT);
            Map var20 = var7.getInfo(var1, var19);
            String var21 = (String)var20.get(ProgramCentralConstants.SELECT_IS_KINDOF_PROJECT_SNAPSHOT);
            String var22 = (String)var20.get(ProgramCentralConstants.SELECT_KINDOF_EXPERIMENT_PROJECT);
            boolean var23 = "true".equalsIgnoreCase(var21);
            boolean var24 = "true".equalsIgnoreCase(var22);
            int var25 = var9.size();
            String[] var26 = new String[var25];

            for(int var27 = 0; var27 < var25; ++var27) {
                Map var28 = (Map)var9.get(var27);
                var26[var27] = (String)var28.get("id");
            }

            var19.addElement(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
            MapList var64 = DomainObject.getInfo(var1, var26, var19);
            Iterator var65 = var9.iterator();
            int var29 = 0;
            ProjectSequence var30 = null;

            try {
                var30 = new ProjectSequence(var1, var13);
                var30.startUpdateSession(var1);

                while(var65.hasNext()) {
                    Map var31 = (Map)var65.next();
                    String var32 = (String)var31.get("level");
                    String var33 = String.valueOf(Integer.parseInt(var32) - 1);
                    DomainObject var34 = (DomainObject)var16.get(var33);
                    if (!var17.contains(var32)) {
                        var17.addElement(var32);
                    }

                    String var35 = (String)var31.get("id");
                    String var36 = (String)var31.get("type");
                    String var37 = (String)var31.get("name");
                    String var38 = (String)var31.get("description");
                    String var39 = (String)var31.get("policy");
                    String var40 = (String)var31.get("vault");
                    String var41 = (String)var31.get("current");
                    String var42 = (String)var31.get(SubtaskRelationship.SELECT_SEQUENCE_ORDER);
                    String var43 = (String)var31.get(SubtaskRelationship.SELECT_TASK_WBS);
                    String var44 = (String)var31.get("owner");
                    String var45 = (String)var31.get(ProgramCentralConstants.SELECT_TASK_ESTIMATED_DURATION_INPUTUNIT);
                    var14.put(SubtaskRelationship.ATTRIBUTE_TASK_WBS, var43);
                    var14.put(DomainObject.ATTRIBUTE_SEQUENCE_ORDER, var42);
                    var8.setId(var35);
                    Map var46 = var8.getAttributeMap(var1, true);
                    String var47 = (String)var46.get(ProgramCentralConstants.ATTRIBUTE_PERCENT_COMPLETE);
                    var46.put(ProgramCentralConstants.ATTRIBUTE_PERCENT_COMPLETE, "0");
                    String var48 = var46.get(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_DURATION).toString();
                    double var49 = com.matrixone.apps.common.Task.parseToDouble(var48);
                    double var51 = this.getDenormalizedDuration(var1, var49, var45);
                    var46.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_DURATION, var51 + " " + var45);
                    String var53 = (String)var46.get(com.matrixone.apps.common.Task.ATTRIBUTE_ESTIMATED_DURATION_KEYWORD);
                    if (var53 == null || !var53.isEmpty()) {
                        var46.put(com.matrixone.apps.common.Task.ATTRIBUTE_ESTIMATED_DURATION_KEYWORD, "");
                    }

                    String var54 = "";
                    Map var55 = (Map)var64.get(var29);
                    String var56 = (String)var55.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
                    boolean var57 = "true".equalsIgnoreCase(var56);
                    Object var58 = null;
                    if (var57) {
                        if (var23) {
                            gscProjectSpace var59 = new gscProjectSpace(var8);
                            var58 = var59.createSnapshot(var1, true);
                        } else if (var24) {
                            new Experiment();
                            boolean var60 = false;
                        }

                        var54 = ((gscProjectSpace)var58).getId(var1);
                        connectProjects(var1, var34, (gscProjectSpace)var58, ProgramCentralConstants.RELATIONSHIP_SUBTASK, var14);
                    } else {
                        var58 = new gscProjectSpace();
                        ((gscProjectSpace)var58).setId(var35);
                        ((gscProjectSpace)var58).createAndConnectTask(var1, var36, var37, var39, var34, var12, var46, var14, var38, var1.getVault().getName(), var30);
                        var54 = ((gscProjectSpace)var58).getId(var1);
                    }

                    connectWithParentTask(var1, var35, var54);
                    String var62 = updateOwner(var1, var54, var44);
                    var46.put(DomainObject.ATTRIBUTE_ORIGINATOR, var62);
                    ((gscProjectSpace)var58).setAttributeValues(var1, var46);
                    var15.put(var35, var58);
                    var16.put(var32, var58);
                    HashMap var63 = new HashMap();
                    var63.put("level", var32);
                    var63.put("newTaskId", var54);
                    var63.put("state", var41);
                    var63.put("percentValue", var47);
                    var63.put("type", var36);
                    var63.put("policy", var39);
                    var18.add(var63);
                    ++var29;
                }

                var30.finishUpdateSession(var1);
                cloneDependency(var1, var9, var15);
                updateNewTask(var1, var18);
            } catch (FoundationException var61) {
                var61.printStackTrace();
                throw new MatrixException(var61);
            }
        }

    }

    private static void connectWithParentTask(Context var0, String var1, String var2) throws MatrixException {
        Experiment var3 = null;

        try {
            var3 = new Experiment(var1);
        } catch (Exception var9) {
            throw new MatrixException(var9);
        }

        boolean var4 = var3.checkAccess(var0, (short)1);
        if (var4) {
            DomainRelationship.connect(var0, var1, ProgramCentralConstants.RELATIONSHIP_EXPERIMENT, var2, true);
        } else {
            try {
                ContextUtil.pushContext(var0);
                DomainRelationship.connect(var0, var1, ProgramCentralConstants.RELATIONSHIP_EXPERIMENT, var2, true);
            } finally {
                ContextUtil.popContext(var0);
            }
        }

    }

    protected static void connectProjects(Context var0, DomainObject var1, gscProjectSpace var2, String var3, Map var4) throws MatrixException {
        assert var1 != null;

        assert var2 != null;

        assert ProgramCentralUtil.isNotNullString(var3);

        DomainRelationship var5 = DomainRelationship.connect(var0, var1, var3, var2);
        if (var4 != null) {
            var5.setAttributeValues(var0, var4);
        }

    }

    protected boolean isFirstSnapshot(Context var1) throws MatrixException {
        StringList var2 = this.getInfoList(var1, "from[Related Projects].to.id");
        int var3 = var2 != null ? var2.size() : 0;
        return var3 == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    private void addMembers(Context var1, String var2) throws MatrixException {
        assert this.getId(var1) != null;

        gscProjectSpace var4 = new gscProjectSpace();
        var4.setId(var2);
        DomainObject var5 = DomainObject.newInstance(var1, var2);
        StringList var6 = var5.getInfoList(var1, "from[Member].id");
        System.out.println("=============Added For ODT LOG:START===============");
        System.out.println("slExpMemberList : :" + var6);

        for(int var7 = 0; var7 < var6.size(); ++var7) {
            String var8 = (String)var6.get(var7);
            System.out.println("i=" + var7 + "::strMemberRelId=" + var8);
            DomainRelationship.disconnect(var1, var8);
        }

        System.out.println("=============Added For ODT LOG:END===============");
        MapList var16 = this.getMasterProjectMemberList(var1);

        for(int var17 = 0; var17 < var16.size(); ++var17) {
            Map var9 = (Map)var16.get(var17);
            String var10 = (String)var9.get("id");
            String var11 = (String)var9.get(MemberRelationship.SELECT_PROJECT_ACCESS);
            String var12 = (String)var9.get(MemberRelationship.SELECT_PROJECT_ROLE);
            HashMap var13 = new HashMap();
            var13.put(DomainObject.ATTRIBUTE_PROJECT_ACCESS, var11);
            var13.put(DomainObject.ATTRIBUTE_PROJECT_ROLE, var12);
            DomainObject var14 = DomainObject.newInstance(var1, var10);
            DomainRelationship var15 = DomainRelationship.connect(var1, var5, ProgramCentralConstants.RELATIONSHIP_MEMBER, var14);
            var15.setAttributeValues(var1, var13);
        }

    }

    private MapList getMasterProjectMemberList(Context var1) throws MatrixException {
        assert this.getId(var1) != null;

        StringList var2 = new StringList();
        var2.addElement("id");
        StringList var3 = new StringList(2);
        var3.add(MemberRelationship.SELECT_PROJECT_ROLE);
        var3.add(MemberRelationship.SELECT_PROJECT_ACCESS);
        MapList var4 = this.getMembers(var1, var2, var3, (String)null, (String)null);
        return var4;
    }

    public void setDefaultEffectivity(Context var1) throws MatrixException {
        StringList var2 = new StringList();
        var2.addElement(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE);
        var2.addElement(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
        var2.addElement("id");
        Map var3 = this.getInfo(var1, var2);
        String var4 = (String)var3.get(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE);
        String var5 = (String)var3.get("id");
        Map var6 = formatDateEffectivityExpression(var1, var4, "");
        setDateEffectivity(var1, var5, var6, true);
    }

    public static Map formatDateEffectivityExpression(Context var0, String var1, String var2) throws MatrixException {
        EffectivityFramework var3 = new EffectivityFramework();
        double var4 = getClientTimeZone(var0);
        String var6 = var3.getInfinitySymbolKeyIn(var0);
        if (ProgramCentralUtil.isNullString(var1)) {
            var1 = var6;
        }

        if (ProgramCentralUtil.isNullString(var2)) {
            var2 = var6;
        }

        if (!var6.equalsIgnoreCase(var1)) {
            var1 = ProgramCentralUtil.parseDateUsingMatrixFormat(var0, var1);
        }

        if (!var6.equalsIgnoreCase(var2)) {
            var2 = ProgramCentralUtil.parseDateUsingMatrixFormat(var0, var2);
        }

        String var7 = var1 + "~" + var2;

        try {
            Map var8 = var3.formatExpression(var0, "Date", var7, var4);
            return var8;
        } catch (Exception var10) {
            throw new MatrixException(var10);
        }
    }

    public static void setDateEffectivity(Context var0, String var1, Map var2, boolean var3) throws MatrixException {
        EffectivityFramework var4 = new EffectivityFramework();
        if (var2 != null && !ProgramCentralUtil.isNullString(var1)) {
            String var5 = (String)var2.get("actualValue");
            if (ProgramCentralUtil.isNullString(var5)) {
                throw new IllegalArgumentException("Actual formatted effectivity expression is null");
            } else {
                if (var3) {
                    EffectivityFramework.setObjectEffectivityTypes(var0, var1, "Date");
                    var4.setObjectExpression(var0, var1, var5);
                } else {
                    DomainObject var6 = DomainObject.newInstance(var0, var1);
                    StringList var7 = new StringList();
                    var7.addElement("to[Related Projects].id");
                    Map var8 = var6.getInfo(var0, var7);
                    String var9 = (String)var8.get("to[Related Projects].id");
                    if (ProgramCentralUtil.isNotNullString(var9)) {
                        EffectivityFramework.setRelEffectivityTypes(var0, var9, "Date");
                        var4.setRelExpression(var0, var9, var5);
                    }
                }

            }
        } else {
            throw new IllegalArgumentException("Input map is null OR object id is null");
        }
    }

    protected Map getEffectivityExpression(Context var1, boolean var2) throws MatrixException {
        String var3 = this.getId(var1);

        assert ProgramCentralUtil.isNotNullString(var3);

        EffectivityFramework var4 = new EffectivityFramework();
        Map var5 = null;

        try {
            MapList var6 = null;
            if (var2) {
                var6 = var4.getObjectExpression(var1, var3);
            } else {
                String var7 = this.getInfo(var1, "to[Related Projects].id");
                var6 = var4.getRelExpression(var1, var7);
            }

            if (var6 != null) {
                var5 = (Map)var6.get(0);
            }
        } catch (Exception var8) {
            throw new MatrixException(var8);
        }

        if (var5 == null) {
            var5 = Collections.EMPTY_MAP;
        }

        return var5;
    }

    protected static double getClientTimeZone(Context var0) {
        TimeZone var1 = TimeZone.getTimeZone(var0.getSession().getTimezone());
        int var2 = var1.getRawOffset();
        Double var3 = new Double((double)(var2 / 3600000));
        return var3;
    }

    public static String getGoverningProjectPlanId(Context var0, String var1) throws MatrixException {
        String var2 = "";
        StringList var3 = new StringList();
        var3.add(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
        var3.add(ProgramCentralConstants.SELECT_RELATIONSHIP_GOVERNING_PROJECT_ID);
        var3.add(ProgramCentralConstants.SELECT_RELATIONSHIP_DELIVERS_ID);
        var3.add(ProgramCentralConstants.SELECT_GOVERNING_PROJECT_PLAN);
        var3.add(ProgramCentralConstants.SELECT_GOVERNING_PROJECT_PLAN_FROM_DELIVERS);
        DomainObject var4 = DomainObject.newInstance(var0, var1);
        Map var5 = var4.getInfo(var0, var3);
        String var6 = (String)var5.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_SPACE);
        String var7 = (String)var5.get(ProgramCentralConstants.SELECT_RELATIONSHIP_GOVERNING_PROJECT_ID);
        String var8 = (String)var5.get(ProgramCentralConstants.SELECT_RELATIONSHIP_DELIVERS_ID);
        String var9 = (String)var5.get(ProgramCentralConstants.SELECT_GOVERNING_PROJECT_PLAN);
        String var10 = (String)var5.get(ProgramCentralConstants.SELECT_GOVERNING_PROJECT_PLAN_FROM_DELIVERS);
        String var11;
        if (ProgramCentralUtil.isNotNullString(var7)) {
            var11 = (String)var5.get(ProgramCentralConstants.SELECT_GOVERNING_PROJECT_PLAN);
            if (ProgramCentralUtil.isNotNullString(var11)) {
                var2 = var11;
            }
        } else if ("true".equalsIgnoreCase(var6)) {
            var2 = var1;
        } else if (ProgramCentralUtil.isNotNullString(var8)) {
            var11 = (String)var5.get(ProgramCentralConstants.SELECT_GOVERNING_PROJECT_PLAN_FROM_DELIVERS);
            if (ProgramCentralUtil.isNotNullString(var11)) {
                var2 = var11;
            }
        }

        return var2;
    }

    public void deleteSnapshot(Context var1, String var2) throws MatrixException {
        if (ProgramCentralUtil.isNullString(var2)) {
            throw new IllegalArgumentException("Snapshot Id is null");
        } else {
            gscProjectSnapshot var3 = (gscProjectSnapshot) gscProjectSnapshot.newInstance(var1, ProgramCentralConstants.TYPE_PROJECT_SNAPSHOT, "Program");
            Task var4 = (Task)DomainObject.newInstance(var1, DomainConstants.TYPE_TASK, "Program");
            var3.setId(var2);

            try {
                ContextUtil.startTransaction(var1, true);
                Map var5 = var3.getMasterProjectInfo(var1);
                String var6 = (String)var5.get("current");
                String var7 = (String)var5.get("owner");
                StringList var8 = getDependentProjects(var1, var2);
                this.deleteProjectSubTypeWBSStructure(var1, var2, ProgramCentralConstants.TYPE_PROJECT_SNAPSHOT);
                if (!var6.equalsIgnoreCase(ProgramCentralConstants.STATE_PROJECT_SPACE_CREATE)) {
                    var3.setState(var1, ProgramCentralConstants.STATE_PROJECT_SPACE_CREATE);
                }

                String var9 = var1.getUser();
                if (var9.equals(var7)) {
                    var3.deleteObject(var1);
                } else {
                    try {
                        ContextUtil.pushContext(var1, PropertyUtil.getSchemaProperty(var1, "person_UserAgent"), "", "");
                        var3.deleteObject(var1);
                    } finally {
                        ContextUtil.popContext(var1);
                    }
                }

                int var10 = var8 != null ? var8.size() : 0;

                for(int var11 = 0; var11 < var10; ++var11) {
                    String var12 = (String)var8.get(var11);
                    var4.setId(var12);
                    var4.reSequence(var1, var12);
                    var4.rollupAndSave(var1);
                }

                ContextUtil.commitTransaction(var1);
            } catch (Exception var16) {
                ContextUtil.abortTransaction(var1);
                throw new MatrixException(var16);
            }
        }
    }

    private static StringList getDependentProjects(Context var0, String var1) throws MatrixException {
        StringList var2 = new StringList();
        MapList var3 = getSubTaskList(var0, var1);
        StringList var4 = new StringList();

        int var5;
        int var9;
        for(var5 = 0; var5 < var3.size(); ++var5) {
            Map var6 = (Map)var3.get(var5);
            Object var7 = var6.get(SELECT_SUCCESSOR_IDS);
            if (var7 instanceof StringList) {
                StringList var8 = (StringList)var7;

                for(var9 = 0; var9 < var8.size(); ++var9) {
                    var4.addElement((String)var8.get(var9));
                }
            } else if (var7 != null) {
                var4.addElement((String)var7);
            }
        }

        var5 = var4.size();
        String[] var12 = new String[var5];

        for(int var13 = 0; var13 < var5; ++var13) {
            String var15 = (String)var4.get(var13);
            var12[var13] = var15;
        }

        StringList var14 = new StringList();
        var14.addElement(ProgramCentralConstants.SELECT_PROJECT_ID);
        MapList var16 = DomainObject.getInfo(var0, var12, var14);
        var5 = var16.size();

        for(var9 = 0; var9 < var5; ++var9) {
            Map var10 = (Map)var16.get(var9);
            String var11 = (String)var10.get(ProgramCentralConstants.SELECT_PROJECT_ID);
            if (var11 != null && !var1.equals(var11)) {
                var2.addElement(var11);
            }
        }

        return var2;
    }

    private void deleteProjectSubTypeWBSStructure(Context var1, String var2, String var3) throws MatrixException {
        MapList var4 = getSubTaskList(var1, var2);
        var4.sort("level", "descending", "integer");
        boolean var5 = true;
        int var6 = var4 != null ? var4.size() : 0;
        String[] var7 = new String[var6];

        for(int var8 = 0; var8 < var6; ++var8) {
            Map var9 = (Map)var4.get(var8);
            String var10 = (String)var9.get("id");
            String var11 = (String)var9.get("current");
            String var12 = (String)var9.get("type");
            String var13 = (String)var9.get("policy");
            if (var12.equalsIgnoreCase(var3) && var5) {
                var5 = ProgramCentralUtil.hasAccessToModifyProject(var1, var10);
            }

            DomainObject var14 = DomainObject.newInstance(var1, var10);
            if (!var11.equalsIgnoreCase(ProgramCentralConstants.STATE_PROJECT_TASK_CREATE)) {
                if ((var12.equalsIgnoreCase(ProgramCentralConstants.TYPE_GATE) || var12.equalsIgnoreCase(ProgramCentralConstants.TYPE_MILESTONE)) && var13.equalsIgnoreCase(ProgramCentralConstants.POLICY_PROJECT_REVIEW) && var11.equalsIgnoreCase(ProgramCentralConstants.STATE_PROJECT_TASK_COMPLETE)) {
                    try {
                        ContextUtil.pushContext(var1, PropertyUtil.getSchemaProperty(var1, "person_UserAgent"), "", "");
                        var14.setState(var1, ProgramCentralConstants.STATE_PROJECT_TASK_CREATE);
                    } finally {
                        ContextUtil.popContext(var1);
                    }
                }

                var14.setState(var1, ProgramCentralConstants.STATE_PROJECT_TASK_CREATE);
            }

            var7[var8] = var10;
        }

        try {
            com.matrixone.apps.common.Task var26 = new com.matrixone.apps.common.Task(var2);
            if (var5) {
                var26.delete(var1, var7);
            } else {
                try {
                    ContextUtil.pushContext(var1, PropertyUtil.getSchemaProperty(var1, "person_UserAgent"), "", "");
                    var26.delete(var1, var7);
                } finally {
                    ContextUtil.popContext(var1);
                }
            }

        } catch (Exception var24) {
            throw new MatrixException(var24);
        }
    }

    public String getEffectivityStartDate(Context var1) throws MatrixException {
        return this.getEffectivityStartDate(var1, true);
    }

    protected String getEffectivityStartDate(Context var1, boolean var2) throws MatrixException {
        String var3 = this.getId(var1);
        if (ProgramCentralUtil.isNullString(var3)) {
            throw new IllegalArgumentException("No project businees object");
        } else {
            String var4 = "";
            Map var5 = this.getEffectivityExpressionOnMasterProject(var1);
            if (var2) {
                var4 = this.evaluateDisplayEffectivityEndDateFromExpression(var1, var5);
            } else {
                var4 = this.evaluateActualEffectivityEndDateFromExpression(var1, var5);
            }

            if (ProgramCentralUtil.isNullString(var4)) {
                StringList var6 = new StringList();
                var6.addElement(ProgramCentralConstants.SELECT_PROJECT_DEFAULT_EFFECTIVITY_START_DATE);
                Map var7 = this.getInfo(var1, var6);
                var4 = (String)var7.get(ProgramCentralConstants.SELECT_PROJECT_DEFAULT_EFFECTIVITY_START_DATE);
            }

            return ProgramCentralUtil.isNotNullString(var4) ? var4 : "";
        }
    }

    public String getEffectivityEndDate(Context var1) throws MatrixException {
        String var2 = this.getId(var1);
        if (ProgramCentralUtil.isNullString(var2)) {
            throw new IllegalArgumentException("No project businees object");
        } else {
            StringList var3 = new StringList();
            var3.addElement(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ACTUAL_FINISH_DATE);
            var3.addElement("current");
            Map var4 = this.getInfo(var1, var3);
            String var5 = (String)var4.get("current");
            String var6 = "";
            if (ProgramCentralConstants.STATE_PROJECT_SPACE_COMPLETE.equalsIgnoreCase(var5)) {
                var6 = (String)var4.get(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ACTUAL_FINISH_DATE);
            } else {
                var6 = (new EffectivityFramework()).getInfinitySymbolDisplay(var1);
            }

            return ProgramCentralUtil.isNotNullString(var6) ? var6 : "";
        }
    }

    protected String evaluateDisplayEffectivityStartDateFromExpression(Context var1, Map var2) throws MatrixException {
        if (var2 == null) {
            throw new IllegalArgumentException("Effectivity Expression is null");
        } else {
            String var3 = "";
            String var4 = (String)var2.get("displayValue");
            var3 = this.evaluateDisplayEffectivityStartDateFromExpression(var1, var4);
            return var3;
        }
    }

    private String evaluateDisplayEffectivityStartDateFromExpression(Context var1, String var2) throws MatrixException {
        String var3 = "";
        if (ProgramCentralUtil.isNotNullString(var2)) {
            try {
                Map var4 = ProgramCentralUtil.parseDisplayDateEffectivityValue(var1, var2);
                var3 = (String)var4.get("DisplayEffectivityStartDate");
            } catch (Exception var5) {
                throw new MatrixException(var5);
            }
        }

        return var3;
    }

    protected String evaluateDisplayEffectivityEndDateFromExpression(Context var1, Map var2) throws MatrixException {
        if (var2 == null) {
            throw new IllegalArgumentException("Effectivity Expression is null");
        } else {
            String var3 = "";
            String var4 = (String)var2.get("displayValue");
            var3 = this.evaluateDisplayEffectivityEndDateFromExpression(var1, var4);
            return var3;
        }
    }

    private String evaluateDisplayEffectivityEndDateFromExpression(Context var1, String var2) throws MatrixException {
        String var3 = "";
        if (ProgramCentralUtil.isNotNullString(var2)) {
            try {
                Map var4 = ProgramCentralUtil.parseDisplayDateEffectivityValue(var1, var2);
                var3 = (String)var4.get("DisplayEffectivityEndDate");
            } catch (Exception var5) {
                throw new MatrixException(var5);
            }
        }

        return ProgramCentralUtil.isNullString(var3) ? "" : var3;
    }

    protected String evaluateActualEffectivityStartDateFromExpression(Context var1, Map var2) throws MatrixException {
        if (var2 == null) {
            throw new IllegalArgumentException("Effectivity Expression is null");
        } else {
            String var3 = "";
            String var4 = (String)var2.get("actualValue");
            var3 = this.evaluateActualEffectivityStartDateFromExpression(var1, var4);
            return var3;
        }
    }

    private String evaluateActualEffectivityStartDateFromExpression(Context var1, String var2) throws MatrixException {
        String var3 = "";
        if (ProgramCentralUtil.isNotNullString(var2)) {
            try {
                Map var4 = ProgramCentralUtil.parseActualDateEffectivityValue(var1, var2);
                var3 = (String)var4.get("EffectivityStartDate");
            } catch (Exception var5) {
                throw new MatrixException(var5);
            }
        }

        return var3;
    }

    protected String evaluateActualEffectivityEndDateFromExpression(Context var1, Map var2) throws MatrixException {
        if (var2 == null) {
            throw new IllegalArgumentException("Effectivity Expression is null");
        } else {
            String var3 = "";
            String var4 = (String)var2.get("actualValue");
            var3 = this.evaluateActualEffectivityEndDateFromExpression(var1, var4);
            return var3;
        }
    }

    private String evaluateActualEffectivityEndDateFromExpression(Context var1, String var2) throws MatrixException {
        String var3 = "";
        if (ProgramCentralUtil.isNotNullString(var2)) {
            try {
                Map var4 = ProgramCentralUtil.parseActualDateEffectivityValue(var1, var2);
                var3 = (String)var4.get("EffectivityEndDate");
            } catch (Exception var5) {
                throw new MatrixException(var5);
            }
        }

        return ProgramCentralUtil.isNullString(var3) ? "" : var3;
    }

    public MapList getDateSortedSnapshotList(Context var1) throws MatrixException {
        String var2 = this.getId(var1);
        if (ProgramCentralUtil.isNullString(var2)) {
            throw new IllegalArgumentException("No master project business object");
        } else {
            new EffectivityFramework();
            new HashMap();
            StringList var5 = this.getInfoList(var1, "from[Related Projects].to.id");
            StringList var6 = new StringList();
            var6.addElement("originated");
            var6.addElement("to[Related Projects].id");
            var6.addElement("id");
            var6.addElement("type");
            int var7 = var5.size();
            MapList var8 = new MapList();
            String[] var9 = new String[var7];
            if (var7 > 0) {
                var5.toArray(var9);
                var8 = getInfo(var1, var9, var6);
                var8.addSortKey("originated", "descending", "date");
                var8.sort();
            }

            return var8 == null ? new MapList() : var8;
        }
    }

    private Map getEffectivityExpressionOnMasterProject(Context var1) throws MatrixException {
        Map var2 = this.getLatestEffectiveSnapshotInfo(var1);
        Object var3 = (Map)var2.get("keySnapshotEffectivityExpressionMap");
        if (var3 == null) {
            var3 = new HashMap();
        }

        return (Map)var3;
    }

    private Map getLatestEffectiveSnapshotInfo(Context var1) throws MatrixException {
        String var2 = this.getId(var1);
        if (ProgramCentralUtil.isNullString(var2)) {
            throw new IllegalArgumentException("No master project business object");
        } else {
            MapList var3 = this.getDateSortedSnapshotList(var1);
            MapList var4 = this.getSnapshotEffectivityExpressionList(var1, var3);
            int var5 = var4.size();
            int var6 = -1;
            Object var7 = new HashMap();

            for(int var8 = 0; var8 < var5; ++var8) {
                Map var9 = (Map)var4.get(var8);
                String var10 = (String)var9.get("actualValue");
                if (ProgramCentralUtil.isNotNullString(var10)) {
                    var7 = var9;
                    var6 = var8;
                    break;
                }
            }

            Object var11 = new HashMap();
            if (var6 != -1) {
                var11 = (Map)var3.get(var6);
                ((Map)var11).put("keySnapshotEffectivityExpressionMap", var7);
            }

            return (Map)var11;
        }
    }

    private MapList getSnapshotEffectivityExpressionList(Context var1, MapList var2) throws MatrixException {
        if (var2 == null) {
            throw new IllegalArgumentException();
        } else {
            int var3 = var2.size();
            MapList var4 = new MapList(var3);

            for(int var5 = 0; var5 < var3; ++var5) {
                Map var6 = (Map)var2.get(var5);
                HashMap var7 = new HashMap();
                String var8 = (String)var6.get("to[Related Projects].id");
                var7.put("id[connection]", var8);
                var4.add(var7);
            }

            boolean var11 = true;
            boolean var12 = false;
            boolean var13 = false;
            new MapList();

            MapList var14;
            try {
                EffectivityFramework var9 = new EffectivityFramework();
                var14 = var9.getRelExpression(var1, var4, gscProjectSnapshot.getClientTimeZone(var1), var12);
            } catch (Exception var10) {
                throw new MatrixException(var10);
            }

            if (var14 == null) {
                var14 = new MapList();
            }

            return var14;
        }
    }

    public Map getAllSnapshotsEffectivityStartDate(Context var1) throws MatrixException {
        String var2 = this.getId(var1);
        if (ProgramCentralUtil.isNullString(var2)) {
            throw new IllegalArgumentException();
        } else {
            MapList var3 = this.getDateSortedSnapshotList(var1);
            MapList var4 = this.getSnapshotEffectivityExpressionList(var1, var3);
            int var5 = var3.size();
            int var6 = var4.size();
            HashMap var7 = new HashMap();

            for(int var8 = 0; var8 < var5; ++var8) {
                Map var9 = (Map)var3.get(var8);
                String var10 = (String)var9.get("id");
                String var11 = "";
                if (var8 < var6) {
                    Map var12 = (Map)var4.get(var8);
                    String var13 = (String)var12.get("displayValue");
                    Map var14 = ProgramCentralUtil.parseDisplayDateEffectivityValue(var1, var13);
                    var11 = (String)var14.get("DisplayEffectivityStartDate");
                    if (ProgramCentralUtil.isNullString(var11)) {
                        var11 = "";
                    }
                }

                var7.put(var10, var11);
            }

            return var7;
        }
    }

    public static MapList getOwnershipAccess(Context var0, String var1) throws MatrixException {
        return getOwnershipAccess(var0, var1, false);
    }

    public static MapList getOwnershipAccess(Context var0, String var1, boolean var2) throws MatrixException {
        MapList var3 = new MapList();

        try {
            MapList var4 = DomainAccess.getAccessSummaryList(var0, var1, true);
            if (var4 == null) {
                return new MapList();
            } else {
                StringList var5 = getUserProjects(var0);
                Iterator var6 = var4.iterator();

                while(var6.hasNext()) {
                    Map var7 = (Map)var6.next();
                    String var8 = (String)var7.get("id");
                    String var9 = (String)var7.get("comment");
                    if ("Primary".equalsIgnoreCase(var9)) {
                        if (var2) {
                            var7.put("isPrimaryOwnership", "true");
                            var3.add(var7);
                        }
                    } else {
                        StringList var10 = FrameworkUtil.splitString(var8, ":");
                        String var11 = (String)var10.remove(0);
                        String var12 = (String)var10.remove(0);
                        String var13 = (String)var10.remove(0);
                        String var14 = (String)var10.remove(0);
                        String var15 = "false";
                        if (var5.contains(var13)) {
                            String var16 = var13.substring(0, var13.lastIndexOf("_PRJ"));
                            var7.put("personName", var16);
                            var15 = "true";
                        }

                        var7.put("ownershipId", var11);
                        var7.put("ownershipOrg", var12);
                        var7.put("ownershipProject", var13);
                        var7.put("ownershipComment", var14);
                        var7.put("isPersonOwnership", var15);
                        var3.add(var7);
                    }
                }

                return var3;
            }
        } catch (Exception var17) {
            throw new MatrixException(var17);
        }
    }

    public static StringList getUserProjects(Context var0) throws MatrixException {
        String var1 = "list role $1";
        StringList var2 = null;

        try {
            String var3 = MqlUtil.mqlCommand(var0, var1, new String[]{"*_PRJ"});
            var2 = StringUtil.split(var3, "\n");
        } catch (FrameworkException var4) {
            throw new MatrixException(var4);
        }

        if (var2 == null) {
            var2 = new StringList();
        }

        return var2;
    }

    public gscProjectSpace createBlankProject(Context var1, Map<String, String> var2, Map<String, String> var3, Map var4) throws Exception {
        gscProjectSpace var5 = (gscProjectSpace)DomainObject.newInstance(var1, ProgramCentralConstants.TYPE_PROJECT_SPACE, "Program");

        try {
            ContextUtil.startTransaction(var1, true);
            String var6 = (String)var2.get("name");
            String var7 = (String)var2.get("type");
            String var8 = (String)var2.get("policy");
            String var9 = (String)var2.get("vault");
            String var10 = (String)var2.get("description");
            var5.create(var1, var7, var6, var8, var1.getVault().getName(), var3, var10, false);
            String var11 = var5.getObjectId();
            var4.put("objectId", var11);
            updateProjectRelatedInfo(var1, var4);
            ContextUtil.commitTransaction(var1);
            return var5;
        } catch (Exception var12) {
            ContextUtil.abortTransaction(var1);
            var12.printStackTrace();
            throw var12;
        }
    }

    public gscProjectSpace clone(Context var1, String var2, Map<String, String> var3, Map<String, String> var4, Map<String, String> var5, boolean var6, boolean var7, boolean var8) throws Exception {
        return this.clone(var1, var2, var3, var4, var5, var6, var7, var8, false);
    }

    public gscProjectSpace clone(Context var1, String var2, Map<String, String> var3, Map<String, String> var4, Map<String, String> var5, boolean var6, boolean var7, boolean var8, boolean var9) throws Exception {
        gscProjectSpace var10 = (gscProjectSpace)DomainObject.newInstance(var1, ProgramCentralConstants.TYPE_PROJECT_SPACE, "Program");

        try {
            ContextUtil.startTransaction(var1, true);
            String var11 = (String)var3.get("name");
            String var12 = (String)var3.get("type");
            String var13 = (String)var3.get("policy");
            String var14 = (String)var3.get("vault");
            String var15 = (String)var3.get("discription");
            var10.setId(var2);
            BusinessInterfaceList var16 = var10.getBusinessInterfaces(var1, true);

            for(int var17 = 0; var17 < var16.size(); ++var17) {
                BusinessInterface var18 = (BusinessInterface)var16.get(var17);
                if (var18.getName().equalsIgnoreCase("DPMForecast")) {
                    var16.remove(var18);
                    break;
                }
            }

            new gscProjectSpace();
            gscProjectSpace var23;
            if (var9) {
                var23 = (gscProjectSpace)var10.clone(var1, var12, var11, var13, var14, (Map)null, "", false, (Map)null, false, var7 ? ProjectCloneOption.CLONE_FOLDERS : ProjectCloneOption.CLONE_WBS, ProjectCloneOption.CLONE_WBS_CONSTRAINTS);
            } else {
                var23 = (gscProjectSpace)var10.clone(var1, var12, var11, var13, var14, (Map)null, "", false, (Map)null, false, var7 ? ProjectCloneOption.CLONE_FOLDERS : ProjectCloneOption.CLONE_WBS);
            }

            if (var16 != null) {
                BusinessInterfaceList var24 = var23.getBusinessInterfaces(var1, true);
                int var19 = 0;

                for(int var20 = var16.size(); var19 < var20; ++var19) {
                    BusinessInterface var21 = (BusinessInterface)var16.get(var19);
                    if (!var24.contains(var21)) {
                        var23.addBusinessInterface(var1, var21);
                    }
                }
            }

            var23.setAttributeValues(var1, var5);
            String var25 = var23.getObjectId();
            var4.put("objectId", var25);
            updateProjectRelatedInfo(var1, var4);
            com.matrixone.apps.common.Task var26 = new com.matrixone.apps.common.Task(var25);
            var26.rollupAndSave(var1);
            if (var6) {
                connectRelatedProjects(var1, var10, (DomainObject)var23);
            }

            if (var8) {
                com.gsc.apps.program.Financials.cloneStructure(var1, var10, var23);
            }

            ContextUtil.commitTransaction(var1);
            return var23;
        } catch (Exception var22) {
            ContextUtil.abortTransaction(var1);
            var22.printStackTrace();
            throw var22;
        }
    }

    public gscProjectSpace cloneTemplate(Context var1, String var2, Map<String, String> var3, Map<String, String> var4, Map<String, String> var5, Map var6, boolean var7, boolean var8, boolean var9) throws Exception {
        return this.cloneTemplate(var1, var2, var3, var4, var5, var6, var7, var8, var9, false);
    }

    public gscProjectSpace cloneTemplate(Context var1, String var2, Map<String, String> var3, Map<String, String> var4, Map<String, String> var5, Map var6, boolean var7, boolean var8, boolean var9, boolean var10) throws Exception {
        return this.cloneTemplate(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, ContentReplicateOptions.COPY);
    }

    public gscProjectSpace cloneTemplate(Context var1, String var2, Map<String, String> var3, Map<String, String> var4, Map<String, String> var5, Map var6, boolean var7, boolean var8, boolean var9, boolean var10, ContentReplicateOptions var11) throws Exception {
        gscProjectSpace var12 = (gscProjectSpace)DomainObject.newInstance(var1, ProgramCentralConstants.TYPE_PROJECT_SPACE, "Program");

        try {
            ContextUtil.startTransaction(var1, true);
            String var13 = (String)var3.get("name");
            String var14 = (String)var3.get("type");
            String var15 = (String)var3.get("policy");
            String var16 = (String)var3.get("vault");
            String var17 = (String)var3.get("discription");
            var12.setId(var2);
            new gscProjectSpace();
            gscProjectSpace var18;
            if (var10) {
                var18 = (gscProjectSpace)var12.clone(var1, var14, var13, var15, var16, var6, "", var7, var5, true, var11, var8 ? ProjectCloneOption.CLONE_FOLDERS : ProjectCloneOption.CLONE_WBS, ProjectCloneOption.CLONE_WBS_CONSTRAINTS);
            } else {
                var18 = (gscProjectSpace)var12.clone(var1, var14, var13, var15, var16, var6, "", var7, var5, true, var11, var8 ? ProjectCloneOption.CLONE_FOLDERS : ProjectCloneOption.CLONE_WBS);
            }

            var18.setAttributeValues(var1, var5);
            String var19 = var18.getObjectId();
            var4.put("sourceTemplateId", var2);
            var4.put("objectId", var19);
            updateProjectRelatedInfo(var1, var4);
            com.matrixone.apps.common.Task var20 = new com.matrixone.apps.common.Task(var19);
            var20.rollupAndSave(var1);
            if (var9) {
                com.gsc.apps.program.Financials.cloneStructure(var1, var12, var18);
            }

            ContextUtil.commitTransaction(var1);
            return var18;
        } catch (Exception var21) {
            ContextUtil.abortTransaction(var1);
            var21.printStackTrace();
            throw var21;
        }
    }

    public String getCurrentDate(Context var1, String[] var2) throws Exception {
        String var3 = "";
        Map var4 = (Map)JPO.unpackArgs(var2);
        Map var5 = (Map)var4.get("paramMap");
        Map var6 = (Map)var4.get("requestMap");
        String var7 = (String)var6.get("timeZone");
        double var8 = Double.parseDouble(var7);
        Locale var10 = var1.getLocale();
        Date var11 = new Date();
        SimpleDateFormat var12 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
        var3 = var12.format(var11);
        var3 = eMatrixDateFormat.getFormattedDisplayDate(var3, var8, var10);
        return var3;
    }

    private static void updateProjectRelatedInfo(Context var0, Map<String, String> var1) throws Exception {
        gscProjectSpace var2 = (gscProjectSpace)DomainObject.newInstance(var0, ProgramCentralConstants.TYPE_PROJECT_SPACE, "Program");
        String var3 = (String)var1.get("objectId");
        String var4 = (String)var1.get("sourceTemplateId");
        String var5 = (String)var1.get("programId");
        String var6 = (String)var1.get("businessUnitId");
        String var7 = (String)var1.get("businessGoalId");
        String var8 = (String)var1.get("resourceTemplateId");
        String var9 = (String)var1.get("AddAsChild");
        String var10 = (String)var1.get("deliverableId");
        var2.setId(var3);
        if (ProgramCentralUtil.isNotNullString(var5)) {
            if (var5.contains("|")) {
                var5 = var5.replace("|", ",");
            }

            var2.setProgram(var0, var5);
        }

        if (ProgramCentralUtil.isNotNullString(var6)) {
            var2.setBusinessUnit(var0, var6);
        }

        if (ProgramCentralUtil.isNotNullString(var7)) {
            var2.connectBusinessGoalToProject(var0, var3, var7);
        }

        if (ProgramCentralUtil.isNotNullString(var10)) {
            DomainObject var11 = DomainObject.newInstance(var0, var10);
            DomainRelationship.connect(var0, var2, RELATIONSHIP_CONTRIBUTES_TO, var11);
        }

        if (ProgramCentralUtil.isNotNullString(var8)) {
            copyResourcePlanTemplate(var0, var3, var8, var4);
        }

        if (ProgramCentralUtil.isNotNullString(var9) && "true".equalsIgnoreCase(var9)) {
            String var13 = (String)var1.get("RelatedProjectId");
            String[] var12 = new String[]{var13};
            connectRelatedProjects(var0, var2, (String[])var12);
        }

    }

    public void connectBusinessGoalToProject(Context var1, String var2, String var3) throws Exception {
        DomainObject var4 = DomainObject.newInstance(var1, var2);
        DomainObject var5 = DomainObject.newInstance(var1, var3);

        try {
            DomainRelationship.connect(var1, var5, BusinessGoal.RELATIONSHIP_BUSINESS_GOAL_PROJECT_SPACE, var4);
        } catch (Exception var7) {
            var7.printStackTrace();
            throw var7;
        }
    }

    private static void copyResourcePlanTemplate(Context var0, String var1, String var2, String var3) throws Exception {
        MapList var4 = ResourcePlanTemplate.getPhasesForResourceRequestView(var0, DomainObject.newInstance(var0, var1), new StringList());
        if (var4.size() != 0 && ProgramCentralUtil.isNotNullString(var2)) {
            ResourcePlanTemplate var5 = new ResourcePlanTemplate();
            var5.createResourcePlan(var0, var1, var2, var3);
        }

    }

    private static void connectRelatedProjects(Context var0, DomainObject var1, DomainObject var2) throws Exception {
        String var3 = PropertyUtil.getSchemaProperty(var0, "relationship_RelatedProjects");
        StringList var4 = new StringList();
        var4.add("id");
        MapList var5 = var1.getRelatedObjects(var0, var3, "*", var4, (StringList)null, false, true, (short)1, (String)null, (String)null, 0);
        if (var5 != null && var5.size() > 0) {
            String[] var6 = new String[var5.size()];

            for(int var7 = 0; var7 < var5.size(); ++var7) {
                Map var8 = (Map)var5.get(var7);
                String var9 = (String)var8.get("id");
                var6[var7] = var9;
            }

            DomainRelationship.connect(var0, var2, var3, true, var6, false);
        }

    }

    private static void connectRelatedProjects(Context var0, DomainObject var1, String[] var2) throws Exception {
        String var3 = PropertyUtil.getSchemaProperty(var0, "relationship_RelatedProjects");
        if (var2 != null && var2.length > 0) {
            DomainRelationship.connect(var0, var1, var3, false, var2, false);
        }

    }

    public Map getSelectedProjectInfo(Context var1, String var2, String var3) throws Exception {
        MapList var4 = this.getSelectedProjectInfo(var1, new String[]{var2}, var3);
        Map var5 = (Map)var4.get(0);
        return var5;
    }

    public MapList getSelectedProjectInfo(Context var1, String[] var2, String var3) throws Exception {
        double var4 = Double.parseDouble(var3);
        Locale var6 = var1.getLocale();
        String var7 = var1.getSession().getLanguage();
        String var8 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.CopyOf", var7);
        String var9 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.ProjectFromTemplate", var7);
        String var10 = "attribute[" + ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE + "].value";
        String var11 = "attribute[" + ATTRIBUTE_PROJECT_SCHEDULE_FROM + "].value";
        String var12 = "from[" + RELATIONSHIP_PROJECT_QUESTION + "].id";
        StringList var15 = new StringList(2);
        var15.add(SELECT_PROGRAM_NAME);
        var15.add(SELECT_PROGRAM_ID);
        StringList var16 = new StringList(15);
        var16.addElement("id");
        var16.addElement("name");
        var16.addElement("type");
        var16.addElement("current");
        var16.addElement("description");
        var16.addElement(SELECT_PROGRAM_NAME);
        var16.addElement(SELECT_PROGRAM_ID);
        var16.addElement(SELECT_BUSINESS_UNIT_ID);
        var16.addElement(SELECT_BUSINESS_UNIT_NAME);
        var16.addElement(SELECT_TASK_ESTIMATED_START_DATE);
        var16.addElement(SELECT_TASK_CONSTRAINT_DATE);
        var16.addElement("policy");
        var16.addElement(SELECT_PROJECT_VISIBILITY);
        var16.addElement("vault");
        var16.addElement(ProgramCentralConstants.SELECT_ATTRIBUTE_CURRENCY);
        var16.addElement(SELECT_BUSINESS_GOAL_ID);
        var16.addElement(SELECT_BUSINESS_GOAL_NAME);
        var16.addElement(ProgramCentralConstants.SELECT_KINDOF_PROJECT_TEMPLATE);
        var16.addElement(var10);
        var16.addElement(var11);
        var16.addElement("from[" + RELATIONSHIP_PROJECT_QUESTION + "].to.current");
        var16.addElement("attribute[ProjectAutonameSeries]");
        var16.addElement("attribute[EnforceProjectName]");
        MapList var17 = DomainObject.getInfo(var1, var2, var16, var15);
        MapList var18 = new MapList();
        Iterator var19 = var17.iterator();

        while(var19.hasNext()) {
            Map var20 = (Map)var19.next();
            String var21 = (String)var20.get("id");
            String var22 = (String)var20.get(SELECT_TASK_CONSTRAINT_DATE);
            var22 = eMatrixDateFormat.getFormattedDisplayDate(var22, var4, var6);
            String var23 = (String)var20.get(ProgramCentralConstants.SELECT_KINDOF_PROJECT_TEMPLATE);
            String var24 = (String)var20.get("current");
            String var25 = (String)var20.get("from[" + RELATIONSHIP_PROJECT_QUESTION + "].to.current");
            String var26 = "";
            if (ProgramCentralUtil.isNotNullString(var25)) {
                StringList var27 = FrameworkUtil.split(var25, "\u0007");
                var26 = var27.toString();
            }

            String var40 = "";
            String var28 = "";
            StringList var29 = (StringList)var20.get(SELECT_PROGRAM_NAME);
            StringList var30 = (StringList)var20.get(SELECT_PROGRAM_ID);
            if (var29 != null && var29.size() > 0) {
                for(int var31 = 0; var31 < var29.size(); ++var31) {
                    if (var31 == 0) {
                        var40 = (String)var29.get(var31);
                        var28 = (String)var30.get(var31);
                    } else {
                        var40 = var40 + "|" + (String)var29.get(var31);
                        var28 = var28 + "|" + (String)var30.get(var31);
                    }
                }
            }

            String var41 = (String)var20.get("type");
            String var32 = EnoviaResourceBundle.getAdminI18NString(var1, "Type", var41, var7);
            String var33 = (String)var20.get(var11);
            String var34 = (String)var20.get(var10);
            if (TYPE_PROJECT_TEMPLATE.equalsIgnoreCase(var41)) {
                if ("Project Start Date".equalsIgnoreCase(var33)) {
                    var34 = "As Soon As Possible";
                } else {
                    var34 = "As Late As Possible";
                }
            }

            HashMap var35 = new HashMap();
            String var36 = var8 + " " + (String)var20.get("name");
            String var37 = (String)var20.get("attribute[ProjectAutonameSeries]");
            if (ProgramCentralUtil.isNotNullString(var37) && !var37.isEmpty()) {
                String var38 = "print bus $1 select $2 dump";
                String var39 = MqlUtil.mqlCommand(var1, var38, new String[]{var37, "revision"});
                if (!var39.equals("")) {
                    var36 = DomainObject.getAutoGeneratedName(var1, "type_ProjectSpace", var39);
                }
            }

            var35.put("projectId", var21);
            var35.put("Name", var36);
            var35.put("TypeActual", (String)var20.get("type"));
            var35.put("TypeActualDisplay", var32);
            var35.put("DefaultConstraintType", var34);
            var35.put("ScheduleFrom", var33);
            var35.put("KindOfProjectTemplate", var23);
            var35.put("state", var24);
            var35.put("QuestionsInfo", var26);
            if ("true".equalsIgnoreCase(var23)) {
                var35.put("description", var9 + ":" + (String)var20.get("name"));
            } else {
                var35.put("description", (String)var20.get("description"));
                var35.put("ProjectVisibility", (String)var20.get(SELECT_PROJECT_VISIBILITY));
                var35.put("BusinessGoalDisplay", (String)var20.get(SELECT_BUSINESS_GOAL_NAME) != null ? (String)var20.get(SELECT_BUSINESS_GOAL_NAME) : "");
                var35.put("BusinessGoal", (String)var20.get(SELECT_BUSINESS_GOAL_NAME) != null ? (String)var20.get(SELECT_BUSINESS_GOAL_NAME) : "");
                var35.put("BusinessGoalOID", (String)var20.get(SELECT_BUSINESS_GOAL_ID) != null ? (String)var20.get(SELECT_BUSINESS_GOAL_ID) : "");
            }

            var35.put("Program", var40);
            var35.put("ProgramDisplay", var40);
            var35.put("ProgramOID", var28);
            var35.put("ProjectDate", var22);
            var35.put("BusinessUnitOID", (String)var20.get(SELECT_BUSINESS_UNIT_ID) != null ? (String)var20.get(SELECT_BUSINESS_UNIT_ID) : "");
            var35.put("BusinessUnit", (String)var20.get(SELECT_BUSINESS_UNIT_NAME) != null ? (String)var20.get(SELECT_BUSINESS_UNIT_NAME) : "");
            var35.put("BusinessUnitDisplay", (String)var20.get(SELECT_BUSINESS_UNIT_NAME) != null ? (String)var20.get(SELECT_BUSINESS_UNIT_NAME) : "");
            var35.put("Policy", (String)var20.get("policy"));
            var35.put("BaseCurrency", (String)var20.get(ProgramCentralConstants.SELECT_ATTRIBUTE_CURRENCY));
            var35.put("autoNameSeries", (String)var20.get("attribute[ProjectAutonameSeries]"));
            var35.put("nameFieldReadOnly", (String)var20.get("attribute[EnforceProjectName]"));
            var18.add(var35);
        }

        return var18;
    }

    public String getFilePreviewViewContent(Context var1, MapList var2) throws Exception {
        StringBuilder var3 = new StringBuilder();
        int var4 = 0;
        if (var2 != null) {
            var4 = var2.size();
        }

        if (var4 > 0) {
            for(int var5 = 0; var5 < var4; ++var5) {
                Map var6 = (Map)var2.get(var5);
                StringBuilder var7 = new StringBuilder();
                Set var8 = var6.keySet();
                var8.remove("Stamp");
                var8.remove("Children");
                var8.remove("predecessorsWBS");
                var8.forEach((var2x) -> {
                    String var33 = (String)var6.get(var2x);
                    var7.append(var2x);
                    var7.append("=");
                    var7.append(var33);
                    var7.append(";");
                });
                var7.deleteCharAt(var7.lastIndexOf(";"));
                var3.append(var7.toString());
                if (var5 < var4 - 1) {
                    var3.append("\n");
                }
            }
        }

        return var3.toString();
    }

    public void completeImportProcess(Context var1, String var2, MapList var3) throws Exception {
        gscProjectSpace var4 = (gscProjectSpace)DomainObject.newInstance(var1, TYPE_PROJECT_SPACE, "Program");
        var4.setId(var2);
        var4.completeImportProcess(var1, var3);
    }

    public void completeImportProcess(Context var1, MapList var2) throws Exception {
        long var3 = System.currentTimeMillis();
        long var5 = 0L;

        try {
            StringList var7 = this.getAttributeNameList(var1);
            StringList var8 = this.getBasicAttributeColName(var1);
            StringList var9 = ProgramCentralUtil.getSubTypesList(var1, DomainObject.TYPE_PROJECT_SPACE);
            MapList var10 = new MapList();
            var2.forEach((var2x) -> {
                String var33 = (String)((Map)var2x).get("type");
                if (!var9.contains(var33)) {
                    var10.add(var2x);
                }

            });
            MapList var11 = new MapList();
            HashMap var12 = new HashMap();
            HashMap var13 = new HashMap();
            HashMap var14 = new HashMap();
            HashMap var15 = new HashMap();
            DomainObject var16 = this.getAccessListObject(var1, this);
            var14.put(this, var16);
            String var17 = ProgramCentralUtil.getPhysicalId(var1, var16.getObjectId(var1));
            String var18 = var1.getUser();
            String var19 = PersonUtil.getDefaultProject(var1, var18);
            TimeZone var20 = TimeZone.getTimeZone(var1.getSession().getTimezone());
            double var21 = -1.0 * (double)var20.getRawOffset();
            double var23 = new Double(var21 / 3600000.0);
            int var25 = eMatrixDateFormat.getEMatrixDisplayDateFormat();
            DateFormat var26 = DateFormat.getDateTimeInstance(var25, var25, Locale.US);
            SimpleDateFormat var27 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            Locale var28 = var1.getLocale();
            new HashMap();
            int var30 = 1;
            PropertyUtil.setGlobalRPEValue(var1, "IGNORE_CREATE_TRIGGER", "true");
            if (var10 != null) {
                Calendar var31 = Calendar.getInstance();
                int var32 = var10.size();
                StringList var33 = new StringList();
                HashMap var34 = new HashMap();
                HashMap var35 = new HashMap();
                String var36 = "";
                String var37 = "";
                String var38 = ProgramCentralUtil.getPhysicalId(var1, this.getObjectId());
                ProjectSequence var39 = new ProjectSequence(var1, var17);
                var39.startUpdateSession(var1);

                String var44;
                for(int var40 = 0; var40 < var32; ++var40) {
                    var33.clear();
                    var34.clear();
                    var35.clear();
                    var36 = "";
                    var37 = "";
                    Map var41 = (Map)var10.get(var40);
                    String var42 = (String)var41.get("name");
                    String var43 = (String)var41.get("type");
                    var44 = (String)var41.get("Level");
                    String var45 = (String)var41.get("Assignees");
                    String var46 = (String)var41.get("Dependencies");
                    String var47 = (String)var41.get(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_START_DATE);
                    String var48 = (String)var41.get(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
                    String var49 = (String)var41.get(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_DURATION);
                    var49 = var49.toLowerCase();
                    String var50 = (String)var41.get(DomainConstants.ATTRIBUTE_TASK_CONSTRAINT_DATE);
                    String var51 = (String)var41.get(DomainConstants.ATTRIBUTE_TASK_CONSTRAINT_TYPE);
                    if (var49.contains("days")) {
                        var49 = var49.replace("days", "d");
                    } else if (var49.contains("hours")) {
                        var49 = var49.replace("hours", "h");
                    } else if (var49.contains("day")) {
                        var49 = var49.replace("day", "d");
                    } else if (var49.contains("hour")) {
                        var49 = var49.replace("hour", "h");
                    }

                    var41.put("Task Estimated Duration", var49);
                    var7.forEach((var3x) -> {
                        String var4 = (String)var41.get(var3x) != null ? (String)var41.get(var3x) : "";
                        AttributeType var55 = new AttributeType(var3x);

                        try {
                            var55.open(var1);
                            boolean var6 = var55.isMultiVal();
                            if (var6 && ProgramCentralUtil.isNotNullString(var4)) {
                                StringList var77 = FrameworkUtil.split(var4, ",");
                                var34.put(var3x, var77);
                            } else {
                                var34.put(var3x, var4);
                            }

                            var55.close(var1);
                        } catch (MatrixException var88) {
                            var88.printStackTrace();
                        }

                    });
                    Date var52;
                    if (var47 != null) {
                        var47 = eMatrixDateFormat.getFormattedDisplayDateTime(var1, var47, true, var25, var23, Locale.US);
                        var52 = var26.parse(var47);
                        var31.setTime(var52);
                        var31.set(11, 8);
                        var31.set(12, 0);
                        var31.set(13, 0);
                        var52 = var31.getTime();
                        var34.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_START_DATE, var27.format(var52));
                    }

                    if (var48 != null) {
                        var48 = eMatrixDateFormat.getFormattedDisplayDateTime(var1, var48, true, var25, var23, Locale.US);
                        var52 = var26.parse(var48);
                        var31.clear();
                        var31.setTime(var52);
                        var31.set(11, 17);
                        var31.set(12, 0);
                        var31.set(13, 0);
                        var52 = var31.getTime();
                        var34.put(DomainConstants.ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE, var27.format(var52));
                    }

                    if (var51 != null && !var51.isEmpty()) {
                        var34.put(DomainConstants.ATTRIBUTE_TASK_CONSTRAINT_TYPE, var51);
                    }

                    if (var50 != null && !var50.isEmpty()) {
                        var50 = eMatrixDateFormat.getFormattedDisplayDateTime(var1, var50, true, var25, var23, Locale.US);
                        var52 = var26.parse(var50);
                        var31.clear();
                        var31.setTime(var52);
                        if (!"Start No Later Than".equalsIgnoreCase(var51) && !"Start No Earlier Than".equalsIgnoreCase(var51) && !"Must Start On".equalsIgnoreCase(var51)) {
                            if ("Must Finish On".equalsIgnoreCase(var51) || "Finish No Later Than".equalsIgnoreCase(var51) || "Finish No Earlier Than".equalsIgnoreCase(var51)) {
                                var31.set(11, 17);
                            }
                        } else {
                            var31.set(11, 8);
                        }

                        var31.set(12, 0);
                        var31.set(13, 0);
                        var34.put(DomainConstants.ATTRIBUTE_TASK_CONSTRAINT_DATE, var27.format(var31.getTime()));
                    }

                    var8.forEach((var2x) -> {
                        String var53 = (String)var41.get(var2x) != null ? (String)var41.get(var2x) : "";
                        var35.put(var2x, var53);
                    });
                    var37 = ProgramCentralUtil.getDefaultPolicy(var1, var43);
                    if (ProgramCentralUtil.isNullString(var37)) {
                        var37 = ProgramCentralConstants.POLICY_PROJECT_TASK;
                        if (ProgramCentralConstants.TYPE_GATE.equals(var43) || ProgramCentralConstants.TYPE_MILESTONE.equals(var43)) {
                            var37 = ProgramCentralConstants.POLICY_PROJECT_REVIEW;
                        }
                    }

                    String var69 = (String)var35.get("description");
                    Task var53 = new Task();
                    int var54 = var44.lastIndexOf(".");
                    Object var55 = null;
                    if (var54 == -1) {
                        var16 = (DomainObject)var14.get(this);
                        var55 = this;
                        var53._CREATE_TASK_PARAM_PARENT_TASK_PHYSICALID = var38;
                    } else {
                        String var56 = var44.substring(0, var54);
                        var55 = (TaskHolder)var12.get(var56);
                        if (var13.containsKey(var56)) {
                            var53._CREATE_TASK_PARAM_PARENT_TASK_PHYSICALID = (String)var13.get(var56);
                        } else {
                            String var57 = ProgramCentralUtil.getPhysicalId(var1, ((Task)var12.get(var56)).getObjectId());
                            var13.put(var56, var57);
                        }

                        var16 = (DomainObject)var14.get(var55);
                    }

                    var53._CREATE_TASK_PARAM_LOGGED_IN_USER = var18;
                    var53.create(var1, var43, var42, var37, var69, (TaskHolder)var55, var16, (String)null, var34, (Map)null, var19, var39);
                    var14.put(var53, var16);
                    var36 = var53.getObjectId(var1);
                    var12.put(var44, var53);
                    var15.put(Integer.toString(var30), var36);
                    if (ProgramCentralUtil.isNotNullString(var46)) {
                        MapList var70 = this.createDepedencyMap(var1, var46, var36);
                        var70.forEach((var1x) -> {
                            var11.add(var1x);
                        });
                    }

                    ++var30;
                    if (ProgramCentralUtil.isNotNullString(var45)) {
                        StringList var71 = FrameworkUtil.split(var45, "|");

                        String var58;
                        int var72;
                        for(var72 = 0; var72 < var71.size(); ++var72) {
                            var58 = (String)var71.get(var72);
                            StringList var59 = FrameworkUtil.split(var58, ":");
                            String var60 = (String)var59.get(1);
                            if (!var33.contains(var60)) {
                                var33.addElement(var60);
                            }
                        }

                        if (var33 != null && !var33.isEmpty()) {
                            for(var72 = 0; var72 < var33.size(); ++var72) {
                                var58 = (String)var33.get(var72);
                                var53.addAssignee(var1, var58, (String)null);
                            }
                        }
                    }
                }

                var39.finishUpdateSession(var1);
                DebugUtil.debug("Total time taken creation:::" + var5);
                this.createTaskDependency(var1, var11, var15);
                String var66 = this.getObjectId(var1);
                Task var67 = new Task();
                var67.setId(var66);
                long var68 = System.currentTimeMillis();
                var44 = this.getSchedule(var1);
                if (var44 == null || var44.isEmpty()) {
                    var44 = var67.getProjectSchedule(var1);
                }

                if (ProgramCentralUtil.isNullString(var44) || "Auto".equalsIgnoreCase(var44)) {
                    var67.rollupAndSave(var1);
                    DebugUtil.debug("Total time taken by rollupAndSave():::" + (System.currentTimeMillis() - var68));
                }
            }
        } catch (Exception var64) {
            var64.printStackTrace();
        } finally {
            PropertyUtil.setGlobalRPEValue(var1, "IGNORE_CREATE_TRIGGER", "false");
        }

        DebugUtil.debug("Total time taken by completeImportProcess():::" + (System.currentTimeMillis() - var3));
    }

    private StringList getBasicAttributeColName(Context var1) throws Exception {
        StringList var2 = new StringList();
        String var3 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.ColumnName");
        StringList var4 = FrameworkUtil.split(var3, ",");
        HashMap var5 = new HashMap();

        int var6;
        String var7;
        Map var8;
        for(var6 = 0; var6 < var4.size(); ++var6) {
            var7 = (String)var4.get(var6);
            var8 = this.getImportedFileHeaderSettings(var1, var7);
            var5.put(var7, var8);
        }

        for(var6 = 0; var6 < var4.size(); ++var6) {
            var7 = (String)var4.get(var6);
            var8 = (Map)var5.get(var7);
            String var9 = (String)var8.get("BasicAttribute");
            String var10 = (String)var8.get("AttributeName");
            if (var9.equalsIgnoreCase("YES") && !var2.contains(var10)) {
                var2.addElement(var10);
            }
        }

        return var2;
    }

    private StringList getAttributeNameList(Context var1) throws Exception {
        String var2 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.ColumnName");
        StringList var3 = FrameworkUtil.split(var2, ",");
        HashMap var4 = new HashMap();

        for(int var5 = 0; var5 < var3.size(); ++var5) {
            String var6 = (String)var3.get(var5);
            Map var7 = this.getImportedFileHeaderSettings(var1, var6);
            var4.put(var6, var7);
        }

        StringList var12 = this.getDefaultColName(var1);
        if (!var12.contains("Name") && !var12.contains("Type")) {
            var12.addElement("Name");
            var12.addElement("Type");
        }

        StringList var13 = new StringList();

        for(int var14 = 0; var14 < var3.size(); ++var14) {
            String var8 = (String)var3.get(var14);
            if (!var12.contains(var8)) {
                Map var9 = (Map)var4.get(var8);
                String var10 = (String)var9.get("AttributeName");
                String var11 = (String)var9.get("BasicAttribute");
                if (!"YES".equalsIgnoreCase(var11)) {
                    var13.addElement(var10);
                }
            }
        }

        return var13;
    }

    private StringList getDefaultColName(Context var1) throws Exception {
        StringList var2 = new StringList();
        String var3 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.ColumnName");
        StringList var4 = FrameworkUtil.split(var3, ",");
        HashMap var5 = new HashMap();

        int var6;
        String var7;
        Map var8;
        for(var6 = 0; var6 < var4.size(); ++var6) {
            var7 = (String)var4.get(var6);
            var8 = this.getImportedFileHeaderSettings(var1, var7);
            var5.put(var7, var8);
        }

        for(var6 = 0; var6 < var4.size(); ++var6) {
            var7 = (String)var4.get(var6);
            var8 = (Map)var5.get(var7);
            String var9 = (String)var8.get("AttributeName");
            String var10 = (String)var8.get("BasicAttribute");
            if (var9.equalsIgnoreCase("NA") && var10.equalsIgnoreCase("NO") && !var2.contains(var7)) {
                var2.addElement(var7);
            }
        }

        return var2;
    }

    private MapList createDepedencyMap(Context var1, String var2, String var3) throws Exception {
        StringList var4 = FrameworkUtil.split(var2, ",");
        MapList var5 = new MapList();
        var4.forEach((var2x) -> {
            StringList var3x = FrameworkUtil.split(var2x, ":");
            String var44 = (String)var3x.get(0);
            String var5x = "";
            if (var3x.size() > 1) {
                var5x = (String)var3x.get(1);
            } else {
                var5x = "FS+0.0 d";
            }

            String var6 = var5x.indexOf("+") != -1 ? "+" : "-";
            StringList var7 = FrameworkUtil.split(var5x, var6);
            String var8 = (String)var7.get(0);
            String var9 = "";
            if (var7.size() > 1) {
                var9 = (String)var7.get(1);
            } else {
                var9 = "0.0 d";
            }

            HashMap var10 = new HashMap();
            var10.put("Task", var3);
            var10.put("TargetTaskId", var44);
            var10.put("dependencyType", var8);
            var10.put("lag", var6 + var9);
            var5.add(var10);
        });
        return var5;
    }

    private void createTaskDependency(Context var1, MapList var2, Map var3) throws Exception {
        long var4 = System.currentTimeMillis();
        var2.forEach((var2x) -> {
            Map var3x = (Map)var2x;
            String var54 = (String)var3x.get("Task");
            String var5 = (String)var3x.get("TargetTaskId");
            String var6 = (String)var3x.get("dependencyType");
            String var7 = (String)var3x.get("lag");
            String var8 = (String)var3.get(var5);
            if (ProgramCentralUtil.isNotNullString(var8) && ProgramCentralUtil.isNotNullString(var6)) {
                String var9 = "connect businessobject $1 relationship $2 to $3 $4 $5 $6 $7";

                try {
                    (new MQLCommand()).executeCommand(var1, false, false, var9, new String[]{var54, RELATIONSHIP_DEPENDENCY, var8, ATTRIBUTE_DEPENDENCY_TYPE, var6, DependencyRelationship.ATTRIBUTE_LAG_TIME, var7});
                } catch (MatrixException var11) {
                    var11.printStackTrace();
                }
            }

        });
        DebugUtil.debug("Total time taken by createTaskDependency():____" + (System.currentTimeMillis() - var4));
    }

    public boolean isCorrectValue(Context var1, String var2) throws Exception {
        boolean var3 = true;
        if (ProgramCentralUtil.isNotNullString(var2) && var2.startsWith("Error:")) {
            var3 = false;
        }

        return var3;
    }

    public boolean hasCorrectValue(Context var1, Map var2) throws Exception {
        return this.isFileCorrect;
    }

    private String getValidHeaderValue(Context var1, String var2) throws MatrixException {
        if (ProgramCentralUtil.isNotNullString(var2)) {
            var2 = var2.replaceAll("\\s+", "");
        }

        return var2;
    }

    public Map getImportedFileHeaderSettings(Context var1, String var2) throws MatrixException {
        return this.getImportedFileHeaderSettings(var1, var2, var1.getSession().getLanguage());
    }

    public Map getImportedFileHeaderSettings(Context var1, String var2, String var3) throws MatrixException {
        HashMap var4 = new HashMap();
        if (ProgramCentralUtil.isNotNullString(var2)) {
            var2 = this.getValidHeaderValue(var1, var2);
            String var5 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".Header");
            String var6 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", var5, Locale.US);
            String var7 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", var5, var3);
            String var8 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".AttributeName");
            String var9 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".ColumnType");
            String var10 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".validationMethod");
            String var11 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".validationProgram");
            String var12 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".function");
            String var13 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".program");
            String var14 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".GroupHeader");
            var14 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", var14, var3);
            String var15 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport." + var2 + ".BasicAttribute");
            var4.put("displayHeader", var7);
            var4.put("Header", var6);
            var4.put("AttributeName", var8);
            var4.put("ColumnType", var9);
            var4.put("validationMethod", var10);
            var4.put("validationProgram", var11);
            var4.put("program", var13);
            var4.put("function", var12);
            var4.put("GroupHeader", var14);
            var4.put("BasicAttribute", var15);
        }

        return var4;
    }

    private StringList getFileContent(Context var1, InputStream var2) throws Exception {
        StringList var3 = new StringList();
        BufferedReader var4 = new BufferedReader(new InputStreamReader(var2));
        String var5 = "";
        boolean var6 = true;

        while((var5 = var4.readLine()) != null) {
            if (!var5.isEmpty()) {
                if (var6) {
                    var5 = var5.replaceFirst("^\ufeff", "");
                    var6 = false;
                }

                var3.addElement(var5);
            }
        }

        return var3;
    }

    private Map getFileMap(Context var1, StringList var2, String var3) throws Exception {
        HashMap var4 = new HashMap();
        String var5 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.ColumnName");
        StringList var6 = FrameworkUtil.split(var5, ",");
        String var7 = (String)var2.get(0);
        if (!var7.contains((String)var6.get(0)) && !var7.contains(TYPE_PROJECT_SPACE) && !var7.contains(TYPE_PHASE) && !var7.contains(TYPE_TASK) && !var7.contains(TYPE_GATE) && !var7.contains(TYPE_MILESTONE)) {
            var2.remove(0);
        }

        String var8 = "";
        String var9 = "";
        String var10 = "";
        if (var2 != null && var2.size() >= 2) {
            for(int var11 = 0; var11 < var2.size(); ++var11) {
                String var12 = (String)var2.get(var11);
                StringList var13 = FrameworkUtil.split(var12, var3);
                if (var11 == 0) {
                    String var14 = (String)var13.get(0);
                    if (var13.contains(DomainObject.TYPE_PROJECT_SPACE)) {
                        var8 = "Project";
                    } else if (var6.contains(var14)) {
                        if (var6.containsAll(var13)) {
                            var8 = "header";
                        }
                    } else if ("WBS".equalsIgnoreCase(var14)) {
                        var13.remove(0);
                        var13.add(0, (String)var6.get(0));
                        if (var6.containsAll(var13)) {
                            var8 = "header";
                        }
                    } else {
                        var8 = "TaskManagement";
                    }
                } else {
                    if (var11 != 1) {
                        var10 = "TaskManagement";
                        break;
                    }

                    if (var13.contains(DomainObject.TYPE_PROJECT_SPACE)) {
                        var9 = "Project";
                    } else {
                        var9 = "TaskManagement";
                    }
                }
            }
        }

        var4.put("FirstRow", var8);
        var4.put("SecondRow", var9);
        var4.put("ThirdRow", var10);
        return var4;
    }

    private Map validateFileContent(Context var1, StringList var2, String var3) throws Exception {
        boolean var4 = false;
        boolean var5 = false;
        HashMap var6 = new HashMap();
        Map var7 = this.getFileMap(var1, var2, var3);
        String var8 = (String)var7.get("FirstRow");
        String var9 = (String)var7.get("SecondRow");
        String var10 = (String)var7.get("ThirdRow");
        if (var8.equalsIgnoreCase("header")) {
            var4 = true;
        }

        if ("Project".equalsIgnoreCase(var8) || "Project".equalsIgnoreCase(var9)) {
            var5 = true;
        }

        if (var4) {
            String var11 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.ColumnName");
            StringList var12 = FrameworkUtil.split(var11, ",");
            StringList var13 = FrameworkUtil.split((String)var2.get(0), var3);
            String var14 = (String)var13.get(0);
            if (var12.contains(var14)) {
                if (var12.containsAll(var13)) {
                    var6.put("headerConfig", "ok");
                }
            } else if ("WBS".equalsIgnoreCase(var14)) {
                var13.remove(0);
                var13.add(0, (String)var12.get(0));
                if (var12.containsAll(var13)) {
                    var6.put("headerConfig", "ok");
                }
            }
        }

        var6.put("header", var4);
        var6.put("FirstRow", var8);
        var6.put("secondRow", var9);
        var6.put("isProjectPresent", var5);
        return var6;
    }

    public MapList getImportedFileDetails(Context var1, InputStream var2, String var3, Map var4) throws Exception {
        MapList var5 = new MapList();
        StringList var6 = new StringList();
        Object var7 = new HashMap();
        StringList var8 = new StringList();
        StringList var9 = new StringList();
        HashMap var10 = new HashMap();
        var8.add("Level");
        var8.add("Name");
        var8.add("Type");
        var8.add("Estimated Duration");
        var8.add("Estimated Start Date");
        var8.add("Estimated Finish Date");
        var8.add("Dependencies");
        var8.add("Assignees");
        var8.add("Constraint Type");
        var8.add("Constraint Date");
        String var11 = (String)var4.get("timeZone");
        String var12 = (String)var4.get("importSubProject");
        boolean var13 = !"false".equalsIgnoreCase(var12);
        if (var2 != null) {
            var6 = this.getFileContent(var1, var2);
            var7 = this.validateFileContent(var1, var6, var3);
        }

        String var14 = (String)((Map)var7).get("FirstRow");
        String var15 = (String)((Map)var7).get("secondRow");
        boolean var16 = (Boolean)((Map)var7).get("header");
        boolean var17 = false;
        boolean var18 = (Boolean)((Map)var7).get("isProjectPresent");
        String var19;
        if (var16) {
            var19 = (String)((Map)var7).get("headerConfig");
            var17 = ProgramCentralUtil.isNotNullString(var19) && var19.equalsIgnoreCase("ok");
        }

        StringList var20;
        int var22;
        String var24;
        String var28;
        String var33;
        HashMap var51;
        String var59;
        HashMap var65;
        int var70;
        if (!var17 && var16) {
            var51 = new HashMap();
            String var52 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.ImportedFileError", var1.getSession().getLanguage());
            var51.put("error", var52);
            var5.add(var51);
        } else {
            var19 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.ColumnName");
            var20 = FrameworkUtil.split(var19, ",");
            HashMap var21 = new HashMap();
            var22 = var20.size();

            int var23;
            for(var23 = 0; var23 < var22; ++var23) {
                var24 = (String)var20.get(var23);
                Map var25 = this.getImportedFileHeaderSettings(var1, var24);
                var21.put(var24, var25);
            }

            var23 = 0;

            for(int var55 = var20.size(); var23 < var55; ++var23) {
                if (!var8.contains(var20.get(var23))) {
                    var59 = (String)var20.get(var23);
                    Map var26 = (Map)var21.get(var59);
                    boolean var27 = "Yes".equalsIgnoreCase((String)var26.get("BasicAttribute"));
                    if (!var27) {
                        var28 = (String)var26.get("AttributeName");
                        AttributeType var29 = new AttributeType(var28);
                        var29.open(var1);
                        if (var29.isMultiVal()) {
                            var9.add(var20.get(var23));
                            var10.put(var20.get(var23), var29.getChoices(var1));
                        }

                        var29.close(var1);
                    }
                }
            }

            byte var56 = 0;
            if (var16) {
                var56 = 1;
            }

            java.util.regex.Pattern var57 = java.util.regex.Pattern.compile(var3);
            if (var3.equalsIgnoreCase(",")) {
                var57 = java.util.regex.Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");
            }

            long var60 = System.currentTimeMillis();
            if (var6 != null && !var6.isEmpty()) {
                var65 = new HashMap();
                HashMap var67 = new HashMap();
                var70 = var56;
                if (!var18 && !var16) {
                    var70 = var56 + 1;
                } else if (var18 && var16) {
                    var70 = var56 - 1;
                }

                AttributeType var30 = new AttributeType(ProgramCentralConstants.ATTRIBUTE_TASK_CONSTRAINT_TYPE);
                var30.open(var1);
                this.slDefaultContraintType = var30.getChoices(var1);
                var30.close(var1);
                int var31 = var56;

                for(int var32 = var6.size(); var31 < var32; ++var70) {
                    var33 = (String)var6.get(var31);
                    String[] var34 = var57.split(var33);
                    String var35 = "";
                    String var36 = String.valueOf(var70);
                    HashMap var37 = new HashMap();

                    for(int var38 = 0; var38 < var22; ++var38) {
                        String var39 = (String)var20.get(var38);
                        String var40 = "";
                        if (var38 < var34.length) {
                            var40 = var34[var38];
                        }

                        Map var41 = (Map)var21.get(var39);
                        String var42 = (String)var41.get("AttributeName");
                        String var43 = (String)var41.get("ColumnType");
                        if (!var40.isEmpty()) {
                            String var44 = "\"";
                            var40 = FrameworkUtil.findAndReplace(var40, var44, "");
                        }

                        if ("Level".equalsIgnoreCase(var39)) {
                            var35 = var40;
                        }

                        if (var40 == null) {
                            if (ProgramCentralUtil.isNullString(var42) || var42.equalsIgnoreCase("NA")) {
                                var37.put(var39, var40);
                            }
                        } else {
                            if (var9.contains(var39) && !var40.isEmpty()) {
                                StringList var75 = (StringList)var10.get(var39);
                                String[] var45 = var40.split(",");
                                int var46 = 0;

                                for(int var47 = var45.length; var46 < var47; ++var46) {
                                    String var48 = var45[var46];
                                    if (!var75.contains(var48)) {
                                        new HashMap();
                                        String var50 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.ImportedFileError", var1.getSession().getLanguage());
                                        this.isFileCorrect = false;
                                        var40 = "Error:" + var50;
                                    }
                                }
                            } else {
                                var40 = this.getValidFileContentValue(var1, var39, var40, var41, var11, var34, var65, var36, var67, var13);
                            }

                            if (ProgramCentralUtil.isNotNullString(var42) && !var42.equalsIgnoreCase("NA")) {
                                var37.put(var42, var40);
                            } else {
                                var37.put(var39, var40);
                            }
                        }
                    }

                    if (var37 != null) {
                        var5.add(var37);
                        var65.put(var35, var37);
                    }

                    ++var31;
                }

                var67.clear();
            }
        }

        var51 = new HashMap();
        var51.put("0", new MapList());
        var20 = (StringList)CacheUtil.getCacheObject(var1, "DPM_ImportProject_ProjectList");
        if (var20 == null || var20.isEmpty()) {
            var20 = ProgramCentralUtil.getSubTypesList(var1, DomainObject.TYPE_PROJECT_SPACE);
            var20.remove(ProgramCentralConstants.TYPE_EXPERIMENT);
            var20.remove(ProgramCentralConstants.TYPE_PROJECT_BASELINE);
            var20.remove(ProgramCentralConstants.TYPE_PROJECT_SNAPSHOT);
        }

        StringList var53 = new StringList();

        Map var58;
        for(var22 = 0; var22 < var5.size(); ++var22) {
            var58 = (Map)var5.get(var22);
            var24 = (String)var58.get("type");
            var59 = (String)var58.get("Level");
            if (var20 != null && var20.contains(var24) && !"0".equals(var59)) {
                var53.add(var59);
            }
        }

        for(var22 = 0; var22 < var5.size(); ++var22) {
            var58 = (Map)var5.get(var22);
            var24 = (String)var58.get("type");
            var59 = (String)var58.get("Level");
            boolean var63 = false;

            for(int var66 = var53.size() - 1; var66 >= 0; --var66) {
                var28 = (String)var53.get(var66);
                if (var59.startsWith(var28) && !var59.equals(var28)) {
                    var58.put("ParentProjectLevel", var28);
                    MapList var72 = new MapList();
                    var63 = true;
                    if (var51.containsKey(var28)) {
                        var72 = (MapList)var51.get(var28);
                        var72.add(var58);
                    } else {
                        var72.add(var58);
                        var51.put(var28, var72);
                    }
                    break;
                }
            }

            if (!var63) {
                MapList var68 = (MapList)var51.get("0");
                var58.put("ParentProjectLevel", "0");
                var68.add(var58);
            }
        }

        Set var54 = var51.keySet();
        Iterator var61 = var54.iterator();
        HashMap var62 = new HashMap();

        while(var61.hasNext()) {
            var59 = (String)var61.next();
            MapList var64 = (MapList)var51.get(var59);
            var65 = new HashMap();
            int var69 = 1;

            for(var70 = 0; var70 < var64.size(); ++var70) {
                Map var71 = (Map)var64.get(var70);
                String var73 = (String)var71.get("type");
                String var74 = (String)var71.get("Level");
                if (var20 != null && var20.contains(var73) && var70 == 0) {
                    var69 = 0;
                }

                var33 = Integer.toString(var69);
                var71.put("WBSId", var33);
                var65.put(var33, var74);
                ++var69;
            }

            var62.put(var59, var65);
        }

        var4.put("projectWBSIdToLevelMap", var62);
        return var5;
    }

    private String getValidFileContentValue(Context var1, String var2, String var3, Map var4, String var5, String[] var6, Map var7, String var8, Map<String, Set<String>> var9, boolean var10) throws Exception {
        if (var3 != null) {
            String var11 = (String)var4.get("AttributeName");
            String var12 = (String)var4.get("ColumnType");
            String var13 = (String)var4.get("validationMethod");
            String var14 = (String)var4.get("validationProgram");
            if (!"NA".equalsIgnoreCase(var13) && !"NA".equalsIgnoreCase(var14)) {
                Map var15 = null;
                if ("validateDependency".equalsIgnoreCase(var13)) {
                    var15 = this.validateDependency(var1, var3, var8, var9);
                } else if (!"validateDate".equalsIgnoreCase(var13) && !"validateConstraintDate".equalsIgnoreCase(var13)) {
                    if ("validateTaskDuration".equalsIgnoreCase(var13)) {
                        var15 = this.validateTaskDuration(var1, var3);
                    } else if ("validateName".equalsIgnoreCase(var13)) {
                        var15 = this.validateName(var1, var3);
                    } else if ("validateType".equalsIgnoreCase(var13)) {
                        var15 = this.validateType(var1, var3, var6, var7, var10);
                    } else if ("validateTaskWBS".equalsIgnoreCase(var13)) {
                        var15 = this.validateTaskWBS(var1, var3);
                    } else if ("validateConstraintType".equalsIgnoreCase(var13)) {
                        var15 = this.validateConstraintType(var1, var3);
                    } else if (!var3.isEmpty()) {
                        HashMap var16 = new HashMap();
                        var16.put("timeZone", String.valueOf(var5));
                        var16.put("rowInfo", var6);
                        var16.put("parentDetails", var7);
                        if (var12.equalsIgnoreCase("Date")) {
                            var16.put("Date", var3);
                        } else {
                            var16.put(var2, var3);
                        }

                        String[] var17 = JPO.packArgs(var16);
                        var15 = (Map)JPO.invoke(var1, var14, new String[0], var13, var17, Map.class);
                    }
                } else {
                    var15 = this.validateDate(var1, var3, String.valueOf(var5), var1.getSession().getLanguage(), var13);
                }

                if (var15 == null) {
                    var3 = "";
                } else {
                    String var18 = (String)var15.get("Valid");
                    if (var12.equalsIgnoreCase("Date") && var18.equalsIgnoreCase("true")) {
                        var3 = (String)var15.get("Date");
                    }

                    if (var12.equalsIgnoreCase("Person")) {
                        var3 = (String)var15.get("AssigneeList");
                    }

                    if (!var18.equalsIgnoreCase("true")) {
                        var3 = "Error:" + (String)var15.get("Error");
                        this.isFileCorrect = false;
                    }
                }
            }
        } else {
            var3 = "";
        }

        return var3;
    }

    private Map<String, String> validateTaskWBS(Context var1, String var2) throws Exception {
        HashMap var3 = new HashMap();
        String var4 = "";
        String var5 = "true";
        this.WBSLevelList.add(var2);
        if (ProgramCentralUtil.isNotNullString(var2)) {
            for(int var6 = 0; var6 < var2.length(); ++var6) {
                char var7 = var2.charAt(var6);
                if ((var7 < '0' || var7 > '9') && var7 != '.') {
                    var4 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.InvalidImportFileFormat", var1.getSession().getLanguage());
                    var5 = "false";
                }
            }
        } else {
            var4 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.FieldShouldNotBeEmpty", var1.getSession().getLanguage());
            var5 = "false";
        }

        var3.put("Error", var4);
        var3.put("Valid", var5);
        return var3;
    }

    private Map<String, String> validateDependency(Context var1, String var2, String var3, Map<String, Set<String>> var4) throws Exception {
        HashMap var5 = new HashMap();
        String var6 = "";
        String var7 = "true";
        StringList var8 = new StringList(4);
        var8.add("FS");
        var8.add("SF");
        var8.add("FF");
        var8.add("SS");
        StringList var9 = new StringList();
        var9.add("d");
        var9.add("h");
        StringBuffer var10 = new StringBuffer();
        if (var2 != null && !"".equalsIgnoreCase(var2)) {
            StringList var11 = FrameworkUtil.split(var2, ",");
            boolean var12 = false;
            int var13 = var11.size();

            for(int var14 = 0; var14 < var13; ++var14) {
                String var15 = (String)var11.get(var14);
                String var16 = "";
                String var17 = "";
                String var18 = "";
                String var19 = "";
                String var20 = "";
                String var21 = "";
                boolean var22 = false;
                if (ProgramCentralUtil.isNullString(var15)) {
                    var12 = true;
                    break;
                }

                if (var15.indexOf(":") == -1) {
                    var22 = true;
                    var16 = var15;
                    var21 = "FS";
                }

                StringList var23;
                if (!var22) {
                    var23 = FrameworkUtil.split(var15, ":");
                    var16 = (String)var23.get(0);
                    var21 = (String)var23.get(1);
                }

                try {
                    int var30 = Integer.parseInt(var16);
                } catch (NumberFormatException var28) {
                    var12 = true;
                    break;
                }

                StringList var25;
                if (var21.indexOf("+") == -1 && var21.indexOf("-") == -1) {
                    var17 = var21;
                    var18 = "+";
                    var19 = "0.0";
                    var20 = "d";
                } else {
                    var18 = var21.indexOf("+") != -1 ? "+" : "-";
                    var23 = FrameworkUtil.split(var21, var18);
                    var17 = (String)var23.get(0);
                    var17 = var17.trim();
                    String var24 = (String)var23.get(1);
                    var24 = var24.trim();
                    if (var24.indexOf(" ") == -1) {
                        var12 = true;
                        break;
                    }

                    var25 = FrameworkUtil.split(var24, " ");
                    var19 = (String)var25.get(0);
                    var20 = ((String)var25.get(1)).toLowerCase();
                }

                if (var17 == null || "".equals(var17) || "null".equals(var17)) {
                    var12 = true;
                    break;
                }

                Iterator var31 = var8.iterator();
                boolean var32 = false;
                var25 = null;

                while(var31.hasNext()) {
                    String var33 = (String)var31.next();
                    if (var17.equals(var33)) {
                        var32 = true;
                        break;
                    }
                }

                if (!var32) {
                    var12 = true;
                    break;
                }

                if ("+".equals(var18) || "-".equals(var18)) {
                    try {
                        double var26 = com.matrixone.apps.common.Task.parseToDouble(var19);
                    } catch (NumberFormatException var29) {
                        var12 = true;
                        break;
                    }

                    if (!var9.contains(var20)) {
                        var12 = true;
                        break;
                    }
                }

                boolean var34 = "0".equalsIgnoreCase(var16) || var3.equalsIgnoreCase(var16);
                if (var34) {
                    if (var10.length() != 0) {
                        var10.append(",");
                    }

                    var10.append(var16).append(":").append(var17).append(var18).append(var19).append(" ").append(var20);
                } else {
                    Set<String> var27 = var4.get(var3);
                    if (var27 == null) {
                        var27 = new HashSet();
                        var4.put(var3, var27);
                    }

                    ((Set)var27).add(var16);
                }
            }

            if (var12) {
                var6 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.UnsupportedDependencyType", var1.getSession().getLanguage()) + ".";
                var7 = "false";
            } else if (var10.length() > 1) {
                var6 = "\"" + var10 + "\" " + EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.CircularDependancy", var1.getSession().getLanguage());
                var7 = "false";
            }
        }

        var5.put("Error", var6);
        var5.put("Valid", var7);
        return var5;
    }

    private Map<String, String> validateDate(Context var1, String var2, String var3, String var4, String var5) throws Exception {
        HashMap var6 = new HashMap();
        String var7 = "";
        String var8 = "true";
        String var9 = EnoviaResourceBundle.getProperty(var1, "emxImportExportProjectSettings", Locale.US, "emxProgramCentral.ProjectImport.eMatrixDateFormat");
        if (ProgramCentralUtil.isNotNullString(var2)) {
            String var10 = ImportUtil.dateValidator(var9, var2, var4, var1);
            if (ProgramCentralUtil.isNullString(var10)) {
                var8 = "false";
                var7 = var2 + " " + EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Import.InvalidDate", var4) + ".";
            } else {
                try {
                    TimeZone var11 = TimeZone.getTimeZone(var1.getSession().getTimezone());
                    double var12 = -1.0 * (double)var11.getRawOffset();
                    double var14 = new Double(var12 / 3600000.0);
                    var2 = eMatrixDateFormat.getFormattedDisplayDate(var10.trim(), var14, Locale.US);
                } catch (Exception var16) {
                    var16.printStackTrace();
                }
            }
        } else if ("validateDate".equalsIgnoreCase(var5)) {
            var7 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.FieldShouldNotBeEmpty", var4) + ".";
            var8 = "false";
        }

        var6.put("Error", var7);
        var6.put("Valid", var8);
        var6.put("Date", var2);
        return var6;
    }

    private Map<String, String> validateTaskDuration(Context var1, String var2) throws Exception {
        HashMap var3 = new HashMap();
        StringBuffer var4 = new StringBuffer("*[]<>@$%?|,");
        String var5 = "";
        String var6 = "true";
        if (ProgramCentralUtil.isNullString(var2)) {
            var6 = "false";
            var5 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.FieldShouldNotBeEmpty", var1.getSession().getLanguage());
        } else {
            for(int var7 = 0; var7 < var4.length(); ++var7) {
                char var8 = var4.charAt(var7);
                if (var2.indexOf(var8) >= 0) {
                    var5 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.EstimatedDuration", var1.getSession().getLanguage()) + " ";
                    var5 = var5 + EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Import.MustBeARealNumber", var1.getSession().getLanguage());
                    var6 = "false";
                }
            }

            if ("true".equalsIgnoreCase(var6)) {
                try {
                    var2 = var2.replaceAll("days|hours|d|h|D|H", "").trim();
                    if (com.matrixone.apps.common.Task.parseToDouble(var2) < 0.0) {
                        var5 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.ValueMustBeAPositiveReal", var1.getSession().getLanguage()) + " ";
                        var6 = "false";
                    }
                } catch (Exception var9) {
                    var5 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.ValueMustBeAPositiveReal", var1.getSession().getLanguage()) + " ";
                    var6 = "false";
                }
            }
        }

        var3.put("Error", var5);
        var3.put("Valid", var6);
        return var3;
    }

    private Map<String, String> validateType(Context var1, String var2, String[] var3, Map var4, boolean var5) throws Exception {
        HashMap var6 = new HashMap();
        String var7 = "";
        String var8 = "true";
        String var9 = null;
        String var10 = null;
        new StringList();
        if (var3 != null && var3.length > 0) {
            var9 = var3[0];
        }

        int var12 = var9.lastIndexOf(".");
        if (var9 != null && var12 != -1) {
            StringList var11 = (StringList)CacheUtil.getCacheObject(var1, "NotSupportedTypeListAsParent");
            String var13;
            if (var11 == null || var11.isEmpty()) {
                var13 = "print type $1 select derivative dump $2";
                String var14 = MqlUtil.mqlCommand(var1, var13, new String[]{"Gate", "|"});
                var11 = FrameworkUtil.split(var14, "|");
                var14 = MqlUtil.mqlCommand(var1, var13, new String[]{"Milestone", "|"});
                var11.addAll(FrameworkUtil.split(var14, "|"));
                var11.add("Gate");
                var11.add("Milestone");
                CacheUtil.setCacheObject(var1, "NotSupportedTypeListAsParent", var11);
            }

            var13 = var9.substring(0, var12);
            Map var18 = (Map)var4.get(var13);
            if (var18 != null && var18.containsKey("type")) {
                var10 = (String)var18.get("type");
                if (var11.contains(var10)) {
                    this.isFileCorrect = false;
                    Locale var15 = new Locale(var1.getSession().getLanguage());
                    String[] var16 = new String[]{var10, null};
                    var7 = MessageUtil.getMessage(var1, (String)null, "emxProgramCentral.ProjectImport.InvalidSummaryType", var16, (String)null, var15, "emxProgramCentralStringResource");
                    var18.put("type", "Error:" + var7);
                }
            }
        }

        StringList var17 = (StringList)CacheUtil.getCacheObject(var1, "DPM_ImportProject_ProjectList");
        if (var17 == null || var17.isEmpty()) {
            var17 = ProgramCentralUtil.getSubTypesList(var1, DomainObject.TYPE_PROJECT_SPACE);
            var17.remove(ProgramCentralConstants.TYPE_EXPERIMENT);
            var17.remove(ProgramCentralConstants.TYPE_PROJECT_BASELINE);
            var17.remove(ProgramCentralConstants.TYPE_PROJECT_SNAPSHOT);
            CacheUtil.setCacheObject(var1, "DPM_ImportProject_ProjectList", var17);
        }

        StringList var19 = (StringList)CacheUtil.getCacheObject(var1, "typeList");
        if (var19 == null || var19.isEmpty()) {
            var19 = ProgramCentralUtil.getSubTypesList(var1, DomainObject.TYPE_TASK_MANAGEMENT);
            CacheUtil.setCacheObject(var1, "typeList", var19);
        }

        if (ProgramCentralUtil.isNullString(var2) || !var19.contains(var2) && !var17.contains(var2)) {
            var7 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.TaskTypeNotSupported", var1.getSession().getLanguage()) + ".";
            var8 = "false";
        } else if (!var5 && var17.contains(var2) && !"0".equals(var9)) {
            var7 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.SubProjectNotSupported", var1.getSession().getLanguage()) + ".";
            var8 = "false";
        }

        var6.put("Error", var7);
        var6.put("Valid", var8);
        return var6;
    }

    private Map<String, String> validateName(Context var1, String var2) throws Exception {
        HashMap var3 = new HashMap();
        String var4 = "";
        String var5 = "true";
        if (ProgramCentralUtil.isNullString(var2)) {
            var4 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.FieldShouldNotBeEmpty", var1.getSession().getLanguage());
            var5 = "false";
        }

        if (var2.length() > 128) {
            var4 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.FieldExceedsMaxLength", var1.getSession().getLanguage());
            var5 = "false";
        }

        var2 = var2.contains(" ") ? var2.replace(" ", "") : var2;
        String var6 = EnoviaResourceBundle.getProperty(var1, "emxFramework.Javascript.NameBadChars");
        var6 = var6 + ";";
        boolean var7 = false;
        StringBuilder var8 = new StringBuilder();
        int var9 = 0;

        for(int var10 = var6.length(); var9 < var10; ++var9) {
            char var11 = var6.charAt(var9);
            if (var2.indexOf(var11) >= 0) {
                String var12 = "" + var11;
                if (';' == var11) {
                    var12 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.Common.Semicolon", var1.getSession().getLanguage());
                }

                if (var8.indexOf(var12) == -1) {
                    var8.append(var12);
                }

                var7 = true;
            }
        }

        if (var7) {
            var4 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.InvalidCharacterInTaskName", var1.getSession().getLanguage());
            var4 = var4 + " '" + var8.toString() + "'.";
            var5 = "false";
        }

        var3.put("Error", var4);
        var3.put("Valid", var5);
        return var3;
    }

    private Map<String, String> validateConstraintType(Context var1, String var2) throws Exception {
        HashMap var3 = new HashMap();
        String var4 = "";
        String var5 = "true";
        if (ProgramCentralUtil.isNotNullString(var2) && !this.slDefaultContraintType.contains(var2)) {
            var4 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectImport.InvalidConstraintType", var1.getSession().getLanguage());
            var4 = var4 + " '" + var2 + "'.";
            var5 = "false";
        }

        var3.put("Error", var4);
        var3.put("Valid", var5);
        return var3;
    }

    public Map createAndConnectShadowGate(Context var1, String var2, String[] var3) throws Exception {
        HashMap var4 = new HashMap();
        return var4;
    }

    public MapList getProjectCalendars(Context var1) throws MatrixException {
        new MapList();
        StringList var3 = new StringList();
        var3.add("id");
        var3.add("name");
        StringList var4 = new StringList("id[connection]");
        MapList var2 = this.getRelatedObjects(var1, ProgramCentralConstants.RELATIONSHIP_CALENDAR, ProgramCentralConstants.TYPE_WORK_CALENDAR, var3, var4, false, true, (short)1, "", "", 0);
        return var2;
    }

    public MapList getDefaultCalendar(Context var1) throws MatrixException {
        new MapList();
        StringList var3 = new StringList("id");
        StringList var4 = new StringList("id[connection]");
        MapList var2 = this.getRelatedObjects(var1, ProgramCentralConstants.RELATIONSHIP_DEFAULT_CALENDAR, ProgramCentralConstants.TYPE_WORK_CALENDAR, var3, var4, false, true, (short)1, "", "", 0);
        return var2;
    }

    public void addCalendars(Context var1, StringList var2) throws MatrixException {
        DomainObject var3 = DomainObject.newInstance(var1);
        Iterator var4 = var2.iterator();
        String var5 = "";

        while(true) {
            String var6;
            while(true) {
                if (!var4.hasNext()) {
                    return;
                }

                var5 = ProgramCentralConstants.RELATIONSHIP_CALENDAR;
                var6 = (String)var4.next();
                if (!var6.contains("|DefaultCalendar")) {
                    break;
                }

                var6 = var6.substring(0, var6.indexOf("|DefaultCalendar"));
                new MapList();
                MapList var7 = this.getDefaultCalendar(var1);
                if (!var7.isEmpty()) {
                    boolean var8 = false;
                    Iterator var9 = var7.iterator();

                    while(var9.hasNext()) {
                        Map var10 = (Map)var9.next();
                        String var11 = (String)var10.get("id");
                        String var12 = (String)var10.get("id[connection]");
                        if (!var6.equals(var11)) {
                            DomainRelationship.setType(var1, var12, ProgramCentralConstants.RELATIONSHIP_CALENDAR);
                            var8 = true;
                        }
                    }

                    if (!var8) {
                        continue;
                    }
                }

                var5 = ProgramCentralConstants.RELATIONSHIP_DEFAULT_CALENDAR;
                break;
            }

            var3.setId(var6);
            DomainRelationship.connect(var1, this, var5, var3);
        }
    }

    public void removeCalendars(Context var1, StringList var2) throws MatrixException {
        new MapList();
        String[] var4 = new String[var2.size()];
        int var5 = 0;
        MapList var3 = this.getProjectCalendars(var1);
        Iterator var6 = var3.iterator();

        while(var6.hasNext()) {
            Map var7 = (Map)var6.next();
            String var8 = (String)var7.get("id");
            String var9 = (String)var7.get("id[connection]");
            if (var2.contains(var8)) {
                var4[var5] = var9;
                ++var5;
            }
        }

        DomainRelationship.disconnect(var1, var4);
    }

    public MapList getProjectBaselines(Context var1, StringList var2, StringList var3) throws MatrixException {
        ComponentsUtil.checkLicenseReserved(var1, ProgramCentralConstants.PGE_PRG_LICENSE_ARRAY);
        new MapList();

        try {
            byte var5 = 1;
            String var6 = ProgramCentralConstants.RELATIONSHIP_PROJECT_BASELINE;
            String var7 = ProgramCentralConstants.TYPE_PROJECT_BASELINE;
            MapList var4 = this.getRelatedObjects(var1, var6, var7, var2, var3, false, true, var5, (String)null, (String)null, (short)0, false, true, (short)0, (Pattern)null, (Pattern)null, (Map)null, (String)null);
            return var4;
        } catch (Exception var8) {
            throw new MatrixException(var8);
        }
    }

    public boolean isCtxUserProjectMember(Context var1) throws Exception {
        String var2 = PersonUtil.getPersonObjectID(var1);
        MapList var3 = this.getMembers(var1, new StringList("id"), (StringList)null, (String)null, (String)null, true, true);
        Iterator var4 = var3.iterator();

        String var6;
        do {
            if (!var4.hasNext()) {
                return false;
            }

            Map var5 = (Map)var4.next();
            var6 = (String)var5.get("id");
        } while(!var2.equals(var6));

        return true;
    }

    public MapList getProjectRelatedObjects(Context var1, String var2, String var3, StringList var4, StringList var5, boolean var6, boolean var7, short var8, String var9, String var10) throws FrameworkException {
        new MapList();
        StringList var12 = new StringList();
        StringList var13 = new StringList();

        MapList var11;
        int var15;
        int var16;
        try {
            if (ProgramCentralUtil.isNullString(var2)) {
                var2 = RELATIONSHIP_SUBTASK;
            }

            if (ProgramCentralUtil.isNullString(var3)) {
                var3 = TYPE_PROJECT_MANAGEMENT;
            }

            ProgramCentralUtil.pushUserContext(var1);
            StringList var14 = new StringList(2);
            var14.addElement("id");
            var14.addElement(ProgramCentralConstants.SELECT_IS_PROJECT_SPACE);
            var11 = this.getRelatedObjects(var1, var2, var3, var14, (StringList)null, var6, var7, var8, var9, var10, 0);
            var15 = 0;

            for(var16 = var11.size(); var15 < var16; ++var15) {
                Map var17 = (Map)var11.get(var15);
                String var18 = (String)var17.get(ProgramCentralConstants.SELECT_IS_PROJECT_SPACE);
                if ("true".equalsIgnoreCase(var18)) {
                    var13.addElement((String)var17.get("id"));
                }
            }
        } finally {
            ProgramCentralUtil.popUserContext(var1);
        }

        int var33 = 0;

        for(var15 = var13.size(); var33 < var15; ++var33) {
            String var35 = (String)var13.get(var33);

            try {
                DomainObject.newInstance(var1, var35);
            } catch (FrameworkException var29) {
                var12.addElement(var35);
            }
        }

        try {
            ProgramCentralUtil.pushUserContext(var1);
            StringBuilder var34 = new StringBuilder();
            var15 = 0;

            for(var16 = var12.size(); var15 < var16; ++var15) {
                String var36 = (String)var12.get(var15);
                if (var15 == 0) {
                    var34.append("id != ").append(var36);
                } else {
                    var34.append(" && id != ").append(var36);
                }
            }

            if (var34.length() > 0) {
                if (var9 != null && !var9.isEmpty()) {
                    var9 = var9 + var9 + " && " + var34.toString();
                } else {
                    var9 = var34.toString();
                }
            }

            var11 = this.getRelatedObjects(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, 0);
        } catch (Exception var30) {
            var30.printStackTrace();
        } finally {
            ProgramCentralUtil.popUserContext(var1);
        }

        return var11;
    }

    public String getSchedule(Context var1) throws FrameworkException {
        return this.getInfo(var1, ProgramCentralConstants.SELECT_PROJECT_SCHEDULE);
    }

    public MapList getCriticalTasks(Context var1) throws MatrixException {
        StringList var2 = new StringList(3);
        var2.add("id");
        var2.add(com.matrixone.apps.common.Task.SELECT_CRITICAL_TASK);
        var2.add("name");
        String var3 = com.matrixone.apps.common.Task.SELECT_CRITICAL_TASK + " == TRUE || type.kindOf[" + DomainConstants.TYPE_PROJECT_SPACE + "]==TRUE";
        MapList var4 = this.getRelatedObjects(var1, ProgramCentralConstants.RELATIONSHIP_SUBTASK, "*", var2, (StringList)null, false, true, (short)0, var3, (String)null, (short)0, false, true, (short)0, (Pattern)null, (Pattern)null, (Map)null, (String)null);
        return var4;
    }

    public boolean hasAccessToAddGovernedItemsAction(Context var1) throws MatrixException {
        try {
            String var2 = this.getInfo(var1, "current");
            return ProgramCentralConstants.STATE_PROJECT_TASK_CREATE.equals(var2) || ProgramCentralConstants.STATE_PROJECT_TASK_ASSIGN.equals(var2) || ProgramCentralConstants.STATE_PROJECT_TASK_ACTIVE.equals(var2) || ProgramCentralConstants.STATE_PROJECT_TASK_REVIEW.equals(var2);
        } catch (Exception var3) {
            throw new MatrixException(var3);
        }
    }

    public static Map getProjectFloatFromPAL(Context var0, String var1) {
        Object var2 = new HashMap();
        if (UIUtil.isNotNullAndNotEmpty(var1)) {
            try {
                String var4 = MqlUtil.mqlCommand(var0, true, true, "print bus $1 select $2 dump", true, new String[]{var1, ProgramCentralConstants.SELECT_ATTRIBUTE_PROJECTFLOAT});
                if (ProgramCentralUtil.isNotNullString(var4)) {
                    String var5 = unzip(var4);
                    var5 = var5.replace("{", "");
                    var5 = var5.replace("}", "");
                    var2 = (Map)Arrays.stream(var5.split(",")).map((var0x) -> {
                        return var0x.split("=");
                    }).collect(Collectors.toMap((var0x) -> {
                        return var0x[0].trim();
                    }, (var0x) -> {
                        return var0x[1];
                    }));
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        return (Map)var2;
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

    public MapList getRIOItems(Context var1) throws Exception {
        new MapList();

        try {
            MapList var5 = new MapList();
            HashSet var6 = new HashSet();
            new HashMap();
            HashSet var8 = new HashSet();
            HashSet var9 = new HashSet();
            HashSet var10 = new HashSet();
            HashMap var11 = new HashMap();
            var11.put("objectId", this.getObjectId());
            var11.put("modeTabName", "ResolvesTab");
            String[] var12 = JPO.packArgs(var11);
            MapList var13 = (MapList)JPO.invoke(var1, "emxRisk", (String[])null, "getAllRisks", var12, MapList.class);
            Iterator var14 = var13.iterator();

            while(var14.hasNext()) {
                Object var15 = var14.next();
                Map var16 = (Map)var15;
                String var17 = (String)var16.get("id");
                var8.add(var17);
            }

            if (_testCase) {
                var11.put("fromODT", "true");
                var12 = JPO.packArgs(var11);
            }

            MapList var44 = (MapList)JPO.invoke(var1, "emxProgramCentralUtil", (String[])null, "getAllContextIssues", var12, MapList.class);
            Iterator var45 = var44.iterator();

            while(var45.hasNext()) {
                Object var47 = var45.next();
                Map var49 = (Map)var47;
                String var18 = (String)var49.get("id");
                var10.add(var18);
            }

            var11.put("mode", "PMCProjectOpportunity");
            var11.put("modeTabName", "ResolvesTab");
            String[] var46 = JPO.packArgs(var11);
            MapList var48 = (MapList)JPO.invoke(var1, "emxRisk", (String[])null, "getAllRisks", var46, MapList.class);
            Iterator var50 = var48.iterator();

            while(var50.hasNext()) {
                Object var52 = var50.next();
                Map var19 = (Map)var52;
                String var20 = (String)var19.get("id");
                var9.add(var20);
            }

            var6.addAll(var8);
            var6.addAll(var10);
            var6.addAll(var9);
            HashSet var51 = new HashSet();
            HashSet var53 = new HashSet();
            HashSet var54 = new HashSet();
            String[] var55 = (String[])var8.stream().toArray((var0) -> {
                return new String[var0];
            });
            String[] var21 = (String[])var10.stream().toArray((var0) -> {
                return new String[var0];
            });
            String[] var22 = (String[])var9.stream().toArray((var0) -> {
                return new String[var0];
            });
            StringList var23 = new StringList();
            var23.add(RESOLVED_BY_ITEM_ID_FOR_ISSUE);
            BusinessObjectWithSelectList var24 = ProgramCentralUtil.getObjectWithSelectList(var1, var21, var23, true);
            Iterator var25 = var24.iterator();

            while(var25.hasNext()) {
                BusinessObjectWithSelect var26 = (BusinessObjectWithSelect)var25.next();
                StringList var27 = var26.getSelectDataList(RESOLVED_BY_ITEM_ID_FOR_ISSUE);
                if (var27 != null) {
                    var53.addAll(var27);
                }
            }

            var25 = var53.iterator();

            while(var25.hasNext()) {
                String var57 = (String)var25.next();
                HashMap var59 = new HashMap();
                var59.put("objectId", var57);
                var59.put("modeTabName", "ResolvesTab");
                if (_testCase) {
                    var59.put("fromODT", "true");
                }

                String[] var28 = JPO.packArgs(var59);
                MapList var29 = (MapList)JPO.invoke(var1, "emxProgramCentralUtil", (String[])null, "getAllContextIssues", var28, MapList.class);
                var5.addAll(var29);
            }

            StringList var56 = new StringList();
            var56.add("from[Contributes To].to.id");
            BusinessObjectWithSelectList var58 = ProgramCentralUtil.getObjectWithSelectList(var1, var55, var56, true);
            Iterator var60 = var58.iterator();

            while(var60.hasNext()) {
                BusinessObjectWithSelect var61 = (BusinessObjectWithSelect)var60.next();
                StringList var65 = var61.getSelectDataList("from[Contributes To].to.id");
                if (var65 != null) {
                    var51.addAll(var65);
                }
            }

            var60 = var51.iterator();

            while(var60.hasNext()) {
                String var63 = (String)var60.next();
                HashMap var66 = new HashMap();
                var66.put("objectId", var63);
                var66.put("modeTabName", "ResolvesTab");
                String[] var30 = JPO.packArgs(var66);
                MapList var31 = (MapList)JPO.invoke(var1, "emxRisk", (String[])null, "getAllRisks", var30, MapList.class);
                var5.addAll(var31);
            }

            BusinessObjectWithSelectList var62 = ProgramCentralUtil.getObjectWithSelectList(var1, var22, var56, true);
            Iterator var64 = var62.iterator();

            while(var64.hasNext()) {
                BusinessObjectWithSelect var67 = (BusinessObjectWithSelect)var64.next();
                StringList var69 = var67.getSelectDataList("from[Contributes To].to.id");
                if (var69 != null) {
                    var54.addAll(var69);
                }
            }

            var64 = var54.iterator();

            while(var64.hasNext()) {
                String var70 = (String)var64.next();
                HashMap var71 = new HashMap();
                var71.put("objectId", var70);
                var71.put("mode", "PMCProjectOpportunity");
                var71.put("modeTabName", "ResolvesTab");
                String[] var75 = JPO.packArgs(var71);
                MapList var32 = (MapList)JPO.invoke(var1, "emxRisk", (String[])null, "getAllRisks", var75, MapList.class);
                var5.addAll(var32);
            }

            var64 = var5.iterator();

            while(var64.hasNext()) {
                Object var72 = var64.next();
                Map var74 = (Map)var72;
                String var77 = (String)var74.get("id");
                var6.add(var77);
            }

            StringList var68 = new StringList();
            var68.add("to[Contributes To].from.id");
            var68.add(RESOLVED_ISSUE_ID);
            BusinessObjectWithSelectList var73 = ProgramCentralUtil.getObjectWithSelectList(var1, new String[]{this.getObjectId()}, var68, true);
            BusinessObjectWithSelect var76 = (BusinessObjectWithSelect)var73.getElement(0);
            StringList var78 = var76.getSelectDataList("to[Contributes To].from.id");
            StringList var79 = var76.getSelectDataList(RESOLVED_ISSUE_ID);
            if (null != var78) {
                var6.addAll(var78);
            }

            if (null != var79) {
                var6.addAll(var79);
            }

            StringList var33 = new StringList("id");
            var33.add(ProgramCentralConstants.SELECT_IS_RISK);
            var33.add(RiskManagement.SELECT_RISK_VISIBILITY);
            var33.add("current.access[modify]");
            var33.add("current.access[fromconnect]");
            var33.add("type.kindof[Issue]");
            String var34 = PropertyUtil.getSchemaProperty(var1, "type_Issue");
            String var35 = "Risk Management," + var34;
            String var36 = "current.access[fromconnect]=='TRUE'&&";
            var36 = var36 + " current!='" + ProgramCentralConstants.STATE_ISSUE_CLOSE + "'";
            var36 = var36 + " || current !='" + RiskManagement.STATE_PROJECT_RISK_COMPLETE + "'";
            MapList var4 = findObjects(var1, var35, (String)null, var36, var33);
            Iterator var37 = var4.iterator();

            while(var37.hasNext()) {
                Map var38 = (Map)var37.next();
                String var39 = (String)var38.get("id");
                String var40 = (String)var38.get(ProgramCentralConstants.SELECT_IS_RISK);
                if (var6.contains(var39)) {
                    var37.remove();
                } else if ("true".equalsIgnoreCase(var40)) {
                    String var41 = (String)var38.get(RiskManagement.SELECT_RISK_VISIBILITY);
                    String var42 = (String)var38.get("current.access[modify]");
                    if ("Restricted".equalsIgnoreCase(var41) && !"TRUE".equalsIgnoreCase(var42)) {
                        var37.remove();
                    }
                }
            }

            return var4;
        } catch (Exception var43) {
            throw new Exception(var43);
        }
    }

    private void getAllChildren(Dataobject var1, List<Dataobject> var2, short var3) {
        Iterator var4 = var1.getChildren().iterator();

        while(var4.hasNext()) {
            Dataobject var5 = (Dataobject)var4.next();
            var2.add(var5);
            if (var3 == 0) {
                this.getAllChildren(var5, var2, var3);
            } else {
                short var6 = Short.valueOf((String)var5.getDataelements().get("level"));
                if (var6 < var3) {
                    this.getAllChildren(var5, var2, var3);
                }
            }
        }

    }

    public MapList getObjectListFromPAL(Context var1, StringList var2, short var3) throws FrameworkException {
        return this.getObjectListFromPAL(var1, var2, var3, (List)null);
    }

    public MapList getObjectListFromPAL(Context var1, StringList var2, short var3, List<String> var4) throws FrameworkException {
        String var5 = "to[Subtask].id";
        String var6 = "to[Subtask].from.physicalid";
        String[] var7 = null;
        MapList var8 = null;
        MapList var9 = new MapList();

        try {
            String var10 = "";
            String var11 = "";
            if (!_testCase) {
                String var41 = "print bus $1 select $2 $3 $4 dump";
                String var43 = MqlUtil.mqlCommand(var1, false, false, var41, false, new String[]{this.getObjectId(), ProgramCentralConstants.SELECT_PROJECT_ACCESS_LIST_ID, ProgramCentralConstants.SELECT_PROJECT_ACCESS_KEY_ID, "physicalid"});
                if (var43 == null || var43.isEmpty()) {
                    throw new FrameworkException("Invalid objectId.....! objectId should be Task management and should have a PAL connection");
                }

                String[] var45 = var43.split("\\,");
                if (var45.length < 2) {
                    return new MapList();
                }

                var10 = var45[0];
                var11 = var45[1];
            } else {
                StringList var12 = new StringList();
                var12.add("physicalid");
                var12.add(ProgramCentralConstants.SELECT_PROJECT_ACCESS_LIST_ID);
                var12.add(ProgramCentralConstants.SELECT_PROJECT_ACCESS_KEY_ID);
                BusinessObjectWithSelectList var13 = null;
                var13 = ProgramCentralUtil.getObjectWithSelectList(var1, new String[]{this.getObjectId()}, var12, true);
                BusinessObjectWithSelect var14 = (BusinessObjectWithSelect)var13.getElement(0);
                var10 = var14.getSelectData(ProgramCentralConstants.SELECT_PROJECT_ACCESS_LIST_ID);
                if (var10.isEmpty()) {
                    var10 = var14.getSelectData(ProgramCentralConstants.SELECT_PROJECT_ACCESS_KEY_ID);
                }

                var11 = var14.getSelectData("physicalid");
            }

            ProjectSequence var42 = new ProjectSequence(var1, var10);
            Map var44 = var42.getSequenceData(var1);
            HashMap var46 = new HashMap();
            List var15 = var42.getProjects(var1);
            int var16 = var15.size();
            if (var16 > 1) {
                ArrayList var17 = new ArrayList();
                Iterator var18 = var15.iterator();

                Dataobject var19;
                while(var18.hasNext()) {
                    var19 = (Dataobject)var18.next();
                    String var20 = (String)var19.getDataelements().get("seqId");
                    if (Integer.valueOf(var20) != 0) {
                        var17.add(var19.getId());
                    }
                }

                String[] var48 = new String[var17.size()];
                var17.toArray(var48);
                var19 = null;
                BusinessObjectWithSelectList var50;
                if (_testCase) {
                    var50 = ProgramCentralUtil.getObjectWithSelectList(var1, var48, new StringList("current.access[read]"), true);
                } else {
                    var50 = ProgramCentralUtil.getObjectWithSelectList(var1, var48, new StringList("current.access[read]"));
                }

                int var51 = 0;

                for(int var21 = var50.size(); var51 < var21; ++var51) {
                    BusinessObjectWithSelect var22 = (BusinessObjectWithSelect)var50.getElement(var51);
                    String var23 = var22.getSelectData("current.access[read]");
                    var46.put(var48[var51], Boolean.valueOf(var23));
                }
            }

            Dataobject var47 = (Dataobject)var44.get(var11);
            String var49 = (String)var47.getDataelements().get("seqId");
            String var52 = (String)var47.getDataelements().get("level");
            Iterator var55;
            if (var3 != 0) {
                Object var54 = new ArrayList();
                short var56 = (short)(Short.valueOf(var52) + var3);
                Iterator var60;
                Dataobject var61;
                if (var3 == 1) {
                    var54 = (List)var47.getChildren().stream().filter((var1x) -> {
                        return Short.valueOf((String)var1x.getDataelements().get("level")) <= var56;
                    }).collect(Collectors.toList());
                } else {
                    var60 = var47.getChildren().iterator();

                    while(var60.hasNext()) {
                        var61 = (Dataobject)var60.next();
                        ((List)var54).add(var61);
                        this.getAllChildren(var61, (List)var54, var56);
                    }
                }

                var44.clear();
                var60 = ((List)var54).iterator();

                while(var60.hasNext()) {
                    var61 = (Dataobject)var60.next();
                    var44.put(var61.getId(), var61);
                }
            } else if (Integer.valueOf(var49) != 0 && var3 == 0) {
                ArrayList var53 = new ArrayList();
                var55 = var47.getChildren().iterator();

                Dataobject var58;
                while(var55.hasNext()) {
                    var58 = (Dataobject)var55.next();
                    var53.add(var58);
                    this.getAllChildren(var58, var53, (short)0);
                }

                var44.clear();
                var55 = var53.iterator();

                while(var55.hasNext()) {
                    var58 = (Dataobject)var55.next();
                    var44.put(var58.getId(), var58);
                }
            }

            var44.remove(var11);
            Set var57 = var44.keySet();
            var7 = new String[var57.size()];
            var57.toArray(var7);
            var57.remove(var11);
            if (var2 != null) {
                var2.addElement(var5);
                var2.addElement(var6);
            }

            var55 = null;
            BusinessObjectWithSelectList var59;
            if (_testCase) {
                var59 = ProgramCentralUtil.getObjectWithSelectList(var1, var7, var2, true);
            } else {
                var59 = ProgramCentralUtil.getObjectWithSelectList(var1, var7, var2);
            }

            if (var4 != null) {
                var8 = FrameworkUtil.toMapList(var59, var4);
            } else {
                var8 = FrameworkUtil.toMapList(var59);
            }

            int var62 = 0;

            for(int var63 = var59.size(); var62 < var63; ++var62) {
                Map var24 = (Map)var8.get(var62);
                BusinessObjectWithSelect var25 = (BusinessObjectWithSelect)var59.getElement(var62);
                String var26 = var25.getSelectData("name");
                String var27 = var25.getSelectData("physicalid");
                StringList var28 = var25.getSelectDataList(var5);
                StringList var29 = var25.getSelectDataList(var6);
                boolean var30 = ProgramCentralUtil.isNotNullString(var27) && var28 != null && var29 != null;
                if (var30) {
                    Dataobject var31 = (Dataobject)var44.get(var27);
                    DataelementMap var32 = var31.getDataelements();
                    String var33 = (String)var32.get("level");
                    String var34 = (String)var32.get("seqId");
                    String var35 = (String)var32.get("project");
                    if ((var31.getChildren() == null || var31.getChildren().isEmpty()) && !"true".equalsIgnoreCase(var35)) {
                        var24.put("hasChildren", "false");
                    } else {
                        var24.put("hasChildren", "true");
                    }

                    int var36 = 0;
                    int var37 = var29.size();
                    if ("true".equalsIgnoreCase(var35)) {
                        for(String var38 = var31.getParent().getId(); var36 < var37 && !((String)var29.get(var36)).contains(var38); ++var36) {
                        }
                    }

                    if (var36 != var37) {
                        int var64 = Integer.valueOf(var33) - Integer.valueOf(var52);
                        var24.put("name", var26);
                        var24.put("level", String.valueOf(var64));
                        var24.put("direction", "from");
                        var24.put("id[connection]", var28.get(var36));
                        var24.put("seqId", var34);
                        if ("true".equalsIgnoreCase(var35)) {
                            boolean var39 = (Boolean)var46.get(var27);
                            if (var39) {
                                var9.add(var24);
                            }
                        } else {
                            var9.add(var24);
                        }
                    }
                }
            }

            var9.sort("seqId", "ascending", "integer");
        } catch (MatrixException | FoundationException var40) {
            var40.printStackTrace();
        }

        return var9;
    }

    private Dataobject getDefaultPreference(Context var1) throws Exception {
        String var3 = MqlUtil.mqlCommand(var1, false, "print bus $1 select $2 dump", false, new String[]{this.getObjectId(), ProgramCentralConstants.SELECT_ATTRIBUTE_PROJECT_USER_PREFERENCE});
        Dataobject var4 = new Dataobject();
        if (!var3.isEmpty()) {
            String var5 = unzip(var3);
            if (!var5.isEmpty()) {
                var4 = ServiceJson.readDataobjectfromJson(var5);
            }
        }

        return var4;
    }

    private Map<String, Dataobject> getDefaultUserPreference(Context var1) throws Exception {
        HashMap var2 = new HashMap();
        Dataobject var3 = this.getDefaultPreference(var1);
        if (var3 == null) {
            var3 = new Dataobject();
        }

        String var4 = var3.getId();
        if (var4 != null) {
            var2.put(var4, var3);
            Iterator var5 = var3.getChildren().iterator();

            while(var5.hasNext()) {
                Dataobject var6 = (Dataobject)var5.next();
                var4 = var6.getId();
                var2.put(var4, var6);
                var6.setParent(var3);
            }
        }

        return var2;
    }

    public Dataobject getUserPreference(Context var1) throws Exception {
        Map var2 = this.getDefaultUserPreference(var1);
        return (Dataobject)var2.get(var1.getUser());
    }

    public void setUserPreference(Context var1, String var2, String var3) throws Exception {
        String var4 = var1.getUser();
        String var5 = this.getObjectId();
        String var6 = ProgramCentralUtil.getPhysicalId(var1, var5);
        String var7 = "defCat";
        String var8 = "defTab";
        Dataobject var9 = null;
        Dataobject var10 = null;
        Map var11 = this.getDefaultUserPreference(var1);
        if (!var11.isEmpty()) {
            var9 = (Dataobject)var11.get(var6);
            var10 = (Dataobject)var11.get(var4);
        } else {
            var9 = new Dataobject();
            var9.setId(var6);
        }

        if (var10 == null) {
            var10 = new Dataobject();
            var10.setId(var4);
            var9.getChildren().add(var10);
        }

        DataelementMapAdapter.setDataelementValue(var10, var7, var2);
        DataelementMapAdapter.setDataelementValue(var10, var8, var3);
        String var12 = ServiceJson.generateJsonStringfromJAXB(var9);
        if (var12 != null && !var12.isEmpty()) {
            try {
                var12 = zip(var12);
            } catch (IOException var16) {
                var16.printStackTrace();
            }

            String var13 = "modify bus $1 $2 $3";

            try {
                (new MQLCommand()).executeCommand(var1, false, false, var13, new String[]{this.getObjectId(), ProgramCentralConstants.ATTRIBUTE_PROJECT_USER_PREFERENCE, var12});
            } catch (Exception var15) {
                var15.printStackTrace();
            }
        }

    }

    public void removeUserPreference(Context var1) throws Exception {
    }

    private static String zip(String var0) throws IOException {
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        Throwable var2 = null;

        String var32;
        try {
            GZIPOutputStream var3 = new GZIPOutputStream(var1);
            Throwable var4 = null;

            try {
                var3.write(var0.getBytes(StandardCharsets.UTF_8));
            } catch (Throwable var27) {
                var4 = var27;
                throw var27;
            } finally {
                if (var3 != null) {
                    if (var4 != null) {
                        try {
                            var3.close();
                        } catch (Throwable var26) {
                            var4.addSuppressed(var26);
                        }
                    } else {
                        var3.close();
                    }
                }

            }

            Base64.Encoder var31 = Base64.getEncoder();
            var32 = var31.encodeToString(var1.toByteArray());
        } catch (Throwable var29) {
            var2 = var29;
            throw var29;
        } finally {
            if (var1 != null) {
                if (var2 != null) {
                    try {
                        var1.close();
                    } catch (Throwable var25) {
                        var2.addSuppressed(var25);
                    }
                } else {
                    var1.close();
                }
            }

        }

        return var32;
    }

    public String bookmarkConversion(Context var1, String var2) throws Exception {
        try {
            ContextUtil.startTransaction(var1, true);
            gscProjectSpace var3 = new gscProjectSpace();
            var3.setId(var2);
            StringList var4 = new StringList(2);
            var4.add("name");
            var4.add("revision");
            Map var5 = var3.getInfo(var1, var4);
            String var6 = (String)var5.get("name");
            String var7 = (String)var5.get("revision");
            String var8 = "";
            String var9 = "";
            HashMap var10 = new HashMap();
            var10.put("Name", var6 + "-" + var7);
            var10.put("Description", var6 + "-" + var7);
            String[] var11 = JPO.packArgs(var10);
            Map var12 = (Map)JPO.invoke(var1, "emxWorkspace", (String[])null, "createWorkspaceProcess", var11, (Class)null);
            if (var12 == null) {
                throw new FrameworkException("Can't create Bookmark Workspace");
            } else {
                var8 = (String)var12.get("id");
                var9 = ProgramCentralUtil.getPhysicalId(var1, var8);
                DomainObject var13 = DomainObject.newInstance(var1, var8);
                var13.promote(var1);
                String var35 = "modify $1 $2 add interface $3";
                MqlUtil.mqlCommand(var1, var35, new String[]{"bus", var2, INTERFACE_DPMBOOKMARKROOT});
                var3.setAttributeValue(var1, ATTRIBUTE_BOOKMARK_PHYSICAL_ID, var9);
                String var15 = DomainConstants.RELATIONSHIP_PROJECT_VAULTS;
                StringList var16 = new StringList(1);
                var16.add("id[connection]");
                String var17 = "to[" + DomainConstants.RELATIONSHIP_PROJECT_VAULTS + "].from.id";
                StringList var18 = new StringList(1);
                var18.add("id");
                String var19 = "*";
                MapList var20 = var3.getRelatedObjects(var1, var15, var19, var18, var16, true, true, (short)1, (String)null, (String)null, 0);
                int var21 = var20.size();
                String var22 = "";
                String var23 = "";
                new HashMap();
                String var25 = "modify connection $1 from $2";

                for(int var26 = 0; var26 < var21; ++var26) {
                    Map var24 = (Map)var20.get(var26);
                    var22 = (String)var24.get("id[connection]");
                    var23 = (String)var24.get("id");
                    MqlUtil.mqlCommand(var1, var25, new String[]{var22, var8});
                    DomainAccess.deleteObjectOwnership(var1, var23, var2, "", true);
                }

                MapList var36 = DomainAccess.getAccessSummaryList(var1, var2);
                Iterator var27 = var36.iterator();

                while(var27.hasNext()) {
                    Map var28 = (Map)var27.next();
                    String var29 = (String)var28.get("org");
                    String var30 = (String)var28.get("name");
                    String var31 = (String)var28.get("project");
                    String var32 = (String)var28.get("comment");
                    String var33 = (String)var28.get("access");
                    if (var30.contains("_PRJ") && !"Primary".equals(var32)) {
                        if ("Project Lead".equals(var33)) {
                            DomainAccess.createObjectOwnership(var1, var8, "", var30, "Owner", var32);
                        } else {
                            DomainAccess.createObjectOwnership(var1, var8, "", var30, "Reader", var32);
                        }
                    }
                }

                ContextUtil.commitTransaction(var1);
                return var8;
            }
        } catch (Exception var34) {
            ContextUtil.abortTransaction(var1);
            var34.printStackTrace();
            throw var34;
        }
    }
//        return this.cloneFromTemplate(context, type, name, policy, vault, questionResponseMap, blank, isTemplateTaskAutoName, attributeMap, aTrue, true, selectedOptionForReferenceDocument, calendarIds, projectCloneOptions);
    public DomainObject cloneFromTemplate(Context context, String type, String name, String policy, String vault, Map questionResponseMap, String blank, boolean isTemplateTaskAutoName, Map attributeMap, boolean aTrue, boolean twoTrue, ContentReplicateOptions selectedOptionForReferenceDocument, StringList calendarIds, ProjectCloneOption... projectCloneOptions) throws FrameworkException {
        DebugUtil.debug("Entering cloneObject.");
        ContextUtil.startTransaction(context, true);

        try {
            PropertyUtil.setGlobalRPEValue(context, "CREATING_PROJECT_FROM_PT", "true");
            ComponentsUtil.checkLicenseReserved(context, ProgramCentralConstants.PGE_PRG_LICENSE_ARRAY);
            String var15 = this.getUniqueName("");
            DebugUtil.debug("Duplicating Object Information.");
            StringList tmplAttMap = this.getAttributeNames(context);
            tmplAttMap.remove(ATTRIBUTE_TASK_ACTUAL_DURATION);
            tmplAttMap.remove(ATTRIBUTE_TASK_ACTUAL_START_DATE);
            tmplAttMap.remove(ATTRIBUTE_TASK_ACTUAL_FINISH_DATE);
            tmplAttMap.remove(ATTRIBUTE_PERCENT_COMPLETE);
            tmplAttMap.remove(ATTRIBUTE_ORIGINATOR);
            tmplAttMap.remove(ATTRIBUTE_PROJECT_TYPE);
            tmplAttMap.remove(ATTRIBUTE_PROJECT_FUNCTION);
            tmplAttMap.remove("Is Proposal Template");
            tmplAttMap.remove("EnforceProjectName");
            tmplAttMap.remove("ProjectAutonameSeries");
            tmplAttMap.add(ProgramCentralConstants.ATTRIBUTE_SOURCE_ID);
            String var19 = "false";
            Map attributeMap1 = this.getAttributeMap(context, true);
            HashMap projAttrMap = new HashMap();
            Iterator iterator = tmplAttMap.iterator();

            while(iterator.hasNext()) {
                String attrName = (String)iterator.next();
                AttributeType var24 = new AttributeType(attrName);
                var24.open(context);
                String symName;
                if (var24.isMultiVal()) {
                    symName = "";
                    symName = PropertyUtil.getSchemaProperty("attribute_" + attrName);
                    if (ProgramCentralUtil.isNullString(symName)) {
                        symName = attrName;
                    }

                    StringList infoList = this.getInfoList(context, "attribute[" + symName + "]");
                    projAttrMap.put(attrName, infoList);
                } else {
                    symName = (String)attributeMap1.get(attrName);
                    if (symName != null) {
                        projAttrMap.put(attrName, symName);
                    }
                }
            }

            StringList stringList = new StringList();
            stringList.add("physicalid");
            stringList.add("id");
            stringList.add("description");
            stringList.add(SELECT_REFERENCE_DOC_IDS);
            stringList.add(SELECT_REFERENCE_DOC_HAS_FILE);
            stringList.add(REFERENCE_DOC_SELECT_IS_VERSION_OBJECT);
            stringList.add(SELECT_URL_IDS);
            stringList.add("to[" + DomainConstants.RELATIONSHIP_CLASSIFIED_ITEM + "].from.id");
            StringList relList = new StringList();
            relList.add(SELECT_REFERENCE_DOC_IDS);
            relList.add(SELECT_REFERENCE_DOC_HAS_FILE);
            relList.add(REFERENCE_DOC_SELECT_IS_VERSION_OBJECT);
            relList.add(SELECT_URL_IDS);
            Map infos = this.getInfo(context, stringList, relList);
            MapList var94 = new MapList();
            var94.add(infos);
            String description = (String)infos.get("description");
            String physicalid = (String)infos.get("physicalid");
            String id = (String)infos.get("id");
            Object classifiedFromObj = infos.get("to[" + DomainConstants.RELATIONSHIP_CLASSIFIED_ITEM + "].from.id");
            boolean hasAttrMap = null != attributeMap && attributeMap.size() > 0;
            BusinessInterfaceList businessInterfaces = this.getBusinessInterfaces(context, true);
            BusinessInterfaceList businessInterfaces1 = new BusinessInterfaceList();
            businessInterfaces1.addAll(businessInterfaces);
            String biName;
            String biType;
            if (hasAttrMap) {
                projAttrMap.putAll(attributeMap);

                for(int i = 0; i < businessInterfaces1.size(); ++i) {
                    BusinessInterface businessInterface = (BusinessInterface)businessInterfaces1.get(i);
                    biName = businessInterface.getName();
                    biType = MqlUtil.mqlCommand(context, "print interface $1 select $2 dump", new String[]{biName, "type"});
                    String[] var38 = biType.split(",");
                    Set set = (Set)Arrays.stream(var38).collect(Collectors.toSet());
                    if (!set.contains("all") && !set.contains(type)) {
                        businessInterfaces.remove(businessInterface);
                    }
                }
            }

            gscProjectSpace newProjectSpace = new gscProjectSpace();
            String var97 = PropertyUtil.getGlobalRPEValue(context, "CopyColorAttribute");
            if ("false".equalsIgnoreCase(var97)) {
                projAttrMap.remove(ATTRIBUTE_COLOR);
                projAttrMap.remove(ATTRIBUTE_FORMAT_PATTERN);
            }

            newProjectSpace.create(context, type, name, policy, context.getVault().getName(), projAttrMap, description, aTrue, businessInterfaces);
            if (classifiedFromObj != null) {
                this.cloneClassifiedItems(context, classifiedFromObj, newProjectSpace.getObjectId(context));
            }

            if (calendarIds.size() == 1) {
                biName = (String)calendarIds.get(0) + "|DefaultCalendar";
                calendarIds.remove(0);
                calendarIds.add(biName);
            }

            newProjectSpace.addCalendars(context, calendarIds);
            biName = (String)projAttrMap.get("Schedule From");
            biType = (String)projAttrMap.get("Default Constraint Type");
            String var98 = "As Soon As Possible";
            DebugUtil.debug("Cloning successful.");
            HashMap var99 = new HashMap();
            ArrayList var40 = new ArrayList();
            if (projectCloneOptions != null) {
                ProjectCloneOption[] var41 = projectCloneOptions;
                int var42 = projectCloneOptions.length;

                for(int var43 = 0; var43 < var42; ++var43) {
                    ProjectCloneOption var44 = var41[var43];
                    var40.add(var44);
                }
            }

            if (var40.size() > 0 && (var40.contains(ProjectCloneOption.CLONE_FOLDERS) || var40.contains(ProjectCloneOption.CLONE_EVERYTHING))) {
                WorkspaceVault.cloneStructure(context, this, newProjectSpace, var99, false, ContentReplicateOptions.COPY);
            }

            StringList infoList = new StringList(3);
            infoList.add("physicalid");
            infoList.add(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_PROJECT);
            infoList.add("id");
            Map infoMap1 = newProjectSpace.getInfo(context, infoList);
            String var102 = (String)infoMap1.get("id");
            if (var40.contains(ProjectCloneOption.CLONE_WBS_CONSTRAINTS)) {
                var19 = "true";
            }

            if (ProgramCentralUtil.isNotNullString(biType)) {
                var98 = biType;
            } else if ("false".equalsIgnoreCase(var19) && "Project Finish Date".equalsIgnoreCase(biName)) {
                var98 = "As Late As Possible";
            }

            List var103 = (List)infos.get(SELECT_URL_IDS);
            if (var103 != null) {
                String[] var45 = new String[var103.size()];
                var103.toArray(var45);
                URL.copyURLs(context, var45, newProjectSpace);
            }

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
            if ("true".equalsIgnoreCase(var19)) {
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
                if (twoTrue) {
                    taskList = getTemplateTasks(context, this, 0, templBusSelect, (StringList)null, false, false, true, templRelSelect);
                } else {
                    taskList = getTemplateTasks(context, this, 0, templBusSelect, (StringList)null, false, false, false, templRelSelect);
                }
            } finally {
                if (var51) {
                    ContextUtil.popContext(context);
                }

            }

            String tId;
            for(int j = 0; j < taskList.size(); ++j) {
                Map taskMap = (Map)taskList.get(j);
                String taskTransfer = (String)taskMap.get(SELECT_TASK_TRANSFER);
                String questionId = (String)taskMap.get(SELECT_QUESTION_ID);
                StringList taskTransferIds = FrameworkUtil.split(taskTransfer, "\u0007");
                StringList questionIds = FrameworkUtil.split(questionId, "\u0007");

                for(int k = 0; k < taskTransferIds.size() && k < questionIds.size(); ++k) {
                    String qId = (String)questionIds.get(k);
                    tId = (String)taskTransferIds.get(k);
                    if (ProgramCentralUtil.isNotNullString((String)questionResponseMap.get(qId)) && (String)questionResponseMap.get(qId) != "" && ((String)questionResponseMap.get(qId)).equalsIgnoreCase(tId)) {
                        taskMap.put(SELECT_TASK_TRANSFER, tId);
                        taskMap.put(SELECT_QUESTION_ID, qId);
                        break;
                    }
                }

                newList.add(taskMap);
            }

            if (this.isKindOf(context, DomainConstants.TYPE_PROJECT_TEMPLATE) && newProjectSpace.isKindOf(context, DomainConstants.TYPE_PROJECT_SPACE)) {
                DebugUtil.debug("Connecting project to initiating template.");
                String questionResponse = (String)CacheUtil.getCacheObject(context, "QuestionsResponse");
                String questionComment = (String)CacheUtil.getCacheObject(context, "QuestionsResponseComment");
                StringList qrList = FrameworkUtil.split(questionResponse, "|");
                StringList qcList = FrameworkUtil.split(questionComment, "|");
                String questionInfo = "{QuestionInfo:[";

                String questionResponseInfo;
                StringList qriList;
                for(int i = 0; i < qrList.size(); ++i) {
                    questionResponseInfo = (String)qrList.get(i);
                    qriList = FrameworkUtil.split(questionResponseInfo, "=");
                    tId = (String)qcList.get(i);
                    StringList tIdList = FrameworkUtil.split(tId, "=");
                    String qInfoDesc = "{";
                    if (qriList.size() >= 2) {
                        qInfoDesc = qInfoDesc + "questionId : \"" + (String)qriList.get(0) + "\",";
                        qInfoDesc = qInfoDesc + "response : \"" + (String)qriList.get(1) + "\",";
                        if (tIdList.size() >= 2 && ((String)tIdList.get(0)).equalsIgnoreCase((String)qriList.get(0))) {
                            qInfoDesc = qInfoDesc + "comment : \"" + (String)tIdList.get(1) + "\"";
                        }
                    }

                    qInfoDesc = qInfoDesc + "},";
                    questionInfo = questionInfo + qInfoDesc;
                }

                questionInfo = questionInfo.substring(0, questionInfo.length() - 1);
                questionInfo = questionInfo + "]}";
                DomainRelationship.connect(context, var102, RELATIONSHIP_INITIATED_TEMPLATE_PROJECT, this.getObjectId(context), true);
                questionResponseInfo = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump $3", new String[]{this.getObjectId(context), "relationship[Initiated Template Project].id", "|"});
                qriList = FrameworkUtil.split(questionResponseInfo, "|");
                tId = (String)qriList.get(qriList.size() - 1);
                DomainRelationship domainRelationship = new DomainRelationship(tId);
                domainRelationship.setAttributeValue(context, "ProjectQuestionResponse", questionInfo);
                HashMap selOptions = new HashMap();
                selOptions.put("copyTaskConstraint", var19);
                HashMap infoMap = new HashMap();
                infoMap.put("projectTemplateId", physicalid);
                infoMap.put("projectId", var102);
                infoMap.put("TaskMapList", newList);
                infoMap.put("answerList", questionResponseMap);
                infoMap.put("defaultTaskConstraintType", var98);
                infoMap.put("selectedOption", selOptions);
                String[] var64 = JPO.packArgs(infoMap);
                JPO.invoke(context, "emxProjectSpace", (String[])null, "createProjectFromProjectTemplate", var64);
                String palPhysicalId = (String)infoMap1.get(ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_PROJECT);
                String physicalid1 = (String)infoMap1.get("physicalid");
                ProjectSequence projectSequence = new ProjectSequence(context, palPhysicalId);
                Map sequenceData = projectSequence.getSequenceData(context);
                Set seqSet = sequenceData.keySet();
                seqSet.remove(physicalid1);
                String[] seqArry = new String[seqSet.size()];
                seqSet.toArray(seqArry);
                MapList var71 = new MapList();
                int var72 = 0;
                String[] seqArry1 = seqArry;
                int seqLen = seqArry.length;

                for(int k = 0; k < seqLen; ++k) {
                    String seqId = seqArry1[k];
                    HashMap seqInfo = new HashMap();
                    String var78 = (String)((Dataobject)sequenceData.get(seqArry[var72])).getDataelements().get("seqId");
                    seqInfo.put("seqId", var78);
                    seqInfo.put("id", seqArry[var72++]);
                    var71.add(seqInfo);
                }

                var71.sortStructure("seqId", "ascending", "integer");
                int var115 = 0;
                Iterator var116 = newList.iterator();

                String[] var117;
                for(var117 = new String[newList.size()]; var116.hasNext(); ++var115) {
                    Map var76 = (Map)var116.next();
                    String var119 = (String)var76.get("id");
                    var117[var115] = var119;
                    com.matrixone.apps.common.Task var121 = new com.matrixone.apps.common.Task((String)((Map)var71.get(var115)).get("id"));
                    String var79 = (String)var76.get(ProgramCentralConstants.SELECT_CALENDAR_ID);
                    if (ProgramCentralUtil.isNotNullString(var79)) {
                        var121.addRelatedObject(context, new RelationshipType(ProgramCentralConstants.RELATIONSHIP_CALENDAR), false, var79);
                    }

                    var99.put(var119, var121);
                }

                HashMap hashMap1 = new HashMap();
                hashMap1.put(id, newProjectSpace);
                this.cloneTaskDeliverablesandReferenceDocuments(context, var94, hashMap1, var51, selectedOptionForReferenceDocument);
                this.cloneTaskDeliverablesandReferenceDocuments(context, newList, var99, var51, selectedOptionForReferenceDocument);
                this.connectSimulationProcessAndTemplate(context, newList, var99);
                newList.stream().forEach((var3x) -> {
                    Object var214 = ((Map)var3x).get("to[" + DomainConstants.RELATIONSHIP_CLASSIFIED_ITEM + "].from.id");
                    if (var214 != null) {
                        try {
                            String var215 = (String)((Map)var3x).get("id");
                            DomainObject var216 = (DomainObject)var99.get(var215);
                            String var217 = var216.getId(context);
                            String var218 = "print bus $1 select $2 dump";
                            String var219 = MqlUtil.mqlCommand(context, false, false, var218, false, new String[]{var217, "id"});
                            this.cloneClassifiedItems(context, var214, var219);
                        } catch (Exception var210) {
                            var210.printStackTrace();
                        }
                    }

                });
                this.addInterfacesColorAndMandatoryFlagToTaskList(context, var117, var99);
                LinkedList gateList = new LinkedList();
                newList.parallelStream().forEach((var1x) -> {
                    String var222 = (String)((Map)var1x).get("id");
                    String var223 = (String)((Map)var1x).get("type");
                    if (ProgramCentralConstants.TYPE_GATE.equalsIgnoreCase(var222)) {
                        gateList.add(var222);
                    }

                });
                CheckList checkList = new CheckList();
                Task task = new Task();
                int gateCount = gateList.size();

                for(int k = 0; k < gateCount; ++k) {
                    Class checkListClass = CheckList.class;
                    synchronized(CheckList.class) {
                        task.addEventListener(checkList);
                        HashMap notiMap = new HashMap();
                        notiMap.put("sourceId", (String)gateList.get(k));
                        String var84 = ((com.matrixone.apps.common.Task)var99.get((String)gateList.get(k))).getObjectId();
                        notiMap.put("targetId", var84);
                        this.notifyEventListeners(context, 1, notiMap);
                        task.removeEventListener(checkList);
                    }
                }

                MapList var124 = new MapList();
                LinkedList var125 = new LinkedList();
                newList.parallelStream().forEach((var2x) -> {
                    HashMap var233 = new HashMap();
                    StringList var234 = (StringList)((Map)var2x).get("from[" + DomainConstants.RELATIONSHIP_LINK_URL + "].to.id");
                    if (var234 != null) {
                        String var235 = (String)((Map)var2x).get("id");
                        var233.put("sourceTaskId", var235);
                        var233.put("urlData", var234);
                        var125.add(var235);
                        var124.add(var233);
                    }

                });
                var124.forEach((var3x) -> {
                    String var314 = (String)((Map)var3x).get("sourceTaskId");
                    StringList var315 = (StringList)((Map)var3x).get("urlData");
                    TaskHolder var316 = (TaskHolder)var99.get(var314);

                    try {
                        this.cloneUrlsForPT(context, var315, var316, DomainConstants.RELATIONSHIP_LINK_URL);
                    } catch (Exception var328) {
                        var328.printStackTrace();
                    }

                });
                this.copyRelatedItemsAndExtendedMemberRoles(context, newProjectSpace);
                ContextUtil.commitTransaction(context);
            }

            return newProjectSpace;
        } catch (Exception var91) {
            ContextUtil.abortTransaction(context);
            throw new FrameworkException(var91);
        }
    }
//                newProjectSpace = (ProjectSpace)projectSpace.cloneFromTemplate(context, type, name, policy, vault, questionResponseMap, "", isTemplateTaskAutoName, attributeMap, true, selectedOptionForReferenceDocument, calendarIds, isCopyFolderData ? ProjectSpace.ProjectCloneOption.CLONE_FOLDERS : ProjectSpace.ProjectCloneOption.CLONE_WBS, ProjectSpace.ProjectCloneOption.CLONE_WBS_CONSTRAINTS);
    public DomainObject cloneFromTemplate(Context context, String type, String name, String policy, String vault, Map questionResponseMap, String blank, boolean isTemplateTaskAutoName, Map attributeMap, boolean aTrue, ContentReplicateOptions selectedOptionForReferenceDocument, StringList calendarIds, ProjectCloneOption... projectCloneOptions) throws FrameworkException {
        return this.cloneFromTemplate(context, type, name, policy, vault, questionResponseMap, blank, isTemplateTaskAutoName, attributeMap, aTrue, true, selectedOptionForReferenceDocument, calendarIds, projectCloneOptions);
    }

    public gscProjectSpace cloneTemplateToCreateProject(Context context, String selectedTemplateId, Map<String, String> basicProjectInfo, Map<String, String> relatedProjectInfo, Map<String, String> attributeMap, Map questionResponseMap, boolean isTemplateTaskAutoName, boolean isCopyFolderData, boolean isCopyFinancialData, boolean keepSourceConstraints, ContentReplicateOptions selectedOptionForReferenceDocument, StringList calendarIds) throws Exception {
        gscProjectSpace gscProjectSpace = (gscProjectSpace)DomainObject.newInstance(context, ProgramCentralConstants.TYPE_PROJECT_SPACE, "Program");
        PropertyUtil.setGlobalRPEValue(context, "CREATING_PROJECT_FROM_PT", "true");

        try {
            ContextUtil.startTransaction(context, true);
            String name = (String)basicProjectInfo.get("name");
            String type = (String)basicProjectInfo.get("type");
            String policy = (String)basicProjectInfo.get("policy");
            String vault = (String)basicProjectInfo.get("vault");
            String discription = (String)basicProjectInfo.get("discription");
            gscProjectSpace.setId(selectedTemplateId);

            gscProjectSpace newGscProjectSpace = new gscProjectSpace();
            if (keepSourceConstraints) {
                newGscProjectSpace = (gscProjectSpace) gscProjectSpace.cloneFromTemplate(context, type, name, policy, vault, questionResponseMap, "", isTemplateTaskAutoName, attributeMap, true, selectedOptionForReferenceDocument, calendarIds, isCopyFolderData ? ProjectCloneOption.CLONE_FOLDERS : ProjectCloneOption.CLONE_WBS, ProjectCloneOption.CLONE_WBS_CONSTRAINTS);
            } else {
                newGscProjectSpace = (gscProjectSpace) gscProjectSpace.cloneFromTemplate(context, type, name, policy, vault, questionResponseMap, "", isTemplateTaskAutoName, attributeMap, true, selectedOptionForReferenceDocument, calendarIds, isCopyFolderData ? ProjectCloneOption.CLONE_FOLDERS : ProjectCloneOption.CLONE_WBS);
            }

            newGscProjectSpace.setAttributeValues(context, attributeMap);
            String ganttConfigFromProject = gscProjectSpace.getInfo(context, ProgramCentralConstants.SELECT_GANTT_CONFIG_FROM_PROJECT);
            StringList infoList = new StringList(2);
            infoList.add("id");
            infoList.add(SELECT_PROJECT_ACCESS_LIST_ID_FOR_PROJECT);
            Map infoMap = newGscProjectSpace.getInfo(context, infoList);
            String accessListId = (String)infoMap.get("id");
            String palObjId = (String)infoMap.get(ProgramCentralConstants.SELECT_PAL_OBJECTID_FROM_PROJECT);
            DomainObject palObj = DomainObject.newInstance(context, palObjId);
            palObj.setAttributeValue(context, ProgramCentralConstants.ATTRIBUTE_GANTT_CONFIG, ganttConfigFromProject);
            relatedProjectInfo.put("sourceTemplateId", selectedTemplateId);
            relatedProjectInfo.put("objectId", accessListId);
            updateProjectRelatedInfo(context, relatedProjectInfo);
            com.matrixone.apps.common.Task task = new com.matrixone.apps.common.Task(accessListId);
            task.rollupAndSave(context);
            gscProjectSpace.setId(selectedTemplateId);
            if (isCopyFinancialData) {
                Financials.cloneStructure(context, gscProjectSpace, newGscProjectSpace);
            }

            ContextUtil.commitTransaction(context);
            return newGscProjectSpace;
        } catch (Exception var27) {
            ContextUtil.abortTransaction(context);
            var27.printStackTrace();
            throw var27;
        }
    }

    public static MapList getTemplateTasks(Context var0, TaskHolder var1, int var2, StringList var3, StringList var4, boolean var5, boolean var6, boolean var7, Collection var8) throws FrameworkException {
        MapList var9 = new MapList();
        DomainObject var10 = (DomainObject)var1;
        String var11 = "*";
        if (!var6) {
            var11 = DomainObject.TYPE_TASK_MANAGEMENT;
        }

        String var12 = "";
        if (!var7) {
            var12 = "to[" + PropertyUtil.getSchemaProperty("relationship_RoadmapTask") + "] == 'False'";
        }

        if (var4 == null) {
            var4 = new StringList(1);
        }

        String var13 = RELATIONSHIP_SUBTASK;
        if (var5) {
            var13 = var13 + "," + RELATIONSHIP_DELETED_SUBTASK;
        }

        StringList var14 = new StringList(3);
        var14.add("id");

        try {
            MapList var15 = var10.getRelatedObjects(var0, var13, var11, false, true, (short)var2, new StringList(var14), var4, (String)null, var12, (String)null, (String)null, (Map)null);
            int var16 = var15.size();
            StringList var17 = new StringList(var16);
            String[] var18 = new String[var16];
            var15.forEach((var1x) -> {
                var17.add((String)((Map)var1x).get("id"));
            });
            var17.toArray(var18);
            BusinessObjectWithSelectList var19 = ProgramCentralUtil.getObjectWithSelectList(var0, var18, var3, false);
            var9 = FrameworkUtil.toMapList(var19, var8);
            String var20 = "print bus $1 select $2 $3 dump $4";
            String var21 = MqlUtil.mqlCommand(var0, true, true, var20, true, new String[]{var10.getId(var0), ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_PROJECT, ProgramCentralConstants.SELECT_PAL_PHYSICALID_FROM_TASK, "|"});
            if (ProgramCentralUtil.isNullString(var21)) {
                return var9;
            }

            ProjectSequence var22 = new ProjectSequence(var0, var21);
            Map<String, Dataobject> var23 = var22.getSequenceData(var0);
            Stream var24 = var9.stream();
            var24.forEach((var1x) -> {
                Dataobject var422 = (Dataobject)var23.get((String)((Map)var1x).get("physicalid"));
                if (var422 != null) {
                    ((Map)var1x).put(SubtaskRelationship.SELECT_SEQUENCE_ORDER, (String)var422.getDataelements().get("seqId"));
                    ((Map)var1x).put(SubtaskRelationship.SELECT_TASK_WBS, (String)var422.getDataelements().get("wbsId"));
                }

            });
            var9.sortStructure(SubtaskRelationship.SELECT_SEQUENCE_ORDER, "ascending", "integer");
        } catch (Exception var25) {
            var25.printStackTrace();
        }

        return var9;
    }

    public static StringList moveConstraintDate(Context var0, Map var1) throws Exception {
        new HashMap();
        StringList var3 = new StringList();

        try {
            TimeZone var4 = TimeZone.getTimeZone(var0.getSession().getTimezone());
            double var5 = -1.0 * (double)var4.getRawOffset();
            double var7 = new Double(var5 / 3600000.0);
            Locale var9 = var0.getLocale();
            if (var1 != null && !var1.isEmpty()) {
                Set var10 = var1.keySet();
                Object var11 = null;
                Iterator var12 = var10.iterator();

                while(true) {
                    String var13;
                    SimpleDateFormat var19;
                    int var24;
                    do {
                        if (!var12.hasNext()) {
                            return var3;
                        }

                        var13 = (String)var12.next();
                        Map var14 = (Map)var1.get(var13);
                        String var15 = (String)var14.get("oldConstraintDate");
                        String var16 = (String)var14.get("newConstraintDate");
                        var16 = eMatrixDateFormat.getFormattedInputDate(var0, var16, var7, var9);
                        int var17 = eMatrixDateFormat.getEMatrixDisplayDateFormat();
                        DateFormat.getDateTimeInstance(var17, var17, var9);
                        var19 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
                        Date var20 = var19.parse(var16);
                        Date var21 = var19.parse(var15);
                        GregorianCalendar var22 = new GregorianCalendar();
                        var22.setTime(var21);
                        var22.set(10, 0);
                        var22.set(12, 0);
                        var22.set(13, 0);
                        var22.set(14, 0);
                        GregorianCalendar var23 = new GregorianCalendar();
                        var23.setTime(var20);
                        var23.set(10, 0);
                        var23.set(12, 0);
                        var23.set(13, 0);
                        var23.set(14, 0);
                        var24 = (int)((var23.getTime().getTime() - var22.getTime().getTime()) / 86400000L);
                    } while(!ProgramCentralUtil.isNotNullString(var13));

                    StringList var25 = new StringList();
                    var25.add(ProgramCentralConstants.SELECT_PAL_OBJECTID_FROM_PROJECT);
                    MapList var26 = DomainObject.getInfo(var0, new String[]{var13}, var25);
                    Map var27 = (Map)var26.get(0);
                    String var28 = (String)var27.get(ProgramCentralConstants.SELECT_PAL_OBJECTID_FROM_PROJECT);
                    DomainObject var29 = DomainObject.newInstance(var0, var28);
                    boolean var30 = false;
                    boolean var31 = true;
                    String var32 = "(current ~~ " + ProgramCentralConstants.STATE_PROJECT_TASK_CREATE + " || current ~~ " + ProgramCentralConstants.STATE_PROJECT_TASK_ASSIGN + ") && " + ProgramCentralConstants.SELECT_ATTRIBUTE_CONSTRAINT_DATE + " != ''";
                    String var33 = "";
                    StringList var34 = new StringList();
                    var34.add(ProgramCentralConstants.SELECT_ATTRIBUTE_CONSTRAINT_DATE);
                    var34.add(ProjectManagement.SELECT_TASK_CONSTRAINT_TYPE);
                    var34.add("id");
                    MapList var35 = var29.getRelatedObjects(var0, ProgramCentralConstants.RELATIONSHIP_PROJECT_ACCESS_KEY, ProgramCentralConstants.TYPE_TASK_MANAGEMENT, var34, (StringList)null, var30, var31, (short)1, var32, var33, (short)0, true, true, (short)1000, (Pattern)null, (Pattern)null, (Map)null, "");
                    int var36 = var35.size();

                    for(int var37 = 0; var37 < var36; ++var37) {
                        Map var38 = (Map)var35.get(var37);
                        String var39 = (String)var38.get(ProgramCentralConstants.SELECT_ATTRIBUTE_CONSTRAINT_DATE);
                        String var40 = (String)var38.get("id");
                        String var41 = (String)var38.get(ProjectManagement.SELECT_TASK_CONSTRAINT_TYPE);
                        if (ProgramCentralUtil.isNotNullString(var39)) {
                            Date var42 = var19.parse(var39);
                            GregorianCalendar var43 = new GregorianCalendar();
                            var43.setTime(var42);
                            var43.add(5, var24);
                            var42 = var43.getTime();
                            var39 = var19.format(var42);
                            StringBuilder var44 = new StringBuilder();
                            var44.append("modify bus $1 $2 $3");
                            ArrayList var45 = new ArrayList();
                            var45.add(var40);
                            var45.add(DomainConstants.ATTRIBUTE_TASK_CONSTRAINT_DATE);
                            var45.add(var39);
                            if ((ProgramCentralUtil.isNotNullString(var41) || ProgramCentralUtil.isNotNullString(var39)) && var45.size() > 1) {
                                String[] var46 = new String[var45.size()];
                                var45.toArray(var46);
                                MqlUtil.mqlCommand(var0, var44.toString(), var45);
                                var3.add(var40);
                            }
                        }
                    }
                }
            } else {
                return var3;
            }
        } catch (Exception var47) {
            var47.printStackTrace();
            throw var47;
        }
    }

    /** @deprecated */
    public static boolean hasCircularDependency(Context var0, MapList var1) {
        return false;
    }

    public static boolean hasCircularDependency(Context var0, MapList var1, Map var2) {
        try {
            hasCircularDependency = false;
            HashMap var3 = new HashMap();
            String var6;
            Map var7;
            if (var2 != null && !var2.isEmpty()) {
                Set var4 = var2.keySet();
                Iterator var5 = var4.iterator();

                while(var5.hasNext()) {
                    var6 = (String)var5.next();
                    var7 = (Map)var2.get(var6);
                    LinkedHashMap var8 = new LinkedHashMap();
                    var7.forEach((var1x, var2x) -> {
                        var8.put(var2x, var1x);
                    });
                    var3.put(var6, var8);
                }
            }

            MapList var20 = new MapList();
            var20.addAll(var1);
            HashMap var21 = new HashMap();
            var21.put("0", new HashMap());
            Iterator var22 = var1.iterator();

            while(true) {
                while(var22.hasNext()) {
                    var7 = (Map)var22.next();
                    String var23 = (String)var7.get("Level");
                    String var9 = (String)var7.get("ParentProjectLevel");
                    if (ProgramCentralUtil.isNullString(var9)) {
                        var9 = "0";
                    }

                    Map var10 = (Map)var2.get(var9);
                    if ("0".equals(var23)) {
                        var21.put("0", var7);
                    } else {
                        String var11 = (String)var7.get("Dependencies");
                        StringList var12 = new StringList();
                        int var14;
                        String var15;
                        if (var11 != null && !var11.isEmpty()) {
                            StringList var13 = FrameworkUtil.split(var11, ",");
                            var14 = var13.size();
                            var15 = EnoviaResourceBundle.getProperty(var0, "ProgramCentral", "emxProgramCentral.Error.ProjectAsPredecessorAlert", var0.getSession().getLanguage());

                            for(int var16 = 0; var16 < var14; ++var16) {
                                String var17 = (String)var13.get(var16);
                                var17 = (String)FrameworkUtil.split(var17, ":").get(0);
                                String var18 = (String)var10.get(var17);
                                if (var2 != null && var2.containsKey(var18)) {
                                    var7.put("Dependencies", "Error:" + var15);
                                    hasCircularDependency = true;
                                    break;
                                }

                                if (ProgramCentralUtil.isNotNullString(var18) && !var12.contains(var18) && !"Error".equalsIgnoreCase(var17)) {
                                    var12.add(var18);
                                }
                            }
                        }

                        Map var24 = (Map)var21.get("0");
                        var14 = var23.lastIndexOf(".");
                        if (var14 != -1) {
                            var15 = var23.substring(0, var14);
                            var24 = (Map)var21.get(var15);
                        }

                        new StringList();
                        StringList var26;
                        if (var24.get("Children") != null) {
                            StringList var25 = (StringList)var24.get("Children");
                            var25.add(var23);
                        } else {
                            var26 = new StringList();
                            var26.add(var23);
                            var24.put("Children", var26);
                        }

                        var26 = new StringList();
                        if (var24.get("predecessorsWBS") != null) {
                            var26 = (StringList)var24.get("predecessorsWBS");
                        }

                        var12.addAll(var26);
                        var7.put("predecessorsWBS", var12);
                        var21.put(var23, var7);
                    }
                }

                var6 = String.valueOf(System.currentTimeMillis());
                processTask(var0, (Map)var21.get("0"), var21, var3, "Stamp", var6);
                break;
            }
        } catch (Exception var19) {
            var19.printStackTrace();
        }

        return hasCircularDependency;
    }

    private static void processTask(Context var0, Map var1, Map var2, Map var3, String var4, String var5) throws Exception {
        if (!var5.equals(var1.get(var4))) {
            boolean var6 = false;
            StringList var7 = (StringList)var1.get("Children");
            String var12;
            Map var13;
            String var14;
            String[] var15;
            String var16;
            if (var7 != null && !var7.isEmpty()) {
                for(int var17 = 0; var17 < var7.size(); ++var17) {
                    String var18 = (String)var7.get(var17);
                    Map var19 = (Map)var2.get(var18);
                    if ("pending".equals(var19.get(var4))) {
                        String var20 = (String)var1.get("Level");
                        var12 = (String)var1.get("ParentProjectLevel");
                        var13 = (Map)var3.get(var12);
                        var14 = (String)var13.get(var20);
                        var15 = new String[]{var14};
                        var16 = MessageUtil.getMessage(var0, (String)null, "emxProgramCentral.Import.CircularDependency", var15, (String[])null, var0.getSession().getLanguage(), "emxProgramCentralStringResource");
                        var16 = FrameworkUtil.findAndReplace(var16, "{0}", var14);
                        var19.put("Dependencies", "Error:" + var16);
                        hasCircularDependency = true;
                        var6 = true;
                    } else {
                        processTask(var0, var19, var2, var3, var4, var5);
                    }
                }
            } else {
                StringList var8 = (StringList)var1.get("predecessorsWBS");
                if (var8 != null && !var8.isEmpty()) {
                    var1.put(var4, "pending");

                    for(int var9 = 0; var9 < var8.size(); ++var9) {
                        String var10 = (String)var8.get(var9);
                        Map var11 = (Map)var2.get(var10);
                        var12 = (String)var1.get("ParentProjectLevel");
                        var13 = (Map)var3.get(var12);
                        var14 = (String)var13.get(var10);
                        if ("pending".equals(var11.get(var4))) {
                            var15 = new String[]{var14};
                            var16 = MessageUtil.getMessage(var0, (String)null, "emxProgramCentral.Import.CircularDependency", var15, (String[])null, var0.getSession().getLanguage(), "emxProgramCentralStringResource");
                            var16 = FrameworkUtil.findAndReplace(var16, "{0}", var14);
                            var1.put("Dependencies", "Error:" + var16);
                            hasCircularDependency = true;
                            var6 = true;
                        } else {
                            processTask(var0, var11, var2, var3, var4, var5);
                        }
                    }

                    var1.remove(var4);
                }
            }

            var1.put(var4, var5);
        }
    }

    public static Datacollections promoteSuccessor(Context var0, String var1) {
        StringList var2 = new StringList();
        Datacollection var3 = new Datacollection();
        Datacollections var4 = new Datacollections();

        try {
            int var5 = getPromoteReadyTasksDays(var0);
            boolean var6 = checkIfAutoPromoteDependentTaskEnabled(var0);
            if (ProgramCentralUtil.isNotNullString(var1)) {
                StringList var7 = new StringList();
                var7.add(SELECT_PROJECT_ACCESS_LIST_ID_FOR_TASK);
                var7.add(SELECT_PROJECT_ACCESS_LIST_ID_FOR_PROJECT);
                gscProjectSpace var8 = new gscProjectSpace(var1);
                Map var9 = var8.getInfo(var0, var7);
                String var10 = (String)var9.get(SELECT_PROJECT_ACCESS_LIST_ID_FOR_PROJECT);
                if (ProgramCentralUtil.isNullString(var10)) {
                    var10 = (String)var9.get(SELECT_PROJECT_ACCESS_LIST_ID_FOR_TASK);
                }

                if (ProgramCentralUtil.isNotNullString(var10)) {
                    SimpleDateFormat var11 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
                    Calendar var12 = Calendar.getInstance();
                    var12.add(5, var5);
                    Date var13 = var12.getTime();
                    var13 = Helper.cleanTime(var13);
                    String var14 = var11.format(var13);
                    DomainObject var15 = DomainObject.newInstance(var0, var10);
                    String var16 = ProgramCentralConstants.RELATIONSHIP_PROJECT_ACCESS_KEY;
                    String var17 = ProgramCentralConstants.TYPE_TASK_MANAGEMENT;
                    StringList var18 = new StringList();
                    var18.add("id");
                    var18.add("physicalid");
                    var18.add(ProgramCentralConstants.SELECT_DEPENDENCY_TYPE);
                    var18.add("from[" + DomainConstants.RELATIONSHIP_DEPENDENCY + "].to.current");
                    var18.add(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE);
                    String var19 = "current ~~ " + ProgramCentralConstants.STATE_PROJECT_TASK_CREATE + " && policy != '" + ProgramCentralConstants.POLICY_PROJECT_REVIEW + "'";
                    if (var5 > 0) {
                        var19 = var19 + " && ( from[" + ProgramCentralConstants.RELATIONSHIP_DEPENDENCY + "] ~~ True || " + ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE + " < '" + var14 + "' )";
                    } else {
                        var19 = var19 + " && from[" + ProgramCentralConstants.RELATIONSHIP_DEPENDENCY + "] ~~ True";
                    }

                    new MapList();
                    MapList var20 = var15.getRelatedObjects(var0, var16, var17, var18, (StringList)null, false, true, (short)1, var19, "", (short)0, true, true, (short)1000, (Pattern)null, (Pattern)null, (Map)null, "");
                    if (var6) {
                        var19 = "current ~~ " + ProgramCentralConstants.STATE_PROJECT_TASK_CREATE + " && policy != '" + ProgramCentralConstants.POLICY_PROJECT_REVIEW + "'&& to[Project Access Key].from.id != " + var10;
                        DomainObject var22 = DomainObject.newInstance(var0, var1);
                        MapList var23 = var22.getRelatedObjects(var0, RELATIONSHIP_DEPENDENCY, "*", var18, (StringList)null, true, false, (short)1, var19, "", (short)0, true, true, (short)1000, (Pattern)null, (Pattern)null, (Map)null, "");
                        var20.addAll(var23);
                    }

                    int var21 = var20.size();
                    HashSet var41 = new HashSet();
                    HashSet var42 = new HashSet();
                    HashSet var24 = new HashSet();
                    boolean var25 = false;
                    int var26 = 0;

                    label115:
                    while(true) {
                        String var28;
                        String var29;
                        if (var26 >= var21) {
                            DomainObject var43 = DomainObject.newInstance(var0);
                            Iterator var44 = var41.iterator();

                            while(true) {
                                if (!var44.hasNext()) {
                                    break label115;
                                }

                                var28 = "";
                                var29 = "";
                                String var45 = (String)var44.next();
                                String[] var46 = var45.split("-");

                                try {
                                    Dataobject var47 = new Dataobject();
                                    var28 = var46[0];
                                    var29 = var46[1];
                                    var43.setId(var28);
                                    var47.setId(var29);
                                    var43.setState(var0, ProgramCentralConstants.STATE_PROJECT_TASK_ASSIGN);
                                    DataelementMapAdapter.setDataelementValue(var47, "state", ProgramCentralConstants.STATE_PROJECT_TASK_ASSIGN);
                                    var2.add(var28);
                                    var3.getDataobjects().add(var47);
                                    String[] var48;
                                    if (var42.contains(var28)) {
                                        var48 = new String[]{"TASK_START1", "false", var28};
                                        JPO.invoke(var0, "emxProjectSpaceBase", (String[])null, "promoteSuccessor3DNotification", var48);
                                    } else if (var24.contains(var28)) {
                                        var48 = new String[]{"TASK_FINISH_2", "false", var28};
                                        JPO.invoke(var0, "emxProjectSpaceBase", (String[])null, "promoteSuccessor3DNotification", var48);
                                    }
                                } catch (Exception var39) {
                                    var39.printStackTrace();
                                    System.out.println("Successor promotion failed for task " + var28 + " due to :" + var39.getMessage());
                                }
                            }
                        }

                        Map var27 = (Map)var20.get(var26);
                        var28 = (String)var27.get("id");
                        var29 = (String)var27.get("physicalid");
                        Object var30 = var27.get(ProgramCentralConstants.SELECT_DEPENDENCY_TYPE);
                        Object var31 = var27.get("from[" + DomainConstants.RELATIONSHIP_DEPENDENCY + "].to.current");
                        String var32 = (String)var27.get(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE);
                        StringList var33 = new StringList();
                        StringList var34 = new StringList();
                        if (var30 != null && var31 != null && var30 instanceof String) {
                            var33.add((String)var30);
                            var34.add((String)var31);
                        } else if (var30 != null && var31 != null && var30 instanceof StringList) {
                            var33 = (StringList)var30;
                            var34 = (StringList)var31;
                        }

                        int var35 = var33.size();

                        for(int var36 = 0; var36 < var35; ++var36) {
                            String var37 = (String)var33.get(var36);
                            String var38 = (String)var34.get(var36);
                            if (("SS".equalsIgnoreCase(var37) || "SF".equalsIgnoreCase(var37)) && ("Active".equalsIgnoreCase(var38) || "Review".equalsIgnoreCase(var38) || "Complete".equalsIgnoreCase(var38))) {
                                var41.add(var28 + "-" + var29);
                                var42.add(var28);
                                break;
                            }

                            if (("FF".equalsIgnoreCase(var37) || "FS".equalsIgnoreCase(var37)) && "Complete".equalsIgnoreCase(var38)) {
                                var41.add(var28 + "-" + var29);
                                var24.add(var28);
                                break;
                            }
                        }

                        if (var5 > 0) {
                            Date var49 = var11.parse(var32);
                            if (var49.before(var13)) {
                                var41.add(var28 + "-" + var29);
                            }
                        }

                        ++var26;
                    }
                }
            }

            var4.getDatacollections().add(var3);
        } catch (Exception var40) {
            var40.printStackTrace();
        }

        return var4;
    }

    public void connectSimulationProcessAndTemplate(Context var1, MapList var2, Map var3) throws Exception {
        try {
            if (var2 != null && var2.size() > 0) {
                if (var3 == null) {
                    var3 = new HashMap();
                }

                Iterator var4 = var2.iterator();
                Object var5 = new ArrayList(1);

                while(true) {
                    com.matrixone.apps.common.Task var8;
                    Object var9;
                    do {
                        Map var6;
                        do {
                            if (!var4.hasNext()) {
                                return;
                            }

                            var6 = (Map)var4.next();
                            String var7 = (String)var6.get("id");
                            var8 = (com.matrixone.apps.common.Task)((Map)var3).get(var7);
                        } while(var8 == null);

                        var9 = var6.get("from[Referenced Simulations].to.id");
                    } while(var9 == null);

                    if (var9 instanceof String) {
                        ((List)var5).add(var9);
                    } else {
                        var5 = (List)var9;
                    }

                    int var10 = ((List)var5).size();

                    for(int var11 = 0; var11 < var10; ++var11) {
                        String var12 = (String)((List)var5).get(var11);
                        if (var12 != null && !"".equals(var12)) {
                            DomainObject var13 = new DomainObject();
                            var13.setId(var12);
                            DomainRelationship.connect(var1, var8, "Referenced Simulations", var13);
                        }
                    }
                }
            }
        } catch (Exception var15) {
            var15.printStackTrace();
        }

    }

    public String getDocumentRelationshipType() {
        return RELATIONSHIP_REFERENCE_DOCUMENT;
    }

    public static boolean checkIfAutoPromoteDependentTaskEnabled(Context var0) throws Exception {
        boolean var1 = false;

        try {
            String var2 = "print bus $1 $2 $3 select $4 dump $5";
            String var3 = MqlUtil.mqlCommand(var0, var2, new String[]{"eService Trigger Program Parameters", "PolicyProjectTaskStateAssignPromoteAction", "ActiveNotificationDependentTaskAction", "current", "value"});
            String var4 = MqlUtil.mqlCommand(var0, var2, new String[]{"eService Trigger Program Parameters", "PolicyProjectTaskStateReviewPromoteAction", "CompletionNotificationDependentTaskAction", "current", "value"});
            if ("ACTIVE".equalsIgnoreCase(var3) && "ACTIVE".equalsIgnoreCase(var4)) {
                var1 = true;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return var1;
    }

    public static int getPromoteReadyTasksDays(Context var0) throws Exception {
        int var1 = 0;

        try {
            String var2 = "print expression $1 select $2 dump";
            String var3 = MqlUtil.mqlCommand(var0, var2, new String[]{"DPM_ReadyTaskDays", "value"});
            var1 = Integer.parseInt(var3);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return var1;
    }

    static {
        SELECT_ESCALATION_DATE = getAttributeSelect(ATTRIBUTE_ESCALATION_DATE);
        SELECT_RECURRENCE_INTERVAL = getAttributeSelect(ATTRIBUTE_RECURRENCE_INTERVAL);
        SELECT_SEND_REMINDER = getAttributeSelect(ATTRIBUTE_SEND_REMINDER);
        ATTRIBUTE_TASK_RECURRENCE_INTERVAL = PropertyUtil.getSchemaProperty("attribute_TaskAssignmentReminderRecurrenceInterval");
        ATTRIBUTE_TASK_SEND_REMINDER = PropertyUtil.getSchemaProperty("attribute_TaskAssignmentReminderDuration");
        SELECT_TASK_RECURRENCE_INTERVAL = getAttributeSelect(ATTRIBUTE_TASK_RECURRENCE_INTERVAL);
        SELECT_TASK_SEND_REMINDER = getAttributeSelect(ATTRIBUTE_TASK_SEND_REMINDER);
        ATTRIBUTE_BOOKMARK_PHYSICAL_ID = PropertyUtil.getSchemaProperty("attribute_BookmarkPhysicalId");
        INTERFACE_DPMBOOKMARKROOT = PropertyUtil.getSchemaProperty("interface_DPMBookmarkRoot");
        SELECT_PROJECT_FUNCTION = getAttributeSelect(ATTRIBUTE_PROJECT_FUNCTION);
        SELECT_PROJECT_TYPE = getAttributeSelect(ATTRIBUTE_PROJECT_TYPE);
        SELECT_PROJECT_VISIBILITY = getAttributeSelect(ATTRIBUTE_PROJECT_VISIBILITY);
        SELECT_SCOPE = getAttributeSelect(ATTRIBUTE_SCOPE);
        SELECT_BUSINESS_UNIT_ID = "to[" + RELATIONSHIP_BUSINESS_UNIT_PROJECT + "].from.id";
        SELECT_BUSINESS_UNIT_NAME = "to[" + RELATIONSHIP_BUSINESS_UNIT_PROJECT + "].from.name";
        SELECT_COMPANY_ID = "to[" + RELATIONSHIP_COMPANY_PROJECT + "].from.id";
        SELECT_COMPANY_NAME = "to[" + RELATIONSHIP_COMPANY_PROJECT + "].from.name";
        SELECT_PROGRAM_ID = "to[" + RELATIONSHIP_PROGRAM_PROJECT + "].from.id";
        SELECT_PROGRAM_NAME = "to[" + RELATIONSHIP_PROGRAM_PROJECT + "].from.name";
        SELECT_MEMBERS_ID = "from[" + RELATIONSHIP_MEMBER + "].to.id";
        SELECT_MEMBERS_PROJECT_ACCESS = "from[" + RELATIONSHIP_MEMBER + "]." + MemberRelationship.SELECT_PROJECT_ACCESS;
        SELECT_MEMBERS_PROJECT_ROLE = "from[" + RELATIONSHIP_MEMBER + "]." + MemberRelationship.SELECT_PROJECT_ROLE;
        SELECT_BASELINE_CURRENT_END_DATE = getAttributeSelect(ATTRIBUTE_BASELINE_CURRENT_END_DATE);
        SELECT_BASELINE_CURRENT_START_DATE = getAttributeSelect(ATTRIBUTE_BASELINE_CURRENT_START_DATE);
        SELECT_BASELINE_INITIAL_END_DATE = getAttributeSelect(ATTRIBUTE_BASELINE_INITIAL_END_DATE);
        SELECT_BASELINE_INITIAL_START_DATE = getAttributeSelect(ATTRIBUTE_BASELINE_INITIAL_START_DATE);
        SELECT_BASELINE_ID = "from[" + RELATIONSHIP_BASELINE_LOG + "].to.id";
        SELECT_MEMBERSHIP_ACCESS = "to." + getAttributeSelect(ATTRIBUTE_PROJECT_ACCESS);
        SELECT_MEMBERSHIP_ROLE = "to." + getAttributeSelect(ATTRIBUTE_PROJECT_ROLE);
        SELECT_MEMBERSHIP_VAULT_ACCESS = getAttributeSelect(ATTRIBUTE_VAULT_ACCESS);
        SELECT_PROJECT_ACCESS = getAttributeSelect(ATTRIBUTE_PROJECT_ACCESS);
        SELECT_PROJECT_ROLE = getAttributeSelect(ATTRIBUTE_PROJECT_ROLE);
        SELECT_PROJECT_ACCESS_LIST_ID = "to[" + RELATIONSHIP_PROJECT_ACCESS_LIST + "].from.id";
        SELECT_TASK_PROJECT_ACCESS_LIST_ID = "to[" + RELATIONSHIP_PROJECT_ACCESS_KEY + "].from.id";
        ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE = PropertyUtil.getSchemaProperty("attribute_DefaultConstraintType");
        SELECT_DEFAULT_CONSTRAINT_TYPE = getAttributeSelect(ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE);
        RELATIONSHIP_CONTRIBUTES_TO = PropertyUtil.getSchemaProperty("relationship_ContributesTo");
        MEMBER_RELATIONSHIP_TYPE = new RelationshipType(RELATIONSHIP_MEMBER);
        STATE_PROJECT_COMPLETE = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_SPACE, "state_Complete");
        STATE_PROJECT_ARCHIVE = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_SPACE, "state_Archive");
        SELECT_EFFORT_NOTIFICATION = "attribute[" + PropertyUtil.getSchemaProperty("attribute_EffortNotifications") + "]";
        TYPE_GATE = PropertyUtil.getSchemaProperty("type_Gate");
        TYPE_PHASE = PropertyUtil.getSchemaProperty("type_Phase");
        TYPE_MILESTONE = PropertyUtil.getSchemaProperty("type_Milestone");
        POLICY_PROJECT_REVIEW = PropertyUtil.getSchemaProperty("policy_ProjectReview");
        STATE_PROJECT_REVIEW_CREATE = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_REVIEW, "state_Create");
        STATE_PROJECT_REVIEW_REVIEW = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_REVIEW, "state_Review");
        STATE_PROJECT_REVIEW_COMPLETE = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_REVIEW, "state_Complete");
        POLICY_PROJECT_SPACE_HOLD_CANCEL = PropertyUtil.getSchemaProperty("policy_ProjectSpaceHoldCancel");
        STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_SPACE_HOLD_CANCEL, "state_Hold");
        STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_SPACE_HOLD_CANCEL, "state_Cancel");
        SELECT_PROJECT_TEMPLATE_ID = "from[" + RELATIONSHIP_INITIATED_TEMPLATE_PROJECT + "].to.id";
        ROLE_FINANCIAL_REVIEWER = PropertyUtil.getSchemaProperty("role_FinancialReviewer");
        ROLE_PROJECT_ASSESSOR = PropertyUtil.getSchemaProperty("role_ProjectAssessor");
        ROLE_EXTERNAL_PROJECT_LEAD = PropertyUtil.getSchemaProperty("role_ExternalProjectLead");
        ROLE_EXTERNAL_FINANCIAL_REVIEWER = PropertyUtil.getSchemaProperty("role_ExternalFinancialReviewer");
        ROLE_EXTERNAL_PROJECT_ASSESSOR = PropertyUtil.getSchemaProperty("role_ExternalProjectAssessor");
        SELECT_SUCCESSOR_IDS = "to[" + RELATIONSHIP_DEPENDENCY + "].from.id";
        SELECT_BUSINESS_GOAL_ID = "to[" + RELATIONSHIP_BUSINESS_GOAL_PROJECT_SPACE + "].from.id";
        SELECT_BUSINESS_GOAL_NAME = "to[" + RELATIONSHIP_BUSINESS_GOAL_PROJECT_SPACE + "].from.name";
        RELATIONSHIP_RESOLVED_TO = PropertyUtil.getSchemaProperty("relationship_ResolvedTo");
        RESOLVED_ISSUE_ID = "to[" + RELATIONSHIP_RESOLVED_TO + "].from.id";
        RESOLVED_BY_ITEM_ID_FOR_ISSUE = "from[" + RELATIONSHIP_RESOLVED_TO + "].to.id";
        SELECT_SOURCE_ID = "attribute[" + ProgramCentralConstants.ATTRIBUTE_SOURCE_ID + "]";
        ATTRIBUTE_FORMAT_PATTERN = PropertyUtil.getSchemaProperty("attribute_FormatPattern");
        SELECT_PROJECT_SCHEDULE = "attribute[" + ATTRIBUTE_PROJECT_SCHEDULE + "].value";
        hasCircularDependency = false;
        _testCase = false;
    }

    public static enum ProjectCloneOption {
        CLONE_WBS,
        CLONE_FOLDERS,
        CLONE_WBS_CONSTRAINTS,
        CLONE_EVERYTHING;

        private ProjectCloneOption() {
        }
    }
}
