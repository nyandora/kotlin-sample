import kotlinx.coroutines.*
import java.lang.Exception

fun main() {
    runBlocking {
        try {
            coroutineScope {
                val deferred = async {
                    delay(1000L)
                    throw ArithmeticException()
                    // this coroutine is marked "failed", so the parent coroutine is also marked "failed".
                }
                async {
                    try {
                        delay(2000L)
                        println("this is never printed.")
                    } catch (e: CancellationException) {
                        println("cancelled caused by 1st async call failure.")
                    }
                }
                println("waiting...")
                try {
                    deferred.await()
                } catch (ae: ArithmeticException) {
                    println("the exception thrown by 1st async call is caught.")
                    throw ae
                    // even though the exception is not thrown, coroutineScope() throws ArithmeticException.
                    // because by a failure in 1st async call the parent coroutine is already marked "failed".
                }
            }
        } catch (ae: ArithmeticException) {
            println("coroutineScope() throws the exception thrown in 1st async call.")
        }
    }
}
