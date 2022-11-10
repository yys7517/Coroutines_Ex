package Channels

// produce 코루틴 빌더와 consumeEach 함수를 사용하여 이 패턴을 정확히 구현할 수 있다.
// 두 함수를 사용하면 Channel 객체를 명시적으로 만들지 않아도 된다.

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking

/*
    producer( Coroutine Builder )

    public fun <E> CoroutineScope.produce(
    context: CoroutineContext = EmptyCoroutineContext,
    capacity: Int = 0,
    @BuilderInference block: suspend ProducerScope<E>.() -> Unit): ReceiveChannel<E>
    {
        val channel = Channel<E>(capacity)
        val newContext = newCoroutineContext(context)
        val coroutine = ProducerCoroutine(newContext, channel)
        coroutine.start(CoroutineStart.DEFAULT, coroutine, block)
        return coroutine
    }

    반환형 : ReceiveChannel 객체. ( return coroutine ==> Why? Coroutine Builder )
 */

private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) send(x * x)     // 값을 하나씩 produce..
}

fun main() = runBlocking {
    val squares = produceSquares()
    squares.consumeEach { println(it) }             // 값을 하나씩 consume..
    println("Done!")

    // squares.consumeAsFlow()
}

/*
    consumeEach => Channel 의 요소를 하나씩 방문하는 함수. but suspend fun. 중단 함수이다.

    public suspend inline fun <E> ReceiveChannel<E>.consumeEach(action: (E) -> Unit) =
    consume {
        for (e in this) action(e)
    }

    사용법 : [ ReceiveChannel 객체 ].consumeEach{ }


    consumeAsFlow 을 사용하면 데이터를 Flow 형식으로 받는 것도 가능하다.
    public fun <T> ReceiveChannel<T>.consumeAsFlow(): Flow<T> = ConsumeAsFlow(this)

    하나의 Flow가 배출되므로, Channel에서 한 개의 값이 배출 되는 것과 같다.
    따라서, Channel 처럼 여러 값을 여러 곳에서 받는 것이 불가능하다.
    Flow는 Channel에서의 한 개의 값으로 취급. ( ex) Channel<Flow<String>> )

    출처: https://two22.tistory.com/23?category=1134189 [루크의 코드테라피:티스토리]
 */