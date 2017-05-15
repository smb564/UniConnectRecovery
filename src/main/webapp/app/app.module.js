(function() {
    'use strict';

    angular
        .module('uniConnectApp', [
            'ngStorage',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'ui.select',
            'ngSanitize',
            'linkify'
        ])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();

