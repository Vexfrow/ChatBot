package fr.c1.chatbot.composable

import android.net.Uri
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.rememberMutableStateOf

@Composable
fun MySettings() {
    val context = LocalContext.current

    var isDialogOpen by remember { mutableStateOf(false) }
    var botDialog by rememberMutableStateOf(false)

    var isColorPickerOpen by remember { mutableStateOf(false) }
    var botColorPicker by rememberMutableStateOf(false)
    var fontColorPicker by rememberMutableStateOf(false)


    val selectImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (botDialog) Settings.botImage = uri
            else Settings.userImage = uri

            isDialogOpen = false
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

            if (Settings.botImage == null) MyIcon(icon = Settings.botIcon)
            else MyImage(img = Settings.botImage!!)

            if (Settings.userImage == null) MyIcon(icon = Settings.userIcon)
            else MyImage(img = Settings.userImage!!)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Aperçu de la taille du texte",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = Settings.textSize.value,
            onValueChange = { Settings.textSize = it.sp },
            valueRange = 30f..50f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(colors = ButtonDefaults.buttonColors(containerColor = with(Color) { if (Settings.tts) Green else Red }),
                onClick = {
                    Settings.tts = !Settings.tts
                }) {
                Text(text = "Lecture Audio: ${if (Settings.tts) "ON" else "OFF"}")
            }
            Button(onClick = { botDialog = true; isDialogOpen = true }) {
                Text(text = "Changer l'icône du Robot")
            }
            Button(onClick = { botDialog = false; isDialogOpen = true }) {
                Text(text = "Changer l'icône de l'utilisateur")
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {
                botColorPicker = true; fontColorPicker = false; isColorPickerOpen = true
            }) {
                Text(text = "Changer la couleur des bulles du robot")
            }
            Button(onClick = {
                botColorPicker = false; fontColorPicker = false; isColorPickerOpen = true
            }) {
                Text(text = "Changer la couleur des bulles de l'utilisateur")
            }
            Button(onClick = {
                botColorPicker = false; fontColorPicker = true; isColorPickerOpen = true
            }) {
                Text(text = "Changer la couleur du background")
            }
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

                        item {// Ajouter + d'icônes si nécessaire
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable { selectImageLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Ajouter une Photo de la galerie")
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
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        val colors = with(Color) {
                            listOf(
                                Red, Blue, White, Gray, Cyan, Green
                            )
                        }

                        items(colors) { color ->
                            Box(
                                modifier = Modifier
                                    .background(color)
                                    .size(100.dp)
                                    .clickable {
                                        if (botColorPicker) Settings.bubbleSpeechBotColor = color
                                        else if (fontColorPicker) Settings.fontColor = color
                                        else Settings.bubbleSpeechUserColor = color

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
fun GreetingPreview() = ChatBotPrev { MySettings() }