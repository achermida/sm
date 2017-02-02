'use strict';

describe('Controller Tests', function() {

    describe('Requerimento Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRequerimento, MockAnalise, MockProcesso;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRequerimento = jasmine.createSpy('MockRequerimento');
            MockAnalise = jasmine.createSpy('MockAnalise');
            MockProcesso = jasmine.createSpy('MockProcesso');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Requerimento': MockRequerimento,
                'Analise': MockAnalise,
                'Processo': MockProcesso
            };
            createController = function() {
                $injector.get('$controller')("RequerimentoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smApp:requerimentoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
