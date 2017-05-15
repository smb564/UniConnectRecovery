'use strict';

describe('Controller Tests', function() {

    describe('ModulePage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockModulePage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockModulePage = jasmine.createSpy('MockModulePage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ModulePage': MockModulePage
            };
            createController = function() {
                $injector.get('$controller')("ModulePageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'uniConnectApp:modulePageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
