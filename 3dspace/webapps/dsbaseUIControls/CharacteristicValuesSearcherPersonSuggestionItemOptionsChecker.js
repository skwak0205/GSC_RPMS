define('DS/dsbaseUIControls/CharacteristicValuesSearcherPersonSuggestionItemOptionsChecker', [
    'UWA/Core'
], function (
    UWA
) {


    /**
    * @summary CharacteristicValuesSearcherPersonSuggestionItemOptionsCheckerTool constructor
    * @class DS/dsbaseUIControls/CharacteristicValuesSearcherPersonSuggestionItemOptionsChecker
	*
    * @param {Object} searcherBadgeOptions  - Searcher person suggestion item options to check
    * @param {String} type                  - Type of the suggestion item (can be one of : 'picture', 'trigram')
	*/
    function CharacteristicValuesSearcherPersonSuggestionItemOptionsCheckerTool(searcherSuggestionItemOptions, type) {
        this.searcherSuggestionItemOptions = searcherSuggestionItemOptions;
        this.type = type;
    };


    CharacteristicValuesSearcherPersonSuggestionItemOptionsCheckerTool.prototype = (function () {

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

                if (!this.searcherSuggestionItemOptions.name) {
                    throw new Error('A name (person identity) must be defined !');
                }

                // Trigram ?
                if (this.type === 'trigram') {

                    if (!this.searcherSuggestionItemOptions.trigram) {
                        throw new Error('A trigram must be defined !');
                    }

                    // Picture ?
                    if (this.type === 'picture') {

                        if (!this.searcherSuggestionItemOptions.employeeId) {
                            throw new Error('An employee id (badge number) must be defined !');
                        }
                        
                    }

                }                

            },


            /*********************************************** 3 ***********************************************/
            /**
             * @summary Check customization/optional options
             */
            checkCustomizationOptions: function () { }

        };

    })();

    return CharacteristicValuesSearcherPersonSuggestionItemOptionsCheckerTool.prototype.constructor = CharacteristicValuesSearcherPersonSuggestionItemOptionsCheckerTool;

});
