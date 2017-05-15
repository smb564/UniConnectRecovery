(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('opportunity-question', {
            parent: 'entity',
            url: '/opportunity-question?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OpportunityQuestions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity-question/opportunity-questions.html',
                    controller: 'OpportunityQuestionController',
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
        .state('opportunity-question-detail', {
            parent: 'opportunity-question',
            url: '/opportunity-question/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OpportunityQuestion'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/opportunity-question/opportunity-question-detail.html',
                    controller: 'OpportunityQuestionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OpportunityQuestion', function($stateParams, OpportunityQuestion) {
                    return OpportunityQuestion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'opportunity-question',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('opportunity-question-detail.edit', {
            parent: 'opportunity-question-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-question/opportunity-question-dialog.html',
                    controller: 'OpportunityQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OpportunityQuestion', function(OpportunityQuestion) {
                            return OpportunityQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity-question.new', {
            parent: 'opportunity-question',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-question/opportunity-question-dialog.html',
                    controller: 'OpportunityQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                date: null,
                                votes: null,
                                ownerLogin: null,
                                answer: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('opportunity-question', null, { reload: 'opportunity-question' });
                }, function() {
                    $state.go('opportunity-question');
                });
            }]
        })
        .state('opportunity-question.edit', {
            parent: 'opportunity-question',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-question/opportunity-question-dialog.html',
                    controller: 'OpportunityQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OpportunityQuestion', function(OpportunityQuestion) {
                            return OpportunityQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity-question', null, { reload: 'opportunity-question' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('opportunity-question.delete', {
            parent: 'opportunity-question',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/opportunity-question/opportunity-question-delete-dialog.html',
                    controller: 'OpportunityQuestionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OpportunityQuestion', function(OpportunityQuestion) {
                            return OpportunityQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('opportunity-question', null, { reload: 'opportunity-question' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
