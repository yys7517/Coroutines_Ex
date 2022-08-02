package Coroutine_Context_and_Dispatchers

// Combining context elements

// Coroutine Context 로 여러 개의 요소들을 정의하고 싶을 때는
// context 부분에 + 연산을 사용할 수 있다.

// 요소들이 더해져서 하나의 Coroutine Context가 된다.

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    // public operator fun plus(context: CoroutineContext)
    launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }
}