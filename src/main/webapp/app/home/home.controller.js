(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'User', 'Notification', 'StudentUser'];

    function HomeController ($scope, Principal, LoginService, $state, User, Notification, StudentUser) {
        var vm = this;

        vm.student = false;
        vm.disposeUserNotification = disposeUserNotification;

        // Global notifications
        vm.notifications = [];

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;

                // can be not authenticated
                if (!account){
                    return;
                }

                // Global notifications only applicable for student users
                StudentUser.getForCurrentUser(function(data){
                    // get global notifications
                    vm.student = true;
                    getNotifications(data.currentSemester, data.department);
                });

            });
        }
        function register () {
            $state.go('register');
        }

        function disposeUserNotification($index){
            vm.account.notifications[$index].viewed = true;
            User.update(vm.account);
        }

        function getNotifications(semester, department){
            if(!semester && !department){
                return;
            }

            var filterObj = [semester, department];

            Notification.getTarget(filterObj, function(res){
                vm.notifications = res;
            })
        }
    }
})();
