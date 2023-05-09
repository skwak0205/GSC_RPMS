package com.gsc.apps.program;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompil
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.DebugUtil;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.matrixone.apps.program.ProgramCentralUtil;
import com.gsc.apps.program.ProjectTemplate;
import com.matrixone.apps.program.QuestionHolder;
import com.matrixone.apps.program.QuestionRelationship;
import com.matrixone.apps.program.Questionable;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.StringList;

public class Question extends DomainObject {
    public static final String SELECT_HAS_ITEMS;
    protected static final String SELECT_ITEMS_ID;
    protected static final String SELECT_ITEMS_NAME;
    protected static final String SELECT_ITEMS_TRANSFER;

    public Question() {
    }

    public Question(String var1) throws Exception {
        super(var1);
    }

    public Question(BusinessObject var1) {
        super(var1);
    }

    public void clear() throws FrameworkException {
        super.clear();
    }

    public QuestionRelationship addItem(Context var1, Questionable var2, boolean var3) throws FrameworkException {
        ContextUtil.startTransaction(var1, true);

        try {
            DomainRelationship var4 = DomainRelationship.connect(var1, this, RELATIONSHIP_QUESTION, (DomainObject)var2);
            QuestionRelationship var5 = new QuestionRelationship(var4);
            var5.setAttributeValue(var1, ATTRIBUTE_TASK_TRANSFER, String.valueOf(var3));
            ContextUtil.commitTransaction(var1);
            return var5;
        } catch (Exception var6) {
            ContextUtil.abortTransaction(var1);
            throw new FrameworkException(var6);
        }
    }

    protected com.gsc.apps.program.Question clone(Context var1, String var2, QuestionHolder var3) throws FrameworkException {
        ContextUtil.startTransaction(var1, true);

        try {
            DebugUtil.debug("Entered Question clone");
            DomainObject var4 = (DomainObject)var3;
            BusinessObject var5 = this.cloneObject(var1, var2, this.getUniqueName(""), this.getDefaultVault(var1, var4));
            com.gsc.apps.program.Question var6 = new com.gsc.apps.program.Question(var5);
            DomainRelationship.connect(var1, var4, RELATIONSHIP_PROJECT_QUESTION, var6);
            ContextUtil.commitTransaction(var1);
            return var6;
        } catch (Exception var7) {
            ContextUtil.abortTransaction(var1);
            throw new FrameworkException(var7);
        }
    }

    protected static void cloneStructure(Context var0, QuestionHolder var1, QuestionHolder var2, Map var3) throws FrameworkException {
        DebugUtil.debug("Entered Question cloneStructure");
        StringList var4 = new StringList(4);
        var4.add("id");
        var4.add("name");
        var4.add(SELECT_ITEMS_ID);
        var4.add(SELECT_ITEMS_TRANSFER);
        MapList var5 = getQuestions(var0, var1, var4, (StringList)null, (String)null, (String)null);
        DomainObject var6 = (DomainObject)var2;
        if (var3 == null) {
            var3 = new HashMap();
        }

        com.gsc.apps.program.Question var7 = new com.gsc.apps.program.Question();
        Iterator var8 = var5.iterator();

        while(true) {
            Map var9;
            com.gsc.apps.program.Question var12;
            Object var13;
            do {
                if (!var8.hasNext()) {
                    return;
                }

                var9 = (Map)var8.next();
                String var10 = (String)var9.get("id");
                String var11 = (String)var9.get("name");
                var7.setId(var10);
                var12 = var7.clone(var0, var11, var2);
                ((Map)var3).put(var10, var12);
                var13 = var9.get(SELECT_ITEMS_ID);
            } while(var13 == null);

            Object var14 = var9.get(SELECT_ITEMS_TRANSFER);
            Object var15 = null;
            Object var16 = null;
            if (var13 instanceof String) {
                var15 = new ArrayList(1);
                ((List)var15).add(var13);
                var16 = new ArrayList(1);
                ((List)var16).add(var14);
            } else {
                var15 = (List)var13;
                var16 = (List)var14;
            }

            for(int var17 = 0; var17 < ((List)var15).size(); ++var17) {
                String var18 = (String)((List)var15).get(var17);
                String var19 = (String)((List)var16).get(var17);
                DomainObject var20 = (DomainObject)((Map)var3).get(var18);
                if (var20 != null) {
                    String var21 = "connect bus $1  preserve relationship $2  to $3 $4 $5";
                    MqlUtil.mqlCommand(var0, var21, new String[]{var12.getObjectId(), RELATIONSHIP_QUESTION, var20.getObjectId(), ATTRIBUTE_TASK_TRANSFER, var19});
                }
            }
        }
    }

