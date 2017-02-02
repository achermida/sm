(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('EmpreendimentoController', EmpreendimentoController);

    EmpreendimentoController.$inject = ['$scope', '$state', 'Empreendimento', 'EmpreendimentoSearch'];

    function EmpreendimentoController ($scope, $state, Empreendimento, EmpreendimentoSearch) {
        var vm = this;

        vm.empreendimentos = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Empreendimento.query(function(result) {
                vm.empreendimentos = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EmpreendimentoSearch.query({query: vm.searchQuery}, function(result) {
                vm.empreendimentos = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
