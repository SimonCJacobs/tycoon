package jacobs.tycoon.domain.actions.property

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.actions.results.RentChargeResult
import kotlinx.serialization.Serializable

@Serializable
class RentCharge : GameAction() {

    var result = RentChargeResult.NULL

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = gameController.chargeRent( actorPosition )
        this.executedSuccessfully()
    }
}