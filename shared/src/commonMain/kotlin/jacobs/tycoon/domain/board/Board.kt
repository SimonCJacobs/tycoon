package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
abstract class Board {
    abstract val location: String
    abstract val squareList: List < Square >
}