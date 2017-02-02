(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('MunicipioController', MunicipioController);

    MunicipioController.$inject = ['$scope', '$state', 'Municipio', 'MunicipioSearch'];

    function MunicipioController ($scope, $state, Municipio, MunicipioSearch) {
        var vm = this;

        vm.municipios = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Municipio.query(function(result) {
                vm.municipios = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MunicipioSearch.query({query: vm.searchQuery}, function(result) {
                vm.municipios = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
