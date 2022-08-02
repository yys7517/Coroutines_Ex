
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
fun main() = runBlocking {

    try {
        val time = measureTimeMillis {
            // somethingUseful"XXX"Async() - 일반함수
            // suspend fun 이 아닌 일반 함수이므로 어디서든 호출이 가능하다.
            // 따라서, coroutine 환경에서는 이와 같은 style 의 async 는 권장하지 않는다.
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()

            kotlin.io.println("my exceptions")
            throw Exception("my exceptions")

            runBlocking {
                kotlin.io.println("The answer is ${one.await() + two.await()}")
            }
        }
        kotlin.io.println("Completed in $time ms")
    } catch (e : Exception) {

    }

    runBlocking {
        delay( 100000 )
    }
}

fun somethingUsefulOneAsync() = GlobalScope.async {
    kotlin.io.println("start, somethingUsefulOneAsync")
    val res = doSomethingUsefulOne()
    kotlin.io.println("end, somethingUsefulOneAsync")
    res
}
fun somethingUsefulTwoAsync() = GlobalScope.async {
    kotlin.io.println("start, somethingUsefulTwoAsync")
    val res = doSomethingUsefulTwo()
    kotlin.io.println("end, somethingUsefulTwoAsync")
    res
}
*/

/*
fun main() = runBlocking<Unit> {
    val time = measureTimeMillis {
        println("The answer is ${concurrentSum()}")
    }
    println("Completed in $time ms")
}

// coroutine scope 로 한번 감싸서, 어디서 쓸 수 있는 형태가 아닌, coroutine scope 안에서만 사용할 수 있도록 바꾼다.
// 코루틴들 사이에서 exception 이 발생되면, exception 이 전파되면서 코루틴이 전부 취소된다.

// GlobalScope 에서 독립적으로 사용하면 exception 이 핸들링이 되지 않으므로.

suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }

    delay(10)
    kotlin.io.println("Exception")
    throw Exception()

    one.await() + two.await()
}
*/

fun main() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch(e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }
}

// 간단한 연산은 GlobalScope 로 Coroutine 을 실행시켜 오류를 내서, 모든 Coroutine 을 동작 중단 시키기 보다는
// coroutineScope 를 사용하여, 오류가 발생하면, coroutineScope의 coroutines만 종료하도록 하자.
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE) // Emulates very long computation
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}

// GlobalScope -> 앱이 처음 시작부터 종료 할때까지 하나의 CoroutineContext 안에서 동작하도록 할 수 있다.

// 필요할 때 선언하고, 종료하자 -> CoroutineScope 사용
// 매번 새롭게 생성하는 CoroutineScope을 활용함으로써 효율을 높일 수 있다.

// 당연히 앱을 사용하면서 장시간 동작해야 할 thread가 필요하다면 매번 생성하는 CoroutineScope보다는
// 최초 접근 시 만들어지는 GlobalScope이 효율적이다.