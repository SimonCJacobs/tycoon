package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.players.Player

class TradeBeingConsidered(
    override val playerWithTurn: Player,
    val offer: TradeOffer
) : TurnBasedPhase {

    var answer: Boolean = false

    fun isPlayerWhoReceivedOffer( testPlayer: Player ): Boolean {
        return offer.offerRecipient == testPlayer
    }

    fun respondToTrade( answer: Boolean ) {
        this.answer = answer
        if ( answer == true )
            this.offer.putIntoEffect()
    }

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}