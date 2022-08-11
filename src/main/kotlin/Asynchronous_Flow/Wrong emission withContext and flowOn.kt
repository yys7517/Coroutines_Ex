package Asynchronous_Flow

// Wrong emission withContext
// withContext 를 통한 잘못 된 방출.

// 오랫동안 실행되는 CPU 소모적인 코드는 Dispathers.Default 의 context에서 실행해야 하며
// UI를 업데이트하는 코드는 Dispathers.Main 의 context에서 실행해야 합니다.

// 일반적으로 withContext는 코틀린 코루틴이 실행되는 context를 변경하는데 사용되지만
// flow { ... } builder는 context preservation를 지켜야하며 다른 context에서 방출할 수 없습니다.


import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun wrongEmissionFlow() : Flow<Int> = flow {
    kotlinx.coroutines.withContext( Dispatchers.Default ) {
        for( i in 1..3 ) {
            Thread.sleep(100) // CPU 많이 사용하는 작업.
            emit(i)
        }
    }
}

// flowOn 연산자
// flow 의 코드블럭이 동작될 context 를 변경해준다.
// flowOn 을 사용하여 올바른 Dispatcher 에서 flow 의 코드블럭을 동작시키자.

fun rightWayToConvertContextFlow() : Flow<Int> = flow{
    for( i in 1..3 ) {
        Thread.sleep(100)   // CPU 의 소모율이 높은 동작.
        log("Emitting $i")
        emit(i)
    }
}.flowOn( Dispatchers.Default )

fun main() = runBlocking<Unit> {

    // wrongEmissionFlow().collect { value -> println(value) }

    // Exception in thread "main" java.lang.IllegalStateException: Flow invariant is violated:
    // Flow was collected in [BlockingCoroutine{Active}@5e123c6, BlockingEventLoop@55bb356f],
    // but emission happened in [DispatchedCoroutine{Active}@639825a1, DefaultDispatcher].
    // Please refer to 'flow' documentation or use 'flowOn' instead

    // 위 예외는 flow의 방출 context를 변경하기 위해선 "flowOn 함수를 사용하라는 것"을 알려주고 있습니다.

    // flowOn 연산자
    // flow 의 emit 동작을 수행할 context 를 변경해준다.
    rightWayToConvertContextFlow().collect { value -> log("Collected $value") }

    // 결과
    // [DefaultDispatcher-worker-1] Emitting 1
    // [main] Collected 1
    // [DefaultDispatcher-worker-1] Emitting 2
    // [main] Collected 2
    // [DefaultDispatcher-worker-1] Emitting 3
    // [main] Collected 3

    // flow 의 코드블럭은 flowOn 연산자에 의해 Default Dispatcher 에서 동작하고, Collect 연산은 main 스레드에서 수행된다.
}