define("DS/VCXWebVisibilityImpl/VCXVisibilityHidden",["require","exports","DS/VCXWebVisibility/VCXVisibility"],function(i,e,t){"use strict";return class extends t{init(){this._parent()}GetType(){return"VCXVisibilityHidden"}SetVisibility(i){return!0}GetVisibility(){return-2}}}),define("DS/VCXWebVisibilityImpl/VCXVisibilityOccurrence",["DS/VCXWebVisibility/VCXVisibility","DS/VCXWebVisibility/VCXIVisibility","DS/VCXWebProperties/VCXPropertyValueFloat"],function(i,e,t){"use strict";return i.extend({init:function(){this._parent()},SetVisibility:function(i){this._parent(i);var r=this.GetObject();if(r){var n;if(r.localVisibility=this._visible!==e.EVisState.Hidden,!r._experienceBase.getManager("VCXContextManager").useTraverseGraph)(n=r.QueryInterface("VCXIModifiable"))&&n.OnChangePropertiesEnd();if(!0!==e.lockVisibilityOpacity)if(i!==e.EVisState.Hidden)if(n=r.QueryInterface("VCXIModifiable")){var s=new t;s.SetValue(1),n.OnChangeProperty("Actor.Visibility.Opacity",s),n.OnChangePropertiesEnd()}}}})});