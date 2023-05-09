package com.gsc.enovia.dpm.projecttemplate;

import com.dassault_systemes.enovia.dpm.ProjectSpace;
import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ExpandData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.matrixone.apps.common.VaultHolder;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.PersonUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.StringList;

public class ProjectTemplate extends com.matrixone.apps.program.ProjectTemplate implements VaultHolder {
    private static final long serialVersionUID = 1L;
    protected static final BigInteger MAX_OBJECTS = new BigInteger("100");
    protected static final String TYPE_PROJECT_TEMPLATE = "Project Template";
    protected static final String STATE_PROJECT_TEMPLATE_DRAFT = "Draft";
    protected static final String STATE_PROJECT_TEMPLATE_IN_APPROVAL = "In Approval";
    protected static final String STATE_PROJECT_TEMPLATE_RELEASED = "Released";
    protected static final String STATE_PROJECT_TEMPLATE_OBSOLETE = "Obsolete";

    static StringList getProjectTemplateStateList() {
        StringList var0 = new StringList(4);
        var0.add("Draft");
        var0.add("In Approval");
        var0.add("Released");
        var0.add("Obsolete");
        return var0;
    }

    public ProjectTemplate() {
    }

    public ProjectTemplate(String var1) throws Exception {
        super(var1);
    }

    public ProjectTemplate(BusinessObject var1) throws Exception {
        super(var1);
    }

    public static Datacollection getUserProjectTemplates(Context context, Datacollection datacollection, List selects, String var3) throws FoundationException {
        Dataobject var4 = new Dataobject();
        String where = "";
        if (var3 != null && !var3.isEmpty()) {
            where = ServiceUtil.getStateWhereClause(context, where, var3, getProjectTemplateStateList());
        } else {
            where = String.format("current == %s ", "Released");
        }

        Datacollection datacollection2 = new Datacollection();
        System.out.println("getUserProjectTemplates datacollection : " + datacollection);

        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            if (selects == null || selects.isEmpty()) {
                throw new FoundationException("You must provide some selectables to ProjectTemplate.getUserProjectTemplates");
            }

            try {
                ProjectSpace.getProjectInfo(context, datacollection, selects);
                List var15 = datacollection.getDataobjects();
                Iterator var16 = var15.iterator();

                while(var16.hasNext()) {
                    Dataobject var17 = (Dataobject)var16.next();
                    if (var17.getId().isEmpty()) {
                        throw new FoundationException("DPM100: Invalid Project Template ID. Business Object Does Not Exist");
                    }

                    datacollection2.getDataobjects().add(var17);
                }
            } catch (FoundationException var14) {
                if (var14.getMessage().contains("Business Object Does Not Exist")) {
                    throw new FoundationException("DPM100: Invalid Project Template ID. Business Object Does Not Exist");
                }

                throw var14;
            }
        } else {
//         ExpandData var7 = new ExpandData();
//         var7.setRelationshipPattern(RELATIONSHIP_MEMBER);
//         var7.setLimit(Short.valueOf((short)0));
//         var7.setRecurseToLevel(BigDecimal.valueOf(1L));
//         var7.setGetFrom(false);
//         var7.setGetTo(true);
//         var7.setTypePattern("Project Template");
//         var7.setObjectWhere(where);

            try {
//            DomainObject var8 = PersonUtil.getPersonObject(var0);
//            String var9 = var8.getInfo(var0, "id");
//            var4.setId(var9);
//            var6 = ObjectUtil.expand(var0, var4, var7, var2);
//            String var10 = String.format(" owner == %s AND %s", var0.getUser(), where);
                QueryData queryData = new QueryData();
                queryData.setTypePattern("Project Template");
                queryData.setWhereExpression(where);
                System.out.println("where : " + where);
                System.out.println("type : Project Template");
                Datacollection datacollection1 = ObjectUtil.query(context, queryData, selects);
                datacollection2.getDataobjects().addAll(datacollection1.getDataobjects());
            } catch (Exception var13) {
                var13.printStackTrace();
                throw new FoundationException(var13.getMessage());
            }
        }

        return datacollection2;
    }

    public void deleteObject(Context var1) throws Exception {
        String var2 = "";

        try {
            var2 = this.getId(var1);
        } catch (FrameworkException var5) {
            throw new FoundationException("DPM110: Only Owner or co-owner can delete Project Template");
        }

        boolean var3 = this.isOwnerOrCoOwner(var1, var2);
        if (var3) {
            String var4 = this.getInfo(var1, "relationship[" + DomainConstants.RELATIONSHIP_INITIATED_TEMPLATE_PROJECT + "]");
            if (!"FALSE".equalsIgnoreCase(var4)) {
                throw new Exception("DPM110:Project Template can not be deleted as Project is created from it");
            }

            this.remove(var1);
        }

    }
}
