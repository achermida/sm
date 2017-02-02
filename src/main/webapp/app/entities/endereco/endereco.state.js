(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('endereco', {
            parent: 'entity',
            url: '/endereco',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.endereco.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/endereco/enderecos.html',
                    controller: 'EnderecoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('endereco');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('endereco-detail', {
            parent: 'entity',
            url: '/endereco/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.endereco.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/endereco/endereco-detail.html',
                    controller: 'EnderecoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('endereco');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Endereco', function($stateParams, Endereco) {
                    return Endereco.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'endereco',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('endereco-detail.edit', {
            parent: 'endereco-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/endereco/endereco-dialog.html',
                    controller: 'EnderecoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Endereco', function(Endereco) {
                            return Endereco.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('endereco.new', {
            parent: 'endereco',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/endereco/endereco-dialog.html',
                    controller: 'EnderecoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                logradouro: null,
                                numero: null,
                                bairro: null,
                                complemento: null,
                                cep: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('endereco', null, { reload: 'endereco' });
                }, function() {
                    $state.go('endereco');
                });
            }]
        })
        .state('endereco.edit', {
            parent: 'endereco',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/endereco/endereco-dialog.html',
                    controller: 'EnderecoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Endereco', function(Endereco) {
                            return Endereco.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('endereco', null, { reload: 'endereco' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('endereco.delete', {
            parent: 'endereco',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/endereco/endereco-delete-dialog.html',
                    controller: 'EnderecoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Endereco', function(Endereco) {
                            return Endereco.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('endereco', null, { reload: 'endereco' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
