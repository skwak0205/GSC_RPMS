define("DS/ENOXTriptych/js/ENOXTriptych",["DS/CoreEvents/ModelEvents","DS/Handlebars/Handlebars","DS/ENOXWelcomePanel/js/ENOXWelcomePanel","text!DS/ENOXTriptych/html/ENOXTriptych.html","i18n!DS/ENOXTriptych/assets/nls/ENOXTriptych","css!DS/ENOXTriptych/css/ENOXTriptych.css","css!DS/UIKIT/UIKIT.css"],function(t,e,i,s,n){"use strict";var o=window.localStorage.DS_Triptych_DebugON,l=e.compile(s),h=function(){};return h.prototype.init=function(e,i,s,n,o){this._options={},this._modelEvents=e.modelEvents?e.modelEvents:new t,this._options.withtransition=e.withtransition,this._isMobileMode=null,this._options.left={},this._options.left.minWidth=e.left&&e.left.minWidth?e.left.minWidth:40,this._options.left.resizable=!(!e.left||void 0===e.left.resizable)&&e.left.resizable,this._options.left.originalSize=e.left&&e.left.originalSize?e.left.originalSize:300,this._options.left.originalSize=Math.max(this._options.left.minWidth,this._options.left.originalSize),this._options.left.originalState=e.left&&e.left.originalState?e.left.originalState:"close",this._options.left.overMobile=!0,this._isLeftOpen="open"===this._options.left.originalState,this._overlayedLeft=this._options.left.overMobile,this._options.borderLeft=void 0!==e.borderLeft&&e.borderLeft,this._options.right={},this._options.right.minWidth=e.right&&e.right.minWidth?e.right.minWidth:250,this._options.right.resizable=!e.right||void 0===e.right.resizable||e.right.resizable,this._options.right.originalSize=e.right&&e.right.originalSize?e.right.originalSize:300,this._options.right.originalSize=Math.max(this._options.right.minWidth,this._options.right.originalSize),this._options.right.originalState=e.right&&e.right.originalState?e.right.originalState:"close",this._options.right.overMobile=!0,this._isRightOpen="open"===this._options.right.originalState,this._overlayedRight=this._options.right.overMobile,this._options.withCloseInRightPanel=!e.right||void 0===e.right.withClose||e.right.withClose,this._options.withCloseInLeftPanel=!(!e.left||void 0===e.left.withClose)&&e.left.withClose,this._options.borderRight=void 0===e.borderRight||e.borderRight,this._checkCallbacksLeft={},this._checkCallbacksRight={},this._listSubscription=[],this._initDivs(e,i,s,n,o),this._initResizer(),this._subscribeToEvents(),this._subscribeToDomEvents(),this.__mobileBreakpoint=550},h.prototype.getLeftOpen=function(){return this._isLeftOpen},h.prototype.getRightOpen=function(){return this._isRightOpen},h.prototype.getContent=function(){return this._container},h.prototype.isInMobileMode=function(){return this._container&&this._container.offsetWidth<=this.__mobileBreakpoint},h.prototype._subscribeToEvents=function(){var t=this;this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-get-panel-visibility"},function(e){"left"===e&&t._modelEvents.publish({event:"triptych-panel-left-visilibity",data:t._isLeftOpen}),"right"===e&&t._modelEvents.publish({event:"triptych-panel-right-visilibity",data:t._isRightOpen})})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-fetch-panel-visibility-status"},function(e){"object"==typeof e?e.side?"left"===e.side?e.cbFunc(t._isLeftOpen):"right"===e.side?e.cbFunc(t._isRightOpen):"both"!==e.side&&""!==e.side||e.cbFunc({left:t._isLeftOpen,right:t._isRightOpen}):e.cbFunc({left:t._isLeftOpen,right:t._isRightOpen}):"function"==typeof e&&e({left:t._isLeftOpen,right:t._isRightOpen})})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-toggle-panel"},function(e){t._togglePanel(e)})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-show-panel"},function(e){t._showPanel(e)})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-hide-panel"},function(e){t._hidePanel(e)})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-set-size"},function(e){"left"===e.side?(t._currentLeft=e.size,t._options.left.originalSize=e.size,t._refreshContainerLeftSize()):(t._currentRight=e.size,t._options.right.originalSize=e.size,t._refreshContainerRightSize())})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-set-content"},function(e){switch(e.side){case"right":t._rightPanelContent.firstChild&&t._rightPanelContent.removeChild(t._rightPanelContent.firstChild),e&&e.content&&t._rightPanelContent.appendChild(e.content);break;case"middle":t._options._withWelcomePanelToggleButton&&t._mainPanel.firstElementChild&&!t._mainPanel.firstElementChild.classList.contains("triptych-wp-toggle-btn")?t._mainPanel.removeChild(t._mainPanel.firstElementChild):t._mainPanel.firstChild&&t._mainPanel.removeChild(t._mainPanel.firstChild),e&&e.content&&t._mainPanel.appendChild(e.content);break;case"left":t._leftPanelContent.firstChild&&t._leftPanelContent.removeChild(t._leftPanelContent.firstChild),e&&e.content&&t._leftPanelContent.appendChild(e.content)}})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-add-check-before-close"},function(e){"left"===e.side?t._checkCallbacksLeft[e.id]=e.callback:"right"===e.side&&(t._checkCallbacksRight[e.id]=e.callback)})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-remove-check-before-close"},function(e){"left"===e.side&&t._checkCallbacksLeft[e.id]?delete t._checkCallbacksLeft[e.id]:"right"===e.side&&t._checkCallbacksRight[e.id]&&delete t._checkCallbacksRight[e.id]})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-make-modal"},function(e){t._makeModal(e)})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-unmake-modal"},function(e){t._unmakeModal(e)})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-activate-transition"},function(){t._activateTransition(),t._options.withtransition=!0})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-deactivate-transition"},function(){t._deactivateTransition(),t._options.withtransition=!1}))},h.prototype._subscribeToDomEvents=function(){var t=this;var e,i,s,n;void 0===this._resizeHandler&&(this._resizeHandler=(e=function(){t._container&&(t._modelEvents.publish({event:"resizing-triptych-container",data:t._container.offsetWidth}),0!==t._container.offsetWidth&&(t._container.offsetWidth<=t.__mobileBreakpoint&&(t._isMobileMode||(t._modelEvents.publish({event:"triptych-entering-mobile"}),t._isRightOpen&&(t._right_closed_from_triptych=!0)),t._isMobileMode=!0),t._container.offsetWidth>t.__mobileBreakpoint&&(t._isMobileMode&&(t._modelEvents.publish({event:"triptych-leaving-mobile"}),t._right_closed_from_triptych&&t._showPanel("right")),t._isMobileMode=!1,t._right_closed_from_triptych=!1),t._refreshContainerLeftSize(),t._refreshContainerRightSize()))},i=150,function(){var t=this,o=arguments,l=s&&!n;clearTimeout(n),n=setTimeout(function(){n=null,s||e.apply(t,o)},i),l&&e.apply(t,o)})),window.addEventListener("resize",this._resizeHandler)},h.prototype.destroy=function(){void 0!==this._resizeHandler&&(window.removeEventListener("resize",this._resizeHandler),this._resizeHandler=void 0),this._container=null;var t=0,e=this._listSubscription.length;for(t=0;t<e;t++)this._modelEvents.unsubscribe(this._listSubscription[t]);var i=Object.keys(this),s=i.indexOf("_modelEvents");for(s>-1&&i.splice(s,1),t=0;t<i.length;t++)this[i[t]]=void 0},h.prototype.inject=function(t){t.appendChild(this._container)},h.prototype.getLeftPanelContainer=function(){return this._leftPanel},h.prototype.getRightPanelContainer=function(){return this._rightPanel},h.prototype.getLeftPanelContent=function(){return this._leftPanelContent},h.prototype.getRightPanelContent=function(){return this._rightPanelContent},h.prototype.getMainPanelContainer=function(){return this._mainPanel},h.prototype._getLeftWidth=function(){return this._currentLeft},h.prototype._checkBeforeClosingLeft=function(){var t=Object.keys(this._checkCallbacksLeft),e=t.length,i=0;for(i=0;i<e;i++)if(!this._checkCallbacksLeft[t[i]]())return!1;return!0},h.prototype._checkBeforeClosingRight=function(){var t=Object.keys(this._checkCallbacksRight),e=t.length,i=0;for(i=0;i<e;i++)if(!this._checkCallbacksRight[t[i]]())return!1;return!0},h.prototype._togglePanel=function(t){"left"===t?this._isLeftOpen?this._hidePanel("left"):this._showPanel("left"):this._isRightOpen?this._hidePanel("right"):this._showPanel("right")},h.prototype._showPanel=function(t){var e=this;"left"===t?(this._isLeftOpen=!0,this._leftPanel.classList.add("open"),this._leftPanel.classList.remove("close"),this._mainPanel.classList.add("left-open"),this._refreshContainerLeftSize(),this._refreshContainerRightSize(),this._options.withCloseInLeftPanel&&this._leftPanel.querySelector(".triptych-close-left").classList.remove("displaynone")):(this._options.withCloseInRightPanel&&this._rightPanel.querySelector(".triptych-close-right").classList.remove("displaynone"),this._isRightOpen=!0,this._rightPanel.classList.add("open"),this._rightPanel.classList.remove("close"),this._mainPanel.classList.add("right-open"),this._refreshContainerRightSize(),this._refreshContainerLeftSize()),e._modelEvents.publish({event:"triptych-panel-show-started",data:t}),this._options.withtransition?setTimeout(function(){e._modelEvents.publish({event:"triptych-panel-visible",data:t}),"left"===t&&e._leftPanel?e._modelEvents.publish({event:"triptych-panel-visible-size",data:{side:"left",width:e._leftPanel.offsetWidth}}):"right"===t&&e._rightPanel&&e._modelEvents.publish({event:"triptych-panel-visible-size",data:{side:"right",width:e._rightPanel.offsetWidth}})},400):(e._modelEvents.publish({event:"triptych-panel-visible",data:t}),"left"===t?e._modelEvents.publish({event:"triptych-panel-visible-size",data:{side:"left",width:e._leftPanel.offsetWidth}}):"right"===t&&e._modelEvents.publish({event:"triptych-panel-visible-size",data:{side:"right",width:e._rightPanel.offsetWidth}}))},h.prototype._hidePanel=function(t,e){var i=this;if(e||(this._right_closed_from_triptych=!1),"left"===t){if(!i._checkBeforeClosingLeft())return UWA.log("preventing from closing left"),!1;this._isLeftOpen=!1,this._leftPanel.classList.remove("open"),this._leftPanel.classList.add("close"),this._mainPanel.classList.remove("left-open"),this._refreshContainerLeftSize(),this._options.withCloseInLeftPanel&&this._leftPanel.querySelector(".triptych-close-left").classList.add("displaynone")}else{if(!i._checkBeforeClosingRight())return UWA.log("preventing from closing right"),!1;this._isRightOpen=!1,this._options.withCloseInRightPanel&&this._rightPanel.querySelector(".triptych-close-right").classList.add("displaynone"),this._rightPanel.classList.remove("open"),this._rightPanel.classList.add("close"),this._mainPanel.classList.remove("right-open"),this._refreshContainerRightSize()}i._modelEvents.publish({event:"triptych-panel-hide-started",data:t}),this._options.withtransition?setTimeout(function(){i._modelEvents.publish({event:"triptych-panel-hidden",data:t}),i._unmakeModal()},400):(i._modelEvents.publish({event:"triptych-panel-hidden",data:t}),i._unmakeModal())},h.prototype._initDivs=function(t,e,s,h,r){this._options._close=n._close,this._options._withWelcomePanelToggleButton=!!r,this._options._leftPanelOpen=this._isLeftOpen;var a=l(this._options);this._container=t.container?t.container:document.createElement("div"),this._container.innerHTML=a,t.container||(this._container=this._container.querySelector(".triptych-container")),this._mainPanel=this._container.querySelector(".triptych-main"),this._currentLeft=this._options.left.originalSize,this._currentRight=this._options.right.originalSize,this._leftPanel=this._container.querySelector(".triptych-left"),this._leftPanelContent=this._container.querySelector(".triptych-left-content"),this._rightPanel=this._container.querySelector(".triptych-right"),this._rightPanelContent=this._container.querySelector(".triptych-right-content"),this._leftResizer=this._container.querySelector(".triptych-resizer.left"),this._rightResizer=this._container.querySelector(".triptych-resizer.right"),this._overlayLeft=this._container.querySelector(".overlay-left"),this._overlayRight=this._container.querySelector(".overlay-right");var c=this;if(this._overlayLeft.addEventListener("click",function(){o&&UWA.log("overlay left clicked"),c._hidePanel("left"),c._unmakeModal(),c._modelEvents.publish({event:"triptych-clicked-overlay",data:"left"})}),this._overlayRight.addEventListener("click",function(){o&&UWA.log("overlay right clicked"),c._hidePanel("right"),c._unmakeModal(),c._modelEvents.publish({event:"triptych-clicked-overlay",data:"right"})}),this._refreshContainerLeftSize(),this._options._withWelcomePanelToggleButton){r.parentContainer=e||c._leftPanelContent,r.noborder=!0,c.welcomePanel=new i(r),c.welcomePanel.render();var _=this._mainPanel.querySelector(".triptych-wp-toggle-btn"),d=_.querySelector(".fonticon");c.welcomePanel.collapsed?(d.classList.remove("fonticon-expand-left"),d.classList.add("fonticon-expand-right")):(d.classList.remove("fonticon-expand-right"),d.classList.add("fonticon-expand-left")),_.addEventListener("click",function(){c._isLeftOpen?c.welcomePanel&&(c.welcomePanel.collapsed?(c._currentLeft=300,c._options.left.minWidth=300,c._options.left.originalSize=300,c._refreshContainerLeftSize(),c.welcomePanel.show(),d.classList.remove("fonticon-expand-right"),d.classList.add("fonticon-expand-left")):(c._currentLeft=40,c._options.left.minWidth=40,c._options.left.originalSize=40,c._refreshContainerLeftSize(),c.welcomePanel.hide(),d.classList.remove("fonticon-expand-left"),d.classList.add("fonticon-expand-right")),_.classList.toggle("open")):c._togglePanel("left")}),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-entering-mobile"},function(){c.welcomePanel&&c.welcomePanel.show()})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-leaving-mobile"},function(){c.welcomePanel&&(40===c._currentLeft?(c.welcomePanel.hide(),d.classList.remove("fonticon-expand-left"),d.classList.add("fonticon-expand-right")):(c.welcomePanel.show(),d.classList.remove("fonticon-expand-right"),d.classList.add("fonticon-expand-left"))),c._isLeftOpen||c._togglePanel("left")})),this._listSubscription.push(this._modelEvents.subscribe({event:"triptych-clicked-overlay"},function(t){"left"===t&&(d.classList.remove("fonticon-expand-left"),d.classList.add("fonticon-expand-right"))}))}e&&this._leftPanelContent.appendChild(e),h&&this._rightPanelContent.appendChild(h),this._refreshContainerRightSize(),s&&this._mainPanel.appendChild(s),this._leftPanel.classList.add("close"),this._isLeftOpen&&(this._leftPanel.classList.add("open"),this._leftPanel.classList.remove("close"),this._mainPanel.classList.add("left-open"),this._options.withCloseInLeftPanel&&this._leftPanel.querySelector(".triptych-close-left").classList.remove("displaynone")),this._rightPanel.classList.add("close"),this._isRightOpen&&(this._rightPanel.classList.add("open"),this._rightPanel.classList.remove("close"),this._mainPanel.classList.add("right-open"),this._options.withCloseInRightPanel&&this._rightPanel.querySelector(".triptych-close-right").classList.remove("displaynone"))},h.prototype._activateTransition=function(){this._leftPanel.classList.add("withtransition"),this._rightPanel.classList.add("withtransition"),this._mainPanel.classList.add("withtransition")},h.prototype._deactivateTransition=function(){this._leftPanel.classList.remove("withtransition"),this._rightPanel.classList.remove("withtransition"),this._mainPanel.classList.remove("withtransition")},h.prototype._initResizer=function(){var t=this,e=t._rightPanel.querySelector(".triptych-close-right");e&&e.addEventListener("click",function(){t._modelEvents.publish({event:"triptych-hide-panel",data:"right"})});var i=t._leftPanel.querySelector(".triptych-close-left");i&&i.addEventListener("click",function(){t._modelEvents.publish({event:"triptych-hide-panel",data:"left"})});var s=function(){t._options.withtransition&&t._deactivateTransition()},n=function(){t._options.withtransition&&t._activateTransition()},l=function(t){return t.stopPropagation&&t.stopPropagation(),t.preventDefault&&t.preventDefault(),t.cancelBubble=!0,t.returnValue=!1,!1},h=null,r=null,a=null;if(this._options.left.resizable){var c=!1,_=function(e){s(),h=t._currentLeft,a=e.clientX?e.clientX:e.touches&&e.touches[0]?e.touches[0].clientX:0,c=!0,l(e),"touchstart"===e.type?(document.addEventListener("touchmove",d),document.addEventListener("touchend",p)):"mousedown"===e.type&&(document.addEventListener("mousemove",d),document.addEventListener("mouseup",p))},d=function(e){var i=e.clientX?e.clientX:e.touches&&e.touches[0]?e.touches[0].clientX:0;c&&(t._currentLeft=h+(i-a),t._refreshContainerLeftSize())},p=function(e){n(),o&&UWA.log("stop resizeleft"),c=!1,h=t._currentLeft,"touchend"===e.type?(document.removeEventListener("touchmove",d),document.removeEventListener("touchend",p)):"mouseup"===e.type&&(document.removeEventListener("mousemove",d),document.removeEventListener("mouseup",p))};t._leftResizer.addEventListener("mousedown",_),t._leftResizer.addEventListener("touchstart",_)}if(this._options.right.resizable){var f=!1,u=function(e){s(),r=t._currentRight,a=e.clientX?e.clientX:e.touches&&e.touches[0]?e.touches[0].clientX:0,f=!0,l(e),"touchstart"===e.type?(document.addEventListener("touchmove",g),document.addEventListener("touchend",m)):"mousedown"===e.type&&(document.addEventListener("mousemove",g),document.addEventListener("mouseup",m))},g=function(e){o&&UWA.log("resize right)");var i=e.clientX?e.clientX:e.touches&&e.touches[0]?e.touches[0].clientX:0;f&&(t._currentRight=r+(a-i),t._refreshContainerRightSize())},m=function(e){n(),f=!1,o&&UWA.log("stop resizeright"),r=t._currentRight,"touchend"===e.type?(document.removeEventListener("touchmove",g),document.removeEventListener("touchend",m)):"mouseup"===e.type&&(document.removeEventListener("mousemove",g),document.removeEventListener("mouseup",m))};t._rightResizer.addEventListener("mousedown",u),t._rightResizer.addEventListener("touchstart",u)}},h.prototype._refreshContainerLeftSize=function(){var t=this._container.offsetWidth;this._isMobileMode||(this._isMobileMode=this._container.offsetWidth<=this.__mobileBreakpoint);var e=this._overlayedLeft&&t<this.__mobileBreakpoint;this._isRightOpen&&(this._currentLeft=Math.min(t-this._currentRight-150,this._currentLeft)),this._isMobileMode||(this._currentLeft=Math.min(this._currentLeft,t-100)),this._currentLeft=Math.max(this._options.left.minWidth,this._currentLeft),e?(this._leftResizer&&this._leftResizer.classList.add("displaynone"),this._isLeftOpen?(this._overlayLeft.classList.remove("displaynone"),this._isRightOpen&&this._hidePanel("right",!0),this._leftPanel.style.width="90%",this._mainPanel.style.left=0):(this._overlayLeft.classList.add("displaynone"),this._leftPanel.style.width=0,this._mainPanel.style.left=0,this._leftResizer&&this._leftResizer.classList.add("displaynone"))):(this._overlayLeft.classList.add("displaynone"),this._isLeftOpen?(this._leftPanel.style.width=this._currentLeft+"px",this._mainPanel.style.left=this._currentLeft+"px",this._modelEvents.publish({event:"triptych-resized",data:{side:"left",width:this._currentLeft}}),this._leftResizer&&(this._leftResizer.style.left=this._currentLeft-5+"px",this._leftResizer.classList.remove("displaynone"))):(this._leftPanel.style.width=0,this._mainPanel.style.left=0,this._leftResizer&&this._leftResizer.classList.add("displaynone")))},h.prototype._refreshContainerRightSize=function(){var t=this._container.offsetWidth,e=this._overlayedRight&&t<this.__mobileBreakpoint;this._currentRight<0&&(this._currentRight=this._options.right.originalSize),this._currentRight=Math.max(this._options.right.minWidth,this._currentRight),this._isMobileMode||(this._currentRight=Math.min(this._currentRight,this._container.offsetWidth-100)),this._isLeftOpen&&(this._currentRight=Math.min(t-this._currentLeft-150,this._currentRight)),e?(this._rightResizer&&this._rightResizer.classList.add("displaynone"),this._isRightOpen?(this._overlayRight.classList.remove("displaynone"),this._isLeftOpen&&this._hidePanel("left"),this._rightPanel.style.width="90%",this._rightPanel.style.right=0,this._mainPanel.style.left=0):(this._overlayRight.classList.add("displaynone"),this._rightPanel.style.width=0,this._mainPanel.style.right=0,this._rightResizer&&this._rightResizer.classList.add("displaynone"))):(this._overlayRight.classList.add("displaynone"),this._isRightOpen?(this._rightPanel.style.width=this._currentRight+"px",this._mainPanel.style.right=this._currentRight+"px",this._modelEvents.publish({event:"triptych-resized",data:{side:"right",width:this._currentRight}}),this._rightResizer&&(this._rightResizer.style.right=this._currentRight-5+"px",this._rightResizer.classList.remove("displaynone"))):(this._rightPanel.style.width=0,this._mainPanel.style.right=0,this._rightResizer&&this._rightResizer.classList.add("displaynone")))},h.prototype._makeModal=function(t){switch(t){case"left":this._leftPanel.classList.add("triptych-modal"),this._leftPanel.classList.add("triptych-modal-main"),this._rightPanel.classList.add("triptych-modal"),this._mainPanel.classList.add("triptych-modal"),this._rightPanel.classList.add("triptych-modal-overlayed"),this._mainPanel.classList.add("triptych-modal-overlayed"),this._modelEvents.publish({event:"triptych-made-modal",data:t});break;case"right":this._leftPanel.classList.add("triptych-modal"),this._rightPanel.classList.add("triptych-modal-main"),this._rightPanel.classList.add("triptych-modal"),this._mainPanel.classList.add("triptych-modal"),this._leftPanel.classList.add("triptych-modal-overlayed"),this._mainPanel.classList.add("triptych-modal-overlayed"),this._modelEvents.publish({event:"triptych-made-modal",data:t});break;default:o&&UWA.log("Triptych.prototype._makeModal called with strange data..")}return 0},h.prototype._unmakeModal=function(){this._leftPanel.classList.remove("triptych-modal"),this._leftPanel.classList.remove("triptych-modal-main"),this._rightPanel.classList.remove("triptych-modal-main"),this._rightPanel.classList.remove("triptych-modal"),this._mainPanel.classList.remove("triptych-modal"),this._rightPanel.classList.remove("triptych-modal-overlayed"),this._leftPanel.classList.remove("triptych-modal-overlayed"),this._mainPanel.classList.remove("triptych-modal-overlayed"),this._modelEvents.publish({event:"triptych-made-unmodal"})},h});