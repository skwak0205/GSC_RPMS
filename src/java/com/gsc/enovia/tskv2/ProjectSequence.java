//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tskv2;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceJson;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceSave;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.MqlUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.db.MQLCommand;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ProjectSequence {
    private String _palPhysicalId = null;
    private Dataobject _palDataObject = null;
    private boolean _isSessionStarted = false;
    private Map<String, Dataobject> _projectDataObjInfo = null;
    private int _count = 1;
    private String ATTRIBUTE_PROJECT_SEQUENCE = null;
    public static final String KEY_PROJECT = "project";
    public static final String KEY_SEQ_ID = "seqId";
    public static final String KEY_WBS_ID = "wbsId";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_PAL_ID = "palId";
    private BusinessObject bo = null;

    public ProjectSequence(Context var1, String var2) throws FoundationException {
        try {
            this.bo = new BusinessObject(var2);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        this._palPhysicalId = var2;
        this._projectDataObjInfo = new HashMap();
        this.ATTRIBUTE_PROJECT_SEQUENCE = PropertyUtil.getSchemaName(var1, "attribute_ProjectSequence");
    }

    public boolean startUpdateSession(Context var1) throws FoundationException {
        boolean var2 = false;
        if (!this._isSessionStarted) {
            try {
                this.bo.lockForUpdate(var1);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            this._palDataObject = this.getProjectSequenceAttribute(var1);
            this._isSessionStarted = true;
            var2 = true;
        }

        return var2;
    }

    public void finishUpdateSession(Context var1) throws FoundationException {
        if (this._isSessionStarted) {
            this._isSessionStarted = false;
            this.updateProjectSequence(var1, this._palDataObject);
            this._projectDataObjInfo = null;
        }

    }

    private void updateProjectSequence(Context var1, Dataobject var2) throws FoundationException {
        removeProcessingElements(var2);
        String var3 = ServiceJson.generateJsonStringfromJAXB(var2);
        if (var3 != null && !var3.isEmpty()) {
            try {
                var3 = zip(var3);
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            String var4 = "modify bus $1 $2 $3";

            try {
                ContextUtil.pushContext(var1);
                (new MQLCommand()).executeCommand(var1, false, false, var4, new String[]{this._palPhysicalId, this.ATTRIBUTE_PROJECT_SEQUENCE, var3});
            } catch (Exception var10) {
                var10.printStackTrace();
            } finally {
                ContextUtil.popContext(var1);
            }
        }

    }

    private static void removeProcessingElements(Dataobject var0) {
        DataelementMap var1 = var0.getDataelements();
        if (var1 != null) {
            var1.remove("seqId");
            var1.remove("wbsId");
            var1.remove("level");
        }

        Iterator var2 = var0.getChildren().iterator();

        while(var2.hasNext()) {
            Dataobject var3 = (Dataobject)var2.next();
            removeProcessingElements(var3);
        }

    }

    private Dataobject getProjectSequenceAttribute(Context var1) throws FoundationException {
        String var3 = MqlUtil.mqlCommand(var1, true, "print bus $1 select $2 dump", true, new String[]{this._palPhysicalId, "attribute[" + this.ATTRIBUTE_PROJECT_SEQUENCE + "]"});
        Dataobject var4 = new Dataobject();
        if (!var3.isEmpty()) {
            String var5 = null;

            try {
                var5 = unzip(var3);
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            if (!var5.isEmpty()) {
                var4 = ServiceJson.readDataobjectfromJson(var5);
            }
        }

        return var4;
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

    private static String unzip(String var0) throws IOException {
        Base64.Decoder var1 = Base64.getDecoder();
        byte[] var2 = var1.decode(var0);
        if (!isCompressed(var2)) {
            return var0;
        } else {
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
                        } catch (Throwable var85) {
                            var10 = var85;
                            throw var85;
                        } finally {
                            if (var9 != null) {
                                if (var10 != null) {
                                    try {
                                        var9.close();
                                    } catch (Throwable var84) {
                                        var10.addSuppressed(var84);
                                    }
                                } else {
                                    var9.close();
                                }
                            }

                        }
                    } catch (Throwable var87) {
                        var8 = var87;
                        throw var87;
                    } finally {
                        if (var7 != null) {
                            if (var8 != null) {
                                try {
                                    var7.close();
                                } catch (Throwable var83) {
                                    var8.addSuppressed(var83);
                                }
                            } else {
                                var7.close();
                            }
                        }

                    }
                } catch (Throwable var89) {
                    var6 = var89;
                    throw var89;
                } finally {
                    if (var5 != null) {
                        if (var6 != null) {
                            try {
                                var5.close();
                            } catch (Throwable var82) {
                                var6.addSuppressed(var82);
                            }
                        } else {
                            var5.close();
                        }
                    }

                }
            } catch (Throwable var91) {
                var4 = var91;
                throw var91;
            } finally {
                if (var3 != null) {
                    if (var4 != null) {
                        try {
                            var3.close();
                        } catch (Throwable var81) {
                            var4.addSuppressed(var81);
                        }
                    } else {
                        var3.close();
                    }
                }

            }
        }
    }

    private static boolean isCompressed(byte[] var0) {
        return var0[0] == 31 && var0[1] == -117;
    }

    private void indexedData(Map<String, Dataobject> var1, Dataobject var2) {
        String var3 = var2.getId();
        if (var3 != null) {
            int var4 = 1;
            var1.put(var3, var2);
            Iterator var5 = var2.getChildren().iterator();

            while(var5.hasNext()) {
                Dataobject var6 = (Dataobject)var5.next();
                var6.setParent(var2);
                String var7 = DataelementMapAdapter.getDataelementValue(var2, "level");
                String var8;
                String var9;
                if ("0".equals(var7)) {
                    var8 = String.valueOf(var4++);
                    var9 = "1";
                } else {
                    String var10 = DataelementMapAdapter.getDataelementValue(var2, "wbsId");
                    var8 = var10 + "." + var4++;
                    var9 = getDeepLevel(var8);
                }

                DataelementMapAdapter.setDataelementValue(var6, "seqId", String.valueOf(this._count));
                DataelementMapAdapter.setDataelementValue(var6, "wbsId", var8);
                DataelementMapAdapter.setDataelementValue(var6, "level", var9);
                ++this._count;
                this.indexedData(var1, var6);
            }
        }

    }

    private Map<String, Dataobject> getIndexedData(Dataobject var1) throws FoundationException {
        HashMap var2 = new HashMap();
        this._count = 1;
        DataelementMapAdapter.setDataelementValue(var1, "seqId", "0");
        DataelementMapAdapter.setDataelementValue(var1, "wbsId", "0");
        DataelementMapAdapter.setDataelementValue(var1, "level", "0");
        this.indexedData(var2, var1);
        return var2;
    }

    public void unAssignSequence(Context var1, List<String> var2) throws FoundationException {
        boolean var3 = this.startUpdateSession(var1);
        if (var2 != null && !var2.isEmpty()) {
            if (this._projectDataObjInfo == null || this._projectDataObjInfo.isEmpty()) {
                this._projectDataObjInfo = this.getSequenceData(var1);
            }

            String var5;
            for(Iterator var4 = var2.iterator(); var4.hasNext(); this._projectDataObjInfo.remove(var5)) {
                var5 = (String)var4.next();
                Dataobject var6 = (Dataobject)this._projectDataObjInfo.get(var5);
                if (var6 != null) {
                    Dataobject var7 = var6.getParent();
                    if (var7 != null) {
                        var7.getChildren().remove(var6);
                    }
                }
            }

            if (var3) {
                this.finishUpdateSession(var1);
            }
        }

    }

    public void assignSequence(Context var1, String var2, String var3, String var4, String var5, boolean var6) throws FoundationException {
        boolean var7 = this.startUpdateSession(var1);
        if (this._projectDataObjInfo == null || this._projectDataObjInfo.isEmpty()) {
            this._projectDataObjInfo = this.getSequenceData(var1);
        }

        if (this._projectDataObjInfo.containsKey(var3)) {
            throw new FoundationException("Object already present!!--->>" + var3);
        } else {
            boolean var8 = false;
            if (!this._projectDataObjInfo.isEmpty()) {
                Dataobject var9 = (Dataobject)this._projectDataObjInfo.get(var2);
                String var10 = DataelementMapAdapter.getDataelementValue(var9, "seqId");
                String var11 = null;
                boolean var12 = var5 != null && !var5.isEmpty();
                boolean var13 = var4 != null && !var4.isEmpty();
                Dataobject var20;
                if (var12) {
                    var20 = (Dataobject)this._projectDataObjInfo.get(var5);
                    var11 = DataelementMapAdapter.getDataelementValue(var20, "seqId");
                    var8 = true;
                } else if (var13) {
                    var20 = (Dataobject)this._projectDataObjInfo.get(var4);
                    var11 = DataelementMapAdapter.getDataelementValue(var20, "seqId");
                    var8 = true;
                } else if (var4 != null && var4.isEmpty()) {
                    var8 = true;
                } else {
                    int var14 = var9.getChildren().size();
                    if (var14 > 0) {
                        Dataobject var15 = (Dataobject)var9.getChildren().get(var14 - 1);
                        String var16 = DataelementMapAdapter.getDataelementValue(var15, "seqId");
                        var11 = String.valueOf(Integer.parseInt(var16) + 1);
                    } else {
                        var11 = String.valueOf(Integer.parseInt(var10) + 1);
                    }
                }

                var20 = new Dataobject();
                var20.setId(var3);
                if (!var8) {
                    var9.getChildren().add(var20);
                } else {
                    int var21 = 0;
                    if (var11 == null) {
                        var9.getChildren().add(var21, var20);
                    } else {
                        boolean var22 = false;

                        for(Iterator var17 = var9.getChildren().iterator(); var17.hasNext(); ++var21) {
                            Dataobject var18 = (Dataobject)var17.next();
                            String var19 = DataelementMapAdapter.getDataelementValue(var18, "seqId");
                            if (var19.equals(var11)) {
                                var22 = true;
                                break;
                            }
                        }

                        if (var13 && var22) {
                            ++var21;
                        }

                        var9.getChildren().add(var21, var20);
                    }
                }

                var20.setParent(var9);
                this._projectDataObjInfo.put(var3, var20);
                DataelementMapAdapter.setDataelementValue(var20, "seqId", var11);
                if (var6) {
                    DataelementMapAdapter.setDataelementValue(var20, "project", String.valueOf(var6));
                }
            } else {
                this._palDataObject.setId(var3);
                DataelementMapAdapter.setDataelementValue(this._palDataObject, "project", String.valueOf(var6));
            }

            if (var8) {
                this.getIndexedData(this._palDataObject);
            }

            if (var7) {
                this.finishUpdateSession(var1);
            }

        }
    }

    public void moveSequence(Context var1, String var2, Dataobject var3, String var4, String var5, String var6) throws FoundationException {
        if (var3 != null) {
            boolean var7 = this.startUpdateSession(var1);
            this._projectDataObjInfo = this.getSequenceData(var1);
            String var8 = var3.getId();
            List var9 = var3.getChildren();
            if ((var9 == null || var9.isEmpty()) && var6 == null) {
                var3 = (Dataobject)this._projectDataObjInfo.get(var8);
            }

            ArrayList var10 = new ArrayList();
            var10.add(var8);
            if (var6 != null && !var6.isEmpty()) {
                ProjectSequence var11 = new ProjectSequence(var1, var6);
                var11.unAssignSequence(var1, var10);
            } else {
                this.unAssignSequence(var1, var10);
            }

            Dataobject var21 = (Dataobject)this._projectDataObjInfo.get(var2);
            boolean var12 = var5 != null && !var5.isEmpty();
            boolean var13 = var4 != null && !var4.isEmpty();
            String var14 = null;
            boolean var15 = false;
            Dataobject var16;
            if (var12) {
                var16 = (Dataobject)this._projectDataObjInfo.get(var5);
                var14 = DataelementMapAdapter.getDataelementValue(var16, "seqId");
                var15 = true;
            } else if (var13) {
                if (var4.contains("_")) {
                    var4 = ServiceSave.getActualIdForTempId(var1, var4);
                }

                var16 = (Dataobject)this._projectDataObjInfo.get(var4);
                var14 = DataelementMapAdapter.getDataelementValue(var16, "seqId");
                var15 = true;
            } else if (var4 != null && var4.isEmpty()) {
                var15 = true;
            }

            if (!var15) {
                var21.getChildren().add(var3);
            } else {
                int var22 = 0;
                if (var14 == null) {
                    var21.getChildren().add(var22, var3);
                } else {
                    boolean var17 = false;

                    for(Iterator var18 = var21.getChildren().iterator(); var18.hasNext(); ++var22) {
                        Dataobject var19 = (Dataobject)var18.next();
                        String var20 = DataelementMapAdapter.getDataelementValue(var19, "seqId");
                        if (var20.equals(var14)) {
                            var17 = true;
                            break;
                        }
                    }

                    if (var13 && var17) {
                        ++var22;
                    }

                    var21.getChildren().add(var22, var3);
                }
            }

            var3.setParent(var21);
            if (var7) {
                this.finishUpdateSession(var1);
            }
        }

    }

    private static String getDeepLevel(String var0) {
        String var1 = "1";
        if (var0 != null && var0.lastIndexOf(".") != -1) {
            long var2 = var0.chars().filter((var0x) -> {
                return var0x == 46;
            }).count() + 1L;
            var1 = String.valueOf(var2);
        }

        return var1;
    }

    public Map<String, Dataobject> getSequenceData(Context var1) throws FoundationException {
        if (this._palDataObject == null) {
            this._palDataObject = this.getProjectSequenceAttribute(var1);
        }

        return this.getIndexedData(this._palDataObject);
    }

    public List<Dataobject> getProjects(Context context) throws FoundationException {
        Map<String, Dataobject> sequenceData = this.getSequenceData(context);
        Map var3 = (Map)sequenceData.entrySet().stream().filter((var0) -> {
            return "true".equalsIgnoreCase((String)((Dataobject)var0.getValue()).getDataelements().get("project"));
        }).collect(Collectors.toMap((var0) -> {
            return (String)var0.getKey();
        }, (var0) -> {
            return (Dataobject)var0.getValue();
        }));
        ArrayList var4 = new ArrayList();
        Set var5 = var3.keySet();
        var5.forEach((var2x) -> {
            var4.add(var3.get(var2x));
        });
        return var4;
    }
}
