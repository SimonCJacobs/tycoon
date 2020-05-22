package jacobs.tycoon.domain.board.squares

import kotlinx.serialization.Serializable

@Serializable
abstract class Square {

    companion object {
        val NULL = NullSquare()
    }

    abstract val indexOnBoard: Int
    abstract val name: String

    abstract fun < T > accept( squareVisitor: SquareVisitor < T > ): T

    override fun equals( other: Any? ): Boolean {
        return other != null &&
            other is Square &&
            other.indexOnBoard == this.indexOnBoard &&
            this::class == other::class
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return this.name
    }

}