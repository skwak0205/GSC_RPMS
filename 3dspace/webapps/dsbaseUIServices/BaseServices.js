define('DS/dsbaseUIServices/BaseServices', [
	"DS/dstoolsUIServices/dsbaseServices"
], function (commonServices) {
		/**
		* @summary dsbase accelerators for client-side APIs
		* @module DS/dsbaseUIServices/BaseServices
		*/

	'use strict';

	function asArray(variable) {
		var toReturn = variable
		if (!Array.isArray(toReturn)) {
            toReturn = [variable];
		}
		return toReturn;
	}
	var baseServices = {
		/**
		* @summary Returns an instance of BaseServices
		* @memberof module:DS/dsbaseUIServices/BaseServices
		* @returns {DS/dsbaseUIServices/BaseServicesTools}
		*
		* @param {String} rootServerURL - the root server URL
		*
		* @example
		*    define('DS/MyMwebModule/MyAMDFile',
		*           ['DS/dsbaseUIServices/BaseServices'], function (services) () {
		*         ...
		*         var myInstance = services.getHandle('root/server/url/');
		*         ...
		*    }
		*/
		getHandle: function(rootServerURL) {
			return new BaseServicesTools(rootServerURL);
		}
	};

	/**
	* @summary BaseServices constructor
	* @desc
	* <p> An instance  is returned by the  
	* {@link module:DS/dsbaseUIServices/BaseServices#getHandle}   method. </p>
	* @class DS/dsbaseUIServices/BaseServicesTools
	* @global
	*
		* @param {String} rootServerURL - the root server URL
	*/
	function BaseServicesTools(rootServerURL) {
		this.rootServerURL = rootServerURL;
		this.WAFRequestFunction = commonServices.getRequestFunction();
	}

	BaseServicesTools.prototype =  {
		/**
		 * Gets meta information about a given resource
		 * @memberof DS/dsbaseUIServices/BaseServicesTools#
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- resourcesURI (String or Array of Strings) : the resource(s) to analyze (mandatory)
		 * 						- toFetch (Array of Strings): information to retrieve (mandatory) Only one possible value for now: Type
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object containing requested information - through the onComplete callback!
		 */
		getMetaInfos: function(params) {
			if(params.timeout === undefined)
				params.timeout = 30000;
				
			var payload = [JSON.stringify(asArray(params.resourcesURI)), JSON.stringify(asArray(params.toFetch))];
			
			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL, "dsbase:getMetaInfos", params.urlParams),{
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
						data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout: params.timeout
				});
			});
			return promise;
		},
		/**
		 * Gets search suggestions for a given type
		 * @param {Object} params - an object containing the following fields :
		 * 			- rdfType : type (short or long URI) of the ressources we are looking for
		 * 			- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an array containing all the search suggestions according to the given type 
		 */
		getSearchSuggestionsFromType: function (params) {
			if (params.timeout === undefined) {
				params.timeout = 300000;
			}
			var payload = [params.rdfType];
			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var contextInfos = this.contextInfos;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(contextInfos, "dsbase:getSearchSuggestionsFromType"), {
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type': 'application/json' },
					data: dataArray,
					onComplete: function (data, status) {
						data.value = JSON.parse(data.value);
						if (params.cbObject !== undefined) {
							params.cbObject.onComplete(data, status);
						}
						resolve(data.value);
					},
					onFailure: function (data, status, error) {
						if (params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout: params.timeout
				});
			});
			return promise;
		},
	};
	BaseServicesTools.prototype.constructor = BaseServicesTools;
	return baseServices;
});
