package Coroutine_Basic

import kotlinx.coroutines.*
import java.lang.AssertionError

// 매 번 CoroutineContext에 SupervisorJob을 설정할 필요 없이
// 특정 블럭 내부의 모든 코루틴에 SupervisorJob 을 설정하고 싶을 때 사용.

// 그 모든 코루틴을 supervisorScope { } 를 사용하여 덮어줄 수 있다.

suspend fun main() {

    //supervisorScope {     // 이 범위에 supervisorScope 를 지정하게 되면, 부모 코루틴이 종료되게 된다.
    CoroutineScope(Dispatchers.IO).launch {

        supervisorScope {
            // 자식 코루틴 firstChildJob, secondChildJob 이 supervisorScope 안에서 실행된다.
            // 따라서 부모 코루틴에 에러가 전파되지 않는다.

            val firstChildJob = launch {
                throw AssertionError("첫 째 Job이 AssertionError로 인해 취소됩니다.")
            }

            val secondChildJob = launch(Dispatchers.Default) {
                delay(1000)
                println("둘 째 Job이 살아있습니다.")
            }

//            firstChildJob.join()
//            secondChildJob.join()
        }

    }.join()
}
// }