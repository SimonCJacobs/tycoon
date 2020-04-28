package jacobs.websockets.engine

import jacobs.websockets.WebSocket
import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal abstract class WebSocketsApplication < T : WebSocketParameters < T > > (
    timestampFactory: TimestampFactory
) {
    private val container: Container < T > by lazy { this.getPlatformContainerImplementation( timestampFactory ) }
    private val parametersBySocket: MutableMap < WebSocket, T > = mutableMapOf()

    protected abstract fun getPlatformContainerImplementation( timestampFactory: TimestampFactory ): Container < T >

    abstract fun closeAll()

    fun closeSocket( socket: WebSocket ) {
        this.parametersBySocket.remove( socket )!!
            .let {
                this.container.deleteScopeRegistry( it )
                this.stopEngine( it )
            }
    }

    suspend fun getNewWebSocket( parameters: T ): WebSocket {
        this.container.prepareForNewScope( parameters )
        this.container.getEngine( parameters )
            .also { this.startEngine( it, parameters ) }
        val socket = this.container.getWebSocket( parameters )
        this.parametersBySocket.put( socket, parameters )
        return socket
    }

    protected abstract suspend fun startEngine( engine: WebSocketEngine, parameters: T )
    protected abstract fun stopEngine( parameters: T )

}