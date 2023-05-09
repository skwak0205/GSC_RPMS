/*! Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsUserPreference/CAAWebAppsUPMngt',  function () {
    	
	'use strict';

    var exports;

	exports = {
	     
		displayUP : function ( txtUp, listUp, booleanUp, rangeUp ,roleUp ) {
			var ligne = '' ;
			 
			ligne = ligne + 'User Name: ' + txtUp + '<br>';
			
			ligne = ligne + 'Category: ' + listUp + '<br>';
			
			ligne = ligne + 'Open File: ' + booleanUp  + '<br>';
			
			ligne = ligne + 'Max item to display: ' + rangeUp  + '<br>';  
			
			ligne = ligne + 'Role: ' + roleUp  + '<br>';  
			
			return ligne ;
		}
		 
	} ;
    
    return exports;
});
