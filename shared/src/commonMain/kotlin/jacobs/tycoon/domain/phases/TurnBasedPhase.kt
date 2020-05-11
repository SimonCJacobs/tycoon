package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.players.Player

interface TurnBasedPhase : GamePhase {
    val playerWithTurn: Player
    fun accept( turnBasedPhaseVisitor: TurnBasedPhaseVisitor )
    fun isTurnOfPlayer( testPlayer: Player ): Boolean { return this.playerWithTurn == testPlayer }
}
