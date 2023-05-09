/**
 * <pre>
 * MQL Command 항목을 전역변수로 정의하는 Class.
 * </pre>
 *
 * @ClassName   : emdDBConstants.java
 * @Description : MQL Command 항목을 전역변수로 정의하는 Class.
 * @author      : BongJun,Park
 * @since       : 2020-11-06
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2020-11-06     BongJun,Park   최초 생성
 * </pre>
 */
package com.gsc.apps.app.util;

import java.text.DecimalFormat;

public interface gscDBConstants {
    /* RDB Mybatis Name space */
    public static final String NAME_SPACE_INTERFACE_LOG = "com.gsc.apps.interface.INTERFACE_LOG";
    public static final String NAME_SPACE_USER_IF = "com.gsc.apps.interface.USER_IF";
    public static final String NAME_SPACE_DEPT_IF = "com.gsc.apps.interface.DEPT_IF";
    public static final String NAME_SPACE_TKCAA001 = "com.gsc.apps.interface.TKCAA001";
    public static final String NAME_SPACE_KTPA001 = "com.gsc.apps.interface.KTPA001";
    public static final String NAME_SPACE_TKCBA001 = "com.gsc.apps.interface.TKCBA001";
    public static final String NAME_SPACE_SVW_KTPB001_99 = "com.gsc.apps.interface.SVW_KTPB001_99";
    public static final String NAME_SPACE_ENGINECOLOR_IF = "com.gsc.apps.interface.ENGINECOLOR_IF";
    public static final String NAME_SPACE_ENGINEPLATE_IF = "com.gsc.apps.interface.ENGINEPLATE_IF";
    public static final String NAME_SPACE_PROJECTINFO_IF = "com.gsc.apps.interface.PROJECTINFO_IF";
    public static final String NAME_SPACE_PROJECTDELIVERYDATE_IF = "com.gsc.apps.interface.PROJECTDELIVERYDATE_IF";
    public static final String NAME_SPACE_PARENTPROJECT_IF = "com.gsc.apps.interface.PARENTPROJECT_IF";
    public static final String NAME_SPACE_DESIGNSCHEDULE_IF = "com.gsc.apps.interface.DESIGNSCHEDULE_IF";
    public static final String NAME_SPACE_STANDARDPART_IF = "com.gsc.apps.interface.STANDARDPART_IF";
    public static final String NAME_SPACE_PART_IF = "com.gsc.apps.interface.PART_IF";
    public static final String NAME_SPACE_PLM_DRAWING = "com.gsc.apps.interface.PLM_DRAWING";
    public static final String NAME_SPACE_PROJECTPARTLIST_HEADER_IF = "com.gsc.apps.interface.PROJECTPARTLIST_HEADER_IF";
    public static final String NAME_SPACE_PROJECTPARTLIST_DETAIL_IF = "com.gsc.apps.interface.PROJECTPARTLIST_DETAIL_IF";
    public static final String NAME_SPACE_FINISHEDPROJECTPARTLIST_HEADER_IF = "com.gsc.apps.interface.FINISHEDPROJECTPARTLIST_HEADER_IF";
    public static final String NAME_SPACE_FINISHEDPROJECTPARTLIST_DETAIL_IF = "com.gsc.apps.interface.FINISHEDPROJECTPARTLIST_DETAIL_IF";
    public static final String NAME_SPACE_PLM_PARTLIST = "com.gsc.apps.interface.PLM_PARTLIST";
    public static final String NAME_SPACE_PROJECTPART_HEADER = "com.gsc.apps.interface.PROJECTPART_HEADER";
    public static final String NAME_SPACE_PROJECTPART_DETAIL = "com.gsc.apps.interface.PROJECTPART_DETAIL";
    public static final String NAME_SPACE_PROJECTPARTALLOC_HEADER = "com.gsc.apps.interface.PROJECTPARTALLOC_HEADER";
    public static final String NAME_SPACE_PROJECTPARTALLOC_DETAIL = "com.gsc.apps.interface.PROJECTPARTALLOC_DETAIL";
    public static final String NAME_SPACE_PROJECTBOM_IF = "com.gsc.apps.interface.PROJECTBOM_IF";
    public static final String NAME_SPACE_TKCAA050 = "com.gsc.apps.interface.TKCAA050";
    public static final String NAME_SPACE_BATCH_MANAGEMENT = "com.gsc.apps.interface.BATCH_MANAGEMENT";
    public static final String NAME_SPACE_LOG_TKCHB022_IF = "com.gsc.apps.interface.LOG_TKCHB022_IF";
    public static final String NAME_SPACE_TKCHB022_IF = "com.gsc.apps.interface.TKCHB022_IF";
    public static final String NAME_SPACE_SEND_DISTRIBUTION = "com.gsc.apps.interface.SEND_DISTRIBUTION";
    public static final String NAME_SPACE_CSBOM_BATCH = "com.gsc.apps.interface.CSBOM_BATCH";
    public static final String NAME_SPACE_TKQAA104 = "com.gsc.apps.interface.TKQAA104";
    public static final String NAME_SPACE_CS_FINAL_BOM_IF = "com.gsc.apps.interface.CS_FINAL_BOM_IF";
    public static final String NAME_SPACE_COPYROOM_IF = "com.gsc.apps.interface.COPYROOM_IF";
    public static final String NAME_SPACE_COPYROOMPRINTIF = "com.gsc.apps.interface.CopyRoomPrintIF";
    public static final String NAME_SPACE_ALL_AMSTM_VIEW = "com.gsc.apps.interface.ALL_AMSTM_VIEW";
    public static final String NAME_SPACE_DRAWING_BATCH = "com.gsc.apps.interface.DRAWING_BATCH";
    public static final String NAME_SPACE_DOCUMENT_BATCH = "com.gsc.apps.interface.DOCUMENT_BATCH";
    public static final String NAME_SPACE_PROJECTBOMIF_BATCH = "com.gsc.apps.interface.PROJECTBOMIF_BATCH";
    public static final String NAME_SPACE_MO_BATCH = "com.gsc.apps.interface.MO_BATCH";
    public static final String NAME_SPACE_SMG_BATCH = "com.gsc.apps.interface.SMG_BATCH";
    public static final String NAME_SPACE_MAKER_PARTLIST = "com.gsc.apps.interface.MAKER_PARTLIST";
    public static final String NAME_SPACE_POR_PLAN_IF = "com.gsc.apps.interface.POR_PLAN_IF";

