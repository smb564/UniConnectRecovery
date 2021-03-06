(function () {
    'use strict';

    angular
        .module('uniConnectApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'},
            'addNotification' : {
                method : "POST",
                url : 'api/users/:login/notification',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getNotifications' : {
                method : 'GET',
                isArray : true,
                url : 'api/users/:login/notification'
            }
        });

        return service;
    }
})();
