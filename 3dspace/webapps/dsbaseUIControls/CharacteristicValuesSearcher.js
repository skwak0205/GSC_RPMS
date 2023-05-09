define('DS/dsbaseUIControls/CharacteristicValuesSearcher', [
    'UWA/Utils',
    'UWA/Event',
    'DS/dsbaseUIControls/CharacteristicValuesSearcherOptionsChecker',
    'DS/dsbaseUIControls/CharacteristicValuesSearcherService',
    'DS/UIKIT/Autocomplete',
    'DS/UIKIT/Badge',
    'css!DS/UIKIT/UIKIT.css',
    'css!DS/dsbaseUIControls/assets/style/CharacteristicValuesSearcher.css'
], function (
    Utils,
    Event,
    CharacteristicValuesSearcherOptionsChecker,
    SearchService,
    Autocomplete,
    Badge
) {

    'use strict';

    /**
     * @summary Generic autocomplete input to search for resources
     * @extends DS/UIKIT/Autocomplete
     */
    let ValuesSearcher = Autocomplete.extend({

        tagName: 'div',


        defaultOptions: {
            minLengthBeforeSearch: 3,
            maxSuggestsToDisplay: 4
        },


        /**
         * @private
         * @summary Get complementary class names for the searcher component
         */
        getClassNames: function (classNames) {

            let className = 'characteristic-values-searcher ';

            // Verify if other CSS class names are defined
            if (classNames !== undefined) {
                classNames.forEach(function (cn) {
                    className += cn + ' ';
                });
            }

            className += 'autocomplete';
            return className;

        },

        /**
         * @typedef     {Object} Item
         * @property    {String} label  - Label to display when searching for a text
         * @property    {String} value  - Value being set
         */

        /**
         * @typedef {Function} searchInLocal
         * @param   {String} text       - Text to search
         * @param   {Item[]} localItems - Local items used for searching
         */

        /**
         * @typedef {Function} searchInFedSearch
         * @param   {String}    query   - Query to execute through FedSearch service
         * @param   {String}    tenant  - Tenant to use 
         * @param   {String[]}  sources - FedSearch sources
         */

        /**
         * Callback used to search resources for a given text
         * @callback    SearchEngine    
         * @param       {String} text - Text to search
         * @param       {Object.<String, searchInLocal|searchInFedSearch>}  searchServices  - OOTB services that can be used to search resources matching the given text
         * @returns     {Item[]|Promise.<Item[]>}
         */

        /**
         * @constructor
         * @alias CharacteristicValuesSearcher
         * @summary Setup component
         * 
         * @param {Object}          options                                     - Options to adapt behavior and customize the input
         * @param {SearchEngine}    options.searchEngine                        - Search engine to use in order to find values|resources
         * 
         * @param {Object}          options.customization                       - Object to define customization parameters to render the input
         * @param {String[]}        [options.customization.classNames=[]]       - Array of additionnal CSS class names
         * @param {Boolean}         [options.customization.multiSelect=false]   - Boolean to indicate if we can select more than one item
         * @param {Boolean}         [options.customization.editMode=true]       - Boolean to indicate if input should be disabled or not(true => edit, false => read)
         * @param {Item[]}          [options.customization.defaultValues=[]]    - Array of default values to display in the input
         * 
         * @param {Function}        [options.customization.onSuggest]           - Function called when an item is shown in the list of suggestions (has to return a UWA/Element to render the associated item in the list of suggestions)
         * @param {Function}        [options.customization.onSelect]            - Function called when an item is selected in the list of suggestions (has to return a string to render the associated badge)         
         * @param {Function}        [options.customization.onCleanDataset]      - Function called when all items are cleaned at the same time 
         */
        init: function (options) {

            // CSS class names
            if (options.customization) {
                options.className = this.getClassNames(options.customization.classNames);
            }

            // Checking
            this.searcherOptionsChecker = new CharacteristicValuesSearcherOptionsChecker(options);
            this.searcherOptionsChecker.checkAllOptions();

            // Dataset
            options.customization.defaultValues.forEach(function (dv) {
                if (!dv.selected) dv.selected = true;
            });
            options.datasets = {
                name: 'dataset',
                items: options.customization.defaultValues,
                configuration: {
                    remoteSearchEngine: this.getRemoteSearchEngine()
                }
            };

            // Multi-select ?
            options.multiSelect = options.customization.multiSelect;

            this._parent(options);

        },


        /**
         * @private
         * @summary Build the input after initialization (init)
         */
        buildSkeleton: function () {

            this._parent();

            // Multi-select ?
            // If yes, add a clear button to remove all items in search input
            let multiSelect = this.options.customization.multiSelect;
            if (multiSelect) {
                this.getContent().addClassName('input-autocomplete input-group');
                this.getContent().getElement('.autocomplete-suggests').addClassName('form-group');
                this.elements.clearIcon = this.createClearIcon();
                this.elements.clearIcon.inject(this.getContent());
            } else {
                this.getContent().addClassName('characteristic-values-searcher-single-value');
            }

            // Edit mode ?
            let readOnly = !this.options.customization.editMode;
            this.setDisabled(readOnly);
            if (readOnly && multiSelect) {
                this.elements.clearIcon.addClassName('clear-icon-non-clickable');
            }

        },


        /**
         * @private
         * @summary Create an icon button to clear a list of items
         */
        createClearIcon: function () {

            let that = this;

            return UWA.createElement('span', {
                class: 'input-group-addon fonticon fonticon-close'
            }).addEvent('click', function () {
                if (!that.getContent().hasClassName('characteristic-values-searcher-disabled')) {
                    if (that.selectedItems.length > 0) {
                        that.cleanDataset('dataset');
                    }
                    that.options.customization.onCleanDataset();
                }
            });

        },


        /**
         * @private
         * @summary Check if a variable is a promise or not
         */
        isPromise: function (variable) {
            return (variable !== undefined && variable !== null && typeof variable.then === 'function');
        },


        /**
         * @private
         * @summary Format results to respect wanted format for DS/UIKit/Autocomplete
         * @param {Object}      dataset         - Remote search engine dataset
         * @param {Object[]}    givenResults    - Results given by defined function
         */
        formatResults: function (dataset, givenResults) {

            this.searcherOptionsChecker.checkGivenResultsForSearchEngine(givenResults);

            let results = {};
            results.matchingItems = givenResults;
            results.dataset = dataset;

            return results;

        },


        /**
         * @private
         * @summary Create the remote search engine in order to get suggestions while typing
         */
        getRemoteSearchEngine: function () {

            let that = this;

            return function (dataset, text) {

                let results;

                // If text is empty, return no result
                if (!text.trim()) {
                    results = {};
                    results.matchingItems = [];
                    results.dataset = dataset;
                    return results;
                }

                // Otherwise, look for eventual results
                return new Promise(function (resolve, reject) {

                    let returnVariable = that.options.searchEngine(
                        text,
                        {
                            searchInFedSearch: SearchService.getFedSearchSuggestions,
                            searchInLocal: SearchService.getLocalSuggestions
                        }
                    );

                    if (that.isPromise(returnVariable)) {
                        returnVariable.then(function (results) {
                            resolve(that.formatResults(dataset, results));
                        });
                    } else {
                        resolve(that.formatResults(dataset, returnVariable));
                    }

                }).catch(function (error) {
                    console.log(error);
                    error.type = 'error';
                    error.dataset = dataset;
                    reject(error);
                });

            };

        },


        /**
         * @private
         * @summary Select the given item
         * @override
         */
        onSelect: function (item, position) {
            
            /************************************ COPIED FROM DS/UIKIT/Autocomplete ************************************/
            let itemBadgeOptions, itemBadge, itemLi, innerInput;
            let options = this.options;
            let that = this;

            if (!options.multiSelect) {
                
                // If single select mode, unselect the currently selected item and update input
                this.selectedItems[0] && this.onUnselect(this.selectedItems[0]);
                this.elements.input.value = options.customization.onSelect(item);
                this.elements.container.toggleClassName(this.CLS_FILLED, true);

                // Hide suggestions list
                this.dispatchEvent('onHideSuggests');

            } else {

                innerInput = this._buildInnerInput();

                // If multi select mode, add item in input container
                itemBadgeOptions = {
                    id: 'selected-' + item.id,
                    content: options.customization.onSelect(item),
                    selectable: true,
                    events: {
                        onClose: function (event) {
                            var deletionPosition = that.getBadgePosition(itemBadge.elements.container),
                                // Next position : current (next badge is going to be on this position) or -1 if no badge on right
                                newPosition = (deletionPosition === that.badges.length - 1) ? -1 : deletionPosition;

                            that._handleBadgeDeletion(deletionPosition, newPosition);
                            Event.preventDefault(event);
                        }
                    }
                };

                if (options.closableItems) {
                    itemBadgeOptions.closable = true;
                }
                /***********************************************************************************************************/


                // BMR4
                itemBadge = new Badge(itemBadgeOptions);


                /************************************ COPIED FROM DS/UIKIT/Autocomplete ************************************/
                // To allow focus on badge
                itemBadge.elements.container.setAttribute('tabindex', '-1');
                itemBadge.elements.container.setStyle('outline', '0');

                // Unselect previously selected badge if some
                this._toggleBadgeSelection(this.currentPosition, false);

                if (position >= 0) {
                    this.resetInput(position);

                    // Insert badges right after previous if existing, else at the beginning of the input (index is 0)
                    if (this.badges[position - 1]) {
                        itemBadge.inject(this.badges[position - 1].elements.container, 'after');
                    } else {
                        itemBadge.inject(this.elements.inputContainer.getChildren()[0], 'before');
                    }
                    this.badges.splice(position, 0, itemBadge);
                    this.innerInputs.splice(position, 0, innerInput);
                } else {
                    this.resetInput();
                    itemBadge.inject(this.elements.input, 'before');
                    this.badges.push(itemBadge);
                    this.innerInputs.push(innerInput);
                }

                innerInput.inject(itemBadge.elements.container, 'before');

                // Update position in badges items and focus this new badge
                this.dispatchEvent('onUpdateInputPosition', [position]);
                this._toggleBadgeSelection(this.currentPosition);

                // If item can be selected only once, retrieve its li in suggests to select it
                if (!options.itemMultiSelect) {
                    itemLi = this.elements.suggests.getElement('#' + this.CLS_ITEM + '-' + item.id);
                    itemLi && itemLi.addClassName('selected-item');
                }
            }

            item.selected = true;

            if (position >= 0) {
                this.selectedItems.splice(position, 0, item);
            } else {
                this.selectedItems.push(item);
            }

            // Re-show all in multi-select + item multi select modes
            this.options.multiSelect && this.options.itemMultiSelect && this.options.showSuggestsOnFocus && this.showAll();

            // Call the item handler if it was provided.
            if (UWA.is(item.handler, 'function')) {
                item.handler.call(this);
            }

            this.suggestsDisplayed && this._updateSuggestsPosition();
            /***********************************************************************************************************/

        },


       /**
        * @private
        * @summary Add a dataset for autocomplete suggestions
        * 
        * @param {Object}   [options]                               - The dataset options
        *
        * @param {String}   [options.name]                          - Name for dataset identification.
        * @param {Boolean}  [options.displayName=false]             - True to show dataset name with matching suggestions below.
        * @param {String}   [options.searchUrl]                     - Optional URL to be used to retrieve remote suggestions instead of passing them through a static array. Expected to contains '{text}' to be set by the input text to search for. Please use remoteSearchEngine for advanced remote suggestions retrieving.
        *
        * @param {Object}   [options.items]                         - Static array of suggestions.
        * @param {String}   options.items.value                     - Required suggestion value.
        * @param {String}   [options.items.label]                   - Suggestion label to display. Takes value if none..
        * @param {String}   [options.items.subLabel]                - Optional, sub-label displayed below label.
        * @param {Boolean}  [options.items.disabled=false]          - True if the suggestion is disabled by default.
        * @param {Function} [options.items.handler]                 - An optional handler to call when this item is clicked.
        *
        * @param {Object}   [configuration]                         - Configuration for dataset manipulations.
        * @param {Function} [configuration.searchEngine]            - Optional custom definition of search engine for the given dataset. Called with two arguments : the dataset and the text being searched. Expected to return matching items for given input text. Default search engine used if none.
        * @param {Function} [configuration.remoteSearchEngine]      - Optional custom definition of search engine for the given remote dataset. Called with two arguments : the dataset and the text being searched. Expected to return matching items for given input text. Default remote search engine used if none.
        * @param {Function} [configuration.filterEngine]            - Optional custom definition of filter engine for the given dataset. This could be used as a post-processing on matching data : sort, remove duplicates... On the given dataset only. Called with two arguments : the dataset and suggestions matching input text. Expected to return matching items after filtering. No filtering by default.
        *
        * @param {Object}   [configuration.templateEngine]          - Optional custom definition of template engine for the given dataset. This could be used to display suggestions the way you want for the given dataset. Called for every matching suggestion with three arguments : the DOM container where you should inject your template, the suggestion dataset and its corresponding item data.
        *
        * @param {Function} [configuration.dataParser]              - Optional custom method to parse data in a correct output for autocomplete needs from the response of the searchUrl call. Expected to return items in a correct output i.e. Array of items with at least a 'value' for each item, and additionally any variable to be used in a custom templateEngine. defaultDataParser called if none.
        * @param {Function} [configuration.onGetResults]            - Optional callback that will be called on remote search results retrieve.
        * @param {Function} [configuration.onNoResults]             - Optional callback that will be called on remote search results retrieve if no results.
        * @param {Function} [configuration.onError]                 - Optional callback that will be called on remote search results retrieve if an error occurred. Called with the data returned by server and the text of the input.
        * @param {Function} [configuration.onTimeout]               - Optional callback that will be called on remote search. Called with the data returned by server and the text of the input.
        *
        * @override
        */
        addDataset: function (options, configuration) {

            /************************************ COPIED FROM DS/UIKIT/Autocomplete ************************************/
            let that = this;
            let dataset = { items: [] };
            /***********************************************************************************************************/


            /**
             * @summary Called when an item needs to be displayed in the suggestion list, you should apply the value and maybe change the DOM here.
             *
             * @param  {Element} element        - The suggestion container where custom template should be injected
             * @param  {Object}  dataset        - The suggestion dataset
             * @param  {Object}  item           - The suggestion data (value, label... and more custom data)
             * @param  {boolean} [item.parent]  - True if the item is a parent item
             */
            let templateEngine = function (element, dataset, item) {

                let itemLabel, itemSubLabel;
                element.addClassName('default-template');

                if (!item.parent) {

                    itemLabel = UWA.createElement('span', { 'class': 'item-label' });
                    that.options.customization.onSuggest(item).inject(itemLabel);

                    if (item.parentReference) {
                        itemLabel.setStyle('padding-left', item.parentReference.level + 'em');
                        itemLabel.addClassName('sub-item');
                    }

                    itemLabel.inject(element);

                    /*if (item.subLabel) {
                        element.addClassName('with-sub-label');
                        itemSubLabel = UWA.createElement('span', { 'class': 'sub-label' });
                        itemSubLabel.setText(item.subLabel);
                        if (item.parentReference) {
                            itemSubLabel.setStyle('padding-left', item.parentReference.level + 'em');
                        }
                        itemSubLabel.inject(element);
                    }*/

                } else {

                    /************************************ COPIED FROM DS/UIKIT/Autocomplete ************************************/
                    // Display parent item
                    itemLabel = UWA.createElement('span', { 'class': 'item-parent-label' });
                    itemLabel.setText(item.label);

                    if (item.parentReference) {
                        itemLabel.setStyle('margin-left', item.parentReference.level + 'em');
                        itemLabel.addClassName('sub-item');
                    }

                    itemLabel.inject(element);
                    /***********************************************************************************************************/

                }
            };

            /************************************ COPIED FROM DS/UIKIT/Autocomplete ************************************/
            dataset.id = Utils.getUUID().substr(0, 6);
            dataset.dataCache = {};

            if (options.items) {
                this.addItems(options.items, dataset);
            }

            // Handling configuration being either in first argument or second argument
            configuration = configuration ? configuration : options.configuration;

            // Merging authorized configuration methods in dataset object
            if (configuration) {
                ['searchEngine', 'remoteSearchEngine', 'filterEngine', 'templateEngine', 'dataParser', 'onError', 'onTimeout', 'onGetResults', 'onNoResults'].forEach(function (method) {
                    if (UWA.is(configuration[method], 'function')) {
                        dataset[method] = configuration[method];
                    }
                });

            }

            dataset.templateEngine = dataset.templateEngine ? dataset.templateEngine : templateEngine;
            this.datasets.push(UWA.merge(dataset, options));

            // If user has already begin to interact with the autocomplete, update current suggestions.
            this.used && this.getSuggestions(false, true);
            /***********************************************************************************************************/

        },


        /******************************************* PUBLIC METHODS *******************************************/
        /**
         * @summary Disable/Enable autocomplete input
         * @param {Boolean} disabled - Indicate if the input has to be disabled or not 
         * @override
         */
        setDisabled: function (disabled) {

            this._parent(disabled);

            if (disabled) {
                this.getContent().addClassName('characteristic-values-searcher-disabled');
            } else {
                this.getContent().removeClassName('characteristic-values-searcher-disabled');
            }

        },


        /**
         * @summary Get labels associated to values
         * @return String with labels separated by ';' token
         */
        getLabel: function () {

            let labels = '';

            this.selectedItems.forEach(function (si) {
                labels += si.label + ';';
            });
            
            labels = labels.substring(0, labels.length - 1);
            return labels;
            
        },


        /**
         * @summary Clear data in input
         * @override
         */
        clear: function () {

            let that = this;

            this.datasets.forEach(function(d) {
                let id = d.id;
                that.cleanDataset(id);
            });

        }

    });

    return ValuesSearcher;

});
