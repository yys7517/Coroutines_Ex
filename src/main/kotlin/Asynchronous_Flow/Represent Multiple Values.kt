package Asynchronous_Flow

import kotlinx.coroutines.*
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

// Suspend 함수는 단일 값이 비동기적으로 반환되지만,
// 비동기식으로 계산된 값이 여러 개라면 어떻게 반환해야할까요 ? => Flows

// fun foo() : List<Int> = listOf(1,2,3)        // 리스트를 반환하는 foo()
// fun main() = foo().forEach { println(it) }  // forEach 를 사용하여 리스트의 각각의 값을 사용할 수 있다.

// 우리가 많은 숫자들을 CPU 를 많이 소모하는 blocking 코드로 계산한다면 ?
// (각각 100ms가 소요됩니다)
// sequence 를 사용하여 표현할 수 있다.
/*fun foo() : Sequence<Int> = sequence { // sequence builder
    for( i in 1..3 ) {
        Thread.sleep(100)           // 메인 스레드를 차단하는 코드.
        yield(i)                    // i 값 내보내기
    }
}   // => [return] Sequence<Int> (1,2,3)

fun main() = foo().forEach { println(it) }  // sequence 를 forEach 로 방문할 수 있다.*/

// 위 코드는 메인 스레드를 차단하는 코드이지만, 위 작업을 비동기로 진행하기 위해 foo 함수에 suspend 한정자를 표시한다면,
// 메인 스레드를 차단하지 않고 작업을 수행할 수 있다.
/*
suspend fun foo() : List<Int> {
    delay(1000)     // list 에 값을 입력.
    return listOf(1,2,3)
}

fun main() = runBlocking {

    launch {

        // foo가 suspend 함수이므로 메인 스레드를 차단하지 않고, 중단-재개 형식으로 작업을 진행할 수 있다.
        foo().forEach{ value -> println(value) }

        // foo 에 대한 작업을 마쳤으면, 다시 메인 스레드의 리소스를 돌려 받아 launch 1 ~ 3이 실행된다.
        for ( k in 1..3 ) {
            println("launch $k")
        }
    }

    delay(1000)
}
 */

// Flows
// Flow 타입에 대한 builder 함수를 flow 라고 한다.
// Flow를 반환하는 foo() 함수는 suspend 한정자를 표시하지 않았다.
// flow {…} 의 builder 블록 내부에서 suspend 함수를 사용할 수 있다.

// Flow 형태의 값을 반환하는 함수처럼 보이지만 그 결과 또한 리스트로 변환할 수 있다.
// Flow 는 데이터 스트림이며, emit 을 통해 데이터를 발행한다.
fun foo() : Flow<Int> = flow {
    for( i in 1..3 ) {
        delay(100)     // flow {…} 의 builder 블록은 suspend 한정자를 지정하지 않아도 suspend 함수 delay 를 사용할 수 있다.
        emit(i)                   // 데이터 발행.
    }
}

fun main() = runBlocking {
    // 동시에 다른 코루틴을 launch 로 실행하여 메인 스레드가 블록되지 않음을 확인할 수 있다.
    launch {
        for ( k in 1..3 ) {
            println("I'm not Blocked $k")
            delay(100)
        }
    }
    // Flow의 emit() 에 의해 발행된 값을 collect 를 이용해서 소비할 수 있다.
    foo().collect { value -> println( value )  }    // 데이터 소비.

}

