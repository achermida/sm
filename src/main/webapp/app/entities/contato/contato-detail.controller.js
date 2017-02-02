(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ContatoDetailController', ContatoDetailController);

    ContatoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contato', 'Usuario'];

    function ContatoDetailController($scope, $rootScope, $stateParams, previousState, entity, Contato, Usuario) {
        var vm = this;

        vm.contato = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:contatoUpdate', function(event, result) {
            vm.contato = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
