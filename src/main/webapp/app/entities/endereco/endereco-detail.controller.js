(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('EnderecoDetailController', EnderecoDetailController);

    EnderecoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Endereco', 'Municipio', 'Empreendimento'];

    function EnderecoDetailController($scope, $rootScope, $stateParams, previousState, entity, Endereco, Municipio, Empreendimento) {
        var vm = this;

        vm.endereco = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smApp:enderecoUpdate', function(event, result) {
            vm.endereco = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
