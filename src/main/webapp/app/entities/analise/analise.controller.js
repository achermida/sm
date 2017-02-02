(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('AnaliseController', AnaliseController);

    AnaliseController.$inject = ['$scope', '$state', 'Analise', 'AnaliseSearch'];

    function AnaliseController ($scope, $state, Analise, AnaliseSearch) {
        var vm = this;

        vm.analises = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Analise.query(function(result) {
                vm.analises = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AnaliseSearch.query({query: vm.searchQuery}, function(result) {
                vm.analises = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
