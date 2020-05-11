package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.players.Player

class AuctionProperty (
    override val playerWithTurn: Player,
    private val targetProperty: Property
) : TurnBasedPhase {

    override fun accept(turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}