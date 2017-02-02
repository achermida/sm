(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contato', {
            parent: 'entity',
            url: '/contato',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.contato.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contato/contatoes.html',
                    controller: 'ContatoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contato');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contato-detail', {
            parent: 'entity',
            url: '/contato/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.contato.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contato/contato-detail.html',
                    controller: 'ContatoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contato');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Contato', function($stateParams, Contato) {
                    return Contato.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'contato',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('contato-detail.edit', {
            parent: 'contato-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contato/contato-dialog.html',
                    controller: 'ContatoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contato', function(Contato) {
                            return Contato.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contato.new', {
            parent: 'contato',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contato/contato-dialog.html',
                    controller: 'ContatoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                email: null,
                                telefone: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contato', null, { reload: 'contato' });
                }, function() {
                    $state.go('contato');
                });
            }]
        })
        .state('contato.edit', {
            parent: 'contato',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contato/contato-dialog.html',
                    controller: 'ContatoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contato', function(Contato) {
                            return Contato.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contato', null, { reload: 'contato' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contato.delete', {
            parent: 'contato',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contato/contato-delete-dialog.html',
                    controller: 'ContatoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contato', function(Contato) {
                            return Contato.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contato', null, { reload: 'contato' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
