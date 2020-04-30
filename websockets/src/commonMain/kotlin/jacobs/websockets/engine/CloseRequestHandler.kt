package jacobs.websockets.engine

import jacobs.websockets.WebSocket

internal class CloseRequestHandler(
    private val closeLambda: ( WebSocket ) -> Unit
) {

    fun close( websocket: WebSocket ) {
        this.closeLambda.invoke( websocket )
    }

}