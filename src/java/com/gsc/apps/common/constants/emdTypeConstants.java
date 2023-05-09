/**
 * <pre>
 * Type Constants 항목을 전역변수로 정의하는 Class.
 * </pre>
 * 
 * @ClassName   : emdTypeConstants.java
 * @Description : Type Constants 항목을 전역변수로 정의하는 Class.
 *                하나의 Type에 대해 아래 순서로 기술한다.
 *                1) Type Fileld 주석 정의. 여러줄 주석을 이용.
 *  *                   작성예제.
 *  *                     - \/* 시험품 *\/
 *  *                  2) Type Symbolic Name 정의
 *  *                   작성 예제.
 *  *                     - public static final String SYMBOLIC_TYPE_EMDTESTPART = "type_emdTestPart";
 *  *                3) Type Original Name 정의.
 *                   작성 예제.
 *                     - public static final String TYPE_EMDTESTPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDTESTPART);
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

public interface emdTypeConstants {
    /* Test Part */
    public static final String SYMBOLIC_TYPE_EMDTESTPART = "type_emdTestPart";
    public static final String TYPE_EMDTESTPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDTESTPART);
    
    /* Engine Group */
    public static final String SYMBOLIC_TYPE_MODEL = "type_Model";
    /* Engine Model Group */
    public static final String SYMBOLIC_TYPE_EMDENGINEMODELGROUP = "type_emdEngineModelGroup";
    public static final String TYPE_EMDENGINEMODELGROUP          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDENGINEMODELGROUP);
    /* Engine Model  */
    public static final String SYMBOLIC_TYPE_EMDENGINEMODEL = "type_emdEngineModel";
    public static final String TYPE_EMDENGINEMODEL          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDENGINEMODEL);
    
    /* Engineering Order*/
    public static final String SYMBOLIC_TYPE_EMDENGINEERINGORDER  = "type_emdEngineeringOrder";
    public static final String TYPE_EMDENGINEERINGORDER           = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDENGINEERINGORDER);
    
    /* PSN Mater */
    public static final String SYMBOLIC_TYPE_EMDPSNMASTER = "type_emdPSNMaster";
    public static final String TYPE_EMDPSNMASTER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPSNMASTER);

    /* PSN */
    public static final String SYMBOLIC_TYPE_EMDPSN = "type_emdPSN";
    public static final String TYPE_EMDPSN          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPSN);

    /* General Library */
    public static final String TYPE_GENERAL_LIBRARY = LibraryCentralConstants.TYPE_GENERAL_LIBRARY;

    /* Change Request */
    public static final String SYMBOLIC_TYPE_EMDCHANGEREQUEST  = "type_emdChangeRequest";
    public static final String TYPE_EMDCHANGEREQUEST           = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCHANGEREQUEST);
    
    /* Part Library */
    public static final String TYPE_PART_LIBRARY               = LibraryCentralConstants.TYPE_PART_LIBRARY;
    
    /* Document Library */
    public static final String TYPE_DOCUMENT_LIBRARY           = LibraryCentralConstants.TYPE_DOCUMENT_LIBRARY;

    /* Engine Group Part */
    public static final String SYMBOLIC_TYPE_EMDENGINEGROUPPART = "type_emdEngineGroupPart";
    public static final String TYPE_EMDENGINEGROUPPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDENGINEGROUPPART);
    
    /* EOD Mater */
    public static final String SYMBOLIC_TYPE_EMDEODMASTER = "type_emdEODMaster";
    public static final String TYPE_EMDEODMASTER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDEODMASTER);

    /* EOD */
    public static final String SYMBOLIC_TYPE_EMDEOD = "type_emdEOD";
    public static final String TYPE_EMDEOD          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDEOD);
    
    /* B-Sheet Abstract Type */
    public static final String SYMBOLIC_TYPE_EMDBSHEETOBJECT       = "type_emdBSHEETOBJECT";
    public static final String TYPE_EMDBSHEETOBJECT                = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDBSHEETOBJECT);
    
    /* EO B-Sheet Type */
    public static final String SYMBOLIC_TYPE_EMDEOBSHEETOBJECT     = "type_emdEOBSheetObject";
    public static final String TYPE_EMDEOBSHEETOBJECT              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDEOBSHEETOBJECT);
    
    /* S & O B-Sheet Type */
    public static final String SYMBOLIC_TYPE_EMDSOBSHEETOBJECT     = "type_emdSOBSheetObject";
    public static final String TYPE_EMDSOBSHEETOBJECT              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDSOBSHEETOBJECT);
    
    /* Order Option List B-SheeT TYPE */
    public static final String SYMBOLIC_TYPE_EMDOCOBSHEETOBJECT    = "type_emdOCOBSheetObject";
    public static final String TYPE_EMDOCOBSHEETOBJECT             = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDOCOBSHEETOBJECT);

    /* MO B-Sheet Type */
    public static final String SYMBOLIC_TYPE_EMDMOBSHEETOBJECT     = "type_emdMOBSheetObject";
    public static final String TYPE_EMDMOBSHEETOBJECT              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDMOBSHEETOBJECT);
    
    /* BPC Mater */
    public static final String SYMBOLIC_TYPE_EMDBPCMASTER          = "type_emdBPCMaster";
    public static final String TYPE_EMDBPCMASTER                   = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDBPCMASTER);

    /* BPC */
    public static final String SYMBOLIC_TYPE_EMDBPC                = "type_emdBPC";
    public static final String TYPE_EMDBPC                         = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDBPC);

    /* S&O Engineering Order TYPE */
    public static final String SYMBOLIC_TYPE_EMDSOCHANGEORDER      = "type_emdSOChangeOrder";
    public static final String TYPE_EMDSOCHANGEORDER               = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDSOCHANGEORDER);
    
    /* Manufacturing Order TYPE */
    public static final String SYMBOLIC_TYPE_EMDMANUFACTURINGORDER = "type_emdManufacturingOrder";
    public static final String TYPE_EMDMANUFACTURINGORDER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDMANUFACTURINGORDER);
    
    /* Order Option List Change Order Type*/
    public static final String SYMBOLIC_TYPE_EMDORDEROPTIONLISTORDER = "type_emdOrderOptionListOrder";
    public static final String TYPE_EMDORDEROPTIONLISTORDER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDORDEROPTIONLISTORDER);
    
    /* CR Review Results Type*/
    public static final String SYMBOLIC_TYPE_EMDCRREVIEWRESULTS     = "type_emdCRReviewResults";
    public static final String TYPE_EMDCRREVIEWRESULTS              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCRREVIEWRESULTS);
    
    /* PFG Part */
    public static final String SYMBOLIC_TYPE_EMDPFGPART = "type_emdPFGPart";
    public static final String TYPE_EMDPFGPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPFGPART);
    
    /* PFG Variant Part */
    public static final String SYMBOLIC_TYPE_EMDPFGVARIANTPART = "type_emdPFGVariantPart";
    public static final String TYPE_EMDPFGVARIANTPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPFGVARIANTPART);
    
    /* Code Master Type*/
    public static final String SYMBOLIC_TYPE_EMDCODEMASTER          = "type_emdCodeMaster";
    public static final String TYPE_EMDCODEMASTER                   = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCODEMASTER);
    
    /* Reserved Part No*/
    public static final String SYMBOLIC_TYPE_EMDRESERVEDPARTNO   	= "type_emdReservedPartNo";
    public static final String TYPE_EMDRESERVEDPARTNO            	= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDRESERVEDPARTNO);
    
    /* Latest PFG Part*/
    public static final String SYMBOLIC_EMDLATESTPFGPART       = "emdLatestPFGPart";
    public static final String EMDLATESTPFGPART                = PropertyUtil.getSchemaProperty(SYMBOLIC_EMDLATESTPFGPART);
    
    /* VPMReference (CATIA 3D)*/
    public static final String SYMBOLIC_TYPE_VPMREFERENCE = "type_VPMReference";
    public static final String TYPE_VPMREFERENCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_VPMREFERENCE);
    
    /* Drawing (CATIA 2D)*/
    public static final String SYMBOLIC_TYPE_DRAWING  = "type_Drawing";
    public static final String TYPE_DRAWING                 = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_DRAWING);
    
    /* Approval Line */
    public static final String SYMB_TYPE_EMDAPPROVALLINE       = "type_emdApprovalLine";
    public static final String TYPE_EMDAPPROVALLINE            = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDAPPROVALLINE);

    /* Approval Request */
    public static final String SYMB_TYPE_EMDAPPROVALREQUEST       = "type_emdApprovalRequest";
    public static final String TYPE_EMDAPPROVALREQUEST           = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDAPPROVALREQUEST);

    /* Series */
    public static final String SYMB_TYPE_EMDSERIES           = "type_emdSeries";
    public static final String TYPE_EMDSERIES                = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDSERIES);

    /* Ship */
    public static final String SYMB_TYPE_EMDSHIP             = "type_emdShip";
    public static final String TYPE_EMDSHIP                  = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDSHIP);

    /* Project */
    public static final String SYMB_TYPE_EMDPROJECT          = "type_emdProject";
    public static final String TYPE_EMDPROJECT               = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDPROJECT);

    /* Package Project */
    public static final String SYMB_TYPE_EMDPACKAGEPROJECT   = "type_emdPackageProject";
    public static final String TYPE_EMDPACKAGEPROJECT        = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDPACKAGEPROJECT);

    /* Project Remark */
    public static final String SYMB_TYPE_EMDPROJECTREMARK    = "type_emdProjectRemark";
    public static final String TYPE_EMDPROJECTREMARK         = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDPROJECTREMARK);

    /* Project EOD */
    public static final String SYMB_TYPE_EMDPROJECTEOD    = "type_emdProjectEOD";
    public static final String TYPE_EMDPROJECTEOD         = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDPROJECTEOD);
    
    /* Latest PFG */
    public static final String SYMB_TYPE_EMDLATESTPFGPART    = "type_emdLatestPFGPart";
    public static final String TYPE_EMDLATESTPFGPART         = PropertyUtil.getSchemaProperty(SYMB_TYPE_EMDLATESTPFGPART);
    
    /* Review Check Abstract Type */
    public static final String SYMBOLIC_TYPE_EMDCOREVIEWCHECKLIST          = "type_emdCOREVIEWCHECKLIST";
    public static final String TYPE_EMDCOREVIEWCHECKLIST                   = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCOREVIEWCHECKLIST);

    /* S&O Review Check List Type */
    public static final String SYMBOLIC_TYPE_EMDCHANGEORDERCHECKLIST       = "type_emdChangeOrderCheckList";
    public static final String TYPE_EMDCHANGEORDERCHECKLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCHANGEORDERCHECKLIST);

    /* S&O Review Check List Master Type */
    public static final String SYMBOLIC_TYPE_EMDCHANGEORDERCHECKLISTMASTER = "type_emdChangeOrderCheckListMaster";
    public static final String TYPE_EMDCHANGEORDERCHECKLISTMASTER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCHANGEORDERCHECKLISTMASTER);
    
    /* AD Engine Group Type*/
    public static final String SYMBOLIC_TYPE_EMDADENGINEGROUP	= "type_emdADEngineGroup";
    public static final String TYPE_EMDADENGINEGROUP 			= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDADENGINEGROUP);
    
    /* AD Model Type*/
    public static final String SYMBOLIC_TYPE_EMDADMODEL		= "type_emdADModel";
    public static final String TYPE_EMDADMODEL 				= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDADMODEL);
    
    /* ADG Type*/
    public static final String SYMBOLIC_TYPE_EMDADG		= "type_emdADG";
    public static final String TYPE_EMDADG 				= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDADG);
    
    /* ADG Variant Part Type*/
    public static final String SYMBOLIC_TYPE_EMDADVARIANTPART		= "type_emdADGVariantPart";
    public static final String TYPE_EMDADVARIANTPART 				= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDADVARIANTPART);
    
    /* AD Part Type*/
    public static final String SYMBOLIC_TYPE_EMDADPART		= "type_emdADPart";
    public static final String TYPE_EMDADPART 				= PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDADPART);
    /* Service Document */
    public static final String SYMBOLIC_TYPE_EMDSERVICEDOCUMENT = "type_emdServiceDocument";
    public static final String TYPE_EMDSERVICEDOCUMENT          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDSERVICEDOCUMENT);

    /* PPRContext */
    public static final String SYMBOLIC_TYPE_PPRCONTEXT             = "type_PPRContext";
    public static final String TYPE_PPRCONTEXT                      = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_PPRCONTEXT);

    /* Engine Group MFG */
    public static final String SYMBOLIC_TYPE_EMDENGINEGROUPMFG      = "type_emdEngineGroupMfg";
    public static final String TYPE_EMDENGINEGROUPMFG               = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDENGINEGROUPMFG);

    /* PFG Part MFG*/
    public static final String SYMBOLIC_TYPE_EMDPFGMFG              = "type_emdPFGMfg";
    public static final String TYPE_EMDPFGMFG                       = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPFGMFG);

    /* PFG Variant Part MFG*/
    public static final String SYMBOLIC_TYPE_EMDPFGVARIANTMFG       = "type_emdPFGVariantMfg";
    public static final String TYPE_EMDPFGVARIANTMFG                = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPFGVARIANTMFG);

    /* Part MFG*/
    public static final String SYMBOLIC_TYPE_EMDASSEMBLYMFG         = "type_emdAssemblyMfg";
    public static final String TYPE_EMDASSEMBLYMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDASSEMBLYMFG);

    /* Engine Group */
    public static final String SYMBOLIC_TYPE_EMDENGINEGROUP         = "type_emdEngineGroup";
    public static final String TYPE_EMDENGINEGROUP                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDENGINEGROUP);

    /* PFG */
    public static final String SYMBOLIC_TYPE_EMDPFG                 = "type_emdPFG";
    public static final String TYPE_EMDPFG                          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPFG);

    /* PFG Variant */
    public static final String SYMBOLIC_TYPE_EMDPFGVARIANT          = "type_emdPFGVariant";
    public static final String TYPE_EMDPFGVARIANT                   = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPFGVARIANT);
    
    /* 3D Shape */
    public static final String SYMBOLIC_TYPE_3DSHAPE = "type_3DShape";
    public static final String TYPE_3DSHAPE          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_3DSHAPE);
    
    /* type_VPMRepReference */
    public static final String SYMBOLIC_TYPE_VPMREPREFERENCE = "type_VPMRepReference";
    public static final String TYPE_VPMREPREFERENCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_VPMREPREFERENCE);

    /* type_emdMFGModel */
    public static final String SYMBOLIC_TYPE_EMDMFGMODEL = "type_emdMFGModel";
    public static final String TYPE_EMDMFGMODEL          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDMFGMODEL);
    
    /* type_emdChangeValidationMaster */
    public static final String SYMBOLIC_TYPE_EMDCHANGEVALIDATIONMASTER = "type_emdChangeValidationMaster";
    public static final String TYPE_EMDCHANGEVALIDATIONMASTER          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCHANGEVALIDATIONMASTER);
    
    /* type_emdChangeValidation */
    public static final String SYMBOLIC_TYPE_EMDCHANGEVALIDATION       = "type_emdChangeValidation";
    public static final String TYPE_EMDCHANGEVALIDATION          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCHANGEVALIDATION);

    /* type_emdSBOMEngineModel */
    public static final String SYMBOLIC_TYPE_EMDSBOMENGINEMODEL       = "type_emdSBOMEngineModel";
    public static final String TYPE_EMDSBOMENGINEMODEL           = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDSBOMENGINEMODEL);

    /* type_emdSBOMGroup */
    public static final String SYMBOLIC_TYPE_EMDSBOMGROUP       = "type_emdSBOMGroup";
    public static final String TYPE_EMDSBOMGROUP            = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDSBOMGROUP);

    /* type_emdUnitExtension */
    public static final String SYMBOLIC_TYPE_EMDUNITEXTENSIONREQUEST       = "type_emdUnitExtensionRequest";
    public static final String TYPE_EMDUNITEXTENSIONREQUEST            = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDUNITEXTENSIONREQUEST);

    /* type_emdDesignReflection */
    public static final String SYMBOLIC_TYPE_EMDDESIGNREFLECTION       = "type_emdDesignReflection";
    public static final String TYPE_EMDDESIGNREFLECTION            = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDDESIGNREFLECTION);

    /* type_emdCommonMfg */
    public static final String SYMBOLIC_TYPE_EMDCOMMONMFG           = "type_emdCommonMfg";
    public static final String TYPE_EMDCOMMONMFG                    = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDCOMMONMFG);

    /* type_emdExtentionNumber */
    public static final String SYMBOLIC_TYPE_EMDEXTENTIONNUMBER     = "type_emdExtentionNumber";
    public static final String TYPE_EMDEXTENTIONNUMBER              = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDEXTENTIONNUMBER);
    
    /* type_emdPORPublicationPlan */
    public static final String SYMBOLIC_TYPE_EMDPORPUBLICATIONPLAN  = "type_emdPORPublicationPlan";
    public static final String TYPE_EMDPORPUBLICATIONPLAN           = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPORPUBLICATIONPLAN);
    
    /* type_emdDrawingDistribution */
    public static final String SYMBOLIC_TYPE_EMDDRAWINGDISTRIBUTION = "type_emdDrawingDistribution";
    public static final String TYPE_EMDDRAWINGDISTRIBUTION          = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDDRAWINGDISTRIBUTION);

    /* type_PLMDMTDocument */
    public static final String SYMBOLIC_TYPE_PLMDMTDOCUMENT         = "type_PLMDMTDocument";
    public static final String TYPE_PLMDMTDOCUMENT                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_PLMDMTDOCUMENT);

    /* type_emdERPIFTarget */
    public static final String SYMBOLIC_TYPE_EMDERPIFTARGET         = "type_emdERPIFTarget";
    public static final String TYPE_EMDERPIFTARGET                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDERPIFTARGET);

    /* type_emdPFGPlan */
    public static final String SYMBOLIC_TYPE_EMDPFGPLAN         = "type_emdPFGPlan";
    public static final String TYPE_EMDPFGPLAN                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPFGPLAN);

    /* type_emdSTDLibMfg */
    public static final String SYMBOLIC_TYPE_EMDSTDLIBMFG         = "type_emdSTDLibMfg";
    public static final String TYPE_EMDSTDLIBMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDSTDLIBMFG);

    /* type_emdLibGroupMfg */
    public static final String SYMBOLIC_TYPE_EMDLIBGROUPMFG         = "type_emdLibGroupMfg";
    public static final String TYPE_EMDLIBGROUPMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDLIBGROUPMFG);

    /* type_emdLibValueMfg */
    public static final String SYMBOLIC_TYPE_EMDLIBVALUEMFG         = "type_emdLibValueMfg";
    public static final String TYPE_EMDLIBVALUEMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDLIBVALUEMFG);

    /* type_emdLibPFGMfg */
    public static final String SYMBOLIC_TYPE_EMDLIBPFGMFG         = "type_emdLibPFGMfg";
    public static final String TYPE_EMDLIBPFGMFG                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDLIBPFGMFG);

    /* type_emdProjectEODExcelResult */
    public static final String SYMBOLIC_TYPE_EMDPROJECTEODEXCELRESULT         = "type_emdProjectEODExcelResult";
    public static final String TYPE_EMDPROJECTEODEXCELRESULT                  = PropertyUtil.getSchemaProperty(SYMBOLIC_TYPE_EMDPROJECTEODEXCELRESULT);
}
