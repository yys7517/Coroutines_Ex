// 코루틴(coroutine) - 협동 루틴
// 코루틴의 Co는 "With" 또는 "Together"을 뜻한다.

// 이전에 자신의 실행이 마지막으로 "중단" 되었던 지점 다음의 장소에서 실행을 다시 "재개"한다.
// 비동기 처리를 간단하게 만들어준다.
// Main thread 가 blocking 되는 점을 관리할 수 있게 도움을 준다.

// 비동기 처리를 위한 "긴 콜백 처리 작업"들을 "순차적인 코드로 변환"해주는 특징이 있다.

// 가장 많이 사용되는 3가지 JetPack 라이브러리 LifeCycle, WorkManager, Room 에 사용되고 있다.

// coroutine builder - launch, async, runBlocking
// runBlocking 은 새로운 코루틴을 실행하고, 완료되기 전까지 현재 스레드를 블로킹합니다.
// launch 는 Job 객체를 반환하고, async 는 Deferred 통하여 결과값을 반환한다.
// builder 를 사용하려면 coroutine scope 가 필요하다.
// suspend fun 을 사용하려면 coroutine scope 안에서 사용해야 한다.

import kotlinx.coroutines.*

/*
    fun main() {
        GlobalScope.launch {
            delay(1000L)
            println("World!")
        }

        println("Hello")
        // Thread.sleep(2000)
        runBlocking {
            delay(2000L)
        }
    }

   위 코드는 어차피 main 스레드에서는 작업이 없으므로, main 함수 안 코드를 모두 runBlocking { }으로 묶을 수 있다.
 */

fun main() {
    runBlocking {
        GlobalScope.launch {
            delay(1000L)
            println("World!")
        }

        println("Hello")
        delay(2000L)
    }
}

/*  표현식 형태
fun main() = runBlocking {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello")
}

 */
