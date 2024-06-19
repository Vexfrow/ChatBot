package fr.c1.chatbot.utils

typealias ResourceList<T> = Resource<List<T>>

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class None<T>() : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data = data)
    class Loading<T>(data: T) : Resource<T>(data = data)
    class Failed<T>(error: Throwable, data: T? = null) : Resource<T>(error = error)

    fun succeed() = when(this) {
        is Success -> this
        is None -> Success(null)
        is Loading -> Success(data)
        is Failed -> throw IllegalStateException()
    }
}