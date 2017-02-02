(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('Colaborador', Colaborador);

    Colaborador.$inject = ['$resource'];

    function Colaborador ($resource) {
        var resourceUrl =  'api/colaboradors/:id';

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
