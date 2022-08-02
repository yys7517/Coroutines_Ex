import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Delaying is not a good approach. - 지연은 좋은 접근 방법이 아니다.
// delay 함수를 사용하는 것은 좋은 방법이 아닌 것 같다.

// launch 의 반환되는 값은 job 객체이다.

fun main() = runBlocking {

        val job = GlobalScope.launch {
            delay(3000L)
            println("World!")
        }

        println("Hello")
        job.join()  //  job 이 속한 coroutine 이 종료될 때까지 기다린다.
}
