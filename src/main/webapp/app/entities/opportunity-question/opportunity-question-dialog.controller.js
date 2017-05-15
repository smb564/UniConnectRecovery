(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('OpportunityQuestionDialogController', OpportunityQuestionDialogController);

    OpportunityQuestionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OpportunityQuestion'];

    function OpportunityQuestionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OpportunityQuestion) {
        var vm = this;

        vm.opportunityQuestion = entity;
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
            if (vm.opportunityQuestion.id !== null) {
                OpportunityQuestion.update(vm.opportunityQuestion, onSaveSuccess, onSaveError);
            } else {
                OpportunityQuestion.save(vm.opportunityQuestion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uniConnectApp:opportunityQuestionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
