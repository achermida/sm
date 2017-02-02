'use strict';

describe('Controller Tests', function() {

    describe('Processo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProcesso, MockRequerimento, MockEmpreendimento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProcesso = jasmine.createSpy('MockProcesso');
            MockRequerimento = jasmine.createSpy('MockRequerimento');
            MockEmpreendimento = jasmine.createSpy('MockEmpreendimento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Processo': MockProcesso,
                'Requerimento': MockRequerimento,
                'Empreendimento': MockEmpreendimento
            };
            createController = function() {
                $injector.get('$controller')("ProcessoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smApp:processoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
