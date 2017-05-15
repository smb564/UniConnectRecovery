(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('PostDetailController', PostDetailController);

    PostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Post'];

    function PostDetailController($scope, $rootScope, $stateParams, previousState, entity, Post) {
        var vm = this;

        vm.post = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:postUpdate', function(event, result) {
            vm.post = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
