define("DS/DMUMarkerManipulators/DMUMarkerRecManipulator",["UWA/Class","DS/Visualization/ThreeJS_DS","DS/Visualization/Node3D","DS/Manipulators/PointHandle","DS/Visualization/SceneGraphFactory","DS/DMUBaseExperience/EXPToolsMaterial","DS/DMUBaseExperience/EXPToolsVisualization","DS/Mesh/Mesh","DS/WebappsUtils/WebappsUtils","DS/DMUMeasurable/DMUToolsSceneGraph","DS/DMUBaseUIServices/DMUObjectsServices","DS/DMUBaseUIManipulators/DMUManipulatorsHelper"],function(e,t,i,n,o,a,r,s,l,c,h,u){"use strict";return e.extend({init:function(e){if(e&&e.dmuObj&&e.viewer){var p,d,M,g,f,P,v,m,w,b,D,y,V,S,x=e.viewer,I=e.dmuObj,C=I.getManipulationData(),B=I.getNode().getRepNode(),U=new u(x),R=U.getPointedElem(),T=l.getWebappsAssetUrl("DMUMarkerManipulators","icons/32/"),k=["url("+T+"DMUPositionCursorHL.png) 10 10, auto","url("+T+"DMURotationCursorHL.png) 16 16, auto","nwse-resize","ns-resize","nesw-resize","ew-resize","nwse-resize","ns-resize","nesw-resize","ew-resize"];if(this.onBeginManipulateCB=function(e){e&&(g||(g={}),e.boundingBox&&(g.height=e.boundingBox.height,g.width=e.boundingBox.width,g.position=e.boundingBox.position),y=C.width,D=C.height,V=C.rotation,b=C.position,w=r.getViewerPositionFromPoint(x,b),w=new t.Vector2(w.left,w.top))},this.onManipulateCB=function(e){e&&(e.handleType?function(e){if(e)if(void 0!==e.rotationAngle&&"Rot"===e.handleType)C.rotation=V+e.rotationAngle;else if(e.transVec2D)if("Center"===e.handleType){C.position=r.getNearPositionFrom2DCoordinates(w.clone().add(e.transVec2D),x,b);var i=r.getMMPerPixelRatio(x,b),n=r.getMMPerPixelRatio(x,C.position);C.width=y*n/i,C.height=D*n/i}else{var o,a,s,l,c=e.transVec2D.clone(),h=r.getMMPerPixelRatio(x,b),u=r.getMMPerPixelRatio(x,g.position),p=y/h/(g.width/u),d=D/h/(g.height/u);switch(c.setX(c.x*p),c.setY(c.y*d),h*=e.ctrlKey?2:1,e.handleType){case"TL":a=y-c.x*h,s=D-c.y*h,l=new t.Vector3(-1,-1),o=!0;break;case"TR":a=y+c.x*h,s=D-c.y*h,l=new t.Vector3(-1,1),o=!0;break;case"BL":a=y-c.x*h,s=D+c.y*h,l=new t.Vector3(1,-1),o=!0;break;case"BR":a=y+c.x*h,s=D+c.y*h,l=new t.Vector3(1,1),o=!0;break;case"Top":s=D-c.y*h,c.setX(0);break;case"Right":a=y+c.x*h,c.setY(0);break;case"Bottom":s=D+c.y*h,c.setX(0);break;case"Left":a=y-c.x*h,c.setY(0)}if(e.shiftKey&&o){var M=Math.max(Math.abs(a/y),Math.abs(s/D));C.width=y*M,C.height=D*M;var f=new t.Vector2(Math.sin(C.rotation),Math.cos(C.rotation)).multiplyScalar(l.x*(C.height*Math.sign(s)-D)/h),P=new t.Vector2(Math.cos(C.rotation),-Math.sin(C.rotation)).multiplyScalar(l.y*(C.width*Math.sign(a)-y)/h);c=f.add(P)}else if(C.width=a?Math.abs(a):C.width,C.height=s?Math.abs(s):C.height,!e.ctrlKey&&C.rotation){var v=c.x*Math.cos(-C.rotation)-c.y*Math.sin(-C.rotation),m=c.x*Math.sin(-C.rotation)+c.y*Math.cos(-C.rotation);c.setX(v),c.setY(m)}e.ctrlKey?C.position=b:C.position=r.getPositionFromIntersection(x,w.clone().add(c.multiplyScalar(.5)),b)}}(e):e.translationVector2D&&(C.position=r.getPositionFromIntersection(x,w.clone().add(e.translationVector2D),b)),I.updateRepresentation&&I.updateRepresentation(C,!1))},this.onEndManipulateCB=function(){K(),I.updateRepresentation&&I.updateRepresentation(C,!0)},!p){p=[];for(var E=0;E<10;E++){var N=20;1===E&&(N=24);var A,L,F=new n({viewer:x,sceneGraph:B,size:N});switch(F.subscribe("BeginManipulate",z),F.subscribe("Manipulate",H),F.subscribe("EndManipulate",O),F.subscribe("BeginPreactivate",W),F.subscribe("EndPreactivate",function(){R.setStyle("cursor","")}),F.setCursor(!1),F.diskNode.setNoHighlightNoZ(!0),F.manipulator.setPreActivationDuringManipulation(!1),E){case 0:A="Center",L="move";break;case 1:A="Rot",L="url("+T+"DMURotationCursorSel.png) 16 16, auto";break;case 2:A="TL",L="crosshair";break;case 3:A="Top",L="crosshair";break;case 4:A="TR",L="crosshair";break;case 5:A="Right",L="crosshair";break;case 6:A="BR",L="crosshair";break;case 7:A="Bottom",L="crosshair";break;case 8:A="BL",L="crosshair";break;case 9:A="Left",L="crosshair"}0!==E&&(F.manipulator.setPrePickingDuringManipulation(!1),F.manipulator.setPickingDuringManipulation(!1)),1===E&&(F.pointMat.map=new t.ImageUtils.loadTexture(T+"DMURotationManip.png",void 0,null,null,t.ImageUtils.crossOriginPolicy)),p.push({uid:F.manipulator.getUID(),type:A,handle:F,cursor:L})}d||((d=new i).setPickable(s.PICKTHROUGH),d.setNoHighlightNoZ(!0),M||(M=a.createLineMaterial({color:new t.Color(11843258),opacity:1,lineWidth:window.devicePixelRatio,pattern:"6"})),B.addChild(d)),K()}this.setHandlesVisibility=function(e){if(p&&p.length)for(var t=0;t<p.length;t++)p[t].handle.setVisibility(e);d&&d.setVisibility(e)},this.remove=function(){p&&p.length&&p.forEach(function(e){e.handle.dispose()}),p=null,d&&(d.removeChildren(),B.removeChild(d)),U&&U.remove(),M&&M.dispose(),d=B=g=I=f=M=C=x=null,U=null}}function H(i){if(i&&U.isCanvasToDraw(i.event.from[0].getView())){U.updateManipulationData(i);var n,o,a=i.event.from[0].getCurrentPosition(i.viewer.canvas),s=i.ctrlKey||void 0===i.ctrlKey&&i.event.from&&i.event.from.length&&i.event.from[0].event&&i.event.from[0].event.ctrlKey,l=i.shiftKey||void 0===i.shiftKey&&i.event.from&&i.event.from.length&&i.event.from[0].event&&i.event.from[0].event.shiftKey;if("Center"===f){if(i.intersection&&i.intersection.path&&i.intersection.path.length&&c.isAPOIPath(i.intersection.path[0])){const{position:e}=h.getPOIInformationFromPathElement(i.intersection.path[0]);if(e){const n=r.getViewerPositionFromPoint(i.viewer,e);n&&(a=new t.Vector2(n.left,n.top))}}n=r.get2DSnappedPosition(a).sub(m),l&&(Math.abs(n.y)>=Math.abs(n.x)?n.setX(0):n.setY(0))}else if("Rot"===f){var u=(new t.Vector2).subVectors(a,w),p=new t.Vector2(1,0).dot(u.clone().normalize());if(o=a.y<=w.y?Math.acos(p):2*Math.PI-Math.acos(p),v!==l){var d=Math.round(o/P)*P,M=new t.Vector2(Math.cos(d),-Math.sin(d));M.setLength(u.dot(M)),M.distanceTo(u)<U.ROTATION_SNAP_THRESHOLD?(o=d,U.displayAxis(C.position,o,u.length()+20)):U.removeAxis()}else U.removeAxis();o-=V+Math.PI/2}else if(n=r.get2DSnappedPosition(a,C.rotation).sub(m),C.rotation){var g=n.x*Math.cos(C.rotation)-n.y*Math.sin(C.rotation),b=n.x*Math.sin(C.rotation)+n.y*Math.cos(C.rotation);n.setX(g),n.setY(b)}e.onManipulateCB&&e.onManipulateCB({transVec2D:n,rotationAngle:o,handleType:f,ctrlKey:s,shiftKey:l})}}function z(i){if(i){var n;I.setNodePickable(s.INVISIBLE);for(var o=0;o<p.length;o++)if(p[o].uid===i.manipulator.getUID()){f=p[o].type,R.setStyle("cursor",p[o].cursor),n=(new t.Vector3).getPositionFromMatrix(p[o].handle.getMatrixWorld());break}var a=parseFloat(localStorage.getItem("DMUSnapRotationAngle"));P=(isNaN(a)?90:a)*Math.PI/180,v="false"!==localStorage.getItem("DMUSnapRotationToggle"),m=r.getViewerPositionFromPoint(x,n),m=new t.Vector2(m.left,m.top),"Center"===f?U.createHVAxis((new t.Vector3).getPositionFromMatrix(B.getMatrixWorld()),{key:"Shift",keyCode:16},H):"Rot"===f?U.addToListener("Shift",H):(U.addToListener("Shift",H),U.addToListener("Control",H)),U.activate(),e.onBeginManipulateCB&&e.onBeginManipulateCB({boundingBox:C})}}function O(t){t&&(R.setStyle("cursor",""),U.remove(),e.onManipulateCB&&e.onEndManipulateCB(),I.setNodePickable(s.PICKABLE),f=null)}function K(){var e=function(e){for(var t=0;t<e;t++){var i=S.pop();S.splice(2,0,i)}};S=k.slice();var i=Math.sin(C.rotation),n=Math.cos(C.rotation);n<Math.cos(7*Math.PI/8)?e(4):n<Math.cos(Math.PI/8)&&(n>Math.cos(3*Math.PI/8)?e(i<0?7:1):n<Math.cos(5*Math.PI/8)?e(i<0?5:3):e(i<0?6:2));var a=r.getMMPerPixelRatio(x,C.position),s=(new t.Matrix4).makeRotationZ(C.rotation);if(p&&p.length)for(var l=C.width/2,c=C.height/2,h=0;h<p.length;h++)switch(p[h].type){case"TL":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(-l,c,0).applyMatrix4(s)));break;case"TR":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(l,c,0).applyMatrix4(s)));break;case"BL":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(-l,-c,0).applyMatrix4(s)));break;case"BR":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(l,-c,0).applyMatrix4(s)));break;case"Rot":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(0,30*a+c,0).applyMatrix4(s))),p[h].handle.diskNode.setMatrix(s.clone().setPosition(new t.Vector3(-.5,-.5,0).applyMatrix4(s)));break;case"Top":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(0,c,0).applyMatrix4(s)));break;case"Right":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(l,0,0).applyMatrix4(s)));break;case"Bottom":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(0,-c,0).applyMatrix4(s)));break;case"Left":p[h].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(-l,0,0).applyMatrix4(s)))}if(d){d.removeChildren();var u=C.height,g=C.width,f=o.createRectangleNode({width:g,height:u,fill:!1,material:M});f.excludeFromHighlight(!0,!0),d.addChild(f);var P=o.createLineNode({points:[new t.Vector3(g/2,u<0?0:u,0),new t.Vector3(g/2,30*a+u,0)],fill:!1,material:M});P.excludeFromHighlight(!0,!0),d.addChild(P);var v=(new t.Matrix4).makeRotationZ(C.rotation);d.setMatrix((new t.Matrix4).setPosition(new t.Vector3(-g/2,-u/2,0).applyMatrix4(v)).multiply(v))}}function W(e){if(e)for(var t=0;t<p.length;t++)if(p[t].uid===e.manipulator.getUID()){R.setStyle("cursor",S[t]);break}}}})}),define("DS/DMUMarkerManipulators/DMUMarkerCircleManipulator",["UWA/Class","DS/Visualization/ThreeJS_DS","DS/Visualization/Node3D","DS/Manipulators/PointHandle","DS/Visualization/SceneGraphFactory","DS/DMUBaseExperience/EXPToolsMaterial","DS/DMUBaseExperience/EXPToolsVisualization","DS/DMUBaseUIManipulators/DMUManipulatorsHelper","DS/WebappsUtils/WebappsUtils","DS/DMUMeasurable/DMUToolsSceneGraph","DS/DMUBaseUIServices/DMUObjectsServices","DS/Mesh/Mesh"],function(e,t,i,n,o,a,r,s,l,c,h,u){"use strict";return e.extend({init:function(e){if(e&&e.dmuObj&&e.viewer){var p,d,M,g,f,P,v,m,w,b,D,y=e.viewer,V=e.dmuObj,S=V.getManipulationData(),x=V.getNode().getRepNode(),I=new s(y),C=I.getPointedElem(),B=l.getWebappsAssetUrl("DMUMarkerManipulators","icons/32/");if(this.onBeginManipulateCB=function(e){e&&(P||(P={}),e.circleParams&&(P.radius=e.circleParams.radius),m=S.position,w=S.radius,D=r.getViewerPositionFromPoint(y,m),D=new t.Vector2(D.left,D.top))},this.onManipulateCB=function(e){e&&(e.handleType?function(e){if("Center"===e.handleType&&e.transVec2D){S.position=r.getNearPositionFrom2DCoordinates(D.clone().add(e.transVec2D),y,m);var t=r.getMMPerPixelRatio(y,m),i=r.getMMPerPixelRatio(y,S.position);S.radius=w*i/t}else"Radius"===e.handleType&&e.radius&&(S.radius=e.radius*w/P.radius)}(e):e.translationVector2D&&(S.position=r.getPositionFromIntersection(y,D.clone().add(e.translationVector2D),m)),V.updateRepresentation&&V.updateRepresentation(S,!1))},this.onEndManipulateCB=function(){L(),V.updateRepresentation&&V.updateRepresentation(S,!0)},!p){p=[];for(var U=0;U<2;U++){var R,T,k=new n({viewer:y,sceneGraph:x});switch(k.subscribe("BeginManipulate",N),k.subscribe("Manipulate",E),k.subscribe("EndManipulate",A),k.subscribe("EndPreactivate",function(){C.setStyle("cursor","")}),k.setCursor(!1),k.diskNode.setNoHighlightNoZ(!0),k.manipulator.setPreActivationDuringManipulation(!1),U){case 0:R="Center",T="move",k.subscribe("BeginPreactivate",function(){C.setStyle("cursor","url("+B+"DMUPositionCursorHL.png) 10 10, auto")});break;case 1:R="Radius",T="crosshair",k.subscribe("BeginPreactivate",F)}0!==U&&k.manipulator.setPrePickingDuringManipulation(!1),p.push({uid:k.manipulator.getUID(),type:R,handle:k,cursor:T})}(d=new i).setPickable(u.PICKTHROUGH),d.setNoHighlightNoZ(!0),x.addChild(d),M||(M=a.createLineMaterial({color:new t.Color(11843258),opacity:1,lineWidth:window.devicePixelRatio,pattern:"6"})),L()}this.setHandlesVisibility=function(e){if(p&&p.length){for(var t=0;t<p.length;t++)p[t].handle.setVisibility(e);d&&d.setVisibility(e)}},this.remove=function(){p&&p.length&&p.forEach(function(e){e.handle.dispose()}),p=null,d&&(d.removeChildren(),x.removeChild(d)),I&&I.remove(),M&&M.dispose(),I=d=M=x=P=V=f=g=null}}function E(i){if(i&&I.isCanvasToDraw(i.event.from[0].getView())){var n,o,a;if(I.updateManipulationData(i),"Center"===g){let e;if(i.intersection&&i.intersection.path&&i.intersection.path.length&&c.isAPOIPath(i.intersection.path[0])){const{position:n}=h.getPOIInformationFromPathElement(i.intersection.path[0]);if(n){const o=r.getViewerPositionFromPoint(i.viewer,n);o&&(e=new t.Vector2(o.left,o.top))}}e||(e=i.event.from[0].getCurrentPosition(i.viewer.canvas));var s=i.shiftKey||void 0===i.shiftKey&&i.event.from&&i.event.from.length&&i.event.from[0].event&&i.event.from[0].event.shiftKey;n=r.get2DSnappedPosition(e).sub(b),s&&(Math.abs(n.y)>=Math.abs(n.x)?n.setX(0):n.setY(0))}else"Radius"===g&&(o=r.getPositionFromIntersection(y,i.intersection.position,m,!0),a=m.clone().sub(o).length());e.onManipulateCB&&e.onManipulateCB({transVec2D:n,radius:a,handleType:g})}}function N(i){if(i){var n;V.setNodePickable(u.INVISIBLE);for(var o=0;o<p.length;o++)if(p[o].uid===i.manipulator.getUID()){g=p[o].type,C.setStyle("cursor",p[o].cursor),n=(new t.Vector3).getPositionFromMatrix(p[o].handle.getMatrixWorld());break}b=r.getViewerPositionFromPoint(y,n),b=new t.Vector2(b.left,b.top),"Center"===g&&(I.createHVAxis(n,{key:"Shift",keyCode:16},E),I.activate()),e.onBeginManipulateCB&&e.onBeginManipulateCB({circleParams:S})}}function A(t){t&&(C.setStyle("cursor",""),"Center"===g&&I.remove(),V.setNodePickable(u.PICKABLE),g=null,e.onEndManipulateCB&&e.onEndManipulateCB())}function L(){var e=y.currentViewpoint.project(S.position);e=new t.Vector2(e.x,e.y);var i=Math.PI;if(f){var n=y.currentViewpoint.project(f);n=new t.Vector2(n.x,n.y);var a=e.sub(n);(i=Math.atan2(a.y,a.x))<0&&(i+=2*Math.PI)}var r=(new t.Matrix4).makeRotationZ(Math.PI-i).multiply((new t.Matrix4).makeTranslation(S.radius,0,0));if(v=i>Math.PI/8&&i<=3*Math.PI/8||i>9*Math.PI/8&&i<=11*Math.PI/8?"nwse-resize":i>3*Math.PI/8&&i<=5*Math.PI/8||i>11*Math.PI/8&&i<=13*Math.PI/8?"ns-resize":i>5*Math.PI/8&&i<=7*Math.PI/8||i>13*Math.PI/8&&i<=15*Math.PI/8?"nesw-resize":"ew-resize",p&&p.length&&(p.forEach(function(e){"Radius"===e.type&&e.handle.setMatrix(r)}),d)){d.removeChildren();var s=o.createLineNode({points:[new t.Vector3,(new t.Vector3).getPositionFromMatrix(r)],material:M});s.excludeFromHighlight(!0,!0),d.addChild(s)}}function F(){C.setStyle("cursor",v)}}})}),define("DS/DMUMarkerManipulators/DMUMarkerPolylineManipulator",["UWA/Class","DS/Visualization/ThreeJS_DS","DS/Manipulators/PointHandle","DS/DMUBaseExperience/EXPToolsVisualization","DS/Mesh/Mesh","DS/DMUBaseUIManipulators/DMUManipulatorsHelper","DS/DMUMeasurable/DMUToolsSceneGraph","DS/DMUBaseUIServices/DMUObjectsServices"],function(e,t,i,n,o,a,r,s){"use strict";return e.extend({init:function(e){if(e&&e.dmuObj&&e.viewer){var l,c,h,u,p,d,M=e.dmuObj,g=M.getManipulationData(),f=e.viewer,P=new a(f),v=P.getPointedElem();if(this.onBeginManipulateCB=function(e){if(e){h=g.slice(),u=[];for(var i=0;i<h.length;i++){var o=n.getViewerPositionFromPoint(f,h[i]);o=new t.Vector2(o.left,o.top),u.push(o)}}},this.onManipulateCB=function(e){if(e){if(e.translationVector2D&&h&&h.length&&u&&u.length)for(var t=0;t<h.length;t++)g[t]=n.getPositionFromIntersection(f,u[t].clone().add(e.translationVector2D),h[t]);else if(e.handleType&&"Center"===e.handleType&&e.transVec2D)for(var i=0;i<h.length;i++)g[i]=n.getPositionFromIntersection(f,u[i].clone().add(e.transVec2D),h[i]);M.updateRepresentation&&M.updateRepresentation(g,!1)}},this.onEndManipulateCB=function(){if(h)for(var e=0;e<h.length;e++)b(e);M.updateRepresentation&&M.updateRepresentation(g,!0)},!l){l=[];for(var m=0;m<g.length;m++){var w=new i({viewer:f,sceneGraph:M.getNode().getRepNode()});w.subscribe("BeginManipulate",y),w.subscribe("Manipulate",D),w.subscribe("EndManipulate",V),w.subscribe("EndPreactivate",function(){v.setStyle("cursor","")}),w.subscribe("BeginPreactivate",function(){v.setStyle("cursor","move")}),w.setCursor(!1),w.diskNode.setNoHighlightNoZ(!0),w.manipulator.setPreActivationDuringManipulation(!1),l.push({uid:w.manipulator.getUID(),handleNb:m,handle:w}),b(m)}}this.setHandlesVisibility=function(e){if(l&&l.length)for(var t=0;t<l.length;t++)l[t].handle.setVisibility(e)},this.remove=function(){l&&l.length&&l.forEach(function(e){e.handle.dispose()}),P&&P.remove(),P=l=c=u=p=d=h=g=null}}function b(e){l&&l.length&&l[e].handle.setMatrix((new t.Matrix4).setPosition(g[e]))}function D(i){if(i&&P.isCanvasToDraw(i.event.from[0].getView())){P.updateManipulationData(i);var o,a=i.intersection,l=0===c?1:0,h=i.shiftKey||void 0===i.shiftKey&&i.event.from&&i.event.from.length&&i.event.from[0].event&&i.event.from[0].event.shiftKey;if(a.position&&a.path.length){if(r.isAPOIPath(a.path[0])){const{position:e}=s.getPOIInformationFromPathElement(a.path[0]);e&&(o=e)}o||(o=n.getPositionFromIntersection(f,a.position,a.position,!0))}else o=n.getPositionFromIntersection(f,a.position,g[l],!0);var u,v=i.event.from[0].getCurrentPosition(f.canvas),m=n.getViewerPositionFromPoint(f,g[l]);m=new t.Vector2(m.left,m.top);var w=(new t.Vector2).subVectors(v,m),b=new t.Vector2(1,0).dot(w.clone().normalize());if(u=v.y<=m.y?Math.acos(b):2*Math.PI-Math.acos(b),d!==h){var D=Math.round(u/p)*p,y=new t.Vector2(Math.cos(D),-Math.sin(D));if(y.setLength(w.dot(y)),y.distanceTo(w)<P.ROTATION_SNAP_THRESHOLD){y.normalize();var V=0===c?1:0,S=(new t.Vector2).subVectors(n.get2DSnappedPosition(v,D),m);y.setLength(S.dot(y)),g[c]=n.getPositionFromIntersection(f,m.add(y),g[l]),P.displayAxis(g[V],D,S.length()+20)}else g[c]=o,P.removeAxis()}else g[c]=o,P.removeAxis();M.updateRepresentation&&M.updateRepresentation(g,!1),e.onManipulateCB&&e.onManipulateCB()}}function y(t){if(t){M.setNodePickable(o.INVISIBLE);for(var i=0;i<l.length;i++)if(l[i].uid===t.manipulator.getUID()){c=l[i].handleNb;break}var n=parseFloat(localStorage.getItem("DMUSnapRotationAngle"));p=(isNaN(n)?90:n)*Math.PI/180,d="false"!==localStorage.getItem("DMUSnapRotationToggle"),P.addToListener("Shift",D),P.activate(),v.setStyle("cursor","crosshair"),e.onBeginManipulateCB&&e.onBeginManipulateCB()}}function V(t){t&&(v.setStyle("cursor",""),P.remove(),b(c),M.setNodePickable(o.PICKABLE),c=null,e.onEndManipulateCB&&e.onEndManipulateCB())}}})}),define("DS/DMUMarkerManipulators/DMUMarkerArrowManipulator",["UWA/Class","DS/Visualization/ThreeJS_DS","DS/Manipulators/PointHandle","DS/DMUBaseExperience/EXPToolsVisualization","DS/Mesh/Mesh","DS/DMUMeasurable/DMUToolsSceneGraph","DS/DMUBaseUIServices/DMUObjectsServices","DS/DMUBaseUIManipulators/DMUManipulatorsHelper"],function(e,t,i,n,o,a,r,s){"use strict";return e.extend({init:function(e){if(e&&e.dmuObj&&e.viewer){var l,c,h,u,p,d,M,g,f,P,v,m,w,b=e.dmuObj,D=b.getManipulationData(),y=e.viewer,V=new s(y),S=V.getPointedElem();if(this.onBeginManipulateCB=function(e){e&&(v=D.pointingPoint,P=D.endingPoint,m=n.getViewerPositionFromPoint(y,v),m=new t.Vector2(m.left,m.top),w=n.getViewerPositionFromPoint(y,P),w=new t.Vector2(w.left,w.top))},this.onManipulateCB=function(e){e&&(e.translationVector2D?(D.pointingPoint=n.getPositionFromIntersection(y,m.clone().add(e.translationVector2D),v),D.endingPoint=n.getPositionFromIntersection(y,w.clone().add(e.translationVector2D),P)):e.handleType&&"Center"===e.handleType&&e.transVec2D&&(D.pointingPoint=n.getPositionFromIntersection(y,m.clone().add(e.transVec2D),v),D.endingPoint=n.getPositionFromIntersection(y,w.clone().add(e.transVec2D),P)),b.updateRepresentation&&b.updateRepresentation(D,!1))},this.onEndManipulateCB=function(){b.updateRepresentation&&b.updateRepresentation(D,!0)},!l){l=[];for(var x=0;x<3;x++){var I,C=new i({viewer:y,sceneGraph:b.getNode().getRepNode()});switch(C.subscribe("BeginManipulate",R),C.subscribe("Manipulate",U),C.subscribe("EndPreactivate",function(){S.setStyle("cursor","")}),C.subscribe("BeginPreactivate",function(){S.setStyle("cursor","move")}),C.setCursor(!1),C.subscribe("EndManipulate",T),C.diskNode.setNoHighlightNoZ(!0),C.manipulator.setPreActivationDuringManipulation(!1),2===x&&C.manipulator.setPrePickingDuringManipulation(!1),x){case 0:I="Pointing";break;case 1:I="Ending";break;case 2:I="Shaft"}l.push({uid:C.manipulator.getUID(),type:I,handle:C})}B()}this.setHandlesVisibility=function(e){if(l&&l.length)for(var t=0;t<l.length;t++)l[t].handle.setVisibility(e)},this.remove=function(){l&&l.length&&l.forEach(function(e){e.handle.dispose()}),V&&V.remove(),V=l=c=P=v=h=u=null,p=d=M=g=f=null}}function B(){if(l&&l.length){var e=D.endingPoint.clone().sub(D.pointingPoint);l.forEach(function(i){switch(i.type){case"Ending":i.handle.setMatrix((new t.Matrix4).makeTranslation(0,0,e.length()));break;case"Shaft":i.handle.setMatrix((new t.Matrix4).makeTranslation(D.width/2,0,e.length()*(1-D.shaftRatio)))}})}}function U(i){i&&V.isCanvasToDraw(i.event.from[0].getView())&&(V.updateManipulationData(i),function(e){if(e&&e.intersection){var i,o=e.intersection;if(o.position&&o.path.length){if(a.isAPOIPath(o.path[0])){const{position:e}=r.getPOIInformationFromPathElement(o.path[0]);e&&(i=e)}i||(i=n.getPositionFromIntersection(y,o.position,o.position,!0))}else i=n.getPositionFromIntersection(y,o.position,D.pointingPoint,!0);var s,l,P,v,m,w=e.event.from[0].getCurrentPosition(y.canvas),S=e.shiftKey||void 0===e.shiftKey&&e.event.from&&e.event.from.length&&e.event.from[0].event&&e.event.from[0].event.shiftKey;switch(c){case"Pointing":var x=n.getViewerPositionFromPoint(y,D.endingPoint);if(x=new t.Vector2(x.left,x.top),l=(new t.Vector2).subVectors(w,x),P=new t.Vector2(1,0).dot(l.clone().normalize()),s=w.y<=x.y?Math.acos(P):2*Math.PI-Math.acos(P),u!==S)if(v=Math.round(s/h)*h,(m=new t.Vector2(Math.cos(v),-Math.sin(v))).setLength(l.dot(m)),m.distanceTo(l)<V.ROTATION_SNAP_THRESHOLD){m.normalize();var I=(new t.Vector2).subVectors(n.get2DSnappedPosition(w,v),x);m.setLength(I.dot(m)),D.pointingPoint=n.getPositionFromIntersection(y,x.clone().add(m),D.endingPoint),V.displayAxis(D.endingPoint,v,I.length()+20)}else D.pointingPoint=i,V.removeAxis();else D.pointingPoint=i,V.removeAxis();break;case"Ending":var C=n.getViewerPositionFromPoint(y,D.pointingPoint);if(C=new t.Vector2(C.left,C.top),l=(new t.Vector2).subVectors(w,C),P=new t.Vector2(1,0).dot(l.clone().normalize()),s=w.y<=C.y?Math.acos(P):2*Math.PI-Math.acos(P),u!==S)if(v=Math.round(s/h)*h,(m=new t.Vector2(Math.cos(v),-Math.sin(v))).setLength(l.dot(m)),m.distanceTo(l)<V.ROTATION_SNAP_THRESHOLD){m.normalize();var B=(new t.Vector2).subVectors(n.get2DSnappedPosition(w,v),C);m.setLength(B.dot(m)),D.endingPoint=n.getPositionFromIntersection(y,C.add(m),D.pointingPoint),V.displayAxis(D.pointingPoint,v,B.length()+20)}else D.endingPoint=i,V.removeAxis();else D.endingPoint=i,V.removeAxis();break;case"Shaft":var U=(new t.Vector2).copy(e.event.from[0].getCurrentPosition(y.canvas)),R=e.event.viewer.currentViewpoint.create3DRayFrom2DPoint(U),T=R.computeShortestPointToLine(p,d),k=R.computeShortestPointToLine(p,M),E=D.endingPoint.clone().sub(D.pointingPoint),N=(new t.Vector3).subVectors(T,g);D.width=2*N.length();var A=(new t.Vector3).subVectors(k,f).length()/E.length();("beginSide"===b.getAttribute("sStyle")&&A<=.9&&A>=.1||"bothSide"===b.getAttribute("sStyle")&&A<=.9&&A>=.55)&&(D.shaftRatio=A)}}}(i),b.updateRepresentation&&b.updateRepresentation(D,!1),e.onManipulateCB&&e.onManipulateCB())}function R(t){if(t){b.setNodePickable(o.INVISIBLE);for(var i=0;i<l.length;i++)if(l[i].uid===t.manipulator.getUID()){c=l[i].type;break}if("Shaft"===c){p=t.intersection.position;var n=D.endingPoint.clone().sub(D.pointingPoint),a=y.currentViewpoint.getSightDirection().clone().cross(n);d=p.clone().add(a),M=p.clone().add(n),g=n.clone().normalize().multiplyScalar((1-D.shaftRatio)*n.length()).add(D.pointingPoint),f=a.clone().normalize().multiplyScalar(D.width/2).add(D.endingPoint)}var r=parseFloat(localStorage.getItem("DMUSnapRotationAngle"));h=(isNaN(r)?90:r)*Math.PI/180,u="false"!==localStorage.getItem("DMUSnapRotationToggle"),V.addToListener("Shift",U),V.activate(),S.setStyle("cursor","crosshair"),e.onBeginManipulateCB&&e.onBeginManipulateCB()}}function T(){S.setStyle("cursor",""),B(),V.remove(),b.setNodePickable(o.PICKABLE),c=null,e.onEndManipulateCB&&e.onEndManipulateCB()}}})}),define("DS/DMUMarkerManipulators/DMUMarkerLabelManipulator",["UWA/Class","DS/Visualization/ThreeJS_DS","DS/Manipulators/PointHandle","DS/DMUBaseExperience/EXPToolsVisualization","DS/Mesh/Mesh","DS/CoreEvents/Events","DS/WebappsUtils/WebappsUtils","DS/DMUBaseUIManipulators/DMUManipulatorsHelper","DS/Visualization/SceneGraphFactory","DS/DMUBaseExperience/EXPToolsMaterial","DS/DMUMeasurable/DMUToolsSceneGraph","DS/DMUBaseUIServices/DMUObjectsServices","DS/Visualization/Node3D"],function(e,t,i,n,o,a,r,s,l,c,h,u,p){"use strict";return e.extend({init:function(e){if(e&&e.dmuObj&&e.viewer){var d,M,g,f,P,v,m,w,b,D,y,V,S,x,I,C,B,U=this,R={MEASURE:0,TEXT:1,PICTURE:2},T=e.dmuObj,k=T.getManipulationData(),E={offsetWidth:k.width-k.imgWidth,offsetHeight:k.height-k.imgHeight},N=e.viewer,A=new s(N),L=A.getPointedElem(),F=r.getWebappsAssetUrl("DMUMarkerManipulators","icons/32/"),H=["nwse-resize","ns-resize","nesw-resize","ew-resize","nwse-resize","ns-resize","nesw-resize","ew-resize"],z=T.getType();if(d="DMUMeasure"===z?R.MEASURE:z.includes("Text")||z.includes("Stamp")?R.TEXT:R.PICTURE,this.onBeginManipulateCB=function(e){e&&(e.labelParams&&(D||(D={}),D.width=e.labelParams.width,D.height=e.labelParams.height),k=T.getManipulationData(),E={offsetWidth:k.width-k.imgWidth,offsetHeight:k.height-k.imgHeight},S=k.position,V=n.getViewerPositionFromPoint(N,S),V=new t.Vector2(V.left,V.top),I=k.width,C=k.height,x=k.rotationAngle)},this.onManipulateCB=function(e){e&&(e.translationVector2D?k.position=n.getPositionFromIntersection(N,V.clone().add(e.translationVector2D),S):e.handleType&&function(e){if(e)if("Center"===e.handleType&&e.transVec2D)k.position=n.getPositionFromIntersection(N,V.clone().add(e.transVec2D),S);else if("Rot"===e.handleType)k.rotationAngle=x+e.rotationAngle;else if(e.transVecLabel2D&&d===R.PICTURE){var i,o,a,r=e.transVecLabel2D.clone(),s=I/D.width,l=C/D.height,c=e.ctrlKey?2:1;switch(r.setX(r.x*s*c),r.setY(r.y*l*c),e.handleType){case"TL":i=I-r.x,o=C-r.y,a=new t.Vector3(-1,-1);break;case"TR":i=I+r.x,o=C-r.y,a=new t.Vector3(-1,1);break;case"BL":i=I-r.x,o=C+r.y,a=new t.Vector3(1,-1);break;case"BR":i=I+r.x,o=C+r.y,a=new t.Vector3(1,1)}var h=Math.max(Math.abs(i/I),Math.abs(o/C));k.width=I*h,k.imgWidth=k.width-E.offsetWidth,k.height=C*h,k.imgHeight=k.height-E.offsetHeight;var u=new t.Vector2(Math.sin(k.rotationAngle),Math.cos(k.rotationAngle)).multiplyScalar(a.x*(k.height*Math.sign(o)-C)),p=new t.Vector2(Math.cos(k.rotationAngle),-Math.sin(k.rotationAngle)).multiplyScalar(a.y*(k.width*Math.sign(i)-I));r=u.add(p),e.ctrlKey?k.position=S:k.position=n.getPositionFromIntersection(N,V.clone().add(r.multiplyScalar(.5)),S)}}(e),T.updateRepresentation&&T.updateRepresentation(k,!1))},this.onEndManipulateCB=function(e){e&&e.event&&e.event.from&&e.event.from.length>0&&(k.mousePosition=e.event.from[e.event.from.length-1].getCurrentPosition()),T.updateRepresentation&&T.updateRepresentation(k,!0),k=T.getManipulationData(),Y()},!M){if(M=[],b=T.getNode().getRepNode(),d===R.PICTURE)for(var O=0;O<4;O++){var K,W,X=new i({viewer:N,sceneGraph:b.box,size:20});switch(X.subscribe("BeginManipulate",q),X.subscribe("Manipulate",J),X.subscribe("EndManipulate",Q),X.subscribe("BeginPreactivate",$),X.subscribe("EndPreactivate",function(){L.setStyle("cursor","")}),X.setCursor(!1),X.diskNode.setNoHighlightNoZ(!0),X.diskNode.setRenderPasses(["robot"]),X.manipulator.setPreActivationDuringManipulation(!1),O){case 0:K="TL",W="crosshair";break;case 1:K="TR",W="crosshair";break;case 2:K="BR",W="crosshair";break;case 3:K="BL",W="crosshair"}M.push({uid:X.manipulator.getUID(),type:K,handle:X,cursor:W,hlCursor:O})}var G=new i({viewer:N,sceneGraph:b.box,size:24});if(G.subscribe("BeginManipulate",q),G.subscribe("Manipulate",J),G.subscribe("EndManipulate",Q),G.subscribe("BeginPreactivate",$),G.subscribe("EndPreactivate",function(){L.setStyle("cursor","")}),G.setCursor(!1),G.diskNode.setNoHighlightNoZ(!0),G.manipulator.setPreActivationDuringManipulation(!1),G.manipulator.setPrePickingDuringManipulation(!1),G.manipulator.setPickingDuringManipulation(!1),G.pointMat.map=new t.ImageUtils.loadTexture(F+"DMURotationManip.png",void 0,null,null,t.ImageUtils.crossOriginPolicy),M.push({uid:G.manipulator.getUID(),type:"Rot",handle:G,cursor:"url("+F+"DMURotationCursorSel.png) 16 16, auto",hlCursor:"url("+F+"DMURotationCursorHL.png) 16 16, auto"}),d!==R.MEASURE)for(var Z=k.leaderPoints.length,j=0;j<Z;j++){var _=new i({viewer:N,sceneGraph:b.leader,size:20});_.subscribe("BeginManipulate",q),_.subscribe("Manipulate",J),_.subscribe("EndManipulate",Q),_.subscribe("BeginPreactivate",$),_.subscribe("EndPreactivate",function(){L.setStyle("cursor","")}),_.setCursor(!1),_.diskNode.setNoHighlightNoZ(!0),_.manipulator.setPreActivationDuringManipulation(!1),M.push({uid:_.manipulator.getUID(),type:"Leader",number:j,handle:_,cursor:"crosshair",hlCursor:"move"})}m||((m=new p).setPickable(o.PICKTHROUGH),m.setNoHighlightNoZ(!0),w||(w=c.createLineMaterial({color:new t.Color(11843258),opacity:1,lineWidth:window.devicePixelRatio,pattern:"6"})),b.box.addChild(m)),Y()}z.includes("2D")&&(g=a.subscribe({event:"/VISU/"},function(e){"@onWindowResized"!==e.eventType&&"@onViewpointChange"!==e.eventType||U.updateHandlePosition()})),this.updateHandlePosition=function(){k=T.getManipulationData(),E={offsetWidth:k.width-k.imgWidth,offsetHeight:k.height-k.imgHeight},Y()},this.setHandlesVisibility=function(e){M&&M.forEach(function(t){t.handle.setVisibility(e)}),m&&m.setVisibility(e)},this.remove=function(){M&&M&&M.forEach(function(e){e.handle.dispose()}),m&&(m.removeChildren(),b.box.removeChild(m)),A&&A.remove(),g&&a.unsubscribe(g),m=b=A=g=M=k=null,V=S=x=I=C=B=null}}function Y(){if(M){var e=function(e){for(var t=0;t<e;t++){var i=y.pop();y.splice(0,0,i)}};y=H.slice();var i=Math.sin(k.rotationAngle),o=Math.cos(k.rotationAngle);o<Math.cos(7*Math.PI/8)?e(4):o<Math.cos(Math.PI/8)&&(o>Math.cos(3*Math.PI/8)?e(i<0?7:1):o<Math.cos(5*Math.PI/8)?e(i<0?5:3):e(i<0?6:2));for(var a=1;a<5;a++)y.splice(a,1);for(var r=T.isZoomMode&&T.isZoomMode()?n.getMMPerPixelRatio(N,k.position):1,s="topleft"===k.anchorMode?0:r*k.height/2,c=-r*("topleft"===k.anchorMode?k.height:k.height/2),h="topleft"===k.anchorMode?0:-r*k.width/2,u=r*("topleft"===k.anchorMode?k.width:k.width/2),p=(h+u)/2,d=(new t.Matrix4).makeRotationZ(k.rotationAngle),g=0;g<M.length;g++)switch(M[g].type){case"Leader":M[g].handle.setMatrix((new t.Matrix4).setPosition(k.leaderPoints[M[g].number]));break;case"Rot":M[g].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(p,30*r+s,0))),M[g].handle.diskNode.setMatrix(d.clone().setPosition(new t.Vector3(-.5,-.5,0).applyMatrix4(d)));break;case"TL":M[g].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(h,s,0)));break;case"TR":M[g].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(u,s,0)));break;case"BL":M[g].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(h,c,0)));break;case"BR":M[g].handle.setMatrix((new t.Matrix4).setPosition(new t.Vector3(u,c,0)))}if(m){m.removeChildren();var f=l.createLineNode({points:[new t.Vector3(p,s,0),new t.Vector3(p,30*r+s,0)],fill:!1,material:w});f.excludeFromHighlight(!0,!0),m.addChild(f)}}}function J(i){if(i&&A.isCanvasToDraw(i.event.from[0].getView())){A.updateManipulationData(i);var o,a,r,s=i.event.from[0].getCurrentPosition(i.viewer.canvas);if("Leader"===f.type){if(i.intersection.position&&i.intersection.path.length){if(h.isAPOIPath(i.intersection.path[0])){const{position:e}=u.getPOIInformationFromPathElement(i.intersection.path[0]);e&&(o=e)}o||(o=n.getPositionFromIntersection(N,i.intersection.position,i.intersection.position,!0))}else o=n.getPositionFromIntersection(N,i.intersection.position,k.leaderPoints[f.number],!0);k.leaderPoints[f.number]=o}else if("Rot"===f.type){var l=(new t.Vector2).subVectors(s,V),c=new t.Vector2(1,0).dot(l.clone().normalize());a=s.y<=V.y?Math.acos(c):2*Math.PI-Math.acos(c);var p=i.shiftKey||void 0===i.shiftKey&&i.event.from&&i.event.from.length&&i.event.from[0].event&&i.event.from[0].event.shiftKey;if(v!==p){var d=Math.round(a/P)*P,M=new t.Vector2(Math.cos(d),-Math.sin(d));if(M.setLength(l.dot(M)),M.distanceTo(l)<A.ROTATION_SNAP_THRESHOLD){a=d;var g=k.position;if("topleft"===k.anchorMode){var m=T.isZoomMode&&T.isZoomMode()?n.getMMPerPixelRatio(N,k.position):1,w=i.viewer.currentViewpoint.getUpDirection(),b=(new t.Vector3).crossVectors(i.viewer.currentViewpoint.getSightDirection(),w);g=k.position.clone().add(b.multiplyScalar(m*k.width/2).add(w.multiplyScalar(m*-k.height/2)))}A.displayAxis(g,a,l.length()+20)}else A.removeAxis()}else A.removeAxis();a-=x+Math.PI/2}else{if(r=n.get2DSnappedPosition(s,k.rotationAngle).sub(B),k.rotationAngle){var D=r.x*Math.cos(k.rotationAngle)-r.y*Math.sin(k.rotationAngle),y=r.x*Math.sin(k.rotationAngle)+r.y*Math.cos(k.rotationAngle);r.setX(D),r.setY(y)}}var S=i.ctrlKey||void 0===i.ctrlKey&&i.event.from&&i.event.from.length&&i.event.from[0].event&&i.event.from[0].event.ctrlKey;e.onManipulateCB&&e.onManipulateCB({transVecLabel2D:r,rotationAngle:a,handleType:f.type,ctrlKey:S})}}function q(i){if(i){var a;T.setNodePickable(o.INVISIBLE);for(var r=0;r<M.length;r++)if(M[r].uid===i.manipulator.getUID()){f=M[r],L.setStyle("cursor",M[r].cursor),a=(new t.Vector3).getPositionFromMatrix(M[r].handle.getMatrixWorld());break}var s=parseFloat(localStorage.getItem("DMUSnapRotationAngle"));P=(isNaN(s)?90:s)*Math.PI/180,v="false"!==localStorage.getItem("DMUSnapRotationToggle"),V=n.getViewerPositionFromPoint(N,k.position),V=new t.Vector2(V.left,V.top),B=n.getViewerPositionFromPoint(N,a),B=new t.Vector2(B.left,B.top),"Rot"===f.type?A.addToListener("Shift",J):A.addToListener("Control",J),A.activate(),e.onBeginManipulateCB&&e.onBeginManipulateCB({labelParams:k})}}function Q(t){t&&(L.setStyle("cursor",""),A.remove(),T.setNodePickable(o.PICKABLE),e.onEndManipulateCB&&e.onEndManipulateCB())}function $(e){if(e)for(var t=0;t<M.length;t++)if(M[t].uid===e.manipulator.getUID()){"number"==typeof M[t].hlCursor?L.setStyle("cursor",y[t]):L.setStyle("cursor",M[t].hlCursor);break}}}})});