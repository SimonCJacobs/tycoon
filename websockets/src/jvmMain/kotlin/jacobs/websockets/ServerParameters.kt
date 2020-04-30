package jacobs.websockets

import jacobs.websockets.content.ContentClassCollection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi @ExperimentalStdlibApi
class ServerParameters() : WebSocketParameters < ServerParameters >() {

    override var coroutineScope = CoroutineScope( Dispatchers.IO )
    var contentClasses: ContentClassCollection = ContentClassCollection()
    var wait: Boolean = true

}