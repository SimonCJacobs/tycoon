package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.board.cards.ChanceCards
import jacobs.tycoon.domain.board.cards.CommunityChestCards
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.PoundsSterling
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.board.squares.FreeParkingSquare
import jacobs.tycoon.domain.board.squares.GoSquare
import jacobs.tycoon.domain.board.squares.GoToJailSquare
import jacobs.tycoon.domain.board.squares.JustVisitingJailSquare
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.TaxSquare
import jacobs.tycoon.domain.board.squares.Utility
import kotlinx.serialization.Serializable

/**
 * Source: http://www.jdawiseman.com/papers/trivia/monopoly-rents.html
 */
@Serializable
abstract class StandardBoard : Board() {

    companion object {
        const val STATION_PRICE = 200
        const val UTILITY_PRICE = 150
    }

    protected fun buildSquareList( nameList: List < String > ): List <Square> {
        return nameList.mapIndexed { index, eachName ->
            this.getNamelessSquareMapList()[ index ]( index, eachName ) }
    }

    private fun getNamelessSquareMapList(): List < ( Int, String ) -> Square > {
        val chanceCards = ChanceCards()
        val communityChestCards = CommunityChestCards()
        return listOf(

            { index, name ->
                GoSquare( indexOnBoard = index, name = name, creditAmount = 200.toCurrency())
            },

            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 60.toCurrency()) },
            { index, name ->
                CardSquare( indexOnBoard = index, name = name, cardSet = communityChestCards ) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 60.toCurrency()) },
            { index, name ->
                TaxSquare( indexOnBoard = index, name = name, taxCharge = 200.toCurrency()) },

            { index, name ->
                Station( indexOnBoard = index, name = name, listPrice = STATION_PRICE.toCurrency()) },

            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 100.toCurrency()) },
            { index, name ->
                CardSquare( indexOnBoard = index, name = name, cardSet = chanceCards ) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 100.toCurrency()) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 120.toCurrency()) },

            { index, _ -> JustVisitingJailSquare( indexOnBoard = index ) },

            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 140.toCurrency()) },
            { index, name ->
                Utility( indexOnBoard = index, name = name, listPrice = UTILITY_PRICE.toCurrency()) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 140.toCurrency()) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 160.toCurrency()) },

            { index, name ->
                Station( indexOnBoard = index, name = name, listPrice = STATION_PRICE.toCurrency()) },

            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 180.toCurrency()) },
            { index, name ->
                CardSquare( indexOnBoard = index, name = name, cardSet = communityChestCards ) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 180.toCurrency()) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 200.toCurrency()) },

            { index, name -> FreeParkingSquare( indexOnBoard = index, name = name ) },

            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 220.toCurrency()) },
            { index, name ->
                CardSquare( indexOnBoard = index, name = name, cardSet = chanceCards ) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 220.toCurrency()) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 240.toCurrency()) },

            { index, name ->
                Station( indexOnBoard = index, name = name, listPrice = STATION_PRICE.toCurrency()) },

            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 260.toCurrency()) },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 260.toCurrency()) },
            { index, name ->
                Utility( indexOnBoard = index, name = name, listPrice = UTILITY_PRICE.toCurrency())
            },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 280.toCurrency())
            },

            { index, _ -> GoToJailSquare( indexOnBoard = index ) },

            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 300.toCurrency())
            },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 300.toCurrency())
            },
            { index, name ->
                CardSquare( indexOnBoard = index, name = name, cardSet = communityChestCards )
            },
            { index, name ->
                Street( indexOnBoard = index, name = name, listPrice = 320.toCurrency())
            },

            { index, name ->
                Station( indexOnBoard = index, name = name, listPrice = STATION_PRICE.toCurrency())
            },

            {
                index, name -> CardSquare( indexOnBoard = index, name = name, cardSet = chanceCards )
            },
            {
                index, name -> Street( indexOnBoard = index, name = name, listPrice = 350.toCurrency())
            },
            {
                index, name -> TaxSquare( indexOnBoard = index, name = name, taxCharge = 150.toCurrency())
            },
            {
                index, name -> Street( indexOnBoard = index, name = name, listPrice = 400.toCurrency())
            }
        )
    }

}

