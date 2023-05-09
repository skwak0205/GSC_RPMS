/*!  Copyright 2019 Dassault Systemes. All rights reserved. */
!function(){"use strict";function e(e){return e.pimCtrl&&e.pimCtrl.clearIsr(),Object.getPrototypeOf(e.exp).clearView.apply(e.exp,e.args)}function t(e,t,i,r){var n;e&&t&&i&&(n=["onIsrAdded","onItfAdded"].map(function(t){return e.subscribe(t,a)}),e.load(i));function a(){n.forEach(e.unsubscribe,e),n=void 0,window.widget&&t.pad3DViewer.getContentName({callbacks:{onComplete:function(e){window.widget.setTitle(e.contentName)},onFailure:function(){}}}),setTimeout(function(){t.publish("ASSET/LOADINGFINISHED",{loadAssetInfo:r.asset})},10)}}function i(i){var r,n,a=i.exp;a.clearView=function(){return e({exp:a,pimCtrl:r,args:arguments})},a.dispose=function(){var t=function(t){var i;if(t.args&&!0===t.args.disposeFrameWindow?(e(t),t.pimCtrl&&t.pimCtrl.dereference()):t.pimCtrl&&t.pimCtrl.deactivate(),t.pimExpPref&&t.exp&&t.exp.pad3DViewer&&require("DS/CATWebUXPreferences/CATWebUXPreferencesManager").unreferencePreferenceManager({context:t.exp.pad3DViewer.getFrameWindow()}),window.widget&&window.widget.setMetas({helpPath:void 0}),t.args&&!0!==t.args.disposeFrameWindow){var r=t.exp.pad3DViewer.getCommandContext();i=Object.getPrototypeOf(t.exp).dispose.apply(t.exp,t.args);var n=require("DS/ApplicationFrame/CommandsManager").getCommandCheckHeaders(r);n&&Object.keys(n).forEach(function(e){delete n[e]})}else i=Object.getPrototypeOf(t.exp).dispose.apply(t.exp,t.args);return i}({exp:a,pimCtrl:r,pimExpPref:n,args:arguments});return r=n=null,t},a.refresh=function(){return e={exp:a,pimCtrl:r,args:arguments},Object.getPrototypeOf(e.exp).refresh.apply(e.exp,e.args);var e},a.onPostCreate=function(){return r||require(["DS/PIMwebViewController/PIMwebItfCtrl"],function(e){(r=e.getItfCtrl({pad3DViewer:a.pad3DViewer})).activate({readOnly:!0,noStatusBarCmd:!0})}),(t=(e={exp:a,pimCtrl:r,args:arguments}).exp.pad3DViewer)&&(require("DS/PADUtils/PADSettingsMgt").setSetting("settypes",!0),require("DS/ApplicationFrame/CommandsManager").setDefaultCommand({ID:"PIMwebDefaultHdr",className:"DS/PIMwebViewController/PIMwebDefaultCmd",context:t.getCommandContext()}),t.getViewer().setPicking({primitive:0,trapSelection:!0,trapPrimitive:!1,trapGPU:!0}),window.widget&&(window.widget.setMetas({helpPath:"PIMweb3DPlayExperience/assets/help"}),window.widget.dispatchEvent("onPlayExperienceReady",[{pad3DViewer:t}]))),Object.getPrototypeOf(e.exp).onPostCreate.apply(e.exp,e.args);var e,t},a.loadAsset=function(e){e&&e.asset&&e.asset.physicalid||window.console.log("No data provided!!"),r?(r.activate({readOnly:!0}),require("DS/PADUtils/PADUtilsServices").multiTenantMgt([{envId:e.asset.tenant,contextId:e.asset.contextid}],require("DS/ENOPAD3DViewer/utils/PAD3DViewerModelServices").getRoots(a.pad3DViewer),function(n){n.isMultiTenant||(i.isr?t(r,a,{isrID:e.asset.physicalid},e):t(r,a,{metricID:e.asset.physicalid},e))})):(a.pad3DViewer.setDefaultContextualMenuVisibility(!1),a.pad3DViewer.setDefaultContextualBarVisibility(!1),a.pad3DViewer.setOpenWithMenuAvailability(!1),require(["DS/PIMwebViewController/PIMwebItfCtrl"],function(t){r=t.getItfCtrl({pad3DViewer:a.pad3DViewer}),a.loadAsset(e)}))};var o=require("DS/ApplicationFrame/CommandsManager");o._cmdCheckHeader&&(o._cmdCheckHeader={}),i.options.options&&i.options.options.pad3DViewer&&require(["DS/CATWebUXPreferences/CATWebUXPreferencesManager"].concat([]),function(e){(n=e.getPreferenceManager({context:i.options.options.pad3DViewer.getFrameWindow()})).removeAllPreferences()}),i.options.commandApplication=!1;var s={};""!==window.location.search?require(["UWA/Core"].concat([]),function(e){e.extend(s,e.Utils.parseQuery(window.location.search)),s.widgetDomain&&(e.extend(s,e.Utils.parseQuery(s.widgetDomain)),i.options.workbenchs="__PIM_CFDB"in s?[{module:"PIMweb3DPlayExperience",name:"PIMweb3DPlayExperienceNext"}]:[{module:"PIMweb3DPlayExperience",name:"PIMweb3DPlayExperience"}])}):i.options.workbenchs=[{module:"PIMweb3DPlayExperience",name:"PIMweb3DPlayExperience"}]}define("DS/PIMweb3DPlayExperience/PIMwebIsr3DPlayExperience",["DS/3DPlayExperienceModule/PlayExperience3D"],function(e){return e.extend({init:function(e){i({exp:this,options:e,isr:!0}),this._parent(e)}})}),define("DS/PIMweb3DPlayExperience/PIMwebItf3DPlayExperience",["DS/3DPlayExperienceModule/PlayExperience3D"],function(e){return e.extend({init:function(e){i({exp:this,options:e,isr:!1}),this._parent(e)}})})}();