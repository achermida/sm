(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ContatoDeleteController',ContatoDeleteController);

    ContatoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contato'];

    function ContatoDeleteController($uibModalInstance, entity, Contato) {
        var vm = this;

        vm.contato = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Contato.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
