package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
abstract class GameAction {
    var executed = false
    var successful = false

    fun setPositionOfActor( position: SeatingPosition ) {
        this.actorPosition = position
    }

    abstract fun < T > accept( visitor: ActionVisitor < T > ): T
    abstract suspend fun duplicate( gameController: GameController )
    abstract suspend fun execute( gameController: GameController )

    var actorPosition: SeatingPosition = SeatingPosition.UNINITIALISED

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