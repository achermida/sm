(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ParecerItensDeleteController',ParecerItensDeleteController);

    ParecerItensDeleteController.$inject = ['$uibModalInstance', 'entity', 'ParecerItens'];

    function ParecerItensDeleteController($uibModalInstance, entity, ParecerItens) {
        var vm = this;

        vm.parecerItens = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ParecerItens.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
