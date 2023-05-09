define("DS/VCXMediaChooser/VCXLoadServices",["UWA/Class"],function(t){"use strict";var e=t.extend({init:function(t){e.prototype._onOpenFile=null,this._inputId="VCXMediaChooser-fileinput",document.getElementById(this._inputId)||this.createInputFile(),this._acceptAttributes="image/*",t&&t.acceptAttributes&&(this._acceptAttributes=t.acceptAttributes)},createInputFile:function(){var t=document.getElementsByClassName(this._inputId+"-class");if(t)for(var e=0;e<t.length;e++)t[e].parentElement.remove();new UWA.Element("div",{html:[{tag:"input",id:this._inputId,class:this._inputId+"-class",type:"file",styles:{visibility:"hidden",position:"absolute",left:"0px",top:"0px"},events:{change:this.openFile.bind(this)}}]}).inject(document.body)},openFileSelector:function(t){var i=document.getElementById(this._inputId);i.setAttribute("accept",this._acceptAttributes),e.prototype._onOpenFile=t,i.value=null,i.click()},openFile:function(t){if(t){var i=t.target.files;if(i&&i.length)for(var n=0;n<i.length;n++){var r=i[n];if(r.type.length){var a=window.URL.createObjectURL(r);e.prototype._onOpenFile(a,r)}}else e.prototype._onOpenFile(null)}}});return e}),define("DS/VCXMediaChooser/VCXCanvasServices",["UWA/Class"],function(t){"use strict";var e=UWA.Class.extend({loadImage:function(t,e,i){var n=new Image;n.onload=function(){e&&e(n)},n.onerror=function(e){console.error("Failed to load image "+t),i&&i(e)},n.crossOrigin=!0,n.src=t},getOptimizedDataUrlFromUrl:function(t,e,i,n,r){this.loadImage(t,function(t){var a=this.getOptimizedDataUrlFromImg(t,e,i,n);r&&r(a)}.bind(this))},getOptimizedDataUrlFromImg:function(t,e,i,n){var r=e||2e3,a=i||1500,o=this._getNewDimensions(t,r,a),s=document.createElement("canvas");s.width=o.width,s.height=o.height;var l=s.getContext("2d");l.fillStyle="rgba(255, 255, 255, 0)",l.fillRect(0,0,o.width,o.height),l.drawImage(t,0,0,o.width,o.height);var c=s.toDataURL(n||"image/jpeg"),h=c.split(",")[0].split(":")[1].split(";")[0];return s=null,l=null,{dataURL:c,type:h}},getDataUrlFromImg:function(t,e){var i,n=document.createElement("canvas");n.height=t.height,n.width=t.width;var r=n.getContext("2d");return r.drawImage(t,0,0,t.width,t.height),i=n.toDataURL(e||"image/jpeg"),n=null,r=null,i},getDataUrlFromImgArray:function(t,e,i,n){var r,a=document.createElement("canvas");a.height=i,a.width=e;var o=a.getContext("2d"),s=o.createImageData(e,i);if(t.length==e*i*4)for(var l=0;l<t.length;l++)s.data[l]=t[l];else if(t.length==e*i*3){var c=e*i*4,h=0;for(l=0;l<c;l+=4)s.data[l]=t[h],s.data[l+1]=t[h+1],s.data[l+2]=t[h+2],s.data[l+3]=255,h+=3}return o.putImageData(s,0,0),r=a.toDataURL(n||"image/jpeg"),a=null,o=null,r},getImgDataFromUrl:function(t,e){t?this.loadImage(t,function(t){var i=document.createElement("canvas");i.height=t.height,i.width=t.width;var n=i.getContext("2d");n.drawImage(t,0,0,t.width,t.height);var r=n.getImageData(0,0,t.width,t.height);i=null,n=null,e(r)}):e(null)},getImgDataFromImg:function(t){var e=document.createElement("canvas");e.height=t.height,e.width=t.width;var i=e.getContext("2d");i.drawImage(t,0,0,t.width,t.height);var n=i.getImageData(0,0,t.width,t.height);return e=null,i=null,n},getBlobUrlFromImg:function(t,e,i,n){this.getDataUrlFromImgSrc(t,e,function(t,n){var r=this.getBlobUrlFromDataUrl(t,e);i(r,n)}.bind(this),n)},getBlobUrlFromCanvas:function(t,e){if(!t)return null;var i=e||"image/jpg",n=t.toDataURL(i);return this.getBlobUrlFromDataUrl(n,i)},getBlobUrlFromDataUrl:function(t,e){if(!t)return"";var i=this._fromBase64ToBlob(t,e);return this._fromBlobToImage(i)},getBlobFromDataUrl:function(t,e){return t?this._fromBase64ToBlob(t,e):""},getDataUrlFromImgSrc:function(t,e,i,n){this.loadImage(t,function(t){var n=this.getDataUrlFromImg(t,e);i(n,{width:t.width,height:t.height})}.bind(this),n)},_fromBase64ToBlob:function(t,e){return this._b64toBlob(t.split(",")[1],e)},_fromBlobToImage:function(t){return URL.createObjectURL(t)},_b64toBlob:function(t,e){for(var i=e||"image/jpeg",n=atob(t),r=n.length,a=new Uint8Array(r),o=0;o<r;o++)a[o]=n.charCodeAt(o);return new Blob([a],{type:i})},_getNewDimensions:function(t,e,i){var n=t.width,r=t.height;return n>r?n>e&&(r*=e/n,n=e):r>i&&(n*=i/r,r=i),{width:n,height:r}}});return UWA.namespace("DS/VCXMediaChooser/VCXCanvasServices",e)}),define("DS/VCXMediaChooser/VCXImageGallery",["UWA/Core","UWA/Controls/Abstract","UWA/Json","WebappsUtils/WebappsUtils","text!DS/VCXMediaChooser/assets/gallerySettings.json","i18n!DS/VCXMediaChooser/assets/nls/VCXImageGallery"],function(t,e,i,n,r,a){"use strict";var o="variable";return e.singleton({init:function(){this._currentSection={},this._maxLength=8,this._settingsURL=null,this._initialPath="",this._currentPath=""},initProperties:function(){var e=JSON.parse(r),a=i.path(e,"$");a[0].sections.map(function(t){return t.thumbnails.forEach(function(e){e.sectionName=t.name,e.url=e.url?n.getWebappsAssetUrl("VCXMediaChooser",e.url):e.url}),t}),this._initialPath+=t.Json.encode(a),this._currentPath+=this._initialPath},getIDs:function(t){var e=i.decode(this._currentPath);return i.path(e[0],'$.sections[?(@.name=="'+t+'")].thumbnails..id')},getThumbnails:function(t){var e=i.decode(this._currentPath);return i.path(e[0],'$.sections[?(@.name=="'+t+'")].thumbnails..url')},addThumbnail:function(t,e){var n=i.decode(this._currentPath);if(this._currentSection=i.path(n[0],'$.sections[?(@.name=="'+t+'")]')[0],this._currentSection&&this._currentSection.type===o){var r=this._genId();return this._currentSection.thumbnails||(this._currentSection.thumbnails=[]),this._currentSection.thumbnails.unshift({id:r,url:e}),this._currentSection.thumbnails.length>this._maxLength&&this._currentSection.thumbnails.pop(),this._currentPath=i.encode(n),r}return!1},getThumbnailPropertiesByID:function(t){var e=i.decode(this._currentPath);return i.path(e[0],'$..properties[?(@.id=="'+t+'")]')[0]},getSectionTitle:function(t){return a.get(t)},isSectionSelected:function(t){var e=i.decode(this._currentPath);return i.path(e[0],'$.sections[?(@.name=="'+t+'")].selected')[0]},setSectionVisible:function(t,e){var n=i.decode(this._currentPath);this._currentSection=i.path(n[0],'$.sections[?(@.name=="'+t+'")]')[0],this._currentSection&&(this._currentSection.visible=e,this._currentPath=i.encode(n))},countVisibleSections:function(){if(""!==this._currentPath){var t=i.decode(this._currentPath);return i.path(t[0],"$.sections[?(@.visible==true)]").length}return 0},isSectionVisible:function(t){var e=i.decode(this._currentPath);return i.path(e[0],'$.sections[?(@.name=="'+t+'")].visible')[0]},getOnClickCB:function(t){var e=i.decode(this._currentPath);return i.path(e[0],'$.sections[?(@.name=="'+t+'")].onClick')[0]},getSectionsNames:function(){var t=i.decode(this._currentPath);return i.path(t[0],"$.sections..name")},reset:function(){this._currentPath="",this._currentPath+=this._initialPath},_genId:function(){var t=Date.now();return t+=Math.floor(10*Math.random())}})}),define("DS/VCXMediaChooser/VCXChoosePicture",["UWA/Class","UWA/Controls/Scroller","DS/UIKIT/Accordion","DS/UIKIT/Modal","DS/UIKIT/SuperModal","DS/VCXMediaChooser/VCXLoadServices","DS/VCXMediaChooser/VCXCanvasServices","DS/VCXMediaChooser/VCXImageGallery","DS/i3DXCompassServices/i3DXCompassPubSub","DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices","WebappsUtils/WebappsUtils","DS/WAFData/WAFData","i18n!DS/VCXMediaChooser/assets/nls/VCXChoosePicture","css!DS/VCXMediaChooser/VCXMediaChooser"],function(t,e,i,n,r,a,o,s,l,c,h,u,d){"use strict";var m=UWA.Class.extend({init:function(){this._onOpenFile=null,this._url=null,this._loadServices=new a,this._canvasServices=new o,this._drivePanelAnchor="sidepanel:X3PDRIV_AP",this._maxWidth=2e3,this._maxHeight=1500,this._widget=null},get3DDriveURL:function(){if(!this._widget.getValue("x3dPlatformId"))return UWA.Promise.resolve(null);var t=UWA.Promise.deferred();return c.getServiceUrl({serviceName:"3DDrive",platformId:this._widget.getValue("x3dPlatformId"),onComplete:function(e){this._cacheUrl3DDrive;Array.isArray(e)&&e.length?t.resolve(e[0].url):e.constructor===String&&t.resolve(e)},onFailure:function(e){t.reject(e)}}),t.promise},setWidget:function(t){this._widget=t},setODTMode:function(t){this._ODTMode=t},open:function(t,e){this._onOpenFile=t,this._onHideCB=e;var i=new r({className:"choose-picture-modal"});i.onHide=this.onHide.bind(this,i),this.get3DDriveURL().then(t=>{this._cacheUrl3DDrive=t,this._chooseProviderContent(i)})},dropImage:function(t,e){this._onOpenFile=e,this._canvasServices.loadImage(t,t=>{let e=this._computeImageUrl(t,null);this._url=e&&e.blobUrl,this._onOpenFile(this._url)})},onHide:function(t){this._ignoreHide||(this._onHideCB(),this._url=null),t.destroy(),l.unsubscribe("setX3DContent")},_chooseProviderContent:function(t){var e=[];if(this._ODTMode){var i=this._createProviderObject(d.get("odtBtnLabel"),this._onODTClick.bind(this,t),"fonticon-cog",null);e.push(i);new UWA.Element("input",{type:"hidden",id:"x3DPhoto-ODT-imageUrl"}).inject(document.body)}var n=this._createProviderObject(d.get("deviceBtnLabel"),this._onComputerClick.bind(this,t),"fonticon-install",null);if(e.push(n),this._cacheUrl3DDrive){var r=this._getDrivePanelUrl(),a=this._createProviderObject(d.get("driveBtnLabel"),this._on3DDriveClick.bind(this,t),"fonticon-component",r);e.push(a)}var o=null;s.countVisibleSections()>1?o=this._createAccordionContainer("divided",t):1==s.countVisibleSections()&&(o=this._createSectionContainer(t)),o&&e.push(o);var l=new UWA.Element("div",{html:e});t.dialog({body:l,title:d.get("title"),buttons:[{value:d.get("cancelBtnLabel"),action:t=>{t.hide()}}]});var c=document.getElementById("toScroll");c&&this._addToScroller(c)},_onComputerClick:function(t){this._loadServices.openFileSelector((e,i)=>{this._loadLocalImage(t,e,i)})},_onODTClick:function(t){var e=document.getElementById("x3DPhoto-ODT-imageUrl");this._loadLocalImage(t,e.value)},_loadLocalImage:function(t,e,i){this._canvasServices.loadImage(e,e=>{let n=this._computeImageUrl(e,i&&i.type);if(this._url=n&&n.blobUrl,this._fileName=i&&i.name||"",i&&this._fileName&&i.type!==n.type){let t=n.type.split("/").pop();this._fileName+="."+t}this._onOpenFile(this._url,this._fileName),t.next()})},_on3DDriveClick:function(t){var e=this._selectPictureContent(t);this._ignoreHide=!0,t.next(),this._ignoreHide=!1,l.subscribe("setX3DContent",this._on3DXContentSelection.bind(this,e))},_onRecentContentClick:function(t){this._canvasServices.getBlobUrlFromImg(this._url,this._type,e=>{this._url=e,this._onOpenFile(this._url),t.next()})},_onRecentTextureClick:function(t){this._canvasServices.getBlobUrlFromImg(this._url,this._type,e=>{this._url=e,this._onOpenFile(this._url),t.next()})},_onSampleClick:function(t,e){if(this._url=s.getThumbnailPropertiesByID(e),!this._url.type)return this._onOpenFile(this._url),void t.next();if("side"==this._url.type){for(var i in this._url.set){var n=this._url.set[i];n&&"string"==typeof n&&(this._url.set[i]=n?n.match("http")?n:h.getWebappsAssetUrl("VCXMediaChooser",n):null)}this._canvasServices.getBlobUrlFromImg(this._url.set.front,this._type,e=>{this._url.set.front=e,this._canvasServices.getBlobUrlFromImg(this._url.set.left,this._type,e=>{this._url.set.left=e,this._canvasServices.getBlobUrlFromImg(this._url.set.top,this._type,e=>{this._url.set.top=e,this._onOpenFile(this._url),t.next()})})})}else"scene"==this._url.type?(this._url.background=h.getWebappsAssetUrl("VCXMediaChooser",this._url.background),this._canvasServices.getBlobUrlFromImg(this._url.background,this._type,e=>{this._onOpenFile(e),t.next()})):(this._onOpenFile(this._url),t.next())},_selectPictureContent:function(t){var e=h.getWebappsAssetUrl("VCXMediaChooser","graphics/G_NXGAddImageWhite.png"),i=new UWA.Element("div",{html:[{tag:"div",class:"thumbnail",styles:{backgroundImage:"url("+e+")"},html:{tag:"img",src:e,class:"hide"}}]});return t.dialog({body:i,title:d.get("title"),buttons:[{className:"primary",value:d.get("validateBtnLabel"),action:t=>{this._url&&(this._onOpenFile(this._url),t.hide())}},{value:d.get("cancelBtnLabel"),action:t=>{t.hide()}}]}),i.getElementsByTagName("img")[0]},_on3DXContentSelection:function(t,e){if(e&&null!=e.data.items[0]){var i=this._cacheUrl3DDrive+"/resources/3ddrive/v1/bos/"+e.data.items[0].objectId+"/download";u.authenticatedRequest(i,{method:"GET",type:"json",onComplete:e=>{this._canvasServices.loadImage(e.url,e=>{let i=this._computeImageUrl(e,"image/png");this._url=i&&i.blobUrl,t.parentNode.setStyle("backgroundImage","url("+this._url+")")})},onFailure:t=>{console.error(t)}})}},_createProviderObject:function(t,e,i,n){return{tag:"a",href:n||"javascript:void(0)",target:"_parent",html:{tag:"div",class:"provider",html:[{tag:"span",class:"fonticon "+i+" fonticon-2x"},{tag:"span",class:"name",text:t}],events:{click:e}}}},_createPictureContainer:function(t,e,i){var n=new UWA.Element("div",{class:"section-thumbnail",events:{click:()=>{this._url=t,e?i(e):i()}}});return this._canvasServices.loadImage(t,t=>{t.naturalWidth>t.naturalHeight?n.addClassName("landscape"):n.addClassName("portrait"),n.appendChild(t)}),n},_createSectionContent:function(t,e,i){if(n=null,t&&t.length>0){var n=new UWA.Element("div",{class:"section-thumbnail-container",id:"toScroll"});t.forEach((t,r)=>{this._createPictureContainer(t,e?e[r]:null,i).inject(n,"bottom")}),new UWA.Element("div",{class:"section-padding-trick"}).inject(n)}return n},_createAccordionContainer:function(t,e){var n=new UWA.Element("div",{class:"section-container"}),r=new i({className:t||"",exclusive:!1,items:[]});return s.getSectionsNames().forEach(t=>{s.isSectionVisible(t)&&(r.addItem({title:s.getSectionTitle(t),name:t,content:this._createSectionContent(s.getThumbnails(t),s.getIDs(t),this[s.getOnClickCB(t)].bind(this,e)),selected:s.isSectionSelected(t)}),r.inject(n))}),n},_createSectionContainer:function(t){var e=new UWA.Element("div",{class:"section-container"});return s.getSectionsNames().forEach(i=>{if(s.isSectionVisible(i)){var n=this._createSectionContent(s.getThumbnails(i),s.getIDs(i),this[s.getOnClickCB(i)].bind(this,t));if(n)new UWA.Element("div",{class:"section-title",text:s.getSectionTitle(i)}).inject(e),n.inject(e)}}),e},_addToScroller:function(t){new e(t,{scrollbarV:!1,scrollableY:!1})},_getDrivePanelUrl:function(){return parent.document.location.hash="","/#"+this._drivePanelAnchor},_computeImageUrl:function(t,e){var i=this._widget?this._widget.getValue("ImageOptimization"):"1";if("-1"!==i||!(t.width>this._maxWidth||t.height>this._maxHeight))return this._optimizeImage(t,i,e);this._openImageOtimizationModal(i=>this._optimizeImage(t,i,e))},_openImageOtimizationModal:function(t){var e=new n({className:"image-optimization",overlay:!0,closable:!1,header:"<h4>"+d.get("optimTitle")+"</h4>",body:"<p>"+d.get("optimMessage1")+"</p><p>"+d.get("optimMessage2")+'</p><br /><p><font size="2">'+d.get("optimTip")+"</font></p>",footer:'<button type="button" class="btn btn-primary" value="1">'+d.get("optimYesBtnLabel")+'</button> <button type="button" class="btn btn-default" value="0">'+d.get("optimNoBtnLabel")+"</button>"}).inject(document.body);e.onHide=this._onImageOptimizationHide.bind(this,e),e.getContent().getElements(".btn").forEach(i=>{i.addEvent("click",()=>{e.hide(),this._widget&&this._widget.setValue("ImageOptimization",i.value),t(i.value)})}),e.show()},_optimizeImage:function(t,e,i){var n=t.width,r=t.height;"1"===e?(n=this._maxWidth,r=this._maxHeight):"0"===e&&(n=4e3,r=4e3);var a=this._canvasServices.getOptimizedDataUrlFromImg(t,n,r,i);return a.blobUrl=a&&this._canvasServices.getBlobUrlFromDataUrl(a.dataURL,a.type),a},_onImageOptimizationHide:function(t){t.destroy()}});return UWA.namespace("DS/VCXMediaChooser/VCXChoosePicture",m)});