package Channels

// 개념적으로 close()는 채널에 특별한 close Token 을 보내는 것과 같습니다.
// 이 Close Token 이 수신되는 즉시 반복이 중지되므로
// Channel 이 닫히기 전에 이전에 전송된 모든 요소가 수신된다는 보장이 있습니다.

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking {
    val channel = Channel<Int>()

    launch {
        for (x in 1..5) channel.send(x * x)
        channel.close() // 더 이상 보낼 값이 없음, close
    }

    // Channel basic 예제와 같이 횟수를 정하여 값을 받는 대신
    // Channel 에서 값이 몇 개나 올 지 모르기 때문에 range-based for 문을 이용하여 값을 받는 것이 좋다.

    // 일반 for 루프를 사용하여 Channel 로부터 요소를 수신할 수 있다.
    for (y in channel) println(y)

    println("Done!")
}