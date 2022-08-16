package Asynchronous_Flow.Flow_Completion

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// onCompletion, catch 는 모두 Upstream 에서 전달되는 에외만 식별하고 처리할 수 있다.
// DownStream 의 예외는 알 수 없으며 처리할 수 없다.

private fun getFlow(): Flow<Int> = (1..3).asFlow()

fun main() = runBlocking<Unit> {
    getFlow()
        .onCompletion { cause ->
            if( cause != null ) println("Flow completed exceptionally") // cause != null -> Completed Exceptionally
            else println("Flow completed successfully")                   // cause == null -> Successful Completion
        }
        .collect { value ->
            check(value <= 1) { "Crashed on $value" }
            println(value)
        }

    //

    // 하지만 위 예제에서는 onCompletion 의 파라미터 값은 null 이지만 -> upstream 에서 예외는 발생하지 않았다.
    // downStream 에서 발생한 예외로 Flow 가 중단되었음을 알 수 있다.
    // Exception in thread "main" java.lang.IllegalStateException: Crashed on 2
}