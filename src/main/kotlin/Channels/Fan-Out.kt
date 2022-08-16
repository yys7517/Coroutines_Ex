package Channels

// Fan-Out : 분산 처리
// Channel 에서 생성되는 값을 여러 개의 코루틴이 나누어 처리할 수 있다.

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

private fun CoroutineScope.produceNumbers() = produce {
    var x = 1
    while (true) {
        send(x++)  // 생산
        delay(100) // 100ms 기다린 후 다시 생산
    }
}

private fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        println("Processor #$id received $msg")
    }
    /*
    하나의 channel 에서 생산하는 값을 여러 개의 코루틴에서 받을 때에는 for 문을 사용하는 것이 안전하다.

    Why ? => consumeEach 함수는 suspend 함수이므로, 다른 코루틴을 중단시킬 수 있다.
             for 문을 사용하면 하나의 코루틴이 중단되더라도 다른 코루틴은 계속 작동할 수 있기 때문이다.

     */
}

fun main() = runBlocking {
    val producer = produceNumbers()
    repeat(5) { count -> launchProcessor(count, producer) } // count : 0 ~ 4
    // 5번의 반복으로 5개의 id를 가진 launchProcessor 로 인해 5개의 Coroutine 이 동작된다.
    // Processor #0 ~ Processor #4

    delay(950)
    producer.cancel()   // 모든 코루틴을 중단하기 위해 channel 을 중단. producer : ReceiveChannel<Int>
}


// Result => 생산되는 값이 여러 코루틴에 분배됨을 알 수 있다.
// Processor #0 received 1
// Processor #0 received 2
// Processor #1 received 3
// Processor #2 received 4
// Processor #3 received 5
// Processor #4 received 6
// Processor #0 received 7
// Processor #1 received 8
// Processor #2 received 9
