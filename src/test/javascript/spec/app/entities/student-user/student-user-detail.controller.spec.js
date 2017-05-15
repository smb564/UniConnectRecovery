'use strict';

describe('Controller Tests', function() {

    describe('Student_user Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStudent_user;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStudent_user = jasmine.createSpy('MockStudent_user');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Student_user': MockStudent_user
            };
            createController = function() {
                $injector.get('$controller')("StudentUserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'uniConnectApp:student_userUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
