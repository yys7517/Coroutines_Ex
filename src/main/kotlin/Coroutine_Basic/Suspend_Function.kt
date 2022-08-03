// Coroutine -> 중단(suspend)과 재개(resume)
// suspend point 를 통해 suspend(중지)하고
// resume 하는 것

// suspend 는 무엇인가 ? 사전적으로는 '중지하다'라는 뜻의 단어이다.
// 비동기 실행을 위한 중단 지점을 의미한다.

// Suspend Function

// 이렇게 생성된 중단 함수는 코루틴이나 다른 중단 함수 안에서만 호출될 수 있다는 제약이 생기긴 하지만
// 코루틴이 제공하는 유용한 다른 중단함수들을 사용할 수 있게 된다는 장점 또한 갖게 됩니다.

// suspend fun 은 suspend fun 또는 coroutine 하에 호출이 가능하다.
// suspend fun 을 사용한다면, 하나의 thread 가 blocked 된 상태에서도 thread 에서 다른 작업을 수행할 수 있다.

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    launch {

        // delay(3000L)        // delay - suspend fun 중 하나, suspend fun 과 coroutine 에서 호출할 수 있다.
        // println("World!")

        // launch 내에 구현부를 메서드로 만들어 호출만하여 구현해보자. myWorld()
        myWorld()   // myWorld() 또한 suspend fun 또는 coroutine(CoroutineScope.launch or CoroutineScope.async)에서 호출할 수 있다.
    }

    println("Hello")
}


suspend fun myWorld() {
    delay(3000L)  // delay() 는 suspend fun 이므로 다른 suspend fun 에서 호출할 수 있다.
    println("World!")
}


