package jacobs.tycoon.domain.actions.property

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.PositionalGameAction
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.phases.results.RentChargeResult
import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.serialization.Serializable

@Serializable
class RentCharge(
    private val property: Property,
    override val playerPosition: SeatingPosition
) : PositionalGameAction() {

    var result = RentChargeResult.NULL

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        this.result = gameController.chargeRent( property, playerPosition )
        this.executedSuccessfully()
    }
}