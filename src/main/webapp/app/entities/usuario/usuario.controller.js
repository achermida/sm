(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('UsuarioController', UsuarioController);

    UsuarioController.$inject = ['$scope', '$state', 'Usuario', 'UsuarioSearch'];

    function UsuarioController ($scope, $state, Usuario, UsuarioSearch) {
        var vm = this;

        vm.usuarios = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Usuario.query(function(result) {
                vm.usuarios = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            UsuarioSearch.query({query: vm.searchQuery}, function(result) {
                vm.usuarios = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
