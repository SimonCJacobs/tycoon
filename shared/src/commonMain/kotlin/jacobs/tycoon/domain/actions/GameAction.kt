package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Position
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class GameAction {
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
sealed class NoArgAction : GameAction() {
    override fun args(): Array < Any > {
        return emptyArray()
    }
}

@Serializable
sealed class OneArgAction < T0 : Any > : GameAction() {
    override fun args(): Array < Any > {
        return arrayOf( singleArg() )
    }
    abstract fun singleArg(): T0
}

@Serializable
sealed class TwoArgAction < T0 : Any, T1 : Any >: GameAction() {
    override fun args(): Array < Any > {
        return argPair().run { arrayOf( first, second ) }
    }
    abstract fun argPair(): Pair < T0, T1 >
}

@Serializable
sealed class ThreeArgAction < T0 : Any, T1 : Any, T2 : Any >: GameAction() {
    override fun args(): Array < Any > {
        return argTriple().run { arrayOf( first, second, third ) }
    }
    abstract fun argTriple(): Triple < T0, T1, T2 >
}

@Serializable
class AddPlayer ( private val playerName: String, private val playingPiece: PlayingPiece, private val position: Position )
        : ThreeArgAction < String, PlayingPiece, Position >() {
    @Transient override val methodName = "addPlayerAsync"
    override fun argTriple(): Triple < String, PlayingPiece, Position > {
        return Triple( playerName, playingPiece, position )
    }
}

@Serializable
object CompleteSignUp : NoArgAction () {
    @Transient override val methodName = "completeSignUpAsync"
}

@Serializable
object NewGame : NoArgAction () {
    @Transient override val methodName = "newGameAsync"
}

@Serializable
class SetBoard ( private val board: Board ) : OneArgAction < Board >() {
    @Transient override val methodName = "setBoardAsync"
    override fun singleArg(): Board { return board }
}

@Serializable
class SetPieces ( private val pieceSet: PieceSet ) : OneArgAction < PieceSet >() {
    @Transient override val methodName = "setPiecesAsync"
    override fun singleArg(): PieceSet { return pieceSet }
}