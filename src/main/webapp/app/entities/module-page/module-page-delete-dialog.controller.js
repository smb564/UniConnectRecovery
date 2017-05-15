(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('ModulePageDeleteController',ModulePageDeleteController);

    ModulePageDeleteController.$inject = ['$uibModalInstance', 'entity', 'ModulePage'];

    function ModulePageDeleteController($uibModalInstance, entity, ModulePage) {
        var vm = this;

        vm.modulePage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ModulePage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
