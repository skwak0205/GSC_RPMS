/*!  Copyright 2015 Dassault Systemes. All rights reserved. */
define("DS/SPAContentOptimizer/COptMiscSymmetry",["DS/CSICommandBinder/CSICommandBinder","DS/SPAContentOptimizer/COptEntity"],function(t,e){"use strict";var n=function(){this.mode=0,this.rotation=[],this.translation=[],this.linkedElement};return t.declareType({type:"COptMiscSymmetry",serialize:function(t,e){return t.writeInt32("mode",e.mode),t.writeDoubleArray("rotation",e.rotation),t.writeDoubleArray("translation",e.translation),t.writeObject("linkedElement","COptEntity",e.linkedElement),!0},unserialize:function(t){var e=new n;return e.mode=t.readInt32("mode"),e.rotation=t.readDoubleArray("rotation"),e.translation=t.readDoubleArray("translation"),e.linkedElement=t.readObject("linkedElement","COptEntity"),e}}),n});