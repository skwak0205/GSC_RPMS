"use strict";define("DS/WAfrContainerAsWidget/WAfrContainerAsWidget",["UWA/Class/Promise","UWA/Data","DS/WAfrDebug/mod_Performances","WebappsUtils/WebappsUtils"],function(e,t,n,o){var r=1,i=n.Performances,a=new i,s=function(){this._container=null};return s.setDefaultVersion=function(e){r=e},s.getWidgetURL=function(){var e=widget.uwaUrl;return e||(e=window.location.href),e},s.getParametersInUrl=function(){var e,t=s.getWidgetURL();if(t){var n="?afrWebService",r=t.indexOf(n);if(r>0){var i=t.substring(r+n.length,t.length).split("/"),a=null;e={protocol:"http",host:"",port:"8080",service:null,appId:"",debug:"true",containerId:null};for(var d=0;d<i.length;d++)2===(a=i[d].split(":")).length&&(e[a[0]]=a[1]);var p=e.service?e.service+"/wafr/apps/":"";return e.protocol&&e.host&&(p=e.protocol+"://"+e.host+":"+e.port+"/"+p),{afrService:p,afrServicePort:e.port,appId:e.appId,debug:"true"===e.debug,containerId:e.containerId,forceProxy:"true"===e.forceProxy}}if(n=encodeURIComponent("forDebug:"),(r=t.indexOf(n))>0){var c=t.substring(r+n.length,t.length);return c=decodeURIComponent(c),JSON.parse(c)}if((r=t.indexOf("?"))>0){var l=t.substring(r+1);return e={conf:"",appId:"",debug:"true"},l.split("&").forEach(function(t){var n=t.split("=");e[n[0]]=decodeURIComponent(n[1])}),e.conf?{configUrl:o.getWebappsBaseUrl()+e.conf+"/assets/json/"+e.appId+"_conf.json",debug:e.debug}:{appId:e.appId,debug:"true"==e.debug,containerId:null,forceProxy:"true"===e.forceProxy}}}return null},s.loadWidget=function(){if(this.perfLoadWigToken=a.start("WAFR: load widget"),window.widget){var t=function(e,t){window.widget.launched?s.onReadyWidget().then(e,t):window.widget.onLoad=s.onReadyWidget};return new e(function(e,n){try{var r=widget.uwaUrl||o.getWebappsBaseUrl();if(!(widget.uwaUrl&&!r.startsWith("http://localhost")))return t(e,n),!0;require(["DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices","UWA/Data"],function(o,r){var i=window.widget.data.afrAppId?window.widget.data.afrAppId:window.widget.data.appId;"X3DPLAW_AP"===window.widget.data.appId&&"CAT3DPLAW_AP"===window.widget.data.afrAppId&&(i=window.widget.data.appId,window.widget.setValue("afrAppId",i)),o.getAppInfo({appId:i,onFailure:function(e){console.log(e)},onComplete:function(o){var i="";o.brand&&"string"==typeof o.brand&&"3DEXPERIENCE"!==o.brand&&"SOLIDWORKS"!==o.brand&&(i=o.brand),window.widget.setMetas({brand:i});let a=null;if(o.config)a=o.config;else for(var s=0;s<o.platforms.length;s++)if((o.platforms[s].id===o.platformId||o.platforms[s].id==widget.data.x3dPlatformId)&&o.platforms[s].config){a=o.platforms[s].config;break}var d;null!==a?(console.debug("[WebAfr] Application using WebAfr web service."),d=a,r.request(d,{type:"json",responseType:"json",onComplete:function(o){for(var r in o)window.widget.setValue(r,o[r]);return t(e,n),!0}})):(console.debug("[WebAfr] Application NOT using WebAfr web service."),delete widget.data.afrService,t(e,n))}})})}catch(e){throw new Error("failed to start the widget : "+e)}return!0})}throw new Error("widget do not exist ")},s.onReadyWidget=function(n){a.stop(this.perfLoadWigToken);var o=s.getParametersInUrl();return o?o.configUrl?new e(function(e,r){t.request(o.configUrl,{type:"json",responseType:"json",onComplete:function(t){t.debug=o.debug,t.forceProxy=n&&n.forceProxy,s.loadContainer(t).then(e,r)},onFailure:r})}):s.loadContainer(o):s.loadContainer()},s.createContainer=function(e){var t=new e.containerViewCtor({debug:e.debug,version:e.version,config:e.config});e.containerOptions.view=t,widget.body.empty(!0),t.inject(widget.body);var n=new e.containerCtor(e.containerOptions);return widget.setTitle(""),widget.setTitle=function(e){n.updateTitle({title:e})},widget.getTitle=function(){return n.productTitle},e.config&&t.setValues(e.config),n},s.loadContainer=function(t,n){n||(n=r);var i="DS/WAfrContainer/Container",a=!1;return t&&(a=t.debug,t.debug&&(t.customAfrContainer="DS/AppsTester/Container",delete t.debug),t.customAfrContainer&&(i=t.customAfrContainer,delete t.customAfrContainer),t.containerId&&(widget.id=t.containerId)),new e(function(e,r){require([i,"DS/WAfrContainerAsWidget/ContainerView",n>=2?"DS/WAfrContainerAsWidget/default_config":"DS/WAfrContainerAsWidget/old_config"],function(i,d,p){var c=widget.uwaUrl||o.getWebappsBaseUrl(),l=widget.uwaUrl&&!c.startsWith("http://localhost"),m=s.createContainer({containerViewCtor:d,containerCtor:i,containerOptions:{config:p,baseUrl:c,proxified:l,forceProxy:!(!t||!t.forceProxy)&&t.forceProxy},version:n,config:t,debug:a});m.loadCurrentConfig().then(function(){e(m)},r)},r)})},s}),define("DS/WAfrUIStandardComponents/mod_TreeModel",[],function(){class e{constructor(e){this._model=e,this._idx=e._private._childrenOrder.length-1}next(){-1!==this._idx&&(--this._idx,-1===this._idx&&(this._model=null))}isValid(){return this._model&&-1!=this._idx}get(){if(!this.isValid())throw new Error("This iterator is invalid.");let e=this._model._private._childrenOrder[this._idx];return this._model.getChild(e)}}class t{constructor(e){this._model=e,this._idx=0,0===e._private._childrenOrder.length&&(this._idx=-1)}next(){-1!==this._idx&&(++this._idx,this._model._private._childrenOrder.length<=this._idx&&(this._idx=-1,this._model=null))}isValid(){return this._model&&-1!=this._idx}get(){let e=this._model._private._childrenOrder[this._idx];return this._model.getChild(e)}}class n{constructor(e){if(this._private=Object.create(null),this._private._children={},this._private._childrenOrder=[],!e.identifier)throw new Error("data must have an identifier property");this._private._data=e}getId(){return this._private._data?this._private._data.identifier:null}getData(){return this._private._data}addChild(e){let t=new n(e);return this._private._children[e.identifier]=t,this._private._childrenOrder.push(t.getId()),t}addChildNode(e){if(!(e instanceof n))throw new Error("childNode must be a TreeModel");let t=e.getId();if(!t)throw new Error("id must be defined and not empty");this._private._children[t]=e,this._private._childrenOrder.push(t)}getChild(e){return this._private._children[e]}getForwardIterator(){return new t(this)}getBackwardIterator(){return new e(this)}reorder(e){let t=!0;const n=this._private._childrenOrder;for(let o=0,r=e.length;o<r;++o)if(!n.includes(e[o])){t=!1;break}return t&&(n.length===e.length?this._private._childrenOrder=e:t=!1),t}}return{TreeModel:n}}),define("DS/WAfrUIStandardComponents/UIFrame",["UWA/Core","UWA/Element","DS/Controls/LayerSet","DS/Windows/ImmersiveFrame","DS/Utilities/TouchUtils","DS/Utilities/Css"],function(e,t,n,o,r,i,a){var s=function(t){this.options=e.clone(e.extend({hasStatusBar:!1},t)),this.elements={},window.UIFRAME_DEBUG=this};return s.prototype._setHasStatusBar=function(e){this.options.hasStatusBar=e,this.elements.container&&(this.options.hasStatusBar?this.elements.container.addClassName("wafr-uiframe-statusbar"):this.elements.container.removeClassName("wafr-uiframe-statusbar"))},s.prototype._buildView=function(){var t=this;this.elements.container=e.Element.create("div",{class:"wafr-uiframe wux-afr-framewindow wux-ui-ambiance-light"}),this.options.hasStatusBar&&this.elements.container.addClassName("wafr-uiframe-statusbar"),this._onTouchChange=function(){r.getTouchMode()?this.elements.container.addClassName("wafr-uiframe-touch"):this.elements.container.removeClassName("wafr-uiframe-touch")}.bind(this),document.addEventListener("touchChange",this._onTouchChange),this.elements.viewerFrame=e.Element.create("div",{styles:{width:"100%",height:"100%"}}),this.elements.uiFrame=e.Element.create("div",{class:"wux-afr-framewindow-uiframe",dataType:"UI-Frame"}),this.elements.uiFrame3d=e.Element.create("div",{class:"wux-afr-framewindow-uiframe",dataType:"UI-Frame-3D"}),this.elements.layerSet=new n,this.elements.layerSet.inject(this.elements.container),this.elements.layerSet.addLayer(this.elements.viewerFrame).setAttribute("data-type","viewer"),this.elements.layerSet.addLayer(this.elements.uiFrame3d,{top:0,bottom:0,left:0,right:0,pointerEvents:"none",zIndex:2e4}).setAttribute("data-type","ui-frame-3d"),this.elements.layerSet.addLayer(this.elements.uiFrame,{top:0,bottom:0,left:0,right:0,pointerEvents:"none",zIndex:3e4,overflow:"auto"}).setAttribute("data-type","ui-frame-2d"),setTimeout(function(){t.elements.container&&t.elements.container.addClassName("wux-ui-at-screen")},0);var a=this.getExecutionContext();this.elements._immersiveFrame=new o({identifier:a.getSourceApp()&&a.getSourceApp().getId()?a.getSourceApp().getId():a.getId(),_ActionBar_V3:!0,dockingElementsPriority:[WUXDockAreaEnum.TopDockArea,WUXDockAreaEnum.BottomDockArea,WUXDockAreaEnum.LeftDockArea,WUXDockAreaEnum.RightDockArea]}),this.elements._immersiveFrame.inject(this.elements.uiFrame),i.disableSelection(this.elements.container),i.disableSelection(this.getUIFrame()),this.elements._statusBarWrapper=new e.Element("div",{class:"wafr-statusbar-wrapper"}),this.elements._statusBarWrapper.classList.add("wafr-statusbar-wrapper"),this.elements._statusBarWrapper.inject(this.elements.container)},s.prototype.initialize=function(){var t=this.getExecutionContext();if(!t)throw new Error("Execution context cannot be null here");return this.elements.container||(this._buildView(),this.elements.container.inject(t.getUiContainer())),e.Class.Promise.resolve()},s.prototype._enableBusyMode=function(){this.elements.container.addClassName("wux-ui-state-busy")},s.prototype._disableBusyMode=function(){this.elements.container.removeClassName("wux-ui-state-busy")},s.prototype.getUIFrame=function(){return this.elements.uiFrame},s.prototype.getContainer=function(){return this.elements.container},s.prototype.isDisplayedActionBar=function(){return!1},s.prototype.getImmersiveFrame=function(){return this.elements._immersiveFrame},s.prototype.getExposedComponentCtor=function(){var e=function(){},t=this;return e.prototype.getImmersiveFrame=function(){return t.getImmersiveFrame()},e.prototype.getUIFrame=function(){return t.getUIFrame()},e.prototype.getViewerLayout=function(){return t.elements.viewerFrame},e.prototype.isDisplayedActionBar=function(){return t.isDisplayedActionBar()},e.prototype.addLayer=function(e,n){return t.elements.layerSet.addLayer(e,n)},e.prototype.addRootClassName=function(e){t.elements.container.addClassName(e)},e.prototype._privateGetStatusBarWrapper=function(){return t.elements._statusBarWrapper},e.prototype._privateSetStatusBarWrapperVisibility=s.prototype._privateSetStatusBarWrapperVisibility.bind(this),e},s.prototype._privateSetStatusBarWrapperVisibility=function(e){if(null==e||"boolean"!=typeof e)throw new Error("Invalid value");if(!this.elements.container.hasClassName("wafr-uiframe-statusbar"))throw new Error("Impossible to change the status bar visibility on an UIFrame which does not contain a status bar.");e?this.elements.container.removeClassName("wafr-statusbar-wrapper-hidden"):this.elements.container.addClassName("wafr-statusbar-wrapper-hidden")},s.prototype.destroy=function(){this.elements._immersiveFrame.destroy(),this.elements.container.destroy(),document.removeEventListener("touchChange",this._onTouchChange)},s.DEFAULT_COMPONENT_NAME="AFR_UIFrameManager",s});var WAfrStandardComponentKey={AppManager:"AFR_AppManager",ModelEvents:"AFR_ModelEvents",NotificationManager:"AFR_NotificationManager"};Object.freeze(WAfrStandardComponentKey),define("DS/WAfrContainerAsWidget/old_config",[],function(){return{interpreters:[],components:[{key:WAfrStandardComponentKey.AppManager,code:"DS/WAfrAppManager/mod_AppManager",ctor:"AppManager"},{key:WAfrStandardComponentKey.ModelEvents,code:"DS/WAfrModelEvents/mod_ModelEvents"},{key:WAfrStandardComponentKey.NotificationManager,code:"DS/WAfrUIStandardComponents/NotificationManager"}]}}),define("DS/WAfrUIStandardComponents/mod_HandlerProxyUtilities",[],function(){function e(e,t){t.getId=e.getId.bind(e),t.isAvailable=e.isAvailable.bind(e),t.onAvailabilityEvent=e.onAvailabilityEvent.bind(e),t.removeAvailabilityEventSubscription=e.removeAvailabilityEventSubscription.bind(e),t.getI18n=e.getI18n.bind(e),t.getAccelerator=e.getAccelerator.bind(e),t.isExecutable=e.isExecutable.bind(e),t.onIsEnabledChangeEvent=e.onIsEnabledChangeEvent.bind(e),t.removeIsEnabledChangeEventSubscription=e.removeIsEnabledChangeEventSubscription.bind(e)}function t(t,n){e(t,n),n.getIcon=t.getIcon.bind(t),n.execute=t.execute.bind(t),n.onLifeCycleEvent=t.onLifeCycleEvent.bind(t),n.removeLifeCycleEventSubscription=t.removeLifeCycleEventSubscription.bind(t)}function n(e){var n={};return t(e,n),n}function o(t){var o={};return function(t,o){e(t,o),o.onStateEvent=t.onStateEvent.bind(t),o.getState=t.getState.bind(t),o.setState=t.setState.bind(t);var r=t.getCheckHandler(),i=t.getUncheckHandler();o.onCheckHandler=r?n(r):null,o.onUncheckHandler=i?n(i):null,o.removeStateEventSubscription=t.removeStateEventSubscription.bind(t),o.accept=function(e){return e.visitCheckHandler(o)}}(t,o),o}return{createActionHandlerProxy:function(e){var n={};return function(e,n){t(e,n),n.accept=function(e){return e.visitActionHandler(n)}}(e,n),n},createCommandHandlerProxy:function(e){var n={};return function(e,n){t(e,n),n.getState=e.getState.bind(e),n.accept=function(e){return e.visitCommandHandler(n)}}(e,n),n},createCheckHandlerProxy:o,createRadioHandlerProxy:function(t){var n={};return function(t,n){e(t,n);for(var r=t.getElements(),i=r.length,a=0,s=[];a<i;++a)s.push(o(r[a]));n.items=s,n.isUnselectAuthorized=t.isUnselectAuthorized.bind(t),n.getSelectedItem=function(){var e=t.getSelectedItem(),n=null;return e&&(n=o(e)),n},n.getSelectedItemId=t.getSelectedItemId.bind(t),n.onItemStateEvent=t.onItemStateEvent.bind(t),n.removeItemStateEventSubscription=t.removeItemStateEventSubscription.bind(t),n.getRadioItemState=t.getRadioItemState.bind(t),n.setRadioItemState=t.setRadioItemState.bind(t)}(t,n),n}}}),define("DS/WAfrUIStandardComponents/NotificationManager",["DS/Notifications/NotificationsManagerUXMessages","DS/Notifications/NotificationsManagerViewOnScreen"],function(e,t){var n=function(){t.setNotificationManager(e)};return n.prototype.notifyError=function(e){if(e){var t={level:"error",title:e.title,message:e.message,sticky:!1};this.addNotif(t)}},n.prototype.notifySuccess=function(e){var t={level:"success",title:e.title,message:e.message,sticky:!1};this.addNotif(t)},n.prototype.addNotif=function(t){e.addNotif(t)},n.prototype.createExposedComponent=function(){return{notifyError:this.notifyError.bind(this),notifySuccess:this.notifySuccess.bind(this),addNotif:this.addNotif.bind(this)}},n}),define("DS/WAfrContainerAsWidget/ContainerView",["UWA/Core","DS/Core/Core"],function(e,t){var n="Apps supporting transition",o=function(n){n=n||{},this._presenter=null,this._version=n.version||1,this.elements=Object.create(null),this.elements.container=new e.Element("div",{class:"wux-applicationframe-app",styles:{width:"100%",height:"100%"},debug:n.debug?"true":""}),n.debug&&t.setFullscreen(),widget.id||(widget.id=widget.getValue("containerId")),widget.id||(widget.id=e.Utils.getUUID()),window.addEventListener("unload",function(){this.dispatchEvent("onDestroy")}.bind(this))};return o.prototype.addExecutionContextView=function(t,n){var o=new e.Element("div",{class:"wux-applicationframe-execution-context",tabindex:"0",styles:{width:"auto",height:"100%",outline:"none",position:"relative"}}),r=this;o.addEventListener("click",function(){n&&0!==r.elements.container.getChildren().length&&n.call(t)}),o.inject(this.elements.container),o.focus(),t.setUiContainer(o)},o.prototype.setCurrentExecutionContextIndex=function(e){this._current=e},o.prototype.inject=function(e){this.elements.container.inject(e)},o.prototype.initPreferences=function(){widget.initPreferences()},o.prototype.hasPreference=function(e){return widget.hasPreference(e)},o.prototype.hasPreferences=function(){return widget.hasPreferences()},o.prototype.setPreferences=function(e){widget.setPreferences(e)},o.prototype.mergePreferences=function(e){widget.mergePreferences(e)},o.prototype.getPreferences=function(){return widget.getPreferences()},o.prototype.addPreference=function(e){widget.addPreference(e)},o.prototype.getPreference=function(e){return widget.getPreference(e)},o.prototype.addEvent=function(e,t,n,o){return widget.addEvent(e,t,n,o)},o.prototype.addEventOnce=function(e,t,n,o){return widget.addEventOnce(e,t,n,o)},o.prototype.removeEvent=function(e,t,n,o){return widget.removeEvent(e,t,n,o)},o.prototype.dispatchEvent=function(e,t,n){return widget.dispatchEvent(e,t,n)},o.prototype.deleteValue=function(e){return widget.deleteValue(e)},o.prototype.getValue=function(e){return widget.getValue(e)},o.prototype.setValue=function(e,t){return widget.setValue(e,t)},o.prototype.setValues=function(e){return widget.setValues(e)},o.prototype.getValues=function(){return widget.data},o.prototype.getId=function(){return widget.id},o.prototype.getTitle=function(){return widget.getTitle()},o.prototype.setTitle=function(e){widget.setTitle(e)},o.prototype.setIcon=function(e){widget.setIcon(e)},o.prototype.setBrand=function(e){"3DEXPERIENCE"===e?(widget.icon="",widget.setMetas({brand:"3DSWYM"}),widget.setIcon(widget.uwaUrl.substr(0,widget.uwaUrl.search("/webapps/")+9)+"WAfrContainerAsWidget/assets/icons/afr-no-brand-icon.png")):void 0!==e&&widget.setMetas({brand:e})},o.prototype.destroy=function(){this.elements.container.empty(!0)},o.prototype.getParentContainer=function(){return window.top.document.querySelector("#m_"+widget.id)},o.prototype.getTitleHeader=function(){var e=this.getParentContainer(),t=e?e.querySelector(".moduleHeader__title"):void 0,n={widgetParentHeader:t,brand:void 0,appTitle:void 0,title:void 0};if(!t)return n;var o=t.innerHTML.split(" - ");return 3===o.length?(n.brand=o[0],n.appTitle=o[1],n.title=o[2]):2===o.length?o[0]===widget.getValue("appTitle")?(n.brand=void 0,n.appTitle=o[0],n.title=o[1]):(n.brand=o[0],n.appTitle=o[1]):n.appTitle=o.join(" - "),n},o.prototype.setTitleHeader=function(e){var t=(e=e||{}).appTitle,o=this.getTitleHeader(),r=!1;if(t!==n&&o.widgetParentHeader){var i=e.brand,a=e.appTitle,s=e.title;if(o.title===n&&(o.title=""),void 0!==s&&(o.title=s,r=!0),a&&a!==n&&(o.appTitle=a,r=!0),null!=i&&(o.brand=i,r=!0),"SOLIDWORKS"!==o.brand&&"3DEXPERIENCE"!==o.brand||(o.brand=void 0),r){var d=[o.brand,o.appTitle];o.widgetParentHeader.innerHTML=d.filter(function(e){return e}).join(" - "),o.title&&(o.widgetParentHeader.textContent+=` - ${o.title}`)}}},o.prototype.getLanguage=function(){return widget.lang},o.prototype.setDir=function(e){widget.setDir(e)},o.prototype.getView=function(){return widget.getView()},o.prototype.getAvailableViews=function(){return widget.getAvailableViews()},o.prototype.getUrl=function(){return widget.getUrl()},o.prototype.setOptions=function(e){widget.setOptions(e)},o.prototype.setCounter=function(e,t){widget.setCounter(e,t)},o.prototype.getViewportDimensions=function(){return widget.getViewportDimensions()},o.prototype.getViewportDimensions=function(){return widget.getViewportDimensions()},o.prototype.setPlugins=function(e){widget.setPlugins(e)},o.prototype.initPlugins=function(){widget.initPlugins()},o.prototype.isApplicationInWidget=function(){return!!widget},o}),define("DS/WAfrUIStandardComponents/mod_UIComponent",["UWA/Core","UWA/Class/Promise","DS/Tweakers/TypeRepresentationFactory","DS/Tree/TreeListView","DS/Windows/Panel","DS/Controls/Button","DS/WAfrUIStandardComponents/UIFrame","DS/Utilities/TouchUtils","DS/WAfrUIModelManagers/MainUIComponentManager","DS/WAfrUIModelManagers/ContextualUIComponentManager","DS/WAfrHandlers/mod_HandlerComponent","DS/WAfrTweakers/TweakerHandlerGenerateMethods","DS/WAfrUIStandardComponents/mod_HandlerProxyUtilities","DS/WelcomeScreenView/WelcomeScreenButtonController","text!DS/WAfrTweakers/assets/WAfrTypeRepresentations.json","text!DS/WAfrTweakers/assets/WAfrTypeTemplates.json"],function(e,t,n,o,r,i,a,s,d,p,c,l,m,u,f,g){var h={DEFAULT_COMPONENT_NAME:"Afr_UIComponent"},C=m.createActionHandlerProxy,v=m.createCommandHandlerProxy,y=m.createCheckHandlerProxy,_=m.createRadioHandlerProxy;return h.UIComponent=function(e){this._properties={},this._properties._typeRepFactory=new n,this._properties._typeRepFactory.registerTypeRepresentations(f),this._properties._typeRepFactory.registerTypeTemplates(g),this._properties.components={panels:[]},this._properties._mainComponentAvailability=!0,this._properties._mainUIComponentManager=new d,this._properties._contextualUIComponentManager=new p},h.UIComponent.prototype.createDefaultHandlerRepresentation=function(e){var n=null;if(e||e.id){var o=this.getExecutionContext();if(o){var r=o.getComponent(c.DEFAULT_COMPONENT_NAME);if(r){var i=r.getLeafHandlerMaps(),a=null,d=null,p=i.actionHandlers[e.id];if(p?(a="actionHandler",d=C(p)):(p=i.commandHandlers[e.id])&&(a="commandHandler",d=v(p)),d&&e.representationType&&"WelcomeScreenButton"===e.representationType){let n,o=function(e){return n.controller.subscribe({event:"REMOVE_CUSTOM_CONTENT"},e)},r=function(e){n.controller.unsubscribe(e)};return n=e.hasCustomContent?{id:e.id,hasCustomContent:!0,i18n:d.getI18n(),icon:d.getIcon().hasOwnProperty("fonticon")?d.getIcon().fonticon:"",controller:new u({onAction:e=>{return new Promise((e,t)=>{d.execute({args:{viewCallback:e,subscribeToCancelCustomContentFunction:o,unsubscribeToCancelCustomContentFunction:r}})})}})}:{id:e.id,i18n:d.getI18n(),icon:d.getIcon().hasOwnProperty("fonticon")?d.getIcon().fonticon:"",controller:new u({onAction:e=>(d.execute(),Promise.resolve())})},t.resolve(n)}if(d&&a){let e=l[a]({value:d,typePath:a,touchMode:s.getTouchMode()});n=Promise.resolve(e).then(function(e){return t.resolve(e.getContent())})}else n=t.reject(new Error("Impossible to generate the view for "+e.id))}else n=t.reject(new Error("Handler Component not found."))}else n=t.reject(new Error("Execution Context not found."))}else n=t.reject(new Error("Wrong parameters."));return n},h.UIComponent.prototype.createCheckHandlerRepresentation=function(e){var n=null;if(e||e.id){var o=this.getExecutionContext();if(o){var r=o.getComponent(c.DEFAULT_COMPONENT_NAME);if(r){var i=r.getCheckHandlerMap()[e.id];if(i){var a=y(i);let e=l.checkHandlerButton({value:a,typePath:"checkHandlerButton",touchMode:s.getTouchMode()});n=Promise.resolve(e).then(function(e){return t.resolve(e.getContent())})}else n=t.reject(new Error("Impossible to generate the view for "+e.id))}else n=t.reject(new Error("Handler Component not found."))}else n=t.reject(new Error("Execution Context not found."))}else n=t.reject(new Error("Wrong parameters."));return n},h.UIComponent.prototype.createRadioHandlerRepresentation=function(e){var n=null,o={default:"radioHandler",singleButton:"radioHandlerLoopButton"};if(e||e.id){var r=o.default;if(e.representationType&&(o.hasOwnProperty(e.representationType)?r=o[e.representationType]:n=Promise.reject(new Error("The specified representation type does not exist."))),null===n){var i=this.getExecutionContext();if(i){var a=i.getComponent(c.DEFAULT_COMPONENT_NAME);if(a){var d=a.getRadioHandlerMap()[e.id];if(d){var p=_(d);let e=l[r]({value:p,typePath:r,touchMode:s.getTouchMode()});n=Promise.resolve(e).then(function(e){return t.resolve(e.getContent())})}else n=t.reject(new Error("Impossible to generate the view for "+e.id))}else n=t.reject(new Error("Handler Component not found."))}else n=t.reject(new Error("Execution Context not found."))}}else n=t.reject(new Error("Wrong parameters."));return n},h.UIComponent.DEFAULT_COMPONENT_NAME=h.DEFAULT_COMPONENT_NAME,h.UIComponent.prototype.createExposedComponent=function(){return{deactivateMainComponent:this.deactivateMainComponent.bind(this),activateMainComponent:this.activateMainComponent.bind(this),hideMainComponent:this.hideMainComponent.bind(this),showMainComponent:this.showMainComponent.bind(this),getContextualUIComponentManager:this.getContextualUIComponentManager.bind(this),activateSection:this.activateSection.bind(this),getActiveSections:this.getActiveSections.bind(this),pinSection:this.pinSection.bind(this),unpinSection:this.unpinSection.bind(this),isSectionPinnedByUser:this.isSectionPinnedByUser.bind(this),createDefaultHandlerRepresentation:this.createDefaultHandlerRepresentation.bind(this),createCheckHandlerRepresentation:this.createCheckHandlerRepresentation.bind(this),createRadioHandlerRepresentation:this.createRadioHandlerRepresentation.bind(this)}},h.UIComponent.prototype.destroy=function(){return this._typeRepFactory=void 0,t.resolve()},h.UIComponent.prototype.initialize=function(){var e=[];this._properties._mainUIComponentManager.setContext({executionContext:this.getExecutionContext(),UIComponent:this.getExecutionContext().getComponent(this._private._identifier),UIFrame:this.getExecutionContext().getComponent(a.DEFAULT_COMPONENT_NAME)}),this._properties._mainComponentAvailability&&e.push(this._properties._mainUIComponentManager.generate({touchMode:s.getTouchMode()}));var n=this.getExecutionContext();return this._properties._contextualUIComponentManager.setContext({executionContext:n,UIComponent:void 0===n?void 0:n.getComponent(this._private._identifier),UIFrame:void 0===n?void 0:n.getComponent(a.DEFAULT_COMPONENT_NAME),Viewer:void 0===n?void 0:void 0===n.getApplicationComponent("Viewer")?void 0:n.getApplicationComponent("Viewer").get3DViewer()}),t.all(e)},h.UIComponent.prototype.configure=function(){return t.resolve()},h.UIComponent.prototype.clean=function(){var e,n=this;n._properties.flatModel=void 0,n._properties.tagList=void 0,n._properties._mainComponentAvailability=!0,n._properties._mainUIComponentManager.clean(),n._properties._contextualUIComponentManager.dispose();var o=n.getExecutionContext().getComponent(a.DEFAULT_COMPONENT_NAME);if(o&&(e=o.getImmersiveFrame()),e&&n._properties.components&&n._properties.components.panels)for(var r,i;0!==n._properties.components.panels.length||void 0!==n._properties.components.panels[0];)i=n._properties.components.panels[0],n._properties.components.panels.splice(0,1),(r=i.windowGroup)&&(e.removeWindow(r),r.destroy()),i&&(e.removeWindow(i),i.destroy());return t.resolve()},h.UIComponent.prototype.setGlobalFlatModel=function(e){this._properties.flatModel=e.model},h.UIComponent.prototype.setTagList=function(e){this._properties.tagList=e.tab},h.UIComponent.prototype.deactivateMainComponent=function(){this._properties._mainComponentAvailability=!1,this.hideMainComponent()},h.UIComponent.prototype.activateMainComponent=function(){this._properties._mainComponentAvailability=!0},h.UIComponent.prototype.activateSection=function(e){var t=this;void 0!==t&&void 0!==t._properties&&void 0!==t._properties._mainUIComponentManager&&(Array.isArray(e)?t._properties._mainUIComponentManager.activateSections(e):t._properties._mainUIComponentManager.activateSection(e))},h.UIComponent.prototype.getActiveSections=function(){return void 0!==this&&void 0!==this._properties&&void 0!==this._properties._mainUIComponentManager?this._properties._mainUIComponentManager.getActiveSections():null},h.UIComponent.prototype.pinSection=function(e){if(void 0!==this&&void 0!==this._properties&&void 0!==this._properties._mainUIComponentManager)return this._properties._mainUIComponentManager.pinSection(e)},h.UIComponent.prototype.unpinSection=function(e){if(void 0!==this&&void 0!==this._properties&&void 0!==this._properties._mainUIComponentManager)return this._properties._mainUIComponentManager.unpinSection(e)},h.UIComponent.prototype.isSectionPinnedByUser=function(e){if(void 0!==this&&void 0!==this._properties&&void 0!==this._properties._mainUIComponentManager)return this._properties._mainUIComponentManager.isSectionPinnedByUser(e)},h.UIComponent.prototype.hideMainComponent=function(){void 0!==this&&void 0!==this._properties&&void 0!==this._properties._mainUIComponentManager&&this._properties._mainUIComponentManager.hide()},h.UIComponent.prototype.showMainComponent=function(){void 0!==this&&void 0!==this._properties&&void 0!==this._properties._mainUIComponentManager&&this._properties._mainUIComponentManager.show()},h.UIComponent.prototype.setMainComponentActivatedCategory=function(e){void 0!==this&&void 0!==this._properties&&void 0!==this._properties._mainUIComponentManager&&this._properties._mainUIComponentManager.setActivatedCategory(e)},h.UIComponent.prototype.setMainComponentCategoriesOrder=function(e){void 0!==this&&void 0!==this._properties&&void 0!==this._properties._mainUIComponentManager&&this._properties._mainUIComponentManager.setCategoriesOrder(e)},h.UIComponent.prototype.getContextualUIComponentManager=function(){return this._properties._contextualUIComponentManager},h.UIComponent.prototype.setCategories=function(e){this._properties.categories=e},h.UIComponent.prototype.getCategories=function(){return this._properties.categories},h}),define("DS/WAfrUIStandardComponents/mod_UIInterpreter",["UWA/Class/Promise","DS/WAfrUIStandardComponents/mod_UIComponent","DS/WAfrHandlers/mod_HandlerComponent","DS/Tree/TreeNodeModel","DS/TreeModel/TreeDocument","DS/WAfrUIStandardComponents/mod_HandlerProxyUtilities","DS/WAfrUIStandardComponents/UIFrame"],function(e,t,n,o,r,i,a){var s=i.createActionHandlerProxy,d=i.createCommandHandlerProxy,p=i.createCheckHandlerProxy,c=i.createRadioHandlerProxy;function l(){this._treeDocument=void 0,this._uiComponent=void 0,this._tagList=[]}function m(e,t){if(!e||!t)throw new Error("Missing parameters.");t.handlerList&&t.handlerList.forEach(function(t){for(var n in t)e._tagList.indexOf(n)<0&&e._tagList.push(n)})}function u(e,t){if(!e||!t)throw new Error("Missing parameters.");t.handlerList.forEach(function(t){var n,r,i={tagList:e._tagList,elem:t};e._treeDocument.addChild((r=new o({label:(n=i).elem.identifier,icon:"",grid:{label:n.elem.identifier,type:null,tweaker:null},useAsyncPreExpand:!1}),n.tagList.forEach(function(e){r.options.grid[e]=n.elem[e]}),r))})}function f(e,t){if(!e||!t)throw new Error("Missing parameters.");void 0===e._categories&&(e._categories={});let n=0;const o=t.categories.length;let r=null;for(;n<o;++n)r=t.categories[n],e._categories[r.identifier]=r}l.prototype.interpret=function(n,o,i){if(void 0!==n&&void 0!==o){this._executionContext=o;var s=t.DEFAULT_COMPONENT_NAME;this._uiComponent=o.getComponent(s);var d=n.UIModel;this._treeDocument=new r,this._treeDocument.options.shouldBeSelected=function(e){return!1},d&&d.handlerList&&(m(this,d),u(this,d),void 0!==d.mainComponent&&(this._mainComponentAvailability=d.mainComponent.mainComponentAvailability,this._mainComponentDefaultCategory=d.mainComponent.defaultActivatedCategory,this._mainComponentCategoriesOrder=d.mainComponent.categoriesOrder),d.categories&&f(this,d));var p=o.getComponent(a.DEFAULT_COMPONENT_NAME);if(!p)throw new Error("Cannot find the UIFrame component");var c=!1;return d&&d.statusComponent&&d.statusComponent._specialAuthorization&&(c=!0),p._setHasStatusBar(c),e.resolve()}},l.prototype.commit=function(){var t=this,o=n.DEFAULT_COMPONENT_NAME;t._handlerComponent=t._executionContext.getComponent(o);var r=t._handlerComponent._private._chkHdlrMap,i=t._handlerComponent._private._radioHdlrMap,a=t._handlerComponent.getLeafHandlerMaps(),l=a.actionHandlers,m=a.commandHandlers,u=!1;if(!this._handlerComponent)throw new Error("HandlerComponent not found");var f=this._treeDocument.getChildren();return this._treeDocument.prepareUpdate(),f.forEach(function(e){var t=e.options.grid.identifier,n="";if(l.hasOwnProperty(t))n=void 0!==e.options.grid.set?"commandFlyout":"actionHandler",Array.isArray(e.options.grid.category)&&e.options.grid.category.includes("WAfrFront")&&(u=!0),e.updateOptions({grid:{type:n,typeView:"actionHandler",handler:s(l[t])}});else if(m.hasOwnProperty(t))n=void 0!==e.options.grid.set?"commandFlyout":"commandHandler",Array.isArray(e.options.grid.category)&&e.options.grid.category.includes("WAfrFront")&&(u=!0),e.updateOptions({grid:{type:n,typeView:"commandHandler",handler:d(m[t])}});else if(r.hasOwnProperty(t))n=void 0!==e.options.grid.set?"commandFlyout":"checkHandlerButton",Array.isArray(e.options.grid.category)&&e.options.grid.category.includes("WAfrFront")&&(u=!0),e.updateOptions({grid:{type:n,typeView:"checkHandlerButton",handler:p(r[t])}});else if(i.hasOwnProperty(t)){n=void 0!==e.options.grid.set?"commandFlyout":"radioHandler",Array.isArray(e.options.grid.category)&&e.options.grid.category.includes("WAfrFront")&&(u=!0);let o="radioHandler";void 0!==e.options.grid.typeRepresentation&&(n=o=e.options.grid.typeRepresentation),e.updateOptions({grid:{type:n,typeView:o,handler:c(i[t])}})}}),t._treeDocument.pushUpdate(),t._uiComponent.setTagList({tab:t._tagList}),t._uiComponent.setGlobalFlatModel({model:t._treeDocument}),!1===t._mainComponentAvailability&&t._uiComponent.deactivateMainComponent(),t._uiComponent._isMABAvailable=u,t._uiComponent.setMainComponentCategoriesOrder(t._mainComponentCategoriesOrder),t._uiComponent.setCategories(t._categories),e.resolve()},l.prototype.enrich=function(e,t){if(!e||!t)throw new Error("Missing parameters");const n=e.UIModel;let o=Promise.resolve();return n&&n.handlerList&&(m(this,n),u(this,n),void 0!==n.mainComponent&&Array.isArray(n.mainComponent.categoriesOrder)&&(void 0===this._mainComponentCategoriesOrder?this._mainComponentCategoriesOrder=n.mainComponent.categoriesOrder:this._mainComponentCategoriesOrder=this._mainComponentCategoriesOrder.concat(n.mainComponent.categoriesOrder)),n.categories&&f(this,n),o=this.commit()),o};var g={generateInterpreter:function(e){return new l}};return g});WAfrStandardComponentKey={A2XClientComponent:"AFR_A2XClientComponent",AppManager:"AFR_AppManager",ModelEvents:"AFR_ModelEvents",AvailabilityModesComponent:"AFR_AvailabilityModesComponent",HandlerComponent:"AFR_HandlerComponent",CommandsManagerComponent:"AFR_CommandsManager",UIFrame:"AFR_UIFrameManager",UIComponent:"Afr_UIComponent",UndoRedoComponent:"AFR_UndoRedoComponent",CSComponent:"AFR_CSComponent",NotificationManager:"AFR_NotificationManager",RecordComponent:"AFR_RecordComponent",AcceleratorManagementComponent:"AFR_AcceleratorManagementComponent"};Object.freeze(WAfrStandardComponentKey),define("DS/WAfrContainerAsWidget/default_config",["DS/WAfrAppManager/mod_AppManager","DS/WAfrHandlers/mod_HandlerInterpreter","DS/WAfrUIStandardComponents/mod_UIInterpreter","DS/WAfrCommandsArchitecture/mod_CommandsInterpreter","DS/WAfrAcceleratorManager/mod_AcceleratorInterpreter"],function(e,t,n,o,r){return{interpreters:[{module:t,target:WAfrStandardComponentKey.HandlerComponent},{module:n,target:WAfrStandardComponentKey.UIComponent},{module:o,target:WAfrStandardComponentKey.CommandsManagerComponent},{module:r,target:WAfrStandardComponentKey.AcceleratorManagementComponent}],components:[{key:WAfrStandardComponentKey.A2XClientComponent,code:"DS/WAfrA2XCommunication/mod_WAfrA2XClientComponent",ctor:"WAfrA2XClientComponent",when:"a2x==true"},{key:WAfrStandardComponentKey.AppManager,code:"DS/WAfrAppManager/mod_AppManager",ctor:"AppManager"},{key:WAfrStandardComponentKey.ModelEvents,code:"DS/WAfrModelEvents/mod_ModelEvents"},{key:WAfrStandardComponentKey.AvailabilityModesComponent,code:"DS/WAfrAvailabilityModes/mod_AvailabilityModesComponent",ctor:"AvailabilityModesComponent"},{key:WAfrStandardComponentKey.HandlerComponent,code:"DS/WAfrHandlers/mod_HandlerComponent",ctor:"HandlerComponent"},{key:WAfrStandardComponentKey.CommandsManagerComponent,code:"DS/WAfrCommandsArchitecture/mod_CommandsManagerComponent",ctor:"CommandsManagerComponent"},{key:WAfrStandardComponentKey.UIFrame,code:"DS/WAfrUIStandardComponents/UIFrame"},{key:WAfrStandardComponentKey.UIComponent,code:"DS/WAfrUIStandardComponents/mod_UIComponent",ctor:"UIComponent"},{key:WAfrStandardComponentKey.UndoRedoComponent,code:"DS/WAfrUndoRedo/WAfrUndoRedoComponent",ctor:"WAfrUndoRedoComponent"},{key:WAfrStandardComponentKey.CSComponent,code:"DS/AfrCS/WAfrCSComponent"},{key:WAfrStandardComponentKey.NotificationManager,code:"DS/WAfrUIStandardComponents/NotificationManager"},{key:WAfrStandardComponentKey.RecordComponent,code:"DS/WAfrODTServices/mod_CommandRecorderComponent",when:"CaptureMode==true"},{key:WAfrStandardComponentKey.AcceleratorManagementComponent,code:"DS/WAfrAcceleratorManager/mod_AcceleratorManagementComponent",ctor:"AcceleratorManagementComponent"}]}});