(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('LicencaDialogController', LicencaDialogController);

    LicencaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Licenca', 'Empreendimento'];

    function LicencaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Licenca, Empreendimento) {
        var vm = this;

        vm.licenca = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.empreendimentos = Empreendimento.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.licenca.id !== null) {
                Licenca.update(vm.licenca, onSaveSuccess, onSaveError);
            } else {
                Licenca.save(vm.licenca, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:licencaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.licenDataEmissao = false;
        vm.datePickerOpenStatus.licenDataEntrega = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
