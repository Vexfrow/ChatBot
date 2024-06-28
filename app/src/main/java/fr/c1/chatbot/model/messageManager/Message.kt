package fr.c1.chatbot.model.messageManager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Reprensent a message
 *
 * @property messageContent Optional content of the message, empty by default
 * @property isUser Optional to indicate if the message is from the user, false by default
 * @property isScript Optional to indicate if it's a bot message and is in the script,
 * false by default
 *
 * @param showed Optional to indicate if the message was already shown, false by default
 */
class Message(
    var messageContent: String = "",
    var isUser: Boolean = false,
    var isScript: Boolean = false,
    showed: Boolean = false
) {
    var showed: Boolean by mutableStateOf(showed)
}