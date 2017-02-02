(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('Requerimento', Requerimento);

    Requerimento.$inject = ['$resource', 'DateUtils'];

    function Requerimento ($resource, DateUtils) {
        var resourceUrl =  'api/requerimentos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dtCadastro = DateUtils.convertLocalDateFromServer(data.dtCadastro);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dtCadastro = DateUtils.convertLocalDateToServer(copy.dtCadastro);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dtCadastro = DateUtils.convertLocalDateToServer(copy.dtCadastro);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
