(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('opportunity', {
            parent: 'entity',
            url: '/opportunity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Opportunities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity/opportunities.html',
                    controller: 'OpportunityController',
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
        .state('opportunity-detail', {
            parent: 'opportunity',
            url: '/opportunity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Opportunity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity/opportunity-detail.html',
                    controller: 'OpportunityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Opportunity', function($stateParams, Opportunity) {
                    return Opportunity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'opportunity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('opportunity-detail.edit', {
            parent: 'opportunity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity/opportunity-dialog.html',
                    controller: 'OpportunityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Opportunity', function(Opportunity) {
                            return Opportunity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity.new', {
            parent: 'opportunity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity/opportunity-dialog.html',
                    controller: 'OpportunityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                startDate: null,
                                endDate: null,
                                ownerLogin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('opportunity', null, { reload: 'opportunity' });
                }, function() {
                    $state.go('opportunity');
                });
            }]
        })
        .state('opportunity.edit', {
            parent: 'opportunity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity/opportunity-dialog.html',
                    controller: 'OpportunityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Opportunity', function(Opportunity) {
                            return Opportunity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity', null, { reload: 'opportunity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity.delete', {
            parent: 'opportunity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity/opportunity-delete-dialog.html',
                    controller: 'OpportunityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Opportunity', function(Opportunity) {
                            return Opportunity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity', null, { reload: 'opportunity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
