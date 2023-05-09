function executeWidgetCode() {
    require(["DS/TagNavigatorProxy/TagNavigatorProxy"], function(TagNavigatorProxy) {
        var myWidget = {
			 
            dataFull: [
                {
                    firstName: "John",
                    lastName: "Doe",
                    userId: "JD1"
                },
                {
                    firstName: "Jane",
                    lastName: "Doe",
                    userId: "JD2"
                },
                {
                    firstName: "David",
                    lastName: "Doe",
                    userId: "DD1"
                },
                {
                    firstName: "David",
                    lastName: "Black",
                    userId: "DB1"
                },
                {
                    firstName: "David",
                    lastName: "White",
                    userId: "DW1"
                },
                {
                    firstName: "John",
                    lastName: "Black",
                    userId: "JB1"
                }
            ],
			//tag summary
			dataDB: [ 
			   {'object' : 'jhn', 'sixw' : 'ds6w:who/firstName',  'dispValue' : 'John',  'type' : 'string', 'count' : 2 }, 
			   {'object' : 'jne', 'sixw' : 'ds6w:who/firstName',  'dispValue' : 'Jane',  'type' : 'string', 'count' : 1 } , 
			   {'object' : 'dvd', 'sixw' : 'ds6w:who/firstName',  'dispValue' : 'David', 'type' : 'string', 'count' : 3 }
				 
			],
			

            displayData: function(arrData) {
                var tableHTML = "<table><thead><tr><th>First Name</th><th>Last Name</th><th>userId</th></tr></thead><tbody>";

                for (var i = 0; i < arrData.length; i++) {
                    tableHTML =
                        tableHTML + "<tr><td>" + arrData[i].firstName + "</td><td>" + arrData[i].lastName + "</td><td>" + arrData[i].userId + "</td></tr>";
                }

                tableHTML += "</tbody></table>";

                widget.body.innerHTML = tableHTML;
            },


            onLoadWidget: function() {
				myWidget.displayData(myWidget.dataFull);
				//initializing tagger
				myWidget.initTagger();
			},
			
			initTagger: function() {
				/*var options = {					
                   	widgetId: widget.id,
                   	filteringMode: "FilteringOnServer"
                };
				myWidget.taggerProxy = TagNavigatorProxy.createProxy(options);
				myWidget.taggerProxy.addEvent('onFilterChange',myWidget.onFilterChange );
				myWidget.onFilterChange(null);*/
				
			},
			//Function for displaying the data inside widget
			onFilterChange: function(filter) {
			/*	myWidget.taggerProxy.setTags(null,myWidget.dataDB);
				var subjectno=myWidget.subjectFromTags(filter);
					var arrResult=[];
					for (var i = 0; i < myWidget.dataFull.length; i++) {
					var objData = myWidget.dataFull[i];
					if (subjectno.indexOf(objData.firstName) !== -1) {
						arrResult.push(objData);
						myWidget.displayData(arrResult);
					}
                }
				
				if((subjectno.length)===0) {
					//No result and no filters applied : Filters are probably cleared !
                    arrResult = myWidget.dataFull;
					myWidget.displayData(arrResult);
				}*/
			},
			
			//Return subjects
			subjectFromTags : function (filter) {			
				var listSubject = [];
				var subjectDBFiltered = {};
				var subjectDBCopy = myWidget.dataDB;
				var atLeastOnFilter=false;
				if ( filter && filter.allfilters ) {
					var allFilters=filter.allfilters; 
					for (var predicate in allFilters) { 					
						atLeastOnFilter=true;
						listSubject = [];
						var predicateObjects= allFilters[predicate];
						for ( var couple in predicateObjects ) {
							var object=predicateObjects[couple].object;
							for (var subject in subjectDBCopy) {
								var SubjectArray=[];
								SubjectArray.push(subjectDBCopy[subject]);
								var i=0;
								var found= false;								
								while ( ! found &&  ( i< SubjectArray.length )  ) {
									var objectToComp = SubjectArray[i].object;
									var predicateToComp = SubjectArray[i].sixw; 
									if ( ( objectToComp === object ) && ( predicateToComp === predicate  ) ) {
										listSubject.push(SubjectArray[i].dispValue);
										found = true;
									}
									i++;
								}
							}							
						}
					}
				}
				return listSubject;
			},
			onSearchWidget: function(searchQuery) {
                var arrResult = [];
                for (var i = 0; i < myWidget.dataFull.length; i++) {
                    var objData = myWidget.dataFull[i];
                    if (objData.firstName === searchQuery || objData.lastName === searchQuery) {
                        arrResult.push(objData);
                    }
                }
                myWidget.displayData(arrResult);
            },

            onResetSearchWidget: function() {
                myWidget.displayData(myWidget.dataFull);
            }
        };

        widget.addEvent("onLoad", myWidget.onLoadWidget);
        widget.addEvent("onSearch", myWidget.onSearchWidget);
        widget.addEvent("onResetSearch", myWidget.onResetSearchWidget);
    });
}