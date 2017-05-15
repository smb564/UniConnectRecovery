(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('OpportunityDetailController', OpportunityDetailController);

    OpportunityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Opportunity'];

    function OpportunityDetailController($scope, $rootScope, $stateParams, previousState, entity, Opportunity) {
        var vm = this;

        vm.opportunity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:opportunityUpdate', function(event, result) {
            vm.opportunity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
