define("DS/DocumentWebEdition/Actions/OfficeWebEditorSave",["UWA/Core","i18n!DS/DocumentWebEdition/assets/nls/DocumentWebEdition"],function(e,t){"use strict";var n=function(e){return{id:"editorsave",dataElements:{typeRepresentation:"functionIcon",icon:{iconName:"floppy",fontIconFamily:1},label:t.get("WebEditorSave"),tooltip:t.get("WebEditorSave"),position:"far",category:"stable area",value:function(t){t.options=e,n.execute(t)},visibleFlag:!0},execute:function(){n.execute({options:e})},updateForSelection:function(t,n){t({visibleFlag:1==e.getSelectedNodes().length})}}};return n.execute=function(e){e.options.ctrl.saveOfficeEditorFile()},n}),define("DS/DocumentWebEdition/Actions/OfficeWebEditorClose",["DS/UIKIT/Modal","DS/DocumentCommands/Helper","i18n!DS/DocumentWebEdition/assets/nls/DocumentWebEdition"],function(e,t,n){"use strict";var i=function(e){return{id:"OfficeWebEditorClose",dataElements:{docV2Preview:!0,typeRepresentation:"functionIcon",icon:{iconName:"close",fontIconFamily:1},label:n.get("Close"),tooltip:n.get("Close"),position:"far",category:"stable area",priority:10,value:function(t){t.options=e,i.execute(t)},visibleFlag:!0},execute:function(){i.execute({options:e})},updateForSelection:function(t,n){t({visibleFlag:1==e.getSelectedNodes().length})}}};return i.execute=function(i){var o=i.options.ctrl,s=i.options.getSelectedNodes()[0],a=i.options.getDocumentFromNodeModel(s);t.IsWebEditionDocumentSessionActive(i.options.platformUrls.platformId,a.id).then(t=>{if(t&&t.is_modified){var i='<div style="display: flex;"><span class="fonticon fonticon-attention text-warning fonticon-2x"></span><p style="margin-left: 10px; white-space: pre-line">'+n.get("WebEditorConfirmMessageOnClose")+"</p></div>",s=new e({className:"site-reset",header:n.get("SaveDocument"),body:i,footer:'<button type="button" value="save" class="btn btn-primary">'+n.get("WebEditorSave")+'</button> <button type="button" value="closewithoutsave" class="btn btn-default">'+n.get("WebEditorCloseWithoutSave")+'</button><button type="button" value="cancel" class="btn btn-default">'+n.get("Cancel")+"</button>",animate:!0,resizable:!0,autocenter:!0,visible:!0,limitParent:!0,overlay:!0,closable:!1});s.show(),s.elements.container.setStyle("z-index","50000"),s.inject(widget.body),s.getContent().getElements(".btn").forEach(function(e){"save"===e.value?e.addEvent("click",function(){s.hide(),s.destroy(),o.saveOfficeEditorFile().then(()=>{o.close()})}):"closewithoutsave"===e.value?e.addEvent("click",function(){o.close(),s.hide(),s.destroy()}):"cancel"===e.value&&e.addEvent("click",function(){s.hide(),s.destroy()})})}else o.close()})},i}),define("DS/DocumentWebEdition/OfficeWebEditorToolbar",["UWA/Core","DS/Tweakers/GeneratedToolbar","DS/DocumentWebEdition/Actions/OfficeWebEditorClose","DS/DocumentWebEdition/Actions/OfficeWebEditorSave"],function(e,t,n,i){"use strict";return e.Class.extend({init:function(o,s){let a;var r={fetchSecurityContext:function(){return o.options.securityContext},getWorkUnderHeaders:function(){return{}},getSelectedNodes:function(){return[o.options.nodeModel]},getDocumentFromNodeModel:function(e){return o.options.document},getOrigin:function(){return{source:e.is(widget.data.appId)?widget.data.appId:widget.environment.data._data._x_appId,widgetId:widget.id,component:"ODEtoolbar"}},platformUrls:o.options.platformUrls,events:o.events,ctrl:o};this.commands={ODEClose:new n(r),ODESave:new i(r)};var c={entries:[this.commands.ODESave,this.commands.ODEClose]};a=this.FixedToolbarTreeDocument=t.prototype.createTreeDocument(c),(this.FixedToolbar=new t({overflowManagement:"dropdown",direction:"horizontal",items:a})).inject(s)}})}),define("DS/DocumentWebEdition/OfficeWebEditorTopbar",["UWA/Core","DS/DocumentWebEdition/OfficeWebEditorToolbar"],function(e,t){"use strict";function n(e){return e.title||e.name||e.dataelements.title||e.dataelements.name}return function(i){var o={ctrl:void 0,build:function(t){this.ctrl=t,this.container=e.createElement("div",{class:t.getClassNames("-topbar")}),this.container.inject(t.elements.content),this.imgDiv=e.createElement("div",{class:"topbar-image-container"}).inject(this.container),this.img=e.createElement("div",{class:"topbar-image"}).inject(this.imgDiv),this.titleDiv=e.createElement("div",{class:"topbar-title-container",text:n(t.options.document)}).inject(this.container),t.topBar=o},setTitleText:function(e){this.titleDiv.setText(e),this.titleDiv.title=e},fillData:function(){this.setTitleText(n(i.options.document)),this.img.style.backgroundImage="url("+(i.options.document.typeicon||i.options.document.dataelements.indexedTypeicon||i.options.document.dataelements.typeicon)+")",this.recreateToolbar()},recreateToolbar:function(){null==this.ODEToolbar&&(this.ODEToolbar=new t(this.ctrl,this.container))},resize:function(e){this.recreateToolbar()}};return o.build(i),o}}),define("DS/DocumentWebEdition/OfficeWebEditorComponent",["UWA/Core","DS/Core/ModelEvents","DS/DocumentWebEdition/OfficeWebEditorTopbar","DS/UIKIT/Mask","DS/DocumentManagement/DocumentManagement","DS/Notifications/NotificationsManagerUXMessages","DS/OfficeDocumentEditorClient/Editor","DS/WAFData/WAFData","DS/DocumentCommands/Helper","UWA/Promise","i18n!DS/DocumentCommands/assets/nls/DocumentCommands","i18n!DS/DocumentWebEdition/assets/nls/DocumentWebEdition","css!DS/DocumentWebEdition/DocumentWebEdition.css"],function(e,t,n,i,o,s,a,r,c,l,d,u,m){"use strict";return e.Controls.Abstract.extend({name:"document-webedition",options:{className:""},init:function(e){this._parent(e),this.e6wDocument=e.document,this.events=e.modelEvents||new t,this.notificationManager=s,this.buildSkeleton();var n=[];this.e6wDocument.relateddata&&this.e6wDocument.relateddata.files?n=this.e6wDocument.relateddata.files:this.e6wDocument.files&&(n=this.e6wDocument.files);this.e6wDocument&&null==(this.e6wDocument.title||this.e6wDocument.name)&&this.e6wDocument.dataelements&&(this.e6wDocument.dataelements.title||this.e6wDocument.dataelements.name),1==n.length&&this.openDocument(this.e6wDocument.id)},renderEditor:function(e,t,n,i){this.finishLoading();var o=this;a.init({title:e,ext:t,url:n,docKeyToUse:i,readOnly:!1,rootElem:o.elements.viewer,platformId:o.options.platformUrls.platformId}).catch(e=>{o.showNotification("error","",e.message,"",!1,!1)})},buildSkeleton:function(){this.elements.container=e.createElement("div",{class:this.getClassNames()}),this.startLoading(),this.elements.content=e.createElement("div",{class:this.getClassNames("-content")}),this.elements.content.inject(this.elements.container),new n(this),this.topBar.fillData(),this.elements.detail=e.createElement("div",{class:this.getClassNames("-detail")}),this.elements.detail.inject(this.elements.content),this.elements.viewer=e.createElement("div",{class:this.getClassNames("-viewer")}),this.elements.viewer.inject(this.elements.detail),this.elements.container.addEvent("resize",this.resize.bind(this))},startLoading:function(e){i.mask(this.elements.container,e)},finishLoading:function(){i.unmask(this.elements.container)},resize:function(e,t,n){var i=this.elements.container.getSize();this.topBar.resize(i),i.width<550?this.elements.info.isHidden()||this.elements.viewer.setStyles({opacity:0}):this.elements.viewer.setStyles({opacity:1})},saveOfficeEditorFile:function(){var e=this;return new l(function(t){o.getDocuments([e.options.document.id],{tenant:e.options.platformUrls.platformId,securityContext:e.options.securityContext,tenantUrl:e.options.platformUrls["3DSpace"],onComplete:function(n){if(n.success){if(!Array.isArray(n.data))return void console.error("no data received in DocumentManagement.getDocuments");if(e.e6wDocument=n.data[0],0==e.e6wDocument.relateddata.files.length)e.showNotification("error","",d.get("objectcontainsnofile"),"",!1,!1);else if(1==e.e6wDocument.relateddata.files.length){c.IsWebEditionDocumentSessionActive(e.options.platformUrls.platformId,e.options.document.id).then(i=>{i.is_modified?e.EditorSave(e.options,e.e6wDocument,n.csrf,e.options.nodeModel).then(()=>{t()}):e.showNotification("info","",u.get("FileContentIsNotChanged"),!1,!1)})}else e.showNotification("error","",d.get("Nomultifilesupport"),"",!1,!1)}else console.error("Issue in getDocuments() in saveOfficeEditorFile")}})})},getHeaderInfo:function(e){return{Accept:"application/json","Content-Type":"application/json",SecurityContext:e.securityContext}},showNotification:function(e,t,n,i,o,s){this.events.publish({event:"docv2-show-notification",data:{level:e,title:t,subtitle:n,message:i,sticky:o,allowUnsafeHTML:s}})},EditorSave:function(e,t,n,i){var o=this;return new l(function(s,c){o.startLoading(u.get("SaveInProgress")),t.relateddata&&t.relateddata.files&&1==t.relateddata.files.length&&(o.isFileLocked=""!=t.relateddata.files[0].dataelements.locker?"true":"false");var l=e.platformUrls["3DSpace"]+"/resources/v1/modeler/documents/files/CheckinTicket";l+="?tenant="+e.platformUrls.platformId;var d={method:"PUT",headers:o.getHeaderInfo(e),type:"json",onComplete:function(l){var d=l;a.save({callbackUrl:d.data[0].dataelements.ticketURL,platformId:e.platformUrls.platformId,params:{"file-name":t.relateddata.files[0].dataelements.title,"file-title":t.relateddata.files[0].dataelements.title,"file-description":t.relateddata.files[0].dataelements.title,__fcs__jobTicket:d.data[0].dataelements.ticket},headers:{},filenameForUpload:"file_0"}).catch(e=>{c(),o.finishLoading(),o.showNotification("error",u.get("WebEditionSaveFailed"),e.message,"",!1,!1)}).then(a=>{if(a&&200==a.endpoint_status_code){var l={data:JSON.stringify({csrf:n,data:[{id:t.id,dataelements:{title:t.relateddata.files[0].dataelements.title},relateddata:{files:[{dataelements:{keepLocked:o.isFileLocked,title:t.relateddata.files[0].dataelements.title,receipt:a.endpoint_body},updateAction:"REVISE"}]},updateAction:"NONE"}]}),headers:o.getHeaderInfo(e),onComplete:function(n){s(),o.finishLoading(),t=n.data[0],o.showNotification("success","",u.get("OfficeWebEditorSuccessfulSave"),"",!1,!1),e.modelEvents.publish({event:"docv2-update-complete",data:{nodeModel:i}})},onFailure:function(e,t){c(),o.finishLoading();let n=t.message?t.message:t.error;o.showNotification("error","",n,"",!1,!1)},method:"PUT",type:"json",url:e.platformUrls["3DSpace"]+"/resources/v1/modeler/documents?initiatedFromODE=true"};r.authenticatedRequest(l.url,l)}else c(),console.error("savePromise response is not successful")})},onFailure:function(e,t){c(),o.showNotification("error","",t.message,"",!1,!1)},data:JSON.stringify({csrf:n})};r.authenticatedRequest(l,d)})},openDocument:function(t){var n=this.options,i=this,o={};i.addInterface=!0,c.IsWebEditionDocumentSessionActive(n.platformUrls.platformId,t).then(s=>{s&&s.opened&&(i.addInterface=!1),r.authenticatedRequest(n.platformUrls["3DSpace"]+"/resources/v1/application/E6WFoundation/CSRF",e.extend(o,{method:"GET",type:"json",onComplete:function(e){var o=n.platformUrls["3DSpace"]+"/resources/v1/modeler/documents/DownloadTicket?fileStream=true&addInterface="+i.addInterface,s={tenant:n.platformUrls.platformId,securityContext:n.securityContext,onComplete:function(e){if(e){let t=e.data[0].dataelements.fileName,n=t.lastIndexOf("."),o=t.substring(n+1,t.length);i.renderEditor(t,o,e.data[0].dataelements.ticketURL,e.data[0].id)}},onFailure:function(e,t){i.showNotification("error","",t.message,"",!1,!1)},url:o,type:"json",data:JSON.stringify({csrf:e.csrf,data:[{id:t}]}),headers:i.getHeaderInfo(n),method:"PUT"};r.authenticatedRequest(s.url,s)}}))}).catch(e=>{console.error("Error in state promise")})}})});