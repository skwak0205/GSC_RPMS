/**
 * <pre>
 * Role Constants 항목을 전역변수로 정의하는 Class.
 * </pre>
 *
 * @ClassName   : emdRoleConstants.java
 * @Description : Role Constants 항목을 전역변수로 정의하는 Class.
 *                하나의 Role에 대해 아래 순서로 기술한다.
 *                1) Role Fileld 주석 정의. 여러줄 주석을 이용.
 *                   작성예제.
 *                     - \/* 관리자 Role *\/
 *	              2) Role Symbolic Name 정의
 *                   작성 예제.
 *                     - public static final String SYMBOLIC_ROLE_EMPLOYEE = "role_Employee";
 *                3) Role Original Name 정의.
 *                   작성 예제.
 *                     - public static final String ROLE_EXCHANGEUSER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_EXCHANGEUSER);
 * @author      : BongJun,Park
 * @since       : 2020-07-28
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-07-28     BongJun,Park   최초 생성
 * </pre>
 */
package com.gsc.apps.common.constants;

import com.matrixone.apps.domain.util.PropertyUtil;

public interface emdRoleConstants {

    /* Employee Role */
    public static final String SYMBOLIC_ROLE_EMPLOYEE = "role_Employee";
    public static final String ROLE_EMPLOYEE = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_EMPLOYEE);

    /* Employee Role */
    public static final String SYMBOLIC_ROLE_BASICUSER = "role_BasicUser";
    public static final String ROLE_BASICUSER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_BASICUSER);

    /* ExchangeUser Role */
    public static final String SYMBOLIC_ROLE_EXCHANGEUSER = "role_ExchangeUser";
    public static final String ROLE_EXCHANGEUSER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_EXCHANGEUSER);

    /* ProjectUser Role */
    public static final String SYMBOLIC_ROLE_PROJECTUSER = "role_ProjectUser";
    public static final String ROLE_PROJECTUSER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_PROJECTUSER);

    /* ExternalProjectUser Role */
    public static final String SYMBOLIC_ROLE_EXTERNALPROJECTUSER = "role_ExternalProjectUser";
    public static final String ROLE_EXTERNALPROJECTUSER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_EXTERNALPROJECTUSER);

    /* 사양담당관리자 Role */
    public static final String SYMBOLIC_ROLE_EMDVARIANTMANAGER = "role_emdVariantManager";
    public static final String ROLE_EMDVARIANTMANAGER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_EMDVARIANTMANAGER);

    /* 승인도 관리자 Role */
    public static final String SYMBOLIC_ROLE_EMDMFGADMANAGER = "role_emdMFGADManager";
    public static final String ROLE_EMDMFGADMANAGER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_EMDMFGADMANAGER);

    /* 공사별 승인도 관리자 Role */
    public static final String SYMBOLIC_ROLE_EMDMFGADCREATOR = "role_emdMFGADCreator";
    public static final String ROLE_EMDMFGADCREATOR = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_EMDMFGADCREATOR);
}
