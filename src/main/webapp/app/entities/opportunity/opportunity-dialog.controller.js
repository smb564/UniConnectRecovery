(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('OpportunityDialogController', OpportunityDialogController);

    OpportunityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Opportunity', 'DEPARTMENTS', 'INTEREST_FIELDS'];

    function OpportunityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Opportunity, DEPARTMENTS, INTEREST_FIELDS) {
        var vm = this;

        vm.opportunity = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.interests = INTEREST_FIELDS;

        // Validate start and end dates
        vm.checkStartDate = new Date() > new Date(vm.opportunity.startDate);

        // Set the departments and semesters
        vm.departments = DEPARTMENTS;
        vm.semesters = [];
        for(var i = 1; i < 9 ; i++){
            vm.semesters.push(i);
        }

        // Set selected
        vm.targetDep = vm.departments[0];
        vm.targetSem = vm.semesters[0];

        // To add and remove targets
        vm.addTarget = addTarget;
        vm.removeTarget = removeTarget;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.opportunity.id !== null) {
                Opportunity.update(vm.opportunity, onSaveSuccess, onSaveError);
            } else {
                Opportunity.save(vm.opportunity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uniConnectApp:opportunityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function addTarget(){
            // Check for duplicates and add
            for(var i=0; i < vm.opportunity.targets.length ; i++){
                if (vm.opportunity.targets[i][0]===vm.targetSem && vm.opportunity.targets[i][1]===vm.targetDep){ return ; }
            }
            vm.opportunity.targets.push([vm.targetSem, vm.targetDep]);
        }

        function removeTarget(index){
            vm.opportunity.targets.splice(index, 1);
        }

    }
})();
