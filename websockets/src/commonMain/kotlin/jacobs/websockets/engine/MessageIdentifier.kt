package jacobs.websockets.engine

import kotlinx.serialization.Serializable

@Serializable
internal data class MessageIdentifier (
    val communicationType: String,
    private val timestamp: String,
    private val timestampCount: Int
) {

    fun cloneForNewCommunicationType( communicationIdentifier: String ): MessageIdentifier {
        return MessageIdentifier(
            communicationType = communicationIdentifier,
            timestamp = this.timestamp,
            timestampCount = this.timestampCount
        )
    }

    fun matches( otherId: MessageIdentifier ): Boolean {
        return this.timestamp == otherId.timestamp &&
            this.timestampCount == otherId.timestampCount
    }

}