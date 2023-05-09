/* Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsOperationBase/basic/UnitaryOp',[
	'DS/CAAWebAppsOperationBase/basic/BinaryOp',
	'DS/CAAWebAppsOperationBase/basic/LogicOp'
] , function (binaryOp, logicOp ) {
	
	/**
    * @module DS/CAAWebAppsOperationBase/basic/UnitaryOp
    */
	
	'use strict';

	var exports;
	
	exports = {
	     /**
         * Sums the N first integers. 
         * @param {integer} N 
         * @returns {integer} If N is positive, 1+2+...+N,  otherwise -1
         */
		nFirst : function (N) {
			if ( N > 0 ) {
				var value=0 ;
				for ( var i= 1 ; i <= N ; i++  ) { value = binaryOp.add(value,i); }
				return value;
			} else {
				return -1 ;
			}
		},
		
		 /**
         * Returns the absolute value. 
         * @param {integer} x 
         * @returns {integer} The absolute value of x
         */
		abs 	: function (x) {
			if ( logicOp.isPositive(x) ) {
				return x ;
			} else {
				return -x;
			}
		}
	} ;
	
	return exports;
});
