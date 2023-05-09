package com.gsc.apps.program;

import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ExpandData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.PrintData;
import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.StringUtil;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.program.ProgramCentralConstants;
import matrix.db.*;
import matrix.util.MatrixException;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Set;

public class ProjectBudget extends DomainObject {

    public void create(Context context, String var2, String var3, String var4) throws FrameworkException {
        this.create(context, "Budget", var2, var3, var4);
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
        if (attrMap == null) {
            attrMap = new HashMap(3);
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

}
