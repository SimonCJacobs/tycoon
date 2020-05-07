package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.results.MoveOutcome
import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.Square
import jacobs.tycoon.domain.dice.Dice
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.DiceRolling
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.domain.phases.SignUp
import jacobs.tycoon.domain.phases.SinglePlayerPhase
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.coroutines.sync.Mutex

class Game(
    private val minimumNumberOfPlayers: Int
) {
    lateinit var board: Board
    val dice = Dice()
    var phase: GamePhase = SignUp
    val players: GamePlayers = GamePlayers()

    private val mutex = Mutex()

    suspend fun addPlayer( name: String, piece: PlayingPiece, position: SeatingPosition ): Boolean {
        var returnValue = false
        mutex.lock()
        if ( this.canNewPlayerJoin() && false == this.players.isThereAPlayerOfName( name ) ) {
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
        return this.isSignUpPhase() && this.players.count() < this.board.getPieceCount()
    }

    fun completeSignUp(): Boolean {
        if ( false == this.isSignUpPhase() )
            return false
        this.phase = RollingForOrder.firstRoll( this )
        this.board.pieceSet.freezePiecesInUse( this.players )
        return true
    }

    fun continueRollingForThrowingOrderPhase( rollingForOrder: RollingForOrder ): Boolean {
        this.phase = rollingForOrder
        return true
    }

    fun getAvailablePieces(): List < PlayingPiece > {
        return this.board.pieceSet.getAvailablePieces( players )
    }

    fun isGameUnderway(): Boolean {
        return false == ( this.phase is SignUp || this.phase is RollingForOrder )
    }

    fun isSignUpPhase(): Boolean  {
        return this.phase is SignUp
    }

    fun isTurnOfPlayer( testPlayer: Player ): Boolean {
        val phase = this.phase
        if ( false == phase is SinglePlayerPhase )
            return false
        else
            return phase.isTurnOfPlayer( testPlayer )
    }

    suspend fun movePiece(): MoveResult {
        val phase = this.phase
        mutex.lock()
        if ( false == phase is MovingAPiece )
            throw Error( "Not time to move a piece" )
        phase.activePlayer.moveToSquare( phase.destinationSquare )
        mutex.unlock()
        return MoveResult( phase.destinationSquare, MoveOutcome.TAX )
    }

    suspend fun rollTheDiceForMove( position: SeatingPosition, maybeDiceRoll: DiceRoll? = null )
            : RollForMoveResult {
        var result = RollForMoveResult.NULL
        mutex.lock()
        val currentPhase = this.phase
        if ( currentPhase is RollingForMove && currentPhase.isTurnOfPlayer( position.getPlayer( this ) ) ) {
            result = currentPhase.actOnRoll( this, this.dice.roll( maybeDiceRoll ) )
        }
        mutex.unlock()
        return result
    }

    /**
     * Not sure it clarifies must to try and jam this together with the the other roll function.
     */
    suspend fun rollTheDiceForThrowingOrder( position: SeatingPosition, maybeDiceRoll: DiceRoll? = null )
            : RollForOrderResult {
        var result = RollForOrderResult.NULL
        mutex.lock()
        val currentPhase = this.phase
        if ( currentPhase is RollingForOrder && currentPhase.isTurnOfPlayer( position.getPlayer( this ) ) ) {
            result = currentPhase.actOnRoll( this, this.dice.roll( maybeDiceRoll ) )
        }
        mutex.unlock()
        return result
    }

    fun setWinnerOfRollForThrowingOrder( winner: Player ) {
        this.phase = RollingForMove( winner )
        this.board.pieceSet.moveAllToSquare( this.board.startingSquare() )
    }

}