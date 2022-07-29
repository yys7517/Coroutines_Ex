import kotlinx.coroutines.*

// 코루틴을 취소하는 데에 있어서 코드 구성이 취소에 협조적일 필요가 있다.

// Cancellation is Cooperative -> 취소는 협조적이다.

// Coroutine cancellation is cooperative -> 코루틴의 취소는 협조적이다 ?
// A coroutine code has to cooperate to be cancellable -> 코루틴이 취소 가능하게 하려면, 코루틴의 코드는 협조적이어야 할 필요가 있다.
// suspending functions are cancellable -> suspend fun 은 취소 가능하다.

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        try {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {

                if (System.currentTimeMillis() >= nextPrintTime) {
                    delay( 1L )   // suspend fun 을 추가하여, cancel 가능하게 cooperate 하자.
                    // yield()               // delay 와 유사한 지연 함수.
                    // suspend fun 이 없다면, cancel 시킬 수 없다.
                    kotlin.io.println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        } catch ( e : Exception ) {
                println("Exception : [${e}]")
        }

    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()     // job.cancel() + job.join()
    println("main: Now I can quit.")

    // 결과 ( job 의 스코프 내에서 delay() 를 사용하지 않았을 때 )
    // job: I'm sleeping 0 ... [DefaultDispatcher-worker-1]
    // job: I'm sleeping 1 ... [DefaultDispatcher-worker-1]
    // job: I'm sleeping 2 ... [DefaultDispatcher-worker-1]
    // main: I'm tired of waiting! [main]
    // job: I'm sleeping 3 ... [DefaultDispatcher-worker-1]
    // job: I'm sleeping 4 ... [DefaultDispatcher-worker-1]
    // main: Now I can quit. [main]

    // => suspend fun 이 없으므로, job 이 cancel 되지 않는다.

    // Job 을 cancel 후, 다른 코루틴으로 재개될 때 다음과 같이 "kotlinx.coroutines.JobCancellationException" 이 발생하게 된다.
    // Job 이 취소되었을 때 suspend 함수를 호출하면 JobCancellationException 을 발생시킨다.

    // try - catch 사용하여 exception 을 확인해보자

    // try - catch 구문을 사용해본 결과
    // job: I'm sleeping 0 ...
    // job: I'm sleeping 1 ...
    // job: I'm sleeping 2 ...
    // main: I'm tired of waiting! [main]
    // Exception : [kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled; job=StandaloneCoroutine{Cancelling}@4740c0e3] [DefaultDispatcher-worker-1]
    // main: Now I can quit. [main]
}