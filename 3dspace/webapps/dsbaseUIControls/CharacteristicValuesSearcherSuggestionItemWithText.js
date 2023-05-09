define('DS/dsbaseUIControls/CharacteristicValuesSearcherSuggestionItemWithText', [
    //'DS/dsbaseUIControls/CharacteristicValuesSearcherPersonSuggestionItemOptionsChecker',
    'UWA/Core',
    'UWA/Class/View'
], function (
    //CharacteristicValuesSearcherPersonSuggestionItemOptionsChecker,
    UWA,
    View
) {

    'use strict';


    /**
    * @summary OOTB suggestion item to display a label and a sublabel
    * @extends UWA/Class/View
    */
    let CharacteristicValuesSearcherSuggestionItemWithText = View.extend({

        tagName: 'div',


        /**
         * @param {Object}  options                     - Options to adapt behavior & customize the view
         * @param {String}  options.label               - Label displayed (on top)
         * @param {String}  options.sublabel            - Sublabel displayed below the label
         *
         * @constructs CharacteristicValuesSearcherSuggestionItemWithText
         * @summary Setup component
         * @memberof module:DS/dsbaseUIControls/CharacteristicValuesSearcherSuggestionItemWithText
         */
        setup: function (options) {

            // Checking options
            //(new CharacteristicValuesSearcherPersonSuggestionItemOptionsChecker(options, 'trigram')).checkAllOptions();
            this.options = options;

            this.elements.label = this.createLabel();
            this.elements.sublabel = this.createSublabel();
            this.render();

        },


        /**
         * @private
         * @summary Render the complete view
         */
        render: function() {
            this.elements.label.inject(this.container);
            this.elements.sublabel.inject(this.container);
        },


        /**
         * @private
         * @summary Create the UI element to display the label
         */
        createLabel: function () {

            return (new UWA.Element('div', {
                class: 'item-label',
                html: this.options.label
            }));

        },


        /**
         * @private
         * @summary Create the UI element to display the sublabel
         */
        createSublabel: function() {

            return (new UWA.Element('div', {
                class: 'sub-label',
                html: this.options.sublabel
            }));

        }

    });

    return CharacteristicValuesSearcherSuggestionItemWithText;

});
