package fr.c1.chatbot.ui.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import fr.c1.chatbot.R
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
        1 -> {}
        2 -> {}
        3 -> {}
        4 -> {}
        5 -> onNextTab(Tab.ChatBotResults.finalTab)
        6 -> onNextTab(Tab.ChatBotMap.finalTab)
        7 -> {}
        8 -> onNextTab(Tab.Account.finalTab)
        9 -> onNextTab(Tab.AccountData.finalTab)
        10 -> {}
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
        FloatingActionButton( // ChatBot
            onClick = {},
            modifier = Modifier
                .size(90.dp)
                .padding(start = 130.dp, top = 30.dp)
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
                                text = "Bonjour, je suis ton assistant pour ce tutoriel.\n" +
                                        "Voici l'onglet principal, clique dessus pour commencer !",
                                fontSize = 26.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton( // Conversation
            onClick = {},
            modifier = Modifier
                .size(150.dp)
                .padding(start = 212.dp, top = 130.dp)
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
                                text = "Voici le sous-onglet conversation\n" +
                                        "Clique dessus pour continuer !",
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton( // ChatBot with speech bubble
            onClick = {},
            modifier = Modifier
                .size(600.dp)
                .padding(start = 50.dp, top = 100.dp)
                .introShowCaseTarget(
                    index = 2,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Voici le 1er message du robot\n" +
                                        "Clique dessus pour continuer !",
                                fontSize = 26.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton( // Select text
            onClick = {},
            modifier = Modifier
                .size(1400.dp)
                .padding(start = 70.dp, top = 680.dp)
                .introShowCaseTarget(
                    index = 3,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.Gray
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Voici les propositions de messages sur lesquelles tu dois cliquer\n" +
                                        "Clique dessus pour continuer !",
                                fontSize = 26.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton( // Résultats
            onClick = {},
            modifier = Modifier
                .size(150.dp)
                .padding(start = 640.dp, top = 130.dp)
                .introShowCaseTarget(
                    index = 4,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Voici le sous onglet où s'afficheront les résultats de ta reqûete\n" +
                                        "Clique dessus pour continuer !",
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton( // Résultats (display)
            onClick = {},
            modifier = Modifier
                .size(150.dp)
                .padding(start = 640.dp, top = 130.dp)
                .introShowCaseTarget(
                    index = 5,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Gray,
                        backgroundAlpha = 0.6f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                modifier = Modifier.padding(start = 50.dp),
                                text = "Voici les résultats de ta reqûete\n" +
                                        "Clique sur l'onglet pour continuer !",
                                fontSize = 30.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                painterResource(id = R.drawable.right_arrow),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp)
                                    .align(Alignment.End),
                                tint = Color.Black
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton(
            // Map
            onClick = {},
            modifier = Modifier
                .size(150.dp)
                .padding(start = 1065.dp, top = 128.dp)
                .introShowCaseTarget(
                    index = 6,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.4f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column(
                            modifier = Modifier
                                .height(100.dp)
                                .width(850.dp)
                                .align(Alignment.BottomEnd)
                                .background(Color.Black.copy(alpha = 1.0f)),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                text = "Voici la carte sur laquelle seront aussi présents les résultats\n" +
                                        "Clique sur l'onglet pour continuer !",
                                fontSize = 30.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                lineHeight = 40.sp
                            )
                        }
                    }
                ),
        ) {}

        FloatingActionButton( // Compte
            onClick = {},
            modifier = Modifier
                .size(90.dp)
                .padding(start = 895.dp, top = 30.dp)
                .introShowCaseTarget(
                    index = 7,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Voici l'onglet Compte\n" +
                                        "Clique dessus pour y accéder !",
                                fontSize = 26.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton( // Compte
            onClick = {},
            modifier = Modifier
                .size(150.dp)
                .padding(start = 213.dp, top = 130.dp)
                .introShowCaseTarget(
                    index = 8,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.8f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Voici le sous onglet Informations\n",
                                fontSize = 26.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Clique dessus pour continuer !",
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton( // Informations
            onClick = {},
            modifier = Modifier
                .size(700.dp)
                .padding(start = 230.dp, top = 280.dp)
                .introShowCaseTarget(
                    index = 9,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.Gray
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Voici les informations que tu peux modifier\n" +
                                        "Clique dessus pour continuer !",
                                fontSize = 26.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}

        FloatingActionButton( // Suggestions
            onClick = {},
            modifier = Modifier
                .size(120.dp)
                .padding(start = 383.dp, top = 17.dp)
                .introShowCaseTarget(
                    index = -1,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color.Black,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Voici l'onglet suggestion\n" +
                                        "Clique dessus pour continuer !",
                                fontSize = 26.sp,
                                color = Color.White
                            )
                        }
                    }
                )
        ) {}
    }
}