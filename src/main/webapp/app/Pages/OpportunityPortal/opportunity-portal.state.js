(function(){
    'use strict';

    angular
        .module("uniConnectApp")
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider){
        $stateProvider
            .state('opportunity-portal',{
                parent : 'entity',
                url : '/oportal',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_COMPANY'],
                    pageTitle: 'Opportunity Portal'
                },
                views:{
                    'content@':{
                        templateUrl : 'app/Pages/OpportunityPortal/opportunity-portal.html',
                        controller: 'OpportunityPortalController',
                        controllerAs: 'vm'
                    }
                }
            })
    }

})();
