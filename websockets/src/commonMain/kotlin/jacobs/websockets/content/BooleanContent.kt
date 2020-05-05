package jacobs.websockets.content

import kotlinx.serialization.Serializable

@Serializable
data class BooleanContent(
    val boolean: Boolean
) : MessageContent