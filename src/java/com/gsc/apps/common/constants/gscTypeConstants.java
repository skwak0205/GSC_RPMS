/**
 * <pre>
 * Type Constants 항목을 전역변수로 정의하는 Class.
 * </pre>
 * 
 * @ClassName   : gscTypeConstants.java
 * @Description : Type Constants 항목을 전역변수로 정의하는 Class.
 *                하나의 Type에 대해 아래 순서로 기술한다.
 *                1) Type Fileld 주석 정의. 여러줄 주석을 이용.
 *  *                   작성예제.
 *  *                     - \/* 시험품 *\/
 *  *                  2) Type Symbolic Name 정의
 *  *                   작성 예제.
 *  *                     - public static final String SYMBOLIC_TYPE_GSCTESTPART = "type_gscTestPart";
 *  *                3) Type Original Name 정의.
 *                   작성 예제.
 *                     - public static final String TYPE_GSCTESTPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCTESTPART);
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

package com.gsc.apps.common.constants;

import com.matrixone.apps.domain.util.PropertyUtil;
import com.matrixone.apps.library.LibraryCentralConstants;

public interface gscTypeConstants {
    /* Test Part */
    public static final String SYMBOLIC_TYPE_GSCTESTPART = "type_gscTestPart";
    public static final String TYPE_GSCTESTPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCTESTPART);
    
    /* Engine Group */
    public static final String SYMBOLIC_TYPE_MODEL = "type_Model";
    /* Engine Model Group */
    public static final String SYMBOLIC_TYPE_GSCENGINEMODELGROUP = "type_gscEngineModelGroup";
    public static final String TYPE_GSCENGINEMODELGROUP          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCENGINEMODELGROUP);
    /* Engine Model  */
    public static final String SYMBOLIC_TYPE_GSCENGINEMODEL = "type_gscEngineModel";
    public static final String TYPE_GSCENGINEMODEL          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCENGINEMODEL);
    
    /* Engineering Order*/
    public static final String SYMBOLIC_TYPE_GSCENGINEERINGORDER  = "type_gscEngineeringOrder";
    public static final String TYPE_GSCENGINEERINGORDER           = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCENGINEERINGORDER);
    
    /* PSN Mater */
    public static final String SYMBOLIC_TYPE_GSCPSNMASTER = "type_gscPSNMaster";
    public static final String TYPE_GSCPSNMASTER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPSNMASTER);

    /* PSN */
    public static final String SYMBOLIC_TYPE_GSCPSN = "type_gscPSN";
    public static final String TYPE_GSCPSN          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPSN);

    /* General Library */
    public static final String TYPE_GENERAL_LIBRARY = LibraryCentralConstants.TYPE_GENERAL_LIBRARY;

    /* Change Request */
    public static final String SYMBOLIC_TYPE_GSCCHANGEREQUEST  = "type_gscChangeRequest";
    public static final String TYPE_GSCCHANGEREQUEST           = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCHANGEREQUEST);
    
    /* Part Library */
    public static final String TYPE_PART_LIBRARY               = LibraryCentralConstants.TYPE_PART_LIBRARY;
    
    /* Document Library */
    public static final String TYPE_DOCUMENT_LIBRARY           = LibraryCentralConstants.TYPE_DOCUMENT_LIBRARY;

    /* Engine Group Part */
    public static final String SYMBOLIC_TYPE_GSCENGINEGROUPPART = "type_gscEngineGroupPart";
    public static final String TYPE_GSCENGINEGROUPPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCENGINEGROUPPART);
    
    /* EOD Mater */
    public static final String SYMBOLIC_TYPE_GSCEODMASTER = "type_gscEODMaster";
    public static final String TYPE_GSCEODMASTER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCEODMASTER);

    /* EOD */
    public static final String SYMBOLIC_TYPE_GSCEOD = "type_gscEOD";
    public static final String TYPE_GSCEOD          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCEOD);
    
    /* B-Sheet Abstract Type */
    public static final String SYMBOLIC_TYPE_GSCBSHEETOBJECT       = "type_gscBSHEETOBJECT";
    public static final String TYPE_GSCBSHEETOBJECT                = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCBSHEETOBJECT);
    
    /* EO B-Sheet Type */
    public static final String SYMBOLIC_TYPE_GSCEOBSHEETOBJECT     = "type_gscEOBSheetObject";
    public static final String TYPE_GSCEOBSHEETOBJECT              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCEOBSHEETOBJECT);
    
    /* S & O B-Sheet Type */
    public static final String SYMBOLIC_TYPE_GSCSOBSHEETOBJECT     = "type_gscSOBSheetObject";
    public static final String TYPE_GSCSOBSHEETOBJECT              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCSOBSHEETOBJECT);
    
    /* Order Option List B-SheeT TYPE */
    public static final String SYMBOLIC_TYPE_GSCOCOBSHEETOBJECT    = "type_gscOCOBSheetObject";
    public static final String TYPE_GSCOCOBSHEETOBJECT             = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCOCOBSHEETOBJECT);

    /* MO B-Sheet Type */
    public static final String SYMBOLIC_TYPE_GSCMOBSHEETOBJECT     = "type_gscMOBSheetObject";
    public static final String TYPE_GSCMOBSHEETOBJECT              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCMOBSHEETOBJECT);
    
    /* BPC Mater */
    public static final String SYMBOLIC_TYPE_GSCBPCMASTER          = "type_gscBPCMaster";
    public static final String TYPE_GSCBPCMASTER                   = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCBPCMASTER);

    /* BPC */
    public static final String SYMBOLIC_TYPE_GSCBPC                = "type_gscBPC";
    public static final String TYPE_GSCBPC                         = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCBPC);

    /* S&O Engineering Order TYPE */
    public static final String SYMBOLIC_TYPE_GSCSOCHANGEORDER      = "type_gscSOChangeOrder";
    public static final String TYPE_GSCSOCHANGEORDER               = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCSOCHANGEORDER);
    
    /* Manufacturing Order TYPE */
    public static final String SYMBOLIC_TYPE_GSCMANUFACTURINGORDER = "type_gscManufacturingOrder";
    public static final String TYPE_GSCMANUFACTURINGORDER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCMANUFACTURINGORDER);
    
    /* Order Option List Change Order Type*/
    public static final String SYMBOLIC_TYPE_GSCORDEROPTIONLISTORDER = "type_gscOrderOptionListOrder";
    public static final String TYPE_GSCORDEROPTIONLISTORDER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCORDEROPTIONLISTORDER);
    
    /* CR Review Results Type*/
    public static final String SYMBOLIC_TYPE_GSCCRREVIEWRESULTS     = "type_gscCRReviewResults";
    public static final String TYPE_GSCCRREVIEWRESULTS              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCRREVIEWRESULTS);
    
    /* PFG Part */
    public static final String SYMBOLIC_TYPE_GSCPFGPART = "type_gscPFGPart";
    public static final String TYPE_GSCPFGPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPFGPART);
    
    /* PFG Variant Part */
    public static final String SYMBOLIC_TYPE_GSCPFGVARIANTPART = "type_gscPFGVariantPart";
    public static final String TYPE_GSCPFGVARIANTPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPFGVARIANTPART);
    
    /* Code Master Type*/
    public static final String SYMBOLIC_TYPE_GSCCODEMASTER          = "type_gscCodeMaster";
    public static final String TYPE_GSCCODEMASTER                   = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCODEMASTER);
    
    /* Reserved Part No*/
    public static final String SYMBOLIC_TYPE_GSCRESERVEDPARTNO   	= "type_gscReservedPartNo";
    public static final String TYPE_GSCRESERVEDPARTNO            	= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCRESERVEDPARTNO);
    
    /* Latest PFG Part*/
    public static final String SYMBOLIC_GSCLATESTPFGPART       = "gscLatestPFGPart";
    public static final String GSCLATESTPFGPART                = PropertyUtil.getSchemaProperty(SYMBOLIC_GSCLATESTPFGPART);
    
    /* VPMReference (CATIA 3D)*/
    public static final String SYMBOLIC_TYPE_VPMREFERENCE = "type_VPMReference";
    public static final String TYPE_VPMREFERENCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_VPMREFERENCE);
    
    /* Drawing (CATIA 2D)*/
    public static final String SYMBOLIC_TYPE_DRAWING  = "type_Drawing";
    public static final String TYPE_DRAWING                 = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_DRAWING);
    
    /* Approval Line */
    public static final String SYMB_TYPE_GSCAPPROVALLINE       = "type_gscApprovalLine";
    public static final String TYPE_GSCAPPROVALLINE            = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCAPPROVALLINE);

    /* Approval Request */
    public static final String SYMB_TYPE_GSCAPPROVALREQUEST       = "type_gscApprovalRequest";
    public static final String TYPE_GSCAPPROVALREQUEST           = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCAPPROVALREQUEST);

    /* Series */
    public static final String SYMB_TYPE_GSCSERIES           = "type_gscSeries";
    public static final String TYPE_GSCSERIES                = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCSERIES);

    /* Ship */
    public static final String SYMB_TYPE_GSCSHIP             = "type_gscShip";
    public static final String TYPE_GSCSHIP                  = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCSHIP);

    /* Project */
    public static final String SYMB_TYPE_GSCPROJECT          = "type_gscProject";
    public static final String TYPE_GSCPROJECT               = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCPROJECT);

    /* Package Project */
    public static final String SYMB_TYPE_GSCPACKAGEPROJECT   = "type_gscPackageProject";
    public static final String TYPE_GSCPACKAGEPROJECT        = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCPACKAGEPROJECT);

    /* Project Remark */
    public static final String SYMB_TYPE_GSCPROJECTREMARK    = "type_gscProjectRemark";
    public static final String TYPE_GSCPROJECTREMARK         = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCPROJECTREMARK);

    /* Project EOD */
    public static final String SYMB_TYPE_GSCPROJECTEOD    = "type_gscProjectEOD";
    public static final String TYPE_GSCPROJECTEOD         = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCPROJECTEOD);
    
    /* Latest PFG */
    public static final String SYMB_TYPE_GSCLATESTPFGPART    = "type_gscLatestPFGPart";
    public static final String TYPE_GSCLATESTPFGPART         = PropertyUtil.getSchemaProperty(SYMB_TYPE_GSCLATESTPFGPART);
    
    /* Review Check Abstract Type */
    public static final String SYMBOLIC_TYPE_GSCCOREVIEWCHECKLIST          = "type_gscCOREVIEWCHECKLIST";
    public static final String TYPE_GSCCOREVIEWCHECKLIST                   = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCOREVIEWCHECKLIST);

    /* S&O Review Check List Type */
    public static final String SYMBOLIC_TYPE_GSCCHANGEORDERCHECKLIST       = "type_gscChangeOrderCheckList";
    public static final String TYPE_GSCCHANGEORDERCHECKLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCHANGEORDERCHECKLIST);

    /* S&O Review Check List Master Type */
    public static final String SYMBOLIC_TYPE_GSCCHANGEORDERCHECKLISTMASTER = "type_gscChangeOrderCheckListMaster";
    public static final String TYPE_GSCCHANGEORDERCHECKLISTMASTER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCHANGEORDERCHECKLISTMASTER);
    
    /* AD Engine Group Type*/
    public static final String SYMBOLIC_TYPE_GSCADENGINEGROUP	= "type_gscADEngineGroup";
    public static final String TYPE_GSCADENGINEGROUP 			= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCADENGINEGROUP);
    
    /* AD Model Type*/
    public static final String SYMBOLIC_TYPE_GSCADMODEL		= "type_gscADModel";
    public static final String TYPE_GSCADMODEL 				= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCADMODEL);
    
    /* ADG Type*/
    public static final String SYMBOLIC_TYPE_GSCADG		= "type_gscADG";
    public static final String TYPE_GSCADG 				= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCADG);
    
    /* ADG Variant Part Type*/
    public static final String SYMBOLIC_TYPE_GSCADVARIANTPART		= "type_gscADGVariantPart";
    public static final String TYPE_GSCADVARIANTPART 				= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCADVARIANTPART);
    
    /* AD Part Type*/
    public static final String SYMBOLIC_TYPE_GSCADPART		= "type_gscADPart";
    public static final String TYPE_GSCADPART 				= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCADPART);
    /* Service Document */
    public static final String SYMBOLIC_TYPE_GSCSERVICEDOCUMENT = "type_gscServiceDocument";
    public static final String TYPE_GSCSERVICEDOCUMENT          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCSERVICEDOCUMENT);

    /* PPRContext */
    public static final String SYMBOLIC_TYPE_PPRCONTEXT             = "type_PPRContext";
    public static final String TYPE_PPRCONTEXT                      = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_PPRCONTEXT);

    /* Engine Group MFG */
    public static final String SYMBOLIC_TYPE_GSCENGINEGROUPMFG      = "type_gscEngineGroupMfg";
    public static final String TYPE_GSCENGINEGROUPMFG               = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCENGINEGROUPMFG);

    /* PFG Part MFG*/
    public static final String SYMBOLIC_TYPE_GSCPFGMFG              = "type_gscPFGMfg";
    public static final String TYPE_GSCPFGMFG                       = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPFGMFG);

    /* PFG Variant Part MFG*/
    public static final String SYMBOLIC_TYPE_GSCPFGVARIANTMFG       = "type_gscPFGVariantMfg";
    public static final String TYPE_GSCPFGVARIANTMFG                = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPFGVARIANTMFG);

    /* Part MFG*/
    public static final String SYMBOLIC_TYPE_GSCASSEMBLYMFG         = "type_gscAssemblyMfg";
    public static final String TYPE_GSCASSEMBLYMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCASSEMBLYMFG);

    /* Engine Group */
    public static final String SYMBOLIC_TYPE_GSCENGINEGROUP         = "type_gscEngineGroup";
    public static final String TYPE_GSCENGINEGROUP                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCENGINEGROUP);

    /* PFG */
    public static final String SYMBOLIC_TYPE_GSCPFG                 = "type_gscPFG";
    public static final String TYPE_GSCPFG                          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPFG);

    /* PFG Variant */
    public static final String SYMBOLIC_TYPE_GSCPFGVARIANT          = "type_gscPFGVariant";
    public static final String TYPE_GSCPFGVARIANT                   = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPFGVARIANT);
    
    /* 3D Shape */
    public static final String SYMBOLIC_TYPE_3DSHAPE = "type_3DShape";
    public static final String TYPE_3DSHAPE          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_3DSHAPE);
    
    /* type_VPMRepReference */
    public static final String SYMBOLIC_TYPE_VPMREPREFERENCE = "type_VPMRepReference";
    public static final String TYPE_VPMREPREFERENCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_VPMREPREFERENCE);

    /* type_gscMFGModel */
    public static final String SYMBOLIC_TYPE_GSCMFGMODEL = "type_gscMFGModel";
    public static final String TYPE_GSCMFGMODEL          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCMFGMODEL);
    
    /* type_gscChangeValidationMaster */
    public static final String SYMBOLIC_TYPE_GSCCHANGEVALIDATIONMASTER = "type_gscChangeValidationMaster";
    public static final String TYPE_GSCCHANGEVALIDATIONMASTER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCHANGEVALIDATIONMASTER);
    
    /* type_gscChangeValidation */
    public static final String SYMBOLIC_TYPE_GSCCHANGEVALIDATION       = "type_gscChangeValidation";
    public static final String TYPE_GSCCHANGEVALIDATION          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCHANGEVALIDATION);

    /* type_gscSBOMEngineModel */
    public static final String SYMBOLIC_TYPE_GSCSBOMENGINEMODEL       = "type_gscSBOMEngineModel";
    public static final String TYPE_GSCSBOMENGINEMODEL           = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCSBOMENGINEMODEL);

    /* type_gscSBOMGroup */
    public static final String SYMBOLIC_TYPE_GSCSBOMGROUP       = "type_gscSBOMGroup";
    public static final String TYPE_GSCSBOMGROUP            = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCSBOMGROUP);

    /* type_gscUnitExtension */
    public static final String SYMBOLIC_TYPE_GSCUNITEXTENSIONREQUEST       = "type_gscUnitExtensionRequest";
    public static final String TYPE_GSCUNITEXTENSIONREQUEST            = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCUNITEXTENSIONREQUEST);

    /* type_gscDesignReflection */
    public static final String SYMBOLIC_TYPE_GSCDESIGNREFLECTION       = "type_gscDesignReflection";
    public static final String TYPE_GSCDESIGNREFLECTION            = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCDESIGNREFLECTION);

    /* type_gscCommonMfg */
    public static final String SYMBOLIC_TYPE_GSCCOMMONMFG           = "type_gscCommonMfg";
    public static final String TYPE_GSCCOMMONMFG                    = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCCOMMONMFG);

    /* type_gscExtentionNumber */
    public static final String SYMBOLIC_TYPE_GSCEXTENTIONNUMBER     = "type_gscExtentionNumber";
    public static final String TYPE_GSCEXTENTIONNUMBER              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCEXTENTIONNUMBER);
    
    /* type_gscPORPublicationPlan */
    public static final String SYMBOLIC_TYPE_GSCPORPUBLICATIONPLAN  = "type_gscPORPublicationPlan";
    public static final String TYPE_GSCPORPUBLICATIONPLAN           = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPORPUBLICATIONPLAN);
    
    /* type_gscDrawingDistribution */
    public static final String SYMBOLIC_TYPE_GSCDRAWINGDISTRIBUTION = "type_gscDrawingDistribution";
    public static final String TYPE_GSCDRAWINGDISTRIBUTION          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCDRAWINGDISTRIBUTION);

    /* type_PLMDMTDocument */
    public static final String SYMBOLIC_TYPE_PLMDMTDOCUMENT         = "type_PLMDMTDocument";
    public static final String TYPE_PLMDMTDOCUMENT                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_PLMDMTDOCUMENT);

    /* type_gscERPIFTarget */
    public static final String SYMBOLIC_TYPE_GSCERPIFTARGET         = "type_gscERPIFTarget";
    public static final String TYPE_GSCERPIFTARGET                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCERPIFTARGET);

    /* type_gscPFGPlan */
    public static final String SYMBOLIC_TYPE_GSCPFGPLAN         = "type_gscPFGPlan";
    public static final String TYPE_GSCPFGPLAN                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPFGPLAN);

    /* type_gscSTDLibMfg */
    public static final String SYMBOLIC_TYPE_GSCSTDLIBMFG         = "type_gscSTDLibMfg";
    public static final String TYPE_GSCSTDLIBMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCSTDLIBMFG);

    /* type_gscLibGroupMfg */
    public static final String SYMBOLIC_TYPE_GSCLIBGROUPMFG         = "type_gscLibGroupMfg";
    public static final String TYPE_GSCLIBGROUPMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCLIBGROUPMFG);

    /* type_gscLibValueMfg */
    public static final String SYMBOLIC_TYPE_GSCLIBVALUEMFG         = "type_gscLibValueMfg";
    public static final String TYPE_GSCLIBVALUEMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCLIBVALUEMFG);

    /* type_gscLibPFGMfg */
    public static final String SYMBOLIC_TYPE_GSCLIBPFGMFG         = "type_gscLibPFGMfg";
    public static final String TYPE_GSCLIBPFGMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCLIBPFGMFG);

    /* type_gscProjectEODExcelResult */
    public static final String SYMBOLIC_TYPE_GSCPROJECTEODEXCELRESULT         = "type_gscProjectEODExcelResult";
    public static final String TYPE_GSCPROJECTEODEXCELRESULT                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_GSCPROJECTEODEXCELRESULT);
}
