package Asynchronous_Flow.Composing_multiple_flows

// zip 과 다른 점 은 최신 데이터만 결합하여 방출하며,
// 모든 Flow 모두 방출이 완료될 때까지 멈추지 않고 동작한다.

// 각 Flow 가 최소 하나의 데이터를 방출한 경우에 첫 번째 결합 데이터가 방출되고,
// 이후로는 어느 Flow 라도 데이터를 방출하면 각 Flow의 최신 데이터를 결합하여 방출해준다.
//출처: https://two22.tistory.com/18?category=1134189 [루크의 코드테라피:티스토리]

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val nums = (1..3).asFlow().onEach { delay(300) }
    val strs = flowOf("one","two","three").onEach { delay(400) }

    nums.combine(strs) { a, b -> "$a -> $b" }
        .collect { value ->  println(value) }

    // 위의 예제를 보면 "1", "one" 이 나온 시점이 최소 하나씩 데이터가 방출된 시점이고,
    // 그 이후로는 하나라도 방출되면 각 Flow 의 최신 값들이 결합이 되어 출력이 발생하는 것을 확인할 수 있다.

    // 또한 combine 은 Flow를 묶는데 개수 제한을 두지 않아 무제한으로 묶어서 사용할 수 있다.
    /*
    combine(nums, strs){ num, str ->
        "$num $str"
    }.collect { ... }

    combine(listOf(nums, nums, nums, nums, nums, strs, strs)) {
        it.reduce { a, b -> "$a $b"}
    }.collect { ... }

    combine(nums, nums, nums, nums, nums, strs, strs) {
        it.reduce { a, b -> "$a $b"}
    }.collect { ... }
    */
}

