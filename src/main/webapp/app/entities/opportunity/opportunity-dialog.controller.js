(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('OpportunityDialogController', OpportunityDialogController);

    OpportunityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Opportunity'];

    function OpportunityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Opportunity) {
        var vm = this;

        vm.opportunity = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

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
    }
})();
