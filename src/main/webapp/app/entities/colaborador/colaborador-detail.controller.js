(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ColaboradorDetailController', ColaboradorDetailController);

    ColaboradorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Colaborador', 'Usuario', 'Municipio'];

    function ColaboradorDetailController($scope, $rootScope, $stateParams, previousState, entity, Colaborador, Usuario, Municipio) {
        var vm = this;

        vm.colaborador = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:colaboradorUpdate', function(event, result) {
            vm.colaborador = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
