package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.players.Player

class BankruptcyProceedings(
    override val playerWithTurn: Player,
    bankruptPlayer: Player
) : TurnBasedPhase {

    override fun accept(turnBasedPhaseVisitor: TurnBasedPhaseVisitor ) {
        turnBasedPhaseVisitor.visit( this )
    }

}