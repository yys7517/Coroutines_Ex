package Asynchronous_Flow

// Flows are sequential
// Flow 는 순차적이다.
// 특별한 Operator 를 사용하지 않는 이상, Flow 의 Collection 들은 각각 순차적으로 동작되도록 Performed 되었다.

// upstream 의 모든 중간 연산자들에 의해 처리되어 downstream 으로 전달되며
// 마지막으로 종단 연산자로 전달된다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    (1..5).asFlow()
        .filter {
            println("Filter $it")
            it % 2 == 0
        }
        .map {
            println("Map $it")
            "string $it"
        }
        .collect { value -> println("Collect $value") }

    // 결과
    // Filter 1
    // Filter 2
    // Map 2
    // Collect string 2
    // Filter 3
    // Filter 4
    // Map 4
    // Collect string 4
    // Filter 5

}