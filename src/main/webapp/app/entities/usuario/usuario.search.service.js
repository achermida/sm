(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('UsuarioSearch', UsuarioSearch);

    UsuarioSearch.$inject = ['$resource'];

    function UsuarioSearch($resource) {
        var resourceUrl =  'api/_search/usuarios/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
