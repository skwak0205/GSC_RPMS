/*   emxObjectAccess.
**
**   Copyright (c) 2006-2020 Dassault Systemes.
**   All Rights Reserved.
**   This program contains proprietary and trade secret information of MatrixOne,
**   Inc.  Copyright notice is precautionary only
**   and does not evidence any actual or intended publication of such program.
**
**   This JPO contains code for the generic Object "Access" implementation.
**
*/

import matrix.db.Context;

/**
 * The <code>emxObjectAccess</code> class contains code for the generic "Access" implementation.
 *
 * @version Common Components 11.0.JCI.0 - Copyright (c) 2006, MatrixOne, Inc.
 */
    public class emxObjectAccess_mxJPO extends emxObjectAccessBase_mxJPO
    {
        /**
        * Constructor.
        *
        * @param context the eMatrix <code>Context</code> object.
        * @param args holds no arguments.
        * @throws Exception if the operation fails.
        */

        public emxObjectAccess_mxJPO (Context context, String[] args)
          throws Exception
        {
            super(context, args);
        }
    }
