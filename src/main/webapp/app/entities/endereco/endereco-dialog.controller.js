(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('EnderecoDialogController', EnderecoDialogController);

    EnderecoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Endereco', 'Municipio', 'Empreendimento'];

    function EnderecoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Endereco, Municipio, Empreendimento) {
        var vm = this;

        vm.endereco = entity;
        vm.clear = clear;
        vm.save = save;
        vm.municipios = Municipio.query({filter: 'endereco-is-null'});
        $q.all([vm.endereco.$promise, vm.municipios.$promise]).then(function() {
            if (!vm.endereco.municipio || !vm.endereco.municipio.id) {
                return $q.reject();
            }
            return Municipio.get({id : vm.endereco.municipio.id}).$promise;
        }).then(function(municipio) {
            vm.municipios.push(municipio);
        });
        vm.empreendimentos = Empreendimento.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.endereco.id !== null) {
                Endereco.update(vm.endereco, onSaveSuccess, onSaveError);
            } else {
                Endereco.save(vm.endereco, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:enderecoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
