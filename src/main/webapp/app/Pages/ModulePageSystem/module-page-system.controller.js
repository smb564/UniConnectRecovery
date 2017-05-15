(function () {
    'user strict';

    angular
        .module('uniConnectApp')
        .controller("ModuleSystemController", ModuleSystemController);

    ModuleSystemController.$inject = ['ModulePage','AlertService', 'Principal'];

    function ModuleSystemController(ModulePage, AlertService, Principal){
        var vm = this;
        vm.modules = [];
        vm.isAdmin = false;
        vm.deletePage = deletePage;

        loadModules();

        function loadModules() {
            ModulePage.query(onSuccess, onError);
        }

        function onSuccess(data, headers){
            vm.modules = data;
        }

        function onError(err){
            AlertService.error(err.data.message);
        }

        Principal.identity().then(function(account) {
            vm.account = account;

            for(var i=0; i < vm.account.authorities.length; i++){
                if ("ROLE_ADMIN" == vm.account.authorities[i]){
                    vm.isAdmin = true;
                    break;
                }
            }
        });

        function deletePage(id){
            if (confirm("Are you sure want to delete?")){
                ModulePage.delete({id : id}, loadModules);
            }
        }

    }

})();
