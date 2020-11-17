package jacobs.tycoon.domain.actions.auction

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.PositionalGameAction
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.serialization.Serializable

@Serializable
class AuctionBid (
    val bid: CurrencyAmount,
    override val playerPosition: SeatingPosition
) : PositionalGameAction() {

    override fun < T > accept(visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        gameController.game().bidFromPosition( bid, playerPosition )
            .also { this.setExecutionResult( it ) }
    }
}