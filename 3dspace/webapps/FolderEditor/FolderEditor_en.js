define("DS/FolderEditor/FolderEditor_en",{});define("DS/FolderEditor/assets/nls/AddCommentPopup",{SelectFile_PlaceHolder:"Select a File...",SelectFile_Button:"Browse",PanelTitle:"Check In",Comment_Title:"Comment",Comment_PlaceHolder:"Enter the Check-In Reason...",OK:"Check In",Cancel:"Cancel"});define("DS/FolderEditor/assets/nls/AddMovePopup",{operationToPerform:"Which operation to perform with these objects ?",msg_Title:'{number} object(s) have been dropped to bookmark "{folderName}".',Add:"Add",Move:"Move"});define("DS/FolderEditor/assets/nls/BookmarkLocationPopup",{OK:"Ok",Cancel:"Cancel",OpenLocation:"Parent Bookmark",Bookmark_Locations:"Bookmark Locations",Open:"Open",OpenLocationPopupLable:"Select the Bookmark you want to open:",msg_error_BookmarkLocation:'Unable to open Location for bookmark "{name1}" (may have been deleted or is not accessible).',msg_error_BM_LOC_InValid_Object:"Unable to open Location selected object, Object may have been deleted or is not accessible.",removeFromSearchNotif:{success:"The object has been removed.",failNotAllowed:"Verify that the bookmark is in a state that allows change of content.",failNoRelationFound:"Verify that you have sufficient permission."}});define("DS/FolderEditor/assets/nls/FolderEditor",{name_Section_FavoriteFolders:"Favorites",name_Section_AllFolders:"Bookmarks",name_Section_Trash:"Deleted",name_Section_ParentFolders:"{number} Parent Bookmarks",name_Widget_Title:"Bookmark Editor",name_Widget_Help:"Bookmark Editor Help",name_RootFolder_type:"Bookmark Root",name_Folder_type:"Bookmark",folder_pref_dnd:"Drag and drop behavior",folder_pref_filter:"Hide bookmarked child products",folder_pref_allow_products_expand:"Enable expansion of products",folder_pref_subfolder:"Show child bookmarks in content view",folder_pref_populateTags:"Enable 6W tags for filtering bookmarks",folder_pref_activateRelationsColumns:"Show Related",useIndexAcceleration_label:"Show indexed data",folder_pref_autoSwitch_Label:"Auto switch from Authoring mode to Index mode",button_remove:"Remove",button_close:"Close",Move_label:"Move",Copy_label:"Copy",oneMin_Label:"1 Minute",threeMin_Label:"3 Minutes",fiveMin_Label:"5 Minutes",tenMin_Label:"10 Minutes",Never_Label:"Never",Upload_content_loading:"Upload content",AddExisting_content_loading:"Add existing content",Paste_content_loading:"Paste content",Remove_content_loading:"Remove content",Replace_revision_loading:"Replacing content with latest revision",Content_Loading:"Loading content...",Widget_Loading:"Loading widget...",Tree_Loading:"Loading tree...",Bookmark_Loading:"Selecting bookmark...",SetActiveBookmark_Loading:"Setting new global default bookmark...",UnsetActiveBookmark_Loading:"Unsetting global default bookmark...",EnableActiveBookmark_Loading:"Enabling global default bookmark...",DisableActiveBookmark_Loading:"Disabling global default bookmark...",Shared_label:"Shared",Personal_label:"Personal",label_ActionBar_AddToFavorites:"Add / Remove Favorites",label_ActionBar_RemoveFromFavorites:"Remove From Favorites",msg_success_copyLink_content:'Link to document "{filename}" inside the bookmark  "{foldername}" has been copied.',msg_success_copyLink_folder:'Link to the bookmark "{foldername}" has been copied.',msg_success_favoriteReordered:"Reordered successfully.",msg_info_contentcopied:"Content has been copied.",msg_info_contentcut:"Content has been cut.",msg_info_foldercut:"Bookmark has been cut.",msg_info_objetremoved:'{number} object(s) removed from bookmark "{foldername}".',msg_info_objectremovedfromfolders:'Object "{objectname}" successfully removed from {number} bookmark(s).',msg_info_Addinfolder:"{objectsAdded} Object(s) added to the bookmark {targetfolder}, {objectsAlreadyPresent} Object(s) to be added were already present.",msg_info_Addinfolder_1:"{objectsAdded} Object(s) added to the bookmark {targetfolder}.",msg_info_objetadded:'"{filename}" added to "{foldername}".',msg_info_objetmoved:"{objectsMoved} Object(s) moved to the bookmark {targetfolder}, {objectsAlreadyPresent} Object(s) to be moved were already present.",msg_info_objetmoved_1:"{objectsMoved} Object(s) moved to the bookmark {targetfolder}.",msg_info_foldercopied:'{number} bookmark(s) added to the bookmark "{foldername}".',msg_info_foldermoved:'{number} bookmark(s) moved to the bookmark "{foldername}".',msg_info_foldercreated:"Bookmark has been created.",msg_info_filecheckedout:"File has been successfully checked out.",msg_info_filecheckedin:"File has been successfully checked in.",msg_info_filecancelcheckout:"File Edit operation has been cancelled successfully.",msg_info_favoriteadded:'Bookmark "{name1}" has been added to Favorites.',msg_info_favoriteremoved:'Bookmark "{name1}" has been removed from Favorites.',msg_info_favoriterenamed:'Favorite "{name1}" has been renamed to "{name2}".',msg_info_rename:'Bookmark "{name1}" is renamed to "{name2}".',msg_info_refresh:"Refresh done.",msg_info_noResultSearch:"No result found.",msg_info_SortNonAvailable:"This column is not available for sorting during a search.",msg_info_replaceByRevision:"Object has been successfully replaced.",msg_info_replaceByLatestRevision:"Replace by Latest Revision was successfully executed",msg_info_replaceByLatestRevision_removed:"{number} Object(s) were explicitly  removed as they had their latest revision already present in the bookmark or multiple selected objects had a common revision.",msg_info_treeFilterRemove:"Tree Filters are removed after root Bookmark addition",msg_info_active_successfully:"{folderLabel} has been successfully set as global default bookmark.",msg_info_inactive_successfully:"Global default bookmark {folderLabel} has been successfully unset.",msg_info_enable_successfully:"Global default bookmark {folderLabel} is now enabled.",msg_info_disable_successfully:"Global default bookmark {folderLabel} is now disabled.",msg_info_defaultLocation:"The global default bookmark  is {folderLabel}.",msg_warning_MultipleSelection:"Multi-selection is not supported.",msg_warning_nocontent:"No content selected: please select content.",msg_warning_nofoldernocontent:"Nothing selected: please select content or bookmark.",msg_warning_nocurrentfolder:"No current bookmark: please select a bookmark.",msg_warning_noselection:"At least one object must be selected.",msg_warning_dndnotsupported:"This drop operation is not supported.",msg_warning_dndnotsupported_external_on_Section_FavoriteFolders:"The drop of external Bookmark(s) on Favorites section is not accepted.",msg_warning_dndnotsupported_from_different_tenant:"The drop of object(s) from a different 3DEXPERIENCE Platform is not accepted.",msg_warning_dndnotsupported_from_different_source:"This drop operation is not supported. Please drop objects from a collaborative space.",msg_warning_addnotsupported_from_different_tenant:"The addition of object(s) from a different 3DEXPERIENCE Platform is not accepted.",msg_warning_webInWinDnD:"The drop of object(s) is only accepted on 3DDashboard.",msg_warning_FolderAlreadyAmongFavorites:'The bookmark "{foldername}" is already among Favorites.',msg_warning_expandSaveLimitation:{Title:"The last expand status will not be saved.",Advice:"Collapse some Bookmarks to enable saving of expand status.",Details:"There are too many bookmarks expanded."},msg_warning_copyLink:"Selected Object is not a Bookmark.",msg_warning_LimitInCtxSearchOnAllBookmarks:"In Context Search not supported while Root Bookmark count is more than 200.",msg_error_contentNotFound:"The content is no longer accessible, or might have been deleted.",msg_error_OnRefreshCmdfailure:"Network error: Operation has been interrupted.",msg_error_preferencessnotsaved:"Unable to save your preferences.",msg_error_nopreferences:"Unable to retrieve your preferences.",msg_error_Addinfolder:"You cannot add content to this Bookmark.",msg_error_CopyFolder:"Unable to copy the bookmarks.",msg_error_MoveFolder:"Unable to move the bookmarks.",msg_error_MoveFolder_parent:"The target bookmark is one of the parent of the bookmark to move.",msg_error_CutPasteToRoot:"You cannot move the bookmarks to the root bookmark.",msg_error_Removefromfolder:"Unable to remove the objects from the bookmark.",msg_error_MoveInfolder:"Unable to move the objects.",msg_error_MoveInSamefolder:{Title:"Select a different bookmark.",Advice:"The source bookmark and the target bookmark are the same."},msg_error_rename:{Title:"You cannot rename the Bookmark : {name}.",Advice:"Verify that the name is not already used or you are using the correct credentials.",Details:"You may not have the required responsibility in the target collaborative space to rename this object."},msg_error_name_maxlength:"Enter a new name between 1 and {maxLength} characters.",msg_error_name_empty:"Enter a name.",msg_error_FolderActivate:{Title:"Unable to activate {name1}.",Advice:"The bookmark is no longer accessible, or might have been deleted."},msg_error_NoVisibleRootFolder:{title:"You do not have the required permissions to open the bookmark.",subtitle:"The parent bookmark might not be shared, or is in a maturity state that does not allow you to see it."},msg_error_TreeExpandCancelledNotReady:{title:"Unable to expand the tree.",subtitle:"Refresh the widget."},msg_error_forbiddenChar:{message:"Column name contains unvalid characters: {badChars}"},msg_error_contentnotavailable:{Title:"This bookmark is no longer accessible, or might have been deleted. ",Advice:"Refresh the widget."},msg_error_contentnotavailableInFavorite:{Title:"The bookmark is no longer accessible, or might have been deleted.",Advice:"Remove the bookmark from your favorites."},msg_InfoAction_Timeout:"The operation is taking longer than expected. It will continue in the background. Refresh to update the data.",msg_error_timeout:{title:"Unable to retrieve the content of the bookmark.",subtitle:"The connection to the server timed out.",message:"Verify your connection, or contact your administrator for support."},msg_error_folderPath:"The bookmark is no longer accessible, or might have been deleted.",msg_error_folderPath_roles:"This object is no longer accessible, or might have been deleted.",msg_error_appWhereUsedNotFound:{title:"You cannot open the Relations app.",subtitle:" Verify your licence role."},msg_error_ReplaceBylatestRevision:"Unable to replace the objects with their latest revision.",msg_error_MoveOrCopyAcrossSharedAndPersonal:"You cannot {move_copy} a {personal_shared} bookmark into a {shared_personal} bookmark.",msg_error_copylink:"You do not have permission to copy the link to the clipboard.",msg_error_createNew:{Title:"You cannot create this object: {type}.",Advice:"Verify that you are using the correct credentials.",Details:"You may not have the required responsibility in the target collaborative space to create this type of object."},msg_error_rest:{title:"An error occurred on server.",subtitle:"Contact your administrator for support."},msg_error_rest_GetActiveFolder:{title:"Unable to retrieve the default bookmark.",subtitle:"The bookmark is no longer accessible or might have been deleted.",message:"The bookmark has been unset as default bookmark."},msg_warning_replaceByRevision_sameObject:{title:"Select another revision.",subtitle:"You cannot replace an object's revision with the same revision."},msg_warning_replaceByLatestRevision_sameObject:"The bookmark already contains the latest revision of the selected objects.",msg_warning_notProductRootInFilteredContext:{title:"You cannot display this path.",subtitle:"No parent object is visible in the bookmark.",message:'Clear the "Hide bookmarked child products" preference.'},msg_warning_notProductRoot:"The object is no longer accessible, or might have been deleted.",msg_warning_replaceByLatestRevision_NotMorethan100Objects:"Select no more than 100 objects.",msg_warning_contentnotavailable:"The content is no longer accessible, or might have been deleted.",msg_warning_removeContentFromSearch:{title:"Cannot remove content.",advice:"Please select a sub-bookmark to refine the search results.",details:"Cannot remove content from more than 20 bookmarks."},CMDInsertNewFolder:"New Bookmark",CMDRemoveContent:"Remove Content",CMDAddFile:"Upload Document",CMDDownloadFile:"Download",CMDPreviewFile:"Preview",CMDCheckOutFile:"Check Out",CMDCheckInFile:"Check In",CMDEditFile:"Edit",CMDUndoEditFile:"Undo Edit",CMDFolderUpload:"Folder Upload",CMDUpdateFile:"Update",CMDRename:"Rename",CMDCut:"Cut",CMDCopy:"Copy",CMDPaste:"Paste",CMDDelete:"Delete",CMDClearSort:"Clear Sort",CMDOpenColumnCusto:"Tree List View Options",CMDAddExistingContent:"Add Existing Content",CMDExpandAll:"Expand All",CMDCollapseAll:"Collapse All",CMDAccessRightCmd:"Share",CMDAddToFavorites:"Add to Favorites",CMDProperties:"Properties",CMDSynchronize:"Synchronize to local disk",CMDActiveFolder:"Global Default Bookmark",CMDOpen:"Open",CMDOpenWithCV5:"Connect CATIA V5",CMDFindInCtx:"Find in Context",csv_base_file_name:"bookmark_editor_",alpha_sorting:"Alphabetical Order",date_sorting:"Date Order",fonticon_filter:"The panel content is currently filtered",download:"Download",upload:"Upload",CMDDeleteDefinitively:"Delete",CMDShareBookmarkLink:"Copy Link",CMDRestore:"Restore",CMDSizeColumnToFit:"Size Column to Fit",HeaderThumbnail:"Thumbnail",HeaderDeletionDate:"Deletion Date",HeaderDeleteBy:"Deleted By",SearchEnterMoreThan:"{minNumber} characters minimum are needed for a search.",ShowNavPane:"Display the navigation pane",HideNavPane:"Hide the navigation pane",relations_tab:"Relations",confirmation_ok:"OK",confirmation_cancel:"Cancel",confirmation_move_title:"Move content",confirmation_move_message:"Do you want to move the content of {source} to {target} ?",confirmation_move_message_monosel:"Do you want to move {selection} from {source} to {target} ?",confirmation_move_message_mono_from_tree:"Do you want to move {source} to {target} ?",confirmation_move_message_multisel:"Do you want to move {nth} items from {source} to {target} ?",confirmation_move_button:"Move",confirmation_remove_title:"Remove content",confirmation_remove_message:"Are you sure to remove content from Bookmark?",confirmation_copyMassive_title:"Create Copy of {source} in {target}",confirmation_copyMassive_message:"You are about to copy a large number of items.",confirmation_copyMassive_checkBox:"Keep bookmarked items in copied bookmark",confirmation_copy:"Copy",confirmation_info:"Attributes other than title and description are lost while converting \n {From} to {To}. All attributes are copied for child objects.",authentication_error:"You cannot use this app. Verify that you are using the correct credentials.",noPlatform_error:"This widget is not available on your platform.",placeholder_msg_folder:"This bookmark is currently empty.",placeholder_CMD_AddExisting:"Add Existing",placeholder_CMD_UploadFile:"Upload file",placeholder_CreateFolder:"Create Bookmark",tooltip_reserved:"Reserved",tooltip_unreserved:"Unreserved",tooltip_noDefaultLocation:"No current global default bookmark.",tooltip_defaultLocation:"The global default bookmark is {folderLabel}. Click to explore it.",tooltip_enableDefaultLocation:"Global default bookmark {folderLabel} is disabled. Click to enable.",tooltip_disableDefaultLocation:"Click to disable global default bookmark {folderLabel}.",BookmarkLocation:"Show in parent Bookmark",column_isLastRevision:"Is Last Revision",column_BookmarkLocation:"Parent Bookmark",column_Thumbnail:"Thumbnail",placeholder_msg_favorite:"No Favorite bookmark.",placeholder_msg_section:"No Bookmark Found.",placeholder_msg_search:"No results found.",refresh_ongoing:"Refreshing...",refresh_ok:"Refresh done",refreshError:"Unable to refresh the widget.",placeholder_findInCtx:"Search or Select object",findInCtxsearchview:{ftssearch:"Search",searchBtn:"Search",check:"Confirm Selection",cannotRetrieveLabel:"Cannot retrieve the label of the selected object",loadingLabel:"Loading..."},badCredentialChanged:'"{credential}" has been set as you don\'t have access to the preselected credentials.',credentialChanged:'"{credential}" has been set.',success:"Success",warning:"Warning",information:"Information",revisionReport:{reportTitle:"Report - Replace By Latest Revision",successfullyReplaced:"{prd1} has been replaced by {prd2}.",explicitlyRemoved:"{prd1} has been explicitly removed.",isLatestRevison:"{prd1} to be replaced is already a latest revision.",msgObjectSelected:"{object} object(s) were selected to be replaced by their latest revision.",msgObjectAlreadyLatestRevision:"{object} object(s) are already at their latest revision.",msgSuccessfullyReplaced:"{object} object(s) were successfully replaced by their latest revision.",msgObjectExplicitlyRemoved:"{object} object(s) were explicitly  removed as they had their latest revision already present in the bookmark or multiple selected object(s) had a common revision."},msg_advice_refreshWidget:"Refresh the widget or your browser, and verify that you are using the correct credentials.",msg_error_objectNoLongerAccessible:"This content is no longer accessible, or might have been deleted.",Attachments:"Attachments",Specification:"Specification Documents",OpenLocationRemoveTitle:"Remove content: {label}",OpenLocationRemoveMessage:"Do you want to remove this object from the following bookmarks?",CMDUnsetGlobalActiveFolder:"Unset Global Default Bookmark",CMDShowInBookmarks:"Show in bookmarks",CMDEnableGlobalActiveBookmark:"Enable",CMDDisableGlobalActiveBookmark:"Disable",indexMode:{Authoring:"Authoring.",Index:"Index.",Label:"Index",seconds:"second(s)",minutes:"minute(s)",hours:"hour(s)",msg_Authoring:"Authoring mode is enabled. Performance may be impacted.",activeMode:"Active mode : {mode}",lastRefresh:"Last Refresh : {timeElapsed} ago.",reactIndexMode:"Index mode will be reactivated in {autoSwitch} minutes from the time of authoring operation or refresh.",errorMessageBookmarkExpand:"Unable to expand the bookmarks.",errorMessageBookmarkRoot:"Unable to retrieve the root bookmarks.",info_msg_DataNotUptoDate:"Index mode is active. Data may not be up to date."},CMD_Yes:"Yes",CMD_No:"No",dialog_replaceByLatestRevision:"Replace by Revision",msg_warning_FolderNotVisisbleinTree:"Selected bookmark {name} is not visible in tree, scroll to view and select it.",msg_warning_DndNotSupported:"Drop is not supported in read-only mode.",ViewTooltip:{"ds6w:label":"Title","ds6wg:revision":"Revision","ds6w:modified":"Modification Date","ds6w:responsible":"Owner","ds6w:type":"Type","ds6w:status":"Maturity State","ds6w:project":"Collaborative Space","ds6w:identifier":"Name"},msg_warning_dndWindowsFoldernotsupported:"Drop contains Folder and is not supported",BookmarkDownload:{BookmarkDownloadLimit:"Bookmark Structure contains more than 1000 Documents; Please select Bookmark Structure containing less number of Documents",BookmarkNo_Documents:"Bookmark Structure doesn't contain any Document, nothing will be downloaded",BookmarkDupalicateDOC:"Bookmark Structure Download Failed as same hierachy already exisits for Document",DownloadStructureFailure:"Failed to Download the Bookmark Structure.",BookmarkDownloadStarted:"Bookmark Structure Download Started.",BookmarkDownloadMultiFileError:"Following Documents have multiple files in it. \n",BookmarkDownloadNoFileError:"\n Following Documents have no files in it. \n"},NoPathFound:"No valid paths found.",progressPanel_dragDrop:"Uploading {objectsAdded} items to {targetfolder}",progressPanel_add:"Adding {objectsAdded} items to {targetfolder}",progressPanel_move:"Moving {objectsAdded} items to {targetfolder}",progressPanel_copy:"Copying {objectsAdded} items to {targetfolder}",progressPanel_remove:"Removing {objectsAdded} items from {targetfolder}"});define("DS/FolderEditor/assets/nls/FolderFindInCtx",{findInCtxSearchView:{objects:"objects"},msg_warning_FindInCtx_objectsLimit:"There is a limit to the number of Physical Products in which find will be done, First {limit} Physical Products in the current bookmark will be considered for finding the object.",title_warning_FindInCtx_objectsLimit:"Find in context will be done on limited Physical Products.",msg_warning_FindInCtx_novalidobject:"There is no valid object in the present bookmark for performing find in context. Find in context panel will not be launched.",title_warning_FindInCtx_novalidobject:"Cannot perform find in context.",msg_warning_FindInCtx_allowProductExpand:"Cannot perform find in context as the preference setting 'Allow Products Expand' has been set to false.",subtitle_warning_FindInCtx_allowProductExpand:"Change the preference setting 'Allow Products Expand' to true.",msg_error_FindInCtx_noValidPhysicalObject:" There is no valid physical product in the present bookmark for performing find in context"});define("DS/FolderEditor/assets/nls/FolderFindInTree",{loader:{findingBookmarks:"Finding Bookmarks..."},msg_warning_FindInTree_NoLaunchOnFavourite:"Find in tree cannot be performed on favorite section and favorite bookmarks.",msg_warning_FindInTree_InvalidDrop:{Title:"Dropped object is not a bookmark.",Advice:"Please drop a root bookmark or bookmark."},msg_warning_expandTotheFindInNode:{Title:"Find in tree cannot be done.",Advice:"Expand the tree to that specific bookmark node",Details:"The bookmark to find-in is not visisble in the tree."},msg_warning_NoObjectsUnderSection:{Title:"Find in tree cannot be done.",Advice:"Expand the bookmark section to level one.",Details:"There are no visible bookmarks under the bookmark section."},msg_info_limit_FindInTree:"Limit is reached. Limit to find the bookmark is set to 100.",msg_info_limitToFindInRootNode:"Limit to find in root bookmarks is set to 200, first 200 root bookmarks will be considered.",msg_info_No_Occurrence:"No occurrences of the bookmark(s) have been found.",find_in_Tree_Tooltip:"Find in Tree"});define("DS/FolderEditor/assets/nls/ImportCSVBookmarkPanel",{OK:"Ok",Cancel:"Cancel",Cancel_Tooltip:"Cancel and Close this dialog.",Close:"Close",Close_Tooltip:"Close this dialog.",Change_Csv_File:"Change CSV file.",Drop_Csv_File:"Drop CSV file here.",Dialog_Title:"Import Bookmark Structure - ",Help_Csv_File:"Download the template file.",Label_Import:"Import",Label_Import_Tooltip:"Run the Import.",Label_LineNumber:"Line",Label_Column:"Column",Label_Column_Level:"Level",Label_Column_Name:"Name",Label_Column_Description:"Description",Label_Column_Owner:"Owner",Label_Column_SecurityContext:"SecurityContext",Label_Confirm_Title:"Warning - Large Structure",ConfirmMsg_Drop_File_Size_Large:"This operation might take a while due to the large size of the file. Do you want to continue?",ErrorMsg_Avoid_Char:"It has invalid characters.",ErrorMsg_Column_Count:"The number of columns specified is outside of the supported range of values.",ErrorMsg_Desc_Max_Over:"Maximum length for Description is 1024 characters.",ErrorMsg_Drop_Multiple_File:"Multiple files are forbidden.",ErrorMsg_Drop_File_Not_CSV:"Not a CSV file.",ErrorMsg_Drop_File_Empty:"Dropped file is empty.",ErrorMsg_Exist_Folder_Created:"This bookmark has already been created. Please refresh the Bookmark Editor widget.",ErrorMsg_Exist_Folder_Modified:"This bookmark has already been updated. Please refresh the Bookmark Editor widget.",ErrorMsg_Folder_Create_Stoped:"Bookmark creation was stopped.",ErrorMsg_Level_Is_Not_Number:"Level is blank or has invalid characters (the level for following lines won't be checked).",ErrorMsg_Level_Is_Not_Sequencial:"Level has to be a sequencial number.",ErrorMsg_Level_Origin:"Level has less number than 1.",ErrorMsg_Name_Duplicate:"The same Bookmark name is already in the same parent Bookmark.",ErrorMsg_Name_Required:"Name cannot be blank.",ErrorMsg_Name_Max_Over:"Maximum length for Name is 127 characters.",ErrorMsg_SecCtx_Is_Invalid:"Security Context is invalid.",ErrorMsg_SecCtx_Does_Not_Exist:"Security Context does not exist.",ErrorMsg_Owner_Is_Not_Ascii:"Owner has invalid characters.",ErrorMsg_Owner_Does_Not_Exist:"Owner does not exist.",ErrorMsg_Owner_Change_Failure:"Owner cannot be changed.",ErrorMsg_Bookmark_Create_Stoped_SetProperties:"Bookmark creation was stopped. Unable to set expected properties (name, description or owner)",ErrorMsg_Rest_Error:"Server communication Error.",InfoMsg_Load_Csv_Success:"CSV file loaded successfully.",InfoMsg_Load_Csv_Error:"CSV file has invalid lines.",InfoMsg_Create_Target_Size_Zero:"The whole structure already exists.",InfoMsg_Folder_Creating:"Creating Bookmark...",InfoMsg_Folder_Create_Success:"Bookmark(s) has been created successfully.",WarnMsg_SecurityContext_MissMatch:"Security Contexts don't match (selected Bookmark SC & current SC)"});define("DS/FolderEditor/assets/nls/PADSlidder",{openSlidder:"Display the Bookmark tree",closeSlidder:"Hide the Bookmark tree"});