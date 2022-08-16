package Asynchronous_Flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Flow의 요소를 Collect 할 때, try catch 블록을 사용하여 예외를 다룰 수 있습니다.
private fun getFlow(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    try {
        getFlow().collect { value ->
            check( value <= 1 ) {
                // check 조건에 맞지 않을 경우 이 코드 블럭이 실행되는 것 같다.
                // 이 코드 블럭의 마지막 구문이 반환 값이 되고, lazyMessage 가 된다.
                println("IllegalStateException!!")
                "$value is inappropriate"
            }
            println(value)
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}

/*
public inline fun check(value: Boolean, lazyMessage: () -> Any): Unit {
    contract {
        returns() implies value
    }
    if (!value) {
        val message = lazyMessage()
        throw IllegalStateException(message.toString())
    }
}

    throw IllegalStateException(message.toString())
    check 조건에 맞지 않으면 lazyMessage 값을 전달하고, IllegalStateException을 발생시킨다.
 */