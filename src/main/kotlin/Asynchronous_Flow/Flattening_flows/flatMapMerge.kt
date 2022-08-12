package Asynchronous_Flow.Flattening_flows

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*

// 우리가 Flow 를 처리하는 과정에서, Flow 에서 collect 되는 element 들 또한 Flow 형태로 collect 될 수 있다.
// 우리는 추가적인 처리를 위해 하나의 Flow 로 평탄화하여야 한다.

// flattenMerge 와 flatMapMerge
// 이 두 연산자는 모두 concurrency 값을 통해 동시에 수집 가능한 Flow 의 개수를 제한할 수 있다.

// 출처: https://two22.tistory.com/17?category=1134189 [루크의 코드테라피:티스토리]

private fun requestFlow( i : Int ) : Flow<String> = flow {
    emit("$i : First")
    delay(100)
    emit("$i : Second")
}

fun main() = runBlocking<Unit> {

    val mappedFlow: Flow<Flow<String>> = (1..3).asFlow()
        .onEach { delay(100) }
        .map { requestFlow(it) }
    // Flow [ 1,2,3 ] map { requestFlow(it) } => Flow [ Flow<String>, Flow<String>, Flow<String> ]

    /*
        flattenMerge

        public fun <T> Flow<Flow<T>>.flattenMerge(concurrency: Int = DEFAULT_CONCURRENCY): Flow<T>
        {
            require(concurrency > 0) { "Expected positive concurrency level, but had $concurrency" }
            return if (concurrency == 1) flattenConcat() else ChannelFlowMerge(this, concurrency)
        }

        concurrency(동시성) 값은 받지 않으면, 기본 값으로는 DEFAULT_CONCURRENCY 가 된다.
        concurrency 는 동시에 수집하는 Flow 의 개수를 의미하는 것 같다.
        Flow 의 개수가 1개일 때, flattenConcat() 연산을 수행한다.
     */

    // flattenMerge
    mappedFlow
        .flattenMerge()
        .collect { value ->
            println(value)
        }

    /*
        flatMapMerge

        public fun <T, R> Flow<T>.flatMapMerge( concurrency: Int = DEFAULT_CONCURRENCY
                                                ,transform: suspend (value: T) -> Flow<R>) : Flow<R>
                                                = map(transform).flattenMerge(concurrency)


        transform 형식에 따라 Mapping 을 진행 후, flattenMerge 연산을 수행하는 것이 flatMapMerge 이다.
     */
    // flatMapMerge
    // flatMapMerge 는 응답인 Flow의 완료 여부와 상관없이 다음 단계를 동시에 진행한다.
    // 생성자에서 몇개까지 동시에 진행할지에 대한 개수를 지정할 수 있다.

    val startTime = System.currentTimeMillis()
    (1..3).asFlow()
        .onEach { delay(100) }
        .flatMapMerge { requestFlow(it) }
        .collect { value ->
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }

    println("concurrency : CONCURRENCY_DEFAULT")
    (1..3).asFlow()
        .flatMapMerge { requestFlow(it) }
        .collect { value ->
            delay(300)
            println(value)
        }

    // concurrency : CONCURRENCY_DEFAULT    => 모든 Flow 가 동시에 Collect 가 진행되었다.
    // 1 : First
    // 2 : First
    // 3 : First
    // 1 : Second
    // 2 : Second
    // 3 : Second


    println("concurrency : 2")
    (1..3).asFlow()
        .flatMapMerge(2) { requestFlow(it) }
        .collect { value ->
            delay(300)
            println(value)
        }

    // concurrency : 2       => 2개의 Flow 가 동시에 Collect 가 진행되었다.
    // 1 : First
    // 2 : First
    // 1 : Second
    // 2 : Second
    // 3 : First
    // 3 : Second


}
