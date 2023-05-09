/**
 * <pre>
 *  List 관련 Util
 * </pre>
 *
 * @ClassName   : gscAPPListUtil.java
 * @Description : List 관련 Util
 * @author      : BongJun,Park
 * @since       : 2020-06-09
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-06-09     BongJun,Park   최초 생성
 * </pre>
 */

package com.gsc.apps.common.util;

import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.StringUtil;
import com.matrixone.apps.framework.ui.UIUtil;
import matrix.util.StringList;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class gscListUtil {

    /**
     * <pre>
     * Maplist에 Map이 Object형식을 Map으로 변환하여 리턴
     * </pre>
     * @param mapList maplist
     * @return
     */
    public static List<Map> foreach(MapList mapList) {
        return isNotEmpty(mapList) ? Collections.synchronizedList(mapList) : new ArrayList(0);
    }

    /**
     * <pre>
     * StringList에 String이 Object형식을 String으로 변환
     * </pre>
     * @param list
     * @return
     */
    public static List<String> foreach(StringList list) {
        return isNotEmpty(list) ? Collections.synchronizedList(list) : new ArrayList(0);
    }

    /**
     * <pre>
     * List null check
     * </pre>
     * @param list list
     * @return is null
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * <pre>
     * List null check
     * </pre>
     * @param list list
     * @return is not null
     */
    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    /**
     * <pre>
     * StringList에 같은 값이 존제시 중복 제거
     * </pre>
     * @param stringList stringlist
     * @return 중복제거된 stringlist
     */
    public static StringList distinct(StringList stringList) {
        if (isNotEmpty(stringList)) {
            StringList rStrLst = new StringList();
            for (String str : foreach(stringList)) {
                if (!rStrLst.contains(str)) {
                    rStrLst.add(str);
                }
            }
            return rStrLst;
        } else {
            return new StringList(0);
        }
    }

    /**
     * <pre>
     * maplist ascending sort
     * </pre>
     * @param mapList maplist
     * @param key sort key
     */
    public static void sortAscendingString(MapList mapList, String key) {
        mapList.addSortKey(key, "ascending", "string");
        mapList.sort();
    }

    /**
     * <PRE>
     * MapList에 name를 키로 Map으로 반환
     * </PRE>
     * @param mlList mapdata
     * @return map
     */
    public static Map<String, Map> getMapListByMap(MapList mlList) {
        return getMapListByMap(mlList, DomainConstants.SELECT_NAME);
    }

    /**
     * <PRE>
     * MapList에 name를 키로 Map으로 반환
     * </PRE>
     * @param mlList mapdata
     * @param strKeyName Map정보의 Key
     * @return map
     */
    public static Map<String, Map> getMapListByMap(MapList mlList, String strKeyName) {
        Map<String, Map> mapIF = new HashMap();
        try {
            for (Map mapInfo : foreach(mlList)) {
                String strKey = (String)mapInfo.get(strKeyName);
                mapIF.put(strKey, mapInfo);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return mapIF;
    }

    /**
     * <PRE>
     * MQL 실행 결과를 받아 마지막 코드만 StringList로 반환
     * </PRE>
     * @param strQueryResult
     * @param strSeparate
     * @return
     */
    public static StringList getQueryResultLastName(String strQueryResult, String strSeparate) {
        StringList slResult = new StringList();
        if (UIUtil.isNotNullAndNotEmpty(strQueryResult)) {
            List<String> liCmdList = StringUtil.split(strQueryResult, "\n");
            Iterator<String> itrCmd = liCmdList.iterator();
            while(itrCmd.hasNext()) {
                String strCmd 	= itrCmd.next();
                String strData = StringUtils.substringAfterLast(strCmd, strSeparate);
                slResult.add(strData);
            }
        }
        return slResult;
    }
}
