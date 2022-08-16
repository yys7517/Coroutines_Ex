package Asynchronous_Flow.Flow_Completion


// try 내의 구문이 Collect 연산을 하는 구문이라면, finally 블럭을 사용하여
// Collect 종료 시 실행한 구문을 정의할 수 있다.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private fun getFlow() : Flow<Int> = (1..3).asFlow()

fun main() = runBlocking {
    try {
        getFlow().collect { value -> println(value) }
    } finally {
        println("Done")
    }

    // onCompletion 중간 연산자를 사용하여 선언형으로 다음과 같이 다시 작성할 수 있다.
    getFlow()
        .onCompletion { println("Done") }     // Flow Collection 을 마쳤을 떄 실행될 로직을 정의할 수 있다.
        .collect { value -> println(value) }
}