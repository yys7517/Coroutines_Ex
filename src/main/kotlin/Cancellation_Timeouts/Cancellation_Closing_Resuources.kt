// Closing resources with finally

// 코루틴이 캔슬되었을 때, resource 를 해제 해주자. -> 자원을 돌려주자.
// finally 구문에서 리소스를 해제해주자는 취지를 보여주는 예제.

import kotlinx.coroutines.*

fun main() = runBlocking {

    val job = launch {
        try {
            repeat( 1000 ) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
                // 위와 같이 delay() 와 같은 suspend fun 으로 cancel 할 때, 리소스 해제 지역은 finally 블럭이 된다.
            }
        } finally {
            // 리소스 해제 지역
            println("job: I'm running finally")
        }
    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()     // job.cancel() + job.join()
    println("main: Now I can quit.")

}
