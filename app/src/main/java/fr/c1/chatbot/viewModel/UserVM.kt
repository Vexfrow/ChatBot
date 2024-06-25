package fr.c1.chatbot.viewModel

import fr.c1.chatbot.model.User
import fr.c1.chatbot.repositories.UserRepository
import fr.c1.chatbot.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import android.util.Log

private const val TAG = "UserVM"

class UserVM(
    private val repo: UserRepository = UserRepository
) : ViewModel() {
    var currentUser: User? by mutableStateOf(null)
        @JvmName("set_currentUser")
        private set

    fun setCurrentUser(user: User) {
        check(currentUser == null) { "A user is already selected !" }

        currentUser = user
    }

    var users: Resource<List<User>> by mutableStateOf(Resource.None())
        private set

    fun load(ctx: Context) {
        users = Resource.Loading(emptyList())
        viewModelScope.launch(Dispatchers.IO) {
            val tmp = try {
                Resource.Success(repo.loadAll(ctx))
            } catch (e: Exception) {
                Resource.Failed(e)
            }

            withContext(Dispatchers.Main) {
                users = tmp
            }

            Log.i(TAG, "Loaded users: $users")
        }
    }

    fun newUser(user: User, ctx: Context) {
        repo.store(user, ctx)
        val tmp = users.data!!.toMutableList().apply { add(user) }
        users = Resource.Success(tmp)
    }
}