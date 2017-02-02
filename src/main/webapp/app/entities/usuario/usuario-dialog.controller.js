(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('UsuarioDialogController', UsuarioDialogController);

    UsuarioDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Usuario', 'Contato', 'User', 'Empreendimento'];

    function UsuarioDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Usuario, Contato, User, Empreendimento) {
        var vm = this;

        vm.usuario = entity;
        vm.clear = clear;
        vm.save = save;
        vm.contatoes = Contato.query({filter: 'usuario-is-null'});
        $q.all([vm.usuario.$promise, vm.contatoes.$promise]).then(function() {
            if (!vm.usuario.contato || !vm.usuario.contato.id) {
                return $q.reject();
            }
            return Contato.get({id : vm.usuario.contato.id}).$promise;
        }).then(function(contato) {
            vm.contatoes.push(contato);
        });
        vm.users = User.query();
        vm.empreendimentos = Empreendimento.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.usuario.id !== null) {
                Usuario.update(vm.usuario, onSaveSuccess, onSaveError);
            } else {
                Usuario.save(vm.usuario, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:usuarioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
