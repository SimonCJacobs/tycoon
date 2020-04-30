package jacobs.websockets

import io.ktor.util.KtorExperimentalAPI
import org.assertj.core.api.Assertions.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
class AcceptanceTests {

    companion object {
        const val ENOUGH_MILLISECONDS_FOR_THE_SERVER_TO_START = 2500L // Hopefully enough :)
        const val PORT = 8888
    }

    private val sockets = ServerWebSockets()
    private val ownScope = CoroutineScope( Dispatchers.IO )

    @Test
    fun `Starting server without setting wait parameter causes execution to halt`() {
        runBlocking {
            val serverPostStartJob = ownScope.async { startServerAsync().join() }
            delay( ENOUGH_MILLISECONDS_FOR_THE_SERVER_TO_START ) // Give time for server to do something
            assertThat( serverPostStartJob.isActive )
                .describedAs( "Should have not executed code following start, so job should be ongoing" )
                .isTrue()
            sockets.close()
        }
    }

    @Test
    fun `Starting server with wait = false causes execution to continue`() {
        runBlocking {
            val serverPostStartJob = ownScope.async { startServerAsync( false ).join() }
            delay( ENOUGH_MILLISECONDS_FOR_THE_SERVER_TO_START ) // Give time for server to do something
            assertThat( serverPostStartJob.isActive )
                .describedAs( "Should have not executed code following start, so job should be finished" )
                .isFalse()
            sockets.close()
        }
    }

    /**
     * If the server is instructed to wait, the deferred should never
     */
    private fun startServerAsync( waitParameter: Boolean? = null ): Job {
        return this.ownScope.launch {
            sockets.startServer {
                coroutineScope = ownScope
                port = PORT
                if ( null != waitParameter ) wait = waitParameter
            }
        }
    }

}