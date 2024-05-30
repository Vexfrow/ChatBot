package fr.c1.chatbot.composable

import fr.c1.chatbot.model.activity.AbstractActivity
import fr.c1.chatbot.model.activity.Associations
import fr.c1.chatbot.model.activity.Contenus
import fr.c1.chatbot.model.activity.Edifices
import fr.c1.chatbot.model.activity.EquipementsSport
import fr.c1.chatbot.model.activity.Expositions
import fr.c1.chatbot.model.activity.Festivals
import fr.c1.chatbot.model.activity.Jardins
import fr.c1.chatbot.model.activity.Musees
import fr.c1.chatbot.model.activity.Sites
import fr.c1.chatbot.ui.icons.Deceased
import fr.c1.chatbot.ui.icons.InteractiveSpace
import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.Festival
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.UUID

object Activities {
    @Composable
    operator fun invoke(
        list: List<AbstractActivity>,
        modifier: Modifier = Modifier
    ) = LazyColumn(modifier) {
        items(list) {
            when (it) {
                is Associations -> Assoc(association = it)
                else -> throw NotImplementedError()
            }
        }
    }

    @Composable
    fun TextWithIcon(
        text: String,
        style: TextStyle = LocalTextStyle.current,
        icon: ImageVector,
        modifier: Modifier = Modifier
    ) = Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = text)
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = text, style = style)
    }

    @Composable
    fun MyChip(text: String, icon: ImageVector) = SuggestionChip(
        onClick = { },
        label = { Text(text = text) },
        icon = { Icon(imageVector = icon, contentDescription = text) }
    )

    private fun Modifier.backgroundIf(color: Color, condition: Boolean) =
        if (condition) background(color)
        else this

    @Composable
    private fun MyColumn(
        accessible: Boolean,
        modifier: Modifier = Modifier,
        url: String? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        var mod = modifier
            .border(1.dp, Color.Black)
            .fillMaxWidth()
            .backgroundIf(Color.Red.copy(alpha = .5f), !accessible)

        if (url != null) {
            val uriHdl = LocalUriHandler.current

            mod = mod.clickable { uriHdl.openUri(url) }
        }

        Column(
            modifier = mod,
            content = content
        )
    }

    @Composable
    fun Assoc(
        association: Associations,
        modifier: Modifier = Modifier
    ) = MyColumn(association.accessible, modifier) {
        Text(
            text = association.nom,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${association.adresse}, ${association.codePostal} ${association.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Association", icon = Icons.Default.Groups)
    }

    @Composable
    fun Content(
        contenu: Contenus,
        modifier: Modifier = Modifier
    ) = MyColumn(contenu.accessible, modifier, contenu.url) {
        Text(
            text = contenu.nom,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${contenu.adresse}, ${contenu.codePostal} ${contenu.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Culture", icon = Icons.Default.TheaterComedy)
    }

    @Composable
    fun Building(
        edifice: Edifices,
        modifier: Modifier = Modifier
    ) = MyColumn(edifice.accessible, modifier) {
        Text(
            text = edifice.nom,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${edifice.adresse}, ${edifice.commune}, ${edifice.region}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Edifice", icon = Icons.Default.Castle)
    }

    @Composable
    fun SportEquipment(
        equipementSport: EquipementsSport,
        modifier: Modifier = Modifier
    ) = MyColumn(equipementSport.accessible, modifier, equipementSport.url) {
        Text(
            text = equipementSport.nom,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${equipementSport.adresse}, ${equipementSport.codePostal} ${equipementSport.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Équipement de sport", icon = Icons.Default.SportsSoccer)
    }

    @Composable
    fun Expo(
        exposition: Expositions,
        modifier: Modifier = Modifier
    ) = MyColumn(exposition.accessible, modifier, exposition.url) {
        Text(
            text = exposition.nom,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${exposition.commune} ${exposition.departement}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Exposition", icon = Icons.Default.InteractiveSpace)
    }

    @Composable
    fun Festival(
        festival: Festivals,
        modifier: Modifier = Modifier
    ) = MyColumn(festival.accessible, modifier) {
        Text(
            text = festival.nom,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${festival.adresse}, ${festival.codePostal} ${festival.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Festival", icon = Icons.Default.Festival)
    }

    @Composable
    fun Garden(
        jardin: Jardins,
        modifier: Modifier = Modifier
    ) = MyColumn(jardin.accessible, modifier) {
        Text(
            text = jardin.nom,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${jardin.adresse}, ${jardin.codePostal} ${jardin.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Jardin", icon = Icons.Default.Deceased)
    }

    @Composable
    fun Museum(
        musee: Musees,
        modifier: Modifier = Modifier
    ) = MyColumn(musee.accessible, modifier) {
        Text(
            text = musee.nom,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${musee.adresse}, ${musee.codePostal} ${musee.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Musée", icon = Icons.Default.Museum)
    }

    @Composable
    fun Site(
        site: Sites,
        modifier: Modifier = Modifier
    ) = MyColumn(site.accessible, modifier) {
        Text(
            text = site.commune,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = site.region,
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Site", icon = Icons.Default.LocationCity)
    }
}

@Preview(device = Devices.PIXEL_TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    LazyColumn {
        item {
            Activities.Assoc(
                association = Associations(
                    departement = "Dep",
                    identifiant = UUID.randomUUID().toString(),
                    commune = "Comm",
                    nom = "Name",
                    adresse = "Addr",
                    codePostal = "38610",
                    accessible = true
                )
            )
        }

        item {
            Activities.Content(
                contenu = Contenus(
                    identifiant = UUID.randomUUID().toString(),
                    commune = "Comm",
                    nom = "Name",
                    adresse = "Addr",
                    lieu = "Lieu",
                    codePostal = "38610",
                    url = "www.google.fr",
                    accessible = true
                )
            )
        }

        item {
            Activities.Building(
                edifice = Edifices(
                    region = "reg",
                    departement = "dep",
                    commune = "comm",
                    nom = "name",
                    adresse = "addr",
                    accessible = true
                )
            )
        }

        item {
            Activities.SportEquipment(
                EquipementsSport(
                    identifiant = UUID.randomUUID().toString(),
                    departement = "dep",
                    commune = "comm",
                    nom = "name",
                    adresse = "addr",
                    accessible = true,
                    codePostal = "cp",
                    url = "www.google.fr"
                )
            )
        }

        item {
            Activities.Expo(
                Expositions(
                    identifiant = UUID.randomUUID().toString(),
                    departement = "dep",
                    commune = "comm",
                    nom = "name",
                    accessible = true,
                    url = "www.google.fr",
                    region = "reg"
                )
            )
        }

        item {
            Activities.Festival(
                Festivals(
                    departement = "dep",
                    commune = "comm",
                    nom = "name",
                    accessible = true,
                    region = "reg",
                    adresse = "addr",
                    codePostal = "cp"
                )
            )
        }

        item {
            Activities.Garden(
                Jardins(
                    departement = "dep",
                    commune = "comm",
                    nom = "name",
                    accessible = true,
                    region = "reg",
                    adresse = "addr",
                    codePostal = "cp"
                )
            )
        }

        item {
            Activities.Museum(
                Musees(
                    identifiant = UUID.randomUUID().toString(),
                    departement = "dep",
                    commune = "comm",
                    nom = "name",
                    accessible = true,
                    region = "reg",
                    adresse = "addr",
                    codePostal = "cp",
                    lieu = "lieu2",
                    telephone = "tel",
                    url = "www.google.fr"
                )
            )
        }

        item {
            Activities.Site(
                Sites(
                    departement = "dep",
                    commune = "comm",
                    accessible = true,
                    region = "reg",
                )
            )
        }
    }
}