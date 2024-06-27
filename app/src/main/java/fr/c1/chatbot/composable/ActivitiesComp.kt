package fr.c1.chatbot.composable

import fr.c1.chatbot.model.activity.AbstractActivity
import fr.c1.chatbot.model.activity.Association
import fr.c1.chatbot.model.activity.Building
import fr.c1.chatbot.model.activity.Content
import fr.c1.chatbot.model.activity.Exposition
import fr.c1.chatbot.model.activity.Festival
import fr.c1.chatbot.model.activity.Garden
import fr.c1.chatbot.model.activity.Museum
import fr.c1.chatbot.model.activity.Site
import fr.c1.chatbot.model.activity.SportEquipment
import fr.c1.chatbot.ui.icons.Deceased
import fr.c1.chatbot.ui.icons.InteractiveSpace
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.backgroundIf
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

/** All [AbstractActivity] component */
object ActivitiesComp {
    /**
     * Component of the [Tab.ChatBotResults]
     *
     * @param list List of the [AbstractActivity] to show
     */
    @Composable
    operator fun invoke(
        list: List<AbstractActivity>,
        modifier: Modifier = Modifier
    ) = LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(list) {
            when (it) {
                is Association -> Association(association = it)
                is Content -> Content(contenu = it)
                is Building -> Building(edifice = it)
                is SportEquipment -> SportEquipment(equipementSport = it)
                is Exposition -> Exposition(exposition = it)
                is Festival -> Festival(festival = it)
                is Garden -> Garden(jardin = it)
                is Museum -> Museum(musee = it)
                is Site -> Site(site = it)
                else -> throw NotImplementedError()
            }
        }
    }

    /**
     * Show a [text] under an [icon]
     *
     * @param style Style of thr [text]
     */
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

    /** Chip with a specified [text] and an [icon] and no click listener */
    @Composable
    fun MyChip(text: String, icon: ImageVector) = SuggestionChip(
        onClick = { },
        label = { Text(text = text) },
        icon = { Icon(imageVector = icon, contentDescription = text) }
    )

    /**
     * Column for the [AbstractActivity]
     *
     * @param accessible Indicate if the [AbstractActivity] is accessible
     * @param url Url of the [AbstractActivity]. Null by default
     */
    @Composable
    private fun MyColumn(
        accessible: Boolean,
        modifier: Modifier = Modifier,
        url: String? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        var mod = modifier
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth()
            .backgroundIf(Color.Red.copy(alpha = .5f), !accessible)
            .padding(10.dp)

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
    fun Association(
        association: Association,
        modifier: Modifier = Modifier
    ) = MyColumn(association.accessible, modifier) {
        Text(
            text = association.name,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.fontSize
        )
        TextWithIcon(
            text = "${association.address}, ${association.postalCode} ${association.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Association", icon = Icons.Default.Groups)
    }

    @Composable
    fun Content(
        contenu: Content,
        modifier: Modifier = Modifier
    ) = MyColumn(contenu.accessible, modifier, contenu.url) {
        Text(
            text = contenu.name,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${contenu.address}, ${contenu.postalCode} ${contenu.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Culture", icon = Icons.Default.TheaterComedy)
    }

    @Composable
    fun Building(
        edifice: Building,
        modifier: Modifier = Modifier
    ) = MyColumn(edifice.accessible, modifier) {
        Text(
            text = edifice.name,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${edifice.address}, ${edifice.commune}, ${edifice.region}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Edifice", icon = Icons.Default.Castle)
    }

    @Composable
    fun SportEquipment(
        equipementSport: SportEquipment,
        modifier: Modifier = Modifier
    ) = MyColumn(equipementSport.accessible, modifier, null) {
        Text(
            text = equipementSport.name,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${equipementSport.address}, ${equipementSport.postalCode} ${equipementSport.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Équipement de sport", icon = Icons.Default.SportsSoccer)
    }

    @Composable
    fun Exposition(
        exposition: Exposition,
        modifier: Modifier = Modifier
    ) = MyColumn(exposition.accessible, modifier, exposition.url) {
        Text(
            text = exposition.name,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${exposition.commune} ${exposition.department}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Exposition", icon = Icons.Default.InteractiveSpace)
    }

    @Composable
    fun Festival(
        festival: Festival,
        modifier: Modifier = Modifier
    ) = MyColumn(festival.accessible, modifier) {
        Text(
            text = festival.name,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${festival.address}, ${festival.postalCode} ${festival.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Festival", icon = Icons.Default.Festival)
    }

    @Composable
    fun Garden(
        jardin: Garden,
        modifier: Modifier = Modifier
    ) = MyColumn(jardin.accessible, modifier) {
        Text(
            text = jardin.name,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${jardin.address}, ${jardin.postalCode} ${jardin.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Jardin", icon = Icons.Default.Deceased)
    }

    @Composable
    fun Museum(
        musee: Museum,
        modifier: Modifier = Modifier
    ) = MyColumn(musee.accessible, modifier) {
        Text(
            text = musee.name,
            style = MaterialTheme.typography.bodyLarge
        )
        TextWithIcon(
            text = "${musee.address}, ${musee.postalCode} ${musee.commune}",
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Home
        )
        MyChip(text = "Musée", icon = Icons.Default.Museum)
    }

    @Composable
    fun Site(
        site: Site,
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
    ActivitiesComp(
        list = listOf(
            Association(
                department = "Dep",
                id = UUID.randomUUID().toString(),
                commune = "Comm",
                name = "Name",
                address = "Addr",
                postalCode = "38610",
                accessible = true,
                latitude = 0.0,
                longitude = 0.0,
                url = "www.google.fr"
            ),

            Content(
                id = UUID.randomUUID().toString(),
                commune = "Comm",
                name = "Name",
                address = "Addr",
                location = "Lieu",
                postalCode = "38610",
                url = "www.google.fr",
                accessible = true,
                latitude = 0.0,
                longitude = 0.0
            ),

            Building(
                region = "reg",
                department = "dep",
                commune = "comm",
                name = "name",
                address = "addr",
                accessible = true,
                latitude = 0.0,
                longitude = 0.0
            ),

            SportEquipment(
                department = "dep",
                commune = "comm",
                name = "name",
                address = "addr",
                accessible = true,
                postalCode = "cp",
                latitude = 0.0,
                longitude = 0.0
            ),

            Exposition(
                id = UUID.randomUUID().toString(),
                department = "dep",
                commune = "comm",
                name = "name",
                accessible = true,
                url = "www.google.fr",
                region = "reg",
                latitude = 0.0,
                longitude = 0.0
            ),

            Festival(
                department = "dep",
                commune = "comm",
                name = "name",
                accessible = true,
                region = "reg",
                address = "addr",
                postalCode = "cp",
                discipline = "disc",
                latitude = 0.0,
                longitude = 0.0
            ),

            Garden(
                department = "dep",
                commune = "comm",
                name = "name",
                accessible = true,
                region = "reg",
                address = "addr",
                postalCode = "cp",
                latitude = 0.0,
                longitude = 0.0
            ),


            Museum(
                id = UUID.randomUUID().toString(),
                department = "dep",
                commune = "comm",
                name = "name",
                accessible = true,
                region = "reg",
                address = "addr",
                postalCode = "cp",
                location = "lieu2",
                phone = "tel",
                url = "www.google.fr",
                latitude = 0.0,
                longitude = 0.0
            ),

            Site(
                department = "dep",
                commune = "comm",
                accessible = true,
                region = "reg",
                latitude = 0.0,
                longitude = 0.0
            )
        )
    )
}