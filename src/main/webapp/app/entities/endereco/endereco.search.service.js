(function() {
    'use strict';

    angular
        .module('smApp')
        .factory('EnderecoSearch', EnderecoSearch);

    EnderecoSearch.$inject = ['$resource'];

    function EnderecoSearch($resource) {
        var resourceUrl =  'api/_search/enderecos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
