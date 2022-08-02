import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch {
        try {
            repeat(1000) {i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                // withContext(NonCancellable) -> Job 이 Cancel 되었지만, suspend 함수를 호출시켜야 할 때 사용한다.
                // withContext 는 context 를 지정한 이후, 코루틴을 실행하는 기능을 한다.
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }

    delay(1300L)
    kotlin.io.println("main: I'm tired of waiting!")
    job.cancelAndJoin()     // job.cancel() + job.join()
    kotlin.io.println("main: Now I can quit.")

    // 결과
    // job: I'm sleeping 0 ... [main]
    // job: I'm sleeping 1 ... [main]
    // job: I'm sleeping 2 ... [main]
    // main: I'm tired of waiting!
    // job: I'm running finally [main]
    // job: And I've just delayed for 1 sec because I'm non-cancellable [main]
    // main: Now I can quit.
}