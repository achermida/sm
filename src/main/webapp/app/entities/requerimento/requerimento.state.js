(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('requerimento', {
            parent: 'entity',
            url: '/requerimento?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.requerimento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/requerimento/requerimentos.html',
                    controller: 'RequerimentoController',
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
                    $translatePartialLoader.addPart('requerimento');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    $translatePartialLoader.addPart('reqFaseEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('requerimento-detail', {
            parent: 'entity',
            url: '/requerimento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.requerimento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/requerimento/requerimento-detail.html',
                    controller: 'RequerimentoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('requerimento');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    $translatePartialLoader.addPart('reqFaseEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Requerimento', function($stateParams, Requerimento) {
                    return Requerimento.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'requerimento',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('requerimento-detail.edit', {
            parent: 'requerimento-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/requerimento/requerimento-dialog.html',
                    controller: 'RequerimentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Requerimento', function(Requerimento) {
                            return Requerimento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('requerimento.new', {
            parent: 'requerimento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/requerimento/requerimento-dialog.html',
                    controller: 'RequerimentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dtCadastro: null,
                                status: null,
                                fase: null,
                                obs: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('requerimento', null, { reload: 'requerimento' });
                }, function() {
                    $state.go('requerimento');
                });
            }]
        })
        .state('requerimento.edit', {
            parent: 'requerimento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/requerimento/requerimento-dialog.html',
                    controller: 'RequerimentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Requerimento', function(Requerimento) {
                            return Requerimento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('requerimento', null, { reload: 'requerimento' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('requerimento.delete', {
            parent: 'requerimento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/requerimento/requerimento-delete-dialog.html',
                    controller: 'RequerimentoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Requerimento', function(Requerimento) {
                            return Requerimento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('requerimento', null, { reload: 'requerimento' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
