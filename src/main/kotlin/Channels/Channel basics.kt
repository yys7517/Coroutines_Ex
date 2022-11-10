package Channels

// 기존에는 Deferred 값으로 코루틴 간 단일 방향으로의 값을 전달하는 방법을 제공했지만 (단일 값 전달)
// Channel 은 Stream 형태의 값을 전달하는 방법을 제공합니다.  (여러 개의 값 전달)
// Channel 은 여러 방향에서 데이터를 던지고 받는 형식으로 코루틴 끼리의 데이터를 전달하기 위한 친구이다.

// BlockingQueue 와 매우 유사하며, BlockingQueue 와 다르게 값을 전달하는 과정에서 스레드를 block 하지 않고 suspend 한다.

// Channel<...>() 함수를 통해 생성할 수 있으며,
// 데이터를 스트림에 밀어 넣을 땐 send( value )
// 스트림에서 받을 땐 receive() 를 사용하면 된다.

// send 와 receive 는 모두 suspend 함수이기 때문에, 코루틴 내부에서 호출되어야 한다.
// Channel 을 통해서 더 이상 아무 데이터도 보내거나 받지 않는다면 Channel 을 종료시켜야 한다.
// close() 를 통해서 종료시킬 수 있으며, 종료 이후에는 send, receive 사용 시 ClosedReceiveChannelException 이 발생한다.

// 출처: https://two22.tistory.com/23?category=1134189 [루크의 코드테라피:티스토리]

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking {
    val channel = Channel<Int>()

    launch {
        for( x in 1..5 ) channel.send(x*x)
    }

    repeat(5) { println(channel.receive()) }
    println("Done!")
}