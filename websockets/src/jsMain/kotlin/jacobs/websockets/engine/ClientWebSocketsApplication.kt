package jacobs.websockets.engine

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.util.KtorExperimentalAPI
import jacobs.websockets.ClientWebSocketParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ClientWebSocketsApplication  (
    timestampFactory: JsTimestampFactory
) : WebSocketsApplication < ClientWebSocketParameters > ( timestampFactory ) {

    override suspend fun startEngine( engine: WebSocketEngine,
            parameters: ClientWebSocketParameters ) {
        HttpClient ( Js ) {
            install( WebSockets )
        }
            .ws(
                method = HttpMethod.Get,
                host = parameters.hostname,
                port = parameters.port,
                path = parameters.path
            ) {
                engine.startMessageLoop( incoming, outgoing )
            }
    }

}