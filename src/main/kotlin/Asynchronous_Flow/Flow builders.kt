package Asynchronous_Flow

// Flow builders
// Flow 생성 방법 - Flow Builders 를 사용하여 생성 가능.
// 가장 기본적인 빌더인 flow { ... }
// 고정된 값 집합을 생성하는 flowOf
// asFlow() 함수를 사용하여 다양한 collection 과 sequence 를 flow 로 변환할 수 있다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


private fun getNumbers() : Flow<Int> = flow {   // 1 ~ 200 의 값을 담은 Flow
    for( i in 1..100 ) {
        emit(i)
        println("Emit $i")
    }
    // public suspend inline fun <T> FlowCollector<T>.emitAll(flow: Flow<T>) = flow.collect(this)
    emitAll((101..200).asFlow())
    // emitAll 은 Flow 를 Parameter 로 받는다. Parameter Flow 의 데이터를 Collect 하면서(flow.collect(this)) 모두 반환될 Flow 에 emit 한다.
}

fun getStr() : String = "String"
fun getFlowStr() : Flow<String> = ::getStr.asFlow()     // getStr() 의 값을 flow 로 변환.

suspend fun getSuspendStr() : String = "String"
fun getFlowSuspendStr() : Flow<String> = ::getSuspendStr.asFlow()   // getSuspendStr() 의 값을 flow 로 변환.
fun main() = runBlocking {

    // asFlow() - 다양한 collection 과 sequence 를 flow 로 변환할 수 있다.
    // array, iterator, range 등을 flow로 변환할 수 도 있다.
    // 출처: https://two22.tistory.com/16 [루크의 코드테라피:티스토리]
    getNumbers()
        .collect { value -> println(value) }
    getFlowStr()
        .collect { value -> println(value) }
    getFlowSuspendStr()
        .collect { value -> println(value) }

    (1..3).asFlow()     // range
        .collect { value -> println(value) }

    // map + asFlow()
    mapOf(Pair(3, 4)).asIterable().asFlow()  // iterator (asIterable())
        .collect { value -> println(value) }

    flowOf(1, "213", 5, 3, 3.0f)  // flowOf - 고정된 값의 집합을 flow 로 만들어준다.
        .collect { value -> println(value) }
}
