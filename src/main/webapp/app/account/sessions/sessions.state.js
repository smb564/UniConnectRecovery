(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('sessions', {
            parent: 'account',
            url: '/sessions',
            data: {
                authorities: ['ROLE_USER','ROLE_COMPANY'],
                pageTitle: 'Sessions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/sessions/sessions.html',
                    controller: 'SessionsController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
