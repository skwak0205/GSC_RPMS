/*
  Empty place-holder file used to prevent any 404 HTTP error when
  referencing concatenated JS file in debug (-g) mode.

  When using mkmk without debug an EPSSchematicEngine.js file is created
  containing the concatenation of all files in EPSSchematicEngine.mweb;
  this file is returned by mkrun webserver on request of any of the URL
  EPSSchematicEngine/xxx.js where xxx is the name of one of the original file in
  EPSSchematicEngine.mweb/src. To speedup the page loading by preventing useless
  GET requests of EPSSchematicEngine/xxx.js files that would return the same
  concatenated content the recommendation is to include in the page
  EPSSchematicEngine/EPSSchematicEngine.js with an explicit script element. But since the
  concatenated file is not created by "mkmk -g" that would trigger a
  404 error. To prevent this error this empty file is available and
  will be returned in debug mode.
*/
