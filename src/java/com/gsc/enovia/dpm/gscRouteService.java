package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUserException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ServiceParameters;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.UpdateActions;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.RelateddataMap;
import com.dassault_systemes.enovia.e6wv2.foundation.service.ServiceResource;
import com.dassault_systemes.enovia.util.RouteManagementConstants;
import com.gsc.dbConfig.dbSessionUtil;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.common.Route;
import com.matrixone.apps.common.Task;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.PersonUtil;
import com.matrixone.apps.framework.ui.UIUtil;
import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.StringList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.dassault_systemes.enovia.e6wv2.foundation.db.MQLConstants.SELECTABLE_CURRENT;
import static com.dassault_systemes.enovia.e6wv2.foundation.db.MQLConstants.SELECTABLE_TYPE;

public class gscRouteService {
    private static final String _namespace = "com.gsc.gscInterface.approvalIF";
    private static dbSessionUtil _session = new dbSessionUtil(_namespace);

    /**
     * <pre>
     *     결재 서버에 결재 요청 후 응답이 성공일 경우, 호출
     * </pre>
     * @param req Request
     * @param body Request Body
     * @return Response
     * @throws Exception
     */
    public static Response sendApproval(HttpServletRequest req, String body) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        ServiceParameters serviceParameters = new ServiceParameters();
        ServiceResource sr = new ServiceResource();
        matrix.db.Context context = sr.authenticateRequest(req, serviceParameters);
        sr.applySecurityContext(context, req, serviceParameters);
        System.out.println("결재 생성 context user >>>> " + context.getUser());

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(body);

        JSONObject approvalLine = (JSONObject) jsonObject.get("approvalLine");
        JSONArray approvers = (JSONArray) approvalLine.get("approver");
        JSONArray receivers = (JSONArray) approvalLine.get("receiver");

        List<Map<String, String>> userLists = new ArrayList<>();
        for (int i = 0; i < approvers.size(); i++) {
            Map approverInfo = (Map) approvers.get(i);
            if (approverInfo.get("approveType").equals("X")) {
                userLists.add(0, approverInfo);
            } else  userLists.add(approverInfo);
        }

        for (int j = 0; j < receivers.size(); j++) {
            Map receiverInfo = (Map) receivers.get(j);
            String checkReceiver = receiverInfo.get("approver").toString().substring(0, 2);
            userLists.add(receiverInfo);
        }

        String title = (String) jsonObject.get("title");
        String objectId = (String) jsonObject.get("objectId");
        //String uuId = (String) jsonObject.get("id");

        // 전달된 objectId와 연결된 Route가 있는지 조회, 그 Route가 시작하지 않은 경우 삭제 처리
        DomainObject contentObj = new DomainObject(objectId);
        StringList select = new StringList();
        select.add("physicalid");
        select.add("owner");
        select.add("current");

        MapList relRoute = contentObj.getRelatedObjects(context, "Object Route", "*", select, (StringList) null, true, true, (short) 1, (String) null, "", 0);
        if (!relRoute.isEmpty()) {
            Iterator relRouteItr = relRoute.iterator();
            while (relRouteItr.hasNext()) {
                Map routeInfo = (Map) relRouteItr.next();
                String routePhysicId = routeInfo.get("physicalid").toString();
                String owner = routeInfo.get("owner").toString();
                String routeCurrent = routeInfo.get("current").toString();
                System.out.println("route physic id >>> " + routePhysicId + " >>> " + routeCurrent);

                if (owner.equals(context.getUser()) && routeCurrent.equals("Define")) {
                    System.out.println("deleted >>> " + routePhysicId);
                    DomainObject delRoute = new DomainObject(routePhysicId);
                    delRoute.deleteObject(context);
                }
            }
        }

        ArrayList<Dataobject> routeNodeList = new ArrayList<>();
        for (int k = 0; k < userLists.size(); k++) {
            Map routeNodeInfo = (Map) userLists.get(k);
            System.out.println(routeNodeInfo.toString());
            String approveType = routeNodeInfo.get("approveType").toString();
            String approver = routeNodeInfo.get("approver").toString();

            // Route Object를 생성하기 위해 필요
            Dataobject approvalUser = new Dataobject();
            DataelementMap approvalAttr = new DataelementMap();

            if (approver.substring(0, 2).equals("C1") || approver.contains("project") || approver.contains("admin")) {
                String userPhysicId = PersonUtil.getPersonObjectPhysicalID(context, approver);
                approvalAttr.put("assigneeId", userPhysicId);
            } else {
                approvalAttr.put("assigneeId", approver);
            }

            //approvalAttr.put("taskOrder", String.valueOf(k+1));
            approvalAttr.putAll(setAssigneeAttribute(String.valueOf(k+1), approveType));
            approvalUser.setDataelements(approvalAttr);
            approvalUser.setType(approveType);
            routeNodeList.add(approvalUser);
        }

