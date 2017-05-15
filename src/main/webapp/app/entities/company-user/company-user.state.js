(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-user', {
            parent: 'entity',
            url: '/company-user?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'CompanyUsers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-user/company-users.html',
                    controller: 'CompanyUserController',
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
        .state('company-user-detail', {
            parent: 'company-user',
            url: '/company-user/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'CompanyUser'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-user/company-user-detail.html',
                    controller: 'CompanyUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CompanyUser', function($stateParams, CompanyUser) {
                    return CompanyUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-user-detail.edit', {
            parent: 'company-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-user/company-user-dialog.html',
                    controller: 'CompanyUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyUser', function(CompanyUser) {
                            return CompanyUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-user.new', {
            parent: 'company-user',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-user/company-user-dialog.html',
                    controller: 'CompanyUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                company: null,
                                type: null,
                                userLogin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-user', null, { reload: 'company-user' });
                }, function() {
                    $state.go('company-user');
                });
            }]
        })
        .state('company-user.edit', {
            parent: 'company-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-user/company-user-dialog.html',
                    controller: 'CompanyUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyUser', function(CompanyUser) {
                            return CompanyUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-user', null, { reload: 'company-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-user.delete', {
            parent: 'company-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-user/company-user-delete-dialog.html',
                    controller: 'CompanyUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyUser', function(CompanyUser) {
                            return CompanyUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-user', null, { reload: 'company-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
