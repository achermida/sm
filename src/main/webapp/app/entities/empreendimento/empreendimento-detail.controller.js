(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('EmpreendimentoDetailController', EmpreendimentoDetailController);

    EmpreendimentoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Empreendimento', 'Endereco', 'Usuario', 'Processo', 'Licenca'];

    function EmpreendimentoDetailController($scope, $rootScope, $stateParams, previousState, entity, Empreendimento, Endereco, Usuario, Processo, Licenca) {
        var vm = this;

        vm.empreendimento = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:empreendimentoUpdate', function(event, result) {
            vm.empreendimento = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
