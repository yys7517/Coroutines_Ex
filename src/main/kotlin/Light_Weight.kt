// Coroutine 은 light - weight 의 특징을 가지고 있다.
// Coroutine 은 같은 양의 작업을 처리하는 데에 있어서 Thread 보다는 메모리 오류를 예방할 수 있다.

import kotlinx.coroutines.*

fun main() = runBlocking {
    repeat(100_000) {
        delay(1000L)
        print(".")
    }
}

/*
fun main() = runBlocking {
    repeat(1000) {i ->
        println("I'm sleeping $i ... ")
        delay(500L)
    }

    delay(1300L)
}

위 Coroutine 의 반복을 모두 마치기 전에, 프로세스가 종료된다면, Coroutine 또한 종료가 된다.
 */
