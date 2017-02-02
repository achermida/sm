(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('RequerimentoDeleteController',RequerimentoDeleteController);

    RequerimentoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Requerimento'];

    function RequerimentoDeleteController($uibModalInstance, entity, Requerimento) {
        var vm = this;

        vm.requerimento = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Requerimento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
