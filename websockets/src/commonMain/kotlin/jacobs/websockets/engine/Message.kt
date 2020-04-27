package jacobs.websockets.engine

import kotlinx.serialization.Serializable

@Serializable
internal data class Message (
    val id: MessageIdentifier,
    val serializedContent: String
)