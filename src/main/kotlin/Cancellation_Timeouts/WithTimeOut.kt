
import kotlinx.coroutines.*

fun main() = runBlocking {
    withTimeout( 1300L ) {
        repeat( 1000 ) { i ->
            println("I'm sleeping $i ...")
            delay( 500L )
        }
    }   // withTimeout( timeMills : ... ) -> Coroutine Scope 를 생성한 후, TimeOut 이 발생하면 Exception 을 발생시킨다.
        // 제한시간이 있는 Coroutine Scope 라고 생각할 수 있다.

}