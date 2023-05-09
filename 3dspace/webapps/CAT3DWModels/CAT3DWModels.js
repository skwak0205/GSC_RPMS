define("DS/CAT3DWModels/CAT3DWModelView",["DS/CAT3DWInfra/CAT3DWPreferenceManager","DS/CAT3DWMedia/CAT3DWMediaView","DS/Visualization/Node3D","DS/Visualization/ThreeJS_DS","DS/Visualization/SceneGraphFactory"],function(e,t,i,n,r){"use strict";var a=t.extend({init:function(e,t){this._parent(e,t),this._model3DNode=new i("3dobjet"),this.addChild(this._model3DNode)},initialize:function(t){this._mediaID=t,this._model3DNode.mediaID=t;for(var i=this.getBoundingBox().center(),o=(new n.Matrix4).makeTranslation(-i.x,-i.y,-i.z),l=0;l<this.children.length;l++)this.children[l].applyMatrix(o);if("whiteboard"===e.getPreferenceValue("appMode")){let e=100/this.parents[0].getBoundingSphere().radius,t=(new n.Matrix4).makeScale(e,e,e);this.applyMatrix(t);let i=this.getBoundingBox(),o=r.createCuboidNode({cornerPoint:i.min,firstAxis:new n.Vector3(i.max.x-i.min.x,0,0),secondAxis:new n.Vector3(0,i.max.y-i.min.y,0),thirdAxis:new n.Vector3(0,0,i.max.z-i.min.z),fill:!0,material:a._invisibleMaterial});t=(new n.Matrix4).makeScale(.8,.8,.8),o.applyMatrix(t),this.addChild(o)}this._parent()},getMediaID:function(){return this._mediaID},getModelNode:function(){return this._model3DNode},getCorners:function(){for(var e=this.getCorners3D(),t=[],i=0;i<e.length;i++)t.push(this._viewer.currentViewpoint.project(e[i]));var r=Math.min(t[0].x,t[1].x,t[2].x,t[3].x,t[4].x,t[5].x,t[6].x,t[7].x),a=Math.max(t[0].x,t[1].x,t[2].x,t[3].x,t[4].x,t[5].x,t[6].x,t[7].x),o=Math.min(t[0].y,t[1].y,t[2].y,t[3].y,t[4].y,t[5].y,t[6].y,t[7].y),l=Math.max(t[0].y,t[1].y,t[2].y,t[3].y,t[4].y,t[5].y,t[6].y,t[7].y),s=new n.Plane(new n.Vector3(0,0,1),50),d=this._viewer.currentViewpoint.create3DRayFrom2DPoint(new n.Vector2(r,o));return{topLeft:d.intersectPlane(s),topRight:(d=this._viewer.currentViewpoint.create3DRayFrom2DPoint(new n.Vector2(a,o))).intersectPlane(s),bottomRight:(d=this._viewer.currentViewpoint.create3DRayFrom2DPoint(new n.Vector2(a,l))).intersectPlane(s),bottomLeft:(d=this._viewer.currentViewpoint.create3DRayFrom2DPoint(new n.Vector2(r,l))).intersectPlane(s)}},getCorners3D:function(){var e=this.getMatrixWorld(),t=this.getBoundingBox();return[t.min.clone().applyMatrix4(e),new n.Vector3(t.min.x,t.max.y,t.min.z).applyMatrix4(e),new n.Vector3(t.max.x,t.max.y,t.min.z).applyMatrix4(e),new n.Vector3(t.max.x,t.min.y,t.min.z).applyMatrix4(e),t.max.clone().applyMatrix4(e),new n.Vector3(t.max.x,t.min.y,t.max.z).applyMatrix4(e),new n.Vector3(t.min.x,t.min.y,t.max.z).applyMatrix4(e),new n.Vector3(t.min.x,t.max.y,t.max.z).applyMatrix4(e)]},dispose:function(){this.remove(this._model3DNode),this._model3DNode=null,this._parent()},getCreationData:function(){var e=this._parent();return e.matrix=this.getMatrix(),e},getMoveData:function(){return{matrix:this.getMatrix()}},setMoveData:function(e){this.setMatrix(e.matrix)}});return a._invisibleMaterial=new n.MeshBasicMaterial({color:new n.Color(255),transparent:!0,opacity:0}),a._invisibleMaterial.force=!0,UWA.namespace("DS/CAT3DWModels/CAT3DWModelView",a)}),define("DS/CAT3DWModels/CAT3DWloaderSequencer",["DS/CAT3DWModels/CAT3DWModelView","DS/Visualization/ModelLoader","DS/CAT3DWInfra/CAT3DWMutex","DS/CAT3DWInfra/CAT3DWPreferenceManager"],function(e,t,i,n){"use strict";const r=new i;return UWA.namespace("DS/CAT3DWModels/CAT3DWloaderSequencer",function(){this.LoadModel=async function(e,i){const a=await r.lock();await function(e,i){return new Promise(r=>{var a=i.getView();let o,l;e.callback&&(o=e.callback),a&&a.getModelNode&&(l=a.getModelNode());var s=new t;s.setOnLoadedCallback(function(){a.initialize(),o&&(o(i),r())}.bind(i)),s.setOnErrorCallback(function(e){console.error(e),o&&(o(i),r())}.bind(i)),null!==e.extension?s.loadModel({filename:e.url,proxyurl:"none",format:e.extension,withCredentials:n.getPreferenceValue("UseCredentials"),viewer:e.viewer},l):s.loadModel({filename:e.url,proxyurl:"none",withCredentials:n.getPreferenceValue("UseCredentials")},l)})}(e,i),a()}})}),define("DS/CAT3DWModels/CAT3DWModel",["DS/CAT3DWMedia/CAT3DWMedia","DS/CAT3DWModels/CAT3DWModelView","DS/CAT3DWModels/CAT3DWloaderSequencer"],function(e,t,i){"use strict";var n=e.extend({init:function(e){this._parent(e)},initialize:function(e){(new i).LoadModel(e,this)},createMediaView:function(e){return new t(this,e)},getType:function(){return"CAT3DWModel"}});return UWA.namespace("DS/CAT3DWModels/CAT3DWModel",n)}),define("DS/CAT3DWModels/CAT3DWModelsManager",["DS/CAT3DWMedia/CAT3DWMediaManager","DS/CAT3DWModels/CAT3DWModel","DS/Visualization/Node3D","DS/CAT3DWVisu/CAT3DWPickingControllerFactory"],function(e,t,i,n){"use strict";return e.extend({init:function(){this._parent()},initialize:function(){this._parent();var e=this._viewer.getRootNode(),t=n.getRootModelNodeName();this._rootNode=new i(t),e.addChild(this._rootNode)},createMediaObject:function(e){return new t(e)},getResetEvent:function(){return"CAT3DWModelsManagerReset"}})});