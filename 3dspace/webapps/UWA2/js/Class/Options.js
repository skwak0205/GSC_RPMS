define("UWA/Class/Options",["UWA/Core","UWA/Class"],function(t,s){"use strict";var n=s.extend({setOptions:function(s){return s=s||{},this.hasOwnProperty("options")||(this.options=t.clone(this.defaultOptions||this.options||{})),s.events&&this.addEvents&&this.addEvents(s.events),t.extend(this.options,s,!0),this},setOption:function(t,s){var n={};return n[t]=s,this.setOptions(n)},getOption:function(t,s){var n=this.options;return void 0!==n[t]?n[t]:s}});return t.namespace("Options",n,s)});