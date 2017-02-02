(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('MunicipioDetailController', MunicipioDetailController);

    MunicipioDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Municipio', 'Colaborador'];

    function MunicipioDetailController($scope, $rootScope, $stateParams, previousState, entity, Municipio, Colaborador) {
        var vm = this;

        vm.municipio = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:municipioUpdate', function(event, result) {
            vm.municipio = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
