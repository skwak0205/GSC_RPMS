define("DS/DMUReviewWebAppController/DMUReviewWebAppController",["UWA/Class","DS/DMUReviewCommonWebApp/DMUCommonWebAppController"],function(t,e){"use strict";var n=e.extend({init:function(t){this._parent(t,"R3R"),this.setActiveState=async function(t){await this.setControllerActiveState(t)}}}),o=[];return t.singleton({init:function(){},giveController:function(t){if(t&&t.context)for(var e=0;e<o.length;e++)if(o[e].context===t.context)return o[e].controller},getController:function(t){var e;if(t&&t.context&&!(e=this.giveController(t))){var r=new n({ctxViewer:t.context});e=Object.freeze({setActiveState:async function(t){r&&await r.setActiveState(t)},loadReviewAfterRefresh:function(t){r&&r.loadReviewAfterRefresh(t)},getActiveState:function(){if(r)return r.getActiveState()},subscribe:function(t,e){if(r)return r.subscribe(t,e)},unsubscribe:function(t){r&&r.unsubscribe(t)},clearController:function(){r&&(r.clearController(),r=null)},appInitFromTransition:function(t){return r?r.appInitFromTransition(t):Promise.resolve()},loadValidation:function(t){r&&r.loadValidation(t)},refreshContext:function(){r&&r.refreshContext()},disableDMUContextualBar:function(){r&&r.disableDMUContextualBar()},enableDMUContextualBar:function(){r&&r.enableDMUContextualBar()},publish:function(t,e){r&&r.publish(t,e)},setappTransition:function(t){r&&r.setappTransition(t)}}),o.push({context:t.context,controller:e})}return e},unreferenceController:function(t){if(t&&t.context)for(var e=0;e<o.length;e++)if(o[e].context===t.context){t.options&&t.options.appInfo&&"X3DPLAW_AP"===t.options.appInfo&&o[e].controller.setappTransition(!0),o[e].controller.clearController(),o.splice(e,1);break}}})});