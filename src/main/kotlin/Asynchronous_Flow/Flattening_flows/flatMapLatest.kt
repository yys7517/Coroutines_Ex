package Asynchronous_Flow.Flattening_flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// flatMapLatest 는 collectLatest 와 성격이 유사하다.
// 새로운 응답이 들어오면, 이전의 응답 데이터로 돌리던 작업을 취소하고 새로운 응답으로 작업을 진행한다.


private fun requestFlow( i : Int ) : Flow<String> = flow {
    emit("$i : First")
    delay(100)
    emit("$i : Second")
}

fun main() = runBlocking {

    (1..3).asFlow()
        .flatMapLatest { requestFlow(it) }
        .collect { value ->
            delay(300)
            println(value)
        }

    // 1 : First
    // 2 : First
    // 3 : First       "1 : Second 와 2 : Second 의 응답을 취소하고, 새로운 응답으로 작업을 새로 진행한다."
    // 3 : Second

    // Flow 의 마지막 값 3 을 응답 받았을 때 Mapping 함수의 코드 블럭을 끝까지 실행하는 것을 알 수 있다.

    /*
    // 굳이 이와같은 Flatten 함수들을 사용하지 않아도 Flow 를 사용하는데 지장은 없다.

    (1..3).asFlow()
        .collect {
            requestFlow(it).collect{ value ->
                println(value)          // 대신, collect 내부에서 collect 를 호출해야 하는 지저분한 상황이 만들어진다...
            }
        }

    // 출처: https://two22.tistory.com/17?category=1134189 [루크의 코드테라피:티스토리]
     */
}

