package jacobs.tycoon.domain

import jacobs.tycoon.domain.actions.cards.PayFineOrTakeCardDecision
import jacobs.tycoon.domain.actions.property.BuildingProject
import jacobs.tycoon.domain.actions.property.MortgageOnTransferDecision
import jacobs.tycoon.domain.phases.results.MoveResult
import jacobs.tycoon.domain.phases.results.ReadCardResult
import jacobs.tycoon.domain.phases.results.RentChargeResult
import jacobs.tycoon.domain.phases.results.RollForMoveResult
import jacobs.tycoon.domain.phases.results.RollForOrderResult
import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.bank.Bank
import jacobs.tycoon.domain.board.Board
import jacobs.tycoon.domain.board.cards.Card
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.Dice
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.AcceptingFunds
import jacobs.tycoon.domain.phases.AuctionProperty
import jacobs.tycoon.domain.phases.BankruptcyProceedings
import jacobs.tycoon.domain.services.auction.AuctionPhase
import jacobs.tycoon.domain.phases.CardReading
import jacobs.tycoon.domain.phases.CrownTheVictor
import jacobs.tycoon.domain.phases.DealingWithMortgageInterestOnTransfer
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.MovingAPiece
import jacobs.tycoon.domain.phases.NotTurnOfPlayerException
import jacobs.tycoon.domain.phases.PayingFineOrTakingCard
import jacobs.tycoon.domain.phases.PaymentDue
import jacobs.tycoon.domain.phases.status.PhaseStatus
import jacobs.tycoon.domain.phases.PotentialPurchase
import jacobs.tycoon.domain.phases.rent.PotentialRentCharge
import jacobs.tycoon.domain.phases.RollingForMove
import jacobs.tycoon.domain.phases.RollingForMoveFromJail
import jacobs.tycoon.domain.phases.RollingForOrder
import jacobs.tycoon.domain.phases.SignUp
import jacobs.tycoon.domain.phases.TradeBeingConsidered
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.phases.status.TurnStatus
import jacobs.tycoon.domain.phases.status.TurnlessPhaseStatus
import jacobs.tycoon.domain.phases.WrongPhaseException
import jacobs.tycoon.domain.phases.results.RollForMoveFromJailResult
import jacobs.tycoon.domain.pieces.PlayingPiece
import jacobs.tycoon.domain.players.GamePlayers
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.players.SeatingPosition
import jacobs.tycoon.domain.rules.JailRules
import jacobs.tycoon.domain.rules.MiscellaneousRules
import jacobs.tycoon.domain.services.GameCycle
import jacobs.tycoon.domain.services.auction.AuctionStatus
import kotlin.reflect.KClass

/**
 * Class is *NOT* designed to be thread safe. It is intended that only one coroutine at a time will access the game.
 * Monopoly isn't exactly a fast-paced game now is it?
 */
