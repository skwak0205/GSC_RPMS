<%--  emxPackagesConfigurationPublish.jsp  --  Publish the selected packages
  Copyright (c) 1992-2011 Dassault Systemes.
  All Rights Reserved.
  This program contains proprietary and trade secret information of MatrixOne,Inc.
  Copyright notice is precautionary only and does not evidence any actual or intended publication of such program

  static const char RCSID[] = $Id: emxPackagesConfigurationCreatePackage.jsp.rca 1.35 Wed Oct 22 16:06:17 2008 przemek Experimental przemek $
 --%>

<%@include file = "../emxUICommonAppInclude.inc"%>
<%@include file = "../common/emxNavigatorTopErrorInclude.inc"%>
<%@page import = "com.dassault_systemes.DictionaryAuthor_itfs.DAuManagerAccess"%>
<%@page import = "com.dassault_systemes.DictionaryAuthor_itfs.IDAuManager"%>
<%@page import = "com.dassault_systemes.DictionaryAuthor_itfs.IDAuPackage"%>
<%@page import = "com.dassault_systemes.DictionaryAuthor_itfs.IDAuAttribute"%>
<%@page import = "com.dassault_systemes.DictionaryAuthor_itfs.DAuData"%>
<%@page import = "com.dassault_systemes.DictionaryAuthor_itfs.DAuUtilities"%>
<script language="JavaScript" src="../common/scripts/emxUIConstants.js" type="text/javascript"></script>
<%
String selPacksIds[] = emxGetParameterValues(request, "emxTableRowId");
int nbPacks = (null != selPacksIds) ? selPacksIds.length : -1;

int k = 0;
String selectedFrame = "";
System.out.println(nbPacks + " package(s) to Index");
if ((null != selPacksIds) )
{
    int nbSelectedPacks = 0;
	IDAuManager manager = DAuManagerAccess.getDAuManager();
	if (null != manager)
	{
		try
		{
			for (k = 0; k<nbPacks; k++)
			{	
				String packId = selPacksIds[k];
				// JPI le 07 octobre 11 curieusement la forme des Ids a chang :  |38016.53567.5632.5790||0,1				
				if (packId.startsWith("|") == true ) {
					packId = packId.substring(1);
				}
				int idxPipes = packId.indexOf("|");
				if (idxPipes > 0 ) {								
					packId = packId.substring(0,idxPipes);
				}
				IDAuPackage pack = manager.getPackageFromId(context,packId);
				if (null != pack){
					//Update search index : 
					if ("Yes".equals(pack.getPublishStatus(context))){
						//pack.UpdateIndexForPackage(context);
						manager.addImportedPackage(pack.getName());
						nbSelectedPacks++;
					}else 
						emxNavErrorObject.addMessage(pack.getName(context) + " is Not published. ");
				}
				else
					System.out.println("ERROR : package " + packId + " not found");
			}
			Map<String, String> indexingState = null;
			if(!manager.getImportedPackages().isEmpty()){
				indexingState = manager.UpdateIndexForPackages(context, "Manual");
			}
			if(indexingState==null) {
				indexingState = Collections.emptyMap();
			}
			String indexingStatus = indexingState.getOrDefault("status", "Ended");
			ArrayList<String> numberOfPack = new ArrayList<String>();
			if(!"Rejected".equalsIgnoreCase(indexingStatus)) {
				System.out.println("Indexation OK : " + nbSelectedPacks + " Indexed on " + nbPacks );
				numberOfPack.add(Integer.toString(nbSelectedPacks));
				numberOfPack.add(Integer.toString(nbPacks));
				String indexedOK = DAuUtilities.getSpecializationStringResource(context,"emxDataSpecialization.UpdateIndexOK",numberOfPack);
				emxNavErrorObject.addMessage(indexedOK);	
			} else {
				String indexingUser = indexingState.getOrDefault("User", "?unknown?");
				String indexingDate;
				try {
					indexingDate = new Date(Long.parseLong(indexingState.getOrDefault("startedAt", ""))).toString();
				} catch(NumberFormatException nfe) {
					indexingDate = new Date().toString();
				}
				System.out.println("Indexation OG : " + indexingUser + " is indexing since " + indexingDate);
				numberOfPack.add(indexingUser);
				numberOfPack.add(indexingDate);
				String indexedKO = DAuUtilities.getSpecializationStringResource(context,"emxDataSpecialization.UpdateIndexOG",numberOfPack);
				emxNavErrorObject.addMessage(indexedKO);	
			}
		}
		catch (Exception e)
		{
			String IndexationFailure = DAuUtilities.getSpecializationStringResource(context,"emxDataSpecialization.IndexationFailure");
			System.out.println("Indexation KO" + e.getMessage());
			emxNavErrorObject.addMessage( IndexationFailure + e.getMessage());									
		}		
	}
}
%>
<%@include file="../common/emxNavigatorBottomErrorInclude.inc"%>
<%-- Reload the page --%>
<script language="Javascript" >
window.parent.location.href = window.parent.location.href ;	
</script>
