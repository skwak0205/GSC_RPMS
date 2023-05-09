define("DS/DMUBaseUI/DMUToolsUsers",["require","exports","DS/WAFData/WAFData","DS/PADUtils/PADUtilsServices","DS/Visualization/ThreeJS_DS","DS/PADUtils/PADSettingsMgt","UWA/Core"],function(e,t,n,o,r,a,i){"use strict";let s=[],c=[],l=[16007990,15277667,10233776,6765239,4149685,2201331,240116,48340,38536,5025616,9159498,13491257,16771899,16761095,16750592,16733986,7951688,6323595];return class{static getUserLogin(){return o.getUserLogin()}static getUserName(e,t){return new Promise(r=>{if(!e)return void r("");if(e===o.getUserLogin()){const e=o.getUserName();return t&&t(e),r(e)}let i=s.find(function(t){return t.login===e});if(i&&i.name)return t&&t(i.name),r(i.name);let c=function(t){(i=s.find(t=>t.login===e))?i.name=t:s.push({login:e,name:t})};n.authenticatedRequest(o.get3DSpaceUrl()+"/resources/modeler/pno/person/"+e+"?tenant="+o.getPlatformId(),{method:"GET",headers:{Accept:"application/json","Content-Type":"application/json",SecurityContext:a.getSetting("pad_security_ctx")},timeout:6e4,onComplete:function(n){let o=JSON.parse(n),a=e;o&&o.firstname&&(a=o.firstname,a+=o.lastname?" "+o.lastname:""),c(a),t&&t(a),r(a)},onFailure:function(){c(e),r(e)},onTimeout:function(){c(e),r(e)}})})}static getUserColor(e){if(!e)return;if(e===o.getUserLogin())return new r.Color(7913210);let t,n=s.find(function(t){return t.login===e});if(n&&n.color)return n.color;let a,i=function(e){let t=Math.min(e,255).toString(16);return 1===t.length&&(t="0"+t),t},c=[];for(a=0;a<e.length;a++)c[a%3]?c[a%3]=Math.floor(((e.charCodeAt(a)-48)/74*255+c[a%3])/2):c[a%3]=Math.floor((e.charCodeAt(a)-48)/74*255);let u=parseInt(i(c[0])+i(c[1])+i(c[2]),16);if(l&&l.length){let o=l[0];return l.forEach(function(e){o=Math.abs(e-u)<Math.abs(o-u)?e:o}),t=new r.Color(o),n?n.color=t:s.push({login:e,color:t}),l.splice(l.indexOf(o),1),n?n.color=t:s.push({login:e,color:t}),t}let h=(t=new r.Color).getHSL();return h.s=Math.max(h.s,.5),h.l=h.l<.5?.5:h.l>.7?.7:h.l,t.setHSL(h.h,h.s,h.l),n?n.color=t:s.push({login:e,color:t}),t}static getImage(e,t){if(e&&t){for(let n=0;n<c.length;n++)if(c[n].url===e)return void t(c[n].stream);n.authenticatedRequest(e,{proxy:"none",type:"blob",timeout:18e4,onComplete:function(n){let o=new FileReader;o.onload=function(){c.push({url:e,stream:o.result}),t(o.result)},o.readAsDataURL(n)},onFailure:function(){t(e,!0)},onTimeout:function(){t(e,!0)}})}}static getUserImageURL(e){if(!e)return;let t=s.find(function(t){return t.login===e});if(t&&t.imageUrl)return t.imageUrl;let n=o.getPlatformURL("3DSwym")+"/api/user/getpicture/login/"+e+"/format/mini";return t?t.imageUrl=n:s.push({login:e,imageUrl:n}),n}static formatDate(e){if(e)return i.String.parseRelativeTime(e.toString())}}}),define("DS/DMUBaseUI/DMUContextualBarAdapter",["UWA/Class","DS/DMUBaseExperience/EXPManagerSet"],function(e,t){"use strict";return e.extend({getSharedContextualCommandList:function(){return[]},getContextualCommandList:function(){return null},getSharedContextualMenuCommandList:function(){return[]},getContextualMenuCommandList:function(){return[]},getSharedHeaderTypes:function(){return{}},register:function(e){if(e){var n=t.getManager({name:"DMUUserExperienceManager",context:e.frmWindow});n&&(e.contextualBar&&n.registerContextualBar(e.contextualBar),e.contextualMenu&&n.registerContextualMenu(e.contextualMenu))}},unregister:function(e){if(e){var n=t.getManager({name:"DMUUserExperienceManager",context:e.frmWindow});n&&(e.contextualBar&&n.unregisterContextualBar(e.contextualBar),e.contextualMenu&&n.unregisterContextualMenu(e.contextualMenu))}}})}),define("DS/DMUBaseUI/DMUToolsLeader",["DS/DMUMeasurable/DMUToolsMaths","DS/DMUMeasurable/DMUMathsInfiniteGeometry","DS/DMUBaseExperience/EXPToolsVisualization","DS/Visualization/ThreeJS_DS","DS/SceneGraphNodes/ArrowNode","DS/DMUBaseExperience/EXPToolsMaterial"],function(e,t,n,o,r,a){"use strict";let i={attachment:{CENTER:0,LEFT:1,RIGHT:2,TOP:3,BOTTOM:4},_computeNewPoints:function(e,t,r,a,s,c){var l={};l.vBreak=t.clone(),l.vAttach=t.clone();var u,h,d=a&&void 0!==a.width?s.offsetWidth*a.width:15,f=a&&void 0!==a.height?s.offsetHeight*a.height:0;s.offsetHeight?h=s.offsetHeight/2:s.style&&s.style.pixelHeight&&(h=s.style.pixelHeight/2),s.offsetWidth?u=s.offsetWidth/2:s.style&&s.style.pixelWidth&&(u=s.style.pixelWidth/2);var g=n.getWindowPositionFromPoint(e,t,c);g=new o.Vector2(g.left,g.top);var m=n.getWindowPositionFromPoint(e,r,c);m=new o.Vector2(m.left,m.top);var M,p,D,v,w=(new o.Vector2).subVectors(m,g),P=u+d,S=h+f,x=s.rotationAngle?s.rotationAngle:0;if(w=new o.Vector2(w.x*Math.cos(x)-w.y*Math.sin(x),w.x*Math.sin(x)+w.y*Math.cos(x)),Math.abs(w.y)<h&&Math.abs(w.x)<u)return l.vBreak=null,l.vAttach=null,l;if(w.x>0){var T=(new o.Vector2).addVectors(g,new o.Vector2(P*Math.cos(x),-P*Math.sin(x)));w.y>0?(D=(new o.Vector2).addVectors(g,new o.Vector2(-S*Math.sin(x+Math.PI),-S*Math.cos(x+Math.PI))),m.distanceToSquared(T)<m.distanceToSquared(D)?(p=T,M=i.attachment.RIGHT):(p=D,M=i.attachment.BOTTOM)):(v=(new o.Vector2).addVectors(g,new o.Vector2(-S*Math.sin(x),-S*Math.cos(x))),m.distanceToSquared(T)<m.distanceToSquared(v)?(p=T,M=i.attachment.RIGHT):(p=v,M=i.attachment.TOP))}else{var U=(new o.Vector2).addVectors(g,new o.Vector2(P*Math.cos(x+Math.PI),-P*Math.sin(x+Math.PI)));w.y>0?(D=(new o.Vector2).addVectors(g,new o.Vector2(-S*Math.sin(x+Math.PI),-S*Math.cos(x+Math.PI))),m.distanceToSquared(U)<m.distanceToSquared(D)?(p=U,M=i.attachment.LEFT):(p=D,M=i.attachment.BOTTOM)):(v=(new o.Vector2).addVectors(g,new o.Vector2(-S*Math.sin(x),-S*Math.cos(x))),m.distanceToSquared(U)<m.distanceToSquared(v)?(p=U,M=i.attachment.LEFT):(p=v,M=i.attachment.TOP))}if(l.vBreak=i._computeProjection(e,t,p.x,p.y),M===i.attachment.RIGHT||M===i.attachment.LEFT){M===i.attachment.LEFT&&(x+=Math.PI);var y=new o.Vector2(u*Math.cos(x),u*Math.sin(x));l.vAttach=i._computeProjection(e,t,g.x+y.x,g.y-y.y)}else if(M===i.attachment.TOP||M===i.attachment.BOTTOM)if(f){M===i.attachment.TOP&&(x+=Math.PI);var V=new o.Vector2(-h*Math.sin(x),h*Math.cos(x));l.vAttach=i._computeProjection(e,t,g.x-V.x,g.y+V.y)}else l.vAttach=l.vBreak.clone();return l},_computeProjection:function(e,r,a,i){var s=n.getViewpointInfo(e.currentViewpoint).sight.negate(),c=n.getLineFromWindowPosition(e,a,i),l=t.computeLinePlaneDistance(c.position.toArray(),c.direction.toArray(),r.toArray(),s.toArray());return new o.Vector3(l.aPosition[0],l.aPosition[1],l.aPosition[2])}};return Object.freeze({modifyLeaderNode:function(t,n,o,s,c,l,u,h,d,f,g,m){var M=!1,p={};if(p.vBreak=s.clone(),p.vAttach=s.clone(),o&&(p=i._computeNewPoints(t,s,c,l,g,m)),null===p.vBreak&&null===p.vAttach)return M;var D=a.getDashType(d),v={arrowWidth:u,headLength:Math.max(2*u+4,10),viewpoint:t.currentViewpoint,arrowDashType:D,arrowScale:2,color:h},w=!e.areEqual(p.vBreak,c,"Vector3"),P=n.getChildByName("attach"),S=Math.max(2*u+4,10);P?w?(p.vBreak.distanceTo(P._initialPoint)&&P.updateInitialPosition(p.vBreak),c.distanceTo(P._finalPoint)&&P.updateFinalPosition(c),h!==P.materialArrow.color&&P.updateColor(h),u!==P.materialArrow.lineWidth&&P.setArrowWidth(u),S!==P._headLength&&P.setHeadSize(S),f!==P._head&&P.setHeadType(f),D!==P.savedPattern&&(P.savedPattern=D,P.setArrowPattern(D)),2!==P.materialArrow.scale&&P.setArrowPatternScale(2),M=!0):(n.removeChild(P),M=!0):w&&(v.initialPoint=p.vBreak.clone(),v.finalPoint=c.clone(),v.head=f,(P=new r(v)).setPickParent(!0),P.name="attach",n.addChild(P),M=!0);var x=w&&!e.areEqual(p.vBreak,p.vAttach,"Vector3"),T=n.getChildByName("step");return T?x?(p.vBreak.distanceTo(T._initialPoint)&&T.updateInitialPosition(p.vBreak),p.vAttach.distanceTo(T._finalPoint)&&T.updateFinalPosition(p.vAttach),h!==T.materialArrow.color&&T.updateColor(h),u!==T.materialArrow.lineWidth&&T.setArrowWidth(u),S!==T._headLength&&T.setHeadSize(S),0!==T._head&&T.setHeadType(0),D!==T.savedPattern&&(T.savedPattern=D,T.setArrowPattern(D)),2!==T.materialArrow.scale&&T.setArrowPatternScale(2),M=!0):(n.removeChild(T),M=!0):x&&(v.initialPoint=p.vBreak.clone(),v.finalPoint=p.vAttach.clone(),v.head=0,(T=new r(v)).setPickParent(!0),T.name="step",n.addChild(T),M=!0),M}})}),define("DS/DMUBaseUI/DMUNode3D",["DS/Visualization/Node3D","DS/Mesh/MeshUtils"],function(e,t){"use strict";return e.extend({init:function(e){this._parent();var n=e||{},o=this;this.getDMUType=function(){return"function"==typeof n.getType?n.getType():""},this.getViewer=function(){return n.frmWindow?n.frmWindow.getViewer():null},this.setReadOnly=function(e){o.setPickable(e?t.PICKTHROUGH:t.PICKABLE)},this.mergeParams=function(e,t){for(var n=function(e){return null!=e&&e.clone?e.clone():e},o=Object.keys(t),r=0;r<o.length;r++)if("graphicProperties"!==o[r])e[o[r]]=n(t[o[r]]);else for(var a=Object.keys(t[o[r]]),i=0;i<a.length;i++)if("fontStyle"===a[i]||"fillStyle"===a[i]||"fillStyle"===a[i])for(var s=Object.keys(t[o[r]][a[i]]),c=0;c<s.length;c++)e[o[r]][a[i]][s[c]]=n(t[o[r]][a[i]][s[c]]);else e[o[r]][a[i]]=n(t[o[r]][a[i]]);return e}},remove:function(){this.removeDMUNode3DOverload&&this.removeDMUNode3DOverload(),this._parent()}})}),define("DS/DMUBaseUI/DMUManipulableNode3D",["DS/DMUBaseUI/DMUNode3D"],function(e){"use strict";return e.extend({init:function(e){this._parent(e),this.setNoZ(!0);var t,n,o=this;this.setRepManipulator=function(e){e&&(t&&t.destroy(),(t=e)&&n&&t.linkToNode(n))},this.getRepManipulator=function(){return t},this.linkManipulatorToNode=function(e){n=e},this.removeDMUNode3DOverload=function(){t&&(t.destroy(),t=null),n=null,o.removeChildren&&o.removeChildren()}},remove:function(){this.removeDMUNode3DOverload&&this.removeDMUNode3DOverload(),this._parent()}})}),define("DS/DMUBaseUI/DMUNode2D",["DS/DMUBaseUI/DMUManipulableNode3D","DS/DMUBaseExperience/EXPToolsVisualization","DS/Visualization/ThreeJS_DS"],function(e,t,n){"use strict";return e.extend({init:function(e){this._parent(e);var o=this;this.setExcludeFromBounding(!0);var r=1;this.setMaximalProportionalZoneWidth=function(e){r=e},this.getProportionalFactor=function(e){var t=o.getViewer().SCREEN_WIDTH/2,n=o.getViewer().SCREEN_HEIGHT/2,a=t/n;if(e&&a<r){var i=Math.pow((r-a)/r,3);return Math.round(a/r*n*i+n*(1-i))}return n},this.convertPixelsInProportionalCoord=function(e,t){return e/o.getProportionalFactor(t)/window.devicePixelRatio},this.convertProportionalCoordInPixels=function(e,t){return e*o.getProportionalFactor(t)*window.devicePixelRatio},this.convertCoordsInPixels=function(e){var t=o.getViewer().SCREEN_WIDTH/2,r=o.getProportionalFactor(e.fitOnX);return"center"===e.windowAnchor?new n.Vector2(t+e.coords.x*r,(1-e.coords.y)*r+(o.getViewer().SCREEN_HEIGHT/2-r)):new n.Vector2(e.coords.x*r,e.coords.y*r)},this.get3DPositionFromScreenCoordinates=function(e){if(e.coords){var a=(new n.Vector2).copy(e.coords),i=o.getProportionalFactor();let h=o.getViewer().SCREEN_HEIGHT/2;if(e.proportionalPosition&&"center"===e.windowAnchor){var s=o.getViewer().SCREEN_WIDTH/2,c=s/h,l=r*h>s?a.x/r*c:a.x;if(a.x=s+l*i,a.y=(1-a.y)*i+(h-i),e.width&&"center"===e.anchor){var u=e.width/2;a.x-u<0&&(a.x=u),a.x+u>2*s&&(a.x=2*s-u)}}let d=o.getViewer().getSceneBoundingSphere();return t.getPositionFromIntersection(o.getViewer(),a,d?d.center:new n.Vector3)}return new n.Vector3(1,0,0)}}})});