(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('RequerimentoSearch', RequerimentoSearch);

    RequerimentoSearch.$inject = ['$resource'];

    function RequerimentoSearch($resource) {
        var resourceUrl =  'api/_search/requerimentos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
