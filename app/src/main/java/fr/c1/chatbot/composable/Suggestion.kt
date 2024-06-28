package fr.c1.chatbot.composable

import fr.c1.chatbot.composable.utils.BotLoading
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.Resource
import fr.c1.chatbot.utils.randoms
import fr.c1.chatbot.viewModel.ActivitiesVM
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * Component of the [Tab.Suggestion] tab
 *
 * @param activitiesVM View model to get all the activities
 * @param count Optional number of activities suggested, 10 by default
 */
@Composable
fun Suggestion(
    activitiesVM: ActivitiesVM,
    count: Int = 10
) {
    val all = activitiesVM.all
    when {
        all.any { it is Resource.Loading } -> BotLoading()
        else -> ActivitiesComp(all.map { it.data!! }.flatten().randoms(count))
    }
}

@Preview
@Composable
private fun Prev() = ChatBotPrev {}