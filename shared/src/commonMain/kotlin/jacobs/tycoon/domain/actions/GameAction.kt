package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.players.Position
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
abstract class GameAction {
    var executed = false
    var successful = false

    fun setPositionOfActor( position: Position ) {
        this.actorPosition = position
    }

    abstract fun < T > accept( visitor: ActionVisitor < T > ): T
    abstract suspend fun duplicate( gameState: GameState )
    abstract suspend fun execute( gameState: GameState )

    var actorPosition: Position = Position.UNINITIALISED

    protected fun executedSuccessfully() {
        this.executed = true
        this.successful = true
    }

    protected fun executedUnsuccessfully() {
        this.executed = true
        this.successful = false
    }

    protected fun setExecutionResult( result: Boolean ) {
        this.executed = true
        this.successful = result
    }
}