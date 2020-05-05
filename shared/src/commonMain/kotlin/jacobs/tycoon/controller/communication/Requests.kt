package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.actions.OpenGameAction
import jacobs.tycoon.domain.actions.PositionalGameAction
import kotlinx.serialization.Serializable

@Serializable
class OpenActionRequest (
    val action: OpenGameAction
) : Request {
    override suspend fun < T > accept( visitor: RequestVisitor < T > ): T {
        return visitor.visit( this )
    }
}

@Serializable
class PositionalActionRequest(
    val positionalAction: PositionalGameAction
) : Request {
    override suspend fun < T > accept( visitor: RequestVisitor < T > ): T {
        return visitor.visit( this )
    }
}
