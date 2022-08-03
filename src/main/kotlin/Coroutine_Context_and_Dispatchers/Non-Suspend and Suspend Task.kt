package Composing_Suspending_functions

import kotlinx.coroutines.*

fun main() : Unit = runBlocking {

    launch(Dispatchers.IO) {

        // async { nonSuspendTask1() }
        // async { nonSuspendTask2() }

        async { suspendTask1() }
        async { suspendTask2() }
    }
    // 결과
    // non-suspendTask
    // [nonSuspendTask2] After 1s in (DefaultDispatcher-worker-1)
    // [nonSuspendTask1] After 3s in (DefaultDispatcher-worker-3)
    // [nonSuspendTask2] After 4s in (DefaultDispatcher-worker-1)
    // [nonSuspendTask2] END in (DefaultDispatcher-worker-1)*****
    // [nonSuspendTask1] After 6s in (DefaultDispatcher-worker-3)
    // [nonSuspendTask1] END in (DefaultDispatcher-worker-3) *****

    // Thread.sleep() 동안 thread 가 blocked 되어 다른 작업을 수행할 수 없기 때문에, 각 함수는 서로 다른 스레드에서 실행된다.

    // suspendTask
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