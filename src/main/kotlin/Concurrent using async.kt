import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// Concurrent using async
// Concurrent - '동시에 발생하는'이라는 뜻

// CoroutineScope.async - deferred 값을 리턴.

// async{ ... }.await()
// without blocking a thread and resumes when deferred computation is complete, returning the resulting value
// Thread 를 방해하지 않고, deferred 값이 계산될 때까지, 기다리게 하는 함수.


/*
fun main() = runBlocking {

    val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        val oneRes = one.await()
        // await 을 통해서 값이 계산될 때 까지 기다리게 하면 순차적으로 실행하는 효과를 얻을 수 있다.

        // public interface Deferred<out T> : Job
        // Deferred 의 경우 Job 을 상속받기 때문에, cancel() 또한 가능하다.
        // one.cancel()
        val two = async { doSomethingUsefulTwo() }

        // println("The answer is ${one.await() + two.await()}")
        kotlin.io.println("The answer is ${oneRes + two.await()}")
    }
    kotlin.io.println("Completed in $time ms")

    // 결과
    // doSomethingUsefulOne [main]
    // doSomethingUsefulTwo [main]
    // The answer is 42 [main]
    // Completed in 1016 ms [main]

    // one + one.await() + two
    // doSomethingUsefulOne [main]
    // doSomethingUsefulTwo [main]
    // The answer is 42 [main]
    // Completed in 2022 ms [main]
}
*/

// Lazily started async

// Optionally, async can be made lazy by setting its start parameter
// 선택적으로, async 는 start 라는 파라메터를 사용하여 lazy 하게 세팅될 수 있다.

// its result is required by await, or if its Job's start function is invoked
// await 또는 start 를 통해 coroutine 을 실행해야 한다.

fun main() = runBlocking {

    val time = measureTimeMillis {
        val one = async (start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async (start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        // start 파라메터를 생략하면 기본값인 start = CoroutineStart.DEFAULT 가 된다.

        // start - start 를 통해 두 LAZY 된 Coroutine 을 실행시킨다.
        one.start()
        two.start()

        // if we just call await in println without first calling start
        // start 를 생략한다면, Coroutines가 await 를 만나 실행되어서 값을 받아올 때까지 기다리게 된다.
        println("The answer is ${one.await() + two.await()}")

    }
    println("Completed in $time ms")
}
// 결과
// start
// doSomethingUsefulOne [main]
// doSomethingUsefulTwo [main]
// The answer is 42 [main]
// Completed in 1022 ms [main]

// start 생략
// doSomethingUsefulOne [main]
// doSomethingUsefulTwo [main]
// The answer is 42 [main]
// Completed in 2016 ms [main]




