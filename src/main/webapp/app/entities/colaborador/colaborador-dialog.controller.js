(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ColaboradorDialogController', ColaboradorDialogController);

    ColaboradorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Colaborador', 'Usuario', 'Municipio'];

    function ColaboradorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Colaborador, Usuario, Municipio) {
        var vm = this;

        vm.colaborador = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usuarios = Usuario.query({filter: 'colaborador-is-null'});
        $q.all([vm.colaborador.$promise, vm.usuarios.$promise]).then(function() {
            if (!vm.colaborador.usuario || !vm.colaborador.usuario.id) {
                return $q.reject();
            }
            return Usuario.get({id : vm.colaborador.usuario.id}).$promise;
        }).then(function(usuario) {
            vm.usuarios.push(usuario);
        });
        vm.municipios = Municipio.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.colaborador.id !== null) {
                Colaborador.update(vm.colaborador, onSaveSuccess, onSaveError);
            } else {
                Colaborador.save(vm.colaborador, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:colaboradorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
