package Asynchronous_Flow.Flattening_flows

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*

// 우리가 Flow 를 처리하는 과정에서, Flow 에서 collect 되는 element 들 또한 Flow 형태로 collect 될 수 있다.
// 우리는 추가적인 처리를 위해 하나의 Flow 로 평탄화하여야 한다.

// Concatenating(연결)모드는 flatMapConcat, flattenConcat 연산자들을 사용하여 구현한다.

private fun requestFlow( i : Int ) : Flow<String> = flow {
    emit("$i : First")
    delay(500)
    emit("$i : Second")
}

fun main() = runBlocking<Unit> {

    /*
        flattenConcat

        public fun <T> Flow<Flow<T>>.flattenConcat(): Flow<T> = flow {
            collect { value -> emitAll(value) }
        }

        Flow<Flow<T>>.flattenConcat() 을 사용하면
        Flow 속의 Flow 꼴을 하나의 Flow 로 반환해준다.

        Flow 속의 Flow 들 각 각 collect 하여 모두 하나의 반환될 Flow 에 emitAll 한다.
        하나의 Flow 가 반환된다.
     */

    // flattenConcat
    (1..3).asFlow()
        .map { value ->  requestFlow(value) } // Flow [ 1,2,3 ] map { requestFlow(it) } => Flow [ Flow<String>, Flow<String>, Flow<String> ]
        .flattenConcat()
        .collect { value -> println(value) }


    /*
        flatMapConcat

        public fun <T, R> Flow<T>.flatMapConcat(transform: suspend (value: T) -> Flow<R>): Flow<R> = map(transform).flattenConcat()

        flatMapConcat 은 flow 를 전달받은 parameter 형태로 Mapping(변환) 하고, 그 flow 를 flattenConcat() 을 진행하는 함수.
        parameter 는 flow 를 변환할 수 있는 transform 을 parameter로 받는다.
     */

    // flatMapConcat - 전달받은 parameter 형태로, flow 를 mapping 하고, 그 flow 를 flattenConcat() 을 진행하는 함수.
    val startTime = System.currentTimeMillis()
    (1..3).asFlow()
        .onEach { delay(100) }
        .flatMapConcat{ num -> requestFlow(num) }// 전달받은 parameter 형태로, flow 를 mapping 하고, 그 flow 를 flattenConcat() 을 진행하는 함수.
        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }

    // 두 실행 결과, 수집되는 Flow 의 요소들은 모두 같다. (flattenConcat, flatMapConcat)
}
