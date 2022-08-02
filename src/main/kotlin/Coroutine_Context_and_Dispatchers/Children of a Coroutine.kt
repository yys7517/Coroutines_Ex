package Coroutine_Context_and_Dispatchers

// 어떠한 새 Coroutine 을 동작하는 Job 은 부모 Coroutine 을 동작하는 Job 의 자식이 된다.
// 따라서 부모 Coroutine 의 실행이 취소되면, 재귀적으로 자식들의 Coroutine 또한 실행이 취소된다.

// 그러나 GlobalScope 에서 launch 로 동작하는 Coroutine 은 부모 Coroutine 이 존재하지 않다.
// 따라서, 이것은 부모 - 자식 관계가 성립되지 않으며, 독립적으로 동작하게 된다.

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    val request = launch { // [main]

        GlobalScope.launch {    // GlobalScope 이므로 독립적으로 동작한다. 부모가 존재하지 않는다.
            println("job1: I run in GlobalScope and execute independently! [${Thread.currentThread().name}]")
            delay(1000)
            println("job1: I am not affected by cancellation of the request [${Thread.currentThread().name}]")
        }


        launch {// launch 에 옵션값이 없으므로 상위 context(부모 request의 launch context)를 상속받는다. [main]
            delay(100)
            println("job2: I am a child of the request coroutine [${Thread.currentThread().name}]")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled [${Thread.currentThread().name}]")
        }
    }
    delay(500)
    request.cancel() // request 취소 처리.
    delay(1000) // 어떤 일이 발생하는지 보려고 딜레이
    println("main: Who has survived request cancellation? [${Thread.currentThread().name}]")
}