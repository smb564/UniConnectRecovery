(function(){
    'use strict';

    angular
        .module("uniConnectApp")
        .controller("ForumThreadController", ForumThreadController);

    ForumThreadController.$inject = ['entity', 'Post', 'Principal'];

    function ForumThreadController(entity, Post, Principal){
        var vm = this;
        vm.forum = entity;

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
            Post.getForum({ id : vm.forum.id}, function (data) {
                vm.posts = data;
            });
        }



        function addPost () {
            vm.isSaving = true;
            if (vm.post.id != null) {
                Post.update(vm.post, onSaveSuccess, onSaveError);
            } else {
                Post.saveForum({id : vm.forum.id}, vm.post, onSaveSuccess, onSaveError);
            }
        }


        function onSaveSuccess(){
            vm.isSaving = false;
            vm.post.title = null;
            vm.post.description = null;
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
                Post.deleteForForum({postId : postId, forumId : vm.forum.id}, loadPosts);
            }
        }
    }
})();
