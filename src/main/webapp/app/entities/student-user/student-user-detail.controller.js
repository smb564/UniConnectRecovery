(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('StudentUserDetailController', StudentUserDetailController);

    StudentUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StudentUser'];

    function StudentUserDetailController($scope, $rootScope, $stateParams, previousState, entity, Student_user) {
        var vm = this;

        vm.student_user = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:student_userUpdate', function(event, result) {
            vm.student_user = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
