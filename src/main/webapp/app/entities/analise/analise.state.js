(function() {
    'use strict';

    angular
        .module('smApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('analise', {
            parent: 'entity',
            url: '/analise',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.analise.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analise/analises.html',
                    controller: 'AnaliseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('analise');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('analise-detail', {
            parent: 'entity',
            url: '/analise/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smApp.analise.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analise/analise-detail.html',
                    controller: 'AnaliseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('analise');
                    $translatePartialLoader.addPart('statusGeralEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Analise', function($stateParams, Analise) {
                    return Analise.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'analise',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('analise-detail.edit', {
            parent: 'analise-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analise/analise-dialog.html',
                    controller: 'AnaliseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Analise', function(Analise) {
                            return Analise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('analise.new', {
            parent: 'analise',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analise/analise-dialog.html',
                    controller: 'AnaliseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('analise', null, { reload: 'analise' });
                }, function() {
                    $state.go('analise');
                });
            }]
        })
        .state('analise.edit', {
            parent: 'analise',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analise/analise-dialog.html',
                    controller: 'AnaliseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Analise', function(Analise) {
                            return Analise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('analise', null, { reload: 'analise' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('analise.delete', {
            parent: 'analise',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analise/analise-delete-dialog.html',
                    controller: 'AnaliseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Analise', function(Analise) {
                            return Analise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('analise', null, { reload: 'analise' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
