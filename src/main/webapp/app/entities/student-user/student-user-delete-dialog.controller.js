(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('StudentUserDeleteController',StudentUserDeleteController);

    StudentUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'StudentUser'];

    function StudentUserDeleteController($uibModalInstance, entity, Student_user) {
        var vm = this;

        vm.student_user = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Student_user.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
