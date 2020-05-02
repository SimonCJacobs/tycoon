package jacobs.websockets.engine

import jacobs.websockets.ServerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class ServerContext(
    parameters: ServerParameters
) : SocketContext < ServerParameters >( parameters )