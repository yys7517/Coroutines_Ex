package Channels

// Fan-In : 역 분산 처리이다.
// 여러 개의 코루틴이 하나의 Channel 에 값을 보낼 수 있다.

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

private suspend fun sendString(channel : SendChannel<String>, s : String, time : Long ) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}

fun main() = runBlocking {
    val channel = Channel<String>()
    launch { sendString(channel, "foo", 200L) }
    launch { sendString(channel, "BAR!", 500L) }
    repeat(6) {
        println(channel.receive())
    }
    coroutineContext.cancelChildren()
}