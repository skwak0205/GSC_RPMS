/*  Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsOperationBase/complex/ComplexOp',[
	'DS/CAAWebAppsOperationMsg/MessageOp',
	'i18n!DS/CAAWebAppsOperationBase/assets/nls/BaseNLS'
] , function ( MessageOp ,NLS) {

	/**
    * @module DS/CAAWebAppsOperationBase/complex/ComplexOp
    */
	
    'use strict' ;

    var exports;

    exports = {
         /**
         * Checks positivity.
         * @param {integer} x
         * @returns {string} A string with OK or Error 
         */
		 
	    check1 : function (x) {
	        if ( x >=0 ) {
	            return ( MessageOp.oK(NLS.get("oK")) );
	        }else {
				return ( MessageOp.error( NLS.get("error") ));
			}
	    } 
	} ;
    
    return exports;
});
