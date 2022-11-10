package Coroutine_Context_and_Dispatchers

// 부모 Coroutine 의 책임.

// 부모 Coroutine 은 자식 Coroutine 의 실행이 모두 완료되기를 기다립니다.
// 명시적으로 자식 Coroutine 을 추적할 필요가 없으며,
// Job 의 join() 과 같은 행위를 하지 않아도 된다.

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    val request = launch {

        repeat(3) { i -> // 3개의 자식 launch
            launch {
                delay((i + 1) * 200L) // 가변적인 지연 200ms, 400ms, 600ms
                println("Coroutine $i is done [${Thread.currentThread().name}]")
            }
        }

        println("request: I'm done and I don't explicitly join my children that are still active [${Thread.currentThread().name}]")
    }

    // request: I'm done and I don't explicitly join my children that are still active [main]
    // Coroutine 0 is done [main]
    // Coroutine 1 is done [main]
    // Coroutine 2 is done [main]

    // 위 결과처럼 부모 코루틴 request 는 3개의 자식 코루틴의 실행이 모두 완료되기를 기다린다.

    request.join() // main 스레드가 일시중단. request 의 실행이 완료되기를 기다린다.

    println("Now processing of the request is complete [${Thread.currentThread().name}]")
    // Now processing of the request is complete [main]
}