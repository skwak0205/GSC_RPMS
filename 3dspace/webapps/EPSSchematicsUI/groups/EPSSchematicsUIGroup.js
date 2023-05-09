/// <amd-module name='DS/EPSSchematicsUI/groups/EPSSchematicsUIGroup'/>
define("DS/EPSSchematicsUI/groups/EPSSchematicsUIGroup", ["require", "exports", "DS/EPSSchematicsUI/typings/WebUX/EPSWUXEGraphCore", "DS/EPSSchematicsUI/nodes/views/EPSSchematicsUINodeView"], function (require, exports, EGraphCore, UINodeView) {
    "use strict";
    /* eslint-enable no-unused-vars */
    /**
     * This class defines a UI group.
     * @class UIGroup
     * @alias module:DS/EPSSchematicsUI/groups/EPSSchematicsUIGroup
     * @private
     */
    var UIGroup = /** @class */ (function () {
        /**
         * @constructor
         * @param {UIEGraphViewer} viewer - The viewer.
         */
        function UIGroup(viewer) {
            this.display = undefined;
            this.viewer = undefined;
            this.editor = undefined;
            this.viewer = viewer;
            this.editor = this.viewer.getEditor();
            this.display = new EGraphCore.Group();
            this.display.data = { graph: this }; // TODO: Check if we can be more generic and call it uiElement
        }
        /**
         * Removes the group.
         * @public
         */
        UIGroup.prototype.remove = function () {
            this.display = undefined;
            this.viewer = undefined;
            this.editor = undefined;
        };
        /**
         * Gets the group display.
         * @public
         * @returns {EGraphCore.Group} The group display.
         */
        UIGroup.prototype.getDisplay = function () {
            return this.display;
        };
        /**
         * Sets the main view of the group.
         * @public
         * @param {UINodeView} view - The main view of the group.
         */
        UIGroup.prototype.setView = function (view) {
            if (view !== undefined && view instanceof UINodeView) {
                this.display.views.main = view;
            }
        };
        /**
         * Gets the viewer.
         * @returns {UIEGraphViewer} The viewer.
         */
        UIGroup.prototype.getViewer = function () {
            return this.viewer;
        };
        /**
         * Gets the editor.
         * @public
         * @returns {UIEditor} The editor.
         */
        UIGroup.prototype.getEditor = function () {
            return this.viewer.getEditor();
        };
        /**
         * Gets the left position of the group.
         * @public
         * @returns {number} The left position of the group.
         */
        UIGroup.prototype.getLeft = function () {
            return this.display.actualLeft;
        };
        /**
         * Gets the top position of the group.
         * @public
         * @returns {number} The top position of the group.
         */
        UIGroup.prototype.getTop = function () {
            return this.display.actualTop;
        };
        /**
         * Gets the height of the group.
         * @public
         * @returns {number} The height of the group.
         */
        UIGroup.prototype.getHeight = function () {
            return this.display.actualHeight;
        };
        /**
         * Gets the width of the group.
         * @public
         * @returns {number} The width of the group.
         */
        UIGroup.prototype.getWidth = function () {
            return this.display.actualWidth;
        };
        /**
         * Sets the left position of the group.
         * TODO: Check if this implementation is specific to graph with its geometry?
         * @public
         * @param {number} left - The left position of the group.
         */
        UIGroup.prototype.setLeft = function (left) {
            this.display.geometry.left = left;
        };
        /**
         * Sets the top position of the group.
         * TODO: Check if this implementation is specific to graph with its geometry?
         * @public
         * @param {number} top - The top position of the group.
         */
        UIGroup.prototype.setTop = function (top) {
            this.display.geometry.top = top;
        };
        /**
         * Sets the height of the group.
         * TODO: Check if this implementation is specific to graph with its geometry?
         * @public
         * @param {number} height - The height of the group.
         */
        UIGroup.prototype.setHeight = function (height) {
            this.display.geometry.height = height;
            this.display.set('actualHeight', height);
            this.display.set('height', height);
        };
        /**
         * Sets the width of the group.
         * TODO: Check if this implementation is specific to graph with its geometry?
         * @public
         * @param {number} width - The width of the group.
         */
        UIGroup.prototype.setWidth = function (width) {
            this.display.geometry.width = width;
            this.display.set('actualWidth', width);
            this.display.set('width', width);
        };
        return UIGroup;
    }());
    return UIGroup;
});
