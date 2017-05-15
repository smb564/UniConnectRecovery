(function(){
    'use strict';

    angular
        .module('uniConnectApp')
        .controller('ModuleThreadController', ModuleThreadController);

    ModuleThreadController.$inject = ['entity', 'Post', 'AlertService', 'Principal', 'Notification'];

    function ModuleThreadController(entity, Post, AlertService, Principal, Notification){
        var vm = this;

        // Method to save new post
        vm.addPost = addPost;

        // To format the string before displaying
        vm.formatString = formatString;

        // To format date
        vm.formatDate = formatDate;

        // To up vote posts
        vm.upVote = upVote;

        // To delete posts
        vm.deletePost = deletePost;

        vm.modulePage = entity;
        vm.post = {};
        vm.posts = [];
        vm.addingPost = false;
        vm.isAdmin = false;

        loadPosts();

        Principal.identity().then(function(account) {
            vm.account = account;

            for(var i=0; i < vm.account.authorities.length; i++){
                if ("ROLE_ADMIN" == vm.account.authorities[i]){
                    vm.isAdmin = true;
                    break;
                }
            }
        });

        // Getting all the posts
        function loadPosts(){
            Post.getModule({ module : vm.modulePage.id}, function (data) {
                vm.posts = data;
            });
        }



        function addPost () {
            vm.isSaving = true;
            if (vm.post.id != null) {
                Post.update(vm.post, onSaveSuccess, onSaveError);
            } else {
                Post.saveModule({module : vm.modulePage.id}, vm.post, sendNotifications, onSaveError);
            }
        }

        function sendNotifications(result){
            // Send notifications to targets
            for(var i = 0; i < vm.modulePage.targets.length ; i ++){
                Notification.save(
                    {
                        "semester" : vm.modulePage.targets[i][0],
                        "department" : vm.modulePage.targets[i][1],
                        "notification" : "A new post added in the module " + vm.modulePage.title + ". Go to <a href='" + window.location.href + "/" + result.id + "' >this</a>"
                    }
                );
            }

            onSaveSuccess();
        }

        function onSaveSuccess(){
            vm.isSaving = false;
            vm.post.title = "";
            vm.post.description = "";
            loadPosts();
        }

        function onSaveError(){
            vm.isSaving = false;
        }

        function formatString(text){
            // Add line breaks when displaying
            return text.replace("\n", "<br>");
        }

        function formatDate(){
            console.log(vm.post.date);
            return vm.post.date.split("T")[0];
        }

        function upVote(postId){
            Post.upVote({postId : postId, userLogin : vm.account.login}, loadPosts);
        }

        function deletePost(postId){
            if (confirm("Do you want to delete the post?")){
                Post.deleteForModule({postId : postId, moduleId : vm.modulePage.id}, loadPosts);
            }
        }
    }

})();
