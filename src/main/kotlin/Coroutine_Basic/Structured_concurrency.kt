import kotlinx.coroutines.*

fun main() = runBlocking {
    /*
    val job = GlobalScope.launch {
        delay(3000L)
        println("World!")
    }

    println("Hello")
    job.join()
     */

    // Structured Concurrency 란 ?
    // => 위와 같이 job( GlobalScope.launch { } )이 속한 Coroutine Scope 가 끝나기를 기다리지 않고, 같은 Scope 내에서 사용하자.

    launch {// Launches a new coroutine without blocking the current thread
        delay(3000L)
        println("World!")
    }

    println("Hello")
}