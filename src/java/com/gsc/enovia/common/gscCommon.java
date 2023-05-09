package com.gsc.enovia.common;

import com.dassault_systemes.enovia.enterprisechangemgt.util.ChangeUtil;
import com.dassault_systemes.platform.restServices.RestService;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.json.JSONArray;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.matrixone.json.JSONObject;
import com.matrixone.vplm.posmodel.VPLMSecurityContext;
import matrix.db.JPO;
import matrix.util.StringList;
import org.apache.commons.lang3.StringUtils;

@Path("/common")
public class gscCommon extends RestService {
    @Context
    private HttpServletResponse httpServletResponse;
    @Context
    private HttpServletRequest httpServletRequest;

    public gscCommon() {
    }

    @GET
    @Path("/invokeJPO")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response invokeJPO(@QueryParam("program") String programExpr, @QueryParam("oid") String objectId, @QueryParam("pushContext") String pushContext, @QueryParam("selectBasic") String selectBasic, @QueryParam("basicTarget") String basicTarget, @QueryParam("basicConvert") String basicConvert, @QueryParam("selectExpr") String selectExpr, @QueryParam("convertExpr") String convertExpr, @QueryParam("selectRelExpr") String selectRelExpr, @QueryParam("convertRelExpr") String convertRelExpr, @QueryParam("saveCache") String saveCache) throws Exception {
        String outputStr = "";
        matrix.db.Context context = null;

        try {
            if (StringUtils.isEmpty(programExpr) || ":".equals(programExpr)) {
                return Response.status(200).entity(outputStr).build();
            }

            gscRestAPIUtil util = new gscRestAPIUtil();
            context = util.getContext(this.httpServletRequest);
            StringList slProgram = FrameworkUtil.splitString(programExpr, ",");
            String sTempProgramExpr = null;
            StringList slTempProgram = null;
            StringList slSelect = FrameworkUtil.splitString(selectExpr, ",");
            StringList slConvert = FrameworkUtil.splitString(convertExpr, ",");
            StringList slRelSelect = FrameworkUtil.splitString(selectRelExpr, ",");
            StringList slRelConvert = FrameworkUtil.splitString(convertRelExpr, ",");
            String sBasicTarget;
            String sBasicSelect;
            String array;
            if ("TRUE".equalsIgnoreCase(selectBasic)) {
                StringList slBasicSelect = util.getBasicSelectList();
                StringList slBasicTarget = FrameworkUtil.splitString(basicTarget, ",");
                StringList slBasicConvert = FrameworkUtil.splitString(basicConvert, ",");
                slSelect.addAll(slBasicSelect);
                slConvert.addAll(slBasicSelect);
                sBasicTarget = null;
                sBasicSelect = null;
                array = null;

                for(int k = 0; k < slBasicTarget.size(); ++k) {
                    sBasicTarget = (String)slBasicTarget.get(k);
                    array = (String)slBasicConvert.get(k);

                    for(int m = 0; m < slBasicSelect.size(); ++m) {
                        sBasicSelect = (String)slBasicSelect.get(m);
                        slSelect.add(sBasicTarget + sBasicSelect);
                        slConvert.add(array + sBasicSelect);
                    }
                }
            }

            MapList mlReturn = new MapList();
            Map paramMap = new HashMap();
            Enumeration en = this.httpServletRequest.getParameterNames();
            sBasicTarget = null;
            sBasicSelect = null;

            while(en.hasMoreElements()) {
                sBasicTarget = (String)en.nextElement();
                sBasicSelect = this.httpServletRequest.getParameter(sBasicTarget);
                paramMap.put(sBasicTarget, sBasicSelect);
            }

            paramMap.put("objectId", objectId);
            paramMap.put("slSelect", slSelect);
            paramMap.put("slRelSelect", slRelSelect);

            for(int k = 0; k < slProgram.size(); ++k) {
                sTempProgramExpr = (String)slProgram.get(k);
                slTempProgram = FrameworkUtil.splitString(sTempProgramExpr, ":");
                if (slTempProgram == null || slTempProgram.size() < 2) {
                    throw new Exception("program parameter is not proper. program : " + programExpr);
                }

                String sProgram = (String)slTempProgram.get(0);
                String sMethod = (String)slTempProgram.get(1);

                try {
                    if ("TRUE".equalsIgnoreCase(pushContext)) {
                        ContextUtil.pushContext(context);
                    }

                    mlReturn.addAll((Collection)JPO.invoke(context, sProgram, (String[])null, sMethod, JPO.packArgs(paramMap), MapList.class));
                } catch (Exception var35) {
                    var35.printStackTrace();
                    throw var35;
                } finally {
                    if ("TRUE".equalsIgnoreCase(pushContext)) {
                        ContextUtil.popContext(context);
                    }

                }
            }

            if ("TRUE".equalsIgnoreCase(saveCache)) {
                HttpSession session = this.httpServletRequest.getSession();
                session.setAttribute(util.getCacheId(programExpr, objectId), mlReturn);
            }

            slSelect.addAll(slRelSelect);
            slConvert.addAll(slRelConvert);
            slConvert = slConvert.size() == 0 ? slSelect : slConvert;
            array = null;
            JSONArray jsonArray;
            if (slSelect.size() == 1) {
                StringList slReturn = (new ChangeUtil()).getStringListFromMapList(mlReturn, selectExpr);
                jsonArray = util.convertStringList2JsonArray(slReturn, convertExpr);
            } else {
                jsonArray = util.convertMapList2JsonArray(mlReturn, slSelect, slConvert);
            }

            outputStr = jsonArray.toString();
        } catch (Exception var37) {
            var37.printStackTrace();
        }

        System.out.println("JSONObject: " + outputStr);
        return Response.status(200).entity(outputStr).build();
    }

