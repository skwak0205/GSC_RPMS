define("DS/OfficeDocumentEditorWdg/Widget",["UWA/Core","UWA/Utils","UWA/Event","DS/UIKIT/Input/Button","DS/UIKIT/Alert","DS/OfficeDocumentEditorClient/Utils","DS/WAFData/WAFData","i18n!OfficeDocumentEditorWdg/assets/nls/widget","css!OfficeDocumentEditorWdg/Widget"],function(e,t,i,a,n,r,l,c){"use strict";const o={alertContainer:{},container:{},serviceUrl:"",load:function(){widget.body.empty(),widget.setTitle(widget.getValue("title")),r.getOfficeDocumentEditorServiceUrl().then(e=>{o.serviceUrl=e,o.displayContent()}).catch(e=>{o.notify(c.service_url_not_found)})},notify:function(t,i){let a="error";["warning","error"].indexOf(i)>-1&&(a=i),new n({closable:!0,className:"officedocumenteditor-notify",visible:!0,messages:[{message:e.createElement("div",{html:[e.createElement("span",{text:t})]}),className:a}]}).inject(o.alertContainer)},displayContent:()=>{},getACL:()=>{},displayContentFromUrl:function(t){o.alertContainer=e.createElement("div",{class:"alert-container"}).inject(widget.body);const n=e.createElement("div",{styles:{height:"100%",display:"flex","flex-direction":"column","align-items":"stretch"}}),r=e.createElement("div",{styles:{height:"100%"}}),l=e.createElement("div");new a({className:"input-submit primary",value:c.saveButton,events:{onClick:function(t){i.stop(t);let a={};try{a=JSON.parse(widget.getValue("callbackUrlParams"))}catch(t){}let n={};try{n=JSON.parse(widget.getValue("callbackUrlHeaders"))}catch(t){}Editor.save({callbackUrl:widget.getValue("callbackUrl"),params:a,headers:n,filenameForUpload:widget.getValue("callbackUrlFilenameForUpload")}).then(function(e){e&&e.success?o.notify(c.saveSuccess,"primary"):o.notify(c.saveError)}).catch(function(t){e.log("Error:",t),o.notify(c.saveError)})}}}).inject(l),l.inject(n),r.inject(n),n.inject(widget.body),o.container=n,Editor.init({title:widget.getValue("title"),ext:widget.getValue("ext"),url:widget.getValue("url"),docKeyToUse:widget.getValue("docKeyToUse"),callbackUrl:widget.getValue("callbackUrl"),readOnly:widget.getValue("readOnly"),useIframe:widget.getValue("useIframe"),rootElem:r}).catch(function(e){switch(e){case Editor.ERROR.NO_DOCUMENT_Url_PROVIDED:o.notify(c.noDocumentUrlProvided);break;case Editor.ERROR.SERVICE_NOT_FOUND:o.notify(c.serviceNotFound);break;default:o.notify(c.unknownError)}})}};return o});