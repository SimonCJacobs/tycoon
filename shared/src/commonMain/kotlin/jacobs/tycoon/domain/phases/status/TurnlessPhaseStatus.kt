package jacobs.tycoon.domain.phases.status

import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.players.Player

/**
 * Only a single-phase implementation because of the requirements of the game, which is (of course!)
 * primarily turn based.
 */
class TurnlessPhaseStatus(
    private val phase: GamePhase
) : PhaseStatus {

    override val playerWithTurn: Player
        get() = throw NotTurnOfPlayerException( "Asking for turn when not a turn-based phase" )

    private var complete = false

    override fun current(): GamePhase {
        return this.phase
    }

    override fun isItTurnOfPlayer( testPlayer: Player ): Boolean {
        return false
    }

    override fun accept( phaseStatusVisitor: PhaseStatusVisitor) {
        phaseStatusVisitor.visit( this )
    }

}