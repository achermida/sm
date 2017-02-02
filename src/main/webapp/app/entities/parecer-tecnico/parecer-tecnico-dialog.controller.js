(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ParecerTecnicoDialogController', ParecerTecnicoDialogController);

    ParecerTecnicoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ParecerTecnico', 'Analise', 'ParecerItens'];

    function ParecerTecnicoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ParecerTecnico, Analise, ParecerItens) {
        var vm = this;

        vm.parecerTecnico = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.analises = Analise.query();
        vm.pareceritens = ParecerItens.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.parecerTecnico.id !== null) {
                ParecerTecnico.update(vm.parecerTecnico, onSaveSuccess, onSaveError);
            } else {
                ParecerTecnico.save(vm.parecerTecnico, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:parecerTecnicoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.parecDataInicio = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
