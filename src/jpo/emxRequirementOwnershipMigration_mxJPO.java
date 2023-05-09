/*
**  emxRequirementRDOMigration
**
**  Copyright (c) 1992-2020 Dassault Systemes.
**  All Rights Reserved.
**  This program contains proprietary and trade secret information of MatrixOne,
**  Inc.  Copyright notice is precautionary only
**  and does not evidence any actual or intended publication of such program.
**
*/

import matrix.db.Context;


/**

 */
public class emxRequirementOwnershipMigration_mxJPO extends emxRequirementOwnershipMigrationBase_mxJPO {


    /**
	 * 
	 */
	private static final long serialVersionUID = -6361938157558341853L;

	/**
     * Constructor.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation
     * @since V6R2018x
     * @grade 0
     */
    public emxRequirementOwnershipMigration_mxJPO (Context context, String[] args)
        throws Exception
    {
        super(context, args);
    }
}
