define("DS/FolderEditor/FolderEditor_cs",{});define("DS/FolderEditor/assets/nls/AddCommentPopup",{SelectFile_PlaceHolder:"Vyberte soubor...",SelectFile_Button:"Procházet",PanelTitle:"Odevzdat",Comment_Title:"Komentář",Comment_PlaceHolder:"Zadejte důvod odevzdání...",OK:"Odevzdat",Cancel:"Storno"});define("DS/FolderEditor/assets/nls/AddMovePopup",{operationToPerform:"Kterou operaci je třeba provést s těmito objekty?",msg_Title:"{number} objektů bylo přetaženo do záložky „{folderName}“.",Add:"Přidat",Move:"Přesunout"});define("DS/FolderEditor/assets/nls/BookmarkLocationPopup",{OK:"OK",Cancel:"Storno",OpenLocation:"Nadřazená záložka",Bookmark_Locations:"Umístění záložek",Open:"Otevřít",OpenLocationPopupLable:"Vyberte záložku, kterou chcete otevřít:",msg_error_BookmarkLocation:"Nelze otevřít umístění pro záložku „{name1}“ (může být odstraněno nebo není přístupné).",msg_error_BM_LOC_InValid_Object:"Nelze otevřít vybraný objekt umístění. Objekt mohl být odstraněn nebo není přístupný.",removeFromSearchNotif:{success:"Objekt byl odebrán.",failNotAllowed:"Zkontrolujte, zda je záložka ve stavu, který umožňuje změnu obsahu.",failNoRelationFound:"Ověřte, zda máte dostatečné oprávnění."}});define("DS/FolderEditor/assets/nls/FolderEditor",{name_Section_FavoriteFolders:"Oblíbené",name_Section_AllFolders:"Záložky",name_Section_Trash:"Odstraněno",name_Section_ParentFolders:"Nadřízené záložky ({number})",name_Widget_Title:"Bookmark Editor",name_Widget_Help:"Nápověda editoru záložek Bookmark Editor",name_RootFolder_type:"Kořen záložky",name_Folder_type:"Záložka",folder_pref_dnd:"Chování při přetažení",folder_pref_filter:"Skrýt podřízené výrobky označené záložkou",folder_pref_allow_products_expand:"Povolit rozšíření výrobků",folder_pref_subfolder:"Zobrazit podřízené záložky v zobrazení obsahu",folder_pref_populateTags:"Povolit tagy 6W pro filtrování záložek",folder_pref_activateRelationsColumns:"Zobrazit související",useIndexAcceleration_label:"Zobrazit indexovaná data",folder_pref_autoSwitch_Label:"Automatické přepnutí z režimu vytváření do režimu indexu",button_remove:"Odebrat",button_close:"Zavřít",Move_label:"Přesunout",Copy_label:"Kopírovat",oneMin_Label:"1 minuta",threeMin_Label:"3 minuty",fiveMin_Label:"5 minut",tenMin_Label:"10 minut",Never_Label:"Nikdy",Upload_content_loading:"Nahrát obsah",AddExisting_content_loading:"Přidat stávající obsah",Paste_content_loading:"Vložit obsah",Remove_content_loading:"Odebrat obsah",Replace_revision_loading:"Nahrazování obsahu nejnovější opravou",Content_Loading:"Načítání obsahu...",Widget_Loading:"Načítání pomůcky...",Tree_Loading:"Načítání stromu...",Bookmark_Loading:"Vybírání záložky...",SetActiveBookmark_Loading:"Nastavení nové globální výchozí záložky...",UnsetActiveBookmark_Loading:"Rušení nastavení globální výchozí záložky...",EnableActiveBookmark_Loading:"Aktivace globální výchozí záložky...",DisableActiveBookmark_Loading:"Deaktivace globální výchozí záložky...",Shared_label:"Sdíleno",Personal_label:"Osobní",label_ActionBar_AddToFavorites:"Přidat/odebrat oblíbené",label_ActionBar_RemoveFromFavorites:"Odebrat z oblíbených",msg_success_copyLink_content:"Odkaz na dokument „{filename}“ uvnitř záložky „{foldername}“ byl zkopírován.",msg_success_copyLink_folder:"Odkaz na záložku „{foldername}“ byl zkopírován.",msg_success_favoriteReordered:"Změna pořadí proběhla úspěšně.",msg_info_contentcopied:"Obsah byl zkopírován.",msg_info_contentcut:"Obsah byl vyjmut.",msg_info_foldercut:"Záložka byla vyjmuta.",msg_info_objetremoved:"Bylo odebráno {number} objektů ze složky záložek „{foldername}“.",msg_info_objectremovedfromfolders:"Objekt „{objectname}“ byl úspěšně odebrán ze {number} záložek.",msg_info_Addinfolder:"Bylo přidáno {objectsAdded} objektů do složky záložek {targetfolder}, {objectsAlreadyPresent} objektů, které měly být přidány, již bylo přítomno.",msg_info_Addinfolder_1:"Bylo přidáno {objectsAdded} objektů do složky záložek {targetfolder}.",msg_info_objetadded:"Soubor „{filename}“ přidán do složky záložek „{foldername}“.",msg_info_objetmoved:"Bylo přesunuto {objectsMoved} objektů do složky záložek {targetfolder}, {objectsAlreadyPresent} objektů, které měly být přesunuty, již bylo přítomno.",msg_info_objetmoved_1:"Bylo přesunuto {objectsMoved} objektů do složky záložek {targetfolder}.",msg_info_foldercopied:"Bylo přidáno {number} záložek do složky záložek „{foldername}“.",msg_info_foldermoved:"Bylo přesunuto {number} záložek do složky záložek „{foldername}“.",msg_info_foldercreated:"Záložka byla vytvořena.",msg_info_filecheckedout:"Soubor byl úspěšně vyzvednut.",msg_info_filecheckedin:"Soubor byl úspěšně odevzdán.",msg_info_filecancelcheckout:"Operace úpravy souboru byla úspěšně zrušena.",msg_info_favoriteadded:"Záložka „{name1}“ byla přidána do oblíbených.",msg_info_favoriteremoved:"Záložka „{name1}“ byla odebrána z oblíbených.",msg_info_favoriterenamed:"Oblíbená položka „{name1}“ byla přejmenována na „{name2}“.",msg_info_rename:"Záložka „{name1}“ je přejmenována na „{name2}“.",msg_info_refresh:"Aktualizace provedena.",msg_info_noResultSearch:"Nebyl nalezen žádný výsledek.",msg_info_SortNonAvailable:"Tento sloupec není k dispozici pro třídění během vyhledávání.",msg_info_replaceByRevision:"Objekt byl úspěšně nahrazen.",msg_info_replaceByLatestRevision:"Nahrazení poslední verzí bylo úspěšně provedeno",msg_info_replaceByLatestRevision_removed:"Tento počet: {number} objektů byl explicitně odebrán, protože jejich poslední verze již byla přítomna v záložce nebo mělo několik vybraných objektů společnou verzi.",msg_info_treeFilterRemove:"Filtry stromu se odstraní po přidání kořenové záložky",msg_info_active_successfully:"{folderLabel} byla úspěšně nastavena jako globální výchozí záložka.",msg_info_inactive_successfully:"Globální výchozí záložka {folderLabel} byla úspěšně zrušena.",msg_info_enable_successfully:"Globální výchozí záložka {folderLabel} je nyní aktivována.",msg_info_disable_successfully:"Globální výchozí záložka {folderLabel} je nyní deaktivována.",msg_info_defaultLocation:"Globální výchozí záložkou je {folderLabel}.",msg_warning_MultipleSelection:"Vícenásobný výběr není podporován.",msg_warning_nocontent:"Nebyl vybrán žádný obsah: Vyberte obsah.",msg_warning_nofoldernocontent:"Nic není vybráno: Vyberte obsah nebo záložku.",msg_warning_nocurrentfolder:"Žádná aktuální záložka: Vyberte záložku.",msg_warning_noselection:"Musí být vybrán alespoň jeden objekt.",msg_warning_dndnotsupported:"Tato operace přetažení není podporována.",msg_warning_dndnotsupported_external_on_Section_FavoriteFolders:"Přetažení externích záložek v části Oblíbené není akceptováno.",msg_warning_dndnotsupported_from_different_tenant:"Přetažení objektů z jiné platformy 3DEXPERIENCE Platform není akceptováno.",msg_warning_dndnotsupported_from_different_source:"Tato operace přetažení není podporována. Přetáhněte objekty z prostoru spolupráce.",msg_warning_addnotsupported_from_different_tenant:"Přidání objektů z jiné platformy 3DEXPERIENCE Platform není akceptováno.",msg_warning_webInWinDnD:"Přetažení objektů je akceptováno pouze na 3DDashboard.",msg_warning_FolderAlreadyAmongFavorites:"Záložka „{foldername}“ již patří mezi oblíbené.",msg_warning_expandSaveLimitation:{Title:"Stav posledního rozbalení nebude uložen.",Advice:"Sbalte některé záložky, aby bylo možné ukládat stav rozbalení.",Details:"Je rozbaleno příliš mnoho záložek."},msg_warning_copyLink:"Vybraný objekt není záložka.",msg_warning_LimitInCtxSearchOnAllBookmarks:"V kontextu hledání není podporováno, pokud je počet kořenových záložek větší než 200.",msg_error_contentNotFound:"Obsah již není přístupný nebo byl možná odstraněn.",msg_error_OnRefreshCmdfailure:"Chyba sítě: Operace byla přerušena.",msg_error_preferencessnotsaved:"Předvolby nelze uložit.",msg_error_nopreferences:"Předvolby nelze načíst.",msg_error_Addinfolder:"Do této záložky nelze přidat obsah.",msg_error_CopyFolder:"Záložky nelze zkopírovat.",msg_error_MoveFolder:"Záložky nelze přesunout.",msg_error_MoveFolder_parent:"Cílová záložka je jednou z rodičů záložky, kterou chcete přesunout.",msg_error_CutPasteToRoot:"Záložky nelze přesunout do kořenové záložky.",msg_error_Removefromfolder:"Nelze odstranit objekty ze záložky.",msg_error_MoveInfolder:"Objekty nelze přesunout.",msg_error_MoveInSamefolder:{Title:"Vyberte jinou záložku.",Advice:"Zdrojová záložka a cílová záložka jsou stejné."},msg_error_rename:{Title:"Záložku nelze přejmenovat: {name}.",Advice:"Ověřte, zda se název není již nepoužívá nebo zda používáte správné přihlašovací údaje.",Details:"K přejmenování tohoto typu objektu možná nemáte potřebnou odpovědnost v cílovém prostoru pro spolupráci."},msg_error_name_maxlength:"Zadejte nový název v rozsahu 1 až {maxLength} znaků.",msg_error_name_empty:"Zadejte název.",msg_error_FolderActivate:{Title:"Nelze aktivovat {name1}.",Advice:"Záložka již není přístupná nebo byla možná odstraněna."},msg_error_NoVisibleRootFolder:{title:"Nemáte požadovaná oprávnění k otevření záložky.",subtitle:"Nadřazená záložka možná není sdílená nebo je ve stavu dokončení, který vám neumožňuje ji vidět."},msg_error_TreeExpandCancelledNotReady:{title:"Strom nelze rozbalit.",subtitle:"Obnovte pomůcku."},msg_error_forbiddenChar:{message:"Název sloupce obsahuje neplatné znaky: {badChars}"},msg_error_contentnotavailable:{Title:"Tato záložka již není přístupná nebo byla možná odstraněna. ",Advice:"Obnovte pomůcku."},msg_error_contentnotavailableInFavorite:{Title:"Záložka již není přístupná nebo byla možná odstraněna.",Advice:"Odstraňte záložku z oblíbených položek."},msg_InfoAction_Timeout:"Operace trvá déle, než se očekávalo. Bude pokračovat na pozadí. Obnovením aktualizujte data.",msg_error_timeout:{title:"Obsah záložky nelze načíst.",subtitle:"Vypršel časový limit připojení k serveru.",message:"Zkontrolujte připojení nebo se obraťte na správce se žádostí o pomoc."},msg_error_folderPath:"Záložka již není přístupná nebo byla možná odstraněna.",msg_error_folderPath_roles:"Tento objekt již není přístupný nebo byl možná odstraněn.",msg_error_appWhereUsedNotFound:{title:"Nelze otevřít aplikaci Relations.",subtitle:" Ověřte roli vaší licence."},msg_error_ReplaceBylatestRevision:"Objekty nelze nahradit jejich nejnovější revizí.",msg_error_MoveOrCopyAcrossSharedAndPersonal:"Nelze {move_copy} záložku {personal_shared} do záložky {shared_personal}.",msg_error_copylink:"Nemáte oprávnění ke kopírování odkazu do schránky.",msg_error_createNew:{Title:"Tento objekt nelze vytvořit: {type}",Advice:"Zkontrolujte, zda používáte správné přihlašovací údaje.",Details:"Možná nemáte potřebnou odpovědnost k vytvoření tohoto typu objektu v cílovém prostoru pro spolupráci."},msg_error_rest:{title:"Na serveru došlo k chybě.",subtitle:"Ohledně pomoci se obraťte na správce."},msg_error_rest_GetActiveFolder:{title:"Nelze načíst výchozí záložku.",subtitle:"Záložka již není přístupná nebo mohla být odstraněna.",message:"Nastavení záložky jako výchozí bylo zrušeno."},msg_warning_replaceByRevision_sameObject:{title:"Vyberte jinou revizi.",subtitle:"Revizi objektu nelze nahradit stejnou revizí."},msg_warning_replaceByLatestRevision_sameObject:"Záložka již obsahuje nejnovější revizi vybraných objektů.",msg_warning_notProductRootInFilteredContext:{title:"Tuto cestu nelze zobrazit.",subtitle:"V záložce není vidět žádný nadřazený objekt.",message:"Zrušte výběr předvolby „Skrýt podřízené výrobky označené záložkou“."},msg_warning_notProductRoot:"Objekt již není přístupný nebo byl možná odstraněn.",msg_warning_replaceByLatestRevision_NotMorethan100Objects:"Vyberte maximálně 100 objektů.",msg_warning_contentnotavailable:"Obsah již není přístupný nebo byl možná odstraněn.",msg_warning_removeContentFromSearch:{title:"Obsah nelze odebrat.",advice:"Vyberte podzáložku pro upřesnění výsledků vyhledávání.",details:"Obsah nelze odstranit z více než 20 záložek."},CMDInsertNewFolder:"Nová záložka",CMDRemoveContent:"Odebrat obsah",CMDAddFile:"Nahrát dokument",CMDDownloadFile:"Stáhnout",CMDPreviewFile:"Náhled",CMDCheckOutFile:"Vyzvednout",CMDCheckInFile:"Odevzdat",CMDEditFile:"Upravit",CMDUndoEditFile:"&Zpět úpravy",CMDFolderUpload:"Nahrát složku",CMDUpdateFile:"Aktualizovat",CMDRename:"Přejmenovat",CMDCut:"Vyjmout",CMDCopy:"Kopírovat",CMDPaste:"Vložit",CMDDelete:"Odstranit",CMDClearSort:"Zrušit řazení",CMDOpenColumnCusto:"Možnosti zobrazení stromového seznamu",CMDAddExistingContent:"Přidat stávající obsah",CMDExpandAll:"Rozbalit vše",CMDCollapseAll:"Sbalit vše",CMDAccessRightCmd:"Sdílet",CMDAddToFavorites:"Přidat do oblíbených",CMDProperties:"Vlastnosti",CMDSynchronize:"Synchronizovat s místním diskem",CMDActiveFolder:"Globální výchozí záložka",CMDOpen:"Otevřít",CMDOpenWithCV5:"Připojit CATIA V5",CMDFindInCtx:"Najít v kontextu",csv_base_file_name:"editor_záložek_",alpha_sorting:"Abecední pořadí",date_sorting:"Pořadí kal. data",fonticon_filter:"Obsah panelu je aktuálně filtrován",download:"Stáhnout",upload:"Nahrát",CMDDeleteDefinitively:"Odstranit",CMDShareBookmarkLink:"Kopírovat odkaz",CMDRestore:"Obnovit",CMDSizeColumnToFit:"Přizpůsobit velikost sloupce",HeaderThumbnail:"Miniatura",HeaderDeletionDate:"Datum odstranění",HeaderDeleteBy:"Odstraněno uživatelem",SearchEnterMoreThan:"Hledání vyžaduje tento minimální počet znaků: {minNumber}.",ShowNavPane:"Zobrazit navigační panel",HideNavPane:"Skrýt navigační panel",relations_tab:"Vztahy",confirmation_ok:"OK",confirmation_cancel:"Storno",confirmation_move_title:"Přesunout obsah",confirmation_move_message:"Chcete přesunout obsah {source} do {target}?",confirmation_move_message_monosel:"Chcete přesunout {selection} z {source} do {target} ?",confirmation_move_message_mono_from_tree:"Chcete přesunout {source} do {target} ?",confirmation_move_message_multisel:"Chcete přesunout položky {nth} z {source} do {target} ?",confirmation_move_button:"Přesunout",confirmation_remove_title:"Odebrat obsah",confirmation_remove_message:"Opravdu chcete odebrat obsah ze záložky?",confirmation_copyMassive_title:"Vytvořit kopii {source} v cíli {target}",confirmation_copyMassive_message:"Chystáte se zkopírovat velké množství položek.",confirmation_copyMassive_checkBox:"Ponechat položky označené záložkami v kopírované záložce",confirmation_copy:"Kopírovat",confirmation_info:"Atributy jiné než název a popis jsou ztraceny při převodu \n {From} na {To}. Všechny atributy podřízených objektů jsou zkopírovány.",authentication_error:"Tuto aplikaci nelze použít. Zkontrolujte, zda používáte správné přihlašovací údaje.",noPlatform_error:"Tato pomůcka není na vaší platformě dostupná.",placeholder_msg_folder:"Tato záložka je aktuálně prázdná.",placeholder_CMD_AddExisting:"Přidat stávající",placeholder_CMD_UploadFile:"Nahrát soubor",placeholder_CreateFolder:"Vytvořit záložku",tooltip_reserved:"Rezervováno",tooltip_unreserved:"Rezervace zrušena",tooltip_noDefaultLocation:"Žádná aktuální globální výchozí záložka.",tooltip_defaultLocation:"Globální výchozí záložkou je {folderLabel}. Kliknutím ji prozkoumejte.",tooltip_enableDefaultLocation:"Globální výchozí záložka {folderLabel} je deaktivována. Kliknutím ji aktivujte.",tooltip_disableDefaultLocation:"Kliknutím na toto tlačítko deaktivujete globální výchozí záložku {folderLabel}.",BookmarkLocation:"Zobrazit v rodičovské záložce",column_isLastRevision:"Je poslední verze",column_BookmarkLocation:"Nadřazená záložka",column_Thumbnail:"Miniatura",placeholder_msg_favorite:"Žádná oblíbená záložka.",placeholder_msg_section:"Nebyla nalezena žádná záložka.",placeholder_msg_search:"Nebyly nalezeny žádné výsledky.",refresh_ongoing:"Probíhá aktualizace...",refresh_ok:"Aktualizace provedena",refreshError:"Pomůcku nelze aktualizovat.",placeholder_findInCtx:"Vyhledat nebo vybrat objekt",findInCtxsearchview:{ftssearch:"Hledat",searchBtn:"Hledat",check:"Potvrdit výběr",cannotRetrieveLabel:"Nelze načíst popis vybraného objektu",loadingLabel:"Probíhá načítání..."},badCredentialChanged:"Byly nastaveny přihlašovací údaje „{credential}“, protože nemáte přístup k přednastaveným přihlašovacím údajům.",credentialChanged:"Byly nastaveny přihlašovací údaje „{credential}“.",success:"Úspěšně provedeno",warning:"Varování",information:"Informace",revisionReport:{reportTitle:"Zpráva – Nahradit poslední verzí",successfullyReplaced:"{prd1} byl nahrazen uživatelem {prd2}.",explicitlyRemoved:"{prd1} byl explicitně odebrán.",isLatestRevison:"{prd1} bude nahrazen a je již nejnovější verze.",msgObjectSelected:"Celkem {object} objektů bylo vybráno k nahrazení poslední verzí",msgObjectAlreadyLatestRevision:"Celkem {object} objektů má již nejnovější verzi.",msgSuccessfullyReplaced:"Celkem {object} objektů bylo úspěšně nahrazeno příslušnou nejnovější verzí.",msgObjectExplicitlyRemoved:"Celkem {object} objektů bylo explicitně odebráno, protože jejich poslední verze již byla přítomna v záložce nebo mělo několik vybraných objektů společnou verzi."},msg_advice_refreshWidget:"Obnovte pomůcku nebo stránku v prohlížeči a zkontrolujte, zda jste použili správné přihlašovací údaje.",msg_error_objectNoLongerAccessible:"Tento obsah již není přístupný nebo byl možná odstraněn.",Attachments:"Přílohy",Specification:"Dokumenty specifikací",OpenLocationRemoveTitle:"Odebrat obsah: {label}",OpenLocationRemoveMessage:"Chcete odstranit tento objekt z následujících záložek?",CMDUnsetGlobalActiveFolder:"Zrušit nastavení globální výchozí záložky",CMDShowInBookmarks:"Zobrazit v záložkách",CMDEnableGlobalActiveBookmark:"Povolit",CMDDisableGlobalActiveBookmark:"Zakázat",indexMode:{Authoring:"Vytváření.",Index:"Index.",Label:"Index",seconds:"sekund",minutes:"minut",hours:"hodin",msg_Authoring:"Režim vytváření je povolen. Může dojít k ovlivnění výkonu.",activeMode:"Aktivní režim: {mode}",lastRefresh:"Poslední aktualizace : před {timeElapsed}.",reactIndexMode:"Režim indexu se znovu aktivuje za {autoSwitch} minut od okamžiku vytvoření nebo obnovení.",errorMessageBookmarkExpand:"Záložky nelze rozbalit.",errorMessageBookmarkRoot:"Nelze načíst kořenové záložky.",info_msg_DataNotUptoDate:"Režim indexování je aktivní. Data nemusí být aktuální."},CMD_Yes:"Ano",CMD_No:"Ne",dialog_replaceByLatestRevision:"Nahradit opravou",msg_warning_FolderNotVisisbleinTree:"Vybraná záložka {name} není ve stromu vidět, přejděte na zobrazení a vyberte ji.",msg_warning_DndNotSupported:"Přetažení není podporováno v režimu jen ke čtení.",ViewTooltip:{"ds6w:label":"Název","ds6wg:revision":"Oprava","ds6w:modified":"Datum změny","ds6w:responsible":"Vlastník","ds6w:type":"Typ","ds6w:status":"Stav dokončení","ds6w:project":"Prostor pro spolupráci","ds6w:identifier":"Název"},msg_warning_dndWindowsFoldernotsupported:"Upuštění obsahuje složku a není podporováno",BookmarkDownload:{BookmarkDownloadLimit:"Struktura záložek obsahuje více než 1000 dokumentů; vyberte strukturu záložek obsahující méně dokumentů",BookmarkNo_Documents:"Struktura záložek neobsahuje žádný dokument, nic se nestáhne",BookmarkDupalicateDOC:"Stažení struktury záložek se nezdařilo, protože stejná hierarchie pro dokument již existuje",DownloadStructureFailure:"Nepodařilo se stáhnout strukturu záložek.",BookmarkDownloadStarted:"Stahování struktury záložek bylo zahájeno."},NoPathFound:"Nebyly nalezeny žádné platné cesty.",progressPanel_dragDrop:"Nahrávání položek {objectsAdded} do složky {targetfolder}",progressPanel_add:"Přidávání položek {objectsAdded} do složky {targetfolder}",progressPanel_move:"Přesouvání položek {objectsAdded} do složky {targetfolder}",progressPanel_copy:"Kopírování položek {objectsAdded} do složky {targetfolder}",progressPanel_remove:"Odebírání položek {objectsAdded} ze složky {targetfolder}"});define("DS/FolderEditor/assets/nls/FolderFindInCtx",{findInCtxSearchView:{objects:"objekty"},msg_warning_FindInCtx_objectsLimit:"Počet fyzických produktů, ve kterých bude hledání provedeno, je omezen. Nejprve budou pro nalezení objektu zvažovány {limit} fyzické produkty v aktuální záložce.",title_warning_FindInCtx_objectsLimit:"Kontextové hledání bude provedeno na omezeném počtu fyzických produktů.",msg_warning_FindInCtx_novalidobject:"V této záložce není žádný platný objekt k provádění kontextového hledání. Panel Hledat v kontextu se nespustí.",title_warning_FindInCtx_novalidobject:"Nelze provést kontextové hledání.",msg_warning_FindInCtx_allowProductExpand:"Nelze provést kontextové hledání, protože nastavení předvoleb „Povolit rozšíření produktů“ bylo nastaveno na hodnotu nepravda.",subtitle_warning_FindInCtx_allowProductExpand:"Změňte nastavení předvoleb „Povolit rozšíření produktů“ na hodnotu pravda.",msg_error_FindInCtx_noValidPhysicalObject:" V současné záložce není žádný platný fyzický produkt k provádění kontextového hledání"});define("DS/FolderEditor/assets/nls/FolderFindInTree",{loader:{findingBookmarks:"Hledání záložek..."},msg_warning_FindInTree_NoLaunchOnFavourite:"Hledání ve stromu nelze provést v části Oblíbené a v oblíbených záložkách.",msg_warning_FindInTree_InvalidDrop:{Title:"Přetažené objekty nejsou záložkou.",Advice:"Přetáhněte kořenovou záložku nebo záložku."},msg_warning_expandTotheFindInNode:{Title:"Hledání ve stromu nelze provést.",Advice:"Rozbalte strom k tomuto uzlu záložky",Details:"Hledaná záložka není zobrazená ve stromu."},msg_warning_NoObjectsUnderSection:{Title:"Hledání ve stromu nelze provést.",Advice:"Rozbalte část se záložkami na první úroveň.",Details:"V části Záložky nejsou žádné viditelné záložky."},msg_info_limit_FindInTree:"Bylo dosaženo limitu. Limit pro nalezení záložky je nastaven na 100.",msg_info_limitToFindInRootNode:"Limit pro hledání v kořenových záložkách je nastaven na 200. Bude zvažováno prvních 200 kořenových záložek.",msg_info_No_Occurrence:"Nebyly nalezeny žádné výskyty záložek.",find_in_Tree_Tooltip:"Find in Tree"});define("DS/FolderEditor/assets/nls/ImportCSVBookmarkPanel",{OK:"OK",Cancel:"Storno",Cancel_Tooltip:"Zrušit a zavřít toto dialogové okno.",Close:"Zavřít",Close_Tooltip:"Zavřete toto dialogové okno.",Change_Csv_File:"Změňte soubor CSV.",Drop_Csv_File:"Sem přetáhněte soubor CSV.",Dialog_Title:"Importovat strukturu záložky – ",Help_Csv_File:"Stáhněte soubor šablony.",Label_Import:"Importovat",Label_Import_Tooltip:"Spusťte import.",Label_LineNumber:"Přímka",Label_Column:"Sloupec",Label_Column_Level:"Úroveň",Label_Column_Name:"Název",Label_Column_Description:"Popis",Label_Column_Owner:"Vlastník",Label_Column_SecurityContext:"KontextZabezpečení",Label_Confirm_Title:"Varování – Velká struktura",ConfirmMsg_Drop_File_Size_Large:"Tato operace může chvíli trvat kvůli velké velikosti souboru. Chcete pokračovat?",ErrorMsg_Avoid_Char:"Obsahuje neplatné znaky.",ErrorMsg_Column_Count:"Zadaný počet sloupců je mimo podporovaný rozsah hodnot.",ErrorMsg_Desc_Max_Over:"Maximální délka popisu je 1024 znaků.",ErrorMsg_Drop_Multiple_File:"Více souborů je zakázáno.",ErrorMsg_Drop_File_Not_CSV:"Nejedná se o soubor CSV.",ErrorMsg_Drop_File_Empty:"Upuštěný soubor je prázdný.",ErrorMsg_Exist_Folder_Created:"Tato záložka již byla vytvořena. Obnovte pomůcku Editor záložek.",ErrorMsg_Exist_Folder_Modified:"Tato záložka již byla aktualizována. Obnovte pomůcku Editor záložek.",ErrorMsg_Folder_Create_Stoped:"Vytvoření záložky bylo zastaveno.",ErrorMsg_Level_Is_Not_Number:"Úroveň je prázdná nebo obsahuje neplatné znaky (úroveň pro následující řádky nebude zaškrtnuta).",ErrorMsg_Level_Is_Not_Sequencial:"Úroveň musí být pořadové číslo.",ErrorMsg_Level_Origin:"Úroveň má číslo menší než 1.",ErrorMsg_Name_Duplicate:"Stejný název záložky je již ve stejné nadřazené záložce.",ErrorMsg_Name_Required:"Název nesmí být prázdný.",ErrorMsg_Name_Max_Over:"Maximální délka názvu je 127 znaků.",ErrorMsg_SecCtx_Is_Invalid:"Kontext zabezpečení je neplatný.",ErrorMsg_SecCtx_Does_Not_Exist:"Kontext zabezpečení neexistuje.",ErrorMsg_Owner_Is_Not_Ascii:"Vlastník obsahuje neplatné znaky.",ErrorMsg_Owner_Does_Not_Exist:"Vlastník neexistuje.",ErrorMsg_Owner_Change_Failure:"Vlastníka nelze změnit.",ErrorMsg_Bookmark_Create_Stoped_SetProperties:"Vytvoření záložky bylo zastaveno. Nelze nastavit očekávané vlastnosti (název, popis nebo vlastník)",ErrorMsg_Rest_Error:"Chyba komunikace se serverem.",InfoMsg_Load_Csv_Success:"Soubor CSV byl úspěšně nahrán.",InfoMsg_Load_Csv_Error:"Soubor CSV obsahuje neplatné řádky.",InfoMsg_Create_Target_Size_Zero:"Celá struktura již existuje.",InfoMsg_Folder_Creating:"Vytváření záložky...",InfoMsg_Folder_Create_Success:"Záložky byly úspěšně vytvořeny.",WarnMsg_SecurityContext_MissMatch:"Kontexty zabezpečení se neshodují (vybraná záložka SC a aktuální SC)"});define("DS/FolderEditor/assets/nls/PADSlidder",{openSlidder:"Zobrazit strom záložek",closeSlidder:"Skrýt strom záložek"});