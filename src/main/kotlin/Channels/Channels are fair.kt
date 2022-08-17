package Channels

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Channel들은 평등하다.
// 이 전 예제들에서 살펴보았듯이 Channel은 Stack과 같은 FIFO(First-In-First-Out)구조로 작동한다.
// 먼저 값을 요청한 코루틴이 값을 받게 되는 것이다.

data class Ball(var hits : Int)

fun main() = runBlocking {
    val table = Channel<Ball>()   // 공유 table

    // player가 suspend fun이므로..
    // 거의 동시에 2개의 코루틴이 동시에 실행이 되지만, ping이 먼저 실행이 되고, pong은 공을 받을 때까지 기다리게 된다.
    launch { player("ping", table) }
    launch { player("pong", table) }

    table.send(Ball(0))
    delay(1000)
    coroutineContext.cancelChildren()
}
// Result
// ping Ball(hits=1)
// pong Ball(hits=2)
// ping Ball(hits=3)
// pong Ball(hits=4)
// ping Ball(hits=5)
// pong Ball(hits=6)
// ping Ball(hits=7)
// pong Ball(hits=8)
// ping Ball(hits=9)
// pong Ball(hits=10)


suspend fun player(name : String, table : Channel<Ball>) {
    for (ball in table) {   // table 의 ball 을 수신한다.
        ball.hits++         // ball 의 hits 값 증가.
        println("$name $ball")
        delay(100)
        table.send(ball)    // hits 값이 증가된 ball 을 send.
    }
}