define("DS/TerminologyEditor/TerminologyEditor_it",{});define("DS/TerminologyEditor/assets/nls/AddCommentPopup",{SelectFile_PlaceHolder:"Seleziona un file...",SelectFile_Button:"Sfoglia",PanelTitle:"Check-in",Comment_Title:"Commento",Comment_PlaceHolder:"Inserisci il motivo del check-in...",OK:"Check-in",Cancel:"Annulla"});define("DS/TerminologyEditor/assets/nls/AddMovePopup",{operationToPerform:"Quale operazione si desidera eseguire con questi oggetti?",msg_Title:'{number} oggetto/i è/sono stato/i rilasciato/i nel segnalibro "{terminologyName}".',Add:"Aggiungi",Move:"Sposta"});define("DS/TerminologyEditor/assets/nls/CreateTermDialog",{title:{termCreation:"Crea nodo di navigazione",terminologyCreation:"Crea struttura di navigazione"},options:{title:"Titolo",titleMessage:"È necessario immettere un titolo",description:"Descrizione",descMessage:"Immettere una descrizione"},buttons:{create:"Crea",cancel:"Annulla"},error:{invalidFrame:"l'intervallo selezionato non è valido, la finestra di dialogo non può essere creata",missingcallback:"Richiami mancanti",invalidType:"È necessario chiamare questo comando per creare una struttura di navigazione o un nodo di navigazione"}});define("DS/TerminologyEditor/assets/nls/ImportCSVTerminologyPanel",{OK:"OK",Cancel:"Annulla",Cancel_Tooltip:"Annulla e chiude questa finestra di dialogo.",Close:"Chiudi",Close_Tooltip:"Chiude questa finestra di dialogo.",Change_Csv_File:"Modifica il file CSV.",Drop_Csv_File:"Rilascia il file CSV qui.",Dialog_Title:"Importa struttura terminologica - ",Help_Csv_File:"Scarica il file modello.",Label_Import:"Importa",Label_Import_Tooltip:"Esegue l'importazione.",Label_LineNumber:"Linea",Label_Column:"Colonna",Label_Column_Level:"Livello",Label_Column_Name:"Nome",Label_Column_Description:"Descrizione",Label_Column_Owner:"Proprietario",Label_Column_SecurityContext:"Contesto di sicurezza",Label_Confirm_Title:"Avvertenza - Struttura di grandi dimensioni",ConfirmMsg_Drop_File_Size_Large:"Questa operazione potrebbe richiedere del tempo a causa delle grandi dimensioni del file. Continuare?",ErrorMsg_Avoid_Char:"Contiene caratteri non validi.",ErrorMsg_Column_Count:"Il numero di colonne specificato non rientra nell'intervallo di valori supportato.",ErrorMsg_Desc_Max_Over:"La descrizione deve contenere al massimo 1024 caratteri.",ErrorMsg_Drop_Multiple_File:"Non sono consentiti più file.",ErrorMsg_Drop_File_Not_CSV:"Non è un file CSV.",ErrorMsg_Drop_File_Empty:"Il file rilasciato è vuoto.",ErrorMsg_Exist_Terminology_Created:"Questo segnalibro è già stato creato. Aggiornare il widget Browsing Structure Definition.",ErrorMsg_Exist_Terminology_Modified:"Questo segnalibro è già stato aggiornato. Aggiornare il widget Browsing Structure Definition.",ErrorMsg_Terminology_Create_Stoped:"Creazione della terminologia interrotta.",ErrorMsg_Level_Is_Not_Number:"Il livello è vuoto o contiene caratteri non validi (il livello delle linee seguenti non verrà verificato).",ErrorMsg_Level_Is_Not_Sequencial:"Il livello deve avere un numero in sequenza.",ErrorMsg_Level_Origin:"Il livello ha un numero inferiore a 1.",ErrorMsg_Name_Duplicate:"Lo stesso nome Terminologia è già presente nella Terminologia padre.",ErrorMsg_Name_Required:"Il nome non può essere vuoto.",ErrorMsg_Name_Max_Over:"Il nome deve contenere al massimo 127 caratteri.",ErrorMsg_SecCtx_Is_Invalid:"Il contesto di sicurezza non è valido.",ErrorMsg_SecCtx_Does_Not_Exist:"Il contesto di sicurezza non esiste.",ErrorMsg_Owner_Is_Not_Ascii:"Il proprietario contiene caratteri non validi.",ErrorMsg_Owner_Does_Not_Exist:"Il proprietario non esiste.",ErrorMsg_Owner_Change_Failure:"Il proprietario non può essere modificato.",ErrorMsg_Terminology_Create_Stoped_SetProperties:"Creazione terminologia interrotta. Impossibile impostare le proprietà previste (nome, descrizione o proprietario)",ErrorMsg_Rest_Error:"Errore di comunicazione server.",InfoMsg_Load_Csv_Success:"Caricamento del file CSV riuscito.",InfoMsg_Load_Csv_Error:"Il file CSV contiene linee non valide.",InfoMsg_Create_Target_Size_Zero:"L'intera struttura esiste già.",InfoMsg_Terminology_Creating:"Creazione termine in corso...",InfoMsg_Terminology_Create_Success:"Creazione termine/i riuscita.",WarnMsg_SecurityContext_MissMatch:"I contesti di sicurezza non corrispondono (contesto di sicurezza terminologia selezionato e contesto di sicurezza corrente)"});define("DS/TerminologyEditor/assets/nls/PADSlidder",{openSlidder:"Visualizza la struttura ad albero della terminologia",closeSlidder:"Nasconde la struttura ad albero della terminologia"});define("DS/TerminologyEditor/assets/nls/TerminologyEditor",{name_Section_AllTerminologies:"Strutture di navigazione",name_Section_Trash:"Eliminati",name_Section_ParentTerminologies:"{number} Struttura di navigazione padre",name_Widget_Title:"Definizione struttura di navigazione",name_Widget_Help:"Guida alla definizione della struttura di navigazione",name_Term:"Termine",name_Terminology_type:"Terminologia",terminology_pref_dnd:"Comportamento del trascinamento della selezione",terminology_pref_filter:"Nascondi i prodotti figlio con segnalibri",terminology_pref_allow_products_expand:"Consenti l'espansione dei prodotti",terminology_pref_subterminology:"Mostra strutture di navigazione figlio nella vista del contenuto",terminology_pref_populateTags:"Abilita 6WTag per filtrare le strutture di navigazione",terminology_pref_activateRelationsColumns:"Mostra correlazioni",useIndexAcceleration_label:"Mostra dati indicizzati",terminology_pref_autoSwitch_Label:"Passaggio automatico dalla modalità di creazione alla modalità Indice",button_remove:"Rimuovi",button_close:"Chiudi",Move_label:"Sposta",Copy_label:"Copia",oneMin_Label:"1 minuto",threeMin_Label:"3 minuti",fiveMin_Label:"5 minuti",tenMin_Label:"10 minuti",Never_Label:"Mai",Upload_content_loading:"Carica contenuto",AddExisting_content_loading:"Aggiunti contenuto esistente",Paste_content_loading:"Incolla contenuto",Remove_content_loading:"Rimuovi contenuto",Content_Loading:"Caricamento contenuto in corso...",Widget_Loading:"Caricamento widget in corso...",Tree_Loading:"Caricamento struttura ad albero in corso...",Terminology_Loading:"Selezione struttura di navigazione...",SetActiveTerminology_Loading:"Impostazione della nuova struttura di navigazione predefinita globale...",UnsetActiveTerminology_Loading:"Annullamento dell'impostazione della struttura di navigazione predefinita globale...",EnableActiveTerminology_Loading:"Attivazione struttura di navigazione predefinita globale...",DisableActiveTerminology_Loading:"Disattivazione struttura di navigazione predefinita globale...",Shared_label:"Condiviso",Personal_label:"Personale",msg_success_copyLink_terminology:'Il collegamento alla struttura di navigazione "{terminologyname}" è stato copiato.',msg_success_createTerminology:'Creazione della struttura di navigazione "{terminologyName}" riuscita',msg_success_createTerm:'Creazione del nodo di navigazione "{termName}" riuscita',msg_info_contentcopied:"Il contenuto è stato copiato.",msg_info_contentcut:"Il contenuto è stato tagliato.",msg_info_terminologycut:"La struttura di navigazione è stata tagliata.",msg_info_objetremoved:'{number} oggetto/i rimosso/i dalla struttura di navigazione "{terminologyname}".',msg_info_objectremovedfromterminologies:'L\'oggetto "{objectname}" è stato rimosso correttamente da {number} struttura/e di navigazione.',msg_info_Addinterminology:"{objectsAdded} oggetto/i aggiunto/i alla struttura di navigazione {targetterminology}, {objectsAlreadyPresent} oggetto/i da aggiungere era/erano già presente/i.",msg_info_Addinterminology_1:"{objectsAdded} oggetto/i aggiunto/i alla struttura di navigazione {targetterminology}.",msg_info_terminologycopied:'{number} struttura/e di navigazione aggiunta/e alla struttura di navigazione "{terminologyname}".',msg_info_terminologymoved:'{number} struttura/e di navigazione spostata/e nella struttura di navigazione "{terminologyname}".',msg_info_terminologycreated:"La struttura di navigazione è stata creata.",msg_info_filecancelcheckout:"L'operazione di modifica del file è stata annullata.",msg_info_rename:'"{name1}" è stata rinominata in "{name2}".',msg_info_refresh:"Aggiornamento eseguito.",msg_info_noResultSearch:"Nessun risultato trovato.",msg_info_SortNonAvailable:"Questa colonna non è disponibile per l'ordinamento durante una ricerca.",msg_info_treeFilterRemove:"I filtri dell'albero vengono rimossi dopo l'aggiunta della struttura di navigazione",msg_info_active_successfully:"{terminologyLabel} è stato impostato correttamente come struttura di navigazione predefinita globale.",msg_info_inactive_successfully:"La struttura di navigazione predefinita globale {terminologyLabel} è stata annullata correttamente.",msg_info_enable_successfully:"La struttura di navigazione predefinita globale {terminologyLabel} è ora abilitata.",msg_info_disable_successfully:"La struttura di navigazione predefinita globale {terminologyLabel} è ora disabilitata.",msg_info_defaultLocation:"La struttura di navigazione predefinita globale è {terminologyLabel}.",msg_warning_MultipleSelection:"Selezione multipla non supportata.",msg_warning_nocontent:"Nessun contenuto selezionato: selezionare contenuto.",msg_warning_noterminologynocontent:"Nessuna selezione: selezionare il contenuto o la struttura di navigazione.",msg_warning_nocurrentterminology:"Nessuna struttura di navigazione corrente: selezionare una struttura di navigazione.",msg_warning_noselection:"Selezionare almeno un oggetto.",msg_warning_dndnotsupported:"L'operazione di trascinamento non è supportata.",msg_warning_dndnotsupported_from_different_tenant:"Il rilascio di oggetti da un'altra 3DEXPERIENCE Platform non è accettato.",msg_warning_dndnotsupported_from_different_source:"Questa operazione di rilascio non è supportata. Rilasciare gli oggetti da un'area di collaborazione.",msg_warning_addnotsupported_from_different_tenant:"L'aggiunta di oggetti da un'altra 3DEXPERIENCE Platform non è accettata.",msg_warning_webInWinDnD:"Il rilascio di oggetti è accettato solo su 3DDashboard.",msg_warning_expandSaveLimitation:{Title:"L'ultimo stato di espansione non verrà salvato.",Advice:"Comprimere alcune strutture di navigazione per consentire il salvataggio dello stato di espansione.",Details:"Sono presenti troppe strutture di navigazione espanse."},msg_warning_copyLink:"L'oggetto selezionato non è una struttura di navigazione.",msg_warning_LimitInCtxSearchOnAllTerminologies:"La Ricerca contestuale non è supportata se il numero di strutture di navigazione principali è superiore a 200.",msg_error_contentNotFound:"Contenuto non trovato",msg_error_WSfailure:"Errore del server: verificare i privilegi.",msg_error_notimplemented:"Comando non implementato.",msg_error_preferencessnotsaved:"Errore durante il salvataggio delle preferenze.",msg_error_nopreferences:"Errore durante il recupero delle preferenze.",msg_error_Addinanonterminology:"Aggiunta consentita solo nella struttura di navigazione.",msg_error_Addinterminology:"Non è possibile aggiungere contenuti a questa struttura di navigazione.",msg_error_createTerminology:'Creazione della struttura di navigazione "{terminologyName}" non riuscita.',msg_error_createTerm:'Creazione della struttura di navigazione "{termName}" non riuscita.',msg_error_CopyTerminology:"Errore durante la copia della/e struttura/e di navigazione.",msg_error_MoveTerminology:"Errore durante lo spostamento della/e struttura/e di navigazione.",msg_error_CutPasteToRoot:"Non supporta lo spostamento delle strutture di navigazione nella radice.",msg_error_PasteSharedToRoot:"Non supporta l'opzione Incolla Struttura di navigazione condivisa nella cartella principale o nella Struttura di navigazione principale.",msg_error_Removefromterminology:"Errore durante la rimozione degli oggetti dalla struttura di navigazione.",msg_error_MoveInterminology:"Errore durante lo spostamento degli oggetti.",msg_error_MoveInSameterminology:"La struttura di navigazione di destinazione e origine è la stessa. Selezionare una struttura di navigazione diversa.",msg_error_rename:{Title:"Non è possibile rinominare l'elemento di navigazione: {name}.",Advice:"Verificare che il nome non sia già utilizzato o che si stiano utilizzando le credenziali corrette.",Details:"È possibile che non si disponga della responsabilità richiesta nell'area di collaborazione di destinazione per rinominare questo oggetto."},msg_error_name_maxlength:'Il nuovo nome "{name}" supera la lunghezza massima consentita di {maxLength} caratteri.',msg_error_name_badchars:"Il nuovo nome contiene caratteri non validi. I caratteri non validi sono {badChars}.",msg_error_name_empty:"Nome non può essere vuoto.",msg_error_TerminologyActivate:'Impossibile attivare la struttura di navigazione "{name1}" (potrebbe essere stata eliminata o non essere accessibile).',msg_error_NoVisibleRootTerminology:"Nessuna struttura di navigazione radice visibile associata a questa struttura di navigazione (potrebbe non avere uno stato Attivo).",msg_error_TreeExpandCancelledInternalError:"Espansione struttura annullata, errore interno.",msg_error_TreeExpandCancelledNotReady:"Espansione struttura annullata, widget non ancora pronto.",msg_error_forbiddenChar:{Title:"Caratteri non consentiti.",Advice:"Non utilizzare i seguenti caratteri nel nome della colonna: {badChars}"},msg_error_contentnotavailable:{Title:"Questo contenuto non è più accessibile.",Advice:"Aggiornare il widget.",Details:"Impossibile recuperare il contenuto della struttura di navigazione. La struttura di navigazione potrebbe non essere più esistente o non essere accessibile."},msg_InfoAction_Timeout:"Nessuna risposta ricevuta dall'azione lanciata entro il periodo di timeout, l'esecuzione potrebbe richiedere più tempo del previsto. Il risultato potrebbe essere visibile tra poco dopo un aggiornamento manuale.",msg_error_timeout:"Impossibile recuperare il contenuto della struttura di navigazione entro il periodo di timeout.",msg_error_terminologyPath:"Non si dispone dell'accesso alla struttura di navigazione o la struttura di navigazione è stata eliminata",msg_error_terminologyPath_roles:"Non si dispone dell'accesso per visualizzare questo oggetto. Contattare l'amministratore",msg_error_appWhereUsedNotFound:"Widget Where Used non trovato sul server.",msg_error_MoveOrCopyAcrossSharedAndPersonal:"Impossibile {move_copy} una {personal_shared} struttura di navigazione in una {shared_personal} struttura di navigazione.",msg_error_copylink:"Errore durante la copia del collegamento negli Appunti",msg_error_createNewFromFramework:"Creazione del nuovo elemento non riuscita:",msg_error_createNew:{Title:"Impossibile creare questo oggetto: {type}.",Advice:"Verificare di utilizzare le credenziali corrette.",Details:"È possibile che non si disponga della responsabilità richiesta nell'area di collaborazione di destinazione per creare questo tipo di oggetto."},msg_error_rest:"Errore di comunicazione server. Riprovare più tardi.",msg_error_rest_GetActiveTerminology:"Impossibile recuperare la struttura di navigazione predefinita globale. Non è stato impostata.",msg_warning_notProductRootInFilteredContext:"Il filtro delle preferenze è attivato, impossibile visualizzare questo percorso.",msg_warning_notProductRoot:"Oggetto non trovato.",msg_warning_MoveOrCopyAcrossPersonalAndShared:"Lo spostamento o la copia delle strutture di navigazione non è consentita tra la struttura di navigazione personale e la struttura di navigazione condivisa.",msg_warning_contentnotavailable:"Impossibile recuperare il contenuto. È possibile che sia stato eliminato o impostato sullo stato privato.",msg_warning_removeContentFromSearch:{title:"Impossibile rimuovere il contenuto.",advice:"Selezionare una sub terminologia per perfezionare i risultati della ricerca.",details:"Impossibile rimuovere il contenuto da più di 20 strutture di navigazione."},CMDRename:"Rinomina",CMDSizeColumnToFit:"Adatta colonna",CMDRestore:"Ripristina",CMDDeleteDefinitively:"Elimina",CMDClearSort:"Cancella ordinamento",CMDAccessRightCmd:"Condividi",CMDShareTerminologyLink:"Copia collegamento",CMDUnsetGlobalActiveTerminology:"Deselezionare l'elemento di navigazione predefinito globale",CMDShowInTerminologies:"Mostra nelle strutture di navigazione",CMDEnableGlobalActiveTerminology:"Attiva",CMDDisableGlobalActiveTerminology:"Disattiva",csv_base_file_name:"editor_terminologia_",alpha_sorting:"Ordine alfabetico",alpha_sort_label:"Titolo",date_sorting:"Ordina per data",date_sort_label:"Data modificata",fonticon_filter:"Il contenuto del pannello è attualmente filtrato",download:"Scarica",upload:"Carica",loadingText:"Caricamento in corso...",HeaderThumbnail:"Miniatura",HeaderDeletionDate:"Data di eliminazione",HeaderDeleteBy:"Eliminato da",SearchEnterMoreThan:"Per una ricerca sono necessari almeno {minNumber} caratteri.",ShowNavPane:"Mostra il riquadro di navigazione",HideNavPane:"Nascondi il riquadro di navigazione",relations_tab:"Relazioni",confirmation_ok:"OK",confirmation_cancel:"Annulla",confirmation_move_title:"Sposta contenuto",confirmation_move_message:"Spostare il contenuto di {source} in {target}?",confirmation_move_message_monosel:"Spostare {selection} da {source} a {target}?",confirmation_move_message_mono_from_tree:"Spostare {source} a {target}?",confirmation_move_message_multisel:"Spostare {nth} elementi da {source} a {target}?",confirmation_move_button:"Sposta",confirmation_remove_title:"Rimuovi contenuto",confirmation_remove_message:"Rimuovere il contenuto dalla struttura di navigazione?",confirmation_copyMassive_title:"Crea copia di {source} in {target}",confirmation_copyMassive_message:"Si sta per copiare un gran numero di elementi.",confirmation_copyMassive_checkBox:"Mantieni gli elementi di navigazione nell'elemento di navigazione copiato",confirmation_copy:"Copia",confirmation_info:"Gli attributi diversi da titolo e descrizione vengono persi durante la conversione \n {From} in {To}. Tutti gli attributi vengono copiati per gli oggetti figlio.",authentication_error:"Le credenziali correnti non consentono l'uso di questa applicazione.",noPlatform_error:"Questo widget non è disponibile per i tenant.",placeholder_msg_terminology:"Questa struttura di navigazione è attualmente vuota.",placeholder_CMD_AddExisting:"Aggiungi esistente",placeholder_CreateTerminology:"Nuova struttura di navigazione",placeholder_CreateTerm:"Nuovo nodo di navigazione",tooltip_noDefaultLocation:"Nessuna struttura di navigazione predefinita globale corrente.",tooltip_defaultLocation:"La struttura di navigazione predefinita globale è {terminologyLabel}. Fare clic per esplorarla.",tooltip_enableDefaultLocation:"La struttura di navigazione predefinita globale {terminologyLabel} è disattivata. Fare clic per attivarlo.",tooltip_disableDefaultLocation:"Fare clic per disattivare la struttura di navigazione predefinita globale {terminologyLabel}.",TerminologyLocation:"Mostra nella struttura di navigazione padre",column_TerminologyLocation:"Struttura di navigazione padre",column_Thumbnail:"Miniatura",placeholder_msg_section:"Nessuna struttura di navigazione creata.",placeholder_msg_terms:"Nessun nodo di navigazione creato",placeholder_msg_search:"Nessun risultato trovato.",refresh_ongoing:"Aggiornamento in corso...",refresh_ok:"Aggiornamento eseguito",refreshError:"Si è verificato un errore durante l'aggiornamento",placeholder_findInCtx:"Cerca o Seleziona oggetto",findInCtxsearchview:{ftssearch:"Cerca",searchBtn:"Cerca",check:"Conferma selezione",cannotRetrieveLabel:"Impossibile recuperare l'etichetta dell'oggetto selezionato",loadingLabel:"Caricamento in corso..."},credentialError:"Si è verificato un errore durante il recupero delle credenziali disponibili",setCredentialError:"Si è verificato un errore durante il cambio delle credenziali",badCredentialChanged:'È stato impostato "{credential}", in quanto non si dispone dell\'accesso alle credenziali preselezionate.',credentialChanged:'È stato impostato "{credential}".',success:"Operazione riuscita",warning:"Avviso",information:"Informazioni",msg_advice_refreshWidget:"Aggiornare il widget o il browser e accertarsi di utilizzare le credenziali corrette.",msg_error_objectNoLongerAccessible:{Title:"Questo contenuto non è più accessibile.",Advice:"È possibile che non si disponga della responsabilità richiesta nell'area di collaborazione di destinazione per creare questo tipo di oggetto.",Details:"L'oggetto potrebbe essere stato eliminato o l'utente non dispone più del permesso di accesso richiesto."},Attachments:"Allegati",Specification:"Documenti specifiche",OpenLocationRemoveTitle:"Rimuovi contenuto: {label}",OpenLocationRemoveMessage:"Rimuovere questo oggetto dalle strutture di navigazione seguenti?",indexMode:{Authoring:"Creazione.",Index:"Indice.",Label:"Indice",seconds:"secondo/i",minutes:"minuto/i",hours:"ora/e",msg_Authoring:"La modalità di creazione è attivata. Le prestazioni possono essere influenzate.",activeMode:"Attiva modalità: {mode}",lastRefresh:"Ultimo aggiornamento: {timeElapsed} fa.",reactIndexMode:"La modalità indice verrà riattivata entro {autoSwitch} minuti dal momento dell'operazione di creazione o dell'aggiornamento.",errorMessageTerminologyExpand:"Il servizio non è riuscito ad espandere le strutture di navigazione",errorMessageTerminologyRoot:"Il servizio non è riuscito a ottenere le strutture di navigazione radice",info_msg_DataNotUptoDate:"La modalità indice è attiva. I dati potrebbero non essere aggiornati."},msg_warning_TerminologyNotVisisbleinTree:"La struttura di navigazione selezionata {name} non è visualizzabile nella struttura ad albero, scorrere per visualizzarla e selezionarla.",msg_warning_DndNotSupported:"La funzione di rilascio non è supportata in modalità di sola lettura.",ViewTooltip:{"ds6w:label":"Titolo","ds6w:modified":"Data di modifica","ds6w:responsible":"Proprietario","ds6w:type":"Tipo","ds6w:status":"Stato maturità","ds6w:project":"Area di collaborazione","ds6w:identifier":"Nome"},msg_warning_dndWindowsTerminologynotsupported:"La funzione di rilascio contiene una struttura di navigazione e non è supportata",notifications:{expandTooLarge:"Troppi oggetti recuperati.<br> Restringere la navigazione con un filtro o riducendo il livello di espansione.",initialization_error:"Impossibile inizializzare l'app. Provare a ricaricarla o contattare l'amministratore.",tooManyResultFound:"La ricerca è limitata a {maxResult} risultati. Modificare la query."},find_in_context_timeout:"Timeout Trova nel contesto",generic_service_failure:"Impossibile recuperare le informazioni sull'oggetto. Riprovare tra qualche minuto. Se il problema persiste, contattare il proprio amministratore",cannot_insert_branch:"Aggiunta oggetto non riuscita",cannot_access_after_changeowner:"Impossibile accedere all'oggetto dopo una modifica della proprietà"});define("DS/TerminologyEditor/assets/nls/TerminologyFindInCtx",{findInCtxSearchView:{objects:"oggetti"},msg_warning_FindInCtx_objectsLimit:"Esiste un limite al numero di prodotti fisici in cui verrà eseguita la ricerca. Per trovare l'oggetto verranno presi in considerazione i primi {limit} prodotti fisici nel segnalibro corrente.",title_warning_FindInCtx_objectsLimit:"La ricerca nel contesto verrà eseguita su un numero limitato di prodotti fisici.",msg_warning_FindInCtx_novalidobject:"Nel segnalibro corrente non è presente alcun oggetto valido per l'esecuzione della ricerca nel contesto. Il pannello Trova nel contesto non verrà avviato.",title_warning_FindInCtx_novalidobject:"Impossibile eseguire la ricerca nel contesto.",msg_warning_FindInCtx_allowProductExpand:"Impossibile eseguire la ricerca nel contesto perché l'impostazione della preferenza 'Consenti espansione prodotti' è stata impostata su false.",subtitle_warning_FindInCtx_allowProductExpand:"Modificare l'impostazione della preferenza 'Consenti espansione prodotti' su true.",msg_error_FindInCtx_noValidPhysicalObject:" Nessun prodotto fisico valido nel segnalibro corrente per eseguire la ricerca nel contesto"});define("DS/TerminologyEditor/assets/nls/TerminologyFindInTree",{loader:{findingTerminologies:"Ricerca delle terminologia in corso..."},msg_warning_FindInTree_InvalidDrop:{Title:"L'oggetto rilasciato non è una terminologia.",Advice:"Rilasciare una terminologia o un termine."},msg_warning_expandTotheFindInNode:{Title:"Impossibile eseguire l'opzione Cerca nella struttura ad albero.",Advice:"Espandi la struttura ad albero sul nodo della terminologia specifica",Details:"La terminologia per la ricerca non è visibile nella struttura ad albero."},msg_warning_NoObjectsUnderSection:{Title:"Impossibile eseguire l'opzione Cerca nella struttura ad albero.",Advice:"Espandi la sezione terminologia al livello uno.",Details:"Nella sezione terminologia non sono presenti terminologie visibili."},msg_info_limit_FindInTree:"Limite raggiunto. Il limite di ricerca della terminologia è impostato su 100.",msg_info_limitToFindInRootNode:"Il limite di ricerca nelle terminologie radice è impostato su 200 e vengono considerate le prime 200 terminologie radice.",msg_info_No_Occurrence:"Non sono state trovate occorrenze della terminologia."});define("DS/TerminologyEditor/assets/nls/TerminologyLocationPopup",{OK:"OK",Cancel:"Annulla",OpenLocation:"Terminologia padre",Terminology_Locations:"Posizioni della terminologia",Open:"Apri",OpenLocationPopupLable:"Selezionare la terminologia da aprire:",msg_error_TerminologyLocation:'Impossibile aprire la posizione per il segnalibro "{name1}" (potrebbe essere stato eliminato o non è accessibile).',msg_error_BM_LOC_InValid_Object:"Impossibile aprire l'oggetto posizione selezionato. L'oggetto potrebbe essere stato eliminato o non è accessibile.",removeFromSearchNotif:{success:"L'oggetto è stato rimosso.",failNotAllowed:"Verificare che il segnalibro si trovi in uno stato che consenta di modificarne il contenuto.",failNoRelationFound:"Verificare di disporre delle autorizzazioni sufficienti."}});