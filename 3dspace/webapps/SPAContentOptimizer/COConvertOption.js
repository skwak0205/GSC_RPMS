/*!  Copyright 2015 Dassault Systemes. All rights reserved. */
define("DS/SPAContentOptimizer/COConvertOption",["DS/CSICommandBinder/CSICommandBinder","DS/SPAContentOptimizer/COExportIgesOptions","DS/SPAContentOptimizer/COExportStepOptions"],function(t,e,o){"use strict";var i=function(){this._DRep=!1,this._thumb3D=!1,this._thumb=0,this._pmi=0,this._option3xf="",this.ExportIgesOptions=null,this.ExportStepOptions=null,this.BRepTessellationOptions=null};return i.prototype.GetDRep=function(){return this._DRep},i.prototype.SetDRep=function(t){this._DRep=t},i.prototype.GetThumbnail=function(){return this._thumb},i.prototype.SetThumbnail=function(t){this._thumb=t},i.prototype.GetPMI=function(){return this._pmi},i.prototype.SetPMI=function(t){this._pmi=t},i.prototype.GetThumbnail3D=function(){return this._thumb3D},i.prototype.SetThumbnail3D=function(t){this._thumb3D=t},i.prototype.GetOption3xf=function(){return this._option3xf},i.prototype.SetOption3xf=function(t){this._option3xf=t},t.declareType({type:"COConvertOption",serialize:function(t,e){return t.writeBool("DRep",e._DRep),t.writeBool("Thumb3D",e._thumb3D),t.writeUint8("Thumb",e._thumb),t.writeUint8("PMI",e._pmi),t.writeString("Option3xf",e._option3xf),e.ExportIgesOptions&&t.writeObject("ExportIgesOptions","COExportIgesOptions",e.ExportIgesOptions),e.ExportStepOptions&&t.writeObject("ExportStepOptions","COExportStepOptions",e.ExportStepOptions),e.BRepTessellationOptions&&t.writeObject("BRepTessellationOptions","COBRepTessellationOptions",e.BRepTessellationOptions),!0},unserialize:function(t){var e=new i;return e._DRep=t.readBool("DRep"),e._thumb3D=t.readBool("Thumb3D"),e._thumb=t.readUint8("Thumb"),e._pmi=t.readUint8("PMI"),e._option3xf=t.readString("Option3xf"),e.ExportIgesOptions=t.readObject("ExportIgesOptions","COExportIgesOptions"),e.ExportStepOptions=t.readObject("ExportStepOptions","COExportStepOptions"),e.BRepTessellationOptions=t.readObject("BRepTessellationOptions","COBRepTessellationOptions"),e}}),i});