(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ProcessoDialogController', ProcessoDialogController);

    ProcessoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Processo', 'Requerimento', 'Empreendimento'];

    function ProcessoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Processo, Requerimento, Empreendimento) {
        var vm = this;

        vm.processo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.requerimentos = Requerimento.query();
        vm.empreendimentos = Empreendimento.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.processo.id !== null) {
                Processo.update(vm.processo, onSaveSuccess, onSaveError);
            } else {
                Processo.save(vm.processo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:processoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
