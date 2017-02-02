(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('colaborador', {
            parent: 'entity',
            url: '/colaborador',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.colaborador.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/colaborador/colaboradors.html',
                    controller: 'ColaboradorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('colaborador');
                    $translatePartialLoader.addPart('cargoEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('colaborador-detail', {
            parent: 'entity',
            url: '/colaborador/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.colaborador.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/colaborador/colaborador-detail.html',
                    controller: 'ColaboradorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('colaborador');
                    $translatePartialLoader.addPart('cargoEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Colaborador', function($stateParams, Colaborador) {
                    return Colaborador.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'colaborador',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('colaborador-detail.edit', {
            parent: 'colaborador-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/colaborador/colaborador-dialog.html',
                    controller: 'ColaboradorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Colaborador', function(Colaborador) {
                            return Colaborador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('colaborador.new', {
            parent: 'colaborador',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/colaborador/colaborador-dialog.html',
                    controller: 'ColaboradorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cargo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('colaborador', null, { reload: 'colaborador' });
                }, function() {
                    $state.go('colaborador');
                });
            }]
        })
        .state('colaborador.edit', {
            parent: 'colaborador',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/colaborador/colaborador-dialog.html',
                    controller: 'ColaboradorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Colaborador', function(Colaborador) {
                            return Colaborador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('colaborador', null, { reload: 'colaborador' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('colaborador.delete', {
            parent: 'colaborador',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/colaborador/colaborador-delete-dialog.html',
                    controller: 'ColaboradorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Colaborador', function(Colaborador) {
                            return Colaborador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('colaborador', null, { reload: 'colaborador' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
