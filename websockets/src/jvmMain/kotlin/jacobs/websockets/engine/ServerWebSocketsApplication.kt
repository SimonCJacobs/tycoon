package jacobs.websockets.engine

import io.ktor.application.install
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import jacobs.websockets.ServerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi @KtorExperimentalAPI
internal class ServerWebSocketsApplication(
    timestampFactory: JvmTimestampFactory
) : WebSocketsApplication < ServerParameters > ( timestampFactory ) {

    private val serversByParameters: MutableMap < ServerParameters, ApplicationEngine > = mutableMapOf()

    override fun getPlatformContainerImplementation( timestampFactory: TimestampFactory ): Container < ServerParameters > {
        return ServerContainer( this, timestampFactory )
    }

    override fun closeAll() {
        this.serversByParameters.keys.forEach { this.stopEngine( it ) }
    }

    override suspend fun startEngine( engine: WebSocketEngine, parameters: ServerParameters ) {
        this.serversByParameters.put(
            parameters, this.startNewServer( engine, parameters )
        )
    }

    override fun stopEngine( parameters: ServerParameters ) {
        this.serversByParameters.remove( parameters )!!.stop( 50, 50 )
    }

    /**
     * Note that this function will suspend indefinitely if parameters.wait = true
     */
    private suspend fun startNewServer( engine: WebSocketEngine, parameters: ServerParameters ): ApplicationEngine {
        val server = embeddedServer( Netty, parameters.port ) {
            install( WebSockets )
            routing {
                webSocket( parameters.path ) {
                    engine.startMessageLoop( incoming, outgoing )
                }
            }
        }
        server.start( wait = parameters.wait )
        return server
    }

}