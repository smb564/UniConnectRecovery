(function(){
    'use strict';

    angular
        .module("uniConnectApp")
        .controller('OpportunityQuestionsController', OpportunityQuestionsController);

    OpportunityQuestionsController.$inject = ['entity', 'OpportunityQuestion', 'Principal'];

    function OpportunityQuestionsController(entity, OpportunityQuestion, Principal){
        var vm = this;

        vm.opportunity = entity;
        vm.questions = [];
        vm.isSaving = false;
        vm.isAdmin = false;

        vm.giveAnswer = giveAnswer;
        vm.upVote = upVote;
        vm.deleteQuestion = deleteQuestion;


        loadQuestions();

        // To format string with line breaks
        vm.formatString = formatString;

        // To save questions
        vm.saveQuestion = saveQuestion;

        Principal.identity().then(function(account) {
            vm.account = account;

            for(var i=0; i < vm.account.authorities.length; i++){
                if ("ROLE_ADMIN" == vm.account.authorities[i]){
                    vm.isAdmin = true;
                    break;
                }
            }
        });

        function formatString(text){
            // Add line breaks when displaying
            return text.replace("\n", "<br>");
        }

        function saveQuestion(){
            vm.isSaving = true;
            var question = {
                'description' : vm.questionText
            };

            OpportunityQuestion.saveModule({id : vm.opportunity.id}, question, loadQuestions, function(){
                vm.isSaving = false;
            });

            vm.questionText = null;
        }

        function loadQuestions(){
            vm.isSaving = false;
            OpportunityQuestion.getModule({id : vm.opportunity.id}, function(data){
                vm.questions = data;
            })
        }

        function giveAnswer(question){
            var answer = window.prompt("Enter the answer?");

            if (answer){
                question.answer = answer;
                OpportunityQuestion.update(question);
            }
        }

        function upVote(questionId){
            OpportunityQuestion.upVote({qId : questionId, userId : vm.account.login}, loadQuestions);
        }

        function deleteQuestion(qId){
            if (window.confirm("Are you sure want to delete?")){
                OpportunityQuestion.deleteOpportunity({qId : qId, oId : vm.opportunity.id}, loadQuestions);
            }
        }

    }

})();
