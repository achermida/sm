(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('Licenca', Licenca);

    Licenca.$inject = ['$resource', 'DateUtils'];

    function Licenca ($resource, DateUtils) {
        var resourceUrl =  'api/licencas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.licenDataEmissao = DateUtils.convertLocalDateFromServer(data.licenDataEmissao);
                        data.licenDataEntrega = DateUtils.convertLocalDateFromServer(data.licenDataEntrega);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.licenDataEmissao = DateUtils.convertLocalDateToServer(copy.licenDataEmissao);
                    copy.licenDataEntrega = DateUtils.convertLocalDateToServer(copy.licenDataEntrega);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.licenDataEmissao = DateUtils.convertLocalDateToServer(copy.licenDataEmissao);
                    copy.licenDataEntrega = DateUtils.convertLocalDateToServer(copy.licenDataEntrega);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
