package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
abstract class Square {

    abstract val name: String

    abstract fun < T > accept( squareVisitor: SquareVisitor < T > ): T

}