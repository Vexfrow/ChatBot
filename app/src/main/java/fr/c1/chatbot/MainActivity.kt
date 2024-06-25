package fr.c1.chatbot

import fr.c1.chatbot.composable.AccountComp
import fr.c1.chatbot.composable.ChatBotComp
import fr.c1.chatbot.composable.History
import fr.c1.chatbot.composable.HomeLoading
import fr.c1.chatbot.composable.OsmdroidMapView
import fr.c1.chatbot.composable.PermissionsContent
import fr.c1.chatbot.composable.SettingsComp
import fr.c1.chatbot.composable.Suggestion
import fr.c1.chatbot.composable.Tab
import fr.c1.chatbot.composable.TopBar
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.repositories.UserRepository
import fr.c1.chatbot.ui.theme.ChatBotTheme
import fr.c1.chatbot.utils.UnitLaunchedEffect
import fr.c1.chatbot.utils.app
import fr.c1.chatbot.utils.rememberMutableStateListOf
import fr.c1.chatbot.utils.rememberMutableStateOf
import fr.c1.chatbot.viewModel.ActivitiesVM
import fr.c1.chatbot.viewModel.MessageVM
import fr.c1.chatbot.viewModel.UserVM
import org.osmdroid.config.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.preference.PreferenceManager
import android.os.Bundle

/** MainActivity TAG */
private const val TAG = "MainActivity"

/**
 * Main (and single) activity of the app
 *
 * @constructor Automatically called by android
 */
class MainActivity : ComponentActivity() {
    companion object {
        /** Snackbar host state */
        val snackbarHostState = SnackbarHostState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        enableEdgeToEdge()
        setContent { this() }
    }

    /**
     * Main component of the application
     *
     * - If app not inited, show a loading page and ask the permissions
     * - Otherwise, setup the [Scaffold] and manage the differents tabs
     *
     * @see HomeLoading First loading page
     * @see PermissionsContent Ask the permissions
     * @see ChatBotComp [Tab.ChatBot] components
     * @see OsmdroidMapView [Tab.ChatBotMap] component
     * @see SettingsComp [Tab.Settings] component
     * @see AccountComp [Tab.Account] component
     * @see Suggestion [Tab.Suggestion] component
     * @see History [Tab.History] component
     *
     * @throws NotImplementedError Throwed if the tab doesn't correpond to any [Tab]
     */
    @Composable
    private operator fun invoke() = ChatBotTheme {
        var inited by rememberMutableStateOf(value = false)
        val userVM = remember { UserVM() }

        // Request all needed permissions
        if (!inited) {
            Scaffold { padding ->
                Surface(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) { HomeLoading(app, userVM) { inited = true } }
            }
            PermissionsContent(context = this)
            return@ChatBotTheme
        }

        val activitiesVM = remember {
            ActivitiesVM(userVM.currentUser!!, app.activitiesRepository)
        }

        val messageManager = remember { MessageVM(this) }
        messageManager.initMessageManager()

        UnitLaunchedEffect {
            activitiesVM.load(app)
        }


        var tab by rememberMutableStateOf(value = Tab.ChatBot.finalTab)

        fun switchTab(value: Tab) {
            if (tab == Tab.Settings && value != Tab.Settings)
                Settings.save(this)

            val accountTabs =
                listOf(Tab.AccountPassions, Tab.AccountData, Tab.AccountPreferences)
            if (tab in accountTabs && value !in accountTabs)
                UserRepository.storeAll(this, userVM.users.data!!)

            tab = value
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = { TopBar(tabSelected = tab, onTabSelected = ::switchTab) },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Settings.backgroundColor)
                    .fillMaxSize()
            ) {
                val animated = rememberMutableStateListOf<Boolean>()

                when (tab) {
                    Tab.ChatBotChat -> ChatBotComp.Chat(
                        animated = animated,
                        activitiesVM = activitiesVM,
                        messageVM = messageManager
                    ) { tab = Tab.ChatBotResults }

                    Tab.ChatBotResults -> ChatBotComp.Result(activitiesVM)
                    Tab.Settings -> SettingsComp()

                    Tab.AccountPassions -> AccountComp.PassionsList(
                        userVM = userVM,
                        selected = userVM.currentUser!!::hasPassion
                    )

                    Tab.AccountData -> AccountComp.Data(userVM.currentUser!!)
                    Tab.AccountPreferences -> AccountComp.Preferences()
                    Tab.ChatBotMap -> OsmdroidMapView()
                    Tab.Suggestion -> Suggestion()
                    Tab.History -> History()

                    else -> throw NotImplementedError("The $tab tab is not implemented yet")
                }
            }
        }
    }
}