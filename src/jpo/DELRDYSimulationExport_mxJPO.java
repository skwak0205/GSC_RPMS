
import matrix.db.Context;
import com.dassault_systemes.EKLEngine.completion.CompletionJPOEvaluator;


/**
 * ${CLASSNAME}
 */
public final class DELRDYSimulationExport_mxJPO extends CompletionJPOEvaluator {

	/**
	 * Attributes
	 */
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_0__SIMObjSimulationGener = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("SIMObjSimulationGeneric");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_1__sim_retrieveCategorie = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("sim_retrieveCategories");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_2__PLMDocConnection_ = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("PLMDocConnection");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_3__PLMDocConnection_retr = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("PLMDocConnection_retrieveAllDocuments");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_4__sim_addSimulatedModel = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("sim_addSimulatedModel");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_5__SIMObjSimulationCateg = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("SIMObjSimulationCategoryAndProdCnx/SIMObjSimulationCategoryReference");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_6__sim_retrieveSimuRep_ = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("sim_retrieveSimuRep");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_7__sim_AddPointedDesignT = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("sim_AddPointedDesignTable");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_8__SIMObjSimulationV5Gen = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("SIMObjSimulationV5Generic/SIMObjSimulationV5RepReferenceGeneric");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_9__sim_retrieveExternalD = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("sim_retrieveExternalDocumentfromSimuRep");
	private final static com.dassault_systemes.EKLEngine.common.lib.implementation.StringType _STRING_10__sim_addPublishResult = new com.dassault_systemes.EKLEngine.common.lib.implementation.StringType("sim_addPublishResultCnx");

	/**
	 * evaluate
	 * @param iContext
	 * @param iPLMIDSet
	 * @param oPLMIDSet
	 */
	public final void evaluate(matrix.db.Context iContext, com.dassault_systemes.EKLEngine.common.lib.PLMIDSet iPLMIDSet, com.dassault_systemes.EKLEngine.common.lib.PLMIDSet oPLMIDSet)	
			throws Exception {
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet IdsCategoriesAndSo = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet IdsExtDoc = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet IdsSimuRep = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet IdsSimuPointedTable = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet IdsExtDocFromSimuRep = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet IdsPublishResult = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMIDSet IdsSetModel = new com.dassault_systemes.EKLEngine.common.lib.PLMIDSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet RouteSetCategoriesAndSo = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet RouteSetModel = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet RouteSetExtDoc = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet RouteSimuRep = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet RouteSimuPointedTable = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet RouteExtDocFromSimuRep = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet RouteSetProdAndChildren = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet RoutePublishResult = new com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet();
		RouteSetCategoriesAndSo.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_0__SIMObjSimulationGener, _STRING_1__sim_retrieveCategorie, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { iPLMIDSet } ) );
		IdsCategoriesAndSo.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( RouteSetCategoriesAndSo ) );
		RouteSetExtDoc.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_2__PLMDocConnection_, _STRING_3__PLMDocConnection_retr, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { IdsCategoriesAndSo } ) );
		IdsExtDoc.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( RouteSetExtDoc ) );
		RouteSetModel.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_0__SIMObjSimulationGener, _STRING_4__sim_addSimulatedModel, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.Restrict( iContext , IdsCategoriesAndSo, _STRING_5__SIMObjSimulationCateg ) } ) );
		IdsSetModel.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( RouteSetModel ) );
		RouteSimuRep.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_0__SIMObjSimulationGener, _STRING_6__sim_retrieveSimuRep_, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.Restrict( iContext , IdsCategoriesAndSo, _STRING_5__SIMObjSimulationCateg ) } ) );
		IdsSimuRep.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( RouteSimuRep ) );
		RouteSimuPointedTable.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_0__SIMObjSimulationGener, _STRING_7__sim_AddPointedDesignT, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.Restrict( iContext , IdsSimuRep, _STRING_8__SIMObjSimulationV5Gen ) } ) );
		IdsSimuPointedTable.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( RouteSimuPointedTable ) );
		RouteExtDocFromSimuRep.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_0__SIMObjSimulationGener, _STRING_9__sim_retrieveExternalD, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.Restrict( iContext , IdsSimuRep, _STRING_8__SIMObjSimulationV5Gen ) } ) );
		IdsExtDocFromSimuRep.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( RouteExtDocFromSimuRep ) );
		RoutePublishResult.setValue( com.dassault_systemes.EKLEngine.completion.lib.Completion.ExecutePLMFunction( iContext , _STRING_0__SIMObjSimulationGener, _STRING_10__sim_addPublishResult, new com.dassault_systemes.EKLEngine.common.lib.implementation.ObjectType[] { com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.Restrict( iContext , IdsCategoriesAndSo, _STRING_5__SIMObjSimulationCateg ) } ) );
		IdsPublishResult.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMRouteSet.Ids( RoutePublishResult ) );
		oPLMIDSet.setValue( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( com.dassault_systemes.EKLEngine.common.lib.PLMIDSet.plus( IdsCategoriesAndSo, IdsExtDoc ), IdsSetModel ), IdsSimuRep ), IdsSimuPointedTable ), IdsExtDocFromSimuRep ), IdsPublishResult ) );
	}
}
