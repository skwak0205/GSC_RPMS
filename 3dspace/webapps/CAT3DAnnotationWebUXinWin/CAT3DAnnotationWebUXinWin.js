define("DS/CAT3DAnnotationWebUXinWin/CAT3DAnnotationWebUXinWin",[],function(){}),
/*!  Copyright 2020 Dassault Systemes. All rights reserved. */
define("DS/CAT3DAnnotationWebUXinWin/CATWeb3DAnnotAttrListTable",["UWA/Class","DS/CAT3DAnnotationPanels/CAT3DAnnotationAttrListTable","DS/CAT3DAnnotationRsc/CAT3DAnnotationRscServices"],function(t,n,e){"use strict";return t.extend({init:function(t){let i,o=t.data.attributes,s={discipline:t.data.discipline,disciplineNls:t.data.disciplineNls},a=t&&t.opts?t.opts.csvFilePrefix:"",c=this;function r(t){(t.attrType||t.attrSubtype)&&(t.icon=e.getFeatureIconPath(s.discipline,t.attrSubtype?t.attrSubtype:t.attrType)),t.children&&t.children.length&&t.children.forEach(r)}this.changeActiveState=function(t){for(var n=s.attrPanel.getDisplayedIDPaths(),e=[],i=0;i<n.length;i++){let s={activeState:!1};if(t&&t.length)for(var o=0;o<t.length;o++)if(t[o]===JSON.stringify(n[i])){s={displayFlag:!0,activeState:!0};break}e.push({idPath:n[i],infos:s})}e.length&&s.attrPanel.setAdditionalInformations(e)},this.setCSVFilePrefix=function(t){a=t},this.cleanCustomViewPanel=function(){parent&&parent.closePopUp&&parent.closePopUp(),i=null},this.cleanPanel=function(){c.cleanCustomViewPanel(),s&&s.attrPanel&&s.attrPanel.destroy(),s=null},o&&o.length&&!s.attrPanel&&(s.attrPanel=new n({fromWebInWin:!0}),s.attrPanel.subscribe("onReframeOn",function(t){window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"REFRAME_ON",data:t.discipline}))}),s.attrPanel.subscribe("onAttrActiveStateModified",function(t){if(t&&t.currentSelection){var n=[];t.currentSelection.forEach(t=>{n.push(t.idPath)}),window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"ROW_SELECTED",data:n,discipline:t.discipline}))}}),s.attrPanel.subscribe("onAttrDisplayStateModified",function(t){if(t&&t.currentSelection){var n=[];t.currentSelection.forEach(t=>{n.push(t.idPath)}),window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"ROW_SELECTED_DISPLAY",data:n,discipline:t.discipline}))}}),s.attrPanel.subscribe("onAttributeClicked",function(t){window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"ON_LINK_SELECTED",data:t,discipline:t.discipline}))}),s.attrPanel.subscribe("onSwitchRowSelectionMode",function(t){window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"ON_ROW_SELECTION_CHANGED",state:t.state,discipline:t.discipline}))}),s.attrPanel.subscribe("onSwitchAttributeDisplayMode",function(t){window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"ON_ATTRIBUTE_DISPLAY_CHANGED",state:t.state,discipline:t.discipline}))}),s.attrPanel.subscribe("onSwitchMultiRepDisplayMode",function(t){window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"ON_MULTI_REP_DISPLAY_MODE_CHANGED",state:t.state,discipline:t.discipline}))}),s.attrPanel.subscribe("onSwitchEnableColumnSort",function(t){window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"ON_ENABLE_COLUMN_SORT_CHANGED",state:t.state,discipline:t.discipline}))}),s.attrPanel.subscribe("onExportToCSVRequired",function(t){if(s&&s.attrPanel){var n=s.attrPanel.exportAsCSV(t);if(n){var e=new Blob([n],{type:"text/csv"}),i=window.URL.createObjectURL(e),o=document.createElement("a");o.style.display="none",o.download=a+"_"+s.disciplineNls+".csv",o.href=i,document.body.appendChild(o),o.click(),window.URL.revokeObjectURL(i),document.body.removeChild(o)}}}),s.attrPanel.subscribe("onCustomViewsPanelDisplayed",function(t){parent&&(parent.closePopUp&&parent.closePopUp(),parent.getcollectionView=function(){return t.collectionView},(i=window.open("dsrtv://webapps/CAT3DAnnotationWebUXinWin/CATWeb3DAnnotAttrListCustomView.html","","width=505,height=505,top=150,left=300")).focus(),parent.closePopUp=function(){i&&(i.close(),i=null,parent.document.removeEventListener("mousedown",parent.closePopUp))},parent.document.addEventListener("mousedown",parent.closePopUp))}),o.forEach(t=>{t.elements.forEach(r)}),s.attrPanel.buildPanel({model:o,discipline:s.discipline,allowExport:!0,disciplineNls:s.disciplineNls,representationMode:t&&t.opts?t.opts.representationMode:void 0,multiRepDefaultMode:t&&t.opts?t.opts.multiRepDefaultMode:void 0,rowSelectionCurrentState:t&&t.opts?t.opts.rowSelectionCurrentState:void 0,attributeDisplayCurrentState:t&&t.opts?t.opts.attributeDisplayCurrentState:void 0,columnSortCurrentState:t&&t.opts?t.opts.columnSortCurrentState:void 0}).inject(t.container),window&&window.dscef&&window.dscef.sendString(JSON.stringify({action:"CATWeb3DAnnotAttrListTableInitialized",discipline:s.discipline})))}})});