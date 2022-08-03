package Coroutines_under_the_hood
// Coroutines under the hood

/*

    * A toy Problem

        fun postItem(item: Item) {
            val token = requestToken()
            val post = createPost(token, item)
            processPost(post)
        } // regular code

        서버에서 토큰을 가져와서 게시물을 포스트한 다음,
        포스트 완료처리를 하는 세 가지 연산을 코루틴으로 만들면
        컴파일 할 때, JVM 혹은 byte code에서 내부적으로 어떤 형태로 동작하는 것일까?

        내부적으로 컴파일할 때 regular code 가 CPS(Continuation Passing Style)로 바뀐다.

    * Continuation Passing Style

        fun postItem(item: Item) {
            requestToken { token ->
                val post = createPost(token, item)
                processPost(post)
            }
        }   // CPS Style code
 */

// 코루틴은 DeCompile 되면, 일반 코드일 뿐이다. ( regular code + compile -> CPS code )
// 내부적으로 컴파일 될 때 Continuation Passing Style(CPS, 연속 전달 방식) 이라는 형태의 코드로 전환한다 (=CPS transformation)
// 결과를 호출자에게 직접 반환하는 대신 Callback 같은 것 (continuation)으로 결과를 전달하는 것을 의미

// suspend 키워드가 붙은 함수들은 코틀린 컴파일러의 특별 대우를 받게됩니다.
// suspend 함수를 Compile 할 때, 내부적으로는 JVM 에 의해 byte code로 컴파일되면서
// 우리가 호출한 ""suspend 함수""의 마지막 parameter 에 Continuation cont 라는 인자가 생긴다. => CPS Style ( Continuation 의 객체를 넘겨주는 형태 )

/*
    Coroutine 은 중단(suspension)과 재개(resume)로 이루어져 있다.
    따라서 중단 지점(suspension points)과 재개 지점을 지정할 수 있어야 하는데,

    * Compiler 가 이 중단, 재개지점들을 Label 로 지정해놓는다.
        suspend fun postItem( item : Item ) {
        // LABEL 0
            val token = requestToken()
        // LABEL 1
            val post = createPost(token, item)
        // LABEL 2
            processPost(post)
        }

    ----------------------------------------------
    * Labels
        suspend fun postItem( item : Item ) {
            switch(label) {
                case 0 :
                    val token = requestToken()
                case 1 :
                    val post = createPost(token, item)
                case 2 :
                    processPost(post)
            }
        }

    --------------------------------------------------
    * CPS Transform

        suspend fun postItem( item : Item, cont : Continuation ) {
            val sm = object : CoroutineImpl{ ... }
            switch( sm.label ) {
                case 0 :
                    val token = requestToken(sm)    // sm => "state machine"
                case 1 :
                    val post = createPost(token, item, sm)
                case 2 :
                    processPost(post)
            }
        }

        state machine => 각각의 "suspend 함수"가 호출될 때, 상태(지금까지 했던 연산의 결과)를 같이 넘겨줘야 한다.
                         state machine 의 정체는 결국, 지금까지의 상태, 정보를 가지고 있는 Continuation 이다.

       **** Continuation 이 어떠한 정보값을 가진 형태로 Passing 되면서 코루틴이 내부적으로 동작하게 되는 것
       **** 각각의 Suspend 함수는 Continuation(sm)을 마지막 매개변수로 가져가게 된다.

    * Callback

        fun postItem(item: Item, cont: Continuation) {
            val sm = cont as? ThisSM ?: object : ThisSM {
                fun resume(…) {
                    postItem(null, this)
                }   // resume() 은 자기 자신( =현재까지의 연산 결과를 가진 상태)를 postItem 에 Continuation 으로 호출하는 역할
                    // 이 과정에서 label 값을 계속 하나 씩 올리면서, 다음 케이스들을 실행하는 형태가 되는 것이다.
            }

            switch (sm.label) {
                case 0:
                    sm.item = item
                    sm.label = 1
                    requestToken(sm)    // 만약 requestToken(sm)이 완료되었다면 sm(continuation)에다가 resume()을 호출하게 된다.
                                        // resume 에서는 현재 requestToken()을 완료한 자기 자신을 Continuation 객체로 넘겨주게 되면서
                                        // 다음 번 케이스인 createPost 가 동작할 것이다 !!
                case 1:
                    createPost(token, item, sm)

                case 2 :
                    processPost(post)

                case … :
                    ....
            }
        }
 */

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class ExampleContinuation(override val context: CoroutineContext = EmptyCoroutineContext) : Continuation<String> {

    var label = 0
    var result = ""

    override fun resumeWith(result: Result<String>) {
        this.result = result.getOrDefault("Default")
        postItem(null, this)
    }

}

fun main() {
    postItem("item", ExampleContinuation())
}

fun postItem(item: String?, cont: ExampleContinuation) {
    when (cont.label) {
        0 -> {
            // cont 로 이전까지의 상태를 Passing 받으며
            // 어떠한 작업이 수행이되고,
            cont.label = 1      // label 값을 올려준다.
            requestToken(item!!, cont)
        }
        1 -> {
            cont.label = 2
            createPost(cont.result, cont)
        }
        2 -> {
            processPost(cont.result, cont).run { println(this) }
        }
    }
}

fun requestToken(item: String, cont: Continuation<String>) {
    val result = "requestToken + $item" // requestToken + item
    cont.resumeWith(Result.success(result))
}

fun createPost(token: String, cont: Continuation<String>) {
    val result = "createPost + $token" // createPost + requestToken + item
    cont.resumeWith(Result.success(result))
}

fun processPost(post: String, cont: Continuation<String>): String {
    val result = "processPost + $post"  // processPost + createPost + requestToken + item
    return result
}


