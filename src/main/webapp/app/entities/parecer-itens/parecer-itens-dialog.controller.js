(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ParecerItensDialogController', ParecerItensDialogController);

    ParecerItensDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ParecerItens', 'ParecerTecnico'];

    function ParecerItensDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ParecerItens, ParecerTecnico) {
        var vm = this;

        vm.parecerItens = entity;
        vm.clear = clear;
        vm.save = save;
        vm.parecertecnicos = ParecerTecnico.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.parecerItens.id !== null) {
                ParecerItens.update(vm.parecerItens, onSaveSuccess, onSaveError);
            } else {
                ParecerItens.save(vm.parecerItens, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:parecerItensUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
