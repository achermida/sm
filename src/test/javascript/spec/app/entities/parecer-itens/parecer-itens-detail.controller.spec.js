'use strict';

describe('Controller Tests', function() {

    describe('ParecerItens Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockParecerItens, MockParecerTecnico;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockParecerItens = jasmine.createSpy('MockParecerItens');
            MockParecerTecnico = jasmine.createSpy('MockParecerTecnico');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ParecerItens': MockParecerItens,
                'ParecerTecnico': MockParecerTecnico
            };
            createController = function() {
                $injector.get('$controller')("ParecerItensDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smApp:parecerItensUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
