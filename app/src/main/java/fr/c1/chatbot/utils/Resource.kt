package fr.c1.chatbot.utils

typealias ResourceList<T> = Resource<List<T>>

/**
 * Resource
 *
 * @param T Type of the resource
 * @property data Optional data of the resource
 * @property error Optional error of the resource
 */
sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    /** The [Resource] is not initialized yet */
    class None<T> : Resource<T>()

    /** The [Resource] is complete */
    class Success<T>(data: T) : Resource<T>(data = data)

    /** The [Resource] is incomplete and loading */
    class Loading<T>(data: T) : Resource<T>(data = data)

    /** The [Resource] have failed */
    class Failed<T>(error: Throwable, data: T? = null) : Resource<T>(error = error, data = data)

    /** Transform any [Resource] into a [Success] */
    fun succeed() = when (this) {
        is Success -> this
        is None -> Success(null)
        is Loading -> Success(data)
        is Failed -> throw IllegalStateException()
    }

    override fun toString(): String = this::class.qualifiedName + when (this) {
        is None -> ""
        is Success -> "$data"
        is Loading -> "$data"
        is Failed -> "error=$error, data=$data"
    }
}