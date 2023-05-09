/*!  Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
    This module exports: 
	   -getListPlatformId        : a method for a simulation
	   -getPlatformIdPreference  : returns a json structure for a user preference (platformid as list)
	   -getSecondChoice          : returns a json structure for a user preference (secondchoice as a text)
	   -getSecondChoiceHidden    : returns a json structure for a user preference (secondchoice as hidden)
	   
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsDynamicPreference/CAAWebAppsUPDynamicMngt',  
       ['i18n!DS/CAAWebAppsDynamicPreference/assets/CAAWebAppsUPDyn'],
    function (CAAWebAppsUPDyn) {
    	
	'use strict';

    var exports;

	exports = {
	     
		//
		// returns the list of platform id (simulation)
		//
		getListPlatformId : function (  ) {
			var listId = [ 'ID1'  , 'ID2', 'ID3' ] ;
			 
			return listId ;
		},
		
		//
		// returns the platformid preference fill in with platform id list
		//
		getPlatformIdPreference  : function (  ) {
		    
			var structure = {
				name: "platformid",
				type: "list",
				label: CAAWebAppsUPDyn.get("PlatformId_lbl"),
				options: [],
				onchange: "onPlatformIdChange"
			} ;
			
			var list = exports.getListPlatformId();
			for ( var i=0 ; i < list.length ; i++ ) {
			    // {value,label} both mandatory
				structure.options.push( { value : list[i] , label : list[i] } );
			}
			
			return structure ; 
		},
		
		//
		// returns the secondchoice preference as a text preference
		//
		getSecondChoice  : function (  ) {
		    
			var structure = {
				name: "secondchoice",
				type: "text",
				label: CAAWebAppsUPDyn.get("SecondChoice_lbl"), 
				defaultValue: 'Initial Value' 
			} ;			
			return structure ; 
		},
		
		//
		// returns the secondchoice preference as hidden preference
		//
		getSecondChoiceHidden  : function (  ) {
		    
			var structure = {
				name: "secondchoice",
				defaultValue: 'Initial Value',
				type: "hidden",			
			} ;			
			return structure ; 
		}
	} ;
    
    return exports;
});


