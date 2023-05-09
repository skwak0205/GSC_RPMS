/* Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsOperationBase/basic/BinaryOp', [
	'DS/CAAWebAppsOperationBase/basic/LogicOp'
], function (LogicOp) {
    
	/**
	* Mon Module for BinaryOp.
    * @module DS/CAAWebAppsOperationBase/basic/BinaryOp
    */
	
	'use strict';

    var exports;

	exports = {
	     /**
         * Adds x and y. 
         * @param {integer}  x - The first argument
		 * @param {integer}  y - The second argument
         * @returns {integer}  The sum of x and y 
         */
		add : function (x,y) {
					return x+y ;
				},
				
		 /**
         * Substracts y to x. 
         * @param {integer}  x  The first argument 
		 * @param {integer}  y  The second argument
         * @returns {integer} The substraction of y to x, is y is less than x, otherwise x is substact to y. 
         */
		sub : function (x,y) {
		    if ( LogicOp.isPositive(x-y) )  {
				return x-y ;
				}
			else {
				return y-x ;
				}
		}
	} ;
    
    return exports;
});
