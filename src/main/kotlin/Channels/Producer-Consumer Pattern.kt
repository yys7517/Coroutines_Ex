package Channels

// produce 코루틴 빌더와 consumeEach 함수를 사용하여 이 패턴을 정확히 구현할 수 있다.
// 두 함수를 사용하면 Channel 객체를 명시적으로 만들지 않아도 된다.

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

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
}

/*
    consumeEach => Channel 의 요소를 하나씩 방문하는 함수. but suspend fun. 중단 함수이다.

    public suspend inline fun <E> ReceiveChannel<E>.consumeEach(action: (E) -> Unit) =
    consume {
        for (e in this) action(e)
    }

    사용법 : [ ReceiveChannel 객체 ].consumeEach{ }
 */