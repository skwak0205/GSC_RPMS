/**
 * <pre>
 * 상세 Log Util Class. Log를 호출한 Class의 명과 줄번호까지 Log에 저장.
 * </pre>
 * 
 * @ClassName   : emdCommonDetailLogUtil.java
 * @Description : 상세 Log Util Class. Log를 호출한 Class의 명과 줄번호까지 Log에 저장.
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
 
public class emdCommonDetailLogUtil implements emdILogUtil{

	private String CALL_CLASS = null; 
	private String _callClassName  = null;
	private String _callMethodName = null;
	private String _lineNumber     = null;
	private String _fileName       = null;

	/**
	 * <pre>
	 * 호출한 Class를 세팅하는 메서드.
	 * </pre>
	 * 
	 * @param objDivision Object
	 */
	public void setCallClass(Object objDivision)  {
		CALL_CLASS = objDivision.toString();
	}
	
	/**
	 * <pre>
	 * Log가 발생한 Class정보를 바탕으로 Log를 호출한 Class와 메서드명, 줄번호를 메세지에 저장하여 리턴한다. 
	 * </pre>
	 * 
	 * @return String
	 */
	private String getMessage() {
		StringBuffer sbfMessage = new StringBuffer();
		
		if (this._fileName != null ) {
			sbfMessage.append(this._fileName + " (");
		}
		
		if (this._callClassName != null ) {
			sbfMessage.append("[CLASS: " + this._callClassName +"]");
		}
		if (this._callMethodName != null ) {
			sbfMessage.append("[METHOD: " + this._callMethodName +"]");
		}
		if (this._lineNumber != null ) {
			sbfMessage.append("[LINE: " + this._lineNumber +"]");
		}
		if (this._fileName != null ) {
			sbfMessage.append(") : ");
		}
		
		return sbfMessage.toString();
	}

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
		Logger log = LoggerFactory.getLogger(CALL_CLASS);

		log.error(getMessage() + strMessage);
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
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		
		log.error(getMessage() + strMessage, args);
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
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		
		log.warn(getMessage() + strMessage);
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
	public void warn(String strMessage, String... args)   {
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		log.warn(getMessage() + strMessage, args);
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
	 * @param String 
	 */
	public void info(String strMessage)  {
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		
		log.info(getMessage() + strMessage);
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
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		
		log.info(getMessage() + strMessage, args);
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
	 * @param String 
	 */
	public void debug(String strMessage)  {
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		
		log.debug(getMessage() + strMessage);
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
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		
		log.debug(getMessage() + strMessage, args);
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
	 * @param String 
	 */
	public void trace(String strMessage)  {
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		
		log.trace(getMessage() + strMessage);
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
		Logger log = LoggerFactory.getLogger(CALL_CLASS);
		
		log.trace(getMessage() + strMessage, args);
	}

	/**
	 * <pre>
	 * Log 를 발생한 Class의 파일명을 Setting 
	 * </pre>
	 * 
	 * @param strFileName String
	 */
	public void setFileName(String strFileName) {
		this._fileName = strFileName;
		
	}
	
	/**
	 * <pre>
	 * Log 를 발생한 Class의 메서드명을 Setting 
	 * </pre>
	 * 
	 * @param strCallMethod String
	 */
	public void setCallMethod(String strCallMethod) {
		this._callMethodName = strCallMethod;
	}

	/**
	 * <pre>
	 * Log 를 발생한 Class의 명을 Setting 
	 * </pre>
	 * 
	 * @param strCallClass String
	 */
	public void setCallClassName(String strCallClass) {
		this._callClassName = strCallClass;
	}

	/**
	 * <pre>
	 * Log 를 발생한 Class의 줄번호를 Setting 
	 * </pre>
	 * 
	 * @param strLineNumber String
	 */
	public void setLineNumber(String strLineNumber) {
		this._lineNumber = strLineNumber;
	}
}
