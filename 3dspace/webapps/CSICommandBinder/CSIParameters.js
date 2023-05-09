"undefined"==typeof define&&(define=function(r,e,t){Parameters=t(EK,BinaryHelpers,ParametersIterator,ValueType,ValueTypeAPI,ErrorReport),define=void 0}),define("DS/CSICommandBinder/CSIParameters",["DS/ExperienceKernel/ExperienceKernel","DS/ExperienceKernel/EKBinaryHelpers","DS/CSICommandBinder/CSIParametersIterator","DS/CSICommandBinder/CSIValueType","DS/CSICommandBinder/CSIValueTypeAPI","DS/CSICommandBinder/CSIErrorReport"],function(r,e,t,i,n,a){"use strict";var o=function(r){this.propertyMap_={},this.inputStreamSize_=0,this.typeName_="Parameters",r instanceof o?p.copyParameters(r,this):r instanceof ArrayBuffer&&this.fromBinary(r)};o.prototype.serializeMap_={},o.prototype.unserializeMap_={},o.prototype.typeDefinitions_={},//!< map of <typename, CSIDeclarativeTypeDefinition>, for instance Parameters.prototype.typeDefinition_s['CATMathPoint'] === {...}
o.prototype.checkForIncomplete_=!1,o.prototype.getObjectType=function(){return this.typeName_},o.prototype.exists=function(r){if(void 0!==this.propertyMap_[r])return n.readValueTypeAsString(this.propertyMap_[r])},o.prototype.existsWithType=function(r,e){var t=this.propertyMap_[r];return!!t&&n.readValueTypeAsString(t)===e};var p={copyParameters:function(r,e){if(!(r instanceof o&&e instanceof o))return!1;for(var t in e.typeName_=r.typeName_,e.inputStreamSize_=r.inputStreamSize_,r.propertyMap_)r.propertyMap_.hasOwnProperty(t)&&(e.propertyMap_[t]=r.propertyMap_[t].slice(0));return!0},getArrayNumberPropertyBinarySize:function(r,e){return 8+e*r.readUint64()}};o.prototype.declareType=function(r){return void 0===o.prototype.serializeMap_[r.type]&&(o.prototype.serializeMap_[r.type]=r.serialize,o.prototype.unserializeMap_[r.type]=r.unserialize,!0)},o.prototype.toBinary=function(){return d(this,this.typeName_)},o.prototype.getStreamSize=function(){return y(this.propertyMap_,this.typeName_)};var y=function(r,e){var t=8+e.length+4;for(var i in r)r.hasOwnProperty(i)&&(t+=4+i.length+r[i].byteLength);return t};function s(r,t,i,n){var a=r.propertyMap_[t];if(a instanceof ArrayBuffer){var o=new e.BinaryReader(a);if(o.readUint8()===i)return o[n]()}}function u(r,t,i,n){var a=r.propertyMap_[t];if(a instanceof ArrayBuffer){var o=new e.BinaryReader(a);if(o.readUint8()===i){var p=o.readUint64();return o[n](p)}}}function f(r){if(r.readUint8()===i.binaryType){var e=r.readUint64();if(r.readUint32()===i.CSIBufferGuid)return r.readArrayBuffer(e-4)}}function c(r,t,i,n){var a=new e.BinaryWriter(1+n);return a.writeUint8(t),a[i](r),a.createArrayBuffer()}function l(r){var t=13+r.byteLength,n=new e.BinaryWriter(t);return n.writeUint8(i.binaryType),n.writeUint64(r.byteLength+4),n.writeUint32(i.CSIBufferGuid),n.writeArrayBuffer(r),n.createArrayBuffer()}function h(r,t,i,n){var a=new e.BinaryWriter(9+n);return a.writeUint8(t),a.writeUint64(r.length),a[i](r),a.createArrayBuffer()}o.prototype.getInputStreamSize=function(){return this.inputStreamSize_},o.prototype.fromBinary=function(r){this.propertyMap_={},this.inputStreamSize_=r.byteLength;var t=new e.BinaryReader(r),n=t.readUint32();if(n!==i.CSIParametersGuid&&(t.seek(0),t.readUint8()===i.binaryType)){var a=t.readUint64();void 0!==a&&0!==a&&(n=t.readUint32())}var o=t.readString();if(void 0===o||n!==i.CSIParametersGuid)return!1;for(this.typeName_=o,t.readUint16(),t.readUint16();t.tell()<r.byteLength;)if(!this.readPropertyFromBinary(t))return!1;return!0},o.prototype.readPropertyFromBinary=function(r){var e=r.readString();if(void 0!==e){var t=r.tell(),n=r.readUint8();if(void 0!==n){var a,o=1;switch(n){case i.booleanType:case i.int8Type:case i.uint8Type:o+=1;break;case i.int16Type:case i.uint16Type:o+=2;break;case i.int32Type:case i.uint32Type:o+=4;break;case i.int64Type:case i.uint64Type:o+=8;break;case i.floatType:o+=4;break;case i.doubleType:o+=8;break;case i.stringType:void 0!==(a=r.readUint32())&&(o+=4+a);break;case i.stringsType:var y=r.readUint64();if(void 0!==y){o+=8;for(var s=0;s<y;++s)void 0!==(a=r.readUint32())&&(o+=4+a,r.seek(r.tell()+a))}break;case i.booleanArrayType:case i.int8ArrayType:case i.uint8ArrayType:o+=p.getArrayNumberPropertyBinarySize(r,1);break;case i.int16ArrayType:case i.uint16ArrayType:o+=p.getArrayNumberPropertyBinarySize(r,2);break;case i.int32ArrayType:case i.uint32ArrayType:o+=p.getArrayNumberPropertyBinarySize(r,4);break;case i.int64ArrayType:case i.uint64ArrayType:break;case i.floatArrayType:o+=p.getArrayNumberPropertyBinarySize(r,4);break;case i.doubleArrayType:o+=p.getArrayNumberPropertyBinarySize(r,8);break;case i.bufferArrayType:o+=8+r.readUint64();break;case i.binaryType:var u=r.readUint64();if(void 0!==u&&0!==u){var f=r.readUint32();f!==i.CSIBufferGuid&&f!==i.CSIParametersGuid&&f!==i.CSIParametersArrayGuid&&f!==i.CSIParametersReferenceGuid||(o+=8+u)}}if(o>1){r.seek(t);var c=r.readArrayBuffer(o);if(void 0!==c)return this.propertyMap_[e]=c,!0}}}return!1},o.prototype.readString=function(r){var t=this.propertyMap_[r];if(t instanceof ArrayBuffer){var n=new e.BinaryReader(t);if(n.readUint8()===i.stringType)return n.readString()}},o.prototype.readBool=function(r){return s(this,r,i.booleanType,"readBool")},o.prototype.readInt8=function(r){return s(this,r,i.int8Type,"readInt8")},o.prototype.readUint8=function(r){return s(this,r,i.uint8Type,"readUint8")},o.prototype.readInt16=function(r){return s(this,r,i.int16Type,"readInt16")},o.prototype.readUint16=function(r){return s(this,r,i.uint16Type,"readUint16")},o.prototype.readInt32=function(r){return s(this,r,i.int32Type,"readInt32")},o.prototype.readUint32=function(r){return s(this,r,i.uint32Type,"readUint32")},o.prototype.readInt64=function(r){return s(this,r,i.int64Type,"readInt64")},o.prototype.readUint64=function(r){return s(this,r,i.uint64Type,"readUint64")},o.prototype.readFloat=function(r){return s(this,r,i.floatType,"readFloat")},o.prototype.readDouble=function(r){return s(this,r,i.doubleType,"readDouble")},o.prototype.readStringArray=function(r){return u(this,r,i.stringsType,"readStrings")},o.prototype.readBoolArray=function(r){var e=u(this,r,i.booleanArrayType,"readBoolArray");if(e instanceof Uint8Array){for(var t=new Array(e.length),n=0;n<e.byteLength;++n)t[n]=Boolean(e[n]);return t}},o.prototype.readInt8Array=function(r){return u(this,r,i.int8ArrayType,"readInt8Array")},o.prototype.readUint8Array=function(r){return u(this,r,i.uint8ArrayType,"readUint8Array")},o.prototype.readInt16Array=function(r){return u(this,r,i.int16ArrayType,"readInt16Array")},o.prototype.readUint16Array=function(r){return u(this,r,i.uint16ArrayType,"readUint16Array")},o.prototype.readInt32Array=function(r){return u(this,r,i.int32ArrayType,"readInt32Array")},o.prototype.readUint32Array=function(r){return u(this,r,i.uint32ArrayType,"readUint32Array")},o.prototype.readFloatArray=function(r){return u(this,r,i.floatArrayType,"readFloatArray")},o.prototype.readDoubleArray=function(r){return u(this,r,i.doubleArrayType,"readDoubleArray")},o.prototype.readBuffer=function(r){var t=this.propertyMap_[r];if(t instanceof ArrayBuffer)return f(new e.BinaryReader(t))},o.prototype.readBufferArray=function(r){var t=this.propertyMap_[r];if(void 0!==t){var n=new e.BinaryReader(t),a=[];if(n.readUint8()===i.bufferArrayType){n.readUint64();for(var o=0;n.tell()<n.getSize();)a[o++]=f(n);return a}}},o.prototype.readObject=function(r,e){if(void 0===o.prototype.unserializeMap_[e])return a.errorU("CSIParameters.readObject(), no unserialization function available for type "+e);var t=this.readParameters(r,e);return t?o.prototype.unserializeMap_[e](t):void 0},o.prototype.readParameters=function(r,e){var t=this.propertyMap_[r];if(t instanceof ArrayBuffer){var i=new o;if(i.fromBinary(t))return"Parameters"===e||i.typeName_===e?i:a.errorU("CSIParameters.readParameters(), property <"+r+"> is of type <"+i.typeName_+">, not <"+e+">")}},o.prototype.readParametersArray=function(r,e){var t=this.propertyMap_[r];if(t instanceof ArrayBuffer){var i=n.readParametersArrayBinaries(t);if("string"==typeof i)return"Parameters"===e||e===i?[]:a.errorU("CSIParameters.readParametersArray(), property <"+r+"> is an empty array of type <"+i+">, not <"+e+">");if(void 0!==i&&void 0!==i.length){for(var p=[],y=0;y<i.length;++y){var s=new o;if(s.fromBinary(i[y])){if("Parameters"!==e&&e!==s.typeName_)return a.errorU("CSIParameters.readParametersArray(), property <"+r+"> is an array of type <"+s.typeName_+">, not <"+e+">");p.push(s)}}return p}}},o.prototype.readObjectArray=function(r,e){if(void 0===o.prototype.unserializeMap_[e])return a.errorU("CSIParameters.readObjectArray(), unserialization function not available for type "+e);var t=this.readParametersArray(r,e);if(void 0!==t&&void 0!==t.length){for(var i=[],n=0;n<t.length;++n){var p=t[n],y=o.prototype.unserializeMap_[e](p);if(void 0===y)return a.errorU("CSIParameters.readObjectArray(), Unserialize function returns undefined for property <"+r+"> type <"+e+">");i.push(y)}return i}},o.prototype.writeString=function(r,e){return void 0===e&&a.deprecated("Call CSIParameters.writeString() with an undefined value. Name and value input parameters must be defined"),e+="",this.propertyMap_[r]=c(e,i.stringType,"writeString",4+4*e.length),!0},o.prototype.writeBool=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.booleanType,"writeBool",1),!0)},o.prototype.writeInt8=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.int8Type,"writeInt8",1),!0)},o.prototype.writeUint8=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.uint8Type,"writeUint8",1),!0)},o.prototype.writeInt16=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.int16Type,"writeInt16",2),!0)},o.prototype.writeUint16=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.uint16Type,"writeUint16",2),!0)},o.prototype.writeInt32=function(r,e){return void 0===e&&(a.deprecated("Call CSIParameters.writeInt32() with an undefined value. Name and value input parameters must be defined"),e=0),this.propertyMap_[r]=c(e,i.int32Type,"writeInt32",4),!0},o.prototype.writeUint32=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.uint32Type,"writeUint32",4),!0)},o.prototype.writeInt64=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.int64Type,"writeInt64",8),!0)},o.prototype.writeUint64=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.uint64Type,"writeUint64",8),!0)},o.prototype.writeFloat=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.floatType,"writeFloat",8),!0)},o.prototype.writeDouble=function(r,e){return void 0!==e&&(this.propertyMap_[r]=c(e,i.doubleType,"writeDouble",8),!0)},o.prototype.writeBuffer=function(r,e){return void 0!==e&&(this.propertyMap_[r]=l(e),!0)},o.prototype.writeBufferArray=function(r,t){if(void 0===t)return!1;for(var n,a=0,o=[],p=0;p<t.length;++p)a+=(n=l(t[p])).byteLength,o[p]=n;for(var y=new e.BinaryWriter(a),s=0;s<o.length;++s)y.writeArrayBuffer(o[s]);var u=9+a,f=new e.BinaryWriter(u);return f.writeUint8(i.bufferArrayType),f.writeUint64(a),f.writeArrayBuffer(y.createArrayBuffer()),this.propertyMap_[r]=f.createArrayBuffer(),!0},o.prototype.writeObject=function(r,e,t){if(null==t)return a.error("Unexpected call to CSIParameters.writeObject() with undefined value");if(void 0===o.prototype.serializeMap_[e])return o.prototype.checkForIncomplete_?this.pushIncompleteOperation([e,o.prototype.writeObject,this,Array.prototype.slice.call(arguments)]):a.error("Call to CSIParameters.writeObject() with type <"+e+"> but no serialization function exists");var i=new o;return!1!==o.prototype.serializeMap_[e](i,t)&&i.declareAsObject(e)?this.writeParameters(r,e,i):a.error("Call to CSIParameters.writeObject() but serialization function failed for type "+e+" with object "+JSON.stringify(t))},o.prototype.writeParameters=function(r,t,n){if(null==n)return a.error("Unexpected call to CSIParameters.writeParameters() with undefined value");if(!m(t,n))return!1;if(n.isIncomplete())return!!o.prototype.checkForIncomplete_&&(this.pushIncompleteParameters(n),this.pushIncompleteOperation([t,o.prototype.writeParameters,this,Array.prototype.slice.call(arguments)]));var p=d(n,"Parameters"===t?n.typeName_:t),y=new e.BinaryWriter;return y.writeUint8(i.binaryType),y.writeUint64(p.byteLength),y.writeArrayBuffer(p),this.propertyMap_[r]=y.createArrayBuffer(),!0},o.prototype.writeJSONInternal=function(r,t){if(null==t)return a.error("Unexpected call to CSIParameters.writeJSONInternal() with undefined value");var n=JSON.stringify(t),o=unescape(encodeURIComponent(n)),p=new e.BinaryWriter(13+o.length);return p.writeUint8(i.binaryType),p.writeUint64(4+o.length),p.writeUint32(i.CSIJsonGuid),p.writeStringInternal(o),this.propertyMap_[r]=p.createArrayBuffer(),!0},o.prototype.writeStringArray=function(r,e){if(!e)return!1;for(var t=0,n=0;n<e.length;++n)t+=4+4*e[n].length;return this.propertyMap_[r]=h(e,i.stringsType,"writeStrings",t),!0},o.prototype.writeBoolArray=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.booleanArrayType,"writeBoolArray",e.length),!0)},o.prototype.writeInt8Array=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.int8ArrayType,"writeInt8Array",e.length),!0)},o.prototype.writeUint8Array=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.uint8ArrayType,"writeUint8Array",e.length),!0)},o.prototype.writeInt16Array=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.int16ArrayType,"writeInt16Array",e.length),!0)},o.prototype.writeUint16Array=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.uint16ArrayType,"writeUint16Array",e.length),!0)},o.prototype.writeInt32Array=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.int32ArrayType,"writeInt32Array",e.length),!0)},o.prototype.writeUint32Array=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.uint32ArrayType,"writeUint32Array",e.length),!0)},o.prototype.writeFloatArray=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.floatArrayType,"writeFloatArray",e.length),!0)},o.prototype.writeDoubleArray=function(r,e){return!!e&&(this.propertyMap_[r]=h(e,i.doubleArrayType,"writeDoubleArray",e.length),!0)},o.prototype.writeParametersArray=function(r,t,n){if(!(n instanceof Array))return a.error("Unexpected call to CSIParameters.writeParametersArray() with undefined parameters array");for(var p=0;p<n.length;++p)if(!m(t,n[p]))return!1;for(var y,s=!1,u=0;u<n.length;++u)if((y=n[u]).isIncomplete()){if(!o.prototype.checkForIncomplete_)return!1;s=this.pushIncompleteParameters(y)}if(s)return this.pushIncompleteOperation([t,o.prototype.writeParametersArray,this,Array.prototype.slice.call(arguments)]);for(var f=0,c=[],l=0;l<n.length;++l){y=n[l];var h=d(y,"Parameters"===t?y.typeName_:t);c.push(h),f+=h.byteLength}var A,w=c.length;if(0===w){var v=new e.BinaryWriter;v.writeString(t),A=v.createArrayBuffer()}f+=12+(A?A.byteLength:0),f+=8*w;var _=new e.BinaryWriter(9+f);if(_.writeUint8(i.binaryType),_.writeUint64(f),_.writeUint32(i.CSIParametersArrayGuid),_.writeUint64(w),0===w)_.writeArrayBuffer(A);else for(var b=0;b<w;++b)_.writeUint64(c[b].byteLength),_.writeArrayBuffer(c[b]);return this.propertyMap_[r]=_.createArrayBuffer(),!0},o.prototype.writeObjectArray=function(r,e,t){if(null==t)return a.error("Unexpected call to CSIParameters.writeObjectArray() with undefined value");if(void 0===o.prototype.serializeMap_[e])return o.prototype.checkForIncomplete_?this.pushIncompleteOperation([e,o.prototype.writeObjectArray,this,Array.prototype.slice.call(arguments)]):a.error("CSIParameters.writeObjectArray(), no serialization function available for type "+e);for(var i=[],n=0;n<t.length;n++){var p=new o;if(!1===o.prototype.serializeMap_[e](p,t[n])||!p.declareAsObject(e))return a.error("Call to CSIParameters.writeObjectArray() but serialization function failed for type "+e+" with object "+JSON.stringify(t));i.push(p)}return this.writeParametersArray(r,e,i)},o.prototype.begin=function(){return new t(this,!1)},o.prototype.end=function(){return new t(this,!0)},o.prototype.toJSON=function(){return JSON.stringify(this.toTypedObject())},o.prototype.toTypedObject=function(){for(var r={},e=this.begin(),t=this.end();!e.equal(t);){var i=e.getName();r[i]={type:e.getType()},r[i].value=e.getTypedValue(),e.next()}return r},o.prototype.toJSObject=function(){for(var r={},e=this.begin(),t=this.end();!e.equal(t);){var i=e.getName(),n=e.getJSValue();if(e.isObject())r[i]=n;else if(e.isObjectArray()){r[i]=[];for(var a=n,o=0;o<a.length;o++)r[i].push(a[o])}else r[i]=n;e.next()}return r},o.prototype.fromObject=function(r,e){if(null==r)return a.error("Unexpected call to CSIParameters.fromObject() with undefined value");if(void 0===o.prototype.serializeMap_[e]){var t=o.prototype.typeDefinitions_[e];return void 0===t?!!o.prototype.checkForIncomplete_&&this.pushIncompleteOperation([e,o.prototype.fromObject,this,Array.prototype.slice.call(arguments)]):!!this.fromPropertyDefinitions(r,t.definition)&&(this.typeName_=e,!0)}return!(!1===o.prototype.serializeMap_[e](this,r)||!this.declareAsObject(e))||a.error("Call to CSIParameters.fromObject() but serialization function failed for type "+e+" with object "+JSON.stringify(r))},o.prototype.fromJSON=function(r,e){return this.fromObject(JSON.parse(r),e)},o.prototype.fromPropertyDefinitions=function(r,e){return e instanceof Array&&e.every(function(e){var t,i,n,p;if(e.file)return a.error("CSIParameters.fromPropertyDefinitions() file property type is not supported");if(e.basic)this[p="write"+(p=e.basic).charAt(0).toUpperCase()+p.slice(1)](e.label,r[e.label]);else if(e.basicArray)this[p="write"+(p=e.basicArray+"Array").charAt(0).toUpperCase()+p.slice(1)](e.label,r[e.label]);else if(e.type){if(!(i=new o).fromObject(r[e.label],e.type))return!1;if(!this.writeParameters(e.label,e.type,i))return!1}else if(e.typeArray){if(!(r[e.label]instanceof Array))return!1;for(n=[],t=0;t<r[e.label].length;++t){if(!(i=new o).fromObject(r[e.label][t],e.typeArray))return!1;n.push(i)}if(!this.writeParametersArray(e.label,e.typeArray,n))return!1}else if(e.parameters){if(!(i=new o).fromPropertyDefinitions(r[e.label],e.parameters))return!1;if(!this.writeParameters(e.label,"Parameters",i))return!1}else if(e.parametersArray){if(!(r[e.label]instanceof Array))return!1;for(n=[],t=0;t<r[e.label].length;++t){if(!(i=new o).fromPropertyDefinitions(r[e.label][t],e.parametersArray))return!1;n.push(i)}if(!this.writeParametersArray(e.label,"Parameters",n))return!1}return!0},this)},o.prototype.isEmpty=function(){return!this.isIncomplete()&&0===Object.keys(this.propertyMap_).length},o.prototype.removeProperty=function(r){return!!this.propertyMap_.hasOwnProperty(r)&&(delete this.propertyMap_[r],!0)},o.prototype.declareAsObject=function(r){return!!m(r,this)&&(this.typeName_=r,!0)},o.prototype.overrideWith=function(r){if(r.typeName_!==this.typeName_)return a.error("CSIParameters.overrideWith() cannot override Parameters with different typename, this.typename = "+this.typeName_+" other.typename = "+r.typeName_);var e=!1;for(var t in r.propertyMap_)r.propertyMap_.hasOwnProperty(t)&&(this.propertyMap_[t]=r.propertyMap_[t],e=!0);return e},o.prototype.isEqualTo=function(r){var e=new o;e.fromBinary(this.toBinary());var t=new o;return t.fromBinary(r.toBinary()),e.toJSON()===t.toJSON()};var d=function(r,t){var n=new e.BinaryWriter(r.getStreamSize(t));for(var a in n.writeUint32(i.CSIParametersGuid),n.writeString(t),n.writeUint16(16980),n.writeUint16(1),r.propertyMap_)r.propertyMap_.hasOwnProperty(a)&&(n.writeString(a),n.writeArrayBuffer(r.propertyMap_[a]));return n.createArrayBuffer()},m=function(r,e){return e instanceof o&&(void 0===r||"Parameters"===r||"string"==typeof r)};return o.prototype.exportToString=function(){if(this.isEmpty()&&"Parameters"===this.typeName_)return"{}";var r={content:"",type:this.typeName_,version:3};return r.content=this.toTypedObject(),JSON.stringify(r)},o.prototype.importFromString=function(r){if("{}"===r)return this.typeName_="Parameters",this.propertyMap_={},!0;var e=JSON.parse(r);return(1===e.version||2===e.version||3===e.version)&&(this.propertyMap_={},this.typeName_="Parameters",n.importParametersFromTypedObject(this,e.content,e.version)?(this.typeName_=e.type,!0):(this.propertyMap_={},!1))},o.prototype.isIncomplete=function(){return this.incompleteParameters_ instanceof Array&&this.incompleteParameters_.length>0||this.incompleteOperations_ instanceof Array&&this.incompleteOperations_.length>0},o.prototype.pushIncompleteParameters=function(r){return void 0===this.incompleteParameters_&&(this.incompleteParameters_=[]),this.incompleteParameters_.push(r),!0},o.prototype.pushIncompleteOperation=function(r){return void 0===this.incompleteOperations_&&(this.incompleteOperations_=[]),this.incompleteOperations_.push(r),!0},o.prototype.complete=function(r){var e;if(this.incompleteParameters_ instanceof Array){for(e=0;e<this.incompleteParameters_.length;++e){var t=this.incompleteParameters_[e];if(t instanceof o){if(!t.complete(r))return!1;this.incompleteParameters_[e]=void 0}}delete this.incompleteParameters_}if(this.incompleteOperations_ instanceof Array){for(e=0;e<this.incompleteOperations_.length;++e)if(this.incompleteOperations_[e]instanceof Array){if(void 0!==r&&r instanceof Array){var i=this.incompleteOperations_[e][0];"Parameters"!==i&&-1===r.indexOf(i)&&r.push(i)}var n=this.incompleteOperations_[e][1],a=this.incompleteOperations_[e][2],p=this.incompleteOperations_[e][3];if(!n.apply(a,p))return!1;this.incompleteOperations_[e]=void 0}delete this.incompleteOperations_}return!0},o.prototype.getIncompleteRequiredTypes=function(r){var e,t=!1;if(void 0===r&&(r=[],t=!0),this.incompleteParameters_ instanceof Array)for(e=0;e<this.incompleteParameters_.length;++e){this.incompleteParameters_[e]instanceof o&&this.incompleteParameters_[e].getIncompleteRequiredTypes(r)}if(this.incompleteOperations_ instanceof Array)for(e=0;e<this.incompleteOperations_.length;++e)if(this.incompleteOperations_[e]instanceof Array){var i=this.incompleteOperations_[e][0];"Parameters"!==i&&r.push(i)}if(t)return r},o.prototype.declareType({type:"Parameters",serialize:function(r,e){return p.copyParameters(e,r)},unserialize:function(r){var e=new o;return p.copyParameters(r,e),e}}),o});