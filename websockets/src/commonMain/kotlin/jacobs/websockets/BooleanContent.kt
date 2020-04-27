package jacobs.websockets

import kotlinx.serialization.Serializable

@Serializable
data class BooleanContent(
    val boolean: Boolean
) : MessageContent