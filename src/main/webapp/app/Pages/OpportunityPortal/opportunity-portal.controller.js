(function(){
    'use strict';

    angular
        .module("uniConnectApp")
        .controller("OpportunityPortalController", OpportunityPortalController);

    OpportunityPortalController.$inject = ['Opportunity', 'INTEREST_FIELDS', 'DEPARTMENTS'];

    function OpportunityPortalController(Opportunity, INTEREST_FIELDS, DEPARTMENTS){
        var vm = this;

        // Object sent to filter out opportunities
        vm.OpportunityFilter = {
            'targets' : [],
            'tags' : []
        };

        // methods used to maniputale targets
        vm.addTarget = addTarget;
        vm.removeTarget = removeTarget;

        // Get the interests and departments. Generate semesters
        vm.interests = INTEREST_FIELDS;
        vm.departments = DEPARTMENTS;



        vm.semesters = [];

        for(var i = 1; i < 9; i++){
            vm.semesters.push(i);
        }

        // Set selected for select
        vm.targetSem = vm.semesters[0];
        vm.targetDep = vm.departments[0];

        function addTarget(){
            // Check for duplicates and add
            for(var i=0; i < vm.OpportunityFilter.targets.length ; i++){
                if (vm.OpportunityFilter.targets[i][0]===vm.targetSem && vm.OpportunityFilter.targets[i][1]===vm.targetDep){ return ; }
            }
            vm.OpportunityFilter.targets.push([vm.targetSem, vm.targetDep]);
        }

        function removeTarget(index){
            vm.OpportunityFilter.targets.splice(index, 1);
        }
    }

})();
