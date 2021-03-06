package jacobs.tycoon.domain.actions.gameadmin

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.actions.ActionVisitor
import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
class UpdateCashHoldings (
    val player: Player,
    val newCashAmount: CurrencyAmount
) : GameAction() {

    override fun < T > accept( visitor: ActionVisitor < T > ): T {
        return visitor.visit( this )
    }

    override suspend fun duplicate( gameController: GameController ) {
        this.execute( gameController )
    }

    override suspend fun execute( gameController: GameController ) {
        val actualPlayer = gameController.game().players.getActualPlayer( player )
        gameController.updateCashHoldings( actualPlayer, newCashAmount )
        this.executedSuccessfully()
    }

}