package jacobs.websockets

import jacobs.websockets.content.SerializationLibrary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
class ServerParameters() : WebSocketParameters < ServerParameters >() {

    override var coroutineScope = CoroutineScope( Dispatchers.IO )
    var newConnectionHandler: ( Int ) -> Unit = { }
    var serializationLibrary: SerializationLibrary = SerializationLibrary()
    var wait: Boolean = true

}