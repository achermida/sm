(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('MunicipioSearch', MunicipioSearch);

    MunicipioSearch.$inject = ['$resource'];

    function MunicipioSearch($resource) {
        var resourceUrl =  'api/_search/municipios/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
