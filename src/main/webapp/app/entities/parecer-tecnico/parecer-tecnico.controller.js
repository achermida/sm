(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ParecerTecnicoController', ParecerTecnicoController);

    ParecerTecnicoController.$inject = ['$scope', '$state', 'ParecerTecnico', 'ParecerTecnicoSearch'];

    function ParecerTecnicoController ($scope, $state, ParecerTecnico, ParecerTecnicoSearch) {
        var vm = this;

        vm.parecerTecnicos = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ParecerTecnico.query(function(result) {
                vm.parecerTecnicos = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ParecerTecnicoSearch.query({query: vm.searchQuery}, function(result) {
                vm.parecerTecnicos = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
