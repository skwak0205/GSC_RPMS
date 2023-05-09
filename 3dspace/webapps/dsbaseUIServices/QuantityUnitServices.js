define('DS/dsbaseUIServices/QuantityUnitServices', [
	"DS/dstoolsUIServices/dsbaseServices"
], function (commonServices) {
		/**
		* @summary dsqt accelerators for client-side APIs
		* @module DS/dsbaseUIServices/QuantityUnitServices
		*/

	'use strict';
	function asArray(variable) {
		var toReturn = variable
		if (!Array.isArray(toReturn)) {
            toReturn = [variable];
		}
		return toReturn;
	}
	var quantityUnitServices = {
		/**
		* @summary Returns an instance of QuantityUnitServices
		* @memberof module:DS/dsbaseUIServices/QuantityUnitServices
		* @returns {DS/dsbaseUIServices/QuantityUnitServicesTools}
		*
		* @param {String} rootServerURL - the root server URL
		*
		* @example
		*    define('DS/MyMwebModule/MyAMDFile',
		*           ['DS/dsbaseUIServices/QuantityUnitServices'], function (services) () {
		*         ...
		*         var myInstance = services.getHandle('root/server/url/');
		*         ...
		*    }
		*/
		getHandle: function(rootServerURL) {
			return new QuantityUnitServicesTools(rootServerURL);
		}
	};
	
	/**
	* @summary QuantityUnitServices constructor
	* @desc
	* <p> An instance  is returned by the  
	* {@link module:DS/dsbaseUIServices/QuantityUnitServices#getHandle}   method. </p>
	* @class DS/dsbaseUIServices/QuantityUnitServicesTools
	* @global
	*
		* @param {String} rootServerURL - the root server URL
	*/
	function QuantityUnitServicesTools(rootServerURL) {
		this.rootServerURL = rootServerURL;
		this.WAFRequestFunction = commonServices.getRequestFunction();
	}
	QuantityUnitServicesTools.prototype =  {
		/**
		 * Returns the available units for a given quantity (possibly restrained in a specific system of units)
		 * @memberof DS/dsbaseUIServices/QuantityUnitServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- quantityURIs : the quantity(ies) to retrieve from (mandatory)
		 * 						- systemOfUnitsURI : the system of unit to consider (dsqt:SI, dsqt:Imperial, ...) (optional)
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (optional)
		 * @return {Promise} resolve/onComplete will be called with the result of the query as a map having the quantity as key and the units as values 
		 */
		getUnits: function(params) {
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				var dataArray;
				if(params.systemOfUnitsURI !== undefined)
					dataArray = JSON.stringify([JSON.stringify(asArray(params.quantityURIs)), params.systemOfUnitsURI]);
				else {
					dataArray = JSON.stringify([JSON.stringify(asArray(params.quantityURIs))]);
				}
				WAFReq (commonServices.buildURL(rootServerURL,"dsqt:getUnits", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
					data: dataArray,
					onComplete: function(data, status) { 
						// data.value = JSON.parse(data.value);
						var units = {};
						Object.assign(units, data);
						delete units["@href"];
						delete units["@id"];
						delete units["@mask"];
						delete units["@code"];

						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(units,status); 
						}
						resolve(units);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},
		/**
		 * Returns the prefixed versions of the units for a given unit
		 * @memberof DS/dsbaseUIServices/QuantityUnitServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- unitURI : the unit to consider (mandatory)
		 * 						- prefixesFilter : a filter to indicate which prefixes should be used (optional)
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (optional)
		 * @return {Promise} resolve/onComplete will be called with the result of the query
		 */
		getPrefixedUnits: function(params) {
			var urlParams = (typeof params.urlParams === 'object') ? params.urlParams : {};

			urlParams["$this"] = params.unitURI;

			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				var dataArray;
				if(params.prefixesFilter !== undefined)
					dataArray = JSON.stringify(asArray(params.prefixesFilter));
				else	
					dataArray = "[]";
					WAFReq (commonServices.buildURL(rootServerURL,"dsqt:getPrefixedUnits", urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
					data: dataArray,
					onComplete: function(data, status) { 
						data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status); 
						}
						resolve(data.value);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},
		/**
		 * Converts a value in a given unit to another
		 * @memberof DS/dsbaseUIServices/QuantityUnitServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- conversionsToDo : an array of conversion objects (ex: {"quantity":"dsqt:Time","source-prefix":"dsqt:milli","source-unit":"dsqt:second","target-unit":"dsqt:second","values":[1000.0,200.0]})  (mandatory)
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (optional)
		 * @return {Promise} resolve/onComplete will be called with the result of the conversion
		 */
		convertUnits: function(params) {
			var dataArray = JSON.stringify([JSON.stringify(asArray(params.conversionsToDo))]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsqt:convertUnits", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
					data: dataArray,
					onComplete: function(data, status) { 
						data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status); 
						}
						resolve(data.value);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		}
	};
	QuantityUnitServicesTools.prototype.constructor = QuantityUnitServicesTools;
	return quantityUnitServices;
});
