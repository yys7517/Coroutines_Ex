package Asynchronous_Flow.Composing_multiple_flows

// zip 은 결합된 모든 Flow 가 데이터를 방출하기를 기다리고
// 모든 Flow가 하나씩 방출이 완료되어서 한쌍이 나오는 시점에서 결합 데이터를 방출한다.
// 최대 두 개까지의 Flow 만 결합 할 수 있다.

// Flow 중에 하나라도 방출과 사용이 완료가 되면 나머지 Flow는 자동으로 종료된다.
// ( 따라서, 모든 데이터를 방출하려면 개수의 쌍이 맞아야 한다. )
// 출처: https://two22.tistory.com/18?category=1134189 [루크의 코드테라피:티스토리]
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val nums = (1..3).asFlow()
    val strs = flowOf( "one","two","three","four")

    nums.zip( strs ){ a, b -> "$a -> $b" }
        .collect { value -> println(value) }

    // 1 -> one
    // 2 -> two
    // 3 -> three

    // 위 예제를 보면 nums 가 "3"을 방출하고 사용이 끝난 시점에서
    // strs의 "four"가 남아있음에도 더 이상 출력이 없는 것을 확인할 수 있다.
}