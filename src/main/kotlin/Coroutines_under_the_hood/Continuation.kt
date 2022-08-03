package Coroutines_under_the_hood

import kotlin.coroutines.CoroutineContext

interface Continuation<T> {
    val context : CoroutineContext  // 어떤 스레드에서 수행할 것인가?

    // fun resume(value:T)
    // fun resumeWithException(exception: Throwable)
    fun resumeWith(result: Result<T>)
}
