/**
 * <pre>
 * 전체 도메인에서 공통적으로 사용할 JPO Util 을 정의한 JPO Class
 * </pre>
 *
 * @ClassName   : ${CLASSNAME}.java
 * @Description : 전체 도메인에서 공통적으로 사용할 JPO Util 을 정의한 JPO Class
 * @author      : GeonHwan,Bae
 * @since       : 2020-04-28
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-04-28     GeonHwan,Bae   최초 생성
 * 2020-09-17     MinSung,Kim    getCommonAttributeRange: Common Code Display 값이 없을 경우, Value로 Return 함.
 * </pre>
 */
import com.gsc.apps.common.util.gscCommonCodeUtil;
import com.gsc.apps.common.util.gscStringUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UICache;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.library.LibraryCentralConstants;
import matrix.db.*;
import matrix.util.StringList;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gscCommonUtil_mxJPO {

    /**
     * <pre>
     * Object 체번하는 Number Generator 생성 또는 체번하여 Return 하는 메서드
     * </pre>
     *
     * @param context
     * @param sType   - Type 명
     * @param sPolicy - Policy 명
     * @param sRevision - 해당 Number Object Revision 정의
     * @param sPrefix - Number 체번시 Prefix 정의
     * @param sSuffix   - Number 체번시 Suffix 정의
     * @param sNextNumber - Serial 숫자 지정
     * @return
     * @throws Exception
     */
    public static boolean createObjectGenerator(Context context, String sType, String sPolicy, String sRevision, String sPrefix, String sSuffix, String sNextNumber) throws Exception {
        boolean bleReturnValue = true;

        String strRevisionField = "";
        if (sRevision != null && !sRevision.equals("")) {
            strRevisionField = sRevision;
        }

        boolean isContinue =  true;

        String strTypeSymbolicName = UICache.getSymbolicName(context,  sType, DomainConstants.SELECT_TYPE);
        if (StringUtils.isEmpty(strTypeSymbolicName)) {
            strTypeSymbolicName = sType;
        }

        String strPolicySymbolicName = UICache.getSymbolicName(context, sPolicy, DomainConstants.SELECT_POLICY);
        if (StringUtils.isEmpty(strPolicySymbolicName)) {
            strPolicySymbolicName = sPolicy;
        }

        String strVaultSymbolicName = UICache.getSymbolicName(context, "eService Administration", DomainConstants.SELECT_VAULT);
        if (StringUtils.isEmpty(strVaultSymbolicName)) {
            strVaultSymbolicName = "eService Administration";
        }

        DomainObject busObjectGenerator = new DomainObject(new BusinessObject("eService Object Generator",
                strTypeSymbolicName,
                strRevisionField,
                "eService Administration"
        ));

        DomainObject numberGenerator = null;

        if (!busObjectGenerator.exists(context)) {
            boolean isPush = false;

            try {
                ContextUtil.pushContext(context);
                isPush = true;

                busObjectGenerator.create(context, "eService Object Generator");
                busObjectGenerator.setAttributeValue(context, "eService Name Prefix", sPrefix);
                busObjectGenerator.setAttributeValue(context, "eService Name Suffix", sSuffix);
                busObjectGenerator.setAttributeValue(context, "eService Safety Policy", strPolicySymbolicName);
                busObjectGenerator.setAttributeValue(context, "eService Processing Time Limit", "60");
                busObjectGenerator.setAttributeValue(context, "eService Safety Vault", strVaultSymbolicName);
                busObjectGenerator.setAttributeValue(context, "eService Retry Count", "5");
                busObjectGenerator.setAttributeValue(context, "eService Retry Delay", "1000");

                numberGenerator = new DomainObject(new BusinessObject("eService Number Generator" , strTypeSymbolicName, strRevisionField, "eService Administration"));
                numberGenerator.create(context, "eService Object Generator");
                numberGenerator.setAttributeValue(context, "eService Next Number", sNextNumber);

                DomainRelationship.connect(context, busObjectGenerator, new RelationshipType("eService Number Generator"), numberGenerator);

                UICache.setInitialized(false);
            } catch (Exception e) {
                bleReturnValue = false;
            } finally {
                if (isPush) ContextUtil.popContext(context);
            }
        }



        return bleReturnValue;
    }

    /**
     * <pre>
     * 공통 Range Program 으로 Range Attribute 의  Range 값을 가져온다.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <emdTagUtil:displayMultiSelectUtil program="emdCommonUtil"
     *     function="getCommonAttributeRangeList">emdPartType</emdTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     */
    public HashMap getCommonStateList(Context context, String[] args) throws Exception
    {
        HashMap    rangeMap    = new HashMap();
        StringList choiceList  = new StringList();
        StringList displayList = new StringList();
        StringList defaultList = new StringList();
        try
        {
            HashMap programMap = JPO.unpackArgs(args);
            String  strLangunage = (String) programMap.get("languageStr");
            String  strPolicy    = (String) programMap.get("KEY_VALUE");

            Policy policy = new Policy(strPolicy);

            Iterator stateItr = policy.getStateRequirements(context).iterator();
            while (stateItr.hasNext()) {
                try{
                    StateRequirement stateReq = (StateRequirement) stateItr.next();
                    choiceList.addElement((String)stateReq.getName());
                    displayList.addElement(i18nNow.getStateI18NString(strPolicy, (String)stateReq.getName(), strLangunage));
                }
                catch(Exception e){}
            }
            rangeMap.put("field_choices", choiceList);
            rangeMap.put("field_display_choices", displayList);
            rangeMap.put("field_default_values", defaultList);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new FrameworkException(ex.toString());
        }
        return rangeMap;
    }

    /**
     * <pre>
     * 공통 Range Program 으로  Attribute 의 Range 값을 가져온다.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <emdTagUtil:displayMultiSelectUtil program="emdCommonUtil"
     *     function="getCommonAttributeRangeList">Attribute 명</emdTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public HashMap getCommonAttributeRangeList(Context context, String[] args) throws Exception
    {
        HashMap    rangeMap    = new HashMap();
        StringList choiceList  = new StringList();
        StringList displayList = new StringList();
        StringList defaultList = new StringList();
        try
        {
            HashMap programMap = JPO.unpackArgs(args);
            String  strAttributeName  = (String) programMap.get("KEY_VALUE");

            rangeMap = getCommonAttributeRange(context, strAttributeName, (String) programMap.get("languageStr"), true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new FrameworkException(ex.toString());
        }
        return rangeMap;
    }

    /**
     * <pre>
     * 공통 Range Program 으로  Attribute 의 Range 값을 가져온다. Default 값을 설정하지 않는다.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <emdTagUtil:displayMultiSelectUtil program="emdCommonUtil"
     *     function="getCommonAttributeRangeListForSearch">Attribute 명</emdTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public HashMap getCommonAttributeRangeListForSearch(Context context, String[] args) throws Exception
    {
        HashMap    rangeMap    = new HashMap();
        StringList choiceList  = new StringList();
        StringList displayList = new StringList();
        try
        {
            HashMap programMap = JPO.unpackArgs(args);
            String  strAttributeName  = (String) programMap.get("KEY_VALUE");

            rangeMap = getCommonAttributeRange(context, strAttributeName, (String) programMap.get("languageStr"), false);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new FrameworkException(ex.toString());
        }
        return rangeMap;
    }

    /**
     * <pre>
     * 공통 Range Program 으로  Attribute 의 Range 값을 가져온다.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <emdTagUtil:displayMultiSelectUtil program="emdCommonUtil"
     *     function="getCommonAttributeRangeList">Attribute 명</emdTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     * @param context the eMatrix <code>Context</code> object
     * @param strAttributeName 속성명
     * @param strLanguage 언어
     * @param bleIsDefault
     * @return
     * @throws Exception
     */
    public HashMap getCommonAttributeRange(Context context, String strAttributeName, String strLanguage, boolean bleIsDefault) throws Exception
    {
        return getCommonAttributeRange(context, strAttributeName, strLanguage, bleIsDefault, false);
    }

    /**
     * <pre>
     * 공통 Range Program 으로  Attribute 의 Range 값을 가져온다.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <emdTagUtil:displayMultiSelectUtil program="emdCommonUtil"
     *     function="getCommonAttributeRangeList">Attribute 명</emdTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     * @param context the eMatrix <code>Context</code> object
     * @param strAttributeName 속성명
     * @param strLanguage 언어
     * @param bleIsDefault
     * @param bIsBlankValue 공백 추가 여부
     * @return
     * @throws Exception
     */
    public HashMap getCommonAttributeRange(Context context, String strAttributeName, String strLanguage, boolean bleIsDefault, boolean bIsBlankValue) throws Exception
    {
        HashMap    rangeMap    = new HashMap();
        StringList choiceList  = new StringList();
        StringList displayList = new StringList();
        StringList defaultList = new StringList();
        try
        {
            AttributeType attributeType = new AttributeType(strAttributeName);
            attributeType.open(context);

            choiceList = attributeType.getChoices();
            //[S] Modify 2020-06-10 MinSung,Kim, Range 자체에 blank가 있는 경우 제거
            StringList tempList = new StringList(choiceList);
            if (tempList != null && tempList.size() > 0) {
                int tempListSize = tempList.size();
                for (int i=0; i<tempListSize; i++) {
                    String tempRangeValue = (String)tempList.get(i);
                    if (gscStringUtil.isEmpty(tempRangeValue)) {
                        choiceList.remove(i);
                        break;
                    }
                }
            }
            //[E] Modify 2020-06-10 MinSung,Kim, Range 자체에 blank가 있는 경우 제거

            if (bleIsDefault) {
                //choiceList.sort();
                String defaultValue = gscStringUtil.NVL(attributeType.getDefaultValue());

                if (!"".equals(defaultValue))
                    defaultList.add(defaultValue);
            }

            if(bIsBlankValue) {
                choiceList.add(0, "");
            }

            attributeType.close(context);
            int intChoiceList = choiceList.size();

            for(int i=0; i < intChoiceList; ++i) {
                String strRangeValue = choiceList.get(i).toString();
                String strDisplayName = gscStringUtil.NVL(i18nNow.getRangeI18NString(strAttributeName, strRangeValue, strLanguage));
                if (strRangeValue.equals(strDisplayName))
                    strDisplayName = gscStringUtil.NVL(
                            gscCommonCodeUtil.getCommonCodeDisplay(context, strAttributeName, strRangeValue, strLanguage)
                            , strRangeValue);

                displayList.add(strDisplayName);
            }

            rangeMap.put("field_choices", choiceList);
            rangeMap.put("field_display_choices", displayList);

            if (defaultList.size() > 0)
                rangeMap.put("field_default_values", defaultList);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new FrameworkException(ex.toString());
        }
        return rangeMap;
    }

    /**
     * <pre>
     * Section Header의 Custom Label로 지정해서 사용.
     * JSP에서  CustomTag는 아래와 같이 사용함.
     * ex)
     *     <emdTagUtil:displayMultiSelectUtil program="emdCommonUtil"
     *     function="getCommonAttributeRangeList">emdPartType</emdTagUtil:displayMultiSelectUtil>
     * </pre>
     *
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public String getCommonSectionHeader(Context context, String[] args) throws Exception
    {
        StringBuilder sbuReturnValue = new StringBuilder();

        try
        {
            HashMap programMap = JPO.unpackArgs(args);
            String  strContentValue  = (String) programMap.get("KEY_VALUE");
            String  languageStr     = gscStringUtil.NVL(programMap.get("languageStr"));
            String  strBundle       = gscStringUtil.NVL(programMap.get("bundle"));
            String  strSectionName   = gscStringUtil.NVL(programMap.get("sectionName"));
            i18nNow i18nNowInstance = new i18nNow();

            String strSectionHeader = i18nNowInstance.GetString(strBundle, languageStr, strContentValue);

            sbuReturnValue.append(strSectionHeader);
            sbuReturnValue.append("&nbsp;");
            sbuReturnValue.append("<input type='checkbox' name='chkSectionHeader' onchange='javascript:fnChangeSectionTest(this);'/>");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new FrameworkException(ex.toString());
        }
        return sbuReturnValue.toString();
    }

    /**
     * <pre>
     * Column Name으로 Column Value 리턴
     * 해당 기능은 emxTable, emxIndentedTable에서 정의한 Table 조회하는 JPO 에서 미리 데이터가 담겨있어야 한다.
     * </pre>
     *
     * @param context
     * @param args [] will contain parameter of table data
     * @return
     * @throws Exception
     */
    public Vector getColumnValueByColumnName(Context context, String[] args) throws Exception
    {
        Vector vecReturnValue = new Vector<String>();

        HashMap mpProgramParam = JPO.unpackArgs(args);
        MapList mlObjectList   = (MapList) mpProgramParam.get("objectList");
        HashMap hmColumnMap    = (HashMap) mpProgramParam.get("columnMap");
        HashMap hmSettings     = (HashMap) hmColumnMap.get("settings");
        HashMap hmParamList    = (HashMap) mpProgramParam.get("paramList");
        String  strColumnName  = gscStringUtil.NVL(hmColumnMap.get("name"));
        String  strColumnType  = gscStringUtil.NVL(hmSettings.get("Column Type"));
        int     intObjectSize  = mlObjectList.size();

        if (strColumnType.equalsIgnoreCase("program"))
        {
            for (int i = 0; i < intObjectSize; ++i)
            {
                Map mObject = (Map) mlObjectList.get(i);

                String strResult = gscStringUtil.NVL(mObject.get(strColumnName), "");

                vecReturnValue.add(strResult);
            }
        }
        else
        {
            for (int i = 0; i < intObjectSize; ++i)
            {
                Map mObject = (Map) mlObjectList.get(i);

                String strResult = gscStringUtil.NVL(mObject.get(strColumnName), "");
                if (!strResult.equals(""))
                {
                    strResult = strResult.replaceAll(",", "<br/>");
                }

                vecReturnValue.add(strResult);
            }
        }

        return vecReturnValue;
    }

    /**
     * <pre>
     * Column Expression 으로 Column Value 리턴
     * 해당 기능은 emxTable, emxIndentedTable에서 정의한 Table 조회하는 JPO 에서 미리 데이터가 담겨있어야 한다.
     * </pre>
     *
     * @param context
     * @param args [] will contain parameter of table data
     * @return
     * @throws Exception
     */
    public Vector getColumnValueByColumnExpression(Context context, String[] args) throws Exception
    {
        Vector vecReturnValue = new Vector<String>();

        HashMap mpProgramParam = JPO.unpackArgs(args);
        MapList mlObjectList   = (MapList) mpProgramParam.get("objectList");
        HashMap hmColumnMap    = (HashMap) mpProgramParam.get("columnMap");
        HashMap hmSettings     = (HashMap) hmColumnMap.get("settings");
        String  strColumnType  = gscStringUtil.NVL(hmSettings.get("Column Type"));
        int     intObjectSize  = mlObjectList.size();
        String strSelectName = gscStringUtil.NVL(hmColumnMap.containsKey("expression_businessobject")?hmColumnMap.get("expression_businessobject"):hmColumnMap.get("expression_relationship"));

        if (!"".equalsIgnoreCase(strSelectName)) {
            strSelectName = changeSymbolicName(context, strSelectName);
        }
        if (strColumnType.equalsIgnoreCase("program"))
        {
            for (int i = 0; i < intObjectSize; ++i)
            {
                Map mObject = (Map) mlObjectList.get(i);

                String strResult = gscStringUtil.NVL(mObject.get(strSelectName), "");

                vecReturnValue.add(strResult);
            }
        }
        else
        {
            for (int i = 0; i < intObjectSize; ++i)
            {
                Map mObject = (Map) mlObjectList.get(i);

                String strResult = gscStringUtil.NVL(mObject.get(strSelectName), "");
                if (!strResult.equals(""))
                {
                    strResult = strResult.replaceAll(",", "<br/>");
                }

                vecReturnValue.add(strResult);
            }
        }

        return vecReturnValue;
    }

    /**
     * <pre>
     * Table Expression에서 Symbolic로 정의된 경우 자동으로 original name 로 변경하여 리턴한다.
     * </pre>
     *
     * @param context
     * @param strExpression
     * @return
     */
    private static String changeSymbolicName(Context context, String strExpression)
    {
        String strRegExp = "((relationship_|attribute_)[a-zA-Z0-9]*)";

        Pattern pat   = Pattern.compile(strRegExp);
        Matcher match = pat.matcher(strExpression);

        String strReturnValue = strExpression;
        while (match.find())
        {
            String strSymbolicName = match.group();
            String strOriginalName = PropertyUtil.getSchemaProperty(strSymbolicName);

            strReturnValue = strReturnValue.replace(strSymbolicName, strOriginalName);
        }
        return strReturnValue;
    }

    /**
     * <pre>
     * ENOVIA table/webform Range Function..
     * </pre>
     *
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public Map getCommonAttributeRangeByOOTBUI(Context context, String[] args) throws Exception {
        Map rangeMap = new HashMap();
        Map programMap = JPO.unpackArgs(args);

        Map requestMap = (Map)programMap.get("requestMap");
        Map fieldMap = (Map)programMap.get("fieldMap");

        if (fieldMap == null) {
            fieldMap = (Map)programMap.get("columnMap");
        }

        Map settingsMap = (Map)fieldMap.get("settings");
        String strAttributeName = gscStringUtil.NVL(settingsMap.get("emdCode Master"), (String)settingsMap.get("Admin Type"));
        boolean bIsBlankValue = true;
        String strInputType = gscStringUtil.NVL(settingsMap.get("Input Type"));
        String strRequired = gscStringUtil.NVL(settingsMap.get("Required"));
        if("checkbox".equals(strInputType) || strRequired.equalsIgnoreCase("true")) {
            bIsBlankValue = false;
        }

        if("combobox".equals(strInputType) && strRequired.equalsIgnoreCase("true")) {
            bIsBlankValue = true;
        }

        if(gscStringUtil.isNotEmpty(strAttributeName)) {
            if(strAttributeName.startsWith("attribute_")) {
                strAttributeName = PropertyUtil.getSchemaProperty(strAttributeName);
            }else {
                strAttributeName = strAttributeName;
            }
        }

        try {
            rangeMap = getCommonAttributeRange(context, strAttributeName, context.getSession().getLanguage(), true, bIsBlankValue);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return rangeMap;
    }

    /**
     * <pre>
     * ENOVIA table/webform Range Function..(임시)
     * </pre>
     *
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public Object displayCommonCodeValue(Context context, String[] args) throws Exception {
        String strDisplayValue = "";
        Vector vlDisplayValue = null;
        Map programMap = JPO.unpackArgs(args);
        Map requestMap = (Map)programMap.get("requestMap");
        String strMode = "";
        if (requestMap != null)
            strMode = gscStringUtil.NVL(requestMap.get("mode"));

        boolean bleIsTable = false;

        Map fieldMap = (Map)programMap.get("fieldMap");

        if (fieldMap == null) {
            fieldMap = (Map)programMap.get("columnMap");
            bleIsTable = true;
        }
        String expression_Bus    = (String)fieldMap.get("expression_businessobject");
        Map settingsMap = (Map)fieldMap.get("settings");
        String strAttributeName = gscStringUtil.NVL(settingsMap.get("emdCode Master"), (String)settingsMap.get("Admin Type"));
        if(gscStringUtil.isNotEmpty(strAttributeName)) {
            if(strAttributeName.startsWith("attribute_")) {
                strAttributeName = PropertyUtil.getSchemaProperty(strAttributeName);
            }else {
                strAttributeName = strAttributeName;
            }
        }
        String strLanguage = context.getSession().getLanguage();

        if (bleIsTable) {
            vlDisplayValue = new Vector();
            MapList mlObjects        = (MapList) programMap.get("objectList");
            int objectListSize = mlObjects.size();
            MapList mlColumnInfo = new MapList();

            if(objectListSize > 0) {
                if(((Map) mlObjects.get(0)).containsKey(expression_Bus)){
                    mlColumnInfo = mlObjects;
                }else{
                    StringList selectId = new StringList(1);
                    selectId.add(expression_Bus);

                    String[] objectIdArray = new String[objectListSize];
                    for(int i = 0; objectListSize > i; i++) {
                        Map objectMap = (Map) mlObjects.get(i);
                        objectIdArray[i] = (String) objectMap.get("id");
                    }
                    mlColumnInfo = DomainObject.getInfo(context, objectIdArray, selectId);
                }
            }

            if (strMode.equalsIgnoreCase("edit")) {

            } else {
                int coulmnInfoSize = mlColumnInfo.size();
                for(int i = 0; coulmnInfoSize > i; i++) {
                    try {
                        Map columnInfoMap = (Map) mlColumnInfo.get(i);
                        String strValue = gscStringUtil.NVL(columnInfoMap.get(expression_Bus));
                        if(strValue.indexOf(",") > 0) {
                            String[] strValues = strValue.split(",");
                            StringBuffer sbValue = new StringBuffer();
                            for(String strCodeValue : strValues){
                                sbValue.append(", ").append(gscCommonCodeUtil.getCommonCodeDisplay(context, strAttributeName, strCodeValue, strLanguage));
                            }
                            strDisplayValue = sbValue.substring(2);
                        } else {
                            strDisplayValue = gscCommonCodeUtil.getCommonCodeDisplay(context, strAttributeName, strValue, strLanguage);
                        }
                        vlDisplayValue.add(strDisplayValue);
                    } catch (Exception e) {
                        vlDisplayValue.add("");
                    }
                }
            }
        } else {
            if (strMode.equalsIgnoreCase("edit")) {
                String strObjectId = (String)requestMap.get("objectId");

                String strCode = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump", strObjectId, expression_Bus);

                strDisplayValue = strCode;
            } else {
                String strObjectId = (String)requestMap.get("objectId");

                String strCode = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump", strObjectId, expression_Bus);

                strDisplayValue = gscCommonCodeUtil.getCommonCodeDisplay(context, strAttributeName, strCode, strLanguage);

            }
        }


        return bleIsTable?vlDisplayValue:strDisplayValue;
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
            if( args.length < 2 ){
                return result;
            }
            String objectId = args[0];
            String strAttributeName = args[1];
            boolean bIsMutil = false;
            String strGubunCode = ",";
            if( args.length > 2 ){
                String strMutil = args[2];
                if(UIUtil.isNotNullAndNotEmpty(strMutil)){
                    bIsMutil = true;
                    strGubunCode = strMutil;
                }
            }

            if(gscStringUtil.isNotEmpty(objectId)) {
                DomainObject domObj = DomainObject.newInstance(context, objectId);
                String strDetailCodeValue = domObj.getAttributeValue(context, strAttributeName);
                if(UIUtil.isNotNullAndNotEmpty(strDetailCodeValue)){
                    String strLanguage = context.getSession().getLanguage();
                    if(bIsMutil){
                        String[] strDetailCodeValues = strDetailCodeValue.split(strGubunCode);
                        StringBuffer strCodeDisplayNames = new StringBuffer();

                        for(String strCodeValue : strDetailCodeValues){
                            strCodeDisplayNames.append(", ").append(gscCommonCodeUtil.getCommonCodeDisplay(context, strAttributeName, strCodeValue, strLanguage));
                        }
                        result = strCodeDisplayNames.substring(2);
                    }else{
                        result = gscCommonCodeUtil.getCommonCodeDisplay(context, strAttributeName, strDetailCodeValue, strLanguage);
                    }
                }
            }
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
            Map requestMap  = (Map) JPO.unpackArgs(args);

            Map paramMap    = (Map) requestMap.get("paramMap");
            String strObjectId = (String) paramMap.get("objectId");
            String strNewValue = (String) paramMap.get("New Value");
            String strAdminType = "";

            AttributeList multiValueAttributeList= new AttributeList();
            HashMap multiValuesMap=new HashMap();

            Map requestInfoMap    = (Map) requestMap.get("fieldMap");

            if(requestInfoMap == null) {
                requestInfoMap = (Map) requestMap.get("columnMap");
            }
            Map settingMap = (Map) requestInfoMap.get("settings");
            strAdminType = (String) settingMap.get("Admin Type");

            String attributeKey = "";
            if(gscStringUtil.isNotEmpty(strAdminType)) {
                if(strAdminType.startsWith("attribute_")) {
                    attributeKey = PropertyUtil.getSchemaProperty(strAdminType);
                }else {
                    attributeKey = strAdminType;
                }
            }

            String[] strNewValues = (String[]) paramMap.get("New Values");
            if(strNewValues != null) {
                strNewValue = FrameworkUtil.join(strNewValues, ",");
            }

            // HJ - 복수개의 속성 추가
            String selectExpression = (String)requestInfoMap.get(LibraryCentralConstants.EXPRESSION_BUSINESSOBJECT);
            String attributeName = UOMUtil.getAttrNameFromSelect(selectExpression);
            AttributeType attributeType = new AttributeType(attributeName.trim());
            if(strNewValues.length > 1){
                for(int index = 1; index < strNewValues.length+1; index++){
                    multiValuesMap.put(index,strNewValues[index-1]);
                }
            }else{
                multiValuesMap.put(1,strNewValues[0]);
            }
            // HJ - 복수개의 속성 추가

            DomainObject domObj = DomainObject.newInstance(context, strObjectId);
            if(gscStringUtil.isNotEmpty(strObjectId)) {
                //domObj.setAttributeValue(context, attributeKey, strNewValue);

                // HJ - 복수개의 속성 추가
                Attribute multiValueAttr=new Attribute(attributeType,multiValuesMap);
                multiValueAttributeList.add(multiValueAttr);
                domObj.setAttributeValues(context,multiValueAttributeList);
                // HJ - 복수개의 속성 추가
            }else{
                domObj.setAttributeValue(context, attributeKey, strNewValue);
            }
        } catch (Exception e) {
            throw new FrameworkException(e.getMessage());
        }
    }

    /**
     * <pre>
     * Setting 값의 Field Type으로 Attribute 값 변경.
     * </pre>
     *
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public static int updateColumnValue(Context context, String[] args) throws Exception
    {
        try
        {
            Map    programMap = JPO.unpackArgs(args);
            Map    paramMap   = (Map) programMap.get("paramMap");
            Map    fieldMap   = (Map) programMap.get("columnMap");
            Map    settings   = (Map) fieldMap.get("settings");
            String objectId   = (String) paramMap.get("objectId");
            String newValue   = (String) paramMap.get("New Value");
            String adminType  = gscStringUtil.NVL(settings.get("Admin Type"), "attribute");
            String fieldType  = (String) settings.get("Field Type");
            String attrName   = PropertyUtil.getSchemaProperty(context, adminType);

            if (adminType.equalsIgnoreCase("ATTRIBUTE")) {
                if(gscStringUtil.isNotEmpty(fieldType)) {
                    if(fieldType.startsWith("attribute_")) {
                        fieldType = PropertyUtil.getSchemaProperty(fieldType);
                    }else {
                        fieldType = fieldType;
                    }
                }

                DomainObject domObj = DomainObject.newInstance(context, objectId);
                domObj.setAttributeValue(context, fieldType, newValue);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }


        return 0;
    }

    /**
     * <pre>
     * Table 조회 결과에서 비교할 컬럼을 지정하여 비교하여 다른 경우 Style을 적용하는 Method
     * 해당 기능은 emxTable, emxIndentedTable에서 정의한 Table 조회하는 JPO 에서 미리 데이터가 담겨있어야 한다.
     * </pre>
     *
     * @param context
     * @param args [] will contain parameter of table data
     * @return
     * @throws Exception
     */
    public StringList getCompareStyleByTable(Context context, String[] args) throws Exception
    {
        StringList slReturnValue = new StringList();

        HashMap mpProgramParam        = JPO.unpackArgs(args);
        MapList mlObjectList          = (MapList) mpProgramParam.get("objectList");
        HashMap hmColumnMap           = (HashMap) mpProgramParam.get("columnMap");
        HashMap hmSettings            = (HashMap) hmColumnMap.get("settings");
        String  strColumnName         = gscStringUtil.NVL(hmColumnMap.get("name"));
        String  strDefaultStyle       = gscStringUtil.NVL(hmSettings.get("Style Column"),     "emdRowStyleFontBold");
        String  strCompareStyle       = gscStringUtil.NVL(hmSettings.get("emdCompare Style"), "emdRowStyleFontBoldRed");
        String  strCompareSelectName  = gscStringUtil.NVL(hmSettings.get("emdCompare Column"));
        int     intObjectSize  = mlObjectList.size();
        String strSelectName = gscStringUtil.NVL(hmColumnMap.containsKey("expression_businessobject")?hmColumnMap.get("expression_businessobject"):hmColumnMap.get("expression_relationship"));

        if (!"".equalsIgnoreCase(strSelectName)) {
            strSelectName = changeSymbolicName(context, strSelectName);
        } else {
            strSelectName = strColumnName;
        }

        if (!"".equalsIgnoreCase(strCompareSelectName)) {
            strCompareSelectName = changeSymbolicName(context, strCompareSelectName);
        }

        for (int i = 0; i < intObjectSize; ++i)
        {
            Map mObject = (Map) mlObjectList.get(i);

            String strCurrentValue = gscStringUtil.NVL(mObject.get(strSelectName), "");
            String strCompareValue = gscStringUtil.NVL(mObject.get(strCompareSelectName), "");

            if (!strCurrentValue.equals(strCompareValue))
            {
                slReturnValue.add(strCompareStyle);
            } else {
                slReturnValue.add(strDefaultStyle);
            }
        }
        return slReturnValue;
    }

    /**
     * <pre>
     * 문자열로 Engine Group Name을 반환한다.
     * </pre>
     *
     * @param strName
     * @return
     */
    public static String getEngineGroupByString(String strName) {
        String strReturnEngineGroupName = "";
        if (gscStringUtil.isNotEmpty(strName)) {
            int intLength = strName.length();

            if (intLength > 3)
                strReturnEngineGroupName = strName.substring(0,3);
            else if (intLength  == 3)
                strReturnEngineGroupName = strName;
        }

        return strReturnEngineGroupName;
    }

    // HJ
    public String getAttrRangegscProjectSiteValue(Context context,String[] args) throws Exception
    {
        // 값 조회를 위한 초기 세팅
        Map programMap = JPO.unpackArgs(args);
        Map requestMap = (Map)programMap.get("requestMap");
        Map fieldMap = (Map)programMap.get("fieldMap");
        Map settingsMap = (Map)fieldMap.get("settings");
        String strLanguage = context.getSession().getLanguage();

        // settings 중 'gscCode Master' 와 'Admin Type' 을 통해 속성 Name 을 가져옴
        String strAttributeName = gscStringUtil.NVL(settingsMap.get("gscCode Master"), (String)settingsMap.get("Admin Type"));

        if(gscStringUtil.isNotEmpty(strAttributeName)) {
            if(strAttributeName.startsWith("attribute_")) {
                strAttributeName = PropertyUtil.getSchemaProperty(strAttributeName);
            }else {
                strAttributeName = strAttributeName;
            }
        }

        String strObjectId = (String)requestMap.get("objectId");
        String expression_Bus    = (String)fieldMap.get("expression_businessobject");

        // MQL 을 통해 속성 값을 dump 로 조회
        String strCode = MqlUtil.mqlCommand(context, "print bus $1 select $2 dump", strObjectId, expression_Bus);

        // 조회한 속성 값을 list 로 담기
        String[] strlistCode = strCode.split(",");
        List<String> listCode = new ArrayList<String>();
        for(int n=0; n<strlistCode.length; n++){
            listCode.add(strlistCode[n]);
        }

        // 표시할 속성 Property 조회
        String[] gscProjectSite = new String[5];
        for(int i=0; i<gscProjectSite.length; i++){
            gscProjectSite[i]= EnoviaResourceBundle.getProperty(context, "Framework",
                    "emxFramework.Range."+strAttributeName+"."+"PS0"+(i+1), strLanguage);
        }

        // 표시할 HTML 작성
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<tr>");
        for(int i=0; i<gscProjectSite.length; i++) {
            sb.append("<td>");
            sb.append("<input type=\"checkbox\" name=\""+strAttributeName+"\" id=\""+strAttributeName+"\" value=\"PS0"+(i+1)+"\""+ (listCode.contains("PS0"+(i+1)) ? "checked=\"true\"" : "") + "/>");
            //sb.append("<input type=\"checkbox\" name=\"gscProjectSite\" id=\"gscProjectSite\" value=\"PS0"+(i+1)+"\" checked=\"true\" />");
            sb.append(gscProjectSite[i]);
            sb.append("&emsp;</td>");
        }
        sb.append("</tr>");
        sb.append("</table>");

        return sb.toString();
    }

}
