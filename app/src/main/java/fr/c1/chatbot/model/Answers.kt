package fr.c1.chatbot.model

class Answers(val label : String, val question : String, val answers : ArrayList<Answers>) {

   fun getAnswers(index:Int) : Answers {
      return answers.get(index);
   }

}