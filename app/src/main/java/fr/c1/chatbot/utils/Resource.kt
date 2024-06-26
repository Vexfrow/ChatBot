package fr.c1.chatbot.utils

typealias ResourceList<T> = Resource<List<T>>

/**
 * Resource
 *
 * @param T
 * @property data
 * @property error
 * @constructor Create empty Resource
 */
sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    /**
     * None
     *
     * @param T
     * @constructor Create empty None
     */
    class None<T> : Resource<T>()

    /**
     * Success
     *
     * @param T
     * @constructor
     *
     * @param data
     */
    class Success<T>(data: T) : Resource<T>(data = data)

    /**
     * Loading
     *
     * @param T
     * @constructor
     *
     * @param data
     */
    class Loading<T>(data: T) : Resource<T>(data = data)

    /**
     * Failed
     *
     * @param T
     * @constructor
     *
     * @param error
     * @param data
     */
    class Failed<T>(error: Throwable, data: T? = null) : Resource<T>(error = error, data = data)

    /**
     * Succeed
     *
     */
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