(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('parecer-itens', {
            parent: 'entity',
            url: '/parecer-itens',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.parecerItens.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/parecer-itens/parecer-itens.html',
                    controller: 'ParecerItensController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('parecerItens');
                    $translatePartialLoader.addPart('tipoParecerItensEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('parecer-itens-detail', {
            parent: 'entity',
            url: '/parecer-itens/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.parecerItens.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/parecer-itens/parecer-itens-detail.html',
                    controller: 'ParecerItensDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('parecerItens');
                    $translatePartialLoader.addPart('tipoParecerItensEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ParecerItens', function($stateParams, ParecerItens) {
                    return ParecerItens.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'parecer-itens',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('parecer-itens-detail.edit', {
            parent: 'parecer-itens-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parecer-itens/parecer-itens-dialog.html',
                    controller: 'ParecerItensDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ParecerItens', function(ParecerItens) {
                            return ParecerItens.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('parecer-itens.new', {
            parent: 'parecer-itens',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parecer-itens/parecer-itens-dialog.html',
                    controller: 'ParecerItensDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tipo: null,
                                paritDescricao: null,
                                paritSequencia: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('parecer-itens', null, { reload: 'parecer-itens' });
                }, function() {
                    $state.go('parecer-itens');
                });
            }]
        })
        .state('parecer-itens.edit', {
            parent: 'parecer-itens',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parecer-itens/parecer-itens-dialog.html',
                    controller: 'ParecerItensDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ParecerItens', function(ParecerItens) {
                            return ParecerItens.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('parecer-itens', null, { reload: 'parecer-itens' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('parecer-itens.delete', {
            parent: 'parecer-itens',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parecer-itens/parecer-itens-delete-dialog.html',
                    controller: 'ParecerItensDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ParecerItens', function(ParecerItens) {
                            return ParecerItens.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('parecer-itens', null, { reload: 'parecer-itens' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
