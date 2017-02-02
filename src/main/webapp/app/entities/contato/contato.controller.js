(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ContatoController', ContatoController);

    ContatoController.$inject = ['$scope', '$state', 'Contato', 'ContatoSearch'];

    function ContatoController ($scope, $state, Contato, ContatoSearch) {
        var vm = this;

        vm.contatoes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Contato.query(function(result) {
                vm.contatoes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ContatoSearch.query({query: vm.searchQuery}, function(result) {
                vm.contatoes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
