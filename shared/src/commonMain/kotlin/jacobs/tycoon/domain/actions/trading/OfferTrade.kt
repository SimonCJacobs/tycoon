package jacobs.tycoon.domain.actions.trading

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
class OfferTrade (
    val tradeOffer: TradeOffer
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor<T>): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate(gameController: GameController) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        gameController.offerTrade( tradeOffer, actorPosition )
            .also { setExecutionResult( it ) }
    }
}