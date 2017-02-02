(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('licenca', {
            parent: 'entity',
            url: '/licenca?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.licenca.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/licenca/licencas.html',
                    controller: 'LicencaController',
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
                    $translatePartialLoader.addPart('licenca');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    $translatePartialLoader.addPart('tipoLicencaEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('licenca-detail', {
            parent: 'entity',
            url: '/licenca/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.licenca.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/licenca/licenca-detail.html',
                    controller: 'LicencaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('licenca');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    $translatePartialLoader.addPart('tipoLicencaEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Licenca', function($stateParams, Licenca) {
                    return Licenca.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'licenca',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('licenca-detail.edit', {
            parent: 'licenca-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/licenca/licenca-dialog.html',
                    controller: 'LicencaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Licenca', function(Licenca) {
                            return Licenca.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('licenca.new', {
            parent: 'licenca',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/licenca/licenca-dialog.html',
                    controller: 'LicencaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                licenNumero: null,
                                licenCondicoesValidade: null,
                                licenValidade: null,
                                licenCaracteristica: null,
                                licenDataEmissao: null,
                                licenDataEntrega: null,
                                tipo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('licenca', null, { reload: 'licenca' });
                }, function() {
                    $state.go('licenca');
                });
            }]
        })
        .state('licenca.edit', {
            parent: 'licenca',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/licenca/licenca-dialog.html',
                    controller: 'LicencaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Licenca', function(Licenca) {
                            return Licenca.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('licenca', null, { reload: 'licenca' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('licenca.delete', {
            parent: 'licenca',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/licenca/licenca-delete-dialog.html',
                    controller: 'LicencaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Licenca', function(Licenca) {
                            return Licenca.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('licenca', null, { reload: 'licenca' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
