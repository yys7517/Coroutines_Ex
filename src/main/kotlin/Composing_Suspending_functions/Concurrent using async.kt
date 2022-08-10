import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
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
// Deferred 는 Job 이며, 이로 인해 Deferred 는 Job의 모든 특성을 갖는다.
// Job의 상태변수(isActive, isCancelled, isCompleted)
// Job의 Exception Handling 등을 모두 Deferred에서 똑같이 적용할 수 있다.

// Deferred 와 Job 의 다른 점
// 예외가 자동으로 전파되는 Job, 자동으로 전파하지 않는 Deferred.
// 이유는 Deferred 는 예측되는 값이므로 결과값을 수신하기를 대기해야하기 때문. (결과값 수신 메서드 => await() )

// async{ ... }.await()
// without blocking a thread and resumes when deferred computation is complete, returning the resulting value
// Thread 를 block 하지 않고, deferred 값이 계산될 때까지, 기다리게 하는 함수.
// Deferred<T> 값의 await() 메서드가 수행되면 await() 를 수행한 코루틴은 결과가 반환되기까지 기다린다.


/*
fun main() {
    runBlocking {
        val deferred : Deferred<Int> = async {
            1
        }
        val value = deferred.await()
        // await() 메서드가 수행되면 수행한 해당 코루틴은 일시 중단된다.
        // 이러한 특성으로 await() 메서드는 일시 중단이 가능한 코루틴 스코프 내부 or suspend fun 에서만 호출이 가능하다.
        println( value )
    }
}
 */

suspend fun main() {
    /*
    val deferred : Deferred<String> = CoroutineScope(Dispatchers.IO).async {
            "Deferred Result"
    }
    */
    // val deferredResult = deferred.await()   // await() 를 호출하면 해당 구문을 실행하는 코루틴은 일시 중단해야하므로,
                                            // await() 메서드는 suspend fun 또는 코루틴 내부에서만 작성이 가능하다.

    // 따라서 main() 함수가 수행되는 코루틴은 IO Thread 의 Deferred 의 결과가 수신될 때까지 일시중단 되는 것이다.

    // println( deferredResult )

    val exceptionHandler = CoroutineExceptionHandler{ _, exception ->
        when(exception) {
            is IllegalArgumentException -> println("More Argument Needed to Process Job")
            is InterruptedException -> println("Job is Interrupted")
        }
    }

    /*
    val deferred : Deferred<Array<Int>> = CoroutineScope(Dispatchers.IO).async(exceptionHandler) {
        throw IllegalArgumentException()
        arrayOf(1,2,3)
    }
     */

    // val deferredResult = deferred.await()
    // println( deferredResult )


    // Deferred의 Exception을 Handling 하려면 어떻게 해야할까 ?
    val deferred : Deferred<Array<Int>> = CoroutineScope(Dispatchers.IO).async {
        throw IllegalArgumentException()
        arrayOf(1, 2, 3)
    }

    // 에러가 전파되는 위치에 Handler 를 추가해주는 것이다.
    CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
        val deferredResult = deferred.await()   // 에러가 전파되는 위치

        println( deferredResult )
    }.join()

    // delay(1000)
    // await() 를 사용하지 않으면, 에러를 전파하지 않는다. => 해당 값이 필요 없는 상황에서 에러를 전파시킬 필요가 없다.
    // 해당 값이 필요한 상황일 때는, 결과를 받아오는 중 Exception 이 발생하게 await() 를 호출한 쓰레드에 Error가 전파되며 또한 종료된다.
    // 하지만 이러한 코드는 Exception 이 Handling 되지 않았다.





}
/*
fun main() = runBlocking {

    val time = measureTimeMillis {

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
*/
