(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('AnaliseDetailController', AnaliseDetailController);

    AnaliseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Analise', 'ParecerTecnico', 'Requerimento'];

    function AnaliseDetailController($scope, $rootScope, $stateParams, previousState, entity, Analise, ParecerTecnico, Requerimento) {
        var vm = this;

        vm.analise = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:analiseUpdate', function(event, result) {
            vm.analise = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
