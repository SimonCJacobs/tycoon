package jacobs.websockets.engine

import jacobs.jsutilities.createInstance as externalCreateInstance
import jacobs.websockets.MessageContent
import kotlin.reflect.KClass

internal actual fun < T : SocketCommunication > KClass < T >.createInstance( content: MessageContent, id: MessageIdentifier ) : T {
    return this.externalCreateInstance( content, id )
}
