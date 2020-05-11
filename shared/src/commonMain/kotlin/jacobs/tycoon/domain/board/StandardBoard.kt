package jacobs.tycoon.domain.board

import jacobs.tycoon.domain.board.cards.CardSet
import jacobs.tycoon.domain.board.cards.ChanceCards
import jacobs.tycoon.domain.board.cards.CommunityChestCards
import jacobs.tycoon.domain.board.colourgroups.StandardColourGroups
import jacobs.tycoon.domain.board.squares.CardSquare
import jacobs.tycoon.domain.board.squares.FreeParkingSquare
import jacobs.tycoon.domain.board.squares.GoSquare
import jacobs.tycoon.domain.board.squares.GoToJailSquare
import jacobs.tycoon.domain.board.squares.JailSquare
import jacobs.tycoon.domain.board.squares.JustVisitingJailSquare
import jacobs.tycoon.domain.board.squares.Square
import jacobs.tycoon.domain.board.squares.Station
import jacobs.tycoon.domain.board.squares.Street
import jacobs.tycoon.domain.board.squares.TaxSquare
import jacobs.tycoon.domain.board.squares.Utility
import jacobs.tycoon.domain.board.squares.UtilityMultipliers
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Source: http://www.jdawiseman.com/papers/trivia/monopoly-rents.html
 */
@Serializable
abstract class StandardBoard : Board() {

    companion object {
        const val BASE_STATION_RENT = 25
        const val STATION_PRICE = 200
        const val UTILITY_PRICE = 150
        val UTILITY_MULTIPLIERS_PAIR = 4 to 10
    }

    @Transient
    override val cardSets = listOf( CommunityChestCards(), ChanceCards() )
    @Transient
    private val colourGroups = StandardColourGroups
    @Transient
    override var goSquare: GoSquare = GoSquare.NULL
    @Transient
    override var jailSquare: JailSquare = JailSquare.NULL

    @Transient
    private var cardSetCounter = 0

    override fun buildSquareList(): List < Square > {
        return this.nameListProvider().mapIndexed { index, eachName ->
            this.getNamelessSquareMapList()[ index ]( index, eachName ) }
    }

    protected abstract fun nameListProvider(): List < String >

        // Means we get alternating choices of cards. This rather curious mechanism is to
        // keep card sets as the same object instances but also refer to them in a board
        // context as well as a square context
    private fun getSetOfCards(): CardSet {
        return this.cardSets[ cardSetCounter++.rem( this.cardSets.size ) ]
    }

    private fun utilityMultipliers(): UtilityMultipliers {
        return UtilityMultipliers.new( UTILITY_MULTIPLIERS_PAIR, currency )
    }

    private fun getNamelessSquareMapList(): List < ( Int, String ) -> Square > {
        return listOf(

            // FIRST ROW

            { index, name ->
                goSquare = GoSquare( indexOnBoard = index, name = name, creditAmount = 200.toCurrency() )
                goSquare
            },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 60.toCurrency(),
                    colourGroup = colourGroups.BROWN
                ) },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSet = getSetOfCards()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 60.toCurrency(),
                    colourGroup = colourGroups.BROWN
                ) },
            { index, name -> TaxSquare( indexOnBoard = index, name = name, taxCharge = 200.toCurrency()) },

            { index, name ->
                Station(
                    indexOnBoard = index,
                    name = name,
                    listPrice = STATION_PRICE.toCurrency(),
                    singleStationRent = BASE_STATION_RENT.toCurrency()
                )
            },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 100.toCurrency(),
                    colourGroup = colourGroups.LIGHT_BLUE
                ) },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSet = getSetOfCards()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 100.toCurrency(),
                    colourGroup = colourGroups.LIGHT_BLUE

                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 120.toCurrency(),
                    colourGroup = colourGroups.LIGHT_BLUE
                ) },

            { index, _ -> JustVisitingJailSquare( indexOnBoard = index )
                            .also { this.jailSquare = JailSquare( it.indexOnBoard ) }
            },

            // END OF FIRST ROW
            // LEFT-HAND SIDE

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 140.toCurrency(),
                    colourGroup = colourGroups.PINK
                ) },
            { index, name ->
                Utility(
                    indexOnBoard = index,
                    name = name,
                    listPrice = UTILITY_PRICE.toCurrency(),
                    utilityMultipliers = utilityMultipliers()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 140.toCurrency(),
                    colourGroup = colourGroups.PINK
                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 160.toCurrency(),
                    colourGroup = colourGroups.PINK
                ) },

            { index, name ->
                Station(
                    indexOnBoard = index,
                    name = name,
                    listPrice = STATION_PRICE.toCurrency(),
                    singleStationRent = BASE_STATION_RENT.toCurrency()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 180.toCurrency(),
                    colourGroup = colourGroups.ORANGE
                ) },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSet = getSetOfCards()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 180.toCurrency(),
                    colourGroup = colourGroups.ORANGE
                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 200.toCurrency(),
                    colourGroup = colourGroups.ORANGE
                ) },

            { index, name -> FreeParkingSquare( indexOnBoard = index, name = name ) },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 220.toCurrency(),
                    colourGroup = colourGroups.RED
                ) },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSet = getSetOfCards()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 220.toCurrency(),
                    colourGroup = colourGroups.RED
                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 240.toCurrency(),
                    colourGroup = colourGroups.RED
                ) },

            { index, name ->
                Station(
                    indexOnBoard = index,
                    name = name,
                    listPrice = STATION_PRICE.toCurrency(),
                    singleStationRent = BASE_STATION_RENT.toCurrency()
                )
            },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 260.toCurrency(),
                    colourGroup = colourGroups.YELLOW
                ) },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 260.toCurrency(),
                    colourGroup = colourGroups.YELLOW
                ) },
            { index, name ->
                Utility(
                    indexOnBoard = index,
                    name = name,
                    listPrice = UTILITY_PRICE.toCurrency(),
                    utilityMultipliers = utilityMultipliers()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 280.toCurrency(),
                    colourGroup = colourGroups.YELLOW
                )
            },

            { index, _ -> GoToJailSquare( indexOnBoard = index ) },

            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 300.toCurrency(),
                    colourGroup = colourGroups.GREEN
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 300.toCurrency(),
                    colourGroup = colourGroups.GREEN
                )
            },
            { index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSet = getSetOfCards()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 320.toCurrency(),
                    colourGroup = colourGroups.GREEN
                )
            },

            { index, name ->
                Station(
                    indexOnBoard = index,
                    name = name,
                    listPrice = STATION_PRICE.toCurrency(),
                    singleStationRent = BASE_STATION_RENT.toCurrency()
                )
            },

            {
                index, name ->
                CardSquare(
                    indexOnBoard = index,
                    name = name,
                    cardSet = getSetOfCards()
                )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 350.toCurrency(),
                    colourGroup = colourGroups.DARK_BLUE
                )
            },
            {
                index, name -> TaxSquare( indexOnBoard = index, name = name, taxCharge = 150.toCurrency() )
            },
            { index, name ->
                Street(
                    indexOnBoard = index,
                    name = name,
                    listPrice = 400.toCurrency(),
                    colourGroup = colourGroups.DARK_BLUE
                )
            }
        )
    }

}

