(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('ForumDetailController', ForumDetailController);

    ForumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Forum'];

    function ForumDetailController($scope, $rootScope, $stateParams, previousState, entity, Forum) {
        var vm = this;

        vm.forum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:forumUpdate', function(event, result) {
            vm.forum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
