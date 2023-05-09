import com.dassault_systemes.platform.ven.jackson.databind.ObjectMapper;
import com.gsc.dbConfig.dbSessionUtil;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.framework.ui.UIUtil;
import matrix.db.Context;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class gscPassportUser_mxJPO {
    private static final String _namespace = "com.gsc.gscInterface.userIF";
    private static dbSessionUtil session = new dbSessionUtil(_namespace);
    private static Properties prop;

    public static void syncPassportUser(Context context, String[] args) {
        try {
            prop = com.gsc.dbConfig.dbConnectionUtil.getProperty();
            Map<String, Object> param = new HashMap<>();
            String today = session.getDate();
            param.put("date", today);
            System.out.println(ServiceUtil.printLog("[SQL] List User since updated >= " + today));

            MapList empList = (MapList) session.executeSql("list", "selectUserList", param);

            for (int i = 0; i < empList.size(); i++) {
                Map userInfo = (Map) empList.get(i);
                String curName = userInfo.get("CUR_NAME").toString().trim();

                if (curName.equals("재직")) {
                    String strEmpId = userInfo.get("EMP_ID").toString().trim();
                    String strEmpName = userInfo.get("EMP_NAME").toString();
                    String strDeptId = userInfo.get("DEPT_ID").toString();
                    String strEmail = userInfo.get("E_MAIL").toString().equals("null") ? null : userInfo.get("E_MAIL").toString();
                    String strPosition = userInfo.get("POSITION_NAME").toString();

                    Map attr = new HashMap();
                    attr.put("First Name", strEmpName); // attribute[First Name]에는 사용자 이름이 들어감
                    attr.put("Last Name", strPosition); // attribute[Last Name]에는 사용자 직책이 들어감
                    attr.put("Email Address", strEmail);
                    attr.put("name", strEmpId);

                    sendPassportUser(attr, prop.getProperty("3DPASSPORT_URL"), "register");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            res = res.equals("0") ? empId + " - Already Existed User" : empId + " - Not Exist";
            System.out.println(res);

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
            urlConnection.addRequestProperty("ds-client-id", prop.getProperty("3DPASSPORT_API_CLIENT_ID")); // db.properties에서 읽어옴
            urlConnection.addRequestProperty("ds-client-secret", prop.getProperty("3DPASSPORT_API_CLIENT_SECRET")); // db.properties에서 읽어옴

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
}
