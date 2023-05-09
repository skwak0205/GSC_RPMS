/**
 * <pre>
 * Session Util
 * </pre>
 * 
 * @ClassName   : gscSessionUtil.java
 * @Description : gscRequestContext를 이용한 세션 사용
 * @author      : JeongHun,Ha
 * @since       : 2020-05-22
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-05-22     JeongHun,Ha   최초 생성
 * </pre>
 */

package com.gsc.apps.common.util;

import com.gsc.apps.listener.gscRequestContext;
import com.matrixone.apps.domain.util.MapList;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class gscSessionUtil {

	private static gscSessionUtil ourInstance = null;
	private static final String SESSIONID = "GSCSESSION";
	private static final String LASTACCESSETIME = "GSCLASTACCESSTIME";
	private static WeakHashMap<String, HttpSession> sessions = new WeakHashMap<String, HttpSession>();

	public static gscSessionUtil instance() {
		if(ourInstance == null)
			ourInstance = new gscSessionUtil();
		return ourInstance;
	}

	private gscSessionUtil() {
	}
	
	/**
	 * <pre>
	 * add 
	 * </pre>
	 * 
	 * @since : 2020-06-22
	 * @param key
	 * @param value
	 */
	public void add(HttpSession session) {
		if(!sessions.containsKey(SESSIONID)) {
			session.setAttribute(LASTACCESSETIME, session.getLastAccessedTime());
			sessions.put(SESSIONID, session);				
		} else {
			try {
				HttpSession sess = sessions.get(SESSIONID);
				if(!session.getId().equals(sess.getId())) {
					try {
						Enumeration<String> attrNames = sess.getAttributeNames();
						while(attrNames.hasMoreElements()) {
							String attr = attrNames.nextElement();
							Object value1 = sess.getAttribute(attr);
							Object value2 = session.getAttribute(attr);
							if(value1 != null && checkObj(value1) && value2 == null){
								session.setAttribute(attr, value1);
							}
						}
					} catch(Exception _ex) {						
					}
					
					sessions.put(SESSIONID, session);
				}
			} catch(Exception e) {
				sessions.put(SESSIONID, session);
			}
		}
	}
	/**
	 * <pre>
	 * Check Object 
	 * </pre>
	 * 
	 * @since : 2020-06-22
	 * @param key
	 * @param value
	 */
	private boolean checkObj(Object obj) throws Exception {
		boolean isCheck = false;
		try {
			if(obj instanceof Map) isCheck = true;
			else if(obj instanceof MapList) isCheck = true;
			else if(obj instanceof HashMap) isCheck = true;
			else if(obj instanceof ArrayList) isCheck = true;
			else if(obj instanceof StringList) isCheck = true;
			else if(obj instanceof String) isCheck = true;
			else if(obj instanceof Integer) isCheck = true;			
		} catch(Exception e) {
			isCheck = false;
		}
		return isCheck;
	}
	/**
	 * <pre>
	 * Set 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @param key
	 * @param value
	 */
	public static void setAttribute(String key, Object value) {
		try {
			HttpServletRequest request = gscRequestContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute(key, value);
		} catch(Exception e) {
			//e.printStackTrace();
			//throw e;
		}
	}

	/**
	 * <pre>
	 * Get 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @param key
	 * @return
	 */
	public static Object getAttribute(String key) {
		Object value = null;
		try {
			HttpServletRequest request = gscRequestContext.getRequest();
			HttpSession session = request.getSession();
			value = session.getAttribute(key);
		} catch(Exception e) {
			//e.printStackTrace();
			//throw e;
		}
		return value;
	}
	
	/**
	 * <pre>
	 * Get String 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		Object value = null;
		try {
			HttpServletRequest request = gscRequestContext.getRequest();
			HttpSession session = request.getSession();
			value = session.getAttribute(key);
		} catch(Exception e) {
			//e.printStackTrace();
			//throw e;
		}
		return String.valueOf(value);
	}
	
	/**
	 * <pre>
	 * Remove Attribute 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @param key
	 */
	public static void remove(String key) {
		try {
			HttpServletRequest request = gscRequestContext.getRequest();
			HttpSession session = request.getSession();
			session.removeAttribute(key);
		} catch(Exception e) {
			e.printStackTrace();
			//throw e;
		}
	}
	
	/**
	 * <pre>
	 * Get Attribute Names 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @return
	 */
	public static Enumeration<String> getAttributeNames() {
		Enumeration<String> attrNames = null;
		try {
			HttpServletRequest request = gscRequestContext.getRequest();
			HttpSession session = request.getSession();
			attrNames = session.getAttributeNames();
		} catch(Exception e) {
			e.printStackTrace();
			//throw e;
		}
		return attrNames;
	}
	
	/**
	 * <pre>
	 * Duplicate Check Key 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @param key
	 * @return
	 */
	public static boolean  containsKey(String key) {
		boolean isExist = false;
		try {
			HttpServletRequest request = gscRequestContext.getRequest();
			HttpSession session = request.getSession();
			Enumeration<String> attrNames = session.getAttributeNames();
			while(attrNames.hasMoreElements()) {
				String attr = attrNames.nextElement();
				if(key.equals(attr)) {
					isExist = true;
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			//throw e;
		}
		return isExist;
	}
	
	/**
	 * <pre>
	 * Get Locale 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @return
	 */
	public static Locale getLocale() {
		HttpServletRequest request = gscRequestContext.getRequest();
		return request.getLocale();
	}
	
	/**
	 * <pre>
	 * Get language str 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @return
	 */
	public static String getLanguage() {
		HttpServletRequest request = gscRequestContext.getRequest();
		return request.getHeader("Accept-Language");
	}
	
	/**
	 * <pre>
	 * Get Time Zone 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @return
	 */
	public static String getTimeZone() {
		HttpServletRequest request = gscRequestContext.getRequest();
		return (String)request.getSession().getAttribute("timeZone");
	}
	
	/**
	 * <pre>
	 * Get Time Zone 
	 * </pre>
	 * 
	 * @since : 2020-05-22
	 * @return
	 */
	public static double getTimeOffset() {
		double iClientTimeOffset = -9.0d;
		try {
			iClientTimeOffset = (new Double(getTimeZone())).doubleValue();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return iClientTimeOffset;
	}
	
	/**
	 * <pre>
	 * Session Set Attribute
	 * </pre>
	 *
	 * @param key
	 * @param value
	 * @since : 2020-06-22
	 */
	public static void add(String key, Object value) {
		try {
			HttpServletRequest request = gscRequestContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * Session Get Attribute
	 * </pre>
	 *
	 * @param key
	 * @return
	 * @since : 2020-06-22
	 */
	public static Object get(String key) {
		Object value = null;
		try {
			HttpServletRequest request = gscRequestContext.getRequest();
			HttpSession session = request.getSession();
			value = session.getAttribute(key);
		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
		}
		return value;
	}

	/**
	 * <pre>
	 * Get Client Browser (IE, Firefox, ...)
	 * </pre>
	 *
	 * @return
	 * @since : 2020-08-17
	 */
	public static String getClientBrowser() {
		String browser = "";
		try {
			String userAgent = gscRequestContext.getRequest().getHeader("user-agent").toLowerCase();
			if (userAgent.indexOf("trident") > -1 || userAgent.indexOf("msie") > -1) { //IE
				browser = "IE";
				//if (userAgent.indexOf("Trident/7") > -1) {
				//	browser = "IE 11";
				//} else if (userAgent.indexOf("Trident/6") > -1) {
				//	browser = "IE 10";
				//} else if (userAgent.indexOf("Trident/5") > -1) {
				//	browser = "IE 9";
				//} else if (userAgent.indexOf("Trident/4") > -1) {
				//	browser = "IE 8";
				//} else if (userAgent.indexOf("edge") > -1) {
				//	browser = "IE edge";
				//}
			} else if (userAgent.indexOf("whale") > -1) { //네이버 WHALE
				browser = "WHALE"; // + userAgent.split("Whale/")[1].toString().split(" ")[0].toString();
			} else if (userAgent.indexOf("opera") > -1 || userAgent.indexOf("opr") > -1) { //오페라
				browser = "OPERA";
				//if (userAgent.indexOf("opera") > -1) {
				//	browser = "OPERA " + userAgent.split("Opera/")[1].toString().split(" ")[0].toString();
				//} else if (userAgent.indexOf("OPR") > -1) {
				//	browser = "OPERA " + userAgent.split("OPR/")[1].toString().split(" ")[0].toString();
				//}
			} else if (userAgent.indexOf("firefox") > -1) { //파이어폭스
				browser = "FIREFOX"; // + userAgent.split("Firefox/")[1].toString().split(" ")[0].toString();
			} else if (userAgent.indexOf("safari") > -1 && userAgent.indexOf("chrome") == -1) { //사파리
				browser = "SAFARI"; // + userAgent.split("Safari/")[1].toString().split(" ")[0].toString();
			} else if (userAgent.indexOf("chrome") > -1) { //크롬
				browser = "CHROME"; // + userAgent.split("Chrome/")[1].toString().split(" ")[0].toString();
			}
		} catch(Exception e) {

		}
		return browser;
	}
}
