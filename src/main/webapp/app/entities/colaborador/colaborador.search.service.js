(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('ColaboradorSearch', ColaboradorSearch);

    ColaboradorSearch.$inject = ['$resource'];

    function ColaboradorSearch($resource) {
        var resourceUrl =  'api/_search/colaboradors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
