package Channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Pipelines(파이프라인)
// 파이프라인은 채널을 생성하는 패턴으로
// 하나의 코루틴이 초기 데이터를 생성하고, 소비하는 곳에서 받은 후 새로운 데이터를 생성하는 흐름을 말한다.

// produce<E>{ ... } 코루틴 빌더를 사용하여 만들 수 있다.
// produce<E>{ ... } 코루틴 빌더의 반환형은 ReceiveChannel<E> 이므로, 반환형 타입을 생략해도 된다.

private fun CoroutineScope.produceNumbers()  = produce {
    var x = 1
    while(true) send(x++)   // 1부터 시작해서 무한정으로 증가하는 자연수를 send 한다.
}

private fun CoroutineScope.square( numbers : ReceiveChannel<Int> ) = produce {
    for( x in numbers ) send(x*x)       // 또 다른 정수형 Stream 값을 Parameter 로 받아, 그 수의 제곱을 send 한다.
}

// Sequence, Iterator 또한 위의 코드를 그대로 구현할 수 있으나, 내부 계산에 비동기적인 부분이 있는 경우 Channel 을 사용하는 것이 좋다.
// Sequence, Iterator 은 suspension 을 허용하지 않기 때문이다.

fun main() = runBlocking {
    val numbers = produceNumbers()
    val squares = square(numbers)

    // consumeEach 를 사용하면 횟수를 정할 수 없다 !
    /*squares.consumeEach {
        delay(1000)
        println(it)
    }*/

    repeat(5) {
        println(squares.receive())
        //
    }   // 5개의 값만

    squares.cancel()
    numbers.cancel()
    println("Done!")

    // 위 두 파이프라인(Stream)이 무한정으로 실행되기 때문에, 두 코루틴을 모두 명시적으로 종료할 필요가 있다.
    // coroutineContext.cancelChildren()   // 하위 코루틴 취소
    // coroutineContext

}
