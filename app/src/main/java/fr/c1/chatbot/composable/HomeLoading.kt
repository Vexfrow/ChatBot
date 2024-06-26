package fr.c1.chatbot.composable

import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.composable.utils.Loading
import fr.c1.chatbot.composable.utils.MyText
import fr.c1.chatbot.model.User
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.Resource
import fr.c1.chatbot.utils.UnitLaunchedEffect
import fr.c1.chatbot.utils.rememberMutableStateOf
import fr.c1.chatbot.viewModel.UserVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeLoading(
    app: ChatBot,
    userVM: UserVM,
    onInited: () -> Unit
) {
    val ctx = LocalContext.current

    UnitLaunchedEffect {
        launch(Dispatchers.Default) { app.init() }
        userVM.load(ctx)
    }

    var createNew by rememberMutableStateOf(value = false)

    when {
        userVM.users is Resource.Loading -> Loading(message = "Chargement des comptes...")
        createNew -> NewUser(
            onUndo = { createNew = false },
            onValidate = {
                userVM.newUser(it, ctx)
                userVM.setCurrentUser(it)
                createNew = false
            },
        )

        userVM.users is Resource.Success && userVM.currentUser == null -> {
            if (userVM.users.data!!.isEmpty()) {
                userVM.newUser(User.DEFAULT, ctx)
            }

            UserList(userVM) { createNew = true }
        }

        !app.inited -> Loading(message = "Chargement de l'application...")
        else -> onInited()
    }
}

@Composable
private fun UserList(
    userVM: UserVM,
    onCreate: () -> Unit
) {
    val ctx = LocalContext.current

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterHorizontally)
    ) {
        items(userVM.users.data!!) {
            UserComp(
                user = it,
                onSelect = { userVM.setCurrentUser(it) },
                onDelete = if (it != User.DEFAULT) {
                    { userVM.deleteUser(it, ctx) }
                } else null
            )
        }
        item {
            UserComp(
                icon = Icons.Default.AddCircleOutline,
                firstName = "Créer un",
                lastName = "compte",
                onSelect = onCreate
            )
        }
    }
}

@Composable
fun UserComp(
    user: User,
    onSelect: (() -> Unit),
    onDelete: (() -> Unit)? = null,
) = UserComp(
    firstName = user.firstName, lastName = user.lastName,
    onDelete = onDelete, onSelect = onSelect
)

@Composable
fun UserComp(
    icon: ImageVector = Icons.Default.AccountCircle,
    firstName: String,
    lastName: String,
    onSelect: () -> Unit,
    onDelete: (() -> Unit)? = null,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(25.dp)
) {
    Column(
        modifier = Modifier.clickable(onClick = onSelect),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(72.dp),
            imageVector = icon,
            contentDescription = firstName
        )
        MyText(
            text = firstName,
            style = MaterialTheme.typography.bodyMedium
        )
        MyText(
            text = lastName,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    if (onDelete != null)
        IconButton(
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.Red
            ),
            onClick = onDelete
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.RemoveCircleOutline,
                contentDescription = "Delete user"
            )
        }
}

@Composable
fun NewUser(
    onUndo: () -> Unit,
    onValidate: (User) -> Unit
) = Box(modifier = Modifier.fillMaxSize()) {
    val user = remember { User() }

    AccountComp.Data(user = user)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onUndo,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Retour")
        }

        Button(
            onClick = { onValidate(user) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
        ) {
            Text(text = "Valider")
        }
    }
}

@Preview
@Composable
private fun Prev() = ChatBotPrev { UserComp(user = User(), {}, {}) }