    public void create(Context var1, String var2, String var3, QuestionHolder var4) throws FrameworkException {
        String var5 = this.getUniqueName("");
        if (var2 == null || var2.equals("")) {
            var2 = "Q-" + var5.substring(var5.length() - 6);
        }

        this.createAndConnect(var1, TYPE_QUESTION, var2, var5, var3, (String)null, RELATIONSHIP_PROJECT_QUESTION, (DomainObject)var4, true);
    }

    public static String getDefaultPolicy(Context var0) {
        return POLICY_QUESTION;
    }

    public MapList getItems(Context var1, StringList var2, StringList var3, String var4, String var5) throws FrameworkException {
        MapList var6 = this.getRelatedObjects(var1, RELATIONSHIP_QUESTION, "*", var2, var3, false, true, (short)1, var4, var5);
        return var6;
    }

    public static Map getQuestion(Context var0, Questionable var1, StringList var2, StringList var3) throws FrameworkException {
        Map var4 = ((DomainObject)var1).getRelatedObject(var0, RELATIONSHIP_QUESTION, false, var2, var3);
        return var4;
    }

    public static MapList getQuestions(Context var0, QuestionHolder var1, StringList var2, StringList var3, String var4, String var5) throws FrameworkException {
        MapList var6 = ((DomainObject)var1).getRelatedObjects(var0, RELATIONSHIP_PROJECT_QUESTION, "*", var2, var3, false, true, (short)1, var4, var5);
        return var6;
    }

    public static MapList getTypeAttributes(Context var0) throws FrameworkException {
        return getTypeAttributes(var0, TYPE_QUESTION);
    }

    public static void removeItem(Context var0, String var1) throws FrameworkException {
        DomainRelationship.disconnect(var0, var1);
    }

    public static QuestionRelationship setQuestion(Context var0, String var1, Questionable var2, boolean var3) throws FrameworkException {
        ContextUtil.startTransaction(var0, true);

        try {
            QuestionRelationship var4 = null;
            DomainRelationship var5 = ((DomainObject)var2).setRelatedObject(var0, RELATIONSHIP_QUESTION, false, var1);
            if (var5 != null) {
                var4 = new QuestionRelationship(var5);
                var4.setAttributeValue(var0, ATTRIBUTE_TASK_TRANSFER, String.valueOf(var3));
            }

            ContextUtil.commitTransaction(var0);
            return var4;
        } catch (Exception var6) {
            ContextUtil.abortTransaction(var0);
            throw new FrameworkException(var6);
        }
    }

    public void connectTaskArray(Context var1, String[] var2, String[] var3) throws FrameworkException {
        Map var4 = DomainRelationship.connect(var1, this, RELATIONSHIP_QUESTION, true, var2);

        for(int var5 = 0; var5 < var2.length; ++var5) {
            String var6 = (String)var4.get(var2[var5]);
            DomainRelationship.setAttributeValue(var1, var6, ATTRIBUTE_TASK_TRANSFER, var3[var5]);
        }

    }

