package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.players.Player

interface PhaseStatus {
    val playerWithTurn: Player

    fun current(): GamePhase
    fun isItTurnOfPlayer( testPlayer: Player ): Boolean

    fun accept( phaseStatusVisitor: PhaseStatusVisitor )
}

