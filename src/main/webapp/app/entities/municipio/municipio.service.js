(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('Municipio', Municipio);

    Municipio.$inject = ['$resource'];

    function Municipio ($resource) {
        var resourceUrl =  'api/municipios/:id';

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
