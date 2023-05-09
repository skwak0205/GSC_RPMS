define('DS/dsbaseUIControls/UnitsCacheInitialization', [
    'DS/dsbaseUIControls/UnitsCache'
], function (UnitsCache) {

    /**
     * @private
     * @summary Initialize cache according to widget data
     */
    let UnitsCacheInitialization = (function () {

        return new function () {

            /**
             * @private
             * @summary Get units cache
             * @returns Cache already/newly created
             */
            this.getUnitsCache = function (rdfServerURL) {

                // Is there a widget defined ?
                if (typeof widget !== 'undefined') {
                    if (!widget.getValue('unitsCache')) {
                        widget.setValue('unitsCache', new UnitsCache({
                            rdfServerURL: rdfServerURL
                        }));
                    }
                    return widget.getValue('unitsCache');

                // If no widget, is there already a cache defined ?
                } else if (window.unitsCache) {
                    return window.unitsCache;

                // If no cache
                } else {
                    window.unitsCache = new UnitsCache({
                        rdfServerURL: rdfServerURL
                    });
                    return window.unitsCache;
                }

            };

        };

    })();

    return UnitsCacheInitialization;

});
