define('DS/dsbaseUIServices/AccessServices', [
	"DS/dstoolsUIServices/dsbaseServices"
], function (commonServices) {
		/**
		* @summary dsaccess accelerators for client-side APIs
		* @module DS/dsbaseUIServices/AccessServices
		*/

	'use strict';

	var accessServices = {
		/**
		* @summary Returns an instance of AccessServices
		* @memberof module:DS/dsbaseUIServices/AccessServices
		* @returns {DS/dsbaseUIServicesAccessServicesTools}
		*
		* @param {String} rootServerURL - the root server URL
		*
		* @example
		*    define('DS/MyMwebModule/MyAMDFile',
		*           ['DS/dsbaseUIServices/AccessServices'], function (services) () {
		*         ...
		*         var myInstance = services.getHandle('root/server/url/');
		*         ...
		*    }
		*/
		getHandle: function(rootServerURL) {
			return new AccessServicesTools(rootServerURL);
		}
	}

	/**
	* @summary AccessServices constructor
	* @desc
	* <p> An instance  is returned by the
	* {@link module:DS/dsbaseUIServices/AccessServices#getHandle}   method. </p>
	* @class DS/dsbaseUIServices/AccessServicesTools
	* @global
	*
	* @param {String} rootServerURL - the root server URL
	*/
	function AccessServicesTools(rootServerURL) {
		this.rootServerURL = rootServerURL;
		this.WAFRequestFunction = commonServices.getRequestFunction();
	}

	AccessServicesTools.prototype =  {
		/**
		 * Checks the list of dsaccess operations granted to the current user for the provided resource.
		 * @memberof DS/dsbaseUIServices/AccessServicesTools#
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 						- resourceURI (String) : the resource to check
		 *						- operationURIs (String[]) : A list of dsaccess operations that will restrain the scope
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Promise} A list of dsaccess operations granted to the user among the given scope
		 */
		getOperationsGrantedToUser: function(params) {
			var payload = [params.resourceURI, JSON.stringify(params.operationURIs)];

			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL, 'dsaccess:sharing.getOperationsGrantedToUser', params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
					data: dataArray,
					onComplete: function(data, status) {
						if(params.cbObject) {
							params.cbObject.onComplete(data, status);
						}
						resolve(data);
					},
					onFailure: function(data, status, error){
						if(params.cbObject) {
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
		 * Checks the capacity to run a standalone dsbase:Operation or a dsbase:Operation as a method on a specific resource
		 * @memberof DS/dsbaseUIServices/AccessServicesTools#
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- operationURI (String) : the dsbase:Operation URI to check
     *						- resourceURI (String) : for method only, the resource on which the method is applied
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Promise} true if the operation is granted, false otherwise
		 */
		isRunGranted: function(params) {
			var predefinedProc = "dsaccess:responsibility.runStandAloneOperationAuthorized";
			var payload = [params.operationURI];
			if(params.resourceURI !== undefined) {
				payload = [params.resourceURI,params.operationURI];
				predefinedProc = "dsaccess:responsibility.runMethodOperationAuthorized";
			}

			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL, predefinedProc, params.urlParams),{
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
		}
	};
	AccessServicesTools.prototype.constructor = AccessServicesTools;
	return accessServices;
});
