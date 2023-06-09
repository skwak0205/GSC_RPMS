/* emxProjectFinancialItemRelationship.java

   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of
   MatrixOne, Inc.  Copyright notice is precautionary only and does
   not evidence any actual or intended publication of such program.

   static const char RCSID[] = $Id: ${CLASSNAME}.java.rca 1.10.2.2 Thu Dec  4 07:56:09 2008 ds-ss Experimental ${CLASSNAME}.java.rca 1.10.2.1 Thu Dec  4 01:54:59 2008 ds-ss Experimental ${CLASSNAME}.java.rca 1.10 Wed Oct 22 15:50:30 2008 przemek Experimental przemek $
*/

import java.io.*;
import java.util.*;
import matrix.db.*;
import matrix.util.*;
import com.matrixone.apps.program.*;
import com.matrixone.apps.domain.*;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.common.util.*;

/**
 * The <code>emxProjectFinancialItemRelationship</code> class represents the
 * Project Assessment relationship JPO functionality for the AEF type.
 *
 * @version AEF 9.5.2.0 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxProjectFinancialItemRelationship_mxJPO extends emxProjectFinancialItemRelationshipBase_mxJPO
{

    /**
     * Constructor.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since PMC 10.0.0.0
     */
    public emxProjectFinancialItemRelationship_mxJPO (Context context, String[] args)
        throws Exception
    {
        super(context, args);
    }
}
