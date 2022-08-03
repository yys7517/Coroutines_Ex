import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// Lazily started async

// Optionally, async can be made lazy by setting its start parameter
// 선택적으로, async 는 start 라는 파라메터를 사용하여 lazy 하게 세팅될 수 있다.

// its result is required by await, or if its Job's start function is invoked
// Lazy 된 async coroutine 의 결과는 await 또는 그 들의 start 함수가 호출되야 한다.

fun main() = runBlocking {

    val time = measureTimeMillis {
        val one = async (start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async (start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        // start 파라메터를 생략하면 기본값인 start = CoroutineStart.DEFAULT 가 된다.

        // start - start 를 통해 두 LAZY 된 Coroutine 을 실행시킨다.
        // one.start()
        // two.start()

        // await 를 통해 우리는 async coroutines 가 끝나기를 기다린다.
        // println("The answer is ${one.await() + two.await()}")

        // if we just call await in println without first calling start
        // start 를 통해 LAZY 된 coroutine 을 호출하지 않고, println 구문에서 async 코루틴의 await 를 호출한다면 ?
        // ==> coroutine 들을 println 구문에서 그제서야 호출되고, 선언이 된 후, 늦게서야 coroutine 들이 동작하게 된다.
        kotlin.io.println("if we just call await in println without first calling start")
        kotlin.io.println("The answer is ${one.await() + two.await()}")

    }

    kotlin.io.println("Completed in $time ms")
}
// 결과
// case 1. start 를 사용하여 미리 coroutine 을 실행하도록 호출.
// doSomethingUsefulOne [main]
// doSomethingUsefulTwo [main]
// The answer is 42 [main]
// Completed in 1022 ms [main]

// case 2. start 생략 후 println 에서 await 호출.
// doSomethingUsefulOne [main]
// doSomethingUsefulTwo [main]
// The answer is 42 [main]
// Completed in 2016 ms [main]