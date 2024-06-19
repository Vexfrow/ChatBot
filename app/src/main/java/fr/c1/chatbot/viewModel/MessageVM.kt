package fr.c1.chatbot.viewModel

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import fr.c1.chatbot.R
import fr.c1.chatbot.model.messageManager.Message
import fr.c1.chatbot.model.messageManager.Tree
import fr.c1.chatbot.model.messageManager.TypeAction
import java.io.InputStream

class MessageVM {

    private val chatBotTree = Tree()
    private var mapScript: Map<String, InputStream> = mapOf()
    private val messageHistory : SnapshotStateList<Message> = TODO()


    fun initMessageManager(context: Context) {
        mapScript = mapOf(
            "Rob" to context.resources.openRawResource(R.raw.rob),
            "Amy" to context.resources.openRawResource(R.raw.amy),
            "Georges" to context.resources.openRawResource(R.raw.georges)
        )

        chatBotTree.initTree(this, mapScript)

    }


    fun addMessage(message : Message){
        messageHistory.add(message)
    }

    fun manageActions(action : TypeAction){
        when(action){
            TypeAction.None -> TODO()
            TypeAction.DateInput -> TODO()
            TypeAction.DistanceInput -> TODO()
            TypeAction.CityInput -> TODO()
            TypeAction.ShowResults -> TODO()
            TypeAction.Geolocate -> TODO()
            TypeAction.ChoosePassions -> TODO()
            TypeAction.PhysicalActivity -> TODO()
            TypeAction.CulturalActivity -> TODO()
            TypeAction.DeleteRecalls -> TODO()
            TypeAction.DeleteSuggestions -> TODO()
            TypeAction.DeletNotifs -> TODO()
            TypeAction.Back -> TODO()
            TypeAction.Restart -> TODO()
            TypeAction.ShowFilters -> TODO()
            TypeAction.Skip -> TODO()
        }
    }


}