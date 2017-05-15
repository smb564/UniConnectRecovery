(function(){
    'use strict';

    angular
        .module("uniConnectApp")
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider){
        $stateProvider
            .state('forum-system',{
                parent : 'entity',
                url : '/forums',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_COMPANY'],
                    pageTitle: 'Forum'
                },
                views:{
                    'content@':{
                        templateUrl : 'app/Pages/ForumSystem/forum-main.html',
                        controller: 'ForumMainController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state("forum-thread", {
                parent: 'forum-system',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_COMPANY'],
                    pageTitle: 'Forum Posts'
                },
                views:{
                    'content@':{
                        templateUrl: 'app/Pages/ForumSystem/forum-thread.html',
                        controller: 'ForumThreadController',
                        controllerAs: 'vm'
                    }
                },
                resolve:{
                    entity: ['$stateParams', 'Forum', function ($stateParams, Forum) {
                        return Forum.get({id:$stateParams.id}).$promise;
                    }]
                }
            })
            .state('forum-comments', {
                parent: 'forum-thread',
                url : '/{postId}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_COMPANY'],
                    pageTitle: 'Comments'
                },
                views:{
                    'content@':{
                        templateUrl: 'app/Pages/ModulePageSystem/module-comments.html',
                        controller: 'ModuleCommentsController',
                        controllerAs: 'vm'
                    }
                },
                resolve:{
                    entity: ['$stateParams', 'Post', function ($stateParams, Post) {
                        return Post.get({id:$stateParams.postId}).$promise;
                    }]
                }
            })
    }
})();
