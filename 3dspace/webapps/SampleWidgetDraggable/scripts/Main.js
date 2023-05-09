function executeWidgetCode() {
    require(['DS/DataDragAndDrop/DataDragAndDrop'], function(DataDragAndDrop) {
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
                    firstName: "Walter",
                    lastName: "White",
                    userId: "WW1"
                }
            ],

            displayData: function(arrData) {
                var tableHTML = "<table><thead><tr><th>First Name</th><th>Last Name</th><th>userId</th></tr></thead><tbody>";

                for (var i = 0; i < arrData.length; i++) {
                    tableHTML =
                        tableHTML + "<tr class='dragEligible'><td>" + arrData[i].firstName + "</td><td>" 
                        + arrData[i].lastName + "</td><td>" + arrData[i].userId + "</td></tr>";
                }

                tableHTML += "</tbody></table>";

                widget.body.innerHTML = tableHTML;
            },
			/* makeDraggable: function() {
                 // Code for drag functionality
				var allElems  = widget.body.querySelectorAll(".dragEligible");
				for(var i=0;i<allElems.length;i++) {
					
					DataDragAndDrop.draggable(allElems[i] , {
						data: allElems[i].id,
						start: function(){
							console.log("Start drag");
						},
						stop: function(){
							console.log("Stop drag");
						}
						
					});
				}
				}, */

            onLoadWidget: function() {
                myWidget.displayData(myWidget.dataFull);
				//myWidget.makeDraggable(); // Code for drag functionality				
            }
        };

        widget.addEvent("onLoad", myWidget.onLoadWidget);
    });
}
