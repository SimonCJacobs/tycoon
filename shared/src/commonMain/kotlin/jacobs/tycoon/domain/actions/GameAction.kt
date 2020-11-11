package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.state.GameState
import kotlinx.serialization.Serializable

@Serializable
abstract class GameAction {

    var actorPosition: SeatingPosition = SeatingPosition.UNINITIALISED
    var executed = false
    var successful = false

    abstract fun < T > accept( visitor: ActionVisitor < T > ): T
    abstract suspend fun duplicate( gameController: GameController )
    abstract suspend fun execute( gameController: GameController )

    fun setPositionOfActor( position: SeatingPosition ) {
        this.actorPosition = position
    }

    protected fun executedSuccessfully() {
        this.executed = true
        this.successful = true
    }

    protected fun executionUnsuccessful() {
        this.executed = false
        this.successful = false
    }

    protected fun setExecutionResult( result: Boolean ) {
        this.executed = true
        this.successful = result
    }

}