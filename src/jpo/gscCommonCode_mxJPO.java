/**
 * <pre>
 * General Library/Class에서 사용할 클래스
 * </pre>
 *
 * @ClassName : ${CLASSNAME}.java
 * @Description : General Library/Class에서 사용할 클래스
 * @author : HyungJin,Ju
 * @since : 2023-01-10
 * @version : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2023-01-10     HyungJin,Ju   최초 생성
 * </pre>
 */

import com.gsc.apps.app.util.gscMQLConstants;
import com.gsc.apps.common.constants.gscTypeConstants;
import com.gsc.apps.common.util.gscCommonCodeUtil;
import com.gsc.apps.common.util.gscListUtil;
import com.gsc.apps.common.util.gscStringUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.library.LibraryCentralConstants;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.StringList;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class gscCommonCode_mxJPO {
    private static StringList slSelect = new StringList(5);
    private static String SELECT_CURRENT = "current";
    private static String STATE_CLASSIFICATION_ACTIVE = "Active";
    private static String QUERY_WILDCARD = "*";
    private static String VAULT_ESERVICE_PRODUCTION = "eService Production";
    private static String SELECT_ATTRIBUTE_SEQUENCE_ORDER = "attribute[Sequence Order]";
    private static String SELECT_ID = "id";
    private static String SELECT_NAME = "name";
    private static String SELECT_ATTRIBUTE_TITLE = "attribute[Title]";
    private static String SELECT_ATTRIBUTE_GSCTITLEKO = "attribute[gscTitleKO]";

    private static String ATTRIBUTE_GSCTITLEKO = "gscTitleKO";
    private static String ATTRIBUTE_TITLE = "Title";
    private static String SYMB_WILD = "*";
    private static String TYPE_GSCCODEMASTER = "gscCodeMaster";
    static {
        slSelect.add(SELECT_ID);
        slSelect.add(SELECT_NAME);
        slSelect.add(SELECT_ATTRIBUTE_SEQUENCE_ORDER);
        slSelect.add(SELECT_ATTRIBUTE_TITLE);
        slSelect.add(SELECT_ATTRIBUTE_GSCTITLEKO);
    }

    /**
     * <PRE>
     *     디테일 코드 objectid 값을 리턴
     * </PRE>
     * @param context the eMatrix <code>Context</code> object
     * @param strMasterCodeName
     * @param strDetailCodeName
     * @return 디테일 코드 objectid
     */
    public String selectDetailCodeObjectId(Context context, String strMasterCodeName, String strDetailCodeName) {

        String result = "";
        try {
            StringList slMqlArgumentList = new StringList(6);
            slMqlArgumentList.add(gscTypeConstants.TYPE_GSCCODEMASTER);
            slMqlArgumentList.add(strDetailCodeName);
            slMqlArgumentList.add(strMasterCodeName);
            slMqlArgumentList.add(SELECT_ID);
            slMqlArgumentList.add("|");

            String strDetailResult = MqlUtil.mqlCommand(context, gscMQLConstants.TEMP_BUS1, slMqlArgumentList);

            if (gscStringUtil.isNotEmpty(strDetailResult)) {
                StringList slMqlResultList = FrameworkUtil.splitString(strDetailResult, "|");
                result = slMqlResultList.get(3);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    /**
     * <PRE>
     *     detail code 리스트를 목록 리턴
     * </PRE>
     * @param context the eMatrix <code>Context</code> object
     * @param strMasterCodeName 마스터 코드
     * @return detail code 리스트
     */
    public MapList getDetailCodeList(Context context, String strMasterCodeName) {
        MapList result = new MapList();
        try {
            String strWhere = SELECT_CURRENT + " == " + STATE_CLASSIFICATION_ACTIVE;

            result = DomainObject.findObjects(context
                    , gscTypeConstants.TYPE_GSCCODEMASTER
                    , QUERY_WILDCARD
                    , strMasterCodeName
                    , QUERY_WILDCARD
                    , VAULT_ESERVICE_PRODUCTION
                    , strWhere
                    , false
                    , slSelect);
            result.sort(SELECT_ATTRIBUTE_SEQUENCE_ORDER, "ascending", "integer");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <pre>
     * execute program을 사용하여  detail code value 값 호출
     * </pre>
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        [0] objectId
     *        [1] attribute name
     *        [2] mutil gubun [option]
     * @return
     */
    public String selectDetailCodeDisplayByExecuteProgram(Context context, String[] args) {
        String result = "";
        try {
            if (args.length < 2) {
                return result;
            }
            String objectId = args[0];
            String strAttributeName = args[1];
            boolean bIsMutil = false;
            String strGubunCode = ",";
            if (args.length > 2) {
                String strMutil = args[2];
                if (UIUtil.isNotNullAndNotEmpty(strMutil)) {
                    bIsMutil = true;
                    strGubunCode = strMutil;
                }
            }

            if (gscStringUtil.isNotEmpty(objectId)) {
                DomainObject domObj = DomainObject.newInstance(context, objectId);
                String strDetailCodeValue = domObj.getAttributeValue(context, strAttributeName);
                if (UIUtil.isNotNullAndNotEmpty(strDetailCodeValue)) {
                    if (bIsMutil) {
                        String[] strDetailCodeValues = strDetailCodeValue.split(strGubunCode);
                        StringBuffer strCodeDisplayNames = new StringBuffer();

                        for (String strCodeValue : strDetailCodeValues) {
                            strCodeDisplayNames.append(", ").append(selectDetailCodeDisplay(context, strAttributeName, strCodeValue));
                        }
                        result = strCodeDisplayNames.substring(2);
                    } else {
                        result = selectDetailCodeDisplay(context, strAttributeName, strDetailCodeValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <PRE>
     *     detail code value 값을 리턴
     * </PRE>
     * @param context the eMatrix <code>Context</code> object
     * @param strMasterCodeName 마스터 코드 name
     * @param strDetailCodeName detail code name
     * @return detail code value
     */
    public String selectDetailCodeDisplay(Context context, String strMasterCodeName, String strDetailCodeName) {
        String language = context.getLocale().getLanguage();
        return selectDetailCodeDisplay(context, strMasterCodeName, strDetailCodeName, language);
    }

    /**
     * <PRE>
     *     detail code value 값을 리턴
     * </PRE>
     * @param context the eMatrix <code>Context</code> object
     * @param strMasterCodeName 마스터 코드 name
     * @param strDetailCodeName detail code name
     * @return detail code value
     */
    public String selectDetailCodeDisplay(Context context, String strMasterCodeName, String strDetailCodeName, String language) {
        String result = "";
        try {
            String strdDetailCodeId = selectDetailCodeObjectId(context, strMasterCodeName, strDetailCodeName);
            if (UIUtil.isNullOrEmpty(strdDetailCodeId)) {
                return result;
            }
            DomainObject detailDom = DomainObject.newInstance(context, strdDetailCodeId);

            String selectAttribute = "";
            if (language.equals("ko")) {
                selectAttribute = ATTRIBUTE_GSCTITLEKO;
            } else {
                selectAttribute = ATTRIBUTE_TITLE;
            }

            result = detailDom.getAttributeValue(context, selectAttribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <pre>
     *  Detail Code Attribute Update Program 
     * </pre>
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        paramMap a HashMap
     *        requestMap a HashMap
     * @throws FrameworkException
     */
    public void updateDetailCode(Context context, String[] args) throws FrameworkException {
        try {
            Map requestMap = (Map) JPO.unpackArgs(args);

            Map paramMap = (Map) requestMap.get("paramMap");
            String strObjectId = (String) paramMap.get("objectId");
            String strNewValue = (String) paramMap.get("New Value");
            String strAdminType = "";

            Map requestInfoMap = (Map) requestMap.get("fieldMap");

            if (requestInfoMap == null) {
                requestInfoMap = (Map) requestMap.get("columnMap");
            }
            Map settingMap = (Map) requestInfoMap.get("settings");
            strAdminType = (String) settingMap.get("Admin Type");

            String[] strNewValues = (String[]) paramMap.get("New Values");
            if (strNewValues != null) {
                strNewValue = FrameworkUtil.join(strNewValues, ",");
            }

            String attributeKey = "";
            if (gscStringUtil.isNotEmpty(strObjectId)) {
                if (gscStringUtil.isNotEmpty(strAdminType)) {
                    if (strAdminType.startsWith("attribute_")) {
                        attributeKey = PropertyUtil.getSchemaProperty(strAdminType);
                    } else {
                        attributeKey = strAdminType;
                    }
                }
                DomainObject domObj = DomainObject.newInstance(context, strObjectId);
                domObj.setAttributeValue(context, attributeKey, strNewValue);
            }
        } catch (Exception e) {
            throw new FrameworkException(e.getMessage());
        }
    }

    /**
     * <pre>
     *     code master 목록을 리턴
     * </pre>
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public MapList selectCodeMasterList(Context context, String[] args) throws Exception {
        Map programMap = (Map) JPO.unpackArgs(args);
        String strName = "";

        StringBuffer sbWhere = new StringBuffer();

        sbWhere.append("revision").append("== '-'");

        StringList slObjSelects = new StringList();
        slObjSelects.add(SELECT_ID);
        slObjSelects.add(SELECT_CURRENT);

        MapList codeMasterList = DomainObject.findObjects(
                context,
                TYPE_GSCCODEMASTER,
                SYMB_WILD + strName + SYMB_WILD,
                SYMB_WILD,
                SYMB_WILD,
                VAULT_ESERVICE_PRODUCTION,
                sbWhere.toString(), // where expression
                "",
                false,
                slObjSelects,
                (short) 0);
        return codeMasterList;
    }

    /**
     * <pre>
     *     code detail 목록을 리턴
     * </pre>
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public MapList selectCodeDetailList(Context context, String[] args) throws Exception {
        Map programMap = (Map) JPO.unpackArgs(args);

        Map requestMap = (Map) programMap.get("RequestValuesMap");
        String[] objectIds = (String[]) requestMap.get("objectId");
        String objectId = "";

        if (objectIds != null && objectIds.length > 0) {
            objectId = objectIds[0];
        } else {
            return new MapList();
        }

        StringList slObjSelects = new StringList();
        slObjSelects.add(SELECT_ID);
        slObjSelects.add(SELECT_CURRENT);

        StringList slRelSelects = new StringList();
        slRelSelects.add(DomainConstants.SELECT_RELATIONSHIP_ID);

        DomainObject codeMaster = new DomainObject(objectId);

        MapList mlCodeMasterList = codeMaster.getRelatedObjects(context
                , "gscCodeParentAndChild"
                , TYPE_GSCCODEMASTER
                , slObjSelects
                , slRelSelects
                , false
                , true
                , (short) 1
                , ""
                , ""
                , (short) 0);
        return mlCodeMasterList;
    }

    /**
     * <pre>
     * 공통 코드 마스터로 디테일 리스트 값을 가져온다.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <gscTagUtil:displayMultiSelectUtil program="gscCommonUtil"
     *     function="getCommonList">마스터 코드 명</gscTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public HashMap getCommonCodeList(Context context, String[] args) throws Exception {
        HashMap rangeMap = new HashMap();
        try {
            HashMap programMap = JPO.unpackArgs(args);
            String strAttributeName = (String) programMap.get("KEY_VALUE");

            rangeMap = getCommonCode(context, strAttributeName, (String) programMap.get("languageStr"));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FrameworkException(ex.toString());
        }
        return rangeMap;
    }

    /**
     * <pre>
     * 공통 코드 마스터로 디테일 리스트 값을 가져온다.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <gscTagUtil:displayMultiSelectUtil program="gscCommonUtil"
     *     function="getCommonAttributeRangeList">코드마스터 명</gscTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     * @param context the eMatrix <code>Context</code> object
     * @param strAttributeName 속성명
     * @param strLanguage 언어
     * @return
     * @throws Exception
     */
    public HashMap getCommonCode(Context context, String strAttributeName, String strLanguage) throws Exception {
        return getCommonCode(context, strAttributeName, strLanguage, false);
    }

    /**
     * <pre>
     * 공통 코드 마스터로 디테일 리스트 값을 가져온다.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <gscTagUtil:displayMultiSelectUtil program="gscCommonUtil"
     *     function="getCommonCodeList">코드마스터 명</gscTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     * @param context the eMatrix <code>Context</code> object
     * @param strLanguage 언어
     * @param bIsBlankValue 공백 추가 여부
     * @return
     * @throws Exception
     */
    public HashMap getCommonCode(Context context, String strCodeMaster, String strLanguage, boolean bIsBlankValue) throws Exception {
        HashMap rangeMap = new HashMap();
        StringList choiceList = new StringList();
        StringList displayList = new StringList();
        try {
            if (bIsBlankValue) {
                choiceList.add(0, "");
            }

            MapList mlDetailList = getDetailCodeList(context, strCodeMaster);

            for (Map mapDetailInfo : gscListUtil.foreach(mlDetailList)) {
                String strName = gscStringUtil.NVL(mapDetailInfo.get(SELECT_NAME));
                String strDisplayName = gscCommonCodeUtil.getCommonCodeDisplay(context, strCodeMaster, strName, strLanguage);

                choiceList.add(strName);
                displayList.add(strDisplayName);
            }

            rangeMap.put("field_choices", choiceList);
            rangeMap.put("field_display_choices", displayList);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FrameworkException(ex.toString());
        }
        return rangeMap;
    }

    /**
     *<pre>
     *     code detail create webform에서 Master code Name을 리턴
     *</pre>
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        requestMap a HashMap
     * @return
     * @throws Exception
     */
    public String getToObjectNameByField(Context context, String[] args) throws Exception {
        Map programMap = (Map) JPO.unpackArgs(args);
        Map requestMap = (Map) programMap.get("requestMap");
        String parentOID = (String) requestMap.get("parentOID");

        String sResult = "";

        if (UIUtil.isNotNullAndNotEmpty(parentOID)) {
            DomainObject doParent = new DomainObject(parentOID);
            String sName = doParent.getName(context);

            sResult = sName;
        }
        return sResult;
    }

    /**
     * delete common code
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        paramMap a HashMap
     *        requestMap a HashMap
     * @return error message
     * @throws Exception if the operation fails
     */
    public static Map deleteCommonCode(Context context, String[] args) throws Exception {
        Map returnMap = new HashMap();
        try {
            Map paramMap = JPO.unpackArgs(args);
            String[] strAryObjectId = (String[]) paramMap.get("deleteObjectIds");

            for (String objectId : strAryObjectId) {
                MqlUtil.mqlCommand(context, "delete bus $1", objectId);
            }
        } catch (Exception e) {
            throw e;
        }

        return returnMap;
    }

    /**
     * delete master code
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        paramMap a HashMap
     *        requestMap a HashMap
     * @return error message
     * @throws Exception if the operation fails
     */
    public static Map deleteMasterCode(Context context, String[] args) throws Exception {
        Map returnMap = new HashMap();
        try {
            Map paramMap = JPO.unpackArgs(args);
            String[] strAryObjectId = (String[]) paramMap.get("deleteObjectIds");

            for (String objectId : strAryObjectId) {
                String sMQLResult = MqlUtil.mqlCommand(context, gscMQLConstants.PRINT_BUS_ID1, objectId, "from[gscCodeParentAndChild].to.id", "|");
                if (UIUtil.isNotNullAndNotEmpty(sMQLResult)) {
                    String[] strChildObjectIds = sMQLResult.split("[|]");
                    for (String strChildObjectId : strChildObjectIds) {
                        MqlUtil.mqlCommand(context, "delete bus $1", strChildObjectId);
                    }
                }

                MqlUtil.mqlCommand(context, "delete bus $1", objectId);
            }
        } catch (Exception e) {
            throw e;
        }

        return returnMap;
    }

    /**
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        paramMap a HashMap
     *        requestMap a HashMap
     * @return
     */
    public StringList getStyleForState(Context context, String args[]) {
        StringList styleSheet = new StringList();
        try {
            HashMap programMap = JPO.unpackArgs(args);
            MapList objectList = (MapList) programMap.get("objectList");
            HashMap paramListMap = (HashMap) programMap.get("paramList");

            boolean bIsFirstTime = paramListMap.containsKey("firstTime");

            Iterator itr = objectList.iterator();

            while (itr.hasNext()) {
                if (!bIsFirstTime) {
                    Object oMap = (Object) itr.next();
                    Map mapObject = new HashMap();
                    if (oMap instanceof HashMap) {
                        mapObject = (Map) oMap;
                    } else if (oMap instanceof Hashtable) {
                        mapObject = (Hashtable) oMap;
                    }
                    String strCurrent = gscStringUtil.NVL(mapObject.get(DomainConstants.SELECT_CURRENT));
                    if (UIUtil.isNullOrEmpty(strCurrent)) {
                        System.out.println(mapObject);
                    } else {
                        if (mapObject.get(DomainConstants.SELECT_CURRENT).equals(LibraryCentralConstants.STATE_CLASSIFICATION_INACTIVE)) {
                            styleSheet.add("ResourcePlanningYellowBackGroundColor");
                        } else if (mapObject.get(DomainConstants.SELECT_CURRENT).equals(LibraryCentralConstants.STATE_CLASSIFICATION_ACTIVE)) {
                            styleSheet.add("ResourcePlanningGreenBackGroundColor");
                        } else {
                            styleSheet.add("");
                        }
                    }
                } else {
                    styleSheet.add("");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return styleSheet;
    }

    /***
     * <PRE>
     *     emxTable에 State의 combobox List를 반환한다.
     * </PRE>
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     * @return
     */
    public HashMap getStates(Context context, String[] args) {
        HashMap result = new HashMap();
        try {
            String strLocale = context.getSession().getLanguage();
            String strStateForPolicy = MqlUtil.mqlCommand(context
                    , "print policy $1 select $2 dump $3"
                    , true
                    , "gscCodeMaster"
                    , "state"
                    , "|");
            StringList slStates = FrameworkUtil.split(strStateForPolicy, "|");
            StringList slChoiceList = new StringList();
            StringList slDisplayList = new StringList();
            for (String strState : gscListUtil.foreach(slStates)) {
                slChoiceList.add(strState);
                slDisplayList.add(i18nNow.getStateI18NString("gscCodeMaster", strState, strLocale));
            }
            result.put("field_choices", slChoiceList);
            result.put("field_display_choices", slDisplayList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * <PRE>
     *      emxTable에서 공통코드 상태 수정시
     * </PRE>
     * @param context the Matrix Context
     * @param args no args needed for this method
     * @returns booloen
     * @throws Exception if the operation fails
     */
    public Boolean updateStatus(Context context, String[] args) throws Exception {
        HashMap programMap = (HashMap) JPO.unpackArgs(args);
        HashMap paramMap = (HashMap) programMap.get("paramMap");

        String objectId = (String) paramMap.get("objectId");
        // get the value selected for field Approval Status
        String strNewCurrent = (String) paramMap.get("New Value");

        MqlUtil.mqlCommand(context, gscMQLConstants.MOD_BUS_CURRENT, true, objectId, strNewCurrent);

        return Boolean.TRUE;
    }
}
