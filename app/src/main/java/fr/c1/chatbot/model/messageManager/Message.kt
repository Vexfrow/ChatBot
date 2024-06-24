package fr.c1.chatbot.model.messageManager

class Message(
    var messageContent: String = "",
    var isUser: Boolean = false,
    var isScript : Boolean = false, //True if it's a bot message and is in the script
    var showing : Boolean = true
) {

}