package Asynchronous_Flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Flow
import kotlin.system.measureTimeMillis

// Conflation
// conflate() 를 사용한다.
// 내부코드 : public fun <T> Flow<T>.conflate(): Flow<T> = buffer(CONFLATED)
// buffer()를 호출하는 것은 똑같다.

// 따라서, conflate 는 buffer와 마찬가지로 방출과 사용을 다른 코루틴에서 동작하게 한다.
// buffer() 와 다른 점은 모든 데이터를 버퍼에 넣어두고 하나씩 전달하지 않고, 최신 데이터만 버퍼에 넣어두고 전달한다.

//  emitter와 collector의 처리 속도가 차이가 있을 때 성능을 높이는 방법.

private fun getNumbers() = flow {
    for( i in 1..3 ) {
        delay(100)
        emit(i)
        println("Emit $i")
    }
}

fun main() = runBlocking<Unit> {
    val time = measureTimeMillis {
        getNumbers()
            .conflate()
            .collect { value ->
                delay(300)
                println("Collected $value")
            }
    }
    println("Collected in $time ms")
    // Emit 1
    // Emit 2
    // Emit 3
    // Collected 1
    // Collected 3, why? => Collector 의 동작 시간이 매우 느리기 때문에, 버퍼에 담긴 최신 값만 처리하고 이 전 값은 drop 함으로써, 처리 속도를 높인다.
    // Collected in 744 ms
}
