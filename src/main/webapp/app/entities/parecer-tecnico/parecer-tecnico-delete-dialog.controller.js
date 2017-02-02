(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ParecerTecnicoDeleteController',ParecerTecnicoDeleteController);

    ParecerTecnicoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ParecerTecnico'];

    function ParecerTecnicoDeleteController($uibModalInstance, entity, ParecerTecnico) {
        var vm = this;

        vm.parecerTecnico = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ParecerTecnico.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
