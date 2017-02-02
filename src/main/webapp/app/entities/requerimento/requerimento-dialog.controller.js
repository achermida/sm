(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('RequerimentoDialogController', RequerimentoDialogController);

    RequerimentoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Requerimento', 'Analise', 'Processo'];

    function RequerimentoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Requerimento, Analise, Processo) {
        var vm = this;

        vm.requerimento = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.analises = Analise.query();
        vm.processos = Processo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.requerimento.id !== null) {
                Requerimento.update(vm.requerimento, onSaveSuccess, onSaveError);
            } else {
                Requerimento.save(vm.requerimento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:requerimentoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dtCadastro = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
