(function() {
    'use strict';
    angular
        .module('uniConnectApp')
        .factory('OpportunityQuestion', OpportunityQuestion);

    OpportunityQuestion.$inject = ['$resource', 'DateUtils'];

    function OpportunityQuestion ($resource, DateUtils) {
        var resourceUrl =  'api/opportunity-questions/:id';

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
            'saveModule' : {
                method : "POST",
                url : 'api/opportunity/:id/questions',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            },
            'getModule' : {
                method : 'GET',
                url : 'api/opportunity/:id/questions',
                isArray: true
            },
            'upVote' : {
                method : 'GET',
                url : 'api/questions/:qId/user/:userId/upvote'
            },
            'deleteOpportunity' : {
                method : 'DELETE',
                url : 'api/questions/:qId/opportunity/:oId'
            }
        });
    }
})();
