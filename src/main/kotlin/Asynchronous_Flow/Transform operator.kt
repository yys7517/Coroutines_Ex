package Asynchronous_Flow

// Transform Operator - map 이나 filter 와 같은 단순한 동작을 수행하는 것을 물론, 보다 복잡한 변환을 구현하는데 사용한다.
// transform 을 사용하면 장기적으로 실행되는 비동기적 요청을 수행하기 전에 문자열을 내보내고 응답을 통해서 작업을 수행할 수 있다.

// flow 에 임의의 값, 임의의 횟수 만큼 emit 할 수 있다.
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking<Unit> {

    (1..3).asFlow()
        .transform { request ->
            emit("request $request received - Making response $request") // 다음과 같이 문자열을 emit 하여 flow 를 통해 사용자에게 상황을 알려줄 수 있다.
            emit( performRequest(request) ) // request 에 대한 response 값.
        }
        .collect { response -> println(response) }
    // 결과
    // request 1 received - Making response 1
    // response 1
    // request 2 received - Making response 2
    // response 2
    // request 3 received - Making response 3
    // response 3
}

