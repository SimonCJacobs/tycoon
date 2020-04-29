package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.pieces.PlayingPiece
import kotlinx.serialization.Serializable

@Serializable
class AddPlayerRequest (
    val name: String, val piece: PlayingPiece
) : Request {
    override fun < T > accept( visitor: RequestVisitor < T > ): T {
        return visitor.visit( this )
    }
}

@Serializable
class SimpleRequestWrapper(
    val identifier: SimpleRequest
) : Request {
    override fun < T > accept( visitor: RequestVisitor < T > ): T {
        return visitor.visit( this )
    }
}
