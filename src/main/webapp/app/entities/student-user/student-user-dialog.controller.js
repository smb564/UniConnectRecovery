(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('StudentUserDialogController', StudentUserDialogController);

    StudentUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StudentUser'];

    function StudentUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StudentUser) {
        var vm = this;

        vm.student_user = entity;
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
            if (vm.student_user.id !== null) {
                StudentUser.update(vm.student_user, onSaveSuccess, onSaveError);
            } else {
                StudentUser.save(vm.student_user, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uniConnectApp:student_userUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
