package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
abstract class Board {
    abstract val squareList: List < Square >
}