(function(){
    'use strict';

    angular
        .module("uniConnectApp")
        .controller("OpportunityPortalController", OpportunityPortalController);

    OpportunityPortalController.$inject = ['Opportunity'];

    function OpportunityPortalController(Opportunity){
        var vm = this;

        vm.hello = "Hello from the controller!";

    };

})();
