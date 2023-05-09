/**
 * <pre>
 * 공통 코드 관련 Util 제공.
 * </pre>
 * 
 * @ClassName   : gscCommonCodeUtil.java
 * @Description : 공통 코드 관련 Util 제공.
 * @author      : GeonHwan,Bae
 * @since       : 2020-06-10
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-06-10     GeonHwan,Bae   최초 생성
 * </pre>
 */

package com.gsc.apps.common.util;

import com.gsc.apps.common.vo.gscCommonCodeVO;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Context;
import matrix.util.StringList;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gscCommonCodeUtil {
    private static final String SELECT_PROPERTY_KEY= "emxFramework.CodeDisplay.Format";
    private static HashMap<String, gscCommonCodeVO> mpCommonCodeLists = new HashMap();
    private static final String DEFAULT_REG_EXP = "(#\\{)([^}]*)(\\})";

    /**
     * <pre>
     * 코드 존재 여부
     * </pre>
     *
     * @param strMasterCode
     * @param strCode
     * @return
     */
    public static boolean isExists(String strMasterCode, String strCode) {

        String strKey = strMasterCode + "." + strCode;
        return mpCommonCodeLists.containsKey(strKey);
    }

    /**
     * <pre>
     * 코드 정보를 가져오기 위해 DB에 Connection 수를 줄이기 위해 코드 정보를 Cache에 담는다.
     * </pre>
     *

     */
    public static void reloadCodeInfo(Context context) {
        String strResult;
        try {
            strResult = MqlUtil.mqlCommand(context, "temp query bus $1 $2 $3 where $4 select $5 $6 $7 dump $8", true, "gscCodeMaster", "*", "*", "revision !='-'", "attribute[Title]", "attribute[gscTitleKO]", "description", "|");
            mpCommonCodeLists = new HashMap();
            if (!"".equals(strResult)) {
                String[] strAryCodeLists = strResult.split("\n");
                int intCodeListCount = strAryCodeLists.length;

                for(int i = 0; i < intCodeListCount; ++i) {
                    String strCodeDetailInfo = strAryCodeLists[i];
                    if (gscStringUtil.isNotEmpty(strCodeDetailInfo)) {
                        String[] strDetailInfos = strCodeDetailInfo.split("[|]", -1);
                        String strMasterCode = strDetailInfos[2];
                        String strCode       = strDetailInfos[1];
                        String strTitle      = gscStringUtil.NVL(strDetailInfos[3], strCode);;
                        String strTitleKo    = gscStringUtil.NVL(strDetailInfos[4], strTitle);
                        String strDescription    = gscStringUtil.NVL(strDetailInfos[5], strTitle);

                        setCodeInfo(strMasterCode, strCode, strTitle, strTitleKo, strDescription);
                    }
                }
            }
        } catch (FrameworkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 코드 정보를 가져오기 위해 DB에 Connection 수를 줄이기 위해 코드 정보를 Cache에 담는다.
     * </pre>
     *
     * @param strMasterCode 마스터 코드
     * @param strCode 디테일 코드
     * @param strTitle Title
     * @param strTitleKo Title Ko
     * @param strDescription Description
     */
    public static void setCodeInfo(String strMasterCode, String strCode, String strTitle, String strTitleKo, String strDescription) {

        String strKey = strMasterCode + "." + strCode;
        if (isExists(strMasterCode, strCode)) {
            mpCommonCodeLists.remove(strKey);
        }

        gscCommonCodeVO commonCodeVO = new gscCommonCodeVO();
        commonCodeVO.setCode(strCode);
        commonCodeVO.setTitle(strTitle);
        commonCodeVO.setTitleKo(strTitleKo);
        commonCodeVO.setDescription(strDescription);

        mpCommonCodeLists.put(strKey, commonCodeVO);
    }
    
    /**
     * <pre>
     * 코드 정보를 가져오기 위해 DB에 Connection 수를 줄이기 위해 코드 정보를 Cache에 담는다.
     * </pre>
     * 
     */
    private static String getCodeDisplayFormat(Context context, String strMasterCode) {
        String strCodeDisplayFormat ="";
        
        try {
            strCodeDisplayFormat = EnoviaResourceBundle.getProperty(context, SELECT_PROPERTY_KEY + "." + strMasterCode);
        } catch (FrameworkException fe) {
            try {
                strCodeDisplayFormat = EnoviaResourceBundle.getProperty(context, SELECT_PROPERTY_KEY);
            } catch (Exception ex) {
                strCodeDisplayFormat = "#{TITLE}";
            }
        }
        
        return strCodeDisplayFormat;
    }
    
    /**
     * <pre>
     * 코드의 Display명을 리턴한다.
     * </pre>
     */
    public static String getCommonCodeDisplay(Context context, String strMasterCode, String strCode, String strLanguage) throws Exception {
        String strDisplayName = "";
        HashMap mpParamMap = new HashMap();
        mpParamMap.put("CODE", strCode);
        if (isExists(strMasterCode, strCode)) {
            String strKey = strMasterCode + "." + strCode;
            gscCommonCodeVO commonCodeVO = (gscCommonCodeVO)mpCommonCodeLists.get(strKey);
            strDisplayName = commonCodeVO.getTitle(strLanguage);
        } else {
            strDisplayName = setCommonCodeDisplay(context, strMasterCode, strCode, strLanguage);
        }
        mpParamMap.put("TITLE", strDisplayName);
        
        String strDisplayFormat = getCodeDisplayFormat(context, strMasterCode);
        
        return getDisplayFormat(strDisplayFormat, mpParamMap);
    }

    /**
     * <pre>
     * 표현식에 따라 출력
     * </pre>
     *
     */
    public static String getDisplayFormat(String strDisplayFormat, Map objParam) throws Exception {
        return getDisplayFormat(strDisplayFormat, objParam, null);
    }

    /**
     * <pre>
     * 표현식에 따라 출력
     * </pre>
     *
     */
    public static String getDisplayFormat(String strDisplayFormat, Map objParam, String strRegExp) throws Exception {
        String strReturnValue = strDisplayFormat;

        String strRegExpression = gscStringUtil.NVL(strRegExp, DEFAULT_REG_EXP);

        Pattern pat = Pattern.compile(strRegExpression);
        Matcher match = pat.matcher(strDisplayFormat);

        while (match.find()) {
            String strKeyName = match.group();
            String strHashMapKeyName = strKeyName.replaceFirst("[#][{]", "").replaceFirst("[}]", "");
            String strReplaceValue = gscStringUtil.NVL(objParam.get(strHashMapKeyName));

            strReturnValue = strReturnValue.replace(strKeyName , strReplaceValue);
        }

        return strReturnValue;
    }

    /**
     * <pre>
     * Display Format을 select용 StringList로 변환
     * </pre>
     */
    public static StringList getDisplayFormatSelectList(String strDisplayFormat) throws Exception {
        StringList slSelectList = getDisplayFormatSelectList(strDisplayFormat, DEFAULT_REG_EXP);
        return slSelectList;
    }

    /**
     * <pre>
     * Display Format을 select용 StringList로 변환
     * </pre>
     */
    public static StringList getDisplayFormatSelectList(String strDisplayFormat, String strRegExp)
    {
        String strRegExpression = gscStringUtil.NVL(strRegExp, DEFAULT_REG_EXP);

        Pattern pat = Pattern.compile(strRegExpression);
        Matcher match = pat.matcher(strDisplayFormat);
        StringList slSelectList = new StringList();

        while (match.find()) {
            String strKeyName = match.group();
            String strHashMapKeyName = strKeyName.replaceFirst("[#][{]", "").replaceFirst("[}]", "");

            if (!slSelectList.contains(strHashMapKeyName)){
                slSelectList.add(strHashMapKeyName);
            }
        }

        return slSelectList;
    }
    /**
     * <PRE>
     * 코드의 Display명 캐쉬에 저장 한다.
     * </PRE>

     */
    private static String setCommonCodeDisplay(Context context, String strMasterCode, String strCode, String strLanguage) {
        String strDisplayName = "";
        try {
            boolean isExists = gscDomainUtil.isExists(context, "gscCodeMaster", strCode, strMasterCode);

            if (isExists) {
                String strCodeResult = MqlUtil.mqlCommand(context, "print bus $1 $2 $3 select $4 $5 $6 dump $7", true, "gscCodeMaster", strCode, strMasterCode, "attribute[Title]", "attribute[gscTitleKO", "description", "|");

                String strTitle   = strCode;
                String strTitleKo = "";
                String strDescription = "";
                if (!"".equals(strCodeResult)) {
                    String[] strAryCodeDetail = strCodeResult.split("[|]", -1);

                    strTitle   = strAryCodeDetail[0];
                    strTitleKo = strAryCodeDetail[1];
                    strDescription =gscStringUtil.NVL(strAryCodeDetail[2], strTitle);
                    if (strLanguage.startsWith("ko") && !"".equals(strTitleKo)) {
                        strDisplayName = strTitleKo;
                    } else {
                        strDisplayName = strTitle;
                    }

                } else {
                    strDisplayName = strCode;
                }

                setCodeInfo(strMasterCode, strCode, strTitle, strTitleKo, strDescription);
            }
        } catch(Exception ex) {
            strDisplayName = "";
        }
        return strDisplayName;
    }

    /**
     * <PRE>
     * 코드의 Description를 리턴 한다.
     * </PRE>
     * @param context the eMatrix <code>Context</code> object
     * @param strMasterCode 마스터 코드
     * @param strCode 코드
     * @param strLanguage Language
     * @return
     * @throws Exception
     */
    public static String getCommonCodeDescription(Context context, String strMasterCode, String strCode, String strLanguage) {
        String strDisplayName = "";
        HashMap mpParamMap = new HashMap();
        mpParamMap.put("CODE", strCode);
        String strKey = strMasterCode + "." + strCode;
        if (isExists(strMasterCode, strCode)) {
            gscCommonCodeVO commonCodeVO = mpCommonCodeLists.get(strKey);
            strDisplayName = commonCodeVO.getDescription();
        } else {
            setCommonCodeDisplay(context, strMasterCode, strCode, strLanguage);
            if (isExists(strMasterCode, strCode)) {
                gscCommonCodeVO commonCodeVO = mpCommonCodeLists.get(strKey);
                strDisplayName = commonCodeVO.getDescription();
            }
        }
        return strDisplayName;
    }
}
