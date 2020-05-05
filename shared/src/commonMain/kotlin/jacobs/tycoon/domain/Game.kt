package jacobs.tycoon.domain

import jacobs.tycoon.domain.dice.Dice
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.DiceRollPhase
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.RollForThrowingOrder
import jacobs.tycoon.domain.phases.SignUp
import jacobs.tycoon.domain.phases.SinglePlayerPhase
import jacobs.tycoon.domain.pieces.PieceSet
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.Position
import kotlinx.coroutines.sync.Mutex

class Game(
    private val minimumNumberOfPlayers: Int
) {

    private val mutex = Mutex()

    val dice = Dice()
    var phase: GamePhase = SignUp
    lateinit var pieceSet: PieceSet
    val players: GamePlayers = GamePlayers()

    suspend fun addPlayer( name: String, piece: PlayingPiece, position: Position ): Boolean {
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
        return this.isSignUpPhase() && this.players.count() < this.pieceSet.count()
    }

    fun completeSignUp(): Boolean {
        if ( false == this.isSignUpPhase() )
            return false
        this.phase = RollForThrowingOrder.firstRoll( this )
        return true
    }

    fun continueRollingForThrowingOrder( rollForThrowingOrder: RollForThrowingOrder): Boolean {
        this.phase = rollForThrowingOrder
        return true
    }

    fun getAvailablePieces( pieceSet: PieceSet, players: GamePlayers ): List < PlayingPiece > {
        return pieceSet.getAvailablePieces( players )
    }

    fun isSignUpPhase(): Boolean  {
        return this.phase == SignUp
    }

    fun isTurnOfPlayer( testPlayer: Player ): Boolean {
        val phase = this.phase
        if ( false == phase is SinglePlayerPhase )
            return false
        else
            return phase.isTurnOfPlayer( testPlayer )
    }

    suspend fun rollTheDice( position: Position, knownDiceRoll: DiceRoll? = null ): DiceRoll? {
        var returnValue: DiceRoll? = null
        mutex.lock()
        val currentPhase = this.phase
        if ( currentPhase is DiceRollPhase && currentPhase.isTurnOfPlayer( position.getPlayer( this ) ) ) {
            currentPhase.actOnRoll( this, this.dice.roll( knownDiceRoll ) )
            returnValue = this.dice.lastRoll
        }
        mutex.unlock()
        return returnValue
    }

    fun startGameProperGivenDiceRolls( diceRolls: Map < Player, DiceRoll> ): Boolean {
        TODO()
    }

}