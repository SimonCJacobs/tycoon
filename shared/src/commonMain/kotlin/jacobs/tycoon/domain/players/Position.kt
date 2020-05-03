package jacobs.tycoon.domain.players

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    private val index: Int
)