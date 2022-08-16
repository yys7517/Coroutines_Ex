package Asynchronous_Flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*

private fun getFlow() : Flow<Int> = (1..3).asFlow().onEach { delay(100) }

fun main() = runBlocking {
    /*
    getFlow()
        .onEach { event -> println("Event : $event") }
        .collect()      // flow 가 모두 수집되기를 기다리는 suspend fun

    println("Done")

    // Event : 1
    // Event : 2
    // Event : 3
    // Done

    Flow 의 Collect 연산이 완료되기를 기다린 후, println 으로 출력을 한다.
     */

    // collect() -> public "suspend fun" Flow<*>.collect() = collect(NopCollector)

    // launchIn() -> public fun <T> Flow<T>.launchIn(scope: CoroutineScope): Job = scope.launch { collect() // tail-call }
    // parameter 로는 Coroutine 이 동작할 CoroutineScope 를 값으로 받는다.
    // launch 와 유사하게 launchIn 또한 Job 객체를 반환한다.
    // 따라서 전체 스코프를 취소하거나 결합하지 않고, launchIn 으로 실행되는 해당 코루틴을 취소할 수 있다.

    getFlow()
        .onEach { event -> println("Event : $event") }
        .launchIn(this) // 별도의 코루틴에서 flow 시작.

    println("Done")

    // Flow 의 Collect 연산은 launchIn에 의해 다른 코루틴에서 시작되도록하므로, 출력연산과 Collect 연산이 비동기적으로 실행된다.

    // Done
    // Event : 1
    // Event : 2
    // Event : 3

}