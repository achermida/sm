'use strict';

describe('Controller Tests', function() {

    describe('Analise Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAnalise, MockParecerTecnico, MockRequerimento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAnalise = jasmine.createSpy('MockAnalise');
            MockParecerTecnico = jasmine.createSpy('MockParecerTecnico');
            MockRequerimento = jasmine.createSpy('MockRequerimento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Analise': MockAnalise,
                'ParecerTecnico': MockParecerTecnico,
                'Requerimento': MockRequerimento
            };
            createController = function() {
                $injector.get('$controller')("AnaliseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smApp:analiseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
