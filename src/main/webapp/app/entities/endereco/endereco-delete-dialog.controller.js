(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('EnderecoDeleteController',EnderecoDeleteController);

    EnderecoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Endereco'];

    function EnderecoDeleteController($uibModalInstance, entity, Endereco) {
        var vm = this;

        vm.endereco = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Endereco.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
