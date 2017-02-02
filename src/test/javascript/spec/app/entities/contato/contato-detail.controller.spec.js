'use strict';

describe('Controller Tests', function() {

    describe('Contato Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockContato, MockUsuario;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockContato = jasmine.createSpy('MockContato');
            MockUsuario = jasmine.createSpy('MockUsuario');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Contato': MockContato,
                'Usuario': MockUsuario
            };
            createController = function() {
                $injector.get('$controller')("ContatoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smApp:contatoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
