package Asynchronous_Flow

// Buffering
// Flow 가 같은 코루틴안에서 실행되면 방출과 사용이 순차적으로 일어나고
// 이러한 동작은 전체 실행시간을 느리게 만든다.

// buffer 를 이용하면 방출과 사용이 별개의 코루틴에서 동작하도록 만들 수 있다.
// 데이터를 미리 생산해서 버퍼에 담아두고 바로 사용하기에 실행시간에 불필요한 딜레이가 사라진다.

// 굳이, buffer 를 사용하지 않아도 launchIn 으로 다른 Scope를 지정하여 실행시키거나,
// flowOn 을 통해서 방출과 사용하는 부분의 Context를 다르게 한다면 동일한 효과가 나타난다.
// 출처: https://two22.tistory.com/17?category=1134189 [루크의 코드테라피:티스토리]

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.*

fun flowOnetoThree() : Flow<Int>  = flow {
    for( i in 1..3 ) {
        delay(100)
        emit(i)
    }
} // .flowOn(Dispatchers.Default)       //flowOn 을 통해서 방출과 사용하는 부분의 Context를 다르게 한다면 동일한 효과가 나타난다.

fun main() = runBlocking {
    val time = measureTimeMillis {
        flowOnetoThree().collect { value ->
            delay(300)
            println(value)
        }
    }

    val bufferTime = measureTimeMillis {
        flowOnetoThree()
            .buffer()
            .collect { value ->
                delay(300)
                println(value)
            }
    }

    // buffer 를 이용하면 방출(emit)과 사용(collect)이 별개의 코루틴에서 동작하도록 만들 수 있다.
    // 데이터를 미리 생산해서 버퍼에 담아두고 바로 사용하기에 실행시간에 불필요한 딜레이가 사라진다.

    println("time : $time ms")              // 100 + 100 + 100 + 300 + 300 + 300 + a (ms)
    println("usingBuffer : $bufferTime ms") // 100 + 300 + 300 + 300 + a (ms)
}