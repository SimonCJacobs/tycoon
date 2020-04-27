package jacobs.websockets

import kotlinx.serialization.Serializable

@Serializable
data class StringContent(
    val string: String
) : MessageContent