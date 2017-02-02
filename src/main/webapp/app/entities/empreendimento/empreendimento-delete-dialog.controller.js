(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('EmpreendimentoDeleteController',EmpreendimentoDeleteController);

    EmpreendimentoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Empreendimento'];

    function EmpreendimentoDeleteController($uibModalInstance, entity, Empreendimento) {
        var vm = this;

        vm.empreendimento = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Empreendimento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
