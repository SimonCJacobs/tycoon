package jacobs.tycoon.integration

import kotlinx.coroutines.runBlocking

actual fun runBlockingMultiplatform( closure: suspend () -> Any ) {
    runBlocking { closure() }
}