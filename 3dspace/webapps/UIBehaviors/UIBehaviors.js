define("DS/UIBehaviors/Kinetic",["UWA/Core","UWA/Event","DS/Core/Core","DS/CoreEvents/Events","UWA/Class","DS/Core/ModelEvents","DS/Core/PointerEvents"],function(t,e,i,n,s,o,r){"use strict";function a(t,e){return void 0===t&&(t={x:0,y:0}),void 0===e&&(e={x:0,y:0}),{x:t.x-e.x,y:t.y-e.y}}function h(t,e){return{x:t.x+e.x,y:t.y+e.y}}return s.extend({defaultOptions:{scrollX:!0,scrollY:!0,snap:!0,viewport:null,range:{xMin:0,xMax:1e4,yMin:0,yMax:1e4},sideBorderSize:30,duration:100,viscosity:.2},init:function(e){this.options=t.extend(this.defaultOptions,e),this._parent(e),this._view,this._min,this._max,this._offset,this._initialPosition,this._pressed,this._xform,this._velocity={x:0,y:0},this._frame={x:0,y:0},this._timestamp,this._ticker,this._amplitude={x:0,y:0},this._target={x:0,y:0},this._timeConstant={x:0,y:0},this._snap,this._isFromBorder=null,this._view=this.options.target,this._max=parseInt(getComputedStyle(this._view).height,10)-innerHeight,this._offset=this._min={x:0,y:0},this._pressed=!1,this._timeConstant=this.options.duration,this._x=0,this._y=0,this._px=0,this._py=0;var i=this;this._xform="transform",["webkit","Moz","O","ms"].every(function(t){var e=t+"Transform";return void 0===i._view.style[e]||(i._xform=e,!1)}),this._modelEvents=new o,this._buildTouchBorders(),this._bindEvents()},_buildTouchBorders:function(){var e=this;this.elements={},this.elements.leftBorder=new t.Element("div",{styles:{position:"absolute",height:"100%",width:this.options.sideBorderSize,background:"",top:0}}).inject(this._view),this.elements.leftBorder.addEvent(r.POINTERUP,function(t){this.setStyles({width:e.options.sideBorderSize})}),this.elements.leftBorder.addEvent(r.POINTERDOWN,function(t){this.setStyles({width:"100%"})}),this.elements.rightBorder=new t.Element("div",{styles:{position:"absolute",height:"100%",width:this.options.sideBorderSize,background:"",top:0,right:0}}).inject(this._view),this.elements.rightBorder.addEvent(r.POINTERUP,function(t){this.setStyles({width:e.options.sideBorderSize}),this._isFromBorder="right"}),this.elements.rightBorder.addEvent(r.POINTERDOWN,function(t){this.setStyles({width:"100%"}),this._isFromBorder="left"})},_bindEvents:function(){void 0!==window.ontouchstart&&(this._view.addEventListener("touchstart",this._start.bind(this)),this._view.addEventListener("touchmove",this.drag.bind(this)),this._view.addEventListener("touchcancel",this._stop.bind(this)),this._view.addEventListener("touchend",this._stop.bind(this))),this._view.addEventListener("mousedown",this._start.bind(this)),this._view.addEventListener("mousemove",this.drag.bind(this)),this._view.addEventListener("mouseleave",this._stop.bind(this)),this._view.addEventListener("mouseup",this._stop.bind(this))},onAnimationEnd:function(t){return this._modelEvents.subscribe({event:"ANIMATION_END"},t)},onRelease:function(t){return this._modelEvents.subscribe({event:"_stop"},t)},_getPointerPosition:function(t){return t.targetTouches&&t.targetTouches.length>=1?{x:t.targetTouches[0].clientX,y:t.targetTouches[0].clientY}:{x:t.clientX,y:t.clientY}},_registerPosition:function(t){this._offset&&this._snap&&(this._forceBeginPos={x:parseInt(t,10)/100*this._snap.x,y:0})},_disableInteractions:function(){this._view.setStyles({pointerEvents:"none",background:"red"})},_enableInteractions:function(){this._view.setStyles({pointerEvents:"auto"})},getPosition:function(){return this._offset},getFinalPosition:function(){return this._target},getPositionPercent:function(){return this._offsetPercent},getFinalPositionPercent:function(){return{x:Math.min(Math.max(this._target.x/this._snap.x*100,this._min.x),this._max.x),y:Math.min(Math.max(this._target.y/this._snap.y*100,this._min.y),this._max.y)}},_observeParameters:function(){var t,e,i,n,s;if(this._pressed){e=(t=Date.now())-this._timestamp,this._timestamp=t,this._prevOffset||(this._prevOffset={x:0,y:0}),i={x:this._x-this._px,y:this._y-this._py},this._px=this._x,this._py=this._y,this._prevOffset=this.offset,this._frame=this._offset;n=1e3*i.x/(1+e),s=1e3*i.y/(1+e);this._velocity={x:.8*n+.2*this._velocity.x,y:.8*s+.2*this._velocity.y}}},_resetParams:function(){this._px=0,this._py=0},_inertialScroll:function(){var t,e;this._amplitude&&!this._pressed?(t=Date.now()-this._timestamp,e={x:-this._amplitude.x*Math.exp(-t/this._timeConstant),y:-this._amplitude.y*Math.exp(-t/this._timeConstant)},this.options.duration&&(e.x>5||e.x<-5||e.y>5||e.y<-5)?(this.scroll(h(this._target,e)),requestAnimationFrame(this._inertialScroll.bind(this))):(this.scroll(this._target,!0),this.options.duration&&this._view.setStyles({transition:this._oldTransitionValue}),this._modelEvents.publish({context:this,event:"ANIMATION_END",data:{finalPositionInPixel:this.getFinalPosition(),finalPositionInPercent:this.getFinalPositionPercent(),fromBorder:!!this._isFromBorder,fromWhichBorder:this._isFromBorder}}),this._isFromBorder=!1,this._lastOffsetPercent=this._offsetPercent)):(this._modelEvents.publish({context:this,event:"ANIMATION_END",data:{finalPositionInPixel:this.getFinalPosition(),finalPositionInPercent:this.getFinalPositionPercent(),fromBorder:!!this._isFromBorder,fromWhichBorder:this._isFromBorder}}),this._isFromBorder=!1,this._lastOffsetPercent=this._offsetPercent)},_start:function(t){t.preventDefault(),this._pressed=!0,this._initialPosition=this._getPointerPosition(t),this._velocity=this._amplitude={x:0,y:0},this._frame=this._offset=this._target,this._timestamp=Date.now();var e=this.options.viewport?this.options.viewport:this._view.getParent().getDimensions();return this._min={x:this.options.range.xMin,y:this.options.range.yMin},this._max={x:this.options.range.xMax/100*e.width,y:this.options.range.yMax/100*e.height},this._snap={x:e.width,y:e.height},this._oldTransitionValue=this._view.getStyle("transition"),this._view.setStyles({transition:"none"}),clearInterval(this._ticker),this._ticker=setInterval(this._observeParameters.bind(this),100),!1},drag:function(t){var e,i;return this._pressed&&(e=this._getPointerPosition(t),((i=a(this._initialPosition,e)).x>10||i.x<-10||i.y>10||i.y<-10)&&(this._initialPosition=e,this._forceBeginPos&&(this._offset=a(this._forceBeginPos,e),this._offset.x=-this._forceBeginPos.x,this._forceBeginPos=null),this.scroll(h(this._offset,i)))),t.preventDefault(),!1},scroll:function(t,e){this._x=this._offset.x=this.options.scrollX?t.x:0,this._y=this._offset.y=this.options.scrollY?t.y:0,this._x&&(this._x=this._offset.x=t.x>this._max.x?this._max.x:t.x<this._min.x?this._min.x:t.x),this._y&&(this._y=this._offset.y=t.y>this._max.y?this._max.y:t.y<this._min.y?this._min.y:t.y),this._offsetPercent={x:this._offset.x/this._snap.x*100,y:this._offset.y/this._snap.y*100},(this.options.duration||e)&&(this._view.style[this._xform]="translate3d("+-this._offsetPercent.x+"%,"+-this._offsetPercent.y+"%, 0)"),this.elements.leftBorder.setStyles({left:this._offsetPercent.x+"%"}),this.elements.rightBorder.setStyles({right:-this._offsetPercent.x+"%"})},_stop:function(t){if(this._pressed){0,this._pressed=!1;var e=this.options.viewport?this.options.viewport:this._view.getParent().getDimensions();if(this._snap={x:e.width,y:e.height},clearInterval(this._ticker),this._target=this._offset,this._velocity.x>10||this._velocity.x<-10||this._velocity.y>10||this._velocity.y<-10){this._amplitude={x:.8*this._velocity.x,y:.8*this._velocity.y},this._target=h(this._offset,this._amplitude)}this._target={x:Math.round(this._target.x/this._snap.x)*this._snap.x,y:Math.round(this._target.y/this._snap.y)*this._snap.y},this._amplitude=a(this._target,this._offset),this._timestamp=Date.now(),this._velocity.x||this._velocity.y?(this._modelEvents.publish({context:this,event:"_stop",data:{finalPositionInPixel:this.getFinalPosition(),finalPositionInPercent:this.getFinalPositionPercent(),fromBorder:!!this._isFromBorder,fromWhichBorder:this._isFromBorder}}),this._isFromBorder=!1,requestAnimationFrame(this._inertialScroll.bind(this))):this._view.setStyles({transition:this._oldTransitionValue})}}})}),define("DS/UIBehaviors/DragAndDropManager",["UWA/Core","UWA/Event","DS/Core/Core","DS/CoreEvents/Events","UWA/Class","UWA/Utils"],function(t,e,i,n,s,o){"use strict";var r=s.singleton({defaultOptions:{},init:function(e,i){this.options=t.extend(this.defaultOptions,i),this._parent(i),this.subUid=-1,this._data=[],this._globalData=null},storeGlobalData:function(t){this._globalData=t},storeData:function(t,e){var i="WEBUX_DND_TOKEN|"+o.getUUID();return this._data[i]={data:t,dom:e},i},isTokenValid:function(t){return-1!==t.indexOf("WEBUX_DND_TOKEN|")},getGlobalData:function(){return this._globalData},removeGlobalData:function(){delete this._globalData},getDataFromToken:function(t,e){if(void 0===e&&(e=!0),t&&""!==t)if(this.isTokenValid(t)){if(this._data[t]&&this._data[t].data)return this._data[t].data}else console.warn("WebUX Drag and Drop Token is not valid. It seems you use setDataTransfer to store customData. Please be aware that, in this case, from, and infos parameters could be wrong in the drag and drop callback of this component.")},getDataFromDom:function(t,e){return void 0===e&&(e=!0),i=this._data,n=function(e,i,n){return t===e.dom},s=this,o=null,i.some(function(t,e){return!!n.call(s,t,e,i)&&(o=t,!0)}),o;var i,n,s,o}});return t.namespace("UIBehaviors/DragAndDropManager",r)}),define("DS/UIBehaviors/PopupManager",["UWA/Core","UWA/Class","DS/TreeModel/TreeNodeModel","DS/Utilities/Dom"],function(t,e,i,n){return e.singleton({defaultOptions:{treePopup:null,lastPopup:null},init:function(t){this._parent(t)},add:function(t){if(!this.find(t)){var e=this.getContainingPopup(t.target);e?(e.getChildren()&&this.close(e.getChildren()[0]),this.lastPopup=e):(this.close(this.treePopup),this.treePopup=null,this.lastPopup=null);var n=new i({grid:{data:t}});this.lastPopup?this.lastPopup.addChild(n):this.treePopup=n,this.lastPopup=n}},remove:function(t){this.close(this.find(t))},removeChild:function(t){var e=this.find(t);e&&e.getChildren()&&(this.close(e.getChildren()[0]),this.lastPopup=e)},getContainingPopup:function(t){var e=t.getContent?t.getContent():t;if(e){var i=n.firstParentWithClass(e,"wux-controls-popup");if(i&&i.dsModel)return this.find(i.dsModel)}},find:function(t){if(null!=t){for(var e=this.treePopup;e&&e.options.grid.data!==t&&null!==e.getChildren();)e=e.getChildren()[0];return e&&e.options.grid.data===t?e:void 0}},close:function(t){if(null!=t)if(t.options.grid.data.visibleFlag=!1,t.options.grid.data=null,t.processDescendants({processNode:function(t){t.nodeModel.options.grid.data.visibleFlag=!1,t.nodeModel.options.grid.data=null,t.nodeModel.getParent()&&t.nodeModel.remove()}}),t===this.treePopup)this.treePopup=null,this.lastPopup=null;else{var e=t.getParent();e&&(t.remove(),this.lastPopup=e)}},_getTreePopup:function(){return this.treePopup},_getLastPopup:function(){return this.lastPopup}})}),define("DS/UIBehaviors/ContextualMenuManager",["UWA/Core","UWA/Controls/Abstract","DS/Core/PointerEvents","UWA/Utils"],function(t,e,i,n){"use strict";return e.singleton({defaultOptions:{},init:function(t){this._parent(t),this._buildView(),this._listeners=Object.create(null)},_populateContextualMenu:function(t){var e,i;t&&t!==document&&t.hasAttribute("wux-contextualmenu-id")&&(e=t.getAttribute("wux-contextualmenu-id"),i=this._listeners[e],this._buildMenu(i),this._populateContextualMenu(t.parentNode))},_buildView:function(){var e=this;this.elements.container=new t.Element("div",{class:"wux-uibehaviors-contextualmenu"}).hide().inject(document.body),this.elements.container.addEventListener("contextmenu",function(t){t.preventDefault()}),window.addEventListener(void 0!==document.body.onwheel?"wheel":"mousewheel",function(t){e._hideContextualMenuIfOutside(t.target)},!0),window.addEventListener("mousedown",function(t){e._hideContextualMenuIfOutside(t.target)},!0),window.addEventListener("touchstart",function(t){Array.prototype.forEach.call(t.changedTouches,function(t){e._hideContextualMenuIfOutside(t.target)})},!0)},_buildMenu:function(e){var i,n,s=this;e&&(i="function"==typeof e.nodes?e.nodes():e.nodes)&&i.forEach(function(i){var o=i.sensitivity&&"disabled"===i.sensitivity.toLowerCase();i.type&&"separator"===i.type?n=new t.Element("ul",{class:"wux-ui-stack-h wux-uibehaviors-contextualmenu-separator",styles:{display:"block"}}).inject(s.elements.container):(n=new t.Element("ul",{class:"wux-ui-stack-h wux-uibehaviors-contextualmenu-node",styles:{display:"block"}}).inject(s.elements.container),o&&n.classList.add("wux-uibehaviors-contextualmenu-node-disabled"),new t.Element("li",{class:"wux-uibehaviors-contextualmenu-node-icon",styles:{"background-image":i.icon}}).inject(n),new t.Element("li",{class:"wux-uibehaviors-contextualmenu-node-label",html:i.label}).inject(n)),i.callback&&!o&&n.addEventListener("click",function(t){s.elements.container.hide(),i.callback.call(e.context,i.data)})})},_showContextualMenu:function(){var t,e=!0;for(t=this.elements.container.firstChild;t&&e;t=t.nextSibling)""!==t.firstChild.style.backgroundImage&&(e=!1);e?this.elements.container.addClassName("wux-uibehaviors-contextualmenu-no-icons"):this.elements.container.removeClassName("wux-uibehaviors-contextualmenu-no-icons"),this.elements.container.show()},_hideContextualMenuIfOutside:function(t){for(;t&&t!==this.elements.container;t=t.parentNode);t||this.elements.container.hide()},_onFireEvent:function(t){return this.show(t.currentTarget,[t.pageX,t.pageY]),t.stopPropagation(),t.preventDefault(),!1},_onFireEventTouch:function(t){if(i.isTouchEvent(t))return this.show(t.currentTarget,[t.pageX,t.pageY]),t.stopPropagation(),t.preventDefault(),!1},onReady:function(t){this.listen(t)},listen:function(t){if(!t.target.hasAttribute("wux-contextualmenu-id")){var e=n.getUUID();this._listeners[e]=t,t.target.setAttribute("wux-contextualmenu-id",e),t.target.addEventListener("contextmenu",this._onFireEvent.bind(this)),t.target.addEventListener(i.LONGHOLD,this._onFireEventTouch.bind(this))}},forget:function(t){if(t.target.hasAttribute("wux-contextualmenu-id")){var e=t.target.getAttribute("wux-contextualmenu-id");delete this._listeners[e],t.target.removeAttribute("wux-contextualmenu-id"),t.target.removeEventListener("contextmenu",this._onFireEvent.bind(this)),t.target.removeEventListener(i.LONGHOLD,this._onFireEventTouch.bind(this))}},hide:function(){this.elements.container.hide()},show:function(t,e,i){var n=35;if(!1,this.elements.container.hide(),this.elements.container.empty(),t.nodes?this._buildMenu(t):this._populateContextualMenu(t),this.elements.container.childElementCount>0){var s=e[0],o=e[1];0,this.elements.container.setStyles({left:s,top:o}),this._showContextualMenu();var r=window.innerWidth,a=window.innerHeight,h=this.elements.container.getBoundingClientRect();(h.right>r||h.bottom>a)&&(console.log("reposition"),s=e[0],o=e[1],h.right>r&&(s-=h.width,n=-n),h.bottom>a&&(o-=h.height),s<0&&(s=0),o<0&&(o=0),this.elements.container.setStyles({left:s,top:o}))}}})}),define("DS/UIBehaviors/SwipeableMenu",["UWA/Core","DS/Core/Core","UWA/Controls/Abstract","DS/Core/PointerEvents"],function(t,e,i,n){var s=["webkit","moz","MS","o",""];return i.extend({defaultOptions:{target:null,menu:[],menuItemWidth:60},init:function(t){this._parent(t),this._isOpen=!1,this._buildView();var e=this;this.options.menu.reverse().forEach(function(t,i){e._addMenuItem(t,i)}),this._listenEvents(),this._indexPositions(!0),this.close()},_buildView:function(){this.elements.container=new t.Element("div",{class:"wux-swipeablemenu",styles:{overflow:"hidden"}}),this.elements.layersContainer=new t.Element("ul",{class:"wux-ui-stack-h",styles:{position:"relative"}}).inject(this.elements.container),this.elements.layers=[],this.elements.cover=new t.Element("li",{class:"",styles:{position:"relative",display:"block",width:this.options.target.getStyle("width"),height:this.options.target.getStyle("height"),zIndex:this.options.menu.length+1}}).inject(this.elements.layersContainer),this.options.target.inject(this.elements.cover)},_indexPositions:function(t){this._listOfMenuPositionAtBegin=[],this._listOfMenuPositionAtEnd=[];for(var e=0,i=0;i<this.options.menu.length;i++){var n=this.options.menuItemWidth;this._listOfMenuPositionAtBegin[i]=-n,t||this.elements.layers[i].setStyles({width:n,right:this._listOfMenuPositionAtBegin[i]}),e=n*(i+1),this._listOfMenuPositionAtEnd[i]=e,this._endPosition=e}return e},_listenEvents:function(){var e,i,s,o=this;o._isPressed=!1;var r=this.options.menu.length,a=!1;function h(t){o._isPressed=!1,o._indexPositions(!0),!a&&o._listOfMenuPositionAtBegin&&(o.close(),o.options.menu.forEach(function(t,e){o.elements.layers[e].setStyles({transition:"all ease-in-out 0.2s",right:o._listOfMenuPositionAtBegin[e]})}))}var l=t.extendElement(document);this.elements.cover.addEvent(n.POINTERDOWN,function(t){a=!1,o._isPressed=!0,s=o._indexPositions(),e={x:t.clientX,y:t.clientY},o.elements.cover.setStyles({transition:"none"}),o.options.menu.forEach(function(t,e){o.elements.layers[e].setStyles({transition:"none"})})}),l.addEvent(n.POINTERMOVE,function(t){o._isPressed&&o.options.menu.length>0&&(i={x:t.clientX-e.x,y:t.clientY-e.y},o.elements.cover.getPosition().x<20?Math.abs(i.x)>10&&-i.x<s?(o.elements.cover.setStyles({transform:"translate3d("+i.x+"px, 0,0)"}),o.options.menu.forEach(function(t,e){var n=o.options.menuItemWidth,s=Math.abs(i.x)/r*(e+1)-n;s<o._listOfMenuPositionAtEnd[e]&&o.elements.layers[e].setStyles({right:s})})):-i.x>=s&&(a=!0,o.open()):h())}),l.addEvent(n.POINTERUP,h)},open:function(){var t=this;this.elements.cover.addEventListener("animationend",function(){console.log("--",t.elements.cover.getOffset())},!1),function(t,e,i){for(var n=0;n<s.length;n++)s[n]||(e=e.toLowerCase()),t.addEventListener(s[n]+e,i,!1)}(this.elements.cover,"animationIteration",function(){console.log("--",t.elements.cover.getOffset())}),this.elements.cover.setStyles({transition:"all ease-in-out 0.2s",transform:"translate3d("+-this._endPosition+"px, 0,0)"});var e={x:-this._endPosition},i=this.options.menu.length;t.options.menu.forEach(function(n,s){var o=t.options.menuItemWidth,r=Math.abs(e.x)/i*(s+1)-o;r<t._listOfMenuPositionAtEnd[s]&&t.elements.layers[s].setStyles({right:r})}),this._isOpen=!0},close:function(){this._isPressed=!1,this.elements.cover.setStyles({transition:"all ease-in-out 0.2s",transform:"translate3d(0, 0,0)"}),this._isOpen=!1},toggle:function(){this._isOpen?this.close():this.options.menu.length>0&&this.open()},_addMenuItem:function(e,i){var s=this,o=t.extend({position:"absolute",padding:"0 10px",top:0,bottom:0,right:0,boxSizing:"border-box",lineHeight:this.options.target.getStyle("height"),background:e.styles.backgroundColor},e.styles);this.elements.layers[i]=new t.Element("li",{html:e.label,class:"",styles:o}).inject(this.elements.layersContainer),this.elements.layers[i].addEvent(n.POINTERUP,function(){s.elements.cover.setStyles({transition:"all ease-in-out 0.2s",transform:"translate3d(0, 0,0)"}),e.callback&&e.callback.call(this)})}})}),define("DS/UIBehaviors/CommandsRegistry",["require","exports"],function(t,e){"use strict";return function(){function t(){this._commands={}}return t.prototype.getCommand=function(t){return this._commands[t]?this._commands[t]:null},t.prototype.dispatch=function(t,e){var i,n=this.getCommand(t);return n&&(i=n.call(e)),i},t.prototype.registerCommand=function(t,e){this._commands[t]=e},t}()}),define("DS/UIBehaviors/KeymapManager",["UWA/Core","DS/Utilities/Utils","DS/UIBehaviors/CommandsRegistry","DS/Utilities/Dom"],function(t,e,i,n){"use strict";var s=30,o=!1,r=null,a=!1,h=function(t,e,i,s){if(t._domTarget&&void 0!==e&&!1===o&&t._shouldProcessEvent({event:s})){var r,a,h,l,u,d,c=t._keymap.all,_=t._keymap.defaultKey,f=t._keymap[i],p=!1;if(c&&(r=c[s.type])&&(a=e.getCommand(r)),f?(h=f[s.type])&&(l=e.getCommand(h)):_&&(u=_[s.type])&&(d=e.getCommand(u)),a||l||d){var m,v;if(n.pushUserInteractionContext(),a&&(v=n.callMethodFromUIInteraction(a.name,t._jsModel,a,[s]),t._spyCB&&t._spyCB({keymap:i,commandForKey:r})),l?(m=n.callMethodFromUIInteraction(l.name,t._jsModel,l,[s]),t._spyCB&&t._spyCB({keymap:i,commandForKey:h})):_&&(m=n.callMethodFromUIInteraction(d.name,t._jsModel,d,[s]),t._spyCB&&t._spyCB({keymap:i,commandForKey:u})),n.popUserInteractionContext(),(m&&m.preventDefault||"space"===i&&(!s.target||s.target.type&&-1===s.target.type.indexOf("text")&&"password"!==s.target.type&&"email"!==s.target.type))&&s.preventDefault(),"keyup"===s.type&&t._infosOnKeyDown&&t._infosOnKeyDown[i]){for(var g in t._infosOnKeyDown[i])void 0!==t._infosOnKeyDown[i][g]&&(s[g]=t._infosOnKeyDown[i][g]);t._infosOnKeyDown[i]=void 0,delete t._infosOnKeyDown[i]}if(v&&v.infos){for(var g in v.infos)void 0!==v.infos[g]&&(s[g]=v.infos[g]);if("keydown"===s.type)if(t._infosOnKeyDown)if(t._infosOnKeyDown[i])for(var g in v.infos)t._infosOnKeyDown[i][g]=v.infos[g];else t._infosOnKeyDown[i]=v.infos;else t._infosOnKeyDown={},t._infosOnKeyDown[i]=m.infos}if(m&&m.infos){if("keydown"===s.type)if(t._infosOnKeyDown)if(t._infosOnKeyDown[i])for(var g in m.infos)t._infosOnKeyDown[i][g]=m.infos[g];else t._infosOnKeyDown[i]=m.infos;else t._infosOnKeyDown={},t._infosOnKeyDown[i]=m.infos;for(var g in m.infos)void 0!==m.infos[g]&&(s[g]=m.infos[g])}p=!(m&&!1===m.stopPropagation)}else if("keyup"===s.type&&t._infosOnKeyDown&&t._infosOnKeyDown[i]){for(var g in t._infosOnKeyDown[i])void 0!==t._infosOnKeyDown[i][g]&&(s[g]=t._infosOnKeyDown[i][g]);t._infosOnKeyDown[i]=void 0,delete t._infosOnKeyDown[i]}p&&(s.stopPropagation(),s.stopImmediatePropagation())}};function l(e){this._keymap={},this._domTarget=e.target,this._jsModel=e.jsModel,!e.jsModel&&this._domTarget&&(this._jsModel=this._domTarget.dsModel),this._commandsRegistry=e.commandsRegistry,this._shouldProcessEvent=e.shouldProcessEvent||function(){return!0};var i,n=0,o=this;return this._processCB=function(e){var l=window.performance?performance.now():Date.now(),u=t.Event.whichKey(e);void 0!==window.__karma__||u!==i||r||e.type!==r.type||l-n>=s?(r=e,a&&(console.group("keystroke:"+u),console.time("process "+u)),h(o,o._commandsRegistry,u,e),a&&(console.groupEnd("keystroke:"+u),console.timeEnd("process "+u)),n=window.performance?performance.now():Date.now(),i=u):(a&&console.log("Ignore keydown",l,n,l-n),e.preventDefault(),e.stopPropagation(),e.stopImmediatePropagation())},t.Element.addEvent.call(this._domTarget,"keydown",this._processCB),t.Element.addEvent.call(this._domTarget,"keyup",this._processCB),this}return l.prototype.destroy=function(){this._keymap=null,t.Element.removeEvent.call(this._domTarget,"keydown",this._processCB),t.Element.removeEvent.call(this._domTarget,"keyup",this._processCB)},l.prototype.wait=function(){o=!0},l.prototype.listen=function(){o=!1},l.prototype._getCurrentEvent=function(){return r},l.prototype.registerKeymap=function(t,e,i){this._keymap[t]={keydown:e,keyup:i}},l.prototype.spy=function(t){this._spyCB=t},l.prototype.unregisterKeymap=function(t){this._keymap[t]&&delete this._keymap[t]},l.prototype._getKeymap=function(){return this._keymap},l}),define("DS/UIBehaviors/Droppable",["UWA/Core","UWA/Event","DS/Core/Core","DS/CoreEvents/Events","UWA/Class","DS/UIBehaviors/DragAndDropManager"],function(t,e,i,n,s,o){"use strict";var r=s.extend({defaultOptions:{scope:"",dataTransfer:{type:"Text"},onDragLeave:null,onDragOver:null},init:function(e,i,n){this.options=t.extend(this.defaultOptions,n),this._parent(n),this._uiTarget=i,this._objectTarget=e,this.enable()},_attachBehavior:function(t,e){this._uiTarget&&this._uiTarget.addEventListener(t,e)},_dettachBehavior:function(t){this._uiTarget&&this._uiTarget.removeEventListener(t)},enable:function(){var t=this;this._attachBehavior("dragover",function(t){t.preventDefault()}),this._attachBehavior("dragover",function(e){t.options.onDragOver&&t.options.onDragOver.call(t._objectTarget,e,t._uiTarget)}),this._attachBehavior("dragenter",function(e){t.options.onDragEnter&&t.options.onDragEnter.call(t._objectTarget,e,t._uiTarget)}),this._attachBehavior("dragleave",function(e){t.options.onDragLeave&&t.options.onDragLeave.call(t._objectTarget,e,t._uiTarget)}),this._attachBehavior("drop",function(e){e.preventDefault(),e.stopPropagation();var i=e.dataTransfer.getData(t.options.dataTransfer.type),n=o.getDataFromToken(i);o.removeGlobalData(),t.options.drop&&t.options.drop.call(t._objectTarget,e,t._uiTarget,n)})},disable:function(){this._toggleBehaviors(!1)}});return t.namespace("UIBehaviors/Droppable",r)}),define("DS/UIBehaviors/Draggable",["UWA/Core","UWA/Event","DS/Core/Core","DS/CoreEvents/Events","UWA/Class","DS/UIBehaviors/DragAndDropManager","UWA/Controls/Abstract"],function(t,e,i,n,s,o,r){"use strict";return r.extend({defaultOptions:{scope:"",helper:null,onDragStart:null,onDrop:null,onDrag:null,onDragStop:null,onDragOver:null,dataTransfer:{type:"Text",data:null},data:null},init:function(t,e,i){this._parent(i),this._uiTarget=e,this._objectTarget=t,i&&(this._from=i.from||t,this._data=i.data,this.updateDataToBeTransferred({from:i.from||t,data:i.data}),this._dataTransfer=this.options.dataTransfer),this.enable()},_toggleBehaviors:function(t){this._uiTarget&&(t=t?"true":"false",this._uiTarget.setAttribute("draggable",t))},_attachBehavior:function(t,e){this._uiTarget&&this._uiTarget.addEventListener(t,e)},_dettachBehavior:function(t){this._uiTarget&&this._uiTarget.removeEventListener(t)},updateDataToBeTransferred:function(t){this._dataToBeTransferred=t},getTransferredData:function(){return this._dataToBeTransferred},addDataTransfer:function(t,e){},enable:function(){this._toggleBehaviors(!0);var t=this;this.options.helper||(this.options.helper=this._uiTarget),this._uiTarget.addEventListener("dragstart",function(e){if(t._dataTransfer){var i=t.getTransferredData(),n=o.storeData(i,t._uiTarget);0,o.storeGlobalData(i),e.dataTransfer.setData(t._dataTransfer.type,n)}else alert();t.options.onDragStart&&t.options.onDragStart.call(e,e,t.options.helper,t._uiTarget)},!0),this._attachBehavior("drag",function(e){t.options.onDrag&&t.options.onDrag.call(e,e,t.options.helper)}),this._attachBehavior("drop",function(e){t.options.onDrop&&t.options.onDrop.call(e,e,t.options.helper,t._objectTarget)}),this._attachBehavior("dragend",function(e){t.options.onDrag&&t.options.onDragStop.call(e,e,t.options.helper)})},disable:function(){this._toggleBehaviors(!1)}})});