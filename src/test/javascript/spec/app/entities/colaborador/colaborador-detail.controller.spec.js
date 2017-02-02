'use strict';

describe('Controller Tests', function() {

    describe('Colaborador Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockColaborador, MockUsuario, MockMunicipio;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockColaborador = jasmine.createSpy('MockColaborador');
            MockUsuario = jasmine.createSpy('MockUsuario');
            MockMunicipio = jasmine.createSpy('MockMunicipio');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Colaborador': MockColaborador,
                'Usuario': MockUsuario,
                'Municipio': MockMunicipio
            };
            createController = function() {
                $injector.get('$controller')("ColaboradorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smApp:colaboradorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
