(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('CompanyUserDetailController', CompanyUserDetailController);

    CompanyUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CompanyUser'];

    function CompanyUserDetailController($scope, $rootScope, $stateParams, previousState, entity, CompanyUser) {
        var vm = this;

        vm.companyUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:companyUserUpdate', function(event, result) {
            vm.companyUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
