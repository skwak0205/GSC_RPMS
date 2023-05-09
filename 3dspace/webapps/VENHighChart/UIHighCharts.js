/*! Copyright 2014 Dassault Systèmes */
/* Configure AMD Loader to load non AMD lib
 * See requirejs shim config for references :
 *   http://requirejs.org/docs/api.html#config-shim
 */
/* global require, define, Highcharts */

// if Highcharts has already been exported to global scope (preloaded) and if has same version, define it right away
if (typeof Highcharts !== 'undefined' && Highcharts.version === '8.2.2') {
    define('highcharts/highcharts', function () { 'use strict'; return Highcharts; });
} else {

    // Avoid leaking variables
    (function () {
        'use strict';

        // Remove any query strings
        var path = require.toUrl('DS/VENHighChart/highcharts.8.2.2/highcharts');
        if (path.indexOf('?') > -1) {
            path = path.substring(0, path.indexOf('?'));
        }

        require.config({
            paths: {
                'highcharts/highcharts': path
            },
            shim: { 'highcharts/highcharts': { exports: 'Highcharts' } }
        });
    })();
}

define('DS/VENHighChart/UIHighCharts',
    [
        'highcharts/highcharts'
    ],
    function (Highcharts) {
        'use strict';
        return Highcharts;
    }
);
