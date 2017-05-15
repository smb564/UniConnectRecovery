(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('OpportunityQuestionDetailController', OpportunityQuestionDetailController);

    OpportunityQuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OpportunityQuestion'];

    function OpportunityQuestionDetailController($scope, $rootScope, $stateParams, previousState, entity, OpportunityQuestion) {
        var vm = this;

        vm.opportunityQuestion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:opportunityQuestionUpdate', function(event, result) {
            vm.opportunityQuestion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
