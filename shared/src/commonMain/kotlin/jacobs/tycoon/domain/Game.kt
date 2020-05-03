package jacobs.tycoon.domain

import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player

class Game(
    private val minimumNumberOfPlayers: Int
) {

    lateinit var pieceSet: PieceSet

    private var phase: GamePhase = SignUp
    val players: GamePlayers = GamePlayers()

    fun addPlayer( name: String, piece: PlayingPiece ): Boolean {
        if ( false == this.canNewPlayerJoin() )
            return false
        this.players.addPlayer( Player( name, piece ) )
        return true
    }

    fun canGameStart(): Boolean {
        return this.isSignUpPhase() &&
            this.players.count() >= this.minimumNumberOfPlayers
    }

    fun canNewPlayerJoin(): Boolean {
        return this.isSignUpPhase() && this.players.count() < this.pieceSet.count()
    }

    fun completeSignUp(): Boolean {
        if ( false == this.isSignUpPhase() )
            return false
        this.phase = RollForThrowingOrder
        return true
    }

    fun getAvailablePieces( pieceSet: PieceSet, players: GamePlayers ): List < PlayingPiece > {
        return pieceSet.getAvailablePieces( players )
    }

    fun isSignUpPhase(): Boolean  {
        return this.phase == SignUp
    }

}