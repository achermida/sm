(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('ParecerTecnicoSearch', ParecerTecnicoSearch);

    ParecerTecnicoSearch.$inject = ['$resource'];

    function ParecerTecnicoSearch($resource) {
        var resourceUrl =  'api/_search/parecer-tecnicos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
