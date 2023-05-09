define("DS/QuicklinksWdg/Models/LinkModel",["UWA/Class/Model"],function(e){"use strict";return e.extend({iconProvider:"https://www.google.com/s2/favicons?domain=",iconProviderFallback:"https://icons.better-idea.org/icon",sync:function(e,t,i){return i&&i.onComplete&&i.onComplete(!0),{cancel:function(){}}},defaults:function(){return{link:"",tag:"",icon:"",isReadOnly:!1}},setup:function(){this.set("icon",this.iconProvider+this.get("link")),this.set("iconFallback",this.iconProviderFallback+"?url="+this.get("link")+" &size=12")}})}),define("DS/QuicklinksWdg/Views/IframeView",["UWA/Core","UWA/Class/View","css!DS/QuicklinksWdg/Views/IframeView"],function(e,t){"use strict";return t.extend({tagName:"div",className:"link-detail",render:function(){this.container.setContent(e.createElement("iframe",{class:"link-frame",src:this.model.get("link"),sandbox:"allow-same-origin allow-forms allow-scripts allow-popups"}))}})}),define("DS/QuicklinksWdg/Views/AbstractItemView",["UWA/Core","DS/W3DXComponents/Views/Temp/TempItemView","i18n!DS/QuicklinksWdg/assets/nls/Quicklinks"],function(e,t,i){"use strict";return t.extend({HTML_TAG_REGEXP:/</i,throttle:function(e,t){var i,n,s,l,a=0;function o(){a=new Date,s=null,l=e.apply(i,n)}return function(){var r=new Date,d=t-(r-a);return i=this,n=arguments,d<=0?(clearTimeout(s),s=null,a=r,l=e.apply(i,n)):s||(s=setTimeout(o,d)),l}},addAlert:function(t){var n=t.elements,s=n.tagUrlWarning,l=n.tagInput.elements.input.getParent().getParent();s?s.show():n.tagUrlWarning=e.createElement("div",{class:"alert-warning alert-message",content:i.renameLinkNameNoHTML}).inject(l.getParent(),"before")}})}),define("DS/QuicklinksWdg/Views/LinkItemView",["UWA/Core","UWA/Event","UWA/String","DS/QuicklinksWdg/Views/AbstractItemView","DS/UIKIT/Input/Text","DS/UIKIT/Tooltip","i18n!DS/QuicklinksWdg/assets/nls/Quicklinks","css!DS/QuicklinksWdg/Views/LinkItemView"],function(e,t,i,n,s,l,a){"use strict";return n.extend({tagName:"div",className:"link-itm link-itm-regular",isEditing:!1,isDataValid:!0,inputTagClass:"tag-el tag-el-input",inputUrlClass:"url-el url-el-input",elements:{},template:'<span class="link-el link-selectable link-itm-img"><img draggable="false" class="link-itm-icon" src="{{icon}}"></img></span><span class="link-el link-selectable link-itm-tag"><div class="tag-el link-tag-text" tabindex="-1">{{#if tag}} {{tag}} {{else}} {{link}} {{/if}}</div></span><span class="link-el link-selectable link-itm-url"><a class="link-url-text" rel="noopener" href="{{link}}" target="_blank" tabindex="-1">{{link}}</a></span>{{#unless isReadOnly}}<span class="link-el link-itm-actions view-mode-actions"><span class="fonticon fonticon-pencil link-itm-edit icon clickable"></span></span><span class="link-el link-itm-actions edit-mode-actions"><span class="fonticon fonticon-floppy link-itm-save icon clickable"></span><span class="fonticon fonticon-trash link-itm-delete icon clickable"></span></span>{{/unless}}',render:function(){var i,n,o,r=e.extendElement,d=this.elements,c=this.container,u=this.options||{},g=this.model.get("link")||"",h=this._onInputChange.bind(this);return this._parent.apply(this,arguments),c.setAttribute("title",g),d.tag=r(c.querySelector(".link-tag-text")),n=d.tagInput=new s({className:this.inputTagClass,placeholder:this.model.get("tag")||"",events:{onKeyDown:this.throttle(h,500)}}).inject(c.querySelector(".link-itm-tag")),d.url=i=r(c.querySelector(".link-itm-url a")),i.addEvent("click",function(e){t.preventDefault(e)}),o=d.urlInput=new s({className:this.inputUrlClass,placeholder:g,events:{onKeyDown:this.throttle(h,500)}}).inject(c.querySelector(".link-itm-url")),n.elements.input.addEvent("focus",h),n.elements.input.addEvent("blur",h),o.elements.input.addEvent("focus",h),o.elements.input.addEvent("blur",h),u.isReadOnly||(d.editBtn=r(c.querySelector(".link-itm-edit")),d.saveBtn=r(c.querySelector(".link-itm-save")),d.trashBtn=r(c.querySelector(".link-itm-delete")),d.editBtnTooltip=new l({position:"bottom",target:d.editBtn,body:a.editBtnTooltip,offset:{x:0,y:0}}),d.saveBtnTooltip=new l({position:"bottom",target:d.saveBtn,body:a.saveBtnTooltip,offset:{x:0,y:0}}),d.deleteBtnTooltip=new l({position:"bottom",target:d.trashBtn,body:a.deleteBtnTooltip,offset:{x:0,y:0}})),d.icon=r(c.querySelector(".link-itm-icon")),c.querySelector(".link-itm-url"),this._testIconLoad(d.icon),this},domEvents:{"click .link-itm-edit":"onEdit","click .link-itm-save":"onEditEnd","click .link-itm-delete":"onDelete","click .link-selectable":"onItemClick"},_testIconLoad:function(t){if(t){var i,n=this.model;(i=e.createElement("img",{events:{load:s,error:function(){i.getAttribute("src").indexOf(n.iconProvider)>=0?(i.setAttribute("src",n.get("iconFallback")),t.setAttribute("src",n.get("iconFallback"))):(t.hide(),s())}}})).setAttribute("src",n.get("icon"))}function s(){t.setAttribute("src",n.get("icon")),i.destroy()}},onEdit:function(){var e=this.elements;this.isEditing=!0,e.tagInput.setValue((e.tag.getText()||"").trim()),e.urlInput.setValue((e.url.getText()||"").trim()),this.container.addClassName("edit").removeAttribute("title"),this._onInputChange()},onEditEnd:function(){if(this.isEditing&&this.isDataValid){var e,t,i,n,s=this.options._widgetHelper,l=this.container;e=this.elements,i=this.model,(n=s.formatUrl(e.urlInput.getValue()))&&(t=e.tagInput.getValue().trim(),e.url.setText(n),e.tag.setText(t||n),e.icon.src=i.iconProvider+void 0,i.set({link:n,tag:t,icon:i.iconProvider+n}),l.setAttribute("title",n),this.isEditing=!1,l.removeClassName("edit"),this._testIconLoad(e.icon))}},onCancelEdit:function(){this.isEditing=!1,this.container.removeClassName("edit")},onDelete:function(){this.isEditing&&(this.model.destroy(),this.destroy(),this.options.linkCollectionView.onDeleteItem())},select:function(){},unSelect:function(){},onOpen:function(e){var n=this.options._widgetHelper,s=this.model.get("link");try{window.open(s,"_blank")}catch(e){n.showAlert({message:n.replace(a.unableOpenInvalidUrl,{url:i.stripTags(s)})})}t.preventDefault.call(null,e)},onItemClick:function(e){this.isEditing||(this.dispatchEvent("onClick",[this]),this.onOpen(e))},destroy:function(){this.stopListening(),this._parent.apply(this,arguments)},_onInputChange:function(e){var i=this,n=t.whichKey(e||{});i.isEditing&&setTimeout(function(){var e,t=i.container,s=i.elements,l=s.saveBtn,a=s.urlInput,o=s.tagInput,r=a.elements.container,d=o.elements.container,c=a.getValue().trim(),u=o.getValue().trim(),g=!0;if((e=s.tagUrlWarning)&&e.hide(),t.removeClassName("error"),l.removeClassName("clickable"),r.removeClassName("input-error"),d.removeClassName("input-error"),"esc"!==n){if(c||u){var h=i.HTML_TAG_REGEXP.test(c),m=i.HTML_TAG_REGEXP.test(u);(h||m)&&(i.addAlert(i),t.addClassName("error"),h&&r.addClassName("input-error"),m&&d.addClassName("input-error"),g=!1)}c||(t.addClassName("error"),r.addClassName("input-error"),g=!1),g&&l.addClassName("clickable"),i.isDataValid=g,g&&"return"===n&&i.onEditEnd()}else i.onCancelEdit()},0)}})}),define("DS/QuicklinksWdg/Views/NewLinkItemView",["UWA/Event","DS/QuicklinksWdg/Views/AbstractItemView","DS/UIKIT/Input/Text","i18n!DS/QuicklinksWdg/assets/nls/Quicklinks"],function(e,t,i,n){"use strict";return t.extend({tagName:"div",className:"newlink link-itm",inputs:{},isEditing:!0,isDataValid:!1,inputTagClass:"tag-el tag-el-input",inputUrlClass:"url-el url-el-input",template:'<span class="link-el link-itm-tag"></span><span class="link-el link-itm-url"></span><span class="link-el link-itm-btn"><button class="btn btn-primary" type="submit" disabled="disabled">'+n.add+"</button></span>",domEvents:{"click .link-itm-btn .btn":"onAdd"},render:function(){var e,t,s=this.elements,l=this.container,a=this._onInputChange.bind(this);return this._parent.apply(this,arguments),e=s.tagInput=new i({className:this.inputTagClass,placeholder:n.enterTag,events:{onKeyDown:this.throttle(a,500)}}).inject(l.querySelector(".link-itm-tag")),t=s.urlInput=new i({className:this.inputUrlClass,placeholder:n.enterUrl,events:{onKeyDown:this.throttle(a,500)}}).inject(l.querySelector(".link-itm-url")),e.elements.input.addEvent("focus",a),e.elements.input.addEvent("blur",a),t.elements.input.addEvent("focus",a),t.elements.input.addEvent("blur",a),s.addBtn=l.getElement(".link-itm-btn .btn"),this.resetState(),this},onAdd:function(){this.elements.urlInput.getValue().trim()&&this.onEditEnd()},onEditEnd:function(){if(!this.isDestroyed&&this.isDataValid){var e,t=this.elements,i=t.urlInput,n=t.tagInput;e=this.options._widgetHelper.formatUrl(i.getValue()),this.dispatchEvent("onItemAdd",[{tag:n.getValue(),link:e},this]),this.resetState(),n.focus(),this.disableAddBtn()}},resetState:function(){var e=this.elements,t=e.urlInput,i=e.tagInput;this.isDestroyed||(t.setValue(""),i.setValue(""))},enableAddBtn:function(){this.elements.addBtn.removeAttribute("disabled")},disableAddBtn:function(){this.elements.addBtn.setAttribute("disabled","disabled")},destroy:function(){this.stopListening(),this._parent.apply(this,arguments)},_onInputChange:function(t){var i=this,n=e.whichKey(t||{});i.isEditing&&setTimeout(function(){var e,t=i.container,s=i.elements,l=s.urlInput,a=s.tagInput,o=l.elements.container,r=a.elements.container,d=a.getValue().trim(),c=l.getValue().trim(),u=!0;if((e=s.tagUrlWarning)&&e.hide(),o.removeClassName("input-error"),r.removeClassName("input-error"),c||d){var g=i.HTML_TAG_REGEXP.test(c),h=i.HTML_TAG_REGEXP.test(d);(g||h)&&(i.addAlert(i),t.addClassName("error"),g&&o.addClassName("input-error"),h&&r.addClassName("input-error"),u=!1)}c||(d&&o.addClassName("input-error"),i.disableAddBtn(),u=!1),u&&i.enableAddBtn(),i.isDataValid=u,u&&"return"===n&&i.onEditEnd()},0)}})}),define("DS/QuicklinksWdg/Collections/LinkCollection",["UWA/Core","UWA/Json","UWA/Class/Collection","DS/QuicklinksWdg/Models/LinkModel"],function(e,t,i,n){"use strict";return i.extend({model:n,_widgetHelper:null,iconProvider:"http://www.google.com/s2/favicons?domain=",setup:function(t,i){var n=e.extend({isReadOnly:!1,_widgetHelper:null},i||{});this.isReadOnly=n.isReadOnly,this._widgetHelper=i._widgetHelper,this.addEvents({onChange:this.updateData,onRemove:this.updateData});var s=JSON.parse(this._widgetHelper.getWidgetValue("links")||"[]").length;this._widgetHelper.setPrevNbLinks(s),this._widgetHelper.setNbLinks(s)},collectData:function(e){var i,n,s,l,a=[],o=this.isReadOnly;if(e&&t.isJson(e))for(n in i=t.decode(e))i.hasOwnProperty(n)&&(l=(s=i[n]).link,a.push({tag:s.title,link:this._widgetHelper.formatUrl(l),isReadOnly:o}));return a},sync:function(e,t,i){return i&&i.onComplete&&i.onComplete(this._widgetHelper.getWidgetValue("links")),{cancel:function(){}}},parse:function(e){return this.collectData(e)},updateData:function(){var e=this.map(function(e){return{link:e.get("link"),title:e.get("tag")}}),i=this._models.length;this._widgetHelper.setPrevNbLinks(i),this._widgetHelper.setWidgetValue("links",t.encode(e)),this._widgetHelper.setNbLinks(e.length),e.length>i&&this.fetch({reset:!0})}})}),define("DS/QuicklinksWdg/Views/LinkCollectionView",["UWA/Core","UWA/Class/View","DS/W3DXComponents/Views/Layout/ListView","DS/W3DXComponents/Views/EmptyView","DS/QuicklinksWdg/Views/LinkItemView","DS/QuicklinksWdg/Views/NewLinkItemView","css!DS/QuicklinksWdg/Views/LinkCollectionView"],function(e,t,i,n,s,l){"use strict";return t.extend({tagName:"div",className:"link-list",noAddClassName:"noadd",elements:{},_widgetHelper:null,setup:function(t){var a=this,o=a.elements,r=t||{};a._widgetHelper=r._widgetHelper,o.listView=new i(e.merge({itemView:s,className:"links-ctn",collection:a.collection,emptyView:n,itemViewOptions:{linkCollectionView:a,isReadOnly:r.isReadOnly,_widgetHelper:a._widgetHelper}},t||{})),a.listenTo(o.listView,{onItemViewClick:a.onItemClick}),a.container.addEvent("click",function i(){e.is(t.bookmarklet)&&t.bookmarklet.destroy(),a.container.removeEvent("click",i)}),o.newLinkView=new l({_widgetHelper:a._widgetHelper}),a.listenTo(o.newLinkView,{onItemAdd:a.onNewItem.bind(a)})},unSelectAll:function(){var e=this.elements.listView;e&&e.unSelectAll()},render:function(){var e,t=this.elements,i=this.options||{},n=this._widgetHelper,s=this.container;return i.isReadOnly||(t.newLinkView.render().inject(s),n.isDisplayAdd()&&(n.isUsableHeight()||0===n.getNbLinks())?this.showAdd():this.hideAdd()),t.listView.render().inject(s),(e=t.listView.scrollView.scroller.getContent())&&e.setStyle("height","auto"),this},onItemClick:function(){Array.prototype.unshift.call(arguments,this.elements.listView),this.dispatchEvent("onItemViewSelect",arguments)},onDeleteItem:function(){var e=this._widgetHelper;e.isDisplayAdd()&&0===e.getNbLinks()&&this.showAdd()},onNewItem:function(e){var t=this.collection,i=this.elements.listView,n=this._widgetHelper;t.add(e),t.updateData(),!n.isUsableHeight()&&this.hideAdd(),i&&i.container.isInjected()&&i.scrollToItemView(t.last().cid,"start")},showAdd:function(){this.container&&this.container.removeClassName(this.noAddClassName)},hideAdd:function(){this.container&&this.container.addClassName(this.noAddClassName)},destroy:function(){this.stopListening(),this._parent.apply(this,arguments)}})}),define("DS/QuicklinksWdg/Quicklinks",["UWA/Core","UWA/Class","UWA/String","DS/UIKIT/Alert","DS/Dashboard/Utils","DS/QuicklinksWdg/Collections/LinkCollection","DS/QuicklinksWdg/Views/LinkCollectionView","DS/Bookmarklet/Notification"],function(e,t,i,n,s,l,a,o){"use strict";var r=!0;return t.extend({init:function(t,i){var n=this;n.options=e.extend({isReadOnly:!1},e.clone(i,!1)||{}),n.widget=t,n._nbLinks=0,n._previousNbLinks=0,n._isReadOnly=n.options.isReadOnly,n._bookmarkletContainer=null,n._widgetHelper={replace:n.replace,formatUrl:s.formatUrl,showAlert:n.showAlert.bind(n),setNbLinks:function(e){n._nbLinks=e},getNbLinks:function(){return n._nbLinks},getWidgetValue:function(){return n.widget.getValue.apply(t,arguments)},setWidgetValue:function(){return n.widget.setValue.apply(t,arguments)},setPrevNbLinks:function(e){n._previousNbLinks=e},updateUsableHeight:function(){var e=n.widget.body.offsetHeight,t=2*n._linksView.elements.newLinkView.container.offsetHeight;n._widgetHelper.setWidgetValue("usableHeight",(e>=t)+"")},isUsableHeight:function(){var e=n._widgetHelper.getWidgetValue("usableHeight");return"true"===e||!0===e},isDisplayAdd:function(){var e=n._widgetHelper.getWidgetValue("displayAdd");return"true"===e||!0===e}},n.initWidgetEvents()},initWidgetEvents:function(){var e=this.widget;e.onDestroy=this.onDestroy,e.addEvents({onLoad:this.onLoad.bind(this),onRefresh:this.onLoad.bind(this),onViewChange:this.onViewChange.bind(this),onResize:this.onResize.bind(this)})},buildSkeleton:function(){var t=this.widget.body,i=this._linksView,n=this._linkCollection,s=this._widgetHelper;t.empty(),i&&(i.destroy(),this._linksView=null),this._bookMarkletContainer&&(this._bookMarkletContainer=null),this._bookMarkletContainer=e.createElement("div",{class:"quicklinks-bookmarklet"}).inject(t,"top"),n=new l(null,{_widgetHelper:s,isReadOnly:this._isReadOnly}),(i=new a({collection:n,bookmarklet:this._bookMarkletContainer,_widgetHelper:s,usePullToRefresh:!1,useInfiniteScroll:!1,isReadOnly:this._isReadOnly,itemViewOptions:{_widgetHelper:s,isReadOnly:this._isReadOnly}})).render().inject(t),this._linksView=i,this._linkCollection=n,n.fetch()},onLoad:function(){var e=this;(e.removeAlert(),e._nbLinks=0,e.buildSkeleton(),e.updateTitle(),e._widgetHelper.updateUsableHeight(),r&&"true"!==(e.widget.getValue("suppressBookmarklet")||!1).toString())&&new o({onDoNotShowAgain:!e.isReadOnly&&function(){e.widget.setValue("suppressBookmarklet",!0)}}).render().inject(e._bookMarkletContainer);r=!1},updateTitle:function(e){var t,i=this.widget,n=i.getValue("widgetTitle");n?(i.setValue("title",n),i.setValue("widgetTitle",""),t=n):e?(i.setValue("title",e),t=e):t=i.getValue("title"),t?i.setTitle(t):this.clearTitle()},clearTitle:function(){var e=this.widget;e.setValue("title",""),e.setTitle("")},showAlert:function(e){e=e||{},this.removeAlert(),this._alert=new n({className:"alert alert-quicklinks",closable:!0,visible:!0,messages:e.message,messageClassName:i.stripTags(e.messageType||"error")}).inject(document.body).show()},removeAlert:function(){this._alert&&this._alert.destroy(),this._alert=null},replace:function(e,t){return(e||"").replace(/\{([\w\-]+)\}/g,function(e,i){return void 0!==t[i]?t[i]:""})},onViewChange:function(e){e=e||{};var t=this.widget.body;t.removeClassName("is-maximize").removeClassName("is-fullscreen"),"maximized"===e.type?t.addClassName("is-maximize"):"fullscreen"===e.type&&t.addClassName("is-fullscreen")},onDestroy:function(){var e=this._linksView;this.removeAlert(),this.widget=null,e&&e.destroy(),this._linkCollection=null},onResize:function(){var e=this._widgetHelper.getWidgetValue("usableHeight");this._widgetHelper.updateUsableHeight(),e!==this._widgetHelper.getWidgetValue("usableHeight")&&this._linksView.render()}})});