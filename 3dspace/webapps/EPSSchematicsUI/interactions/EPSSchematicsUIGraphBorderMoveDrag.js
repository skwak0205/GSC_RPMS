/// <amd-module name='DS/EPSSchematicsUI/interactions/EPSSchematicsUIGraphBorderMoveDrag'/>
define("DS/EPSSchematicsUI/interactions/EPSSchematicsUIGraphBorderMoveDrag", ["require", "exports", "DS/EPSSchematicsUI/tools/EPSSchematicsUIMath"], function (require, exports, UIMath) {
    "use strict";
    /* eslint-enable no-unused-vars */
    /**
     * This class defines a graph border move drag interaction.
     * @class UIGraphBorderMoveDrag
     * @alias module:DS/EPSSchematicsUI/interactions/EPSSchematicsUIGraphBorderMoveDrag
     * @private
     **/
    var UIGraphBorderMoveDrag = /** @class */ (function () {
        /**
         * @constructor
         * @param {Element} border - The border of the graph.
         * @param {EGraphCore.Group} group - The group representing the graph.
         */
        function UIGraphBorderMoveDrag(border, group) {
            var _this = this;
            this.border = undefined;
            this.group = undefined;
            this.graph = undefined;
            this.initialLeft = undefined;
            this.initialTop = undefined;
            this.initialWidth = undefined;
            this.initialHeight = undefined;
            this.initialPortOffsets = [];
            this.ymin = undefined;
            this.ymax = undefined;
            this.graphBounds = undefined;
            this.cpBounds = undefined;
            this.clBounds = undefined;
            this.border = border;
            this.group = group;
            this.graph = this.group.data.graph;
            this.initialLeft = this.group.geometry.left;
            this.initialTop = this.group.geometry.top;
            this.initialWidth = this.group.geometry.width;
            this.initialHeight = this.group.geometry.height;
            this.graph.getControlPorts().forEach(function (controlPort) { return _this.initialPortOffsets.push(controlPort.getOffset()); });
            // Merge the blocks and control ports boundaries
            this.graphBounds = this.group.childrenBounds;
            this.cpBounds = this.graph.getControlPortBounds();
            this.clBounds = this.graph.getControlLinkBounds(true);
            this.ymin = UIMath.getMin(this.graphBounds.ymin, this.cpBounds.ymin);
            this.ymax = UIMath.getMax(this.graphBounds.ymax, this.cpBounds.ymax);
            this.ymin = UIMath.getMin(this.clBounds.ymin, this.ymin);
            this.ymax = UIMath.getMax(this.clBounds.ymax, this.ymax);
        }
        /**
         * The graph border mouse move callback.
         * @override
         * @protected
         * @param {EGraphIact.IMoveData} data - The move data.
         */
        UIGraphBorderMoveDrag.prototype.onmousemove = function (data) {
            var _this = this;
            var positionX = data.graphPos[0];
            var positionY = data.graphPos[1];
            positionX = this.graph.getEditor().getOptions().gridSnapping ? UIMath.snapValue(positionX) : positionX;
            positionY = this.graph.getEditor().getOptions().gridSnapping ? UIMath.snapValue(positionY) : positionY;
            var computeTop = false, computeBottom = false, computeLeft = false, computeRight = false;
            var graphView = this.graph.getView();
            if (this.border === graphView.getBorderTop()) {
                computeTop = true;
            }
            else if (this.border === graphView.getBorderBottom()) {
                computeBottom = true;
            }
            else if (this.border === graphView.getBorderLeft()) {
                computeLeft = true;
            }
            else if (this.border === graphView.getBorderRight()) {
                computeRight = true;
            }
            else if (this.border === graphView.getCornerNW()) {
                computeTop = true;
                computeLeft = true;
            }
            else if (this.border === graphView.getCornerNE()) {
                computeTop = true;
                computeRight = true;
            }
            else if (this.border === graphView.getCornerSW()) {
                computeBottom = true;
                computeLeft = true;
            }
            else if (this.border === graphView.getCornerSE()) {
                computeBottom = true;
                computeRight = true;
            }
            if (computeTop) {
                var topLimit = void 0, top_1, height = void 0, offsetDiff_1;
                if (isNaN(this.ymin)) {
                    topLimit = (this.initialTop + this.initialHeight) - (this.graph.getPaddingTop() + this.graph.getPaddingBottom());
                }
                else {
                    topLimit = this.ymin - this.graph.getPaddingTop();
                }
                if (positionY < topLimit) {
                    top_1 = positionY;
                    height = this.initialHeight + this.initialTop - positionY;
                    offsetDiff_1 = this.initialTop - positionY;
                }
                else {
                    top_1 = topLimit;
                    height = this.initialHeight + this.initialTop - topLimit;
                    offsetDiff_1 = this.initialTop - topLimit;
                }
                this.graph.getViewer().getDisplay().updateLock();
                try {
                    this.group.multiset(['geometry', 'top'], top_1, ['geometry', 'height'], height);
                    this.graph.getControlPorts().forEach(function (controlPort, index) { return controlPort.setOffset(UIMath.snapValue(_this.initialPortOffsets[index] + offsetDiff_1)); });
                }
                finally {
                    this.graph.getViewer().getDisplay().updateUnlock();
                }
            }
            if (computeBottom) {
                var bottomLimit = void 0;
                if (isNaN(this.ymax)) {
                    bottomLimit = this.initialTop + this.graph.getPaddingTop() + this.graph.getPaddingBottom();
                }
                else {
                    bottomLimit = this.ymax + this.graph.getPaddingBottom();
                }
                if (positionY > bottomLimit) {
                    this.group.multiset(['geometry', 'top'], this.initialTop, ['geometry', 'height'], this.initialHeight + positionY - this.initialTop - this.initialHeight);
                }
                else {
                    this.group.multiset(['geometry', 'top'], this.initialTop, ['geometry', 'height'], bottomLimit - this.initialTop);
                }
            }
            var minWidth = this.graph.getMinimumGraphWidthFromDrawers();
            this.clBounds = this.graph.getControlLinkBounds(true); // Need to be called again cause of automatic links that are redrawn!
            if (computeLeft) {
                var leftLimit = void 0;
                var xmin = UIMath.getMin(this.graphBounds.xmin, this.clBounds.xmin);
                if (isNaN(xmin)) {
                    leftLimit = (this.initialLeft + this.initialWidth) - (this.graph.getPaddingLeft() + this.graph.getPaddingRight());
                }
                else {
                    leftLimit = xmin - this.graph.getPaddingLeft();
                }
                if ((this.initialLeft + this.initialWidth - leftLimit) < minWidth) {
                    leftLimit = this.initialLeft + this.initialWidth - minWidth;
                }
                if (positionX < leftLimit) {
                    this.group.multiset(['geometry', 'left'], positionX, ['geometry', 'width'], this.initialWidth + this.initialLeft - positionX);
                }
                else {
                    this.group.multiset(['geometry', 'left'], leftLimit, ['geometry', 'width'], this.initialWidth + this.initialLeft - leftLimit);
                }
            }
            if (computeRight) {
                var rightLimit = void 0;
                var xmax = UIMath.getMax(this.graphBounds.xmax, this.clBounds.xmax);
                if (isNaN(xmax)) {
                    rightLimit = this.initialLeft + this.graph.getPaddingLeft() + this.graph.getPaddingRight();
                }
                else {
                    rightLimit = xmax + this.graph.getPaddingRight();
                }
                if ((rightLimit - this.initialLeft) < minWidth) {
                    rightLimit = this.initialLeft + minWidth;
                }
                if (positionX > rightLimit) {
                    this.group.multiset(['geometry', 'left'], this.initialLeft, ['geometry', 'width'], this.initialWidth + positionX - this.initialLeft - this.initialWidth);
                }
                else {
                    this.group.multiset(['geometry', 'left'], this.initialLeft, ['geometry', 'width'], rightLimit - this.initialLeft);
                }
            }
            this.graph.onUIChange();
        };
        /**
         * The graph border move end callback.
         * @override
         * @protected
         * @param {boolean} cancel - True when the drag is cancel else false.
         */
        // eslint-disable-next-line no-unused-vars
        UIGraphBorderMoveDrag.prototype.onend = function (cancel) {
            var update = this.graph.getTop() !== this.initialTop;
            update = update || this.graph.getLeft() !== this.initialLeft;
            update = update || this.graph.getHeight() !== this.initialHeight;
            update = update || this.graph.getWidth() !== this.initialWidth;
            if (update) {
                var historyController = this.graph.getViewer().getEditor().getHistoryController();
                historyController.registerMoveAction([this.graph]);
            }
        };
        return UIGraphBorderMoveDrag;
    }());
    return UIGraphBorderMoveDrag;
});
