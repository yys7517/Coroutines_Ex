package Coroutine_Context_and_Dispatchers

// 모든 코루틴들은 작업을 마쳤으면, 메모리 누출을 방지하기 위해 취소해주어야만 한다.
// CoroutineScope 라는 추상적인 캡슐을 통해 캡슐 내에 있는 코루틴들을 처리할 수 있다.

import kotlinx.coroutines.*

class Activity{
    private val mainScope = CoroutineScope( Dispatchers.Default )

    fun destroy() {
        mainScope.cancel()
    }

    fun doSomething(){

        repeat( 10 ) { i ->
            mainScope.launch {
                delay( (i+1) * 200L )
                println( "Coroutine $i is done.")
            }
        }
    }
}

fun main() = runBlocking<Unit> {
    val activity = Activity()
    activity.doSomething()      // Activity 의 인스턴스에 정의된 mainScope 내에서 코루틴들이 실행된다.
    println("Launched Coroutines")
    delay(500L)
    println("Destroying activitiy!")
    activity.destroy()          // mainScope 가 cancel 되었으므로, mainScope 내의 코루틴들이 모두 취소된다.
    delay(1000)
}