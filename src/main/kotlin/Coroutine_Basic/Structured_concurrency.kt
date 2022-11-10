import kotlinx.coroutines.*

fun main() = runBlocking {
    /*
    val job = GlobalScope.launch {
        delay(3000L)
        println("World!")
    }

    println("Hello")
    job.join()
     */

    // Structured Concurrency 란 ? 구조적 동시성 원칙.
    // => 위와 같이 job은 Global Scope에서 실행되는 작업이기 때문에, 데몬 스레드로 취급을 하므로,
    // join()을 하지 않으면 "World!" 를 출력하지 못 하고 종료된다.

    launch {
        delay(3000L)
        println("World!")
    }
    // 이와 같이 runBlocking{}의 자식 코루틴으로 실행하였을 때는, runBlocking은 자식 코루틴이 종료하기까지
    // 기다리게 된다. 이러한 구조적 동시성 원칙을 사용하면 join()을 사용하지 않아도 된다.

    println("Hello")
}