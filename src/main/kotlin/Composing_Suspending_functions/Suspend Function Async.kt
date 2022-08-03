package Composing_Suspending_functions

import doSomethingUsefulOne
import doSomethingUsefulTwo
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// GlobalScope 에서 각각의 코루틴을 나눠서 실행하지 말고..
// 모든 코루틴 실행을 Coroutine Scope 로 하나로 감싸서, 사용하자.
// 코루틴 실행 중 exception 이 발생되면, exception 이 전파되면서 Coroutine Scope 내에 코루틴이 전부 취소된다.

// ==> GlobalScope 에서 독립적으로 사용하면 exception 이 핸들링이 되지 않으므로.


fun main() = runBlocking<Unit> {
    val time = measureTimeMillis {
        println("The answer is ${concurrentSum()}")
    }
    println("Completed in $time ms")
}


suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }

    // 임의로 Exception 을 발생시켜보자 => Exception 이 발생하면 coroutineScope { } 내에 있는 코루틴들이 모두 실행되지 않고 취소가 된다.
    delay(10)
    println("Exception")
    throw Exception()

    // 반환 값
    one.await() + two.await()
}


// Coroutine Scope 와 Global Scope
// GlobalScope -> 앱이 처음 시작부터 종료 할때까지 하나의 CoroutineContext 안에서 동작하도록 할 수 있다.

// 필요할 때 선언하고, 종료하자 -> CoroutineScope 사용
// 매번 새롭게 생성하는 CoroutineScope을 활용함으로써 효율을 높일 수 있다.

// 당연히 앱을 사용하면서 장시간 동작해야 할 thread가 필요하다면 매번 생성하는 CoroutineScope보다는
// 최초 접근 시 만들어지는 GlobalScope이 효율적이다.