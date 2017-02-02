(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('LicencaDetailController', LicencaDetailController);

    LicencaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Licenca', 'Empreendimento'];

    function LicencaDetailController($scope, $rootScope, $stateParams, previousState, entity, Licenca, Empreendimento) {
        var vm = this;

        vm.licenca = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:licencaUpdate', function(event, result) {
            vm.licenca = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
