package Cancellation_Timeouts

import kotlinx.coroutines.*

// Job 은 항상 실행에 성공하여 실행 완료 상태가 되지 않는다. 다양한 변수로 인해 중간에 취소되어야 할 수 있다.
// ex) 네트워크를 통해 클라이언트가 서버에 정보를 요청했을 때, 서버에서 응답을 성공적으로 주면, job 은 실행에 성공하여 종료될 수 있다.
// 하지만, 서버에서 응답을 주지 않는다면, 이러한 경우에는 무한정 기다릴 수 없기 때문에, 일정 시간 이후에는 취소하는 작업이 필요하다.

// Job 을 취소하는 방법과 취소할 때 생기는 Exception 을 Handling 하는 방법을 알아보자.

@OptIn(InternalCoroutinesApi::class)
suspend fun main() {
    val job = CoroutineScope(Dispatchers.IO).launch {
        delay(1000)     // 어떠한 작업을 진행한다.
    }

    // job.cancel("Job Cancelled by User", InterruptedException("Cancelled Forcibly"))
    // cancel 시 Exception 을
    // InterruptedException 을 Throwable 하였지만.
    // Job 의 cancel 시 넘겨지는 Exception 은 CancellationException 으로 고정된다.
    // cancel 시 Throwable 된 Exception 은 반영되지 않는다.

    // Exception Handling
    // println(job.getCancellationException())

    // invokeOnCompletion -> cancel 되었을 뿐만 아니라, 실행 완료되었을 때에도 실행된다.
    // Why? -> Job 의 상태변수와 관련이 있다.

    // Job 의 상태변수 -> isActive, isCancelled, isCompleted
    // isActive -> Job 이 "실행중" 인지
    // isCancelled -> Job "cancel이 요청" 되었는지
    // isCompleted -> Job 의 "실행이 완료되었거나 cancel이 완료" 되었는지

    /*
    job.invokeOnCompletion { throwable ->
        println( throwable )
    }
    // 취소되지 않고 실행을 완료했을 때에는 throwable 값이 null 이 된다.
    */

    // 따라서 다음과 같이 Handling 해주는 것이 좋다.
    job.invokeOnCompletion { throwable ->
        when(throwable) {
            is CancellationException -> println("Job is Cancelled")
            null -> println("Job is Completed with no error")
        }
    }

    delay(3000)
}