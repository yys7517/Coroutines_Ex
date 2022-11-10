// Coroutine 은 light - weight 의 특징을 가지고 있다.
// Coroutine 은 같은 양의 작업을 처리하는 데에 있어서 Thread 보다는 메모리 오류를 예방할 수 있다.

import kotlinx.coroutines.*

fun main() = runBlocking {
    repeat(100_000) { // launch a lot of coroutines
        launch {
            delay(5000L)
            print(".")
        }
    }
}