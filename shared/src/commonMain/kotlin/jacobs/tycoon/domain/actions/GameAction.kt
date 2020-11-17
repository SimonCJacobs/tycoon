package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.GameController
import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.serialization.Serializable

@Serializable
abstract class GameAction {

    val executed: Boolean
        get() = executedField
    val successful: Boolean
        get() = successfulField

    private var executedField = false
    private var successfulField = false

    abstract fun < T > accept( visitor: ActionVisitor < T > ): T
    abstract suspend fun duplicate( gameController: GameController )
    abstract suspend fun execute( gameController: GameController )

    protected fun executedSuccessfully() {
        this.executedField = true
        this.successfulField = true
    }

    protected fun executionUnsuccessful() {
        this.executedField = false
        this.successfulField = false
    }

    fun setExecutionResult( result: Boolean ) {
        this.executedField = true
        this.successfulField = result
    }

}