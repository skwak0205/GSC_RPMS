define("DS/ENOXEngineerInfra/ENOXFacade",["UWA/Core"],function(e){"use strict";function t(e){this.core=e.core,this._DnDManagers=[]}return t.prototype={getContainer:function(){return this.container},emptyContainer:function(){this.container.innerHTML=""},addContent:function(e){this.container.innerHTML=e},getElement:function(e,t){return t?UWA.extendElement(this.container.querySelector(e)):this.container.querySelector(e)},getElements:function(e){return this.container.querySelectorAll(e)},registerDnDManager:function(e){console.log("implemented in ViewService")},unRegisterDnDManager:function(e){for(var t=-1,n=0;n<this._DnDManagers.length;n++)if(this._DnDManagers[n].token===e){t=n;try{this._DnDManagers[n].manager&&this._DnDManagers[n].manager.destroy()}catch(e){console.error(e)}break}t>=0&&this._DnDManagers.splice(t,1)},unRegisterAllDnDManager:function(){for(var e=0;e<e.length;e++){const t=this._DnDManagers[e];try{t.manager&&t.manager.destroy()}catch(e){console.error(e)}}this._DnDManagers=null},unsubscribeAll:function(){this.logger.info("unsubscribeAll events ");var e=UWA.is(this.eventsTokens)?this.eventsTokens:{},t=this.getApplicationBroker();for(var n in e)e.hasOwnProperty(n)&&(this.logger.info("events to unsubscribe "+n),t.unsubscribe(e[n]),delete e[n])},stop:function(e){this.logger.info("stopping sandbox "),this.unRegisterAllDnDManager(),this.unsubscribeAll(),e&&this.garbageCollector(e),this.garbageCollector(this,"container")},garbageCollector:function(e,t){var n=Object.keys(e);if(UWA.is(t))for(var r=t.split(","),o=0;o<r.length;o++){var i=n.indexOf(r[o]);i>-1&&n.splice(i,1)}for(o=0;o<n.length;o++)e[n[o]]=void 0}},t}),define("DS/ENOXEngineerInfra/Interface",[],function(){"use strict";var e=function(e,t){if(2!=arguments.length)throw new Error("Interface constructor called with "+arguments.length+"arguments, but expected exactly 2.");this.name=e,this.methods=[];for(var n=0,r=t.length;n<r;n++){if("string"!=typeof t[n])throw new Error("Interface constructor expects method names to be passed in as a string.");this.methods.push(t[n])}};return e.ensureImplements=function(t){if(arguments.length<2)throw new Error("Function Interface.ensureImplements called with "+arguments.length+"arguments, but expected at least 2.");for(var n=1,r=arguments.length;n<r;n++){var o=arguments[n];if(o.constructor!==e)throw new Error("Function Interface.ensureImplements expects argumentstwo and above to be instances of Interface.");for(var i=0,s=o.methods.length;i<s;i++){var a=o.methods[i];if(!t[a]||"function"!=typeof t[a])throw new Error("Function Interface.ensureImplements: object "+t+"does not implement the "+o.name+" interface. Method ##"+a+"## was not found.")}}},e}),define("DS/ENOXEngineerInfra/Mediator",["DS/Core/ModelEvents","DS/PlatformAPI/PlatformAPI"],function(e,t){"use strict";var n=null,r=function(){n=new e};return r.prototype.publish=function(e,t){n.publish({event:e,data:t})},r.prototype.subscribe=function(e,t){return n.subscribe({event:e},t)},r.prototype.unsubscribe=function(e){n.unsubscribe(e)},r.prototype.createNewChannel=function(){return new e},r.prototype.getApplicationBroker=function(){return n},r.prototype.publishPlatformEvent=function(e,n){t.publish(e,n)},r.prototype.subscribePlatformEvent=function(e,n){return t.subscribe(e,n)},r.prototype.unsubscribePlatformEvent=function(e){return t.unsubscribe(e)},r.prototype.destroy=function(){n.destroy()},r}),define("DS/ENOXEngineerInfra/ENOXComponentsManager",["UWA/Core"],function(e){"use strict";function t(e){if(!e.componentsRepository)throw new Error("componentsRepository is mandatory for components registration");this.logger=e.core.logger.get("Components manager"),this.componentsRepository=e.componentsRepository,this.componentsList={},this.app=e}var n=[],r=[],o={};function i(e){for(var t in n=[],r=[],e)e.hasOwnProperty(t)&&(n.push(e[t]),r.push(t));return n}function s(e){return new Promise(function(t,n){require(e,function(){t(arguments)},n)})}return t.prototype.getComponent=function(e){return void 0===this.componentsList[e]?null:this.componentsList[e]},t.prototype.getComponentCreator=function(e){var t=this.getComponent(e);return t?"function"!=typeof t.creator?null:t.creator:(this.logger.error("Component not defined"),null)},t.prototype.registerNewComponent=function(e,t,n){var r=this.getComponent(e);if(!r)return this.logger.error("Component not defined"),null;r.instances[t]=n},t.prototype.deleteInstance=function(e,t){if(!this.componentsList[e])return null;delete this.componentsList[e].instances[t]},t.prototype.getComponentInstance=function(e,t){return this.componentsList[e]?null===this.componentsList[e].instances[t]||void 0===this.componentsList[e].instances[t]?null:this.componentsList[e].instances[t]:null},t.prototype.startNewInstance=function(e,t,n){var r=this.getComponentCreator(e);if(null===r)return this.logger.error("Component or constructor not defined"),null;var o=this.app.generateId(e+"_Id_"),i=this.app.sandboxes.create(o,t);i.getComponentName=function(){return e},i.options=n;var s=new r(i);return s.start(),s.ref=o,this.registerNewComponent(e,o,s),s},t.prototype.startInstances=function(e){var t=this.getUnloadedCmp(e),n=this;return this.loadComponents(t).then(function(){for(var t=[],r=0;r<e.length;r++){var o=e[r],i=n.startNewInstance(o.name,o.container,o.contextData);t.push(i)}return t})},t.prototype.getUnloadedCmp=function(e){for(var t={},n=0;n<e.length;n++){var r=e[n];!this.isLoaded(r.name)&&this.isWaitingTBLoad(r.name)&&(t[r.name]=o[r.name],delete o[r.name])}return t},t.prototype.loadComponentsCreatorsLazyLoading=function(){return this.loadComponents(o).then(function(){o={}})},t.prototype.loadComponentsCreatorsForBoot=function(){var e,t=this;return(e=this.componentsRepository,new Promise(function(t,n){"string"==typeof e&&e.toLowerCase().endsWith("json")?require(["text!"+e],function(e){t(JSON.parse(e))},n):"object"==typeof e?t(e):n("the provided repository is not valid :( ")})).then(function(e){var t=e;this.app.core.isInExperimentalLabMode()&&(t=function(e){for(var t=e.experimental||{},n=Object.keys(t),r=e.loadOnBoot,o=e.deffered,i=0;i<n.length;i++){var s=n[i];r[s]?r[s]=t[s]:o[s]&&(o[s]=t[s])}return e}(e));var n=t.loadOnBoot;return o=t.deffered,i(n)}.bind(t)).then(s).then(function(e){t._buildComponentMap(e)}).catch(function(e){throw new Error("##Error loading componentsRepository : "+e)})},t.prototype.loadComponents=function(e){var t=this;return s(function(e){if(!e)throw new Error("need an object of module as input ");return i(e)}(e)).then(function(e){return t._buildComponentMap(e)})},t.prototype.isLoaded=function(e){return!!(this.componentsList[e]&&this.componentsList[e].creator&&this.componentsList[e].instances)},t.prototype.isWaitingTBLoad=function(e){return!!o.hasOwnProperty(e)},t.prototype.isAllowed=function(e){return!(!this.isLoaded(e)&&!this.isWaitingTBLoad(e))},t.prototype._buildComponentMap=function(e){for(var t=0;t<e.length;t++){var n=r[t];this.isLoaded(n)||(this.componentsList[n]={},this.componentsList[n].creator=e[t],this.componentsList[n].instances={})}return this.componentsList},t}),define("DS/ENOXEngineerInfra/router.utils",["DS/Router5/js/Router5","DS/Router5/js/Router5BrowserPlugin","DS/Router5/js/Router5ListenersPlugin","DS/Router5/js/Router5Helpers"],function(e,t,n,r){return{createRouter:function(t,r,o,i){var s=e.createRouter(t,r);return o&&s.usePlugin(n()),s},listenToRouterStateChanges:function(e){e.addListener(function(t,n){var o=r.transitionPath(t,n);o.toDeactivate.forEach(function(r){if(void 0!==e.routerMethods[r]){var o=e.routerMethods[r].deactivate;void 0!==o&&o(t,n)}}),o.toActivate.forEach(function(r){if((r!==t.name||!t.params.noactivate)&&void 0!==e.routerMethods[r]){var o=e.routerMethods[r].activate;void 0!==o&&o(t,n)}})})}}}),define("DS/ENOXEngineerInfra/ENOXExtensionManager",["UWA/Core","UWA/Class/Promise","DS/ENOXEngineerInfra/Interface"],function(e,t,n){"use strict";var r="";function o(e,n){this._extensions=[],this.app=e,this.logger=e.core.logger,this.initStarted=!1,this.initStatus=t.deferred(),r=n&&"string"==typeof n&&n.startsWith("DS")?n:""}var i=new n("ExtensionItf",["initialize"]);return o.prototype.add=function(e){if(this.initStarted)throw new Error("Init extensions already called");if(function(e,t){if(!t)throw new Error("refExt is a mandatory parameter to add extension");for(var n=0;n<e.length;n++)if(e[n].ref===t)return!0;return!1}
/*!
       * If the value of the first argument is a function then invoke
       * it with the rest of the args, otherwise, return it.
       */(this._extensions,e.ref)){var t=e.ref.toString()+" is already registered.";throw new Error(t+="Extensions can only be added once.")}return this._extensions.push(e),this},o.prototype.init=function(){if(this.initStarted)throw new Error("Init extensions already called");this.initStarted=!0;var e=this._extensions.slice(0);return this.serialInit(e)},o.prototype.serialInit=function(e){return e.reduce(function(e,o){return new t(function(s,a){e.then(function(e){
/*!
    * Actual extension loading.
    *
    * The sequence is:
    *
    * * resolves the extension reference
    * * register and requires its dependencies if any
    * * init the extension
    *
    * This method also returns a promise that allows
    * to keep track of the app's loading sequence.
    *
    * If the extension provides a `afterAppStart` method,
    * the promise will resolve to that function that
    * will be called at the end of the app loading sequence.
    *
    * @param {String|Object|Function} extDef the reference and context of the extension
    */
var c;
/*!
    * Extension resolution before actual loading.
    * If `ext` is a String, it is considered as a reference
    * to an AMD module that has to be loaded.
    *
    * This method returns a promise that resolves to the actual extension,
    * With all its dependencies already required' too.
    *
    * @param {String|Object|Function} ext the reference of the extension
    * @param {Object} context the thing this extension is supposed to extend
    */(c=o,new t(function(e,o){var s=c.ref,a=c.context,u=function(e,n){return new t(function(t,o){var i=function(r){e=function(e){return"function"==typeof e?new e(Array.prototype.slice.call(arguments,1)):e}(r,n),t(e)};"string"==typeof e?require([r+e],i,function(e){n.core.logger.error("Error loading ext:"+e),o(e)}):i(e)})}(s,a);u.catch(o),u.then(function(r){if(!r)return e();try{n.ensureImplements(r,i)}catch(e){return o(e.message)}var s=t.cast(r.initialize(a));s.finally(function(){return e(!0)})})})).then(function(){s()}).catch(function(e){a(e)})}).catch(function(e){a(e)})})},t.resolve([]))},o}),define("DS/ENOXEngineerInfra/ENOXEngineerCore",["DS/ENOXEngineerInfra/Mediator","DS/ENOXLogger/Logger","DS/ENOXEngineerInfra/ENOXExtensionManager","DS/ENOXEngineerInfra/ENOXFacade","DS/ENOXEngineerInfra/ENOXComponentsManager","DS/ENOXEngineerInfra/router.utils","UWA/Class/Promise","DS/Notifications/NotificationsManagerUXMessages","DS/Notifications/NotificationsManagerViewOnScreen","i18n!DS/ENOXEngineerInfra/assets/nls/xAppCore.json"],function(e,t,n,r,o,i,s,a,c,u){"use strict";var f=0;return function l(p){if(!(this instanceof l))return new l(p);var d,h=this;switch(this._notif_manager=a,c.setNotificationManager(this._notif_manager),c.setStackingPolicy(5),h.generateId=function(e){return null==e&&(e=null),null===e?++f:e+ ++f},h.started=!1,h.componentsRepository=p.componentsRepository,h.eagerComponentsLoading=p.eagerComponentsLoading,p.LOG_LEVEL="off",p.debug&&(p.LOG_LEVEL="debug"),p.LOG_LEVEL.toUpperCase()){case"DEBUG":d=t.LEVEL.DEBUG;break;case"INFO":d=t.LEVEL.INFO;break;case"WARN":d=t.LEVEL.WARN;break;case"ERROR":d=t.LEVEL.ERROR;break;case"OFF":d=t.LEVEL.OFF;break;default:d=t.LEVEL.OFF}var g={level:d};t.init(g),h.core={},h.core.isInExperimentalLabMode=function(){return!(!widget.getValue("experimentalCmp")||"true"!=widget.getValue("experimentalCmp").toLowerCase())},h.mandatoryStartupPromises=[],h.optionalStartupPromises=[],h.core.logger=t,h.core.isOnDebugMode=p.debug,h.logger=t,h.core.moduleHelper={},h.sandboxBase=new r(h);var m=p.extensionsFolder&&p.extensionsFolder.length>0?p.extensionsFolder:p.odtExtensionRoot?p.odtExtensionRoot:"DS/ENOXEngineer/",E=new n(h,m);h.mediator=new e,h.core.mediator=h.mediator,h.componentsMgt=new o(h);var v=h.componentsMgt.loadComponentsCreatorsForBoot();h.use=function(e){return E.add({ref:e,context:h}),h},h.sandboxes={};var b={};h.sandboxes.create=function(e,t){if(e=e||h.generateId("sandbox-"),b[e])throw new Error("Sandbox with ref "+e+" already exists.");var n=Object.create(h.sandboxBase);return n.ref=e||h.generateId("sandbox-"),n.container=t,n.logger=h.core.logger.get(n.ref),n.eventsTokens={},n.platformEventsTokens={},n.publish=h.mediator.publish,n.publishPlatformEvent=h.mediator.publishPlatformEvent,n.getApplicationBroker=h.mediator.getApplicationBroker,n.subscribe=function(t,r){if(n.eventsTokens[t])throw new Error("Sandbox with ref "+e+" has already subscribed to this topic "+t);var o=h.mediator.subscribe(t,r);n.eventsTokens[t]=o},n.subscribePlatformEvent=function(t,r){if(n.platformEventsTokens[t])throw new Error("Sandbox with ref "+e+" has already subscribed to this platform topic "+t);var o=h.mediator.subscribePlatformEvent(t,r);n.platformEventsTokens[t]=o},n.unsubscribe=function(t){if(!n.eventsTokens[t])return n.logger.error("Sandbox with ref "+e+" has not  subscribed to this topic "+t),null;var r=n.eventsTokens[t];h.mediator.unsubscribe(r),delete n.eventsTokens[t]},n.unsubscribePlatformEvent=function(t){if(!n.platformEventsTokens[t])return n.logger.error("Sandbox with ref "+e+" has not  subscribed to this platform topic "+t),null;var r=n.platformEventsTokens[t];h.mediator.unsubscribePlatformEvent(r),delete n.platformEventsTokens[t]},b[n.ref]=n,n},h.sandboxes.get=function(e){return b[e]},h.start=function(e){if(h.started)throw h.core.logger.error("xEngineer App already started!"),new Error("xEngineer App already started!");h.core.logger.warn("xEngineer App starting ! "),h.started=!0,h._startInit=(new Date).getTime();var t=s.all([v,E.init()]);return new s(function(e,n){t.then(function(){var t=s.all(h.mandatoryStartupPromises),r=s.allSettled(h.optionalStartupPromises);h.eagerComponentsLoading,t.then(function(){r.finally(function(){h.mainController&&"function"==typeof h.mainController.initialize&&h.mainController.initialize(),e(h)})}).catch(function(e){console.error(e),h.notifyGenericIssue(),n(e)})}).catch(function(e){console.error(e),h.notifyGenericIssue()})})},h.notifyGenericIssue=function(){h._notif_manager.addNotif({level:"error",message:u.get("error.on.startup"),sticky:!1})},h.stop=function(){},h.getStartupTime=function(){return(new Date).getTime()-h._startInit},h.useRouter5=function(e){if(!e)throw new Error("options are needed to set router 5");if(!e.config||!e.applicationController)throw new Error("config and applicationController are mandatory options");var t=e.config,n=e.applicationController;if(!t.routes||!t.options)throw new Error("routes or options are undefined");if("function"!=typeof n)throw new Error("applicationController should be a class (function) !");return h.router=i.createRouter(t.routes,t.options,!0,!0),h.routerUtils=i,h.core.navigate=h.router.navigate,h.mainController=new n(h),h}}});