
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Cancelling coroutine execution
// 코루틴의 실행을 멈추는 예제
// Job 은 실행 중인 코루틴을 멈출 수 있다.
// launch 의 반환형 => Job 객체

fun main() = runBlocking {
    val job = launch {
        repeat( 1000 ) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)     // delay 가 없다면, job 은 cancel 되지 않는다.
        }
    }

    delay( 1300L )
    println("main: I'm tired of waiting!")
    job.cancel()    // job 의 coroutine 을 즉시 종료한다.
    job.join()      // job 이 종료될 때 까지 기다린다.
                        // job 이 runBlocking {} 과 다른 scope 에서 동작한다면,
                        // job 은 모든 작업을 마치지 못하고, 현재 runBlocking {}에 속한 scope가 종료되버린다.
    println("main: now I can quit.")

    // 결과
    // job: I'm sleeping 0 ... [main]
    // job: I'm sleeping 1 ... [main]
    // job: I'm sleeping 2 ... [main]
    // main: I'm tired of waiting! [main]
    // main: now I can quit. [main]

}