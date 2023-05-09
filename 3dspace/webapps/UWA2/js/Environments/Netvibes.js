/*!
  Script: Netvibes.js

    This file is part of UWA JS Runtime.

  About: License

    Copyright 2006-2012 Netvibes, a Dassault Systèmes company.
    All rights reserved.
*/
define("UWA/Environments/Netvibes",["UWA/Core","UWA/Utils","UWA/Environment","UWA/Event","UWA/Element","UWA/Utils/Client"],function(e,t,i,n,s,o){"use strict";var a=i.extend({name:"netvibes",netvibes:!0,features:{"drag and compare":{version:1},feedreader:{version:1}},init:function(e,t){this.obj=e,this._parent(t);var i=this;this.html={edit:e.elm_editContent,body:e.elm_moduleContent,icon:e.elm_ico,header:e.elm_moduleHeader,menus:e.elm_menus,wrapper:e.elm_module,module:e.elm_module,moduleFrame:e.elm_moduleFrame};var n=e.dataObj.data._netvibes_cachedModuleHeight;n&&(this.html.body.setStyle("min-height",n+"px"),this.setDelayed("removeForcedHeight",function(){i.html.body.setStyle("min-height",""),e.sendContent("onResize")},1e4))},destroy:function(){this.obj.remove(!0),this.obj=null,this._parent()},filterExternalResources:function(e,t){var i,n;return"script"===e?(n=/\b(highcharts|app\/socialpack|app\.js\.php|handlebars)\b/i,i=function(e){return!n.test(e)}):"style"===e&&App.use_compressed_js&&!App.config.debug["uncompressed-assets"]&&(n=/\/(\w+)\/\1\.css/i,i=function(e){return 0!==e.indexOf(App.urls.startpage)||!n.test(e)}),i?t.filter(i):t},onRegisterWidget:function(){this._parent();var e=this.obj,t=this.widget;e.widget=t,void 0!==e.dataObj.id&&(t.id=e.dataObj.id),t.pageId=App.Page.id||void 0,window.App&&App.lang&&(t.lang=App.lang),window.User&&User.locale?t.locale=User.locale:window.App&&App.locale&&(t.locale=App.locale),window.App&&App.dirRTL&&(t.dir="rtl"),t.isNew=e.isNew(),t.readOnly=e.isReadOnly(),t.userId=User.id,e.dataObj.extendSearch=0,e.refreshMode=!0},registerMenus:function(){this.widget.readOnly||(this.widget.setMenu({name:"close",icon:"delete",help:e.i18n("Close"),visible:function(){return!this.obj.isSticky()&&!this.obj.previewMode}.bind(this)}),this.widget.setMenu({name:"unstick",icon:"pin-point",visible:function(){return this.obj.isSticky()}.bind(this)}),this.widget.setMenu({name:"options",icon:"options",help:e.i18n("Edit")}),this.widget.setMenu({name:"options/preferences",icon:"cogwheel",label:e.i18n("Settings")}),this.widget.setMenu({name:"options/display",icon:"single-window",label:e.i18n("Display")}),this.widget.setMenu({name:"options/share",icon:"curvy-next",label:e.i18n("Share"),visible:function(){return this.obj.hasShare()}.bind(this)}),this.widget.setMenu({name:"options/refresh",icon:"refresh",label:e.i18n("Refresh")}),this.obj.previewMode||this.widget.setMenu({name:"options/clone",icon:"double-window",label:e.i18n("Duplicate")}),this.supportsPotion()&&this.widget.setMenu({name:"options/potion",icon:"potion",label:e.i18n("Potion"),wizard:"Potion",visible:function(){return Cookie.read("potionAvailable")}}),this.widget.setMenu({name:"options/edit",icon:"edit",label:e.i18n("Edit"),visible:function(){return this.obj.isUserOwner()}.bind(this)}))},onUpdateMenu:function(e){var t,i,n=this.html;this._parent(e),n.header&&(i=(t=n.header.getElement(".counter"))&&"none"!==t.getStyle("display",!0),n.header.set("data-menus-items",n.menus.childNodes.length+(i?2:0)))},onMenuExecute:function(e){if("windowed"!==this.widget.getView()&&this.widget.requestView("windowed"),e.onExecute)this._parent(e);else switch(e.name){case"close":this.obj.close();break;case"unstick":this.obj.unstick();break;case"options/preferences":this.obj.expand(!1),this.obj.toggleEdit();break;case"options/share":this.obj.share();break;case"options/refresh":this.obj.sendContent("onRefresh");break;case"options/display":this.obj.expand(!1),this.obj.toggleDisplay(!0);break;case"options/clone":this.obj.clone();break;case"options/edit":this.widget.openURL(this.obj.getEditUrl());break;case"options/potion":this.widget.dispatchEvent("launchWizard",[e])}},onOpenURL:function(e){var i,n,s,o=t.parseUrl(e);return"feed"===o.subprotocol?(s=+o.anchor||0,(n=App.FeedWidgetManager.getCurrentDisplayedFeedObj(this.widget.id))&&this.openFeedReader(n,s,this.widget.environment.module),i=!1):i=this._parent(e),i},onUpdateTitle:function(e){this.obj.setTitle(e),this._parent(e)},onUpdateIcon:function(i){0===i.lastIndexOf(e.Data.proxies.icon,0)&&(i=t.parseQuery(t.parseUrl(i).query).url,t.parseUrl(i).domain!==t.parseUrl(App.urls.storage).domain&&(i=App.getFaviconUrl(i))),this.widget.icon=i,this.obj.setIcon(i),this._parent(i)},onUpdateCounter:function(e,t){this.obj.setCounter(e,t),this._parent(e,t)},onUpdatePreferences:function(e){this.obj.options.editable=e.some(function(e){return"hidden"!==e.type})},onEdit:function(){this.obj.toggleEdit(!0),this._parent()},onResize:function(){if(!this.hasDelayed("removeForcedHeight")&&"windowed"===this.widget.getView().type){var e=this.widget.elements.body.getDimensions().innerHeight;e>0&&this.widget.setValue("_netvibes_cachedModuleHeight",e)}},endEdit:function(){this.obj.toggleEdit(!1),this._parent()},getAllData:function(){var t={};return e.extend(t,this.obj.getData()),delete t.moduleUrl,t},getData:function(e){return this.obj.getData()[e]},setData:function(e,t){this.obj.getData()[e]=t,this.saveDatas()},deleteData:function(e){var t=this.obj.getData();return t.hasOwnProperty(e)&&(delete t[e],this.saveDatas()),!0},saveDatas:function(){this.obj.save()},isSecure:function(){return this.obj.dataObj.secure},resetUnreadCount:function(){this.widget.setUnreadCount(0),this.widget.dispatchEvent("onResetUnreadCount")},addStar:function(t){switch(this.obj.dataObj.moduleName){case"RssReader":t.srcType="feed",t.srcTitle=this.obj.dataObj.title,t.srcUrl=this.obj.dataObj.feedUrl;break;case"UWA":t.srcType="uwa",t.srcTitle=this.obj.dataObj.title,t.srcUrl=this.obj.dataObj.data.moduleUrl;break;case"MultipleFeeds":this.obj.dataObj.data.url?(t.srcType="uwa",t.srcTitle=this.obj.dataObj.title,t.srcUrl="http://"+NV_HOST+"/modules/multipleFeeds/multipleFeeds.php?provider=custom&url="+e.Utils.encodeUrl(this.obj.dataObj.data.url)):(t.srcType="multiplefeeds",t.srcTitle=this.obj.dataObj.title,t.srcUrl=this.obj.dataObj.data.provider);break;default:t.srcType="uwa",t.srcModule=this.obj.dataObj.moduleName,t.srcTitle=this.obj.dataObj.title}t.moduleId=this.obj.dataObj.id,this.obj.dataObj.widgetId&&(t.widgetId=this.obj.dataObj.widgetId),App.Share.show(t)},openFeedReader:function(e,t,i){var n=App.FeedReader;return n&&n.display({feedObj:e,contentObj:i,selectedItemIndex:t}),!!n},updateFeedReader:function(e){var t=App.FeedReader;return t&&t.isOpen()&&t.notifyUpdates(e),!!t},mutate:function(e,t){var i,n,s=this.obj,o=App.Utils.isAbsoluteURL(e);n={moduleName:o?"UWA":e,data:t||{}},o&&(n.data.moduleUrl=e),s.previewMode?((i=Module.factory(n,{preview:!0})).inject(s.elm_module.parentNode).load(),s.elm_module.store("widget",i),s.remove(!1)):App.Panel.AddContent.addModule(n,{beforeModuleId:s.dataObj.id},function(e){e||s.remove(!0)})},getAnalytics:function(){return this.obj.getAnalytics()},supportsPotion:function(){return["socialpack_compare","socialpack_analytics","facebooksearchtrends","facebook","twitter","rssreader","multiplefeeds","todolist","weather","postit"].contains(this.obj.dataObj.moduleName.toLowerCase())&&Cookie.read("potionAvailable")},launchWizard:function(e){var t={type:"maximized",chrome:!1};e&&e.wizard&&(t.wizard=e.wizard),this.widget.requestView(t)}});return a.prototype.views.maximized=i.AbstractView.extend({isChromeLess:function(){return!1===this.event.chrome},isNotFullHeight:function(){return!1===this.event.fullHeight},_enter:function(e){var t,i=this,s=this.environment.html.wrapper;function a(){s.toggleClassName("maximized-view",e).toggleClassName("chromeless-view",e&&i.isChromeLess()).toggleClassName("chrome-view",e&&!i.isChromeLess()).toggleClassName("full-height",e&&!i.isNotFullHeight()),i._active=e,clearTimeout(t),t=null,s.removeEvent("transitionEnd",a).setOpacity(1),i.isChromeLess()||s[e?"addEvents":"removeEvents"](i.events),i.dispatchEvent(e?"onEnter":"onLeave")}this.events||(this.events={click:function(e){n.getElement(e)===s&&i.leave()}}),!o.Features.transitionsCSS||e&&!this.event.previous?a():(s.addEvent("transitionEnd",a).setOpacity(0),t=setTimeout(a,400))},enter:function(){e.$("body").addClass("nv-module-overflow"),this._enter(!0)},leave:function(){this._enter(!1)},isSingle:function(){return!0},destroy:function(){e.$("body").removeClass("nv-module-overflow"),this._parent()}}),a.prototype.views.collapsed=i.AbstractView.extend({_toggle:function(t){var i=t?e.i18n("Expand"):e.i18n("Collapse"),n=this.environment.obj;n.elm_module.toggleClassName("collapsed-view",t),n.elm_showHide.getElement("img").set({class:"actions-window-"+(t?"expand":"collapse"),alt:i,title:i}),this.dispatchEvent(t?"onEnter":"onLeave")},enter:function(){this._toggle(!0)},leave:function(){this._toggle(!1)}}),e.namespace("Environments/Netvibes",a,e)});