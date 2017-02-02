(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('Endereco', Endereco);

    Endereco.$inject = ['$resource'];

    function Endereco ($resource) {
        var resourceUrl =  'api/enderecos/:id';

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
