(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('LicencaSearch', LicencaSearch);

    LicencaSearch.$inject = ['$resource'];

    function LicencaSearch($resource) {
        var resourceUrl =  'api/_search/licencas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
