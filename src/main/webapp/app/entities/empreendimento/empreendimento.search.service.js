(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('EmpreendimentoSearch', EmpreendimentoSearch);

    EmpreendimentoSearch.$inject = ['$resource'];

    function EmpreendimentoSearch($resource) {
        var resourceUrl =  'api/_search/empreendimentos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
