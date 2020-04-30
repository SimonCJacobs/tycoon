package jacobs.websockets.engine

import jacobs.websockets.ServerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
internal class ServerContext(
    @Suppress( "unused" ) val socketIndex: Int,
    parameters: ServerParameters
) : SocketContext < ServerParameters >( parameters )