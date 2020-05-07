package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
abstract class Square {

    companion object {
        val NULL = NullSquare()
    }

    abstract val name: String

    abstract fun < T > accept( squareVisitor: SquareVisitor < T > ): T

    override fun equals( other: Any? ): Boolean {
        return other != null && other is Square && other.name == this.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}