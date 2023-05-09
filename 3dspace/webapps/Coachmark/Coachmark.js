define("DS/Coachmark/CoachmarkControl",["UWA/Core","UWA/Event","UWA/Controls/Abstract","UWA/Controls/Overlay","UWA/Controls/Carousel","UWA/Element","UWA/Utils/Client"],function(e,t,n,s,o,a,i){"use strict";var r=i.Features;return n.extend({defaultOptions:{pages:[{html:{tag:"img",src:"default.png"}}],doNotShowAgain:!1,lang:"en",fullscreen:!1,className:"",width:620,height:480},init:function(t,n){this._parent(e.merge(n,this.defaultOptions)),this.buildSkeleton(t),this.options.doNotShowAgain&&!this.options.forceDisplay||this._getFocus()},buildSkeleton:function(n){var i,l,c,h,d,m,p,u,g,f,y,k,v;if(i=this.options,l=i.fullscreen,c=i.pages,50,m=i.height-100,y=this,k=e.extendElement(document),!this.options.doNotShowAgain||this.options.forceDisplay){for(this.overlay=new s({className:"ds-coachmark "+i.className,closeable:!1,events:{onClose:this.dispatchAsEventListener("onEndClose")}}),this.elements.container=this.overlay.elements.container,this.elements.container.setStyles({"max-width":l?"none":i.width,"max-height":l?"none":i.height,width:l?"100%":"calc(100% - 10px)",height:l?"100%":i.height,position:"fixed","text-align":"center",left:l?0:"50%","margin-left":l?0:-i.width/2,padding:"50px 0px 50px 0","box-sizing":"border-box"}),v=function(){var e=y.elements.container.getSize().width,t=l?k.getSize().height:Math.min(k.getSize().height-75-10,i.height);y.elements.container.setStyles({"margin-left":l?0:-e/2+"px",height:t,top:l?0:75+(k.getSize().height-75-10-t)/2}),y.pagesList&&y.pagesList.setStyle("height",t-100),setTimeout(function(){y.scroller.scrollToItem(y.scroller.getItems()[y.index])},50)},k.addEvent("resize",v),this.addEvent("onClose",function(){k.removeEvent("resize",v)}),this.elements.container.setContent({class:"ds-coachmark-close fonticon fonticon-wrong",tag:"span",events:{click:this.dispatchAsEventListener("_onClose")}}),f=function(e){var n=t.getElement(e),s=n.getParent().getChildren().indexOf(n);y.scroller.scrollToItem(y.pagesList.getElement(".ds-coachmark-item-"+s))},h=[],d=[],p=0,u=c.length;p<u;p++)d[p]=e.createElement("li",{class:"ds-coachmark-item ds-coachmark-item-"+p,html:{class:"ds-coachmark-content",tag:"div",html:c[p].html}}),d[p].addContent({tag:"span",class:"ds-coachmark-index-"+p+" ds-coachmark-index"}),d[p].setData("feedback","ds-coachmark-item-"+p+"-feedback"),h[p]={tag:"li",class:"ds-coachmark-item-"+p+"-feedback ds-coachmark-feedback",events:{click:f}};this.pagesList=e.createElement("ul",{html:d,styles:{position:"relative","white-space":"nowrap",height:m+"px",width:"100%"}}).inject(this.elements.container),this.feedback=e.createElement("ul",{html:h,className:"ds-coachmark-feedback-list"}).inject(this.elements.container),this.checkbox=e.createElement("label",{class:"ds-coachmark-checkbox",html:[{tag:"input",type:"checkbox",checked:i.doNotShowAgain},{tag:"span"}]}).appendText(this._getTextNLS(i.lang)).inject(this.elements.container),this.backgroundOverlay=e.createElement("div",{class:"coachmark-background-overlay",styles:{backgroundColor:"white",width:"100%",height:"100%",transition:"all 0.3s ease-out",opacity:0,position:"absolute",top:0,left:0,zIndex:500},events:{click:this.dispatchAsEventListener("_onClose")}}),this.inject(e.extendElement(n)),this.backgroundOverlay.inject(e.extendElement(n)),this.overlay.open(),this.index=0,this.backgroundOverlay.setStyle("opacity",.5),g=o.extend({isItemVisible:function(e,t){return this.scroller.isInViewport(e.getElement(".ds-coachmark-index"),!1!==t)},getLastItemVisible:function(){var e=this.getVisibleItems(!1);return e.length||(e=this.getVisibleItems(!0)),e.length?e[e.length-1]:null},getFirstItemVisible:function(){var e,t,n,s=this.getItems();for(t=0,n=s.length;t<n;t+=1)if(e=s[t],this.isItemVisible(e,!0))return e;return null}}),this.scroller=new g(this.pagesList,{className:"ds-coachmark-carousel",scrollSize:1,scrollButtonClassName:"ds-coachmark-nav-button",autoScrollPressedDelay:1e4,scrollerOptions:{snap:!0,scrollDrag:!0}}),this.scroller.elements.nextButton.getElement("span").addClassName("fonticon fonticon-play"),this.scroller.elements.previousButton.getElement("span").addClassName("fonticon fonticon-play"),this.scroller.scroller.scrollToElement=function(t,n,s){t=e.extendElement(t),"string"==typeof n?n={x:n,y:n}:void 0===n&&(n={x:"center",y:"center"});var o,i,r,l,c=this.scroll,h=this.maxScroll,d=a.getPosition.call(t,this.elements.wrapper),m=a.getDimensions.call(t),p=this.getSnapSize(),u={x:c.width,y:c.height},g={};for(m={x:m.outerWidth,y:m.outerHeight},o=0;o<2;o+=1)c[i=o?"x":"y"]?(r=-d[i],"center"===n[i]?r+=(u[i]-m[i])/2:"end"===n[i]&&(r+=u[i]-m[i])):r=0,l=r<h[i],r=r>0?0:l?h[i]:r,!l&&this.options.snap&&(r=Math[o?"floor":"round"](Math.round(r)/p[i])*p[i]),r<h[i]&&(r=h[i]),g[i]=r;this.position.x===g.x&&this.position.y===g.y||this.setPosition(g.x,g.y,s)},this.scroller.scroller.getPosition=function(){var e,t=this.position,n=this.options,s=this.elements;if(n.useNative)t={y:-s.scroller.scrollTop,x:-s.scroller.scrollLeft};else if(n.useTransform){if(t={y:0,x:0},(e=a.getStyle.call(s.wrapper,"transform",!0))&&"none"!==e)if(r.cssMatrix)try{t={y:(e=new WebKitCSSMatrix(e)).f,x:e.e}}catch(e){}else t={y:+(e=e.replace(/[^0-9-.,]/g,"").split(","))[5],x:+e[4]}}else t={y:s.wrapper.getComputedSize("top"),x:s.wrapper.getComputedSize("left")};return{x:Math.round(t.x),y:Math.round(t.y)}},v(),this.scroller.scroller.addEvent("onScrollEnd",this.onScrollEnd.bind(this)),this.scroller.updateNoDelay?this.scroller.updateNoDelay():this.scroller.onRefreshNoDelay(),this.onScrollEnd(),this.elements.container.addEvent("click",this.dispatchAsEventListener("_getFocus"))}},onScrollEnd:function(){var e,t;(e=this.elements.container.getElement(".ds-coachmark-highlight"))&&e.removeClassName("ds-coachmark-highlight"),(t=this.scroller.getLastItemVisible())&&(this.index=t.getParent().getChildren().indexOf(t),this.elements.container.getElement("."+t.getData("feedback")).addClassName("ds-coachmark-highlight"))},_onClose:function(e){t.stop(e),this.overlay.close(),this.backgroundOverlay.setStyle("opacity",0),this.dispatchEvent("onClose")},_getFocus:function(){this.elements.container.getElement(".uwa-scroller").focus()},onEndClose:function(){this.backgroundOverlay.destroy(),this.destroy()},getDoNotShowAgainState:function(){return this.elements.container.getElement("input")?this.elements.container.getElement("input").checked:null},_getTextNLS:function(e){return{en:"Do not show at startup",fr:"Ne pas afficher au démarrage",ko:"시작할 때 표시 안 함",es:"No mostrar al inicio",ru:"Не показывать при запуске",zh:"不在启动时显示",de:"Nicht beim Start anzeigen",ja:"起動時に表示しない",it:"Non mostrare all'avvio",pt_BR:"Não mostrar ao inicializar",cs:"Nezobrazovat při spuštění",pl:"Nie pokazuj przy uruchamianiu programu",zh_TW:"啟動時請勿顯示"}[e]}})}),define("DS/Coachmark/Coachmark",["UWA/Core","UWA/Storage","UWA/Storage/Adapter/Cookies","UWA/Utils","DS/WAFData/WAFData","DS/Coachmark/CoachmarkControl"],function(e,t,n,s,o,a){"use strict";var i,r=null;return i={init:function(n){var l,c,h,d,m,p,u,g,f,y,k;k=["en","fr","ko","es","ru","zh","de","ja","it","pt_BR","cs","pl","zh_TW"],l={myAppsBaseURL:"",parentEltSelector:"body",forceDisplay:!1,lang:"en",fullscreen:!1,xappsMode:!1,events:{onDNSAChange:null}},n.lang?(n.lang=n.lang.replace("-","_"),-1===k.indexOf(n.lang)&&(n.lang="en")):n.lang="en",this.options=e.merge(n,l),c=this.options,(h=e.extendElement(document)).getElement(".ds-coachmark")||((d=h.getElement(c.parentEltSelector))||(d=h.getElement("body")),".3ds.com",void 0!==window&&null!==window&&-1!==window.location.hostname.indexOf(".3ds.com",window.location.hostname.length-".3ds.com".length)?m=new t({adapter:"Cookies",database:"myDatabase",adapterOptions:{domain:".3ds.com",path:"/"}}):(m=new t).setCurrentAdapterFromDetected(),p={method:"GET",timeout:5e3,headers:{Accept:"application/json"}},u=function(){c.dnsaStatus=m.get("dnsa"),r=f()},g=function(e){if(""===e)return u();try{if(JSON.parse(e).error)return u()}catch(e){return u()}c.dnsaStatus=e.contains("true"),r=f()},f=function(){var t,n,s,l=c.lang;for(t=[],n=1,4;n<=4;n++)s=3===n&&c.xappsMode?"xapps-":"",t.push({html:{tag:"div",class:"ds-coachmark-content-image ds-coachmark-content-"+s+l+"-"+n}});return r=new a(d,{pages:t,events:{onClose:function(){var t=r.getDoNotShowAgainState();if(t!==c.dnsaStatus&&"none"!==c.myAppsBaseURL)try{p.onFailure=p.onComplete=function(){var n=i.options.events.onDNSAChange;m.set("dnsa",t),e.is(n,"function")&&n(t)},""===c.myAppsBaseURL?p.onFailure():o.authenticatedRequest(c.myAppsBaseURL+"/resources/AppsMngt/user/setPreferences?name=dnsa&value="+t+"&_="+Date.now(),e.clone(p))}catch(e){p.onFailure(e)}else"none"===c.myAppsBaseURL&&c.events.closeAndNotify();r=null}},doNotShowAgain:c.dnsaStatus,forceDisplay:c.forceDisplay,lang:c.lang,fullscreen:c.fullscreen})},y=function(t,a){if(e.is(n.dnsa))g(""+n.dnsa);else try{""===c.myAppsBaseURL?u():(s.parseUrl(c.myAppsBaseURL).domain!==(void 0!==window&&null!==window?window.location.hostname:void 0)&&(a.proxy="passport"),a.onFailure=u,a.onComplete=g,o.authenticatedRequest(t+"/resources/AppsMngt/user/getPreferences?name=dnsa&_="+Date.now(),e.clone(a)))}catch(e){a.onFailure(e)}},"boolean"!=typeof c.dnsaStatus?y(c.myAppsBaseURL,p):r=f())},getDNSAStatus:function(){return r.getDoNotShowAgainState()}}}),define("DS/i3DXCoachmark",["DS/Coachmark/Coachmark"],function(e){"use strict";return e}),define("Coachmark",["DS/Coachmark/Coachmark"],function(e){"use strict";return e}),define("Coachmark/Coachmark",["DS/Coachmark/Coachmark"],function(e){"use strict";return e});