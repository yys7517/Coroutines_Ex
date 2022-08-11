package Asynchronous_Flow

// Flow Context
// Flow 의 collect는 항상 Flow 를 호출한 코루틴의 Context 안에서 수행됩니다.
// 이러한 Flow 의 특성을 context preservation 이라고 한다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun log(msg:String) = println("[${Thread.currentThread().name}] $msg")
fun flowContextTest() : Flow<Int> = flow {
    log("Started flow")
    for( i in 1..3 ) {
        emit(i)
    }
}

fun main() = runBlocking {
    // 그러므로 기본적으로 flow { ... } 빌더에 제공된 코드 블록은 플로우 collect 연산을 실행한 코루틴의 컨텍스트에서 수행됩니다.

    flowContextTest().collect { value -> log("Collected $value") }  // collect 연산을 main 스레드에서 호출하였기 때문에

    // 결과
    // [main] Started flow  => flow 의 코드 블럭( log("Started flow") ) 또한 main 스레드에서 호출된다.
    // [main] Collected 1
    // [main] Collected 2
    // [main] Collected 3
}