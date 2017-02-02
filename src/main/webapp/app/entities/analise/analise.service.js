(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('Analise', Analise);

    Analise.$inject = ['$resource'];

    function Analise ($resource) {
        var resourceUrl =  'api/analises/:id';

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
