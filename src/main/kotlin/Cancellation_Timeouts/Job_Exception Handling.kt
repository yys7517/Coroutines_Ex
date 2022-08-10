package Cancellation_Timeouts

import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

fun main() = runBlocking<Unit> {
    // invokeOnCompletion
    val job = CoroutineScope(Dispatchers.IO).launch {
        throw IllegalArgumentException()
    }
    // invokeOnCompletion 은 job 이 cancel 되든, 에러가 발생하든 수행이 완료가 되든 모든 상황에서 호출되는 것이므로
    // 각 상황에 따라 Error 를 Handling 하는 부분을 분리하는 방법이 필요하다.
    job.invokeOnCompletion { cause: Throwable? ->
        println(cause)
    }
    // java.lang.IllegalArgumentException

    delay(1000)

    // CoroutineExceptionHandler
    /*val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler : $exception")
    }*/

    // exceptionHandler 는 여러 job 에 붙일 수 있다.
    /*val job1 = CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
        throw IllegalArgumentException()
    }

    val job2 = CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
        throw InterruptedException()
    }*/

    // CoroutineExceptionHandler : java.lang.IllegalArgumentException
    // CoroutineExceptionHandler : java.lang.InterruptedException

    // 위의 코드를 조금 응용해보면, when 문을 이용하여 exception 에 대해 에러의 유형별로 처리하도록 만들 수 있다.

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler : $exception")

        when(exception) {
            is IllegalArgumentException -> println("More Argument Needed To Process Job")
            is InterruptedException -> println("Job Interrupted")
        }
    }

    val job1 = CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
        throw IllegalArgumentException()
    }

    val job2 = CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
        throw InterruptedException()
    }

    // CoroutineExceptionHandler : java.lang.IllegalArgumentException
    // More Argument Needed To Process Job
    // CoroutineExceptionHandler : java.lang.InterruptedException
    // Job Interrupted
}