(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('ProcessoSearch', ProcessoSearch);

    ProcessoSearch.$inject = ['$resource'];

    function ProcessoSearch($resource) {
        var resourceUrl =  'api/_search/processos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
