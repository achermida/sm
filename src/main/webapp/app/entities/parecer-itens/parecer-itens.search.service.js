(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('ParecerItensSearch', ParecerItensSearch);

    ParecerItensSearch.$inject = ['$resource'];

    function ParecerItensSearch($resource) {
        var resourceUrl =  'api/_search/parecer-itens/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
