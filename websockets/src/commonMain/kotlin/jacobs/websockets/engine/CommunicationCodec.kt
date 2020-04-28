package jacobs.websockets.engine

import jacobs.websockets.MessageContent
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

internal class CommunicationCodec( kodein: Kodein ) {

    private val jsonSerializer by kodein.instance < JsonSerializer > ()
    private val timestampFactory by kodein.instance < TimestampFactory > ()

    private var lastTimestamp: String = "0"
    private var timestampCounter: Int = 0

    fun getCommunicationFromIncomingMessage( message: Message ): SocketCommunication {
        val content = this.jsonSerializer.deserializeContent( message.serializedContent )
        return SocketCommunication.create( message.id.communicationType, content, message.id )
    }

    fun getMessageFromPrimaryCommunication( communication: PrimarySocketCommunication ): Message {
        return this.getMessageFromIdAndContent(
            this.getNewMessageIdentifier( communication ), communication.content
        )
    }

    fun getMessageFromResponse( response: Response ): Message {
        return this.getMessageFromIdAndContent(
            response.id!!,
            response.content
        )
    }

    private fun getMessageFromIdAndContent( id: MessageIdentifier, content: MessageContent ): Message {
        return Message(
            id = id,
            serializedContent = this.jsonSerializer.serializeContent( content )
        )
    }

    private fun getNewMessageIdentifier( communication: PrimarySocketCommunication ): MessageIdentifier {
        val ( timestamp, timestampCount ) = this.newTimeIdentifier()
        return MessageIdentifier(
            communicationType = communication.getIdentifier(),
            timestamp = timestamp,
            timestampCount = timestampCount
        )
    }

    private fun newTimeIdentifier(): TimeIdentifier {
        val timestampNow = this.timestampFactory.getTimestampNow().toString( 16 )
        val timestampCount =
            if ( timestampNow == lastTimestamp )
                ++timestampCounter
            else
                0
                    .also { timestampCounter = 0 }
        return TimeIdentifier(timestampNow, timestampCount)
    }

    data class TimeIdentifier (
        val timestamp: String,
        val timestampCount: Int
    )

}