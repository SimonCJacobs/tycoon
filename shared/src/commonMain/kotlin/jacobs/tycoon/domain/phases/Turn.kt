package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.players.Player

class Turn(
    val player: Player,
    openingPhase: GamePhase
) {
    private val completedPhases: MutableList < GamePhase > = mutableListOf()
    private val pendingPhases: MutableList < GamePhase > = mutableListOf( openingPhase )

}