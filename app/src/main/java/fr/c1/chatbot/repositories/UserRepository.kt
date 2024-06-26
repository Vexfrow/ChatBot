package fr.c1.chatbot.repositories

import com.google.gson.Gson
import com.google.gson.JsonObject
import fr.c1.chatbot.model.User
import android.content.Context
import android.util.Log
import java.io.FileFilter

private const val TAG = "UserRepository"

object UserRepository {
    /**
     * Load all users information
     */
    fun loadAll(context: Context): List<User> {
        val gson = Gson()
        val userList = mutableListOf<User>()
        val regex = Regex("""[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}.json""")

        val files = context.filesDir.listFiles(FileFilter { it.isFile && regex.matches(it.name) })!!
        if (files.isEmpty())
            Log.i(TAG, "loadAllUsersInformation: No user file found !")
        else
            files.forEach { file ->
                val obj = gson.fromJson(file.reader(), JsonObject::class.java)

                val user = User.from(file.nameWithoutExtension, obj)
                userList.add(user)
                Log.d(
                    TAG,
                    "loadAllUsersInformation: Loaded user profile from ${file.name}\nUtilisateur : ${user.firstName} ${user.lastName}, ${user.age} ans"
                )
            }

        return userList
    }

    /**
     * Store all users information
     */
    fun storeAll(context: Context, userList: List<User>) {
        userList.forEach { user -> store(user, context) }
        Log.d(TAG, "storeAllUsersInformation: Stored all user profiles")
    }

    /**
     * Store the user information in a json file
     */
    fun store(user: User, context: Context) {
        fun <T> Iterable<T>.toJson() = joinToString(prefix = "[", postfix = "]") { "\"$it\"" }

        val fileName = "${user.id}.json"

        // Build the JSON content
        val jsonContent = """
        {
            "lastName": "${user.lastName}",
            "firstName": "${user.firstName}",
            "age": ${user.age},
            "cities": ${user.cities.toJson()},
            "types": ${user.types.toJson()},
            "passions": ${user.passions.toJson()},
            "weeklyPreferences": ${user.weeklyPreferences.toJson()}
        }""".trimIndent()

        // Write the JSON content to the file
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output.write(jsonContent.toByteArray())
        }

        Log.i(TAG, "store: $user stored")
    }

    fun delete(user: User, context: Context) {
        context.deleteFile("${user.id}.json")
    }
}