
import matrix.db.Context;
import com.dassault_systemes.EKLEngine.completion.CompletionJPOEvaluator;


/**
 * ${CLASSNAME}
 */
public final class Functional_ExportFlow_Design_mxJPO extends CompletionJPOEvaluator {

	/**
	 * Attributes
	 */
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_0__RFLPLMFlow_div_RFLPLM = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("RFLPLMFlow/RFLPLMFlowReference");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_1__RFLPLMFlow_ = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("RFLPLMFlow");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_2__RFLPLMFlow_Expand_ = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("RFLPLMFlow_Expand");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_3__RFLPLMImplementConnec = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("RFLPLMImplementConnection");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_4__RFLPLMImplementConnec = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("RFLPLMImplementConnection_AddAllImplementCnx");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_5__PLMDocConnection_ = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("PLMDocConnection");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_6__PLMDocConnection_retr = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("PLMDocConnection_retrieveAllDocuments");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_7__Config_GetStructConfi = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("Config_GetStructConfig");

	/**
	 * evaluate
	 * @param iContext
	 * @param iPLMIDSet
	 * @param oPLMIDSet
	 */
	public final void evaluate(matrix.db.Context iContext, com.dassault_systemes.EKLEngine.common.lib.PLMIDSet iPLMIDSet, com.dassault_systemes.EKLEngine.common.lib.PLMIDSet oPLMIDSet)	
			throws Exception {
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet InputPLMIDSetRestrictedToRefs = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet SetOfPLMRoutesForStructuredFlow = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet PLMIDSetRestrictedToRefs = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet SetOfPLMRoutesForImplementLinks = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet SetOfPLMRoutesForDocuments = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet SetOfConfigObjectsPLMIDs = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		InputPLMIDSetRestrictedToRefs.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.Restrict( iContext , iPLMIDSet, _STRING_0__RFLPLMFlow_div_RFLPLM ) );
		SetOfPLMRoutesForStructuredFlow.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_1__RFLPLMFlow_, _STRING_2__RFLPLMFlow_Expand_, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { InputPLMIDSetRestrictedToRefs } ) );
		PLMIDSetRestrictedToRefs.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( InputPLMIDSetRestrictedToRefs, com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.Restrict( iContext , com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( SetOfPLMRoutesForStructuredFlow ), _STRING_0__RFLPLMFlow_div_RFLPLM ) ) );
		SetOfPLMRoutesForImplementLinks.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_3__RFLPLMImplementConnec, _STRING_4__RFLPLMImplementConnec, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { PLMIDSetRestrictedToRefs } ) );
		SetOfPLMRoutesForDocuments.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_5__PLMDocConnection_, _STRING_6__PLMDocConnection_retr, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { PLMIDSetRestrictedToRefs } ) );
		SetOfConfigObjectsPLMIDs.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMProcedure( iContext , _STRING_7__Config_GetStructConfi, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { iPLMIDSet } ) );
		oPLMIDSet.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( iPLMIDSet, com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( SetOfPLMRoutesForStructuredFlow ) ), com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( SetOfPLMRoutesForImplementLinks ) ), com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( SetOfPLMRoutesForDocuments ) ), SetOfConfigObjectsPLMIDs ) );
	}
}
