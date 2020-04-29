package jacobs.websockets.engine

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.ClientParameters
import jacobs.websockets.content.ContentClassCollection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ClientWebSocketsApplication (
    private val contentClasses: ContentClassCollection,
    private val timestampFactory: JsTimestampFactory
) : WebSocketsApplication < ClientParameters > () {

    private lateinit var client: HttpClient

    override fun getPlatformContainerImplementation(): Container < ClientParameters > {
        return ClientContainer( this, this.contentClasses, this.timestampFactory )
    }

    override fun closeAll() {
        TODO("Not yet implemented")
    }

    override suspend fun startEngine( engine: WebSocketEngine, parameters: ClientParameters ) {
        parameters.coroutineScope.launch { launchEngine( engine, parameters ) }
    }

    private suspend fun launchEngine( engine: WebSocketEngine, parameters: ClientParameters  ) {
        this.client = HttpClient ( Js ) { install( WebSockets ) }
        this.client.ws(
            method = HttpMethod.Get,
            host = parameters.hostname,
            port = parameters.port,
            path = parameters.path
        ) {
            engine.startMessageLoop( incoming, outgoing )
        }
    }

    override fun stopEngine( parameters: ClientParameters ) {
            // TODO: Store clients and stop the right one (can only do one atm)
        this.client.close()
    }

}