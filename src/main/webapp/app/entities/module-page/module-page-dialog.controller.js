(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('ModulePageDialogController', ModulePageDialogController);

    ModulePageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ModulePage', 'DEPARTMENTS', 'Notification'];

    function ModulePageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ModulePage, DEPARTMENTS, Notification) {
        var vm = this;

        vm.modulePage = entity;
        vm.clear = clear;
        vm.save = save;
        vm.addTarget = addTarget;
        vm.removeTarget = removeTarget;
        vm.departments = DEPARTMENTS;
        vm.semesters = [];
        //vm.modulePage.targets = [];

        for(var i=1; i <9; i++){
            vm.semesters.push(i);
        }

        // Make default selections
        vm.targetDep = vm.departments[0];
        vm.targetSem = vm.semesters[0];

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function addTarget(){
            // Check for duplicates and add
            for(var i=0; i < vm.modulePage.targets.length ; i++){
                if (vm.modulePage.targets[i][0]==vm.targetSem && vm.modulePage.targets[i][1]==vm.targetDep){ return ; }
            }
            vm.modulePage.targets.push([vm.targetSem, vm.targetDep]);
        }

        function removeTarget(index){
            vm.modulePage.targets.splice(index, 1);
        }
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.modulePage.id !== null) {
                ModulePage.update(vm.modulePage, onSaveSuccess, onSaveError);
            } else {
                ModulePage.save(vm.modulePage, sendNotification, onSaveError);
            }
        }

        function sendNotification(result){
            // Add notifications
            for(var i = 0; i < vm.modulePage.targets.length ; i++){
                Notification.save(
                    {
                        "semester" : vm.modulePage.targets[i][0],
                        "department" : vm.modulePage.targets[i][1],
                        "notification" : "A new module named " + result.title + " added. Go to <a href='http://localhost:8080/#/modules/" + result.id + "'>this</a>"
                    }
                )
            }

            onSaveSuccess(result);
        }

        function onSaveSuccess (result) {
            $scope.$emit('uniConnectApp:modulePageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
