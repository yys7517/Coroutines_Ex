package Coroutine_Context_and_Dispatchers

// Coroutine Context - Coroutines 는 실행이 될 때, Coroutine Context 에서 실행된다.

// DisPatcher - Coroutine Context 의 요소 중 하나
//            - Coroutine 이 어떤 스레드나 스레드 풀에서 실행될 지 결정하는 역할.
/*

    * Dispatchers.Main - 이 디스패처를 사용하여 기본 Android 스레드에서 코루틴을 실행합니다.
      이 디스패처는 UI와 상호작용하고 빠른 작업을 실행하기 위해서만 사용해야 합니다.
      예를 들어 suspend 함수를 호출하고 Android UI 프레임워크 작업을 실행하며 LiveData 객체를 업데이트합니다.

    * Dispatchers.IO - 이 디스패처는 기본 스레드 외부에서 디스크 또는 네트워크 I/O를 실행하도록 최적화되어 있습니다.
        예를 들어 회의실 구성요소를 사용하고 파일에서 읽거나 파일에 쓰며 네트워크 작업을 실행합니다.

    * Dispatchers.Default - 이 디스패처는 CPU를 많이 사용하는 작업을 기본 스레드 외부에서 실행하도록 최적화되어 있습니다.
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

}
