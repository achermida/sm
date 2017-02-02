(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('EmpreendimentoDialogController', EmpreendimentoDialogController);

    EmpreendimentoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Empreendimento', 'Endereco', 'Usuario', 'Processo', 'Licenca'];

    function EmpreendimentoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Empreendimento, Endereco, Usuario, Processo, Licenca) {
        var vm = this;

        vm.empreendimento = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.enderecos = Endereco.query({filter: 'empreendimento-is-null'});
        $q.all([vm.empreendimento.$promise, vm.enderecos.$promise]).then(function() {
            if (!vm.empreendimento.endereco || !vm.empreendimento.endereco.id) {
                return $q.reject();
            }
            return Endereco.get({id : vm.empreendimento.endereco.id}).$promise;
        }).then(function(endereco) {
            vm.enderecos.push(endereco);
        });
        vm.responsavels = Usuario.query({filter: 'empreendimento-is-null'});
        $q.all([vm.empreendimento.$promise, vm.responsavels.$promise]).then(function() {
            if (!vm.empreendimento.responsavel || !vm.empreendimento.responsavel.id) {
                return $q.reject();
            }
            return Usuario.get({id : vm.empreendimento.responsavel.id}).$promise;
        }).then(function(responsavel) {
            vm.responsavels.push(responsavel);
        });
        vm.processos = Processo.query();
        vm.licencas = Licenca.query();
        vm.usuarios = Usuario.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.empreendimento.id !== null) {
                Empreendimento.update(vm.empreendimento, onSaveSuccess, onSaveError);
            } else {
                Empreendimento.save(vm.empreendimento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smApp:empreendimentoUpdate', result);
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
