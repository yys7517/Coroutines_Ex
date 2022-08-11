package Asynchronous_Flow

// Intermediate flow operators
// Collection 또는 Sequence 가 연산자를 통해 Flow로 변환이 될 수 있다.
// 중간 연산자는 upstream 에 적용되어 downstream flow 를 반환한다.
// 새롭게 변환된 flow 를 리턴하며 빠르게 동작합니다.

// 중간 연산자는 기본적으로 map(데이터 변형), filter(데이터 필터링), onEach(모든 데이터마다 작업 수행) 등이 있다.
// 시퀀스와 중요한 차이점은 연산자 내부의 코드 블럭에 suspend 함수를 호출할 수 있다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// request(요청)를 처리하여 response(응답) 를 반환하는 메서드.
suspend fun performRequest(request : Int) : String{
    delay(1000)
    return "response $request"
}

fun main() = runBlocking<Unit> {

    (1..3).asFlow()
        .map { request ->
            // delay(100)      // 시퀀스와 중요한 차이점은 연산자 내부의 코드 블럭에 suspend 함수를 호출할 수 있다.
            performRequest(request) }// (1..3)  => (response(1..3)) 으로 데이터 변형.
        .collect { response -> println(response) }
    // 결과
    // response 1
    // response 2
    // response 3
}