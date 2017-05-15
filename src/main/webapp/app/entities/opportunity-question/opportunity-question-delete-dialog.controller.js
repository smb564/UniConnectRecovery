(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('OpportunityQuestionDeleteController',OpportunityQuestionDeleteController);

    OpportunityQuestionDeleteController.$inject = ['$uibModalInstance', 'entity', 'OpportunityQuestion'];

    function OpportunityQuestionDeleteController($uibModalInstance, entity, OpportunityQuestion) {
        var vm = this;

        vm.opportunityQuestion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OpportunityQuestion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
