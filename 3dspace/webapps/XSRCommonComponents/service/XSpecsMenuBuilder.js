define("DS/XSRCommonComponents/service/XSpecsMenuBuilder",["UWA/Core","UWA/Class","UWA/Promise","DS/Menu/Menu","DS/XSRCommonComponents/utils/Utils","DS/XSRCommonComponents/utils/TypeUtils","DS/XSRCommonComponents/service/XSpecsActionsFactory","DS/XSRCommonComponents/utils/RequestUtil","DS/XSRCommonComponents/utils/Constants","text!DS/XSRCommonComponents/assets/config/subscribeActions.json","i18n!DS/XSRCommonComponents/assets/nls/XSRCommonComponents"],function(e,t,n,i,a,s,o,r,u,c,d){"use strict";return t.singleton({init:function(e){this._parent(e)},initialize:function(e){o.setOptions(e)},isTypeOf:function(e){},_nodeMenuBuilder:function(e,t,i){var a=this;return new n(function(n,s){n(a._buildMenu(e,t,i))})},_drawMenu:function(e,t,n,i,r,u,l,h,f,p,m,A,y){if(r.subCommand){var M=o.getActionMetaData(r.subCommand),_=u>1?"":r.access&&this.checkAccess(r.access)?"":"disabled";M&&(M.applicableAt.indexOf(atPlace)>=0||M.applicableAt.indexOf("all")>=0)?n.push(this._buildMenuEntry(o.getActionMetaData(i),"PushItem",e,_,i)):t.push(this._buildMenuEntry(o.getActionMetaData(i),"PushItem",e,_,i))}else{var b=u>1?"":r.access&&this.checkAccess(r.access)?"":"disabled";if("CompareWith"===i)u<=2&&t.push(this._buildMenuEntry(o.getActionMetaData(i),"PushItem",e,b,i));else if("DownloadReport"===i)b=""===b?y?"":"disabled":b,t.push(this._buildMenuEntry(o.getActionMetaData(i),"PushItem",e,b,i));else if("OpenWith"===i){var I=a._getSupportedAppsList();I&&I.length>0&&t.push(this._buildMenuGroup(o.getActionMetaData(i),"PushItem",I,e)),t.push({type:"SeparatorItem"})}else if("Subscriptions"===i){var g=[],S=JSON.parse(c).actions;S.forEach(function(t){var n=t.nls_key;g.push({type:"PushItem",title:d[n],fonticon:{content:"wux-ui-3ds wux-ui-3ds-1x wux-ui-3ds-"+t.fonticon,family:WUXManagedFontIcons.Font3DS},action:{this:this,callback:function(n){o.fireAction(t,e)}}})}),S&&S.length>0&&t.push(this._buildMenuGroup(o.getActionMetaData(i),"PushItem",g,e))}else if("VersionExplorer"===i||"ActionBar_ChangeOwner"===i||"DeleteItem"===i){if(t.push({type:"SeparatorItem"}),!(e.data instanceof Array)&&s.isPerformanceSpecChar(e.data.getTypeActualName())){let t=s.isPerformanceSpecification(e.data.getTypeActualName())?e.data:e.data.getParent();if("VersionExplorer"===i){b=t.isReleased&&t.isReleased()||t.isObsolete&&t.isObsolete()?"":"disabled"}else"DeleteItem"===i&&(b=t.isInWork&&t.isInWork()?"":"disabled")}t.push(this._buildMenuEntry(o.getActionMetaData(i),"PushItem",e,b,i))}else"MultiOwnership"===i?(t.push(this._buildMenuEntry(o.getActionMetaData(i),"PushItem",e,b,i)),t.push({type:"SeparatorItem"})):("addPerformanceCharacteristics"===i&&(b=e.data.isInWork()?"":"disabled"),t.push(this._buildMenuEntry(o.getActionMetaData(i),"PushItem",e,b,i)))}n.length>0&&menuNotPushed&&(t.push(this._buildMenuGroup(o.getActionMetaData(r.subCommand),"PushItem",n,e)),menuNotPushed=!1)},_buildMenu:function(e,t,n){var i=this;!e.data||void 0!==e.data.length&&1!==e.data.length||a._setSupportedAppsList(e.data);var s=function(e,s){var o,r=!1;if(Array.isArray(e.data))o=e.data.length,r=1!=o||!!e.data[0].isReportCheckedIn&&e.data[0].isReportCheckedIn();else{if(o=1,!e.data.getID)return;i.currentAccess=e.data.getCurrentAccess(),r=!!e.data.isReportCheckedIn&&e.data.isReportCheckedIn()}var c=[],d=[];return Object.keys(s).forEach(function(l){var h=l,f=s[l],p=f.applicableType;let m=f.excludeType;var A=f.applicableAt,y=i.isApplicableType(e.data,p);if(!i.isExcludeType(e.data,m)&&y&&A&&(A.indexOf(t)>=0||A.indexOf("all")>=0)){var M=!!(n&&n.length>0&&n.indexOf(h)>=0),_=f.isVisibleForMEI,b=a.isMEIItem(e.data);if(e.data.length&&e.data.length>1&&b){if("SetPartNumber"===h||"DownloadReport"===h||"OpenSpecificationView"===h)return;i._drawMenu(e,c,d,h,f,o,p,A,y,M,_,b,r)}else if(e.data.length&&1===e.data.length){var I=a.isMEIItem(e.data[0]);M||"undefined"===e.data[0].currentTabKey||e.data[0].currentTabKey===u.MEI_TAB||I?I&&_&&(b||e.data[0].currentTabKey===u.MEI_TAB)&&A&&(A.indexOf(t)>=0||A.indexOf("all")>=0)&&i._drawMenu(e,c,d,h,f,o,p,A,y,M,_,I,r):i._drawMenu(e,c,d,h,f,o,p,A,y,M,_,I,r)}else M||"undefined"===e.data.currentTabKey||e.data.currentTabKey===u.MEI_TAB||b?b&&_&&(e.data.currentTabKey===u.MEI_TAB||b)&&A&&(A.indexOf(t)>=0||A.indexOf("all")>=0)&&i._drawMenu(e,c,d,h,f,o,p,A,y,M,_,b,r):i._drawMenu(e,c,d,h,f,o,p,A,y,M,_,b,r)}}),c};return Array.isArray(e.data)&&e.data.length>1?s(e,o.getMultiSelActions()):s(e,o.getAllActions())},isApplicableType:function(e,t){var n=!1,i=function(e){return s.isPhysicalProduct(e)?-1!==t.indexOf("PhysicalProduct")||-1!==t.indexOf("all"):!(!a.isDocument(e)||-1===t.indexOf("Documents")&&-1===t.indexOf("all"))||(!(!s.isTechnicalSpecification(e)||-1===t.indexOf("TechnicalSpecs")&&-1===t.indexOf("all"))||(!(!a.isCoreMaterial(e)||-1===t.indexOf(u.TYPE_CORE_MATERIAL)&&-1===t.indexOf(u.TYPE_CORE_MATERIAL_NAME)&&-1===t.indexOf("all"))||(!!(a.isPerformanceSpec(e)&&-1!==t.indexOf(u.TYPE_PERFORMANCE_SPEC)||a.isPerformanceChar(e)&&-1!==t.indexOf(u.TYPE_PERFORMANCE_CHAR))||-1!==t.indexOf("all"))))};if(Array.isArray(e))for(var o=0;o<e.length;o++){if(!(n=i(e[o].getTypeActualName())))break}else if(e){n=i(e.getTypeActualName()||e.getType())}return n},isExcludeType:function(e,t){return!(e instanceof Array)&&!!t.includes(e.getTypeActualName())},_getMenuState:function(e,t,n){if(t.data&&"RemoveChild"!==n)if(t.data instanceof Array)for(var i=0;i<t.data.length;i++){var s=t.data[i];if(a.isOriginatedInGLS(s)){e="disabled";break}}else e=a.isOriginatedInGLS(t.data)?"disabled":e;return e},_buildMenuEntry:function(e,t,n,i,s){var u,c,l=e.nls_key;r.isSafarionIPadorPod()?(c=e.touchFonticon?e.touchFonticon:e.fonticon,l=e.touch_nls_key?e.touch_nls_key:e.nls_key,u=e.touchIcon?a.getIconFormXSR(e.touchIcon):""):(c=e.fonticon,l=e.nls_key,u=e.icon?a.getIconFormPerfSpec(e.icon):""),i=this._getMenuState(i,n,s);var h={title:d[l],type:t,state:i,action:{this:this,callback:function(t){o.fireAction(e,n)}}};return c?h.fonticon={family:"DSFontIcon",content:"wux-ui-3ds wux-ui-3ds-2x wux-ui-3ds-"+c}:h.icon=u,h.fIcon=c,h.cmdID=s,h},_buildMenuGroup:function(e,t,n,i){var s,o=e.nls_key;r.isSafarionIPadorPod()?(s=e.touchFonticon?e.touchFonticon:e.fonticon,o=e.touch_nls_key?e.touch_nls_key:e.nls_key,e.touchIcon&&a.getIconFormXSR(e.touchIcon)):(s=e.fonticon,o=e.nls_key);var u=this._getMenuState("enabled",i,""),c={title:d[o],type:t,submenu:n,state:u};return c.fonticon={family:"DSFontIcon",content:"wux-ui-3ds wux-ui-3ds-2x wux-ui-3ds-"+s},c},checkAccess:function(e){var t=!0;if(this.currentAccess)for(var n=0;n<e.length;n++){var i=e[n];if(!(t=this.currentAccess.indexOf("all")>=0||-1!==this.currentAccess.indexOf(i)))break}return t},showMenu:function(e,t){e&&Array.isArray(t)&&i.show(t,{position:{x:e.x?e.x:e.left,y:e.y?e.y:e.bottom}})},getIdCardMenu:function(e,t){var n=this;if(!t)return this._nodeMenuBuilder(e,"ItemIDCard");this._nodeMenuBuilder(e,"ItemIDCard",["Information"]).then(function(e){n.showMenu(t,e)})},getSpecListMenuView:function(e,t){var n=t||[];return a.hasAdminDisabledTemplate&&n.push("DownloadReport"),this._nodeMenuBuilder(e,"LandingPageActions",n)},getTabActions:function(e,t,n){var i=n||[];return a.hasAdminDisabledTemplate&&i.push("DownloadReport"),this._build(e,t,"ItemTabActions",i)},getQuickViewMenu:function(e,t,n){return this._build(e,t,"ItemTabQuickView",n)},_build:function(e,t,n,i){var a=this;if(!t)return this._nodeMenuBuilder(e,n);this._nodeMenuBuilder(e,n,i).then(function(e){a.showMenu(t,e)})}})});