import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// Concurrent using async
// Concurrent - '동시에 발생하는'이라는 뜻

// CoroutineScope.async - deferred 값을 리턴.

// 개념적으로 async 는 launch(CoroutineScope.launch)와 같습니다. 이것은 다른 모든 코루틴과 동시에 작동하는 가벼운 스레드인 별도의 코루틴을 시작합니다.
// 차이점은 launch가 Job을 반환하고 결과 값을 제공하지 않는 반면
// async에서는 나중에 결과를 제공하겠다는 약속을 나타내는 Deferred (light-weight하고 non-blocking한)를 반환한다는 점입니다.
// Deferred 값은 미래에 올 수 있는 값을 담아놓을 수 있는 객체이다. async{ } 의 마지막 줄에 있는 값이 반환된다.

// 우리는 await() 를 사용하여 결과값으로 deferred 값을 얻을 수 있지만
// public interface Deferred<out T> : Job
// Deferred 의 경우 Job 을 상속받기 때문에, cancel() 또한 가능하다.

// async{ ... }.await()
// without blocking a thread and resumes when deferred computation is complete, returning the resulting value
// Thread 를 block 하지 않고, deferred 값이 계산될 때까지, 기다리게 하는 함수.

// Deferred<T> 값의 await() 메서드가 수행되면 await() 를 수행한 코루틴은 결과가 반환되기까지 기다린다.
// await() 메서드가 수행되면 수행한 해당 코루틴은 일시 중단된다.
// 이러한 특성으로 await() 메서드는 일시 중단이 가능한 코루틴 스코프 내부 or suspend fun 에서만 호출이 가능하다.


fun main() = runBlocking {

    val time = measureTimeMillis {

        // 이것은 두 개의 코루틴이 동시에 실행되기 때문에 두 배 더 빠릅니다.
        // val one = async { doSomethingUsefulOne() }
        // val two = async { doSomethingUsefulTwo() }

        // println("The answer is ${one.await() + two.await()}")

        val one = async { doSomethingUsefulOne() }
        val oneRes = one.await()
        // one 과 two 사이에서 await 을 통해서 값이 계산될 때 까지 기다린 후 반환받으면
        // 하나의 코루틴에서 doSomethingUsefulOne()과 doSomethingUsefulTwo()를 실행하는 것과 같은 실행시간을 가질 수 있다.

        val two = async { doSomethingUsefulTwo() }

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