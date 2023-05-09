define('DS/dsbaseUIControls/CharacteristicValuesSearcherOptionsChecker', [
    'UWA/Core',
    'i18n!DS/dsbaseUIControls/assets/nls/CharacteristicValuesSearcher'
], function (
    UWA,
    NLS
) {

    /**
    * Default values to use if some options are not set
    */
    let defaultValues = {
        editMode: true,
        multiSelect: false,
        defaultValues: [],
        onSuggest: function (item) {
            return (new UWA.Element('div', {
                html: item.label
            }));
        },
        onSelect: function (item) {
            return item.label;
        },
        onCleanDataset: function () { }
    };


    /**
    * @summary CharacteristicValuesSearcherOptionsCheckerTool constructor
    * @class DS/dsbaseUIControls/CharacteristicsEditorSearcherOptionsChecker
	*
	* @param {Object} searcherOptions - Searcher options to check
	*/
    function CharacteristicValuesSearcherOptionsCheckerTool(searcherOptions) {
        this.searcherOptions = searcherOptions;
    };


    CharacteristicValuesSearcherOptionsCheckerTool.prototype = (function () {

        /*---- 3.1 ----*/
        let _checkCustomizationModeOptions = function () {
            if (this.searcherOptions.customization.editMode === undefined) {
                this.searcherOptions.customization.editMode = defaultValues.editMode;
            }
        };


        /*---- 3.2 ----*/
        let _checkCustomizationMultiSelectOptions = function () {
            if (this.searcherOptions.customization.multiSelect === undefined) {
                this.searcherOptions.customization.multiSelect = defaultValues.multiSelect;
            }
        };


        /*---- 3.3 ----*/
        let _checkCustomizationDefaultValuesOptions = function () {

            if (!this.searcherOptions.customization.defaultValues) {
                this.searcherOptions.customization.defaultValues = defaultValues.defaultValues;
            } else {
                _checkArrayValues(this.searcherOptions.customization.defaultValues, [
                    NLS.get('CVS.optionsChecker.defaultValues.array.error'),
                    NLS.get('CVS.optionsChecker.defaultValues.arrayObjects.error'),
                    NLS.get('CVS.optionsChecker.defaultValues.labelExpected.error'),
                    NLS.get('CVS.optionsChecker.defaultValues.valueExpected.error')
                ]);
            }

        };


        /*---- 3.4 ----*/
        let _checkCustomizationOnSuggestOptions = function () {

            if (this.searcherOptions.customization.onSuggest === undefined) {
                this.searcherOptions.customization.onSuggest = defaultValues.onSuggest;
            }

            if (typeof this.searcherOptions.customization.onSuggest !== 'function') {
                throw new Error(NLS.get('CVS.optionsChecker.onSuggest.notFunction.error'));
            }

        };


        /*---- 3.5 ----*/
        let _checkCustomizationOnSelectOptions = function() {

            if (this.searcherOptions.customization.onSelect === undefined) {
                this.searcherOptions.customization.onSelect = defaultValues.onSelect;
            }

            if (typeof this.searcherOptions.customization.onSelect !== 'function') {
                throw new Error(NLS.get('CVS.optionsChecker.onSelect.notFunction.error'));
            }

        };


        /*---- 3.6 ----*/
        let _checkCustomizationOnCleanDatasetOptions = function () {

            if (this.searcherOptions.customization.onCleanDataset === undefined) {
                this.searcherOptions.customization.onCleanDataset = defaultValues.onCleanDataset;
            }

            if (typeof this.searcherOptions.customization.onCleanDataset !== 'function') {
                throw new Error(NLS.get('CVS.optionsChecker.onCleanDataset.notFunction.error'));
            }

        };


        let _checkArrayValues = function (arrayValues, messages) {

            if (!Array.isArray(arrayValues)) {
                throw new Error(messages[0]);
            }

            arrayValues.forEach(function (av) {
                if (typeof av !== 'object' || Array.isArray(av)) {
                    throw new Error(messages[1]);
                } else {
                    if (!av.label) throw new Error(messages[2]);
                    if (!av.value) throw new Error(messages[3]);
                }
            });

        };


        return {

            /*********************************************** 1 ***********************************************/
            /**
             * @summary Check all options passed to the searcher
             */
            checkAllOptions: function () {
                this.checkMandatoryOptions();      // 2
                this.checkCustomizationOptions();  // 3
            },


            /*********************************************** 2 ***********************************************/
            /**
             * @summary Check mandatory options
             */
            checkMandatoryOptions: function () {

                if (!this.searcherOptions.searchEngine) {
                    throw new Error(NLS.get('CVS.optionsChecker.searchEngine.notExisting.error'));
                }

                if (typeof this.searcherOptions.searchEngine !== 'function') {
                    throw new Error(NLS.get('CVS.optionsChecker.searchEngine.notFunction.error'));
                }

            },


            /*********************************************** 3 ***********************************************/
            /**
             * @summary Check customization/optional options
             */
            checkCustomizationOptions: function () {

                if (!this.searcherOptions.customization) {
                    this.searcherOptions.customization = defaultValues;
                }

                _checkCustomizationModeOptions.bind(this)();            // 3.1
                _checkCustomizationMultiSelectOptions.bind(this)();     // 3.2
                _checkCustomizationDefaultValuesOptions.bind(this)()    // 3.3
                _checkCustomizationOnCleanDatasetOptions.bind(this)();  // 3.4
                _checkCustomizationOnSuggestOptions.bind(this)();       // 3.5
                _checkCustomizationOnSelectOptions.bind(this)();        // 3.6

            },


            /*********************************************** 4 ***********************************************/
            /**
             * @summary Check given results for search engine
             * @param {Object[]} givenResults - Results to check
             */
            checkGivenResultsForSearchEngine: function (givenResults) {
                _checkArrayValues(givenResults, [
                    NLS.get('CVS.optionsChecker.defaultValues.array.error'),
                    NLS.get('CVS.optionsChecker.defaultValues.arrayObjects.error'),
                    NLS.get('CVS.optionsChecker.defaultValues.labelExpected.error'),
                    NLS.get('CVS.optionsChecker.defaultValues.valueExpected.error')
                ]);
            }

        };

    })();

    return CharacteristicValuesSearcherOptionsCheckerTool.prototype.constructor = CharacteristicValuesSearcherOptionsCheckerTool;

});
