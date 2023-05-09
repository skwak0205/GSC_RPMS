import com.dassault_systemes.platform.ven.jackson.databind.ObjectMapper;
import com.gsc.dbConfig.dbConnectionUtil;
import com.gsc.dbConfig.dbSessionUtil;
import com.gsc.dbConfig.utils;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.common.BusinessUnit;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.common.util.AttributeUtil;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.DomainSymbolicConstants;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UIUtil;
import matrix.db.Context;
import matrix.util.LicenseUtil;
import matrix.util.StringList;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class gscUserInterface_mxJPO {
    private static final String _namespace = "com.gsc.gscInterface.userIF";
    private static dbSessionUtil session = new dbSessionUtil(_namespace);
    private static Properties prop;
    public static final String ADD_CONNECTION = "add connection $1 from $2 to $3";
    public static final String MOD_CONNECTION_ID1 = "mod connection $1 $2 $3";
    public static final String SYMB_SEP = "|";

    public static void main(String[] args) throws Exception {
        Context context = utils.getContext();
        executeUserInterface(context, null);
    }

    /**
     * <pre>
     *     사용자 인터페이스 실행
     * </pre>
     *
     * @param context context
     * @param args    arguments
     */
    public static void executeUserInterface(Context context, String[] args) {
        String empId = null;
        try {
            prop = com.gsc.dbConfig.dbConnectionUtil.getProperty();
            Map<String, Object> param = new HashMap<>();
            String today = session.getDate();
            param.put("date", today);
            System.out.println(ServiceUtil.printLog("[SQL] List User since updated >= " + today));

            MapList empList = (MapList) session.executeSql("list", "selectUserList", param);
            StringList select = new StringList();
            select.add("id");
            select.add("name");
            select.add("attribute[Email Address].value");
            select.add("attribute[First Name].value");
            select.add("attribute[Last Name].value");
            select.add("to[Business Unit Employee].from.name");
            select.add("to[Member].from[Business Unit].name");
            select.add("current");

            int count = 0;
            String if_msg = "";

            for (int i = 0; i < empList.size(); i++) {
                Map userInfo = (Map) empList.get(i);
                empId = userInfo.get("EMP_ID").toString().trim();
                String curName = userInfo.get("CUR_NAME").toString().trim();

                if (curName.equals("퇴직")) {
                    // 퇴직자인 경우, Inactive 처리
                    if_msg = inactiveUser(context, empId, true);

                } else {
                    MapList checkUser = DomainObject.findObjects(context, "Person", null, "name==" + empId, select);
                    if (checkUser.isEmpty()) {
                        // 사용자 생성
                        String strPersonObjectId = createUser(context, userInfo);
                        if_msg = "'" + empId + "' 사용자가 생성되었습니다.";

                        if (strPersonObjectId != null) {
                            ContextUtil.startTransaction(context, true);
                            updateHasMembers(context, empId);

                            // company에 사용자 연결
                            String companyOid = ServiceUtil.getObjectInfo(context, DomainConstants.TYPE_COMPANY, "name", "Company Name", null).get("id").toString();
                            MqlUtil.mqlCommand(context
                                    , ADD_CONNECTION
                                    , true
                                    , DomainConstants.RELATIONSHIP_EMPLOYEE
                                    , companyOid // to
                                    , strPersonObjectId);

                            String strDeptId = userInfo.get("DEPT_ID").toString();
                            Map buInfo = ServiceUtil.getObjectInfo(context, DomainConstants.TYPE_BUSINESS_UNIT, "name", strDeptId, null);
                            String strOrganizationId = buInfo.get("id").toString();

                            // BU,Department에 사용자 연결
                            if (UIUtil.isNotNullAndNotEmpty(strOrganizationId)) {
                                if (!DomainConstants.TYPE_DEPARTMENT.equals("Business Unit")) {
                                    MqlUtil.mqlCommand(context
                                            , ADD_CONNECTION
                                            , true
                                            , DomainConstants.RELATIONSHIP_BUSINESS_UNIT_EMPLOYEE
                                            , strOrganizationId // to
                                            , strPersonObjectId);
                                }
                                String strMemberRelIdBusUnit = MqlUtil.mqlCommand(context
                                        , "add connection $1 from $2 to $3 select $4 dump $5"
                                        , true
                                        , DomainConstants.RELATIONSHIP_MEMBER
                                        , strOrganizationId // to
                                        , strPersonObjectId
                                        , DomainConstants.SELECT_ID
                                        , SYMB_SEP);

                                Map mapRole = getRole(context, new String[]{});
                                String strRoles = NVL(mapRole.get("RoleString"));
                                StringList slRoleList = (StringList) mapRole.get("RoleStringList");
                                if (slRoleList.size() > 0) {
                                    AttributeUtil.setAttributeList(context
                                            , new DomainRelationship(strMemberRelIdBusUnit)
                                            , DomainObject.ATTRIBUTE_PROJECT_ROLE
                                            , slRoleList
                                            , false
                                            ,
                                            "~");
                                }
                            }
                            ContextUtil.commitTransaction(context);

                            ContextUtil.startTransaction(context, true);
                            Person person = new Person(strPersonObjectId);
                            setSecurityContext(context, person, userInfo);
                            ContextUtil.commitTransaction(context);
                            System.out.println(ServiceUtil.printLog("[CREATE] New Person Object for " + empId + " -> " + strPersonObjectId));
                        }
                    } else {
                        if_msg = updateUser(context, checkUser, userInfo);
                    }
                }

                // 인터페이스 실행이 성공적으로 완료 될 시, ENOVIA_IF.USER_IF의 IF_YN (Y), IF_MSG, IF_RESULT (Success) 컬럼 업데이트
                session.updateIF(null, "Y", "Success", if_msg, empId);
                count++;
            }

            System.out.println(ServiceUtil.printLog("[TOTAL] Executed Count : " + count));

        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();

            // 인터페이스 실행에 오류가 발생할 시, ENOVIA_IF.USER_IF의 IF_YN (N), IF_MSG, IF_RESULT (Fail) 컬럼 업데이트
            session.updateIF(null, "N", "Fail", e.toString(), empId);
        }
    }

    /**
     * <pre>
     *     사용자 3dspace에 생성
     * </pre>
     *
     * @param context  context
     * @param userInfo 생성할 사용자 정보
     */
    public static String createUser(Context context, Map userInfo) {
        String res = null;
        try {
            ContextUtil.startTransaction(context, true);
            Map mapRole = getRole(context, new String[]{});
            String strRoles = NVL(mapRole.get("RoleString"));
            StringList slRoleList = (StringList) mapRole.get("RoleStringList");

            String strEmpId = userInfo.get("EMP_ID").toString();
            String strEmpName = userInfo.get("EMP_NAME").toString();
            String strDeptId = userInfo.get("DEPT_ID").toString();
            String strEmail = userInfo.get("E_MAIL").toString().equals("null") ? null : userInfo.get("E_MAIL").toString();
            String strPosition = userInfo.get("POSITION_NAME").toString();

            // Use the utility function to create the person, with the employee role.
            String strPersonObjectId = Person.createPersonInCompanyVault(context
                    , strEmpId
                    , "Qwer1234"
                    , strRoles
                    , strEmail
                    , PropertyUtil.getSchemaProperty(context, DomainSymbolicConstants.SYMBOLIC_vault_eServiceProduction));

            StringList slGroupList = new StringList();
            String companyOid = ServiceUtil.getObjectInfo(context, DomainConstants.TYPE_COMPANY, "name", "Company Name", null).get("id").toString();
            ComponentsUtil.assignPersonToGroups(context, companyOid, strEmpId, slGroupList);

            // 사용자 속성값 update
            Person person = new Person(strPersonObjectId);
            Map attr = new HashMap();
            attr.put("First Name", strEmpName); // attribute[First Name]에는 사용자 이름이 들어감
            attr.put("Last Name", strPosition); // attribute[Last Name]에는 사용자 직책이 들어감
            attr.put("Email Address", strEmail);

            person.setAttributeValues(context, attr);
            person.promote(context);
            ContextUtil.commitTransaction(context);

            // 3DPassport 사용자 생성
            attr.put("name", strEmpId);
            sendPassportUser(attr, prop.getProperty("3DPASSPORT_URL"), "register");
            System.out.println(ServiceUtil.printLog("[CREATE] New 3DPassport User -> " + strEmpId));

            res = strPersonObjectId;
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
        }

        return res;
    }

    /**
     * <pre>
     *     사용자 정보 업데이트
     * </pre>
     *
     * @param context   context
     * @param checkUser Person Object 정보
     * @param userInfo  변경된 사용자 정보
     * @return
     */
    private static String updateUser(Context context, MapList checkUser, Map userInfo) {
        String if_msg = "";
        try {
            ContextUtil.startTransaction(context, true);
            StringBuilder sb = new StringBuilder();
            String personOid = null;
            String objectName = null;
            String originFirstName = null;
            String originLastName = null;
            String originEmailAddr = null;
            String originDeptCode = null;
            String current = null;
            Iterator userItr = checkUser.iterator();
            while (userItr.hasNext()) {
                Map personInfo = (Map) userItr.next();
                personOid = personInfo.get("id").toString();
                objectName = personInfo.get("name").toString();
                originFirstName = personInfo.get("attribute[First Name].value").toString();
                originLastName = personInfo.get("attribute[Last Name].value").toString();
                originEmailAddr = personInfo.get("attribute[Email Address].value").toString();
                originDeptCode = personInfo.get("to[Business Unit Employee].from.name") != null ? personInfo.get("to[Business Unit Employee].from.name").toString() : "null";
                current = personInfo.get("current").toString();
            }

            Person personObj = new Person(personOid);
            if (current.equals("Inactive")) {
                personObj.promote(context);
                sb.append("Active로 상태 변경\t");
            }

            Boolean res = setSecurityContext(context, personObj, userInfo);
            if (res) sb.append("Security Context 업데이트\t");

            // 사용자 이름 변경
            String newFirstName = userInfo.get("EMP_NAME").toString().trim();
            String newLastName = userInfo.get("POSITION_NAME").toString().trim();

            if (!originFirstName.equals(newFirstName)) {
                Map personAttr = personObj.getAttributeMap(context, true);
                personObj.setAttributeValue(context, "First Name", newFirstName);
                sb.append("First Name 변경\t");
            }

            if (!originLastName.equals(newLastName)) {
                Map personAttr = personObj.getAttributeMap(context, true);
                personObj.setAttributeValue(context, "Last Name", newLastName);
                sb.append("Last Name 변경\t");
            }

            // 사용자 Email Address 업데이트
            String newEmailAddr = userInfo.get("E_MAIL").toString();
            if (!newEmailAddr.equals("null") && !originEmailAddr.equals(newEmailAddr)) {
                Map personAttr = personObj.getAttributeMap(context, true);
                personAttr.put(DomainConstants.ATTRIBUTE_EMAIL_ADDRESS, newEmailAddr);

                // 패스포트의 사용자 정보 업데이트
                personAttr.put("name", objectName);
                sendPassportUser(personAttr, prop.getProperty("3DPASSPORT_URL"), "update");
                sb.append("Email 주소 변경\t");
            }

            // 사용자 정보 업데이트 (부서 정보 변경)
            String newDeptCode = userInfo.get("DEPT_ID").toString();
            if (originDeptCode.equals("null")) {
                String buOid = ServiceUtil.getObjectInfo(context, "Business Unit", "name", userInfo.get("DEPT_ID").toString(), null).get("id").toString();
                MqlUtil.mqlCommand(context, String.format("connect bus %s rel 'Business Unit Employee' to %s", buOid, personOid));
                originDeptCode = userInfo.get("DEPT_ID").toString();
            }

            if (!originDeptCode.equals(newDeptCode)) {
                String originBuOid = ServiceUtil.getObjectInfo(context, "Business Unit", "name", originDeptCode, null).get("id").toString();
                modifyBuRel(context, "disconnect", originBuOid, personOid);

                String newBuOid = ServiceUtil.getObjectInfo(context, "Business Unit", "name", newDeptCode, null).get("id").toString();
                modifyBuRel(context, "connect", newBuOid, personOid);
                sb.append("사용자 소속 부서 변경");
            }

            ContextUtil.commitTransaction(context);
            if_msg = sb.toString();
            System.out.println(ServiceUtil.printLog("[UPDATE] update person object's attribute -> " + personOid));
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
        }

        return if_msg;
    }

    /**
     * <pre>
     *     사용자 Inactive
     * </pre>
     *
     * @param context          context
     * @param isRemoveLicenses 사용자 라이선스 할당 취소 여부
     */
    private static String inactiveUser(Context context, String empId, Boolean isRemoveLicenses) {
        String if_msg = null;
        try {
            StringList s = new StringList();
            s.add("id");
            s.add("name");

            MapList checkUserExist = DomainObject.findObjects(context, "Person", null, "name==" + empId, s);
            if (!checkUserExist.isEmpty()) {
                Iterator checkUserItr = checkUserExist.iterator();
                while (checkUserItr.hasNext()) {
                    Map userInfo = (Map) checkUserItr.next();
                    String userOid = userInfo.get("id").toString();

                    try {
                        DomainObject dObj = new DomainObject(userOid);
                        String stateActive = FrameworkUtil.lookupStateName(context, DomainObject.POLICY_PERSON, "state_Active");
                        String sState = dObj.getInfo(context, DomainObject.SELECT_CURRENT);

                        if (sState.equals(stateActive)) {
                            ContextUtil.startTransaction(context, true);
                            dObj.demote(context); // 사용자 Inactive

                            StringList objectSelects = new StringList();
                            objectSelects.addElement(DomainConstants.SELECT_NAME);
                            objectSelects.addElement("attribute[" + DomainObject.ATTRIBUTE_LICENSED_HOURS + "]");
                            Map personInfo = dObj.getInfo(context, objectSelects);
                            String strUserName = (String) personInfo.get(DomainConstants.SELECT_NAME);
                            String sLicensedHours = (String) personInfo.get("attribute[" + DomainObject.ATTRIBUTE_LICENSED_HOURS + "]");
                            boolean isCasualUser = !"0".equals(sLicensedHours);

                            // 사용자 라이센스 정보
                            String[] licenseList;
                            String[] licenseListToRelease;
                            if (isCasualUser) {
                                Map<String, Integer> mapLicenses = LicenseUtil.getCasualLicenses(context, false);
                                licenseList = (String[]) mapLicenses.keySet().toArray(new String[]{});
                            } else {
                                licenseList = LicenseUtil.getLicenses(context, false);//exportting all the web application licenses for the current user
                            }

                            // license remove
                            if (licenseList != null && licenseList.length != 0 && isRemoveLicenses) {
                                try {
                                    java.util.List info = LicenseUtil.getUserLicenseUsage(context, strUserName, null);
                                    licenseListToRelease = new String[info.size()];
                                    for (int k = 0, len = info.size(); k < len; k++) {
                                        HashMap rowmap = (HashMap) info.get(k);
                                        String licTrigram = (String) rowmap.get(LicenseUtil.INFO_LICENSE_NAME);
                                        licenseListToRelease[k] = licTrigram;
                                    }
                                    try {
                                        LicenseUtil.releaseLicenses(context, strUserName, licenseListToRelease, null);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        //isLicenseError = true;
                                    }
                                } catch (Exception exp) {
                                    String strMsg = exp.getMessage();
                                    if (strMsg.indexOf("License not found") == -1) {
                                        throw new Exception(exp);
                                    }
                                }

                                String strCommand;
                                for (int j = 0; j < licenseList.length; j++) {
                                    String strResult = "";
                                    strCommand = "print product $1 select $2 dump $3";
                                    if (isCasualUser) {
                                        strResult = MqlUtil.mqlCommand(context, strCommand, true, licenseList[j], "casualhour[" + sLicensedHours + "].person", "|");
                                    } else {
                                        strResult = MqlUtil.mqlCommand(context, strCommand, true, licenseList[j], "person", "|");
                                    }
                                    if (strResult != null && !"".equals(strResult) && strResult.indexOf(strUserName) != -1) {
                                        strCommand = "modify product " + licenseList[j] + " remove person \"" + strUserName + "\"";
                                        MqlUtil.mqlCommand(context, strCommand, true);
                                    }
                                }
                            }
                            ContextUtil.commitTransaction(context);
                            if_msg = String.format("'%s' 사용자를 Inactive 하였습니다.", empId);
                            System.out.println(ServiceUtil.printLog(String.format("[INACTIVE] Change '%s' status -> Inactive", userOid)));
                        }
                    } catch (Exception e) {
                        ContextUtil.abortTransaction(context);
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ContextUtil.abortTransaction(context);
        }

        return if_msg;
    }

    /**
     * <pre>
     *     사용자 Security Context Relationship 연결 및 Role 할당
     * </pre>
     *
     * @param context
     * @param personObj
     * @param userInfo
     * @return
     */
    private static Boolean setSecurityContext(Context context, Person personObj, Map userInfo) {
        try {
            ContextUtil.startTransaction(context, true);

            String emp_id = userInfo.get("EMP_ID").toString().trim();
            String dept_id = userInfo.get("DEPT_ID").toString().trim();
            String personOid = personObj.getId(context);

            List<String> scList = new ArrayList<>();
            if (dept_id.equals("R17000") || dept_id.equals("F00050")) {
                // 사용자의 부서가 연구기획팀인 경우 혹은 연구소장님일 경우, VPLMProjectAdministrator Security Context 부여
                scList.add(String.format("VPLMProjectAdministrator.%s.RPMS", dept_id));
                scList.add(String.format("VPLMProjectLeader.%s.RPMS", dept_id));

            } else {
                // 그 외 사용자는 VPLMCreator Security Context 부여
                scList.add(String.format("VPLMCreator.%s.RPMS", dept_id));
            }

            StringList sl = new StringList();
            sl.add("id");
            sl.add("name");

            // 불필요한 Security Context 제거
            List<String> userSCList = new ArrayList<>();
            MapList relSecurityContext = personObj.getRelatedObjects(context, "Assigned Security Context", "*", sl, (StringList) null, true, true, (short) 1, (String) null, "", 0);
            if (!relSecurityContext.isEmpty()) {
                Iterator relItr = relSecurityContext.iterator();
                while (relItr.hasNext()) {
                    Map scInfo = (Map) relItr.next();
                    String relSCId = scInfo.get("id").toString();
                    userSCList.add(relSCId);
                    String scName = scInfo.get("name").toString();

                    if (!scList.contains(scName)) {
                        MqlUtil.mqlCommand(context, String.format("disconnect bus %s rel 'Assigned Security Context' to %s", personOid, relSCId));
                    }
                }
            }

            // 불필요한 Role 제거
            String preFix = "ctx::";
            List<String> userRoleList = new ArrayList<>();
            String data = MqlUtil.mqlCommand(context, String.format("print person %s select assignment dump |", emp_id));
            StringList list = FrameworkUtil.split(data, "\n");
            for (int i = 0; i < list.size(); i++) {
                String row = list.get(i);
                StringList items = FrameworkUtil.split(row, "|");

                for (int j = 0; j < items.size(); j++) {
                    String role = items.get(j);
                    userRoleList.add(role);

                    String checkRole = role.substring(0, 5).equals(preFix) ? role.replace(preFix, "") : role;
                    if (!scList.contains(checkRole) && !checkRole.contains("PRJ")) { // VPLMCreator or VPLMProjectLeader or VPLMProjectAdministrator or PRJ를 포함하지 않는 경우 role에서 제거
                        MqlUtil.mqlCommand(context, String.format("mod person %s remove assign role '%s'", emp_id, role));
                        userRoleList.remove(role);
                    }
                }
            }

            String scId = null;
            for (String sc : scList) {
                MapList checkSC = DomainObject.findObjects(context, "Security Context", null, "name==" + sc, new StringList("id"));
                if (!checkSC.isEmpty()) {
                    Iterator checkSCItr = checkSC.iterator();
                    while (checkSCItr.hasNext()) {
                        Map scInfo = (Map) checkSCItr.next();
                        scId = scInfo.get("id").toString();
                    }

                } else {
                    // Security Context 생성
                    DomainObject dobj =new DomainObject();
                    dobj.createObject(context, "Security Context", sc, "-", "Security Context", context.getVault().getName());
                    scId = dobj.getId(context);

                    String roleName = sc.split("\\.")[0];
                    String buCode = sc.split("\\.")[1];

                    // Role 생성
                    MqlUtil.mqlCommand(context, String.format("add role 'ctx::%s' parent %s,RPMS,%s", sc, buCode, roleName));
                    MapList checkRole = DomainObject.findObjects(context, "Business Role", null, "name=="+roleName, new StringList("id"));
                    Iterator roleItr = checkRole.iterator();
                    while (roleItr.hasNext()) {
                        Map roleInfo = (Map)roleItr.next();
                        String businessRoleId = roleInfo.get("id").toString();
                        MqlUtil.mqlCommand(context, String.format("connect bus %s rel 'Security Context Role' to %s", scId, businessRoleId));
                    }

                    MapList checkBU = DomainObject.findObjects(context, "Business Unit", null, "name=="+dept_id, new StringList("id"));
                    Iterator buItr = checkBU.iterator();
                    while (buItr.hasNext()) {
                        Map buInfo = (Map) buItr.next();
                        String buId = buInfo.get("id").toString();
                        MqlUtil.mqlCommand(context, String.format("connect bus %s rel 'Security Context Organization' to %s", scId, buId));
                    }

                    MapList checkPrj = DomainObject.findObjects(context, "PnOProject", null, "name==RPMS", new StringList("id"));
                    Iterator prjItr = checkPrj.iterator();
                    while (prjItr.hasNext()) {
                        Map prjInfo = (Map) prjItr.next();
                        String prjId = prjInfo.get("id").toString();
                        MqlUtil.mqlCommand(context, String.format("connect bus %s rel 'Security Context Project' to %s", scId, prjId));
                    }
                }

                if (!userSCList.contains(scId)) {
                    MqlUtil.mqlCommand(context, String.format("connect bus '%s' Relationship 'Assigned Security Context' to '%s'", personOid, scId));
                }

                String checkRole = "ctx::" + sc;
                if (!userRoleList.contains(checkRole)) {
                    MqlUtil.mqlCommand(context, String.format("mod person %s assign role '%s'", userInfo.get("EMP_ID").toString(), checkRole));
                }

                // Set Default Security Context
                if (sc.contains("VPLMCreator") || sc.contains("VPLMProjectAdministrator")) {
                    String checkDefaultSC = MqlUtil.mqlCommand(context, String.format("print person %s select property[preference_DefaultSecurityContext].value dump |", emp_id));

                    if (checkDefaultSC.isEmpty()) {
                        MqlUtil.mqlCommand(context, String.format("mod person %s property preference_DefaultSecurityContext value '%s'", emp_id, sc));
                    }
                }
            }
            ContextUtil.commitTransaction(context);

            return true;

        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();

            return false;
        }
    }

    /**
     * <pre>
     *     사용자와 Business Unit 간의 Relationship connect & disconnect
     * </pre>
     *
     * @param context   context
     * @param type      disconnect or connect
     * @param buOid     Business Unit Object Id
     * @param personOid 사용자 Object Id
     * @throws Exception
     */
    private static void modifyBuRel(Context context, String type, String buOid, String personOid) throws Exception {
        String[] personOids = new String[]{personOid};
        StringList selectedRoles = new StringList();
        BusinessUnit buObj = new BusinessUnit(buOid);

        if (type.equals("disconnect")) {
            buObj.removeMemberPersons(context, personOids);
            buObj.removeEmployeePersons(context, personOids);

        } else if (type.equals("connect")) {
            buObj.addMemberPerson(context, personOid, selectedRoles);
            buObj.addEmployeePerson(context, personOid, false);
        }
    }

    /**
     * <pre>
     *     Role 정보 가져오기
     * </pre>
     *
     * @param context context
     * @return
     */
    public static Map getRole(Context context, String strRole[]) {
        StringList slRoleList = new StringList();

        Map mapResult = new HashMap();

        String strRoles = "";
        try {
            String strRolesRequireGroupPerCompany = EnoviaResourceBundle.getProperty(context, "emxComponents.rolesRequireGroupPerCompany");

            if (strRole != null) {
                for (int i = 0; i < strRole.length; i++) {
                    String sRole = strRole[i].trim();

                    String sRoleAlias = FrameworkUtil.getAliasForAdmin(context, "role", sRole, true);
                    if (UIUtil.isNotNullAndNotEmpty(sRoleAlias)) {
                        if (strRolesRequireGroupPerCompany.indexOf(sRole) > -1) {
                            slRoleList.add(sRole);
                        }

                        strRoles += " " + sRoleAlias;
                        slRoleList.addElement(sRoleAlias);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mapResult.put("RoleString", strRoles);
        mapResult.put("RoleStringList", slRoleList);
        return mapResult;
    }

    public static String sendPassportUser(Map person, String passportUrl, String action) {
        String msg = "";
        try {
            String email = person.get("Email Address").toString().equals("Unknown") ? null : person.get("Email Address").toString();
            String firstName = person.get("First Name").toString();
            String lastName = person.get("Last Name").toString();
            String empId = person.get("name").toString().trim();

            /*
             * email 존재 하지 않는 경우는 임의로 생성 한다.
             */
            if (UIUtil.isNullOrEmpty(email)) {
                email = empId + "@gscaltex.com";
                person.put("Email Address", email);
            }

            /*
             * 이름이 없는 경우는 생략 한다.
             */
            if (UIUtil.isNullOrEmpty(firstName) || UIUtil.isNullOrEmpty(lastName)) {
                return "Success";
            }
            String strJson = toJSONString(person);
            String res = checkPassportUser(empId, email, prop.getProperty("3DPASSPORT_URL"));

            /*
             * 패스 포트 url 정보
             */
            URI uri = null;
            if ((!res.equals("0") && action.equals("register")) || (!res.equals("0") && action.equals("update"))) {
                uri = new URI(passportUrl + "/api/private/user/register");
            } else if (res.equals("0") && action.equals("update")) {
                uri = new URI(passportUrl + "/api/private/user/update");
            } else if (res.equals("0") && action.equals("delete")) {
                uri = new URI(passportUrl + "/api/private/user/delete");
            }

            if (uri != null) {
                URL url = uri.toURL();
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConnection.addRequestProperty("Accept", "application/json");
                urlConnection.addRequestProperty("ds-client-id", prop.getProperty("3DPASSPORT_API_CLIENT_ID"));
                urlConnection.addRequestProperty("ds-client-secret", prop.getProperty("3DPASSPORT_API_CLIENT_SECRET"));

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
                outputStreamWriter.write(strJson);
                outputStreamWriter.flush();

                if (urlConnection.getResponseCode() == 201) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    /*
                     * 전송 결과 확인
                     */
                    ObjectMapper objectMapper = new ObjectMapper();
                    for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                        HashMap map = objectMapper.readValue(line, HashMap.class);
                        String code = map.get("code").toString();
                        if ("0".equals(code)) msg = "Success";
                        else msg = map.get("messages").toString();
                    }
                } else {
                    msg = "Success";
                }
            } else {
                msg = "Success";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            msg = ex.getMessage();
        }
        return msg;
    }

    public void registerPassport(Context context, String[] args) throws Exception {
        String email = args[0];
        String firstName = args[1];
        String lastName = args[2];
        String empId = args[3];
        sendPassportUser(email, firstName, lastName, empId);
    }
    public static String sendPassportUser(String email,String firstName,String lastName,String empId) throws Exception {
        String msg = "";
        String action = "update";
        Properties prop = dbConnectionUtil.getProperty();
        String passportUrl = "https://rpms.gscaltex.co.kr/3dpassport";

        try {
//            String email = person.get("Email Address").toString().equals("Unknown") ? null : person.get("Email Address").toString();
//            String firstName = person.get("First Name").toString();
//            String lastName = person.get("Last Name").toString();
//            String empId = person.get("name").toString().trim();
            HashMap person = new HashMap();
            person.put("Email Address", email);
            person.put("First Name", firstName);
            person.put("Last Name", lastName);
            person.put("name", empId);
            /*
             * email 존재 하지 않는 경우는 임의로 생성 한다.
             */
            if (UIUtil.isNullOrEmpty(email)) {
                email = empId + "@gscaltex.com";
                person.put("Email Address", email);
            }

            /*
             * 이름이 없는 경우는 생략 한다.
             */
            if (UIUtil.isNullOrEmpty(firstName) || UIUtil.isNullOrEmpty(lastName)) {
                return "Success";
            }
            String strJson = toJSONString(person);

            System.out.println(strJson);

            String res = checkPassportUser(empId, email);
            System.out.println("check user >>>> " + res);

            /*
             * 패스 포트 url 정보
             */
            URI uri = null;
            if ((!res.equals("0") && action.equals("register")) || (!res.equals("0") && action.equals("update"))) {
                uri = new URI(passportUrl + "/api/private/user/register");
            } else if (res.equals("0") && action.equals("update")) {
                uri = new URI(passportUrl + "/api/private/user/update");
            } else if (res.equals("0") && action.equals("delete")) {
                uri = new URI(passportUrl + "/api/private/user/delete");
            }

            if (uri != null) {
                URL url = uri.toURL();
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConnection.addRequestProperty("Accept", "application/json");
                urlConnection.addRequestProperty("ds-client-id", "passport_api");
                urlConnection.addRequestProperty("ds-client-secret", "fc6ac352-f4e7-4cf9-8d4e-6cd52d558694");

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
                outputStreamWriter.write(strJson);
                outputStreamWriter.flush();

                System.out.println("Create Passport user >>>>> " + urlConnection.getResponseCode());

                if (urlConnection.getResponseCode() == 201) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    /*
                     * 전송 결과 확인
                     */
                    ObjectMapper objectMapper = new ObjectMapper();
                    for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                        HashMap map = objectMapper.readValue(line, HashMap.class);
                        String code = map.get("code").toString();
                        if ("0".equals(code)) msg = "Success";
                        else msg = map.get("messages").toString();
                    }
                } else {
                    msg = "Success";
                }
            } else {
                msg = "Success";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            msg = ex.getMessage();
        }
        return msg;
    }
    private static String checkPassportUser(String username, String email) {
        String code = "";
        try {
            Map body = new HashMap();
            body.put("username", username);
            body.put("email", email);

            JSONObject json = new JSONObject(body);
            /*
             * 페스 포트 url 정보
             */
            String passportUrl = "https://rpms.gscaltex.co.kr/3dpassport";

            URI uri = new URI(passportUrl + "/api/private/user/v2/get");

            /*
             * 전송 정보 세팅
             */
            URL url = uri.toURL();
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.addRequestProperty("ds-client-id", "passport_api");
            urlConnection.addRequestProperty("ds-client-secret", "fc6ac352-f4e7-4cf9-8d4e-6cd52d558694");

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
            outputStreamWriter.write(json.toString());
            outputStreamWriter.flush();
            System.out.println("urlConnection.getResponseCode() : " + urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                ObjectMapper objectMapper = new ObjectMapper();
                for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                    HashMap map = objectMapper.readValue(line, HashMap.class);
                    code = map.get("code").toString();
                }
            } else code = "-1";

        } catch (Exception ex) {
            ex.printStackTrace();
            code = "-1";
        }
        return code;
    }
    private static String checkPassportUser(String username, String email, String passportUrl) {
        String code = "";
        try {
            Map body = new HashMap();
            body.put("username", username);
            body.put("email", email);

            JSONObject json = new JSONObject(body);
            /*
             * 페스 포트 url 정보
             */
            URI uri = new URI(passportUrl + "/api/private/user/v2/get");

            /*
             * 전송 정보 세팅
             */
            URL url = uri.toURL();
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.addRequestProperty("ds-client-id", prop.getProperty("3DPASSPORT_API_CLIENT_ID"));
            urlConnection.addRequestProperty("ds-client-secret", prop.getProperty("3DPASSPORT_API_CLIENT_SECRET"));

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
            outputStreamWriter.write(json.toString());
            outputStreamWriter.flush();

            if (urlConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                ObjectMapper objectMapper = new ObjectMapper();
                for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                    HashMap map = objectMapper.readValue(line, HashMap.class);
                    code = map.get("code").toString();
                }
            } else code = "-1";

        } catch (Exception ex) {
            ex.printStackTrace();
            code = "-1";
        }
        return code;
    }

    private static String toJSONString(Map person) {
        String email = person.get("Email Address").toString();
        String firstName = person.get("First Name").toString();
        String lastName = person.get("Last Name").toString();
        String id = person.get("name").toString();
        String password = "Qwer1234";

        StringBuffer var1 = new StringBuffer();
        var1.append("{\"batchCreation\":true,\"disableUpdateNotification\":true,\"fields\":{");
        var1.append(email != null && !"".equals(email) ? "\"email\":\"" + email + "\"," : "");
        var1.append(lastName != null && !"".equals(lastName) ? "\"lastName\":\"" + lastName + "\"," : "");
        var1.append(firstName != null && !"".equals(firstName) ? "\"firstName\":\"" + firstName + "\"," : "");
        var1.append("\"country\":\"KR\",");
        var1.append("\"language\":\"en\",");
        var1.append(id != null && !"".equals(id) ? "\"username\":\"" + id + "\"" : "");
        var1.append(password != null && !"".equals(password) ? ",\"password\":\"" + password + "\"" : "");
        var1.append("}}");
        return var1.toString();
    }

    private static void updateHasMembers(Context context, String strPersonName) {
        try {

            String strHasMemberConnectId = MqlUtil.mqlCommand(context
                    , "print bus $1 $2 $3 select $4 dump $5"
                    , DomainConstants.TYPE_PERSON
                    , strPersonName
                    , "-"
                    , "to[Has Members|from.type=='Computing Environment'].id"
                    , "|");
            if (UIUtil.isNullOrEmpty(strHasMemberConnectId)) {
                return;
            }
            MqlUtil.mqlCommand(context
                    , MOD_CONNECTION_ID1
                    , strHasMemberConnectId
                    , "CEnv User Role"
                    , "MEMBRE");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <pre>
     * String Emtpy Check
     * </pre>
     *
     * @param string
     * @return true or false
     * @since : 2020-06-01
     */
    private static boolean isEmpty(String string) {
        string = StringUtils.trimToEmpty(string);
        return StringUtils.isEmpty(("null".equals(string) || "undefined".equals(string) ? "" : string));
    }

    /**
     * <pre>
     * String을 Empty 처리를 한다.
     * null일경우는 ""를 리턴하고, 그렇지 않을 경우는 양쪽 스페이스를 없애고 리턴한다.
     * </pre>
     *
     * @param pParam Object
     * @return String  원본Object가  null일 경우 "" 그렇지 않을 경우 trim 된 String
     */
    private static String NVL(Object pParam) {
        return NVL(pParam, "");
    }

    /**
     * <pre>
     * String을 Empty 처리를 한다.
     * null일경우는 strDefaultValue를 리턴하고, 그렇지 않을 경우는 양쪽 스페이스를 없애고 리턴한다.
     * </pre>
     *
     * @param pParam          Object
     * @param strDefaultValue String pParam이 null일시 대체될 값
     * @return String         원본 String가 null일 경우 "" 그렇지 않을 경우 trim 된 String
     */
    private static String NVL(Object pParam, String strDefaultValue) {
        String ret = "";

        if (pParam != null) {
            if (pParam instanceof String) {
                ret = (String) pParam;
            } else {
                ret = pParam.toString();
            }

            ret = ret.trim();
            if (isEmpty(ret)) {
                ret = strDefaultValue;
            }
        } else {
            ret = strDefaultValue;
        }
        return ret;
    }
}
