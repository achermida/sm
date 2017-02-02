(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('LicencaDeleteController',LicencaDeleteController);

    LicencaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Licenca'];

    function LicencaDeleteController($uibModalInstance, entity, Licenca) {
        var vm = this;

        vm.licenca = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Licenca.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
