/*  Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsOperationBase/basic/LogicOp' , 
	function ( ) {
	/**
    * @module DS/CAAWebAppsOperationBase/basic/LogicOp
    */
	'use strict';

	var exports;

	exports = {
	     /**
         * Checks positivy.
         * @param {integer} x
         * @returns {boolean} true, if x is positive or nul, false otherwise 
         */
		 
		isPositive : function (x) {
			if ( x >= 0 ) {
				return true ; 
			} else {
				return false ;
			}
		}
	} ;
	
	return exports ;
} );
