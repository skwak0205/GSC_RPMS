define("DS/CAT3DWCopilot/CAT3DWSpecializedCopilotAdapter",["UWA/Class"],function(e){"use strict";return e.extend({init:function(){this._pickedElements={},this._maxElementsInList=5},preHandlePickedElement:function(){throw Error("This method needs to be overridden by the implementation")}})}),define("DS/CAT3DWCopilot/CAT3DWMagnet",["UWA/Class"],function(e){"use strict";var t=e.extend({init:function(){throw Error("This is an interface, the constructor shall not be called")},getType:function(){throw Error("This is an interface, this method have to be overriden")},snapPosition:function(){throw Error("This is an interface, this method have to be overriden")},getTangents:function(){throw Error("This is an interface, this method have to be overriden")},getGeoms:function(){throw Error("This is an interface, this method have to be overriden")},displayFeedback:function(){throw Error("This is an interface, this method have to be overriden")},hideFeedback:function(){throw Error("This is an interface, this method have to be overriden")}});return t.prototype.Type={GEOM_MAGNET:0,WEB_MAGNET:1},Object.freeze(t.prototype.Type),t}),define("DS/CAT3DWCopilot/CAT3DWMagnetLine",["DS/CAT3DWCopilot/CAT3DWMagnet","DS/CAT3DWInfra/CAT3DWPreferenceManager","DS/CAT3DWVisu/CAT3DWDataFactory","DS/CAT3DWVisu/CAT3DWNode3D","DS/CAT3DWVisu/CAT3DWSession","DS/CAT3DWVisu/CAT3DWTolerance","DS/Mesh/Mesh","DS/Visualization/SceneGraphFactory","DS/Visualization/ThreeJS_DS"],function(e,t,i,n,o,s,r,a,h){"use strict";var d,c,l,g,u,_,p,m,f,D,C,T=n.extend(e,{init:function(e){this._parent(e.name),this.exludeFromBounding(!0),this._position=e.position,this._direction=e.direction,this._grid=e.grid,this._ray=new h.Ray(this._position,this._direction),this._intersectionList={},this._type=e.type,this._geoms=e.geoms?e.geoms:[],T._invisibleMaterial.lineWidth=e.touch?100:25,this._referentialOrigin=new h.Vector3;var t=o.getDrawingPlane().plane;t&&(this._referentialOrigin=t.projectPoint(this._referentialOrigin)),this._feedbackNode=new n("feedbackNode"+e.name),this._feedbackNode.setPickable(r.NOPICKABLE),this.addChild(this._feedbackNode),this.setVisibility(!1),this.setPickingPriorityId(i.getLabelOf(this._type))},getPosition:function(){var e=(new h.Vector3).getPositionFromMatrix(this.getMatrixWorld());return e.add(this._position),e},getDirection:function(){return this._direction.clone().applyMatrix4(this.getMatrixWorld()).normalize()},at:function(e,t){return(t||new h.Vector3).copy(this._direction).multiplyScalar(e).add(this._position)},signedDistanceToPlane:function(e){var t=e.normal.dot(this._direction);return 0===t?0===e.distanceToPoint(this._position)?0:null:-(this._position.dot(e.normal)+e.constant)/t},intersectWithLine:(d=null,c=null,l=null,g=null,u=new h.Vector3,_=0,p=0,m=0,f=0,D=0,C=0,function(e,t){if(d=this._position,c=e._position,l=this._direction,g=e._direction,u.copy(c).sub(d),_=l.dot(g),p=u.dot(l),m=u.dot(g),_<s.toleranceForUnitaryVectors&&(f=d.x+l.x*p+g.x*m-c.x,f*=f,D=d.y+l.y*p+g.y*m-c.y,D*=D,C=d.z+l.z*p+g.z*m-c.z,C*=C,f<s.squareTolerance&&D<s.squareTolerance&&C<s.squareTolerance)){for(var i in t.copy(l).multiplyScalar(p).add(d),this._intersectionList)if(this._intersectionList[i]._position.equals(t))return this._intersectionList[i].addIntersection(e,this),e.addIntersectionPoint(this._intersectionList[i]),1;return 2}return 0}),addIntersectionPoint:function(e){this._intersectionList[e.name]||(this._intersectionList[e.name]=e)},removeIntersectionPoint:function(e){this._intersectionList[e.name]&&delete this._intersectionList[e.name]},destroy:function(){for(var e in this._intersectionList)this._intersectionList[e].removeIntersectionsWith(this),delete this._intersectionList[e];this.setPickable(r.NOPICKABLE)},setEndPoints:function(e,t){if(this._lineRep){var i=this._lineRep.mesh3js.geometry[0].vertexPositionArray;i[0]=e.x,i[1]=e.y,i[2]=e.z,i[3]=t.x,i[4]=t.y,i[5]=t.z,this._lineRep.computeBoundingElements()}else{var n={points:[e,t],material:T._invisibleMaterial};this._lineRep=a.createLineNode(n),this._lineRep.excludeFromCSO(!0),this._lineRep.excludeFromHighlight(!0,!0),this.addChild(this._lineRep),this.setPickable(r.PICKABLE),this.setVisibility(!0)}},getType:function(){return this._type},getTangents:function(){return[this.getDirection()]},getGeoms:function(){return this._geoms},snapPosition:function(e,t){var i=null,n=(t||this._referentialOrigin).clone().sub(this._position),s=this._direction.dot(n),r=this._direction.clone().multiplyScalar(s).add(this._position),a=e.clone().sub(r);if(s=this._direction.dot(a),o.getSnapOnGrid()){var h=.5*this._grid.getGridUnit();s=Math.round(s/h)*h}return(i=this._direction.clone().multiplyScalar(s)).add(r),i},displayFeedback:function(){throw Error("This has to be implemented by the specialization")},hideFeedback:function(){this._feedbackNode.setVisibility(!1)}});return T._invisibleMaterial=new h.MeshBasicMaterial({color:new h.Color(255),transparent:!0,opacity:0}),T._invisibleMaterial.opacity=t.getPreferenceValue("DebugMode")?1:0,T._invisibleMaterial.force=!0,T}),define("DS/CAT3DWCopilot/CAT3DWMagnetPoint",["DS/CAT3DWCopilot/CAT3DWMagnet","DS/CAT3DWInfra/CAT3DWPreferenceManager","DS/CAT3DWVisu/CAT3DWDataFactory","DS/CAT3DWVisu/CAT3DWGeomFactory","DS/CAT3DWVisu/CAT3DWNode3D","DS/Mesh/Mesh","DS/SceneGraphNodes/FixedSizeNode3D","DS/Visualization/ThreeJS_DS"],function(e,t,i,n,o,s,r,a){"use strict";var h=r.extend(e,{init:function(e){e.ratio=1,this._parent(e),this._position=e.position.clone();var s=this.getMatrix();s.setPosition(this._position),this.setMatrix(s),this._type=e.type?e.type:i.SupportTypes.WEB_POINT_MAGNET,this._geoms=e.geoms?e.geoms:[],this._rootNode=e.root,this._rootNode.add(this),t.getPreferenceValue("PickingDebug")&&(h._invisibleMaterial.opacity=.5),this._pickingNode=new o("pickingNode"+e.name),n.createCube({center:new a.Vector3,size:e.size?e.size:10,node:this._pickingNode,material:h._invisibleMaterial}),this.addChild(this._pickingNode),this.setPickingPriorityId(i.getLabelOf(this._type))},getType:function(){return this._type},setPosition:function(e){this._position.copy(e);var t=this.getMatrix();t.setPosition(this._position),this.setMatrix(t)},snapPosition:function(){return this._position},getTangents:function(){for(var e=[],t=0;t<this._geoms.length;t++)this._geoms[t].getTangentAt&&e.push(this._geoms[t].getTangentAt(this._position));return e},getGeoms:function(){return this._geoms},displayFeedback:function(){},hideFeedback:function(){},activate:function(){this._pickingNode&&(this._pickingNode.setPickable(s.PICKABLE),-1===this.children.indexOf(this._pickingNode)&&this.addChild(this._pickingNode))},deactivate:function(){this._pickingNode&&(this._pickingNode.setPickable(s.NOPICKABLE),this.removeChild(this._pickingNode))}});return h._invisibleMaterial=new a.MeshBasicMaterial({color:new a.Color(255),transparent:!0,opacity:0}),h._invisibleMaterial.force=!0,h}),define("DS/CAT3DWCopilot/CAT3DWMagnetsView",["DS/CAT3DWCopilot/CAT3DWMagnetPoint","UWA/Class"],function(e,t){"use strict";return t.extend({init:function(e,t){this._magnetsNode=e,this._magnetsNode.exludeFromBounding(!0),this._viewer=t},addPoint:function(t,i,n,o,s){return new e({name:t,type:n,position:i,root:this._magnetsNode,size:o,geoms:s})},removeMagnetFromID:function(e){var t=this._magnetsNode.getChildByName(e);t&&(this._magnetsNode.remove(t),t=null)},removeMagnet:function(e){e&&(this._magnetsNode.remove(e),e=null)},getMagnet:function(e){return this._magnetsNode.getChildByName(e)},isVisible:function(){return this._magnetsNode.isVisible()},hide:function(){this._magnetsNode.setVisibility(!1)},show:function(){this._magnetsNode.setVisibility(!0)},clear:function(){this._magnetsNode.removeChildren(),this.updateView()},reset:function(){this._magnetsNode.removeChildren()},updateView:function(){this._viewer&&this._viewer.render()}})}),define("DS/CAT3DWCopilot/CAT3DWMagnetsController",["DS/CAT3DWCopilot/CAT3DWMagnetsView","DS/CAT3DWVisu/CAT3DWDataFactory","UWA/Class"],function(e,t,i){"use strict";return i.extend({init:function(e,t,i){this._magnetsUI=this._getView(e,t,i),this._highlightedNode=null},destroy:function(){this._magnetsUI.clear(),this._magnetsUI=null},_getView:function(t,i){return new e(t,i)},addWebPointMagnet:function(e,i,n){return this._magnetsUI.addPoint(e,i,t.SupportTypes.WEB_POINT_MAGNET,30,n)},removeWebPointMagnet:function(e){this._magnetsUI.removeMagnet(e)},removeWebPointMagnetFromID:function(e){this._magnetsUI.removeMagnetFromID(e)},activateWebPointMagnetFromID:function(e){var i=this._magnetsUI.getMagnet(e);i&&i.getType()===t.SupportTypes.WEB_POINT_MAGNET&&i.activate()},deactivateWebPointMagnetFromID:function(e){var i=this._magnetsUI.getMagnet(e);i&&i.getType()===t.SupportTypes.WEB_POINT_MAGNET&&i.deactivate()},setWebPointMagnetPositionFromID:function(e,i){var n=this._magnetsUI.getMagnet(e);n&&n.getType()===t.SupportTypes.WEB_POINT_MAGNET&&n.setPosition(i)},resetMagnets:function(){this._magnetsUI.reset()},displayFeedback:function(e){e.displayFeedback(),this._highlightedNode=e},hideFeedback:function(){this._highlightedNode&&(this._highlightedNode.hideFeedback(),this._highlightedNode=null)},areMagnetsVisible:function(){return this._magnetsUI.isVisible()},hideAllMagnets:function(){this._magnetsUI.hide()},showAllMagnets:function(){this._magnetsUI.show()},computeOnDemandMagnets:function(){}})}),define("DS/CAT3DWCopilot/CAT3DWGeometryCopilot",["DS/ApplicationFrame/FrameWindowsManager","DS/CAT3DWCopilot/CAT3DWSpecializedCopilotAdapter","DS/CAT3DWCopilot/CAT3DWMagnetsController","DS/Visualization/Node3D"],function(e,t,i,n){"use strict";return t.extend({init:function(){this._parent(),this._magnetsNode=new n(this.getMagnetsRootName());var t=e.getFrameWindow();this._viewer=t.getViewer(),this._viewer.addNode(this._magnetsNode),this._magnetsController=null,this._highlightedNode=null},_getMagnetsController:function(e,t){return new i(e,t)},getMagnetsController:function(){return this._magnetsController},start:function(){this._magnetsController=this._getMagnetsController(this._magnetsNode,this._viewer)},stop:function(){this._magnetsController&&(this._magnetsController.destroy(),this._magnetsController=null)},clear:function(){this._magnetsController&&this._magnetsController.destroy(),this._viewer&&this._viewer.removeNode(this._magnetsNode),this._magnetsNode&&(this._magnetsNode.removeChildren(),this._magnetsNode=null)},getMagnetsRootName:function(){return"magnets"},preHandlePickedElement:function(){this._highlightedNode&&(this._highlightedNode.hideFeedback(),this._highlightedNode=null)},handleMagnet:function(e){this._highlightedNode=e.point.supportData.node,this._highlightedNode.displayFeedback(e.point.position)},handleManipulator:function(){},handleVertex:function(){},handleEdge:function(){},handleFace:function(){}})});