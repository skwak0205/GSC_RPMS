"undefined"==typeof define&&(define=function(r,e,a){ValueTypeAPI=a(BinaryHelpers,ValueType,ValueTypePrivateAPI),define=void 0}),define("DS/CSICommandBinder/CSIValueTypeAPI",["DS/ExperienceKernel/EKBinaryHelpers","DS/CSICommandBinder/CSIValueType","DS/CSICommandBinder/CSIValueTypePrivateAPI"],function(r,e,a){"use strict";var t={};function n(r){var t=r.readUint64();if(void 0!==t&&r.readUint32()===e.CSIBufferGuid){var n=t-4;if(void 0!==n&&n>=0)return r.readArrayBuffer(n)}return a.valueTypeError}return t.readValue=function(t){var i,o,y=new r.BinaryReader(t),d=y.readUint8();if(d<0||d>e.endOfTypes)return a.valueTypeError;switch(d){case e.booleanType:return y.readBool();case e.int8Type:return y.readInt8();case e.uint8Type:return y.readUint8();case e.int16Type:return y.readInt16();case e.uint16Type:return y.readUint16();case e.int32Type:return y.readInt32();case e.uint32Type:return y.readUint32();case e.int64Type:return y.readInt64();case e.uint64Type:return y.readUint64();case e.floatType:return y.readFloat();case e.doubleType:return y.readDouble();case e.stringType:return y.readString();case e.stringsType:return o=y.readUint64(),y.readStrings(o);case e.booleanArrayType:if(void 0===(o=y.readUint64()))return a.valueTypeError;var u=Array.prototype.slice.call(y.readBoolArray(o));for(i=0;i<u.length;++i)u[i]=!!u[i];return u;case e.int8ArrayType:return void 0===(o=y.readUint64())?a.valueTypeError:Array.prototype.slice.call(y.readInt8Array(o));case e.uint8ArrayType:return void 0===(o=y.readUint64())?a.valueTypeError:Array.prototype.slice.call(y.readUint8Array(o));case e.int16ArrayType:return void 0===(o=y.readUint64())?a.valueTypeError:Array.prototype.slice.call(y.readInt16Array(o));case e.uint16ArrayType:return void 0===(o=y.readUint64())?a.valueTypeError:Array.prototype.slice.call(y.readUint16Array(o));case e.int32ArrayType:return void 0===(o=y.readUint64())?a.valueTypeError:Array.prototype.slice.call(y.readInt32Array(o));case e.uint32ArrayType:return void 0===(o=y.readUint64())?a.valueTypeError:Array.prototype.slice.call(y.readUint32Array(o));case e.int64ArrayType:case e.uint64ArrayType:return a.valueTypeError;case e.floatArrayType:return void 0===(o=y.readUint64())?a.valueTypeError:Array.prototype.slice.call(y.readFloatArray(o));case e.doubleArrayType:return void 0===(o=y.readUint64())?a.valueTypeError:Array.prototype.slice.call(y.readDoubleArray(o));case e.bufferArrayType:y.readUint64();var f=[];for(i=0;y.tell()<y.getSize();){y.readUint8();var p=n(y);if(p===a.valueTypeError)return a.valueTypeError;f[i++]=p}return f;case e.binaryType:return n(y)}},e.stringifyBufferFromBinary=function(r){if(void 0===r)return r;for(var e="",a=new Uint8Array(r),t=a.byteLength,n=0;n<t;n++)e+=String.fromCharCode(a[n]);for(var i,o,y,d,u="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",f=String(e),p="",c=0,s=f.length%3;c<f.length;){if((o=f.charCodeAt(c++))>255||(y=f.charCodeAt(c++))>255||(d=f.charCodeAt(c++))>255)throw new TypeError('Failed to execute "btoa": The string to be encoded contains characters outside of the Latin1 range.');p+=u.charAt((i=o<<16|y<<8|d)>>18&63)+u.charAt(i>>12&63)+u.charAt(i>>6&63)+u.charAt(63&i)}return s?p.slice(0,s-3)+"===".substring(s):p},t.readValueTypeAsString=function(t){var n=new r.BinaryReader(t),i=n.readUint8();if(i>=0){if(i<e.binaryType||i===e.bufferArrayType)return a.valueTypeToString(i);if(i===e.binaryType){n.readUint64();var o=n.readUint32();if(void 0!==o){if(o===e.CSIBufferGuid)return a.valueTypeToString(e.binaryType);if(o===e.CSIParametersGuid)return a.readObjectTypeAsString(n);if(o===e.CSIParametersArrayGuid)return a.readObjectArrayTypeAsString(n)}}}return a.valueTypeError},t.readParametersArrayBinaries=function(a){var t=new r.BinaryReader(a);if(t.readUint8()===e.binaryType&&(void 0!==t.readUint64()&&t.readUint32()===e.CSIParametersArrayGuid)){var n=t.readUint64();if(void 0!==n){if(0===n)return t.readString();for(var i=[],o=0;o<n;++o){var y=t.readUint64();if(void 0===y)return;var d=t.readArrayBuffer(y);if(void 0===d)return;i.push(d)}return i}}},t.isObject=function(a){var t=new r.BinaryReader(a);if(t.readUint8()===e.binaryType){var n=t.readUint64(),i=t.readUint32();if(void 0!==n&&void 0!==i)return i===e.CSIParametersGuid}},t.isObjectArray=function(a){var t=new r.BinaryReader(a);if(t.readUint8()===e.binaryType){var n=t.readUint64(),i=t.readUint32();if(void 0!==n&&void 0!==i){if(i===e.CSIParametersArrayGuid)if(void 0!==t.readUint64())return!0;return!1}}},t.getObjectArraySize=function(a){var t=new r.BinaryReader(a);if(t.readUint8()===e.binaryType){var n=t.readUint64(),i=t.readUint32();if(void 0!==n&&void 0!==i&&i===e.CSIParametersArrayGuid){var o=t.readUint64();if(void 0!==o)return o}}},t.importParametersFromTypedObject=function(r,e,a){if(!r)return!1;for(var t in e)if(e.hasOwnProperty(t)){var n=e[t].type,i=e[t].value;if(!n)return!1;if(void 0===i)return!1;if(!this.writePropertyFromTypedValue(r,t,n,i,a))return!1}return!0},t.writePropertyFromTypedValue=function(a,t,n,i,o){var y=function(r){if(!function(r){return r.startsWith("[")&&r.endsWith("]")}(r)){var e="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",a=String(r).replace(/[\t\n\f\r ]+/g,"");if(!/^(?:[A-Za-z\d+\/]{4})*?(?:[A-Za-z\d+\/]{2}(?:==)?|[A-Za-z\d+\/]{3}=?)?$/.test(a))throw new TypeError('Failed to execute "atob" on "Window": The string to be decoded is not correctly encoded.');a+="==".slice(2-(3&a.length));for(var t,n,i,o="",y=0;y<a.length;)t=e.indexOf(a.charAt(y++))<<18|e.indexOf(a.charAt(y++))<<12|(n=e.indexOf(a.charAt(y++)))<<6|(i=e.indexOf(a.charAt(y++))),o+=64===n?String.fromCharCode(t>>16&255):64===i?String.fromCharCode(t>>16&255,t>>8&255):String.fromCharCode(t>>16&255,t>>8&255,255&t);return Uint8Array.from(o,function(r){return r.charCodeAt(0)})}if("[]"===r)return new ArrayBuffer;for(var d=r.replace("[","").replace("]","").split(" "),u=new Uint8Array(d.length),f=0;f<d.length;++f)u[f]=parseInt(d[f],16);return u},d=e.typeNames.indexOf(n),u=n.indexOf("[]"),f=u>=0;if(f&&(n=n.substring(0,u)),-1===d){if(!f){var p=new a.constructor;return!!this.importParametersFromTypedObject(p,i,o)&&a.writeParameters(t,n,p)}if(f){for(var c=[],s=0;s<i.length;++s){var l=new a.constructor,v=o<3?i[s]:i[s].value;if(!this.importParametersFromTypedObject(l,v,o))return!1;o>=3&&l.declareAsObject(i[s].type),c.push(l)}return a.writeParametersArray(t,n,c)}}else if("buffer"===n){if(!f)return a.writeBuffer(t,y(i));for(var A,T,h,U=i.replace("[","").substring(0,i.length-2).split(","),g=[],b=0;b<U.length;b++)h=(A=y(U[b]))instanceof ArrayBuffer?A.byteLength:A.length,(T=new r.BinaryWriter(h)).writeArrayBuffer(A),g[b]=T.createArrayBuffer();return a.writeBufferArray(t,g)}var S="write"+n.charAt(0).toUpperCase()+n.slice(1);return u>=0&&(S+="Array"),a[S](t,i)},t});