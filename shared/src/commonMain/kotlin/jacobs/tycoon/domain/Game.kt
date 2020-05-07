package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.Dice
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.domain.phases.SignUp
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import kotlinx.coroutines.sync.Mutex
import kotlin.reflect.KClass

//TODO: gonna need an overall review of thread safety.
class Game(
    private val minimumNumberOfPlayers: Int
) {
    lateinit var board: Board
    val players: GamePlayers = GamePlayers()

    private val dice = Dice()
    @PublishedApi internal lateinit var phase: GamePhase

    private val mutex = Mutex()

    // 0. INITIALISATION

    fun setGamePhase( newPhase: GamePhase ) {
        this.phase = newPhase
    }

    // 1. UPDATING GAME STATE API AS MAIN GAME ACTION

    suspend fun addPlayer( newPlayer: Player ): Boolean {
        var returnValue = false
        mutex.lock()
        if ( this.canNewPlayerJoin() && false == this.players.isThereAPlayerOfName( newPlayer.name ) ) {
            this.players.addPlayer( newPlayer )
            returnValue = true
        }
        mutex.unlock()
        return returnValue
    }

    fun completeSignUp(): Boolean {
        if ( false == this.isPhase < SignUp >() )
            return false
        this.board.pieceSet.freezePiecesInUse( this.players )
        this.goToNextPhase()
        return true
    }

    suspend fun movePieceCompleted(): MoveResult {
        val movePiecePhase = this.phase
        mutex.lock()
        if ( false == movePiecePhase is MovingAPiece ) {
            mutex.unlock()
            throw Error( "Not time to move a piece" )
        }
        movePiecePhase.processOutcome( this )
        this.goToNextPhase()
        mutex.unlock()
        return movePiecePhase.getMoveResult()
    }

    suspend fun rollTheDiceForMove( position: SeatingPosition, maybeDiceRoll: DiceRoll? = null )
        : RollForMoveResult {
        var result = RollForMoveResult.NULL
        mutex.lock()
        val currentPhase = this.phase
        if ( currentPhase is RollingForMove && currentPhase.isTurnOfPlayer( position.getPlayer( this ) ) ) {
            result = currentPhase.actOnRoll( this, this.dice.roll( maybeDiceRoll ) )
        }
        this.goToNextPhase()
        mutex.unlock()
        return result
    }

    /**
     * Not sure it clarifies anything to try and jam this together with the the other roll function.
     */
    suspend fun rollTheDiceForThrowingOrder( position: SeatingPosition, maybeDiceRoll: DiceRoll? = null )
        : RollForOrderResult {
        var result = RollForOrderResult.NULL
        mutex.lock()
        val currentPhase = this.phase
        if ( currentPhase is RollingForOrder && currentPhase.isTurnOfPlayer( position.getPlayer( this ) ) ) {
            result = currentPhase.actOnRoll( this, this.dice.roll( maybeDiceRoll ) )
        }
        this.goToNextPhase()
        mutex.unlock()
        return result
    }

    // 2. UPDATING GAME STATE FROM MID-PHASE ACTION TODO COULD SEPARATE THIS OUT

    fun moveAllPiecesToStartingSquare() {
        this.board.pieceSet.moveAllToSquare( this.board.startingSquare() )
    }

    // 3. INTERROGATING GAME STATE API TODO COULD SEPARATE THIS OUT

    fun canGameStart(): Boolean {
        return this.isPhase < SignUp >() &&
            this.players.count() >= this.minimumNumberOfPlayers
    }

    fun canNewPlayerJoin(): Boolean {
        return this.isPhase < SignUp >() && this.players.count() < this.board.getPieceCount()
    }

    fun getAvailablePieces(): List < PlayingPiece > {
        return this.board.pieceSet.getAvailablePieces( players )
    }

    fun getLastRoll(): DiceRoll {
        return this.dice.lastRoll
    }

    fun haveDiceBeenRolled(): Boolean {
        return this.dice.haveBeenRolled()
    }

    fun isGameUnderway(): Boolean {
        return false == ( this.isPhase < SignUp >() || this.isPhase < RollingForOrder >() )
    }

    inline fun < reified T : GamePhase > isPhase(): Boolean {
        return this.phase is T
    }

    fun isThisThePieceToMove( piece: PlayingPiece ): Boolean {
        return this.phase.let { it is MovingAPiece && it.playerWithTurn.piece == piece }
    }

    fun isThisTheSquareToMoveTo( square: Square ): Boolean {
        return this.phase.let { it is MovingAPiece && it.isSquareDropTarget( square ) }
    }

    fun isTurnOfPlayer( testPlayer: Player ): Boolean {
        val phase = this.phase
        if ( false == phase is TurnBasedPhase )
            return false
        else
            return phase.isTurnOfPlayer( testPlayer )
    }

    // 4. HELPER METHODS

    private fun goToNextPhase() {
        this.phase = this.phase.nextPhase( this )
    }

}