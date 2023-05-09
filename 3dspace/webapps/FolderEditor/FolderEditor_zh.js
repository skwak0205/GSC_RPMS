define("DS/FolderEditor/FolderEditor_zh",{});define("DS/FolderEditor/assets/nls/AddCommentPopup",{SelectFile_PlaceHolder:"选择文件...",SelectFile_Button:"浏览",PanelTitle:"检入",Comment_Title:"备注",Comment_PlaceHolder:"输入签入原因...",OK:"检入",Cancel:"取消"});define("DS/FolderEditor/assets/nls/AddMovePopup",{operationToPerform:"对这些对象执行哪种操作？",msg_Title:"{number} 个对象已被放置到书签“{folderName}”。",Add:"添加",Move:"移动"});define("DS/FolderEditor/assets/nls/BookmarkLocationPopup",{OK:"确定",Cancel:"取消",OpenLocation:"父书签",Bookmark_Locations:"书签位置",Open:"打开",OpenLocationPopupLable:"选择要打开的书签：",msg_error_BookmarkLocation:"无法打开书签“{name1}”的位置（可能已删除或无法访问）。",msg_error_BM_LOC_InValid_Object:"无法打开选定位置的对象，对象可能已被删除或无法访问。",removeFromSearchNotif:{success:"已移除对象。",failNotAllowed:"验证书签是否处于允许更改内容的状态。",failNoRelationFound:"验证您是否具有足够的权限。"}});define("DS/FolderEditor/assets/nls/FolderEditor",{name_Section_FavoriteFolders:"收藏夹",name_Section_AllFolders:"书签",name_Section_Trash:"已删除",name_Section_ParentFolders:"{number} 个父书签",name_Widget_Title:"Bookmark Editor",name_Widget_Help:"Bookmark Editor 帮助",name_RootFolder_type:"书签根",name_Folder_type:"书签",folder_pref_dnd:"拖放行为",folder_pref_filter:"隐藏带书签的子产品",folder_pref_allow_products_expand:"启用产品扩展",folder_pref_subfolder:"在内容视图中显示子书签",folder_pref_populateTags:"启用 6W 标记以过滤书签",folder_pref_activateRelationsColumns:"显示相关",useIndexAcceleration_label:"显示索引数据",folder_pref_autoSwitch_Label:"自动从创作模式切换到索引模式",button_remove:"移除",button_close:"关闭",Move_label:"移动",Copy_label:"复制",oneMin_Label:"1 分钟",threeMin_Label:"3 分钟",fiveMin_Label:"5 分钟",tenMin_Label:"10 分钟",Never_Label:"从不",Upload_content_loading:"上传内容",AddExisting_content_loading:"添加现有内容",Paste_content_loading:"粘贴内容",Remove_content_loading:"移除内容",Replace_revision_loading:"将内容替换为最新修订版",Content_Loading:"正在加载内容...",Widget_Loading:"正在加载构件...",Tree_Loading:"正在加载树...",Bookmark_Loading:"正在选择书签...",SetActiveBookmark_Loading:"正在设置新全局默认书签...",UnsetActiveBookmark_Loading:"正在取消设置全局默认书签...",EnableActiveBookmark_Loading:"正在启用全局默认书签...",DisableActiveBookmark_Loading:"正在禁用全局默认书签...",Shared_label:"已共享",Personal_label:"个人",label_ActionBar_AddToFavorites:"添加/移除收藏夹",label_ActionBar_RemoveFromFavorites:"从收藏夹中移除",msg_success_copyLink_content:"书签“{foldername}”内文档“{filename}”的链接已复制。",msg_success_copyLink_folder:"书签“{foldername}”的链接已复制。",msg_success_favoriteReordered:"已成功重新排序。",msg_info_contentcopied:"已复制内容。",msg_info_contentcut:"已剪切内容。",msg_info_foldercut:"已剪切书签。",msg_info_objetremoved:"已将 {number} 个对象从书签“{foldername}”中移除。",msg_info_objectremovedfromfolders:"对象“{objectname}”已成功从 {number} 个书签中移除。",msg_info_Addinfolder:"{objectsAdded} 个对象已添加到书签 {targetfolder}，{objectsAlreadyPresent} 个要添加的对象已存在。",msg_info_Addinfolder_1:"已将 {objectsAdded} 个对象添加到书签 {targetfolder}。",msg_info_objetadded:"已将 {filename} 添加到“{foldername}”中。",msg_info_objetmoved:"{objectsMoved} 个对象已移动到书签 {targetfolder}，{objectsAlreadyPresent} 个要移动的对象已存在。",msg_info_objetmoved_1:"已将 {objectsMoved} 个对象移动到书签 {targetfolder}。",msg_info_foldercopied:"已将 {number} 个对象添加到书签“{foldername}”中。",msg_info_foldermoved:"已将 {number} 个书签移动到书签“{foldername}”中。",msg_info_foldercreated:"已创建书签。",msg_info_filecheckedout:"已成功签出文件。",msg_info_filecheckedin:"已成功签入文件。",msg_info_filecancelcheckout:"文件编辑操作已成功取消。",msg_info_favoriteadded:"已将书签“{name1}”添加到收藏夹中。",msg_info_favoriteremoved:"已从收藏夹中移除书签“{name1}”。",msg_info_favoriterenamed:"已将收藏夹“{name1}”重命名为“{name2}”。",msg_info_rename:"已将书签“{name1}”重命名为“{name2}”。",msg_info_refresh:"刷新已完成。",msg_info_noResultSearch:"未找到结果。",msg_info_SortNonAvailable:"此列在搜索过程中不可用于排序。",msg_info_replaceByRevision:"对象已成功替换。",msg_info_replaceByLatestRevision:"已成功执行“替换为最新修订版”",msg_info_replaceByLatestRevision_removed:"已显式移除 {number} 个对象，因为其最新修订版已存在于书签中，或者多个选定对象具有通用修订版。",msg_info_treeFilterRemove:"添加根书签后，树过滤器将被移除",msg_info_active_successfully:"{folderLabel} 已成功设置为全局默认书签。",msg_info_inactive_successfully:"全局默认书签 {folderLabel} 已成功取消设置。",msg_info_enable_successfully:"全局默认书签 {folderLabel} 现已启用。",msg_info_disable_successfully:"全局默认书签 {folderLabel} 现已禁用。",msg_info_defaultLocation:"全局默认书签为 {folderLabel}。",msg_warning_MultipleSelection:"不支持多选。",msg_warning_nocontent:"未选择任何内容：请选择内容。",msg_warning_nofoldernocontent:"未选择任何项目：请选择内容或书签。",msg_warning_nocurrentfolder:"当前未选择任何书签：请选择书签。",msg_warning_noselection:"必须选择至少一个对象。",msg_warning_dndnotsupported:"不支持此放置操作。",msg_warning_dndnotsupported_external_on_Section_FavoriteFolders:"系统不接受将外部书签拖放到收藏夹部分。",msg_warning_dndnotsupported_from_different_tenant:"不能接受从不同的 3DEXPERIENCE Platform 放置对象。",msg_warning_dndnotsupported_from_different_source:"不支持此放置操作。请从协作区中放置对象。",msg_warning_addnotsupported_from_different_tenant:"不能接受添加来自不同 3DEXPERIENCE Platform 的对象。",msg_warning_webInWinDnD:"只有 3DDashboard 接受对象拖放。",msg_warning_FolderAlreadyAmongFavorites:"书签“{foldername}”已经在收藏夹中。",msg_warning_expandSaveLimitation:{Title:"最后一个展开状态将不会保存。",Advice:"折叠某些书签以能够保存展开状态。",Details:"展开的书签太多。"},msg_warning_copyLink:"选定对象不是书签。",msg_warning_LimitInCtxSearchOnAllBookmarks:"当根书签数超过 200 时，上下文搜索不受支持。",msg_error_contentNotFound:"此内容不再可访问，或者可能已被删除。",msg_error_OnRefreshCmdfailure:"网络错误：操作已中断。",msg_error_preferencessnotsaved:"无法保存您的首选项。",msg_error_nopreferences:"无法检索您的首选项。",msg_error_Addinfolder:"您不能将内容添加到此书签。",msg_error_CopyFolder:"无法复制书签。",msg_error_MoveFolder:"无法移动书签。",msg_error_MoveFolder_parent:"目标书签是要移动的书签的父项之一。",msg_error_CutPasteToRoot:"您不能将书签移动到根书签。",msg_error_Removefromfolder:"无法从书签中移除对象。",msg_error_MoveInfolder:"无法移动对象。",msg_error_MoveInSamefolder:{Title:"选择不同的书签。",Advice:"源书签和目标书签相同。"},msg_error_rename:{Title:"不能重命名书签：{name}。",Advice:"验证名称是否尚未使用或您使用的凭据是否正确。",Details:"在目标合作区中，您可能没有重命名此对象所需的责任。"},msg_error_name_maxlength:"输入一个介于 1 和 {maxLength} 个字符之间的新名称。",msg_error_name_empty:"输入名称。",msg_error_FolderActivate:{Title:"无法激活 {name1}。",Advice:"书签不再可访问，或者可能已被删除。"},msg_error_NoVisibleRootFolder:{title:"您没有打开书签所需的权限。",subtitle:"父书签可能未共享，或者处于不允许您查看的成熟度状态。"},msg_error_TreeExpandCancelledNotReady:{title:"无法展开树。",subtitle:"刷新小组件。"},msg_error_forbiddenChar:{message:"列名称包含无效字符：{badChars}"},msg_error_contentnotavailable:{Title:"此书签不再可访问，或者可能已被删除。 ",Advice:"刷新小组件。"},msg_error_contentnotavailableInFavorite:{Title:"书签不再可访问，或者可能已被删除。",Advice:"从收藏夹中移除书签。"},msg_InfoAction_Timeout:"操作所需的时间比预期的要长。它将在后台继续运行。请刷新以更新数据。",msg_error_timeout:{title:"无法检索书签的内容。",subtitle:"与服务器的连接超时。",message:"验证您的连接，或联系您的管理员以获得支持。"},msg_error_folderPath:"书签不再可访问，或者可能已被删除。",msg_error_folderPath_roles:"此对象不再可访问，或者可能已被删除。",msg_error_appWhereUsedNotFound:{title:"您无法打开 Relations 应用程序。",subtitle:" 验证您的许可证角色。"},msg_error_ReplaceBylatestRevision:"无法将对象替换为其最新修订版。",msg_error_MoveOrCopyAcrossSharedAndPersonal:"您无法将 {personal_shared} 书签 {move_copy} 到 {shared_personal} 书签。",msg_error_copylink:"您无权将链接复制到剪贴板。",msg_error_createNew:{Title:"您不能创建此对象：{type}。",Advice:"验证您使用的凭据是否正确。",Details:"在目标协作区中，您可能没有创建此类型的对象所需的责任。"},msg_error_rest:{title:"服务器出错。",subtitle:"请与管理员联系，寻求支持。"},msg_error_rest_GetActiveFolder:{title:"无法检索默认书签。",subtitle:"书签不再可访问，或可能已被删除。",message:"书签已被取消设置为默认书签。"},msg_warning_replaceByRevision_sameObject:{title:"选择另一个修订版。",subtitle:"您不能将对象的修订版替换为同一修订版。"},msg_warning_replaceByLatestRevision_sameObject:"书签已包含所选对象的最新修订版。",msg_warning_notProductRootInFilteredContext:{title:"您不能显示此路径。",subtitle:"书签中没有可见的父对象。",message:"清除“隐藏带书签的子产品”首选项。"},msg_warning_notProductRoot:"对象不再可访问，或者可能已被删除。",msg_warning_replaceByLatestRevision_NotMorethan100Objects:"选择不超过 100 个的对象。",msg_warning_contentnotavailable:"此内容不再可访问，或者可能已被删除。",msg_warning_removeContentFromSearch:{title:"无法移除内容。",advice:"请选择子书签以优化搜索结果。",details:"无法从 20 个以上的书签中移除内容。"},CMDInsertNewFolder:"新建书签",CMDRemoveContent:"移除内容",CMDAddFile:"上传文档",CMDDownloadFile:"下载",CMDPreviewFile:"预览",CMDCheckOutFile:"签出",CMDCheckInFile:"签入",CMDEditFile:"编辑",CMDUndoEditFile:"撤消编辑",CMDFolderUpload:"上传文件夹",CMDUpdateFile:"更新",CMDRename:"重命名",CMDCut:"剪切",CMDCopy:"复制",CMDPaste:"粘贴",CMDDelete:"删除",CMDClearSort:"清除排序",CMDOpenColumnCusto:"树列表视图选项",CMDAddExistingContent:"添加现有内容",CMDExpandAll:"全部展开",CMDCollapseAll:"全部折叠",CMDAccessRightCmd:"共享",CMDAddToFavorites:"添加到收藏夹",CMDProperties:"特性",CMDSynchronize:"同步到本地磁盘",CMDActiveFolder:"全局默认书签",CMDOpen:"打开",CMDOpenWithCV5:"连接 CATIA V5",CMDFindInCtx:"在上下文中查找",csv_base_file_name:"书签_编辑器_",alpha_sorting:"字母顺序",date_sorting:"日期顺序",fonticon_filter:"该面板内容当前已被过滤",download:"下载",upload:"上传",CMDDeleteDefinitively:"删除",CMDShareBookmarkLink:"复制链接",CMDRestore:"恢复",CMDSizeColumnToFit:"使当前列适屏显示",HeaderThumbnail:"缩略图",HeaderDeletionDate:"删除日期",HeaderDeleteBy:"删除者",SearchEnterMoreThan:"搜索至少需要有 {minNumber} 个字符。",ShowNavPane:"显示导航窗格",HideNavPane:"隐藏导航窗格",relations_tab:"关系",confirmation_ok:"确定",confirmation_cancel:"取消",confirmation_move_title:"移动内容",confirmation_move_message:"是否要将 {source} 的内容移到 {target}？",confirmation_move_message_monosel:"是否要将 {selection} 从 {source} 移至 {target}？",confirmation_move_message_mono_from_tree:"是否要将 {source} 移至 {target}？",confirmation_move_message_multisel:"是否要将 {nth} 项目从 {source} 移至 {target}？",confirmation_move_button:"移动",confirmation_remove_title:"移除内容",confirmation_remove_message:"是否确定要从书签中移除内容？",confirmation_copyMassive_title:"在 {target} 中创建 {source} 的副本",confirmation_copyMassive_message:"您即将复制大量项目。",confirmation_copyMassive_checkBox:"将已添加书签的项目保留在复制的书签中",confirmation_copy:"复制",confirmation_info:"将 \n {From} 转换为 {To} 时，除标题和描述以外的属性将丢失。将复制子对象的所有属性。",authentication_error:"您不能使用此应用程序。请验证您使用的凭据是否正确。",noPlatform_error:"此小组件在您的平台上不可用。",placeholder_msg_folder:"此书签当前为空。",placeholder_CMD_AddExisting:"添加现有",placeholder_CMD_UploadFile:"上传文件",placeholder_CreateFolder:"创建书签",tooltip_reserved:"已保留",tooltip_unreserved:"未保留",tooltip_noDefaultLocation:"无当前全局默认书签。",tooltip_defaultLocation:"全局默认书签为 {folderLabel}。单击可进行浏览。",tooltip_enableDefaultLocation:"全局默认书签 {folderLabel} 已禁用。单击可启用。",tooltip_disableDefaultLocation:"单击可禁用全局默认书签 {folderLabel}。",BookmarkLocation:"在父书签中显示",column_isLastRevision:"是最新修订版",column_BookmarkLocation:"父书签",column_Thumbnail:"缩略图",placeholder_msg_favorite:"无收藏夹书签。",placeholder_msg_section:"未找到书签。",placeholder_msg_search:"未找到结果。",refresh_ongoing:"正在刷新...",refresh_ok:"刷新已完成",refreshError:"无法刷新小组件。",placeholder_findInCtx:"搜索或选择对象",findInCtxsearchview:{ftssearch:"搜索",searchBtn:"搜索",check:"确认选择",cannotRetrieveLabel:"无法检索选定对象的标签",loadingLabel:"正在加载..."},badCredentialChanged:"“{credential}”已设置，因为您没有预选凭据的访问权限。",credentialChanged:"“{credential}”已设置。",success:"成功",warning:"警告",information:"信息",revisionReport:{reportTitle:"报告 - 替换为最新修订版",successfullyReplaced:"{prd1} 已被替换为 {prd2}。",explicitlyRemoved:"已显式移除 {prd1}。",isLatestRevison:"要替换的 {prd1} 已是最新修订版。",msgObjectSelected:"已选择将 {object} 个对象替换为其最新修订版。",msgObjectAlreadyLatestRevision:"{object} 个对象已处于其最新修订版状态。",msgSuccessfullyReplaced:"已成功将 {object} 个对象替换为其最新修订版。",msgObjectExplicitlyRemoved:"已显式移除 {object} 个对象，因为其最新修订版已存在于书签中，或者多个选定对象具有通用修订版。"},msg_advice_refreshWidget:"刷新小组件或浏览器，并验证您使用的凭据是否正确。",msg_error_objectNoLongerAccessible:"此内容不再可访问，或者可能已被删除。",Attachments:"附件",Specification:"规格文档",OpenLocationRemoveTitle:"移除内容：{label}",OpenLocationRemoveMessage:"是否要从以下书签中移除此对象？",CMDUnsetGlobalActiveFolder:"取消设置全局默认书签",CMDShowInBookmarks:"在书签中显示",CMDEnableGlobalActiveBookmark:"启用",CMDDisableGlobalActiveBookmark:"禁用",indexMode:{Authoring:"创作",Index:"索引。",Label:"索引",seconds:"秒",minutes:"分钟",hours:"小时",msg_Authoring:"已启用创作模式。性能可能会受到影响。",activeMode:"活动模式：{mode}",lastRefresh:"上次刷新：{timeElapsed} 前。",reactIndexMode:"从创作操作或刷新时间算起，索引模式将在 {autoSwitch} 分钟内重新激活。",errorMessageBookmarkExpand:"无法展开书签。",errorMessageBookmarkRoot:"无法检索根书签。",info_msg_DataNotUptoDate:"索引模式处于活动状态。数据可能不是最新的。"},CMD_Yes:"是",CMD_No:"否",dialog_replaceByLatestRevision:"替换为修订版",msg_warning_FolderNotVisisbleinTree:"所选书签 {name} 在树中不可见，请滚动查看并选择它。",msg_warning_DndNotSupported:"只读模式下不支持拖放。",ViewTooltip:{"ds6w:label":"标题","ds6wg:revision":"修订版","ds6w:modified":"修改日期","ds6w:responsible":"所有者","ds6w:type":"类型","ds6w:status":"成熟度状态","ds6w:project":"合作区","ds6w:identifier":"名称"},msg_warning_dndWindowsFoldernotsupported:"拖放包含文件夹，因此不受支持",BookmarkDownload:{BookmarkDownloadLimit:"书签结构包含 1000 多个文档；请选择包含较少文档的书签结构",BookmarkNo_Documents:"书签结构不包含任何文档，不会下载任何内容",BookmarkDupalicateDOC:"书签结构下载失败，因为文档已存在相同的层次结构",DownloadStructureFailure:"无法下载书签结构。",BookmarkDownloadStarted:"书签结构下载已开始。"},NoPathFound:"未找到有效路径。",progressPanel_dragDrop:"正在将 {objectsAdded} 项目上传到{targetfolder}",progressPanel_add:"正在将 {objectsAdded} 项目添加到 {targetfolder}",progressPanel_move:"正在将 {objectsAdded} 项目移动到 {targetfolder}",progressPanel_copy:"正在将 {objectsAdded} 项目复制到 {targetfolder}",progressPanel_remove:"正在从 {targetfolder} 中移除 {objectsAdded} 项目"});define("DS/FolderEditor/assets/nls/FolderFindInCtx",{findInCtxSearchView:{objects:"对象"},msg_warning_FindInCtx_objectsLimit:"将在其中进行查找的物理产品数量有限制，将考虑当前书签中的前 {limit} 个物理产品以查找对象。",title_warning_FindInCtx_objectsLimit:"在上下文中查找将在有限的物理产品上完成。",msg_warning_FindInCtx_novalidobject:"当前书签中没有用于在上下文中执行查找的有效对象。将不会启动“在上下文中查找”面板。",title_warning_FindInCtx_novalidobject:"无法在上下文中执行查找。",msg_warning_FindInCtx_allowProductExpand:"无法在上下文中执行查找，因为首选项设置“允许产品展开”已设置为 false。",subtitle_warning_FindInCtx_allowProductExpand:"将首选项设置“允许产品展开”更改为 true。",msg_error_FindInCtx_noValidPhysicalObject:" 当前书签中没有有效的物理产品可用于在上下文中执行查找"});define("DS/FolderEditor/assets/nls/FolderFindInTree",{loader:{findingBookmarks:"查找书签..."},msg_warning_FindInTree_NoLaunchOnFavourite:"不能在收藏夹部分和收藏夹书签上执行在树中查找。",msg_warning_FindInTree_InvalidDrop:{Title:"放置的对象不是书签。",Advice:"请放置根书签或书签。"},msg_warning_expandTotheFindInNode:{Title:"无法在树中查找。",Advice:"将树扩展到该特定书签节点",Details:"要查找的书签在树中不可见。"},msg_warning_NoObjectsUnderSection:{Title:"无法在树中查找。",Advice:"将书签部分展开至第一级。",Details:"书签部分下没有可见的书签。"},msg_info_limit_FindInTree:"已达到限制。书签查找限制设置为 100。",msg_info_limitToFindInRootNode:"在根书签中查找的限制设置为 200，将考虑前 200 个根书签。",msg_info_No_Occurrence:"未找到书签的匹配项。",find_in_Tree_Tooltip:"Find in Tree"});define("DS/FolderEditor/assets/nls/ImportCSVBookmarkPanel",{OK:"确定",Cancel:"取消",Cancel_Tooltip:"取消并关闭此对话框。",Close:"关闭",Close_Tooltip:"关闭此对话框。",Change_Csv_File:"更改 CSV 文件。",Drop_Csv_File:"将 CSV 文件放置到此处。",Dialog_Title:"导入书签结构 - ",Help_Csv_File:"下载模板文件。",Label_Import:"导入",Label_Import_Tooltip:"运行导入。",Label_LineNumber:"线",Label_Column:"列",Label_Column_Level:"级别",Label_Column_Name:"名称",Label_Column_Description:"描述",Label_Column_Owner:"所有者",Label_Column_SecurityContext:"安全上下文",Label_Confirm_Title:"警告 - 大结构",ConfirmMsg_Drop_File_Size_Large:"由于文件较大，此操作可能需要花费一些时间。是否要继续？",ErrorMsg_Avoid_Char:"它包含无效字符。",ErrorMsg_Column_Count:"指定的列数超出所支持的值范围。",ErrorMsg_Desc_Max_Over:"描述的最大长度为 1024 个字符。",ErrorMsg_Drop_Multiple_File:"禁止选择多个文件。",ErrorMsg_Drop_File_Not_CSV:"并非 CSV 文件。",ErrorMsg_Drop_File_Empty:"放置的文件为空。",ErrorMsg_Exist_Folder_Created:"该书签已创建。请刷新书签编辑器 Widget。",ErrorMsg_Exist_Folder_Modified:"该书签已更新。请刷新书签编辑器 Widget。",ErrorMsg_Folder_Create_Stoped:"书签创建已停止。",ErrorMsg_Level_Is_Not_Number:"级别为空或包含无效字符。（以下行的级别将不被选中）。",ErrorMsg_Level_Is_Not_Sequencial:"级别必须是顺序号。",ErrorMsg_Level_Origin:"级别小于 1。",ErrorMsg_Name_Duplicate:"相同的书签名称已存在于同一父书签中。",ErrorMsg_Name_Required:"名称不能为空。",ErrorMsg_Name_Max_Over:"名称的最大长度为 127 个字符。",ErrorMsg_SecCtx_Is_Invalid:"安全上下文无效。",ErrorMsg_SecCtx_Does_Not_Exist:"安全上下文不存在。",ErrorMsg_Owner_Is_Not_Ascii:"所有者包含无效字符。",ErrorMsg_Owner_Does_Not_Exist:"所有者不存在。",ErrorMsg_Owner_Change_Failure:"所有者不能更改。",ErrorMsg_Bookmark_Create_Stoped_SetProperties:"书签创建已停止。无法设置预期属性（名称、描述或所有者）。",ErrorMsg_Rest_Error:"服务器通信错误。",InfoMsg_Load_Csv_Success:"CSV 文件加载成功。",InfoMsg_Load_Csv_Error:"CSV 文件包含无效行。",InfoMsg_Create_Target_Size_Zero:"整个结构都已存在。",InfoMsg_Folder_Creating:"正在创建书签...",InfoMsg_Folder_Create_Success:"已成功创建书签。",WarnMsg_SecurityContext_MissMatch:"安全上下文不匹配（已选定的书签 SC 和当前 SC）"});define("DS/FolderEditor/assets/nls/PADSlidder",{openSlidder:"显示书签树",closeSlidder:"隐藏书签树"});