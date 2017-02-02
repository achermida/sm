(function() {
    'use strict';

    angular
        .module('smApp')
        .controller('ParecerItensController', ParecerItensController);

    ParecerItensController.$inject = ['$scope', '$state', 'ParecerItens', 'ParecerItensSearch'];

    function ParecerItensController ($scope, $state, ParecerItens, ParecerItensSearch) {
        var vm = this;

        vm.parecerItens = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ParecerItens.query(function(result) {
                vm.parecerItens = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ParecerItensSearch.query({query: vm.searchQuery}, function(result) {
                vm.parecerItens = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
