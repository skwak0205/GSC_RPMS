define("DS/SpecGridView/collection/LargeTilesCollectionView",["UWA/Core","DS/CollectionView/ResponsiveLargeTilesCollectionView"],function(e,l){"use strict";return l.extend({init:function(e){this._parent(e)},_postBuildView:function(){this._parent(),this.cellStyleOverload="wux-cell-responsivelargetile xsr-cell-responsivelargetile",this.elements.container.addClassName("xsr-layout-largetilecollectionView")},_onPostRequestCellCB:function(e){var l=e.nodeModel;if(l){var i=!l._canBeGrouped();!i&&e.cellView.elements.container.hasClassName("xsr-groupnode-responsivelargetile")?e.cellView.elements.container.removeClassName("xsr-groupnode-responsivelargetile"):i&&!e.cellView.elements.container.hasClassName("xsr-groupnode-responsivelargetile")&&e.cellView.elements.container.addClassName("xsr-groupnode-responsivelargetile")}}})});