package fr.c1.chatbot.ui.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import fr.c1.chatbot.composable.Tab

private const val TAG = "Tutorial"

@Composable
fun ShowcaseSample(
    onNextTab: (Tab) -> Unit,
    onShowcaseComplete: () -> Unit
) {
    var showAppIntro by remember { mutableStateOf(true) }
    val introShowcaseState = rememberIntroShowcaseState()

    when (introShowcaseState.currentTargetIndex) {
        0 -> onNextTab(Tab.ChatBotChat.finalTab)
        5 -> onNextTab(Tab.ChatBotResults.finalTab)
        6 -> onNextTab(Tab.ChatBotMap.finalTab)
        8 -> onNextTab(Tab.AccountData.finalTab)
        9 -> onNextTab(Tab.AccountPassions.finalTab)
        11 -> onNextTab(Tab.Settings.finalTab)
        13 -> onNextTab(Tab.Suggestion.finalTab)
        14 -> onNextTab(Tab.ChatBotChat.finalTab)
        else -> {}
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
        Box(
            modifier = Modifier
                .fillMaxWidth(.2f)
                .padding(top = 32.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // ChatBot
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
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
                                    text = "Bonjour, je suis ton assistant pour ce didacticiel.\n" +
                                            "Voici l'onglet principal, clique dessus pour commencer !",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(1 / 3f)
                .padding(top = 96.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Conversation
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
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
                                    text = "Voici l'onglet conversation\n" +
                                            "Clique dessus pour continuer !",
                                    fontSize = 22.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(11 / 20f)
                .padding(top = 200.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // ChatBot with speech bubble
                onClick = {},
                modifier = Modifier
                    .size(400.dp)
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
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(1 / 2f)
                .padding(top = 610.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Select text
                onClick = {},
                modifier = Modifier
                    .size(800.dp)
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
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 96.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Résultats
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
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
                                    text = "Voici l'onglet où s'afficheront les résultats de ta demande\n" +
                                            "Clique dessus pour continuer !",
                                    fontSize = 22.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 96.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Résultats (display)
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
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
                                    text = "Voici les résultats de ta reqûete\n",
                                    fontSize = 30.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Clique sur l'onglet pour continuer !",
                                    fontSize = 30.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 96.dp)
                .height(64.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1 / 3f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    // Map
                    onClick = {},
                    modifier = Modifier
                        .size(70.dp)
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
                                        .height(180.dp)
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
                                    Spacer(modifier = Modifier.height(20.dp))
                                    // Skip button
                                    SkipTutorialButton(
                                        showAppIntroToFalse = { showAppIntro = false },
                                        onSkipTutorial = { onShowcaseComplete() },
                                        onNextTab = onNextTab
                                    )
                                }
                            }
                        ),
                ) {}

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(64.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(3 / 5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton( // Compte
                    onClick = {},
                    modifier = Modifier
                        .size(70.dp)
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
                                    Spacer(modifier = Modifier.height(20.dp))
                                    // Skip button
                                    SkipTutorialButton(
                                        showAppIntroToFalse = { showAppIntro = false },
                                        onSkipTutorial = { onShowcaseComplete() },
                                        onNextTab = onNextTab
                                    )
                                }
                            }
                        )
                ) {}
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(1 / 3f)
                .padding(top = 96.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Onglet informations
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
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
                                    text = "Voici l'onglet Informations\n",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Ici, tu peux modifier\ntes informations comme tu le souhaites",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Clique dessus pour continuer !",
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }

        // Not used
        /*Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 96.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Onglet Préférences Hebdomadaires
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
                    .introShowCaseTarget(
                        index = 9, // Be careful, index must be unique
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color.Black,
                            backgroundAlpha = 0.8f,
                            targetCircleColor = Color.White
                        ),
                        content = {
                            Column {
                                Text(
                                    text = "Voici l'onglet Préférences Hebdomadaires\n",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Ici, tu peux modifier\ntes préférences hebdomadaires comme tu le souhaites",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Clique dessus pour continuer !",
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }*/

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 96.dp)
                .height(64.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1 / 3f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton( // Onglet Centres d'intérêts
                    onClick = {},
                    modifier = Modifier
                        .size(72.dp)
                        .introShowCaseTarget(
                            index = 9,
                            style = ShowcaseStyle.Default.copy(
                                backgroundColor = Color.Black,
                                backgroundAlpha = 0.9f,
                                targetCircleColor = Color.White
                            ),
                            content = {
                                Column {
                                    Text(
                                        text = "Voici l'onglet Centres d'intérêts\n",
                                        fontSize = 26.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Ici, tu peux sélectionner\ntes centres d'intérêts comme tu le souhaites",
                                        fontSize = 24.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Clique dessus pour continuer !",
                                        fontSize = 20.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    // Skip button
                                    SkipTutorialButton(
                                        showAppIntroToFalse = { showAppIntro = false },
                                        onSkipTutorial = { onShowcaseComplete() },
                                        onNextTab = onNextTab
                                    )
                                }
                            }
                        )
                ) {}
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(64.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1 / 5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton( // Onglet Paramètres
                    onClick = {},
                    modifier = Modifier
                        .size(70.dp)
                        .introShowCaseTarget(
                            index = 10,
                            style = ShowcaseStyle.Default.copy(
                                backgroundColor = Color.Black,
                                backgroundAlpha = 0.98f,
                                targetCircleColor = Color.White
                            ),
                            content = {
                                Column {
                                    Text(
                                        text = "Voici l'onglet Paramètres\n",
                                        fontSize = 26.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Clique dessus pour y accéder !",
                                        fontSize = 20.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    // Skip button
                                    SkipTutorialButton(
                                        showAppIntroToFalse = { showAppIntro = false },
                                        onSkipTutorial = { onShowcaseComplete() },
                                        onNextTab = onNextTab
                                    )
                                }
                            }
                        )
                ) {}
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(64.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1 / 5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton( // Onglet Paramètres
                    onClick = {},
                    modifier = Modifier
                        .size(70.dp)
                        .introShowCaseTarget(
                            index = 11,
                            style = ShowcaseStyle.Default.copy(
                                backgroundColor = Color.Black,
                                backgroundAlpha = 0.7f,
                                targetCircleColor = Color.White
                            ),
                            content = {
                                Column {
                                    Text(
                                        text = "Ici, tu peux personnaliser\nton expérience comme\ntu le désires",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Clique dessus pour continuer !",
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    // Skip button
                                    SkipTutorialButton(
                                        showAppIntroToFalse = { showAppIntro = false },
                                        onSkipTutorial = { onShowcaseComplete() },
                                        onNextTab = onNextTab
                                    )
                                }
                            }
                        )
                ) {}
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(3 / 5f)
                .padding(top = 32.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Onglet Suggestions
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
                    .introShowCaseTarget(
                        index = 12,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color.Black,
                            backgroundAlpha = 0.98f,
                            targetCircleColor = Color.White
                        ),
                        content = {
                            Column {
                                Text(
                                    text = "Voici l'onglet suggestions",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Clique dessus pour y accéder !",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(3 / 5f)
                .padding(top = 32.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Suggestions
                onClick = {},
                modifier = Modifier
                    .size(60.dp)
                    .introShowCaseTarget(
                        index = 13,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = MaterialTheme.colorScheme.onSurface,
                            backgroundAlpha = 0.93f,
                            targetCircleColor = MaterialTheme.colorScheme.surface
                        ),
                        content = {
                            Column {
                                Text(
                                    text = "Voici les principales suggestions",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.surface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Clique dessus pour continuer !",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.surface
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Button( // Skip button
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .background(Color.Transparent),
                                    onClick = {
                                        showAppIntro = false
                                        onShowcaseComplete()
                                        onNextTab(Tab.ChatBotChat.finalTab)
                                    },
                                    colors = ButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                        disabledContainerColor = Color.Transparent,
                                        disabledContentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Passer le didacticiel",
                                        fontSize = 20.sp,
                                    )
                                }
                            }
                        }
                    )
            ) {}
        }

        /*Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // Historique
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
                    .introShowCaseTarget(
                        index = 14, // Be careful, index must be unique
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color.Black,
                            backgroundAlpha = 0.98f,
                            targetCircleColor = Color.White
                        ),
                        content = {
                            Column {
                                Text(
                                    text = "Voici l'onglet Historique",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Tu y trouveras toutes tes conversations",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                // Skip button
                                SkipTutorialButton(
                                    showAppIntroToFalse = { showAppIntro = false },
                                    onSkipTutorial = { onShowcaseComplete() },
                                    onNextTab = onNextTab
                                )
                            }
                        }
                    )
            ) {}
        }*/

        Box(
            modifier = Modifier
                .fillMaxWidth(.2f)
                .padding(top = 32.dp)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton( // ChatBot
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
                    .introShowCaseTarget(
                        index = 14,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color.Black,
                            backgroundAlpha = 0.98f,
                            targetCircleColor = Color.White
                        ),
                        content = {
                            Column {
                                Text(
                                    text = "C'est la fin de ce didacticiel !",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "A ton tour maintenant !",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Clique sur l'onglet pour fermer le didacticiel",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                        }
                    )
            ) {}
        }
    }
}

// Button to skip the tutorial
@Composable
fun SkipTutorialButton(
    showAppIntroToFalse: () -> Unit,
    onSkipTutorial: () -> Unit,
    onNextTab: (Tab) -> Unit
) {
    Button( // Skip button
        modifier = Modifier
            .padding(top = 10.dp)
            .background(Color.Transparent),
        onClick = {
            showAppIntroToFalse()
            onSkipTutorial()
            onNextTab(Tab.ChatBotChat.finalTab)
        },
        colors = ButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.White
        )
    ) {
        Text(
            text = "Passer le didacticiel",
            fontSize = 20.sp,
        )
    }
}