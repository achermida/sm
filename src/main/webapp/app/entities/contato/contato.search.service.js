(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('ContatoSearch', ContatoSearch);

    ContatoSearch.$inject = ['$resource'];

    function ContatoSearch($resource) {
        var resourceUrl =  'api/_search/contatoes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