    /* RDB Interface Name */
    public static final String INTERFACE_NAME_DEPT = "Dept";
    public static final String INTERFACE_NAME_PARTMANAGEMENTNO = "PartManagementNo";
    public static final String INTERFACE_NAME_ERPSTATUS = "ERPStatus";
    public static final String INTERFACE_NAME_PROJECT = "Project";
    public static final String INTERFACE_NAME_DELIVERYDATE = "DeliveryDate";
    public static final String INTERFACE_NAME_ENGINECOLOR = "EngineColor";
    public static final String INTERFACE_NAME_ENGINEPLATE = "EnginePlate";
    public static final String INTERFACE_NAME_PARTLISTHEADER = "PartListHeader";
    public static final String INTERFACE_NAME_PARTLISTITEM = "PartListItem";
    public static final String INTERFACE_NAME_STANDARDPART = "StandardPart";
    public static final String INTERFACE_NAME_PART = "Part";
    public static final String INTERFACE_NAME_PARENTPROJECT = "ParentProject";
    public static final String INTERFACE_NAME_DESIGNSCHEDULE = "DesignSchedule";
    public static final String INTERFACE_NAME_USER = "User";
    public static final String INTERFACE_NAME_PORPLAN = "PORPlan";
    public static final String INTERFACE_NAME_PORPLAN_DELETE = "PORPlanDel";
    public static final String INTERFACE_NAME_CS_FINAL_BOM = "CsFinalBom";
    public static final String INTERFACE_NAME_CS_FINAL_BOM_SELECT = "CsFinalBomSelect";
    public static final String INTERFACE_NAME_MAKER_PART_LIST = "MakerPartList";
    public static final String INTERFACE_NAME_SEND_DISTRIBUTION = "SendDistribution";

    /* ERP Web service Result */
    public static final String RESULT_SUCCESS = "S";
    public static final String RESULT_ERROR = "F";
    public static final String BOM_IF_SUCCESS = "S";
    public static final String BOM_IF_FAILED = "F";

    /* Log Type Code */
    public static final String LOG_ADD = "Add";
    public static final String LOG_ERROR = "Error";
    public static final String LOG_UPDATE = "Update";
    public static final String LOG_CHANGE_NAME = "Change Name";
    public static final String LOG_UPDATE_PARENT = "Update Parent";
    public static final String LOG_UPDATE_STATE = "Update State";
    public static final String LOG_UPDATE_BUSINESS_UNIT = "Update Business Unit";
    public static final String LOG_INACTIVATE = "Inactivate";
    public static final String LOG_DELETE = "Delete";

