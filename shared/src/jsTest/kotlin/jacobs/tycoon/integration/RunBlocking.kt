package jacobs.tycoon.integration

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asPromise
import kotlinx.coroutines.async

/**
 * Cast is necessary because it seems that [Unit] must be returned from the test
 * in order for JUnit to pick it up
 */
actual fun runBlockingMultiplatform( closure: suspend () -> Any ) {
    return GlobalScope.async { closure() }.asPromise().unsafeCast < Unit >()
}