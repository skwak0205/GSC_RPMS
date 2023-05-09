package com.gsc.enovia.dpm.projectconcept;

import com.dassault_systemes.enovia.dpm.ProjectSpace;
import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ExpandData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.gsc.enovia.dpm.gscProjectSpace;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.PersonUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.Set;

import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import matrix.db.*;
import matrix.util.MatrixException;
import matrix.util.StringList;

public class gscProjectConcept extends DomainObject {
    private static final long serialVersionUID = 1L;
    protected static final BigInteger MAX_OBJECTS = new BigInteger("100");
    private static final String TYPE_PROJECTCONCEPT = "type_ProjectConcept";
    protected static final String TYPE_PROJECT_TEMPLATE = "Project Template";
    protected static final String STATE_PROJECT_TEMPLATE_DRAFT = "Draft";
    protected static final String STATE_PROJECT_TEMPLATE_IN_APPROVAL = "In Approval";
    protected static final String STATE_PROJECT_TEMPLATE_RELEASED = "Released";
    protected static final String STATE_PROJECT_TEMPLATE_OBSOLETE = "Obsolete";

    static StringList getProjectConceptStateList() {
        StringList var0 = new StringList(4);
        var0.add("Concept");
        var0.add("Prototype");
        var0.add("Review");
        var0.add("Approved");
        return var0;
    }

    protected static Datacollection getUserProjectConcepts(Context context, Datacollection datacollection1, List var3, String state) throws FoundationException {
        boolean hasData = false;
        String var6 = PropertyUtil.getSchemaName(context, "type_ProjectConcept");
        if (datacollection1 != null && !datacollection1.getDataobjects().isEmpty()) {
            hasData = true;
        }

        if (hasData && (var3 == null || var3.isEmpty())) {
            throw new FoundationException("You must provide some selectables to getUserResourceRequests");
        } else {
            Datacollection datacollection = new Datacollection();
            if (hasData) {
                try {
                    com.gsc.enovia.dpm.ServiceUtil.getObjectInfo(context, datacollection1, var3);
                    List var8 = datacollection1.getDataobjects();
                    Iterator var9 = var8.iterator();

                    while(var9.hasNext()) {
                        Dataobject var10 = (Dataobject)var9.next();
                        if (var10.getId().isEmpty()) {
                            throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
                        }

                        datacollection.getDataobjects().add(var10);
                    }
                } catch (Exception var11) {
                    if (var11.getMessage().contains("Business Object Does Not Exist")) {
                        throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
                    }

                    throw var11;
                }
            } else {
                String whereClause = "";
                whereClause = String.format("%s owner == %s", whereClause, context.getUser());

                QueryData queryData = new QueryData();
                queryData.setTypePattern(var6);
                System.out.println("whereClause = " + whereClause);
                queryData.setWhereExpression(whereClause);
                queryData.setLimit(new BigInteger("500"));
                datacollection = ObjectUtil.query(context, queryData, var3);
            }

            return datacollection;
        }
    }

    public static Datacollection getUserProjectConcepts_org(Context context, Datacollection datacollection, List list, String state) throws FoundationException {
        Dataobject var4 = new Dataobject();
        String var5 = "";
        if (state != null && !state.isEmpty()) {
            var5 = ServiceUtil.getStateWhereClause(context, var5, state, getProjectConceptStateList());
        } else {
            //var5 = String.format("current != %s ", "Obsolete");
        }

        Datacollection var6 = new Datacollection();
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            if (list == null || list.isEmpty()) {
                throw new FoundationException("You must provide some selectables to ProjectConcept.getUserProjectConcepts");
            }

            try {
                ProjectSpace.getProjectInfo(context, datacollection, list);
                List var15 = datacollection.getDataobjects();
                Iterator var16 = var15.iterator();

                while(var16.hasNext()) {
                    Dataobject var17 = (Dataobject)var16.next();
                    if (var17.getId().isEmpty()) {
                        throw new FoundationException("DPM100: Invalid Project Concept ID. Business Object Does Not Exist");
                    }

                    var6.getDataobjects().add(var17);
                }
            } catch (FoundationException var14) {
                if (var14.getMessage().contains("Business Object Does Not Exist")) {
                    throw new FoundationException("DPM100: Invalid Project Concept ID. Business Object Does Not Exist");
                }

                throw var14;
            }
        } else {
            ExpandData var7 = new ExpandData();
            var7.setRelationshipPattern(RELATIONSHIP_MEMBER);
            var7.setLimit(Short.valueOf((short)0));
            var7.setRecurseToLevel(BigDecimal.valueOf(1L));
            var7.setGetFrom(false);
            var7.setGetTo(true);
            var7.setTypePattern("Project Template");
            var7.setObjectWhere(var5);

            try {
                DomainObject var8 = PersonUtil.getPersonObject(context);
                String var9 = var8.getInfo(context, "id");
                var4.setId(var9);
                var6 = ObjectUtil.expand(context, var4, var7, list);
                String var10 = "";
                if(ProgramCentralUtil.isNullString(var5)){
                    var10 = String.format(" owner == %s", context.getUser());
                }else{
                    var10 = String.format(" owner == %s AND %s", context.getUser(), var5);
                }
                QueryData var11 = new QueryData();
                var11.setTypePattern("Project Template");
                var11.setWhereExpression(var10);
                Datacollection var12 = ObjectUtil.query(context, var11, list);
                var6.getDataobjects().addAll(var12.getDataobjects());
            } catch (Exception var13) {
                var13.printStackTrace();
                throw new FoundationException(var13.getMessage());
            }
        }

        return var6;
    }

    public void deleteObject(Context var1) throws Exception {
        String var2 = "";

        try {
            var2 = this.getId(var1);
        } catch (FrameworkException var5) {
            throw new FoundationException("DPM110: Only Owner or co-owner can delete Project Template");
        }

        boolean var3 = true;//this.isOwnerOrCoOwner(var1, var2);
        if (var3) {
//            String var4 = this.getInfo(var1, "relationship[" + DomainConstants.RELATIONSHIP_INITIATED_TEMPLATE_PROJECT + "]");
//            if (!"FALSE".equalsIgnoreCase(var4)) {
//                throw new Exception("DPM110:Project Template can not be deleted as Project is created from it");
//            }

            this.remove(var1);
        }

    }

    public void create(Context context, String var2, String var3, String var4) throws FrameworkException {
        this.create(context, "Project Concept", var2, var3, var4);
    }

    public void create(Context context, String type, String var3, String var4, String var5) throws FrameworkException {
        this.create(context, type, var3, var4, var5, (Map)null, (String)null);
    }

    public void create(Context context, String type, String policy, String var4, String var5, Map var6, String var7) throws FrameworkException {
        this.create(context, type, policy, var4, var5, var6, var7, false);
    }

    public void create(Context context, String type, String policy, String var4, String var5, Map var6, String var7, boolean var8) throws FrameworkException {
        this.create(context, type, policy, var4, var5, var6, var7, var8, new BusinessInterfaceList());
    }

    public void create(Context context, String type, String name, String policy, String var5, Map attrMap, String descriptioin, boolean var8, BusinessInterfaceList businessInterfaceList) throws FrameworkException {
        try {
            ComponentsUtil.checkLicenseReserved(context, ProgramCentralConstants.PGE_PRG_LICENSE_ARRAY);
        } catch (MatrixException var21) {
            throw new FrameworkException(var21);
        }

        String revision = this.getUniqueName("");
        // String revision = "-";
        this.createObject(context, type, name, revision, policy, context.getVault().getName());
        if (attrMap == null) {
            attrMap = new HashMap(3);
        }

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
    static {
    }
}
