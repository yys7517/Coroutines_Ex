package Coroutine_Context_and_Dispatchers

import kotlinx.coroutines.*

// coroutineContext 의 주요 요소는 Dispatcher 뿐만 아니라 Job 도 있다.
fun main() : Unit = runBlocking {

    println("My job is ${coroutineContext[Job]}")

    // launch는 새 Coroutine을 시작하고 호출자에게 결과를 반환하지 않습니다.
    // '실행 후 삭제'로 간주되는 모든 작업은 launch를 사용하여 시작할 수 있습니다.
    // 작업 객체 '행위'를 나타내는 Job 반환
    launch {
        println("My job is ${coroutineContext[Job]}")
    }

    // async는 새 Coroutine을 시작하고 await라는 정지 함수로 결과를 반환하도록 허용합니다.
    // Deferred 값 반환
    async {
        println("My job is ${coroutineContext[Job]}")
    }



    // My job is BlockingCoroutine{Active}@3632be31
    // My job is StandaloneCoroutine{Active}@1f36e637
    // My job is DeferredCoroutine{Active}@551aa95a

    isActive
    // fun isActive() => get() = coroutineContext[Job]?.isActive ?: true

}