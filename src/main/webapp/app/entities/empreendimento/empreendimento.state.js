(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('empreendimento', {
            parent: 'entity',
            url: '/empreendimento',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.empreendimento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/empreendimento/empreendimentos.html',
                    controller: 'EmpreendimentoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('empreendimento');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('empreendimento-detail', {
            parent: 'entity',
            url: '/empreendimento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.empreendimento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/empreendimento/empreendimento-detail.html',
                    controller: 'EmpreendimentoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('empreendimento');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Empreendimento', function($stateParams, Empreendimento) {
                    return Empreendimento.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'empreendimento',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('empreendimento-detail.edit', {
            parent: 'empreendimento-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empreendimento/empreendimento-dialog.html',
                    controller: 'EmpreendimentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Empreendimento', function(Empreendimento) {
                            return Empreendimento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('empreendimento.new', {
            parent: 'empreendimento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empreendimento/empreendimento-dialog.html',
                    controller: 'EmpreendimentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cnpj: null,
                                razaoSocial: null,
                                x: null,
                                y: null,
                                dtCadastro: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('empreendimento', null, { reload: 'empreendimento' });
                }, function() {
                    $state.go('empreendimento');
                });
            }]
        })
        .state('empreendimento.edit', {
            parent: 'empreendimento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empreendimento/empreendimento-dialog.html',
                    controller: 'EmpreendimentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Empreendimento', function(Empreendimento) {
                            return Empreendimento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('empreendimento', null, { reload: 'empreendimento' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('empreendimento.delete', {
            parent: 'empreendimento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/empreendimento/empreendimento-delete-dialog.html',
                    controller: 'EmpreendimentoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Empreendimento', function(Empreendimento) {
                            return Empreendimento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('empreendimento', null, { reload: 'empreendimento' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
