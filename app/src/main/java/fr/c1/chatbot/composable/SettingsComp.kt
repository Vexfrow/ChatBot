package fr.c1.chatbot.composable

import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.ui.shape.SpeechBubbleShape
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.rememberMutableStateOf
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import fr.c1.chatbot.ui.theme.pickDarkColorList
import fr.c1.chatbot.ui.theme.pickLightColorList
import fr.c1.chatbot.utils.disableNotification
import fr.c1.chatbot.utils.enableNotification

@Composable
fun SettingsComp() {
    val context = LocalContext.current

    //Vars to manage icon's changes
    var isDialogOpen by rememberMutableStateOf(false)
    var botDialog by rememberMutableStateOf(false)

    //Vars to manage color's changes
    var isColorPickerOpen by rememberMutableStateOf(false)
    var botColorPicker by rememberMutableStateOf(false)
    var fontColorPicker by rememberMutableStateOf(false)

    //Vars to manage bot name's changes
    var isBotNameOpen by rememberMutableStateOf(false)

    //Vars to manage bot personality's changes
    var isBotPersonalityChooserOpen by rememberMutableStateOf(false)


    val selectImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (botDialog) Settings.botImage = uri
            else Settings.userImage = uri

            isDialogOpen = false
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Settings.backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //First Row with parameter for both user and bot
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val mod = Modifier.size(100.dp)

            @Composable
            fun MyIcon(icon: ImageVector) = Icon(
                modifier = mod, imageVector = icon, contentDescription = null
            )

            @Composable
            fun MyImage(img: Uri) = Image(
                modifier = mod, painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(img).build()
                ), contentDescription = null
            )

            //Colum for Bot
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (Settings.botImage == null) MyIcon(icon = Settings.botIcon)
                else MyImage(img = Settings.botImage!!)

                Button(onClick = {
                    botDialog = true
                    isDialogOpen = true
                }) { Text(text = "Changer l'icône du Robot") }

                Spacer(modifier = Modifier.height(20.dp))
                val shapeB = SpeechBubbleShape(15.dp, 15.dp, Size(350f, 170f))
                Box(
                    modifier = Modifier
                        .size(250.dp, 100.dp)
                        .clip(shapeB)
                        .background(Settings.bubbleSpeechBotColor)
                        .border(width = 4.dp, color = Color.Black, shape = shapeB)
                )
                Spacer(modifier = Modifier.height(5.dp))

                Button(onClick = {
                    botColorPicker = true
                    fontColorPicker = false
                    isColorPickerOpen = true
                }) { Text(text = "Changer la couleur des bulles du robot") }
            }


            //Colum for User
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (Settings.userImage == null) MyIcon(icon = Settings.userIcon)
                else MyImage(img = Settings.userImage!!)

                Button(onClick = {
                    botDialog = false
                    isDialogOpen = true
                }) { Text(text = "Changer l'icône de l'utilisateur") }

                Spacer(modifier = Modifier.height(20.dp))
                val shapeB = SpeechBubbleShape(15.dp, 15.dp, Size(350f, 170f))

                Box(
                    modifier = Modifier
                        .size(250.dp, 100.dp)
                        .graphicsLayer(rotationY = 180f)
                        .clip(shapeB)
                        .background(Settings.bubbleSpeechUserColor)
                        .border(width = 4.dp, color = Color.Black, shape = shapeB)

                )
                Spacer(modifier = Modifier.height(5.dp))

                Button(onClick = {
                    botColorPicker = false
                    fontColorPicker = false
                    isColorPickerOpen = true
                }) { Text(text = "Changer la couleur des bulles de l'utilisateur") }
            }

        }

        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Faîte glisser le curseur pour changer la taille du texte",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        Slider(
            value = Settings.textSize.value,
            onValueChange = { Settings.textSize = it.sp },
            valueRange = 30f..50f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )


        Spacer(modifier = Modifier.height(50.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                botColorPicker = false
                fontColorPicker = true
                isColorPickerOpen = true
            }) { Text(text = "Changer la couleur du fond d'écran") }
            Button(onClick = {
                isBotNameOpen = true
            }) { Text(text = "Changer le nom du robot") }
            Button(onClick = {
                isBotPersonalityChooserOpen = true
            }) { Text(text = "Changer la personnalité du robot") }
        }

        Spacer(modifier = Modifier.height(25.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(colors = ButtonDefaults.buttonColors(containerColor = with(Color) { if (Settings.tts) Green else Red }),
                onClick = {
                    Settings.tts = !Settings.tts
                }) { Text(text = "Lecture Audio : ${if (Settings.tts) "Activé" else "Désactivé"}") }
            Button(colors = ButtonDefaults.buttonColors(containerColor = with(Color) { if (Settings.notifs) Green else Red }),
                onClick = {
                    Settings.notifs = !Settings.notifs
                    if(Settings.notifs) enableNotification(context) else disableNotification(context)
                }) { Text(text = "Notifications : ${if (Settings.notifs) "Activé" else "Désactivé"}") }

        }
    }
    if (isDialogOpen) {
        Dialog(onDismissRequest = { isDialogOpen = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Veuillez choisir une icône",
                        style = MaterialTheme.typography.titleLarge
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(Settings.iconsAvailable) { icon ->
                            IconOption(imageVector = icon) {
                                if (botDialog) {
                                    Settings.botIcon = icon
                                    Settings.botImage = null
                                } else {
                                    Settings.userIcon = icon
                                    Settings.userImage = null
                                }

                                isDialogOpen = false
                            }
                        }

                        item {// Add more icon if the user want to
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable { selectImageLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Ajouter une photo de la galerie",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Button(onClick = { isDialogOpen = false }) {
                        Text("Annuler")
                    }
                }
            }
        }
    }
    if (isColorPickerOpen) {
        Dialog(onDismissRequest = { isColorPickerOpen = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Veuillez choisir une couleur",
                        style = MaterialTheme.typography.titleLarge
                    )
                    val colorList =
                        if (isSystemInDarkTheme()) pickDarkColorList else pickLightColorList
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(colorList) { color ->
                            Box(
                                modifier = Modifier
                                    .background(color)
                                    .border(4.dp, Color.Black)
                                    .size(100.dp)
                                    .clickable {
                                        when {
                                            botColorPicker -> Settings.bubbleSpeechBotColor = color
                                            fontColorPicker -> Settings.backgroundColor = color
                                            else -> Settings.bubbleSpeechUserColor = color
                                        }

                                        isColorPickerOpen = false
                                    },
                            ) {

                            }
                        }
                    }

                    Button(onClick = { isColorPickerOpen = false }) {
                        Text("Annuler")
                    }
                }
            }
        }
    }
    if (isBotNameOpen) {
        Dialog(onDismissRequest = { isBotNameOpen = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Comment voulez-vous que le robot s'appelle ?",
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextField(value = Settings.botName, onValueChange = { n ->
                        Settings.botName = n
                    })

                    Button(onClick = { isBotNameOpen = false }) {
                        Text("Valider")
                    }
                }
            }
        }
    }
    if (isBotPersonalityChooserOpen) {
        Dialog(onDismissRequest = { isBotPersonalityChooserOpen = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Quelle personnalité voulez-vous que le robot ait ?\n" +
                                "Attention, vous devrait recommencer la conversation depuis le début en cas de changement de personnalité",
                        style = MaterialTheme.typography.titleLarge
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        val list : List<String> = listOf("Amy", "Georges", "Rob")
                        items(list) { name ->
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable {
                                        Settings.botName = name
                                        Settings.botPersonality = name

                                        isBotPersonalityChooserOpen = false
                                    },
                            ) {
                                Text(text = name)
                            }
                        }
                    }
                    Button(onClick = { isBotPersonalityChooserOpen = false }) {
                        Text("Valider")
                    }
                }
            }
        }
    }
}

@Composable
fun IconOption(imageVector: ImageVector, size: Dp = 100.dp, onClick: () -> Unit) = IconButton(
    modifier = Modifier.size(size), onClick = onClick
) {
    Icon(
        modifier = Modifier.fillMaxSize(),
        imageVector = imageVector,
        contentDescription = null,
    )
}

@Preview(showBackground = true, device = Devices.TABLET)
@Composable
private fun Prev() = ChatBotPrev { SettingsComp() }