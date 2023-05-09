define('DS/dsbaseUIControls/UnitConversionViewOptionsChecker', [
    'i18n!DS/dsbaseUIControls/assets/nls/UnitConversionView',
], function (
    NLS
) {

    /**
    * @summary UnitConversionViewOptionsCheckerTool constructor
    * @class DS/dsbaseUIControls/UnitConversionViewOptionsChecker
	*
	* @param {Object} unitsOptions - Units options to check
	*/
    function UnitConversionViewOptionsCheckerTool(unitsOptions) {
        this.unitsOptions = unitsOptions;
    };


    UnitConversionViewOptionsCheckerTool.prototype = (function () {

        return {

            /*********************************************** 1 ***********************************************/
            /**
             * @summary Check all options passed to the unit conversion view
             */
            checkAllOptions: function () {
                this.checkMandatoryOptions();      // 2
            },


            /*********************************************** 2 ***********************************************/
            /**
             * @summary Check mandatory options
             */
            checkMandatoryOptions: function () {

                if (!this.unitsOptions.rdfServerURL) {
                    throw new Error(NLS.get('UnCV.optionsChecker.rdfServerURL.error'));
                }

                if (this.unitsOptions.originalValue === undefined) {
                    throw new Error(NLS.get('UnCV.optionsChecker.originalValue.notExisting.error'));
                }

                if (typeof this.unitsOptions.originalValue !== 'number') {
                    throw new Error(NLS.get('UnCV.optionsChecker.originalValue.type.error'));
                }

                if (!this.unitsOptions.quantityURI) {
                    throw new Error(NLS.get('UnCV.optionsChecker.quantityURI.error'));
                }

                if (!this.unitsOptions.unitURI) {
                    throw new Error(NLS.get('UnCV.optionsChecker.unitURI.error'));
                }

            }

        };

    })();

    return UnitConversionViewOptionsCheckerTool.prototype.constructor = UnitConversionViewOptionsCheckerTool;

});
