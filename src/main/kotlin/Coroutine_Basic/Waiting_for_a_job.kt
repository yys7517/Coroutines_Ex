import kotlinx.coroutines.*

// Delaying is not a good approach. - 지연은 좋은 접근 방법이 아니다.
// delay 함수를 사용하는 것은 좋은 방법이 아닌 것 같다.

// launch 의 반환되는 값은 job 객체이다.
/*
fun main() = runBlocking {
    // job은 생성과 동시에 실행된다.
    // 따라서 job은 필요한 위치에 생성을 해야하고, 그 위치에서 바로 실행을 시켜야하므로 유연성이 떨어진다.
    val job = GlobalScope.launch {
        delay(3000L)
        println("World!")
    }

    println("Hello")
    job.join()  //  job 이 속한 coroutine 이 종료될 때까지 기다린다.
}
*/

fun main() = runBlocking<Unit> {

    val job3 = CoroutineScope(Dispatchers.Default).async {

        kotlin.io.println("작업3 수행중")
        val result = (1..10).sortedByDescending { it }
        kotlin.io.println("작업3 수행완료")
        result
    }

    val job1 = launch {

        kotlin.io.println("작업1 수행")

        kotlin.io.println("작업1 일시중단")
        val job3Result = job3.await()       // 작업3의 결과가 필요하여 작업1을 수행하는 코루틴이 일시중단 되었다.

        kotlin.io.println("작업1 재개")
        job3Result.forEach { print("$it ") }
        println()
        kotlin.io.println("작업1 수행완료")
    }

    val job2 = launch {
        delay(1L)
        kotlin.io.println("작업2 수행")
        kotlin.io.println("작업2 수행완료")
    }

}