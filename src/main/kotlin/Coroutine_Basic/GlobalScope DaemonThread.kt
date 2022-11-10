package Coroutine_Basic

import kotlinx.coroutines.*

fun main() = runBlocking {
// 시작
    GlobalScope.launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L)  // 지연 후 종료
}

// 위 코드에서, 1300 ms 지연 후, main 쓰레드는 종료된다.
// 하지만 GlobalScope의 코루틴이 끝나지 않아도 main 쓰레드는 종료가 될까 ? -> 종료가 된다.
// 따라서, GlobalScope의 코루틴은 프로세스를 지속시키지 못하는 점이 DaemonThread와 유사하다.

// DaemonThread - 가장 낮은 우선순위를 갖는 스레드.
// JVM 은 데몬 스레드의 종료를 기다리지 않는다.
// JVM 이 종료되는 과정에서 실행중인 데몬 스레드가 있다면 그냥 죽이고, 셧다운 작업을 진행한다.