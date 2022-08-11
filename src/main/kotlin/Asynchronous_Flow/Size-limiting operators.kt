package Asynchronous_Flow

// Size-limiting Operators
// take - 크기를 제한하는 중간 연산자
// take 의 parameter 에 해당하는 count 값을 모두 소모하게 되면, flow 의 실행을 취소한다.
/*
    take 에 대해 알아보자.
    *****
    public fun <T> Flow<T>.take(count: Int): Flow<T> {
        require(count > 0) { "Requested element count $count should be positive" }
        return flow {
            var consumed = 0
            try {
                collect { value ->
                    emit(value)
                    if (++consumed == count) {
                        throw AbortFlowException()
                    }
                }
            } catch (e: AbortFlowException) {
                // Nothing, bail out
            }
        }
    }
    ****

    SUMMARY.
    consumed 의 값이 collect 를 진행하면서 증가된다.
    take 의 parameter 로 지정된 count 값이 consumed 의 값과 같아질 때, AbortFlowException() 을 발생시키며, flow 를 취소한다.
 */

// ""코루틴의 취소는 항상 예외를 던져 취소"" 되므로, try{...} catch{...} finally{...} 또한 정상적으로 작동하며,
// finally {...} 블럭에서 리소스를 모두 반환해주는 것이 좋다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun numbers() : Flow<Int> = flow {
    try{
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}

fun main() = runBlocking {
    numbers()
        .take(2)
        .collect { value -> println(value) }

    // 결과
    // 1
    // 2
    // Finally in numbers - numbers() 가 종료되었다는 뜻.
}
