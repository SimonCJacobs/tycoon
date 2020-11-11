package jacobs.tycoon.controller.communication.application

import jacobs.tycoon.domain.actions.gameadmin.UpdateCashHoldings
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Suppress( "CanBeParameter" )
@Serializable
class UpdateCashHoldingsRequest (
    private val player: Player,
    private val newCashAmount: CurrencyAmount
) : ApplicationAction() {

    private val action: UpdateCashHoldings = UpdateCashHoldings( player, newCashAmount )

    override val requiresAuthorisation: Boolean
        get() = true

    override suspend fun execute( applicationExecutor: ApplicationExecutor ) {
        applicationExecutor.gameExecutor.execute( this.action )
        this.successful = true
    }

}