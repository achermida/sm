(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('AnaliseDialogController', AnaliseDialogController);

    AnaliseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Analise', 'ParecerTecnico', 'Requerimento'];

    function AnaliseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Analise, ParecerTecnico, Requerimento) {
        var vm = this;

        vm.analise = entity;
        vm.clear = clear;
        vm.save = save;
        vm.parecertecnicos = ParecerTecnico.query();
        vm.requerimentos = Requerimento.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.analise.id !== null) {
                Analise.update(vm.analise, onSaveSuccess, onSaveError);
            } else {
                Analise.save(vm.analise, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:analiseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
