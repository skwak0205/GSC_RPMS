/*!  Copyright 2015 Dassault Systemes. All rights reserved. */
define("DS/SPAContentOptimizer/COMiscLayerFilter",["DS/CSICommandBinder/CSICommandBinder","DS/SPAContentOptimizer/COMiscLayer"],function(e,r){"use strict";var i=function(){this.FilterName="",this.Type=0,this.IsCurrent=!1,this.MiscLayers=[]};return e.declareType({type:"COMiscLayerFilter",serialize:function(e,r){return e.writeString("FilterName",r.FilterName),e.writeUint8("Type",r.Type),e.writeBool("IsCurrent",r.IsCurrent),e.writeObjectArray("MiscLayers","COMiscLayer",r.MiscLayers),!0},unserialize:function(e){var r=new i;return r.FilterName=e.readString("FilterName"),r.Type=e.readUint8("Type"),r.IsCurrent=e.readString("IsCurrent"),r.MiscLayers=e.readObjectArray("MiscLayers","COMiscLayer"),r}}),i});