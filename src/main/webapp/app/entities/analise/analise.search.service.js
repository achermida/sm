(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('AnaliseSearch', AnaliseSearch);

    AnaliseSearch.$inject = ['$resource'];

    function AnaliseSearch($resource) {
        var resourceUrl =  'api/_search/analises/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
