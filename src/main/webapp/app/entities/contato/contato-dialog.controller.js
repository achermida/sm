(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ContatoDialogController', ContatoDialogController);

    ContatoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contato', 'Usuario'];

    function ContatoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Contato, Usuario) {
        var vm = this;

        vm.contato = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usuarios = Usuario.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contato.id !== null) {
                Contato.update(vm.contato, onSaveSuccess, onSaveError);
            } else {
                Contato.save(vm.contato, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:contatoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
