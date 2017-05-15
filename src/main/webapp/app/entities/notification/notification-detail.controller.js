(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('NotificationDetailController', NotificationDetailController);

    NotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Notification'];

    function NotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Notification) {
        var vm = this;

        vm.notification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uniConnectApp:notificationUpdate', function(event, result) {
            vm.notification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
