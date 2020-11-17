package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.board.cards.CardSet
import jacobs.tycoon.domain.board.currency.CurrencyAmount
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.phases.rent.PotentialRentCharge
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.JailRules
import jacobs.tycoon.domain.rules.MiscellaneousRules
import jacobs.tycoon.domain.services.auction.Auctioneer
import jacobs.tycoon.state.GameState
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PhasePhactory( kodein: Kodein ) {

    private val auctioneer by kodein.instance < Auctioneer > ()
    private val gameState by kodein.instance < GameState > ()
    private val jailRules by kodein.instance < JailRules > ()
    private val miscellaneousRules by kodein.instance < MiscellaneousRules > ()

    fun acceptFunds( playerWithTurn: Player, amountDue: CurrencyAmount ): AcceptingFunds {
        return AcceptingFunds( playerWithTurn, amountDue )
    }

    fun auctionProperty( playerWithTurn: Player, property: Property ): AuctionProperty {
        return AuctionProperty(
            playerWithTurn = playerWithTurn,
            property = property,
            auctioneer = auctioneer
        )
    }

    fun bankruptcyProceedingsOwing(
        playerWithTurn: Player, bankruptPlayer: Player, creditor: Player?
    ): BankruptcyProceedings {
        return BankruptcyProceedings(
            playerWithTurn = playerWithTurn,
            bankruptPlayer = bankruptPlayer,
            creditor = creditor
        )
    }

    fun cardReading( playerWithTurn: Player, square: CardSquare ): CardReading {
        return CardReading( playerWithTurn ) { square.getNextCard( gameState.game().board ) }
    }

    private fun cardReading( playerWithTurn: Player, cardSet: CardSet ): CardReading {
        return CardReading( playerWithTurn ) { cardSet.drawCard() }
    }

    fun continueRollingForOrder( rollResults: MutableMap < Player, DiceRoll? > ): RollingForOrder {
        return RollingForOrder(
            playerWithTurn = rollResults.entries.first { it.value == null }.key,
            rollResults = rollResults
        )
    }

    fun crownTheVictor( theWinner: Player ): TurnBasedPhase {
        return CrownTheVictor( theWinner )
    }

    fun dealWithMortgageOnTransfer( playerWithTurn: Player, propertyOwner: Player, property: Property )
            : DealingWithMortgageInterestOnTransfer {
        return DealingWithMortgageInterestOnTransfer( playerWithTurn, propertyOwner, property, this )
    }

    fun movingAPiece( playerWithTurn: Player, destinationSquare: Square ): MovingAPiece {
        return MovingAPiece(
            playerWithTurn = playerWithTurn,
            destinationSquare = destinationSquare,
            goCreditAmount = miscellaneousRules.goCreditAmount
        )
    }

    fun movingAPieceNotViaGo( playerWithTurn: Player, destinationSquare: Square ): MovingAPiece {
        return MovingAPiece(
            playerWithTurn = playerWithTurn,
            destinationSquare = destinationSquare,
            goCreditAmount = null
        )
    }

    fun offerTrade( playerWithTurn: Player, tradeOffer: TradeOffer ): TradeBeingConsidered {
        return TradeBeingConsidered( playerWithTurn, tradeOffer )
    }

    fun payFineOrTakeCard( playerWithTurn: Player, fineAmount: CurrencyAmount, cardSet: CardSet ): PayingFineOrTakingCard {
        return PayingFineOrTakingCard(
            playerWithTurn = playerWithTurn,
            paymentDue = this.paymentDueToBank( playerWithTurn, playerWithTurn, "jail fine", fineAmount ),
            cardReading = this.cardReading( playerWithTurn, cardSet )
        )
    }

    fun paymentDue( playerWithTurn: Player, playingOwingMoney: Player, amountDue: CurrencyAmount, reason: String,
                    playerOwedMoney: Player ) : PaymentDue{
        return PaymentDue( playerWithTurn, playingOwingMoney, amountDue, reason, playerOwedMoney )
    }

    fun paymentsDueFromAll( playerWithTurn: Player, playerOwedMoney: Player, amountDue: CurrencyAmount, reason: String )
        : PaymentDue {
        return PaymentDue( playerWithTurn, this.getAllPlayersExcept( playerOwedMoney ), amountDue, reason, playerOwedMoney )
    }

    fun paymentDueToBank( playerWithTurn: Player, playerOwingMoney: Player, reason: String, amountDue: CurrencyAmount ): PaymentDue {
        return PaymentDue( playerWithTurn, playerOwingMoney, amountDue, reason, null )
    }

    fun potentialPurchase( playerWithTurn: Player, property: Property ): PotentialPurchase {
        return PotentialPurchase( playerWithTurn, property )
    }

    fun rollingForMove( playerToRollNext: Player ): RollingForMove {
        return RollingForMove( playerToRollNext, jailRules )
    }

    fun rollingForMoveFromJail( playerToRollNext: Player ): RollingForMoveFromJail {
        return RollingForMoveFromJail( playerToRollNext, jailRules )
    }

    fun startRollingForOrder( playerCollection: List < Player > ): RollingForOrder {
        return RollingForOrder(
            playerWithTurn = playerCollection.first(),
            rollResults = playerCollection.associateWith { null }.toMutableMap()
        )
    }

    fun streetRepairs( playerWithTurn: Player, perHouse: CurrencyAmount, perHotel: CurrencyAmount )
            : StreetRepairs {
        return StreetRepairs( playerWithTurn, perHouse, perHotel )
    }

    private fun getAllPlayersExcept( player: Player ): List < Player > {
        return gameState.game().players.playersToLeftExcluding( player )
    }

}