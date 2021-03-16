package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player

class PotentialPurchase (
    override val playerWithTurn: Player,
    val targetProperty: Property
) : TurnBasedPhase {

    private var maybeDecidedToBuy: Boolean? = null
    val decidedToBuy: Boolean
        get() = maybeDecidedToBuy ?: throw Error( "Early request for decision: Not responded to purchase offer yet" )

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

    fun respondToOffer( decidedToBuy: Boolean ) {
        if ( decidedToBuy )
            this.buyProperty()
        this.maybeDecidedToBuy = decidedToBuy
    }

    private fun buyProperty() {
        if ( playerWithTurn.cashHoldings < this.targetProperty.listPrice )
            throw Error( "Cannot buy the property" )
        this.playerWithTurn.buyAtListPrice(  this.targetProperty )
    }

}