import com.gsc.dbConfig.dbConnectionUtil;
import com.gsc.dbConfig.utils;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Context;
import matrix.util.StringList;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class gscEAIBatch_mxJPO {
    private static final String INVEST_EAI_XML = "<ns0:ER_DJRPMSIVMESTRES_TBE xmlns:ns0=\"http://GSC.BC.DJRPMS.xsdER_DJRPMSIVMESTRES_TBE_COMPOSITE\">" +
            " <ns1:SP_BUDGET_STATUS xmlns:ns1=\"http://schemas.microsoft.com/Sql/2008/05/TypedProcedures/app\">" +
            "  <ns1:p_std_yy>value1</ns1:p_std_yy>" +
            "  <ns1:p_dept_cd>value2</ns1:p_dept_cd>" +
            " </ns1:SP_BUDGET_STATUS>" +
            "</ns0:ER_DJRPMSIVMESTRES_TBE>";

    private static final String BUDGET_EAI_XML = "<ns0:ZCOC14_BUDGET_DETAIL_DISPLAY xmlns:ns0=\"http://Microsoft.LobServices.Sap/2007/03/Rfc/\">" +
            " <ns0:I_KHINR>CGB013</ns0:I_KHINR> " +
            " <ns0:I_KOKRS></ns0:I_KOKRS> " +
            " <ns0:I_MONAT>value1</ns0:I_MONAT> " +
            " <ns0:I_VERSN></ns0:I_VERSN> " +
            " <ns0:I_YEAR>value2</ns0:I_YEAR> " +
            " <ns0:ET_MBUDGET> " +
            " </ns0:ET_MBUDGET> " +
            "</ns0:ZCOC14_BUDGET_DETAIL_DISPLAY>";

    public static void main(String[] args) {
        Context context = utils.getContext();
        callEAI(context, new String[]{"invest"});
    }

    /**
     * <pre>인터페이스 관련 Data 반영 EAI API 호출</pre>
     * @param args 인터페이스 타입 (invest, budget)
     * @return
     */
    public static void callEAI(Context context, String[] args) {
        String type = args[0];

        try {
            Properties prop = dbConnectionUtil.getProperty();
            String request_url = prop.getProperty("EAI_API_URL");
            URI uri = new URI(request_url);
            URL url = uri.toURL();

            LocalDate now = LocalDate.now();
            String currentYear = String.valueOf(now.getYear());
            String currentMonth = String.valueOf(now.getMonth().getValue());

            if (type.equals("invest")) {
                List<String> deptList = getDeptCode(context);
                if (deptList.size() > 0) {
                    for (String deptCode : deptList) {
                        String body = INVEST_EAI_XML.replace("value1", currentYear).replace("value2", deptCode);
                        System.out.println(ServiceUtil.printLog("[INVEST] EAI CALL For '" + deptCode + "' DEPT & Year : '" + currentYear + "'"));
                        sendRequest(url, body);
                    }
                }
            } else if (type.equals("budget")) {
                String body = BUDGET_EAI_XML.replace("value1", currentMonth).replace("value2", currentYear);
                System.out.println(ServiceUtil.printLog(String.format("[BUDGET] EAI CALL until '%s/%s'", currentMonth, currentYear)));
                sendRequest(url, body);
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(ServiceUtil.printLog("Error : " + e.toString()));
        }
    }

    private static void sendRequest(URL url, String body) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.addRequestProperty("Content-Type", "application/xml");
            urlConnection.addRequestProperty("Accept", "application/json");

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
            outputStreamWriter.write(body);
            outputStreamWriter.flush();

            String responseCode = urlConnection.getResponseCode() == 202 ? "Success" : "Fail";
            System.out.println(ServiceUtil.printLog("Response for EAI API Call >>> " + responseCode));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServiceUtil.printLog("Error : " + e.toString()));
        }
    }

    private static List<String> getDeptCode(Context context) {
        List<String> deptCodeList = null;

        try {
            deptCodeList = new ArrayList<>();
            String data = MqlUtil.mqlCommand(context, "temp query bus 'Business Unit' * * where 'current==Active' select name dump |");
            StringList list = FrameworkUtil.split(data, "\n");
            for (int i = 0; i < list.size(); i++) {
                String row = list.get(i);
                StringList items = FrameworkUtil.split(row, "|");
                deptCodeList.add(items.get(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServiceUtil.printLog("Error : " + e.toString()));
        }

        return deptCodeList;
    }
}
