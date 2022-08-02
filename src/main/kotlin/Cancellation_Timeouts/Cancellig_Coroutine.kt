
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Cancelling coroutine execution
// 코루틴의 실행을 멈추는 예제


// launch => 코루틴을 실행한다. without blocking the current thread
// launch( context ) => context 에서 코루틴을 실행한다.
// launch 의 반환형 => Job 객체
// Job 은 실행 중인 코루틴을 멈출 수 있다. ( job.cancel(). job.cancelAndJoin() )

fun main() = runBlocking {

    val job = launch {// Launches a new coroutine ""without blocking the current thread""
        repeat( 1000 ) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)     // delay 가 없다면, job 은 cancel 되지 않는다.
        }
    }

    // runBlocking 의 경우, 자식 스레드가 완료될 때 까지 스레드를 block 하므로
    // job 을 cancel 해주지 않으면 1000번을 모두 반복하게 된다.
    delay( 1300L )
    println("main: I'm tired of waiting!")
    // job.cancel()    // job 의 coroutine 을 즉시 종료한다.
    // job.join()      // job 이 종료될 때 까지 기다린다.
                        // job 이 main 을 덮고 있는 runBlocking 내에 속하지 않고, 예를 들어 GlobalScope에서 동작 중이라면..
                        // job 은 모든 작업을 마치지 못하고, 현재 runBlocking {}에 속한 main이 종료되버린다.

    job.cancelAndJoin()
    println("main: now I can quit.")

    // 결과
    // job: I'm sleeping 0 ... [main]
    // job: I'm sleeping 1 ... [main]
    // job: I'm sleeping 2 ... [main]
    // main: I'm tired of waiting! [main]
    // main: now I can quit. [main]

}