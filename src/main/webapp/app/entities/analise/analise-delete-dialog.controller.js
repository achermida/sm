(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('AnaliseDeleteController',AnaliseDeleteController);

    AnaliseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Analise'];

    function AnaliseDeleteController($uibModalInstance, entity, Analise) {
        var vm = this;

        vm.analise = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Analise.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
