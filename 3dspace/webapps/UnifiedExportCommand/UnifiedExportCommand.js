define("DS/UnifiedExportCommand/Utils/CVExpandUtil",["DS/PADUtils/PADContext"],function(e){"use strict";const t=["identifier","physicalid","type","interface"],n=["identifier","physicalid","type","interface"];let o,i,s,l,a;var r={assemble:function(e){let t=-1;if(i.isGroupingApplied&&i.isGroupingApplied()){var n=e.getID();-1===(t=l.indexOf(n))&&(l.push(n),a.push([[n]]))}else{let n=e.getOccurencePath(),o=e.getRoot().getID();-1===(t=l.indexOf(o))?(l.push(o),a.push([n])):a[t]&&a[t].push(n)}},processSelectedNodes:function(e,t){e.forEach(e=>{if(e.isGroupingNode){var n=e.getChildren();n.length?r.processSelectedNodes(n,t):t(UWA.i18n("exchangecommand.notification.expansionfailed.groupnotexpanded"))}else r.assemble(e)})},extractPAD:function(t){if(o=e.get()){i=o.options.model?o.options.model:o.getPADTreeDocument(),(s=i.getSelectedNodes().filter(e=>e.getRoot())).length<1?t(UWA.i18n("exchangecommand.notification.selectitemstoexport")):(l=[],a=[],r.processSelectedNodes(s,t))}else t(UWA.i18n("exchangecommand.notification.nostructre"))},getExpandRequest:function(){for(var e={expands:[]},i=0;i<l.length;i++){for(var s=a[i],r=[],c=0;c<s.length;c++)r.push({physical_id_path:s[c]});var d=o.GetFilterSpec(l[i],{V2:{prefixPathArray:r}}),u={label:"pa4-unifiedexport-"+(new Date).getTime(),root:{physical_id:l[i]}};d&&(u.filter=d.filter),e.expands.push(u)}return{batch:e,outputs:{select_object:t,select_relation:n,format:"entity_relation_occurrence"}}}};return{getData:function(e){return r.extractPAD(e),r.getExpandRequest()}}}),define("DS/UnifiedExportCommand/Utils/ExchangeInfra",["UWA/Class","DS/WAFData/WAFData","DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices"],function(e,t,n){"use strict";return e.singleton({init:function(){let e=this;return e.getPlatformServices().then(e.getSecurityContext.bind(null,e)).then(e.getGrantedRoles.bind(null,e))},get:function(){return{platformServices:this.platformServices,securityContext:this.securityContext,grantedRoles:this.grantedRoles}},getPlatformServices:function(){let e=this;return new UWA.Promise(function(t,o){n.getPlatformServices({onComplete:e.postGetPlatformServices.bind(null,e,t)})})},postGetPlatformServices:function(e,t,n){let o=n.find(function(e){return e.platformId==widget.environment.getAllData().x3dPlatformId});o||(o=n[0]),e.platformServices=o,t()},getSecurityContext:function(e){return new UWA.Promise(function(n,o){t.authenticatedRequest(e.platformServices["3DSpace"]+"/resources/pno/person/getsecuritycontext",{method:"GET",type:"json",proxy:"passport",onComplete:e.postGetSecurityContext.bind(null,e,n)})})},postGetSecurityContext:function(e,t,n){e.securityContext=n.SecurityContext,t()},getGrantedRoles:function(e){return new UWA.Promise(function(t,o){n.getGrantedRoles(e.postGetGrantedRoles.bind(null,e,t))})},postGetGrantedRoles:function(e,t,n){e.grantedRoles=n,t()}})}),define("DS/UnifiedExportCommand/Views/MoreOptionsView",["UWA/Class/Collection","UWA/Class/View","DS/Controls/Button"],function(e,t,n){"use strict";let o=t.extend({tagName:"div",setup:function(e){this.model=e.model,this.setupContent()},setupContent:function(){let e=this;e.content=UWA.createElement("div",{class:"exportcommand-views-manageoptions-content"}).inject(e.container),e.label=UWA.createElement("span",{class:"exportcommand-views-manageoptions-content-label",html:e.model.get("label")}).inject(e.content),e.content.addEventListener("click",function(){e.model.destroy(),e.content.destroy()})}});return t.extend({tagName:"div",className:"exportcommand-views-manageoptions",setup:function(){try{let t=this;t.collection=new e,t.hide(),t.setupView(),t.listenTo(t.collection,"onAdd",function(e){1==t.collection.size()&&t.show(),new o({model:e}).inject(t.optionsListContainer)}),t.listenTo(t.collection,"onRemove",function(e){0==t.collection.size()&&t.container.hide()})}catch(e){console.log(e)}},setupView:function(){let e=this;e.btnMore=new n({label:UWA.i18n("exchangecommand.options.field.moreoptions"),displayStyle:"lite",icon:{iconName:"task-list-pencil",fontIconFamily:WUXManagedFontIcons.Font3DS}}).inject(e.container),e.optionsListContainer=UWA.createElement("div",{class:"exchangecommand-options-field-moreoptions-list"}),e.optionsListContainer.hide(),e.optionsListContainer.inject(e.container),e.btnMore.addEventListener("click",function(){e.optionsListContainer.isHidden()?(e.btnMore.checkFlag=!0,e.optionsListContainer.show()):(e.btnMore.checkFlag=!1,e.optionsListContainer.hide())})}})}),define("DS/UnifiedExportCommand/Views/ParameterView",["UWA/Class/Model","UWA/Class/View","DS/Controls/Abstract","DS/Controls/TooltipModel","DS/Controls/Popup","DS/Controls/Button"],function(e,t,n,o,i,s){"use strict";return t.extend({tagName:"div",className:"exportcommand-parameterview",setup:function(){try{this.model=new e}catch(e){console.log(e)}},defaults:function(e){},build:function(e,t){try{let n=this;return n.key=e.key,n.resolve=t,e.type?(n.defaults(e),require([e.type],function(t){(n.control=new t(e.options),e.custom)?n.control.inject(n.container):(n.control.getContent().setStyles({width:"100%"}),n.model.validate=e.validate,e.label?n.buildRow(e,n.control).inject(n.container):n.control.inject(n.container),n.control.addEventListener("change",e.change?e.change:t=>{let o=t.target.dsModel;null==o.checkFlag&&null==o.value||n.model.set(e.key,null!=o.checkFlag?o.checkFlag:o.value)}),"boolean"==UWA.typeOf(n.control.value)?(n.control.checkFlag=!e.value,n.control.checkFlag=e.value):(n.control.value=void 0,n.control.value=e.value));n.resolve&&n.resolve(n.control)})):e.hidden&&n.model.set(e.key,e.value),n.control}catch(e){console.log(e)}},buildError:function(e){return UWA.createElement("span",{class:"wux-ui-3ds wux-ui-3ds-1x wux-ui-3ds-alert exportcommand-parametersview-field-invalid",title:e||"Invalid Input",styles:{padding:2,marginLeft:5,borderRadius:"50%",color:"white",background:"#EA4F37"}})},buildDisable:function(){let e=this,t=new s({displayStyle:"lite",emphasize:"primary",icon:{iconName:"remove",fontIconFamily:WUXManagedFontIcons.Font3DS}});return t.addEventListener("click",()=>{e.destroy(),e.model.clear()}),t},buildRow:function(e,t){let n=this;try{let o=UWA.createElement("div",{class:"exportcommand-parametersview-row"}),i=(UWA.createElement("div",{class:"exportcommand-parametersview-col-label",html:n.buildLabel(e)}).inject(o),UWA.createElement("div",{class:"exportcommand-parametersview-col-value"}).inject(o));return n.buildValue(t,e).inject(i),o}catch(e){console.log(e)}},buildLabel:function(e){let t=UWA.createElement("table"),i=UWA.createElement("tr").inject(t);if(UWA.createElement("td",{class:"exportcommand-parametersview-col-label-text",styles:{fontWeight:"bold"},html:e.label}).inject(i),e.help){var s=UWA.createElement("td").inject(i),l=UWA.createElement("span",{class:"wux-ui-3ds wux-ui-3ds-1x wux-ui-3ds-help",styles:{fontSize:10,marginLeft:5,color:"#B4B6BA",position:"relative",top:-1}}),a=new n({tooltipInfos:new o({title:e.label,shortHelp:e.help,mouseRelativePosition:!0,position:"top"})});a.getContent().appendChild(l),a.inject(s)}return t},buildValue:function(e,t){var n=UWA.createElement("div",{styles:{display:"flex",justifyContent:"space-between"}});return UWA.createElement("div",{html:e,styles:{width:"90%"}}).inject(n),UWA.createElement("div",{html:t.optional?this.buildDisable():"",styles:{width:"8%"}}).inject(n),n},valid:function(){return!0},reset:function(){}})}),define("DS/UnifiedExportCommand/Views/ParametersView",["UWA/Class/Model","UWA/Class/Collection","UWA/Class/View","DS/UnifiedExportCommand/Views/MoreOptionsView","DS/UnifiedExportCommand/Views/ParameterView"],function(e,t,n,o,i){"use strict";let s=e.extend({}),l=n.extend({tagName:"div",className:"exportcommand-parametersview",setup:function(){try{let e=this;e.model=new s,e.collection=new t,e.setupOptions()}catch(e){console.log(e)}},setupOptions:function(){let e=this;e.optionsView=new o,e.optionsView.inject(e.container),e.optionsView.getContent().style.marginLeft="10px",e.optionsView.listenTo(e.optionsView.collection,"onRemove",function(t){e.insert2(t._attributes)})},insert:function(e,t){let n=this;e.optional?n.optionsView.collection.add(e):n.insert2(e,t)},insert2:function(e,t){let n=this,o=new i;n.collection.add(o.model),o.listenTo(o.model,"onChange",function(t){if(0==t.keys().length)n.optionsView.collection.add(e),n.model.unset(e.key);else{let e=t._attributes;n.model.set(e)}}),n.container.insertBefore(o.getContent(),n.optionsView.getContent()),o.build(e,t)}});return{getInstance:function(){return new l}}}),define("DS/UnifiedExportCommand/Models/Exchange",["UWA/Class/Model","DS/WAFData/WAFData","DS/Utilities/UUID","DS/PADUtils/PADContext","DS/UnifiedExportCommand/Utils/ExchangeInfra","DS/UnifiedExportCommand/Utils/CVExpandUtil","text!DS/UnifiedExportCommand/assets/config/UnifiedExportCommand.json"],function(e,t,n,o,i,s,l){"use strict";try{window.jasmine||i.init();let a=require.toUrl("");a=a.substr(0,a.lastIndexOf("?")),l=JSON.parse(l);const r="/cvservlet/progressiveexpand/v2?output_format=cvjson&xrequestedwith=xmlhttprequest";return e.extend({defaults:{title:"Export - "+n.v4()},preprocess:null,transform:null,setup:function(){let e=this,t=i.get();for(const n in t)e[n]=t[n]},authorize:function(){let e=this;return new UWA.Promise(function(t,n){e.exporters=[],e.grantedRoles=e.grantedRoles,l.forEach(t=>{e.validateExporter(t)&&e.exporters.push(t)}),e.exporters.length>0?t():n(UWA.i18n("exchangecommand.notification.notallowed"))})},validateExporter:function(e){return e&&e.value&&this.grantedRoles.some(function(t){return e.roles.indexOf(t.id)>-1})&&e.value&&e.service&&e.service.id&&e.service.url&&e.service.ui},expand:function(){let e=this,n=e.platformServices["3DSpace"]+r;return new Promise(function(o,i){let l=s.getData(i),a={Accept:"application/json","Content-Type":"application/json","Accept-Language":window.widget?window.widget.lang:"en","X-Requested-With":"XMLHttpRequest",SecurityContext:e.securityContext},r={data:JSON.stringify(l),method:"POST",headers:a,onComplete:e.postExpandSuccess.bind(null,e,o,i),onFailure:e.postExpandFailure.bind(null,e,i)};t.authenticatedRequest(n,r)})},postExpandSuccess:function(e,t,n,o){let i=(o=JSON.parse(o)).results;o.errors?n(UWA.i18n("exchangecommand.notification.exportfailed.message2")):i.length?(e.set("objects",i),t()):n(UWA.i18n("exchangecommand.notification.exportfailed.noresults"))},postExpandFailure:function(e,t,n){t(UWA.i18n("exchangecommand.notification.expansionfailed"))},save:function(){let e=this,t=o.get(),n=[];if(t){n=(t.options.model?t.options.model:t.getPADTreeDocument()).getSelectedNodes().filter(e=>e.getRoot())}let i=e.preprocess?e.preprocess.bind(null,{selectedNodes:n,securityContext:e.securityContext,platformServices:e.platformServices}):e=>e();return new UWA.Promise(i).then(e.export.bind(null,e))},export:function(e){return new Promise(function(n,o){let i=e.platformServices[e.service.id]+e.service.url,s=e.exportPayload(),l={Accept:"application/json","Content-Type":"application/json","Accept-Language":window.widget?window.widget.lang:"en","X-Requested-With":"XMLHttpRequest",SecurityContext:e.securityContext},a={data:JSON.stringify(s),method:"POST",headers:l,onComplete:e.success.bind(null,e,n),onFailure:e.failure.bind(null,e,o)};t.authenticatedRequest(i,a)})},exportPayload:function(){let e=this;if(e.transform)try{return e.transform.call(UWA.clone(e._attributes),{platformServices:e.platformServices,securityContext:e.securityContext})}catch(e){console.log(e)}return e._attributes},switchBaseUrl:function(e){let t=this;try{if(t.platformServices[e.service.id]){let n=[t.platformServices[e.service.id],e.service.base?e.service.base:"/webapps"].join("");require.toUrl(n),require.config({baseUrl:n})}}catch(e){console.log(e)}},resetBaseUrl:function(){require.config({baseUrl:a})},success:function(e,t,n){t&&t(UWA.i18n("exchangecommand.notification.exportstarted.message"))},failure:function(e,t,n){t&&t(UWA.i18n("exchangecommand.notification.exportfailed.message"))}})}catch(e){console.log()}}),define("DS/UnifiedExportCommand/Views/ParametersViewFactory",["UWA/Class","DS/UnifiedExportCommand/Views/ParameterView","DS/UnifiedExportCommand/Views/ParametersView"],function(e,t,n){"use strict";var o=e.extend({build:function(e){let t=this,o=n.getInstance();return e.forEach(e=>{e.successors?t.buildSuccessors(o,e):e.children?t.buildGroup(o,e):o.insert(e)}),o},buildSuccessors:function(e,n){let o,i=this,s=new t;s.inject(e.container),s.build(n);let l=[];s.listenTo(s.model,"onChange:"+n.key,function(t){if(e.model.set(t._attributes),n.successors){o&&o.container.empty();let s=n.successors.call(UWA.clone(t._attributes));s?(s.forEach(function(e){l.push(e.key)}),(o=i.build(s)).inject(e.container),o.listenTo(o.model,"onChange",function(t){l.forEach(function(t){e.model.unset(t)}),e.model.set(t._attributes)})):l.forEach(function(t){e.model.unset(t)})}})},buildGroup:function(e,n){let o=this;e.model.set(n.key,{}),new UWA.Promise(function(o){let i=new t;i.inject(e.container),i.build(n,o)}).then(function(t){n.children.forEach((i,s)=>{if(0==s&&(i.isSelected=!0),i.label&&(i.header=i.label),i.icon&&(i.icon={iconName:i.icon,fontIconFamily:WUXManagedFontIcons.Font3DS}),i.fields){let s=o.build(i.fields);i.content=s.getContent(),s.listenTo(s.model,"onChange",function(t){let o=t._attributes,s=UWA.clone(e.model.get(n.key));s[i.key]=o,e.model.set(n.key,s)}),t.add?t.add(i):t.addItem?t.addItem(i):s.inject(e.container)}}),t.expandItem&&t.expandItem(0)})}});return{getInstance:function(){return new o}}}),define("DS/UnifiedExportCommand/Views/ExchangeOptions",["UWA/Class/View","DS/Controls/Loader","DS/Utilities/UUID","DS/UnifiedExportCommand/Views/ParametersViewFactory"],function(e,t,n,o){"use strict";let i,s;return e.extend({tagName:"div",id:"exportcommand-optionsview",className:"exportcommand-optionsview",setup:function(e){try{this.setupMethods()}catch(e){console.log(e)}},setupMethods:function(){let e=this;if(e.model.exporters&&e.model.exporters.length){let t=null,n=[];e.model.exporters.forEach((e,o)=>{0==o&&(t=e),n.push({value:e.value,label:UWA.i18n(e.label)})}),(i=o.getInstance().build([{id:"type",label:UWA.i18n("exchangecommand.options.field.exportOutput"),help:UWA.i18n("exchangecommand.options.field.exportOutput.help"),type:"DS/Controls/ComboBox",value:widget.getValue("exportMethod")?widget.getValue("exportMethod"):t.value,options:{elementsList:n},change:e.changeOutput.bind(null,e)}])).inject(e.container)}},clear:function(){let e=this.model.get("objects");this.model.clear(),this.model.set("objects",e),s&&s.container.empty()},changeOutput:function(e,t){let n=t.target.dsModel.value;if(n){e.clear(),widget.setValue("exportMethod",n);for(let t of e.model.exporters)if(t.value==n){e.model.service=t.service,e.load(t);break}}},load:function(e){let n=this;try{n.loader=new t,n.loader.inject(n.container),n.loader.on(""),n.model.switchBaseUrl(e),require([e.service.ui],function(e){n.parse(e)})}catch(e){console.log(e)}},parse:function(e){let t=this;t.config=e,e.preprocess&&(t.model.preprocess=e.preprocess),e.transform&&(t.model.transform=e.transform);var n="function"==UWA.typeOf(e.fields)?e.fields.bind(null,{platformServices:t.model.platformServices,securityContext:t.model.securityContext}):t=>t(e.fields);new UWA.Promise(n).then(function(e){t.parseExpoterFields(e)})},parseExpoterFields:function(e){let t=this;t.loader.off();let n=UWA.clone(e);n=t.refine(n);let i=o.getInstance();(s=i.build(n)).inject(t.container),s.listenTo(s.model,"onChange",function(e){let n=e._attributes;t.model.set(n)})},refine:function(e){let t=[];return e.forEach((e,o)=>{if("title"==e.key){var i=n.v4();e.value="Export - "+i.substring(0,10).replaceAll("-",""),e.type="DS/Controls/LineEditor",e.label=UWA.i18n("exchangecommand.options.field.title"),t.splice(0,0,e)}else t.push(e)}),t}})}),define("DS/UnifiedExportCommand/Views/ExchangeProcessDialog",["UWA/Class/View","DS/Windows/Dialog","DS/Windows/ImmersiveFrame","DS/Controls/Button","DS/Notifications/NotificationsManagerUXMessages","DS/Notifications/NotificationsManagerViewOnScreen","DS/UnifiedExportCommand/Models/Exchange","DS/UnifiedExportCommand/Views/ExchangeOptions","i18n!DS/UnifiedExportCommand/assets/nls/UnifiedExportCommandNLS","css!DS/UnifiedExportCommand/UnifiedExportCommand"],function(e,t,n,o,i,s,l,a,r){"use strict";try{let c,d,u,m,p,f,g,h;return UWA.i18n(r),e.extend({tagName:"div",id:"exportcommand-exchangeprocessdialog",className:"exportcommand-exchangeprocessdialog",setup:function(e){let t=this;t.model=e&&e.model||new l,t.model.authorize().then(t.setupDialog.bind(null,t),t.reject.bind(null,t))},setupDialog:function(e){widget.addEvent("onRefresh",function(){d.close()}),e.model.expand().then(e.expansionResolved.bind(),e.reject.bind(null,e)),(u=new a({model:e.model})).getContent().children.length?(c=UWA.createElement("div",{class:"exchangecommand-dialogcontainer"}),u.inject(c),d&&d.close(),(m=new n).inject(e.container),(d=new t({title:UWA.i18n("exchangecommand.dialog.header"),content:c,immersiveFrame:m,modalFlag:!0,usePaddingFlag:!0,resizableFlag:!1,buttons:e.getFooter()})).addEventListener("close",e.afterClose.bind(null,e)),e.container.inject(widget.body),widget.addEvent("onResize",e.adjust),e.adjust()):e.exportNotAllowed("Select an item to start the Export")},expansionResolved:function(){h.stop(),p.hide(),f.disabled=!1},getFooter:function(){p=new o({displayStyle:"lite",label:UWA.i18n("exchangecommand.dialog.footer.button.ok")}),f=new o({label:UWA.i18n("exchangecommand.dialog.footer.button.save"),disabled:!0,onClick:this.save.bind(null,this)}),g=new o({label:UWA.i18n("exchangecommand.dialog.footer.button.cancel"),onClick:this.cancel.bind(null,this)});var e=p.getContent();return e.style.position="absolute",e.style.left="15px",e.style.cursor="default",(h=new UWA.Fx(e,{duration:1200,wait:!0,events:{onComplete:function(){h.tween("color","#005686","#B4B6BA")}}})).start(),{Ok:p,Save:f,Cancel:g}},save:function(e,t){let n=e;f.label=UWA.i18n("exchangecommand.dialog.footer.button.saving"),f.disabled=!0,n.model.save().then(n.resolve.bind(null,n),n.reject.bind(null,n))},resolve:function(e,t){d&&d.close(),t&&e.exportStarted(t)},reject:function(e,t){d&&d.close(),t&&e.exportNotAllowed(t)},cancel:function(e){d.close()},afterClose:function(e){m.destroy(),d.destroy(),e.model.resetBaseUrl(),e.model.dispatchEvent("closeexchange")},exportStarted:function(e){s.setNotificationManager(i),s.setFadeOutPolicy(2),s.setStackingPolicy(5);let t={message:e,level:"success"};i.addNotif(t)},exportNotAllowed:function(e){s.setNotificationManager(i),s.setFadeOutPolicy(2),s.setStackingPolicy(5);let t={message:e,level:"error"};i.addNotif(t)},adjust:function(){widget.body.clientWidth>360?c.style.minWidth="360px":c.style.minWidth="100%"}})}catch(e){console.log(e)}}),define("DS/UnifiedExportCommand/Commands/UnifiedExportCommand",["DS/ApplicationFrame/Command","DS/PADUtils/PADContext","DS/UnifiedExportCommand/Views/ExchangeProcessDialog"],function(e,t,n){return e.extend({init:function(e){let n=this;n._parent(e),n.disable(),n.model=e&&e.model,n.context=t.get(),n.treeDocument=n.context.options.model?n.context.options.model:n.context.getPADTreeDocument(),n.treeDocument._modelEvents.subscribe({event:"pushUpdate"},function(){if(arguments[0].options){let e=arguments[0].options.updateType;2!=e&&32!=e||n.toggleExport(n)}})},execute:function(){let e=this;e.disable();let t=new n({model:e.model});t.listenTo(t.model,"closeexchange",()=>{e.toggleExport(e)})},toggleExport:function(e){e.disable(),e.treeDocument.getSelectedNodes().find(e=>e.getRoot())&&e.enable()}})});