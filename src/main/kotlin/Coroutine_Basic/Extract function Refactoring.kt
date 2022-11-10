package Coroutine_Basic

// 코루틴의 구현부의 긴 코드를 Extract 하여 Refactoring 하는 예제.
// 코루틴의 구현부를 함수 호출만으로 구현해보자.
// suspend fun 은 suspend fun 또는 coroutine 안에서 호출이 가능하다.

import kotlinx.coroutines.*

fun main() = runBlocking {

    launch {
        /*
        delay(3000L)        // coroutine 안에서 호출이 가능하다.
        println("World!")

        launch 내에 구현부를 메서드로 만들어 호출만하여 구현해보자.
         */
        myWorld()
    }

    println("Hello")
}

suspend fun myWorld() {
    delay( 3000L )  // delay() 는 suspend fun 이므로, myWolrd() 또한 suspend fun 이 되어야 호출할 수 있다.
    println("World!")
}