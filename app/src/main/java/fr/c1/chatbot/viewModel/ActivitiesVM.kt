package fr.c1.chatbot.viewModel

import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.User
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
import fr.c1.chatbot.model.activity.Type
import fr.c1.chatbot.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import kotlin.coroutines.CoroutineContext

private const val TAG = "ActivitiesVM"

class ActivitiesVM(
    val user: User,
    val repo: ActivitiesRepository,
) : ViewModel() {
    var result: Resource<List<AbstractActivity>> by mutableStateOf(Resource.None())
        private set

    private var museums: Resource<List<Museum>> = Resource.None()
    private var sites: Resource<List<Site>> = Resource.None()
    private var expositions: Resource<List<Exposition>> = Resource.None()
    private var contents: Resource<List<Content>> = Resource.None()
    private var buildings: Resource<List<Building>> = Resource.None()
    private var gardens: Resource<List<Garden>> = Resource.None()
    private var festivals: Resource<List<Festival>> = Resource.None()
    private var sportEquipments: Resource<List<SportEquipment>> = Resource.None()
    private var associations: Resource<List<Association>> = Resource.None()

    private val all: List<Resource<out List<AbstractActivity>>>
        get() = listOf(
            museums,
            sites,
            expositions,
            contents,
            buildings,
            gardens,
            festivals,
            sportEquipments,
            associations,
        )

    val jobs: Array<Job?> = Array(all.size) { null }
    var resultJob: Job? = null

    private fun CoroutineContext.launch(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(this, start, block)

    private suspend fun <T> withMain(block: suspend CoroutineScope.() -> T) =
        withContext(Dispatchers.Main, block)

    fun load(ctx: ChatBot) {
        jobs[0] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start museums")
            val tmp = repo.getMuseums(ctx)
            withMain { museums = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished museums")
        }
        jobs[1] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start sites")
            val tmp = repo.getSites(ctx)
            withMain { sites = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished sites")
        }
        jobs[2] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start expositions")
            val tmp = repo.getExpositions(ctx)
            withMain { expositions = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished expositions")
        }
        jobs[3] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start contents")
            val tmp = repo.getContents(ctx)
            withMain { contents = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished contents")
        }
        jobs[4] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start buildings")
            val tmp = repo.getBuildings(ctx)
            withMain { buildings = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished buildings")
        }
        jobs[5] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start gardens")
            val tmp = repo.getGardens(ctx)
            withMain { gardens = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished gardens")
        }
        jobs[6] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start festivals")
            val tmp = repo.getFestivals(ctx)
            withMain { festivals = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished festivals")
        }
        jobs[7] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start sportEquipments")
            val tmp = repo.getSportEquipments(ctx)
            withMain { sportEquipments = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished sportEquipments")
        }
        jobs[8] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start associations")
            val tmp = repo.getAssociations(ctx)
            withMain { associations = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished associations")
        }
    }

    private fun updateResult(onFinish: suspend (List<AbstractActivity>) -> List<AbstractActivity>) {
        Dispatchers.Default.launch {
            resultJob?.join()

            resultJob = launch {
                jobs.forEachIndexed { i, job ->
                    val completed = job?.isCompleted ?: return@forEachIndexed

                    jobs[i] = null

                    if (completed)
                        return@forEachIndexed

                    job.join()
                    jobs[i] = null
                }

                result = Resource.Loading(
                    if (result is Resource.Loading || result is Resource.Success) result.data!!
                    else all.map { it.data!! }.flatten()
                )

                result = try {
                    Resource.Success(onFinish(result.data!!))
                } catch (e: Throwable) {
                    Resource.Failed(e, result.data)
                }
            }
        }
    }

    var date: String = ""
        set(value) {
            // ToDo: Filter by date
            updateResult {
                Log.i(TAG, "addType: Filter by date started")
                val result = it
                Log.i(TAG, "addType: Filter by date finished")
                result
            }
            field = value
        }

    fun addType(type: Type) {
        updateResult {
            Log.i(TAG, "addType: Filter by type started")
            val result = repo.selectByType(it, type)
            Log.i(TAG, "addType: Filter by type finished")
            result
        }
        user.addType(type)
    }

    var city: String
        get() = user.cities.joinToString()
        set(value) {
            updateResult {
                Log.i(TAG, "addType: Filter by city started")
                val result = repo.selectByCommune(it, value)
                Log.i(TAG, "addType: Filter by city finished")
                result
            }
            user.addCity(value)
        }

    var distance: Int = 0
        set(value) {
            // ToDo: Filter from distance
            updateResult {
                Log.i(TAG, "addType: Filter by distance started")
                val result = it
                Log.i(TAG, "addType: Filter by distance finished")
                result
            }
            field = value
        }
}