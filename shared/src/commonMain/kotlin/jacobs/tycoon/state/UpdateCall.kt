package jacobs.tycoon.state

import jacobs.tycoon.domain.GameStage
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.players.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class UpdateCall {
    lateinit var functionName: String
    abstract fun args(): Array < Any >
}

@Serializable
sealed class NoArgUpdateCall : UpdateCall() {
    override fun args(): Array < Any > {
        return emptyArray()
    }
}

@Serializable
sealed class OneArgUpdateCall < T0 : Any > : UpdateCall() {
    override fun args(): Array < Any > {
        return arrayOf( this.run( singleArg() ) )
    }
    abstract fun singleArg(): OneArgUpdateCall < T0 >.() -> T0
}

@Serializable
sealed class TwoArgUpdateCall < T0 : Any, T1 : Any >: UpdateCall() {
    override fun args(): Array < Any > {
        return emptyArray()
    }
    abstract fun argPair(): TwoArgUpdateCall < T0, T1 >.() -> Pair < T0, T1 >
}

@Serializable
class BoardCall ( private val board: Board ) : OneArgUpdateCall < Board >() {
    override fun singleArg(): OneArgUpdateCall < Board >.() -> Board = { board }
}

@Serializable
class GameStateCall ( private val gameState: GameStage ) : OneArgUpdateCall < GameStage >() {
    override fun singleArg(): OneArgUpdateCall < GameStage >.() -> GameStage = { gameState }
}

@Serializable
class PieceSetCall ( private val pieceSet: PieceSet ) : OneArgUpdateCall < PieceSet >() {
    override fun singleArg(): OneArgUpdateCall < PieceSet >.() -> PieceSet = { pieceSet }
}

@Serializable
class PlayerCall ( private val player: Player ) : OneArgUpdateCall < Player >() {
    override fun singleArg(): OneArgUpdateCall < Player >.() -> Player = { player }
}