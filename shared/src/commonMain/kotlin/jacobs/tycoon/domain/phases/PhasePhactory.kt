package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.actions.trading.TradeOffer
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player
import jacobs.tycoon.domain.rules.JailRules
import jacobs.tycoon.domain.rules.MiscellaneousRules
import jacobs.tycoon.domain.services.auction.Auctioneer
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PhasePhactory( kodein: Kodein ) {

    private val auctioneer by kodein.instance < Auctioneer > ()
    private val currency by kodein.instance < Currency > ()
    private val jailRules by kodein.instance < JailRules > ()
    private val miscellaneousRules by kodein.instance < MiscellaneousRules > ()

    fun auctionProperty( playerWithTurn: Player, property: Property ): AuctionProperty {
        return AuctionProperty(
            playerWithTurn = playerWithTurn,
            property = property,
            auctioneer = auctioneer
        )
    }

    fun bankruptcyProceedings( playerWithTurn: Player, bankruptPlayer: Player ): BankruptcyProceedings {
        return BankruptcyProceedings(
            playerWithTurn = playerWithTurn,
            bankruptPlayer = bankruptPlayer
        )
    }

    fun cardReading( playerWithTurn: Player, square: CardSquare ): CardReading {
        return CardReading( playerWithTurn, square )
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
            goCreditAmount = currency.ofAmount( 0 )
        )
    }

    fun offerTrade( playerWithTurn: Player, tradeOffer: TradeOffer ): TradeBeingConsidered {
        return TradeBeingConsidered( playerWithTurn, tradeOffer )
    }

    fun potentialPurchase( playerWithTurn: Player, property: Property ): PotentialPurchase {
        return PotentialPurchase( playerWithTurn, property )
    }

    fun potentialRentCharge( playerWithTurn: Player, playerWithTurnStarting: Player, property: Property )
            : PotentialRentCharge {
        return PotentialRentCharge(
            playerOccupyingProperty = playerWithTurn,
            playerWithTurnStarting = playerWithTurnStarting,
            occupiedProperty = property,
            jailRules = jailRules
        )
    }

    fun rollingForMove( playerToRollNext: Player ): RollingForMove {
        return RollingForMove( playerToRollNext, jailRules )
    }

    fun rollingForMoveFromJail( playerToRollNext: Player ): RollingForMoveFromJail {
        return RollingForMoveFromJail( playerToRollNext, jailRules )
    }

    fun startRollingForOrder( playerCollection: Collection < Player > ): RollingForOrder {
        val players = playerCollection.sorted()
        return RollingForOrder(
            playerWithTurn = players.first(),
            rollResults = players.associateWith { null }.toMutableMap()
        )
    }

}