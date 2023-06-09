//IEFProductCentralUtil-disabled.java
//
// 
// Copyright (c) 2002 MatrixOne, Inc.
// All Rights Reserved
// This program contains proprietary and trade secret information of
// MatrixOne, Inc.  Copyright notice is precautionary only and does
// not evidence any actual or intended publication of such program.

import matrix.db.Context;
import matrix.util.MatrixException;

import com.matrixone.apps.domain.util.MapList;

/**
 * The <code>IEFProductCentralUtil-disabled</code> class represents the JPO for
 * obtaining the MS Office integration menus
 *
 * @version AEF 10.5 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class IEFProductCentralUtil_mxJPO
{

/**
   * Constructs a new IEFProductCentralUtil-disabled JPO object.
   *
   * @param context the eMatrix <code>Context</code> object
   * @param args an array of String arguments for this method
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */
  public IEFProductCentralUtil_mxJPO (Context context, String[] args) throws Exception
  {
  }
   
  /**
   * Get Products of a the current user
   * Returns a maplist of the current users Product ids
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getCurrentUserProducts(Context context, String[] args) throws MatrixException
   {
     MapList productList = new MapList();
    //Dummy function when Product central and supplier central is not installed 
    //this allows to compile all the JPOs without any issue
     return productList;
   }

  /**
   * Get the folders of a Product
   * Returns a maplist of features 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getOwnedContextFeatures(Context context, String[] args) throws MatrixException
   {
     MapList featureList = new MapList();
        //Dummy function when Product central and supplier central is not installed 
        //this allows to compile all the JPOs without any issue
     return featureList;
   }

  /**
   * Get the folders of a Product
   * Returns a maplist of features 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getCurrentUserFeatures(Context context, String[] args) throws MatrixException
   {
     MapList featureList = new MapList();
    //Dummy function when Product central and supplier central is not installed 
    //this allows to compile all the JPOs without any issue
     return featureList;
   }
   
  /**
   * Get the sub folders of a Product
   * Returns a maplist of sub features 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getOptionList(Context context, String[] args) throws MatrixException
   {
     MapList subFeatureList = new MapList();
        //Dummy function when Product central and supplier central is not installed 
        //this allows to compile all the JPOs without any issue
     return subFeatureList;
   }

  /**
   * Get the requirements of a Product
   * Returns a maplist of requirements 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getCurrentUserRequirements(Context context, String[] args) throws MatrixException
   {
     MapList requirementList = new MapList();
    //Dummy function when Product central and supplier central is not installed 
    //this allows to compile all the JPOs without any issue
     return requirementList;
   }

  /**
   * Get the requirements of a Product
   * Returns a maplist of requirements 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getRelatedRequirements(Context context, String[] args) throws MatrixException
   {
     MapList requirementList = new MapList();
        //Dummy function when Product central and supplier central is not installed 
        //this allows to compile all the JPOs without any issue
     return requirementList;
   }

  /**
   * Get the builds of a Product
   * Returns a maplist of builds 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getCurrentUserBuilds(Context context, String[] args) throws MatrixException
   {
     MapList buildsList = new MapList();
        //Dummy function when Product central and supplier central is not installed 
        //this allows to compile all the JPOs without any issue
     return buildsList;
   }

  /**
   * Get the test cases of a Product
   * Returns a maplist of test cases 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getRelatedTestCases(Context context, String[] args) throws MatrixException
   {
     MapList testCasesList = new MapList();
        //Dummy function when Product central and supplier central is not installed 
        //this allows to compile all the JPOs without any issue
     return testCasesList;
   }

  /**
   * Get the use cases of a Product
   * Returns a maplist of use cases 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public MapList getRelatedUseCases(Context context, String[] args) throws MatrixException
   {
     MapList useCasesList = new MapList();
        //Dummy function when Product central and supplier central is not installed 
        //this allows to compile all the JPOs without any issue
     return useCasesList;
   }
}
