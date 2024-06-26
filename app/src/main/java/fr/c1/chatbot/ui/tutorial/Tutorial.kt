package fr.c1.chatbot.ui.tutorial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import fr.c1.chatbot.composable.Tab

@Composable
fun ShowcaseSample(
    onNextTab: (Tab) -> Unit,
    onShowcaseComplete: () -> Unit
) {
    var showAppIntro by remember { mutableStateOf(true) }
    val introShowcaseState = rememberIntroShowcaseState()

    when (introShowcaseState.currentTargetIndex) {
        0 -> {}
        1 -> onNextTab(Tab.Suggestion.finalTab)
        2 -> onNextTab(Tab.ChatBot.finalTab)
    }

    IntroShowcase(
        showIntroShowCase = showAppIntro,
        dismissOnClickOutside = false,
        onShowCaseCompleted = {
            showAppIntro = false
            onShowcaseComplete()
        },
        state = introShowcaseState,
    ) {
        FloatingActionButton(
            onClick = {
                onNextTab(Tab.Suggestion.finalTab)
            },
            modifier = Modifier
                .size(120.dp)
                .padding(start = 383.dp, top = 25.dp)
                .introShowCaseTarget(
                    index = 0,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Cliquez ici pour démarrer",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton(
            onClick = {
                onNextTab(Tab.ChatBot.finalTab)
            },
            modifier = Modifier
                .size(120.dp)
                .padding(start = 130.dp, top = 25.dp)
                .introShowCaseTarget(
                    index = 1,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Cliquez ici pour démarrer",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}
    }
}