        Map result = createRoute(context, title, objectId, routeNodeList);
        int statusCode = Integer.parseInt(result.get("statusCode").toString());
        JSONObject resp = setResponseBody(statusCode, result.get("result"));

        return Response.status(statusCode).entity(resp).build();
    }

    /**
     * <pre>
     *     Route Object 생성 및 Object 상태를 시작 상태로 업데이트
     * </pre>
     * @param context Context
     * @param title Route Object 이름
     * @param objectId 결재 요청할 Content Physical Id
     * @param assigneeList 결재자 목록
     * @return Object 생성 결과를 담은 Map
     * @throws FoundationException
     */
    public static Map createRoute(Context context, String title, String objectId, ArrayList<Dataobject> assigneeList) throws FoundationException {
        Dataobject dataobject = setRouteDataObject(title, objectId, assigneeList);
        UpdateActions updateAction = dataobject.getUpdateAction();
        Map res = new HashMap();
        Dataobject newObject = dataobject;
        try {
            if (UpdateActions.CREATE.equals(updateAction)) {
                ContextUtil.startTransaction(context, true);
                String routeTitle = dataobject.getDataelements().get("name").toString();
                Map attr = dataobject.getDataelements();
                String contentId = attr.get("contentIds").toString();
                //attr.put("gscAppUUID", uuId);
                attr.remove("contentIds");
                String routePhysicId = com.dassault_systemes.enovia.route.Route.createRoute(context, dataobject, routeTitle, attr, null);
                dataobject.setId(routePhysicId);
                System.out.println("created new route object >>> " + routePhysicId);

                // 결재 요청 Content와 Relationship 연결
                Dataobject routeContent = new Dataobject();
                routeContent.setUpdateAction(UpdateActions.MODIFY);
                routeContent.setId(contentId);
                Datacollection newCollection = new Datacollection();
                newCollection.getDataobjects().add(routeContent);
                updateRouteContent(context, newCollection, dataobject);

                // 결재자로 지정된 사용자와 Relationship 연결
                RelateddataMap dM =  dataobject.getRelateddata();
                List<Dataobject> dataobjectList = dM.get("tasks");
                Datacollection newAssignCollection = new Datacollection();
                for (Dataobject relatedDataObject : dataobjectList) {
                    relatedDataObject.setUpdateAction(UpdateActions.CREATE);
                    newAssignCollection.getDataobjects().add(relatedDataObject);
                }
                Datacollection dc = updateRouteTasks(context, newAssignCollection, dataobject);

                String contentName = MqlUtil.mqlCommand(context, "print bus " + contentId + " select name dump |");
                int resultInsertLog = insertApprovalLog(context.getUser(), dataobject, contentName);
                //int resultInsertLog = 1;
                if (resultInsertLog > 0) {
                    res.put("statusCode", 200);
                    res.put("result", dataobject);
                }

                ContextUtil.commitTransaction(context);
            }
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            _session.rollback();
            e.printStackTrace();

            res.put("statusCode", 500);
            res.put("result", "Error : " + e.toString());
        }

        return res;
    }

    /**
     * <pre>
     *     결재자가 결재 요청을 승인 혹은 반려 시 호출
     *     Route Object의 gscAppUUID attribute를 기준으로 Object를 조회, Relationship 중 상태가 Assigned인 Inbox Task를 조회하여 상태 업데이트
     * </pre>
     * @param body Request Body
     * @return Response
     */
    public static Response updateRouteState(String body) {
        Dataobject res = null;
        Map result = new HashMap();
        Context context = null;
        try {
            System.out.println("request body >>> " + body);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(body);

            // 결재 상태 업데이트
            String docCompleteNo = (String) jsonObject.get("docCompleteNo");
            String approver = (String) jsonObject.get("user_id");
            String routeUuid = (String) jsonObject.get("uid");
            String approvalUser = (String) jsonObject.get("user_id");
            String approve_yn = (String) jsonObject.get("approve_yn");
            String update = (String) jsonObject.get("update");
            update = update.replace('-', '/');

            System.out.println("approver >>> " + approver);
            System.out.println("routeUuid >>> " + routeUuid);
            System.out.println("approvalUser >>> " + approvalUser);
            System.out.println("approve_yn >>> " + approve_yn);
            System.out.println("update >>> " + update);

            // Inbox Task의 상태는 Owner로 지정된 사람만 가능
            // 결재자 정보로 context 생성
            context = setNewContext(approver);
            System.out.println("set context user >>>> " + context.getUser());
            if (!approve_yn.equals("1")) {
                String routePhysicId = routeUuid;
                System.out.println("route physic id >>> " + routePhysicId);

                Route route = new Route(routePhysicId);
                StringList slBusSelect = new StringList(DomainObject.SELECT_PHYSICAL_ID);
                slBusSelect.add(Task.SELECT_CURRENT);
                slBusSelect.add(Task.SELECT_OWNER);

                MapList routeTaskList = route.getRouteTasks(context, slBusSelect, null, null, false);
                Iterator routeTaskItr = routeTaskList.iterator();
                while (routeTaskItr.hasNext()) {
                    Map routeTaskInfo = (Map) routeTaskItr.next();
                    String routeTaskId = routeTaskInfo.get("physicalid").toString();
                    String routeTaskCurrent = routeTaskInfo.get("current").toString();

                    if (routeTaskCurrent.equals("Assigned")) {
                        ContextUtil.startTransaction(context, true);

                        String routeTaskApprovalAction = approve_yn.equals("2") || approve_yn.equals("5") ? "Approve" : "Reject";
                        String routeTaskApprovalComments = routeTaskApprovalAction.equals("Approve") ? "승인" : "반려";
                        String state = "Complete";

                        Map attr = new HashMap();
                        attr.put(Task.ATTRIBUTE_APPROVAL_STATUS, routeTaskApprovalAction);
                        attr.put(Task.ATTRIBUTE_COMMENTS, routeTaskApprovalComments);

                        com.dassault_systemes.enovia.tskv2.Task.modifyTask(context, routeTaskId, attr, false, false);
                        com.dassault_systemes.enovia.tskv2.Task.setTaskState(context, routeTaskId, state, null, "Inbox Task");

                        ContextUtil.commitTransaction(context);

                        System.out.println("routeUuid >>> " + routeUuid);
                        System.out.println("approvalUser >>> " + approvalUser);
                        System.out.println("update >>> " + update);
                        System.out.println("String.format(\"'%s' 결재자가 요청을 %s하였습니다. (%s)\", approvalUser, routeTaskApprovalComments, docCompleteNo) >>> " + String.format("'%s' 결재자가 요청을 %s하였습니다. (%s)", approvalUser, routeTaskApprovalComments, docCompleteNo));

                        int res_updateApprovalLine = updateApprovalLine(routeUuid, approvalUser, update, approve_yn, "Success", String.format("'%s' 결재자가 요청을 %s하였습니다. (%s)", approvalUser, routeTaskApprovalComments, docCompleteNo));
                        System.out.println("res_updateApprovalLine >>> " + res_updateApprovalLine);

                        result.put("result", getTaskInfo(context, routeTaskId));
                    }
                }
            } else {
                // 결재 시작 상태로 업데이트
                updateApprovalLine(routeUuid, approvalUser, update, approve_yn, "Success", String.format("'%s' 사용자가 결재 요청을 상신하였습니다. (%s)", approvalUser, docCompleteNo));

                // 230504 HJ - Route Active 처리
                /*
                ContextUtil.pushContext(context);
                String objectIds = MqlUtil.mqlCommand(context,  String.format("print bus '%s' select to[Object Route].from.physicalid to[Object Route].physicalid dump |;",routeUuid));
                String objectId = objectIds.split("\\|")[0];
                String relId = objectIds.split("\\|")[1];
                String strMQLResult = MqlUtil.mqlCommand(context,  String.format("print bus '%s' select type current dump |;",objectId));
                String[] listMQLResult = strMQLResult.split("\\|");
                String objectType = listMQLResult[0];
                String objectCurrent = listMQLResult[1];
                if("Project Space".equals(objectType) && "Active".equals(objectCurrent)){
                    MqlUtil.mqlCommand(context,  String.format("mod connection '%s' 'Route Base State' state_Review;",relId));
                    MqlUtil.mqlCommand(context,  String.format("mod bus '%s' current Review;",objectId));
                }
                ContextUtil.popContext(context);
                 */
                // 230504 HJ - Route Active 처리

                com.dassault_systemes.enovia.route.Route.startRoute(context, routeUuid);
                DomainObject route = new DomainObject(routeUuid);
                route.setAttributeValue(context, "gscAppUUID", docCompleteNo); // 결재 조회를 위해 docCompleteNo를 attribute[gscAppUUID]에 업데이트함
            }

            result.put("statusCode", 200);
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
            result.put("statusCode", 500);
            result.put("result", "Error : " + e.toString());
        }

        System.out.println("res >>> " + result);
        int statusCode = Integer.parseInt(result.get("statusCode").toString());
        JSONObject resp = setResponseBody(statusCode, result.get("result"));

        return Response.status(statusCode).entity(resp).build();
    }

    public static void updateRouteContent(Context var0, Datacollection var5, Dataobject routeObject) throws Exception {
        String var6 = null;
        HashMap var7 = new HashMap();
        HashMap var8 = new HashMap();
        String var9 = "";

        Dataobject var12;
        for(int var11 = 0; var11 < var5.getDataobjects().size(); ++var11) {
            var12 = (Dataobject)var5.getDataobjects().get(var11);
            UpdateActions var13 = var12.getUpdateAction();
            if (UpdateActions.MODIFY.equals(var13)) {
                String var14 = var12.getId();
                var9 = var9 + var14 + "~";
            }
        }

        if (!"".equals(var9)) {
            var9 = var9.substring(0, var9.length() - 1);
            com.dassault_systemes.enovia.route.Route.validateScope(var0, routeObject, (Map)null, var9, false);
        }

        Datacollection var28 = new Datacollection();
        var12 = new Dataobject();
        var28.getDataobjects().add(var12);
        var12.setId(routeObject.getId());
        ArrayList var29 = new ArrayList();
        var29.add(RouteManagementConstants.SELECTABLE_ROUTE_OWNER);
        Dataobject var30 = ObjectUtil.print(var0, routeObject.getId(), null, var29);
        String var15 = DataelementMapAdapter.getDataelementValue(var30, RouteManagementConstants.SELECTABLE_ROUTE_OWNER.getName());
        boolean var16 = com.dassault_systemes.enovia.route.Route.checkCanAddContent(var0, var28, var15);
        String var17 = null;
        HashMap var18 = new HashMap(5);

        for(int var19 = 0; var19 < var5.getDataobjects().size(); ++var19) {
            Dataobject var20 = (Dataobject)var5.getDataobjects().get(var19);
            UpdateActions var21 = var20.getUpdateAction();
            String var22 = var20.getId();
            if (UIUtil.isNullOrEmpty(var22)) {
                var22 = var20.getIdentifier();
            }

            String var23 = routeObject.getId();
            var8.put(var22, var23);
            String var24;
            if ("stateBlock".equals(var17)) {
                var24 = var20.getRelId();
                DataelementMapAdapter.getDataelementValue(var20, "relId");
                String var25 = DataelementMapAdapter.getDataelementValue(var20, "RouteBaseState");
                String var26 = DataelementMapAdapter.getDataelementValue(var20, "policy");
                var7.put(var22, var24);
                com.dassault_systemes.enovia.route.Route.updateStateBlockingCondition(var0, var23, var24, var25, var26);
            } else if (UpdateActions.MODIFY.equals(var21)) {
                var18.clear();
                try {
                    if (!var16) {
                        var24 = com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil.getTranslatedValue(var0, "Components", "emxComponents.RouteMgmt.NoContentAddAccess", var0.getLocale());
                        throw new FoundationUserException(var24);
                    }

                    var24 = com.dassault_systemes.enovia.route.Route.addRouteContent(var0, var23, var22);
                    var7.put(var22, var24);
                } catch (Exception var27) {
                    throw new FoundationUserException(var27.getMessage());
                }
            } else if (UpdateActions.DELETE.equals(var21)) {
                com.dassault_systemes.enovia.route.Route.removeRouteContent(var0, var23, var22);
            } else if (UpdateActions.CREATE.equals(var21)) {
                if (!var16) {
                    var24 = com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil.getTranslatedValue(var0, "Components", "emxComponents.RouteMgmt.NoContentAddAccess", var0.getLocale());
                    throw new FoundationUserException(var24);
                }

                var24 = com.dassault_systemes.enovia.route.Route.addRouteContent(var0, var23, var22);
                var7.put(var22, var24);
            }
        }

        com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil.setAttribute(var0, "contentRouteDeleteMapping", var8);
        com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil.setAttribute(var0, "contentRouteMapping", var7);
        if ("Yes".equalsIgnoreCase(var6)) {
            String var31 = ((Dataobject)var5.getDataobjects().get(0)).getParent().getId();
            var5 = com.dassault_systemes.enovia.route.Route.updateRouteContents(var0, var31, var5);
        }
    }

    public static Datacollection updateRouteTasks(Context var0, Datacollection var7, Dataobject routeObject) throws Exception {
        String var5 = "-9"; // timezone
        String var6 = null;
        String var8 = "";
        String var9 = "";
        String var10 = PersonUtil.getPersonObjectPhysicalID(var0);
        String var11 = PersonUtil.getPersonObjectID(var0);
        HashMap var12 = new HashMap(5);

        int noneAssignee = 0;
        int originOrder = 0;
        for(int var13 = 0; var13 < var7.getDataobjects().size(); ++var13) {
            Dataobject var14 = (Dataobject)var7.getDataobjects().get(var13);
            UpdateActions var15 = var14.getUpdateAction();
            String var16 = var14.getId();
            String var17 = var14.getType();
            String var18 = routeObject.getId();
            var8 = var18;
            if (UIUtil.isNullOrEmpty(var17)) {
                var17 = DataelementMapAdapter.getDataelementValue(var14, "type");
            }

            String var19 = DataelementMapAdapter.getDataelementValue(var14, "taskOwnerReviewAction");
            String var20 = DataelementMapAdapter.getDataelementValue(var14, "taskOwnerReviewComments");
            if (!UpdateActions.CREATE.equals(var15) && !UpdateActions.MODIFY.equals(var15)) {
                if (UpdateActions.DELETE.equals(var15)) {
                    var9 = DataelementMapAdapter.getDataelementValue(var14, "assigneeId");
                    com.dassault_systemes.enovia.route.Route.removeRouteTask(var0, var18, var16, var9);
                    var7.getDataobjects().set(var13, null);
                } else if (UpdateActions.REVISE.equals(var15) && UIUtil.isNotNullAndNotEmpty(var19)) {
                    var14 = com.dassault_systemes.enovia.route.Route.applyTaskAction(var0, var18, var16, var19, var20);
                    var7.getDataobjects().set(var13, var14);
                }
            } else {
                var12.clear();
                //ServiceDataFunctions.fillUpdates(var0, var14, var2.getAutosaveFields(), var12);
                addToMap(var12, "Route Sequence", DataelementMapAdapter.getDataelementValue(var14, "taskOrder"));
                addToMap(var12, RouteManagementConstants.SELECTABLE_TASK_TITLE.getAttribute(), DataelementMapAdapter.getDataelementValue(var14, RouteManagementConstants.SELECTABLE_TASK_TITLE.getName()));
                addToMap(var12, "Route Action", DataelementMapAdapter.getDataelementValue(var14, RouteManagementConstants.SELECTABLE_TASK_ROUTE_ACTION.getName()));
                String var21 = DataelementMapAdapter.getDataelementValue(var14, "assigneeId");
                String var22 = DataelementMapAdapter.getDataelementValue(var14, "routeNodeRelIds");
                String var23;
                String var24;
                String var25;
                String var26;
                if (UpdateActions.CREATE.equals(var15)) {
                    var23 = (String)var12.getOrDefault("Route Owner Task", "False");
                    if ("contextUser".equals(var21) || !var11.equals(var21) && !var10.equalsIgnoreCase(var21) && "true".equalsIgnoreCase(var23)) {
                        var21 = var11;
                        DataelementMapAdapter.setDataelementValue(var14, "assigneeId", var10);
                    }

                    if ("Yes".equalsIgnoreCase(var6)) {
                        if (UIUtil.isNullOrEmpty(var21)) {
                            var24 = DataelementMapAdapter.getDataelementValue(var14, "taskAssigneeUsername");
                            if (UIUtil.isNotNullAndNotEmpty(var24)) {
                                var21 = PersonUtil.getPersonObjectPhysicalID(var0, var24);
                            }
                        }

                        var24 = DataelementMapAdapter.getDataelementValue(var14, "dueDateOffset");
                        var25 = DataelementMapAdapter.getDataelementValue(var14, "dateOffsetFrom");
                        var26 = (String)var12.getOrDefault("Scheduled Completion Date", (Object)null);
                        if (UIUtil.isNullOrEmpty(var26) && UIUtil.isNullOrEmpty(var24)) {
                            var12.put("Assignee Set Due Date", "Yes");
                        } else {
                            var12.put("Due Date Offset", var24);
                            if (UIUtil.isNotNullAndNotEmpty(var25)) {
                                var12.put("Date Offset From", var25);
                            } else {
                                var12.put("Date Offset From", "Route Start Date");
                            }
                        }

                        var12.put("ispublicservice", "Yes");
                    }
                    // assigneeId = DataelementMapAdapter.getDataelementValue(var14, "assigneeId");
                    String approveType = var14.getType();
                    MapList checkBU = DomainObject.findObjects(var0, "Business Unit", null, "name=='" + var21 + "'", new StringList("attribute[Title].value"));
                    String assigneeName = "";
                    if (checkBU.isEmpty()) {
                        var14.getDataelements().remove("assigneeId");

                        if (noneAssignee > 0) {
                            int taskOrder = Integer.parseInt(DataelementMapAdapter.getDataelementValue(var14, "Route Sequence"));
                            System.out.println("task order >>> " + taskOrder);
                            originOrder = taskOrder;
                            taskOrder = taskOrder - noneAssignee;

                            var14.getDataelements().put("Route Sequence", String.valueOf(taskOrder));
                        }

                        var14 = com.dassault_systemes.enovia.route.Route.addRouteTask(var0, var18, var21, var22, (Map)var14.getDataelements(), var5);
                        Map assignee = ServiceUtil.getObjectInfo(var0, "Person", "physicalId", var21, new StringList("name"));
                        assigneeName = assignee.get("name").toString();
                    } else {
                        // 지정된 결재자가 사용자가 아닌 부서인 경우
                        Iterator checkBuItr = checkBU.iterator();
                        while (checkBuItr.hasNext()) {
                            Map buInfo = (Map) checkBuItr.next();
                            assigneeName = buInfo.get("attribute[Title].value").toString();
                        }
                        noneAssignee++;
                    }

                    if (originOrder > 0)    var14.getDataelements().put("taskOrder", String.valueOf(originOrder));

                    // Log 저장
                    String order = DataelementMapAdapter.getDataelementValue(var14, "taskOrder") != null ? DataelementMapAdapter.getDataelementValue(var14, "taskOrder") : DataelementMapAdapter.getDataelementValue(var14, "Route Sequence");
                    insertApprovalLine(var18, Integer.parseInt(order), DataelementMapAdapter.getDataelementValue(routeObject, "name"), assigneeName, approveType);

                } else {
                    var23 = DataelementMapAdapter.getDataelementValue(var14, "isChangeAssignee");
                    var24 = DataelementMapAdapter.getDataelementValue(var14, "newAssigneeId");
                    var25 = DataelementMapAdapter.getDataelementValue(var14, "oldAssigneeId");
                    var26 = DataelementMapAdapter.getDataelementValue(var14, "reAssignComments");
                    String var27;
                    if (UIUtil.isNotNullAndNotEmpty(var23) && "true".equalsIgnoreCase(var23)) {
                        var27 = DataelementMapAdapter.getDataelementValue(var14, RouteManagementConstants.SELECTABLE_ROUTE_OWNER_TASK.getName());
                        if ("TRUE".equalsIgnoreCase(var27)) {
                            var25 = "contextUser";
                        }

                        var16 = DataelementMapAdapter.getDataelementValue(var14, "taskId");
                        var14 = com.dassault_systemes.enovia.route.Route.changeAssignee(var0, var17, var14, var16, var24, var26, var12, var25);
                    } else {
                        var27 = DataelementMapAdapter.getDataelementValue(var14, "routeNodeRelIds");
                        var14 = com.dassault_systemes.enovia.route.Route.modifyRouteTask(var0, var18, var17, var16, var21, var27, var12, false, var5);
                    }
                }

                var7.getDataobjects().set(var13, var14);
            }
        }

        return var7;
    }

    private static void addToMap(Map<String, String> var0, String var1, String var2) {
        if (var2 != null) {
            var0.put(var1, var2);
        }
    }

    /**
     * <pre>
     *     Route Object를 생성하기 위해 필요한 값들을 Dataobject에 담음
     * </pre>
     * @param title Route Object 이름
     * @param objectId Content Physical Id
     * @param assigneeList 결재자 정보
     * @return
     */
    private static Dataobject setRouteDataObject(String title, String objectId, ArrayList<Dataobject> assigneeList) {
        Dataobject dataobject = new Dataobject();
        DataelementMap dataelementMap = new DataelementMap();
        dataelementMap.put("name", title);
        dataelementMap.put("contentIds", objectId);
        dataelementMap.putAll(setRouteAttribute());
        dataobject.setDataelements(dataelementMap);
        dataobject.setUpdateAction(UpdateActions.CREATE);
        RelateddataMap relateddataMap = new RelateddataMap();
        relateddataMap.put("tasks", assigneeList);
        dataobject.setRelateddata(relateddataMap);

        return dataobject;
    }

    /**
     * <pre>
     *     결재 상태 업데이트 후 Inbox Task의 정보 조회
     * </pre>
     * @param context Context
     * @param id Inbox Task Id
     * @return Inbox Task의 정보를 담은 Dataobject
     * @throws FoundationException
     */
    private static Dataobject getTaskInfo(Context context, String id) throws FoundationException {
        ArrayList attr = new ArrayList(5);
        attr.add(SELECTABLE_TYPE);
        attr.add(SELECTABLE_CURRENT);
        attr.add(com.dassault_systemes.enovia.tskv2.Task.SELECT_ROUTE_TASK_NEEDS_REVIEW);
        attr.add(com.dassault_systemes.enovia.tskv2.Task.SELECT_ROUTE_TASK_APPROVAL_STATUS);
        attr.add(com.dassault_systemes.enovia.tskv2.Task.SELECT_ROUTE_TASK_ACTION);
        attr.add(com.dassault_systemes.enovia.tskv2.Task.SELECT_APPROVERS_COMMENTS);
        attr.add(com.dassault_systemes.enovia.tskv2.Task.SELECT_REVIEWERS_COMMENTS);
        Dataobject res = com.dassault_systemes.enovia.tskv2.Task.getTaskInfo(context, id, attr);
        return res;
    }

    /**
     * <pre>
     *     Route object를 생성하기 위해 attribute 값 세팅
     * </pre>
     * @return Attribute 정보 담은 Map
     */
    private static Map setRouteAttribute() {
        Map relAttr = new HashMap();
        relAttr.put("Demote On Rejection", "No");
        relAttr.put("Route Completion Action", "Promote Connected Object");
        relAttr.put("Auto Stop On Rejection", "Immediate");
        relAttr.put("Preserve Task Owner", "False");
        relAttr.put("Requires ESign", "False");
        relAttr.put("Route Base Purpose", "Standard");
        relAttr.put("Restrict Members", "Organization");

        return relAttr;
    }

    /**
     * <pre>
     *     Route object에 결재자 relationship을 연결하기 위해 필요한 attribute 값 세팅
     * </pre>
     * @param taskOrder 결재 순서
     * @return Attribute 정보 담은 Map
     */
    private static Map setAssigneeAttribute(String taskOrder, String approveType){
        DecimalFormat df = new DecimalFormat("00");
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(currentCalendar.DATE, 7);
        String year = Integer.toString(currentCalendar.get(Calendar.YEAR));
        String month = df.format(currentCalendar.get(Calendar.MONTH) + 1);
        String day = df.format(currentCalendar.get(Calendar.DATE));
        String newDate = String.format("%s/%s/%s", month, day, year);

        Map relAttr = new HashMap();
        relAttr.put("Title",  approveType.equals("V") ? "Notify Only" : "Waiting for Approval");
        relAttr.put("Allow Delegation", "FALSE");
        relAttr.put("Assignee Set Due Date", "No");
        relAttr.put("Choose Users From User Group", "False");
        relAttr.put("Date Offset From", "Route Start Date");
        relAttr.put("Due Date Offset", "");
        relAttr.put("Route Instructions", approveType.equals("V") ? "Notify Only" : "Waiting for Approval");
        relAttr.put("Review Comments Needed", "No");
        relAttr.put("Parallel Node Procession Rule", "All");
        relAttr.put("Review Task", "No");
        relAttr.put("Route Action", approveType.equals("V") ? "Notify Only" : "Approve");
        relAttr.put("Template Task", "No");
        relAttr.put("Route Sequence", taskOrder);
        relAttr.put("Scheduled Completion Date", newDate);

        return relAttr;
    }

    /**
     * <pre>
     *     결재 승인 혹은 반려 시, Inbox Task의 상태를 업데이트 하기 위해 필요한 context 세팅
     * </pre>
     * @param person 결재자 사번
     * @return Context
     * @throws MatrixException
     * @throws IOException
     */
    private static Context setNewContext(String person) throws Exception {
        // user_id 입력 Context 생성
        Context newContext = new matrix.db.Context("");
        newContext.setUser(person);
        newContext.setPassword("Qwer1234");
        newContext.setLocale(new Locale("en-US"));

        // 230427 HJ - API 호출 Uesr의 context로 입력 user_id 사용자 DefaultSecurityContext 조회 후, 입력
        String user_DefaultSecurityContext = com.matrixone.apps.domain.util.MqlUtil.mqlCommand(newContext, String.format("print person '%s' select property[preference_DefaultSecurityContext].value dump;",person));
        System.out.println("user_DefaultSecurityContext >>> " + user_DefaultSecurityContext);

        newContext.setRole("ctx::"+user_DefaultSecurityContext);
        newContext.setVault("eService Production");
        newContext.connect(true);

        // 'Error : com.dassault_systemes.enovia.e6wv2.foundation.FoundationException: XML: Message:User must have IFW license to run the kernel command Severity:3 ErrorCode:1600400, statusCode=500' 오류 발생하여 사용 하지 않음
        //Framework.setContext(req.getSession(), newContext);

        return newContext;
    }

    /**
     * <pre>
     *     결재 요청 시 ENOVIA_IF.APPROVAL_IF 테이블에 결재 요청 정보 추가
     * </pre>
     * @param requestUser 작성자
     * @param dataobject 결재 요청 정보
     * @param contentName Content 이름
     * @return Data 입력 결과
     * @throws Exception
     */
    private static int insertApprovalLog(String requestUser, Dataobject dataobject, String contentName) throws Exception {
        int executeSqlResult = 0;
        try {
            String app_id = dataobject.getId();
            String title = DataelementMapAdapter.getDataelementValue(dataobject, "name");
            Map<String, Object> insertData = new HashMap();
            insertData.put("app_id", app_id);
            insertData.put("title", title);
            insertData.put("contents", contentName);
            insertData.put("requester_id", requestUser);

            executeSqlResult = (int) _session.executeSql("insert", "insertApproval", insertData);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error : " + e.getMessage());
        }

        return executeSqlResult;
    }

    /**
     * <pre>
     *     결재 요청 시 ENOVIA_IF.APPROVAL_LINE_IF 테이블에 결재자 정보 추가
     * </pre>
     * @param app_id Route Object Physical Id
     * @param order 결재 순서
     * @param title Route Object 이름
     * @param requestUser 결재자
     * @return Data 입력 결과
     * @throws Exception
     */
    private static int insertApprovalLine(String app_id, int order, String title, String requestUser, String approveType) throws Exception {
        int res = 0;
        try {
            Map insertData = new HashMap();
            insertData.put("app_id", app_id); // uuid
            insertData.put("app_seq", order);
            insertData.put("app_type", approveType); // approve type
            insertData.put("app_title", title);
            insertData.put("user_id", requestUser);

            res = (int) _session.executeSql("insert", "insertApprovalLine", insertData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error : " + e.getMessage());
        }

        return res;
    }

    /**
     * <pre>
     *     결재 승인 혹은 반려 시, ENOVIA_IF.APPROVAL_LINE_IF 테이블에 해당 데이터 업데이트
     * </pre>
     * @param routeId Route Object PhysicalId
     * @param approvalUser 결재자
     * @param updated 결재 일시
     * @param approveYN 결재 상태 (2: 승인, 6: 반려)
     * @param if_result 업데이트 결과 (Success / Fail)
     * @param if_msg 업데이트 결과 메세지
     * @return Data 업데이트 결과
     * @throws Exception
     */
    private static int updateApprovalLine(String routeId, String approvalUser, String updated, String approveYN, String if_result, String if_msg) throws Exception {
        int res = 0;
        try {
            SimpleDateFormat updatedFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            SimpleDateFormat newSQLFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
            Date updatedDate = updatedFormat.parse(updated);
            System.out.println("update date >>> " + updatedDate);

            updated = newSQLFormat.format(updatedDate);
            System.out.println("new updated date >>> " + updated);

            Map<String, Object> param = new HashMap();
            param.put("app_id", routeId);
            param.put("user_id", approvalUser);
            param.put("end_date", updated);
            param.put("approve_yn", approveYN);
            param.put("if_result", if_result);
            param.put("if_msg", if_msg);

            res = (int) _session.executeSql("update", "updateApprovedColumn", param);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error : " + e.getMessage());
        }

        return res;
    }

    /**
     * <pre>
     *     API 호출 후 응답 메세지 세팅
     * </pre>
     * @param statusCode 응답 코드
     * @param result 응답 결과
     * @return Json Object
     */
    private static JSONObject setResponseBody(int statusCode, Object result) {
        JSONObject resp = new JSONObject();
        JSONArray arr = new JSONArray();

        if (statusCode == 200) {
            JSONObject csrf = new JSONObject();
            csrf.put("name", "ENO_CSRF_TOKEN");
            csrf.put("value", "");

            resp.put("csrf", csrf);
            resp.put("items", 1);

            if (result != null) {
                JSONArray dataObjArr = new JSONArray();
                dataObjArr.add(result);
                resp.put("data", dataObjArr);
            } else {
                resp.put("data", arr);
            }

        } else {
            resp.put("error", result);
            resp.put("data", arr);
        }

        resp.put("success", statusCode == 200 ? true : false);
        resp.put("statusCode", statusCode);
        resp.put("definitions", arr);

        System.out.println("resp >>> " + resp);

        return resp;
    }
}
