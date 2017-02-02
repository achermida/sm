(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ColaboradorDeleteController',ColaboradorDeleteController);

    ColaboradorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Colaborador'];

    function ColaboradorDeleteController($uibModalInstance, entity, Colaborador) {
        var vm = this;

        vm.colaborador = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Colaborador.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
