package Asynchronous_Flow

// Flows are cold
// Flow 는 데이터 스트림이다. sequence 와 유사한 cold streams 이다.
// flow builder 내부의 코드는 flow 가 collect 될 때까지 실행되지 않습니다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun simple() : Flow<Int> = flow {
    println("Flow Started")
    for( i in 1..3 ) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking<Unit>{
    println("Calling simple...")
    val flow = simple()
    println("Calling collect...")
    flow.collect { value -> println(value)  }   // collect 될 때마다 flow{ ... } 가 실행됩니다..
    println("Calling collect again...")
    flow.collect { value -> println(value) }    // collect 될 때마다 flow{ ... } 가 실행됩니다..
}
