package jacobs.tycoon.domain.actions.auction

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import kotlinx.serialization.Serializable

@Serializable
class AuctionBid (
    val bid: CurrencyAmount
) : GameAction() {

    override fun < T > accept(visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        gameController.game().bidFromPosition( bid, actorPosition )
            .also { this.setExecutionResult( it ) }
    }
}