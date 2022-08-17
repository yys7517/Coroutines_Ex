package Channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Ticker channel은 정해진 시간마다 Unit을 생산하는 특별한 Channel이다.
// 복잡한 시간 기반 produce 파이프라인과 윈도우 설정 및 기타 시간 단축 처리를 수행하는 연산자를 만드는 데 유용한 빌딩 block입니다.

fun main() = runBlocking<Unit> {
    val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) // ticker() 함수를 이용하여 채널 생성

    var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Initial element is available immediately: $nextElement") // 최초 딜레이는 없다. 즉시 사용가능하다. Unit 값이 반환된다.

    // 이후 모든 요소에 100ms 딜레이가 있다.

    nextElement = withTimeoutOrNull(50) { tickerChannel.receive() }
    println("Next element is not ready in 50 ms: $nextElement") // 50ms 만 지났으므로, receive()에서 null 이 반환된다.

    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() } // 50 + 60 = 110ms
    // 100ms가 넘게 지났으므로 Unit이 출력됨.
    println("Next element is ready in 100 ms: $nextElement")

    // 150ms 딜레이
    println("Consumer pauses for 150ms")
    delay(150)

    // 150ms가 더 지났으므로 다음 요소를 즉시 사용할 수 있음.
    nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Next element is available immediately after large consumer delay: $nextElement")

    // 'receive' 호출 사이의 일시 중지가 고려되어 다음 요소가 비교적 100ms 보다 더 빨리 도착한다는 점에 유의.
    nextElement = withTimeoutOrNull(40) { tickerChannel.receive() }
    println("Next element is ready in 40ms after consumer pause in 150ms: $nextElement")

    tickerChannel.cancel() // 더 이상 요소가 필요없음을 알림.
}