import kotlinx.coroutines.*

// Making computation code cancellable

// way 1: to periodically invoke a suspending    --> 주기적으로 suspending 을 발생시킨다.
// => cancel 되었을 때, suspend fun 을 호출하면 Exception 이 발생한다.

// way 2: explicitly check the cancellation status (isActive) --> 명시적으로 cancel 상태를 체크한다.

// isActive - 현재 코루틴이 cancel 되었는 지 확인한다. 명시적으로 cancel 상태를 체크한다.

fun main() = runBlocking {

    val startTime = System.currentTimeMillis()

    val job = launch(Dispatchers.Default) {
        try {
            var nextPrintTime = startTime
            var i = 0
            kotlin.io.println("isActive $isActive ...")
            while ( isActive ) {    // 현재 scope 내 코루틴이 cancel 되지 않았다면, 반복한다.
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
            kotlin.io.println("isActive $isActive ...")

        } catch ( e : Exception ) {
                println("Exception : [${e}]")
        }

    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()     // job.cancel() + job.join()
    println("main: Now I can quit.")

    // 결과 - Exception 이 발생되지 않았다.
    // isActive true ...
    // job: I'm sleeping 0 ... [DefaultDispatcher-worker-1]
    // job: I'm sleeping 1 ... [DefaultDispatcher-worker-1]
    // job: I'm sleeping 2 ... [DefaultDispatcher-worker-1]
    // main: I'm tired of waiting! [main]
    // isActive false ...
    // main: Now I can quit. [main]

}