(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('Contato', Contato);

    Contato.$inject = ['$resource'];

    function Contato ($resource) {
        var resourceUrl =  'api/contatoes/:id';

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
