/**
 * <pre>
 * Relationship Constants 항목을 전역변수로 정의하는 Class.
 * </pre>
 * 
 * @ClassName   : gscRelationshipConstants.java
 * @Description : Relationship Constants 항목을 전역변수로 정의하는 Class.
 *                하나의 Relationship에 대해 아래 순서로 기술한다.
 *                1) Relationship Fileld 주석 정의. 여러줄 주석을 이용.
 *                   작성예제.
 *                     - \/* 부품과 첨부파일 연결 *\/
 *	              2) Relationship Symbolic Name 정의
 *                   작성 예제.
 *                     - public static final String SYMBOLIC_RELATIONSHIP_GSCPARTANDATTACHFILE = "type_gscPartAndAttachFile";
 *                3) Relationship Original Name 정의.
 *                   작성 예제.
 *                     - public static final String RELATIONSHIP_GSCPARTANDATTACHFILE          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPARTANDATTACHFILE);
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

public interface gscRelationshipConstants {
    /* 3D와 3D 연결 Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_VPMINSTANCE = "relationship_VPMInstance";
    public static final String RELATIONSHIP_VPMINSTANCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_VPMINSTANCE);
    
    /* 3D와 2D 연결 Relationship*/
    public static final String SYMBOLIC_RELATIONSHIP_VPMREPINSTANCE = "relationship_VPMRepInstance";
    public static final String RELATIONSHIP_VPMREPINSTANCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_VPMREPINSTANCE);
    
    /* 부품과 첨부파일 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPARTANDATTACHFILE = "type_gscPartAndAttachFile";
    public static final String RELATIONSHIP_GSCPARTANDATTACHFILE = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPARTANDATTACHFILE);

    /* General Library To General Class */
    public static final String RELATIONSHIP_SUBCLASS = LibraryCentralConstants.RELATIONSHIP_SUBCLASS;

    /* S&O List와 엔진 모델 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_GSCSNOLISTTOENGINEMODEL = "relationship_gscSnOListToEngineModel";
    public static final String RELATIONSHIP_GSCSNOLISTTOENGINEMODEL = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCSNOLISTTOENGINEMODEL);

    /* EOD 와 사양 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_GSCEODTOOPTION = "relationship_gscEODToOption";
    public static final String RELATIONSHIP_GSCEODTOOPTION = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCEODTOOPTION);

    /* EOD 와 Engine Group 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_GSCAFFECTEDENGINEGROUP = "relationship_gscAffectedEngineGroup";
    public static final String RELATIONSHIP_GSCAFFECTEDENGINEGROUP          = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCAFFECTEDENGINEGROUP);
    
    /* EOD 와 사양 Gropu 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_GSCEODTOOPTIONGROUP = "relationship_gscEODToOptionGroup";
    public static final String RELATIONSHIP_GSCEODTOOPTIONGROUP = PropertyUtil
            .getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCEODTOOPTIONGROUP);
    
    /* Change(설계변경)과 B-Sheet 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_GSCCHANGEORDERTOBSHEET = "relationship_gscChangeOrderToBSheet";
    public static final String RELATIONSHIP_GSCCHANGEORDERTOBSHEET          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCCHANGEORDERTOBSHEET);

    /* Configuration Context(OOTB Relationship) */
    public static final String SYMBOLIC_RELATIONSHIP_CONFIGURATIONCONTEXT = "relationship_ConfigurationContext";
    public static final String RELATIONSHIP_CONFIGURATIONCONTEXT          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_CONFIGURATIONCONTEXT);

    /* Change 와 CR Review Results 연결  */
    public static final String SYMBOLIC_RELATIONSHIP_GSCCRTOREVIEWRESULTS = "relationship_gscCRToReviewResults";
    public static final String RELATIONSHIP_GSCCRTOREVIEWRESULTS          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCCRTOREVIEWRESULTS);

    /* BPC 와 부품관리번호 연결 */
    public static final String SYMBOLIC_RELATIONSHIP_GSCBPCTOPARTMANAGEMENTNO = "relationship_gscBPCToPartManagementNo";
    public static final String RELATIONSHIP_GSCBPCTOPARTMANAGEMENTNO          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCBPCTOPARTMANAGEMENTNO);


    /* Engine Group To Engine Model Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_GSCENGINEGROUPTOMODEL = "relationship_gscEngineGroupToModel";
    public static final String RELATIONSHIP_GSCENGINEGROUPTOMODEL          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCENGINEGROUPTOMODEL);

    /* Code Master To Code Master Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_GSCCODEPARENTANDCHILD = "relationship_gscCodeParentAndChild";
    public static final String RELATIONSHIP_GSCCODEPARENTANDCHILD          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCCODEPARENTANDCHILD);

    /* Latest PFG And PFG-V Part Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_GSCLATESTPFGANDPFGVARIANT = "relationship_gscLatestPFGAndPFGVariant";
    public static final String RELATIONSHIP_GSCLATESTPFGANDPFGVARIANT          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCLATESTPFGANDPFGVARIANT);

    /* Change And Leaf Part Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_GSCCHANGEORDERANDLEAFPART = "relationship_gscChangeOrderAndLeafPart";
    public static final String RELATIONSHIP_GSCCHANGEORDERANDLEAFPART          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCCHANGEORDERANDLEAFPART);
    
    /* Approval Line To Route Template */
    public static final String SYMBOLIC_RELATIONSHIP_APPROVALLINE_TO_ROUTETEMPLETE      = "relationship_gscApprovalLineToRouteTemplate";
    public static final String RELATIONSHIP_APPROVALLINE_TO_ROUTETEMPLETE               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_APPROVALLINE_TO_ROUTETEMPLETE);

    /* Package Project To Series */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPACKAGEPROJECTTOSERIES      = "relationship_gscPackageProjectToSeries";
    public static final String RELATIONSHIP_GSCPACKAGEPROJECTTOSERIES               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPACKAGEPROJECTTOSERIES);

    /* Project EOD To Project EOD */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTEODTOPROJECTEOD      = "relationship_gscProjectEODToProjectEOD";
    public static final String RELATIONSHIP_GSCPROJECTEODTOPROJECTEOD               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTEODTOPROJECTEOD);

    /* Project To Project EOD */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTTOPROJECTEOD      = "relationship_gscProjectToProjectEOD";
    public static final String RELATIONSHIP_GSCPROJECTTOPROJECTEOD               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTTOPROJECTEOD);

    /* Project To Project Remark */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTTOPROJECTREMARK      = "relationship_gscProjectToProjectRemark";
    public static final String RELATIONSHIP_GSCPROJECTTOPROJECTREMARK               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTTOPROJECTREMARK);

    /* Series To Project */
    public static final String SYMBOLIC_RELATIONSHIP_GSCSERIESTOPROJECT      = "relationship_gscSeriesToProject";
    public static final String RELATIONSHIP_GSCSERIESTOPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCSERIESTOPROJECT);

    /* Ship To Project */
    public static final String SYMBOLIC_RELATIONSHIP_GSCSHIPTOPROJECT      = "relationship_gscShipToProject";
    public static final String RELATIONSHIP_GSCSHIPTOPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCSHIPTOPROJECT);
    
    /* Approval Request To Part, Document */
    public static final String SYMBOLIC_RELATIONSHIP_GSCAFFECTEDITEM      = "relationship_gscAffectedItem";
    public static final String RELATIONSHIP_GSCAFFECTEDITEM               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCAFFECTEDITEM);

    /* Ship To Project */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPACKAGEPROJECTTOPROJECT      = "relationship_gscPackageProjectToProject";
    public static final String RELATIONSHIP_GSCPACKAGEPROJECTTOPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPACKAGEPROJECTTOPROJECT);

    /* BPC To BPC */
    public static final String SYMBOLIC_RELATIONSHIP_GSCBPCTOBPC      = "relationship_gscBPCToBPC";
    public static final String RELATIONSHIP_GSCBPCTOBPC               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCBPCTOBPC);

    /* Change And Review Check List Relationship */
    public static final String SYMBOLIC_RELATIONSHIP_GSCEOREVIEWCHECKLIST = "relationship_gscEOReviewCheckList";
    public static final String RELATIONSHIP_GSCEOREVIEWCHECKLIST          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCEOREVIEWCHECKLIST);

    /* PFG Part To VPMInstance */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPFGPARTTOVPMINSTANCE = "relationship_gscPFGPartToVPMInstance";
    public static final String RELATIONSHIP_GSCPFGPARTTOVPMINSTANCE          = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPFGPARTTOVPMINSTANCE);

    /* BPC To EBOM */
    public static final String SYMBOLIC_RELATIONSHIP_GSCBPCTOEBOM = "relationship_gscBPCToEBOM";
    public static final String RELATIONSHIP_GSCBPCTOEBOM = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCBPCTOEBOM);

    /* Project Remark To EOD */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOEOD      = "relationship_gscProjectRemarkToEOD";
    public static final String RELATIONSHIP_GSCPROJECTREMARKTOEOD               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOEOD);

    /* Project Remark To Option Group */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOOPTIONGROUP      = "relationship_gscProjectRemarkToOptionGroup";
    public static final String RELATIONSHIP_GSCPROJECTREMARKTOOPTIONGROUP               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOOPTIONGROUP);

    /* MO To IF Master */
    public static final String SYMBOLIC_RELATIONSHIP_GSCMOTOIFMASTER      = "relationship_gscMOToIFMaster";
    public static final String RELATIONSHIP_GSCMOTOIFMASTER               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCMOTOIFMASTER);

    /* EBOM To CADBOM Sync Relationship */
    public static final String RELATIONSHIP_VPLMINTEG_VPLMPROJECTION      = VPLMIntegrationConstants.REL_ITG_PROJECTION;

    /* Project To OOL */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTTOOOL      = "relationship_gscProjectToOOL";
    public static final String RELATIONSHIP_GSCPROJECTTOOOL               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTTOOOL);
    
    /* Change To Project */
    public static final String SYMBOLIC_RELATIONSHIP_GSCRELATEDPROJECT      = "relationship_gscRelatedProject";
    public static final String RELATIONSHIP_GSCRELATEDPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCRELATEDPROJECT);

    /* Engine Model To OOL */
    public static final String SYMBOLIC_RELATIONSHIP_GSCENGINEMODELTOOOL      = "relationship_gscEngineModelToOOL";
    public static final String RELATIONSHIP_GSCENGINEMODELTOOOL               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCENGINEMODELTOOOL);

    /* gscADG To VPMReference */
    public static final String SYMBOLIC_RELATIONSHIP_GSCADGTOVPMREFERENCE      = "relationship_gscADGToVPMReference";
    public static final String RELATIONSHIP_GSCADGTOVPMREFERENCE               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCADGTOVPMREFERENCE);
    
    /* Change Order To Change Order */
    public static final String SYMBOLIC_RELATIONSHIP_GSCRELATEDCHANGEORDER      = "relationship_gscRelatedChangeOrder";
    public static final String RELATIONSHIP_GSCRELATEDCHANGEORDER               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCRELATEDCHANGEORDER);
    
    /* Manufacturing To Series */
    public static final String SYMBOLIC_RELATIONSHIP_GSCMOTOSERIES             = "relationship_gscMOToSeries";
    public static final String RELATIONSHIP_GSCMOTOSERIES                      = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCMOTOSERIES);

    /* Manufacturing Order To Manufacturing Order */
    public static final String SYMBOLIC_RELATIONSHIP_GSCMOTOSERIESMOLIST       = "relationship_gscMOToSeriesMOList";
    public static final String RELATIONSHIP_GSCMOTOSERIESMOLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCMOTOSERIESMOLIST);
    
    /* Manufacturing Order To Manufacturing Order */
    public static final String SYMBOLIC_RELATIONSHIP_GSCCHANGERELATEDORDEROPTIONLIST       = "relationship_gscChangeRelatedOrderOptionList";
    public static final String RELATIONSHIP_GSCCHANGERELATEDORDEROPTIONLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCCHANGERELATEDORDEROPTIONLIST);
    
    /* gscChangeValidationMaster To gscChangeValidation */
    public static final String SYMBOLIC_RELATIONSHIP_GSCCHANGEVALIDATIONMASTER       = "relationship_gscChangeValidationMaster";
    public static final String RELATIONSHIP_GSCCHANGEVALIDATIONMASTER                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCCHANGEVALIDATIONMASTER);

    /* ReferenceDocument */
    public static final String SYMBOLIC_RELATIONSHIP_REFERENCEDOCUMENT       = "relationship_ReferenceDocument";

    /* PFG To Option Group */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPFGTOVARIANT       = "relationship_gscPFGToVariant";
    public static final String RELATIONSHIP_GSCPFGTOVARIANT                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPFGTOVARIANT);

    /* EGM To Option Group */
    public static final String SYMBOLIC_RELATIONSHIP_GSCEGMTOVARIANT       = "relationship_gscEGMToVariant";
    public static final String RELATIONSHIP_GSCEGMTOVARIANT                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCEGMTOVARIANT);

    /* gscADPart To VPMReference */
    public static final String SYMBOLIC_RELATIONSHIP_GSCADPARTTOVPMREFERENCE       = "relationship_gscADPartToVPMReference";
    public static final String RELATIONSHIP_GSCADPARTTOVPMREFERENCE                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCADPARTTOVPMREFERENCE);

    /* PLMReference To PLMReference */
    public static final String SYMBOLIC_RELATIONSHIP_DELFMIFUNCTIONIDENTIFIEDINSTANCE       = "relationship_DELFmiFunctionIdentifiedInstance";
    public static final String RELATIONSHIP_DELFMIFUNCTIONIDENTIFIEDINSTANCE                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_DELFMIFUNCTIONIDENTIFIEDINSTANCE);
    
    /* gscUnitExtensionRequest to Part*/
    public static final String SYMBOLIC_RELATIONSHIP_GSCUNITEXTENSIONLIST       = "relationship_gscUnitExtensionList";
    public static final String RELATIONSHIP_GSCUNITEXTENSIONLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCUNITEXTENSIONLIST);

    /* AD Model To AD EngineGroup*/
    public static final String SYMBOLIC_RELATIONSHIP_GSCADMODELTOADENGINEGROUP      = "relationship_gscADModelToADEngineGroup";
    public static final String RELATIONSHIP_GSCADMODELTOADENGINEGROUP               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCADMODELTOADENGINEGROUP);

    /* gscADPart To AD PLMDocConnection*/
    public static final String SYMBOLIC_RELATIONSHIP_GSCADPARTTOPLMDOCCONNECTION      = "relationship_gscADPartToPLMDocConnection";
    public static final String RELATIONSHIP_GSCADPARTTOPLMDOCCONNECTION               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCADPARTTOPLMDOCCONNECTION);
    
    /* Manufacturing Order To Common Manufacturing Order gscMOToCommonMOList */
    public static final String SYMBOLIC_RELATIONSHIP_GSCMOTOCOMMONMOLIST       = "relationship_gscMOToCommonMOList";
    public static final String RELATIONSHIP_GSCMOTOCOMMONMOLIST                = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCMOTOCOMMONMOLIST);

    /* Logical Feature To gscDesignReflection*/
    public static final String SYMBOLIC_RELATIONSHIP_GSCPFGTODESIGNREFLECTION      = "relationship_gscPFGToDesignReflection";
    public static final String RELATIONSHIP_GSCPFGTODESIGNREFLECTION               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPFGTODESIGNREFLECTION);
    
    /* MO To EBOM Part/Drawing*/
    public static final String SYMBOLIC_RELATIONSHIP_GSCEBOMAFFECTEDITEM      = "relationship_gscEBOMAffectedItem";
    public static final String RELATIONSHIP_GSCEBOMAFFECTEDITEM               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCEBOMAFFECTEDITEM);
    
    /* Leaf PFG To Order Option List*/
    public static final String SYMBOLIC_RELATIONSHIP_GSCPFGTOAPPLYORDEROPTIONLIST      = "relationship_gscPFGToApplyOrderOptionList";
    public static final String RELATIONSHIP_GSCPFGTOAPPLYORDEROPTIONLIST               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPFGTOAPPLYORDEROPTIONLIST);

    /* gscPGU */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPGU      = "relationship_gscPGU";
    public static final String RELATIONSHIP_GSCPGU               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPGU);

    /* gscProject to gscEngineGroupMfg */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTTOENGINEGROUPMFG      = "relationship_gscProjectToEngineGroupMfg";
    public static final String RELATIONSHIP_GSCPROJECTTOENGINEGROUPMFG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTTOENGINEGROUPMFG);

    /* Option Group to gscADG */
    public static final String SYMBOLIC_RELATIONSHIP_GSCOPTIONGROUPTOADG      = "relationship_gscOptionGroupToADG";
    public static final String RELATIONSHIP_GSCOPTIONGROUPTOADG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCOPTIONGROUPTOADG);

    /* AD Model to gscProject */
    public static final String SYMBOLIC_RELATIONSHIP_GSCADMODELTOPROJECT      = "relationship_gscADModelToProject";
    public static final String RELATIONSHIP_GSCADMODELTOPROJECT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCADMODELTOPROJECT);

    /* Project Remark to PFG */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOPFG      = "relationship_gscProjectRemarkToPFG";
    public static final String RELATIONSHIP_GSCPROJECTREMARKTOPFG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOPFG);

    /* Project Remark to ADG */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOADG      = "relationship_gscProjectRemarkToADG";
    public static final String RELATIONSHIP_GSCPROJECTREMARKTOADG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOADG);

    /* Project Remark to PFG Mfg */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOPFGMFG      = "relationship_gscProjectRemarkToPFGMfg";
    public static final String RELATIONSHIP_GSCPROJECTREMARKTOPFGMFG               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOPFGMFG);

    /* PFG Mfg To Drawing Distribute */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPFGMFGTODRAWINGDISTRIBUTE      = "relationship_gscPFGMfgToDrawingDistribute";
    public static final String RELATIONSHIP_GSCPFGMFGTODRAWINGDISTRIBUTE               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPFGMFGTODRAWINGDISTRIBUTE);

    /* Project Remark To Business Unit/Person Distribute */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOCHECK      = "relationship_gscProjectRemarkToCheck";
    public static final String RELATIONSHIP_GSCPROJECTREMARKTOCHECK               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTREMARKTOCHECK);

    /* gscProject To gscPFGPlan */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTPLAN      = "relationship_gscProjectPlan";
    public static final String RELATIONSHIP_GSCPROJECTPLAN               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTPLAN);

    /* gscProject To gscProjectEODExcelResult */
    public static final String SYMBOLIC_RELATIONSHIP_GSCPROJECTTOEODEXCELRESULT      = "relationship_gscProjectToEODExcelResult";
    public static final String RELATIONSHIP_GSCPROJECTTOEODEXCELRESULT               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCPROJECTTOEODEXCELRESULT);

    /* gscADG To gscBPC */
    public static final String SYMBOLIC_RELATIONSHIP_GSCADGTOBPC      = "relationship_gscADGtoBPC";
    public static final String RELATIONSHIP_GSCADGTOBPC               = PropertyUtil.getSchemaProperty(SYMBOLIC_RELATIONSHIP_GSCADGTOBPC);
}
