define('DS/dsbaseUIServices/GraphUIServices', [
	'DS/dstoolsUIServices/dsbaseServices'
], function (commonServices) {
		/**
	  * @private
		* @summary operation graph accelerators for client-side APIs
		* @module DS/dsbaseUIServices/GraphUIServices
		*/

	'use strict';
	function asArray(variable) {
		var toReturn = variable
		if (!Array.isArray(toReturn)) {
            toReturn = [variable];
		}
		return toReturn;
	}

	var graphUIServices = {
		/**
		* @summary Returns an instance of GraphUIServices
		* @memberof module:DS/dsbaseUIServices/GraphUIServices
		* @returns {DS/dsbaseUIServices/GraphUIServicesTools}
		*
		* @param {String} rootServerURL - the root server URL
		*
		* @example
		*    define('DS/MyMwebModule/MyAMDFile',
		*           ['DS/dsbaseUIServices/GraphUIServices'], function (services) () {
		*         ...
		*         var myInstance = services.getHandle('root/server/url/');
		*         ...
		*    }
		*/
		getHandle: function(rootServerURL) {
			return new GraphUIServicesTools(rootServerURL);
		}
	};
	
	/**
	* @summary GraphUIServices constructor
	* @desc
	* <p> An instance  is returned by the  
	* {@link module:DS/dsbaseUIServices/GraphUIServices#getHandle}   method. </p>
	* @class DS/dsbaseUIServices/GraphUIServicesTools
	* @global
	*
		* @param {String} rootServerURL - the root server URL
	*/
	function GraphUIServicesTools(rootServerURL) {
		this.rootServerURL = rootServerURL;
		this.WAFRequestFunction = commonServices.getRequestFunction();
	}

	//INVOKE dsbase:getUpdataeGraphAsJSON dscust:myComposer;

	GraphUIServicesTools.prototype =  {
		/**
		 * Gets all the known Operations libraries as RDF operation
		 *
		 * @param  {Object}  parameters - an object containing the following fields:
		 *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object containing the list of Operations libraries - through the onComplete callback!
		 */
		getOperationLibraries: function(parameters) {
			var params = (parameters === undefined || parameters === null) ? {} : parameters;
			if(params.timeout === undefined) {
				params.timeout = 300000;
			}
			var payload = [];
			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL,"dsbase:getOperationLibraries", params.urlParams),{
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
					timeout: params.timeout
				});
			});
			return promise;
		},
		/**
		 * Gets the graph of a resource for monitoring (composite operation / updatable)
		 * @memberof DS/dsbaseUIServices/GraphUIServicesTools#
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *            - resourceURI : the resource holding the graph (mandatory)
		 *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object containing the list of Operations libraries - through the onComplete callback!
		 */
		getGraphAsJSON: function(params) {
			if(params.timeout === undefined) {
				params.timeout = 300000;
			}
			var payload = [params.resourceURI];
			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL,"dsbase:getGraphAsJSON", params.urlParams),{
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
					timeout: params.timeout
				});
			});
			return promise;
		},
		/**
		 * Gets the graph of a resource for authoring(composite operation / updatable)
		 * @memberof DS/dsbaseUIServices/GraphUIServicesTools#
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *            - resourceURI : the resource holding the graph (mandatory)
		 *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object containing the list of Operations libraries - through the onComplete callback!
		 */
		getGraphForAuthoring: function(params) {
			if(params.timeout === undefined) {
				params.timeout = 300000;
			}
			var payload = [params.resourceURI];
			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL,"dsbase:getGraphForAuthoring", params.urlParams),{
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
					timeout: params.timeout
				});
			});
			return promise;
		},

		/**
		 * Save a new graph from its signature and its implementation
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *            - graph : the resource holding the graph as json (mandatory)
		 *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {object} an object containing the uri of the new created graph - through the onComplete callback!
		 */
		saveGraphAsJSON: function(params) {
			if(params.timeout === undefined) {
				params.timeout = 300000;
			}
			//params.resource.graph.operationInstances.map(function(op) { delete op.nlsLabel; return op; });
			var payload = [JSON.stringify(params.resource)];
			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL,"dsbase:saveGraphAsJSON", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
					data: dataArray,
					onComplete: function(data, status) {
						data.value = data.value['@id'];
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
					timeout: params.timeout
				});
			});
			return promise;
		},
		/**
		 * Gets the update graph of an updatable resource
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *            - resourceURI : the resource holding the graph (mandatory)
		 *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object containing the list of Operations libraries - through the onComplete callback!
		 */
		getUpdateGraphAsJSON: function(params) {
			if(params.timeout === undefined) {
				params.timeout = 300000;
			}
			var payload = [params.resourceURI];
			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL,"dsbase:getUpdateGraphAsJSON", params.urlParams),{
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
					timeout: params.timeout
				});
			});
			return promise;
		},
		/**
		 * Gets the graph of a composite operation
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *            - compositeVersionURI : the resource holding the graph (mandatory)
		 *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object containing the list of Operations libraries - through the onComplete callback!
		 */
		getCompositeGraphAsJSON: function(params) {
			if(params.timeout === undefined) {
				params.timeout = 300000;
			}
			var payload = [params.compositeVersionURI];
			var dataArray = JSON.stringify(payload);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL,"dsbase:getCompositeGraphAsJSON", params.urlParams),{
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
					timeout: params.timeout
				});
			});
			return promise;
		}
	};
	GraphUIServicesTools.prototype.constructor = GraphUIServicesTools;
	return graphUIServices;
});
