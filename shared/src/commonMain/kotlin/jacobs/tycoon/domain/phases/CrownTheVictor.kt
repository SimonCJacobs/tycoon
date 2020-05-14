package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.players.Player

class CrownTheVictor (
    override val playerWithTurn: Player
) : TurnBasedPhase {

    override fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}