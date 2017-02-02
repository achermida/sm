(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ParecerItensDetailController', ParecerItensDetailController);

    ParecerItensDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ParecerItens', 'ParecerTecnico'];

    function ParecerItensDetailController($scope, $rootScope, $stateParams, previousState, entity, ParecerItens, ParecerTecnico) {
        var vm = this;

        vm.parecerItens = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:parecerItensUpdate', function(event, result) {
            vm.parecerItens = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
