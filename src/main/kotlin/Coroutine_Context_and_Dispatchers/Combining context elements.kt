package Coroutine_Context_and_Dispatchers

// Combining context elements

// Coroutine Context 로 여러 개의 요소들을 정의하고 싶을 때는
// public operator fun plus(context: CoroutineContext): CoroutineContext
// CoroutineContext 상의 operator fun plus 를 사용하여
// 요소들이 더해져서 하나의 Coroutine Context가 된다.

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun main() = runBlocking<Unit> {
    // public operator fun plus(context: CoroutineContext)
    launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->  }
    val coroutineContext = Dispatchers.IO + exceptionHandler

    CoroutineScope( coroutineContext ).launch {

    }

    // Dispatcher 의 Key 값을 "KeyA" 라고 하고, CoroutineExceptionHandler 의 Key 값을 "KeyB"라고 하자.

    // CoroutineExceptionHandler 을 부모인 coroutineContext 로부터 가져오고 싶다. 어떻게 해야할까 ?
    val exceptionHandlerFromContext = coroutineContext[exceptionHandler.key]
    if( exceptionHandler == exceptionHandlerFromContext )   // 같은 지 확인해볼까 ?
        println(true)   // 결과 : true == 같은 context 임을 알 수 있다.

    // 자식 CoroutineContext 에 접근이 가능하면 당연 제거도 가능하다. 어떻게 해아할까?
    // CoroutineContext의 minusKey 메서드를 사용하자.
    // public fun minusKey(key: Key<*>): CoroutineContext
    // 반환형은 CoroutineContext 로, key에 의해 제거된 CoroutineContext 객체 값을 반환한다.

    // 부모인 coroutineContext 에서 CoroutineExceptionHandler 를 제거한 context.
    val minusContext = coroutineContext.minusKey(exceptionHandler.key)
    if( minusContext == Dispatchers.IO )
        println(true)   // 결과 : true
}