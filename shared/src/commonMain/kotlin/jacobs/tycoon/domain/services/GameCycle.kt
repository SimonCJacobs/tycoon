package jacobs.tycoon.domain.services

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForMoveOutcome
import jacobs.tycoon.domain.actions.results.RollForOrderOutcome
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.phases.BankruptcyProceedings
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.domain.phases.CrownTheVictor
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.phases.status.PhaseStatusVisitor
import jacobs.tycoon.domain.phases.PotentialPurchase
import jacobs.tycoon.domain.phases.PotentialRentCharge
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.domain.phases.TradeBeingConsidered
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.phases.TurnBasedPhaseVisitor
import jacobs.tycoon.domain.phases.status.TurnStatus
import jacobs.tycoon.domain.phases.status.TurnlessPhaseStatus
import jacobs.tycoon.domain.phases.WrongPhaseException
import jacobs.tycoon.domain.players.SeatingPosition
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameCycle( kodein: Kodein ) {

    private val listeners: MutableList < ( GamePhase ) -> Unit > = mutableListOf()
    private val phasePhactory by kodein.instance < PhasePhactory > ()

    // 1. PUBLIC API

   /**
    * Carry out actions within a (validated) phase, then go to next
    */
    internal inline fun < reified T : GamePhase, R > doInPhaseAndCycle( game: Game, action: T.( Game ) -> R ): R {
        val phase = game.phaseStatus.current() as? T
            ?: throw WrongPhaseException(
                "In phase ${ game.phaseStatus.current()::class } but expected ${ T::class }"
            )
        val returnValue = phase.action( game )
        this.goToNextPhase( game )
        return returnValue
    }

    /**
     * Carry out actions within a (validated) phase, validating that the player with the turn requested
     * the actions, then go to next
     */
    internal inline fun < reified T : TurnBasedPhase, R > doOnTurnPhaseAndCycle( game: Game,
            position: SeatingPosition, action: T.( Game ) -> R ): R {
        return this.doInPhaseAndCycle < T, R > ( game ) { gameInLambda ->
            if ( isTurnOfPlayer( position.getPlayer( gameInLambda ) ) )
                action( gameInLambda )
            else
                throw NotTurnOfPlayerException(
                    "Expected turn of ${ position.getPlayer( gameInLambda ) } but is" +
                        game.phaseStatus.playerWithTurn
                )
        }
    }

    fun startNewTradingPhase( tradeOffer: TradeOffer, game: Game ): Boolean {
        val tradingPhase = phasePhactory.offerTrade( game.phaseStatus.playerWithTurn, tradeOffer )
        val turnBasedPhaseStatus = game.phaseStatus as? TurnStatus
            ?: throw WrongPhaseException( "Not in turn-based phase" )
        turnBasedPhaseStatus.doAsPriority( tradingPhase )
        return true
    }

    fun registerPhaseChangeListener( listener: ( GamePhase ) -> Unit ) {
        this.listeners.add( listener )
    }

    // 2. PRIVATE METHODS TO MANAGE PHASES

    private fun goToNextPhase( game: Game ) {
        val phaseStatusVisitor = PhaseStatusVisitorForNextPhase( game, this.phasePhactory, this )
        game.phaseStatus.accept( phaseStatusVisitor )
    }

    private fun startNewTurn( nextPhase: TurnBasedPhase, game: Game ) {
        game.phaseStatus = TurnStatus( nextPhase )
        this.callListeners( nextPhase )
    }

    private fun callListeners( nextPhase: GamePhase ) {
        this.listeners.forEach { it( nextPhase ) }
    }

    // 3. VISITOR TO PHASE_STATUS TO DETERMINE NEXT PHASE

    class PhaseStatusVisitorForNextPhase(
        private val game: Game,
        private val phasePhactory: PhasePhactory,
        private val gameCycle: GameCycle
    ) : PhaseStatusVisitor {

        override fun visit( turnlessPhaseStatus: TurnlessPhaseStatus) {
                // We must be a sign-up phase
            val nextPhase = this.phasePhactory.startRollingForOrder( game.players.activeList() )
            gameCycle.startNewTurn( nextPhase, game )
        }

        override fun visit( turnStatus: TurnStatus ) {
            val phaseVisitor = PhaseVisitor( turnStatus, game, phasePhactory, gameCycle )
            turnStatus.currentTurnBasedPhase().accept( phaseVisitor )
            if ( false == turnStatus.isThereAWaitingPhase() )
                gameCycle.startNewTurn( this.getFirstPhaseOfNewTurn(), game )
        }

        private fun getFirstPhaseOfNewTurn(): TurnBasedPhase {
            val nextPlayer = game.players.nextActive( game.phaseStatus.playerWithTurn )
            return when {
                game.players.activeCount() == 1 ->
                    this.phasePhactory.crownTheVictor( nextPlayer )
                nextPlayer.location() == game.board.jailSquare ->
                    this.phasePhactory.rollingForMoveFromJail( nextPlayer )
                else -> this.phasePhactory.rollingForMove( nextPlayer )
            }
        }

    }

    // 4. VISITOR TO PHASES TO DECIDE HOW TO DETERMINE NEXT PHASE FROM CURRENT PHASE

    class PhaseVisitor (
        private val phaseStatus: TurnStatus,
        private val game: Game,
        private val phasePhactory: PhasePhactory,
        private val gameCycle: GameCycle
    ) : TurnBasedPhaseVisitor {

        override fun visit( auctionProperty: AuctionProperty ) {
            this.phaseStatus.completePhase()
        }

        override fun visit( bankruptcyProceedings: BankruptcyProceedings ) {
            this.phaseStatus.completePhase()
        }

        override fun visit( cardReading: CardReading ) {
            cardReading.card.action.invoke( this.phasePhactory, this.game, this.phaseStatus.playerWithTurn )
                .doNext()
        }

        override fun visit( crownTheVictor: CrownTheVictor ) {
            throw Error( "Game should have concluded after a victory" )
        }

        override fun visit( movingAPiece: MovingAPiece ) {
            if ( movingAPiece.outcomeGenerator.isNextPhase )
                movingAPiece.outcomeGenerator.nextPhaseLambda.invoke( this.phasePhactory ).doNext()
            else
                this.phaseStatus.completePhase()
        }

        override fun visit( potentialPurchase: PotentialPurchase ) {
            if ( false == potentialPurchase.decidedToBuy )
                this.phasePhactory.auctionProperty(
                    phaseStatus.playerWithTurn, potentialPurchase.targetProperty
                )
                    .doNext()
            else
                this.phaseStatus.completePhase()
        }

        override fun visit( potentialRentCharge: PotentialRentCharge ) {
            when ( potentialRentCharge.result.outcome ) {
                RollForMoveOutcome.BANKRUPTCY_PROCEEDINGS ->
                    this.phasePhactory.bankruptcyProceedings(
                        phaseStatus.playerWithTurn, potentialRentCharge.playerOccupyingProperty
                    )
                        .doNext()
                else -> this.phaseStatus.completePhase()
            }
        }

        override fun visit( rollingForMove: RollingForMove ) {
            when ( rollingForMove.result.outcome ) {
                RollForMoveOutcome.REMAIN_IN_JAIL -> phaseStatus.completePhase()
                else -> this.phasePhactory.movingAPiece(
                    playerWithTurn = rollingForMove.playerWithTurn,
                    destinationSquare = rollingForMove.destinationSquare
                ).doNext()
            }
        }

        override fun visit( rollingForOrder: RollingForOrder ) {
            when ( rollingForOrder.result.nextPhase ) {
                RollForOrderOutcome.ROLLING ->
                    this.phasePhactory.continueRollingForOrder( rollingForOrder.rollResults )
                RollForOrderOutcome.COMPLETE ->
                        this.phasePhactory.rollingForMove( rollingForOrder.result.winner )
                RollForOrderOutcome.ROLL_OFF ->
                    this.phasePhactory.startRollingForOrder( rollingForOrder.result.playersTiedFirst )
            }
                .let { gameCycle.startNewTurn( it, game ) }
        }

        override fun visit( tradeBeingConsidered: TradeBeingConsidered ) {
            this.phaseStatus.completePhase()
        }

        private fun TurnBasedPhase.doNext() {
            phaseStatus.doNext( this )
            gameCycle.callListeners( this )
        }

    }

}