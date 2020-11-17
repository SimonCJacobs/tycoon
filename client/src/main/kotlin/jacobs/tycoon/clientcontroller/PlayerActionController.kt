package jacobs.tycoon.clientcontroller

import jacobs.tycoon.clientstate.ClientState
import jacobs.tycoon.domain.actions.cards.PayFineOrTakeCardDecision
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.phases.AcceptingFunds
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.domain.phases.DiceRollingPhase
import jacobs.tycoon.domain.phases.PayingFineOrTakingCard
import jacobs.tycoon.domain.phases.PotentialPurchase
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForMoveFromJail
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

    fun acceptFunds() {
        launch { outgoingRequestController.acceptFunds() }
    }

    fun areThereFundsToAccept(): Boolean {
        return gameState.game().isPhase < AcceptingFunds > ()
    }

    fun attemptToPay() {
        launch { outgoingRequestController.attemptToPay() }
    }

    fun buyProperty() {
        launch { outgoingRequestController.respondToPropertyOffer( true ) }
    }

    fun canPlayerAffordProperty(): Boolean {
        return this.player.cashHoldings >= ( this.player.location() as Property ).listPrice
    }

    fun chargeRent( property: Property ) {
        launch { outgoingRequestController.chargeRent( property ) }
    }

    fun doesPlayerHaveGetOutOfJailFreeCard(): Boolean {
        return this.player.hasGetOutOfJailFreeCard()
    }

    fun getBillAmount(): String {
        return gameState.game().getAmountOfPaymentDue().toString()
    }

    fun getBillReason(): String {
        return gameState.game().getPaymentReason()
    }

    fun getJailFine(): String {
        return gameState.game().getJailFine().toString()
    }

    fun getPropertyPrice(): String {
        return ( player.location() as Property ).listPrice.toString()
    }

    fun hideDealingCell() {
        this.clientState.isComposingDeal = false
    }

    fun isItOwnTurn(): Boolean {
        return gameState.game().isTurnOfPlayer( this.player )
    }

    fun isItTimeToRollTheDice(): Boolean {
        return this.gameState.game().isPhase < DiceRollingPhase > ()
    }

    fun isPlayerComposingDeal(): Boolean {
        return this.clientState.isComposingDeal
    }

    fun isPlayerRollingOutOfInJail(): Boolean {
        return gameState.game().isPhase < RollingForMoveFromJail > ()
    }

    fun isThereABillToPay(): Boolean {
        return this.gameState.game().isPaymentDueFromPlayer( player )
    }

    fun isThereACardToRead(): Boolean {
        return this.gameState.game().isPhase < CardReading > ()
    }

    fun isThereAChanceToBuyProperty(): Boolean {
        return this.gameState.game().isPhase < PotentialPurchase > ()
    }

    fun isThereAChanceToChargeRent(): Boolean {
        return playerIdentifier.doIfAPlayerOnThisMachineOrFalse {
            this.gameState.game().canPlayerChargeRent( it )
        }
    }

    fun < T > mapOnPropertiesCanChargeRent( callback: ( Property ) -> T ): List < T > {
        return playerIdentifier.doIfAPlayerOnThisMachine( emptyList() ) {
            this.gameState.game().propertiesOnWhichPlayerCanChargeRent( it )
                .map( callback )
        }
    }

    fun payingFineOrTakingChance(): Boolean {
        return this.gameState.game().isPhase < PayingFineOrTakingCard >()
    }

    fun payFineNotTakeChance() {
        return this.payFineOrTakeChance( PayFineOrTakeCardDecision.PAY_FINE )
    }

    fun payJailFine() {
        launch { outgoingRequestController.payJailFine() }
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
        this.clientState.isComposingDeal = true
    }

    fun takeChance() {
        return this.payFineOrTakeChance( PayFineOrTakeCardDecision.TAKE_CARD )
    }

    fun useGetOutOfJailFreeCard() {
        launch { outgoingRequestController.useGetOutOfJailFreeCard() }
    }

    private suspend fun rollTheDiceSuspended() {
        val game = gameState.game()
        when {
            game.isPhase < RollingForMoveFromJail > () -> this.outgoingRequestController.rollTheDiceFromJail()
            game.isPhase < RollingForMove > () -> this.outgoingRequestController.rollTheDiceForMove()
            game.isPhase < RollingForOrder > () -> this.outgoingRequestController.rollTheDiceForOrder()
            else -> throw Error ( "Should not get here: dice roll should not be possible when not expected" )
        }
    }

    private fun payFineOrTakeChance( decision: PayFineOrTakeCardDecision ) {
        launch { outgoingRequestController.payFineOrTakeChance( decision ) }
    }

}