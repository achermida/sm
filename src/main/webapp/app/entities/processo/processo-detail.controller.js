(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ProcessoDetailController', ProcessoDetailController);

    ProcessoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Processo', 'Requerimento', 'Empreendimento'];

    function ProcessoDetailController($scope, $rootScope, $stateParams, previousState, entity, Processo, Requerimento, Empreendimento) {
        var vm = this;

        vm.processo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:processoUpdate', function(event, result) {
            vm.processo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
