(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('MunicipioDeleteController',MunicipioDeleteController);

    MunicipioDeleteController.$inject = ['$uibModalInstance', 'entity', 'Municipio'];

    function MunicipioDeleteController($uibModalInstance, entity, Municipio) {
        var vm = this;

        vm.municipio = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Municipio.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
