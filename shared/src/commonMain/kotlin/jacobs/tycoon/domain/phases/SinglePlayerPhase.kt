package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.players.Player

abstract class SinglePlayerPhase: GamePhase() {
    protected abstract val activePlayer: Player
    fun isTurnOfPlayer( testPlayer: Player ): Boolean { return this.activePlayer == testPlayer }
}
