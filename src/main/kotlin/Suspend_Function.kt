// suspend 는 무엇인가 ? 사전적으로는 '중지하다'라는 뜻의 단어이다.
// 비동기 실행을 위한 중단 지점을 의미한다.

// suspend fun 을 사용한다면, 하나의 thread 가 blocked 된 상태에서 그 작업을 suspend 하고, 그 기간동안 thread 에서 다른 작업을 수행할 수 있다.


import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
    suspend fun 은 suspend fun 또는 coroutine 하에 호출이 가능하다.

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
 */

fun main() : Unit = runBlocking {

    launch(Dispatchers.IO) {

        // async { nonSuspendTask1() }
        // async { nonSuspendTask2() }

        async { suspendTask1() }
        async { suspendTask2() }
    }
    // 결과
    // non - suspend fun
    // [nonSuspendTask2] After 1s in (DefaultDispatcher-worker-1)
    // [nonSuspendTask1] After 3s in (DefaultDispatcher-worker-3)
    // [nonSuspendTask2] After 4s in (DefaultDispatcher-worker-1)
    // [nonSuspendTask2] END in (DefaultDispatcher-worker-1)*****
    // [nonSuspendTask1] After 6s in (DefaultDispatcher-worker-3)
    // [nonSuspendTask1] END in (DefaultDispatcher-worker-3) *****

    // Thread.sleep() 동안 thread 가 blocked 되어 다른 작업을 수행할 수 없기 때문에, 각 함수는 서로 다른 스레드에서 실행된다.

    // suspend fun
    // [suspendTask2] After 1s in (DefaultDispatcher-worker-1)
    // [suspendTask1] After 3s in (DefaultDispatcher-worker-1)
    // [suspendTask2] After 4s in (DefaultDispatcher-worker-1)
    // [suspendTask2] END in (DefaultDispatcher-worker-1)
    // [suspendTask1] After 6s in (DefaultDispatcher-worker-1)
    // [suspendTask1] END in (DefaultDispatcher-worker-1)

    // suspend 함수는 Thread 가 blocked 되어도 다른 작업을 수행할 수 있다.
    // 이유는, delay() 동안 thread 가 suspend 되어 다른 함수의 작업을 수행할 수 있기 때문이다.
    // 단, suspend 를 사용한다고 무조건 하나의 스레드에서만 실행되는 것은 아니다. 여기서는 2개의 함수(task1, task2) 의 작업이
    // 서로 다른 시간에 일어나기 때문에 가능한 것.

    // 같은 시간에 동작시켜보면 어떨까 ?
    // [suspendTask2] After 3s in (DefaultDispatcher-worker-1)
    // [suspendTask1] After 3s in (DefaultDispatcher-worker-3)
    // [suspendTask1] After 6s in (DefaultDispatcher-worker-1)
    // [suspendTask2] After 6s in (DefaultDispatcher-worker-3)
    // [suspendTask1] END in (DefaultDispatcher-worker-1)
    // [suspendTask2] END in (DefaultDispatcher-worker-3)

    // suspend fun 또한 같은 시간에 여러 작업을 진행 시에는 서로 다른 스레드에서 동작하는 것을 알 수 있다.
}


suspend fun suspendTask1() {
    delay(3000)
    kotlin.io.println("[suspendTask1] After 3s in (${Thread.currentThread().name})")
    delay(3000)
    kotlin.io.println("[suspendTask1] After 6s in (${Thread.currentThread().name})")

    kotlin.io.println("[suspendTask1] END in (${Thread.currentThread().name})")
}

suspend fun suspendTask2() {
    delay(1000)
    kotlin.io.println("[suspendTask2] After 1s in (${Thread.currentThread().name})")
    delay(3000)
    kotlin.io.println("[suspendTask2] After 4s in (${Thread.currentThread().name})")

    kotlin.io.println("[suspendTask2] END in (${Thread.currentThread().name})")
}

fun nonSuspendTask1() {
    Thread.sleep(3000)
    kotlin.io.println("[nonSuspendTask1] After 3s in (${Thread.currentThread().name})")
    Thread.sleep(3000)
    kotlin.io.println("[nonSuspendTask1] After 6s in (${Thread.currentThread().name})")

    kotlin.io.println( "[nonSuspendTask1] END in (${Thread.currentThread().name}) *****")
}
fun nonSuspendTask2() {
    Thread.sleep(1000)
    kotlin.io.println( "[nonSuspendTask2] After 1s in (${Thread.currentThread().name})")
    Thread.sleep(3000)
    kotlin.io.println( "[nonSuspendTask2] After 4s in (${Thread.currentThread().name})")

    kotlin.io.println( "[nonSuspendTask2] END in (${Thread.currentThread().name})*****")
}