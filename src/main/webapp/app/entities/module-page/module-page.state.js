(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('module-page', {
            parent: 'entity',
            url: '/module-page?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'ModulePages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/module-page/module-pages.html',
                    controller: 'ModulePageController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('module-page-detail', {
            parent: 'module-page',
            url: '/module-page/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'ModulePage'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/module-page/module-page-detail.html',
                    controller: 'ModulePageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ModulePage', function($stateParams, ModulePage) {
                    return ModulePage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'module-page',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('module-page-detail.edit', {
            parent: 'module-page-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-page/module-page-dialog.html',
                    controller: 'ModulePageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModulePage', function(ModulePage) {
                            return ModulePage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('module-page.new', {
            parent: 'module-page',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-page/module-page-dialog.html',
                    controller: 'ModulePageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                moduleCode: null,
                                id: null,
                                targets: []
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('module-page', null, { reload: 'module-page' });
                }, function() {
                    $state.go('module-page');
                });
            }]
        })
        .state('module-page.edit', {
            parent: 'module-page',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-page/module-page-dialog.html',
                    controller: 'ModulePageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModulePage', function(ModulePage) {
                            return ModulePage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('module-page', null, { reload: 'module-page' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('module-page.delete', {
            parent: 'module-page',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/module-page/module-page-delete-dialog.html',
                    controller: 'ModulePageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ModulePage', function(ModulePage) {
                            return ModulePage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('module-page', null, { reload: 'module-page' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
