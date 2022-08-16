package Asynchronous_Flow.Flow_Exception

import Asynchronous_Flow.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private fun getFlow(): Flow<Int> =
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // emit next value
        }
    }
/*
fun main() = runBlocking<Unit> {

    // "catch 연산자는 중간 연산자로써, upstream 에서 발생하는 예외들에 대해서만 동작"하며
    // downstream 에서 발생한 예외에 대해서는 처리하지 않습니다.
    getFlow()
        .catch { e ->
            // does not catch downstream exceptions
            println("Caught $e")
        }
        .collect { value ->
            check( value <= 1 ) { "Crashed on $value"}  // check 조건을 만족하지 못하면, downstream 에서 예외가 발생.
            println(value)
        }
}
 */

// 중간 연산자 -> map, filter, onEach .... + catch

// 위 Collect 연산에서 check 를 사용하면 downstream 에서 예외를 처리하지 못하는 상황이 발생한다.
// 따라서 Collect 연산자 바디를 onEach 로 옮긴다면, 예제에서 발생하는 모든 예외를 처리할 수 있게 된다.
fun main() = runBlocking<Unit> {
    getFlow()
        .onEach { value ->          // onEach 도 중간 연산자.
            check(value <= 1) { "Crashed on $value" }
            // check 조건을 만족하지 못하면, onEach 블럭이므로, upstream 에서 예외가 발생.
            println(value)
        }
        .catch { e -> println("Caught $e") }       // "catch 연산자는 중간 연산자로써, upstream 에서 발생하는 예외들에 대해서만 동작"
        .collect()
}