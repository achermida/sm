'use strict';

describe('Controller Tests', function() {

    describe('ParecerTecnico Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockParecerTecnico, MockAnalise, MockParecerItens;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockParecerTecnico = jasmine.createSpy('MockParecerTecnico');
            MockAnalise = jasmine.createSpy('MockAnalise');
            MockParecerItens = jasmine.createSpy('MockParecerItens');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ParecerTecnico': MockParecerTecnico,
                'Analise': MockAnalise,
                'ParecerItens': MockParecerItens
            };
            createController = function() {
                $injector.get('$controller')("ParecerTecnicoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smApp:parecerTecnicoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
