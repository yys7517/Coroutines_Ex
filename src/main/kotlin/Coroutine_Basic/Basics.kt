// 코루틴(coroutine) - 협동 루틴
// 코루틴의 Co는 "With" 또는 "Together"을 뜻한다.

// 이전에 자신의 실행이 마지막으로 "중단" 되었던 지점 다음의 장소에서 실행을 다시 "재개"한다.
// 비동기 처리를 간단하게 만들어준다.
// Main thread 가 blocking 되는 점을 관리할 수 있게 도움을 준다.

// 비동기 처리를 위한 "긴 콜백 처리 작업"들을 "순차적인 코드로 변환"해주는 특징이 있다.

// 가장 많이 사용되는 3가지 JetPack 라이브러리 LifeCycle, WorkManager, Room 에 사용되고 있다.

// coroutine builder - launch, async, runBlocking
// runBlocking 은 새로운 코루틴을 실행하고, 완료되기 전까지 현재 스레드를 블로킹합니다.

/* runBlocking 의 위험한 점.

    * 사용하는 runBlocking 의 위치가 UI라면, UI를 Blocking 시키고, Coroutines 가 끝나길 대기한다.
      따라서 UI에서 사용하는 runBlocking은 사용하지 않아야 한다. UI가 멈추는 현상이 발생한다.
    * UI에서 오랜 시간 응답이 없다면 ANRs(Application Not Responding)이 발생할 수 있다.
    * runBlocking은 호출한 위치를 Blocking 시킨다.
    * runBlocking 내부의 응답이 종료되기 전까지 응답을 주지 않는다.
    * runBlocking은 비동기가 아닌 동기화로 동작한다.
    * runBlocking이 필요한 케이스를 찾아야 하는데, 명확한 IO를 보장하고, 데이터의 동기화가 필요한 경우와 UnitTest에서 활용하자.

 */
// launch 는 Job 객체를 반환하고, async 는 Deferred 통하여 결과값을 반환한다.
// builder 를 사용하려면 coroutine scope 가 필요하다.
// suspend fun 을 사용하려면 coroutine scope 안에서 사용해야 한다.

import kotlinx.coroutines.*

/*
    fun main() {
        GlobalScope.launch {
            delay(1000L)        -> Thread 를 blocking 하지 않고, 코루틴을 중단시키는 특별한 suspend fun
            println("World!")
        }

        println("Hello")
        // Thread.sleep(2000)   -> Thread 를 blocking 하는 함수.
        runBlocking {
            delay(2000L)
        }
    }

   위 코드는 어차피 main 스레드에서는 작업이 없으므로, main 함수 안 코드를 모두 runBlocking { }으로 묶을 수 있다.
 */

fun main() {
    runBlocking {
        GlobalScope.launch {    // GlobalScope - 코루틴의 생명주기가 전체 애플리케이션 생명주기와 같음.
            delay(1000L)
            println("World!")
        }

        println("Hello")
        delay(500L)     // delay 길이가 1000ms 보다 적으면, World 가 출력되지않고 끝남.
                                    // 이러한 부분에서 GlobalScope 가 DaemonThread 와 유사하다.
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
