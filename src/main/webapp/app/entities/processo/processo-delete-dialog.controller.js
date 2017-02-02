(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ProcessoDeleteController',ProcessoDeleteController);

    ProcessoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Processo'];

    function ProcessoDeleteController($uibModalInstance, entity, Processo) {
        var vm = this;

        vm.processo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Processo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
