package jacobs.websockets.engine

import jacobs.websockets.WebSocket
import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal abstract class WebSocketsApplication < T : WebSocketParameters > (
    private val timestampFactory: TimestampFactory
) {
        // TODO: Get it to work on socket scopes. currently only good for one socket
        //  TODO: Also figure out how not to duplicate true singletons

    suspend fun getNewWebSocket( parameters: T ): WebSocket {
        val subKodein = Kodein {
            import( getEngineModule < T >( timestampFactory ) )
            import( parameters.asKodeinModule() )
        }
        val scope : CoroutineScope by subKodein.instance()
        scope.launch { launchEngine( subKodein, parameters ) }
        return subKodein.direct.instance()
    }

    private suspend fun launchEngine( subKodein: Kodein, parameters: T ) {
        startEngine( engine = subKodein.direct.instance() , parameters = parameters )
    }

    protected abstract suspend fun startEngine( engine: WebSocketEngine, parameters: T )

}