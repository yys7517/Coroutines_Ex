package Asynchronous_Flow

// Flow Cancellation
// Flow 는 코루틴의 일반적인 취소 메커니즘을 따른다.
// 하지만 Flow 에서는 취소와 관련된 함수는 따로 제공하지 않는다.

// Cancellable 한 Suspend 함수(async, launch(job), Timeout 함수)에서
// flow 가 Suspended 상태일 때, flow 수집을 취소할 수 있다.
// 이외에 다른 방법은 없다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun suspendedFlow() : Flow<Int> = flow {
    for( i in 1..3 ) {
        delay(100)  // flow 가 Suspended 상태 ( delay - suspend fun )
        println("Emitting $i")
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    withTimeoutOrNull( 250 ) {  // Cancellable 한 Suspend 함수 ( withTimeoutOrNull => Timeout 함수 )
        // 250ms 이후에는 시간 초과.
        suspendedFlow().collect { value -> println(value) }
    }
    println("Done")
    // 결과 - 3까지 출력되지 못하고 종료되었다. => Flow 가 모두 수집(collect)되지 못했다.
    // Emitting 1
    // 1
    // Emitting 2
    // 2
    // Done

    // Flow Cancellation ( Job is Cancellable )
    val job = launch {
        (1..3).asFlow().collect { value ->
            println(value)
        }
    }
    job.cancelAndJoin()
}

