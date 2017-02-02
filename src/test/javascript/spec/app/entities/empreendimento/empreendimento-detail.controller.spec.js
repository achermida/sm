'use strict';

describe('Controller Tests', function() {

    describe('Empreendimento Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEmpreendimento, MockEndereco, MockUsuario, MockProcesso, MockLicenca;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEmpreendimento = jasmine.createSpy('MockEmpreendimento');
            MockEndereco = jasmine.createSpy('MockEndereco');
            MockUsuario = jasmine.createSpy('MockUsuario');
            MockProcesso = jasmine.createSpy('MockProcesso');
            MockLicenca = jasmine.createSpy('MockLicenca');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Empreendimento': MockEmpreendimento,
                'Endereco': MockEndereco,
                'Usuario': MockUsuario,
                'Processo': MockProcesso,
                'Licenca': MockLicenca
            };
            createController = function() {
                $injector.get('$controller')("EmpreendimentoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smApp:empreendimentoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
