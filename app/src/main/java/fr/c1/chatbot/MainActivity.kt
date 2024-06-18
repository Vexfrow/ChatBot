package fr.c1.chatbot

import fr.c1.chatbot.composable.AccountComp
import fr.c1.chatbot.composable.ChatBotComp
import fr.c1.chatbot.composable.History
import fr.c1.chatbot.composable.OsmdroidMapView
import fr.c1.chatbot.composable.PermissionsContent
import fr.c1.chatbot.composable.SettingsComp
import fr.c1.chatbot.composable.Suggestion
import fr.c1.chatbot.composable.Tab
import fr.c1.chatbot.composable.TopBar
import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.storeAllUsersInformation
import fr.c1.chatbot.ui.theme.ChatBotTheme
import fr.c1.chatbot.utils.rememberMutableStateListOf
import fr.c1.chatbot.utils.rememberMutableStateOf
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import android.os.Bundle

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager
    private lateinit var app: ChatBot
    private lateinit var activitiesRepository: ActivitiesRepository
    private var requestingLocationUpdates: Boolean = false
    private lateinit var myOpenMapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(this)
        app = application as ChatBot
        activitiesRepository = app.activitiesRepository
        activitiesRepository.initAll(application)
        enableEdgeToEdge()
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        enableEdgeToEdge()
        setContent { this() }
    }

    @Composable
    private operator fun invoke() = ChatBotTheme {
        PermissionsContent(this)

        var tab by rememberMutableStateOf(value = Tab.ChatBot.finalTab)

        fun switchTab(value: Tab) {
            if (tab == Tab.Settings && value != Tab.Settings)
                Settings.save(this)

            val accountTabs =
                listOf(Tab.AccountPassions, Tab.AccountData, Tab.AccountPreferences)
            if (tab in accountTabs && value !in accountTabs)
                storeAllUsersInformation(this, app.userList)

            tab = value
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopBar(tabSelected = tab, onTabSelected = ::switchTab) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Settings.backgroundColor)
            ) {
                val messages =
                    rememberMutableStateListOf((application as ChatBot).chatbotTree.question)
                val animated = rememberMutableStateListOf<Boolean>()

                when (tab) {
                    Tab.ChatBotChat -> ChatBotComp.Chat(
                        messages = messages,
                        animated = animated
                    ) { tab = Tab.ChatBotResults }

                    Tab.ChatBotResults -> ChatBotComp.Result()
                    Tab.Settings -> SettingsComp()

                    Tab.AccountPassions -> AccountComp.PassionsList(
                        selected = app.currentUser::hasPassion,
                        onSelectionChanged = { passion, state ->
                            with(app.currentUser) {
                                if (state) addPassion(passion)
                                else removePassion(passion)
                            }
                        }
                    )

                    Tab.AccountData -> AccountComp.Data()
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