    @GET
    @Path("/getMyProfile")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response getMyProfile(@QueryParam("userId") String userId) throws Exception {
        String outputStr = "";
        matrix.db.Context context = null;
        JSONObject respObject = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            System.out.println("userId: " + userId);

            gscRestAPIUtil util = new gscRestAPIUtil();
            context = util.getContext(this.httpServletRequest);
//            String userId = context.getUser();
            StringList objectSelects = new StringList(2);
            objectSelects.addElement("id");
            objectSelects.addElement("attribute[First Name]");
            objectSelects.addElement("attribute[Last Name]");
            objectSelects.addElement("attribute[Country]");
            objectSelects.addElement("attribute[Email Address]");
            objectSelects.addElement("id");
            objectSelects.addElement("name");
            objectSelects.addElement("current");

            PersonUtil personUtil = new PersonUtil();
//            String defaultSecurityContext = personUtil.getDefaultSecurityContext(context);
            DomainObject personObj = personUtil.getPersonObject(context,userId);
            Map infoMap = personObj.getInfo(context,objectSelects);
            String objectId = (String)infoMap.get("id");
            String firstName = (String)infoMap.get("attribute[First Name]");
            String lastName = (String)infoMap.get("attribute[Last Name]");
            String email = (String)infoMap.get("attribute[Email Address]");
            String country = (String)infoMap.get("attribute[Country]");
            String status = (String)infoMap.get("current");
            String company = "GS Caltex";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("objectId", objectId);
            jsonObject.put("userId", userId);
            jsonObject.put("username", userId);
            jsonObject.put("fullname", firstName+lastName);
            jsonObject.put("email", email);
            jsonObject.put("company", company);
            jsonObject.put("country", country);
            jsonObject.put("status", status);
            String deptCode = MqlUtil.mqlCommand(  context,String.format("print bus %s select to[Member|from.type=='Business Unit'].from.name dump",objectId));
            String deptName = MqlUtil.mqlCommand(  context,String.format("print bus %s select to[Member|from.type=='Business Unit'].from.attribute[Title] dump",objectId));
            String groups = MqlUtil.mqlCommand(  context,String.format("print bus %s select to[Group Member].from.name dump",objectId));
            String products = MqlUtil.mqlCommand(  context,String.format("print person %s select product dump",userId));
            deptCode = "null".equals(deptCode) ? "" : deptCode;
            deptName = "null".equals(deptName) ? "" : deptName;
            jsonObject.put("deptCode", deptCode);
            jsonObject.put("deptName", deptName);

            jsonObject.put("roles", gscRestAPIUtil.convertString2JsonArray(groups,","));
            jsonObject.put("products", gscRestAPIUtil.convertString2JsonArray(products,","));

            String sPreferredCtx = VPLMSecurityContext.getPreferredSecurityContext(context, userId);
            jsonObject.put("securityContext", sPreferredCtx);
            String projectCount = MqlUtil.mqlCommand(context, String.format("eval expr 'count true' on query connection type Member where \"to.id==%s&&from.type=='Project Space'\"",objectId));
            String taskCount = MqlUtil.mqlCommand(context, String.format("eval expr 'count true' on query connection type 'Assigned Tasks' where \"from.id==%s\"",objectId));
            jsonObject.put("projectCount", projectCount);
            jsonObject.put("taskCount", taskCount);
            array.put(jsonObject);
            respObject.put("data", array);
            respObject.put("success", true);
            respObject.put("statusCode", 200);

            outputStr = respObject.toString();
        } catch (Exception var17) {
            var17.printStackTrace();
            respObject.put("data", array);
            respObject.put("success", true);
            respObject.put("statusCode", 200);
            outputStr = respObject.toString();
        }

        System.out.println(outputStr);
        return Response.status(200).entity(outputStr).build();
    }

    private matrix.db.Context getLoginContext(HttpServletRequest request) throws Exception {
        matrix.db.Context context = this.authenticate(request);
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

}
