package jacobs.tycoon.domain.phases.status

import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.players.Player

interface PhaseStatus {
    val playerWithTurn: Player

    fun accept( phaseStatusVisitor: PhaseStatusVisitor)
    fun current(): GamePhase
    fun isCurrent( gamePhase: GamePhase ): Boolean
    fun isItTurnOfPlayer( testPlayer: Player ): Boolean
}

