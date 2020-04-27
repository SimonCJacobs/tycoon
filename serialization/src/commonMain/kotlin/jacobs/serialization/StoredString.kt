package jacobs.serialization

import kotlinx.serialization.Serializable

@Serializable
class StoredString(
    val content: String
) : StoredSerializable