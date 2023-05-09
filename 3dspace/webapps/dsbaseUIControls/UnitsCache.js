define('DS/dsbaseUIControls/UnitsCache', [
    'DS/dstoolsUIServices/dsbaseServices',
    'UWA/Class'
], function (
    DSBaseServices,
    Class
) {

    'use strict';


    function toLongURI(shortURI) {
        if (shortURI.startsWith('http://')) {
            return shortURI;
        }
        return 'http://www.3ds.com/RDF/Corpus/' + shortURI.replace(':', '/');
    }


    function toShortURI(longURI) {
        if (longURI.startsWith('dsqt:')) {
            return longURI;
        }
        let split = longURI.split('/');
        return 'dsqt:' + split[split.length - 1];
    }


    /**
     * @summary Cache to avoid calling the server for retrieving units multiple times
     * @extends UWA/Class
     */
    let UnitsCache = Class.extend({


        /**
         * @private
         * @summary Initialize variables to have an operational cache
         * @param {Object}  options                 - Options needed to initialize the cache
         * @param {String}  options.rdfServerURL    - RDF server root URL (mandatory)
         * @override
         */
        init: function (options) {

            this.cache = {
                prefixes: [],               // Array of objects                 -> [ { uri: 'http://www.3ds.com/RDF/Corpus/dsqt/milli', symbol: 'm' }, { uri: 'http://www.3ds.com/RDF/Corpus/dsqt/centi', symbol: 'c' }, ...]
                prefixesSymbols: {},        // Map of prefixes and symbols      -> { 'http://www.3ds.com/RDF/Corpus/dsqt/milli': 'm', 'http://www.3ds.com/RDF/Corpus/dsqt/centi' : 'c', ... }
                units: {},                  // Map of quantities and units      -> { 'http://www.3ds.com/RDF/Corpus/dsqt/Length' : [ { uri: 'http://www.3ds.com/RDF/Corpus/dsqt/meter', symbol: 'm', isPrefixable: true }, ...  ], ... }
                unitsSymbols: {},           // Map of units and symbols         -> { 'http://www.3ds.com/RDF/Corpus/dsqt/meter': 'm', 'http://www.3ds.com/RDF/Corpus/dsqt/pound': 'lb', ... }
                unitsPrefixable: {},        // Maps of units and booleans       -> { 'http://www.3ds.com/RDF/Corpus/dsqt/meter': true, 'http://www.3ds.com/RDF/Corpus/dsqt/pound': false, ... }
            };

            this.rdfServerURL = options.rdfServerURL;

            this.listPrefixes = this._listPrefixes;
            this.getPrefixSymbol = this._getPrefixSymbol;
            this.getUnits = this._getUnits;
            this.getUnitSymbol = this._getUnitSymbol;
            this.isUnitPrefixable = this._isUnitPrefixable;
            this.toShortURI = this._toShortURI;
            this.toLongURI = this._toLongURI;
            this.convertValue = this._convertValue;

        },


        /**
         * @private
         * @summary Method to get prefixes fro prefixable units
         */
        _listPrefixes: function () {

            let that = this;

            return new Promise(function (resolve, reject) {

                if (that.cache.prefixes.length > 0) {
                    resolve(that.cache.prefixes);
                } else {

                    let url = that.rdfServerURL + 'v0/invoke/dsqt:listPrefixes';

                    /* NEED TO BE REPLACED BY WRAPPER */
                    // Get prefixes
                    DSBaseServices.getRequestFunction()(url, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        type: 'json',
                        data: JSON.stringify([]),
                        onComplete: function (data, status) {

                            var prefixes = data.member;
        
                            // data.value = JSON.parse(data.value);

                            prefixes.forEach(function (prefix) {
                                that.cache.prefixesSymbols[prefix['@id']] = prefix.symbol;
                            });

                            // that.cache.prefixes = prefixes;
                            that.cache.prefixes = prefixes.map(function(prefix){
                                prefix.uri = toLongURI(prefix['@id']);
                                prefix.shortURI = prefix['@id'];
                                return prefix;
                            });
                            resolve(prefixes);
                            
                        },
                        onFailure: function (data, status, error) {
                            reject(error);
                        },
                        timeout: 999999
                    });

                }

            });

        },


        /**
         * @private
         * @summary Method to get a symbol from a prefix URI
         * @param {String} prefixURI - Short/Long URI of the prefix (for example, dsqt:milli, http://www.3ds.com/RDF/Corpus/dsqt/centi, ...)
         */
        _getPrefixSymbol: function (prefixURI) {

            let that = this;

            return new Promise(function (resolve, reject) {

                if (that.cache.prefixesSymbols[prefixURI]) {
                    resolve(that.cache.prefixesSymbols[prefixURI]);
                } else {

                    that.listPrefixes().then(function (prefixes) {

                        let foundedPrefix = prefixes.find(function (prefix) {
                            // return ((prefix['@id'] === prefixURI));
                            return ((prefix.uri === prefixURI) || (prefix.shortURI === prefixURI));
                        });

                        let symbol;
                        if (foundedPrefix) {
                            symbol = foundedPrefix.symbol;
                        }

                        that.cache.prefixesSymbols[prefixURI] = symbol;
                        resolve(symbol);

                    });

                }

            });

        },


        /**
         * @private
         * @summary Method to get units associated to a quantity
         * @param {String} quantityURI - Long URI of the quantity (for example, http://www.3ds.com/RDF/Corpus/dsqt/Length, ...)
         */
        _getUnits: function (quantityURI) {

            let that = this;
            return new Promise(function (resolve, reject) {

                if (quantityURI !== undefined) {

                    // In cache ?
                    if (that.cache.units[quantityURI]) {
                        resolve(that.cache.units[quantityURI]);
                    } else {

                        let url = that.rdfServerURL + 'v0/invoke/dsqt:getUnits'; // ?$mask=dsqt:Mask.Unit.Default

                        /* NEED TO BE REPLACED BY WRAPPER */
                        // Get quantity  units
                        DSBaseServices.getRequestFunction()(url, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            type: 'json',
                            data: JSON.stringify([
                                '<' + quantityURI + '>'
                            ]),
                            onComplete: function (data, status) {

                                if (data) {

                                    // Get returned units
                                    let returnedUnits = data[toShortURI(quantityURI)].member;

                                    // Is there at least one unit ?
                                    if (returnedUnits !== undefined) {

                                        let units = [];
                                        returnedUnits.forEach(function (unit) {
                                            let uri = toLongURI(unit['@id']);
                                            let symbol = unit.symbol;
                                            let isPrefixable = (unit.isPrefixable !== undefined) ? unit.isPrefixable : false;
                                            units.push({
                                                uri: uri,
                                                symbol: symbol,
                                                isPrefixable: isPrefixable
                                            });
                                            that.cache.unitsSymbols[uri] = symbol;
                                            that.cache.unitsPrefixable[uri] = isPrefixable;
                                        });
                                        // if (toShortURI(quantityURI) === 'dsqt:Mass') {
                                        //     var kgUnit = units.shift(); // remove dsqt:kilogram
                                        //     delete that.cache.unitsSymbols[kgUnit.uri];
                                        //     delete that.cache.unitsPrefixable[kgUnit.uri];
                                        // }

                                        that.cache.units[quantityURI] = units;
                                        resolve(units);

                                    } else {
                                        resolve([]);
                                    }
                                    

                                }

                            },
                            onFailure: function (data, status, error) {
                                reject(error);
                            },
                            timeout: 999999
                        });

                    }

                } else {
                    resolve([]);
                }
                

            });

        },


        /**
         * @private
         * @summary Method to get the symbol associated to a unit
         * @param {String}  quantityURI - Short/Long URI of the quantity
         * @param {String}  unitURI     - Short/Long URI of the unit
         */
        _getUnitSymbol: function (quantityURI, unitURI) {

            let that = this;

            return new Promise(function (resolve, reject) {

                if (that.cache.unitsSymbols[unitURI]) {
                    resolve(that.cache.unitsSymbols[unitURI]);
                } else {

                    that.getUnits(quantityURI).then(function(units) {

                        let foundedUnit = units.find(function (u) {
                            return (u.uri === unitURI);
                        });
                        
                        let symbol;
                        if (foundedUnit) {
                            symbol = foundedUnit.symbol;
                        }

                        that.cache.unitsSymbols[unitURI] = symbol;
                        resolve(symbol);

                    });

                }

            });

        },


        /**
         * @private
         * @summary Method to know if a unit is prefixable or not
         * @param {String} quantityURI  - Short/Long URI of the quantity
         * @param {String} unitURI      - Short/Long URI of the unit
         */
        _isUnitPrefixable: function (quantityURI, unitURI) {

            let that = this;

            return new Promise(function (resolve, reject) {

                if (that.cache.unitsPrefixable[unitURI]) {
                    resolve(that.cache.unitsPrefixable[unitURI]);
                } else {

                    that.getUnits(quantityURI).then(function(units) {

                        let foundedUnit = units.find(function (u) {
                            return (u.uri === unitURI);
                        });
                        
                        let isPrefixable;
                        if (foundedUnit) {
                            isPrefixable = foundedUnit.isPrefixable;
                        }

                        that.cache.unitsPrefixable[unitURI] = isPrefixable;
                        resolve(isPrefixable);

                    });

                }

            });

        },


        /**
         * @private
         * @summary Convert to short URI
         * @param {String} longURI - Long dsqt URI
         */
        _toShortURI: function (longURI) {
            return toShortURI(longURI);
        },


        /**
         * @private
         * @summary Convert to long URI
         * @param {String} short - Short dsqt URI
         */
        _toLongURI: function (shortURI) {
            return toLongURI(shortURI);
        },


        /**
         * @private
         * @summary Method to convert a value from one unit to another
         * @param {String} quantity         - Short/Long URI of the quantity
         * @param {String} sourceUnit       - Short/Long URI of the source unit
         * @param {String} [sourcePrefix]   - Short/Long URI of the source prefix
         * @param {String} targetUnit       - Short/Long URI of the target unit
         * @param {String} [targetPrefix]   - Short/Long URI of the target prefix
         * @param {Number} valueToConvert   - Value to convert
         */
        _convertValue: function (quantity, sourceUnit, sourcePrefix, targetUnit, targetPrefix, valueToConvert) {

            // Construct complete URL with root URL and hardcoded dsqt:convertUnits endpoint
            let url = this.rdfServerURL + 'v0/invoke/dsqt:convertUnits';

            return new Promise(function (resolve, reject) {

                let dataToPost = {
                    quantity: (quantity.startsWith('http://')) ? '<' + quantity + '>' : quantity,
                    'source-unit': (sourceUnit.startsWith('http://')) ? '<' + sourceUnit + '>' : sourceUnit,
                    'target-unit': (targetUnit.startsWith('http://')) ? '<' + targetUnit + '>' : targetUnit,
                    values: [
                        valueToConvert
                    ]
                };

                // Source prefix ?
                if (sourcePrefix) {
                    dataToPost['source-prefix'] = (sourcePrefix.startsWith('http://')) ? '<' + sourcePrefix + '>' : sourcePrefix;
                }

                // Target prefix ?
                if (targetPrefix) {
                    dataToPost['target-prefix'] = (targetPrefix.startsWith('http://')) ? '<' + targetPrefix + '>' : targetPrefix;
                }

                /* NEED TO BE REPLACED BY WRAPPER */
                // Get converted unit
                DSBaseServices.getRequestFunction()(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    type: 'json',
                    data: JSON.stringify([
                        JSON.stringify([
                            dataToPost
                        ])
                    ]),
                    onComplete: function (data, status) {
                        data.value = JSON.parse(data.value);
                        resolve(data.value[0]['converted-values'][0]);
                    },
                    onFailure: function (data, status, error) {
                        reject(error);
                    },
                    timeout: 999999
                });

            });

        }

    });

    return UnitsCache;

});
