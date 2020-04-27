package jacobs.websockets.engine

import jacobs.websockets.MessageContent
import kotlin.reflect.KClass

internal actual fun < T : SocketCommunication > KClass < T >
        .createInstance( content: MessageContent, id: MessageIdentifier ) : T {
    return this.java
        .getDeclaredConstructor( MessageContent::class.java, MessageIdentifier::class.java )
        .newInstance( content, id )
}