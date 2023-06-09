// IEFPMCUtilBase.java
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
 * The <code>IEFUtil</code> class represents the JPO for
 * obtaining the MS Office integration menus
 *
 * @version AEF 10.5 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class IEFPMCUtilBase_mxJPO 
{

/**
   * Constructs a new IEFUtil JPO object.
   *
   * @param context the eMatrix <code>Context</code> object
   * @param args an array of String arguments for this method
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */
  public IEFPMCUtilBase_mxJPO (Context context, String[] args) throws Exception
  {
  }
   
  /**
   * Get Projects of a the current user
   * Returns a maplist of the current users project ids
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public static MapList getCurrentUserProjects(Context context, String[] args) throws MatrixException
   {

     MapList projectList = new MapList();
	//Dummy function when program central is not installed 
	//this allows to compile all the JPOs without any issue
     return projectList;
   }

  /**
   * Get the folders of a Project
   * Returns a maplist of folders 
   *
   * @param context the eMatrix <code>Context</code> object
   * @throws Exception if the operation fails
   * @since AEF 10.5
   */

   public static MapList getProjectFolders(Context context, String[] args) throws MatrixException
   {
     MapList folderList = new MapList();
	//Dummy function when program central is not installed 
	//this allows to compile all the JPOs without any issue
     return folderList;
   }
}
