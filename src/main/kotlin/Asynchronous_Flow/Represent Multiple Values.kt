package Asynchronous_Flow

import kotlinx.coroutines.*
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

// Suspend 함수는 단일 값이 비동기적으로 반환되지만,
// 비동기식으로 계산된 값이 여러 개라면 어떻게 반환해야할까요 ? => Flows

/*
    * Representing multiple values
    *
 */

// fun foo() : List<Int> = listOf(1,2,3)        // 리스트를 반환하는 foo()
// fun main() = foo().forEach { println(it) }  // forEach 를 사용하여 리스트의 각각의 값을 사용할 수 있다.


// 우리가 많은 숫자들을 CPU 를 많이 소모하는 blocking 코드로 계산한다면 ?
// (각각 100ms가 소요됩니다)
// sequence 를 사용하여 표현할 수 있다.
/*fun foo() : Sequence<Int> = sequence { // sequence builder

    for( i in 1..3 ) {
        Thread.sleep(100)   // Computing numbers (CPU-consuming)
        yield(i)                    // i 값 내보내기
    }

}   // => [return] Sequence<Int> (1,2,3)

fun main() = foo().forEach { println(it) }  // sequence 를 forEach 로 방문할 수 있다.*/

// foo 가 suspend 함수라면 ?
suspend fun foo() : List<Int> {
    delay(300)
    return listOf(1,2,3)
}

fun main() = runBlocking {

    launch {
        for ( k in 1..3 ) {
            println("I'm Blocked")
            delay(100)
        }
    }

    foo().forEach{ value -> println(value) }

    // 결과
    // I'm Blocked
    // I'm Blocked
    // I'm Blocked
    // 1
    // 2
    // 3
}

// Flows
// Flow 타입에 대한 builder 함수를 flow 라고 한다.
// flow {…} 의 builder 블록은 suspend 를 사용할 수 있다.
// foo() 함수는 suspend 한정자를 표시하지 않았다.

// Flow 값을 반환하는 함수처럼 보이지만 그 결과 또한 리스트로 반환할 수 있다. emit + collect
/*fun foo() : Flow<Int> = flow {
    for( i in 1..3 ) {
        delay(100)     // suspend 한정자를 사용하지 않아도 delay 함수를 사용할 수 있다.
        emit(i) // 값 내보내기     // 반환하는 flow 에 값을 방출하는 함수.
    }
}

fun main() = runBlocking {
    launch {
        for ( k in 1..3 ) {
            println("I'm not Blocked $k")
            delay(100)

        }
    }
    // emit() 에 의해 생산된 값을 collect 를 이용해서 소비할 수 있다.
    foo().collect { value -> println( value )  }

    // 결과
    // I'm not Blocked 1
    // 1
    // I'm not Blocked 2
    // 2
    // I'm not Blocked 3
    // 3
}*/

