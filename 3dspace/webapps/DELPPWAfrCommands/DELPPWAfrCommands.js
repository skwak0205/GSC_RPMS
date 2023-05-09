define("DS/DELPPWAfrCommands/AfrCommand",["DS/ApplicationFrame/Command","DS/Logger/Logger","DS/Core/Events","UWA/Class/Options"],function(e,t,n,i){"use strict";var o=e.extend(i,{name:"AfrCommand",_logger:null,_context:null,init:function(e){this._logger=t.getLogger(o),this._parent(e,{mode:"exclusive",isAsynchronous:!1}),this._context=this.getOption("context")},execute:function(){var e={arguments:arguments};this._logger.log(this._id+" execute"),n.publish({event:"DELPPWCommands/"+this._id,data:e})}});return o}),define("DS/DELPPWAfrCommands/InsertCmd",["DS/ApplicationFrame/Command","DS/Logger/Logger","DS/Core/Events","UWA/Class/Options"],function(e,t,n,i){"use strict";var o=e.extend(i,{name:"InsertCmd",_logger:null,_context:null,init:function(e){this._logger=t.getLogger(o),this._parent(e,{mode:"exclusive",isAsynchronous:!1}),this._context=this.getOption("context")},execute:function(){var e={arguments:this.getArguments()};this._logger.log(this._id+" execute"),n.publish({event:"DELPPWCommands/InsertCmd",data:e})}});return o}),define("DS/DELPPWAfrCommands/FilterCommands/ProxyXSO",["UWA/Core","DS/Selection/XSO"],function(e,t){"use strict";return t.extend({init:function(e){this._parent(e)},onSelectionChange:function(t){var n;e.is(t)&&e.is(t.changed)&&(n=t.changed.nodeId,t.changed.isSelected?this.add(n):this.remove(n))}})}),define("DS/DELPPWAfrCommands/CheckCommand",["UWA/Core","DS/Core/Events","DS/Logger/Logger","DS/ApplicationFrame/CommandCheckHeader","DS/ApplicationFrame/CommandsManager"],function(e,t,n,i,o){"use strict";var s=i.extend({name:"CheckCommand",commandName:null,_logger:null,_checkGroup:null,init:function(t){var i,o;this._parent(t,{mode:"exclusive",isAsynchronous:!1}),this.commandName=t.ID,this._logger=n.getLogger(s),o=t.arguments,i=(Array.isArray(o)?o:[]).reduce((e,{ID:t,Value:n})=>e.set(t,n),new Map),widget.getBool("isViewPersistanceOn")?this._executeViewPersistence(this.commandName):function(t){var n=t.get("SimpleCheck");return e.is(n)&&"SimpleCheck"===n}(i)||e.is(t.isEnabled)&&this.setState(t.isEnabled),this._checkGroup=i.get("CheckGroup"),this.onStateChange(this._stateChanged)},_stateChanged:function(){var n,i=this.getState();!0===i&&(n=this.options.node),this.options.node=null,!function(t){var n,i=t.getState(),s=!0;return e.is(t._checkGroup)&&!1===i&&(n=o.getCommandCheckHeaders(t.options.context),s=Object.keys(n).some(function(e){var i=n[e];return i._checkGroup===t._checkGroup&&!0===i.getState()})),s}(this)?(this.toggleState(),widget.setValue(this._getPreferenceName(this.commandName),!i)):(widget.setValue(this._getPreferenceName(this.commandName),i),t.publish({event:"DELPPWCommands/"+this._id,data:{state:i,node:n}}))},_getPreferenceName:function(t){return function(t){var n=e.is(widget.getValue("isMFNToggleAuxiliaryViewVisible"))?"MFN":"MGA";return"ToggleAuxiliaryView"===t?"is"+n:"is"}(t)+t+"Visible"},_executeViewPersistence:function(e){var t;["ItemsView","ResourcesView","ProcessView","ManufacturingItems","ProductsParts","ToggleAuxiliaryView"].includes(e)&&(t=widget.getBool(this._getPreferenceName(e)),this.setState(t),t||this._stateChanged())}});return s}),define("DS/DELPPWAfrCommands/FilterCommands/ConfigCommandUtil",["UWA/Core"],function(e){"use strict";const t=function(t){return{isRoot:function(t){let n=!0;const i=t.instance;if(e.is(i.id)){const e=i.absolutePath;Array.isArray(e)&&e.length>0&&(n=!1)}return n},getSelections:function(n=!1){return e.is(t.getSelectedNodesInfo,"function")?t.getSelectedNodesInfo(n):[]},getData:function(){const e=this;return{selectedNodes:this.getSelections(!1).map(function(t){let n;const i=e.isRoot(t);return n=i?{id:t.reference.id,alias:t.label,isRoot:i}:{id:t.instance.id,alias:t.instance.label,isRoot:i,VPMRef:t.reference.type,parentID:t.parent.reference.id,parentalias:t.parent.label}})}},getEffectivityModificationEventName:function(){return"cfgEffectivityModifications_"+widget.id},getRelidsAndCommandName:function(t,n){let i=[],o=null;if(e.owns(t,"commandName")){const s=t.commandName;if(e.is(s,"string")&&n(s)&&(o=s,e.owns(t,"response"))){const n=t.response.results;Array.isArray(n)&&(i=n.reduce((t,n)=>{if(e.owns(n,"existing")){const i=n.existing.pid;e.is(i)&&t.push(i)}return t},[]))}}return{relids:i,cfgCommandName:o}},getPidsAndCommandName:function(t,n){let i=[],o=null;if(e.owns(t,"commandName")){const s=t.commandName;if(e.is(s,"string")&&n(s)&&(o=s,e.owns(t,"data"))){const e=t.data.pids;Array.isArray(e)&&(i=e)}}return{pids:i,cfgCommandName:o}}}};return function(){let n;return{getInstance:function(i){return e.is(n)||(n=new t(i)),n}}}()}),define("DS/DELPPWAfrCommands/RelationalExplorerCmd",["UWA/Core","UWA/Utils","DS/Logger/Logger","DS/ApplicationFrame/Command","UWA/Utils/InterCom"],function(e,t,n,i,o){"use strict";var s=null,a=i.extend({name:"RelationIPExplorer",_logger:null,_context:null,init:function(e){var t=this;this._parent(e,{mode:"shared",isAsynchronous:!0}),this._logger=this._logger?this._logger:n.getLogger(a),this._context=e.context,t.enable()},setObjectOnCompass:function(t){var n=e.is(widget)&&widget.id?"padcompassSocket_"+widget.id:"padcompassSocket",i=null;if(this._logger.log("setObjectOnCompass"),!e.is(t.nodesToOpen,"array"))throw new Error("setObjectOnCompass: Invalid init parameter nodesToOpen");null===s&&(s=new o.Socket(n)).subscribeServer("com.ds.compass",window.parent),0===t.nodesToOpen.length?s.dispatchEvent("onResetX3DContent",{},n):(i=this.dropdataFromSelectedNodes(t.nodesToOpen),s.dispatchEvent("onSetX3DContent",i,n))},openApp:function(t,n){var i=e.is(widget)&&widget.id?"padcompassSocket_"+widget.id:"padcompassSocket";this._logger.log("open_app"),null===s&&(s=new o.Socket(i)).subscribeServer("com.ds.compass",window.parent),s.addListener("launchApp",function(t){e.is(n,"function")&&n(t)}),s.dispatchEvent("onLaunchApp",t,i)},dropdataFromSelectedNodes:function(t){var n,i="",o={protocol:"3DXContent",version:"1.1",source:widget.data.appId,widgetId:widget.id,data:{items:[]}},s=this._context.getSecurityContext();return e.is(s)&&e.is(s.SecurityContext,"string")&&(i=s.SecurityContext.split("ctx::").pop()),n=function(e){return{envId:s.tenant,serviceId:"3DSpace",contextId:i,objectId:e.getID(),objectType:e.options.type,displayName:e.options.display?e.options.display:e.getLabel(),displayType:e.options.grid["ds6w:type"],path:e.getOccurencePathWithType?e.getOccurencePathWithType():null}},t.forEach(function(e){o.data.items.push(n(e))}),o},execute:function(){var e=this._context.getSelectedNodes();this._logger.log("Execute"),this.setObjectOnCompass({nodesToOpen:e}),this.openApp({appId:"ENORIPE_AP"},function(){})}});return a}),define("DS/DELPPWAfrCommands/FilterCommands/EditConfigContextCmd",["UWA/Core","DS/CfgConfigurationCommands/commands/CfgEditConfigurationContextCmd","DS/PlatformAPI/PlatformAPI","DS/DELPPWAfrCommands/FilterCommands/ConfigCommandUtil"],function(e,t,n,i){"use strict";return t.extend({init:function(t){let o=this;o._context=t.context,o._configCmdUtil=i.getInstance(o._context);const s=o._configCmdUtil.getEffectivityModificationEventName();n.subscribe(s,function(t){if(e.is(t)){const e=JSON.parse(t),{pids:n,cfgCommandName:i}=o._configCmdUtil.getPidsAndCommandName(e,e=>"cfgEditConfigurationContext"===e);if(n.length>0){const e={pids:n,relids:[]};o._context.updateCfgColumnsBasedOnCommand(e,i)}}}),this._parent(t)},getData:function(){const e=this;return{selectedNodes:this._configCmdUtil.getSelections(!1).map(function(t){let n;const i=e._configCmdUtil.isRoot(t);return n=i?{id:t.reference.id,alias:t.label,isRoot:i}:{id:t.reference.id,alias:t.label,isRoot:i,parentID:t.parent.reference.id,parentalias:t.parent.label}})}}})}),define("DS/DELPPWAfrCommands/FilterCommands/EditEvolutionCmd",["UWA/Core","DS/CfgEffectivityCommands/commands/CfgEvolutionCmd","DS/PlatformAPI/PlatformAPI","DS/DELPPWAfrCommands/FilterCommands/ConfigCommandUtil"],function(e,t,n,i){"use strict";return t.extend({init:function(t){let o=this;o._context=t.context,o._configCmdUtil=i.getInstance(o._context);const s=o._configCmdUtil.getEffectivityModificationEventName();n.subscribe(s,function(t){if(e.is(t)){const e=JSON.parse(t),{relids:n,cfgCommandName:i}=o._configCmdUtil.getRelidsAndCommandName(e,e=>"cfgEditEvolution"===e);if(n.length>0){const e={pids:[],relids:n};o._context.updateCfgColumnsBasedOnCommand(e,i)}}}),o._parent(t)},getData:function(){return this._configCmdUtil.getData()}})}),define("DS/DELPPWAfrCommands/FilterCommands/ExpandFilterCmd",["UWA/Core","DS/ENONextGenFilterCmd/commands/ENONextGenFilterCmd","DS/DELPPWAfrCommands/FilterCommands/ConfigCommandUtil","DS/DELPPWAfrCommands/FilterCommands/ProxyXSO"],function(e,t,n,i){"use strict";return t.extend({_context:null,_proxyXSO:null,_configCmdUtil:null,init:function(e){this._context=e.context?e.context:null,this._configCmdUtil=n.getInstance(this._context),this._initProxyXSO(),this._parent(e)},_initProxyXSO:function(){var t=this;this._proxyXSO=new i,e.is(this._context.subscribeSelectionChange,"function")&&this._context.subscribeSelectionChange(function(e){t._proxyXSO.onSelectionChange(e)})},getAppParams:function(){return{widgetId:this._context.widget.getId(),appId:this._context.widget.data.app3DSharedId}},getSelectorManager:function(){return this._proxyXSO},getRoots:function(){var e=this;return e._configCmdUtil.getSelections(!1).reduce(function(t,n){return e._configCmdUtil.isRoot(n)&&t.push({id:n.reference.id,label:n.label}),t},[])},getSelection:function(){var e=[],t=[];return this._configCmdUtil.getSelections(!0).forEach(function(n){e.push(n.occPathRIR),t.push([n.label])}),{path:e,label:t}}})}),define("DS/DELPPWAfrCommands/FilterCommands/EditVariantCmd",["UWA/Core","DS/CfgEffectivityCommands/commands/CfgVariantEffectivityCmd","DS/PlatformAPI/PlatformAPI","DS/DELPPWAfrCommands/FilterCommands/ConfigCommandUtil"],function(e,t,n,i){"use strict";return t.extend({init:function(t){let o=this;o._context=t.context,o._configCmdUtil=i.getInstance(o._context);const s=o._configCmdUtil.getEffectivityModificationEventName();n.subscribe(s,function(t){if(e.is(t)){const e=JSON.parse(t),{relids:n,cfgCommandName:i}=o._configCmdUtil.getRelidsAndCommandName(e,e=>"cfgMassEditVariant"===e||"cfgEditVariant"===e);if(n.length>0){const e={pids:[],relids:n};o._context.updateCfgColumnsBasedOnCommand(e,i)}}}),this._parent(t)},getData:function(){return this._configCmdUtil.getData()}})});