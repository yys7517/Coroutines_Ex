package Coroutine_Context_and_Dispatchers

import kotlinx.coroutines.*

// Thread 를 스위칭 하는 예제.
// withContext( Dispatcher parameter ) : coroutine 이 실행되는 context 를 변경할 수 있다.

fun main() {

    val ctx1Thread = newSingleThreadContext("Ctx1")
    ctx1Thread.use { ctx1 ->

        val ctx2Thread = newSingleThreadContext("Ctx2")
        ctx2Thread.use { ctx2 ->
            runBlocking(ctx1) {

                log("Started in ctx1")

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