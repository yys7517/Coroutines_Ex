package Asynchronous_Flow.Flow_Exception

// Exception transparency(예외 투명성)

// emit 하는 쪽에서 exception을 캡슐화 하고 싶으면 어떻게 할까요?
// 즉 collector 측에서는 예외처리를 하지 않고, emitter 측에서 에러를 감지하여 처리를 하려면 어떻게 해야 할까요?

// 모든 Flows 구현체는 exception에 투명해야합니다.
// flow {...} 빌더를 try/catch 블럭 안에서 사용하여 값을 emit하는 것은 exception에 투명하지 못한 행위입니다.
// exception에 투명하다는 말은, downstream에서 발생한 에러를 미리 처리하여 collector가 알 수 없게끔 되어서는 안된다는 의미이기 때문입니다.
// 에러가 났더라도 어떤 형태로든 collector가 알아차릴 수 있어야 합니다.

// emitter는 catch 연산자를 통하여 exception transparency를 유지할 수 있고 exception 처리를 캡슐화 할 수 있습니다.

import Asynchronous_Flow.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private fun getFlow(): Flow<String> =
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // emit next value
        }
    }.map { value ->
        check(value <= 1) { "Crashed on $value" }
        "string $value"
    }

fun main() = runBlocking<Unit> {

    // catch 연산자 안에서 예외를 분석하여 어떤 예외가 포착되었는지에 따라 다른 방식으로 대응할 수 있습니다.
    getFlow()
        .catch { e ->
            // throw를 사용하여 throw 를 다시 던질 수 있습니다.
            // throw IllegalArgumentException(e)

            // exception 상황이지만 평소처럼 emit 할 수 있습니다.
            // emit("Caught $e")

            // exception 을 무시하거나 단순히 로그를 찍거나 하는 등의 코드를 넣을 수 있습니다.
            // log("Exception $e")
        }
        .collect { value -> println(value) }



}