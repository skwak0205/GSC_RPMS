/**
 * <pre>
 * Connection 관련된 Constants
 * </pre>
 *
 * @ClassName   : emdConnectionConstants.java
 * @Description : Connection 관련된 Constants
 * @author      : BongJun,Park
 * @since       : 2020-11-04
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-11-04     BongJun,Park   최초 생성
 * </pre>
 */
package com.gsc.apps.app.util;

import com.gsc.apps.common.util.gscStringUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class gscConnectionConstants {
    private static final String PROPERTY_FILE = "connection.properties";
    private static final String PROPERTY_FILE_DEV = "connection_dev.properties";
    private static final String PROPERTY_FILE_PROD = "connection_prod.properties";
    private static final String PROPERTY_FILE_TEST = "connection_test.properties";

    public static final String PROPERTY_INTERFACE_SERVER = "Interface.Server";

    public static final String PROPERTY_ERP_URL = "WebService.ERP.url";
    public static final String PROPERTY_ERP_URLS = "WebService.ERP.urls";
    public static final String PROPERTY_ERP_USER = "WebService.ERP.user";
    public static final String PROPERTY_ERP_PASS = "WebService.ERP.pass";

    public static final String PROPERTY_FTP_COPYROOM_LOGIN_IP = "FTP.CopyRoom.Login.Ip";
    public static final String PROPERTY_FTP_COPYROOM_LOGIN_PORT = "FTP.CopyRoom.Login.Port";
    public static final String PROPERTY_FTP_COPYROOM_LOGIN_ID = "FTP.CopyRoom.Login.Id";
    public static final String PROPERTY_FTP_COPYROOM_LOGIN_PASS = "FTP.CopyRoom.Login.Pass";
    public static final String PROPERTY_FTP_COPYROOM_LOGIN_DEFAULTPATH = "FTP.CopyRoom.Login.DefaultPath";

    public static final String PROPERTY_FILE_OUTPUT_NAS_DOCUMENT_PATH = "File.Output.NAS.Document.Path";
    public static final String PROPERTY_FILE_OUTPUT_NAS_DRAWING_PATH = "File.Output.NAS.Drawing.Path";
    public static final String PROPERTY_FILE_OUTPUT_NAS_COPY_PATH = "File.Output.NAS.Copy.Path";
    public static final String PROPERTY_FILE_OUTPUT_NAS_SMG_DN_PATH = "File.Output.NAS.SMG.DN.Path";
    public static final String PROPERTY_FILE_OUTPUT_NAS_SMG_UP_PATH = "File.Output.NAS.SMG.UP.Path";

    public static final String PROPERTY_INTERFACE_LOGIN_USER_ID = "interface.login.user.id";
    public static final String PROPERTY_INTERFACE_LOGIN_USER_PASS = "interface.login.user.pass";
    public static final String PROPERTY_INTERFACE_LOGIN_USER_ROLE = "interface.login.user.role";
    public static final String PROPERTY_INTERFACE_PASSPORT_URL = "interface.passport.url";

    public static String INTERFACE_SERVER = ""; // Server

    public static String URL = ""; // Erp 접속정보 URL
    public static String URLS = ""; // Erp 접속정보 URLS
    public static String USER = ""; // Erp 접속정보 USER
    public static String PASS = ""; // Erp 접속정보 PASS

    public static String FILE_OUTPUT_NAS_DOCUMENT_PATH = "";  // Convert nas document path
    public static String FILE_OUTPUT_NAS_DRAWING_PATH = "";  // Convert nas drawing path
    public static String FILE_OUTPUT_NAS_COPY_PATH = "";  // copy nas path
    public static String FILE_OUTPUT_NAS_SMG_DN_PATH = ""; // nas SMG File Path
    public static String FILE_OUTPUT_NAS_SMG_UP_PATH = ""; // Convert nas SMG File Path

    public static String FTP_COPYROOM_LOGIN_IP = "";
    public static String FTP_COPYROOM_LOGIN_PORT = "";
    public static String FTP_COPYROOM_LOGIN_ID = "";
    public static String FTP_COPYROOM_LOGIN_PASS = "";
    public static String FTP_COPYROOM_LOGIN_DEFAULTPATH = "";

    public static String INTERFACE_LOGIN_USER_ID = "";   // Interface Login User
    public static String INTERFACE_LOGIN_USER_PASS = ""; // Interface Login Pass
    public static String INTERFACE_LOGIN_USER_ROLE = ""; // Interface Login Role
    public static String INTERFACE_PASSPORT_URL = ""; // Interface Passport url

    static Properties prop = new Properties();
    static Map<String, Properties> mapProp = new HashMap();

    static {
        try {
            String strServerEnvironment = gscStringUtil.NVL(System.getenv("server.environment"));  // 서버 접속 DB
            String strServerLocation = gscStringUtil.NVL(System.getenv("server.location"));  // PC 위치(개인, 서버)
            String strPropertyFIle = "";
            switch (strServerEnvironment) {
                case "production":
                    strPropertyFIle = PROPERTY_FILE_PROD;
                    break;
                case "development":
                    strPropertyFIle = PROPERTY_FILE_DEV;
                    break;
                case "test":
                    strPropertyFIle = PROPERTY_FILE_TEST;
                    break;
                default:
                    strPropertyFIle = PROPERTY_FILE;
                    break;
            }
            INTERFACE_SERVER  = getPropertyValue(strPropertyFIle, PROPERTY_INTERFACE_SERVER ); // Interface 접속 정보

            URL  = getPropertyValue(strPropertyFIle, PROPERTY_ERP_URL ); // ERP 접속 정보 URL
            URLS = getPropertyValue(strPropertyFIle, PROPERTY_ERP_URLS); // ERP 접속 정보 URLS
            USER = getPropertyValue(strPropertyFIle, PROPERTY_ERP_USER); // ERP 접속 정보 USER
            PASS = getPropertyValue(strPropertyFIle, PROPERTY_ERP_PASS); // ERP 접속 정보 PASS

            FILE_OUTPUT_NAS_DOCUMENT_PATH = getPropertyValue(PROPERTY_FILE, PROPERTY_FILE_OUTPUT_NAS_DOCUMENT_PATH); // Convert nas path
            FILE_OUTPUT_NAS_DRAWING_PATH = getPropertyValue(PROPERTY_FILE, PROPERTY_FILE_OUTPUT_NAS_DRAWING_PATH); // Convert nas path
            FILE_OUTPUT_NAS_COPY_PATH = getPropertyValue(PROPERTY_FILE, PROPERTY_FILE_OUTPUT_NAS_COPY_PATH); // copy nas path
            FILE_OUTPUT_NAS_SMG_DN_PATH = getPropertyValue(PROPERTY_FILE, PROPERTY_FILE_OUTPUT_NAS_SMG_DN_PATH); // nas SMG File Path
            FILE_OUTPUT_NAS_SMG_UP_PATH = getPropertyValue(PROPERTY_FILE, PROPERTY_FILE_OUTPUT_NAS_SMG_UP_PATH); // Convert nas SMG File Path

            FTP_COPYROOM_LOGIN_IP = getPropertyValue(strPropertyFIle, PROPERTY_FTP_COPYROOM_LOGIN_IP); // 복사실 FTP IP
            FTP_COPYROOM_LOGIN_PORT = getPropertyValue(strPropertyFIle, PROPERTY_FTP_COPYROOM_LOGIN_PORT); // 복사실 FTP PORT
            FTP_COPYROOM_LOGIN_ID = getPropertyValue(strPropertyFIle, PROPERTY_FTP_COPYROOM_LOGIN_ID); // 복사실 FTP ID
            FTP_COPYROOM_LOGIN_PASS = getPropertyValue(strPropertyFIle, PROPERTY_FTP_COPYROOM_LOGIN_PASS); // 복사실 FTP PASS
            FTP_COPYROOM_LOGIN_DEFAULTPATH = getPropertyValue(strPropertyFIle, PROPERTY_FTP_COPYROOM_LOGIN_DEFAULTPATH); // 복사실 FTP DefaultPath

            INTERFACE_LOGIN_USER_ID = getPropertyValue(strPropertyFIle, PROPERTY_INTERFACE_LOGIN_USER_ID);     // Interface Login User
            INTERFACE_LOGIN_USER_PASS = getPropertyValue(strPropertyFIle, PROPERTY_INTERFACE_LOGIN_USER_PASS); // Interface Login Pass
            INTERFACE_LOGIN_USER_ROLE = getPropertyValue(strPropertyFIle, PROPERTY_INTERFACE_LOGIN_USER_ROLE); // Interface Login Role
            INTERFACE_PASSPORT_URL = getPropertyValue(strPropertyFIle, PROPERTY_INTERFACE_PASSPORT_URL); // Interface passport url
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <PRE>
     * Properties의 Key를 읽어옴.
     * </PRE>
     * @param properties properties file name
     * @param key key
     * @return key value
     * @throws Exception
     */
    public static String getPropertyValue(String properties, String key) {
        String returnValue = "";
        try {
            Properties prop_ = mapProp.get(properties);
            if(prop_ == null || prop_.isEmpty()) {
                prop_ = new Properties();
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream input = loader.getResourceAsStream(properties);
                prop_.load(input);

                mapProp.put(properties, prop_);
            }
            returnValue = prop_.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }
}
