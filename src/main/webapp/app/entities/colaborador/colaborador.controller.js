(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ColaboradorController', ColaboradorController);

    ColaboradorController.$inject = ['$scope', '$state', 'Colaborador', 'ColaboradorSearch'];

    function ColaboradorController ($scope, $state, Colaborador, ColaboradorSearch) {
        var vm = this;

        vm.colaboradors = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Colaborador.query(function(result) {
                vm.colaboradors = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ColaboradorSearch.query({query: vm.searchQuery}, function(result) {
                vm.colaboradors = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
