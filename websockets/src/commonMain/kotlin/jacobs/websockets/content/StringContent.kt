package jacobs.websockets.content

import jacobs.websockets.content.MessageContent
import kotlinx.serialization.Serializable

@Serializable
data class StringContent(
    val string: String
) : MessageContent