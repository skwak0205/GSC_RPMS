/**
 * <pre>
 * 문자열 관련 Util을 제공하는 Class
 * </pre>
 *
 * @ClassName   : gscStringUtil.java
 * @Description : 문자열 관련 Util을 제공하는 Class
 * @author      : GeonHwan,Bae
 * @since       : 2020-04-16
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-04-16     GeonHwan,Bae   최초 생성
 * </pre>
 */

package com.gsc.apps.common.util;

import org.apache.commons.lang3.StringUtils;

public class gscStringUtil extends StringUtils{
	
    /**
     * <pre>
     * String을 Empty 처리를 한다.
     * null일경우는 ""를 리턴하고, 그렇지 않을 경우는 양쪽 스페이스를 없애고 리턴한다.
     * </pre>
     * 
     * @param pParam   Object
     * @return String  원본Object가  null일 경우 "" 그렇지 않을 경우 trim 된 String
     */
    public static String NVL(Object pParam) {
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
    public static String NVL(Object pParam, String strDefaultValue) {
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
    
    /**
     * <pre>
     * String Not Empty Check 
     * </pre>
     * 
     * @since : 2020-06-01
     * @param object
     * @return true or false
     */
    public static boolean isNotEmpty(Object object) {
        return isNotEmpty((String)object);
    }
    
    /**
     * <pre>
     * String Not Empty Check
     * </pre>
     * 
     * @since : 2020-06-01
     * @param string
     * @return true or false
     */
    public static boolean isNotEmpty(String string) {
        string = StringUtils.trimToEmpty(string);
        return StringUtils.isNotEmpty(("null".equals(string) || "undefined".equals(string) ? "" : string));
    }
    
    /**
     * <pre>
     * String Empty Check 
     * </pre>
     * 
     * @since : 2020-06-01
     * @param object
     * @return true or false
     */
    public static boolean isEmpty(Object object) {
        return isEmpty((String)object);
    }
    
    /**
     * <pre>
     * String Emtpy Check 
     * </pre>
     * 
     * @since : 2020-06-01
     * @param string
     * @return true or false
     */
    public static boolean isEmpty(String string) {
        string = StringUtils.trimToEmpty(string);
        return StringUtils.isEmpty(("null".equals(string) || "undefined".equals(string) ? "" : string));
    }
    
    /**
     * <pre>
     * Null String to Empty 
     * </pre>
     * 
     * @since : 2020-06-01
     * @param str
     * @return empty or string
     */
    public static String trimToEmpty(Object str) {
        return trimToEmpty((String) str);
    }
    
    /**
     * <pre>
     * Null String to Empty  
     * </pre>
     * 
     * @since : 2020-06-01
     * @param str
     * @return empty or string
     */
    public static String trimToEmpty(String str) {
        str = StringUtils.trimToEmpty(str);
        return str == null || "null".equals(str) || "undefined".equals(str) ? "" : str;
    }
    /**
     * <pre>
     * String에서 줄바꿈 제거 (exception 메시지를 alert으로 띄울 때 활용)
     * </pre>
     * 
     * @since : 2020-06-01
     * @param str
     * @return empty or string
     */
	public static String replaceSlashes(String str) {
		if (isEmpty(str))
			return "";
		if (str.indexOf("@") > -1) {
			str = StringUtils.substringAfterLast(str, "@");
		}
		int idxEx = StringUtils.indexOfIgnoreCase(str, "Exception: ");
		if (idxEx > -1) {
			str = StringUtils.substringAfter(str, "Exception: ");
		}

		str = StringUtils.replace(str, "\"", "\\\"");
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if ((byte) str.charAt(i) == 13) {
				b.append(" ");
			} else if ((byte) str.charAt(i) == 10) {
				b.append("");
			} else
				b.append(str.charAt(i));
		}
		return b.toString();
	}

	/**
	 * <pre>
	 * String에서 모든 공백 제거
	 * </pre>
	 *
	 * @since : 2020-09-25
	 * @param str
	 * @return empty or string
	 */
	public static String trimAll(String str) {
		if(isEmpty(str)) return ""; else str = trimToEmpty(str);

		str = str.replaceAll("\\p{Z}", "");
		return str;
	}


    /**
     * <pre>
     * 알파벳을 순차적으로 증가 시킴.(ascii code 이용)
     *  ex) AA,AB,AC,...
     * </pre>
     *
     * @param source
     * @return
     * @throws Exception
     */

    public static String nextAlphabet(String source) {

        int length = source.length();
        char lastChar = source.charAt(length - 1);
        if (lastChar == 'Z') {
            if (length == 1) {
                return source = "AA";
            }

            source = nextAlphabet(source.substring(0, length - 1));
            source += "A";
            return source;
        }
        return source.substring(0, length - 1) + String.valueOf((char) (lastChar + 1));
    }
}
