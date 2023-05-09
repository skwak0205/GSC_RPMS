package com.gsc.enovia.common;


import com.dassault_systemes.platform.restServices.RestService;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.json.JSONArray;
import com.matrixone.json.JSONObject;
import com.matrixone.servlet.Framework;
import com.matrixone.vplm.posmodel.VPLMSecurityContext;
import java.io.InputStream;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import matrix.db.Context;
import matrix.util.StringList;

public class gscRestAPIUtil extends RestService {
    public gscRestAPIUtil() {
    }

    public String getCacheId(String cacheId, String objectId) throws Exception {
        return cacheId + "_" + objectId;
    }

    public Context getContext(HttpServletRequest request) throws Exception {
        try {
            Context context = Framework.getContext(request);
            return context;
        } catch (Exception var3) {
            var3.printStackTrace();
            throw var3;
        }
    }

    public static JSONArray convertStringList2JsonArray(StringList strList, String sKey) throws Exception {
        try {
            JSONArray array = new JSONArray();
            JSONObject json = null;
            ListIterator itr = strList.listIterator();

            while(itr.hasNext()) {
                json = new JSONObject();
                array.put(json);
                json.put(sKey, (String)itr.next());
            }

            return array;
        } catch (Exception var6) {
            var6.printStackTrace();
            throw var6;
        }
    }
    public static JSONArray convertString2JsonArray(String str, String separator) throws Exception {
        try {
            JSONArray array = new JSONArray();
            JSONObject json = null;
            String[] strList = str.split(separator);
            for (int i = 0; i < strList.length; i++) {
                array.put(strList[i]);
            }
            return array;
        } catch (Exception var6) {
            var6.printStackTrace();
            throw var6;
        }
    }

    public static JSONArray convertMapList2JsonArray(MapList mapList, StringList slSelect, StringList slKey) throws Exception {
        try {
            JSONArray array = new JSONArray();
            JSONObject json = null;
            Map space = null;
            String sSelectKey = null;
            String sSelectValue = null;
            String sInputKey = null;
            Iterator iterSelect = null;
            ListIterator itr = mapList.listIterator();

            while(itr.hasNext()) {
                json = new JSONObject();
                array.put(json);
                space = (Map)itr.next();

                for(int k = 0; k < slSelect.size(); ++k) {
                    sSelectKey = (String)slSelect.get(k);
                    sSelectValue = convertString(space.get(sSelectKey));
                    sInputKey = (String)slKey.get(k);
                    json.put(sInputKey, sSelectValue);
                }

                System.out.println("json: " + json.toString());
            }

            return array;
        } catch (Exception var13) {
            var13.printStackTrace();
            throw var13;
        }
    }

    private Context getLoginContext(HttpServletRequest request) throws Exception {
        Context context = this.getAuthenticatedContext(request, true);
        String sRole = context.getRole();
        if (sRole == null || sRole.isEmpty()) {
            String sPreferredCtx = VPLMSecurityContext.getPreferredSecurityContext(context, context.getUser());
            if (sPreferredCtx != null && !sPreferredCtx.isEmpty()) {
                sPreferredCtx = "ctx::" + sPreferredCtx;
                context.resetRole(sPreferredCtx);
            } else {
                System.out.println("No preferred security context defined for person " + context.getUser());
            }
        }

        return context;
    }

    public Properties getProperties(String fileName) {
        Properties pro = new Properties();
        String resource = fileName;

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
            pro.load(is);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return pro;
    }

    public static String convertString(Object object) throws Exception {
        try {
            if (object == null) {
                return "";
            } else if (object instanceof String) {
                return (String)object;
            } else {
                return object instanceof StringList ? ((StringList)object).toString() : object.toString();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            throw var3;
        }
    }

    public StringList convertStringList(Object object) throws Exception {
        try {
            StringList slReturn = new StringList();
            if (object == null) {
                slReturn.add("");
            } else if (object instanceof String) {
                slReturn.add((String)object);
            } else if (object instanceof StringList) {
                slReturn = (StringList)object;
            } else {
                slReturn.add(object.toString());
            }

            return slReturn;
        } catch (Exception var3) {
            var3.printStackTrace();
            throw var3;
        }
    }

    public StringList getBasicSelectList() throws Exception {
        return new StringList(new String[]{"id", "type", "name", "revision", "current", "description"});
    }
}
