/*
**	emxMailUtil
**
**  Copyright (c) 1992-2020 Dassault Systemes.
**  All Rights Reserved.
**  This program contains proprietary and trade secret information of MatrixOne,
**  Inc.  Copyright notice is precautionary only
**  and does not evidence any actual or intended publication of such program
**
*/

import matrix.db.Context;

/**
 * The <code>emxMailUtil</code> class contains mail utility methods.
 *
 * @version AEF 10.0.1.0 - Copyright (c) 2003, MatrixOne, Inc.
 */

public class emxMailUtil_mxJPO extends emxMailUtilBase_mxJPO
{

    /**
     * Constructor.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since EC 10.0.0.0
     */

    public emxMailUtil_mxJPO (Context context, String[] args)
        throws Exception
    {
        super(context, args);
    }

}
