package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.players.Player

class TradeBeingConsidered(
    override val playerWithTurn: Player,
    private val offer: TradeOffer
) : TurnBasedPhase {

    fun isPlayerWhoReceivedOffer( testPlayer: Player ): Boolean {
        return offer.offerRecipient == testPlayer
    }

    fun respondToTrade( answer: Boolean ) {
        if ( answer == true )
            this.offer.putIntoEffect()
    }

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}