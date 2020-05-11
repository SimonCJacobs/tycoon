package jacobs.tycoon.domain.services

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.results.RollForOrderOutcome
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.phases.BankruptcyProceedings
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.phases.PhaseStatusVisitor
import jacobs.tycoon.domain.phases.PotentialPurchase
import jacobs.tycoon.domain.phases.PotentialRentCharge
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.phases.TurnBasedPhaseVisitor
import jacobs.tycoon.domain.phases.TurnStatus
import jacobs.tycoon.domain.phases.TurnlessPhaseStatus
import jacobs.tycoon.domain.phases.WrongPhaseException
import jacobs.tycoon.domain.players.SeatingPosition
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameCycle( kodein: Kodein ) {

    private val phasePhactory by kodein.instance < PhasePhactory > ()

    // 1. PUBLIC API

    /**
     * Carry out actions within a (validated) phase, then go to next
     */
    internal inline fun < reified T : GamePhase, R > doInPhaseAndCycle( game: Game, action: T.( Game ) -> R ): R {
        val phase = game.phaseStatus.current() as? T ?: throw WrongPhaseException()
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
                throw NotTurnOfPlayerException()
        }
    }

    // 2. PRIVATE METHODS TO MANAGE PHASES

    private fun goToNextPhase( game: Game ) {
        val phaseStatusVisitor = PhaseStatusVisitorForNextPhase( game, this.phasePhactory )
        game.phaseStatus.accept( phaseStatusVisitor )
    }

    // 3. VISITOR TO PHASE_STATUS TO DETERMINE NEXT PHASE

    class PhaseStatusVisitorForNextPhase(
        private val game: Game, private val phasePhactory: PhasePhactory
    ) : PhaseStatusVisitor {

        override fun visit( turnlessPhaseStatus: TurnlessPhaseStatus ) {
                // We must be a sign-up phase
            val nextPhase = this.phasePhactory.startRollingForOrder( game.players.activeList() )
            this.startNewTurn( nextPhase )
        }

        override fun visit( turnStatus: TurnStatus ) {
            val phaseVisitor = PhaseVisitor( turnStatus, game, phasePhactory )
            turnStatus.currentTurnBasedPhase().accept( phaseVisitor )
            if ( false == turnStatus.isThereAWaitingPhase() )
                this.startNewTurn(
                    this.phasePhactory.rollingForMove(
                        game.players.nextActive( game.phaseStatus.playerWithTurn )
                    )
                )
        }

        private fun startNewTurn( nextPhase: TurnBasedPhase ) {
            game.phaseStatus = TurnStatus( nextPhase )
        }

    }

    // 4. VISITOR TO PHASES TO DECIDE HOW TO DETERMINE NEXT PHASE FROM CURRENT PHASE

    class PhaseVisitor (
        private val phaseStatus: TurnStatus,
        private val game: Game,
        private val phasePhactory: PhasePhactory
    ) : TurnBasedPhaseVisitor {

        override fun visit( auctionProperty: AuctionProperty ) {
            this.phaseStatus.completePhase()
        }

        override fun visit( bankruptcyProceedings: BankruptcyProceedings ) {
            TODO("Not yet implemented")
        }

        override fun visit( cardReading: CardReading ) {
            cardReading.card.action.invoke( this.phasePhactory, this.game, this.phaseStatus.playerWithTurn )
                .doNext()
        }

        override fun visit( movingAPiece: MovingAPiece ) {
            if ( movingAPiece.outcomeGenerator.isNextPhase )
                movingAPiece.outcomeGenerator.nextPhaseLambda.invoke( this.phasePhactory ).doNext()
            else
                this.phaseStatus.completePhase()
        }

        override fun visit( potentialPurchase: PotentialPurchase ) {
            if ( false == potentialPurchase.decidedToBuy )
                this.phasePhactory.auctionProperty( phaseStatus.playerWithTurn, potentialPurchase.targetProperty )
                    .doNext()
            else
                this.phaseStatus.completePhase()
        }

        override fun visit( potentialRentCharge: PotentialRentCharge ) {
            if ( potentialRentCharge.requireBankruptcyProceedings )
                this.phasePhactory.bankruptcyProceedings(
                    phaseStatus.playerWithTurn, potentialRentCharge.playerOccupyingProperty
                )
                    .doNext()
            else
                this.phaseStatus.completePhase()
        }

        override fun visit( rollingForMove: RollingForMove ) {
            this.phasePhactory.movingAPiece(
                playerWithTurn = rollingForMove.playerWithTurn,
                destinationSquare = rollingForMove.destinationSquare
            ).doNext()
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
                .let { game.phaseStatus = TurnStatus( it ) }
        }

        private fun TurnBasedPhase.doNext() {
            phaseStatus.doNext( this )
        }

    }

}