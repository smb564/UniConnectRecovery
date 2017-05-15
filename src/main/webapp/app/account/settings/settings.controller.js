(function() {
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['$scope', 'Principal', 'Auth', 'StudentUser', 'CompanyUser', 'INTEREST_FIELDS', 'DEPARTMENTS'];

    function SettingsController ($scope, Principal, Auth, StudentUser, CompanyUser, INTEREST_FIELDS, DEPARTMENTS) {
        var vm = this;

        // Getting the user type
        vm.isAdmin = false;
        vm.isCompany = false;
        vm.isUser = false;

        Principal.hasAuthority("ROLE_COMPANY").then(function(data){
            vm.isCompany = data;
        });

        Principal.hasAuthority("ROLE_USER").then(function(data){
            vm.isUser = data;
        });

        Principal.hasAuthority("ROLE_ADMIN").then(function(data){
            vm.isAdmin = data;
        });

        // Interests should be one of the following (Loaded from constants)
        vm.interests = INTEREST_FIELDS;
        vm.departments = DEPARTMENTS;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.saveProfile = saveProfile;
        vm.studentUser = {};
        vm.saveCompany = saveCompany;
        vm.companyUser = {};

        $scope.$watch(vm.isAdmin || vm.isCompany || vm.isUser, function(){
            // Get the current user profile (if available and update the fields accordingly)
            if (vm.isUser && !vm.isAdmin){
                StudentUser.getForCurrentUser(onStudentUserLoadSuccess, onError);
            }

            if (vm.isCompany && !vm.isAdmin){
                // Get the company data
                CompanyUser.getForCurrentUser(onCompanyUserLoadSuccess, onError);
            }
        });

        function onCompanyUserLoadSuccess(data){
            vm.companyUser.company = data.company;
            vm.companyUser.type = data.type;
        }

        function onStudentUserLoadSuccess(data){
            vm.studentUser.currentSemester = data.currentSemester;
            vm.studentUser.interests = data.interests;
            vm.studentUser.graduateYear = data.graduateYear;
            vm.studentUser.graduate = data.graduate;
            vm.studentUser.department = data.department;
        }

        function onError(err){
            console.log("Fetching data form the server failed : " + err);
        }

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        function saveProfile(){
            vm.isSaving = true;
            if (vm.studentUser.id !== null) {
                StudentUser.update(vm.studentUser, onSaveSuccess, onSaveError);
            } else {
                StudentUser.save(vm.studentUser, onSaveSuccess, onSaveError);
            }
        }

        function saveCompany () {
            vm.isSaving = true;
            if (vm.companyUser.id !== null) {
                CompanyUser.update(vm.companyUser, onSaveSuccess, onSaveError);
            } else {
                CompanyUser.save(vm.companyUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(){
            vm.isSaving = false;
            vm.error = null;
            vm.success = "OK";
        }

        function onSaveError(){
            vm.isSaving = false;
            vm.success = null;
            vm.error = "ERROR";
        }
    }
})();
