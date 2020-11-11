package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.actions.GameAction
import kotlinx.serialization.Serializable

@Serializable
class GameActionRequest (
    val action: GameAction
) : Request {

    override suspend fun < T > accept( visitor: RequestVisitor < T > ): T {
        return visitor.visit( this )
    }

}
