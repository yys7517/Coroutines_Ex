package Coroutine_Context_and_Dispatchers

import kotlinx.coroutines.*
// Coroutine 이 어떤 동작을 언제, 어디서 하는 지 찾아내기 어려울 수 있다.
// 이러한 경우 Debugging 을 통해 알아낼 수 있다.
// 실행 환경 구성을 편집하여 JVM Option 에 "-Dkotlinx.coroutines.debug" 를 적용해주도록 하자.

fun log( msg : String ) = println("[${Thread.currentThread().name}] $msg")

fun main() = runBlocking {
    val a = async {
        log("I'm computing a piece of the answer")
        6
    }

    val b = async {
        log("I'm computing another piece of the answer")
        7
    }

    log("The answer is ${a.await() * b.await()}")

    // [main @coroutine#2] I'm computing a piece of the answer
    // [main @coroutine#3] I'm computing another piece of the answer
    // [main @coroutine#1] The answer is 42
}