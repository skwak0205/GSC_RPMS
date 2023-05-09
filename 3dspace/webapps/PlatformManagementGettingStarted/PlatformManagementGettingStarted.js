define("DS/PlatformManagementGettingStarted/View/WelcomePlatformView",["UWA/Core","UWA/Class/View","DS/UIKIT/DropdownMenu","DS/UIKIT/Input/Text","DS/UIKIT/Input/Button","DS/PlatformAPI/PlatformAPI","DS/PlatformManagement/Utils","i18n!DS/PlatformManagementGettingStarted/assets/nls/PlatformManagementGettingStarted"],function(t,e,n,a,o,i,s,r){"use strict";return e.extend({setup:function(e){this.platform=e.platform,this.platformCollection=e.platformCollection,this.container=t.createElement("div",{class:"welcome-box"})},render:function(){return this.container.setContent(this._createTemplate()),this._buildSelector(),this},_buildSelector:function(){var t=this,e=[{id:"rename",fonticon:"pencil",text:r.get("Rename")}];new n({target:t.selectIcon,renderTo:this.container,position:"bottom right",items:e,events:{onClick:function(e,n){"rename"===n.id&&(t.container.getElement(".welcome-edit-platform").removeClassName("hidden"),t.container.getElement(".welcome-text-platform-selector").addClassName("hidden"),t.container.getElement("#platform-name").focus())},onClickOutside:function(e){t.selectIcon.hasClassName("item-active")?t.selectIcon.removeClassName("item-active"):t.selectIcon.addClassName("item-active")}}})},_createTemplate:function(){return this.selectIcon=t.createElement("i",{tag:"i",class:"fonticon fonticon-open-down pointer",events:{click:function(){this&&(this.hasClassName("item-active")?this.removeClassName("item-active"):this.addClassName("item-active"))}}}),[{class:"welcome-text-head",text:r.get("Welcome to your platform")},{class:"welcome-text-platform",html:[{class:"welcome-text-platform-selector",html:[{tag:"span",text:this.platform.get("name")},this.selectIcon]},this._buildEditZone()]},{class:"welcome-text",styles:{"font-weight":"bold"},text:r.get("You have been selected as an administrator to manage users & roles.")},{class:"welcome-text",text:s.isLightweight()?r.get("This is the Members Management dashboard."):r.get("This is the Platform Management dashboard.")},{tag:"ul",styles:{"list-style-type":"disc"},html:[{tag:"li",class:"welcome-text",text:r.get("Begin by going to the next tab, “Members”, where you can invite other users and give them access to apps by granting them roles.")},{tag:"li",class:"welcome-text",text:r.get("Be sure to give yourself the roles you need, too.")},{tag:"li",class:"welcome-text",text:r.get("Use the 3DDashboard left panel to switch to a different dashboard, such as My First Dashboard. You can also create your own dashboard.")},{tag:"li",class:"welcome-text",text:r.get("Find your apps in Compass. Some apps can be dragged onto the dashboard, and some launch in other windows.")}]}]},_buildEditZone:function(){var t=this;return{class:"welcome-edit-platform hidden",html:[new a({id:"platform-name",value:this.platform.get("name"),attributes:{maxlength:45,events:{keyup:function(e){13===e.keyCode&&t._onSavePlatformName()}}}}),{styles:{"text-align":"right"},html:[new o({value:r.get("Save"),attributes:{styles:{"margin-right":10}},className:"primary",events:{onClick:this._onSavePlatformName.bind(this)}}),new o({value:r.get("Cancel"),events:{onClick:this._onCancelPlatformName.bind(this)}})]}]}},_onSavePlatformName:function(){var t=this,e=this.container.getElement("#platform-name").value;if(e&&(e=e.trim()),e.length>=3&&e.length<=45&&e!==this.platform.get("name"))this.dispatchEvent("onLoadingStart"),this.platform.save({id:this.platform.get("id"),name:e},{patch:!0,onComplete:this.onComplete.bind(this),onFailure:function(){t.dispatchEvent("onLoadingEnd")}}),this.platformCollection.get(this.platform.get("id")).set({name:e});else{if(e!==this.platform.get("name"))return;this.onComplete()}},_onCancelPlatformName:function(){this.container.getElement(".welcome-edit-platform").addClassName("hidden"),this.container.getElement(".welcome-text-platform-selector").removeClassName("hidden"),this.container.getElement(".fonticon-open-down").removeClassName("item-active")},onComplete:function(){this.dispatchEvent("onLoadingEnd"),this._onCancelPlatformName(),this.dispatchEvent("onPlatformNameSaved"),i.publish("PlatformManagement.renamePlatformName",this.platform.get("name"))}})}),define("DS/PlatformManagementGettingStarted/View/StatusView",["UWA/Core","UWA/Class/View","DS/PlatformAPI/PlatformAPI","DS/UIKIT/Tooltip","i18n!DS/PlatformManagementGettingStarted/assets/nls/PlatformManagementGettingStarted","DS/PlatformManagement/Utils"],function(t,e,n,a,o,i){"use strict";return e.extend({tagName:"div",className:"content-no-overflow",setup:function(t){t.platform&&(this.platform=t.platform),t.model&&(this.model=t.model)},render:function(){return this.cleanTooltips(),this.container.setContent(this._createTemplate()),this},cleanTooltips:function(){this.tooltips&&this.tooltips.length>0&&this.tooltips.invoke("destroy"),this.tooltips=[]},_createTemplate:function(){return{class:"content-wrapper",html:{tag:"content",html:[this._createTitle(),this._createBlocks(),this.options.button?this.options.button:null,{class:"clearfix"}]}}},_createTitle:function(){return{tag:"h3",html:[{tag:"i",class:"fonticon fonticon-"+this.model.get("icon"),styles:{color:"#77797C"}},{tag:"span",text:this.model.get("title")}]}},_formatNumber:function(t){return t||(t=0),t.toString().replace(/\B(?=(\d{3})+(?!\d))/g," ")},_addCursor:function(t){return t.get("isClickable")&&t.get("isClickable").call(t)?" pointer ":""},_createBlocks:function(){var e=this,n=[],o=this.model.get("data"),i=!1;return o.forEach(function(t){parseInt(t.get("count"),10)>=999&&(i=!0)}),o.forEach(function(s){var r=s.get("title"),l=s.get("subtitle"),m=s.get("tooltip"),c=t.createElement("div",{class:"stat"+(s.get("warn")?" warn":"")+e._addCursor(s),styles:{width:"calc("+100/o.length+"% - 10px)"},html:[{class:"count"+(i?" small":""),text:e._formatNumber(s.get("count"))},{class:"title",text:r},l?{class:"subtitle",text:l}:null],events:{click:function(t){e._navigateTabs(t.target,s.get("navigation"))}}});m&&e.tooltips.push(new a({position:"top",target:c,body:m})),n.push(c)}),{class:"blocks",html:n}},_navigateTabs:function(t,e){t.getParent().hasClassName("pointer")&&e&&(n.publish("tab:switchto",{name:"Members"}),e&&(e.indexOf("pending")&&sessionStorage.setItem("refresh-pending",!0),n.publish("PlatformManagement.navigate",e)),"Pending Requests"!==t.nextSibling.innerHTML&&"Pending Requests"!==t.innerHTML||sessionStorage.setItem("pending-requests",!0),window.parent.location.hash=window.parent.location.hash.split("tab:")[0]+"tab:Members/"+e)}})}),define("DS/PlatformManagementGettingStarted/View/StorageView",["UWA/Core","UWA/Class/View","DS/UIKIT/Spinner","DS/i3DXCompassCore/Model/i3DXCompassModel","DS/i3DXCompassCore/CacheManager","DS/UIKIT/Input/Button","DS/WebappsUtils/WebappsUtils","i18n!DS/PlatformManagementGettingStarted/assets/nls/PlatformManagementGettingStarted"],function(t,e,n,a,o,i,s,r){"use strict";var l=a.extend({url:"disk",parse:function(t){return t&&(t.consumptionFormatted=this._bytesFormat(t.consumption),t.quotaFormatted=this._bytesFormat(t.quota)),t},getPercentage:function(){var t=!this.get("quota")||0===parseInt(this.get("quota"),10)||isNaN(this.get("quota"))?0:Math.round(Math.round(100*this.get("consumption"))/this.get("quota"));return this.get("consumption")>0&&(!this.get("quota")||0===parseInt(this.get("quota"),10)||isNaN(this.get("quota")))&&(t=100),t},hasError:function(){return t.is(this.get("code"))&&0!==this.get("code")},isEmpty:function(){return!t.is(this.get("quota"))},_bytesFormat:function(t){var e,n,a,o="",i=["B","KB","MB","GB","TB","PB","EB","ZB","YB"];for(a=0;a<=i.length;a++)if(e=Math.pow(1e3,a),n=Math.pow(1e3,a+1),t>=e&&t<n){o+=i[a],e>0&&(t/=e);break}return Math.round(10*t)/10+o}});return e.extend({tagName:"div",className:"content-no-overflow",setup:function(t){this.model=new l,this.platform=t.platform,this.listenTo(this.model,"onChange",this._renderTemplate)},render:function(){return this._renderTemplate(),this.model.fetch({data:{platform:this.platform.get("id")},cacheTTL:o.LONG_LIFE_TIME,onFailure:function(t,e){t.isEmpty()&&(e.code?t.set(t.parse(e)):t.set("code",1))}}),this},_renderTemplate:function(){this.container.setContent(this._createTemplate())},percentageStorage:function(){var t=this.model.getPercentage();return t<80?0:t>90?1:2},colourPercentageStorage:function(){return[""," warn-full"," warn"][this.percentageStorage()]},colourStorageUsed:function(){return[""," warn-full-storage-used"," warn-storage-used"][this.percentageStorage()]},_buildUpgradeStorage:function(e){return this.upgradeStorage||(this.upgradeStorage=t.createElement("div",{styles:{"text-align":"right"},html:""})),e&&this.upgradeStorage.setContent(new i({value:r.get("Upgrade Storage"),icon:"shopping-cart",events:{onClick:function(){e.whenReady().then(function(){e.openModal()})}}})),this.upgradeStorage},_createTemplate:function(){return{class:"content-wrapper",html:{tag:"content",html:[this._createTitle(),this._createBlocks(),this._buildUpgradeStorage(),{class:"clearfix"}]}}},_createTitle:function(){return{tag:"h3",html:[{tag:"i",class:"fonticon fonticon-"+this.options.config.get("icon"),styles:{color:"#77797C"}},{tag:"span",text:this.options.config.get("title")}]}},_createBlocks:function(){var e;return e=this.model.isEmpty()&&!this.model.hasError()?(new n).show():this.model.hasError()?"":t.String.format(r.get("Used: {0}"),this.model.get("consumptionFormatted")),[{class:"gauge",html:[{class:"gauge-blue"+this.colourPercentageStorage(),styles:{width:Math.min(this.model.getPercentage(),100)+"%"},text:this.model.getPercentage()+"%"},{class:"gauge-free",styles:{width:100-Math.min(this.model.getPercentage(),100)+"%"}},{class:"clearfix"}]},{class:"storage-used"+this.colourStorageUsed(),html:e},{class:"storage-capacity",text:this.model.isEmpty()?"":t.String.format(r.get("Total capacity: {0}"),this.model.get("quotaFormatted"))}]}})}),define("DS/PlatformManagementGettingStarted/PlatformManagementGettingStarted",["UWA/Core","UWA/Class/View","UWA/Class/Model","DS/PlatformManagementGettingStarted/View/StatusView","DS/PlatformManagementGettingStarted/View/StorageView","DS/UIKIT/Input/Button","DS/i3DXCompassCore/Model/i3DXCompassModel","DS/i3DXCompassCore/CacheManager","DS/PlatformManagementGettingStarted/View/WelcomePlatformView","DS/UIKIT/Mask","DS/PlatformManagement/Modal/InviteMember","DS/PlatformManagement/Utils","DS/PlatformAPI/PlatformAPI","i18n!DS/PlatformManagementGettingStarted/assets/nls/PlatformManagementGettingStarted"],function(t,e,n,a,o,i,s,r,l,m,c,g,d,h){"use strict";var u=s.extend({url:"platform/stat"}),f=function(){return this.get("count")>0};return e.extend({tagName:"div",render:function(){var e=[],s={class:"footer-wrapper",html:{class:"footer",text:this.options.platform.get("id")}},g=new n({title:h.get("Members"),icon:"users",data:[new n({title:h.get("Members"),count:0,navigation:"invite-grant-roles"}),new n({title:h.get("Pending Invitations"),count:0,navigation:"invite-grant-roles/pending",warn:!1,isClickable:f}),new n({title:h.get("Remaining Invitations"),count:0,navigation:"invite-grant-roles",warn:!1})]}),p=new n({title:h.get("Roles"),icon:"role-key",data:[new n({title:h.get("Granted"),navigation:"invite-grant-roles/granted-roles",count:0,isClickable:f}),new n({title:h.get("Capacity almost reached"),navigation:"invite-grant-roles/capacity-almost-reached",count:0,isClickable:f}),new n({title:h.get("Expiring Soon"),navigation:"invite-grant-roles/roles-expire-soon",count:0,isClickable:f})]}),w=new n({title:h.get("Pending Requests"),icon:"bell",data:[new n({title:h.get("Pending Requests"),count:0,navigation:"manage-requests",warn:!1,isClickable:f})]}),S=this,v=new a({model:g,platform:this.options.platform}),P=new a({model:p}),C=new a({model:w});return e.push(v.render()),e.push(P.render()),e.push(C.render()),(new u).fetch({data:{platform:S.options.platform.get("id")},cacheTTL:r.LONG_LIFE_TIME,onFailure:function(t,e){},onComplete:function(e){(S.options.platform&&S.options.platform.get("crossCompany")||e.get("remaining")>0)&&(v.options.button={styles:{"text-align":"right"},html:new i({value:h.get("Invite Members"),icon:"user-add",events:{onClick:function(){this.modal=new c({lang:S.options.widget.lang,hasApp:!0,platform:S.options.platform,widget:S.options.widget,onAdded:function(){S.render(),d.publish("PlatformManagement.refresh",S.options.widget.id)}})}}})}),g.get("data")[0].set({subtitle:t.String.format(t.i18n("({0} Administrators)"),e.get("admin")),count:e.get("member")+e.get("admin")}),g.get("data")[1].set({count:e.get("pending"),warn:e.get("pending")>0}),g.get("data")[2].set({count:Math.max(e.get("remaining"),0),warn:e.get("remaining")<=0}),p.get("data")[0].set("count",e.get("role")),p.get("data")[1].set({tooltip:h.get("Only roles whose usage capacity exceeds 90%"),count:e.get("nearlyFull"),warn:e.get("nearlyFull")>0}),"OnPremise"!==S.options.platform.get("id")&&p.get("data")[2].set({tooltip:h.get("Only roles whose license will expire in less than 30 days"),count:e.get("expire"),warn:e.get("expire")>0}),w.get("data")[0].set({count:e.get("request"),warn:e.get("request")>0}),v.render(),P.render(),C.render()}}),this.options.platform.isOnPremise()||e.push(new o({platform:this.options.platform,config:new n({title:h.get("Storage"),icon:"database"})}).render()),e.push(s),this.container.setContent([{class:"body-wrap",html:[new l({platformCollection:this.options.platformCollection,platform:this.options.platform,events:{onLoadingStart:function(){t.log("onLoadingStart"),m.mask(S.container)},onLoadingEnd:function(){t.log("onLoadingEnd"),m.unmask(S.container)},onPlatformNameSaved:function(){S.options.widget.dispatchEvent("onLoad"),t.log("onPlatformNameSaved")}}}).render(),{class:"status-boxs",html:e}]}]),this}})}),define("DS/PlatformManagementGettingStarted/Widgets/PlatformManagementGettingStartedWdg",["UWA/Core","UWA/Class","DS/UIKIT/Mask","DS/i3DXCompassCore/RequestOptions","DS/PlatformManagementComponents/Collection/Platform","DS/i3DXCompassCore/Model/PlatformModel","DS/PlatformManagement/Utils","DS/W3DXComponents/Views/Layout/ScrollView","DS/PlatformManagementGettingStarted/PlatformManagementGettingStarted","DS/PlatformManagement/Model/I18nModel","DS/PlatformAPI/PlatformAPI","DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices","i18n!DS/PlatformManagementGettingStarted/assets/nls/PlatformManagementGettingStarted","css!DS/PlatformManagementGettingStarted/PlatformManagementGettingStarted","css!DS/PlatformManagement/PlatformManagement"],function(t,e,n,a,o,i,s,r,l,m,c,g){"use strict";return e.extend({widget:null,init:function(t){this.widget=t.widget,this.platform=t.platform,a.setupLanguage(this.widget.lang),s.setWidget(this.widget),this._initWidgetEvents()},_initWidgetEvents:function(){this.widget.addEvents({onLoad:this._onBeforeLoad.bind(this)})},_onBeforeLoad:function(){var e=this;"en"!==this.widget.lang?(new m).fetch({onComplete:function(n,a){t.i18n(a),e._initWidgetPreferences()},onFailure:function(){e._initWidgetPreferences()}}):e._initWidgetPreferences()},_initWidgetPreferences:function(){var t=this,e=t.widget;n.mask(e.body),this.collection=new o({includeWu:!1,lang:this.widget.lang}),g.getUser({onComplete:function(e){var n=e.platforms;n&&n.length>0&&(n=n.filter(function(t){return"admin"===t.role})),g.getPlatformServices({onComplete:function(e){var a=[];n.forEach(function(t){var n=e.detect(function(e){return e.platformId===t.id});n&&a.push({id:n.platformId,displayName:n.displayName})}),t.collection.set(a),t._getCurrentPlatform().fetch({data:{id:t._getCurrentPlatform().get("id")},onComplete:function(e,n){var a=t.collection.get(t._getCurrentPlatform().get("id"));a&&a.set(n),t._onLoad()}})}})}})},_getCurrentPlatform:function(){var t,e=this.widget.getValue("x3dPlatformId");return e&&""!==e&&(t=this.collection.get(e)),this.platform||(this.platform=new i(t?t.toJSON():this.collection.first().toJSON())),this.platform},_onLoad:function(){var e=this,a=this.widget.id,o=this.widget.body,i=this._getCurrentPlatform();this.widget.setTitle(""),s.setCurrentPlatform(e._getCurrentPlatform()),n.unmask(o),o.setContent(t.createElement("div",{class:"getting-started",html:new r({useInfiniteScroll:!1,usePullToRefresh:!1,view:new l({widget:this.widget,platformCollection:this.collection,platform:i})}).render()})),c.subscribe("PlatformManagement.refresh",function(t){t!==a&&e._onLoad()})}})});