package jacobs.tycoon.domain.actions

import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.Position
import kotlinx.serialization.Serializable

@Serializable
sealed class GameAction {
    var executed = false
    var successful = false

    protected abstract fun args(): Array < Any >

    override fun equals( other: Any? ): Boolean {
        return other != null &&
            this::class == other::class &&
            this.args().size == ( other as GameAction ).args().size &&
            this.args().indices.all { this.args()[ it ] == other.args()[ it ] }
    }

    override fun hashCode(): Int {
        return this::class.hashCode() + this.args().fold( 0 ) { total, arg -> total + arg.hashCode() }
    }
}

/**
 * An action that requires knowledge of the requesting player's position at the game table
 */
@Serializable
sealed class PositionalGameAction : GameAction() {
    var position: Position = Position.UNINITALISED
}

/**
 * The complement to PositionalAction. Position not required, so open to anyone to ask
 */
@Serializable
sealed class OpenGameAction : GameAction()

@Serializable
sealed class NoArgAction : OpenGameAction() {
    override fun args(): Array < Any > = emptyArray()
}

@Serializable
data class AddPlayer ( val playerName: String, val playingPiece: PlayingPiece ) : PositionalGameAction() {
    override fun args() = arrayOf( playerName, playingPiece, position )
}

@Serializable
class CompleteSignUp : NoArgAction ()

@Serializable
class NewGame : NoArgAction ()

@Serializable
class RollTheDice : PositionalGameAction () {
    override fun args(): Array < Any > = arrayOf( position )
    var diceRoll: DiceRoll = DiceRoll.UNROLLED
}

@Serializable
data class SetBoard ( val board: Board ) : OpenGameAction() {
    override fun args(): Array < Any > = arrayOf( board )
}

@Serializable
data class SetPieces ( val pieceSet: PieceSet ) : OpenGameAction() {
    override fun args(): Array < Any > = arrayOf( pieceSet )
}