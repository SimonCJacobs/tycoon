package jacobs.tycoon.state

import jacobs.tycoon.domain.GamePhase
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class GameUpdate {
    abstract val methodName: String
    abstract fun args(): Array < Any >

    override fun equals( other: Any? ): Boolean {
        if ( other == null )
            return false
        else
            return this::class == other::class
    }

    override fun hashCode(): Int {
        return this.methodName.hashCode()
    }

}

@Serializable
sealed class NoArgUpdate : GameUpdate() {
    override fun args(): Array < Any > {
        return emptyArray()
    }
}

@Serializable
sealed class OneArgUpdate < T0 : Any > : GameUpdate() {
    override fun args(): Array < Any > {
        return arrayOf( singleArg() )
    }
    abstract fun singleArg(): T0
}

@Serializable
sealed class TwoArgUpdate < T0 : Any, T1 : Any >: GameUpdate() {
    override fun args(): Array < Any > {
        return emptyArray()
    }
    abstract fun argPair(): Pair < T0, T1 >
}

@Serializable
class AddPlayer ( private val player: Player ) : OneArgUpdate < Player >() {
    @Transient override val methodName = "addPlayer"
    override fun singleArg(): Player { return player }
}

@Serializable
class SetBoard ( private val board: Board ) : OneArgUpdate < Board >() {
    @Transient override val methodName = "setBoard"
    override fun singleArg(): Board { return board }
}

@Serializable
class SetPieces ( private val pieceSet: PieceSet ) : OneArgUpdate < PieceSet >() {
    @Transient override val methodName = "setPieces"
    override fun singleArg(): PieceSet { return pieceSet }
}

@Serializable
class UpdateStage ( private val gameState: GamePhase ) : OneArgUpdate < GamePhase >() {
    @Transient override val methodName = "updateStage"
    override fun singleArg(): GamePhase { return gameState }
}