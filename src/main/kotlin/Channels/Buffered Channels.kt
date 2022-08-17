package Channels

// 지금까지 살펴본 Channel 들은 Buffer 가 없었습니다.
// Buffering 되지 않은 채널은 Producer 와 Consumer 가 서로 만날 때만 값이 전달됩니다.
// 먼저 호출되는 함수에 따라 Producer가 기다리거나 Consumer가 기다릴 수도 있다.
// send() 와 receive() 는 모두 suspend 함수이다.
// 기다리는 시간이 많아질수록 => 성능도 나빠질 것.

// 따라서 채널에 Buffer를 사용해보자.
// Buffer를 사용하면 Buffer가 꽉 찰 때까지 값을 계속 보낼 수 있다. Buffer가 꽉 차면 send하지 않고 suspend된다.
// 따라서 Producer가 기다리는 일이 줄어들 것이다. Channel() 또는 CoroutineScope.produce() 에서 capacity 매개변수에 값을 지정해주면 된다.

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*


fun main() = runBlocking<Unit> {
    val channel = Channel<Int>(4) // 크기가 4인 버퍼 채널 생성
    val sender = launch {
        repeat(10) {
            try {
                println("Sending $it") // 각 요소를 송신하기 전에 출력
                channel.send(it)       // 버퍼가 가득차면 자동으로 중단  JobCancellationException
            }catch ( e : Exception ) {
                println("Exception : $e")
                return@launch
                // Exception : kotlinx.coroutines.JobCancellationException: Job was cancelled; job=StandaloneCoroutine{Cancelling}@528931cf
            }
        }
    }
    // 아무것도 수신하지 않고 기다린다
    delay(1000)
    sender.cancel() //송신 코루틴 취소
}
