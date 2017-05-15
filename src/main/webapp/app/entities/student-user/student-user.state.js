(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('student-user', {
            parent: 'entity',
            url: '/student-user?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Student_users'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/student-user/student-users.html',
                    controller: 'StudentUserController',
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
                }]
            }
        })
        .state('student-user-detail', {
            parent: 'student-user',
            url: '/student-user/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'StudentUser'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/student-user/student-user-detail.html',
                    controller: 'StudentUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'StudentUser', function($stateParams, Student_user) {
                    return Student_user.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'student-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('student-user-detail.edit', {
            parent: 'student-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-user/student-user-dialog.html',
                    controller: 'StudentUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StudentUser', function(Student_user) {
                            return Student_user.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('student-user.new', {
            parent: 'student-user',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-user/student-user-dialog.html',
                    controller: 'StudentUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                graduate: null,
                                graduate_year: null,
                                current_semester: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('student-user', null, { reload: 'student-user' });
                }, function() {
                    $state.go('student-user');
                });
            }]
        })
        .state('student-user.edit', {
            parent: 'student-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-user/student-user-dialog.html',
                    controller: 'StudentUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StudentUser', function(Student_user) {
                            return Student_user.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('student-user', null, { reload: 'student-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('student-user.delete', {
            parent: 'student-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-user/student-user-delete-dialog.html',
                    controller: 'StudentUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StudentUser', function(Student_user) {
                            return Student_user.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('student-user', null, { reload: 'student-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
