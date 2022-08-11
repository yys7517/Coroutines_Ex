package Asynchronous_Flow

// Terminal flow 연산자.
// flow 의 수집(collect)을 시작하는 suspending 함수.

// toList, toSet 를 통해 List 와 Set 와 같은 컬렉션으로 변환할 수 있다.
// 연산자는 첫 번째 값을 얻고 flow 가 단일 값을 방출하도록 보장.
// reduce, fold 로 flow 를 하나의 값으로 reducing 하여 반환할 수 있다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// collect 는 기본 연산자이지만, terminal 연산자가 있으면 다음처럼 쉽게 사용할 수 있다.
fun main() = runBlocking <Unit> {
    val sum = (1..5).asFlow()
        .map { it * it }    // 1 4 9 16 25
        .reduce{ a, b -> a + b }    // 1 + 4 + 9 + 16 + 25 = 55

    println(sum)    // 55

}