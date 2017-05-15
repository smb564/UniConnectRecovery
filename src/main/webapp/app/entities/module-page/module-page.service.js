(function() {
    'use strict';
    angular
        .module('uniConnectApp')
        .factory('ModulePage', ModulePage);

    ModulePage.$inject = ['$resource'];

    function ModulePage ($resource) {
        var resourceUrl =  'api/module-pages/:id';

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
            'delete': {method : 'DELETE'}
        });
    }
})();
