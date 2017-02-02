(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ParecerTecnicoDetailController', ParecerTecnicoDetailController);

    ParecerTecnicoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ParecerTecnico', 'Analise', 'ParecerItens'];

    function ParecerTecnicoDetailController($scope, $rootScope, $stateParams, previousState, entity, ParecerTecnico, Analise, ParecerItens) {
        var vm = this;

        vm.parecerTecnico = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:parecerTecnicoUpdate', function(event, result) {
            vm.parecerTecnico = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
