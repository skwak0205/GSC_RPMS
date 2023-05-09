define("DS/VisuLoaders/ThreeDXStreamer",["DS/Visualization/ThreeJS_DS","DS/Visualization/Utils","DS/Mesh/MeshUtils","DS/Visualization/Node3D","DS/Visualization/Mesh3D","DS/Visualization/MaterialManager","DS/ZipJS/zip"],function(e,r,a,t,i,n,o){"use strict";var s=null,l=new e.Box3,u=new e.Vector3;return{stream:function(t){var i=["vertexIndexArray","vertexPositionArray","vertexNormalArray","vertexUvArray","vertexUv2Array","vertexColorArray","vertexTangentArray","vertexBinormalArray"],n={vertexPositionArray:1,vertexNormalArray:2,vertexUvArray:4,vertexUv2Array:8,vertexColorArray:16,vertexTangentArray:32,vertexBinormalArray:64},m=["none","PLANAR","SPHERICAL","CUBIC","FINITE_CYLINDRICAL","INFINITE_CYLINDRICAL"],f={1003:"NEAREST",1004:"NEAREST_MIPMAP_NEAREST",1005:"NEAREST_MIPMAP_LINEAR",1006:"LINEAR",1007:"LINEAR_MIPMAP_NEAREST",1008:"LINEAR_MIPMAP_LINEAR"},d={1000:"REPEAT",1001:"CLAMP_TO_EDGE",1002:"MIRRORED_REPEAT"},g={0:"POINTS",1:"LINES",4:"TRIANGLES"},p=!!t.zipped||!1,c=!!t.concat||!1,h=t.compressNormals||!1,v=t.compressVertices||!1,y=null,x=null,b=t.layers||!1,M=t.streamEdges||!1,A=t.streamPoints||!1,T=void 0!==t.primitives&&t.primitives,S=t.reps||!1,C=t.node?t.node:t,L=(t.metalnessToSpecularConverter&&t.metalnessToSpecularConverter,t.nodeCallback),_=t.imageCallback,w=t.endCallback,N=null,E=(t.embeddedTextures,0),O=0,I=new WeakMap,D=0,R={binaries:{},vertexLayouts:{},buffers:{},images:{},textures:{},materials:{},geometries:{},nodes:{}},U={header:{generator:"Dassault Systemes 3DX generator",version:"3.0",upAxis:"Z",unit:"mm"},binaries:[],buffers:[],vertexLayouts:[],images:[],textures:[],materials:[],geometries:[],nodes:[],extensionsUsed:[],extensionsRequired:[],root:null},z=new Set,B=new Set,P=104857600,F={geometry:0,image:1},k=[],G={json:U,chunks:k,zipData:null};function V(e,r,a,t){for(var i=a,n=0;n<t;n++)r[i++]=e[n];return i}function X(e,r,a){var t=function(e){for(var r=new ArrayBuffer(2*e.length),a=new Uint16Array(r),t=0,i=e.length;t<i;t++)a[t]=e.charCodeAt(t);return r}(JSON.stringify(e.json,void 0,2)),i=4+t.byteLength;i%4&&(i+=4-i%4);for(var n=0;n<e.chunks.length;n++)(i+=e.chunks[n].data.byteLength)%4&&(i+=4-i%4);var o=new ArrayBuffer(i),s=4,l=new Uint32Array(o,0,1),u=new Uint8Array(o);l[0]=t.byteLength,(s=V(new Uint8Array(t),u,s,t.byteLength))%4&&(s+=4-s%4);for(n=0;n<e.chunks.length;n++){var m=e.chunks[n].data;(s=V(m,u,s,m.byteLength))%4&&(s+=4-s%4)}e.concatData=o,r&&r()}function W(e,r,a){x||(x=new o.BlobWriter);for(var t=JSON.stringify(e.json,void 0,2),i=new Blob([t],{type:"text/plain"}),n=0,s=[{data:i}],l=0;l<e.chunks.length;l++)s.push({index:l,data:new Blob([e.chunks[l].data],{type:"arraybuffer"})});function u(){var a=s[n],t=n?e.json.binaries[a.index].uri:"manifest.json";y.add(t,new o.BlobReader(a.data),function(){++n<s.length?u():y.close(function(a){e.zipData=a,r&&r()})},null)}y?u():o.createWriter(x,function(e){y=e,u()},a)}function j(e){for(var r=0;r<k.length;r++){var a=k[r];e.binaries[r].byteLength=a.size,a.data=new Uint8Array(a.size);for(var t=0;t<a.buffers.length;t++)for(var i=a.buffers[t],n=i.buffer,o=i.offset,s=0;s<n.byteLength;s++)a.data[o+s]=n[s]}return k}function Y(e,r){var a=k[F[r]];(!a||a.size+e.byteLength>P)&&(F[r]=k.length,a={size:0,buffers:[],data:null,type:r},k.push(a));var t=a.size;return a.buffers.push({buffer:new Uint8Array(e.buffer,e.byteOffset,e.byteLength),offset:t}),a.size+=e.byteLength,a.size%4&&(a.buffers.push({buffer:new Uint8Array(4-a.size%4),offset:a.size}),a.size+=4-a.size%4),t}function J(){E--,console.log("nb textures loading: "+E),N&&!E&&(G.chunks=j(U),p?W(G,function(){N(G)}):c?X(G,function(){N(G)}):N(G))}function H(e,r,a,t){var i={name:a,format:r},n=new FileReader;return n.onload=function(){var e=new DataView(this.result),r=Y(e,"image");i.binary=ee(F,"image",t),i.byteOffset=r,i.byteLength=e.byteLength,J()},n.readAsArrayBuffer(e),i}function q(a,t,i){if(a.image)if(a.image.getContext||a.image.tagName&&"img"===a.image.tagName.toLowerCase())if(a.sourceFile&&"blob:"===a.sourceFile.substring(0,5)){if(!(c=R.images[a.sourceFile])){E++,console.log("nb textures loading: "+E);var n=function(e,r){var a={name:"image_"+e.sourceFile},t=new XMLHttpRequest;return t.open("GET",e.sourceFile,!0),t.responseType="blob",t.onload=function(e){if(200===this.status){var t=this.response.type;a.format="image/jpeg"===t?"jpeg":"png";var i=new FileReader;i.onload=function(){var e=new DataView(this.result),t=Y(e,"image");a.binary=ee(F,"image",r),a.byteOffset=t,a.byteLength=e.byteLength,J()},i.readAsArrayBuffer(this.response),console.log("canvas blob")}},t.send(),a}(a,t);c={index:t.images.length,item:n},t.images.push(n),R.images[a.sourceFile]=c}i.image=c.index}else{var o="jpeg";if(!(y=a.image).getContext){if(y.src){var l=r.getExtension(y.src);l&&"png"===l.toLowerCase()&&(o="png")}(y=s||document.createElement("canvas")).width=a.image.width,y.height=a.image.height,(x=y.getContext("2d")).drawImage(a.image,0,0,a.image.width,a.image.height)}for(var u=atob(y.toDataURL("image/"+o).split(",")[1]),m=new Array(u.length),f=0;f<u.length;f++)m[f]=u.charCodeAt(f);var d=new Uint8Array(m),g=new Blob([d],{type:"image/"+o}),p="img_"+O;O++,E++,console.log("nb textures loading: "+E);n=H(g,o,p,t);var c={index:t.images.length,item:n};t.images.push(n),R.images[p]=c,i.image=c.index,console.log("png")}else if(a.mipmaps&&a.mipmaps.length){var h=a.image.width,v=a.image.height;g=new Blob([e.ImageUtils.streamDDS(a)],{type:"arraybuffer"}),p="img_"+O;O++,E++,console.log("nb textures loading: "+E);n=H(g,"dds",p,t),c={index:t.images.length,item:n};t.images.push(n),R.images[p]=c,i.image=c.index,console.log("dds")}else{var y;h=a.image.width,v=a.image.height;(y=s||document.createElement("canvas")).width=h,y.height=v;var x,b=(g=(x=y.getContext("2d")).getImageData(0,0,h,v)).data;if(1021==a.format)for(f=0;f<v;f++)for(var M=0;M<4*h;M++)b[4*(v-f-1)*h+M]=a.image.data[4*f*h+M];else if(1020==a.format)for(f=0;f<v;f++)for(M=0;M<h;M++){for(var A=0;A<3;A++)b[4*(v-f-1)*h+4*M+A]=a.image.data[3*f*h+3*M+A];b[4*(v-f-1)*h+4*M+3]=255}else console.log("texture format export to code :"+a.format);x.putImageData(g,0,0,0,0,h,v);for(u=atob(y.toDataURL("image/png").split(",")[1]),m=new Array(u.length),f=0;f<u.length;f++)m[f]=u.charCodeAt(f);d=new Uint8Array(m),g=new Blob([d],{type:"image/png"}),p="img_"+O;O++,E++,console.log("nb textures loading: "+E);n=H(g,"png",p,t),c={index:t.images.length,item:n};t.images.push(n),R.images[p]=c,i.image=c.index,console.log("uncompressed")}}function Z(r,a,t){var i;if(!i){var n={anisotropy:r.anisotropy,magFilter:f[r.magFilter],minFilter:f[r.minFilter],uWrap:d[r.wrapS],vWrap:d[r.wrapT],mapping:a};if(_)_(r,n);else if(r.loadEndStatus&&r.loadEndStatus!==e.AssetLoadingStatus.READY){if(r.loadEndStatus===e.AssetLoadingStatus.ERROR)return void console.log("Texture corrupted: impossible to embed it in 3dx archive.");r.onLoadCallbacks.push(function(e){q(e,t,n)})}else q(r,t,n);i={index:t.textures.length,item:n},t.textures.push(n)}return i.index}function K(r,a){var t=R.textures[r.id];if(!t){var i={anisotropy:r.anisotropy,magFilter:f[r.magFilter],minFilter:f[r.minFilter],uWrap:d[r.wrapS],vWrap:d[r.wrapT]};if(_)_(r,i);else if(r.loadEndStatus&&r.loadEndStatus!==e.AssetLoadingStatus.READY){if(r.loadEndStatus===e.AssetLoadingStatus.ERROR)return void console.log("Texture corrupted: impossible to embed it in 3dx archive.");r.onLoadCallbacks.push(function(e){q(e,a,i)})}else q(r,a,i);t={index:a.textures.length,item:i},a.textures.push(i),R.textures[r.id]=t}return t.index}function Q(e){return[e.r,e.g,e.b]}function $(r,a){var t="Phong";if(r instanceof e.ParticleBasicMaterial)t="PointBasic";else if(r instanceof e.LineBasicMaterial)t="LineBasic";else if(r instanceof e.MeshBasicMaterial)t="MeshBasic";else if(r instanceof e.PhysicalMaterial){t="Physical";var i="EXT_Material_PBR";z.has(i)||(z.add(i),U.extensionsUsed.push(i)),B.has(i)||(B.add(i),U.extensionsRequired.push(i))}else r instanceof e.MeshLambertMaterial&&(t="Lambert");var n={type:t,visible:r.visible,side:r.side,transparent:r.transparent,alphaTest:r.alphaTest,depthTest:r.depthTest,depthWrite:r.depthWrite};if(r instanceof e.PhysicalMaterial){var o=function(e,t,i){n[e]={};var o=n[e];if(void 0!==r[t]){if(void 0===r[t+"Map"])return void(n[e]="number"==typeof r[t]?r[t]:r[t].x);o.value="number"==typeof r[t]?r[t]:r[t].x}if(r[t+"Map"])if(i){var s=r[t+"UvTransform"].elements;if(r.mappingType>0)i={operator:{type:m[r.mappingType],transform:r.mappingObjTransformation.elements},uvTransform:[s[0],s[1],s[2],0,s[3],s[4],s[5],0,s[6],s[7],s[8],0,0,0,0,1]};else{r[t+"UvSlot"]&&2===r[t+"UvSlot"]&&"TEXCOORD_1",i={slot:"slot",uvTransform:[s[0],s[1],s[2],0,s[3],s[4],s[5],0,s[6],s[7],s[8],0,0,0,0,1]}}o.texture=Z(r[t+"Map"],i,a)}else o.texture=K(r[t+"Map"],a);r[t+"MulCoef"]>0&&(o.MADCoefficients=[r[t+"MulCoef"],r[t+"AddCoef"]])},s=function(e,t,i){n[e]={};var o=n[e];if(void 0!==r[t+"Map"]){if(o.value=Q(r[t]),r[t+"Map"])if(i){var s=r[t+"UvTransform"].elements;if(r.mappingType>0)i={operator:{type:m[r.mappingType],transform:r.mappingObjTransformation.elements},uvTransform:[s[0],s[1],s[2],0,s[3],s[4],s[5],0,s[6],s[7],s[8],0,0,0,0,1]};else{var l="TEXCOORD_0";r[t+"UvSlot"]&&2===r[t+"UvSlot"]&&(l="TEXCOORD_1"),i={slot:l,uvTransform:[s[0],s[1],s[2],0,s[3],s[4],s[5],0,s[6],s[7],s[8],0,0,0,0,1]}}o.texture=Z(r[t+"Map"],i,a)}else o.texture=K(r[t+"Map"],a);r[t+"MulCoef"]&&(o.MADCoefficients=[r[t+"MulCoef"].x,r[t+"MulCoef"].y,r[t+"MulCoef"].z,r[t+"AddCoef"].x,r[t+"AddCoef"].y,r[t+"AddCoef"].z])}else n[e]=Q(r[t])};if(n.albedo={},n.albedo.value=Q(r.color),r.map){var l,u=r.diffuseUvTransform.elements;if(r.mappingType>0)l={operator:{type:m[r.mappingType],transform:r.mappingObjTransformation.elements},uvTransform:[u[0],u[1],u[2],0,u[3],u[4],u[5],0,u[6],u[7],u[8],0,0,0,0,1]};else{var f="TEXCOORD_0";r.diffuseUvSlot&&2===r.diffuseUvSlot&&(f="TEXCOORD_1"),l={slot:f,uvTransform:[u[0],u[1],u[2],0,u[3],u[4],u[5],0,u[6],u[7],u[8],0,0,0,0,1]}}n.albedo.texture=Z(r.map,l,a)}r.diffuseMulCoef&&(n.albedo.MADCoefficients=[r.diffuseMulCoef.x,r.diffuseMulCoef.y,r.diffuseMulCoef.z,r.diffuseAddCoef.x,r.diffuseAddCoef.y,r.diffuseAddCoef.z]),o("metallic","metalness",!0),o("roughness","roughness",!0),function(e,t,i){n[e]={};var o=n[e];if(r[t+"Map"])if(r[t+"MapFlipY"]&&(n[t+"MapFlipY"]=r[t+"MapFlipY"]),i){var s=r[t+"UvTransform"].elements;if(r.mappingType>0)i={operator:{type:m[r.mappingType],transform:r.mappingObjTransformation.elements},uvTransform:[s[0],s[1],s[2],0,s[3],s[4],s[5],0,s[6],s[7],s[8],0,0,0,0,1]};else{var l="TEXCOORD_0";r[t+"UvSlot"]&&2===r[t+"UvSlot"]&&(l="TEXCOORD_1"),i={slot:l,uvTransform:[s[0],s[1],s[2],0,s[3],s[4],s[5],0,s[6],s[7],s[8],0,0,0,0,1]}}o.texture=Z(r[t+"Map"],i,a)}else o.texture=K(r[t+"Map"],a)}("normal","normal",!0),o("normalScale","normalScale",!1),o("transparency","transparency",!0),o("cutoutOpacity","opacity",!0),o("specular","specularContrib",!0),s("specularTint","specular",!0),s("emissionColor","emissionColor",!0),o("emissionValue","emissionValue",!1),n.emissionEnergyNormalization=r.emissionNormalized,n.emissionMode="LUMINOUS_EMITTANCE",n.thinWalled=r.thinWalled,o("ior","ior",!1),n.vertexColors=r.vertexColors,n.alphaTest=r.alphaTest,r.occlusionMap&&(n.occlusionMap=K(r.occlusionMap,a)),n.uv0Transform=[],r.mappingUVTransformation.flattenToArray(n.uv0Transform),n={extensions:{EXT_Material_PBR:n}}}else n.opacity=r.opacity,r.color&&(n.color=Q(r.color)),r instanceof e.ParticleBasicMaterial&&(n.size=r.size,n.sizeAttenuation=r.sizeAttenuation),r instanceof e.LineBasicMaterial&&(n.lineWidth=r.lineWidth),(r instanceof e.MeshPhongMaterial||r instanceof e.MeshLambertMaterial)&&(n.ambient=Q(r.ambient),n.shininess=r.shininess),(r instanceof e.MeshPhongMaterial||r instanceof e.MeshLambertMaterial)&&(n.emissive=Q(r.emissive),r.specular&&(n.specular=Q(r.specular)),n.vertexColors=r.vertexColors,r.map&&(n.diffuseMap=K(r.map,a)),r.bumpMap&&(n.bumpMap=K(r.bumpMap,a)),r.normalMap&&(n.normalMap=K(r.normalMap,a)),r.specularMap&&(n.specularMap=K(r.specularMap,a)),r.bumpMap&&void 0!==r.bumpScale&&(n.bumpMapScale=r.bumpScale),r.normalMap&&void 0!==r.normalScale&&(n.normalMapScale=r.normalScale));return n}function ee(e,r,a){var t=e[r];return a.binaries[t]||(a.binaries[t]={uri:r+"_"+t+".bin",byteLength:0}),t}function re(e,r){var a=Y(e,"geometry");return{binary:ee(F,"geometry",r),byteOffset:a,byteLength:e.byteLength}}function ae(e,r){var a=re(e,r);return e instanceof Uint32Array?a.format="UNSIGNED_INT":a.format="UNSIGNED_SHORT",r.buffers.push(a),r.buffers.length-1}function te(e,r){var a=re(e,r);return r.buffers.push(a),r.buffers.length-1}function ie(e){for(var r=e.length/3,a=new Uint8Array(3*r),t=0;t<r;t++){var i=e[3*t],n=e[3*t+1],o=e[3*t+2],s=Math.abs(i)+Math.abs(n)+Math.abs(o),l=i/s,u=n/s;if(o<0){var m=(1-Math.abs(u))*(l>=0?1:-1),f=(1-Math.abs(l))*(u>=0?1:-1);l=m,u=f}l=Math.round(4095*(.5*l+.5)),u=Math.round(4095*(.5*u+.5)),a[3*t]=Math.floor(l/16),a[3*t+1]=l%16*16+Math.floor(u/256),a[3*t+2]=u%256}return a}function ne(r,a){var t=r.length/3,i=new Uint16Array(3*t),n=new e.Vector3(Math.max(a.max.x-a.min.x,1e-6),Math.max(a.max.y-a.min.y,1e-6),Math.max(a.max.z-a.min.z,1e-6));n.multiplyScalar(1/65535);var o=(new e.Vector3).copy(a.min);o.x/=n.x,o.y/=n.y,o.z/=n.z,o.x=Math.floor(o.x),o.y=Math.floor(o.y),o.z=Math.floor(o.z);for(var s=function(e,r,a){return Math.max(Math.min(e,a),r)},l=0;l<t;l++){var u=Math.round(r[3*l]/n.x)-o.x,m=Math.round(r[3*l+1]/n.y)-o.y,f=Math.round(r[3*l+2]/n.z)-o.z;i[3*l]=s(u,0,65535),i[3*l+1]=s(m,0,65535),i[3*l+2]=s(f,0,65535)}return i}function oe(e){for(var r=0,a=0;a<e.getPrimitivesCount();a++){e.getPrimitiveGroup(a).glType>=4&&(r+=e.getPrimitiveCount(a)/3)}return r}function se(e,r){l.makeEmpty();for(var a=0;a<e.getPrimitivesCount();a++)for(var t=e.getPrimitiveCount(a),i=e.getPrimitiveStart(a),n=0;n<t;n++)r.getVertexPosition(r.vertexIndexArray[i+n],u),l.expandByPoint(u);return l.getBoundingSphere()}function le(r,t){var o,s,l={indexBuffer:null,vertexBuffers:[],drawingGroups:[],normalCompression:h?"OCT_24":"NONE",positionCompression:v?"BBOX_QUANTIZATION_16_16_16":"NONE"};S&&(o={},s=[]);var u=r.boundingSphere;u&&(l.boundingSphere={center:[u.center.x,u.center.y,u.center.z],radius:u.radius});var m=null;v&&(r.boundingBox||r.computeBoundingBox(),(m=r.boundingBox.clone())&&(l.boundingBox={min:[m.min.x,m.min.y,m.min.z],max:[m.max.x,m.max.y,m.max.z]}));for(var f=0,d=0;d<i.length;d++){var p=i[d],c=r[p],y="vertexIndexArray"===p,x="vertexNormalArray"===p;c&&("vertexPositionArray"===p&&v&&(c=ne(c,m)),x&&h&&(c=ie(c)),y?l.indexBuffer=ae(c,t):(l.vertexBuffers.push(te(c,t)),f+=n[p]))}l.vertexLayout=function(e,r){var a=R.vertexLayouts[e];if(null==a){a=R.vertexLayouts[e]=r.vertexLayouts.length;var t=[],i=1;if(0!=(e&i)){var n=v?"UNSIGNED_SHORT":"FLOAT";t[t.length]=[{attribute:"POSITION",format:n,dimension:3}]}0!=(e&(i<<=1))&&(n=h?"UNSIGNED_BYTE":"FLOAT",t[t.length]=[{attribute:"NORMAL",format:n,dimension:3}]),0!=(e&(i<<=1))&&(t[t.length]=[{attribute:"TEX_COORD_0",format:"FLOAT",dimension:3}]),0!=(e&(i<<=1))&&(t[t.length]=[{attribute:"TEX_COORD_1",format:"FLOAT",dimension:3}]),0!=(e&(i<<=1))&&(t[t.length]=[{attribute:"COLOR",format:"FLOAT",dimension:4}]),0!=(e&(i<<=1))&&(t[t.length]=[{attribute:"TANGENT",format:"FLOAT",dimension:3}]),0!=(e&(i<<=1))&&(t[t.length]=[{attribute:"BINORMAL",format:"FLOAT",dimension:3}]),r.vertexLayouts[a]=t}return a}(f,t);for(d=0;d<r.drawingGroups.length;d++){var C=r.drawingGroups[d];if((1!==C.glType||M)&&(0!==C.glType||A)){b&&hasLayers&&(C.__id__=d);var L=a.GeomTypeString[C._geomType],_={start:C.start,count:C.count,mode:g[C.glType],geometricType:L||"UNKNOWN"},w=[],N=-1,E=0;if(T||S){var O;T&&(O=new Uint32Array(4*C.getPrimitivesCount()));for(var U=0;U<C.getPrimitivesCount();U++){var z=C.getPrimitiveRep(U),B=C.getPrimitiveStart(U),P=C.getPrimitiveCount(U),F=C.getPrimitiveCgmId(U);if(S&&z){var k=I.get(z);if(!k){var G=se(z,r),V=oe(z);k={uid:D,cgmId:z.cgmId,solid:z.solid,sag:z.sag,bsphere:G,nbTriangles:V},I.set(z,k),D++}o[k.uid]||(o[k.uid]=s.length,s.push(k)),N<0&&(N=o[k.uid])}T?(O[4*U]=F,O[4*U+1]=k?o[k.uid]:z?z.cgmId:0,O[4*U+2]=B,O[4*U+3]=P):o[k.uid]!==N?(w.push({index:N,count:E}),N=o[k.uid],E=P):E+=P}if(T)_.primitives=te(O,t);else{w.push({index:N,count:E});for(var X=new Uint32Array(2*w.length),W=0;W<w.length;W++)X[2*W]=w[W].index,X[2*W+1]=w[W].count;_.repRanges=te(X,t)}}if(l.drawingGroups.push(_),C.material instanceof e.Material){var j=R.materials[C.material.id];if(!j){var Y=$(C.material,t);j={index:t.materials.length,item:Y},t.materials.push(Y),R.materials[C.material.id]=j}_.material=j.index}if(C.gas){var J=R.materials[C.gas.id];if(!J){Y=$(C.gas,t);J={index:t.materials.length,item:Y},t.materials.push(Y),R.materials[C.gas.id]=J}_.graphicAttributes=J.index}}}if(S&&s){var H=4*s.length,q=9*s.length,Z=new Uint8Array(4*q),K=new Uint32Array(Z.buffer,0),Q=new Float32Array(Z.buffer,4*H);for(W=0;W<s.length;W++){var ee=s[W];K[4*W]=ee.uid,K[4*W+1]=ee.cgmId,K[4*W+2]=ee.solid,K[4*W+3]=ee.nbTriangles,Q[5*W]=ee.bsphere.center.x,Q[5*W+1]=ee.bsphere.center.y,Q[5*W+2]=ee.bsphere.center.z,Q[5*W+3]=ee.bsphere.radius,Q[5*W+4]=ee.sag}l.reps=te(Z,t)}return l}C.traverse(function(e,r){var a=R.nodes[e.id];if(!a){var t=e.getNodeType();if(a={type:t},e._matrix&&!e._matrix.isIdentity()){var i=[];e._matrix.flattenToArray(i),a.matrix=i}if(e.material&&e.material.force){if(!(p=R.materials[e.material.id])){var n=$(e.material,r);p={index:r.materials.length,item:n},r.materials.push(n),R.materials[e.material.id]=p}a.material=p.index}if(R.nodes[e.id]={index:r.nodes.length,item:a},null===r.root&&(r.root=r.nodes.length),r.nodes.push(a),"Node3D"===t){for(var o=[],s=0;s<e.children.length;s++)o.push(e.children[s].id);a.children=o}else if("LODNode3D"===t){var l,u=[];for(s=0;s<e._representations.length;s++){for(l=[],A=0;A<e._representations[s].length;A++)l.push(e._representations[s][A].id);u.push(l)}a.representations=u,(d=e._boundingSphere)&&(a.boundingSphere={center:[d.center.x,d.center.y,d.center.z],radius:d.radius}),(m=e._boundingBox)&&(a.boundingBox={min:[m.min.x,m.min.y,m.min.z],max:[m.max.x,m.max.y,m.max.z]}),a.lodSelector=e._lodSelector&&e._lodSelector.createJSON()}else if("URLNode3D"===t)a.url=e.url,(d=e._boundingSphere)&&(a.boundingSphere={center:[d.center.x,d.center.y,d.center.z],radius:d.radius});else if("LDHNode3D"===t)a.url=e.url,(d=e._boundingSphere)&&(a.boundingSphere={center:[d.center.x,d.center.y,d.center.z],radius:d.radius}),a.lodSelector=e._lodSelector&&e._lodSelector.createJSON();else if(e._occurrences[0]&&e._occurrences[0].renderable){var m,f=e._occurrences[0].renderable,d=f.boundingSphere;(m=f.boundingBox)||(m=d.getBoundingBox());var g=!1;if(b)for(s=0;s<e._occurrences.length;s++)if(e._occurrences[s].renderable.layer){g=!0;break}var p,c=[];for(s=0;s<f.geometry.length;s++){var h=f.geometry[s].id,v=R.geometries[h];if(!v){var y=le(f.geometry[s],r);v={index:r.geometries.length,item:y},r.geometries.push(y),R.geometries[h]=v}c.push(v.index)}if(a.boundingSphere={center:[d.center.x,d.center.y,d.center.z],radius:d.radius},a.boundingBox={min:[m.min.x,m.min.y,m.min.z],max:[m.max.x,m.max.y,m.max.z]},a.geometries=c,b&&g)for(a.occurrences={},s=0;s<e._occurrences.length;s++){var x=e._occurrences[s].renderable;if(x.layer){var M=[];a.occurrences[s]={index:s,layers:M};for(var A=0;A<x.layer.layers.length;A++){var T=x.layer.layers[A],S={start:T.drawableInterval.start,count:T.drawableInterval.count,primitiveStart:T.drawableInterval.primitiveStart,primitiveCount:T.drawableInterval.primitiveCount,layerNum:T.drawableInterval.layerNum};T.drawableInterval.material&&(S.material=T.drawableInterval.material.id,(p=r.materials[S.material])||(p=T.drawableInterval.material,r.materials[S.material]=$(p,r))),M.push({geometry:T.geometry.id,drawableGroup:T.drawableGroup.__id__,drawableInterval:S})}}}if(!e.material&&f.material&&f.material.force)(p=R.materials[f.material.id])||(n=$(f.material,r),p={index:r.materials.length,item:n},r.materials.push(n),R.materials[f.material.id]=p),a.material=p.index}else a.type="Node3D";L&&L(e,a)}},U,!1,!0);for(var ue=0;ue<U.nodes.length;ue++){var me,fe=[];if("LODNode3D"===(C=U.nodes[ue]).type)for(var de=0;de<C.representations.length;de++)fe.push(C.representations[de]);else C.children&&"URLNode3D"!==C.type&&fe.push(C.children);for(me=0;me<fe.length;me++){var ge=fe[me];for(de=0;de<ge.length;de++)ge[de]=R.nodes[ge[de]].index}}w&&(N=w),!E&&N&&(G.chunks=j(U),p?W(G,function(){N(G)}):c?X(G,function(){N(G)}):N(G))}}});