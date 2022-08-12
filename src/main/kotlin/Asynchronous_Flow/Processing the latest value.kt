package Asynchronous_Flow

// Processing the latest value - 최신 값 처리.
// buffer(CONFLATED) 를 실행하는 conflate() 연산은, buffer() 의 동작과 유사하지만. 최신 값만 버퍼에 담아서 처리하는 점이 있었다.

// 최신 값 처리
// xxxLatest 연산자
// xxxLatest 는 xxx 연산자와 동일하게 필수적인 로직을 수행한다.
// 하지만, 새로운 값에 대해 블록안의 코드를 취소하는 경우가 있다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

private fun getNumbers() : Flow<Int> = flow {
    for( i in 1..3 ) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking {

    val time = measureTimeMillis {
        getNumbers()
            .collectLatest { value ->
                println("Collecting $value")
                delay(300)
                println("Done $value")
            }
    }

    println("Collected in $time ms")

    // collect { } - 모든 값에 대해서 끝까지 수행
    // Collecting 1
    // Done 1
    // Collecting 2
    // Done 2
    // Collecting 3
    // Done 3
    // Collected in 1248 ms

    // collectLatest { } - 마지막 값에 대해서만 끝까지 수행 됨을 확인할 수 있습니다.

    // Collecting 1
    // Collecting 2
    // Collecting 3
    // Done 3
    // Collected in 665 ms

}