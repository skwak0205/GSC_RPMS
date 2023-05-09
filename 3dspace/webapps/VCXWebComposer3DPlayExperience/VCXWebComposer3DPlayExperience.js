define("DS/VCXWebComposer3DPlayExperience/VCXWebPlayerIn3DPlayExperience",["DS/3DPlayExperienceModule/PlayExperience3D","UWA/Core","DS/VCXWebComposer/VCXWebAppHelper","DS/VCXWebComposer/VCXWebDictionary","DS/VCXWebDressup/VCXWebDressupDictionary","DS/VCXWebSMGComponents/VCXSMGDictionary","DS/VCXWebDigger/VCXWebDiggerDictionary","DS/WAFData/WAFData"],function(e,t,s,i,r,n,a,o){"use strict";return e.extend({init:function(e){this._parent(e);var i=s.getOptions();e.commandApplication=!1;var r={composerExp:{workbenchs:[{name:"VCXWebPlayer3DPlayExperience",module:"VCXWebComposer3DPlayExperience"}],visu:i.viewerOptions,ui:{displayTree:!1,displayActionBar:!1}}}.composerExp;this.args=t.extend(e,r,!0)},dispose:function(e){this._parent(e);var t=this._experienceBase;t&&t.webApplication&&t.Clean()},loadAsset:function(e){if(e){var s=e.asset?e.asset:this.args.input.asset;if(s){var i=!1,r=e=>{},n=()=>{console.error("Error in WAFData request")},a=e=>{var s=this._experienceBase,r=null;s&&(s.webApplication&&s.webApplication.requestNewExperienceBaseAsync().then(s=>{(r=s.getManager("VCXGUIManager").getGettingStartedUI())&&r.startLoadingExperience();var n,a=s.getManager("VCXContextManager").getMainViewer();n=i?"DS/VCXWebInteropSMG/VCXSmgXMLExperienceLoader":"DS/VCXWebInteropSMG/VCXSmgLoader",require([n],t=>{var n=new t;n._experienceProgressCB=this.onModelLoadingProgression,this.onModelLoadingStarted(),this.autoReframe=!1;var o=()=>{this.onModelLoadingCompleted(),r&&r.endLoadingExperience(),n._experienceProgressCB=null};i?n.StartLoadingExperience(s,a,p,o):n.LoadFromArrayBuffer(s,a,e,o)},e=>t.Promise.reject(e))}))},p="";if("FILE"===s.provider)if(s.format&&"smg"===s.format.toLowerCase()||s.mimetype&&"smg"===s.mimetype.toLowerCase()||s.format&&"smgs"===s.format.toLowerCase()||s.mimetype&&"smgs"===s.mimetype.toLowerCase()||s.format&&"smgxml"===s.format.toLowerCase()||s.mimetype&&"smgxml"===s.mimetype.toLowerCase()){p=s.filename||s.url,(s.format&&"smgxml"===s.format.toLowerCase()||s.mimetype&&"smgxml"===s.mimetype.toLowerCase())&&(i=!0);var l="";s.requestsOptions&&s.requestsOptions.authentication&&(l=s.requestsOptions.authentication),o.handleRequest(p,{method:"GET",authentication:l,responseType:"arraybuffer",type:"arraybuffer",onComplete:a,onProgress:r,onFailure:()=>{o.handleRequest(p,{method:"GET",authentication:"passport",responseType:"arraybuffer",type:"arraybuffer",onComplete:a,onProgress:r,onFailure:n})}})}else alert("Format not supported:"+s.format);else if("BUFFER"===s.provider)a(s.data);else if("STREAM"===s.provider){var c=s.data[0];a(c.data)}else alert("Hoops it seems that the data is not an smg file")}}},addDictionaries:function(){return t.Promise.resolve().then(()=>this.AddDictionaryDescription(i)).then(()=>this.AddDictionaryDescription(r)).then(()=>this.AddDictionaryDescription(n)).then(()=>this.AddDictionaryDescription(a))},onPreCreate:function(e){},AddDictionaryDescription:function(e){if(e&&e.getNLSModules&&e.getNLSModules()&&(this._nlsToLoad||(this._nlsToLoad=[]),this._nlsToLoad=this._nlsToLoad.concat(e.getNLSModules())),e&&e.GetDictionary&&e.GetDictionary())return this._experienceBase.AddDictionaryDescription(e.GetDictionary())},onPostCreate:function(e){},initExperienceBase:function(e,t){return this._experienceBase=e,this.addComposerManagers().loadNLS(this._nlsToLoad).then(e=>{t&&(console.warn("Should not be here : VCXWebPlayerIn3DPlayExperience.initExperienceBase"),t(0))})},addComposerManagers:function(){var e=this._experienceBase.Factory.BuildComponent("VCXExperience");e.SetID("VCXExperienceID"),e.SetPersistency(1),this._experienceBase._ManagerSet.addManager("VCXExperience",e);var t=this._experienceBase.getManager("VCXContextManager");if(t){t.setRootOptions({componentType:"VCXComponentOccurrenceSMG"}),t.setPlayerMode(),t.setExperienceIn3DPlay(),t.useTraverseGraph=!0;var s=void 0;this.args&&this.args.input&&this.args.input.asset&&(this.args.input.asset.originalAsset?"DriveFile"!==(s=this.args.input.asset.originalAsset.dtype)&&"Document"!==s&&"XCADNonPSBaseRepReference"!==s||t.setExperienceInConnectedPlayer():this.args.input.asset.tenant?t.setExperienceInConnectedPlayer():this.args.input.asset.requestsOptions&&t.setExperienceInConnectedPlayer()),this.args&&this.args.options&&!0===this.args.options.ODTModeRunAnimation&&t.setExperienceInConnectedPlayer(),t.setTrapSelectionRequirements({pick:!0,prePick:!0}),this.args&&this.args.options&&("false"===this.args.options.smgUI&&t.setSupported("DisplayUI",!1),"false"===this.args.options.smgUIAssemblyPanel&&t.setSupported("DisplayUIAssemblyPanel",!1),"false"===this.args.options.smgUIDressupsPanel&&t.setSupported("DisplayUIDressupsPanel",!1),"false"===this.args.options.smgUIScenariosPanel&&t.setSupported("DisplayUIScenariosPanel",!1),"false"===this.args.options.smgUIPropertiesPanel&&t.setSupported("DisplayUIPropertiesPanel",!1),"false"===this.args.options.smgPaper&&t.setSupported("Paper",!1),"true"===this.args.options.smgPaperSpaceVisible&&t.setSupported("PaperVisible",!0),1!==this.args.options.smgScenarioPanelExpand&&0!==this.args.options.smgScenarioPanelExpand||(t.setSupported("PropAndScnPanelExpandOverride",!0),1===this.args.options.smgScenarioPanelExpand?t.setSupported("ScenarioPanelExpand",!0):t.setSupported("ScenarioPanelExpand",!1)),1!==this.args.options.smgPropertiesPanelExpand&&0!==this.args.options.smgPropertiesPanelExpand||(t.setSupported("PropAndScnPanelExpandOverride",!0),1===this.args.options.smgPropertiesPanelExpand?t.setSupported("PropertiesPanelExpand",!0):t.setSupported("PropertiesPanelExpand",!1))),t.getExperienceInConnectedPlayer()?t.setSupported("ImmersiveNavigation",!0):(t.setSupported("ImmersiveNavigation",!1),t.setSupported("DisplayPaperCtrlBtns",!1)),t.setSupported("Timeline"),t.setSupported("OpacityOverride",!1),t.setSupported("FrameUnboxing",!1)}e.SetPreviewMode(!0),e.addApplicationManagers(),this._experienceBase.getManager("VCXManipulatorManager").setManipulatorsRequirements({active:!0});return this._experienceBase.getManager("VCXVisuManager").setDefaultAmbienceOptions({requestedMap:"defaultAmbienceEL",baseModule:"VCXWebVisualization",baseFolder:"ambiences/",hasIBL:!0,hasRoughness:!0,hasBackGround:!1,hasGround:!1}),e},onFinished:function(e){WUX.setFullscreen(),this.options&&this.options.onAppInitialize&&(this.options.onAppInitialize(this,this._experienceBase),this.options.onAppInitialize=null),this._experienceBase&&this._experienceBase._ManagerSet&&this._experienceBase._ManagerSet.getVersions&&console.log(this._experienceBase._ManagerSet.getVersions())}})});