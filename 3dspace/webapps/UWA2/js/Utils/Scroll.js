define("UWA/Utils/Scroll",["vendors/webcomponents/WeakMap","UWA/Core","UWA/Element","UWA/Fx","UWA/Event"],function(e,t,o,l,n){"use strict";function r(e){return"body"===o.getTagName.call(e)}function i(e,t){var l=o.getStyle.call(e,t?"overflow-"+t:"overflow");return r(e)||"scroll"===l||"auto"===l}function a(e,t){for(;e&&1===e.nodeType&&!i(e,t);)e=e.parentNode;return e}function c(e,t){return a(e&&e.parentNode,t)}function s(e){r(e)&&(e=o.getWindow.call(e));var l=o.getScrolls.call(e);return t.owns(l,"x")&&(l.left=l.x,l.top=l.y),l}function f(e,l){r(e)&&(e=o.getWindow.call(e));var n=s(e);t.is(l.left)||(l.left=n.left),t.is(l.top)||(l.top=n.top),t.is(e,"window")?e.scrollTo(l.left,l.top):(e.scrollTop=l.top,e.scrollLeft=l.left)}var p=l.extend({setSteps:function(e){var t,o={},l={},n=s(this.element);for(t in e)Array.isArray(e[t])?(o[t]=e[t][0],l[t]=e[t][1]):(o[t]=n[t],l[t]=e[t]);this.from=o,this.to=l},onAnimate:function(e,t,o){var l,n={};for(l in t)n[l]=this.compute(t[l],o[l]);f(e,n)}}),u=new e;function m(e,o,l){l=t.extend({onComplete:null},l);var n=u.get(e);n&&(n.stop(),n.dispatchEvent("onComplete")),n=new p(e,{transition:"sineOut",duration:n?100:200,events:{onComplete:function(){u.delete(e),l.onComplete&&l.onComplete()}}}),u.set(e,n),n.start(o)}return t.namespace("Utils/Scroll",{scrollToElement:function(e,l){var n=(l=t.extend({top:!1,bottom:!1,margin:0,scrollable:null,smooth:!1,onComplete:null},l)).scrollable||c(e);if(n){var i=r(n),a=i?o.getSize.call(o.getWindow.call(e)):{width:n.clientWidth,height:n.clientHeight},p=o.getSize.call(e),u=o.getOffsets.call(e),g={top:u.y,left:u.x,bottom:0,right:0},h=s(n);[0,1].forEach(function(e){var t=e?"top":"left",r=e?"bottom":"right",c=e?"height":"width";g[t]-=i?h[t]:o.getOffsets.call(n)[e?"y":"x"],g[t]-=l.margin,g[r]=a[c]-g[t]-p[c]-2*l.margin;var s=g[t]<0||g[r]<0&&g[r]<-g[t],f=g[r]<0;l[r]||!s&&!l[t]?(l[r]||f)&&(h[t]-=g[r]):h[t]+=g[t]}),l.smooth?m(n,h,{onComplete:l.onComplete}):(f(n,h),l.onComplete&&l.onComplete())}},smoothScroll:m,preventParentScroll:function(e,t){var l=function(e,t){var o=a(n.getElement(t)),l=!1;if(this.isInjected(o))l=!0;else{var r=o.scrollHeight,i=o.clientHeight,c=o.scrollTop,s=n.wheelDelta(t);(i!==r||e&&!1===e.onlyScrollable)&&(s<0&&!(c+i<r)||s>0&&!(c>0))&&(l=!0)}l&&n.stop(t)}.bind(e,t);return o.addEvent.call(e,"mousewheel",l),function(){o.removeEvent.call(e,"mousewheel",l)}},getClosestScrollable:a,getParentScrollable:c},t)});