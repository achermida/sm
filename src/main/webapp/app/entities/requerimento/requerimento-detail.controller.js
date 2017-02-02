(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('RequerimentoDetailController', RequerimentoDetailController);

    RequerimentoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Requerimento', 'Analise', 'Processo'];

    function RequerimentoDetailController($scope, $rootScope, $stateParams, previousState, entity, Requerimento, Analise, Processo) {
        var vm = this;

        vm.requerimento = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:requerimentoUpdate', function(event, result) {
            vm.requerimento = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
