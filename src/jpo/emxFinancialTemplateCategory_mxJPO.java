/* emxFinancialTemplateCategory.java

   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of
   MatrixOne, Inc.  Copyright notice is precautionary only and does
   not evidence any actual or intended publication of such program.

   static const char RCSID[] = $Id: emxFinancialTemplateCategory.java.rca 1.6 Wed Oct 22 16:21:23 2008 przemek Experimental przemek $
*/

import matrix.db.*;

/**
 * The <code>emxFinancialTemplateCategory</code> class represents the
 * Financial Benefit Category and Financial Cost Category JPO
 * functionality for the AEF type.
 *
 * @version AEF 10.0.SP4 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxFinancialTemplateCategory_mxJPO extends emxFinancialTemplateCategoryBase_mxJPO
{

    /**
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since AEF 10.0.SP4
     * @grade 0
     */
    public emxFinancialTemplateCategory_mxJPO (Context context, String[] args)
        throws Exception
    {
      super(context, args);
    }
}
