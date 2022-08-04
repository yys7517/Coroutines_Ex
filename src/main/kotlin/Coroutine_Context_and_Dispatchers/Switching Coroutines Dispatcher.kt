package Coroutine_Context_and_Dispatchers

import kotlinx.coroutines.*

/*
    DisPatcher  - Coroutine Context 의 요소 중 하나
                - Coroutine 이 어떤 스레드나 스레드 풀에서 실행될 지 결정하는 역할.
                - 우리가 Dispatcher 에 코루틴을 보내기만 하면, Dispatcher은 스레드에 코루틴을 분산시킨다.

    * Dispatchers.Main - 이 디스패처를 사용하여 "기본 Android 스레드"에서 코루틴을 실행합니다.
      이 디스패처는" UI와 상호작용하는 작업을 실행하기 위해서만 사용" 해야 합니다.
      ex) CoroutineScope(Dispatchers.Main).launch {
            UpdateButton()  // UI와 상호작용하는 작업
      }

    * Dispatchers.IO - 이 디스패처는 기본 스레드 외부에서 "디스크 또는 네트워크 I/O를 실행하도록 최적화" 되어 있습니다.

    * Dispatchers.Default - 이 디스패처는 "CPU를 많이 사용하는 작업을 기본 스레드 외부에서 실행하도록 최적화" 되어 있습니다.
      예를 들어 목록을 정렬하고 JSON을 파싱합니다.

 */

fun main() = runBlocking {
    doSomethingUsingDispatchers()
}

private suspend fun doSomethingUsingDispatchers() {
    CoroutineScope(Dispatchers.Main).launch {   // Main Dispatcher 를 기본으로 설정.

        // 1. 데이터의 입출력을 해야 하므로 IO Dispatcher에 배분.
        val deferredInt: Deferred<Array<Int>> = async(Dispatchers.IO) {
            println(1)
            arrayOf(3, 1, 2, 4, 5)
        }

        // 2. Sort 작업을 해야하므로 CPU 작업을 많이 해야하는 Default Dispatcher에 배분.
        val sortedDeferred = async(Dispatchers.Default) {
            val value = deferredInt.await()
            value.sortedBy { it }
        }

        // launch에 Dispatcher를 따로 설정하지 않으면 상위 Context 와 같은 Dispatcher에 보내져 실행된다.
        // UI와 상호작용(setTextView)하는 작업이므로 Main Dispatcher에 배분.
        val textViewSettingJob = launch {
            val sortedArray = sortedDeferred.await()
            setTextView(sortedArray)
        }

    }
}

// TextView 에 값을 세팅하는 함수
suspend fun setTextView(sortedArray: List<Int>) {
    delay(1000L) // TextView 에 정수 리스트 값을 가공해서 세팅하는 과정이 진행된다고 가정하자.
}

