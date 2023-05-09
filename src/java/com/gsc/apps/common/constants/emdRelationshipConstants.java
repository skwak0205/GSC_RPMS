/**
 * <pre>
 * Relationship Constants 항목을 전역변수로 정의하는 Class.
 * </pre>
 * 
 * @ClassName   : emdRelationshipConstants.java
 * @Description : Relationship Constants 항목을 전역변수로 정의하는 Class.
 *                하나의 Relationship에 대해 아래 순서로 기술한다.
 *                1) Relationship Fileld 주석 정의. 여러줄 주석을 이용.
 *                   작성예제.
 *                     - \/* 부품과 첨부파일 연결 *\/
 *	              2) Relationship Symbolic Name 정의
 *                   작성 예제.
 *                     - public static final String SYMBOLIC_RELATIONSHIP_EMDPARTANDATTACHFILE = "type_emdPartAndAttachFile";
 *                3) Relationship Original Name 정의.
 *                   작성 예제.
 *                     - public static final String RELATIONSHIP_EMDPARTANDATTACHFILE          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPARTANDATTACHFILE);
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
import com.matrixone.vplmintegrationitf.util.VPLMIntegrationConstants;

public interface emdRelationshipConstants {
    /* 3D와 3D 연결 Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_VPMINSTANCE = "relationship_VPMInstance";
    public static final String RELATIONSHIP_VPMINSTANCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_VPMINSTANCE);
    
    /* 3D와 2D 연결 Relationship*/
    public static final String SYMBOLIC_RELATIONSHIP_VPMREPINSTANCE = "relationship_VPMRepInstance";
    public static final String RELATIONSHIP_VPMREPINSTANCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_VPMREPINSTANCE);
    
    /* 부품과 첨부파일 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPARTANDATTACHFILE = "type_emdPartAndAttachFile";
    public static final String RELATIONSHIP_EMDPARTANDATTACHFILE = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPARTANDATTACHFILE);

    /* General Library To General Class */
    public static final String RELATIONSHIP_SUBCLASS = LibraryCentralConstants.RELATIONSHIP_SUBCLASS;

    /* S&O List와 엔진 모델 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_EMDSNOLISTTOENGINEMODEL = "relationship_emdSnOListToEngineModel";
    public static final String RELATIONSHIP_EMDSNOLISTTOENGINEMODEL = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDSNOLISTTOENGINEMODEL);

    /* EOD 와 사양 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_EMDEODTOOPTION = "relationship_emdEODToOption";
    public static final String RELATIONSHIP_EMDEODTOOPTION = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDEODTOOPTION);

    /* EOD 와 Engine Group 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_EMDAFFECTEDENGINEGROUP = "relationship_emdAffectedEngineGroup";
    public static final String RELATIONSHIP_EMDAFFECTEDENGINEGROUP          = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDAFFECTEDENGINEGROUP);
    
    /* EOD 와 사양 Gropu 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_EMDEODTOOPTIONGROUP = "relationship_emdEODToOptionGroup";
    public static final String RELATIONSHIP_EMDEODTOOPTIONGROUP = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDEODTOOPTIONGROUP);
    
    /* Change(설계변경)과 B-Sheet 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_EMDCHANGEORDERTOBSHEET = "relationship_emdChangeOrderToBSheet";
    public static final String RELATIONSHIP_EMDCHANGEORDERTOBSHEET          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDCHANGEORDERTOBSHEET);

    /* Configuration Context(OOTB Relationship) */
    public static final String SYMBOLIC_RELATIONSHIP_CONFIGURATIONCONTEXT = "relationship_ConfigurationContext";
    public static final String RELATIONSHIP_CONFIGURATIONCONTEXT          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_CONFIGURATIONCONTEXT);

    /* Change 와 CR Review Results 연결  */
    public static final String SYMBOLIC_RELATIONSHIP_EMDCRTOREVIEWRESULTS = "relationship_emdCRToReviewResults";
    public static final String RELATIONSHIP_EMDCRTOREVIEWRESULTS          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDCRTOREVIEWRESULTS);

    /* BPC 와 부품관리번호 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_EMDBPCTOPARTMANAGEMENTNO = "relationship_emdBPCToPartManagementNo";
    public static final String RELATIONSHIP_EMDBPCTOPARTMANAGEMENTNO          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDBPCTOPARTMANAGEMENTNO);


    /* Engine Group To Engine Model Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_EMDENGINEGROUPTOMODEL = "relationship_emdEngineGroupToModel";
    public static final String RELATIONSHIP_EMDENGINEGROUPTOMODEL          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDENGINEGROUPTOMODEL);

    /* Code Master To Code Master Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_EMDCODEPARENTANDCHILD = "relationship_emdCodeParentAndChild";
    public static final String RELATIONSHIP_EMDCODEPARENTANDCHILD          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDCODEPARENTANDCHILD);

    /* Latest PFG And PFG-V Part Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_EMDLATESTPFGANDPFGVARIANT = "relationship_emdLatestPFGAndPFGVariant";
    public static final String RELATIONSHIP_EMDLATESTPFGANDPFGVARIANT          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDLATESTPFGANDPFGVARIANT);

    /* Change And Leaf Part Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_EMDCHANGEORDERANDLEAFPART = "relationship_emdChangeOrderAndLeafPart";
    public static final String RELATIONSHIP_EMDCHANGEORDERANDLEAFPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDCHANGEORDERANDLEAFPART);
    
    /* Approval Line To Route Template */
    public static final String SYMBOLIC_RELATIONSHIP_APPROVALLINE_TO_ROUTETEMPLETE      = "relationship_emdApprovalLineToRouteTemplate";
    public static final String RELATIONSHIP_APPROVALLINE_TO_ROUTETEMPLETE               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_APPROVALLINE_TO_ROUTETEMPLETE);

    /* Package Project To Series */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPACKAGEPROJECTTOSERIES      = "relationship_emdPackageProjectToSeries";
    public static final String RELATIONSHIP_EMDPACKAGEPROJECTTOSERIES               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPACKAGEPROJECTTOSERIES);

    /* Project EOD To Project EOD */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTEODTOPROJECTEOD      = "relationship_emdProjectEODToProjectEOD";
    public static final String RELATIONSHIP_EMDPROJECTEODTOPROJECTEOD               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTEODTOPROJECTEOD);

    /* Project To Project EOD */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTTOPROJECTEOD      = "relationship_emdProjectToProjectEOD";
    public static final String RELATIONSHIP_EMDPROJECTTOPROJECTEOD               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTTOPROJECTEOD);

    /* Project To Project Remark */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTTOPROJECTREMARK      = "relationship_emdProjectToProjectRemark";
    public static final String RELATIONSHIP_EMDPROJECTTOPROJECTREMARK               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTTOPROJECTREMARK);

    /* Series To Project */
    public static final String SYMBOLIC_RELATIONSHIP_EMDSERIESTOPROJECT      = "relationship_emdSeriesToProject";
    public static final String RELATIONSHIP_EMDSERIESTOPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDSERIESTOPROJECT);

    /* Ship To Project */
    public static final String SYMBOLIC_RELATIONSHIP_EMDSHIPTOPROJECT      = "relationship_emdShipToProject";
    public static final String RELATIONSHIP_EMDSHIPTOPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDSHIPTOPROJECT);
    
    /* Approval Request To Part, Document */
    public static final String SYMBOLIC_RELATIONSHIP_EMDAFFECTEDITEM      = "relationship_emdAffectedItem";
    public static final String RELATIONSHIP_EMDAFFECTEDITEM               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDAFFECTEDITEM);

    /* Ship To Project */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPACKAGEPROJECTTOPROJECT      = "relationship_emdPackageProjectToProject";
    public static final String RELATIONSHIP_EMDPACKAGEPROJECTTOPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPACKAGEPROJECTTOPROJECT);

    /* BPC To BPC */
    public static final String SYMBOLIC_RELATIONSHIP_EMDBPCTOBPC      = "relationship_emdBPCToBPC";
    public static final String RELATIONSHIP_EMDBPCTOBPC               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDBPCTOBPC);

    /* Change And Review Check List Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_EMDEOREVIEWCHECKLIST = "relationship_emdEOReviewCheckList";
    public static final String RELATIONSHIP_EMDEOREVIEWCHECKLIST          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDEOREVIEWCHECKLIST);

    /* PFG Part To VPMInstance */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPFGPARTTOVPMINSTANCE = "relationship_emdPFGPartToVPMInstance";
    public static final String RELATIONSHIP_EMDPFGPARTTOVPMINSTANCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPFGPARTTOVPMINSTANCE);

    /* BPC To EBOM */
    public static final String SYMBOLIC_RELATIONSHIP_EMDBPCTOEBOM = "relationship_emdBPCToEBOM";
    public static final String RELATIONSHIP_EMDBPCTOEBOM = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDBPCTOEBOM);

    /* Project Remark To EOD */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOEOD      = "relationship_emdProjectRemarkToEOD";
    public static final String RELATIONSHIP_EMDPROJECTREMARKTOEOD               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOEOD);

    /* Project Remark To Option Group */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOOPTIONGROUP      = "relationship_emdProjectRemarkToOptionGroup";
    public static final String RELATIONSHIP_EMDPROJECTREMARKTOOPTIONGROUP               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOOPTIONGROUP);

    /* MO To IF Master */
    public static final String SYMBOLIC_RELATIONSHIP_EMDMOTOIFMASTER      = "relationship_emdMOToIFMaster";
    public static final String RELATIONSHIP_EMDMOTOIFMASTER               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDMOTOIFMASTER);

    /* EBOM To CADBOM Sync Relationship */
    public static final String RELATIONSHIP_VPLMINTEG_VPLMPROJECTION      = VPLMIntegrationConstants.REL_ITG_PROJECTION;

    /* Project To OOL */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTTOOOL      = "relationship_emdProjectToOOL";
    public static final String RELATIONSHIP_EMDPROJECTTOOOL               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTTOOOL);
    
    /* Change To Project */
    public static final String SYMBOLIC_RELATIONSHIP_EMDRELATEDPROJECT      = "relationship_emdRelatedProject";
    public static final String RELATIONSHIP_EMDRELATEDPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDRELATEDPROJECT);

    /* Engine Model To OOL */
    public static final String SYMBOLIC_RELATIONSHIP_EMDENGINEMODELTOOOL      = "relationship_emdEngineModelToOOL";
    public static final String RELATIONSHIP_EMDENGINEMODELTOOOL               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDENGINEMODELTOOOL);

    /* emdADG To VPMReference */
    public static final String SYMBOLIC_RELATIONSHIP_EMDADGTOVPMREFERENCE      = "relationship_emdADGToVPMReference";
    public static final String RELATIONSHIP_EMDADGTOVPMREFERENCE               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDADGTOVPMREFERENCE);
    
    /* Change Order To Change Order */
    public static final String SYMBOLIC_RELATIONSHIP_EMDRELATEDCHANGEORDER      = "relationship_emdRelatedChangeOrder";
    public static final String RELATIONSHIP_EMDRELATEDCHANGEORDER               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDRELATEDCHANGEORDER);
    
    /* Manufacturing To Series */
    public static final String SYMBOLIC_RELATIONSHIP_EMDMOTOSERIES             = "relationship_emdMOToSeries";
    public static final String RELATIONSHIP_EMDMOTOSERIES                      = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDMOTOSERIES);

    /* Manufacturing Order To Manufacturing Order */
    public static final String SYMBOLIC_RELATIONSHIP_EMDMOTOSERIESMOLIST       = "relationship_emdMOToSeriesMOList";
    public static final String RELATIONSHIP_EMDMOTOSERIESMOLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDMOTOSERIESMOLIST);
    
    /* Manufacturing Order To Manufacturing Order */
    public static final String SYMBOLIC_RELATIONSHIP_EMDCHANGERELATEDORDEROPTIONLIST       = "relationship_emdChangeRelatedOrderOptionList";
    public static final String RELATIONSHIP_EMDCHANGERELATEDORDEROPTIONLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDCHANGERELATEDORDEROPTIONLIST);
    
    /* emdChangeValidationMaster To emdChangeValidation */
    public static final String SYMBOLIC_RELATIONSHIP_EMDCHANGEVALIDATIONMASTER       = "relationship_emdChangeValidationMaster";
    public static final String RELATIONSHIP_EMDCHANGEVALIDATIONMASTER                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDCHANGEVALIDATIONMASTER);

    /* ReferenceDocument */
    public static final String SYMBOLIC_RELATIONSHIP_REFERENCEDOCUMENT       = "relationship_ReferenceDocument";

    /* PFG To Option Group */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPFGTOVARIANT       = "relationship_emdPFGToVariant";
    public static final String RELATIONSHIP_EMDPFGTOVARIANT                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPFGTOVARIANT);

    /* EGM To Option Group */
    public static final String SYMBOLIC_RELATIONSHIP_EMDEGMTOVARIANT       = "relationship_emdEGMToVariant";
    public static final String RELATIONSHIP_EMDEGMTOVARIANT                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDEGMTOVARIANT);

    /* emdADPart To VPMReference */
    public static final String SYMBOLIC_RELATIONSHIP_EMDADPARTTOVPMREFERENCE       = "relationship_emdADPartToVPMReference";
    public static final String RELATIONSHIP_EMDADPARTTOVPMREFERENCE                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDADPARTTOVPMREFERENCE);

    /* PLMReference To PLMReference */
    public static final String SYMBOLIC_RELATIONSHIP_DELFMIFUNCTIONIDENTIFIEDINSTANCE       = "relationship_DELFmiFunctionIdentifiedInstance";
    public static final String RELATIONSHIP_DELFMIFUNCTIONIDENTIFIEDINSTANCE                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_DELFMIFUNCTIONIDENTIFIEDINSTANCE);
    
    /* emdUnitExtensionRequest to Part*/
    public static final String SYMBOLIC_RELATIONSHIP_EMDUNITEXTENSIONLIST       = "relationship_emdUnitExtensionList";
    public static final String RELATIONSHIP_EMDUNITEXTENSIONLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDUNITEXTENSIONLIST);

    /* AD Model To AD EngineGroup*/
    public static final String SYMBOLIC_RELATIONSHIP_EMDADMODELTOADENGINEGROUP      = "relationship_emdADModelToADEngineGroup";
    public static final String RELATIONSHIP_EMDADMODELTOADENGINEGROUP               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDADMODELTOADENGINEGROUP);

    /* emdADPart To AD PLMDocConnection*/
    public static final String SYMBOLIC_RELATIONSHIP_EMDADPARTTOPLMDOCCONNECTION      = "relationship_emdADPartToPLMDocConnection";
    public static final String RELATIONSHIP_EMDADPARTTOPLMDOCCONNECTION               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDADPARTTOPLMDOCCONNECTION);
    
    /* Manufacturing Order To Common Manufacturing Order emdMOToCommonMOList */
    public static final String SYMBOLIC_RELATIONSHIP_EMDMOTOCOMMONMOLIST       = "relationship_emdMOToCommonMOList";
    public static final String RELATIONSHIP_EMDMOTOCOMMONMOLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDMOTOCOMMONMOLIST);

    /* Logical Feature To emdDesignReflection*/
    public static final String SYMBOLIC_RELATIONSHIP_EMDPFGTODESIGNREFLECTION      = "relationship_emdPFGToDesignReflection";
    public static final String RELATIONSHIP_EMDPFGTODESIGNREFLECTION               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPFGTODESIGNREFLECTION);
    
    /* MO To EBOM Part/Drawing*/
    public static final String SYMBOLIC_RELATIONSHIP_EMDEBOMAFFECTEDITEM      = "relationship_emdEBOMAffectedItem";
    public static final String RELATIONSHIP_EMDEBOMAFFECTEDITEM               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDEBOMAFFECTEDITEM);
    
    /* Leaf PFG To Order Option List*/
    public static final String SYMBOLIC_RELATIONSHIP_EMDPFGTOAPPLYORDEROPTIONLIST      = "relationship_emdPFGToApplyOrderOptionList";
    public static final String RELATIONSHIP_EMDPFGTOAPPLYORDEROPTIONLIST               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPFGTOAPPLYORDEROPTIONLIST);

    /* emdPGU */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPGU      = "relationship_emdPGU";
    public static final String RELATIONSHIP_EMDPGU               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPGU);

    /* emdProject to emdEngineGroupMfg */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTTOENGINEGROUPMFG      = "relationship_emdProjectToEngineGroupMfg";
    public static final String RELATIONSHIP_EMDPROJECTTOENGINEGROUPMFG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTTOENGINEGROUPMFG);

    /* Option Group to emdADG */
    public static final String SYMBOLIC_RELATIONSHIP_EMDOPTIONGROUPTOADG      = "relationship_emdOptionGroupToADG";
    public static final String RELATIONSHIP_EMDOPTIONGROUPTOADG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDOPTIONGROUPTOADG);

    /* AD Model to emdProject */
    public static final String SYMBOLIC_RELATIONSHIP_EMDADMODELTOPROJECT      = "relationship_emdADModelToProject";
    public static final String RELATIONSHIP_EMDADMODELTOPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDADMODELTOPROJECT);

    /* Project Remark to PFG */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOPFG      = "relationship_emdProjectRemarkToPFG";
    public static final String RELATIONSHIP_EMDPROJECTREMARKTOPFG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOPFG);

    /* Project Remark to ADG */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOADG      = "relationship_emdProjectRemarkToADG";
    public static final String RELATIONSHIP_EMDPROJECTREMARKTOADG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOADG);

    /* Project Remark to PFG Mfg */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOPFGMFG      = "relationship_emdProjectRemarkToPFGMfg";
    public static final String RELATIONSHIP_EMDPROJECTREMARKTOPFGMFG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOPFGMFG);

    /* PFG Mfg To Drawing Distribute */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPFGMFGTODRAWINGDISTRIBUTE      = "relationship_emdPFGMfgToDrawingDistribute";
    public static final String RELATIONSHIP_EMDPFGMFGTODRAWINGDISTRIBUTE               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPFGMFGTODRAWINGDISTRIBUTE);

    /* Project Remark To Business Unit/Person Distribute */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOCHECK      = "relationship_emdProjectRemarkToCheck";
    public static final String RELATIONSHIP_EMDPROJECTREMARKTOCHECK               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTREMARKTOCHECK);

    /* emdProject To emdPFGPlan */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTPLAN      = "relationship_emdProjectPlan";
    public static final String RELATIONSHIP_EMDPROJECTPLAN               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTPLAN);

    /* emdProject To emdProjectEODExcelResult */
    public static final String SYMBOLIC_RELATIONSHIP_EMDPROJECTTOEODEXCELRESULT      = "relationship_emdProjectToEODExcelResult";
    public static final String RELATIONSHIP_EMDPROJECTTOEODEXCELRESULT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDPROJECTTOEODEXCELRESULT);

    /* emdADG To emdBPC */
    public static final String SYMBOLIC_RELATIONSHIP_EMDADGTOBPC      = "relationship_emdADGtoBPC";
    public static final String RELATIONSHIP_EMDADGTOBPC               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_EMDADGTOBPC);
}
