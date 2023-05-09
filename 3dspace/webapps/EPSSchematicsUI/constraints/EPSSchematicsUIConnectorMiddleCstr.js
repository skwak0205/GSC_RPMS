/// <amd-module name='DS/EPSSchematicsUI/constraints/EPSSchematicsUIConnectorMiddleCstr'/>
define("DS/EPSSchematicsUI/constraints/EPSSchematicsUIConnectorMiddleCstr", ["require", "exports"], function (require, exports) {
    "use strict";
    /* eslint-enable no-unused-vars */
    var UIConnectorMiddleCstr = /** @class */ (function () {
        /**
         * @constructor
         * @param {EGraphCore.Node} parent - The parent node.
         */
        function UIConnectorMiddleCstr(parent) {
            this._parent = parent;
        }
        /**
         * The callback on the update constraint.
         * @protected
         * @param {EGraphCore.Connector} connector - The connector.
         */
        UIConnectorMiddleCstr.prototype.onupdate = function (connector) {
            var top = this._parent.top + (this._parent.height / 2);
            var left = this._parent.left + (this._parent.width / 2);
            connector.multiset('left', left, 'top', top, 'aleft', left, 'atop', top);
        };
        return UIConnectorMiddleCstr;
    }());
    return UIConnectorMiddleCstr;
});
