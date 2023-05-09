/**
 * <pre>
 * JPO 내부에서 HttpRequest/세션 사용을 위한 Class
 * </pre>
 *
 * @ClassName   : emdRequestContext.java
 * @Description : JPO 내부에서 세션 정보 저장하고 불러오는 Class
 * @author      : JeongHun,Ha
 * @since       : 2020-04-21
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-04-21     JeongHun,Ha   최초 생성
 * </pre>
 */

package com.gsc.apps.listener;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;


@javax.servlet.annotation.WebFilter(urlPatterns={"/*"})
public class emdRequestContext implements Serializable, Filter {

	public emdRequestContext(){

	}
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static ThreadLocal<HttpServletRequest> tlRequest = new ThreadLocal<HttpServletRequest>();

	/**
	 * <pre>
	 * Get Request
	 * </pre>
	 *
	 * @author : JeongHun,Ha
	 * @since : 2020-04-21
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return tlRequest.get();
	}

	/**
	 * <pre>
	 * Get Session
	 * </pre>
	 *
	 * @author : JeongHun,Ha
	 * @since : 2020-04-21
	 * @return
	 */
	public static HttpSession getSession() {
		return tlRequest.get().getSession();
	}

	/**
	 * <pre>
	 * Set Session Value
	 * </pre>
	 *
	 * @author : JeongHun,Ha
	 * @since : 2020-04-21
	 * @param key
	 * @param value
	 */
	public static void setAttribute(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	/**
	 * <pre>
	 * Get Session Value
	 * </pre>
	 *
	 * @author : JeongHun,Ha
	 * @since : 2020-04-21
	 * @param key
	 * @return
	 */
	public static Object getAttribute(String key) {
		return getSession().getAttribute(key);
	}

	/**
	 * <pre>
	 * Get Session Value
	 * </pre>
	 *
	 * @author : JeongHun,Ha
	 * @since : 2020-04-21
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return (String)getSession().getAttribute(key);
	}

	/**
	 * <pre>
	 * Set Request
	 * </pre>
	 *
	 * @author : JeongHun,Ha
	 * @since : 2020-04-21
	 * @param req
	 */
	public static void setRequest(HttpServletRequest req) {
		tlRequest.set((HttpServletRequest) req);
	}

	@Override
	public void destroy() {
		// do nothing.
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		tlRequest.set((HttpServletRequest) req);
		try {
			chain.doFilter(req, resp);
		} finally {
			tlRequest.remove();
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// do nothing.
	}
}