    /* Batch Result */
    public static final String ACTIVITY_STATUS = "ACTIVITY_STATUS";
    public static final String MESSAGE = "MESSAGE";
    public static final String ACTIVITY_STATUS_SUCCESS = "Success";
    public static final String ACTIVITY_STATUS_FAIL = "Fail";
    public static final String BATCH_ID = "BATCH_ID";
    public static final String BATCH_RERUN = "RERUN";

    /* Interface current */
    public static final String CURRENT_IF_REQUEST = "REQUEST";
    public static final String CURRENT_IF_BOM = "BOM";
    public static final String CURRENT_IF_PART = "PART";
    public static final String CURRENT_IF_ALLOC = "ALLOC";
    public static final String CURRENT_IF_COMPLETE = "COMPLETE";
    public static final String CURRENT_ACTIVITY_REQUEST = "Request";
    public static final String CURRENT_ACTIVITY_BOM = "Bom";
    public static final String CURRENT_ACTIVITY_PART = "Part";
    public static final String CURRENT_ACTIVITY_ALLOCATION = "Allocation";

    /* MBOM SpareType Code */
    public static final String SPARETYPE_ADDITIONAL_SPARE_PART = "Additional_Spare_Part";
    public static final String SPARETYPE_GENERAL = "General";
    public static final String SPARETYPE_STANDARD_SPARE_PART = "Standard_Spare_Part";
    public static final String SPARETYPE_WORKING_SPARE_PART = "Working_Spare_Part";

    /* ERP Web service Default Code */
    public static final String DATA_MANDT = "100";  // 클라이언트 100 고정
    public static final String DATA_WERKS = "1400";  // 플랜트 1400 고정

    /* Interface mapping xml */
    public static final String MAPPING_XML_PROJECTINFO = "/DBMap/mapping/mapping-projectInfo.xml";   // 수통정보(공사정보)
    public static final String MAPPING_XML_PORPLAN = "/DBMap/mapping/mapping-porplan.xml";   // POR Plan
    public static final String MAPPING_XML_PORPLAN_DELETE = "/DBMap/mapping/mapping-porplan-delete.xml";   // POR Plan delete
    public static final String MAPPING_PROJECT_DELIVERYDATE = "/DBMap/mapping/mapping-project-deliverydate.xml";  // 공사일정, 공사 상태
    public static final String MAPPING_XML_HGS_CS_BOM = "/DBMap/mapping/mapping-hgs-cs-bom.xml";        // mapping table : TKQAA104 - KST00810
    public static final String MAPPING_DRAWINGDOCUMENT_XML = "/DBMap/mapping/mapping-drawingdocument.xml";
    public static final String MAPPING_DOCUMENT_XML = "/DBMap/mapping/mapping-document.xml";
    public static final String MAPPING_XML_HEADER = "/DBMap/mapping/mapping-partlistheader.xml";        // mapping table : 공사BOM Header I/F 입력 Table
    public static final String MAPPING_XML_ITEM = "/DBMap/mapping/mapping-partlistitem.xml";            // mapping table : 공사 BOM Item I/F 입력 Table
    public static final String MAPPING_XML_PRODUCTMASTER = "/DBMap/mapping/mapping-productmaster.xml";  // mapping table : 자재마스터 생성 입력 Table
    public static final String MAPPING_XML_ZSTKAT0010 = "/DBMap/mapping/mapping-zstkat0010.xml";        // mapping table : 자재마스터 생성 결과 Table
    public static final String MAPPING_XML_ZSTKAT0016 = "/DBMap/mapping/mapping-zstkat0016.xml";        // mapping table : 공사 BOM ERP Partlist Item 결과
    public static final String MAPPING_XML_ZSTKAT0128 = "/DBMap/mapping/mapping-zstkat0128.xml";        // mapping table : 매출완료 이후 ERP Partlist Header Table
    public static final String MAPPING_XML_ZSTKAT0129 = "/DBMap/mapping/mapping-zstkat0129.xml";        // mapping table : 매출 완료 이후 ERP Partlist Item Table
    public static final String MAPPING_XML_ZSTKAS1040 = "/DBMap/mapping/mapping-zstkas1040.xml";        // mapping table : 3번째_자재 할당

    static DecimalFormat df00 = new DecimalFormat("00");
    static DecimalFormat df000 = new DecimalFormat("000");

    public static final int AXIS2_TIMEOUT_MILLISECONDS = 9000000;
}
