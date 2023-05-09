define("DS/FolderEditor/FolderEditor_es",{});define("DS/FolderEditor/assets/nls/AddCommentPopup",{SelectFile_PlaceHolder:"Seleccione un archivo...",SelectFile_Button:"Examinar",PanelTitle:"Realizar check-in",Comment_Title:"Comentario",Comment_PlaceHolder:"Introduzca el motivo del check-in...",OK:"Realizar check-in",Cancel:"Cancelar"});define("DS/FolderEditor/assets/nls/AddMovePopup",{operationToPerform:"¿Qué operación realizar con estos objetos?",msg_Title:"{number} objetos se han colocado en el marcador '{folderName}'.",Add:"Agregar",Move:"Mover"});define("DS/FolderEditor/assets/nls/BookmarkLocationPopup",{OK:"Aceptar",Cancel:"Cancelar",OpenLocation:"Marcador padre",Bookmark_Locations:"Ubicaciones de marcador",Open:"Abrir",OpenLocationPopupLable:"Seleccione el marcador que desea abrir:",msg_error_BookmarkLocation:'No es posible abrir la ubicación del marcador "{name1}" (es posible que se haya eliminado o no sea posible el acceso).',msg_error_BM_LOC_InValid_Object:"No es posible abrir la ubicación del objeto seleccionado. Es posible que el objeto se haya eliminado o que el acceso no sea posible.",removeFromSearchNotif:{success:"Se ha eliminado el objeto.",failNotAllowed:"Compruebe que el marcador se encuentra en un estado que permite el cambio de contenido.",failNoRelationFound:"Compruebe que dispone de suficientes permisos."}});define("DS/FolderEditor/assets/nls/FolderEditor",{name_Section_FavoriteFolders:"Favoritos",name_Section_AllFolders:"Marcadores",name_Section_Trash:"Eliminada",name_Section_ParentFolders:"{number} marcadores padres",name_Widget_Title:"Bookmark Editor",name_Widget_Help:"Ayuda de Bookmark Editor",name_RootFolder_type:"Raíz de marcador",name_Folder_type:"Marcador",folder_pref_dnd:"Comportamiento de arrastrar y soltar",folder_pref_filter:"Ocultar productos hijos marcados",folder_pref_allow_products_expand:"Habilitar la expansión de productos",folder_pref_subfolder:"Mostrar marcadores hijos en la vista de contenido",folder_pref_populateTags:"Habilitar etiquetas 6W para filtrar marcadores",folder_pref_activateRelationsColumns:"Mostrar relacionados",useIndexAcceleration_label:"Mostrar datos indexados",folder_pref_autoSwitch_Label:"Cambiar automáticamente del modo de creación al modo de índice",button_remove:"Quitar",button_close:"Cerrar",Move_label:"Mover",Copy_label:"Copiar",oneMin_Label:"1 minuto",threeMin_Label:"3 minutos",fiveMin_Label:"5 minutos",tenMin_Label:"10 minutos",Never_Label:"Nunca",Upload_content_loading:"Cargar contenido",AddExisting_content_loading:"Agregar contenido existente",Paste_content_loading:"Pegar contenido",Remove_content_loading:"Quitar contenido",Replace_revision_loading:"Sustituyendo contenido con la revisión más reciente",Content_Loading:"Cargando contenido...",Widget_Loading:"Cargando widget...",Tree_Loading:"Cargando árbol...",Bookmark_Loading:"Seleccionando marcador...",SetActiveBookmark_Loading:"Definiendo nuevo marcador predeterminado global…",UnsetActiveBookmark_Loading:"Anulando marcador predeterminado global…",EnableActiveBookmark_Loading:"Activando marcador predeterminado global…",DisableActiveBookmark_Loading:"Desactivando marcador predeterminado global…",Shared_label:"Compartido",Personal_label:"Personal",label_ActionBar_AddToFavorites:"Agregar o quitar favoritos",label_ActionBar_RemoveFromFavorites:"Quitar de favoritos",msg_success_copyLink_content:'Se ha copiado el enlace al documento "{filename}" que se encuentra en el marcador "{foldername}".',msg_success_copyLink_folder:'Se ha copiado el enlace al marcador "{foldername}".',msg_success_favoriteReordered:"Reordenado correctamente.",msg_info_contentcopied:"Se ha copiado el contenido.",msg_info_contentcut:"Se ha cortado el contenido.",msg_info_foldercut:"Se ha cortado el marcador.",msg_info_objetremoved:'{number} objetos eliminados del marcador  "{foldername}".',msg_info_objectremovedfromfolders:'El objeto "{objectname}" se ha eliminado correctamente de {number} marcadores.',msg_info_Addinfolder:"Los objetos {objectsAdded} se han añadido al marcador {targetfolder}, los objetos {objectsAlreadyPresent} que se van a añadir ya están cargados.",msg_info_Addinfolder_1:"Los objetos {objectsAdded} se han añadido al marcador {targetfolder}.",msg_info_objetadded:"'{filename}' agregado a \"{foldername}\".",msg_info_objetmoved:"Los objetos {objectsMoved} se han movido al marcador {targetfolder}, los objetos {objectsAlreadyPresent} que se van a mover ya están cargados.",msg_info_objetmoved_1:"Los objetos {objectsMoved} se han movido al marcador {targetfolder}.",msg_info_foldercopied:'{number} marcadores agregados al marcador  "{foldername}".',msg_info_foldermoved:'{number} marcadores movidos al marcador  "{foldername}".',msg_info_foldercreated:"Se ha creado el marcador.",msg_info_filecheckedout:"Se ha realizado correctamente el check-out del archivo.",msg_info_filecheckedin:"Se ha realizado correctamente el check-in del archivo.",msg_info_filecancelcheckout:"La operación de edición de archivos se ha cancelado correctamente.",msg_info_favoriteadded:'Se ha agregado el marcador "{name1}" a los favoritos.',msg_info_favoriteremoved:'Se ha eliminado el marcador "{name1} " de los favoritos.',msg_info_favoriterenamed:"Se ha cambiado el nombre del favorito '{name1}' a '{name2}'.",msg_info_rename:"El nuevo nombre del marcador '{name1}' es '{name2}'.",msg_info_refresh:"Actualizado.",msg_info_noResultSearch:"No se encontraron resultados.",msg_info_SortNonAvailable:"Esta columna no está disponible para ordenar durante una búsqueda.",msg_info_replaceByRevision:"Se ha sustituido el objeto correctamente.",msg_info_replaceByLatestRevision:"La operación Reemplazar por revisión más reciente se ha ejecutado correctamente",msg_info_replaceByLatestRevision_removed:"Se ha(n) eliminado {number} objeto(s) explícitamente, ya que su revisión más reciente ya estaba presente en el marcador o varios de los objetos seleccionados contaban con una revisión común.",msg_info_treeFilterRemove:"Los filtros del árbol se eliminan después de agregar el marcador de raíz",msg_info_active_successfully:"{folderLabel} se ha definido correctamente como marcador predeterminado global.",msg_info_inactive_successfully:"El marcador predeterminado global {folderLabel} se ha anulado correctamente.",msg_info_enable_successfully:"El marcador predeterminado global {folderLabel} está activado.",msg_info_disable_successfully:"El marcador predeterminado global {folderLabel} está desactivado.",msg_info_defaultLocation:"El marcador predeterminado global es {folderLabel}.",msg_warning_MultipleSelection:"No se admite la selección múltiple.",msg_warning_nocontent:"No se ha seleccionado ningún contenido. Seleccione contenido.",msg_warning_nofoldernocontent:"No se ha seleccionado nada. Seleccione contenido o un marcador.",msg_warning_nocurrentfolder:"No hay ningún marcador actual. Seleccione un marcador.",msg_warning_noselection:"Debe seleccionar al menos un objeto.",msg_warning_dndnotsupported:"Esta operación de soltado no es compatible.",msg_warning_dndnotsupported_external_on_Section_FavoriteFolders:"No se admite soltar marcadores externos en la sección de favoritos.",msg_warning_dndnotsupported_from_different_tenant:"No se admite soltar objetos provenientes de una 3DEXPERIENCE Platform distinta.",msg_warning_dndnotsupported_from_different_source:"Esta operación de arrastrar no es compatible. Arrastre los objetos desde un espacio de colaboración.",msg_warning_addnotsupported_from_different_tenant:"No se admite la adición de objetos provenientes de una 3DEXPERIENCE Platform distinta.",msg_warning_webInWinDnD:"Solo se admite soltar objetos en 3DDashboard.",msg_warning_FolderAlreadyAmongFavorites:'El marcador "{foldername}" ya está en los favoritos.',msg_warning_expandSaveLimitation:{Title:"No se guardará el último estado de expansión.",Advice:"Contraiga algunos marcadores para que se pueda guardar el estado de expansión.",Details:"Hay demasiados marcadores expandidos."},msg_warning_copyLink:"El objeto seleccionado no es un marcador",msg_warning_LimitInCtxSearchOnAllBookmarks:"No se admite la búsqueda de contexto si el recuento de marcadores raíz es superior a 200.",msg_error_contentNotFound:"El contenido ya no es accesible o puede que se haya eliminado.",msg_error_OnRefreshCmdfailure:"Error de red. Se ha interrumpido la operación.",msg_error_preferencessnotsaved:"No se pueden guardar sus preferencias.",msg_error_nopreferences:"No se pueden recuperar sus preferencias.",msg_error_Addinfolder:"No puede agregar contenido a este marcador.",msg_error_CopyFolder:"No se pueden copiar los marcadores.",msg_error_MoveFolder:"No se pueden mover los marcadores.",msg_error_MoveFolder_parent:"El marcador de destino es uno de los principales del marcador que se va a mover.",msg_error_CutPasteToRoot:"No puede mover los marcadores al marcador raíz.",msg_error_Removefromfolder:"No se pueden eliminar los objetos del marcador.",msg_error_MoveInfolder:"No se pueden mover los objetos.",msg_error_MoveInSamefolder:{Title:"Seleccione una vista distinta.",Advice:"El marcador de origen y el marcador de destino son los mismos."},msg_error_rename:{Title:"No puede cambiar el nombre del marcador : {name}.",Advice:"Compruebe que el nombre no está ya en uso o que está utilizando las credenciales correctas.",Details:"Es posible que no tenga la responsabilidad necesaria en el espacio de colaboración de destino para cambiar el nombre de este objeto."},msg_error_name_maxlength:"Introduzca un nuevo nombre de entre 1 {maxLength} y caracteres.",msg_error_name_empty:"Introduzca un nombre.",msg_error_FolderActivate:{Title:"No se puede activar {name1}.",Advice:"El marcador ya no es accesible o puede que se haya eliminado."},msg_error_NoVisibleRootFolder:{title:"No tiene los permisos necesarios para abrir el marcador.",subtitle:"Es posible que el marcador principal no se comparta o que se encuentre en un estado de madurez que no le permita verlo."},msg_error_TreeExpandCancelledNotReady:{title:"No se puede expandir el árbol.",subtitle:"Actualice el widget."},msg_error_forbiddenChar:{message:"El nombre de columna contiene caracteres no válidos: {badChars}"},msg_error_contentnotavailable:{Title:"El marcador ya no es accesible o puede que se haya eliminado. ",Advice:"Actualice el widget."},msg_error_contentnotavailableInFavorite:{Title:"El marcador ya no es accesible o puede que se haya eliminado.",Advice:"Elimine el marcador de sus favoritos."},msg_InfoAction_Timeout:"La operación está tardando más de lo esperado. Continuará en segundo plano. Refresque para actualizar los datos.",msg_error_timeout:{title:"No se puede recuperar el contenido del marcador.",subtitle:"Se ha agotado el tiempo de espera de la conexión con el servidor.",message:"Compruebe la conexión o póngase en contacto con el administrador para obtener asistencia."},msg_error_folderPath:"El marcador ya no es accesible o puede que se haya eliminado.",msg_error_folderPath_roles:"Este objeto ya no es accesible o puede que se haya eliminado.",msg_error_appWhereUsedNotFound:{title:"No puede abrir la aplicación Relations.",subtitle:" Verifique su función de licencia."},msg_error_ReplaceBylatestRevision:"No se pueden reemplazar los objetos por su última revisión.",msg_error_MoveOrCopyAcrossSharedAndPersonal:"No puede {move_copy} un marcador {personal_shared} en un marcador {shared_personal}.",msg_error_copylink:"No tiene permiso para copiar el enlace en el portapapeles.",msg_error_createNew:{Title:"No puede crear este objeto: {type}.",Advice:"Compruebe que está utilizando las credenciales correctas.",Details:"Es posible que no tenga la responsabilidad necesaria en el espacio de colaboración de destino para crear este tipo de objeto."},msg_error_rest:{title:"Se ha producido un error en el servidor.",subtitle:"Póngase en contacto con su administrador para recibir asistencia."},msg_error_rest_GetActiveFolder:{title:"No se puede recuperar el marcador predeterminado.",subtitle:"El marcador ya no es accesible o puede que se haya eliminado.",message:"El marcador se ha desactivado como predeterminado."},msg_warning_replaceByRevision_sameObject:{title:"Seleccione otra revisión.",subtitle:"No puede reemplazar la revisión de un objeto por la misma revisión."},msg_warning_replaceByLatestRevision_sameObject:"El marcador ya contiene la última revisión de los objetos seleccionados.",msg_warning_notProductRootInFilteredContext:{title:"No puede mostrar esta ruta.",subtitle:"No hay ningún objeto principal visible en el marcador.",message:'Desactive la preferencia "Ocultar productos hijo marcados".'},msg_warning_notProductRoot:"El objeto ya no es accesible o puede que se haya eliminado.",msg_warning_replaceByLatestRevision_NotMorethan100Objects:"No seleccione más de 100 objetos.",msg_warning_contentnotavailable:"El contenido ya no es accesible o puede que se haya eliminado.",msg_warning_removeContentFromSearch:{title:"No se puede eliminar el contenido.",advice:"Seleccione un submarcador para acotar los resultados de la búsqueda.",details:"No se puede eliminar contenido de más de 20 marcadores."},CMDInsertNewFolder:"Nuevo marcador",CMDRemoveContent:"Quitar contenido",CMDAddFile:"Cargar documento",CMDDownloadFile:"Descargar",CMDPreviewFile:"Vista preliminar",CMDCheckOutFile:"Realizar check-out",CMDCheckInFile:"Realizar check-in",CMDEditFile:"Editar",CMDUndoEditFile:"Deshacer Editar",CMDFolderUpload:"Carga de carpeta",CMDUpdateFile:"Actualizar",CMDRename:"Cambiar nombre",CMDCut:"Cortar",CMDCopy:"Copiar",CMDPaste:"Pegar",CMDDelete:"Eliminar",CMDClearSort:"Borrar orden",CMDOpenColumnCusto:"Opciones de la vista de lista de árbol",CMDAddExistingContent:"Agregar contenido existente",CMDExpandAll:"Expandir todo",CMDCollapseAll:"Contraer todo",CMDAccessRightCmd:"Compartir",CMDAddToFavorites:"Agregar a favoritos",CMDProperties:"Propiedades",CMDSynchronize:"Sincronizar con disco local",CMDActiveFolder:"Marcador predeterminado global",CMDOpen:"Abrir",CMDOpenWithCV5:"Conectar CATIA V5",CMDFindInCtx:"Buscar en contexto",csv_base_file_name:"bookmark_editor_",alpha_sorting:"Orden alfabético",date_sorting:"Orden de fecha",fonticon_filter:"El contenido del panel está filtrado actualmente",download:"Descargar",upload:"Cargar",CMDDeleteDefinitively:"Eliminar",CMDShareBookmarkLink:"Copiar enlace",CMDRestore:"Restaurar",CMDSizeColumnToFit:"Tamaño de columna que desea ajustar",HeaderThumbnail:"Miniatura",HeaderDeletionDate:"Fecha de eliminación",HeaderDeleteBy:"Eliminado por",SearchEnterMoreThan:"Se necesitan {minNumber} como mínimo para buscar.",ShowNavPane:"Mostrar el panel de navegación",HideNavPane:"Ocultar el panel de navegación",relations_tab:"Relaciones",confirmation_ok:"Aceptar",confirmation_cancel:"Cancelar",confirmation_move_title:"Mover contenido",confirmation_move_message:"¿Desea mover el contenido de {source} a {target}?",confirmation_move_message_monosel:"¿Desea mover {selection} de {source} a {target}?",confirmation_move_message_mono_from_tree:"¿Desea mover {source} a {target}?",confirmation_move_message_multisel:"¿Desea mover {nth} elementos de {source} a {target}?",confirmation_move_button:"Mover",confirmation_remove_title:"Quitar contenido",confirmation_remove_message:"¿Seguro que desea quitar el contenido del marcador?",confirmation_copyMassive_title:"Crear copia de {source} en {target}",confirmation_copyMassive_message:"Va a copiar un gran número de elementos.",confirmation_copyMassive_checkBox:"Mantener los elementos marcados en el marcador copiado",confirmation_copy:"Copiar",confirmation_info:"Los atributos que no sean el título y la descripción se pierden al convertir \n de {From} a {To}. Todos los atributos se copian para los objetos hijos.",authentication_error:"No puede utilizar esta aplicación. Compruebe que está utilizando las credenciales correctas.",noPlatform_error:"Este widget no está disponible en su plataforma.",placeholder_msg_folder:"Este marcador está vacío actualmente.",placeholder_CMD_AddExisting:"Agregar existente",placeholder_CMD_UploadFile:"Cargar archivo",placeholder_CreateFolder:"Crear marcador",tooltip_reserved:"Reservado",tooltip_unreserved:"No reservado",tooltip_noDefaultLocation:"No hay ningún marcador global predeterminado en este momento.",tooltip_defaultLocation:"El marcador predeterminado global es {folderLabel}. Haga clic para explorarlo.",tooltip_enableDefaultLocation:"El marcador predeterminado global {folderLabel} está desactivado. Haga clic para activarlo.",tooltip_disableDefaultLocation:"Haga clic para deshabilitar el marcador predeterminado global {folderLabel}.",BookmarkLocation:"Mostrar en marcador principal",column_isLastRevision:"Última revisión",column_BookmarkLocation:"Marcador principal",column_Thumbnail:"Miniatura",placeholder_msg_favorite:"No hay marcadores en los favoritos.",placeholder_msg_section:"No se ha encontrado ningún marcador.",placeholder_msg_search:"No se han encontrado resultados.",refresh_ongoing:"Actualizando...",refresh_ok:"Actualizado",refreshError:"No se puede actualizar el widget.",placeholder_findInCtx:"Buscar o seleccionar objeto",findInCtxsearchview:{ftssearch:"Buscar",searchBtn:"Buscar",check:"Confirmar selección",cannotRetrieveLabel:"No se puede recuperar la etiqueta del objeto seleccionado",loadingLabel:"Cargando..."},badCredentialChanged:'Se ha establecido "{credential}" porque no tiene acceso a las credenciales preseleccionadas.',credentialChanged:'"{credential}" se ha establecido.',success:"Correcto",warning:"Advertencia",information:"Información",revisionReport:{reportTitle:"Informe: sustituir por la revisión más reciente",successfullyReplaced:"{prd1} ha sido sustituido por {prd2}.",explicitlyRemoved:"{prd1} se ha eliminado explícitamente.",isLatestRevison:"{prd1} que debe sustituirse es ya la revisión más reciente.",msgObjectSelected:"{object} objeto(s) se ha(n) seleccionado para reemplazarlo(s) por su revisión más reciente",msgObjectAlreadyLatestRevision:"{object} objeto(s) ya está(n) en su revisión más reciente.",msgSuccessfullyReplaced:"{object} objeto(s) se ha(n) reemplazado correctamente por su revisión más reciente.",msgObjectExplicitlyRemoved:"Se ha(n) eliminado {object} objeto(s) explícitamente, ya que su revisión más reciente ya estaba presente en el marcador o varios de los objetos seleccionados contaban con una revisión común."},msg_advice_refreshWidget:"Actualice el widget o el navegador y compruebe que está utilizando las credenciales correctas.",msg_error_objectNoLongerAccessible:"Este contenido ya no es accesible o puede que se haya eliminado.",Attachments:"Adjuntos",Specification:"Documentos de especificación",OpenLocationRemoveTitle:"Quitar contenido: {label}",OpenLocationRemoveMessage:"¿Desea quitar este objeto de los siguientes marcadores?",CMDUnsetGlobalActiveFolder:"Anular marcador predeterminado global",CMDShowInBookmarks:"Mostrar en marcadores",CMDEnableGlobalActiveBookmark:"Activar",CMDDisableGlobalActiveBookmark:"Desactivar",indexMode:{Authoring:"Creación.",Index:"Índice.",Label:"Índice",seconds:"segundo(s)",minutes:"minuto(s)",hours:"hora(s)",msg_Authoring:"El modo de creación está activo. Es posible que el rendimiento se vea afectado.",activeMode:"Modo activo: {mode}",lastRefresh:"Última actualización: hace {timeElapsed}.",reactIndexMode:"El modo de índice se reactivará en {autoSwitch} minutos a partir del momento de la operación de creación o actualización.",errorMessageBookmarkExpand:"No se pueden expandir los marcadores.",errorMessageBookmarkRoot:"No se pueden recuperar los marcadores de raíz.",info_msg_DataNotUptoDate:"El modo de índice está activo. Es posible que los datos no estén actualizados."},CMD_Yes:"Sí",CMD_No:"No",dialog_replaceByLatestRevision:"Reemplazar por revisión",msg_warning_FolderNotVisisbleinTree:"El marcador seleccionado {name} no es visible en el árbol, desplácese para verlo y seleccionarlo.",msg_warning_DndNotSupported:"Soltar es incompatible con el modo de solo lectura.",ViewTooltip:{"ds6w:label":"Título","ds6wg:revision":"Revisión","ds6w:modified":"Fecha de modificación","ds6w:responsible":"Propietario","ds6w:type":"Tipo","ds6w:status":"Estado de madurez","ds6w:project":"Espacio de colaboración","ds6w:identifier":"Nombre"},msg_warning_dndWindowsFoldernotsupported:"Soltar contiene una carpeta y no es compatible",BookmarkDownload:{BookmarkDownloadLimit:"La estructura de marcadores contiene más de 1000 documentos; seleccione una estructura de marcadores que contenga menos documentos",BookmarkNo_Documents:"La estructura de marcadores no contiene ningún documento, no se descargará nada",BookmarkDupalicateDOC:"La descarga de la estructura de marcadores ha fallado porque ya existe la misma jerarquía para el documento",DownloadStructureFailure:"Error al descargar la estructura de marcadores.",BookmarkDownloadStarted:"Se ha iniciado la descarga de la estructura de marcadores."},NoPathFound:"No se han encontrado trayectos válidos.",progressPanel_dragDrop:"Cargando {objectsAdded} elementos a {targetfolder}",progressPanel_add:"Agregando {objectsAdded} elementos a {targetfolder}",progressPanel_move:"Moviendo {objectsAdded} elementos a {targetfolder}",progressPanel_copy:"Copiando {objectsAdded} elementos a {targetfolder}",progressPanel_remove:"Eliminando {objectsAdded} elementos de {targetfolder}"});define("DS/FolderEditor/assets/nls/FolderFindInCtx",{findInCtxSearchView:{objects:"objetos"},msg_warning_FindInCtx_objectsLimit:"Existe un límite en el número de productos físicos en los que se realizará la búsqueda. Se considerarán los primeros {limit} productos físicos del marcador actual para buscar el objeto.",title_warning_FindInCtx_objectsLimit:"La búsqueda en contexto se realizará en una serie limitada de productos físicos.",msg_warning_FindInCtx_novalidobject:"No hay ningún objeto válido en el marcador actual para realizar la búsqueda en contexto. El panel Buscar en contexto no se iniciará.",title_warning_FindInCtx_novalidobject:"No se puede realizar la búsqueda en contexto.",msg_warning_FindInCtx_allowProductExpand:"No se puede realizar la búsqueda en contexto porque la configuración de preferencia Permitir expansión de productos se ha establecido en falso.",subtitle_warning_FindInCtx_allowProductExpand:"Cambie la configuración de preferencia Permitir expansión de productos a verdadero.",msg_error_FindInCtx_noValidPhysicalObject:" No hay ningún producto físico válido en el marcador actual para realizar una búsqueda en contexto"});define("DS/FolderEditor/assets/nls/FolderFindInTree",{placeholder_findInTree:"Buscar en",toolTip:"Buscar en árbol",loader:{findingBookmarks:"Buscando marcadores...",retrievingPaths:"Recuperando rutas...",formulatingPaths:"Formulando rutas..."},msg_warning_FindInTree_NoLaunchOnFavourite:"La función Buscar en árbol no se puede realizar en la sección favorita ni en los marcadores favoritos.",msg_warning_FindInTree_InvalidDrop:{Title:"El objeto soltado no es un marcador.",Advice:"Suelte un marcador de raíz o un marcador."},msg_warning_expandTotheFindInNode:{Title:"No se puede buscar en árbol.",Advice:"Ampliar el árbol hasta ese nodo de marcador concreto",Details:"El marcador que se va a buscar no es visible en el árbol."},msg_warning_NoBookmarksUnderBookmarkSection:{Title:"No se puede buscar en árbol.",Advice:"Ampliar la sección de marcadores hasta el nivel uno.",Details:"No hay marcadores visibles en la sección de marcadores."},msg_warning_TreeTagger_ClosingThefind:{Title:"No se puede buscar en árbol.",Details:"Cerrando la búsqueda en el árbol, ya que se aplica el filtro de etiquetas 6W en el árbol mientras la búsqueda en el árbol todavía estaba en proceso."},msg_info_limit_FindInTree:"Se ha alcanzado el límite. El límite para buscar el marcador se establece en 100.",msg_info_limitToFindInRootNode:"El límite de la búsqueda en marcadores de raíz se establece en 200; se tendrán en cuenta los primeros 200 marcadores de raíz.",msg_info_No_Occurrence:"No se han encontrado ocurrencias de los marcadores.",msg_warning_NoObjectsUnderSection:{Title:"Find in tree cannot be done.",Advice:"Expand the bookmark section to level one.",Details:"There are no visible bookmarks under the bookmark section."},find_in_Tree_Tooltip:"Find in Tree"});define("DS/FolderEditor/assets/nls/ImportCSVBookmarkPanel",{OK:"Aceptar",Cancel:"Cancelar",Cancel_Tooltip:"Cancelar y cerrar este cuadro de diálogo.",Close:"Cerrar",Close_Tooltip:"Cerrar este cuadro de diálogo.",Change_Csv_File:"Cambiar archivo CSV.",Drop_Csv_File:"Suelte el archivo CSV aquí.",Dialog_Title:"Importar estructura de marcadores - ",Help_Csv_File:"Descargar el archivo de plantilla.",Label_Import:"Importar",Label_Import_Tooltip:"Ejecutar la importación.",Label_LineNumber:"Línea",Label_Column:"Columna",Label_Column_Level:"Nivel",Label_Column_Name:"Nombre",Label_Column_Description:"Descripción",Label_Column_Owner:"Propietario",Label_Column_SecurityContext:"Contexto de seguridad",Label_Confirm_Title:"Advertencia: Estructura grande",ConfirmMsg_Drop_File_Size_Large:"Esta operación puede tardar un rato debido al gran tamaño del archivo. ¿Desea continuar?",ErrorMsg_Avoid_Char:"Tiene caracteres no válidos.",ErrorMsg_Column_Count:"El número de columnas especificado está fuera del rango de valores admitido.",ErrorMsg_Desc_Max_Over:"La longitud máxima de la descripción es de 1024 caracteres.",ErrorMsg_Drop_Multiple_File:"Se prohíbe el uso de varios archivos.",ErrorMsg_Drop_File_Not_CSV:"No es un archivo CSV.",ErrorMsg_Drop_File_Empty:"El archivo soltado está vacío.",ErrorMsg_Exist_Folder_Created:"Este marcador ya se ha creado. Actualice el widget Editor de marcadores.",ErrorMsg_Exist_Folder_Modified:"Este marcador ya se ha actualizado. Actualice el widget Editor de marcadores.",ErrorMsg_Folder_Create_Stoped:"Se ha detenido la creación del marcador.",ErrorMsg_Level_Is_Not_Number:"El nivel está en blanco o tiene caracteres no válidos (el nivel de las siguientes líneas no se comprobará).",ErrorMsg_Level_Is_Not_Sequencial:"El nivel debe tener un número secuencial.",ErrorMsg_Level_Origin:"El nivel tiene un número menor de 1.",ErrorMsg_Name_Duplicate:"El nombre del marcador ya se encuentra en el mismo marcador padre.",ErrorMsg_Name_Required:"El nombre no puede estar en blanco.",ErrorMsg_Name_Max_Over:"La longitud máxima del nombre es de 127 caracteres.",ErrorMsg_SecCtx_Is_Invalid:"El contexto de seguridad no es válido.",ErrorMsg_SecCtx_Does_Not_Exist:"El contexto de seguridad no existe.",ErrorMsg_Owner_Is_Not_Ascii:"El propietario tiene caracteres no válidos.",ErrorMsg_Owner_Does_Not_Exist:"El propietario no existe.",ErrorMsg_Owner_Change_Failure:"No se puede cambiar el propietario.",ErrorMsg_Bookmark_Create_Stoped_SetProperties:"Se ha detenido la creación del marcador. No se pueden establecer las propiedades esperadas (nombre, descripción o propietario)",ErrorMsg_Rest_Error:"Error de comunicación con el servidor.",InfoMsg_Load_Csv_Success:"Archivo CSV cargado correctamente.",InfoMsg_Load_Csv_Error:"El archivo CSV tiene líneas no válidas.",InfoMsg_Create_Target_Size_Zero:"Ya existe toda la estructura.",InfoMsg_Folder_Creating:"Creando marcador...",InfoMsg_Folder_Create_Success:"Los marcadores se han creado correctamente.",WarnMsg_SecurityContext_MissMatch:"Los contextos de seguridad no coinciden (el actual y del marcador seleccionado)"});define("DS/FolderEditor/assets/nls/PADSlidder",{openSlidder:"Mostrar el árbol del marcador",closeSlidder:"Ocultar el árbol del marcador"});