package jacobs.tycoon.controller.communication

import jacobs.tycoon.domain.actions.GameActionWithResponse
import kotlinx.serialization.Serializable

@Serializable
class GameActionRequestWithResponse (
    override val action: GameActionWithResponse
) : GameActionRequest() {

    override suspend fun < T > accept( visitor: RequestVisitor < T > ): T {
        return visitor.visit( this )
    }

}
