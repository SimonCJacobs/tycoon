package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.results.MoveResult
import jacobs.tycoon.domain.actions.results.ReadCardResult
import jacobs.tycoon.domain.actions.results.RentChargeResult
import jacobs.tycoon.domain.actions.results.RollForMoveResult
import jacobs.tycoon.domain.actions.results.RollForOrderResult
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.cards.Card
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.Dice
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.phases.PhaseStatus
import jacobs.tycoon.domain.phases.PotentialPurchase
import jacobs.tycoon.domain.phases.PotentialRentCharge
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.domain.phases.SignUp
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.phases.TurnStatus
import jacobs.tycoon.domain.phases.TurnlessPhaseStatus
import jacobs.tycoon.domain.phases.WrongPhaseException
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.domain.services.GameCycle
import kotlin.reflect.KClass

/**
 * Class is *NOT* designed to be thread safe. It is intended that only one coroutine at a time will access the game.
 * Monopoly isn't exactly a fast-paced game now is it?
 */
class Game(
    private val minimumNumberOfPlayers: Int
) {

    lateinit var board: Board
    private val dice = Dice()
    @PublishedApi internal var phaseStatus: PhaseStatus = TurnlessPhaseStatus( SignUp )
    val players: GamePlayers = GamePlayers()

    /**
     * *** 1. UPDATING GAME STATE API AS MAIN GAME ACTION ***
     */

    fun addPlayer( possibleNewPlayer: Player ): Boolean {
        if ( canGivenNewPlayerJoin( possibleNewPlayer ) )
            return players.addPlayer( possibleNewPlayer )
        else
            return false
    }

    fun completePieceMove( gameCycle: GameCycle, position: SeatingPosition ): MoveResult {
        return gameCycle.doOnTurnPhaseAndCycle < MovingAPiece, MoveResult > ( this, position ) {
            this.carryOutMove( it )
        }
    }

    fun chargeRent( gameCycle: GameCycle, actorPosition: SeatingPosition ): RentChargeResult {
        return gameCycle.doInPhaseAndCycle < PotentialRentCharge, RentChargeResult > ( this ) {
            val requestingPlayer = actorPosition.getPlayer( it )
            if ( false == this.canPlayerChargeRent( requestingPlayer ) )
                throw NotTurnOfPlayerException(
                    "Requesting player ${ requestingPlayer.name } does not own the occupied property " +
                        this.occupiedProperty.name
                )
            this.chargeRent( requestingPlayer )
        }
    }

    fun completeSignUp( gameCycle: GameCycle) {
        gameCycle.doInPhaseAndCycle < SignUp, Unit > ( this ) {
            this.completeSignUp( it )
        }
    }

    fun readCard( gameCycle: GameCycle, position: SeatingPosition ): ReadCardResult {
        return gameCycle.doOnTurnPhaseAndCycle < CardReading, ReadCardResult > ( this, position ) {
            this.result
        }
    }

    fun respondToPropertyOffer(gameCycle: GameCycle, decidedToBuy: Boolean, position: SeatingPosition ) {
        return gameCycle.doOnTurnPhaseAndCycle < PotentialPurchase, Unit > ( this, position ) {
            this.respondToOffer( decidedToBuy )
        }
    }

    fun rollTheDiceForMove( gameCycle: GameCycle, position: SeatingPosition, maybeDiceRoll: DiceRoll? = null )
            : RollForMoveResult {
        return gameCycle.doOnTurnPhaseAndCycle < RollingForMove, RollForMoveResult > ( this, position ) {
            this.setDiceRoll( dice.roll( maybeDiceRoll ) )
            this.getRollResult( it )
        }
    }

    fun rollTheDiceForThrowingOrder( gameCycle: GameCycle, position: SeatingPosition,
                                    maybeDiceRoll: DiceRoll? = null ) : RollForOrderResult {
        return gameCycle.doOnTurnPhaseAndCycle < RollingForOrder, RollForOrderResult > ( this, position ) {
            this.setDiceRoll( dice.roll( maybeDiceRoll ) )
            this.noteDiceRoll( it )
        }
    }

    // 2. UPDATING GAME STATE FROM MID-PHASE ACTION TODO COULD SEPARATE THIS OUT

    fun moveAllPiecesToStartingSquare() {
        this.board.pieceSet.moveAllToSquare( this.board.startingSquare() )
    }

    // 3. INTERROGATING GAME STATE API TODO COULD SEPARATE THIS OUT

    inline fun < reified T : TurnBasedPhase > anyPastPhasesOfTypeInTurn(): Boolean {
        val turnStatus = phaseStatus as? TurnStatus ?: return false
        return turnStatus.anyPastPhasesOfType < T > ()
    }

    fun canGameStart(): Boolean {
        return doInPhaseOrElse < SignUp, Boolean > ( false ) {
            players.countAll() >= minimumNumberOfPlayers
        }
    }

    fun canAnyNewPlayerJoin(): Boolean {
        return doInPhaseOrElse < SignUp, Boolean > ( false ) {
            players.countAll() < board.getPieceCount()
        }
    }

    fun getAvailablePieces(): List < PlayingPiece > {
        return this.board.pieceSet.getAvailablePieces( players )
    }

    fun getCardBeingRead(): Card {
        return this.doInPhaseOrPastPhasesOfTurn < CardReading, Card > { this.card }
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
        return this.phaseStatus.current() is T
    }

    fun isPropertyOwned( property: Property ): Boolean {
        return this.players.doesAnyoneOwnProperty( property )
    }

    fun isThisThePieceToMove( piece: PlayingPiece ): Boolean {
        return doInPhaseOrElse < MovingAPiece, Boolean > ( false ) { playerWithTurn.piece == piece }
    }

    fun isThisTheSquareToMoveTo( square: Square ): Boolean {
        return doInPhaseOrElse < MovingAPiece, Boolean > ( false ) { isSquareDropTarget( square ) }
    }

    fun isTurnOfPlayer( testPlayer: Player ): Boolean {
        return this.phaseStatus.isItTurnOfPlayer( testPlayer )
    }

    // 4. HELPER METHODS

    private fun canGivenNewPlayerJoin( player: Player ): Boolean {
        return this.canAnyNewPlayerJoin() && false == this.players.isThereAPlayerOfName( player.name )
    }

    /**
     * Carry out actions within a (validated) phase, or else throw
     */
    private inline fun < reified T : GamePhase, R > doInPhase( action: T.() -> R ): R {
        val phase = phaseStatus.current() as? T
            ?: this.throwWrongPhaseException( T::class, phaseStatus.current()::class )
        return phase.action()
    }

    /**
     * Carry out actions within a (validated) phase, or else return
     */
    private inline fun < reified T : GamePhase, R > doInPhaseOrElse( elseReturn: R, action: T.() -> R ): R {
        val phase = phaseStatus.current()
        if ( phase is T )
            return phase.action()
        else
            return elseReturn
    }

    private inline fun < reified T : TurnBasedPhase, R > doInPhaseOrPastPhasesOfTurn( action: T.() -> R ): R {
        val turnStatus = phaseStatus as? TurnStatus
            ?: this.throwWrongPhaseException( T::class, phaseStatus.current()::class )
        return turnStatus.getFirstPhaseOfType < T > ()
            ?.run { action() }
            ?: this.throwWrongPhaseException( T::class, phaseStatus.current()::class )
    }

    private fun throwWrongPhaseException( expected: KClass < out GamePhase >, actual: KClass < out GamePhase > ): Nothing {
        throw WrongPhaseException( "Expected ${ expected.simpleName } got ${ actual.simpleName } ")
    }

}