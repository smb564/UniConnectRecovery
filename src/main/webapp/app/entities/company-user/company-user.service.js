(function() {
    'use strict';
    angular
        .module('uniConnectApp')
        .factory('CompanyUser', CompanyUser);

    CompanyUser.$inject = ['$resource'];

    function CompanyUser ($resource) {
        var resourceUrl =  'api/company-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getForCurrentUser': {
                method : 'GET',
                url : 'api/company-users/me',
                transformResponse : function(data){
                    if (data){
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
        });
    }
})();
