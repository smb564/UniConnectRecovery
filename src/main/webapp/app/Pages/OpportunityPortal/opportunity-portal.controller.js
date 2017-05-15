(function(){
    'use strict';

    angular
        .module("uniConnectApp")
        .controller("OpportunityPortalController", OpportunityPortalController);

    OpportunityPortalController.$inject = ['Opportunity', 'INTEREST_FIELDS', 'DEPARTMENTS'];

    function OpportunityPortalController(Opportunity, INTEREST_FIELDS, DEPARTMENTS){
        var vm = this;

        //Opportunities got from the server
        vm.opportunities = [];

        // Object sent to filter out opportunities
        vm.opportunityFilter = {
            'targets' : [],
            'tags' : []
        };

        // get all the opportunities first
        getOpportunities();

        // methods used to manipulate targets
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

        // To clear all the filters applied
        vm.clearFilters = clearFilters;

        // To get the results for the filter
        vm.getOpportunities = getOpportunities;

        function addTarget(){
            // Check for duplicates and add
            for(var i=0; i < vm.opportunityFilter.targets.length ; i++){
                if (vm.opportunityFilter.targets[i][0]===vm.targetSem && vm.opportunityFilter.targets[i][1]===vm.targetDep){ return ; }
            }
            vm.opportunityFilter.targets.push([vm.targetSem, vm.targetDep]);
        }

        function removeTarget(index){
            vm.opportunityFilter.targets.splice(index, 1);
        }

        function clearFilters(){
            vm.opportunityFilter = {
                'targets' : [],
                'tags' : []
            };
        }

        function getOpportunities(){
            Opportunity.getFilter(vm.opportunityFilter, function(data){
                vm.opportunities = data;
            });
        }
    }

})();
