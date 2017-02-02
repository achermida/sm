(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('ParecerItens', ParecerItens);

    ParecerItens.$inject = ['$resource'];

    function ParecerItens ($resource) {
        var resourceUrl =  'api/parecer-itens/:id';

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
