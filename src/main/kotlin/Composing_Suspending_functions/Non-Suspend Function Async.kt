package Coroutines_under_the_hood

import doSomethingUsefulOne
import doSomethingUsefulTwo
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*fun main() = runBlocking {

    try {
        val time = measureTimeMillis {
            // somethingUseful"XXX"Async() - 일반함수
            // suspend fun 이 아닌 일반 함수이므로 어디서든 호출이 가능하다.
            // 따라서, coroutine 환경에서는 이와 같은 style 의 async 는 권장하지 않는다.
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()

            kotlin.io.println("my exceptions")
            throw Exception("my exceptions")

            runBlocking {
                kotlin.io.println("The answer is ${one.await() + two.await()}")
            }
        }
        kotlin.io.println("Completed in $time ms")
    } catch (e : Exception) {
        println("Exception : $e")
    }

    runBlocking {
        delay( 100000 )
    }
}*/

fun main() = runBlocking {
    val time = measureTimeMillis {
        // somethingUseful"XXX"Async() - 일반함수
        // suspend fun 이 아닌 일반 함수이므로 어디서든 호출이 가능하다.
        // 따라서, coroutine 환경에서는 이와 같은 style 의 async 는 권장하지 않는다.
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()

        // 여기서 `runBlocking { ... }`으로 인해 main 스레드는 block 된 후, 코루틴이 마치기를 기다립니다.
        println("The answer is ${one.await() + two.await()}")
    }

    kotlin.io.println("Completed in $time ms [${Thread.currentThread().name}]")
}



fun somethingUsefulOneAsync() = GlobalScope.async {
    kotlin.io.println("start, somethingUsefulOneAsync")
    val res = doSomethingUsefulOne()
    kotlin.io.println("end, somethingUsefulOneAsync")
    res
}

fun somethingUsefulTwoAsync() = GlobalScope.async {
    kotlin.io.println("start, somethingUsefulTwoAsync")
    val res = doSomethingUsefulTwo()
    kotlin.io.println("end, somethingUsefulTwoAsync")
    res
}


