requirejs.config({paths:{react:"react/react.development","react-dom":"react-dom/react-dom.development"}}),define("DS/xCityBasicUtils/Utils",["DS/Visualization/ThreeJS_DS","DS/Visualization/PathElement","DS/VENTurfJS/turf.min","DS/Mesh/MeshUtils","DS/Visualization/SceneGraphFactory"],function(t,e,i,r,n){"use strict";var o=function(e,i){var r,n,o,s,a,l=void 0===i.min?0:i.min,c=void 0===i.max?1:i.max;e=Math.max(0,Math.min((e-l)/c,1));var h=void 0===i.minHue?0:i.minHue,u=void 0===i.maxHue?360:i.maxHue,f=void 0===i.sat?1:i.sat,p=(e*(u-h)+h)/360;function d(t,e,i){return i<0?i++:i>1&&i--,(i=255*(i<1/6?t+6*(e-t)*i:i<.5?e:i<2/3?t+(e-t)*(2/3-i)*6:t)|0)?(i=i.toString(16)).length>1?i:"0"+i:"00"}return 0===f?r=n=o=(e=127)?(e=e.toString(16)).length>1?e:"0"+e:"00":(r=d(s=1-(a=.5+f-.5*f),a,p+1/3),n=d(s,a,p),o=d(s,a,p-1/3)),new t.Color("#"+r+n+o)},s=function(t){return UWA.is(t)},a=function(t,e){if(t&&t.parents&&1===t.parents.length){var i=t.parents[0];i.notAllowedInPathElement||(a(i,e),e.push(i))}},l=function(t){var e=t.userData;if(s(e)){var i=e.records;if(s(i))return e}if(!s(t.parents)||t.parents.length<1||"DataModel"===t.name)return null;var r=t.parents[0];return s(r)?l(r):void 0},c=function(t){if(s(t.mesh3js)&&s(t.mesh3js.geometry)&&t.mesh3js.geometry.length>0){var e=t.mesh3js.geometry[0];if(s(e)&&s(e.drawingGroups)&&e.drawingGroups.length>0)return e.drawingGroups[0]}return null},h=function(t){if(t&&t.mesh3js&&t.mesh3js.geometry&&t.mesh3js.geometry.length>0)for(var e=0;e<t.mesh3js.geometry.length;e++){var i=t.mesh3js.geometry[e];if(i&&i.drawingGroups&&i.drawingGroups.length>0)for(var r=0,n=i.drawingGroups.length;r<n;r++){var o=i.drawingGroups[r];o.material&&o.material.map&&o.material.map.lods&&(o.material.map.usedTexture=-1)}}for(var s=0,a=t.children.length;s<a;s++)h(t.children[s])},u=function(t){var e,i,r,n={};if(1===t.nodeType){if(t.attributes.length>0){n["@attributes"]={};for(var o=0;o<t.attributes.length;o++){var a=t.attributes.item(o);n["@attributes"][a.nodeName]=a.nodeValue}}}else 3===t.nodeType&&(n=t.nodeValue);if(t.hasChildNodes())for(var l=0;l<t.childNodes.length;l++){var c=t.childNodes.item(l),h=c.nodeName,f=(i=void 0,r=void 0,i=(e=h).indexOf(":"),(r={}).name=e.substring(i+1),i>=0&&(r.prefix=e.substring(0,i)),r),p=f.name;"#text"===p?void 0===n[p]?n[p]=u(c):n[p]+=u(c):(void 0===n[p]&&(n[p]=[]),n[p].push(u(c))),s(f.prefix)&&(s(n[p].prefix)||(n[p].prefix=f.prefix))}return n};function f(t,e){var i;if(s(t)&&s(e)){var r,n=Object.keys(t),o=n.length;for(r=0;r<o;r++){var a,l=n[r],c=t[l],h=e[l];s(c)&&(s(h)?c!==h&&(a=c instanceof Object&&h instanceof Object?p(c,h):c):a=c,s(a)&&(s(i)||(i={}),i[l]=a))}}return i}function p(t,e){var i;i=f(t,e);var r=f(e,t);return s(i)&&(i=Object.assign({},i)),s(r)&&(s(i)||(i={}),i=Object.assign(i,r)),i}function d(t){let e;return"string"==typeof t?e=t.split("."):Array.isArray(t)&&(e=t),e}return{is:s,isBright:function(t){return this.computeBrightness(t)>140},isDark:function(t){return this.computeBrightness(t)<=140},computeBrightness:function(t){return 255*t.r*.299+255*t.g*.587+255*t.b*.114},clone:function(t){return JSON.parse(JSON.stringify(t))},cloneRecKeepingClasses:function(t){return Object.assign(Object.create(Object.getPrototypeOf(t)),t)},cloneQuad:function(t){return{I:t.I,J:t.J,LOD:t.LOD,dist:t.dist,key:t.key,leaf:t.leaf,timestamp:t.timestamp}},toDeg:function(t){return 57.29577951308232*t},toRad:function(t){return.017453292519943295*t},toColor:function(t,e,i,r){return o(t,{minHue:e,maxHue:i,sat:r}).getHex()},getQuadKey:function(t,e,i){return 1099511627776*t+1048576*e+i},endsWith:function(t,e){return-1!==t.indexOf(e,t.length-e.length)},loadTexture:function(e){var i=t.ImageUtils.loadTexture(e,null,null,null,!0);return i.wrapS=i.wrapS||1e3,i.wrapT=i.wrapT||1e3,i.anisotropy=i.anisotropy||8,i.minFilter=i.minFilter||1008,i.magFilter=i.magFilter||1006,i.repeat=i.repeat||{x:1,y:1},i},eulerAnglesFromQuaternion:function(e){var i,r,n,o=2*(e.w*e.x+e.y*e.z),s=1-2*(e.x*e.x+e.y*e.y);i=Math.atan2(o,s);var a=2*(e.w*e.y-e.z*e.x);r=Math.abs(a)>=1?(a>0?1:-1)*Math.PI/2:Math.asin(a);var l=2*(e.w*e.z+e.x*e.y),c=1-2*(e.y*e.y+e.z*e.z);return n=Math.atan2(l,c),new t.Vector3(i,r,n)},quaternionFromEulerAngles:function(e,i,r){var n=new t.Quaternion,o=Math.cos(.5*r),s=Math.sin(.5*r),a=Math.cos(.5*e),l=Math.sin(.5*e),c=Math.cos(.5*i),h=Math.sin(.5*i);return n.w=o*a*c+s*l*h,n.x=o*l*c-s*a*h,n.y=o*a*h+s*l*c,n.z=s*a*c-o*l*h,n},extractExtension:function(t){var e,i;return s(t)&&(i=(i=t.split("?")[0]).split(".")).length>1&&(e=i[i.length-1]),e},getUrbanPathElement:function(t){var i=[];return a(t,i),i.push(t),new e(i)},getColorGradient:o,getDataSource:l,displayVector:function(t){if(!this.is(t))return null;var e=" x: "+t.x.toFixed(3)+" y: "+t.y.toFixed(3);return this.is(t.z)&&(e+=" z: "+t.z.toFixed(3)),this.is(t.w)&&(e+=" w: "+t.w.toFixed(3))," "+e+" "},getAllMaterials:function(t){var e=[];s(t)&&t.traverse(function(t){if(t.material&&e.push(t.material),t.mesh3js){t.mesh3js.material&&e.push(t.mesh3js.material);var i=c(t);s(i.gas)&&e.push(i.gas),s(i.material)&&e.push(i.material)}});return e},isParent:function(t,e){return e.traverse(function(t,e){return t.id===e.parent.id},{parent:t},!0)},resetLODs:h,removeElementFromArray:function(t,e){t.splice(e,1)},getMainMaterial:function(t){if(s(t)){var e,i=t.length;for(e=0;e<i;e++){var r=t[e];if(!0===r.force)return r}return t[i-1]}},getFirstDrawingGroup:c,floatPack:function(t,e,i){s(t)||(t=0);var r=0;return e<0&&(e=0),i?t|=r=1*Math.pow(2,e):(r=1*Math.pow(2,e),t&=r=Math.pow(2,32)-1-r),t},parseXML:function(t){var e=null;if(window.DOMParser)try{e=(new DOMParser).parseFromString(t,"text/xml")}catch(t){e=null}else console.error("Could not parse XMLstring ");return e},xmlToJson:u,initColor:function(e){"object"==typeof e&&(e="rgb("+Math.floor(255*e.r)+","+Math.floor(255*e.g)+","+Math.floor(255*e.b)+")");return new t.Color(e)},sortObjectArraybyAttribute:function(t,e){t.sort(function(t,i){return t[e]<i[e]?-1:t[e]>i[e]?1:0})},simpleJSONCompare:function(t,e){return JSON.stringify(t)===JSON.stringify(e)},getDifferences:p,getParentTile:function t(e,i){var r={LOD:e.LOD-1,I:e.I>>1,J:e.J>>1};return s(i)&&r.LOD>i?t(r,i):r},isSameTile:function(t,e){return t.LOD===e.LOD&&e.I===t.I&&t.J===e.J},removeUrlEnd:function(t,e){var i=t.lastIndexOf(e);return t.slice(0,i)},startsWith:function(t,e){return 0===t.indexOf(e)},createUUID:function(){var t=(new Date).getTime();return"xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,function(e){var i=(t+16*Math.random())%16|0;return t=Math.floor(t/16),("x"==e?i:3&i|8).toString(16)})},getSubsetOfBinA:function t(e,i){var r;if(s(e)&&s(i)){var n,o=Object.keys(e),a=o.length;for(n=0;n<a;n++){var l,c=o[n],h=e[c],u=i[c];s(u)&&(l=h instanceof Object&&u instanceof Object?t(h,u):u),s(l)&&(s(r)||(r={}),r[c]=l)}}return r},getAllAttributeValues:function(t,e){if(s(t)){var i=[];return function t(e,i,r){var n,o,a,l;if(s(e))for(o=(n=Object.keys(e)).length,a=0;a<o;a++)(l=n[a])===i?r.push(e[i]):e[l]instanceof Object&&t(e[l],i,r)}(t,e,i),i}return null},logX:function(t,e){return Math.log(e)/Math.log(t)},compute2DPolygonCentroid:function(t){for(var e,i,r,n=t.length,o=t[0],s=0,a=0,l=0,c=0,h=n-1;c<n;h=c++)e=t[c],i=t[h],s+=r=(e.x-o.x)*(i.y-o.y)-(i.x-o.x)*(e.y-o.y),a+=(e.x+i.x-2*o.x)*r,l+=(e.y+i.y-2*o.y)*r;return{x:a/(r=3*s)+o.x,y:l/r+o.y}},computePolygonCentroidTurf:function(t){return i.pointOnSurface(t).geometry.coordinates},getBbox:function(t){return i.bbox(t)},drawCuboid:function(e,i,o,s,a=new r.Color(.8,.2,.6,.5)){new t.MeshBasicMaterial({color:new t.Color(a)});var l=n.createCuboidNode({cornerPoint:e,firstAxis:new t.Vector3(i,0,0),secondAxis:new t.Vector3(0,o,0),thirdAxis:new t.Vector3(0,0,s),color:a});return l.name="cube",xCity.get3DRootNode().addChild(l),l},isPolygonClockwise:function(t){for(var e=t,i=0,r=e.length-1,n=0;n<e.length;r=n++)i+=e[r].x*e[n].y-e[n].x*e[r].y;return.5*i>=0},projectPointOnDirection:function(e,i,r){var n=new t.Vector2(i.x,i.y).sub(e).clone().normalize(),o=r.clone().sub(e),s=n.dot(o);return e.clone().add(n.clone().setLength(s))},removeDuplicateInArray:function(t){var e=[];return t.forEach(function(t){e.includes(t)||e.push(t)}),e},isEnumerable:function(t){switch(typeof t){case"object":case"array":case"string":return!0;default:return!1}},getNewCreatedName:function(t){var e,i=t;i?i+=" ":i="";for(var r=1;r<1e3;r++)if(e=i+r,!xCity.findItem({name:e}))return e},sortByDistance:function(t,e){return t.sort((t,i)=>t.pos3D.distanceTo(e.getViewpoint().getPosition())-i.pos3D.distanceTo(e.getViewpoint().getPosition()))},getComposedProperty:function(t,e){let i=d(e);if(s(i))return i.reduce((t,e)=>s(t)?t[e]:void 0,t)},setComposedProperty:function(t,e,i){return function t(e,i,r){let n=i.shift();0!==i.length?(s(e[n])||(e[n]={}),t(e[n],i,r)):e[n]=r}(t,e.split("."),i)},getArrayFromComposedPropertyName:d,generateLabelPriorities:function(t,e,i){let r,n,o,s,a;try{a=Number(t.userData.get("Content").get("levelMin"))}catch(t){}try{s=Number(t.userData.get("Content").get("priority"))}catch(t){}try{e?(o=Number(e.get("StreamPriority")),r=Number(t.userData.get("Content").get("userData").cache.dataset.analytics.attributeList.filter(t=>"StreamPriority"===t.name)[0].min),n=Number(t.userData.get("Content").get("userData").cache.dataset.analytics.attributeList.filter(t=>"StreamPriority"===t.name)[0].max)):i&&(o=Number(i.StreamPriority))}catch(t){}return{levelMin:isNaN(a)?void 0:a,priority:isNaN(s)?void 0:s,streamPriority:isNaN(o)?void 0:o,streamPriorityMin:isNaN(r)?void 0:r,streamPriorityMax:isNaN(n)?void 0:n}},getItemType:function(t){let e=t.get("className");if("BuildingSet"===e)t.isHouse()&&(e+="House");else if(e.includes("RdbLink")){let i=t.get("userData"),r=t.get("factory"),n="";i["poi-dataset"]?n="_POI":"SimpleFeatureProxy"===i.datasetType?n="_WFS":"FactoryModel"===r&&"3DXM"===t.get("type")&&(n="_3DXM"),e+="s."+r+n}return e},coordinatesToTurfLineString:function(t){return i.lineString(t)},coordinatesToTurfPoint:function(t){return i.point(t)},coordinatesToTurfPolygon:function(t){return i.polygon(t)},turfBooleanContains:function(t,e){return i.booleanContains(t,e)},turfPointToLineDistance:function(t,e,r){return i.pointToLineDistance(t,e,r)},slideWithinRange:function(t,e,i,r,n){return t+e>r&&(i<r?t=r-e:(n&&(n.toHide=!0),t=i-e)),t},draw2Drect:function(t,e,i,r,n,o="#007700",s="#005500",a="rgba(0,100,0,0.1)"){var l=t.getContext("2d");l.strokeStyle=o,l.lineWidth=10,l.strokeRect(e,i,r,n),l.fillStyle=a,l.fillRect(e,i,r,n);var c=function(t,e,i="blue"){l.beginPath(),l.fillStyle=i,l.arc(t,e,4,0,2*Math.PI),l.fill()};c(e,i,s),c(e,i+n,s),c(e+r,i+n,s),c(e+r,i,s)},createDebugCanvas:function(){var t=document.createElement("CANVAS");t.style.position="absolute",xCity._urbanImpl.viewerContainer.appendChild(t),t.style.top=0,t.style.left=0,t.style.pointerEvents="none";var e=function(){t.width=window.innerWidth,t.height=window.innerHeight};return window.addEventListener("resize",e,!1),e(),t},structuredClone:function(t){return window.structuredClone?structuredClone(t):JSON.parse(JSON.stringify(t))},requestIdleCallback:function(t,e){window.requestIdleCallback?requestIdleCallback(t,e):setTimeout(t,e&&e.timeout?e.timeout:0)},addObserver:function(t,e){Utils.is(t.observers)||(t.observers=[]),t.observers.push(e)}}}),define("DS/xCityBasicUtils/AABBox",["DS/Visualization/ThreeJS_DS"],function(t){"use strict";var e=function(e,i){this._worldSize=1;var r={x:0,y:0};void 0!==i&&(this._worldSize=i.worldSize?i.worldSize:1,r.x=i.shiftedPosition?i.shiftedPosition.x:0,r.y=i.shiftedPosition?i.shiftedPosition.y:0),void 0!==e&&(void 0!==e.min&&void 0!==e.max?(this._min=e.min.slice(),this._max=e.max.slice(),this._size={x:this._max[0]-this._min[0],y:this._max[1]-this._min[1]},this._center={x:(this._min[0]+this._max[0])/2,y:(this._min[1]+this._max[1])/2},this._centerShifted={x:this._center.x-r.x,y:this._center.y-r.y},this._minShifted=[this._min[0]-r.x,this._min[1]-r.y],this._maxShifted=[this._max[0]-r.x,this._max[1]-r.y]):void 0!==e.center&&(void 0!==e.size||void 0!==e.width&&void 0!==e.height)&&(this._center={},this._size={},void 0!==e.center.x&&void 0!==e.center.y?(this._center.x=e.center.x,this._center.y=e.center.y):(this._center.x=e.center[0],this._center.y=e.center[1]),void 0!==e.size?void 0!==e.size.x&&void 0!==e.size.y?(this._size.x=e.size.x,this._size.y=e.size.y):this._size.x=this._size.y=e.size:(this._size.x=e.width,this._size.y=e.height),this._min=[this._center.x-this._size.x/2,this._center.y-this._size.y/2],this._max=[this._center.x+this._size.x/2,this._center.y+this._size.y/2],this._centerShifted={x:this._center.x-r.x,y:this._center.y-r.y},this._minShifted=[this._min[0]-r.x,this._min[1]-r.y],this._maxShifted=[this._max[0]-r.x,this._max[1]-r.y])),void 0===this._min&&void 0===this._max&&(this._min=[r.x,r.y],this._max=[r.x+this._worldSize,r.y+this._worldSize],this._minShifted=[0,0],this._maxShifted=[this._worldSize,this._worldSize],this._size={x:this._worldSize,y:this._worldSize},this._center={x:r.x+this._worldSize/2,y:r.x+this._worldSize/2},this._centerShifted={x:this._worldSize/2,y:this._worldSize/2}),this._sizeRatio=new t.Vector2(this._size.x/this._worldSize,this._size.y/this._worldSize),this._shiftedCenterRatio=new t.Vector2(this._centerShifted.x/this._worldSize,this._centerShifted.y/this._worldSize)};return e.prototype={toJSON:function(t){var e={};return e.min=this._min.slice(),e.max=this._max.slice(),e},contains:function(t){return!(this._min[0]>t._max[0]||this._max[0]<t._min[0]||this._min[1]>t._max[1]||this._max[1]<t._min[1])},intersectIJ:function(t,e,i){var r=this._worldSize/(1<<t),n=Math.abs(r*(e+.5)-this._centerShifted.x),o=Math.abs(r*(i+.5)-this._centerShifted.y);return n<.5*(r+this._size.x)&&o<.5*(r+this._size.y)},getIJBox:function(t,e){var i=1<<t,r=this._shiftedCenterRatio.x*i,n=this._shiftedCenterRatio.y*i,o=.5*this._sizeRatio.x*i,s=.5*this._sizeRatio.y*i;return e.IMin=Math.floor(r-o),e.IMax=Math.floor(r+o),e.JMin=Math.floor(n-s),e.JMax=Math.floor(n+s),e},getShiftedBBox:function(t,e,i){var r=this._worldSize/(1<<t),n=Math.abs(r*(e+0)),o=Math.abs(r*(e+1));return{xMin:n,yMin:Math.abs(r*(i+0)),xMax:o,yMax:Math.abs(r*(i+1))}},getSize:function(){return this._size},getCenter:function(){return this._center},getShiftedCenter:function(){return this._centerShifted},getMin:function(){return this._min},getShiftedMin:function(){return this._minShifted},getMax:function(){return this._max},getShiftedMax:function(){return this._maxShifted}},e}),define("DS/xCityBasicUtils/DataFormatTools",["UWA/Class","DS/xCityBasicUtils/Utils"],function(t,e){var i=t.singleton({fromWKTToArray:function(t){var e=t.replace(/^[^\(]*\(/,"(");return e="["+(e=(e=(e=(e=(e=e.replace(/\(/g,"[")).replace(/,\s*/g,"],[")).replace(/\)/g,"]")).replace(/[a-z]+\s/gi,"")).replace(/\s/g,","))+"]",JSON.parse(e)},fromArrayToWKT:function(t,i){var r;return e.is(t)&&(r=this._initWKT(t,i),1===t.length?r+=this._convertDataToString(t):r+="("+this._convertDataToString(t)+")"),r},_initWKT:function(t,e){var i="LINESTRING",r="POLYGON",n="MULTIPOLYGON",o="MULTILINESTRING",s="POINT";if(1===t.length&&!Array.isArray(t[0][0]))return s;if("Polygon"===e){if(1===t.length&&!Array.isArray(t[0][0][0][0]))return r;if(t.length>1&&!Array.isArray(t[0][0][0][0]))return n}if("Line"===e||"LineString"===e){if(1===t.length&&!Array.isArray(t[0][0][0]))return i;if(t.length>1&&!Array.isArray(t[0][0][0]))return o}},_convertDataToString:function(t){for(var e="",i=0;i<t.length;i++)Array.isArray(t[i])?(Array.isArray(t[0][0])?e+="("+this._convertDataToString(t[i])+")":e+=this._convertDataToString(t[i]),i!==t.length-1&&(e+=",")):(1==i&&(e+=" "),e+=t[i]);return e},fromJSONToArray:function(t){var i,r,n=[];if(e.is(t)&&e.is(t.features)&&Array.isArray(t.features))for(i=0;i<t.features.length;i+=1)r=t.features[i],e.is(r.geometry)&&e.is(r.geometry.coordinates)&&Array.isArray(r.geometry.coordinates)&&n.push(r.geometry.coordinates);return n},fromArrayToJSON:function(t,i){var r,n;if(e.is(t))for(r=this.initGeoJsonObject(),n=0;n<t.length;n+=1)r.features[n]=this._initFeature(),r.features[n].properties={STRID:UWA.Utils.getUUID()},r.features[n]&&r.features[n].geometry&&this._setGeoJSONGeometryAttributes(r.features[n].geometry,i,t[n]);return r},initGeoJsonObject:function(){return{type:"FeatureCollection",features:[]}},_initFeature:function(){return{type:"Feature",geometry:{coordinates:[]}}},_setGeoJSONGeometryAttributes:function(t,i,r){if(e.is(t)){switch(i.toLowerCase()){case"linestring":i="LineString";break;case"polygon":i="Polygon";break;case"multilinestring":i="MultiLineString";break;case"multipolygon":i="MultiPolygon";break;case"point":i="Point";break;case"multipoint":i="MultiPoint"}t.type=i,t.coordinates=r}},_needsLinearRing:function(t){var e=t.geometry.type.toLowerCase();return"polygon"===e||"multipolygon"===e},updateGeoJSON:function(t,i,r){if(e.is(t))if(e.is(r)){var n=r[0],o=r[1],s=r[2];if(!(f=t.features[n])){t.features[n]=this._initFeature(),(f=t.features[n]).properties={STRID:UWA.Utils.getUUID()};var a=t.features[0];a&&a.geometry&&a.geometry.type&&(f.geometry.type=a.geometry.type)}var l=f.geometry.coordinates;this._needsLinearRing(f)&&(i=this.closeLinearRings(i)),e.is(o)?e.is(s)?l[o][s]=i:l[o]=i:t.features[n].geometry.coordinates=i}else{for(var c=[],h=i.length,u=0;u<h;u++){var f;(f=t.features[u])||((f=this._initFeature()).properties={STRID:UWA.Utils.getUUID()}),f&&f.geometry&&this._setGeoJSONGeometryAttributes(f.geometry,"Point",i[u]),c[u]=f}t.features=c}},removeFromGeoJSON:function(t,i){if(e.is(i)&&e.is(t)){var r=i[0],n=i[1],o=i[2],s=t.features[r].geometry.coordinates;e.is(n)?e.is(o)?delete s[n][o]:delete s[n]:t.features[r].geometry.coordinates=[]}},updateFeatureType:function(t,i,r,n){if(e.is(r)&&e.is(t)){var o=r[0];r[1],r[2];if(t.features[o].geometry.type.toLowerCase()===n.toLowerCase())this.updateGeoJSON(t,i,r);else{var s=this.fromCoodinatesToJSON(i,n);t.features[o].geometry=s.features[0].geometry}}},_containsPoints:function(t){return t.length>0&&(2===t[0].length||3===t[0].length)&&"number"==typeof t[0][0]&&"number"==typeof t[0][1]},closeLinearRings:function(t){if(e.is(t))if(this._containsPoints(t)){var i=t[0],r=t[t.length-1];i[0]===r[0]&&i[1]===r[1]&&i[2]===r[2]||t.push(t[0].slice())}else for(var n=0;n<t.length;n++)t[n]=this.closeLinearRings(t[n]);return t},fromCoodinatesToJSON:function(t,e){var i;switch(e.toLowerCase()){case"linestring":e="LineString",i=this.fromArrayToJSON([t],e);break;case"multilinestring":e="MultiLineString",i=this.fromArrayToJSON([[t]],e);break;case"polygon":e="Polygon",t=this.closeLinearRings(t),i=this.fromArrayToJSON([[t]],e);break;case"multipolygon":e="MultiPolygon",t=this.closeLinearRings(t),i=this.fromArrayToJSON([[[t]]],e);break;case"point":e="Point",i=this.fromArrayToJSON(t,e);break;case"multipoint":e="MultiPoint",i=this.fromArrayToJSON([t],e)}return i},fromGeometryToBbox:function(t){let e=i.fromWKTToArray(t)[0];return{height:Math.abs(e[0][1]-e[3][1]),width:Math.abs(e[0][0]-e[1][0]),xmax:Math.max(e[0][0],e[1][0]),xmin:Math.min(e[0][0],e[1][0]),ymax:Math.max(e[0][1],e[3][1]),ymin:Math.min(e[0][1],e[3][1])}}});return i}),define("DS/xCityBasicUtils/FloatGradient",["UWA/Core","UWA/Class","DS/xCityBasicUtils/Utils","DS/Visualization/ThreeJS_DS","DS/Mesh/MeshUtils"],function(t,e,r,n,o){"use strict";var s=0;function a(t,e){r.is(t)||(t=0),"string"==typeof t&&(t=Number(t)),this.xCityid="xCityFloatGradient_"+s++,this.currentDefaultValue=0,this.defaultFloat=1,t instanceof a||"webworkerstitle"===self.document.title&&t.isFloatGradientForWorkers?this.initFromGradient(t,e):this.initFromStructure(t,e)}return a.prototype.constructor=a,a.prototype.setDefaultValue=function(t){r.is(t)&&(this.defaultFloat=t)},a.prototype.updateInterval=function(){r.is(this.floatDescription)&&(this.intervalMax=this.floatDescription[this.floatDescription.length-1][this.dataType],this.intervalMin=this.floatDescription[0][this.dataType])},a.prototype.initFromStructure=function(t,e){if(r.is(t.intervalMax)&&(this.intervalMax=t.intervalMax),r.is(t.intervalMin)&&(this.intervalMin=t.intervalMin),t.keyframes&&(t=this.initKeyframes(t.keyframes),this.interpolation=!0),void 0!==t.start&&void 0!==t.end){var n=[t.start];void 0!==t.mid&&n.push(t.mid),n.push(t.end),t=n}if("undefined"!==typeof t&&Array.isArray(t))if(void 0!==e&&("boolean"==typeof e.interpolation?this.interpolation=e.interpolation:"string"==typeof e.interpolation&&"conditional"===e.interpolation&&(this.conditional=!0,this.interpolation=!1)),this.interpolation||t.push(t[t.length-1]),t.length<2)r.is(t[0])&&r.is(t[0].float)?this.initFromfloat(t[0].float):this._defaultGradient();else if(this.floatDescription=this._initGradient(t),this.dataType=void 0,void 0!==t[0].percent&&(this.dataType="percent"),void 0!==t[0].value&&(this.dataType="value"),void 0===this.dataType&&(this.dataType="percent"),this.intervalMax=void 0===e||void 0===e.interval||void 0===e.interval.max?t[t.length-1][this.dataType]:e.interval.max,this.intervalMin=void 0===e||void 0===e.interval||void 0===e.interval.min?t[0][this.dataType]:e.interval.min,this.crop=void 0!==e&&void 0!==e.crop&&e.crop,void 0===this.intervalMax&&(this.intervalMax=1),void 0===this.intervalMin&&(this.intervalMin=0),!0===this.conditional){let e={};for(i=0;i<t.length;i++){let r=t[i];e[r.value]=r.float}this.floatDescription=e}else this.floatDescription=this._convertToPercent(this.floatDescription,this.intervalMin,this.intervalMax,this.dataType);else this._defaultGradient()},a.prototype.initFromfloat=function(t,e){r.is(t)?this.constantValue=Number(t):this.constantValue=null},a.prototype.initFromGradient=function(t,e){this.initFromfloat(t.constantValue),this.floatDescription=t.floatDescription,this.dataType=t.dataType,this.intervalMax=t.intervalMax,this.intervalMin=t.intervalMin,this.lastValue=t.lastValue,this.crop=t.crop,this.interpolation=t.interpolation,this.conditional=t.conditional,this.computeFloat()},a.prototype.initKeyframes=function(t){var e=[];if(r.is(t)){var i,n=t.length;for(i=0;i<n;i++){var o=t[i],s=Number(o.float),a={value:o.value};a.float=s,e.push(a)}r.sortObjectArraybyAttribute(e,"value")}return e},a.prototype.copy=function(t){var e=t;if(!(t instanceof a)){if("number"==typeof t)return void this.initFromfloat(t);e=new a(t)}this.initFromGradient(e)},a.prototype.clone=function(){return new a(this)},a.prototype.setLastValue=function(t){this.lastValue=t;this.computeFloat()},a.prototype.computeFloat=function(t){if(r.is(this.constantValue))return this.constantValue;var e=0,i=t;if(r.is(i)||(i=this.lastValue),r.is(i))if(!0===this.conditional)e=this._getFloatIfValue(i);else{var n=(i-this.intervalMin)/(this.intervalMax-this.intervalMin);if("number"==typeof n&&!isNaN(n)){if(this.crop&&(n>1||n<0))return;n=Math.max(n,0),n=Math.min(n,1),e=this.getLinearGradient(n)}}return e},a.prototype.getLinearGradient=function(t){if(void 0!==this.floatDescription){var e,i=!0;0===t&&(e=this._getfloatInGradient({percent:1,float:this.floatDescription[0].float}),i=!1),1===t&&(e=this._getfloatInGradient({percent:1,float:this.floatDescription[this.floatDescription.length-1].float}),i=!1);for(var r=0;i;)if(r++,t<this.floatDescription[r].percent){var n=this.floatDescription[r].percent-this.floatDescription[r-1].percent,o=(this.floatDescription[r].percent-t)/n,s=(t-this.floatDescription[r-1].percent)/n;e=this._getfloatInGradient({percent:1-o,float:this.floatDescription[r].float},{percent:1-s,float:this.floatDescription[r-1].float}),i=!1}return e}},a.prototype._defaultGradient=function(){this.interpolation=!1,this.floatDescription=this._initGradient([{percent:0,float:[1,0,0]},{percent:.5,float:[1,.5,0]},{percent:1,float:[0,1,0]}]),this.intervalMax=1e3,this.intervalMin=0,this.crop=!1},a.prototype._initGradient=function(t){for(var e=[],i=0;i<t.length;i++)e.push(this._checkfloatValues(t,t[i]));return e},a.prototype._checkfloatValues=function(t,e){return void 0===e||void 0===e.float?e={percent:this.currentDefaultValue,float:[1,0,0,1]}:void 0===e.value&&void 0===e.percent&&(e={percent:this.currentDefaultValue,float:e.float}),this.currentDefaultValue+=1/(t.length-1),e},a.prototype._getfloatInGradient=function(t,e){return void 0===e&&((e={}).percent=0,e.float=t.float),this.interpolation?t.percent*t.float+e.percent*e.float:e.float},a.prototype._getFloatIfValue=function(t){return this.floatDescription[t]},a.prototype._convertToPercent=function(t,e,i,r){for(var n=0;n<t.length;n++){var o=(t[n][r]-e)/(i-e);t[n].percent=o}return t},a.prototype.toJSON=function(){if(r.is(this.constantValue))return this.constantValue;if(r.is(this.floatDescription)){for(var t=[],e=0;e<this.floatDescription.length;e++){var i=this.floatDescription[e];t.push({float:"#"+i.float.getHexString(),value:i.value})}return{keyframes:t}}},a}),define("DS/xCityBasicUtils/ColorGradient",["UWA/Core","UWA/Class","DS/xCityBasicUtils/Utils","DS/Visualization/ThreeJS_DS","DS/Mesh/MeshUtils"],function(t,e,r,n,o){"use strict";var s=0;function a(t,e){n.Color.prototype.constructor.call(this),r.is(t)||(t=new n.Color),"string"==typeof t&&(t=new n.Color(t)),this.xCityid="xCityColorGradient_"+s++,this.currentDefaultValue=0,this.defaultColor=new n.Color("white"),t instanceof a||"webworkerstitle"===self.document.title&&t.isColorGradientForWorkers?this.initFromGradient(t,e):t instanceof o.Color||t instanceof n.Color?this.initFromColor(t,e):this.initFromStructure(t,e)}return a.prototype=Object.create(n.Color.prototype),a.prototype.constructor=a,a.prototype.setDefaultValue=function(t){r.is(t)&&t instanceof n.Color&&(this.defaultColor=t)},a.prototype.updateInterval=function(){r.is(this.colorsDescription)&&(this.intervalMax=this.colorsDescription[this.colorsDescription.length-1][this.dataType],this.intervalMin=this.colorsDescription[0][this.dataType])},a.prototype.initCosine=function(t){r.is(t)?(this.cosine={},this.cosine.DCOffset=new n.Vector3(1,1,1),this.cosine.Amplitude=new n.Vector3(1,1,1),this.cosine.Frequency=new n.Vector3(1,1,1),this.cosine.Phase=new n.Vector3(1,1,1),r.is(t.DCOffset)&&(3===t.DCOffset.length?this.cosine.DCOffset.set(t.DCOffset[0],t.DCOffset[1],t.DCOffset[2]):t.DCOffset instanceof n.Vector3&&this.cosine.DCOffset.copy(t.DCOffset)),r.is(t.Amplitude)&&(3===t.Amplitude.length?this.cosine.Amplitude.set(t.Amplitude[0],t.Amplitude[1],t.Amplitude[2]):t.Amplitude instanceof n.Vector3&&this.cosine.Amplitude.copy(t.Amplitude)),r.is(t.Frequency)&&(3===t.Frequency.length?this.cosine.Frequency.set(t.Frequency[0],t.Frequency[1],t.Frequency[2]):t.Frequency instanceof n.Vector3&&this.cosine.Frequency.copy(t.Frequency)),r.is(t.Phase)&&(3===t.Phase.length?this.cosine.Phase.set(t.Phase[0],t.Phase[1],t.Phase[2]):t.Phase instanceof n.Vector3&&this.cosine.Phase.copy(t.Phase))):this.cosine=null},a.prototype.initFromStructure=function(t,e){if(r.is(t.intervalMax)&&(this.intervalMax=t.intervalMax),r.is(t.intervalMin)&&(this.intervalMin=t.intervalMin),r.is(t.cosine))this.initCosine(t.cosine);else{if(t.keyframes&&(t=this.initKeyframes(t.keyframes),this.interpolation=!0),void 0!==t.start&&void 0!==t.end){var n=[t.start];void 0!==t.mid&&n.push(t.mid),n.push(t.end),t=n}if("undefined"!==typeof t&&Array.isArray(t))if(void 0!==e&&("boolean"==typeof e.interpolation?this.interpolation=e.interpolation:"string"==typeof e.interpolation&&"conditional"===e.interpolation&&(this.conditional=!0,this.interpolation=!1)),this.interpolation||t.push(t[t.length-1]),t.length<2)r.is(t[0])&&r.is(t[0].color)?this.initFromColor(t[0].color):this._defaultGradient();else if(this.colorsDescription=this._initGradient(t),this.dataType=void 0,void 0!==t[0].percent&&(this.dataType="percent"),void 0!==t[0].value&&(this.dataType="value"),void 0===this.dataType&&(this.dataType="percent"),this.colorFct=void 0===e||void 0===e.colorFct?void 0:e.colorFct,this.intervalMax=void 0===e||void 0===e.interval||void 0===e.interval.max?t[t.length-1][this.dataType]:e.interval.max,this.intervalMin=void 0===e||void 0===e.interval||void 0===e.interval.min?t[0][this.dataType]:e.interval.min,this.crop=void 0!==e&&void 0!==e.crop&&e.crop,this.HSV=void 0!==e&&void 0!==e.HSV&&e.HSV,void 0===this.intervalMax&&(this.intervalMax=1),void 0===this.intervalMin&&(this.intervalMin=0),!0===this.conditional){let e={};for(i=0;i<t.length;i++){let r=t[i];e[r.value]=r.color}this.colorsDescription=e}else this.colorsDescription=this._convertToPercent(this.colorsDescription,this.intervalMin,this.intervalMax,this.dataType);else this._defaultGradient();if(this.HSV){var o=this._rgb2hsv(t[0].color),s=this._rgb2hsv(t[t.length-1].color);this.hsvOptions={min:this.intervalMin,max:this.intervalMax,minHue:o.hue,maxHue:s.hue,sat:Math.max(o.sat,s.sat)}}}},a.prototype.initFromColor=function(t,e){r.is(t)?(this.constantValue=new n.Color,this.constantValue.setRGB(t.r,t.g,t.b),this.setRGB(this.constantValue.r,this.constantValue.g,this.constantValue.b)):this.constantValue=null},a.prototype.initFromGradient=function(t,e){this.initFromColor(t.constantValue),this.colorsDescription=t.colorsDescription,this.dataType=t.dataType,this.intervalMax=t.intervalMax,this.intervalMin=t.intervalMin,this.lastValue=t.lastValue,this.crop=t.crop,this.interpolation=t.interpolation,this.conditional=t.conditional,this.initCosine(t.cosine),this.computeColor()},a.prototype.initKeyframes=function(t){var e=[];if(r.is(t)){var i,n=t.length;for(i=0;i<n;i++){var o=t[i],s=r.initColor(o.color),a={value:o.value};a.color=s,e.push(a)}r.sortObjectArraybyAttribute(e,"value")}return e},a.prototype.copy=function(t){var e=t;if(!(t instanceof a)){if(t instanceof n.Color)return n.Color.prototype.copy.call(this,t),void this.initFromColor(t);e=new a(t)}this.initFromGradient(e)},a.prototype.clone=function(){return new a(this)},a.prototype.setLastValue=function(t){this.lastValue=t;var e=this.computeColor();r.is(e)&&this.setRGB(e.r,e.g,e.b)},a.prototype.computeColor=function(t){if(r.is(this.constantValue))return this.constantValue.clone();var e=new n.Color,i=t;if(r.is(i)||(i=this.lastValue),r.is(i))if(!0===this.conditional)e=this._getColorIfValue(i);else{var o=(i-this.intervalMin)/(this.intervalMax-this.intervalMin);if("number"==typeof o&&!isNaN(o)){if(this.crop&&(o>1||o<0))return;if(this.HSV){var s=r.getColorGradient(i,this.hsvOptions);return[s.r,s.g,s.b,this.hsvOptions.sat]}o=Math.max(o,0),o=Math.min(o,1),e=this.cosine?this.getCosineGradient(o):this.getLinearGradient(o)}}return r.is(e)&&this.setRGB(e.r,e.g,e.b),e},a.prototype.getLinearGradient=function(t){if(void 0!==this.colorsDescription){var e,i=!0;0===t&&(e=this._getColorInGradient({percent:1,color:this.colorsDescription[0].color}),i=!1),1===t&&(e=this._getColorInGradient({percent:1,color:this.colorsDescription[this.colorsDescription.length-1].color}),i=!1);for(var r=0;i;)if(r++,t<this.colorsDescription[r].percent){var n=this.colorsDescription[r].percent-this.colorsDescription[r-1].percent,o=(this.colorsDescription[r].percent-t)/n,s=(t-this.colorsDescription[r-1].percent)/n;e=this._getColorInGradient({percent:1-o,color:this.colorsDescription[r].color},{percent:1-s,color:this.colorsDescription[r-1].color}),i=!1}return e}},a.prototype._getCosineComposant=function(t,e,i,r,n){return t+e*Math.cos(6.28318*(i*n+r))},a.prototype.getCosineGradient=function(t){var e,i,r,o=this.cosine.DCOffset,s=this.cosine.Amplitude,a=this.cosine.Frequency,l=this.cosine.Phase;return e=this._getCosineComposant(o.x,s.x,a.x,l.x,t),i=this._getCosineComposant(o.y,s.y,a.y,l.y,t),r=this._getCosineComposant(o.z,s.z,a.z,l.z,t),(new n.Color).setRGB(e,i,r)},a.prototype._defaultGradient=function(){this.interpolation=!1,this.colorsDescription=this._initGradient([{percent:0,color:[1,0,0]},{percent:.5,color:[1,.5,0]},{percent:1,color:[0,1,0]}]),this.intervalMax=1e3,this.intervalMin=0,this.crop=!1},a.prototype._initGradient=function(t){for(var e=[],i=0;i<t.length;i++)e.push(this._checkColorValues(t,t[i]));return e},a.prototype._checkColorValues=function(t,e){return void 0===e||void 0===e.color?e={percent:this.currentDefaultValue,color:[1,0,0,1]}:void 0===e.value&&void 0===e.percent&&(e={percent:this.currentDefaultValue,color:e.color}),this.currentDefaultValue+=1/(t.length-1),e},a.prototype._getColorInGradient=function(t,e){if(void 0===e&&((e={}).percent=0,e.color=t.color),this.interpolation){var i=t.percent*t.color.r+e.percent*e.color.r,r=t.percent*t.color.g+e.percent*e.color.g,o=t.percent*t.color.b+e.percent*e.color.b;return(new n.Color).setRGB(i,r,o)}return e.color},a.prototype._getColorIfValue=function(t){return this.colorsDescription[t]},a.prototype._convertToPercent=function(t,e,i,r){for(var n=0;n<t.length;n++){var o=(t[n][r]-e)/(i-e);t[n].percent=o}return t},a.prototype._rgb2hsv=function(t){var e=t[0],i=t[1],r=t[2],n=Math.min(e,Math.min(i,r)),o=Math.max(e,Math.max(i,r));return{hue:60*((e==n?3:r==n?1:5)-(e==n?i-r:r==n?e-i:r-e)/(o-n)),sat:(o-n)/o,val:o}},a.prototype.getStyle=function(){return this.computeColor(),n.Color.prototype.getStyle.call(this)},a.prototype.getHexString=function(){return this.computeColor(),n.Color.prototype.getHexString.call(this)},a.prototype.setStyle=function(t){n.Color.prototype.setStyle.call(this,t),this.initFromColor(this)},a.prototype.toJSON=function(){if(r.is(this.constantValue))return"#"+this.constantValue.getHexString();if(r.is(this.colorsDescription)){for(var t=[],e=0;e<this.colorsDescription.length;e++){var i=this.colorsDescription[e];t.push({color:"#"+i.color.getHexString(),value:i.value})}return{keyframes:t}}this.cosine&&console.error("save of cosine gradient not implemented")},a}),define("DS/xCityBasicUtils/LoadingProgressList",["UWA/Class","DS/xCityBasicUtils/Utils"],function(t,e){"use strict";return t.extend({init:function(t,i){this.loaded=0,this.total=0,this.loading={},e.is(t)&&(this._progressCallback=t),e.is(i)&&(this._loadDoneCallback=i)},updateLoading:function(t,i){e.is(this.loading[t])&&(this.loaded-=this.loading[t].loaded,this.total-=this.loading[t].total),e.is(i)&&(this.loading[t]=i,this.loaded+=i.loaded,this.total+=i.total),this.updateProgressBar()},modelLoaded:function(t){var i=1;e.is(this.loading[t])&&(i=this.loading[t].total),this.updateLoading(t,{loaded:i,total:i})},updateProgressBar:function(){e.is(this._progressCallback)&&this._progressCallback(this.getProgress()),(this.total<=0||this.loaded>=this.total)&&(e.is(this._loadDoneCallback)&&this._loadDoneCallback(this.getProgress()),this.loaded=0,this.total=0,this.loading={})},getProgress:function(){return{loaded:this.loaded,total:this.total}}})});