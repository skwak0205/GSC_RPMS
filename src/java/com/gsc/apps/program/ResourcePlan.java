//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.apps.program;

import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ResourcePlan extends DomainObject {
    public ResourcePlan() throws Exception {
    }

    public ResourcePlan(String var1) throws Exception {
        super(var1);
    }

    public String cloneResourcePlan(Context var1, Map var2) throws MatrixException {
        try {
            String[] var3 = new String[1];
            var3 = JPO.packArgs(var2);
            String var4 = "";
            var4 = (String)JPO.invoke(var1, "emxResourcePlan", (String[])null, "cloneResourcePlan", var3, String.class);
            String var5 = "success";
            return var5;
        } catch (Exception var6) {
            var6.printStackTrace();
            throw new MatrixException(var6);
        }
    }

    public ResourceRequest copyRequest(Context var1, String var2, String var3) throws MatrixException {
        try {
            String[] var4 = new String[1];
            HashMap var5 = new HashMap();
            var5.put("projectId", var2);
            var5.put("requestId", var3);
            var4 = JPO.packArgs(var5);
            return (ResourceRequest)JPO.invoke(var1, "emxResourceRequest", (String[])null, "copyRequest", var4, ResourceRequest.class);
        } catch (Exception var6) {
            var6.printStackTrace();
            throw new MatrixException(var6);
        }
    }

    public static MapList getLeafNodes(Context var0, String var1, String var2, String var3, String var4, StringList var5, StringList var6) throws Exception {
        MapList var7 = new MapList();
        StringBuffer var8 = new StringBuffer(100);

        try {
            int var9 = 1;
            ArrayList var10 = new ArrayList();
            var8.append("expand bus $" + var9++);
            var10.add(var1);
            if ("from".equalsIgnoreCase(var3)) {
                var8.append(" from ");
            } else if ("to".equalsIgnoreCase(var3)) {
                var8.append(" to ");
            }

            var8.append(" rel $" + var9++);
            var10.add("\"" + var2 + "\" ");
            if ("end".equalsIgnoreCase(var4)) {
                var8.append(" recurse to end ");
            } else {
                var8.append(" recurse to rel ");
            }

            var8.append(" terse ");
            int var11 = var5.size();
            int var12;
            if (var11 > 0) {
                var8.append(" select bus ");

                for(var12 = 0; var12 < var11; ++var12) {
                    var8.append(var9++);
                    var10.add((String)var5.get(var12));
                }
            }

            var11 = var6.size();
            if (var11 > 0) {
                var8.append(" select rel ");

                for(var12 = 0; var12 < var11; ++var12) {
                    var8.append(var9++);
                    var10.add((String)var6.get(var12));
                }
            }

            var8.append(" dump $" + var9++);
            var10.add("|");
            String var25 = MqlUtil.mqlCommand(var0, var8.toString(), var10);
            var11 = var5.size() + var6.size();
            StringList var13 = new StringList(var5);
            var13.addAll(var6);
            String[] var14 = var25.split("\\n");
            //char var15 = '\\';
            Pattern var16 = Pattern.compile( "\\|");
            new HashMap();
            String[] var18 = var14;
            int var19 = var14.length;

            for(int var20 = 0; var20 < var19; ++var20) {
                String var21 = var18[var20];
                if (var21 == null || "null".equals(var21) || "".equals(var21)) {
                    var7 = new MapList();
                    break;
                }

                String[] var22 = var16.split(var21, -1);
                HashMap var17 = new HashMap();
                var17.put("level", var22[0]);
                var17.put("relationship", var22[1]);
                var17.put("direction", var22[2]);
                var17.put("id", var22[3]);

                for(int var23 = 0; var23 < var11; ++var23) {
                    var17.put((String)var13.get(var23), var22[var23 + 4]);
                }

                var7.add(var17);
            }

            return var7;
        } catch (Exception var24) {
            throw var24;
        }
    }
}
