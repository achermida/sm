(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('EnderecoController', EnderecoController);

    EnderecoController.$inject = ['$scope', '$state', 'Endereco', 'EnderecoSearch'];

    function EnderecoController ($scope, $state, Endereco, EnderecoSearch) {
        var vm = this;

        vm.enderecos = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Endereco.query(function(result) {
                vm.enderecos = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnderecoSearch.query({query: vm.searchQuery}, function(result) {
                vm.enderecos = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
