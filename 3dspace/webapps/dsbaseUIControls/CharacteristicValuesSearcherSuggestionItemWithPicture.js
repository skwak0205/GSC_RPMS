define('DS/dsbaseUIControls/CharacteristicValuesSearcherSuggestionItemWithPicture', [
    //'DS/dsbaseUIControls/CharacteristicValuesSearcherSuggestionItemOptionsChecker',
    'UWA/Core',
    'UWA/Class/View'
], function (
   // CharacteristicValuesSearcherSuggestionItemOptionsChecker,
    UWA,
    View
) {

    'use strict';


    /**
    * @summary OOTB suggestion item to display a picture, a label and a sublabel
    * @extends UWA/Class/View
    */
    let CharacteristicValuesSearcherSuggestionItemWithPicture = View.extend({

        tagName: 'div',


        /**
         * @param {Object}  options                     - Options to adapt behavior & customize the view
         * @param {String}  options.picture             - URL to load the picture (preferably, with same height and width)
         * @param {String}  options.label               - Label displayed next to the picture (on the right)
         * @param {String}  options.sublabel            - Sublabel displayed below the label
         * 
         * @constructs CharacteristicValuesSearcherSuggestionItemWithPicture
         * @summary Setup component
         * @memberof module:DS/dsbaseUIControls/CharacteristicValuesSearcherSuggestionItemWithPicture
         */
        setup: function (options) {

            // Checking options
            //(new CharacteristicValuesSearcherPersonSuggestionItemOptionsChecker(options, 'picture')).checkAllOptions();
            this.options = options;

            this.elements.image = this.createImage();
            this.elements.label = this.createLabel();
            this.elements.sublabel = this.createSubLabel();
            this.render();

        },


        /**
         * @private
         * @summary Render the complete view
         */
        render: function() {
            this.elements.image.inject(this.container);
            this.elements.label.inject(this.container);
            this.elements.sublabel.inject(this.container);
        },


        /**
         * @private
         * @summary Create the UI element to embed the image
         */
        createImage: function () {

            return (new UWA.Element('img', {
                class: 'people-search-img',
                src: this.options.picture
            }));

        },


        /**
         * @private
         * @summary Create the UI element to display label
         */
        createLabel: function () {

            return (new UWA.Element('div', {
                class: 'item-label',
                html: this.options.label
            }));

        },


        /**
         * @private
         * @summary Create the UI element to display sublabel
         */
        createSubLabel: function () {

            return (new UWA.Element('div', {
                class: 'sub-label',
                html: this.options.sublabel
            }));

        }

    });

    return CharacteristicValuesSearcherSuggestionItemWithPicture;

});
