(function() {
    'use strict';
    angular
        .module('uniConnectApp')
        .factory('Post', Post);

    Post.$inject = ['$resource', 'DateUtils'];

    function Post ($resource, DateUtils) {
        var resourceUrl =  'api/posts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertLocalDateFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            },
            'getModule': {
                method: "GET",
                isArray: true,
                url: 'api/posts/module/:module',
                transformResponse: function (data) {
                    if (data){
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertLocalDateFromServer(data.date);
                    }
                    return data;
                }
            },
            'saveModule': {
                method : 'POST',
                url : 'api/posts/module/:module',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            },
            'upVote' : {
                url : 'api/posts/:postId/:userLogin',
                method : "GET"
            },
            'deleteForModule' : {
                url : 'api/posts/:postId/modules/:moduleId',
                method : "DELETE"
            },
            'deleteForForum' : {
                url : 'api/posts/:postId/forum/:forumId',
                method : "DELETE"
            },
            'getForum' : {
                method : 'GET',
                url : 'api/forums/:id/posts',
                isArray : true,
                transformResponse: function (data) {
                    if (data){
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertLocalDateFromServer(data.date);
                    }
                    return data;
                }
            },
            'saveForum' : {
                method : 'POST',
                url : 'api/forums/:id/posts',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
