package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.domain.phases.DiceRollingPhase
import jacobs.tycoon.domain.phases.PotentialPurchase
import jacobs.tycoon.domain.phases.PotentialRentCharge
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.state.GameState
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PlayerActionController(
    kodein: Kodein,
    private val player: Player
): UserInterfaceController( kodein ) {

    private val clientState by kodein.instance < ClientState > ()
    private val gameState by kodein.instance < GameState > ()
    private val outgoingRequestController by kodein.instance < OutgoingRequestController > ()

    fun buyProperty() {
        launch { outgoingRequestController.respondToPropertyOffer( true ) }
    }

    fun canPlayerAffordProperty(): Boolean {
        return this.player.cashHoldings >= ( this.player.location() as Property ).listPrice
    }

    fun chargeRent() {
        launch { outgoingRequestController.chargeRent() }
    }

    fun getPropertyPrice(): String {
        return ( player.location() as Property ).listPrice.toString()
    }

    fun isItOwnTurn(): Boolean {
        return gameState.game().isTurnOfPlayer( this.player )
    }

    fun isItTimeToRollTheDice(): Boolean {
        return this.gameState.game().isPhase < DiceRollingPhase > ()
    }

    fun isThereACardToRead(): Boolean {
        return this.gameState.game().isPhase < CardReading > ()
    }

    fun isThereAChanceToBuyProperty(): Boolean {
        return this.gameState.game().isPhase < PotentialPurchase > ()
    }

    fun isThereAChanceToChargeRent(): Boolean {
        return this.gameState.game().isPhase < PotentialRentCharge > ()
    }

    fun readCard() {
        launch { outgoingRequestController.readCard() }
    }

    fun rollTheDice() {
        launch { rollTheDiceSuspended() }
    }

    fun sendForAuction() {
        launch { outgoingRequestController.respondToPropertyOffer( false ) }
    }

    fun startComposingDeal() {
        this.clientState.isPlayerComposingDeal = true
    }

    private suspend fun rollTheDiceSuspended() {
        val game = gameState.game()
        when {
            game.isPhase < RollingForMove > () -> this.outgoingRequestController.rollTheDiceForMove()
            game.isPhase <RollingForOrder> () -> this.outgoingRequestController.rollTheDiceForOrder()
            else -> throw Error ( "Should not get here: dice roll should not be possible when not expected" )
        }
    }

}