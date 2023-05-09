package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUserException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceSave;
import com.dassault_systemes.enovia.e6wv2.foundation.db.MqlUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.PrintData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Range;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ServiceParameters;
import com.dassault_systemes.enovia.e6wv2.foundation.util.LicenseUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.StringUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.eMatrixDateFormat;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.StringList;

public class ServiceUtil {
    protected static final String TYPE_PROJECT_SPACE = "Project Space";
    protected static final String PARAMETER_STATE = "state";
    protected static final String PARAMETER_OWNED = "owned";
    protected static final String PARAMETER_ASSIGNED = "assigned";
    protected static final String PARAMETER_COMPLETED = "completed";
    protected static final String PARAMETER_ALLRISKS = "allrisks";
    protected static final String PARAMETER_FORCE = "force";
    protected static final String PARAMETER_CONTEXTID = "contextId";
    protected static final String PARAMETER_SUBPROJRISKS = "subprojectRisks";
    protected static final String PARAMETER_STRUCTURED = "structured";
    protected static final String PARAMETER_TYPE = "type";
    protected static final String PARAMETER_CONNECT_RMC = "connectRMC";
    protected static final String PARAMETER_PROGRAM_ID = "programId";
    protected static final String SELECT_PHYSICAL_ID = "physicalid";
    protected static final String PHYSICAL_ID = "physicalId";
    private static final String TAG = ">>> UTILITY (DPM):  ";
    protected static final String REGISTERED_SUITE = "ProgramCentral";

    public static void checkLicenseRiskWidget(Context var0) throws FoundationException {
        try {
            checkLicenseProjectLead(var0, (HttpServletRequest)null);
        } catch (FoundationUserException var5) {
            try {
                checkLicenseXProject(var0, (HttpServletRequest)null);
            } catch (FoundationUserException var4) {
                String var3 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", "emxProjectService.Error.NoValidlicense", var0.getLocale());
                throw new FoundationUserException(var3);
            }
        }

    }

    public static void checkLicenseDPM(Context var0) throws FoundationException {
        checkLicenseProjectLead(var0, (HttpServletRequest)null);
    }

    public static void checkLicenseProject(Context var0, HttpServletRequest var1) throws FoundationException {
        try {
            LicenseUtil.checkLicenseReserved(var0, "ENO_PGE_TP", var1);
        } catch (Exception var4) {
            String var3 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", "emxProjectService.Error.NoPGElicense", var0.getLocale());
            throw new FoundationUserException(var3);
        }
    }

    protected static void checkLicenseProjectLead(Context var0, HttpServletRequest var1) throws FoundationException {
        try {
            LicenseUtil.checkLicenseReserved(var0, "ENO_PRF_TP", var1);
        } catch (Exception var4) {
            String var3 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", "emxProjectService.Error.NoPRFlicense", var0.getLocale());
            throw new FoundationUserException(var3);
        }
    }

    protected static void checkLicenseXProject(Context var0, HttpServletRequest var1) throws FoundationException {
        try {
            LicenseUtil.checkLicenseReserved(var0, "ENXPLAI_TP", var1);
        } catch (Exception var4) {
            String var3 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", "emxProjectService.Error.NoXProjectlicense", var0.getLocale());
            throw new FoundationUserException(var3);
        }
    }

    protected static void addToMap(Map var0, String var1, String var2) {
        if (var2 != null) {
            var0.put(var1, var2);
        }

    }

    protected static void debug(String var0, long var1, ServiceParameters var3) {
        if (var3.getStartTime() != 0L) {
            FoundationUtil.debug(var0, var1);
        }

    }

    protected static boolean isValid(Context var0, String var1) throws Exception {
        try {
            MqlUtil.mqlCommand(var0, "print bus $1 select $2 dump", new String[]{var1, "physicalid"});
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public static Dataobject getObjectInfo(Context var0, String var1, List var2) throws FoundationException {
        Dataobject var3 = new Dataobject();
        var3.setId(var1);
        Datacollection var4 = new Datacollection();
        var4.getDataobjects().add(var3);
        getObjectInfo(var0, var4, var2);
        return (Dataobject)var4.getDataobjects().get(0);
    }

    protected static String formatDBDate(Context var0, String var1) throws Exception {
        if (var1 != null && !var1.isEmpty()) {
            Date var2 = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)).parse(var1);
            SimpleDateFormat var3 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            String var4 = var3.format(var2);
            return var4;
        } else {
            return var1;
        }
    }

