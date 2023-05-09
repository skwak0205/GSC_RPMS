/**
 * <pre>
 * 기본 Log Util Class
 * </pre>
 * 
 * @ClassName   : emdCommonLogUtil.java
 * @Description : 기본 Log Util Class
 * @author      : GeonHwan,Bae
 * @since       : 2020-04-21
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-04-21     GeonHwan,Bae   최초 생성
 * </pre>
 */

package com.gsc.apps.common.util.log;

import com.gsc.apps.common.util.impl.emdILogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class emdCommonLogUtil implements emdILogUtil{
	private static Logger log = LoggerFactory.getLogger("COMMON_LOG");
	
	/**
	 * 
	 * <pre>
	 * error 일반 로그 처리
	 * 
	 * [사용 예제]
	 * error("Error Message")	===> Error Message
	 * </pre>
	 * 
	 * @param strMessage String 
	 */
	public void error(String strMessage)  {
		log.error(strMessage);
	}
	
	/**
	 * 
	 * <pre>
	 * error 메세지 파라미터로 받아서 로그를 처리하는 부분
	 * 
	 * [사용 예제]
	 * error("Error Message {} {}", "A", "B")	===> Error Message A B
	 * error("Error Message {} {}", new String[]{"A", "B"}); ===> Error Message A B
	 * </pre>
	 * 
	 * @param strMessage String 
	 * @param args String...
	 */
	public void error(String strMessage, String... args)  {
		log.error(strMessage, args);
	}
	
	/**
	 * 
	 * <pre>
	 * Warning 일반 로그 처리
	 * 
	 * [사용 예제]
	 * warn("Warning Message")	===> Warning Message
	 * </pre>
	 * 
	 * @param strMessage String 
	 */
	public void warn(String strMessage)  {
		log.warn(strMessage);
	}
	
	/**
	 * 
	 * <pre>
	 * Warning 메세지 파라미터로 받아서 로그를 처리하는 부분
	 * 
	 * [사용 예제]
	 * warn("Warning Message {} {}", "A", "B")	===> Warning Message A B
	 * warn("Warning Message {} {}", new String[]{"A", "B"}); ===> Warning Message A B
	 * </pre>
	 * 
	 * @param strMessage String 
	 * @param args String...
	 */
	public  void warn(String strMessage, String... args)  {
		log.warn(strMessage, args);
	}
	
	/**
	 * 
	 * <pre>
	 * 정보관련 일반 로그 처리
	 * 
	 * [사용 예제]
	 * info("Information Message")	===> Information Message
	 * </pre>
	 * 
	 * @param strMessage String 
	 */
	public void info(String strMessage)  {
		log.info(strMessage);
	}
	
	/**
	 * 
	 * <pre>
	 * 정보관련 메세지 파라미터로 받아서 로그를 처리하는 부분
	 * 
	 * [사용 예제]
	 * info("Information Message {} {}", "A", "B")	===> Information Message A B
	 * info("Information Message {} {}", new String[]{"A", "B"}); ===> Information Message A B
	 * </pre>
	 * 
	 * @param strMessage String 
	 * @param args String...
	 */
	public void info(String strMessage, String... args)  {
		log.info(strMessage);
	}
	
	/**
	 * 
	 * <pre>
	 * debug 일반 로그 처리
	 * 
	 * [사용 예제]
	 * debug("Debug Message")	===> Debug Message
	 * </pre>
	 * 
	 * @param strMessage String 
	 */
	public void debug(String strMessage)  {
		log.debug(strMessage);
	}
	
	/**
	 * 
	 * <pre>
	 * debug 메세지 파라미터로 받아서 로그를 처리하는 부분
	 * 
	 * [사용 예제]
	 * debug("Debug Message {} {}", "A", "B")	===> Debug Message A B
	 * debug("Debug Message {} {}", new String[]{"A", "B"}); ===> Debug Message A B
	 * </pre>
	 * 
	 * @param strMessage String 
	 * @param args String...
	 */
	public void debug(String strMessage, String... args)  {
		log.debug(strMessage);
	}
	
	/**
	 * 
	 * <pre>
	 * trace 일반 로그 처리
	 * 
	 * [사용 예제]
	 * trace("Trace Message")	===> Trace Message
	 * </pre>
	 * 
	 * @param strMessage String 
	 */
	public void trace(String strMessage)  {
		log.trace(strMessage);
	}
	
	/**
	 * 
	 * <pre>
	 * trace 메세지 파라미터로 받아서 로그를 처리하는 부분
	 * 
	 * [사용 예제]
	 * trace("Trace Message {} {}", "A", "B")	===> Trace Message A B
	 * trace("Trace Message {} {}", new String[]{"A", "B"}); ===> Trace Message A B
	 * </pre>
	 * 
	 * @param strMessage String 
	 * @param args String...
	 */
	public void trace(String strMessage, String... args)  {
		log.trace(strMessage, args);
	}

	@Deprecated
	public void setFileName(String strFileName) {
	}

	@Deprecated
	public void setCallMethod(String strCallMethod) {
	}

	@Deprecated
	public void setCallClassName(String strCallClass) {
	}

	@Deprecated
	public void setLineNumber(String strLineNumber) {
	}

	@Deprecated
	public void setCallClass(Object objDivision) {
	}
}
