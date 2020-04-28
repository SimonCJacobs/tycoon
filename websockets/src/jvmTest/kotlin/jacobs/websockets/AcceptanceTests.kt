package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import org.assertj.core.api.Assertions.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class AcceptanceTests {

    companion object {
        const val ENOUGH_MILLISECONDS_FOR_THE_SERVER_TO_START = 2500L // Hopefully enough :)
    }

    private val ownScope = CoroutineScope( Dispatchers.IO )

    @Test
    fun `Starting server without setting wait parameter causes execution to halt`() {
        runBlocking < Unit > {
            val serverPostStartDeferred = ownScope.async { startServerAsync( 8887 ).await() }
            delay( ENOUGH_MILLISECONDS_FOR_THE_SERVER_TO_START ) // Give time for server to do something
            assertThat( serverPostStartDeferred.isActive )
                .describedAs( "Should have not executed code following start" )
                .isTrue()
            // Can't stop the server at present when started in this manner (28.4.20)
        }
    }

    @Test
    fun `Starting server with wait = false causes execution to continue`() {
        runBlocking < Unit > {
            val serverPostStartDeferred = ownScope.async { startServerAsync( 8888, false ).await() }
            delay( ENOUGH_MILLISECONDS_FOR_THE_SERVER_TO_START ) // Give time for server to do something
            assertThat( serverPostStartDeferred.isActive )
                .describedAs( "Should have not executed code following start" )
                .isFalse()
            serverPostStartDeferred.await().close() // Can tidy up
        }
    }

    /**
     * If the server is instructed to wait, the deferred should never
     */
    private fun startServerAsync( serverPort: Int, waitParameter: Boolean? = null ): Deferred < WebSocket > {
        return this.ownScope.async {
            WebSockets().websocketServer {
                coroutineScope = ownScope
                port = serverPort
                if ( null != waitParameter ) wait = waitParameter
            }
        }
    }

}