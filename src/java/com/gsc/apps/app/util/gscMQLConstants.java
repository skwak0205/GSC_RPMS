/**
 * <pre>
 * MQL Command 항목을 전역변수로 정의하는 Class.
 * </pre>
 *
 * @ClassName   : gscMQLConstants.java
 * @Description : MQL Command 항목을 전역변수로 정의하는 Class.
 * @author      : BongJun,Park
 * @since       : 2020-05-20
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-05-20     BongJun,Park   최초 생성
 * </pre>
 */

package com.gsc.apps.app.util;

public interface gscMQLConstants {
    /* MQL COMAND */
    public static final String DIRECTION_FROM            = "from";
    public static final String DIRECTION_TO              = "to";
    public static final String RECURSE_TO_0              = "0";
    public static final String RECURSE_TO_1              = "1";
    public static final String RECURSE_TO_END            = "end";

    /* print bus */
    public static final String PRINT_BUS_ID             = "print bus $1 select $2 dump";
    public static final String PRINT_BUS_ID1            = "print bus $1 select $2 dump $3";
    public static final String PRINT_BUS_ID2            = "print bus $1 select $2 $3 dump $4";
    public static final String PRINT_BUS_ID3            = "print bus $1 select $2 $3 $4 dump $5";
    public static final String PRINT_BUS_ID4            = "print bus $1 select $2 $3 $4 $5 dump $6";
    public static final String PRINT_BUS_ID5            = "print bus $1 select $2 $3 $4 $5 $6 dump $7";
    public static final String PRINT_BUS_TNR           = "print bus $1 $2 $3 select $4 dump";
    public static final String PRINT_BUS_TNR1           = "print bus $1 $2 $3 select $4 dump $5";
    public static final String PRINT_BUS_ID3_EX_DUMP    = "print bus $1 select $2 $3 $4";

    /* temp bus */
    public static final String TEMP_BUS1                = "temp query bus $1 $2 $3 select $4 dump $5";
    public static final String TEMP_BUS2                = "temp query bus $1 $2 $3 select $4 $5 dump $6";
    public static final String TEMP_BUS3                = "temp query bus $1 $2 $3 select $4 $5 $6 dump $7";
    public static final String TEMP_BUS_WHERE1          = "temp query bus $1 $2 $3 where $4 select $5 dump $6";
    public static final String TEMP_BUS_WHERE2          = "temp query bus $1 $2 $3 where $4 select $5 $6 dump $7";
    public static final String TEMP_BUS_WHERE3          = "temp query bus $1 $2 $3 where $4 select $5 $6 $7 dump $8";
    public static final String TEMP_BUS_WHERE4          = "temp query bus $1 $2 $3 where $4 select $5 $6 $7 $8 dump $9";
    public static final String TEMP_BUS_VAULT_LIMIT_WHERE1          = "temp query bus $1 $2 $3 vault $4 limit $5 where $6 select $7 dump $8";

    /* modify bus */
    public static final String MOD_BUS_CURRENT = "mod bus $1 current $2";

    /* delete bus */
    public static final String MOD_BUS_DELETE = "delete bus $1";

    /* print connection */
    public static final String PRINT_CONNECTION_ID1 = "print connection $1 select $2 dump";
    public static final String PRINT_CONNECTION_ID2 = "print connection $1 select $2 $3 dump $4";

    /* add connection */
    public static final String ADD_CONNECTION = "add connection $1 from $2 to $3";
    public static final String ADD_CONNECTION_ATTRIBUTE = "add connection $1 from $2 to $3 $4 $5";
    public static final String ADD_CONNECTION_SELECT = "add connection $1 from $2 to $3 select $4 dump $5";

    /* mod connection */
    public static final String MOD_CONNECTION_ID1 = "mod connection $1 $2 $3";
    public static final String MOD_CONNECTION_ID3 = "mod connection $1 $2 $3 $4 $5 $6 $7";

    /* add connection(torel) */
    public static final String ADD_CONNECTION_TOREL = "add connection $1 from $2 torel $3";

    /* del connection */
    public static final String DEL_CONNECTION = "del connection $1";

    /* Approve */
    public static final String APPROVE_BUS_SIGNATURE = "approve bus $1 signature $2";
    public static final String UNSIGN_BUS_SIGNATURE = "unsign bus $1 signature $2";

    /* expand bus */
    public static final String EXPAND_BUS_DIRECTION_RECURSE_REL1            = "expand bus $1 $2 rel $3 recurse to $4 select bus $5 dump $6";
    public static final String EXPAND_BUS_TNR_FROM_REL_TEYP_BUS1            = "expand bus $1 $2 $3 from rel $4 type $5 select bus $6 where $7 dump $8";
    public static final String EXPAND_BUS_FROM_REL_TYPE_REL1                = "expand bus $1 from rel $2 type $3 select rel $4 dump 5";
    public static final String EXPAND_BUS_FROM_REL_TEYP_RECURSE_BUS1        = "expand bus $1 from rel $2 type $3 recurse to $4 select bus $5 dump $6";
    public static final String EXPAND_BUS_FROM_REL_TEYP_RECURSE_REL1        = "expand bus $1 from rel $2 type $3 recurse to $4 select rel $5 dump $6";
    public static final String EXPAND_BUS_FROM_REL_RECURSE_BUS_WHERE        = "expand bus $1 from rel $2 recurse to $3 select bus $4 where $5 dump $6";
    public static final String EXPAND_BUS_DIRECTION_RECURSE_BUS_WHERE1      = "expand bus $1 $2 rel $3 recurse to $4 select bus $5 where $6 dump $7";
    public static final String EXPAND_BUS_REL_BUS_DIRECTION_RECURSE_WHERE1  = "expand bus $1 $2 rel $3 type $4 recurse to $5 select bus $6 where $7 dump $8";
}
