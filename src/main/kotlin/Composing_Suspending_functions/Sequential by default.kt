
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

// The Dream Code - 꿈의 코드
// to simplify code that executes asynchronously - 비동기적으로 실행되는 코드를 단순화하다.
// converts async callbacks to sequential code   - 비동기 콜백을 순차적 코드로 변환.
// Use suspend functions to make async code sequential - suspend function 들을 통해 비동기 코드를 순차적으로.

fun main() = runBlocking {
    // retrofit 호출 같은 것(=heavy 한 비동기 job)을 순차적으로 실행하고 싶으면 어떻게 해야되는가 ?

    // 코루틴에서는 일반 코드처럼 작성하면, "비동기일지라도 순차적으로 실행되는 것이 기본"이다.
    // 비동기 실행을 순차적으로 맞춰줄 수 있고, 콜백을 순차 실행한다.

    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()

        println("The answer is ${one + two}")
    }
    println("Completed in $time ms")
}


suspend fun doSomethingUsefulOne() : Int {
    println("start, doSomethingUsefulOne")
    delay(1000L)
    println("end, doSomethingUsefulOne")
    return 13
}

suspend fun doSomethingUsefulTwo() : Int {
    println("start, doSomethingUsefulTwo")
    delay(1000L)
    kotlin.io.println("end, doSomethingUsefulTwo")
    return 29
}