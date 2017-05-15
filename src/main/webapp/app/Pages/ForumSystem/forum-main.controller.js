(function(){
    'use strict';

    angular
        .module("uniConnectApp")
        .controller("ForumMainController", ForumMainController);

    ForumMainController.$inject = ['Forum'];

    function ForumMainController(Forum){
        var vm = this;

        Forum.query(function(result){
            vm.forums = result;
        });

    }
})();
