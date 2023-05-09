define("DS/VCXWebModifiablesImpl/VCXModifiableOccurrence",["DS/Visualization/PathElement","DS/Visualization/ThreeJS_DS","DS/Visualization/SceneGraphUtils","DS/VCXWebModifiables/VCXModifiable","DS/VCXWebComponents/VCXWebComponentOccurrence","DS/VCXWebProperties/VCXColor","DS/VCXWebProperties/VCXPropertySet","DS/VCXWebProperties/VCXProperty","DS/VCXWebProperties/VCXPropertyGroup","DS/VCXWebProperties/VCXPropertyInfo","DS/VCXWebProperties/VCXPropertyInfoEnum","DS/VCXWebProperties/VCXPropertyValueFrame","DS/VCXWebProperties/VCXPropertyValueLocation","DS/VCXWebProperties/VCXPropertyValueColor","DS/VCXWebProperties/VCXPropertyValueFloat","DS/VCXWebProperties/VCXPropertyValueEnum","DS/VCXWebProperties/VCXPropertyValueBoolean","DS/VCXWebProperties/VCXPropertyValueString","DS/VCXWebTracks/VCXInterpolator","DS/VCXWebTracks/VCXInterpolatorConstant","DS/VCXWebTracks/VCXInterpolatorLinear","DS/VCXWebTracks/VCXInterpolatorCubic","i18n!DS/VCXWebModifiables/assets/nls/VCXWebModifiables"],function(e,t,r,o,i,n,a,s,l,p,c,u,V,d,y,f,G,b,C,S,P,h,X){"use strict";var M=o.extend({init:function(){this._parent(),this._interpolationType=0,M.prototype._interpolatorConstant||(M.prototype._interpolatorConstant=new S,M.prototype._interpolatorLinear=new P,M.prototype._interpolatorCubic=new h)},OnChangeProperty:function(e,t,r){if(r||(r={}),r.fromChildClass);else if(this.OnChangePropertyBegin(e,t))return!0;switch(e){case"Actor.Name":return(o=this.GetObject()).name=t.Value,!0;case"Actor.Position":(o=this.GetObject()).matrix=t.GetValue()[0].GetValue();return o.pivot=t.GetValue()[1].GetValue(),!0;case"Actor.Color.Defined":return(o=this.GetObject()).localColorIsDefined=t.GetValue(),!0;case"Actor.Color":var o=this.GetObject(),i=t.GetValue();return null===i&&console.log("occurrence color problem"),o.localColor=i.Clone(),!0;case"Actor.Opacity":return(o=this.GetObject()).alpha=t.GetValue(),!0;case"Actor.Opacity.Defined":return(o=this.GetObject()).localOpacityIsDefined=t.GetValue(),!0;case"Actor.Visibility.Opacity":return(o=this.GetObject()).visibilityAlpha=t.GetValue(),!0;case"Actor.Interpolation.Type":var n=t.GetValue();return void 0!==n&&(this._interpolationType=n),!0;default:return r.fromChildClass=!0,this._parent(e,t,r)}return!1},OnChangePropertiesEnd:function(){var e,t=this.GetObject();t._dirtyMask&&!t.IsDressup&&((e=t._experienceBase.getManager("VCXContextManager"))&&e.useTraverseGraph&&(e.traverseGraphMustBePerformed=!0),t.getDirtyFlag(i.FDirty.Color)&&(t._localColor||(t._localColor=this.GetOriginalColor())),t.getDirtyFlag(i.FDirty.Alpha)&&(null!==t._alpha?t._localAlpha=t._visibilityAlpha*t._alpha:t._localAlpha=1*t._visibilityAlpha))},GetInterpolator:function(e){return"Actor.Position"===e&&1===this._interpolationType?this._interpolatorCubic:null},GetProperty:function(e){var t=null;if("Actor.Name"===e){var r="",o=(i=this.GetObject()).GetName&&i.GetName()?i.GetName():i.name;"string"==typeof o&&""!==o&&(r=o),(W=new b).SetValue(r),t=new s(new p("Actor.Name",p.EBehaviorType.Singleton,!0,X),W)}else if("Actor.Position"===e){var i,n=null;if(!(n=(i=this.GetObject()).matrix)&&this.QueryInterface("W3AISGNodeHolder").getPathElement()){i._experienceBase.getManager("VCXContextManager");n=i.getPosition()}if(n){var a=new V,l=new u;l.SetValue(n),a.SetFrame(l);var S=new u;i.pivot?S.SetValue(i.pivot):S.SetValue(n),a.SetPivot(S),t=new s(new p(e,0,!0,X),a)}else console.log("[VCXModifiableOccurence::GetProperty] Error matrix null")}else if("Actor.Color"===e){var P=this.GetObject().localColor;null===P&&(P=this.GetOriginalColor());var h=new d;h.SetValue(P),null!==this.GetObject().localColorIsDefined&&(h._bDefined=this.GetObject().localColorIsDefined),(_=new p(e,0,!0,X)).SetGroupId("Group.Appearance"),t=new s(_,h)}else if("Actor.Color.Defined"===e){var M=new G,D=this.GetObject().localColorIsDefined;null!==D?M.SetValue(D):M.SetValue(!1),(_=new p(e,0,!0,X)).SetVisible(!0),t=new s(_,M)}else if("Actor.Opacity"===e){null===(I=this.GetObject().alpha)&&(I=1),(g=new y).SetValue(I),(_=new p(e,0,!0,X)).SetBounds(0,1,.1),t=new s(_,g)}else if("Actor.Opacity.Defined"===e){M=new G;var O=this.GetObject().localOpacityIsDefined;null!==O?M.SetValue(O):M.SetValue(!1),(_=new p(e,0,!0,X)).SetVisible(!0),t=new s(_,M)}else if("Actor.Interpolation.Type"===e){var A=new f;A.SetValue(this._interpolationType);var w=new c(e,1,!0,X);w.SetGroupId("Group.Animation");var m=this._generateMapChoiceFromEnumAndId(C.EInterpolationType,"Interpolation",["Custom","Smooth"]);w.SetMapChoiceEnum(m),t=new s(w,A)}else if("Actor.Visibility.Opacity"===e){var g,I=1,v=this.GetObject().visibilityAlpha;null!==v&&(I=v),(g=new y).SetValue(I),(_=new p(e,0,!1,X)).SetBounds(0,1,.1),_.SetVisible(!1),t=new s(_,g)}else if("Actor.3DReference"===e){var W,_,j="",T=this.QueryInterface("W3AISGNodeHolder").getPathElement().getLastElement();if(T){var E=T.getChildren();if(E.length>0)"Reference3D"===E[0].productType&&(j=E[0].name)}if(!j){var x=this.GetProperty("Modifiable.HasChildren");if(x&&!x.GetPropertyValue().GetValue()){var B=this.GetProperty("Meta.ReferenceName");B&&(j=B.GetPropertyValue().GetValue())}}(W=new b).SetValue(j),(_=new p(e,1,!1,X)).SetGroupId("Group.Introspection"),t=new s(_,W)}else t=this._parent(e);return t},GetProperties:function(){var e=this._parent();return e.AddOrModifyProperty(this.GetProperty("Actor.Name")),e.AddOrModifyProperty(this.GetProperty("Actor.Position")),e.AddOrModifyProperty(this.GetProperty("Actor.Color")),e.AddOrModifyProperty(this.GetProperty("Actor.Color.Defined")),e.AddOrModifyProperty(this.GetProperty("Actor.Opacity")),e.AddOrModifyProperty(this.GetProperty("Actor.Opacity.Defined")),e.AddOrModifyProperty(this.GetProperty("Actor.Visibility.Opacity")),e.AddOrModifyProperty(this.GetProperty("Actor.Hyperlink")),e.AddOrModifyProperty(this.GetProperty("Actor.Interpolation.Type")),this.GetObject()._experienceBase.ModifiablesAdvancedProperties&&e.AddOrModifyProperty(this.GetProperty("Actor.3DReference")),e},RequestModification:function(e){if(e){var t=e.GetPropertyInfo().GetPropertyName();if("Actor.Color"===t){if((r=this.GetProperty("Actor.Color.Defined"))&&!1===r.GetPropertyValue().Value)(o=new G).Value=!0,r.SetPropertyValue(o);return(i=new a).AddOrModifyProperty(e),i.AddOrModifyProperty(r),i}if("Actor.Opacity"===t){var r,o,i;if((r=this.GetProperty("Actor.Opacity.Defined"))&&!1===r.GetPropertyValue().Value)(o=new G).Value=!0,r.SetPropertyValue(o);return(i=new a).AddOrModifyProperty(e),i.AddOrModifyProperty(r),i}}return this._parent(e)},GetOriginalOpacity:function(){var e=this.GetObject().BuildGraphicPropertiesStructure({getMaterial:!0,getOpacity:!0}),t=e&&e[0]&&e[0].cad&&e[0].cad.Material;return t?t.opacity:e&&e[0]&&e[0].cad&&e[0].cad.Opacity},GetOriginalColor:function(){var e=this.GetObject().BuildGraphicPropertiesStructure({getMaterial:!0,getColor:!0}),t=e&&e[0]&&e[0].cad&&e[0].cad.Material,r=new n;if(t)t.color&&r.SetRGB(t.color.r,t.color.g,t.color.b),t._color&&r.SetRGB(t._color.r,t._color.g,t._color.b);else{var o=e&&e[0]&&e[0].cad&&e[0].cad.Color;o&&r.SetRGB(o.r,o.g,o.b)}return r},GetOriginalColor2:function(){var e=this.GetOriginalMaterial(),t=new n;return e&&(e.color&&t.SetRGB(e.color.r,e.color.g,e.color.b),e._color&&t.SetRGB(e._color.r,e._color.g,e._color.b)),t},GetOriginalMaterial:function(){var e=this.GetObject().BuildGraphicPropertiesStructure({getMaterial:!0});return e&&e[0]&&e[0].cad&&e[0].cad.Material},GetOriginalMaterial2:function(){var t=this.QueryInterface("W3AISGNodeHolder"),o=t&&t.getPathElement();if(!o||void 0===o||!o.externalPath.length)return null;for(var i=new e,n=0;n<o.externalPath.length;++n)i.addElement(o.externalPath[n]);for(var a=i.getLastElement();a&&(!(a=a.children.length?a.children[0]:null)||(i.addElement(a),"Mesh3D"!==a.getNodeType())););var s=this.GetObject()._experienceBase.getManager("VCXContextManager").getMainViewer(),l=r.getMaterialsFromPath(s,i);return l&&l.materials&&l.materials.length?l.materials[0]:null}});return M}),define("DS/VCXWebModifiablesImpl/VCXModifiableCommandManager",["DS/VCXWebModifiables/VCXModifiable","DS/VCXWebProperties/VCXPropertySet","DS/VCXWebProperties/VCXProperty","DS/VCXWebProperties/VCXPropertyInfo","DS/VCXWebProperties/VCXPropertyValueInt"],function(e,t,r,o,i){"use strict";return e.extend({init:function(){this._parent()},GetStackSize:function(){return this.GetObject().GetStackMaxSize()},OnChangeProperty:function(e,t){return"CommandManager.maxSize"!==e?(console.log("property not found!"),!1):(this.GetObject().SetStackMaxSize(t.GetValue()),!0)},GetProperty:function(e){var t=null;if("CommandManager.maxSize"===e){var n=new i;n.SetValue(this.GetStackSize()),t=new r(new o(e,2),n)}return t},GetProperties:function(){var e=new t;return e.AddOrModifyProperty(this.GetProperty("CommandManager.maxSize")),e}})}),define("DS/VCXWebModifiablesImpl/VCXModifiableScenario",["DS/CoreEvents/Events","DS/VCXWebModifiables/VCXModifiable","DS/VCXWebProperties/VCXPropertySet","DS/VCXWebProperties/VCXPropertyGroupSet","DS/VCXWebProperties/VCXPropertyValueString","DS/VCXWebProperties/VCXPropertyValueEnum","DS/VCXWebProperties/VCXProperty","DS/VCXWebProperties/VCXPropertyInfo","DS/VCXWebProperties/VCXPropertyInfoEnum","DS/VCXWebProperties/VCXPropertyValueDocId","DS/VCXWebProperties/VCXPropertyValueBoolean","DS/VCXWebProperties/VCXPropertyValueMap","DS/VCXWebProperties/VCXPropertyValueLink","DS/VCXWebProperties/VCXPropertyValueInt","DS/VCXWebProperties/VCXPropertyValueFloat","DS/WebRBTree/WebRBIterator","i18n!DS/VCXWebModifiables/assets/nls/VCXWebModifiables"],function(e,t,r,o,i,n,a,s,l,p,c,u,V,d,y,f,G){"use strict";return t.extend({init:function(){this._parent()},OnChangeProperty:function(e,t){if("Actor.Name"===e)this.GetObject().SetName(t.GetValue());else if("Actor.DocID"===e)this.GetObject().SetDocId(t.GetValue());else if("Scenario.Partial"===e)this.GetObject().SetApplyNeutralAtGoToScenario(!t.GetValue());else if("Scenario.ZoomFitAll"===e)this.GetObject().SetZoomFitAll(t.GetValue());else if("Actor.Scenario.Type"===e)this.GetObject().SetScenarioType(t.GetValue());else if("Actor.Scenario.Markers"===e);else if("Scenario.bIsLoop"===e)this.GetObject().SetIsLoop(t.GetValue());else if("Scenario.bIsBounce"===e)this.GetObject().SetIsBounce(t.GetValue());else if("Scenario.bIsReverse"===e)this.GetObject().SetIsReverse(t.GetValue());else if("Scenario.Speed"===e)this.GetObject().SetSpeed(t.GetValue());else{if("Scenario.Visibility.Mode"!==e){var r=this._parent(e,t);return r||console.log("property not found : '"+e+"'"),r}this.GetObject().GetVisibility().SetMode(t.GetValue())}return!0},GetProperty:function(e){var t=null;if("Actor.Name"===e){var r=new i;r.SetValue(this.GetObject().GetName()),t=new a(new s(e,2,!0,G),r)}else if("Actor.DocID"===e){var o=new p;o.SetValue(this.GetObject().GetDocId()),(t=new a(new s(e,2,!0,G),o)).GetPropertyInfo().SetVisible(!1)}else if("Scenario.Partial"===e){(P=new c).SetValue(!this.GetObject().GetApplyNeutralAtGoToScenario()),t=new a(new s(e,2,!0,G),P)}else if("Scenario.ZoomFitAll"===e){(P=new c).SetValue(this.GetObject().GetZoomFitAll());var y=new s(e,2,!0,G);y.SetVisible(!1),t=new a(y,P)}else if("Scenario.bIsLoop"===e){(P=new c).SetValue(this.GetObject().GetIsLoop());var b=new s(e,2,!0,G);b.SetVisible(!1),t=new a(b,P)}else if("Scenario.bIsBounce"===e){(P=new c).SetValue(this.GetObject().GetIsBounce());var C=new s(e,2,!0,G);C.SetVisible(!1),t=new a(C,P)}else if("Scenario.bIsReverse"===e){(P=new c).SetValue(this.GetObject().GetIsReverse());var S=new s(e,2,!0,G);S.SetVisible(!1),t=new a(S,P)}else if("Scenario.Speed"===e){var P;(P=new d).SetValue(this.GetObject().GetSpeed()),t=new a(new s(e,2,!0,G),P)}else if("Actor.Scenario.Type"===e){(v=new n).SetValue(this.GetObject().GetScenarioType());var h={0:"scenario.type.scenario",1:"scenario.type.layer",2:"scenario.type.CADViews",3:"scenario.type.CADCaptures"};(W=new l(e,2,!0,G)).SetMapChoiceEnum(h),W.SetVisible(!1),t=new a(W,v)}else if("Actor.Scenario.Markers"===e){var X=new u,M=this.GetObject().GetTrackMarkers();if(M)for(var D=0,O=M._treekeys,A=new f(O),w=A.next();null!==w;){var m=w.GetValue(),g=new V;g.SetValue(m),X.SetValueAtIndex(D,g),D++,w=A.next()}var I=new s(e,2,!0,G);I.SetVisible(!1),t=new a(I,X)}else if("Scenario.Visibility.Mode"===e){var v;(v=new n).SetValue(this.GetObject().GetVisibility().GetMode());var W;h={0:"scenario.mode.setboth",1:"scenario.mode.setvisible",2:"scenario.mode.setinvisible"};(W=new l(e,2,!0,G)).SetMapChoiceEnum(h),t=new a(W,v)}else t=this._parent(e);return t},GetProperties:function(){var e=new r;return e.AddOrModifyProperty(this.GetProperty("Actor.Name")),e.AddOrModifyProperty(this.GetProperty("Actor.DocID")),e.AddOrModifyProperty(this.GetProperty("Scenario.Partial")),e.AddOrModifyProperty(this.GetProperty("Scenario.ZoomFitAll")),e.AddOrModifyProperty(this.GetProperty("Actor.Scenario.Type")),e.AddOrModifyProperty(this.GetProperty("Actor.Scenario.Markers")),e.AddOrModifyProperty(this.GetProperty("Scenario.bIsLoop")),e.AddOrModifyProperty(this.GetProperty("Scenario.bIsBounce")),e.AddOrModifyProperty(this.GetProperty("Scenario.bIsReverse")),e.AddOrModifyProperty(this.GetProperty("Scenario.Speed")),e.AddOrModifyProperty(this.GetProperty("Scenario.Visibility.Mode")),e}})}),define("DS/VCXWebModifiablesImpl/VCXModifiableDocManager",["DS/VCXWebModifiables/VCXModifiable","DS/VCXWebProperties/VCXPropertySet","DS/VCXWebProperties/VCXProperty","DS/VCXWebProperties/VCXPropertyInfo","DS/VCXWebProperties/VCXPropertyValueInt"],function(e,t,r,o,i){"use strict";return e.extend({init:function(){this._parent()},GetDefaultImageIdx:function(){return this.GetObject().GetDefaultImageIndex()},OnChangeProperty:function(e,t){return console.log("property not found!"),!1},GetProperty:function(e){return null},GetProperties:function(){return new t}})}),define("DS/VCXWebModifiablesImpl/VCXModifiableCamera",["DS/VCXWebModifiables/VCXModifiable","DS/Visualization/ThreeJS_DS","DS/VCXWebProperties/VCXProperty","DS/VCXWebProperties/VCXPropertyInfo","DS/VCXWebProperties/VCXPropertyInfoEnum","DS/VCXWebProperties/VCXPropertySet","DS/VCXWebProperties/VCXPropertyValueEnum","DS/VCXWebProperties/VCXPropertyValueFrame","DS/VCXWebTracks/VCXInterpolator","DS/VCXWebTracks/VCXInterpolatorCubic","i18n!DS/VCXWebModifiables/assets/nls/VCXWebModifiables"],function(e,t,r,o,i,n,a,s,l,p,c){"use strict";return e.extend({init:function(){this._parent(),this._interpolationType=l.EInterpolationType.Linear,this._interpolatorCubic=new p},OnChangeProperty:function(e,r){var o=this._parent(e,r);if("Viewpoint.Position"===e){var i=r.Clone().GetValue();if(null!=i){this._currentMatrix=i;var n=i.decompose(),a=n[0],s=n[1];this.GetObject()._experienceBase.getManager("VCXContextManager").getMainViewer().getViewpoints()[0].moveTo({eyePosition:a,upDirection:new t.Vector3(0,1,0).applyQuaternion(s),sightDirection:new t.Vector3(0,0,-1).applyQuaternion(s),duration:0})}o=!0}else if("Viewpoint.InterpolationType"===e){var l=r.GetValue();"undefined"!=typeof bInter&&(this._interpolationType=l),o=!0}return o},GetInterpolator:function(e){return"Viewpoint.Position"===e&&this._interpolationType===l.EInterpolationType.Cubic?this._interpolatorCubic:null},GetProperty:function(e){var n=this.GetObject()._experienceBase.getManager("VCXContextManager").getMainViewer().getViewpoints()[0],l=null;if("Viewpoint.Position"===e){var p=n.getControl(),u=new t.Vector3(0,0,1);u.applyQuaternion(p.orientation),u.setLength(p.distanceToTarget);var V=(new t.Vector3).addVectors(p.target,u),d=new t.Vector3(1,1,1),y=new t.Matrix4;y.compose(V,p.orientation,d);var f=new s;f.SetValue(y),l=new r(new o(e,0,!0,c),f)}else if("Viewpoint.InterpolationType"===e){var G=new a;G.SetValue(this._interpolationType),l=new r(new i(e,0,!0,c),G)}return l},GetProperties:function(){var e=new n;return e.AddOrModifyProperty(this.GetProperty("Viewpoint.Position")),e.AddOrModifyProperty(this.GetProperty("Viewpoint.InterpolationType")),e}})}),define("DS/VCXWebModifiablesImpl/VCXModifiableViewOccurrence",["DS/VCXWebModifiables/VCXModifiableView","DS/VCXWebProperties/VCXPropertyGroup","i18n!DS/VCXWebModifiables/assets/nls/VCXWebModifiables"],function(e,t,r){"use strict";return e.extend({init:function(){this._parent()},getPropertyGroups:function(e){e||(e={}),this._parent(e);var o=this.getGroupPropertiesOrders();return e["Group.Essentials"]||(e["Group.Essentials"]=new t({groupID:"Group.Essentials"}),e["Group.Essentials"].i18nGroup=r),e["Group.Essentials"].setPropertiesOrder(o["Group.Essentials"]),e["Group.Essentials"].priority=0,e["Group.Essentials"].addProperties([{propertyName:"Actor.Name",i18n:r},{propertyName:"Actor.Opacity",i18n:r}]),this.GetObject()._experienceBase.getManager("VCXContextManager").isSupported("Timeline")&&(e["Group.Animation"]||(e["Group.Animation"]=new t({groupID:"Group.Animation"}),e["Group.Animation"].i18nGroup=r),e["Group.Animation"].priority=80,e["Group.Animation"].addProperty({propertyName:"Actor.Interpolation.Type",i18n:r})),this.GetObject()._experienceBase.ModifiablesAdvancedProperties&&(e["Group.Introspection"]||(e["Group.Introspection"]=new t({groupID:"Group.Introspection"}),e["Group.Introspection"].i18nGroup=r),e["Group.Introspection"].setPropertiesOrder(o["Group.Introspection"]),e["Group.Introspection"].priority=90,e["Group.Introspection"].addProperty({propertyName:"Actor.3DReference",i18n:r})),e},getGroupPropertiesOrders:function(){var e=this._parent()||{};return UWA.extend(e,{"Group.Essentials":["Actor.Name","Actor.Position","Modifiable.Style","Actor.Opacity"],"Group.Animation":["Actor.Interpolation.Type"],"Group.Introspection":["Modifiable.ID","Modifiable.Type","Modifiable.Hashing","Modifiable.Level","Modifiable.HasChildren","Actor.3DReference"]}),e},getCollapsedGroupsNames:function(){return this._parent().concat(["Group.Animation","Group.Effect"])}})}),define("DS/VCXWebModifiablesImpl/VCXModifiableViewScenario",["DS/VCXWebModifiables/VCXModifiableView","DS/VCXWebProperties/VCXPropertyGroup","i18n!DS/VCXWebModifiables/assets/nls/VCXWebModifiables"],function(e,t,r){"use strict";return e.extend({init:function(){this._parent()},getPropertyGroups:function(e){e||(e={}),this._parent(e);var o=this.getGroupPropertiesOrders();e["Group.Essentials"]||(e["Group.Essentials"]=new t({groupID:"Group.Essentials"}),e["Group.Essentials"].i18nGroup=r),e["Group.Essentials"].setPropertiesOrder(o["Group.Essentials"]),e["Group.Essentials"].priority=0;var i=[{propertyName:"Actor.Name",i18n:r}];return this.GetObject()._experienceBase.getManager("VCXContextManager").isSupported("Timeline")&&i.push({propertyName:"Scenario.Partial",i18n_VCXWebModifiables:r}),e["Group.Essentials"].addProperties(i),e},getGroupPropertiesOrders:function(){var e=this._parent()||{};return UWA.extend(e,{"Group.Essentials":["Actor.Name","Scenario.Partial"]}),e}})});