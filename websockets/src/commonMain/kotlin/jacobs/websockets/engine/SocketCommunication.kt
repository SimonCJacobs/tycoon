package jacobs.websockets.engine

import jacobs.websockets.content.MessageContent
import kotlin.reflect.KClass

/**
 * Note that implementing classes must implement an two-arg content and id primary constructor
 */
internal sealed class SocketCommunication {

    abstract val content: MessageContent
    abstract var id: MessageIdentifier?

    companion object {
            // A poor replacement for sealedSubclasses while JS reflection catches up
        private val subclasses = listOf(
            Notification::class, Request::class, Response::class
        )

        private fun getFromName( name: String ): KClass < out SocketCommunication > {
            val matchingClass = subclasses.find {
                it.getIdentifier() == name
            }
            if ( null != matchingClass )
                return matchingClass
            else
                throw Error( "No class of name $name" )
        }

        fun create(name: String, content: MessageContent, id: MessageIdentifier ): SocketCommunication {
            return this.getFromName( name ).createInstance( content, id )
        }
    }

    fun getIdentifier(): String {
        return this::class.getIdentifier()
    }

    abstract suspend fun accept( visitor: CommunicationVisitor )

}

internal sealed class PrimarySocketCommunication : SocketCommunication() {
    fun getResponseWithNewContent( newContent: MessageContent): Response {
        return Response( newContent, this.id!! )
    }
}

internal class Notification(
    override val content: MessageContent,
    override var id: MessageIdentifier? = null
) : PrimarySocketCommunication() {

    override suspend fun accept( visitor: CommunicationVisitor ) {
        visitor.visit( this )
    }
}

internal class Request(
    override val content: MessageContent,
    override var id: MessageIdentifier? = null
) : PrimarySocketCommunication() {

    override suspend fun accept( visitor: CommunicationVisitor ) {
        visitor.visit( this )
    }
}

internal class Response(
    override val content: MessageContent,
    oldId: MessageIdentifier
) : SocketCommunication() {

    override var id: MessageIdentifier? = oldId.cloneForNewCommunicationType( this.getIdentifier() )

    override suspend fun accept( visitor: CommunicationVisitor ) {
        visitor.visit( this )
    }
}

internal expect fun < T : SocketCommunication > KClass < T >.createInstance(content: MessageContent, id: MessageIdentifier ) : T

fun < T : SocketCommunication > KClass < T >.getIdentifier(): String {
    return this.simpleName!!
}