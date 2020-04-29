package jacobs.websockets.content

import jacobs.websockets.content.MessageContent
import kotlinx.serialization.Serializable

@Serializable
data class BooleanContent(
    val boolean: Boolean
) : MessageContent