package Coroutine_Context_and_Dispatchers

import kotlinx.coroutines.*

// Thread 를 스위칭 하는 예제.
// withContext( Dispatcher parameter ) : coroutine 이 실행되는 context 를 변경할 수 있다.

// withContext 를 이용하여 비동기 작업을 순차화할 수 있다.
// withContext 의 마지막 줄의 값이 반환 값이 된다.
// withContext 가 끝나기 전까지 해당 코루틴은 일시 정지된다. (join 과 await 을 대체할 수 있다)

// withContext 를 사용하면 비동기 작업을 순차화할 수 있지만,
// 비동기 작업을 사용하는 이유가 순차적인 처리를 했을 때 연산시간을 줄이기 위함인데, 순차적으로 처리하도록 만들면
// 비동기 작업의 장점이 사라진다고 볼 수 있다.

suspend fun main() {

    val result : String = withContext(Dispatchers.IO) {
        "Async Result"
    }
    // withContext 의 작업을 마칠 때 까지 main 쓰레드는 일시 중단 된다. fun main() -> suspend fun main()
    println( result )

    val ctx1Thread = newSingleThreadContext("Ctx1")
    ctx1Thread.use { ctx1 ->

        val ctx2Thread = newSingleThreadContext("Ctx2")
        ctx2Thread.use { ctx2 ->
            runBlocking(ctx1) {

                log("Started in ctx1")

                // withContext 가 끝나기 전까지 해당 코루틴은 일시 정지된다. => ctx1 은 일시정지 된다.
                withContext(ctx2) {
                    log("Working in ctx2")
                }

                log("Back to ctx1")
            }
        }

        ctx2Thread.close()
    }
    ctx1Thread.close()

}