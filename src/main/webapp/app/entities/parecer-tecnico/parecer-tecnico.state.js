(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('parecer-tecnico', {
            parent: 'entity',
            url: '/parecer-tecnico',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.parecerTecnico.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/parecer-tecnico/parecer-tecnicos.html',
                    controller: 'ParecerTecnicoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('parecerTecnico');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('parecer-tecnico-detail', {
            parent: 'entity',
            url: '/parecer-tecnico/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.parecerTecnico.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/parecer-tecnico/parecer-tecnico-detail.html',
                    controller: 'ParecerTecnicoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('parecerTecnico');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ParecerTecnico', function($stateParams, ParecerTecnico) {
                    return ParecerTecnico.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'parecer-tecnico',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('parecer-tecnico-detail.edit', {
            parent: 'parecer-tecnico-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parecer-tecnico/parecer-tecnico-dialog.html',
                    controller: 'ParecerTecnicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ParecerTecnico', function(ParecerTecnico) {
                            return ParecerTecnico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('parecer-tecnico.new', {
            parent: 'parecer-tecnico',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parecer-tecnico/parecer-tecnico-dialog.html',
                    controller: 'ParecerTecnicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                parecNumero: null,
                                parecObjetivo: null,
                                parecAtendimentoIn: null,
                                parecObservacao: null,
                                parecDataInicio: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('parecer-tecnico', null, { reload: 'parecer-tecnico' });
                }, function() {
                    $state.go('parecer-tecnico');
                });
            }]
        })
        .state('parecer-tecnico.edit', {
            parent: 'parecer-tecnico',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parecer-tecnico/parecer-tecnico-dialog.html',
                    controller: 'ParecerTecnicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ParecerTecnico', function(ParecerTecnico) {
                            return ParecerTecnico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('parecer-tecnico', null, { reload: 'parecer-tecnico' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('parecer-tecnico.delete', {
            parent: 'parecer-tecnico',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parecer-tecnico/parecer-tecnico-delete-dialog.html',
                    controller: 'ParecerTecnicoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ParecerTecnico', function(ParecerTecnico) {
                            return ParecerTecnico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('parecer-tecnico', null, { reload: 'parecer-tecnico' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
