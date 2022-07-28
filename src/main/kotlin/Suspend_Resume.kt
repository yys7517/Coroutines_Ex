import kotlinx.coroutines.*

fun main() = runBlocking {

    launch {
        repeat(5) { i ->
            println("Coroutine A, $i")
            // delay(10L)  // 현재 coroutine이 일시 중단될 수 있다.
        }
    }

    launch {
        repeat(5) { i ->
            println("Coroutine B, $i")
            // delay(10L)  // 현재 coroutine이 일시 중단될 수 있다.
        }
    }

    println("Coroutine Outer")
    // 결과
    // Coroutine Outer [main @coroutine#1]
    // Coroutine A, 0 [main @coroutine#2]
    // Coroutine A, 1 [main @coroutine#2]
    // Coroutine A, 2 [main @coroutine#2]
    // Coroutine A, 3 [main @coroutine#2]
    // Coroutine A, 4 [main @coroutine#2]
    // Coroutine B, 0 [main @coroutine#3]
    // Coroutine B, 1 [main @coroutine#3]
    // Coroutine B, 2 [main @coroutine#3]
    // Coroutine B, 3 [main @coroutine#3]
    // Coroutine B, 4 [main @coroutine#3]


    // launch 는 바로 coroutine 을 시행하는 것이 아니고, 스케줄링을 먼저 한다.
    // 따라서 Outer 가 먼저 출력이 되고, Coroutine A와 B가 시행이 되는것이다.

    // A 와 B 에 delay 를 준다면, 중단지점이 된다.
    // delay - 일시 중단 함수

    // 코드 내 delay 추가 결과
    // Coroutine Outer [main @coroutine#1]
    // Coroutine A, 0 [main @coroutine#2]
    // Coroutine B, 0 [main @coroutine#3]
    // Coroutine A, 1 [main @coroutine#2]
    // Coroutine B, 1 [main @coroutine#3]
    // Coroutine A, 2 [main @coroutine#2]
    // Coroutine B, 2 [main @coroutine#3]
    // Coroutine A, 3 [main @coroutine#2]
    // Coroutine B, 3 [main @coroutine#3]
    // Coroutine A, 4 [main @coroutine#2]
    // Coroutine B, 4 [main @coroutine#3]
}


fun <T>println( msg : T ) {
    kotlin.io.println("$msg [${Thread.currentThread().name}]")
}

