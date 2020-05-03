package jacobs.websockets.engine

import jacobs.websockets.SocketId
import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
class SocketContext (
    val parameters: WebSocketParameters < * >,
    val socket: SocketId
)