package jacobs.websockets.engine

import io.ktor.application.install
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import jacobs.websockets.ServerWebSocketParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class ServerWebSocketsApplication(
    timestampFactory: JvmTimestampFactory
) : WebSocketsApplication < ServerWebSocketParameters > ( timestampFactory ) {

    override suspend fun startEngine( engine: WebSocketEngine, parameters: ServerWebSocketParameters ) {
        val server: NettyApplicationEngine = embeddedServer( Netty, parameters.port ) {
            install( WebSockets )
            routing {
                webSocket( parameters.path ) {
                    engine.startMessageLoop( incoming, outgoing )
                }
            }
        }
        server.start( wait = true )
    }

}