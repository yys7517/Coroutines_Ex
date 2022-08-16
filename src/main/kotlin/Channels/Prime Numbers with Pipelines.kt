package Channels

// Coroutine의 Pipeline을 사용하여 소수를 생성하는 예제.

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

private fun CoroutineScope.numbersFrom( start : Int ) = produce {
    var x = start
    while (true) send(x++)
}

private fun CoroutineScope.filter
            ( numbers : ReceiveChannel<Int>, prime : Int ) = produce {
    for( x in numbers )  {
        if( x % prime != 0 )
            send(x)
    }
}

// 소수 - 1과 자기자신으로만 나누어지는 수.
// 10개의 소수값을 출력 해보자.
fun main() = runBlocking {
    var cur = numbersFrom(2)    // 소수는 1보다 큰 양의 정수이어야 한다. 2는 첫 번째 소수이다.

    repeat(10) {
        val prime = cur.receive()
        println(prime)
        cur = filter(cur, prime)
    }

    coroutineContext.cancelChildren()   // 무한정으로 코루틴이 실행되므로, 하위 코루틴들을 취소시켜준다.

    // 동작순서
    // 실행 초기에 cur = ReceiveChannel<Int>{ 2 3 4 5 6 7 8 9 .... } 무한대의 집합이다.
    // receive()를 통해 제일 앞에 있는 값을 받아 prime 에 저장하고, prime 을 출력해준다.
    // 제일 앞에 있는 소수 값이 빠진 cur, 최근 알아낸 소수 prime 을 파라메터로 넘겨주게 되면,
    // 최근 알아낸 소수 prime 으로 나눠지지 않는 수의 집합으로 계속 갱신이 된다.

    // ex) ReceiveChannel<Int>{ 3 4 5 6 7 8 9 .... }, prime = 2 를 parameter 로 넘겨준다.
    // cur = { 3 5 7 9 11 13 ... } 짝수들의 집합이 된다. 하지만 9와 15 등등 소수가 아닌 수들은 이 과정들을 계속 반복하면
    // 소수들을 찾아낼 수 있다.
}