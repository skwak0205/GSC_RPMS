/**
 * <pre>
 * Role Constants 항목을 전역변수로 정의하는 Class.
 * </pre>
 *
 * @ClassName   : gscRoleConstants.java
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

public interface gscRoleConstants {

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
    public static final String SYMBOLIC_ROLE_GSCVARIANTMANAGER = "role_gscVariantManager";
    public static final String ROLE_GSCVARIANTMANAGER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_GSCVARIANTMANAGER);

    /* 승인도 관리자 Role */
    public static final String SYMBOLIC_ROLE_GSCMFGADMANAGER = "role_gscMFGADManager";
    public static final String ROLE_GSCMFGADMANAGER = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_GSCMFGADMANAGER);

    /* 공사별 승인도 관리자 Role */
    public static final String SYMBOLIC_ROLE_GSCMFGADCREATOR = "role_gscMFGADCreator";
    public static final String ROLE_GSCMFGADCREATOR = PropertyUtil.getSchemaProperty(SYMBOLIC_ROLE_GSCMFGADCREATOR);
}
