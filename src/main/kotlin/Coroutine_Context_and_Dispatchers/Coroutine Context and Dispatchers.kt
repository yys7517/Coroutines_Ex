package Coroutine_Context_and_Dispatchers

// Coroutine Context - Coroutines 는 실행이 될 때, Coroutine Context 에서 실행된다. "Coroutine이 실행되는 환경"

// 하지만, 우리는 앞서 배운 내용 중, launch 의 Coroutine Context parameter 로 CoroutineExceptionHadler 와 Dispatcher 가 들어가는 것을 볼 수 있다.
// 이것이 가능한 이유는 각 각 Coroutine Context 를 확장하는 인터페이스 구현체이기 때문이다.
// Dispatcher 와 CoroutineExceptionHandler 또한 Coroutine 이 실행되는 환경의 일부라고 할 수 있다.

// DisPatcher - Coroutine Context 의 요소 중 하나
//            - Coroutine 이 어떤 스레드나 스레드 풀에서 실행될 지 결정하는 역할.
//            - 우리가 Dispatcher 에 코루틴을 보내기만 하면, Dispatcher은 스레드에 코루틴을 분산시킨다.
/*

    * Dispatchers.Main - 이 디스패처를 사용하여 "기본 Android 스레드"에서 코루틴을 실행합니다.
      이 디스패처는" UI와 상호작용하는 작업을 실행하기 위해서만 사용" 해야 합니다.
      ex) CoroutineScope(Dispatchers.Main).launch {
            UpdateButton()  // UI와 상호작용하는 작업
      }

    * Dispatchers.IO - 이 디스패처는 기본 스레드 외부에서 "디스크 또는 네트워크 I/O를 실행하도록 최적화" 되어 있습니다.

    * Dispatchers.Default - 이 디스패처는 "CPU를 많이 사용하는 작업을 기본 스레드 외부에서 실행하도록 최적화" 되어 있습니다.
      예를 들어 목록을 정렬하고 JSON을 파싱합니다.

 */
// 모든 Coroutine 의 builder(launch, runBlocking, ...) 들은 CoroutineContext parameter 를 갖고 있다.
// 명시적으로 어떤 Dispatcher 를 사용할 지 구체화하여, 어떤 Context 에서 Coroutine 을 실행할 지 결정할 수 있다.

import kotlinx.coroutines.*

fun main() : Unit = runBlocking {

    launch {
        // 이 구문과 같이 runBlocking 에서 옵셔널 파라미터 없이 그냥 실행하게 되면,
        // 자신이 실행된 Context 를 상속받아서 작업하기 때문에, Main Thread 에서 실행하게 된다. runBlocking 과 같은 Context 인 Main Thread.
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        // 출력결과 = I'm working in thread main
    }

    launch(Dispatchers.Unconfined) {// UnConfined(정의되있지 않다.) -> main Thread
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        // 출력결과 = I'm working in thread main
    }

    launch(Dispatchers.Default) { //  DefaultDispatcher 에서 실행
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
        // 출력결과 = I'm working in thread DefaultDispatcher-worker-1
    }

    /*
    launch( newSingleThreadContext("MyOwnThread") ) {
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
    */

    // newSingleThreadContext - 코루틴을 실행할 때 마다 새로운 스레드를 하나 생성해서 실행한다.
    val newThreadDispatcher = newSingleThreadContext("MyOwnThread")

    newThreadDispatcher.use { // "MyOwnThread" 에서 실행될 것이다.
        launch(it) {
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
            // 출력결과 = I'm working in thread MyOwnThread
        }
    }

    // close() 를 통해 메모리 관리를 해주어야 한다.
    newThreadDispatcher.close()

    // newFixedThreadPoolContext(nThreads : Int, name : String) : Thread Pool 을 만드는 메서드
    //  parameter -> (쓰레드 개수, 쓰레드 풀 이름)
    val newThreadPool = newFixedThreadPoolContext(2,"ThreadPool with 2 Threads")

    CoroutineScope(newThreadPool).launch {

    }
}
