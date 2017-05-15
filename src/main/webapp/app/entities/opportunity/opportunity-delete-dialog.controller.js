(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('OpportunityDeleteController',OpportunityDeleteController);

    OpportunityDeleteController.$inject = ['$uibModalInstance', 'entity', 'Opportunity'];

    function OpportunityDeleteController($uibModalInstance, entity, Opportunity) {
        var vm = this;

        vm.opportunity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Opportunity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
