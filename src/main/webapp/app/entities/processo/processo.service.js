(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('Processo', Processo);

    Processo.$inject = ['$resource'];

    function Processo ($resource) {
        var resourceUrl =  'api/processos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
