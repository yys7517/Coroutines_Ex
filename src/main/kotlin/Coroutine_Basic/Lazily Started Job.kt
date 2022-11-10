package Coroutine_Basic

import kotlinx.coroutines.*

// main 스레드는 일시중단될 수 있으므로, suspend 키워드를 붙여줘야한다.
suspend fun main() {
    // job은 필요한 위치에 생성을 해야하고, 그 위치에서 바로 실행을 시켜야하므로 유연성이 떨어진다.
    // job을 Lazy 하게 생성해놓으면, 수행되지 않고 대기상태로 있는다. => job을 Lazy하게 실행한다.
    val job = CoroutineScope(Dispatchers.Main).launch(start = CoroutineStart.LAZY) {
        println(1)
    }

    // job.start()
    // delay(100L) // fun main() -> suspend fun main()

    // Lazy하게 생성된 Job을 실행하는 두 가지 방법 : start(), join()
    // 두 메서드의 차이점은 일시중단의 유무이다.
    // start 는 일시중단 없이 실행한다. 따라서 실행되는 위치가 코루틴 내부나 suspend fun 이 아니어도 된다.

    // 일시중단이 없이 실행되므로, job 이 Lazy 하게 생성 후, 실행되기 전에
    // 현재 스레드가 실행이 종료되게 된다면, 아무 작업도 실행하지 못하게 된다.
    // Job 의 실행시간을 예측하여 delay 를 주는 것은 바람직하지 않다. 정확하게 알 수 없을뿐더러, delay 를 주는 것은 프로그램의 안정성을 해친다.

    // 반면 start 와 달리 join 은 job 이 작업을 마칠 때 까지 실행되고 있는 코루틴을 일시중단 해준다.
    // 따라서 join()은 일시중단 가능한 코루틴 내부 또는 suspend fun 내부에서 사용되어야 한다.
    // job 이 작업을 마치면, 코루틴이 다시 재개(resume)된다.
    job.join()  // fun main() -> suspend fun main()
}