class Game(
    val bank: Bank,
    private val rules: MiscellaneousRules,
    private val jailRules: JailRules
) {

    lateinit var board: Board
    private val dice = Dice()
    @PublishedApi internal var phaseStatus: PhaseStatus = TurnlessPhaseStatus( SignUp )
    val players: GamePlayers = GamePlayers()

    /**
     * *** 1. UPDATING GAME STATE API AS MAIN GAME ACTION ***
     */

    fun acceptFunds( gameCycle: GameCycle, actorPosition: SeatingPosition ): Boolean {
        return gameCycle.doOnTurnPhaseAndCycle < AcceptingFunds, Boolean > ( this, actorPosition ) {
            this.acceptFunds()
            true
        }
    }

    fun addPlayer( possibleNewPlayer: Player ): Boolean {
        if ( canGivenNewPlayerJoin( possibleNewPlayer ) )
            return players.addPlayer( possibleNewPlayer )
        else
            return false
    }

    fun attemptToPay( gameCycle: GameCycle, position: SeatingPosition ): Boolean {
        return gameCycle.doInPhaseAndCycle < PaymentDue, Boolean > ( this ) {
            val requestingPlayer = position.player()
            if ( false == this.doesPlayerStillOweMoney( requestingPlayer ) )
                throw NotTurnOfPlayerException( "Requesting player ${ requestingPlayer.name } does not owe money" )
            this.attemptPayment( requestingPlayer )
            true
        }
    }

    fun bidFromPosition( amount: CurrencyAmount, actorPosition: SeatingPosition ): Boolean {
        return doInPhase < AuctionProperty, Boolean > {
            this.makeBid( amount, actorPosition.player() )
        }
    }

    fun build( project: BuildingProject ): Boolean {
        if ( project.isValid() == false )
            return false
        project.carryOut()
        return true
    }

    fun carryOutBankruptcyProceedings( gameCycle: GameCycle ): Boolean {
        return gameCycle.doInPhaseAndCycle < BankruptcyProceedings, Boolean > ( this ) {
            this.carryOutProceedings( it )
            true
        }
    }

    fun chargeRent( gameCycle: GameCycle, property: Property, actorPosition: SeatingPosition ): RentChargeResult {
        val rentCharge = getPotentialRentChargeOrNullGivenProperty( actorPosition.player(), property )
            ?: throw NotTurnOfPlayerException( "Invalid rent charge attempt" )
        val result = rentCharge.recordRentCharge( actorPosition.player() )
        gameCycle.dealWithRentCharge( result, this )
        return result
    }

    fun completePieceMove( gameCycle: GameCycle, position: SeatingPosition ): MoveResult {
        return gameCycle.doOnTurnPhaseAndCycle < MovingAPiece, MoveResult > ( this, position ) {
            this.carryOutMove( it )
        }
    }

    fun completeSignUp( gameCycle: GameCycle ) {
        gameCycle.doInPhaseAndCycle < SignUp, Unit > ( this ) {
            this.completeSignUp( it )
        }
    }

    fun concludeAuction( gameCycle: GameCycle ): AuctionStatus {
        return gameCycle.doInPhaseAndCycle < AuctionProperty, AuctionStatus > ( this ) {
            this.concludeAuction()
            this.auction.status
        }
    }

    fun mortgageProperty( property: Property, actorPosition: SeatingPosition ): Boolean {
        if ( property.isMortgaged() || actorPosition.player().owns( property ) == false )
            return false
        property.takeOutMortgage()
        actorPosition.player().creditFunds( property.mortgagedValue() )
        return true
    }

    fun offerTrade( gameCycle: GameCycle, tradeOffer: TradeOffer, actorPosition: SeatingPosition ): Boolean {
        if ( actorPosition.player() != tradeOffer.offeringPlayer )
            return false
        else
            return gameCycle.startNewTradingPhase( tradeOffer, this )
    }

    fun payFineOrTakeCard( gameCycle: GameCycle, decision: PayFineOrTakeCardDecision,
                           actorPosition: SeatingPosition ): Boolean {
        return gameCycle.doOnTurnPhaseAndCycle < PayingFineOrTakingCard, Boolean > ( this, actorPosition ) {
            this.carryOutDecision( decision )
            true
        }
    }

    fun payJailFineVoluntarily( gameCycle: GameCycle, seatingPosition: SeatingPosition ): Boolean {
        return gameCycle.doOnTurnPhaseAndCycle < RollingForMoveFromJail, Boolean > ( this, seatingPosition ) {
            this.payFine( this@Game )
            true
        }
    }

    fun payOffMortgage( property: Property, actorPosition: SeatingPosition ): Boolean {
        if ( false == property.isMortgaged() || actorPosition.player().owns( property ) == false )
            return false
        property.payOffMortgage()
        actorPosition.player().debitFunds( property.mortgagePlusInterest( bank.interestRate ) )
        return true
    }

    fun payOffMortgageOnTransfer( gameCycle: GameCycle, decision: MortgageOnTransferDecision, actorPosition: SeatingPosition ): Boolean {
        return gameCycle.doInPhaseAndCycle < DealingWithMortgageInterestOnTransfer, Boolean > ( this ) {
            if ( false == this.doesOwnProperty( actorPosition.getPlayer( it ) ) )
                throw NotTurnOfPlayerException( "Not turn of ${ actorPosition.getPlayer( it ) } to pay off mortgage in transfer" )
            this.dealWithMortgage( decision, it )
            true
        }
    }

    fun playGetOutOfJailFreeCard( actorPosition: SeatingPosition ): Boolean {
        return this.doInPhaseOrElse < RollingForMoveFromJail, Boolean > ( false ) {
            if ( actorPosition.player().hasGetOutOfJailFreeCard() ) {
                this.useGetOutOfJailFreeCard()
                actorPosition.player().returnGetOutOfJailFreeCard()
                true
            }
            else
                false
        }
    }

    fun readCard( gameCycle: GameCycle, position: SeatingPosition ): ReadCardResult {
        return gameCycle.doOnTurnPhaseAndCycle < CardReading, ReadCardResult > ( this, position ) {
            this.readCard()
        }
    }

    fun respondToPropertyOffer(gameCycle: GameCycle, decidedToBuy: Boolean, position: SeatingPosition ) {
        return gameCycle.doOnTurnPhaseAndCycle < PotentialPurchase, Unit > ( this, position ) {
            this.respondToOffer( decidedToBuy )
        }
    }

    fun respondToTradeOffer( gameCycle: GameCycle, response: Boolean,
                             actorPosition: SeatingPosition ): Boolean {
        return gameCycle.doInPhaseAndCycle < TradeBeingConsidered, Boolean > ( this ) {
            if ( false == this.isPlayerWhoReceivedOffer( actorPosition.player() ) )
                throw NotTurnOfPlayerException( "${ actorPosition.player() } can't respond to the offer"  )
            this.respondToTrade( response )
            true
        }
    }

    fun rollTheDiceForMove( gameCycle: GameCycle, position: SeatingPosition, maybeDiceRoll: DiceRoll? = null )
            : RollForMoveResult {
        return gameCycle.doOnTurnPhaseAndCycle < RollingForMove, RollForMoveResult > ( this, position ) {
            this.setDiceRoll( dice.roll( maybeDiceRoll ) )
            this.getRollResult( it )
        }
    }

    fun rollTheDiceFromJailForMove( gameCycle: GameCycle, position: SeatingPosition,
                                    maybeDiceRoll: DiceRoll? ): RollForMoveFromJailResult {
        return gameCycle.doOnTurnPhaseAndCycle < RollingForMoveFromJail, RollForMoveFromJailResult > ( this, position ) {
            this.setDiceRoll( dice.roll( maybeDiceRoll ) )
            this.decideResultOfDiceRoll( it )
        }
    }

    fun rollTheDiceForThrowingOrder( gameCycle: GameCycle, position: SeatingPosition,
                                    maybeDiceRoll: DiceRoll? = null ) : RollForOrderResult {
        return gameCycle.doOnTurnPhaseAndCycle < RollingForOrder, RollForOrderResult > ( this, position ) {
            this.setDiceRoll( dice.roll( maybeDiceRoll ) )
            this.noteDiceRoll( it )
        }
    }

    fun sellProperties( project: BuildingProject ): Boolean {
        if ( project.isValid() == false )
            return false
        project.carryOut()
        return true
    }

    fun updateAuctionProceedings(auctionPhase: AuctionPhase) : Boolean {
        return doInPhase < AuctionProperty, Boolean > {
            this.updateAuction( auctionPhase )
        }
    }

    fun useGetOutOfJailFreeCard( gameCycle: GameCycle, seatingPosition: SeatingPosition ): Boolean {
        return gameCycle.doOnTurnPhaseAndCycle < RollingForMoveFromJail, Boolean > ( this, seatingPosition ) {
            this.useGetOutOfJailFreeCard()
            true
        }
    }

    // 2. UPDATING GAME STATE OTHERWISE

    fun moveAllPiecesToStartingSquare() {
        this.board.pieceSet.moveAllToSquare( this.board.startingSquare() )
    }

    fun startNewTurn( phaseStatus: TurnStatus ) {
        this.phaseStatus = phaseStatus
    }

    // 3. INTERROGATING GAME STATE API TODO COULD SEPARATE THIS OUT

    inline fun < reified T : TurnBasedPhase > anyPastPhasesOfTypeInTurn(): Boolean {
        val turnStatus = phaseStatus as? TurnStatus ?: return false
        return turnStatus.anyPastPhasesOfType < T > ()
    }

    fun canGameStart(): Boolean {
        return doInPhaseOrElse < SignUp, Boolean > ( false ) {
            players.countAll() >= rules.minimumNumberOfPlayers
        }
    }

    fun canAnyNewPlayerJoin(): Boolean {
        return doInPhaseOrElse < SignUp, Boolean > ( false ) {
            players.countAll() < board.getPieceCount()
        }
    }

    fun canPlayerChargeRent( player: Player ): Boolean {
        return this.getPotentialRentChargeOrNull( player ) != null
    }

    fun canPlayerPayJailFine( player: Player ): Boolean {
        return doInPhase < RollingForMoveFromJail, Boolean > {
            player.cashHoldings < jailRules.leaveJailFineAmount
        }
    }

    fun getAmountOfPaymentDue(): CurrencyAmount {
        return this.doInPhase < PaymentDue, CurrencyAmount > {
            this.amountDue
        }
    }

    fun getAuctionStatus(): AuctionStatus {
        return this.doInPhase < AuctionProperty, AuctionStatus > {
            this.auction.status
        }
    }

    fun getAvailablePieces(): List < PlayingPiece > {
        return this.board.pieceSet.getAvailablePieces( players )
    }

    fun getBankruptPlayer(): Player {
        return this.doInPhase < BankruptcyProceedings, Player > {
            this.bankruptPlayer
        }
    }

    fun getPaymentReason(): String {
        return this.doInPhase < PaymentDue, String > {
            this.reason
        }
    }

    fun getCardBeingRead(): Card {
        return this.doInPhaseOrPastPhasesOfTurn < CardReading, Card > { this.card }
    }

    fun getJailFine(): CurrencyAmount {
        return jailRules.leaveJailFineAmount
    }

    fun getLastRoll(): DiceRoll {
        return this.dice.lastRoll
    }

    fun getOfferBeingConsidered(): TradeOffer {
        return doInPhase < TradeBeingConsidered, TradeOffer > {
            this.offer
        }
    }

    fun getTheWinner(): Player {
        return doInPhase < CrownTheVictor, Player > {
            this.playerWithTurn
        }
    }

    fun haveDiceBeenRolled(): Boolean {
        return this.dice.haveBeenRolled()
    }

    fun isGameUnderway(): Boolean {
        return false == ( this.isPhase < SignUp >() || this.isPhase < RollingForOrder >() )
    }

    fun isPaymentDueFromPlayer( player: Player): Boolean {
        return doInPhaseOrElse < PaymentDue, Boolean > ( false ) {
            this.doesPlayerStillOweMoney( player )
        }
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

    fun isTradeBeingConsideredBy( player: Player ): Boolean {
        return doInPhaseOrElse < TradeBeingConsidered, Boolean > ( false ) {
            this.isPlayerWhoReceivedOffer( player )
        }
    }

    fun isTradeBeingConsideredBySomeoneOtherThan( player: Player ): Boolean {
        return doInPhaseOrElse < TradeBeingConsidered, Boolean > ( false ) {
            false == this.isPlayerWhoReceivedOffer( player )
        }
    }

    fun isTurnOfPlayer( testPlayer: Player ): Boolean {
        return this.phaseStatus.isItTurnOfPlayer( testPlayer )
    }

    fun propertiesOnWhichPlayerCanChargeRent( player: Player ): List < Property > {
        val possibleRentCharges = ( this.phaseStatus as TurnStatus ).potentialRentCharges
        return possibleRentCharges.filter { it.canPlayerChargeRentOnProperty( player ) }
            .map { it.occupiedProperty }
    }

    // 4. HELPER METHODS

    private fun canGivenNewPlayerJoin( player: Player ): Boolean {
        return this.canAnyNewPlayerJoin() &&
            false == this.players.isThereAPlayerOfName( player.name ) &&
            player.name != ""
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

    private fun < T > doWhenTurnBasedOrElse( elseReturn: T, action: TurnStatus.() -> T ): T {
        val turnStatus = this.phaseStatus as? TurnStatus ?: return elseReturn
        return turnStatus.action()
    }

    private fun getPotentialRentChargeOrNullGivenProperty( player: Player, property: Property ): PotentialRentCharge? {
        return doWhenTurnBasedOrElse( null ) {
            potentialRentCharges.firstOrNull {
                it.canPlayerChargeRentOnGivenProperty( player, property )
            }
        }
    }

    private fun getPotentialRentChargeOrNull( player: Player ): PotentialRentCharge? {
        return doWhenTurnBasedOrElse( null ) {
            potentialRentCharges.firstOrNull {
                it.canPlayerChargeRentOnProperty( player )
            }
        }
    }

    private fun throwWrongPhaseException( expected: KClass < out GamePhase >, actual: KClass < out GamePhase > ): Nothing {
        throw WrongPhaseException( "Expected ${ expected.simpleName } got ${ actual.simpleName } ")
    }

    private fun SeatingPosition.player(): Player {
        return this.getPlayer( this@Game )
    }

}