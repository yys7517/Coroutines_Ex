package Coroutine_Basic

// 다른 builder 가 제공하는 코루틴 스코프 외에도 CoroutineScope 라는 builder 를 사용하여,
// 자신만의 스코프를 선언할 수 있다.
// CoroutineScope - 이것은 코루틴의 범위를 만들고 모든 하위 launch 들이 완료될 때까지 완료되지 않습니다.

// runBlocking 과 coroutineScope 는 둘 다 자신 블록 내부의 동작이 완료되기를 기다리기 때문에 비슷하게 보일 수 있습니다.
// 이 둘의 주된 차이점은 runBlocking 은 자식 스레드가 완료될 때 까지 현재 스레드를 block 한다.
// coroutineScope 의 경우는 자식 스레드가 완료될 때까지 현재 스레드를 block 하지 않는다.

// 이런 차이점 때문에 runBlocking 은 정규 함수이고 coroutineScope 는 일시 중단 함수입니다.

import kotlinx.coroutines.*

fun main() = runBlocking {

    // launch 의 parameter 가 생략되었으므로, 상위 context 인, runBlocking 의 현재 스레드(context) 에서 실행만한다.
    launch {
        delay(200L)
        println("Task from runBlocking [${Thread.currentThread().name}]")    // 2
    }

    coroutineScope{
        // // CoroutineScope - 이것은 코루틴의 범위를 만들고 모든 하위 launch 들이 완료될 때까지 완료되지 않습니다.

        // launch 의 parameter 가 생략되었으므로, 상위 context 인, coroutineScope 와 같은 스레드(context) 에서 실행만한다.
        launch {
            delay(500L)
            println("Task from nested launch [${Thread.currentThread().name}]")   // 3
        }

        delay(100L)
        println("Task from coroutine scope [${Thread.currentThread().name}]")    // 1
    }

    // CoroutineScope 와 같은 Thread 이므로 ...
    // CoroutineScope 의 모든 하위 launch 들이 완료될 때까지 CoroutineScope 내의 실행이 완료되지 않기 때문에, 아래 구문은 마지막으로 동작한다.
    println("Coroutine scope is over [${Thread.currentThread().name}]")             // 4

    // 결과
    // Task from coroutine scope [main]
    // Task from runBlocking [main]
    // Task from nested launch [main]
    // Coroutine scope is over [main]

}

