(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('CompanyUserDialogController', CompanyUserDialogController);

    CompanyUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyUser'];

    function CompanyUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyUser) {
        var vm = this;

        vm.companyUser = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyUser.id !== null) {
                CompanyUser.update(vm.companyUser, onSaveSuccess, onSaveError);
            } else {
                CompanyUser.save(vm.companyUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uniConnectApp:companyUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
