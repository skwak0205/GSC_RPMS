define("DS/GPUFormats/FormatEnums",["DS/Mesh/ThreeJS_Base"],function(e){"use strict";var t={UVMapping:function(){},CubeReflectionMapping:function(){},CubeRefractionMapping:function(){},SphericalReflectionMapping:function(){},SphericalRefractionMapping:function(){},LatLongReflectionMapping:function(){},LatLongRefractionMapping:function(){},SimpleEnvMapping:function(){},SimpleEnvMappingPreserveRatio:function(){},SimpleEnvMappingFit:function(){},SimpleEnvMappingFitWidth:function(){},SimpleEnvMappingFitHeight:function(){},SimpleEnvMappingCustom:function(){},CubeEnvMapping:function(){},LightProbeEnvMapping:function(){},LatLongEnvMapping:function(){},RepeatWrapping:1e3,ClampToEdgeWrapping:1001,MirroredRepeatWrapping:1002,_ClampToBorderWrapping:1025,NearestFilter:1003,NearestMipMapNearestFilter:1004,NearestMipMapLinearFilter:1005,LinearFilter:1006,LinearMipMapNearestFilter:1007,LinearMipMapLinearFilter:1008,UnsignedByteType:1009,ByteType:1010,ShortType:1011,UnsignedShortType:1012,IntType:1013,UnsignedIntType:1014,FloatType:1015,HalfFloatType:1024,UnsignedShort4444Type:1016,UnsignedShort5551Type:1017,UnsignedShort565Type:1018,AlphaFormat:1019,RGBFormat:1020,RGBAFormat:1021,LuminanceFormat:1022,LuminanceAlphaFormat:1023,CompressedFormats:2e3,MaxDDSFormats:2013,RGB_S3TC_DXT1_Format:2001,RGBA_S3TC_DXT1_Format:2002,RGBA_S3TC_DXT3_Format:2003,RGBA_S3TC_DXT5_Format:2004,RED_RGTC1_Format:2005,RED_RGTC1_S_Format:2006,REDGREEN_RGTC2_Format:2007,REDGREEN_RGTC2_S_Format:2008,RGBA_BPTC_UNORM_Format:2009,SRGB_ALPHA_BPTC_UNORM_Format:2010,RGB_BPTC_SIGNED_FLOAT_Format:2011,RGB_BPTC_UNSIGNED_FLOAT_Format:2012,RGB_ETC1_Format:2201,RGB_PVRTC_2BPPV1_Format:2301,RGB_PVRTC_4BPPV1_Format:2302,RGBA_PVRTC_2BPPV1_Format:2303,RGBA_PVRTC_4BPPV1_Format:2304,R11_EAC_Format:2401,SIGNED_R11_EAC_Format:2402,RG11_ETC2_EAC_Format:2403,SIGNED_RG11_ETC2_EAC_Format:2404,RGB8_ETC2_Format:2405,RGBA8_ETC2_EAC_Format:2406,SRGB8_ETC2_Format:2407,SRGB8_ALPHA8_ETC2_EAC_Format:2408,RGB8_PUNCHTHROUGH_ALPHA1_ETC2_Format:2409,SRGB8_PUNCHTHROUGH_ALPHA1_ETC2_Format:2410},i=["4x4","5x4","5x5","6x5","6x6","8x5","8x6","8x8","10x5","10x6","10x8","10x10","12x10","12x12"];return function(){for(var e=0;e<i.length;e++){var n=i[e],s=2*e;t["RGBA_ASTC_"+n+"_Format"]=2100+s+1,t["SRGB8_ALPHA8_ASTC_"+n+"_Format"]=2100+s+2}}(),t}),define("DS/GPUFormats/TextureLODPool",["DS/Mesh/ThreeJS_Base"],function(e){"use strict";var t=function(){this.totalSize=0,this.textures=[]};return t.prototype.removeUnusedTextures=function(){for(var t=[],i=0;i<this.textures.length;i++){var n=this.textures[i];if(n.textureLOD.usedTexture!==n.lod){var s=n.textureLOD.textures[n.lod],r=n.textureLOD.textures[n.textureLOD.usedTexture];r&&r.loadEndStatus!==e.AssetLoadingStatus.READY?t.push(n):(s.dispose(),n.textureLOD.textures[n.lod]=null,this.totalSize-=n.size)}else t.push(n)}this.textures=t},t.prototype.add=function(e){this.totalSize+=e.size,this.textures.push(e)},t}),define("DS/GPUFormats/VertexAttribute",[],function(){"use strict";return{float16:{base:"float16",components:1,normalized:!1,size:2,native:void 0},float16x2:{base:"float16",components:2,normalized:!1,size:4,native:void 0},float16x3:{base:"float16",components:3,normalized:!1,size:6,native:void 0},float16x4:{base:"float16",components:4,normalized:!1,size:8,native:void 0},float32:{base:"float32",components:1,normalized:!1,size:4,native:void 0},float32x2:{base:"float32",components:2,normalized:!1,size:8,native:void 0},float32x3:{base:"float32",components:3,normalized:!1,size:12,native:void 0},float32x4:{base:"float32",components:4,normalized:!1,size:16,native:void 0},sint8:{base:"sint8",components:1,normalized:!1,size:1,native:void 0},sint8x2:{base:"sint8",components:2,normalized:!1,size:2,native:void 0},sint8x3:{base:"sint8",components:3,normalized:!1,size:3,native:void 0},sint8x4:{base:"sint8",components:4,normalized:!1,size:4,native:void 0},sint16:{base:"sint16",components:1,normalized:!1,size:2,native:void 0},sint16x2:{base:"sint16",components:2,normalized:!1,size:4,native:void 0},sint16x3:{base:"sint16",components:3,normalized:!1,size:6,native:void 0},sint16x4:{base:"sint16",components:4,normalized:!1,size:8,native:void 0},sint32:{base:"sint32",components:1,normalized:!1,size:4,native:void 0},sint32x2:{base:"sint32",components:2,normalized:!1,size:8,native:void 0},sint32x3:{base:"sint32",components:3,normalized:!1,size:12,native:void 0},sint32x4:{base:"sint32",components:4,normalized:!1,size:16,native:void 0},snorm8:{base:"sint8",components:1,normalized:!0,size:1,native:void 0},snorm8x2:{base:"sint8",components:2,normalized:!0,size:2,native:void 0},snorm8x3:{base:"sint8",components:3,normalized:!0,size:3,native:void 0},snorm8x4:{base:"sint8",components:4,normalized:!0,size:4,native:void 0},snorm16:{base:"sint16",components:1,normalized:!0,size:2,native:void 0},snorm16x2:{base:"sint16",components:2,normalized:!0,size:4,native:void 0},snorm16x3:{base:"sint16",components:3,normalized:!0,size:6,native:void 0},snorm16x4:{base:"sint16",components:4,normalized:!0,size:8,native:void 0},uint8:{base:"uint8",components:1,normalized:!1,size:1,native:void 0},uint8x2:{base:"uint8",components:2,normalized:!1,size:2,native:void 0},uint8x3:{base:"uint8",components:3,normalized:!1,size:3,native:void 0},uint8x4:{base:"uint8",components:4,normalized:!1,size:4,native:void 0},uint16:{base:"uint16",components:1,normalized:!1,size:2,native:void 0},uint16x2:{base:"uint16",components:2,normalized:!1,size:4,native:void 0},uint16x3:{base:"uint16",components:3,normalized:!1,size:6,native:void 0},uint16x4:{base:"uint16",components:4,normalized:!1,size:8,native:void 0},uint32:{base:"uint32",components:1,normalized:!1,size:4,native:void 0},uint32x2:{base:"uint32",components:2,normalized:!1,size:8,native:void 0},uint32x3:{base:"uint32",components:3,normalized:!1,size:12,native:void 0},uint32x4:{base:"uint32",components:4,normalized:!1,size:16,native:void 0},unorm8:{base:"uint8",components:1,normalized:!0,size:1,native:void 0},unorm8x2:{base:"uint8",components:2,normalized:!0,size:2,native:void 0},unorm8x3:{base:"uint8",components:3,normalized:!0,size:3,native:void 0},unorm8x4:{base:"uint8",components:4,normalized:!0,size:4,native:void 0},unorm16:{base:"uint16",components:1,normalized:!0,size:2,native:void 0},unorm16x2:{base:"uint16",components:2,normalized:!0,size:4,native:void 0},unorm16x3:{base:"uint16",components:3,normalized:!0,size:6,native:void 0},unorm16x4:{base:"uint16",components:4,normalized:!0,size:8,native:void 0}}}),define("DS/GPUFormats/Texture",["DS/Mesh/ThreeJS_Base","DS/GPUFormats/FormatEnums"],function(e,t){"use strict";function i(e,t,i){var n=i?e.mipmaps[0]:e.image;if(n.width<=t&&n.height<=t)return e;var s=Math.max(n.width,n.height),r=Math.floor(n.width*t/s),o=Math.floor(n.height*t/s),a=document.createElement("canvas");return a.width=r,a.height=o,a.getContext("2d").drawImage(n,0,0,n.width,n.height,0,0,r,o),i?e.mipmaps[0]=a:e.image=a,e}var n=0,s=function(i,s,r,o,a,p,m,h,l){e.EventDispatcher.call(this),this.id=n++,this.name="",this._useCount=0,this._source=e.AssetSource.MANUAL,this.forceUpdate=!1,this.hdr=!1,this.rgbHdr=!1,this.hasDiffuse=!1,this.sRGB=!0,this.image=i,this.mipmaps=[],this.extraFaces=null,this.mapping=void 0!==s?s:new t.UVMapping,this.wrapS=void 0!==r?r:t.ClampToEdgeWrapping,this.wrapT=void 0!==o?o:t.ClampToEdgeWrapping,this.magFilter=void 0!==a?a:t.LinearFilter,this.minFilter=void 0!==p?p:t.LinearMipMapLinearFilter,this.anisotropy=void 0!==l?l:1,this.format=void 0!==m?m:t.RGBAFormat,this.type=void 0!==h?h:t.UnsignedByteType,this.offset=new e.Vector2(0,0),this.repeat=new e.Vector2(1,1),this.generateMipmaps=!0,this.premultiplyAlpha=!1,this.flipY=!0,this.unpackAlignment=4,this.needsUpdate=!1,this.onUpdate=null,this.onRequest=null,this.borderColor=null,this.loadEndStatus=e.AssetLoadingStatus.INIT,this.onLoadCallbacks=[],this.__webglInit=!1,this.__webglTexture=null};return s.Face=function(e,t){this.image=e,this.mipmaps=t},s.prototype={constructor:s,clone:function(t){return void 0===t&&(t=new s),t.hdr=this.hdr,t.sRGB=this.sRGB,t.image=this.image,t.mipmaps=this.mipmaps.slice(0),t.mapping=this.mapping,t.wrapS=this.wrapS,t.wrapT=this.wrapT,t.magFilter=this.magFilter,t.minFilter=this.minFilter,t.anisotropy=this.anisotropy,t.format=this.format,t.type=this.type,t.offset.copy(this.offset),t.repeat.copy(this.repeat),t.generateMipmaps=this.generateMipmaps,t.premultiplyAlpha=this.premultiplyAlpha,t.flipY=this.flipY,t.unpackAlignment=this.unpackAlignment,t.needsUpdate=!0,t.loadEndStatus=this.loadEndStatus,this.borderColor&&(t.borderColor=new e.Vector4,t.borderColor.copy(this.borderColor)),t},_sendTextureToGPU:function(e,t,i,n,s,r,o,a){i?console.error("Error: Texture can't be a compressed format"):e.texImage2D(t,n,s,o,a,r)},_useBorderColor:function(){var e={u:!1,v:!1};return this.borderColor&&(this.wrapS===t._ClampToBorderWrapping&&(e.u=!0),this.wrapT===t._ClampToBorderWrapping&&(e.v=!0)),e},isCubeMap:function(){return!!this.extraFaces&&5===this.extraFaces.length},_setupTexture:function(e,n,r,o,a,p,m,h,l){if(!this.needsUpdate&&!this.forceUpdate)return;this.__webglInit||(this.__webglInit=!0,this.addEventListener("dispose",m),this.__webglTexture=e.createTexture(),n.info.memory.textures++,n.__glResources__.texture.push(this));const u=this.format>t.CompressedFormats,d=this.isCubeMap(),c=d?e.TEXTURE_CUBE_MAP:e.TEXTURE_2D,g=d?e.TEXTURE_CUBE_MAP_POSITIVE_X:e.TEXTURE_2D,f=this.mipmaps.length>0,T=this.mipmaps.length>1;let v=this.generateMipmaps&&!T,F=[new s.Face(this.image,this.mipmaps)];if(d&&(F=F.concat(this.extraFaces),!u&&l))for(let e=0;e<F.length;e++)F[e]=i(F[e],l,f);const x=f?F[0].mipmaps[0]:F[0].image,_=r(x.width)&&r(x.height);if(!_&&!this.nPot&&!d){let e=!1;if(!(u&&this.format<t.MaxDDSFormats)||this.wrapS===t.ClampToEdgeWrapping&&this.wrapT===t.ClampToEdgeWrapping||(e=!0),e)return void n._texturesToResize.add(this)}const z=o(this.format),S=o(this.type),b=a(z,S,v);let E=this.flipY;u&&(E&&(E=!1),v&&(v=!1)),!f||T||v||(this.minFilter!==t.NearestMipMapNearestFilter&&this.minFilter!==t.LinearMipMapNearestFilter||(this.minFilter=t.NearestFilter),this.minFilter!==t.NearestMipMapLinearFilter&&this.minFilter!==t.LinearMipMapLinearFilter||(this.minFilter=t.LinearFilter)),this.magFilter!==t.NearestMipMapNearestFilter&&this.magFilter!==t.LinearMipMapNearestFilter||(this.magFilter=t.NearestFilter),this.magFilter!==t.NearestMipMapLinearFilter&&this.magFilter!==t.LinearMipMapLinearFilter||(this.magFilter=t.LinearFilter),null!=h&&e.activeTexture(e.TEXTURE0+h),e.bindTexture(c,this.__webglTexture),e.pixelStorei(e.UNPACK_FLIP_Y_WEBGL,E),e.pixelStorei(e.UNPACK_PREMULTIPLY_ALPHA_WEBGL,this.premultiplyAlpha),e.pixelStorei(e.UNPACK_ALIGNMENT,this.unpackAlignment),p(c,this,_);for(let t=0;t<F.length;t++){const i=F[t];if(f)for(let n=0;n<i.mipmaps.length;n++){const s=i.mipmaps[n];this._sendTextureToGPU(e,g+t,u,n,b,s,z,S)}else this._sendTextureToGPU(e,g+t,u,0,b,i.image,z,S)}v&&_&&e.generateMipmap(c),this.needsUpdate=!1,this.onUpdate&&this.onUpdate()},dispose:function(){this.dispatchEvent({type:"dispose"})},setAsLOD:function(e){this.lods=[],this.textures=[],this.usedTexture=-1,this.lastUsedTexture=-1,this.switchLODTextureCB=e},chooseTexture:function(e,t){if(this.switchLODTextureCB?this.usedTexture=this.switchLODTextureCB({camera:e,object:t,texture:this,lods:this.lods,bSphere:t.geometry.boundingSphere,bBox:t.geometry.boundingBox,matrix:t.matrixWorld}):this.usedTexture=-1,-1!==this.usedTexture){var i=this.lods[this.usedTexture];!this.textures[this.usedTexture]&&i.loadCB&&(this.textures[this.usedTexture]=i.loadCB(this))}}},s}),define("DS/GPUFormats/WebGLRenderTarget",["DS/Mesh/ThreeJS_Base","DS/GPUFormats/FormatEnums"],function(e,t){"use strict";var i=0,n=function(t,n,s){e.EventDispatcher.call(this),this.uniqueID=i++,this.id=this.uniqueID,this.width=t,this.height=n,s&&(this.originalMagFilter=s.magFilter,this.originalMinFilter=s.minFilter),this.loadEndStatus=e.AssetLoadingStatus.READY,this.reset(s)};return n.prototype.clone=function(){var e=new n(this.width,this.height);return e.wrapS=this.wrapS,e.wrapT=this.wrapT,e.magFilter=this.magFilter,e.minFilter=this.minFilter,e.anisotropy=this.anisotropy,e.offset.copy(this.offset),e.repeat.copy(this.repeat),e.format=this.format,e.type=this.type,e.depthBuffer=this.depthBuffer,e.stencilBuffer=this.stencilBuffer,e.generateMipmaps=this.generateMipmaps,e.mapping=this.mapping,e.shareDepthFrom=this.shareDepthFrom,this.originalMagFilter&&(e.originalMagFilter=this.originalMagFilter),this.originalMinFilter&&(e.originalMinFilter=this.originalMinFilter),e},n.prototype.reset=function(i){i=i||{},this.wrapS=void 0!==i.wrapS?i.wrapS:t.ClampToEdgeWrapping,this.wrapT=void 0!==i.wrapT?i.wrapT:t.ClampToEdgeWrapping,this.magFilter=void 0!==i.magFilter?i.magFilter:t.LinearFilter,this.minFilter=void 0!==i.minFilter?i.minFilter:t.LinearMipMapLinearFilter,this.anisotropy=void 0!==i.anisotropy?i.anisotropy:1,this.offset=new e.Vector2(0,0),this.repeat=new e.Vector2(1,1),this.format=void 0!==i.format?i.format:t.RGBAFormat,this.type=void 0!==i.type?i.type:t.UnsignedByteType,this.depthBuffer=void 0===i.depthBuffer||i.depthBuffer,this.stencilBuffer=void 0===i.stencilBuffer||i.stencilBuffer,this.generateMipmaps=!0,this.mapping=new t.UVMapping,this.shareDepthFrom=null,this.hdr=!1,this.rgbHdr=!1,this.hasDiffuse=!1,this.sRGB=!1,this.useCount=0},n.prototype.dispose=function(){this.dispatchEvent({type:"dispose"})},n}),define("DS/GPUFormats/CompressedTexture",["DS/Mesh/ThreeJS_Base","DS/GPUFormats/Texture"],function(e,t){"use strict";var i=function(e,i,n,s,r,o,a,p,m,h,l){t.call(this,{width:i,height:n,data:null},o,a,p,m,h,s,r,l),this.mipmaps=e||[]};return(i.prototype=Object.create(t.prototype)).clone=function(){var e=new i;return t.prototype.clone.call(this,e),e},i.prototype._sendTextureToGPU=function(e,t,i,n,s,r,o,a){r.data?i?e.compressedTexImage2D(t,n,o,r.width,r.height,0,r.data):e.texImage2D(t,n,s,r.width,r.height,0,o,a,r.data):console.error("Error: wrong CompressedTexture usage, no data found")},i}),define("DS/GPUFormats/WebGLRenderTargetCube",["DS/GPUFormats/WebGLRenderTarget"],function(e){"use strict";var t=function(t,i,n){e.call(this,t,i,n),this.activeCubeFace=0};return t.prototype=Object.create(e.prototype),t}),define("DS/GPUFormats/DataTexture",["DS/Mesh/ThreeJS_Base","DS/GPUFormats/Texture"],function(e,t){"use strict";var i=function(e,i,n,s,r,o,a,p,m,h,l){t.call(this,{data:e,width:i,height:n},o,a,p,m,h,s,r,l)};return(i.prototype=Object.create(t.prototype)).clone=function(){var e=new i;return t.prototype.clone.call(this,e),e},i.prototype._sendTextureToGPU=function(e,t,i,n,s,r,o,a){r.data?i?console.error("Error: DataTexture can't be a compressed format"):e.texImage2D(t,n,s,r.width,r.height,0,o,a,r.data):console.error("Error: wrong DataTexture usage, no data found")},i}),define("DS/GPUFormats/NonPowerOfTwoTexture",["DS/Mesh/ThreeJS_Base","DS/GPUFormats/CompressedTexture"],function(e,t){"use strict";var i=function(i,n,s,r,o,a,p,m,h,l,u,d){t.call(this,{width:s,height:r,data:null},p,m,h,l,u,o,a,d),this.image=i.image,this.mapping=i.mapping,this.wrapS=e.ClampToEdgeWrapping,this.wrapT=e.ClampToEdgeWrapping,this.magFilter=e.LinearFilter,this.minFilter=e.LinearFilter,this.anisotropy=i.anisotropy,this.format=i.format,this.type=i.type,this.needsUpdate=!0,this.generateMipmaps=!1,n.length&&(this.image=n[0]),this.nPot=!0};return(i.prototype=Object.create(t.prototype)).clone=function(){var e=new t;return Texture.prototype.clone.call(this,e),e},i}),define("DS/GPUFormats/GPUFormats",["DS/GPUFormats/FormatEnums","DS/GPUFormats/Texture","DS/GPUFormats/CompressedTexture","DS/GPUFormats/DataTexture","DS/GPUFormats/TextureLODPool","DS/GPUFormats/WebGLRenderTarget","DS/GPUFormats/WebGLRenderTargetCube","DS/GPUFormats/NonPowerOfTwoTexture","DS/GPUFormats/VertexAttribute"],function(e,t,i,n,s,r,o,a,p){"use strict";var m={VertexAttribute:p,Texture:t,AssetLoadingStatus:{INIT:null,PAUSED:"pause",LOADING:"loading",ERROR:"error",READY:"complete"},CompressedTexture:i,DataTexture:n,TextureLODPool:s,NonPowerOfTwoTexture:a,WebGLRenderTargetProperties:function(e,t,i){this.width=e,this.height=t,this.options=i},WebGLRenderTarget:r,WebGLRenderTargetCube:o};return Object.assign(m,e),m});