package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.board.squares.Property
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.dice.DiceRoll
import jacobs.tycoon.domain.players.Player
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class PhasePhactory( kodein: Kodein ) {

    private val currency by kodein.instance < Currency > ()
    private val goCreditAmount by kodein.instance < Int > ( tag = "goCreditAmount" )

    fun auctionProperty( playerWithTurn: Player, property: Property ): AuctionProperty {
        return AuctionProperty(
            playerWithTurn = playerWithTurn,
            targetProperty = property
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

    fun movingAPiece( playerWithTurn: Player, destinationSquare: Square ): MovingAPiece {
        return MovingAPiece(
            playerWithTurn = playerWithTurn,
            destinationSquare = destinationSquare,
            goCreditAmount = currency.ofAmount( goCreditAmount )
        )
    }

    fun movingAPieceNotViaGo( playerWithTurn: Player, destinationSquare: Square ): MovingAPiece {
        return MovingAPiece(
            playerWithTurn = playerWithTurn,
            destinationSquare = destinationSquare,
            goCreditAmount = currency.ofAmount( 0 )
        )
    }

    fun potentialPurchase( playerWithTurn: Player, property: Property ): PotentialPurchase {
        return PotentialPurchase( playerWithTurn, property )
    }

    fun potentialRentCharge( playerWithTurn: Player, playerWithTurnStarting: Player, property: Property )
            : PotentialRentCharge {
        return PotentialRentCharge(
            playerOccupyingProperty = playerWithTurn,
            playerWithTurnStarting = playerWithTurnStarting,
            occupiedProperty = property
        )
    }

    fun rollingForMove( playerToRollNext: Player ): RollingForMove {
        return RollingForMove( playerToRollNext )
    }

    fun startRollingForOrder( playerCollection: Collection < Player > ): RollingForOrder {
        val players = playerCollection.sorted()
        return RollingForOrder(
            playerWithTurn = players.first(),
            rollResults = players.associateWith { null }.toMutableMap()
        )
    }

}