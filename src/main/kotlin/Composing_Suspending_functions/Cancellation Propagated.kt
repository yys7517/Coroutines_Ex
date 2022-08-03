// Cancellation Propagated (coroutines hierarchy)
// Cancellation 은 언제나 코루틴 hierarchy 를 통해 전달된다. 계층적으로 연관된 부모-자식

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch(e: ArithmeticException) {
        println("Computation failed with ArithmeticException")  // => main 에서 또한 Exception 이 발생했다는 사실을 전달받아
        // catch 에 의한 에러 핸들링이 실행되고, 종료되며 finally 블럭이 실행.
    }
}

// 간단한 연산은 GlobalScope 로 Coroutine 을 실행시켜 오류를 내서, 모든 Coroutine 을 동작 중단 시키기 보다는
// coroutineScope 를 사용하여, 오류가 발생하면, coroutineScope의 coroutines만 종료하도록 하자.
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE) // Emulates very long computation
            42
        } finally {
            println("First child was cancelled")    // ==> Exception 이 전파가 되어서, one(async) 코루틴 또한 실행이 취소되고
                                                            // catch 에 의한 에러 핸들링이 실행되고, 종료되며 finally 블럭이 실행.
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException() // ==> 임의로 ArithmeticException() 을 발생시켜보자.
    }
    one.await() + two.await()
}
