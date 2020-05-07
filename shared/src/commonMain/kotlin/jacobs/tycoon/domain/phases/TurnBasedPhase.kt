package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.players.Player

abstract class TurnBasedPhase : GamePhase() {
    abstract val playerWithTurn: Player
    fun isTurnOfPlayer( testPlayer: Player ): Boolean { return this.playerWithTurn == testPlayer }
}
