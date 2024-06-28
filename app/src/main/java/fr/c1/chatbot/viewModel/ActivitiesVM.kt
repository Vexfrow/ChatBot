package fr.c1.chatbot.viewModel

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
import fr.c1.chatbot.repositories.ActivitiesRepository
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
import android.content.Context
import android.util.Log
import kotlin.coroutines.CoroutineContext

private const val TAG = "ActivitiesVM"

/**
 * Activities view model
 *
 * @property user Current [User]
 * @property repo Repository of the [AbstractActivity]
 */
class ActivitiesVM(
    val user: User,
    val repo: ActivitiesRepository,
) : ViewModel() {
    /** [Resource] of the [AbstractActivity] filtered */
    var result: Resource<List<AbstractActivity>> by mutableStateOf(Resource.None())
        private set

    private var museums: Resource<List<Museum>> by mutableStateOf(Resource.None())
    private var sites: Resource<List<Site>> by mutableStateOf(Resource.None())
    private var expositions: Resource<List<Exposition>> by mutableStateOf(Resource.None())
    private var contents: Resource<List<Content>> by mutableStateOf(Resource.None())
    private var buildings: Resource<List<Building>> by mutableStateOf(Resource.None())
    private var gardens: Resource<List<Garden>> by mutableStateOf(Resource.None())
    private var festivals: Resource<List<Festival>> by mutableStateOf(Resource.None())
    private var sportEquipments: Resource<List<SportEquipment>> by mutableStateOf(Resource.None())
    private var associations: Resource<List<Association>> by mutableStateOf(Resource.None())

    /** All activities */
    val all: List<Resource<out List<AbstractActivity>>>
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

    /** Jobs for each [AbstractActivity] load or null */
    private val jobs: Array<Job?> = Array(all.size) { null }
    /** Job for the result filter */
    private var resultJob: Job? = null

    /**
     * Launch a coroutine in the [viewModelScope]
     *
     * @see CoroutineScope.launch
     */
    private fun CoroutineContext.launch(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(this, start, block)

    /** Do [block] on the [Dispatchers.Main] */
    private suspend fun <T> withMain(block: suspend CoroutineScope.() -> T) =
        withContext(Dispatchers.Main, block)

    /** Load activities */
    fun load(ctx: Context) {
        jobs[0] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start museums")
            withMain { museums = Resource.Loading(emptyList()) }
            val tmp = repo.getMuseums(user, ctx)
            withMain { museums = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished museums")
        }
        jobs[1] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start sites")
            withMain { sites = Resource.Loading(emptyList()) }
            val tmp = repo.getSites(user, ctx)
            withMain { sites = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished sites")
        }
        jobs[2] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start expositions")
            withMain { expositions = Resource.Loading(emptyList()) }
            val tmp = repo.getExpositions(user, ctx)
            withMain { expositions = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished expositions")
        }
        jobs[3] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start contents")
            withMain { contents = Resource.Loading(emptyList()) }
            val tmp = repo.getContents(user, ctx)
            withMain { contents = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished contents")
        }
        jobs[4] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start buildings")
            withMain { buildings = Resource.Loading(emptyList()) }
            val tmp = repo.getBuildings(user, ctx)
            withMain { buildings = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished buildings")
        }
        jobs[5] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start gardens")
            withMain { gardens = Resource.Loading(emptyList()) }
            val tmp = repo.getGardens(user, ctx)
            withMain { gardens = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished gardens")
        }
        jobs[6] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start festivals")
            withMain { festivals = Resource.Loading(emptyList()) }
            val tmp = repo.getFestivals(user, ctx)
            withMain { festivals = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished festivals")
        }
        jobs[7] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start sportEquipments")
            withMain { sportEquipments = Resource.Loading(emptyList()) }
            val tmp = repo.getSportEquipments(user, ctx)
            withMain { sportEquipments = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished sportEquipments")
        }
        jobs[8] = Dispatchers.IO.launch {
            Log.i(TAG, "load: Start associations")
            withMain { associations = Resource.Loading(emptyList()) }
            val tmp = repo.getAssociations(user, ctx)
            withMain { associations = Resource.Success(tmp) }
            Log.i(TAG, "load: Finished associations")
        }
    }

    /**
     * Update result
     *
     * @param onFinish Callback at the end of all jobs to filter the results
     */
    private fun updateResult(onFinish: suspend (List<AbstractActivity>) -> List<AbstractActivity>) {
        Dispatchers.Default.launch {
            resultJob?.join()

            resultJob = launch {
                if (result is Resource.None)
                    result = Resource.Loading(emptyList())
                else
                    history.add(result.data!!)

                jobs.forEachIndexed { i, job ->
                    val completed = job?.isCompleted ?: return@forEachIndexed

                    jobs[i] = null

                    if (completed)
                        return@forEachIndexed

                    job.join()
                    jobs[i] = null
                }

                result = Resource.Loading(
                    if (result is Resource.Success) result.data!!
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

    /** History of all results */
    private val history: ArrayDeque<List<AbstractActivity>> = ArrayDeque()

    /** Undo */
    fun undo() {
        result = history.removeLastOrNull()?.let { Resource.Success(it) } ?: Resource.None()
    }

    /** Reset */
    fun reset() {
        result = Resource.None()
    }

    /** Date */
    var date: String = ""
        get() = field
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

    /** Add type */
    fun addType(type: Type) {
        updateResult {
            Log.i(TAG, "addType: Filter by type started")
            val result = repo.selectByType(it, type)
            Log.i(TAG, "addType: Filter by type finished")
            result
        }
        user.addType(type)
    }

    /** City */
    var city: String
        get() = throw Exception()
        set(value) {
            updateResult {
                Log.i(TAG, "addType: Filter by city started")
                val result = repo.selectByCommune(it, value)
                Log.i(TAG, "addType: Filter by city finished")
                result
            }
            user.addCity(value)
        }

    /** Distance */
    var distance: Int = -1
        get() = field
        set(value) {
            updateResult {
                Log.i(TAG, "addType: Filter by distance started")
                val result = repo.selectByDistance(it, value)
                Log.i(TAG, "addType: Filter by distance finished")
                result
            }
            field = value
        }
}