(function() {
    'use strict';
    angular
        .module('smApp')
        .factory('ParecerTecnico', ParecerTecnico);

    ParecerTecnico.$inject = ['$resource', 'DateUtils'];

    function ParecerTecnico ($resource, DateUtils) {
        var resourceUrl =  'api/parecer-tecnicos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.parecDataInicio = DateUtils.convertLocalDateFromServer(data.parecDataInicio);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.parecDataInicio = DateUtils.convertLocalDateToServer(copy.parecDataInicio);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.parecDataInicio = DateUtils.convertLocalDateToServer(copy.parecDataInicio);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
