(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('CommentDetailController', CommentDetailController);

    CommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Comment'];

    function CommentDetailController($scope, $rootScope, $stateParams, previousState, entity, Comment) {
        var vm = this;

        vm.comment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:commentUpdate', function(event, result) {
            vm.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
