/*! -- Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsOperationMsg/MessageOp', 
	function () {
	
	/**
    * @module DS/CAAWebAppsOperationMsg/MessageOp
    */
	
	'use strict';

    var exports;

	exports = {
	
	    /**
         * OK message.
         * @param {string} text
         * @returns {string} The returned text is YES + input text
         */
		oK : function (text) {
				return "YES, " + text ;
		} ,
		/**
         * Error message.
         * @param {string} text
         * @return {string} The returned text is NO + input text
         */
		error : function (text) {
				return "NO, " + text ;
		} 
	} ; 
    
    return exports;
});
