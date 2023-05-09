define('DS/dsbaseUIControls/GenericComponentsForm', [
    'DS/dsbaseUIControls/CharacteristicValuesSearcher',
    'UWA/Utils',
    'DS/UIKIT/Form'
], function (
    CharacteristicValuesSearcher,
    Utils,
    Form
) {

    'use strict';

    let GCForm = Form.extend({

        fields: {
            autocomplete: function (options, form) {

                /************************************ COPIED FROM DS/UIKIT/Form ************************************/
                var input, label,
                    that = form,
                    fieldWrapper,
                    defaultOptions = {
                        id: 'input-' + Utils.getUUID().substring(0, 6)
                    };

                if (options.hasOwnProperty('autocomplete')) {
                    options.inputAutocompleteAttribute = options.autocomplete;
                }
                /***************************************************************************************************/

                
                if (options.searchFunction) {
                    options.searchEngine = function (text, searchServices) {
                        return options.searchFunction(text, searchServices);
                    };
                } else {
                    options.searchEngine = function (text, searchServices) {
                        return [];
                    };
                    options.allowFreeInput = true;
                    options.minLengthBeforeSearch = 999999999;
                }

                options.customization = {
                    editMode: !options.disabled,
                    multiSelect: options.infos.IsList,
                    defaultValues: options.defaultItems,
                    onSelect: options.onSelect,
                    onSuggest: options.onSuggest,
                    onCleanDataset: options.onCleanDataset
                };

                input = new CharacteristicValuesSearcher(UWA.extend(options, defaultOptions, true));


                /************************************ COPIED FROM DS/UIKIT/Form ************************************/
                input.setId(Utils.getUUID().substr(0, 6));

                form.autocompletes.push(input);

                // When the input changes, fire a onChange for the form.
                input.addEvent('onChange', function () {
                    that.dispatchEvent('onChange', [this.getName(), this.getValue(), options.onchange]);

                });

                if (UWA.is(options.label) || UWA.is(options.name)) {
                    label = UWA.createElement('label', {
                        'for': input.getId(),
                        text: UWA.i18n((options.label || options.name) + that.options.labelSuffix)
                    });
                }

                fieldWrapper = that.createField.call(that, input, label);
                input.addEvent('onFocus', function () {
                    fieldWrapper.toggleClassName('focused', true);
                });

                input.addEvent('onBlur', function () {
                    fieldWrapper.toggleClassName('focused', false);
                });

                return fieldWrapper;
                /***************************************************************************************************/

            }
        }

    });

    return GCForm;

});