    protected static String formatUTCDate(Context var0, String var1) throws Exception {
        if (var1 != null && !var1.isEmpty()) {
            Date var2 = (new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US)).parse(var1);
            SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z", Locale.US);
            String var4 = var3.format(var2);
            return var4;
        } else {
            return var1;
        }
    }

    protected static String formatISODate(Context var0, String var1) throws Exception {
        if (var1 != null && !var1.isEmpty()) {
            Date var2 = (new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US)).parse(var1);
            SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            String var4 = var3.format(var2);
            return var4;
        } else {
            return var1;
        }
    }

    public static void getObjectInfo(Context var0, Datacollection var1, List var2) throws FoundationException {
        if (var2 != null && !var2.isEmpty()) {
            if (var1.getDataobjects().size() > 0) {
                ObjectUtil.print(var0, var1, (PrintData)null, var2, true);
            }

        } else {
            throw new FoundationException("You must provide some selectables to ProgramService.getProgramInfo");
        }
    }

    public static String addConnection(Context var0, String var1, String var2, Map var3, String var4) throws FoundationException {
        long var5 = System.currentTimeMillis();
        boolean var7 = !ServiceSave.isObjectNew(var0, var1);
        String var8 = ObjectEditUtil.connect(var0, var1, var4, var2, (Map)null, false, false, var7);
        ObjectEditUtil.modifyConnection(var0, var8, var3, true);
        String var9 = String.format("%s - addConnection %s - (%s) ...\t", ">>> UTILITY (DPM):  ", var4, var2);
        FoundationUtil.debug(var9, var5);
        return var8;
    }

    public static String modifyConnection(Context var0, String var1, String var2, Map var3, String var4) throws FoundationException {
        long var5 = System.currentTimeMillis();
        String var7 = ObjectEditUtil.modifyConnection(var0, var1, var4, var2, var3, true, "id");
        String var8 = String.format("%s - modifyConnection %s - (%s) ...\t", ">>> UTILITY (DPM):  ", var4, var2);
        FoundationUtil.debug(var8, var5);
        return var7;
    }

    protected static Datacollection getProjectInfo(Context var0, String var1, List var2, int var3) throws FoundationException {
        long var4 = System.currentTimeMillis();
        QueryData var6 = new QueryData();
        var6.setTypePattern("Project Space");
        var6.setNamePattern(var1);
        var6.setLimit(BigInteger.valueOf((long)new Integer(var3)));
        Datacollection var7 = ObjectUtil.query(var0, var6, var2);
        String var8 = String.format("%s%s Project (%s)...\t", ">>> UTILITY (DPM):  ", "getProjectInfo", var1);
        FoundationUtil.debug(var8, var4);
        return var7;
    }

    protected static Range getStates(Context var0, String var1) throws FoundationException {
        Range var2 = new Range();
        String var3 = var0.getLocale().getLanguage();
        String var5 = MqlUtil.mqlCommand(var0, "print policy $1 select $2 dump $3", new String[]{var1, "state", ","});
        List var6 = StringUtil.splitString(var5, ",");
        Iterator var7 = var6.iterator();

        while(var7.hasNext()) {
            String var8 = (String)var7.next();
            String var9 = PropertyUtil.getStateI18NString(var0, var1, var8, var3);
            Range.Item var10 = new Range.Item();
            var10.setValue(var8);
            var10.setDisplay(var9);
            var10.setHelpinfo(var1);
            var2.getItem().add(var10);
        }

        return var2;
    }

    protected static Range getAllPolicyStates(Context var0, String var1) throws FoundationException {
        Range var2 = new Range();
        String var3 = var0.getLocale().getLanguage();
        Range var4 = getPolicies(var0, var1);
        Iterator var5 = var4.getItem().iterator();

        while(var5.hasNext()) {
            Range.Item var6 = (Range.Item)var5.next();
            Range var7 = getStates(var0, var6.getValue());
            var2.getItem().addAll(var7.getItem());
        }

        return var2;
    }

    protected static Range getAllTypePolicy(Context var0, String var1, String var2, StringList var3, StringList var4) throws FoundationException {
        Range var5 = new Range();
        Range var6 = getTypes(var0, var1, var3);

        Range var9;
        for(Iterator var7 = var6.getItem().iterator(); var7.hasNext(); var5.getItem().addAll(var9.getItem())) {
            Range.Item var8 = (Range.Item)var7.next();
            var9 = null;
            if (var1.equalsIgnoreCase(var8.getValue())) {
                var9 = getPolicies(var0, var8.getValue(), var2, var4);
            } else {
                var9 = getPolicies(var0, var8.getValue(), (String)null, var4);
            }
        }

        return var5;
    }

    protected static Range getTypes(Context var0, String var1, StringList var2) throws FoundationException {
        Range var3 = new Range();

        try {
            String var4 = var0.getLocale().getLanguage();
            List var5 = ObjectUtil.getTypeDerivatives(var0, var1, true);
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
                String var7 = (String)var6.next();
                if (!var2.contains(var7)) {
                    String var8 = EnoviaResourceBundle.getAdminI18NString(var0, "Type", var7, var4);
                    Range.Item var9 = new Range.Item();
                    var9.setValue(var7);
                    var9.setDisplay(var8);
                    var9.setHelpinfo(var1);
                    if (var1.equals(var7)) {
                        var9.setDefault(true);
                    }

                    var3.getItem().add(var9);
                }
            }

            return var3;
        } catch (MatrixException var10) {
            throw new FoundationException(var10);
        }
    }

    protected static Range getPolicies(Context var0, String var1) throws FoundationException {
        return getPolicies(var0, var1, (String)null, new StringList());
    }

    protected static Range getPolicies(Context var0, String var1, String var2, StringList var3) throws FoundationException {
        Range var4 = new Range();
        String var5 = var0.getLocale().getLanguage();
        String var6 = "print type $1 select $2 dump $3";
        String var7 = MqlUtil.mqlCommand(var0, var6, new String[]{var1, "policy", ","});
        List var8 = StringUtil.splitString(var7, ",");
        Iterator var9 = var8.iterator();

        while(true) {
            String var10;
            do {
                if (!var9.hasNext()) {
                    return var4;
                }

                var10 = (String)var9.next();
            } while(var3 != null && var3.contains(var10));

            var6 = "print policy $1 select $2 dump $3";
            var7 = MqlUtil.mqlCommand(var0, var6, new String[]{var10, "hidden", ","});
            if ("FALSE".equalsIgnoreCase(var7)) {
                String var11 = PropertyUtil.getAdminI18NString(var0, "policy", var10, var5);
                Range.Item var12 = new Range.Item();
                var12.setValue(var10);
                var12.setDisplay(var11);
                var12.setHelpinfo(var1);
                if (var2 != null && !var2.isEmpty() && var10.equals(var2)) {
                    var12.setDefault(true);
                }

                var4.getItem().add(var12);
            }
        }
    }

    protected static Range getRiskTypes(Context var0, String var1) throws FoundationException {
        Range var2 = new Range();
        String var3 = var0.getLocale().getLanguage();
        String var5 = MqlUtil.mqlCommand(var0, "print attribute $1 select $2 dump $3", new String[]{var1, "range", ","});
        List var6 = StringUtil.splitString(var5, ",");
        Iterator var7 = var6.iterator();

        while(var7.hasNext()) {
            String var8 = (String)var7.next();
            String var9 = PropertyUtil.getStateI18NString(var0, var1, var8, var3);
            Range.Item var10 = new Range.Item();
            var10.setValue(var8);
            var10.setDisplay(var9);
            var2.getItem().add(var10);
        }

        return var2;
    }

    protected static Range getAttributeRange(Context var0, String var1, boolean var2, String var3) throws FoundationException {
        Range var4 = new Range();
        String var5 = "list";
        if (var1.indexOf(46) != -1) {
            var5 = "print";
        }

        var4.setDefaultValue("");
        String var6 = MqlUtil.mqlCommand(var0, "$1 attribute $2 select $3 $4 dump $5", new String[]{var5, var1, "default", "range", ServiceConstants.VALUE_SEPARATOR});
        if (!var6.isEmpty()) {
            List var7 = StringUtil.splitString(var6, ServiceConstants.VALUE_SEPARATOR);
            String var8 = (String)var7.remove(0);
            if (var3 != null && !var3.isEmpty()) {
                var8 = var3;
            }

            var4 = new Range();
            var4.setDefaultValue(var8);
            String var9 = var0.getLocale().getLanguage();
            if (var2) {
                Collections.sort(var7);
            }

            Iterator var10 = var7.iterator();

            while(var10.hasNext()) {
                String var11 = (String)var10.next();
                if (var11.startsWith("= ")) {
                    var11 = var11.substring(2);
                    String var12 = PropertyUtil.getRangeI18NString(var0, var1, var11, var9);
                    Range.Item var13 = new Range.Item();
                    var4.getItem().add(var13);
                    var13.setValue(var11);
                    if (var11.equals(var8)) {
                        var13.setDefault(true);
                    }

                    var13.setDisplay(var12);
                }
            }
        }

        return var4;
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
}
