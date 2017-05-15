(function(){
    'use strict';

    angular
        .module('uniConnectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider){
        $stateProvider
            .state('module-pages-system', {
                parent: 'entity',
                url: '/modules',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_COMPANY'],
                    pageTitle: 'Modules'
                },
                views:{
                    'content@':{
                        templateUrl : 'app/Pages/ModulePageSystem/module-page-system-main.html',
                        controller: 'ModuleSystemController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('module-thread', {
                parent: 'module-pages-system',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_COMPANY'],
                    pageTitle: 'Modules'
                },
                views:{
                    'content@':{
                        templateUrl: 'app/Pages/ModulePageSystem/module-thread.html',
                        controller: 'ModuleThreadController',
                        controllerAs: 'vm'
                    }
                },
                resolve:{
                    entity: ['$stateParams', 'ModulePage', function ($stateParams, ModulePage) {
                        return ModulePage.get({id:$stateParams.id}).$promise;
                    }]
                }

            })

            .state('module-comments', {
                parent: 'module-thread',
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
        ;
    }

    }
)();
