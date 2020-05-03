package jacobs.tycoon.domain

import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.Position
import jacobs.websockets.SocketId
import kotlinx.coroutines.sync.Mutex

class Game(
    private val minimumNumberOfPlayers: Int
) {

    private val mutex = Mutex()

    lateinit var pieceSet: PieceSet

    private var phase: GamePhase = SignUp
    val players: GamePlayers = GamePlayers()

    suspend fun addPlayer( name: String, piece: PlayingPiece, position: Position ): Boolean {
        println( "adding player in game with $name ${piece.name} and $position")
        var returnValue = false
        mutex.lock()
        if ( this.canNewPlayerJoin() || false == this.players.isThereAPlayerOfName( name ) ) {
            this.players.addPlayer( Player( name, piece, position ) )
            returnValue = true
        }
        mutex.unlock()
        return returnValue
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