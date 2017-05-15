(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('ModulePageDetailController', ModulePageDetailController);

    ModulePageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ModulePage'];

    function ModulePageDetailController($scope, $rootScope, $stateParams, previousState, entity, ModulePage) {
        var vm = this;

        vm.modulePage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:modulePageUpdate', function(event, result) {
            vm.modulePage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
