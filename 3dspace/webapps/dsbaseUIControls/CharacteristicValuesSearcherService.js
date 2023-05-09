define('DS/dsbaseUIControls/CharacteristicValuesSearcherService', [
    'DS/dsbaseUIControls/CharacteristicValuesSearcherSetup',
    'DS/WAFData/WAFData'
], function (
    Setup,
    WAFData
) {

    'use strict';

    let CharacteristicValuesSearcherService = {


        /***************************************************** FEDSEARCH *****************************************************/
        getFedSearchSuggestions: function (query, platformId, sources) {

            let getPredicateValue = function (attrs, predicate) {
                let value;
                attrs.forEach(function (a) {
                    if (a.name === predicate) {
                        value = a.value;
                    }
                });
                return value;
            };

            return new Promise(function (resolve, reject) {
                return Setup.getInstance(platformId).fedSearchPromise.then(function (fedSearchInfos) {

                    let url = fedSearchInfos.fedSearchURL + '/search';
                    let currentUserID = fedSearchInfos.userID;

                    let requestOptions = {
                        type: 'json',
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept-Language': widget.lang
                        },
                        data: JSON.stringify({
                            source: sources,
                            order_by: 'desc',
                            order_field: 'relevance',
                            nresults: 8,
                            tenant: platformId,
                            select_predicate: ['ds6w:label', 'ds6w:resourceUid'],
                            label: 'autocomplete-input-' + currentUserID + '-' + Date.now(),
                            query: query//'[ds6w:type]:string AND ([ds6w:what]:(' + 'feature' + '*) OR ' + 'feature' + ' )'//'[ds6w:type]:"pno:Person" AND ([ds6w:label]:(' + text.trim() + '*) OR ' + text.trim() + ' )'
                        }),
                        onComplete: function (response) {
                            let toReturn = [];
                            if (response.results) {
                                response.results.forEach(function (r) {
                                    let attrs = r.attributes;
                                    let value = getPredicateValue(attrs, 'ds6w:resourceUid');
                                    if (!value) {
                                        value = getPredicateValue(attrs, 'resourceid');
                                    }
                                    if (value) {
                                        let label = getPredicateValue(attrs, 'ds6w:label');
                                        toReturn.push({
                                            label: (label) ? label : value,
                                            value: value,
                                            data: attrs
                                        });
                                    }
                                });
                            }
                            resolve(toReturn);
                        },
                        onFailure: function (error) {
                            resolve([]);
                        }
                    };

                    WAFData.authenticatedRequest(url, requestOptions);
                });
            });

        },


        /***************************************************** LOCAL *****************************************************/
        getLocalSuggestions: function (text, localItems) {

            return new Promise(function (resolve, reject) {
                let results = [];
                localItems.forEach(function (element) {
                    if (typeof element.value === 'string') {

                        if (element.label.toLowerCase().includes(text.toLowerCase())
                            || element.value.toLowerCase().includes(text.toLowerCase())) {
                            results.push({
                                label: element.label,
                                value: element.value,
                                data: element.data
                            });

                        } else if (element.data) {

                            let toBeAdded = false;
                            Object.keys(element.data).forEach(function (key) {
                                if ((typeof element.data[key] === 'string') && (element.data[key].toLowerCase().includes(text.toLowerCase()))) {
                                    toBeAdded = true;
                                }
                            });

                            if (toBeAdded) {
                                results.push({
                                    label: element.label,
                                    value: element.value,
                                    data: element.data
                                });
                            }

                        }
                    }
                });
                resolve(results);
            });

        },


        /***************************************************** GLOBAL *****************************************************/
        /*getAllSuggestions: function (text, localItems) {
            
            let fedSearchSuggestionsPromise = this.getFedSearchSuggestions(text);
            let localSearchSuggestionsPromise = this.getLocalSuggestions(text, localItems);

            return Promise.all([
                fedSearchSuggestionsPromise,
                localSearchSuggestionsPromise
            ]).then(function(results) {
                return results[1]; // BMR4 - TO FIX, BECAUSE ONLY TAKING INTO ACCOUNT LOCAL SEARCH
            });

        }*/
    };

    return CharacteristicValuesSearcherService;

});
