/**
 * <pre>
 * Log Util 관련 interface class
 * </pre>
 * 
 * @ClassName   : emxILogUtil.java
 * @Description : Log Util 관련 interface class
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

package com.gsc.apps.common.util.impl;

public interface emdILogUtil {
	/**
	 * <pre>
	 * Log를 호출한 Class를 세팅하는 추상화 메서드
	 * </pre>
	 * 
	 * @param Object objDivision
	 */
	public void setCallClass(Object objDivision) ;
	
	/**
	 * <pre>
	 * Log를 호출한 Class의 메서드명을 세팅하는 추상화 메서드
	 * </pre>
	 * 
	 * @param Object objDivision
	 */
	public void setCallMethod(String strCallMethod);
	
	/**
	 * <pre>
	 * Log를 호출한 Class의 전체 Package를 세팅하는 추상화 메서드
	 * </pre>
	 * 
	 * @param Object objDivision
	 */
	public void setCallClassName(String strCallClass);
	
	/**
	 * <pre>
	 * Log를 호출한 Class의 줄번호를 세팅하는 추상화 메서드
	 * </pre>
	 * 
	 * @param Object objDivision
	 */
	public void setLineNumber(String strLineNumber);
	
	/**
	 * <pre>
	 * Log를 호출한 Class의 파일명을 세팅하는 추상화 메서드
	 * </pre>
	 * 
	 * @param Object objDivision
	 */
	public void setFileName(String strFileName);
	
	/**
	 * 
	 * <pre>
	 * error 일반 로그 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * error("Error Message")	===> Error Message
	 * </pre>
	 * 
	 * @param strMessage String
	 */
	public void error(String strMessage) ;
	
	/**
	 * 
	 * <pre>
	 * error 메세지 파라미터로 받아서 로그를 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * error("Error Message {} {}", "A", "B")	===> Error Message A B
	 * error("Error Message {} {}", new String[]{"A", "B"}); ===> Error Message A B
	 * </pre>
	 * 
	 * @param strMessage String
	 * @param args String...
	 */
	public void error(String strMessage, String... args) ;
	
	/**
	 * 
	 * <pre>
	 * Warning 일반 로그 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * warn("Warning Message")	===> Warning Message
	 * </pre>
	 * 
	 * @param strMessage String
	 */
	public void warn(String strMessage) ;
	
	/**
	 * 
	 * <pre>
	 * Warning 메세지 파라미터로 받아서 로그를 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * warn("Warning Message {} {}", "A", "B")	===> Warning Message A B
	 * warn("Warning Message {} {}", new String[]{"A", "B"}); ===> Warning Message A B
	 * </pre>
	 * 
	 * @param strMessage String
	 * @param args String...
	 */
	public void warn(String strMessage, String... args) ;
	
	/**
	 * 
	 * <pre>
	 * 정보관련 일반 로그 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * info("Information Message")	===> Information Message
	 * </pre>
	 * 
	 * @param strMessage String
	 */
	public void info(String strMessage) ;
	
	/**
	 * 
	 * <pre>
	 * 정보관련 메세지 파라미터로 받아서 로그를 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * info("Information Message {} {}", "A", "B")	===> Information Message A B
	 * info("Information Message {} {}", new String[]{"A", "B"}); ===> Information Message A B
	 * </pre>
	 * 
	 * @param strMessage String
	 * @param args String...
	 */
	public void info(String strMessage, String... args) ;
	
	/**
	 * 
	 * <pre>
	 * debug 일반 로그 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * debug("Debug Message")	===> Debug Message
	 * </pre>
	 * 
	 * @param strMessage String
	 */
	public void debug(String strMessage) ;
	
	/**
	 * 
	 * <pre>
	 * debug 메세지 파라미터로 받아서 로그를 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * debug("Debug Message {} {}", "A", "B")	===> Debug Message A B
	 * debug("Debug Message {} {}", new String[]{"A", "B"}); ===> Debug Message A B
	 * </pre>
	 * 
	 * @param strMessage String
	 * @param args String...
	 */
	public void debug(String strMessage, String... args) ;
	
	/**
	 * 
	 * <pre>
	 * trace 일반 로그 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * trace("Trace Message")	===> Trace Message
	 * </pre>
	 * 
	 * @param strMessage String
	 */
	public void trace(String strMessage) ;
	
	/**
	 * 
	 * <pre>
	 * trace 메세지 파라미터로 받아서 로그를 처리하는  추상화 메서드
	 * 
	 * [사용 예제]
	 * trace("Trace Message {} {}", "A", "B")	===> Trace Message A B
	 * trace("Trace Message {} {}", new String[]{"A", "B"}); ===> Trace Message A B
	 * </pre>
	 * 
	 * @param strMessage String
	 * @param args String...
	 */
	public void trace(String strMessage, String... args) ;
}
