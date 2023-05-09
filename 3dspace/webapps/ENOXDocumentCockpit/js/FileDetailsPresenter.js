/**
 * @module DS/ENOXDocumentCockpit/js/FileDetailsPresenter
 * @description Module defining the list of documents attached to a model
 */

define(
	'DS/ENOXDocumentCockpit/js/FileDetailsPresenter',
	[
		// 'DS/ENOXSplitView/js/ENOXSplitView',
		'UWA/Core',
		'UWA/Class/View', 'DS/UIKIT/Input/Button', 'DS/UIKIT/Mask',
		'DS/UIKIT/DropdownMenu', 'DS/CoreEvents/ModelEvents',
		'DS/ENOXDocumentCockpit/js/FilesTableMainContainer',
		'DS/DocumentManagement/DocumentManagement',
		'DS/xPortfolioQueryServices/js/infra/xPortfolioInfra',
		'DS/xPortfolioUXCommons/NotificationsUtil/xNotificationsUtil',
		'DS/ResizeSensor/js/ResizeSensor',
		'DS/WildcardSearchComponent/WildcardSearchComponent',
		'DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices',
		'DS/DocumentManagementCustom/js/DocumentManagementCustom',
		'DS/Controls/ProgressBar',
		'i18n!DS/ENOXDocumentCockpit/assets/nls/DocumentCockpit',
		'i18n!DS/ENOXDocumentPresenter/assets/nls/DocumentsPresenter'
	],
	function (UWA,View, Button, Mask,
		 DropdownMenu, ModelEvents,
		FilesTableMainContainer, DocumentManagement,
		xPortfolioInfra, xNotificationsUtil, ResizeSensor, WildcardSearchComponent, i3DXCompassPlatformServices, DocumentManagementCustom,WUXProgressBar, Document_NLS,DOC_NLS) {
			"use strict";
	return View
	.extend({

		className: 'files-presenter',
		init: function (options) {
			this._applicationChannel = options.applicationChannel ? options.applicationChannel
				 : new ModelEvents();
			// this._applicationChannel.unsubscribe({
			// 	event: 'search-in-current-dashboard'
			// }); ;
			this._globalapplicationChannelForDocPresenter = options.applicationChannel ? options.applicationChannel
				 : new ModelEvents();
			this._localChannel = options.localChannel ? options.localChannel
				 : new ModelEvents();
			this.dataJSON = options.dataJSON ? options.dataJSON : {};

			this.docId = options.docId;
			this.WildcardSearch = new WildcardSearchComponent();

			this._parent(options);
			this.userRole = undefined;
			//this.();
		},

		_definedActionAccess  : function(){
			switch (this.userRole){
				case 'VPLMViewer':
					{
						 return {
						 'upload' : false,
						 'delete' : false
					 };
				 }

				case 'VPLMExperimenter' :{
					return {
						'upload' : true,
						'delete' : false
					};
        }
				default : return {
						'upload' : true,
						'delete' : true
					};
		 }
		},

		checkActionAccess : function(actionId){
			var errMessageTitle;
			var errMessage;
			if(widget.hasPreference("xPref_CREDENTIAL") && widget.getValue("xPref_CREDENTIAL")){
				 this.userRole = widget.getValue("xPref_CREDENTIAL").split(".")[0];
			}
			var actionAccess = this._definedActionAccess();
			if(!actionAccess[actionId]){
				switch(actionId){

					case "delete" : {
													 errMessageTitle = DOC_NLS.deletion_error_title;
													 errMessage = DOC_NLS.error_credentials_description_deletion;
													 break;
													}

					case 'upload' : {
													errMessageTitle = DOC_NLS.upload_error_title;
													errMessage = DOC_NLS.error_credentials_description_upload;
														break;
													}
					default : {
											errMessageTitle = DOC_NLS.default_err_message;
											errMessage = DOC_NLS.error_credentials_description;
											break;
										}
				}
				xNotificationsUtil.showError(
					{
						title : errMessageTitle + " : "+ DOC_NLS.DOC_Type_Product_Specification, //msgH1 - NLS to be handled in presenter
						subtitle : DOC_NLS.error_subtitle_credentials, //H2msg, l3msg {add comment}
						message : errMessage  //H3msg
					}
				);
				return false;
			}
			return true;
		},

		setup: function () {
			var that = this;
			this._parent.apply(this, arguments);
			this._localChannel.subscribe({event :'show-progress-bar'}, function () {
			 that.displayProgressBar();
			});

			this._localChannel.subscribe({event : 'hide-progress-bar'}, function (data) {
				that.removeProgressBar(data);
			});
		},

		render: function (data) {
			// Reload the selected Models if not updated
			// if (this.selectedModels && this.selectedModels.length == 1) {
			// } // what was that used for ?

			var that = this;
			var dataJson = '';
			var Reserve_Unreserve = 'lock-open';
			var Reserve_Unreserve_text = Document_NLS.DOC_Command_Unlock;
		//	var reserved = true;
			var emptyContainer=true;
			if (data === undefined) {
				dataJson = this.dataJSON;
			} else {
				dataJson = data;
				this.dataJSON = dataJson;
			}

			if(dataJson.relateddata.files.length>0){
				emptyContainer=false;
			}

			if (dataJson.dataelements['reservedby'] === "") {
				Reserve_Unreserve = 'lock';
				Reserve_Unreserve_text = Document_NLS.DOC_Command_Lock;
				//reserved = false;
			}
			this.tablePresenter = new FilesTableMainContainer({
					dataJSON: dataJson,
					kind: 'instance',
					showSwitchViewAction: false,
					localChannel: this._localChannel,
					_filesPresenter: this,
					actions: [{
							id: 'Reserve_Unreserve',
							text: Reserve_Unreserve_text,
							fonticon: Reserve_Unreserve,
							disable: false,
							handler: function () {
								that.reserve_unreserve(that, Reserve_Unreserve);
								UWA.log('actionView--' + actionView);
							}
						}, {
							id: 'upload',
							text: Document_NLS.DOC_Command_Upload,
							fonticon: 'upload',
							disable: false,
							// 'event': 'onUploadAction'
							handler: function () {
									//Mask.mask(that.container);
								if(that.checkActionAccess('upload'))	{
									that.uploadFiles(that);
									UWA.log('actionView--' + that);
								}
							}
						}, {
							id: 'download',
							text: Document_NLS.DOC_Command_Download,
							fonticon: 'download',
							disable: false,
							handler: function () {
								if(emptyContainer){
										xNotificationsUtil.showError(Document_NLS.DOC_DELETE_No_Files_Download);
									}
								else {
								that.downloadLatestFile(that);
								}
							}

						}, {
							id: 'Multi',
							text: Document_NLS.DOC_Delete,
							fonticon: 'trash',
							disable: false,
							content: [
								//{
								// 	id : 'DeleteLatestVersion',
								// 	text : Document_NLS.DOC_Delete_Latest_Version,
								// //	fonticon : 'fonticon fonticon-lock',
								// 	selectable : true,
								// 	handler : function() {
								// 	var action='DeleteLatestVersion';
								// 		that.deleteFileAction(that,action);
								// 	}
								// },
								{
									id: 'DeleteAllVersions',
									text: Document_NLS.DOC_Delete_All_Version,
									//	fonticon : 'fonticon fonticon-lock-open',
									selectable: true,
									handler: function () {
											if(that.checkActionAccess('delete'))	{
												var action = 'DeleteAllVersions';
												Mask.mask(that.container);
												that.deleteFileAction(that, action);
											}
									}
								}, {
									id: 'DeleteAllVersionsButLast',
									text: Document_NLS.DOC_Delete_All_Versions_But_Last,
									//fonticon : 'fonticon fonticon-download',
									selectable: true,
									handler: function () {
											if(that.checkActionAccess('delete'))	{
												var action = 'DeleteAllVersionsButLast';
												Mask.mask(that.container);
												that.deleteFileAction(that, action);
												//	that.downloadSelection(that);
											}
									}
								}
							],
							handler: function () {}
						}
					],
					itemActions: [{
							title: Document_NLS.DOC_Command_Download,
							text: Document_NLS.DOC_Command_Download,
							fonticon: 'download',
							'event': 'onDownloadAction'
						}
					],
					contentFilter: function (item) {
						var includeItem = true;
						if (item.kind === 'reference') {
							includeItem = false;
						}
						return includeItem;
					}
					// actionView : this,
				});
			this.tablePresenter.render(that);
			//	this.tablePresenter.attachResizeSensor();
			//	this.attachResizeSensor();

			this.tablePresenter.subscribe('onDownloadAction', function (item) {
				UWA.log('onDownloadAction action');
				that.downloadFile(item);
				// that.removeCriteria(criteria);
				// that.removeDictionaryCriteria(criteria);
				// that.onChange();
			});

			var contentView = UWA.createElement('div', {
				id: 'contentviewid',
					styles: {
						width: '100%',
						height: '100%',
						display: 'table'
					}
				});
			var okButton = new Button({
					value: 'Ok',
					className: 'primary'
				});
			okButton.addEvent('onClick', function () {
				that.okAction();
			});
			var cancelButton = new Button({
					value: 'Cancel'
				});
			cancelButton.addEvent('onClick', function () {
				that.cancelAction();
			});

			this.actionPanel = UWA.createElement('div', {
					html: [okButton, cancelButton],
					'class': 'modal-footer',
					styles: {
						width: '100%',
						display: 'none'
					}
				});

			var tableContainer = UWA.createElement('div', {
					html: this.tablePresenter,
					styles: {
						height: '100%',
						width: '100%',
						display: 'table-row'
					}
				});
			contentView.addContent(tableContainer);
			contentView.addContent(this.actionPanel);

			this.container.setContent(contentView);
			this.container.setStyle('height', '100%');
			Mask.mask(this.container);
			this._parent();
			setTimeout(function () {
				that.onRender();
			}, 100);
			return this;
		},
		onRender: function () {
			UWA.log();
			this.initializeVariabilityView();
			// Mask.mask(this.container);
		},

		initializeVariabilityView: function () {
			//	this.attachResizeSensor();
			Mask.unmask(this.container);
			return this;
		},

		downloadFile: function (id) {
			var that = this;
			UWA.log('Downloading the files');
      that._localChannel.publish({ event: 'show-progress-bar'});
		//	var docId = id.docId;
			var fileId = id.id;
			var fileName = id.data.filesAttributes.title;
		//	var version = id.version;
			var options = {
				onComplete: function (result) {
					UWA.log('download complete ..' + result);
					if (result) {
						UWA.log('Result' + result);
						var xhr = new XMLHttpRequest();
						xhr.open('GET', result);
						xhr.responseType = 'blob';
						xhr.onload = function () {
							// IR-556364-3DEXPERIENCER2017x: need a specific way to download blob
							// on IE 11
							if (!window.navigator.msSaveBlob) {
								var a = document.createElement('a');
								a.href = window.URL.createObjectURL(xhr.response); // xhr.response
								// is a blob
								if (fileName) {
									a.download = fileName;
								} // Set the file name.
								a.style.display = 'none';
								document.body.appendChild(a);
								a.click();
								a = undefined;
							} else {
								window.navigator.msSaveBlob(xhr.response, fileName);
							}
						};
						that._localChannel.publish({ event: 'hide-progress-bar',data: true});
						xhr.send();
						xNotificationsUtil.showSuccess(Document_NLS.DOC_Files_download_success);
					} else {
						console
						.log('Error in downloading document: ' + fileInfo.errorMessage);
						that._localChannel.publish({ event: 'hide-progress-bar',data: false});
					}
				},
				onFailure: function (result) {
					UWA.log('failed' + result);
					xNotificationsUtil.showError(Document_NLS.DOC_Files_download_failure);
	        that._localChannel.publish({ event: 'hide-progress-bar',data: false});
				}
			};
			DocumentManagement.downloadDocument(fileId, undefined, false, options);
		},

		reserve_unreserve: function (_data, action) {

			var that = _data;
			var docId = that.dataJSON.id;

			if (action !== undefined && action === 'lock') {
				Mask.mask(that.container);
				DocumentManagementCustom.reserveDocument(docId, {
					securityContext: xPortfolioInfra.getSecurityContext(),
					onComplete: function () {
						xNotificationsUtil.showSuccess(Document_NLS.DOC_Select_Lock_Success);

						var docOpt = {
							getVersions: true,
							onComplete: function (output) {
								var data = output.data;
								if (data) {
									if (UWA.is(data, 'array')) {
										data = data[0];
									}
								}
								that.render(data);
								Mask.unmask(that.container);

							},
							onFailure: function () {}
						};
						DocumentManagement.getDocuments([docId], docOpt);
					},
					onFailure: function (failureData) {
						UWA.log(failureData);
						xNotificationsUtil.showError(Document_NLS.DOC_Select_Lock_Error+failureData.error);
						Mask.unmask(that.container);
					}
				});
			} else if (action !== undefined && action ==='lock-open') {
				Mask.mask(that.container);
				DocumentManagementCustom.unreserveDocument(docId, {
					securityContext: xPortfolioInfra.getSecurityContext(),
					onComplete: function () {
						xNotificationsUtil.showSuccess(Document_NLS.DOC_Select_Unlock_Success);
						Mask.unmask(that.container);
						var docOpt = {
							getVersions: true,
							onComplete: function (output) {
								var data = output.data;
								if (data) {
									if (UWA.is(data, 'array')) {
										data = data[0];
									}
								}
								that.render(data);
								Mask.unmask(that.container);

							},
							onFailure: function () {}
						};
						DocumentManagement.getDocuments([docId], docOpt);
					},
					onFailure: function (failureData) {
						UWA.log(failureData);
						xNotificationsUtil.showError(Document_NLS.DOC_Select_Lock_Error+failureData.error);
						Mask.unmask(that.container);
					}
				});
			} else {
				xNotificationsUtil.showError(Document_NLS.DOC_Select_Lock_Error);
			}

		},
		downloadLatestFile: function (data) {
			var that = data;
			UWA.log('Downloading the files');
			that._localChannel.publish({ event: 'show-progress-bar'});
			var docId = that.dataJSON.id;
		//	var fileId = that.dataJSON.relateddata.files[0].id;
			//var fileName = that.dataJSON.relateddata.files[0].dataelements.title;
			// var docId = id.docId;
			// var fileId = id.id;
			// var fileName = id.data.filesAttributes.title;
			//
			var options = {
				// onComplete : function (result) {
				// console.log('download complete ..');
				// }
				onComplete: function (result) {
					if (UWA.is(result, 'array')) {
						result.forEach(function (fileInfo) {
							if (fileInfo.downloadUrl) {

								xPortfolioInfra.getBlobObject(fileInfo.downloadUrl, function () {
									var blob = this.response;
									if (window.navigator.msSaveOrOpenBlob) {
										var contentType = this.getResponseHeader("content-type");
										window.navigator.msSaveOrOpenBlob(new Blob([blob], {
												type: contentType
											}), fileInfo.fileName);
									} else {
										var downloadLink = UWA.createElement('a', {
												href: window.URL.createObjectURL(blob),
												download: fileInfo.fileName,
												type: 'application/octet-stream'
											});
										downloadLink.click();
									}
									that._localChannel.publish({ event: 'hide-progress-bar',data: true });
								});
							}
						});
					}
				},
				onFailure: function (result) {
					UWA.log('failed' + result);
					xNotificationsUtil.showError(Document_NLS.DOC_Files_download_failure);
					that._localChannel.publish({ event: 'hide-progress-bar',data: false});
				}
			};
			DocumentManagement.downloadDocuments([docId], options);
		},

		displayProgressBar: function(){
			var that = this;
			that.progressBarContainer = UWA.createElement('div', {
			 html :'',
			 'class' : 'progress-bar',
			 styles : {
				 width : '100%',
				 display : 'none'
			 }
		 });
		 if (that.container.getElement('#contentviewid') !== null){
			 that.container.getElement('#contentviewid').addContent(that.progressBarContainer);
		 }
		 if (that.container.getElementsByClassName('progress-bar').length >0){
			 that.container.getElement('.progress-bar').style.display = 'block';
		 }
		 that.progressbar = new WUXProgressBar(
			 {
				 infiniteFlag: false,
				 emphasize: 'success',
				 value:10
			 }).inject(that.progressBarContainer);
		},

		removeProgressBar: function(value){
     var that = this;
			if(value){
 				this.progressbar.value = 100;
					setTimeout(function(){
						if(that.container && that.container.getElementsByClassName('progress-bar').length >0){
								that.container.getElement('.progress-bar').remove();
						}
					},1000);
		 }else{
			 if(that.container && that.container.getElementsByClassName('progress-bar').length >0){
				   that.container.getElement('.progress-bar').remove();
			 }
		 }
		},

		deleteFileAction: function (data, action) {
			var that = data;
			var docId = that.dataJSON.id;
			var fileId = '';
		//	var fileName = '';
			var versions = '';
			var multifiles = false;
		//	var reserved = true;
			var loggedInUser = '';
			var owner = that.dataJSON.relateddata.ownerInfo[0].dataelements['name'];
			var access = false;
			if (that.dataJSON.relateddata.files.length > 0) {
				fileId = that.dataJSON.relateddata.files[0].id;
			//	fileName = that.dataJSON.relateddata.files[0].dataelements.title;
				versions = that.dataJSON.relateddata.files[0].relateddata.versions;
				if (that.dataJSON.relateddata.files.length > 1) {
					multifiles = true;
				}
				i3DXCompassPlatformServices.getUser({
					onComplete: function (_data) {
						loggedInUser = _data.id;
					}
				});
				if (owner !== '' && owner === loggedInUser) {
					access = true;
				}
				if (multifiles) {
					xNotificationsUtil.showError(Document_NLS.DOC_Multi_Files);
					Mask.unmask(that.container);
				} else if (!access) {
					xNotificationsUtil.showError(Document_NLS.DOC_No_Access);
					Mask.unmask(that.container);

				} else {
					if (action === 'DeleteLatestVersion') {
						xNotificationsUtil.showError(Document_NLS.DOC_Files_delete_failure);

					} else if (action === 'DeleteAllVersionsButLast') {
						if (versions.length < 1) {
							xNotificationsUtil.showError(Document_NLS.DOC_DELETE_No_Versions);
							Mask.unmask(that.container);
						} else {
							var length = versions.length;
							that.deleteFile(docId, fileId, versions, length);

						}
					} else if (action === 'DeleteAllVersions') {
						if (fileId === undefined || fileId === '') {
							xNotificationsUtil.showError(Document_NLS.DOC_DELETE_No_Files);
							Mask.unmask(that.container);
						} else {
							var optionsDoc = {
								onComplete: function (output) {
									var docOpt = {
										getVersions: true,
										onComplete: function (output) {
											var data = output.data;
											if (data) {
												if (UWA.is(data, 'array')) {
													data = data[0];
												}
											}
											that.render(data);
											xNotificationsUtil.showSuccess(Document_NLS.DOC_Files_delete_success);

										},
										onFailure: function () {

										}
									};
									DocumentManagement.getDocuments([docId], docOpt);
									//		xNotificationsUtil.showSuccess(Document_NLS.DOC_Files_delete_success);

								},
								onFailure: function () {
									xNotificationsUtil.showError(Document_NLS.DOC_Files_delete_failure);
								Mask.unmask(that.container);
								}
							};

							DocumentManagement.deleteFile(docId, fileId, optionsDoc);
						}
					} else {
						xNotificationsUtil.showError(Document_NLS.DOC_Files_delete_failure);
								Mask.unmask(that.container);
					}
				}

			} else {
				xNotificationsUtil.showError(Document_NLS.DOC_DELETE_No_Files);
						Mask.unmask(that.container);
			}
		},

		deleteFile: function (docId, fileId, versions, length) {
			var that = this;
			var versionId = versions[length - 1].id;
			var optionsDoc = {
				onComplete: function () {
					length = length - 1;
					if (length > 0) {
						that.deleteFile(docId, fileId, versions, length);
					} else {
						xNotificationsUtil.showSuccess(Document_NLS.DOC_Files_delete_success);
						Mask.unmask(that.container);
						var docOpt = {
							getVersions: true,
							onComplete: function (output) {
								var data = output.data;
								if (data) {
									if (UWA.is(data, 'array')) {
										data = data[0];
									}
								}
								that.render(data);

							},
							onFailure: function () {}
						};
						DocumentManagement.getDocuments([docId], docOpt);
					}

				},
				onFailure: function () {
					xNotificationsUtil.showError(Document_NLS.DOC_Files_delete_failure);
				}
			};
			//	DocumentManagement.getDocuments([docId], docOpt);
			DocumentManagement.deleteVersion(docId, fileId, versionId, optionsDoc);

		},

		uploadFiles: function (data) {
			var that = data;
			// if(data.reservedby !=undefined && data.reservedby==''){
			var fileInput = UWA.createElement('input', {
					type: 'file',
					name: 'files',
					events: {
						change: function () {
							that.tablePresenter.uploadDocument(that.docId, this.files);
							//	that.uploadDocument(that.docId, this.files);
						}
					}
				});
			fileInput.click();
		}
	});
});
