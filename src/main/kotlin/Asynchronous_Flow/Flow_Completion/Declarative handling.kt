package Asynchronous_Flow.Flow_Completion

// Declarative Handling
// onCompletion 중간 연산자를 사용하면 선언형으로 코드를 작성하여 Flow Completion 을 다룰 수 있다.

// onCompletion 의 장점은 위와 같이 선언형이라는 점에서 그치지 않는다.
// 가장 큰 장점으로는, onCompletion 이 받는 람다의 nullable 한 Throwable 파라미터고 할 수 있다.
// onCompletion 이 받는 람다의 파라미터 Throwable 이 null 이면, 완전하게 수행되었음을 알 수 있고
// null 이 아니라면, 예외가 발생하면서 수행되었음을 알 수 있다.

// onCompletion { cause : Throwable? }
// cause == null -> Successful Completion
// cause != null -> Completed Exceptionally

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.RuntimeException

private fun getFlow() : Flow<Int> = flow {
    emit(1)
    throw RuntimeException()
}

fun main() = runBlocking {
    getFlow()
        .onCompletion { cause ->
            if( cause != null ) println("Flow completed exceptionally") // cause != null -> Completed Exceptionally
            else println("Flow completed successfully")                 // cause == null -> Successful Completion
        }   // onCompletion 에서는 예외를 처리하지 않고, downstream 방향으로 흘러간다.
        .catch { cause -> println("Caught Exception : $cause") }  // catch 블럭에서 예외를 받아 처리할 수 있다.
        .collect { value -> println(value) }
}