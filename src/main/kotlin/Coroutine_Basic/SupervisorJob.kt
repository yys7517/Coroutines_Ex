package Coroutine_Basic

import kotlinx.coroutines.*
import java.lang.AssertionError

// SupervisorJob 은 무엇인가 ?

// 코루틴 - 비동기 프로그래밍을 위한 일시중단이 가능한 경량 스레드.
// 코루틴 내부에서 코루틴이 수행될 수 있으며, 이론적으로 그 깊이는 무한해질 수 있다.
// 하지만, 코루틴 내부에서 수행되는 자식 코루틴에 에러가 발생하게 되면
// 계층적으로, 부모 코루틴까지 취소되게 된다. 부모 코루틴이 취소된다면, 그 자식 코루틴 모두가 취소된다.

// 이와 같은 문제를 방지하기 위해, ""에러의 전파 방향을 자식으로 한정짓는 것이 바로 SupervisorJob"" 이다.
// SupervisorJob 은 CoroutineContext 의 형태로, 다른 CoroutineContext 들과 혼합해서 사용된다.

// **** SupervisorJob 을 자식 Coroutine 에 사용하면 부모 Coroutine 으로 에러가 전파되지 않는다.

suspend fun main() {
    val supervisorJob = SupervisorJob()

    CoroutineScope(Dispatchers.IO).launch {
        // **** SupervisorJob 을 자식 Coroutine 에 사용하면 부모 Coroutine 으로 에러가 전파되지 않는다.
        val firstChildJob = launch(Dispatchers.IO + supervisorJob ) {
            throw AssertionError("첫 째 Job이 AssertionError로 인해 취소됩니다.")
        }

        val secondChildJob = launch(Dispatchers.Default) {
            delay(1000)
            println("둘 째 Job이 살아있습니다.")
        }

    }.join()

    // 첫 째 Job이 SupervisorJob() 을 CoroutineContext로 받았기 때문에 부모로 에러가 전파되지 않아, 둘 째 Job까지 실행되는 모습.

    // 결과
    // Exception in thread "DefaultDispatcher-worker-3" java.lang.AssertionError: 첫 째 Job이 AssertionError로 인해 취소됩니다.
    //	at Coroutine_Basic.SupervisorJobKt$main$2$firstJob$1.invokeSuspend(SupervisorJob.kt:26)
    //	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
    //	at kotlinx.coroutines.DispatchedTask.run(Dispatched.kt:241)
    //	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:594)
    //	at kotlinx.coroutines.scheduling.CoroutineScheduler.access$runSafely(CoroutineScheduler.kt:60)
    //	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:740)
    // 둘 째 Job이 살아있습니다.
}