package jacobs.tycoon.domain.services

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.actions.trading.Assets
import jacobs.tycoon.domain.phases.results.RollForOrderOutcome
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.phases.AcceptingFunds
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.phases.BankruptcyProceedings
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.domain.phases.CrownTheVictor
import jacobs.tycoon.domain.phases.DealingWithMortgageInterestOnTransfer
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.phases.PayingFineOrTakingCard
import jacobs.tycoon.domain.phases.PaymentDue
import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.phases.status.PhaseStatusVisitor
import jacobs.tycoon.domain.phases.PotentialPurchase
import jacobs.tycoon.domain.phases.rent.PotentialRentCharge
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForMoveFromJail
import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.domain.phases.TradeBeingConsidered
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.phases.TurnBasedPhaseVisitor
import jacobs.tycoon.domain.phases.status.TurnStatus
import jacobs.tycoon.domain.phases.status.TurnlessPhaseStatus
import jacobs.tycoon.domain.phases.WrongPhaseException
import jacobs.tycoon.domain.phases.results.JailOutcome
import jacobs.tycoon.domain.phases.results.PayFineOrTakeChanceOutcome
import jacobs.tycoon.domain.phases.results.RentChargeResult
import jacobs.tycoon.domain.phases.results.RollForMoveResult
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.domain.rules.MiscellaneousRules
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameCycle( kodein: Kodein ) {

    private val listeners: MutableList < ( GamePhase ) -> Unit > = mutableListOf()
    private val miscellaneousRules by kodein.instance < MiscellaneousRules >()
    private val phasePhactory by kodein.instance < PhasePhactory > ()

    // 1. PUBLIC API

    fun dealWithRentCharge( result: RentChargeResult, game: Game ) {
        if ( game.players.isPlayerInGame( result.occupyingPlayer ) == false ) return
        val paymentDue = phasePhactory.paymentDue(
            playerWithTurn = game.phaseStatus.playerWithTurn,
            playingOwingMoney = result.occupyingPlayer,
            amountDue = result.rentDue,
            reason = "rent for ${ result.occupiedProperty }",
            playerOwedMoney = result.propertyOwner
        )
        ( game.phaseStatus as TurnStatus ).doAsPriority( paymentDue )
    }

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

    fun registerPhaseChangeListener( listener: ( GamePhase ) -> Unit ) {
        this.listeners.add( listener )
    }

    fun startNewTradingPhase( tradeOffer: TradeOffer, game: Game ): Boolean {
        val tradingPhase = phasePhactory.offerTrade( game.phaseStatus.playerWithTurn, tradeOffer )
        val turnBasedPhaseStatus = game.phaseStatus as? TurnStatus
            ?: throw WrongPhaseException( "Not in turn-based phase" )
        turnBasedPhaseStatus.doAsPriority( tradingPhase )
        return true
    }

    fun getPlayerToGoNext( game: Game ): Player {
        if ( game.phaseStatus.playerWithTurn.justRolledADouble() &&
            game.phaseStatus.playerWithTurn.location() != game.board.jailSquare
        )
            return game.phaseStatus.playerWithTurn
        else
            return game.players.nextActive( game.phaseStatus.playerWithTurn )
    }

    private fun numberOfTurnsHaveToChargeRent(): Int {
        return miscellaneousRules.numberOfTurnsHaveToChargeRent
    }

    // 2. PRIVATE METHODS TO MANAGE PHASES

    private fun goToNextPhase( game: Game ) {
        val phaseStatusVisitor = PhaseStatusVisitorForNextPhase( game, this.phasePhactory, this )
        game.phaseStatus.accept( phaseStatusVisitor )
    }

    private fun callListeners( game: Game ) {
        this.listeners.forEach { it( game.phaseStatus.current() ) }
        println( "in phase ${ game.phaseStatus.current()::class }")
    }

    // 3. VISITOR TO PHASE_STATUS TO DETERMINE NEXT PHASE

    class PhaseStatusVisitorForNextPhase(
        private val game: Game,
        private val phasePhactory: PhasePhactory,
        private val gameCycle: GameCycle
    ) : PhaseStatusVisitor {

        private var maybePotentialRentCharge: PotentialRentCharge? = null

        override fun visit( turnlessPhaseStatus: TurnlessPhaseStatus) {
                // We must be in a sign-up phase
            val nextPhase = this.phasePhactory.startRollingForOrder( game.players.activeOrderedList() )
            game.startNewTurn( TurnStatus( nextPhase ) )
        }

        override fun visit( turnStatus: TurnStatus ) {
            this.moveAlongPhaseDependingOnCurrentPhase( turnStatus )
            this.startNewTurnIfRequired( turnStatus )
            gameCycle.callListeners( game )
        }

        private fun moveAlongPhaseDependingOnCurrentPhase( turnStatus: TurnStatus ) {
            val phaseVisitor = PhaseVisitorToCompleteCurrentAndMoveToNext(
                turnStatus, game, phasePhactory, gameCycle
            )
            turnStatus.acceptVisitToCurrentPhase( phaseVisitor )
            this.maybePotentialRentCharge = phaseVisitor.maybePotentialRentCharge
        }

        private fun startNewTurnIfRequired( turnStatus: TurnStatus ) {
            if ( turnStatus.isThereACurrentPhase() == false )
                this.getFirstPhaseOfNewTurn()
                    .also { this.startNewTurn( turnStatus, it ) }
        }

        private fun getFirstPhaseOfNewTurn(): TurnBasedPhase {
            val nextPlayer = this.gameCycle.getPlayerToGoNext( game )
            return if ( nextPlayer.location() == game.board.jailSquare )
                    this.phasePhactory.rollingForMoveFromJail( nextPlayer )
                else
                    this.phasePhactory.rollingForMove( nextPlayer )
        }

        private fun startNewTurn( lastTurn: TurnStatus, nextPhase: TurnBasedPhase ) {
            game.startNewTurn( TurnStatus( nextPhase, this.getPotentialRentCharges( lastTurn ) ) )
        }

        private fun getPotentialRentCharges( lastTurn: TurnStatus ): List < PotentialRentCharge > {
            return lastTurn.potentialRentCharges
                .apply { forEach { it.incrementTurns() } }
                .filter { it.turnsBeenChargeable < gameCycle.numberOfTurnsHaveToChargeRent() }
                .toMutableList()
                .apply { maybePotentialRentCharge?.let { add( it ) } }
        }

    }

    // 4. VISITOR TO PHASES TO DECIDE HOW TO DETERMINE NEXT PHASE FROM CURRENT PHASE

    class PhaseVisitorToCompleteCurrentAndMoveToNext (
        private val phaseStatus: TurnStatus,
        private val game: Game,
        private val phasePhactory: PhasePhactory,
        private val gameCycle: GameCycle
    ) : TurnBasedPhaseVisitor {

        var maybePotentialRentCharge: PotentialRentCharge? = null

        override fun visit( acceptingFunds: AcceptingFunds ) {
            this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
        }

        override fun visit( auctionProperty: AuctionProperty ) {
            this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
        }

        override fun visit( bankruptcyProceedings: BankruptcyProceedings ) {
            when {
                this.game.players.activeCount() == 1 ->
                    this.phaseStatus.completeAndDoAsPriority(
                        this.phasePhactory.crownTheVictor( this.game.players.getSoleActivePlayer() )
                    )
                bankruptcyProceedings.areAuctionsRequired() ->
                    bankruptcyProceedings.forEachPropertyOfBankruptPlayer {
                        phasePhactory.auctionProperty( bankruptcyProceedings.playerWithTurn, it )
                            .doAsPriority()
                    }
                else -> this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
            }
        }

        override fun visit( cardReading: CardReading ) {
            cardReading.card.action.invoke(
                this.phasePhactory, this.phaseStatus.playerWithTurn
            )
                ?.completeAndDoAsPriority()
                ?: this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
        }

        override fun visit( crownTheVictor: CrownTheVictor ) {
            throw Error( "Game should have concluded after a victory" )
        }

        override fun visit( dealingWithMortgageInterestOnTransfer: DealingWithMortgageInterestOnTransfer ) {
            this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
        }

        override fun visit( movingAPiece: MovingAPiece ) {
            if ( movingAPiece.outcomeGenerator.isNextPhase )
                movingAPiece.outcomeGenerator.nextPhaseLambda.invoke(
                    this.phasePhactory, this.gameCycle.getPlayerToGoNext( game )
                )
                    .completeAndDoAsPriority()
            else
                this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
            this.maybePotentialRentCharge = movingAPiece.outcomeGenerator.maybePotentialRentCharge
        }

        override fun visit( payingFineOrTakingCard: PayingFineOrTakingCard ) {
            when ( payingFineOrTakingCard.outcome ) {
                PayFineOrTakeChanceOutcome.PAY_FINE -> this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
                PayFineOrTakeChanceOutcome.TAKE_CHANCE -> this.visit( payingFineOrTakingCard.cardReading )
            }
        }

        override fun visit( paymentDue: PaymentDue ) {
            if ( false == paymentDue.haveAllPaymentsBeenMade() )
                return
            this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
            paymentDue.bankruptciesPending.forEach {
                phasePhactory.bankruptcyProceedingsOwing(
                    playerWithTurn = paymentDue.playerWithTurn,
                    bankruptPlayer = it,
                    creditor = paymentDue.playerOwedMoney
                )
                    .doAsPriority()
            }
        }

        override fun visit( potentialPurchase: PotentialPurchase ) {
            if ( false == potentialPurchase.decidedToBuy )
                this.phasePhactory.auctionProperty(
                    phaseStatus.playerWithTurn, potentialPurchase.targetProperty
                )
                    .completeAndDoAsPriority()
            else
                this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
        }

        override fun visit( rollingForMove: RollingForMove ) {
            goToNextPhaseFromRollingForMoveOutcome( rollingForMove.playerWithTurn, rollingForMove.result )
                .completeAndDoDuringThisTurn()
        }

        override fun visit( rollingForMoveFromJail: RollingForMoveFromJail) {
            when ( rollingForMoveFromJail.result.jailOutcome ) {
                JailOutcome.FORCED_TO_PAY_FINE -> {
                    this.phasePhactory.paymentDueToBank(
                        rollingForMoveFromJail.playerWithTurn,
                        rollingForMoveFromJail.playerWithTurn,
                        "jail fine",
                        rollingForMoveFromJail.jailFine
                    )
                        .completeAndDoAsPriority()
                    this.goToNextPhaseFromRollingForMoveOutcome(
                        rollingForMoveFromJail.playerWithTurn,
                        rollingForMoveFromJail.result.rollForMoveResult
                    )
                        .doPhaseDuringThisTurn()
                }
                JailOutcome.PAID_FINE_VOLUNTARILY ->
                    phasePhactory.rollingForMove( rollingForMoveFromJail.playerWithTurn )
                        .completeAndDoDuringThisTurn()
                JailOutcome.REMAIN_IN_JAIL ->
                    this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
                JailOutcome.ROLLED_A_DOUBLE ->
                    this.goToNextPhaseFromRollingForMoveOutcome(
                        rollingForMoveFromJail.playerWithTurn,
                        rollingForMoveFromJail.result.rollForMoveResult
                    )
                        .completeAndDoDuringThisTurn()
                JailOutcome.USED_CARD ->
                    phasePhactory.rollingForMove( rollingForMoveFromJail.playerWithTurn )
                        .completeAndDoDuringThisTurn()
            }
        }

        private fun goToNextPhaseFromRollingForMoveOutcome( playerWithTurn: Player,
                                                            moveResult: RollForMoveResult ) : MovingAPiece {
            return this.phasePhactory.movingAPiece(
                playerWithTurn = playerWithTurn,
                destinationSquare = moveResult.destinationSquare
            )
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
                .let { game.startNewTurn( TurnStatus( it ) ) }
        }

        override fun visit( tradeBeingConsidered: TradeBeingConsidered ) {
            this.phaseStatus.completeCurrentPhaseAndDoAnyWaiting()
            if ( tradeBeingConsidered.answer == true ) {
                this.payMortgageInterestOnAssets(
                    tradeBeingConsidered.offer.offered, tradeBeingConsidered.offer.offerRecipient, tradeBeingConsidered.playerWithTurn
                )
                this.payMortgageInterestOnAssets(
                    tradeBeingConsidered.offer.wanted, tradeBeingConsidered.offer.offeringPlayer, tradeBeingConsidered.playerWithTurn
                )
            }
        }

        private fun payMortgageInterestOnAssets( assets: Assets, payingPlayer: Player, playerWithTurn: Player ) {
            assets.forEachMortgaged {
                this.phasePhactory.dealWithMortgageOnTransfer(
                    playerWithTurn = playerWithTurn,
                    propertyOwner = payingPlayer,
                    property = it
                )
                    .doAsPriority()
            }
        }

        private fun TurnBasedPhase.completeAndDoAsPriority() {
            phaseStatus.completeAndDoAsPriority( this )
        }

        private fun TurnBasedPhase.completeAndDoDuringThisTurn() {
            phaseStatus.completeAndDoDuringThisTurn( this )
        }

        private fun TurnBasedPhase.doAsPriority() {
            phaseStatus.doAsPriority( this )
        }

        private fun TurnBasedPhase.doPhaseDuringThisTurn() {
            phaseStatus.doPhaseDuringThisTurn( this )
        }

    }

}