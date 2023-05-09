define('DS/dsbaseUIControls/UnitConversionView', [
    'DS/dsbaseUIControls/UnitsCacheInitialization',
    'DS/dsbaseUIControls/UnitConversionViewOptionsChecker',
    'UWA/Core',
    'UWA/Class/View',
    'DS/UIKIT/Input/Text',
    'DS/UIKIT/DropdownMenu',
    'i18n!DS/dsbaseUIControls/assets/nls/UnitConversionView',
    'css!DS/UIKIT/UIKIT.css',
    'css!DS/dsbaseUIControls/assets/style/UnitConversionView.css'
], function (
    UnitsCacheInitialization,
    UnitConversionViewOptionsChecker,
    UWA,
    View,
    Text,
    DropdownMenu,
    NLS
) {

    'use strict';


    /**
     * @summary Generic view to convert a value to another (according to a selected unit)
     * @extends UWA/Class/View
     */
    let UnitConversionView = View.extend({


        /**
         * @private
         * @summary Add complementary class names to the unit conversion view component
         * @returns {String} Complementary class names separated by white spaces
         * @override
         */
        className: function () {
            return 'unit-conversion-view';
        },


        /**
         * @param {Object}      options                     - Options to adapt behavior & customize the view
         * @param {String}      options.rdfServerURL        - RDF server root URL (mandatory)
         * @param {Number}      options.originalValue       - Value taken as reference for conversions (mandatory)
         * @param {String}      options.quantityURI         - Long URI of the quantity
         * @param {String}      options.unitURI             - Long URI of the unit
         * @param {String}      options.prefixURI           - Long URI of the prefixed unit
         * 
         * @constructs UnitConversionView
         * @summary Setup component
         * @memberof module:DS/dsbaseUIControls/UnitConversionView
         * @override 
         */
        setup: function (options) {
            if (options.unitURI === 'http://www.3ds.com/RDF/Corpus/dsqt/kilogram') { // kg case
                options.unitURI = 'http://www.3ds.com/RDF/Corpus/dsqt/gram';
                options.prefixURI = 'http://www.3ds.com/RDF/Corpus/dsqt/kilo';
            }

            // Checking options
            (new UnitConversionViewOptionsChecker(options)).checkAllOptions();

            this._parent();

            // Original unit
            this.quantityURI = this.accessUnitsCache().toLongURI(options.quantityURI);
            this.currentUnitURI = this.accessUnitsCache().toLongURI(options.unitURI);
            
            // Is unit prefixable ?
            let that = this;
            this.accessUnitsCache().isUnitPrefixable(this.quantityURI, this.currentUnitURI).then(function (unitPrefixable) {

                that.originalPrefixSymbol = null;
                that.currentPrefixSymbol = null;
                that.originalUnitSymbol = null;
                that.currentUnitSymbol = null;

                // Original value
                that.currentValue = options.originalValue;

                // Original prefix
                if ((options.prefixURI) && (unitPrefixable)) {
                    that.currentPrefixURI = that.accessUnitsCache().toLongURI(options.prefixURI);
                } else {
                    that.currentPrefixURI = undefined;
                }

                // Is prefixes dropdown displayed ?
                that.currentIsUnitPrefixable = unitPrefixable;
                that.hiddenPrefixDropdown = !unitPrefixable;

                that.createComponents();
                that.render();

            });

        },


        /**
         * @private
         * @summary Render the view with created components
         * @override
         */
        render: function () {
            this.elements.parentDiv.inject(this.container);
        },


        /**
         * @private
         * @summary 
         * @returns {DS/dsbaseUIControls/UnitsCache} Cache for prefixes, units, prefixed units... 
         */
        accessUnitsCache: function () {
            return UnitsCacheInitialization.getUnitsCache(this.options.rdfServerURL);
        },


        /**
         * @private
         * @summary Create the components of the view
         */
        createComponents: function () {
            this.createMainDivs();
            this.createOriginalValueInput();
            this.createArrow();
            this.createConvertedValueInput();
        },


        /**
         * @private
         * @summary Create the main divs (parts of the view)
         */
        createMainDivs: function () {

            this.elements.parentDiv = new UWA.Element('div');

            // Original value
            this.elements.originalValueDiv = new UWA.Element('div', {
                class: 'row unit-conversion-view-original-value-div'
            }).inject(this.elements.parentDiv);

            // Arrow
            this.elements.arrowDiv = new UWA.Element('div', {
                class: 'row unit-conversion-view-arrow-div'
            }).inject(this.elements.parentDiv);

            // Converted value
            this.elements.convertedValueDiv = new UWA.Element('div', {
                class: 'row unit-conversion-view-converted-value-div'
            }).inject(this.elements.parentDiv);

        },


        /**
         * @private
         * @summary Create the first div with the original value
         */
        createOriginalValueInput: function () {

            let that = this;

            let div = new UWA.Element('div', {
                class: 'col-xs-10 col-xs-offset-1'
            }).inject(this.elements.originalValueDiv);

            // Label
            (new UWA.Element('label', {
                html: NLS.get('UnCV.originalValue'),
                class: 'unit-conversion-view-label'
            })).inject(div);

            // Input group
            let inputGroup = new UWA.Element('div', {
                class: 'input-group'
            }).inject(div);

            // Input
            this.elements.originalValueInput = new Text({
                value: this.options.originalValue,
                className: 'unit-conversion-view-input',
                disabled: true,
                label: NLS.get('UnCV.originalValue')
            }).inject(inputGroup);

            // Symbol span
            let symbolSpan = (new UWA.Element('span', {
                class: 'input-group-addon unit-conversion-view-addon'
            })).inject(inputGroup);

            // Prefix span
            let prefixSpan = (new UWA.Element('span')).inject(symbolSpan);

            // Unit span
            let unitSpan = (new UWA.Element('span')).inject(symbolSpan);

            // Prefix content
            if ((this.currentPrefixURI !== undefined) && (this.currentIsUnitPrefixable)) {
                this.accessUnitsCache().getPrefixSymbol(this.currentPrefixURI).then(function (prefixSymbol) {
                    that.originalPrefixSymbol = prefixSymbol;
                    that.currentPrefixSymbol = prefixSymbol;
                    prefixSpan.setContent(prefixSymbol);
                });
            }

            // Unit content
            this.accessUnitsCache().getUnitSymbol(this.quantityURI, this.currentUnitURI).then(function (unitSymbol) {
                that.originalUnitSymbol = unitSymbol;
                that.currentUnitSymbol = unitSymbol;
                unitSpan.setContent(unitSymbol);
            });            

        },


        /**
         * @private
         * @summary Create the second div with the bottom-oriented arrow
         */
        createArrow: function () {

            let div = new UWA.Element('div', {
                class: 'col-xs-10 col-xs-offset-1'
            }).inject(this.elements.arrowDiv);

            (new UWA.Element('i', {
                class: 'fonticon fonticon-down'
            })).inject(div);

        },


        /**
         * @private
         * @summary Create the third div with the live-converted value
         */
        createConvertedValueInput: function () {

            let that = this;

            // Get prefixes
            this.accessUnitsCache().listPrefixes().then(function (prefixes) {

                let div = new UWA.Element('div', {
                    class: 'col-xs-10 col-xs-offset-1'
                }).inject(that.elements.convertedValueDiv);

                // Label
                (new UWA.Element('label', {
                    html: NLS.get('UnCV.convertedValue')
                })).inject(div);

                // Input group
                let inputGroup = new UWA.Element('div', {
                    class: 'input-group'
                }).inject(div);

                // Input
                that.elements.convertedValueInput = new Text({
                    value: that.options.originalValue,
                    className: 'unit-conversion-view-original-value',
                    disabled: true,
                    label: NLS.get('UnCV.originalValue')
                }).inject(inputGroup);

                // Prefixed unit
                let prefixedUnitSpan = new UWA.Element('span', {
                    class: 'input-group-addon unit-conversion-view-addon unit-conversion-view-addon-clickable'
                }).inject(inputGroup);

                let prefixedUnitContent = new UWA.Element('span').inject(prefixedUnitSpan);
                that.accessUnitsCache().getPrefixSymbol(that.currentPrefixURI).then(function (prefixSymbol) {
                    if (prefixSymbol) {
                        prefixedUnitContent.setContent(prefixSymbol);
                    } else {
                        prefixedUnitContent.setContent('');
                    }
                });

                (new UWA.Element('i', {
                    class: 'fonticon fonticon-chevron-down'
                })).inject(prefixedUnitSpan);

                that.elements.dropdownPrefixedUnits = new DropdownMenu({
                    className: 'unit-conversion-view-prefixed-units-dropdown',
                    maxItemsBeforeScroll: 3,
                    target: prefixedUnitSpan,
                    items: function () {

                        let items = [];

                        // Add empty
                        items.push({
                            text: '',
                            handler: function() {

                                // Backup
                                let previousPrefixURI = that.currentPrefixURI;
                                let previousPrefixSymbol = that.currentPrefixSymbol;

                                // Set
                                that.currentPrefixURI = undefined;
                                that.currentPrefixSymbol = null;

                                // Update
                                that.updateConvertedInputValue().then(function () {
                                    prefixedUnitContent.setContent('');
                                }).catch(function () {
                                    // Restore
                                    that.currentPrefixURI = previousPrefixURI;
                                    that.currentPrefixSymbol = previousPrefixSymbol;
                                });

                            }
                        });

                        prefixes.forEach(function (p) {
                            items.push({
                                text: p.symbol,
                                name: p.uri,
                                uri: p.uri,
                                handler: function () {

                                    // Backup
                                    let previousPrefixURI = that.currentPrefixURI;
                                    let previousPrefixSymbol = that.currentPrefixSymbol;

                                    // Set
                                    that.currentPrefixURI = p.uri;
                                    that.currentPrefixSymbol = p.symbol;

                                    // Update
                                    that.updateConvertedInputValue().then(function () {
                                        prefixedUnitContent.setContent(p.symbol);
                                    }).catch(function () {
                                        // Restore
                                        that.currentPrefixURI = previousPrefixURI;
                                        that.currentPrefixSymbol = previousPrefixSymbol;
                                    });

                                }
                            });
                        });

                        return items;

                    }()
                });
                if (!that.currentIsUnitPrefixable) {
                    prefixedUnitSpan.hide();
                    that.hiddenPrefixDropdown = true;
                }

                // Unit
                let unitSpan = (new UWA.Element('span', {
                    class: 'input-group-addon unit-conversion-view-addon unit-conversion-view-addon-clickable'
                })).inject(inputGroup);
                    
                if (that.options.unitURI === 'http://www.3ds.com/RDF/Corpus/dsqt/kilogram') { // kg case
                    that.options.unitURI = 'http://www.3ds.com/RDF/Corpus/dsqt/gram';
                    that.options.prefixURI = 'http://www.3ds.com/RDF/Corpus/dsqt/kilo';
                }

                let unitContent;
                that.accessUnitsCache().getUnitSymbol(that.options.quantityURI, that.options.unitURI).then(function (symbol) {
                    unitContent = new UWA.Element('span', {
                        content: symbol
                    }).inject(unitSpan);
                    (new UWA.Element('i', {
                        class: 'fonticon fonticon-chevron-down'
                    })).inject(unitSpan);
                });

                // Units dropdown
                that.accessUnitsCache().getUnits(that.options.quantityURI).then(function (units) {
                    that.elements.dropdownUnits = new DropdownMenu({
                        className: 'unit-conversion-view-units-dropdown',
                        maxItemsBeforeScroll: 3,
                        target: unitSpan,
                        items: function () {

                            let items = [];
                            let gramUnit;
                            units.forEach(function (u) {
                                if (u.uri !== 'http://www.3ds.com/RDF/Corpus/dsqt/kilogram') {
                                    let item = {
                                        text: u.symbol,
                                        name: u.uri,
                                        uri: u.uri,
                                        handler: function () {

                                            if (prefixedUnitSpan && unitContent) {

                                                // Backup
                                                let previousUnitURI = that.currentUnitURI;
                                                let previousUnitSymbol = that.currentUnitSymbol;
                                                let previousIsUnitPrefixable = that.currentIsUnitPrefixable;

                                                // Set global variables
                                                that.currentUnitURI = u.uri;
                                                that.currentUnitSymbol = u.symbol;
                                                that.currentIsUnitPrefixable = u.isPrefixable;
                                                if (that.currentIsUnitPrefixable) {
                                                    that.hiddenPrefixDropdown = false;
                                                    prefixedUnitSpan.show();
                                                } else {
                                                    that.hiddenPrefixDropdown = true;
                                                    prefixedUnitSpan.hide();
                                                }

                                                // Convert to new value
                                                that.updateConvertedInputValue().then(function () {
                                                    unitContent.setContent(u.symbol);
                                                    if (that.currentIsUnitPrefixable) {
                                                        that.accessUnitsCache().getPrefixSymbol(that.currentPrefixURI).then(function (prefixSymbol) {
                                                            if (prefixSymbol !== undefined) {
                                                                prefixedUnitContent.setContent(prefixSymbol);
                                                            } else {
                                                                prefixedUnitContent.setContent('');
                                                            }
                                                        });
                                                    }
                                                }).catch(function () {

                                                    // Restore data
                                                    that.currentUnitURI = previousUnitURI;
                                                    that.currentUnitSymbol = previousUnitSymbol;
                                                    that.currentIsUnitPrefixable = previousIsUnitPrefixable;

                                                    // Prefixable ?
                                                    if (that.currentIsUnitPrefixable) {
                                                        that.hiddenPrefixDropdown = false;
                                                        prefixedUnitSpan.show();
                                                    } else {
                                                        that.hiddenPrefixDropdown = true;
                                                        prefixedUnitSpan.hide();
                                                    }

                                                });
                                                
                                            }

                                        }
                                    };

                                    if (item.uri === 'http://www.3ds.com/RDF/Corpus/dsqt/gram')
                                        gramUnit = item;
                                    else
                                        items.push(item);
                                }
                            });
                            if (gramUnit !== undefined) // add gram first instead of kilogram
                                items.unshift(gramUnit);

                            return items;

                        }()
                    });
                });

            });

        },


        /**
         * @private
         * @summary Update the value (by calling dsqt:convertUnits) according to current units & prefix units
         * @returns {Promise} Return the promise used to convert a value from one unit to another
         */
        updateConvertedInputValue: function () {

            let that = this;
            let targetPrefixURI = (this.hiddenPrefixDropdown) ? undefined : this.currentPrefixURI;

            return this.accessUnitsCache().convertValue(this.options.quantityURI, this.options.unitURI, this.options.prefixURI, this.currentUnitURI, targetPrefixURI, this.options.originalValue).then(function (newValue) {
                that.elements.convertedValueInput.setValue(newValue);
                that.currentValue = newValue;
            });

        },


        /**
         * @summary Return all the interesting data about the original and the converted values
         * @return {Object} Object with all the information (unit URI, prefix unit URI, new value, ...)
         */
        getValues: function () {

            var convertedUnit   = this.currentUnitURI;
            var convertedPrefix = this.currentPrefixURI;
            if (!this.hiddenPrefixDropdown
            && this.currentUnitURI === 'http://www.3ds.com/RDF/Corpus/dsqt/gram' && this.currentPrefixURI === 'http://www.3ds.com/RDF/Corpus/dsqt/kilo') { // kg case
                convertedUnit = 'http://www.3ds.com/RDF/Corpus/dsqt/kilogram';
                convertedPrefix = undefined;
            }

            return {
                original: {
                    quantityURI: this.options.quantityURI,
                    unitURI: this.options.unitURI,
                    prefixURI: this.options.prefixURI,
                    symbol: (this.originalPrefixSymbol === null) ? this.originalUnitSymbol : this.originalPrefixSymbol + this.originalUnitSymbol,
                    value: this.options.originalValue.toLocaleString('fullwide', { useGrouping: false })
                },
                converted: {
                    quantityURI: this.options.quantityURI,
                    unitURI: convertedUnit,
                    prefixURI: (this.hiddenPrefixDropdown) ? undefined : convertedPrefix,
                    symbol: (this.hiddenPrefixDropdown || this.currentPrefixSymbol === null) ? this.currentUnitSymbol : this.currentPrefixSymbol + this.currentUnitSymbol,
                    value: this.currentValue.toLocaleString('fullwide', { useGrouping: false })
                }
            };
        }

    });

    return UnitConversionView;

});
