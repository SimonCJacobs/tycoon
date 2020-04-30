package jacobs.websockets.engine

import jacobs.websockets.WebSocketParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
abstract class SocketContext < TParameters : WebSocketParameters < TParameters > > (
    val parameters: TParameters
) {


}