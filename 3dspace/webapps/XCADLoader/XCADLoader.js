define("DS/XCADLoader/XCADGeomLoader",["DS/Mesh/Mesh","DS/Mesh/ThreeJS_Base","DS/XCADInputDocuments/XCADInputDocument","DS/XCADInputDocuments/XCADRepresentation","DS/XCADInputDocuments/XCADTessellatedRepresentation"],function(e,t,r,n,i){"use strict";function s(e,t){this.primitive=e,this.transfo=t,this.descendants=[]}var o=function(){this.DEFAULT_COLOR=new e.Color(.825,.825,1),this.DEFAULT_WIRE_COLOR=new e.Color(0,0,0),t.EventDispatcher.call(this),this.currentObject=new e.PrimitiveGroup,this.posBuffer=new e.Buffer,this.normBuffer=new e.Buffer,this.posIdxBuffer=new e.Buffer,this.normIdxBuffer=new e.Buffer,this.meshNb=0,this.primitivesNode=[],this.rootNode=new s(this.currentObject),this.primitivesNode.push(this.rootNode)};const a="WIRE_EDGE",l="BOUNDARY_EDGE";return o.prototype={constructor:o,load:function(t,r){switch(this.geomRepresentation=t,this.progressCB=r,this.currentObject.fillAttr=new e.FillAttributes,this.currentObject.lineAttr=new e.LineAttributes,this.currentObject.PointAttr=new e.PointAttributes,this.meshNb=0,this.geomRepresentation.getRepresentationType()){case n.XCAD_TESSELLATED_REPRESENTATION:this.processTessRep()}return this.primitivesNode},startNewMesh:function(t){this.posBuffer=new e.Buffer,this.posBuffer.component=e.VertexComponentEnum.POSITION,this.posBuffer.format=e.DataTypeEnum.FLOAT,this.posBuffer.dimension=3,this.normBuffer=new e.Buffer,this.normBuffer.component=e.VertexComponentEnum.NORMAL,this.normBuffer.format=e.DataTypeEnum.FLOAT,this.normBuffer.dimension=3,this.currentObject.addBuffer(3*this.meshNb,this.posBuffer),this.currentObject.addBuffer(3*this.meshNb+1,this.normBuffer),!0===t&&this.geomRepresentation.isIndexed&&this.geomRepresentation.isIndexed()&&(this.posIdxBuffer=new e.Buffer,this.posIdxBuffer.indexBuffer=!0,this.posIdxBuffer.format=e.DataTypeEnum.UINT,this.posIdxBuffer.dimension=1,this.currentObject.addBuffer(3*this.meshNb+2,this.posIdxBuffer))},finishCurrentMesh:function(){this.meshNb++},processTessRep:function(){for(var e=this.geomRepresentation.getRootNodesCount(),t=0;t<e;t++){this.progressCB&&this.progressCB({loaded:t,total:e});var r=this.geomRepresentation.getRootNode(t);let s=[];r instanceof Array?s=r:s[0]=r;for(var n=0;n<s.length;n++){switch(this.geomRepresentation.getNodeType(s[n])){case i.KindOfNode.NOD_ContainerNode:this.processTessContainer(s[n]);break;case i.KindOfNode.NOD_SolidNode:null!=this.rootNode.primitive&&(this.currentObject=this.rootNode.primitive),this.processTessSolid(s[n]);break;case i.KindOfNode.NOD_SurfacicNode:null!=this.rootNode.primitive&&(this.currentObject=this.rootNode.primitive),this.processTessSurfacic(s[n])}}}},processTessContainer:function(t,r){this.currentObject=new e.PrimitiveGroup;var n=this.geomRepresentation.getAssociatedTransfo(t),o=new s(this.currentObject,n);null!=r?r.descendants.push(o):this.primitivesNode.push(o);for(var a=this.geomRepresentation.getChildren(t,!0),l=0;l<a.length;l++){var d=a[l];switch(this.geomRepresentation.getNodeType(d)){case i.KindOfNode.NOD_SurfacicNode:this.currentObject=o.primitive,this.processTessSurfacic(d);break;case i.KindOfNode.NOD_SolidNode:this.currentObject=o.primitive,this.processTessSolid(d);break;case i.KindOfNode.NOD_ContainerNode:this.processTessContainer(d,o)}}},processTessSurfacic:function(t){for(var r,n=this.geomRepresentation.getNodeColor(t),s=this.geomRepresentation.getPolyLineData(t),o=this.geomRepresentation.getChildren(t),l=0,d=o.length;l<d;l++){r=o[l];var c=this.geomRepresentation.getNodeColor(t);switch(this.geomRepresentation.getNodeType(r)){case i.KindOfNode.NOD_GP:var h=this.geomRepresentation.getGPType(r,c);switch(null==c&&(c=n),null==n&&(c=this.DEFAULT_WIRE_COLOR),h){case i.KindOfGP.GP_Edge:this.processEdge(r,new e.Color(c[0],c[1],c[2],c[3]),s,a)}}}},processTessSolid:function(t){for(var r,n=this.geomRepresentation.getNodeColor(t),s=this.geomRepresentation.getChildren(t),o=0,a=s.length;o<a;o++){r=s[o];var d=this.geomRepresentation.getNodeType(r),c=this.geomRepresentation.getNodeColor(r);switch(void 0===c&&(c=n),void 0===n&&(c=this.DEFAULT_COLOR),d){case i.KindOfNode.NOD_GP:switch(this.geomRepresentation.getGPType(r)){case i.KindOfGP.GP_Face:this.processTriangulatedFace(r,new e.Color(c[0],c[1],c[2],c[3]));break;case i.KindOfGP.GP_Edge:this.processEdge(r,void 0,[void 0,void 0],l)}}}},processEdge:function(t,r,n,i){var s=this.geomRepresentation.getEdgeData(t);this.startNewMesh(!1),this.posBuffer.size=s.vertices.length,this.posBuffer.data=new Float32Array(s.vertices.length);for(var o=0;o<s.vertices.length;++o)this.posBuffer.data[o]=parseFloat(s.vertices[o]);this.normBuffer.size=s.vertices.length,this.normBuffer.data=new Array(this.normBuffer.size),this.currentObject.addBuffer(3*this.meshNb,this.posBuffer);var a=new e.Primitive,l=s.getVerticesNumber();a.nbIndices=l,a.connectivity=e.ConnectivityTypeEnum.LINE_STRIP,a.geomType=i;var d=e.LinePatternEnum.SOLID;null!=n[0]&&("continuous"===n[0]?d=e.LinePatternEnum.SOLID:"chain"===n[0]?d=e.LinePatternEnum.DOTDASHED:"chain double dash"===n[0]?d=e.LinePatternEnum.PHANTOM:"dashed"===n[0]?d=e.LinePatternEnum.DASHED:"dotted"===n[0]&&(d=e.LinePatternEnum.DOTTED)),a.attr={fill:new e.FillAttributes(void 0===r?this.DEFAULT_WIRE_COLOR:r),line:new e.LineAttributes(void 0===r?this.DEFAULT_WIRE_COLOR:r,void 0===n[1]?1:n[1],d),point:new e.PointAttributes};var c=new e.VertexComponent;c.component=this.posBuffer.component,c.nbVertices=l,c.nbValuesPerVertex=this.posBuffer.dimension,c.format=this.posBuffer.format,c.cardinality=0,c.bufferId=3*this.meshNb,a.addVertexComponent(c),this.currentObject.addPrimitive(a),this.finishCurrentMesh()},processTriangulatedFace:function(t,r){var n=this.geomRepresentation.getFaceData(t);this.startNewMesh(!0),this.posBuffer.size=n.vertices.length,this.posBuffer.data=new Float32Array(n.vertices.length);for(var i=0;i<n.vertices.length;++i)this.posBuffer.data[i]=parseFloat(n.vertices[i]);this.normBuffer.size=n.normals.length,this.normBuffer.data=new Float32Array(n.normals.length);for(i=0;i<n.normals.length;++i)this.normBuffer.data[i]=parseFloat(n.normals[i]);for(var s=0,o=0;o<n.nbVertexPerStripTriangle.length;o++)s+=3*(n.nbVertexPerStripTriangle[o]-2);for(o=0;o<n.nbVertexPerFanTriangle.length;o++)s+=3*(n.nbVertexPerFanTriangle[o]-2);s+=n.singleTrianglesIndices.length,this.posIdxBuffer.size=s,this.posIdxBuffer.data=new Uint16Array(this.posIdxBuffer.size);var a=0;(p=new e.Primitive).nbIndices=s,p.connectivity=e.ConnectivityTypeEnum.TRIANGLES,p.geomType="FACE",p.attr={fill:new e.FillAttributes(void 0===r?this.DEFAULT_COLOR:r),line:new e.LineAttributes,point:new e.PointAttributes},(f=new e.VertexComponent).component=e.VertexComponentEnum.POSITION,f.nbVertices=n.vertices.length,f.nbValuesPerVertex=3,f.format=e.DataTypeEnum.FLOAT,f.cardinality=0,f.bufferId=3*this.meshNb,f.indices=new e.IndexArray,f.indices.format=e.DataTypeEnum.UINT,f.indices.bufferId=3*this.meshNb+2,f.indices.offset=a,(m=new e.VertexComponent).component=e.VertexComponentEnum.NORMAL,m.nbVertices=n.vertices.length,m.nbValuesPerVertex=3,m.format=e.DataTypeEnum.FLOAT,m.cardinality=0,m.bufferId=3*this.meshNb+1,m.indices=new e.IndexArray,m.indices.format=e.DataTypeEnum.UINT,m.indices.bufferId=3*this.meshNb+2,m.indices.offset=a,p.addVertexComponent(f),p.addVertexComponent(m),p.nbVertexComponents>0&&this.currentObject.addPrimitive(p);var l=0;for(o=0;o<n.nbVertexPerStripTriangle.length;o++){for(var d=n.nbVertexPerStripTriangle[o],c=2;c<d;++c)c%2?(this.posIdxBuffer.data[a++]=n.stripTrianglesIndices[l+c],this.posIdxBuffer.data[a++]=n.stripTrianglesIndices[l+c-1],this.posIdxBuffer.data[a++]=n.stripTrianglesIndices[l+c-2]):(this.posIdxBuffer.data[a++]=n.stripTrianglesIndices[l+c-1],this.posIdxBuffer.data[a++]=n.stripTrianglesIndices[l+c],this.posIdxBuffer.data[a++]=n.stripTrianglesIndices[l+c-2]);l+=d}var h=0;for(o=0;o<n.nbVertexPerFanTriangle.length;o++){var u=n.nbVertexPerFanTriangle[o];for(c=2;c<u;++c)this.posIdxBuffer.data[a++]=n.fanTrianglesIndices[h],this.posIdxBuffer.data[a++]=n.fanTrianglesIndices[h+c-1],this.posIdxBuffer.data[a++]=n.fanTrianglesIndices[h+c];h+=u}var p,f,m,g=0;for(o=0;o<n.singleTrianglesIndices.length;o+=3){for(var v=0;v<3;v++){var D=n.singleTrianglesIndices[g+v];this.posIdxBuffer.data[a++]=D}g+=3}this.geomRepresentation.isIndexed&&!this.geomRepresentation.isIndexed()&&((p=new e.Primitive).geomType="FACE",p.nbIndices=n.vertices.length/3,p.connectivity=e.ConnectivityTypeEnum.TRIANGLES,p.attr={fill:new e.FillAttributes(void 0===r?this.DEFAULT_COLOR:r),line:new e.LineAttributes,point:new e.PointAttributes},(f=new e.VertexComponent).component=e.VertexComponentEnum.POSITION,f.nbVertices=n.vertices.length/3,f.nbValuesPerVertex=3,f.format=e.DataTypeEnum.FLOAT,f.cardinality=0,f.bufferId=3*this.meshNb,f.indices=null,(m=new e.VertexComponent).component=e.VertexComponentEnum.NORMAL,m.nbVertices=n.normals.length/3,m.nbValuesPerVertex=3,m.format=e.DataTypeEnum.FLOAT,m.cardinality=0,m.bufferId=3*this.meshNb+1,m.indices=null,p.addVertexComponent(f),p.addVertexComponent(m),this.currentObject.addPrimitive(p));this.finishCurrentMesh()}},o}),define("DS/XCADLoader/XCADOOCHandler",["DS/Visualization/ThreeJS_DS","DS/3DXMTiles/OOCHandler","DS/3DXMTiles/3DXMTilesUtils","DS/EasySax/EasySax"],function(e,t,r,n){"use strict";var i=function(e){t.call(this,t.InstRefHander),this.viewer=e.viewer,this.model3DHandler=e.model3DHandler||null,this.manager=e.manager,this.rootId=e.rootId,this.onHandlePathesCB=null,this.onDoneCB=null,this.ready=!0,this.zipArchive=e.zipArchive||null,this.filename=e.filename||null,this.useZip2=e.useZip2,this.cnt=0,this.process=0,this.nbExternalRef=0,this.processExternalRef=0,this.shapeRepListMap=new Map,this.assyProductsMap=new Map,this.rootProducts=[],this.rootBbox=void 0,this.rootName=void 0,this.childSet=new Set,this._filesSet=new Set,this._Files=[],this._Parts=[],this._VORs=[],this._occurencesMap=new Map,this._matchingDoc=new Map,this._matchingRC=new Map,this._matchingBBox=new Map,this._matchingFile=new Map,this.filesuids=[]};return(i.prototype=Object.create(t.prototype)).dispose=function(){this.model3DHandler=null},i.prototype.isReady=function(){return this.ready},i.prototype.init=function(t){function r(r){var i=r.data;let s=!1,o=!1,a=!1,l=!1,d=!1,c=!1,h=!1,u=!1,p=!1,f=!1,m=!1,g=!1,v=!1,D=!1,b=!1,C=void 0,S=void 0,T=void 0,x=void 0,_=void 0;var w=new n;w.on("startNode",function(e,t){var n=t();switch(e){case"Header":s=!0;break;case"DataContainer":break;case"File":if(o=!0,!a){var i={uid:n.uid,path:".",root:C};r.handler._matchingFile.set(i.uid+C,r.handler._Files.length),r.handler._Files.push(i)}break;case"Id":null!=n.id&&!0===o&&(r.handler._Files[r.handler._Files.length-1].filename=n.id),null!=n.id&&!0===h&&!0===D&&(r.handler._Parts[r.handler._Parts.length-1].occurences[r.handler._Parts[r.handler._Parts.length-1].occurences.length-1].id=n.id);break;case"Identifier":null!=n.id&&!0===h&&!0===D&&(r.handler._Parts[r.handler._Parts.length-1].occurences[r.handler._Parts[r.handler._Parts.length-1].occurences.length-1].id=n.id);break;case"IdentifierString":l=!0;break;case"DocumentVersion":a=!0,_=n.uid;break;case"ClassString":d=!0;break;case"Part":h=!0;var S={uid:n.uid,root:C,occurences:[],vors:[],bbox:[]};r.handler._Parts.push(S);break;case"Name":c=!0;break;case"CharacterString":v=!0;break;case"Occurrence":if(D=!0,null!=n.uid&&!0===h){var T={uid:n.uid};r.handler._Parts[r.handler._Parts.length-1].occurences.push(T)}break;case"AssignedDocument":if(h){let e=n.uidRef;r.handler._Parts[r.handler._Parts.length-1].AssignedDocumentUidRef=e}break;case"ViewOccurrenceRelationship":u=!0;var w={uid:n.uid,idx:r.handler._VORs.length,part_idx:-1};h&&(w.part_idx=r.handler._Parts.length-1),o&&(w.File_id=r.handler._Files[r.handler._Files.length-1].id),r.handler._VORs.push(w);break;case"Related":u&&(r.handler._VORs[r.handler._VORs.length-1].uidRef=n.uidRef);break;case"RotationMatrix":p=!0;break;case"TranslationVector":f=!0;break;case"Document":break;case"RepresentationContext":x=n.uid,m=!0;break;case"DigitalFile":a&&r.handler._matchingDoc.set(_,n.uidRef);break;case"ShapeDependentProperty":g=!0;break;case"DefinedIn":h&&g&&(r.handler._Parts[r.handler._Parts.length-1].bbox[r.handler._Parts[r.handler._Parts.length-1].bbox.length]=n.uidRef);break;case"Coordinates":b=!0}}),w.on("textNode",function(e,t){!0===o&&(!0===l&&(r.handler._Files[r.handler._Files.length-1].path=e),!0===d&&(r.handler._Files[r.handler._Files.length-1].type=e)),!0===u&&(!0===p&&(S=e),!0===f&&(T=e)),!0===h&&c&&v&&void 0===r.handler._Parts[r.handler._Parts.length-1].name&&(r.handler._Parts[r.handler._Parts.length-1].name=e+r.handler._Parts[r.handler._Parts.length-1].uid),m&&b&&r.handler._matchingRC.set(x,e),s&&c&&void 0===C&&(C=e)}),w.on("endNode",function(t){switch(t){case"Header":s=!1;break;case"DataContainer":C=void 0;break;case"File":o=!1;break;case"Id":break;case"IdentifierString":l=!1;break;case"ClassString":d=!1;break;case"Part":h=!1;break;case"Name":c=!1;break;case"CharacterString":v=!1;break;case"Occurrence":D=!1;break;case"ViewOccurrenceRelationship":let w=(n=S.trim().split(" "),i=T.trim().split(" "),(x=new e.Matrix4).set(parseFloat(n[0]),parseFloat(n[3]),parseFloat(n[6]),parseFloat(i[0]),parseFloat(n[1]),parseFloat(n[4]),parseFloat(n[7]),parseFloat(i[1]),parseFloat(n[2]),parseFloat(n[5]),parseFloat(n[8]),parseFloat(i[2]),0,0,0,1),x);r.handler._VORs[r.handler._VORs.length-1].curTransfo=w,!0===h&&r.handler._Parts[r.handler._Parts.length-1].vors.push(r.handler._VORs[r.handler._VORs.length-1]),u=!1;break;case"RotationMatrix":p=!1;break;case"TranslationVector":f=!1;break;case"Document":break;case"RepresentationContext":m=!1;break;case"DocumentVersion":a=!1,_=void 0;break;case"ShapeDependentProperty":g=!1;break;case"Coordinates":b=!1}var n,i,x}),r.handler.processSTPXFileV2(i,w,function(){for(let e=0;e<r.handler._Parts.length;e++){for(let t=0;t<r.handler._Parts[e].vors.length;t++)r.handler._occurencesMap.set(r.handler._Parts[e].root+r.handler._Parts[e].vors[t].uidRef,r.handler._Parts[e].vors[t].idx);if(2===r.handler._Parts[e].bbox.length){let t=r.handler._matchingRC.get(r.handler._Parts[e].bbox[0]),n=r.handler._matchingRC.get(r.handler._Parts[e].bbox[1]),i=r.handler.buildBbox(t.trim().split(" "),n.trim().split(" "));r.handler._matchingBBox.set(r.handler._Parts[e].name,i)}}for(var e=0;e<r.handler._Parts.length;e++)r.handler.processOccurenceV2(r.handler._Parts[e]);t()})}if(this.zipArchive){var i=this.filename.substring(this.filename.lastIndexOf("/")+1,this.filename.lastIndexOf("."));i+=".stpx";var s=this.zipArchive.find(i);s&&(this.useZip2?s.getBlob("application/octet-stream").then(e=>{var t=new FileReader;t.onload=(e=>{r({data:e.target.result,handler:this})}),t.readAsText(e)}):s.getBlob("application/octet-stream",e=>{var t=new FileReader;t.onload=(e=>{r({data:e.target.result,handler:this})}),t.readAsText(e)}))}},i.prototype.processSTPXFileV2=function(e,t,r,n,i){this.process++;let s=0;t.parse(e);for(var o=0;o<this._Files.length;o++){let e=void 0;e=void 0===n?this._Files[o].uid:n;var a=this._Files[o].filename.substring(this._Files[o].filename.lastIndexOf(".")+1,this._Files[o].filename.length);if("geometry"==this._Files[o].type||"STPX"!=a.toUpperCase()){let t=this._Files[o].path;t.startsWith("./")?t=t.substring(2,t.length):t.startsWith(".")&&(t=t.substring(1,t.length));let r=t+this._Files[o].filename;null!=this.zipArchive.find(r)&&this.shapeRepListMap.set(e+this._Files[o].root,{type:this._Files[o].type,filename:this._Files[o].filename,folder:this._Files[o].path})}if(!1===this._filesSet.has(this._Files[o].path+this._Files[o].filename)){s++,this._filesSet.add(this._Files[o].path+this._Files[o].filename);let e=this._Files[o].uid;if("STPX"===a.toUpperCase()){this.nbExternalRef++;let n=this.zipArchive.find(this._Files[o].filename);n&&(this.useZip2?n.getBlob("application/octet-stream").then(n=>{let i=new FileReader;i.onload=(n=>{this.processSTPXFileV2(n.target.result,t,r,e,function(e){e.processExternalRef++,e.nbExternalRef==e.processExternalRef&&r()})}),i.readAsText(n)}):n.getBlob("application/octet-stream",n=>{var i=new FileReader;i.onload=(n=>{this.processSTPXFileV2(n.target.result,t,r,e,function(e){e.processExternalRef++,e.nbExternalRef==e.processExternalRef&&r()})}),i.readAsText(n)}))}}}null!=i&&i(this),null!=i||0!=s&&0!=this.nbExternalRef||r()},i.prototype.processOccurenceV2=function(e){let t=void 0,r=void 0,n=void 0,i=void 0,s=void 0;for(let o=0;o<e.occurences.length;o++){let a=this._occurencesMap.get(e.root+e.occurences[o].uid);t=e.occurences[o].id,r=e.name;let l=(n=this._Parts[this._VORs[a].part_idx].name)?this._matchingBBox.get(r):void 0;if(null!=(i=e.AssignedDocumentUidRef)&&i.startsWith("DV")&&(i=this._matchingDoc.get(i)),null!=i){let t=this._matchingFile.get(i+e.root);"STPX"===this._Files[t].filename.substring(this._Files[t].filename.lastIndexOf(".")+1,this._Files[t].filename.length).toUpperCase()?i+=this._Files[t].filename:i+=e.root}if(null!=(s=e.AssignedDocumentUidRef)&&s.startsWith("DV")&&(s=this._matchingDoc.get(s)),null!=s){let t=this._matchingFile.get(s+e.root);"STPX"===this._Files[t].filename.substring(this._Files[t].filename.lastIndexOf(".")+1,this._Files[t].filename.length).toUpperCase()?s+=this._Files[t].filename:s+=e.root}if(null!==r&&null!==r){if(!1===this.assyProductsMap.has(n)&&(this.assyProductsMap.set(n,{referenceName:n,isRoot:!0,childrenList:[],geomRep:this.shapeRepListMap.get(s)}),this.rootProducts.push(n),this.rootName=n,this.rootBbox=l),!0===this.assyProductsMap.has(r)){this.assyProductsMap.get(r).isRoot=!1;let e=this.rootProducts.indexOf(r);-1!=e&&this.rootProducts.splice(e,1)}else this.assyProductsMap.set(r,{referenceName:r,isRoot:!1,childrenList:[],geomRep:this.shapeRepListMap.get(i)});this.assyProductsMap.get(n).geomRep=void 0,this.assyProductsMap.get(n).childrenList.push({instanceName:t,productReferenceIdx:r,transfo:this._VORs[a].curTransfo,bbox:l})}}},i.prototype.buildBbox=function(t,r){let n=new e.Box3;return n.setValues(parseFloat(t[0]),parseFloat(t[1]),parseFloat(t[2]),parseFloat(r[0]),parseFloat(r[1]),parseFloat(r[2])),n},i.prototype.processInstance=function(e,t,r){let n=e.instanceName,i=e.productReferenceIdx,s=e.transfo,o=e.bbox,a=void 0,l=this.assyProductsMap.get(i);if(l.geomRep){let e=l.geomRep.folder;e.startsWith("./")?e=e.substring(2,e.length):e.startsWith(".")&&(e=e.substring(1,e.length)),a=e+l.geomRep.filename}if(!0===r?this.manager.registerReference(i,{boundingSphere:null,boundingBox:o,url:this.zipArchive?void 0:a,src:this.zipArchive?{zipArchive:this.zipArchive,path:a}:void 0,handler:l.geomRep?this.model3DHandler:this}):0==this.childSet.has(t+" : "+i+" : "+n)&&(this.childSet.add(t+" : "+i+" : "+n),this.manager.addChild(t,i,{instanceId:n+this.cnt++,matrix:s})),!l.geomRep){let e=l.childrenList.length;for(let t=0;t<e;t++)this.processInstance(l.childrenList[t],i,r)}},i.prototype.handlePathes=function(e,t,r){if(1!==e.length)throw new Error("Invalid pathes.length ("+e.length+")");if(1!==e[0].ids.length)throw new Error("Invalid pathes.ids.length ("+e.ids.length+")");this.ready=!1,this.onHandlePathesCB&&this.onHandlePathesCB(e,t,r),setTimeout(this.handlePathes_internal.bind(this,e,t,r),0)},i.prototype.handlePathes_internal=function(e,t,r){this.manager.startInstRefGraphTransaction(),this.assyProductsMap.forEach((e,t)=>{if(e.isRoot){let t=e.childrenList.length;for(let r=0;r<t;r++)this.processInstance(e.childrenList[r],"root_url",!0)}}),this.assyProductsMap.forEach((e,t)=>{if(e.isRoot){let t=e.childrenList.length;for(let r=0;r<t;r++)this.processInstance(e.childrenList[r],"root_url",!1)}});let n=this.model3DHandler.rootId;this.manager.freezeRoot(n),this.manager.endInstRefGraphTransaction(),this.ready=!0,t.doneCB(e,[])},i}),define("DS/XCADLoader/XCADSingleton",[],function(){"use strict";var e=null;function t(){this.data=[]}return t.getInstance=function(){return null===e&&(e=new t),e},t.prototype={constructor:t,addData:function(t){e.data=t},getDataValue:function(t){return e.data[t]}},t.getInstance()}),define("DS/XCADLoader/XCADZipUtils",["UWA/Core","DS/ZipJS/zip-fs","UWA/Class/Promise"],function(e,t,r){var n={},i=function(e){var t="";switch(e.toLowerCase()){case"stp":t="text/stp";break;case"cgr":t="text/plain";break;case"stpx":t="text/xml";break;default:t=""}return t};return n.download=function(t,n){return new r(function(r,i){e.is(t.url)&&e.is(t.fileExtension)||(console.log("Invalid / empty ticket for file download"),i());var s=new XMLHttpRequest,o=t.url;s.onload=function(e){if(4===s.readyState)if(200===s.status){var o=new Blob([this.response],{type:"application/"+t.fileExtension}),a=window.URL||window.webkitURL;r(!0===n?o:a.createObjectURL(o))}else i()},s.open("GET",o,!0),s.setRequestHeader("Cache-Control","no-cache"),s.setRequestHeader("Content-type","application/x-www-form-urlencoded"),s.responseType="arraybuffer",s.send()})},n.importBlobURLToZip=function(n){return new r(function(r,i){if(e.is(n)){t.workerScriptsPath="scripts/ThreeDS/Visualization/zip-js/";var s=new t.fs.FS,o={serverurl:"",filename:n,proxyurl:"none"};s.importHttpContent(o,!1,function(){r(s)},function(){console.log("Failure importing to Zip FS"),i()})}else i()})},n.unzip=function(e){var n,i;return n=e,i=function(e){return r.all(e.map(function(e){return function(e,n){return new r(function(r,n){var i=new t.BlobWriter;e.getData(i,function(t){var n=new FileReader;n.onloadend=function(t){r({name:e.filename,content:this.result})},n.readAsDataURL(t)})})}(e)}))},new r(function(e,r){t.createReader(new t.BlobReader(n),function(t){t.getEntries(function(t){e(i(t))})},function(e){console.log(e),r(e)})})},n.importZipFileEntryToBlob=function(e){var t=e.fileName||"",n=e.fileExtension||"",s=i(n),o=r.deferred();try{e.zipFileEntry.getBlob(s,function(e){var r=new FileReader;r.onload=function(e){o.resolve({name:t,blob:e.target.result})},r.readAsArrayBuffer(e)})}catch(e){o.reject(e)}return o.promise},n.importZipFileEntryToText=function(e){var t=e.fileName||"",n=e.fileExtension||"",s=(i(n),r.deferred());try{e.zipFileEntry.getText(function(e){s.resolve({name:t,blob:e.currentTarget.result})})}catch(e){s.reject(e)}return s.promise},n.downloadFiles=function(t){return new r(function(i,s){if(t.length>0){var o={fileExtension:"zip",url:t};n.download(o).then(function(t){n.importBlobURLToZip(t).then(function(t){if(e.is(t)){var o=t.entries;zipPromises=[];for(var a=0;a<o.length;a++)if(!0!==o[a].directory){for(var l="",d=o[a].parent;null!=d;)null!=d.name&&(l=l.length>0?d.name+"_"+l:d.name),d=d.parent;var c,h=o[a].name.split(".");c=l.length>0?l+"_"+h[0]:h[0];var u=h[1],p={fileName:c,fileExtension:u,zipFileEntry:o[a]};"cgr"===u.toLowerCase()?zipPromises.push(n.importZipFileEntryToBlob(p)):zipPromises.push(n.importZipFileEntryToText(p))}r.all(zipPromises).then(function(e){var t={};e.forEach(function(e){t[e.name]=e.blob}),i(t)},function(e){console.log("downloadRAEData - zipPromises",e),s(e)})}else console.log("downloadRAEData - zipPromises NO DATA"),s("NO DATA");0==zipPromises.length&&(console.log("downloadRAEData - zipPromises NO DATA"),s("NO DATA"))})},s)}else console.log("empty file names"),s("empty file names")})},n}),define("DS/XCADLoader/IModelLoaderPlug",[],function(){"use strict";return{isManagedType:function(e){},loadModel:function(e,t,r,n,i,s,o,a,l,d){}}}),define("DS/XCADLoader/XCADError",["DS/WebInfraUI/Notification"],function(e){"use strict";function t(){this.rsc="",this.url="",this.type="",this.level=""}return t.prototype={constructor:t,setError:function(t,r,n,i){switch(this.rsc=t,this.url=r,this.type=n,i){case"info":this.level=e.info;break;case"fatal":this.level=e.fatal;break;case"error":this.level=e.error;break;case"warning":this.level=e.warning;break;case"success":this.level=e.success}}},t}),define("DS/XCADLoader/XCADModelLoader",["DS/Visualization/ThreeJS_DS","DS/Visualization/Node3D","DS/3DXMTiles/OOCModel3DZipHandler","DS/XCADLoader/XCADOOCHandler","DS/3DXMTiles/OOCManager","DS/ZipJS/zip-fs","DS/ZipJS2/ZipJS2","DS/Visualization/Utils"],function(e,t,r,n,i,s,o,a){"use strict";var l=!0,d="zipjs2",c=function(t,h,u,p){var f=window.localStorage.getItem("XCADLoader_ODTMode"),m=void 0;l&&(null!=f?o.configure({maxWorkers:1,terminateWorkerTimeout:NaN}):o.configure({maxWorkers:4,terminateWorkerTimeout:NaN}));var g=void 0;if(g=l?new o.fs.FS:new s.fs.FS,null!=u)l?g.importBlob(u,{directAccess:!0}).then(D):g.importBlob(u,D);else{this.modelName="";var v=function(e,t){var r=function(e){if("string"==typeof e){var t="",r="";if("blob:"===e.substring(0,5))return{provider:"FILE",serverurl:"",filename:e,requiredAuth:null,proxyurl:"none"};if("http://"===e.substring(0,7))t=e;else if("https://"===e.substring(0,8))t=e;else if("/"===e.substring(0,1))t=e;else if("ms-appx://"===e.substring(0,10))t=e;else if("file://"===e.substring(0,7))t=e;else{var n=location.href,i=n.indexOf("?");-1!==i&&(n=n.substring(0,i));var s=n.lastIndexOf("/");-1!==s&&(n=n.substring(0,s+1)),t=n+e}r=(t=a.parseUrl(t)).protocol,""!==t.protocol&&(r+="://"),r+=t.authority+t.path.substring(0,t.path.lastIndexOf("/"))+"/";var o=t.path.substring(t.path.lastIndexOf("/")+1,t.path.length);return""!==t.query&&(o=o+"?"+t.query),e={provider:"FILE",serverurl:r,filename:o,requiredAuth:null,proxyurl:"none"}}return"EV6"===e.provider&&(t=a.parseUrl(e.serverurl),e.serverdefinition={},e.serverdefinition.protocol=t.protocol,e.serverdefinition.hostname=""!==t.user?t.user+"@"+t.domain:t.domain,e.serverdefinition.port=t.port,e.serverdefinition.rooturi=t.path,"/"===e.serverdefinition.rooturi.slice(0,1)&&(e.serverdefinition.rooturi=e.serverdefinition.rooturi.slice(1,e.serverdefinition.rooturi.length))),e}(t);return e.modelName=r.serverurl?r.serverurl:"",e.modelName+=r.filename?r.filename:"",r}(this,p);l?g.importHttpContent(p,{useXHR:!1,preventHeadRequest:!0}).then(D):g.importHttpContent({filename:v.filename,serverurl:v.serverurl},!1,D)}function D(){var s="root_url";m=new i(t),c.oocManager=m,m.setTargetNode(h.targetNode),h.setOnLoadedCallback(void 0),h.setKeepLoaderMode(!0),c.StartLoadingCB&&m.setOnStartLoadingCB(c.StartLoadingCB),c.EndLoadingCB&&m.setOnEndLoadingCB(c.EndLoadingCB),c.ObjectLoadedCB&&h.setOnLoadedCallback(c.ObjectLoadedCB);var o=new r({loadModelCB:function(e){var t={};h.setOnLoadedProductStructureCallback(t=>{e.onLoadedCB()});let r=this.manager.fullVPHandler.oocHandler.assyProductsMap.get(e.targetNode.modelIdentification).geomRep.filename;if("CGR"===r.substring(r.lastIndexOf(".")+1,r.length).toUpperCase())h.fileType="cgr",h.loadModelFromBuffer(e.data,e.targetNode,t);else{h.fileType="stp";var n=new TextDecoder("utf-8").decode(e.data);h.loadModelFromBuffer(n,e.targetNode,t)}},unzipLib:d});null===f&&(o.ldhHandler.nbTickets=8);var a=new n({manager:m,viewer:t,model3DHandler:o,zipArchive:g,filename:void 0===v?u.name:v.filename,useZip2:l});a.init(function(){m.registerReference(s,{boundingSphere:null,boundingBox:a.rootBbox,manager:m,handler:a,url:s});var r=m.addRoot(s,{matrix:new e.Matrix4,node:h.targetNode,manager:m,instanceId:a.rootName});o.rootId=r,t.currentViewpoint.reframe()})}};return c.prototype.clear=function(){c.oocManager&&c.oocManager.getRootOccurrences().forEach(function(e,t,r){c.oocManager.removeRoot(e.rootId)})},c.prototype.pauseLoading=function(e){c.oocManager&&c.oocManager.setPauseLoading(e,"XCAD")},c.prototype.setOnStartLoadingCB=function(e){c.StartLoadingCB=e},c.prototype.setOnEndLoadingCB=function(e){c.EndLoadingCB=e},c.prototype.setObjectLoadedCB=function(e){c.ObjectLoadedCB=e},c}),define("DS/XCADLoader/XCADLoader",["UWA/Promise","DS/Visualization/Node3D","DS/Visualization/SceneGraphFactory","DS/XCADInputDocuments/XCADRepresentation","DS/XCADLoader/XCADGeomLoader","DS/Mesh/ThreeJS_Base","DS/ZipJS/zip-fs","DS/ZipJS/LoaderInflate","DS/Visualization/Utils","DS/VisuDataAccess/ProxyAbstraction","DS/XCADLoader/XCADError","DS/XCADLoader/XCADSingleton","DS/XCADLoader/XCADZipUtils"],function(e,t,r,n,i,s,o,a,l,d,c,h,u){"use strict";var p=0,f=function(){var o,f=null,m=null,g=null,v=null,D=[],b=0,C=0;this.processModel=function(e){},this.load=async function(S,T,x,_,w,P,R,A,y,I,O){f=null,m=null,g=null,v=null,null,"",D=[],T,o=T.substr(T.lastIndexOf(".")+1).toUpperCase(),f=x,m=_,g=w,v=P;new t;var E=S,N=new s.MeshBasicMaterial({side:s.DoubleSide,transparent:!1}),F=window.localStorage.getItem("XCAD_DebugMode");if(null!=O){if(null!=F)var L=performance.now();if(await z(O),null!=F){var B=performance.now();console.log("_processModel_"+p+" in "+(B-L)+" milliseconds."),window.localStorage.setItem("processModel_"+p,"_processModel in "+(B-L)+" milliseconds."),p++}}else if(1==S.zipped&&2==S.zipMethod){var M=I.serverurl?I.serverurl:"";M+=I.filename?I.filename:"";var V=new d;l.setProxyFromSpec(I,V),V.executeGetHttpRequest(M,"arraybuffer",null,function(e){var t=new Uint8Array(e),r=t.length,n=t.subarray(10,r);z(function(e){var t,r,n,i,s,o;for(t="",n=e.length,r=0;r<n;)switch((i=e[r++])>>4){case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:t+=String.fromCharCode(i);break;case 12:case 13:s=e[r++],t+=String.fromCharCode((31&i)<<6|63&s);break;case 14:s=e[r++],o=e[r++],t+=String.fromCharCode((15&i)<<12|(63&s)<<6|(63&o)<<0)}return t}((new a).Inflate(n)))})}else if(1==S.zipped){M=I.serverurl?I.serverurl:"";M+=I.filename?I.filename:"";var X=I.filename.substring(0,I.filename.lastIndexOf(".")),k=[];k.push(u.downloadFiles(M)),e.all(k).then(function(e){console.log("download Zip archive done");let t=void 0;e&&(e.forEach(function(e){h.addData(e),null!=e[X]&&(t=e[X])}),null!=t&&z(t))})}else{T.startsWith("blob")||(I.proxyurl=null);V=new d;l.setProxyFromSpec(I,V);M=I.serverurl?I.serverurl:"";M+=I.filename?I.filename:"",V.executeGetHttpRequest(M,y,null,function(e){if(null!=F)var t=performance.now();if(z(e),null!=F){var r=performance.now();console.log("_processModel in "+(r-t)+" milliseconds."),window.localStorage.setItem("processModel_"+p,"_processModel in "+(r-t)+" milliseconds."),p++}})}async function z(e){Date.now();var t=new c,s=await E.initialize(e);if(s instanceof c)return v&&v(s),void(m&&m({loaded:0,total:0}));var a=!1,l=E.getRepresentation(n.XCAD_TESSELLATED_REPRESENTATION);if(void 0!==l)for(var d=(new i).load(l,m),h=void 0,u=0;u<d.length;u++){var D=d[u],C=D.primitive,S=D.transfo,x=D.descendants;if(0!=C.nbPrimitives||0==C.nbPrimitives&&d.length>1)if(void 0===h)(h=l.isIndexed&&!l.isIndexed()?r.createMeshNode(C,N):r.createMeshNode(C,N,0,"mono")).productType="Reference3D",null!=S&&h.setMatrix(S),b++;else{var _=void 0;_=l.isIndexed&&!l.isIndexed()?r.createMeshNode(C,N):r.createMeshNode(C,N,0,"mono"),null!=S&&_.setMatrix(S),h.addChild(_),U(x,_,l)}}else if(void 0!==(l=E.getRepresentation(n.XCAD_PRODUCT_REPRESENTATION)))for(var w=l.getRootProducts(),P=0;P<w.length;P++)0===(h=j(w[P],m,{},null)).children.length&&null==h.mesh3js&&(a=!0,console.error(T+" : "+o+" Root with no supported Geometry"),t.setError("XCADLoader/XCADLoader_errors",null,"EMPTY_ROOT","error"),v&&v(t)),R.add(h);else a=!0,console.error(T+" : "+o+" file with no supported representation"),t.setError("XCADLoader/XCADLoader_errors",null,"NO_REP","error"),v&&v(t);if(null!=F){var A="Number of detected instanciate Parts / Number of loaded instanciate Parts: "+l._associatedDocument.nbInstPart+" / "+b;console.log(A),window.localStorage.setItem("processModel_"+p,A),p++,A=b===l._associatedDocument.nbInstPart?"Number of part is OK":"Number of part is KO",console.log(A),window.localStorage.setItem("processModel_"+p,A),p++}l=null,E.close(),E=null,f&&h?(f(h),h=null):f(null),a||g&&g()}function U(e,t,n){for(var i=0;i<e.length;i++){var s=e[i].primitive,o=e[i].transfo,a=e[i].descendants,l=void 0;l=n&&n.isIndexed&&!n.isIndexed()?r.createMeshNode(s,N):r.createMeshNode(s,N,0,"mono"),null!=o&&l.setMatrix(o),t.addChild(l),U(a,l)}}function j(e,s,a,l){var d,h=new c,u=e.getProductAttributes(),p=u[0],f=e.getInstanceList(),m=e.getFilePath();if(void 0!==m){console.log(T+" has an external reference: "+m);var g=u[1];(d=new t(l)).productType="Instance3D",d.name=l,void 0!==g&&(g.name=p,d.add(g)),D[p]=d,b++}else{if(""!==p){var S=D[p];if(null!=S&&"Mesh3D"===S.getNodeType())return b++,S.clone();var x=e.getDocument();if(void 0!==x){x.initialize("");var _=x.getRepresentation(n.XCAD_TESSELLATED_REPRESENTATION);if(void 0!==_)for(var w=(new i).load(_,null),P=(g=void 0,0);P<w.length;P++){var R=w[P],A=R.primitive,y=R.transfo,I=R.descendants;if(0!=A.nbPrimitives||0==A.nbPrimitives&&w.length>1)if(void 0===g)(g=_.isIndexed&&!_.isIndexed()?r.createMeshNode(A,N):r.createMeshNode(A,N,0,"mono")).name=p,g.productType="Instance3D",D[p]=g,null!=y&&g.setMatrix(y),b++;else{var O=void 0;O=_.isIndexed&&!_.isIndexed()?r.createMeshNode(A,N):r.createMeshNode(A,N,0,"mono"),null!=y&&O.setMatrix(y),g.addChild(O),U(I,O,_)}}else console.warn(T+" : "+o+" file with empty representation"),h.setError("XCADLoader/XCADLoader_errors",null,"NO_REP","warning"),v&&v(h);x.close()}}f.length>0?void 0===d&&((d=new t(p)).productType="Instance3D",void 0!==g&&d.add(g),D[p]=d):d=g,C++;var E=Object.keys(e._document.assyProducts).length;s&&s({loaded:C,total:E});for(P=0;P<f.length;P++){var F=H(f[P]);void 0!==F&&d.add(F)}d&&"Reference3D"===d.productType&&0===d.children.length&&(console.warn(T+" : "+o+" Product with empty Geometry"),h.setError("XCADLoader/XCADLoader_errors",null,"EMPTY_PROD","warning"),v&&v(h))}return d}function H(e){var t=e.getTransformation(),r=new s.Matrix4;if(void 0!==t&&t instanceof s.Matrix4)r=t;else{var n=t[1],i=t[0];r.set(i[0][0],i[0][1],i[0][2],n[0],i[1][0],i[1][1],i[1][2],n[1],i[2][0],i[2][1],i[2][2],n[2],0,0,0,1)}var o={CoordSys:""},a=j(e.getReferenceProduct(),null,0,e.getInstanceAttributes()[0]);if(null!=o.CoordSys&&1==o.CoordSys.Coord&&null!=o.CoordSys.Origin&&0!=o.CoordSys.Origin.length){var l=parseFloat(o.CoordSys.Origin[0]),d=parseFloat(o.CoordSys.Origin[1]),c=parseFloat(o.CoordSys.Origin[2]),h=new s.Matrix3,u=new s.Vector3(o.CoordSys.RefDir[0],o.CoordSys.RefDir[1],o.CoordSys.RefDir[2]),p=new s.Vector3(o.CoordSys.Axis[0],o.CoordSys.Axis[1],o.CoordSys.Axis[2]),f=new s.Vector3;f.crossVectors(p,u),h.set(u.x,f.x,p.x,u.y,f.y,p.y,u.z,f.z,p.z);var m=new s.Matrix4;m.set(h.elements[0],h.elements[3],h.elements[6],l,h.elements[1],h.elements[4],h.elements[7],d,h.elements[2],h.elements[5],h.elements[8],c,0,0,0,1),r.multiply(m)}return void 0!==a&&a.setMatrix(r),a}}};return f}),define("DS/XCADLoader/XCADModelLoaderPlug",["DS/XCADLoader/IModelLoaderPlug","DS/XCADLoader/XCADLoader","DS/XCADSTEPDocument/STEPInputDocument","DS/XCADSTPDocument/STPInputDocument","DS/XCADSTPZDocument/STPZInputDocument","DS/XCADSTPXDocument/STPXInputDocument","DS/XCADSTPXZDocument/STPXZInputDocument","DS/XCADSTPADocument/STPAInputDocument","DS/XCADSTLDocument/STLInputDocument"],function(e,t,r,n,i,s,o,a,l){"use strict";var d=2,c=1,h=new function(e){let t=new Array(e),r=0,n=0,i=!1;this.addValue=function(e,s){i&&r!=t.length&&r++,r==t.length&&(r=0),i&&r==n&&n++,n==t.length&&(n=0),t[r]={x:e,y:s},i=!0},this.findValue=function(e){let s=n,o=0;for(n>r||r==t.length-1?o=t.length:i&&(o=r-n+1);0!=o;){if(null!=t[s]&&e==t[s].x)return t[s].y;++s==t.length&&(s=0),o--}return null}}(3),u=function(){this.converter=null};return(u.prototype=Object.create(e)).isManagedType=function(e){var t=e.toUpperCase()+"InputDocument";return"cgr"!=e&&("obj"==e&&(t="XCAD"+e.toUpperCase()+"InputDocument"),void 0!==require("DS/XCAD"+e.toUpperCase()+"Document/"+t))},u.prototype.isSuppotedStep=async function(e){let t=!1;return null!=e&&(t=await async function(e){let t=!1;try{const r=await fetch(e);if(!r.ok)throw new Error(r.statusText);const n=await r.text();return h.addValue(e,n),null!=n.match(/#\d*=TESSELLATED_SHAPE_REPRESENTATION/)&&(t=!0),t}catch(e){throw console.error(e),e}}(e)),t},u.prototype.loadModel=function(e,r,n,i,s,o,a,l,u,p,f,m,g){var v=e.toUpperCase()+"InputDocument";"obj"==e&&(v="XCAD"+e.toUpperCase()+"InputDocument");var D=require("DS/XCAD"+e.toUpperCase()+"Document/"+v);if(void 0===D)return!1;var b=h.findValue(i);null===b&&(b=g),null===this.converter&&(this.converter=new t);var C=new D;return"stpz"==e?(C.zipped=!0,C.zipMethod=d):"stpx"==e?C.sceneGraph=f:"stpxz"==e?C.zipped=!0:"stpa"==e?(C.zipped=!0,C.zipMethod=c):C.zipped=!1,C.spec=m,C.url=i,this.converter.load(C,i,s,o,a,l,f,C.zipped,C.requestResponseType,m,b),C=null,!0},u});