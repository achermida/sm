(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('processo', {
            parent: 'entity',
            url: '/processo?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.processo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/processo/processos.html',
                    controller: 'ProcessoController',
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
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('processo');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('processo-detail', {
            parent: 'entity',
            url: '/processo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.processo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/processo/processo-detail.html',
                    controller: 'ProcessoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('processo');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Processo', function($stateParams, Processo) {
                    return Processo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'processo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('processo-detail.edit', {
            parent: 'processo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/processo/processo-dialog.html',
                    controller: 'ProcessoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Processo', function(Processo) {
                            return Processo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('processo.new', {
            parent: 'processo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/processo/processo-dialog.html',
                    controller: 'ProcessoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                procNumero: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('processo', null, { reload: 'processo' });
                }, function() {
                    $state.go('processo');
                });
            }]
        })
        .state('processo.edit', {
            parent: 'processo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/processo/processo-dialog.html',
                    controller: 'ProcessoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Processo', function(Processo) {
                            return Processo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('processo', null, { reload: 'processo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('processo.delete', {
            parent: 'processo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/processo/processo-delete-dialog.html',
                    controller: 'ProcessoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Processo', function(Processo) {
                            return Processo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('processo', null, { reload: 'processo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