    public String disconnectTaskList(Context var1, String[] var2) throws Exception {
        String var3 = "";
        String var4 = var1.getLocale().getLanguage();
        ArrayList var5 = new ArrayList();
        Map var6 = ProgramCentralUtil.parseTableRowId(var1, var2[0]);
        String var7 = (String)var6.get("parentOId");
        var5.add(var6.get("objectId"));

        for(int var8 = 0; var8 < var2.length; ++var8) {
            Map var9 = ProgramCentralUtil.parseTableRowId(var1, var2[var8]);
            String var10 = (String)var9.get("parentOId");
            if (!var7.equalsIgnoreCase(var10)) {
                var3 = ProgramCentralUtil.getPMCI18nString(var1, "emxProgramCentral.common.PleaseSelectTasksOfOnlyOneQuestion", var4);
                return var3;
            }

            var5.add(var9.get("objectId"));
        }

        StringList var17 = new StringList(1);
        var17.add("id");
        StringList var18 = new StringList(1);
        var18.add("id[connection]");
        ArrayList var19 = new ArrayList();
        String[] var11 = null;
        com.gsc.apps.program.Question var12 = new com.gsc.apps.program.Question(var7);
        MapList var13 = var12.getRelatedObjects(var1, RELATIONSHIP_QUESTION, TYPE_TASK_MANAGEMENT, var17, var18, false, true, (short)1, (String)null, (String)null, 0);
        Iterator var14 = var13.iterator();

        while(var14.hasNext()) {
            Map var15 = (Map)var14.next();
            String var16 = (String)var15.get("id");
            if (var5.contains(var16)) {
                var19.add(var15.get("id[connection]"));
            }
        }

        var11 = new String[var19.size()];
        var19.toArray(var11);
        DomainRelationship.disconnect(var1, var11);
        return var3;
    }

    public Map<String, String> createAndConnectQuestion(Context var1, String var2) throws MatrixException {
        HashMap var3 = new HashMap(2);

        try {
            com.gsc.apps.program.ProjectTemplate var4 = new ProjectTemplate(var2);
            String var5 = this.getUniqueName("");
            String var6 = this.getUniqueName("Q-", 2);
            String var7 = this.getDefaultPolicy(var1, TYPE_QUESTION);
            String var8 = var6.replace("Q", "Question");
            this.createAndConnect(var1, TYPE_QUESTION, var6, var5, var7, var1.getVault().getName(), RELATIONSHIP_PROJECT_QUESTION, var4, true);
            this.setDescription(var1, var8);
            String var9 = this.getId(var1);
            String var10 = this.getInfo(var1, "to[Project Question].id");
            var3.put("questionId", var9);
            var3.put("questionProjectRelId", var10);
            return var3;
        } catch (Exception var11) {
            throw new MatrixException(var11);
        }
    }

    public static Map<String, String> getQuestionResponce(String var0) throws Exception {
        HashMap var1 = new HashMap();
        if (ProgramCentralUtil.isNotNullString(var0)) {
            StringList var2 = FrameworkUtil.split(var0, "|");

            for(int var3 = 0; var3 < var2.size(); ++var3) {
                String var4 = (String)var2.get(var3);
                StringList var5 = FrameworkUtil.split(var4, "=");
                var1.put((String)var5.get(0), (String)var5.get(1));
            }
        }

        return var1;
    }

    public static String getQuestionResponceURL(Map var0, String var1, String var2, String var3, String var4, String var5) throws FrameworkException {
        StringBuilder var6 = new StringBuilder();
        var6.append("../common/emxIndentedTable.jsp?table=PMCProjectTemplateQuestionTable");
        var6.append("&program=").append(var2);
        var6.append("&suiteKey=ProgramCentral&SuiteDirectory=programcentral");
        var6.append("&customize=false");
        var6.append("&rowGrouping=false");
        var6.append("&multiColumnSort=false");
        var6.append("&showPageURLIcon=false");
        var6.append("&displayView=details");
        var6.append("&showClipboard=false");
        var6.append("&objectCompare=false");
        var6.append("&findMxLink=false");
        var6.append("&autoFilter=false");
        var6.append("&showRMB=false");
        var6.append("&objectId=").append(var1);
        var6.append("&submitURL=../programcentral/emxProgramCentralUtil.jsp?mode=updateQuestionValue");
        var6.append("&fieldNameDisplay=").append(var3);
        var6.append("&fieldNameActual=").append(var4);
        var6.append("&fieldNameOID=").append(var5);
        return var6.toString();
    }

    static {
        SELECT_HAS_ITEMS = "from[" + RELATIONSHIP_QUESTION + "]";
        SELECT_ITEMS_ID = "from[" + RELATIONSHIP_QUESTION + "].to.id";
        SELECT_ITEMS_NAME = "from[" + RELATIONSHIP_QUESTION + "].to.name";
        SELECT_ITEMS_TRANSFER = "from[" + RELATIONSHIP_QUESTION + "]." + QuestionRelationship.SELECT_TASK_TRANSFER;
    }
}
