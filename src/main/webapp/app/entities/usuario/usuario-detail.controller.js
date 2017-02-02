(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('UsuarioDetailController', UsuarioDetailController);

    UsuarioDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Usuario', 'Contato', 'User', 'Empreendimento'];

    function UsuarioDetailController($scope, $rootScope, $stateParams, previousState, entity, Usuario, Contato, User, Empreendimento) {
        var vm = this;

        vm.usuario = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:usuarioUpdate', function(event, result) {
            vm.usuario = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
