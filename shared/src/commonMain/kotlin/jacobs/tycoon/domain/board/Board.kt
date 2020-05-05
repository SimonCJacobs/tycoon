package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
abstract class Board {
    abstract val location: String
    abstract val squareList: List < Square >

    override fun equals( other: Any? ): Boolean {
        return other != null && this::class == other::class
    }

    override fun hashCode(): Int {
        var result = location.hashCode()
        result = 31 * result + squareList.hashCode()
        return result
    }

}