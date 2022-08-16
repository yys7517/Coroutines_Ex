package Asynchronous_Flow.Flow_Exception

// Everything is caught(모든 예외 처리)
// 이 예제는 Emit 로직이나 중간 혹은 종단 연산자에서 발생하는 모든 예외를 잡아냅니다.
// 중간 연산자 - map, filter, onEach ...
// 종단(terminal) 연산자 - collect, toList, toSet, first, reduce, fold ...

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private fun getFlow(): Flow<String> =
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // emit next value
        }
    }.map { value ->
            check(value <= 1) { "Crashed on $value" }
            "string $value"
    }

fun main() = runBlocking<Unit> {
    try {
        getFlow().collect { value -> println(value) }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}