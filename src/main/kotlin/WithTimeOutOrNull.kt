
import kotlinx.coroutines.*

fun main() = runBlocking {
    val result = withTimeoutOrNull( 1300L ) {
        repeat( 1000 ) { i ->
            kotlin.io.println("I'm sleeping $i ...")
            delay( 500L )
        }

        "Done"
    }   // withTimeoutOrNull( timeMills : ... ) -> Coroutine Scope 를 생성한 후, TimeOut 이 발생하면 Null 을 반환한다.
        // 반환 값이 있다.
        // 제한시간이 있는 Coroutine Scope 라고 생각할 수 있다.

    println("Result is $result")

    // 결과
    // I'm sleeping 0 ...
    // I'm sleeping 1 ...
    // I'm sleeping 2 ...
    // return : null [main]
}