package jacobs.websockets

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
class ServerParameters() : WebSocketParameters < ServerParameters >() {

    override var coroutineScope = CoroutineScope( Dispatchers.IO )
    var wait: Boolean = true

}