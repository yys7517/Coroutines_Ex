package Asynchronous_Flow.Composing_multiple_flows

// CombineTransform
// combine() 과 transform() 를 하나의 함수로 처리하는 것이 가능하며,
// combine 과 동일하게 여러 개의 Flow를 대상으로 하 룻 있다.
// combine 하고, transform 을 따로 호출해도 결과는 동일하다. (하나의 함수로 두 작업을 할 수 있다.)

// 결과물은 combine 과 동일하게 최신 데이터가 원하는 형태로 반환하여 방출한다.
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking<Unit> {
    val request = (1..3).asFlow().onEach { delay(400) }
    val search = flowOf("Google", "Naver", "Daum").onEach { delay(300) }

    request
        .combineTransform( search ) { request, search ->
            emit("Start request")
            delay(400)
            emit("end request $request at $search")
        }
        .collect { value -> println(value) }

    // 결과
    // Start request
    // end request 1 at Google
    // Start request
    // end request 1 at Naver
    // Start request
    // end request 2 at Naver
    // Start request
    // end request 2 at Daum
    // Start request
    // end request 3 at Daum

}