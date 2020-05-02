package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
class Station (
    override val name: String,
    override val listPrice: Int
) : Property() {

    override fun < T > accept( squareVisitor: SquareVisitor<T>): T {
        return squareVisitor.visit( this )
    }

    override fun rent(): Int {
        TODO("Not yet implemented")
    }


}